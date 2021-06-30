package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.18. 作废记录 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class VoucherInvalidPageScene extends BaseScene {
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
    private final Long id;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String receiver;

    /**
     * 描述 领取人手机号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String receivePhone;

    /**
     * 描述 领取开始时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String startTime;

    /**
     * 描述 领取结束时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String endTime;

    /**
     * 描述 作废人名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String invalidName;

    /**
     * 描述 作废人手机号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String invalidPhone;

    /**
     * 描述 作废开始时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String invalidStartTime;

    /**
     * 描述 作废结束时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String invalidEndTime;

    /**
     * 描述 卡券名称
     * 是否必填 false
     * 版本 v2.2
     */
    private final String voucherName;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("id", id);
        object.put("receiver", receiver);
        object.put("receive_phone", receivePhone);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("invalidName", invalidName);
        object.put("invalidPhone", invalidPhone);
        object.put("invalid_start_time", invalidStartTime);
        object.put("invalid_end_time", invalidEndTime);
        object.put("voucher_name", voucherName);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/voucher-manage/voucher-invalid-page";
    }
}