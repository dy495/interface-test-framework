package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 套餐管理 -> 创建套餐
 *
 * @author wwangmin
 * @date 2020-11-24
 */
@Builder
public class CreatePackageScene extends BaseScene {
    private final String packageName;
    private final String validity;
    private final String packageDescription;
    private final JSONArray voucherList;
    private final Double packagePrice;
    private final List<Long> shopIds;
    private final String subjectType;
    private final Long subjectId;
    private final Boolean status;
    private final Integer customerUseValidity;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("package_name", packageName);
        object.put("validity", validity);
        object.put("package_description", packageDescription);
        object.put("voucher_list", voucherList);
        object.put("package_price", packagePrice);
        object.put("shop_ids", shopIds);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("status", status);
        object.put("customer_use_validity", customerUseValidity);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/create-package";
    }

}
