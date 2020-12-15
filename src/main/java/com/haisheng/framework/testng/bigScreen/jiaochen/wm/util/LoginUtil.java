package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
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

    public void loginApplet(EnumAppletToken appletToken) {
        if (appletToken.isDaily()) {
            jcDaily.appletLoginToken(appletToken.getToken());
        } else {
            jcOnline.appletLoginToken(appletToken.getToken());
        }
    }
}
