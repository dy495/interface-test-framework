package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 58.24. 
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class VoucherManageTransferScene extends BaseScene {
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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("transfer_phone", transferPhone);
        object.put("receive_phone", receivePhone);
        object.put("voucher_ids", voucherIds);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/transfer";
    }
}