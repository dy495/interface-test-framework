package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.6. 专属售后顾问提交 （池） （2021-03-22）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletConsultAfterServiceSubmitScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 咨询内容
     * 是否必填 true
     * 版本 v3.0
     */
    private final String content;

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
     * 描述 车型
     * 是否必填 true
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
     * 描述 服务顾问id
     * 是否必填 true
     * 版本 v3.0
     */
    private final String salesId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("content", content);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("model_id", modelId);
        object.put("reset_model_name", resetModelName);
        object.put("sales_id", salesId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/consult/after-service-submit";
    }
}