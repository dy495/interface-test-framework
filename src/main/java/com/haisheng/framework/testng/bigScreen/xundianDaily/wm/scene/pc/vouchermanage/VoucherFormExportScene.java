package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.21. 卡券表单导出 （华成裕） v2.0
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class VoucherFormExportScene extends BaseScene {
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
     * 描述 状态
     * 是否必填 false
     * 版本 v2.0
     */
    private final String voucherStatus;

    /**
     * 描述 优惠券类型
     * 是否必填 false
     * 版本 v2.0
     */
    private final String voucherType;

    /**
     * 描述 卡券名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String voucherName;

    /**
     * 描述 归属
     * 是否必填 false
     * 版本 v2.0
     */
    private final String subjectName;

    /**
     * 描述 创建者姓名
     * 是否必填 false
     * 版本 v2.0
     */
    private final String creatorName;

    /**
     * 描述 创建者账号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String creatorAccount;

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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("voucher_status", voucherStatus);
        object.put("voucher_type", voucherType);
        object.put("voucher_name", voucherName);
        object.put("subject_name", subjectName);
        object.put("creator_name", creatorName);
        object.put("creator_account", creatorAccount);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher-manage/voucher-form/export";
    }
}