package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/reception-manage/purchase-fixed-package的接口
 *
 * @author wangmin
 * @date 2021-03-15 10:02:41
 */
@Builder
public class PurchaseFixedPackageScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long receptionId;

    /**
     * 描述 接待所属门店id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long shopId;

    /**
     * 描述 是否限定车辆 0：接待车辆，1：全部车辆
     * 是否必填 true
     * 版本 v2.0
     */
    private final String carType;

    /**
     * 描述 套餐有效期
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer expiryDate;

    /**
     * 描述 套餐说明
     * 是否必填 false
     * 版本 v1.0
     */
    private final String remark;

    /**
     * 描述 主体类型
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

    /**
     * 描述 延保年数
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer extendedInsuranceYear;

    /**
     * 描述 延保分数
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer extendedInsuranceCopies;

    /**
     * 描述 购买类型，赠送/购买
     * 是否必填 false
     * 版本 -
     */
    private final Integer type;

    /**
     * 描述 购买套餐列表
     * 是否必填 true
     * 版本 v2.0
     */
    private final JSONArray packageId;

    /**
     * 描述 套餐价格
     * 是否必填 true
     * 版本 v2.0
     */
    private final Double packagePrice;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        object.put("shop_id", shopId);
        object.put("car_type", carType);
        object.put("expiry_date", expiryDate);
        object.put("remark", remark);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("extended_insurance_year", extendedInsuranceYear);
        object.put("extended_insurance_copies", extendedInsuranceCopies);
        object.put("type", type);
        object.put("package_id", packageId);
        object.put("package_price", packagePrice);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/reception-manage/purchase-fixed-package";
    }
}