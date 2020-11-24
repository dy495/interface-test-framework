package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 套餐管理 -> 修改套餐
 */
@Builder
public class EditPackage extends BaseScene {
    private final String packageName;
    private final String validity;
    private final String packageDescription;
    private final String voucherList;
    private final Integer packagePrice;
    private final List<Long> shopIds;
    private final Boolean status;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("package_name", packageName);
        object.put("validity", validity);
        object.put("package_description", packageDescription);
        object.put("voucher_list", voucherList);
        object.put("package_price", packagePrice);
        object.put("shop_ids", shopIds);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/edit-package";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
