package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm.testcase;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 积分中心测试用例
 */
public class IntegralCenterCaseOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_ONLINE;
    private static final EnumAccount ADMINISTRATOR = EnumAccount.ADMINISTRATOR_ONLINE;
    //小程序用户
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_ONLINE;
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

    //日常有问题
    @Test(description = "积分兑换--库存详情--当前库存=兑换品库存明细加和")
    public void IntegralOrder_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene exchangePageScene = ExchangePageScene.builder().build();
            List<JSONObject> exchangePageList = util.collectBean(exchangePageScene, JSONObject.class);
            exchangePageList.forEach(e -> {
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

}
