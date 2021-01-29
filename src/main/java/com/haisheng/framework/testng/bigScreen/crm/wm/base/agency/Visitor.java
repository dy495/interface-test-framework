package com.haisheng.framework.testng.bigScreen.crm.wm.base.agency;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.DataException;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

/**
 * 调用接口类
 *
 * @author wangmin
 * @date 2021/1/20 13:36
 */
public class Visitor extends TestCaseCommon {

    private final EnumTestProduce product;

    /**
     * 构造函数
     *
     * @param product 调用产品
     */
    public Visitor(EnumTestProduce product) {
        this.product = product;
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
        return invokeApi(scene.getPath(), scene.getJSONObject(), checkCode);
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
        String IpPort = product.getAddress();
        if (StringUtils.isEmpty(path)) {
            throw new DataException("path不可为空");
        }
        String request = JSON.toJSONString(requestBody);
        String result = null;
        if (checkCode) {
            result = httpPostWithCheckCode(path, request, IpPort);
            return JSON.parseObject(result).getJSONObject("data");
        } else {
            try {
                result = httpPost(path, request, IpPort);
            } catch (Exception e) {
                appendFailReason(e.toString());
            }
            return JSON.parseObject(result);
        }
    }

    /**
     * 上传
     *
     * @param scene 场景接口
     * @return 返回值
     */
    public JSONObject uploadFile(IScene scene) {
        String response = uploadFile(scene.getJSONObject().getString("filePath"), scene.getPath(), product.getAddress());
        return JSON.parseObject(response);
    }

    /**
     * pc登录
     *
     * @param scene 场景
     * @return 返回值
     */
    public void login(@NotNull IScene scene) {
        httpPost(scene.getPath(), scene.getJSONObject(), product.getAddress());
    }

    /**
     * 小程序登录
     *
     * @param token token
     */
    public void login(String token) {
        authorization = token;
        logger.info("applet authorization is:{}", authorization);
    }

    /**
     * 为空判断
     *
     * @return true/false
     */
    public Boolean isEmpty() {
        return product == null;
    }


    /**
     * 判断是否是线上地址
     *
     * @return boolean
     */
    public Boolean isOnline() {
        return product.name().contains("ONLINE");
    }
}
