package com.haisheng.framework.testng.bigScreen;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.collect.ImmutableMap;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class FeidanMiniApiDataConsistencyDaily {
    //    -------------------------------------------------数据一致性验证-------------------------------------------------------------


    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        qaDbUtil.openConnection();
        qaDbUtil.openConnectionRdDaily();
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        initHttpConfig();
        String path = "/risk-login";
        String loginUrl = getIpPort() + path;
        String json = "{\"username\":\"yuexiu@test.com\",\"passwd\":\"f5b3e737510f31b88eb2d4b5d0cd2fb4\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization: {}", this.authorization);
        } catch (Exception e) {
            aCase.setFailReason("http post 调用异常，url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, ciCaseName, caseName, "登录获取authentication");
    }

    @AfterClass
    public void clean() {
        qaDbUtil.closeConnection();
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    @Test
    public void testShopList() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String path = "/risk/shop/list";
            String json = "{}";
            String checkColumnName = "list";
            httpPostWithCheckCode(path, json, checkColumnName);

        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "校验shop");
        }

    }

    /**
     * H5页面业务员报备总数与H5页面内业务员报备条数一致  gaile
     */
    @Test
    public void reportInfoEquals() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：H5页面业务员报备总数与H5页面内业务员报备条数一致\n";

        try {

//            H5页面内报备数
            String staffPhone = "17722222221";
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
                    "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";

            customerReportH5("2098", caseName, genPhoneStar(), "MALE", token);

            String staffDetailH5 = staffDetailH5(token);

            int reportNumH5 = JSON.parseObject(staffDetailH5).getJSONObject("data").getInteger("report_num");
            int reportNumListNum = 0;
            int reportNumListTotal = 0;
            if (reportNumH5 > 50) {
                System.out.println(reportNumH5);

                int a = (int) Math.ceil(reportNumH5 / 50) + 1;
                System.out.println(a);
                for (int i = 1; i <= a; i++) {
                    String customerListH5 = channelCustomerListH5(token, i, 50);
                    reportNumListNum = reportNumListNum + JSON.parseObject(customerListH5).getJSONObject("data").getJSONArray("list").size();

                    reportNumListTotal = JSON.parseObject(customerListH5).getJSONObject("data").getInteger("total");
                }
            } else {
                String customerListH5 = channelCustomerListH5(token, 1, 50);
                reportNumListNum = JSON.parseObject(customerListH5).getJSONObject("data").getJSONArray("list").size();
                reportNumListTotal = JSON.parseObject(customerListH5).getJSONObject("data").getInteger("total");
            }

            if (reportNumH5 != reportNumListNum) {
                throw new Exception("业务员手机号:" + staffPhone + "， H5页面内的报备总数=" + reportNumH5 + ", H5页面内的报备条数=" + reportNumListNum + "，与预期结果不符");
            }

            if (reportNumListNum != reportNumListTotal) {
                throw new Exception("业务员手机号:" + staffPhone + "， H5页面内列表中的报备条数=" + reportNumListTotal + ", H5页面内的显示的报备条数=" + reportNumListNum+ "，与预期结果不符");
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * H5页面报备总数与PC页面内报备总数一致 gaile
     */
    @Test
    public void reportNumPCEqualsH5() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：H5页面报备总数与PC页面内报备总数一致\n";

        try {

            int reportNumPC = 0;

            String staffPhone = "17722222221";
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
                    "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";

            customerReportH5("2098", caseName, genPhoneStar(), "MALE", token);

            String staffDetailH5 = staffDetailH5(token);

            int reportNumH5 = JSON.parseObject(staffDetailH5).getJSONObject("data").getInteger("report_num");

            String channelId = "5";
            int totalnum = Integer.parseInt(channelStaffList(channelId, staffPhone, 1, pageSize).getString("total"));
            if (totalnum > 50) {
                int a = (int) Math.ceil(totalnum / 50) + 1;
                for (int i = 1; i <= a; i++) {
                    JSONArray staffList = channelStaffList(channelId, staffPhone, i, pageSize).getJSONArray("list");
                    for (int j = 0; j < staffList.size(); j++) {
                        JSONObject single = staffList.getJSONObject(j);
                        if ("2098".equals(single.getString("id"))) {
                            reportNumPC = reportNumPC + single.getInteger("total_report");
                            break;
                        } else {
                            throw new Exception("不存在手机号为：" + staffPhone + "的业务员。");
                        }
                    }
                }
            } else {
                JSONArray staffList = channelStaffList(channelId, staffPhone, 1, pageSize).getJSONArray("list");
                for (int i = 0; i < staffList.size(); i++) {
                    JSONObject single = staffList.getJSONObject(i);
                    if ("2098".equals(single.getString("id"))) {
                        reportNumPC = single.getInteger("total_report");
                        break;
                    } else {
                        throw new Exception("不存在手机号为：" + staffPhone + "的业务员。");
                    }
                }
            }


            if (reportNumH5 != reportNumPC) {
                throw new Exception("业务员手机号:" + staffPhone + ", H5页面内的报备数=" + reportNumH5 + ", PC端的报备数=" + reportNumPC+ "，与预期结果不符");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 渠道总报备数==该渠道每个业务员的报备数 重复
     */
    //@Test
    public void channelEqualsStaffReport() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：渠道总报备数与该渠道每个业务员的报备数之和一致\n";

        try {

            JSONArray list = channelList(1, pageSize).getJSONArray("list");

            checkChannelEqualsStaff(list);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 渠道的累计报备数==各个业务员的累计报备数之和  gaile
     **/
    @Test
    public void channelTotalEqualsStaffTotal() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：渠道的累计报备数=各个业务员的累计报备数之和\n";
        try {
            JSONArray channelList = channelList(1, pageSize).getJSONArray("list");

            for (int i = 0; i < channelList.size(); i++) {
                JSONObject singleChannel = channelList.getJSONObject(i);
                int channelNum = singleChannel.getInteger("total_customers");
                String channelId = singleChannel.getString("channel_id");

                String channelName = singleChannel.getString("channel_name");

                int staffNum = 0;
                int totalnum = Integer.parseInt(channelStaffList(channelId, "", 1, pageSize).getString("total"));
                if (totalnum > 50) {
                    int a = (int) Math.ceil(totalnum / 50) + 1;
                    for (int j = 1; j <= a; j++) {
                        JSONArray staffList = channelStaffList(channelId, "", j, pageSize).getJSONArray("list");
                        for (int k = 0; k < staffList.size(); k++) {
                            JSONObject singleStaff = staffList.getJSONObject(k);
                            staffNum += singleStaff.getInteger("total_report");
                        }
                    }
                } else {
                    JSONArray staffList = channelStaffList(channelId, "", 1, pageSize).getJSONArray("list");
                    for (int k = 0; k < staffList.size(); k++) {
                        JSONObject singleStaff = staffList.getJSONObject(k);
                        staffNum += singleStaff.getInteger("total_report");
                    }
                }


                if (staffNum != channelNum) {
                    throw new Exception("渠道【" + channelName + "】,渠道累计报备数=" + channelNum + " ，业务员累计报备数之和=" + staffNum+ " ，与预期结果不符");
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    /**
     * 证据页事项与风控列表中展示的信息一致：置业顾问、成交渠道、首次到访时间、刷证时间 要写一下订单ID
     */
    //@Test
    public void OrderListLinkEquals() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：证据页事项与风控列表中展示的信息一致\n";

        try {

            JSONArray list = orderList(1, "", 1, pageSize).getJSONArray("list");

            checkOrderListEqualsLinkList(list);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 风控列表过滤项的子单数 <= 总单数
     */
    @Test
    public void OrderListFilter() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验：风控列表过滤项的子单数 <= 总单数\n";

        try {

            checkOrderListFilter();

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    /**
     * 订单详情与订单列表中信息是否一致
     **/
    @Test
    public void dealListEqualsDetail() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            // 订单列表
            JSONArray list = orderList(1, "", 1, pageSize).getJSONArray("list");
            for (int i = 0; i < list.size() && i <= 20; i++) {
                JSONObject single = list.getJSONObject(i);
                String orderId = getValue(single, "order_id");
                String customerName = getValue(single, "customer_name");
                String adviserName = getValue(single, "adviser_name");
                String channelName = getValue(single, "channel_name");
                String firstAppearTime = getValue(single, "first_appear_time");
                String reportTime = getValue(single, "report_time");
                String signTime = getValue(single, "sign_time");
                String dealTime = getValue(single, "deal_time");
                String status = getValue(single, "status");
                String isAudited = getValue(single, "is_audited");

                if ("3".equals(orderId)) {

                    JSONObject data = orderDetail(orderId);
                    compareValue(data, "订单", orderId, "customer_name", customerName, "顾客姓名");
                    compareValue(data, "订单", orderId, "adviser_name", adviserName, "置业顾问");
                    compareValue(data, "订单", orderId, "channel_name", channelName, "渠道名称");
                    compareValue(data, "订单", orderId, "order_status", status, "订单状态");
                    compareValue(data, "订单", orderId, "is_audited", isAudited, "是否审核");

                    compareOrderTimeValue(data, "first_appear_time", orderId, firstAppearTime, "订单列表", "订单详情");
                    compareOrderTimeValue(data, "report_time", orderId, reportTime, "订单列表", "订单详情");
                    compareOrderTimeValue(data, "sign_time", orderId, signTime, "订单列表", "订单详情");
                    compareOrderTimeValue(data, "deal_time", orderId, dealTime, "订单列表", "订单详情");
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "校验订单详情与订单列表中信息是否一致\n");
        }
    }


    /**
     * 渠道中的报备顾客数 >= 0
     **/
    @Test
    public void channelReportCustomerNum() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            //查询渠道列表，获取channel_id
            JSONArray channelList = channelList(1, pageSize).getJSONArray("list");

            for (int i = 0; i < channelList.size(); i++) {
                JSONObject singleChannel = channelList.getJSONObject(i);
                String channelName = singleChannel.getString("channel_name");
                Integer channelReportNum = singleChannel.getInteger("total_customers");

                if (null == channelReportNum || channelReportNum < 0) {
                    throw new Exception("渠道【" + channelName + "】, 渠道列表中的报备数：" + channelReportNum+ "，与预期结果不符");
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：渠道中的报备顾客数不小于0\n");
        }
    }

    /**
     * 订单列表中，风险+正常+未知的订单数==订单列表总数 gaile
     **/
    @Test
    public void orderListDiffType() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String totalList = orderListt(pageSize).getString("total");
            int totalNum = Integer.parseInt(totalList);

//            获取正常订单数
            int normal_totalnum = Integer.parseInt(orderList(1, "", 1, pageSize).getString("total"));
            System.out.println("normal" + normal_totalnum);

//            获取未知订单数
            int unknown_totalnum = Integer.parseInt(orderList(2, "", 1, pageSize).getString("total"));
            System.out.println("unkonw" + unknown_totalnum);

//            获取风险订单数
            int risk_totalnum = Integer.parseInt(orderList(3, "", 1, pageSize).getString("total"));
            System.out.println("risk" + risk_totalnum);


            if (normal_totalnum + unknown_totalnum + risk_totalnum != totalNum) {
                throw new Exception("订单列表总数" + totalNum + " != 正常订单数" + normal_totalnum + " + 未知订单数" + unknown_totalnum + " + 异常订单数" + risk_totalnum+ " ，与预期结果不符");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：订单列表中，风险+正常+未知的订单数=订单列表总数\n");
        }
    }

    /**
     * 员工管理中，各类型员工数量统计是否正确 V2.4取消员工类型
     **/
    @Test
    public void staffTypeNum() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

//            1、获取员工类型
            JSONArray staffTypeList = staffTypeList();

            HashMap<String, String> staffTypes = new HashMap<>();

            for (int i = 0; i < staffTypeList.size(); i++) {

                JSONObject singleType = staffTypeList.getJSONObject(i);

                staffTypes.put(singleType.getString("staff_type"), singleType.getString("type_name"));
            }

//            2、查询员工总体中各类型的员工数
            JSONArray totalList = staffList(1, pageSize);

            HashMap<String, Integer> staffNumHm = new HashMap<>();

            for (String key : staffTypes.keySet()) {
                staffNumHm.put(key, 0);
            }

            for (int j = 0; j < totalList.size(); j++) {
                String staffType = totalList.getJSONObject(j).getString("staff_type");
                staffNumHm.put(staffType, staffNumHm.get(staffType) + 1);
            }

//            3、查询各个类型的员工列表
            for (Map.Entry<String, String> entry : staffTypes.entrySet()) {
                String staffType = entry.getKey();

                JSONArray array = staffListWithType(staffType, 1, pageSize);
                int size = 0;
                if (array != null) {
                    size = array.size();
                }

                if (size != staffNumHm.get(staffType)) {
                    throw new Exception("不选员工类型时，列表返回结果中【" + staffTypes.get(staffType) + "】的数量为：" + staffNumHm.get(staffType) +
                            ", 选择类型查询时，查询结果中该类型员工数为：" + array.size()+ "，与预期结果不符");
                }
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：员工管理中，各类型员工数量统计准确性\n");
        }
    }

    /**
     * 订单列表按照新建时间倒排
     **/
    @Test
    public void orderListRank() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            // 订单列表
            JSONArray jsonArray = orderList(1, "", 1, pageSize).getJSONArray("list");
            checkRank(jsonArray, "customer_phone", "订单列表\n");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：订单列表按照新建时间倒排\n");
        }
    }

    /**
     * 员工列表按照新建时间倒排
     **/
    @Test
    public void staffListRank() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            // 员工列表
            JSONArray jsonArray = staffList(1, pageSize);
            checkRank(jsonArray, "phone", "员工列表\n");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：员工列表按照新建时间倒排\n");
        }
    }

    /**
     * 渠道列表按照新建时间倒排
     **/
    @Test
    public void channelListRank() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            // 渠道列表
            JSONArray jsonArray = channelList(1, pageSize).getJSONArray("list");
            checkRank(jsonArray, "phone", "渠道列表\n");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：渠道列表按照新建时间倒排\n");
        }
    }

    /**
     * 渠道员工列表按照新建时间倒排
     **/
    @Test
    public void channelStaffListRank() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            // 渠道员工列表
            JSONArray jsonArray = channelStaffList(mineChannelStr, "", 1, pageSize).getJSONArray("list");
            checkRank(jsonArray, "phone", "渠道员工列表\n");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：渠道员工列表按照新建时间倒排\n");
        }
    }





    /**
     * V3.0截止昨天-未知订单=截止昨天24点前订单页的未知订单数量 ok
     **/
    @Test
    public void FKdata_unknownOrder() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            int til24unknownnum = getnum(2); //订单列表中截止今天00：00：00之前的未知订单数量
            int unknow_order = Integer.parseInt(historyRuleDetail().getString("unknow_order")); //风控数据中未知订单数量
            if (til24unknownnum != unknow_order){
                throw new Exception("截止今天零点，订单列表中未知订单数量=" + til24unknownnum +" ，风控数据页未知订单数量=" + unknow_order + " ，与预期结果不符");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页未知订单数量与截止今天零点订单页的未知订单数量一致\n");
        }
    }



    /**
     * V3.0截止昨天-正常订单=截止昨天24点前订单页的正常订单数量 ok
     **/
    @Test
    public void FKdata_normalOrder() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            int til24normalnnum = getnum(1); //订单列表中截止今天00：00：00之前的正常订单数量
            int normal_order = Integer.parseInt(historyRuleDetail().getString("normal_order")); //风控数据中正常订单数量
            if (til24normalnnum != normal_order){
                throw new Exception("截止今天零点，订单列表中正常订单数量=" + til24normalnnum +" ，风控数据页正常订单数量=" + normal_order + " ，与预期结果不符");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页正常订单数量与截止今天零点订单页的正常订单数量一致\n");
        }
    }

    /**
     * V3.0截止昨天-风险订单=截止昨天24点前订单页的风险订单数量 ok
     **/
    @Test
    public void FKdata_riskOrder() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            int til24risknnum = getnum(3); //订单列表中截止今天00：00：00之前的风险订单数量
            int risk_order = Integer.parseInt(historyRuleDetail().getString("risk_order")); //风控数据中风险订单数量
            if (til24risknnum != risk_order){
                throw new Exception("截止今天零点，订单列表中风险订单数量=" + til24risknnum +" ，风控数据页风险订单数量=" + risk_order + " ，与预期结果不符");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页风险订单数量与截止今天零点订单页的风险订单数量一致\n");
        }
    }
    /**
     * V3.0订单趋势-订单数量=某n天订单页的订单数量 有点问题
     **/
    //@Test
    public void FKdata_riskOrderTrend() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String starttime = Long.toString(getStartTime(7));
            String endtime = Long.toString(getStartTime(1));
            JSONArray list = historyOrderTrend("WEEK",starttime,endtime).getJSONArray("list"); //订单趋势中风险订单数量
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                long day = Long.parseLong(single.getString("day"));
                int list_risknum = getTimeNum(3,day);//该天订单列表中风险订单数
                int list_normalnum = getTimeNum(1,day);//该天订单列表中正常订单数
                int list_unknownnum = getTimeNum(2,day);//该天订单列表中未知订单数
                int list_all = list_risknum + list_normalnum + list_unknownnum; //该天订单列表中全部订单数

                int risk_order = single.getInteger("risk_order");
                int unknow_order = single.getInteger("unknow_order");
                int normal_order = single.getInteger("normal_order");
                int all_order = single.getInteger("all_order");
                SimpleDateFormat sdff=new SimpleDateFormat("yyyy-MM-dd");// 时间戳转换成时间
                String sd = sdff.format(new Date(day));//时间戳转为日期
                System.out.println(sd);//打印

                if (list_all != all_order){
                    throw new Exception(sd + " : 订单列表中全部订单数=" + list_all + " ， 风控数据页面订单趋势中，当天的全部订单数=" + all_order + " , 与预期不符");
                }
                else {
                    if (list_risknum != risk_order){
                        throw new Exception(sd + " : 订单列表中风险订单数=" + list_risknum + " ， 风控数据页面订单趋势中，当天的风险订单数=" + risk_order + " , 与预期不符");
                    }
                    else {
                        if (list_normalnum != normal_order){
                            throw new Exception(sd + " : 订单列表中正常订单数=" + list_normalnum + " ， 风控数据页面订单趋势中，当天的正常订单数=" + normal_order + " , 与预期不符");
                        }
                        else {
                            if (list_unknownnum != unknow_order){
                                throw new Exception(sd + " : 订单列表中未知订单数=" + list_unknownnum + " ， 风控数据页面订单趋势中，当天的未知订单数=" + unknow_order + " , 与预期不符");
                            }
                        }
                    }
                }

            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页风险订单数量与截止今天零点订单页的风险订单数量一致\n");
        }
    }


    /**
     * V3.0风控数据--异常环节数=每个订单异常环节之和
     **/
    @Test
    public void FKdata_risklink() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            int total = orderList(3,"",1,10).getInteger("total");
            JSONArray list = orderList(3,"",1,total).getJSONArray("list");
            int risklinknunm = 0; //各订单异常环节总数
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                risklinknunm = risklinknunm + single.getInteger("risk_link");
            }
            int historynum = historyRuleDetail().getInteger("abnormal_link"); //风控数据页异常环节数
            if (risklinknunm != historynum){
                throw new Exception("订单列表中，各风险订单异常环节总数=" + risklinknunm + "，风控数据页，异常环节数=" + historynum + ", 与预期不符");

            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页异常环节数与风险订单总异常环节数一致\n");
        }
    }


    /**
     * V3.0截至昨天，自然访客/渠道访客 有问题
     **/
    //@Test
    public void FKdata_fangke() throws Exception {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;


        try {

            int [] customer = getcustomer_num();
            int [] order = getorder_num();
            int channel = 0;
            int natual = 0;
            natual = customer[0] + order[0];
            channel = customer[1] + order[1];
            System.out.println("natual" + natual);
            System.out.println("channel" + channel);
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页访客数量\n");
        }

    }




    /**
     * V3.0到访人物页中成交顾客人数的照片数 >= 【风险订单 + 正常订单】订单个数总和 日常环境无法保证
     **/
    //@Test
    public void DFpeople_dealOrder() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            int DFdeafnum = Integer.parseInt(personCatch(1,10,"DEAL","","","").getString("total"));
            int risknum = Integer.parseInt(orderList(3,"",1,10).getString("total"));
            int normalnum = Integer.parseInt(orderList(1,"",1,10).getString("total"));
            int riskaddnormal = risknum + normalnum;
            if (DFdeafnum < riskaddnormal){
                throw new Exception("到访人物页成交顾客照片数量=" + DFdeafnum + " , 截至目前，风险订单+正常订单数量=" + riskaddnormal + " ，成交顾客照片数小于订单数，与预期不符");
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：到访人物页中成交顾客人数的照片数 >= 风险订单数 + 正常订单数\n");
        }
    }



    //---------三个页面-------------
    /**
     * V2.3 今日到访人数=今日客流身份分布中的总人数
     **/
    @Test
    public void todayPeople() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            int uv = realTime().getInteger("uv"); //今日到访人数
            int oldnew = 0; //今日新客+老客人数
            JSONArray list = realCustomerType().getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                oldnew = oldnew + single.getInteger("num");
            }
            if (uv!=oldnew){
                throw new Exception("今日实时页面：今日到访人数=" + uv + " , 今日客流分布总人数=" + oldnew + " , 与预期不一致");
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：今日实时页面,今日到访人数=今日客流分布总人数\n");
        }
    }

    /**
     * V2.3 今日到访人数<=今日到访趋势中各时间段的累计人数
     **/
    @Test
    public void todayuvLEtimeAdd() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            int uv = realTime().getInteger("uv"); //今日到访人数
            int timeadd = 0; //今日到访趋势中各时间段的累计人数
            JSONArray list = realPersonAccumulate().getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                timeadd = timeadd + single.getInteger("present_cycle");
            }
            if (uv>timeadd){
                throw new Exception("今日实时页面：今日到访人数=" + uv + " , 今日到访趋势中各时间段的累计人数=" + timeadd + " , 与预期不一致");
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：今日实时页面,今日到访人数<=今日到访趋势中各时间段的累计人数\n");
        }
    }

    /**
     * V2.3 历史统计页面：本周累计到访人数<=顾客到访趋势中每天的累计人数
     **/
    @Test
    public void historyuvLEtimeAdd() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String pattern = "yyyy-MM-dd";

        DateTime dateTime = new DateTime();

        try {

            String startTime = dateTimeUtil.getBeginDayOfWeek(dateTime.toLocalDateTime().toDate(), pattern);
            System.out.println(startTime);

            int uv = historyShop(startTime).getInteger("uv"); //这周历史累计到访人数
            int timeadd = 0; //这周顾客到访趋势中各时间段的累计人数
            JSONArray list = historypersonAccumulate(startTime).getJSONArray("list");
            int validDays = getValidDays(historypersonAccumulate(startTime));//有效天数
            for (int i = 0; i < validDays;i++){
                JSONObject single = list.getJSONObject(i);
                timeadd = timeadd + single.getInteger("present_cycle");
            }
            if (uv>timeadd){
                throw new Exception("历史统计页面：本周累计到访人数=" + uv + " , 顾客到访趋势中每天的累计人数=" + timeadd + " , 与预期不一致");
            }


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：历史统计页面：本周累计到访人数<=顾客到访趋势中每天的累计人数\n");
        }
    }

    /**
     * V2.3 历史统计页面：累计到访人数==顾客身份分布中的总人数
     **/
    @Test
    public void historyPeople() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String pattern = "yyyy-MM-dd";

        DateTime dateTime = new DateTime();

        try {

            String startTime = dateTimeUtil.getBeginDayOfWeek(dateTime.toLocalDateTime().toDate(), pattern);
            System.out.println(startTime);
            int uv = historyShop(startTime).getInteger("uv"); //本周累计到访人数
            int oldnew = 0; //今日新客+老客人数
            JSONArray list = historyCustomerType(startTime).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject single = list.getJSONObject(i);
                oldnew = oldnew + single.getInteger("num");
            }
            if (uv!=oldnew){
                throw new Exception("历史统计页面：本周累计到访人数=" + uv + " , 顾客身份分布总人数=" + oldnew + " , 与预期不一致");
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：历史统计页面：本周累计到访人数=顾客身份分布总人数\n");
        }
    }





//    ----------------------------------------------变量定义--------------------------------------------------------------------

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String failReason = "";

    private String response = "";

    private boolean FAIL = false;

    private Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();

    private QADbUtil qaDbUtil = new QADbUtil();

    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;

    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    String mineChannelStr = "5";

    int pageSize = 50;

    private String getIpPort() {
        return "http://dev.store.winsenseos.cn";
    }

    private void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "初始化http配置异常" + "\n" + e;
            return;
            //throw new RuntimeException("初始化http配置异常", e);
        }
        //String authorization = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ5dWV4aXUiLCJleHAiOjE1NzE0NzM1OTh9.QYK9oGRG48kdwzYlYgZIeF7H2svr3xgYDV8ghBtC-YUnLzfFpP_sDI39D2_00wiVONSelVd5qQrjtsXNxRUQ_A";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", getShopId().toString())
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private String httpPostWithCheckCode(String path, String json, String... checkColumnNames) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        checkCode(response, StatusCode.SUCCESS, "");

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private String httpPostUrl(String path, String json) throws Exception {
        initHttpConfig();
        config.url(path).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            message += resJo.getString("message");

            if (expect != code) {
                throw new Exception(message + " expect code: " + expect + ",actual: " + code);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }


    private Object getShopId() {
        return "4116";
    }



    private static final String STAFF_LIST = "/risk/staff/page";
    private static final String STAFF_TYPE_LIST = "/risk/staff/type/list";


    private static String STAFF_TYPE_LIST_JSON = "{\"shop_id\":${shopId}}";

    private static String STAFF_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String STAFF_LIST_WITH_TYPE_JSON = "{\"shop_idaddStaffTestPage\":${shopId},\"staff_type\":\"${staffType}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";


//    ----------------------------------------------接口方法--------------------------------------------------------------------

    /**
     * 订单详情
     */
    public JSONObject orderDetail(String orderId) throws Exception {
        String json =
                "{" +
                        "   \"shop_id\" : " + getShopId() + ",\n" +
                        "\"order_id\":" + orderId +
                        "}";
        String url = "/risk/order/detail";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 订单关键步骤接口
     */
    public JSONObject orderLinkList(String orderId) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"order_id\":\"" + orderId + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 订单关键步骤接口分页
     */
    public JSONObject orderLinkListPage(String orderId, int page, int pageSize) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"order_id\":\"" + orderId + "\",\n" +
                        "    \"page\":" + page + ",\n" +
                        "    \"size\":" + pageSize + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 订单列表
     */
    public JSONObject orderList(int status, String namePhone, int page, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":" + page + ",\n";
        if (status != -1) {
            json += "    \"status\":\"" + status + "\",\n";
        }

        if (!"".equals(namePhone)) {
            json += "    \"customer_name\":\"" + namePhone + "\",\n";
        }

        json += "    \"size\":" + pageSize + "\n" +
                "}";
        String[] checkColumnNames = {};
        String res = httpPostWithCheckCode(url, json, checkColumnNames);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*订单列表根据审核过进行筛选*/
    public JSONObject orderList_audited(int page, int pageSize, String is_audited) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":" + page + ",\n"+
                        "   \"is_audited\":\"" + is_audited + "\",\n"+
                        "    \"size\":" + pageSize + "\n" +
                "}";
        String[] checkColumnNames = {};
        String res = httpPostWithCheckCode(url, json, checkColumnNames);

        return JSON.parseObject(res).getJSONObject("data");
    }



    /**
     * 无状态的订单列表
     */
    public JSONObject orderListt(int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":1" + ",\n" +
                        "    \"size\":" + pageSize + "\n" +
                        "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 顾客列表
     */
    public JSONObject customerList(int page, int pageSize, String phone_or_name) throws Exception {
        String url = "/risk/customer/list";
        String json =
                "{\n" +
                        "    \"page\":" + page + ",\n" +
                        "    \"size\":" + pageSize + ",\n" ;

        if (phone_or_name.equals("")){
        }
        else {
            json = json + "    \"phone_or_name\":" + phone_or_name + ",\n";

        }
        json = json + "    \"shop_id\":" + getShopId() + "\n" +
                "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 渠道列表
     */
    public JSONObject channelList(int page, int pageSize) throws Exception {
        String url = "/risk/channel/page";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":" + page + ",\n" +
                        "    \"size\":" + pageSize + "\n"+
                        "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 渠道业务员列表
     */
    public JSONObject channelStaffList(String channelId, String namePhone, int page, int pageSize) throws Exception {
        String url = "/risk/channel/staff/page";

        String json =
                "{\n";
        if (!"".equals(namePhone)) {
            json += "    \"name_phone\":\"" + namePhone + "\",";

        }

        json +=
                "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"channel_id\":\"" + channelId + "\",\n" +
                        "    \"page\":" + page + ",\n" +
                        "    \"size\":" + pageSize +
                        "}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 人脸轨迹搜索
     */
    public JSONObject faceTraces(String showUrl) throws Exception {
        String url = "/risk/evidence/face/traces";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"show_url\":\"" + showUrl + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 历史信息数据(2020.02.12)
     */
    public JSONObject historyRuleDetail() throws Exception {
        String url = "/risk/history/rule/detail";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }





    /**
     * 人物抓拍列表(2020-02-12)
     */
    public JSONObject personCatch(int page, int pageSize,String person_type, String device_id, String start_time, String end_time) throws Exception {
        String url = "/risk/evidence/person-catch/page";
        String json = "{\n" ;
        if (!"".equals(person_type)){
            json = json + "   \"person_type\":\"" + person_type + "\",\n" ;
        }
        if (!"".equals(device_id)){
            json = json + "   \"device_id\":" + device_id + ",\n" ;
        }
        if (!"".equals(start_time)){
            json = json + "   \"start_time\":" + start_time + ",\n" ;
        }
        if (!"".equals(end_time)){
            json = json + "   \"end_time\":" + end_time + ",\n" ;
        }
         json = json+
                 "   \"page\":" + page + ",\n" +
                "   \"size\":" + pageSize + ",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 订单数据趋势(2020.02.12) 框架 要改
     */
    public JSONObject  historyOrderTrend(String trendtype, String start,String end) throws Exception {
        String url = "/risk/history/rule/order/trend";
        String json = "{\n" +
                "   \"trend_type\":\"" + trendtype + "\",\n" +
                "   \"start_day\":\"" + start + "\",\n" +
                "   \"end_day\":\"" + end + "\",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 门店实时客流统计
     */
    public JSONObject realTime() throws Exception {
        String url = "/risk/real-time/shop";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 门店实时客流身份统计
     */
    public JSONObject realCustomerType() throws Exception {
        String url = "/risk/real-time/customer-type/distribution";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 今日全场累计客流
     */
    public JSONObject realPersonAccumulate() throws Exception {
        String url = "/risk/real-time/persons/accumulated";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 门店历史客流统计
     */
    public JSONObject historyShop(String startTime) throws Exception {
        String url = "/risk/history/shop";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"cycle\":\"" + "WEEK" + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 历史客流身份分布
     */
    public JSONObject historyCustomerType(String startTime) throws Exception {
        String url = "/risk/history/customer-type/distribution";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"cycle\":\"" + "WEEK" + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 历史互动人数趋势
     */
    public JSONObject historypersonAccumulate(String startTime) throws Exception {
        String url = "/risk/history/persons/accumulated";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"cycle\":\"" + "WEEK" + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }




    /**
     * 渠道业务员详情H5
     */
    public String staffDetailH5(String token) throws Exception {
        String url = "http://dev.store.winsenseos.cn/external/channel/staff/detail";
        String json =
                "{\n" +
                        "    \"token\":\"" + token + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}\n";

        String response = httpPostUrl(url, json);

        return response;
    }

    /**
     * 渠道客户报备H5
     */
    public String customerReportH5(String staffId, String customerName, String phone, String gender, String token) throws Exception {
        String url = "/external/channel/customer/report";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + staffId + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"customer_phone\":\"" + phone + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"token\":\"" + token + "\"" +
                        "}\n";

        String response = httpPostWithCheckCode(url, json);

        return response;
    }

    /**
     * 获取客户报备列表H5
     */
    public String channelCustomerListH5(String token, int page, int pageSize) throws Exception {
        String url = "http://dev.store.winsenseos.cn/external/channel/customer/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"token\":\"" + token + "\"," +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + pageSize + "\"" +
                        "}\n";

        String response = httpPostUrl(url, json);

        return response;
    }

    /**
     * 员工身份列表
     */
    public JSONArray staffTypeList() throws Exception {
        String json = StrSubstitutor.replace(STAFF_TYPE_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .build()
        );

        String res = httpPostWithCheckCode(STAFF_TYPE_LIST, json);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    /**
     * 员工列表
     */
    public JSONArray staffList(int page, int pageSize) throws Exception {
        return staffListReturnData(page, pageSize).getJSONArray("list");
    }

    public JSONObject staffListReturnData(int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(STAFF_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );

        String res = httpPostWithCheckCode(STAFF_LIST, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONArray staffListWithType(String staffType, int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(STAFF_LIST_WITH_TYPE_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("staffType", staffType)
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );
        String url = "/risk/staff/page";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }


//-------------------------------------------------------------用例用到的方法--------------------------------------------------------------------

    private long getTimebeforetoday() throws ParseException {//今天的00：00：00
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");//设置日期格式,今天的0点之前
        String datenow = df.format(new Date());// new Date()为获取当前系统时间，2020-02-18 00:00:00
        Date date = df.parse(datenow);
        long ts = date.getTime(); //转换为时间戳1581955200000
        System.out.println(ts);
        return ts;
    }

    private long getTimebeforeyseterday() throws ParseException { //昨天的00：00：00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - 1);
        Date d = c.getTime();
        String day = format.format(d);
        long yesterdray = Long.parseLong(day);
        return yesterdray;
    }

    private long getStartTime(int n) throws ParseException { //前第n天的开始时间（当天的0点）
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - n);
        Date d = c.getTime();
        String day = format.format(d);
        long starttime = Long.parseLong(day);
        return starttime;
    }

    private long getEndTime(int n) throws ParseException { //前第n天的结束时间(第二天的0点)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - n+1);
        Date d = c.getTime();
        String day = format.format(d);
        long endtime = Long.parseLong(day);
        return endtime;
    }


    private void checkOrderListEqualsLinkList(JSONArray list) throws Exception {

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String name = single.getString("customer_name");
            String phone = single.getString("customer_phone");
            String orderId = single.getString("order_id");
            String adviserName = single.getString("adviser_name"); //置业顾问
            String channelName = single.getString("channel_name"); //成交渠道
            String firstappearTime = single.getString("first_appear_time"); //首次到访时间
            String dealTime = single.getString("deal_time"); //刷证时间
            JSONArray orderLinkList = orderLinkList(orderId).getJSONArray("list");
            for (int r = 0 ; r < orderLinkList.size() ; r++) {
                JSONObject link = orderLinkList.getJSONObject(r);
                String linkPoint = link.getString("link_point");
                String linkName = link.getString("link_name");

                if (linkPoint.contains("置业顾问")) {
                    String content = link.getJSONObject("link_note").getString("content");
                    System.out.println(content);
                    String adviserNameLink = content.substring(content.indexOf("为") + 1);
                    System.out.println(adviserNameLink);

                    if (!adviserName.equals(adviserNameLink)) {
                        throw new Exception("订单编号" + orderId + " 风控列表页，置业顾问是：" + adviserName + ",风控单页，置业顾问是：" + adviserNameLink+ "，与预期结果不符");
                    }
                    break;
                }
                if (channelName != null && !"".equals(channelName)) {
                    if ("渠道报备".equals(linkName)) {
                        String content = link.getJSONObject("link_note").getString("content");
                        String channleNameLink = content.substring(0, content.indexOf("-"));
                        if (!channleNameLink.equals(channelName)) {
                            throw new Exception("订单编号" + orderId + "风控列表页，成交渠道是：" + channelName + "，风控单页，成交渠道是：" + channleNameLink+ "，与预期结果不符");
                        }
                    }
                }


                if ("首次到访".equals(linkName)) {
                    String apperTimeLink = link.getString("link_time");
                    if (!apperTimeLink.equals(firstappearTime)) {
                        throw new Exception("订单编号" + orderId + "风控列表页，首次到访时间为：" + firstappearTime + "，风控单页，首次到访时间为：" + apperTimeLink+ "，与预期结果不符");
                    }
                }



            }
            for (int m = orderLinkList.size(); m > 0; m--){ //多次刷证取最开始一次
                JSONObject link = orderLinkList.getJSONObject(m);
                String linkPoint = link.getString("link_point");
                String linkName = link.getString("link_name");
                if ("正常:人证⽐对通过".equals(linkPoint)) {
                    String dealTimeLink = link.getString("link_time");
                    if (!dealTimeLink.equals(dealTime)) {
                        throw new Exception("订单编号" + orderId + "风控列表页，刷证时间为：" + dealTime + "，风控单页，刷证时间为：" + dealTimeLink+ "，与预期结果不符");
                    }
                    break;
                }

            }
        }
    }

    private void checkOrderListFilter() throws Exception {
        String normal_list = orderList(1, "", 1, pageSize).getString("total");
        System.out.println(normal_list);
        String risk_list = orderList(3, "", 1, pageSize).getString("total");
        System.out.println(risk_list);
        String unknown_list = orderList(2, "", 1, pageSize).getString("total");
        System.out.println(unknown_list);
        int total = Integer.parseInt(normal_list) + Integer.parseInt(risk_list) + Integer.parseInt(unknown_list);
        if (Integer.parseInt(normal_list) > total) {
            throw new Exception("总单数" + total + " < 正常单单数" + normal_list+ " ，与预期结果不符");
        }
        if (Integer.parseInt(risk_list) > total) {
            throw new Exception("总单数" + total + " < 风险单单数" + risk_list+ " ，与预期结果不符");
        }
        if (Integer.parseInt(unknown_list) > total) {
            throw new Exception("总单数" + total + " < 未知单单数" + unknown_list+ " ，与预期结果不符");
        }
    }

    private void checkRank(JSONArray list, String key, String function) throws Exception {
        for (int i = 0; i < list.size() - 1; i++) {
            JSONObject singleB = list.getJSONObject(i);
            long gmtCreateB = singleB.getLongValue("gmt_create");
            JSONObject singleA = list.getJSONObject(i + 1);
            long gmtCreateA = singleA.getLongValue("gmt_create");

            if (gmtCreateB < gmtCreateA) {
                String phoneB = singleB.getString(key);
                String phoneA = singleA.getString(key);

                throw new Exception(function + "没有按照创建时间倒排！前一条,phone:【" + phoneB + ",gmt_create【" + gmtCreateB +
                        "】，后一条phone【" + phoneA + ",gmt_create【" + gmtCreateA + "】"+ " ，与预期结果不符");
            }
        }
    }

    public void compareOrderTimeValue(JSONObject data, String key, String orderId, String valueExpect, String function1, String function2) throws Exception {
        String valueStr = data.getString(key);
        if (valueStr != null && !"".equals(valueStr)) {
            String firstStr = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", Long.valueOf(valueStr));
            if (!firstStr.equals(valueExpect)) {
                throw new Exception("订单id：" + orderId + ",【" + key + "】在" + function1 + "中是：" + valueExpect + ",在" + function2 + "中是：" + firstStr+ "，与预期结果不符");
            }
        }
    }

    public void compareValue(JSONObject data, String function, String cid, String key, String valueExpect, String comment) throws Exception {

        String value = getValue(data, key);

        if (!valueExpect.equals(value)) {
            throw new Exception(function + "id：" + cid + ",列表中" + comment + "：" + valueExpect + ",详情中：" + value+ "，与预期结果不符");
        }
    }

    public String getValue(JSONObject data, String key) {
        String value = data.getString(key);

        if (value == null || "".equals(value)) {
            value = "";
        }

        return value;
    }

    public String getOneStaffType() throws Exception {
        JSONArray staffTypeList = staffTypeList();
        Random random = new Random();
        return staffTypeList.getJSONObject(random.nextInt(3)).getString("staff_type");
    }

    public String genPhoneStar() {
        Random random = new Random();
        String num = "177****" + (random.nextInt(8999) + 1000);

        return num;
    }

    private void checkChannelEqualsStaff(JSONArray list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String channelName = single.getString("channel_name");
            if (channelName.contains("channel-")) {
                break;
            }
            int reportNum = single.getInteger("total_customers");
            String channelId = single.getString("channel_id");
            int totalstaff = Integer.parseInt(channelStaffList(channelId, "", 1, pageSize).getString("total"));
            int total = 0;
            if (totalstaff > 50) {
                int a = (int) Math.ceil(totalstaff / 50) + 1;
                for (int j = 1; j <= a; j++) {
                    JSONArray staffList = channelStaffList(channelId, "", j, pageSize).getJSONArray("list");
                    for (int k = 0; k < totalstaff; k++) {
                        JSONObject singleStaff = staffList.getJSONObject(k);
                        total += singleStaff.getInteger("total_report");
                    }
                }
            } else {
                JSONArray staffList = channelStaffList(channelId, "", 1, pageSize).getJSONArray("list");
                for (int k = 0; k < totalstaff; k++) {
                    JSONObject singleStaff = staffList.getJSONObject(k);
                    total += singleStaff.getInteger("total_report");
                }
            }


            if (reportNum != total) {
                throw new Exception("渠道：" + channelName + "，渠道报备数=" + reportNum + "，业务员总报备数=" + total+ "，与预期结果不符");
            }
        }
    }




    private int getnum(int status) throws Exception { //截至昨天24点的数量。
        int total = Integer.parseInt(orderList(status,"",1,10).getString("total"));//1正常 2未知 3风险
        int todaynum = 0; //未知订单总数-今天订单=截止昨天24点前订单页的未知订单数量
        if (total > 50){
            int a = (int) Math.ceil(total / 50) + 1;
            for (int i = 1; i <=a; i++){
                JSONArray list = orderList(status, "", i, pageSize).getJSONArray("list");
                for (int j = 0; j< list.size();j++){
                    JSONObject single = list.getJSONObject(j);
                    long ordertime =Long.parseLong(single.getString("deal_time"));
                    if (ordertime > getTimebeforetoday()){
                        todaynum = todaynum + 1;
                    }
                }
            }
        }
        else{
            JSONArray list = orderList(status, "", 1, pageSize).getJSONArray("list");
            for (int j = 0; j< list.size();j++){
                JSONObject single = list.getJSONObject(j);
                long ordertime =Long.parseLong(single.getString("deal_time"));
                if (ordertime > getTimebeforetoday()){
                    todaynum = todaynum + 1;
                }
            }
        }
        int til24num = total - todaynum;
        return til24num;
    }

    private int getTimeNum(int status,long day) throws Exception { //某一天的数量。status为订单状态，day为某一天0点的时间戳
        int total = Integer.parseInt(orderList(status,"",1,10).getString("total"));//1正常 2未知 3风险
        int Timenum = 0; //未知订单总数-今天订单=截止昨天24点前订单页的未知订单数量
        if (total > 50){
            int a = (int) Math.ceil(total / 50) + 1;
            for (int i = 1; i <=a; i++){
                JSONArray list = orderList(status, "", i, pageSize).getJSONArray("list");
                for (int j = 0; j< list.size();j++){
                    JSONObject single = list.getJSONObject(j);
                    long ordertime =Long.parseLong(single.getString("deal_time"));
                    if (ordertime >= day && ordertime < (day + 86400000)){
                        Timenum = Timenum + 1;
                    }
                }
            }
        }
        else{
            JSONArray list = orderList(status, "", 1, pageSize).getJSONArray("list");
            for (int j = 0; j< list.size();j++){
                JSONObject single = list.getJSONObject(j);
                long ordertime =Long.parseLong(single.getString("deal_time"));
                if (ordertime >= day && ordertime < (day + 86400000)){
                    Timenum = Timenum + 1;
                }
            }
        }
        return Timenum;
    }

    private int getValidDays(JSONObject data) {
        int num = 0;

        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String presentCycle = single.getString("present_cycle");
            if (presentCycle != null && !"".equals(presentCycle)) {
                num++;
            }
        }

        return num;
    }

    private int[] getorder_num() throws Exception { //获取成交订单中的自然访客+渠道访客
        int orderlist_natual = 0;
        int orderlist_channel = 0;
        int [] o_num = new int[2];
        long beforetodaytime = getTimebeforetoday();
        //正常订单，风险订单中，顾客唯一。 获取正常+风险中的所有自然+渠道
        int normal_list_total = Integer.parseInt(orderList(1,"",1,10).getString("total")); //正常订单总单数
        int risk_list_total = Integer.parseInt(orderList(3,"",1,10).getString("total")); //风险订单总单数
        //int customerlist_total = Integer.parseInt(customerList(1,10,"").getString("total")); //登记顾客列表总单数
        JSONArray normal_list = orderList(1,"",1,normal_list_total).getJSONArray("list");//正常订单
        for (int i = 0; i < normal_list.size(); i++){//先对比正常订单，风险订单同理
            JSONObject single1 = normal_list.getJSONObject(i);
            if (Long.parseLong(single1.getString("deal_time")) < beforetodaytime){ //刷证时间在昨天的24：00之前
                if (single1.containsKey("channelId")){ //有渠道id的key,则渠道订单+1
                    orderlist_channel = orderlist_channel + 1;
                }
                else {
                    orderlist_natual = orderlist_natual + 1; //否则 自然访客+ 1
                }

            }
        }
        JSONArray risk_list = orderList(3,"",1,risk_list_total).getJSONArray("list");//风险订单
        for (int j = 0; j < risk_list.size(); j++){//再对比风险订单
            JSONObject single2 = risk_list.getJSONObject(j);
            if (Long.parseLong(single2.getString("deal_time")) < beforetodaytime){ //刷证时间在昨天的24：00之前
                if (single2.containsKey("channelId")){ //有渠道id的key,则渠道订单+1
                    orderlist_channel = orderlist_channel + 1;
                }
                else {
                    orderlist_natual = orderlist_natual + 1; //否则 自然访客+ 1
                }

            }
        }
        o_num[0] = orderlist_natual;
        o_num[1] = orderlist_channel;
        return o_num;
        //这里要返回两个int
    }
    private int[] getcustomer_num() throws Exception {
        int customer_channel = 0;
        int customer_natual = 0;
        int [] c_num = new int[2];
        int total = customerList(1,1,"").getInteger("total");
        JSONArray list = customerList(1,total,"").getJSONArray("list");
        int count = 0;
        int test = 0;
        for (int i = 0; i< list.size();i++){
            JSONObject single = list.getJSONObject(i);
            if (!single.getString("phone").contains("*")){
                count = count + 1;
            }
        }
        String [][] all_cstm = new String [total] [4];
        for (int i = 0; i< list.size();i++){
            JSONObject single = list.getJSONObject(i);
            all_cstm[i][0] = single.getString("customer_name");
            all_cstm[i][1] = single.getString("phone");
            all_cstm[i][2] = single.getString("gmt_create");
            all_cstm[i][3] = single.getString("channel_staff_Id");
        }
        for (int j = 0; j < total; j++){
            for (int k = 1; k < total; k++){
                if (all_cstm[j][0].equals(all_cstm[k][0]) && all_cstm[j][1].equals(all_cstm[k][1])){ //手机号姓名都匹配
                    if (Long.parseLong(all_cstm[j][2]) < Long.parseLong(all_cstm[k][2])){//就比较时间
                        if (all_cstm[k][3].equals("0")){ //channel_staff_Id=0是自然访客
                            customer_natual = customer_natual + 1;
                        }
                        else {
                            customer_channel = customer_channel + 1;
                        }
                    }
                    else {
                        if (all_cstm[j][3].equals("0")){ //channel_staff_Id=0是自然访客
                            customer_natual = customer_natual + 1;
                        }
                        else {
                            customer_channel = customer_channel + 1;
                        }
                    }
                    break;
                }
                else{ //手机号姓名不匹配，就看是渠道访客/自然访客
                    test = test + 1; //他和剩下的所有进行比较，如果比较了total-1次，那就是唯一的
                    continue;
                }
            }
            if (test == total -1){
                if (all_cstm[j][3].equals("0")){
                    customer_natual = customer_natual + 1;
                }
                else {
                    customer_channel = customer_channel + 1;
                }
            }
            test = 0;
        }
        c_num[0] = customer_natual;
        c_num[1] = customer_channel;
        return c_num;


    }

    private void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("飞单日常-数据一致性校验 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        msg = msg.replace("java.lang.Exception: ", "异常：");
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());


    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {"18513118484", //杨航
                    "15011479599", //谢志东
                    "15898182672"}; //华成裕
            alarmPush.alarmToRd(rd);
        }
    }

 //  public static void main(String[] args) throws ParseException { ---不用理我！
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");//设置日期格式,今天的0点之前
        //String datenow = df.format(new Date());// new Date()为获取当前系统时间，2020-02-18 00:00:00
        //Date date = df.parse(datenow);
        //long ts = date.getTime(); //转换为时间戳1581955200000
        //System.out.println(ts);

/**
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(Calendar.DATE, - 7);
        Date d = c.getTime();
        String day = format.format(d);
        System.out.println(day);


        SimpleDateFormat sdff=new SimpleDateFormat("yyyy-MM-dd");
        // 时间戳转换成时间
        long a = 1581868800000L ;
        String sd = sdff.format(new Date(a));
        System.out.println(sd);//打印出你要的时间


    }

    private int getValidDays(JSONObject data) {
        int num = 0;

        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String presentCycle = single.getString("present_cycle");
            if (presentCycle != null && !"".equals(presentCycle)) {
                num++;
            }
        }

        return num;
 */


      /**  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - 1);
        Date d = c.getTime();
        String day = format.format(d);
        System.out.println(day);
*/

 //   }



}




