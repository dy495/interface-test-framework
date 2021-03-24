package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.ArticlePage;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.om.ArticleStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.banner.AppletBannerScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ActivityManageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner.BannerEdit;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUpload;
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
public class ContentOperationCaseOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JIAOCHEN_ONLINE;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_ONLINE;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_ONLINE;
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
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
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

    //ok
    @Test(description = "banner--跳转活动/文章的条数=展示中的文章+进行中或者已结束活动条数之和")
    public void banner_data_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int num = util.getArticleIdList().size();
            IScene articlePageScene = ArticlePageScene.builder().build();
            int articlePageListSize = (int) util.collectBean(articlePageScene, ArticlePage.class).stream().filter(e -> e.getStatusName().equals(ArticleStatusEnum.SHOW.getTypeName())).count();
            IScene activityManageListScene = ActivityManageListScene.builder().build();
            int activityManageListSize = (int) util.collectBean(activityManageListScene, JSONObject.class).stream()
                    .filter(e -> e.getString("status_name").equals(ActivityStatusEnum.PASSED.getStatusName()) || e.getString("status_name").equals(ActivityStatusEnum.FINISH.getStatusName())).count();
            CommonUtil.checkResult("跳转活动/文章的条数", activityManageListSize + articlePageListSize, num);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("banner--跳转活动/文章的条数=展示中的文章+进行中活动条数之和");
        }
    }

    //ok
    @Test(description = "内容运营--banner--填写banner1-banner5的内容", enabled = false)
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
            IScene scene = BannerEdit.builder()
                    .bannerImgUrl1(picPaths.get(0)).articleId1(articleIds.get(0))
                    .bannerImgUrl2(picPaths.get(1)).articleId2(articleIds.get(1))
                    .bannerImgUrl3(picPaths.get(2)).articleId3(articleIds.get(2))
                    .bannerImgUrl4(picPaths.get(3)).articleId4(articleIds.get(3))
                    .bannerImgUrl5(picPaths.get(4)).articleId5(articleIds.get(4)).build();
            visitor.invokeApi(scene);
            user.loginApplet(APPLET_USER_ONE);
            JSONArray array = visitor.invokeApi(AppletBannerScene.builder().build()).getJSONArray("list");
            List<Long> appletArticleIds = array.stream().map(e -> (JSONObject) e).map(e -> e.getLong("article_id")).collect(Collectors.toList());
            CommonUtil.checkResultPlus("pc端文章为：", appletArticleIds, "applet端文章为：", articleIds.subList(0, 5));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容运营--banner--填写banner1-banner5的内容");
        }
    }
}
