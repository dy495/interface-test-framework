package com.haisheng.framework.testng.bigScreen.itemMall.common.util;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.LoginPcMall;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.UserUtil;
import com.haisheng.framework.util.MD5Util;

public class LoginUntil {

    private final VisitorProxy visitorProxy;

    public LoginUntil(VisitorProxy visitorProxy) {
        this.visitorProxy = visitorProxy;
    }

    public void loginPc(AccountEnum account) {
        String password = new MD5Util().getMD5(account.getPassword());
        IScene scene = LoginPcMall.builder().password(password).username(account.getUsername()).type(0).build();
        visitorProxy.setToken(scene);
    }
}
