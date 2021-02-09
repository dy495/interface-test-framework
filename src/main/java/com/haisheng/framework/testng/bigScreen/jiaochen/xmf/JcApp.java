package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.SelectReception;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.appointmentRecodeSelect;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.*;

public class JcApp extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = new ScenarioUtil();

    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    JcFunction pf = new JcFunction();
    JsonPathUtil jp=new JsonPathUtil();
    CommonConfig commonConfig = new CommonConfig();


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";
        commonConfig.referer = EnumTestProduce.JIAOCHEN_DAILY.getReferer();
//        commonConfig.referer=getJcReferdaily();


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getDesc() + commonConfig.checklistQaOwner);

        //replace ding f
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "49195";
        commonConfig.roleId="2945";
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        appLogin(pp.jdgw, pp.jdgwpassword,pp.roleidJdgw);


    }
    //app登录
    public void appLogin(String username, String password,String roleId) {
        String path = "/jiaochen/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        commonConfig.roleId=roleId;
        httpPost(path, object, EnumTestProduce.JIAOCHEN_DAILY.getAddress());
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

    @DataProvider(name = "ACOUNT")
    public static Object[] acount() {
        return new String[][]{
                {"13114785236", "000000", "轿辰（赢识测试）","603"},
//                {"13412010055", "000000", "全部-区域"},
//                {"13412010054", "000000", "全部-品牌"},
                {"13402050050", "000000", "自动化专用账号","2945"},
                {"13402050049", "000000", "中关村店长勿动","2946"},
        };
    }

    @Test(description = "今日任务数==今日数据各列数据之和", dataProvider = "ACOUNT")  //ok
    public void taskEquelDate(String name, String code, String names,String roleId) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
           appLogin(name, code,roleId);
            String type = "all";   //home \all
            //获取今日任务数
            int tasknum[] = pf.appTask();

            Integer appointmentcountZ = 0;  //预约
            Integer appointmentcountM = 0;

            Integer receptioncountZ = 0;  //接待
            Integer receptioncountM = 0;
            //今日数据
            JSONArray todaydate = jc.apptodayDate(type, null, 100).getJSONArray("list");

            for (int i = 0; i < todaydate.size(); i++) {
                JSONObject list_data = todaydate.getJSONObject(i);
                //待处理预约数和
                String pending_appointment = list_data.getString("pending_appointment");
                if (!pending_appointment.contains("-")) {
                    String[] appointment = pending_appointment.split("/");
                    appointmentcountZ += Integer.valueOf(appointment[0]);
                    appointmentcountM += Integer.valueOf(appointment[1]);
                }

                //接待
                String pending_reception = list_data.getString("pending_reception");
                if (!pending_reception.contains("-")) {
                    String[] reception = pending_reception.split("/");
                    receptioncountZ += Integer.parseInt(reception[0]);
                    receptioncountM += Integer.parseInt(reception[1]);
                    System.out.println(receptioncountM + ":" + receptioncountM);
                }
            }
            Preconditions.checkArgument(tasknum[0] == appointmentcountZ, name + "今日任务未处理预约数:" + tasknum[0] + "!=今日数据处理数据和" + appointmentcountZ);
            Preconditions.checkArgument(tasknum[1] == appointmentcountM, name + "今日任务总预约数:" + tasknum[1] + "!=今日数据处理数据和" + appointmentcountM);
            Preconditions.checkArgument(tasknum[2] == receptioncountZ, name + "今日任务未处理接待数:" + tasknum[2] + "!=今日数据处理数据和" + receptioncountZ);
            Preconditions.checkArgument(tasknum[3] == receptioncountM, name + "今日任务总接待数:" + tasknum[3] + "!=今日数据处理数据和" + receptioncountM);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-今日任务数=今日数据各列数据之和");
        }
    }

    @Test(description = "今日任务未完成接待（预约）数（分子）==【任务-接待（预约）】列表条数")  //ok
    public void Jc_appointmentPageAndtodaydate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //app今日任务数
            int tasknum[] = pf.appTask();

            int appointmentTotal = jc.appointmentPage(null, 100).getInteger("total");
            int receptionTotal = jc.appreceptionPage(null, 100).getInteger("total");

            Preconditions.checkArgument(tasknum[0] == appointmentTotal, "今日任务待处理预约数" + tasknum[0] + "!=[任务-预约]列表数" + appointmentTotal);
            Preconditions.checkArgument(tasknum[2] == receptionTotal, "今日任务待处理接待数" + tasknum[2] + "!=[任务-接待]列表数" + receptionTotal);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-今日任务未完成接待数（分子）==【任务-接待】列表条数");
        }
    }

    @Test(description = "顾问：今日任务接待总数（分母）==【pc接待管理】接待时间为今天&&接待人为app登录接待顾问 数据和")
    public void Jc_receptionPageAndpctodaydate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.jdgw, pp.gwpassword,pp.roleidJdgw);
            //app今日任务数
            int tasknum[] = pf.appTask();

            //pc登录  预约记录页该顾问今日数据
            jc.pcLogin(pp.jdgw, pp.gwpassword);
            IScene scene = appointmentRecodeSelect.builder().page("1")
                    .size("100").customer_name(pp.jdgwName)
                    .shop_id(pp.shopIdZ)
                    .create_end(dt.getHistoryDate(0))
                    .create_start(dt.getHistoryDate(0)).build();

            int appointmentTotal1 = jc.invokeApi(scene).getInteger("total");

            IScene scene2 = appointmentRecodeSelect.builder().page("1")
                    .size("10").customer_name(pp.jdgwName)
                    .shop_id(pp.shopIdZ)
                    .appointment_status("20")
                    .create_end(dt.getHistoryDate(0))
                    .create_start(dt.getHistoryDate(0)).build();

            int appointmentTotal2 = jc.invokeApi(scene2).getInteger("total");
            int appointmentTotal = appointmentTotal1 - appointmentTotal2;

            //接待管理页今日数据
            SelectReception sr = new SelectReception();
            sr.shop_id = pp.shopIdZ;
            sr.reception_sale_name = pp.reception_sale_id;
            sr.reception_end = dt.getHistoryDate(0);
            sr.reception_start = dt.getHistoryDate(0);

            int total1 = jc.receptionManageC(sr).getInteger("total");
            sr.reception_status = "2000";
            int total2 = jc.receptionManageC(sr).getInteger("total");
            int total = total1 - total2;
            sr = null;

            Preconditions.checkArgument(tasknum[3] == total, "今日任务接待总数" + tasknum[3] + "!=[pc今日该接待顾问接待总数]" + total);
            Preconditions.checkArgument(tasknum[1] == appointmentTotal, "今日任务预约总数" + tasknum[1] + "!=[pc今日该接待顾问预约总数]" + appointmentTotal);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            appLogin(pp.gwphone, pp.gwpassword,pp.roleId);
            saveData("轿辰-今日任务接待（预约）总数（分母）==pc【】列表条数");
        }
    }

    @Test(description = "店长：今日任务接待总数（分母）==【pc接待管理】接待时间为今天数据和")  //ok
    public void Jc_receptionPageAndpctodaydate2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.dzphone, pp.dzcode,pp.dzroleId);
            //app今日任务数
            int tasknum[] = pf.appTask();

            //pc登录  预约记录页该顾问今日数据
            jc.pcLogin(pp.dzphone, pp.dzcode);
            IScene scene = appointmentRecodeSelect.builder().page("1")
                    .size("10")
                    .shop_id(pp.shopIdZ)
                    .create_end(dt.getHistoryDate(0))
                    .create_start(dt.getHistoryDate(0)).build();

            int appointmentTotal1 = jc.invokeApi(scene).getInteger("total");

            IScene scene2 = appointmentRecodeSelect.builder().page("1")
                    .size("10")
                    .shop_id(pp.shopIdZ)
                    .appointment_status("20")
                    .create_end(dt.getHistoryDate(0))
                    .create_start(dt.getHistoryDate(0)).build();

            int appointmentTotal2 = jc.invokeApi(scene2).getInteger("total");
            int appointmentTotal = appointmentTotal1 - appointmentTotal2;

            //接待管理页今日数据
            SelectReception sr = new SelectReception();
            sr.shop_id = pp.shopIdZ;
            sr.reception_end = dt.getHistoryDate(0);
            sr.reception_start = dt.getHistoryDate(0);

            int total1 = jc.receptionManageC(sr).getInteger("total");
            sr.reception_status = "2000";
            int total2 = jc.receptionManageC(sr).getInteger("total");
            int total = total1 - total2;
            sr = null;
            Preconditions.checkArgument(tasknum[3] == total, "今日任务接待总数" + tasknum[3] + "!=[pc今日该接待顾问接待总数]" + total);
            Preconditions.checkArgument(tasknum[1] == appointmentTotal, "今日任务预约总数" + tasknum[1] + "!=[pc今日该接待顾问预约总数]" + appointmentTotal);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            appLogin(pp.gwphone, pp.gwpassword,pp.roleId);
            saveData("轿辰-今日任务接待（预约）总数（分母）==pc【】列表条数");
        }
    }

    @Test(description = "集团：今日任务接待总数（分母）==【pc接待管理】接待时间为今天数据和")
    public void Jc_receptionPageAndpctodaydate3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId="-1";
            appLogin(pp.gwphone, pp.gwpassword,pp.roleId);
            //app今日任务数
            int tasknum[] = pf.appTask();

            //pc登录  预约记录页该顾问今日数据
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            IScene scene = appointmentRecodeSelect.builder().page("1")
                    .size("10")
                    .create_end(dt.getHistoryDate(0))
                    .create_start(dt.getHistoryDate(0)).build();

            int appointmentTotal1 = jc.invokeApi(scene).getInteger("total");

            IScene scene2 = appointmentRecodeSelect.builder().page("1")
                    .size("10")
                    .appointment_status("20")
                    .create_end(dt.getHistoryDate(0))
                    .create_start(dt.getHistoryDate(0)).build();

            int appointmentTotal2 = jc.invokeApi(scene2).getInteger("total");
            int appointmentTotal = appointmentTotal1 - appointmentTotal2;
            //接待管理页今日数据
            SelectReception sr = new SelectReception();
//            sr.shop_id = pp.shopId;
            sr.reception_end = dt.getHistoryDate(0);
            sr.reception_start = dt.getHistoryDate(0);

            int total1 = jc.receptionManageC(sr).getInteger("total");
            sr.reception_status = "2000";
            int total2 = jc.receptionManageC(sr).getInteger("total");
            int total = total1 - total2;
            sr = null;
            Preconditions.checkArgument(tasknum[3] == total, "今日任务接待总数" + tasknum[3] + "!=[pc今日该接待顾问接待总数]" + total);
            Preconditions.checkArgument(tasknum[1] == appointmentTotal, "今日任务预约总数" + tasknum[1] + "!=[pc今日该接待顾问预约总数]" + appointmentTotal);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            appLogin(pp.gwphone, pp.gwpassword,pp.roleId);
            commonConfig.shopId=pp.shopIdZ;
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
            Long id[] = pf.startReception(pp.carplate7);

            JSONObject dd = jc.appreceptionPage(null, 10);
            int totalA = dd.getInteger("total");
            String shopId = dd.getJSONArray("list").getJSONObject(0).getString("shop_id");

            //完成接待
            jc.finishReception(id[0], id[1]);
            int totalC = jc.appreceptionPage(null, 10).getInteger("total");

            Preconditions.checkArgument(totalA - total == 1, "接待后接待列表未+1,接待前：" + total + "，接待后：" + totalA);
            Preconditions.checkArgument(totalA - totalC == 1, "完成接待后接待列表未-1,接待前：" + totalA + "，接待后：" + totalA);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-app接待,接待任务+1,完成接待，接待任务-1");
        }
    }

    @Test(description = "app接待车牌号7位，汉字+字母",enabled = false)  //受接待车牌次数限制，此 case与上一个case合并
    public void Jc_reception2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long id[] = pf.startReception(pp.carplate7);

            jc.finishReception(id[0], id[1]);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待车牌号7位，汉字+字母");
        }
    }

    @Test(description = "app接待,今日任务分子、分母+1，完成接待分子-1，分母-0")    //一次
    public void Jc_receptionTodayTask() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //接待前，今日任务
            int tasknum[] = pf.appTask();
            //开始接待
            Long id[] = pf.startReception(pp.carplate);
            int tasknumA[] = pf.appTask();
            //完成接待
            jc.finishReception(id[0], id[1]);
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


    @Test(description = "app接待,pc接待管理列表+1")   //二次
    public void AJc_receptionPcPage() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //pc登录
            jc.pcLogin(pp.jdgw, pp.jdgwpassword);
            //接待前，接待任务列表总数
            int total = jc.receptionManage(pp.shopIdZ, "1", "10", "", "").getInteger("total");

            //app登录 开始接待
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            Long id[] = pf.startReception(pp.carplate);

            //pc登录
            jc.pcLogin(pp.jdgw, pp.jdgwpassword);
            int totalA = jc.receptionManage(pp.shopIdZ, "1", "10", "", "").getInteger("total");

            //完成接待
//            jc.appLogin(pp.jdgw, pp.jdgwpassword);
//            jc.finishReception(id[0], id[1]);

            //pc登录
            jc.pcLogin(pp.jdgw, pp.jdgwpassword);
            int totalC = jc.receptionManage(pp.shopIdZ, "1", "10", "", "").getInteger("total");

            Preconditions.checkArgument(totalA - total == 1, "接待后接待列表未+1,接待前：" + total + "，接待后：" + totalA);
            Preconditions.checkArgument(totalA - totalC == 0, "完成接待后接待列表未-0,接待前：" + totalA + "，接待后：" + totalA);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.appLogin(pp.gwphone, pp.gwpassword);   //最后app登录
            saveData("轿辰-app接待,接待任务+1,完成接待，接待任务-1");
        }
    }

    @Test(description = "app取消接待,接待任务列表-1，今日任务数分子分母都-1" )
    public void BJc_canclereception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //开始接待
            Long id[] = new Long[2];
            JSONObject dd = jc.appreceptionPage(null, 10).getJSONArray("list").getJSONObject(0);
            id[0] = dd.getLong("id");
            id[1] = dd.getLong("shop_id");

            int total = jc.appreceptionPage(null, 10).getInteger("total");
            int tasknum[] = pf.appTask();

            //取消接待
            jc.cancleReception(id[0], id[1]);
            int totalA = jc.appreceptionPage(null, 10).getInteger("total");
            int tasknumA[] = pf.appTask();

            Preconditions.checkArgument(total - totalA == 1, "取消接待后接待列表未-1,接待前：" + total + "，接待后：" + totalA);
            Preconditions.checkArgument(tasknum[2] - tasknumA[2] == 1, "取消接待后今日任务-1,接待前：" + tasknum[2] + "，接待后：" + tasknumA[2]);
            Preconditions.checkArgument(tasknum[3] - tasknumA[3] == 1, "取消接待后今日任务未-1,接待前：" + tasknum[3] + "，接待后：" + tasknumA[3]);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-app取消接待,接待任务-1,今日任务-1");
        }
    }

    //**************************今日数据******************************
    @Test(description = "app接待,今日数据分子、分母+1，完成接待分子-1，分母-0")   //三次
    public void Jc_receptionTodayDate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            String type = "all";
            String name = pp.jdgwName; //Todo:账号名称, 或者店铺的名字
            //接待前，今日任务
            int tasknum[] = pf.apptodayDate(type, name);
            //开始接待
            Long id[] = pf.startReception(pp.carplate);
            int tasknumA[] = pf.apptodayDate(type, name);
            //完成接待
            jc.finishReception(id[0], id[1]);
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

    @DataProvider(name = "HEXIAONUM")  //核销，核销记录+1
    public static Object[] hexiaonum() {   //异常核销码集合  (正常：17-19数字)
        return new String[]{
                "1234567890123456",     //16位
                "12345678901234561234",     //20位
                "一二三四五六七八九十一二三四五六七",    //汉字
                "123456789012四五六七",  //含汉字
                "1234567890123ASD", //含小写
                "1234567890123asd", //含字母
                "234567890123asd&**",//含字符
                "160731387200000030",//已使用的核销码
//                "160741180800000005",//已过期
        };
    }

    //核销码异常验证  ok
    @Test(description = "app核销码异常验证", dataProvider = "HEXIAONUM")
    public void Jc_hexiaoAB(String num) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = jc.verification(num, false).getInteger("code");
            Preconditions.checkArgument(code == 1001, "异常核销码，返回不是1001，code:" + code);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app核销码异常验证");
        }
    }

    //车牌号异常验证  ok
    @Test(description = "app接待车牌号验证", dataProvider = "PLATE", dataProviderClass = ScenarioUtil.class)
    public void Jc_ReceiptAb(String plate) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = jc.appReceptionAdmitcode(plate).getInteger("code");
            Preconditions.checkArgument(code == 1001, "异常车牌号依然成功");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app接待车牌号验证");
        }
    }

    /**
     * @description :消息记录查询
     * @date :2020/12/10 21:08
     **/

    @Test()
    public void messageFormOneFilter2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            JSONArray result = jc.enummap().getJSONArray("PUSH_REASON_TYPE");

            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < result.size(); i++) {
                String tt = result.getJSONObject(i).getString("key");
                String vv = result.getJSONObject(i).getString("value");
                map.put(tt, vv);
            }
            Set<String> keySet = map.keySet();
            Iterator<String> t = keySet.iterator();
            while (t.hasNext()) {
                String key = t.next();
                System.out.println("key:" + key);
                JSONArray respon1 = jc.pushMsgListFilterManage("-1", "1", "10", "message_type", key).getJSONArray("list");
                String temp = map.get(key);
                for (int j = 0; j < respon1.size(); j++) {
                    String resultA = respon1.getJSONObject(j).getString("message_type_name");
                    Preconditions.checkArgument(resultA.equals(temp), resultA + ":" + temp);
                    System.out.println("start:" + key + "___---------------------result:" + temp);
                }
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            saveData("消息表单单项查询，结果校验");
        }
    }

    @Test(description = "登录登出验证")
    public void Jc_apploginNor() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin2(pp.jdgw, pp.jdgwpassword, true);
            jc.appLoginout();
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("登录登出校验");
        }
    }

    /**
     * @description :登录异常
     * @date :2020/12/17 11:45
     **/
    @Test(dataProvider = "ERR_PHONE", dataProviderClass = DataAbnormal.class)
    public void Jc_apploginAb(String phone) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = jc.appLogin2(phone, "000000", false).getInteger("code");
            Preconditions.checkArgument(code == 1001, "登录异常手机号");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("登录手机号异常校验");
        }
    }

    @Test(dataProvider = "CODE", dataProviderClass = DataAbnormal.class)
    public void Jc_apploginAb2(String code) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code1 = jc.appLogin2(pp.jdgw, code, false).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "登录异常手机号");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("登录验证码异常校验");
        }
    }

    /**
     * @description :核销----需要小程序有源源不断的卡券
     * @date :2020/12/17 14:58
     **/
    @Test()
    public void write() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String voucher_code[] = pf.voucherName();
            //pc
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            int messagePctotal = jc.pushMsgListFilterManage("-1", "1", "10", null, null).getInteger("total");
            int verificationReordPctotal = jc.verificationReordFilterManage("-1", "","1", "10", null, null).getInteger("total");

            //核销记录总数
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            int total = jc.appWriteOffRecordsPage("ALL", "10", null).getInteger("total");
            //核销
            jc.verification(voucher_code[0], true);
            int totalA = jc.appWriteOffRecordsPage("ALL", "10", null).getInteger("total");
            //小程序消息最新一条信息校验
            jc.appletLoginToken(pp.appletTocken);
            JSONObject message = jc.appletMessageList(null, 20).getJSONArray("list").getJSONObject(0);
            String messageName = message.getString("content");
//            String messageTime=message.getString("content");

            jc.pcLogin(pp.gwphone, pp.gwpassword);
            int messagePctotalA = jc.pushMsgListFilterManage("-1", "1", "10", null, null).getInteger("total");
            int verificationReordPctotalA = jc.verificationReordFilterManage("-1","", "1", "10", null, null).getInteger("total");


            Preconditions.checkArgument(messagePctotalA - messagePctotal == 1, "核销后pc消息总数没-1");
            Preconditions.checkArgument(verificationReordPctotalA - verificationReordPctotal == 1, "核销后pc核销记录记录总数没-1");
            Preconditions.checkArgument(totalA - total == 1, "核销后记录总数没-1");
            Preconditions.checkArgument(messageName.equals("您的卡券【" + voucher_code[1] + "】已被核销，请立即查看"));


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            saveData("app核销记录数据一致校验");
        }
    }

    //2.0 变更接待  调试时需注意账号登录登出顺序
//    @Test(description = "app变更接待,接待任务变更")
    public void Jc_recepchangetion() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //开始接待
            Long id[] = pf.startReception(pp.carplate7);
            //变更接待前
            int total = jc.appreceptionPage(null, 10).getInteger("total");
            int tasknum[] = pf.appTask();

            jc.receptorChange(id[0], id[1],pp.userid2);    //变更接待

            //变更接待后
            int total2 = jc.appreceptionPage(null, 10).getInteger("total");
            int tasknumA[] = pf.appTask();

            jc.receptorChange(id[0], id[1],pp.userid);    //变更接待，变回来
            int total3 = jc.appreceptionPage(null, 10).getInteger("total");

            //完成接待
            jc.finishReception(id[0], id[1]);

            Preconditions.checkArgument(total - total2 == 1, "变更接待后接待列表未-1,接待前：" + total + "，接待后：" + total2);
            Preconditions.checkArgument(total3 - total2 == 1, "变更接待后接待列表未-1,接待前：" + total2 + "，接待后：" + total3);

            Preconditions.checkArgument(tasknumA[2] - tasknum[2] == 1, "变更接待后今日任务-分子+1 ");
            Preconditions.checkArgument(tasknumA[3] - tasknum[3] == 1, "变更接待后今日任务-分母+1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-appapp变更接待,接待任务变更");
        }
    }

    //2.0 变更接待
//    @Test(description = "变更接待列表")
    public void receptorOnlyList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data=jc.receptorList(Long.parseLong(pp.shopIdZ));
            jp.spiltString(data.toJSONString(),"$.list[*].uid&&$.list[*].name&&$.list[*].phone");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("变更接待列表返回值校验");
        }
    }
    /**
     * @description :新增一个接待权限账户，接待人员列表+1
     * @date :2021/1/21 14:56
     **/
//    @Test(description = "新增一个接待权限账户，接待人员列表+1")
    public void receptorListAndCreateAccount() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建账户前，接待列表人数
            Integer total=jc.receptorList(Long.parseLong(pp.shopIdZ)).getInteger("total");
            //创建账户
            JSONArray r_dList = new JSONArray();
            r_dList.add(pp.roleId);

            JSONArray shop_list = new JSONArray();
            shop_list.add(pp.shopIdZ);
            String name=""+System.currentTimeMillis();
            String phone=pf.genPhoneNum();

            jc.pcLogin(pp.gwphone,pp.gwpassword);
             jc.organizationAccountAdd(name, phone, r_dList, shop_list,true);

            JSONArray accountList = jc.pcStaffPage(name, 1, 10).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("id");
            jc.appLogin(pp.jdgw,pp.jdgwpassword);
            Integer totalAfterAdd=jc.receptorList(Long.parseLong(pp.shopIdZ)).getInteger("total");

            jc.pcLogin(pp.gwphone,pp.gwpassword);
            jc.organizationAccountDelete(account);

            jc.appLogin(pp.jdgw,pp.jdgwpassword);
            Integer totalAfterDelate=jc.receptorList(Long.parseLong(pp.shopIdZ)).getInteger("total");
            Preconditions.checkArgument(totalAfterAdd-total==1,"新增接待权限账户，接待人原列表+1");
            Preconditions.checkArgument(totalAfterDelate-totalAfterAdd==-1,"删除接待权限账户，接待人原列表-1");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增/删除接待权限账户，接待人原列表+-1");
        }
    }

}
