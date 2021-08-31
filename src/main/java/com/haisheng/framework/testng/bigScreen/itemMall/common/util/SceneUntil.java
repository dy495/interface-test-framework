package com.haisheng.framework.testng.bigScreen.itemMall.common.util;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.LoginPcMall;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.DingPushUtil;
import com.haisheng.framework.util.MD5Util;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class SceneUntil extends BasicUtil {

    private final VisitorProxy visitor;

    public SceneUntil(VisitorProxy proxy) {
        super(proxy);
        this.visitor = proxy;
    }

    public void loginPc(@NotNull AccountEnum enumAccount) {
        String password = new MD5Util().getMD5(enumAccount.getPassword());
        IScene scene = LoginPcMall.builder().password(password).username(enumAccount.getUsername()).type(0).build();
        login(scene);
    }

    public void login(IScene scene) {
        EnumTestProduct oldProduce = visitor.getProduct();
        EnumTestProduct newProduce = visitor.isDaily() ? EnumTestProduct.MALL_DAILY_SSO : EnumTestProduct.MALL_ONLINE_SSO;
        visitor.setProduct(newProduce);
        visitor.login(scene);
        visitor.setProduct(oldProduce);
    }

    public void sendMessage(String subjectName, String time, Map<String, Integer> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("##### ").append(subjectName).append(" ").append(time).append("数据为：").append("\n");
        sb.append("###### ");
        map.forEach((key, value) -> sb.append(key).append("数据为：").append(value).append(" "));
        DingPushUtil.send(sb.toString());
    }
}
