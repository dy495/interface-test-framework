package com.haisheng.framework.testng.bigScreen.jiaochen.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.operation.ArticlePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.om.ArticleStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.banner.AppletBannerScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ActivityManageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner.EditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticleList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticlePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 内容运营
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class ContentOperationCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_DAILY;
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
    @Test(description = "内容运营--banner--上传图片不符合3:2")
    public void banner_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
            String base64 = new ImageUtil().getImageBinary(filePath);
            String message = FileUpload.builder().pic(base64).permanentPicType(0).isPermanent(false).ratio(1.5).ratioStr("3：2").build().invoke(visitor, false).getString("message");
            String err = "图片宽高比不符合3：2的要求";
            CommonUtil.checkResult("图片比", "非3：2", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--banner--上传图片不符合3:2");
        }
    }

    //bug
    @Test(description = "banner--跳转活动/文章的条数=展示中的文章+进行中或者已结束活动条数之和")
    public void banner_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int num = ArticleList.builder().build().invoke(visitor).getJSONArray("list").size();
            IScene articlePageScene = ArticlePageScene.builder().build();
            int articlePageListSize = (int) util.collectBeanList(articlePageScene, ArticlePageBean.class).stream().filter(e -> e.getStatusName().equals(ArticleStatusEnum.SHOW.getTypeName())).count();
            int passedSTotal = ActivityManageListScene.builder().status(ActivityStatusEnum.PASSED.getId()).build().invoke(visitor).getInteger("total");
            int finishTotal = ActivityManageListScene.builder().status(ActivityStatusEnum.FINISH.getId()).build().invoke(visitor).getInteger("total");
            CommonUtil.checkResult("跳转活动/文章的条数", passedSTotal + finishTotal + articlePageListSize, num);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("banner--跳转活动/文章的条数=展示中的文章+进行中活动条数之和");
        }
    }

    //ok
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
            List<String> picPaths = base64s.stream().map(e -> visitor.invokeApi(FileUpload.builder().pic(e).permanentPicType(0).isPermanent(false).ratio(1.5).ratioStr("3：2").build()).getString("pic_path")).collect(Collectors.toList());
            JSONArray array = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("article_id", articleIds.get(0));
            jsonObject1.put("banner_img_url", picPaths.get(0));
            jsonObject1.put("banner_id", 11);
            jsonObject1.put("banner_select", "banner1");
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("article_id", articleIds.get(1));
            jsonObject2.put("banner_img_url", picPaths.get(1));
            jsonObject2.put("banner_id", 12);
            jsonObject2.put("banner_select", "banner2");
            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("article_id", articleIds.get(2));
            jsonObject3.put("banner_img_url", picPaths.get(2));
            jsonObject3.put("banner_id", 13);
            jsonObject3.put("banner_select", "banner3");
            JSONObject jsonObject4 = new JSONObject();
            jsonObject4.put("article_id", articleIds.get(3));
            jsonObject4.put("banner_img_url", picPaths.get(3));
            jsonObject4.put("banner_id", 14);
            jsonObject4.put("banner_select", "banner4");
            JSONObject jsonObject5 = new JSONObject();
            jsonObject5.put("article_id", articleIds.get(4));
            jsonObject5.put("banner_img_url", picPaths.get(4));
            jsonObject5.put("banner_id", 15);
            jsonObject5.put("banner_select", "banner5");
            array.add(jsonObject1);
            array.add(jsonObject2);
            array.add(jsonObject3);
            array.add(jsonObject4);
            array.add(jsonObject5);
            EditScene.builder().list(array).build().invoke(visitor);
            user.loginApplet(APPLET_USER_ONE);
            JSONArray list = AppletBannerScene.builder().build().invoke(visitor).getJSONArray("list");
            List<Long> appletArticleIds = list.stream().map(e -> (JSONObject) e).map(e -> e.getLong("article_id")).collect(Collectors.toList());
            CommonUtil.checkResultPlus("pc端文章为：", appletArticleIds, "applet端文章为：", articleIds.subList(0, 5));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--banner--填写banner1-banner5的内容");
        }
    }
}
//
//    //bug
//    @Test(description = "内容运营--报名管理--到达报名名额后不可再审批通过,提示：人数已达到报名上线", enabled = false)
//    public void operationRegister_system_1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //创建只有1个名额的文章
//            Long articleId = new JcFunction().creteArticle("1");
//            //报名
//            EnumAppletToken[] appletTokens = new EnumAppletToken[]{applet, applet1};
//            Arrays.stream(appletTokens).forEach(e -> {
//                //报名
//                user.loginApplet(e);
//                util.applyArticle(articleId);
//            });
//            user.login(administrator);
//            List<OperationApproval> operationApprovals = util.getApprovalList(articleId);
//            List<Long> ids = operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).map(OperationApproval::getId).collect(Collectors.toList());
//            List<Long> list1 = new ArrayList<>();
//            list1.add(ids.get(0));
//            List<Long> list2 = new ArrayList<>();
//            list2.add(ids.get(1));
//            //审批通过
//            String message1 = jc.invokeApi(ApprovalScene.builder().registerIds(list1).status("APPROVAL_CONFIRM").build(), false).getString("message");
//            Preconditions.checkArgument(message1.equals("success"), "第一个审批通过" + CommonUtil.result("success", message1));
//            String message2 = jc.invokeApi(ApprovalScene.builder().registerIds(list2).status("APPROVAL_CONFIRM").build(), false).getString("message");
//            Preconditions.checkArgument(message2.equals("success"), "第一个审批通过" + CommonUtil.result("success", message2));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("内容运营--报名管理--到达报名名额后不可再审批通过,提示：人数已达到报名上线");
//        }
//    }
//
//    @Test(description = "内容运营--报名管理--小程序报名一次,对应活动审批列表+1&&我的活动列表+1&&活动报名名单分子+1")
//    public void operationRegister_data_1() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            int size = 1;
//            List<Long> activityIds = util.getCanApplyArticleList(size);
//            //全部开启
//            activityIds.forEach(e -> jc.invokeApi(StatusChange.builder().id(e).build()));
//            Long articleId = activityIds.get(size - 1);
//            String activityName = util.getArticleName(articleId);
//            //报名人数
//            IScene scene = ApprovalPage.builder().articleId(String.valueOf(articleId)).build();
//            int num = jc.invokeApi(scene).getInteger("total");
//            user.loginApplet(applet);
//            //我的活动列表数
//            int applyNum = util.getAppletArticleNum();
//            int registerNum = jc.appletArticleDetail(String.valueOf(articleId)).getInteger("register_num");
//            //报名
//            util.applyArticle(articleId);
//            //我的报名列表消息+1
//            int newApplyNum = util.getAppletArticleNum();
//            CommonUtil.valueView(applyNum, newApplyNum);
//            Preconditions.checkArgument(newApplyNum == applyNum + 1, "报名前我的活动列表数：" + applyNum + "报名后我的活动列表数：" + newApplyNum);
//            int newRegisterNum = jc.appletArticleDetail(String.valueOf(articleId)).getInteger("register_num");
//            CommonUtil.valueView(registerNum, newRegisterNum);
//            Preconditions.checkArgument(newRegisterNum == registerNum + 1, "报名前活动报名表名单分子数：" + applyNum + "报名后活动报名表名单分子数：" + newApplyNum);
//            //报名后报名列表数量+1
//            user.login(administrator);
//            int newNum = jc.invokeApi(scene).getInteger("total");
//            CommonUtil.valueView(num, newNum);
//            Preconditions.checkArgument(newNum == num + 1, activityName + "报名前此活动报名人数：" + num + "报名后此活动报名人数：" + newNum);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("内容运营--报名管理--小程序报名一次,对应活动审批列表+1&&我的活动列表+1&&活动报名名单分子+1");
//        }
//    }
//
//    @Test(description = "内容运营--报名管理--已入选数=审批列表中已通过数")
//    public void operationRegister_data_2() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            IScene scene = RegisterPage.builder().build();
//            List<OperationRegister> operationRegisters = util.collectBean(scene, OperationRegister.class);
//            List<Integer> statusNameList = operationRegisters.stream().map(e -> (int) util.getApprovalList(e.getId()).stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count()).collect(Collectors.toList());
//            List<Integer> passedList = operationRegisters.stream().map(OperationRegister::getPassedNum).collect(Collectors.toList());
//            List<String> titleList = operationRegisters.stream().map(OperationRegister::getTitle).collect(Collectors.toList());
//            for (int i = 0; i < statusNameList.size(); i++) {
//                CommonUtil.valueView(statusNameList.get(i), passedList.get(i));
//                Preconditions.checkArgument(statusNameList.get(i).equals(passedList.get(i)), titleList.get(i) + "已入选数：" + passedList.get(i) + "审核列表已通过数：" + statusNameList.get(i));
//                CommonUtil.logger(titleList.get(i));
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("内容运营--报名管理--已入选数=审批列表中已通过数");
//        }
//    }
//
//    @Test(description = "内容运营--报名管理--已报名数=审批列表数")
//    public void operationRegister_data_3() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            IScene scene = RegisterPage.builder().build();
//            List<OperationRegister> operationRegisters = util.collectBean(scene, OperationRegister.class);
//            List<Integer> statusNameList = operationRegisters.stream().map(e -> util.getApprovalList(e.getId()).size()).collect(Collectors.toList());
//            List<Integer> registerList = operationRegisters.stream().map(OperationRegister::getRegisterNum).collect(Collectors.toList());
//            List<String> titleList = operationRegisters.stream().map(OperationRegister::getTitle).collect(Collectors.toList());
//            for (int i = 0; i < statusNameList.size(); i++) {
//                CommonUtil.valueView(statusNameList.get(i), registerList.get(i));
//                Preconditions.checkArgument(statusNameList.get(i).equals(registerList.get(i)), titleList.get(i) + "已报名数：" + registerList.get(i) + "审批列表数：" + statusNameList.get(i));
//                CommonUtil.logger(titleList.get(i));
//            }
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("内容运营--报名管理--已报名数=审批列表数");
//        }
//    }
//
//    @Test(description = "内容运营--报名管理--PC活动报名中审批通过1个报名客户，审核页面中已通过+1，待审批-1&&申请列表已入选+1&&入选时间=当前时间&&操作人=当前帐号", priority = 1)
//    public void operationRegister_data_4() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String date = DateTimeUtil.getFormat(new Date());
//            //遍历报名列表，如果其中含有待审核的任务，获取文章id&标题
//            List<OperationRegister> operationRegisters = util.getRegisterList();
//            OperationRegister operationRegister = operationRegisters.stream().filter(e -> util.getApprovalList(e.getId()).stream().anyMatch(approvalVO -> approvalVO.getStatusName().equals("待审批"))).findFirst().orElse(null);
//            assert operationRegister != null;
//            Long articleId = operationRegister.getId();
//            String articleTitle = operationRegister.getTitle();
//            //已入选数量
//            Integer passedNum = operationRegister.getPassedNum();
//            List<OperationApproval> operationApprovals = util.getApprovalList(articleId);
//            //审批中数量
//            int approvalNum = (int) operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
//            //已通过数量
//            int passNum = (int) operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count();
//            CommonUtil.valueView(articleTitle, articleId, approvalNum, passNum, passedNum);
//            Long id = operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).map(OperationApproval::getId).findFirst().orElse(null);
//            //审批通过
//            List<Long> list = new ArrayList<>();
//            list.add(id);
//            jc.invokeApi(ApprovalScene.builder().registerIds(list).status("APPROVAL_CONFIRM").build());
//            //审批通过后和数据数量
//            List<OperationApproval> newOperationApprovals = util.getApprovalList(articleId);
//            int newApprovalNum = (int) newOperationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
//            int newPassNum = (int) newOperationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count();
//            Preconditions.checkArgument(newApprovalNum == approvalNum - 1, articleTitle + "审批通过前待审核数量：" + approvalNum + "审批通过后待审核数量：" + newApprovalNum);
//            Preconditions.checkArgument(newPassNum == passNum + 1, articleTitle + "审批通过前已通过数量：" + passNum + "审批通过后已通过数量：" + newPassNum);
//            int newPassedNum = util.getRegisterInfo(articleId).getPassedNum();
//            Preconditions.checkArgument(newPassedNum == passedNum + 1, "审批通过前已入选数量：" + passedNum + "审批通过后已入选数量：" + newPassedNum);
//            //入选时间
//            String passedDate = Objects.requireNonNull(newOperationApprovals.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null)).getPassedDate();
//            Preconditions.checkArgument(passedDate.equals(date), "审批通过后入选时间为：" + passedDate);
//            //操作账号
//            String userAccountName = Objects.requireNonNull(newOperationApprovals.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null)).getUserAccountName();
//            Preconditions.checkArgument(userAccountName.equals(administrator.getName()), "审批通过后为操作账号为：" + userAccountName);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("内容运营--报名管理--PC活动报名中审批通过1个报名客户，审核页面中已通过+1，待审批-1&&申请列表已入选+1&&入选时间=当前时间");
//        }
//    }
//
//    @Test(description = "内容运营--报名管理--PC活动报名中审批拒绝1个报名客户，审核页面中已拒绝+1，待审批-1&&操作账号=当前帐号")
//    public void operationRegister_data_5() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //遍历报名列表，如果其中含有待审核的任务，获取文章id&标题
//            List<OperationRegister> operationRegisters = util.getRegisterList();
//            OperationRegister operationRegister = operationRegisters.stream().filter(e -> util.getApprovalList(e.getId()).stream().anyMatch(approvalVO -> approvalVO.getStatusName().equals("待审批"))).findFirst().orElse(null);
//            assert operationRegister != null;
//            Long articleId = operationRegister.getId();
//            String articleTitle = operationRegister.getTitle();
//            //获取已拒绝和待审批数量
//            List<OperationApproval> operationApprovals = util.getApprovalList(articleId);
//            int refuseNum = (int) operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已拒绝")).count();
//            int approvalNum = (int) operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
//            CommonUtil.valueView(articleTitle, articleId, approvalNum, refuseNum, approvalNum);
//            //获取审批任务id
//            Long id = operationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).map(OperationApproval::getId).findFirst().orElse(null);
//            //审批拒绝
//            List<Long> list = new ArrayList<>();
//            list.add(id);
//            jc.invokeApi(ApprovalScene.builder().registerIds(list).status("APPROVAL_REJECT").build());
//            //拒绝之后的已拒绝&待审批数量
//            List<OperationApproval> newOperationApprovals = util.getApprovalList(articleId);
//            int newRefuseNum = (int) newOperationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已拒绝")).count();
//            int newApprovalNum = (int) newOperationApprovals.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
//            Preconditions.checkArgument(newApprovalNum == approvalNum - 1, articleTitle + "审批前待审批数量：" + approvalNum + "审批拒绝后待审批数量：" + newApprovalNum);
//            Preconditions.checkArgument(newRefuseNum == refuseNum + 1, articleTitle + "审批前已拒绝数量：" + refuseNum + "审批拒绝后已拒绝数量：" + newRefuseNum);
//            //操作账号
//            String userAccountName = Objects.requireNonNull(newOperationApprovals.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null)).getUserAccountName();
//            Preconditions.checkArgument(userAccountName.equals(administrator.getName()), "审批通过后为操作账号为：" + userAccountName);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("内容运营--报名管理--PC活动报名中审批拒绝1个报名客户，审核页面中已拒绝+1，待审批-1&&操作账号=当前帐号");
//        }
//    }
//
//    @Test(description = "内容运营--报名管理--同一个人报名n个不同的活动,n个活动中都有此人的报名信息")
//    public void operationRegister_data_6() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String date = DateTimeUtil.getFormat(new Date());
//            List<Long> activityIds = util.getCanApplyArticleList(3);
//            //报名
//            user.loginApplet(applet);
//            activityIds.forEach(e -> util.applyArticle(e));
//            //报名后，均有该用户的报名信息
//            user.login(administrator);
//            List<List<OperationApproval>> list = activityIds.stream().map(e -> util.getApprovalList(e)).collect(Collectors.toList());
//            List<OperationApproval> operationApprovalList = list.stream().map(subList -> subList.stream().filter(e -> e.getRegisterTime().equals(date)).findFirst().orElse(null)).collect(Collectors.toList());
//            operationApprovalList.forEach(e -> {
//                Preconditions.checkArgument(e.getCustomerName().equals("隔壁老王"), e.getTitle() + "报名客户名称为：隔壁老王" + "审批列表客户名称为：" + e.getCustomerName());
//                Preconditions.checkArgument(e.getPhone().equals(marketing.getPhone()), e.getTitle() + "报名联系方式为：" + marketing.getPhone() + "审批列表客户名称为：" + e.getPhone());
//            });
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("内容运营--报名管理--同一个人报名n个不同的活动,n个活动中都有此人的报名信息");
//        }
//    }
//
//    @Test(description = "内容运营--报名管理--已入选<=活动名额")
//    public void operationRegister_data_7() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            IScene scene = RegisterPage.builder().build();
//            List<OperationRegister> operationRegisters = util.collectBean(scene, OperationRegister.class);
//            operationRegisters.forEach(e -> Preconditions.checkArgument(e.getTotalQuota() >= e.getPassedNum(), e.getTitle() + " 活动名额数：" + e.getTotalQuota() + " 已入选数：" + e.getPassedNum()));
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("内容运营--报名管理--已入选<=活动名额");
//        }
//    }
//
//    @Test(description = "内容运营--报名管理--多人报名，批量取消，已取消=原待已取消+批量数")
//    public void operationRegister_data_8() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String date = DateTimeUtil.getFormat(new Date());
//            Long articleId = new JcFunction().creteArticle(null, "ARTICLE_BUTTON");
//            String title = util.getRegisterInfo(articleId).getTitle();
//            //三人报名
//            EnumAppletToken[] tokens = new EnumAppletToken[]{applet, applet1, applet2};
//            Arrays.stream(tokens).forEach(token -> {
//                //登录
//                user.loginApplet(token);
//                //报名
//                util.applyArticle(articleId);
//            });
//            //取消数量
//            user.login(administrator);
//            List<OperationApproval> operationApprovals = util.getApprovalList(articleId);
//            long cancelNum = operationApprovals.stream().filter(e -> e.getStatusName().equals("已取消")).count();
//            Arrays.stream(tokens).forEach(token -> {
//                //登录
//                user.loginApplet(token);
//                //取消
//                Long id = util.getAppletArticleList().stream().filter(e -> e.getStatusName().equals("待审批") && e.getTitle().equals(title) && e.getTimeStr().equals(date))
//                        .map(AppointmentActivity::getId).findFirst().orElse(null);
//                jc.appletactivityCancel(String.valueOf(id));
//            });
//            //新取消数量
//            user.login(administrator);
//            List<OperationApproval> newOperationApprovals = util.getApprovalList(articleId);
//            long newCancelNum = newOperationApprovals.stream().filter(e -> e.getStatusName().equals("已取消")).count();
//            CommonUtil.valueView(cancelNum, newCancelNum);
//            Preconditions.checkArgument(newCancelNum == cancelNum + tokens.length, title + "去消前已取消数：" + cancelNum + "取消后已取消数：" + newCancelNum);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("内容运营--报名管理--多人报名，批量取消，已取消=原待已取消+批量数");
//        }
//    }
//
//    @Test(description = "内容运营--报名管理--多人报名，批量通过，待审批=原待审批数-批量数&&已通过=原已通过数+批量数")
//    public void operationRegister_data_9() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            Long voucherId = util.getVoucherId(EnumVP.ONE.getVoucherName());
//            JSONArray array = new JSONArray();
//            array.add(voucherId);
//            Long articleId = new JcFunction().creteArticle(array, "ARTICLE_BUTTON");
//            String title = util.getRegisterInfo(articleId).getTitle();
//            //三人报名
//            EnumAppletToken[] tokens = new EnumAppletToken[]{applet, applet1, applet2};
//            Arrays.stream(tokens).forEach(token -> {
//                //登录
//                user.loginApplet(token);
//                //报名
//                util.applyArticle(articleId);
//            });
//            user.login(administrator);
//            List<OperationApproval> operationApprovals = util.getApprovalList(articleId);
//            long passNum = operationApprovals.stream().filter(e -> e.getStatusName().equals("已通过")).count();
//            long approvalNum = operationApprovals.stream().filter(e -> e.getStatusName().equals("待审批")).count();
//            //批量通过
//            List<Long> ids = util.getApprovalList(articleId).stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).map(OperationApproval::getId).collect(Collectors.toList());
//            jc.invokeApi(ApprovalScene.builder().registerIds(ids).status("APPROVAL_CONFIRM").build());
//            List<OperationApproval> newOperationApprovals = util.getApprovalList(articleId);
//            long newApprovalNum = newOperationApprovals.stream().filter(e -> e.getStatusName().equals("待审批")).count();
//            long newPassNum = newOperationApprovals.stream().filter(e -> e.getStatusName().equals("已通过")).count();
//            CommonUtil.valueView(approvalNum, newApprovalNum, passNum, newPassNum);
//            Preconditions.checkArgument(newApprovalNum == approvalNum - ids.size(), title + "批量通过前待审批数：" + approvalNum + "批量通过后待审批数：" + newApprovalNum);
//            Preconditions.checkArgument(newPassNum == passNum + ids.size(), title + "批量通过前已通过数：" + passNum + "批量通过后已通过数：" + newPassNum);
//        } catch (Exception | AssertionError e) {
//            collectMessage(e);
//        } finally {
//            saveData("内容运营--报名管理--多人报名，批量通过，待审批=原待审批数-批量数&&已通过=原已通过数+批量数");
//        }
//    }
