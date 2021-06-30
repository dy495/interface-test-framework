package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.5. 增发记录 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class AdditionalRecordScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 卡券id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long voucherId;

    /**
     * 描述 卡券名称
     * 是否必填 false
     * 版本 v2.2
     */
    private final String voucherName;

    /**
     * 描述 增发开始时间
     * 是否必填 false
     * 版本 v2.2
     */
    private final String addStartTime;

    /**
     * 描述 增发结束时间
     * 是否必填 false
     * 版本 v2.2
     */
    private final String addEndTime;

    /**
     * 描述 操作人名称
     * 是否必填 false
     * 版本 v2.2
     */
    private final String saleName;

    /**
     * 描述 操作人手机号
     * 是否必填 false
     * 版本 v2.2
     */
    private final String salePhone;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("voucherId", voucherId);
        object.put("voucher_name", voucherName);
        object.put("add_start_time", addStartTime);
        object.put("add_end_time", addEndTime);
        object.put("sale_name", saleName);
        object.put("sale_phone", salePhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/additional-record";
    }
}