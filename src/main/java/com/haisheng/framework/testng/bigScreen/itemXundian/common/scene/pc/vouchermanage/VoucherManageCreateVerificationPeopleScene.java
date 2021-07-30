package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 58.30. 
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class VoucherManageCreateVerificationPeopleScene extends BaseScene {
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
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String verificationPerson;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String verificationPhone;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String verificationCode;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String verificationPersonName;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String verificationPersonPhone;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Boolean status;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Integer type;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Boolean isSystem;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("verification_person", verificationPerson);
        object.put("verification_phone", verificationPhone);
        object.put("verification_code", verificationCode);
        object.put("verification_person_name", verificationPersonName);
        object.put("verification_person_phone", verificationPersonPhone);
        object.put("status", status);
        object.put("type", type);
        object.put("isSystem", isSystem);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/create-verification-people";
    }
}