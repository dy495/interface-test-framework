package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 22.6. 创建内容营销活动 （谢）v3.0（2021-03-16）
 *
 * @author wangmin
 * @date 2021-03-31 12:50:51
 */
@Builder
public class ManageContentMarketingAddScene extends BaseScene {
    /**
     * 描述 活动类型 见字典表《活动类型》
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer type;

    /**
     * 描述 活动参与限制类型，0：全部可参加，1：部分可参加，2：部分不可参加
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer participationLimitType;

    /**
     * 描述 选中标签，限制类型为 1 或 2 时不可为空 使用人群标签组接口获取
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray chooseLabels;

    /**
     * 描述 活动标题
     * 是否必填 true
     * 版本 v2.0
     */
    private final String title;

    /**
     * 描述 活动规则（v3.0）兼容活动介绍
     * 是否必填 true
     * 版本 v2.0
     */
    private final String rule;

    /**
     * 描述 活动开始日期
     * 是否必填 true
     * 版本 v2.0
     */
    private final String startDate;

    /**
     * 描述 活动结束日期
     * 是否必填 true
     * 版本 v2.0
     */
    private final String endDate;

    /**
     * 描述 所属主体类型
     * 是否必填 true
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
     * 描述 活动标签
     * 是否必填 true
     * 版本 v2.0
     */
    private final String label;

    /**
     * 描述 活动图片OSS地址
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray picList;

    /**
     * 描述 是否自定义分享信息
     * 是否必填 false
     * 版本 v3.0
     */
    private final Boolean isCustomShareInfo;

    /**
     * 描述 自定义分享图片
     * 是否必填 false
     * 版本 v3.0
     */
    private final String sharePicPath;

    /**
     * 描述 自定义分享标题
     * 是否必填 false
     * 版本 v3.0
     */
    private final String shareTitle;

    /**
     * 描述 行动点类型 见字典表《行动点类型》
     * 是否必填 true
     * 版本 v3.0
     */
    private final String actionPoint;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("participation_limit_type", participationLimitType);
        object.put("choose_labels", chooseLabels);
        object.put("title", title);
        object.put("rule", rule);
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("label", label);
        object.put("pic_list", picList);
        object.put("is_custom_share_info", isCustomShareInfo);
        object.put("share_pic_path", sharePicPath);
        object.put("share_title", shareTitle);
        object.put("actionPoint", actionPoint);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/content-marketing/add";
    }
}