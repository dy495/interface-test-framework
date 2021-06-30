package com.haisheng.framework.testng.bigScreen.itemXundian.util;

import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.PatrolLoginScene;
import com.haisheng.framework.util.MD5Util;

public class UserUtil {

    private final VisitorProxy visitorProxy;

    public UserUtil(VisitorProxy visitorProxy) {
        this.visitorProxy = visitorProxy;
    }

    public void loginPc(AccountEnum account) {
        String password = new MD5Util().getMD5(account.getPassword());
        IScene scene = PatrolLoginScene.builder().password(password).username(account.getUsername()).type(0).build();
        visitorProxy.login(scene);
    }
}
