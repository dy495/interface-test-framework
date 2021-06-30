package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 19.8. 登陆日志 (杨) v3.0
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
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
    public JSONObject getRequestBody() {
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
        return "/jiaochen/pc/record/login-record/page";
    }
}