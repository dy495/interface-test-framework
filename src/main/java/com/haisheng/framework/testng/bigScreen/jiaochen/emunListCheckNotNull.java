package com.haisheng.framework.testng.bigScreen.jiaochen;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumRefer;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @description :校验单接口列表返回值各项不为空 xmf
 * @date :2020/11/27 15:30
 **/


public class emunListCheckNotNull extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = ScenarioUtil.getInstance();
    JsonPathUtil jpu = new JsonPathUtil();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";


        //replace backend gaturl
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName() + commonConfig.checklistQaOwner);

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = "-1";
        commonConfig.referer = EnumRefer.JIAOCHEN_REFERER_DAILY.getReferer();
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);

        jc.appletLoginToken(pp.appletTocken);

    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test
    public void erCode() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin(pp.gwphone, pp.gwpassword);
            JSONObject data = jc.apperCOde();
            String jsonpath = "$.er_code_url";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.appletLoginToken(pp.appletTocken);
            saveData("轿辰-app个人中心，小程序码返回结果不为空");
        }
    }

    @Test
    public void Jc_bannerList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletbanner();
            String jsonpath = "$.list[*].id&&$.list[*].name&&$.list[*].address&&$.list[*].tel&&$.list[*].coordinate&&$.list[*].distance";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-applet首页-banner返回不为空");
        }
    }

    @Test
    public void Jc_ArticleList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletArticleList("20", null);
            String jsonpath = "$.list[*].id&&$.list[*].label&&$.list[*].label_name&&$.list[*].title&&$.list[*].pic_type&&$.list[*].pic_list&&$.list[*].timestamp&&$.list[*].time_str";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-applet首页-文章列表返回为空警告");
        }
    }

    @Test
    public void Jc_shopList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletShopInfo();
            String jsonpath = "$.id&&$.name&&$.address&&$.tel";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-applet首页-门店信息返回不为空");
        }
    }

    @Test
    public void Jc_bandList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletBrandList();
            String jsonpath = "$.list[*].id&&$.list[*].name&&$.list[*].logo";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-applet品牌列表返回不为空");
        }
    }

    @Test
    public void Jc_appletName() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletName();
            String jsonpath = "$.name";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-applet名不为空");
        }
    }

    @Test
    public void Jc_appletProvinceList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletplateNumberProvinceList();
            String jsonpath = "$.list[*].province&&$.list[*].province_name";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-apple车牌号列表不为空");
        }
    }

    @Test
    public void Jc_appletMaintainShop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List coor = new ArrayList();
            coor.add(116.29845);
            coor.add(39.95933);

            JSONObject data = jc.appletmaintainShopList(pp.car_id, coor);
            String jsonpath = "$.list[*].id\"&&$.list[*].name&&$.list[*].address&&$.list[*].distance&&$.list[*].pic_url&&$.list[*].label";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-apple保养门店列表不为空");
        }
    }

    @Test
    public void Jc_appletStaffList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletStaffList(pp.shopId);
            String jsonpath = "$.list[*].uid&&$.list[*].name&&$.list[*].greetings&&$.list[*].pic_url&&$.list[*].label&&$.list[*].is_selected";
            jpu.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-apple保养门店服务列表为空提醒");
        }
    }
}
