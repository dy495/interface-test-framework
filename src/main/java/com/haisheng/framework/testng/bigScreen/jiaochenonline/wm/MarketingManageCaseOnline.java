package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.PackageDetail;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.PackagePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.UseStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.audit.AuditStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer.CustomMessageStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.IVoucher;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage.WechatCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.MessageFormPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.PackageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.VoucherListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record.PushMsgPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.SendRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormVoucherPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 营销管理模块测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class MarketingManageCaseOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_ONLINE;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_ONLINE;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
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
    @Test(description = "套餐管理--创建套餐包含卡券列表数=卡券状态为进行中的列表数")
    public void packageManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int voucherListSize = VoucherListScene.builder().build().invoke(visitor, true).getJSONArray("list").size();
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherStatus(VoucherStatusEnum.WORKING.name()).build();
            int voucherTotal = voucherPageScene.invoke(visitor, true).getInteger("total");
            CommonUtil.checkResultPlus("套餐包含卡券列表数", voucherListSize, "进行中的卡券数", voucherTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐包含卡券列表数=卡券状态为进行中的列表数");
        }
    }

    //ok
    @Test(description = "套餐管理--创建套餐，套餐列表每次均+1")
    public void packageManager_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //创建套餐前列表数量
            IScene packageFormPageScene = PackageFormPageScene.builder().build();
            int total = packageFormPageScene.invoke(visitor, true).getInteger("total");
            //创建套餐
            JSONArray voucherArray = util.getVoucherArray(voucherId, 10);
            String packageName = util.createPackage(voucherArray, com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.CURRENT);
            Long packageId = util.getPackageId(packageName);
            //创建套餐后列表数量
            int newTotal = packageFormPageScene.invoke(visitor, true).getInteger("total");
            CommonUtil.checkResult("创建套餐后套餐列表数", total + 1, newTotal);
            //列表内容校验
            PackagePage packagePage = util.collectBean(PackageFormPageScene.builder().packageName(packageName).build(), PackagePage.class).get(0);
            CommonUtil.checkResult(packageName + " 套餐价格", "49.99", packagePage.getPrice());
            CommonUtil.checkResult(packageName + " 套餐内含卡券数", 10, packagePage.getVoucherNumber());
            CommonUtil.checkResult(packageName + " 客户有效期", "10天", packagePage.getCustomerUseValidity());
            CommonUtil.checkResult(packageName + " 审核状态", AuditStatusEnum.AUDITING.getName(), packagePage.getAuditStatusName());
            //审核通过
            AuditPackageStatusScene.builder().id(packageId).status(AuditStatusEnum.AGREE.name()).build().invoke(visitor, true);
            PackagePage newPackagePage = util.collectBean(PackageFormPageScene.builder().packageName(packageName).build(), PackagePage.class).get(0);
            CommonUtil.checkResult(packageName + " 审核状态", AuditStatusEnum.AGREE.getName(), newPackagePage.getAuditStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐，套餐列表每次均+1");
        }
    }

    //ok
    @Test(description = "套餐管理--购买套餐--【小程序客户】列表的手机号，均可查询到姓名")
    public void packageManager_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = WechatCustomerPageScene.builder().build();
            List<JSONObject> jsonObjects = util.collectBean(scene, JSONObject.class);
            jsonObjects.forEach(e -> {
                String customerPhone = e.getString("customer_phone");
                String customerName = e.getString("customer_name");
                String findCustomerName = SearchCustomerScene.builder().customerPhone(customerPhone).build().invoke(visitor, true).getString("customer_name");
                CommonUtil.checkResultPlus(customerPhone + " 小程序客户名称", customerName, "购买套餐查询到的名称", findCustomerName);
                CommonUtil.logger(customerPhone);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--购买套餐--【小程序客户】列表的手机号，均可查询到姓名");
        }
    }

    //ok
    @Test(description = "套餐管理--套餐列表展示内容与套餐详情一致")
    public void packageManager_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene packageFormPageScene = PackageFormPageScene.builder().build();
            List<PackagePage> packagePageList = util.collectBean(packageFormPageScene, PackagePage.class);
            packagePageList.forEach(packagePage -> {
                PackageDetail packageDetail = util.getPackageDetail(packagePage.getPackageId());
                int voucherCountSum = packageDetail.getVoucherList().stream().map(e -> (JSONObject) e).mapToInt(e -> e.getInteger("voucher_count")).sum();
                CommonUtil.checkResultPlus(packagePage.getPackageName() + " 列表价格", packagePage.getPrice().equals("0") ? "0.00" : packagePage.getPrice(), "详情价格", packageDetail.getPackagePrice());
                CommonUtil.checkResultPlus(packagePage.getPackageName() + " 卡券数量", packagePage.getVoucherNumber(), "详情卡券数量", voucherCountSum);
                CommonUtil.logger(packagePage.getPackageName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--套餐列表展示内容与套餐详情一致");
        }
    }

    //ok
    @Test(description = "套餐管理--购买套餐--下拉可选择的套餐数量=审核通过并且开启的套餐数")
    public void packageManager_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene packageListScene = PackageListScene.builder().build();
            int packageListSize = packageListScene.invoke(visitor, true).getJSONArray("list").size();
            IScene packageFormPageScene = PackageFormPageScene.builder().packageStatus(true).build();
            List<PackagePage> packagePageList = util.collectBean(packageFormPageScene, PackagePage.class);
            int packagePageListSize = (int) packagePageList.stream().filter(e -> e.getAuditStatusName().equals(PackageStatusEnum.AGREE.getName())).count();
            CommonUtil.checkResultPlus("购买套餐下拉列表数", packageListSize, "审核通过并且开启的套餐数", packagePageListSize);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--购买套餐--下拉可选择的套餐数量=未取消&未过期的套餐数");
        }
    }

    //ok
    @Test(description = "套餐购买记录--套餐累计出售数量=套餐购买记录中该套餐的出售数量")
    public void packageManager_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime = "2021-02-25";
            IScene packageFormPageScene = PackageFormPageScene.builder().startTime(startTime).endTime(startTime).build();
            List<PackagePage> packagePages = util.collectBean(packageFormPageScene, PackagePage.class);
            packagePages.forEach(e -> {
                String packageName = e.getPackageName();
                int soldNumber = e.getSoldNumber();
                IScene buyPackageRecordScene = BuyPackageRecordScene.builder().packageName(packageName).sendType(SendWayEnum.SOLD.getId()).build();
                List<JSONObject> jsonObjectList = util.collectBean(buyPackageRecordScene, JSONObject.class);
                int giverCount = jsonObjectList.stream().filter(jsonObject -> jsonObject.getString("package_name").equals(packageName)).mapToInt(jsonObject -> jsonObject.getInteger("send_number") == null ? 0 : jsonObject.getInteger("send_number")).sum();
                CommonUtil.checkResultPlus(packageName + " 累计购买数", soldNumber, " 购买记录累计售出发出数量", giverCount);
                CommonUtil.logger(e.getPackageName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐购买记录--套餐累计出售数量=套餐购买记录中该套餐的出售数量");
        }
    }

    //ok
    @Test(description = "购买套餐--选择卡券接口看不见已作废的卡券")
    public void packageManager_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherStatus(VoucherStatusEnum.INVALIDED.name()).build();
            List<VoucherPage> voucherPageList = util.collectBean(voucherPageScene, VoucherPage.class);
            List<Long> voucherIdList = voucherPageList.stream().map(VoucherPage::getVoucherId).collect(Collectors.toList());
            IScene voucherListScene = VoucherListScene.builder().build();
            JSONArray array = visitor.invokeApi(voucherListScene).getJSONArray("list");
            List<Long> voucherLit = array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("voucher_id")).collect(Collectors.toList());
            voucherIdList.forEach(e -> Preconditions.checkArgument(!voucherLit.contains(e), voucherListScene.getPath() + " 接口包含已作废卡券 " + util.getVoucherName(e)));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("购买套餐--选择卡券接口看不见已作废的卡券");
        }
    }

    //ok
    @Test(description = "套餐管理--创建套餐，拒绝，套餐状态为已拒绝")
    public void packageManager_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //创建套餐前列表数量
            IScene packageFormPageScene = PackageFormPageScene.builder().build();
            int total = packageFormPageScene.invoke(visitor, true).getInteger("total");
            //创建套餐
            JSONArray voucherArray = util.getVoucherArray(voucherId, 10);
            String packageName = util.createPackage(voucherArray, com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.CURRENT);
            Long packageId = util.getPackageId(packageName);
            //创建套餐后列表数量
            int newTotal = packageFormPageScene.invoke(visitor, true).getInteger("total");
            CommonUtil.checkResult("创建套餐后套餐列表数", total + 1, newTotal);
            //列表内容校验
            PackagePage packagePage = util.collectBean(PackageFormPageScene.builder().packageName(packageName).build(), PackagePage.class).get(0);
            CommonUtil.checkResult(packageName + " 套餐价格", "49.99", packagePage.getPrice());
            CommonUtil.checkResult(packageName + " 套餐内含卡券数", 10, packagePage.getVoucherNumber());
            CommonUtil.checkResult(packageName + " 客户有效期", "10天", packagePage.getCustomerUseValidity());
            CommonUtil.checkResult(packageName + " 审核状态", AuditStatusEnum.AUDITING.getName(), packagePage.getAuditStatusName());
            //审核不通过
            AuditPackageStatusScene.builder().id(packageId).status(AuditStatusEnum.REFUSAL.name()).build().invoke(visitor, true);
            PackagePage newPackagePage = util.collectBean(PackageFormPageScene.builder().packageName(packageName).build(), PackagePage.class).get(0);
            CommonUtil.checkResult(packageName + " 审核状态", AuditStatusEnum.REFUSAL.getName(), newPackagePage.getAuditStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--套餐管理--创建套餐，拒绝，套餐状态为已拒绝");
        }
    }

    //ok
    @Test(description = "套餐表单--创建套餐，套餐名称异常")
    public void packageManager_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumDesc.DESC_BETWEEN_20_30.getDesc(), "1", null, ""};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Arrays.stream(strings).forEach(name -> {
                IScene scene = CreatePackageScene.builder().packageName(name).packageDescription(util.getDesc())
                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType())).expireType(2).expiryDate("10")
                        .voucherList(voucherList).packagePrice("5000.00").status(true).shopIds(util.getShopIdList()).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(name) ? "套餐名称不能为空" : "套餐名称输入应大于2字小于20字";
                CommonUtil.checkResult("套餐名称为：" + name, err, message);
                CommonUtil.logger(name);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--创建套餐，套餐名称异常");
        }
    }

    //ok
    @Test(description = "套餐表单--创建套餐，套餐说明异常")
    public void packageManager_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, "", EnumDesc.DESC_BETWEEN_200_300.getDesc()};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Arrays.stream(strings).forEach(desc -> {
                IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(UseRangeEnum.BRAND)).packageDescription(desc)
                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType())).expireType(2).expiryDate("10")
                        .voucherList(voucherList).packagePrice("5000.00").status(true).shopIds(util.getShopIdList()).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(desc) ? "套餐说明不能为空" : "套餐说明不能超过200字";
                CommonUtil.checkResult("套餐名称为：" + desc, err, message);
                CommonUtil.logger(desc);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--创建套餐，套餐说明异常");
        }
    }

    //ok
    @Test(description = "套餐表单--创建套餐，主体类型异常")
    public void packageManager_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {"全部权限", null, ""};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Arrays.stream(strings).forEach(subjectType -> {
                IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(UseRangeEnum.BRAND)).packageDescription(util.getDesc())
                        .subjectType(subjectType).subjectId(util.getSubjectDesc(util.getSubjectType())).voucherList(voucherList)
                        .packagePrice("5000.00").expireType(2).expiryDate("12").shopIds(util.getShopIdList()).status(true).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = "主体类型不存在";
                CommonUtil.checkResult("主体类型为" + subjectType, err, message);
                CommonUtil.logger(subjectType);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--创建套餐，主体类型异常");
        }
    }

    //ok
    @Test(description = "套餐表单--创建套餐，主体详情异常")
    public void packageManager_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(UseRangeEnum.BRAND)).packageDescription(util.getDesc())
                    .subjectType(UseRangeEnum.STORE.name()).voucherList(voucherList).packagePrice("5000.00").expireType(2)
                    .expiryDate("12").shopIds(util.getShopIdList()).status(true).build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "主体详情不能为空";
            CommonUtil.checkResult("主体详情为：" + null, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--创建套餐，主体详情异常");
        }
    }

    //ok
    @Test(description = "套餐表单--创建套餐，包含卡券为空")
    public void packageManager_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(UseRangeEnum.BRAND))
                    .packageDescription(util.getDesc()).subjectType(UseRangeEnum.CURRENT.name()).packagePrice("5000.00")
                    .expireType(2).expiryDate("12").shopIds(util.getShopIdList()).status(true).build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "所选卡券不能为空";
            CommonUtil.checkResult("包含卡券为：" + null, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--创建套餐，包含卡券为空");
        }
    }

    //ok
    @Test(description = "套餐表单--创建套餐，套餐价格异常")
    public void packageManager_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] doubles = {null, "100000000.01", "-3.55"};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Arrays.stream(doubles).forEach(packagePrice -> {
                IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(UseRangeEnum.BRAND))
                        .packageDescription(util.getDesc()).subjectType(UseRangeEnum.STORE.name()).packagePrice(packagePrice)
                        .voucherList(voucherList).expireType(2).expiryDate("12").shopIds(util.getShopIdList()).status(true).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = packagePrice == null ? "套餐价格不能为空" : "套餐价格不能大于100,000,000";
                CommonUtil.checkResult("有效期为：" + packagePrice, err, message);
                CommonUtil.logger(packagePrice);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--创建套餐，套餐价格异常");
        }
    }

    //ok
    @Test(description = "套餐表单--创建套餐，选择门店为空")
    public void packageManager_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(UseRangeEnum.BRAND))
                    .packageDescription(util.getDesc()).subjectType(UseRangeEnum.STORE.name()).packagePrice("499.99")
                    .voucherList(voucherList).expireType(2).expiryDate("10").status(true).build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "套餐适用门店列表不能为空";
            CommonUtil.checkResult("选择门店为：" + null, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--创建套餐，选择门店为空");
        }
    }

    //ok
    @Test(description = "套餐表单--选择作废的卡券创建套餐，提示：卡券【xxxx】已被作废，请重新选择！")
    public void packageManager_system_29() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.INVALIDED).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 10);
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(UseRangeEnum.BRAND))
                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                    .packagePrice("499.99").status(true).voucherList(voucherList).shopIds(util.getShopIdList())
                    .expireType(2).expiryDate("10").build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "卡券【" + voucherName + "】已被作废，请重新选择！";
            CommonUtil.checkResult("创建套餐时包含已作废卡券" + voucherName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--选择作废的卡券创建套餐，提示：卡券【xxxx】已被作废，请重新选择！");
        }
    }

    //ok
    @Test(description = "套餐表单--选择待审核的卡券创建套餐，提示：卡券已被拒绝或者已取消，请重新选择！")
    public void packageManager_system_30() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 10);
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(UseRangeEnum.BRAND))
                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                    .packagePrice("499.99").status(true).voucherList(voucherList).shopIds(util.getShopIdList())
                    .expireType(2).expiryDate("10").build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "卡券已被拒绝或者已取消，请重新选择！";
            CommonUtil.checkResult("创建套餐时包含已作废卡券" + voucherName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--选择待审核的卡券创建套餐，提示：卡券已被拒绝或者已取消，请重新选择！");
        }
    }

    //ok
    @Test(description = "套餐表单--选择审核未通过的卡券创建套餐，提示：卡券已被拒绝或者已取消，请重新选择！")
    public void packageManager_system_31() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 10);
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(UseRangeEnum.BRAND))
                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                    .packagePrice("499.99").status(true).voucherList(voucherList).shopIds(util.getShopIdList())
                    .expireType(2).expiryDate("10").build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "卡券已被拒绝或者已取消，请重新选择！";
            CommonUtil.checkResult("创建套餐时包含已作废卡券" + voucherName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--选择审核未通过的卡券创建套餐，提示：卡券已被拒绝或者已取消，请重新选择！");
        }
    }

    //ok
    @Test(description = "套餐表单--购买套餐，联系方式异常")
    public void packageManager_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] phones = {null, "", "1532152798", "13654973499", "010-8888888"};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 10);
            Arrays.stream(phones).forEach(phone -> {
                IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackageScene.builder().customerPhone(phone)
                        .carType(PackageUseTypeEnum.ALL_CAR.name()).voucherList(voucherList)
                        .expiryDate("1").remark(EnumDesc.DESC_BETWEEN_20_30.getDesc()).subjectType(util.getSubjectType())
                        .subjectId(util.getSubjectDesc(util.getSubjectType())).extendedInsuranceYear("1")
                        .extendedInsuranceCopies("1").type(1).build();
                String message = visitor.invokeApi(purchaseTemporaryPackageScene, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(phone) ? "客户手机号不能为空" : "客户不存在";
                CommonUtil.checkResult("联系方式为" + phone, err, message);
                CommonUtil.logger(phone);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--购买套餐，联系方式异常");
        }
    }

    //ok
    @Test(description = "套餐表单--购买套餐，车牌号异常")
    public void packageManager_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] plateNumbers = {null, "", "京A444", "岗A88776"};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 10);
            Arrays.stream(plateNumbers).forEach(plateNumber -> {
                IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackageScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                        .carType(PackageUseTypeEnum.RECEPTION_CAR.name()).plateNumber(plateNumber).voucherList(voucherList)
                        .expiryDate("1").remark(EnumDesc.DESC_BETWEEN_20_30.getDesc()).subjectType(util.getSubjectType())
                        .subjectId(util.getSubjectDesc(util.getSubjectType())).extendedInsuranceYear("1")
                        .extendedInsuranceCopies("1").type(1).build();
                String message = visitor.invokeApi(purchaseTemporaryPackageScene, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(plateNumber) ? "车牌号不可为空" : "车牌号格式不正确";
                CommonUtil.checkResult("车牌号" + plateNumber, err, message);
                CommonUtil.logger(plateNumber);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--购买套餐，车牌号异常");
        }
    }

    //ok
    @Test(description = "套餐表单--购买套餐，超过10张卡券")
    public void packageManager_system_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 101);
            IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackageScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).voucherList(voucherList).expiryDate("1").remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType())).extendedInsuranceYear("1")
                    .extendedInsuranceCopies("1").type(1).build();
            String message = visitor.invokeApi(purchaseTemporaryPackageScene, false).getString("message");
            String err = voucherList == null ? "卡券列表不能为空" : "卡券数量不能超过100张";
            CommonUtil.checkResult("卡券数量为" + voucherList, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--购买套餐，超过10张卡券");
        }
    }

    //ok
    @Test(description = "套餐表单--购买套餐，套餐价格异常")
    public void packageManager_system_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long packageId = util.getPackagePage(PackageStatusEnum.AGREE).getPackageId();
            String[] packagePrices = {"100000001"};
            Arrays.stream(packagePrices).forEach(packagePrice -> {
                IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                        .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).packagePrice(packagePrice).expiryDate("1")
                        .remark(EnumDesc.DESC_BETWEEN_20_30.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .extendedInsuranceYear(10).extendedInsuranceCopies(10).type(1).build();
                String message = visitor.invokeApi(purchaseFixedPackageScene, false).getString("message");
                String err = "套餐购买价格不能超过100000000元";
                CommonUtil.checkResult("套餐价格为" + packagePrice, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--购买套餐，套餐价格异常");
        }
    }

    //ok
    @Test(description = "套餐表单--购买套餐，套餐说明异常")
    public void packageManager_system_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long packageId = util.getPackagePage(PackageStatusEnum.AGREE).getPackageId();
            //购买固定套餐
            String[] remarks = {EnumDesc.DESC_BETWEEN_200_300.getDesc()};
            Arrays.stream(remarks).forEach(remark -> {
                IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                        .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).packagePrice("49.99").expiryDate("1").expiryDate("10")
                        .remark(EnumDesc.DESC_BETWEEN_20_30.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .extendedInsuranceYear(10).extendedInsuranceCopies(10).type(1).remark(remark).build();
                String message = visitor.invokeApi(purchaseFixedPackageScene, false).getString("message");
                String err = "备注不能超过200字";
                CommonUtil.checkResult("套餐说明为" + remark, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--购买套餐，套餐说明异常");
        }
    }

    //ok
    @Test(description = "套餐表单--购买套餐，主体类型异常")
    public void packageManager_system_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long packageId = util.getPackagePage(PackageStatusEnum.AGREE).getPackageId();
            //购买固定套餐
            String[] subjectTypes = {"全部权限", null, ""};
            Arrays.stream(subjectTypes).forEach(subjectType -> {
                IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                        .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).packagePrice("49.99").expiryDate("1").expiryDate("10")
                        .remark(EnumDesc.DESC_BETWEEN_20_30.getDesc()).subjectType(subjectType).subjectId(util.getSubjectDesc(subjectType))
                        .extendedInsuranceYear(10).extendedInsuranceCopies(10).type(1).build();
                String message = visitor.invokeApi(purchaseFixedPackageScene, false).getString("message");
                String err = "主体类型不存在";
                CommonUtil.checkResult("主体类型为" + subjectType, err, message);
                CommonUtil.logger(subjectType);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--购买套餐，主体类型异常");
        }
    }

    //ok
    @Test(description = "套餐表单--购买套餐，主体详情异常")
    public void packageManager_system_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long packageId = util.getPackagePage(PackageStatusEnum.AGREE).getPackageId();
            //购买固定套餐
            IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).packagePrice("49.99").expiryDate("1").expiryDate("10")
                    .remark(EnumDesc.DESC_BETWEEN_20_30.getDesc()).subjectType(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.STORE.getName())
                    .extendedInsuranceYear(10).extendedInsuranceCopies(10).type(1).build();
            String message = visitor.invokeApi(purchaseFixedPackageScene, false).getString("message");
            String err = "主体类型不存在";
            CommonUtil.checkResult("主体类型为" + null, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--购买套餐，主体详情异常");
        }
    }

    //ok
    @Test(description = "套餐表单--购买套餐，选择套餐异常")
    public void packageManager_system_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long[] packageIds = {null};
            Arrays.stream(packageIds).forEach(packageId -> {
                //购买固定套餐
                IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                        .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).packagePrice("49.99").expiryDate("1").expiryDate("10")
                        .remark(EnumDesc.DESC_BETWEEN_20_30.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .extendedInsuranceYear(10).extendedInsuranceCopies(10).type(1).build();
                String message = visitor.invokeApi(purchaseFixedPackageScene, false).getString("message");
                String err = packageId == null ? "套餐列表不能为空" : "";
                CommonUtil.checkResult("选择套餐", packageId, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--购买套餐，选择套餐异常");
        }
    }

    //ok
    @Test(description = "套餐表单--临时套餐购买已售罄的卡券，确认提示：卡券【XXXX】已售罄")
    public void packageManager_system_18() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 2);
            //购买临时套餐
            IScene temporaryScene = PurchaseTemporaryPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).voucherList(voucherList).expiryDate("1").remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                    .extendedInsuranceYear("1").extendedInsuranceCopies("1").type(1).build();
            String message = visitor.invokeApi(temporaryScene, false).getString("message");
            String err = "卡券【" + voucherName + "】已售罄";
            CommonUtil.checkResult("购买无库存卡券" + voucherName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--临时套餐购买已售罄的卡券，确认提示：卡券【XXXX】已售罄");
        }
    }

    //ok
    @Test(description = "套餐表单--临时套餐购买已作废卡券，确认时会有提示：卡券【XXX】已被作废，请重新选择！")
    public void packageManager_system_19() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.INVALIDED).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 2);
            //购买临时套餐
            IScene temporaryScene = PurchaseTemporaryPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).voucherList(voucherList).expiryDate("1").remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                    .extendedInsuranceYear("1").extendedInsuranceCopies("1").type(1).build();
            String message = visitor.invokeApi(temporaryScene, false).getString("message");
            String err = "卡券【" + voucherName + "】已被作废，请重新选择！";
            CommonUtil.checkResult("购买已作废的卡券" + voucherName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--临时套餐购买已作废卡券，确认时会有提示：卡券【XXX】已被作废，请重新选择！");
        }
    }

    //ok
    @Test(description = "套餐表单--购买包含已售罄卡券的套餐，确认时会有提示：卡券【XXX】已被作废，请重新选择！")
    public void packageManager_system_20() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long packageId = util.editPackage(voucherId, 1);
            //购买固定套餐
            IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).expiryDate("1").remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType())).packagePrice("49.99")
                    .extendedInsuranceYear(1).extendedInsuranceCopies(1).type(1).build();
            String message = visitor.invokeApi(purchaseFixedPackageScene, false).getString("message");
            String err = "卡券【" + voucherName + "】已售罄";
            CommonUtil.checkResult("购买包含已售罄卡券的套餐", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--购买包含已售罄卡券的套餐，确认时会有提示：卡券【XXX】已被作废，请重新选择！");
        }
    }

    //ok
    @Test(description = "套餐表单--购买/赠送固定套餐时，将套餐关闭，确认时会有提示：此套餐已关闭")
    public void packageManager_system_21() {
        logger.logCaseStart(caseResult.getCaseName());
        long packageId = 0;
        try {
            packageId = util.getPackagePage(PackageStatusEnum.AGREE).getPackageId();
            String packageName = util.getPackageName(packageId);
            //关闭套餐
            IScene switchPackageStatusScene = PackageFormSwitchPackageStatusScene.builder().id(packageId).status(false).build();
            visitor.invokeApi(switchPackageStatusScene);
            //购买固定套餐
            IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).expiryDate("1").remark(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType())).packagePrice("49.99")
                    .extendedInsuranceYear(1).extendedInsuranceCopies(1).type(1).build();
            String message = visitor.invokeApi(purchaseFixedPackageScene, false).getString("message");
            String err = "套餐【" + packageName + "】不允许发放";
            CommonUtil.checkResult("购买已关闭套餐" + packageName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            IScene switchPackageStatusScene = PackageFormSwitchPackageStatusScene.builder().id(packageId).status(true).build();
            visitor.invokeApi(switchPackageStatusScene);
            saveData("套餐表单--购买/赠送固定套餐时，将套餐关闭，确认时会有提示：此套餐已关闭");
        }
    }

    //ok
    @Test(description = "套餐表单--修改套餐，套餐名称异常")
    public void packageManager_system_22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherArray = util.getVoucherArray(voucherId, 1);
            Long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String[] packageNames = {EnumDesc.DESC_BETWEEN_20_30.getDesc(), null, ""};
            Arrays.stream(packageNames).forEach(packageName -> {
                IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .voucherList(voucherArray).packagePrice("1.11").status(true).shopIds(util.getShopIdList())
                        .id(String.valueOf(packageId)).expireType(2).expiryDate(10).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(packageName) ? "套餐名称不能为空" : "套餐名称输入应大于2字小于20字";
                CommonUtil.checkResult("修改套餐名称为：" + packageName, err, message);
                CommonUtil.logger(packageName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--修改套餐，套餐名称异常");
        }
    }

    //ok
    @Test(description = "套餐表单--修改套餐，套餐名称重复")
    public void packageManager_system_33() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherArray = util.getVoucherArray(voucherId, 1);
            long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String packageName = util.getPackageName(packageId);
            long editPackageId = util.getPackagePage(PackageStatusEnum.AGREE).getPackageId();
            IScene editPackageScene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.DESC_BETWEEN_20_30.getDesc())
                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                    .voucherList(voucherArray).packagePrice("1.11").status(true).shopIds(util.getShopIdList())
                    .id(String.valueOf(editPackageId)).expireType(2).expiryDate(10).build();
            String message = visitor.invokeApi(editPackageScene, false).getString("message");
            String err = "套餐名称重复，请重新输入！";
            CommonUtil.checkResult("修改套餐名称为：" + packageName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--修改套餐，套餐名称异常");
        }
    }

    //ok
    @Test(description = "套餐表单--修改套餐，套餐说明异常")
    public void packageManager_system_23() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherArray = util.getVoucherArray(voucherId, 1);
            long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String packageName = util.getPackageName(packageId);
            String[] contents = {EnumDesc.DESC_BETWEEN_200_300.getDesc(), null, ""};
            Arrays.stream(contents).forEach(content -> {
                IScene editPackageScene = EditPackageScene.builder().packageName(packageName).packageDescription(content)
                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .voucherList(voucherArray).packagePrice("1.11").status(true).shopIds(util.getShopIdList())
                        .id(String.valueOf(packageId)).expireType(2).expiryDate(10).build();
                String message = visitor.invokeApi(editPackageScene, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(content) ? "套餐说明不能为空" : "套餐说明不能超过200字";
                CommonUtil.checkResult("修改套餐说明为：" + content, err, message);
                CommonUtil.logger(content);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--修改套餐，套餐说明异常");
        }
    }

    //ok
    @Test(description = "套餐表单--修改套餐，主体类型异常")
    public void packageManager_system_24() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray voucherArray = util.getVoucherArray();
            long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String packageName = util.getPackageName(packageId);
            String[] subjectTypes = {"全部权限", null, ""};
            Arrays.stream(subjectTypes).forEach(subjectType -> {
                IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.DESC_BETWEEN_40_50.getDesc())
                        .subjectType(subjectType).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .voucherList(voucherArray).packagePrice("1.11").status(true).shopIds(util.getShopIdList())
                        .id(String.valueOf(packageId)).expireType(2).expiryDate(3650).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(subjectType) ? "主体类型不存在" : "主体类型不存在";
                CommonUtil.checkResult("修改主体类型为：" + subjectType, err, message);
                CommonUtil.logger(subjectType);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--修改套餐，主体类型异常");
        }
    }

    //ok
    @Test(description = "套餐表单--修改套餐，主体详情异常")
    public void packageManager_system_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray voucherArray = util.getVoucherArray();
            long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String packageName = util.getPackageName(packageId);
            IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.DESC_BETWEEN_40_50.getDesc())
                    .subjectType(UseRangeEnum.STORE.name()).expireType(2).expiryDate(2).voucherList(voucherArray).packagePrice("1.11")
                    .status(true).shopIds(util.getShopIdList()).id(String.valueOf(packageId)).build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "主体详情不能为空";
            CommonUtil.checkResult("修改主体详情为：" + null, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--修改套餐，主体类型异常");
        }
    }

    //ok
    @Test(description = "套餐表单--修改套餐，包含卡券异常")
    public void packageManager_system_26() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<JSONArray> list = new ArrayList<>();
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherArray = util.getVoucherArray(voucherId, 101);
            list.add(voucherArray);
            list.add(null);
            long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String packageName = util.getPackageName(packageId);
            list.forEach(e -> {
                IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.DESC_BETWEEN_40_50.getDesc())
                        .subjectType(util.getSubjectType()).expireType(2).expiryDate(23).packagePrice("1.11").voucherList(e)
                        .status(true).shopIds(util.getShopIdList()).id(String.valueOf(packageId)).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = e == null ? "所选卡券不能为空" : "卡券数量不能超过100张";
                CommonUtil.checkResult("编辑所选卡券为：" + e, err, message);
                CommonUtil.logger(e);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--修改套餐，包含卡券异常");
        }
    }

    //ok
    @Test(description = "套餐表单--修改套餐，套餐价格异常")
    public void packageManager_system_27() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray voucherArray = util.getVoucherArray();
            long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String packageName = util.getPackageName(packageId);
            String[] prices = {"100000000.01", "10000000001", null, ""};
            Arrays.stream(prices).forEach(price -> {
                IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.DESC_BETWEEN_40_50.getDesc())
                        .subjectType(util.getSubjectType()).expireType(2).expiryDate(2).packagePrice(price).voucherList(voucherArray)
                        .status(true).shopIds(util.getShopIdList()).id(String.valueOf(packageId)).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(price) ? "套餐价格不能为空" : "套餐价格不能大于100,000,000";
                CommonUtil.checkResult("修改套餐价格为：" + price, err, message);
                CommonUtil.logger(price);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--修改套餐，套餐价格异常");
        }
    }

    //ok
    @Test(description = "套餐表单--修改套餐，所选门店为空")
    public void packageManager_system_28() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray voucherArray = util.getVoucherArray();
            long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String packageName = util.getPackageName(packageId);
            IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.DESC_BETWEEN_40_50.getDesc())
                    .subjectType(util.getSubjectType()).expireType(2).expiryDate(122).packagePrice("99.99").voucherList(voucherArray)
                    .status(true).id(String.valueOf(packageId)).build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "套餐适用门店列表不能为空";
            CommonUtil.checkResult("修改套餐适用门店为：" + null, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--修改套餐，所选门店为空");
        }
    }

    //ok
    @Test(description = "套餐表单--购买套餐，确认购买前，套餐状态改为关闭，再确认购买小程序会收到套餐/卡券")
    public void packageManager_system_34() {
        logger.logCaseStart(caseResult.getCaseName());
        Long packageId = null;
        try {
            user.loginApplet(APPLET_USER_ONE);
            int packageNum = util.getAppletPackageNum();
            int voucherNum = util.getAppletVoucherNum();
            user.loginPc(ALL_AUTHORITY);
            PackagePage packagePage = util.getPackagePage(PackageStatusEnum.AGREE);
            packageId = packagePage.getPackageId();
            String packageName = packagePage.getPackageName();
            util.editPackage(packageId, util.getVoucherArray());
            //购买套餐
            util.buyFixedPackage(packageId, 1);
            //将套餐关闭
            IScene switchPackageStatusScene = PackageFormSwitchPackageStatusScene.builder().id(packageId).status(false).build();
            visitor.invokeApi(switchPackageStatusScene);
            //确认购买
            util.makeSureBuyPackage(packageName);
            user.loginApplet(APPLET_USER_ONE);
            int newPackageNum = util.getAppletPackageNum();
            int newVoucherNum = util.getAppletVoucherNum();
            CommonUtil.checkResult("确认支付套餐后小程序我的卡券", voucherNum, newVoucherNum);
            CommonUtil.checkResult("确认支付套餐后小程序我的套餐", packageNum + 1, newPackageNum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            user.loginPc(ALL_AUTHORITY);
            IScene switchPackageStatusScene = PackageFormSwitchPackageStatusScene.builder().id(packageId).status(true).build();
            visitor.invokeApi(switchPackageStatusScene);
            saveData("套餐表单--购买套餐，确认购买前，套餐状态改为关闭，再确认购买小程序会收到套餐/卡券");
        }
    }

    //ok
    @Test(description = "消息管理--推送消息含有一张卡券的消息，消息记录+1，卡券库存-1，发卡记录+1")
    public void messageManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene messageFormPageScene = MessageFormPageScene.builder().build();
            int messageTotal = visitor.invokeApi(messageFormPageScene).getInteger("total");
            IScene sendRecordScene = SendRecordScene.builder().voucherId(voucherId).build();
            int sendRecordTotal = visitor.invokeApi(sendRecordScene).getInteger("total");
            IScene pushMsgPageScene = PushMsgPageScene.builder().build();
            int pushMsgPageTotal = visitor.invokeApi(pushMsgPageScene).getInteger("total");
            Long surplusInventory = util.getVoucherPage(voucherId).getSurplusInventory();
            //消息发送一张卡券
            util.pushMessage(0, true, voucherId);
            String sendStatusName = visitor.invokeApi(messageFormPageScene).getJSONArray("list").getJSONObject(0).getString("send_status_name");
            CommonUtil.checkResult("发送状态", CustomMessageStatusEnum.SUCCESS.getStatusName(), sendStatusName);
            CommonUtil.checkResult("消息管理列表", messageTotal + 1, visitor.invokeApi(messageFormPageScene).getInteger("total"));
            CommonUtil.checkResult("消息记录", pushMsgPageTotal + 1, visitor.invokeApi(pushMsgPageScene).getInteger("total"));
            CommonUtil.checkResult(voucherName + " 发卡记录列表", sendRecordTotal + 1, visitor.invokeApi(sendRecordScene).getInteger("total"));
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory - 1, util.getVoucherPage(voucherId).getSurplusInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--推送消息含有一张卡券，推送成功后，【卡券管理页】该卡券累计发出+1，发卡记录列表+1");
        }
    }

    //ok
    @Test(description = "消息管理--定时发送，列表+1&状态=排期中")
    public void messageManager_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            IScene messageFormPageScene = MessageFormPageScene.builder().build();
            int messageTotal = visitor.invokeApi(messageFormPageScene).getInteger("total");
            String pushTime = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 80), "yyyy-MM-dd HH:mm");
            util.pushMessage(0, false, voucherId);
            String sendStatusName = visitor.invokeApi(messageFormPageScene).getJSONArray("list").getJSONObject(0).getString("send_status_name");
            CommonUtil.checkResult("消息管理列表", messageTotal + 1, visitor.invokeApi(messageFormPageScene).getInteger("total"));
            CommonUtil.checkResult("发送状态", CustomMessageStatusEnum.SCHEDULING.getStatusName(), sendStatusName);
            sleep(90);
            JSONArray list = visitor.invokeApi(messageFormPageScene).getJSONArray("list");
            JSONObject jsonObject = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("push_time").contains(pushTime)).findFirst().orElse(null);
            Preconditions.checkArgument(jsonObject != null, "不存在 " + pushTime + " 发送的消息");
            String newSendStatusName = jsonObject.getString("send_status_name");
            CommonUtil.checkResult("发送状态", CustomMessageStatusEnum.SUCCESS.getStatusName(), newSendStatusName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--定时发送，列表+1&状态=排期中");
        }
    }

    //ok
    @Test(description = "消息管理--发送给一个客户时客户名称&联系方式&车牌号码显示该客户信息")
    public void messageManager_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletMessageNum = util.getAppletMessageNum();
            int appletPackageNum = util.getAppletPackageNum();
            user.loginPc(ALL_AUTHORITY);
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //发送消息
            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            util.pushMessage(0, true, voucherId);
            IScene messageFormPageScene = MessageFormPageScene.builder().size(100).build();
            JSONArray list = messageFormPageScene.invoke(visitor, true).getJSONArray("list");
            JSONObject jsonObject = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("push_time").contains(date)).findFirst().orElse(null);
            Preconditions.checkArgument(jsonObject != null, "不存在 " + date + " 发送的消息");
            String customerPhone = jsonObject.getString("customer_phone");
            String platNumber = jsonObject.getString("plate_number");
            String content = jsonObject.getString("content");
            CommonUtil.checkResult("内容", EnumDesc.DESC_BETWEEN_40_50.getDesc(), content);
            CommonUtil.checkResult("车牌号", util.getPlatNumber(customerPhone), platNumber);
            CommonUtil.checkResult("联系方式", APPLET_USER_ONE.getPhone(), customerPhone);
            user.loginApplet(APPLET_USER_ONE);
            int newAppletVoucherNum = util.getAppletVoucherNum();
            int newAppletMessageNum = util.getAppletMessageNum();
            int newAppletPackageNum = util.getAppletPackageNum();
            CommonUtil.checkResult("小程序我的卡券数量", appletVoucherNum + 1, newAppletVoucherNum);
            CommonUtil.checkResult("小程序我的消息数量", appletMessageNum + 1, newAppletMessageNum);
            CommonUtil.checkResult("小程序我的套餐数量", appletPackageNum, newAppletPackageNum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--发送给一个客户时客户名称&联系方式&车牌号码显示该客户信息");
        }
    }

    //ok
    @Test(description = "消息管理--发送给发送多人时客户名称为全部&联系方式&车牌号码显示为空", priority = 1)
    public void messageManager_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> list = new ArrayList<>();
            Long shopId = util.getContainMoreCustomerShopId();
            list.add(shopId);
            //发送消息
            String pushTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            IScene scene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.SHOP_CUSTOMER.getId()).shopList(list)
                    .messageName(EnumDesc.DESC_BETWEEN_5_10.getDesc()).messageContent(EnumDesc.DESC_BETWEEN_40_50.getDesc())
                    .ifSendImmediately(true).build();
            visitor.invokeApi(scene);
            IScene messageFormPageScene = MessageFormPageScene.builder().build();
            JSONArray array = visitor.invokeApi(messageFormPageScene).getJSONArray("list");
            List<JSONObject> jsonObjectList = array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("push_time").equals(pushTime)).collect(Collectors.toList());
            jsonObjectList.forEach(jsonObject -> {
                CommonUtil.checkResult("内容", EnumDesc.DESC_BETWEEN_40_50.getDesc(), jsonObject != null ? jsonObject.getString("content") : null);
                CommonUtil.checkResult("车牌号", null, jsonObject != null ? jsonObject.getString("plat_number") : null);
                CommonUtil.checkResult("客户名称", "全部", jsonObject != null ? jsonObject.getString("customer_name") : null);
                CommonUtil.checkResult("联系方式", null, jsonObject != null ? jsonObject.getString("customer_phone") : null);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--发送给发送多人时客户名称为全部&联系方式&车牌号码显示为空");
        }
    }

    //逻辑有问题先关闭
    @Test(description = "消息管理--发送成功率=发出条数/收到条数，结果x=100%时为全部成功，结果0%<=x<100%显示成功百分比", enabled = false)
    public void messageManager_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = MessageFormPageScene.builder().build();
            List<JSONObject> jsonObjectList = util.collectBean(scene, JSONObject.class);
            for (int i = 0; i < jsonObjectList.size(); i++) {
                int sendCount = jsonObjectList.get(i).getInteger("send_count");
                int receiveCount = jsonObjectList.get(i).getInteger("receive_count");
                String percent = CommonUtil.getPercent(sendCount, receiveCount);
                String result = percent.equals("0.0%") ? "成功0%" : "全部成功";
                String statusName = jsonObjectList.get(i).getString("status_name");
                int page = (i / 10) + 1;
                int size = (i % 10) + 1;
                CommonUtil.checkResultPlus(page + "页第" + size + "条" + " 发出条数/收到条数", result, "显示百分比", statusName);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--发送成功率=发出条数/收到条数，结果x=100%时为全部成功，结果0%<=x<100%显示成功百分比");
        }
    }

    //ok
    @Test(description = "消息管理--正常情况，发出条数=收到条数")
    public void messageManager_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene messageFormPageScene = MessageFormPageScene.builder().build();
            List<JSONObject> jsonObjectList = util.collectBean(messageFormPageScene, JSONObject.class);
            jsonObjectList.forEach(jsonObject -> {
                int sendCount = jsonObject.getInteger("send_count");
                int receiveCount = jsonObject.getInteger("receive_count");
                CommonUtil.checkResultPlus("发出条数", sendCount, "收到条数", receiveCount);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--正常情况，发出条数=收到条数");
        }
    }

    //ok
    @Test(description = "消息管理--消息发出后，消息内容&标题一致")
    public void messageManager_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //发消息
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            util.pushMessage(0, true, voucherId);
            //消息列表消息内容
            user.loginApplet(APPLET_USER_ONE);
            IScene appletMessageListScene = AppletMessageListScene.builder().size(20).build();
            JSONObject jsonObject = visitor.invokeApi(appletMessageListScene).getJSONArray("list").getJSONObject(0);
            Long id = jsonObject.getLong("id");
            IScene appletMessageDetailScene = AppletMessageDetailScene.builder().id(id).build();
            String content = visitor.invokeApi(appletMessageDetailScene).getString("content");
            String title = visitor.invokeApi(appletMessageDetailScene).getString("title");
            CommonUtil.checkResult("消息内容", EnumDesc.DESC_BETWEEN_40_50.getDesc(), content);
            CommonUtil.checkResult("消息标题", EnumDesc.DESC_BETWEEN_5_10.getDesc(), title);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--消息发出后，消息内容&标题一致");
        }
    }

    //ok
    @Test(description = "消息管理--推送消息，消息名称异常")
    public void messageManager_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] titles = {null, EnumDesc.DESC_BETWEEN_40_50.getDesc()};
            Arrays.stream(titles).forEach(title -> {
                IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                        .telList(getList(APPLET_USER_ONE.getPhone())).messageName(title)
                        .messageContent(EnumDesc.DESC_BETWEEN_20_30.getDesc()).type(1).build();
                String message = visitor.invokeApi(pushMessageScene, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(title) ? "消息名称不能为空" : "消息名称长度范围[1,20]";
                CommonUtil.checkResult("消息名称 " + title, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--推送消息，消息名称异常");
        }
    }

    //ok
    @Test(description = "消息管理--推送消息，消息内容异常")
    public void messageManager_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] contents = {null, EnumDesc.DESC_BETWEEN_200_300.getDesc()};
            Arrays.stream(contents).forEach(content -> {
                IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                        .telList(getList(APPLET_USER_ONE.getPhone())).messageName(EnumDesc.DESC_BETWEEN_5_10.getDesc())
                        .messageContent(content).type(1).build();
                String message = visitor.invokeApi(pushMessageScene, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(content) ? "消息内容不能为空" : "消息内容长度范围[2,200]";
                CommonUtil.checkResult("消息内容 " + content, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--推送消息，消息名称异常");
        }
    }

    //ok
    @Test(description = "消息管理--发送已作废卡券，提交时提示：卡券【XXXXX】已作废, 请重新选择！")
    public void messageManager_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //发消息
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.INVALIDED).buildVoucher().getVoucherId();
            IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                    .telList(getList(APPLET_USER_ONE.getPhone())).messageName(EnumDesc.DESC_BETWEEN_5_10.getDesc())
                    .messageContent(EnumDesc.DESC_BETWEEN_20_30.getDesc()).type(0).voucherOrPackageList(getList(voucherId))
                    .useDays("10").ifSendImmediately(true).build();
            String message = visitor.invokeApi(pushMessageScene, false).getString("message");
            String err = "卡券【" + util.getVoucherName(voucherId) + "】已作废，请重新选择！";
            CommonUtil.checkResult("发送已作废卡券", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--发送已作废卡券，提交时提示：卡券【XXXXX】已作废, 请重新选择！");
        }
    }

    //ok
    @Test(description = "消息管理--选择卡券时，卡券无库存，提交时提示：卡券【XXXXX】库存不足, 请重新选择！")
    public void messageManager_system_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //发消息
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                    .telList(getList(APPLET_USER_ONE.getPhone())).messageName(EnumDesc.DESC_BETWEEN_5_10.getDesc())
                    .messageContent(EnumDesc.DESC_BETWEEN_20_30.getDesc()).type(0).voucherOrPackageList(getList(voucherId))
                    .useDays("10").ifSendImmediately(true).build();
            String message = visitor.invokeApi(pushMessageScene, false).getString("message");
            String err = "卡券【" + util.getVoucherName(voucherId) + "】库存不足，请重新选择！";
            CommonUtil.checkResult("发送已售罄卡券", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--选择卡券时，卡券无库存，提交时提示：卡券【XXXXX】库存不足, 请重新选择！");
        }
    }

    //ok
    @Test(description = "消息管理--选择套餐时，套餐被关闭，提交时提示：套餐不允许发送，请重新选择")
    public void messageManager_system_12() {
        logger.logCaseStart(caseResult.getCaseName());
        long packageId = 0;
        try {
            //发消息
            packageId = util.getPackagePage(PackageStatusEnum.AGREE).getPackageId();
            //关闭套餐
            IScene switchPackageStatusScene = PackageFormSwitchPackageStatusScene.builder().id(packageId).status(false).build();
            visitor.invokeApi(switchPackageStatusScene);
            IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                    .telList(getList(APPLET_USER_ONE.getPhone())).messageName(EnumDesc.DESC_BETWEEN_5_10.getDesc())
                    .messageContent(EnumDesc.DESC_BETWEEN_20_30.getDesc()).type(1).voucherOrPackageList(getList(packageId))
                    .useDays("10").ifSendImmediately(true).build();
            String message = visitor.invokeApi(pushMessageScene, false).getString("message");
            String err = "套餐不允许发送，请重新选择";
            CommonUtil.checkResult("推送已关闭套餐", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            IScene switchPackageStatusScene = PackageFormSwitchPackageStatusScene.builder().id(packageId).status(true).build();
            visitor.invokeApi(switchPackageStatusScene);
            saveData("消息管理--选择套餐时，套餐被关闭，提交时提示：套餐不允许发送，请重新选择");
        }
    }

    //bug
    @Test(description = "消息管理--选择套餐时，套餐内包含无库存卡券，提交时提示：套餐不允许发送，请重新选择")
    public void messageManager_system_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            Long packageId = util.editPackage(voucherId, 1);
            //发消息
            IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                    .telList(getList(APPLET_USER_ONE.getPhone())).messageName(EnumDesc.DESC_BETWEEN_5_10.getDesc())
                    .messageContent(EnumDesc.DESC_BETWEEN_20_30.getDesc()).type(1).voucherOrPackageList(getList(packageId))
                    .useDays("10").ifSendImmediately(true).build();
            String message = visitor.invokeApi(pushMessageScene, false).getString("message");
            String err = util.getPackageName(packageId) + ":卡券【" + util.getVoucherName(util.getPackageContainVoucher(packageId).get(0)) + "】库存不足，请重新选择！";
            CommonUtil.checkResult("推送包含已售罄的套餐", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--选择套餐时，套餐内包含无库存卡券，提交时提示：套餐不允许发送，请重新选择");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    //ok
    @Test(description = "权益列表--修改权益，applet与pc所见内容一致")
    public void vipMarketing_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] desc = {EnumDesc.DESC_BETWEEN_15_20.getDesc(), EnumDesc.DESC_BETWEEN_5_10.getDesc(), EnumDesc.DESC_BETWEEN_10_15.getDesc()};
            IScene scene = EquityPageScene.builder().build();
            JSONObject equityPageScene = getResponseByEquityPageScene(scene);
            Integer equityId = equityPageScene.getInteger("equity_id");
            String description = Arrays.stream(desc).filter(e -> !e.equals(equityPageScene.getString("description"))).findFirst().orElse(equityPageScene.getString("description"));
            EquityEditScene.builder().awardCount(4).equityId(equityId).description(description).build().invoke(visitor, true);
            JSONObject newEquityPageScene = getResponseByEquityPageScene(scene);
            String newDescription = newEquityPageScene.getString("description");
            CommonUtil.checkResult("pc修改过权益说明后", description, newDescription);
            user.loginApplet(APPLET_USER_ONE);
            JSONObject response = AppletMemberCenterHomePageScene.builder().build().invoke(visitor, true);
            int vipType = response.getInteger("vip_type");
            JSONObject equity = response.getJSONArray("equity_list").getJSONObject(0);
            String equityName = equity.getString("equity_name");
            CommonUtil.checkResultPlus("pc权益服务类型", VipTypeEnum.VIP.getId(), "applet权益服务类型", vipType);
            CommonUtil.checkResultPlus("pc权益名称", AppletCodeBusinessTypeEnum.WASH_CAR.getTypeName(), "applet权益名称", equityName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权益列表--修改权益，applet与pc所见内容一致");
        }
    }

    //ok
    @Test(description = "权益列表--关闭权益，applet与pc所见内容一致")
    public void vipMarketing_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene equityPageScene = EquityPageScene.builder().build();
            JSONObject response = getResponseByEquityPageScene(equityPageScene);
            Integer equityId = response.getInteger("equity_id");
            String status = response.getString("status");
            if (status.equals(UseStatusEnum.DISABLE.name())) {
                CommonUtil.warning("已经是禁用状态");
            } else {
                //关闭权益
                EquityStartOrCloseScene.builder().equityId(equityId).equityStatus(UseStatusEnum.DISABLE.name()).build().invoke(visitor, true);
            }
            user.loginApplet(APPLET_USER_ONE);
            IScene memberCenterEquityListScene = AppletMemberCenterHomePageScene.builder().build();
            JSONObject jsonObject = visitor.invokeApi(memberCenterEquityListScene).getJSONArray("equity_list").getJSONObject(0);
            CommonUtil.checkResult(jsonObject.getString("equity_name") + "开启状态", UseStatusEnum.DISABLE.name(), jsonObject.getString("equity_status"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权益列表--关闭权益，applet与pc所见内容一致");
        }
    }

    //ok
    @Test(description = "权益列表--开启权益，applet与pc所见内容一致")
    public void vipMarketing_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene equityPageScene = EquityPageScene.builder().build();
            JSONObject response = getResponseByEquityPageScene(equityPageScene);
            Integer equityId = response.getInteger("equity_id");
            String status = response.getString("status");
            if (status.equals(UseStatusEnum.ENABLE.name())) {
                CommonUtil.warning("已经是开启状态");
            } else {
                //开启权益
                EquityStartOrCloseScene.builder().equityId(equityId).equityStatus(UseStatusEnum.ENABLE.name()).build().invoke(visitor, true);
            }
            user.loginApplet(APPLET_USER_ONE);
            IScene memberCenterEquityListScene = AppletMemberCenterHomePageScene.builder().build();
            JSONObject jsonObject = visitor.invokeApi(memberCenterEquityListScene).getJSONArray("equity_list").getJSONObject(0);
            CommonUtil.checkResult(jsonObject.getString("equity_name") + "开启状态", UseStatusEnum.ENABLE.name(), jsonObject.getString("equity_status"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权益列表--开启权益，applet与pc所见内容一致");
        }
    }

    //ok
    @Test(description = "权益列表--修改生日积分，描述异常")
    public void vipMarketing_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene equityPageScene = EquityPageScene.builder().build();
            Integer equityId = visitor.invokeApi(equityPageScene).getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getString("equity_name").equals("生日积分")).map(e -> e.getInteger("equity_id")).findFirst().orElse(0);
            //修改权益
            String[] descriptions = {null, EnumDesc.DESC_BETWEEN_40_50.getDesc()};
            Arrays.stream(descriptions).forEach(description -> {
                IScene equityEditScene = EquityEditScene.builder().awardCount(100).equityId(equityId).description(description).build();
                String message = visitor.invokeApi(equityEditScene, false).getString("message");
                String err = description == null ? "说明不能为空" : "说明只能在0-30个字";
                CommonUtil.checkResult("说明为" + description, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权益列表--修改生日积分，描述异常");
        }
    }

    //ok
    @Test(description = "权益列表--修改免费洗车，积分异常")
    public void vipMarketing_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene equityPageScene = EquityPageScene.builder().build();
            Integer equityId = visitor.invokeApi(equityPageScene).getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getString("equity_name").equals("免费洗车")).map(e -> e.getInteger("equity_id")).findFirst().orElse(0);
            //修改权益
            Integer[] awardCounts = {null, 100};
            Arrays.stream(awardCounts).forEach(awardCount -> {
                IScene equityEditScene = EquityEditScene.builder().awardCount(awardCount).equityId(equityId).description(EnumDesc.DESC_BETWEEN_15_20.getDesc()).build();
                String message = visitor.invokeApi(equityEditScene, false).getString("message");
                String err = awardCount == null ? "奖励数不能为空" : "次数范围只能在1-99";
                CommonUtil.checkResult("积分为" + awardCount, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权益列表--修改免费洗车，积分异常");
        }
    }

    private JSONObject getResponseByEquityPageScene(IScene equityPageScene) {
        JSONArray list = equityPageScene.invoke(visitor, true).getJSONArray("list");
        JSONObject equityPageResponse = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("service_type_name").equals("vip会员") && e.getString("equity_name").equals("免费洗车")).findFirst().orElse(null);
        Preconditions.checkArgument(equityPageResponse != null, "未找到vip会员免费洗车权益");
        return equityPageResponse;
    }

    //ok
    @Test(description = "任务管理--修改分享内容，applet与pc所见内容一致")
    public void vipMarketing_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumDesc.DESC_BETWEEN_15_20.getDesc(), EnumDesc.DESC_BETWEEN_5_10.getDesc(), EnumDesc.DESC_BETWEEN_10_15.getDesc()};
            IVoucher voucher = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher();
            Integer voucherId = Math.toIntExact(voucher.getVoucherId());
            IScene shareManagerPageScene = ShareManagerPageScene.builder().build();
            JSONArray list = visitor.invokeApi(shareManagerPageScene).getJSONArray("list");
            String description = null;
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonObject = list.getJSONObject(i);
                int id = jsonObject.getInteger("id");
                String businessType = jsonObject.getString("business_type");
                description = Arrays.stream(strings).filter(string -> !jsonObject.getString("description").equals(string)).findFirst().orElse(jsonObject.getString("description"));
                ShareManagerEditScene.builder().id(id).awardCustomerRule("ONCE").taskExplain(description).awardScore(1000).awardCardVolumeId(voucherId).takeEffectType("DAY").dayNumber("2000").businessType(businessType).build().invoke(visitor, true);
            }
            user.loginApplet(APPLET_USER_ONE);
            JSONArray taskList = AppletShareTaskListScene.builder().build().invoke(visitor, true).getJSONArray("task_list");
            String finalDescription = description;
            taskList.stream().map(e -> (JSONObject) e).forEach(e -> CommonUtil.checkResult(e.getString("task_name") + "的内容", finalDescription, e.getString("award_description")));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("任务管理--修改分享内容，applet与pc所见内容一致");
        }
    }

    //ok
    @Test(description = "任务管理--修改分享内容，说明异常")
    public void vipMarketing_system_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IVoucher voucher = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher();
            Integer voucherId = Math.toIntExact(voucher.getVoucherId());
            IScene shareManagerPageScene = ShareManagerPageScene.builder().build();
            JSONArray list = visitor.invokeApi(shareManagerPageScene).getJSONArray("list");
            List<Integer> ids = list.stream().map(e -> (JSONObject) e).map(e -> e.getInteger("id")).collect(Collectors.toList());
            ids.forEach(id -> {
                String[] taskExplains = {null, EnumDesc.DESC_BETWEEN_200_300.getDesc()};
                Arrays.stream(taskExplains).forEach(desc -> {
                    IScene shareManagerEditScene = ShareManagerEditScene.builder().id(id).taskExplain(desc).dayNumber("9")
                            .awardScore(1000).awardCustomerRule("ONCE").awardCardVolumeId(voucherId).takeEffectType(TaskEffectTypeEnum.DAY.name()).build();
                    String message = visitor.invokeApi(shareManagerEditScene, false).getString("message");
                    String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(desc) ? "说明不能为空" : "备注只能在0-20";
                    CommonUtil.checkResult("说明为" + desc, err, message);
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("任务管理--修改分享内容，说明异常");
        }
    }

    //ok
    @Test(description = "签到配置--修改签到配置，applet与pc所见内容一致")
    public void vipMarketing_system_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene signInConfigPageScene = SignInConfigPageScene.builder().build();
            JSONArray list = visitor.invokeApi(signInConfigPageScene).getJSONArray("list");
            int id = list.getJSONObject(0).getInteger("id");
            IScene signInConfigEditScene = SignInConfigEditScene.builder().signInConfigId(id).awardScore(999).explain(EnumDesc.DESC_BETWEEN_20_30.getDesc()).build();
            visitor.invokeApi(signInConfigEditScene);
            user.loginApplet(APPLET_USER_ONE);
            IScene messageFormPageScene = AppletSignInDetailScene.builder().build();
            Integer signInScore = visitor.invokeApi(messageFormPageScene).getInteger("sign_in_score");
            CommonUtil.checkResultPlus("pc修改后签到积分为", 999, "小程序签到可得积分为", signInScore);
            user.loginPc(ALL_AUTHORITY);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("签到配置--修改签到配置，applet与pc所见内容一致");
        }
    }

    //ok
    @Test(description = "签到配置--签到配置修改，变更记录+1，变更积分为修改的积分&操作时间为当前时间&操作账号为当前账号&备注为修改的详情")
    public void vipMarketing_system_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = SignInConfigPageScene.builder().build().invoke(visitor, true).getJSONArray("list").getJSONObject(0);
            int signInConfigId = response.getInteger("id");
            int awardScore = response.getInteger("award_score") >= 1999 ? 1 : response.getInteger("award_score");
            int recordTotal = SignInConfigChangeRecordScene.builder().signInConfigId(signInConfigId).build().invoke(visitor, true).getInteger("total");
            //变更积分&说明
            SignInConfigEditScene.builder().signInConfigId(signInConfigId).awardScore(awardScore + 1).explain(EnumDesc.DESC_BETWEEN_15_20.getDesc()).build().invoke(visitor, true);
            //变更后列表数
            int newRecordTotal = SignInConfigChangeRecordScene.builder().signInConfigId(signInConfigId).build().invoke(visitor, true).getInteger("total");
            CommonUtil.checkResult("签到任务变更记录总数", recordTotal + 1, newRecordTotal);
            //变更内容
            JSONObject newResponse = SignInConfigChangeRecordScene.builder().build().invoke(visitor, true).getJSONArray("list").getJSONObject(0);
            CommonUtil.checkResult("操作员手机号", ALL_AUTHORITY.getPhone(), newResponse.getString("operate_phone"));
            CommonUtil.checkResult("变更积分", awardScore + 1, newResponse.getInteger("change_score"));
            CommonUtil.checkResult("变更备注", EnumDesc.DESC_BETWEEN_15_20.getDesc(), newResponse.getString("change_remark"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("签到配置--签到配置修改，变更记录+1，变更积分为修改的积分&操作时间为当前时间&操作账号为当前账号&备注为修改的详情");
        }
    }

    //ok
    @Test(description = "权益列表--修改生日积分，积分异常")
    public void vipMarketing_system_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene equityPageScene = EquityPageScene.builder().build();
            Integer equityId = visitor.invokeApi(equityPageScene).getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getString("equity_name").equals("生日积分")).map(e -> e.getInteger("equity_id")).findFirst().orElse(0);
            Integer[] awardCounts = {null, 10001};
            Arrays.stream(awardCounts).forEach(awardCount -> {
                IScene equityEditScene = EquityEditScene.builder().awardCount(awardCount).equityId(equityId).description(EnumDesc.DESC_BETWEEN_15_20.getDesc()).build();
                String message = visitor.invokeApi(equityEditScene, false).getString("message");
                String err = awardCount == null ? "奖励数不能为空" : "次数范围1-1000";
                CommonUtil.checkResult("积分为" + awardCount, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权益列表--修改生日积分，积分异常");
        }
    }

    //ok
    @Test(description = "洗车管理--增加洗车次数，洗车记录列表+1，小程序客户洗车次数更新")
    public void vipMarketing_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene adjustNumberRecordScene = WashCarManagerAdjustNumberRecordScene.builder().build();
            int washRecordTotal = visitor.invokeApi(adjustNumberRecordScene).getInteger("total");
            //增加洗车次数
            WashCarManagerAdjustNumberScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).adjustNumber("1").remark(EnumDesc.DESC_BETWEEN_5_10.getDesc()).build().invoke(visitor, true);
            //调整记录次数
            int newWashRecordTotal = visitor.invokeApi(adjustNumberRecordScene).getInteger("total");
            IScene newAdjustNumberRecordScene = WashCarManagerAdjustNumberRecordScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
            JSONObject response = visitor.invokeApi(newAdjustNumberRecordScene).getJSONArray("list").getJSONObject(0);
            int afterNumber = response.getInteger("after_number");
            CommonUtil.checkResult("增加" + APPLET_USER_ONE.getPhone() + "洗车次数3次后调整记录列表数", washRecordTotal + 1, newWashRecordTotal);
            //小程序洗车次数
            user.loginApplet(APPLET_USER_ONE);
            String remainNumber = visitor.invokeApi(AppletCarWashRemainNumberScene.builder().build()).getString("remainNumber");
            CommonUtil.checkResult("增加" + APPLET_USER_ONE.getPhone() + "洗车次数3次后小程序洗车次数", String.valueOf(afterNumber), remainNumber);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("洗车管理--增加洗车次数，洗车记录列表+1，小程序客户洗车次数更新");
        }
    }

    //ok
    @Test(description = "洗车管理--修改洗车规则，applet与pc所见内容一致")
    public void vipMarketing_system_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumDesc.DESC_BETWEEN_200_300.getDesc(), EnumDesc.DESC_BETWEEN_400_500.getDesc(), EnumDesc.DESC_BETWEEN_10_15.getDesc()};
            String ruleDetail = WashCarManagerRuleDetailScene.builder().build().invoke(visitor, true).getString("rule_detail");
            String detail = Arrays.stream(strings).filter(e -> !e.equals(ruleDetail)).findFirst().orElse(ruleDetail);
            WashCarManagerEditRuleScene.builder().ruleDetail(detail).build().invoke(visitor, true);
            user.loginApplet(APPLET_USER_ONE);
            JSONObject jsonObject = AppletCommonRuleExplainDetailScene.builder().businessType(AppletCodeBusinessTypeEnum.WASH_CAR.getKey()).build().invoke(visitor, true);
            CommonUtil.checkResultPlus("pc端洗车规则说明为", detail, "applet端洗车规则说明为", jsonObject.getString("rule_explain"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("洗车管理--修改洗车规则，applet与pc所见内容一致");
        }
    }

    //ok
    @Test(description = "洗车管理--修改洗车规则，内容异常")
    public void vipMarketing_system_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumDesc.DESC_BETWEEN_1000_2000.getDesc(), null, ""};
            Arrays.stream(strings).forEach(detail -> {
                String message = WashCarManagerEditRuleScene.builder().ruleDetail(detail).build().invoke(visitor, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(detail) ? "洗车规则说明不能为空" : "洗车规则说明只能在1-1000字";
                CommonUtil.checkResult("pc端洗车权益内容为", detail, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("洗车管理--修改洗车规则，内容异常");
        }
    }

    //ok
    @Test(description = "洗车管理--修改洗车权益，applet与pc所见内容一致")
    public void vipMarketing_system_18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumDesc.DESC_BETWEEN_200_300.getDesc(), EnumDesc.DESC_BETWEEN_400_500.getDesc(), EnumDesc.DESC_BETWEEN_10_15.getDesc()};
            String equityDetail = WashCarManagerEquityDetailScene.builder().build().invoke(visitor, true).getString("equity_detail");
            String detail = Arrays.stream(strings).filter(e -> !e.equals(equityDetail)).findFirst().orElse(equityDetail);
            WashCarManagerEditEquityScene.builder().equityDetail(detail).build().invoke(visitor, true);
            user.loginApplet(APPLET_USER_ONE);
            JSONObject jsonObject = AppletCommonRuleExplainDetailScene.builder().businessType(AppletCodeBusinessTypeEnum.WASH_CAR.getKey()).build().invoke(visitor, true);
            CommonUtil.checkResultPlus("pc端洗车权益为", detail, "applet端洗车权益为", jsonObject.getString("equity_explain"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("洗车管理--修改洗车权益，applet与pc所见内容一致");
        }
    }

    //ok
    @Test(description = "洗车管理--修改洗车权益，内容异常")
    public void vipMarketing_system_19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, EnumDesc.DESC_BETWEEN_1000_2000.getDesc(), ""};
            Arrays.stream(strings).forEach(detail -> {
                String message = WashCarManagerEditEquityScene.builder().equityDetail(detail).build().invoke(visitor, false).getString("message");
                String err = detail == null ? "洗车权益说明不能为空" : "洗车规则说明只能在1-1000字";
                CommonUtil.checkResult("pc端洗车权益内容为", detail, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("洗车管理--修改洗车权益，applet与pc所见内容一致");
        }
    }

    //ok
    @Test(description = "洗车管理--增加洗车次数，手机号异常")
    public void vipMarketing_system_20() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] phones = {"11111111111", "010-23886688", null, ""};
            Arrays.stream(phones).forEach(phone -> {
                String message = WashCarManagerAdjustNumberScene.builder().customerPhone(phone).adjustNumber("1")
                        .remark(EnumDesc.DESC_BETWEEN_15_20.getDesc()).build().invoke(visitor, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(phone) ? "手机号不能为空" : "手机号格式不正确";
                CommonUtil.checkResult("手机号异常为", phone, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("洗车管理--增加洗车次数，手机号异常");
        }
    }

    //ok
    @Test(description = "洗车管理--增加洗车次数，次数异常")
    public void vipMarketing_system_21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] adjustNumbers = {"", "6", null, "0"};
            Arrays.stream(adjustNumbers).forEach(adjustNumber -> {
                String message = WashCarManagerAdjustNumberScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                        .adjustNumber(adjustNumber).remark(EnumDesc.DESC_BETWEEN_15_20.getDesc()).build().invoke(visitor, false).getString("message");
                String err = com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils.isEmpty(adjustNumber) ? "调整次数不能为空" : "调整次数只能在1-5";
                CommonUtil.checkResult("手机号异常为", adjustNumber, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("洗车管理--增加洗车次数，次数异常");
        }
    }

    //ok
    @Test(description = "洗车管理--增加洗车次数，备注异常")
    public void vipMarketing_system_22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] remarks = {"", EnumDesc.DESC_BETWEEN_20_30.getDesc(), null};
            Arrays.stream(remarks).forEach(remark -> {
                String message = WashCarManagerAdjustNumberScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                        .adjustNumber("1").remark(remark).build().invoke(visitor, false).getString("message");
                String err = StringUtils.isEmpty(remark) ? "备注不能为空" : "备注不得超过20个字";
                CommonUtil.checkResult("手机号异常为", remark, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("洗车管理--增加洗车次数，备注异常");
        }
    }

    private <T> List<T> getList(T str) {
        List<T> list = new ArrayList<>();
        list.add(str);
        return list;
    }
}


