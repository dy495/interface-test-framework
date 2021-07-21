package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 18.3. 完成评价跟进（谢）（2021-01-08）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class EvaluateFollowUpScene extends BaseScene {
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
    private final Integer evaluate_type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("remark", remark);
        object.put("evaluate_type", evaluate_type);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/manage/evaluate/follow-up";
    }
}