package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.experiment.enumerator.*;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;

public class CrmPc3_0 extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.QA_TEST_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
        CommonUtil.login(EnumAccount.ZJL);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        CommonUtil.login(EnumAccount.ZJL);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test
    public void stationMessage_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            boolean flag = false;
            int total = crm.messagePage(1, 10).getInteger("total");
            for (int i = 1; i < CommonUtil.pageTurning(total, 100); i++) {
                JSONArray list = crm.messagePage(1, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("customer_types").contains("销售/售后")
                            || list.getJSONObject(j).getString("customer_types").contains("销售")
                            || list.getJSONObject(j).getString("customer_types").contains("售后")) {
                        flag = true;
                    }
                }
            }
            CommonUtil.valueView(flag);
            Preconditions.checkArgument(flag, "站内消息投放人群不正确");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息可投放人群为售前、售后、售前/售后");
        }
    }

    @Test
    public void stationMessage_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = crm.messagePage(1, 10);
            String date = CommonUtil.getStrField(response, 0, "send_time");
            boolean result = CommonUtil.isLegalDate(date, "yyyy-MM-dd HH:mm");
            CommonUtil.valueView(date, result);
            Preconditions.checkArgument(result, "站内消息生效日期格式非yyyy-MM-dd hh:mm");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("站内消息生效日期格式：yyyy-MM-dd hh：mm");
        }
    }

    @Test
    public void stationMessage_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = 0;
            String title = null;
            int total = crm.messagePage(1, 10).getInteger("total");
            for (int i = 1; i < CommonUtil.pageTurning(total, 100); i++) {
                JSONArray list = crm.messagePage(1, 100).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    if (list.getJSONObject(j).getString("status_name").equals("排期中")) {
                        id = list.getJSONObject(i).getInteger("id");
                        title = list.getJSONObject(i).getString("title");
                        break;
                    }
                }
            }
            JSONObject result = crm.messageDetail(id);
            String title1 = result.getString("title");
            assert title != null;
            CommonUtil.valueView(title, title1);
            Preconditions.checkArgument(title.equals(title1), "排期中的站内消息可以查看操作");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("排期中的消息可以查看操作，接口不做限制，前端限制");
        }
    }

    @Test()
    public void stationMessage_4() {
        logger.logCaseStart(caseResult.getCaseName());
        int messageId = 0;
        String title = "自动化站内消息-待删";
        String content = "自动化";
        Date date = DateTimeUtil.addDay(new Date(), 1);
        String sendDate = DateTimeUtil.getFormat(date, "yyyy-MM-dd HH:mm");
        try {
            crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES", "AFTER_SALES");
            messageId = crm.messagePage(1, 10).getJSONArray("list").getJSONObject(0).getInteger("id");
            String content1 = crm.messageDetail(messageId).getString("content");
            String title1 = crm.messageDetail(messageId).getString("title");
            CommonUtil.valueView(title, title1, content, content1);
            Preconditions.checkArgument(content.equals(content1), "新创建的文章未排列在第一位");
            Preconditions.checkArgument(title.equals(title1), "新创建的文章未排列在第一位");
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            //删除消息
            crm.messageDelete(messageId);
            saveData("站内消息倒叙排列-最新的在最上方&&排期中的站内消息可以删除");
        }
    }

    @Test()
    public void stationMessage_5() {
        int messageId = 0;
        String title = "自动化站内消息-待删";
        String content = "自动化";
        String sendDate = DateTimeUtil.getFormat(DateTimeUtil.addSecond(new Date(), 60), "yyyy-MM-dd HH:mm");
        CommonUtil.valueView(sendDate);
        try {
            crm.messageAdd("", "", "", sendDate, title, content, "", "", "PRE_SALES", "AFTER_SALES");
            JSONArray list = crm.messagePage(1, 100).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                if (list.getJSONObject(i).getString("status_name").equals("发送成功")) {
                    messageId = list.getJSONObject(i).getInteger("id");
                }
            }
            //删除消息
            crm.messageDelete(messageId);
        } catch (Exception | AssertionError e) {
            appendFailreason(e.toString());
        } finally {
            saveData("发送成功的站内消息可以删除");
        }
    }

}
