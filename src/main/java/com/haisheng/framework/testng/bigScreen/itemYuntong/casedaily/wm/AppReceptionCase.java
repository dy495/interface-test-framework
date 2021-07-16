package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.wm;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.homepagev4.AppTodayDataBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.presalesreception.AppPreSalesReceptionPageBean;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.homepagev4.AppTodayTaskScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppCustomerRemarkV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionCreateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerBuyCarPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerCreateCustomerScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoBuyCarRecordScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoRemarkRecordScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanagev4.PreSaleCustomerInfoScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluateV4PageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.BuyCarScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.FinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * app接待测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class AppReceptionCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY_SSO;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.YT_RECEPTION_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);
    private static AppPreSalesReceptionPageBean preSalesReceptionPage;
    CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //放入shopId
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = ALL_AUTHORITY.getReceptionShopId();
        commonConfig.roleId = ALL_AUTHORITY.getRoleId();
        beforeClassInit(commonConfig);
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
        util.loginPc(ALL_AUTHORITY);
        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
    }

    private void initAppPreSalesReceptionPageBean() {
        util.loginApp(ALL_AUTHORITY);
        preSalesReceptionPage = util.getAppPreSalesReceptionPageList().stream().filter(e -> e.getCustomerName().equals("自动化创建的接待人")).findFirst().orElse(null);
        if (preSalesReceptionPage == null) {
            logger.info("不存在接待人，需要创建");
            AppPreSalesReceptionCreateScene.builder().customerName("自动化创建的接待人").customerPhone("15366666666").sexId("1").intentionCarModelId(util.getCarModelId()).estimateBuyCarTime("2100-07-12").build().invoke(visitor);
            preSalesReceptionPage = util.getAppPreSalesReceptionPageList().stream().filter(e -> e.getCustomerName().equals("自动化创建的接待人")).findFirst().orElse(null);
        }
    }

    @Test(description = "app接待时产生新的节点，节点名称为销售创建")
    public void saleCustomerManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            IScene preSalesReceptionPageScene = PreSalesReceptionPageScene.builder().customerId(String.valueOf(preSalesReceptionPage.getCustomerId())).build();
            JSONObject response = util.toFirstJavaObject(preSalesReceptionPageScene, JSONObject.class);
            String receptionTypeName = response.getString("reception_type_name");
            Preconditions.checkArgument(receptionTypeName.equals("销售创建"), "app接待时产生新的节点，节点名称为：" + receptionTypeName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待时产生新的节点，节点名称为销售创建");
        }
    }

    @Test(description = "app接待时填写备注，备注记录+1")
    public void saleCustomerManager_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            IScene scene = PreSaleCustomerInfoRemarkRecordScene.builder().customerId(String.valueOf(preSalesReceptionPage.getCustomerId())).build();
            int total = scene.invoke(visitor).getInteger("total");
            util.loginApp(ALL_AUTHORITY);
            AppCustomerRemarkV4Scene.builder().id(String.valueOf(preSalesReceptionPage.getId())).customerId(String.valueOf(preSalesReceptionPage.getCustomerId())).remark(EnumDesc.DESC_BETWEEN_200_300.getDesc())
                    .shopId(util.getReceptionShopId()).build().invoke(visitor);
            util.loginPc(ALL_AUTHORITY);
            int newTotal = scene.invoke(visitor).getInteger("total");
            String remarkContent = util.toFirstJavaObject(scene, JSONObject.class).getString("remark_content");
            Preconditions.checkArgument(newTotal == total + 1, "app接待时填写备注前pc备注条数：" + total + " 填写备注后pc备注条数：" + newTotal);
            Preconditions.checkArgument(EnumDesc.DESC_BETWEEN_200_300.getDesc().equals(remarkContent), "app接待时填写备注后pc备注内容预期为：" + EnumDesc.DESC_BETWEEN_200_300.getDesc() + " 实际为：" + remarkContent);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待时填写备注，备注记录+1");
        }
    }

    @Test(description = "app接待时购买车辆，购车记录+1")
    public void saleCustomerManager_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            IScene preSaleCustomerBuyCarPageScene = PreSaleCustomerBuyCarPageScene.builder().build();
            int buyCarTotal = preSaleCustomerBuyCarPageScene.invoke(visitor).getInteger("total");
            IScene preSaleCustomerInfoBuyCarRecordScene = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(preSalesReceptionPage.getCustomerId()).build();
            int total = preSaleCustomerInfoBuyCarRecordScene.invoke(visitor).getInteger("total");
            String vin = util.createVin();
            //买车
            BuyCarScene.builder().carModel(Long.parseLong(util.getCarModelId())).carStyle(Long.parseLong(util.getCarStyleId())).vin(vin)
                    .id(preSalesReceptionPage.getId()).shopId(Long.parseLong(util.getReceptionShopId())).build().invoke(visitor);
            JSONObject response = preSaleCustomerInfoBuyCarRecordScene.invoke(visitor);
            int newTotal = response.getInteger("total");
            int newBuyCarTotal = preSaleCustomerBuyCarPageScene.invoke(visitor).getInteger("total");
            String vehicleChassisCode = response.getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");
            Preconditions.checkArgument(newTotal == total + 1, "app接待时购买车辆后，购车记录预期为：" + (total + 1) + " 实际为：" + newTotal);
            Preconditions.checkArgument(vehicleChassisCode.equals(vin), "app接待时购买车辆后，底盘号预期为：" + vin + " 实际为：" + vehicleChassisCode);
            Preconditions.checkArgument(newBuyCarTotal == buyCarTotal + 1, "app接待时购买车辆后，成交记录页预期为：" + (buyCarTotal + 1) + " 实际为：" + newBuyCarTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("app接待时购买车辆，购车记录+1");
        }
    }

    @Test(description = "pc新建成交记录，购车记录+1")
    public void saleCustomerManager_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            IScene scene = PreSaleCustomerInfoBuyCarRecordScene.builder().customerId(preSalesReceptionPage.getCustomerId()).build();
            int total = scene.invoke(visitor).getInteger("total");
            String vin = util.createVin();
            //新建成交记录
            PreSaleCustomerCreateCustomerScene.builder().customerPhone(preSalesReceptionPage.getCustomerPhone()).customerName(preSalesReceptionPage.getCustomerName())
                    .sex("1").customerType("PERSON").shopId(Long.parseLong(util.getReceptionShopId()))
                    .carStyleId(Long.parseLong(util.getCarStyleId())).carModelId(Long.parseLong(util.getCarModelId())).salesId(util.getSaleId())
                    .purchaseCarDate(DateTimeUtil.addDayFormat(new Date(), -10)).vehicleChassisCode(vin).build().invoke(visitor);
            JSONObject response = scene.invoke(visitor);
            int newTotal = response.getInteger("total");
            String vehicleChassisCode = response.getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");
            Preconditions.checkArgument(newTotal == total + 1, "新建成交记录后，购车记录预期为：" + (total + 1) + " 实际为：" + newTotal);
            Preconditions.checkArgument(vehicleChassisCode.equals(vin), "新建成交记录后，底盘号预期为：" + vin + " 实际为：" + vehicleChassisCode);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("pc新建成交记录，购车记录+1");
        }
    }

    @Test(description = "接待客户一次，更新最近到店时间为当前接待时间")
    public void saleCustomerManager_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            IScene scene = PreSaleCustomerInfoScene.builder().customerId(preSalesReceptionPage.getCustomerId()).shopId(Long.parseLong(util.getReceptionShopId())).build();
            String lastToShopDate = scene.invoke(visitor).getString("last_to_shop_date");
            String date = DateTimeUtil.getFormat(new Date());
            Preconditions.checkArgument(lastToShopDate.equals(date), "更新最近到店时间预期为：" + date + " 实际为：" + lastToShopDate);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待客户一次，更新最近到店时间为当前接待时间");
        }
    }

    @AfterClass(description = "完成此条接待记录")
    @Test(description = "完成接待，更新接待时间")
    public void saleCustomerManager_data_6() {
        logger.info(caseResult.getCaseName());
        try {
            initAppPreSalesReceptionPageBean();
            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            FinishReceptionScene.builder().id(preSalesReceptionPage.getId()).shopId(Long.parseLong(util.getReceptionShopId())).build().invoke(visitor);
            IScene scene = PreSalesReceptionPageScene.builder().phone(preSalesReceptionPage.getCustomerPhone()).build();
            JSONObject response = util.toFirstJavaObject(scene, JSONObject.class);
            Preconditions.checkArgument(response.getString("reception_end_time").contains(date), "预期完成接待时间：" + date + " 实际完成接待时间：" + response.getString("reception_end_time"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("完成接待，更新接待时间");
        }
    }

    //ok
    @Test(description = "个人单个门店-【今日任务】销售接待数据=【今日数据】销售接待数据")
    public void taskFollowUp_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        commonConfig.roleId = util.getRoleId();
        try {
            List<AppTodayDataBean> todayDataList = util.getAppTodayDataList();
            String prePendingReception = todayDataList.stream().map(AppTodayDataBean::getPrePendingReception).findFirst().orElse(null);
            String preReception = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_reception");
            CommonUtil.valueView(prePendingReception, preReception);
            Preconditions.checkArgument(prePendingReception != null);
            Preconditions.checkArgument(prePendingReception.equals(preReception), "【今日任务】销售接待数据：" + prePendingReception + " 【今日数据】销售接待数据：" + preReception);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.roleId = ALL_AUTHORITY.getRoleId();
            saveData("个人单个门店-【今日任务】销售接待数据=【今日数据】销售接待数据");
        }
    }

    //ok
    @Test(description = "个人单个门店-【今日任务】销售跟进数据=【今日数据】销售跟进数据")
    public void taskFollowUp_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        commonConfig.roleId = util.getRoleId();
        try {
            List<AppTodayDataBean> todayDataList = util.getAppTodayDataList();
            String prePendingReception = todayDataList.stream().map(AppTodayDataBean::getPrePendingFollow).findFirst().orElse(null);
            String preReception = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_follow");
            CommonUtil.valueView(prePendingReception, preReception);
            Preconditions.checkArgument(prePendingReception != null);
            Preconditions.checkArgument(prePendingReception.equals(preReception), "【今日任务】销售跟进数据：" + prePendingReception + " 【今日数据】销售跟进数据：" + preReception);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.roleId = ALL_AUTHORITY.getRoleId();
            saveData("个人单个门店-【今日任务】销售跟进数据=【今日数据】销售跟进数据");
        }
    }

    //ok
    @Test(description = "个人单个门店-【今日任务】销售接待分子=【销售接待】列表数")
    public void taskFollowUp_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        commonConfig.roleId = util.getRoleId();
        try {
            int receptionCount = util.getAppPreSalesReceptionPageList().size();
            String preReception = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_reception");
            int count = Integer.parseInt(preReception.split("/")[0]);
            CommonUtil.valueView(receptionCount, count);
            Preconditions.checkArgument(receptionCount == count, "【今日任务】销售接待分子：" + count + " 【销售接待】列表数：" + receptionCount);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.roleId = ALL_AUTHORITY.getRoleId();
            saveData("个人单个门店-【今日任务】销售接待分子=【销售接待】列表数");
        }
    }

    //ok
    @Test(description = "个人单个门店-【今日任务】销售接待分子=PC【销售接待记录】列表未完成接待总数")
    public void taskFollowUp_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        commonConfig.roleId = util.getRoleId();
        try {
            String preReception = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_reception");
            int receptionCount = Integer.parseInt(preReception.split("/")[0]);
            IScene scene = PreSalesReceptionPageScene.builder().build();
            int count = util.toJavaObjectList(scene, JSONObject.class, "is_finish", false).size();
            CommonUtil.valueView(receptionCount, count);
            Preconditions.checkArgument(receptionCount == count, "【今日任务】销售接待分子：" + receptionCount + " 【销售接待记录】列表未完成接待总数：" + count);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.roleId = ALL_AUTHORITY.getRoleId();
            saveData("个人单个门店-【今日任务】销售接待分子=PC【销售接待记录】列表未完成接待总数");
        }
    }

    //ok
    @Test(description = "个人单个门店-【今日任务】销售接待分母=PC【接待管理】列表当天完成接待总数")
    public void taskFollowUp_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        commonConfig.roleId = util.getRoleId();
        try {
            String date = DateTimeUtil.getFormat(new Date());
            String preReception = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_reception");
            int receptionCount = Integer.parseInt(preReception.split("/")[1]);
            IScene scene = PreSalesReceptionPageScene.builder().receptionStart(date).receptionEnd(date).build();
            int count = util.toJavaObjectList(scene, JSONObject.class, "is_finish", true).size();
            CommonUtil.valueView(receptionCount, count);
            Preconditions.checkArgument(receptionCount == count, "【今日任务】销售接待分母：" + receptionCount + " 【销售接待记录】列表当天完成接待总数：" + count);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.roleId = ALL_AUTHORITY.getRoleId();
            saveData("个人单个门店-【今日任务】销售接待分母=【销售接待记录】列表当天完成接待总数");
        }
    }

    //ok
    @Test(description = "个人单个门店-【今日任务】销售跟进分子=PC【销售线下接待评价】列表未跟进数量")
    public void taskFollowUp_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        commonConfig.roleId = util.getRoleId();
        try {
            String preFollow = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_follow");
            int followCount = Integer.parseInt(preFollow.split("/")[0]);
            IScene scene = EvaluateV4PageScene.builder().isFollowUp(false).evaluateType(5).build();
            int count = util.toJavaObjectList(scene, JSONObject.class).size();
            CommonUtil.valueView(followCount, count);
            Preconditions.checkArgument(followCount == count, "【今日任务】销售跟进分子：" + followCount + " PC【销售线下接待评价】列表未跟进数量：" + count);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.roleId = ALL_AUTHORITY.getRoleId();
            saveData("个人单个门店-【今日任务】销售跟进分子=PC【销售线下接待评价】列表未跟进数量");
        }
    }

    //ok
    @Test(description = "个人单个门店-【今日任务】销售跟进分母=PC【销售线下接待评价】列表今天产生跟进总数")
    public void taskFollowUp_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        commonConfig.roleId = util.getRoleId();
        try {
            String date = DateTimeUtil.getFormat(new Date());
            String preFollow = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_follow");
            int followCount = Integer.parseInt(preFollow.split("/")[1]);
            IScene scene = EvaluateV4PageScene.builder().isFollowUp(true).evaluateType(5).build();
            int count = util.toJavaObjectList(scene, JSONObject.class, "follow_time", date).size();
            CommonUtil.valueView(followCount, count);
            Preconditions.checkArgument(followCount == count, "【今日任务】销售跟进分母：" + followCount + " PC【销售线下接待评价】列表今天产生跟进总数：" + count);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.roleId = ALL_AUTHORITY.getRoleId();
            saveData("个人单个门店-【今日任务】销售跟进分母=PC【销售线下接待评价】列表今天产生跟进总数");
        }
    }

    //ok
    @Test(description = "全部多个门店-【今日任务】销售接待分子/分母=【今日数据】所有销售接待分子/分母之和")
    public void taskFollowUp_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppTodayDataBean> todayDataList = util.getAppTodayDataList();
            List<Integer> receptionList = new ArrayList<>();
            List<Integer> finishList = new ArrayList<>();
            todayDataList.stream().map(AppTodayDataBean::getPrePendingReception).filter(Objects::nonNull)
                    .map(e -> e.split("/")).forEach(strings -> {
                receptionList.add(Integer.parseInt(strings[0]));
                finishList.add(Integer.parseInt(strings[1]));
            });
            int receptionSum = receptionList.stream().mapToInt(e -> e).sum();
            int finishSum = finishList.stream().mapToInt(e -> e).sum();
            CommonUtil.valueView(receptionSum, finishSum);
            String preReception = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_reception");
            String[] strings = preReception.split("/");
            Preconditions.checkArgument(Integer.parseInt(strings[1]) == finishSum, "【今日任务】销售接待分母：" + Integer.parseInt(strings[1]) + " 【今日数据】所有销售接待分母之和：" + finishSum);
//            Preconditions.checkArgument(Integer.parseInt(strings[0]) == receptionSum, "【今日任务】销售接待分子：" + Integer.parseInt(strings[0]) + " 【今日数据】所有销售接待分子之和：" + receptionSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("全部多个门店-【今日任务】销售接待分子/分母=【今日数据】所有销售接待分子/分母之和");
        }
    }

    //ok
    @Test(description = "全部多个门店-【今日任务】销售跟进分子/分母=【今日数据】所有销售跟进分子/分母之和")
    public void taskFollowUp_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<AppTodayDataBean> todayDataList = util.getAppTodayDataList();
            List<Integer> receptionList = new ArrayList<>();
            List<Integer> finishList = new ArrayList<>();
            todayDataList.stream().map(AppTodayDataBean::getPrePendingFollow).filter(Objects::nonNull)
                    .map(e -> e.split("/")).forEach(strings -> {
                receptionList.add(strings[0].equals("-") ? 0 : Integer.parseInt(strings[0]));
                finishList.add(strings[1].equals("-") ? 0 : Integer.parseInt(strings[1]));
            });
            int receptionSum = receptionList.stream().mapToInt(e -> e).sum();
            int finishSum = finishList.stream().mapToInt(e -> e).sum();
            CommonUtil.valueView(receptionSum, finishSum);
            String preFollow = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_follow");
            String[] strings = preFollow.split("/");
            Preconditions.checkArgument(Integer.parseInt(strings[1]) == finishSum, "【今日任务】销售跟进分母：" + Integer.parseInt(strings[1]) + " 【今日数据】所有销售跟进分母之和：" + finishSum);
//            Preconditions.checkArgument(Integer.parseInt(strings[0]) == receptionSum, "【今日任务】销售跟进分子：" + Integer.parseInt(strings[0]) + " 【今日数据】所有销售跟进分子之和：" + receptionSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("全部多个门店-【今日任务】销售跟进分子/分母=【今日数据】所有销售跟进分子/分母之和");
        }
    }

    //ok
    @Test(description = "全部多个门店-【今日任务】销售接待分子=【销售接待】列表数")
    public void taskFollowUp_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = AppPreSalesReceptionPageScene.builder().build().invoke(visitor).getInteger("total");
            String preReception = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_reception");
            String[] strings = preReception.split("/");
            Preconditions.checkArgument(Integer.parseInt(strings[0]) == total, "【今日任务】销售接待分子：" + Integer.parseInt(strings[0]) + " 【销售接待】列表数：" + total);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("全部多个门店-【今日任务】销售接待分子=【销售接待】列表数");
        }
    }

    //ok
    @Test(description = "全部多个门店-【今日任务】销售接待分母=PC【接待管理】列表未完成接待总数")
    public void taskFollowUp_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        commonConfig.shopId = PRODUCE.getShopId();
        try {
            IScene scene = PreSalesReceptionPageScene.builder().build();
            int count = (int) util.toJavaObjectList(scene, JSONObject.class).stream().filter(e -> !e.getBoolean("is_finish")).count();
            String preReception = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_reception");
            String[] strings = preReception.split("/");
            int receptionCount = Integer.parseInt(strings[0]);
            CommonUtil.valueView(receptionCount, count);
            Preconditions.checkArgument(receptionCount == count, "【今日任务】销售接待分母：" + receptionCount + " PC【销售接待列表】未完成接待数：" + count);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.shopId = ALL_AUTHORITY.getReceptionShopId();
            saveData("全部多个门店-【今日任务】销售接待分母=PC【接待管理】列表未完成接待总数");
        }
    }

    //ok
    @Test(description = "全部多个门店-【今日任务】销售接待分子=PC【接待管理】列表今日完成接待总数")
    public void taskFollowUp_data_15() {
        logger.logCaseStart(caseResult.getCaseName());
        commonConfig.shopId = PRODUCE.getShopId();
        try {
            String date = DateTimeUtil.getFormat(new Date());
            IScene scene = PreSalesReceptionPageScene.builder().receptionStart(date).receptionEnd(date).build();
            int count = (int) util.toJavaObjectList(scene, JSONObject.class).stream().filter(e -> e.getBoolean("is_finish")).count();
            String preReception = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_reception");
            String[] strings = preReception.split("/");
            int receptionCount = Integer.parseInt(strings[1]);
            CommonUtil.valueView(receptionCount, count);
            Preconditions.checkArgument(receptionCount == count, "【今日任务】销售接待分子：" + receptionCount + " PC【接待管理】列表今日完成接待总数：" + count);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.shopId = ALL_AUTHORITY.getReceptionShopId();
            saveData("全部多个门店-【今日任务】销售接待分子=PC【接待管理】列表今日完成接待总数");
        }
    }

    //ok
    @Test(description = "全部多个门店-【今日任务】销售跟进分母=PC【销售接待线下评价】列表今日已跟进总数")
    public void taskFollowUp_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        commonConfig.shopId = PRODUCE.getShopId();
        try {
            String date = DateTimeUtil.getFormat(new Date());
            int total = EvaluateV4PageScene.builder().sourceCreateStart(date).sourceCreateEnd(date).isFollowUp(true).evaluateType(5).build().invoke(visitor).getInteger("total");
            String preFollow = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_follow");
            String[] strings = preFollow.split("/");
            int receptionCount = Integer.parseInt(strings[1]);
            CommonUtil.valueView(receptionCount, total);
            Preconditions.checkArgument(receptionCount == total, "【今日任务】销售跟进分母：" + receptionCount + " PC【销售接待线下评价】列表今日已跟进总数：" + total);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.shopId = ALL_AUTHORITY.getReceptionShopId();
            saveData("全部多个门店-【今日任务】销售跟进分母=PC【销售接待线下评价】列表今日已跟进总数");
        }
    }

    //no
    @Test(description = "全部多个门店-【今日任务】销售跟进分子=PC【销售接待线下评价】列表未跟进总数")
    public void taskFollowUp_data_17() {
        logger.logCaseStart(caseResult.getCaseName());
        commonConfig.shopId = PRODUCE.getShopId();
        try {
            int total = EvaluateV4PageScene.builder().isFollowUp(false).evaluateType(5).build().invoke(visitor).getInteger("total");
            String preFollow = AppTodayTaskScene.builder().build().invoke(visitor).getString("pre_follow");
            String[] strings = preFollow.split("/");
            int receptionCount = Integer.parseInt(strings[0]);
            CommonUtil.valueView(receptionCount, total);
            Preconditions.checkArgument(receptionCount == total, "【今日任务】销售跟进分子：" + receptionCount + " PC【销售接待线下评价】列表未跟进总数：" + total);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.shopId = ALL_AUTHORITY.getReceptionShopId();
            saveData("全部多个门店-【今日任务】销售跟进分子=PC【销售接待线下评价】列表未跟进总数");
        }
    }
}
