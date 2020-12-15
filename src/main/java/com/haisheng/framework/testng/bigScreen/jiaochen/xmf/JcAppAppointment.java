package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.SelectReception;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.appletAppointment;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.appointmentRecodeSelect;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.*;

public class JcAppAppointment extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = new ScenarioUtil();

    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    JcFunction pf = new JcFunction();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";
        commonConfig.referer=getJcRefer();


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName() + commonConfig.checklistQaOwner);

        //replace ding f
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "-1";
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        jc.appLogin(pp.jdgw, pp.jdgwpassword);


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

          //****************************预约系列*************************

    @DataProvider(name = "TYPE")
    public static Object[] type() {
        return new String[]{
                "AGREE",
                "CANCLE",

        };
    }

    @Test(description = "确认预约,app任务列表-1，今日任务数-1", dataProvider = "TYPE")
    public void Jc_agreeCancleAppointment(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appointmentPage(null, 10);
            int total = data.getInteger("total");

            JSONArray list = data.getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("没预约任务,case无法执行");
            }
            Long id = list.getJSONObject(0).getLong("id");  //取一个预约id
            int tasknum[] = pf.appTask();

            //确认预约
            jc.appointmentHandle(id, type);

            int totalA = jc.appointmentPage(null, 10).getInteger("total");
            int tasknumA[] = pf.appTask();

            Preconditions.checkArgument(total - totalA == 1, "确认预约 列表未-1,前：" + total + "，后：" + totalA);
            Preconditions.checkArgument(tasknum[0] - tasknumA[0] == 1, "确认预约后今日任务(分子)未-1,前：" + tasknum[0] + "，后：" + tasknumA[0]);
            Preconditions.checkArgument(tasknumA[1] - tasknum[1] == 0, "确认预约后今日任务（分母）变了,前：" + tasknum[1] + "，后：" + tasknumA[1]);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-app确认、取消预约,预约任务-1,今日任务-1");
        }
    }

    @Test(description = "小程序预约,app任务列表+1，今日任务数+1")
    public void Jc_AppletAppointment() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appointmentPage(null, 10);
            int total = data.getInteger("total");
            int tasknum[] = pf.appTask();

            jc.appletLoginToken(pp.appletTocken);
            //小程序预约
            appletAppointment pm = new appletAppointment();
            pm.car_id=pp.car_idA;
            pm.appointment_name="自动夏";
            pm.shop_id=Long.parseLong(pp.shopIdZ);
            pm.staff_id="uid_f9342ae2";
            pm.time_id=pf.getTimeId(pm.shop_id,pm.car_id,dt.getHistoryDate(0));

            Long appointmentId=jc.appletAppointment(pm).getLong("id");

            jc.appLogin(pp.gwphone, pp.gwpassword);
            int totalA = jc.appointmentPage(null, 10).getInteger("total");
            int tasknumA[] = pf.appTask();

            jc.appletLoginToken(pp.appletTocken);
            jc.appletCancleAppointment(appointmentId,pp.shopIdZ);
            Preconditions.checkArgument(totalA - total == 1, "小程序预约 列表未+1,前：" + total + "，后：" + totalA);
            Preconditions.checkArgument(tasknumA[0] - tasknum[0] == 1, "确认预约后今日任务(分子)未+1,前：" + tasknum[0] + "，后：" + tasknumA[0]);
            Preconditions.checkArgument(tasknumA[1] - tasknum[1] == 1, "确认预约后今日任务（分母）未+1,前：" + tasknum[1] + "，后：" + tasknumA[1]);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-小程序预约,app任务列表+1，今日任务数+1");
        }
    }


}
