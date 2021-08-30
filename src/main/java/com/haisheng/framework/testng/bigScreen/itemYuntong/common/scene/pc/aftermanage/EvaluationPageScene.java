package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.1. 语音评鉴列表分页（华成裕）
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class EvaluationPageScene extends BaseScene {
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

    /**
     * 描述 是否收藏， 不选传null
     * 是否必填 false
     * 版本 v7
     */
    private final Boolean isFavorite;

    /**
     * 描述 排序信息 ： key：排序字段 value：排序类型 true 升序 false 降序
     * 是否必填 false
     * 版本 v7
     */
    private final JSONArray sortInfoList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("receptor_name", receptorName);
        object.put("enter_status", enterStatus);
        object.put("evaluate_status", evaluateStatus);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("reception_start", receptionStart);
        object.put("reception_end", receptionEnd);
        object.put("is_favorite", isFavorite);
        object.put("sort_info_list", sortInfoList);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/after-manage/voice/evaluation/page";
    }
}