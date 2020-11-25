package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PackFunction;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.app.AppCustomerCreateScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.appStartReception;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.appointmentRecodeSelect;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.omg.PortableInterceptor.INACTIVE;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Random;

public class JcApp extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = new ScenarioUtil();
    JsonPathUtil jpu = new JsonPathUtil();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    JcFunction pf = new JcFunction();
    FileUtil file = new FileUtil();
    Random random = new Random();
    public int page = 1;
    public int size = 50;
    public String name = "";
    public String email = "";
    public String phone = "";

    Integer status = 1;
    String type = "PHONE";

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
        commonConfig.checklistQaOwner = "xmf";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 日常X");

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        jc.appLogin("", "");


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

    @Test
    public void erCode() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.apperCOde();
            String jsonpath = "$.er_code_url1";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app个人中心，小程序码返回结果不为空");
        }
    }

    @Test(description = "今日任务数==今日数据各列数据之和")
    public void taskEquelDate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "all";   //home \all
            //获取今日任务数
            JSONObject data = jc.appTask();
            //预约
            Integer surplus_appointment = data.getInteger("surplus_appointment");
            Integer all_appointment = data.getInteger("all_appointment");
            //接待
            Integer surplus_reception = data.getInteger("surplus_reception");
            Integer all_reception = data.getInteger("all_reception");

            Integer appointmentcountZ = 0;  //预约
            Integer appointmentcountM = 0;

            Integer receptioncountZ = 0;  //接待
            Integer receptioncountM = 0;
            //今日数据
            JSONArray todaydate = jc.apptodayDate(type, null, 10).getJSONArray("list");

            for (int i = 0; i < todaydate.size(); i++) {
                JSONObject list_data = todaydate.getJSONObject(i);
                //待处理预约数和
                String pending_appointment = list_data.getString("pending_appointment");
                String[] appointment = pending_appointment.split("/");
                appointmentcountZ += Integer.valueOf(appointment[0]);
                appointmentcountM += Integer.valueOf(appointment[1]);
                //接待
                String pending_reception = list_data.getString("pending_reception");
                String[] reception = pending_reception.split("/");
                receptioncountZ += Integer.parseInt(reception[0]);
                receptioncountZ += Integer.parseInt(reception[1]);
            }
            Preconditions.checkArgument(surplus_appointment==appointmentcountZ,"今日任务未处理预约数:"+surplus_appointment+"!=今日数据处理数据和"+appointmentcountZ);
            Preconditions.checkArgument(all_appointment==appointmentcountM,"今日任务总预约数:"+all_appointment+"!=今日数据处理数据和"+appointmentcountM);
            Preconditions.checkArgument(surplus_reception==receptioncountZ,"今日任务未处理接待数:"+surplus_reception+"!=今日数据处理数据和"+receptioncountZ);
            Preconditions.checkArgument(all_reception==receptioncountM,"今日任务总接待数:"+all_reception+"!=今日数据处理数据和"+receptioncountM);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日任务数=今日数据各列数据之和");
        }
    }

    @Test(description = "今日任务未完成接待数（分子）==【任务-接待】列表条数")
    public void appointmentPageAndtodaydate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取今日任务数
            JSONObject data = jc.appTask();
            //预约
            Integer surplus_appointment = data.getInteger("surplus_appointment");
            //接待
            Integer surplus_reception = data.getInteger("surplus_reception");

            int appointmentTotal=jc.appointmentPage(null,10).getInteger("total");
            int receptionTotal=jc.appreceptionPage(null,10).getInteger("total");

            Preconditions.checkArgument(surplus_appointment==appointmentTotal,"今日任务待处理预约数"+surplus_appointment+"!=[任务-预约]列表数"+appointmentTotal);
            Preconditions.checkArgument(surplus_reception==receptionTotal,"今日任务待处理预约数"+surplus_reception+"!=[任务-预约]列表数"+receptionTotal);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日任务未完成接待数（分子）==【任务-接待】列表条数");
        }
    }

    @Test(description = "今日任务接待总数（分母）==【pc接待管理】接待时间为今天&&接待人为app登录接待顾问 数据和")
    public void receptionPageAndpctodaydate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appTask();
            //预约
            Integer all_appointment = data.getInteger("all_appointment");
            IScene scene = appointmentRecodeSelect.builder().page("1")
                    .size("10").customer_manager(pp.name)
                    .create_date(dt.getHistoryDate(0)).build();

            int appointmentTotal=jc.invokeApi(scene).getInteger("total");
            //接待
            Integer all_reception = data.getInteger("all_reception");
            //pc登录
            int total=jc.receptionManage(pp.shopId,"1","10","reception_sale_id",pp.reception_sale_id,"reception_date",dt.getHistoryDate(0)).getInteger("total");
            Preconditions.checkArgument(all_reception==total,"今日任务接待总数"+all_reception+"!=[pc今日该接待顾问接待总数]"+total);
            Preconditions.checkArgument(all_appointment==appointmentTotal,"今日任务接待总数"+all_appointment+"!=[pc今日该接待顾问接待总数]"+appointmentTotal);



        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日任务未完成接待数（分子）==【任务-接待】列表条数");
        }
    }

    @Test(description = "app接待,接待任务+1")
    public void reception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待前，接待任务列表总数
            int total=jc.appreceptionPage(null,10).getInteger("total");
            Long id=pf.startReception(pp.carplate);

            int totalA=jc.appreceptionPage(null,10).getInteger("total");

            //完成接待
            jc.finishReception(id);
            int totalC=jc.appreceptionPage(null,10).getInteger("total");
            Preconditions.checkArgument(totalA-total==1,"接待后接待列表未+1,接待前："+total+"，接待后："+totalA);
            Preconditions.checkArgument(totalA-totalC==1,"完成接待后接待列表未-1,接待前："+totalA+"，接待后："+totalA);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待,接待任务+1,完成接待，接待任务-1");
        }
    }


}
