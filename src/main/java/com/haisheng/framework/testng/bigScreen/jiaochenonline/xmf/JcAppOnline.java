package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.SelectReception;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.appointmentRecodeSelect;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.*;

public class JcAppOnline extends TestCaseCommon implements TestCaseStd {

    ScenarioUtilOnline jc = new ScenarioUtilOnline();

    DateTimeUtil dt = new DateTimeUtil();
    PublicParmOnline pp = new PublicParmOnline();
    JcFunctionOnline pf = new JcFunctionOnline();
    public int page = 1;
    public int size = 50;


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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";
        commonConfig.referer = EnumTestProduce.JIAOCHEN_ONLINE.getReferer();
        commonConfig.product = EnumTestProduce.JIAOCHEN_ONLINE.getName();

//        commonConfig.referer=getJcReferOnline();


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_ONLINE.getName() + commonConfig.checklistQaOwner);

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

    @DataProvider(name = "ACOUNT")
    public static Object[] acount() {
        return new String[][]{
                {"15711200001", "000000", "全部-集团"},
                {"15037286014", "000000", "全部-区域"},
                {"15037286014", "000000", "店长"},
        };
    }

    @Test(description = "今日任务数==今日数据各列数据之和", dataProvider = "ACOUNT")  //ok
    public void Jc_taskEquelDate(String name, String code, String names) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin(name, code);
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
            jc.appLogin(pp.jdgw, pp.gwpassword);
            //app今日任务数
            int tasknum[] = pf.appTask();

            //pc登录  预约记录页该顾问今日数据
            jc.pcLogin(pp.jdgw, pp.gwpassword);
            IScene scene = appointmentRecodeSelect.builder().page("1")
                    .size("100").customer_name(pp.jdgwName)
                    .shop_id(pp.shopIdZ)
                    .create_end(dt.getHistoryDate(0))
                    .create_start(dt.getHistoryDate(0)).build();

            int appointmentTotal = jc.invokeApi(scene).getInteger("total");

            //接待管理页今日数据
            SelectReception sr = new SelectReception();
            sr.shop_id = pp.shopIdZ;
            sr.reception_sale_name = pp.reception_sale_id;
            sr.reception_end = dt.getHistoryDate(0);
            sr.reception_start = dt.getHistoryDate(0);

            int total = jc.receptionManageC(sr).getInteger("total");
            sr = null;

            Preconditions.checkArgument(tasknum[3] == total, "今日任务接待总数" + tasknum[3] + "!=[pc今日该接待顾问接待总数]" + total);
            Preconditions.checkArgument(tasknum[1] == appointmentTotal, "今日任务预约总数" + tasknum[1] + "!=[pc今日该接待顾问预约总数]" + appointmentTotal);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.appLogin(pp.gwphone, pp.gwpassword);
            saveData("轿辰-今日任务接待（预约）总数（分母）==pc【】列表条数");
        }
    }

    @Test(description = "店长：今日任务接待总数（分母）==【pc接待管理】接待时间为今天数据和")  //ok
    public void Jc_receptionPageAndpctodaydate2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin(pp.dzphone, pp.dzcode);
            //app今日任务数
            int tasknum[] = pf.appTask();

            //pc登录  预约记录页该顾问今日数据
            jc.pcLogin(pp.dzphone, pp.dzcode);
            IScene scene = appointmentRecodeSelect.builder().page("1")
                    .size("10")
                    .shop_id(pp.shopIdZ)
                    .create_end(dt.getHistoryDate(0))
                    .create_start(dt.getHistoryDate(0)).build();

            int appointmentTotal = jc.invokeApi(scene).getInteger("total");
            //接待管理页今日数据
            SelectReception sr = new SelectReception();
            sr.shop_id = pp.shopIdZ;
            sr.reception_end = dt.getHistoryDate(0);
            sr.reception_start = dt.getHistoryDate(0);

            int total = jc.receptionManageC(sr).getInteger("total");
            sr = null;
            Preconditions.checkArgument(tasknum[3] == total, "今日任务接待总数" + tasknum[3] + "!=[pc今日该接待顾问接待总数]" + total);
            Preconditions.checkArgument(tasknum[1] == appointmentTotal, "今日任务预约总数" + tasknum[1] + "!=[pc今日该接待顾问预约总数]" + appointmentTotal);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.appLogin(pp.gwphone, pp.gwpassword);
            saveData("轿辰-今日任务接待（预约）总数（分母）==pc【】列表条数");
        }
    }

    @Test(description = "集团：今日任务接待总数（分母）==【pc接待管理】接待时间为今天数据和")
    public void Jc_receptionPageAndpctodaydate3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin(pp.gwphone, pp.gwpassword);
            //app今日任务数
            int tasknum[] = pf.appTask();

            //pc登录  预约记录页该顾问今日数据
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            IScene scene = appointmentRecodeSelect.builder().page("1")
                    .size("10")
                    .shop_id(pp.shopId)
                    .create_end(dt.getHistoryDate(0))
                    .create_start(dt.getHistoryDate(0)).build();

            int appointmentTotal = jc.invokeApi(scene).getInteger("total");
            //接待管理页今日数据
            SelectReception sr = new SelectReception();
            sr.shop_id = pp.shopId;
            sr.reception_end = dt.getHistoryDate(0);
            sr.reception_start = dt.getHistoryDate(0);

            int total = jc.receptionManageC(sr).getInteger("total");
            sr = null;
            Preconditions.checkArgument(tasknum[3] == total, "今日任务接待总数" + tasknum[3] + "!=[pc今日该接待顾问接待总数]" + total);
            Preconditions.checkArgument(tasknum[1] == appointmentTotal, "今日任务预约总数" + tasknum[1] + "!=[pc今日该接待顾问预约总数]" + appointmentTotal);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.appLogin(pp.gwphone, pp.gwpassword);
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

            JSONObject dd = jc.appreceptionPage(null, 10);
            int totalA = dd.getInteger("total");
            String shopId = dd.getJSONArray("list").getJSONObject(0).getString("shop_id");

            //完成接待
            jc.finishReception(id, shopId);
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
            jc.finishReception(id, pp.shopIdZ);
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
            jc.pcLogin(pp.jdgw, pp.jdgwpassword);
            //接待前，接待任务列表总数
            int total = jc.receptionManage(pp.shopIdZ, "1", "10", "", "").getInteger("total");

            //app登录 开始接待
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            Long id = pf.startReception(pp.carplate);

            //pc登录
            jc.pcLogin(pp.jdgw, pp.jdgwpassword);
            int totalA = jc.receptionManage(pp.shopIdZ, "1", "10", "", "").getInteger("total");

            //完成接待
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            jc.finishReception(id, pp.shopIdZ);

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

    @Test(description = "app取消接待,接待任务列表-1，今日任务数分子分母都-1")
    public void Jc_canclereception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //开始接待
            Long id = pf.startReception(pp.carplate);

            int total = jc.appreceptionPage(null, 10).getInteger("total");
            int tasknum[] = pf.appTask();

            //取消接待
            jc.cancleReception(id, pp.shopIdZ);
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
    @Test(description = "app接待,今日数据分子、分母+1，完成接待分子-1，分母-0")
    public void Jc_receptionTodayDate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "all";
            String name = pp.jdgwName; //Todo:账号名称, 或者店铺的名字
            //接待前，今日任务
            int tasknum[] = pf.apptodayDate(type, name);
            //开始接待
            Long id = pf.startReception(pp.carplate);
            int tasknumA[] = pf.apptodayDate(type, name);
            //完成接待
            jc.finishReception(id, pp.shopIdZ);
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

    //核销码异常验证  ok
    @Test(description = "app核销码异常验证", dataProvider = "HEXIAONUM")
    public void Jc_ApphexiaoAB(String num) {
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
    public void Jc_AppReceiptAb(String plate) {
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
            saveData("消息表单单项查询，结果校验");
        }
    }


}
