package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletVoucher;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.UseStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.CustomerLabelTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.audit.AuditStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer.CustomMessageStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.financial.ApplyTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.IVoucher;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanager.WechatCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.MessageFormPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.messagemanage.PushMessageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.PackageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanager.ReceptionVoucherListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record.PushMsgPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
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
import java.util.stream.Collectors;

/**
 * 营销管理模块测试用例
 */
public class MarketingManageCaseOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_ONLINE;
    private static final EnumAccount ADMINISTRATOR = EnumAccount.ADMINISTRATOR_ONLINE;
    //小程序用户
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_ONLINE;
    private static final EnumAppletToken APPLET_USER_TWO = EnumAppletToken.JC_XMF_ONLINE;
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
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.product = product.getAbbreviation();
        commonConfig.referer = product.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.roleId = product.getRoleId();
        commonConfig.referer = product.getReferer();
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

    //ok
    @Test(description = "卡券管理--创建卡券--列表数+1&卡券状态=待审批；【卡券审批】列表数+1&审批状态=审批中&申请类型=首发")
    public void voucherManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(VoucherTypeEnum.values()).forEach(anEnum -> {
                int voucherTotal = visitor.invokeApi(VoucherPageScene.builder().build()).getInteger("total");
                int applyTotal = visitor.invokeApi(ApplyPageScene.builder().build()).getInteger("total");
                //创建卡券
                String voucherName = util.createVoucher(1, anEnum);
                IScene voucherPageScene = VoucherPageScene.builder().voucherName(voucherName).build();
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
                int newVoucherTotal = visitor.invokeApi(VoucherPageScene.builder().build()).getInteger("total");
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
            IScene scene = EditVoucherScene.builder().id(Math.toIntExact(voucherId)).voucherName(newVoucherName).voucherDescription(EnumDesc.MESSAGE_DESC.getDesc()).shopIds(shopIds).shopType(1).selfVerification(false).build();
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
            CommonUtil.checkResult(newVoucherName + " 详情", EnumDesc.MESSAGE_DESC.getDesc(), voucherDetail.getVoucherDescription());
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
            IScene voucherPageScene = VoucherPageScene.builder().voucherName(voucherName).build();
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
            IScene voucherPageScene = VoucherPageScene.builder().voucherName(voucherName).build();
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
            CommonUtil.checkResult(voucherName + " 变更时间", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm"), voucherChangeRecord.getTime());
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.INVALIDED.getName(), voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
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
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
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
    @Test(description = "卡券管理--进行中的卡券增发，再撤回增发卡券，审核列表状态变为已撤回", priority = 1)
    public void voucherManage_data_31() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = additionalRecordScene.execute(visitor, true).getInteger("total");
            //增发卡券
            AddVoucherScene.builder().id(voucherId).addNumber(10).build().execute(visitor, true);
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
            RecallAdditionalScene.builder().id(additionalId).build().execute(visitor, true);
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
    @Test(description = "卡券管理--停止发放的卡券开始发放--卡券状态=进行中")
    public void voucherManage_data_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //停止发放
            ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build().execute(visitor, true);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.STOP.getName(), voucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.STOP.name(), voucherPage.getVoucherStatus());
            //开始发放
            ChangeProvideStatusScene.builder().id(voucherId).isStart(true).build().execute(visitor, true);
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
    @Test(description = "卡券管理--停止发放的卡券作废--状态=已作废&变更记录+1变更事项=作废卡券")
    public void voucherManage_data_11() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.STOP).buildVoucher().getVoucherId();
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = changeRecordScene.execute(visitor, true).getInteger("total");
            String voucherName = util.getVoucherName(voucherId);
            //作废卡券
            InvalidVoucherScene.builder().id(voucherId).build().execute(visitor, true);
            //校验卡券状态
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.name(), voucherPage.getVoucherStatus());
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.getName(), voucherPage.getVoucherStatusName());
            //校验变更记录列表数
            int newChangeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            VoucherChangeRecord voucherChangeRecord = util.collectBean(changeRecordScene, VoucherChangeRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 变更时间", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm"), voucherChangeRecord.getTime());
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.INVALIDED.getName(), voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
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
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
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
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
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
            CommonUtil.checkResult(voucherName + " 操作人", ADMINISTRATOR.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ADMINISTRATOR.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ADMINISTRATOR.getPhone(), voucherChangeRecord.getOperateSaleAccount());
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
            IScene scene = VoucherPageScene.builder().voucherStatus(VoucherStatusEnum.SELL_OUT.name()).build();
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
            user.loginPc(ADMINISTRATOR);
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
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.COMMON.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
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
            user.loginPc(ADMINISTRATOR);
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
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.COMMON.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
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
            user.loginPc(ADMINISTRATOR);
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
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.COMMON.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
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
            user.loginPc(ADMINISTRATOR);
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
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.COMMON.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
            Preconditions.checkArgument(voucherSendRecord.getCustomerLabelName().contains(CustomerLabelTypeEnum.APPLET.getTypeName()), "领取人标签" + voucherSendRecord.getCustomerLabelName());
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
            user.loginPc(ADMINISTRATOR);
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
            CommonUtil.checkResult(voucherName + " 作废后作废记录列表数", voucherInvalidPages.size() + 1, newVoucherInvalidPages.size());
            CommonUtil.checkResult(voucherName + " 作废后领取人电话", APPLET_USER_ONE.getPhone(), newVoucherInvalidPages.get(0).getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 作废后作废人姓名", ADMINISTRATOR.getName(), newVoucherInvalidPages.get(0).getInvalidName());
            CommonUtil.checkResult(voucherName + " 作废后作废人电话", ADMINISTRATOR.getPhone(), newVoucherInvalidPages.get(0).getInvalidPhone());
            CommonUtil.checkResult(voucherName + " 作废后作废说明", EnumDesc.INVALID_REASON.getDesc(), newVoucherInvalidPages.get(0).getInvalidDescription());
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
            IScene scene = VoucherPageScene.builder().build();
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
            IScene scene = VoucherPageScene.builder().build();
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
            IScene scene = VoucherPageScene.builder().build();
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
            IScene scene = VoucherPageScene.builder().build();
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
            //获取最新发出卡券的code
            String voucherCode = util.getVoucherCode(voucherId);
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
            IScene voucherVerificationScene = VoucherVerificationScene.builder().id(String.valueOf(appletVoucherId)).verificationCode(code).build();
            visitor.invokeApi(voucherVerificationScene);
            //核销之后数据
            user.loginPc(ADMINISTRATOR);
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
            user.loginPc(ADMINISTRATOR);
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
            user.loginPc(ADMINISTRATOR);
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
    @Test(description = "卡券管理--新建卡券--卡券名称异常")
    public void voucherManage_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumDesc.MESSAGE_DESC.getDesc(), "1", null, ""};
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
            String[] strings = {EnumDesc.ARTICLE_DESC_500.getDesc()};
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
            user.loginPc(ADMINISTRATOR);
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
            user.loginPc(ADMINISTRATOR);
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
            user.loginPc(ADMINISTRATOR);
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
            user.loginPc(ADMINISTRATOR);
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
            IScene voucherPageScene = VoucherPageScene.builder().voucherStatus(VoucherStatusEnum.INVALIDED.name()).build();
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
            IScene voucherPageScene = VoucherPageScene.builder().voucherStatus(VoucherStatusEnum.INVALIDED.name()).build();
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

    //ok
    @Test(description = "卡券申请--发出数量（首发）=【卡券表单】发行库存数量")
    public void voucherApply_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene voucherPageScene = VoucherPageScene.builder().build();
            List<VoucherPage> voucherPageList = util.collectBean(voucherPageScene, VoucherPage.class);
            voucherPageList.forEach(voucherPage -> {
                IScene voucherDetailScene = VoucherDetailScene.builder().id(voucherPage.getVoucherId()).build();
                VoucherDetail voucherDetail = JSONObject.toJavaObject(visitor.invokeApi(voucherDetailScene), VoucherDetail.class);
                Integer stock = voucherDetail.getStock();
                String voucherName = voucherPage.getVoucherName();
                IScene applyPageScene = ApplyPageScene.builder().name(voucherName).build();
                List<ApplyPage> applyPageList = util.collectBean(applyPageScene, ApplyPage.class);
                Integer num = Objects.requireNonNull(applyPageList.stream().filter(e -> e.getName().equals(voucherName) && e.getApplyTypeName().equals(ApplyTypeEnum.VOUCHER.getName())).findFirst().orElse(null)).getNum();
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
            IScene voucherPageScene = VoucherPageScene.builder().build();
            List<VoucherPage> voucherPageList = util.collectBean(voucherPageScene, VoucherPage.class);
            voucherPageList.forEach(voucherPage -> {
                CommonUtil.valueView(voucherPage.getVoucherName());
                IScene voucherDetailScene = VoucherDetailScene.builder().id(voucherPage.getVoucherId()).build();
                VoucherDetail voucherDetail = JSONObject.toJavaObject(voucherDetailScene.execute(visitor, true), VoucherDetail.class);
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
            int total = verificationPeopleScene.execute(visitor, true).getInteger("total");
            String phone = util.getDistinctPhone();
            CreateVerificationPeopleScene.builder().verificationPersonName("异页打工人").verificationPersonPhone(phone).type(1).status(1).build().execute(visitor, true);
            int newTotal = verificationPeopleScene.execute(visitor, true).getInteger("total");
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
            int total = verificationPeopleScene.execute(visitor, true).getInteger("total");
            String phone = util.getDistinctPhone();
            CreateVerificationPeopleScene.builder().verificationPersonName("本司打工人").verificationPersonPhone(phone).type(0).status(1).build().execute(visitor, true);
            int newTotal = verificationPeopleScene.execute(visitor, true).getInteger("total");
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
            String message = VoucherVerificationScene.builder().id(String.valueOf(id)).verificationCode(code).build().execute(visitor, false).getString("message");
            String err = "核销码错误";
            CommonUtil.checkResult("核销卡券时核销码状态关闭", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            user.loginPc(ADMINISTRATOR);
            util.switchVerificationStatus(code, true);
            saveData("核销人员--卡券核销时将核销人状态关闭，提示核销失败");
        }
    }

    //ok
    @Test(description = "核销人员--创建异页核销,名称异常")
    public void verificationPeople_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, EnumDesc.ARTICLE_DESC.getDesc()};
            Arrays.stream(strings).forEach(name -> {
                String message = CreateVerificationPeopleScene.builder().verificationPersonName(name).verificationPersonPhone("13663366788").status(1).type(1).build().execute(visitor, false).getString("message");
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
            String[] strings = {null, EnumDesc.ARTICLE_DESC.getDesc()};
            Arrays.stream(strings).forEach(name -> {
                String message = CreateVerificationPeopleScene.builder().verificationPersonName(name).verificationPersonPhone("13663366788").status(1).type(1).build().execute(visitor, false).getString("message");
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
                String message = CreateVerificationPeopleScene.builder().verificationPersonName("打工人").verificationPersonPhone(phone).status(1).type(0).build().execute(visitor, false).getString("message");
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
            String message = CreateVerificationPeopleScene.builder().verificationPersonName("打工人").verificationPersonPhone(phone).status(1).type(0).build().execute(visitor, false).getString("message");
            String err = "手机号已存在";
            CommonUtil.checkResult("手机号格式为：" + phone, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,电话存在");
        }
    }

    //ok
    @Test(description = "套餐管理--创建套餐包含卡券列表数=卡券状态为进行中的列表数")
    public void packageManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int voucherListSize = ReceptionVoucherListScene.builder().build().execute(visitor, true).getJSONArray("list").size();
            IScene voucherPageScene = VoucherPageScene.builder().voucherStatus(VoucherStatusEnum.WORKING.name()).build();
            int voucherTotal = voucherPageScene.execute(visitor, true).getInteger("total");
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            //创建套餐前列表数量
            IScene packageFormPageScene = PackageFormPageScene.builder().build();
            int total = packageFormPageScene.execute(visitor, true).getInteger("total");
            //创建套餐
            JSONArray voucherArray = util.getVoucherArray(voucherId, 10);
            String packageName = util.createPackage(voucherArray, com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.CURRENT);
            Long packageId = util.getPackageId(packageName);
            //创建套餐后列表数量
            int newTotal = packageFormPageScene.execute(visitor, true).getInteger("total");
            CommonUtil.checkResult("创建套餐后套餐列表数", total + 1, newTotal);
            //列表内容校验
            PackagePage packagePage = util.collectBean(PackageFormPageScene.builder().packageName(packageName).build(), PackagePage.class).get(0);
            CommonUtil.checkResult(packageName + " 套餐价格", "49.99", packagePage.getPrice());
            CommonUtil.checkResult(packageName + " 套餐有效期", 30, packagePage.getValidity());
            CommonUtil.checkResult(packageName + " 套餐内含卡券数", 10, packagePage.getVoucherNumber());
            CommonUtil.checkResult(packageName + " 客户有效期", 1, packagePage.getCustomerUseValidity());
            CommonUtil.checkResult(packageName + " 审核状态", AuditStatusEnum.AUDITING.getName(), packagePage.getAuditStatusName());
            //审核通过
            AuditPackageStatusScene.builder().id(packageId).status(AuditStatusEnum.AGREE.name()).build().execute(visitor, true);
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
                String findCustomerName = SearchCustomerScene.builder().customerPhone(customerPhone).build().execute(visitor, true).getString("customer_name");
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
                CommonUtil.checkResultPlus(packagePage.getPackageName() + " 列表有效期", packagePage.getValidity(), "详情有效期", packageDetail.getValidity());
                CommonUtil.checkResultPlus(packagePage.getPackageName() + " 列表有效期", packagePage.getVoucherNumber(), "详情有效期", voucherCountSum);
                CommonUtil.checkResultPlus(packagePage.getPackageName() + " 列表客户套餐有效期", packagePage.getCustomerUseValidity(), "详情客户套餐有效期", packageDetail.getCustomerUseValidity());
                CommonUtil.logger(packagePage.getPackageName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐管理--套餐列表展示内容与套餐详情一致");
        }
    }

    //ok
    @Test(description = "套餐管理--购买套餐--下拉可选择的套餐数量=未取消&未过期的套餐数")
    public void packageManager_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene packageListScene = PackageListScene.builder().build();
            int packageListSize = packageListScene.execute(visitor, true).getJSONArray("list").size();
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
            IScene voucherPageScene = VoucherPageScene.builder().voucherStatus(VoucherStatusEnum.INVALIDED.name()).build();
            List<VoucherPage> voucherPageList = util.collectBean(voucherPageScene, VoucherPage.class);
            List<Long> voucherIdList = voucherPageList.stream().map(VoucherPage::getVoucherId).collect(Collectors.toList());
            IScene voucherListScene = ReceptionVoucherListScene.builder().build();
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
    @Test(description = "套餐表单--创建套餐，套餐名称异常")
    public void packageManager_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumDesc.VOUCHER_DESC.getDesc(), "1", null, ""};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Arrays.stream(strings).forEach(name -> {
                IScene scene = CreatePackageScene.builder().packageName(name).validity("30").packageDescription(util.getDesc())
                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType())).customerUseValidity(1)
                        .voucherList(voucherList).packagePrice(5000.00).status(true).shopIds(util.getShopIdList()).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = StringUtils.isEmpty(name) ? "套餐名称不能为空" : "套餐名称输入应大于2字小于20字";
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
            String[] strings = {null, "", EnumDesc.ARTICLE_DESC.getDesc()};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Arrays.stream(strings).forEach(desc -> {
                IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.BRAND)).validity("30").packageDescription(desc)
                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType())).customerUseValidity(1)
                        .voucherList(voucherList).packagePrice(5000.00).status(true).shopIds(util.getShopIdList()).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = StringUtils.isEmpty(desc) ? "套餐说明不能为空" : "套餐说明不能超过200字";
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
    @Test(description = "套餐表单--创建套餐，有效天数异常")
    public void packageManager_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, "", "2001", "0"};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Arrays.stream(strings).forEach(validity -> {
                IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.BRAND)).validity(validity).packageDescription(util.getDesc())
                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType())).customerUseValidity(1)
                        .voucherList(voucherList).packagePrice(5000.00).status(true).shopIds(util.getShopIdList()).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = StringUtils.isEmpty(validity) ? "套餐有效期不能为空" : "有效期请小于2000天";
                CommonUtil.checkResult("套餐有效期", err, message);
                CommonUtil.logger(validity);
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Arrays.stream(strings).forEach(subjectType -> {
                IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.BRAND)).validity("30").packageDescription(util.getDesc())
                        .subjectType(subjectType).subjectId(util.getSubjectDesc(util.getSubjectType())).customerUseValidity(1)
                        .voucherList(voucherList).packagePrice(5000.00).status(true).shopIds(util.getShopIdList()).build();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.BRAND)).validity("30")
                    .packageDescription(util.getDesc()).subjectType(UseRangeEnum.STORE.name())
                    .voucherList(voucherList).packagePrice(5000.00).status(true).shopIds(util.getShopIdList()).build();
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
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.BRAND)).validity("30")
                    .packageDescription(util.getDesc()).subjectType(UseRangeEnum.STORE.name()).packagePrice(5000.00)
                    .status(true).shopIds(util.getShopIdList()).build();
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
            Double[] doubles = {null, 100000000.01, -3.55};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            Arrays.stream(doubles).forEach(packagePrice -> {
                IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.BRAND)).validity("30")
                        .packageDescription(util.getDesc()).subjectType(UseRangeEnum.STORE.name()).packagePrice(packagePrice)
                        .status(true).shopIds(util.getShopIdList()).voucherList(voucherList).build();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.BRAND)).validity("30")
                    .packageDescription(util.getDesc()).subjectType(UseRangeEnum.STORE.name()).packagePrice(499.99)
                    .status(true).voucherList(voucherList).build();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.INVALIDED).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 10);
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.BRAND)).validity("30")
                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                    .packagePrice(499.99).status(true).voucherList(voucherList).shopIds(util.getShopIdList()).build();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 10);
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.BRAND)).validity("30")
                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                    .packagePrice(499.99).status(true).voucherList(voucherList).shopIds(util.getShopIdList()).build();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 10);
            IScene scene = CreatePackageScene.builder().packageName(util.createPackageName(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.BRAND)).validity("30")
                    .packageDescription(util.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                    .packagePrice(499.99).status(true).voucherList(voucherList).shopIds(util.getShopIdList()).build();
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
            String[] phones = {null, "", "11111111111", "1532152798", "13654973499", "010-8888888"};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 10);
            Arrays.stream(phones).forEach(phone -> {
                IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackageScene.builder().customerPhone(phone)
                        .carType(PackageUseTypeEnum.ALL_CAR.name()).voucherList(voucherList)
                        .expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc()).subjectType(util.getSubjectType())
                        .subjectId(util.getSubjectDesc(util.getSubjectType())).extendedInsuranceYear("1")
                        .extendedInsuranceCopies("1").type(1).build();
                String message = visitor.invokeApi(purchaseTemporaryPackageScene, false).getString("message");
                String err = StringUtils.isEmpty(phone) ? "客户手机号不能为空" : "客户不存在";
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 10);
            Arrays.stream(plateNumbers).forEach(plateNumber -> {
                IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackageScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                        .carType(PackageUseTypeEnum.RECEPTION_CAR.name()).plateNumber(plateNumber).voucherList(voucherList)
                        .expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc()).subjectType(util.getSubjectType())
                        .subjectId(util.getSubjectDesc(util.getSubjectType())).extendedInsuranceYear("1")
                        .extendedInsuranceCopies("1").type(1).build();
                String message = visitor.invokeApi(purchaseTemporaryPackageScene, false).getString("message");
                String err = StringUtils.isEmpty(plateNumber) ? "车牌号不可为空" : "车牌号格式不正确";
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 11);
            IScene purchaseTemporaryPackageScene = PurchaseTemporaryPackageScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).voucherList(voucherList).expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc())
                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType())).extendedInsuranceYear("1")
                    .extendedInsuranceCopies("1").type(1).build();
            String message = visitor.invokeApi(purchaseTemporaryPackageScene, false).getString("message");
            String err = voucherList == null ? "卡券列表不能为空" : "卡券数量不能超过10张";
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
                        .remark(EnumDesc.VOUCHER_DESC.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
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
    @Test(description = "套餐表单--购买套餐，有效日期异常")
    public void packageManager_system_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long packageId = util.getPackagePage(PackageStatusEnum.AGREE).getPackageId();
            String[] expiryDates = {null, "", "20001", "12.23"};
            Arrays.stream(expiryDates).forEach(expiryDate -> {
                IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                        .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).packagePrice("49.99").expiryDate("1").expiryDate(expiryDate)
                        .remark(EnumDesc.VOUCHER_DESC.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .extendedInsuranceYear(10).extendedInsuranceCopies(10).type(1).build();
                String message = visitor.invokeApi(purchaseFixedPackageScene, false).getString("message");
                String err = StringUtils.isEmpty(expiryDate) ? "有效期不能为空" : expiryDate.contains(".") ? "请求入参类型不正确" : "有效期不能查过2000天";
                CommonUtil.checkResult("有效日期为" + expiryDate, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--购买套餐，有效日期异常");
        }
    }

    //ok
    @Test(description = "套餐表单--购买套餐，套餐说明异常")
    public void packageManager_system_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long packageId = util.getPackagePage(PackageStatusEnum.AGREE).getPackageId();
            //购买固定套餐
            String[] remarks = {EnumDesc.ARTICLE_DESC.getDesc()};
            Arrays.stream(remarks).forEach(remark -> {
                IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(APPLET_USER_ONE.getPhone())
                        .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).packagePrice("49.99").expiryDate("1").expiryDate("10")
                        .remark(EnumDesc.VOUCHER_DESC.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
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
                        .remark(EnumDesc.VOUCHER_DESC.getDesc()).subjectType(subjectType).subjectId(util.getSubjectDesc(subjectType))
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
                    .remark(EnumDesc.VOUCHER_DESC.getDesc()).subjectType(com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.UseRangeEnum.STORE.getName())
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
                        .remark(EnumDesc.VOUCHER_DESC.getDesc()).subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 2);
            //购买临时套餐
            IScene temporaryScene = PurchaseTemporaryPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).voucherList(voucherList).expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc())
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.INVALIDED).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            JSONArray voucherList = util.getVoucherArray(voucherId, 2);
            //购买临时套餐
            IScene temporaryScene = PurchaseTemporaryPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).voucherList(voucherList).expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc())
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long packageId = util.editPackage(voucherId, 1);
            //购买固定套餐
            IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc())
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
            IScene switchPackageStatusScene = SwitchPackageStatusScene.builder().id(packageId).status(false).build();
            visitor.invokeApi(switchPackageStatusScene);
            //购买固定套餐
            IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc())
                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType())).packagePrice("49.99")
                    .extendedInsuranceYear(1).extendedInsuranceCopies(1).type(1).build();
            String message = visitor.invokeApi(purchaseFixedPackageScene, false).getString("message");
            String err = "套餐【" + packageName + "】不允许发放";
            CommonUtil.checkResult("购买已关闭套餐" + packageName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            IScene switchPackageStatusScene = SwitchPackageStatusScene.builder().id(packageId).status(true).build();
            visitor.invokeApi(switchPackageStatusScene);
            saveData("套餐表单--购买/赠送固定套餐时，将套餐关闭，确认时会有提示：此套餐已关闭");
        }
    }

    //ok
    @Test(description = "套餐表单--购买/赠送固定套餐时，套餐过期，确认时会有提示：此套餐已过期")
    public void packageManager_system_32() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherList = util.getVoucherArray(voucherId, 1);
            //过期套餐
            long packageId = util.getPackagePage(PackageStatusEnum.EXPIRED).getPackageId();
            String packageName = util.editPackageContainVoucher(packageId, voucherList);
            //购买固定套餐
            IScene purchaseFixedPackageScene = PurchaseFixedPackageScene.builder().customerPhone(EnumAccount.MARKETING_DAILY.getPhone())
                    .carType(PackageUseTypeEnum.ALL_CAR.name()).packageId(packageId).expiryDate("1").remark(EnumDesc.VOUCHER_DESC.getDesc())
                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType())).packagePrice("49.99")
                    .extendedInsuranceYear(1).extendedInsuranceCopies(1).type(1).build();
            String message = visitor.invokeApi(purchaseFixedPackageScene, false).getString("message");
            String err = "套餐【" + packageName + "】已过期";
            CommonUtil.checkResult("购买过期套餐" + packageName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--购买/赠送固定套餐时，套餐过期，确认时会有提示：此套餐已过期");
        }
    }

    //ok
    @Test(description = "套餐表单--修改套餐，套餐名称异常")
    public void packageManager_system_22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherArray = util.getVoucherArray(voucherId, 1);
            Long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String[] packageNames = {EnumDesc.VOUCHER_DESC.getDesc(), null, ""};
            Arrays.stream(packageNames).forEach(packageName -> {
                IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.VOUCHER_DESC.getDesc())
                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .voucherList(voucherArray).packagePrice("1.11").status(true).shopIds(util.getShopIdList())
                        .id(String.valueOf(packageId)).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = StringUtils.isEmpty(packageName) ? "套餐名称不能为空" : "套餐名称输入应大于2字小于20字";
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherArray = util.getVoucherArray(voucherId, 1);
            long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String packageName = util.getPackageName(packageId);
            long editPackageId = util.getPackagePage(PackageStatusEnum.AGREE).getPackageId();
            IScene editPackageScene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.VOUCHER_DESC.getDesc())
                    .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                    .voucherList(voucherArray).packagePrice("1.11").status(true).shopIds(util.getShopIdList())
                    .id(String.valueOf(editPackageId)).build();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherArray = util.getVoucherArray(voucherId, 1);
            long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String packageName = util.getPackageName(packageId);
            String[] contents = {EnumDesc.ARTICLE_DESC.getDesc(), null, ""};
            Arrays.stream(contents).forEach(content -> {
                IScene editPackageScene = EditPackageScene.builder().packageName(packageName).packageDescription(content)
                        .subjectType(util.getSubjectType()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .voucherList(voucherArray).packagePrice("1.11").status(true).shopIds(util.getShopIdList())
                        .id(String.valueOf(packageId)).build();
                String message = visitor.invokeApi(editPackageScene, false).getString("message");
                String err = StringUtils.isEmpty(content) ? "套餐说明不能为空" : "套餐说明不能超过200字";
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
                IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.MESSAGE_DESC.getDesc())
                        .subjectType(subjectType).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .voucherList(voucherArray).packagePrice("1.11").status(true).shopIds(util.getShopIdList())
                        .id(String.valueOf(packageId)).customerUseValidity(1).validity(1).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = StringUtils.isEmpty(subjectType) ? "主体类型不存在" : "主体类型不存在";
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
            IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.MESSAGE_DESC.getDesc())
                    .subjectType(UseRangeEnum.STORE.name()).validity(2000).voucherList(voucherArray).packagePrice("1.11")
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            JSONArray voucherArray = util.getVoucherArray(voucherId, 15);
            list.add(voucherArray);
            list.add(null);
            long packageId = util.getPackagePage(PackageStatusEnum.REFUSAL).getPackageId();
            String packageName = util.getPackageName(packageId);
            list.forEach(e -> {
                IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.MESSAGE_DESC.getDesc())
                        .subjectType(util.getSubjectType()).validity(2000).packagePrice("1.11").voucherList(e)
                        .status(true).shopIds(util.getShopIdList()).id(String.valueOf(packageId)).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = e == null ? "所选卡券不能为空" : "卡券数量不能超过10张";
                CommonUtil.checkResult("编辑所选卡券为：" + e, err, message);
                CommonUtil.logger(e);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("套餐表单--修改套餐，主体类型异常");
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
                IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.MESSAGE_DESC.getDesc())
                        .subjectType(util.getSubjectType()).validity(2000).packagePrice(price).voucherList(voucherArray)
                        .status(true).shopIds(util.getShopIdList()).id(String.valueOf(packageId)).build();
                String message = visitor.invokeApi(scene, false).getString("message");
                String err = StringUtils.isEmpty(price) ? "套餐价格不能为空" : "套餐价格不能大于100,000,000";
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
            IScene scene = EditPackageScene.builder().packageName(packageName).packageDescription(EnumDesc.MESSAGE_DESC.getDesc())
                    .subjectType(util.getSubjectType()).validity(2000).packagePrice("99.99").voucherList(voucherArray)
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
            user.loginPc(ADMINISTRATOR);
            PackagePage packagePage = util.getPackagePage(PackageStatusEnum.AGREE);
            packageId = packagePage.getPackageId();
            String packageName = packagePage.getPackageName();
            util.editPackage(packageId, util.getVoucherArray());
            //购买套餐
            util.buyFixedPackage(packageId, 1);
            //将套餐关闭
            IScene switchPackageStatusScene = SwitchPackageStatusScene.builder().id(packageId).status(false).build();
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
            user.loginPc(ADMINISTRATOR);
            IScene switchPackageStatusScene = SwitchPackageStatusScene.builder().id(packageId).status(true).build();
            visitor.invokeApi(switchPackageStatusScene);
            saveData("套餐表单--购买套餐，确认购买前，套餐状态改为关闭，再确认购买小程序会收到套餐/卡券");
        }
    }

    //ok
    @Test(description = "消息管理--推送消息含有一张卡券的消息，消息记录+1，卡券库存-1，发卡记录+1")
    public void messageManager_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            IScene messageFormPageScene = MessageFormPageScene.builder().build();
            int messageTotal = visitor.invokeApi(messageFormPageScene).getInteger("total");
            String pushTime = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 80), "yyyy-MM-dd HH:mm");
            util.pushMessage(0, false, voucherId);
            String sendStatusName = visitor.invokeApi(messageFormPageScene).getJSONArray("list").getJSONObject(0).getString("send_status_name");
            CommonUtil.checkResult("消息管理列表", messageTotal + 1, visitor.invokeApi(messageFormPageScene).getInteger("total"));
            CommonUtil.checkResult("发送状态", CustomMessageStatusEnum.SCHEDULING.getStatusName(), sendStatusName);
            sleep(90);
            JSONArray list = visitor.invokeApi(messageFormPageScene).getJSONArray("list");
            JSONObject jsonObject = Objects.requireNonNull(list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("push_time").equals(pushTime)).findFirst().orElse(null));
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
            String content = jsonObject.getString("content");
            CommonUtil.checkResult("内容", EnumDesc.MESSAGE_DESC.getDesc(), content);
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
                    .messageName(EnumDesc.MESSAGE_TITLE.getDesc()).messageContent(EnumDesc.MESSAGE_DESC.getDesc())
                    .ifSendImmediately(true).build();
            visitor.invokeApi(scene);
            IScene messageFormPageScene = MessageFormPageScene.builder().build();
            JSONArray array = visitor.invokeApi(messageFormPageScene).getJSONArray("list");
            List<JSONObject> jsonObjectList = array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("push_time").equals(pushTime)).collect(Collectors.toList());
            jsonObjectList.forEach(jsonObject -> {
                CommonUtil.checkResult("内容", EnumDesc.MESSAGE_DESC.getDesc(), jsonObject != null ? jsonObject.getString("content") : null);
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            util.pushMessage(0, true, voucherId);
            //消息列表消息内容
            user.loginApplet(APPLET_USER_ONE);
            IScene appletMessageListScene = AppletMessageListScene.builder().size(20).build();
            JSONObject jsonObject = visitor.invokeApi(appletMessageListScene).getJSONArray("list").getJSONObject(0);
            Long id = jsonObject.getLong("id");
            IScene appletMessageDetailScene = AppletMessageDetailScene.builder().id(id).build();
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

    //ok
    @Test(description = "消息管理--推送消息，消息名称异常")
    public void messageManager_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] titles = {null, EnumDesc.MESSAGE_DESC.getDesc()};
            Arrays.stream(titles).forEach(title -> {
                IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                        .telList(getList(APPLET_USER_ONE.getPhone())).messageName(title)
                        .messageContent(EnumDesc.VOUCHER_DESC.getDesc()).type(1).build();
                String message = visitor.invokeApi(pushMessageScene, false).getString("message");
                String err = StringUtils.isEmpty(title) ? "消息名称不能为空" : "消息名称长度范围[1,20]";
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
            String[] contents = {null, EnumDesc.ARTICLE_DESC.getDesc()};
            Arrays.stream(contents).forEach(content -> {
                IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                        .telList(getList(APPLET_USER_ONE.getPhone())).messageName(EnumDesc.MESSAGE_TITLE.getDesc())
                        .messageContent(content).type(1).build();
                String message = visitor.invokeApi(pushMessageScene, false).getString("message");
                String err = StringUtils.isEmpty(content) ? "消息内容不能为空" : "消息内容长度范围[2,200]";
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.INVALIDED).buildVoucher().getVoucherId();
            IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                    .telList(getList(APPLET_USER_ONE.getPhone())).messageName(EnumDesc.MESSAGE_TITLE.getDesc())
                    .messageContent(EnumDesc.VOUCHER_DESC.getDesc()).type(0).voucherOrPackageList(getList(voucherId))
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                    .telList(getList(APPLET_USER_ONE.getPhone())).messageName(EnumDesc.MESSAGE_TITLE.getDesc())
                    .messageContent(EnumDesc.VOUCHER_DESC.getDesc()).type(0).voucherOrPackageList(getList(voucherId))
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
            IScene switchPackageStatusScene = SwitchPackageStatusScene.builder().id(packageId).status(false).build();
            visitor.invokeApi(switchPackageStatusScene);
            IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                    .telList(getList(APPLET_USER_ONE.getPhone())).messageName(EnumDesc.MESSAGE_TITLE.getDesc())
                    .messageContent(EnumDesc.VOUCHER_DESC.getDesc()).type(1).voucherOrPackageList(getList(packageId))
                    .useDays("10").ifSendImmediately(true).build();
            String message = visitor.invokeApi(pushMessageScene, false).getString("message");
            String err = "套餐不允许发送，请重新选择";
            CommonUtil.checkResult("推送已关闭套餐", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            IScene switchPackageStatusScene = SwitchPackageStatusScene.builder().id(packageId).status(true).build();
            visitor.invokeApi(switchPackageStatusScene);
            saveData("消息管理--选择套餐时，套餐被关闭，提交时提示：套餐不允许发送，请重新选择");
        }
    }

    //ok
    @Test(description = "消息管理--选择套餐时，套餐过期，提交时提示：套餐不允许发送，请重新选择")
    public void messageManager_system_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //发消息
            long packageId = util.getPackagePage(PackageStatusEnum.EXPIRED).getPackageId();
            String packageName = util.getPackageName(packageId);
            IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                    .telList(getList(APPLET_USER_ONE.getPhone())).messageName(EnumDesc.MESSAGE_TITLE.getDesc())
                    .messageContent(EnumDesc.VOUCHER_DESC.getDesc()).type(1).voucherOrPackageList(getList(packageId))
                    .useDays("10").ifSendImmediately(true).build();
            String message = visitor.invokeApi(pushMessageScene, false).getString("message");
            String err = "已过套餐有效期";
            CommonUtil.checkResult("推送过期套餐" + packageName, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("消息管理--选择套餐时，套餐被关闭，提交时提示：套餐不允许发送，请重新选择");
        }
    }

    //ok
    @Test(description = "消息管理--选择套餐时，套餐内包含无库存卡券，提交时提示：套餐不允许发送，请重新选择")
    public void messageManager_system_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            Long packageId = util.editPackage(voucherId, 1);
            //发消息
            IScene pushMessageScene = PushMessageScene.builder().pushTarget(AppletPushTargetEnum.PERSONNEL_CUSTOMER.getId())
                    .telList(getList(APPLET_USER_ONE.getPhone())).messageName(EnumDesc.MESSAGE_TITLE.getDesc())
                    .messageContent(EnumDesc.VOUCHER_DESC.getDesc()).type(1).voucherOrPackageList(getList(packageId))
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

    //bug
    @Test(description = "权益列表--修改权益，applet与pc所见内容一致")
    public void vipMarketing_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] desc = {EnumDesc.FAULT_DESCRIPTION.getDesc(), EnumDesc.MESSAGE_TITLE.getDesc(), EnumDesc.INVALID_REASON.getDesc()};
            JSONObject equityPageResponse = EquityPageScene.builder().build().execute(visitor, true).getJSONArray("list").getJSONObject(0);
            int equityId = equityPageResponse.getInteger("equity_id");
            String description = Arrays.stream(desc).filter(e -> !e.equals(equityPageResponse.getString("description"))).findFirst().orElse(equityPageResponse.getString("description"));
            EquityEditScene.builder().awardCount(4).equityId(equityId).description(description).build().execute(visitor, true);
            JSONObject newEquityPageResponse = EquityPageScene.builder().build().execute(visitor, true).getJSONArray("list").getJSONObject(0);
            String newDescription = newEquityPageResponse.getString("description");
            CommonUtil.checkResult("pc修改过权益说明后", description, newDescription);
            user.loginApplet(APPLET_USER_ONE);
            JSONObject response = MemberCenterHomePageScene.builder().build().execute(visitor, true);
            int vipType = response.getInteger("vip_type");
            JSONObject equity = response.getJSONArray("equity_list").getJSONObject(0);
            String equityName = equity.getString("equity_name");
            CommonUtil.checkResultPlus("pc权益服务类型", VipTypeEnum.COMMON.getId(), "applet权益服务类型", vipType);
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
            Integer equityId = visitor.invokeApi(equityPageScene).getJSONArray("list").getJSONObject(0).getInteger("equity_id");
            //关闭权益
            IScene equityStartOrCloseScene = EquityStartOrCloseScene.builder().equityId(equityId).equityStatus(UseStatusEnum.DISABLE.name()).build();
            visitor.invokeApi(equityStartOrCloseScene);
            user.loginApplet(APPLET_USER_ONE);
            IScene memberCenterEquityListScene = MemberCenterHomePageScene.builder().build();
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
            Integer equityId = visitor.invokeApi(equityPageScene).getJSONArray("list").getJSONObject(0).getInteger("equity_id");
            //开启权益
            IScene equityStartOrCloseScene = EquityStartOrCloseScene.builder().equityId(equityId).equityStatus(UseStatusEnum.ENABLE.name()).build();
            visitor.invokeApi(equityStartOrCloseScene);
            user.loginApplet(APPLET_USER_ONE);
            IScene memberCenterEquityListScene = MemberCenterHomePageScene.builder().build();
            JSONObject jsonObject = visitor.invokeApi(memberCenterEquityListScene).getJSONArray("equity_list").getJSONObject(0);
            CommonUtil.checkResult(jsonObject.getString("equity_name") + "开启状态", UseStatusEnum.ENABLE.name(), jsonObject.getString("equity_status"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权益列表--开启权益，applet与pc所见内容一致");
        }
    }

    //ok
    @Test(description = "任务管理--修改分享内容，applet与pc所见内容一致")
    public void vipMarketing_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IVoucher voucher = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher();
            Integer voucherId = Math.toIntExact(voucher.getVoucherId());
            IScene shareManagerPageScene = ShareManagerPageScene.builder().build();
            JSONArray list = visitor.invokeApi(shareManagerPageScene).getJSONArray("list");
            list.stream().map(e -> (JSONObject) e).forEach(e -> {
                int id = e.getInteger("id");
                String businessType = e.getString("business_type");
                IScene shareManagerEditScene = ShareManagerEditScene.builder().id(id).awardCustomerRule("ONCE").taskExplain(EnumDesc.MESSAGE_TITLE.getDesc())
                        .awardScore(1000).awardCardVolumeId(voucherId).takeEffectType("DAY").dayNumber("2000").businessType(businessType).build();
                visitor.invokeApi(shareManagerEditScene);
            });
            user.loginApplet(APPLET_USER_ONE);
            JSONArray taskList = visitor.invokeApi(MemberCenterShareTaskListScene.builder().build()).getJSONArray("task_list");
            taskList.stream().map(e -> (JSONObject) e).forEach(e -> CommonUtil.checkResult(e.getString("task_name") + "的内容", EnumDesc.MESSAGE_TITLE.getDesc() + "+1000积分.", e.getString("award_description")));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("任务管理--修改分享内容，applet与pc所见内容一致");
        }
    }

    //ok
    @Test(description = "签到配置--修改签到配置，applet与pc所见内容一致")
    public void vipMarketing_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene signInConfigPageScene = SignInConfigPageScene.builder().build();
            JSONArray list = visitor.invokeApi(signInConfigPageScene).getJSONArray("list");
            int id = list.getJSONObject(0).getInteger("id");
            IScene signInConfigEditScene = SignInConfigEditScene.builder().signInConfigId(id).awardScore(999).explain(EnumDesc.VOUCHER_DESC.getDesc()).build();
            visitor.invokeApi(signInConfigEditScene);
            user.loginApplet(APPLET_USER_ONE);
            IScene messageFormPageScene = MemberCenterSignInDetailScene.builder().build();
            Integer signInScore = visitor.invokeApi(messageFormPageScene).getInteger("sign_in_score");
            CommonUtil.checkResultPlus("pc修改后签到积分为", 999, "小程序签到可得积分为", signInScore);
            user.loginPc(ADMINISTRATOR);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("签到配置--修改签到配置，applet与pc所见内容一致");
        }
    }

    //ok
    @Test(description = "签到配置--签到配置修改，变更记录+1，变更积分为修改的积分&操作时间为当前时间&操作账号为当前账号&备注为修改的详情")
    public void vipMarketing_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = SignInConfigPageScene.builder().build().execute(visitor, true).getJSONArray("list").getJSONObject(0);
            int signInConfigId = response.getInteger("id");
            int awardScore = response.getInteger("award_score") >= 1999 ? 1 : response.getInteger("award_score");
            int recordTotal = SignInConfigChangeRecordScene.builder().signInConfigId(signInConfigId).build().execute(visitor, true).getInteger("total");
            //变更积分&说明
            SignInConfigEditScene.builder().signInConfigId(signInConfigId).awardScore(awardScore + 1).explain(EnumDesc.FAULT_DESCRIPTION.getDesc()).build().execute(visitor, true);
            //变更后列表数
            int newRecordTotal = SignInConfigChangeRecordScene.builder().signInConfigId(signInConfigId).build().execute(visitor, true).getInteger("total");
            CommonUtil.checkResult("签到任务变更记录总数", recordTotal + 1, newRecordTotal);
            //变更内容
            JSONObject newResponse = SignInConfigChangeRecordScene.builder().build().execute(visitor, true).getJSONArray("list").getJSONObject(0);
            CommonUtil.checkResult("操作员手机号", ADMINISTRATOR.getPhone(), newResponse.getString("operate_phone"));
            CommonUtil.checkResult("操作时间", DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm"), newResponse.getString("operate_date"));
            CommonUtil.checkResult("变更积分", awardScore + 1, newResponse.getInteger("change_score"));
            CommonUtil.checkResult("变更备注", EnumDesc.FAULT_DESCRIPTION.getDesc(), newResponse.getString("change_remark"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("签到配置--签到配置修改，变更记录+1，变更积分为修改的积分&操作时间为当前时间&操作账号为当前账号&备注为修改的详情");
        }
    }

    //ok
    @Test(description = "权益列表--修改生日积分，积分异常")
    public void vipMarketing_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene equityPageScene = EquityPageScene.builder().build();
            Integer equityId = visitor.invokeApi(equityPageScene).getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getString("equity_name").equals("生日积分")).map(e -> e.getInteger("equity_id")).findFirst().orElse(0);
            Integer[] awardCounts = {null, 10001};
            Arrays.stream(awardCounts).forEach(awardCount -> {
                IScene equityEditScene = EquityEditScene.builder().awardCount(awardCount).equityId(equityId).description(EnumDesc.FAULT_DESCRIPTION.getDesc()).build();
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
    @Test(description = "权益列表--修改生日积分，描述异常")
    public void vipMarketing_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene equityPageScene = EquityPageScene.builder().build();
            Integer equityId = visitor.invokeApi(equityPageScene).getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getString("equity_name").equals("生日积分")).map(e -> e.getInteger("equity_id")).findFirst().orElse(0);
            //修改权益
            String[] descriptions = {null, EnumDesc.MESSAGE_DESC.getDesc()};
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
    @Test(description = "权益列表--修改生日积分，积分异常")
    public void vipMarketing_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene equityPageScene = EquityPageScene.builder().build();
            Integer equityId = visitor.invokeApi(equityPageScene).getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getString("equity_name").equals("免费洗车")).map(e -> e.getInteger("equity_id")).findFirst().orElse(0);
            //修改权益
            Integer[] awardCounts = {null, 100};
            Arrays.stream(awardCounts).forEach(awardCount -> {
                IScene equityEditScene = EquityEditScene.builder().awardCount(awardCount).equityId(equityId).description(EnumDesc.FAULT_DESCRIPTION.getDesc()).build();
                String message = visitor.invokeApi(equityEditScene, false).getString("message");
                String err = awardCount == null ? "奖励数不能为空" : "次数范围只能在1-99";
                CommonUtil.checkResult("积分为" + awardCount, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("权益列表--修改生日积分，积分异常");
        }
    }

    //ok
    @Test(description = "任务管理--修改分享内容，说明异常")
    public void vipMarketing_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IVoucher voucher = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher();
            Integer voucherId = Math.toIntExact(voucher.getVoucherId());
            IScene shareManagerPageScene = ShareManagerPageScene.builder().build();
            JSONArray list = visitor.invokeApi(shareManagerPageScene).getJSONArray("list");
            List<Integer> ids = list.stream().map(e -> (JSONObject) e).map(e -> e.getInteger("id")).collect(Collectors.toList());
            ids.forEach(id -> {
                String[] taskExplains = {null, EnumDesc.ARTICLE_DESC.getDesc()};
                Arrays.stream(taskExplains).forEach(desc -> {
                    IScene shareManagerEditScene = ShareManagerEditScene.builder().id(id).taskExplain(desc).dayNumber("9")
                            .awardScore(1000).awardCustomerRule("ONCE").awardCardVolumeId(voucherId).takeEffectType(TaskEffectTypeEnum.DAY.name()).build();
                    String message = visitor.invokeApi(shareManagerEditScene, false).getString("message");
                    String err = StringUtils.isEmpty(desc) ? "说明不能为空" : "备注只能在0-20";
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
    @Test(description = "任务管理--修改分享内容，积分异常")
    public void vipMarketing_system_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IVoucher voucher = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher();
            Integer voucherId = Math.toIntExact(voucher.getVoucherId());
            IScene shareManagerPageScene = ShareManagerPageScene.builder().build();
            JSONArray list = visitor.invokeApi(shareManagerPageScene).getJSONArray("list");
            List<Integer> ids = list.stream().map(e -> (JSONObject) e).filter(e -> !e.getString("business_type").equals(AppletCodeBusinessTypeEnum.ACTIVITY_APPLY_PRIORITY.getKey())).map(e -> e.getInteger("id")).collect(Collectors.toList());
            ids.forEach(id -> {
                Integer[] awardScores = {null};
                Arrays.stream(awardScores).forEach(awardScore -> {
                    IScene shareManagerEditScene = ShareManagerEditScene.builder().id(id).taskExplain(EnumDesc.MESSAGE_TITLE.getDesc())
                            .awardScore(awardScore).awardCustomerRule("ONCE").awardCardVolumeId(voucherId).takeEffectType(TaskEffectTypeEnum.DAY.name()).build();
                    String message = visitor.invokeApi(shareManagerEditScene, false).getString("message");
                    String err = "积分不能为空";
                    CommonUtil.checkResult("积分为" + awardScore, err, message);
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("任务管理--修改分享内容，积分异常");
        }
    }

    //ok
    @Test(description = "洗车管理--每人的洗车剩余次数+调整记录增加次数=调整记录调整后次数")
    public void vipMarketing_system_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene adjustNumberRecordScene = AdjustNumberRecordScene.builder().build();
            int washRecordTotal = visitor.invokeApi(adjustNumberRecordScene).getInteger("total");
            //增加洗车次数
            IScene adjustNumberScene = AdjustNumberScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).adjustNumber("3").remark(EnumDesc.MESSAGE_TITLE.getDesc()).build();
            visitor.invokeApi(adjustNumberScene);
            //调整记录次数
            int newWashRecordTotal = visitor.invokeApi(adjustNumberRecordScene).getInteger("total");
            IScene newAdjustNumberRecordScene = AdjustNumberRecordScene.builder().customerPhone(APPLET_USER_ONE.getPhone()).build();
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
            saveData("洗车管理--每人的洗车剩余次数+调整记录增加次数=调整记录调整后次数");
        }
    }

    //----------------------------------------------------------活动管理-------------------------------------------------
    private <T> List<T> getList(T str) {
        List<T> list = new ArrayList<>();
        list.add(str);
        return list;
    }
}


