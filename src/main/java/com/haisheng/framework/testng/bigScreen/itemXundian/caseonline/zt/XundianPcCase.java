package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.hqq.StorePcAndAppData;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.*;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;

public class XundianPcCase extends TestCaseCommon implements TestCaseStd {
    private final EnumTestProduce product = EnumTestProduce.XD_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(product);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    public static final int size = 100;
    XundianScenarioUtilOnline xd = XundianScenarioUtilOnline.getInstance();
    StoreScenarioUtilOnline md = StoreScenarioUtilOnline.getInstance();
    MendianInfoOnline info = new MendianInfoOnline();


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "周涛";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "mendian-online-test");
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店(巡店) 线上");
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13604609869", "15084928847"};
//        commonConfig.shopId = getXunDianShopOnline(); //要改！！！
        beforeClassInit(commonConfig);
        logger.debug("xundian " + xd);

        xd.login("storedemo@winsense.ai","b0581aa73b04d9fe6e3057a613e6f363");

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

    //pc特殊截屏（四张图片截取成功）+事件时间
    @Test
    public void problemMark() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //JSONObject list = xd.checkStart("\"REMOTE\"", 1); //进入远程巡店
            JSONObject data = xd.problemeItems();

            JSONArray check_lists = data.getJSONArray("list");  //执行清单列表
            long listId = check_lists.getJSONObject(0).getInteger("id"); //取一个执行清单
            //取执行清单的一个执行项
            long itemId = check_lists.getJSONObject(0).getJSONArray("check_items").getJSONObject(0).getLong("id");
            //截屏图片
            JSONArray pic_list = info.getpicFour(0);
            //获取整改处理人
            String responsorId = xd.problemesponsors().getJSONArray("list").getJSONObject(0).getString("id");
            String audit_comment = "pc 截屏留痕推送给门店负责人";
            xd.problemMarkTime( responsorId, listId, itemId, pic_list,14630, audit_comment,20);
//            checkArgument(res.getInteger("code") == 1000, "截图四张失败message+"+res.getString("message"));
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc特有截屏留痕");
        }
    }



}




