package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.18. 作废客户卡券 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class InvalidCustomerVoucherScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

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
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("invalid_reason", invalidReason);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/invalid-customer-voucher";
    }
}