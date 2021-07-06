package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.6. 星级评分趋势（谢）
 *
 * @author wangmin
 * @date 2021-06-01 19:09:09
 */
@Builder
public class EvaluateV4ScoreTrendScene extends BaseScene {
    /**
     * 描述 客户经理id 通过权限员工列表获取下拉列表 auth_type，售后：AFTER_SALE_RECEPTION，售前：PRE_SALE_RECEPTION
     * 是否必填 false
     * 版本 v2.0
     */
    private final String serviceSaleId;

    /**
     * 描述 进店情况 使用"获取指定枚举值列表"接口获取 enum_type为 ENTER_STORE_STATUS_LIST
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer enterStatus;

    /**
     * 描述 接待环节 使用"获取指定枚举值列表"接口获取 enum_type为 RECEPTION_LINKS
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer linkType;

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


    private final Integer evaluateType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("service_sale_id", serviceSaleId);
        object.put("enter_status", enterStatus);
        object.put("link_type", linkType);
        object.put("reception_start", receptionStart);
        object.put("reception_end", receptionEnd);
        object.put("evaluate_type", evaluateType);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/manage/evaluate/v4/score/trend";
    }
}