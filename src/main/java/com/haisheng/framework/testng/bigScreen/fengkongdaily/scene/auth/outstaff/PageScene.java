package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.outstaff;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.1. 外部员工分页
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class PageScene extends BaseScene {
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
     * 描述 是否导出
     * 是否必填 false
     * 版本 v1.0
     */
    private final Boolean isExport;

    /**
     * 描述 外部员工账号Id
     * 是否必填 false
     * 版本 v1.0
     */
    private final String outStaffId;

    /**
     * 描述 外部员工账号名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String outStaffName;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("is_export", isExport);
        object.put("out_staff_id", outStaffId);
        object.put("out_staff_name", outStaffName);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/out-staff/page";
    }
}