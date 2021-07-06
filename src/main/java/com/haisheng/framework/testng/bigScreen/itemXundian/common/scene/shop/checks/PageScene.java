package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop.checks;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.4. 获取门店巡店记录列表
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class PageScene extends BaseScene {
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
     * 是否必填 true
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 巡店结果（0：合格，1：不合格）
     * 是否必填 false
     * 版本 -
     */
    private final Integer checkResult;

    /**
     * 描述 处理状态（0：无需处理，1：已处理，2：待处理）
     * 是否必填 false
     * 版本 -
     */
    private final Integer handleStatus;

    /**
     * 描述 巡店员id
     * 是否必填 false
     * 版本 -
     */
    private final String inspectorId;

    /**
     * 描述 巡店时间正、倒序（ASC：时间由旧到新；DESC：按时间由新到旧顺序 默认为DESC）
     * 是否必填 false
     * 版本 -
     */
    private final String orderRule;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("check_result", checkResult);
        object.put("handle_status", handleStatus);
        object.put("inspector_id", inspectorId);
        object.put("order_rule", orderRule);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/checks/page";
    }
}