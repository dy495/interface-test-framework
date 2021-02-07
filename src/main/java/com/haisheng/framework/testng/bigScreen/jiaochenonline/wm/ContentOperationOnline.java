package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.OperationApproval;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.OperationRegister;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumArticleStatus;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.banner.BannerScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletActivityRegisterScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner.BannerEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.LoginUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.wm.util.BusinessUtilOnline;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务管理测试用例
 */
public class ContentOperationOnline extends TestCaseCommon implements TestCaseStd {
    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
    BusinessUtilOnline util = new BusinessUtilOnline();
    LoginUtil user = new LoginUtil();
    private static final Integer size = 100;
    private static final EnumAccount administrator = EnumAccount.ADMINISTRATOR_ONLINE;
    private static final EnumAppletToken applet = EnumAppletToken.JC_WM_ONLINE;
    private static final EnumAppletToken appletUser = EnumAppletToken.JC_GLY_ONLINE;
    private static final EnumAccount marketing = EnumAccount.MARKETING_ONLINE;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.product = EnumProduce.JC.name();
        commonConfig.referer = EnumTestProduce.JIAOCHEN_ONLINE.getReferer();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_ONLINE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumTestProduce.JIAOCHEN_ONLINE.getShopId();
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
            List<String> base64s = Arrays.stream(files).filter(e -> e.toString().contains("banner")).map(e -> new ImageUtil().getImageBinary(e.getPath())).collect(Collectors.toList());
            List<String> picPaths = base64s.stream().map(e -> jc.invokeApi(FileUpload.builder().pic(e).isPermanent(false).ratio(1.5).ratioStr("3：2").build()).getString("pic_path")).collect(Collectors.toList());
            IScene scene = BannerEditScene.builder()
                    .bannerImgUrl1(picPaths.get(0)).articleId1(articleIds.get(0)).bannerId1(16)
                    .bannerImgUrl2(picPaths.get(1)).articleId2(articleIds.get(0)).bannerId2(17)
                    .bannerImgUrl3(picPaths.get(2)).articleId3(articleIds.get(0)).bannerId3(18)
                    .bannerImgUrl4(picPaths.get(3)).articleId4(articleIds.get(0)).bannerId4(19)
                    .bannerImgUrl5(picPaths.get(4)).articleId5(articleIds.get(0)).bannerId5(20)
                    .build();
            jc.invokeApi(scene);
            user.loginApplet(applet);
            JSONArray array = jc.invokeApi(BannerScene.builder().build()).getJSONArray("list");
            List<Long> appletArticleIds = array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("article_id")).collect(Collectors.toList());
            appletArticleIds.forEach(e -> Preconditions.checkArgument(e.equals(articleIds.get(0)), "pc端文章为：" + e + " applet端文章为：" + articleIds.get(0)));
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
            List<Long> activityIds = util.getCanApplyArticleList(size);
            //全部开启
            activityIds.forEach(e -> jc.invokeApi(StatusChange.builder().id(e).build()));
            Long activityId = activityIds.get(size - 1);
            String activityName = util.getArticleName(activityId);
            //报名人数
            IScene scene = ApprovalPage.builder().articleId(String.valueOf(activityId)).build();
            int num = jc.invokeApi(scene).getInteger("total");
            user.loginApplet(applet);
            //我的活动列表数
            int applyNum = util.getAppletArticleNum();
            int registerNum = jc.appletArticleDetail(String.valueOf(activityId)).getInteger("register_num");
            //报名
            jc.invokeApi(AppletActivityRegisterScene.builder().id(activityId).name(EnumAccount.MARKETING_DAILY.name()).phone(EnumAccount.MARKETING_DAILY.getPhone()).num(1).build());
            //我的报名列表消息+1
            int newApplyNum = util.getAppletArticleNum();
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
            List<OperationRegister> operationRegisters = new ArrayList<>();
            RegisterPage.RegisterPageBuilder registerBuilder = RegisterPage.builder();
            int total = jc.invokeApi(registerBuilder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(registerBuilder.page(i).size(size).build()).getJSONArray("list");
                operationRegisters.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSON.parseObject(JSON.toJSONString(e), OperationRegister.class)).collect(Collectors.toList()));
            }
            List<Integer> statusNameList = operationRegisters.stream().map(e -> (int) util.getApprovalInfo(e.getId()).stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count()).collect(Collectors.toList());
            List<Integer> passedList = operationRegisters.stream().map(OperationRegister::getPassedNum).collect(Collectors.toList());
            List<String> titleList = operationRegisters.stream().map(OperationRegister::getTitle).collect(Collectors.toList());
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
            List<OperationRegister> operationRegisters = new ArrayList<>();
            RegisterPage.RegisterPageBuilder registerBuilder = RegisterPage.builder();
            int total = jc.invokeApi(registerBuilder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(registerBuilder.page(i).size(size).build()).getJSONArray("list");
                operationRegisters.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSON.parseObject(JSON.toJSONString(e), OperationRegister.class)).collect(Collectors.toList()));
            }
            List<Integer> statusNameList = operationRegisters.stream().map(e -> util.getApprovalList(e.getId()).size()).collect(Collectors.toList());
            List<Integer> registerList = operationRegisters.stream().map(OperationRegister::getRegisterNum).collect(Collectors.toList());
            List<String> titleList = operationRegisters.stream().map(OperationRegister::getTitle).collect(Collectors.toList());
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

    @Test(description = "内容运营--报名管理--PC活动报名中审批通过1个报名客户，审核页面中已通过+1，待审批-1&&申请列表已入选+1&&入选时间=当前时间&&操作人=当前帐号", priority = 1)
    public void operationRegister_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            //遍历报名列表，如果其中含有待审核的任务，获取文章id&标题
            List<OperationRegister> operationRegisters = util.getRegisterList();
            OperationRegister operationRegister = operationRegisters.stream().filter(e -> util.getApprovalList(e.getId()).stream().anyMatch(approvalVO -> approvalVO.getStatusName().equals("待审批"))).findFirst().orElse(null);
            assert operationRegister != null;
            Long articleId = operationRegister.getId();
            String articleTitle = operationRegister.getTitle();
            //已入选数量
            Integer passedNum = operationRegister.getPassedNum();
            List<OperationApproval> operationApprovals = util.getApprovalList(articleId);
            //审批中数量
            int approvalNum = (int) operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
            //已通过数量
            int passNum = (int) operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count();
            CommonUtil.valueView(articleTitle, articleId, approvalNum, passNum, passedNum);
            Long id = operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).map(OperationApproval::getId).findFirst().orElse(null);
            //审批通过
            List<Long> list = new ArrayList<>();
            list.add(id);
            jc.invokeApi(ApprovalScene.builder().registerIds(list).status("APPROVAL_CONFIRM").build());
            //审批通过后和数据数量
            List<OperationApproval> newOperationApprovals = util.getApprovalList(articleId);
            int newApprovalNum = (int) newOperationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
            int newPassNum = (int) newOperationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count();
            Preconditions.checkArgument(newApprovalNum == approvalNum - 1, articleTitle + "审批通过前待审核数量：" + approvalNum + "审批通过后待审核数量：" + newApprovalNum);
            Preconditions.checkArgument(newPassNum == passNum + 1, articleTitle + "审批通过前已通过数量：" + passNum + "审批通过后已通过数量：" + newPassNum);
            int newPassedNum = util.getRegisterInfo(articleId).getPassedNum();
            Preconditions.checkArgument(newPassedNum == passedNum + 1, "审批通过前已入选数量：" + passedNum + "审批通过后已入选数量：" + newPassedNum);
            //入选时间
            String passedDate = Objects.requireNonNull(newOperationApprovals.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null)).getPassedDate();
            Preconditions.checkArgument(passedDate.equals(date), "审批通过后入选时间为：" + passedDate);
            //操作账号
            String userAccountName = Objects.requireNonNull(newOperationApprovals.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null)).getUserAccountName();
            Preconditions.checkArgument(userAccountName.equals(administrator.getName()), "审批通过后为操作账号为：" + userAccountName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--PC活动报名中审批通过1个报名客户，审核页面中已通过+1，待审批-1&&申请列表已入选+1&&入选时间=当前时间");
        }
    }

    @Test(description = "内容运营--报名管理--PC活动报名中审批拒绝1个报名客户，审核页面中已拒绝+1，待审批-1&&操作账号=当前帐号")
    public void operationRegister_data_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //遍历报名列表，如果其中含有待审核的任务，获取文章id&标题
            OperationRegister operationRegister = util.getContainApplyRegisterInfo(appletUser);
            Long articleId = operationRegister.getId();
            String articleTitle = operationRegister.getTitle();
            //获取已拒绝和待审批数量
            List<OperationApproval> operationApprovals = util.getApprovalList(articleId);
            int refuseNum = (int) operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已拒绝")).count();
            int approvalNum = (int) operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
            CommonUtil.valueView(articleTitle, articleId, approvalNum, refuseNum, approvalNum);
            //获取审批任务id
            Long id = operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).map(OperationApproval::getId).findFirst().orElse(null);
            //审批拒绝
            List<Long> list = new ArrayList<>();
            list.add(id);
            jc.invokeApi(ApprovalScene.builder().registerIds(list).status("APPROVAL_REJECT").build());
            //拒绝之后的已拒绝&待审批数量
            List<OperationApproval> newOperationApprovals = util.getApprovalList(articleId);
            int newRefuseNum = (int) newOperationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已拒绝")).count();
            int newApprovalNum = (int) newOperationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
            Preconditions.checkArgument(newApprovalNum == approvalNum - 1, articleTitle + "审批前待审批数量：" + approvalNum + "审批拒绝后待审批数量：" + newApprovalNum);
            Preconditions.checkArgument(newRefuseNum == refuseNum + 1, articleTitle + "审批前已拒绝数量：" + refuseNum + "审批拒绝后已拒绝数量：" + newRefuseNum);
            //操作账号
            String userAccountName = Objects.requireNonNull(newOperationApprovals.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null)).getUserAccountName();
            Preconditions.checkArgument(userAccountName.equals(administrator.getName()), "审批通过后为操作账号为：" + userAccountName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--PC活动报名中审批拒绝1个报名客户，审核页面中已拒绝+1，待审批-1&&操作账号=当前帐号");
        }
    }

    @Test(description = "内容运营--报名管理--同一个人报名n个不同的活动,n个活动中都有此人的报名信息")
    public void operationRegister_data_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            List<Long> activityIds = util.getCanApplyArticleList(3);
            //报名
            user.loginApplet(applet);
            activityIds.forEach(e -> util.applyArticle(e));
            //报名后，均有该用户的报名信息
            user.login(administrator);
            List<List<OperationApproval>> list = activityIds.stream().map(e -> util.getApprovalList(e)).collect(Collectors.toList());
            List<OperationApproval> operationApprovalList = list.stream().map(subList -> subList.stream().filter(e -> e.getRegisterTime().equals(date)).findFirst().orElse(null)).collect(Collectors.toList());
            operationApprovalList.forEach(e -> {
                Preconditions.checkArgument(e.getCustomerName().equals("小王"), e.getTitle() + "报名客户名称为：小王" + "审批列表客户名称为：" + e.getCustomerName());
                Preconditions.checkArgument(e.getPhone().equals(marketing.getPhone()), e.getTitle() + "报名联系方式为：" + marketing.getPhone() + "审批列表客户名称为：" + e.getPhone());
            });
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--同一个人报名n个不同的活动,n个活动中都有此人的报名信息");
        }
    }

    @Test(description = "内容运营--报名管理--已入选<=活动名额")
    public void operationRegister_data_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<OperationRegister> operationRegisters = new ArrayList<>();
            RegisterPage.RegisterPageBuilder registerBuilder = RegisterPage.builder();
            int total = jc.invokeApi(registerBuilder.build()).getInteger("total");
            int s = CommonUtil.getTurningPage(total, size);
            for (int i = 1; i < s; i++) {
                JSONArray array = jc.invokeApi(registerBuilder.page(i).size(size).build()).getJSONArray("list");
                operationRegisters.addAll(array.stream().map(e -> (JSONObject) e).map(e -> JSON.parseObject(JSON.toJSONString(e), OperationRegister.class)).collect(Collectors.toList()));
            }
            operationRegisters.forEach(e -> Preconditions.checkArgument(e.getTotalQuota() >= e.getPassedNum(), e.getTitle() + " 活动名额数：" + e.getTotalQuota() + " 已入选数：" + e.getPassedNum()));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--已入选<=活动名额");
        }
    }
}
