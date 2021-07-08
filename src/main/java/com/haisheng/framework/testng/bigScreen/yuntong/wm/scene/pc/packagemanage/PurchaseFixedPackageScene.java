package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 17.8. 购买固定套餐 v1.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class PurchaseFixedPackageScene extends BaseScene {
    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 -
     */
    private final String customerPhone;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 -
     */
    private final String customerName;

    /**
     * 描述 套餐列表
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray packageId;

    /**
     * 描述 临时套餐 - 选择卡券列表
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray voucherList;

    /**
     * 描述 套餐价格
     * 是否必填 false
     * 版本 -
     */
    private final Double packagePrice;

    /**
     * 描述 套餐描述
     * 是否必填 false
     * 版本 -
     */
    private final String packageDescription;

    /**
     * 描述 车牌类型[enum:]
     * 是否必填 false
     * 版本 -
     */
    private final String carType;

    /**
     * 描述 车牌号码
     * 是否必填 false
     * 版本 -
     */
    private final String plateNumber;

    /**
     * 描述 卡券有效期类型 选择发送卡券时必填，1：时间段，2：有效天数(20210316)
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer expireType;

    /**
     * 描述 有效期
     * 是否必填 false
     * 版本 -
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
     * 描述 备注
     * 是否必填 false
     * 版本 -
     */
    private final String remark;

    /**
     * 描述 主体类型
     * 是否必填 false
     * 版本 -
     */
    private final String subjectType;

    /**
     * 描述 主体id
     * 是否必填 false
     * 版本 -
     */
    private final Long subjectId;

    /**
     * 描述 延保年数
     * 是否必填 false
     * 版本 -
     */
    private final Integer extendedInsuranceYear;

    /**
     * 描述 延保份数
     * 是否必填 false
     * 版本 -
     */
    private final Integer extendedInsuranceCopies;

    /**
     * 描述 发出方式
     * 是否必填 false
     * 版本 -
     */
    private final Integer type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        object.put("customer_name", customerName);
        object.put("package_id", packageId);
        object.put("voucher_list", voucherList);
        object.put("package_price", packagePrice);
        object.put("package_description", packageDescription);
        object.put("car_type", carType);
        object.put("plate_number", plateNumber);
        object.put("expire_type", expireType);
        object.put("expiry_date", expiryDate);
        object.put("begin_use_time", beginUseTime);
        object.put("end_use_time", endUseTime);
        object.put("remark", remark);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("extended_insurance_year", extendedInsuranceYear);
        object.put("extended_insurance_copies", extendedInsuranceCopies);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/package-manage/purchase-fixed-package";
    }
}