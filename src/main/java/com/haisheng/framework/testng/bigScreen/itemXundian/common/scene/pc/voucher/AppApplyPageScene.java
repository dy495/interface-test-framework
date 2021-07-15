package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.1. 卡券审批申请分页 （张小龙）
 *
 * @author wangmin
 * @date 2021-07-14 14:30:21
 */
@Builder
public class AppApplyPageScene extends BaseScene {
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
     * 描述 项目名称
     * 是否必填 false
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 申请人姓名
     * 是否必填 false
     * 版本 v1.0
     */
    private final String applyName;

    /**
     * 描述 状态 通用枚举接口获取，key为 VOUCHER_AUDIT_STATUS_LIST
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer status;

    /**
     * 描述 申请门店
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long applyGroup;

    /**
     * 描述 申请时间范围查询起始日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String startTime;

    /**
     * 描述 申请时间范围查询结束日期
     * 是否必填 false
     * 版本 v1.0
     */
    private final String endTime;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        object.put("apply_name", applyName);
        object.put("status", status);
        object.put("apply_group", applyGroup);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/voucher/apply/page";
    }
}