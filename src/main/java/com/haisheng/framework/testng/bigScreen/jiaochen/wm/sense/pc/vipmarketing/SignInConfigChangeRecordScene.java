package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 积分变更记录
 *
 * @author wangmin
 * @date 2021/2/1 17:02
 */
@Builder
public class SignInConfigChangeRecordScene extends BaseScene {

    @Builder.Default
    private Integer size = 10;
    @Builder.Default
    private Integer page = 1;

    /**
     * 签到任务id
     */
    private final Integer signInConfigId;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        object.put("sign_in_config_id", signInConfigId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/sign_in_config/change-record";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}
