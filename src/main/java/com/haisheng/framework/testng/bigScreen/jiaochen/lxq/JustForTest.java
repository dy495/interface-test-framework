package com.haisheng.framework.testng.bigScreen.jiaochen.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.JiaoChenInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.PublicParameter;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.FissionVoucherAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageContentMarketingAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ManageRecruitAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JustForTest extends TestCaseCommon implements TestCaseStd {

    //--------日常开始---------
    ScenarioUtil jc = new ScenarioUtil();
    private static final EnumTestProduct product = EnumTestProduct.JC_DAILY;
    //private static final EnumAppletToken APPLET_USER = EnumAppletToken.JC_GLY_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
    BusinessUtil businessUtil = new BusinessUtil(visitor);
    SupporterUtil supporterUtil = new SupporterUtil(visitor);
    PublicParameter pp = new PublicParameter();
    UserUtil user = new UserUtil(visitor);
    JiaoChenInfo info = new JiaoChenInfo();


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.product = product.getAbbreviation();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "jc-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.referer = "https://servicewechat.com/wxbd41de85739a00c7/0/page-frame.html";
        commonConfig.shopId = product.getShopId();
        commonConfig.roleId = "603";
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
    }


    //--------日常结束---------
    //--------线上---------


//    ScenarioUtil jc = new ScenarioUtil();
//    private static final EnumTestProduce product = EnumTestProduce.JC_ONLINE;
//    private static final EnumAccount ADMINISTRATOR = EnumAccount.JC_ALL_ONLINE;
//    public VisitorProxy visitor = new VisitorProxy(product);
//    //    BusinessUtil businessUtil = new BusinessUtil(visitor);
//    BusinessUtilOnline businessUtil = new BusinessUtilOnline(visitor);
//    SupporterUtil supporterUtil = new SupporterUtil(visitor);
//    PublicParameter pp = new PublicParameter();
//    UserUtil user = new UserUtil(visitor);
//    CommonConfig commonConfig = new CommonConfig();
//jiaoChenInfoOnline info = new jiaoChenInfoOnline();
//
//    /**
//     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
//     */
//    @BeforeClass
//    @Override
//    public void initial() {
//        logger.debug("before class initial");
//        jc.changeIpPort(EnumTestProduce.JC_ONLINE.getPort());
//        //替换checklist的相关信息
//        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
//        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
//        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
//        commonConfig.product = product.getAbbreviation();
//        //替换jenkins-job的相关信息
//        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
//        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
//        //替换钉钉推送
//        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
//        //放入shopId
//        commonConfig.roleId = ADMINISTRATOR.getRoleId();
//        commonConfig.referer = product.getReferer();
//        commonConfig.shopId = product.getShopId();
//        beforeClassInit(commonConfig);
//        logger.debug("jc: " + jc);
//    }



    //--------线上结束---------

    //-----------以下不用动-------------


    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @deprecated :get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        jc.pcLogin("13114785236", pp.password);
    }






    /**
     * 创建裂变活动-客户标签
     */
    @Test(description = "创建裂变活动-客户标签")
    public void activityType12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            List<Integer> chooseLabels = new ArrayList<>();
            chooseLabels.add(1000);
            chooseLabels.add(1);
            chooseLabels.add(100);
            chooseLabels.add(2000);
            chooseLabels.add(3000);
            String[][] label = {{"CAR_WELFARE", "车福利"}, {"CAR_INFORMATION", "车资讯"}, {"CAR_LIFE", "车生活"}, {"CAR_ACVITITY", "车活动"}, {"CAR_KNOWLEDGE", "车知识"}};
            for (int i = 0; i < label.length; i++) {
                System.err.println(label.length + "-------" + 1);
                SupporterUtil supporterUtil = new SupporterUtil(visitor);
                PublicParameter pp = new PublicParameter();
                List<String> picList = new ArrayList<>();
                picList.add(supporterUtil.getPicPath());
                // 创建被邀请者和分享者的信息字段
                JSONObject invitedVoucher = businessUtil.getInvitedVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherAllowUseInventory(voucherId)), 2, "", "", 3);
                JSONObject shareVoucher = businessUtil.getShareVoucher(voucherId, 1, String.valueOf(businessUtil.getVoucherAllowUseInventory(voucherId)), 2, "", "", 3);
                IScene scene = FissionVoucherAddScene.builder()
                        .type(1)
                        .participationLimitType(1)
                        .chooseLabels(chooseLabels)
                        .receiveLimitType(0)
                        .title("裂变活动标签为-" + label[i][1] + "-" + (int) (Math.random() * 10000))
                        .rule(pp.rule)
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label(label[i][0])
                        .picList(picList)
                        .shareNum("3")
                        .shareVoucher(shareVoucher)
                        .invitedVoucher(invitedVoucher)
                        .build();
                Long activityId = visitor.invokeApi(scene).getLong("id");
                //审批通过招募活动
                businessUtil.getApprovalPassed(activityId);
                Preconditions.checkArgument(activityId > 0, "裂变活动创建失败");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建裂变活动-各标签");
        }
    }

    @Test(dataProvider = "TYPE")
    public void activityType5(String lable, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.WORKING).buildVoucher().getVoucherId();
//            Long voucherId=49L;
            //客户限制
            List<Integer> labels = new ArrayList<>();
            String[][] label = {{"1", "普通会员"}};
            List<String> picList = new ArrayList<>();
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            PublicParameter pp = new PublicParameter();
            picList.add(0, supporterUtil.getPicPath());
            //填写报名所需要信息
            List<Boolean> isShow = new ArrayList<>();
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            isShow.add(false);
            List<Boolean> isRequired = new ArrayList<>();
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            isRequired.add(false);
            JSONArray registerInformationList = this.businessUtil.getRegisterInformationList(isShow, isRequired);
            //报名成功奖励
            JSONArray registerObject = businessUtil.getRewardVouchers(voucherId, 1, businessUtil.getVoucherAllowUseInventory(voucherId));
            //卡券有效期
            JSONObject voucherValid = businessUtil.getVoucherValid(2, null, null, 10);
            //创建招募活动-共有的--基础信息
            for (int i = 0; i < label.length; i++) {
                labels.add(Integer.valueOf(label[i][0]));
                ManageRecruitAddScene.ManageRecruitAddSceneBuilder builder = ManageRecruitAddScene.builder()
                        .type(2)
                        .participationLimitType(1)
                        .chooseLabels(labels)
                        .title("招募-" + mess)
                        .startDate(businessUtil.getStartDate())
                        .endDate(businessUtil.getEndDate())
                        .applyStart(businessUtil.getStartDate())
                        .applyEnd(businessUtil.getEndDate())
                        .isLimitQuota(true)
                        .quota(10)
                        .subjectType(supporterUtil.getSubjectType())
                        .subjectId(supporterUtil.getSubjectDesc(supporterUtil.getSubjectType()))
                        .label(lable)
                        .picList(picList)
                        .rule(pp.rule)
                        .registerInformationList(registerInformationList)
                        .successReward(true)
                        .rewardReceiveType(0)
                        .isNeedApproval(true);
                if (true) {
                    builder.rewardVouchers(registerObject)
                            .voucherValid(voucherValid);
                }
                IScene scene = builder.build();
                Long activityId = visitor.invokeApi(scene).getLong("id");
                //审批通过招募活动
                businessUtil.getApprovalPassed(activityId);
                Preconditions.checkArgument(activityId > 0, "招募活动创建失败");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("招募活动-各标签");
        }
    }

    @DataProvider(name = "TYPE")
    public Object[] type() {
        return new Object[][]{
                {"CAR_WELFARE", "车福利"},
                {"CAR_INFORMATION", "车资讯"},
                {"CAR_LIFE", "车生活"},
                {"CAR_ACVITITY", "车活动"},
                {"CAR_KNOWLEDGE", "车知识"}

        };
    }

    /**
     * 营销活动-各标签
     */
    @Test(dataProvider = "TYPE")
    public void activityType2(String lable, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            SupporterUtil supporterUtil = new SupporterUtil(visitor);
            List<String> picList = new ArrayList<>();
            picList.add(0, businessUtil.getPicPath());

                    IScene scene = ManageContentMarketingAddScene.builder()
                            .type(3)
                            .participationLimitType(0)
                            .title("内容营销-"+mess)
                            .rule(pp.rule)
                            .startDate(businessUtil.getStartDate())
                            .endDate(businessUtil.getEndDate())
                            .subjectType(supporterUtil.getSubjectType())
                            .label(lable)
                            .picList(picList)
                            .actionPoint(1)
                            .build();
                    visitor.invokeApi(scene);
            Long activityId = visitor.invokeApi(scene).getLong("id");
            //审批通过招募活动
            businessUtil.getApprovalPassed(activityId);
            Preconditions.checkArgument(activityId > 0, "活动创建失败");



        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("营销活动");
        }
    }

    //新建文章
    @Test(dataProvider = "ARTICLE") //ok
    public void addArticle(String title, String pic_type, String content, String label) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray pic_list1 = new JSONArray();
            //pic_list1.add("general_temp/9c6fbc65-0f1f-4341-9892-1f1052b6aa04");
            pic_list1.add(info.getLogo());

            JSONArray pic_list2 = new JSONArray();
            pic_list2.add("");
            pic_list2.add("");
            pic_list2.add("");
            JSONObject obj = jc.addArticleNotChk(title, pic_type, pic_list1, content, label, "ARTICEL", null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null);
            int code = obj.getInteger("code");
            Long id = obj.getJSONObject("data").getLong("id");
            //关闭文章
            jc.changeArticleStatus(id);
            //删除文章
            jc.delArticle(id);


            Preconditions.checkArgument(code == 1000, "期待1000，实际" + code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【内容运营】，新建文章1张图");
        }
    }

    @DataProvider(name = "ARTICLE") //要补充
    public Object[] article() {
        return new String[][]{
                {"1234", "ONE_BIG", info.stringone, "CAR_WELFARE"},
                {info.stringten, "ONE_BIG", info.stringfifty, "CAR_INFORMATION"},
                {info.string20, "ONE_LEFT", info.stringten, "CAR_LIFE"},
                {info.stringten, "ONE_LEFT", info.stringlong, "CAR_ACVITITY"},
                {info.stringsix, "ONE_LEFT", info.stringlong, "CAR_KNOWLEDGE"},

        };
    }

}
