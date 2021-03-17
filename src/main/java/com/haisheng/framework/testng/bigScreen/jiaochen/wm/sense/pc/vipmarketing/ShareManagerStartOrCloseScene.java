package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/vip-marketing/share-manager/start-or-close的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:17
 */
@Builder
public class ShareManagerStartOrCloseScene extends BaseScene {
    /**
     * 描述 任务id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 状态
     * 是否必填 true
     * 版本 v2.0
     */
    private final String status;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/share-manager/start-or-close";
    }
}