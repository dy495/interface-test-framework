package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.messagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 45.3. 消息发送客户总数
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class MessageManageGroupTotalScene extends BaseScene {
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
     * 描述 推送目标 0：全部客户，1：门店客户，2：个人客户
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer pushTarget;

    /**
     * 描述 门店列表 推送目标为门店客户时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONArray shopList;

    /**
     * 描述 客户手机号列表 推送目标个人客户时时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONArray telList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("push_target", pushTarget);
        object.put("shop_list", shopList);
        object.put("tel_list", telList);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pc/message-manage/group-total";
    }
}