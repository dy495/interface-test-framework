package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.organization.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 32.5. 账号编辑
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
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
     * 描述 账号id
     * 是否必填 true
     * 版本 -
     */
    private final String account;

    /**
     * 描述 名称
     * 是否必填 true
     * 版本 -
     */
    private final String name;

    /**
     * 描述 员工工号
     * 是否必填 true
     * 版本 -
     */
    private final String number;

    /**
     * 描述 上级领导
     * 是否必填 true
     * 版本 -
     */
    private final String leaderUid;

    /**
     * 描述 登陆类型
     * 是否必填 true
     * 版本 -
     */
    private final String type;

    /**
     * 描述 邮箱
     * 是否必填 true
     * 版本 -
     */
    private final String email;

    /**
     * 描述 手机号
     * 是否必填 true
     * 版本 -
     */
    private final String phone;

    /**
     * 描述 状态 1启用，0禁用 默认启用
     * 是否必填 true
     * 版本 -
     */
    private final Integer status;

    /**
     * 描述 角色列表
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray roleIdList;

    /**
     * 描述 门店列表
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray shopIdList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("account", account);
        object.put("name", name);
        object.put("number", number);
        object.put("leaderUid", leaderUid);
        object.put("type", type);
        object.put("email", email);
        object.put("phone", phone);
        object.put("status", status);
        object.put("roleIdList", roleIdList);
        object.put("shopIdList", shopIdList);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/organization/account/edit";
    }
}