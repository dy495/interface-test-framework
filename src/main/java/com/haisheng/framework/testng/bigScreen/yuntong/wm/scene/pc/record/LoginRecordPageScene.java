package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.record;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.8. 登陆日志 (杨) v3.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class LoginRecordPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 登陆开始日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String login_date;

    /**
     * 描述 登陆名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String loginName;

    /**
     * 描述 登陆账号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String loginAccount;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("login__date", login_date);
        object.put("login_name", loginName);
        object.put("login_account", loginAccount);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/record/login-record/page";
    }
}