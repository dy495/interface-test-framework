package com.haisheng.framework.testng.bigScreen.jiaochen.mc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.Response;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.customermanager.AppCustomerManagerPreCustomerAddPlateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppCustomerEditV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppCustomerRemarkV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppReceptorChangeScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.reid.AppReidReidDistributeScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.retention.AppRetentionReidCustomerAddScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.saleschedule.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.bean.PreReceptionBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool.JcDataCenter;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool.FastContent;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppBuyCarScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanagev4.PreSaleCustomerInfoScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class SystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_DAILY_JD;
    private static final EnumAccount ACCOUNT = EnumAccount.JC_DAILY_MC;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");

        //??????checklist???????????????
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "??????";
        //??????jenkins-job???????????????
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //??????shopId
        commonConfig.setProduct(PRODUCE.getAbbreviation()).setReferer(PRODUCE.getReferer()).setShopId(ACCOUNT.getReceptionShopId()).setRoleId(ACCOUNT.getRoleId());
        beforeClassInit(commonConfig);
        util.loginApp(ACCOUNT);
        AppSaleScheduleUpdateSaleStatusScene.builder().saleId(util.getBusySaleId()).sourceSaleStatus(0).targetSaleStatus(2).build().visitor(visitor).execute();
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
        logger.logCaseStart(caseResult.getCaseName());
    }

    /**
     * @param shopId: ??????????????????Id
     * @return : list, ????????????????????????????????????????????????
     * @description : ???????????????????????????????????????????????????????????????
     **/
    public List<String> checkSalesStatus(String shopId) {
        commonConfig.setShopId("-1");
        List<String> list1 = AppPreSalesReceptionPageScene.builder().size(40).build().visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> e.getString("reception_sale_name")).distinct().collect(Collectors.toList());
        commonConfig.setShopId(shopId);
        List<String> list2 = AppSaleScheduleDayListScene.builder().type("PRE").build().visitor(visitor).execute().getJSONArray("sales_info_list").stream().map(e -> (JSONObject) e).filter(e -> Objects.equals(e.getString("sale_status"), "?????????")).map(e -> e.getString("sale_name")).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        for (String s : list2) {
            if (!list1.contains(s)) {
                list.add(s);
            }
        }
        return list;
//        Preconditions.checkArgument(list.size()==0,list+"??????????????????????????????????????????????????????????????????????????????");
    }

    public JSONObject preCreate(String name, String phone, boolean isDecision) {
        JSONObject customer = new JSONObject();
        Integer customerId = AppRetentionReidCustomerAddScene.builder().name(name).phone(phone).build().visitor(visitor).execute().getInteger("customer_id");
        customer.put("customer_id", customerId);
        customer.put("is_decision", isDecision);
        return customer;
    }

    /**
     * @param name  :  ??????
     * @param phone : ??????
     * @return : ????????????????????? message
     * @description : ?????????????????????????????????
     **/
    public Response preAssign(String name, String phone) {
        JSONArray customerList = new JSONArray();
        JSONObject customer = preCreate(name, phone, true);
        customerList.add(customer);
        return AppReidReidDistributeScene.builder().reidInfoList(customerList).enterType("PRE_SALE").build().visitor(visitor).getResponse();
    }

    /**
     * @param name:  ??????
     * @param phone: ??????
     * @return : AppReceptionBean  ,???????????????javaBean
     * @description : ?????????????????????????????????
     **/
    public PreReceptionBean preAssign(String name, String phone, boolean checkSale) {
        // ??????????????????????????????
        JSONObject first = AppSaleScheduleDayListScene.builder().type("PRE").build().visitor(visitor).execute().getJSONArray("sales_info_list").stream().map(e -> (JSONObject) e).
                filter(e -> Objects.equals(e.getString("sale_status"), "?????????")).min((x, y) -> x.getInteger("order") - y.getInteger("order")).get();
        // ????????????
        String message = preAssign(name, phone).getMessage();
        Preconditions.checkArgument(Objects.equals("success", message), "????????????????????????,message:" + message);
        // ??????????????????????????????????????????
        if (checkSale) {
            PreReceptionBean reception = AppPreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).toJavaObject(PreReceptionBean.class);
            // ???????????????????????????
            String receptionSaleId = reception.getReceptorId();
            String receptionSaleName = reception.getReceptionSaleName();
            Preconditions.checkArgument(Objects.equals(receptionSaleId, first.getString("sale_id")),
                    "???????????????????????????????????????????????????????????????" + first.getString("sale_name") + ";?????????????????????" + receptionSaleName);
            return reception;
        } else {
            return null;
        }
    }

    /**
     * @param reception: AppReceptionBean : ??????javaBean
     * @return : ??????????????????????????? message
     * @description : ?????????????????????????????????????????????????????????
     **/
    public String finishReception(PreReceptionBean reception) {
        if (reception.getEstimatedBuyTime() == null) {
            AppCustomerEditV4Scene.builder().id(reception.getId()).shopId(reception.getShopId()).customerId(reception.getCustomerId()).customerName(dt.getHistoryDate(0) + "????????????_" + CommonUtil.getRandom(3)).sexId(1).
                    customerPhone("18" + CommonUtil.getRandom(9)).intentionCarModelId(util.getBuyCarId()).estimateBuyCarDate("2030-08-08").build().visitor(visitor).getResponse();
        }
        return finishReception(reception.getId(), reception.getShopId());
    }

    public String finishReception(long id, long shopId) {
        return AppFinishReceptionScene.builder().id(id).shopId(shopId).build().visitor(visitor).getResponse().getMessage();
    }

//    @Test(description = "?????????????????????????????????????????????")
//    public void test01SaleStatusInit(){
//        //AppSaleScheduleUpdateSaleStatusScene.builder().saleId(util.getVacationSaleId()).sourceSaleStatus(0).targetSaleStatus(3).vacationStartTime("2021-08-18").vacationEndTime("2035-08-18").build().visitor(visitor).execute();
//        AppSaleScheduleUpdateSaleStatusScene.builder().saleId(util.getBusySaleId()).sourceSaleStatus(0).targetSaleStatus(2).build().visitor(visitor).execute();
//    }

    @Test(dataProvider = "notFreeSale")
    public void test02ChangeSale1(String description, Integer statusId) {
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").stream().findAny().orElse(null);
            PreReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("N2????????????" + dt.getHistoryDate(0), null, true);
            } else {
                getReception = reception.toJavaObject(PreReceptionBean.class);
            }
            long id = getReception.getId();
            JSONObject neededSale = util.getNeededSale(statusId);
            if (neededSale != null) {
                String saleId = neededSale.getString("sale_id");
                String message = AppReceptorChangeScene.builder().id(id).shopId(Long.parseLong(ACCOUNT.getReceptionShopId())).receptorId(saleId).build().visitor(visitor).getResponse().getMessage();
                Preconditions.checkArgument(Objects.equals("?????????????????????,?????????????????????!", message), description + "???????????????????????????" + message);
            } else {
                logger.warn("??????????????????????????????");
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("???????????????????????????");
        }
    }

    @DataProvider(name = "notFreeSale")
    public Object[] getNotFree() {
        return new Object[][]{
                {"??????????????????????????????", 1},
                {"??????????????????????????????", 2},
                {"??????????????????????????????", 3}
        };
    }

    @Test(description = "????????????")
    public void test02ChangeSale2() {
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").stream().findAny().orElse(null);
            PreReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("Y1????????????" + dt.getHistoryDate(0), null, true);
            } else {
                getReception = reception.toJavaObject(PreReceptionBean.class);
            }
            String saleId = util.getNeededSale(0).getString("sale_id");
            String receptorId = getReception.getReceptorId();
            long id = getReception.getId();
            AppReceptorChangeScene.builder().id(id).shopId(Long.parseLong(ACCOUNT.getReceptionShopId())).receptorId(saleId).build().visitor(visitor).getResponse();
            String lastId = util.getLastSale().getString("sale_id");
            Preconditions.checkArgument(Objects.equals(receptorId, lastId), "???????????????????????????????????????????????????????????????");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("????????????????????????");
        }
    }

    /**
     * @description :????????????
     **/
    @Test(description = "????????????")
    public void groupConfig() {
        try {
            // ?????????????????????
            int size1;
            JSONArray groups = AppSaleScheduleGroupListScene.builder().type("PRE").build().visitor(visitor).execute().getJSONArray("group_infos");
            int s = groups.size();
            if (s < 2) {
                for (int i = 0; i < (2 - s); i++) {
                    AppSaleScheduleAddGroupScene.builder().type("PRE").build().visitor(visitor).getResponse();
                }
                size1 = 2;
                groups = AppSaleScheduleGroupListScene.builder().type("PRE").build().visitor(visitor).execute().getJSONArray("group_infos");
            } else {
                size1 = groups.size();
            }

            //??????2???
            JSONObject second = groups.stream().map(e -> (JSONObject) e).sorted((x, y) -> x.getInteger("group_id") - y.getInteger("group_id")).collect(Collectors.toList()).get(1);
            if (second != null) {
                // ??????2???
                String delMessage = AppSaleScheduleDelGroupScene.builder().groupId(second.getLong("group_id")).type("PRE").build().visitor(visitor).getResponse().getMessage();
                //??????2???????????????
                int size2 = AppSaleScheduleGroupListScene.builder().type("PRE").build().visitor(visitor).execute().getJSONArray("group_infos").size();
                Preconditions.checkArgument(size2 == size1 - 1, "???????????????:" + delMessage);
                //??????????????????3???
                String addMessage = AppSaleScheduleAddGroupScene.builder().type("PRE").build().visitor(visitor).getResponse().getMessage();
                //??????3???????????????
                JSONArray groups2 = AppSaleScheduleGroupListScene.builder().type("PRE").build().visitor(visitor).execute().getJSONArray("group_infos");
                int size3 = groups2.size();
                // ??????3???
                //JSONObject three = groups2.stream().map(e -> (JSONObject) e).filter(e -> Objects.equals("3???", e.getString("group_name"))).findFirst().orElse(null);
                JSONObject three = groups2.stream().map(e -> (JSONObject) e).min((x, y) -> y.getInteger("group_id") - x.getInteger("group_id")).get();
                Preconditions.checkArgument(size3 == size2 + 1, "???????????????:" + addMessage);
                //?????????3?????????id??????
                List<String> list = AppSaleScheduleFreeSaleScene.builder().type("PRE").build().visitor(visitor).execute().
                        getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> e.getString("sale_id")).limit(3).collect(Collectors.toList());
                JSONArray saleList = new JSONArray();
                saleList.addAll(list);
                // ?????????3????????????3?????????
                String addSaleMessage = AppSaleScheduleJoinGroupScene.builder().groupId(three.getLong("group_id")).type("PRE").salesInfoList(saleList).build().visitor(visitor).getResponse().getMessage();
                int groupSize = AppSaleScheduleGroupListScene.builder().type("PRE").build().visitor(visitor).execute().getJSONArray("group_infos").
                        stream().map(e -> (JSONObject) e).filter(e -> Objects.equals(three.getString("group_name"), e.getString("group_name"))).findFirst().orElse(null).
                        getJSONArray("sales_info_list").size();
                Preconditions.checkArgument(groupSize == 3, "3?????????????????????:" + addSaleMessage);

            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("????????????-??????????????????????????????");
        }
    }

    // @Test(description = "???????????????????????????????????????????????????????????????????????????")
//    public void assignReceptionLine() {
//        try {
//            AppReceptionBean reception = preCreate("????????????"+dt.getHistoryDate(0), "13"+CommonUtil.getRandom(9), true);
//            String receptionSaleId = reception.getReceptorId();
//            finishReception(reception);
//            JSONObject lastSale = util.getLastSale();
//            Preconditions.checkArgument(Objects.equals(lastSale.getString("sale_id"), receptionSaleId),
//                    "???????????????????????????????????????????????????????????????" + reception.getReceptionSaleName());
//        } catch (AssertionError | Exception e) {
//            collectMessage(e);
//        } finally {
//            saveData("??????????????????????????????????????????????????????????????????????????????");
//        }
//    }

    @Test(description = "??????????????????????????????????????????")
    public void zFinishReception() {
        try {
            PreReceptionBean reception = null;
            JSONObject res = AppPreSalesReceptionPageScene.builder().build().visitor(visitor).execute();
            Integer total = res.getInteger("total");
            if (total == 0) {
                reception = preAssign("????????????" + dt.getHistoryDate(0), "17" + CommonUtil.getRandom(9), true);
            } else {
                // ????????????????????????????????????????????????
                reception = res.getJSONArray("list").getJSONObject(total - 1).toJavaObject(PreReceptionBean.class);
            }
            String receptionSaleId = reception.getReceptorId();
            finishReception(reception);
            JSONObject lastSale = util.getLastSale();
            Preconditions.checkArgument(Objects.equals(lastSale.getString("sale_id"), receptionSaleId),
                    "???????????????????????????????????????????????????????????????" + reception.getReceptionSaleName());
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("?????????????????????????????????????????????");
        }
    }

    @Test(dataProvider = "preSuccess")
    public void a01AssignReceptionY(String description, String name, String phone) {
        try {
            // ????????????
            String message = preAssign(name, phone).getMessage();
            Preconditions.checkArgument(Objects.equals("success", message), description + "??????????????????????????????message???" + message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("????????????????????????");
        }
    }

    @DataProvider(name = "preSuccess")
    private Object[] preSuccess() {
        return new Object[][]{
                {"????????????50???", FastContent.NAME50, ""},
                {"????????????1???", ".", ""}
        };
    }

    //    @Test(dataProvider = "preFalse", dataProviderClass = DataCenter.class)
    public void test01AssignReceptionN(String description, String name, String phone) {
        try {
            // ????????????
            Response creat = preAssign(name, phone);
            Integer code = creat.getCode();
            String message = creat.getMessage();
            Preconditions.checkArgument(Objects.equals(1001, code), description + "??????????????????????????????message???" + message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("????????????????????????");
        }
    }

    @Test(description = "?????????????????????", dataProvider = "editErrorInfo", dataProviderClass = JcDataCenter.class)
    public void test04EditUserInfo(String description, String point, String content, String expect) {
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").stream().findAny().orElse(null);
            PreReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("????????????for????????????" + dt.getHistoryDate(0), null, true);
            } else {
                getReception = reception.toJavaObject(PreReceptionBean.class);
            }
            sleep(3);
            String message = AppCustomerEditV4Scene.builder().id(getReception.getId()).customerId(getReception.getCustomerId()).shopId(getReception.getShopId()).
                    customerName(FastContent.NAME50).customerPhone("18" + CommonUtil.getRandom(9)).sexId(1).
                    intentionCarModelId(util.getBuyCarId()).estimateBuyCarDate(dt.getHistoryDate(0)).build()
                    .modify(point, content).visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(Objects.equals(expect, message), "????????????" + description + "??????????????????" + expect + "????????????" + message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("?????????????????????");
        }
    }


    @Test(description = "?????????????????????????????????", dataProvider = "customerLevel", dataProviderClass = JcDataCenter.class)
    public void test04CustomerLevel(String time, String expectLevel) {
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").stream().findAny().orElse(null);
            PreReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("????????????for??????????????????" + dt.getHistoryDate(0), null, true);
            } else {
                getReception = reception.toJavaObject(PreReceptionBean.class);
            }
            sleep(3);
            String message = AppCustomerEditV4Scene.builder().id(getReception.getId()).customerId(getReception.getCustomerId()).shopId(getReception.getShopId()).
                    customerName("??????"+time+"??????").customerPhone("13" + CommonUtil.getRandom(9)).sexId(1).
                    intentionCarModelId(util.getBuyCarId()).estimateBuyCarDate(time).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(Objects.equals("success", message), "??????????????????"+ message);
            String customerLevel = PreSaleCustomerInfoScene.builder().customerId(getReception.getCustomerId()).shopId(getReception.getShopId()).build().visitor(visitor).execute().getString("customer_level");
                        Preconditions.checkArgument(Objects.equals(customerLevel,expectLevel),"???????????????????????????"+time+":????????????????????????"+expectLevel+",???????????????"+customerLevel);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("?????????????????????????????????");
        }
    }


    @Test(description = "???????????????????????????", dataProvider = "errorBuyCar", dataProviderClass = JcDataCenter.class)
    public void test05receptionBuyCar(String description, String point, String content, String expect) {
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").stream().findAny().orElse(null);
            PreReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("????????????for??????" + dt.getHistoryDate(0), null, true);
            } else {
                getReception = reception.toJavaObject(PreReceptionBean.class);
            }
            String message = AppBuyCarScene.builder().id(getReception.getId()).shopId(getReception.getShopId()).carModel(util.getBuyCarId()).vin(null).build().modify(point, content).visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(Objects.equals(expect, message), "???????????????" + description + "??????????????????" + expect + "????????????" + message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("??????????????????????????????");
        }
    }

    @Test(description = "?????????????????????,??????", dataProvider = "addPlate", dataProviderClass = JcDataCenter.class)
    public void test03AddPlateNumber(String description, String content, String expect) {
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").stream().findAny().orElse(null);
            PreReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("????????????for?????????" + dt.getHistoryDate(0), null, true);
            } else {
                getReception = reception.toJavaObject(PreReceptionBean.class);
            }
            String message = AppCustomerManagerPreCustomerAddPlateScene.builder().customerId(getReception.getCustomerId()).shopId(getReception.getShopId()).plateNumber(content).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(Objects.equals(expect, message), "????????????????????????" + description + "??????????????????" + expect + "????????????" + message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("?????????????????????,??????");
        }
    }

    @Test(description = "???????????????", dataProvider = "remark", dataProviderClass = JcDataCenter.class)
    public void receptionRemark(String description, String content, String expect) {
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").stream().findAny().orElse(null);
            PreReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("????????????for?????????" + dt.getHistoryDate(0), null, true);
            } else {
                getReception = reception.toJavaObject(PreReceptionBean.class);
            }
            Integer code = AppCustomerRemarkV4Scene.builder().id(getReception.getId()).shopId(getReception.getShopId()).remark(content).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(Objects.equals(expect, String.valueOf(code)), "???????????????" + description + "???????????????code???" + expect + "?????????code???" + code);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("???????????????");
        }
    }


    //@Test
    public void finishAccompany() {
        JSONObject main = preCreate("??????", "", true);
        JSONObject acc = preCreate("??????", "17602698599", false);
        JSONArray customers = new JSONArray();
        customers.add(main);
        customers.add(acc);
        AppReidReidDistributeScene.builder().reidInfoList(customers).enterType("PRE_SALE").build().visitor(visitor).getResponse();
    }

    /**
     * @description: ???????????????????????????????????????
     **/
    //@Test
    public void finish() {
        AppPreSalesReceptionPageScene.builder().build().visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> e.toJavaObject(PreReceptionBean.class)).forEach(e -> finishReception(e));
        //AppPreSalesReceptionPageScene.builder().size(100).build().execute(visitor);
    }


}