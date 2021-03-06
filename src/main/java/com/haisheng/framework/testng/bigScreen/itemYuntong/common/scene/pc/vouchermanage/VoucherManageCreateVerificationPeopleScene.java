package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.29. 创建核销人员
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class VoucherManageCreateVerificationPeopleScene extends BaseScene {
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
    private final Long shopId;

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
        object.put("id", id);
        object.put("verification_person", verificationPerson);
        object.put("verification_phone", verificationPhone);
        object.put("verification_code", verificationCode);
        object.put("verification_person_name", verificationPersonName);
        object.put("verification_person_phone", verificationPersonPhone);
        object.put("shopId", shopId);
        object.put("status", status);
        object.put("type", type);
        object.put("isSystem", isSystem);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/voucher-manage/create-verification-people";
    }
}