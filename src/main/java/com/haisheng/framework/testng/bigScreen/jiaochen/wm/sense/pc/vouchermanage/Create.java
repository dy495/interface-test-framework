package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 卡券管理 -> 新建卡券
 */
@Builder
public class Create extends BaseScene {
    private final String voucherPic;
    private final String voucherName;
    private final String voucherDescription;
    private final Long stock;
    private final Double cost;
    private final Integer shopType;
    private final List<Long> shopIds;
    private final Boolean selfVerification;
    private final String subjectType;
    private final Long subjectId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("voucher_pic", voucherPic);
        object.put("voucher_name", voucherName);
        object.put("voucher_description", voucherDescription);
        object.put("stock", stock);
        object.put("cost", cost);
        object.put("shop_type", shopType);
        object.put("shop_ids", shopIds);
        object.put("self_verification", selfVerification);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/create";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
