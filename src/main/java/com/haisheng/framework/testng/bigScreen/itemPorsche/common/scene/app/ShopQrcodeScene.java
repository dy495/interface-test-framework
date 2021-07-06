package com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
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


}
