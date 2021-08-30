package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.insurancemanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.10. 保险公司新增（池）（2021-03-12）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:54
 */
@Builder
public class InsuranceManagementInsuranceCompanyAddScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final String name;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/insurance-management/insurance-company-add";
    }
}