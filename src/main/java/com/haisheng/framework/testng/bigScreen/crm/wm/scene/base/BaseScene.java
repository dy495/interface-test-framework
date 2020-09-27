package com.haisheng.framework.testng.bigScreen.crm.wm.scene.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;

/**
 * 接口执行抽象类
 * 统一执行方法executeInvoke进行调用
 *
 * @author wangmin
 */
public abstract class BaseScene extends TestCaseCommon implements IScene {

    @Override
    public abstract JSONObject invokeApi();

    @Override
    public abstract String getPath();

    @Override
    public abstract String geIpPort();

    @Override
    public JSONObject execute(JSONObject requestBody, boolean data) {
        if (StringUtils.isEmpty(geIpPort()) && StringUtils.isEmpty(geIpPort())) {
            throw new RuntimeException("path+IpPort不能为空");
        }
        String request = JSON.toJSONString(requestBody);
        String result = httpPostWithCheckCode(geIpPort(), request, getPath());
        if (data) {
            return JSON.parseObject(result).getJSONObject("data");
        } else {
            return JSON.parseObject(result);
        }
    }
}
