package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import lombok.Builder;

/**
 * 34.4. 删除员工 （杨航）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class StaffDeleteScene extends BaseScene {
    /**
     * 描述 账号id
     * 是否必填 true
     * 版本 v1.0
     */
    private final String id;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/staff/delete";
    }

    @Override
    public String getIpPort() {
        return getVisitor().isDaily() ? EnumTestProduct.JC_DAILY_ZH.getIp() : EnumTestProduct.JC_ONLINE_ZH.getIp();
    }
}