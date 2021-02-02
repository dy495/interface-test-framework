package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.agency.Visitor;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.AppletCodeBusinessTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherExpireTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.WechatCustomerTaskAwardLogicRuleEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.EquityEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.EquityPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.ShareManagerEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing.ShareManagerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher.IVoucher;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.voucher.VoucherGenerator;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 异常情况
 *
 * @author wangmin
 * @date 2021/1/29 10:48
 */
public class AbnormalManager extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_DAILY;
    private static final EnumAccount ADMINISTRATOR = EnumAccount.ADMINISTRATOR_DAILY;
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

    @Test(description = "修改生日积分，积分异常")
    public void vipMarketIng_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene equityPageScene = EquityPageScene.builder().build();
            Integer equityId = visitor.invokeApi(equityPageScene).getJSONArray("list").stream().map(e -> (JSONObject) e).filter(e -> e.getString("equity_name").equals("生日积分")).map(e -> e.getInteger("equity_id")).findFirst().orElse(0);
            //修改权益
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
            saveData("修改生日积分，积分异常");
        }
    }

    @Test(description = "修改生日积分，描述异常")
    public void vipMarketIng_system_2() {
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
            saveData("修改生日积分，描述异常");
        }
    }

    @Test(description = "修改免费洗车，次数异常")
    public void vipMarketIng_system_3() {
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
            saveData("修改生日积分，积分异常");
        }
    }

    @Test(description = "修改分享内容，说明异常")
    public void vipMarketIng_system_4() {
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
                    IScene shareManagerEditScene = ShareManagerEditScene.builder().id(id).taskExplain(desc)
                            .awardScore(1000).awardCustomerRule(WechatCustomerTaskAwardLogicRuleEnum.EVERY_TIME.name())
                            .awardCardVolumeId(voucherId).takeEffectType(VoucherExpireTypeEnum.EXPIRE_DAYS.name()).build();
                    visitor.invokeApi(shareManagerEditScene);
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("修改分享内容，说明异常");
        }
    }

    @Test(description = "修改分享内容，积分异常")
    public void vipMarketIng_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IVoucher voucher = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher();
            Integer voucherId = Math.toIntExact(voucher.getVoucherId());
            IScene shareManagerPageScene = ShareManagerPageScene.builder().build();
            JSONArray list = visitor.invokeApi(shareManagerPageScene).getJSONArray("list");
            List<Integer> ids = list.stream().map(e -> (JSONObject) e).filter(e -> !e.getString("business_type").equals(AppletCodeBusinessTypeEnum.ACTIVITY_APPLY_PRIORITY.getKey())).map(e -> e.getInteger("id")).collect(Collectors.toList());
            ids.forEach(id -> {
                Integer[] awardScores = {null, 100000000};
                Arrays.stream(awardScores).forEach(awardScore -> {
                    IScene shareManagerEditScene = ShareManagerEditScene.builder().id(id).taskExplain(EnumDesc.MESSAGE_DESC.getDesc())
                            .awardScore(awardScore).awardCustomerRule(WechatCustomerTaskAwardLogicRuleEnum.EVERY_TIME.name())
                            .awardCardVolumeId(voucherId).takeEffectType(VoucherExpireTypeEnum.EXPIRE_DAYS.name()).build();
                    visitor.invokeApi(shareManagerEditScene);
                });
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("修改分享内容，积分异常");
        }
    }

    @Test(description = "修改分享内容，图片异常")
    public void vipMarketIng_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IVoucher voucher = new VoucherGenerator.Builder().visitor(visitor).voucherStatus(VoucherStatusEnum.WORKING).buildVoucher();
            Integer voucherId = Math.toIntExact(voucher.getVoucherId());
            IScene shareManagerPageScene = ShareManagerPageScene.builder().build();
            JSONArray list = visitor.invokeApi(shareManagerPageScene).getJSONArray("list");
            List<Integer> ids = list.stream().map(e -> (JSONObject) e).filter(e -> e.getString("business_type").equals(AppletCodeBusinessTypeEnum.ACTIVITY_APPLY_PRIORITY.getKey())).map(e -> e.getInteger("id")).collect(Collectors.toList());
            String picPath = util.getPicPath("src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/大于500k.jpg");
            List<String> picturePathList = new ArrayList<>();
            picturePathList.add(picPath);
            ids.forEach(id -> picturePathList.forEach(picturePath -> {
                IScene shareManagerEditScene = ShareManagerEditScene.builder().id(id).taskExplain(EnumDesc.MESSAGE_DESC.getDesc())
                        .picturePath(picturePath).awardScore(100).awardCustomerRule(WechatCustomerTaskAwardLogicRuleEnum.EVERY_TIME.name())
                        .awardCardVolumeId(voucherId).takeEffectType(VoucherExpireTypeEnum.EXPIRE_DAYS.name()).build();
                visitor.invokeApi(shareManagerEditScene);
            }));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("修改分享内容，图片异常");
        }
    }

}
