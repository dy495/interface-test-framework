package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.2. 语音评鉴列表导出（谢）
 *
 * @author wangmin
 * @date 2021-05-31 16:28:11
 */
@Builder
public class VoiceEvaluationExportScene extends BaseScene {
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
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页
     * 是否必填 true
     * 版本 v1.0
     */
    private final String exportType;

    /**
     * 描述 接待顾问姓名
     * 是否必填 false
     * 版本 v1.0
     */
    private final String receptorName;

    /**
     * 描述 进店情况 使用"获取指定枚举值列表"接口获取 enum_type为ENTER_STORE_STATUS_LIST
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer enterStatus;

    /**
     * 描述 评分状态 使用"获取指定枚举值列表"接口获取 enum_type为VOICE_EVALUATE_STATUS_LIST
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer evaluateStatus;

    /**
     * 描述 客户姓名
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerName;

    /**
     * 描述 客户联系方式
     * 是否必填 false
     * 版本 v1.0
     */
    private final String customerPhone;

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
        object.put("page", page);
        object.put("size", size);
        object.put("export_type", exportType);
        object.put("receptor_name", receptorName);
        object.put("enter_status", enterStatus);
        object.put("evaluate_status", evaluateStatus);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("reception_start", receptionStart);
        object.put("reception_end", receptionEnd);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/manage/voice/evaluation/export";
    }
}