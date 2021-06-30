package com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 卡券管理 -> 创建核销人员
 */
@Builder
public class CreateVerificationPeopleScene extends BaseScene {
    private final String verificationPersonName;
    private final String verificationPersonPhone;
    private final Integer status;
    private final Integer type;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("verification_person_name", verificationPersonName);
        object.put("verification_person_phone", verificationPersonPhone);
        object.put("status", status);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/create-verification-people";
    }
}
