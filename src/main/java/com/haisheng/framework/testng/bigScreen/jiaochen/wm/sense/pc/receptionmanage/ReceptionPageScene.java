package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 接待管理 -> 列表
 */
@Builder
public class ReceptionPageScene extends BaseScene {
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
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getRequestBody() {
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
        return "/car-platform/pc/reception-manage/page";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}
