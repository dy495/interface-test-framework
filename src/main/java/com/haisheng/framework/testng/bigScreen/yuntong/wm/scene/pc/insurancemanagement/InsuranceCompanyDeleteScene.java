package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.insurancemanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 19.8. 投保公司删除（池）（2021-03-05）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("insurance_company_id", insuranceCompanyId);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/insurance-management/insurance-company-delete";
    }
}