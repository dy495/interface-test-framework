package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.16. 签到积分变更记录 (池) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class SignInConfigChangeRecordScene extends BaseScene {
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
     * 描述 签到id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long signInConfigId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("sign_in_config_id", signInConfigId);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/vip-marketing/sign_in_config/change-record";
    }
}