package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.receptionmanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 28.9. 购买固定套餐（张小龙）(2020-12-17)
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class ReceptionManagePurchaseFixedPackageScene extends BaseScene {
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
     * 描述 有效期开始时间
     * 是否必填 false
     * 版本 v2.2
     */
    private final String beginUseTime;

    /**
     * 描述 有效期结束时间
     * 是否必填 false
     * 版本 v2.2
     */
    private final String endUseTime;

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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        object.put("shop_id", shopId);
        object.put("car_type", carType);
        object.put("expiry_date", expiryDate);
        object.put("begin_use_time", beginUseTime);
        object.put("end_use_time", endUseTime);
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
        return "/car-platform/pc/reception-manage/purchase-fixed-package";
    }
}