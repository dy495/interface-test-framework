package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletVoucherInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.PackagePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ReceptionPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherSendRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.customermanage.AfterSaleCustomerInfoBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.customermanage.AfterSaleCustomerPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.receptionmanage.PackageListBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherFormVoucherPageBean;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.CustomerLabelTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common.EnableStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherSourceEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherUseStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage.AfterSaleCustomerEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage.AfterSaleCustomerInfoScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage.AfterSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage.RepairPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.BuyPackageRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.PackageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherInfoScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 业务管理测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class BusinessManageCaseOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount ACCOUNT = EnumAccount.JC_ONLINE_LXQ;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_ONLINE;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCE);
    private final SceneUtil util = new SceneUtil(visitor);
    private final CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
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
        commonConfig.setShopId(ACCOUNT.getShopId()).setRoleId(ACCOUNT.getRoleId()).setReferer(PRODUCE.getReferer()).setProduct(PRODUCE.getAbbreviation());
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
        util.loginPc(ACCOUNT);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        logger.logCaseStart(caseResult.getCaseName());
    }

    //ok
    @Test(description = "售后接待管理--购买一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1")
    public void receptionManage_data_1() {
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
            ReceptionPage receptionPage = util.toFirstJavaObject(receptionPageScene, ReceptionPage.class);
            String platNumber = receptionPage.getPlateNumber();
            VoucherFormVoucherPageBean voucherPage = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherPage();
            Long voucherId = voucherPage.getVoucherId();
            String voucherName = voucherPage.getVoucherName();
            //购买前数据
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = buyPackageRecordScene.visitor(visitor).execute().getInteger("total");
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = voucherInfoScene.visitor(visitor).execute().getInteger("total_send");
            util.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            //购买临时套餐
            util.loginPc(ACCOUNT);
            JSONArray voucherList = util.getVoucherArray(voucherPage, 1);
            util.receptionBuyTemporaryPackage(voucherList, 1);
            VoucherFormVoucherPageBean secondVoucherPage = util.flushVoucherPage(voucherPage);
            CommonUtil.checkResult(voucherName + " 购买后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 购买后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //确认支付
            util.makeSureBuyPackage("临时套餐");
            //支付后数据
            CommonUtil.checkResult("套餐购买记录", buyPackageRecordTotal + 1, buyPackageRecordScene.visitor(visitor).execute().getInteger("total"));
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecord(voucherId);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            VoucherFormVoucherPageBean thirdVoucherPage = util.flushVoucherPage(secondVoucherPage);
            CommonUtil.checkResult(voucherName + " 确认支付后可用库存", secondVoucherPage.getAllowUseInventory(), thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 确认支付后剩余库存", secondVoucherPage.getSurplusInventory() - 1, thirdVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, voucherInfoScene.visitor(visitor).execute().getInteger("total_send"));
            util.loginApplet(APPLET_USER_ONE);
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
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
            ReceptionPage receptionPage = util.toFirstJavaObject(receptionPageScene, ReceptionPage.class);
            String platNumber = receptionPage.getPlateNumber();
            VoucherFormVoucherPageBean voucherPage = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherPage();
            Long voucherId = voucherPage.getVoucherId();
            String voucherName = voucherPage.getVoucherName();
            JSONArray voucherList = util.getVoucherArray(voucherPage, 1);
            IScene packageListScene = PackageListScene.builder().build();
            Long packageId = util.toFirstJavaObject(packageListScene, PackageListBean.class).getPackageId();
            String packageName = util.editPackage(packageId, voucherList);
            //购买前数据
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = buyPackageRecordScene.visitor(visitor).execute().getInteger("total");
            PackagePage packagePage = util.getPackagePage(packageName, packageId);
            int soldNumber = packagePage.getSoldNumber();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = voucherInfoScene.visitor(visitor).execute().getInteger("total_send");
            util.loginApplet(APPLET_USER_ONE);
            int appletMessageMessageNum = util.getAppletMessageNum();
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            util.loginPc(ACCOUNT);
            //购买固定套餐
            util.receptionBuyFixedPackage(packageId, 1);
            VoucherFormVoucherPageBean secondVoucherPage = util.flushVoucherPage(voucherPage);
            CommonUtil.checkResult(voucherName + " 购买后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 购买后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //确认支付
            util.makeSureBuyPackage(packageName);
            //支付后数据
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal + 1, buyPackageRecordScene.visitor(visitor).execute().getInteger("total"));
            CommonUtil.checkResult(packageName + " 售出（套）", soldNumber + 1, util.getPackagePage(packageName, packageId).getSoldNumber());
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecord(voucherId);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            VoucherFormVoucherPageBean thirdVoucherPage = util.flushVoucherPage(secondVoucherPage);
            CommonUtil.checkResult(voucherName + " 确认支付后可用库存", secondVoucherPage.getAllowUseInventory(), thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 确认支付后剩余库存", secondVoucherPage.getSurplusInventory() - 1, thirdVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, voucherInfoScene.visitor(visitor).execute().getInteger("total_send"));
            util.loginApplet(APPLET_USER_ONE);
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
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
            ReceptionPage receptionPage = util.toJavaObjectList(receptionPageScene, ReceptionPage.class).get(0);
            String platNumber = receptionPage.getPlateNumber();
            VoucherFormVoucherPageBean voucherPage = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherPage();
            Long voucherId = voucherPage.getVoucherId();
            String voucherName = voucherPage.getVoucherName();
            //赠送前数据
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = buyPackageRecordScene.visitor(visitor).execute().getInteger("total");
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = voucherInfoScene.visitor(visitor).execute().getInteger("total_send");
            util.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            //赠送临时套餐
            util.loginPc(ACCOUNT);
            JSONArray voucherList = util.getVoucherArray(voucherPage, 1);
            util.receptionBuyTemporaryPackage(voucherList, 0);
            VoucherFormVoucherPageBean secondVoucherPage = util.flushVoucherPage(voucherPage);
            CommonUtil.checkResult(voucherName + " 赠送后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 赠送后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //赠送
            util.makeSureBuyPackage("临时套餐");
            //确认后数据
            CommonUtil.checkResult("套餐购买记录", buyPackageRecordTotal + 1, buyPackageRecordScene.visitor(visitor).execute().getInteger("total"));
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecord(voucherId);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            VoucherFormVoucherPageBean thirdVoucherPage = util.flushVoucherPage(secondVoucherPage);
            CommonUtil.checkResult(voucherName + " 确认支付后可用库存", secondVoucherPage.getAllowUseInventory(), thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 确认支付后剩余库存", secondVoucherPage.getSurplusInventory() - 1, thirdVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, voucherInfoScene.visitor(visitor).execute().getInteger("total_send"));
            util.loginApplet(APPLET_USER_ONE);
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
        try {
            IScene receptionPageScene = ReceptionPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
            ReceptionPage receptionPage = util.toFirstJavaObject(receptionPageScene, ReceptionPage.class);
            String platNumber = receptionPage.getPlateNumber();
            VoucherFormVoucherPageBean voucherPage = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherPage();
            String voucherName = voucherPage.getVoucherName();
            Long voucherId = voucherPage.getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherPage, 1);
            PackagePage packagePage = util.toFirstJavaObject(PackageListScene.builder().build(), PackagePage.class);
            Long packageId = packagePage.getPackageId();
            String packageName = util.editPackage(packageId, voucherList);
            //赠送前数据
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = buyPackageRecordScene.visitor(visitor).execute().getInteger("total");
            int giveNumber = util.getPackagePage(packageName, packageId).getGiveNumber();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = voucherInfoScene.visitor(visitor).execute().getInteger("total_send");
            util.loginApplet(APPLET_USER_ONE);
            int appletMessageMessageNum = util.getAppletMessageNum();
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            util.loginPc(ACCOUNT);
            //赠送固定套餐
            util.receptionBuyFixedPackage(packageId, 0);
            VoucherFormVoucherPageBean secondVoucherPage = util.flushVoucherPage(voucherPage);
            CommonUtil.checkResult(voucherName + " 赠送后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 赠送后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //确认支付
            util.makeSureBuyPackage(packageName);
            //购买后数据
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal + 1, buyPackageRecordScene.visitor(visitor).execute().getInteger("total"));
            CommonUtil.checkResult(packageName + " 赠送（套）", giveNumber + 1, util.getPackagePage(packageName, packageId).getGiveNumber());
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecord(voucherId);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            VoucherFormVoucherPageBean thirdVoucherPage = util.flushVoucherPage(secondVoucherPage);
            CommonUtil.checkResult(voucherName + " 确认支付后可用库存", secondVoucherPage.getAllowUseInventory(), thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 确认支付后剩余库存", secondVoucherPage.getSurplusInventory() - 1, thirdVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, voucherInfoScene.visitor(visitor).execute().getInteger("total_send"));
            util.loginApplet(APPLET_USER_ONE);
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
        try {
            VoucherFormVoucherPageBean voucherPage = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherPage();
            Long voucherId = voucherPage.getVoucherId();
            String voucherName = voucherPage.getVoucherName();
            JSONArray voucherList = util.getVoucherArray(voucherPage, 1);
            Long packageId = util.toFirstJavaObject(PackageListScene.builder().build(), PackageListBean.class).getPackageId();
            String packageName = util.editPackage(packageId, voucherList);
            //赠送前数据
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = buyPackageRecordScene.visitor(visitor).execute().getInteger("total");
            int giveNumber = util.getPackagePage(packageName, packageId).getGiveNumber();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = voucherInfoScene.visitor(visitor).execute().getInteger("total_send");
            util.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            util.loginPc(ACCOUNT);
            //赠送固定套餐
            util.receptionBuyFixedPackage(packageId, 0);
            VoucherFormVoucherPageBean secondVoucherPage = util.flushVoucherPage(voucherPage);
            CommonUtil.checkResult(voucherName + " 赠送后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 赠送后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //取消支付
            util.cancelSoldPackage(packageName);
            //取消后数据
            PackagePage packagePage = util.getPackagePage(packageName, packageId);
            VoucherFormVoucherPageBean thirdVoucherPage = util.flushVoucherPage(secondVoucherPage);
            CommonUtil.checkResult(voucherName + " 取消后可用库存", secondVoucherPage.getAllowUseInventory() + 1, thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 取消后剩余库存", secondVoucherPage.getSurplusInventory(), thirdVoucherPage.getSurplusInventory());
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal + 1, buyPackageRecordScene.visitor(visitor).execute().getInteger("total"));
            CommonUtil.checkResult(packageName + " 赠送（套）", giveNumber, packagePage.getGiveNumber());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend, voucherInfoScene.visitor(visitor).execute().getInteger("total_send"));
            util.loginApplet(APPLET_USER_ONE);
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
        try {
            VoucherFormVoucherPageBean voucherPage = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherPage();
            Long voucherId = voucherPage.getVoucherId();
            String voucherName = voucherPage.getVoucherName();
            //赠送前数据
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = buyPackageRecordScene.visitor(visitor).execute().getInteger("total");
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = voucherInfoScene.visitor(visitor).execute().getInteger("total_send");
            util.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            //赠送临时套餐
            util.loginPc(ACCOUNT);
            JSONArray voucherList = util.getVoucherArray(voucherPage, 1);
            util.receptionBuyTemporaryPackage(voucherList, 0);
            VoucherFormVoucherPageBean secondVoucherPage = util.flushVoucherPage(voucherPage);
            CommonUtil.checkResult(voucherName + " 赠送后可用库存", voucherPage.getAllowUseInventory() - 1, secondVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 赠送后剩余库存", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            //取消
            util.cancelSoldPackage("临时套餐");
            //取消后数据
            CommonUtil.checkResult("套餐购买记录", buyPackageRecordTotal + 1, buyPackageRecordScene.visitor(visitor).execute().getInteger("total"));
            VoucherFormVoucherPageBean thirdVoucherPage = util.flushVoucherPage(secondVoucherPage);
            CommonUtil.checkResult(voucherName + " 取消后可用库存", secondVoucherPage.getAllowUseInventory() + 1, thirdVoucherPage.getAllowUseInventory());
            CommonUtil.checkResult(voucherName + " 取消后剩余库存", secondVoucherPage.getSurplusInventory(), thirdVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend, voucherInfoScene.visitor(visitor).execute().getInteger("total_send"));
            util.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("售后接待管理--赠送一个临时套餐，取消后卡券&套餐数量不变");
        }
    }

    //ok
    @Test(description = "售后客户管理--相同底盘号的客户最新里程数相等")
    public void afterSaleCustomerManager_data_1() {
        try {
            IScene afterSaleCustomerPageScene = AfterSaleCustomerPageScene.builder().build();
            List<AfterSaleCustomerPageBean> afterSaleCustomerPageList = util.toJavaObjectList(afterSaleCustomerPageScene, AfterSaleCustomerPageBean.class, SceneUtil.SIZE);
            afterSaleCustomerPageList.forEach(afterSaleCustomerPage -> {
                String vehicleChassisCode = afterSaleCustomerPage.getVehicleChassisCode();
                if (vehicleChassisCode != null) {
                    IScene afterSaleCustomerPageScene1 = AfterSaleCustomerPageScene.builder().vehicleChassisCode(vehicleChassisCode).build();
                    List<AfterSaleCustomerPageBean> afterSaleCustomerPageList1 = util.toJavaObjectList(afterSaleCustomerPageScene1, AfterSaleCustomerPageBean.class);
                    List<Integer> miles = afterSaleCustomerPageList1.stream().map(AfterSaleCustomerPageBean::getNewestMiles).collect(Collectors.toList());
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

    //ok
    @Test(description = "售后客户管理--售后客户列表，消费频次=维修记录中产值不为0的记录之和，总消费=维修记录列表产值之和")
    public void afterSaleCustomerManager_data_2() {
        try {
            IScene scene = AfterSaleCustomerPageScene.builder().build();
            List<AfterSaleCustomerPageBean> customerPageBeanList = util.toJavaObjectList(scene, AfterSaleCustomerPageBean.class, SceneUtil.SIZE);
            customerPageBeanList.forEach(pageBean -> {
                JSONObject responseData = RepairPageScene.builder().carId(String.valueOf(pageBean.getCarId())).shopId(String.valueOf(pageBean.getShopId())).build().visitor(visitor).execute();
                JSONArray repairPageList = responseData.getJSONArray("list");
                int repairTimes = (int) repairPageList.stream().map(a -> (JSONObject) a).filter(a -> !util.toJavaObject(a, ReceptionPage.class).getOutputValue().equals("0")).count();
                CommonUtil.checkResultPlus("销售客户列表消费频次", pageBean.getRepairTimes() == null ? 0 : pageBean.getRepairTimes(), "维修记录列表维修次数", repairTimes);
                Double totalOutputValue = repairPageList.stream().map(a -> (JSONObject) a).map(a -> util.toJavaObject(a, ReceptionPage.class)).map(ReceptionPage::getOutputValue).map(a -> a.replace(",", "")).mapToDouble(Double::parseDouble).sum();
                CommonUtil.checkResultPlus("销售客户列表总消费", pageBean.getTotalPrice() == null ? 0.0 : pageBean.getTotalPrice(), "维修记录列表产值之和", totalOutputValue);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("售后客户管理--售后客户列表，消费频次=维修记录中产值不为0的记录之和，总消费=维修记录列表产值之和");
        }
    }

    //bug会造成小程序车牌号消失，暂时关闭
    @Test(description = "售后客户管理--编辑售后客户各项信息，必填项不填，全部失败", enabled = false)
    public void afterSaleCustomerManager_data_3() {
        try {
            IScene saleCustomerPageScene = AfterSaleCustomerPageScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
            AfterSaleCustomerPageBean customerPage = util.toJavaObjectList(saleCustomerPageScene, AfterSaleCustomerPageBean.class).get(0);
            IScene afterSaleCustomerInfoScene = AfterSaleCustomerInfoScene.builder().shopId(customerPage.getShopId()).carId(customerPage.getCarId()).customerId(customerPage.getCustomerId()).shopId(customerPage.getShopId()).build();
            AfterSaleCustomerInfoBean customerInfo = util.toJavaObject(afterSaleCustomerInfoScene, AfterSaleCustomerInfoBean.class);
            IScene afterSaleCustomerEditScene = AfterSaleCustomerEditScene.builder()
                    .customerPhone(customerInfo.getCustomerPhone())
                    .customerName(customerInfo.getCustomerName())
                    .sexId(1)
                    .vehicleChassisCode(customerInfo.getVehicleChassisCode())
                    .plateNumber(util.getPlatNumber(APPLET_USER_ONE.getPhone()))
                    .newestMiles(customerInfo.getNewestMiles())
                    .carStyleId(customerInfo.getCarStyleId())
                    .carModelId(customerInfo.getCarModelId())
                    .buyCarTime(DateTimeUtil.getFormat(new Date()))
                    .customerId(customerInfo.getCustomerId())
                    .shopId(customerInfo.getShopId())
                    .carId(customerInfo.getCarId())
                    .build();
            String[] messageList = util.getMessageList(afterSaleCustomerEditScene);
            String[] errList = {"性别不能为空", "shopId 不能为空", "品牌车型id不能为空", "用户手机号不能为空", "客户名称不能为空", "车牌号格式不正确", "客户不存在", "车辆id不能为空", "底盘号格式不正确", "车系不存在", "购车时间不能为空", "最新公里数不能为空"};
            CommonUtil.checkResultPlus("预期返回值message", errList, "实际返回值message", messageList);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("售后售后客户管理--编辑售后客户各项信息，必填项不填，全部失败");
        }
    }

    //ok
    @Test(description = "预约管理--保养配置，修改保养价格")
    public void customerManager_data_6() {
        try {
            commonConfig.setShopId(ACCOUNT.getReceptionShopId());
            Double[] doubles = {1.00, 2.99, 3.66, 50.1};
            JSONObject jsonObject = ManageModelPageScene.builder().type("MAINTAIN").build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            Long id = jsonObject.getLong("id");
            Double price = Arrays.stream(doubles).filter(e -> !e.equals(jsonObject.getDouble("price"))).findFirst().orElse(jsonObject.getDouble("price"));
            ManageModelEditScene.builder().id(id).price(price).status(EnableStatusEnum.DISABLE.name()).type("MAINTAIN").build().visitor(visitor).execute();
            JSONObject newJSONObject = ManageModelPageScene.builder().type("MAINTAIN").build().visitor(visitor).execute().getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getLong("id").equals(id)).findFirst().orElse(null);
            CommonUtil.checkResult(id + "保养价格", price, Objects.requireNonNull(newJSONObject).getDouble("price"));
            CommonUtil.checkResult(id + "预约状态", EnableStatusEnum.DISABLE.name(), Objects.requireNonNull(newJSONObject).getString("status"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            commonConfig.setShopId(PRODUCE.getShopId());
            logger.info(commonConfig.getHeaders().toString());
            saveData("预约管理--保养配置，修改保养价格");
        }
    }
}
