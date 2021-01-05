package com.haisheng.framework.testng.bigScreen.jiaochen.wm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.OperationApprovalVO;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.OperationRegisterVO;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumArticleStatus;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.banner.Banner;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.ActivityRegister;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner.BannerEdit;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.LoginUtil;
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

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 内容运营
 */
public class ContentOperation extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = ScenarioUtil.getInstance();
    LoginUtil user = new LoginUtil();
    BusinessUtil util = new BusinessUtil();
    private static final Integer size = 100;
    private static final EnumAccount administrator = EnumAccount.ADMINISTRATOR;
    private static final EnumAppletToken applet = EnumAppletToken.JC_WM_DAILY;
    private static final EnumAccount marketing = EnumAccount.MARKETING;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.produce = EnumProduce.JC.name();
        commonConfig.referer = EnumTestProduce.JIAOCHEN_DAILY.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumTestProduce.JIAOCHEN_DAILY.getShopId();
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
        user.login(administrator);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "跳转活动/文章的条数=展示中的文章条数之和")
    public void banner_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int num = util.getArticleIdList().size();
            ArticlePage.ArticlePageBuilder builder = ArticlePage.builder();
            int total = jc.invokeApi(builder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            List<JSONObject> list = new ArrayList<>();
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(builder.page(i).size(size).build()).getJSONArray("list");
                list.addAll(array.stream().map(e -> (JSONObject) e).filter(e -> e.getString("status_name").equals(EnumArticleStatus.SHOW.getTypeName()))
                        .collect(Collectors.toList()));
            }
            int articleNum = list.size();
            CommonUtil.valueView(num, articleNum);
            Preconditions.checkArgument(num == articleNum, "跳转活动/文章的条数：" + num + "展示中的文章条数之和：" + articleNum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("跳转活动/文章的条数=展示中的文章条数之和");
        }
    }

    @Test(description = "内容运营--banner--填写banner1-banner5的内容")
    public void banner_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> articleIds = util.getArticleIdList();
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture";
            File file = new File(filePath);
            File[] files = file.listFiles();
            assert files != null;
            List<String> base64s = Arrays.stream(files).filter(e -> e.toString().contains("banner")).map(e -> new ImageUtil().getImageBinary(e.getPath()))
                    .collect(Collectors.toList());
            List<String> picPaths = base64s.stream().map(e -> jc.invokeApi(FileUpload.builder().pic(e).isPermanent(false).ratio(1.5).ratioStr("3：2").build()).getString("pic_path"))
                    .collect(Collectors.toList());
            IScene scene = BannerEdit.builder()
                    .bannerImgUrl1(picPaths.get(0)).articleId1(articleIds.get(0))
                    .bannerImgUrl2(picPaths.get(1)).articleId2(articleIds.get(1))
                    .bannerImgUrl3(picPaths.get(2)).articleId3(articleIds.get(2))
                    .bannerImgUrl4(picPaths.get(3)).articleId4(articleIds.get(3))
                    .bannerImgUrl5(picPaths.get(4)).articleId5(articleIds.get(4))
                    .build();
            jc.invokeApi(scene);
            user.loginApplet(applet);
            JSONArray array = jc.invokeApi(Banner.builder().build()).getJSONArray("list");
            List<Long> appletArticleIds = array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("article_id")).collect(Collectors.toList());
            Preconditions.checkArgument(appletArticleIds.equals(articleIds.subList(0, 5)), "pc端文章为：" + appletArticleIds + " applet端文章为：" + articleIds.subList(0, 5));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--banner--填写banner1-banner5的内容");
        }
    }

    @Test(description = "内容运营--小程序报名一次,对应活动审批列表+1&&我的活动列表+1&&活动报名名单分子+1")
    public void operationRegister_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int size = 1;
            List<Long> activityIds = util.getCanApplyActivityList(size);
            //全部开启
            activityIds.forEach(e -> jc.invokeApi(StatusChange.builder().id(e).build()));
            Long activityId = activityIds.get(size - 1);
            String activityName = util.getActivityName(activityId);
            //报名人数
            IScene scene = ApprovalPage.builder().articleId(String.valueOf(activityId)).build();
            int num = jc.invokeApi(scene).getInteger("total");
            user.loginApplet(applet);
            //我的活动列表数
            int applyNum = util.getAppletActivityNum();
            int registerNum = jc.appletArticleDetail(String.valueOf(activityId)).getInteger("register_num");
            //报名
            jc.invokeApi(ActivityRegister.builder().id(activityId).name(EnumAccount.MARKETING.name()).phone(EnumAccount.MARKETING.getPhone()).num(1).build());
            //我的报名列表消息+1
            int newApplyNum = util.getAppletActivityNum();
            CommonUtil.valueView(applyNum, newApplyNum);
            Preconditions.checkArgument(newApplyNum == applyNum + 1, "报名前我的活动列表数：" + applyNum + "报名后我的活动列表数：" + newApplyNum);
            int newRegisterNum = jc.appletArticleDetail(String.valueOf(activityId)).getInteger("register_num");
            CommonUtil.valueView(registerNum, newRegisterNum);
            Preconditions.checkArgument(newRegisterNum == registerNum + 1, "报名前活动报名表名单分子数：" + applyNum + "报名后活动报名表名单分子数：" + newApplyNum);
            //报名后报名列表数量+1
            user.login(administrator);
            int newNum = jc.invokeApi(scene).getInteger("total");
            CommonUtil.valueView(num, newNum);
            Preconditions.checkArgument(newNum == num + 1, activityName + "报名前此活动报名人数：" + num + "报名后此活动报名人数：" + newNum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--小程序报名一次,对应活动审批列表+1&&我的活动列表+1&&活动报名名单分子+1");
        }
    }

    @Test(description = "内容运营--报名管理--已入选数=审批列表中已通过数")
    public void operationRegister_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<OperationRegisterVO> operationRegisters = new ArrayList<>();
            RegisterPage.RegisterPageBuilder registerBuilder = RegisterPage.builder();
            int total = jc.invokeApi(registerBuilder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(registerBuilder.page(i).size(size).build()).getJSONArray("list");
                operationRegisters.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSON.parseObject(JSON.toJSONString(e), OperationRegisterVO.class)).collect(Collectors.toList()));
            }
            List<Integer> statusNameList = operationRegisters.stream().map(e -> (int) util.getOperationApprovalInfo(e.getId()).stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count()).collect(Collectors.toList());
            List<Integer> passedList = operationRegisters.stream().map(OperationRegisterVO::getPassedNum).collect(Collectors.toList());
            List<String> titleList = operationRegisters.stream().map(OperationRegisterVO::getTitle).collect(Collectors.toList());
            for (int i = 0; i < statusNameList.size(); i++) {
                CommonUtil.valueView(statusNameList.get(i), passedList.get(i));
                Preconditions.checkArgument(statusNameList.get(i).equals(passedList.get(i)), titleList.get(i) + "已入选数：" + passedList.get(i) + "审核列表已通过数：" + statusNameList.get(i));
                CommonUtil.logger(titleList.get(i));
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--已入选数=审批列表中已通过数");
        }
    }

    @Test(description = "内容运营--报名管理--已报名数=审批列表数")
    public void operationRegister_data_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<OperationRegisterVO> operationRegisters = new ArrayList<>();
            RegisterPage.RegisterPageBuilder registerBuilder = RegisterPage.builder();
            int total = jc.invokeApi(registerBuilder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(registerBuilder.page(i).size(size).build()).getJSONArray("list");
                operationRegisters.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSON.parseObject(JSON.toJSONString(e), OperationRegisterVO.class)).collect(Collectors.toList()));
            }
            List<Integer> statusNameList = operationRegisters.stream().map(e -> util.getOperationApprovalInfo(e.getId()).size()).collect(Collectors.toList());
            List<Integer> registerList = operationRegisters.stream().map(OperationRegisterVO::getRegisterNum).collect(Collectors.toList());
            List<String> titleList = operationRegisters.stream().map(OperationRegisterVO::getTitle).collect(Collectors.toList());
            for (int i = 0; i < statusNameList.size(); i++) {
                CommonUtil.valueView(statusNameList.get(i), registerList.get(i));
                Preconditions.checkArgument(statusNameList.get(i).equals(registerList.get(i)), titleList.get(i) + "已报名数：" + registerList.get(i) + "审批列表数：" + statusNameList.get(i));
                CommonUtil.logger(titleList.get(i));
            }
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--已报名数=审批列表数");
        }
    }

    @Test(description = "内容运营--报名管理--PC活动报名中审批通过1个报名客户，审核页面中已通过+1，待审批-1&&申请列表已入选+1&&入选时间=当前时间", priority = 1)
    public void operationRegister_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            List<OperationRegisterVO> operationRegisters = new ArrayList<>();
            RegisterPage.RegisterPageBuilder registerBuilder = RegisterPage.builder();
            int total = jc.invokeApi(registerBuilder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(registerBuilder.page(i).size(size).build()).getJSONArray("list");
                operationRegisters.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSON.parseObject(JSON.toJSONString(e), OperationRegisterVO.class)).collect(Collectors.toList()));
            }
            List<OperationRegisterVO> operationRegisterVOList = operationRegisters.stream().filter(e -> util.getOperationApprovalInfo(e.getId()).stream().anyMatch(approvalVO -> approvalVO.getStatusName().equals("待审批"))).collect(Collectors.toList());
            Long activityId = operationRegisterVOList.get(0).getId();
            Integer passedNum = operationRegisterVOList.get(0).getPassedNum();
            String activityName = operationRegisterVOList.get(0).getTitle();
            int approvalNum = (int) util.getOperationApprovalInfo(activityId).stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
            int passNum = (int) util.getOperationApprovalInfo(activityId).stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count();
            CommonUtil.valueView(activityName, activityId, approvalNum, passNum, passedNum);
            Long id = util.getOperationApprovalInfo(activityId).stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).map(OperationApprovalVO::getId).findFirst().orElse(null);
            //审批通过
            List<Long> list = new ArrayList<>();
            list.add(id);
            jc.invokeApi(Approval.builder().registerIds(list).status("APPROVAL_CONFIRM").build());
            //审批通过后和数据数量
            int newApprovalNum = (int) util.getOperationApprovalInfo(activityId).stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
            int newPassNum = (int) util.getOperationApprovalInfo(activityId).stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count();
            Preconditions.checkArgument(newApprovalNum == approvalNum - 1, activityName + "审批通过前待审核数量：" + approvalNum + "审批通过后待审核数量：" + newApprovalNum);
            Preconditions.checkArgument(newPassNum == passNum + 1, activityName + "审批通过前已通过数量：" + passNum + "审批通过后已通过数量：" + newPassNum);
            int newPassedNum = util.getOperationRegisterInfo(activityId).getPassedNum();
            Preconditions.checkArgument(newPassedNum == passedNum + 1, "审批通过前已入选数量：" + passedNum + "审批通过后已入选数量：" + newPassedNum);
            //入选时间
            String passedDate = util.getOperationApprovalInfo(activityId, id).getPassedDate();
            Preconditions.checkArgument(passedDate.equals(date), "审批通过后入选时间为：" + passedDate);
            //操作账号
            String userAccountName = util.getOperationApprovalInfo(activityId, id).getUserAccountName();
            Preconditions.checkArgument(userAccountName.equals(administrator.getName()), "审批通过后为操作账号为：" + userAccountName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--PC活动报名中审批通过1个报名客户，审核页面中已通过+1，待审批-1&&申请列表已入选+1&&入选时间=当前时间");
        }
    }

    @Test(description = "内容运营--报名管理--同一个人报名n个不同的活动,n个活动中都有此人的报名信息", priority = 1)
    public void operationRegister_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            List<Long> activityIds = util.getCanApplyActivityList(3);
            //报名
            user.loginApplet(applet);
            activityIds.forEach(e -> util.applyActivity(e));
            //报名后，均有该用户的报名信息
            user.login(administrator);
            List<List<OperationApprovalVO>> list = activityIds.stream().map(e -> util.getOperationApprovalInfo(e)).collect(Collectors.toList());
            List<OperationApprovalVO> operationApprovalVOList = list.stream().map(subList -> subList.stream().filter(e -> e.getRegisterTime().equals(date)).findFirst().orElse(null)).collect(Collectors.toList());
            operationApprovalVOList.forEach(e -> {
                Preconditions.checkArgument(e.getCustomerName().equals("隔壁老王"), e.getTitle() + "报名客户名称为：隔壁老王" + "审批列表客户名称为：" + e.getCustomerName());
                Preconditions.checkArgument(e.getPhone().equals(marketing.getPhone()), e.getTitle() + "报名联系方式为：" + marketing.getPhone() + "审批列表客户名称为：" + e.getPhone());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--同一个人报名n个不同的活动,n个活动中都有此人的报名信息");
        }
    }

    @Test(description = "内容运营--报名管理--已入选<=活动名额")
    public void operationRegister_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<OperationRegisterVO> operationRegisters = new ArrayList<>();
            RegisterPage.RegisterPageBuilder registerBuilder = RegisterPage.builder();
            int total = jc.invokeApi(registerBuilder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(registerBuilder.page(i).size(size).build()).getJSONArray("list");
                operationRegisters.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSON.parseObject(JSON.toJSONString(e), OperationRegisterVO.class)).collect(Collectors.toList()));
            }
            operationRegisters.forEach(e -> Preconditions.checkArgument(e.getTotalQuota() >= e.getPassedNum(), e.getTitle() + " 活动名额数：" + e.getTotalQuota() + " 已入选数：" + e.getPassedNum()));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--已入选<=活动名额");
        }
    }
}
