package com.haisheng.framework.testng.bigScreen.jiaochen.wm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.AppointmentActivityVO;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.OperationApprovalVO;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.OperationRegisterVO;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumArticleStatus;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumVP;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.banner.Banner;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.ActivityRegister;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner.BannerEdit;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.LoginUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.JcFunction;
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
 * 内容运营
 */
public class ContentOperation extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = ScenarioUtil.getInstance();
    LoginUtil user = new LoginUtil();
    BusinessUtil util = new BusinessUtil();
    private static final Integer size = 100;
    private static final EnumAccount administrator = EnumAccount.ADMINISTRATOR;
    private static final EnumAccount marketing = EnumAccount.MARKETING;
    private static final EnumAppletToken applet = EnumAppletToken.JC_WM_DAILY;
    private static final EnumAppletToken applet1 = EnumAppletToken.JC_GLY_DAILY;
    private static final EnumAppletToken applet2 = EnumAppletToken.JC_XMF_DAILY;

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

    @Test(description = "内容运营--banner--上传图片不符合3:2")
    public void banner_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
            String base64 = new ImageUtil().getImageBinary(filePath);
            String message = jc.invokeApi(FileUpload.builder().pic(base64).isPermanent(false).ratio(1.5).ratioStr("3：2").build(), false).getString("message");
            String err = "图片宽高比不符合3：2的要求";
            CommonUtil.checkResult("图片比", "非3：2", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--banner--上传图片不符合3:2");
        }
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

    @Test(description = "内容运营--报名管理--小程序报名一次,对应活动审批列表+1&&我的活动列表+1&&活动报名名单分子+1")
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
            jc.invokeApi(ActivityRegister.builder().id(activityId).name(EnumAccount.MARKETING.name()).phone(EnumAccount.MARKETING.getPhone()).num(1).build());
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
            saveData("内容运营--报名管理--小程序报名一次,对应活动审批列表+1&&我的活动列表+1&&活动报名名单分子+1");
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
            List<Integer> statusNameList = operationRegisters.stream().map(e -> (int) util.getApprovalList(e.getId()).stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count()).collect(Collectors.toList());
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
            List<Integer> statusNameList = operationRegisters.stream().map(e -> util.getApprovalList(e.getId()).size()).collect(Collectors.toList());
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

    @Test(description = "内容运营--报名管理--PC活动报名中审批通过1个报名客户，审核页面中已通过+1，待审批-1&&申请列表已入选+1&&入选时间=当前时间&&操作人=当前帐号", priority = 1)
    public void operationRegister_data_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            //遍历报名列表，如果其中含有待审核的任务，获取文章id&标题
            List<OperationRegisterVO> operationRegisters = util.getRegisterList();
            OperationRegisterVO operationRegisterVO = operationRegisters.stream().filter(e -> util.getApprovalList(e.getId()).stream().anyMatch(approvalVO -> approvalVO.getStatusName().equals("待审批"))).findFirst().orElse(null);
            assert operationRegisterVO != null;
            Long articleId = operationRegisterVO.getId();
            String articleTitle = operationRegisterVO.getTitle();
            //已入选数量
            Integer passedNum = operationRegisterVO.getPassedNum();
            List<OperationApprovalVO> operationApprovalVOs = util.getApprovalList(articleId);
            //审批中数量
            int approvalNum = (int) operationApprovalVOs.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
            //已通过数量
            int passNum = (int) operationApprovalVOs.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count();
            CommonUtil.valueView(articleTitle, articleId, approvalNum, passNum, passedNum);
            Long id = operationApprovalVOs.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).map(OperationApprovalVO::getId).findFirst().orElse(null);
            //审批通过
            List<Long> list = new ArrayList<>();
            list.add(id);
            jc.invokeApi(Approval.builder().registerIds(list).status("APPROVAL_CONFIRM").build());
            //审批通过后和数据数量
            List<OperationApprovalVO> newOperationApprovalVOs = util.getApprovalList(articleId);
            int newApprovalNum = (int) newOperationApprovalVOs.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
            int newPassNum = (int) newOperationApprovalVOs.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已通过")).count();
            Preconditions.checkArgument(newApprovalNum == approvalNum - 1, articleTitle + "审批通过前待审核数量：" + approvalNum + "审批通过后待审核数量：" + newApprovalNum);
            Preconditions.checkArgument(newPassNum == passNum + 1, articleTitle + "审批通过前已通过数量：" + passNum + "审批通过后已通过数量：" + newPassNum);
            int newPassedNum = util.getRegisterInfo(articleId).getPassedNum();
            Preconditions.checkArgument(newPassedNum == passedNum + 1, "审批通过前已入选数量：" + passedNum + "审批通过后已入选数量：" + newPassedNum);
            //入选时间
            String passedDate = Objects.requireNonNull(newOperationApprovalVOs.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null)).getPassedDate();
            Preconditions.checkArgument(passedDate.equals(date), "审批通过后入选时间为：" + passedDate);
            //操作账号
            String userAccountName = Objects.requireNonNull(newOperationApprovalVOs.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null)).getUserAccountName();
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
            List<OperationRegisterVO> operationRegisters = util.getRegisterList();
            OperationRegisterVO operationRegisterVO = operationRegisters.stream().filter(e -> util.getApprovalList(e.getId()).stream().anyMatch(approvalVO -> approvalVO.getStatusName().equals("待审批"))).findFirst().orElse(null);
            assert operationRegisterVO != null;
            Long articleId = operationRegisterVO.getId();
            String articleTitle = operationRegisterVO.getTitle();
            //获取已拒绝和待审批数量
            List<OperationApprovalVO> operationApprovalVOs = util.getApprovalList(articleId);
            int refuseNum = (int) operationApprovalVOs.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已拒绝")).count();
            int approvalNum = (int) operationApprovalVOs.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
            CommonUtil.valueView(articleTitle, articleId, approvalNum, refuseNum, approvalNum);
            //获取审批任务id
            Long id = operationApprovalVOs.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).map(OperationApprovalVO::getId).findFirst().orElse(null);
            //审批拒绝
            List<Long> list = new ArrayList<>();
            list.add(id);
            jc.invokeApi(Approval.builder().registerIds(list).status("APPROVAL_REJECT").build());
            //拒绝之后的已拒绝&待审批数量
            List<OperationApprovalVO> newOperationApprovalVOs = util.getApprovalList(articleId);
            int newRefuseNum = (int) newOperationApprovalVOs.stream().filter(approvalVO -> approvalVO.getStatusName().equals("已拒绝")).count();
            int newApprovalNum = (int) newOperationApprovalVOs.stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).count();
            Preconditions.checkArgument(newApprovalNum == approvalNum - 1, articleTitle + "审批前待审批数量：" + approvalNum + "审批拒绝后待审批数量：" + newApprovalNum);
            Preconditions.checkArgument(newRefuseNum == refuseNum + 1, articleTitle + "审批前已拒绝数量：" + refuseNum + "审批拒绝后已拒绝数量：" + newRefuseNum);
            //操作账号
            String userAccountName = Objects.requireNonNull(newOperationApprovalVOs.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null)).getUserAccountName();
            Preconditions.checkArgument(userAccountName.equals(administrator.getName()), "审批通过后为操作账号为：" + userAccountName);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--PC活动报名中审批拒绝1个报名客户，审核页面中已拒绝+1，待审批-1&&操作账号=当前帐号");
        }
    }

    @Test(description = "内容运营--报名管理--同一个人报名n个不同的活动,n个活动中都有此人的报名信息", priority = 1)
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
            List<List<OperationApprovalVO>> list = activityIds.stream().map(e -> util.getApprovalList(e)).collect(Collectors.toList());
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
    public void operationRegister_data_7() {
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

    @Test(description = "内容运营--报名管理--多人报名，批量通过，待审批=原待审批数-批量数&&已通过=原已通过数+批量数")
    public void operationRegister_data_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long voucherId = util.getVoucherId(EnumVP.ONE.getVoucherName());
            JSONArray array = new JSONArray();
            array.add(voucherId);
            Long articleId = new JcFunction().creteArticle(array, "ARTICLE_BUTTON");
            String title = util.getRegisterInfo(articleId).getTitle();
            //三人报名
            EnumAppletToken[] tokens = new EnumAppletToken[]{applet, applet1, applet2};
            Arrays.stream(tokens).forEach(token -> {
                //登录
                user.loginApplet(token);
                //报名
                util.applyArticle(articleId);
            });
            user.login(administrator);
            List<OperationApprovalVO> operationApprovalVOs = util.getApprovalList(articleId);
            long passNum = operationApprovalVOs.stream().filter(e -> e.getStatusName().equals("已通过")).count();
            long approvalNum = operationApprovalVOs.stream().filter(e -> e.getStatusName().equals("待审批")).count();
            //批量通过
            List<Long> ids = util.getApprovalList(articleId).stream().filter(approvalVO -> approvalVO.getStatusName().equals("待审批")).map(OperationApprovalVO::getId).collect(Collectors.toList());
            jc.invokeApi(Approval.builder().registerIds(ids).status("APPROVAL_CONFIRM").build());
            List<OperationApprovalVO> newOperationApprovalVOs = util.getApprovalList(articleId);
            long newApprovalNum = newOperationApprovalVOs.stream().filter(e -> e.getStatusName().equals("待审批")).count();
            long newPassNum = newOperationApprovalVOs.stream().filter(e -> e.getStatusName().equals("已通过")).count();
            CommonUtil.valueView(approvalNum, newApprovalNum, passNum, newPassNum);
            Preconditions.checkArgument(newApprovalNum == approvalNum - ids.size(), title + "批量通过前待审批数：" + approvalNum + "批量通过后待审批数：" + newApprovalNum);
            Preconditions.checkArgument(newPassNum == passNum + ids.size(), title + "批量通过前已通过数：" + passNum + "批量通过后已通过数：" + newPassNum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--多人报名，批量通过，待审批=原待审批数-批量数&&已通过=原已通过数+批量数");
        }
    }

    @Test(description = "内容运营--报名管理--多人报名，批量取消，已取消=原待已取消+批量数")
    public void operationRegister_data_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = DateTimeUtil.getFormat(new Date());
            Long articleId = new JcFunction().creteArticle(null, "ARTICLE_BUTTON");
            String title = util.getRegisterInfo(articleId).getTitle();
            //三人报名
            EnumAppletToken[] tokens = new EnumAppletToken[]{applet, applet1, applet2};
            Arrays.stream(tokens).forEach(token -> {
                //登录
                user.loginApplet(token);
                //报名
                util.applyArticle(articleId);
            });
            //取消数量
            user.login(administrator);
            List<OperationApprovalVO> operationApprovalVOs = util.getApprovalList(articleId);
            long cancelNum = operationApprovalVOs.stream().filter(e -> e.getStatusName().equals("已取消")).count();
            Arrays.stream(tokens).forEach(token -> {
                //登录
                user.loginApplet(token);
                //取消
                Long id = util.getAppletArticleList().stream().filter(e -> e.getStatusName().equals("待审批") && e.getTitle().equals(title) && e.getTimeStr().equals(date))
                        .map(AppointmentActivityVO::getId).findFirst().orElse(null);
                jc.appletactivityCancel(String.valueOf(id));
            });
            //新取消数量
            user.login(administrator);
            List<OperationApprovalVO> newOperationApprovalVOs = util.getApprovalList(articleId);
            long newCancelNum = newOperationApprovalVOs.stream().filter(e -> e.getStatusName().equals("已取消")).count();
            CommonUtil.valueView(cancelNum, newCancelNum);
            Preconditions.checkArgument(newCancelNum == cancelNum + tokens.length, title + "去消前已取消数：" + cancelNum + "取消后已取消数：" + newCancelNum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--报名管理--多人报名，批量取消，已取消=原待已取消+批量数");
        }
    }
}
