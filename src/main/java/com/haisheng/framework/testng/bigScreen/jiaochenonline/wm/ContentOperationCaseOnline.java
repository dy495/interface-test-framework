package com.haisheng.framework.testng.bigScreen.jiaochenonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.operation.ArticlePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity.ActivityStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.om.ArticleStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.banner.AppletBannerScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity.ActivityManagePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner.EditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.file.FileUploadScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticleList;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation.ArticlePageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
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
    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.JC_ALL_ONLINE_LXQ;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_WM_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public SceneUtil util = new SceneUtil(visitor);

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
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        util.loginPc(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    //ok
    @Test(description = "内容管理--banner--上传图片不符合3:2")
    public void banner_system_1() {
        try {
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
            String base64 = new ImageUtil().getImageBinary(filePath);
            String message = FileUploadScene.builder().pic(base64).permanentPicType(0).isPermanent(false).ratio(1.5).ratioStr("3：2").build().execute(visitor, false).getString("message");
            String err = "图片宽高比不符合3：2的要求";
            CommonUtil.checkResult("图片比", "非3：2", err, message);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容管理--banner--上传图片不符合3:2");
        }
    }

    //bug
    @Test(description = "banner--跳转活动/文章的条数=展示中的文章+进行中或者已结束活动条数之和")
    public void banner_data_1() {
        try {
            int num = ArticleList.builder().build().execute(visitor).getJSONArray("list").size();
            IScene articlePageScene = ArticlePageScene.builder().build();
            int articlePageListSize = (int) util.toJavaObjectList(articlePageScene, ArticlePageBean.class).stream().filter(e -> e.getStatusName().equals(ArticleStatusEnum.SHOW.getTypeName())).count();
            int passedSTotal = ActivityManagePageScene.builder().status(ActivityStatusEnum.PASSED.getId()).build().execute(visitor).getInteger("total");
            int finishTotal = ActivityManagePageScene.builder().status(ActivityStatusEnum.FINISH.getId()).build().execute(visitor).getInteger("total");
            CommonUtil.checkResult("跳转活动/文章的条数", passedSTotal + finishTotal + articlePageListSize, num);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("banner--跳转活动/文章的条数=展示中的文章+进行中活动条数之和");
        }
    }

    //ok
    @Test(description = "banner--填写banner1-banner5的内容")
    public void banner_data_2() {
        try {
            List<Long> articleIds = util.getArticleIdList();
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture";
            File file = new File(filePath);
            File[] files = file.listFiles();
            assert files != null;
            List<String> base64s = Arrays.stream(files).filter(e -> e.toString().contains("banner")).map(e -> new ImageUtil().getImageBinary(e.getPath())).collect(Collectors.toList());
            List<String> picPaths = base64s.stream().map(e -> visitor.invokeApi(FileUploadScene.builder().pic(e).permanentPicType(0).isPermanent(false).ratio(1.5).ratioStr("3：2").build()).getString("pic_path")).collect(Collectors.toList());
            JSONArray array = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("article_id", articleIds.get(0));
            jsonObject1.put("banner_img_url", picPaths.get(0));
            jsonObject1.put("banner_id", 41);
            jsonObject1.put("banner_select", "banner1");
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("article_id", articleIds.get(1));
            jsonObject2.put("banner_img_url", picPaths.get(1));
            jsonObject2.put("banner_id", 42);
            jsonObject2.put("banner_select", "banner2");
            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("article_id", articleIds.get(2));
            jsonObject3.put("banner_img_url", picPaths.get(2));
            jsonObject3.put("banner_id", 43);
            jsonObject3.put("banner_select", "banner3");
            JSONObject jsonObject4 = new JSONObject();
            jsonObject4.put("article_id", articleIds.get(3));
            jsonObject4.put("banner_img_url", picPaths.get(3));
            jsonObject4.put("banner_id", 44);
            jsonObject4.put("banner_select", "banner4");
            JSONObject jsonObject5 = new JSONObject();
            jsonObject5.put("article_id", articleIds.get(4));
            jsonObject5.put("banner_img_url", picPaths.get(4));
            jsonObject5.put("banner_id", 45);
            jsonObject5.put("banner_select", "banner5");
            array.add(jsonObject1);
            array.add(jsonObject2);
            array.add(jsonObject3);
            array.add(jsonObject4);
            array.add(jsonObject5);
            EditScene.builder().bannerType("HOME_PAGE").list(array).build().execute(visitor);
            util.loginApplet(APPLET_USER_ONE);
            JSONArray list = AppletBannerScene.builder().build().execute(visitor).getJSONArray("list");
            List<Long> appletArticleIds = list.stream().map(e -> (JSONObject) e).map(e -> e.getLong("article_id")).collect(Collectors.toList());
            CommonUtil.checkResultPlus("pc端文章为：", appletArticleIds, "applet端文章为：", articleIds.subList(0, 5));
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        } finally {
            saveData("内容管理--banner--填写banner1-banner5的内容");
        }
    }
}
