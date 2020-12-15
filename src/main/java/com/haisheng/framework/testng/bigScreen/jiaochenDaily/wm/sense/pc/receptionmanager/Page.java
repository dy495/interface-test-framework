package com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.receptionmanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 接待管理 -> 列表
 */
@Builder
public class Page extends BaseScene {
    private final String plateNumber;
    private final String receptionSaleId;
    private final String receptionDate;
    private final String customerName;
    private final Integer receptionStatus;
    private final String finishDate;
    private final String customerPhone;
    private final Integer receptionType;
    private final Integer receptionSaleName;
    private final Long shopId;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("plate_number", plateNumber);
        object.put("reception_sale_id", receptionSaleId);
        object.put("reception_sale_name", receptionSaleName);
        object.put("reception_date", receptionDate);
        object.put("customer_name", customerName);
        object.put("reception_status", receptionStatus);
        object.put("finish_date", finishDate);
        object.put("customer_phone", customerPhone);
        object.put("reception_type", receptionType);
        object.put("shop_id", shopId);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/reception-manage/page";
    }
}
