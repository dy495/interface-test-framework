package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumApplyTypeName;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumPushTarget;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.CustomerLabelTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer.CustomMessageStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.financial.ApplyTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletMessageDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletMessageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.VoucherVerificationScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.WechatCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.MessageFormPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.BuyPackageRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PackageDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.PackageFormPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.SearchCustomerScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.PackageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.VoucherListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record.PushMsgPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher.VoucherGenerator;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 营销管理模块测试用例
 */
public class MarketingManage extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_ONLINE;
    private static final EnumAccount ADMINISTRATOR = EnumAccount.ADMINISTRATOR_ONLINE;
    private static final EnumAccount MARKETING = EnumAccount.MARKETING_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_DAILY;
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
        commonConfig.shopId = product.getShopId();
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

    @Test(description = "优惠券管理--创建优惠券--列表数+1&优惠券状态=待审核；【优惠券审批】列表数+1&审核状态=审核中&申请类型=首发")
    public void voucherManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(VoucherTypeEnum.values()).forEach(anEnum -> {
                int voucherTotal = visitor.invokeApi(VoucherFormPageScene.builder().build()).getInteger("total");
                int applyTotal = visitor.invokeApi(ApplyPageScene.builder().build()).getInteger("total");
                //创建优惠券
                String voucherName = util.createVoucher(1, anEnum);
                IScene scene = VoucherFormPageScene.builder().voucherName(voucherName).build();
                visitor.invokeApi(scene);
                //优惠券列表+1
                int newVoucherTotal = visitor.invokeApi(VoucherFormPageScene.builder().build()).getInteger("total");
                CommonUtil.checkResult("优惠券列表数", voucherTotal + 1, newVoucherTotal);
                //优惠券状态=待审核
                VoucherPage voucher = util.getVoucherPage(voucherName);
                Integer auditStatusName = voucher.getVoucherStatus();
                CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.getNameById(auditStatusName), VoucherStatusEnum.WAITING.getName());
                //优惠券变更记录+1
                Long voucherId = util.getVoucherId(voucherName);
                IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
                JSONObject response = visitor.invokeApi(changeRecordScene);
                int total = response.getInteger("total");
                CommonUtil.checkResult(voucherName + " 变更记录数", 1, total);
                String changeItem = response.getJSONArray("list").getJSONObject(0).getString("change_item");
                CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.CREATE.getName(), changeItem);
                //审核列表+1
                int newApplyTotal = visitor.invokeApi(ApplyPageScene.builder().build()).getInteger("total");
                CommonUtil.checkResult(voucherName + " 审批列表数", applyTotal + 1, newApplyTotal);
                //审核状态=审核中
                ApplyPage applyPage = util.getApplyPage(voucherName);
                CommonUtil.checkResult(voucherName + " 审核列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
                //申请类型=首发
                CommonUtil.checkResult(voucherName + " 审核申请类型", ApplyTypeEnum.VOUCHER.getName(), applyPage.getApplyTypeName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--创建自定义优惠券--列表数+1&优惠券状态=待审核；【优惠券审批】列表数+1&审核状态=审核中&申请类型=首发");
        }
    }

    @Test(description = "优惠券管理--优惠券详情--优惠券详情与创建时相同")
    public void voucherManage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(VoucherTypeEnum.values()).forEach(anEnum -> {
                IScene scene = VoucherFormPageScene.builder().voucherName(anEnum.getDesc()).build();
                List<Long> voucherIds = util.collectBean(scene, VoucherPage.class).stream().map(VoucherPage::getVoucherId).collect(Collectors.toList());
                List<VoucherDetail> voucherDetails = voucherIds.stream().map(id -> visitor.invokeApi(VoucherDetailScene.builder().id(id).build())).map(jsonObject -> JSONObject.toJavaObject(jsonObject, VoucherDetail.class)).collect(Collectors.toList());
                voucherDetails.forEach(e -> {
                    String voucherName = e.getVoucherName();
                    CommonUtil.checkResult(voucherName + " 描述", EnumDesc.VOUCHER_DESC.getDesc(), e.getVoucherDescription());
                    CommonUtil.checkResult(voucherName + " 主体类型", util.getSubjectType(), e.getSubjectType());
                    CommonUtil.checkResult(voucherName + " 主体类型id", util.getSubjectDesc(util.getSubjectType()), e.getSubjectId());
                    CommonUtil.checkResult(voucherName + " 面值", 49.99, e.getParValue());
                    CommonUtil.checkResult(voucherName + " 类型", anEnum.name(), e.getCardType());
                    CommonUtil.checkResult(voucherName + " 类型名称", anEnum.getDesc(), e.getCardTypeName());
                    if (anEnum.getDesc().equals(VoucherTypeEnum.COMMODITY_EXCHANGE.getDesc())) {
                        CommonUtil.checkResult(voucherName + " 满足金额", 999.99, e.getThresholdPrice());
                        CommonUtil.checkResult(voucherName + " 商品", "兑换布加迪威龙一辆", e.getExchangeCommodityName());
                    }
                    if (anEnum.getDesc().equals(VoucherTypeEnum.COUPON.getDesc())) {
                        CommonUtil.checkResult(voucherName + " 满足金额", 999.99, e.getThresholdPrice());
                        CommonUtil.checkResult(voucherName + " 折扣", 2.5, e.getDiscount());
                        CommonUtil.checkResult(voucherName + " 最对减", 100.00, e.getMostDiscount());
                    }
                    if (anEnum.getDesc().equals(VoucherTypeEnum.FULL_DISCOUNT.getDesc())) {
                        CommonUtil.checkResult(voucherName + " 满足金额", 999.99, e.getThresholdPrice());
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
    public void voucherManage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审核的优惠券id
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            String voucherName = util.getVoucherName(voucherId);
            String newVoucherName = voucherName + "改";
            List<Long> shopIds = util.getShopIdList(2);
            IScene scene = EditVoucher.builder().id(voucherId).voucherName(newVoucherName).voucherDescription(EnumDesc.MESSAGE_DESC.getDesc()).shopIds(shopIds).shopType(1).selfVerification(false).build();
            visitor.invokeApi(scene);
            //编辑
            IScene voucherDetailScene = VoucherDetailScene.builder().id(voucherId).build();
            VoucherDetail voucherDetail = JSONObject.toJavaObject(visitor.invokeApi(voucherDetailScene), VoucherDetail.class);
            //优惠券变更记录+1
            int newChangeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            CommonUtil.checkResult(newVoucherName + " 变更记列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //变更事项为编辑优惠券
            String changeItem = visitor.invokeApi(changeRecordScene).getJSONArray("list").getJSONObject(0).getString("change_item");
            CommonUtil.checkResult(newVoucherName + " 变更记录变更事项", ChangeItemEnum.EDIT.getName(), changeItem);
            //卡券列表
            CommonUtil.checkResult(newVoucherName + " 名称", newVoucherName, voucherDetail.getVoucherName());
            CommonUtil.checkResult(newVoucherName + " 详情", EnumDesc.MESSAGE_DESC.getDesc(), voucherDetail.getVoucherDescription());
            CommonUtil.checkResult(newVoucherName + " 适用门店", shopIds, voucherDetail.getShopIds());
            CommonUtil.checkResult(newVoucherName + " 业务类型", ShopTypeEnum.DIFF.getId(), voucherDetail.getShopType());
            CommonUtil.checkResult(newVoucherName + " 是否自助核销", false, voucherDetail.getSelfVerification());
            //审核列表
            ApplyPage applyPage = util.getApplyPage(newVoucherName);
            CommonUtil.checkResult(newVoucherName + " 审核列表名称", newVoucherName, applyPage.getName());
            CommonUtil.checkResult(newVoucherName + " 审核列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--编辑待审核优惠券--卡券信息更新");
        }
    }

    @Test(description = "优惠券管理--撤回优惠券--优惠券状态=已撤回&此优惠券在审核列表状态=已取消")
    public void voucherManage_data_4() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            //撤回
            IScene recallVoucherScene = RecallVoucherScene.builder().id(voucherId).build();
            visitor.invokeApi(recallVoucherScene);
            //校验
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.RECALL.getName(), voucherPage.getVoucherStatus());
            //审核列表
            ApplyPage applyPage = util.getApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审核列表状态", ApplyStatusEnum.CANCEL.getName(), applyPage.getStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--撤回优惠券--优惠券状态=已撤回&此优惠券在审核列表状态=已取消");
        }
    }

    @Test(description = "优惠券管理--删除已撤回优惠券--此券记录消失")
    public void voucherManage_data_5() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.RECALL).buildVoucher().getVoucherId();
            //删除
            visitor.invokeApi(DeleteVoucherScene.builder().id(voucherId).build());
            //校验
            String voucherName = util.getVoucherName(voucherId);
            IScene voucherPageScene = VoucherFormPageScene.builder().voucherName(voucherName).build();
            JSONArray list = visitor.invokeApi(voucherPageScene).getJSONArray("list");
            CommonUtil.checkResult(voucherName + " 结果列表", 0, list.size());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--删除已撤回优惠券--此券记录消失");
        }
    }

    @Test(description = "优惠券管理--删除审核未通过优惠券--此券记录消失")
    public void voucherManage_data_6() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.REJECT).buildVoucher().getVoucherId();
            //删除
            visitor.invokeApi(DeleteVoucherScene.builder().id(voucherId).build());
            //校验
            String voucherName = util.getVoucherName(voucherId);
            IScene voucherPageScene = VoucherFormPageScene.builder().voucherName(voucherName).build();
            JSONArray list = visitor.invokeApi(voucherPageScene).getJSONArray("list");
            CommonUtil.checkResult(voucherName + " 结果列表", 0, list.size());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--删除审核未通过优惠券--此券记录消失");
        }
    }

    @Test(description = "优惠券管理--进行中的优惠券暂停发放--优惠券状态=暂停发放")
    public void voucherManage_data_7() {
        try {
            Long voucherId = new VoucherGenerator.Builder().voucherStatus(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId();
            //暂停发放
            IScene changeProvideStatusScene = ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build();
            visitor.invokeApi(changeProvideStatusScene);
            //校验
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP, voucherPage.getVoucherStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--进行中的优惠券暂停发放--优惠券状态=暂停发放");
        }
    }

    @Test(description = "优惠券管理--进行中的优惠券作废--状态=已作废&变更记录+1变更事项=作废优惠券&操作人=当前账号&操作人角色=当前操作人角色&操作人账号=当前操作人账号")
    public void voucherManage_data_8() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            IScene scene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(scene).getInteger("total");
            //作废优惠券
            visitor.invokeApi(InvalidVoucher.builder().id(voucherId).build());
            //校验优惠券状态
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.getName(), voucherPage.getVoucherStatus());
            //校验变更记录列表数
            JSONObject response = visitor.invokeApi(scene);
            int newChangeRecordTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            List<VoucherChangeRecord> voucherChangeRecords = util.collectBean(scene, VoucherChangeRecord.class);
            VoucherChangeRecord voucherChangeRecord = voucherChangeRecords.get(0);
            String changeItem = voucherChangeRecord.getChangeItem();
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.INVALIDED.getName(), changeItem);
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--进行中的优惠券作废--状态=已作废&变更记录+1变更事项=作废优惠券&操作人=当前账号&操作人角色=当前操作人角色&操作人账号=当前操作人账号");
        }
    }

    @Test(description = "优惠券管理--进行中的优惠券增发")
    public void voucherManage_data_9() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = visitor.invokeApi(additionalRecordScene).getInteger("total");
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            //增发优惠券
            visitor.invokeApi(AddVoucherScene.builder().id(voucherId).addNumber(10).build());
            //优惠券审核列表卡券状态=增发
            ApplyPage applyPage = util.getApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审核申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审核列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            JSONObject response = visitor.invokeApi(additionalRecordScene);
            int newAddTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, newAddTotal);
            //增发记录状态
            String statusName = response.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.WAITING, statusName);
            //审核通过优惠券剩余库存+10
            util.applyVoucher(voucherName, "1");
            Long newSurplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory + 10, newSurplusInventory);
            //变更记录变更事项
            JSONObject changeResponse = visitor.invokeApi(changeRecordScene);
            int newChangeRecordTotal = changeResponse.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            List<VoucherChangeRecord> voucherChangeRecords = util.collectBean(changeRecordScene, VoucherChangeRecord.class);
            VoucherChangeRecord voucherChangeRecord = voucherChangeRecords.get(0);
            String changeItem = voucherChangeRecord.getChangeItem();
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.ADD.getName(), changeItem);
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
            //增发记录状体=已通过
            JSONObject newResponse = visitor.invokeApi(additionalRecordScene);
            String newStatusName = newResponse.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.WORKING, newStatusName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--进行中的优惠券增发");
        }
    }

    @Test(description = "优惠券管理--停止发放的优惠券开始发放--卡券状态=进行中")
    public void voucherManage_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.STOP).buildVoucher().getVoucherId();
            //开始发放
            IScene scene = ChangeProvideStatusScene.builder().id(voucherId).isStart(true).build();
            visitor.invokeApi(scene);
            //校验状态
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.WORKING.getName(), voucherPage.getVoucherStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--停止发放的优惠券开始发放--卡券状态=进行中");
        }
    }

    @Test(description = "优惠券管理--停止发放的优惠券作废--状态=已作废&变更记录+1变更事项=作废优惠券")
    public void voucherManage_data_11() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.STOP).buildVoucher().getVoucherId();
            IScene scene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(scene).getInteger("total");
            //作废优惠券
            visitor.invokeApi(InvalidVoucher.builder().id(voucherId).build());
            //校验优惠券状态
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.getName(), voucherPage.getVoucherStatus());
            JSONObject response = visitor.invokeApi(scene);
            //校验变更记录列表数
            int newChangeRecordTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            List<VoucherChangeRecord> voucherChangeRecords = util.collectBean(scene, VoucherChangeRecord.class);
            VoucherChangeRecord voucherChangeRecord = voucherChangeRecords.get(0);
            String changeItem = voucherChangeRecord.getChangeItem();
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.INVALIDED.getName(), changeItem);
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--停止发放的优惠券作废--状态=已作废&变更记录+1变更事项=作废优惠券");
        }
    }

    @Test(description = "优惠券管理--停止发放的优惠券增发")
    public void voucherManage_data_12() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.STOP).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = visitor.invokeApi(additionalRecordScene).getInteger("total");
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            //增发优惠券
            visitor.invokeApi(AddVoucherScene.builder().id(voucherId).addNumber(10).build());
            //优惠券审核列表卡券状态=增发
            ApplyPage applyPage = util.getApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审核申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审核列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            JSONObject response = visitor.invokeApi(additionalRecordScene);
            int newAddTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, newAddTotal);
            //增发记录状态
            String statusName = response.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.WAITING, statusName);
            //审核通过优惠券剩余库存+10
            util.applyVoucher(voucherName, "1");
            Long newSurplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory + 10, newSurplusInventory);
            //变更记录+1
            JSONObject changeResponse = visitor.invokeApi(changeRecordScene);
            int newChangeRecordTotal = changeResponse.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            List<VoucherChangeRecord> voucherChangeRecords = util.collectBean(changeRecordScene, VoucherChangeRecord.class);
            VoucherChangeRecord voucherChangeRecord = voucherChangeRecords.get(0);
            String changeItem = voucherChangeRecord.getChangeItem();
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.ADD.getName(), changeItem);
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
            //增发记录状体=已通过
            JSONObject newResponse = visitor.invokeApi(additionalRecordScene);
            String newStatusName = newResponse.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.WORKING, newStatusName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--停止发放的优惠券增发");
        }
    }

    @Test(description = "优惠券管理--已售罄的优惠券暂停发放--优惠券状态=暂停发放")
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
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP, voucherPage.getVoucherStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--已售罄的优惠券暂停发放--优惠券状态=暂停发放");
        }
    }

    @Test(description = "优惠券管理--已售罄的优惠券作废--状态=已作废&变更记录+1变更事项=作废优惠券")
    public void voucherManage_data_14() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            //作废优惠券
            visitor.invokeApi(InvalidVoucher.builder().id(voucherId).build());
            //校验优惠券状态
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.getName(), voucherPage.getVoucherStatus());
            //变更记录+1
            JSONObject changeResponse = visitor.invokeApi(changeRecordScene);
            int newChangeRecordTotal = changeResponse.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            List<VoucherChangeRecord> voucherChangeRecords = util.collectBean(changeRecordScene, VoucherChangeRecord.class);
            VoucherChangeRecord voucherChangeRecord = voucherChangeRecords.get(0);
            String changeItem = voucherChangeRecord.getChangeItem();
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.INVALIDED.getName(), changeItem);
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--已售罄的优惠券作废--状态=已作废&变更记录+1变更事项=作废优惠券");
        }
    }

    @Test(description = "优惠券管理--已售罄的优惠券增发--审核通过状态")
    public void voucherManage_data_15() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = visitor.invokeApi(additionalRecordScene).getInteger("total");
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            //增发优惠券
            visitor.invokeApi(AddVoucherScene.builder().id(voucherId).addNumber(10).build());
            //优惠券审核列表卡券状态=增发
            ApplyPage applyPage = util.getApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审核申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审核列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            JSONObject response = visitor.invokeApi(additionalRecordScene);
            int newAddTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, newAddTotal);
            //增发记录状态
            String statusName = response.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.WAITING, statusName);
            //审核通过优惠券剩余库存+10
            util.applyVoucher(voucherName, "1");
            Long newSurplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory + 10, newSurplusInventory);
            //变更记录变更事项
            JSONObject changeResponse = visitor.invokeApi(changeRecordScene);
            int newChangeRecordTotal = changeResponse.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            List<VoucherChangeRecord> voucherChangeRecords = util.collectBean(changeRecordScene, VoucherChangeRecord.class);
            VoucherChangeRecord voucherChangeRecord = voucherChangeRecords.get(0);
            String changeItem = voucherChangeRecord.getChangeItem();
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.ADD.getName(), changeItem);
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
            //增发记录状体=已通过
            JSONObject newResponse = visitor.invokeApi(additionalRecordScene);
            String newStatusName = newResponse.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.WORKING, newStatusName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--已售罄的优惠券增发--审核通过状态");
        }
    }

    @Test(description = "优惠券管理--增发卡券--审批通过前，增发库存不变，剩余库存存不变")
    public void voucherManage_data_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            Long surplusInventory = voucherPage.getSurplusInventory();
            //增发
            IScene addVoucherScene = AddVoucherScene.builder().id(voucherId).addNumber(10).build();
            visitor.invokeApi(addVoucherScene);
            //增发后数据
            CommonUtil.checkResult(voucherName + " 增发审批通过前剩余库存数量", surplusInventory, util.getVoucherPage(voucherName).getSurplusInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--增发卡券--审批通过前，增发库存不变，剩余库存存不变");
        }
    }

    @Test(description = "优惠券管理--增发卡券--审批不通过，新剩余库存=原剩余库存")
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
            CommonUtil.checkResult(voucherName + " 增发审批拒绝后剩余库存数量", surplusInventory, util.getVoucherPage(voucherName).getSurplusInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--增发卡券--审批不通过，新剩余库存=原剩余库存");
        }
    }

    @Test(description = "优惠券管理--状态为已售罄时，剩余库存=0")
    public void voucherManage_data_18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormPageScene.builder().voucherStatus(VoucherStatusEnum.SELL_OUT.name()).build();
            List<VoucherPage> voucherPages = util.collectBean(scene, VoucherPage.class);
            voucherPages.forEach(e -> CommonUtil.checkResult(e.getVoucherName() + " 剩余库存", 0, e.getSurplusInventory()));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--状态为已售罄时，剩余库存=0");
        }
    }

    @Test(description = "优惠券管理--购买一个临时套餐，套餐内优惠券剩余库存-1&套餐购买记录+1")
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
            //购买临时套餐
            user.loginPc(ADMINISTRATOR);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            util.buyTemporaryPackage(voucherList, 1);
            util.makeSureBuyPackage("临时套餐");
            //购买后数据
            CommonUtil.checkResult("套餐购买记录", buyPackageRecordTotal + 1, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            List<VoucherSendRecord> voucherSendRecords = util.getVoucherSendRecordList(voucherName);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NORMAL.getName(), voucherSendRecords.get(0).getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取人电话", MARKETING.getPhone(), voucherSendRecords.get(0).getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecords.get(0).getSendChannelName());
            CommonUtil.checkResult(voucherName + " 领取人标签", CustomerLabelTypeEnum.VIP.getTypeName(), voucherSendRecords.get(0).getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum + 1, util.getAppletVoucherNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--购买一个临时套餐，套餐内优惠券剩余库存-1&套餐购买记录+1");
        }
    }

    @Test(description = "优惠券管理--购买一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1")
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
            user.loginPc(ADMINISTRATOR);
            //购买固定套餐
            util.buyFixedPackage(packageId, 1);
            //确认支付
            util.makeSureBuyPackage(packageName);
            //购买后数据
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            CommonUtil.checkResult(packageName + " 累计购买", soldNumber + 1, util.getPackagePage(packageName).getSoldNumber());
            List<VoucherSendRecord> voucherSendRecords = util.getVoucherSendRecordList(voucherName);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NORMAL.getName(), voucherSendRecords.get(0).getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取人电话", MARKETING.getPhone(), voucherSendRecords.get(0).getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PURCHASE.getName(), voucherSendRecords.get(0).getSendChannelName());
            CommonUtil.checkResult(voucherName + " 领取人标签", CustomerLabelTypeEnum.VIP.getTypeName(), voucherSendRecords.get(0).getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 购买后剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum + 1, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum + 1, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--购买一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1");
        }
    }

    @Test(description = "优惠券管理--赠送一个临时套餐，套餐内优惠券剩余库存-1&套餐购买记录+1")
    public void voucherManage_data_21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //购买前数据
            Long surplusInventory = util.getVoucherPage(voucherName).getSurplusInventory();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            //赠送临时套餐
            user.loginPc(ADMINISTRATOR);
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            util.buyTemporaryPackage(voucherList, 0);
            //赠送后数据
            List<VoucherSendRecord> voucherSendRecords = util.getVoucherSendRecordList(voucherName);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NORMAL.getName(), voucherSendRecords.get(0).getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取人电话", MARKETING.getPhone(), voucherSendRecords.get(0).getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PRESENT.getName(), voucherSendRecords.get(0).getSendChannelName());
            CommonUtil.checkResult(voucherName + " 领取人标签", CustomerLabelTypeEnum.VIP.getTypeName(), voucherSendRecords.get(0).getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum + 1, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--赠送一个临时套餐，套餐内优惠券剩余库存-1&套餐购买记录+1");
        }
    }

    @Test(description = "优惠券管理--赠送一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1")
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
            int giveNumber = util.getPackagePage(packageName).getGiveNumber();
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalSend = visitor.invokeApi(voucherInfoScene).getInteger("total_send");
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletPackageNum = util.getAppletPackageNum();
            user.loginPc(ADMINISTRATOR);
            //赠送固定套餐
            util.buyFixedPackage(packageId, 0);
            //赠送后数据
            CommonUtil.checkResult("套餐购买列表数", buyPackageRecordTotal, visitor.invokeApi(buyPackageRecordScene).getInteger("total"));
            CommonUtil.checkResult(packageName + " 累计赠送", giveNumber + 1, util.getPackagePage(packageName).getGiveNumber());
            List<VoucherSendRecord> voucherSendRecords = util.getVoucherSendRecordList(voucherName);
            CommonUtil.checkResult(voucherName + " 领取记录卡券使用状态", VoucherUseStatusEnum.NORMAL.getName(), voucherSendRecords.get(0).getVoucherUseStatus());
            CommonUtil.checkResult(voucherName + " 领取人电话", MARKETING.getPhone(), voucherSendRecords.get(0).getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 领取记录卡券发出渠道", VoucherSourceEnum.PRESENT.getName(), voucherSendRecords.get(0).getSendChannelName());
            CommonUtil.checkResult(voucherName + " 领取人标签", CustomerLabelTypeEnum.VIP.getTypeName(), voucherSendRecords.get(0).getCustomerLabelName());
            CommonUtil.checkResult(voucherName + " 购买后剩余库存", surplusInventory - 1, util.getVoucherPage(voucherName).getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 共领取数", totalSend + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_send"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult("小程序我的卡券数", appletVoucherNum + 1, util.getAppletVoucherNum());
            CommonUtil.checkResult("小程序我的套餐数", appletPackageNum + 1, util.getAppletPackageNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--赠送一个固定套餐，包含卡券剩余库存-1&套餐购买记录+1&套餐购买数量+1");
        }
    }

    @Test(description = "优惠券管理--作废某人的优惠券，小程序上此券状态为已作废&作废记录+1")
    public void voucherManage_data_24() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            util.pushMessage(0, true, voucherId);
            //作废前数据
            List<VoucherInvalidPage> voucherInvalidPages = util.getVoucherInvalidList(voucherId);
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalInvalid = visitor.invokeApi(voucherInfoScene).getInteger("total_invalid");
            //作废
            List<VoucherSendRecord> voucherSendRecords = util.getVoucherSendRecordList(voucherId);
            Long recordId = voucherSendRecords.get(0).getId();
            String voucherCode = voucherSendRecords.get(0).getVoucherCode();
            IScene scene = InvalidCustomerVoucherScene.builder().id(recordId).invalidReason(EnumDesc.INVALID_REASON.getDesc()).build();
            visitor.invokeApi(scene);
            //作废后数据
            List<VoucherInvalidPage> newVoucherInvalidPages = util.getVoucherInvalidList(voucherId);
            CommonUtil.checkResult(voucherName + " 作废后作废记录列表数", voucherInvalidPages.size() + 1, newVoucherInvalidPages);
            CommonUtil.checkResult(voucherName + " 作废后领取人电话", MARKETING.getPhone(), newVoucherInvalidPages.get(0).getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 作废后作废人姓名", ADMINISTRATOR.getName(), newVoucherInvalidPages.get(0).getInvalidName());
            CommonUtil.checkResult(voucherName + " 作废后作废人电话", ADMINISTRATOR.getPhone(), newVoucherInvalidPages.get(0).getInvalidPhone());
            CommonUtil.checkResult(voucherName + " 作废后作废说明", EnumDesc.INVALID_REASON.getDesc(), newVoucherInvalidPages.get(0).getInvalidDescription());
            CommonUtil.checkResult(voucherCode + " 作废后共作废数", totalInvalid + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_invalid"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult(voucherName + " 作废后小程序上此卡券状态", CustomerVoucherStatusEnum.INVALIDED.getDesc(), util.getAppletVoucherInfo(voucherCode).getStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--作废某人的优惠券，小程序上此券状态为已作废&作废记录+1");
        }
    }

    @Test(description = "优惠券管理--优惠券共领取数=领取记录列表数")
    public void voucherManage_data_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormPageScene.builder().build();
            List<VoucherPage> voucherPages = util.collectBean(scene, VoucherPage.class);
            voucherPages.forEach(e -> {
                String voucherName = util.getVoucherName(e.getVoucherId());
                Long totalSend = visitor.invokeApi(VoucherInfoScene.builder().id(e.getVoucherId()).build()).getLong("total_send");
                Long total = visitor.invokeApi(SendRecordScene.builder().id(e.getVoucherId()).build()).getLong("total");
                CommonUtil.checkResultPlus(voucherName + " 共领取数", totalSend, "领取记录列表数", total);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--卡券领取记录共领取数=领取记录列表数");
        }
    }

    @Test(description = "优惠券管理--优惠券共核销数=核销记录列表数")
    public void voucherManage_data_26() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormPageScene.builder().build();
            List<VoucherPage> voucherPages = util.collectBean(scene, VoucherPage.class);
            voucherPages.forEach(e -> {
                String voucherName = util.getVoucherName(e.getVoucherId());
                Long totalVerify = visitor.invokeApi(VoucherInfoScene.builder().id(e.getVoucherId()).build()).getLong("total_verify");
                Long total = visitor.invokeApi(VerificationRecordScene.builder().id(e.getVoucherId()).build()).getLong("total");
                CommonUtil.checkResultPlus(voucherName + " 共核销数", totalVerify, "核销记录列表数", total);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券表单--累计使用=【核销记录】中按该卡券名称搜索结果的列表数");
        }
    }

    @Test(description = "优惠券管理--优惠券共作废数=作废记录列表数")
    public void voucherManage_data_27() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormPageScene.builder().build();
            List<VoucherPage> voucherPages = util.collectBean(scene, VoucherPage.class);
            voucherPages.forEach(e -> {
                String voucherName = util.getVoucherName(e.getVoucherId());
                Long totalInvalid = visitor.invokeApi(VoucherInfoScene.builder().id(e.getVoucherId()).build()).getLong("total_invalid");
                Long total = visitor.invokeApi(VoucherInvalidPageScene.builder().id(e.getVoucherId()).build()).getLong("total");
                CommonUtil.checkResultPlus(voucherName + " 共作废数", totalInvalid, "作废记录列表数", total);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--优惠券共作废数=作废记录列表数");
        }
    }

    @Test(description = "优惠券管理--增发记录列表数=卡券审核列表申请类型为增发的列表数&两边增发数量之和相等")
    public void voucherManage_data_28() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherPageScene.builder().build();
            List<VoucherPage> voucherPages = util.collectBean(scene, VoucherPage.class);
            List<Long> voucherIds = voucherPages.stream().map(VoucherPage::getVoucherId).collect(Collectors.toList());
            voucherIds.forEach(voucherId -> {
                //增发记录
                IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
                List<AdditionalRecord> additionalRecords = util.collectBean(additionalRecordScene, AdditionalRecord.class);
                Long additionalRecordSum = (long) additionalRecords.stream().map(AdditionalRecord::getAdditionalNum).collect(Collectors.toList()).stream().mapToInt(Integer::parseInt).sum();
                String voucherName = util.getVoucherName(voucherId);
                IScene applyPageScene = ApplyPageScene.builder().name(voucherName).build();
                List<ApplyPage> applyPages = util.collectBean(applyPageScene, ApplyPage.class);
                List<Integer> applyPageList = applyPages.stream().filter(e -> e.getApplyTypeName().equals(EnumApplyTypeName.FIRST_PUBLISH.getName())).map(ApplyPage::getNum).collect(Collectors.toList());
                Long applyPageSum = (long) applyPageList.stream().mapToInt(e -> e).sum();
                CommonUtil.checkResultPlus(voucherName + " 增发记录列表数", additionalRecords.size(), "卡券审核列表申请类型为增发的列表数", applyPageList.size());
                CommonUtil.checkResultPlus(voucherName + " 增发记录总增发量", additionalRecordSum, "卡券审核列表申请类型为增发的总增发量", applyPageSum);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--增发记录列表数=优惠券审核列表此卡全的增发审核列表数&两边深审核通过的增发数量之和相等");
        }
    }

    @Test(description = "优惠券管理--小程序自助核销一张，使用的核销码对应人员册核销数量+1&【核销记录】列表数+1&&核销渠道=主动核销")
    public void voucherManage_data_29() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //发出一张卡券
            util.pushMessage(0, true, voucherId);
            //获取最新发出卡券的code
            String voucherCode = util.getVoucherCode(voucherId);
            //核销列表数
            IScene verificationRecordScene = VerificationRecordScene.builder().build();
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
            IScene voucherVerificationScene = VoucherVerificationScene.builder().id(String.valueOf(appletVoucherId)).build();
            visitor.invokeApi(voucherVerificationScene);
            //核销之后数据
            user.loginPc(ADMINISTRATOR);
            List<VerificationRecord> newVerificationRecords = util.collectBean(verificationRecordScene, VerificationRecord.class);
            CommonUtil.checkResult(voucherName + " 核销记录列表数", verificationRecords.size() + 1, newVerificationRecords.size());
            CommonUtil.checkResult(voucherCode + " 核销渠道", VerifyChannelEnum.ACTIVE.getName(), newVerificationRecords.get(0).getVerificationChannelName());
            int newVerificationNumber = visitor.invokeApi(verificationPeopleScene).getJSONArray("list").getJSONObject(0).getInteger("verification_number");
            CommonUtil.checkResult("核销码" + code + "核销数", verificationNumber, newVerificationNumber);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--小程序自助核销一张，使用的核销码对应人员册核销数量+1&【核销记录】列表数+1&&核销渠道=主动核销");
        }
    }


    @Test(description = "核销人员--创建异页核销,列表数+1")
    public void verificationPeople_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene verificationPeopleScene = VerificationPeopleScene.builder().build();
            //查询列表数
            int total = visitor.invokeApi(verificationPeopleScene).getInteger("total");
            String phone = util.getDistinctPhone();
            IScene createVerificationPeopleScene = CreateVerificationPeopleScene.builder().verificationPersonName("异页打工人").verificationPersonPhone(phone).type(1).status(1).build();
            visitor.invokeApi(createVerificationPeopleScene);
            int newTotal = visitor.invokeApi(createVerificationPeopleScene).getInteger("total");
            CommonUtil.checkResult("核销人员列表数", total + 1, newTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建异页核销,列表数+1");
        }
    }


    @Test(description = "核销人员--创建财务核销,列表数+1&创建异页核销,列表数+1")
    public void voucherManage_data_30() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String phone = util.getStaffPhone();
            if (phone != null) {
                IScene verificationPeopleScene = VerificationPeopleScene.builder().build();
                //查询列表数
                int total = visitor.invokeApi(verificationPeopleScene).getInteger("total");
                //创建核销人员
                IScene createVerificationPeopleScene = CreateVerificationPeopleScene.builder().verificationPersonName("本司打工人").verificationPersonPhone(phone).type(0).status(1).build();
                visitor.invokeApi(createVerificationPeopleScene);
                //创建核销人员后数据
                int newTotal = visitor.invokeApi(verificationPeopleScene).getInteger("total");
                CommonUtil.checkResult("核销人员列表数", total + 1, newTotal);
            } else {
                System.err.println("本司角色手机号都被用完了");
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,列表数+1&创建异页核销,列表数+1");
        }
    }

    @Test(description = "套餐管理--创建套餐包含卡券列表数=卡券状态为进行中的列表数")
    public void packageManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene voucherListScene = VoucherListScene.builder().build();
            int voucherListSize = visitor.invokeApi(voucherListScene).getJSONArray("list").size();
            IScene voucherPageScene = VoucherPageScene.builder().voucherStatus(VoucherStatusEnum.WORKING.name()).build();
            int voucherTotal = visitor.invokeApi(voucherPageScene).getInteger("total");
            CommonUtil.checkResult("套餐包含卡券列表数", voucherListSize, "进行中的卡券数", voucherTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐包含卡券列表数=卡券状态为进行中的列表数");
        }
    }

    @Test(description = "套餐管理--创建套餐，选择各个主体创建套餐，套餐列表每次均+1")
    public void packageManager_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Arrays.stream(UseRangeEnum.values()).forEach(subject -> {
                //创建套餐前列表数量
                IScene packageFormPageScene = PackageFormPageScene.builder().build();
                int total = visitor.invokeApi(packageFormPageScene).getInteger("total");
                //创建套餐
                JSONArray voucherArray = util.getVoucherArray(voucherId, 10);
                String packageName = util.createPackage(voucherArray);
                //创建套餐后列表数量
                int newTotal = visitor.invokeApi(packageFormPageScene).getInteger("total");
                CommonUtil.checkResult("创建套餐后套餐列表数", total + 1, newTotal);
                //列表内容校验
                PackagePage packagePage = util.collectBean(PackageFormPageScene.builder().packageName(packageName).build(), PackagePage.class).get(0);
                CommonUtil.checkResult(packageName + " 套餐价格", 49.99, packagePage.getPrice());
                CommonUtil.checkResult(packageName + " 套餐有效期", 30, packagePage.getValidity());
                CommonUtil.checkResult(packageName + " 套餐内含卡券数", 10, packagePage.getVoucherNumber());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--创建套餐，选择各个主体创建套餐，套餐列表每次均+1");
        }
    }

    @Test(description = "套餐管理--购买套餐--【小程序客户】列表的手机号，均可查询到姓名")
    public void packageManager_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = WechatCustomerPageScene.builder().build();
            List<JSONObject> jsonObjects = util.collectBean(scene, JSONObject.class);
            jsonObjects.forEach(e -> {
                String customerPhone = e.getString("customer_phone");
                String customerName = e.getString("customer_name");
                IScene searchCustomerScene = SearchCustomerScene.builder().customerPhone(customerPhone).build();
                String findCustomerName = visitor.invokeApi(searchCustomerScene).getString("customer_name");
                CommonUtil.checkResultPlus(customerPhone + " 小程序客户名称", customerName, "购买套餐查询到的名称", findCustomerName);
                CommonUtil.logger(customerPhone);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--购买套餐--【小程序客户】列表的手机号，均可查询到姓名");
        }
    }

    @Test(description = "套餐管理--套餐列表展示内容与套餐详情一致")
    public void packageManager_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene packageFormPageScene = PackageFormPageScene.builder().build();
            List<PackagePage> packagePages = util.collectBean(packageFormPageScene, PackagePage.class);
            packagePages.forEach(packagePage -> {
                IScene packageDetailScene = PackageDetailScene.builder().id(packagePage.getPackageId()).build();
                JSONObject detail = visitor.invokeApi(packageDetailScene);
                CommonUtil.checkResultPlus(packagePage.getPackageName() + " 列表价格", packagePage.getPrice(), "详情价格", detail.getString("package_price"));
                CommonUtil.checkResultPlus(packagePage.getPackageName() + " 列表有效期", packagePage.getValidity(), "详情有效期", detail.getString("validity"));
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--套餐列表展示内容与套餐详情一致");
        }
    }

    @Test(description = "套餐管理--购买套餐--下拉可选择的套餐数量=未取消&未过期的套餐数")
    public void packageManager_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene packageListScene = PackageListScene.builder().build();
            int packageListSize = visitor.invokeApi(packageListScene).getJSONArray("list").size();
            IScene packageFormPageScene = PackageFormPageScene.builder().packageStatus(true).build();
            List<PackagePage> packagePages = util.collectBean(packageFormPageScene, PackagePage.class);
            int packageCount = (int) packagePages.stream().filter(this::compareTime).count();
            CommonUtil.checkResultPlus("未取消&未过期套餐数", packageListSize, "购买套餐下拉列表数", packageCount);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--购买套餐--下拉可选择的套餐数量=未取消&未过期的套餐数");
        }
    }

    @NotNull
    private Boolean compareTime(@NotNull PackagePage packagePage) {
        String format = "yyyy-MM-dd HH:mm";
        String today = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd") + " 00:00";
        long todayUnix = Long.parseLong(DateTimeUtil.dateToStamp(today, format));
        String createTime = packagePage.getCreateTime();
        Integer validity = packagePage.getValidity();
        long unix = Long.parseLong(DateTimeUtil.dateToStamp(createTime, format)) + (long) validity * 24 * 60 * 60 * 1000;
        return unix >= todayUnix;
    }

    @Test(description = "套餐购买记录--套餐累计赠送数量=套餐购买记录中该套餐的赠送数量")
    public void packageManager_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene packageFormPageScene = PackageFormPageScene.builder().build();
            List<PackagePage> packagePages = util.collectBean(packageFormPageScene, PackagePage.class);
            packagePages.forEach(e -> {
                String packageName = e.getPackageName();
                int giverNumber = e.getGiveNumber();
                IScene buyPackageRecordScene = BuyPackageRecordScene.builder().packageName(packageName).build();
                List<JSONObject> jsonObjects = util.collectBean(buyPackageRecordScene, JSONObject.class);
                int giverCount = (int) jsonObjects.stream().filter(jsonObject -> jsonObject.getString("pay_type_name").equals(SendWayEnum.PRESENT.getName())).count();
                CommonUtil.checkResultPlus(packageName + " 累计赠送数", giverNumber, " 购买记录赠送列表数", giverCount);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐购买记录--套餐累计赠送数量=套餐购买记录中该套餐的赠送数量");
        }
    }

    @Test(description = "套餐购买记录--套餐累计出售数量=套餐购买记录中该套餐的出售数量")
    public void packageManager_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene packageFormPageScene = PackageFormPageScene.builder().build();
            List<PackagePage> packagePages = util.collectBean(packageFormPageScene, PackagePage.class);
            packagePages.forEach(e -> {
                String packageName = e.getPackageName();
                int giverNumber = e.getSoldNumber();
                IScene buyPackageRecordScene = BuyPackageRecordScene.builder().packageName(packageName).build();
                List<JSONObject> jsonObjects = util.collectBean(buyPackageRecordScene, JSONObject.class);
                int giverCount = (int) jsonObjects.stream().filter(jsonObject -> jsonObject.getString("pay_type_name").equals(SendWayEnum.SOLD.getName())).count();
                CommonUtil.checkResultPlus(packageName + " 累计赠送数", giverNumber, " 购买记录赠送列表数", giverCount);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐购买记录--套餐累计出售数量=套餐购买记录中该套餐的出售数量");
        }
    }

    @Test(description = "消息管理--推送消息含有一张优惠券的消息，消息记录+1，卡券库存-1，发卡记录+1")
    public void messageManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene messageFormPageScene = MessageFormPageScene.builder().build();
            int messageTotal = visitor.invokeApi(messageFormPageScene).getInteger("total");
            IScene sendRecordScene = SendRecordScene.builder().id(voucherId).build();
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
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory + 1, util.getVoucherPage(voucherId).getSurplusInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--推送消息含有一张卡券，推送成功后，【卡券表单页】该卡券累计发出+1，发卡记录列表+1");
        }
    }

    @Test(description = "消息管理--定时发送，列表+1&状态=排期中")
    public void messageManager_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            IScene messageFormPageScene = MessageFormPageScene.builder().build();
            int messageTotal = visitor.invokeApi(messageFormPageScene).getInteger("total");
            util.pushMessage(0, false, voucherId);
            String sendStatusName = visitor.invokeApi(messageFormPageScene).getJSONArray("list").getJSONObject(0).getString("send_status_name");
            CommonUtil.checkResult("消息管理列表", messageTotal + 1, visitor.invokeApi(messageFormPageScene).getInteger("total"));
            CommonUtil.checkResult("发送状态", CustomMessageStatusEnum.SCHEDULING.getStatusName(), sendStatusName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--定时发送，列表+1&状态=排期中");
        }
    }

    @Test(description = "消息管理--发送给一个客户时客户名称&联系方式&车牌号码显示该客户信息")
    public void messageManager_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            user.loginApplet(APPLET_USER_ONE);
            int appletVoucherNum = util.getAppletVoucherNum();
            int appletMessageNum = util.getAppletMessageNum();
            user.loginPc(ADMINISTRATOR);
            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //发送消息
            util.pushMessage(0, true, voucherId);
            IScene messageFormPageScene = MessageFormPageScene.builder().build();
            List<JSONObject> jsonObjects = util.collectBean(messageFormPageScene, JSONObject.class);
            JSONObject jsonObject = Objects.requireNonNull(jsonObjects.stream().filter(e -> e.getString("push_time").equals(date)).findFirst().orElse(null));
            String customerPhone = jsonObject.getString("customer_phone");
            String platNumber = jsonObject.getString("plate_number");
            CommonUtil.checkResult("车牌号", util.getPlatNumber(customerPhone), platNumber);
            CommonUtil.checkResult("联系方式", MARKETING.getPhone(), customerPhone);
            user.loginApplet(APPLET_USER_ONE);
            int newAppletVoucherNum = util.getAppletVoucherNum();
            int newAppletMessageNum = util.getAppletMessageNum();
            CommonUtil.checkResult("小程序我的卡券数量", appletVoucherNum + 1, newAppletVoucherNum);
            CommonUtil.checkResult("小程序我的消息数量", appletMessageNum + 1, newAppletMessageNum);
            //小程序我的消息+1，我的卡券+1
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--发送给一个客户时客户名称&联系方式&车牌号码显示该客户信息");
        }
    }

    @Test(description = "消息管理--发送给发送多人时客户名称为全部&联系方式&车牌号码显示为空", priority = 1)
    public void messageManager_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> shopList = util.getShopIdList(1);
            //发送消息
            IScene scene = PushMessageScene.builder().pushTarget(EnumPushTarget.SHOP_CUSTOMER.name()).shopList(shopList)
                    .messageName(EnumDesc.MESSAGE_TITLE.getDesc()).messageContent(EnumDesc.MESSAGE_DESC.getDesc())
                    .ifSendImmediately(true).build();
            visitor.invokeApi(scene);
            String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            IScene messageFormPageScene = MessageFormPageScene.builder().build();
            List<JSONObject> jsonObjectList = util.collectBean(messageFormPageScene, JSONObject.class);
            JSONObject jsonObject = jsonObjectList.stream().filter(e -> e.getString("push_time").equals(date)).findFirst().orElse(null);
            CommonUtil.checkResult("车牌号", null, jsonObject != null ? jsonObject.getString("plat_number") : null);
            CommonUtil.checkResult("客户名称", "全部", jsonObject != null ? jsonObject.getString("customer_name") : null);
            CommonUtil.checkResult("联系方式", null, jsonObject != null ? jsonObject.getString("customer_phone") : null);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--发送给发送多人时客户名称为全部&联系方式&车牌号码显示为空");
        }
    }


    @Test(description = "消息管理--发送成功率=发出条数/收到条数，结果x=100%时为全部成功，结果0%<=x<100%显示成功百分比")
    public void messageManager_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            AtomicInteger i = new AtomicInteger();
            IScene scene = MessageFormPageScene.builder().build();
            List<JSONObject> jsonObjectList = util.collectBean(scene, JSONObject.class);
            jsonObjectList.forEach(jsonObject -> {
                int sendCount = jsonObject.getInteger("send_count");
                int receiveCount = jsonObject.getInteger("receive_count");
                String percent = CommonUtil.getPercent(sendCount, receiveCount);
                String result = percent.equals("0.0%") ? "成功0%" : "全部成功";
                String statusName = jsonObject.getString("status_name");
                int page = (i.getAndIncrement() / 10) + 1;
                int size = (i.getAndIncrement() % 10) + 1;
                CommonUtil.valueView(page + "页" + size + "条" + "发出条数/收到条数");
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
//            saveData("消息管理--发送成功率=发出条数/收到条数，结果x=100%时为全部成功，结果0%<=x<100%显示成功百分比");
        }
    }

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

    @Test(description = "消息管理--消息发出后，消息内容&标题一致")
    public void messageManager_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //发消息
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            util.pushMessage(0, true, voucherId);
            //消息列表消息内容
            user.loginApplet(APPLET_USER_ONE);
            IScene appletMessageListScene = AppletMessageListScene.builder().size(20).build();
            JSONObject jsonObject = visitor.invokeApi(appletMessageListScene).getJSONArray("list").getJSONObject(0);
            int id = jsonObject.getInteger("id");
            IScene appletMessageDetailScene = AppletMessageDetailScene.builder().id(String.valueOf(id)).build();
            String content = visitor.invokeApi(appletMessageDetailScene).getString("content");
            String title = visitor.invokeApi(appletMessageDetailScene).getString("title");
            CommonUtil.checkResult("消息内容", EnumDesc.MESSAGE_DESC.getDesc(), content);
            CommonUtil.checkResult("消息标题", EnumDesc.MESSAGE_TITLE.getDesc(), title);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--消息发出后，消息内容&标题一致");
        }
    }
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

    //------------------------------------------------------------------------------------------------------------------

    private <T> List<T> getList(T str) {
        List<T> list = new ArrayList<>();
        list.add(str);
        return list;
    }
}


