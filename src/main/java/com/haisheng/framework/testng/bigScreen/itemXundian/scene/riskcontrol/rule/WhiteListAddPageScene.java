package com.haisheng.framework.testng.bigScreen.itemXundian.scene.riskcontrol.rule;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 48.3. 黑白名单名单新增列表(黑白名单共用)
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class WhiteListAddPageScene extends BaseScene {
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
     * 描述 姓名
     * 是否必填 false
     * 版本 门店 v4.1
     */
    private final String name;

    /**
     * 描述 会员id
     * 是否必填 false
     * 版本 门店 v4.1
     */
    private final String memberId;

    /**
     * 描述 人物id
     * 是否必填 false
     * 版本 门店 v4.1
     */
    private final String customerId;

    /**
     * 描述 类型 (枚举值: BLACK or WHITE)
     * 是否必填 true
     * 版本 门店v4.1
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("name", name);
        object.put("member_id", memberId);
        object.put("customer_id", customerId);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/risk-control/rule/white-list/add/page";
    }
}