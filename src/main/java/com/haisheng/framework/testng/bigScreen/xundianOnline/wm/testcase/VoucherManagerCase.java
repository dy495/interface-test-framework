package com.haisheng.framework.testng.bigScreen.xundianOnline.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet.AppletVoucher;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherFormVoucherPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherInvalidPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.financial.ApplyTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.enumerator.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.generator.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted.AppletMessageDetailScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted.AppletMessageListScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.record.PushMsgPageScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.vouchermanage.*;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.UserUtil;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 营销管理测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class VoucherManagerCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.INS_ONLINE;
    private static final AccountEnum ALL_AUTHORITY = AccountEnum.YUE_XIU_DAILY;
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
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
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
        logger.debug("beforeMethod");
        user.loginPc(ALL_AUTHORITY);
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    //ok
    @Test(description = "卡券管理--创建卡券--列表数+1&卡券状态=待审批；【卡券审批】列表数+1&审批状态=审批中&申请类型=首发")
    public void voucherManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(VoucherTypeEnum.values()).forEach(anEnum -> {
                if (!anEnum.equals(VoucherTypeEnum.CUSTOM)) {
                    int voucherTotal = VoucherFormVoucherPageScene.builder().build().invoke(visitor).getInteger("total");
                    int applyTotal = ApplyPageScene.builder().build().invoke(visitor).getInteger("total");
                    //创建卡券
                    String voucherName = util.createVoucher(1, anEnum);
                    VoucherFormVoucherPageScene.builder().voucherName(voucherName).build().invoke(visitor);
                    JSONObject data = VoucherDetailScene.builder().id(util.getVoucherId(voucherName)).build().invoke(visitor);
                    VoucherDetail voucherDetail = JSONObject.toJavaObject(data, VoucherDetail.class);
                    CommonUtil.checkResult(voucherName + "描述", util.getDesc(), voucherDetail.getVoucherDescription());
                    CommonUtil.checkResult(voucherName + " 类型", anEnum.name(), voucherDetail.getCardType());
                    CommonUtil.checkResult(voucherName + " 类型名称", anEnum.getDesc(), voucherDetail.getCardTypeName());
                    if (anEnum.getDesc().equals(VoucherTypeEnum.COMMODITY_EXCHANGE.getDesc())) {
                        CommonUtil.checkResult(voucherName + " 可兑换商品", "兑换布加迪威龙一辆", voucherDetail.getExchangeCommodityName());
                        CommonUtil.checkResult(voucherName + " 成本价格", "49.99", voucherDetail.getCost());
                    } else if (anEnum.getDesc().equals(VoucherTypeEnum.COUPON.getDesc())) {
                        CommonUtil.checkResult(voucherName + " 门槛价格", 999.99, voucherDetail.getThresholdPrice());
                        CommonUtil.checkResult(voucherName + " 折扣", 2.50, voucherDetail.getDiscount());
                        CommonUtil.checkResult(voucherName + " 最多优惠", 99.99, voucherDetail.getMostDiscount());
                        CommonUtil.checkResult(voucherName + " 成本价格", "99.99", voucherDetail.getCost());
                    } else if (anEnum.getDesc().equals(VoucherTypeEnum.FULL_DISCOUNT.getDesc())) {
                        CommonUtil.checkResult(voucherName + " 面值", 49.99, voucherDetail.getParValue());
                        CommonUtil.checkResult(voucherName + " 门槛价格", 999.99, voucherDetail.getThresholdPrice());
                        CommonUtil.checkResult(voucherName + " 成本价格", "49.99", voucherDetail.getCost());
                    }
                    //卡券列表+1
                    int newVoucherTotal = VoucherFormVoucherPageScene.builder().build().invoke(visitor).getInteger("total");
                    CommonUtil.checkResult("卡券列表数", voucherTotal + 1, newVoucherTotal);
                    //卡券状态=待审批
                    VoucherPage voucherPage = util.getVoucherPage(voucherName);
                    CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.WAITING.getName(), voucherPage.getVoucherStatusName());
                    CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.WAITING.name(), voucherPage.getVoucherStatus());
                    //卡券变更记录不+1
                    Long voucherId = util.getVoucherId(voucherName);
                    int newTotal = ChangeRecordScene.builder().voucherId(voucherId).build().invoke(visitor).getInteger("total");
                    CommonUtil.checkResult(voucherName + " 变更记录数", 0, newTotal);
                    //审批列表+1
                    int newApplyTotal = ApplyPageScene.builder().build().invoke(visitor).getInteger("total");
                    CommonUtil.checkResult(voucherName + " 审批列表数", applyTotal + 1, newApplyTotal);
                    //审批状态=审批中
                    ApplyPage applyPage = util.getAuditingApplyPage(voucherName);
                    CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
                    CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.VOUCHER.getName(), applyPage.getApplyTypeName());
                    CommonUtil.checkResult(voucherName + " 审批发出数量", 1, applyPage.getNum());
                    CommonUtil.logger(voucherName);
                }
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--创建自定义卡券--列表数+1&卡券状态=待审批；【卡券审批】列表数+1&审批状态=审批中&申请类型=首发");
        }
    }

    @AfterClass
    @Test(description = "清理卡券")
    public void cleanVoucher() {
        Arrays.stream(VoucherTypeEnum.values()).forEach(anEnum -> {
            IScene scene = VoucherFormVoucherPageScene.builder().voucherName(anEnum.getDesc()).voucherStatus(VoucherStatusEnum.WAITING.name()).build();
            List<VoucherFormVoucherPageBean> voucherFormVoucherPageBeanList = util.collectBean(scene, VoucherFormVoucherPageBean.class);
            List<Long> voucherIdList = voucherFormVoucherPageBeanList.stream().map(VoucherFormVoucherPageBean::getVoucherId).collect(Collectors.toList());
            voucherIdList.forEach(voucherId -> {
                RecallVoucherScene.builder().id(voucherId).build().invoke(visitor);
                DeleteVoucherScene.builder().id(voucherId).build().invoke(visitor);
            });
        });
    }

    //ok
    @Test(description = "卡券管理--编辑待审批卡券--卡券信息更新")
    public void voucherManage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待审批的卡券id
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = visitor.invokeApi(changeRecordScene).getInteger("total");
            String newVoucherName = util.createVoucherName();
            List<Long> shopIds = util.getShopIdList(2);
            //编辑
            IScene scene = EditVoucherScene.builder().id(voucherId).voucherName(newVoucherName).voucherDescription(EnumDesc.DESC_BETWEEN_40_50.getDesc()).shopIds(shopIds).shopType(1).selfVerification(false).build();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.RECALL).buildVoucher().getVoucherId();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.REJECT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //删除
            visitor.invokeApi(DeleteVoucherScene.builder().id(voucherId).build());
            //校验
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build();
            JSONArray list = visitor.invokeApi(voucherPageScene).getJSONArray("list");
            int count = (int) list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_name").equals(voucherName)).count();
            CommonUtil.checkResult(voucherName + " 结果列表", 0, count);
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
            Long voucherId = new VoucherGenerator.Builder().status(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId();
            //暂停发放
            ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build().invoke(visitor);
            //校验
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP.getName(), voucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP.name(), voucherPage.getVoucherStatus());
            //打开
            ChangeProvideStatusScene.builder().id(voucherId).isStart(true).build().invoke(visitor);
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRoleName(), voucherChangeRecord.getOperateSaleRole());
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRoleName(), voucherChangeRecord.getOperateSaleRole());
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //停止发放
            ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build().invoke(visitor);
            VoucherPage voucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.STOP.getName(), voucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.STOP.name(), voucherPage.getVoucherStatus());
            //开始发放
            ChangeProvideStatusScene.builder().id(voucherId).isStart(true).build().invoke(visitor);
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.STOP).buildVoucher().getVoucherId();
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = changeRecordScene.invoke(visitor).getInteger("total");
            String voucherName = util.getVoucherName(voucherId);
            //作废卡券
            InvalidVoucherScene.builder().id(voucherId).build().invoke(visitor);
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
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRoleName(), voucherChangeRecord.getOperateSaleRole());
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.STOP).buildVoucher().getVoucherId();
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
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRoleName(), voucherChangeRecord.getOperateSaleRole());
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
            Long voucherId = new VoucherGenerator.Builder().status(VoucherStatusEnum.SELL_OUT).visitor(visitor).buildVoucher().getVoucherId();
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
    @Test(description = "卡券管理--已售罄的卡券作废--状态=已作废&变更记录+1变更事项=作废卡券", enabled = false)
    public void voucherManage_data_14() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
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
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRoleName(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("卡券管理--已售罄的卡券作废--状态=已作废&变更记录+1变更事项=作废卡券");
        }
    }

    //ok
    @Test(description = "卡券管理--已售罄的卡券增发--审批通过状态", enabled = false)
    public void voucherManage_data_15() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
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
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRoleName(), voucherChangeRecord.getOperateSaleRole());
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
    @Test(description = "卡券管理--作废某人的卡券，小程序上我的卡券-1&作废记录+1")
    public void voucherManage_data_24() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            util.pushMessage(0, true, voucherId);
            //作废前数据
            visitor.login(APPLET_USER_ONE.getToken());
            int voucherCherNum = util.getAppletVoucherNum();
            user.loginPc(ALL_AUTHORITY);
            List<VoucherInvalidPageBean> voucherInvalidPages = util.getVoucherInvalidList(voucherId);
            IScene voucherInfoScene = VoucherInfoScene.builder().id(voucherId).build();
            int totalInvalid = visitor.invokeApi(voucherInfoScene).getInteger("total_invalid");
            //作废
            List<VoucherSendRecord> voucherSendRecords = util.getVoucherSendRecordList(voucherId);
            Long recordId = voucherSendRecords.get(0).getId();
            String voucherCode = voucherSendRecords.get(0).getVoucherCode();
            IScene scene = InvalidCustomerVoucherScene.builder().id(recordId).invalidReason(EnumDesc.DESC_BETWEEN_10_15.getDesc()).build();
            visitor.invokeApi(scene);
            //作废后数据
            List<VoucherInvalidPageBean> newVoucherInvalidPages = util.getVoucherInvalidList(voucherId);
            CommonUtil.checkResult(voucherName + " 作废后作废记录列表数", voucherInvalidPages.size() + 1, newVoucherInvalidPages.size());
            CommonUtil.checkResult(voucherName + " 作废后领取人电话", APPLET_USER_ONE.getPhone(), newVoucherInvalidPages.get(0).getCustomerPhone());
            CommonUtil.checkResult(voucherName + " 作废后作废人姓名", ALL_AUTHORITY.getName(), newVoucherInvalidPages.get(0).getInvalidName());
            CommonUtil.checkResult(voucherName + " 作废后作废人电话", ALL_AUTHORITY.getPhone(), newVoucherInvalidPages.get(0).getInvalidPhone());
            CommonUtil.checkResult(voucherName + " 作废后作废说明", EnumDesc.DESC_BETWEEN_10_15.getDesc(), newVoucherInvalidPages.get(0).getInvalidDescription());
            CommonUtil.checkResult(voucherCode + " 作废后共作废数", totalInvalid + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_invalid"));
            visitor.login(APPLET_USER_ONE.getToken());
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
    @Test(description = "卡券管理--转移卡券，转移人小程序我的卡券数量-1，被转移人我的卡券数量+1")
    public void voucherManage_data_30() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已使用的卡券列表
            visitor.login(APPLET_USER_ONE.getToken());
            int transferVoucherNum = util.getAppletVoucherNum();
            int transferMessageNum = util.getAppletMessageNum();
            AppletVoucher appletVoucher = util.getAppletVoucher(VoucherUseStatusEnum.NEAR_EXPIRE);
            Long id = appletVoucher.getId();
            String voucherName = appletVoucher.getTitle();
            visitor.login(APPLET_USER_TWO.getToken());
            int receiveVoucherNum = util.getAppletVoucherNum();
            //转移
            user.loginPc(ALL_AUTHORITY);
            int messageNum = visitor.invokeApi(PushMsgPageScene.builder().build()).getInteger("total");
            IScene transferScene = TransferScene.builder().transferPhone(APPLET_USER_ONE.getPhone()).receivePhone(APPLET_USER_TWO.getPhone()).voucherIds(getList(id)).build();
            visitor.invokeApi(transferScene);
            visitor.login(APPLET_USER_ONE.getToken());
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
            visitor.login(APPLET_USER_TWO.getToken());
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
    @Test(description = "卡券管理--进行中的卡券增发，再撤回增发卡券，审核列表状态变为已撤回", dependsOnMethods = "voucherManage_system_1")
    public void voucherManage_data_31() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = additionalRecordScene.invoke(visitor).getInteger("total");
            //增发卡券
            AddVoucherScene.builder().id(voucherId).addNumber(10).build().invoke(visitor);
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
            RecallAdditionalScene.builder().id(additionalId).build().invoke(visitor);
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
                IScene scene = CreateScene.builder().voucherName(name)
                        .cardType(VoucherTypeEnum.COMMODITY_EXCHANGE.name())
                        .stock(1000).voucherDescription(util.getDesc())
                        .shopType(0)
                        .shopIds(util.getShopIdList())
                        .selfVerification(true).build();
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
                IScene scene = CreateScene.builder().voucherName(util.createVoucherName())
                        .cardType(VoucherTypeEnum.COMMODITY_EXCHANGE.name()).parValue(99.99).voucherDescription(desc).stock(1000)
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
    @Test(description = "卡券管理--新建卡券--库存数量异常情况")
    public void voucherManage_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer[] integers = {1000000000, null, -100, Integer.MAX_VALUE};
            Arrays.stream(integers).forEach(stock -> {
                IScene scene = CreateScene.builder().voucherName(util.createVoucherName())
                        .cardType(VoucherTypeEnum.COMMODITY_EXCHANGE.name()).parValue(99.99)
                        .voucherDescription(util.getDesc()).stock(stock)
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
                IScene scene = CreateScene.builder().voucherName(util.createVoucherName())
                        .cardType(VoucherTypeEnum.COMMODITY_EXCHANGE.name()).parValue(99.99)
                        .voucherDescription(util.getDesc()).stock(1000)
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
    @Test(description = "卡券管理--新建卡券--选择门店异常")
    public void voucherManage_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CreateScene.builder().voucherName(util.createVoucherName())
                    .cardType(VoucherTypeEnum.FULL_DISCOUNT.name()).isThreshold(false).parValue(99.99)
                    .voucherDescription(util.getDesc()).stock(1000)
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
    @Test(description = "卡券管理--转移已使用的卡券，提示：卡券【XXXX】已被使用或已过期，请重新选择！", enabled = false)
    public void voucherManage_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已使用的卡券列表
            visitor.login(APPLET_USER_ONE.getToken());
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
    @Test(description = "卡券管理--转移已过期的卡券，提示：卡券【XXXX】已被使用或已过期，请重新选择！", enabled = false)
    public void voucherManage_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取已使用的卡券列表
            visitor.login(APPLET_USER_ONE.getToken());
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
    @Test(description = "卡券管理--卡券转移，转移账号异常", enabled = false)
    public void voucherManage_system_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] phones = {"13654973499"};
            //获取已过期的卡券列表
            visitor.login(APPLET_USER_ONE.getToken());
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
    @Test(description = "卡券管理--卡券转移，接收账号异常", enabled = false)
    public void voucherManage_system_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] phones = {"13654973499", APPLET_USER_ONE.getPhone()};
            //获取已过期的卡券列表
            visitor.login(APPLET_USER_ONE.getToken());
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
            visitor.login(APPLET_USER_ONE.getToken());
            int nearExpireNum = util.getAppletVoucherNum(VoucherUseStatusEnum.NEAR_EXPIRE);
            int normalNum = util.getAppletVoucherNum(VoucherUseStatusEnum.NORMAL);
            CommonUtil.valueView(nearExpireNum, normalNum);
            CommonUtil.checkResultPlus("可转移列表数", voucherNum, "小程序可用卡券数", nearExpireNum + normalNum);
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
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
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
                VoucherDetail voucherDetail = JSONObject.toJavaObject(voucherDetailScene.invoke(visitor), VoucherDetail.class);
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

    private <T> List<T> getList(T str) {
        List<T> list = new ArrayList<>();
        list.add(str);
        return list;
    }
}
