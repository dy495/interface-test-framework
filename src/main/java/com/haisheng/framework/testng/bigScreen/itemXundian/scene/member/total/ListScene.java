package com.haisheng.framework.testng.bigScreen.itemXundian.scene.member.total;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.6. 门店客户列表页
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
     * 描述 门店id
     * 是否必填 false
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 人物id
     * 是否必填 false
     * 版本 -
     */
    private final String customerId;

    /**
     * 描述 首次到店
     * 是否必填 false
     * 版本 -
     */
    private final String firstVisitShop;

    /**
     * 描述 顾客姓名
     * 是否必填 false
     * 版本 -
     */
    private final String name;

    /**
     * 描述 会员id
     * 是否必填 false
     * 版本 -
     */
    private final String memberId;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("customer_id", customerId);
        object.put("first_visit_shop", firstVisitShop);
        object.put("name", name);
        object.put("member_id", memberId);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/member/total/list";
    }
}