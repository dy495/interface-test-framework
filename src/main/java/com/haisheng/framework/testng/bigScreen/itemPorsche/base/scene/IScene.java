package com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.proxy.VisitorProxy;

import java.util.List;

/**
 * 场景接口
 * 提供一些处理方法
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
     * 访问接口
     *
     * @param visitor   产品
     * @param checkCode 是否校验返回值code
     * @return 接口返回值
     */
    JSONObject invoke(VisitorProxy visitor, boolean checkCode);

    /**
     * 访问接口
     *
     * @param visitor 产品
     * @return 接口返回值
     */
    JSONObject invoke(VisitorProxy visitor);

    /**
     * 上传文件
     *
     * @param visitor 产品
     * @return 接口返回值
     */
    JSONObject upload(VisitorProxy visitor);

    /**
     * 移除键keys
     *
     * @param keys 键列表
     * @return IScene
     */
    IScene remove(String... keys);

    /**
     * 获取键列表
     *
     * @return 键列表
     */
    List<String> getKeyList();

}
