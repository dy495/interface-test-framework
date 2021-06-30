package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 29.1. 活动列表 （谢）v3.0（2021-04-02）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class ManagePageScene extends BaseScene {
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
     * 描述 活动标题
     * 是否必填 false
     * 版本 v2.0
     */
    private final String title;

    /**
     * 描述 活动状态 通过通用枚举接口获取，key为 ACTIVITY_STATUS
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer status;

    /**
     * 描述 活动审批状态 通过通用枚举接口获取，key为 ACTIVITY_APPROVAL_STATUS
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer approvalStatus;

    /**
     * 描述 创建者账号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String creatorAccount;

    /**
     * 描述 创建者姓名
     * 是否必填 false
     * 版本 v2.0
     */
    private final String creatorName;

    /**
     * 描述 所属主体类型
     * 是否必填 false
     * 版本 v2.0
     */
    private final String subjectType;

    /**
     * 描述 所属主体id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long subjectId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("title", title);
        object.put("status", status);
        object.put("approval_status", approvalStatus);
        object.put("creator_account", creatorAccount);
        object.put("creator_name", creatorName);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/activity/manage/page";
    }
}