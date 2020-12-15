package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAppletCode;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;

public class LoginUtil {
    ScenarioUtilOnline jcOnline = ScenarioUtilOnline.getInstance();
    ScenarioUtil jcDaily = ScenarioUtil.getInstance();

    /**
     * 账号登录
     *
     * @param account 账号
     */
    public void login(EnumAccount account) {
        if (account.isDaily()) {
            jcDaily.pcLogin(account.getPhone(), account.getPassword());
        } else {
            jcOnline.pcLogin(account.getPhone(), account.getPassword());
        }
    }

    public void loginApplet(EnumAppletCode appletCode) {
        if (appletCode.isDaily()) {
            jcDaily.appletLoginToken(appletCode.getToken());
        } else {
            jcOnline.appletLoginToken(appletCode.getToken());
        }
    }
}
