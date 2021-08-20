package com.haisheng.framework.testng.bigScreen.jiaochen.mc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppCustomerEditV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception.AppReceptorChangeScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.reid.AppReidReidDistributeScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.retention.AppRetentionReidCustomerAddScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.saleschedule.*;
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
import java.util.*;
import java.util.stream.Collectors;

public class SystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_DAILY_JD;
    private static final EnumAccount ACCOUNT = EnumAccount.JC_ALL_AUTHORITY_DAILY_NEW;
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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.setProduct(PRODUCE.getAbbreviation()).setReferer(PRODUCE.getReferer()).setShopId(ACCOUNT.getReceptionShopId()).setRoleId(ACCOUNT.getRoleId());
        beforeClassInit(commonConfig);
        util.loginApp(ACCOUNT);
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
     * @description : 门店中状态为接待中的销售，判断状态是否正确
     * @return  : list, 状态为接待中但没有接待卡片的销售
     **/
    public List<String> checkSalesStatus(String shopId){
        commonConfig.setShopId("-1");
        List<String> list1 = AppPreSalesReceptionPageScene.builder().size(40).build().execute(visitor, true).getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> e.getString("reception_sale_name")).distinct().collect(Collectors.toList());
        commonConfig.setShopId(shopId);
        List<String> list2 = AppSaleScheduleDayListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("sales_info_list").stream().map(e -> (JSONObject) e).filter(e -> Objects.equals(e.getString("sale_status"), "接待中")).map(e -> e.getString("sale_name")).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        for (String s : list2) {
            if (!list1.contains(s)){ list.add(s); }
        }
        return list;
//        Preconditions.checkArgument(list.size()==0,list+"：这些销售状态为接待中，在所有门店都没有接待中的卡片");
    }

    public void preCreate(){
        JSONArray customerList = new JSONArray();
        JSONObject customer = new JSONObject();
        Integer customerId = AppRetentionReidCustomerAddScene.builder().name(dt.getHistoryDate(0) + "自动2").phone("155" + CommonUtil.getRandom(8)).build().execute(visitor, true).getInteger("customer_id");
        customer.put("customer_id",customerId);
        customer.put("is_decision",true);
        customerList.add(customer);
        AppReidReidDistributeScene.builder().reidInfoList(customerList).enterType("PRE_SALE").build().execute(visitor);
    }
    /**
     * @description : 自动完成一个接待，自动编辑信息然后完成
     **/
    public String finishReception(AppReception reception){
        if (reception.getEstimatedBuyTime() == null){
            AppCustomerEditV4Scene.builder().id(reception.getId()).shopId(reception.getShopId()).customerId(reception.getCustomerId()).customerName(dt.getHistoryDate(0)+"自动姓名").sexId(1).
                    customerPhone("18" + CommonUtil.getRandom(9)).intentionCarModelId(util.getBuyCarId()).estimateBuyCarDate("2030-08-08").build().execute(visitor,false);
        }
        return AppFinishReceptionScene.builder().id(reception.getId()).shopId(reception.getShopId()).build().execute(visitor, false).getString("message");
    }

    @Test(description = "初始化销售状态，每个状态都有人")
    public void aaSaleStatusInit(){
        //AppSaleScheduleUpdateSaleStatusScene.builder().saleId("uid_38b574df").sourceSaleStatus(0).targetSaleStatus(3).vacationStartTime("2021-08-18").vacationEndTime("2035-08-18").build().invoke(visitor);
        AppSaleScheduleUpdateSaleStatusScene.builder().saleId(util.getBusySaleId()).sourceSaleStatus(0).targetSaleStatus(2).build().execute(visitor);

    }
    @Test(dataProvider = "notFreeSale")
    public void changeSale1(String description, Integer statusId){
        try {
            Object reception = AppPreSalesReceptionPageScene.builder().build().execute(visitor, true).getJSONArray("list").stream().findAny().orElse(null);
            if (reception == null){ preCreate(); }
            Long id = AppPreSalesReceptionPageScene.builder().build().execute(visitor, true).getJSONArray("list").getJSONObject(0).getLong("id");
            JSONObject neededSale = util.getNeededSale(statusId);
            if (neededSale != null){
                String saleId = neededSale.getString("sale_id");
                String message = AppReceptorChangeScene.builder().id(id).shopId(Long.parseLong(ACCOUNT.getReceptionShopId())).receptorId(saleId).build().execute(visitor,false).getString("message");
                Preconditions.checkArgument(Objects.equals("当前顾问非空闲,请选择其他顾问!",message),description+"，期待失败，实际："+message);
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
    public Object[] getNotFree(){
        return new Object[][]{
                {"变更销售状态为接待中",1},
                {"变更销售状态为忙碌中",2},
                {"变更销售状态为休假中",3}
        };
    }

    @Test(description = "变更接待")
    public void changeSale2(){
        try {
            Object reception = AppPreSalesReceptionPageScene.builder().build().execute(visitor, true).getJSONArray("list").stream().findAny().orElse(null);
            if (reception == null){ preCreate(); }
            String saleId = util.getNeededSale(0).getString("sale_id");
            JSONObject getReception = AppPreSalesReceptionPageScene.builder().build().execute(visitor, true).getJSONArray("list").getJSONObject(0);
            Long id = getReception.getLong("id");
            String receptorId = getReception.getString("receptor_id");
            AppReceptorChangeScene.builder().id(id).shopId(Long.parseLong(ACCOUNT.getReceptionShopId())).receptorId(saleId).build().execute(visitor,false);
            String lastId = util.getLastSale().getString("sale_id");
            Preconditions.checkArgument(Objects.equals(receptorId,lastId),"变更销售后，被替换的销售未在空闲中最后一位");
        }catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("变更成空闲的销售");
        }
    }

    /**
     * @description :分组设置
     **/
    @Test(description = "分组设置")
    public void groupConfig(){
        try {
            JSONArray groups = AppSaleScheduleGroupListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("group_infos");
            //拿到2组
            JSONObject second = groups.stream().map(e -> (JSONObject) e).sorted((x, y) -> x.getInteger("group_id") - y.getInteger("group_id")).collect(Collectors.toList()).get(1);
            // 操作前的总组数
            int size1 = groups.size();
            if (second != null) {
                // 删除2组
                String delMessage = AppSaleScheduleDelGroupScene.builder().groupId(second.getLong("group_id")).type("PRE").build().execute(visitor, false).getString("message");
                //删除2组后的组数
                int size2 = AppSaleScheduleGroupListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("group_infos").size();
                Preconditions.checkArgument(size2 == size1-1,"删除组失败:"+delMessage);
                //新建一个分组3组
                String addMessage = AppSaleScheduleAddGroupScene.builder().type("PRE").build().execute(visitor, false).getString("message");
                //新建3组后的组数
                JSONArray groups2 = AppSaleScheduleGroupListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("group_infos");
                int size3 = groups2.size();
                // 获取3组
                //JSONObject three = groups2.stream().map(e -> (JSONObject) e).filter(e -> Objects.equals("3组", e.getString("group_name"))).findFirst().orElse(null);
                JSONObject three = groups2.stream().map(e -> (JSONObject) e).min((x, y) -> y.getInteger("group_id") - x.getInteger("group_id")).get();
                Preconditions.checkArgument(size3 == size2 + 1, "创建组失败:" + addMessage);
                //获取前6个销售id列表
                List<String> list = AppSaleScheduleFreeSaleScene.builder().type("PRE").build().execute(visitor, true).
                        getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> e.getString("sale_id")).limit(6).collect(Collectors.toList());
                JSONArray saleList = new JSONArray();
                saleList.addAll(list);
                if (three != null) {
                    // 新增的3组里添加6个销售
                    String addSaleMessage = AppSaleScheduleJoinGroupScene.builder().groupId(three.getLong("group_id")).type("PRE").salesInfoList(saleList).build().execute(visitor, false).getString("message");
                    int groupSize = AppSaleScheduleGroupListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("group_infos").
                            stream().map(e -> (JSONObject) e).filter(e -> Objects.equals(three.getString("group_name"), e.getString("group_name"))).findFirst().orElse(null).
                            getJSONArray("sales_info_list").size();
                    Preconditions.checkArgument(groupSize==6,"3组添加销售失败:"+addSaleMessage);
                }
            }
        }catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("销售排班-设置销售分组流程操作");
        }
    }

    @Test(description = "分配给第一个空闲销售，完成接待销售变为空闲最后一位")
    public void assignReception(){
        try {
            JSONObject first = AppSaleScheduleDayListScene.builder().type("PRE").build().execute(visitor, true).getJSONArray("sales_info_list").stream().map(e -> (JSONObject) e).
                    filter(e -> Objects.equals(e.getString("sale_status"), "空闲中")).min((x, y) -> x.getInteger("order") - y.getInteger("order")).get();
            preCreate();
            AppReception reception = AppPreSalesReceptionPageScene.builder().build().execute(visitor).getJSONArray("list").getJSONObject(0).toJavaObject(AppReception.class);
            String receptionSaleId = reception.getReceptorId();
            Preconditions.checkArgument(Objects.equals(receptionSaleId,first.getString("sale_id")),
                    "分配的销售不是空闲第一位，空闲第一位销售："+first.getString("sale_name")+";分配给的销售："+reception.getReceptionSaleName());
            finishReception(reception);
            JSONObject lastSale = util.getLastSale();
            Preconditions.checkArgument(Objects.equals(lastSale.getString("sale_id"),receptionSaleId),
                    "完成接待后销售未变成空闲中最后一位，销售："+reception.getReceptionSaleName());
        }catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("分配给第一个空闲销售，完成接待该销售变为空闲最后一位");
        }
    }

    @Test
    public void finish(){
        AppReception appReception = AppPreSalesReceptionPageScene.builder().build().execute(visitor).getJSONArray("list").getJSONObject(0).toJavaObject(AppReception.class);
        finishReception(appReception);
    }

}