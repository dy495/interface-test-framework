package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginPc;

public class UserUtil {
    private final Visitor visitor;

    public UserUtil(Visitor visitor) {
        this.visitor = visitor;
    }

    /**
     * pc账号登录
     *
     * @param enumAccount 账号
     */
    public void loginPc(EnumAccount enumAccount) {
        IScene scene = LoginPc.builder().phone(enumAccount.getPhone()).verificationCode(enumAccount.getPassword()).build();
        visitor.login(scene);
    }

    /**
     * 小程序账号登录
     *
     * @param appletToken 小程序token
     */
    public void loginApplet(EnumAppletToken appletToken) {
        visitor.appletLogin(appletToken.getToken());
    }
}