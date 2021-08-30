package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 29.17. 作废客户卡券 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class VoucherManageInvalidCustomerVoucherScene extends BaseScene {
    /**
     * 描述 领取记录id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 做给原因
     * 是否必填 false
     * 版本 v2.0
     */
    private final String invalidReason;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("invalid_reason", invalidReason);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/voucher-manage/invalid-customer-voucher";
    }
}