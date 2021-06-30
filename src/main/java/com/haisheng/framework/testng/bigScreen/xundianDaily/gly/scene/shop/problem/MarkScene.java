package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.shop.problem;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 36.18. 留痕
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class MarkScene extends BaseScene {
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
     * 描述 清单id
     * 是否必填 true
     * 版本 -
     */
    private final Long listId;

    /**
     * 描述 问题项id
     * 是否必填 true
     * 版本 -
     */
    private final Long itemId;

    /**
     * 描述 问题描述
     * 是否必填 true
     * 版本 -
     */
    private final String auditComment;

    /**
     * 描述 责任人id
     * 是否必填 true
     * 版本 -
     */
    private final String responsorId;

    /**
     * 描述 留痕照片
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray picList;

    /**
     * 描述 限时整改时间
     * 是否必填 false
     * 版本 -
     */
    private final Integer limitTime;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("shop_id", shopId);
        object.put("list_id", listId);
        object.put("item_id", itemId);
        object.put("audit_comment", auditComment);
        object.put("responsor_id", responsorId);
        object.put("pic_list", picList);
        object.put("limit_time", limitTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/shop/problem/mark";
    }
}