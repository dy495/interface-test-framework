package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.riskcontrol;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.4. 未接待风控列表 v5.0
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class NonReceptionPageScene extends BaseScene {
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
     * 描述 门店名称
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long shopId;

    /**
     * 描述 客户状态 CUSTOMER_FACE_STATUS
     * 是否必填 false
     * 版本 v5.0
     */
    private final String customerStatus;

    /**
     * 描述 开始时间
     * 是否必填 false
     * 版本 v5.0
     */
    private final String startDate;

    /**
     * 描述 结束时间
     * 是否必填 false
     * 版本 v5.0
     */
    private final String endDate;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("customer_status", customerStatus);
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/risk-control/non-reception/page";
    }
}