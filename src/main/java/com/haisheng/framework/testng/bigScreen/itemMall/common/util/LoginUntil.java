package com.haisheng.framework.testng.bigScreen.itemMall.common.util;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.LoginPcMall;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginPc;
import com.haisheng.framework.util.MD5Util;

import javax.validation.constraints.NotNull;

public class LoginUntil {

    private final VisitorProxy visitor;

    public LoginUntil(VisitorProxy visitorProxy) {
        this.visitor = visitorProxy;
    }

//    public void loginPc(@NotNull AccountEnum enumAccount) {
//        String password = new MD5Util().getMD5(enumAccount.getPassword());
//        IScene scene = LoginPcMall.builder().password(password).username(enumAccount.getUsername()).type(0).build();
//        visitor.setToken(scene);
//    }
    public void loginPc(@NotNull AccountEnum enumAccount) {
        String password = new MD5Util().getMD5(enumAccount.getPassword());
        IScene scene = LoginPcMall.builder().password(password).username(enumAccount.getUsername()).type(0).build();
        login(scene);
    }
    public void login(IScene scene) {
        EnumTestProduct oldProduce = visitor.getProduct();
        EnumTestProduct newProduce = visitor.isDaily() ? EnumTestProduct.MALL_DAILY_SSO : EnumTestProduct.MALL_ONLINE_SSO;
        visitor.setProduct(newProduce);
        visitor.setToken(scene);
        visitor.setProduct(oldProduce);
    }
}
