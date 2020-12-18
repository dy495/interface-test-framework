package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.MessageList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.VoucherVerification;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.WechatCustomerPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.MessageFormPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.LoginUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.wm.util.BusinessUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 营销管理模块测试用例
 */
public class FinanceManagerOnline extends TestCaseCommon implements TestCaseStd {
    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
    LoginUtil user = new LoginUtil();
    private static final Integer size = 100;
    private static final EnumAccount administrator = EnumAccount.ADMINISTRATOR_ONLINE;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.produce = EnumProduce.JC.name();
        commonConfig.referer = EnumRefer.JIAOCHEN_REFERER_ONLINE.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_ONLINE.getName() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.JIAOCHEN_ONLINE.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        user.login(administrator);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "卡券申请--成本累计=发出数量*成本单价")
    public void voucherApply_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            ApplyPage.ApplyPageBuilder builder = ApplyPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                array.forEach(e -> {
                    JSONObject jsonObject = (JSONObject) e;
                    String voucherName = jsonObject.getString("name");
                    double price = jsonObject.getDouble("price");
                    double totalPrice = jsonObject.getDouble("total_price");
                    int num = jsonObject.getInteger("num");
                    CommonUtil.valueView(price, num, totalPrice);
                    Preconditions.checkArgument(totalPrice == price * num,
                            voucherName + "成本累计:" + totalPrice + " 发出数量*成本单价：" + price * num);
                    CommonUtil.logger(voucherName);
                });
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券申请--成本累计=发出数量*成本单价");
        }
    }

    @Test(description = "卡券申请--发出数量（首发）=【卡券表单】发行库存数量")
    public void voucherApply_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.login(administrator);
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                array.forEach(e -> {
                    JSONObject jsonObject = (JSONObject) e;
                    String voucherName = jsonObject.getString("voucher_name");
                    Integer issueInventory = jsonObject.getInteger("issue_inventory");
                    ApplyPage.ApplyPageBuilder builder1 = ApplyPage.builder().name(voucherName);
                    int total1 = jc.invokeApi(builder1.build()).getInteger("total");
                    int s1 = CommonUtil.getTurningPage(total1, size);
                    for (int j = 1; j < s1; j++) {
                        JSONArray array1 = jc.invokeApi(builder1.page(j).size(size).build()).getJSONArray("list");
                        array1.forEach(x -> {
                            JSONObject jsonObject1 = (JSONObject) x;
                            if (jsonObject1.getString("apply_type_name").equals("首发")
                                    && jsonObject1.getString("name").equals(voucherName)) {
                                Integer num = jsonObject1.getInteger("num") == 0 ? null : jsonObject1.getInteger("num");
                                CommonUtil.valueView(num, issueInventory);
                                Preconditions.checkArgument(Objects.equals(num, issueInventory),
                                        voucherName + "发出数量（首发）：" + num + "【卡券表单】发行库存数量：" + issueInventory);
                                CommonUtil.logger(voucherName);
                            }
                        });
                    }
                });
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券申请--发出数量（首发）=【卡券表单】发行库存数量");
        }
    }

    @Test(description = "卡券申请--成本单价=【卡券表单】成本")
    public void voucherApply_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.login(administrator);
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                array.forEach(e -> {
                    JSONObject jsonObject = (JSONObject) e;
                    String voucherName = jsonObject.getString("voucher_name");
                    double cost = jsonObject.getDouble("cost");
                    ApplyPage.ApplyPageBuilder builder1 = ApplyPage.builder().name(voucherName);
                    int total1 = jc.invokeApi(builder1.build()).getInteger("total");
                    int s1 = CommonUtil.getTurningPage(total1, size);
                    for (int j = 1; j < s1; j++) {
                        JSONArray array1 = jc.invokeApi(builder1.page(j).size(size).build()).getJSONArray("list");
                        array1.forEach(x -> {
                            JSONObject jsonObject1 = (JSONObject) x;
                            if (jsonObject1.getString("name").equals(voucherName)) {
                                double price = jsonObject1.getDouble("price");
                                CommonUtil.valueView(cost, price);
                                Preconditions.checkArgument(Objects.equals(cost, price),
                                        voucherName + "成本单价：" + price + "【卡券表单】成本：" + cost);
                                CommonUtil.logger(voucherName);
                            }
                        });
                    }
                });
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券申请--成本单价=【卡券表单】成本");
        }
    }
}
