package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 获取接待二维码接口
 *
 * @author wangmin
 */
@Builder
public class ShopQrcodeScene extends BaseScene {
    @Override
    public JSONObject getRequest() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/porsche/app/shop/qrcode";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
