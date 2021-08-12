package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.MyUtil.TopicUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluateV4ConfigSubmitScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EvaluateConfigCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.YT_DAILY_ZH; // 管理页—-首页
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.YT_ALL_DAILY; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(product);   // 产品类放到代理类中（通过代理类发请求）
    public SceneUtil util = new TopicUtil(visitor);    //场景工具类中放入代理类，类中封装接口方法直接调用
    CommonConfig commonConfig = new CommonConfig();    // 配置类初始化

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId("57279").setReferer(product.getReferer()).setRoleId(ALL_AUTHORITY.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);  // 配置请求头
        util.loginPc(ALL_AUTHORITY);   //登录
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        logger.logCaseStart(caseResult.getCaseName());
    }

    @Test(dataProvider = "contentCheck")
    public void addATopic1(String description, String expect, String title, String... answer) {
        visitor.setProduct(EnumTestProduct.YT_DAILY_JD);
        try {
            if (util instanceof TopicUtil) {
                TopicUtil topicUtil = (TopicUtil) util;
                JSONArray links = topicUtil.checkContents(title, answer);
                String code = EvaluateV4ConfigSubmitScene.builder().links(links).build().invoke(visitor, false).getString("code");
                Preconditions.checkArgument(Objects.equals(code, expect), description + ",期待:" + expect + ", 结果code=" + code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("评价配置：内容校验");
        }

    }

    @DataProvider(name = "contentCheck")
    public Object[] exportPages() {
        return new String[][]{
                {"标题长度3，答案两个，答案长度1", "1000", "一二三", "一", "二"},
                {"标题长度40，答案两个，答案长度1", "1000", "一二三1414&djhfa昂发开发!@#$%^&*(){}[]?\"\"|?/\\NM", "一", "二"},
                {"标题长度41，答案两个，答案长度1", "1001", "一二三1414&djhfa昂发开发!@#$%^&*(){}[]?\"\"|?/\\NM2", "一", "二"},
                {"标题长度2，答案两个，答案长度1", "1001", "一二", "一", "二"},
                {"标题长度0，答案两个，答案长度1", "1001", "", "一", "二"},
                {"标题长度3，答案两个，答案长度20", "1000", "一二三", "&*(%:LJ：“发LJ*^(%$# $", "UHuh是"},
                //{"标题长度3，答案两个，答案长度21", "1001", "一二三", "一二三四五六四五六四五六四五六四五六四五六", "二44444"},
                //{"标题长度3，答案两个，答案长度0", "1001", "一二三", "", ""},
        };
    }

    @Test(dataProvider = "topicNum")
    public void addATopic2(String description, List<Integer> topicList, String expectCode, String... answer) {
        visitor.setProduct(EnumTestProduct.YT_DAILY_JD);
        try {
            if (util instanceof TopicUtil) {
                TopicUtil topicUtil = (TopicUtil) util;
                JSONArray links = topicUtil.checkTopicNum(topicList, answer);
                String code = EvaluateV4ConfigSubmitScene.builder().links(links).build().invoke(visitor, false).getString("code");
                Preconditions.checkArgument(Objects.equals(code, expectCode), description + ",期待:" + expectCode + ", 结果code=" + code);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("评价配置：题目数量");
        }


    }

    @DataProvider(name = "topicNum")
    public Object[] exports() {
        return new Object[][]{
                {"1道题,5个答案", Arrays.asList(1, 1, 1, 1, 1), "1000", "选项1", "选项2", "选项3", "选项4", "选项5"},
                {"10道题,5个答案", Arrays.asList(10, 10, 10, 10, 10), "1000", "选项1", "选项2", "选项3", "选项4", "选项5"},
                {"11道题,5个答案", Arrays.asList(11, 10, 10, 10, 10), "1001", "选项1", "选项2", "选项3", "选项4", "选项5"},
                {"有0道题,5个答案", Arrays.asList(10, 10, 10, 10, 0), "1001", "选项1", "选项2", "选项3", "选项4", "选项5"},

        };
    }


}
