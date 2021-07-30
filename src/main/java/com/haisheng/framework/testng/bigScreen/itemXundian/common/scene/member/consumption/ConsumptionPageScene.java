package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.member.consumption;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 40.1. 门店消费记录列表 lj
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class ConsumptionPageScene extends BaseScene {
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
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 下单会员名称
     * 是否必填 false
     * 版本 -
     */
    private final String memberName;

    /**
     * 描述 下单会员手机号
     * 是否必填 false
     * 版本 -
     */
    private final String memberPhone;

    /**
     * 描述 用户id
     * 是否必填 false
     * 版本 -
     */
    private final String userId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("shop_name", shopName);
        object.put("member_name", memberName);
        object.put("member_phone", memberPhone);
        object.put("user_id", userId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/member/consumption/page";
    }
}