package com.haisheng.framework.testng.bigScreen.crm.wm.base.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;

/**
 * 场景抽象类
 *
 * @author wangmin
 */
public abstract class BaseScene implements IScene {
    /**
     * 子类实现提供请求体
     *
     * @return 请求提
     */
    @Override
    public abstract JSONObject getRequestBody();

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
     * 提供子类修改page的方法
     *
     * @param page 页码
     */
    @Override
    public void setPage(Integer page) {

    }

    /**
     * 提供子类修改size的方法
     *
     * @param size 页码尺寸
     */
    @Override
    public void setSize(Integer size) {

    }

    /**
     * 提供调用接口的能力
     *
     * @param visitor   要执行的产品
     * @param checkCode 是否校验code
     * @return 接口返回值
     */
    @Override
    public JSONObject invoke(VisitorProxy visitor, boolean checkCode) {
        return visitor.invokeApi(getPath(), getRequestBody(), checkCode);
    }
}
