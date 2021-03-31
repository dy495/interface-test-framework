package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletVoucher;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.CustomerLabelTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.financial.ApplyTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletMessageDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletMessageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletVoucherVerificationScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage.BuyPackageRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionVoucherListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record.PushMsgPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.*;
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

public class VoucherManagerCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.INS_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.INS_ZT_DAILY;
    private static final EnumAppletToken APPLET_USER_TWO = EnumAppletToken.JC_XMF_DAILY;
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
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
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
    @Test(description = "卡券管理--创建卡券--列表数+1&卡券状态=待审批；【卡券审批】列表数+1&审批状态=审批中&申请类型=首发")
    public void voucherManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(VoucherTypeEnum.values()).forEach(anEnum -> {
                int voucherTotal = visitor.invokeApi(VoucherFormVoucherPageScene.builder().build()).getInteger("total");
                int applyTotal = visitor.invokeApi(ApplyPageScene.builder().build()).getInteger("total");
                //创建卡券
                String voucherName = util.createVoucher(1, anEnum);
                IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build();
                visitor.invokeApi(voucherPageScene);
                IScene voucherDetailScene = VoucherDetailScene.builder().id(util.getVoucherId(voucherName)).build();
                VoucherDetail voucherDetail = JSONObject.toJavaObject(visitor.invokeApi(voucherDetailScene), VoucherDetail.class);
                CommonUtil.checkResult(voucherName + "描述", util.getDesc(), voucherDetail.getVoucherDescription());
                CommonUtil.checkResult(voucherName + " 主体类型", util.getSubjectType(), voucherDetail.getSubjectType());
                CommonUtil.checkResult(voucherName + " 主体类型id", util.getSubjectDesc(util.getSubjectType()), voucherDetail.getSubjectId());
                CommonUtil.checkResult(voucherName + " 类型", anEnum.name(), voucherDetail.getCardType());
                CommonUtil.checkResult(voucherName + " 类型名称", anEnum.getDesc(), voucherDetail.getCardTypeName());
                CommonUtil.checkResult(voucherName + " 成本价格", "0.01", voucherDetail.getCost());
                if (anEnum.getDesc().equals(VoucherTypeEnum.COMMODITY_EXCHANGE.getDesc())) {
                    CommonUtil.checkResult(voucherName + " 可兑换商品", "兑换布加迪威龙一辆", voucherDetail.getExchangeCommodityName());
                } else if (anEnum.getDesc().equals(VoucherTypeEnum.COUPON.getDesc())) {
                    CommonUtil.checkResult(voucherName + " 门槛价格", 999.99, voucherDetail.getThresholdPrice());
                    CommonUtil.checkResult(voucherName + " 折扣", 2.50, voucherDetail.getDiscount());
                    CommonUtil.checkResult(voucherName + " 最多优惠", 99.99, voucherDetail.getMostDiscount());
                } else if (anEnum.getDesc().equals(VoucherTypeEnum.FULL_DISCOUNT.getDesc())) {
                    CommonUtil.checkResult(voucherName + " 面值", 49.99, voucherDetail.getParValue());
                    CommonUtil.checkResult(voucherName + " 门槛价格", 999.99, voucherDetail.getThresholdPrice());
                }
                //卡券列表+1
                int newVoucherTotal = visitor.invokeApi(VoucherFormVoucherPageScene.builder().build()).getInteger("total");
                CommonUtil.checkResult("卡券列表数", voucherTotal + 1, newVoucherTotal);
                //卡券状态=待审批
                VoucherPage voucherPage = util.getVoucherPage(voucherName);
                CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.WAITING.getName(), voucherPage.getVoucherStatusName());
                CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.WAITING.name(), voucherPage.getVoucherStatus());
                //卡券变更记录不+1
                Long voucherId = util.getVoucherId(voucherName);
                IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
                CommonUtil.checkResult(voucherName + " 变更记录数", 0, visitor.invokeApi(changeRecordScene).getInteger("total"));
                //审批列表+1
                int newApplyTotal = visitor.invokeApi(ApplyPageScene.builder().build()).getInteger("total");
                CommonUtil.checkResult(voucherName + " 审批列表数", applyTotal + 1, newApplyTotal);
                //审批状态=审批中
                ApplyPage applyPage = util.getAuditingApplyPage(voucherName);
                CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
                CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.VOUCHER.getName(), applyPage.getApplyTypeName());
                CommonUtil.checkResult(voucherName + " 审批成本单价/元", "0.01", applyPage.getPrice());
                CommonUtil.checkResult(voucherName + " 审批发出数量", 1, applyPage.getNum());
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--创建自定义卡券--列表数+1&卡券状态=待审批；【卡券审批】列表数+1&审批状态=审批中&申请类型=首发");
        }
    }

    //3.1
    @Test(description = "卡券管理--卡券的剩余库存>=可用库存", enabled = false)
    public void voucherManage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = VoucherFormVoucherPageScene.builder().size(100).build().invoke(visitor, true);
            List<VoucherPage> voucherPageList = response.getJSONArray("list").stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, VoucherPage.class)).collect(Collectors.toList());
            voucherPageList.forEach(e -> {
                Long surplusInventory = e.getSurplusInventory();
                Integer allowUseInventory = e.getAllowUseInventory();
                Preconditions.checkArgument(surplusInventory >= allowUseInventory, e.getVoucherName() + "剩余库存为：" + surplusInventory + " 可用库存为：" + allowUseInventory);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--卡券的剩余库存>=可用库存");
        }
    }

    //ok
    @Test(description = "卡券管理--编辑待审批卡券--卡券信息更新")
    public void voucherManage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审批的卡券id
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            String newVoucherName = util.createVoucherName();
            List<Long> shopIds = util.getShopIdList(2);
            //编辑
            IScene scene = EditVoucherScene.builder().id(Math.toIntExact(voucherId)).voucherName(newVoucherName).voucherDescription(EnumDesc.DESC_BETWEEN_40_50.getDesc()).shopIds(shopIds).shopType(1).selfVerification(false).build();
            visitor.invokeApi(scene);
            IScene voucherDetailScene = VoucherDetailScene.builder().id(voucherId).build();
            VoucherDetail voucherDetail = JSONObject.toJavaObject(visitor.invokeApi(voucherDetailScene), VoucherDetail.class);
            //卡券变更记录+1
            int newChangeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            CommonUtil.checkResult(newVoucherName + " 变更记列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //变更事项为编辑卡券
            String changeItem = visitor.invokeApi(changeRecordScene).getJSONArray("list").getJSONObject(0).getString("change_item");
            Preconditions.checkArgument(changeItem.contains(ChangeItemEnum.EDIT.getName()), "变更记录变更事项：" + changeItem);
            //卡券列表
            CommonUtil.checkResult(newVoucherName + " 名称", newVoucherName, voucherDetail.getVoucherName());
            CommonUtil.checkResult(newVoucherName + " 详情", EnumDesc.DESC_BETWEEN_40_50.getDesc(), voucherDetail.getVoucherDescription());
            CommonUtil.checkResult(newVoucherName + " 适用门店", shopIds, voucherDetail.getShopIds().stream().map(e -> (JSONObject) e).map(e -> e.getLong("shop_id")).collect(Collectors.toList()));
            CommonUtil.checkResult(newVoucherName + " 业务类型", ShopTypeEnum.DIFF.getId(), voucherDetail.getShopType());
            CommonUtil.checkResult(newVoucherName + " 是否自助核销", false, voucherDetail.getSelfVerification());
            //审批列表
            ApplyPage applyPage = util.getAuditingApplyPage(newVoucherName);
            CommonUtil.checkResult(newVoucherName + " 审批列表名称", newVoucherName, applyPage.getName());
            CommonUtil.checkResult(newVoucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--编辑待审批卡券--卡券信息更新");
        }
    }

    //ok
    @Test(description = "卡券管理--撤回卡券--卡券状态=已撤回&此卡券在审批列表状态=已取消")
    public void voucherManage_data_4() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            //撤回
            IScene recallVoucherScene = RecallVoucherScene.builder().id(voucherId).build();
            visitor.invokeApi(recallVoucherScene);
            //校验
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.RECALL.getName(), voucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.RECALL.name(), voucherPage.getVoucherStatus());
            //审批列表
            ApplyPage applyPage = util.getApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.CANCEL.getName(), applyPage.getStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--撤回卡券--卡券状态=已撤回&此卡券在审批列表状态=已取消");
        }
    }

    //ok
    @Test(description = "卡券管理--删除已撤回的卡券--此券记录消失")
    public void voucherManage_data_5() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.RECALL).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //删除
            visitor.invokeApi(DeleteVoucherScene.builder().id(voucherId).build());
            //校验
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build();
            JSONArray list = visitor.invokeApi(voucherPageScene).getJSONArray("list");
            CommonUtil.checkResult(voucherName + " 结果列表", 0, list.size());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--删除已撤回的卡券--此券记录消失");
        }
    }

    //ok
    @Test(description = "卡券管理--删除审批未通过的卡券--此券记录消失")
    public void voucherManage_data_6() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.REJECT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
//            IScene recordPageScene = RecordPageScene.builder().build();
//            int total = visitor.invokeApi(recordPageScene).getInteger("total");
            //删除
            visitor.invokeApi(DeleteVoucherScene.builder().id(voucherId).build());
            //校验
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build();
            JSONArray list = visitor.invokeApi(voucherPageScene).getJSONArray("list");
            int count = (int) list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_name").equals(voucherName)).count();
            CommonUtil.checkResult(voucherName + " 结果列表", 0, count);
//            //删除记录
//            List<JSONObject> recordPageList = util.collectBean(recordPageScene, JSONObject.class);
//            String dataTypeName = recordPageList.stream().map(e -> e.getString("data_type_name")).findFirst().orElse(null);
//            CommonUtil.checkResult("删除记录页面", "优惠券", dataTypeName);
//            CommonUtil.checkResult("删除记录列表", total + 1, recordPageList.size());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--删除审批未通的过卡券--此券记录消失");
        }
    }

    //ok
    @Test(description = "卡券管理--进行中的卡券暂停发放--卡券状态=暂停发放")
    public void voucherManage_data_7() {
        try {
            Long voucherId = new VoucherGenerator.Builder().voucherStatus(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId();
            //暂停发放
            IScene changeProvideStatusScene = ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build();
            visitor.invokeApi(changeProvideStatusScene);
            //校验
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP.getName(), voucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP.name(), voucherPage.getVoucherStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--进行中的卡券暂停发放--卡券状态=暂停发放");
        }
    }

    //ok
    @Test(description = "卡券管理--进行中的卡券作废--状态&变更记录校验")
    public void voucherManage_data_8() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            IScene scene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(scene).getInteger("total");
            //作废卡券
            visitor.invokeApi(InvalidVoucherScene.builder().id(voucherId).build());
            //校验卡券状态
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.getName(), voucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.name(), voucherPage.getVoucherStatus());
            //校验变更记录列表数
            JSONObject response = visitor.invokeApi(scene);
            int newChangeRecordTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            VoucherChangeRecord voucherChangeRecord = util.collectBean(scene, VoucherChangeRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.INVALIDED.getName(), voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--进行中的卡券作废--状态&变更记录校验");
        }
    }

    //ok
    @Test(description = "卡券管理--进行中的卡券增发")
    public void voucherManage_data_9() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = visitor.invokeApi(additionalRecordScene).getInteger("total");
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            //增发卡券
            visitor.invokeApi(AddVoucherScene.builder().id(voucherId).addNumber(10).build());
            //卡券审批列表卡券状态=增发
            ApplyPage applyPage = util.getAuditingApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            JSONObject response = visitor.invokeApi(additionalRecordScene);
            int newAddTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, newAddTotal);
            //增发记录状态
            String statusName = response.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AUDITING.getName(), statusName);
            //审批通过卡券剩余库存+10
            util.applyVoucher(voucherName, "1");
            Long newSurplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory + 10, newSurplusInventory);
            //变更记录变更事项
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, visitor.invokeApi(changeRecordScene).getInteger("total"));
            //校验变更记录变更事项
            VoucherChangeRecord voucherChangeRecord = util.collectBean(changeRecordScene, VoucherChangeRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.ADD.getName() + 10 + "张", voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
            //增发记录状态=已通过
            JSONObject newResponse = visitor.invokeApi(additionalRecordScene).getJSONArray("list").getJSONObject(0);
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AGREE.getName(), newResponse.getString("status_name"));
            CommonUtil.checkResult(voucherName + " 增发记录申请增发数量", 10, newResponse.getInteger("additional_num"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--进行中的卡券增发");
        }
    }

    //ok
    @Test(description = "卡券管理--停止发放的卡券开始发放--卡券状态=进行中")
    public void voucherManage_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //停止发放
            ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build().invoke(visitor, true);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.STOP.getName(), voucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.STOP.name(), voucherPage.getVoucherStatus());
            //开始发放
            ChangeProvideStatusScene.builder().id(voucherId).isStart(true).build().invoke(visitor, true);
            //校验状态
            VoucherPage newVoucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.WORKING.getName(), newVoucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.WORKING.name(), newVoucherPage.getVoucherStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--停止发放的卡券开始发放--卡券状态=进行中");
        }
    }

    //ok
    @Test(description = "卡券管理--进行中的卡券增发")
    public void voucherManage_data_23() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = visitor.invokeApi(additionalRecordScene).getInteger("total");
            //增发卡券
            visitor.invokeApi(AddVoucherScene.builder().id(voucherId).addNumber(10).build());
            //卡券审批列表卡券状态=增发
            ApplyPage applyPage = util.getAuditingApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            JSONObject response = visitor.invokeApi(additionalRecordScene);
            int newAddTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, newAddTotal);
            //增发记录状态
            String statusName = response.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AUDITING.getName(), statusName);
            //审批拒绝卡券剩余库存+0
            util.applyVoucher(voucherName, "2");
            Long newSurplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory, newSurplusInventory);
            //增发记录状态=已拒绝
            JSONObject newResponse = visitor.invokeApi(additionalRecordScene).getJSONArray("list").getJSONObject(0);
            CommonUtil.checkResult(voucherName + " 增发记录状态", "审核未通过", newResponse.getString("status_name"));
            CommonUtil.checkResult(voucherName + " 增发记录申请增发数量", 10, newResponse.getInteger("additional_num"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--进行中的卡券增发");
        }
    }

    //ok
    @Test(description = "卡券管理--停止发放的卡券作废--状态=已作废&变更记录+1变更事项=作废卡券")
    public void voucherManage_data_11() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.STOP).buildVoucher().getVoucherId();
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = changeRecordScene.invoke(visitor, true).getInteger("total");
            String voucherName = util.getVoucherName(voucherId);
            //作废卡券
            InvalidVoucherScene.builder().id(voucherId).build().invoke(visitor, true);
            //校验卡券状态
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.name(), voucherPage.getVoucherStatus());
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.getName(), voucherPage.getVoucherStatusName());
            //校验变更记录列表数
            int newChangeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            VoucherChangeRecord voucherChangeRecord = util.collectBean(changeRecordScene, VoucherChangeRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.INVALIDED.getName(), voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--停止发放的卡券作废--状态=已作废&变更记录+1变更事项=作废卡券");
        }
    }

    //ok
    @Test(description = "卡券管理--停止发放的卡券增发")
    public void voucherManage_data_12() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.STOP).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = visitor.invokeApi(additionalRecordScene).getInteger("total");
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            //增发卡券
            visitor.invokeApi(AddVoucherScene.builder().id(voucherId).addNumber(10).build());
            //卡券审批列表卡券状态=增发
            ApplyPage applyPage = util.getAuditingApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            JSONObject response = visitor.invokeApi(additionalRecordScene);
            int newAddTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, newAddTotal);
            //增发记录状态
            String statusName = response.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AUDITING.getName(), statusName);
            //审批通过卡券剩余库存+10
            util.applyVoucher(voucherName, "1");
            Long newSurplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory + 10, newSurplusInventory);
            //变更记录+1
            JSONObject changeResponse = visitor.invokeApi(changeRecordScene);
            int newChangeRecordTotal = changeResponse.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            VoucherChangeRecord voucherChangeRecord = util.collectBean(changeRecordScene, VoucherChangeRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.ADD.getName() + "10张", voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
            //增发记录状体=已通过
            JSONObject newResponse = visitor.invokeApi(additionalRecordScene);
            String newStatusName = newResponse.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AGREE.getName(), newStatusName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--停止发放的卡券增发");
        }
    }

    //ok
    @Test(description = "卡券管理--已售罄的卡券暂停发放--卡券状态=暂停发放")
    public void voucherManage_data_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().voucherStatus(VoucherStatusEnum.SELL_OUT).visitor(visitor).buildVoucher().getVoucherId();
            //暂停发放
            IScene changeProvideStatusScene = ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build();
            visitor.invokeApi(changeProvideStatusScene);
            //校验
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP.name(), voucherPage.getVoucherStatus());
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP.getName(), voucherPage.getVoucherStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--已售罄的卡券暂停发放--卡券状态=暂停发放");
        }
    }

    //ok
    @Test(description = "卡券管理--已售罄的卡券作废--状态=已作废&变更记录+1变更事项=作废卡券")
    public void voucherManage_data_14() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            //作废卡券
            visitor.invokeApi(InvalidVoucherScene.builder().id(voucherId).build());
            //校验卡券状态
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.name(), voucherPage.getVoucherStatus());
            CommonUtil.checkResult(voucherName + " 状态名称", VoucherStatusEnum.INVALIDED.getName(), voucherPage.getVoucherStatusName());
            //变更记录+1
            JSONObject changeResponse = visitor.invokeApi(changeRecordScene);
            int newChangeRecordTotal = changeResponse.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            VoucherChangeRecord voucherChangeRecord = util.collectBean(changeRecordScene, VoucherChangeRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.INVALIDED.getName(), voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--已售罄的卡券作废--状态=已作废&变更记录+1变更事项=作废卡券");
        }
    }

    //ok
    @Test(description = "卡券管理--已售罄的卡券增发--审批通过状态")
    public void voucherManage_data_15() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = visitor.invokeApi(additionalRecordScene).getInteger("total");
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            //增发卡券
            visitor.invokeApi(AddVoucherScene.builder().id(voucherId).addNumber(1).build());
            //卡券审批列表卡券状态=增发
            ApplyPage applyPage = util.getAuditingApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            JSONObject response = visitor.invokeApi(additionalRecordScene);
            int newAddTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, newAddTotal);
            //增发记录状态
            String statusName = response.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AUDITING.getName(), statusName);
            //审批通过卡券剩余库存+10
            util.applyVoucher(voucherName, "1");
            Long newSurplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory + 1, newSurplusInventory);
            //变更记录变更事项
            JSONObject changeResponse = visitor.invokeApi(changeRecordScene);
            int newChangeRecordTotal = changeResponse.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            List<VoucherChangeRecord> voucherChangeRecords = util.collectBean(changeRecordScene, VoucherChangeRecord.class);
            VoucherChangeRecord voucherChangeRecord = voucherChangeRecords.get(0);
            String changeItem = voucherChangeRecord.getChangeItem();
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.ADD.getName() + "1张", changeItem);
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
            //增发记录状体=已通过
            JSONObject newResponse = visitor.invokeApi(additionalRecordScene);
            String newStatusName = newResponse.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AGREE.getName(), newStatusName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--已售罄的卡券增发--审批通过状态");
        }
    }

    //ok
    @Test(description = "卡券管理--增发卡券--审批通过前，剩余库存存不变")
    public void voucherManage_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            //增发
            IScene addVoucherScene = AddVoucherScene.builder().id(voucherId).addNumber(10).build();
            visitor.invokeApi(addVoucherScene);
            //增发后数据
            CommonUtil.checkResult(voucherName + " 增发审批通过前剩余库存数量", surplusInventory, util.getVoucherPage(voucherName).getSurplusInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--增发卡券--审批通过前，剩余库存存不变");
        }
    }

    //ok
    @Test(description = "卡券管理--增发卡券--审批不通过，新剩余库存=原剩余库存")
    public void voucherManage_data_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            Long surplusInventory = voucherPage.getSurplusInventory();
            //增发
            IScene addVoucherScene = AddVoucherScene.builder().id(voucherId).addNumber(10).build();
            visitor.invokeApi(addVoucherScene);
            //审批拒绝
            util.applyVoucher(voucherName, "2");
            //审批后数据
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            AdditionalRecord additionalRecord = util.collectBean(additionalRecordScene, AdditionalRecord.class).get(0);
            CommonUtil.checkResult(voucherName + "增发记录列表状态", AdditionalRecordStatusEnum.REFUSAL.getName(), additionalRecord.getStatusName());
            CommonUtil.checkResult(voucherName + " 增发审批拒绝后剩余库存数量", surplusInventory, util.getVoucherPage(voucherName).getSurplusInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--增发卡券--审批不通过，新剩余库存=原剩余库存");
        }
    }

    //ok
    @Test(description = "卡券管理--状态为已售罄时，剩余库存=0")
    public void voucherManage_data_18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormVoucherPageScene.builder().voucherStatus(VoucherStatusEnum.SELL_OUT.name()).build();
            List<VoucherPage> voucherPageList = util.collectBean(scene, VoucherPage.class);
            voucherPageList.forEach(e -> CommonUtil.checkResult(e.getVoucherName() + " 剩余库存", 0L, e.getSurplusInventory()));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--状态为已售罄时，剩余库存=0");
        }
    }

    //ok
    @Test(description = "卡券管理--购买一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1")
    public void voucherManage_data_19() {
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
            //购买临时套餐
            user.loginPc(ALL_AUTHORITY);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            util.buyTemporaryPackage(voucherList, 1);
            util.makeSureBuyPackage("临时套餐");
            //购买后数据
            CommonUtil.checkResult("套餐购买记录", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherName).get(0);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum + 1, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--购买一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1");
        }
    }

    //ok
    @Test(description = "卡券管理--购买一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1")
    public void voucherManage_data_20() {
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
            int soldNumber = util.getPackagePage(packageName).getSoldNumber();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            user.loginPc(ALL_AUTHORITY);
            //购买固定套餐
            util.buyFixedPackage(packageId, 1);
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
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 购买后剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum + 1, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--购买一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1");
        }
    }

    //ok
    @Test(description = "卡券管理--赠送一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1")
    public void voucherManage_data_21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //购买前数据
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = visitor.invokeApi(buyPackageRecordScene).getInteger("total");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            //临时套餐
            user.loginPc(ALL_AUTHORITY);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            util.buyTemporaryPackage(voucherList, 0);
            util.makeSureBuyPackage("临时套餐");
            //赠送后数据
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherName).get(0);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum + 1, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--赠送一个临时套餐，套餐内卡券剩余库存-1&套餐购买记录+1");
        }
    }

    //ok
    @Test(description = "卡券管理--赠送一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1")
    public void voucherManage_data_22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            String packageName = util.editPackage(voucherList);
            Long packageId = util.getPackageId(packageName);
            //赠送前数据
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene buyPackageRecordScene = BuyPackageRecordScene.builder().build();
            int buyPackageRecordTotal = visitor.invokeApi(buyPackageRecordScene).getInteger("total");
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            user.loginPc(ALL_AUTHORITY);
            //赠送固定套餐
            util.buyFixedPackage(packageId, 0);
            //确认支付
            util.makeSureBuyPackage(packageName);
            //赠送后数据
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            VoucherSendRecord voucherSendRecord = util.getVoucherSendRecordList(voucherName).get(0);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.name(), voucherSendRecord.getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NEAR_EXPIRE.getName(), voucherSendRecord.getVoucherUseStatusName());
            CommonUtil.checkResult(voucherName + " 领取人电话", APPLET_USER_ONE.getPhone(), voucherSendRecord.getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecord.getSendChannelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.VIP.getTypeName()), "领取人标签：" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签：" + voucherSendRecord.getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum + 1, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--赠送一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1");
        }
    }

    //ok
    @Test(description = "卡券管理--作废某人的卡券，小程序上我的卡券-1&作废记录+1")
    public void voucherManage_data_24() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            util.pushMessage(0, true, voucherId);
            //作废前数据
            user.loginApplet(APPLET_USER_ONE);
            int voucherCherNum = util.getAppletVoucherNum();
            user.loginPc(ALL_AUTHORITY);
            List<VoucherInvalidPage> voucherInvalidPages = util.getVoucherInvalidList(voucherId);
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalInvalid = visitor.invokeApi(voucherInfoScene).getInteger("total_invalid");
            //作废
            List<VoucherSendRecord> voucherSendRecords = util.getVoucherSendRecordList(voucherId);
            Long recordId = voucherSendRecords.get(0).getId();
            String voucherCode = voucherSendRecords.get(0).getVoucherCode();
            IScene scene = InvalidCustomerVoucherScene.builder().id(recordId).invalidReason(EnumDesc.DESC_BETWEEN_10_15.getDesc()).build();
            visitor.invokeApi(scene);
            //作废后数据
            List<VoucherInvalidPage> newVoucherInvalidPages = util.getVoucherInvalidList(voucherId);
            CommonUtil.checkResult(voucherName + " 作废后作废记录列表数", voucherInvalidPages.size() + 1, newVoucherInvalidPages.size());
            CommonUtil.checkResult(voucherName + " 作废后领取人电话", APPLET_USER_ONE.getPhone(), newVoucherInvalidPages.get(0).getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 作废后作废人姓名", ALL_AUTHORITY.getName(), newVoucherInvalidPages.get(0).getInvalidName());
            CommonUtil.checkResult(voucherName + " 作废后作废人电话", ALL_AUTHORITY.getPhone(), newVoucherInvalidPages.get(0).getInvalidPhone());
            CommonUtil.checkResult(voucherName + " 作废后作废说明", EnumDesc.DESC_BETWEEN_10_15.getDesc(), newVoucherInvalidPages.get(0).getInvalidDescription());
            CommonUtil.checkResult(voucherCode + " 作废后共作废数", totalInvalid + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_invalid"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult(voucherName + " 作废后小程序我的卡券数量", voucherCherNum, util.getAppletVoucherNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--作废某人的卡券，小程序上我的卡券-1&作废记录+1");
        }
    }

    //ok
    @Test(description = "卡券管理--卡券共领取数=已领取数")
    public void voucherManage_data_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherPage> voucherPages = util.collectBean(scene, VoucherPage.class);
            voucherPages.forEach(e -> {
                String voucherName = e.getVoucherName();
                Long totalSend = visitor.invokeApi(VoucherInfoScene.builder().id(e.getVoucherId()).build()).getLong("total_send");
                Long cumulativeDelivery = util.getVoucherPage(voucherName).getCumulativeDelivery();
                CommonUtil.checkResultPlus(voucherName + " 累计发出", cumulativeDelivery, "共领取数", totalSend);
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--卡券共领取数=已领取数");
        }
    }

    //ok
    @Test(description = "卡券管理--卡券已核销数=核销记录列表数")
    public void voucherManage_data_26() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherPage> voucherPages = util.collectBean(scene, VoucherPage.class);
            voucherPages.forEach(e -> {
                String voucherName = e.getVoucherName();
                Long totalVerify = visitor.invokeApi(VoucherInfoScene.builder().id(e.getVoucherId()).build()).getLong("total_verify");
                Long total = visitor.invokeApi(VerificationRecordScene.builder().voucherId(e.getVoucherId()).build()).getLong("total");
                CommonUtil.checkResultPlus(voucherName + " 共核销数", totalVerify, "核销记录列表数", total);
                IScene sendRecordScene = SendRecordScene.builder().voucherId(e.getVoucherId()).useStatus(VoucherUseStatusEnum.IS_USED.name()).build();
                long usedTotal = visitor.invokeApi(sendRecordScene).getLong("total");
                CommonUtil.checkResultPlus(voucherName + " 共核销数", totalVerify, "领取记录已使用数", usedTotal);
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--卡券已核销数=核销记录列表数");
        }
    }

    //ok
    @Test(description = "卡券管理--卡券共作废数=作废记录列表数")
    public void voucherManage_data_27() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherPage> voucherPages = util.collectBean(scene, VoucherPage.class);
            voucherPages.forEach(e -> {
                String voucherName = e.getVoucherName();
                Long totalInvalid = visitor.invokeApi(VoucherInfoScene.builder().id(e.getVoucherId()).build()).getLong("total_invalid");
                Long total = visitor.invokeApi(VoucherInvalidPageScene.builder().id(e.getVoucherId()).build()).getLong("total");
                CommonUtil.checkResultPlus(voucherName + " 共作废数", totalInvalid, "作废记录列表数", total);
                CommonUtil.valueView(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--卡券共作废数=作废记录列表数");
        }
    }

    //ok
    @Test(description = "卡券管理--增发记录列表数=卡券审批列表此卡全的增发审批列表数&两边深审批通过的增发数量之和相等")
    public void voucherManage_data_28() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherPage> voucherPages = util.collectBean(scene, VoucherPage.class);
            voucherPages.stream().map(VoucherPage::getVoucherId).forEach(voucherId -> {
                IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
                List<AdditionalRecord> additionalRecordList = util.collectBean(additionalRecordScene, AdditionalRecord.class);
                int additionalRecordNum = additionalRecordList.stream().map(AdditionalRecord::getAdditionalNum).mapToInt(Integer::parseInt).sum();
                String voucherName = util.getVoucherName(voucherId);
                IScene applyPageScene = ApplyPageScene.builder().name(voucherName).build();
                List<ApplyPage> applyPageList = util.collectBean(applyPageScene, ApplyPage.class).stream().filter(e -> e.getName().equals(voucherName) && e.getApplyTypeName().equals(ApplyTypeEnum.ADDITIONAL.getName())).collect(Collectors.toList());
                int applyPageSum = applyPageList.stream().mapToInt(ApplyPage::getNum).sum();
                CommonUtil.checkResultPlus(voucherName + " 增发记录列表数", additionalRecordList.size(), "卡券审批列表申请类型为增发的列表数", applyPageList.size());
                CommonUtil.checkResultPlus(voucherName + " 增发记录总增发量", additionalRecordNum, "卡券审批列表申请类型为增发的总增发量", applyPageSum);
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--增发记录列表数=卡券审批列表此卡全的增发审批列表数&两边深审批通过的增发数量之和相等");
        }
    }

    //ok
    @Test(description = "卡券管理--小程序自助核销一张，使用的核销码对应人员册核销数量+1&【核销记录】列表数+1&&核销渠道=主动核销")
    public void voucherManage_data_29() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //发出一张卡券
            util.pushMessage(0, true, voucherId);
            Thread.sleep(500);
            //获取最新发出卡券的code
            String voucherCode = util.getVoucherCode(voucherId);
            CommonUtil.valueView(voucherCode);
            //核销列表数
            IScene verificationRecordScene = VerificationRecordScene.builder().voucherId(voucherId).build();
            List<VerificationRecord> verificationRecords = util.collectBean(verificationRecordScene, VerificationRecord.class);
            //获取核销码
            String code = util.getVerificationCode("本司员工");
            IScene verificationPeopleScene = VerificationPeopleScene.builder().verificationCode(code).build();
            //核销人员核销数量
            int verificationNumber = visitor.invokeApi(verificationPeopleScene).getJSONArray("list").getJSONObject(0).getInteger("verification_number");
            //获取卡券核销id
            user.loginApplet(APPLET_USER_ONE);
            Long appletVoucherId = util.getAppletVoucherInfo(voucherCode).getId();
            //核销
            IScene voucherVerificationScene = AppletVoucherVerificationScene.builder().id(String.valueOf(appletVoucherId)).verificationCode(code).build();
            visitor.invokeApi(voucherVerificationScene);
            //核销之后数据
            user.loginPc(ALL_AUTHORITY);
            List<VerificationRecord> newVerificationRecords = util.collectBean(verificationRecordScene, VerificationRecord.class);
            CommonUtil.checkResult(voucherName + " 核销记录列表数", verificationRecords.size() + 1, newVerificationRecords.size());
            CommonUtil.checkResult(voucherCode + " 核销渠道", VerifyChannelEnum.ACTIVE.getName(), newVerificationRecords.get(0).getVerificationChannelName());
            int newVerificationNumber = visitor.invokeApi(verificationPeopleScene).getJSONArray("list").getJSONObject(0).getInteger("verification_number");
            CommonUtil.checkResult("核销码" + code + "核销数", verificationNumber + 1, newVerificationNumber);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--小程序自助核销一张，使用的核销码对应人员册核销数量+1&【核销记录】列表数+1&&核销渠道=主动核销");
        }
    }

    //ok
    @Test(description = "卡券管理--转移卡券，转移人小程序我的卡券数量-1，被转移人我的卡券数量+1")
    public void voucherManage_data_30() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已使用的卡券列表
            user.loginApplet(APPLET_USER_ONE);
            int transferVoucherNum = util.getAppletVoucherNum();
            int transferMessageNum = util.getAppletMessageNum();
            AppletVoucher appletVoucher = util.getAppletVoucher(VoucherUseStatusEnum.NEAR_EXPIRE);
            Long id = appletVoucher.getId();
            String voucherName = appletVoucher.getTitle();
            user.loginApplet(APPLET_USER_TWO);
            int receiveVoucherNum = util.getAppletVoucherNum();
            //转移
            user.loginPc(ALL_AUTHORITY);
            int messageNum = visitor.invokeApi(PushMsgPageScene.builder().build()).getInteger("total");
            IScene transferScene = TransferScene.builder().transferPhone(APPLET_USER_ONE.getPhone()).receivePhone(APPLET_USER_TWO.getPhone()).voucherIds(getList(id)).build();
            visitor.invokeApi(transferScene);
            user.loginApplet(APPLET_USER_ONE);
            int newTransferVoucherNum = util.getAppletVoucherNum();
            int newTransferMessageNum = util.getAppletMessageNum();
            CommonUtil.checkResult("转移者我的卡券数", transferVoucherNum - 1, newTransferVoucherNum);
            //我的消息数量
            CommonUtil.checkResult("转移者我的消息数", transferMessageNum + 1, newTransferMessageNum);
            //我的消息内容
            Long messageId = visitor.invokeApi(AppletMessageListScene.builder().size(20).build()).getJSONArray("list").getJSONObject(0).getLong("id");
            JSONObject response = visitor.invokeApi(AppletMessageDetailScene.builder().id(messageId).build());
            String title = response.getString("title");
            String content = response.getString("content");
            CommonUtil.checkResult("消息名称", "系统消息", title);
            CommonUtil.checkResult("消息内容", "您的卡券【" + voucherName + "】已被转移至" + APPLET_USER_TWO.getPhone() + "账号，如非本人授权，请联系轿辰会客服，对应卡券变更至对应转移的账号中；", content);
            user.loginApplet(APPLET_USER_TWO);
            int newReceiveVoucherNum = util.getAppletVoucherNum();
            CommonUtil.checkResult("接收者我的卡券数", receiveVoucherNum + 1, newReceiveVoucherNum);
            //pc消息记录+1
            user.loginPc(ALL_AUTHORITY);
            JSONObject messageResponse = visitor.invokeApi(PushMsgPageScene.builder().build());
            int newMessageNum = messageResponse.getInteger("total");
            CommonUtil.checkResult("消息记录数", messageNum + 1, newMessageNum);
            String messageContent = messageResponse.getJSONArray("list").getJSONObject(0).getString("content");
            CommonUtil.checkResult("消息内容", "您的卡券【" + voucherName + "】已被转移至" + APPLET_USER_TWO.getPhone() + "账号，如非本人授权，请联系轿辰会客服，对应卡券变更至对应转移的账号中；", messageContent);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--转移卡券，转移人小程序我的卡券数量-1，被转移人我的卡券数量+1");
        }
    }

    //ok
    @Test(description = "卡券管理--进行中的卡券增发，再撤回增发卡券，审核列表状态变为已撤回", priority = 1)
    public void voucherManage_data_31() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = additionalRecordScene.invoke(visitor, true).getInteger("total");
            //增发卡券
            AddVoucherScene.builder().id(voucherId).addNumber(10).build().invoke(visitor, true);
            String applyTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            //卡券审批列表卡券状态=增发
            ApplyPage applyPage = util.getAuditingApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            List<AdditionalRecord> additionalRecordList = util.collectBean(additionalRecordScene, AdditionalRecord.class);
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, additionalRecordList.size());
            //增发记录状态
            String statusName = additionalRecordList.get(0).getStatusName();
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AUDITING.getName(), statusName);
            //撤回卡券审批列表卡券状态=已取消
            long additionalId = additionalRecordList.get(0).getId();
            RecallAdditionalScene.builder().id(additionalId).build().invoke(visitor, true);
            String newStatusName = util.collectBean(additionalRecordScene, AdditionalRecord.class).get(0).getStatusName();
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.RECALL.getName(), newStatusName);
            ApplyPage newApplyPage = util.getApplyPageByTime(voucherName, applyTime);
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.CANCEL.getName(), newApplyPage.getStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--进行中的卡券增发，再撤回增发卡券，审核列表状态变为已撤回");
        }
    }

    //ok
    @Test(description = "卡券管理--新建卡券--卡券名称异常")
    public void voucherManage_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumDesc.DESC_BETWEEN_40_50.getDesc(), "1", null, ""};
            Arrays.stream(strings).forEach(name -> {
                IScene scene = CreateVoucherScene.builder().voucherName(name)
                        .subjectType(util.getSubjectType()).cardType(VoucherTypeEnum.CUSTOM.name()).cost(99.99).stock(1000)
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = StringUtils.isEmpty(name) ? "卡券名称不能为空" : "卡券名称长度应为2～20个字";
                CommonUtil.checkResult("卡券名称为：" + name, err, message);
                CommonUtil.logger(name);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--新建卡券--卡券名称异常");
        }
    }

    //ok
    @Test(description = "卡券管理--新建卡券--卡券说明异常")
    public void voucherManage_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumDesc.DESC_BETWEEN_500_1000.getDesc()};
            Arrays.stream(strings).forEach(desc -> {
                IScene scene = CreateVoucherScene.builder().voucherName(util.createVoucherName())
                        .subjectType(util.getSubjectType()).cardType(VoucherTypeEnum.CUSTOM.name()).cost(99.99).parValue(99.99)
                        .voucherDescription(desc).subjectId(util.getSubjectDesc(util.getSubjectType())).stock(1000)
                        .shopType(0).shopIds(util.getShopIdList(2)).selfVerification(true).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = StringUtils.isEmpty(desc) ? "卡券说明不能为空" : "卡券描述不能超过500个字";
                CommonUtil.checkResult("卡券说明为：" + desc, err, message);
                CommonUtil.logger(desc);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--新建卡券--卡券说明异常");
        }
    }

    //ok
    @Test(description = "卡券管理--新建卡券--主体类型异常")
    public void voucherManage_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {"全部权限", null, ""};
            Arrays.stream(strings).forEach(subjectType -> {
                IScene scene = CreateVoucherScene.builder().voucherName(util.createVoucherName())
                        .subjectType(subjectType).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99).cost(99.99)
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(subjectType)).stock(1000)
                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = "主体类型不存在";
                CommonUtil.checkResult("主体类型为：" + subjectType, err, message);
                CommonUtil.logger(subjectType);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--新建卡券--主体类型异常");
        }
    }

    //ok
    @Test(description = "卡券管理--新建卡券--主体详情异常")
    public void voucherManage_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CreateVoucherScene.builder().voucherName(util.createVoucherName())
                    .subjectType(UseRangeEnum.STORE.name()).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99)
                    .voucherDescription(util.getDesc()).stock(1000).cost(99.99).shopType(0)
                    .shopIds(util.getShopIdList(2)).selfVerification(true).build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "主体详情不能为空";
            CommonUtil.checkResult("主体类型为：" + null, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--新建卡券--主体详情异常");
        }
    }

    //ok
    @Test(description = "卡券管理--新建卡券--库存数量异常情况")
    public void voucherManage_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer[] integers = {1000000000, null, -100, Integer.MAX_VALUE};
            Arrays.stream(integers).forEach(stock -> {
                IScene scene = CreateVoucherScene.builder().voucherName(util.createVoucherName())
                        .subjectType(util.getSubjectType()).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99).cost(99.99)
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(util.getSubjectType())).stock(stock)
                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = stock == null ? "库存不能为空" : "卡券库存范围应在0 ～ 100000000张";
                CommonUtil.checkResult("卡券库存为：" + stock, err, message);
                CommonUtil.logger(stock);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--新建卡券--库存数量异常情况");
        }
    }

    //ok
    @Test(description = "卡券管理--新建卡券--业务类型异常情况")
    public void voucherManage_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer[] integers = {null, -1, 100};
            Arrays.stream(integers).forEach(shopType -> {
                IScene scene = CreateVoucherScene.builder().voucherName(util.createVoucherName())
                        .subjectType(util.getSubjectType()).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99).cost(99.99)
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(util.getSubjectType())).stock(1000)
                        .shopType(shopType).shopIds(util.getShopIdList()).selfVerification(true).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = shopType == null ? "业务类型不能为空" : "业务类型不存在";
                CommonUtil.checkResult("业务类型为：" + shopType, err, message);
                CommonUtil.logger(shopType);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--新建卡券--业务类型异常情况");
        }
    }

    //ok
    @Test(description = "卡券管理--新建卡券--成本异常情况")
    public void voucherManage_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Double[] doubles = {(double) -1, (double) 1000000000, 100000000.11};
            Arrays.stream(doubles).forEach(cost -> {
                IScene scene = CreateVoucherScene.builder().voucherName(util.createVoucherName())
                        .subjectType(util.getSubjectType()).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99).cost(cost)
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(util.getSubjectType())).stock(1000)
                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = cost == null ? "成本不能为空" : "优惠券成本应在0～100000000元之间";
                CommonUtil.checkResult("成本为：" + cost, err, message);
                CommonUtil.logger(cost);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--新建卡券--成本异常情况");
        }
    }

    //ok
    @Test(description = "卡券管理--新建卡券--选择门店异常")
    public void voucherManage_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CreateVoucherScene.builder().voucherName(util.createVoucherName())
                    .subjectType(util.getSubjectType()).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99).cost(99.99)
                    .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(util.getSubjectType())).stock(1000)
                    .shopType(0).selfVerification(true).build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "卡券适用门店列表不能为空";
            CommonUtil.checkResult("卡券适用门店列表不能为：" + null, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--新建卡券--成本异常情况");
        }
    }

    //ok
    @Test(description = "卡券管理--转移已使用的卡券，提示：卡券【XXXX】已被使用或已过期，请重新选择！")
    public void voucherManage_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已使用的卡券列表
            user.loginApplet(APPLET_USER_ONE);
            AppletVoucher appletVoucher = util.getAppletVoucher(VoucherUseStatusEnum.IS_USED);
            String voucherName = appletVoucher.getTitle();
            Long voucherId = appletVoucher.getId();
            //转移
            user.loginPc(ALL_AUTHORITY);
            IScene scene = TransferScene.builder().transferPhone(APPLET_USER_ONE.getPhone()).receivePhone(APPLET_USER_TWO.getPhone()).voucherIds(getList(voucherId)).build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "卡券【" + voucherName + "】已被使用或已过期，请重新选择！";
            CommonUtil.checkResult("转移卡券 " + voucherName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--转移已使用的卡券，提示：卡券【XXXX】已被使用或已过期，请重新选择！");
        }
    }

    //ok
    @Test(description = "卡券管理--转移已过期的卡券，提示：卡券【XXXX】已被使用或已过期，请重新选择！")
    public void voucherManage_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已使用的卡券列表
            user.loginApplet(APPLET_USER_ONE);
            AppletVoucher appletVoucher = util.getAppletVoucher(VoucherUseStatusEnum.EXPIRED);
            String voucherName = appletVoucher.getTitle();
            Long voucherId = appletVoucher.getId();
            //转移
            user.loginPc(ALL_AUTHORITY);
            IScene scene = TransferScene.builder().transferPhone(APPLET_USER_ONE.getPhone()).receivePhone(APPLET_USER_TWO.getPhone()).voucherIds(getList(voucherId)).build();
            String message = visitor.invokeApi(scene, false).getString("message");
            String err = "卡券【" + voucherName + "】已被使用或已过期，请重新选择！";
            CommonUtil.checkResult("转移卡券 " + voucherName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--转移已过期的卡券，提示：卡券【XXXX】已被使用或已过期，请重新选择！");
        }
    }

    //ok
    @Test(description = "卡券管理--卡券转移，转移账号异常")
    public void voucherManage_system_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] phones = {"13654973499"};
            //获取已过期的卡券列表
            user.loginApplet(APPLET_USER_ONE);
            AppletVoucher appletVoucher = util.getAppletVoucher(VoucherUseStatusEnum.NEAR_EXPIRE);
            String voucherName = appletVoucher.getTitle();
            Long voucherId = appletVoucher.getId();
            //转移
            user.loginPc(ALL_AUTHORITY);
            Arrays.stream(phones).forEach(phone -> {
                IScene scene = TransferScene.builder().transferPhone(phone).receivePhone(APPLET_USER_ONE.getPhone()).voucherIds(getList(voucherId)).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = "推送用户id不能为空";
                CommonUtil.checkResult("转移卡券" + voucherName, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--卡券转移，转移账号异常");
        }
    }

    //ok
    @Test(description = "卡券管理--卡券转移，接收账号异常")
    public void voucherManage_system_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] phones = {"13654973499", APPLET_USER_ONE.getPhone()};
            //获取已过期的卡券列表
            user.loginApplet(APPLET_USER_ONE);
            AppletVoucher appletVoucher = util.getAppletVoucher(VoucherUseStatusEnum.NEAR_EXPIRE);
            String voucherName = appletVoucher.getTitle();
            Long voucherId = appletVoucher.getId();
            //转移
            user.loginPc(ALL_AUTHORITY);
            Arrays.stream(phones).forEach(phone -> {
                IScene scene = TransferScene.builder().transferPhone(APPLET_USER_ONE.getPhone()).receivePhone(phone).voucherIds(getList(voucherId)).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = phone.equals(APPLET_USER_ONE.getPhone()) ? "转移账号和接收账号不能相同" : "卡券接收人未注册小程序";
                CommonUtil.checkResult("转移卡券" + voucherName, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--卡券转移，选择要转移的卡券，卡券刚好过期，确认，提示：卡券【XXXX】已被使用或已过期，请重新选择！");
        }
    }

    //ok
    @Test(description = "卡券管理--卡券转移列表数=小程序可用卡券数量")
    public void voucherManage_system_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene voucherListScene = VoucherListScene.builder().transferPhone(APPLET_USER_ONE.getPhone()).build();
            int voucherNum = visitor.invokeApi(voucherListScene).getJSONArray("list").size();
            user.loginApplet(APPLET_USER_ONE);
            int nearExpireNum = util.getAppletVoucherNum(VoucherUseStatusEnum.NEAR_EXPIRE);
            int normalNum = util.getAppletVoucherNum(VoucherUseStatusEnum.NORMAL);
            int a = (int) util.getAppletPackageContainVoucherList().stream().filter(e -> e.getStatusName().equals(VoucherUseStatusEnum.NEAR_EXPIRE.getName())).count();
            int b = (int) util.getAppletPackageContainVoucherList().stream().filter(e -> e.getStatusName().equals(VoucherUseStatusEnum.NORMAL.getName())).count();
            CommonUtil.valueView(nearExpireNum, normalNum, a, b);
            CommonUtil.checkResultPlus("可转移列表数", voucherNum, "小程序可用卡券数", nearExpireNum + normalNum + a + b);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--卡券转移列表数=小程序可用卡券数量");
        }
    }

    //ok
    @Test(description = "卡券管理--卡券增发,异常情况")
    public void voucherManage_system_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer[] integers = {null};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Arrays.stream(integers).forEach(count -> {
                IScene addVoucherScene = AddVoucherScene.builder().addNumber(count).id(voucherId).build();
                String message = visitor.invokeApi(addVoucherScene, false).getString("message");
                String err = "增发数量不能为空";
                CommonUtil.checkResult("增发数量" + count, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--卡券增发,异常情况");
        }
    }

    //ok
    @Test(description = "卡券表单--作废卡券无法增发")
    public void voucherManage_system_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherStatus(VoucherStatusEnum.INVALIDED.name()).build();
            List<VoucherPage> voucherPageList = util.collectBean(voucherPageScene, VoucherPage.class);
            voucherPageList.forEach(voucherPage -> {
                CommonUtil.checkResult(voucherPage.getVoucherName() + "能否增发", null, voucherPage.getIsAdditional());
                CommonUtil.logger(voucherPage.getVoucherName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--作废卡券无法增发");
        }
    }

    //ok
    @Test(description = "卡券表单--选择卡券列表不显示已作废卡券")
    public void voucherManage_system_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherStatus(VoucherStatusEnum.INVALIDED.name()).build();
            List<VoucherPage> voucherPageList = util.collectBean(voucherPageScene, VoucherPage.class);
            List<Long> invalidedVoucherList = voucherPageList.stream().map(VoucherPage::getVoucherId).collect(Collectors.toList());
            IScene receptionVoucherListScene = ReceptionVoucherListScene.builder().build();
            JSONArray array = visitor.invokeApi(receptionVoucherListScene).getJSONArray("list");
            List<Long> voucherList = array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("voucher_id")).collect(Collectors.toList());
            invalidedVoucherList.forEach(voucherId -> {
                Preconditions.checkArgument(!voucherList.contains(voucherId), "选择卡券列表能看见已作废的卡券：" + voucherId);
                CommonUtil.logger(voucherId);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--卡券增发,作废卡券增发");
        }
    }

    //ok
    @Test(description = "卡券申请--成本累计=发出数量*成本单价")
    public void voucherApply_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene applyPageScene = ApplyPageScene.builder().build();
            List<ApplyPage> applyPageList = util.collectBean(applyPageScene, ApplyPage.class);
            applyPageList.forEach(applyPage -> {
                String voucherName = applyPage.getName();
                Integer num = applyPage.getNum();
                String price = applyPage.getPrice();
                String totalPrice = applyPage.getTotalPrice();
                Preconditions.checkArgument(Double.parseDouble(totalPrice) <= Double.parseDouble(price) * num + 0.000001 || Double.parseDouble(totalPrice) >= Double.parseDouble(price) * num - 0.000001
                        , voucherName + "成本累计：" + Double.parseDouble(totalPrice) + "发出数量*成本单价：" + Double.parseDouble(price) * num);
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券申请--成本累计=发出数量*成本单价");
        }
    }

    //bug
    @Test(description = "卡券申请--发出数量（首发）=【卡券表单】发行库存数量")
    public void voucherApply_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherPage> voucherPageList = util.collectBean(voucherPageScene, VoucherPage.class);
            voucherPageList.forEach(voucherPage -> {
                IScene voucherDetailScene = VoucherDetailScene.builder().id(voucherPage.getVoucherId()).build();
                VoucherDetail voucherDetail = JSONObject.toJavaObject(visitor.invokeApi(voucherDetailScene), VoucherDetail.class);
                Integer stock = voucherDetail.getStock();
                String voucherName = voucherPage.getVoucherName();
                IScene applyPageScene = ApplyPageScene.builder().name(voucherName).build();
                List<ApplyPage> applyPageList = util.collectBean(applyPageScene, ApplyPage.class);
                ApplyPage applyPage = applyPageList.stream().filter(e -> e.getName().equals(voucherName) && e.getApplyTypeName().equals(ApplyTypeEnum.VOUCHER.getName())).findFirst().orElse(null);
                Preconditions.checkArgument(applyPage != null, voucherName + " 在审核列表为空");
                Integer num = applyPage.getNum();
                CommonUtil.checkResultPlus(voucherName + "发行库存数量", stock, "发出数量（首发）", num);
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券申请--发出数量（首发）=【卡券表单】发行库存数量");
        }
    }

    //ok
    @Test(description = "卡券申请--审批列表成本单价=【卡券表单】成本")
    public void voucherApply_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherPage> voucherPageList = util.collectBean(voucherPageScene, VoucherPage.class);
            voucherPageList.forEach(voucherPage -> {
                CommonUtil.valueView(voucherPage.getVoucherName());
                IScene voucherDetailScene = VoucherDetailScene.builder().id(voucherPage.getVoucherId()).build();
                VoucherDetail voucherDetail = JSONObject.toJavaObject(voucherDetailScene.invoke(visitor, true), VoucherDetail.class);
                String cost = voucherDetail.getCost();
                String voucherName = voucherPage.getVoucherName();
                IScene applyPageScene = ApplyPageScene.builder().name(voucherName).build();
                List<ApplyPage> applyPageList = util.collectBean(applyPageScene, ApplyPage.class);
                applyPageList.stream().filter(applyPage -> applyPage.getName().equals(voucherName)).map(ApplyPage::getPrice).forEach(price -> CommonUtil.checkResultPlus(voucherName + " 成本", cost, "审批列表成本单价", price));
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券申请--审批列表成本单价=【卡券表单】成本");
        }
    }

    //ok
    @Test(description = "核销人员--创建异页核销,列表数+1")
    public void verificationPeople_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene verificationPeopleScene = VerificationPeopleScene.builder().build();
            //查询列表数
            int total = verificationPeopleScene.invoke(visitor, true).getInteger("total");
            String phone = util.getDistinctPhone();
            CreateVerificationPeopleScene.builder().verificationPersonName("异页打工人").verificationPersonPhone(phone).type(1).status(1).build().invoke(visitor, true);
            int newTotal = verificationPeopleScene.invoke(visitor, true).getInteger("total");
            CommonUtil.checkResult("核销人员列表数", total + 1, newTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建异页核销,列表数+1");
        }
    }

    //ok
    @Test(description = "核销人员--创建财务核销,列表数+1")
    public void verificationPeople_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene verificationPeopleScene = VerificationPeopleScene.builder().build();
            //查询列表数
            int total = verificationPeopleScene.invoke(visitor, true).getInteger("total");
            String phone = util.getDistinctPhone();
            CreateVerificationPeopleScene.builder().verificationPersonName("本司打工人").verificationPersonPhone(phone).type(0).status(1).build().invoke(visitor, true);
            int newTotal = verificationPeopleScene.invoke(visitor, true).getInteger("total");
            CommonUtil.checkResult("核销人员列表数", total + 1, newTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,列表数+1");
        }
    }

    //ok
    @Test(description = "核销人员--卡券核销时将核销人状态关闭，提示核销失败")
    public void verificationPeople_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String code = null;
        try {
            code = util.getVerificationCode(false, "本司员工");
            user.loginApplet(APPLET_USER_ONE);
            long id = util.getAppletVoucher(VoucherUseStatusEnum.NEAR_EXPIRE).getId();
            String message = AppletVoucherVerificationScene.builder().id(String.valueOf(id)).verificationCode(code).build().invoke(visitor, false).getString("message");
            String err = "当前核销码已失效";
            CommonUtil.checkResult("核销卡券时核销码状态关闭", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            user.loginPc(ALL_AUTHORITY);
            util.switchVerificationStatus(code, true);
            saveData("核销人员--卡券核销时将核销人状态关闭，提示核销失败");
        }
    }

    //ok
    @Test(description = "核销人员--创建异页核销,名称异常")
    public void verificationPeople_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, EnumDesc.DESC_BETWEEN_200_300.getDesc()};
            Arrays.stream(strings).forEach(name -> {
                String message = CreateVerificationPeopleScene.builder().verificationPersonName(name).verificationPersonPhone("13663366788").status(1).type(1).build().invoke(visitor, false).getString("message");
                String err = StringUtils.isEmpty(name) ? "核销人员名字不能为空" : "核销人员名字必须为1～20个字";
                CommonUtil.checkResult("核销人员名字为 " + name, err, message);
                CommonUtil.logger(name);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建异页核销,名称异常");
        }
    }

    //ok
    @Test(description = "核销人员--创建财务核销,名称异常")
    public void verificationPeople_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, EnumDesc.DESC_BETWEEN_200_300.getDesc()};
            Arrays.stream(strings).forEach(name -> {
                String message = CreateVerificationPeopleScene.builder().verificationPersonName(name).verificationPersonPhone("13663366788").status(1).type(1).build().invoke(visitor, false).getString("message");
                String err = StringUtils.isEmpty(name) ? "核销人员名字不能为空" : "核销人员名字必须为1～20个字";
                CommonUtil.checkResult("核销人员名字为 " + name, err, message);
                CommonUtil.logger(name);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,名称异常");
        }
    }

    //ok
    @Test(description = "核销人员--创建财务核销,电话异常")
    public void verificationPeople_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, "", "11111111111", "1337316680", "133731668062"};
            Arrays.stream(strings).forEach(phone -> {
                String message = CreateVerificationPeopleScene.builder().verificationPersonName("打工人").verificationPersonPhone(phone).status(1).type(0).build().invoke(visitor, false).getString("message");
                String err = "手机号格式不正确";
                CommonUtil.checkResult("手机号格式为：" + phone, err, message);
                CommonUtil.logger(phone);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,电话异常");
        }
    }

    //ok
    @Test(description = "核销人员--创建财务核销,电话存在")
    public void verificationPeople_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String phone = util.getRepetitionVerificationPhone();
            String message = CreateVerificationPeopleScene.builder().verificationPersonName("打工人").verificationPersonPhone(phone).status(1).type(0).build().invoke(visitor, false).getString("message");
            String err = "手机号已存在";
            CommonUtil.checkResult("手机号格式为：" + phone, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,电话存在");
        }
    }

    private <T> List<T> getList(T str) {
        List<T> list = new ArrayList<>();
        list.add(str);
        return list;
    }
}
