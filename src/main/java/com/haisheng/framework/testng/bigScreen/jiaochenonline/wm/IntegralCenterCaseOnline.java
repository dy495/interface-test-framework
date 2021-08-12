package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletExchangeRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletIntegralRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletShippingAddress;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ExchangeDetailed;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ExchangePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.IntegralRule;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.CustomerIntegralChangeRecordPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.ExchangeGoodsDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.ExchangeOrderBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter.ExchangeStockPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.GoodsManagePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherFormVoucherPageBean;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.ChangeStockTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.CommodityTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralExchangeStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.OrderStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity.CommodityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralmall.GoodsManagePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.EquityPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.ShareManagerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.SignInConfigPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.AddVoucherScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
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
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class IntegralCenterCaseOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.JC_ALL_AUTHORITY_ONLINE;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.setShopId(PRODUCE.getShopId()).setReferer(PRODUCE.getReferer()).setRoleId(ALL_AUTHORITY.getRoleId()).setProduct(PRODUCE.getAbbreviation());
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
        util.loginPc(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    //ok
    @Test(description = "积分客户管理--增加某人积分异常")
    public void integralCustomerPage_system_1() {
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
    @Test(description = "积分客户管理--增加某人积分")
    public void integralCustomerPage_system_3() {
        try {
            util.loginApplet(APPLET_USER_ONE);
            Integer score = AppletUserInfoDetailScene.builder().build().invoke(visitor).getInteger("score");
            util.loginPc(ALL_AUTHORITY);
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
            CommonUtil.checkResult("剩余积分", String.valueOf(score + 1), newChangeRecordList.get(0).getLeft());
            CommonUtil.checkResult("备注", EnumDesc.DESC_BETWEEN_5_10.getDesc(), newChangeRecordList.get(0).getRemark());
            ExchangeDetailed exchangeDetailed = util.toJavaObjectList(ExchangeDetailedScene.builder().phone(APPLET_USER_ONE.getPhone()).build(), ExchangeDetailed.class).get(0);
            CommonUtil.checkResult("pc积分明细变更内容", 1, exchangeDetailed.getStockDetail());
            CommonUtil.checkResult("pc积分明细兑换类型", ChangeStockTypeEnum.ADD.getDescription(), exchangeDetailed.getExchangeTypeName());
            CommonUtil.checkResult("pc积分明细变更类型", ChangeStockTypeEnum.ADD.name(), exchangeDetailed.getExchangeType());
            util.loginApplet(APPLET_USER_ONE);
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
    public void integralCustomerPage_system_4() {
        try {
            util.loginApplet(APPLET_USER_ONE);
            Integer score = AppletUserInfoDetailScene.builder().build().invoke(visitor).getInteger("score");
            util.loginPc(ALL_AUTHORITY);
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
            CommonUtil.checkResult("剩余积分", String.valueOf(score - 1), newChangeRecordList.get(0).getLeft());
            CommonUtil.checkResult("备注", EnumDesc.DESC_BETWEEN_5_10.getDesc(), newChangeRecordList.get(0).getRemark());
            ExchangeDetailed exchangeDetailed = util.toJavaObjectList(ExchangeDetailedScene.builder().phone(APPLET_USER_ONE.getPhone()).build(), ExchangeDetailed.class).get(0);
            CommonUtil.checkResult("pc积分明细变更内容", 1, exchangeDetailed.getStockDetail());
            CommonUtil.checkResult("pc积分明细兑换类型", ChangeStockTypeEnum.MINUS.getDescription(), exchangeDetailed.getExchangeTypeName());
            CommonUtil.checkResult("pc积分明细变更类型", ChangeStockTypeEnum.MINUS.name(), exchangeDetailed.getExchangeType());
            util.loginApplet(APPLET_USER_ONE);
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
    @Test(description = "积分兑换--库存详情--当前库存=兑换品库存明细加和")
    public void integralExchange_data_1() {
        try {
            //上线日期，之前得数据不做校验
            long time = Long.parseLong(DateTimeUtil.dateToStamp("2021-02-25", "yyyy-MM-dd"));
            IScene exchangePageScene = ExchangePageScene.builder().build();
            List<JSONObject> exchangePageList = util.toJavaObjectList(exchangePageScene, JSONObject.class);
            exchangePageList.stream().filter(e -> Long.parseLong(DateTimeUtil.dateToStamp(e.getString("begin_use_time"))) >= time).forEach(e -> {
                int id = e.getInteger("id");
                AtomicInteger s = new AtomicInteger();
                IScene exchangeStockScene = ExchangeGoodsStockScene.builder().id(String.valueOf(id)).build();
                JSONObject response = visitor.invokeApi(exchangeStockScene);
                String goodsName = response.getString("goods_name");
                int goodsStock = response.getInteger("goods_stock");
                IScene exchangeStockPageScene = ExchangeStockPageScene.builder().id(String.valueOf(id)).build();
                List<JSONObject> exchangeStockPageList = util.toJavaObjectList(exchangeStockPageScene, JSONObject.class);
                exchangeStockPageList.forEach(a -> {
                    String exchangeType = a.getString("exchange_type");
                    int stockDetail = a.getInteger("stock_detail");
                    s.set(exchangeType.equals("ADD") ? s.addAndGet(stockDetail) : s.addAndGet(-stockDetail));
                });
                CommonUtil.checkResultPlus(goodsName + " 兑换品库存明细加和", s.get(), "当前库存", goodsStock);
                CommonUtil.logger(goodsName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--库存详情--当前库存=兑换品库存明细加和");
        }
    }

    //ok
    @Test(description = "积分兑换--创建实物积分兑换，积分兑换列表+1")
    public void integralExchange_data_2() {
        try {
            int total = ExchangePageScene.builder().build().invoke(visitor).getInteger("total");
            //创建实物兑换
            ExchangePage exchangePage = util.createExchangeRealGoods();
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
            saveData("积分兑换--创建实物积分兑换，积分兑换列表+1");
        }
    }

    //ok
    @Test(description = "积分兑换--创建虚拟积分兑换，积分兑换列表+1")
    public void integralExchange_data_3() {
        try {
            int total = ExchangePageScene.builder().build().invoke(visitor).getInteger("total");
            Long voucherId = new VoucherGenerator.Builder().status(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId();
            //创建实物兑换
            ExchangePage exchangePage = util.createExchangeFictitiousGoods(voucherId);
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
            saveData("积分兑换--创建虚拟积分兑换，积分兑换列表+1");
        }
    }

    //ok
    @Test(description = "积分兑换--创建虚拟积分兑换，编辑库存-1，卡券可用库存+1")
    public void integralExchange_data_4() {
        Long id = null;
        try {
            Long voucherId = new VoucherGenerator.Builder().status(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId();
            VoucherFormVoucherPageBean voucherPage = util.getVoucherPage(voucherId);
            //创建实物兑换
            ExchangePage exchangePage = util.createExchangeFictitiousGoods(voucherId);
            id = exchangePage.getId();
            VoucherFormVoucherPageBean secondVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult("创建积分兑换后" + voucherPage.getVoucherName() + "的可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            //减少库存1
            EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.MINUS.name()).num(String.valueOf(1)).id(id)
                    .goodsName(exchangePage.getGoodsName()).type(CommodityTypeEnum.FICTITIOUS.name()).build().invoke(visitor);
            VoucherFormVoucherPageBean thirdVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult("减少库存后卡券" + voucherPage.getVoucherName() + "的可用库存", voucherPage.getAllowUseInventory(), thirdVoucherPage.getAllowUseInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            ChangeSwitchStatusScene.builder().id(id).status(false).build().invoke(visitor);
            DeleteExchangeGoodsScene.builder().id(id).build().invoke(visitor);
            saveData("积分兑换--创建虚拟积分兑换，编辑库存-1，卡券可用库存+1");
        }
    }

    //ok
    @Test(description = "积分兑换--创建虚拟积分兑换，编辑库存+1，卡券可用库存-1")
    public void integralExchange_data_5() {
        Long id = null;
        try {
            Long voucherId = new VoucherGenerator.Builder().status(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId();
            VoucherFormVoucherPageBean voucherPage = util.getVoucherPage(voucherId);
            //创建实物兑换
            ExchangePage exchangePage = util.createExchangeFictitiousGoods(voucherId);
            id = exchangePage.getId();
            VoucherFormVoucherPageBean secondVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult("创建积分兑换后" + voucherPage.getVoucherName() + "的可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            //增加库存1
            EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.ADD.name()).num(String.valueOf(1)).id(id)
                    .goodsName(exchangePage.getGoodsName()).type(CommodityTypeEnum.FICTITIOUS.name()).build().invoke(visitor);
            VoucherFormVoucherPageBean thirdVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult("减少库存后卡券" + voucherPage.getVoucherName() + "的可用库存", secondVoucherPage.getAllowUseInventory() - 1, thirdVoucherPage.getAllowUseInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            ChangeSwitchStatusScene.builder().id(id).status(false).build().invoke(visitor);
            DeleteExchangeGoodsScene.builder().id(id).build().invoke(visitor);
            saveData("积分兑换--创建虚拟积分兑换，编辑库存+1，卡券可用库存-1");
        }
    }

    //ok
    @Test(description = "积分兑换--创建实体积分兑换")
    public void integralExchange_system_1() {
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
            CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId).exchangePrice("1")
                    .isLimit(true).exchangePeopleNum("1").specificationList(specificationList).exchangeStartTime(exchangeStartTime)
                    .exchangeEndTime(exchangeEndTime).build().invoke(visitor);
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
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            VoucherFormVoucherPageBean voucherPage = util.getVoucherPage(voucherId);
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            long exchangeNum = voucherPage.getAllowUseInventory() == 1 ? 1 : voucherPage.getAllowUseInventory() - 1;
            //创建积分兑换
            CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId).exchangePrice("1")
                    .exchangeNum(String.valueOf(exchangeNum)).isLimit(true).exchangePeopleNum("1").exchangeStartTime(exchangeStartTime)
                    .exchangeEndTime(exchangeEndTime).build().invoke(visitor);
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
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            VoucherFormVoucherPageBean voucherPage = util.getVoucherPage(voucherId);
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            long exchangeNum = voucherPage.getSurplusInventory() + 1;
            //创建积分兑换
            String message = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId).exchangePrice("1")
                    .exchangeNum(String.valueOf(exchangeNum)).isLimit(true).exchangePeopleNum("1").exchangeStartTime(exchangeStartTime)
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
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            VoucherFormVoucherPageBean voucherPage = util.getVoucherPage(voucherId);
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            long exchangeNum = voucherPage.getSurplusInventory() == 1 ? 1 : voucherPage.getSurplusInventory() - 1;
            String[] strings = {"1.11", null, "", "-1"};
            Arrays.stream(strings).forEach(exchangePrice -> {
                //创建积分兑换
                String message = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId).exchangePrice(exchangePrice)
                        .exchangeNum(String.valueOf(exchangeNum)).isLimit(true).exchangePeopleNum("1").exchangeStartTime(exchangeStartTime)
                        .exchangeEndTime(exchangeEndTime).build().invoke(visitor, false).getString("message");
                String err = StringUtils.isEmpty(exchangePrice) ? "兑换价格不能为空" : Double.parseDouble(exchangePrice) < 0 ? "兑换价格必须大于等于0" : "请求入参类型不正确";
                CommonUtil.checkResult("兑换价为" + exchangePrice, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--创建虚拟积分兑换，兑换价异常");
        }
    }

    //ok
    @Test(description = "积分兑换--创建虚拟积分兑换，包含无库存的卡券")
    public void integralExchange_system_5() {
        try {
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherPage(voucherId).getVoucherName();
            String message = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId).exchangePrice("1")
                    .exchangeNum("1").isLimit(true).exchangePeopleNum("1").exchangeStartTime(exchangeStartTime)
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
        try {
            IScene exchangePageScene = ExchangePageScene.builder().exchangeType(CommodityTypeEnum.REAL.name()).build();
            List<ExchangePage> exchangePageList = util.toJavaObjectList(exchangePageScene, ExchangePage.class);
            Long id = exchangePageList.stream().filter(e -> Integer.parseInt(e.getExchangedAndSurplus().split("/")[1]) < 10).map(ExchangePage::getId).findFirst().orElse(0L);
            IScene exchangeStockPageScene = ExchangeStockPageScene.builder().id(String.valueOf(id)).build();
            int exchangeStockPageListSize = util.toJavaObjectList(exchangeStockPageScene, ExchangeStockPageBean.class).size();
            String goodsName = ExchangeGoodsStockScene.builder().id(String.valueOf(id)).build().invoke(visitor).getString("goods_name");
            JSONObject specificationDetail = ExchangeCommoditySpecificationsListScene.builder().id(id).build().invoke(visitor).getJSONArray("specification_detail_list").getJSONObject(0);
            String firstSpecificationsName = specificationDetail.getString("first_specifications_name");
            Integer num = specificationDetail.getInteger("num");
            //增加库存
            EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.ADD.name()).num("1").id(specificationDetail.getLong("id")).goodsName(goodsName).type(CommodityTypeEnum.REAL.name()).build().invoke(visitor);
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
            EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.MINUS.name()).num("1").id(specificationDetail.getLong("id")).goodsName(goodsName).type(CommodityTypeEnum.REAL.name()).build().invoke(visitor);
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
        try {
            IScene exchangePageScene = ExchangePageScene.builder().exchangeType(CommodityTypeEnum.REAL.name()).build();
            List<ExchangePage> exchangePageList = util.toJavaObjectList(exchangePageScene, ExchangePage.class);
            Long id = exchangePageList.stream().filter(e -> Integer.parseInt(e.getExchangedAndSurplus().split("/")[1]) < 10).map(ExchangePage::getId).findFirst().orElse(util.createExchangeRealGoods().getId());
            String goodsName = ExchangeGoodsStockScene.builder().id(String.valueOf(id)).build().invoke(visitor).getString("goods_name");
            JSONObject specificationDetail = ExchangeCommoditySpecificationsListScene.builder().id(id).build().invoke(visitor).getJSONArray("specification_detail_list").getJSONObject(0);
            String[] strings = {"1000000000", "99.99", null, "", "-11", "0"};
            Arrays.stream(strings).forEach(num -> {
                String addMessage = EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.ADD.name()).num(num).id(specificationDetail.getLong("id"))
                        .goodsName(goodsName).type(CommodityTypeEnum.REAL.name()).build().invoke(visitor, false).getString("message");
                String err = StringUtils.isEmpty(num) ? "改变库存数量不能为空" : Double.parseDouble(num) >= 1000000000 ? "库存改变数量不能超过100000000" :
                        Double.parseDouble(num) <= 0 ? "库存变动数量需大于等于1" : "请求入参类型不正确";
                CommonUtil.checkResult(goodsName + "修改库存" + num, err, addMessage);
                CommonUtil.logger(num);
                String minusMessage = EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.MINUS.name()).num(num).id(specificationDetail.getLong("id"))
                        .goodsName(goodsName).type(CommodityTypeEnum.REAL.name()).build().invoke(visitor, false).getString("message");
                CommonUtil.checkResult(goodsName + "修改库存" + num, err, minusMessage);
                CommonUtil.logger(num);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--修改实体积分兑换库存，异常情况");
        }
    }

    //ok
    @Test(description = "积分兑换--修改实体积分兑换库存，减少大于当前库存的数")
    public void integralExchange_system_11() {
        try {
            IScene exchangePageScene = ExchangePageScene.builder().exchangeType(CommodityTypeEnum.REAL.name()).build();
            List<ExchangePage> exchangePageList = util.toJavaObjectList(exchangePageScene, ExchangePage.class);
            Long id = exchangePageList.stream().map(ExchangePage::getId).findFirst().orElse(0L);
            String goodsName = ExchangeGoodsStockScene.builder().id(String.valueOf(id)).build().invoke(visitor).getString("goods_name");
            JSONObject specificationDetail = ExchangeCommoditySpecificationsListScene.builder().id(id).build().invoke(visitor).getJSONArray("specification_detail_list").getJSONObject(0);
            String message = EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.MINUS.name()).num(String.valueOf(specificationDetail.getInteger("num") + 1)).id(specificationDetail.getLong("id")).goodsName(goodsName).type(CommodityTypeEnum.REAL.name()).build().invoke(visitor, false).getString("message");
            String err = "减少库存量需小于等于当前库存量";
            CommonUtil.checkResult(goodsName + "减少库存" + (specificationDetail.getInteger("num") + 1), err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--修改实体积分兑换库存，减少大于当前库存的数");
        }
    }

    //ok
    @Test(description = "积分兑换--修改虚拟积分兑换库存，增加大于卡券库存的数")
    public void integralExchange_system_12() {
        try {
            IScene exchangePageScene = ExchangePageScene.builder().exchangeType(CommodityTypeEnum.FICTITIOUS.name()).build();
            List<ExchangePage> exchangePageList = util.toJavaObjectList(exchangePageScene, ExchangePage.class);
            Long id = exchangePageList.stream().map(ExchangePage::getId).findFirst().orElse(0L);
            JSONObject exchangeGoodsStockResponse = ExchangeGoodsStockScene.builder().id(String.valueOf(id)).build().invoke(visitor);
            Integer goodsStock = exchangeGoodsStockResponse.getInteger("goods_stock");
            String goodsName = exchangeGoodsStockResponse.getString("goods_name");
            int surplusInventory = util.getVoucherPage(goodsName).getSurplusInventory();
            long num = surplusInventory >= goodsStock ? surplusInventory - goodsStock + 1 : goodsStock - surplusInventory;
            //增加库存
            String message = EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.ADD.name()).num(String.valueOf(num)).id(id)
                    .goodsName(goodsName).type(CommodityTypeEnum.FICTITIOUS.name()).build().invoke(visitor, false).getString("message");
            String err = "积分商品库存不能超过商品可用库存";
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
        try {
            IScene exchangePageScene = ExchangePageScene.builder().exchangeType(CommodityTypeEnum.FICTITIOUS.name()).build();
            List<ExchangePage> exchangePageList = util.toJavaObjectList(exchangePageScene, ExchangePage.class);
            Long id = exchangePageList.stream().map(ExchangePage::getId).findFirst().orElse(0L);
            JSONObject exchangeGoodsStockResponse = ExchangeGoodsStockScene.builder().id(String.valueOf(id)).build().invoke(visitor);
            Integer goodsStock = exchangeGoodsStockResponse.getInteger("goods_stock");
            String goodsName = exchangeGoodsStockResponse.getString("goods_name");
            long num = goodsStock + 1;
            //增加库存
            String message = EditExchangeStockScene.builder().changeStockType(ChangeStockTypeEnum.MINUS.name()).num(String.valueOf(num)).id(id).goodsName(goodsName).type(CommodityTypeEnum.FICTITIOUS.name()).build().invoke(visitor, false).getString("message");
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
    public void integralExchange_system_14() {
        try {
            IScene exchangePageScene = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).exchangeType(CommodityTypeEnum.REAL.name()).build();
            ExchangePage a = util.toJavaObjectList(exchangePageScene, ExchangePage.class).stream().filter(e -> !e.getExchangedAndSurplus().split("/")[1].equals("0") && e.getExchangePrice() == 1).findFirst().orElse(null);
            ExchangePage exchangePage = a == null ? util.createExchangeRealGoods() : a;
            //不限兑换次数
            util.modifyExchangeGoodsLimit(exchangePage.getId(), exchangePage.getExchangeType(), false);
            List<Integer> exchangedAndSurplusList = Arrays.stream(exchangePage.getExchangedAndSurplus().split("/")).map(Integer::valueOf).collect(Collectors.toList());
            util.loginApplet(APPLET_USER_ONE);
            int score = AppletDetailScene.builder().build().invoke(visitor).getInteger("score");
            int integralRecordNum = util.getAppletIntegralRecordNum();
            int exchangeRecordNum = util.getAppletExchangeRecordNum();
            int specificationId = AppletCommodityDetailScene.builder().id(exchangePage.getId()).build().invoke(visitor).getJSONArray("specification_compose_list").getJSONObject(0).getInteger("id");
            //获取邮寄信息
            AppletShippingAddress appletShippingAddress = AppletShippingAddressListScene.builder().build().invoke(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, AppletShippingAddress.class)).collect(Collectors.toList()).get(0);
            //兑换积分
            AppletSubmitOrderScene.builder().commodityId(exchangePage.getId()).specificationId(specificationId).smsNotify(false).commodityNum("1").districtCode(appletShippingAddress.getDistrictCode()).address(appletShippingAddress.getAddress()).receiver(appletShippingAddress.getName()).receivePhone(appletShippingAddress.getPhone()).build().invoke(visitor);
            util.loginPc(ALL_AUTHORITY);
            ExchangePage newExchangePage = util.getExchangePage(exchangePage.getId());
            List<Integer> newExchangedAndSurplusList = Arrays.stream(newExchangePage.getExchangedAndSurplus().split("/")).map(Integer::valueOf).collect(Collectors.toList());
            CommonUtil.checkResult(newExchangePage.getGoodsName() + "已兑换数量", exchangedAndSurplusList.get(0) + 1, newExchangedAndSurplusList.get(0));
            CommonUtil.checkResult(newExchangePage.getGoodsName() + "剩余数量", exchangedAndSurplusList.get(1) - 1, newExchangedAndSurplusList.get(1));
            //小程序积分明细列表数量
            util.loginApplet(APPLET_USER_ONE);
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
            int scoreTwo = AppletDetailScene.builder().build().invoke(visitor).getInteger("score");
            CommonUtil.checkResult("小程序积分总数", score - appletExchangeRecord.getIntegral(), scoreTwo);
            //积分订单页
            util.loginPc(ALL_AUTHORITY);
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
            util.loginApplet(APPLET_USER_ONE);
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
            util.loginPc(ALL_AUTHORITY);
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
            CommonUtil.checkResult("pc积分明细页详情", "使用" + appletExchangeRecord.getIntegral() + "积分兑换了【" + appletExchangeRecord.getName() + "】", exchangeDetailed.getChangeReason());
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
            util.loginApplet(APPLET_USER_ONE);
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
    @Test(description = "积分兑换--创建虚拟积分兑换->小程序兑换虚拟商品")
    public void integralExchange_system_15() {
        Long exchangeGoodsId = null;
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            ExchangePage exchangePage = util.createExchangeFictitiousGoods(voucherId);
            exchangeGoodsId = exchangePage.getId();
            //不限兑换次数
            util.modifyExchangeGoodsLimit(exchangeGoodsId, exchangePage.getExchangeType(), false);
            List<Integer> exchangedAndSurplusList = Arrays.stream(exchangePage.getExchangedAndSurplus().split("/")).map(Integer::valueOf).collect(Collectors.toList());
            visitor.setToken(APPLET_USER_ONE.getToken());
            int score = AppletDetailScene.builder().build().invoke(visitor).getInteger("score");
            int integralRecordNum = util.getAppletIntegralRecordNum();
            int exchangeRecordNum = util.getAppletExchangeRecordNum();
            //兑换积分
            AppletIntegralExchangeScene.builder().id(exchangeGoodsId).build().invoke(visitor);
            util.loginPc(ALL_AUTHORITY);
            ExchangePage newExchangePage = util.getExchangePage(exchangeGoodsId);
            List<Integer> newExchangedAndSurplusList = Arrays.stream(newExchangePage.getExchangedAndSurplus().split("/")).map(Integer::valueOf).collect(Collectors.toList());
            CommonUtil.checkResult(newExchangePage.getGoodsName() + "已兑换数量", exchangedAndSurplusList.get(0) + 1, newExchangedAndSurplusList.get(0));
            CommonUtil.checkResult(newExchangePage.getGoodsName() + "剩余数量", exchangedAndSurplusList.get(1) - 1, newExchangedAndSurplusList.get(1));
            //小程序积分明细列表数量
            visitor.setToken(APPLET_USER_ONE.getToken());
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
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.FINISHED.getName(), appletExchangeRecord.getExchangeStatusName());
            CommonUtil.checkResult("小程序订单状态", OrderStatusEnum.FINISHED.name(), appletExchangeRecord.getExchangeStatus());
            CommonUtil.checkResult("小程序商品id", exchangePage.getId(), appletExchangeRecord.getCommodityId());
            CommonUtil.checkResult("小程序商品数量", 1, appletExchangeRecord.getNum());
            CommonUtil.checkResult("小程序商品积分", exchangePage.getExchangePrice(), appletExchangeRecord.getIntegral());
            //小程序我的页
            int scoreTwo = AppletDetailScene.builder().build().invoke(visitor).getInteger("score");
            CommonUtil.checkResult("小程序积分总数", score - appletExchangeRecord.getIntegral(), scoreTwo);
            //积分订单页
            util.loginPc(ALL_AUTHORITY);
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
            saveData("积分兑换--创建虚拟积分兑换->小程序兑换虚拟商品");
        }
    }

    //ok
    @Test(description = "积分兑换--置顶某一积分兑换，小程序人气推荐置顶该兑换，再置顶一个积分兑换，人气推荐刷新第一位，原第一位降到第二位")
    public void integralExchange_system_16() {
        try {
            JSONObject response = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).build().invoke(visitor);
            Long id = response.getJSONArray("list").getJSONObject(0).getLong("id");
            Preconditions.checkArgument(id != null, "不存在积分兑换商品");
            //置顶
            MakeTopScene.builder().id(id).build().invoke(visitor);
            //小程序人气推荐
            util.loginApplet(APPLET_USER_ONE);
            JSONArray list = AppletHomePageScene.builder().build().invoke(visitor).getJSONArray("recommend_list");
            Long appletId = list.getJSONObject(0).getLong("id");
            CommonUtil.checkResult("人气推荐第一项", id, appletId);
            util.loginPc(ALL_AUTHORITY);
            //再置顶第二个积分兑换
            JSONObject response1 = ExchangePageScene.builder().status(IntegralExchangeStatusEnum.WORKING.name()).build().invoke(visitor);
            Long id1 = response1.getJSONArray("list").getJSONObject(1).getLong("id");
            Preconditions.checkArgument(id1 != null, "不存在积分兑换商品");
            //置顶
            MakeTopScene.builder().id(id1).build().invoke(visitor);
            util.loginApplet(APPLET_USER_ONE);
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
    public void integralExchange_system_17() {
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
    @Test(description = "积分兑换--关闭积分兑换，优惠券可用库存释放")
    public void integralExchange_system_18() {
        Long id = null;
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            VoucherFormVoucherPageBean voucherPage = util.getVoucherPage(voucherId);
            //创建积分兑换
            ExchangePage exchangePage = util.createExchangeFictitiousGoods(voucherId);
            id = exchangePage.getId();
            //开启
            String message = ChangeSwitchStatusScene.builder().id(exchangePage.getId()).status(true).build().getResponse(visitor).getMessage();
            String err = "积分兑换商品已开启，请勿重复操作";
            CommonUtil.checkResult("再次开启已开启的积分兑换商品", err, message);
            VoucherFormVoucherPageBean secondVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherPage.getVoucherName() + "的可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherPage.getVoucherName() + "的剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //关闭
            ChangeSwitchStatusScene.builder().id(exchangePage.getId()).status(false).build().invoke(visitor);
            VoucherFormVoucherPageBean thirdVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherPage.getVoucherName() + "的可用库存", voucherPage.getAllowUseInventory(), thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherPage.getVoucherName() + "的剩余库存", voucherPage.getSurplusInventory(), thirdVoucherPage.getSurplusInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            String message = ChangeSwitchStatusScene.builder().id(id).status(false).build().getResponse(visitor).getMessage();
            String err = "积分兑换商品已关闭，请勿重复操作";
            CommonUtil.checkResult("再次关闭已关闭的积分兑换商品", err, message);
            DeleteExchangeGoodsScene.builder().id(id).build().invoke(visitor);
            saveData("积分兑换--关闭积分兑换，优惠券可用库存释放");
        }
    }

    //ok
    @Test(description = "积分兑换--兑换商品过期，不释放可用库存")
    public void integralExchange_system_19() {
        Long id = null;
        try {
            String exchangeStartTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -2), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -1), "yyyy-MM-dd HH:mm:ss");
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            VoucherFormVoucherPageBean voucherPage = util.getVoucherPage(voucherId);
            //创建积分兑换
            ExchangePage exchangePage = util.createExchangeFictitiousGoods(voucherId);
            id = exchangePage.getId();
            IScene scene = ExchangeGoodsDetailScene.builder().id(id).build();
            ExchangeGoodsDetailBean bean = util.toJavaObject(scene, ExchangeGoodsDetailBean.class);
            //修改至过期
            EditExchangeGoodsScene.builder().exchangeGoodsType(bean.getExchangeGoodsType())
                    .goodsId(bean.getGoodsId()).exchangePrice(bean.getExchangePrice()).exchangeNum(bean.getExchangeNum())
                    .isLimit(bean.getIsLimit()).exchangePeopleNum(bean.getExchangePeopleNum()).expireType(bean.getExpireType())
                    .useDays(bean.getUseDays()).exchangeStartTime(exchangeStartTime).exchangeEndTime(exchangeEndTime)
                    .id(id).build().invoke(visitor);
            VoucherFormVoucherPageBean newVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherPage.getVoucherName() + "的可用库存", voucherPage.getAllowUseInventory() - 1, newVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherPage.getVoucherName() + "的剩余库存", voucherPage.getSurplusInventory(), newVoucherPage.getSurplusInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            ChangeSwitchStatusScene.builder().id(id).status(false).build().invoke(visitor);
            DeleteExchangeGoodsScene.builder().id(id).build().invoke(visitor);
            saveData("积分兑换--兑换商品过期，释放可用库存");
        }
    }

    //ok
    @Test(description = "积分兑换--创建虚拟兑换商品时，可兑换库存大于卡券可用库存，创建失败")
    public void integralExchange_system_20() {
        try {
            VoucherFormVoucherPageBean voucherPage = util.getOccupyVoucherId();
            //创建积分兑换
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            String message = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherPage.getVoucherId())
                    .exchangePrice("1").isLimit(true).exchangePeopleNum("10").exchangeStartTime(exchangeStartTime)
                    .exchangeEndTime(exchangeEndTime).expireType(2).useDays("10").exchangeNum(String.valueOf(voucherPage.getAllowUseInventory() + 1))
                    .build().getResponse(visitor).getMessage();
            String err = "卡券【" + voucherPage.getVoucherName() + "】可用库存不足！";
            CommonUtil.checkResult("创建虚拟兑换商品时，可兑换库存大于卡券剩余库存", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--创建虚拟兑换商品时，可兑换库存大于卡券可用库存，创建失败");
        }
    }

    //ok
    @Test(description = "积分兑换--先关闭虚拟兑换商品，商品内卡券库存不足再开启兑换商品，失败")
    public void integralExchange_system_21() {
        ExchangePage exchangePage = null;
        try {
            VoucherFormVoucherPageBean voucherPage = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherPage();
            Long voucherId = voucherPage.getVoucherId();
            String voucherName = voucherPage.getVoucherName();
            AddVoucherScene.builder().addNumber(1).id(voucherId).build().invoke(visitor);
            util.applyVoucher(voucherName, "1");
            voucherPage = util.flushVoucherPage(voucherPage);
            int exchangeNum = voucherPage.getAllowUseInventory() - 1;
            //创建积分兑换，库存=可用库存-1
            exchangePage = util.createExchangeFictitiousGoods(voucherId, (long) exchangeNum);
            long id = exchangePage.getId();
            VoucherFormVoucherPageBean secondVoucherPage = util.flushVoucherPage(voucherPage);
            CommonUtil.checkResult("创建积分兑换后 " + voucherPage.getVoucherName() + " 的可用库存", voucherPage.getAllowUseInventory() - exchangeNum, secondVoucherPage.getAllowUseInventory());
            //关闭积分兑换还库存
            ChangeSwitchStatusScene.builder().id(id).status(false).build().invoke(visitor);
            VoucherFormVoucherPageBean thirdVoucherPage = util.flushVoucherPage(secondVoucherPage);
            CommonUtil.checkResult("关闭积分兑换后 " + voucherPage.getVoucherName() + " 的可用库存", voucherPage.getAllowUseInventory(), thirdVoucherPage.getAllowUseInventory());
            //占用两个卡券
            JSONArray voucherArray = util.getVoucherArray(voucherPage, 2);
            util.buyTemporaryPackage(voucherArray, 1);
            VoucherFormVoucherPageBean fourVoucherPage = util.flushVoucherPage(thirdVoucherPage);
            CommonUtil.checkResult("占用卡券后 " + voucherPage.getVoucherName() + " 的可用库存", voucherPage.getAllowUseInventory() - 2, fourVoucherPage.getAllowUseInventory());
            //再开启积分兑换
            String message = ChangeSwitchStatusScene.builder().id(id).status(true).build().invoke(visitor, false).getString("message");
            String err = "卡券【" + voucherPage.getVoucherName() + "】可用库存不足！";
            CommonUtil.checkResult("创建虚拟兑换商品时，可兑换库存大于卡券剩余库存", err, message);
            //取消卡券的购买
            util.cancelSoldPackage("临时套餐");
            VoucherFormVoucherPageBean fiveVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult("拒绝支付后 " + voucherPage.getVoucherName() + " 的可用库存", voucherPage.getAllowUseInventory(), fiveVoucherPage.getAllowUseInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            Preconditions.checkArgument(exchangePage != null, "闭虚拟兑换商品不存在");
            ChangeSwitchStatusScene.builder().id(exchangePage.getId()).status(false).build().invoke(visitor, false);
            DeleteExchangeGoodsScene.builder().id(exchangePage.getId()).build().invoke(visitor);
            saveData("积分兑换--先关闭虚拟兑换商品，商品内卡券库存不足再开启兑换商品，失败");
        }
    }

    //ok
    @Test(description = "积分兑换--创建积分兑换，兑换一个，再关闭积分兑换")
    public void integralExchange_system_22() {
        Long exchangeGoodsId = null;
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //为确保可用库存够用，先增加两个
            AddVoucherScene.builder().addNumber(2).id(voucherId).build().invoke(visitor);
            util.applyVoucher(util.getVoucherName(voucherId), "1");
            VoucherFormVoucherPageBean voucherPage = util.getVoucherPage(voucherId);
            ExchangePage exchangePage = util.createExchangeFictitiousGoods(voucherId, 2L);
            exchangeGoodsId = exchangePage.getId();
            //不限兑换次数
            util.modifyExchangeGoodsLimit(exchangeGoodsId, exchangePage.getExchangeType(), false);
            visitor.setToken(APPLET_USER_ONE.getToken());
            //兑换积分
            AppletIntegralExchangeScene.builder().id(exchangeGoodsId).build().invoke(visitor);
            util.loginPc(ALL_AUTHORITY);
            VoucherFormVoucherPageBean secondExchangePage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult("兑换一张卡券后 " + voucherPage.getVoucherName() + " 的可用库存", voucherPage.getAllowUseInventory() - 2, secondExchangePage.getAllowUseInventory());
            CommonUtil.checkResult("兑换一张卡券后 " + voucherPage.getVoucherName() + " 的剩余库存", voucherPage.getSurplusInventory() - 1, secondExchangePage.getSurplusInventory());
            //关闭积分兑换
            ChangeSwitchStatusScene.builder().id(exchangeGoodsId).status(false).build().invoke(visitor);
            //卡券的可用库存还回去了
            VoucherFormVoucherPageBean thirdExchangePage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult("关闭积分兑换后 " + voucherPage.getVoucherName() + " 的可用库存", secondExchangePage.getAllowUseInventory() + 1, thirdExchangePage.getAllowUseInventory());
            CommonUtil.checkResult("关闭积分兑换后 " + voucherPage.getVoucherName() + " 的剩余库存", secondExchangePage.getSurplusInventory(), thirdExchangePage.getSurplusInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            ChangeSwitchStatusScene.builder().id(exchangeGoodsId).status(false).build().invoke(visitor, false);
            DeleteExchangeGoodsScene.builder().id(exchangeGoodsId).build().invoke(visitor);
            saveData("积分兑换--积分兑换--创建积分兑换，兑换一个，再关闭积分兑换");
        }
    }

    //ok
    @Test(description = "积分兑换--创建实体积分兑换时将商品下架")
    public void integralExchange_system_23() {
        try {
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            IScene goodsManagePageScene = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.DOWN.name()).build();
            long goodsId = util.toFirstJavaObject(goodsManagePageScene, GoodsManagePageBean.class).getId();
            JSONArray specificationDetailList = CommoditySpecificationsListScene.builder().id(goodsId).build().invoke(visitor).getJSONArray("specification_detail_list");
            JSONArray specificationList = new JSONArray(specificationDetailList.stream().map(e -> (JSONObject) e).map(e -> put(e.getInteger("id"), 1)).collect(Collectors.toList()));
            IScene scene = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId)
                    .exchangePrice("1").isLimit(true).exchangePeopleNum("10").specificationList(specificationList).expireType(2).useDays("10")
                    .exchangeStartTime(exchangeStartTime).exchangeEndTime(exchangeEndTime).build();
            String message = scene.getResponse(visitor).getMessage();
            String err = "商品已下架";
            CommonUtil.checkResult("下架的商品创建积分兑换", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--创建实体积分兑换时将商品下架");
        }
    }

    @NotNull
    private JSONObject put(Integer id, Integer stock) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("stock", stock);
        return jsonObject;
    }

    //ok
    @Test(description = "积分兑换--创建实体积分兑换-兑换价格异常")
    public void integralExchange_system_24() {
        try {
            String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            //获取商品id
            long goodsId = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
            JSONArray specificationDetailList = CommoditySpecificationsListScene.builder().id(goodsId).build().invoke(visitor).getJSONArray("specification_detail_list");
            JSONArray specificationList = new JSONArray(specificationDetailList.stream().map(e -> (JSONObject) e).map(e -> put(e.getInteger("id"), 2)).collect(Collectors.toList()));
            String[] exchangePriceList = {"", null, "1.11", "-3", "中文", "english"};
            Arrays.stream(exchangePriceList).forEach(exchangePrice -> {
                //创建积分兑换
                IScene scene = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId)
                        .exchangePrice(exchangePrice).isLimit(true).exchangePeopleNum("10").specificationList(specificationList).expireType(2).useDays("10")
                        .exchangeStartTime(exchangeStartTime).exchangeEndTime(exchangeEndTime).build();
                String message = scene.getResponse(visitor).getMessage();
                String err = StringUtils.isEmpty(exchangePrice) ? "兑换价格不能为空" : exchangePrice.compareTo("0") < 0 ? "兑换价格必须大于等于0" : "请求入参类型不正确";
                CommonUtil.checkResult("兑换价格为：" + exchangePrice, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--创建实体积分兑换-兑换价格异常");
        }
    }

    //bug能创建成功
    @Test(description = "积分兑换--创建实体积分兑换-结束时间大于开始时间")
    public void integralExchange_system_25() {
        Long exchangeId = null;
        try {
            String exchangeStartTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
            String exchangeEndTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
            long goodsId = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
            JSONArray specificationDetailList = CommoditySpecificationsListScene.builder().id(goodsId).build().invoke(visitor).getJSONArray("specification_detail_list");
            JSONArray specificationList = new JSONArray(specificationDetailList.stream().map(e -> (JSONObject) e).map(e -> put(e.getInteger("id"), 2)).collect(Collectors.toList()));
            IScene scene = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.REAL.name()).goodsId(goodsId)
                    .exchangePrice("1").isLimit(true).exchangePeopleNum("10").specificationList(specificationList).expireType(2).useDays("10")
                    .exchangeStartTime(exchangeStartTime).exchangeEndTime(exchangeEndTime).build();
            String message = scene.getResponse(visitor).getMessage();
            String err = "";
            ExchangePage exchangePage = util.toJavaObjectList(ExchangePageScene.builder().build(), ExchangePage.class).get(0);
            exchangeId = exchangePage.getId();
            CommonUtil.checkResult("结束时间小于开始时间", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            ChangeSwitchStatusScene.builder().id(exchangeId).status(false).build().invoke(visitor);
            DeleteExchangeGoodsScene.builder().id(exchangeId).build().invoke(visitor);
            saveData("积分兑换--创建实体积分兑换-结束时间大于开始时间");
        }
    }

    //bug有效天数为空没校验
    @Test(description = "积分兑换--创建虚拟兑换商品时，优惠券有效期异常")
    public void integralExchange_system_26() {
        try {
            Long voucherId = new VoucherGenerator.Builder().status(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId();
            VoucherFormVoucherPageBean voucherPage = util.getVoucherPage(voucherId);
            //创建积分兑换
            String[] useDaysList = {"3651", "10.5"};
            Arrays.stream(useDaysList).forEach(useDays -> {
                String exchangeStartTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
                String exchangeEndTime = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), 30), "yyyy-MM-dd HH:mm:ss");
                IScene scene = CreateExchangeGoodsScene.builder().exchangeGoodsType(CommodityTypeEnum.FICTITIOUS.name()).goodsId(voucherId)
                        .exchangePrice("1").isLimit(true).exchangePeopleNum("10").exchangeStartTime(exchangeStartTime).expireType(2)
                        .exchangeEndTime(exchangeEndTime).useDays(useDays).exchangeNum(String.valueOf(voucherPage.getAllowUseInventory()))
                        .build();
                String message = scene.getResponse(visitor).getMessage();
                String err = StringUtils.isEmpty(useDays) ? "" : useDays.compareTo("3650") > 0 ? "使用时间不能超过3650天" : "请求入参类型不正确";
                CommonUtil.checkResult("优惠券有效期为：" + useDays, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("积分兑换--创建虚拟兑换商品时，优惠券有效期异常");
        }
    }

    //ok
    @Test(description = "积分订单--订单明细--实付总积分=商品积分*兑换数量")
    public void integralOrder_data_1() {
        try {
            IScene exchangeOrderScene = ExchangeOrderScene.builder().build();
            List<JSONObject> jsonObjectList = util.toJavaObjectList(exchangeOrderScene, JSONObject.class, SceneUtil.SIZE);
            jsonObjectList.forEach(e -> {
                int id = e.getInteger("id");
                IScene orderDetailScene = OrderDetailScene.builder().id(id).build();
                JSONObject response = orderDetailScene.invoke(visitor);
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

    //bug 积分数对不上
    @Test(description = "积分明细--各个积分规则所得积分相加=此规则的已发放积分")
    public void integralRule_data_1() {
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

    //ok
    @Test(description = "积分明细--各个积分规则的奖励积分=该规则的单笔发放积分")
    public void integralRule_data_2() {
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
    public void integralRule_data_3() {
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
    public void integralRule_data_4() {
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
    public void integralRule_data_5() {
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
    public void integralRule_data_6() {
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
        List<JSONObject> integralExchangeRulesList = util.toJavaObjectList(integralExchangeRulesScene, JSONObject.class);
        int singleSend = integralExchangeRulesList.stream().filter(e -> e.getString("rule_name").contains(ruleName)).map(e -> e.getInteger("single_send")).findFirst().orElse(0);
        List<JSONObject> sceneList = util.toJavaObjectList(scene, JSONObject.class);
        int awardScore = 0;
        if (scene instanceof SignInConfigPageScene) {
            awardScore = util.toJavaObjectList(scene, JSONObject.class).stream().map(e -> e.getInteger("award_score")).findFirst().orElse(0);
        } else if (scene instanceof EquityPageScene) {
            awardScore = sceneList.stream().filter(e -> ruleName.contains(e.getString("equity_name")) && ruleName.contains(e.getString("service_type_name"))).map(e -> e.getInteger("award_count")).findFirst().orElse(0);
        } else if (scene instanceof ShareManagerPageScene) {
            awardScore = sceneList.stream().filter(e -> e.getString("taskName").contains(ruleName)).map(e -> e.getInteger("award_score")).findFirst().orElse(0);
        }
        integralRule.setAwardScore(awardScore);
        integralRule.setSingleSend(singleSend);
        return integralRule;
    }
}
