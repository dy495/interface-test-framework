package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.insurancemanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.5. 购买 （池）（2021-03-05）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:54
 */
@Builder
public class InsuranceManagementBuyInsuranceScene extends BaseScene {
    /**
     * 描述 id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long id;

    /**
     * 描述 投保公司
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long insuranceCompanyId;

    /**
     * 描述 投保金额
     * 是否必填 false
     * 版本 v3.0
     */
    private final Double insuranceMoney;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("insurance_company_id", insuranceCompanyId);
        object.put("insurance_money", insuranceMoney);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/insurance-management/buy-insurance";
    }
}