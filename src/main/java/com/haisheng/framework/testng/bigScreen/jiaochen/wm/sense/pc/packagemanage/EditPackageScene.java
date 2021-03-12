package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/package-manage/edit-package的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class EditPackageScene extends BaseScene {
    /**
     * 描述 门店列表
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONArray shopIds;

    /**
     * 描述 套餐名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String packageName;

    /**
     * 描述 有效期
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer validity;

    /**
     * 描述 客户使用有效期
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer customerUseValidity;

    /**
     * 描述 卡券列表
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONArray voucherList;

    /**
     * 描述 套餐价格
     * 是否必填 true
     * 版本 v1.0
     */
    private final Double packagePrice;

    /**
     * 描述 套餐说明
     * 是否必填 true
     * 版本 v1.0
     */
    private final String packageDescription;

    /**
     * 描述 是否开启状态
     * 是否必填 true
     * 版本 v1.0
     */
    private final Boolean status;

    /**
     * 描述 主体名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String subjectType;

    /**
     * 描述 主体id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long subjectId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("shop_ids", shopIds);
        object.put("package_name", packageName);
        object.put("validity", validity);
        object.put("customer_use_validity", customerUseValidity);
        object.put("voucher_list", voucherList);
        object.put("package_price", packagePrice);
        object.put("package_description", packageDescription);
        object.put("status", status);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/edit-package";
    }
}