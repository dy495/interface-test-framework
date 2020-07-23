package com.haisheng.framework.util.checker;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.experiment.except.CheckExcept;
import com.haisheng.framework.util.operator.EnumOperator;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Api值校验
 *
 * @author wangmin
 * @date 2020/7/20 10:55
 */
@Getter
public class ApiChecker implements IChecker {
    @Override
    public void check() {
        //todo 验证xml格式，暂时不做
        responseStructureCheck();
        // 验证字段取值
        if (!responseValueCheck()) {
            throw new CheckExcept(errorMsg.toString());
        }
    }

    public void checkPlus() {
        if (!responseValueCheckPlus()) {
            throw new CheckExcept(errorMsg.toString());
        }
    }

    @Override
    public String getErrorMsg() {
        return errorMsg.toString();
    }

    @Override
    public String getCheckerInfo() {
        return null;
    }

    /**
     * 返回值结构校验
     * 暂时不做
     */
    private void responseStructureCheck() {
        //todo 返回值接口校验逻辑待补充
//        String path = FileUtil.getResourcePath(xmlPath);
//        ResultChecker resultChecker = new ResultChecker.Builder().xmlFileName(path).build();
//        if (!resultChecker.checkRealResult(responseJson)) {
//            errorMsg.append(resultChecker.getErrorMsg()).append("origin log:\n");
//        }
    }

    /**
     * 返回值结果校验
     *
     * @return boolean
     */
    private boolean responseValueCheck() {
        checkEnumMap.forEach((key, value) -> value
                .forEach((opt, expect) -> getValueByPath(responseJson, key).forEach(actualValue -> {
                    if (!opt.compare(actualValue, expect)) {
                        errorMsg.append(String.format("json key=%s，判定错误，actual=%s，operator=%s，expect=%s\n",
                                key, actualValue, opt.name(), Arrays.toString(expect)));
                    }
                })));
        return errorMsg.length() <= 0;
    }

    /**
     * 两个返回值结果校验
     *
     * @return boolean
     */
    private boolean responseValueCheckPlus() {
        checkMap.forEach((key1, value) ->
                value.forEach((operator, key2) -> getValueByPath(responseJson1, key2)
                        .forEach(expect -> getValueByPath(responseJson, key1).forEach(actualValue -> {
                            if (!operator.compare(actualValue, expect)) {
                                errorMsg.append(String.format("json.key=%s，判定错误，actual=%s，operator=%s，expect=%s\n",
                                        key1, actualValue, operator.name(), expect));
                            }
                        }))
                ));
        return errorMsg.length() <= 0;
    }

    /**
     * 1.入参可以为纯字段名称，如code，此时对json进行递归搜索，返回全部code名称的字段值；<br/> 2.入参可以为包含$的具体jsonPath表达式，精准解析。
     *
     * @param jsonPath json表达式
     * @return List<Object>
     */
    private List<Object> getValueByPath(JSONObject responseJson, String jsonPath) {
        String json = responseJson.toJSONString();
        List<Object> valueList = new ArrayList<>();
        if (!jsonPath.contains(normalPath)) {
            jsonPath = "$.." + jsonPath;
        }
        try {
            try {
                valueList = JsonPath.read(json, jsonPath);
            } catch (ClassCastException e) {
                valueList.add(JsonPath.read(json, jsonPath));
            }
            if (valueList.isEmpty()) {
                errorMsg.append(jsonPath).append(" field can't find!\n");
            }
        } catch (Exception e) {
            errorMsg.append(e.getMessage());
        }
        return valueList;
    }

    private static final Logger logger = LoggerFactory.getLogger(ApiChecker.class);
    private final JSONObject responseJson;
    private final JSONObject responseJson1;
    //    private final String xmlPath;
    private final Map<String, Map<EnumOperator, Object[]>> checkEnumMap = new HashMap<>(32);
    private final Map<String, Map<EnumOperator, String>> checkMap = new HashMap<>(32);
    private final StringBuilder errorMsg = new StringBuilder();
    private final String normalPath = "$";

    protected ApiChecker(Builder builder) {
        this.responseJson = builder.responseJson;
        this.responseJson1 = builder.responseJson1;
//        this.xmlPath = builder.xmlPath;
        this.checkEnumMap.putAll(builder.checkEnumMap);
        this.checkMap.putAll(builder.checkMap);
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        protected static long checkerGlobalId = 1L;
        private JSONObject responseJson;
        private JSONObject responseJson1;
        //        private String xmlPath;
        private final Map<String, Map<EnumOperator, Object[]>> checkEnumMap = new HashMap<>(32);
        private final Map<String, Map<EnumOperator, String>> checkMap = new HashMap<>(32);

        /**
         * 进行相等值校验
         *
         * @param key   json字段或表达式
         * @param value 期望值
         * @return Builder
         */
        public <T> Builder equal(String key, T value) {
            check(key, EnumOperator.EQ, value);
            return this;
        }

        /**
         * 不为空校验时使用
         *
         * @param key json字段或表达式
         * @return Builder
         */
        public <T> Builder isNotNull(String key) {
            return check(key, EnumOperator.IS_NOT_NULL, (T) null);
        }

        /**
         * 进行值的验证
         *
         * @param key      json字段或表达式
         * @param operator 运算符对象
         * @param value    预期值
         * @return Builder
         */
        @SafeVarargs
        public final <T> Builder check(String key, EnumOperator operator, T... value) {
            if (!checkEnumMap.containsKey(key)) {
                checkEnumMap.put(key, new HashMap<>(16));
            }
            checkEnumMap.get(key).put(operator, value);
            return this;
        }

        /**
         * 多接口返回值校验
         *
         * @param key1     response1字段
         * @param operator 运算符对象
         * @param key2     response2字段
         * @param <T>      T
         * @return Builder
         */
        public final <T> Builder checkPlus(String key1, EnumOperator operator, String key2) {
            if (responseJson.isEmpty() || responseJson1.isEmpty()) {
                throw new CheckExcept("使用checkPlus需要response&&response1");
            }
            if (!checkMap.containsKey(key1) && !checkMap.containsKey(key2)) {
                checkMap.put(key1, new HashMap<>(16));
            }
            checkMap.get(key1).put(operator, key2);
            return this;
        }

        public ApiChecker build() {
            if (responseJson.isEmpty()) {
                throw new CheckExcept("接口响应值为空");
            }
            return new ApiChecker(this);
        }
    }
}
