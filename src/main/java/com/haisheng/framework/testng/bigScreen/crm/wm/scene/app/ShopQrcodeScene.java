package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.base.BaseScene;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取接待二维码接口
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class ShopQrcodeScene extends BaseScene {
    @Override
    public JSONObject getJSONObject() {
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
