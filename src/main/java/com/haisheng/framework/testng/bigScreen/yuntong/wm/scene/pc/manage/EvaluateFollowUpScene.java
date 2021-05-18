package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/manage/evaluate/follow-up";
    }
}