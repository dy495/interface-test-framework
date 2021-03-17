package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/consult/dedicated-service-submit的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
 */
@Builder
public class AppletConsultDedicatedServiceSubmitScene extends BaseScene {
    /**
     * 描述 联系人
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 联系电话
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerPhone;

    /**
     * 描述 品牌
     * 是否必填 true
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
     * 描述 其他车型
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long resetModelName;

    /**
     * 描述 服务顾问
     * 是否必填 true
     * 版本 v3.0
     */
    private final JSONObject sales;

    /**
     * 描述 咨询内容
     * 是否必填 false
     * 版本 v3.0
     */
    private final String content;

    /**
     * 描述 类型/销售 SALES、售后AFTER
     * 是否必填 true
     * 版本 v3.0
     */
    private final String type;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("brand_id", brandId);
        object.put("model_id", modelId);
        object.put("resetModelName", resetModelName);
        object.put("sales", sales);
        object.put("content", content);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/consult/dedicated-service-submit";
    }
}