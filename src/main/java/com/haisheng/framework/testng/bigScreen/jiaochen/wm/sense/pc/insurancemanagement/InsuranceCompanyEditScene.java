package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.insurancemanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.9. 保险公司维护（池）（2021-03-05）
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
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
    public JSONObject getRequestBody() {
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