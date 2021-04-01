package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 兑换商品
 */
@Builder
public class AppletSubmitOrderScene extends BaseScene {
    private final Long commodityId;
    private final Long specificationId;
    private final String buyerMessage;
    private final Boolean smsNotify;
    private final Integer commodityNum;
    private final String districtCode;
    private final String address;
    private final String receiver;
    private final String receivePhone;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("commodity_id", commodityId);
        object.put("specification_id", specificationId);
        object.put("buyer_message", buyerMessage);
        object.put("sms_notify", smsNotify);
        object.put("commodity_num", commodityNum);
        object.put("district_code", districtCode);
        object.put("address", address);
        object.put("receiver", receiver);
        object.put("receive_phone", receivePhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/applet/granted/integral-mall/submit-order";
    }

}
