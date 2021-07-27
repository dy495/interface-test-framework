package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 21.15. 查询客户 v1.0
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class PackageManageSearchCustomerScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String customerPhone;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/package-manage/search-customer";
    }
}