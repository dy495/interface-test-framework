package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.19. 售后变更所属销售 (杨)v3.0
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
 */
@Builder
public class AfterSaleCustomerChangeBelongSaleScene extends BaseScene {
    /**
     * 描述 门店Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 车辆Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long cardId;

    /**
     * 描述 客户Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;

    /**
     * 描述 客户Id
     * 是否必填 true
     * 版本 v3.0
     */
    private final String saleId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("card_id", cardId);
        object.put("customer_id", customerId);
        object.put("sale_id", saleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/customer-manage/after-sale-customer/change-belong-sale";
    }
}