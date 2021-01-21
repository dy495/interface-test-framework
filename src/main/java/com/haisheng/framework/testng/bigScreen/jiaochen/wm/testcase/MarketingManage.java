package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.VoucherApply;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.VoucherDetail;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumContent;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.ApplyStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.ShopTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.EditVoucher;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.RecallVoucherScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherPageScene;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 营销管理模块测试用例
 */
public class MarketingManage extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_DAILY;
    private static final EnumAccount ADMINISTRATOR = EnumAccount.MARKETING;
    private static final Integer SIZE = 100;
    public Visitor visitor = new Visitor(product);
    public UserUtil user = new UserUtil(visitor);
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
        commonConfig.shopId = "48065";
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

    @Test(description = "优惠券管理--创建自定义优惠券--列表数+1&优惠券状态=待审核；【优惠券审批】列表数+1&审核状态=审核中&申请类型=首发")
    public void voucherManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int voucherTotal = visitor.invokeApi(VoucherPageScene.builder().build()).getInteger("total");
            int applyTotal = visitor.invokeApi(ApplyPageScene.builder().build()).getInteger("total");
            //创建优惠券
            Arrays.stream(VoucherTypeEnum.values()).forEach(anEnum -> {
                String voucherName = util.createVoucher(1, anEnum);
                IScene scene = VoucherPageScene.builder().voucherName(voucherName).build();
                visitor.invokeApi(scene);
                //优惠券列表+1
                int newVoucherTotal = visitor.invokeApi(VoucherPageScene.builder().build()).getInteger("total");
                CommonUtil.checkResult("优惠券列表数", voucherTotal + 1, newVoucherTotal);
                //优惠券状态=待审核
                VoucherPage voucher = util.getVoucherInfo(voucherName);
                Integer auditStatusName = voucher.getVoucherStatus();
                CommonUtil.checkResult("优惠券状态", VoucherStatusEnum.getNameById(auditStatusName), VoucherStatusEnum.WAITING.getName());
                Preconditions.checkArgument(VoucherStatusEnum.getNameById(auditStatusName).equals(VoucherStatusEnum.WAITING.getName()), "新增优惠券状态为审核中,实际状态");
                //审核列表+1
                int newApplyTotal = visitor.invokeApi(ApplyPageScene.builder().build()).getInteger("total");
                CommonUtil.checkResult("优惠券审批列表数", applyTotal + 1, newApplyTotal);
                //审核状态=审核中
                VoucherApply voucherApply = util.getVoucherApplyInfo(voucherName);
                CommonUtil.checkResult("优惠券审核状态", ApplyStatusEnum.AUDITING.getName(), voucherApply.getStatusName());
                //申请类型=首发
                CommonUtil.checkResult("优惠券申请类型", "首发", voucherApply.getApplyTypeName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--创建自定义优惠券--列表数+1&优惠券状态=待审核；【优惠券审批】列表数+1&审核状态=审核中&申请类型=首发");
        }
    }

    @Test(description = "优惠券管理--优惠券详情--优惠券详情与创建时相同")
    public void voucherManage_data_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(VoucherTypeEnum.values()).forEach(anEnum -> {
                IScene scene = VoucherPageScene.builder().voucherName(anEnum.getDesc()).build();
                List<Long> voucherIds = util.collectBean(scene, VoucherPage.class).stream().map(VoucherPage::getVoucherId).collect(Collectors.toList());
                List<VoucherDetail> voucherDetails = voucherIds.stream().map(id -> visitor.invokeApi(VoucherDetailScene.builder().id(id).build())).map(jsonObject -> JSONObject.toJavaObject(jsonObject, VoucherDetail.class)).collect(Collectors.toList());
                voucherDetails.forEach(e -> {
                    CommonUtil.checkResult("卡券描述", EnumContent.B.getContent(), e.getVoucherDescription());
                    CommonUtil.checkResult("卡券主体类型", util.getSubjectType(), e.getSubjectType());
                    CommonUtil.checkResult("卡券主体类型id", util.getSubjectDesc(util.getSubjectType()), e.getSubjectId());
                    CommonUtil.checkResult("卡券面值", 49.99, e.getParValue());
                    CommonUtil.checkResult("卡券类型", anEnum.name(), e.getCardType());
                    CommonUtil.checkResult("卡券类型名称", anEnum.getDesc(), e.getCardTypeName());
                    if (anEnum.getDesc().equals(VoucherTypeEnum.COMMODITY_EXCHANGE.getDesc())) {
                        CommonUtil.checkResult("满足金额", 999.99, e.getThresholdPrice());
                        CommonUtil.checkResult("兑换商品", "兑换布加迪威龙一辆", e.getExchangeCommodityName());
                    }
                    if (anEnum.getDesc().equals(VoucherTypeEnum.COUPON.getDesc())) {
                        CommonUtil.checkResult("满足金额", 999.99, e.getThresholdPrice());
                        CommonUtil.checkResult("折扣", 2.5, e.getDiscount());
                        CommonUtil.checkResult("最对减", 100.00, e.getMostDiscount());
                    }
                    if (anEnum.getDesc().equals(VoucherTypeEnum.FULL_DISCOUNT.getDesc())) {
                        CommonUtil.checkResult("满足金额", 999.99, e.getThresholdPrice());
                    }
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理-优惠券详情--优惠券详情与创建时相同");
        }
    }

    @Test(description = "优惠券管理--编辑待审核优惠券--卡券信息更新")
    public void voucherManage_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审核的优惠券id
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildGenerator().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            String newVoucherName = voucherName + "改";
            List<Long> shopIds = util.getShopIdList(2);
            IScene scene = EditVoucher.builder().id(voucherId).voucherName(newVoucherName).voucherDescription(EnumContent.D.getContent()).shopIds(shopIds).shopType(1).selfVerification(false).build();
            visitor.invokeApi(scene);
            IScene voucherDetailScene = VoucherDetailScene.builder().id(voucherId).build();
            VoucherDetail voucherDetail = JSONObject.toJavaObject(visitor.invokeApi(voucherDetailScene), VoucherDetail.class);
            //卡券列表
            CommonUtil.checkResult("优惠券名称", newVoucherName, voucherDetail.getVoucherName());
            CommonUtil.checkResult("卡券详情", EnumContent.D.getContent(), voucherDetail.getVoucherDescription());
            CommonUtil.checkResult("适用门店", shopIds, voucherDetail.getShopIds());
            CommonUtil.checkResult("业务类型", ShopTypeEnum.DIFF.getId(), voucherDetail.getShopType());
            CommonUtil.checkResult("是否自助核销", false, voucherDetail.getSelfVerification());
            //审核列表
            VoucherApply voucherApply = util.getVoucherApplyInfo(newVoucherName);
            CommonUtil.checkResult("此卡券审核列表名称", newVoucherName, voucherApply.getName());
            CommonUtil.checkResult("此卡券审核列表状态", ApplyStatusEnum.AUDITING.getName(), voucherApply.getStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--编辑待审核优惠券--卡券信息更新");
        }
    }

    @Test(description = "优惠券管理--撤回优惠券--优惠券状态=已撤回&此优惠券在审核列表状态=已取消")
    public void voucherManage_data_14() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildGenerator().getVoucherId();
            //撤回
            IScene recallVoucherScene = RecallVoucherScene.builder().id(voucherId).build();
            visitor.invokeApi(recallVoucherScene);
            //校验
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherInfo(voucherName);
            CommonUtil.checkResult("优惠券状态", VoucherStatusEnum.RECALL.getName(), voucherPage.getVoucherStatus());
            //审核列表
            VoucherApply voucherApply = util.getVoucherApplyInfo(voucherName);
            CommonUtil.checkResult("此优惠券在审核列表状态", ApplyStatusEnum.CANCEL.getName(), voucherApply.getStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--撤回优惠券--优惠券状态=已撤回&此优惠券在审核列表状态=已取消");
        }
    }

    @Test(description = "优惠券管理--删除优惠券--此券记录消失")
    public void voucherManage_data_15() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.RECALL).buildGenerator().getVoucherId();
            //撤回
            IScene recallVoucherScene = RecallVoucherScene.builder().id(voucherId).build();
            visitor.invokeApi(recallVoucherScene);
            //校验
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherInfo(voucherName);
            CommonUtil.checkResult("优惠券状态", VoucherStatusEnum.RECALL.getName(), voucherPage.getVoucherStatus());
            //审核列表
            VoucherApply voucherApply = util.getVoucherApplyInfo(voucherName);
            CommonUtil.checkResult("此优惠券在审核列表状态", ApplyStatusEnum.CANCEL.getName(), voucherApply.getStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理-撤回优惠券--优惠券状态=已撤回&此优惠券在审核列表状态=已取消");
        }
    }


//    private <T> List<T> getList(T str) {
//        List<T> list = new ArrayList<>();
//        list.add(str);
//        return list;
//    }
//
//    @Test(description = "卡券表单--新建卡券--列表数+1&发行库存=创建时填写数量&成本=创建时填写的成本")
//    public void voucherManage_data_1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long stock = 1000L;
//            //获取列表数据
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            //创建卡券
//            String voucherName = util.createVoucher(stock);
//            //创建后此卡券
//            int newTotal = jc.invokeApi(builder.build()).getInteger("total");
//            Preconditions.checkArgument(newTotal == total + 1,
//                    "创建卡券前卡券列表数量为：" + total + " 创建卡券后数量为：" + newTotal);
//            builder.voucherName(voucherName);
//            JSONObject data = jc.invokeApi(builder.build());
//            int issueInventory = CommonUtil.getIntField(data, 0, "issue_inventory");
//            Preconditions.checkArgument(issueInventory == stock,
//                    "创建卡券时数量为1000，创建完成后列表展示发行库存为：" + issueInventory);
//            Double cost = data.getJSONArray("list").getJSONObject(0).getDouble("cost");
//            Preconditions.checkArgument(cost == 50.05,
//                    "创建卡券时成本为:" + 50.05 + "，创建完成后列表展示成本为：" + cost);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--新建卡券--列表数+1&发行库存=创建时填写数量&成本=创建时填写的成本");
//        }
//    }
//
//    @Test(description = "卡券表单--作废卡券--作废时间=当前时间&&作废账号=当前操作账号&&发放状态=已作废")
//    public void voucherManage_data_2() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
//            //创建卡券
//            String voucherName = util.createVoucher(1000L);
//            //作废此卡券
//            util.invalidVoucher(voucherName);
//            IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
//            JSONObject response = jc.invokeApi(scene);
//            String invalidTime = CommonUtil.getStrField(response, 0, "create_time");
//            String invalidAccount = CommonUtil.getStrField(response, 0, "invalid_account");
//            String invalidStatusName = CommonUtil.getStrField(response, 0, "invalid_status_name");
//            Preconditions.checkArgument(invalidTime.equals(date), "作废时间：" + invalidTime + " 当前时间：" + date);
//            Preconditions.checkArgument(invalidAccount.equals(administrator.getPhone()),
//                    "作废账号：" + invalidAccount + " 当前操作账号" + administrator.getPhone());
//            Preconditions.checkArgument(invalidStatusName.equals(EnumVoucherStatus.INVALID.getName()),
//                    "发放状态：" + invalidStatusName);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--作废卡券--作废时间=当前时间&&作废账号=当前操作账号&&发放状态=已作废");
//        }
//    }
//
//    @Test(description = "卡券表单--增发卡券--审批通过前，增发库存不变，剩余库存存不变")
//    public void voucherManage_data_3() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //增发前剩余库存和增发库存
//            int surplusInventory = 0;
//            int additionalInventory = 0;
//            String voucherName = null;
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    if (array.getJSONObject(j).getString("audit_status_name").equals("已通过")
//                            && !array.getJSONObject(j).getString("invalid_status_name").equals("已作废")) {
//                        surplusInventory = array.getJSONObject(j).getInteger("surplus_inventory") == null ?
//                                0 : array.getJSONObject(j).getInteger("surplus_inventory");
//                        additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
//                        voucherName = array.getJSONObject(j).getString("voucher_name");
//                        break;
//                    }
//                }
//            }
//            //增发
//            util.addVoucher(voucherName, 10);
//            //增发后数据
//            VoucherInfo voucherInfo = util.getVoucherInfo(voucherName);
//            long newSurplusInventory = voucherInfo.getSurplusInventory() == null ? 0 : voucherInfo.getSurplusInventory();
//            Long newAdditionalInventory = voucherInfo.getAdditionalInventory();
//            CommonUtil.valueView(surplusInventory, newSurplusInventory, additionalInventory, newAdditionalInventory);
//            Preconditions.checkArgument(newSurplusInventory == surplusInventory,
//                    "增发前剩余库存：" + surplusInventory + "增发后剩余库存：" + newSurplusInventory);
//            Preconditions.checkArgument(newAdditionalInventory == additionalInventory,
//                    "增发前增发库存：" + additionalInventory + "增发后增发库存：" + newAdditionalInventory);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--增发卡券--审批通过前，增发库存不变，剩余库存存不变");
//        }
//    }
//
//    @Test(description = "卡券表单--增发卡券--审批通过后，新增发库存=原增发库存+增发数量，新剩余库存=原剩余库存+增发数量")
//    public void voucherManage_data_4() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //增发前剩余库存和增发库存
//            int surplusInventory = 0;
//            int additionalInventory = 0;
//            String voucherName = null;
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    if (array.getJSONObject(j).getString("audit_status_name").equals("已通过")
//                            && !array.getJSONObject(j).getString("invalid_status_name").equals("已作废")) {
//                        surplusInventory = array.getJSONObject(j).getInteger("surplus_inventory") == null
//                                ? 0 : array.getJSONObject(j).getInteger("surplus_inventory");
//                        additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
//                        voucherName = array.getJSONObject(j).getString("voucher_name");
//                        break;
//                    }
//                }
//            }
//            //增发
//            util.addVoucher(voucherName, 10);
//            //审批通过
//            util.applyVoucher(voucherName, "1");
//            //审批后数据
//            VoucherInfo voucherInfo = util.getVoucherInfo(voucherName);
//            long newSurplusInventory = voucherInfo.getSurplusInventory() == null ? 0 : voucherInfo.getSurplusInventory();
//            Long newAdditionalInventory = voucherInfo.getAdditionalInventory();
//            CommonUtil.valueView(surplusInventory, newSurplusInventory, additionalInventory, newAdditionalInventory);
//            if (surplusInventory != 0) {
//                Preconditions.checkArgument(newSurplusInventory == surplusInventory + 10,
//                        "增发前剩余库存：" + surplusInventory + "增发后剩余库存：" + newSurplusInventory);
//            }
//            Preconditions.checkArgument(newAdditionalInventory == additionalInventory + 10,
//                    "增发前增发库存：" + additionalInventory + "增发后增发库存：" + newAdditionalInventory);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--增发卡券--审批通过后，新增发库存=原增发库存+增发数量，新剩余库存=原剩余库存+增发数量");
//        }
//    }
//
//    @Test(description = "卡券表单--增发卡券--审批不通过，新增发库存=原增发库存，新剩余库存=原剩余库存")
//    public void voucherManage_data_5() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //增发前剩余库存和增发库存
//            int surplusInventory = 0;
//            int additionalInventory = 0;
//            String voucherName = null;
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    if (array.getJSONObject(j).getString("audit_status_name").equals("已通过")
//                            && !array.getJSONObject(j).getString("invalid_status_name").equals("已作废")) {
//                        surplusInventory = array.getJSONObject(j).getInteger("surplus_inventory") == null
//                                ? 0 : array.getJSONObject(j).getInteger("surplus_inventory");
//                        additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
//                        voucherName = array.getJSONObject(j).getString("voucher_name");
//                        break;
//                    }
//                }
//            }
//            //增发
//            util.addVoucher(voucherName, 10);
//            //审批不通过
//            util.applyVoucher(voucherName, "2");
//            //审批后数据
//            VoucherInfo voucherInfo = util.getVoucherInfo(voucherName);
//            Long newSurplusInventory = voucherInfo.getSurplusInventory();
//            Long newAdditionalInventory = voucherInfo.getAdditionalInventory();
//            CommonUtil.valueView(surplusInventory, newSurplusInventory, additionalInventory, newAdditionalInventory);
//            if (surplusInventory != 0) {
//                Preconditions.checkArgument(newSurplusInventory == surplusInventory,
//                        voucherName + "增发前剩余库存：" + surplusInventory + "增发后剩余库存：" + newSurplusInventory);
//            }
//            Preconditions.checkArgument(newAdditionalInventory == additionalInventory,
//                    voucherName + "增发前增发库存：" + additionalInventory + "增发后增发库存：" + newAdditionalInventory);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--增发卡券--审批不通过，新增发库存=原增发库存，新剩余库存=原剩余库存");
//        }
//    }
//
//    @Test(description = "卡券表单--剩余库存=发行库存+增发库存-累计发出")
//    public void voucherManage_data_6() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                builder.page(i).size(size);
//                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    String voucherName = array.getJSONObject(j).getString("voucher_name");
//                    Integer issueInventory = array.getJSONObject(j).getInteger("issue_inventory");
//                    Integer additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
//                    Integer surplusInventory = array.getJSONObject(j).getInteger("surplus_inventory");
//                    Integer cumulativeDelivery = array.getJSONObject(j).getInteger("cumulative_delivery");
//                    if (issueInventory != null && surplusInventory != null) {
//                        CommonUtil.valueView(voucherName);
//                        int num = additionalInventory + issueInventory - cumulativeDelivery;
//                        CommonUtil.valueView(surplusInventory, num);
//                        Preconditions.checkArgument(surplusInventory == num,
//                                voucherName + " 剩余库存：" + surplusInventory + "发行库存+增发库存-累计过期-累计使用：" + num);
//                        CommonUtil.logger(voucherName);
//                    }
//                }
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--剩余库存=发行库存+增发库存-累计发出");
//        }
//    }
//
//    @Test(description = "卡券表单--发放状态为未发出时，累计发出=0")
//    public void voucherManage_data_7() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    if (array.getJSONObject(j).getString("invalid_status_name").equals(EnumVoucherStatus.UNSENT.getName())) {
//                        int cumulativeDelivery = array.getJSONObject(j).getInteger("cumulative_delivery");
//                        String voucherName = array.getJSONObject(j).getString("voucher_name");
//                        Preconditions.checkArgument(cumulativeDelivery == 0,
//                                voucherName + "累计发出数量：" + CommonUtil.checkResult(0, cumulativeDelivery));
//                    }
//                }
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--发放状态为未发出时，累计发出=0");
//        }
//    }
//
//    @Test(description = "卡券表单--发放状态为已发出时，累计发出>0")
//    public void voucherManage_data_8() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                array.forEach(e -> {
//                    JSONObject jsonObject = (JSONObject) e;
//                    if (jsonObject.getString("invalid_status_name").equals(EnumVoucherStatus.SENT.getName())) {
//                        int cumulativeDelivery = jsonObject.getInteger("cumulative_delivery");
//                        String voucherName = jsonObject.getString("voucher_name");
//                        Preconditions.checkArgument(cumulativeDelivery > 0, voucherName + "累计发出数量：" +
//                                CommonUtil.checkResult(">0", cumulativeDelivery));
//                        CommonUtil.logger(voucherName);
//                    }
//                });
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--发放状态为已发出时，累计发出>0");
//        }
//    }
//
//    @Test(description = "卡券表单--累计发出>=累计过期&&累计发出>=累计使用&&累计发出>=累计使用+累计过期")
//    public void voucherManage_data_9() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String date = "2020-12-09";
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    if (array.getJSONObject(j).getString("create_time").compareTo(date) > 0) {
//                        String voucherName = array.getJSONObject(j).getString("voucher_name");
//                        int cumulativeUse = array.getJSONObject(j).getInteger("cumulative_use");
//                        int cumulativeOverdue = array.getJSONObject(j).getInteger("cumulative_overdue");
//                        int cumulativeDelivery = array.getJSONObject(j).getInteger("cumulative_delivery");
//                        Preconditions.checkArgument(cumulativeDelivery >= cumulativeOverdue,
//                                voucherName + "累计发出：" + cumulativeDelivery + "累计过期：" + cumulativeOverdue);
//                        Preconditions.checkArgument(cumulativeDelivery >= cumulativeUse,
//                                voucherName + "累计发出：" + cumulativeDelivery + "累计使用：" + cumulativeUse);
//                        Preconditions.checkArgument(cumulativeDelivery >= cumulativeOverdue + cumulativeUse,
//                                voucherName + "累计发出：" + cumulativeDelivery + "累计使用+累计过期+：" + cumulativeOverdue + cumulativeUse);
//                        CommonUtil.logger(voucherName);
//                    }
//                }
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--累计发出>=累计过期&&累计发出>=累计使用&&累计发出>=累计使用+累计过期");
//        }
//    }
//
//    @Test(description = "卡券表单--此卡券每核销一张，累计使用+1")
//    public void voucherManage_data_10() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //获取核销码
//            String code = util.getVerificationCode("本司员工");
//            //发出一张卡券
//            Long voucherId = util.pushMessage(true);
//            //获取卡券名
//            String voucherName = util.getVoucherName(voucherId);
//            CommonUtil.valueView(voucherName);
//            //累计使用
//            IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
//            int cumulativeUse = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_use");
//            //核销
//            user.loginApplet(appletUser);
//            long id = util.getAppletVoucherId(voucherName);
//            IScene scene1 = VoucherVerification.builder().id(String.valueOf(id)).verificationCode(code).build();
//            jc.invokeApi(scene1);
//            user.login(administrator);
//            //核销后数据
//            int newCumulativeUse = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_use");
//            CommonUtil.valueView(cumulativeUse, newCumulativeUse);
//            Preconditions.checkArgument(newCumulativeUse == cumulativeUse + 1,
//                    voucherName + "被核销后" + CommonUtil.checkResult(cumulativeUse + 1, newCumulativeUse));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--此卡券每核销一张，累计使用+1");
//        }
//    }
//
//    @Test(description = "卡券表单--临时套餐购买一张此卡券，累计发出+1")
//    public void voucherManage_data_11() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray voucherList = util.getVoucherInfo(1);
//            String voucherName = voucherList.getJSONObject(0).getString("voucher_name");
//            IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
//            long cumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
//            //购买临时套餐
//            String platNumber = util.getPlatNumber(marketing.getPhone());
//            IScene temporaryScene = PurchaseTemporaryPackage.builder().customerPhone(marketing.getPhone())
//                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(platNumber).voucherList(voucherList)
//                    .expiryDate("1").remark(EnumContent.B.getContent()).subjectType(util.getSubjectType())
//                    .subjectId(util.getSubjectId(util.getSubjectType())).extendedInsuranceYear("1")
//                    .extendedInsuranceCopies("1").type(1).build();
//            jc.invokeApi(temporaryScene);
//            //确认支付
//            util.makeSureBuyPackage("临时套餐");
//            //购买后累计发出
//            long newCumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
//            CommonUtil.valueView(cumulativeDelivery, newCumulativeDelivery);
//            Preconditions.checkArgument(newCumulativeDelivery == cumulativeDelivery + 1,
//                    voucherName + "累计发出数：" + CommonUtil.checkResult(cumulativeDelivery + 1, newCumulativeDelivery));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--临时套餐购买一张此卡券，累计发出+1");
//        }
//    }
//
//    @Test(description = "卡券表单--固定套餐购买一个套餐（此套餐内包含此卡券一张）累计发出+1，套餐购买数量+1")
//    public void voucherManage_data_12() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String phone = marketing.getPhone();
//            String subjectType = util.getSubjectType();
//            //获取累计发出
//            long packageId = util.getPackageId("凯迪拉克无限套餐");
//            long voucherId = util.getPackageContainVoucher(packageId).get(0);
//            String voucherName = util.getVoucherName(voucherId);
//            IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
//            int cumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
//            //购买固定套餐
//            IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(phone)
//                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(phone))
//                    .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
//                    .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear(10)
//                    .extendedInsuranceCopies(10).type(0).build();
//            jc.invokeApi(purchaseFixedPackageScene);
//            //确认支付
//            util.makeSureBuyPackage("凯迪拉克无限套餐");
//            //购买后累计发出
//            long newCumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
//            CommonUtil.valueView(cumulativeDelivery, newCumulativeDelivery);
//            Preconditions.checkArgument(newCumulativeDelivery == cumulativeDelivery + 1,
//                    voucherName + "累计发出数：" + CommonUtil.checkResult(cumulativeDelivery + 1, newCumulativeDelivery));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--固定套餐购买一个套餐（此套餐内包含此卡券一张）累计发出+1，套餐购买数量+1");
//        }
//    }
//
//    @Test(description = "卡券表单--固定套餐购买一个套餐（此套餐内包含此卡券一张）剩余库存-1")
//    public void voucherManage_data_13() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String phone = marketing.getPhone();
//            String subjectType = util.getSubjectType();
//            //获取累计发出
//            long packageId = util.getPackageId("凯迪拉克无限套餐");
//            long voucherId = util.getPackageContainVoucher(packageId).get(0);
//            String voucherName = util.getVoucherName(voucherId);
//            IScene scene = VoucherFormPage.builder().voucherName(voucherName).build();
//            int cumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
//            //购买固定套餐
//            IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(phone)
//                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(phone))
//                    .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
//                    .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear(10)
//                    .extendedInsuranceCopies(10).type(0).build();
//            jc.invokeApi(purchaseFixedPackageScene);
//            //确认支付
//            util.makeSureBuyPackage("凯迪拉克无限套餐");
//            //购买后累计发出
//            long newCumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
//            CommonUtil.valueView(cumulativeDelivery, newCumulativeDelivery);
//            Preconditions.checkArgument(newCumulativeDelivery == cumulativeDelivery + 1,
//                    voucherName + "累计发出数：" + CommonUtil.checkResult(cumulativeDelivery + 1, newCumulativeDelivery));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--固定套餐购买一个套餐（此套餐内包含此卡券一张）累计发出+1，套餐购买数量+1");
//        }
//    }
//
//    @Test(description = "卡券表单--累计发出=【发卡记录】中按该卡券名称搜索结果的列表数")
//    public void voucherManage_data_14() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String format = "yyyy-MM-dd HH:mm";
//            String date = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -30), format);
//            long time = Long.parseLong(DateTimeUtil.dateToStamp(date, format));
//            user.login(administrator);
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    String createTime = array.getJSONObject(j).getString("create_time");
//                    if (Long.parseLong(DateTimeUtil.dateToStamp(createTime, format)) > time) {
//                        String voucherName = array.getJSONObject(j).getString("voucher_name");
//                        //累计发出数
//                        long cumulativeDelivery = array.getJSONObject(j).getLong("cumulative_delivery");
//                        //发卡记录数
//                        IScene scene = SendRecord.builder().voucherName(voucherName).build();
//                        long sendRecordTotal = jc.invokeApi(scene).getLong("total");
//                        CommonUtil.valueView(cumulativeDelivery, sendRecordTotal);
//                        Preconditions.checkArgument(sendRecordTotal == cumulativeDelivery,
//                                voucherName + "累计发出数：" + cumulativeDelivery + "发卡记录数：" + sendRecordTotal);
//                        CommonUtil.logger(voucherName);
//                    }
//                }
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--累计发出=【发卡记录】中按该卡券名称搜索结果的列表数");
//        }
//    }
//
//    @Test(description = "卡券表单--累计使用=【核销记录】中按该卡券名称搜索结果的列表数")
//    public void voucherManage_data_15() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String format = "yyyy-MM-dd HH:mm";
//            String date = DateTimeUtil.getFormat(DateTimeUtil.addDay(new Date(), -2), format);
//            long time = Long.parseLong(DateTimeUtil.dateToStamp(date, format));
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    String createTime = array.getJSONObject(j).getString("create_time");
//                    if (Long.parseLong(DateTimeUtil.dateToStamp(createTime, format)) > time) {
//                        String voucherName = array.getJSONObject(j).getString("voucher_name");
//                        //累计使用
//                        long cumulativeUse = array.getJSONObject(j).getLong("cumulative_use");
//                        IScene scene = VerificationRecord.builder().voucherName(voucherName).build();
//                        //核销数量
//                        long verificationRecordTotal = jc.invokeApi(scene).getLong("total");
//                        CommonUtil.valueView(cumulativeUse, verificationRecordTotal);
//                        Preconditions.checkArgument(cumulativeUse == verificationRecordTotal,
//                                voucherName + "累计发出数：" + cumulativeUse + "发卡记录数：" + verificationRecordTotal);
//                        CommonUtil.logger(voucherName);
//                    }
//                }
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--累计使用=【核销记录】中按该卡券名称搜索结果的列表数");
//        }
//    }
//
//    @Test(description = "卡券表单--增发库存=【卡券审核】该卡券申请记录中通过的增发总数")
//    public void voucherManage_data_16() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    String voucherName = array.getJSONObject(j).getString("voucher_name");
//                    int additionalInventory = array.getJSONObject(j).getInteger("additional_inventory");
//                    int numSum = 0;
//                    //卡券审核页
//                    ApplyPage.ApplyPageBuilder applyBuilder = ApplyPage.builder().status("AGREE").name(voucherName);
//                    int total1 = jc.invokeApi(applyBuilder.build()).getInteger("total");
//                    int x = CommonUtil.getTurningPage(total1, size);
//                    for (int i1 = 1; i1 < x; i1++) {
//                        JSONArray applyArray = jc.invokeApi(applyBuilder.page(i1).size(size).build()).getJSONArray("list");
//                        for (int j1 = 0; j1 < applyArray.size(); j1++) {
//                            if (applyArray.getJSONObject(j1).getString("apply_type_name").equals("增发")
//                                    && applyArray.getJSONObject(j1).getString("name").equals(voucherName)) {
//                                numSum += applyArray.getJSONObject(j1).getInteger("num");
//                            }
//                        }
//                    }
//                    CommonUtil.valueView(additionalInventory, numSum);
//                    Preconditions.checkArgument(additionalInventory == numSum,
//                            voucherName + "增发库存为：" + additionalInventory + "申请记录中通过的增发总数：" + numSum);
//                }
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--增发库存=【卡券审核】该卡券申请记录中通过的增发总数");
//        }
//    }
//
//    @Test(description = "卡券表单--创建一种卡券，【卡券审核】列表+1")
//    public void voucherManage_data_17() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //创建卡券前卡券审核页列表数
//            ApplyPage.ApplyPageBuilder builder = ApplyPage.builder();
//            Long total = jc.invokeApi(builder.build()).getLong("total");
//            //创建一种卡券
//            util.createVoucher(1000L);
//            //创建卡券后卡券审核页列表数
//            Long newTotal = jc.invokeApi(builder.build()).getLong("total");
//            Preconditions.checkArgument(newTotal == total + 1,
//                    "【卡券审核】列表数：" + CommonUtil.checkResult(total + 1, newTotal));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--增发库存=【卡券审核】该卡券所有通过申请记录(标记为增发)发出个数之和");
//        }
//    }
//
//    @Test(description = "卡券表单--每增发一次卡券，【卡券审核】列表+1")
//    public void voucherManage_data_18() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //增发卡券前卡券审核页列表数
//            ApplyPage.ApplyPageBuilder applyBuild = ApplyPage.builder();
//            Long applyTotal = jc.invokeApi(applyBuild.build()).getLong("total");
//            //增发一种卡券
//            String voucherName = null;
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    if (array.getJSONObject(j).getString("audit_status_name").equals("已通过")
//                            && !array.getJSONObject(j).getString("invalid_status_name").equals("已作废")) {
//                        voucherName = array.getJSONObject(j).getString("voucher_name");
//                        break;
//                    }
//                }
//            }
//            //增发卡券
//            util.addVoucher(voucherName, 10);
//            //增发卡券后卡券审核页列表数
//            Long newApplyTotal = jc.invokeApi(applyBuild.build()).getLong("total");
//            CommonUtil.valueView(applyTotal, newApplyTotal);
//            Preconditions.checkArgument(newApplyTotal == applyTotal + 1,
//                    "【卡券审核】列表数：" + CommonUtil.checkResult(applyTotal + 1, newApplyTotal));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--每增发一次卡券，【卡券审核】列表+1");
//        }
//    }
//
//    @Test(description = "卡券表单--【创建套餐】下拉选择列表数=卡券列表数（未作废）")
//    public void voucherManage_data_19() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray list = jc.pcVoucherList().getJSONArray("list");
//            VoucherFormPage.VoucherFormPageBuilder builder = VoucherFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            int listSize = 0;
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                for (int j = 0; j < array.size(); j++) {
//                    if (!array.getJSONObject(j).getString("invalid_status_name").equals("已作废")
//                            && !array.getJSONObject(j).getString("audit_status_name").equals("已拒绝")
//                            && !array.getJSONObject(j).getString("audit_status_name").equals("审核中")) {
//                        listSize++;
//                    }
//                }
//            }
//            CommonUtil.valueView(list.size(), listSize);
//            Preconditions.checkArgument(list.size() == listSize,
//                    "卡券列表数：" + listSize + "下拉选择列表：" + list.size());
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--创建一张卡券后，【创建套餐】下拉选择列表数=卡券列表数（未作废&剩余库存！=0）");
//        }
//    }
//
//    @Test(description = "卡券表单--每转移一张卡券，发卡记录列表数量+0")
//    public void voucherManage_data_20() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //发出一张卡券
//            long voucherId = util.pushMessage(true);
//            String voucherName = util.getVoucherName(voucherId);
//            //发卡记录列表数
//            IScene scene = SendRecord.builder().build();
//            int total = jc.invokeApi(scene).getInteger("total");
//            //转移一张卡券
//            user.loginApplet(appletUser);
//            long id = util.getAppletVoucherId(voucherName);
//            user.login(administrator);
//            List<Long> list = new ArrayList<>();
//            list.add(id);
//            IScene scene1 = Transfer.builder().transferPhone(marketing.getPhone())
//                    .receivePhone("13373166806").voucherIds(list).build();
//            jc.invokeApi(scene1);
//            int newTotal = jc.invokeApi(scene).getInteger("total");
//            CommonUtil.valueView(total, newTotal);
//            Preconditions.checkArgument(newTotal == total, "转移卡券前发卡记录列表数：" + total + "转移卡券后发卡记录列表数：" + newTotal);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--每转移一张卡券，发卡记录列表数量+0");
//        }
//    }
//
//    @Test(description = "核销人员--创建异页核销,列表数+1")
//    public void voucherManage_data_21() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            IScene scene = VerificationPeople.builder().build();
//            //查询列表数
//            int total = jc.invokeApi(scene).getInteger("total");
//            String phone = util.getDistinctPhone();
//            IScene scene1 = CreateVerificationPeople.builder().verificationPersonName("walawala")
//                    .verificationPersonPhone(phone).type(1).status(1).build();
//            jc.invokeApi(scene1);
//            int newTotal = jc.invokeApi(scene).getInteger("total");
//            CommonUtil.valueView(total, newTotal);
//            Preconditions.checkArgument(newTotal == total + 1,
//                    "列表数：" + CommonUtil.checkResult(total + 1, newTotal));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("核销人员--创建异页核销,列表数+1");
//        }
//    }
//
//    @Test(description = "核销人员--小程序自助核销一张，使用的核销码对应人员册核销数量+1&【核销记录】列表数+1&&核销渠道=主动核销")
//    public void voucherManage_data_22() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //获取核销码
//            String code = util.getVerificationCode("本司员工");
//            IScene scene = VerificationPeople.builder().verificationCode(code).build();
//            int num = CommonUtil.getIntField(jc.invokeApi(scene), 0, "verification_number");
//            //核销列表
//            VerificationRecord.VerificationRecordBuilder builder = VerificationRecord.builder();
//            int verificationTotal = jc.invokeApi(builder.build()).getInteger("total");
//            //核销
//            user.loginApplet(appletUser);
//            AppletVoucherList voucherInfo = util.getAppletCanUsedVoucherInfoList().get(0);
//            long id = voucherInfo.getId();
//            String voucherName = voucherInfo.getTitle();
//            CommonUtil.valueView(id, voucherName);
//            long voucherId = util.getAppletVoucherId(voucherName);
//            IScene scene1 = VoucherVerification.builder().id(String.valueOf(id)).verificationCode(code).build();
//            String message = jc.invokeApi(scene1, false).getString("message");
//            if (message.equals("该卡券未开启自助核销功能")) {
//                jc.pcSwichSelfVerification(voucherId, true);
//                jc.invokeApi(scene1, false);
//            }
//            //核销后该核销员的核销数量
//            user.login(administrator);
//            int newNum = CommonUtil.getIntField(jc.invokeApi(scene), 0, "verification_number");
//            CommonUtil.valueView(num, newNum);
//            Preconditions.checkArgument(newNum == num + 1, "核销前核销数：" + num + "核销后核销数：" + newNum);
//            int newVerificationTotal = jc.invokeApi(builder.build()).getInteger("total");
//            CommonUtil.valueView(verificationTotal, newVerificationTotal);
//            Preconditions.checkArgument(newVerificationTotal == verificationTotal + 1,
//                    "核销前核销记录列表数：" + verificationTotal + "核销后核销记录列表数：" + newVerificationTotal);
//            //核销渠道
//            JSONObject response = jc.invokeApi(builder.voucherName(voucherName).build());
//            String verificationChannelName = CommonUtil.getStrField(response, 0, "verification_channel_name");
//            CommonUtil.valueView(verificationChannelName);
//            Preconditions.checkArgument(verificationChannelName.equals("主动核销"),
//                    "核销渠道：" + CommonUtil.checkResult("主动核销", verificationChannelName));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("核销人员--小程序自助核销一张，使用的核销码对应人员册核销数量+1&【核销记录】列表数+1&&核销渠道=主动核销");
//        }
//    }
//
//    @Test(description = "核销人员--创建财务核销,列表数+1&创建异页核销,列表数+1", enabled = false)
//    public void voucherManage_data_30() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            IScene scene = VerificationPeople.builder().build();
//            //查询列表数
//            int total = jc.invokeApi(scene).getInteger("total");
//            String phone = util.getStaffPhone();
//            CommonUtil.valueView(phone);
//            user.login(marketing);
//            //创建核销人员
//            IScene scene1 = CreateVerificationPeople.builder().verificationPersonName("walawala")
//                    .verificationPersonPhone(phone).type(0).status(1).build();
//            jc.invokeApi(scene1);
//            //创建核销人员后数据
//            int newTotal = jc.invokeApi(scene).getInteger("total");
//            Preconditions.checkArgument(newTotal == total + 1,
//                    "列表数：" + CommonUtil.checkResult(total + 1, newTotal));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("核销人员--创建财务核销,列表数+1&创建异页核销,列表数+1");
//        }
//    }
//
//    @Test(description = "套餐表单--创建套餐，选择各个主体创建套餐，套餐列表每次均+1")
//    public void packageManager_data_1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Arrays.stream(EnumSubject.values()).forEach(subject -> {
//                //创建套餐前列表数量
//                IScene scene = PackageFormPage.builder().build();
//                int total = jc.invokeApi(scene).getInteger("total");
//                //创建套餐
//                String packageName = util.createPackageName();
//                String subjectType = subject.name();
//                Long subjectId = util.getSubjectId(subjectType);
//                IScene scene1 = CreatePackage.builder().packageName(packageName).validity("30")
//                        .packageDescription(util.getDesc()).subjectType(subjectType)
//                        .subjectId(subjectId).voucherList(util.getVoucherInfo())
//                        .packagePrice(5000.99).shopIds(util.getShopIdList()).build();
//                jc.invokeApi(scene1);
//                //创建套餐后列表数量
//                int newTotal = jc.invokeApi(scene).getInteger("total");
//                Preconditions.checkArgument(newTotal == total + 1,
//                        "创建套餐前列表数量：" + total + " 创建套餐后列表数量：" + newTotal);
//                //列表内容校验
//                IScene scene2 = PackageFormPage.builder().packageName(packageName).build();
//                JSONObject object = jc.invokeApi(scene2);
//                String packagePrice = CommonUtil.getStrField(object, 0, "package_price");
//                Preconditions.checkArgument(packagePrice.equals("5000.99"), "创建套餐时套餐价格：5000.99" + "创建成功后列表展示套餐价格：" + packagePrice);
//                int validity = CommonUtil.getIntField(object, 0, "validity");
//                Preconditions.checkArgument(validity == 30, "创建套餐时有效期：30" + "创建成功后列表展示有效期：" + validity);
//                int voucherNumber = CommonUtil.getIntField(object, 0, "voucher_number");
//                Preconditions.checkArgument(voucherNumber == 10, "创建套餐时套餐内包含卡券数：10" + "创建成功后列表展示卡券数量：" + voucherNumber);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--创建套餐，选择各个主体创建套餐，套餐列表每次均+1");
//        }
//    }
//
//    @Test(description = "套餐表单--购买一个固定套餐，该套餐售出+1")
//    public void packageManager_data_2() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //获取售出套数
//            IScene scene = PackageFormPage.builder().packageName("凯迪拉克无限套餐").build();
//            int soldNumber = CommonUtil.getIntField(jc.invokeApi(scene), 0, "sold_number");
//            //购买固定套餐
//            util.buyFixedPackage(1);
//            //确认支付
//            util.makeSureBuyPackage("凯迪拉克无限套餐");
//            //购买后售出套数
//            long newSoldNumber = CommonUtil.getIntField(jc.invokeApi(scene), 0, "sold_number");
//            CommonUtil.valueView(soldNumber, newSoldNumber);
//            Preconditions.checkArgument(newSoldNumber == soldNumber + 1,
//                    "凯迪拉克无限套餐" + "售出：" + CommonUtil.checkResult(soldNumber + 1, newSoldNumber));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买一个固定套餐，该套餐售出+1");
//        }
//    }
//
//    @Test(description = "套餐表单--赠送一个固定套餐，该套餐赠送+1")
//    public void packageManager_data_3() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //获取售出套数
//            IScene scene = PackageFormPage.builder().packageName("凯迪拉克无限套餐").build();
//            int giveNumber = CommonUtil.getIntField(jc.invokeApi(scene), 0, "give_number");
//            //购买固定套餐
//            util.buyFixedPackage(0);
//            //确认支付
//            util.makeSureBuyPackage("凯迪拉克无限套餐");
//            //购买后售出套数
//            long newGiveNumber = CommonUtil.getIntField(jc.invokeApi(scene), 0, "give_number");
//            CommonUtil.valueView(giveNumber, newGiveNumber);
//            Preconditions.checkArgument(newGiveNumber == giveNumber + 1,
//                    "凯迪拉克无限套餐赠送：" + CommonUtil.checkResult(giveNumber + 1, newGiveNumber));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--赠送一个固定套餐，该套餐赠送+1");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐--【小程序客户】列表的手机号，均可查询到姓名")
//    public void packageManager_data_4() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            WechatCustomerPage.WechatCustomerPageBuilder builder = WechatCustomerPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                array.forEach(e -> {
//                    JSONObject jsonObject = (JSONObject) e;
//                    String customerPhone = jsonObject.getString("customer_phone");
//                    String customerName = jsonObject.getString("customer_name");
//                    String name = jc.pcSearchCustomer(customerPhone).getString("customer_name");
//                    CommonUtil.valueView(customerName, name);
//                    Preconditions.checkArgument(customerName.equals(name),
//                            customerPhone + "在小程序客户的名称为：" + customerName + "购买套餐时展示的名称为：" + name);
//                    CommonUtil.logger(customerPhone);
//                });
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐--【小程序客户】列表的手机号，均可查询到姓名");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐--套餐价格=所选套餐的列表展示价格")
//    public void packageManager_data_5() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray array = jc.pcPackageList().getJSONArray("list");
//            array.forEach(e -> {
//                JSONObject jsonObject = (JSONObject) e;
//                String packageName = jsonObject.getString("package_name");
//                String price = jsonObject.getString("price");
//                String listPrice = util.getPackageInfo(packageName).getPrice();
//                CommonUtil.valueView(price, listPrice);
//                Preconditions.checkArgument(listPrice.equals(price), packageName + "购买套餐时套餐价格为：" + price + "此套餐列表展示套餐价格为：" + listPrice);
//                CommonUtil.logger(packageName);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐--套餐价格=所选套餐的列表展示价格");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐--下拉可选择的套餐数量=套餐表单列表数（未取消&未过期）")
//    public void packageManager_data_6() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String format = "yyyy-MM-dd HH:mm";
//            String today = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd") + " 00:00";
//            long todayUnix = Long.parseLong(DateTimeUtil.dateToStamp(today, format));
//            user.login(administrator);
//            int arraySize = jc.pcPackageList().getJSONArray("list").size();
//            PackageFormPage.PackageFormPageBuilder builder = PackageFormPage.builder().packageStatus(true);
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            AtomicInteger sum = new AtomicInteger();
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                array.forEach(e -> {
//                    JSONObject object = (JSONObject) e;
//                    String createTime = object.getString("create_time");
//                    int validity = object.getInteger("validity");
//                    long unix = Long.parseLong(DateTimeUtil.dateToStamp(createTime, format)) + (long) validity * 24 * 60 * 60 * 1000;
//                    if (unix >= todayUnix) {
//                        sum.getAndIncrement();
//                    }
//                    CommonUtil.logger(createTime);
//                });
//            }
//            CommonUtil.valueView(arraySize, sum);
//            Preconditions.checkArgument(arraySize == sum.get(), "下拉可选择的套餐数量为：" + arraySize + "套餐表单列表数（未取消&未过期）为：" + sum.get());
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐--下拉可选择的套餐数量=套餐表单列表数（未取消）");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐--临时套餐购买/赠送一张此卡券，【发卡记录】列表+1")
//    public void packageManager_data_7() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //发卡记录列表数
//            int sendRecordTotal = jc.invokeApi(SendRecord.builder().build()).getInteger("total");
//            //购买临时套餐
//            util.buyTemporaryPackage(1);
//            //确认支付
//            util.makeSureBuyPackage("临时套餐");
//            //购买后发卡记录
//            int newSendRecordTotal = jc.invokeApi(SendRecord.builder().build()).getInteger("total");
//            CommonUtil.valueView(sendRecordTotal, newSendRecordTotal);
//            Preconditions.checkArgument(newSendRecordTotal == sendRecordTotal + 1,
//                    "购买套餐前发卡记录列表数为：" + sendRecordTotal + "购买套餐后发卡记录列表数为：" + newSendRecordTotal);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐--临时套餐购买/赠送一张此卡券，【发卡记录】列表+1");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐--固定套餐购买/赠送一个套餐（此套餐内包含此卡券一张），【发卡记录】列表+1")
//    public void packageManager_data_8() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //发卡记录列表数
//            int sendRecordTotal = jc.invokeApi(SendRecord.builder().build()).getInteger("total");
//            // 购买固定套餐
//            util.buyFixedPackage(1);
//            //确认支付
//            util.makeSureBuyPackage("凯迪拉克无限套餐");
//            int newSendRecordTotal = jc.invokeApi(SendRecord.builder().build()).getInteger("total");
//            CommonUtil.valueView(sendRecordTotal, newSendRecordTotal);
//            Preconditions.checkArgument(newSendRecordTotal == sendRecordTotal + 1,
//                    "购买套餐前发卡记录列表数为：" + sendRecordTotal + "购买套餐后发卡记录列表数为：" + newSendRecordTotal);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐--固定套餐购买/赠送一个套餐（此套餐内包含此卡券一张），【发卡记录】列表+1");
//        }
//    }
//
//    @Test(description = "套餐购买记录--套餐表单购买/赠送一次套餐，列表+1&渠道=线下")
//    public void packageManager_data_9() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //套餐购买记录列表数
//            int buyPackageRecordTotal = jc.invokeApi(BuyPackageRecord.builder().build()).getInteger("total");
//            // 购买固定套餐
//            util.buyFixedPackage(1);
//            // 确认支付
//            util.makeSureBuyPackage("凯迪拉克无限套餐");
//            JSONObject response = jc.invokeApi(BuyPackageRecord.builder().build());
//            int newBuyPackageRecordTotal = response.getInteger("total");
//            String sendChannelName = CommonUtil.getStrField(jc.invokeApi(BuyPackageRecord.builder().packageName("凯迪拉克无限套餐").build()), 0, "send_channel_name");
//            Preconditions.checkArgument(sendChannelName.equals("线下"), "购买套餐后发出渠道为：" + sendChannelName);
//            CommonUtil.valueView(buyPackageRecordTotal, newBuyPackageRecordTotal);
//            Preconditions.checkArgument(newBuyPackageRecordTotal == buyPackageRecordTotal + 1,
//                    "购买套餐前套餐购买记录列表数为：" + buyPackageRecordTotal + "购买套餐后套餐购买记录列表数为：" + newBuyPackageRecordTotal);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐购买记录--套餐表单购买/赠送一次套餐，列表+1&渠道=线下");
//        }
//    }
//
//    @Test(description = "套餐购买记录--某一赠送类型套餐的列表数>=【套餐表单】中此套餐的赠送数量")
//    public void packageManager_data_10() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String format = "yyyy-MM-dd HH:mm";
//            String startTime = "2020-12-09";
//            String endTime = DateTimeUtil.getFormat(new Date());
//            PackageFormPage.PackageFormPageBuilder builder = PackageFormPage.builder().startTime(startTime).endTime(endTime);
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                builder.page(i).size(size);
//                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
//                array.forEach(e -> {
//                    JSONObject jsonObject = (JSONObject) e;
//                    String packageName = jsonObject.getString("package_name");
//                    int giveNumber = jsonObject.getInteger("give_number");
//                    long createTime = Long.parseLong(DateTimeUtil.dateToStamp(jsonObject.getString("create_time"), format));
//                    BuyPackageRecord.BuyPackageRecordBuilder builder1 = BuyPackageRecord.builder().packageName(packageName)
//                            .startTime(startTime).endTime(endTime).sendType(0);
//                    int buyPackageRecordTotal = jc.invokeApi(builder1.build()).getInteger("total");
//                    int x = CommonUtil.getTurningPage(buyPackageRecordTotal, size);
//                    AtomicInteger sum = new AtomicInteger();
//                    for (int j = 1; j < x; j++) {
//                        JSONArray array1 = jc.invokeApi(builder1.page(j).size(size).build()).getJSONArray("list");
//                        array1.forEach(b -> {
//                            JSONObject object = (JSONObject) b;
//                            long sendTime = Long.parseLong(DateTimeUtil.dateToStamp(object.getString("send_time"), format));
//                            if (sendTime > createTime) {
//                                sum.getAndIncrement();
//                            }
//                        });
//                    }
//                    CommonUtil.valueView(giveNumber, sum.get());
//                    Preconditions.checkArgument(giveNumber >= sum.get(),
//                            packageName + "累计赠送：" + giveNumber + " 套餐购买记录中赠送数量：" + sum.get());
//                    CommonUtil.logger(packageName);
//                });
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐购买记录--某一赠送类型套餐的列表数>=【套餐表单】中此套餐的赠送数量");
//        }
//    }
//
//    @Test(description = "套餐购买记录--某一类型套餐确认支付后的的列表数=【套餐表单】中此套餐的售出数量")
//    public void packageManager_data_11() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String format = "yyyy-MM-dd HH:mm";
//            String startTime = "2020-12-09";
//            String endTime = DateTimeUtil.getFormat(new Date());
//            PackageFormPage.PackageFormPageBuilder builder = PackageFormPage.builder().startTime(startTime).endTime(endTime);
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                builder.page(i).size(size);
//                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
//                array.forEach(e -> {
//                    JSONObject jsonObject = (JSONObject) e;
//                    String packageName = jsonObject.getString("package_name");
//                    int soldNumber = jsonObject.getInteger("sold_number");
//                    long createTime = Long.parseLong(DateTimeUtil.dateToStamp(jsonObject.getString("create_time"), format));
//                    BuyPackageRecord.BuyPackageRecordBuilder builder1 = BuyPackageRecord.builder().packageName(packageName)
//                            .sendType(1).startTime(startTime).endTime(endTime);
//                    int buyPackageRecordTotal = jc.invokeApi(builder1.build()).getInteger("total");
//                    int x = CommonUtil.getTurningPage(buyPackageRecordTotal, size);
//                    AtomicInteger sum = new AtomicInteger();
//                    for (int j = 1; j < x; j++) {
//                        JSONArray array1 = jc.invokeApi(builder1.page(j).size(size).build()).getJSONArray("list");
//                        array1.forEach(b -> {
//                            JSONObject object = (JSONObject) b;
//                            long sendTime = Long.parseLong(DateTimeUtil.dateToStamp(object.getString("send_time"), format));
//                            if (sendTime >= createTime && !((JSONObject) b).getBoolean("status")) {
//                                sum.getAndIncrement();
//                            }
//                        });
//                    }
//                    CommonUtil.valueView(soldNumber, sum.get());
//                    Preconditions.checkArgument(soldNumber == sum.get(),
//                            packageName + "累计售出：" + soldNumber + " 套餐购买记录中购买并且确认支付数量：" + sum.get());
//                    CommonUtil.logger(packageName);
//                });
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐购买记录--某一类型套餐确认支付后的的列表数=【套餐表单】中此套餐的售出数量");
//        }
//    }
//
//    @Test(description = "套餐购买记录--购买临时套餐确认支付后，购买客户对应的小程序【我的卡券】+1，【我的套餐】+0")
//    public void packageManager_data_12() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            user.loginApplet(appletUser);
//            int voucherNumber = util.getAppletVoucherNum();
//            int packageNumber = util.getAppletPackageNum();
//            user.login(marketing);
//            //购买临时套餐
//            util.buyTemporaryPackage(1);
//            //确认支付
//            util.makeSureBuyPackage("临时套餐");
//            user.loginApplet(appletUser);
//            int newVoucherNumber = util.getAppletVoucherNum();
//            int newPackageNumber = util.getAppletPackageNum();
//            CommonUtil.valueView(voucherNumber, newVoucherNumber, packageNumber, newPackageNumber);
//            Preconditions.checkArgument(newVoucherNumber == voucherNumber + 1, "购买套餐前我的卡券列表数：" + voucherNumber + "购买套餐后我的卡券列表数：" + newVoucherNumber);
//            Preconditions.checkArgument(newPackageNumber == packageNumber, "购买套餐前我的套餐列表数：" + packageNumber + "购买套餐后我的套餐列表数：" + newPackageNumber);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐购买记录--某一售出类型套餐的列表数=【套餐表单】中此套餐的售出数量");
//        }
//    }
//
//    @Test(description = "套餐购买记录--购买固定套餐时包含1张卡券，确认支付后，购买客户对应的小程序【我的套餐】+1，【我的卡券】+0")
//    public void packageManager_data_13() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            user.loginApplet(appletUser);
//            int voucherNumber = util.getAppletVoucherNum();
//            int packageNumber = util.getAppletPackageNum();
//            user.login(administrator);
//            //购买固定套餐
//            util.buyFixedPackage(1);
//            //确认支付
//            util.makeSureBuyPackage(EnumVP.ONE.getPackageName());
//            user.loginApplet(appletUser);
//            int newVoucherNumber = util.getAppletVoucherNum();
//            int newPackageNumber = util.getAppletPackageNum();
//            CommonUtil.valueView(voucherNumber, newVoucherNumber, packageNumber, newPackageNumber);
//            Preconditions.checkArgument(newVoucherNumber == voucherNumber, "购买套餐前我的卡券列表数：" + voucherNumber + "购买套餐后我的卡券列表数：" + newVoucherNumber);
//            Preconditions.checkArgument(newPackageNumber == packageNumber + 1, "购买套餐前我的套餐列表数：" + packageNumber + "购买套餐后我的套餐列表数：" + newPackageNumber);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐购买记录--购买固定套餐时包含1张卡券，确认支付后，购买客户对应的小程序【我的套餐】+1，【我的卡券】+0");
//        }
//    }
//
//    @Test(description = "消息管理--推送消息含有一张卡券，推送成功后，【卡券表单页】该卡券累计发出+1，发卡记录列表+1")
//    public void messageManager_data_1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            List<String> phoneList = new ArrayList<>();
//            phoneList.add(marketing.getPhone());
//            List<Long> voucherList = new ArrayList<>();
//            long voucherId = util.getVoucherInfo().getJSONObject(0).getLong("voucher_id");
//            voucherList.add(voucherId);
//            String name = util.getVoucherName(voucherId);
//            //获取卡券累计发出
//            IScene scene = VoucherFormPage.builder().voucherName(name).build();
//            long cumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
//            //发卡记录页列表数
//            int sendRecordTotal = jc.invokeApi(SendRecord.builder().build()).getInteger("total");
//            //消息发送一张卡券
//            IScene sendMesScene = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                    .telList(phoneList).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
//                    .type(0).voucherOrPackageList(voucherList).useDays(10).ifSendImmediately(true).build();
//            jc.invokeApi(sendMesScene);
//            long newCumulativeDelivery = CommonUtil.getIntField(jc.invokeApi(scene), 0, "cumulative_delivery");
//            CommonUtil.valueView(cumulativeDelivery, newCumulativeDelivery);
//            Preconditions.checkArgument(newCumulativeDelivery == cumulativeDelivery + 1,
//                    "消息发出一张卡券后，" + name + "累计发出数量：" + CommonUtil.checkResult(cumulativeDelivery + 1, newCumulativeDelivery));
//            int newSendRecordTotal = jc.invokeApi(SendRecord.builder().build()).getInteger("total");
//            CommonUtil.valueView(sendRecordTotal, newSendRecordTotal);
//            Preconditions.checkArgument(newSendRecordTotal == sendRecordTotal + 1
//                    , "发卡记录列表数：" + CommonUtil.checkResult(sendRecordTotal + 1, newSendRecordTotal));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--推送消息含有一张卡券，推送成功后，【卡券表单页】该卡券累计发出+1，发卡记录列表+1");
//        }
//    }
//
//    @Test(description = "消息管理--实时发送，列表+1&状态=发送成功")
//    public void messageManager_data_2() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //消息列表数
//            MessageFormPage.MessageFormPageBuilder builder = MessageFormPage.builder();
//            int messageTotal = jc.invokeApi(builder.build()).getInteger("total");
//            //推送消息
//            util.pushMessage(true);
//            //消息发送时间
//            String sendTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
//            //推送后消息列表数
//            int newMessageTotal = jc.invokeApi(builder.build()).getInteger("total");
//            CommonUtil.valueView(messageTotal, newMessageTotal);
//            Preconditions.checkArgument(newMessageTotal == messageTotal + 1
//                    , "消息推送后，消息列表数：" + CommonUtil.checkResult(messageTotal + 1, newMessageTotal));
//            //消息的状态为发送成功
//            int s = CommonUtil.getTurningPage(newMessageTotal, size);
//            for (int i = 1; i < s; i++) {
//                builder.page(i).size(size);
//                JSONArray array = jc.invokeApi(builder.build()).getJSONArray("list");
//                array.forEach(e -> {
//                    //消息状态校验
//                    JSONObject jsonObject = (JSONObject) e;
//                    String pushTime = jsonObject.getString("push_time");
//                    if (pushTime.equals(sendTime)) {
//                        CommonUtil.valueView(sendTime, pushTime);
//                        String sendStatusName = jsonObject.getString("send_status_name");
//                        Preconditions.checkArgument(sendStatusName.equals(EnumSendStatusName.SENT.getName())
//                                , "消息状态为：" + CommonUtil.checkResult(EnumSendStatusName.SENT.getName(), sendStatusName));
//                        //消息内容校验
//                        String content = jsonObject.getString("content");
//                        CommonUtil.valueView(content, EnumContent.C.getContent());
//                        Preconditions.checkArgument(content.equals(EnumContent.C.getContent()),
//                                "消息内容：" + CommonUtil.checkResult(EnumContent.C.getContent(), content));
//                    }
//                });
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--实时发送，列表+1&状态=发送成功");
//        }
//    }
//
//    @Test(description = "消息管理--定时发送，列表+1&状态=排期中")
//    public void messageManager_data_3() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //消息列表数
//            MessageFormPage.MessageFormPageBuilder builder = MessageFormPage.builder();
//            int messageTotal = jc.invokeApi(builder.build()).getInteger("total");
//            //发送消息
//            util.pushMessage(false);
//            //新消息列表
//            int newMessageTotal = jc.invokeApi(builder.build()).getInteger("total");
//            CommonUtil.valueView(messageTotal, newMessageTotal);
//            Preconditions.checkArgument(newMessageTotal == messageTotal + 1,
//                    "消息定时后，消息列表数：" + CommonUtil.checkResult(messageTotal + 1, newMessageTotal));
//            String sendTime = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 80), "yyyy-MM-dd HH:mm");
//            int s = CommonUtil.getTurningPage(newMessageTotal, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                array.forEach(e -> {
//                    JSONObject jsonObject = (JSONObject) e;
//                    String pushTime = jsonObject.getString("push_time");
//                    if (pushTime.equals(sendTime)) {
//                        CommonUtil.valueView(sendTime, pushTime);
//                        String sendStatusName = jsonObject.getString("send_status_name");
//                        Preconditions.checkArgument(sendStatusName.equals(EnumSendStatusName.SCHEDULE.getName())
//                                , "消息状态为：" + CommonUtil.checkResult(EnumSendStatusName.SCHEDULE.getName(), sendStatusName));
//                    }
//                });
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--定时发送，列表+1&状态=排期中");
//        }
//    }
//
//    @Test(description = "消息管理--发送成功率=发出条数/收到条数，结果x=100%时为全部成功，结果0%<=x<100%显示成功百分比")
//    public void messageManager_data_4() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            MessageFormPage.MessageFormPageBuilder builder = MessageFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, 10);
//            for (int i = 1; i < s; i++) {
//                JSONArray list = jc.invokeApi(builder.page(i).size(10).build()).getJSONArray("list");
//                for (int j = 0; j < list.size(); j++) {
//                    JSONObject jsonObject = list.getJSONObject(j);
//                    int sendCount = jsonObject.getInteger("send_count");
//                    int receiveCount = jsonObject.getInteger("receive_count");
//                    String percent = CommonUtil.getPercent(sendCount, receiveCount);
//                    CommonUtil.valueView(percent);
//                    String result = percent.equals("0.0%") ? "成功0%" : "全部成功";
//                    String statusName = jsonObject.getString("status_name");
//                    CommonUtil.valueView(result, statusName);
//                    Preconditions.checkArgument(statusName.equals(result),
//                            i + "页" + (j + 1) + "条" + "发出条数/收到条数=" + percent + CommonUtil.checkResult(result, statusName));
//                    CommonUtil.logger(i + "页" + (j + 1) + "条");
//                }
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--发送成功率=发出条数/收到条数，结果x=100%时为全部成功，结果0%<=x<100%显示成功百分比");
//        }
//    }
//
//    @Test(description = "消息管理--发送给一个客户时客户名称&联系方式&车牌号码显示该客户信息")
//    public void messageManager_data_5() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //发送消息
//            util.pushMessage(true);
//            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
//            //发送消息后表单
//            MessageFormPage.MessageFormPageBuilder builder = MessageFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray list = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                list.forEach(e -> {
//                    JSONObject jsonObject = (JSONObject) e;
//                    if (jsonObject.getString("push_time").equals(date)) {
//                        String platNumber = jsonObject.getString("plate_number");
//                        String customerName = jsonObject.getString("customer_name");
//                        String customerPhone = jsonObject.getString("customer_phone");
//                        String number = util.getPlatNumber(customerPhone);
//                        CommonUtil.valueView(platNumber, customerName, customerPhone, number);
//                        Preconditions.checkArgument(platNumber.equals(number),
//                                "车牌号" + CommonUtil.checkResult(number, platNumber));
//                        Preconditions.checkArgument(customerPhone.equals(marketing.getPhone()),
//                                "联系方式" + CommonUtil.checkResult(marketing.getPhone(), customerPhone));
//                    }
//                });
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--发送给一个客户时客户名称&联系方式&车牌号码显示该客户信息");
//        }
//    }
//
//    @Test(description = "消息管理--发送给发送多人时客户名称为全部&联系方式&车牌号码显示为空", priority = 1)
//    public void messageManager_data_6() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            user.login(administrator);
//            List<Long> shopList = new ArrayList<>();
//            JSONArray array = jc.pcShopList().getJSONArray("list");
//            shopList.add(array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("shop_id")).collect(Collectors.toList()).get(0));
//            //发送消息
//            IScene scene = PushMessage.builder().pushTarget(EnumPushTarget.SHOP_CUSTOMER.name()).shopList(shopList)
//                    .messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
//                    .ifSendImmediately(true).build();
//            jc.invokeApi(scene);
//            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
//            MessageFormPage.MessageFormPageBuilder builder = MessageFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray list = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                list.forEach(e -> {
//                    JSONObject jsonObject = (JSONObject) e;
//                    if (jsonObject.getString("push_time").equals(date)) {
//                        String platNumber = jsonObject.getString("plate_number");
//                        CommonUtil.valueView(platNumber);
//                        Preconditions.checkArgument(platNumber == null,
//                                "车牌号" + CommonUtil.checkResult(null, platNumber));
//                        String customerName = jsonObject.getString("customer_name");
//                        CommonUtil.valueView(customerName);
//                        Preconditions.checkArgument(customerName.equals("全部"),
//                                "客户名称" + CommonUtil.checkResult("全部", customerName));
//                        String customerPhone = jsonObject.getString("customer_phone");
//                        CommonUtil.valueView(customerPhone);
//                        Preconditions.checkArgument(customerPhone == null,
//                                "联系方式" + CommonUtil.checkResult(null, customerPhone));
//                    }
//                });
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--发送给发送多人时客户名称为全部&联系方式&车牌号码显示为空");
//        }
//    }
//
//    @Test(description = "消息管理--正常情况，发出条数=收到条数")
//    public void messageManager_data_7() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            MessageFormPage.MessageFormPageBuilder builder = MessageFormPage.builder();
//            int total = jc.invokeApi(builder.build()).getInteger("total");
//            int s = CommonUtil.getTurningPage(total, size);
//            for (int i = 1; i < s; i++) {
//                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
//                array.forEach(e -> {
//                    JSONObject jsonObject = (JSONObject) e;
//                    int sendCount = jsonObject.getInteger("send_count");
//                    int receiveCount = jsonObject.getInteger("receive_count");
//                    CommonUtil.valueView(sendCount, receiveCount);
//                    Preconditions.checkArgument(sendCount == receiveCount,
//                            "发出条数：" + sendCount + "收到条数：" + receiveCount);
//                    CommonUtil.logger(jsonObject.getInteger("id"));
//                });
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        }
//    }
//
//    @Test(description = "消息管理--消息发出后，我的消息列表+1")
//    public void messageManager_data_8() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //消息列表数
//            user.loginApplet(appletUser);
//            int listSize = util.getAppletMessageNum();
//            //发消息
//            user.login(administrator);
//            util.pushMessage(true);
//            //消息列表数
//            user.loginApplet(appletUser);
//            int newListSize = util.getAppletMessageNum();
//            CommonUtil.valueView(listSize, newListSize);
//            Preconditions.checkArgument(newListSize == listSize + 1,
//                    "收到消息前消息列表数：" + listSize + "收到消息后消息列表数：" + newListSize);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--消息发出后，我的消息列表+1");
//        }
//    }
//
//    @Test(description = "消息管理--消息发出后，消息内容一致")
//    public void messageManager_data_9() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //发消息
//            util.pushMessage(true);
//            //消息列表消息内容
//            user.loginApplet(appletUser);
//            IScene scene = MessageList.builder().size(20).build();
//            JSONObject response = jc.invokeApi(scene);
//            int id = CommonUtil.getIntField(response, 0, "id");
//            String content = jc.appletMessageDetail(String.valueOf(id)).getString("content");
//            CommonUtil.valueView(content, EnumContent.C.getContent());
//            Preconditions.checkArgument(content.equals(EnumContent.C.getContent()),
//                    "消息内容" + CommonUtil.checkResult(EnumContent.C.getContent(), content));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--消息发出后，消息内容一致");
//        }
//    }
//
//    @Test(description = "卡券表单--新建卡券--卡券名称异常")
//    public void voucherManage_system_1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Long stock = 1000L;
//            String[] strings = {EnumContent.B.getContent(), "1", null, ""};
//            Arrays.stream(strings).forEach(name -> {
//                IScene scene = CreateVoucher.builder().voucherPic(util.getPicPath()).voucherName(name).subjectType(util.getSubjectType())
//                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
//                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = StringUtils.isEmpty(name) ? "卡券名称不能为空" : "卡券名称长度应为2～20个字";
//                Preconditions.checkArgument(message.equals(err), "卡券名称为：" + name + "创建成功");
//                CommonUtil.logger(name);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--新建卡券--卡券名称异常");
//        }
//    }
//
//    @Test(description = "卡券表单--新建卡券--卡券说明异常")
//    public void voucherManage_system_2() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Long stock = 1000L;
//            String[] strings = {null, "", EnumContent.C.getContent()};
//            Arrays.stream(strings).forEach(desc -> {
//                IScene scene = CreateVoucher.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName()).subjectType(util.getSubjectType())
//                        .voucherDescription(desc).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
//                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = StringUtils.isEmpty(desc) ? "卡券说明不能为空" : "卡券描述不能超过200个字";
//                Preconditions.checkArgument(message.equals(err), "卡券说明为：" + desc + "创建成功");
//                CommonUtil.logger(desc);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--新建卡券--卡券说明异常");
//        }
//    }
//
//    @Test(description = "卡券表单--新建卡券--主体类型异常")
//    public void voucherManage_system_3() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Long stock = 1000L;
//            String[] strings = {"全部权限", null, ""};
//            Arrays.stream(strings).forEach(subjectType -> {
//                IScene scene = CreateVoucher.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName()).subjectType(subjectType)
//                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(subjectType)).stock(stock).cost(util.getCost(stock))
//                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                Preconditions.checkArgument(message.equals("主体类型不存在"), "主体类型为：" + subjectType + "创建成功");
//                CommonUtil.logger(subjectType);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--新建卡券--主体类型异常");
//        }
//    }
//
//    @Test(description = "卡券表单--新建卡券--主体详情异常")
//    public void voucherManage_system_4() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Long stock = 1000L;
//            IScene scene = CreateVoucher.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName())
//                    .subjectType(EnumSubject.STORE.name())
//                    .voucherDescription(util.getDesc()).stock(stock).cost(util.getCost(stock))
//                    .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
//            String message = jc.invokeApi(scene, false).getString("message");
//            CommonUtil.valueView(message);
//            Preconditions.checkArgument(message.equals("主体详情不能为空"), "主体详情为：" + null + "创建成功");
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--新建卡券--主体详情异常");
//        }
//    }
//
//    @Test(description = "卡券表单--新建卡券--库存数量异常情况")
//    public void voucherManage_system_5() {
//
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Long[] longs = {1000000000L, null, -100L, 9999999999L};
//            Arrays.stream(longs).forEach(stock -> {
//                IScene scene = CreateVoucher.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName()).subjectType(util.getSubjectType())
//                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
//                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = StringUtils.isEmpty(stock) ? "库存不能为空" : stock > 1000000000L ? "请求入参类型不正确" : "卡券库存范围应在0 ～ 100000000张";
//                Preconditions.checkArgument(message.equals(err), "卡券库存为：" + stock + "创建成功");
//                CommonUtil.logger(stock);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--新建卡券--库存数量异常情况");
//        }
//    }
//
//    @Test(description = "卡券表单--新建卡券--业务类型异常情况")
//    public void voucherManage_system_6() {
//
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Long stock = 1000L;
//            Integer[] integers = {null, -1, 100};
//            Arrays.stream(integers).forEach(shopType -> {
//                IScene scene = CreateVoucher.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName()).subjectType(util.getSubjectType())
//                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
//                        .shopType(shopType).shopIds(util.getShopIdList()).selfVerification(true).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = StringUtils.isEmpty(shopType) ? "业务类型不能为空" : "业务类型不存在";
//                Preconditions.checkArgument(message.equals(err), "业务类型为：" + shopType + "创建成功");
//                CommonUtil.logger(shopType);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--新建卡券--业务类型异常情况");
//        }
//    }
//
//    @Test(description = "卡券表单--新建卡券--成本异常情况")
//    public void voucherManage_system_7() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Long stock = 1000L;
//            Double[] doubles = {null, (double) -1, (double) 1000000000, 100000000.11};
//            Arrays.stream(doubles).forEach(cost -> {
//                IScene scene = CreateVoucher.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName()).subjectType(util.getSubjectType())
//                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(cost)
//                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = StringUtils.isEmpty(cost) ? "成本不能为空" : "卡券成本金额范围应在0 ～ 100000000元";
//                Preconditions.checkArgument(message.equals(err), "成本为：" + cost + "创建成功");
//                CommonUtil.logger(cost);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--新建卡券--成本异常情况");
//        }
//    }
//
//    @Test(description = "卡券表单--新建卡券--选择门店异常")
//    public void voucherManage_system_8() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Long stock = 1000L;
//            IScene scene = CreateVoucher.builder().voucherPic(util.getPicPath()).voucherName(util.createVoucherName())
//                    .subjectType(util.getSubjectType()).voucherDescription(util.getDesc())
//                    .subjectId(util.getSubjectId(util.getSubjectType())).stock(stock).cost(util.getCost(stock))
//                    .shopType(0).selfVerification(true).build();
//            String message = jc.invokeApi(scene, false).getString("message");
//            CommonUtil.valueView(message);
//            Preconditions.checkArgument(message.equals("卡券适用门店列表不能为空"),
//                    "卡券适用门店列表为：" + null + "创建成功");
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--新建卡券--成本异常情况");
//        }
//    }
//
//    @Test(description = "卡券表单--卡券转移，选择要转移的卡券，此时将卡券核销，提示：优惠券【XXXX】已被使用或已过期，请重新选择！")
//    public void voucherManage_system_9() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //获取已使用的卡券列表
//            user.loginApplet(appletUser);
//            List<Long> voucherList = util.getAppletVoucherList(EnumAppletVoucherStatus.USED.getName());
//            String voucherName = util.getAppletVoucherName(voucherList.get(0));
//            //转移
//            user.login(administrator);
//            IScene scene = Transfer.builder().transferPhone(marketing.getPhone()).receivePhone(applet.getPhone())
//                    .voucherIds(getList(voucherList.get(0))).build();
//            String message = jc.invokeApi(scene, false).getString("message");
//            CommonUtil.valueView(message);
//            String err = "优惠券【" + voucherName + "】已被使用或已过期，请重新选择！";
//            CommonUtil.checkResult("转移卡券", voucherName, err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--卡券转移，选择要转移的卡券，此时将卡券核销，确认，提示：优惠券【XXXX】已被使用或已过期，请重新选择！");
//        }
//    }
//
//    @Test(description = "卡券表单--卡券转移，选择要转移的卡券，卡券刚好过期，确认，提示：优惠券【XXXX】已被使用或已过期，请重新选择！")
//    public void voucherManage_system_10() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //获取已使用的卡券列表
//            user.loginApplet(appletUser);
//            List<Long> voucherList = util.getAppletVoucherList(EnumAppletVoucherStatus.EXPIRED.getName());
//            String voucherName = util.getAppletVoucherName(voucherList.get(0));
//            //转移
//            user.login(administrator);
//            IScene scene = Transfer.builder().transferPhone(marketing.getPhone()).receivePhone(applet.getPhone())
//                    .voucherIds(getList(voucherList.get(0))).build();
//            String message = jc.invokeApi(scene, false).getString("message");
//            CommonUtil.valueView(message);
//            String err = "优惠券【" + voucherName + "】已被使用或已过期，请重新选择！";
//            CommonUtil.checkResult("转移卡券", voucherName, err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--卡券转移，选择要转移的卡券，卡券刚好过期，确认，提示：优惠券【XXXX】已被使用或已过期，请重新选择！");
//        }
//    }
//
//    @Test(description = "卡券表单--卡券转移，转移账号异常")
//    public void voucherManage_system_11() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] phones = {"13654973499"};
//            //获取已过期的卡券列表
//            user.loginApplet(appletUser);
//            List<Long> voucherList = util.getAppletVoucherList(EnumAppletVoucherStatus.NEAR_EXPIRED.getName());
//            String voucherName = util.getAppletVoucherName(voucherList.get(0));
//            //转移
//            user.login(administrator);
//            Arrays.stream(phones).forEach(phone -> {
//                IScene scene = Transfer.builder().transferPhone(phone).receivePhone(applet.getPhone())
//                        .voucherIds(getList(voucherList.get(0))).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                String err = "推送用户id不能为空";
//                CommonUtil.checkResult("转移卡券", voucherName, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--卡券转移，选择要转移的卡券，卡券刚好过期，确认，提示：优惠券【XXXX】已被使用或已过期，请重新选择！");
//        }
//    }
//
//    @Test(description = "卡券表单--卡券转移，接收账号异常")
//    public void voucherManage_system_12() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] phones = {"13654973499", marketing.getPhone()};
//            //获取已过期的卡券列表
//            user.loginApplet(appletUser);
//            List<Long> voucherList = util.getAppletVoucherList(EnumAppletVoucherStatus.NEAR_EXPIRED.getName());
//            String voucherName = util.getAppletVoucherName(voucherList.get(0));
//            //转移
//            user.login(administrator);
//            Arrays.stream(phones).forEach(phone -> {
//                IScene scene = Transfer.builder().transferPhone(marketing.getPhone()).receivePhone(phone)
//                        .voucherIds(getList(voucherList.get(0))).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                String err = phone.equals(marketing.getPhone()) ? "转移账号和接收账号不能相同" : "卡券接收人未注册小程序";
//                CommonUtil.checkResult("转移卡券", voucherName, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--卡券转移，选择要转移的卡券，卡券刚好过期，确认，提示：优惠券【XXXX】已被使用或已过期，请重新选择！");
//        }
//    }
//
//    @Test(description = "卡券表单--卡券增发,正常情况")
//    public void voucherManage_system_13() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Integer[] integers = {1, 100, 100000000};
//            Arrays.stream(integers).forEach(count -> {
//                long id = util.getVoucherId(EnumVP.ONE.getVoucherName());
//                jc.invokeApi(AddVoucher.builder().id(id).addNumber(count).build());
//                IScene scene = ApplyPage.builder().name(EnumVP.ONE.getVoucherName()).build();
//                JSONObject object = jc.invokeApi(scene).getJSONArray("list").getJSONObject(0);
//                String statusName = object.getString("status_name");
//                String applyTypeName = object.getString("apply_type_name");
//                CommonUtil.checkResult("增发数量", count + "审核列表状态", EnumApplyStatusName.AUDITING.getName(), statusName);
//                CommonUtil.checkResult("增发数量", count + "审核列表申请类型", EnumApplyTypeName.FIRST_PUBLISH.getName(), applyTypeName);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--卡券增发,正常情况");
//        }
//    }
//
//    //100000001可以增发
//    @Test(description = "卡券表单--卡券增发,异常情况", enabled = false)
//    public void voucherManage_system_14() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Integer[] integers = {null, 100000001};
//            Arrays.stream(integers).forEach(count -> {
//                long id = util.getVoucherId(EnumVP.ONE.getVoucherName());
//                jc.invokeApi(AddVoucher.builder().id(id).addNumber(count).build(), false);
//                IScene scene = ApplyPage.builder().name(EnumVP.ONE.getVoucherName()).build();
//                JSONObject object = jc.invokeApi(scene).getJSONArray("list").getJSONObject(0);
//                String statusName = object.getString("status_name");
//                String applyTypeName = object.getString("apply_type_name");
//                CommonUtil.checkResult("增发数量", count + "审核列表状态", EnumApplyStatusName.AUDITING.getName(), statusName);
//                CommonUtil.checkResult("增发数量", count + "审核列表申请类型", EnumApplyTypeName.FIRST_PUBLISH.getName(), applyTypeName);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--卡券增发,正常情况");
//        }
//    }
//
//    //作废卡券还可增发
//    @Test(description = "卡券表单--卡券增发,作废卡券增发", enabled = false)
//    public void voucherManage_system_15() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long id = util.getObsoleteVoucherId();
//            String message = jc.invokeApi(AddVoucher.builder().id(id).addNumber(1).build(), false).getString("message");
//            String err = "";
//            CommonUtil.checkResult("增发卡券", "过期卡券", err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--卡券增发,作废卡券增发");
//        }
//    }
//
//    @Test(description = "卡券表单--选择卡券列表不显示已作废卡券")
//    public void voucherManage_system_16() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Long voucherId = util.getObsoleteVoucherId();
//            JSONArray array = jc.pcVoucherList().getJSONArray("list");
//            List<Long> voucherLit = array.stream().map(e -> (JSONObject) e)
//                    .filter(e -> e.getLong("voucher_id").equals(voucherId)).map(e -> e.getLong("voucher_id")).collect(Collectors.toList());
//            CommonUtil.valueView(voucherLit);
//            Preconditions.checkArgument(voucherLit.size() == 0, "选择卡券列表能看见已作废的卡券");
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("卡券表单--卡券增发,作废卡券增发");
//        }
//    }
//
//    @Test(description = "核销人员--卡券核销时将核销人状态关闭，提示核销失败")
//    public void voucherManage_system_29() {
//        logger.logCaseStart(caseResult.getCaseName());
//        String code = null;
//        try {
//            code = util.getVerificationCode(false, "本司员工");
//            user.loginApplet(appletUser);
//            AppletVoucherList voucherInfo = util.getAppletCanUsedVoucherInfoList().get(0);
//            long id = voucherInfo.getId();
//            IScene scene = VoucherVerification.builder().id(String.valueOf(id)).verificationCode(code).build();
//            String message = jc.invokeApi(scene, false).getString("message");
//            String err = "核销码错误";
//            CommonUtil.checkResult("核销卡券时核销码状态", "关闭", err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            user.login(administrator);
//            util.switchVerificationStatus(code, true);
//            saveData("核销人员--卡券核销时将核销人状态关闭，提示核销失败");
//        }
//    }
//
//    @Test(description = "核销人员--创建异页核销,名称异常")
//    public void voucherManage_system_30() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] strings = {null, EnumContent.B.getContent()};
//            Arrays.stream(strings).forEach(name -> {
//                IScene scene = CreateVerificationPeople.builder().verificationPersonName(name)
//                        .verificationPersonPhone("13663366788").status(1).type(1).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = StringUtils.isEmpty(name) ? "核销人员名字不能为空" : "核销人员名字必须为1～20个字";
//                Preconditions.checkArgument(message.equals(err),
//                        "核销人员名字为：" + name + CommonUtil.checkResult(err, message));
//                CommonUtil.logger(name);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("核销人员--创建异页核销,名称异常");
//        }
//    }
//
//    @Test(description = "核销人员--创建财务核销,名称异常")
//    public void voucherManage_system_37() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] strings = {null, EnumContent.B.getContent()};
//            Arrays.stream(strings).forEach(name -> {
//                IScene scene = CreateVerificationPeople.builder().verificationPersonName(name)
//                        .verificationPersonPhone("13663366788").status(1).type(1).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = StringUtils.isEmpty(name) ? "核销人员名字不能为空" : "核销人员名字必须为1～20个字";
//                Preconditions.checkArgument(message.equals(err), "核销人员名字为：" + name + "创建成功");
//                CommonUtil.logger(name);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("核销人员--创建财务核销,名称异常");
//        }
//    }
//
//    @Test(description = "核销人员--创建财务核销,电话异常")
//    public void voucherManage_system_38() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] strings = {null, "", "11111111111", "1337316680", "133731668062"};
//            Arrays.stream(strings).forEach(phone -> {
//                IScene scene = CreateVerificationPeople.builder().verificationPersonName("郭丽雅")
//                        .verificationPersonPhone(phone).status(1).type(0).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = "手机号格式不正确";
//                Preconditions.checkArgument(message.equals(err),
//                        "手机号格式为：" + phone + CommonUtil.checkResult(err, message));
//                CommonUtil.logger(phone);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("核销人员--创建财务核销,电话异常");
//        }
//    }
//
//    @Test(description = "核销人员--创建财务核销,电话存在")
//    public void voucherManage_system_39() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] strings = {util.getRepetitionVerificationPhone()};
//            Arrays.stream(strings).forEach(phone -> {
//                IScene scene = CreateVerificationPeople.builder().verificationPersonName("郭丽雅")
//                        .verificationPersonPhone(phone).status(1).type(0).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = "手机号已存在";
//                Preconditions.checkArgument(message.equals(err),
//                        "手机号格式为：" + phone + CommonUtil.checkResult(err, message));
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("核销人员--创建财务核销,电话存在");
//        }
//    }
//
//    @Test(description = "套餐表单--创建套餐，套餐名称异常")
//    public void packageManager_system_1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] strings = {EnumContent.B.getContent(), "1", null, ""};
//            Arrays.stream(strings).forEach(name -> {
//                IScene scene = CreatePackage.builder().packageName(name).validity("30").packageDescription(util.getDesc())
//                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectId(util.getSubjectType()))
//                        .voucherList(util.getVoucherInfo()).packagePrice(5000.00).status(true).shopIds(util.getShopIdList()).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = StringUtils.isEmpty(name) ? "套餐名称不能为空" : "套餐名称输入应大于2字小于20字";
//                Preconditions.checkArgument(message.equals(err),
//                        "套餐名称为：" + name + CommonUtil.checkResult(err, message));
//                CommonUtil.logger(name);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--创建套餐，套餐名称异常");
//        }
//    }
//
//    @Test(description = "套餐表单--创建套餐，套餐说明异常")
//    public void packageManager_system_2() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] strings = {null, "", EnumContent.C.getContent()};
//            Arrays.stream(strings).forEach(desc -> {
//                IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30").packageDescription(desc)
//                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectId(util.getSubjectType()))
//                        .voucherList(util.getVoucherInfo()).packagePrice(5000.00).status(true).shopIds(util.getShopIdList()).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = StringUtils.isEmpty(desc) ? "套餐说明不能为空" : "套餐说明不能超过200字";
//                Preconditions.checkArgument(message.equals(err),
//                        "套餐说明为：" + desc + CommonUtil.checkResult(err, message));
//                CommonUtil.logger(desc);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--创建套餐，套餐说明异常");
//        }
//    }
//
//    @Test(description = "套餐表单--创建套餐，有效天数异常")
//    public void packageManager_system_3() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] strings = {null, "", "2001", "0"};
//            Arrays.stream(strings).forEach(validity -> {
//                IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity(validity)
//                        .packageDescription(util.getDesc()).subjectType(util.getSubjectType())
//                        .subjectId(util.getSubjectId(util.getSubjectType())).voucherList(util.getVoucherInfo())
//                        .packagePrice(5000.00).status(true).shopIds(util.getShopIdList()).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = StringUtils.isEmpty(validity) ? "套餐有效期不能为空" : "有效期请小于2000天";
//                Preconditions.checkArgument(message.equals(err),
//                        "有效期为：" + validity + CommonUtil.checkResult(err, message));
//                CommonUtil.logger(validity);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--创建套餐，套餐说明异常");
//        }
//    }
//
//    @Test(description = "套餐表单--创建套餐，主体类型异常")
//    public void packageManager_system_4() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] strings = {"全部权限", null, ""};
//            Arrays.stream(strings).forEach(subjectType -> {
//                IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
//                        .packageDescription(util.getDesc()).subjectType(subjectType)
//                        .subjectId(util.getSubjectId(subjectType)).voucherList(util.getVoucherInfo())
//                        .packagePrice(5000.00).status(true).shopIds(util.getShopIdList()).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = "主体类型不存在";
//                CommonUtil.checkResult("有效期", subjectType, err, message);
//                CommonUtil.logger(subjectType);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--创建套餐，主体类型异常");
//        }
//    }
//
//    @Test(description = "套餐表单--创建套餐，主体详情异常")
//    public void packageManager_system_5() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
//                    .packageDescription(util.getDesc()).subjectType(EnumSubject.STORE.name())
//                    .voucherList(util.getVoucherInfo())
//                    .packagePrice(5000.00).status(true).shopIds(util.getShopIdList()).build();
//            String message = jc.invokeApi(scene, false).getString("message");
//            CommonUtil.valueView(message);
//            String err = "主体详情不能为空";
//            Preconditions.checkArgument(message.equals(err),
//                    "主体详情为：" + null + CommonUtil.checkResult(err, message));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--创建套餐，主体详情异常");
//        }
//    }
//
//    @Test(description = "套餐表单--创建套餐，包含卡券为空")
//    public void packageManager_system_6() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
//                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType())
//                    .subjectId(util.getSubjectId(util.getSubjectType()))
//                    .packagePrice(5000.00).status(true).shopIds(util.getShopIdList()).build();
//            String message = jc.invokeApi(scene, false).getString("message");
//            CommonUtil.valueView(message);
//            String err = "所选卡券不能为空";
//            Preconditions.checkArgument(message.equals(err),
//                    "包含卡券为：" + null + CommonUtil.checkResult(err, message));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--创建套餐，包含卡券为空");
//        }
//    }
//
//    @Test(description = "套餐表单--创建套餐，套餐价格异常")
//    public void packageManager_system_7() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Double[] doubles = {null, 100000000.01};
//            Arrays.stream(doubles).forEach(packagePrice -> {
//                IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
//                        .packageDescription(util.getDesc()).subjectType(util.getSubjectType())
//                        .subjectId(util.getSubjectId(util.getSubjectType())).voucherList(util.getVoucherInfo())
//                        .packagePrice(packagePrice).status(true).shopIds(util.getShopIdList()).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                CommonUtil.valueView(message);
//                String err = StringUtils.isEmpty(packagePrice) ? "套餐价格不能为空" : "套餐价格不能大于100,000,000";
//                Preconditions.checkArgument(message.equals(err),
//                        "有效期为：" + packagePrice + CommonUtil.checkResult(err, message));
//                CommonUtil.logger(packagePrice);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--创建套餐，套餐价格异常");
//        }
//    }
//
//    @Test(description = "套餐表单--创建套餐，选择门店为空")
//    public void packageManager_system_8() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
//                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType())
//                    .subjectId(util.getSubjectId(util.getSubjectType())).voucherList(util.getVoucherInfo())
//                    .packagePrice(5000.00).build();
//            String message = jc.invokeApi(scene, false).getString("message");
//            CommonUtil.valueView(message);
//            String err = "套餐适用门店列表不能为空";
//            Preconditions.checkArgument(message.equals(err),
//                    "选择门店为：" + null + CommonUtil.checkResult(err, message));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--创建套餐，选择门店为空");
//        }
//    }
//
//    @Test(description = "套餐表单--【创建套餐】选择卡券后将卡券作废，点击确定，提示：优惠券【xxxx】已被作废，请重新选择！")
//    public void packageManager_system_29() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long voucherId = util.getObsoleteVoucherId();
//            String voucherName = util.getVoucherName(voucherId);
//            JSONArray voucherList = util.getVoucherInfo(voucherName, 10);
//            IScene scene = CreatePackage.builder().packageName(util.createPackageName()).validity("30")
//                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType())
//                    .subjectId(util.getSubjectId(util.getSubjectType())).voucherList(voucherList)
//                    .packagePrice(5000.00).shopIds(util.getShopIdList()).build();
//            String message = jc.invokeApi(scene, false).getString("message");
//            CommonUtil.valueView(message);
//            String err = "优惠券【" + voucherName + "】已被作废，请重新选择！";
//            CommonUtil.checkResult("创建套餐时所含卡券", "已作废卡券", err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--【创建套餐】选择卡券后将卡券作废，点击确定，提示：优惠券【xxxx】已被作废，请重新选择！");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐，联系方式异常")
//    public void packageManager_system_9() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] phones = {null, "", "11111111111", "1532152798", "13654973499", "010-8888888"};
//            JSONArray voucherList = util.getVoucherInfo(1);
//            Arrays.stream(phones).forEach(phone -> {
//                IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackage.builder().customerPhone(phone)
//                        .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone())).voucherList(voucherList)
//                        .expiryDate("1").remark(EnumContent.B.getContent()).subjectType(util.getSubjectType())
//                        .subjectId(util.getSubjectId(util.getSubjectType())).extendedInsuranceYear("1")
//                        .extendedInsuranceCopies("1").type(1).build();
//                String message = jc.invokeApi(purchaseTemporaryPackageScene, false).getString("message");
//                String err = StringUtils.isEmpty(phone) ? "客户手机号不能为空" : "客户不存在";
//                CommonUtil.checkResult("联系方式", phone, err, message);
//                CommonUtil.logger(phone);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐，联系方式异常");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐，车牌号为空")
//    public void packageManager_system_10() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] plateNumbers = {null, "", "京A444", "岗A88776"};
//            JSONArray voucherList = util.getVoucherInfo(1);
//            Arrays.stream(plateNumbers).forEach(plateNumber -> {
//                IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackage.builder().customerPhone(marketing.getPhone())
//                        .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(plateNumber).voucherList(voucherList)
//                        .expiryDate("1").remark(EnumContent.B.getContent()).subjectType(util.getSubjectType())
//                        .subjectId(util.getSubjectId(util.getSubjectType())).extendedInsuranceYear("1")
//                        .extendedInsuranceCopies("1").type(1).build();
//                String message = jc.invokeApi(purchaseTemporaryPackageScene, false).getString("message");
//                String err = StringUtils.isEmpty(plateNumber) ? "车牌号不可为空" : plateNumber.length() == 7 || plateNumber.length() == 8 ?
//                        "车牌号格式不正确" : "请输入7-8位车牌号码位数";
//                CommonUtil.checkResult("车牌号", plateNumber, err, message);
//                CommonUtil.logger(plateNumber);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐，联系方式异常");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐，超过10张卡券")
//    public void packageManager_system_11() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            List<JSONArray> voucherLists = new ArrayList<>();
//            JSONArray array = util.getVoucherInfo(1);
//            array.getJSONObject(0).put("voucher_count", 100);
//            JSONArray array1 = util.getVoucherInfo(2);
//            array1.getJSONObject(0).put("voucher_count", 5);
//            array1.getJSONObject(1).put("voucher_count", 6);
//            voucherLists.add(array);
//            voucherLists.add(null);
//            voucherLists.add(array1);
//            voucherLists.forEach(voucherList -> {
//                IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackage.builder().customerPhone(marketing.getPhone())
//                        .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone())).voucherList(voucherList)
//                        .expiryDate("1").remark(EnumContent.B.getContent()).subjectType(util.getSubjectType())
//                        .subjectId(util.getSubjectId(util.getSubjectType())).extendedInsuranceYear("1")
//                        .extendedInsuranceCopies("1").type(1).build();
//                String message = jc.invokeApi(purchaseTemporaryPackageScene, false).getString("message");
//                String err = voucherList == null ? "卡券列表不能为空" : "卡券数量不能超过10张";
//                CommonUtil.checkResult("卡券数量", voucherList, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐，超过10张卡券");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐，累计金额异常", enabled = false)
//    public void packageManager_system_12() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String subjectType = util.getSubjectType();
//            long packageId = util.getPackageId("凯迪拉克无限套餐");
//            //购买固定套餐
//            String[] packagePrices = {"0", "100000001"};
//            Arrays.stream(packagePrices).forEach(packagePrice -> {
//                IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
//                        .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone()))
//                        .packageId(packageId).packagePrice(packagePrice).expiryDate("1").remark(EnumContent.B.getContent())
//                        .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear(10)
//                        .extendedInsuranceCopies(10).type(1).build();
//                String message = jc.invokeApi(purchaseFixedPackageScene, false).getString("message");
//                String err = "卡券数量不能超过10张";
//                CommonUtil.checkResult("累计金额", packagePrice, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐，累计金额异常");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐，有效日期异常")
//    public void packageManager_system_13() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String subjectType = util.getSubjectType();
//            long packageId = util.getPackageId("凯迪拉克无限套餐");
//            //购买固定套餐
//            String[] expiryDates = {null, "", "20001", "12.23"};
//            Arrays.stream(expiryDates).forEach(expiryDate -> {
//                IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
//                        .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone()))
//                        .packageId(packageId).packagePrice("1.11").expiryDate(expiryDate).remark(EnumContent.B.getContent())
//                        .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear(10)
//                        .extendedInsuranceCopies(10).type(1).build();
//                String message = jc.invokeApi(purchaseFixedPackageScene, false).getString("message");
//                String err = StringUtils.isEmpty(expiryDate) ? "有效期不能为空" : expiryDate.contains(".") ? "请求入参类型不正确" : "有效期不能查过2000天";
//                CommonUtil.checkResult("有效日期", expiryDate, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐，有效日期异常");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐，套餐说明异常")
//    public void packageManager_system_14() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String subjectType = util.getSubjectType();
//            long packageId = util.getPackageId("凯迪拉克无限套餐");
//            //购买固定套餐
//            String[] remarks = {EnumContent.C.getContent()};
//            Arrays.stream(remarks).forEach(remark -> {
//                IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
//                        .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone()))
//                        .packageId(packageId).packagePrice("1.11").expiryDate("10").remark(remark)
//                        .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear(10)
//                        .extendedInsuranceCopies(10).type(1).build();
//                String message = jc.invokeApi(purchaseFixedPackageScene, false).getString("message");
//                String err = "备注不能超过200字";
//                CommonUtil.checkResult("套餐说明", remark, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐，套餐说明异常");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐，主体类型异常")
//    public void packageManager_system_15() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long packageId = util.getPackageId("凯迪拉克无限套餐");
//            //购买固定套餐
//            String[] subjectTypes = {"全部权限", null, ""};
//            Arrays.stream(subjectTypes).forEach(subjectType -> {
//                IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
//                        .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone()))
//                        .packageId(packageId).packagePrice("1.11").expiryDate("10").remark(EnumContent.B.getContent())
//                        .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear(10)
//                        .extendedInsuranceCopies(10).type(1).build();
//                String message = jc.invokeApi(purchaseFixedPackageScene, false).getString("message");
//                String err = "主体类型不存在";
//                CommonUtil.checkResult("主体类型", subjectType, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐，主体类型异常");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐，主体详情异常", enabled = false)
//    public void packageManager_system_16() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String subjectType = util.getSubjectType();
//            long packageId = util.getPackageId("凯迪拉克无限套餐");
//            //购买固定套餐
//            IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
//                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone()))
//                    .packageId(packageId).packagePrice("1.11").expiryDate("10").remark(EnumContent.B.getContent())
//                    .subjectType(subjectType).extendedInsuranceYear(10)
//                    .extendedInsuranceCopies(10).type(1).build();
//            String message = jc.invokeApi(purchaseFixedPackageScene, false).getString("message");
//            String err = "主体详情不能为空";
//            CommonUtil.checkResult("主体类型", null, err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐，主体详情异常");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐，选择套餐异常", enabled = false)
//    public void packageManager_system_17() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String subjectType = util.getSubjectType();
//            Long[] packageIds = {null, 1L};
//            //购买固定套餐
//            Arrays.stream(packageIds).forEach(packageId -> {
//                //购买固定套餐
//                IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
//                        .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone()))
//                        .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
//                        .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear(10)
//                        .extendedInsuranceCopies(10).type(1).build();
//                String message = jc.invokeApi(purchaseFixedPackageScene, false).getString("message");
//                String err = packageId == null ? "套餐列表不能为空" : "";
//                CommonUtil.checkResult("选择套餐", packageId, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买套餐，主体详情异常");
//        }
//    }
//
//    @Test(description = "套餐表单--购买/赠送临时套餐时，选择的卡券无库存，确认时会有提示：优惠券【XXXX】库存不足")
//    public void packageManager_system_18() {
//        try {
//            Long id = util.getNoInventoryVoucherId();
//            String voucherName = util.getVoucherName(id);
//            JSONArray voucherList = util.getVoucherInfo(voucherName, 2);
//            String subjectType = util.getSubjectType();
//            //后买临时套餐
//            IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackage.builder().customerPhone(marketing.getPhone())
//                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone()))
//                    .voucherList(voucherList).expiryDate("1").remark(EnumContent.B.getContent()).subjectType(subjectType)
//                    .subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear("1")
//                    .extendedInsuranceCopies("1").type(1).build();
//            String message = jc.invokeApi(purchaseTemporaryPackageScene, false).getString("message");
//            String err = "优惠券【" + voucherName + "】库存不足";
//            CommonUtil.checkResult("选择的卡券", "无库存卡券", err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买/赠送临时套餐时，选择的卡券无库存，确认时会有提示：优惠券【XXXX】库存不足");
//        }
//    }
//
//    @Test(description = "套餐表单--购买/赠送套餐未确认前，选择的卡券被作废，确认时会有提示：优惠券【XXX】已被作废，请重新选择！")
//    public void packageManager_system_19() {
//        try {
//            Long id = util.getObsoleteVoucherId();
//            String voucherName = util.getVoucherName(id);
//            JSONArray voucherList = util.getOneVoucherInfo(voucherName);
//            String subjectType = util.getSubjectType();
//            //购买临时套餐
//            IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackage.builder().customerPhone(marketing.getPhone())
//                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone()))
//                    .voucherList(voucherList).expiryDate("1").remark(EnumContent.B.getContent()).subjectType(subjectType)
//                    .subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear("1")
//                    .extendedInsuranceCopies("1").type(1).build();
//            String message = jc.invokeApi(purchaseTemporaryPackageScene, false).getString("message");
//            String err = "优惠券【" + voucherName + "】已被作废，请重新选择！";
//            CommonUtil.checkResult("选择的卡券", "已作废卡券", err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买/赠送套餐未确认前，选择的卡券被作废，确认时会有提示：卡券已被拒绝或者已取消，请重新选择！");
//        }
//    }
//
//    @Test(description = "套餐表单--购买/赠送固定套餐时，选择的套餐内包含无库存卡券，确认时会有提示：优惠券【XXX】已被作废，请重新选择！")
//    public void packageManager_system_20() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String subjectType = util.getSubjectType();
//            long packageId = util.getPackageId(EnumVP.TWO.getPackageName());
//            //购买固定套餐
//            IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
//                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone()))
//                    .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
//                    .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).extendedInsuranceYear(10)
//                    .extendedInsuranceCopies(10).type(1).build();
//            String message = jc.invokeApi(purchaseFixedPackageScene, false).getString("message");
//            String err = "优惠券【" + util.getVoucherName(util.getPackageContainVoucher(packageId).get(0)) + "】库存不足";
//            CommonUtil.checkResult("选择的套餐", "包含无库存卡券", err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买/赠送固定套餐时，选择的套餐内包含无库存卡券，确认时会有提示：优惠券【XXXX】库存不足");
//        }
//    }
//
//    //关闭套餐也可购买
//    @Test(description = "套餐表单--购买/赠送固定套餐时，将套餐关闭，确认时会有提示：此套餐已关闭", enabled = false)
//    public void packageManager_system_21() {
//        logger.logCaseStart(caseResult.getCaseName());
//        long packageId = 0;
//        try {
//            packageId = util.getPackageId(EnumVP.ONE.getPackageName());
//            //关闭套餐
//            jc.pcSwitchPackageStatus(false, packageId);
//            //购买固定套餐
//            IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
//                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone()))
//                    .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
//                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectId(util.getSubjectType())).extendedInsuranceYear(10)
//                    .extendedInsuranceCopies(10).type(1).build();
//            String message = jc.invokeApi(purchaseFixedPackageScene, false).getString("message");
//            String err = "此套餐已关闭";
//            Preconditions.checkArgument(message.contains(err), "购买的套餐内包含无库存卡券" + CommonUtil.checkResult(err, message));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            jc.pcSwitchPackageStatus(true, packageId);
//            saveData("套餐表单--购买/赠送固定套餐时，将套餐关闭，确认时会有提示：此套餐已关闭");
//        }
//    }
//
//    //没校验套餐过期
//    @Test(description = "套餐表单--购买/赠送固定套餐时，套餐过期，确认时会有提示：此套餐已过期", enabled = false)
//    public void packageManager_system_30() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //过期套餐
//            long packageId = util.getPackageId(EnumVP.THREE.getPackageName());
//            //购买固定套餐
//            IScene purchaseFixedPackageScene = PurchaseFixedPackage.builder().customerPhone(marketing.getPhone())
//                    .carType(EnumCarType.RECEPTION_CAR.name()).plateNumber(util.getPlatNumber(marketing.getPhone()))
//                    .packageId(packageId).packagePrice("1.00").expiryDate("1").remark(EnumContent.B.getContent())
//                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectId(util.getSubjectType())).extendedInsuranceYear(10)
//                    .extendedInsuranceCopies(10).type(1).build();
//            jc.invokeApi(purchaseFixedPackageScene, false).getString("message");
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--购买/赠送固定套餐时，套餐过期，确认时会有提示：此套餐已过期");
//        }
//    }
//
//    @Test(description = "套餐表单--修改套餐，套餐名称异常")
//    public void packageManager_system_22() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] packageNames = {EnumVP.ONE.getPackageName(), EnumContent.A.getContent(), null, ""};
//            long packageId = util.getModifyPackageId();
//            Arrays.stream(packageNames).forEach(packageName -> {
//                IScene scene = EditPackage.builder().packageName(packageName).packageDescription(EnumContent.B.getContent())
//                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectId(util.getSubjectType()))
//                        .voucherList(util.getVoucherInfo()).packagePrice("1.11").status(true).shopIds(util.getShopIdList())
//                        .id(String.valueOf(packageId)).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                String err = StringUtils.isEmpty(packageName) ? "套餐名称不能为空" : EnumVP.isContains(packageName) ? "套餐名称重复，请重新输入！" : "套餐名称输入应大于2字小于20字";
//                CommonUtil.checkResult("套餐名称", packageName, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--修改套餐，套餐名称异常");
//        }
//    }
//
//    @Test(description = "套餐表单--修改套餐， 套餐说明异常")
//    public void packageManager_system_23() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] contents = {EnumContent.C.getContent(), null, ""};
//            long packageId = util.getModifyPackageId();
//            String packageName = util.getPackageName(packageId);
//            Arrays.stream(contents).forEach(content -> {
//                IScene scene = EditPackage.builder().packageName(packageName).packageDescription(content).validity(2000)
//                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectId(util.getSubjectType()))
//                        .voucherList(util.getVoucherInfo()).packagePrice("1.11").status(true).shopIds(util.getShopIdList())
//                        .id(String.valueOf(packageId)).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                String err = StringUtils.isEmpty(content) ? "套餐说明不能为空" : "套餐说明不能超过200字";
//                CommonUtil.checkResult("套餐说明", content, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--修改套餐，名称异常");
//        }
//    }
//
//    @Test(description = "套餐表单--修改套餐，主体类型异常")
//    public void packageManager_system_24() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] subjectTypes = {"全部权限", null, ""};
//            long packageId = util.getModifyPackageId();
//            String packageName = util.getPackageName(packageId);
//            Arrays.stream(subjectTypes).forEach(subjectType -> {
//                IScene scene = EditPackage.builder().packageName(packageName).packageDescription(EnumContent.B.getContent())
//                        .subjectType(subjectType).subjectId(util.getSubjectId(subjectType)).validity(2000)
//                        .voucherList(util.getVoucherInfo()).packagePrice("1.11").status(true).shopIds(util.getShopIdList())
//                        .id(String.valueOf(packageId)).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                String err = StringUtils.isEmpty(subjectType) ? "主体类型不存在" : "主体类型不存在";
//                CommonUtil.checkResult("主体类型", subjectType, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--修改套餐，主体类型异常");
//        }
//    }
//
//    @Test(description = "套餐表单--修改套餐，主体详情异常")
//    public void packageManager_system_25() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long packageId = util.getModifyPackageId();
//            String packageName = util.getPackageName(packageId);
//            IScene scene = EditPackage.builder().packageName(packageName).packageDescription(EnumContent.B.getContent())
//                    .subjectType(EnumSubject.STORE.name()).validity(2000).voucherList(util.getVoucherInfo()).packagePrice("1.11")
//                    .status(true).shopIds(util.getShopIdList()).id(String.valueOf(packageId)).build();
//            String message = jc.invokeApi(scene, false).getString("message");
//            String err = "主体详情不能为空";
//            CommonUtil.checkResult("主体详情", null, err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--修改套餐，主体类型异常");
//        }
//    }
//
//    @Test(description = "套餐表单--修改套餐，包含卡券异常")
//    public void packageManager_system_26() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long packageId = util.getModifyPackageId();
//            String packageName = util.getPackageName(packageId);
//            List<JSONArray> voucherList = new ArrayList<>();
//            voucherList.add(util.getVoucherInfo(11));
//            voucherList.add(null);
//            voucherList.forEach(voucher -> {
//                IScene scene = EditPackage.builder().packageName(packageName).packageDescription(EnumContent.B.getContent())
//                        .subjectType(util.getSubjectType()).validity(2000).packagePrice("1.11").voucherList(voucher)
//                        .status(true).shopIds(util.getShopIdList()).id(String.valueOf(packageId)).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                String err = StringUtils.isEmpty(voucher) ? "所选卡券不能为空" : "卡券数量不能超过10张";
//                CommonUtil.checkResult("所选卡券", voucher, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--修改套餐，主体类型异常");
//        }
//    }
//
//    @Test(description = "套餐表单--修改套餐，套餐价格异常")
//    public void packageManager_system_27() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long packageId = util.getModifyPackageId();
//            String packageName = util.getPackageName(packageId);
//            String[] prices = {"100000000.01", "10000000001", null, ""};
//            Arrays.stream(prices).forEach(price -> {
//                IScene scene = EditPackage.builder().packageName(packageName).packageDescription(EnumContent.B.getContent())
//                        .subjectType(util.getSubjectType()).validity(2000).packagePrice(price).voucherList(util.getVoucherInfo())
//                        .status(true).shopIds(util.getShopIdList()).id(String.valueOf(packageId)).build();
//                String message = jc.invokeApi(scene, false).getString("message");
//                String err = StringUtils.isEmpty(price) ? "套餐价格不能为空" : "套餐价格不能大于100,000,000";
//                CommonUtil.checkResult("套餐价格", price, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--修改套餐，套餐价格异常");
//        }
//    }
//
//    @Test(description = "套餐表单--修改套餐，所选门店为空")
//    public void packageManager_system_28() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long packageId = util.getModifyPackageId();
//            String packageName = util.getPackageName(packageId);
//            IScene scene = EditPackage.builder().packageName(packageName).packageDescription(EnumContent.B.getContent())
//                    .subjectType(util.getSubjectType()).validity(2000).packagePrice("1.11").voucherList(util.getVoucherInfo())
//                    .status(true).id(String.valueOf(packageId)).build();
//            String message = jc.invokeApi(scene, false).getString("message");
//            String err = "套餐适用门店列表不能为空";
//            CommonUtil.checkResult("套餐适用门店", null, err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("套餐表单--修改套餐，所选门店为空");
//        }
//    }
//
//    @Test(description = "套餐表单--购买套餐，确认购买前，套餐状态改为关闭，再确认购买小程序会收到套餐/优惠券")
//    public void packageManager_system_31() {
//        logger.logCaseStart(caseResult.getCaseName());
//        Long packageId = null;
//        try {
//            user.loginApplet(appletUser);
//            int num = util.getAppletPackageNum();
//            user.login(administrator);
//            packageId = util.getPackageId(EnumVP.ONE.getPackageName());
//            String packageName = util.getPackageName(packageId);
//            //购买套餐
//            util.buyFixedPackage(1);
//            //将套餐关闭
//            jc.pcSwitchPackageStatus(false, packageId);
//            //确认购买
//            util.makeSureBuyPackage(packageName);
//            user.loginApplet(appletUser);
//            int newNum = util.getAppletPackageNum();
//            CommonUtil.valueView(num, newNum);
//            Preconditions.checkArgument(newNum == num + 1, "确认购买前小程序套餐数量" + num + "确认购买后小程序套餐数量" + newNum);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            user.login(administrator);
//            jc.pcSwitchPackageStatus(true, packageId);
//            saveData("套餐表单--购买套餐，确认购买前，套餐状态改为关闭，再确认购买小程序会收到套餐/优惠券");
//        }
//    }
//
//    @Test(description = "消息管理--推送消息，消息名称异常", enabled = false)
//    public void messageManager_system_1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] titles = {null, "", EnumContent.A.getContent()};
//            long packageId = util.getPackageId(EnumVP.ONE.getPackageName());
//            Arrays.stream(titles).forEach(title -> {
//                PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                        .telList(getList(marketing.getPhone())).messageName(title).messageContent(EnumContent.C.getContent())
//                        .type(1).voucherOrPackageList(getList(packageId)).useDays(10);
//                String message = jc.invokeApi(builder.build(), false).getString("message");
//                String err = StringUtils.isEmpty(title) ? "消息名称不能为空" : "消息名称不能超过20个字";
//                CommonUtil.checkResult("消息名称", title, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--推送消息，消息名称异常");
//        }
//    }
//
//    @Test(description = "消息管理--推送消息，消息内容异常", enabled = false)
//    public void messageManager_system_2() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String[] contents = {null, "", EnumContent.C.getContent()};
//            long packageId = util.getPackageId(EnumVP.ONE.getPackageName());
//            Arrays.stream(contents).forEach(content -> {
//                PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                        .telList(getList(marketing.getPhone())).messageName(EnumContent.D.getContent()).messageContent(content)
//                        .type(1).voucherOrPackageList(getList(packageId)).useDays(10);
//                String message = jc.invokeApi(builder.build(), false).getString("message");
//                String err = StringUtils.isEmpty(content) ? "消息内容不能为空" : "消息内容不能超过200个字";
//                CommonUtil.checkResult("消息内容", content, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--推送消息，消息名称异常");
//        }
//    }
//
//    @Test(description = "消息管理--推送消息，选择卡券但不选择具体卡券/套餐", enabled = false)
//    public void messageManager_system_3() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            int[] types = {0, 1};
//            Arrays.stream(types).forEach(type -> {
//                PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                        .telList(getList(marketing.getPhone())).messageName(EnumContent.D.getContent()).messageContent(EnumContent.A.getContent())
//                        .type(type).useDays(10);
//                String message = jc.invokeApi(builder.build(), false).getString("message");
//                String err = "系统错误,请联系技术支持";
//                CommonUtil.checkResult("卡券列表", null, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--推送消息，选择卡券但不选择具体卡券");
//        }
//    }
//
//    @Test(description = "消息管理--推送消息，使用时间不填", enabled = false)
//    public void messageManager_system_4() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long packageId = util.getPackageId(EnumVP.ONE.getPackageName());
//            int[] types = {0, 1};
//            Arrays.stream(types).forEach(type -> {
//                PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                        .telList(getList(marketing.getPhone())).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
//                        .type(0).voucherOrPackageList(getList(packageId));
//                String message = jc.invokeApi(builder.build(), false).getString("message");
//                String err = "系统错误,请联系技术支持";
//                CommonUtil.checkResult("卡券列表", null, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--推送消息，使用时间不填");
//        }
//    }
//
//    @Test(description = "消息管理--推送消息，发送时间不填", enabled = false)
//    public void messageManager_system_5() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long packageId = util.getPackageId(EnumVP.ONE.getPackageName());
//            int[] types = {0, 1};
//            Arrays.stream(types).forEach(type -> {
//                PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                        .telList(getList(marketing.getPhone())).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
//                        .type(0).voucherOrPackageList(getList(packageId)).useDays(10).ifSendImmediately(null);
//                String message = jc.invokeApi(builder.build(), false).getString("message");
//                String err = "系统错误,请联系技术支持";
//                CommonUtil.checkResult("卡券列表", null, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--推送消息，发送时间不填");
//        }
//    }
//
//    @Test(description = "消息管理--推送消息，推送目标异常")
//    public void messageManager_system_6() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long packageId = util.getPackageId(EnumVP.ONE.getPackageName());
//            String[] phones = {"133456", "133333333333", null, ""};
//            List<List<String>> lists = Arrays.stream(phones).map(this::getList).collect(Collectors.toList());
//            int[] types = {0, 1};
//            Arrays.stream(types).forEach(type -> lists.forEach(list -> {
//                PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                        .telList(list).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
//                        .type(type).voucherOrPackageList(getList(packageId)).useDays(10);
//                String message = jc.invokeApi(builder.build(), false).getString("message");
//                String err = "输入手机号【" + list.get(0) + "】格式不正确";
//                CommonUtil.checkResult("手机号", list.get(0), err, message);
//            }));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--推送消息，推送目标异常");
//        }
//    }
//
//    @Test(description = "消息管理--推送消息，发送时间不填", enabled = false)
//    public void messageManager_system_7() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            long packageId = util.getPackageId(EnumVP.ONE.getPackageName());
//            int[] types = {0, 1};
//            Arrays.stream(types).forEach(type -> {
//                PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                        .telList(getList(marketing.getPhone())).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
//                        .type(0).voucherOrPackageList(getList(packageId)).useDays(10).ifSendImmediately(null);
//                String message = jc.invokeApi(builder.build(), false).getString("message");
//                String err = "系统错误,请联系技术支持";
//                CommonUtil.checkResult("卡券列表", null, err, message);
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--推送消息，发送时间不填");
//        }
//    }
//
//    @Test(description = "消息管理--选择卡券时，卡券被作废，提交时提示：优惠券【XXXXX】已作废, 请重新选择！")
//    public void messageManager_system_10() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //发消息
//            long voucherId = util.getObsoleteVoucherId();
//            PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                    .telList(getList(marketing.getPhone())).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
//                    .type(0).voucherOrPackageList(getList(voucherId)).useDays(10).ifSendImmediately(true);
//            String message = jc.invokeApi(builder.build(), false).getString("message");
//            String err = "优惠券【" + util.getVoucherName(voucherId) + "】已作废, 请重新选择！";
//            CommonUtil.checkResult("发送卡券", "已作废卡券", err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--选择卡券时，卡券被作废，提交时提示：优惠券【XXXXX】已作废, 请重新选择！");
//        }
//    }
//
//    @Test(description = "消息管理--选择卡券时，卡券无库存，提交时提示：优惠券【XXXXX】库存不足, 请重新选择！")
//    public void messageManager_system_11() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //发消息
//            long voucherId = util.getNoInventoryVoucherId();
//            CommonUtil.valueView(util.getVoucherName(voucherId));
//            PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                    .telList(getList(marketing.getPhone())).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
//                    .type(0).voucherOrPackageList(getList(voucherId)).useDays(10).ifSendImmediately(true);
//            String message = jc.invokeApi(builder.build(), false).getString("message");
//            String err = "优惠券【" + util.getVoucherName(voucherId) + "】库存不足";
//            CommonUtil.checkResult("发送卡券", "库存不足卡券", err, message);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--选择卡券时，卡券无库存，提交时提示：优惠券【XXXXX】库存不足, 请重新选择！");
//        }
//    }
//
//    @Test(description = "消息管理--选择套餐时，套餐被关闭，提交时提示：套餐不允许发送，请重新选择")
//    public void messageManager_system_12() {
//        logger.logCaseStart(caseResult.getCaseName());
//        long packageId = 0;
//        try {
//            //发消息
//            packageId = util.getPackageId(EnumVP.ONE.getPackageName());
//            jc.pcSwitchPackageStatus(false, packageId);
//            PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                    .telList(getList(marketing.getPhone())).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
//                    .type(1).voucherOrPackageList(getList(packageId)).useDays(10).ifSendImmediately(true);
//            String message = jc.invokeApi(builder.build(), false).getString("message");
//            String err = "套餐不允许发送，请重新选择";
//            Preconditions.checkArgument(message.equals(err), "选择套餐时，套餐被关闭" + CommonUtil.checkResult(err, message));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            jc.pcSwitchPackageStatus(true, packageId);
//            saveData("消息管理--选择套餐时，套餐被关闭，提交时提示：套餐不允许发送，请重新选择");
//        }
//    }
//
//    @Test(description = "消息管理--选择套餐时，套餐内包含无库存卡券，提交时提示：套餐不允许发送，请重新选择")
//    public void messageManager_system_13() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //发消息
//            long packageId = util.getPackageId(EnumVP.TWO.getPackageName());
//            PushMessage.PushMessageBuilder builder = PushMessage.builder().pushTarget(EnumPushTarget.PERSONNEL_CUSTOMER.name())
//                    .telList(getList(marketing.getPhone())).messageName(EnumContent.D.getContent()).messageContent(EnumContent.C.getContent())
//                    .type(1).voucherOrPackageList(getList(packageId)).useDays(10).ifSendImmediately(true);
//            String message = jc.invokeApi(builder.build(), false).getString("message");
//            String err = "套餐【" + util.getPackageName(packageId) + "】中卡券【" + util.getVoucherName(util.getPackageContainVoucher(packageId).get(0)) + "】库存不足";
//            Preconditions.checkArgument(message.equals(err), "选择套餐时，套餐内包含无库存卡券" + CommonUtil.checkResult(err, message));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("消息管理--选择套餐时，套餐内包含无库存卡券，提交时提示：套餐不允许发送，请重新选择");
//        }
//    }
}


