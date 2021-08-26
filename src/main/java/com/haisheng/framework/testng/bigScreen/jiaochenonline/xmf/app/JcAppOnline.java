package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.staff.StaffPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.LogoutAppScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppCancelReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task.AppReceptionReceptorListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.loginuser.LoginApp;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffDeleteScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.DataAbnormal;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.JcFunctionOnline;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.PublicParamOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class JcAppOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCT = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount ADMIN = EnumAccount.JC_ONLINE_YS;
    private static final EnumAccount ACCOUNT = EnumAccount.JC_ONLINE_LXQ;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCT);
    private final SceneUtil util = new SceneUtil(visitor);
    ScenarioUtil jc = new ScenarioUtil();
    PublicParamOnline pp = new PublicParamOnline();
    JcFunctionOnline pf = new JcFunctionOnline();
    CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        jc.changeIpPort(PRODUCT.getIp());
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.XMF.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(ACCOUNT.getReceptionShopId()).setReferer(PRODUCT.getReferer()).setRoleId(ACCOUNT.getRoleId()).setProduct(PRODUCT.getAbbreviation());
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
        util.loginApp(ACCOUNT);
    }

    //app登录
    public void appLogin(String username, String password, String roleId) {
        String path = "/account-platform/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        commonConfig.setRoleId(roleId);
        httpPost(EnumTestProduct.JC_ONLINE_ZH.getIp(), path, object);
    }

    //pc登录
    public void pcLogin(String phone, String verificationCode, String roleId) {
        String path = "/account-platform/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        object.put("type", 1);
        commonConfig.setRoleId(roleId);
        httpPost(EnumTestProduct.JC_ONLINE_ZH.getIp(), path, object);
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

    @DataProvider(name = "ACCOUNT")
    public static Object[] account() {
        return new String[][]{
                {"15711200001", "000000", "轿辰（赢识测试）", "395"},
//                {"13412010055", "000000", "全部-区域"},
//                {"13412010054", "000000", "全部-品牌"},
                {"15037286011", "000000", "接待顾问x", "424"},
                {"15037286014", "000000", "店长-集团", "439"},
        };
    }

    @Test(description = "今日任务数==今日数据各列数据之和", dataProvider = "ACCOUNT")  //ok
    public void taskEqualDate(String name, String code, String roleId) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(name, code, roleId);
            String type = "all";   //home \all
            //获取今日任务数
            int[] taskNum = pf.appTask();

            int appointmentCountZ = 0;  //预约
            int appointmentCountM = 0;

            int receptionCountZ = 0;  //接待
            int receptionCountM = 0;
            //今日数据
            JSONArray todayDate = jc.apptodayDate(type, null, 100).getJSONArray("list");

            for (int i = 0; i < todayDate.size(); i++) {
                JSONObject list_data = todayDate.getJSONObject(i);
                //待处理预约数和
                String pending_appointment = list_data.getString("pending_appointment");
                if (!pending_appointment.contains("-")) {
                    String[] appointment = pending_appointment.split("/");
                    appointmentCountZ += Integer.parseInt(appointment[0]);
                    appointmentCountM += Integer.parseInt(appointment[1]);
                }

                //接待
                String pending_reception = list_data.getString("pending_reception");
                if (!pending_reception.contains("-")) {
                    String[] reception = pending_reception.split("/");
                    receptionCountZ += Integer.parseInt(reception[0]);
                    receptionCountM += Integer.parseInt(reception[1]);
                    System.out.println(receptionCountM + ":" + receptionCountM);
                }
            }
            Preconditions.checkArgument(taskNum[0] == appointmentCountZ, name + "今日任务未处理预约数:" + taskNum[0] + "!=今日数据处理数据和" + appointmentCountZ);
            Preconditions.checkArgument(taskNum[1] == appointmentCountM, name + "今日任务总预约数:" + taskNum[1] + "!=今日数据处理数据和" + appointmentCountM);
            Preconditions.checkArgument(taskNum[2] == receptionCountZ, name + "今日任务未处理接待数:" + taskNum[2] + "!=今日数据处理数据和" + receptionCountZ);
            Preconditions.checkArgument(taskNum[3] == receptionCountM, name + "今日任务总接待数:" + taskNum[3] + "!=今日数据处理数据和" + receptionCountM);

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("今日任务数=今日数据各列数据之和");
        }
    }

//    @Test(description = "今日任务未完成接待（预约）数（分子）==【任务-接待（预约）】列表条数")  //ok
//    public void Jc_appointmentPageAndtodaydate() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
//            //app今日任务数
//            int tasknum[] = pf.appTask();
//
//            int appointmentTotal = jc.appointmentPage(null, 100).getInteger("total");
//            int receptionTotal = jc.appreceptionPage(null, 100).getInteger("total");
//
//            Preconditions.checkArgument(tasknum[0] == appointmentTotal, "今日任务待处理预约数" + tasknum[0] + "!=[任务-预约]列表数" + appointmentTotal);
//            Preconditions.checkArgument(tasknum[2] == receptionTotal, "今日任务待处理接待数" + tasknum[2] + "!=[任务-接待]列表数" + receptionTotal);
//
//        } catch (AssertionError | Exception e) {
//             collectMessage(e);
//        } finally {
//            saveData("今日任务未完成接待数（分子）==【任务-接待】列表条数");
//        }
//    }
//
//    @Test(description = "顾问：今日任务接待总数（分母）==【pc接待管理】接待时间为今天&&接待人为app登录接待顾问 数据和",enabled = false)  //数据待下期规划
//    public void Jc_receptionPageAndpctodaydate() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            appLogin(pp.jdgw, pp.gwpassword, pp.roleidJdgw);
//            //app今日任务数
//            int tasknum[] = pf.appTask();
//
//            //pc登录  预约记录页该顾问今日数据
//            pcLogin(pp.jdgw, pp.gwpassword, pp.roleidJdgw);
//            IScene scene = appointmentRecodeSelect.builder().page("1")
//                    .size("100").service_sale_id(pp.userid)
//                    .shop_id(pp.shopIdZ)
//                    .create_end(dt.getHistoryDate(0))
//                    .create_start(dt.getHistoryDate(0)).build();
//
//            int appointmentTotal1 = jc.invokeApi(scene).getInteger("total");
//
//            IScene scene2 = appointmentRecodeSelect.builder().page("1")
//                    .size("10").service_sale_id(pp.userid)
//                    .shop_id(pp.shopIdZ)
//                    .appointment_status("20")
//                    .create_end(dt.getHistoryDate(0))
//                    .create_start(dt.getHistoryDate(0)).build();
//
//            int appointmentTotal2 = jc.invokeApi(scene2).getInteger("total");
//            int appointmentTotal = appointmentTotal1 - appointmentTotal2;
//
//            //接待管理页今日数据
//            SelectReception sr = new SelectReception();
//            sr.shop_id = pp.shopIdZ;
//            sr.reception_sale_name = pp.reception_sale_id;
//            sr.reception_end = dt.getHistoryDate(0);
//            sr.reception_start = dt.getHistoryDate(0);
//
//            int total1 = jc.receptionManageC(sr).getInteger("total");
//            sr.reception_status = "2000";
//            int total2 = jc.receptionManageC(sr).getInteger("total");
//            int total = total1 - total2;
//            sr = null;
//
//            Preconditions.checkArgument(tasknum[3] == total, "今日任务接待总数" + tasknum[3] + "!=[pc今日该接待顾问接待总数]" + total);
//            Preconditions.checkArgument(tasknum[1] == appointmentTotal, "今日任务预约总数" + tasknum[1] + "!=[pc今日该接待顾问预约总数]" + appointmentTotal);
//
//
//        } catch (AssertionError | Exception e) {
//             collectMessage(e);
//        } finally {
//            appLogin(pp.gwphone, pp.gwpassword, pp.roleId);
//            saveData("今日任务接待（预约）总数（分母）==pc【】列表条数");
//        }
//    }
//
//    @Test(description = "店长：今日任务接待总数（分母）==【pc接待管理】接待时间为今天数据和",enabled = false)  //数据待下期规划
//    public void Jc_receptionPageAndpctodaydate2() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            appLogin(pp.dzphone, pp.dzcode, pp.dzroleId);
//            //app今日任务数
//            int tasknum[] = pf.appTask();
//
//            //pc登录  预约记录页该顾问今日数据
//            pcLogin(pp.dzphone, pp.dzcode, pp.dzroleId);
//            IScene scene = appointmentRecodeSelect.builder().page("1")
//                    .size("10")
//                    .shop_id(pp.shopIdZ)
//                    .create_end(dt.getHistoryDate(0))
//                    .create_start(dt.getHistoryDate(0)).build();
//
//            int appointmentTotal1 = jc.invokeApi(scene).getInteger("total");
//
//            IScene scene2 = appointmentRecodeSelect.builder().page("1")
//                    .size("10")
//                    .shop_id(pp.shopIdZ)
//                    .appointment_status("20")
//                    .create_end(dt.getHistoryDate(0))
//                    .create_start(dt.getHistoryDate(0)).build();
//
//            int appointmentTotal2 = jc.invokeApi(scene2).getInteger("total");
//            int appointmentTotal = appointmentTotal1 - appointmentTotal2;
//
//            //接待管理页今日数据
//            SelectReception sr = new SelectReception();
//            sr.shop_id = pp.shopIdZ;
//            sr.reception_end = dt.getHistoryDate(0);
//            sr.reception_start = dt.getHistoryDate(0);
//
//            int total1 = jc.receptionManageC(sr).getInteger("total");
//            sr.reception_status = "2000";
//            int total2 = jc.receptionManageC(sr).getInteger("total");
//            int total = total1 - total2;
//            sr = null;
//            Preconditions.checkArgument(tasknum[3] == total, "今日任务接待总数" + tasknum[3] + "!=[pc今日该接待顾问接待总数]" + total);
//            Preconditions.checkArgument(tasknum[1] == appointmentTotal, "今日任务预约总数" + tasknum[1] + "!=[pc今日该接待顾问预约总数]" + appointmentTotal);
//
//
//        } catch (AssertionError | Exception e) {
//             collectMessage(e);
//        } finally {
//            appLogin(pp.gwphone, pp.gwpassword, pp.roleId);
//            saveData("今日任务接待（预约）总数（分母）==pc【】列表条数");
//        }
//    }
//
//    @Test(description = "集团：今日任务接待总数（分母）==【pc接待管理】接待时间为今天数据和",enabled = false)  //数据待下期规划
//    public void Jc_receptionPageAndpctodaydate3() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            commonConfig.shopId = "-1";
//            appLogin(pp.gwphone, pp.gwpassword, pp.roleId);
//            //app今日任务数
//            int tasknum[] = pf.appTask();
//
//            //pc登录  预约记录页该顾问今日数据
//            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
//            IScene scene = appointmentRecodeSelect.builder().page("1")
//                    .size("10")
//                    .create_end(dt.getHistoryDate(0))
//                    .create_start(dt.getHistoryDate(0)).build();
//
//            int appointmentTotal1 = jc.invokeApi(scene).getInteger("total");
//
//            IScene scene2 = appointmentRecodeSelect.builder().page("1")
//                    .size("10")
//                    .appointment_status("20")
//                    .create_end(dt.getHistoryDate(0))
//                    .create_start(dt.getHistoryDate(0)).build();
//
//            int appointmentTotal2 = jc.invokeApi(scene2).getInteger("total");
//            int appointmentTotal = appointmentTotal1 - appointmentTotal2;
//            //接待管理页今日数据
//            SelectReception sr = new SelectReception();
////            sr.shop_id = pp.shopId;
//            sr.reception_end = dt.getHistoryDate(0);
//            sr.reception_start = dt.getHistoryDate(0);
//
//            int total1 = jc.receptionManageC(sr).getInteger("total");
//            sr.reception_status = "2000";
//            int total2 = jc.receptionManageC(sr).getInteger("total");
//            int total = total1 - total2;
//            sr = null;
//            Preconditions.checkArgument(tasknum[3] == total, "今日任务接待总数" + tasknum[3] + "!=[pc今日该接待顾问接待总数]" + total);
//            Preconditions.checkArgument(tasknum[1] == appointmentTotal, "今日任务预约总数" + tasknum[1] + "!=[pc今日该接待顾问预约总数]" + appointmentTotal);
//
//
//        } catch (AssertionError | Exception e) {
//             collectMessage(e);
//        } finally {
//            appLogin(pp.gwphone, pp.gwpassword, pp.roleId);
//            commonConfig.shopId = pp.shopIdZ;
//            saveData("今日任务接待（预约）总数（分母）==pc【】列表条数");
//        }
//    }


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
                "160731387200000030",//已使用的核销码
//                "160741180800000005",//已过期
        };
    }

    //核销码异常验证  ok
    @Test(description = "app核销码异常验证", dataProvider = "HEXIAONUM")
    public void Jc_hexiaoAB(String num) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            int code = jc.verification(num, false).getInteger("code");
            Preconditions.checkArgument(code == 1001, "异常核销码，返回不是1001，code:" + code);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
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
            collectMessage(e);
        } finally {
            saveData("app接待车牌号验证");
        }
    }


    @Test(description = "登录登出验证")
    public void Jc_appLoginNor() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            LoginApp.builder().phone(pp.jdgw).verificationCode(pp.jdgwpassword).build().visitor(visitor).execute();
            LogoutAppScene.builder().build().visitor(visitor).getResponse();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
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
            collectMessage(e);
        } finally {
            saveData("登录手机号异常校验");
        }
    }

    @Test(dataProvider = "CODE", dataProviderClass = DataAbnormal.class)
    public void Jc_apploginAb2(String code) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            int code1 = jc.appLogin2(pp.jdgw, code, false).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "登录异常手机号");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("登录验证码异常校验");
        }
    }

    /**
     * @description :核销----需要小程序有源源不断的卡券;  核销，核销记录+1
     * @date :2020/12/17 14:58
     **/
    @Test()
    public void write() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] voucher_code = pf.voucherName();
            //pc
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            int messagePctotal = jc.pushMsgListFilterManage("-1", "1", "10", null, null).getInteger("total");
            int verificationReordPctotal = jc.verificationReordFilterManage("-1", "", "1", "10", null, null).getInteger("total");

            //核销记录总数
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            int total = jc.appWriteOffRecordsPage("ALL", "10", null).getInteger("total");
            //核销
            jc.verification(voucher_code[0], true);
            int totalA = jc.appWriteOffRecordsPage("ALL", "10", null).getInteger("total");
            //小程序消息最新一条信息校验
            jc.appletLoginToken(pp.appletToken);
            JSONObject message = jc.appletMessageList(null, 20).getJSONArray("list").getJSONObject(0);
            String messageName = message.getString("content");
//            String messageTime=message.getString("content");

            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            int messagePctotalA = jc.pushMsgListFilterManage("-1", "1", "10", null, null).getInteger("total");
            int verificationReordPctotalA = jc.verificationReordFilterManage("-1", "", "1", "10", null, null).getInteger("total");


            Preconditions.checkArgument(messagePctotalA - messagePctotal == 1, "核销后pc消息总数没-1");
            Preconditions.checkArgument(verificationReordPctotalA - verificationReordPctotal == 1, "核销后pc核销记录记录总数没-1");
            Preconditions.checkArgument(totalA - total == 1, "核销后记录总数没-1");
            Preconditions.checkArgument(messageName.equals("您的卡券【" + voucher_code[1] + "】已被核销，请立即查看"));


        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            saveData("app核销记录数据一致校验");
        }
    }

    //2.0 变更接待  调试时需注意账号登录登出顺序
    @Test(description = "app变更接待,接待任务变更")
    public void receptionChange() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            //开始接待
            Long[] id = pf.startReception(pp.carPlate);
            //变更接待前
            int total = jc.appreceptionPage(null, 10).getInteger("total");
            int[] tasknum = pf.appTask();

            jc.receptorChange(id[0], id[1], pp.userid2);    //变更接待

            //变更接待后
            int total2 = jc.appreceptionPage(null, 10).getInteger("total");
            int[] tasknumA = pf.appTask();
            appLogin(pp.dzphone, pp.dzcode, pp.dzroleId);
            jc.receptorChange(id[0], id[1], pp.userid);    //变更接待，变回来
            appLogin(pp.jdgw, pp.gwpassword, pp.roleidJdgw);
            int total3 = jc.appreceptionPage(null, 10).getInteger("total");

            //完成接待
//            jc.finishReception(id[0], id[1]);
            Preconditions.checkArgument(total - total2 == 1, "变更接待后接待列表未-1,接待前：" + total + "，接待后：" + total2);
            Preconditions.checkArgument(total3 - total2 == 1, "变更接待后接待列表未-1,接待前：" + total2 + "，接待后：" + total3);
            Preconditions.checkArgument(tasknumA[2] - tasknum[2] == -1, "变更接待后今日任务-分子+1 ");
            Preconditions.checkArgument(tasknumA[3] - tasknum[3] == -1, "变更接待后今日任务-分母+1");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("appapp变更接待,接待任务变更");
        }
    }

    @Test(description = "app取消接待,接待任务列表-1，今日任务数分子分母都-1")
    public void cancelReception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginApp(ACCOUNT);
            //开始接待
            Long[] id = new Long[2];
            IScene scene = AppReceptionPageScene.builder().lastValue(null).size(10).build().visitor(visitor);
            JSONObject rsp = scene.execute().getJSONArray("list").getJSONObject(0);
            id[0] = rsp.getLong("id");
            id[1] = rsp.getLong("shop_id");
            int total = scene.execute().getInteger("total");
            int[] taskNum = pf.appTask();
            //取消接待
            AppCancelReceptionScene.builder().id(id[0]).shopId(id[1]).build().visitor(visitor).execute();
            int totalA = scene.execute().getInteger("total");
            int[] taskNumA = pf.appTask();
            Preconditions.checkArgument(total - totalA == 1, "取消接待后接待列表未-1,接待前：" + total + "，接待后：" + totalA);
            Preconditions.checkArgument(taskNum[2] - taskNumA[2] == 1, "取消接待后今日任务-1,接待前：" + taskNum[2] + "，接待后：" + taskNumA[2]);
            Preconditions.checkArgument(taskNum[3] - taskNumA[3] == 1, "取消接待后今日任务未-1,接待前：" + taskNum[3] + "，接待后：" + taskNumA[3]);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("app取消接待,接待任务-1,今日任务-1");
        }
    }

    //2.0 变更接待
    @Test(description = "变更接待列表")
    public void receptorOnlyList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = AppReceptionReceptorListScene.builder().shopId(Long.parseLong(ACCOUNT.getReceptionShopId())).build().visitor(visitor).execute();
            JsonPathUtil.spiltString(data.toJSONString(), "$.list[*].uid&&$.list[*].name&&$.list[*].phone");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("变更接待列表返回值校验");
        }
    }

    /**
     * @description :新增一个接待权限账户，接待人员列表+1 ok
     * @date :2021/1/21 14:56
     **/
    @Test(description = "新增一个接待权限账户，接待人员列表+1")
    public void receptorListAndCreateAccount() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.loginApp(ACCOUNT);
            IScene appReceptionReceptorListScene = AppReceptionReceptorListScene.builder().shopId(Long.parseLong(ACCOUNT.getReceptionShopId())).build().visitor(visitor);
            //新建账户前，接待列表人数
            int total = appReceptionReceptorListScene.execute().getJSONArray("list").size();
            //创建账户
            //shopList
            JSONObject shopDate = new JSONObject();
            shopDate.put("shop_id", pp.shopIdZ);
            shopDate.put("shop_name", pp.shopName);
            JSONArray shop_list = new JSONArray();
            shop_list.add(shopDate);
            //shopList
            JSONObject roleList = new JSONObject();
            roleList.put("role_id", pp.roleidJdgw);
            roleList.put("role_name", pp.nameJdgw);
            roleList.put("shop_list", shop_list);
            JSONArray r_dList = new JSONArray();
            r_dList.add(roleList);
            String name = "" + System.currentTimeMillis();
            String phone = pf.genPhoneNum();
            util.loginPc(ADMIN);
            StaffAddScene.builder().roleList(r_dList).shopList(shop_list).name(name).phone(phone).build().visitor(visitor).execute();
            IScene staffPageScene = StaffPageScene.builder().name(name).build();
            String id = util.toFirstJavaObject(staffPageScene, StaffPageBean.class).getId();
            util.loginApp(ACCOUNT);
            int totalAfterAdd = appReceptionReceptorListScene.execute().getJSONArray("list").size();
            util.loginPc(ACCOUNT);
            StaffDeleteScene.builder().id(id).build().visitor(visitor).execute();
            util.loginApp(ACCOUNT);
            int totalAfterDelete = appReceptionReceptorListScene.execute().getJSONArray("list").size();
            Preconditions.checkArgument(totalAfterAdd - total == 1, "新增接待权限账户，接待人原列表+1");
            Preconditions.checkArgument(totalAfterDelete - totalAfterAdd == -1, "删除接待权限账户，接待人原列表-1");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新增/删除接待权限账户，接待人原列表+-1");
        }
    }
}
