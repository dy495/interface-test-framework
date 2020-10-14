package com.haisheng.framework.testng.bigScreen.crm.wm.datastore;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.EnumContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.container.Factory;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
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
import java.text.ParseException;
import java.util.Date;

public class BOnline extends TestCaseCommon implements TestCaseStd {

    CrmScenarioUtilOnline crm = CrmScenarioUtilOnline.getInstance();
    private static final EnumAccount zjl = EnumAccount.ZJL_ONLINE;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
//        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
//        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
//        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
//        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAY_DATA_STORE.getJobName());
//        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_ONLINE.getName());
        //替换钉钉推送
//        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP_ONLINE.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        CommonUtil.login(zjl);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test
    public void testB() {
        String reception_sale;
        String reception_start_time;
        String reception_end_time;
        String reception_date;
        String customer_name;
        String customer_active_type;
        String shop_id;
        String customer_phone1;
        String customer_phone2;
        int reception_duration = 0;
        String date = DateTimeUtil.addDayFormat(new Date(), -1);
        int total = crm.receptionPage("", "", "PRE_SALES", 1, 10).getInteger("total");
        int s = CommonUtil.getTurningPage(total, 100);
        for (int i = 1; i < s; i++) {
            JSONArray list = crm.receptionPage("", "", "PRE_SALES", i, 100).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("reception_date").equals(date)) {
                    String saleName = list.getJSONObject(j).getString("reception_sale");
                    String name = saleName.substring(0, saleName.indexOf("（"));
                    reception_sale = setSqlParam(name);
                    reception_start_time = setSqlParam(list.getJSONObject(j).getString("reception_time"));
                    reception_end_time = setSqlParam(list.getJSONObject(j).getString("leave_time"));
                    reception_date = setSqlParam(list.getJSONObject(j).getString("reception_date"));
                    customer_name = setSqlParam(list.getJSONObject(j).getString("customer_name"));
                    customer_active_type = setSqlParam(list.getJSONObject(j).getString("customer_active_type"));
                    shop_id = setSqlParam(list.getJSONObject(j).getString("shopId"));
                    customer_phone1 = setSqlParam(list.getJSONObject(j).getString("phone1"));
                    customer_phone2 = setSqlParam(list.getJSONObject(j).getString("phone2"));
                    try {
                        reception_duration = new DateTimeUtil().calTimeHourDiff(list.getJSONObject(j).getString("reception_time"), list.getJSONObject(j).getString("leave_time"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String sql = "insert into t_porsche_today_reception_data (shop_id, reception_sale, reception_start_time, reception_end_time, reception_date, customer_name, customer_active_type,customer_phone_1, customer_phone_2, reception_duration)\n" +
                            "value (" + shop_id + ", " + reception_sale + "," + reception_start_time + "," + reception_end_time +
                            "," + reception_date + "," + customer_name + "," + customer_active_type + "," + customer_phone1 + "," + customer_phone2 + "," + reception_duration + ")";
                    new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                }
            }
        }
        int appTotal = crm.receptionPage(1, 10, date, date).getInteger("total");
        int y = CommonUtil.getTurningPage(appTotal, 100);
        for (int i = 1; i < y; i++) {
            JSONArray list = crm.receptionPage(i, 100, date, date).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("reception_sale_name") == null) {
                    reception_date = setSqlParam(list.getJSONObject(j).getString("reception_date"));
                    customer_name = setSqlParam(list.getJSONObject(j).getString("customer_name"));
                    shop_id = setSqlParam(EnumShopId.PORSCHE_SHOP_ONLINE.getShopId());
                    reception_sale = setSqlParam(list.getJSONObject(j).getString("reception_sale_name"));
                    reception_start_time = setSqlParam(list.getJSONObject(j).getString("reception_time"));
                    reception_end_time = setSqlParam(list.getJSONObject(j).getString("leave_time_str"));
                    try {
                        String q = list.getJSONObject(j).getString("reception_time") == null ? "00:00" : list.getJSONObject(j).getString("reception_time");
                        String p = list.getJSONObject(j).getString("leave_time_str") == null ? "00:00" : list.getJSONObject(j).getString("leave_time_str");
                        reception_duration = new DateTimeUtil().calTimeHourDiff(q, p);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String sql = "insert into t_porsche_today_reception_data (shop_id, reception_sale, reception_start_time, reception_end_time, reception_date, customer_name, customer_active_type,customer_phone_1, customer_phone_2, reception_duration)\n" +
                            "value (" + shop_id + ", " + reception_sale + "," + reception_start_time + "," + reception_end_time +
                            "," + reception_date + "," + customer_name + "," + null + "," + null + "," + null + "," + reception_duration + ")";
                    new Factory.Builder().container(EnumContainer.ONE_PIECE.getContainer()).build().create(sql);
                }
            }
        }
    }

    private static String setSqlParam(String param) {
        return param == null ? null : "'" + param + "'";
    }
}
