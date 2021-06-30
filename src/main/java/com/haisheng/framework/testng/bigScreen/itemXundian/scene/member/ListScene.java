package com.haisheng.framework.testng.bigScreen.itemXundian.scene.member;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.1. 会员信息列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ListScene extends BaseScene {
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
     * 描述 注册日期
     * 是否必填 false
     * 版本 -
     */
    private final String registerDate;

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


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("register_date", registerDate);
        object.put("member_name", memberName);
        object.put("phone", phone);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/member/list";
    }
}