package com.haisheng.framework.testng.bigScreen.itemCms.common.util;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemCms.common.enumerator.EnumCloudSceneType;
import com.haisheng.framework.testng.bigScreen.itemCms.common.scene.LoginScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;

public class ScenarioUtil extends BasicUtil {
    private final VisitorProxy visitor;

    public ScenarioUtil(VisitorProxy visitor) {
        super(visitor);
        this.visitor = visitor;
    }

    public void login(EnumAccount account) {
        String email = "28e3e02ba627a44c949d3ef94b217388";
        IScene scene = LoginScene.builder().email(account.getPhone()).password(email).build();
        visitor.login(scene);
    }


    public Long getDeploymentGroupId() {
        return visitor.isDaily() ? 72L : 0L;
    }

    public Long getDeploymentId() {
        return visitor.isDaily() ? 256L : 0L;
    }

    public String getCloudSceneType() {
        return EnumCloudSceneType.AUTO_DEFAULT.name();
    }
}
