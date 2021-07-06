package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.followup;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.2. app完成评价跟进（谢）（2020-12-15） (不用)
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppCompleteScene extends BaseScene {
    /**
     * 描述 跟进任务id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 跟进任务所属门店id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long shopId;

    /**
     * 描述 跟进备注
     * 是否必填 true
     * 版本 v2.0
     */
    private final String remark;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/follow-up/complete";
    }
}