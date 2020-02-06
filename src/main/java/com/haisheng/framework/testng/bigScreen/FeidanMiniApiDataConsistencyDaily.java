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
import org.apache.tomcat.util.http.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    @BeforeSuite
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

    @AfterSuite
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

        String function = "校验H5页面业务员报备总数与H5页面内业务员报备条数一致\n";

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
            if (reportNumH5 > 50){
                System.out.println(reportNumH5);

                int a = (int)Math.ceil(reportNumH5 / 50) + 1;
                System.out.println(a);
                for (int i = 1 ; i <= a; i++){
                    String customerListH5 = channelCustomerListH5(token, i, 50);
                    reportNumListNum = reportNumListNum + JSON.parseObject(customerListH5).getJSONObject("data").getJSONArray("list").size();

                    reportNumListTotal = JSON.parseObject(customerListH5).getJSONObject("data").getInteger("total");
                }
            }
            else {
                String customerListH5 = channelCustomerListH5(token, 1, 50);
                reportNumListNum = JSON.parseObject(customerListH5).getJSONObject("data").getJSONArray("list").size();
                reportNumListTotal = JSON.parseObject(customerListH5).getJSONObject("data").getInteger("total");
            }

            if (reportNumH5 != reportNumListNum) {
                throw new Exception("业务员手机号:" + staffPhone + "， H5页面内的报备总数=" + reportNumH5 + ", H5页面内的报备条数=" + reportNumListNum);
            }

            if (reportNumListNum != reportNumListTotal) {
                throw new Exception("业务员手机号:" + staffPhone + "， H5页面内列表中的报备条数=" + reportNumListTotal + ", H5页面内的显示的报备条数=" + reportNumListNum);
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

        String function = "H5页面报备总数与PC页面内报备总数一致\n";

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
            if (totalnum > 50){
                int a = (int)Math.ceil(totalnum/50) + 1;
                for(int i = 1; i <= a; i++){
                    JSONArray staffList = channelStaffList(channelId, staffPhone, i, pageSize).getJSONArray("list");
                    for (int j = 0; j < staffList.size(); j++) {
                        JSONObject single = staffList.getJSONObject(j);
                        if ("2098".equals(single.getString("id"))) {
                            reportNumPC = reportNumPC + single.getInteger("total_report");
                            break;
                        } else {
                            throw new Exception("不存在手机号为：" + staffPhone + "，的业务员。");
                        }
                    }
                }
            }
            else {
                JSONArray staffList = channelStaffList(channelId, staffPhone, 1, pageSize).getJSONArray("list");
                for (int i = 0; i < staffList.size(); i++) {
                    JSONObject single = staffList.getJSONObject(i);
                    if ("2098".equals(single.getString("id"))) {
                        reportNumPC = single.getInteger("total_report");
                        break;
                    } else {
                        throw new Exception("不存在手机号为：" + staffPhone + "，的业务员。");
                    }
                }
            }


            if (reportNumH5 != reportNumPC) {
                throw new Exception("业务员手机号:" + staffPhone + ", H5页面内的报备数=" + reportNumH5 + ", PC端的报备数=" + reportNumPC);
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
     * 渠道总报备数==该渠道每个业务员的报备数 gaile
     */
    @Test
    public void channelEqualsStaffReport()  {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验渠道总报备数与该渠道每个业务员的报备数之和一致\n";

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
     * 证据页事项与风控列表中展示的信息一致：置业顾问、成交渠道、首次到访时间、刷证时间
     */
    @Test
    public void OrderListLinkEquals() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "校验证据页事项与风控列表中展示的信息一致\n";

        try {

            JSONArray list = orderList(1, "", 1,pageSize).getJSONArray("list");

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

        String function = "校验风控列表过滤项的子单数 <= 总单数\n";

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
            JSONArray list = orderList(1, "",1, pageSize).getJSONArray("list");
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
     * 渠道的累计报备数==各个业务员的累计报备数之和  gaile
     **/
    @Test
    public void channelTotalEqualsStaffTotal() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            JSONArray channelList = channelList(1, pageSize).getJSONArray("list");

            for (int i = 0; i < channelList.size(); i++) {
                JSONObject singleChannel = channelList.getJSONObject(i);
                int channelNum = singleChannel.getInteger("total_customers");
                String channelId = singleChannel.getString("channel_id");
                if ("1".equals(channelId)) {
                    channelNum -= 4;
                }
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
                }
                else {
                    JSONArray staffList = channelStaffList(channelId, "", 1, pageSize).getJSONArray("list");
                    for (int k = 0; k < staffList.size(); k++) {
                        JSONObject singleStaff = staffList.getJSONObject(k);
                        staffNum += singleStaff.getInteger("total_report");
                    }
                }


                if (staffNum != channelNum) {
                    throw new Exception("渠道【" + channelName + "】,渠道累计报备数：" + channelNum + "，业务员累计报备数之和：" + staffNum);
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "校验渠道的累计报备数==各个业务员的累计报备数之和\n");
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
                    throw new Exception("渠道【" + channelName + "】, 渠道列表中的报备数：" + channelReportNum);
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "校验渠道中的报备顾客数 >= 0\n");
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
            int normal_totalnum = Integer.parseInt(orderList(1,"",1,pageSize).getString("total"));
            System.out.println("normal"+ normal_totalnum);

//            获取未知订单数
            int unknown_totalnum = Integer.parseInt(orderList(2,"",1,pageSize).getString("total"));
            System.out.println("unkonw"+ unknown_totalnum);

//            获取风险订单数
            int risk_totalnum = Integer.parseInt(orderList(3,"",1,pageSize).getString("total"));
            System.out.println("risk"+ risk_totalnum);


            if (normal_totalnum + unknown_totalnum + risk_totalnum != totalNum) {
                throw new Exception("订单列表总数为：" + totalNum + "，正常订单数为" + normal_totalnum + "，未知订单数为" +unknown_totalnum + "，异常订单数为" +risk_totalnum);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "校验订单列表中，风险+正常+未知的订单数=订单列表总数\n");
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
                            ", 选择类型查询时，查询结果中该类型员工数为：" + array.size());
                }
            }

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "校验员工管理中，各类型员工数量统计是否正确\n");
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
            JSONArray jsonArray = orderList(1, "",1, pageSize).getJSONArray("list");
            checkRank(jsonArray, "customer_phone", "订单列表\n");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验订单列表按照新建时间倒排\n");
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
            saveData(aCase, ciCaseName, caseName, "校验员工列表按照新建时间倒排\n");
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
            saveData(aCase, ciCaseName, caseName, "校验渠道列表按照新建时间倒排\n");
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
            saveData(aCase, ciCaseName, caseName, "校验渠道员工列表按照新建时间倒排\n");
        }
    }

    /**
     * V2.4风控详情和下载的风控报告内容一致
     **/
   // @Test
    public void ReportEqualDetail() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            JSONArray list = orderList(1,"",1,10).getJSONArray("list");
            checkReportEqualDetail(list);
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "风控详情内容和下载的风控报告内容一致\n");
        }
    }


    /**
     * V2.4风控详情图片数量==人脸搜索图片数量
     **/
    //@Test
    public void LinkEqualFace() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            JSONArray list = orderList(1,"",1,10).getJSONArray("list"); //获取订单号
            checkLinkEqualFace(list);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "风控详情图片数量与人脸搜索图片数量一致\n");
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
                "{"+
                        "   \"shop_id\" : "+ getShopId()+",\n"+
                        "\"order_id\":"+ orderId +
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
                        "    \"page\":1" + page+ ",\n";
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
     * 渠道列表
     */
    public JSONObject channelList(int page, int pageSize) throws Exception {
        String url = "/risk/channel/page";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"page\":" + page + "," +
                        "    \"size\":" + pageSize +
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
     * 生成风控单
     */
    public JSONObject reportCreate(String orderId) throws Exception {
        String url = "/risk/evidence/risk-report/create";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"orderId\":\"" + orderId + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 人脸搜索
     */
    public JSONObject faceTraces(String showUrl) throws Exception {
        String url = "/risk/evidence/face/traces";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"orderId\":\"" + showUrl + "\"" +
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

    private void checkOrderListEqualsLinkList(JSONArray list) throws Exception {

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String orderId = single.getString("order_id");
            String adviserName = single.getString("adviser_name"); //置业顾问
            String channelName = single.getString("channel_name"); //成交渠道
            String firstappearTime = single.getString("first_appear_time"); //首次到访时间
            String dealTime = single.getString("deal_time"); //刷证时间
            JSONArray orderLinkList = orderLinkList(orderId).getJSONArray("list");
            for (int r = 1 ; r < orderLinkList.size(); r++) {
                JSONObject link = orderLinkList.getJSONObject(r);
                String linkPoint = link.getString("link_point");
                String linkName = link.getString("link_name");

                if (linkPoint.contains("置业顾问")) {
                    String content = link.getJSONObject("link_note").getString("content");
                    String adviserNameLink = content.substring(content.indexOf("为")+1);

                    if (!adviserName.equals(adviserNameLink)) {
                        throw new Exception("风控列表页，置业顾问是:" + adviserName + ",风控单页，置业顾问是：" + adviserNameLink);
                    }
                }
                if (channelName != null && !"".equals(channelName)) {
                    if ("渠道报备".equals(linkName)) {
                        String content = link.getJSONObject("link_note").getString("content");
                        String channleNameLink = content.substring(0,content.indexOf("-"));
                        if (!channleNameLink.equals(channelName)){
                            throw new Exception("风控列表页，成交渠道是："+ channelName +",风控单页，成交渠道是：" + channleNameLink);
                        }
                    }
                }


                if ("首次到访".equals(linkName)){
                    String apperTimeLink = link.getString("link_time");
                    if (!apperTimeLink.equals(firstappearTime)){
                        throw new Exception("风控列表页，首次到访时间为："+ firstappearTime + ",风控单页，首次到访时间为："+ apperTimeLink);
                    }
                }

                if ("正常:人证⽐对通过".equals(linkPoint)){
                    String dealTimeLink = link.getString("link_time");
                    if (!dealTimeLink.equals(dealTime)){
                        throw new Exception("风控列表页，刷证时间为："+ dealTime + ",风控单页，刷证时间为："+ dealTimeLink);
                    }
                }
            }
        }
    }

    private void checkOrderListFilter() throws Exception{
        String normal_list = orderList(1, "",1, pageSize).getString("total");
        System.out.println(normal_list);
        String risk_list = orderList(3, "", 1,pageSize).getString("total");
        System.out.println(risk_list);
        String unknown_list = orderList(2, "", 1,pageSize).getString("total");
        System.out.println(unknown_list);
        int total = Integer.parseInt(normal_list) + Integer.parseInt(risk_list) + Integer.parseInt(unknown_list);
        if (Integer.parseInt(normal_list) > total){
            throw new Exception("总单数为：" + total + ",正常单单数为："+ normal_list);
        }
        if (Integer.parseInt(risk_list) > total){
            throw new Exception("总单数为：" + total + ",风险单单数为："+ risk_list);
        }
        if ( Integer.parseInt(unknown_list) > total){
            throw new Exception("总单数为：" + total + ",未知单单数为："+ unknown_list);
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
                        "】，后一条phone【" + phoneA + ",gmt_create【" + gmtCreateA + "】");
            }
        }
    }

    public void compareOrderTimeValue(JSONObject data, String key, String orderId, String valueExpect, String function1, String function2) throws Exception {
        String valueStr = data.getString(key);
        if (valueStr != null && !"".equals(valueStr)) {
            String firstStr = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", Long.valueOf(valueStr));
            if (!firstStr.equals(valueExpect)) {
                throw new Exception("订单id：" + orderId + ",【" + key + "】在" + function1 + "中是：" + valueExpect + ",在" + function2 + "中是：" + firstStr);
            }
        }
    }

    public void compareValue(JSONObject data, String function, String cid, String key, String valueExpect, String comment) throws Exception {

        String value = getValue(data, key);

        if (!valueExpect.equals(value)) {
            throw new Exception(function + "id：" + cid + ",列表中" + comment + "：" + valueExpect + ",详情中：" + value);
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
            if (totalstaff > 50){
                int a = (int)Math.ceil(totalstaff/50) + 1;
                for(int j = 1; j <= a; j++) {
                    JSONArray staffList = channelStaffList(channelId, "", j, pageSize).getJSONArray("list");
                    for (int k = 0; k < totalstaff; k++) {
                        JSONObject singleStaff = staffList.getJSONObject(k);
                        total += singleStaff.getInteger("total_report");
                    }
                }
            }
            else {
                JSONArray staffList = channelStaffList(channelId, "", 1, pageSize).getJSONArray("list");
                for (int k = 0; k < totalstaff; k++) {
                    JSONObject singleStaff = staffList.getJSONObject(k);
                    total += singleStaff.getInteger("total_report");
                }
            }


            if (reportNum != total) {
                throw new Exception("渠道：" + channelName + ",渠道报备数=" + reportNum + ",业务员总报备数=" + total);
            }
        }
    }

    private void checkReportEqualDetail(JSONArray list) throws Exception{
        for (int i = 0;i< list.size();i++) {
            JSONObject single = list.getJSONObject(i);
            String orderId = single.getString("order_id");
            JSONObject orderdeatil = orderDetail(orderId);
            JSONObject reportdetail = reportCreate(orderId);
            //风控详情中
            String order_customername = orderdeatil.getString("customer_name"); //顾客姓名
            String order_firstappear = orderdeatil.getString("first_appear_time"); //首次出现时间
            String order_phone = orderdeatil.getString("phone"); //手机号
            String order_channelname = orderdeatil.getString("channel_name"); //渠道名称
            String order_channelstaff = orderdeatil.getString("channel_staff_name"); //渠道业务员名称
            String order_reporttime = orderdeatil.getString("report_time"); //渠道报备时间
            String order_advisername = orderdeatil.getString("adviser_name"); //置业顾问
            //生成的PDF里
            String report_customername = reportdetail.getString("customer_name");//顾客姓名
            String report_firstappear = reportdetail.getString("first_appear_time"); //首次出现时间
            String report_phone = reportdetail.getString("phone"); //手机号
            String report_channelname = reportdetail.getString("channel_name"); //渠道名称
            String report_channelstaff = reportdetail.getString("channel_staff_name"); //渠道业务员名称
            String report_reporttime = reportdetail.getString("report_time"); //渠道报备时间
            String report_advisername = reportdetail.getString("adviser_name"); //置业顾问

            if (!order_customername.equals(report_customername)){
                throw new Exception("风控详情与下载的风控报告PDF中 顾客姓名不一致");
            }
            if (!order_phone.equals(report_phone)){
                throw new Exception("风控详情与下载的风控报告PDF中 顾客手机号不一致");
            }
            if (!order_firstappear.equals(report_firstappear)){
                throw new Exception("风控详情与下载的风控报告PDF中 顾客首次出现时间不一致"); //格式是否一致？时间戳还是北京时间？？
            }
            if (order_channelname != null || !order_channelname.equals("")){
                if (!order_channelname.equals(report_channelname)){
                    throw new Exception("风控详情与下载的风控报告PDF中 渠道名称不一致");
                }
                if (!order_channelstaff.equals(report_channelstaff)){
                    throw new Exception("风控详情与下载的风控报告PDF中 渠道业务员姓名不一致");
                }
                if (!order_reporttime.equals(report_reporttime)){
                    throw new Exception("风控详情与下载的风控报告PDF中 渠道报备时间不一致"); //格式是否一致？时间戳还是北京时间？？
                }
            }
            if (order_advisername != null || !order_advisername.equals("")){
                if (!order_advisername.equals(report_advisername)){
                    throw new Exception("风控详情与下载的风控报告PDF中 置业顾问不一致");
                }
            }
        }
    }


    private void checkLinkEqualFace(JSONArray list) throws Exception{
        for(int i = 0; i< list.size(); i++){
            int linknum = 0; //风控详情照片数量
            int Facelistnum = 0;
            JSONObject single = list.getJSONObject(i);
            String orderId = single.getString("order_id");

            int total =  Integer.parseInt(orderLinkListPage(orderId,1,10).getString("total"));//订单详情条数，大于50则分开请求
            if (total > 50){
                int a = (int)Math.ceil(total / 50) + 1;
                for (int j = 1; j <= a; j++){
                    JSONArray detaillist = orderLinkListPage(orderId,j,pageSize).getJSONArray("list");
                    for (int k = 0; k < detaillist.size(); k++){
                        JSONObject single2 = list.getJSONObject(i);
                        if ("场内轨迹".equals(single2.getString("link_name"))){
                            linknum = linknum + 1;
                        }
                        if ("首次到访".equals(single2.getString("link_name"))){
                            linknum = linknum + 1;
                            String faceurl = single2.getString("face_url");
                            Facelistnum = faceTraces(faceurl).getJSONArray("list").size(); //人脸搜索图片数量
                        }
                    }
                }
            }
            else {
                JSONArray detaillist = orderLinkListPage(orderId,1,pageSize).getJSONArray("list");
                for (int k = 0; k < detaillist.size(); k++){
                    JSONObject single2 = list.getJSONObject(i);
                    if ("场内轨迹".equals(single2.getString("link_name"))){
                        linknum = linknum + 1;
                    }
                    if ("首次到访".equals(single2.getString("link_name"))){
                        linknum = linknum + 1;
                        String faceurl = single2.getString("face_url");
                        Facelistnum = faceTraces(faceurl).getJSONArray("list").size(); //人脸搜索图片数量
                    }
                }
            }
            if (linknum != Facelistnum){
                throw new Exception("风控详情中照片数量="+ linknum + "人脸搜索中照片数量=" + Facelistnum +"\n");
            }
        }
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
            dingPush("飞单日常 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        msg = msg.replace("java.lang.Exception: ","");
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



}

