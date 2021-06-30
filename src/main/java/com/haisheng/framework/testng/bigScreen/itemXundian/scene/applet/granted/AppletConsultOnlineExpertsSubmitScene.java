package com.haisheng.framework.testng.bigScreen.itemXundian.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /patrol-applet/granted/consult/online-experts-submit的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
 */
@Builder
public class AppletConsultOnlineExpertsSubmitScene extends BaseScene {
    /**
     * 描述 联系人
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 联系电话
     * 是否必填 false
     * 版本 v3,0
     */
    private final String customerPhone;

    /**
     * 描述 车辆品牌
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long brandId;

    /**
     * 描述 车型
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long modelId;

    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 咨询内容
     * 是否必填 false
     * 版本 v3.0
     */
    private final String content;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("brand_id", brandId);
        object.put("model_id", modelId);
        object.put("shop_id", shopId);
        object.put("content", content);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/consult/online-experts-submit";
    }
}