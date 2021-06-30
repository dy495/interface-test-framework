package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.insurancemanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.10. 保险公司新增（池）（2021-03-12）
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/insurance-management/insurance-company-add";
    }
}