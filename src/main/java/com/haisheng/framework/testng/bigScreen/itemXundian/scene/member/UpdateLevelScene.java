package com.haisheng.framework.testng.bigScreen.itemXundian.scene.member;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.4. 会员等级分配
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class UpdateLevelScene extends BaseScene {
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
     * 描述 会员id
     * 是否必填 false
     * 版本 -
     */
    private final String id;

    /**
     * 描述 等级id
     * 是否必填 false
     * 版本 -
     */
    private final Long levelId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("level_id", levelId);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/member/update_level";
    }
}