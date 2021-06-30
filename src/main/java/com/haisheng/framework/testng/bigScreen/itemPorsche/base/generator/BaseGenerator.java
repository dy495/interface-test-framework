package com.haisheng.framework.testng.bigScreen.itemPorsche.base.generator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.IScene;
import com.haisheng.framework.util.CommonUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成器的抽象类
 *
 * @author wangmin
 * @date 2021/1/22 11:30
 */
public abstract class BaseGenerator implements IGenerator {
    protected static final Logger logger = LoggerFactory.getLogger(BaseGenerator.class);
    protected static final int SIZE = 100;
    protected final StringBuilder errorMsg;
    protected int counter = 0;
    protected VisitorProxy visitor;

    protected BaseGenerator(@NotNull BaseBuilder<?, ?> baseBuilder) {
        this.visitor = baseBuilder.visitor;
        this.errorMsg = new StringBuilder(16);
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg.toString();
    }

    @Override
    public abstract void execute(VisitorProxy visitor, IScene scene);

    public static abstract class BaseBuilder<T extends BaseBuilder<?, ?>, R> {
        private VisitorProxy visitor;

        /**
         * 传入访问者
         * 作为识别产品线的标识
         *
         * @param visitor visitor
         * @return AbstractBuilder
         */
        public T visitor(VisitorProxy visitor) {
            this.visitor = visitor;
            return (T) this;
        }

        /**
         * 初始产品的类型
         *
         * @param scene 构建初始产品的场景
         * @return T
         */
        protected abstract T scene(IScene scene);

        /**
         * 构建产品
         * 具体产品自己编写
         *
         * @return IGenerator
         */
        protected abstract R buildProduct();

        protected R build() {
            return buildProduct();
        }
    }

    /**
     * 判断visitor是空
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return visitor == null;
    }

    /**
     * 收集结果
     * 结果为bean类型
     *
     * @param scene 接口场景
     * @param bean  bean类
     * @param key   指定的key
     * @param value 指定key的指定value
     * @param <T>   T
     * @return bean
     */
    public <T> T findBeanByField(IScene scene, Class<T> bean, String key, Object value) {
        int total = scene.invoke(visitor).getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = scene.invoke(visitor).getJSONArray("list");
            T clazz = array.stream().map(e -> (JSONObject) e).filter(e -> e.getObject(key, value.getClass())
                    .equals(value)).findFirst().map(e -> JSONObject.toJavaObject(e, bean)).orElse(null);
            if (clazz != null) {
                return clazz;
            }
        }
        return null;
    }

    /**
     * 步骤标记
     *
     * @param str 内容
     */
    protected void logger(String str) {
        logger.info("---------------------------------------[ {} ]---------------------------------------", str);
    }

    /**
     * 清空visitor&计数器
     */
    protected void clear() {
        this.visitor = null;
        this.counter = 0;
    }
}
