package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.7. 星级比例（谢）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:54
 */
@Builder
public class ScoreRateScene extends BaseScene {
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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("service_sale_id", serviceSaleId);
        object.put("enter_status", enterStatus);
        object.put("link_type", linkType);
        object.put("reception_start", receptionStart);
        object.put("reception_end", receptionEnd);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/manage/evaluate/v4/score/rate";
    }
}