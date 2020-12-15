package com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 卡券管理 -> 核销人员
 */
@Builder
public class VerificationPeople extends BaseScene {
    private final String verificationPerson;
    private final String verificationPhone;
    private final String verificationCode;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("verification_person", verificationPerson);
        object.put("verification_phone", verificationPhone);
        object.put("verification_code", verificationCode);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/verification-people";
    }
}
