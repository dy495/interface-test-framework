package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.29. 核销人员导出（华成裕）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class VerificationPeopleExportScene extends BaseScene {
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
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
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

    /**
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页，SPECIFIED_DATA：导出特定数据
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exportType;

    /**
     * 描述 导出数据id列表，特定数据时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray ids;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
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
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/verification-people/export";
    }
}