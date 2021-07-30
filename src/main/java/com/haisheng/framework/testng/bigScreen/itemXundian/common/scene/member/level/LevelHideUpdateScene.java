package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.member.level;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 43.7. 会员等级隐藏状态更新 ljq
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class LevelHideUpdateScene extends BaseScene {
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