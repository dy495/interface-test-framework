package com.haisheng.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.testng.collections.Lists;
import java.util.List;



public class JsonpathUtil {


    private static org.slf4j.Logger logger = LoggerFactory.getLogger(JsonpathUtil.class);
    // 使用jackson的mapper对象将任意对象序列化为想要的类型
    private static ObjectMapper mapper = new ObjectMapper();


    /**
     * 返回json的key对应的String类型value
     * @param json
     * @param path
     * @return
     */
    public static String readValUsingJsonPath(String json, String path){
        if(json == null || path == null){
            return null;
        }
        try{

            Object val = JsonPath.read(json,  "$." + path);
            return val == null ? null :val.toString();
        }catch (Exception e){
            logger.error("jsonPath解析发生异常", e);
            return null;
        }
    }

    /**
     * 读出Json类型的String
     * @param json
     * @param path   jsonpath语法：https://km.sankuai.com/page/64183144
     * @return
     */
    public static String readJsonStrUsingJsonPath(String json, String path){
        if (json == null || path == null){
            return null;
        }
        try {
            Object jsonObject = JsonPath.read(json, "$." + path);
            //Object to JSON in String
            return mapper.writeValueAsString(jsonObject);

        } catch (Exception e) {
            logger.error("jsonPath解析发生异常, json={}", json, e);
            return null;
        }
    }

    public static String readJsonStrUsingJsonArray(String jsonArray, String path){
        if (jsonArray == null || path == null){
            return null;
        }
        try {
            Object jsonObject = JsonPath.read(jsonArray, "$" + path);
            //Object to JSON in String
            return mapper.writeValueAsString(jsonObject);

        } catch (Exception e) {
            logger.error("jsonPath解析发生异常, json={}", jsonArray, e);
            return null;
        }
    }

    /**
     * 返回所有数组中某个key的value字符串，并存放在list中
     * @param json
     * @param path
     * @return
     */
    public static List<String> readListUsingJsonPath(String json, String path){
        if (json == null || path == null){
            return null;
        }
        try {
            return JsonPath.read(json, "$." + path);
        } catch (Exception e) {
            logger.error("jsonPath解析发生异常", e);
            return null;
        }
    }

    /**
     * 返回所有数组中某个key的value，value是int类型，并存放在list中
     * @param json
     * @param path
     * @return
     */

    public static List<Integer> readIntListUsingJsonPath(String json, String path){
        if (json == null || path == null){
            return null;
        }
        try {
            return JsonPath.read(json, "$." + path);
        } catch (Exception e) {
            logger.error("jsonPath解析发生异常", e);
            return null;
        }
    }

    /**
     * 返回某个key中存放的对象并存放在list中
     * @param json
     * @param path
     * @return
     */
    public static List<Object> readListObjectUsingJsonPath(String json, String path){
        if (json == null || path == null){
            return null;
        }
        return JsonPath.read(json,"$." + path);
    }

    /**
     *
     * @param json
     * @param path
     * @return
     */
    public static List<String> readListJsonStrUsingJsonPath(String json, String path){
        if (json == null || path == null){
            return null;
        }
        List<Object> read = JsonPath.read(json, "$." + path);
        if (CollectionUtils.isEmpty(read)){
            return null;
        }
        List<String> ret = Lists.newArrayList();
        for (Object o : read) {
            try {
                ret.add(mapper.writeValueAsString(o));
            } catch (JsonProcessingException e) {
                continue;
            }
        }
        return ret;
    }

}
