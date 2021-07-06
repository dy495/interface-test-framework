package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 22.3. 活动导出 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-03-31 12:50:51
 */
@Builder
public class ManageExportScene extends BaseScene {
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
    public JSONObject getRequestBody() {
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
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/export";
    }
}