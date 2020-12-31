package com.haisheng.framework.testng.bigScreen.jiaochen.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumArticleStatus;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.banner.Banner;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.ActivityRegister;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner.BannerEdit;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ApprovalPage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticleList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticlePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.StatusChange;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.LoginUtil;
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
import java.util.ArrayList;
import java.util.Arrays;
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
            int num = getArticleId().size();
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
            List<Long> articleIds = getArticleId();
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

    public List<Long> getArticleId() {
        JSONArray array = jc.invokeApi(ArticleList.builder().build()).getJSONArray("list");
        return array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
    }

    @Test(description = "内容运营--小程序报名一次,对应活动审批列表+1", enabled = false)
    public void operationRegister_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int size = 1;
            List<Long> activityIds = util.getCanApplyActivityList(5);
            //全部开启
            activityIds.forEach(e -> jc.invokeApi(StatusChange.builder().id(e).build()));
            user.login(administrator);
            //报名人数
            IScene scene = ApprovalPage.builder().articleId(String.valueOf(activityIds.get(size))).build();
            int num = jc.invokeApi(scene).getInteger("total");
            //报名
            user.loginApplet(applet);
            int applyNum = util.getAppletActivityNum();
            jc.invokeApi(ActivityRegister.builder().id(activityIds.get(size)).name(EnumAccount.MARKETING.name()).phone(EnumAccount.MARKETING.getPhone()).num(1).build());
            //报名后报名列表数量
            user.login(administrator);
            int newNum = jc.invokeApi(scene).getInteger("total");
            CommonUtil.valueView(num, newNum);
            Preconditions.checkArgument(num == newNum);
            //我的报名列表消息+1
            int newApplyNum = util.getAppletActivityNum();
            CommonUtil.valueView(applyNum, newApplyNum);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--小程序报名一次,对应活动审批列表+1");
        }
    }

    @Test
    public void test() {
        user.loginApplet(applet);
        int num = util.getAppletActivityNum();
        System.err.println(num);
    }
}
