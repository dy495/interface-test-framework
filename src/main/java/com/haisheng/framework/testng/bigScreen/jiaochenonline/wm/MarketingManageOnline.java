package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.CreatePackage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PackageFormPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PurchaseFixedPackage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PurchaseTemporaryPackage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.wm.util.BusinessUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 营销管理模块测试用例
 */
public class MarketingManageOnline extends TestCaseCommon implements TestCaseStd {
    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
    BusinessUtilOnline util = new BusinessUtilOnline();
    private static final Integer size = 100;
    private static final EnumAccount marketing = EnumAccount.MARKETING;
    private static final EnumAccount administrator = EnumAccount.ADMINISTRATOR;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_JIAOCHEN_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.produce = EnumProduce.JC.name();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.JIAOCHEN_DAILY.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        util.login(marketing);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "卡券表单--新建卡券--卡券名称异常")
    public void voucherManage_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumContent.B.getContent(), "1", null, ""};
            for (String name : strings) {
                Long stock = 1000L;
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(name).subjectType(util.getSubjectType())
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
                        .shopType(0).shopIds(util.getShopIds()).selfVerification(true).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = StringUtils.isEmpty(name) ? "卡券名称不能为空" : "卡券名称长度应为2～20个字";
                Preconditions.checkArgument(message.equals(err), "卡券名称为：" + name + "创建成功");
                CommonUtil.logger(name);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--新建卡券--卡券名称异常");
        }
    }

    @Test(description = "卡券表单--新建卡券--卡券说明异常")
    public void voucherManage_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, "", EnumContent.C.getContent()};
            for (String desc : strings) {
                Long stock = 1000L;
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName()).subjectType(util.getSubjectType())
                        .voucherDescription(desc).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
                        .shopType(0).shopIds(util.getShopIds()).selfVerification(true).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = StringUtils.isEmpty(desc) ? "卡券说明不能为空" : "卡券描述不能超过200个字";
                Preconditions.checkArgument(message.equals(err), "卡券说明为：" + desc + "创建成功");
                CommonUtil.logger(desc);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--新建卡券--卡券说明异常");
        }
    }

    @Test(description = "卡券表单--新建卡券--主体类型异常")
    public void voucherManage_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {"全部权限", null, ""};
            for (String subjectType : strings) {
                Long stock = 1000L;
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName()).subjectType(subjectType)
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(subjectType)).stock(stock).cost(util.getCost(stock))
                        .shopType(0).shopIds(util.getShopIds()).selfVerification(true).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                Preconditions.checkArgument(message.equals("主体类型不存在"), "主体类型为：" + subjectType + "创建成功");
                CommonUtil.logger(subjectType);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--新建卡券--主体类型异常");
        }
    }

    @Test(description = "卡券表单--新建卡券--主体详情异常")
    public void voucherManage_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long stock = 1000L;
            IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName()).subjectType(util.getSubjectType())
                    .voucherDescription(util.getDesc()).stock(stock).cost(util.getCost(stock))
                    .shopType(0).shopIds(util.getShopIds()).selfVerification(true).build();
            String message = jc.invokeApi(scene, false).getString("message");
            CommonUtil.valueView(message);
            Preconditions.checkArgument(message.equals("主体详情不能为空"), "主体详情为：" + null + "创建成功");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--新建卡券--主体详情异常");
        }
    }

    @Test(description = "卡券表单--新建卡券--库存数量异常情况")
    public void voucherManage_system_5() {
        Long[] longs = {1000000000L, null, -100L, 9999999999L};
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (Long stock : longs) {
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName()).subjectType(util.getSubjectType())
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
                        .shopType(0).shopIds(util.getShopIds()).selfVerification(true).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = StringUtils.isEmpty(stock) ? "库存不能为空" : stock > 1000000000L ? "请求入参类型不正确" : "卡券库存范围应在0 ～ 100000000张";
                Preconditions.checkArgument(message.equals(err), "卡券库存为：" + stock + "创建成功");
                CommonUtil.logger(stock);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--新建卡券--库存数量异常情况");
        }
    }

    @Test(description = "卡券表单--新建卡券--业务类型异常情况")
    public void voucherManage_system_6() {
        Integer[] integers = {null, -1, 100};
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long stock = 1000L;
            for (Integer shopType : integers) {
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName()).subjectType(util.getSubjectType())
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
                        .shopType(shopType).shopIds(util.getShopIds()).selfVerification(true).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = StringUtils.isEmpty(shopType) ? "业务类型不能为空" : "业务类型不存在";
                Preconditions.checkArgument(message.equals(err), "业务类型为：" + shopType + "创建成功");
                CommonUtil.logger(shopType);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--新建卡券--业务类型异常情况");
        }
    }

    @Test(description = "卡券表单--新建卡券--成本异常情况")
    public void voucherManage_system_7() {
        Double[] doubles = {null, (double) -1, (double) 1000000000, 100000000.11, 99999.999};
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long stock = 1000L;
            for (Double cost : doubles) {
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName()).subjectType(util.getSubjectType())
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(cost)
                        .shopType(0).shopIds(util.getShopIds()).selfVerification(true).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = StringUtils.isEmpty(cost) ? "成本不能为空" : "卡券成本金额范围应在0 ～ 100000000元";
                Preconditions.checkArgument(message.equals(err), "成本为：" + cost + "创建成功");
                CommonUtil.logger(cost);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--新建卡券--成本异常情况");
        }
    }

    @Test(description = "卡券表单--新建卡券--选择门店异常")
    public void voucherManage_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long stock = 1000L;
            IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName())
                    .subjectType(util.getSubjectType()).voucherDescription(util.getDesc())
                    .subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
                    .shopType(0).selfVerification(true).build();
            String message = jc.invokeApi(scene, false).getString("message");
            CommonUtil.valueView(message);
            Preconditions.checkArgument(message.equals("卡券适用门店列表不能为空"), "卡券适用门店列表为：" + null + "创建成功");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--新建卡券--成本异常情况");
        }
    }

    @Test(description = "卡券表单--新建卡券--列表数+1&发行库存=创建时填写数量&成本=创建时填写的成本")
    public void voucherManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            long stock = 1000L;
            //获取列表数据
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            //创建卡券
            String voucherName = util.createVoucher(stock);
            //创建后此卡券
            int newTotal = jc.invokeApi(builder.build()).getInteger("total");
            Preconditions.checkArgument(newTotal == total + 1, "创建卡券前卡券列表数量为：" + total + " 创建卡券后数量为：" + newTotal);
            builder.voucherName(voucherName);
            JSONObject data = jc.invokeApi(builder.build());
            int issueInventory = CommonUtil.getIntField(data, 0, "issue_inventory");
            Preconditions.checkArgument(issueInventory == stock, "创建卡券时数量为1000，创建完成后列表展示发行库存为：" + issueInventory);
            Double cost = data.getJSONArray("list").getJSONObject(0).getDouble("cost");
            Preconditions.checkArgument(cost == (double) 50, "创建卡券时成本为:" + (double) 50 + "，创建完成后列表展示成本为：" + cost);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--新建卡券--列表数+1&发行库存=创建时填写数量&成本=创建时填写的成本");
        }
    }

    @Test(description = "卡券表单--作废卡券--作废时间=当前时间&&作废账号=当前操作账号&&发放状态=已作废")
    public void voucherManage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            //创建卡券
            String voucherName = util.createVoucher(1000L);
            //作废此卡券
            util.invalidVoucher(voucherName);
            IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
            JSONObject response = jc.invokeApi(scene);
            String invalidTime = CommonUtil.getStrField(response, 0, "create_time");
            String invalidAccount = CommonUtil.getStrField(response, 0, "invalid_account");
            String invalidStatusName = CommonUtil.getStrField(response, 0, "invalid_status_name");
            Preconditions.checkArgument(invalidTime.equals(date), "作废时间：" + invalidTime + " 当前时间：" + date);
            Preconditions.checkArgument(invalidAccount.equals(marketing.getPhone()), "作废账号：" + invalidAccount + " 当前操作账号" + marketing.getPhone());
            Preconditions.checkArgument(invalidStatusName.equals(EnumVoucherStatus.INVALID.getName()), "发放状态：" + invalidStatusName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--作废卡券--作废时间=当前时间&&作废账号=当前操作账号&&发放状态=已作废");
        }
    }

    @Test(description = "卡券表单--增发卡券--审批通过前，增发库存不变，剩余客库存不变")
    public void voucherManage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //增发前剩余库存和增发库存
            int surplusInventory = 0;
            int additionalInventory = 0;
            String voucherName = null;
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getString("audit_status_name").equals("已通过")
                            && !array.getJSONObject(j).getString("invalid_status_name").equals("已作废")) {
                        surplusInventory = array.getJSONObject(j).getInteger("surplus_inventory");
                        additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
                        voucherName = array.getJSONObject(j).getString("voucher_name");
                        break;
                    }
                }
            }
            //增发
            util.addVoucher(voucherName, 10);
            //增发后数据
            builder.voucherName(voucherName);
            JSONObject response = jc.invokeApi(builder.build());
            int newSurplusInventory = CommonUtil.getIntField(response, 0, "surplus_inventory");
            int newAdditionalInventory = CommonUtil.getIntField(response, 0, "additional_inventory");
            CommonUtil.valueView(surplusInventory, newSurplusInventory, additionalInventory, newAdditionalInventory);
            Preconditions.checkArgument(newSurplusInventory == surplusInventory, "增发前剩余库存：" + surplusInventory + "增发后剩余库存：" + newSurplusInventory);
            Preconditions.checkArgument(newAdditionalInventory == additionalInventory, "增发前增发库存：" + additionalInventory + "增发后增发库存：" + newAdditionalInventory);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--增发卡券--审批通过前，增发库存=增发库存");
        }
    }

    @Test(description = "卡券表单--增发卡券--审批通过后，新增发库存=原增发库存+增发数量，新剩余库存=原剩余库存+增发数量")
    public void voucherManage_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //增发前剩余库存和增发库存
            int surplusInventory = 0;
            int additionalInventory = 0;
            String voucherName = null;
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getString("audit_status_name").equals("已通过")
                            && !array.getJSONObject(j).getString("invalid_status_name").equals("已作废")) {
                        surplusInventory = array.getJSONObject(j).getInteger("surplus_inventory");
                        additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
                        voucherName = array.getJSONObject(j).getString("voucher_name");
                        break;
                    }
                }
            }
            //增发
            util.addVoucher(voucherName, 10);
            //审批通过
            util.applyVoucher(voucherName, "1");
            //审批后数据
            builder.voucherName(voucherName);
            JSONObject response = jc.invokeApi(builder.build());
            int newSurplusInventory = CommonUtil.getIntField(response, 0, "surplus_inventory");
            int newAdditionalInventory = CommonUtil.getIntField(response, 0, "additional_inventory");
            CommonUtil.valueView(surplusInventory, newSurplusInventory, additionalInventory, newAdditionalInventory);
            Preconditions.checkArgument(newSurplusInventory == surplusInventory + 10, "增发前剩余库存：" + surplusInventory + "增发后剩余库存：" + newSurplusInventory);
            Preconditions.checkArgument(newAdditionalInventory == additionalInventory + 10, "增发前增发库存：" + additionalInventory + "增发后增发库存：" + newAdditionalInventory);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--增发卡券--审批通过后，新增发库存=原增发库存+增发数量，新剩余库存=原剩余库存+增发数量");
        }
    }

    @Test(description = "卡券表单--增发卡券--审批通过后，新增发库存=原增发库存，新剩余库存=原剩余库存")
    public void voucherManage_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //增发前剩余库存和增发库存
            int surplusInventory = 0;
            int additionalInventory = 0;
            String voucherName = null;
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getString("audit_status_name").equals("已通过")
                            && !array.getJSONObject(j).getString("invalid_status_name").equals("已作废")) {
                        surplusInventory = array.getJSONObject(j).getInteger("surplus_inventory");
                        additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
                        voucherName = array.getJSONObject(j).getString("voucher_name");
                        break;
                    }
                }
            }
            //增发
            util.addVoucher(voucherName, 10);
            //审批不通过
            util.applyVoucher(voucherName, "2");
            //审批后数据
            builder.voucherName(voucherName);
            JSONObject response = jc.invokeApi(builder.build());
            int newSurplusInventory = CommonUtil.getIntField(response, 0, "surplus_inventory");
            int newAdditionalInventory = CommonUtil.getIntField(response, 0, "additional_inventory");
            CommonUtil.valueView(surplusInventory, newSurplusInventory, additionalInventory, newAdditionalInventory);
            Preconditions.checkArgument(newSurplusInventory == surplusInventory, "增发前剩余库存：" + surplusInventory + "增发后剩余库存：" + newSurplusInventory);
            Preconditions.checkArgument(newAdditionalInventory == additionalInventory, "增发前增发库存：" + additionalInventory + "增发后增发库存：" + newAdditionalInventory);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--增发卡券--审批通过后，新增发库存=原增发库存，新剩余库存=原剩余库存");
        }
    }

    @Test(description = "卡券表单--剩余库存=发行库存+增发库存-累计发出")
    public void voucherManage_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.login(administrator);
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    String voucherName = array.getJSONObject(j).getString("voucher_name");
                    Integer issueInventory = array.getJSONObject(j).getInteger("issue_inventory");
                    Integer additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
                    Integer surplusInventory = array.getJSONObject(j).getInteger("surplus_inventory");
                    Integer cumulativeDelivery = array.getJSONObject(j).getInteger("cumulative_delivery");
                    if (issueInventory != null && surplusInventory != null) {
                        CommonUtil.valueView(voucherName);
                        int num = additionalInventory + issueInventory - cumulativeDelivery;
                        CommonUtil.valueView(surplusInventory, num);
                        Preconditions.checkArgument(surplusInventory == num, voucherName + " 剩余库存：" + surplusInventory + "发行库存+增发库存-累计过期-累计使用：" + num);
                        CommonUtil.logger(voucherName);
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--剩余库存=发行库存+增发库存-累计发出");
        }
    }

    @Test(description = "卡券表单--发放状态为未发出时，累计发出=0")
    public void voucherManage_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getString("invalid_status_name").equals(EnumVoucherStatus.UNSENT.getName())) {
                        int cumulativeDelivery = array.getJSONObject(j).getInteger("cumulative_delivery");
                        String voucherName = array.getJSONObject(j).getString("voucher_name");
                        Preconditions.checkArgument(cumulativeDelivery == 0, voucherName + "累计发出数量：" + CommonUtil.errMessage(0, cumulativeDelivery));
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--发放状态为未发出时，累计发出=0");
        }
    }

    @Test(description = "卡券表单--发放状态为已发出时，累计发出>0")
    public void voucherManage_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getString("invalid_status_name").equals(EnumVoucherStatus.SENT.getName())) {
                        int cumulativeUse = array.getJSONObject(j).getInteger("cumulative_use");
                        String voucherName = array.getJSONObject(j).getString("voucher_name");
                        Preconditions.checkArgument(cumulativeUse > 0, voucherName + "累计发出数量：" + CommonUtil.errMessage(">0", cumulativeUse));
                        CommonUtil.logger(voucherName);
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--发放状态为已发出时，累计发出>0");
        }
    }

    @Test(description = "卡券表单--累计发出>=累计过期&&累计发出>=累计使用&&累计发出>=累计使用+累计过期")
    public void voucherManage_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    String voucherName = array.getJSONObject(j).getString("voucher_name");
                    int cumulativeUse = array.getJSONObject(j).getInteger("cumulative_use");
                    int cumulativeOverdue = array.getJSONObject(j).getInteger("cumulative_overdue");
                    int cumulativeDelivery = array.getJSONObject(j).getInteger("cumulative_delivery");
                    Preconditions.checkArgument(cumulativeDelivery >= cumulativeOverdue, voucherName + "累计发出：" + cumulativeDelivery + "累计过期：" + cumulativeOverdue);
                    Preconditions.checkArgument(cumulativeDelivery >= cumulativeUse, voucherName + "累计发出：" + cumulativeDelivery + "累计使用：" + cumulativeUse);
                    Preconditions.checkArgument(cumulativeDelivery >= cumulativeOverdue + cumulativeUse, voucherName + "累计发出：" + cumulativeDelivery + "累计使用+累计过期+：" + cumulativeOverdue + cumulativeUse);
                    CommonUtil.logger(voucherName);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--累计发出>=累计过期&&累计发出>=累计使用&&累计发出>=累计使用+累计过期");
        }
    }

    @Test(description = "卡券表单--临时套餐购买一张此卡券，累计发出+1")
    public void voucherManage_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray voucherList = util.getVoucherList(1);
            String voucherName = voucherList.getJSONObject(0).getString("voucher_name");
            IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
            long cumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
            //购买临时套餐
            String platNumber = util.getPlatNumber(marketing.getPhone());
            if (!platNumber.equals(EnumStatus.FALSE.getStatus())) {
                IScene temporaryScene = PurchaseTemporaryPackage.builder().customerPhone(marketing.getPhone())
                        .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(platNumber).voucherList(voucherList)
                        .expiryDate("1").remark(EnumContent.B.getContent()).subjectType(util.getSubjectType())
                        .subjectId(util.getSubjectId(util.getSubjectType())).extendedInsuranceYear("1")
                        .extendedInsuranceCopies("1").type(1).build();
                jc.invokeApi(temporaryScene);
                //确认支付
                util.makeSureBuyPackage("临时套餐");
                //购买后累计发出
                long newCumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
                CommonUtil.valueView(cumulativeDelivery, newCumulativeDelivery);
                Preconditions.checkArgument(newCumulativeDelivery == cumulativeDelivery + 1,
                        voucherName + "累计发出数：" + CommonUtil.errMessage(cumulativeDelivery + 1, newCumulativeDelivery));
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--临时套餐购买一张此卡券，累计发出+1");
        }
    }

    @Test(description = "卡券表单--固定套餐购买一个套餐（此套餐内包含此卡券一张）累计发出+1，套餐购买数量+1")
    public void voucherManage_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String phone = marketing.getPhone();
            String subjectType = util.getSubjectType();
            //获取累计发出
            long packageId = util.getPackageId("凯迪拉克无限套餐");
            long voucherId = util.getPackageContainVoucher(packageId).get(0);
            String voucherName = util.getVoucherName(voucherId);
            IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
            int cumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
            //购买固定套餐
            IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(phone)
                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(phone))
                    .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
                    .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear(10)
                    .extendedInsuranceCopies(10).type(0).build();
            jc.invokeApi(purchaseFixedPackageScene);
            //确认支付
            util.makeSureBuyPackage("凯迪拉克无限套餐");
            //购买后累计发出
            long newCumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
            CommonUtil.valueView(cumulativeDelivery, newCumulativeDelivery);
            Preconditions.checkArgument(newCumulativeDelivery == cumulativeDelivery + 1,
                    voucherName + "累计发出数：" + CommonUtil.errMessage(cumulativeDelivery + 1, newCumulativeDelivery));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--固定套餐购买一个套餐（此套餐内包含此卡券一张）累计发出+1，套餐购买数量+1");
        }
    }

    @Test(description = "卡券表单--累计发出=【发卡记录】中按该卡券名称搜索结果的列表数")
    public void voucherManage_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String format = "yyyy-MM-dd HH:mm";
            String date = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -2), format);
            long time = Long.parseLong(DateTimeUtil.dateToStamp(date, format));
            util.login(administrator);
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    String createTime = array.getJSONObject(j).getString("create_time");
                    if (Long.parseLong(DateTimeUtil.dateToStamp(createTime, format)) > time) {
                        String voucherName = array.getJSONObject(j).getString("voucher_name");
                        //累计发出数
                        long cumulativeDelivery = array.getJSONObject(j).getLong("cumulative_delivery");
                        //发卡记录数
                        IScene scene = SendRecord.builder().voucherName(voucherName).build();
                        long sendRecordTotal = jc.invokeApi(scene).getLong("total");
                        CommonUtil.valueView(cumulativeDelivery, sendRecordTotal);
                        Preconditions.checkArgument(sendRecordTotal == cumulativeDelivery,
                                voucherName + "累计发出数：" + cumulativeDelivery + "发卡记录数：" + sendRecordTotal);
                        CommonUtil.logger(voucherName);
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--累计发出=【发卡记录】中按该卡券名称搜索结果的列表数");
        }
    }

    @Test(description = "卡券表单--累计使用=【核销记录】中按该卡券名称搜索结果的列表数")
    public void voucherManage_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String format = "yyyy-MM-dd HH:mm";
            String date = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -2), format);
            long time = Long.parseLong(DateTimeUtil.dateToStamp(date, format));
            util.login(administrator);
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    String createTime = array.getJSONObject(j).getString("create_time");
                    if (Long.parseLong(DateTimeUtil.dateToStamp(createTime, format)) > time) {
                        String voucherName = array.getJSONObject(j).getString("voucher_name");
                        //累计使用
                        long cumulativeUse = array.getJSONObject(j).getLong("cumulative_use");
                        IScene scene = VerificationRecord.builder().voucherName(voucherName).build();
                        //核销数量
                        long verificationRecordTotal = jc.invokeApi(scene).getLong("total");
                        CommonUtil.valueView(cumulativeUse, verificationRecordTotal);
                        Preconditions.checkArgument(cumulativeUse == verificationRecordTotal,
                                voucherName + "累计发出数：" + cumulativeUse + "发卡记录数：" + verificationRecordTotal);
                        CommonUtil.logger(voucherName);
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--累计使用=【核销记录】中按该卡券名称搜索结果的列表数");
        }
    }

    @Test(description = "卡券表单--增发库存=【卡券审核】该卡券申请记录中通过的增发总数")
    public void voucherManage_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    String voucherName = array.getJSONObject(j).getString("voucher_name");
                    int additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
                    int numSum = 0;
                    //卡券审核页
                    ApplyPage.ApplyPageBuilder applyBuilder = ApplyPage.builder().status("AGREE").name(voucherName);
                    int total1 = jc.invokeApi(applyBuilder.build()).getInteger("total");
                    int x = CommonUtil.getTurningPage(total1, size);
                    for (int i1 = 1; i1 < x; i1++) {
                        JSONArray applyArray = jc.invokeApi(applyBuilder.page(i1).size(size).build()).getJSONArray("list");
                        for (int j1 = 0; j1 < applyArray.size(); j1++) {
                            if (applyArray.getJSONObject(j1).getString("apply_type_name").equals("增发")) {
                                numSum += applyArray.getJSONObject(j1).getInteger("num");
                            }
                        }
                    }
                    CommonUtil.valueView(additionalInventory, numSum);
                    Preconditions.checkArgument(additionalInventory == numSum,
                            voucherName + "增发库存为：" + additionalInventory + "申请记录中通过的增发总数：" + numSum);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--增发库存=【卡券审核】该卡券申请记录中通过的增发总数");
        }
    }

    @Test(description = "卡券表单--创建一种卡券，【卡券审核】列表+1")
    public void voucherManage_data_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建卡券前卡券审核页列表数
            ApplyPage.ApplyPageBuilder builder = ApplyPage.builder();
            Long total = jc.invokeApi(builder.build()).getLong("total");
            //创建一种卡券
            util.createVoucher(1000L);
            //创建卡券后卡券审核页列表数
            Long newTotal = jc.invokeApi(builder.build()).getLong("total");
            Preconditions.checkArgument(newTotal == total + 1,
                    "【卡券审核】列表数：" + CommonUtil.errMessage(total + 1, newTotal));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--增发库存=【卡券审核】该卡券所有通过申请记录(标记为增发)发出个数之和");
        }
    }

    @Test(description = "卡券表单--每增发一次卡券，【卡券审核】列表+1")
    public void voucherManage_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //增发卡券前卡券审核页列表数
            ApplyPage.ApplyPageBuilder applyBuild = ApplyPage.builder();
            Long applyTotal = jc.invokeApi(applyBuild.build()).getLong("total");
            //增发一种卡券
            String voucherName = null;
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getString("audit_status_name").equals("已通过")
                            && !array.getJSONObject(j).getString("invalid_status_name").equals("已作废")) {
                        voucherName = array.getJSONObject(j).getString("voucher_name");
                        break;
                    }
                }
            }
            //增发卡券
            util.addVoucher(voucherName, 10);
            //增发卡券后卡券审核页列表数
            Long newApplyTotal = jc.invokeApi(applyBuild.build()).getLong("total");
            CommonUtil.valueView(applyTotal, newApplyTotal);
            Preconditions.checkArgument(newApplyTotal == applyTotal + 1,
                    "【卡券审核】列表数：" + CommonUtil.errMessage(applyTotal + 1, newApplyTotal));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--每增发一次卡券，【卡券审核】列表+1");
        }
    }

    @Test(description = "卡券表单--创建一张卡券后，【创建套餐】下拉选择列表数=卡券列表数（未作废&剩余库存！=0）")
    public void voucherManage_data_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            util.login(administrator);
            JSONArray list = jc.pcVoucherList().getJSONArray("list");
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int listSize = 0;
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (!array.getJSONObject(j).getString("invalid_status_name").equals("已作废")
                            && !array.getJSONObject(j).getString("audit_status_name").equals("已拒绝")
                            && !array.getJSONObject(j).getString("audit_status_name").equals("审核中")) {
                        listSize++;
                    }
                }
            }
            CommonUtil.valueView(list.size(), listSize);
            Preconditions.checkArgument(list.size() == listSize,
                    "卡券列表数：" + listSize + "下拉选择列表：" + list.size());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--创建一张卡券后，【创建套餐】下拉选择列表数=卡券列表数（未作废&剩余库存！=0）");
        }
    }

    @Test(description = "核销人员--创建异页核销,名称异常")
    public void voucherManage_system_30() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, EnumContent.B.getContent()};
            for (String name : strings) {
                IScene scene = CreateVerificationPeople.builder().verificationPersonName(name)
                        .verificationPersonPhone("13663366788").status(1).type(1).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = StringUtils.isEmpty(name) ? "核销人员名字不能为空" : "核销人员名字必须为1～20个字";
                Preconditions.checkArgument(message.equals(err),
                        "核销人员名字为：" + name + CommonUtil.errMessage(err, message));
                CommonUtil.logger(name);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建异页核销,名称异常");
        }
    }

    @Test(description = "核销人员--创建财务核销,名称异常")
    public void voucherManage_system_37() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, EnumContent.B.getContent()};
            for (String name : strings) {
                IScene scene = CreateVerificationPeople.builder().verificationPersonName(name)
                        .verificationPersonPhone("13663366788").status(1).type(1).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = StringUtils.isEmpty(name) ? "核销人员名字不能为空" : "核销人员名字必须为1～20个字";
                Preconditions.checkArgument(message.equals(err), "核销人员名字为：" + name + "创建成功");
                CommonUtil.logger(name);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,名称异常");
        }
    }

    @Test(description = "核销人员--创建财务核销,电话异常")
    public void voucherManage_system_38() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, "", "11111111111", "1337316680", "133731668062"};
            for (String phone : strings) {
                IScene scene = CreateVerificationPeople.builder().verificationPersonName("郭丽雅")
                        .verificationPersonPhone(phone).status(1).type(0).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = "手机号格式不正确";
                Preconditions.checkArgument(message.equals(err),
                        "手机号格式为：" + phone + CommonUtil.errMessage(err, message));
                CommonUtil.logger(phone);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,电话异常");
        }
    }

    @Test(description = "核销人员--创建财务核销,电话存在")
    public void voucherManage_system_39() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {util.getRepetitionVerificationPhone()};
            for (String phone : strings) {
                IScene scene = CreateVerificationPeople.builder().verificationPersonName("郭丽雅")
                        .verificationPersonPhone(phone).status(1).type(0).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = "手机号已存在";
                Preconditions.checkArgument(message.equals(err), "手机号格式为：" + phone + CommonUtil.errMessage(err, message));
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,电话存在");
        }
    }

    @Test(description = "核销人员--创建财务核销,列表数+1&创建异页核销,列表数+1", enabled = false)
    public void voucherManage_data_30() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VerificationPeople.builder().build();
            //查询列表数
            int total = jc.invokeApi(scene).getInteger("total");
            util.login(administrator);
            String phone = util.getStaffPhone();
            if (!phone.equals(EnumStatus.FALSE.name())) {
                CommonUtil.valueView(phone);
                util.login(marketing);
                //创建核销人员
                IScene scene1 = CreateVerificationPeople.builder().verificationPersonName("walawala")
                        .verificationPersonPhone(phone).type(0).status(1).build();
                jc.invokeApi(scene1);
                //创建核销人员后数据
                int newTotal = jc.invokeApi(scene).getInteger("total");
                Preconditions.checkArgument(newTotal == total + 1, "列表数：" + CommonUtil.errMessage(total + 1, newTotal));
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,列表数+1&创建异页核销,列表数+1");
        }
    }

    @Test(description = "核销人员--创建异页核销,列表数+1")
    public void voucherManage_data_31() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VerificationPeople.builder().build();
            //查询列表数
            int total = jc.invokeApi(scene).getInteger("total");
            String phone = util.getDistinctPhone();
            util.login(marketing);
            IScene scene1 = CreateVerificationPeople.builder().verificationPersonName("walawala")
                    .verificationPersonPhone(phone).type(1).status(1).build();
            jc.invokeApi(scene1);
            int newTotal = jc.invokeApi(scene).getInteger("total");
            CommonUtil.valueView(total, newTotal);
            Preconditions.checkArgument(newTotal == total + 1, "列表数：" + CommonUtil.errMessage(total + 1, newTotal));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建异页核销,列表数+1");
        }
    }

    @Test(description = "套餐管理--创建套餐，套餐名称异常")
    public void packageManager_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumContent.B.getContent(), "1", null, ""};
            for (String name : strings) {
                IScene scene = CreatePackage.builder().packageName(name).validity("30").packageDescription(util.getDesc())
                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectId(util.getSubjectType()))
                        .voucherList(util.getVoucherList()).packagePrice(5000.00).status(true).shopIds(util.getShopIds()).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = StringUtils.isEmpty(name) ? "套餐名称不能为空" : "套餐名称输入应大于2字小于20字";
                Preconditions.checkArgument(message.equals(err), "套餐名称为：" + name + CommonUtil.errMessage(err, message));
                CommonUtil.logger(name);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐，套餐名称异常");
        }
    }

    @Test(description = "套餐管理--创建套餐，套餐说明异常")
    public void packageManager_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, "", EnumContent.C.getContent()};
            for (String desc : strings) {
                IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30").packageDescription(desc)
                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectId(util.getSubjectType()))
                        .voucherList(util.getVoucherList()).packagePrice(5000.00).status(true).shopIds(util.getShopIds()).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = StringUtils.isEmpty(desc) ? "套餐说明不能为空" : "套餐说明不能超过200字";
                Preconditions.checkArgument(message.equals(err), "套餐说明为：" + desc + CommonUtil.errMessage(err, message));
                CommonUtil.logger(desc);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐，套餐说明异常");
        }
    }

    @Test(description = "套餐管理--创建套餐，有效天数异常")
    public void packageManager_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, "", "2001", "0"};
            for (String validity : strings) {
                IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity(validity)
                        .packageDescription(util.getDesc()).subjectType(util.getSubjectType())
                        .subjectId(util.getSubjectId(util.getSubjectType())).voucherList(util.getVoucherList())
                        .packagePrice(5000.00).status(true).shopIds(util.getShopIds()).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = StringUtils.isEmpty(validity) ? "套餐有效期不能为空" : "有效期请小于2000天";
                Preconditions.checkArgument(message.equals(err), "有效期为：" + validity + CommonUtil.errMessage(err, message));
                CommonUtil.logger(validity);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐，套餐说明异常");
        }
    }

    @Test(description = "套餐管理--创建套餐，主体类型异常")
    public void packageManager_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {"全部权限", null, ""};
            for (String subjectType : strings) {
                IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
                        .packageDescription(util.getDesc()).subjectType(subjectType)
                        .subjectId(util.getSubjectId(subjectType)).voucherList(util.getVoucherList())
                        .packagePrice(5000.00).status(true).shopIds(util.getShopIds()).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = "主体类型不存在";
                Preconditions.checkArgument(message.equals(err), "有效期为：" + subjectType + CommonUtil.errMessage(err, message));
                CommonUtil.logger(subjectType);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐，主体类型异常");
        }
    }

    @Test(description = "套餐管理--创建套餐，主体详情异常")
    public void packageManager_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType())
                    .voucherList(util.getVoucherList())
                    .packagePrice(5000.00).status(true).shopIds(util.getShopIds()).build();
            String message = jc.invokeApi(scene, false).getString("message");
            CommonUtil.valueView(message);
            String err = "主体详情不能为空";
            Preconditions.checkArgument(message.equals(err), "主体详情为：" + null + CommonUtil.errMessage(err, message));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐，主体详情异常");
        }
    }

    @Test(description = "套餐管理--创建套餐，包含卡券为空")
    public void packageManager_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType())
                    .subjectId(util.getSubjectId(util.getSubjectType()))
                    .packagePrice(5000.00).status(true).shopIds(util.getShopIds()).build();
            String message = jc.invokeApi(scene, false).getString("message");
            CommonUtil.valueView(message);
            String err = "所选卡券不能为空";
            Preconditions.checkArgument(message.equals(err), "包含卡券为：" + null + CommonUtil.errMessage(err, message));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐，包含卡券为空");
        }
    }

    @Test(description = "套餐管理--创建套餐，套餐价格异常")
    public void packageManager_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Double[] longs = {null, 100000000.01};
            for (Double packagePrice : longs) {
                IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
                        .packageDescription(util.getDesc()).subjectType(util.getSubjectType())
                        .subjectId(util.getSubjectId(util.getSubjectType())).voucherList(util.getVoucherList())
                        .packagePrice(packagePrice).status(true).shopIds(util.getShopIds()).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                String err = StringUtils.isEmpty(packagePrice) ? "套餐价格不能为空" : "套餐价格不能大于100,000,000";
                Preconditions.checkArgument(message.equals(err), "有效期为：" + packagePrice + CommonUtil.errMessage(err, message));
                CommonUtil.logger(packagePrice);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐，套餐价格异常");
        }
    }

    @Test(description = "套餐管理--创建套餐，选择门店为空")
    public void packageManager_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType())
                    .subjectId(util.getSubjectId(util.getSubjectType())).voucherList(util.getVoucherList())
                    .packagePrice(5000.00).build();
            String message = jc.invokeApi(scene, false).getString("message");
            CommonUtil.valueView(message);
            String err = "套餐适用门店列表不能为空";
            Preconditions.checkArgument(message.equals(err), "有效期为：" + null + CommonUtil.errMessage(err, message));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐，选择门店为空");
        }
    }

    @Test(description = "套餐管理--创建套餐，选择各个主体创建套餐，套餐列表每次均+1")
    public void packageManager_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //登录管理员
            util.login(administrator);
            for (EnumSubject subject : EnumSubject.values()) {
                //创建套餐前列表数量
                IScene scene = PackageFormPage.builder().build();
                int total = jc.invokeApi(scene).getInteger("total");
                //创建套餐
                String subjectType = subject.name();
                Long subjectId = util.getSubjectId(subjectType);
                IScene scene1 = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
                        .packageDescription(util.getDesc()).subjectType(subjectType)
                        .subjectId(subjectId).voucherList(util.getVoucherList())
                        .packagePrice(5000.00).shopIds(util.getShopIds()).build();
                jc.invokeApi(scene1);
                //创建套餐后列表数量
                int newTotal = jc.invokeApi(scene).getInteger("total");
                Preconditions.checkArgument(newTotal == total + 1, "创建套餐前列表数量：" + total + " 创建套餐后列表数量：" + newTotal);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐，选择各个主体创建套餐，套餐列表每次均+1");
        }
    }

    @Test(description = "套餐管理--购买一个固定套餐，该套餐售出+1")
    public void packageManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String phone = marketing.getPhone();
            String subjectType = util.getSubjectType();
            //获取售出套数
            long packageId = util.getPackageId("凯迪拉克无限套餐");
            IScene scene = PackageFormPage.builder().packageName("凯迪拉克无限套餐").build();
            int soldNumber = CommonUtil.getIntField(jc.invokeApi(scene), 0, "sold_number");
            //购买固定套餐
            IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(phone)
                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(phone))
                    .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
                    .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear(10)
                    .extendedInsuranceCopies(10).type(1).build();
            jc.invokeApi(purchaseFixedPackageScene);
            //确认支付
            util.makeSureBuyPackage("凯迪拉克无限套餐");
            //购买后售出套数
            long newSoldNumber = CommonUtil.getIntField(jc.invokeApi(scene), 0, "sold_number");
            CommonUtil.valueView(soldNumber, newSoldNumber);
            Preconditions.checkArgument(newSoldNumber == soldNumber + 1,
                    "凯迪拉克无限套餐" + "售出：" + CommonUtil.errMessage(soldNumber + 1, newSoldNumber));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--购买一个固定套餐，该套餐售出+1");
        }
    }

    @Test(description = "套餐管理--赠送一个固定套餐，该套餐赠送+1")
    public void packageManager_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String phone = marketing.getPhone();
            String subjectType = util.getSubjectType();
            //获取售出套数
            long packageId = util.getPackageId("凯迪拉克无限套餐");
            IScene scene = PackageFormPage.builder().packageName("凯迪拉克无限套餐").build();
            int giveNumber = CommonUtil.getIntField(jc.invokeApi(scene), 0, "give_number");
            //购买固定套餐
            IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(phone)
                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(phone))
                    .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
                    .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear(10)
                    .extendedInsuranceCopies(10).type(0).build();
            jc.invokeApi(purchaseFixedPackageScene);
            //确认支付
            util.makeSureBuyPackage("凯迪拉克无限套餐");
            //购买后售出套数
            long newGiveNumber = CommonUtil.getIntField(jc.invokeApi(scene), 0, "give_number");
            CommonUtil.valueView(giveNumber, newGiveNumber);
            Preconditions.checkArgument(newGiveNumber == giveNumber + 1,
                    "凯迪拉克无限套餐" + "赠送：" + CommonUtil.errMessage(giveNumber + 1, newGiveNumber));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--赠送一个固定套餐，该套餐赠送+1");
        }
    }


    @Test(description = "消息管理--推送消息含有一张卡券，推送成功后，【卡券表单页】该卡券累计发出+1")
    public void messageManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> phoneList = new ArrayList<>();
            phoneList.add(marketing.getPhone());
            List<Long> voucherList = new ArrayList<>();
            long voucherId = util.getVoucherList().getJSONObject(0).getLong("voucher_id");
            voucherList.add(voucherId);
            String name = util.createVoucherName(voucherId);
            if (!name.equals(EnumStatus.FALSE.getStatus())) {
                //获取卡券累计发出
                IScene scene = VoucherFormPage.builder().voucherName(name).build();
                long cumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
                //消息发送一张卡券z
                IScene sendMesScene = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
                        .telList(phoneList).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
                        .type(0).voucherOrPackageList(voucherList).useDays(10).ifSendImmediately(true).build();
                jc.invokeApi(sendMesScene);
                long newCumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
                CommonUtil.valueView(cumulativeDelivery, newCumulativeDelivery);
                Preconditions.checkArgument(newCumulativeDelivery == cumulativeDelivery + 1,
                        "消息发出一张卡券后，" + name + "累计发出数量：" + CommonUtil.errMessage(cumulativeDelivery + 1, newCumulativeDelivery));
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--推送消息含有一张卡券，推送成功后，【卡券表单页】该卡券累计发出+1");
        }
    }
}
