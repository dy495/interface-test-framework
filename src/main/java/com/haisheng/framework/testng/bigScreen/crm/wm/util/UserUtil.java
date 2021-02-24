package com.haisheng.framework.testng.bigScreen.crm.wm.util;

import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;

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
            throw new DataException("enumAccount is null");
        }
        if (enumAccount.getEnvironment().equals("daily")) {
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
            throw new DataException("appletToken is null");
        }
        crm.appletLoginToken(appletToken.getToken());
    }

}
