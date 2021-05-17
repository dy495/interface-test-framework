package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.entity.IEntity;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.util.ContainerEnum;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.SendRecordBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.SendRecordScene;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class MyThread extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    private static IEntity<?, ?>[] entities;
    private static final int RULE = 1;
    private static int index = 0;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = PRODUCE.getShopId();
        commonConfig.roleId = ALL_AUTHORITY.getRoleId();
        beforeClassInit(commonConfig);
        user.loginPc(ALL_AUTHORITY);
        entities = new Factory.Builder().container(ContainerEnum.EXCEL.getContainer()).build().createExcel("/excel/数据.xlsx");
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    public synchronized void run(int index) {
        if (index + RULE <= entities.length) {
            for (int i = index; i < index + RULE; i++) {
                //获取表格信息
                IEntity<?, ?> entity = entities[i];
                String voucherCode = entity.getFieldValue("卡券号");
                String voucherName = entity.getFieldValue("卡券名称");
                //接口业务逻辑
                System.err.println(Thread.currentThread().getName() + "--查询：" + voucherName + "--" + voucherCode);
                IScene scene = SendRecordScene.builder().voucherName(voucherName).build();
                SendRecordBean sendRecordBean = util.toJavaObject(scene, SendRecordBean.class, "voucher_code", voucherCode);
                if (sendRecordBean == null) {
                    System.err.println("没查到");
                } else {
                    System.err.println(sendRecordBean.getVoucherCode());
                }
                System.err.println(Thread.currentThread().getName() + "--查询完毕");
            }
        }
    }

    public synchronized JSONObject readRule() {
        JSONObject jsonObject;
        synchronized (MyThread.class) {
            jsonObject = rule();
            index += RULE;
        }
        return jsonObject;
    }

    public JSONObject rule() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("index", index);
        jsonObject.put("rule", RULE);
        return jsonObject;
    }

    @Test(threadPoolSize = 4, invocationCount = 10)
    public void test() {
        int index = readRule().getInteger("index");
        run(index);
    }
}
