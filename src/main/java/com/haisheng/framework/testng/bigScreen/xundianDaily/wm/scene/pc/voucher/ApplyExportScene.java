package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.2. 卡券审批申请导出 （华成裕） （2020-12-24）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class ApplyExportScene extends BaseScene {
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
     * 描述 优惠券名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String name;

    /**
     * 描述 申请人姓名
     * 是否必填 false
     * 版本 v3.0
     */
    private final String applyName;

    /**
     * 描述 优惠券状态 通用枚举接口获取，key为 VOUCHER_AUDIT_STATUS_LIST
     * 是否必填 false
     * 版本 v3.0
     */
    private final Integer status;

    /**
     * 描述 所属主体
     * 是否必填 false
     * 版本 v3.0
     */
    private final String subjectType;

    /**
     * 描述 主体id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long subjectId;

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
        object.put("name", name);
        object.put("apply_name", applyName);
        object.put("status", status);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pc/voucher/apply/export";
    }
}