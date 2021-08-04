package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.4. 卡券批量审批 （张小龙） v3.0
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class ApplyApprovalInfoScene extends BaseScene {

    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/voucher/apply/approval-info";
    }
}