package com.haisheng.framework.testng.bigScreen.jiaochen.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumArticleStatus;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.FileUpload;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner.BannerEdit;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticleList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticlePage;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统配置测试用例
 */
public class ContentOperation extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = ScenarioUtil.getInstance();
    LoginUtil user = new LoginUtil();
    private static final Integer size = 100;
    private static final EnumAccount marketing = EnumAccount.MARKETING;
    private static final EnumAccount administrator = EnumAccount.ADMINISTRATOR;

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
            IScene scene = ArticleList.builder().build();
            int num = jc.invokeApi(scene).getJSONArray("list").size();
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

    @Test(description = "跳转活动/文章的条数=展示中的文章条数之和")
    public void banner_data_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String picturePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/";
            String banner1Path = picturePath + "banner-1.jpg";
            String pic = new ImageUtil().getImageBinary(banner1Path);
            IScene scene = FileUpload.builder().pic(pic).isPermanent(false).ratio(1.5).ratioStr("3：2").build();
            String picPath = jc.invokeApi(scene).getString("pic_path");
            IScene scene1 = BannerEdit.builder().bannerImgUrl1(picPath).build();
            jc.invokeApi(scene1);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("跳转活动/文章的条数=展示中的文章条数之和");
        }
    }

}
