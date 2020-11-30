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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "汽车-轿辰 日常X");

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        jc.appLogin(pp.gwphone, pp.gwpassword);


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



    @Test(description = "今日任务数==今日数据各列数据之和")
    public void Jc_taskEquelDate() {
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
            saveData("轿辰-今日任务数=今日数据各列数据之和");
        }
    }

    @Test(description = "今日任务未完成接待（预约）数（分子）==【任务-接待（预约）】列表条数")
    public void Jc_appointmentPageAndtodaydate() {
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
            saveData("轿辰-今日任务未完成接待数（分子）==【任务-接待】列表条数");
        }
    }

    @Test(description = "今日任务接待总数（分母）==【pc接待管理】接待时间为今天&&接待人为app登录接待顾问 数据和")
    public void Jc_receptionPageAndpctodaydate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app今日任务数
            int tasknum[] = pf.appTask();

            //pc登录  预约记录页该顾问今日数据
            jc.pcLogin(pp.gwphone,pp.gwpassword);
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
            jc.appLogin(pp.gwphone,pp.gwpassword);
            saveData("轿辰-今日任务接待（预约）总数（分母）==pc【】列表条数");
        }
    }

    @Test(description = "app接待,接待任务列表+1,完成接待列表数-1")
    public void Jc_reception() {
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
            saveData("轿辰-app接待,接待任务+1,完成接待，接待任务-1");
        }
    }

    @Test(description = "app接待,今日任务分子、分母+1，完成接待分子-1，分母-0")
    public void Jc_receptionTodayTask() {
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
            saveData("轿辰-app接待,接待任务+1,完成接待，接待任务-1");
        }
    }

    @Test(description = "app接待,pc接待管理列表+1")
    public void Jc_receptionPcPage() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //pc登录
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            //接待前，接待任务列表总数
            int total = jc.receptionManage(pp.shopId, "1", "10", "", "").getInteger("total");

            //app登录 开始接待
            jc.appLogin(pp.gwphone,pp.gwpassword);
            Long id = pf.startReception(pp.carplate);

            //pc登录
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            int totalA = jc.receptionManage(pp.shopId, "1", "10", "", "").getInteger("total");

            //完成接待
            jc.appLogin(pp.gwphone,pp.gwpassword);
            jc.finishReception(id);

            //pc登录
            jc.pcLogin(pp.gwphone,pp.gwpassword);
            int totalC = jc.receptionManage(pp.shopId, "1", "10", "", "").getInteger("total");

            Preconditions.checkArgument(totalA - total == 1, "接待后接待列表未+1,接待前：" + total + "，接待后：" + totalA);
            Preconditions.checkArgument(totalA - totalC == 1, "完成接待后接待列表未-1,接待前：" + totalA + "，接待后：" + totalA);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.appLogin(pp.gwphone,pp.gwpassword);   //最后app登录
            saveData("轿辰-app接待,接待任务+1,完成接待，接待任务-1");
        }
    }

    @Test(description = "app取消接待,接待任务列表-1，今日任务数分子分母都-1")
    public void Jc_canclereception() {
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
            saveData("轿辰-app取消接待,接待任务-1,今日任务-1");
        }
    }

    //**************************今日数据******************************
    @Test(description = "app接待,今日数据分子、分母+1，完成接待分子-1，分母-0")
    public void Jc_receptionTodayDate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "all";
            String name = pp.gwname; //Todo:账号名称, 或者店铺的名字
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
            saveData("轿辰-app接待,今日数据待处理接待+1,完成接待，待处理接待-1");
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
    public void Jc_agreeCancleAppointment(String type) {
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
            appletAppointment pm = new appletAppointment();

            jc.appletLoginToken(pp.appletTocken);
            //小程序预约
            jc.appletAppointment(pm);

            jc.appLogin(pp.gwphone,pp.gwpassword);
            int totalA = jc.appointmentPage(null, 10).getInteger("total");
            int tasknumA[] = pf.appTask();

            Preconditions.checkArgument(totalA - total == 1, "小程序预约 列表未+1,前：" + total + "，后：" + totalA);
            Preconditions.checkArgument(tasknumA[0] - tasknumA[0] == 1, "确认预约后今日任务(分子)未+1,前：" + tasknum[0] + "，后：" + tasknumA[0]);
            Preconditions.checkArgument(tasknumA[1] - tasknum[1] == 1, "确认预约后今日任务（分母）未+1,前：" + tasknum[1] + "，后：" + tasknumA[1]);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-小程序预约,app任务列表+1，今日任务数+1");
        }
    }
    @DataProvider(name = "HEXIAONUM")
    public static Object[] hexiaonum() {   //异常核销码集合  (正常：17-19数字)
        return new String[]{
                "1234567890123456",     //16位
                "12345678901234561234",     //20位
                "一二三四五六七八九十一二三四五六七",    //汉字
                "123456789012四五六七",  //含汉字
                "1234567890123ASD", //含小写
                "1234567890123asd", //含字母
                "234567890123asd&**",//含字符
        };
    }
    //核销码异常验证
    @Test(description = "app核销码异常验证",dataProvider = "HEXIAONUM")
    public void Jc_ApphexiaoAB(String num) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
           int code= jc.verification(num,false).getInteger("code");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app核销码异常验证");
        }
    }

    //车牌号异常验证
    @Test(description = "app接待车牌号验证",dataProvider = "PLATE",dataProviderClass = ScenarioUtil.class)
    public void Jc_AppReceiptAb(String plate) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code=jc.appReceptionAdmitcode(plate).getInteger("code");
            Preconditions.checkArgument(code==1000,"异常车牌号依然成功");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待车牌号验证");
        }
    }



}
