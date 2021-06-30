package com.haisheng.framework.testng.bigScreen.itemPorsche.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 获取接待二维码接口
 *
 * @author wangmin
 */
@Builder
public class ShopQrcodeScene extends BaseScene {
    @Override
    public JSONObject getRequestBody() {
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
