package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.insurancemanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/insurance-management/insurance-company-delete的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class InsuranceCompanyDeleteScene extends BaseScene {
    /**
     * 描述 投保公司id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long insuranceCompanyId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("insurance_company_id", insuranceCompanyId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/insurance-management/insurance-company-delete";
    }
}