package com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.util.CommonUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生成器的抽象类
 *
 * @author wangmin
 * @date 2021/1/22 11:30
 */
public abstract class AbstractGenerator implements IGenerator {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractGenerator.class);
    protected static final int SIZE = 100;
    protected final StringBuilder errorMsg;
    protected int counter = 0;
    protected VisitorProxy visitor;

    protected AbstractGenerator(@NotNull AbstractBuilder<?> abstractBuilder) {
        this.visitor = abstractBuilder.visitor;
        this.errorMsg = new StringBuilder(16);
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg.toString();
    }

    @Override
    public abstract void execute(VisitorProxy visitor, IScene scene);

    public static abstract class AbstractBuilder<T extends AbstractBuilder<?>> {
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
        protected abstract T createScene(IScene scene);

        /**
         * 构建产品
         * 具体产品自己编写
         *
         * @return IGenerator
         */
        protected abstract IGenerator buildProduct();

        protected IGenerator build() {
            return buildProduct();
        }
    }

    /**
     * 收集结果
     * 结果为bean类型
     *
     * @param scene 接口场景
     * @param bean  bean类
     * @param <T>   T
     * @return bean的集合
     */
    protected <T> List<T> resultCollectToBean(IScene scene, Class<T> bean) {
        List<T> list = new ArrayList<>();
        int total = visitor.invokeApi(scene).getInteger("total");
        int s = CommonUtil.getTurningPage(total, SIZE);
        for (int i = 1; i < s; i++) {
            scene.setPage(i);
            scene.setSize(SIZE);
            JSONArray array = visitor.invokeApi(scene).getJSONArray("list");
            list.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, bean)).collect(Collectors.toList()));
        }
        return list;
    }

    /**
     * 判断visitor是否为空
     *
     * @return boolean
     */
    protected Boolean isEmpty() {
        return visitor == null;
    }

    /**
     * 步骤标记
     *
     * @param str 内容
     */
    protected void logger(String str) {
        logger.info("---------------------------------------[ {} ]---------------------------------------", str);
    }

    protected void clear() {
        this.visitor = null;
        this.counter = 0;
    }
}
