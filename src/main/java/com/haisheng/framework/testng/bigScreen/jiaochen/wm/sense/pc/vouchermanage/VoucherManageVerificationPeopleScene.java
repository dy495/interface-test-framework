package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 29.27. 
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class VoucherManageVerificationPeopleScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

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
        object.put("page", page);
        object.put("size", size);
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
        return "/car-platform/pc/voucher-manage/verification-people";
    }
}