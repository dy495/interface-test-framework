package com.haisheng.framework.testng.bigScreen.jiaochenonline.mc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.customermanager.AppCustomerManagerPreCustomerAddPlateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppCustomerEditV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppCustomerRemarkV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppReceptorChangeScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.reid.AppReidReidDistributeScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.retention.AppRetentionReidCustomerAddScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.saleschedule.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.AppReceptionBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool.DataCenter;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool.FastContent;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppBuyCarScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount ACCOUNT = EnumAccount.JC_ONLINE_MC;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");

        //替换checklist的相关信息
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.setProduct(PRODUCE.getAbbreviation()).setReferer(PRODUCE.getReferer()).setShopId(ACCOUNT.getReceptionShopId()).setRoleId(ACCOUNT.getRoleId());
        beforeClassInit(commonConfig);
        util.loginApp(ACCOUNT);
        AppSaleScheduleUpdateSaleStatusScene.builder().saleId(util.getBusySaleId()).sourceSaleStatus(0).targetSaleStatus(2).build().execute(visitor);
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
     * @param shopId: 指定一个门店Id
     * @return : list, 状态为接待中但没有接待卡片的销售
     * @description : 门店中状态为接待中的销售，判断状态是否正确
     **/
    public List<String> checkSalesStatus(String shopId) {
        commonConfig.setShopId("-1");
        List<String> list1 = AppPreSalesReceptionPageScene.builder().size(40).build().execute(visitor, true).getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> e.getString("reception_sale_name")).distinct().collect(Collectors.toList());
        commonConfig.setShopId(shopId);
        List<String> list2 = AppSaleScheduleDayListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("sales_info_list").stream().map(e -> (JSONObject) e).filter(e -> Objects.equals(e.getString("sale_status"), "接待中")).map(e -> e.getString("sale_name")).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        for (String s : list2) {
            if (!list1.contains(s)) {
                list.add(s);
            }
        }
        return list;
//        Preconditions.checkArgument(list.size()==0,list+"：这些销售状态为接待中，在所有门店都没有接待中的卡片");
    }
    public JSONObject preCreate(String name, String phone,boolean isDecision){
        JSONObject customer = new JSONObject();
        Integer customerId = AppRetentionReidCustomerAddScene.builder().name(name).phone(phone).build().execute(visitor, true).getInteger("customer_id");
        customer.put("customer_id", customerId);
        customer.put("is_decision", isDecision);
        return customer;
    }

    /**
     * @param name :  姓名
     * @param phone : 手机
     * @description : 前台无人脸分配一个客户
     * @return : 创建分配接口的 message
     **/
    public JSONObject preAssign(String name, String phone){
        JSONArray customerList = new JSONArray();
        JSONObject customer = preCreate(name, phone, true);
        customerList.add(customer);
        return AppReidReidDistributeScene.builder().reidInfoList(customerList).enterType("PRE_SALE").build().execute(visitor, false);
    }

    /**
     * @param name:  姓名
     * @param phone: 手机
     * @description : 前台无人脸分配一个客户
     * @return : AppReceptionBean  ,分配的接待javaBean
     **/
    public AppReceptionBean preAssign(String name, String phone, boolean checkSale) {
        // 获取第一个空闲的销售
        JSONObject first = AppSaleScheduleDayListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("sales_info_list").stream().map(e -> (JSONObject) e).
                filter(e -> Objects.equals(e.getString("sale_status"), "空闲中")).min((x, y) -> x.getInteger("order") - y.getInteger("order")).get();
        // 前台分配
        String message = preAssign(name, phone).getString("message");
        Preconditions.checkArgument(Objects.equals("success", message), "前台分配分配失败,message:" + message);
        // 判断是否分给第一个空闲的销售
        if (checkSale) {
            AppReceptionBean reception = AppPreSalesReceptionPageScene.builder().build().execute(visitor).getJSONArray("list").getJSONObject(0).toJavaObject(AppReceptionBean.class);
            // 获取接待的接待销售
            String receptionSaleId = reception.getReceptorId();
            String receptionSaleName = reception.getReceptionSaleName();
            Preconditions.checkArgument(Objects.equals(receptionSaleId, first.getString("sale_id")),
                    "分配的销售不是空闲第一位，空闲第一位销售：" + first.getString("sale_name") + ";分配给的销售：" + receptionSaleName);
            return reception;
        } else {
            return null;
        }
    }

    /**
     * @param reception: AppReceptionBean : 接待javaBean
     * @return : 完成接待接口返回的 message
     * @description : 自动完成一个接待，自动编辑信息然后完成
     **/
    public String finishReception(AppReceptionBean reception) {
        if (reception.getEstimatedBuyTime() == null) {
            AppCustomerEditV4Scene.builder().id(reception.getId()).shopId(reception.getShopId()).customerId(reception.getCustomerId()).customerName(dt.getHistoryDate(0) + "自动完成_" + CommonUtil.getRandom(3)).sexId(1).
                    customerPhone("18" + CommonUtil.getRandom(9)).intentionCarModelId(util.getBuyCarId()).estimateBuyCarDate("2030-08-08").build().execute(visitor, false);
        }
        return finishReception(reception.getId(), reception.getShopId());
    }

    public String finishReception(long id, long shopId) {
        return AppFinishReceptionScene.builder().id(id).shopId(shopId).build().execute(visitor, false).getString("message");
    }

//    @Test(description = "初始化销售状态，每个状态都有人")
//    public void test01SaleStatusInit(){
//        //AppSaleScheduleUpdateSaleStatusScene.builder().saleId(util.getVacationSaleId()).sourceSaleStatus(0).targetSaleStatus(3).vacationStartTime("2021-08-18").vacationEndTime("2035-08-18").build().execute(visitor);
//        AppSaleScheduleUpdateSaleStatusScene.builder().saleId(util.getBusySaleId()).sourceSaleStatus(0).targetSaleStatus(2).build().execute(visitor);
//    }

    @Test(dataProvider = "notFreeSale")
    public void test02ChangeSale1(String description, Integer statusId) {
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().execute(visitor, true).getJSONArray("list").stream().findAny().orElse(null);
            AppReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("N2变更销售" + dt.getHistoryDate(0), null, true);
            }else {
                getReception = reception.toJavaObject(AppReceptionBean.class);
            }
            long id = getReception.getId();
            JSONObject neededSale = util.getNeededSale(statusId);
            if (neededSale != null) {
                String saleId = neededSale.getString("sale_id");
                String message = AppReceptorChangeScene.builder().id(id).shopId(Long.parseLong(ACCOUNT.getReceptionShopId())).receptorId(saleId).build().execute(visitor, false).getString("message");
                Preconditions.checkArgument(Objects.equals("当前顾问非空闲,请选择其他顾问!", message), description + "，期待失败，实际：" + message);
            } else {
                logger.warn("目前没有该状态的销售");
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("变更成非空闲的销售");
        }
    }

    @DataProvider(name = "notFreeSale")
    public Object[] getNotFree() {
        return new Object[][]{
                {"变更销售状态为接待中", 1},
                {"变更销售状态为忙碌中", 2},
                {"变更销售状态为休假中", 3}
        };
    }

    @Test(description = "变更接待")
    public void test02ChangeSale2() {
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().execute(visitor, true).getJSONArray("list").stream().findAny().orElse(null);
            AppReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("Y1变更销售" + dt.getHistoryDate(0), null, true);
            }else {
                getReception = reception.toJavaObject(AppReceptionBean.class);
            }
            String saleId = util.getNeededSale(0).getString("sale_id");
            String receptorId = getReception.getReceptorId();
            long id = getReception.getId();
            AppReceptorChangeScene.builder().id(id).shopId(Long.parseLong(ACCOUNT.getReceptionShopId())).receptorId(saleId).build().execute(visitor, false);
            String lastId = util.getLastSale().getString("sale_id");
            Preconditions.checkArgument(Objects.equals(receptorId, lastId), "变更销售后，被替换的销售未在空闲中最后一位");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("变更成空闲的销售");
        }
    }

    /**
     * @description :分组设置
     **/
    @Test(description = "分组设置")
    public void groupConfig() {
        try {
            // 操作前的总组数
            int size1;
            JSONArray groups = AppSaleScheduleGroupListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("group_infos");
            int s = groups.size();
            if (s < 2){
                for (int i = 0; i < (2-s); i++) {
                    AppSaleScheduleAddGroupScene.builder().type("PRE").build().execute(visitor, false);
                }
                size1 = 2;
                groups = AppSaleScheduleGroupListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("group_infos");
            }else {
                size1 = groups.size();
            }
            //拿到2组
            JSONObject second = groups.stream().map(e -> (JSONObject) e).sorted((x, y) -> x.getInteger("group_id") - y.getInteger("group_id")).collect(Collectors.toList()).get(1);
            if (second != null) {
                // 删除2组
                String delMessage = AppSaleScheduleDelGroupScene.builder().groupId(second.getLong("group_id")).type("PRE").build().execute(visitor, false).getString("message");
                //删除2组后的组数
                int size2 = AppSaleScheduleGroupListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("group_infos").size();
                Preconditions.checkArgument(size2 == size1 - 1, "删除组失败:" + delMessage);
                //新建一个分组3组
                String addMessage = AppSaleScheduleAddGroupScene.builder().type("PRE").build().execute(visitor, false).getString("message");
                //新建3组后的组数
                JSONArray groups2 = AppSaleScheduleGroupListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("group_infos");
                int size3 = groups2.size();
                // 获取3组
                //JSONObject three = groups2.stream().map(e -> (JSONObject) e).filter(e -> Objects.equals("3组", e.getString("group_name"))).findFirst().orElse(null);
                JSONObject three = groups2.stream().map(e -> (JSONObject) e).min((x, y) -> y.getInteger("group_id") - x.getInteger("group_id")).get();
                Preconditions.checkArgument(size3 == size2 + 1, "创建组失败:" + addMessage);
                //获取前3个销售id列表
                List<String> list = AppSaleScheduleFreeSaleScene.builder().type("PRE").build().execute(visitor, true).
                        getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> e.getString("sale_id")).limit(3).collect(Collectors.toList());
                JSONArray saleList = new JSONArray();
                saleList.addAll(list);
                // 新增的3组里添加3个销售
                String addSaleMessage = AppSaleScheduleJoinGroupScene.builder().groupId(three.getLong("group_id")).type("PRE").salesInfoList(saleList).build().execute(visitor, false).getString("message");
                int groupSize = AppSaleScheduleGroupListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("group_infos").
                        stream().map(e -> (JSONObject) e).filter(e -> Objects.equals(three.getString("group_name"), e.getString("group_name"))).findFirst().orElse(null).
                        getJSONArray("sales_info_list").size();
                Preconditions.checkArgument(groupSize == 3, "3组添加销售失败:" + addSaleMessage);

            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("销售排班-设置销售分组流程操作");
        }
    }

    // @Test(description = "分配给第一个空闲销售，完成接待销售变为空闲最后一位")
//    public void assignReceptionLine() {
//        try {
//            AppReceptionBean reception = preCreate("分配流程"+dt.getHistoryDate(0), "13"+CommonUtil.getRandom(9), true);
//            String receptionSaleId = reception.getReceptorId();
//            finishReception(reception);
//            JSONObject lastSale = util.getLastSale();
//            Preconditions.checkArgument(Objects.equals(lastSale.getString("sale_id"), receptionSaleId),
//                    "完成接待后销售未变成空闲中最后一位，销售：" + reception.getReceptionSaleName());
//        } catch (AssertionError | Exception e) {
//            collectMessage(e);
//        } finally {
//            saveData("分配给第一个空闲销售，完成接待该销售变为空闲最后一位");
//        }
//    }

    @Test(description = "完成接待销售变为空闲最后一位")
    public void zFinishReception() {
        try {
            AppReceptionBean reception = null;
            JSONObject res = AppPreSalesReceptionPageScene.builder().build().execute(visitor, true);
            Integer total = res.getInteger("total");
            if (total == 0){
                reception = preAssign("完成接待"+dt.getHistoryDate(0), "17"+CommonUtil.getRandom(9), true);
            }else {
                // 获取最后一个接待（第一个创建的）
                reception = res.getJSONArray("list").getJSONObject(total - 1).toJavaObject(AppReceptionBean.class);
            }
            String receptionSaleId = reception.getReceptorId();
            finishReception(reception);
            JSONObject lastSale = util.getLastSale();
            Preconditions.checkArgument(Objects.equals(lastSale.getString("sale_id"), receptionSaleId),
                    "完成接待后销售未变成空闲中最后一位，销售：" + reception.getReceptionSaleName());
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("完成接待该销售变为空闲最后一位");
        }
    }

    @Test(dataProvider = "preSuccess")
    public void test01AssignReceptionY(String description, String name, String phone) {
        try {
            // 前台分配
            String message = preAssign(name, phone).getString("message");
            Preconditions.checkArgument(Objects.equals("success", message),description+"，期待成功，实际返回message："+message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("前台分配正常情况");
        }
    }
    @DataProvider(name = "preSuccess")
    private Object[] preSuccess(){
        return new Object[][]{
                {"正常名字50字", FastContent.NAME50,""},
                {"正常名字1字", ".",""}
        };
    }

    //@Test(dataProvider = "preFalse", dataProviderClass = DataCenter.class)
    public void test01AssignReceptionN(String description, String name, String phone) {
        try {
            // 前台分配
            JSONObject creat = preAssign(name, phone);
            String code = creat.getString("code");
            String message = creat.getString("message");
            Preconditions.checkArgument(Objects.equals("1001", code),description+"，期待失败，实际返回message："+message+"成功的手机号"+phone);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("前台分配异常情况");
        }
    }

    @Test(description = "接待中编辑资料，异常情况",dataProvider = "editErrorInfo", dataProviderClass = DataCenter.class)
    public void test03EditUserInfo(String description, String point, String content, String expect){
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().execute(visitor, true).getJSONArray("list").stream().findAny().orElse(null);
            AppReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("创建接待for修改资料" + dt.getHistoryDate(0), null, true);
            }else {
                getReception = reception.toJavaObject(AppReceptionBean.class);
            }
            sleep(3);
            String message = AppCustomerEditV4Scene.builder().id(getReception.getId()).customerId(getReception.getCustomerId()).shopId(getReception.getShopId()).
                    customerName("正常normal").customerPhone("18" + CommonUtil.getRandom(9)).sexId(1).
                    intentionCarModelId(util.getBuyCarId()).estimateBuyCarDate("2030-08-08").build()
                    .modify(point, content).execute(visitor, false).getString("message");
            Preconditions.checkArgument(Objects.equals(expect, message),"修改资料"+description+"，预期结果："+expect+"，实际："+message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("接待中编辑资料，异常情况");
        }
    }


    @Test(description = "接待中购车异常情况",dataProvider = "errorBuyCar", dataProviderClass = DataCenter.class)
    public void test04receptionBuyCar(String description, String point, String content, String expect){
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().execute(visitor, true).getJSONArray("list").stream().findAny().orElse(null);
            AppReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("创建接待for购车" + dt.getHistoryDate(0), null, true);
            }else {
                getReception = reception.toJavaObject(AppReceptionBean.class);
            }
            String message = AppBuyCarScene.builder().id(getReception.getId()).shopId(getReception.getShopId()).carModel(util.getBuyCarId()).vin(null).build().modify(point, content).execute(visitor, false).getString("message");
            Preconditions.checkArgument(Objects.equals(expect, message),"接待中购车"+description+"，预期结果："+expect+"，实际："+message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("接待中编辑资料，异常情况");
        }
    }

    @Test(description = "用户添加车牌号,异常",dataProvider = "addPlate", dataProviderClass = DataCenter.class)
    public void test05AddPlateNumber(String description, String content, String expect){
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().execute(visitor, true).getJSONArray("list").stream().findAny().orElse(null);
            AppReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("创建接待for加车牌" + dt.getHistoryDate(0), null, true);
            }else {
                getReception = reception.toJavaObject(AppReceptionBean.class);
            }
            String message = AppCustomerManagerPreCustomerAddPlateScene.builder().customerId(getReception.getCustomerId()).shopId(getReception.getShopId()).plateNumber(content).build().execute(visitor, false).getString("message");
            Preconditions.checkArgument(Objects.equals(expect, message),"接待中添加车牌号"+description+"，预期结果："+expect+"，实际："+message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("用户添加车牌号,异常");
        }
    }

    @Test(description = "接待中备注",dataProvider = "remark", dataProviderClass = DataCenter.class)
    public void test06receptionRemark(String description, String content, String expect){
        try {
            JSONObject reception = (JSONObject) AppPreSalesReceptionPageScene.builder().build().execute(visitor, true).getJSONArray("list").stream().findAny().orElse(null);
            AppReceptionBean getReception;
            if (reception == null) {
                getReception = preAssign("创建接待for加车牌" + dt.getHistoryDate(0), null, true);
            }else {
                getReception = reception.toJavaObject(AppReceptionBean.class);
            }
            String code = AppCustomerRemarkV4Scene.builder().id(getReception.getId()).shopId(getReception.getShopId()).remark(content).build().execute(visitor, false).getString("code");
            Preconditions.checkArgument(Objects.equals(expect, code),"接待中备注"+description+"，预期结果code："+expect+"，实际code："+code);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("接待中备注");
        }
    }

    //@Test(description = "自动完成接待，手动清接待")
    public void finish() {
        AppPreSalesReceptionPageScene.builder().build().execute(visitor).getJSONArray("list").stream().map(e->(JSONObject) e).map(e->e.toJavaObject(AppReceptionBean.class)).forEach(e->finishReception(e));
        //AppPreSalesReceptionPageScene.builder().size(100).build().execute(visitor);
    }

}