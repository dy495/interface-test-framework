package com.haisheng.framework.testng.bigScreen.xundian.scene.member.level;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.7. 会员等级隐藏状态更新 ljq
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class HideUpdateScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 是否隐藏
     * 是否必填 false
     * 版本 -
     */
    private final Boolean isHide;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("is_hide", isHide);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/member/level/hide_update";
    }
}