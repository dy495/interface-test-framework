package com.haisheng.framework.testng.bigScreen.jiaochen.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumShopId;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.Variable.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.SelectReception;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FilterColumnSystemDaily extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = new ScenarioUtil();
    PublicParm pp = new PublicParm();
    //    JsonPathUtil jpu = new JsonPathUtil();
    public String shopId = "-1";

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_JIAOCHEN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.produce = EnumProduce.JC.name();
       //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "jc-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_DAILY.getName() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.shopId = EnumShopId.JIAOCHEN_DAILY.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
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
        jc.pcLogin(pp.gwphone, pp.gwpassword);
    }


    /**
     * @description :接待管理查询-筛选栏参数单项插查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_ReceptionManageFilter", dataProviderClass = Constant.class)
    public void selectAppointmentRecodeOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.pcReceptionManagePage("", "1", "10");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.receptionManage("", String.valueOf(1), "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.receptionManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String flag = list.getJSONObject(i).getString(output);
//                      list.forEach(e -> {
//                      JSONObject jsonObject = (JSONObject) e;
//                        String flag = jsonObject.getString(output);
                        Preconditions.checkArgument(flag.contains(result), "接待管理按" + result + "查询，结果错误" + flag);
                    };
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
           saveData("接待管理查询单项查询，结果校验");
        }
    }

    /**
     * @description :接待管理-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void selectAppointmentRecodeTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-5);
            String endTime=  dt.getHistoryDate(5);
            JSONObject respon=jc.receptionTimeManage("","1","10",startTime,endTime,startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.receptionTimeManage("", String.valueOf(page),"10",startTime,endTime,startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String receptionDate = list.getJSONObject(i).getString("reception_date");
                    String finishTime = list.getJSONObject(i).getString("finish_time");
                    Preconditions.checkArgument(receptionDate.compareTo(startTime)>=0&&receptionDate.compareTo(endTime)<=0, "接待管理开始时间："+startTime+" 结束时间："+endTime+" 列表中的接待时间为："+receptionDate);
                    Preconditions.checkArgument(finishTime.compareTo(startTime)>=0&&finishTime.compareTo(endTime)<=0, "接待管理开始时间："+startTime+" 结束时间："+endTime+" 列表中的完成时间为："+finishTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待管理列表时间的筛选，结果校验");
        }
    }

    /**
     * @description :接待管路-筛选栏参数全填查询
     * @date :2020/11/28
     **/
    @Test()
    public void selectAppointmentRecodeAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] ss = Constant.receptionManageFilter_pram();
            SelectReception sr = new SelectReception();
//            String startTime=  dt.getHistoryDate(-5);
//            String endTime=  dt.getHistoryDate(5);
            JSONArray res = jc.receptionManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                sr.plate_number = data.getString(ss[0][1].toString());
                sr.reception_sale_name = data.getString(ss[1][1].toString());
                sr.customer_name = data.getString(ss[2][1].toString());
                sr.reception_status = data.getString(ss[3][1].toString());
                sr.customer_phone = data.getString(ss[4][1].toString());
                sr.reception_type = data.getString(ss[5][1].toString());
                sr.shop_id = data.getString(ss[6][1].toString());
                sr.page = "1";
                sr.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.receptionManageC(sr).getJSONArray("list").getJSONObject(0);
                System.out.println("--------"+sr.reception_type);
                Preconditions.checkArgument(result.getString(ss[0][1].toString()).contains(sr.plate_number), "参数全部输入的查询的" + sr.plate_number + "与列表信息的第一行的" + result.getString(ss[0][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(ss[1][1].toString()).contains(sr.reception_sale_name), "参数全部输入的查询的" + result.getString(ss[1][1].toString() + "与列表信息的第一行的" + sr.reception_sale_name + "不一致"));
                Preconditions.checkArgument(result.getString(ss[2][1].toString()).contains(sr.customer_name), "参数全部输入的查询的" + sr.customer_name + "与列表信息的第一行的" + result.getString(ss[2][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(ss[3][1])).contains(sr.reception_status), "参数全部输入的查询的" + sr.reception_status + "与列表信息的第一行的" + result.getString(ss[3][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(ss[4][1].toString()).contains(sr.customer_phone), "参数全部输入的查询的" + sr.customer_phone + "与列表信息的第一行的" + result.getString(ss[4][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(ss[5][1])).contains(sr.reception_type), "参数全部输入的查询的" + sr.reception_type + "与列表信息的第一行的" + result.getString(String.valueOf(ss[5][1]) + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(ss[6][1])).contains(sr.shop_id), "参数全部输入的查询的" + sr.shop_id + "与列表信息的第一行的" + result.getString(ss[6][1].toString() + "不一致"));
           } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待管理列表查询全填，结果校验");
        }
    }


    /**
     * @description :接待管路-筛选栏参数多项查询
     * @date :2020/11/28
     **/
    @Test()
    public void selectAppointmentRecodeSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] ss = Constant.receptionManageFilter_pram();
            SelectReception sr = new SelectReception();
            JSONArray res = jc.receptionManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                sr.plate_number = data.getString(ss[0][1].toString());
                sr.reception_sale_name = data.getString(ss[1][1].toString());
                sr.customer_name = data.getString(ss[2][1].toString());
                sr.reception_status = data.getString(ss[3][1].toString());
                sr.customer_phone = data.getString(ss[4][1].toString());
                sr.page = "1";
                sr.size = "10";

                //全部筛选之后的结果
                JSONObject result = jc.receptionManageC(sr).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(ss[0][1].toString()).contains(sr.plate_number), "参数全部输入的查询的" + sr.plate_number + "与列表信息的第一行的" + result.getString(ss[0][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(ss[1][1].toString()).contains(sr.reception_sale_name), "参数全部输入的查询的" + result.getString(ss[1][1].toString() + "与列表信息的第一行的" + sr.reception_sale_name + "不一致"));
                Preconditions.checkArgument(result.getString(ss[2][1].toString()).contains(sr.customer_name), "参数全部输入的查询的" + sr.customer_name + "与列表信息的第一行的" + result.getString(ss[2][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(ss[3][1])).contains(sr.reception_status), "参数全部输入的查询的" + sr.reception_status + "与列表信息的第一行的" + result.getString(ss[3][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(ss[4][1].toString()).contains(sr.customer_phone), "参数全部输入的查询的" + sr.customer_phone + "与列表信息的第一行的" + result.getString(ss[4][1].toString() + "不一致"));
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待管理列表多项查询，结果校验");
        }
    }

    /**
     * @description :接待管路-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void selectAppointmentRecodeEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.receptionManage("", "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待管理列表查询参数不填写，结果校验");
        }
    }

//    @Test
//    public void Jc_erCode() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONObject data = jc.apperCOde();
//            String jsonpath = "$.er_code_url1";
//            jpu.spiltString(data.toJSONString(), jsonpath);
//
//        } catch (AssertionError | Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("轿辰-app个人中心，小程序码返回结果不为空");
//        }
//    }

    /**
     * @description :比较日期大小的方法-----暂时不用
     * @date :2020/12/14
     **/
    public void dataCompare() throws ParseException {
        String flag = "2020-12-13 12:33";
        String aaa = flag.substring(0, 9);
        //获取当前时间---df.format(date)
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //当前时间+-3天
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);    //获取年
        int month = rightNow.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
        int day = rightNow.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        int time = rightNow.get(Calendar.HOUR_OF_DAY);       //获取当前小时
        int min = rightNow.get(Calendar.MINUTE);          //获取当前分钟
        String startTime = year + "-" + month + "-" + (day - 3) + " " + time + ":" + min;
        String endTime = year + "-" + month + "-" + (day + 3) + " " + time + ":" + min;
        System.out.println("--------创建时间：" + "--------开始时间：" + startTime + "--------开始时间：" + endTime);
        Date startDate = df.parse(startTime);
        Date endDate = df.parse(endTime);
        Date createDate = df.parse("2020-12-14 12:56");
        System.out.println("--------创建时间：" + createDate + "--------开始时间：" + startDate + "--------开始时间：" + endDate);
        System.out.println(createDate.compareTo(startDate));
//        Preconditions.checkArgument(createDate.compareTo(startDate)>=0&&createDate.compareTo(endDate)<=0, "列表中创建时间："+createDate+"筛选栏开始时间："+startTime+"筛选栏开始时间："+endTime+"创建时间不在开始系欸结束之间之间");

    }

    /**
     * @description :销售客户查询-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_PreSleCustomerManageFilter", dataProviderClass = Constant.class)
    public void preSleCustomerManageOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.preSleCustomerManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.preSleCustomerManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.preSleCustomerManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "销售按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
         saveData("销售客户查询单项查询，结果校验");
        }
    }

    /**
     * @description 销售客户查询-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void preSleCustomerManageTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-10);
            String endTime=  dt.getHistoryDate(10);
            JSONObject respon=jc.preSleCustomerTimeManage("","1","10",startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.preSleCustomerTimeManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String createDate = list.getJSONObject(i).getString("create_date");
                    Preconditions.checkArgument(createDate.compareTo(startTime)>=0&&createDate.compareTo(endTime)<=0, "销售客户创建开始时间："+startTime+" 结束时间："+endTime+" 列表中的创建时间为："+createDate);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户列表时间的筛选，结果校验");
        }
    }

    /**
     * @description :销售客户查询-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void preSleCustomerManageALLFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.preSleCustomerManage_pram();
            PreSleCustomerVariable variable = new PreSleCustomerVariable();
            JSONArray res = jc.preSleCustomerManage(shopId, "1", "10", "", "").getJSONArray("list");
            String startTime=  dt.getHistoryDate(-10);
            String endTime=  dt.getHistoryDate(10);
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.customer_name = data.getString(flag[0][1].toString());
                variable.customer_phone = data.getString(flag[1][1].toString());
                variable.sale_name = data.getString(flag[2][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.shop_id = "-1";
                //全部筛选之后的结果
                JSONObject result = jc.preSleCustomerManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(flag[0][1].toString()).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.customer_phone), "参数全部输入的查询的" + variable.customer_phone + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(variable.sale_name), "参数全部输入的查询的" + variable.sale_name + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString("create_date").compareTo(startTime)>=0&&result.getString("create_date").compareTo(endTime)<=0, "销售客户创建开始时间："+startTime+" 结束时间："+endTime+" 列表中的创建时间为："+result.getString("create_date"));

            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户查询填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :销售客户查询-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void preSleCustomerManageSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.preSleCustomerManage_pram();
            PreSleCustomerVariable variable = new PreSleCustomerVariable();
            JSONArray res = jc.preSleCustomerManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.customer_name = data.getString(flag[0][1].toString());
                variable.customer_phone = data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.shop_id = "-1";
                //全部筛选之后的结果
                JSONArray result = jc.preSleCustomerManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(flag[0][1].toString()).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString()) + "不一致");
                    Preconditions.checkArgument(result.getJSONObject(i).getString(flag[1][1].toString()).contains(variable.customer_phone), "参数全部输入的查询的" + variable.customer_phone + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString()) + "不一致");
                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户查询填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :销售客户查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void preSleCustomerManageEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.preSleCustomerManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户列表参数不填写，结果校验");
        }
    }


    /**
     * @description :售后客户查询-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_AfterSleCustomerManageFilter", dataProviderClass = Constant.class)
    public void selectAfterSleCustomerManageOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.afterSleCustomerManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                if (result != null) {
                    JSONObject respon1 = jc.afterSleCustomerManage(shopId, "1", "10", pram, result);
                    int pages = respon1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.afterSleCustomerManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "售后客户管理按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后客户客户查询单项搜索，结果校验");
        }
    }

    /**
     * @description 售后客户查询-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void selectAfterSleCustomerManageTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-10);
            String endTime=  dt.getHistoryDate(10);
            JSONObject respon=jc.afterSleCustomerTimeManage("","1","10",startTime,endTime,startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.afterSleCustomerTimeManage("", String.valueOf(page),"10",startTime,endTime,startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String orderDate = list.getJSONObject(i).getString("start_order_date");
                    String importDate = list.getJSONObject(i).getString("import_date");
                    Preconditions.checkArgument(orderDate.compareTo(startTime)>=0&&orderDate.compareTo(endTime)<=0, "订单开始时间："+startTime+" 订单结束时间："+endTime+" 列表中的开单时间为："+orderDate);
                    Preconditions.checkArgument(importDate.compareTo(startTime)>=0&&importDate.compareTo(endTime)<=0, "创建开始时间："+startTime+" 创建结束时间："+endTime+" 列表中的导入时间为："+importDate);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后客户列表时间的筛选，结果校验");
        }
    }

    /**
     * @description :售后客户查询-筛选栏填写全部参数查询
     * @date :2020/11/25
     **/
    @Test()
    public void selectAfterSleCustomerManageAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.afterSleCustomerManage_pram();
            AfterSleCustomerVariable variable = new AfterSleCustomerVariable();
            JSONArray res = jc.afterSleCustomerManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.vehicle_chassis_code = data.getString(flag[0][1].toString());
                variable.repair_customer_name = data.getString(flag[1][1].toString());
                variable.repair_customer_phone = data.getString(flag[2][1].toString());
                variable.size = "10";
                variable.page="1";
                variable.shop_id = "-1";
                //全部筛选之后的结果
                JSONObject result = jc.afterSleCustomerManage(variable).getJSONArray("list").getJSONObject(1);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.vehicle_chassis_code), "参数全部输入的查询的" + variable.vehicle_chassis_code + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.repair_customer_name), "参数全部输入的查询的" + variable.repair_customer_name + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.repair_customer_phone), "参数全部输入的查询的" + variable.repair_customer_phone + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后客户客户查询填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :售后客户查询-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void selectAfterSleCustomerManageFSomeilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.afterSleCustomerManage_pram();
            AfterSleCustomerVariable variable = new AfterSleCustomerVariable();
            JSONArray res = jc.afterSleCustomerManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.vehicle_chassis_code = data.getString(flag[0][1].toString());
                variable.repair_customer_name = data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.shop_id = "-1";
                //全部筛选之后的结果
                JSONArray result = jc.afterSleCustomerManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.vehicle_chassis_code), "参数全部输入的查询的" + variable.vehicle_chassis_code + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString()) + "不一致");
                    Preconditions.checkArgument(result.getJSONObject(i).getString(flag[1][1].toString()).contains(variable.repair_customer_name), "参数全部输入的查询的" + variable.repair_customer_name + "与列表信息的第一行的" + result.getJSONObject(i).getString(flag[2][1].toString()) + "不一致");
                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后客户客户查询填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :售后客户查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void selectAfterSleCustomerManageEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.afterSleCustomerManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("售后客户列表参数不填写，结果校验");
        }
    }

    /**
     * @description :小程序客户查询-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_weChatSleCustomerManageFilter", dataProviderClass = Constant.class)
    public void weChatSleCustomerManageOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.weChatSleCustomerManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(pram);
                JSONObject respon1 = jc.weChatSleCustomerManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.weChatSleCustomerManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "小程序客户管理按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户小程序客户单项查询，结果校验");
        }
    }

    /**
     * @description 小程序查询-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void weChatSleCustomerManageTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-10);
            String endTime=  dt.getHistoryDate(10);
            JSONObject respon=jc.weChatSleCustomerTimeManage("","1","10",startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.weChatSleCustomerTimeManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String createDate = list.getJSONObject(i).getString("create_date");
                    Preconditions.checkArgument(createDate.compareTo(startTime)>=0&&createDate.compareTo(endTime)<=0, "小程序客户注册开始时间："+startTime+" 注册结束时间："+endTime+" 列表中的注册时间为："+createDate);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序客户列表时间的筛选，结果校验");
        }
    }

    /**
     * @description :小程序客户查询-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void weChatSleCustomerManageAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.weChatSleCustomerManage_pram();
            weChatSleCustomerVariable variable = new weChatSleCustomerVariable();
            JSONArray res = jc.weChatSleCustomerManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.customer_phone = data.getString(flag[0][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.shop_id = "-1";
                //全部筛选之后的结果
                JSONObject result = jc.weChatSleCustomerManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(flag[0][1].toString()).contains(variable.customer_phone), "参数全部输入的查询的" + variable.customer_phone + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户小程序客户填写全部参数查询，结果校验");
        }
    }


    /**
     * @description :小程序客户查询-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test
    public void weChatSleCustomerManageSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.weChatSleCustomerManage_pram();
            weChatSleCustomerVariable variable = new weChatSleCustomerVariable();
            JSONArray res = jc.weChatSleCustomerManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.customer_phone = data.getString(flag[0][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.shop_id = "-1";

                //全部筛选之后的结果
                JSONArray result = jc.weChatSleCustomerManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(flag[0][1].toString()).contains(variable.customer_phone), "参数全部输入的查询的" + variable.customer_phone + "与列表信息的第一行的" + result.getJSONObject(i).getString(flag[0][1].toString()) + "不一致");
                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户小程序客户填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :客户小程序查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void weChatSleCustomerManageEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.afterSleCustomerManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客户小程序列表参数不填写，结果校验");
        }
    }

    /**
     * @description :预约记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_appointmentRecordFilter", dataProviderClass = Constant.class)
    public void appointmentRecordOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.appointmentRecordManage("", "1", "10", "", "");
            String confirmStatus = respon.getJSONArray("list").getJSONObject(0).getString("appointment_status_name");
            String status = messageFormCustomerTurnMethod("MAINTAIN_CONFIRM_STATUS", confirmStatus);
            String result = null;
            if (respon.getJSONArray("list").size() > 0) {
                if (pram.equals("confirm_status")) {
                    JSONObject respon1 = jc.appointmentRecordManage("", "1", "10", pram, status);
                    int pages = respon1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.appointmentRecordManage("", String.valueOf(page), "10", pram, status).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = jc.appointmentRecordManage("", String.valueOf(page), String.valueOf(list.size()), pram, status).getJSONArray("list").getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(confirmStatus), "预约记录管理按" + confirmStatus + "查询，结果错误" + Flag);
                        }
                    }
                } else {
                    result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                    JSONObject respon1 = jc.appointmentRecordManage("", "1", "10", pram, result);
                    int pages = respon1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.appointmentRecordManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag=list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "预约记录管理按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }

            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约记录单项查询，结果校验");
        }
    }

    /**
     * @description 预约记录查询-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void appointmentRecordTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-10);
            String endTime=  dt.getHistoryDate(10);
            JSONObject respon=jc.appointmentRecordTimeManage("","1","10",startTime,endTime,startTime,endTime,startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.appointmentRecordTimeManage("", String.valueOf(page),"10",startTime,endTime,startTime,endTime,startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String createDate = list.getJSONObject(i).getString("create_date");
                    String arriveTime = list.getJSONObject(i).getString("arrive_time");
                    String confirmTime = list.getJSONObject(i).getString("confirm_time");
                    Preconditions.checkArgument(confirmTime.compareTo(startTime)>=0&&confirmTime.compareTo(endTime)<=0, "预约记录确认开始时间："+startTime+" 确认结束时间："+endTime+" 列表中的确认时间为："+confirmTime);
                    Preconditions.checkArgument(createDate.compareTo(startTime)>=0&&createDate.compareTo(endTime)<=0, "预约记录创建开始时间："+startTime+" 注册结束时间："+endTime+" 列表中的创建时间为："+createDate);
                    Preconditions.checkArgument(arriveTime.compareTo(startTime)>=0&&arriveTime.compareTo(endTime)<=0, "预约记录预约开始时间："+startTime+" 预约结束时间："+endTime+" 列表中的预约时间为："+arriveTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约记录列表时间的筛选，结果校验");
        }
    }

    /**
     * @description :预约记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void appointmentRecordAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.appointmentRecordFilter_pram();
            appointmentRecordVariable variable = new appointmentRecordVariable();
            JSONArray res = jc.appointmentRecordManage("", "1", "10", "", "").getJSONArray("list");
            String confirmStatus = res.getJSONObject(0).getString("appointment_status_name");
            String status = messageFormCustomerTurnMethod("MAINTAIN_CONFIRM_STATUS", confirmStatus);
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.plate_number = data.getString(flag[0][1].toString());
                variable.customer_manager = data.getString(flag[1][1].toString());
                variable.shop_id = data.getString(flag[2][1].toString());
                variable.customer_name = data.getString(flag[3][1].toString());
                variable.confirm_status = status;
                variable.customer_phone = data.getString(flag[5][1].toString());
                variable.is_overtime = data.getString(flag[6][1].toString());
                variable.page = "1";
                variable.size = "10";

                //全部筛选之后的结果
                JSONObject result = jc.appointmentRecordManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.plate_number), "参数全部输入的查询的" + variable.plate_number + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.customer_manager), "参数全部输入的查询的" + variable.customer_manager + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(variable.shop_id), "参数全部输入的查询的" + variable.shop_id + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[3][1].toString()).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[4][1].toString()).contains(confirmStatus), "参数全部输入的查询的" + result.getString(flag[4][1].toString()) + "与列表信息的第一行的" + confirmStatus + "不一致");
                Preconditions.checkArgument(result.getString(flag[5][1].toString()).contains(variable.customer_phone), "参数全部输入的查询的" + variable.customer_phone + "与列表信息的第一行的" + result.getString(flag[5][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[6][1].toString()).contains(variable.is_overtime), "参数全部输入的查询的" + variable.is_overtime + "与列表信息的第一行的" + result.getString(flag[6][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约记录填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :预约记录-筛选栏填写参数多项查询
     * @date :2020/11/28
     **/
    @Test()
    public void appointmentRecordSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.appointmentRecordFilter_pram();
            appointmentRecordVariable variable = new appointmentRecordVariable();
            JSONArray res = jc.appointmentRecordManage("", "1", "10", "", "").getJSONArray("list");
            String confirmStatus = res.getJSONObject(0).getString("appointment_status_name");
            String status = messageFormCustomerTurnMethod("MAINTAIN_CONFIRM_STATUS", confirmStatus);
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.plate_number = data.getString(flag[0][1].toString());
                variable.customer_manager = data.getString(flag[1][1].toString());
                variable.shop_id = data.getString(flag[2][1].toString());
                variable.customer_name = data.getString(flag[3][1].toString());
                variable.confirm_status = status;
                variable.page = "1";
                variable.size = "10";

                //全部筛选之后的结果
                JSONObject result = jc.appointmentRecordManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.plate_number), "参数全部输入的查询的" + variable.plate_number + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.customer_manager), "参数全部输入的查询的" + variable.customer_manager + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(variable.shop_id), "参数全部输入的查询的" + variable.shop_id + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[3][1].toString()).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[4][1].toString()).contains(confirmStatus), "参数全部输入的查询的" + result.getString(flag[4][1].toString()) + "与列表信息的第一行的" + confirmStatus + "不一致");
               System.out.println("参数全部输入的查询的" + variable.shop_id + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约记录填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :预约记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void appointmentRecordEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appointmentRecordManage("", "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约记录列表参数不填写，结果校验");
        }
    }

    /**
     * @description :保养配置-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_maintainFilter", dataProviderClass = Constant.class)
    public void maintainOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.maintainFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.maintainFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.maintainFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "保养配置按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("保养配置单项查询，结果校验");
        }
    }

    /**
     * @description :保养配置-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test
    public void maintainALLFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.maintainFilter_pram();
            maintainVariable variable = new maintainVariable();
            JSONArray res = jc.maintainFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.brand_name = data.getString(flag[0][1].toString());
                variable.manufacturer = data.getString(flag[1][1].toString());
                variable.car_model = data.getString(flag[2][1].toString());
                variable.year = data.getString(flag[3][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.shop_id = "-1";
                //全部筛选之后的结果
                JSONObject result = jc.maintainFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.brand_name), "参数全部输入的查询的" + variable.brand_name + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.manufacturer), "参数全部输入的查询的" + variable.manufacturer + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.car_model), "参数全部输入的查询的" + variable.car_model + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.year), "参数全部输入的查询的" + variable.year + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("保养配置填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :保养配置-筛选栏填写参数多项查询
     * @date :2020/11/28
     **/
    @Test()
    public void maintainSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.maintainFilter_pram();
            maintainVariable variable = new maintainVariable();
            JSONArray res = jc.maintainFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.brand_name = data.getString(flag[0][1].toString());
                variable.manufacturer = data.getString(flag[1][1].toString());
                variable.car_model = data.getString(flag[2][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.shop_id = "-1";

                //全部筛选之后的结果
                JSONArray result = jc.maintainFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.brand_name), "参数全部输入的查询的" + variable.brand_name + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString() + "不一致"));
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.manufacturer), "参数全部输入的查询的" + variable.manufacturer + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString() + "不一致"));
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(variable.car_model), "参数全部输入的查询的" + variable.car_model + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[2][1].toString() + "不一致"));

                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("保养配置填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :保养配置查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void maintainEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.maintainFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("保养配置列表参数不填写，结果校验");
        }
    }

    /**
     * @description :卡券管理-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_voucherFormFilter", dataProviderClass = Constant.class)
    public void voucherFormOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.voucherFormFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.voucherFormFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.voucherFormFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "卡券管理管理按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("卡券管理单项查询，结果校验");
        }
    }

    /**
     * @description :卡券管理-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void voucherFormAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.voucherFormFilter_pram();
            voucherFormVariable variable = new voucherFormVariable();
            JSONArray res = jc.voucherFormFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
//                variable.subject_name=data.getString(flag[0][1].toString());
                variable.voucher_name = data.getString(flag[1][1].toString());
                variable.creator = data.getString(flag[2][1].toString());
                variable.is_diff = data.getString(flag[3][1].toString());
                variable.is_self_verification = data.getString(flag[4][1].toString());
                variable.voucher_status = data.getString(flag[5][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.voucherFormFilterManage(variable).getJSONArray("list").getJSONObject(0);
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.subject_name),"参数全部输入的查询的"+variable.subject_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.voucher_name), "参数全部输入的查询的" + variable.voucher_name + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.creator), "参数全部输入的查询的" + variable.creator + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.is_diff), "参数全部输入的查询的" + variable.is_diff + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[4][1])).contains(variable.is_self_verification), "参数全部输入的查询的" + variable.is_self_verification + "与列表信息的第一行的" + result.getString(flag[4][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[5][1])).contains(variable.voucher_status), "参数全部输入的查询的" + variable.voucher_status + "与列表信息的第一行的" + result.getString(flag[5][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("卡券管理填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :卡券管理-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void voucherFormSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.voucherFormFilter_pram();
            voucherFormVariable variable = new voucherFormVariable();
            JSONArray res = jc.voucherFormFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
//                variable.subject_name=data.getString(flag[0][1].toString());
                variable.voucher_name = data.getString(flag[1][1].toString());
                variable.creator = data.getString(flag[2][1].toString());
                variable.is_diff = data.getString(flag[3][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.voucherFormFilterManage(variable).getJSONArray("list").getJSONObject(0);
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.subject_name),"参数全部输入的查询的"+variable.subject_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.voucher_name), "参数全部输入的查询的" + variable.voucher_name + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.creator), "参数全部输入的查询的" + variable.creator + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.is_diff), "参数全部输入的查询的" + variable.is_diff + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("卡券管理填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :卡券管理查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void voucherFormEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.voucherFormFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("卡券管理列表参数不填写，结果校验");
        }
    }

    /**
     * @description :发卡记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_sendRecordFilter", dataProviderClass = Constant.class)
    public void sendRecordOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.sendRecordFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.sendRecordFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.sendRecordFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        System.out.println("----------" + "发卡记录管理按" + result + "查询，结果错误" + Flag);
                        Preconditions.checkArgument(Flag.contains(result), "发卡记录管理按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("发卡记录单项查询，结果校验");
        }
    }

    /**
     * @description 发卡记录查询-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void sendRecordTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-5);
            String endTime=  dt.getHistoryDate(5);
            JSONObject respon=jc.sendRecordFilterTimeManage("","1","10",startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.sendRecordFilterTimeManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String sendTime = list.getJSONObject(i).getString("send_time");
                    Preconditions.checkArgument(sendTime.compareTo(startTime)>=0&&sendTime.compareTo(endTime)<=0, "发卡开始时间："+startTime+" 发卡结束时间："+endTime+" 列表中发放时间为："+sendTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("预约记录列表时间的筛选，结果校验");
        }
    }

    /**
     * @description :发卡记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void sendRecordAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.sendRecordFilter_pram();
            sendRecordVariable variable = new sendRecordVariable();
            JSONArray res = jc.sendRecordFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.voucher_name = data.getString(flag[0][1].toString());
                variable.sender = data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.sendRecordFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(flag[0][1].toString()).contains(variable.voucher_name), "参数全部输入的查询的" + variable.voucher_name + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.sender), "参数全部输入的查询的" + variable.sender + "与列表信息的第一行的" + result.getString(flag[1][1].toString() + "不一致"));
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("发卡记录填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :发卡记录-筛选栏填写多数参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void sendRecordSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.sendRecordFilter_pram();
            sendRecordVariable variable = new sendRecordVariable();
            JSONArray res = jc.sendRecordFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.voucher_name = data.getString(flag[0][1].toString());
                variable.sender = data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "10";

                //全部筛选之后的结果
                JSONArray result = jc.sendRecordFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(flag[0][1].toString()).contains(variable.voucher_name), "参数全部输入的查询的" + variable.voucher_name + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString() + "不一致"));
                    Preconditions.checkArgument(result.getJSONObject(i).getString(flag[1][1].toString()).contains(variable.sender), "参数全部输入的查询的" + variable.sender + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString() + "不一致"));
                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("发卡记录填写多数参数查询，结果校验");
        }
    }

    /**
     * @description :发卡记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void sendRecordFilterEmptyEmpty() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.sendRecordFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("发卡记录列表参数不填写，结果校验");
        }
    }

    /**
     * @description :核销记录-筛选栏单项查询-----发券者列表中不存在，传入值和返回值不一致，没有校验
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_verificationRecordFilter", dataProviderClass = Constant.class)
    public void verificationRecordOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.verificationReordFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.verificationReordFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.verificationReordFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "核销记录管理按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("核销记录单项查询，结果校验");
        }
    }

    /**
     * @description 核销记录查询-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void verificationRecordTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-5);
            String endTime=  dt.getHistoryDate(5);
            JSONObject respon=jc.verificationReordTimeFilterManage("","1","10",startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.verificationReordTimeFilterManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String verificationTime = list.getJSONObject(i).getString("verification_time");
                    Preconditions.checkArgument(verificationTime.compareTo(startTime)>=0&&verificationTime.compareTo(endTime)<=0, "核销开始时间："+startTime+" 核销结束时间："+endTime+" 列表中核销时间为："+verificationTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("核销记录列表时间的筛选，结果校验");
        }
    }

    /**
     * @description :核销记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void verificationRecordAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.verificationRecordFilter_pram();
            verificationRecordVariable variable = new verificationRecordVariable();
            JSONArray res = jc.verificationReordFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.voucher_name = data.getString(flag[0][1].toString());
                variable.page = "1";
                variable.size = "10";


                //全部筛选之后的结果
                JSONObject result = jc.verificationReordFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.voucher_name), "参数全部输入的查询的" + variable.voucher_name + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));
          } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("核销记录填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :核销记录-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void verificationRecordSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.verificationRecordFilter_pram();
            verificationRecordVariable variable = new verificationRecordVariable();
            JSONArray res = jc.verificationReordFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.voucher_name = data.getString(flag[0][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONArray result = jc.verificationReordFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.voucher_name), "参数全部输入的查询的" + variable.voucher_name + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString() + "不一致"));
             }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("核销记录填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :核销记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void verificationRecordEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.verificationReordFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("核销记录列表参数不填写，结果校验");
        }
    }

    /**
     * @description :核销人员记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_verificationPeopleFilter", dataProviderClass = Constant.class)
    public void verificationPeopleOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.verificationPeopleFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(pram);
                JSONObject respon1 = jc.verificationPeopleFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.verificationPeopleFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "核销记录管理按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("核销记录单项查询，结果校验");
        }
    }

    /**
     * @description :核销人员记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void verificationPeopleAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.verificationPeopleFilter_pram();
            verificationPeopleVariable variable = new verificationPeopleVariable();
            JSONArray res = jc.verificationPeopleFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.verification_person = data.getString(flag[0][1].toString());
                variable.verification_phone = data.getString(flag[1][1].toString());
                variable.verification_code = data.getString(flag[2][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.verificationPeopleFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.verification_person), "参数全部输入的查询的" + variable.verification_person + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.verification_phone), "参数全部输入的查询的" + variable.verification_phone + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.verification_code), "参数全部输入的查询的" + variable.verification_code + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");

            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("核销人员记录填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :核销人员记录-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void verificationPeopleSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.verificationPeopleFilter_pram();
            verificationPeopleVariable variable = new verificationPeopleVariable();
            JSONArray res = jc.verificationPeopleFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.verification_person = data.getString(flag[0][1].toString());
                variable.verification_phone = data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONArray result = jc.verificationPeopleFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {

                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.verification_person), "参数全部输入的查询的" + variable.verification_person + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString()) + "不一致");
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.verification_phone), "参数全部输入的查询的" + variable.verification_phone + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString()) + "不一致");
                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("核销人员记录填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :核销人员记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void verificationPeopleEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.verificationPeopleFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("核销人员记录参数不填写，结果校验");
        }
    }

    /**
     * @description :套餐表单-筛选栏单项查询----时间和归属门店没有调
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_packageFormFilter", dataProviderClass = Constant.class)
    public void packageFormOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.packageFormFilterManage("", "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.packageFormFilterManage("", "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.packageFormFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "套餐表单管理按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("套餐表单单项查询，结果校验");
        }
    }

    /**
     * @description 套餐表单查询-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void packageFormTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-5);
            String endTime=  dt.getHistoryDate(5);
            JSONObject respon=jc.packageFormTimeFilterManage("","1","10",startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.packageFormTimeFilterManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String createTime = list.getJSONObject(i).getString("create_time");
                    Preconditions.checkArgument(createTime.compareTo(startTime)>=0&&createTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime+" 列表中创建时间为："+createTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("套餐表单列表时间的筛选，结果校验");
        }
    }

    /**
     * @description :套餐表单-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void packageFormAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.packageFormFilter_pram();
            packageFormVariable variable = new packageFormVariable();
            JSONArray res = jc.packageFormFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.package_name = data.getString(flag[0][1].toString());
                variable.creator = data.getString(flag[1][1].toString());
                variable.package_status = data.getString(flag[2][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.packageFormFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.package_name), "参数全部输入的查询的" + variable.package_name + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.creator), "参数全部输入的查询的" + variable.creator + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.package_status), "参数全部输入的查询的" + variable.package_status + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
            }
            else{
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("套餐表单填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :套餐表单-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void packageFormSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.packageFormFilter_pram();
            packageFormVariable variable = new packageFormVariable();
            JSONArray res = jc.packageFormFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.package_name = data.getString(flag[0][1].toString());
                variable.creator = data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONArray result = jc.packageFormFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.package_name), "参数全部输入的查询的" + variable.package_name + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString() + "不一致"));
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.creator), "参数全部输入的查询的" + variable.creator + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString() + "不一致"));
                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("套餐表单填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :套餐表单查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void packageFormEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.packageFormFilterManage("", "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("套餐表单参数不填写，结果校验");
        }
    }

    /**
     * @description :套餐购买记录-筛选栏单项查询---发套餐者搜索，服务端筛选返回有问题,支付类型在5页3行为空
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_buyPackageRecordFilter", dataProviderClass = Constant.class)
    public void buyPackageRecordOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.buyPackageRecordFilterManage("", "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.buyPackageRecordFilterManage("", "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.buyPackageRecordFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "套餐购买管理按" + result + "查询结果" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("套餐购买记录单项查询，结果校验");
        }
    }

    /**
     * @description 套餐购买记录查询-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void buyPackageRecordTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-5);
            String endTime=  dt.getHistoryDate(5);
            JSONObject respon=jc.buyPackageRecordFilterTimeManage("","1","10",startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.buyPackageRecordFilterTimeManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String sendTime = list.getJSONObject(i).getString("send_time");
                    Preconditions.checkArgument(sendTime.compareTo(startTime)>=0&&sendTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime+" 列表中时间发出为："+sendTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("套餐购买记录列表时间的筛选，结果校验");
        }
    }

    /**
     * @description :套餐购买记录-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void buyPackageRecordAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.buyPackageRecordFilter_pram();
            buyPackageVariable variable = new buyPackageVariable();
            JSONArray res = jc.buyPackageRecordFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.package_name = data.getString(flag[0][1].toString());
                variable.send_type = data.getString(flag[1][1].toString());
                variable.sender = data.getString(flag[2][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.buyPackageRecordFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.package_name), "参数全部输入的查询的" + variable.package_name + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.send_type), "参数全部输入的查询的" + variable.send_type + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.sender), "参数全部输入的查询的" + variable.sender + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("套餐购买记录填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :套餐购买记录-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void buyPackageRecordSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.buyPackageRecordFilter_pram();
            buyPackageVariable variable = new buyPackageVariable();
            JSONArray res = jc.buyPackageRecordFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.package_name = data.getString(flag[0][1].toString());
                variable.send_type = data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONArray result = jc.buyPackageRecordFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.package_name), "参数全部输入的查询的" + variable.package_name + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString()) + "不一致");
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.send_type), "参数全部输入的查询的" + variable.send_type + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString()) + "不一致");

                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("套餐购买记录填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :套餐购买记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void buyPackageRecordEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.buyPackageRecordFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("套餐购买记录参数不填写，结果校验");
        }
    }

    /**
     * @description :消息表单-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_messageFormFilter", dataProviderClass = Constant.class)
    public void messageFormOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.messageFormFilterManage("", "1", "10", "", "");
            String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
            String sendAccountRespon = respon.getJSONArray("list").getJSONObject(0).getString("message_type_name");
            String sendAccount = messageFormCustomerTurnMethod("MESSAGE_TYPE_LIST", sendAccountRespon);
            if (pram.equals("message_type")) {
                JSONObject respon1 = jc.messageFormFilterManage("", "1", "10", pram, sendAccount);
                if (respon1.getJSONArray("list").size() > 0 && sendAccountRespon != null){
                    int pages = respon1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.messageFormFilterManage("", String.valueOf(page), "10", pram, sendAccount).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(sendAccountRespon), "消息表单管理按" + sendAccount + "查询结果" + Flag);
                        }
                    }
                } else if (result != null) {
                Preconditions.checkArgument(respon1.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

            }else{
                JSONObject respon1 = jc.messageFormFilterManage("", "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                if (respon1.getJSONArray("list").size() > 0 && result != null){
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.messageFormFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "消息表单管理按" + result + "查询结果" + Flag);
                        }
                    }
                }else if (result != null) {
                    Preconditions.checkArgument(respon1.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息表单单项查询，结果校验");
        }
    }

    /**
     * @description 消息表单-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void messageFormTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-2);
            String endTime=  dt.getHistoryDate(2);
            JSONObject respon=jc.messageFormTimeFilterManage("","1","10",startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.messageFormTimeFilterManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String pushTime = list.getJSONObject(i).getString("push_time");
                    Preconditions.checkArgument(pushTime.compareTo(startTime)>=0&&pushTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime+" 推送时间为："+pushTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息表单的时间的筛选，结果校验");
        }
    }

    /**
     * @description :消息表单-筛选栏填写全部参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void messageFormAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.messageFormFilter_pram();
            messageFormVariable variable = new messageFormVariable();
            JSONArray res = jc.messageFormFilterManage("", "1", "10", "", "").getJSONArray("list");
           if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.shop_id = data.getString(flag[0][1].toString());
                variable.customer_name = data.getString(flag[1][1].toString());
                variable.send_account = data.getString(flag[3][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.messageFormFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.shop_id), "参数全部输入的查询的" + variable.shop_id + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.send_account), "参数全部输入的查询的" + variable.send_account + "与列表信息的第一行的" + result.getString(flag[3][1].toString() + "不一致"));

            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
//            saveData("消息表单填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :消息表单-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void messageFormSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.messageFormFilter_pram();
            messageFormVariable variable = new messageFormVariable();
            JSONArray res = jc.messageFormFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.shop_id = data.getString(flag[0][1].toString());
                variable.customer_name = data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONArray result = jc.messageFormFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.shop_id), "参数全部输入的查询的" + variable.shop_id + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString()) + "不一致");
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString()) + "不一致");
                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息表单填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :消息表单查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void messageFormEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.messageFormFilterManage("", "1", "10", "", "").getJSONArray("list");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息表单参数不填写，结果校验");
        }
    }

    /**
     * @description :内容管理-文章表单-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_articleFilter", dataProviderClass = Constant.class)
    public void articleOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.articleFilterManage(shopId, "1", "10", "", "");
            String result = respon.getJSONArray("list").getJSONObject(0).getString(pram);
            JSONObject tt = jc.articleFilterManage(shopId, "1", "10", pram, result);
            int pages = tt.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.articleFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String Flag = list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "文章表单按" + result + "查询，结果错误" + Flag);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("文章表单单项查询，结果校验");
        }
    }

    /**
     * @description 内容管理-文章表单-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void articleTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-5);
            String endTime=  dt.getHistoryDate(5);
            JSONObject respon=jc.articleTimeFilterManage("","1","10",startTime,endTime,startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.articleTimeFilterManage("", String.valueOf(page),"10",startTime,endTime,startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String startDate = list.getJSONObject(i).getString("start_date");
                    String endDate = list.getJSONObject(i).getString("end_date");
                    String registerEndDate = list.getJSONObject(i).getString("register_end_date");
                    String registerStartDate = list.getJSONObject(i).getString("register_start_date");
                    Preconditions.checkArgument(startDate.compareTo(startTime)>=0&&endDate.compareTo(endTime)<=0&&endDate.compareTo(startTime)>=0&&startDate.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的开始时间:"+startDate+"列表中的结束时间:"+endDate);
                    Preconditions.checkArgument(registerStartDate.compareTo(startTime)>=0&&registerEndDate.compareTo(endTime)<=0&&registerEndDate.compareTo(startTime)>=0&&registerStartDate.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的开始时间:"+startDate+"列表中的结束时间:"+registerEndDate);

                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息管理列表时间的筛选，结果校验");
        }
    }


    /**
     * @description :内容管理-文章表单-筛选栏填写全部参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void articleAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.articleFilter_pram();
            articleVariable variable = new articleVariable();
            JSONArray res = jc.articleFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.title = data.getString(flag[0][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.articleFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.title), "参数全部输入的查询的" + variable.title + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));

            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("文章表单填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :文章表单查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void articleFilterEmpty() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.articleFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("文章表单参数不填写，结果校验");
        }
    }

    /**
     * @description :报名列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_registerListFilter", dataProviderClass = Constant.class)
    public void registerListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.registerListFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(pram);
                JSONObject respon1 = jc.registerListFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.registerListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "报名列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表单项查询，结果校验");
        }
    }

    /**
     * @description 报名列表-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void registerListTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-5);
            String endTime=  dt.getHistoryDate(5);
            JSONObject respon=jc.registerListTimeFilterManage("","1","10",startTime,endTime,startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.registerListTimeFilterManage("", String.valueOf(page),"10",startTime,endTime,startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String startDate = list.getJSONObject(i).getString("start_date");
                    String endDate = list.getJSONObject(i).getString("end_date");
                    String registerEndDate = list.getJSONObject(i).getString("register_end_date");
                    String registerStartDate = list.getJSONObject(i).getString("register_start_date");
                    Preconditions.checkArgument(startDate.compareTo(startTime)>=0&&endDate.compareTo(endTime)<=0&&endDate.compareTo(startTime)>=0&&startDate.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的开始时间:"+startDate+"列表中的结束时间:"+endDate);
                    Preconditions.checkArgument(registerStartDate.compareTo(startTime)>=0&&registerEndDate.compareTo(endTime)<=0&&registerEndDate.compareTo(startTime)>=0&&registerStartDate.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的开始时间:"+startDate+"列表中的结束时间:"+registerEndDate);

                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表时间的筛选，结果校验");
        }
    }

    /**
     * @description :报名列表-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void registerListAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.registerListFilter_pram();
            registerListVariable variable = new registerListVariable();
            JSONArray res = jc.registerListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.title = data.getString(flag[0][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.registerListFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.title), "参数全部输入的查询的" + variable.title + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :报名列表-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void registerListSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.registerListFilter_pram();
            registerListVariable variable = new registerListVariable();
            JSONArray res = jc.registerListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.title = data.getString(flag[0][1].toString());
                variable.page = "1";
                variable.size = "10";

                //全部筛选之后的结果
                JSONArray result = jc.registerListFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
              }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :报名列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void registerListFilterSomeEmpty() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.registerListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表参数不填写，结果校验");
        }
    }

    /**
     * @description :报名审批列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_approvalListFilter", dataProviderClass = Constant.class)
    public void approvalListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = jc.registerListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getInteger("id");
            JSONObject respon = jc.approvalListFilterManage(shopId, "1", "10", id, "", "");
            if (respon.getJSONArray("list").size() > 0) {
                int pages = respon.getInteger("pages");
                if (pram.equals("status")) {
                    String statusNameRespon = respon.getJSONArray("list").getJSONObject(0).getString("status_name");
                    String statusName = messageFormCustomerTurnMethod("ACTIVITY_APPROVAL_STATUS", statusNameRespon);
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.approvalListFilterManage(shopId, String.valueOf(page), "10", id, pram, statusName).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(statusNameRespon), "报名审批按" + statusNameRespon + "查询，结果错误" + Flag);
                        }
                    }
                } else {
                    String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.approvalListFilterManage(shopId, String.valueOf(page), "10", id, pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "报名审批按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名审批单项查询，结果校验");
        }
    }

    /**
     * @description :报名审批列表-筛选栏填写全部参数查询
     * @date :2020/11/24
     **/
    @Test()
    public void approvalListAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.approvalListFilter_pram();
            int id = jc.registerListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getInteger("id");
            approvalListVariable variable = new approvalListVariable();
            JSONArray res = jc.approvalListFilterManage(shopId, "1", "10", id, "", "").getJSONArray("list");
            String statusNameRespon = res.getJSONObject(0).getString("status_name");
            String statusName = messageFormCustomerTurnMethod("ACTIVITY_APPROVAL_STATUS", statusNameRespon);

            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.customer_name = data.getString(flag[0][1].toString());
                variable.phone = data.getString(flag[1][1].toString());
                variable.status = statusName;
                variable.page = "1";
                variable.size = "10";
                variable.article_id = id;
                //全部筛选之后的结果
                JSONObject result = jc.approvalListFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.phone), "参数全部输入的查询的" + variable.phone + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(statusNameRespon), "参数全部输入的查询的" + statusNameRespon + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名审批填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :报名审批列表-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void approvalListSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.approvalListFilter_pram();
            int id = jc.registerListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getInteger("id");
            approvalListVariable variable = new approvalListVariable();
            JSONArray res = jc.approvalListFilterManage(shopId, "1", "10", id, "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.customer_name = data.getString(flag[0][1].toString());
                variable.phone = data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.article_id = id;
                //全部筛选之后的结果
                JSONArray result = jc.approvalListFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString() + "不一致"));
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.phone), "参数全部输入的查询的" + variable.phone + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString() + "不一致"));

                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名审批填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :报名审批查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void approvalListFilterEmpty() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int id = jc.registerListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getInteger("id");
            jc.approvalListFilterManage(shopId, "1", "10", id, "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名审批参数不填写，结果校验");
        }
    }

    /**
     * @description :卡券申请-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_applyListFilter", dataProviderClass = Constant.class)
    public void applyListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.applyListFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                if (pram.equals("status")) {
                    String statusNameRespon = respon.getJSONArray("list").getJSONObject(0).getString("status_name");
                    String statusName = messageFormCustomerTurnMethod("VOUCHER_AUDIT_STATUS_LIST", statusNameRespon);
                    JSONObject respon2 = jc.applyListFilterManage(shopId, "1", "10", pram, statusName);
                    int pages = respon2.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.applyListFilterManage("", String.valueOf(page),"10", pram, statusName).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "卡券申请按" + result + "查询，结果错误" + Flag);
                        }
                    }
                } else {
                    JSONObject respon1 = jc.applyListFilterManage(shopId, "1", "10", pram, result);
                    int pages = respon1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.applyListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "卡券申请按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("卡券申请单项查询，结果校验");
        }
    }

    /**
     * @description 卡券申请-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void applyListTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-5);
            String endTime=  dt.getHistoryDate(5);
            JSONObject respon=jc.applyListTimeFilterManage("","1","10",startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.applyListTimeFilterManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String applyTime = list.getJSONObject(i).getString("apply_time");
                    Preconditions.checkArgument(applyTime.compareTo(startTime)>=0&&applyTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的申请时间:"+applyTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("报名列表时间的筛选，结果校验");
        }
    }
    /**
     * @description :卡券申请-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test
    public void applyListAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.applyListFilter_pram();
            applyListVariable variable = new applyListVariable();
            JSONArray res = jc.applyListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            String statusNameRespon = res.getJSONObject(0).getString("status_name");
            String statusName = messageFormCustomerTurnMethod("VOUCHER_AUDIT_STATUS_LIST", statusNameRespon);

            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.name = data.getString(flag[0][1].toString());
                variable.apply_name = data.getString(flag[1][1].toString());
                variable.status = statusName;
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.applyListFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.name), "参数全部输入的查询的" + variable.name + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.apply_name), "参数全部输入的查询的" + variable.apply_name + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(statusNameRespon), "参数全部输入的查询的" + statusNameRespon + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("卡券申请填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :卡券申请-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void applyListSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.applyListFilter_pram();
            applyListVariable variable = new applyListVariable();
            JSONArray res = jc.applyListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            String statusNameRespon = res.getJSONObject(0).getString("status_name");
            String statusName = messageFormCustomerTurnMethod("VOUCHER_AUDIT_STATUS_LIST", statusNameRespon);

            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.name = data.getString(flag[0][1].toString());
                variable.apply_name = data.getString(flag[1][1].toString());
                variable.status = statusName;
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.applyListFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.name), "参数全部输入的查询的" + variable.name + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.apply_name), "参数全部输入的查询的" + variable.apply_name + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(statusNameRespon), "参数全部输入的查询的" + statusNameRespon + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");

            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("卡券申请填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :卡券申请查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void voidapplyListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.applyListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("卡券申请参数不填写，结果校验");
        }
    }

    /**
     * @description :门店列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_shopListFilter", dataProviderClass = Constant.class)
    public void shopListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.shopListFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(pram);
                JSONObject respon1 = jc.shopListFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.shopListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "门店列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店列表单项查询，结果校验");
        }
    }

    /**
     * @description :门店列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void shopListAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.shopListFilter_pram();
            JSONArray res = jc.shopListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                String name = data.getString(flag[0][1].toString());

                //全部筛选之后的结果
                JSONObject result = jc.shopListFilterManage(shopId, "1", "10", name).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(name), "参数全部输入的查询的" + result.getString(flag[0][1].toString() + "与列表信息的第一行的" + name + "不一致"));
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店列表填写全部参数查询，结果校验");
        }
    }


    /**
     * @description :门店列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void shopListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.shopListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店列表参数不填写，结果校验");
        }
    }

    /**
     * @description :品牌列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_brandListFilter", dataProviderClass = Constant.class)
    public void brandListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.brandListFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.brandListFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.brandListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "品牌列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("品牌列表单项查询，结果校验");
        }
    }

    /**
     * @description :品牌列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void brandListAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.brandListFilter_pram();
            JSONArray res = jc.brandListFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                String name = data.getString(flag[0][1].toString());
                String firstLetter = data.getString(flag[1][1].toString());

                //全部筛选之后的结果
                JSONObject result = jc.brandListFilterManage2(shopId, "1", "10", name, firstLetter).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(name.contains(result.getString(String.valueOf(flag[0][1]))), "参数全部输入的查询的" + result.getString(flag[0][1].toString() + "与列表信息的第一行的" + name) + "不一致");
                Preconditions.checkArgument(firstLetter.contains(result.getString(String.valueOf(flag[1][1]))), "参数全部输入的查询的" + result.getString(flag[1][1].toString() + "与列表信息的第一行的" + firstLetter) + "不一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("品牌列表填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :品牌列表-筛选栏填写部分参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void brandListSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.brandListFilter_pram();
            JSONArray res = jc.brandListFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                String name = data.getString(flag[0][1].toString());

                //全部筛选之后的结果
                JSONObject result = jc.brandListFilterManage3(shopId, "1", "10", name).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(name.contains(result.getString(String.valueOf(flag[0][1]))), "参数全部输入的查询的" + result.getString(flag[0][1].toString() + "与列表信息的第一行的" + name) + "不一致");
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("品牌列表填写全部参数查询，结果校验");
        }
    }


    /**
     * @description :品牌列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void brandListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.brandListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("品牌列表参数不填写，结果校验");
        }
    }

    /**
     * @description :车系列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_carStyleListFilter", dataProviderClass = Constant.class)
    public void carStyleListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String id = jc.brandListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("id");
            JSONObject respon = jc.carStyleListFilterManage(shopId, "1", "10", id, "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(pram);
                JSONObject respon1 = jc.carStyleListFilterManage(shopId, "1", "10", id, pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.carStyleListFilterManage("", String.valueOf(page),"10", id, pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "车系列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("车系列表单项查询，结果校验");
        }
    }

    /**
     * @description :车系列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void carStyleListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String id = jc.brandListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("id");
            jc.carStyleListFilterManage(shopId, "1", "10", id, "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("车系列表参数不填写，结果校验");
        }
    }

    /**
     * @description :车型列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_carModelListFilter", dataProviderClass = Constant.class)
    public void carModelListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String brand_id = jc.brandListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("id");
            String style_id = jc.carStyleListFilterManage(shopId, "1", "10", "", brand_id).getJSONArray("list").getJSONObject(0).getString("id");
            JSONObject respon = jc.carModelListFilterManage1(shopId, "1", "10", "", "", brand_id, style_id);
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.carModelListFilterManage(shopId, "1", "10", brand_id, style_id, pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.carModelListFilterManage("", String.valueOf(page),"10", brand_id, style_id,pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++)
                    {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "车型列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("车型列表单项查询，结果校验");
        }
    }

    /**
     * @description :车型列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void carModelListAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.carModelListFilter_pram();
            String brand_id = jc.brandListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("id");
            String style_id = jc.carStyleListFilterManage(shopId, "1", "10", "", brand_id).getJSONArray("list").getJSONObject(0).getString("id");
            JSONObject res = jc.carModelListFilterManage1(shopId, "1", "10", "", "", brand_id, style_id);
            JSONArray list=res.getJSONArray("list");
            if (list.size() > 0) {
                JSONObject data = res.getJSONArray("list").getJSONObject(0);
                String name = data.getString(flag[0][1].toString());
                String year = data.getString(flag[1][1].toString());
                //全部筛选之后的结果
                JSONObject result = jc.carModelListFilterManage1(shopId, "1", "10", name, year, brand_id, style_id).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(name), "参数全部输入的查询的" + name + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(year), "参数全部输入的查询的" + year + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(list.size()== 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("车型列表填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :车型列表-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void carModelListSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.carModelListFilter_pram();
            String brand_id = jc.brandListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("id");
            String style_id = jc.carStyleListFilterManage(shopId, "1", "10", "", brand_id).getJSONArray("list").getJSONObject(0).getString("id");
            JSONObject res = jc.carModelListFilterManage1(shopId, "1", "10", "", "", brand_id, style_id);
            JSONArray list=res.getJSONArray("list");
            if (list.size() > 0) {
                JSONObject data = res.getJSONArray("list").getJSONObject(0);
                String name = data.getString(flag[0][1].toString());
                //全部筛选之后的结果
                JSONObject result = jc.carModelListFilterManage1(shopId, "1", "10", name, "", brand_id, style_id).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(name), "参数全部输入的查询的" + name + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(list.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("车型列表填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :车型列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void carModelListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String brand_id = jc.brandListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("id");
            String style_id = jc.carStyleListFilterManage(shopId, "1", "10", brand_id, "", "").getJSONArray("list").getJSONObject(0).getString("brand_id");
            jc.carModelListFilterManage1(shopId, "1", "10", "", "", brand_id, style_id).getJSONArray("list");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("车型列表参数不填写，结果校验");
        }
    }

    /**
     * @description :角色列表-筛选栏单项查询
     * @date :2020/11/27
     **/
    @Test(dataProvider = "SELECT_roleListFilter", dataProviderClass = Constant.class)
    public void roleListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.roleListFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.roleListFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.roleListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "角色列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("角色列表单项查询，结果校验");
        }
    }


    /**
     * @description :角色列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void roleListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.roleListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("角色列表参数不填写，结果校验");
        }
    }

    /**
     * @description :员工列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_roleListFilter", dataProviderClass = Constant.class)
    public void staffListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.staffListFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(pram);
                JSONObject respon1 = jc.staffListFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.staffListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "员工列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("员工列表单项查询，结果校验");
        }
    }


    /**
     * @description :员工列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void staffListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.staffListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("员工列表参数不填写，结果校验");
        }
    }

    /**
     * @description :导入记录列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_importListFilter", dataProviderClass = Constant.class)
    public void importListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.importListFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respon1 = jc.importListFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.importListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "导入记录按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导入记录列表单项查询，结果校验");
        }
    }

    /**
     * @description 导入记录-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void importListTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-2);
            String endTime=  dt.getHistoryDate(2);
            JSONObject respon=jc.importListTimeFilterManage("","1","10",startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.importListTimeFilterManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String importTime = list.getJSONObject(i).getString("import_time");
                    Preconditions.checkArgument(importTime.compareTo(startTime)>=0&&importTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的导入时间:"+importTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导入记录时间的筛选，结果校验");
        }
    }

    /**
     * @description :导入记录列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void importListAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.importListFilter_pram();
            JSONArray res = jc.importListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                String type = data.getString(flag[0][1].toString());
                String user = data.getString(flag[1][1].toString());
                //全部筛选之后的结果
                JSONObject result = jc.importListFilterManage(shopId, "1", "10", type, user, "").getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(type), "参数全部输入的查询的" + type + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(user), "参数全部输入的查询的" + user + "与列表信息的第一行的" + result.getString(flag[1][1].toString() + "不一致"));

            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导入记录列表填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :导入记录列表-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void importListSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.importListFilter_pram();
            JSONArray res = jc.importListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                String type = data.getString(flag[0][1].toString());
                String user = data.getString(flag[1][1].toString());

                //全部筛选之后的结果
                JSONArray result = jc.importListFilterManage(shopId, "1", "10", type, user, "").getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(type), "参数全部输入的查询的" + type + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString() + "不一致"));
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(user), "参数全部输入的查询的" + user + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString() + "不一致"));

                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导入记录列表填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :导入记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void importListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.importListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导入记录数不填写，结果校验");
        }
    }

    /**
     * @description :导出记录列表-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_exportListFilter", dataProviderClass = Constant.class)
    public void exportListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.exportListFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String result = respon.getJSONArray("list").getJSONObject(0).getString(pram);
                JSONObject respon1 = jc.exportListFilterManage(shopId, "1", "10", pram, result);
                int pages = respon1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.exportListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "导出记录按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respon.getJSONArray("list").size() == 0, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出记录列表单项查询，结果校验");
        }
    }

    /**
     * @description :导出记录列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void exportListAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.exportListFilter_pram();
            JSONArray res = jc.exportListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                String type = data.getString(flag[0][1].toString());
                String user = data.getString(flag[1][1].toString());
                String export_time = data.getString(flag[2][1].toString());

                //全部筛选之后的结果
                JSONObject result = jc.exportListFilterManage(shopId, "1", "10", type, user, export_time).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(type), "参数全部输入的查询的" + type + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(user), "参数全部输入的查询的" + user + "与列表信息的第一行的" + result.getString(flag[1][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(export_time), "参数全部输入的查询的" + export_time + "与列表信息的第一行的" + result.getString(flag[2][1].toString() + "不一致"));
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出记录列表填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :导出记录列表-筛选栏填写多项参数查询
     * @date :2020/11/28
     **/
    @Test()
    public void exportListSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.exportListFilter_pram();
            JSONArray res = jc.exportListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                String type = data.getString(flag[0][1].toString());
                String user = data.getString(flag[1][1].toString());

                //全部筛选之后的结果
                JSONArray result = jc.exportListFilterManage(shopId, "1", "10", type, user, "").getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(type), "参数全部输入的查询的" + type + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString() + "不一致"));
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(user), "参数全部输入的查询的" + user + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString() + "不一致"));

                }
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出记录列表填写多项参数查询，结果校验");
        }
    }

    /**
     * @description :导出记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void exportListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.exportListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出记录数不填写，结果校验");
        }
    }

    /**
     * @description :消息记录列表-推送单项查询
     * @date :2020/11/24
     **/
    @Test()
    public void messageFormMessageTypeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            messageFormCustomerMethod("PUSH_REASON_TYPE", "message_type", "message_type_name");
            messageFormCustomerMethod("CUSTOMER_TYPE_LIST", "customer_type", "customer_type_name");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息表单单项查询，结果校验");
        }
    }

    /**
     * @description 消息记录-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void messageFormMessageTypeTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-2);
            String endTime=  dt.getHistoryDate(2);
            JSONObject respon=jc.pushMsgListTimeFilterManage("","1","10",startTime,endTime);
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.pushMsgListTimeFilterManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String sendTime = list.getJSONObject(i).getString("send_time");
                    Preconditions.checkArgument(sendTime.compareTo(startTime)>=0&&sendTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的推送时间:"+sendTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息记录时间的筛选，结果校验");
        }
    }


    /**
     * @description :消息记录列表-单项推送的方法
     * @date :2020/11/24
     **/
    public void messageFormCustomerMethod(String jsonArray, String pram, String fieldReturn) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray result = jc.enummap().getJSONArray(jsonArray);
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < result.size(); i++) {
                String kk = result.getJSONObject(i).getString("key");
                String vv = result.getJSONObject(i).getString("value");
                map.put(kk, vv);
            }
            Set<String> keySet = map.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                JSONObject respon1 = jc.pushMsgListFilterManage("-1", "1", "10", pram, key);
                JSONArray list = respon1.getJSONArray("list");
                String value = map.get(key);
                if (list.size() > 0) {
                    for (int j = 0; j < list.size(); j++) {
                        String resultA = list.getJSONObject(j).getString(fieldReturn);
                        Preconditions.checkArgument(resultA.equals(value), "列表中的结果为：" + resultA + "筛选传入的值为" + value);
                    }
                } else {
                    Preconditions.checkArgument(list.size() == 0, "消息记录列表系统错误,请联系开发人员");
                }
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息表单单项查询，结果校验");
        }
    }

    /**
     * @description :消息记录列表-筛选栏填写全部参数的转换的方法
     * @date :2020/11/27
     **/
    public String messageFormCustomerTurnMethod(String jsonArray, String fieldReturn) {
        logger.logCaseStart(caseResult.getCaseName());
        JSONArray result = jc.enummap().getJSONArray(jsonArray);
        String key = "";
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < result.size(); i++) {
            String kk = result.getJSONObject(i).getString("key");
            String vv = result.getJSONObject(i).getString("value");
            map.put(kk, vv);
        }
        Set<String> keySet = map.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            String value = map.get(key);
            if (value.equals(fieldReturn)) {
                break;
            } else {
                key = "";
            }
        }
        return key;
    }

    /**
     * @description :消息记录列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test()
    public void pushMsgListAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取respon的返回字段
            String messageType = jc.pushMsgListFilterManage("-1", "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("message_type_name");
            String customerType = jc.pushMsgListFilterManage("-1", "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("customer_type_name");
            //获取类型转化方法的字段
            String messageTypeReturn = messageFormCustomerTurnMethod("PUSH_REASON_TYPE", messageType);
            String customerTypeReturn = messageFormCustomerTurnMethod("CUSTOMER_TYPE_LIST", customerType);
            JSONArray res = jc.pushMsgListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                //全部筛选之后的结果
                JSONObject result = jc.pushMsgListFilterManage1(shopId, "1", "10", customerTypeReturn, messageTypeReturn, "").getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString("message_type_name").contains(messageType), "参数全部输入的查询的" + messageType + "与列表信息的第一行的" + result.getString("message_type_name") + "不一致");
                Preconditions.checkArgument(result.getString("customer_type_name").contains(customerType), "参数全部输入的查询的" + customerType + "与列表信息的第一行的" + result.getString("customer_type_name"));
            } else {
                Preconditions.checkArgument(res.size() == 0, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息记录列表填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :消息记录列表查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test
    public void pushMsgListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.pushMsgListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息记录列表数不填写，结果校验");
        }
    }

    /**
     * @description :小程序预约保养.取消保养,确认保养,数据一致性比较
     * @date :2020/11/27
     **/
    @Test(enabled = false)
    public void appletAppointmentMaintain() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取APP的今日任务－全部预约
            Long allAppointmentBefore = jc.appTodayTask().getLong("all_appointment");
            //小程序预约保养
            JSONObject respon = jc.appointmentMaintain(123L, "", 123, 123, "", "");
            //获取APP的今日任务－全部预约,数据应该+1
            Long allAppointmentAfter1 = jc.appTodayTask().getLong("all_appointment");

            Preconditions.checkArgument(allAppointmentAfter1 == allAppointmentBefore + 1, "预约保养前APP全部预约为" + allAppointmentBefore + "  预约保养后的全部预约为" + allAppointmentAfter1 + "  数据有问题");
            //取消预约保养
            jc.appointmentHandle(123L, "10");
            //获取APP的今日任务－全部预约,数据应该-1
            Long allAppointmentAfter2 = jc.appTodayTask().getLong("all_appointment");

            Preconditions.checkArgument(allAppointmentAfter2 == allAppointmentAfter1 - 1, "保养取消前APP全部预约为" + allAppointmentAfter1 + "  取消保养后的全部预约为" + allAppointmentAfter2 + "  数据有问题");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序预约保养.取消保养,确认保养,数据一致性比较");
        }
    }
}
