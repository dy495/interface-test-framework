package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 21.6. 增发记录-导出 （华成裕） v2.0
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class AdditionalRecordExportScene extends BaseScene {
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

    /**
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页，SPECIFIED_DATA：导出特定数据
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exportType;

    /**
     * 描述 导出数据id列表，特定数据时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray ids;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("voucherId", voucherId);
        object.put("voucher_name", voucherName);
        object.put("add_start_time", addStartTime);
        object.put("add_end_time", addEndTime);
        object.put("sale_name", saleName);
        object.put("sale_phone", salePhone);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/additional-record/export";
    }
}