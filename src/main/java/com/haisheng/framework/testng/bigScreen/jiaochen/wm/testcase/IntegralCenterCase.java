package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletExchangeRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletIntegralRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletShippingAddress;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.ChangeStockTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.CommodityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralExchangeStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.OrderStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.Integralmall.GoodsManagePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.EquityPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.ShareManagerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.SignInConfigPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 积分中心测试用例
 */
public class IntegralCenterCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_DAILY;
    private static final EnumAccount ADMINISTRATOR = EnumAccount.WINSENSE_LAB_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_DAILY;
    //访问者
    public Visitor visitor = new Visitor(product);
    //登录工具
    public UserUtil user = new UserUtil(visitor);
    //封装方法
    public SupporterUtil util = new SupporterUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.product = product.getAbbreviation();
        commonConfig.referer = product.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = product.getShopId();
        commonConfig.roleId = product.getRoleId();
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
        user.loginPc(ADMINISTRATOR);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    //ok
    @Test(description = "积分兑换--库存详情--当前库存=兑换品库存明细加和")
    public void IntegralExchange_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //上线日期，之前得数据不做校验
            long time = Long.parseLong(DateTimeUtil.dateToStamp("2021-02-25", "yyyy-MM-dd"));
            IScene exchangePageScene = ExchangePageScene.builder().build();
            List<JSONObject> exchangePageList = util.collectBean(exchangePageScene, JSONObject.class);
            exchangePageList.stream().filter(e -> Long.parseLong(DateTimeUtil.dateToStamp(e.getString("begin_use_time"))) >= time).forEach(e -> {
                int id = e.getInteger("id");
                AtomicInteger s = new AtomicInteger();
                IScene exchangeStockScene = ExchangeStockScene.builder().id(String.valueOf(id)).build();
                JSONObject response = visitor.invokeApi(exchangeStockScene);
                String goodsName = response.getString("goods_name");
                int goodsStock = response.getInteger("goods_stock");
                IScene exchangeStockPageScene = ExchangeStockPageScene.builder().id(String.valueOf(id)).build();
                List<JSONObject> exchangeStockPageList = util.collectBean(exchangeStockPageScene, JSONObject.class);
                exchangeStockPageList.forEach(a -> {
                    String exchangeType = a.getString("exchange_type");
                    int stockDetail = a.getInteger("stock_detail");
                    s.set(exchangeType.equals("ADD") ? s.addAndGet(stockDetail) : s.addAndGet(-stockDetail));
                });
                CommonUtil.checkResult(goodsName + "当前库存", s.get(), goodsStock);
                CommonUtil.logger(goodsName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--库存详情--当前库存=兑换品库存明细加和");
        }
    }

    //ok
    @Test(description = "积分兑换--创建实体积分兑换")
    public void IntegralExchange_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            JSONArray specificationList = new JSONArray();
            JSONObject response = GoodsManagePageScene.builder().goodsStatus("UP").build().execute(visitor, true).getJSONArray("list").getJSONObject(0);
            long goodsId = response.getLong("id");
            String goodsName = response.getString("goods_name");
            JSONArray specificationDetailList = CommoditySpecificationsListScene.builder().id(goodsId).build().execute(visitor, true).getJSONArray("specification_detail_list");
            specificationDetailList.forEach(e -> {
                JSONObject specificationDetail = (JSONObject) e;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", specificationDetail.getInteger("id"));
                jsonObject.put("stock", 1);
                specificationList.add(jsonObject);
            });
            //创建积分兑换
            CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId).exchangePrice("1")
                    .isLimit(true).exchangePeopleNum("1").specificationList(specificationList).exchangeStartTime(exchangeStartTime)
                    .exchangeEndTime(exchangeEndTime).build().execute(visitor, true);
            IScene exchangePageScene = ExchangePageScene.builder().build();
            ExchangePage exchangePage = util.collectBean(exchangePageScene, ExchangePage.class).get(0);
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
            long id = ExchangePageScene.builder().build().execute(visitor, true).getJSONArray("list").getJSONObject(0).getLong("id");
            ExchangeSwitchStatusScene.builder().id(id).status(false).build().execute(visitor, true);
            DeleteExchangeGoodsScene.builder().id(id).build().execute(visitor, true);
            saveData("积分兑换--创建实体积分兑换");
        }
    }

    //ok
    @Test(description = "积分兑换--创建虚拟积分兑换")
    public void IntegralExchange_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            long exchangeNum = voucherPage.getSurplusInventory() == 1 ? 1 : voucherPage.getSurplusInventory() - 1;
            //创建积分兑换
            CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId).exchangePrice("1")
                    .exchangeNum(String.valueOf(exchangeNum)).isLimit(true).exchangePeopleNum("1").exchangeStartTime(exchangeStartTime)
                    .exchangeEndTime(exchangeEndTime).build().execute(visitor, true);
            IScene exchangePageScene = ExchangePageScene.builder().build();
            ExchangePage exchangePage = util.collectBean(exchangePageScene, ExchangePage.class).get(0);
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
            long id = ExchangePageScene.builder().build().execute(visitor, true).getJSONArray("list").getJSONObject(0).getLong("id");
            ExchangeSwitchStatusScene.builder().id(id).status(false).build().execute(visitor, true);
            DeleteExchangeGoodsScene.builder().id(id).build().execute(visitor, true);
            saveData("积分兑换--创建虚拟积分兑换");
        }
    }

    //ok
    @Test(description = "积分兑换--创建虚拟积分兑换，可兑换数量大于卡券库存")
    public void IntegralExchange_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            long exchangeNum = voucherPage.getSurplusInventory() + 1;
            //创建积分兑换
            String message = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId).exchangePrice("1")
                    .exchangeNum(String.valueOf(exchangeNum)).isLimit(true).exchangePeopleNum("1").exchangeStartTime(exchangeStartTime)
                    .exchangeEndTime(exchangeEndTime).build().execute(visitor, false).getString("message");
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
    public void IntegralExchange_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            long exchangeNum = voucherPage.getSurplusInventory() == 1 ? 1 : voucherPage.getSurplusInventory() - 1;
            String[] strings = {"1.11", null, ""};
            Arrays.stream(strings).forEach(exchangePrice -> {
                //创建积分兑换
                String message = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId).exchangePrice(exchangePrice)
                        .exchangeNum(String.valueOf(exchangeNum)).isLimit(true).exchangePeopleNum("1").exchangeStartTime(exchangeStartTime)
                        .exchangeEndTime(exchangeEndTime).build().execute(visitor, false).getString("message");
                String err = StringUtils.isEmpty(exchangePrice) ? "兑换价格不能为空" : "请求入参类型不正确";
                CommonUtil.checkResult("兑换价为" + exchangePrice, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--创建虚拟积分兑换，兑换价异常");
        }
    }

    //ok
    @Test(description = "积分兑换--创建实体积分兑换->小程序兑换实体商品->发货->确认收货->取消")
    public void IntegralExchange_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangePageScene = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).build();
            ExchangePage a = util.collectBean(exchangePageScene, ExchangePage.class).stream().filter(e -> e.getExchangeType().equals(CommodityTypeEnum.REAL.name()) && e.getExchangePrice() == 1).findFirst().orElse(null);
            ExchangePage exchangePage = a == null ? util.CreateExchangeGoods() : a;
            List<Integer> exchangedAndSurplusList = Arrays.stream(exchangePage.getExchangedAndSurplus().split("/")).map(Integer::valueOf).collect(Collectors.toList());
            user.loginApplet(APPLET_USER_ONE);
            int score = AppletDetailScene.builder().build().execute(visitor, true).getInteger("score");
            int integralRecordNum = util.getAppletIntegralRecordNum();
            int exchangeRecordNum = util.getAppletExchangeRecordNum();
            int specificationId = AppletCommodityDetailScene.builder().id(exchangePage.getId()).build().execute(visitor, true).getJSONArray("specification_compose_list").getJSONObject(0).getInteger("id");
            //获取邮寄信息
            AppletShippingAddress appletShippingAddress = AppletShippingAddressListScene.builder().build().execute(visitor, true).getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletShippingAddress.class)).collect(Collectors.toList()).get(0);
            //兑换积分
            AppletSubmitOrderScene.builder().commodityId(exchangePage.getId()).specificationId(specificationId).smsNotify(false).commodityNum("1").districtCode(appletShippingAddress.getDistrictCode()).address(appletShippingAddress.getAddress()).receiver(appletShippingAddress.getName()).receivePhone(appletShippingAddress.getPhone()).build().execute(visitor, true);
            user.loginPc(ADMINISTRATOR);
            ExchangePage newExchangePage = util.getExchangePage(exchangePage.getId());
            List<Integer> newExchangedAndSurplusList = Arrays.stream(newExchangePage.getExchangedAndSurplus().split("/")).map(Integer::valueOf).collect(Collectors.toList());
            CommonUtil.checkResult(newExchangePage.getGoodsName() + "已兑换数量", exchangedAndSurplusList.get(0) + 1, newExchangedAndSurplusList.get(0));
            CommonUtil.checkResult(newExchangePage.getGoodsName() + "剩余数量", exchangedAndSurplusList.get(1) - 1, newExchangedAndSurplusList.get(1));
            //小程序积分明细列表数量
            user.loginApplet(APPLET_USER_ONE);
            int newIntegralRecordNum = util.getAppletIntegralRecordNum();
            int newExchangeRecordNum = util.getAppletExchangeRecordNum();
            CommonUtil.checkResult("小程序积分明细页数量", integralRecordNum + 1, newIntegralRecordNum);
            CommonUtil.checkResult("小程序积分订单页兑换商品数量", exchangeRecordNum + 1, newExchangeRecordNum);
            //小程序积分明细
            AppletIntegralRecord appletIntegralRecord = util.getAppletIntegralRecordList().get(0);
            CommonUtil.checkResult("小程序积分明细页积分数", exchangePage.getExchangePrice(), Integer.valueOf(appletIntegralRecord.getIntegral()));
            CommonUtil.checkResult("小程序积分明细详情", "使用" + exchangePage.getExchangePrice() + "积分兑换了【" + exchangePage.getGoodsName() + "】", appletIntegralRecord.getName());
            CommonUtil.checkResult("小程序积分明细兑换类型", ChangeStockTypeEnum.MINUS.name(), appletIntegralRecord.getChangeType());
            //小程序订单状态
            AppletExchangeRecord appletExchangeRecord = util.getAppletExchangeRecordList().get(0);
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.WAITING.getName(), appletExchangeRecord.getExchangeStatusName());
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.WAITING.name(), appletExchangeRecord.getExchangeStatus());
            CommonUtil.checkResult("小程序商品id", exchangePage.getId(), appletExchangeRecord.getCommodityId());
            CommonUtil.checkResult("小程序商品数量", 1, appletExchangeRecord.getNum());
            CommonUtil.checkResult("小程序商品积分", exchangePage.getExchangePrice(), appletExchangeRecord.getIntegral());
            //小程序我的页
            int scoreTwo = AppletDetailScene.builder().build().execute(visitor, true).getInteger("score");
            CommonUtil.checkResult("小程序积分总数", score - appletExchangeRecord.getIntegral(), scoreTwo);
            //积分订单页
            user.loginPc(ADMINISTRATOR);
            IScene exchangeOrderScene = ExchangeOrderScene.builder().build();
            ExchangeOrder exchangeOrder = util.collectBean(exchangeOrderScene, ExchangeOrder.class).get(0);
            CommonUtil.checkResult("pc积分订单页订单号", appletExchangeRecord.getOrderId(), exchangeOrder.getOrderId());
            CommonUtil.checkResult("pc积分订单页订单名称", appletExchangeRecord.getName(), exchangeOrder.getGoodsName());
            CommonUtil.checkResult("pc积分订单页订单分类", CommodityTypeEnum.REAL.getName(), exchangeOrder.getGoodsType());
            CommonUtil.checkResult("pc积分订单页订单状态", appletExchangeRecord.getExchangeStatus(), exchangeOrder.getOrderStatus());
            CommonUtil.checkResult("pc积分订单页订单状态", appletExchangeRecord.getExchangeStatusName(), exchangeOrder.getOrderStatusName());
            //发货
            ConfirmShipmentScene.builder().id(exchangeOrder.getId()).oddNumbers("1122").build().execute(visitor, true);
            //pc状态2
            ExchangeOrder exchangeOrderTwo = util.collectBean(exchangeOrderScene, ExchangeOrder.class).get(0);
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.SEND.name(), exchangeOrderTwo.getOrderStatus());
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.SEND.getName(), exchangeOrderTwo.getOrderStatusName());
            //小程序状态2
            user.loginApplet(APPLET_USER_ONE);
            AppletExchangeRecord appletExchangeRecordTwo = util.getAppletExchangeRecordList().get(0);
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.SEND.name(), appletExchangeRecordTwo.getExchangeStatus());
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.SEND.getName(), appletExchangeRecordTwo.getExchangeStatusName());
            //确认收货
            AppletConfirmReceiveScene.builder().id(exchangeOrder.getId()).build().execute(visitor, true);
            //小程序状态3
            AppletExchangeRecord appletExchangeRecordThread = util.getAppletExchangeRecordList().get(0);
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.FINISHED.name(), appletExchangeRecordThread.getExchangeStatus());
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.FINISHED.getName(), appletExchangeRecordThread.getExchangeStatusName());
            JSONObject response = AppletExchangeRecordDetailScene.builder().id(exchangeOrder.getId()).build().execute(visitor, true);
            CommonUtil.checkResult("小程序订单详情快递单号", "1122", response.getString("odd_numbers"));
            CommonUtil.checkResult("小程序订单详情订单状态", OrderStatusEnum.FINISHED.getName(), response.getString("exchange_status_name"));
            //pc状态3
            user.loginPc(ADMINISTRATOR);
            ExchangeOrder exchangeOrderThread = util.collectBean(exchangeOrderScene, ExchangeOrder.class).get(0);
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.FINISHED.name(), exchangeOrderThread.getOrderStatus());
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.FINISHED.getName(), exchangeOrderThread.getOrderStatusName());
            CommonUtil.checkResult("pc积分订单页订单确认发货", false, exchangeOrderThread.getIsConfirmShipment());
            //积分明细页
            IScene exchangeDetailedScene = ExchangeDetailedScene.builder().build();
            ExchangeDetailed exchangeDetailed = util.collectBean(exchangeDetailedScene, ExchangeDetailed.class).get(0);
            CommonUtil.checkResult("pc积分明细页订单号", appletExchangeRecord.getOrderId(), exchangeDetailed.getOrderCode());
            CommonUtil.checkResult("pc积分明细页明细", appletExchangeRecord.getIntegral(), exchangeDetailed.getStockDetail());
            CommonUtil.checkResult("pc积分明细页明细", ChangeStockTypeEnum.MINUS.name(), exchangeDetailed.getExchangeType());
            CommonUtil.checkResult("pc积分明细页兑换类型", ChangeStockTypeEnum.MINUS.getDescription(), exchangeDetailed.getExchangeTypeName());
            CommonUtil.checkResult("pc积分明细页详情", "使用" + appletExchangeRecord.getIntegral() + "积分兑换了【" + appletExchangeRecord.getName() + "】", exchangeDetailed.getChangeReason());
            //取消
            CancelOrderScene.builder().id(exchangeOrder.getId()).build().execute(visitor, true);
            //pc状态4
            ExchangeOrder exchangeOrderFour = util.collectBean(exchangeOrderScene, ExchangeOrder.class).get(0);
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.CANCELED.name(), exchangeOrderFour.getOrderStatus());
            CommonUtil.checkResult("pc积分订单页订单状态", OrderStatusEnum.CANCELED.getName(), exchangeOrderFour.getOrderStatusName());
            CommonUtil.checkResult("pc积分订单页订单确认发货", false, exchangeOrderFour.getIsConfirmShipment());
            CommonUtil.checkResult("pc积分订单页订单确认取消", false, exchangeOrderFour.getIsCancel());
            //积分明细页2
            ExchangeDetailed exchangeDetailedTwo = util.collectBean(exchangeDetailedScene, ExchangeDetailed.class).get(0);
            CommonUtil.checkResult("pc积分明细页订单号", appletExchangeRecord.getOrderId(), exchangeDetailedTwo.getOrderCode());
            CommonUtil.checkResult("pc积分明细页明细", appletExchangeRecord.getIntegral(), exchangeDetailedTwo.getStockDetail());
            CommonUtil.checkResult("pc积分明细页详情", "取消购买【" + appletExchangeRecord.getName() + "】订单", exchangeDetailedTwo.getChangeReason());
            CommonUtil.checkResult("pc积分明细页明细", ChangeStockTypeEnum.ADD.name(), exchangeDetailedTwo.getExchangeType());
            CommonUtil.checkResult("pc积分明细页兑换类型", ChangeStockTypeEnum.ADD.getDescription(), exchangeDetailedTwo.getExchangeTypeName());
            //小程序状态4
            user.loginApplet(APPLET_USER_ONE);
            AppletExchangeRecord appletExchangeRecordFour = util.getAppletExchangeRecordList().get(0);
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.CANCELED.name(), appletExchangeRecordFour.getExchangeStatus());
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.CANCELED.getName(), appletExchangeRecordFour.getExchangeStatusName());
            //小程序积分明细2
            AppletIntegralRecord appletIntegralRecordTwo = util.getAppletIntegralRecordList().get(0);
            CommonUtil.checkResult("小程序积分明细页积分数", exchangePage.getExchangePrice(), Integer.parseInt(appletIntegralRecordTwo.getIntegral()));
            CommonUtil.checkResult("小程序积分明细详情", "取消购买【" + appletExchangeRecord.getName() + "】订单", appletIntegralRecordTwo.getName());
            CommonUtil.checkResult("小程序积分明细兑换类型", ChangeStockTypeEnum.ADD.name(), appletIntegralRecordTwo.getChangeType());
            //小程序我的页
            int scoreThread = AppletDetailScene.builder().build().execute(visitor, true).getInteger("score");
            CommonUtil.checkResult("小程序积分总数", scoreTwo + exchangePage.getExchangePrice(), scoreThread);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--创建实体积分兑换->小程序兑换实体商品->发货->确认收货->取消");
        }
    }

    //ok
    @Test(description = "积分订单--订单明细--实付总积分=商品积分*兑换数量")
    public void IntegralOrder_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangeOrderScene = ExchangeOrderScene.builder().build();
            List<JSONObject> jsonObjectList = util.collectBean(exchangeOrderScene, JSONObject.class);
            jsonObjectList.forEach(e -> {
                int id = e.getInteger("id");
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

    //bug
    @Test(description = "积分明细--各个积分规则所得积分相加=改规则的已发放积分")
    public void IntegralRule_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangeDetailedScene = ExchangeDetailedScene.builder().build();
            List<ExchangeDetailed> exchangeDetailedList = util.collectBean(exchangeDetailedScene, ExchangeDetailed.class);
            Long stockSum = exchangeDetailedList.stream().filter(e -> e.getChangeReason() != null && e.getChangeReason().contains("签到获得")).mapToLong(ExchangeDetailed::getStockDetail).sum();
            int allSend = IntegralExchangeRulesScene.builder().build().execute(visitor, true).getJSONArray("list").stream().map(e -> (JSONObject) e)
                    .filter(e -> e.getString("rule_name").equals("签到")).map(e -> e.getInteger("all_send")).findFirst().orElse(0);
            CommonUtil.checkResult("签到规则已发放积分", stockSum, allSend);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    //ok
    @Test(description = "积分明细--各个积分规则的奖励积分=该规则的单笔发放积分")
    public void IntegralRule_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene shareManagerPageScene = ShareManagerPageScene.builder().build();
            String[] strings = (String[]) ruleName()[0];
            Arrays.stream(strings).forEach(ruleName -> {
                IntegralRule integralRule = getIntegralRule(ruleName, shareManagerPageScene);
                CommonUtil.checkResultPlus("积分规则" + ruleName + " 单笔发放积分", integralRule.getSingleSend(), "任务管理奖励积分", integralRule.getAwardScore());
                CommonUtil.valueView(ruleName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    //ok
    @Test(description = "积分明细--各个积分规则的奖励积分=该规则的单笔发放积分")
    public void IntegralRule_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene shareManagerPageScene = ShareManagerPageScene.builder().build();
            String[] strings = (String[]) ruleName()[0];
            Arrays.stream(strings).forEach(ruleName -> {
                IntegralRule integralRule = getIntegralRule(ruleName, shareManagerPageScene);
                CommonUtil.checkResultPlus("积分规则" + ruleName + " 单笔发放积分", integralRule.getSingleSend(), "任务管理奖励积分", integralRule.getAwardScore());
                CommonUtil.valueView(ruleName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分明细--各个积分规则的奖励积分=该规则的单笔发放积分");
        }
    }

    //ok
    @Test(description = "积分明细--各个积分规则的奖励积分=该规则的单笔发放积分")
    public void IntegralRule_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene equityPageScene = EquityPageScene.builder().build();
            String[] strings = (String[]) ruleName()[1];
            Arrays.stream(strings).forEach(ruleName -> {
                IntegralRule integralRule = getIntegralRule(ruleName, equityPageScene);
                CommonUtil.checkResultPlus("积分规则" + ruleName + " 单笔发放积分", integralRule.getSingleSend(), "权益列表奖励积分", integralRule.getAwardScore());
                CommonUtil.valueView(ruleName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分明细--各个积分规则的奖励积分=该规则的单笔发放积分");
        }
    }

    //ok
    @Test(description = "积分明细--各个积分规则的奖励积分=该规则的单笔发放积分")
    public void IntegralRule_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene equityPageScene = EquityPageScene.builder().build();
            String[] strings = (String[]) ruleName()[1];
            Arrays.stream(strings).forEach(ruleName -> {
                IntegralRule integralRule = getIntegralRule(ruleName, equityPageScene);
                CommonUtil.checkResultPlus("积分规则" + ruleName + " 单笔发放积分", integralRule.getSingleSend(), "权益列表奖励积分", integralRule.getAwardScore());
                CommonUtil.valueView(ruleName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分明细--各个积分规则的奖励积分=该规则的单笔发放积分");
        }
    }

    //ok
    @Test(description = "积分明细--各个积分规则的奖励积分=该规则的单笔发放积分")
    public void IntegralRule_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene signInConfigPageScene = SignInConfigPageScene.builder().build();
            String[] strings = (String[]) ruleName()[2];
            Arrays.stream(strings).forEach(ruleName -> {
                IntegralRule integralRule = getIntegralRule(ruleName, signInConfigPageScene);
                CommonUtil.checkResultPlus("积分规则" + ruleName + " 单笔发放积分", integralRule.getSingleSend(), "签到配置奖励积分", integralRule.getAwardScore());
                CommonUtil.valueView(ruleName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分明细--各个积分规则的奖励积分=该规则的单笔发放积分");
        }
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    @DataProvider(name = "RULE_NAME")
    public static Object[][] ruleName() {
        return new String[][]{
                {"活动分享", "二维码分享", "完善资料", "预约保养", "预约维修"},
                {"生日积分-普通会员", "生日积分-vip会员"},
                {"签到"}
        };
    }

    /**
     * 比较积分方法
     *
     * @param ruleName 规则名称
     * @param scene    接口场景
     * @return 结果
     */
    public IntegralRule getIntegralRule(String ruleName, IScene scene) {
        IntegralRule integralRule = new IntegralRule();
        IScene integralExchangeRulesScene = IntegralExchangeRulesScene.builder().build();
        List<JSONObject> integralExchangeRulesList = util.collectBean(integralExchangeRulesScene, JSONObject.class);
        int singleSend = integralExchangeRulesList.stream().filter(e -> e.getString("rule_name").contains(ruleName)).map(e -> e.getInteger("single_send")).findFirst().orElse(0);
        List<JSONObject> sceneList = util.collectBean(scene, JSONObject.class);
        int awardScore = Arrays.stream(ruleName()).anyMatch(e -> Arrays.asList(e).contains(ruleName))
                ? util.collectBean(scene, JSONObject.class).stream().map(e -> e.getInteger("award_score")).findFirst().orElse(0)
                : Arrays.stream(ruleName()).anyMatch(e -> Arrays.asList(e).contains(ruleName))
                ? sceneList.stream().filter(e -> ruleName.contains(e.getString("equity_name")) && ruleName.contains(e.getString("service_type_name"))).map(e -> e.getInteger("award_count")).findFirst().orElse(0)
                : sceneList.stream().filter(e -> e.getString("taskName").contains(ruleName)).map(e -> e.getInteger("award_score")).findFirst().orElse(0);
        integralRule.setAwardScore(awardScore);
        integralRule.setSingleSend(singleSend);
        return integralRule;
    }
}