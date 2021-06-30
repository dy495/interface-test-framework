package com.haisheng.framework.testng.bigScreen.itemXundian.scene.shop.check;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.14. 批量提交巡检项目结果
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class ItemSubmitAllItemScene extends BaseScene {
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
     * 描述 门店id
     * 是否必填 true
     * 版本 -
     */
    private final Long shopId;

    /**
     * 描述 巡检记录id
     * 是否必填 true
     * 版本 -
     */
    private final Long patrolId;

    /**
     * 描述 巡检清单id
     * 是否必填 true
     * 版本 -
     */
    private final Long listId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("shop_id", shopId);
        object.put("patrol_id", patrolId);
        object.put("list_id", listId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/check/item/submit_all_item";
    }
}