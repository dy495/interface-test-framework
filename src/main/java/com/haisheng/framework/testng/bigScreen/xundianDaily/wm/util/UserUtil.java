package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.PatrolLoginScene;
import com.haisheng.framework.util.MD5Util;

public class UserUtil {

    private final VisitorProxy visitorProxy;

    public UserUtil(VisitorProxy visitorProxy) {
        this.visitorProxy = visitorProxy;
    }

    public void loginPc(EnumAccount account) {
        String password = new MD5Util().getMD5(account.getPassword());
        IScene scene = PatrolLoginScene.builder().password(password).username(account.getUsername()).type(0).build();
        visitorProxy.login(scene);
    }
}
