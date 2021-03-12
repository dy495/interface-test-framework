package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/package-manage/purchase-temporary-package的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class PurchaseTemporaryPackageScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray shopIds;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Boolean packageStatus;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String packageName;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String creator;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String startTime;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String endTime;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long startTimeToLong;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long endTimeToLong;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Integer validity;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray voucherList;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Double packagePrice;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String packageDescription;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long customerId;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String customerName;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String customerPhone;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray packageId;

    /**
     * 描述 null(enum:)
     * 是否必填 false
     * 版本 -
     */
    private final String carType;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Integer selectNumber;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String plateNumber;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Double price;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Integer expiryDate;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String remark;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Integer extendedInsuranceYear;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Integer extendedInsuranceCopies;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Integer type;

    /**
     * 描述 null(enum:)
     * 是否必填 false
     * 版本 -
     */
    private final String useType;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String sender;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Integer sendType;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Boolean sendStatus;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Boolean status;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray saleIdList;

    /**
     * 描述 null(enum:)
     * 是否必填 false
     * 版本 -
     */
    private final String auditStatus;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Integer intPackageStatus;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final JSONArray senderList;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String subjectType;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long subjectId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_ids", shopIds);
        object.put("package_status", packageStatus);
        object.put("package_name", packageName);
        object.put("creator", creator);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("startTimeToLong", startTimeToLong);
        object.put("endTimeToLong", endTimeToLong);
        object.put("shop_id", shopId);
        object.put("validity", validity);
        object.put("voucher_list", voucherList);
        object.put("package_price", packagePrice);
        object.put("package_description", packageDescription);
        object.put("customer_id", customerId);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("package_id", packageId);
        object.put("car_type", carType);
        object.put("select_number", selectNumber);
        object.put("plate_number", plateNumber);
        object.put("price", price);
        object.put("expiry_date", expiryDate);
        object.put("remark", remark);
        object.put("extended_insurance_year", extendedInsuranceYear);
        object.put("extended_insurance_copies", extendedInsuranceCopies);
        object.put("type", type);
        object.put("use_type", useType);
        object.put("sender", sender);
        object.put("send_type", sendType);
        object.put("send_status", sendStatus);
        object.put("status", status);
        object.put("saleIdList", saleIdList);
        object.put("audit_status", auditStatus);
        object.put("intPackageStatus", intPackageStatus);
        object.put("senderList", senderList);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/purchase-temporary-package";
    }
}