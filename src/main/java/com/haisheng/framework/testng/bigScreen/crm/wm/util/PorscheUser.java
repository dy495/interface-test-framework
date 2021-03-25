package com.haisheng.framework.testng.bigScreen.crm.wm.util;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.PorscheLoginScene;
import org.jetbrains.annotations.NotNull;

public class PorscheUser {
    private final VisitorProxy visitor;

    public PorscheUser(VisitorProxy visitor) {
        this.visitor = visitor;
    }

    /**
     * pc账号登录
     *
     * @param enumAccount 账号
     */
    public void login(@NotNull EnumAccount enumAccount) {
        IScene scene = PorscheLoginScene.builder().username(enumAccount.getUsername()).password(enumAccount.getPassword()).build();
        visitor.login(scene);
    }

    /**
     * 小程序账号登录
     *
     * @param appletToken 小程序token
     */
    public void loginApplet(EnumAppletToken appletToken) {
        visitor.login(appletToken.getToken());
    }
}
