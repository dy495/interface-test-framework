package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftersensitivewords;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.1. 敏感词标签统计列表（华成裕）
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class AfterSensitiveWordsLabelListScene extends BaseScene {
    /**
     * 描述 接待顾问姓名
     * 是否必填 false
     * 版本 v1.0
     */
    private final String receptorName;

    /**
     * 描述 敏感词类别 使用"获取指定枚举值列表"接口获取 enum_type为 SENSITIVE_WORDS_TYPES
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer sensitiveWordsType;

    /**
     * 描述 审核状态 使用"获取指定枚举值列表"接口获取 enum_type为 APPROVAL_STATUSES
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer approvalStatus;

    /**
     * 描述 接待时间范围查询开始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String receptionStart;

    /**
     * 描述 接待时间范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String receptionEnd;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("receptor_name", receptorName);
        object.put("sensitive_words_type", sensitiveWordsType);
        object.put("approval_status", approvalStatus);
        object.put("reception_start", receptionStart);
        object.put("reception_end", receptionEnd);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/after-sensitive-words/label-list";
    }
}