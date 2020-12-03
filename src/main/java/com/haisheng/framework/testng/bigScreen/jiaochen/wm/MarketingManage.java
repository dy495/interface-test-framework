package com.haisheng.framework.testng.bigScreen.jiaochen.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumContent;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumVoucherStatus;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PurchaseFixedPackage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PurchaseTemporaryPackage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.BusinessUtil;
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
import java.util.Date;

public class MarketingManage extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = ScenarioUtil.getInstance();
    BusinessUtil util = new BusinessUtil();
    private static final Integer size = 100;
    private static final EnumAccount marketing = EnumAccount.MARKETING;

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
        String[] strings = {EnumContent.B.getContent(), "1", null, ""};
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (String name : strings) {
                Long stock = 1000L;
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(name).subjectType(util.getSubjectType())
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
                        .shopType(0).shopIds(util.getShopIds()).selfVerification(true).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                if (StringUtils.isEmpty(name)) {
                    Preconditions.checkArgument(message.equals("卡券名称不能为空"), "卡券名称为：" + name + "创建成功");
                } else {
                    Preconditions.checkArgument(message.equals("卡券名称长度应为2～20个字"), "卡券名称为：" + name + "创建成功");
                }
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
        String[] strings = {null, "", EnumContent.C.getContent()};
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (String desc : strings) {
                Long stock = 1000L;
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.getVoucherName()).subjectType(util.getSubjectType())
                        .voucherDescription(desc).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
                        .shopType(0).shopIds(util.getShopIds()).selfVerification(true).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                if (StringUtils.isEmpty(desc)) {
                    Preconditions.checkArgument(message.equals("卡券说明不能为空"), "卡券说明为：" + desc + "创建成功");
                } else {
                    Preconditions.checkArgument(message.equals("卡券描述不能超过200个字"), "卡券说明为：" + desc + "创建成功");
                }
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
        String[] strings = {"全部权限", null, ""};
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (String subjectType : strings) {
                Long stock = 1000L;
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.getVoucherName()).subjectType(subjectType)
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

    @Test(description = "卡券表单--新建卡券--主体类型选择门店，店面id异常")
    public void voucherManage_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long stock = 1000L;
            IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.getVoucherName()).subjectType(util.getSubjectType())
                    .voucherDescription(util.getDesc()).stock(stock).cost(util.getCost(stock))
                    .shopType(0).shopIds(util.getShopIds()).selfVerification(true).build();
            String message = jc.invokeApi(scene, false).getString("message");
            CommonUtil.valueView(message);
            Preconditions.checkArgument(message.equals("主体详情不能为空"), "主体详情为：" + null + "创建成功");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--新建卡券--主体类型下店面id为空");
        }
    }

    @Test(description = "卡券表单--新建卡券--库存数量异常情况")
    public void voucherManage_system_5() {
        Long[] strings = {1000000000L, null, -100L, 9999999999L};
        logger.logCaseStart(caseResult.getCaseName());
        try {
            for (Long stock : strings) {
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.getVoucherName()).subjectType(util.getSubjectType())
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
                        .shopType(0).shopIds(util.getShopIds()).selfVerification(true).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                if (StringUtils.isEmpty(stock)) {
                    Preconditions.checkArgument(message.equals("库存不能为空"), "卡券库存为：" + stock + "创建成功");
                } else if (stock > 1000000000L) {
                    Preconditions.checkArgument(message.equals("请求入参类型不正确"), "卡券库存为：" + stock + "创建成功");
                } else {
                    Preconditions.checkArgument(message.equals("卡券库存范围应在0 ～ 100000000张"), "卡券库存为：" + stock + "创建成功");
                }
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
        Integer[] strings = {null, -1, 100};
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long stock = 1000L;
            for (Integer shopType : strings) {
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.getVoucherName()).subjectType(util.getSubjectType())
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
                        .shopType(shopType).shopIds(util.getShopIds()).selfVerification(true).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                if (StringUtils.isEmpty(shopType)) {
                    Preconditions.checkArgument(message.equals("业务类型不能为空"), "业务类型为：" + shopType + "创建成功");
                } else {
                    Preconditions.checkArgument(message.equals("业务类型不存在"), "业务类型为：" + shopType + "创建成功");
                }
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
        Double[] strings = {null, (double) -1, (double) 1000000000, 100000000.11};
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long stock = 1000L;
            for (Double cost : strings) {
                IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.getVoucherName()).subjectType(util.getSubjectType())
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(cost)
                        .shopType(0).shopIds(util.getShopIds()).selfVerification(true).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                if (StringUtils.isEmpty(cost)) {
                    Preconditions.checkArgument(message.equals("成本不能为空"), "成本为：" + cost + "创建成功");
                } else {
                    Preconditions.checkArgument(message.equals("卡券成本金额范围应在0 ～ 100000000元"), "成本为：" + cost + "创建成功");
                }
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
            IScene scene = Create.builder().voucherPic(util.getPicPath()).voucherName(util.getVoucherName())
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
            int cost = CommonUtil.getIntField(data, 0, "cost");
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
            //作废卡券
            String voucherName = util.invalidVoucher();
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

    @Test(description = "卡券表单--增发卡券--审批通过前，增发库存=增发库存")
    public void voucherManage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int additionalInventory = 0;
            String voucherName = null;
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            IScene applyScene = ApplyPage.builder().build();
            int applyTotal = jc.invokeApi(applyScene).getInteger("total");
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int id = 0;
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getBoolean("if_can_invalid")) {
                        id = array.getJSONObject(j).getInteger("id");
                        voucherName = array.getJSONObject(j).getString("voucher_name");
                        additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
                        break;
                    }
                }
            }
            //增发
            jc.pcAddVoucher((long) id, 20);
            //增发后校验
            int newApplyTotal = jc.invokeApi(applyScene).getInteger("total");
            Preconditions.checkArgument(newApplyTotal == applyTotal + 1, "");
            IScene voucherScene = builder.voucherName(voucherName).build();
            int newAdditionalInventory = CommonUtil.getIntField(jc.invokeApi(voucherScene), 0, "additional_inventory");
            CommonUtil.valueView(additionalInventory, newAdditionalInventory);
            Preconditions.checkArgument(additionalInventory == newAdditionalInventory, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--增发卡券--审批通过前，增发库存=增发库存");
        }
    }

    @Test(description = "卡券表单--增发卡券--审批通过后，增发库存=增发库存+增发数量")
    public void voucherManage_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int id = 0;
            //增发库存
            int additionalInventory = 0;
            //剩余库存
            int surplusInventory = 0;
            String voucherName = null;
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getBoolean("if_can_invalid")) {
                        id = array.getJSONObject(j).getInteger("id");
                        voucherName = array.getJSONObject(j).getString("voucher_name");
                        additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
                        surplusInventory = array.getJSONObject(j).getInteger("surplus_inventory");
                        break;
                    }
                }
            }
            //增发
            jc.pcAddVoucher((long) id, 20);
            //审批通过
            JSONObject applyResponse = jc.invokeApi(ApplyPage.builder().name(voucherName).build());
            int applyId = CommonUtil.getIntField(applyResponse, 0, "id");
            jc.pcApplyApproval((long) applyId, "1");
            JSONObject response = jc.invokeApi(builder.voucherName(voucherName).build());
            int newAdditionalInventory = CommonUtil.getIntField(response, 0, "additional_inventory");
            int newSurplusInventory = CommonUtil.getIntField(response, 0, "surplus_inventory");
            Preconditions.checkArgument(newAdditionalInventory == additionalInventory + 20, "");
            Preconditions.checkArgument(newSurplusInventory == surplusInventory + 20, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--增发卡券--审批通过前，增发库存=增发库存");
        }
    }

    @Test(description = "卡券表单--增发卡券--审批不通过，增发库存=增发库存")
    public void voucherManage_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int id = 0;
            //增发库存
            int additionalInventory = 0;
            //剩余库存
            String voucherName = null;
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getBoolean("if_can_invalid")) {
                        id = array.getJSONObject(j).getInteger("id");
                        voucherName = array.getJSONObject(j).getString("voucher_name");
                        additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
                        break;
                    }
                }
            }
            //增发
            jc.pcAddVoucher((long) id, 20);
            //审批不通过
            JSONObject applyResponse = jc.invokeApi(ApplyPage.builder().name(voucherName).build());
            int applyId = CommonUtil.getIntField(applyResponse, 0, "id");
            jc.pcApplyApproval((long) applyId, "0");
            JSONObject response = jc.invokeApi(builder.voucherName(voucherName).build());
            int newAdditionalInventory = CommonUtil.getIntField(response, 0, "additional_inventory");
            Preconditions.checkArgument(newAdditionalInventory == additionalInventory, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--增发卡券--审批不通过，增发库存=增发库存");
        }
    }

    @Test(description = "卡券表单--剩余库存=发行库存+增发库存-累计过期-累计使用")
    public void voucherManage_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    int issueInventory = array.getJSONObject(j).getInteger("issue_inventory");
                    int additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
                    int surplusInventory = array.getJSONObject(j).getInteger("surplus_inventory");
                    int cumulativeDelivery = array.getJSONObject(j).getInteger("cumulative_delivery");
                    int cumulativeOverdue = array.getJSONObject(j).getInteger("cumulative_overdue");
                    Preconditions.checkArgument(surplusInventory == additionalInventory + issueInventory - cumulativeDelivery - cumulativeOverdue, "");
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--剩余库存=发行库存+增发库存-累计过期-累计使用");
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
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getString("invalid_status_name").equals(EnumVoucherStatus.UNSENT.getName())) {
                        int cumulativeDelivery = array.getJSONObject(j).getInteger("cumulative_delivery");
                        Preconditions.checkArgument(cumulativeDelivery == 0, "");
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
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getString("invalid_status_name").equals(EnumVoucherStatus.SENT.getName())) {
                        int cumulativeUse = array.getJSONObject(j).getInteger("cumulative_use");
                        Preconditions.checkArgument(cumulativeUse > 0, "");
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
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    int cumulativeUse = array.getJSONObject(j).getInteger("cumulative_use");
                    int cumulativeOverdue = array.getJSONObject(j).getInteger("cumulative_overdue");
                    int cumulativeDelivery = array.getJSONObject(j).getInteger("cumulative_delivery");
                    Preconditions.checkArgument(cumulativeDelivery >= cumulativeOverdue, "");
                    Preconditions.checkArgument(cumulativeDelivery >= cumulativeUse, "");
                    Preconditions.checkArgument(cumulativeDelivery >= cumulativeOverdue + cumulativeUse);
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--累计发出>=累计过期&&累计发出>=累计使用&&累计发出>=累计使用+累计过期");
        }
    }

    @Test(description = "卡券表单--临时套餐购买/赠送一张此卡券，累计发出+1")
    public void voucherManage_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //套餐发出数量
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder().voucherName("");
            long cumulativeDelivery = jc.invokeApi(builder.build()).getLong("cumulative_delivery");
            //购买临时套餐
            IScene scene = PurchaseTemporaryPackage.builder().customerId(12L).carType("ALL_CAR").selectNumber(1).price(100)
                    .expiryDate(30).remark("xxxxxxx").type(1).subjectType("").subjectId(1L).build();
            jc.invokeApi(scene);
            //购买后数量
            long newCumulativeDelivery = jc.invokeApi(builder.build()).getLong("cumulative_delivery");
            Preconditions.checkArgument(newCumulativeDelivery == cumulativeDelivery + 1, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--临时套餐购买/赠送一张此卡券，累计发出+1");
        }
    }

    @Test(description = "卡券表单--固定套餐购买/赠送一个套餐（此套餐内包含此卡券一张）累计发出+1")
    public void voucherManage_data_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //套餐发出数量
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder().voucherName("");
            long cumulativeDelivery = jc.invokeApi(builder.build()).getLong("cumulative_delivery");
            //购买固定套餐
            IScene scene = PurchaseFixedPackage.builder().customerId(12L).carType("ALL_CAR").selectNumber(1).price(100)
                    .expiryDate(30).remark("xxxxxxx").type(1).subjectType("").subjectId(1L).build();
            jc.invokeApi(scene);
            //购买后数量
            long newCumulativeDelivery = jc.invokeApi(builder.build()).getLong("cumulative_delivery");
            Preconditions.checkArgument(newCumulativeDelivery == cumulativeDelivery + 1, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--固定套餐购买/赠送一个套餐（此套餐内包含此卡券一张）累计发出+1");
        }
    }

    @Test(description = "卡券表单--累计发出=【发卡记录】中按该卡券名称搜索结果的列表数")
    public void voucherManage_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (!array.getJSONObject(j).getString("invalid_status_name").equals("已作废")) {
                        String voucherName = array.getJSONObject(j).getString("voucher_name");
                        int cumulativeDelivery = array.getJSONObject(j).getInteger("cumulative_delivery");
                        //发卡记录页查询
                        IScene scene1 = SendRecord.builder().voucherName(voucherName).build();
                        int deliveryTotal = jc.invokeApi(scene1).getInteger("total");
                        Preconditions.checkArgument(deliveryTotal == cumulativeDelivery, "");
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
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (!array.getJSONObject(j).getString("invalid_status_name").equals("已作废")) {
                        String voucherName = array.getJSONObject(j).getString("voucher_name");
                        int cumulativeUse = array.getJSONObject(j).getInteger("cumulative_use");
                        //核销记录页查询
                        IScene scene1 = VerificationRecord.builder().voucherName(voucherName).build();
                        int deliveryTotal = jc.invokeApi(scene1).getInteger("total");
                        Preconditions.checkArgument(deliveryTotal == cumulativeUse, "");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--累计使用=【核销记录】中按该卡券名称搜索结果的列表数");
        }
    }

    @Test(description = "卡券表单--增发库存=【卡券审核】该卡券所有通过申请记录(标记为增发)发出个数之和")
    public void voucherManage_data_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    String voucherName = array.getJSONObject(j).getString("voucher_name");
                    int additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
                    int numSum = 0;
                    //卡券审核页
                    ApplyPage.ApplyPageBuilder builder1 = ApplyPage.builder().name(voucherName);
                    int total1 = jc.invokeApi(builder1.build()).getInteger("total");
                    int x = CommonUtil.getTurningPage(total1, size);
                    for (int i1 = 1; i1 < x; i1++) {
                        builder1.page(i1).size(size);
                        JSONArray array1 = jc.invokeApi(builder1.build()).getJSONArray("list");
                        for (int j1 = 0; j1 < array1.size(); j1++) {
                            if (array1.getJSONObject(j1).getInteger("status") == 1) {
                                numSum += array1.getJSONObject(j1).getInteger("");
                            }
                        }
                    }
                    Preconditions.checkArgument(additionalInventory == numSum, "");
                }
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--增发库存=【卡券审核】该卡券所有通过申请记录(标记为增发)发出个数之和");
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
            Preconditions.checkArgument(newTotal == total + 1, "");
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
            ApplyPage.ApplyPageBuilder builder = ApplyPage.builder();
            Long total = jc.invokeApi(builder.build()).getLong("total");
            //增发一种卡券
            util.createVoucher(1000L);
            //增发卡券后卡券审核页列表数
            Long newTotal = jc.invokeApi(builder.build()).getLong("total");
            Preconditions.checkArgument(newTotal == total + 1, "");
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
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int listSize = 0;
            for (int i = 1; i < s; i++) {
                builder.page(i).size(size);
                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getLong("surplus_inventory") != 0
                            && !array.getJSONObject(j).getString("invalid_status_name").equals("已作废")) {
                        listSize++;
                    }
                }
            }
            //创建卡券
            util.createVoucher(1000L);
            long listTotal = jc.pcVoucherList().getLong("total");
            Preconditions.checkArgument(listTotal != 0, "");
            Preconditions.checkArgument(listTotal == listSize + 1, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "卡券表单--消息发出此一张卡券，累计发出+1")
    public void voucherManage_data_18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //发消息前累计发出数量
            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder().voucherName("");
            Long cumulativeDelivery = jc.invokeApi(builder.build()).getLong("cumulative_delivery");
            //推送消息
            util.pushMessage();
            //推送消息后累计发出数量
            Long newCumulativeDelivery = jc.invokeApi(builder.build()).getLong("cumulative_delivery");
            Preconditions.checkArgument(newCumulativeDelivery == cumulativeDelivery + 1, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
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
                if (StringUtils.isEmpty(name)) {
                    Preconditions.checkArgument(message.equals("核销人员名字不能为空"), "核销人员名字为：" + name + "创建成功");
                } else {
                    Preconditions.checkArgument(message.equals("核销人员名字必须为1～20个字"), "核销人员名字为：" + name + "创建成功");
                }
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
                if (StringUtils.isEmpty(name)) {
                    Preconditions.checkArgument(message.equals("核销人员名字不能为空"), "核销人员名字为：" + name + "创建成功");
                } else {
                    Preconditions.checkArgument(message.equals("核销人员名字必须为1～20个字"), "核销人员名字为：" + name + "创建成功");
                }
                CommonUtil.logger(name);
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }

    @Test(description = "核销人员--创建财务核销,电话异常")
    public void voucherManage_system_38() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {"11111111111", "1337316680", "133731668062"};
            for (String phone : strings) {
                IScene scene = CreateVerificationPeople.builder().verificationPersonName("郭丽雅")
                        .verificationPersonPhone(phone).status(1).type(0).build();
                String message = jc.invokeApi(scene, false).getString("message");
                CommonUtil.valueView(message);
                if (StringUtils.isEmpty(phone)) {
                    Preconditions.checkArgument(message.equals("核销人员电话不能为空"), "手机号格式为：" + phone + "创建成功");
                } else {
                    Preconditions.checkArgument(message.equals("手机号格式不正确"), "手机号格式为：" + phone + "创建成功");
                }
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
                Preconditions.checkArgument(message.equals("手机号已存在"), "手机号格式为：" + phone + "创建成功");
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,电话存在");
        }
    }


    @Test(description = "核销人员--创建财务核销,列表数+1&创建异页核销,列表数+1")
    public void voucherManage_data_30() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //查询列表数
            int total = jc.invokeApi(VerificationPeople.builder().build()).getInteger("total");
            //创建核销人员
            CreateVerificationPeople.CreateVerificationPeopleBuilder builder = CreateVerificationPeople.builder();
            builder.verificationPersonName("aa").verificationPersonPhone("15321527989").status(1).type(0);
            int total1 = jc.invokeApi(builder.build()).getInteger("total");
            Preconditions.checkArgument(total1 == total + 1, "");
            int total2 = jc.invokeApi(builder.type(1).build()).getInteger("total");
            Preconditions.checkArgument(total2 == total1 + 1, "");
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,列表数+1&创建异页核销,列表数+1");
        }
    }


}
