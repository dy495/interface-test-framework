package com.haisheng.framework.testng.bigScreen.xundianDaily.hqq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.StoreFuncPackage;
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
import java.util.*;
;import static com.google.common.base.Preconditions.checkArgument;


/**
 * @author : qingqing
 * @date :  2020/07/06
 */

public class StorePcData extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    StoreFuncPackage mds = StoreFuncPackage.getInstance();
    String cycle_type = "RECENT_THIRTY";
    String month = "";
    long shop_id = 4116;
    long shop_id_01 = 43072l;
    Integer page = 1;
    Integer size = 50;

    String name = "是青青的";
    String email = "1667009257@qq.com";
    String phone = "15084928847";


    Integer status = 1;
    String type = "PHONE";


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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";


        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 日常");

        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672","18513118484", "18810332354", "15084928847"};
        //13436941018 吕雪晴
        //17610248107 廖祥茹
        //15084928847 黄青青
        //13581630214 马琨
        //18513118484 杨航
        //13259979249 黄鑫
        //18672733045 高凯
        //15898182672 华成裕
        //18810332354 刘峤


        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("store " + md);

        md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");


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


//这些一致性需要进行操作，不可用在线上客户的账户下去验证-----------------------------------------------------------------------3.0版本新增的数据一致性---------------------------------------------------

    /**
     * ====================账户管理中的一致性========================
     */
    @Test
    public void accountInfoData() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            Integer total = md.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");

            List<String> r_dList = new ArrayList<String>();
            r_dList.add("296");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("4116");

            Integer status = 1;
            String type = "EMAIL";
            //用EMAIL新增一个账号
            JSONObject res = md.organizationAccountAdd(name, email, "", r_dList, status, shop_list, type);
            Integer code = res.getInteger("code");


            //从列表获取刚刚新增的账户的account
            JSONArray accountList = md.organizationAccountPage(name, "", email, "", "", "", page, size).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("account");

            //新增账号以后，再查询列表
            Integer total1 = md.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");
            int result = total1 - total;
            Preconditions.checkArgument(result == 1, "新增1个账号，账号列表的数量却加了：" + result);


            //编辑账号的名称，是否与列表该账号的一致
            String reName = "qingqing测编辑";
            md.organizationAccountEdit(account, reName, email, "", r_dList, status, shop_list, type);
            JSONArray accountsList = md.organizationAccountPage("", "", "", "", "", "", page, size).getJSONArray("list");
            String name_1 = accountsList.getJSONObject(0).getString("name");
            Preconditions.checkArgument(name_1.equals(reName), "修改账号：" + account + "的名称为：" + reName + "修改后，该账号的名称为：" + name_1);


            //删除账号以后，再查询列表
            Integer code1 = md.organizationAccountDelete(account).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "删除emial的账号:" + email + "失败了");
            Integer total2 = md.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");
            int result1 = total1 - total2;
            Preconditions.checkArgument(result1 == 1, "删除1个账号，账号列表的数量却减了：" + result);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新增1个账号，列表+1；删除1个账号，列表-1；修改账号信息以后与列表是否一致");
        }

    }

    /**
     * ====================账户管理中的一致性========================
     */
    @Test
    public void accountInfoData_1() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            Integer total = md.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");

            List<String> r_dList = new ArrayList<String>();
            r_dList.add("296");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("4116");


            JSONArray list = md.organizationAccountPage("", "", "", "", "", "", page, size).getJSONArray("list");
            String today = dt.getHHmm(0);
            String account = "";
            String old_phone = "";
            String create_time = "";
            for (int i = 1; i < list.size(); i++) {
                create_time = list.getJSONObject(0).getString("create_time");
                if (!create_time.equals(today)) {
                    account = list.getJSONObject(0).getString("account");
                    old_phone = list.getJSONObject(0).getString("phone");
                    break;
                }
            }

            if (old_phone != "" && old_phone !=null) {
                //编辑账号的名称，权限
                String reName = "qingqing在测编辑";
                md.organizationAccountEdit(account, reName, "", old_phone, r_dList, status, shop_list, type);
                //获取列表该账号
                JSONArray accountList = md.organizationAccountPage("", "", "", old_phone, "", "", page, size).getJSONArray("list");
                String create_time_1 = "";
                String phone_1 = accountList.getJSONObject(0).getString("phone");//获取通过手机号搜索到的账号的手机号
                if (phone_1.equals(old_phone)) {
                    create_time_1 = accountList.getJSONObject(0).getString("create_time");

                }
                Preconditions.checkArgument(create_time_1.equals(create_time), "编辑昨天" + create_time + "的创建的账号" + old_phone + "列表该账号的创建时间变成了最新编辑的时间" + create_time_1);
                //编辑完以后获取列表的数量，是否有增多或者减少
                Integer total1 = md.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");
                Preconditions.checkArgument(total == total1, "编辑一个账号，账号列表的数量由:" + total + "变成了" + total1);

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("编辑账号信息以后，创建者和创建时间是否发生改变");
        }

    }

    /**
     * ====================账户管理中的一致性（使用账号数量==账号列表中的数量）========================
     */
    @Test
    public void accountInfoData_2() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            JSONArray list = md.organizationRolePage("", page, size).getJSONArray("list");
            boolean result = false;
            for (int i = 1; i < list.size(); i++) {
                String role_name = list.getJSONObject(i).getString("role_name");
                JSONArray list1 = md.organizationRolePage(role_name, page, size).getJSONArray("list");
                int account_num = list1.getJSONObject(0).getInteger("account_number");

                Integer Total = md.organizationAccountPage("", "", "", "", role_name, "", page, size).getInteger("total");
                if (account_num == Total) {
                    result = true;
                }
                Preconditions.checkArgument(result = true, "角色名为:" + role_name + "的使用账户数量：" + account_num + "！=【账户列表】中该角色的账户数量：" + Total);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("角色的使用账号数量==账号列表中该角色的数量");
        }

    }

    /**
     * ====================收银风控的数据一致性(累计的风险事件==【收银风控事件】待处理+已处理+已过期)========================
     */
    @Test
    public void cashierDataInfo() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            JSONArray list = md.cashier_page("AI-Test(门店订单录像)", "", "", "", null, page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                //获取列表门店的累计风险事件
                int risk_total = list.getJSONObject(i).getInteger("risk_total");
                //获取该门店的shop_id
                long shop_id = list.getJSONObject(i).getInteger("id");
                //获取待处理的数量
                String current_state = "PENDING";
                int pend_total = md.cashier_riskPage(shop_id_01, "", "", "", "", "", current_state, page, size).getInteger("total");

                //获取已处理的数量
                String current_state1 = "PROCESSED";
                int processe_total = md.cashier_riskPage(shop_id_01, "", "", "", "", "", current_state1, page, size).getInteger("total");

                //获取已过期的数量
                String current_state2 = "EXPIRED";
                int expired_total = md.cashier_riskPage(shop_id_01, "", "", "", "", "", current_state2, page, size).getInteger("total");

                int result = pend_total + processe_total + expired_total;
                Preconditions.checkArgument(result == risk_total, "累计风险事件：" + risk_total + "!=待处理：" + pend_total + "+已处理：" + processe_total + "+已过期：" + expired_total + "之和：" + result);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("累计的风险事件==【收银风控事件】待处理+已处理+已过期");
        }

    }

    /**
     * ====================收银风控的数据一致性（正常事件==【收银风控事件】列表页处理结果为正常的数量）========================
     */
    @Test
    public void cashierDataInfo1() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            JSONArray list = md.cashier_page("AI-Test(门店订单录像)", "", "", "", null, page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int normal_num = list.getJSONObject(i).getInteger("normal_total");
                long shop_id = list.getJSONObject(i).getInteger("id");
                //获取收银风控事件列表的正常事件的数量
                String handle_result = "NORMAL";
                int total = md.cashier_riskPage(shop_id_01, "", "", "", "", handle_result, "", page, size).getInteger("total");
                Preconditions.checkArgument(normal_num == total, "收银风控列表门店ID：" + shop_id_01 + "的正常事件：" + normal_num + "!=【收银风控事件】中该门店正常事件的数量：" + total);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("正常事件==【收银风控事件】列表页处理结果为正常的数量");
        }

    }

    /**
     * ====================收银风控的数据一致性（异常事件==【收银风控事件】列表页处理结果为异常的数量）========================
     */
    @Test
    public void cashierDataInfo2() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            JSONArray list = md.cashier_page("AI-Test(门店订单录像)", "", "", "", null, page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int abnormal_num = list.getJSONObject(i).getInteger("abnormal_total");
                long shop_id = list.getJSONObject(i).getInteger("id");
                //获取收银风控事件列表的异常事件的数量
                String handle_result = "ABNORMAL";
                int total = md.cashier_riskPage(shop_id_01, "", "", "", "", handle_result, "", page, size).getInteger("total");
                Preconditions.checkArgument(abnormal_num == total, "收银风控列表门店ID：" + shop_id_01 + "的待处理事件：" + abnormal_num + "！=【收银风控事件】中该门店待处理事件的数量：" + total);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("异常事件==【收银风控事件】列表页处理结果为异常的数量");
        }

    }

    /**
     * ====================收银风控的数据一致性（待处理事件==【收银风控事件】列表页中当前状态为待处理的事件）========================
     */
    @Test
    public void cashierDataInfo3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.cashier_page("AI-Test(门店订单录像)", "", "", "", null, page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                int pending_num = list.getJSONObject(i).getInteger("pending_risks_total");
                long shop_id = list.getJSONObject(i).getInteger("id");
                //获取收银风控事件列表的待处理事件的数量
                String current_state = "PENDING";
                int total = md.cashier_riskPage(shop_id_01, "", "", "", "", "", current_state, page, size).getInteger("total");
                Preconditions.checkArgument(pending_num == total, "收银风控列表门店ID：" + shop_id_01 + "的正常事件：" + pending_num + "！=【收银风控事件】中该门店正常事件的数量：" + total);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("待处理事件==【收银风控事件】列表页中当前状态为待处理的事件");
        }

    }

    /**
     * ====================收银风控的数据一致性（正常事件+异常事件==【收银风控事件】列表页的当前状态为已处理的数量）========================
     */
    @Test
    public void cashierDataInfo4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.cashier_page("AI-Test(门店订单录像)", "", "", "", null, page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                //获取处理结果为正常和异常的数量
                int normal_num = list.getJSONObject(i).getInteger("normal_total");
                int abnormal_num = list.getJSONObject(i).getInteger("abnormal_total");
                int result = normal_num + abnormal_num;
                long shop_id = list.getJSONObject(i).getInteger("id");
                //获取收银风控事件列表的当前状态为已处理的数量
                String current_state = "PROCESSED";
                int total = md.cashier_riskPage(shop_id_01, "", "", "", "", "", current_state, page, size).getInteger("total");
                Preconditions.checkArgument(result == total, "收银风控列表门店ID：" + shop_id_01 + "处理结果为正常+处理结果为异常的和：" + result + "！=【收银风控事件】中该门店已处理事件的数量：" + total);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("正常事件+异常事件==【收银风控事件】列表页的当前状态为已处理的数量");
        }

    }

//    /**
//     * ====================收银追溯的数据一致性（小票数量<=【收银风控事件】中的小票数量||【收银追溯】页的A小票号的时间==【收银风控时间】中A小票号的时间）========================
//     */
//    @Test
//    public void orderDataInfo() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            //获取收银风控事件列表的小票总数量
//            JSONArray list1 = response.getJSONArray("list");
//            String order_id1 = list1.getJSONObject(0).getString("order_id");
//            String order_time1 = list1.getJSONObject(0).getString("order_date");
//            JSONObject response = md.cashier_riskPage(shop_id_01, "", order_id1, "", "", "", "", page, size);
//
//            JSONObject res = md.cashier_traceBack(shop_id_01, "", "", page, size);
//            JSONArray list = res.getJSONArray("list");
//
//            //获取收银追溯页面的第一个小票单号
//            String order_id = list.getJSONObject(0).getString("order_id");
//            Long order_time = list.getJSONObject(0).getLong("order_time");
//            String order_time_02 = dt.timestampToDate("yyyy-MM-dd HH:mm:ss", order_time);
//
//
//            Preconditions.checkArgument(order_time_02.equals(order_time1), "【收银追溯】中列表门店ID：" + shop_id_01 + "小票：" + order_id + "的下单时间" + order_time_02 + "!==【收银风控事件】中小票号" + order_id + "的时间" + order_time1);
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("小票数量<=【收银风控事件】中的小票数量||【收银追溯】页的A小票号的时间==【收银风控时间】中A小票号的时间");
//        }
//    }

//    /**
//     * ====================收银风控事件的数据一致性（待处理进行处理为正常,【收银风控】列表正常事件+1；【收银风控】列表待处理-1，累计风险数量不变）========================
//     */
//    @Test
//    public void ruleDeal_DataInfo() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //获取收银追溯列表第一个门店的门第ID和门店名称，累计正常事件，和待处理事件
//            JSONArray dataList = md.cashier_page("", "", "", "", null, page, size).getJSONArray("list");
//            long shop_id = dataList.getJSONObject(0).getInteger("shop_id");
//            String shop_name = dataList.getJSONObject(0).getString("shop_name");
//            int normal_total = dataList.getJSONObject(0).getInteger("normal_total");
//            int pending_total = dataList.getJSONObject(0).getInteger("pending_risks_total");
//            int risk_total = dataList.getJSONObject(0).getInteger("risk_total");
//
//
//            //获取待处理风险事件ID
//            JSONArray list = md.cashier_riskPage(shop_id, "", "", "", "", "PENDING", "", page, size).getJSONArray("list");
//            long id = list.getJSONObject(0).getInteger("id");
//
//            String order = list.getJSONObject(0).getString("order_id");
//
//
//            //将待处理的风控事件处理成正常
//            md.cashier_riskEventHandle(id, 1, "人工处理订单无异常");
//            //获取处理完以后的累计正常事件和待处理事项
//            JSONArray list1 = md.cashier_page(shop_name, "", "", "", null, page, size).getJSONArray("list");
//            int normal_total1 = list1.getJSONObject(0).getInteger("normal_total");
//            int pending_total1 = list1.getJSONObject(0).getInteger("pending_risks_total");
//            int risk_total1 = dataList.getJSONObject(0).getInteger("risk_total");
//
//            Preconditions.checkArgument(normal_total - normal_total1 == 1, "将待处理事件中小票单号为" + order + "处理成正常，【收银风控】列表正常事件-处理前正常事件！=1");
//            Preconditions.checkArgument(pending_total1 - pending_total == 1, "将待处理事件中小票单号为" + order + "处理成正常，【收银风控】列表待处理没有-1");
//            Preconditions.checkArgument(risk_total1 == risk_total, "将待处理事件中小票单号为" + order + "处理成正常，累计风险数量数量变化了，处理前：" + risk_total + "处理以后：" + risk_total1);
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("待处理进行处理为正常,【收银风控】列表正常事件+1；【收银风控】列表待处理-1，累计风险数量不变");
//        }
//    }

    /**
     * ====================收银风控事件的数据一致性（待处理进行处理为异常,【【收银风控】列表异常事件+1；【收银风控】列表待处理-1，累计风险数量不变）========================
     */
   // @Test
    public void ruleDeal_DataInf1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取收银追溯列表第一个门店的门第ID和门店名称，累计异常事件，和待处理事件
            JSONArray data_list = md.cashier_page("", "", "", "", null, page, size).getJSONArray("list");
            long shop_id = data_list.getJSONObject(0).getInteger("shop_id");
            String shop_name = data_list.getJSONObject(0).getString("shop_name");
            int normal_total = data_list.getJSONObject(0).getInteger("normal_total");
            int pending_total = data_list.getJSONObject(0).getInteger("pending_risks_total");
            int risk_total = data_list.getJSONObject(0).getInteger("risk_total");


            //获取待处理风险事件ID和order_id
            JSONArray list = md.cashier_riskPage(shop_id, "", "", "", "", "", "PENDING", page, size).getJSONArray("list");
            long id = list.getJSONObject(0).getInteger("id");
            String order = list.getJSONObject(0).getString("order_id");


            //将待处理的风控事件处理成异常
            md.cashier_riskEventHandle(id, 0, "该客户有刷单造假的嫌疑，请注意");
            //获取处理完以后的累计异常事件和待处理事项
            JSONArray data_list1 = md.cashier_page(shop_name, "", "", "", null, page, size).getJSONArray("list");
            int normal_total1 = data_list1.getJSONObject(0).getInteger("normal_total");
            int pending_total1 = data_list1.getJSONObject(0).getInteger("pending_risks_total");
            int risk_total1 = data_list1.getJSONObject(0).getInteger("risk_total");

            Preconditions.checkArgument(normal_total - normal_total1 == 1, "将待处理事件中小票单号为" + order + "处理成异常，【收银风控】列表异常事件-处理前异常事件！=1");
            Preconditions.checkArgument(pending_total1 - pending_total == 1, "将待处理事件中小票单号为" + order + "处理成异常，【收银风控】列表待处理没有-1");
            Preconditions.checkArgument(risk_total1 == risk_total, "将待处理事件中小票单号为" + order + "处理成异常，累计风险数量数量变化了，处理前：" + risk_total + "处理以后：" + risk_total1);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("待处理进行处理为异常,【【收银风控】列表异常事件+1；【收银风控】列表待处理-1，累计风险数量不变");
        }
    }

    /**
     * ====================收银风控事件的数据一致性（涉当前状态为【待处理】==响应时长；处理人；处理结果；备注为空）========================
     */
    @Test
    public void orderInfoData() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.cashier_riskPage(shop_id_01, "", "", "", "", "PENDING", "", page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String order_id = list.getJSONObject(i).getString("order_id");
                String response_time = list.getJSONObject(i).getString("response_time");
                String handler = list.getJSONObject(i).getString("handler");
                String remarks = list.getJSONObject(i).getString("remarks");
                String handle_result = list.getJSONObject(i).getString("handle_result");
                Preconditions.checkArgument(response_time.isEmpty(), "待处理的事件：" + order_id + "的响应时间不为空");
                Preconditions.checkArgument(handler.isEmpty(), "待处理的事件：" + order_id + "的处理人不为空");
                Preconditions.checkArgument(remarks.isEmpty(), "待处理的事件：" + order_id + "的备注不为空");
                Preconditions.checkArgument(handle_result.isEmpty(), "待处理的事件：" + order_id + "的处理结果不为空");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("涉当前状态为【待处理】==响应时长；处理人；处理结果；备注为空");
        }

    }

    /**
     * ====================风控规则的数据一致性（新增一个规则==更新者为新增该风控规则的人员;列表风险规则+1;）========================
     */
    @Test
    public void creatRuleInfo() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total = md.risk_controlPage("", "", "", null, page, size).getInteger("total");

            //新建一个风控规则rule中的参数再调试时要进行修改
            String name = "QA_test01";
            JSONObject para = new JSONObject();
            para.put("DAY_RANGE", 10);
            para.put("ORDER_QUANTITY_UPPER_LIMIT", 5);

            JSONObject rule = new JSONObject();
            rule.put("type", "CASHIER");
            rule.put("item", "RISK_SINGLE_MEMBER_ORDER_QUANTITY");
            rule.put("parameters", para);

            String shop_type = "NORMAL";

            int id = md.riskRuleAdd(name, shop_type, rule).getJSONObject("data").getInteger("id");
            int total1 = md.risk_controlPage("", "", "", null, page, size).getInteger("total");
            Preconditions.checkArgument(total1 - total == 1, "新增一个规则以后，新增前后风控规则列表总数相差不为1，新增前：" + total + "新增后：" + total1);

            //删除刚刚新建的这个风控规则
            md.risk_controlDelete(id);
            int total2 = md.risk_controlPage("", "", "", null, page, size).getInteger("total");
            Preconditions.checkArgument(total1 - total2 == 1, "删除一个规则以后，删除前后列表的总数相差不为1，删除前：" + total1 + "删除后：" + total2);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控规则的数据一致性（新增一个规则，列表+1/删除一个规则，列表-1）");
        }

    }

    /**
     * ====================风控告警列表的数据一致性（最新告警时间>=首次告警时间）========================
     */
    @Test
    public void ruleSwitch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = md.alarm_page("", "", "", page, size).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String first_alarm_time = list.getJSONObject(i).getString("first_alarm_time");
                String last_alarm_time = list.getJSONObject(i).getString("last_alarm_time");
                boolean result = new DateTimeUtil().timeDiff(first_alarm_time, last_alarm_time, "yyyy-MM-dd HH:mm:ss") >= 0;
//                System.err.println(result);
                Preconditions.checkArgument(result = true, "最新告警时间<首次告警时间，首次告警时间：" + first_alarm_time + "最新告警时间" + last_alarm_time);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控告警列表的数据一致性（最新告警时间>=首次告警时间）");
        }

    }


    /**
     * ====================风控告警规则的数据一致性（新增一个风控告警规则，列表+1；删除一个规则，列表-1；）========================
     */
    @Test
    public void risk_ruleDataInfo() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int total = md.alarm_rulePage("", "", "", null, page, size).getInteger("total");
            //新增一个沉默时间为10分钟的告警
            String name = "q_test01";
            String type = "CASHIER";

            //风控告警规则的ID
            JSONArray rule_id = new JSONArray();
            rule_id.add(57);
            //告警接收者按照选择角色的ID
            JSONArray accept_id = new JSONArray();
            accept_id.add(3);

            String start_time = "08:00";
            String end_time = "16:00";
            String silent_time = "6400000";

            md.alarm_ruleAdd(name, type, rule_id, accept_id, start_time, end_time, silent_time);
            int total1 = md.alarm_rulePage("", "", "", null, page, size).getInteger("total");
            Preconditions.checkArgument(total1 - total == 1, "新增一个风控告警规则以后，新增前后风控规则列表总数相差不为1，新增前：" + total + "新增后：" + total1);


            //在列表查找这个新增成功的规则
            JSONArray list = md.alarm_rulePage(name, type, "", null, page, size).getJSONArray("list");
            String name2 = list.getJSONObject(0).getString("name");
            int id = list.getJSONObject(0).getInteger("id");
            checkArgument(name2.equals(name), "新增风控告警规则，在列表找不到");

            //删除刚刚新增的风控告警规则
            md.alarm_ruleDelete(id);
            int total2 = md.alarm_rulePage("", "", "", null, page, size).getInteger("total");
            Preconditions.checkArgument(total1 - total2 == 1, "删除一个风控规则以后，删除前后列表的总数相差不为1，删除前：" + total1 + "删除后：" + total2);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("风控告警规则的数据一致性（新增一个规则，列表+1/删除一个规则，列表-1）");
        }

    }
    /**
     * ====================历史数据-区域关注度数据(自然日)======================
     */
    //@Test()
    public void region_data1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = "2020-02-01";
            JSONObject res = md.regin_PUv(shop_id,"NATURE_DAY",date);
            JSONObject data = res.getJSONObject("data");
            JSONArray region_list = data.getJSONArray("region_data_day_list");
            Map<String, Integer> Puvdata = mds.getRegionData(region_list);
            int total_pv =Puvdata.get("total_pv");
            int total_uv =Puvdata.get("uv2");
            int total_stay_time =Puvdata.get("total_stay_time");
            int pv = Puvdata.get("pv");
            int uv =Puvdata.get("uv");
            int stay_time =Puvdata.get("stay_time");

            checkArgument(total_pv==pv, "【区域关注度】时间选择为-日-该日"+date+"总PV =="+date+"【折线图】中的PV，门店ID"+shop_id);
            checkArgument(total_uv<=uv, "【区域关注度】时间选择为-日-该日"+date+"总UV =="+date+"【折线图】中的UV，门店ID"+shop_id);
            checkArgument(total_stay_time==stay_time, "【区域关注度】时间选择为-日-该日"+date+"总停留时长 =="+date+"【折线图】中的停留时长，门店ID"+shop_id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【区域关注度】时间选择为-日-该日总PV/UV/停留时长 ==该日【折线图】中的PV/UV/停留时长");
        }

    }

    /**
     * ====================历史数据-区域关注度数据(周)======================
     */
    //@Test()
    public void region_data2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String date = "2020-02-01";
            JSONArray region_list = md.regin_PUv(shop_id_01,"NATURE_WEEK","2021-02-25").getJSONArray("region_data_list");
            Map<String, Integer> Puvdata = mds.getRegionData1(region_list);
            int total_pv =Puvdata.get("total_pv");
            int total_uv =Puvdata.get("uv2");
            int pv = Puvdata.get("pv");
            int uv =Puvdata.get("uv");

            checkArgument(total_pv==pv, "【区域关注度】时间选择为-日-该周，从日期："+date+"开始的一周。总PV =="+date+"【折线图】中的PV，门店ID"+shop_id);
            checkArgument(total_uv<=uv, "【区域关注度】时间选择为-日-该周，从日期："+date+"开始的一周。总UV =="+date+"【折线图】中的UV，门店ID"+shop_id);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【区域关注度】时间选择为-周-该周总PV/UV/停留时长 ==该周【折线图】中的PV/UV/停留时长");
        }

    }


}

