package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

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
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.AdditionalRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherChangeRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.VoucherSendRecord;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.voucher.ApplyPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.financial.ApplyTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletMessageDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletMessageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletVoucherVerificationScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record.PushMsgPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage.ReceptionManagerVoucherListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
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

public class VoucherManagerCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_DAILY;
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
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
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
//        util.cleanVoucher();
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
    @Test(description = "优惠券管理--创建卡券5种类型卡券")
    public void voucherManage_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(VoucherTypeEnum.values()).forEach(anEnum -> {
                IScene voucherPageScene = VoucherFormVoucherPageScene.builder().build();
                int voucherTotal = voucherPageScene.invoke(visitor).getInteger("total");
                IScene applyPageScene = ApplyPageScene.builder().build();
                int applyTotal = applyPageScene.invoke(visitor).getInteger("total");
                //创建卡券
                String voucherName = util.createVoucher(1, anEnum);
                Long voucherId = util.getVoucherId(voucherName);
                String subjectType = util.getSubjectType();
                VoucherDetailBean voucherDetail = util.getVoucherDetail(voucherId);
                CommonUtil.checkResult(voucherName + " 描述", "<p>" + util.getDesc() + "</p>", voucherDetail.getVoucherDescription());
                CommonUtil.checkResult(voucherName + " 主体类型", subjectType, voucherDetail.getSubjectType());
                CommonUtil.checkResult(voucherName + " 主体类型id", util.getSubjectDesc(subjectType), voucherDetail.getSubjectId());
                CommonUtil.checkResult(voucherName + " 类型", anEnum.name(), voucherDetail.getCardType());
                CommonUtil.checkResult(voucherName + " 类型名称", anEnum.getDesc(), voucherDetail.getCardTypeName());
                CommonUtil.checkResult(voucherName + " 成本价格", "0.01", voucherDetail.getCost());
                if (anEnum.equals(VoucherTypeEnum.COMMODITY_EXCHANGE)) {
                    CommonUtil.checkResult(voucherName + " 可兑换商品", "兑换布加迪威龙一辆", voucherDetail.getExchangeCommodityName());
                } else if (anEnum.equals(VoucherTypeEnum.COUPON)) {
                    CommonUtil.checkResult(voucherName + " 门槛价格", "999.99", voucherDetail.getThresholdPrice());
                    CommonUtil.checkResult(voucherName + " 折扣", "2.50", voucherDetail.getDiscount());
                    CommonUtil.checkResult(voucherName + " 最多优惠", "99.99", voucherDetail.getMostDiscount());
                } else if (anEnum.getDesc().equals(VoucherTypeEnum.FULL_DISCOUNT.getDesc())) {
                    CommonUtil.checkResult(voucherName + " 面值", "49.99", voucherDetail.getParValue());
                    CommonUtil.checkResult(voucherName + " 门槛价格", "999.99", voucherDetail.getThresholdPrice());
                } else if (anEnum.equals(VoucherTypeEnum.CASH_COUPON)) {
                    CommonUtil.checkResult(voucherName + " 面值", "49.99", voucherDetail.getParValue());
                    CommonUtil.checkResult(voucherName + " 门槛价格", "999.99", voucherDetail.getThresholdPrice());
                    CommonUtil.checkResult(voucherName + " 抵扣金额", "99.99", voucherDetail.getReplacePrice());
                }
                //卡券列表+1
                int newVoucherTotal = voucherPageScene.invoke(visitor).getInteger("total");
                CommonUtil.checkResult("卡券列表数", voucherTotal + 1, newVoucherTotal);
                //卡券状态=待审批
                VoucherPage voucherPage = util.getVoucherPage(voucherId);
                CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.WAITING.getName(), voucherPage.getVoucherStatusName());
                CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.WAITING.name(), voucherPage.getVoucherStatus());
                //卡券变更记录不+1
                IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
                CommonUtil.checkResult(voucherName + " 变更记录数", 0, changeRecordScene.invoke(visitor).getInteger("total"));
                //审批列表+1
                int newApplyTotal = applyPageScene.invoke(visitor).getInteger("total");
                CommonUtil.checkResult(voucherName + " 审批列表数", applyTotal + 1, newApplyTotal);
                //审批状态=审批中
                ApplyPageBean applyPage = util.getAuditingApplyPage(voucherName);
                CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
                CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.VOUCHER.getName(), applyPage.getApplyTypeName());
                CommonUtil.checkResult(voucherName + " 审批成本单价/元", "0.01", applyPage.getPrice());
                CommonUtil.checkResult(voucherName + " 审批发出数量", 1, applyPage.getNum());
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--创建卡券5种类型卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--卡券的剩余库存>=可用库存")
    public void voucherManage_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherFormVoucherPageBean> voucherPageList = util.toJavaObjectList(scene, VoucherFormVoucherPageBean.class, SupporterUtil.SIZE);
            voucherPageList.forEach(e -> {
                Integer surplusInventory = e.getSurplusInventory();
                Integer allowUseInventory = e.getAllowUseInventory();
                Preconditions.checkArgument(surplusInventory >= 0, e.getVoucherName() + "剩余库存小于0");
                Preconditions.checkArgument(allowUseInventory >= 0, e.getVoucherName() + "可用库存小于0");
                Preconditions.checkArgument(surplusInventory >= allowUseInventory, e.getVoucherName() + "剩余库存为：" + surplusInventory + " 可用库存为：" + allowUseInventory);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--卡券的剩余库存>=可用库存");
        }
    }

    //ok
    @Test(description = "优惠券管理--已售罄的卡券剩余库存=0")
    public void voucherManage_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormVoucherPageScene.builder().voucherStatus(VoucherStatusEnum.SELL_OUT.name()).build();
            List<VoucherFormVoucherPageBean> voucherPageList = util.toJavaObjectList(scene, VoucherFormVoucherPageBean.class);
            voucherPageList.forEach(e -> CommonUtil.checkResult(e.getVoucherName() + " 剩余库存", 0, e.getSurplusInventory()));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--已售罄的卡券剩余库存=0");
        }
    }

    //ok
    @Test(description = "优惠券管理--卡券共领取数=已领取数")
    public void voucherManage_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherFormVoucherPageBean> voucherPages = util.toJavaObjectList(scene, VoucherFormVoucherPageBean.class, SupporterUtil.SIZE);
            voucherPages.forEach(e -> {
                String voucherName = e.getVoucherName();
                Integer totalSend = VoucherInfoScene.builder().id(e.getVoucherId()).build().invoke(visitor).getInteger("total_send");
                Integer cumulativeDelivery = e.getCumulativeDelivery();
                CommonUtil.checkResultPlus(voucherName + " 累计发出", cumulativeDelivery, "共领取数", totalSend);
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--卡券共领取数=已领取数");
        }
    }

    //ok
    @Test(description = "优惠券管理--卡券已核销数=核销记录列表数")
    public void voucherManage_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherFormVoucherPageBean> voucherPages = util.toJavaObjectList(scene, VoucherFormVoucherPageBean.class, SupporterUtil.SIZE);
            voucherPages.forEach(e -> {
                String voucherName = e.getVoucherName();
                Integer totalVerify = VoucherInfoScene.builder().id(e.getVoucherId()).build().invoke(visitor).getInteger("total_verify");
                Integer total = VerificationRecordScene.builder().voucherId(e.getVoucherId()).build().invoke(visitor).getInteger("total");
                CommonUtil.checkResultPlus(voucherName + " 共核销数", totalVerify, "核销记录列表数", total);
                IScene sendRecordScene = SendRecordScene.builder().voucherId(e.getVoucherId()).useStatus(VoucherUseStatusEnum.IS_USED.name()).build();
                Integer usedTotal = sendRecordScene.invoke(visitor).getInteger("total");
                CommonUtil.checkResultPlus(voucherName + " 共核销数", totalVerify, "领取记录已使用数", usedTotal);
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--卡券已核销数=核销记录列表数");
        }
    }

    //ok
    @Test(description = "优惠券管理--卡券共作废数=作废记录列表数")
    public void voucherManage_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherFormVoucherPageBean> voucherPages = util.toJavaObjectList(scene, VoucherFormVoucherPageBean.class, SupporterUtil.SIZE);
            voucherPages.forEach(e -> {
                String voucherName = e.getVoucherName();
                long totalInvalid = VoucherInfoScene.builder().id(e.getVoucherId()).build().invoke(visitor).getLong("total_invalid");
                long total = VoucherInvalidPageScene.builder().id(e.getVoucherId()).build().invoke(visitor).getLong("total");
                CommonUtil.checkResultPlus(voucherName + " 共作废数", totalInvalid, "作废记录列表数", total);
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--卡券共作废数=作废记录列表数");
        }
    }

    //ok
    @Test(description = "优惠券管理--增发记录的列表数=卡券在审批列表的增发记录数")
    public void voucherManage_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherFormVoucherPageBean> voucherPages = util.toJavaObjectList(scene, VoucherFormVoucherPageBean.class, SupporterUtil.SIZE);
            voucherPages.stream().map(VoucherFormVoucherPageBean::getVoucherId).forEach(voucherId -> {
                IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
                List<AdditionalRecord> additionalRecordList = util.toJavaObjectList(additionalRecordScene, AdditionalRecord.class);
                int additionalRecordNum = additionalRecordList.stream().map(AdditionalRecord::getAdditionalNum).mapToInt(Integer::parseInt).sum();
                String voucherName = util.getVoucherName(voucherId);
                IScene applyPageScene = ApplyPageScene.builder().name(voucherName).build();
                List<ApplyPageBean> applyPageList = util.toJavaObjectList(applyPageScene, ApplyPageBean.class).stream().filter(e -> e.getName().equals(voucherName) && e.getApplyTypeName().equals(ApplyTypeEnum.ADDITIONAL.getName())).collect(Collectors.toList());
                int applyPageSum = applyPageList.stream().mapToInt(ApplyPageBean::getNum).sum();
                CommonUtil.checkResultPlus(voucherName + " 增发记录列表数", additionalRecordList.size(), "卡券审批列表申请类型为增发的列表数", applyPageList.size());
                CommonUtil.checkResultPlus(voucherName + " 增发记录总增发量", additionalRecordNum, "卡券审批列表申请类型为增发的总增发量", applyPageSum);
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--增发记录的列表数=卡券在审批列表的增发记录数");
        }
    }

    //ok
    @Test(description = "优惠券管理--卡券转移列表数=小程序可用卡券数量")
    public void voucherManage_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene voucherListScene = VoucherListScene.builder().transferPhone(APPLET_USER_ONE.getPhone()).build();
            int voucherNum = voucherListScene.invoke(visitor).getJSONArray("list").size();
            visitor.login(APPLET_USER_ONE.getToken());
            int nearExpireNum = util.getAppletVoucherNum(VoucherUseStatusEnum.NEAR_EXPIRE);
            int normalNum = util.getAppletVoucherNum(VoucherUseStatusEnum.NORMAL);
            int a = (int) util.getAppletPackageContainVoucherList().stream().filter(e -> e.getStatusName().equals(VoucherUseStatusEnum.NEAR_EXPIRE.getName())).count();
            int b = (int) util.getAppletPackageContainVoucherList().stream().filter(e -> e.getStatusName().equals(VoucherUseStatusEnum.NORMAL.getName())).count();
            CommonUtil.valueView(nearExpireNum, normalNum, a, b);
            CommonUtil.checkResultPlus("可转移列表数", voucherNum, "小程序可用卡券数", nearExpireNum + normalNum + a + b);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--卡券转移列表数=小程序可用卡券数量");
        }
    }

    //ok
    @Test(description = "优惠券管理--新建卡券--卡券名称异常")
    public void voucherManage_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {EnumDesc.DESC_BETWEEN_40_50.getDesc(), "1", null, ""};
            Arrays.stream(strings).forEach(name -> {
                IScene scene = CreateScene.builder().voucherName(name)
                        .subjectType(util.getSubjectType()).cardType(VoucherTypeEnum.CUSTOM.name()).cost(99.99).stock(1000)
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(util.getSubjectType()))
                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
                String message = util.getResponse(scene).getMessage();
                String err = StringUtils.isEmpty(name) ? "卡券名称不能为空" : "卡券名称长度应为2～20个字";
                CommonUtil.checkResult("卡券名称为：" + name, err, message);
                CommonUtil.logger(name);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--新建卡券--卡券名称异常");
        }
    }

    //ok
    @Test(description = "优惠券管理--新建卡券--主体类型异常")
    public void voucherManage_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {"全部权限", null, ""};
            Arrays.stream(strings).forEach(subjectType -> {
                IScene scene = CreateScene.builder().voucherName(util.createVoucherName())
                        .subjectType(subjectType).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99).cost(99.99)
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(subjectType)).stock(1000)
                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
                String message = util.getResponse(scene).getMessage();
                String err = "主体类型不存在";
                CommonUtil.checkResult("主体类型为：" + subjectType, err, message);
                CommonUtil.logger(subjectType);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--新建卡券--主体类型异常");
        }
    }

    //ok
    @Test(description = "优惠券管理--新建卡券--主体详情异常")
    public void voucherManage_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CreateScene.builder().voucherName(util.createVoucherName())
                    .subjectType(UseRangeEnum.STORE.name()).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99)
                    .voucherDescription(util.getDesc()).stock(1000).cost(99.99).shopType(0)
                    .shopIds(util.getShopIdList(2)).selfVerification(true).build();
            String message = util.getResponse(scene).getMessage();
            String err = "主体详情不能为空";
            CommonUtil.checkResult("主体类型为：" + null, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--新建卡券--主体详情异常");
        }
    }

    //ok
    @Test(description = "优惠券管理--新建卡券--库存数量异常情况")
    public void voucherManage_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer[] integers = {1000000000, null, -100, Integer.MAX_VALUE};
            Arrays.stream(integers).forEach(stock -> {
                IScene scene = CreateScene.builder().voucherName(util.createVoucherName()).isDefaultPic(true)
                        .subjectType(util.getSubjectType()).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99).cost(99.99)
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(util.getSubjectType())).stock(stock)
                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
                String message = util.getResponse(scene).getMessage();
                String err = stock == null ? "库存不能为空" : "优惠券发行总量范围应在0 ～ 100000000张";
                CommonUtil.checkResult("卡券库存为：" + stock, err, message);
                CommonUtil.logger(stock);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--新建卡券--库存数量异常情况");
        }
    }

    //ok
    @Test(description = "优惠券管理--新建卡券--业务类型异常情况")
    public void voucherManage_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer[] integers = {null, -1, 100};
            Arrays.stream(integers).forEach(shopType -> {
                IScene scene = CreateScene.builder().voucherName(util.createVoucherName()).isDefaultPic(true)
                        .subjectType(util.getSubjectType()).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99).cost(99.99)
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(util.getSubjectType())).stock(1000)
                        .shopType(shopType).shopIds(util.getShopIdList()).selfVerification(true).build();
                String message = util.getResponse(scene).getMessage();
                String err = shopType == null ? "业务类型不能为空" : "业务类型不存在";
                CommonUtil.checkResult("业务类型为：" + shopType, err, message);
                CommonUtil.logger(shopType);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--新建卡券--业务类型异常情况");
        }
    }

    //ok
    @Test(description = "优惠券管理--新建卡券--成本异常情况")
    public void voucherManage_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Double[] doubles = {(double) -1, (double) 1000000000, 100000000.11};
            Arrays.stream(doubles).forEach(cost -> {
                IScene scene = CreateScene.builder().voucherName(util.createVoucherName()).isDefaultPic(true)
                        .subjectType(util.getSubjectType()).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99).cost(cost)
                        .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(util.getSubjectType())).stock(1000)
                        .shopType(0).shopIds(util.getShopIdList()).selfVerification(true).build();
                String message = util.getResponse(scene).getMessage();
                String err = cost == null ? "成本不能为空" : "优惠券成本应在0～10000000元之间";
                CommonUtil.checkResult("成本为：" + cost, err, message);
                CommonUtil.logger(cost);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--新建卡券--成本异常情况");
        }
    }

    //ok
    @Test(description = "优惠券管理--新建卡券--选择门店异常")
    public void voucherManage_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = CreateScene.builder().voucherName(util.createVoucherName()).isDefaultPic(true)
                    .subjectType(util.getSubjectType()).cardType(VoucherTypeEnum.CUSTOM.name()).parValue(99.99).cost(99.99)
                    .voucherDescription(util.getDesc()).subjectId(util.getSubjectDesc(util.getSubjectType())).stock(1000)
                    .shopType(0).selfVerification(true).build();
            String message = util.getResponse(scene).getMessage();
            String err = "卡券适用门店列表不能为空";
            CommonUtil.checkResult("卡券适用门店列表不能为：" + null, err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--新建卡券--成本异常情况");
        }
    }

    //ok
    @Test(description = "优惠券管理--卡券转移，转移账号异常")
    public void voucherManage_system_9() {
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
                String message = util.getResponse(scene).getMessage();
                String err = "推送用户id不能为空";
                CommonUtil.checkResult("转移卡券" + voucherName, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--卡券转移，转移账号异常");
        }
    }

    //ok
    @Test(description = "优惠券管理--卡券转移，接收账号异常")
    public void voucherManage_system_10() {
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
                String message = util.getResponse(scene).getMessage();
                String err = phone.equals(APPLET_USER_ONE.getPhone()) ? "转移账号和接收账号不能相同" : "您输入的手机号尚未注册小程序，请提醒用户注册小程序后再进行转移";
                CommonUtil.checkResult("转移卡券" + voucherName, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--卡券转移，接收账号异常");
        }
    }

    //ok
    @Test(description = "优惠券管理-卡券增发,异常情况")
    public void voucherManage_system_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer[] integers = {null};
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            Arrays.stream(integers).forEach(count -> {
                IScene scene = AddVoucherScene.builder().addNumber(count).id(voucherId).build();
                String message = util.getResponse(scene).getMessage();
                String err = "增发数量不能为空";
                CommonUtil.checkResult("增发数量" + count, err, message);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--卡券增发,异常情况");
        }
    }

    //ok
    @Test(description = "优惠券管理--转移全部卡券")
    public void voucherManage_system_12() {
        logger.logCaseStart(caseResult.getCaseName());
        List<Long> ids = null;
        try {
            visitor.login(APPLET_USER_ONE.getToken());
            int transferNum = util.getAppletVoucherNum();
            visitor.login(APPLET_USER_TWO.getToken());
            int receiveNum = util.getAppletVoucherNum();
            user.loginPc(ALL_AUTHORITY);
            JSONArray list = VoucherListScene.builder().transferPhone(APPLET_USER_ONE.getPhone()).build().invoke(visitor).getJSONArray("list");
            ids = list.stream().map(e -> (JSONObject) e).map(e -> util.toJavaObject(e, VoucherListBean.class)).map(VoucherListBean::getId).collect(Collectors.toList());
            //转移
            TransferScene.builder().transferPhone(APPLET_USER_ONE.getPhone()).receivePhone(APPLET_USER_TWO.getPhone()).voucherIds(ids).build().invoke(visitor);
            visitor.login(APPLET_USER_ONE.getToken());
            CommonUtil.checkResult("转移后，转移者优惠券数量", transferNum - ids.size(), util.getAppletVoucherNum());
            visitor.login(APPLET_USER_TWO.getToken());
            CommonUtil.checkResult("转移后，接收者优惠券数量", receiveNum + ids.size(), util.getAppletVoucherNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            //转回去
            user.loginPc(ALL_AUTHORITY);
            TransferScene.builder().transferPhone(APPLET_USER_TWO.getPhone()).receivePhone(APPLET_USER_ONE.getPhone()).voucherIds(ids).build().invoke(visitor);
            saveData("优惠券管理--转移全部卡券");
        }
    }

    //---------------------------撤回-----------------------------

    //ok
    @Test(description = "优惠券管理--撤回待审核卡券")
    public void voucherManage_system_20() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            //撤回
            RecallVoucherScene.builder().id(voucherId).build().invoke(visitor);
            //校验
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.RECALL.getName(), voucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.RECALL.name(), voucherPage.getVoucherStatus());
            //审批列表
            ApplyPageBean applyPage = util.getApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.CANCEL.getName(), applyPage.getStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--撤回待审核卡券");
        }
    }

    //---------------------------删除-----------------------------

    //ok
    @Test(description = "优惠券管理--删除已撤回的卡券")
    public void voucherManage_system_21() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.RECALL).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //删除
            DeleteVoucherScene.builder().id(voucherId).build().invoke(visitor);
            //校验
            JSONArray list = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build().invoke(visitor).getJSONArray("list");
            CommonUtil.checkResult(voucherName + " 结果列表", 0, list.size());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--删除已撤回的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--删除审批未通过的卡券")
    public void voucherManage_system_22() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.REJECT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //删除
            DeleteVoucherScene.builder().id(voucherId).build().invoke(visitor);
            //校验
            JSONArray list = VoucherFormVoucherPageScene.builder().voucherName(voucherName).build().invoke(visitor).getJSONArray("list");
            int count = (int) list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("voucher_name").equals(voucherName)).count();
            CommonUtil.checkResult(voucherName + " 结果列表", 0, count);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--删除审批未通的过卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--删除待审核的卡券")
    public void voucherManage_system_43() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            VoucherFormVoucherPageBean voucherFormVoucherPageBean = util.toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
            CommonUtil.checkResult("卡券 " + voucherName + " 能否删除", voucherFormVoucherPageBean.getIsDelete(), null);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--删除待审核的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--删除进行中的卡券")
    public void voucherManage_system_44() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            VoucherFormVoucherPageBean voucherFormVoucherPageBean = util.toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
            CommonUtil.checkResult("卡券 " + voucherName + " 能否删除", voucherFormVoucherPageBean.getIsDelete(), null);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--删除进行中的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--删除暂停发放的卡券")
    public void voucherManage_system_45() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.STOP).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            VoucherFormVoucherPageBean voucherFormVoucherPageBean = util.toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
            CommonUtil.checkResult("卡券 " + voucherName + " 能否删除", voucherFormVoucherPageBean.getIsDelete(), null);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--删除暂停发放的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--删除已作废的卡券")
    public void voucherManage_system_46() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.INVALIDED).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            VoucherFormVoucherPageBean voucherFormVoucherPageBean = util.toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
            CommonUtil.checkResult("卡券 " + voucherName + " 能否删除", voucherFormVoucherPageBean.getIsDelete(), null);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--删除已作废的卡券");
        }
    }

    //---------------------------暂停-----------------------------

    //ok
    @Test(description = "优惠券管理--暂停发放进行中的卡券")
    public void voucherManage_system_23() {
        try {
            Long voucherId = new VoucherGenerator.Builder().status(VoucherStatusEnum.WORKING).visitor(visitor).buildVoucher().getVoucherId();
            //暂停发放
            ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build().invoke(visitor);
            //校验
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP.getName(), voucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP.name(), voucherPage.getVoucherStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--暂停发放进行中的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--暂停发放已售罄的卡券")
    public void voucherManage_system_24() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().status(VoucherStatusEnum.SELL_OUT).visitor(visitor).buildVoucher().getVoucherId();
            //暂停发放
            ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build().invoke(visitor);
            //校验
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP.name(), voucherPage.getVoucherStatus());
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.STOP.getName(), voucherPage.getVoucherStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--暂停发放已售罄的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--暂停发放待审核的卡券")
    public void voucherManage_system_47() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            VoucherFormVoucherPageBean voucherFormVoucherPageBean = util.toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
            CommonUtil.checkResult("卡券 " + voucherName + " 能否暂停发放", voucherFormVoucherPageBean.getIsStop(), null);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--暂停发放待审核的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--暂停发放已撤回的卡券")
    public void voucherManage_system_48() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.RECALL).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            VoucherFormVoucherPageBean voucherFormVoucherPageBean = util.toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
            CommonUtil.checkResult("卡券 " + voucherName + " 能否暂停发放", voucherFormVoucherPageBean.getIsStop(), null);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--暂停发放待审核的卡券");
        }
    }

    //---------------------------开始-----------------------------

    //ok
    @Test(description = "优惠券管理--开始发放停止发放的卡券")
    public void voucherManage_system_25() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //停止发放
            ChangeProvideStatusScene.builder().id(voucherId).isStart(false).build().invoke(visitor);
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.STOP.getName(), voucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.STOP.name(), voucherPage.getVoucherStatus());
            //开始发放
            ChangeProvideStatusScene.builder().id(voucherId).isStart(true).build().invoke(visitor);
            //校验状态
            VoucherPage newVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.WORKING.getName(), newVoucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + "状态", VoucherStatusEnum.WORKING.name(), newVoucherPage.getVoucherStatus());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--开始发放停止发放的卡券");
        }
    }

    //---------------------------作废-----------------------------

    //ok
    @Test(description = "优惠券管理--作废停止发放的卡券")
    public void voucherManage_system_26() {
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
            int newChangeRecordTotal = changeRecordScene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            VoucherChangeRecord voucherChangeRecord = util.toJavaObjectList(changeRecordScene, VoucherChangeRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.INVALIDED.getName(), voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--作废停止发放的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--作废进行中的卡券")
    public void voucherManage_system_27() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            IScene scene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = scene.invoke(visitor).getInteger("total");
            //作废卡券
            InvalidVoucherScene.builder().id(voucherId).build().invoke(visitor);
            //校验卡券状态
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.getName(), voucherPage.getVoucherStatusName());
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.name(), voucherPage.getVoucherStatus());
            //校验变更记录列表数
            int newChangeRecordTotal = scene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            VoucherChangeRecord voucherChangeRecord = util.toJavaObjectList(scene, VoucherChangeRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.INVALIDED.getName(), voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--作废进行中的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--作废已售罄的卡券")
    public void voucherManage_system_28() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = changeRecordScene.invoke(visitor).getInteger("total");
            //作废卡券
            InvalidVoucherScene.builder().id(voucherId).build().invoke(visitor);
            //校验卡券状态
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 状态", VoucherStatusEnum.INVALIDED.name(), voucherPage.getVoucherStatus());
            CommonUtil.checkResult(voucherName + " 状态名称", VoucherStatusEnum.INVALIDED.getName(), voucherPage.getVoucherStatusName());
            //变更记录+1
            JSONObject changeResponse = visitor.invokeApi(changeRecordScene);
            int newChangeRecordTotal = changeResponse.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            VoucherChangeRecord voucherChangeRecord = util.toJavaObjectList(changeRecordScene, VoucherChangeRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.INVALIDED.getName(), voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--作废已售罄的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--作废已撤回的卡券")
    public void voucherManage_system_49() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.RECALL).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            VoucherFormVoucherPageBean voucherFormVoucherPageBean = util.toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
            CommonUtil.checkResult("卡券 " + voucherName + " 能否作废", voucherFormVoucherPageBean.getIsStop(), null);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--暂停发放待审核的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--作废待审核的卡券")
    public void voucherManage_system_50() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WAITING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            VoucherFormVoucherPageBean voucherFormVoucherPageBean = util.toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
            CommonUtil.checkResult("卡券 " + voucherName + " 能否作废", voucherFormVoucherPageBean.getIsStop(), null);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--作废待审核的卡券");
        }
    }

    //---------------------------增发-----------------------------

    //ok
    @Test(description = "优惠券管理--增发进行中的卡券")
    public void voucherManage_system_29() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = additionalRecordScene.invoke(visitor).getInteger("total");
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = changeRecordScene.invoke(visitor).getInteger("total");
            //增发卡券
            AddVoucherScene.builder().id(voucherId).addNumber(10).build().invoke(visitor);
            //卡券审批列表卡券状态=增发
            ApplyPageBean applyPage = util.getAuditingApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            JSONObject response = additionalRecordScene.invoke(visitor);
            int newAddTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, newAddTotal);
            //增发记录状态
            String statusName = response.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AUDITING.getName(), statusName);
            //审批通过卡券剩余库存+10
            util.applyVoucher(voucherName, "1");
            VoucherPage secondVoucherPage = util.getVoucherPage(voucherName);
            CommonUtil.checkResult(voucherName + " 剩余库存", voucherPage.getSurplusInventory() + 10, secondVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 剩余库存", voucherPage.getAllowUseInventory() + 10, secondVoucherPage.getAllowUseInventory());
            //变更记录变更事项
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, changeRecordScene.invoke(visitor).getInteger("total"));
            //校验变更记录变更事项
            VoucherChangeRecord voucherChangeRecord = util.toJavaObjectList(changeRecordScene, VoucherChangeRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.ADD.getName() + 10 + "张", voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
            //增发记录状态=已通过
            JSONObject newResponse = additionalRecordScene.invoke(visitor).getJSONArray("list").getJSONObject(0);
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AGREE.getName(), newResponse.getString("status_name"));
            CommonUtil.checkResult(voucherName + " 增发记录申请增发数量", 10, newResponse.getInteger("additional_num"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--增发进行中的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--增发已售罄的卡券")
    public void voucherManage_system_30() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.SELL_OUT).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long surplusInventory = util.getVoucherPage(voucherId).getSurplusInventory();
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = additionalRecordScene.invoke(visitor).getInteger("total");
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = changeRecordScene.invoke(visitor).getInteger("total");
            //增发卡券
            AddVoucherScene.builder().id(voucherId).addNumber(1).build().invoke(visitor);
            //卡券审批列表卡券状态=增发
            ApplyPageBean applyPage = util.getAuditingApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            JSONObject response = additionalRecordScene.invoke(visitor);
            int newAddTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, newAddTotal);
            //增发记录状态
            String statusName = response.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AUDITING.getName(), statusName);
            //审批通过卡券剩余库存+10
            util.applyVoucher(voucherName, "1");
            Long newSurplusInventory = util.getVoucherPage(voucherId).getSurplusInventory();
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory + 1, newSurplusInventory);
            //变更记录变更事项
            JSONObject changeResponse = changeRecordScene.invoke(visitor);
            int newChangeRecordTotal = changeResponse.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            List<VoucherChangeRecord> voucherChangeRecords = util.toJavaObjectList(changeRecordScene, VoucherChangeRecord.class);
            VoucherChangeRecord voucherChangeRecord = voucherChangeRecords.get(0);
            String changeItem = voucherChangeRecord.getChangeItem();
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.ADD.getName() + "1张", changeItem);
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
            //增发记录状体=已通过
            JSONObject newResponse = additionalRecordScene.invoke(visitor);
            String newStatusName = newResponse.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AGREE.getName(), newStatusName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--增发已售罄的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--增发停止发放的卡券")
    public void voucherManage_system_31() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.STOP).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            Long surplusInventory = util.getVoucherPage(voucherId).getSurplusInventory();
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = additionalRecordScene.invoke(visitor).getInteger("total");
            IScene changeRecordScene = ChangeRecordScene.builder().voucherId(voucherId).build();
            int changeRecordTotal = changeRecordScene.invoke(visitor).getInteger("total");
            //增发卡券
            AddVoucherScene.builder().id(voucherId).addNumber(10).build().invoke(visitor);
            //卡券审批列表卡券状态=增发
            ApplyPageBean applyPage = util.getAuditingApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            JSONObject response = additionalRecordScene.invoke(visitor);
            int newAddTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, newAddTotal);
            //增发记录状态
            String statusName = response.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AUDITING.getName(), statusName);
            //审批通过卡券剩余库存+10
            util.applyVoucher(voucherName, "1");
            Long newSurplusInventory = util.getVoucherPage(voucherId).getSurplusInventory();
            CommonUtil.checkResult(voucherName + " 剩余库存", surplusInventory + 10, newSurplusInventory);
            //变更记录+1
            JSONObject changeResponse = changeRecordScene.invoke(visitor);
            int newChangeRecordTotal = changeResponse.getInteger("total");
            CommonUtil.checkResult(voucherName + " 变更记录列表数", changeRecordTotal + 1, newChangeRecordTotal);
            //校验变更记录变更事项
            VoucherChangeRecord voucherChangeRecord = util.toJavaObjectList(changeRecordScene, VoucherChangeRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 变更记录变更事项", ChangeItemEnum.ADD.getName() + "10张", voucherChangeRecord.getChangeItem());
            CommonUtil.checkResult(voucherName + " 操作人", ALL_AUTHORITY.getName(), voucherChangeRecord.getOperateSaleName());
            CommonUtil.checkResult(voucherName + " 操作人角色", ALL_AUTHORITY.getRole(), voucherChangeRecord.getOperateSaleRole());
            CommonUtil.checkResult(voucherName + " 操作人账号", ALL_AUTHORITY.getPhone(), voucherChangeRecord.getOperateSaleAccount());
            //增发记录状体=已通过
            JSONObject newResponse = additionalRecordScene.invoke(visitor);
            String newStatusName = newResponse.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AGREE.getName(), newStatusName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--增发停止发放的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--增发作废卡券无法")
    public void voucherManage_system_32() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherStatus(VoucherStatusEnum.INVALIDED.name()).build();
            List<VoucherPage> voucherPageList = util.toJavaObjectList(voucherPageScene, VoucherPage.class);
            voucherPageList.forEach(voucherPage -> {
                CommonUtil.checkResult(voucherPage.getVoucherName() + "能否增发", null, voucherPage.getIsAdditional());
                CommonUtil.logger(voucherPage.getVoucherName());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--增发作废卡券无法");
        }
    }

    //ok
    @Test(description = "优惠券管理--增发卡券--审批通过前，剩余库存存、可用库存不变")
    public void voucherManage_system_33() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            //增发
            AddVoucherScene.builder().id(voucherId).addNumber(10).build().invoke(visitor);
            VoucherPage secondVoucherPage = util.getVoucherPage(voucherId);
            //增发后数据
            CommonUtil.checkResult(voucherName + " 增发审批通过前剩余库存数量", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 增发审批通过前可用库存数量", voucherPage.getAllowUseInventory(), secondVoucherPage.getAllowUseInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--增发卡券--审批通过前，剩余库存存不变");
        }
    }

    //ok
    @Test(description = "优惠券管理--增发卡券--审批不通过，剩余库存、可用库存不变")
    public void voucherManage_system_34() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            //增发
            AddVoucherScene.builder().id(voucherId).addNumber(10).build().invoke(visitor);
            //审批拒绝
            util.applyVoucher(voucherName, "2");
            //审批后数据
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            AdditionalRecord additionalRecord = util.toJavaObjectList(additionalRecordScene, AdditionalRecord.class).get(0);
            CommonUtil.checkResult(voucherName + " 增发记录列表状态", AdditionalRecordStatusEnum.REFUSAL.getName(), additionalRecord.getStatusName());
            VoucherPage secondVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 增发审批拒绝后剩余库存数量", voucherPage.getSurplusInventory(), secondVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 增发审批拒绝后剩可用存数量", secondVoucherPage.getAllowUseInventory(), secondVoucherPage.getAllowUseInventory());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--增发卡券--审批不通过，剩余库存、可用库存不变");
        }
    }

    //ok
    @Test(description = "优惠券管理--作废某人的卡券，小程序上我的卡券-1&作废记录+1")
    public void voucherManage_system_35() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/excel/发单人消息手机号.xlsx";
            util.pushCustomMessage(0, true, true, filePath, voucherId);
            //作废前数据
            user.loginApplet(APPLET_USER_ONE);
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
            CommonUtil.checkResult(voucherName + " 作废后作废人姓名", ALL_AUTHORITY.getName(), newVoucherInvalidPages.get(0).getInvalidName());
            CommonUtil.checkResult(voucherName + " 作废后作废人电话", ALL_AUTHORITY.getPhone(), newVoucherInvalidPages.get(0).getInvalidPhone());
            CommonUtil.checkResult(voucherName + " 作废后作废说明", EnumDesc.DESC_BETWEEN_10_15.getDesc(), newVoucherInvalidPages.get(0).getInvalidDescription());
            CommonUtil.checkResult(voucherCode + " 作废后共作废数", totalInvalid + 1, visitor.invokeApi(voucherInfoScene).getInteger("total_invalid"));
            user.loginApplet(APPLET_USER_ONE);
            CommonUtil.checkResult(voucherName + " 作废后小程序我的卡券数量", voucherCherNum, util.getAppletVoucherNum());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--作废某人的卡券，小程序上我的卡券-1&作废记录+1");
        }
    }

    //ok
    @Test(description = "优惠券管理--进行中的卡券增发，再撤回增发卡券")
    public void voucherManage_system_36() {
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = additionalRecordScene.invoke(visitor).getInteger("total");
            //增发卡券
            AddVoucherScene.builder().id(voucherId).addNumber(10).build().invoke(visitor);
            String applyTime = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            //卡券审批列表卡券状态=增发
            ApplyPageBean applyPage = util.getAuditingApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            List<AdditionalRecord> additionalRecordList = util.toJavaObjectList(additionalRecordScene, AdditionalRecord.class);
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, additionalRecordList.size());
            //增发记录状态
            String statusName = additionalRecordList.get(0).getStatusName();
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AUDITING.getName(), statusName);
            //撤回卡券审批列表卡券状态=已取消
            long additionalId = additionalRecordList.get(0).getId();
            RecallAdditionalScene.builder().id(additionalId).build().invoke(visitor);
            String newStatusName = util.toJavaObjectList(additionalRecordScene, AdditionalRecord.class).get(0).getStatusName();
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.RECALL.getName(), newStatusName);
            ApplyPageBean newApplyPage = util.getApplyPageByTime(voucherName, applyTime);
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.CANCEL.getName(), newApplyPage.getStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--进行中的卡券增发，再撤回增发卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--转移卡券")
    public void voucherManage_system_37() {
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
            saveData("优惠券管理--转移卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--转移已使用的卡券")
    public void voucherManage_system_38() {
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
            saveData("优惠券管理--转移已使用的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--转移已过期的卡券")
    public void voucherManage_system_39() {
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
            saveData("优惠券管理--转移已过期的卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--选择卡券列表不显示已作废卡券")
    public void voucherManage_system_40() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().voucherStatus(VoucherStatusEnum.INVALIDED.name()).build();
            List<VoucherPage> voucherPageList = util.toJavaObjectList(voucherPageScene, VoucherPage.class);
            List<Long> invalidedVoucherList = voucherPageList.stream().map(VoucherPage::getVoucherId).collect(Collectors.toList());
            IScene receptionVoucherListScene = ReceptionManagerVoucherListScene.builder().build();
            JSONArray array = receptionVoucherListScene.invoke(visitor).getJSONArray("list");
            List<Long> voucherList = array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("voucher_id")).collect(Collectors.toList());
            invalidedVoucherList.forEach(voucherId -> {
                Preconditions.checkArgument(!voucherList.contains(voucherId), "选择卡券列表能看见已作废的卡券：" + voucherId);
                CommonUtil.logger(voucherId);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--选择卡券列表不显示已作废卡券");
        }
    }

    //ok
    @Test(description = "优惠券管理--撤回的卡券再编辑")
    public void voucherManage_system_41() {
        logger.logCaseStart(caseResult.getCaseName());
        Long voucherId = null;
        try {
            voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.RECALL).buildVoucher().getVoucherId();
            VoucherDetailBean detail = util.getVoucherDetail(voucherId);
            EditVoucherScene.builder().voucherName(detail.getVoucherName()).subjectType(detail.getSubjectType())
                    .subjectId(detail.getSubjectId()).stock(detail.getStock()).cardType(detail.getCardType())
                    .isThreshold(detail.getIsThreshold()).thresholdPrice(detail.getThresholdPrice()).exchangeCommodityName(detail.getExchangeCommodityName())
                    .parValue(detail.getParValue()).replacePrice(detail.getReplacePrice()).discount(detail.getDiscount())
                    .mostDiscount(detail.getMostDiscount()).cost(detail.getCost()).isDefaultPic(detail.getIsDefaultPic())
                    .voucherDescription(detail.getVoucherDescription()).selfVerification(detail.getSelfVerification())
                    .shopIds(detail.getShopIds().stream().map(e -> (JSONObject) e).map(e -> e.getLong("shop_id")).collect(Collectors.toList()))
                    .shopType(detail.getShopType()).subjectName(detail.getSubjectName()).id(voucherId).build().invoke(visitor);
            //编辑完成之后
            VoucherPage voucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherPage.getVoucherName() + " 的状态", VoucherStatusEnum.WAITING.name(), voucherPage.getVoucherStatus());
            CommonUtil.checkResult(voucherPage.getVoucherName() + " 的状态", VoucherStatusEnum.WAITING.getName(), voucherPage.getVoucherStatusName());
            //审核页
            IScene applyPageScene = ApplyPageScene.builder().name(voucherPage.getVoucherName()).build();
            ApplyPageBean applyPageBean = util.toJavaObject(applyPageScene, ApplyPageBean.class, "name", voucherPage.getVoucherName());
            CommonUtil.checkResult(voucherPage.getVoucherName() + " 在审核页的状态", ApplyStatusEnum.AUDITING.name(), applyPageBean.getStatus());
            CommonUtil.checkResult(voucherPage.getVoucherName() + " 在审核页的状态", ApplyStatusEnum.AUDITING.getName(), applyPageBean.getStatusName());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            //撤回
            RecallVoucherScene.builder().id(voucherId).build().invoke(visitor);
            saveData("优惠券管理--撤回的卡券再编辑");
        }
    }

    //ok
    @Test(description = "优惠券管理--小程序自助核销一张，使用的核销码对应人员册核销数量+1&【核销记录】列表数+1&&核销渠道=主动核销")
    public void voucherManage_system_42() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
            String voucherName = util.getVoucherName(voucherId);
            //发出一张卡券
            util.pushCustomMessage(0, true, true, voucherId);
            Thread.sleep(500);
            //获取最新发出卡券的code
            String voucherCode = util.getVoucherCode(voucherId);
            CommonUtil.valueView(voucherCode);
            //核销列表数
            IScene verificationRecordScene = VerificationRecordScene.builder().voucherId(voucherId).build();
            List<VerificationRecordBean> verificationRecords = util.toJavaObjectList(verificationRecordScene, VerificationRecordBean.class);
            //获取核销码
            String code = util.getVerificationCode("本司员工");
            IScene verificationPeopleScene = VerificationPeopleScene.builder().verificationCode(code).build();
            //核销人员核销数量
            int verificationNumber = verificationPeopleScene.invoke(visitor).getJSONArray("list").getJSONObject(0).getInteger("verification_number");
            //获取卡券核销id
            user.loginApplet(APPLET_USER_ONE);
            Long appletVoucherId = util.getAppletVoucherInfo(voucherCode).getId();
            //核销
            AppletVoucherVerificationScene.builder().id(String.valueOf(appletVoucherId)).verificationCode(code).build().invoke(visitor);
            //核销之后数据
            user.loginPc(ALL_AUTHORITY);
            List<VerificationRecordBean> newVerificationRecords = util.toJavaObjectList(verificationRecordScene, VerificationRecordBean.class);
            CommonUtil.checkResult(voucherName + " 核销记录列表数", verificationRecords.size() + 1, newVerificationRecords.size());
            CommonUtil.checkResult(voucherCode + " 核销渠道", VerifyChannelEnum.ACTIVE.getName(), newVerificationRecords.get(0).getVerificationChannelName());
            int newVerificationNumber = verificationPeopleScene.invoke(visitor).getJSONArray("list").getJSONObject(0).getInteger("verification_number");
            CommonUtil.checkResult("核销码" + code + "核销数", verificationNumber + 1, newVerificationNumber);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券管理--小程序自助核销一张，使用的核销码对应人员册核销数量+1&【核销记录】列表数+1&&核销渠道=主动核销");
        }
    }

    //ok
    @Test(description = "核销人员--创建异页核销,列表数+1")
    public void verificationPeople_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene verificationPeopleScene = VerificationPeopleScene.builder().build();
            //查询列表数
            int total = verificationPeopleScene.invoke(visitor).getInteger("total");
            String phone = util.getDistinctPhone();
            CreateVerificationPeopleScene.builder().verificationPersonName("异页打工人").verificationPersonPhone(phone).type(1).status(1).build().invoke(visitor);
            int newTotal = verificationPeopleScene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult("核销人员列表数", total + 1, newTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建异页核销,列表数+1");
        }
    }

    //ok
    @Test(description = "核销人员--创建财务核销,列表数+1")
    public void verificationPeople_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene verificationPeopleScene = VerificationPeopleScene.builder().build();
            //查询列表数
            int total = verificationPeopleScene.invoke(visitor).getInteger("total");
            String phone = util.getDistinctPhone();
            CreateVerificationPeopleScene.builder().verificationPersonName("本司打工人").verificationPersonPhone(phone).type(0).status(1).build().invoke(visitor);
            int newTotal = verificationPeopleScene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult("核销人员列表数", total + 1, newTotal);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("核销人员--创建财务核销,列表数+1");
        }
    }

    //ok
    @Test(description = "核销人员--卡券核销时将核销人状态关闭，提示核销失败")
    public void verificationPeople_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        String code = null;
        try {
            code = util.getVerificationCode(false, "本司员工");
            visitor.login(APPLET_USER_ONE.getToken());
            long id = util.getAppletVoucher(VoucherUseStatusEnum.NEAR_EXPIRE).getId();
            IScene scene = AppletVoucherVerificationScene.builder().id(String.valueOf(id)).verificationCode(code).build();
            String message = util.getResponse(scene).getMessage();
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
    public void verificationPeople_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, EnumDesc.DESC_BETWEEN_200_300.getDesc()};
            Arrays.stream(strings).forEach(name -> {
                IScene scene = CreateVerificationPeopleScene.builder().verificationPersonName(name)
                        .verificationPersonPhone("13663366788").status(1).type(1).build();
                String message = util.getResponse(scene).getMessage();
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
    public void verificationPeople_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, EnumDesc.DESC_BETWEEN_200_300.getDesc()};
            Arrays.stream(strings).forEach(name -> {
                IScene scene = CreateVerificationPeopleScene.builder().verificationPersonName(name)
                        .verificationPersonPhone("13663366788").status(1).type(1).build();
                String message = util.getResponse(scene).getMessage();
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
    public void verificationPeople_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] strings = {null, "", "11111111111", "1337316680", "133731668062"};
            Arrays.stream(strings).forEach(phone -> {
                IScene scene = CreateVerificationPeopleScene.builder().verificationPersonName("打工人")
                        .verificationPersonPhone(phone).status(1).type(0).build();
                String message = util.getResponse(scene).getMessage();
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
    public void verificationPeople_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String phone = util.getRepetitionVerificationPhone();
            IScene scene = CreateVerificationPeopleScene.builder().verificationPersonName("打工人")
                    .verificationPersonPhone(phone).status(1).type(0).build();
            String message = util.getResponse(scene).getMessage();
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
