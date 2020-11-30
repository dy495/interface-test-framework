package com.haisheng.framework.testng.bigScreen.jiaochen.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.sale.EnumVoucherStatus;
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
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

public class MarketingManage extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = ScenarioUtil.getInstance();
    BusinessUtil util = new BusinessUtil();
    private static final Integer size = 100;

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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP.getShopId();
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
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "卡券表单--新建卡券--列表数+1&发行库存=创建时填写数量&成本=创建时填写的成本")
    public void voucherManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取列表数据
            VoucherFormPage.VoucherFormPageBuilder scene = VoucherFormPage.builder();
            int total = jc.invokeApi(scene.build()).getInteger("total");
            IScene applyScene = ApplyPage.builder().build();
            int applyTotal = jc.invokeApi(applyScene).getInteger("total");
            //创建卡券
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/卡券图.jpg";
            String picture = new ImageUtil().getImageBinary(path);
            String voucherDescription = "商家大促销";
            String voucherName = util.getVoucherName();
            IScene createScene = Create.builder().voucherPic(picture).voucherName(voucherName).voucherDescription(voucherDescription)
                    .stock(10000).cost(10000).shopType(1).shopIds(new ArrayList<>()).selfVerification(true).subjectType("").subjectId(1L).build();
            jc.invokeApi(createScene);
            //获取列表数据
            int newTotal = jc.invokeApi(scene.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            int issueInventory = 0;
            int cost = 0;
            for (int i = 1; i < s; i++) {
                scene.page(i).size(size);
                JSONArray array = jc.invokeApi(scene.build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getString("voucher_name").equals(voucherName)) {
                        issueInventory = array.getJSONObject(j).getInteger("issue_inventory");
                        cost = array.getJSONObject(j).getInteger("cost");
                    }
                }
            }
            int newApplyTotal = jc.invokeApi(applyScene).getInteger("total");
            Preconditions.checkArgument(newApplyTotal == applyTotal + 1, "");
            Preconditions.checkArgument(newTotal == total + 1, "");
            Preconditions.checkArgument(issueInventory == 10000, "");
            Preconditions.checkArgument(cost == 100000, "");
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--新建卡券--列表数+1&发行库存=创建时填写数量&成本=创建时填写的成本");
        }
    }

    @Test(description = "卡券表单--作废卡券--作废时间=当前时间&&作废账号=当前操作账号&&发放状态=已作废")
    public void voucherManage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:ss");
            VoucherFormPage.VoucherFormPageBuilder scene = VoucherFormPage.builder();
            int total = jc.invokeApi(scene.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            String invalidTime = null;
            String invalidAccount = null;
            String invalidStatusName = null;
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(scene.page(i).size(size).build()).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    if (array.getJSONObject(j).getBoolean("if_can_invalid")) {
                        int id = array.getJSONObject(j).getInteger("id");
                        //作废
                        jc.pcInvalidVoucher((long) id);
                        invalidTime = array.getJSONObject(j).getString("invalid_time");
                        invalidAccount = array.getJSONObject(j).getString("invalid_account");
                        invalidStatusName = array.getJSONObject(j).getString("invalid_status_name");
                        break;
                    }
                }
            }
            Preconditions.checkArgument(invalidTime != null && invalidTime.equals(date), "");
            Preconditions.checkArgument(invalidAccount != null && invalidAccount.equals(""), "");
            Preconditions.checkArgument(invalidStatusName.equals(EnumVoucherStatus.INVALID.getName()), "");
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
            util.createVoucher();
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
            util.createVoucher();
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
            util.createVoucher();
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
