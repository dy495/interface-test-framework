package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletConsultAfterServiceSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletConsultOnlineExpertsSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletConsultPreServiceSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement.ResponseRuleEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.FollowType;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.JcFunctionOnline;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.PublicParamOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.QADbProxy;
import com.haisheng.framework.util.QADbUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class JcApp3_1Online extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount account = EnumAccount.JC_ONLINE_LXQ;
    private final VisitorProxy visitor = new VisitorProxy(product);
    private final SceneUtil util = new SceneUtil(visitor);
    ScenarioUtil jc = new ScenarioUtil();

    PublicParamOnline pp = new PublicParamOnline();
    JcFunctionOnline pf = new JcFunctionOnline();
    CommonConfig commonConfig = new CommonConfig();

    private final QADbProxy qaDbProxy = QADbProxy.getInstance();
    public QADbUtil qaDbUtil = qaDbProxy.getQaUtil();

    public String dataName = "app_sale_receptionIdOnline";


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "?????????";
        jc.changeIpPort(product.getIp());

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);

        //replace ding f
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};
        commonConfig.setShopId(pp.shopIdZ).setReferer(product.getReferer()).setRoleId(pp.roleidJdgw).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        util.loginApp(account);

    }

    //app??????
    public void appLogin(String username, String password, String roleId) {
        String path = "/account-platform/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        commonConfig.setRoleId(roleId);
        httpPost(EnumTestProduct.JC_ONLINE_ZH.getIp(), path, object);
    }

    //pc??????
    public void pcLogin(String phone, String verificationCode, String roleId) {
        String path = "/account-platform/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        object.put("type", 1);
        commonConfig.setRoleId(roleId);
        httpPost(EnumTestProduct.JC_ONLINE_ZH.getIp(), path, object);
    }

    public void startReception() {
        int receptionId = Integer.parseInt(pf.salereception(pp.customerPhone)[0]);
        qaDbUtil.updateDataNum(dataName, receptionId);
    }

    public void finishReception() {
        Integer receptionId = qaDbUtil.selectDataTempOne("pcAppointmentRecordNum", dataName);
        IScene appcustomerEdit = AppCustomerEditScene.builder()
                .id(String.valueOf(receptionId))
                .shopId(pp.shopIdZ)
                .customerName("?????????")
                .estimatedBuyTime(dt.getHistoryDate(0))
                .build();
        jc.invokeApi(appcustomerEdit, true);


        IScene appfinishReception = AppFinishReceptionScene.builder()
                .shopId(Long.valueOf(pp.shopIdZ))
                .id((long) receptionId).build();

        jc.invokeApi(appfinishReception);
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

    @Test()
    public void Astart() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            startReception();
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????");
        }
    }

    @Test()
    public void zfinish() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            finishReception();

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????");
        }
    }


    @Test(description = "??????????????????51????????????")
    public void editCustomer1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = qaDbUtil.selectDataTempOne("pcAppointmentRecordNum", dataName);
            String[] reception = {String.valueOf(id), null};
            //????????????--????????????50???
            IScene appcustomerEdit = AppCustomerEditScene.builder()
                    .id(reception[0])
                    .shopId(pp.shopIdZ)
                    .customerName(pp.String_50 + "???")
                    .estimatedBuyTime(dt.getHistoryDate(0))
                    .build();
            JSONObject data1 = jc.invokeApi(appcustomerEdit, false);

            Preconditions.checkArgument(data1.getLong("code") == 1001, "???????????????????????????");
            Preconditions.checkArgument(data1.getString("message").contains("50"), "????????????????????????");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????");
        }
    }

    @Test(description = "???????????????????????????")
    public void editCustomer2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = qaDbUtil.selectDataTempOne("pcAppointmentRecordNum", dataName);
            String[] reception = {String.valueOf(id), null};
            String[] errphone = {"1590293829", "178273766554", "??????%???#"};
            for (String s : errphone) {
                IScene appcustomerEdit2 = AppCustomerEditScene.builder()
                        .id(reception[0])
                        .shopId(pp.shopIdZ)
                        .customerName("?????????")
                        .customerPhone(s)
                        .estimatedBuyTime(dt.getHistoryDate(0))
                        .build();
                JSONObject data1 = jc.invokeApi(appcustomerEdit2, false);

                Preconditions.checkArgument(data1.getLong("code") == 1001, "??????????????????????????????");
                Preconditions.checkArgument(data1.getString("message").contains("?????????"), "???????????????????????????");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????");
        }
    }

    //??????  ???????????????????????????????????????????????????
    @Test(description = "??????????????????+1", enabled = false)
    public void buyCar() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = qaDbUtil.selectDataTempOne("pcAppointmentRecordNum", dataName);
            String[] reception = {String.valueOf(id), null};
            //??????????????????
            IScene appcustomerEdit2 = AppCustomerEditScene.builder()
                    .id(reception[0])
                    .shopId(pp.shopIdZ)
                    .customerName("?????????")
                    .customerPhone(pp.customerPhone)
                    .estimatedBuyTime(dt.getHistoryDate(0))
                    .carModel(Long.valueOf(pp.carModelId))
                    .build();
            jc.invokeApi(appcustomerEdit2);

            //???????????????
            IScene customerDetail = AppCustomerDetailScene.builder().id(reception[0]).shopId(pp.shopIdZ).build();
            JSONObject orderList = jc.invokeApi(customerDetail);
            JSONArray list = orderList.getJSONArray("order_list");

            //??????
            IScene appbuycar = AppBuyCarScene.builder().id(Long.valueOf(reception[0]))
                    .carModel(Long.valueOf(pp.carModelId))
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .vin("AAAAAAASDS1829837")   //?????????
                    .build();
            jc.invokeApi(appbuycar);
            //???????????????
            IScene customerDetail2 = AppCustomerDetailScene.builder().id(reception[0]).shopId(pp.shopIdZ).build();
            JSONObject orderList2 = jc.invokeApi(customerDetail2);
            JSONArray list2 = orderList2.getJSONArray("order_list");

            Preconditions.checkArgument(list2.size() - list.size() == 1, "???????????????+1");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????+1");
        }
    }

    @Test(description = "??????????????????", enabled = false)
    public void changeReception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = qaDbUtil.selectDataTempOne("pcAppointmentRecordNum", dataName);
            String[] reception = {String.valueOf(id), null};
            String uid = "";

            JSONArray saleList = jc.AppReceptorListScene(Long.parseLong(pp.shopIdZ)).getJSONArray("list");
            for (int i = 0; i < saleList.size(); i++) {
                uid = saleList.getJSONObject(i).getString("uid");
                if (!uid.equals(pp.useridxs)) {
                    break;
                }
            }
            int totalBefore = pf.appSaleReceptionPage();
            //????????????
            IScene appreceptionChange = AppReceptorChangeScene.builder().receptorId(uid)
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .id(Long.valueOf(reception[0]))
                    .build();
            jc.invokeApi(appreceptionChange);
            int total = pf.appSaleReceptionPage();

            pcLogin(pp.dzphone, pp.dzcode, pp.dzroleId);
            //???????????? ????????????
            appreceptionChange = AppReceptorChangeScene.builder().receptorId(pp.useridxs)
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .id(Long.valueOf(reception[0]))
                    .build();
            jc.invokeApi(appreceptionChange);
            appLogin(pp.jdgw, pp.gwpassword, pp.roleidJdgw);
            int totalAfter = pf.appSaleReceptionPage();

            Preconditions.checkArgument(total - totalBefore == -1, "??????????????????-1");
            Preconditions.checkArgument(total - totalAfter == -1, "??????????????????+1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            saveData("????????????+1");
        }
    }

    @Test(description = "??????")
    public void remark() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            int id = qaDbUtil.selectDataTempOne("pcAppointmentRecordNum", dataName);
            String[] reception = {String.valueOf(id), null};

            IScene customerDetail = AppCustomerDetailScene.builder().id(reception[0]).shopId(pp.shopIdZ).build();
            JSONObject orderList = jc.invokeApi(customerDetail);
            orderList.getJSONArray("remarks");

            String remark = pp.String_50;
            jc.AppCustomerRemarkScene(Long.valueOf(reception[0]), Long.valueOf(pp.shopIdZ), remark);

            IScene customerDetail2 = AppCustomerDetailScene.builder().id(reception[0]).shopId(pp.shopIdZ).build();
            JSONObject orderList2 = jc.invokeApi(customerDetail2);
            orderList2.getJSONArray("remarks");

//            Preconditions.checkArgument(list2.size()-list.size()==1,"????????????");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????? ????????????");
        }
    }

    //    @Test(description = "??????????????????")
    public void follow_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int totalBefore = pf.followPageNumber();
            jc.appletLoginToken(pp.appletToken);
            IScene onlineExpert = AppletConsultOnlineExpertsSubmitScene.builder()
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .content("??????????????????")
                    .customerName("??????")
                    .customerPhone("15037286013")
                    .modelId(Long.valueOf(pp.carModelId))
                    .brandId(pp.brandId)
                    .build();
            jc.invokeApi(onlineExpert);

            //???????????? ??????id
            JSONObject followList = jc.AppPageV3Scene(10, null, FollowType.ONLINE_EXPERTS.getName());
            int total = followList.getInteger("total");
            Long id = followList.getJSONArray("list").getJSONObject(0).getLong("id");

            Preconditions.checkArgument(total - totalBefore == 1, "???????????????????????????+1");

            //??????
            jc.AppReplyV3Scene(id, "??????????????????????????????");

            //??????
            jc.AppRemarkV3Scene(id, "????????????");

            JSONObject lastvalue = null;
            int size = 10;
            while (size < 10) {
                JSONObject followPageAfter = jc.AppPageV3Scene(10, lastvalue, FollowType.ONLINE_EXPERTS.getName());
                JSONArray followListAfter = followPageAfter.getJSONArray("list");
                size = followListAfter.size();
                lastvalue = followPageAfter.getJSONObject("last_value");

                for (int i = 0; i < followListAfter.size(); i++) {
                    Long idAfter = followListAfter.getJSONObject(i).getLong("id");
                    if (idAfter.equals(id)) {
                        //??????????????? ???????????????
                        break;
                    }

                }

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????");
        }
    }


    //    @Test(description = "??????????????????")   //TODO: ?????????????????? copy ??????
    public void follow_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject followListBefore = jc.AppPageV3Scene(10, null, FollowType.ONLINE_EXPERTS.getName());
            int totalBefore = followListBefore.getInteger("total");
            IScene onlineExpert = AppletConsultAfterServiceSubmitScene.builder()
                    .shopId(Long.valueOf(pp.shopIdZ))
                    .content("??????????????????")
                    .customerName("??????")
                    .customerPhone("15037286013")
                    .modelId(Long.valueOf(pp.carModelId))
                    .salesId(pp.userid)
                    .build();
            jc.invokeApi(onlineExpert);

            //???????????? ??????id
            JSONObject followList = jc.AppPageV3Scene(10, null, FollowType.ONLINE_EXPERTS.getName());
            int total = followList.getInteger("total");
            Long id = followList.getJSONArray("list").getJSONObject(0).getLong("id");

            Preconditions.checkArgument(total - totalBefore == 1, "???????????????????????????+1");

            //??????
            jc.AppReplyV3Scene(id, "??????????????????????????????");

            //??????
            jc.AppRemarkV3Scene(id, "????????????");

            //???????????? ??????????????????????????????
            JSONObject lastvalue = null;
            int size = 10;
            while (size < 10) {
                JSONObject followPageAfter = jc.AppPageV3Scene(10, lastvalue, FollowType.ONLINE_EXPERTS.getName());
                JSONArray followListAfter = followPageAfter.getJSONArray("list");
                size = followListAfter.size();
                lastvalue = followPageAfter.getJSONObject("last_value");

                for (int i = 0; i < followListAfter.size(); i++) {
                    Long idAfter = followListAfter.getJSONObject(i).getLong("id");
                    if (idAfter.equals(id)) {
                        //??????????????? ???????????????
                        break;
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????");
        }
    }

    //????????????

    @Test(description = "????????????")  //?????????????????????????????????????????????????????????????????? ok
    public void permissions() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
//            Long i=pf.getAccessId2("??????");
//            System.out.println(i);
//            System.out.println(pf.getAccessId2("??????"));
//
//            System.out.println(pf.getAccessId2("??????????????????"));

            //????????????  ?????????????????????
            JSONArray roleList1 = new JSONArray();
            roleList1.add(pf.getAccessId2("??????"));
            roleList1.add(pf.getAccessId2("??????"));
            //??????????????????
            jc.organizationRoleEdit(Long.parseLong(pp.userRoleId), "??????", "????????????????????????", roleList1);
            jc.appletLoginToken(pp.appletToken);
            int staffTotalBefore = jc.AppletAppointmentStaffListScene("MAINTAIN", Long.valueOf(pp.shopIdZ)).getJSONArray("list").size();

            //            appLogin(pp.user,pp.userpassword,pp.userroleId);
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            roleList1.add(pf.getAccessId2("??????????????????"));

            jc.organizationRoleEdit(Long.parseLong(pp.userRoleId), "??????", "????????????????????????", roleList1);

            //????????? ???????????????????????????
            jc.appletLoginToken(pp.appletToken);
            int staffTotal = jc.AppletAppointmentStaffListScene("MAINTAIN", Long.valueOf(pp.shopIdZ)).getJSONArray("list").size();

            roleList1.remove(2);
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            jc.organizationRoleEdit(Long.parseLong(pp.userRoleId), "??????", "????????????????????????", roleList1);

            jc.appletLoginToken(pp.appletToken);
            int staffTotalAfter = jc.AppletAppointmentStaffListScene("MAINTAIN", Long.valueOf(pp.shopIdZ)).getJSONArray("list").size();
            Preconditions.checkArgument(staffTotalAfter - staffTotal == -1, "??????????????????" + staffTotalAfter + "???????????????:" + staffTotal);
            Preconditions.checkArgument(staffTotalBefore - staffTotal == -1, "??????????????????" + staffTotalBefore + "????????????:" + staffTotal);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????");
        }
    }


    @Test(description = "??????????????????")
    public void expertsConfig() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "ONLINE_EXPERTS";
            Integer remind = 1;
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            String workday = "{\n" +
                    "            \"afternoon_date_start\":\"13:00\",\n" +
                    "            \"forenoon_date_end\":\"12:00\",\n" +
                    "            \"forenoon_date_start\":\"04:00\",\n" +
                    "            \"afternoon_date_end\":\"23:59\"\n" +
                    "        }";
            String weekDay = "{\n" +
                    "            \"afternoon_date_start\":\"13:00\",\n" +
                    "            \"forenoon_date_end\":\"12:00\",\n" +
                    "            \"forenoon_date_start\":\"04:00\",\n" +
                    "            \"afternoon_date_end\":\"17:00\"\n" +
                    "        }";
            JSONObject work_day = JSONObject.parseObject(workday);
            JSONObject week_day = JSONObject.parseObject(weekDay);
            IScene responseRuleEditScene = ResponseRuleEditScene.builder().businessType(type).remindTime(remind).overTime(remind)
                    .workDay(work_day).weekDay(week_day).build();
            jc.invokeApi(responseRuleEditScene);


            jc.appletLoginToken(pp.appletToken);
            //??????????????????
            IScene appletCustomer = AppletConsultOnlineExpertsSubmitScene.builder().customerName("?????????").customerPhone(pp.customerPhone)
                    .content("???????????????????????????1234567890")
                    .modelId(Long.parseLong(pp.carModelId)).shopId(Long.parseLong(pp.shopIdZ)).build();
            jc.invokeApi(appletCustomer);

            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            //?????????
            int gwtotal = jc.appmessageList("20", null).getInteger("total");
            appLogin(pp.dzphone, pp.dzcode, pp.dzroleId);
            int dztotal = jc.appmessageList("20", null).getInteger("total");

            sleep(remind * 60);
            int dztotalAfter = jc.appmessageList("20", null).getInteger("total");

            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            int gwtotalAfter = jc.appmessageList("20", null).getInteger("total");

            Preconditions.checkArgument(gwtotalAfter == gwtotal, "????????????????????????????????????,???????????????");
            Preconditions.checkArgument(dztotalAfter - dztotal == 1, "????????????????????????????????????????????????");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????????????????????????????????????????");
        }
    }

    @Test(description = "pc??????????????????")
    public void serverConfig() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "SALES";
            Integer remind = 1;
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            String workday = "{\n" +
                    "            \"afternoon_date_start\":\"13:00\",\n" +
                    "            \"forenoon_date_end\":\"12:00\",\n" +
                    "            \"forenoon_date_start\":\"04:00\",\n" +
                    "            \"afternoon_date_end\":\"23:59\"\n" +
                    "        }";
            String weekDay = "{\n" +
                    "            \"afternoon_date_start\":\"13:00\",\n" +
                    "            \"forenoon_date_end\":\"12:00\",\n" +
                    "            \"forenoon_date_start\":\"04:00\",\n" +
                    "            \"afternoon_date_end\":\"17:00\"\n" +
                    "        }";
            JSONObject work_day = JSONObject.parseObject(workday);
            JSONObject week_day = JSONObject.parseObject(weekDay);
            IScene responseRuleEditScene = ResponseRuleEditScene.builder().businessType(type).remindTime(remind).overTime(remind)
                    .workDay(work_day).weekDay(week_day).build();
            jc.invokeApi(responseRuleEditScene);


            jc.appletLoginToken(pp.appletToken);
            //??????????????????
            IScene appletCustomer = AppletConsultPreServiceSubmitScene.builder().customerName("?????????").customerPhone(pp.customerPhone)
                    .content("????????????????????????????????????????????????1111111")
                    .salesId(pp.userid).modelId(Long.parseLong(pp.carModelId)).shopId(Long.parseLong(pp.shopIdZ)).build();
            jc.invokeApi(appletCustomer);

            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            //?????????
            int gwtotal = jc.appmessageList("20", null).getInteger("total");
            appLogin(pp.dzphone, pp.dzcode, pp.dzroleId);
            int dztotal = jc.appmessageList("20", null).getInteger("total");

            sleep(remind * 60);
            int dztotalAfter = jc.appmessageList("20", null).getInteger("total");

            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            int gwtotalAfter = jc.appmessageList("20", null).getInteger("total");

            Preconditions.checkArgument(gwtotalAfter == gwtotal, "????????????????????????????????????,???????????????");
            Preconditions.checkArgument(dztotalAfter - dztotal == 1, "????????????????????????????????????????????????");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????????????????????????????????????????");
        }
    }

    @DataProvider(name = "onlineExpertrInfo")
    public static Object[] onlineExpertrInfo() {
        return new String[]{
                FollowType.ONLINE_EXPERTS.getType(),
                FollowType.SALES.getType(),
        };
    }

    @Test(dataProvider = "onlineExpertrInfo", description = "???????????????????????????????????????")   //??????
    public void zfollowRemark(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            JSONObject data = jc.AppPageV3Scene(10, null, type);
            JSONArray list = data.getJSONArray("list");
            Long followId = list.getJSONObject(0).getLong("id");
//            jc.AppRemarkV3Scene(followId,"?????????????????????????????????????????????????????????");AppReplyV3Scene
            jc.AppReplyV3Scene(followId, "?????????????????????????????????????????????????????????");

//            JSONObject dataAfter=jc.AppPageV3Scene(10,null,type);
//            JSONArray listAfter=dataAfter.getJSONArray("list");
//            Long followIdAfter=listAfter.getJSONObject(0).getLong("id");
//            String isreply=listAfter.getJSONObject(0).getString("is_reply");
//            Preconditions.checkArgument(isreply.equals("true"),"");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????????");
        }
    }

    @DataProvider(name = "repairreply")
    public static Object[] repairreply() {
        return new String[]{
                FollowType.MAINTAIN_EVALUATE.getType(),
                FollowType.REPAIR_EVALUATE.getType(),
        };
    }

    //    @Test(dataProvider ="repairreply", description = "??????????????????")   //??????
    public void zfollowRemark2(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.dzphone, pp.dzcode, pp.dzroleId);
            JSONObject data = jc.AppPageV3Scene(10, null, type);
            JSONArray list = data.getJSONArray("list");
            Long followId = list.getJSONObject(0).getLong("id");
            jc.AppRemarkV3Scene(followId, "???????????????????????????????????????????????????");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            saveData("app??????,???????????????????????????+1,??????????????????????????????-1");
        }
    }


}
