package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 卡券管理 -> 创建核销人员
 */
@Builder
public class CreateVerificationPeople extends BaseScene {
    private final String verificationPersonName;
    private final String verificationPersonPhone;
    private final Integer status;
    private final Integer type;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("verification_person_name", verificationPersonName);
        object.put("verification_person_phone", verificationPersonPhone);
        object.put("status", status);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/create-verification-people";
    }
}
