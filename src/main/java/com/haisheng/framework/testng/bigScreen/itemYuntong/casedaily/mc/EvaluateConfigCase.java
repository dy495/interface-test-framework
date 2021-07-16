package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.MyUtil.TopicUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluateV4ConfigDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluateV4ConfigSubmitScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class EvaluateConfigCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY_SSO; // 管理页—-首页
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.YT_ALL_DAILY; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);   // 产品类放到代理类中（通过代理类发请求）
    public TopicUtil util = new TopicUtil(visitor);    //场景工具类中放入代理类，类中封装接口方法直接调用
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
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = PRODUCE.getAbbreviation(); // 产品代号 -- YT
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = "57279";  //请求头放入shopId
        commonConfig.roleId = ALL_AUTHORITY.getRoleId(); //请求头放入roleId
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
    }

    @Test(dataProvider = "titleCheck")
    public void addATopic1(String description,String expect,String title,String... answer){
        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
        try{
            JSONArray links = util.checkContents(title,answer);
            String code = EvaluateV4ConfigSubmitScene.builder().links(links).build().invoke(visitor, false).getString("code");;
            Preconditions.checkArgument(expect.equals(code),description+",结果code="+code);
        }catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("评价配置");
        }

    }
    @DataProvider(name = "titleCheck")
    public Object[] exportPages() {
        return new String[][]{
                {"标题长度3，答案两个，答案长度1","1000","一二三","一","二"},
                {"标题长度40，答案两个，答案长度1","1000","一二三1414&djhfa昂发开发!@#$%^&*(){}[]?\"\"|?/\\NM","一","二"},
                {"标题长度41，答案两个，答案长度1","1001","一二三1414&djhfa昂发开发!@#$%^&*(){}[]?\"\"|?/\\NM2","一","二"}
        };
    }












}
