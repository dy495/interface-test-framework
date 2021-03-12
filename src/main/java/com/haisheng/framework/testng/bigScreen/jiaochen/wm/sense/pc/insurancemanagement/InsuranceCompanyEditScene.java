package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.insurancemanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/insurance-management/insurance-company-edit的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class InsuranceCompanyEditScene extends BaseScene {
    /**
     * 描述 投保公司id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long insuranceCompanyId;

    /**
     * 描述 投保公司名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String insuranceCompanyName;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("insurance_company_id", insuranceCompanyId);
        object.put("insurance_company_name", insuranceCompanyName);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/insurance-management/insurance-company-edit";
    }
}