package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.4. 增发记录 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class AdditionalRecordScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
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
        return "/yt/pc/voucher-manage/additional-record";
    }
}