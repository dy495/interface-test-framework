package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.insurancemanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/insurance-management/buy-insurance的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class BuyInsuranceScene extends BaseScene {
    /**
     * 描述 列表中id
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("insurance_company_id", insuranceCompanyId);
        object.put("insurance_money", insuranceMoney);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/insurance-management/buy-insurance";
    }
}