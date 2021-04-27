package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletVoucherInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.customermanage.PreSaleCustomerInfoBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.customermanage.PreSaleCustomerPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.CustomerLabelTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common.EnableStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherSourceEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherUseStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.BuyPackageRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.PackageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherInfoScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 业务管理测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class BusinessManageCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    public CommonConfig commonConfig = new CommonConfig();

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
        commonConfig.shopId = PRODUCE.getShopId();
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
        user.loginPc(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    //ok
    @Test(description = "售后接待管理--购买一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1")
    public void receptionManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
            ReceptionPage receptionPage = util.collectBeanList(receptionPageScene, ReceptionPage.class).get(0);
            String platNumber = receptionPage.getPlateNumber();
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //购买前数据
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = buyPackageRecordScene.invoke(visitor).getInteger("total");
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = voucherInfoScene.invoke(visitor).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            //购买临时套餐
            user.loginPc(ALL_AUTHORITY);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            util.receptionBuyTemporaryPackage(voucherList, 1);
            VoucherPage secondVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 购买后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 购买后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //确认支付
            util.makeSureBuyPackage("临时套餐");
            //支付后数据
            CommonUtil.checkResult("套餐购买记录", buyPackageRecordTotal + 1, buyPackageRecordScene.invoke(visitor).getInteger("total"));
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherId).get(0);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            VoucherPage thirdVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 确认支付后可用库存", secondVoucherPage.getAllowUseInventory(), thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 确认支付后剩余库存", secondVoucherPage.getSurplusInventory() - 1, thirdVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, voucherInfoScene.invoke(visitor).getInteger("total_send"));
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
            saveData("售后接待管理--购买一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1");
        }
    }

    //ok
    @Test(description = "售后接待管理--购买一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1")
    public void receptionManage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
            ReceptionPage receptionPage = util.collectBeanList(receptionPageScene, ReceptionPage.class).get(0);
            String platNumber = receptionPage.getPlateNumber();
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Long packageId = PackageListScene.builder().build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("package_id");
            String packageName = util.editPackage(packageId, voucherList);
            //购买前数据
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = buyPackageRecordScene.invoke(visitor).getInteger("total");
            int soldNumber = util.getPackagePage(packageId).getSoldNumber();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = voucherInfoScene.invoke(visitor).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletMessageMessageNum = util.getAppletMessageNum();
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            user.loginPc(ALL_AUTHORITY);
            //购买固定套餐
            util.receptionBuyFixedPackage(packageId, 1);
            VoucherPage secondVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 购买后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 购买后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //确认支付
            util.makeSureBuyPackage(packageName);
            //支付后数据
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            CommonUtil.checkResult(packageName + " 售出（套）", soldNumber + 1, util.getPackagePage(packageId).getSoldNumber());
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherId).get(0);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            VoucherPage thirdVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 确认支付后可用库存", secondVoucherPage.getAllowUseInventory(), thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 确认支付后剩余库存", secondVoucherPage.getSurplusInventory() - 1, thirdVoucherPage.getSurplusInventory());
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
            saveData("售后接待管理--购买一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1");
        }
    }

    //ok
    @Test(description = "售后接待管理--赠送一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1")
    public void receptionManage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
            ReceptionPage receptionPage = util.collectBeanList(receptionPageScene, ReceptionPage.class).get(0);
            String platNumber = receptionPage.getPlateNumber();
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //赠送前数据
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = buyPackageRecordScene.invoke(visitor).getInteger("total");
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = voucherInfoScene.invoke(visitor).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            //赠送临时套餐
            user.loginPc(ALL_AUTHORITY);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            util.receptionBuyTemporaryPackage(voucherList, 0);
            VoucherPage secondVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 赠送后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 赠送后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //赠送
            util.makeSureBuyPackage("临时套餐");
            //确认后数据
            CommonUtil.checkResult("套餐购买记录", buyPackageRecordTotal + 1, buyPackageRecordScene.invoke(visitor).getInteger("total"));
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherId).get(0);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            VoucherPage thirdVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 确认支付后可用库存", secondVoucherPage.getAllowUseInventory(), thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 确认支付后剩余库存", secondVoucherPage.getSurplusInventory() - 1, thirdVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, voucherInfoScene.invoke(visitor).getInteger("total_send"));
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
            saveData("售后接待管理--赠送一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1");
        }
    }

    //ok
    @Test(description = "售后接待管理--赠送一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1")
    public void receptionManage_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
            ReceptionPage receptionPage = util.collectBeanList(receptionPageScene, ReceptionPage.class).get(0);
            String platNumber = receptionPage.getPlateNumber();
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Long packageId = PackageListScene.builder().build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("package_id");
            String packageName = util.editPackage(packageId, voucherList);
            //赠送前数据
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = buyPackageRecordScene.invoke(visitor).getInteger("total");
            int giveNumber = util.getPackagePage(packageId).getGiveNumber();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = voucherInfoScene.invoke(visitor).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletMessageMessageNum = util.getAppletMessageNum();
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            user.loginPc(ALL_AUTHORITY);
            //赠送固定套餐
            util.receptionBuyFixedPackage(packageId, 0);
            VoucherPage secondVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 赠送后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 赠送后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //确认支付
            util.makeSureBuyPackage(packageName);
            //购买后数据
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            CommonUtil.checkResult(packageName + " 赠送（套）", giveNumber + 1, util.getPackagePage(packageName).getGiveNumber());
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherId).get(0);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            VoucherPage thirdVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 确认支付后可用库存", secondVoucherPage.getAllowUseInventory(), thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 确认支付后剩余库存", secondVoucherPage.getSurplusInventory() - 1, thirdVoucherPage.getSurplusInventory());
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
            saveData("售后接待管理--赠送一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1");
        }
    }

    //ok
    @Test(description = "售后接待管理--赠送一个固定套餐，取消后卡券&套餐数量不变")
    public void receptionManage_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Long packageId = PackageListScene.builder().build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("package_id");
            String packageName = util.editPackage(packageId, voucherList);
            //赠送前数据
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = buyPackageRecordScene.invoke(visitor).getInteger("total");
            int giveNumber = util.getPackagePage(packageId).getGiveNumber();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = voucherInfoScene.invoke(visitor).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            user.loginPc(ALL_AUTHORITY);
            //赠送固定套餐
            util.receptionBuyFixedPackage(packageId, 0);
            VoucherPage secondVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 赠送后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 赠送后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //取消支付
            util.cancelSoldPackage(packageName);
            //取消后数据
            PackagePage packagePage = util.getPackagePage(packageId);
            VoucherPage thirdVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 取消后可用库存", secondVoucherPage.getAllowUseInventory() + 1, thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 取消后剩余库存", secondVoucherPage.getSurplusInventory(), thirdVoucherPage.getSurplusInventory());
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal + 1, buyPackageRecordScene.invoke(visitor).getInteger("total"));
            CommonUtil.checkResult(packageName + " 赠送（套）", giveNumber, packagePage.getGiveNumber());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend, voucherInfoScene.invoke(visitor).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("售后接待管理--赠送一个固定套餐，取消后卡券&套餐数量不变");
        }
    }

    //ok
    @Test(description = "售后接待管理--赠送一个临时套餐，取消后卡券&套餐数量不变")
    public void receptionManage_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //赠送前数据
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = visitor.invokeApi(buyPackageRecordScene).getInteger("total");
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            //赠送临时套餐
            user.loginPc(ALL_AUTHORITY);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            util.receptionBuyTemporaryPackage(voucherList, 0);
            VoucherPage secondVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 赠送后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 赠送后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //取消
            util.cancelSoldPackage("临时套餐");
            //取消后数据
            CommonUtil.checkResult("套餐购买记录", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            VoucherPage thirdVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 取消后可用库存", secondVoucherPage.getAllowUseInventory() + 1, thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 取消后剩余库存", secondVoucherPage.getSurplusInventory(), thirdVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("售后接待管理--赠送一个临时套餐，取消后卡券&套餐数量不变");
        }
    }

    @Test(description = "售后接待管理--套餐购买并发测试", threadPoolSize = 4, invocationCount = 4, enabled = false)
    public void receptionManage_data_7() {
        try {
            long packageId = 146;
            util.receptionBuyFixedPackage(packageId, 1);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("售后接待管理--套餐购买并发测试");
        }
    }

    //ok
    @Test(description = "售后客户管理--相同底盘号的客户最新里程数相等")
    public void afterSaleCustomerManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene afterSaleCustomerPageScene = AfterSaleCustomerPageScene.builder().build();
            List<AfterSaleCustomerPage> afterSaleCustomerPageList = util.collectBeanList(afterSaleCustomerPageScene, AfterSaleCustomerPage.class);
            afterSaleCustomerPageList.forEach(afterSaleCustomerPage -> {
                String vehicleChassisCode = afterSaleCustomerPage.getVehicleChassisCode();
                if (vehicleChassisCode != null) {
                    IScene afterSaleCustomerPageScene1 = AfterSaleCustomerPageScene.builder().vehicleChassisCode(vehicleChassisCode).build();
                    List<AfterSaleCustomerPage> afterSaleCustomerPageList1 = util.collectBeanList(afterSaleCustomerPageScene1, AfterSaleCustomerPage.class);
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
            saveData("售后客户管理--相同底盘号的客户最新里程数相等");
        }
    }

    //逻辑有问题
    @Test(description = "售后客户管理--有维修记录的售后客户，列表最新里程数=维修记录中最新的里程数&总消费/元=维修记录产值/mb之和", enabled = false)
    public void afterSaleCustomerManager_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene afterSaleCustomerPageScene = AfterSaleCustomerPageScene.builder().build();
            List<AfterSaleCustomerPage> afterSaleCustomerPageList = util.collectBeanList(afterSaleCustomerPageScene, AfterSaleCustomerPage.class);
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
            saveData("售后客户管理--有维修记录的售后客户，列表最新里程数=维修记录中最新的里程数&总消费/元=维修记录产值/mb之和");
        }
    }

    //3.1
    @Test(description = "售后售后客户管理--编辑售后客户各项信息，必填项不填，全部失败", enabled = false)
    public void afterSaleCustomerManager_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = PreSaleCustomerPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
            PreSaleCustomerPageBean customerPage = util.collectBeanList(scene, PreSaleCustomerPageBean.class).get(0);
            IScene scene1 = PreSaleCustomerInfoScene.builder().customerId(customerPage.getCustomerId()).shopId(customerPage.getShopId()).build();
            PreSaleCustomerInfoBean customerInfo = util.collectBean(scene1, PreSaleCustomerInfoBean.class);
            IScene scene2 = PreSaleCustomerEditScene.builder()
                    .shopId(customerInfo.getShopId())
                    .customerId(customerInfo.getCustomerId())
                    .subjectType(customerInfo.getSubjectType())
                    .customerPhone(customerInfo.getCustomerPhone())
                    .customerName(customerInfo.getCustomerName())
                    .sex(customerInfo.getSex())
                    .carModelId(customerInfo.getIntentionCarModelId())
                    .carStyleId(customerInfo.getIntentionCarStyleId())
                    .build();
            String[] messageList = util.getMessageList(scene2);
            System.err.println(Arrays.toString(messageList));
            String[] errList = {"客户姓名不能为空", "客户性别不能为空"};
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("售后售后客户管理--编辑售后客户各项信息，必填项不填，全部失败");
        }
    }

    //ok
    @Test(description = "预约管理--保养配置，修改保养价格")
    public void customerManager_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId = ALL_AUTHORITY.getReceptionShopId();
            Double[] doubles = {1.00, 2.99, 3.66, 50.1};
            JSONObject jsonObject = ManageModelPageScene.builder().type("MAINTAIN").build().invoke(visitor).getJSONArray("list").getJSONObject(0);
            Long id = jsonObject.getLong("id");
            Double price = Arrays.stream(doubles).filter(e -> !e.equals(jsonObject.getDouble("price"))).findFirst().orElse(jsonObject.getDouble("price"));
            ManageModelEditScene.builder().id(id).price(price).status(EnableStatusEnum.DISABLE.name()).type("MAINTAIN").build().invoke(visitor);
            JSONObject newJSONObject = ManageModelPageScene.builder().type("MAINTAIN").build().invoke(visitor).getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getLong("id").equals(id)).findFirst().orElse(null);
            CommonUtil.checkResult(id + "保养价格", price, Objects.requireNonNull(newJSONObject).getDouble("price"));
            CommonUtil.checkResult(id + "预约状态", EnableStatusEnum.DISABLE.name(), Objects.requireNonNull(newJSONObject).getString("status"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.shopId = PRODUCE.getShopId();
            saveData("预约管理--保养配置，修改保养价格");
        }
    }
}
