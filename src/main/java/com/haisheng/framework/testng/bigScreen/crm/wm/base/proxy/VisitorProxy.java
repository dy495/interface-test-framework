package com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

/**
 * 调用http请求的代理
 *
 * @author wangmin
 * @date 2021/1/20 13:36
 */
public class VisitorProxy extends TestCaseCommon {
    private static volatile VisitorProxy INSTANCE = null;
    private static EnumTestProduce PRODUCT;

    public static synchronized VisitorProxy getInstance(EnumTestProduce product) {
        if (INSTANCE == null) {
            INSTANCE = new VisitorProxy(product);
        } else {
            if (PRODUCT != product) {
                INSTANCE = new VisitorProxy(product);
            }
        }
        return INSTANCE;
    }

    /**
     * 构造函数，私有
     *
     * @param product 调用产品
     */
    private VisitorProxy(EnumTestProduce product) {
        PRODUCT = product;
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
        String IpPort = PRODUCT.getAddress();
        if (StringUtils.isEmpty(path)) {
            throw new RuntimeException("path不可为空");
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
                collectMessage(e);
            }
            return JSON.parseObject(result);
        }
    }

    /**
     * 上传
     *
     * @param filePath 文件路径
     * @param path     接口地址
     * @return 返回值
     */
    public JSONObject upload(String path, String filePath) {
        String response = uploadFile(filePath, path, PRODUCT.getAddress());
        return JSON.parseObject(response);
    }

    /**
     * pc登录
     *
     * @param scene 场景
     */
    public void login(@NotNull IScene scene) {
        httpPost(scene.getPath(), scene.getBody(), PRODUCT.getAddress());
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
        return PRODUCT == null;
    }

    /**
     * 判断是否是线上地址
     *
     * @return boolean
     */
    public Boolean isDaily() {
        return PRODUCT.getIsDaily();
    }
}
