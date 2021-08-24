package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.voucher.ApplyApprovalInfoBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.voucher.ApplyPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherFormVoucherPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityApprovalStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.financial.ApplyTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.NoticeMessagePullScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageDataScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManagePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyApprovalInfoScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyBatchApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.AddVoucherScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.AdditionalRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormVoucherPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
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
 * 审批管理
 */
public class ApproveManagerCaseOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.JC_ONLINE_LXQ;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCE);
    private final SceneUtil util = new SceneUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.setShopId(PRODUCE.getShopId()).setReferer(PRODUCE.getReferer()).setRoleId(ALL_AUTHORITY.getRoleId()).setProduct(PRODUCE.getAbbreviation());
        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        util.cleanVoucher();
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        util.loginPc(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        logger.logCaseStart(caseResult.getCaseName());
    }

    //ok
    @Test(description = "优惠券审批--成本累计=发出数量*成本单价")
    public void voucherApply_data_1() {
        try {
            IScene applyPageScene = ApplyPageScene.builder().build();
            List<ApplyPageBean> applyPageList = util.toJavaObjectList(applyPageScene, ApplyPageBean.class, SceneUtil.SIZE);
            applyPageList.forEach(applyPage -> {
                String voucherName = applyPage.getName();
                Integer num = applyPage.getNum();
                String price = applyPage.getPrice();
                String totalPrice = applyPage.getTotalPrice();
                Preconditions.checkArgument(Double.parseDouble(totalPrice) <= Double.parseDouble(price) * num + 0.000001 || Double.parseDouble(totalPrice) >= Double.parseDouble(price) * num - 0.000001,
                        voucherName + "成本累计：" + Double.parseDouble(totalPrice) + "发出数量*成本单价：" + Double.parseDouble(price) * num);
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--成本累计=发出数量*成本单价");
        }
    }

    //ok
    @Test(description = "优惠券审批--发出数量（首发）=【优惠券管理】发行库存数量")
    public void voucherApply_data_2() {
        try {
            //卡券列表
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherFormVoucherPageBean> voucherPageList = util.toJavaObjectList(voucherPageScene, VoucherFormVoucherPageBean.class, SceneUtil.SIZE);
            voucherPageList.forEach(voucherPage -> {
                VoucherDetailBean voucherDetail = util.getVoucherDetail(voucherPage.getVoucherId());
                String voucherName = voucherPage.getVoucherName();
                IScene applyPageScene = ApplyPageScene.builder().name(voucherName).build();
                List<ApplyPageBean> applyPageList = util.toJavaObjectList(applyPageScene, ApplyPageBean.class);
                ApplyPageBean applyPage = applyPageList.stream().filter(e -> e.getName().equals(voucherName) && e.getApplyTypeName().equals(ApplyTypeEnum.VOUCHER.getName())).findFirst().orElse(null);
                Preconditions.checkArgument(applyPage != null, voucherName + " 在审核列表为空");
                CommonUtil.checkResultPlus(voucherName + "发行库存数量", voucherDetail.getStock(), "发出数量（首发）", applyPage.getNum());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--发出数量（首发）=【优惠券管理】发行库存数量");
        }
    }

    //ok
    @Test(description = "优惠券审批--审批列表成本单价=【优惠券管理】成本")
    public void voucherApply_data_3() {
        try {
            //卡券列表
            IScene voucherPageScene = VoucherFormVoucherPageScene.builder().build();
            List<VoucherFormVoucherPageBean> voucherPageList = util.toJavaObjectList(voucherPageScene, VoucherFormVoucherPageBean.class, SceneUtil.SIZE);
            voucherPageList.forEach(voucherPage -> {
                CommonUtil.valueView(voucherPage.getVoucherName());
                VoucherDetailBean voucherDetail = util.getVoucherDetail(voucherPage.getVoucherId());
                String cost = voucherDetail.getCost();
                String voucherName = voucherPage.getVoucherName();
                IScene applyPageScene = ApplyPageScene.builder().name(voucherName).build();
                List<ApplyPageBean> applyPageList = util.toJavaObjectList(applyPageScene, ApplyPageBean.class);
                applyPageList.stream().filter(applyPage -> applyPage.getName().equals(voucherName)).map(ApplyPageBean::getPrice).forEach(price -> CommonUtil.checkResultPlus(voucherName + " 成本", cost, "审批列表成本单价", price));
                CommonUtil.logger(voucherName);
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--审批列表成本单价=【优惠券管理】成本");
        }
    }

    //ok
    @Test(description = "优惠券审批--优惠券审批页各状态数量=审批数据统计中各状态数量")
    public void voucherApply_data_4() {
        try {
            List<Long> totalLost = Arrays.stream(VoucherApprovalStatusEnum.values()).map(e -> ApplyPageScene.builder().status(e.getId()).build().visitor(visitor).execute().getLong("total")).collect(Collectors.toList());
            IScene scene = ApplyApprovalInfoScene.builder().build();
            ApplyApprovalInfoBean applyApprovalInfoBean = util.toJavaObject(scene, ApplyApprovalInfoBean.class);
            CommonUtil.checkResultPlus(VoucherApprovalStatusEnum.AUDITING.getName() + " 在优惠券审批页数量", totalLost.get(VoucherApprovalStatusEnum.AUDITING.getId()), "在审批数据统数量", applyApprovalInfoBean.getWaitApproval());
            CommonUtil.checkResultPlus(VoucherApprovalStatusEnum.AGREE.getName() + " 在优惠券审批页数量", totalLost.get(VoucherApprovalStatusEnum.AGREE.getId()), "在审批数据统数量", applyApprovalInfoBean.getPassApproval());
            CommonUtil.checkResultPlus(VoucherApprovalStatusEnum.REFUSAL.getName() + " 在优惠券审批页数量", totalLost.get(VoucherApprovalStatusEnum.REFUSAL.getId()), "在审批数据统数量", applyApprovalInfoBean.getFailApproval());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--优惠券审批--优惠券审批页个状态数量=审批数据统计中各状态数量");
        }
    }

    //ok
    @Test(description = "优惠券审批--优惠券审批页全部审批=待审批+审批通过+审批未通过+已撤销")
    public void voucherApply_data_5() {
        try {
            JSONObject data = ApplyApprovalInfoScene.builder().build().visitor(visitor).execute();
            Long total = data.getLong("total_approval");
            Long wait = data.getLong("wait_approval");
            Long failed = data.getLong("fail_approval");
            Long passed = data.getLong("pass_approval");
            Long auditingTotal = ApplyPageScene.builder().status(VoucherApprovalStatusEnum.AUDITING.getId()).build().visitor(visitor).execute().getLong("total");
            Long agreeTotal = ApplyPageScene.builder().status(VoucherApprovalStatusEnum.AGREE.getId()).build().visitor(visitor).execute().getLong("total");
            Long refusalTotal = ApplyPageScene.builder().status(VoucherApprovalStatusEnum.REFUSAL.getId()).build().visitor(visitor).execute().getLong("total");
            Long recallTotal = ApplyPageScene.builder().status(VoucherApprovalStatusEnum.RECALL.getId()).build().visitor(visitor).execute().getLong("total");
            Preconditions.checkArgument(wait.equals(auditingTotal), "审核数据接口待审核卡券数：" + wait + " 按照待审核搜索列表数：" + auditingTotal);
            Preconditions.checkArgument(passed.equals(agreeTotal), "审核数据接口审核通过卡券数：" + passed + " 按照审核通过搜索列表数：" + agreeTotal);
            Preconditions.checkArgument(failed.equals(refusalTotal), "审核数据接口审核未通过卡券数：" + failed + " 按照审核未通过搜索列表数：" + refusalTotal);
            Preconditions.checkArgument(total == (auditingTotal + agreeTotal + refusalTotal + recallTotal), " 审核数据接口全部审批卡券数：" + total + " 列表四种状态总数：" + (auditingTotal + agreeTotal + refusalTotal + recallTotal));
            Preconditions.checkArgument(total >= (wait + failed + passed), "审核数据接口全部审批卡券数：" + total + " 审核数据接口三种状态总数：" + (wait + failed + passed));
            Preconditions.checkArgument((wait + failed + passed) == (auditingTotal + agreeTotal + refusalTotal), "审核数据接口三种状态总数：" + (wait + failed + passed) + " 列表三种状态总数：" + (auditingTotal + agreeTotal + refusalTotal));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--优惠券审批页全部审批=优惠券审批页全部审批=待审批+审批通过+审批未通过+已撤销");
        }
    }

    //ok
    @Test(description = "优惠券审批--待审批的卡券数量=全局提醒卡券审批的数量")
    public void voucherApply_data_6() {
        try {
            Integer total = ApplyPageScene.builder().status(ApplyStatusEnum.AUDITING.getId()).build().visitor(visitor).execute().getInteger("total");
            JSONArray list = NoticeMessagePullScene.builder().build().visitor(visitor).execute().getJSONArray("list");
            String messageContent = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("message_content").contains("卡券审批"))
                    .map(e -> e.getString("message_content")).findFirst().orElse(null);
            Preconditions.checkArgument(messageContent != null, "全局提醒关于卡券审批的提示内容为空");
            Preconditions.checkArgument(messageContent.contains(String.valueOf(total)), "全局提醒内容为：" + messageContent + "卡券审批条数为：" + total);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--待审批的卡券数量=全局提醒卡券审批的数量");
        }
    }

    //ok
    @Test(description = "优惠券审批--待审批的活动数量=全局提醒活动审批的数量")
    public void voucherApply_data_7() {
        try {
            int total = ManagePageScene.builder().approvalStatus(ActivityStatusEnum.PENDING.getId()).build().visitor(visitor).execute().getInteger("total");
            JSONArray list = NoticeMessagePullScene.builder().build().visitor(visitor).execute().getJSONArray("list");
            String messageContent = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("message_content").contains("活动审批"))
                    .map(e -> e.getString("message_content")).findFirst().orElse(null);
            Preconditions.checkArgument(messageContent != null, "全局提醒关于活动审批的提示内容为空");
            Preconditions.checkArgument(messageContent.contains(String.valueOf(total)), "全局提醒内容为：" + messageContent + "活动审批条数为：" + total);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--待审批的活动数量=全局提醒活动审批的数量");
        }
    }

    //ok
    @Test(description = "优惠券审批--拒绝卡券的增发")
    public void voucherApply_system_1() {
        try {
            VoucherFormVoucherPageBean voucherPage = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherPage();
            Long voucherId = voucherPage.getVoucherId();
            String voucherName = voucherPage.getVoucherName();
            voucherPage = util.getVoucherPage(voucherId);
            IScene additionalRecordScene = AdditionalRecordScene.builder().voucherId(voucherId).build();
            int addTotal = additionalRecordScene.visitor(visitor).execute().getInteger("total");
            //增发卡券
            AddVoucherScene.builder().id(voucherId).addNumber(10).build().visitor(visitor).execute();
            //卡券审批列表卡券状态=增发
            ApplyPageBean applyPage = util.getAuditingApplyPage(voucherName);
            CommonUtil.checkResult(voucherName + " 审批申请类型", ApplyTypeEnum.ADDITIONAL.getName(), applyPage.getApplyTypeName());
            CommonUtil.checkResult(voucherName + " 审批列表状态", ApplyStatusEnum.AUDITING.getName(), applyPage.getStatusName());
            //增发记录数量+1
            JSONObject response = additionalRecordScene.visitor(visitor).execute();
            int newAddTotal = response.getInteger("total");
            CommonUtil.checkResult(voucherName + " 增发记录列表数", addTotal + 1, newAddTotal);
            //增发记录状态
            String statusName = response.getJSONArray("list").getJSONObject(0).getString("status_name");
            CommonUtil.checkResult(voucherName + " 增发记录状态", AdditionalRecordStatusEnum.AUDITING.getName(), statusName);
            //审批拒绝卡券剩余库存+0
            util.applyVoucher(voucherName, "2");
            VoucherFormVoucherPageBean newVoucherPage = util.getVoucherPage(voucherId);
            CommonUtil.checkResult(voucherName + " 剩余库存", voucherPage.getSurplusInventory(), newVoucherPage.getSurplusInventory());
            CommonUtil.checkResult(voucherName + " 可用库存", voucherPage.getAllowUseInventory(), newVoucherPage.getAllowUseInventory());
            //增发记录状态=已拒绝
            JSONObject newResponse = additionalRecordScene.visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            CommonUtil.checkResult(voucherName + " 增发记录状态", "审核未通过", newResponse.getString("status_name"));
            CommonUtil.checkResult(voucherName + " 增发记录申请增发数量", 10, newResponse.getInteger("additional_num"));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--拒绝卡券的增发");
        }
    }

    //ok
    @Test(description = "优惠券审批--卡券批量审批通过")
    public void voucherApply_system_2() {
        try {
            IScene applyApprovalInfoScene = ApplyApprovalInfoScene.builder().build();
            ApplyApprovalInfoBean applyApprovalInfoBean = util.toJavaObject(applyApprovalInfoScene, ApplyApprovalInfoBean.class);
            //创建2个待审批数据
            Arrays.stream(VoucherTypeEnum.values()).collect(Collectors.toList()).subList(0, 2).forEach(e -> util.createVoucher(1, e));
            IScene applyPageScene = ApplyPageScene.builder().status(ApplyStatusEnum.AUDITING.getId()).build();
            List<ApplyPageBean> applyPageBeanList = util.toJavaObjectList(applyPageScene, ApplyPageBean.class, SceneUtil.SIZE).subList(0, 2);
            List<Long> applyIdList = applyPageBeanList.stream().map(ApplyPageBean::getId).collect(Collectors.toList());
            List<Long> voucherIdList = applyPageBeanList.stream().map(ApplyPageBean::getVoucherId).collect(Collectors.toList());
            //批量审批通过
            ApplyBatchApprovalScene.builder().ids(applyIdList).status(ApplyStatusEnum.AGREE.getId()).build().visitor(visitor).execute();
            //审批通过后状态
            applyIdList.forEach(applyId -> {
                ApplyPageBean applyPageBean = util.toJavaObject(ApplyPageScene.builder().build(), ApplyPageBean.class, "id", applyId);
                CommonUtil.checkResult("卡券的状态", ApplyStatusEnum.AGREE.getName(), applyPageBean.getStatusName());
                CommonUtil.checkResult("卡券的状态", ApplyStatusEnum.AGREE.name(), applyPageBean.getStatus());
            });
            //卡券状态
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            voucherIdList.forEach(voucherId -> {
                VoucherFormVoucherPageBean voucherFormVoucherPageBean = util.toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
                CommonUtil.checkResult("卡券" + voucherFormVoucherPageBean.getVoucherName() + "的状态", VoucherStatusEnum.WORKING.getName(), voucherFormVoucherPageBean.getVoucherStatusName());
                CommonUtil.checkResult("卡券" + voucherFormVoucherPageBean.getVoucherName() + "的状态", VoucherStatusEnum.WORKING.name(), voucherFormVoucherPageBean.getVoucherStatus());
            });
            //审批数据
            ApplyApprovalInfoBean newApplyApprovalInfoBean = util.toJavaObject(applyApprovalInfoScene, ApplyApprovalInfoBean.class);
            CommonUtil.checkResult("待审核卡券的数量", applyApprovalInfoBean.getWaitApproval(), newApplyApprovalInfoBean.getWaitApproval());
            CommonUtil.checkResult("已通过卡券的数量", applyApprovalInfoBean.getPassApproval() + voucherIdList.size(), newApplyApprovalInfoBean.getPassApproval());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--卡券批量审批通过");
        }
    }

    //ok
    @Test(description = "优惠券审批--卡券批量审批不通过")
    public void voucherApply_system_3() {
        try {
            IScene applyApprovalInfoScene = ApplyApprovalInfoScene.builder().build();
            ApplyApprovalInfoBean applyApprovalInfoBean = util.toJavaObject(applyApprovalInfoScene, ApplyApprovalInfoBean.class);
            //创建2个待审批数据
            Arrays.stream(VoucherTypeEnum.values()).collect(Collectors.toList()).subList(0, 2).forEach(e -> util.createVoucher(1, e));
            IScene applyPageScene = ApplyPageScene.builder().status(ApplyStatusEnum.AUDITING.getId()).build();
            List<ApplyPageBean> applyPageBeanList = util.toJavaObjectList(applyPageScene, ApplyPageBean.class, SceneUtil.SIZE).subList(0, 2);
            List<Long> applyIdList = applyPageBeanList.stream().map(ApplyPageBean::getId).collect(Collectors.toList());
            List<Long> voucherIdList = applyPageBeanList.stream().map(ApplyPageBean::getVoucherId).collect(Collectors.toList());
            //批量审批不通过
            ApplyBatchApprovalScene.builder().ids(applyIdList).status(ApplyStatusEnum.REFUSAL.getId()).build().visitor(visitor).execute();
            //审批不通过后状态
            applyIdList.forEach(applyId -> {
                IScene scene = ApplyPageScene.builder().status(ApplyStatusEnum.REFUSAL.getId()).build();
                ApplyPageBean applyPageBean = util.toJavaObject(scene, ApplyPageBean.class, "id", applyId);
                CommonUtil.checkResult("卡券的状态", ApplyStatusEnum.REFUSAL.getName(), applyPageBean.getStatusName());
                CommonUtil.checkResult("卡券的状态", ApplyStatusEnum.REFUSAL.name(), applyPageBean.getStatus());
            });
            //卡券状态
            IScene scene = VoucherFormVoucherPageScene.builder().build();
            voucherIdList.forEach(voucherId -> {
                VoucherFormVoucherPageBean voucherFormVoucherPageBean = util.toJavaObject(scene, VoucherFormVoucherPageBean.class, "voucher_id", voucherId);
                CommonUtil.checkResult("卡券" + voucherFormVoucherPageBean.getVoucherName() + "的状态", VoucherStatusEnum.REJECT.getName(), voucherFormVoucherPageBean.getVoucherStatusName());
                CommonUtil.checkResult("卡券" + voucherFormVoucherPageBean.getVoucherName() + "的状态", VoucherStatusEnum.REJECT.name(), voucherFormVoucherPageBean.getVoucherStatus());
            });
            //审批数据
            ApplyApprovalInfoBean newApplyApprovalInfoBean = util.toJavaObject(applyApprovalInfoScene, ApplyApprovalInfoBean.class);
            CommonUtil.checkResult("待审核卡券的数量", applyApprovalInfoBean.getWaitApproval(), newApplyApprovalInfoBean.getWaitApproval());
            CommonUtil.checkResult("已拒绝卡券的数量", applyApprovalInfoBean.getFailApproval() + voucherIdList.size(), newApplyApprovalInfoBean.getFailApproval());
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--卡券批量审批不通过");
        }
    }

    //ok
    @Test(description = "优惠券审批--卡券审批已通过的卡券")
    public void voucherApply_system_5() {
        try {
            JSONArray list = ApplyPageScene.builder().status(ApplyStatusEnum.AGREE.getId()).build().visitor(visitor).execute().getJSONArray("list");
            Long applyId = list.getJSONObject(0).getLong("id");
            //批量审批通过
            String message = ApplyApprovalScene.builder().id(applyId).status(String.valueOf(VoucherApprovalStatusEnum.AGREE.getId())).build().visitor(visitor).getResponse().getMessage();
            String err = "success";
            CommonUtil.checkResult("批量审批已通过的卡券", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--卡券审批已通过的卡券");
        }
    }

    //ok
    @Test(description = "活动审批--活动审批页全部审批=待审批+审批通过+审批未通过")
    public void activityApply_data_1() {
        try {
            JSONObject data = ManageDataScene.builder().build().visitor(visitor).execute();
            Long total = data.getLong("total");
            Long wait = data.getLong("wait");
            Long failed = data.getLong("failed");
            Long passed = data.getLong("passed");
            Long pendingTotal = ManagePageScene.builder().approvalStatus(ActivityApprovalStatusEnum.PENDING.getId()).build().visitor(visitor).execute().getLong("total");
            Long passedTotal = ManagePageScene.builder().approvalStatus(ActivityApprovalStatusEnum.PASSED.getId()).build().visitor(visitor).execute().getLong("total");
            Long rejectTotal = ManagePageScene.builder().approvalStatus(ActivityApprovalStatusEnum.REJECT.getId()).build().visitor(visitor).execute().getLong("total");
            Preconditions.checkArgument(wait.equals(pendingTotal), "审核数据接口待审核活动数：" + wait + " 按照待审核搜索列表数：" + pendingTotal);
            Preconditions.checkArgument(passed.equals(passedTotal), "审核数据接口审核通过活动数：" + wait + " 按照审核通过搜索列表数：" + passedTotal);
            Preconditions.checkArgument(failed.equals(rejectTotal), "审核数据接口审核未通过活动数：" + wait + " 按照审核未通过搜索列表数：" + rejectTotal);
            Preconditions.checkArgument(total == (pendingTotal + passedTotal + rejectTotal), "审核数据接口全部审批活动数" + total + " 列表三种状态总数：" + (pendingTotal + passedTotal + rejectTotal));
            Preconditions.checkArgument(total == (wait + failed + passed), "审核数据接口全部审批活动数" + total + " 数和数据接口三种状态总数：" + (wait + failed + passed));
            Preconditions.checkArgument((pendingTotal + passedTotal + rejectTotal) == (wait + failed + passed), "审核数据接口三种状态总数" + (wait + failed + passed) + " 列表三种状态总数：" + (pendingTotal + passedTotal + rejectTotal));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("优惠券审批--活动审批页全部审批=待审批+审批通过+审批未通过");
        }
    }
}
