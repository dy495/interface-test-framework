package com.haisheng.framework.testng.bigScreen.crm.wm.scene.base;

import com.alibaba.fastjson.JSONObject;

/**
 * 场景接口
 * 接口类统一管理
 * 每个接口自己赋值url+address
 *
 * @author wangmin
 */
public interface IScene {

    /**
     * 调用api
     *
     * @return response
     */
    JSONObject invokeApi();

    /**
     * 获取地址
     *
     * @return address
     */
    String getPath();

    /**
     * 获取url
     *
     * @return url
     */
    String getIpPort();


    /**
     * 执行调用
     *
     * @param path        路径
     * @param requestBody 请求体
     * @param data        取不取data
     * @return response
     */
    JSONObject execute(JSONObject requestBody, boolean data);
}
