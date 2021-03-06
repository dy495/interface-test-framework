package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.1. 卡券审批申请分页 （张小龙）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class ApplyPageScene extends BaseScene {
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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        object.put("apply_name", applyName);
        object.put("status", status);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/voucher/apply/page";
    }
}