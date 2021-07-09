package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.insurancemanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 19.10. 保险公司新增（池）（2021-03-12）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class InsuranceCompanyAddScene extends BaseScene {
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
        return "/account-platform/auth/insurance-management/insurance-company-add";
    }
}