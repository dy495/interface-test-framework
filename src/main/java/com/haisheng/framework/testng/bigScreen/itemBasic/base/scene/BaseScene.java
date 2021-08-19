package com.haisheng.framework.testng.bigScreen.itemBasic.base.scene;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 场景抽象类
 *
 * @author wangmin
 * @date 2020/9/27
 */

public abstract class BaseScene implements IScene {
    private final static Logger logger = LoggerFactory.getLogger(BaseScene.class);
    @Setter
    protected Integer size;
    @Setter
    protected Integer page;
    private JSONObject body;
    @Getter
    private VisitorProxy visitor;

    /**
     * 处理后的请求体
     *
     * @return 请求体
     */
    @Override
    public JSONObject getBody() {
        return body == null ? getRequestBody() : body;
    }

    /**
     * 获取请求体
     *
     * @return 请求体
     */
    protected abstract JSONObject getRequestBody();

    /**
     * 获取接口路径
     *
     * @return 路径
     */
    @Override
    public abstract String getPath();

    /**
     * 获取接口域名
     *
     * @return 域名
     */
    @Override
    public String getIpPort() {
        return null;
    }

    /**
     * 执行
     *
     * @param visitor   要执行的产品
     * @param checkCode 是否校验code
     * @return 接口返回值
     */
    @Override
    public JSONObject execute(@NotNull VisitorProxy visitor, boolean checkCode) {
        return visitor.invokeApi(getPath(), getBody(), checkCode);
    }

    /**
     * 执行
     *
     * @return 接口返回值
     */
    @Override
    public JSONObject execute() {
        return execute(true);
    }

    /**
     * 执行
     *
     * @param checkCode 是否校验code
     * @return 返回值
     */
    private JSONObject execute(boolean checkCode) {
        Preconditions.checkNotNull(this.visitor, "visitor 未传入");
        return this.visitor.invokeApi(getIpPort(), getPath(), getBody(), checkCode);
    }

    /**
     * 上传
     *
     * @param visitor 要执行的产品
     * @return 接口返回值
     */
    @Override
    public JSONObject upload(@NotNull VisitorProxy visitor) {
        Preconditions.checkNotNull(getBody().getString("filePath"), "文件路径为空");
        String filePath = getBody().getString("filePath");
        return visitor.upload(getPath(), filePath);
    }

    /**
     * 下载
     *
     * @return 接口返回值
     */
    public JSONObject download() {
        //todo 未开发
        return null;
    }

    /**
     * 获取值非空的键集合
     *
     * @return key的集合
     */
    public List<String> getKeyList() {
        return getBody().entrySet().stream().filter(e -> e.getValue() != null)
                .map(Map.Entry::getKey).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * 去掉某些参数
     * 对构建的请求体操作
     *
     * @param keys 键的集合
     * @return IScene
     */
    public IScene remove(String... keys) {
        body = getRequestBody();
        logger.info("移除键：{}", Arrays.stream(keys).toArray());
        Arrays.stream(keys).forEach(e -> body.remove(e));
        return this;
    }

    /**
     * 修改某个key的值
     * 对构建的请求体操作
     *
     * @param key   key
     * @param value value
     * @param <T>   T
     * @return this
     */
    public <T> IScene modify(String key, T value) {
        body = getRequestBody();
        body.put(key, value);
        return this;
    }

    /**
     * 获取接口返回值
     *
     * @return 响应数据
     */
    @Override
    public Response getResponse() {
        return JSONObject.toJavaObject(execute(false), Response.class);
    }

    @Override
    public IScene visitor(VisitorProxy visitor) {
        this.visitor = visitor;
        return this;
    }
}
