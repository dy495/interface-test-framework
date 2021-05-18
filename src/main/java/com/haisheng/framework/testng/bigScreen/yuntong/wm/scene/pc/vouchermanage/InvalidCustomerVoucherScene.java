package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.17. 作废客户卡券 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class InvalidCustomerVoucherScene extends BaseScene {
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
        return "/yt/pc/voucher-manage/invalid-customer-voucher";
    }
}