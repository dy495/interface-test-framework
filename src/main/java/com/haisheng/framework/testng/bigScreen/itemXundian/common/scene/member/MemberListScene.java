package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.member;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 41.1. 会员信息列表
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class MemberListScene extends BaseScene {
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
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 会员id
     * 是否必填 false
     * 版本 -
     */
    private final String memberId;

    /**
     * 描述 会员名称
     * 是否必填 false
     * 版本 -
     */
    private final String memberName;

    /**
     * 描述 联系电话
     * 是否必填 false
     * 版本 -
     */
    private final String phone;

    /**
     * 描述 人物id
     * 是否必填 false
     * 版本 -
     */
    private final String userId;

    /**
     * 描述 会员身份
     * 是否必填 false
     * 版本 -
     */
    private final Long identity;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("member_id", memberId);
        object.put("member_name", memberName);
        object.put("phone", phone);
        object.put("user_id", userId);
        object.put("identity", identity);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/member/list";
    }
}