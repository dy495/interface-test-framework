package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 新建活动
 */
@Builder
public class ArticleAdd extends BaseScene {
    private final String title;
    private final String picType;
    private final String picList;
    private final String content;
    private final String label;
    private final String startDate;
    private final String endDate;
    private final String registerStartDate;
    private final String registerEndDate;
    private final String contentType;
    private final Integer totalQuota;
    private final String address;
    private final String isCanMaintain;
    private final String isVoucher;
    private final List<Long> voucherList;
    private final String voucherReceiveType;
    private final String voucherStartDate;
    private final String voucherEndDate;
    private final String voucherGetUseDays;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("title", title);
        object.put("pic_type", picType);
        object.put("pic_list", picList);
        object.put("content", content);
        object.put("label", label);
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        object.put("register_start_date", registerStartDate);
        object.put("register_end_date", registerEndDate);
        object.put("content_type", contentType);
        object.put("total_quota", totalQuota);
        object.put("address", address);
        object.put("is_can_maintain", isCanMaintain);
        object.put("is_voucher", isVoucher);
        object.put("voucher_list", voucherList);
        object.put("voucher_receive_type", voucherReceiveType);
        object.put("voucher_start_date", voucherStartDate);
        object.put("voucher_end_date", voucherEndDate);
        object.put("voucher_get_use_days", voucherGetUseDays);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/operation/article/add";
    }
}
