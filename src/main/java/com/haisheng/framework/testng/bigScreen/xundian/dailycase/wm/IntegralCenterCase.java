package com.haisheng.framework.testng.bigScreen.xundian.dailycase.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletExchangeRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletIntegralRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletShippingAddress;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ExchangeDetailed;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ExchangePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.ChangeStockTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.CommodityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralExchangeStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.OrderStatusEnum;
import com.haisheng.framework.testng.bigScreen.xundian.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.xundian.enumerator.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.xundian.generator.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.xundian.scene.applet.granted.*;
//import com.haisheng.framework.testng.bigScreen.xundian.scene.pc.integralcenter.*;
//import com.haisheng.framework.testng.bigScreen.xundian.scene.pc.integralmall.GoodsManagePageScene;
import com.haisheng.framework.testng.bigScreen.xundian.scene.pc.integralcenter.*;
import com.haisheng.framework.testng.bigScreen.xundian.scene.pc.integralmall.GoodsManagePageScene;
import com.haisheng.framework.testng.bigScreen.xundian.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.xundian.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分中心测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class IntegralCenterCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.INS_DAILY;
    private static final AccountEnum ALL_AUTHORITY = AccountEnum.YUE_XIU_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.INS_WM_DAILY;
    private static final Integer SIZE = 100;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
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
        user.loginPc(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    //ok
    @Test(description = "积分客户管理--增加某人积分")
    public void integralCustomerPage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            visitor.login(APPLET_USER_ONE.getToken());
            Integer score = AppletUserInfoDetailScene.builder().build().invoke(visitor).getInteger("score");
            user.loginPc(ALL_AUTHORITY);
            //变更记录
            IScene scene = CustomerIntegralChangeRecordPageScene.builder().build();
            List<CustomerIntegralChangeRecordPageBean> changeRecordList = util.toJavaObjectList(scene, CustomerIntegralChangeRecordPageBean.class);
            JSONObject data = CustomerPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build().invoke(visitor);
            Long id = data.getJSONArray("list").getJSONObject(0).getLong("id");
            CustomerIntegralChangeScene.builder().id(id).changeType(ChangeStockTypeEnum.ADD.name()).remark(EnumDesc.DESC_BETWEEN_5_10.getDesc()).integral(1L).build().invoke(visitor);
            //变更记录
            List<CustomerIntegralChangeRecordPageBean> newChangeRecordList = util.toJavaObjectList(scene, CustomerIntegralChangeRecordPageBean.class);
            CommonUtil.checkResult("增加积分后，变更记录页列表条数", changeRecordList.size() + 1, newChangeRecordList.size());
            //变更内容
            CommonUtil.checkResult("联系方式", APPLET_USER_ONE.getPhone(), newChangeRecordList.get(0).getCustomerPhone());
            CommonUtil.checkResult("操作人", ALL_AUTHORITY.getName(), newChangeRecordList.get(0).getOperatorName());
            CommonUtil.checkResult("操作账号", ALL_AUTHORITY.getPhone(), newChangeRecordList.get(0).getOperatorAccount());
            CommonUtil.checkResult("积分变动", "+1", newChangeRecordList.get(0).getIntegral());
            CommonUtil.checkResult("备注", EnumDesc.DESC_BETWEEN_5_10.getDesc(), newChangeRecordList.get(0).getRemark());
            ExchangeDetailed exchangeDetailed = util.toJavaObjectList(ExchangeDetailedScene.builder().phone(APPLET_USER_ONE.getPhone()).build(), ExchangeDetailed.class).get(0);
            CommonUtil.checkResult("pc积分明细变更内容", 1, exchangeDetailed.getStockDetail());
            CommonUtil.checkResult("pc积分明细兑换类型", ChangeStockTypeEnum.ADD.getDescription(), exchangeDetailed.getExchangeTypeName());
            CommonUtil.checkResult("pc积分明细变更类型", ChangeStockTypeEnum.ADD.name(), exchangeDetailed.getExchangeType());
            visitor.login(APPLET_USER_ONE.getToken());
            Integer newScore = AppletUserInfoDetailScene.builder().build().invoke(visitor).getInteger("score");
            CommonUtil.checkResult("变更积分后" + APPLET_USER_ONE.getPhone() + "的积分为", score + 1, newScore);
            AppletIntegralRecord appletIntegralRecord = util.getAppletIntegralRecordList().get(0);
            CommonUtil.checkResult("applet积分明细变更内容", EnumDesc.DESC_BETWEEN_5_10.getDesc(), appletIntegralRecord.getName());
            CommonUtil.checkResult("applet积分明细变更积分", "1", appletIntegralRecord.getIntegral());
            CommonUtil.checkResult("applet积分明细变更类型", ChangeStockTypeEnum.ADD.name(), appletIntegralRecord.getChangeType());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分客户管理--增加某人积分");
        }
    }

    //ok
    @Test(description = "积分客户管理--减少某人积分")
    public void integralCustomerPage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            visitor.login(APPLET_USER_ONE.getToken());
            Integer score = AppletUserInfoDetailScene.builder().build().invoke(visitor).getInteger("score");
            user.loginPc(ALL_AUTHORITY);
            //变更记录
            IScene scene = CustomerIntegralChangeRecordPageScene.builder().build();
            List<CustomerIntegralChangeRecordPageBean> changeRecordList = util.toJavaObjectList(scene, CustomerIntegralChangeRecordPageBean.class);
            JSONObject data = CustomerPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build().invoke(visitor);
            Long id = data.getJSONArray("list").getJSONObject(0).getLong("id");
            CustomerIntegralChangeScene.builder().id(id).changeType(ChangeStockTypeEnum.MINUS.name()).remark(EnumDesc.DESC_BETWEEN_5_10.getDesc()).integral(1L).build().invoke(visitor);
            //变更记录
            List<CustomerIntegralChangeRecordPageBean> newChangeRecordList = util.toJavaObjectList(scene, CustomerIntegralChangeRecordPageBean.class);
            CommonUtil.checkResult("增加积分后，变更记录页列表条数", changeRecordList.size() + 1, newChangeRecordList.size());
            //变更内容
            CommonUtil.checkResult("联系方式", APPLET_USER_ONE.getPhone(), newChangeRecordList.get(0).getCustomerPhone());
            CommonUtil.checkResult("操作人", ALL_AUTHORITY.getName(), newChangeRecordList.get(0).getOperatorName());
            CommonUtil.checkResult("操作账号", ALL_AUTHORITY.getPhone(), newChangeRecordList.get(0).getOperatorAccount());
            CommonUtil.checkResult("积分变动", "-1", newChangeRecordList.get(0).getIntegral());
            CommonUtil.checkResult("备注", EnumDesc.DESC_BETWEEN_5_10.getDesc(), newChangeRecordList.get(0).getRemark());
            ExchangeDetailed exchangeDetailed = util.toJavaObjectList(ExchangeDetailedScene.builder().phone(APPLET_USER_ONE.getPhone()).build(), ExchangeDetailed.class).get(0);
            CommonUtil.checkResult("pc积分明细变更内容", 1, exchangeDetailed.getStockDetail());
            CommonUtil.checkResult("pc积分明细兑换类型", ChangeStockTypeEnum.MINUS.getDescription(), exchangeDetailed.getExchangeTypeName());
            CommonUtil.checkResult("pc积分明细变更类型", ChangeStockTypeEnum.MINUS.name(), exchangeDetailed.getExchangeType());
            visitor.login(APPLET_USER_ONE.getToken());
            Integer newScore = AppletUserInfoDetailScene.builder().build().invoke(visitor).getInteger("score");
            CommonUtil.checkResult("变更积分后" + APPLET_USER_ONE.getPhone() + "的积分为", score - 1, newScore);
            AppletIntegralRecord appletIntegralRecord = util.getAppletIntegralRecordList().get(0);
            CommonUtil.checkResult("applet积分明细变更内容", EnumDesc.DESC_BETWEEN_5_10.getDesc(), appletIntegralRecord.getName());
            CommonUtil.checkResult("applet积分明细变更积分", "1", appletIntegralRecord.getIntegral());
            CommonUtil.checkResult("applet积分明细变更类型", ChangeStockTypeEnum.MINUS.name(), appletIntegralRecord.getChangeType());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分客户管理--减少某人积分");
        }
    }

    //ok
    @Test(description = "积分客户管理--增加某人积分异常")
    public void integralCustomerPage_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = CustomerPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build().invoke(visitor);
            Long id = data.getJSONArray("list").getJSONObject(0).getLong("id");
            Long[] integrals = {0L, null, 100001L};
            Arrays.stream(integrals).forEach(integral -> {
                String message = CustomerIntegralChangeScene.builder().id(id).changeType(ChangeStockTypeEnum.ADD.name()).remark(EnumDesc.DESC_BETWEEN_5_10.getDesc()).integral(integral).build().invoke(visitor, false).getString("message");
                String err = integral == null ? "积分变更类型不能为空" : "变更积分取值范围[1,100000]";
                CommonUtil.checkResult("增加积分" + integral, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分客户管理--增加某人积分异常");
        }
    }

    //ok
    @Test(description = "积分客户管理--扣减某人减积分大于剩余积分")
    public void integralCustomerPage_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = CustomerPageScene.builder().build().invoke(visitor).getJSONArray("list");
            JSONObject data = list.stream().map(e -> (JSONObject) e).filter(e -> Long.parseLong(e.getString("integral")) < 100000L).findFirst().orElse(null);
            Preconditions.checkArgument(data != null, "没找到剩余积分<100000的客户");
            Long integral = data.getLong("integral");
            Long id = data.getLong("id");
            String message = CustomerIntegralChangeScene.builder().id(id).changeType(ChangeStockTypeEnum.MINUS.name()).remark(EnumDesc.DESC_BETWEEN_5_10.getDesc()).integral(integral + 1).build().invoke(visitor, false).getString("message");
            String err = "积分不足";
            CommonUtil.checkResult("增加积分" + integral, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分客户管理--扣减某人减积分大于剩余积分");
        }
    }

    //ok
//    @Test(description = "积分兑换--库存详情--当前库存=兑换品库存明细加和")
//    public void integralExchange_data_1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //上线日期，之前得数据不做校验
//            long time = Long.parseLong(DateTimeUtil.dateToStamp("2021-02-25", "yyyy-MM-dd"));
//            IScene exchangePageScene = ExchangePageScene.builder().build();
//            List<JSONObject> exchangePageList = util.collectBean(exchangePageScene, JSONObject.class);
//            exchangePageList.stream().filter(e -> Long.parseLong(DateTimeUtil.dateToStamp(e.getString("begin_use_time"))) >= time).forEach(e -> {
//                int id = e.getInteger("id");
//                AtomicInteger s = new AtomicInteger();
//                IScene exchangeStockScene = ExchangeGoodsStockScene.builder().id((long) id).build();
//                JSONObject response = visitor.invokeApi(exchangeStockScene);
//                String goodsName = response.getString("goods_name");
//                int goodsStock = response.getInteger("goods_stock");
//                IScene exchangeStockPageScene = ExchangeStockPageScene.builder().id((long) id).build();
//                List<JSONObject> exchangeStockPageList = util.collectBean(exchangeStockPageScene, JSONObject.class);
//                exchangeStockPageList.forEach(a -> {
//                    String exchangeType = a.getString("exchange_type");
//                    int stockDetail = a.getInteger("stock_detail");
//                    s.set(exchangeType.equals("ADD") ? s.addAndGet(stockDetail) : s.addAndGet(-stockDetail));
//                });
//                CommonUtil.checkResultPlus(goodsName + " 兑换品库存明细加和", s.get(), "当前库存", goodsStock);
//                CommonUtil.logger(goodsName);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("积分兑换--库存详情--当前库存=兑换品库存明细加和");
//        }
//    }

    //ok
    @Test(description = "积分明细--创建实物积分兑换，积分兑换列表+1")
    public void integralExchange_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ExchangePageScene.builder().build();
            int total = scene.invoke(visitor).getInteger("total");
            //创建实物兑换
            util.createExchangeRealGoods();
            sleep(1);
            ExchangePageBean exchangePage = util.toJavaObjectList(scene, ExchangePageBean.class).get(0);
            int newTotal = scene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult("创建积分兑换后积分兑换总数", total + 1, newTotal);
            CommonUtil.checkResult("创建积分兑换后状态", IntegralExchangeStatusEnum.WORKING.getDesc(), exchangePage.getStatusName());
            CommonUtil.checkResult("创建积分兑换后状态", IntegralExchangeStatusEnum.WORKING.name(), exchangePage.getStatus());
            //删除商品
            ChangeSwitchStatusScene.builder().id(exchangePage.getId()).status(false).build().invoke(visitor);
            DeleteExchangeGoodsScene.builder().id(exchangePage.getId()).build().invoke(visitor);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分明细--创建实物积分兑换，积分兑换列表+1");
        }
    }


    @Test(description = "积分明细--创建虚拟积分兑换，积分兑换列表+1")
    public void integralExchange_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ExchangePageScene.builder().build();
            int total = scene.invoke(visitor).getInteger("total");
            Long voucherId = new VoucherGenerator.Builder().status(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId();
            //创建虚拟兑换
            util.createExchangeFictitiousGoods(voucherId);
            sleep(1);
            ExchangePageBean exchangePage = util.toJavaObjectList(scene, ExchangePageBean.class).get(0);
            int newTotal = ExchangePageScene.builder().build().invoke(visitor).getInteger("total");
            CommonUtil.checkResult("创建积分兑换后积分兑换总数", total + 1, newTotal);
            CommonUtil.checkResult("创建积分兑换后状态", IntegralExchangeStatusEnum.WORKING.getDesc(), exchangePage.getStatusName());
            CommonUtil.checkResult("创建积分兑换后状态", IntegralExchangeStatusEnum.WORKING.name(), exchangePage.getStatus());
            //删除商品
            ChangeSwitchStatusScene.builder().id(exchangePage.getId()).status(false).build().invoke(visitor);
            DeleteExchangeGoodsScene.builder().id(exchangePage.getId()).build().invoke(visitor);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分明细--创建虚拟积分兑换，积分兑换列表+1");
        }
    }

    //ok
    @Test(description = "积分兑换--创建实体积分兑换")
    public void integralExchange_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            JSONArray specificationList = new JSONArray();
            JSONObject response = GoodsManagePageScene.builder().goodsStatus("UP").build().invoke(visitor).getJSONArray("list").getJSONObject(0);
            long goodsId = response.getLong("id");
            String goodsName = response.getString("goods_name");
            JSONArray specificationDetailList = CommoditySpecificationsListScene.builder().id(goodsId).build().invoke(visitor).getJSONArray("specification_detail_list");
            specificationDetailList.forEach(e -> {
                JSONObject specificationDetail = (JSONObject) e;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", specificationDetail.getInteger("id"));
                jsonObject.put("stock", 1);
                specificationList.add(jsonObject);
            });
            //创建积分兑换
            CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId).exchangePrice(1L)
                    .isLimit(true).exchangePeopleNum(1).specificationList(specificationList).exchangeStartTime(exchangeStartTime)
                    .exchangeEndTime(exchangeEndTime).build().invoke(visitor);
            sleep(1);
            IScene exchangePageScene = ExchangePageScene.builder().build();
            ExchangePage exchangePage = util.toJavaObjectList(exchangePageScene, ExchangePage.class).get(0);
            CommonUtil.checkResult("商品名称", goodsName, exchangePage.getGoodsName());
            CommonUtil.checkResult("兑换类型", CommodityTypeEnum.REAL.getName(), exchangePage.getExchangeTypeName());
            CommonUtil.checkResult("兑换类型", CommodityTypeEnum.REAL.name(), exchangePage.getExchangeType());
            CommonUtil.checkResult("兑换价格", 1, exchangePage.getExchangePrice());
            CommonUtil.checkResult("已兑换/剩余", "0/" + specificationDetailList.size(), exchangePage.getExchangedAndSurplus());
            CommonUtil.checkResult("兑换数量", 1, exchangePage.getExchangeNumber());
            CommonUtil.checkResult("状态", IntegralExchangeStatusEnum.WORKING.getDesc(), exchangePage.getStatusName());
            CommonUtil.checkResult("状态", IntegralExchangeStatusEnum.WORKING.name(), exchangePage.getStatus());
            CommonUtil.checkResult("开始时间", exchangeStartTime, exchangePage.getBeginUseTime());
            CommonUtil.checkResult("结束时间", exchangeEndTime, exchangePage.getEndUseTime());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            long id = ExchangePageScene.builder().build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
            ChangeSwitchStatusScene.builder().id(id).status(false).build().invoke(visitor);
            DeleteExchangeGoodsScene.builder().id(id).build().invoke(visitor);
            saveData("积分兑换--创建实体积分兑换");
        }
    }

    //ok
    @Test(description = "积分兑换--创建虚拟积分兑换")
    public void integralExchange_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            long exchangeNum = voucherPage.getSurplusInventory() == 1 ? 1 : voucherPage.getSurplusInventory() - 1;
            //创建积分兑换
            CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId).exchangePrice(1L)
                    .exchangeNum(exchangeNum).isLimit(true).exchangePeopleNum(1).exchangeStartTime(exchangeStartTime)
                    .exchangeEndTime(exchangeEndTime).build().invoke(visitor);
            sleep(1);
            IScene exchangePageScene = ExchangePageScene.builder().build();
            ExchangePage exchangePage = util.toJavaObjectList(exchangePageScene, ExchangePage.class).get(0);
            CommonUtil.checkResult("商品名称", voucherPage.getVoucherName(), exchangePage.getGoodsName());
            CommonUtil.checkResult("兑换类型", CommodityTypeEnum.FICTITIOUS.getName(), exchangePage.getExchangeTypeName());
            CommonUtil.checkResult("兑换类型", CommodityTypeEnum.FICTITIOUS.name(), exchangePage.getExchangeType());
            CommonUtil.checkResult("兑换价格", 1, exchangePage.getExchangePrice());
            CommonUtil.checkResult("已兑换/剩余", "0/" + exchangeNum, exchangePage.getExchangedAndSurplus());
            CommonUtil.checkResult("兑换数量", 1, exchangePage.getExchangeNumber());
            CommonUtil.checkResult("状态", IntegralExchangeStatusEnum.WORKING.getDesc(), exchangePage.getStatusName());
            CommonUtil.checkResult("状态", IntegralExchangeStatusEnum.WORKING.name(), exchangePage.getStatus());
            CommonUtil.checkResult("开始时间", exchangeStartTime, exchangePage.getBeginUseTime());
            CommonUtil.checkResult("结束时间", exchangeEndTime, exchangePage.getEndUseTime());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            long id = ExchangePageScene.builder().build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
            ChangeSwitchStatusScene.builder().id(id).status(false).build().invoke(visitor);
            DeleteExchangeGoodsScene.builder().id(id).build().invoke(visitor);
            saveData("积分兑换--创建虚拟积分兑换");
        }
    }

    //ok
    @Test(description = "积分兑换--创建虚拟积分兑换，可兑换数量大于卡券库存")
    public void integralExchange_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            long exchangeNum = voucherPage.getSurplusInventory() + 1;
            //创建积分兑换
            String message = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId).exchangePrice(1L)
                    .exchangeNum(exchangeNum).isLimit(true).exchangePeopleNum(1).exchangeStartTime(exchangeStartTime)
                    .exchangeEndTime(exchangeEndTime).build().invoke(visitor, false).getString("message");
            String err = "卡券【" + voucherPage.getVoucherName() + "】库存不足";
            CommonUtil.checkResult("可兑换数量为" + exchangeNum, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--创建虚拟积分兑换，可兑换数量大于卡券库存");
        }
    }

    //ok
    @Test(description = "积分兑换--创建虚拟积分兑换，兑换价异常")
    public void integralExchange_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            long exchangeNum = voucherPage.getSurplusInventory() == 1 ? 1 : voucherPage.getSurplusInventory() - 1;
            Long[] longs = {null};
            Arrays.stream(longs).forEach(exchangePrice -> {
                //创建积分兑换
                String message = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId).exchangePrice(exchangePrice)
                        .exchangeNum(exchangeNum).isLimit(true).exchangePeopleNum(1).exchangeStartTime(exchangeStartTime)
                        .exchangeEndTime(exchangeEndTime).build().invoke(visitor, false).getString("message");
                String err = exchangePrice == null ? "兑换价格不能为空" : "请求入参类型不正确";
                CommonUtil.checkResult("兑换价为" + exchangePrice, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--创建虚拟积分兑换，兑换价异常");
        }
    }

    //ok
    @Test(description = "积分兑换--创建虚拟积分兑换，包含无库存的卡券", enabled = false)
    public void integralExchange_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherPage(voucherId).getVoucherName();
            String message = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId).exchangePrice(1L)
                    .exchangeNum(1L).isLimit(true).exchangePeopleNum(1).exchangeStartTime(exchangeStartTime)
                    .exchangeEndTime(exchangeEndTime).build().invoke(visitor, false).getString("message");
            String err = "卡券【" + voucherName + "】库存不足";
            CommonUtil.checkResult("创建时包含无库存的卡券", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--创建虚拟积分兑换，包含无库存的卡券");
        }
    }

    //ok
    @Test(description = "积分兑换--修改实体积分兑换库存")
    public void integralExchange_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangePageScene = ExchangePageScene.builder().exchangeType(CommodityTypeEnum.REAL.name()).build();
            List<ExchangePage> exchangePageList = util.toJavaObjectList(exchangePageScene, ExchangePage.class);
            Long id = exchangePageList.stream().filter(e -> Integer.parseInt(e.getExchangedAndSurplus().split("/")[1]) < 10).map(ExchangePage::getId).findFirst().orElse(0L);
            IScene exchangeStockPageScene = ExchangeStockPageScene.builder().id(id).build();
            int exchangeStockPageListSize = util.toJavaObjectList(exchangeStockPageScene, ExchangeStockPageBean.class).size();
            String goodsName = ExchangeGoodsStockScene.builder().id(id).build().invoke(visitor).getString("goods_name");
            JSONObject specificationDetail = ExchangeCommoditySpecificationsListScene.builder().id(id).build().invoke(visitor).getJSONArray("specification_detail_list").getJSONObject(0);
            String firstSpecificationsName = specificationDetail.getString("first_specifications_name");
            Integer num = specificationDetail.getInteger("num");
            //增加库存
            EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.ADD.name()).num(1).id(specificationDetail.getLong("id")).goodsName(goodsName).type(CommodityTypeEnum.REAL.name()).build().invoke(visitor);
            Integer numTow = ExchangeCommoditySpecificationsListScene.builder().id(id).build().invoke(visitor).getJSONArray("specification_detail_list").getJSONObject(0).getInteger("num");
            //规格详情剩余库存	+1
            CommonUtil.checkResult(firstSpecificationsName + "剩余库存", num + 1, numTow);
            //库存明细
            List<ExchangeStockPageBean> exchangeStockPageList = util.toJavaObjectList(exchangeStockPageScene, ExchangeStockPageBean.class);
            CommonUtil.checkResult("兑换品库存明细列表数", exchangeStockPageListSize + 1, exchangeStockPageList.size());
            CommonUtil.checkResult("兑换品库存明细手机号", ALL_AUTHORITY.getPhone(), exchangeStockPageList.get(0).getSalePhone());
            CommonUtil.checkResult("兑换品库存明细操作人", ALL_AUTHORITY.getName(), exchangeStockPageList.get(0).getSaleName());
            CommonUtil.checkResult("兑换品库存明细库存明细", 1L, exchangeStockPageList.get(0).getStockDetail());
            CommonUtil.checkResult("兑换品库存明细变更类型", ChangeStockTypeEnum.ADD.name(), exchangeStockPageList.get(0).getExchangeType());
            CommonUtil.checkResult("兑换品库存明细变动原因", "管理员编辑库存", exchangeStockPageList.get(0).getChangeReason());
            //减少库存
            EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.MINUS.name()).num(1).id(specificationDetail.getLong("id")).goodsName(goodsName).type(CommodityTypeEnum.REAL.name()).build().invoke(visitor);
            Integer numThree = ExchangeCommoditySpecificationsListScene.builder().id(id).build().invoke(visitor).getJSONArray("specification_detail_list").getJSONObject(0).getInteger("num");
            CommonUtil.checkResult(firstSpecificationsName + "剩余库存", numTow - 1, numThree);
            //库存明细
            List<ExchangeStockPageBean> exchangeStockPageListTwo = util.toJavaObjectList(exchangeStockPageScene, ExchangeStockPageBean.class);
            CommonUtil.checkResult("兑换品库存明细列表数", exchangeStockPageList.size() + 1, exchangeStockPageListTwo.size());
            CommonUtil.checkResult("兑换品库存明细手机号", ALL_AUTHORITY.getPhone(), exchangeStockPageListTwo.get(0).getSalePhone());
            CommonUtil.checkResult("兑换品库存明细操作人", ALL_AUTHORITY.getName(), exchangeStockPageListTwo.get(0).getSaleName());
            CommonUtil.checkResult("兑换品库存明细库存明细", 1L, exchangeStockPageListTwo.get(0).getStockDetail());
            CommonUtil.checkResult("兑换品库存明细变更类型", ChangeStockTypeEnum.MINUS.name(), exchangeStockPageListTwo.get(0).getExchangeType());
            CommonUtil.checkResult("兑换品库存明细变动原因", "管理员编辑库存", exchangeStockPageListTwo.get(0).getChangeReason());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--修改实体积分兑换库存");
        }
    }

    //ok
    @Test(description = "积分兑换--修改实体积分兑换库存，异常情况")
    public void integralExchange_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangePageScene = ExchangePageScene.builder().exchangeType(CommodityTypeEnum.REAL.name()).build();
            List<ExchangePage> exchangePageList = util.toJavaObjectList(exchangePageScene, ExchangePage.class);
            Long id = exchangePageList.stream().filter(e -> Integer.parseInt(e.getExchangedAndSurplus().split("/")[1]) < 10).map(ExchangePage::getId).findFirst().orElse(util.createExchangeRealGoods().getId());
            String goodsName = ExchangeGoodsStockScene.builder().id(id).build().invoke(visitor).getString("goods_name");
            JSONObject specificationDetail = ExchangeCommoditySpecificationsListScene.builder().id(id).build().invoke(visitor).getJSONArray("specification_detail_list").getJSONObject(0);
            Integer[] integers = {1000000000, null, -1, 0};
            Arrays.stream(integers).forEach(num -> {
                String addMessage = EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.ADD.name()).num(num).id(specificationDetail.getLong("id"))
                        .goodsName(goodsName).type(CommodityTypeEnum.REAL.name()).build().invoke(visitor, false).getString("message");
                String err = num == null ? "改变库存数量不能为空" : num == 1000000000 ? "超出最大库存限额，请重新输入！" : "库存变动数量需大于等于1";
                CommonUtil.checkResult(goodsName + "修改库存" + num, err, addMessage);
                CommonUtil.logger(num);
                String minusMessage = EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.MINUS.name()).num(num).id(specificationDetail.getLong("id"))
                        .goodsName(goodsName).type(CommodityTypeEnum.REAL.name()).build().invoke(visitor, false).getString("message");
                String errTwo = num == null ? "改变库存数量不能为空" : num == 1000000000 ? "减少库存量需小于等于当前库存量" : "库存变动数量需大于等于1";
                CommonUtil.checkResult(goodsName + "修改库存" + num, errTwo, minusMessage);
                CommonUtil.logger(num);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--修改实体积分兑换库存，异常情况");
        }
    }

    //bug
    @Test(description = "积分兑换--修改实体积分兑换库存，减少大于当前库存的数", enabled = false)
    public void integralExchange_system_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangePageScene = ExchangePageScene.builder().exchangeType(CommodityTypeEnum.REAL.name()).build();
            List<ExchangePage> exchangePageList = util.toJavaObjectList(exchangePageScene, ExchangePage.class);
            Long id = exchangePageList.stream().map(ExchangePage::getId).findFirst().orElse(0L);
            String goodsName = ExchangeGoodsStockScene.builder().id(id).build().invoke(visitor).getString("goods_name");
            JSONObject specificationDetail = ExchangeCommoditySpecificationsListScene.builder().id(id).build().invoke(visitor).getJSONArray("specification_detail_list").getJSONObject(0);
            String message = EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.MINUS.name()).num(specificationDetail.getInteger("num") + 1).id(specificationDetail.getLong("id")).goodsName(goodsName).type(CommodityTypeEnum.REAL.name()).build().invoke(visitor, false).getString("message");
            String err = "减少库存量需小于等于当前库存量";
            CommonUtil.checkResult(goodsName + "减少库存" + specificationDetail.getInteger("num") + 1, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--修改实体积分兑换库存，减少大于当前库存的数");
        }
    }

    //ok
    @Test(description = "积分兑换--修改虚拟积分兑换库存，增加大于卡券库存的数")
    public void integralExchange_system_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangePageScene = ExchangePageScene.builder().exchangeType(CommodityTypeEnum.FICTITIOUS.name()).build();
            List<ExchangePage> exchangePageList = util.toJavaObjectList(exchangePageScene, ExchangePage.class);
            Long id = exchangePageList.stream().map(ExchangePage::getId).findFirst().orElse(0L);
            JSONObject exchangeGoodsStockResponse = ExchangeGoodsStockScene.builder().id(id).build().invoke(visitor);
            Integer goodsStock = exchangeGoodsStockResponse.getInteger("goods_stock");
            String goodsName = exchangeGoodsStockResponse.getString("goods_name");
            Long surplusInventory = util.getVoucherPage(goodsName).getSurplusInventory();
            long num = surplusInventory >= goodsStock ? surplusInventory - goodsStock + 1 : goodsStock - surplusInventory;
            //增加库存
            String message = EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.ADD.name()).num((int) num).id(id)
                    .goodsName(goodsName).type(CommodityTypeEnum.FICTITIOUS.name()).build().invoke(visitor, false).getString("message");
            String err = "积分商品库存不能超过商品库存";
            CommonUtil.checkResult(goodsName + "增加库存 " + num, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--修改虚拟积分兑换库存，增加大于卡券库存的数");
        }
    }

    //ok
    @Test(description = "积分兑换--修改虚拟积分兑换库存，减少大于当前库存的数")
    public void integralExchange_system_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangePageScene = ExchangePageScene.builder().exchangeType(CommodityTypeEnum.FICTITIOUS.name()).build();
            List<ExchangePage> exchangePageList = util.toJavaObjectList(exchangePageScene, ExchangePage.class);
            Long id = exchangePageList.stream().map(ExchangePage::getId).findFirst().orElse(util.createExchangeFictitiousGoods(
                    new VoucherGenerator.Builder().status(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId()).getId());
            JSONObject exchangeGoodsStockResponse = ExchangeGoodsStockScene.builder().id(id).build().invoke(visitor);
            Integer goodsStock = exchangeGoodsStockResponse.getInteger("goods_stock");
            String goodsName = exchangeGoodsStockResponse.getString("goods_name");
            int num = goodsStock + 1;
            //增加库存
            String message = EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.MINUS.name()).num(num).id(id)
                    .goodsName(goodsName).type(CommodityTypeEnum.FICTITIOUS.name()).build().invoke(visitor, false).getString("message");
            String err = "减少库存量需小于等于当前库存量";
            CommonUtil.checkResult(goodsName + "减少库存 " + num, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--修改虚拟积分兑换库存，减少大于当前库存的数");
        }
    }

    //ok
    @Test(description = "积分兑换--创建实体积分兑换->小程序兑换实体商品->发货->确认收货->取消")
    public void integralExchange_system_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangePageScene = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).exchangeType(CommodityTypeEnum.REAL.name()).build();
            ExchangePage exchangePage = util.toJavaObjectList(exchangePageScene, ExchangePage.class).stream().filter(e -> !e.getExchangedAndSurplus().split("/")[1].equals("0") && e.getExchangePrice() == 1).findFirst().orElse(util.createExchangeRealGoods());
            //不限兑换次数
            util.modifyExchangeGoodsLimit(exchangePage.getId(), exchangePage.getExchangeType(), false);
            List<Integer> exchangedAndSurplusList = Arrays.stream(exchangePage.getExchangedAndSurplus().split("/")).map(Integer::valueOf).collect(Collectors.toList());
            visitor.login(APPLET_USER_ONE.getToken());
            int score = AppletDetailScene.builder().build().invoke(visitor).getInteger("score");
            int integralRecordNum = util.getAppletIntegralRecordNum();
            int exchangeRecordNum = util.getAppletExchangeRecordNum();
            long specificationId = AppletCommodityDetailScene.builder().id(exchangePage.getId()).build().invoke(visitor).getJSONArray("specification_compose_list").getJSONObject(0).getLong("id");
            //获取邮寄信息
            AppletShippingAddress appletShippingAddress = AppletShippingAddressListScene.builder().build().invoke(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletShippingAddress.class)).collect(Collectors.toList()).get(0);
            //兑换积分
            AppletSubmitOrderScene.builder().commodityId(exchangePage.getId()).specificationId(specificationId).smsNotify(false).commodityNum(1).districtCode(appletShippingAddress.getDistrictCode()).address(appletShippingAddress.getAddress()).receiver(appletShippingAddress.getName()).receivePhone(appletShippingAddress.getPhone()).build().invoke(visitor);
            user.loginPc(ALL_AUTHORITY);
            ExchangePage newExchangePage = util.getExchangePage(exchangePage.getId());
            List<Integer> newExchangedAndSurplusList = Arrays.stream(newExchangePage.getExchangedAndSurplus().split("/")).map(Integer::valueOf).collect(Collectors.toList());
            CommonUtil.checkResult(newExchangePage.getGoodsName() + "已兑换数量", exchangedAndSurplusList.get(0) + 1, newExchangedAndSurplusList.get(0));
            CommonUtil.checkResult(newExchangePage.getGoodsName() + "剩余数量", exchangedAndSurplusList.get(1) - 1, newExchangedAndSurplusList.get(1));
            //小程序积分明细列表数量
            visitor.login(APPLET_USER_ONE.getToken());
            int newIntegralRecordNum = util.getAppletIntegralRecordNum();
            int newExchangeRecordNum = util.getAppletExchangeRecordNum();
            CommonUtil.checkResult("小程序积分明细页数量", integralRecordNum + 1, newIntegralRecordNum);
            CommonUtil.checkResult("小程序积分订单页兑换商品数量", exchangeRecordNum + 1, newExchangeRecordNum);
            //小程序积分明细
            AppletIntegralRecord appletIntegralRecord = util.getAppletIntegralRecordList().get(0);
            CommonUtil.checkResult("小程序积分明细页积分数", exchangePage.getExchangePrice(), Integer.valueOf(appletIntegralRecord.getIntegral()));
            CommonUtil.checkResult("小程序积分明细详情", "使用" + exchangePage.getExchangePrice() + "银元兑换了【" + exchangePage.getGoodsName() + "】", appletIntegralRecord.getName());
            CommonUtil.checkResult("小程序积分明细兑换类型", ChangeStockTypeEnum.MINUS.name(), appletIntegralRecord.getChangeType());
            //小程序订单状态
            AppletExchangeRecord appletExchangeRecord = util.getAppletExchangeRecordList().get(0);
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.WAITING.getName(), appletExchangeRecord.getExchangeStatusName());
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.WAITING.name(), appletExchangeRecord.getExchangeStatus());
            CommonUtil.checkResult("小程序商品id", exchangePage.getId(), appletExchangeRecord.getCommodityId());
            CommonUtil.checkResult("小程序商品数量", 1, appletExchangeRecord.getNum());
            CommonUtil.checkResult("小程序商品积分", exchangePage.getExchangePrice(), appletExchangeRecord.getIntegral());
            //小程序我的页
            int scoreTwo = AppletDetailScene.builder().build().invoke(visitor).getInteger("score");
            CommonUtil.checkResult("小程序积分总数", score - appletExchangeRecord.getIntegral(), scoreTwo);
            //积分订单页
            user.loginPc(ALL_AUTHORITY);
            IScene exchangeOrderScene = ExchangeOrderScene.builder().build();
            ExchangeOrderBean exchangeOrder = util.toJavaObjectList(exchangeOrderScene, ExchangeOrderBean.class).get(0);
            CommonUtil.checkResult("pc积分订单页订单号", appletExchangeRecord.getOrderId(), exchangeOrder.getOrderId());
            CommonUtil.checkResult("pc积分订单页订单名称", appletExchangeRecord.getName(), exchangeOrder.getGoodsName());
            CommonUtil.checkResult("pc积分订单页订单分类", CommodityTypeEnum.REAL.getName(), exchangeOrder.getGoodsType());
            CommonUtil.checkResult("pc积分订单页订单状态", appletExchangeRecord.getExchangeStatus(), exchangeOrder.getOrderStatus());
            CommonUtil.checkResult("pc积分订单页订单状态", appletExchangeRecord.getExchangeStatusName(), exchangeOrder.getOrderStatusName());
            //发货
            ConfirmShipmentScene.builder().id(exchangeOrder.getId()).oddNumbers("1122").build().invoke(visitor);
            //pc状态2
            ExchangeOrderBean exchangeOrderTwo = util.toJavaObjectList(exchangeOrderScene, ExchangeOrderBean.class).get(0);
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.SEND.name(), exchangeOrderTwo.getOrderStatus());
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.SEND.getName(), exchangeOrderTwo.getOrderStatusName());
            //小程序状态2
            visitor.login(APPLET_USER_ONE.getToken());
            AppletExchangeRecord appletExchangeRecordTwo = util.getAppletExchangeRecordList().get(0);
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.SEND.name(), appletExchangeRecordTwo.getExchangeStatus());
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.SEND.getName(), appletExchangeRecordTwo.getExchangeStatusName());
            //确认收货
            AppletConfirmReceiveScene.builder().id(exchangeOrder.getId()).build().invoke(visitor);
            //小程序状态3
            AppletExchangeRecord appletExchangeRecordThree = util.getAppletExchangeRecordList().get(0);
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.FINISHED.name(), appletExchangeRecordThree.getExchangeStatus());
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.FINISHED.getName(), appletExchangeRecordThree.getExchangeStatusName());
            JSONObject response = AppletExchangeRecordDetailScene.builder().id(exchangeOrder.getId()).build().invoke(visitor);
            CommonUtil.checkResult("小程序订单详情快递单号", "1122", response.getString("odd_numbers"));
            CommonUtil.checkResult("小程序订单详情订单状态", OrderStatusEnum.FINISHED.getName(), response.getString("exchange_status_name"));
            //pc状态3
            user.loginPc(ALL_AUTHORITY);
            ExchangeOrderBean exchangeOrderThree = util.toJavaObjectList(exchangeOrderScene, ExchangeOrderBean.class).get(0);
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.FINISHED.name(), exchangeOrderThree.getOrderStatus());
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.FINISHED.getName(), exchangeOrderThree.getOrderStatusName());
            CommonUtil.checkResult("pc积分订单页订单确认发货", false, exchangeOrderThree.getIsConfirmShipment());
            //积分明细页
            IScene exchangeDetailedScene = ExchangeDetailedScene.builder().build();
            ExchangeDetailed exchangeDetailed = util.toJavaObjectList(exchangeDetailedScene, ExchangeDetailed.class).get(0);
            CommonUtil.checkResult("pc积分明细页订单号", appletExchangeRecord.getOrderId(), exchangeDetailed.getOrderCode());
            CommonUtil.checkResult("pc积分明细页明细", appletExchangeRecord.getIntegral(), exchangeDetailed.getStockDetail());
            CommonUtil.checkResult("pc积分明细页明细", ChangeStockTypeEnum.MINUS.name(), exchangeDetailed.getExchangeType());
            CommonUtil.checkResult("pc积分明细页兑换类型", ChangeStockTypeEnum.MINUS.getDescription(), exchangeDetailed.getExchangeTypeName());
            CommonUtil.checkResult("pc积分明细页详情", "使用" + appletExchangeRecord.getIntegral() + "银元兑换了【" + appletExchangeRecord.getName() + "】", exchangeDetailed.getChangeReason());
            //取消
            CancelOrderScene.builder().id(exchangeOrder.getId()).build().invoke(visitor);
            //pc状态4
            ExchangeOrderBean exchangeOrderFour = util.toJavaObjectList(exchangeOrderScene, ExchangeOrderBean.class).get(0);
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.CANCELED.name(), exchangeOrderFour.getOrderStatus());
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.CANCELED.getName(), exchangeOrderFour.getOrderStatusName());
            CommonUtil.checkResult("pc积分订单页订单确认发货", false, exchangeOrderFour.getIsConfirmShipment());
            CommonUtil.checkResult("pc积分订单页订单确认取消", false, exchangeOrderFour.getIsCancel());
            //积分明细页2
            ExchangeDetailed exchangeDetailedTwo = util.toJavaObjectList(exchangeDetailedScene, ExchangeDetailed.class).get(0);
            CommonUtil.checkResult("pc积分明细页订单号", appletExchangeRecord.getOrderId(), exchangeDetailedTwo.getOrderCode());
            CommonUtil.checkResult("pc积分明细页明细", appletExchangeRecord.getIntegral(), exchangeDetailedTwo.getStockDetail());
            CommonUtil.checkResult("pc积分明细页详情", "取消购买【" + appletExchangeRecord.getName() + "】订单", exchangeDetailedTwo.getChangeReason());
            CommonUtil.checkResult("pc积分明细页明细", ChangeStockTypeEnum.ADD.name(), exchangeDetailedTwo.getExchangeType());
            CommonUtil.checkResult("pc积分明细页兑换类型", ChangeStockTypeEnum.ADD.getDescription(), exchangeDetailedTwo.getExchangeTypeName());
            //小程序状态4
            visitor.login(APPLET_USER_ONE.getToken());
            AppletExchangeRecord appletExchangeRecordFour = util.getAppletExchangeRecordList().get(0);
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.CANCELED.name(), appletExchangeRecordFour.getExchangeStatus());
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.CANCELED.getName(), appletExchangeRecordFour.getExchangeStatusName());
            //小程序积分明细2
            AppletIntegralRecord appletIntegralRecordTwo = util.getAppletIntegralRecordList().get(0);
            CommonUtil.checkResult("小程序积分明细页积分数", exchangePage.getExchangePrice(), Integer.parseInt(appletIntegralRecordTwo.getIntegral()));
            CommonUtil.checkResult("小程序积分明细详情", "取消购买【" + appletExchangeRecord.getName() + "】订单", appletIntegralRecordTwo.getName());
            CommonUtil.checkResult("小程序积分明细兑换类型", ChangeStockTypeEnum.ADD.name(), appletIntegralRecordTwo.getChangeType());
            //小程序我的页
            int scoreThree = AppletDetailScene.builder().build().invoke(visitor).getInteger("score");
            CommonUtil.checkResult("小程序积分总数", scoreTwo + exchangePage.getExchangePrice(), scoreThree);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--创建实体积分兑换->小程序兑换实体商品->发货->确认收货->取消");
        }
    }

    //ok
    @Test(description = "积分兑换--创建实体积分兑换->小程序兑换虚拟商品")
    public void integralExchange_system_16() {
        logger.logCaseStart(caseResult.getCaseName());
        Long exchangeGoodsId = null;
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            ExchangePage exchangePage = util.createExchangeFictitiousGoods(voucherId);
            exchangeGoodsId = exchangePage.getId();
            //不限兑换次数
            util.modifyExchangeGoodsLimit(exchangeGoodsId, exchangePage.getExchangeType(), false);
            List<Integer> exchangedAndSurplusList = Arrays.stream(exchangePage.getExchangedAndSurplus().split("/")).map(Integer::valueOf).collect(Collectors.toList());
            visitor.login(APPLET_USER_ONE.getToken());
            int score = AppletDetailScene.builder().build().invoke(visitor).getInteger("score");
            int integralRecordNum = util.getAppletIntegralRecordNum();
            int exchangeRecordNum = util.getAppletExchangeRecordNum();
            //兑换积分
            AppletIntegralExchangeScene.builder().id(exchangeGoodsId).build().invoke(visitor);
            user.loginPc(ALL_AUTHORITY);
            ExchangePage newExchangePage = util.getExchangePage(exchangeGoodsId);
            List<Integer> newExchangedAndSurplusList = Arrays.stream(newExchangePage.getExchangedAndSurplus().split("/")).map(Integer::valueOf).collect(Collectors.toList());
            CommonUtil.checkResult(newExchangePage.getGoodsName() + "已兑换数量", exchangedAndSurplusList.get(0) + 1, newExchangedAndSurplusList.get(0));
            CommonUtil.checkResult(newExchangePage.getGoodsName() + "剩余数量", exchangedAndSurplusList.get(1) - 1, newExchangedAndSurplusList.get(1));
            //小程序积分明细列表数量
            visitor.login(APPLET_USER_ONE.getToken());
            int newIntegralRecordNum = util.getAppletIntegralRecordNum();
            int newExchangeRecordNum = util.getAppletExchangeRecordNum();
            CommonUtil.checkResult("小程序积分明细页数量", integralRecordNum + 1, newIntegralRecordNum);
            CommonUtil.checkResult("小程序积分订单页兑换商品数量", exchangeRecordNum + 1, newExchangeRecordNum);
            //小程序积分明细
            AppletIntegralRecord appletIntegralRecord = util.getAppletIntegralRecordList().get(0);
            CommonUtil.checkResult("小程序积分明细页积分数", exchangePage.getExchangePrice(), Integer.valueOf(appletIntegralRecord.getIntegral()));
            CommonUtil.checkResult("小程序积分明细详情", "使用" + exchangePage.getExchangePrice() + "银元兑换了【" + exchangePage.getGoodsName() + "】", appletIntegralRecord.getName());
            CommonUtil.checkResult("小程序积分明细兑换类型", ChangeStockTypeEnum.MINUS.name(), appletIntegralRecord.getChangeType());
            //小程序订单状态
            AppletExchangeRecord appletExchangeRecord = util.getAppletExchangeRecordList().get(0);
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.FINISHED.getName(), appletExchangeRecord.getExchangeStatusName());
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.FINISHED.name(), appletExchangeRecord.getExchangeStatus());
            CommonUtil.checkResult("小程序商品id", exchangePage.getId(), appletExchangeRecord.getCommodityId());
            CommonUtil.checkResult("小程序商品数量", 1, appletExchangeRecord.getNum());
            CommonUtil.checkResult("小程序商品积分", exchangePage.getExchangePrice(), appletExchangeRecord.getIntegral());
            //小程序我的页
            int scoreTwo = AppletDetailScene.builder().build().invoke(visitor).getInteger("score");
            CommonUtil.checkResult("小程序积分总数", score - appletExchangeRecord.getIntegral(), scoreTwo);
            //积分订单页
            user.loginPc(ALL_AUTHORITY);
            IScene exchangeOrderScene = ExchangeOrderScene.builder().build();
            ExchangeOrderBean exchangeOrder = util.toJavaObjectList(exchangeOrderScene, ExchangeOrderBean.class).get(0);
            CommonUtil.checkResult("pc积分订单页订单号", appletExchangeRecord.getOrderId(), exchangeOrder.getOrderId());
            CommonUtil.checkResult("pc积分订单页订单名称", appletExchangeRecord.getName(), exchangeOrder.getGoodsName());
            CommonUtil.checkResult("pc积分订单页订单分类", CommodityTypeEnum.FICTITIOUS.getName(), exchangeOrder.getGoodsType());
            CommonUtil.checkResult("pc积分订单页订单状态", appletExchangeRecord.getExchangeStatus(), exchangeOrder.getOrderStatus());
            CommonUtil.checkResult("pc积分订单页订单状态", appletExchangeRecord.getExchangeStatusName(), exchangeOrder.getOrderStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            ChangeSwitchStatusScene.builder().id(exchangeGoodsId).status(false).build().invoke(visitor);
            DeleteExchangeGoodsScene.builder().id(exchangeGoodsId).build().invoke(visitor);
            saveData("积分兑换--创建实体积分兑换->小程序兑换虚拟商品");
        }
    }

    //ok
    @Test(description = "积分兑换--置顶某一积分兑换，小程序人气推荐置顶该兑换，再置顶一个积分兑换，人气推荐刷新第一位，原第一位降到第二位")
    public void integralExchange_system_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).build().invoke(visitor);
            Long id = response.getJSONArray("list").getJSONObject(0).getLong("id");
            Preconditions.checkArgument(id != null, "不存在积分兑换商品");
            //置顶
            MakeTopScene.builder().id(id).build().invoke(visitor);
            //小程序人气推荐
            visitor.login(APPLET_USER_ONE.getToken());
            JSONArray list = AppletHomePageScene.builder().build().invoke(visitor).getJSONArray("recommend_list");
            Long appletId = list.getJSONObject(0).getLong("id");
            CommonUtil.checkResult("人气推荐第一项", id, appletId);
            user.loginPc(ALL_AUTHORITY);
            //再置顶第二个积分兑换
            JSONObject response1 = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).build().invoke(visitor);
            Long id1 = response1.getJSONArray("list").getJSONObject(1).getLong("id");
            Preconditions.checkArgument(id1 != null, "不存在积分兑换商品");
            //置顶
            MakeTopScene.builder().id(id1).build().invoke(visitor);
            visitor.login(APPLET_USER_ONE.getToken());
            JSONArray list1 = AppletHomePageScene.builder().build().invoke(visitor).getJSONArray("recommend_list");
            Long appletId1 = list1.getJSONObject(0).getLong("id");
            Long appletId2 = list1.getJSONObject(1).getLong("id");
            CommonUtil.checkResult("人气推荐第一项", id1, appletId1);
            CommonUtil.checkResult("人气推荐第二项", id, appletId2);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--置顶某一积分兑换，小程序人气推荐置顶该兑换，再置顶一个积分兑换，人气推荐刷新第一位，原第一位降到第二位");
        }
    }

    //ok
    @Test(description = "积分兑换--删除未关闭的积分兑换，失败")
    public void integralExchange_system_18() {
        logger.logCaseStart(caseResult.getCaseName());
        Long id = null;
        try {
            //创建积分兑换
            ExchangePage exchangePage = util.createExchangeRealGoods(1);
            id = exchangePage.getId();
            String message = DeleteExchangeGoodsScene.builder().id(id).build().invoke(visitor, false).getString("message");
            String err = "积分商品正在售卖中，不可删除";
            CommonUtil.checkResult("置顶某一删除未关闭的积分兑换", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            ChangeSwitchStatusScene.builder().id(id).status(false).build().invoke(visitor);
            DeleteExchangeGoodsScene.builder().id(id).build().invoke(visitor);
            saveData("积分兑换--删除未关闭的积分兑换，失败");
        }
    }

    //ok
    @Test(description = "积分兑换--按积分兑换品筛选，结构名称都包含所搜内容")
    public void integralExchange_system_19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ExchangePageScene.builder().build();
            ExchangePageBean exchangePageBean = util.toJavaObjectList(scene, ExchangePageBean.class).get(0);
            IScene exchangePageScene = ExchangePageScene.builder().exchangeGoods(exchangePageBean.getGoodsName()).build();
            List<ExchangePageBean> exchangePageBeanList = util.toJavaObjectList(exchangePageScene, ExchangePageBean.class);
            exchangePageBeanList.forEach(e -> Preconditions.checkArgument(e.getGoodsName().contains(exchangePageBean.getGoodsName()), e.getGoodsName() + "不包含" + exchangePageBean.getGoodsName()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--按积分兑换品筛选，结构名称都包含所搜内容");
        }
    }

    //ok
    @Test(description = "积分兑换--按积分兑换类型筛选")
    public void integralExchange_system_20() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(CommodityTypeEnum.values()).forEach(e -> {
                IScene scene = ExchangePageScene.builder().exchangeType(e.name()).build();
                List<ExchangePageBean> exchangePageBeanList = util.toJavaObjectList(scene, ExchangePageBean.class);
                exchangePageBeanList.forEach(exchangePageBean -> {
                    CommonUtil.checkResult(exchangePageBean.getGoodsName() + "的兑换类型", e.name(), exchangePageBean.getExchangeType());
                    CommonUtil.checkResult(exchangePageBean.getGoodsName() + "的兑换类型", e.getName(), exchangePageBean.getExchangeTypeName());
                });
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--按积分兑换类型筛选");
        }
    }

    //ok
    @Test(description = "PC【积分兑换】根据状态筛选")
    public void integralExchange_system_21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(IntegralExchangeStatusEnum.values()).forEach(e -> {
                IScene scene = ExchangePageScene.builder().status(e.name()).build();
                List<ExchangePageBean> exchangePageBeanList = util.toJavaObjectList(scene, ExchangePageBean.class);
                exchangePageBeanList.forEach(exchangePageBean -> {
                    CommonUtil.checkResult(exchangePageBean.getGoodsName() + "的状态", e.name(), exchangePageBean.getStatus());
                    CommonUtil.checkResult(exchangePageBean.getGoodsName() + "的状态", e.getDesc(), exchangePageBean.getStatusName());
                });
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分兑换】根据状态筛选");
        }
    }

    //ok
    @Test(description = "PC【积分明细】根据存在的兑换客户全称筛选")
    public void integralDetailed_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ExchangeDetailedScene.builder().build();
            ExchangeDetailedBean exchangeDetailedBead = util.toJavaObjectList(scene, ExchangeDetailedBean.class).get(0);
            IScene exchangeDetailedScene = ExchangeDetailedScene.builder().exchangeCustomerName(exchangeDetailedBead.getExchangeCustomerName()).build();
            List<ExchangeDetailedBean> exchangeDetailedBeanList = util.toJavaObjectList(exchangeDetailedScene, ExchangeDetailedBean.class);
            exchangeDetailedBeanList.forEach(e -> Preconditions.checkArgument(exchangeDetailedBead.getExchangeCustomerName().contains(e.getExchangeCustomerName()), "按" + exchangeDetailedBead.getExchangeCustomerName() + "搜索，结果不包含" + e.getExchangeCustomerName()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分明细】根据存在的兑换客户全称筛选");
        }
    }

    //ok
    @Test(description = "PC【积分明细】根据存在的兑换类型筛选")
    public void integralDetailed_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(ChangeStockTypeEnum.values()).forEach(e -> {
                IScene scene = ExchangeDetailedScene.builder().exchangeType(e.name()).build();
                List<ExchangeDetailedBean> exchangeDetailedBeanList = util.toJavaObjectList(scene, ExchangeDetailedBean.class);
                exchangeDetailedBeanList.forEach(exchangeDetailedBean -> {
                    CommonUtil.checkResult(exchangeDetailedBean.getOperateTime() + "的兑换明细的兑换类型", e.name(), exchangeDetailedBean.getExchangeType());
                    CommonUtil.checkResult(exchangeDetailedBean.getOperateTime() + "的兑换明细的兑换类型", e.getDescription(), exchangeDetailedBean.getExchangeTypeName());
                });
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分明细】根据存在的兑换类型筛选");
        }
    }

    //ok
    @Test(description = "PC【积分明细】根据兑换时间筛选")
    public void integralDetailed_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -10), "yyyy-MM-dd");
            String endTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
            String time = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 1), "yyyy-MM-dd");
            IScene scene = ExchangeDetailedScene.builder().exchangeStartTime(startTime).exchangeEndTime(endTime).build();
            List<ExchangeDetailedBean> exchangeDetailedBeanList = util.toJavaObjectList(scene, ExchangeDetailedBean.class);
            exchangeDetailedBeanList.forEach(e -> {
                String operateTime = DateTimeUtil.dateToStamp(e.getOperateTime());
                Preconditions.checkArgument(operateTime.compareTo(DateTimeUtil.dateToStamp(startTime, "yyyy-MM-dd")) >= 0
                        && operateTime.compareTo(DateTimeUtil.dateToStamp(time, "yyyy-MM-dd")) <= 0, "搜索" + startTime + "~" + endTime + "，结果包含" + e.getOperateTime());
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分明细】根据兑换时间筛选");
        }
    }

    //ok
    @Test(description = "PC【积分明细】列表展示项校验")
    public void integralDetailed_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ExchangeDetailedScene.builder().size(SIZE).build().invoke(visitor).getJSONArray("list");
            list.stream().map(e -> (JSONObject) e).forEach(obj -> {
                Preconditions.checkArgument(obj.containsKey("exchange_customer_name"), obj.getString("phone") + "未展示客户名称");
                Preconditions.checkArgument(obj.containsKey("phone"), obj.getString("exchange_customer_name") + "未展示客户手机号");
                Preconditions.checkArgument(obj.containsKey("exchange_type"), obj.getString("exchange_customer_name") + "未展示兑换类型");
                Preconditions.checkArgument(obj.containsKey("stock_detail"), obj.getString("exchange_customer_name") + "未展示库存明细");
                Preconditions.checkArgument(obj.containsKey("operate_time"), obj.getString("exchange_customer_name") + "未展示操作时间");
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分明细】列表展示项校验");
        }
    }

    //ok
    @Test(description = "PC【积分明细】列表根据兑换时间倒序排列")
    public void integralDetailed_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ExchangeDetailedScene.builder().build();
            List<ExchangeDetailedBean> exchangeDetailedBeanList = util.toJavaObjectList(scene, ExchangeDetailedBean.class);
            List<String> timeList = exchangeDetailedBeanList.stream().map(ExchangeDetailedBean::getOperateTime).collect(Collectors.toList());
            for (int i = 0; i < timeList.size() - 1; i++) {
                Preconditions.checkArgument(DateTimeUtil.dateToStamp(timeList.get(i)).compareTo(DateTimeUtil.dateToStamp(timeList.get(i + 1))) >= 0, "时间" + timeList.get(i) + "排在" + timeList.get(i + 1) + "后面");
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分明细】列表根据兑换时间倒序排列");
        }
    }

    //ok
    @Test(description = "积分订单--订单明细--实付总积分=商品积分*兑换数量")
    public void integralOrder_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangeOrderScene = ExchangeOrderScene.builder().build();
            List<JSONObject> jsonObjectList = util.toJavaObjectList(exchangeOrderScene, JSONObject.class);
            jsonObjectList.forEach(e -> {
                Long id = e.getLong("id");
                IScene orderDetailScene = OrderDetailScene.builder().id(id).build();
                JSONObject response = visitor.invokeApi(orderDetailScene);
                int integralNum = response.getInteger("integral_num");
                JSONObject detailedList = response.getJSONArray("detailed_list").getJSONObject(0);
                int commodityIntegral = detailedList.getInteger("commodity_integral");
                int commodityNum = detailedList.getInteger("commodity_num");
                CommonUtil.checkResult(e.getString("order_id") + " 实付总积分", commodityNum * commodityIntegral, integralNum);
                CommonUtil.logger(e.getString("order_id"));
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分订单--订单明细--实付总积分=商品积分*兑换数量");
        }
    }

    //ok
    @Test(description = "PC【积分订单】列表根据兑换时间倒序排列")
    public void integralOrder_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ExchangeOrderScene.builder().build();
            List<String> timeList = util.toJavaObjectList(scene, ExchangeOrderBean.class).stream().map(ExchangeOrderBean::getExchangeTime).collect(Collectors.toList());
            for (int i = 0; i < timeList.size() - 1; i++) {
                Preconditions.checkArgument(DateTimeUtil.dateToStamp(timeList.get(i)).compareTo(DateTimeUtil.dateToStamp(timeList.get(i + 1))) >= 0, "时间" + timeList.get(i) + "排在" + timeList.get(i + 1) + "后面");
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】列表根据兑换时间倒序排列");
        }
    }

    //ok
    @Test(description = "PC【积分订单】根据订单ID筛选")
    public void integralOrder_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ExchangeOrderScene.builder().build();
            ExchangeOrderBean exchangeOrderBean = util.toJavaObjectList(scene, ExchangeOrderBean.class).get(0);
            String orderId = exchangeOrderBean.getOrderId().substring(0, 3);
            IScene exchangeOrderScene = ExchangeOrderScene.builder().orderId(orderId).build();
            List<ExchangeOrderBean> exchangeOrderBeanList = util.toJavaObjectList(exchangeOrderScene, ExchangeOrderBean.class);
            exchangeOrderBeanList.forEach(e -> Preconditions.checkArgument(e.getOrderId().contains(orderId), "按照" + orderId + "搜索出异常结果：" + e.getOrderId()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】根据订单ID筛选");
        }
    }

    //ok
    @Test(description = "PC【积分订单】根据会员名称筛选")
    public void integralOrder_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ExchangeOrderScene.builder().build();
            ExchangeOrderBean exchangeOrderBean = util.toJavaObjectList(scene, ExchangeOrderBean.class).get(0);
            String customerName = exchangeOrderBean.getMemberName();
            IScene exchangeOrderScene = ExchangeOrderScene.builder().member(customerName).build();
            List<ExchangeOrderBean> exchangeOrderBeanList = util.toJavaObjectList(exchangeOrderScene, ExchangeOrderBean.class);
            exchangeOrderBeanList.forEach(e -> Preconditions.checkArgument(e.getMemberName().contains(customerName), "按照" + customerName + "搜索出异常结果：" + e.getMemberName()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】根据会员名称筛选");
        }
    }

    //ok
    @Test(description = "PC【积分订单】根据商品名称筛选")
    public void integralOrder_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = ExchangeOrderScene.builder().build();
            ExchangeOrderBean exchangeOrderBean = util.toJavaObjectList(scene, ExchangeOrderBean.class).get(0);
            String goodsName = exchangeOrderBean.getGoodsName().substring(0, 3);
            IScene exchangeOrderScene = ExchangeOrderScene.builder().goodsName(goodsName).build();
            List<ExchangeOrderBean> exchangeOrderBeanList = util.toJavaObjectList(exchangeOrderScene, ExchangeOrderBean.class);
            exchangeOrderBeanList.forEach(e -> Preconditions.checkArgument(e.getGoodsName().contains(goodsName), "按照" + goodsName + "搜索出异常结果：" + e.getGoodsName()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】根据商品名称筛选");
        }
    }

    //ok
    @Test(description = "PC【积分订单】根据兑换时间筛选")
    public void integralOrder_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -10), "yyyy-MM-dd");
            String endTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
            String time = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 1), "yyyy-MM-dd");

            IScene scene = ExchangeOrderScene.builder().startTime(startTime).endTime(endTime).build();
            List<ExchangeOrderBean> exchangeOrderBeanList = util.toJavaObjectList(scene, ExchangeOrderBean.class);
            exchangeOrderBeanList.forEach(e -> {
                String exchangeTime = DateTimeUtil.dateToStamp(e.getExchangeTime());
                Preconditions.checkArgument(exchangeTime.compareTo(DateTimeUtil.dateToStamp(startTime, "yyyy-MM-dd")) >= 0
                        && exchangeTime.compareTo(DateTimeUtil.dateToStamp(time, "yyyy-MM-dd")) <= 0, "搜索" + startTime + "~" + endTime + "，结果包含" + e.getExchangeTime());
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】根据兑换时间筛选");
        }
    }

    //ok
    @Test(description = "PC【积分订单】根据状态筛选")
    public void integralOrder_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(OrderStatusEnum.values()).forEach(e -> {
                IScene scene = ExchangeOrderScene.builder().orderStatus(e.name()).build();
                List<ExchangeOrderBean> exchangeOrderBeanList = util.toJavaObjectList(scene, ExchangeOrderBean.class);
                exchangeOrderBeanList.forEach(exchangeOrderBean -> {
                    CommonUtil.checkResult(exchangeOrderBean.getGoodsName() + "的订单状态", e.name(), exchangeOrderBean.getOrderStatus());
                    CommonUtil.checkResult(exchangeOrderBean.getGoodsName() + "的订单状态", e.getName(), exchangeOrderBean.getOrderStatusName());
                });
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】根据状态筛选");
        }
    }

    //bug 积分数对不上
    @Test(description = "积分明细--各个积分规则所得积分相加=此规则的已发放积分", enabled = false)
    public void integralRule_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangeDetailedScene = ExchangeDetailedScene.builder().build();
            List<ExchangeDetailed> exchangeDetailedList = util.toJavaObjectList(exchangeDetailedScene, ExchangeDetailed.class);
            Long stockSum = exchangeDetailedList.stream().filter(e -> e.getChangeReason() != null && e.getChangeReason().contains("签到获得")).mapToLong(ExchangeDetailed::getStockDetail).sum();
            int allSend = IntegralExchangeRulesScene.builder().build().invoke(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e)
                    .filter(e -> e.getString("rule_name").equals("签到")).map(e -> e.getInteger("all_send")).findFirst().orElse(0);
            CommonUtil.checkResultPlus("签到规则已发放积分", allSend, "积分明细签到获得积分总和", stockSum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分明细--各个积分规则所得积分相加=此规则的已发放积分");
        }
    }
}
