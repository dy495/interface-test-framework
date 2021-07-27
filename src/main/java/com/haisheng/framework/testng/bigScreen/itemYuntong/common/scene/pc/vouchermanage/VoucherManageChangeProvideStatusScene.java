package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.14. 开始/结束发放 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class VoucherManageChangeProvideStatusScene extends BaseScene {
    /**
     * 描述 卡券id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 是否开始发放
     * 是否必填 true
     * 版本 v2.0
     */
    private final Boolean isStart;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("is_start", isStart);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/voucher-manage/change-provide-status";
    }
}