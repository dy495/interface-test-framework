package com.haisheng.framework.testng.bigScreen.itemPorsche.common.util;

import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.enumerator.sale.EnumAccount;

public class UserUtil {
    private static final CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    private static final CrmScenarioUtilOnline crmOnline = CrmScenarioUtilOnline.getInstance();

    /**
     * 登录账号
     *
     * @param enumAccount 人员
     */
    public static void login(EnumAccount enumAccount) {
        if (enumAccount == null) {
            throw new RuntimeException("enumAccount is null");
        }
        if (enumAccount.getIsDaily()) {
            crm.login(enumAccount.getAccount(), enumAccount.getPassword());
        } else {
            crmOnline.login(enumAccount.getAccount(), enumAccount.getPassword());
        }
    }

    /**
     * 更新小程序
     *
     * @param appletCode 自己的token
     */
    public static void loginApplet(EnumAppletToken appletToken) {
        if (appletToken == null) {
            throw new RuntimeException("appletToken is null");
        }
        crm.appletLoginToken(appletToken.getToken());
    }

}
