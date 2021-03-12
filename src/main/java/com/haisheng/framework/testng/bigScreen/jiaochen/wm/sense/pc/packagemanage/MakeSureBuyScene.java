package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/package-manage/make-sure-buy的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class MakeSureBuyScene extends BaseScene {
    /**
     * 描述 套餐id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 套餐审核状态 AUDITING(0, "审核中"), AGREE(1,"已通过"),REFUSAL(2,"已拒绝"),
     * 是否必填 false
     * 版本 v2.0
     */
    private final String status;

    /**
     * 描述 确认购买状态 AGREE(1,"已通过")
     * 是否必填 false
     * 版本 -
     */
    private final String auditStatus;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        object.put("audit_status", auditStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/make-sure-buy";
    }
}