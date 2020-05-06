package com.haisheng.framework.testng.bigScreen.feidanDaily;


import java.io.File;

import com.haisheng.framework.model.bean.ReportTime;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
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
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.ParseException;
import java.util.*;
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
     * H5页面业务员报备总数与H5页面内业务员报备条数一致
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
            System.out.println("staffDetailH5" + staffDetailH5);
            int reportNumH5 = JSON.parseObject(staffDetailH5).getJSONObject("data").getInteger("report_num");
            int reportNumListNum = 0;
            int reportNumListTotal = 0;
            int a = 0;
            if (reportNumH5 > 50) {
                if (reportNumH5 % 50 == 0) {
                    a = reportNumH5 / 50;
                } else {
                    a = (int) Math.ceil(reportNumH5 / 50) + 1;
                }
                System.out.println(a);
                for (int i = 1; i <= a; i++) {
                    String customerListH5 = channelCustomerListH5(token, i, 50);
                    System.out.println(customerListH5);
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
                throw new Exception("业务员手机号:" + staffPhone + "， H5页面内列表中的报备条数=" + reportNumListTotal + ", H5页面内的显示的报备条数=" + reportNumListNum + "，与预期结果不符");
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
     * H5页面报备总数与PC页面内报备总数一致
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
            int a = 0;
            if (totalnum > 50) {
                if (totalnum % 50 == 0) {
                    a = totalnum / 50;
                } else {
                    a = (int) Math.ceil(totalnum / 50) + 1;
                }
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
                throw new Exception("业务员手机号:" + staffPhone + ", H5页面内的报备数=" + reportNumH5 + ", PC端的报备数=" + reportNumPC + "，与预期结果不符");
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
     * 渠道的累计报备数==各个业务员的累计报备数之和
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
                int a = 0;
                if (totalnum > 50) {
                    if (totalnum % 50 == 0) {
                        a = totalnum / 50;
                    } else {
                        a = (int) Math.ceil(totalnum / 50) + 1;
                    }
                    for (int j = 1; j <= a; j++) {
                        JSONArray staffList = channelStaffList(channelId, "", j, pageSize).getJSONArray("list");
                        System.out.println(staffList);
                        for (int k = 0; k < staffList.size(); k++) {
                            JSONObject singleStaff = staffList.getJSONObject(k);
                            System.out.print(singleStaff.getInteger("total_report") + "  ");
                            staffNum += singleStaff.getInteger("total_report");

                        }
                        System.out.println(" ");
                    }
                } else {
                    JSONArray staffList = channelStaffList(channelId, "", 1, pageSize).getJSONArray("list");
                    for (int k = 0; k < staffList.size(); k++) {
                        JSONObject singleStaff = staffList.getJSONObject(k);
                        staffNum += singleStaff.getInteger("total_report");
                    }
                }


                if (staffNum != channelNum) {
                    throw new Exception("渠道-" + channelName + " ,渠道累计报备数=" + channelNum + " ，业务员累计报备数之和=" + staffNum + " ，与预期结果不符");
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
    @Test
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
                    throw new Exception("渠道【" + channelName + "】, 渠道列表中的报备数：" + channelReportNum + "，与预期结果不符");
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
     * 订单列表中，风险+正常+未知的订单数==订单列表总数
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
                throw new Exception("订单列表总数" + totalNum + " != 正常订单数" + normal_totalnum + " + 未知订单数" + unknown_totalnum + " + 异常订单数" + risk_totalnum + " ，与预期结果不符");
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
     * V3.0截至目前-未知订单=目前订单页的未知订单数量
     **/
    @Test
    public void FKdata_unknownOrder() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            int unknownnum = orderList(2, "", 1, 1).getInteger("total"); //订单列表的未知订单数量
            int unknow_order = historyRuleDetail().getInteger("unknow_order"); //风控数据中未知订单数量
            if (unknownnum != unknow_order) {
                throw new Exception("截至目前，订单列表中未知订单数量=" + unknownnum + " ，风控数据页未知订单数量=" + unknow_order + " ，与预期结果不符");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页未知订单数量与订单页的未知订单数量一致\n");
        }
    }


    /**
     * V3.0截至目前-正常订单=订单页的正常订单数量
     **/
    @Test
    public void FKdata_normalOrder() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            int normalnum = orderList(1, "", 1, 1).getInteger("total");//订单列表中的正常订单数量
            int normal_order = historyRuleDetail().getInteger("normal_order"); //风控数据中正常订单数量
            if (normalnum != normal_order) {
                throw new Exception("截至目前，订单列表中正常订单数量=" + normalnum + " ，风控数据页正常订单数量=" + normal_order + " ，与预期结果不符");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页正常订单数量与订单页的正常订单数量一致\n");
        }
    }

    /**
     * V3.0截至目前-风险订单=订单页的风险订单数量
     **/
    @Test
    public void FKdata_riskOrder() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            int til24risknnum = orderList(3, "", 1, 1).getInteger("total"); //订单列表中的风险订单数量
            int risk_order = Integer.parseInt(historyRuleDetail().getString("risk_order")); //风控数据中风险订单数量
            if (til24risknnum != risk_order) {
                throw new Exception("截至目前，订单列表中风险订单数量=" + til24risknnum + " ，风控数据页风险订单数量=" + risk_order + " ，与预期结果不符");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页风险订单数量与订单页的风险订单数量一致\n");
        }
    }

    /**
     * V3.0订单趋势-全部=正常+异常+未知
     **/
    @Test
    public void FKdata_Trendallorder() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String starttime = getStartTime(30);
            String endtime = getStartTime(1);
            JSONArray list = historyOrderTrend(starttime, endtime).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                int all = single.getInteger("all_order");
                int normal = single.getInteger("normal_order");
                int risk = single.getInteger("risk_order");
                int unknown = single.getInteger("unknow_order");
                String day = single.getString("day");
                if (all != normal + risk + unknown) {
                    throw new Exception("风控数据页订单趋势" + day + "全部订单数" + all + " != 正常订单数" + normal + " + 风险订单数" + risk + " + 未知订单数" + unknown + "\n");
                }
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页订单趋势-全部订单=正常+风险+未知\n");
        }
    }


    /**
     * V3.0访客趋势-全部=自然 + 渠道
     **/
    @Test
    public void FKdata_Trendallfangke() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String starttime = getStartTime(30);
            String endtime = getStartTime(1);
            JSONArray list = historycustomerTrend(starttime, endtime).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                int all = single.getInteger("all_visitor");
                int channel = single.getInteger("channel_visitor");
                int natural = single.getInteger("natural_visitor");
                String day = single.getString("day");
                if (all != channel + natural) {
                    throw new Exception("风控数据页访客趋势" + day + "全部访客数" + all + " != 渠道访客数" + channel + " + 自然访客数" + natural + "\n");
                }
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页访客趋势-全部访客=渠道+自然\n");
        }
    }

    /**
     * V3.0截至目前--自然顾客+渠道顾客>=未知订单+正常订单+风险订单
     **/
    @Test
    public void FKdata_fangkeGEorder() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            JSONObject historyRuleDetail = historyRuleDetail();
            int natual = historyRuleDetail.getInteger("natural_visitor");
            int channel = historyRuleDetail.getInteger("channel_visitor");
            int unknownorder = historyRuleDetail.getInteger("unknow_order");
            int normalorder = historyRuleDetail.getInteger("normal_order");
            int riskorder = historyRuleDetail.getInteger("risk_order");
            int fangke = natual + channel;
            int order = unknownorder + normalorder + riskorder;
            if (fangke < order) {
                throw new Exception("风控数据页，自然访客+渠道访客=" + fangke + " ，未知订单+正常订单+风险订单=" + order + " ，与预期不符");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页顾客数量大于等于订单数量\n");
        }
    }

    /**
     * V3.0截至昨天-未知订单+正常订单+风险订单 >= 订单趋势中每天数据总和（2月份开始  要改！！！！
     **/
    //@Test
    public void FKdata_orderEQtrend() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1; //获取当前月份
        System.out.println(month);

        try {
            int unknownorder = historyRuleDetail().getInteger("unknow_order");
            int normalorder = historyRuleDetail().getInteger("normal_order");
            int riskorder = historyRuleDetail().getInteger("risk_order");
            int order = unknownorder + normalorder + riskorder;
            int trendorder = 0;
            String starttime = "2020-02-01";
            String endtime = "2020-02-30";
            JSONArray list = historyOrderTrend(starttime, endtime).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                trendorder = trendorder + single.getInteger("all_order");
            }
            String a = String.format("%02d", month);
            System.out.println(a);
            if (month > 2) {
                starttime = "2020-" + a + "-01";
                endtime = "2020-" + a + "-31";
                JSONArray list2 = historyOrderTrend(starttime, endtime).getJSONArray("list");
                for (int i = 0; i < list2.size(); i++) {
                    JSONObject single = list2.getJSONObject(i);
                    trendorder = trendorder + single.getInteger("all_order");
                }
                month = month - 1;
            }

            if (trendorder > order) {
                throw new Exception("风控数据页面截至目前，未知+正常+风险订单数量" + order + " < 订单趋势中，二月份以来全部订单数量" + trendorder + " ，与预期不符");
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页面截止昨天的未知+正常+风险订单数量 >= 订单趋势中每天数据总和（2月份开始）\n");
        }
    }

    /**
     * V3.0截至目前-自然登记人数 >= 访客趋势中每天自然登记人数总和（1月份开始）
     **/
    @Test
    public void FKdata_naturalEQtrend() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1; //获取当前月份
        //System.out.println(month);

        try {
            int natual = historyRuleDetail().getInteger("natural_visitor");

            int trendcustomer = 0;
            String starttime = "2020-01-01";
            String endtime = "2020-01-31";
            JSONArray list = historycustomerTrend(starttime, endtime).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                trendcustomer = trendcustomer + single.getInteger("natural_visitor");
            }

            while (month > 1) {
                String a = String.format("%02d", month);
                System.out.println(a);
                starttime = "2020-" + a + "-01";
                endtime = "2020-" + a + "-31";
                JSONArray list2 = historycustomerTrend(starttime, endtime).getJSONArray("list");
                for (int i = 0; i < list2.size(); i++) {
                    JSONObject single = list2.getJSONObject(i);
                    trendcustomer = trendcustomer + single.getInteger("natural_visitor");
                }
                month = month - 1;
            }

            if (trendcustomer > natual) {
                throw new Exception("风控数据页面截至目前，自然登记人数=" + natual + "  < 访客趋势中，一月份以来全部自然登记人数" + trendcustomer + " ，与预期不符");
            }


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：截至目前-自然登记人数 >= 访客趋势中每天自然登记人数总和（1月份开始）\n");
        }
    }


    /**
     * V3.0截至目前-渠道报备人数 >= 访客趋势中每天渠道报备人数总和（1月份开始）
     **/
    @Test
    public void FKdata_channelEQtrend() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1; //获取当前月份
        //System.out.println(month);

        try {
            int channel = historyRuleDetail().getInteger("channel_visitor");

            int trendcustomer = 0;
            String starttime = "2020-01-01";
            String endtime = "2020-01-31";
            JSONArray list = historycustomerTrend(starttime, endtime).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                trendcustomer = trendcustomer + single.getInteger("channel_visitor");
            }

            while (month > 1) {
                String a = String.format("%02d", month);
                System.out.println(a);
                starttime = "2020-" + a + "-01";
                endtime = "2020-" + a + "-31";
                JSONArray list2 = historycustomerTrend(starttime, endtime).getJSONArray("list");
                for (int i = 0; i < list2.size(); i++) {
                    JSONObject single = list2.getJSONObject(i);
                    trendcustomer = trendcustomer + single.getInteger("channel_visitor");
                }
                month = month - 1;
            }

            if (trendcustomer > channel) {
                throw new Exception("风控数据页面截至目前，渠道报备人数=" + channel + "  < 访客趋势中，一月份以来全部渠道报备人数" + trendcustomer + " ，与预期不符");
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：截至目前-渠道报备人数 >= 访客趋势中每天渠道报备人数总和（1月份开始）\n");
        }
    }


    /**
     * 自然登记人数+渠道报备人数>=登记顾客数量+正常单数量+风险单数量
     **/
    //@Test
    public void FKdata_peopleGTorder() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        Calendar cal = Calendar.getInstance();

        try {
            int channel = historyRuleDetail().getInteger("channel_visitor"); //渠道报备人数
            int natual = historyRuleDetail().getInteger("natural_visitor"); //自然登记人数
            int customer = customerList(1, 1, "").getInteger("total");//登记顾客数量
            int normal = orderList(1, "", 1, 1).getInteger("total");//正常单数量
            int risk = orderList(3, "", 1, 1).getInteger("total");//风险单数量
            int people = channel + natual;
            int order = customer + normal + risk;
            Preconditions.checkArgument(people >= order, "自然登记人数+渠道报备人数" + people + " < 登记顾客数量+正常单数量+风险单数量" + order + "\n");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：自然登记人数+渠道报备人数>=登记顾客数量+正常单数量+风险单数量\n");
        }
    }


    /**
     * 自然登记人数>=登记顾客中自然登记人数
     **/
    @Test
    public void FKdata_natural() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        Calendar cal = Calendar.getInstance();

        try {
            int natrual = historyRuleDetail().getInteger("natural_visitor"); //自然登记人数
            int naturalcustomer = customerList2("", "0", "", 1, 1).getInteger("total");//登记顾客自然登记数量

            Preconditions.checkArgument(natrual >= naturalcustomer, "自然登记人数" + natrual + " < 登记信息中自然登记顾客" + naturalcustomer + "\n");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页自然登记人数>=登记信息中自然登记的数量\n");
        }
    }


    /**
     * V3.0订单趋势-订单数量=某n天订单页的订单数量
     * 跟刷证时间无关
     * 一个订单在3-5创建为未知订单的话  那么3-5会统计一个未知订单在里面  3-6变为正常订单的话  就会在3-6的正常订单数+1
     * 所以  这里面每天的订单总数 和 订单列表里面不存在相等关系的
     * 注释掉 趋势图不是实时改变的
     **/
    //@Test
    public void FKdata_riskOrderTrend() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String starttime = getStartTime(7);
            String endtime = getStartTime(1);
            JSONArray list = historyOrderTrend(starttime, endtime).getJSONArray("list"); //订单趋势中风险订单数量
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                String day = single.getString("day");
                int list_risknum = getTimeNum(3, day);//该天订单列表中风险订单数
                int list_normalnum = getTimeNum(1, day);//该天订单列表中正常订单数
                int list_unknownnum = getTimeNum(2, day);//该天订单列表中未知订单数
                int list_all = list_risknum + list_normalnum + list_unknownnum; //该天订单列表中全部订单数

                int risk_order = single.getInteger("risk_order");
                int unknow_order = single.getInteger("unknow_order");
                int normal_order = single.getInteger("normal_order");
                int all_order = single.getInteger("all_order");
                System.out.println(day);
                System.out.println("趋势风险" + risk_order);
                System.out.println("列表风险" + list_risknum);
                System.out.println("趋势未知" + unknow_order);
                System.out.println("列表未知" + list_unknownnum);
                System.out.println("趋势正常" + normal_order);
                System.out.println("列表正常" + list_normalnum);
                System.out.println("趋势全部" + all_order);
                System.out.println("列表全部" + list_all);
                Preconditions.checkArgument(all_order >= list_all, day + "当天订单列表中全部订单数" + list_all + "应 <= 风控数据页面订单趋势中，当天的全部订单数" + all_order + "\n");
                Preconditions.checkArgument(risk_order >= list_risknum, day + "当天订单列表中全部风险订单数" + list_risknum + "应 <= 风控数据页面订单趋势中，当天的风险订单数" + risk_order + "\n");
                Preconditions.checkArgument(normal_order >= list_normalnum, day + "当天订单列表中全部正常订单数" + list_normalnum + "应 <= 风控数据页面订单趋势中，当天的正常订单数" + normal_order + "\n");
                Preconditions.checkArgument(unknow_order <= list_unknownnum, day + "当天订单列表中全部未知订单数" + list_unknownnum + "应 <= 风控数据页面订单趋势中，当天的未知订单数" + unknow_order + "\n");


            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页订单趋势与订单页的订单一致\n");
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
            int risklinknunm = 0; //各订单异常环节总数
            int risk_total = orderList(-1, "", 1, 10).getInteger("total");
            int a = 0;
            if (risk_total > 50) {
                if (risk_total % 50 == 0) {
                    a = risk_total / 50;
                } else {
                    a = (int) Math.ceil(risk_total / 50) + 1;
                }
                for (int i = 1; i <= a; i++) {
                    JSONArray list = orderList(-1, "", i, pageSize).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        //System.out.println(single.getString("customer_name") + " phone " + single.getString("customer_phone") + "  risklin=" + single.getString("risk_link"));
                        risklinknunm = risklinknunm + single.getInteger("risk_link");

                    }
                }
            } else {
                JSONArray list = orderList(-1, "", 1, pageSize).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    risklinknunm = risklinknunm + single.getInteger("risk_link");
                }
            }
            int historynum = historyRuleDetail().getInteger("abnormal_link"); //风控数据页异常环节数

            Preconditions.checkArgument(risklinknunm == historynum, "订单列表中，各订单异常环节总数=" + risklinknunm + "，风控数据页，异常环节数=" + historynum + ", 与预期不符");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：风控数据页异常环节数==订单总异常环节数\n");
        }
    }


//----------------- 客流三个页面 start ------------------

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
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                oldnew = oldnew + single.getInteger("num");
            }
            if (uv != oldnew) {
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
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                timeadd = timeadd + single.getInteger("present_cycle");
            }
            if (uv > timeadd) {
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
     * V3.1 今日实时客流身份新客 == 到访人物今天新客customer_id去重数
     */
    @Test
    public void newEQpersonCatch() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            int newNum = realCustomerType().getJSONArray("list").getJSONObject(0).getInteger("num"); //今日客流身份-新客数量
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int total = personCatch(1, 1, "NEW", today, today).getInteger("total");
            int a = 0;
            ArrayList obj = new ArrayList();
            int new_catchNum = 0;
            if (total > 50) {
                if (total % 50 == 0) {
                    a = total / 50;
                } else {
                    a = (int) Math.ceil(total / 50) + 1;
                }
                for (int i = 1; i <= a; i++) {
                    JSONArray list = personCatch(i, pageSize, "NEW", today, today).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        obj.add(single.getString("customer_id"));
                    }
                }
                unique(obj);
                new_catchNum = obj.size();
            }
            if (total > 0 && total <= 50) {
                JSONArray list = personCatch(1, pageSize, "NEW", today, today).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    obj.add(single.getString("customer_id"));
                }
                unique(obj);
                new_catchNum = obj.size();
            }
            Preconditions.checkArgument(newNum == new_catchNum, "今日实时-今日客流身份分布-新客=" + newNum + " != 到访人物页面今日新客去重后=" + new_catchNum + " , 与预期不符");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：今日实时客流新客数量与到访人物页今天的新客数去重后一致\n");
        }
    }


    /**
     * V3.1 今日实时客流身份老客 == 到访人物今天老客customer_id去重数
     */
    @Test
    public void oldEQpersonCatch() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            int oldNum = realCustomerType().getJSONArray("list").getJSONObject(1).getInteger("num"); //今日客流身份-老客数量
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int total = personCatch(1, 1, "OLD", today, today).getInteger("total");
            int a = 0;
            ArrayList obj = new ArrayList();
            int old_catchNum = 0;
            if (total > 50) {
                if (total % 50 == 0) {
                    a = total / 50;
                } else {
                    a = (int) Math.ceil(total / 50) + 1;
                }
                for (int i = 1; i <= a; i++) {
                    JSONArray list = personCatch(i, pageSize, "OLD", today, today).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        obj.add(single.getString("customer_id"));
                    }
                }
                unique(obj);
                old_catchNum = obj.size();
            }
            if (total > 0 && total <= 50) {
                JSONArray list = personCatch(1, pageSize, "OLD", today, today).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    obj.add(single.getString("customer_id"));
                }
                unique(obj);
                old_catchNum = obj.size();
            }
            Preconditions.checkArgument(oldNum == old_catchNum, "今日实时-今日客流身份分布-老客=" + oldNum + " != 到访人物页面今日老客去重后=" + old_catchNum + " , 与预期不符");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：今日实时客流老客数量与到访人物页今天的老客数去重后一致\n");
        }
    }


    /**
     * V3.1 今日实时客流身份疑似员工 == 到访人物今天疑似员工customer_id去重数
     */
    @Test
    public void suspectedEQpersonCatch() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            int suspectedNum = realCustomerType().getJSONArray("list").getJSONObject(2).getInteger("num"); //今日客流身份-疑似员工数量
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int total = personCatch(1, 1, "SUSPECTED_STAFF", today, today).getInteger("total");
            int a = 0;
            ArrayList obj = new ArrayList();
            int suspected_catchNum = 0;
            if (total > 50) {
                if (total % 50 == 0) {
                    a = total / 50;
                } else {
                    a = (int) Math.ceil(total / 50) + 1;
                }
                for (int i = 1; i <= a; i++) {
                    JSONArray list = personCatch(i, pageSize, "SUSPECTED_STAFF", today, today).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        obj.add(single.getString("customer_id"));
                    }
                }
                unique(obj);
                suspected_catchNum = obj.size();
            }
            if (total > 0 && total <= 50) {
                JSONArray list = personCatch(1, pageSize, "SUSPECTED_STAFF", today, today).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    obj.add(single.getString("customer_id"));
                }
                unique(obj);
                suspected_catchNum = obj.size();
            }
            Preconditions.checkArgument(suspectedNum == suspected_catchNum, "今日实时-今日客流身份分布-疑似员工=" + suspectedNum + " != 到访人物页面今日疑似员工去重后=" + suspected_catchNum + " , 与预期不符");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：今日实时客流疑似员工数量与到访人物页今天的疑似员工数去重后一致\n");
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
            for (int i = 0; i < validDays; i++) {
                JSONObject single = list.getJSONObject(i);
                timeadd = timeadd + single.getInteger("present_cycle");
            }
            if (uv > timeadd) {
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
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                oldnew = oldnew + single.getInteger("num");
            }
            if (uv != oldnew) {
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

    /**
     * V2.3 活动详情页面-活动客流会对比中各日期的数据与历史统计中的一致
     **/
    @Test
    public void activity() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            /*
            String activityId = activityList().getJSONArray("list").getJSONObject(0).getString("id");
            if (activityId != null) {
                activitydateEQhistory(activityId);
            }
             */
            activitydateEQhistory("81");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：活动详情页面-活动客流会对比中各日期的数据与历史统计中的一致\n");
        }
    }

    /**
     * V2.3 活动详情页面：三个时期的新老顾客之和分别小于等于客流对比趋势图每天之和

     @Test public void activityDetailEqualsContrast() throws Exception {

     String ciCaseName = new Object() {
     }.getClass().getEnclosingMethod().getName();

     String caseName = ciCaseName;

     String function = "三个时期的新老顾客之和分别小于等于客流对比趋势图每天之和\n";

     //String activityId = activityList().getJSONArray("list").getJSONObject(0).getString("id");

     try {
     //if (activityId != null) {
     String activityId ="80";//新
     JSONObject detailData = activityDetail(activityId);
     int int_detailContrastNew = 0;
     int int_detailContrastOld = 0;
     int int_detailThisNew = 0;
     int int_detailThisOld = 0;
     int int_detailInfluenceNew = 0;
     int int_detailInfluenceOld = 0;
     int int_ = 0;
     int int_ = 0;
     int int_ = 0;
     String detailContrastNew = detailData.getJSONObject("contrast_cycle").getString("new_num");//对比新
     if (!detailContrastNew.equals("-")){
     int_detailContrastNew = Integer.parseInt(detailContrastNew);
     }
     String detailContrastOld = detailData.getJSONObject("contrast_cycle").getString("old_num"); //对比老
     if (!detailContrastOld.equals("-")){
     int_detailContrastOld = Integer.parseInt(detailContrastOld);
     }

     String detailThisNew = detailData.getJSONObject("this_cycle").getString("new_num"); //活动中新
     if (!detailThisNew.equals("-")){
     int_detailThisNew = Integer.parseInt(detailThisNew);
     }
     String detailThisOld = detailData.getJSONObject("this_cycle").getString("old_num"); //活动中老
     if (!detailThisOld.equals("-")){
     int_detailThisOld = Integer.parseInt(detailThisOld);
     }
     String detailInfluenceNew = detailData.getJSONObject("influence_cycle").getString("new_num"); //后期新
     if (!detailInfluenceNew.equals("-")){
     int_detailInfluenceNew = Integer.parseInt(detailInfluenceNew);
     }
     String detailInfluenceOld = detailData.getJSONObject("influence_cycle").getString("old_num"); //后期老
     if (!detailInfluenceOld.equals("-")){
     int_detailInfluenceOld = Integer.parseInt(detailInfluenceOld);

     }

     JSONObject contrastData = activityContrast(activityId);

     int contrastCycleNum = getContrastPassFlowNum(contrastData, "contrast_cycle");
     int thisCycleNum = getContrastPassFlowNum(contrastData, "this_cycle");
     int influenceCycleNum = getContrastPassFlowNum(contrastData, "influence_cycle");

     contrastActivityNum(activityId, "对比时期", detailContrastNew, detailContrastOld, contrastCycleNum);
     contrastActivityNum(activityId, "活动期间", detailThisNew, detailThisOld, thisCycleNum);
     contrastActivityNum(activityId, "活动后期", detailInfluenceNew, detailInfluenceOld, influenceCycleNum);
     // }

     } catch (Exception e) {
     failReason += e.getMessage();
     aCase.setFailReason(failReason);

     } finally {
     saveData(aCase, ciCaseName, caseName, function);
     }
     }
     */

//----------------- 客流三个页面 start ------------------


    //---------------- 顾客数量一致性 start -------------------

    /**
     * c1
     * V3.0 PC端新建顾客 风控数据-截至目前-自然顾客 + 1
     * 修改该顾客姓名 风控数据-截至目前-自然顾客 不变
     * 修改该顾客手机号 风控数据-截至目前-自然顾客 不变
     **/
    @Test
    public void PCnew_naturaladdone() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：PC无渠道新建一顾客，再修改顾客的手机号/姓名\n";
        desc = desc + "\n期待：修改前：截至目前自然登记人数 +1" +
                "\n修改后：截至目前自然登记人数 +0\n ";
        try {
            int before_fknatural = historyRuleDetail().getInteger("natural_visitor"); //获取自然顾客数量
            System.out.println(before_fknatural);
            String name = "PC" + System.currentTimeMillis(); //PC新建顾客无渠道
            String customerPhone = "14422110002";
            PCF(name, customerPhone);

            Thread.sleep(3000);
            int after_fknatural = historyRuleDetail().getInteger("natural_visitor"); //获取自然顾客数量 应+1
            System.out.println(after_fknatural);
            int a = after_fknatural - before_fknatural;
            Preconditions.checkArgument(a == 1, "新建顾客后，截至目前自然登记人数增加了" + a + " , 与预期不符");

            String cid = customerList2(name, "", "", 1, 1).getJSONArray("list").getJSONObject(0).getString("cid");
            System.out.println(cid);
            String newname = name + "new";
            customerEditPC(cid, newname, "14422110002", "", "");
            Thread.sleep(2000);
            int fknatural2 = historyRuleDetail().getInteger("natural_visitor");//获取自然顾客数量 应不变
            Preconditions.checkArgument(after_fknatural == fknatural2, "修改顾客姓名后，截至目前自然登记人数增加了" + a + " , 与预期不符");

            customerEditPC(cid, newname, "14422110003", "", "");
            Thread.sleep(2000);
            int fknatural3 = historyRuleDetail().getInteger("natural_visitor");//获取自然顾客数量 应不变
            Preconditions.checkArgument(fknatural3 == fknatural2, "修改顾客手机号后，增加了" + a + " , 与预期不符");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }

    }

    /**
     * c2
     * V3.0 两个渠道报备同一个顾客 都是全号
     * 风控数据-截至目前-渠道顾客+1
     * 渠道管理-渠道报备统计-累计报备顾客数量 + 1
     * 渠道管理-渠道报备统计-累计报备信息数量 + 2
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 + 1
     * 渠道管理-渠道报备统计-今日新增报备信息数量 + 2
     * <p>
     * 修改其中一个人的手机号
     * 风控数据-截至目前-渠道顾客+1
     * 渠道管理-渠道报备统计-累计报备顾客数量 + 1
     * 渠道管理-渠道报备统计-累计报备信息数量 + 0
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 + 1
     * 渠道管理-渠道报备统计-今日新增报备信息数量 + 0
     **/
    @Test
    public void Twochannel_onecustomer() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：两个渠道全号报备同一顾客，再修改其中一条记录的手机号\n";
        desc = desc + "\n期待：修改前：截至目前渠道报备人数 +1 ; 累计报备顾客数量 +1 ; 累计报备信息数量 +2 ; 今日新增报备顾客数量 +1 ; 今日新增报备信息数量 +2 ;" +
                "\n修改后：截至目前渠道报备人数 +1 ; 累计报备顾客数量 +1 ; 累计报备信息数量 +0 ; 今日新增报备顾客数量 +1 ; 今日新增报备信息数量 +0 \n ";
        try {
            int before_fkchannel = historyRuleDetail().getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            int before_customer_total = channelReptstatistics().getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatistics().getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatistics().getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatistics().getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);
            String name = "2channel" + System.currentTimeMillis(); //2个渠道报备同一顾客
            String customerPhone = "14422110002";
            H5Lianjia(name, customerPhone);
            H5WuDong(name, customerPhone);
            Thread.sleep(2000);
            int after_fkchannel = historyRuleDetail().getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            int after_customer_total = channelReptstatistics().getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatistics().getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatistics().getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatistics().getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == 1, "修改前：截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == 1, "修改前：累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 2, "修改前：累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 2, "修改前：今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == 1, "修改前：今日新增报备顾客数量增加了" + customer_today + "\n");

            //修改其中一个的手机号
            String cid = customerList2(name, "1", "", 1, 1).getJSONArray("list").getJSONObject(0).getString("cid");
            System.out.println(cid);
            customerEditPC(cid, name, "14422110003", "", "");
            Thread.sleep(2000);
            int after_fkchannel1 = historyRuleDetail().getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            int after_customer_total1 = channelReptstatistics().getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total1 = channelReptstatistics().getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today1 = channelReptstatistics().getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today1 = channelReptstatistics().getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量

            int fkchannel1 = after_fkchannel1 - after_fkchannel;
            int customer_total1 = after_customer_total1 - after_customer_total;
            int customer_today1 = after_customer_today1 - after_customer_today;
            int record_total1 = after_record_total1 - after_record_total;
            int record_today1 = after_record_today1 - after_record_today;
            Preconditions.checkArgument(fkchannel1 == 1, "修改后：截至目前渠道报备人数增加了" + fkchannel1 + "\n");
            Preconditions.checkArgument(customer_total1 == 1, "修改后：累计报备顾客数量增加了" + customer_total1 + "\n");
            Preconditions.checkArgument(customer_today1 == 1, "修改后：今日新增报备顾客数量增加了" + customer_today1 + "\n");
            Preconditions.checkArgument(record_total1 == 0, "修改后：累计报备信息数量增加了" + record_total1 + "\n");
            Preconditions.checkArgument(record_today1 == 0, "修改后：今日新增报备信息数量增加了" + record_today1 + "\n");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }

    }

    /**
     * c3
     * V3.0 一个渠道报备两个顾客，都是全号
     * 风控数据-截至目前-渠道顾客+2
     * 渠道管理-渠道报备统计-累计报备顾客数量 + 2
     * 渠道管理-渠道报备统计-累计报备信息数量 + 2
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 + 2
     * 渠道管理-渠道报备统计-今日新增报备信息数量 + 2
     **/
    @Test
    public void Onechannel_twocustomer() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：一个渠道全号报备两个不同的顾客\n";
        desc = desc + "\n期待：截至目前渠道报备人数 +2 ; 累计报备顾客数量 +2 ; 累计报备信息数量 +2 ; 今日新增报备顾客数量 +2 ; 今日新增报备信息数量 +2 \n ";
        try {
            int before_fkchannel = historyRuleDetail().getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            int before_customer_total = channelReptstatistics().getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatistics().getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatistics().getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatistics().getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);
            String name1 = "firstuser" + System.currentTimeMillis(); //1个渠道报备2顾客
            String customerPhone1 = "14422110002";
            H5Lianjia(name1, customerPhone1);
            String name2 = "seconduser" + System.currentTimeMillis(); //1个渠道报备2顾客
            String customerPhone2 = "14422110003";
            H5Lianjia(name2, customerPhone2);

            Thread.sleep(5000);

            int after_fkchannel = historyRuleDetail().getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            int after_customer_total = channelReptstatistics().getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatistics().getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatistics().getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatistics().getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == 2, "截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == 2, "累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 2, "累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 2, "今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == 2, "今日新增报备顾客数量增加了" + customer_today + "\n");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }

    }

    /**
     * c4
     * V3.0 一个渠道报备一个顾客，全号 + 隐藏
     * 风控数据-截至目前-渠道顾客+2
     * 渠道管理-渠道报备统计-累计报备顾客数量 + 2
     * 渠道管理-渠道报备统计-累计报备信息数量 + 2
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 + 2
     * 渠道管理-渠道报备统计-今日新增报备信息数量 + 2
     **/
    @Test
    public void Onechannel_onecustomer() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：一个渠道报备同一顾客的全号和隐藏手机号\n";
        desc = desc + "\n期待：截至目前渠道报备人数 +2 ; 累计报备顾客数量 +2 ; 累计报备信息数量 +2 ; 今日新增报备顾客数量 +2 ; 今日新增报备信息数量 +2 \n ";
        try {
            int before_fkchannel = historyRuleDetail().getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            int before_customer_total = channelReptstatistics().getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatistics().getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatistics().getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatistics().getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);
            String name1 = "oneuser" + System.currentTimeMillis(); //1个渠道报备1顾客 全号+隐藏
            String customerPhone1 = "14422110002";
            H5Lianjia(name1, customerPhone1);
            String customerPhone2 = "144****0002";
            H5Lianjia(name1, customerPhone2);

            Thread.sleep(5000);

            int after_fkchannel = historyRuleDetail().getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            int after_customer_total = channelReptstatistics().getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatistics().getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatistics().getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatistics().getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == 2, "截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == 2, "累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 2, "累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 2, "今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == 2, "今日新增报备顾客数量增加了" + customer_today + "\n");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }
    }

    /**
     * c5
     * V3.0 两个渠道报备一个顾客，姓名 + 隐藏 ；再都补全
     * 报备后补全前：
     * 风控数据-截至目前-渠道顾客+2
     * 渠道管理-渠道报备统计-累计报备顾客数量 + 2
     * 渠道管理-渠道报备统计-累计报备信息数量 + 2
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 + 2
     * 渠道管理-渠道报备统计-今日新增报备信息数量 + 2
     * <p>
     * 补全后：
     * 风控数据-截至目前-渠道顾客-1
     * 渠道管理-渠道报备统计-累计报备顾客数量 -1
     * 渠道管理-渠道报备统计-累计报备信息数量 +0
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 -1
     * 渠道管理-渠道报备统计-今日新增报备信息数量 +0
     **/
    @Test
    public void Twochannel_onecustomerfix() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：两个渠道隐藏手机号报备同一顾客，再将两条记录补全\n";
        desc = desc + "\n期待：补全前：截至目前渠道报备人数 +2 ; 累计报备顾客数量 +2 ; 累计报备信息数量 +2 ; 今日新增报备顾客数量 +2 ; 今日新增报备信息数量 +2 ;" +
                "\n修改后：截至目前渠道报备人数 -1 ; 累计报备顾客数量 -1 ; 累计报备信息数量 +0 ; 今日新增报备顾客数量 -1 ; 今日新增报备信息数量 +0 \n ";
        try {

            JSONObject historyRuleDetailB = historyRuleDetail();
            int before_fkchannel = historyRuleDetailB.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客

            JSONObject channelReptstatisticsB = channelReptstatistics();
            int before_customer_total = channelReptstatisticsB.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatisticsB.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatisticsB.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatisticsB.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);

            String name1 = "fixphone" + System.currentTimeMillis(); //2个渠道报备1顾客 隐藏手机号
            String customerPhone1 = "144****0002";
            String customerfix = "14422110002";
            H5Lianjia(name1, customerPhone1);
            H5WuDong(name1, customerPhone1);

            Thread.sleep(5000);

            JSONObject historyRuleDetailA = historyRuleDetail();
            int after_fkchannel = historyRuleDetailA.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客

            JSONObject channelReptstatisticsA = channelReptstatistics();
            int after_customer_total = channelReptstatisticsA.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatisticsA.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatisticsA.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatisticsA.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == 2, "补全前：截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == 2, "补全前：累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 2, "补全前：累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 2, "补全前：今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == 2, "补全前：今日新增报备顾客数量增加了" + customer_today + "\n");

            //两个渠道补全手机号
            JSONArray list = customerList2(name1, wudongChannelIdStr, "", 1, 10).getJSONArray("list");
            String cid = list.getJSONObject(0).getString("cid");
            customerEditPC(cid, name1, customerfix, "", "");
            JSONArray list2 = customerList2(name1, lianjiaChannelStr, "", 1, 10).getJSONArray("list");
            String cid2 = list2.getJSONObject(0).getString("cid");
            customerEditPC(cid2, name1, customerfix, "", "");

            Thread.sleep(5000);

            JSONObject historyRuleDetailFix = historyRuleDetail();
            int fix_fkchannel = historyRuleDetailFix.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客

            JSONObject channelReptstatisticsFix = channelReptstatistics();
            int fix_customer_total = channelReptstatisticsFix.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int fix_record_total = channelReptstatisticsFix.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int fix_customer_today = channelReptstatisticsFix.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int fix_record_today = channelReptstatisticsFix.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(fix_customer_today + " " + fix_customer_total + " " + fix_record_today + " " + fix_record_total + " " + fix_fkchannel);

            int fkchannel2 = after_fkchannel - fix_fkchannel;
            int record_total2 = after_record_total - fix_record_total;
            int customer_total2 = after_customer_total - fix_customer_total;
            int record_today2 = after_record_today - fix_record_today;
            int customer_today2 = after_customer_today - fix_customer_today;
            Preconditions.checkArgument(fkchannel2 == 1, "补全后：截至目前渠道报备人数减少了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total2 == 1, "补全后：累计报备顾客数量减少了" + customer_total + "\n");
            Preconditions.checkArgument(record_total2 == 0, "补全后：累计报备信息数量减少了" + record_total + "\n");
            Preconditions.checkArgument(record_today2 == 0, "补全后：今日新增报备信息数量减少了" + record_today + "\n");
            Preconditions.checkArgument(customer_today2 == 1, "补全后：今日新增报备顾客数量减少了" + customer_today + "\n");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }
    }


    /**
     * c6
     * 两个渠道报备不同的两个人
     * 风控数据-截至目前-渠道顾客 +2
     * 渠道管理-渠道报备统计-累计报备顾客数量 +2
     * 渠道管理-渠道报备统计-累计报备信息数量 +2
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 +2
     * 渠道管理-渠道报备统计-今日新增报备信息数量 +2
     * <p>
     * 修改其中一个人的姓名手机号为今天已报备过的人
     * 风控数据-截至目前-渠道顾客 -1
     * 渠道管理-渠道报备统计-累计报备顾客数量 -1
     * 渠道管理-渠道报备统计-累计报备信息数量 +0
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 -1
     * 渠道管理-渠道报备统计-今日新增报备信息数量 +0
     */
    @Test
    public void Twochannel_twocustomer() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：两个渠道全号报备不同顾客，修改其中一条记录为今天渠道报备过的顾客信息\n";
        desc = desc + "\n期待：修改前：截至目前渠道报备人数 +0 ; 累计报备顾客数量 +0 ; 累计报备信息数量 +0 ; 今日新增报备顾客数量 +0 ; 今日新增报备信息数量 +0 ;" +
                "\n修改后：截至目前渠道报备人数 -1 ; 累计报备顾客数量 -1 ; 累计报备信息数量 +0 ; 今日新增报备顾客数量 -1 ; 今日新增报备信息数量 +0 \n ";
        try {
            //链家先报备一个人
            String beforename = "before" + System.currentTimeMillis();
            String beforephone = "14422110002";
            H5Lianjia(beforename, beforephone);
            Thread.sleep(2000);

            JSONObject historyRuleDetailB = historyRuleDetail();
            int before_fkchannel = historyRuleDetailB.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsB = channelReptstatistics();
            int before_customer_total = channelReptstatisticsB.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatisticsB.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatisticsB.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatisticsB.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);

            String name1 = "1-" + System.currentTimeMillis(); //2个渠道报备2个不同的顾客
            String phone1 = "14422110004";
            Thread.sleep(1000);
            String name2 = "2-" + System.currentTimeMillis(); //2个渠道报备2个不同的顾客
            String phone2 = "14422110005";
            H5Lianjia(name1, phone1);
            H5WuDong(name2, phone2);

            Thread.sleep(2000);

            JSONObject historyRuleDetailA = historyRuleDetail();
            int after_fkchannel = historyRuleDetailA.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsA = channelReptstatistics();
            int after_customer_total = channelReptstatisticsA.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatisticsA.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatisticsA.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatisticsA.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == 2, "修改前：截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == 2, "修改前：累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 2, "修改前：累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 2, "修改前：今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == 2, "修改前：今日新增报备顾客数量增加了" + customer_today + "\n");

            //修改其中一个人的姓名手机号为今天已报备过的人
            JSONArray list = customerList2(name2, wudongChannelIdStr, "", 1, 10).getJSONArray("list");
            String cid = list.getJSONObject(0).getString("cid");
            customerEditPC(cid, beforename, beforephone, "", "");
            Thread.sleep(2000);
            JSONObject historyRuleDetailFix = historyRuleDetail();
            int fix_fkchannel = historyRuleDetailFix.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsFix = channelReptstatistics();
            int fix_customer_total = channelReptstatisticsFix.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int fix_record_total = channelReptstatisticsFix.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int fix_customer_today = channelReptstatisticsFix.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int fix_record_today = channelReptstatisticsFix.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(fix_customer_today + " " + fix_customer_total + " " + fix_record_today + " " + fix_record_total + " " + fix_fkchannel);

            int fkchannel2 = after_fkchannel - fix_fkchannel;
            int record_total2 = after_record_total - fix_record_total;
            int customer_total2 = after_customer_total - fix_customer_total;
            int record_today2 = after_record_today - fix_record_today;
            int customer_today2 = after_customer_today - fix_customer_today;
            Preconditions.checkArgument(fkchannel2 == 1, "修改后：截至目前渠道报备人数减少了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total2 == 1, "修改后：累计报备顾客数量减少了" + customer_total + "\n");
            Preconditions.checkArgument(record_total2 == 0, "修改后：累计报备信息数量减少了" + record_total + "\n");
            Preconditions.checkArgument(record_today2 == 0, "修改后：今日新增报备信息数量减少了" + record_today + "\n");
            Preconditions.checkArgument(customer_today2 == 1, "修改后：今日新增报备顾客数量减少了" + customer_today + "\n");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }
    }


    /**
     * c7
     * V3.0 两个渠道报备同一个顾客 都是全号
     * <p>
     * 修改其中一条记录为今天/之前有渠道报备过的顾客信息
     * 风控数据-截至目前-渠道顾客+0
     * 渠道管理-渠道报备统计-累计报备顾客数量 + 0
     * 渠道管理-渠道报备统计-累计报备信息数量 + 0
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 + 0
     * 渠道管理-渠道报备统计-今日新增报备信息数量 + 0
     **/
    @Test
    public void Twochannel_onecustomer2() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：两个渠道全号报备同一顾客，修改其中一条记录为渠道报备过的顾客信息\n";
        desc = desc + "\n期待：截至目前渠道报备人数 +0 ; 累计报备顾客数量 +0 ; 累计报备信息数量 +0 ; 今日新增报备顾客数量 +0 ; 今日新增报备信息数量 +0 \n ";
        try {
            String beforename = "before" + System.currentTimeMillis();
            String beforephone = "14422110009";
            H5Lianjia(beforename, beforephone); //先报备一条信息

            String name = "2channel" + System.currentTimeMillis(); //2个渠道报备同一顾客
            String customerPhone = "14422110002";
            H5Lianjia(name, customerPhone);
            H5WuDong(name, customerPhone);
            Thread.sleep(2000);
            int before_fkchannel = historyRuleDetail().getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            int before_customer_total = channelReptstatistics().getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatistics().getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatistics().getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatistics().getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);
            //修改其中一条记录为今天报备过的顾客信息

            JSONArray list = customerList2(name, wudongChannelIdStr, "", 1, 10).getJSONArray("list");
            String cid = list.getJSONObject(0).getString("cid");
            customerEditPC(cid, beforename, beforephone, "", "");

            int after_fkchannel = historyRuleDetail().getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            int after_customer_total = channelReptstatistics().getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatistics().getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatistics().getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatistics().getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == 0, "截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == 0, "累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 0, "累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 0, "今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == 0, "今日新增报备顾客数量增加了" + customer_today + "\n");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }

    }


    /**
     * c8
     * V3.0 两个渠道报备同一个顾客 都是全号
     * <p>
     * 修改其中一条记录为由PC新建且无报备信息的顾客
     * 风控数据-截至目前-渠道顾客+1
     * 渠道管理-渠道报备统计-累计报备顾客数量 + 1
     * 渠道管理-渠道报备统计-累计报备信息数量 + 0
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 + 1
     * 渠道管理-渠道报备统计-今日新增报备信息数量 + 0
     **/
    @Test
    public void Twochannel_onecustomer3() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：两个渠道报备同一顾客，修改其中一条记录为PC报备无渠道的顾客信息\n";
        desc = desc + "\n期待：截至目前渠道报备人数 +1 ; 累计报备顾客数量 +1 ; 累计报备信息数量 +0 ; 今日新增报备顾客数量 +1 ; 今日新增报备信息数量 +0 \n ";
        try {
            String PCname = "PC" + System.currentTimeMillis(); //PC新建顾客无渠道
            String customerPhone1 = "14422110002";
            PCF(PCname, customerPhone1);
            String name = "2channel" + System.currentTimeMillis(); //2个渠道报备同一顾客
            String customerPhone = "14422110002";
            H5Lianjia(name, customerPhone);
            H5WuDong(name, customerPhone);
            Thread.sleep(2000);
            int before_fkchannel = historyRuleDetail().getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            int before_customer_total = channelReptstatistics().getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatistics().getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatistics().getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatistics().getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);
            //修改其中一条记录为今天PC无渠道的顾客信息

            JSONArray list = customerList2(name, wudongChannelIdStr, "", 1, 10).getJSONArray("list");
            String cid = list.getJSONObject(0).getString("cid");
            customerEditPC(cid, PCname, "14422110002", "", "");
            Thread.sleep(2000);
            int after_fkchannel = historyRuleDetail().getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            int after_customer_total = channelReptstatistics().getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatistics().getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatistics().getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatistics().getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == 1, "截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == 1, "累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 0, "两累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 0, "今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == 1, "今日新增报备顾客数量增加了" + customer_today + "\n");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }

    }

    /**
     * c9
     * V3.0 两个渠道报备不同顾客
     * 将其中一条记录修改为今天之前报备的有渠道信息的顾客(报备一个，修改报备时间)
     * <p>
     * 风控数据-截至目前-渠道顾客 -1
     * 渠道管理-渠道报备统计-累计报备顾客数量 -1
     * 渠道管理-渠道报备统计-累计报备信息数量 +0
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 -1
     * 渠道管理-渠道报备统计-今日新增报备信息数量 +0
     **/
    @Test
    public void Twochannel_twocustomer2() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：两个渠道报备两个顾客，修改其中一条记录为报备时间在今天之前的顾客信息\n";
        desc = desc + "\n期待：截至目前渠道报备人数 -1 ; 累计报备顾客数量 -1 ; 累计报备信息数量 +0 ; 今日新增报备顾客数量 -1 ; 今日新增报备信息数量 +0 \n ";
        try {
            //报备一个用户，修改报备时间
            String name = "changereporttime" + System.currentTimeMillis();
            String phone = "14422110003";
            H5WuDong(name, phone); //勿动报备顾客
            Thread.sleep(500);
            updateReportTimeChannel(phone, name, 5, 2098, 1585220718000L);
            Thread.sleep(500);
            String name1 = "L" + System.currentTimeMillis(); //2个渠道报备2个不同的顾客
            String phone1 = "14422110004";
            Thread.sleep(1000);
            String name2 = "W" + System.currentTimeMillis(); //2个渠道报备2个不同的顾客
            String phone2 = "14422110005";
            H5Lianjia(name1, phone1);
            H5WuDong(name2, phone2);

            Thread.sleep(2000);

            JSONObject historyRuleDetailB = historyRuleDetail();
            int before_fkchannel = historyRuleDetailB.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsB = channelReptstatistics();
            int before_customer_total = channelReptstatisticsB.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatisticsB.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatisticsB.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatisticsB.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);


            JSONArray list = customerList2(name1, lianjiaChannelStr, "", 1, 10).getJSONArray("list");
            String cid = list.getJSONObject(0).getString("cid");
            customerEditPC(cid, name, phone, "", ""); //将今天新建的链家顾客改为之前的报备过的勿动的顾客

            Thread.sleep(1000);

            JSONObject historyRuleDetailA = historyRuleDetail();
            int after_fkchannel = historyRuleDetailA.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsA = channelReptstatistics();
            int after_customer_total = channelReptstatisticsA.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatisticsA.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatisticsA.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatisticsA.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == -1, "截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == -1, "累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 0, "累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 0, "今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == -1, "今日新增报备顾客数量增加了" + customer_today + "\n");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }

    }


    /**
     * c10
     * PC使用渠道报备该渠道业务员
     * 截至目前渠道报备人数+1
     * 累计报备顾客数量+1
     * 今日新增报备顾客数量+1
     * 累计报备信息数量+1
     * 今日新增报备信息+1
     **/
    @Test
    public void PCchannelstaff() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：PC有渠道报备该渠道业务员\n";
        desc = desc + "\n期待：截至目前渠道报备人数 +1 ; 累计报备顾客数量 +1 ; 累计报备信息数量 +1 ; 今日新增报备顾客数量 +1 ; 今日新增报备信息数量 +1 \n ";
        try {
            //新建渠道
            String channelname = Long.toString(System.currentTimeMillis());
            Random random = new Random();
            String phone = "144";
            for (int i = 0; i < 8; i++) {
                phone = phone + random.nextInt(10);

            }
            addChannel(channelname, channelname, phone, "837");
            int channelid = channelList(1, 1).getJSONArray("list").getJSONObject(0).getInteger("channel_id");
            //先新建业务员
            String staffname = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + System.currentTimeMillis();
            //业务员手机号随机生成
            String phone2 = "135";
            for (int i = 0; i < 8; i++) {
                phone2 = phone2 + random.nextInt(10);

            }
            addChannelStaff(Integer.toString(channelid), staffname, phone2);

            Thread.sleep(2000);
            JSONObject historyRuleDetailB = historyRuleDetail();
            int before_fkchannel = historyRuleDetailB.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsB = channelReptstatistics();
            int before_customer_total = channelReptstatisticsB.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatisticsB.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatisticsB.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatisticsB.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);

            //PC有渠道报备
            PCT(staffname, channelid, staffname, phone2);
            Thread.sleep(1000);

            JSONObject historyRuleDetailA = historyRuleDetail();
            int after_fkchannel = historyRuleDetailA.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsA = channelReptstatistics();
            int after_customer_total = channelReptstatisticsA.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatisticsA.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatisticsA.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatisticsA.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == 1, "截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == 1, "累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 1, "累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 1, "今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == 1, "今日新增报备顾客数量增加了" + customer_today + "\n");
            //禁用业务员

            String staffid = channelStaffList(Integer.toString(channelid), staffname, 1, 1).getJSONArray("list").getJSONObject(0).getString("id");//业务员id
            changeChannelStaffState(staffid);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }

    }

    /**
     * c11
     * PC无渠道登记置业顾问和业务员
     * 截至目前自然登记人数 +2
     **/
    @Test
    public void PCchannel_staff() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：PC无渠道登记置业顾问和业务员\n";
        desc = desc + "\n期待：截至目前自然登记人数 +2 \n ";
        try {
            //新建渠道
            String channelname = Long.toString(System.currentTimeMillis());
            Random random = new Random();
            String phone = "134";
            for (int i = 0; i < 8; i++) {
                phone = phone + random.nextInt(10);

            }
            //System.out.println("phone : "+phone);
            addChannel(channelname, channelname, phone, "837");
            int channelid = channelList(1, 1).getJSONArray("list").getJSONObject(0).getInteger("channel_id");


            //先新建业务员
            String staffname = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + System.currentTimeMillis();
            //业务员手机号随机生成
            String phone2 = "135";
            for (int i = 0; i < 8; i++) {
                phone2 = phone2 + random.nextInt(10);

            }
            //System.out.println("phone2 : "+phone2);
            addChannelStaff(Integer.toString(channelid), staffname, phone2);
            //新建置业顾问
            addStaff(staffname, "14422119999", "");

            JSONObject historyRuleDetailB = historyRuleDetail();
            int before_fkchannel = historyRuleDetailB.getInteger("natural_visitor"); //风控数据-截至目前-自然顾客
            //PC 无渠道报备
            PCF(staffname, phone2);
            PCF(staffname, "14422119999");
            Thread.sleep(2000);

            JSONObject historyRuleDetailA = historyRuleDetail();
            int after_fkchannel = historyRuleDetailA.getInteger("natural_visitor"); //风控数据-截至目前-渠道顾客

            int fkchannel = after_fkchannel - before_fkchannel;

            Preconditions.checkArgument(fkchannel == 2, "截至目前渠道报备人数增加了" + fkchannel + "\n");
            //禁用业务员
            String staffid = channelStaffList(Integer.toString(channelid), staffname, 1, 1).getJSONArray("list").getJSONObject(0).getString("id");//业务员id
            changeChannelStaffState(staffid);
            //删除置业顾问
            String staffid2 = staffList("14422119999", 1, 1).getJSONArray("list").getJSONObject(0).getString("id");//置业顾问id
            staffDelete(staffid2);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }

    }


    /**
     * c12
     * V3.0 将今天之前渠道报备的隐藏手机号顾客，补全为 未被渠道报备过的顾客 （修改报备时间）
     * 风控数据-截至目前-渠道顾客 +0
     * 渠道管理-渠道报备统计-累计报备顾客数量 +0
     * 渠道管理-渠道报备统计-累计报备信息数量 +0
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 +0
     * 渠道管理-渠道报备统计-今日新增报备信息数量 +0
     **/
    @Test
    public void afterfix_unique() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：补全报备时间在今天之前的隐藏手机号顾客为未被渠道报备过的顾客\n";
        desc = desc + "\n期待：截至目前渠道报备人数 +0 ; 累计报备顾客数量 +0 ; 累计报备信息数量 +0 ; 今日新增报备顾客数量 +0 ; 今日新增报备信息数量 +0 \n ";
        try {

            String name = "L" + System.currentTimeMillis();
            String phone = "144****0066";
            String fixphone = "14422110066";
            H5Lianjia(name, phone);
            Thread.sleep(500);

            updateReportTimeChannel(phone, name, 1, 2136, 1585220718000L);

            JSONObject historyRuleDetailB = historyRuleDetail();
            int before_fkchannel = historyRuleDetailB.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsB = channelReptstatistics();
            int before_customer_total = channelReptstatisticsB.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatisticsB.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatisticsB.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatisticsB.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);
            JSONArray list = customerList2(name, lianjiaChannelStr, "", 1, 10).getJSONArray("list");
            String cid = list.getJSONObject(0).getString("cid");
            customerEditPC(cid, name, fixphone, "", ""); //将今天新建的链家顾客改为之前的报备过的勿动的顾客

            Thread.sleep(500);
            JSONObject historyRuleDetailA = historyRuleDetail();
            int after_fkchannel = historyRuleDetailA.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsA = channelReptstatistics();
            int after_customer_total = channelReptstatisticsA.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatisticsA.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatisticsA.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatisticsA.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == 0, "截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == 0, "累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 0, "累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 0, "今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == 0, "今日新增报备顾客数量增加了" + customer_today + "\n");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }

    }


    /**
     * c13
     * V3.0 将今天之前渠道报备的隐藏手机号顾客，补全为 被渠道报备过的顾客 （修改报备时间）
     * 风控数据-截至目前-渠道顾客 -1
     * 渠道管理-渠道报备统计-累计报备顾客数量 -1
     * 渠道管理-渠道报备统计-累计报备信息数量 +0
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 +0
     * 渠道管理-渠道报备统计-今日新增报备信息数量 +0
     **/
    @Test
    public void afterfix_exist() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        String desc = "校验：补全报备时间在今天之前的隐藏手机号顾客为被渠道报备过的顾客\n";
        desc = desc + "\n期待：截至目前渠道报备人数 -1 ; 累计报备顾客数量 -1 ; 累计报备信息数量 +0 ; 今日新增报备顾客数量 +0 ; 今日新增报备信息数量 +0 \n ";
        try {

            String name = "LW" + System.currentTimeMillis();
            String phone = "144****0066";
            String fixphone = "14422110066";
            H5Lianjia(name, phone);
            H5WuDong(name, fixphone);
            Thread.sleep(500);

            updateReportTimeChannel(phone, name, 1, 2136, 1585220718000L);
            updateReportTimeChannel(fixphone, name, 5, 2098, 1585220718000L);

            JSONObject historyRuleDetailB = historyRuleDetail();
            int before_fkchannel = historyRuleDetailB.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsB = channelReptstatistics();
            int before_customer_total = channelReptstatisticsB.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatisticsB.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatisticsB.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatisticsB.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);
            JSONArray list = customerList2(name, lianjiaChannelStr, "", 1, 10).getJSONArray("list");
            String cid = list.getJSONObject(0).getString("cid");
            customerEditPC(cid, name, fixphone, "", ""); //将之前新建的链家顾客改为之前的报备过的勿动的顾客

            Thread.sleep(500);
            JSONObject historyRuleDetailA = historyRuleDetail();
            int after_fkchannel = historyRuleDetailA.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsA = channelReptstatistics();
            int after_customer_total = channelReptstatisticsA.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatisticsA.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatisticsA.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatisticsA.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == -1, "截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == -1, "累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 0, "累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 0, "今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == 0, "今日新增报备顾客数量增加了" + customer_today + "\n");


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }

    }

    /**
     * c14
     * V3.0 将今天之前渠道报备的两个相同隐藏手机号相同姓名的顾客，今天补全 （修改报备时间）
     * 风控数据-截至目前-渠道顾客 -1
     * 渠道管理-渠道报备统计-累计报备顾客数量 -1
     * 渠道管理-渠道报备统计-累计报备信息数量 +0
     * 渠道管理-渠道报备统计-今日新增报备顾客数量 +0
     * 渠道管理-渠道报备统计-今日新增报备信息数量 +0
     **/
    @Test
    public void twofixone() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        String desc = "校验：补全报备时间在今天之前的相同隐藏手机号相同姓名的顾客为同一个人\n";
        desc = desc + "\n期待：截至目前渠道报备人数 -1 ; 累计报备顾客数量 -1 ; 累计报备信息数量 +0 ; 今日新增报备顾客数量 +0 ; 今日新增报备信息数量 +0 \n";

        try {

            String name = "LW" + System.currentTimeMillis();
            String phone = "144****0077";
            String fixphone = "14422110077";
            H5Lianjia(name, phone);
            H5WuDong(name, phone);
            Thread.sleep(500);

            updateReportTimeChannel(phone, name, 1, 2136, 1585220718000L);
            Thread.sleep(500);
            updateReportTimeChannel(phone, name, 5, 2098, 1585220718000L);
            Thread.sleep(500);

            JSONObject historyRuleDetailB = historyRuleDetail();
            int before_fkchannel = historyRuleDetailB.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsB = channelReptstatistics();
            int before_customer_total = channelReptstatisticsB.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int before_record_total = channelReptstatisticsB.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int before_customer_today = channelReptstatisticsB.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int before_record_today = channelReptstatisticsB.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(before_customer_today + " " + before_customer_total + " " + before_record_today + " " + before_record_total + " " + before_fkchannel);
            JSONArray list1 = customerList2(name, lianjiaChannelStr, "", 1, 10).getJSONArray("list");
            String cid1 = list1.getJSONObject(0).getString("cid");
            customerEditPC(cid1, name, fixphone, "", ""); //补全手机号
            JSONArray list2 = customerList2(name, wudongChannelIdStr, "", 1, 10).getJSONArray("list");
            String cid2 = list2.getJSONObject(0).getString("cid");
            customerEditPC(cid2, name, fixphone, "", ""); //补全手机号

            Thread.sleep(500);
            JSONObject historyRuleDetailA = historyRuleDetail();
            int after_fkchannel = historyRuleDetailA.getInteger("channel_visitor"); //风控数据-截至目前-渠道顾客
            JSONObject channelReptstatisticsA = channelReptstatistics();
            int after_customer_total = channelReptstatisticsA.getInteger("customer_total");//渠道管理-渠道报备统计-累计报备顾客数量
            int after_record_total = channelReptstatisticsA.getInteger("record_total");//渠道管理-渠道报备统计-累计报备信息数量
            int after_customer_today = channelReptstatisticsA.getInteger("customer_today");//渠道管理-渠道报备统计-今日新增报备顾客数量
            int after_record_today = channelReptstatisticsA.getInteger("record_today");//渠道管理-渠道报备统计-今日新增报备信息数量
//            System.out.println(after_customer_today + " " + after_customer_total + " " + after_record_today + " " + after_record_total + " " + after_fkchannel);

            int fkchannel = after_fkchannel - before_fkchannel;
            int record_total = after_record_total - before_record_total;
            int customer_total = after_customer_total - before_customer_total;
            int record_today = after_record_today - before_record_today;
            int customer_today = after_customer_today - before_customer_today;
            Preconditions.checkArgument(fkchannel == -1, "截至目前渠道报备人数增加了" + fkchannel + "\n");
            Preconditions.checkArgument(customer_total == -1, "累计报备顾客数量增加了" + customer_total + "\n");
            Preconditions.checkArgument(record_total == 0, "累计报备信息数量增加了" + record_total + "\n");
            Preconditions.checkArgument(record_today == 0, "今日新增报备信息数量增加了" + record_today + "\n");
            Preconditions.checkArgument(customer_today == 0, "今日新增报备顾客数量增加了" + customer_today + "\n");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }

    }


    /**
     * V3.1渠道管理-累计报备顾客数量==风控数据-渠道顾客
     **/
    @Test
    public void cCustomer_EQ_FKcustomer() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        try {

            int customer_total = channelReptstatistics().getInteger("customer_total"); //渠道管理页-累计报备顾客数量
            int channel_visitor = historyRuleDetail().getInteger("channel_visitor"); //风控数据页-截至目前-渠道顾客
            Preconditions.checkArgument(customer_total == channel_visitor, "渠道管理页累计报备顾客数量" + customer_total + " != 风控数据页截至目前渠道报备人数" + channel_visitor);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：渠道管理页累计报备顾客数量与风控数据页渠道顾客一致\n");
        }
    }

    /**
     * V3.1渠道管理-累计报备顾客数量 <= 渠道管理-累计报备信息数量
     **/
    @Test
    public void cCustomer_LT_cRecord() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        try {

            int customer_total = channelReptstatistics().getInteger("customer_total"); //渠道管理页-累计报备顾客数量
            int record_total = channelReptstatistics().getInteger("record_total"); //渠道管理页-累计报备信息数量
            Preconditions.checkArgument(record_total >= customer_total, "渠道管理页累计报备顾客数量" + customer_total + " >  渠道管理页累计报备信息数量" + record_total);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：渠道管理页累计报备顾客数量 <= 渠道管理页累计报备信息数量\n");
        }
    }

    /**
     * V3.1渠道管理-累计报备信息数量 == 每个渠道报备数之和
     **/
    @Test
    public void cRecord_EQ_everychannel() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        try {

            int record_total = channelReptstatistics().getInteger("record_total"); //渠道管理页-累计报备信息数量
            int total = channelList(1, 1).getInteger("total");
            int total_customers = 0;

            for (int i = (int) Math.ceil((double) total / 50); i >= 1; i--) {

                JSONArray list = channelList(i, pageSize).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    total_customers = total_customers + single.getInteger("total_customers");
                }
            }

            Preconditions.checkArgument(record_total == total_customers, "渠道管理页累计报备信息数量" + record_total + " != 每个渠道报备数之和" + total_customers);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：渠道管理页累计报备信息数量与各渠道报备总数一致\n");
        }
    }

    /**
     * V3.1 累计报备顾客 - 今日新增 >= 风控数据-数据趋势的渠道报备人数总和 (线下，有把报备时间改很久之前的脏数据)
     */
    @Test
    public void totalminustoday_GT_trend() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1; //获取当前月份
        String caseName = ciCaseName;
        try {

            int customer_total = channelReptstatistics().getInteger("customer_total"); //渠道管理页-累计报备顾客数量
            int customer_today = channelReptstatistics().getInteger("customer_today"); //渠道管理页-今日新增报备顾客数量
            int trendcustomer = 0;
            String starttime = "2020-01-01";
            String endtime = "2020-01-31";
            JSONArray list = historycustomerTrend(starttime, endtime).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                trendcustomer = trendcustomer + single.getInteger("channel_visitor");
            }

            while (month > 1) {
                String a = String.format("%02d", month);
                System.out.println(a);
                starttime = "2020-" + a + "-01";
                endtime = "2020-" + a + "-31";
                JSONArray list2 = historycustomerTrend(starttime, endtime).getJSONArray("list");
                for (int i = 0; i < list2.size(); i++) {
                    JSONObject single = list2.getJSONObject(i);
                    trendcustomer = trendcustomer + single.getInteger("channel_visitor");
                }
                month = month - 1;
            }
            Preconditions.checkArgument(customer_total - customer_today >= trendcustomer, "累计报备顾客" + customer_total + "-今日新增" + customer_today + " < 风控数据-数据趋势的渠道报备人数总和" + trendcustomer + "\n");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：累计报备顾客 - 今日新增 >= 风控数据-数据趋势的渠道报备人数总和\n");
        }
    }


//---------------- 顾客数量一致性 end ---------------------

    /**
     * 错误token
     **/
    @Test
    public void checkcode() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        try {

            String url = "http://dev.store.winsenseos.cn/risk/";

            String insertBody = "{\"customer_name\":\"1\",\"phone\":\"13411111111\",\"adviser_name\":\"\",\"adviser_phone\":null,\"channel_staff_phone\":null,\"gender\":\"FEMALE\",\"shop_id\":4116}";
            String addStaffBody = "{staff_name: \"1\", phone: \"12312221111\", face_url: \"\", shop_id: 4116}";
            String addRuleBody = "{name: \"aaa\", ahead_report_time: \"0\", report_protect: \"\", shop_id: 4116}";
            String addChannelBody = "{channel_name: \"123\", owner_principal: \"1234\", phone: \"12336941018\", rule_id: 837, shop_id: 4116}";

            int code1 = badTokenPost(url + "customer/insert", insertBody).getInteger("code"); //PC新建顾客/risk/customer/insert
            int code2 = badPasswdLogin().getInteger("code");//登录页面/risk-login
            int code3 = badTokenPost(url + "staff/add", addStaffBody).getInteger("code");//新增置业顾问/risk/staff/add
            int code4 = badTokenPost(url + "rule/add", addRuleBody).getInteger("code");//新增规则/risk/rule/add
            int code5 = badTokenPost(url + "channel/add", addChannelBody).getInteger("code");//新增渠道/risk/channel/add

            Preconditions.checkArgument(code1 == 2001, "/risk/customer/insert接口传错误token，期待code为2001，实际为" + code1);
            Preconditions.checkArgument(code2 == 1001, "/risk-login接口登录时填写错误密码，期待为1001，实际为" + code2);
            //登陆时本来没加application/json时报1009，加上之后报1001
            Preconditions.checkArgument(code3 == 2001, "/risk/staff/add传错误token，期待code为2001，实际为" + code3);
            Preconditions.checkArgument(code4 == 2001, "/risk/rule/add传错误token，期待code为2001，实际为" + code4);
            Preconditions.checkArgument(code5 == 2001, "/risk/channel/add传错误token，期待code为2001，实际为" + code5);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：传递错误token接口返回值\n");
        }
    }


//    ----------------------------------------------变量定义--------------------------------------------------------------------

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    public String failReason = "";

    public String response = "";

    public boolean FAIL = false;

    public Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();

    public QADbUtil qaDbUtil = new QADbUtil();

    public int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;

    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    public String DEBUG = System.getProperty("DEBUG", "true");

    public String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    public HttpConfig config;

    String mineChannelStr = "5";

    int pageSize = 50;

    public String getIpPort() {
        return "http://dev.store.winsenseos.cn";
    }

    public void initHttpConfig() {
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

    public String httpPostWithCheckCode(String path, String json, String... checkColumnNames) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        checkCode(response, StatusCode.SUCCESS, path);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public String httpPostUrl(String path, String json) throws Exception {
        initHttpConfig();
        config.url(path).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public void checkCode(String response, int expect, String message) throws Exception {
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


    public Object getShopId() {
        return "4116";
    }


    public static final String STAFF_LIST = "/risk/staff/page";
    public static final String STAFF_TYPE_LIST = "/risk/staff/type/list";


    public static String STAFF_TYPE_LIST_JSON = "{\"shop_id\":${shopId}}";

    public static String STAFF_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    public static String STAFF_LIST_WITH_TYPE_JSON = "{\"shop_idaddStaffTestPage\":${shopId},\"staff_type\":\"${staffType}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";


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

        json += "    \"page_size\":" + pageSize + "\n" +
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
                        "    \"page\":" + page + ",\n" +
                        "   \"is_audited\":\"" + is_audited + "\",\n" +
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
                        "    \"size\":" + pageSize + ",\n";

        if (phone_or_name.equals("")) {
        } else {
            json = json + "    \"phone_or_name\":\"" + phone_or_name + "\",\n";

        }
        json = json + "    \"shop_id\":" + getShopId() + "\n" +
                "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject customerList2(String phone, String channel, String adviser, int page, int pageSize) throws
            Exception {

        String json =
                "{\n";

        if (!"".equals(phone)) {
            json += "    \"phone_or_name\":\"" + phone + "\",";
        }

        if (!"".equals(channel)) {
            json += "    \"channel_id\":" + channel + ",";
        }

        if (!"".equals(adviser)) {
            json += "    \"adviser_id\":" + adviser + ",";
        }

        json += "    \"shop_id\":" + getShopId() + "," +
                "    \"page\":" + page + "," +
                "    \"page_size\":" + pageSize +
                "}";


        String res = httpPostWithCheckCode("/risk/customer/list", json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject customerEditPC(String cid, String customerName, String phone, String adviserName, String
            adviserPhone) throws Exception {
        String url = "/risk/customer/edit/" + cid;
        String json =
                "{\n" +
                        "\"customer_name\":\"" + customerName + "\"," +
                        "\"phone\":\"" + phone + "\"," +
                        "\"adviser_name\":\"" + adviserName + "\"," +
                        "\"adviser_phone\":\"" + adviserPhone + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        Thread.sleep(1000);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //新建渠道
    public void addChannel(String channelName, String owner, String phone, String ruleId) throws Exception {
        String url = "/risk/channel/add";
        String json =
                "{\n" +
                        "    \"channel_name\":\"" + channelName + "\"," +
                        "    \"owner_principal\":\"" + owner + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"rule_id\":\"" + ruleId + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);
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
                        "    \"size\":" + pageSize + "\n" +
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
    public JSONObject personCatch(int page, int pageSize, String person_type, String start_time, String end_time) throws Exception {
        String url = "/risk/evidence/person-catch/page";
        String json = "{\n";
        if (!"".equals(person_type)) {
            json = json + "   \"person_type\":\"" + person_type + "\",\n";
        }
        if (!"".equals(start_time)) {
            json = json + "   \"start_time\":\"" + start_time + "\",\n";
        }
        if (!"".equals(end_time)) {
            json = json + "   \"end_time\":\"" + end_time + "\",\n";
        }
        json = json +
                "   \"page\":" + page + ",\n" +
                "   \"size\":" + pageSize + ",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 订单数据趋势(2020.02.12) 框架
     */
    public JSONObject historyOrderTrend(String start, String end) throws Exception {
        String url = "/risk/history/rule/order/trend";
        String json = "{\n" +
                //"   \"trend_type\":\"" + trendtype + "\",\n" +
                "   \"start_day\":\"" + start + "\",\n" +
                "   \"end_day\":\"" + end + "\",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 访客数据趋势(2020.02.12)
     */
    public JSONObject historycustomerTrend(String start, String end) throws Exception {
        String url = "/risk/history/rule/customer/trend";
        String json = "{\n" +
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
     * 活动详情
     */
    public JSONObject activityDetail(String id) throws Exception {
        String url = "/risk/manage/activity/detail";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}\n";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 活动客流对比
     */
    public JSONObject activityContrast(String id) throws Exception {
        String url = "/risk/manage/activity/passenger-flow/contrast";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}\n";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 活动列表
     */
    public JSONObject activityList(String activity_name, String activity_type, String activity_date, int page, int pageSize) throws Exception {
        String url = "/risk/manage/activity/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n";
        if (!activity_name.equals("")) {
            json = json + "\"activity_name\":\"" + activity_name + "\",\n";
        }
        if (!activity_type.equals("")) {
            json = json + "\"activity_type\":\"" + activity_type + "\",\n";
        }
        if (!activity_date.equals("")) {
            json = json + "\"activity_date\":\"" + activity_date + "\",\n";
        }
        json = json + "   \"page\":" + page + ",\n" +
                "   \"size\":" + pageSize + "\n" + "}\n";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 人脸搜索上传图片
     */
    public JSONObject imageUpload(String path) throws Exception {
        String url = "http://dev.store.winsenseos.cn/risk/imageUpload";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("authorization", authorization);
        httppost.addHeader("shop_id", String.valueOf(getShopId()));
        File file = new File(path);
        MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
        if (file.toString().contains("png")) {
            mpEntity.addBinaryBody("img_file", file, ContentType.IMAGE_PNG, file.getName());
        }
        if (file.toString().contains("txt")) {
            mpEntity.addBinaryBody("img_file", file, ContentType.IMAGE_JPEG, file.getName());
        }
        if (file.toString().contains("jpg")) {
            mpEntity.addBinaryBody("img_file", file, ContentType.IMAGE_JPEG, file.getName());
        }

        mpEntity.addTextBody("path", "undefined", ContentType.MULTIPART_FORM_DATA);
        HttpEntity httpEntity = mpEntity.build();
        httppost.setEntity(httpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        this.response = EntityUtils.toString(resEntity, "UTF-8");
        System.out.println(response.getStatusLine());
        //checkCode(this.response, StatusCode.SUCCESS, file.getName() + "\n");
        return JSON.parseObject(this.response);
    }


    /**
     * 人脸轨迹搜索
     */
    public String faceTraces(String showUrl) throws Exception {
        String url = "/risk/evidence/face/traces";
        url = getIpPort() + url;
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"show_url\":\"" + showUrl + "\"" +
                        "}";

        String res = httpPostUrl(url, json);

        return res;
    }


    /**
     * 渠道报备统计 (2020-03-02) 框架要改
     */
    public JSONObject channelReptstatistics() throws Exception {
        String url = "/risk/channel/report/statistics";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
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


    public void newCustomer(int channelId, String channelStaffName, String channelStaffPhone, String
            adviserName, String adviserPhone, String phone, String customerName, String gender) throws Exception {
        String url = "/risk/customer/insert";
        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\",";
        if (!"".equals(adviserName)) {
            json += "    \"adviser_name\":\"" + adviserName + "\",";
            json += "    \"adviser_phone\":\"" + adviserPhone + "\",";
        }

        if (channelId != -1) {
            json += "    \"channel_id\":" + channelId + "," +
                    "    \"channel_staff_name\":\"" + channelStaffName + "\"," +
                    "    \"channel_staff_phone\":\"" + channelStaffPhone + "\",";
        }

        json +=
                "    \"gender\":\"" + gender + "\"," +
                        "    \"shop_id\":" + getShopId() + "" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    public JSONObject staffList(String namePhone, int page, int size) throws Exception {
        String url = "/risk/staff/page";
        String json =
                "{\n" +
                        "\"name_phone\":\"" + namePhone + "\"," +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

//-------------------------------------------------------------用例用到的方法--------------------------------------------------------------------

    public long getTimebeforetoday() throws ParseException {//今天的00：00：00
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");//设置日期格式,今天的0点之前
        String datenow = df.format(new Date());// new Date()为获取当前系统时间，2020-02-18 00:00:00
        Date date = df.parse(datenow);
        long ts = date.getTime(); //转换为时间戳1581955200000
        System.out.println(ts);
        return ts;
    }

    public long getTimebeforeyseterday() throws ParseException { //昨天的00：00：00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -1);
        Date d = c.getTime();
        String day = format.format(d);
        long yesterdray = Long.parseLong(day);
        return yesterdray;
    }

    public String getStartTime(int n) throws ParseException { //前第n天的开始时间（当天的0点）
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -n);
        Date d = c.getTime();
        String day = format.format(d);
        //long starttime = Long.parseLong(day);
        return day;
    }

    public long getEndTime(int n) throws ParseException { //前第n天的结束时间(第二天的0点)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -n + 1);
        Date d = c.getTime();
        String day = format.format(d);
        long endtime = Long.parseLong(day);
        return endtime;
    }

    public String datetoday(String date) { //活动页面返回的3.1 转换为 历史页面 2020-03-07 格式
        String[] spl = date.split("\\.");
        String MM = spl[0];
        String DD = spl[1];
        String day = "2020-" + MM + "-" + DD;
        return day;
    }


    public void checkOrderListEqualsLinkList(JSONArray list) throws Exception {

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
            for (int r = 0; r < orderLinkList.size(); r++) {
                JSONObject link = orderLinkList.getJSONObject(r);
                String linkPoint = link.getString("link_point");
                String linkName = link.getString("link_name");

                if (linkPoint.contains("置业顾问")) {
                    String content = link.getJSONObject("link_note").getString("content");
                    System.out.println(content);
                    String adviserNameLink = content.substring(content.indexOf("为") + 1);
                    System.out.println(adviserNameLink);

                    if (!adviserName.equals(adviserNameLink)) {
                        throw new Exception("订单编号" + orderId + " 风控列表页，置业顾问是：" + adviserName + ",风控单页，置业顾问是：" + adviserNameLink + "，与预期结果不符");
                    }
                    break;
                }
                if (channelName != null && !"".equals(channelName)) {
                    if ("渠道报备".equals(linkName)) {
                        String content = link.getJSONObject("link_note").getString("content");
                        String channleNameLink = content.substring(0, content.indexOf("-"));
                        if (!channleNameLink.equals(channelName)) {
                            throw new Exception("订单编号" + orderId + "风控列表页，成交渠道是：" + channelName + "，风控单页，成交渠道是：" + channleNameLink + "，与预期结果不符");
                        }
                    }
                }


                if ("首次到访".equals(linkName)) {
                    String apperTimeLink = link.getString("link_time");
                    if (!apperTimeLink.equals(firstappearTime)) {
                        throw new Exception("订单编号" + orderId + "风控列表页，首次到访时间为：" + firstappearTime + "，风控单页，首次到访时间为：" + apperTimeLink + "，与预期结果不符");
                    }
                }


            }
            for (int m = orderLinkList.size() - 1; m > 0; m--) { //多次刷证取最开始一次
                JSONObject link = orderLinkList.getJSONObject(m);
                String linkPoint = link.getString("link_point");
                String linkName = link.getString("link_name");
                if ("正常:人证⽐对通过".equals(linkPoint)) {
                    String dealTimeLink = link.getString("link_time");
                    if (!dealTimeLink.equals(dealTime)) {
                        throw new Exception("订单编号" + orderId + "风控列表页，刷证时间为：" + dealTime + "，风控单页，刷证时间为：" + dealTimeLink + "，与预期结果不符");
                    }
                    break;
                }

            }
        }
    }

    public void checkOrderListFilter() throws Exception {
        String normal_list = orderList(1, "", 1, pageSize).getString("total");
        System.out.println(normal_list);
        String risk_list = orderList(3, "", 1, pageSize).getString("total");
        System.out.println(risk_list);
        String unknown_list = orderList(2, "", 1, pageSize).getString("total");
        System.out.println(unknown_list);
        int total = Integer.parseInt(normal_list) + Integer.parseInt(risk_list) + Integer.parseInt(unknown_list);
        if (Integer.parseInt(normal_list) > total) {
            throw new Exception("总单数" + total + " < 正常单单数" + normal_list + " ，与预期结果不符");
        }
        if (Integer.parseInt(risk_list) > total) {
            throw new Exception("总单数" + total + " < 风险单单数" + risk_list + " ，与预期结果不符");
        }
        if (Integer.parseInt(unknown_list) > total) {
            throw new Exception("总单数" + total + " < 未知单单数" + unknown_list + " ，与预期结果不符");
        }
    }

    public void checkRank(JSONArray list, String key, String function) throws Exception {
        for (int i = 0; i < list.size() - 1; i++) {
            JSONObject singleB = list.getJSONObject(i);
            long gmtCreateB = singleB.getLongValue("gmt_create");
            JSONObject singleA = list.getJSONObject(i + 1);
            long gmtCreateA = singleA.getLongValue("gmt_create");

            if (gmtCreateB < gmtCreateA) {
                String phoneB = singleB.getString(key);
                String phoneA = singleA.getString(key);

                throw new Exception(function + "没有按照创建时间倒排！前一条,phone:【" + phoneB + ",gmt_create【" + gmtCreateB +
                        "】，后一条phone【" + phoneA + ",gmt_create【" + gmtCreateA + "】" + " ，与预期结果不符");
            }
        }
    }

    public void compareOrderTimeValue(JSONObject data, String key, String orderId, String valueExpect, String function1, String function2) throws Exception {
        String valueStr = data.getString(key);
        if (valueStr != null && !"".equals(valueStr)) {
            String firstStr = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", Long.valueOf(valueStr));
            if (!firstStr.equals(valueExpect)) {
                throw new Exception("订单id：" + orderId + ",【" + key + "】在" + function1 + "中是：" + valueExpect + ",在" + function2 + "中是：" + firstStr + "，与预期结果不符");
            }
        }
    }

    public void compareValue(JSONObject data, String function, String cid, String key, String valueExpect, String comment) throws Exception {

        String value = getValue(data, key);

        if (!valueExpect.equals(value)) {
            throw new Exception(function + "id：" + cid + ",列表中" + comment + "：" + valueExpect + ",详情中：" + value + "，与预期结果不符");
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

    public int getContrastPassFlowNum(JSONObject data, String arrayKey) {

        int num = 0;

        JSONArray list = data.getJSONArray(arrayKey);
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            if (single.containsKey("num")) {
                num += single.getInteger("num");
            }
        }
        return num;
    }

    public void contrastActivityNum(String activityId, String time, int detailNew, int detailOld, int contrastAccmulated) throws Exception {

        if (detailNew + detailOld > contrastAccmulated) {
            throw new Exception("activity_id=" + activityId + "," + time + "，活动详情中新客" + detailNew +
                    "+老客" + detailOld + " > 活动客流对比中的该时期总人数" + contrastAccmulated + "与预期不符");
        }
    }

    public void activitydateEQhistory(String activityId) throws Exception {
        JSONArray this_cycle = activityContrast(activityId).getJSONArray("this_cycle"); //活动中
        JSONArray contrast_cycle = activityContrast(activityId).getJSONArray("contrast_cycle"); //活动前
        JSONArray influence_cycle = activityContrast(activityId).getJSONArray("influence_cycle"); //活动后
        for (int i = 0; i < contrast_cycle.size(); i++) {
            JSONObject single = contrast_cycle.getJSONObject(i);
            if (single.containsKey("num")) {
                String date = single.getString("date");
                String day = datetoday(date);
                int history_people = historypersonAccumulate(day).getJSONArray("list").getJSONObject(0).getInteger("present_cycle");//当天历史页面的人数
                int activity_people = single.getInteger("num");
                if (history_people != activity_people) {
                    throw new Exception(day + "活动" + activityId + "中，客流人数=" + activity_people + " , 历史统计页面顾客人数=" + history_people + " , 与预期不符");
                }

            }
        }
        for (int i = 0; i < this_cycle.size(); i++) {
            JSONObject single = this_cycle.getJSONObject(i);
            if (single.containsKey("num")) {
                String date = single.getString("date");
                String day = datetoday(date);
                int history_people = historypersonAccumulate(day).getJSONArray("list").getJSONObject(0).getInteger("present_cycle");//当天历史页面的人数
                int activity_people = single.getInteger("num");
                if (history_people != activity_people) {
                    throw new Exception(day + "活动" + activityId + "中，客流人数=" + activity_people + " , 历史统计页面顾客人数=" + history_people + " , 与预期不符");
                }

            }
        }
        for (int i = 0; i < influence_cycle.size(); i++) {
            JSONObject single = influence_cycle.getJSONObject(i);
            if (single.containsKey("num")) {
                String date = single.getString("date");
                String day = datetoday(date);
                int history_people = historypersonAccumulate(day).getJSONArray("list").getJSONObject(0).getInteger("present_cycle");//当天历史页面的人数
                int activity_people = single.getInteger("num");
                if (history_people != activity_people) {
                    throw new Exception(day + "活动" + activityId + "中，客流人数=" + activity_people + " , 历史统计页面顾客人数=" + history_people + " , 与预期不符");
                }

            }
        }
    }


    public ArrayList unique(ArrayList obj) { //arraylist 去重
        for (int i = 0; i < obj.size() - 1; i++) {
            for (int j = obj.size() - 1; j > i; j--) {
                if (obj.get(j).equals(obj.get(i))) {
                    obj.remove(j);
                }
            }
        }
        return obj;
    }

    public int getTimeNum(int status, String date) throws Exception { //某一天的数量。status为订单状态，day为某一天0点的时间戳
        int total = Integer.parseInt(orderList(status, "", 1, 10).getString("total"));//1正常 2未知 3风险
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long day = sdf.parse(date).getTime();
        System.out.println("day" + day);
        int Timenum = 0; //未知订单总数-今天订单=截止昨天24点前订单页的未知订单数量
        int a = 0;
        if (total > 50) {
            if (total % 50 == 0) {
                a = total / 50;
            } else {
                a = (int) Math.ceil(total / 50) + 1;
            }
            for (int i = 1; i <= a; i++) {
                JSONArray list = orderList(status, "", i, pageSize).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    long ordertime = single.getLong("deal_time");
                    if (ordertime >= day && ordertime < (day + 86400000)) {
                        Timenum = Timenum + 1;
                    }
                }
            }
        } else {
            JSONArray list = orderList(status, "", 1, pageSize).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                JSONObject single = list.getJSONObject(j);
                long ordertime = single.getLong("deal_time");
                if (ordertime >= day && ordertime < (day + 86400000)) {
                    Timenum = Timenum + 1;
                }
            }
        }
        return Timenum;
    }

    public int getValidDays(JSONObject data) {
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


    String zhangName = "张钧甯";
    String zhangPhone = "19111311116";
    long lianjiaReportTime = 1547014265000L;//2019-01-09 14:11:05
    long noChannelReportTime = 1547034265000L;//2019-01-09 19:44:25
    String lianjiaToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5XjgJDli7_liqjjgJEiLCJ1aWQiOjIxMzYsImxvZ2luVGltZSI6MT" +
            "U3ODk5OTY2NjU3NH0.kQsEw_wGVmPQ4My1p-FNZ556FJC7W177g7jfjFarTu4";
    String lianjiaFreezeStaffIdStr = "2136";
    int lianjiaFreezeStaffIdInt = 2136;
    String wudongToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
            "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";
    String wudongStaffIdStr = "2098";

    String wudongChannelIdStr = "5";
    String lianjiaChannelStr = "1";


    public void PCF(String customerName, String customerPhone) throws Exception { //PC无渠道

        String smsCode = "384435";
        String adviserName = zhangName;
        String adviserPhone = zhangPhone;
        int channelId = -1;
        String channelStaffName = "";
        String channelStaffPhone = "";
        newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = noChannelReportTime;
        updateReportTime_PCF(customerPhone, customerName, afterReportTime);

    }

    public void PCT(String customerName, int channelId, String channelStaffName, String channelStaffPhone) throws Exception { //PC有渠道
        String customerPhone = "14422110002";
        String smsCode = "384435";
        String adviserName = zhangName;
        String adviserPhone = zhangPhone;
        newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = noChannelReportTime;
        updateReportTime_PCF(customerPhone, customerName, afterReportTime);

    }


    public void H5WuDong(String customerName, String customerPhone) throws Exception {
        String smsCode = "384435";
        customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
    }

    public void H5Lianjia(String customerName, String customerPhone) throws Exception {
        String smsCode = "384435";
        customerReportH5(lianjiaFreezeStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);
        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = lianjiaReportTime;
        updateReportTimeChannel(customerPhone, customerName, 1, lianjiaFreezeStaffIdInt, afterReportTime);
    }


    public void updateReportTime_PCF(String phone, String customerName, long repTime) throws Exception {
        ReportTime reportTime = new ReportTime();
        reportTime.setShopId(4116);
        reportTime.setChannelId(-1);
        reportTime.setChannelStaffId(0);
        reportTime.setPhone(phone);
        reportTime.setCustomerName(customerName);
        long timestamp = repTime;
        reportTime.setReportTime(String.valueOf(timestamp));
        reportTime.setGmtCreate(dateTimeUtil.changeDateToSqlTimestamp(timestamp));
        qaDbUtil.updateReportTime(reportTime);
    }


    //@Test//报备
    public void H5WuDong1() throws Exception {
        String customerName = "昨报备今补全3";
        String customerPhone = "144****0199";
        String smsCode = "384435";
        customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
    }

    //@Test//报备
    public void H5Lianjia1() throws Exception {
        String customerName = "昨报备今补全3";
        String customerPhone = "14422110199";
        String smsCode = "384435";
        customerReportH5(lianjiaFreezeStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);
        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = lianjiaReportTime;
        updateReportTimeChannel(customerPhone, customerName, 1, lianjiaFreezeStaffIdInt, afterReportTime);
    }

    public void updateReportTimeChannel(String phone, String customerName, int channelId, int staffId, long repTime) throws
            Exception {
        ReportTime reportTime = new ReportTime();
        reportTime.setShopId(4116);
        reportTime.setChannelId(channelId);
        reportTime.setChannelStaffId(staffId);
        reportTime.setPhone(phone);
        reportTime.setCustomerName(customerName);
        long timestamp = repTime;
        reportTime.setReportTime(String.valueOf(timestamp));
        reportTime.setGmtCreate(dateTimeUtil.changeDateToSqlTimestamp(timestamp));

        qaDbUtil.updateReportTime(reportTime);
    }

    public JSONObject addStaff(String staffName, String phone, String faceUrl) throws Exception { //新建置业顾问

        String url = "/risk/staff/add";

        String json =
                "{\n" +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject staffDelete(String id) throws Exception { //删除置业顾问
        String url = "/risk/staff/delete/" + id;
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject addChannelStaff(String channelId, String staffName, String phone) throws Exception { //新建业务员

        String url = "/risk/channel/staff/register";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public void changeChannelStaffState(String staffId) throws Exception { //禁用业务员
        String json = "{}";

        httpPostWithCheckCode("/risk/channel/staff/state/change/" + staffId, json);
    }


    /**
     * 上传错误token
     */

    public JSONObject badTokenPost(String url, String body) throws Exception {//PC新建顾客错误token

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("authorization", authorization + " asd");
        httppost.addHeader("shop_id", String.valueOf(getShopId()));
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        //设置请求体
        httppost.setEntity(new StringEntity(body));

        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        this.response = EntityUtils.toString(resEntity, "UTF-8");
        System.out.println(response.getStatusLine());
        System.out.println(this.response);
        return JSON.parseObject(this.response);
    }

    public JSONObject badPasswdLogin() throws Exception { //登录错误密码
        String url = "http://dev.store.winsenseos.cn/risk-login";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("authorization", authorization);
        httppost.addHeader("shop_id", String.valueOf(getShopId()));
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        String body = "{\"username\":\"yuexiu@test.com\",\"passwd\":\"f5b3e737510f31b88eb2d4b5d0cd2fbBAD\"}";
        //设置请求体
        httppost.setEntity(new StringEntity(body));

        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        this.response = EntityUtils.toString(resEntity, "UTF-8");
        System.out.println(response.getStatusLine());
        System.out.println(this.response);
        return JSON.parseObject(this.response);
    }

    public void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
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

    public void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("飞单日常-数据一致性校验 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    public void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        msg = msg.replace("java.lang.Exception: ", "异常：");
        msg = msg.replace("java.lang.IllegalArgumentException:", "异常：");
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());


    }

    public void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

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

    //  public static void main(String[] args) throws ParseException {// ---不用理我！

//    }


}



