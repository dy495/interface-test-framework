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
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.appletAppointment;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.appointmentRecodeSelect;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.apache.commons.lang.ArrayUtils;
import org.omg.PortableInterceptor.INACTIVE;
import org.testng.annotations.*;

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
            int tasknum[] = pf.appTask();

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
            Preconditions.checkArgument(tasknum[0] == appointmentcountZ, "今日任务未处理预约数:" + tasknum[0] + "!=今日数据处理数据和" + appointmentcountZ);
            Preconditions.checkArgument(tasknum[1] == appointmentcountM, "今日任务总预约数:" + tasknum[1] + "!=今日数据处理数据和" + appointmentcountM);
            Preconditions.checkArgument(tasknum[2] == receptioncountZ, "今日任务未处理接待数:" + tasknum[2] + "!=今日数据处理数据和" + receptioncountZ);
            Preconditions.checkArgument(tasknum[3] == receptioncountM, "今日任务总接待数:" + tasknum[3] + "!=今日数据处理数据和" + receptioncountM);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日任务数=今日数据各列数据之和");
        }
    }

    @Test(description = "今日任务未完成接待（预约）数（分子）==【任务-接待（预约）】列表条数")
    public void appointmentPageAndtodaydate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app今日任务数
            int tasknum[] = pf.appTask();

            int appointmentTotal = jc.appointmentPage(null, 10).getInteger("total");
            int receptionTotal = jc.appreceptionPage(null, 10).getInteger("total");

            Preconditions.checkArgument(tasknum[0] == appointmentTotal, "今日任务待处理预约数" + tasknum[0] + "!=[任务-预约]列表数" + appointmentTotal);
            Preconditions.checkArgument(tasknum[2] == receptionTotal, "今日任务待处理接待数" + tasknum[2] + "!=[任务-接待]列表数" + receptionTotal);

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
            //app今日任务数
            int tasknum[] = pf.appTask();

            //pc登录  预约记录页该顾问今日数据    TODO：获取pc登录的tocken
            IScene scene = appointmentRecodeSelect.builder().page("1")
                    .size("10").customer_manager(pp.name)
                    .create_date(dt.getHistoryDate(0)).build();

            int appointmentTotal = jc.invokeApi(scene).getInteger("total");

            //接待管理页今日数据
            int total = jc.receptionManage(pp.shopId, "1", "10", "reception_sale_id", pp.reception_sale_id, "reception_date", dt.getHistoryDate(0)).getInteger("total");

            Preconditions.checkArgument(tasknum[3] == total, "今日任务接待总数" + tasknum[3] + "!=[pc今日该接待顾问接待总数]" + total);
            Preconditions.checkArgument(tasknum[1] == appointmentTotal, "今日任务预约总数" + tasknum[1] + "!=[pc今日该接待顾问预约总数]" + appointmentTotal);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("今日任务接待（预约）总数（分母）==pc【】列表条数");
        }
    }

    @Test(description = "app接待,接待任务列表+1,完成接待列表数-1")
    public void reception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待前，接待任务列表总数
            int total = jc.appreceptionPage(null, 10).getInteger("total");

            //开始接待
            Long id = pf.startReception(pp.carplate);

            int totalA = jc.appreceptionPage(null, 10).getInteger("total");

            //完成接待
            jc.finishReception(id);
            int totalC = jc.appreceptionPage(null, 10).getInteger("total");

            Preconditions.checkArgument(totalA - total == 1, "接待后接待列表未+1,接待前：" + total + "，接待后：" + totalA);
            Preconditions.checkArgument(totalA - totalC == 1, "完成接待后接待列表未-1,接待前：" + totalA + "，接待后：" + totalA);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待,接待任务+1,完成接待，接待任务-1");
        }
    }

    @Test(description = "app接待,今日任务分子、分母+1，完成接待分子-1，分母-0")
    public void receptionTodayTask() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待前，今日任务
            int tasknum[] = pf.appTask();
            //开始接待
            Long id = pf.startReception(pp.carplate);
            int tasknumA[] = pf.appTask();
            //完成接待
            jc.finishReception(id);
            int tasknumB[] = pf.appTask();

            Preconditions.checkArgument(tasknumA[2] - tasknum[2] == 1, "接待后分子+1 ");
            Preconditions.checkArgument(tasknumA[3] - tasknum[3] == 1, "接待后分母+1");

            Preconditions.checkArgument(tasknumA[2] - tasknumB[2] == 1, "完成接待后分子-1 ");
            Preconditions.checkArgument(tasknumA[3] - tasknumB[3] == 0, "完成接待后分母-0");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待,接待任务+1,完成接待，接待任务-1");
        }
    }

    @Test(description = "app接待,pc接待管理列表+1")
    public void receptionPcPage() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //pc登录
            //接待前，接待任务列表总数
            int total = jc.receptionManage(pp.shopId, "1", "10", "", "").getInteger("total");

            //app登录 开始接待
            Long id = pf.startReception(pp.carplate);

            //pc登录
            int totalA = jc.receptionManage(pp.shopId, "1", "10", "", "").getInteger("total");

            //完成接待
            jc.finishReception(id);

            //pc登录
            int totalC = jc.receptionManage(pp.shopId, "1", "10", "", "").getInteger("total");

            Preconditions.checkArgument(totalA - total == 1, "接待后接待列表未+1,接待前：" + total + "，接待后：" + totalA);
            Preconditions.checkArgument(totalA - totalC == 1, "完成接待后接待列表未-1,接待前：" + totalA + "，接待后：" + totalA);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            //APP登录 TODO：
            saveData("app接待,接待任务+1,完成接待，接待任务-1");
        }
    }

    @Test(description = "app取消接待,接待任务列表-1，今日任务数分子分母都-1")
    public void canclereception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //开始接待
            Long id = pf.startReception(pp.carplate);

            int total = jc.appreceptionPage(null, 10).getInteger("total");
            int tasknum[] = pf.appTask();

            //取消接待
            jc.cancleReception(id);
            int totalA = jc.appreceptionPage(null, 10).getInteger("total");
            int tasknumA[] = pf.appTask();

            Preconditions.checkArgument(total - totalA == 1, "取消接待后接待列表未-1,接待前：" + total + "，接待后：" + totalA);
            Preconditions.checkArgument(tasknumA[2] - tasknum[2] == 1, "取消接待后今日任务-1,接待前：" + tasknum[2] + "，接待后：" + tasknumA[2]);
            Preconditions.checkArgument(tasknumA[3] - tasknum[3] == 1, "取消接待后今日任务未-1,接待前：" + tasknum[3] + "，接待后：" + tasknumA[3]);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app取消接待,接待任务-1,今日任务-1");
        }
    }

    //**************************今日任务******************************
    @Test(description = "app接待,今日任务分子、分母+1，完成接待分子-1，分母-0")
    public void receptionTodayDate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "";
            String name = ""; //Todo:账号名称, 或者店铺的名字
            //接待前，今日任务
            int tasknum[] = pf.apptodayDate(type, name);
            //开始接待
            Long id = pf.startReception(pp.carplate);
            int tasknumA[] = pf.apptodayDate(type, name);
            //完成接待
            jc.finishReception(id);
            int tasknumB[] = pf.apptodayDate(type, name);

            Preconditions.checkArgument(tasknumA[2] - tasknum[2] == 1, "接待后分子+1 ");
            Preconditions.checkArgument(tasknumA[3] - tasknum[3] == 1, "接待后分母+1");

            Preconditions.checkArgument(tasknumA[2] - tasknumB[2] == 1, "完成接待后分子-1 ");
            Preconditions.checkArgument(tasknumA[3] - tasknumB[3] == 0, "完成接待后分母-0");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待,接待任务+1,完成接待，接待任务-1");
        }
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
    public void agreeCancleAppointment(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appointmentPage(null, 10);
            int total = data.getInteger("total");

            JSONArray list = data.getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("没有接待任务,case无法执行");
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
            saveData("app确认、取消预约,预约任务-1,今日任务-1");
        }
    }

    @Test(description = "小程序预约,app任务列表+1，今日任务数+1")
    public void AppletAppointment() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appointmentPage(null, 10);
            int total = data.getInteger("total");
            int tasknum[] = pf.appTask();
            appletAppointment pm = new appletAppointment();
            //TODO:小程序 tocken
            //小程序预约
            jc.appletAppointment(pm);

            int totalA = jc.appointmentPage(null, 10).getInteger("total");
            int tasknumA[] = pf.appTask();

            Preconditions.checkArgument(totalA - total == 1, "小程序预约 列表未+1,前：" + total + "，后：" + totalA);
            Preconditions.checkArgument(tasknumA[0] - tasknumA[0] == 1, "确认预约后今日任务(分子)未+1,前：" + tasknum[0] + "，后：" + tasknumA[0]);
            Preconditions.checkArgument(tasknumA[1] - tasknum[1] == 1, "确认预约后今日任务（分母）未+1,前：" + tasknum[1] + "，后：" + tasknumA[1]);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序预约,app任务列表+1，今日任务数+1");
        }
    }


}
