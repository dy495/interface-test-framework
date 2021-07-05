package com.haisheng.framework.testng.bigScreen.yuntong.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.presalesreception.AppFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.presalesreception.AppPreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.app.voicerecord.AppVoiceRecordSubmitScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.util.BusinessUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.Base64Util;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * 业务管理测试用例
 *
 * @author wangmin
 * @date 2021/1/29 11:17
 */
public class BusinessManageCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_DAILY_SSO;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_YT_DAILY_ONE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public BusinessUtil util = new BusinessUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
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
        util.loginPc(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
    }

    @Test(description = "接待人")
    public void test() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/yuntong/wm/resources/voice/试驾录音.m4a";
            String testDriverBase64 = Base64Util.encodeBase64File(filePath);
            IScene scene = AppPreSalesReceptionPageScene.builder().lastValue(null).size(10).build();
            JSONObject jsonObject = scene.invoke(visitor).getJSONArray("list").getJSONObject(0);
            String customerId = jsonObject.getString("customer_id");
            String receptionId = jsonObject.getString("id");
            String receptionTime = jsonObject.getString("reception_time");
            String startTime = DateTimeUtil.dateToStamp(receptionTime, "yyyy-MM-dd HH:mm:ss");
            String endTime = String.valueOf(Long.parseLong(startTime) + 2 * 60 * 1000);
            String recordName = receptionId + "_" + startTime + ".aac";
            String type = "100";
            String testDriverTime = String.valueOf(Long.parseLong(startTime) + 30 * 1000);
            JSONArray receptionNodes = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("type", type);
            object.put("start_time", testDriverTime);
            receptionNodes.add(object);
            CommonUtil.valueView(startTime, endTime, recordName, receptionNodes);
            util.appCustomerEditV4(receptionId, customerId);
            //提交
            visitor.setProduct(EnumTestProduce.YT_DAILY_CAR);
            AppVoiceRecordSubmitScene.builder().receptionId(receptionId)
                    .base64(testDriverBase64).recordName(recordName).startTime(startTime).endTime(endTime)
                    .receptionNodes(receptionNodes).build().invoke(visitor);
            //完成接待
//            AppFinishReceptionScene.builder().shopId(util.getReceptionShopId()).id(receptionId).build().invoke(visitor);
        } catch (Exception | AssertionError e) {
            collectMessage(e);
        }
    }
}
