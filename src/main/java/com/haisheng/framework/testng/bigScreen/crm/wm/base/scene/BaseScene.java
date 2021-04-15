package com.haisheng.framework.testng.bigScreen.crm.wm.base.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 场景抽象类
 *
 * @author wangmin
 */
public abstract class BaseScene implements IScene {
    @Setter
    protected Integer size;
    @Setter
    protected Integer page;
    private JSONObject body;

    @Override
    public JSONObject getBody() {
        return body == null ? getRequestBody() : body;
    }

    /**
     * 子类实现提供请求体
     *
     * @return 请求提
     */
    protected abstract JSONObject getRequestBody();

    /**
     * 子类实现提供地址
     *
     * @return 地址
     */
    @Override
    public abstract String getPath();

    /**
     * 子类实现提供域名
     *
     * @return 域名
     */
    @Override
    public String getIpPort() {
        return null;
    }

    /**
     * 提供调用接口的能力
     *
     * @param visitor   要执行的产品
     * @param checkCode 是否校验code
     * @return 接口返回值
     */
    @Override
    public JSONObject invoke(@NotNull VisitorProxy visitor, boolean checkCode) {
        return visitor.invokeApi(getPath(), getBody(), checkCode);
    }

    /**
     * 提供调用接口的能力
     *
     * @param visitor 要执行的产品
     * @return 接口返回值
     */
    public JSONObject invoke(@NotNull VisitorProxy visitor) {
        return invoke(visitor, true);
    }

    /**
     * 去掉某些参数
     *
     * @param keys 键的集合
     * @return IScene
     */
    public IScene remove(String... keys) {
        body = getRequestBody();
        Arrays.stream(keys).forEach(e -> body.remove(e));
        return this;
    }

    /**
     * 获取值非空的键集合
     *
     * @return key的集合
     */
    public List<String> getKeyList() {
        return getBody().entrySet().stream().filter(e -> e.getValue() != null).map(Map.Entry::getKey).collect(Collectors.toCollection(LinkedList::new));
    }
}
