package com.haisheng.framework.testng.bigScreen.crmOnline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.finishReceive;
import com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo.selectTest;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.crmOnline.PublicParmOnline;
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

public class CrmQtOnline extends TestCaseCommon implements TestCaseStd {
    CrmScenarioUtilOnlineX crm = CrmScenarioUtilOnlineX.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParmOnline pp = new PublicParmOnline();
    PackFunctionOnline pf = new PackFunctionOnline();


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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "xmf";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 线上X");

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShopOline();
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(pp.qiantai, pp.qtpassword);


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

    /**
     * @description :展厅接待按名字查询，结果校验  ok
     * @date :2020/8/3 12:48
     **/
    @Test
    public void qtztSelect() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray data = crm.qtreceptionPage("", "", "", "1", "10").getJSONArray("list");
            String customer_name = data.getJSONObject(0).getString("customer_name");
            JSONArray list = crm.deliverSelect(1, 10, customer_name).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument(nameSelect.equals(customer_name), "展厅接待按客户名称查询，结果错误");
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("展厅接待按名字查询，结果校验");
        }
    }

    /**
     * @description :展厅接待按时间查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_DATE", dataProviderClass = CrmScenarioUtilOnlineX.class)
    public void qtztSelectTime(String select_date) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.qtreceptionPage("", select_date, select_date, "1", "10").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String reception_date = list.getJSONObject(i).getString("reception_date");
                Preconditions.checkArgument(reception_date.equals(select_date), "展厅接待按接待时间{}查询，结果{}错误", select_date, reception_date);
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("展厅接待按接待日期查询，结果校验");
        }
    }

    /**
     * @description :展厅接待按时间查仅输入开始结束时间 ok
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void qtztSelectSelect2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.qtreceptionPage("", dt.getHistoryDate(0), "", "1", "10");
            crm.qtreceptionPage("", dt.getHistoryDate(0), dt.getHistoryDate(0), "1", "10");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("展厅接待按时间查仅输入开始结束时间 ");
        }
    }

    /**
     * @description :展厅接待查询
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void qtztSelectTimeAndname() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray data = crm.qtreceptionPage("", "", "", "1", "10").getJSONArray("list");
            String customer_name = data.getJSONObject(0).getString("customer_name");
            String select_date = dt.getHistoryDate(0);
            JSONArray list = crm.qtreceptionPage(customer_name, select_date, select_date, "1", "10").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String reception_date = list.getJSONObject(i).getString("reception_date");
                String nameSelect = list.getJSONObject(i).getString("customer_name");
                Preconditions.checkArgument((reception_date.equals(select_date)) && (customer_name.equals(nameSelect)), "展厅接待按交车时间" + select_date + "查询，结果错误" + reception_date);
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("展厅接待组合查询，结果校验");
        }
    }

    /**
     * @description :到访记录按时间查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_DATE", dataProviderClass = CrmScenarioUtilOnlineX.class)
    public void visitRecodeSelectTime(String select_date) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.visitList(select_date, select_date, "1", "10").getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String reception_date = list.getJSONObject(i).getString("visit_day");
                Preconditions.checkArgument(reception_date.equals(select_date), "到访记录按到访时间{}查询，结果{}错误", select_date, reception_date);
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("到访记录按到访日期查询，结果校验");
        }
    }

    /**
     * @description :到访记录按时间查仅输入开始结束时间 ok
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void visitRecodeSelect() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.visitList(dt.getHistoryDate(0), "", "1", "10");
            crm.visitList("", dt.getHistoryDate(0), "1", "10");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("到访记录按时间查仅输入开始结束时间 ");
        }
    }

    /**
     * @description :为接待离店风控按时间查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_DATE", dataProviderClass = CrmScenarioUtilOnlineX.class)
    public void nonReceptionSelectTime(String select_date) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.nonReceptionList(select_date, select_date, "1", "10");
            for (int i = 0; i < list.size(); i++) {
                String reception_date = list.getJSONObject(i).getString("create_date");
                Preconditions.checkArgument(reception_date.equals(select_date), "到访记录按到访时间{}查询，结果{}错误", select_date, reception_date);
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("为接待离店风控按到访日期查询，结果校验");
        }
    }

    /**
     * @description :为接待离店风控按时间查仅输入开始结束时间 ok
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void nonReceptionSelect() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.nonReceptionList(dt.getHistoryDate(0), "", "1", "10");
            crm.nonReceptionList("", dt.getHistoryDate(0), "1", "10");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("为接待离店风控按时间查仅输入开始结束时间 ");
        }
    }

    /**
     * @description :非客风控按时间查询 ok
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_DATE", dataProviderClass = CrmScenarioUtilOnlineX.class)
    public void nonCustomerSelectTime(String select_date) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.nonCustomerList(select_date, select_date, "1", "10");
            for (int i = 0; i < list.size(); i++) {
                String reception_date = list.getJSONObject(i).getString("create_date");
                Preconditions.checkArgument(reception_date.equals(select_date), "非客按到访时间{}查询，结果{}错误", select_date, reception_date);
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("非客风控按到访日期查询，结果校验");
        }
    }

    /**
     * @description :非客风控按时间查仅输入开始结束时间 ok
     * @date :2020/8/3 12:48
     **/
    @Test()
    public void nonCustomerSelect() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.nonCustomerList(dt.getHistoryDate(0), "", "1", "10");
            crm.nonCustomerList("", dt.getHistoryDate(0), "1", "10");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("非客风控按时间查仅输入开始结束时间 ");
        }
    }

    /**
     * @description :变更接待
     * @date :2020/9/28 17:16
     **/
//    @Test()
    public void changeReception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取空闲销售，sale_id和接待列表数
            String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
            String loginTemp = pf.username(sale_id);
            crm.login(loginTemp, pp.adminpassword);    //变更接待前 接待销售接待列表数
            int receiveTotal = crm.customerMyReceptionList("", "", "", 1, 10).getInteger("total");
            //创建老客接待，获取接待记录id;
            JSONObject json = pf.creatCustOld(pp.customer_phone_number);   //变更接待前 原销售接待列表数
            int total = crm.customerMyReceptionList("", "", "", 1, 10).getInteger("total");
            String receiptId = json.getString("reception_id");
            String customer_id = json.getString("customerId");
            String belong_sale_id = json.getString("sale_id");
            //完成接待
            JSONArray PhoneList = json.getJSONArray("phoneList");
            //前台登录，变更接待
            crm.login(pp.qiantai, pp.qtpassword);
            crm.changeReceptionSale(receiptId, sale_id);

            crm.login(pp.xiaoshouGuwen, pp.adminpassword);          //变更接待后原销售接待列表数
            int total2 = crm.customerMyReceptionList("", "", "", 1, 10).getInteger("total");

            crm.login(loginTemp, pp.adminpassword);   //变更接待后 接待销售接待列表数
            int receiveTotalA = crm.customerMyReceptionList("", "", "", 1, 10).getInteger("total");
            //完成接待
            crm.login(pp.qiantai, pp.qtpassword);
            crm.changeReceptionSale(receiptId, belong_sale_id);

            crm.login(pp.xiaoshouGuwen, pp.adminpassword);
            JSONArray faceList = new JSONArray();
            JSONObject ll = new JSONObject();
            ll.put("analysis_customer_id", "c2aecb35-9e69-4adb-bc7f-98310e34");
            ll.put("id", 0);
            ll.put("is_decision", true);

            finishReceive pm = new finishReceive();
            pm.customer_id = customer_id;
            pm.reception_id = receiptId;
            pm.belongs_sale_id = belong_sale_id;
            pm.name = pp.customer_name;

            pm.reception_type = "BB";
            pm.face_list = faceList;
            pm.plate_number_one = "京DF12334";

//            pm.customer_id="14058";
//            pm.reception_id="4086";
//            pm.belongs_sale_id="uid_37ff7893";
//            JSONArray PhoneList = new JSONArray();
//            JSONObject phone1 = new JSONObject();
//            phone1.put("phone", "15037286013");
//            phone1.put("phone_order", 0);
//            JSONObject phone2 = new JSONObject();
//            phone2.put("phone", "");
//            phone2.put("phone_order", 1);
//            PhoneList.add(0, phone1);
//            PhoneList.add(1, phone2);

            pm.phoneList = PhoneList;
            crm.finishReception3(pm);
            Preconditions.checkArgument(total - total2 == 1, "变更接待，原接待销售接待列表-1");
            Preconditions.checkArgument(receiveTotalA - receiveTotal == 1, "变更接待，原接待销售接待列表+1");


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            crm.login(pp.qiantai, pp.qtpassword);
            saveData("变更接待 ");
        }
    }

//    @Test()
    public void T() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            selectTest ss = new selectTest();
            ss.page = "1";
            crm.nonReceptionList(ss);
            String a = ss.page;
            System.out.println(ss.page);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
//            saveData(" ");
        }
    }


}
