package com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 调用http请求的代理
 * 主要作用换ip，以后有其他工作再加
 *
 * @author wangmin
 * @date 2021/1/20 13:36
 */
public class VisitorProxy extends TestCaseCommon {
    @Getter
    private EnumTestProduct product;

    /**
     * 构造函数
     *
     * @param product 调用产品
     */
    public VisitorProxy(EnumTestProduct product) {
        this.product = product;
    }

    /**
     * 展示ip
     */
    public void showIp() {
        logger.info("ip is:{}", this.product.getIp());
    }

    /**
     * 访问场景
     *
     * @param scene 场景
     * @return 返回值
     */
    public JSONObject invokeApi(IScene scene) {
        return invokeApi(scene, true);
    }

    /**
     * 访问场景
     *
     * @param scene 场景
     * @return 返回值
     */
    public JSONObject invokeApi(@NotNull IScene scene, boolean checkCode) {
        return invokeApi(scene.getPath(), scene.getBody(), checkCode);
    }

    /**
     * http请求方法调用
     *
     * @param path        路径
     * @param requestBody 请求体
     * @param checkCode   是否校验code
     * @return JSONObject response.data
     */
    public JSONObject invokeApi(String path, JSONObject requestBody, boolean checkCode) {
        return invokeApi(product.getIp(), path, requestBody, checkCode);
    }

    /**
     * http请求方法调用
     *
     * @param host        域名
     * @param path        路径
     * @param requestBody 请求体
     * @param checkCode   是否校验code
     * @return JSONObject
     */
    public JSONObject invokeApi(String host, String path, JSONObject requestBody, boolean checkCode) {
        Preconditions.checkArgument(!StringUtils.isEmpty(path), "path不可为空");
        //判断接口域名是不是空，如果不是使用提供的域名，如果是使用product的域名
        host = StringUtils.isEmpty(host) ? product.getIp() : host;
        Preconditions.checkArgument(!StringUtils.isEmpty(host), "host不可为空");
        String request = JSON.toJSONString(requestBody);
        String result = httpPost(host, path, request, checkCode, false);
        JSONObject response = JSON.parseObject(result);
        return checkCode ? response.getJSONObject("data") : response;
    }

    /**
     * 上传
     *
     * @param filePath 文件路径
     * @param path     接口地址
     * @return 返回值
     */
    public JSONObject upload(String path, String filePath) {
        String response = uploadFile(product.getIp(), path, filePath);
        return JSON.parseObject(response);
    }

    /**
     * 设置token
     *
     * @param scene 场景
     */
    public void setToken(@NotNull IScene scene) {
        httpPost(product.getIp(), scene.getPath(), scene.getBody());
    }

    /**
     * 设置token
     *
     * @param token token
     */
    public void setToken(String token) {
        authorization = token;
    }

    /**
     * 为空判断
     *
     * @return true/false
     */
    public boolean isEmpty() {
        return product == null;
    }

    /**
     * 判断是否是线上地址
     *
     * @return boolean
     */
    public boolean isDaily() {
        return product.getIsDaily();
    }

    /**
     * 更换域名
     *
     * @param product 新域名
     */
    public void setProduct(EnumTestProduct product) {
        this.product = product;
    }

    /**
     * 新增处理插件
     * 由于线上线下域名与路径不同，增加处理
     *
     * @param oldPath     旧的路径
     * @param requestBody 旧的请求体
     * @return 内部类数据
     */
    private InnerData transfer(String oldPath, JSONObject requestBody) {
        InnerData innerData = new InnerData();
        if (product.getIp().contains(EnumTestProduct.JC_ONLINE_ZH.getIp())) {
            if (oldPath.contains("/account-platform/")) {
                oldPath = oldPath.replace("/account-platform/", "/jiaochen/");
                requestBody.remove("type");
            } else {
                oldPath = oldPath.contains("/car-platform/") ? oldPath.replace("/car-platform/", "/jiaochen/") : oldPath;
            }
        }
        innerData.setPath(oldPath);
        innerData.setRequestBody(requestBody);
        return innerData;
    }

    @Data
    static class InnerData implements Serializable {
        private JSONObject requestBody;
        private String path;
    }
}
