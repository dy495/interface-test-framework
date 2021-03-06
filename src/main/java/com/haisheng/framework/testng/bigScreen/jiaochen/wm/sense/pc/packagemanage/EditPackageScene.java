package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 套餐管理 -> 修改套餐
 */
@Builder
public class EditPackageScene extends BaseScene {
    private final Long packageId;
    private final String packageName;
    private final String packageDescription;
    private final JSONArray voucherList;
    private final String packagePrice;
    private final List<Long> shopIds;
    private final Boolean status;
    private final String id;
    private final String subjectType;
    private final Long subjectId;
    private final Integer expireType;
    private final Integer expiryDate;
    private final String beginUseTime;
    private final String endUseTime;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("package_name", packageName);
        object.put("package_description", packageDescription);
        object.put("voucher_list", voucherList);
        object.put("package_price", packagePrice);
        object.put("shop_ids", shopIds);
        object.put("status", status);
        object.put("id", id);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("expiry_date", expiryDate);
        object.put("expire_type", expireType);
        object.put("begin_use_time", beginUseTime);
        object.put("end_use_time", endUseTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/package-manage/edit-package";
    }
}
