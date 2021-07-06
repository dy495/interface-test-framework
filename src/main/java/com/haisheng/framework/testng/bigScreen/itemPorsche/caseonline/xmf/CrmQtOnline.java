package com.haisheng.framework.testng.bigScreen.itemPorsche.caseonline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.PublicParmOnline;
import com.haisheng.framework.testng.bigScreen.itemPorsche.caseonline.xmf.interfaceOnline.FinishReceptionOnline;
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
        commonConfig.checklistQaOwner = "夏明凤";
        commonConfig.product=EnumTestProduce.PORSCHE_ONLINE.getAbbreviation();


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.PORSCHE_ONLINE.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = EnumTestProduce.PORSCHE_ONLINE.getShopId();
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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
                Preconditions.checkArgument((reception_date.equals(select_date)) && (customer_name.contains(nameSelect)), "展厅接待按接待时间" + select_date + "查询，结果错误" + reception_date);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
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
            if(select_date.equals("")&&list.size()==0){
                throw new Exception("到访记录为空");
            }
            for (int i = 0; i < list.size(); i++) {
                String reception_date = list.getJSONObject(i).getString("visit_day");
                Preconditions.checkArgument(reception_date.equals(select_date), "到访记录按到访时间{}查询，结果{}错误", select_date, reception_date);
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("到访记录按到访日期查询，结果校验");
        }
    }
    @Test(description = "到访记录若未空，抛异常")
    public void visitRecodeNontull() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String select_date=dt.getHistoryDate(0);
            JSONArray list = crm.visitList(select_date, select_date, "1", "10").getJSONArray("list");
            if(list.size()==0){
                throw new Exception("到访记录为空");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("到访记录不为空");
        }
    }

//    @Test(description = "线上人脸为空警告")
    public void faceListNontull() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.markcustomerList().getJSONArray("list");
            if(list.size()==0){
                throw new Exception("警告：线上人脸列表为空");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("检查前台人脸列表是否为空");
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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
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
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("非客风控按时间查仅输入开始结束时间 ");
        }
    }

    /**
     * @description :变更接待
     * @date :2020/9/28 17:16
     **/
    @Test()
    public void changeReception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取空闲销售，sale_id和接待列表数
            JSONObject json = pf.creatCustOld(pp.customer_phone_number);
            int total = crm.customerMyReceptionList("", "", "", 1, 10).getInteger("total");
            String receiptId = json.getString("reception_id");
            String customer_id = json.getString("customerId");
            String belong_sale_id = json.getString("sale_id");
            String userLoginName = json.getString("userLoginName");
            JSONArray PhoneList = json.getJSONArray("phoneList");
            //前台登录，变更接待
            crm.login(pp.qiantai, pp.qtpassword);
            String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
            String loginTemp = pf.username(sale_id);
            crm.login(loginTemp, pp.adminpassword);    //变更接待前 接待销售接待列表数
            int receiveTotal = crm.customerMyReceptionList("", "", "", 1, 10).getInteger("total");
            //创建老客接待，获取接待记录id;
            crm.changeReceptionSale(receiptId, sale_id);

            crm.login(userLoginName, pp.adminpassword);          //变更接待后原销售接待列表数
            int total2 = crm.customerMyReceptionList("", "", "", 1, 10).getInteger("total");

            crm.login(loginTemp, pp.adminpassword);   //变更接待后 接待销售接待列表数
            int receiveTotalA = crm.customerMyReceptionList("", "", "", 1, 10).getInteger("total");
            //完成接待
            crm.login(pp.qiantai, pp.qtpassword);
            crm.changeReceptionSale(receiptId, belong_sale_id);

            crm.login(userLoginName, pp.adminpassword);
//            JSONArray faceList = new JSONArray();
//            JSONObject ll = new JSONObject();
//            ll.put("analysis_customer_id", "c2aecb35-9e69-4adb-bc7f-98310e34");
//            ll.put("id", 0);
//            ll.put("is_decision", true);

            FinishReceptionOnline pm = new FinishReceptionOnline();
            pm.customer_id = customer_id;
            pm.reception_id = receiptId;
            pm.belongs_sale_id = belong_sale_id;
            pm.name = pp.customer_name;

            pm.reception_type = "BB";
//            pm.face_list = faceList;
//            pm.plate_number_one = "京DF12334";

            pm.remark = new JSONArray();
            pm.phoneList = PhoneList;
            crm.finishReception3(pm);
            Preconditions.checkArgument(total - total2 == 1, "变更接待，原接待销售接待列表-1");
            Preconditions.checkArgument(receiveTotalA - receiveTotal == 1, "变更接待，原接待销售接待列表+1");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(pp.qiantai, pp.qtpassword);
            saveData("变更接待 ");
        }
    }

    /**
     * @description :标记三个非客列表+1/3，删除-1/3
     * @date :2020/10/10 17:57
     **/
    @Test(dataProvider = "NUM", dataProviderClass = CrmScenarioUtilOnlineX.class)
    public void nonGuestListThere(int num) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String time = dt.getHistoryDate(0);
            //标记前未接待离店列表数
            JSONArray date = crm.nonCustomerList(time, time, "1", "10");
            int total = date.size();

            JSONArray list = crm.markcustomerList().getJSONArray("list");
            if(list.size()==0){
                throw new Exception("前台人脸数为0，case无法执行");
            }
            String analysis_customer_id = "";
            int count = 0;
            JSONArray idlist = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                String customer_identity_name = list.getJSONObject(i).getString("customer_identity_name");
                if (customer_identity_name.equals("新客") || customer_identity_name.equals("未接待离店")) {
                    analysis_customer_id = list.getJSONObject(i).getString("analysis_customer_id");
                    JSONObject id = new JSONObject();
                    id.put("analysis_customer_id", analysis_customer_id);
                    idlist.add(id);
                    count++;
                    if (count >= num) {
                        break;
                    }
                }
            }
            if (idlist.size() != num) {
                return;
            }
            crm.markNocustomer(idlist);
            //标记后
            int totalA = crm.nonCustomerList(time, time, "1", "100").size();
            for (int i = 0; i < idlist.size(); i++) {
                String analysis_customer_idL = idlist.getJSONObject(i).getString("analysis_customer_id");
                crm.deleteNocustomer(analysis_customer_idL);
            }
            //删除未接待离店
            int totalB = crm.nonCustomerList(time, time, "1", "100").size();
            Preconditions.checkArgument(totalA - total == num, "标记为非客后，非客列表没+1");
            Preconditions.checkArgument(totalA - totalB == num, "删除标记的非客后，非客列表没-1");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("标记非客列表+1/3，删除-1/3 ");
        }
    }


    /**
     * @description :标记三个未接待离店列表+1/3，删除-1/3
     * @date :2020/10/10 17:57
     **/
    @Test(dataProvider = "NUM", dataProviderClass = CrmScenarioUtilOnlineX.class)
    public void nonReceiveGuestListThere(int num) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String time = dt.getHistoryDate(0);
            //标记前未接待离店列表数
            JSONArray date = crm.nonReceptionList(time, time, "1", "100");
            int total = date.size();

            JSONArray list = crm.markcustomerList().getJSONArray("list");
            String analysis_customer_id = "";
            int count = 0;
            JSONArray idlist = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                String customer_identity_name = list.getJSONObject(i).getString("customer_identity_name");
                if (customer_identity_name.equals("新客") || customer_identity_name.equals("未接待离店")) {
                    analysis_customer_id = list.getJSONObject(i).getString("analysis_customer_id");
                    JSONObject id = new JSONObject();
                    id.put("analysis_customer_id", analysis_customer_id);
                    idlist.add(id);
                    count++;
                    if (count >= num) {
                        break;
                    }
                }
            }
            if (idlist.size() != num) {
                return;
            }
            crm.marknonReception(idlist);
            //标记后
            int totalA = crm.nonReceptionList(time, time, "1", "100").size();
            for (int i = 0; i < idlist.size(); i++) {
                String analysis_customer_idL = idlist.getJSONObject(i).getString("analysis_customer_id");
                crm.deleteNoReception(analysis_customer_idL);
            }
            //删除未接待离店
            int totalB = crm.nonReceptionList(time, time, "1", "100").size();
            Preconditions.checkArgument(totalA - total == num, "标记为未接待离店后，未接待离店列表没+1");
            Preconditions.checkArgument(totalB - totalA == num, "删除标记的未接待离店后，未接待离店列表没-1");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("标记未接待离店列表+1/3，删除-1/3 ");
        }
    }

    /**
     * @description :创建新客，变更接待
     * @date :2020/10/12 20:58
     **/
    @Test()
    public void changeReceptionNew() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取空闲销售，sale_id和接待列表数   o所属销售   2接待销售
            JSONArray data = crm.freeSaleList().getJSONArray("list");
            String sale_id = data.getJSONObject(0).getString("sale_id");
            String sale_id2 = data.getJSONObject(1).getString("sale_id");

            String loginTemp2 = pf.username(sale_id2);
            crm.login(loginTemp2, pp.adminpassword);
            //变更接待前接待销售接待列表数
            int beforeReceipt[]=pf.receiptSum();
            JSONObject json = pf.creatCust();

            FinishReceptionOnline pm = new FinishReceptionOnline();
            pm.customer_id = json.getString("customerId");
            pm.reception_id = json.getString("reception_id");
            pm.belongs_sale_id = json.getString("sale_id");
            pm.name =json.getString("name");
            pm.reception_type = "FU";
            pm.phoneList = json.getJSONArray("phoneList");

            String loginTemp = pf.username(sale_id);
            crm.login(loginTemp, pp.adminpassword);
            //变更接待前 所属销售接待列表数
            int beforeRcceiptBelongSale[]=pf.receiptSum();
            //前台登录，变更接待
            crm.login(pp.qiantai, pp.qtpassword);
            int beforeqt=crm.qtreceptionPage("","","","1","10").getInteger("today_reception_num");
            crm.changeReceptionSale(pm.reception_id, sale_id2);

            crm.login(loginTemp, pp.adminpassword);          //变更接待后所属销售接待列表数
            int afterRcceiptBelongSale[]=pf.receiptSum();

            crm.login(loginTemp2, pp.adminpassword);   //变更接待后 接待销售接待列表数
            int afterRcceipt[]=pf.receiptSum();

            //完成接待
            crm.login(pp.qiantai, pp.qtpassword);
            int afterqt=crm.qtreceptionPage("","","","1","10").getInteger("today_reception_num");
            crm.changeReceptionSale(pm.reception_id, pm.belongs_sale_id);

            crm.login(loginTemp, pp.adminpassword);


            crm.finishReception3(pm);
            Preconditions.checkArgument(beforeRcceiptBelongSale[4] - afterRcceiptBelongSale[4]  == 1, "变更接待，所属销售接待列表-1");
            Preconditions.checkArgument(beforeRcceiptBelongSale[1] - afterRcceiptBelongSale[1]  == 1, "变更接待，所属销售今日接待数-1");
            Preconditions.checkArgument(beforeRcceiptBelongSale[0] - afterRcceiptBelongSale[0]  == 1, "变更接待，所属销售共计接待数-1");
            Preconditions.checkArgument(afterRcceipt[4] - beforeReceipt[4] == 1, "变更接待，接待销售接待列表+1");
            Preconditions.checkArgument(afterRcceipt[1] - beforeReceipt[1] == 1, "变更接待，接待销售今日接待数+1");
            Preconditions.checkArgument(afterRcceipt[0] - beforeReceipt[0] == 1, "变更接待，接待销售共计接待数+1");
            Preconditions.checkArgument(afterqt - beforeqt == 0, "变更接待，前台今日接待数不变");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(pp.qiantai, pp.qtpassword);
            saveData("变更接待，数据统计 ");
        }
    }

    /**
     * @description :非所属，变更接待，无法修改除备注以外功能---前端控制；此case验证，销售顾问无法变更所属销售
     * @date :2020/10/12 20:58
     **/
    @Test()
    public void changeReceptionNoedit() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取空闲销售，sale_id和接待列表数   //遇到空闲第一位的为所属顾问11 时，下一个
            JSONArray list= crm.freeSaleList().getJSONArray("list");
            String sale_id="";
            for(int i=0;i<list.size();i++){
                sale_id =list.getJSONObject(i).getString("sale_id");
                if(!sale_id.equals("uid_c01f9419")){
                    break;
                }
            }

            String loginTemp = pf.username(sale_id);
            crm.login(loginTemp, pp.adminpassword);    //变更接待前 接待销售接待列表数
            //创建老客接待，获取接待记录id;
            JSONObject json = pf.creatCustOld(pp.chengeReceiptPhone);   //变更接待前 原销售接待列表数
            String receiptId = json.getString("reception_id");
            String customer_id = json.getString("customerId");
            String belong_sale_id = json.getString("sale_id");
            //完成接待
            JSONArray PhoneList = json.getJSONArray("phoneList");
            //前台登录，变更接待
            crm.login(pp.qiantai, pp.qtpassword);
            crm.changeReceptionSale(receiptId, sale_id);

            crm.login(loginTemp, pp.adminpassword);

            FinishReceptionOnline pm = new FinishReceptionOnline();
            pm.customer_id = customer_id;
            pm.reception_id = receiptId;

            pm.name = pp.chengeReceiptName;
            pm.reception_type = "BB";
            pm.remark = new JSONArray();
            pm.phoneList = PhoneList;
            pm.checkCode = false;
            pm.belongs_sale_id=pp.belongSaleId;
            int code = crm.finishReception3(pm).getInteger("code");
            Preconditions.checkArgument(code == 1001, "销售顾问变更了所属销售");
            pm.checkCode = true;
            pm.belongs_sale_id = belong_sale_id;
            crm.finishReception3(pm);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(pp.qiantai, pp.qtpassword);
            saveData("销售顾问变更所属销售失败 ");
        }
    }

    /**
     * @description :销售总监修改客户联系方式1，成功验证
     * @date :2020/10/12 20:58
     **/
    @Test()
    public void changeReceptionNoedit2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(pp.xiaoshouZongjian,pp.adminpassword);
            JSONObject data=crm.customerSelect(1,10,"auto").getJSONArray("list").getJSONObject(0);
            String customer_id=data.getString("customer_id");
            String customer_phone=pf.genPhoneNum();

            FinishReceptionOnline pm = new FinishReceptionOnline();
            pm.customer_id = customer_id;

            pm.name = data.getString("customer_name");
            pm.reception_type = "FU";
            pm.phoneList = pf.phoneList(customer_phone,"");
            pm.belongs_sale_id=pp.belongSaleId;
            crm.editCustomer(pm);

            crm.finishReception3(pm);
            JSONObject after=crm.customerInfo(customer_id);
            String belongsSaleId=after.getString("belongs_sale_id");
            String phoneA=after.getJSONArray("phone_list").getJSONObject(0).getString("phone");
            Preconditions.checkArgument(belongsSaleId.equals(pm.belongs_sale_id),"销售总监修改所属顾问失败");
            Preconditions.checkArgument(phoneA.equals(customer_phone),"销售总监修改联系方式失败");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(pp.qiantai, pp.qtpassword);
            saveData("销售总监修改客户联系方式1，成功验证 ");
        }
    }


    @Test(description = "前台老客标记非客失败")
    public void markab(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = crm.markcustomerList().getJSONArray("list");
            String analysis_customer_id = "";
            JSONArray idlist = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                String customer_identity_name = list.getJSONObject(i).getString("customer_identity_name");
                if (customer_identity_name.equals("老客")) {
                    analysis_customer_id = list.getJSONObject(i).getString("analysis_customer_id");
                    JSONObject id = new JSONObject();
                    id.put("analysis_customer_id", analysis_customer_id);
                    idlist.add(id);
                    break;
                }
            }
            int code1=crm.markNocustomercode(idlist).getInteger("code");
            Preconditions.checkArgument(code1==1001,"老客不能被标记成非客");
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally {
            saveData("前台老客标记非客失败");
        }
    }


    @Test(description = "前台分配新客含人脸接待列表+1",enabled = false)
    public void qtFenpei() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            FinishReceptionOnline fr=new FinishReceptionOnline();
            crm.login(pp.qiantai, pp.adminpassword);
            int totaol=crm.qtreceptionPage("","","","1","10").getInteger("total");

            fr.name = "auto" + dt.getHHmm(0);
            String phone = pf.genPhoneNum();
            //获取当前空闲第一位销售id
            String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
            String userLoginName = pf.username(sale_id);
            boolean isDes = true;
            JSONObject list = new JSONObject();
            JSONArray ll = new JSONArray();
            list.put("customer_name", fr.name);
            list.put("is_decision", isDes);
            ll.add(0, list);

            //创建接待
            crm.creatReception2("FIRST_VISIT", ll);
            int totaol2=crm.qtreceptionPage("","","","1","10").getInteger("total");
            crm.login(userLoginName, pp.adminpassword);

            JSONObject data = crm.customerMyReceptionList("", "", "", 10, 1);
            fr.reception_id = data.getJSONArray("list").getJSONObject(0).getString("id");
            fr.customer_id = data.getJSONArray("list").getJSONObject(0).getString("customer_id");
            fr.phoneList =pf.phoneList(phone,"");
            fr.belongs_sale_id=sale_id;
            fr.reception_type="FU";
            crm.finishReception3(fr);
            Preconditions.checkArgument(totaol2-totaol==1,"前台分配接待列表+1");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(pp.qiantai, pp.qtpassword);
            saveData("销售总监修改客户联系方式1，成功验证 ");
        }
    }

    @Test(description = "前台分配新客不含人脸接待列表+1")
    public void qtFenpei2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(pp.qiantai, pp.adminpassword);
            int totaol=crm.qtreceptionPage("","","","1","10").getInteger("total");

            JSONObject json = pf.creatCust();

            FinishReceptionOnline pm = new FinishReceptionOnline();
            pm.customer_id = json.getString("customerId");
            pm.reception_id = json.getString("reception_id");
            pm.belongs_sale_id = json.getString("sale_id");
            pm.name =json.getString("name");
            pm.reception_type = "FU";
            pm.phoneList = json.getJSONArray("phoneList");

            String loginTemp = json.getString("userLoginName");
            crm.login(loginTemp, pp.adminpassword);
            crm.finishReception3(pm);
            crm.login(pp.qiantai,pp.qtpassword);
            int totaol2=crm.qtreceptionPage("","","","1","10").getInteger("total");

            Preconditions.checkArgument(totaol2-totaol==1,"前台分配接待列表+1");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("前台分配新客接待列表+1");
        }
    }


}
