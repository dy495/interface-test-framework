package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 29.23. 卡卷转移
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class VoucherManageTransferScene extends BaseScene {
    /**
     * 描述 转移人手机号
     * 是否必填 false
     * 版本 -
     */
    private final String transferPhone;

    /**
     * 描述 接受人手机号
     * 是否必填 false
     * 版本 -
     */
    private final String receivePhone;

    /**
     * 描述 卡券列表
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray voucherIds;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long customerId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("transfer_phone", transferPhone);
        object.put("receive_phone", receivePhone);
        object.put("voucher_ids", voucherIds);
        object.put("customerId", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/voucher-manage/transfer";
    }
}