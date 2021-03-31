package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.7. 小程序 - 实体商品提交订单 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletIntegralMallSubmitOrderScene extends BaseScene {
    /**
     * 描述 商品id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long commodityId;

    /**
     * 描述 规格组合id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long specificationId;

    /**
     * 描述 买家留言
     * 是否必填 false
     * 版本 v2.0
     */
    private final String buyerMessage;

    /**
     * 描述 开启短信通知
     * 是否必填 false
     * 版本 v2.0
     */
    private final Boolean smsNotify;

    /**
     * 描述 商品数量
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer commodityNum;

    /**
     * 描述 区域
     * 是否必填 false
     * 版本 -
     */
    private final String districtCode;

    /**
     * 描述 收货地址
     * 是否必填 false
     * 版本 v2.0
     */
    private final String address;

    /**
     * 描述 收货人电话
     * 是否必填 false
     * 版本 -
     */
    private final String receiver;

    /**
     * 描述 收货人手机号
     * 是否必填 false
     * 版本 -
     */
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
        return "/jiaochen/applet/granted/integral-mall/submit-order";
    }
}