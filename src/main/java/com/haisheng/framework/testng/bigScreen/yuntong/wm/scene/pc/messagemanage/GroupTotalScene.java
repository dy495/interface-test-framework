package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.messagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.3. 消息发送客户总数 （谢） （2020-12-18）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class GroupTotalScene extends BaseScene {
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
        object.put("push_target", pushTarget);
        object.put("shop_list", shopList);
        object.put("tel_list", telList);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/message-manage/group-total";
    }
}