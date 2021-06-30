package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.20. 分享管理(修改前获取根据任务id查询) (池) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class ShareManagerEditPreScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long taskId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("task_id", taskId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/share-manager/edit-pre";
    }
}