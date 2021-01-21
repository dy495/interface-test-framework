package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 套餐管理 -> 修改套餐
 */
@Builder
public class EditPackage extends BaseScene {
    private final String packageName;
    private final Integer validity;
    private final String packageDescription;
    private final JSONArray voucherList;
    private final String packagePrice;
    private final List<Long> shopIds;
    private final Boolean status;
    private final String id;
    private final String subjectType;
    private final Long subjectId;

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
        object.put("id", id);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
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
