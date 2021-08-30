package com.haisheng.framework.testng.bigScreen.itemBasic.base.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;

import java.util.List;

/**
 * 场景接口
 * 提供一些处理方法
 * 应接入Api类，使用api内的参数进行接口调用
 *
 * @author wangmin
 * @date 2020/9/27
 */
public interface IScene {

    /**
     * 获取请求体
     *
     * @return 请求体
     */
    JSONObject getBody();

    /**
     * 获取接口地址
     *
     * @return 地址
     */
    String getPath();

    /**
     * 获取域名
     *
     * @return 域名
     */
    String getIpPort();

    /**
     * 放入页码
     *
     * @param page 页码
     */
    void setPage(Integer page);

    /**
     * 放入size
     *
     * @param size 页尺寸
     */
    void setSize(Integer size);

    /**
     * 放入请求体
     *
     * @param requestBodyBody 请求体
     */
    IScene setRequestBodyBody(JSONObject requestBodyBody);

    /**
     * 访问接口
     *
     * @return 返回值
     */
    JSONObject execute();

    /**
     * 上传文件
     *
     * @return 接口返回值
     */
    JSONObject upload();

    /**
     * 移除键keys
     *
     * @param keys 键列表
     * @return IScene
     */
    IScene remove(String... keys);


    <T> IScene modify(String key, T value);

    /**
     * 获取键列表
     *
     * @return 键列表
     */
    List<String> getKeyList();

    /**
     * 获取返回值
     *
     * @return 返回值 推荐使用
     */
    Response getResponse();

    /**
     * 放入相关产品代理
     *
     * @param visitor 代理
     * @return IScene
     */
    IScene visitor(VisitorProxy visitor);

}
