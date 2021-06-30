package com.haisheng.framework.testng.bigScreen.itemXundian.scene.applet.wechat.member;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.2. 会员信息 ljq
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class InfoScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/wechat/member/info";
    }
}