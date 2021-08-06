package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.followup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.2. app完成评价跟进（谢）（2020-12-15） (不用)
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppFollowUpCompleteScene extends BaseScene {
    /**
     * 描述 评价类型 评价类型为"销售接待线下评价"时，必传 5
     * 是否必填 false
     * 版本 v4.0
     */
    private final Integer evaluateType;

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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("evaluate_type", evaluateType);
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/follow-up/complete";
    }
}