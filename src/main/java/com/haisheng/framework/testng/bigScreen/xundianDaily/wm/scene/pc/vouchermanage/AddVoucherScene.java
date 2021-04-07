package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 卡券增发接口
 *
 * @author wangmin
 * @date 2020-12-29
 */
@Builder
public class AddVoucherScene extends BaseScene {
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;
    private final Long id;
    private final List<Long> shopIds;
    private final Integer addNumber;
    private final String plateNumber;
    private final String voucherStatus;
    private final String voucherType;
    private final String voucherName;
    private final String creator;
    private final String sender;
    private final Boolean isDiff;
    private final Boolean isSelfVerification;
    private final String transferPhone;
    private final String receivePhone;
    private final List<Integer> voucherIds;
    private final Integer verificationTime;
    private final String voucherPic;
    private final String voucherDescription;
    private final Integer stock;
    private final Double cost;
    private final Integer shopType;
    private final Boolean selfVerification;
    private final String startTime;
    private final String endTime;
    private final Integer customerId;
    private final Integer expiryData;
    private final Integer beginUseTime;
    private final Integer endUseTime;
    private final Integer customerPackageId;
    private final String subjectType;
    private final Integer subjectId;
    private final String subjectName;
    private final String sendChannelEnum;
    private final String sendWayEnum;
    private final String source;
    private final Integer sourceId;
    private final Boolean status;
    private final Integer intVoucherStatus;
    private final List<String> senderList;
    private final Boolean isAdditional;
    private final String packageName;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        object.put("shop_ids", shopIds);
        object.put("id", id);
        object.put("add_number", addNumber);
        object.put("plate_number", plateNumber);
        object.put("voucher_status", voucherStatus);
        object.put("voucher_type", voucherType);
        object.put("voucher_name", voucherName);
        object.put("creator", creator);
        object.put("sender", sender);
        object.put("is_diff", isDiff);
        object.put("is_self_verification", isSelfVerification);
        object.put("transfer_phone", transferPhone);
        object.put("receive_phone", receivePhone);
        object.put("voucher_ids", voucherIds);
        object.put("verification_time", verificationTime);
        object.put("voucher_pic", voucherPic);
        object.put("voucher_description", voucherDescription);
        object.put("stock", stock);
        object.put("cost", cost);
        object.put("shop_type", shopType);
        object.put("self_verification", selfVerification);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("customer_id", customerId);
        object.put("expiry_data", expiryData);
        object.put("begin_use_time", beginUseTime);
        object.put("end_use_time", endUseTime);
        object.put("customer_package_id", customerPackageId);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("subject_name", subjectName);
        object.put("send_channel_enum", sendChannelEnum);
        object.put("send_way_enum", sendWayEnum);
        object.put("source", source);
        object.put("source_id", subjectId);
        object.put("status", status);
        object.put("int_voucher_status", intVoucherStatus);
        object.put("sender_list", senderList);
        object.put("is_additional", isAdditional);
        object.put("package_name", packageName);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pc/voucher-manage/add-voucher";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}
