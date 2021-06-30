package com.haisheng.framework.testng.bigScreen.xundian.scene.member.identity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.2. 会员身份添加
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class AddScene extends BaseScene {
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
     * 描述 身份名称
     * 是否必填 false
     * 版本 -
     */
    private final String identity;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("identity", identity);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/member/identity/add";
    }
}