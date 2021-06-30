package com.haisheng.framework.testng.bigScreen.itemXundian.scene.checklist;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 45.5. 编辑执行清单
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class EditScene extends BaseScene {
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
     * 描述 定检任务名称
     * 是否必填 true
     * 版本 -
     */
    private final String name;

    /**
     * 描述 描述
     * 是否必填 true
     * 版本 -
     */
    private final String desc;

    /**
     * 描述 门店idList
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray shopList;

    /**
     * 描述 清单项
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray items;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("name", name);
        object.put("desc", desc);
        object.put("shop_list", shopList);
        object.put("items", items);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/check-list/edit";
    }
}