package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletVoucherInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.AfterSaleCustomerPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ReceptionPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherSendRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.WechatCustomerPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.CustomerLabelTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherSourceEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherUseStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.AfterSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.RepairPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.WechatCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.BuyPackageRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.ReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherInfoScene;
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
import java.util.stream.Collectors;

/**
 * 业务管理测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class BusinessManageCase extends TestCaseCommon implements TestCaseStd {
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
    @Test(description = "接待管理--购买一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1")
    public void receptionManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone()).build();
            ReceptionPage receptionPage = util.collectBean(receptionPageScene, ReceptionPage.class).get(0);
            String platNumber = receptionPage.getPlateNumber();
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //购买前数据
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = visitor.invokeApi(buyPackageRecordScene).getInteger("total");
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            //购买临时套餐
            user.loginPc(ADMINISTRATOR);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            util.receptionBuyTemporaryPackage(voucherList, 1);
            util.makeSureBuyPackage("临时套餐");
            //购买后数据
            CommonUtil.checkResult("套餐购买记录", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherName).get(0);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.COMMON.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum + 1, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum, util.getAppletPackageNum());
            String voucherCode = voucherSendRecord.getVoucherCode();
            AppletVoucherInfo appletVoucherInfo = util.getAppletVoucherInfo(voucherCode);
            CommonUtil.checkResult("小程序卡券指定车辆", platNumber, appletVoucherInfo.getPlateNumber());
            CommonUtil.checkResult("小程序卡券状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), appletVoucherInfo.getStatusName());
            CommonUtil.checkResult("小程序卡券状态", "NEAR_EXPIRED", appletVoucherInfo.getStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--购买一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1");
        }
    }

    //ok
    @Test(description = "接待管理--购买一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1")
    public void receptionManage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone()).build();
            ReceptionPage receptionPage = util.collectBean(receptionPageScene, ReceptionPage.class).get(0);
            String platNumber = receptionPage.getPlateNumber();
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            String packageName = util.editPackage(voucherList);
            Long packageId = util.getPackageId(packageName);
            //购买前数据
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = visitor.invokeApi(buyPackageRecordScene).getInteger("total");
            int soldNumber = util.getPackagePage(packageName).getSoldNumber();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletMessageMessageNum = util.getAppletMessageNum();
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            user.loginPc(ADMINISTRATOR);
            //购买固定套餐
            util.receptionBuyFixedPackage(packageId, 1);
            //确认支付
            util.makeSureBuyPackage(packageName);
            //购买后数据
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            CommonUtil.checkResult(packageName + " 售出（套）", soldNumber + 1, util.getPackagePage(packageName).getSoldNumber());
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherName).get(0);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.COMMON.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 购买后剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum + 1, util.getAppletPackageNum());
            CommonUtil.checkResult("小程序我的消息数", appletMessageMessageNum + 1, util.getAppletMessageNum());
            String voucherCode = voucherSendRecord.getVoucherCode();
            AppletVoucherInfo appletVoucherInfo = util.getAppletPackageVoucherInfo(voucherCode);
            CommonUtil.checkResult("小程序卡券指定车辆", platNumber, appletVoucherInfo.getPlateNumber());
            CommonUtil.checkResult("小程序卡券状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), appletVoucherInfo.getStatusName());
            CommonUtil.checkResult("小程序卡券状态", "NEAR_EXPIRED", appletVoucherInfo.getStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--购买一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1");
        }
    }

    //ok
    @Test(description = "接待管理--赠送一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1")
    public void receptionManage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone()).build();
            ReceptionPage receptionPage = util.collectBean(receptionPageScene, ReceptionPage.class).get(0);
            String platNumber = receptionPage.getPlateNumber();
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //购买前数据
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = visitor.invokeApi(buyPackageRecordScene).getInteger("total");
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            //赠送临时套餐
            user.loginPc(ADMINISTRATOR);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            util.receptionBuyTemporaryPackage(voucherList, 0);
            util.makeSureBuyPackage("临时套餐");
            //购买后数据
            CommonUtil.checkResult("套餐购买记录", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherName).get(0);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.COMMON.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum + 1, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum, util.getAppletPackageNum());
            String voucherCode = voucherSendRecord.getVoucherCode();
            AppletVoucherInfo appletVoucherInfo = util.getAppletVoucherInfo(voucherCode);
            CommonUtil.checkResult("小程序卡券指定车辆", platNumber, appletVoucherInfo.getPlateNumber());
            CommonUtil.checkResult("小程序卡券状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), appletVoucherInfo.getStatusName());
            CommonUtil.checkResult("小程序卡券状态", "NEAR_EXPIRED", appletVoucherInfo.getStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--赠送一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1");
        }
    }

    //ok
    @Test(description = "接待管理--赠送一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1")
    public void receptionManage_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone()).build();
            ReceptionPage receptionPage = util.collectBean(receptionPageScene, ReceptionPage.class).get(0);
            String platNumber = receptionPage.getPlateNumber();
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            String packageName = util.editPackage(voucherList);
            Long packageId = util.getPackageId(packageName);
            //购买前数据
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = visitor.invokeApi(buyPackageRecordScene).getInteger("total");
            int giveNumber = util.getPackagePage(packageName).getGiveNumber();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletMessageMessageNum = util.getAppletMessageNum();
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            user.loginPc(ADMINISTRATOR);
            //赠送固定套餐
            util.receptionBuyFixedPackage(packageId, 0);
            //确认支付
            util.makeSureBuyPackage(packageName);
            //购买后数据
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            CommonUtil.checkResult(packageName + " 赠送（套）", giveNumber + 1, util.getPackagePage(packageName).getGiveNumber());
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherName).get(0);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.COMMON.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 购买后剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum + 1, util.getAppletPackageNum());
            CommonUtil.checkResult("小程序我的消息数", appletMessageMessageNum + 1, util.getAppletMessageNum());
            String voucherCode = voucherSendRecord.getVoucherCode();
            AppletVoucherInfo appletVoucherInfo = util.getAppletPackageVoucherInfo(voucherCode);
            CommonUtil.checkResult("小程序卡券指定车辆", platNumber, appletVoucherInfo.getPlateNumber());
            CommonUtil.checkResult("小程序卡券状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), appletVoucherInfo.getStatusName());
            CommonUtil.checkResult("小程序卡券状态", "NEAR_EXPIRED", appletVoucherInfo.getStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--赠送一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1");
        }
    }

    //ok
    @Test(description = "接待管理--赠送一个固定套餐，取消后卡券&套餐数量不变")
    public void receptionManage_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            String packageName = util.editPackage(voucherList);
            Long packageId = util.getPackageId(packageName);
            //购买前数据
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = visitor.invokeApi(buyPackageRecordScene).getInteger("total");
            int giveNumber = util.getPackagePage(packageName).getGiveNumber();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            user.loginPc(ADMINISTRATOR);
            //赠送固定套餐
            util.receptionBuyFixedPackage(packageId, 0);
            //取消支付
            util.cancelSoldPackage(packageName);
            //购买后数据
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            CommonUtil.checkResult(packageName + " 赠送（套）", giveNumber, util.getPackagePage(packageName).getGiveNumber());
            CommonUtil.checkResult(voucherName + " 购买后剩余库存", surplusInventory, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--赠送一个固定套餐，取消后卡券&套餐数量不变");
        }
    }

    //ok
    @Test(description = "接待管理--赠送一个临时套餐，取消后卡券&套餐数量不变")
    public void receptionManage_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //购买前数据
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = visitor.invokeApi(buyPackageRecordScene).getInteger("total");
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            //赠送临时套餐
            user.loginPc(ADMINISTRATOR);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            util.receptionBuyTemporaryPackage(voucherList, 0);
            util.cancelSoldPackage("临时套餐");
            //购买后数据
            CommonUtil.checkResult("套餐购买记录", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("接待管理--赠送一个临时套餐，取消后卡券&套餐数量不变");
        }
    }

    @Test(description = "套餐购买并发测试", threadPoolSize = 4, invocationCount = 4, enabled = false)
    public void receptionManage_data_7() {
        try {
            long packageId = 146;
            util.receptionBuyFixedPackage(packageId, 1);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐购买并发测试");
        }
    }

    //逻辑不对
    @Test(description = "客户管理--【小程序客户】对应的总金额=对应手机号的【售后客户】的维修记录的产值之和", enabled = false)
    public void customerManager_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene wechatCustomerPageScene = WechatCustomerPageScene.builder().build();
            List<WechatCustomerPage> wechatCustomerPageList = util.collectBean(wechatCustomerPageScene, WechatCustomerPage.class);
            wechatCustomerPageList.forEach(wechatCustomerPage -> {
                CommonUtil.valueView(wechatCustomerPage.getCustomerName());
                String phone = wechatCustomerPage.getCustomerPhone();
                Double price = wechatCustomerPage.getTotalPrice() == null ? 0 : wechatCustomerPage.getTotalPrice();
                IScene afterSaleCustomerPageScene = AfterSaleCustomerPageScene.builder().customerPhone(phone).build();
                List<JSONObject> jsonObjectList = util.collectBean(afterSaleCustomerPageScene, JSONObject.class);
                Double priceLitSum = jsonObjectList.stream().map(e -> e.getDouble("total_price") == null ? 0 : e.getDouble("total_price")).collect(Collectors.toList()).stream().mapToDouble(e -> e).sum();
                CommonUtil.checkResultPlus(wechatCustomerPage.getCustomerPhone() + " 总金额", price, "产值之和", priceLitSum);
                CommonUtil.logger(wechatCustomerPage.getCustomerName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("客户管理--【小程序客户】对应的总金额=对应手机号的【售后客户】的维修记录的产值之和");
        }
    }

    //ok
    @Test(description = "客户管理--相同底盘号的客户最新里程数相等")
    public void customerManager_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene afterSaleCustomerPageScene = AfterSaleCustomerPageScene.builder().build();
            List<AfterSaleCustomerPage> afterSaleCustomerPageList = util.collectBean(afterSaleCustomerPageScene, AfterSaleCustomerPage.class);
            afterSaleCustomerPageList.forEach(afterSaleCustomerPage -> {
                String vehicleChassisCode = afterSaleCustomerPage.getVehicleChassisCode();
                if (vehicleChassisCode != null) {
                    IScene afterSaleCustomerPageScene1 = AfterSaleCustomerPageScene.builder().vehicleChassisCode(vehicleChassisCode).build();
                    List<AfterSaleCustomerPage> afterSaleCustomerPageList1 = util.collectBean(afterSaleCustomerPageScene1, AfterSaleCustomerPage.class);
                    List<Integer> miles = afterSaleCustomerPageList1.stream().map(AfterSaleCustomerPage::getNewestMiles).collect(Collectors.toList());
                    for (int i = 0; i < miles.size() - 1; i++) {
                        CommonUtil.checkResult(vehicleChassisCode + " 最新里程数", miles.get(i), miles.get(i + 1));
                        CommonUtil.logger(vehicleChassisCode);
                    }
                }
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("客户管理--相同底盘号的客户最新里程数相等");
        }
    }

    //逻辑有问题
    @Test(description = "客户管理--有维修记录的售后客户，列表最新里程数=维修记录中最新的里程数&总消费/元=维修记录产值/mb之和", enabled = false)
    public void customerManager_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene afterSaleCustomerPageScene = AfterSaleCustomerPageScene.builder().build();
            List<AfterSaleCustomerPage> afterSaleCustomerPageList = util.collectBean(afterSaleCustomerPageScene, AfterSaleCustomerPage.class);
            afterSaleCustomerPageList.forEach(afterSaleCustomerPage -> {
                IScene repairPageScene = RepairPageScene.builder().carId(String.valueOf(afterSaleCustomerPage.getCarId())).shopId(String.valueOf(afterSaleCustomerPage.getShopId())).build();
                JSONArray list = visitor.invokeApi(repairPageScene).getJSONArray("list");
                if (!list.isEmpty()) {
                    int newestMiles = list.stream().map(object -> (JSONObject) object).map(e -> e.getInteger("newest_miles")).findFirst().orElse(0);
                    double outputValue = list.stream().map(object -> (JSONObject) object).map(e -> e.getDouble("output_value")).mapToDouble(object -> object).sum();
                    CommonUtil.checkResultPlus(afterSaleCustomerPage.getVehicleChassisCode() + " 列表最新里程数", afterSaleCustomerPage.getNewestMiles(), "维修记录中最新的里程数", newestMiles);
                    CommonUtil.checkResultPlus(afterSaleCustomerPage.getVehicleChassisCode() + " 列表最总消费/元", afterSaleCustomerPage.getTotalPrice(), "维修记录中最新的产值/mb之和", outputValue);
                    CommonUtil.logger(afterSaleCustomerPage.getVehicleChassisCode());
                }
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("客户管理--有维修记录的售后客户，列表最新里程数=维修记录中最新的里程数&总消费/元=维修记录产值/mb之和");
        }
    }
}
