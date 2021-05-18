package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 17.13. 作废客户购买套餐 （张小龙）（2021-03-15）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class InvalidCustomerPackageScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 false
     * 版本 v2.2
     */
    private final Long id;

    /**
     * 描述 作废原因
     * 是否必填 true
     * 版本 v2.2
     */
    private final String reason;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("reason", reason);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/package-manage/invalid-customer-package";
    }
}