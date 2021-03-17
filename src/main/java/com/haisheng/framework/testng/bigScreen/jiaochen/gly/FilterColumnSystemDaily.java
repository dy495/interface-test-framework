package com.haisheng.framework.testng.bigScreen.jiaochen.gly;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.Variable.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.util.BusinessUtil;
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
import java.util.*;

public class FilterColumnSystemDaily extends TestCaseCommon implements TestCaseStd {
    ScenarioUtil jc = new ScenarioUtil();
    private static final EnumTestProduce product = EnumTestProduce.JIAOCHEN_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
    BusinessUtil businessUtil=new BusinessUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();
    PublicParm pp = new PublicParm();
    //    JsonPathUtil jpu = new JsonPathUtil();
    public String shopId = "-1";
    public String shopOne="45973";//ALL-1-1门店

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_JIAOCHEN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.product = product.getAbbreviation();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "jc-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.referer = "https://servicewechat.com/wxbd41de85739a00c7/0/page-frame.html";
        commonConfig.shopId = product.getShopId();
        commonConfig.roleId = "603";
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
        jc.pcLogin("13114785236", pp.gwpassword);
    }


    /**
     * @description :接待管理查询-筛选栏参数单项插查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_ReceptionManageFilter", dataProviderClass = Constant.class)
    public void selectAppointmentRecodeOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respond = jc.pcReceptionManagePage("", "1", "10");
            if (respond.getJSONArray("list").size() > 0) {
                if(pram.equals("reception_sale_id")){
                    String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                    String salesId=businessUtil.authNameTransformId(result,"AFTER_SALE_RECEPTION");
                    String name=businessUtil.getAuthNameExist(result,"AFTER_SALE_RECEPTION");
                    JSONObject respond1 = jc.receptionManage("", String.valueOf(1), "10", pram, salesId);
                    int pages=respond1.getInteger("pages") > 10 ? 10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.receptionManage("", String.valueOf(page),"10", pram, salesId).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(flag.contains(name), "接待管理按" + name + "查询，结果错误" + flag);
                        }
                    }
                }else{
                    String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                    JSONObject respond1 = jc.receptionManage("", String.valueOf(1), "10", pram, result);
                    int pages=respond1.getInteger("pages") > 10 ? 10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.receptionManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(flag.contains(result), "接待管理按" + result + "查询，结果错误" + flag);
                        }
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
    @Test(enabled = true)
    public void selectAppointmentRecodeTimeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.receptionTimeManage("","1","10",startTime,endTime,startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            if (respond.getJSONArray("list").size()>0){
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.receptionTimeManage("", String.valueOf(page),"10",startTime,endTime,startTime,endTime).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String receptionDate = list.getJSONObject(i).containsKey("reception_time")?list.getJSONObject(i).getString("reception_time").substring(0,10):startTime;
                        String finishTime =list.getJSONObject(i).containsKey("finish_time")?list.getJSONObject(i).getString("finish_time").substring(0,10):startTime;
                        System.err.println("receptionDate:"+receptionDate);
                        System.err.println(i+"----finishTime:"+finishTime);
                        Preconditions.checkArgument(receptionDate.compareTo(startTime)>=0&&receptionDate.compareTo(endTime)<=0, "接待管理开始时间："+startTime+" 结束时间："+endTime+" 列表中的接待时间为："+receptionDate);
                        Preconditions.checkArgument(finishTime.compareTo(startTime)>=0&&finishTime.compareTo(endTime)<=0, "接待管理开始时间："+startTime+" 结束时间："+endTime+" 列表中的完成时间为："+finishTime);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待管理列表时间的筛选，结果校验");
        }
    }

    /**
     * @description :接待管理-筛选栏参数全填查询
     * @date :2020/11/28
     **/
    @Test(enabled = true)
    public void selectAppointmentRecodeAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] ss = Constant.receptionManageFilter_pram();
            SelectReception sr = new SelectReception();
            JSONArray object = null;
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
                Preconditions.checkArgument(result.getString(ss[0][1].toString()).contains(sr.plate_number), "参数全部输入的查询的" + sr.plate_number + "与列表信息的第一行的" + result.getString(ss[0][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(ss[1][1].toString()).contains(sr.reception_sale_name), "参数全部输入的查询的" + result.getString(ss[1][1].toString() + "与列表信息的第一行的" + sr.reception_sale_name + "不一致"));
                Preconditions.checkArgument(result.getString(ss[2][1].toString()).contains(sr.customer_name), "参数全部输入的查询的" + sr.customer_name + "与列表信息的第一行的" + result.getString(ss[2][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(ss[3][1])).contains(sr.reception_status), "参数全部输入的查询的" + sr.reception_status + "与列表信息的第一行的" + result.getString(ss[3][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(ss[4][1].toString()).contains(sr.customer_phone), "参数全部输入的查询的" + sr.customer_phone + "与列表信息的第一行的" + result.getString(ss[4][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(ss[5][1])).contains(sr.reception_type), "参数全部输入的查询的" + sr.reception_type + "与列表信息的第一行的" + result.getString(String.valueOf(ss[5][1]) + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(ss[6][1])).contains(sr.shop_id), "参数全部输入的查询的" + sr.shop_id + "与列表信息的第一行的" + result.getString(ss[6][1].toString() + "不一致"));
           } else {
                Preconditions.checkArgument(res.toString()==null, "接待列表系统错误,请联系开发人员");
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
    @Test(enabled = true)
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
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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


    /**
     * @description :销售客户查询-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_PreSleCustomerManageFilter", dataProviderClass = Constant.class)
    public void preSleCustomerManageOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respond = jc.preSleCustomerManage(shopId, "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
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
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.preSleCustomerTimeManage("","1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            if(respond.getJSONArray("list").size()>0){
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.preSleCustomerTimeManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String createDate = list.getJSONObject(i).containsKey("create_date")? list.getJSONObject(i).getString("create_date").substring(0,10):startTime;
                        Preconditions.checkArgument(createDate.compareTo(startTime)>=0&&createDate.compareTo(endTime)<=0, "销售客户创建开始时间："+startTime+" 结束时间："+endTime+" 列表中的创建时间为："+createDate);
                    }
                }
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

            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
                Preconditions.checkArgument(res== null, "接待列表系统错误,请联系开发人员");
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
            JSONObject respond = jc.afterSleCustomerManage(shopId, "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                if (result != null) {
                    JSONObject respond1 = jc.afterSleCustomerManage(shopId, "1", "10", pram, result);
                    int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.afterSleCustomerManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "售后客户管理按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
    @Test(enabled = true)
    public void selectAfterSleCustomerManageTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-10);
            String endTime=  dt.getHistoryDate(10);
            JSONObject respond=jc.afterSleCustomerTimeManage("","1","10",startTime,endTime,startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.afterSleCustomerTimeManage("", String.valueOf(page),"10",startTime,endTime,startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String orderDate = list.getJSONObject(i).containsKey("start_order_date")?list.getJSONObject(i).getString("start_order_date").substring(0,10):startTime;
                    String importDate = list.getJSONObject(i).containsKey("import_date")?list.getJSONObject(i).getString("import_date").substring(0,10):startTime;
                    Preconditions.checkArgument(orderDate.compareTo(startTime)>=0&&orderDate.compareTo(endTime)<=0, "订单开始时间："+startTime+" 订单结束时间："+endTime+" 列表中的开单时间为："+orderDate);
                    Preconditions.checkArgument(importDate.compareTo(startTime)>=0&&importDate.compareTo(endTime)<=0, "创建开始时间："+startTime+" 创建结束时间："+endTime+" 列表中的导入时间为："+importDate);
                }
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
//                variable.vehicle_chassis_code = (data.getString(flag[0][1].toString())==null)?"":data.getString(flag[0][1].toString());
                variable.customer_name = data.getString(flag[1][1].toString());
                variable.customer_phone = data.getString(flag[2][1].toString());
                variable.size = "10";
                variable.page="1";
                variable.shop_id = "-1";
                //全部筛选之后的结果
                JSONObject result = jc.afterSleCustomerManage(variable).getJSONArray("list").getJSONObject(0);
                  if(variable.vehicle_chassis_code!=null){
                    Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.vehicle_chassis_code), "参数全部输入的查询的" + variable.vehicle_chassis_code + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                }
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.customer_phone), "参数全部输入的查询的" + variable.customer_phone + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
                variable.customer_name = data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.shop_id = "-1";
                //全部筛选之后的结果
                JSONArray result = jc.afterSleCustomerManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    if(variable.vehicle_chassis_code!=null){
                        Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.vehicle_chassis_code), "参数全部输入的查询的" + variable.vehicle_chassis_code + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString()) + "不一致");
                    }
                    Preconditions.checkArgument(result.getJSONObject(i).getString(flag[1][1].toString()).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第一行的" + result.getJSONObject(i).getString(flag[2][1].toString()) + "不一致");
                }
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
     * @description :小程序客户查询-筛选栏单项查询--增加vip_type筛选项
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_weChatSleCustomerManageFilter", dataProviderClass = Constant.class)
    public void weChatSleCustomerManageOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respond = jc.weChatSleCustomerManage(shopId, "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                if(pram.equals("vip_type")){
                    String result1 = respond.getJSONArray("list").getJSONObject(0).getString(output).equals("vip会员") ? "10" : "1";
                    System.err.println(result1);
                    String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                    JSONObject respond1 = jc.weChatSleCustomerManage(shopId, "1", "10", pram, result1);
                    int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.weChatSleCustomerManage("", String.valueOf(page), "10", pram, result1).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "小程序客户管理按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }else{
                    String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                    JSONObject respond1 = jc.weChatSleCustomerManage(shopId, "1", "10", pram, result);
                    int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.weChatSleCustomerManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "小程序客户管理按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }
                }else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.weChatSleCustomerTimeManage("","1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
           if(respond.getJSONArray("list").size()>0){
               for (int page = 1; page <= pages; page++) {
                   JSONArray list = jc.weChatSleCustomerTimeManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                   for (int i = 0; i < list.size(); i++) {
                       String orderDate = list.getJSONObject(i).containsKey("create_date")?list.getJSONObject(i).getString("create_date").substring(0,10):startTime;
                       System.out.println("订单开始时间："+startTime+" 订单结束时间："+endTime+" 列表中的开单时间为："+orderDate);
                       Preconditions.checkArgument(orderDate.compareTo(startTime)>=0&&orderDate.compareTo(endTime)<=0, "订单开始时间："+startTime+" 订单结束时间："+endTime+" 列表中的开单时间为："+orderDate);
                   }
               }
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
                String type=data.getString(flag[1][1].toString()).equals("vip会员")?"10":"1";
                String type1=data.getString(flag[1][1].toString());
                variable.customer_phone = data.getString(flag[0][1].toString());
                variable.vip_type =type;
                variable.page = "1";
                variable.size = "10";
                variable.shop_id = "-1";
                //全部筛选之后的结果
                JSONObject result = jc.weChatSleCustomerManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(flag[0][1].toString()).contains(variable.customer_phone), "参数全部输入的查询的" + variable.customer_phone + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(type1), "参数全部输入的查询的" + variable.vip_type + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");

            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
     * @description :V2.0预约记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_appointmentRecordFilter", dataProviderClass = Constant.class)
    public void appointmentRecordOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respond = jc.appointmentRecordManage("", "1", "10", "", "");
            String confirmStatus = respond.getJSONArray("list").getJSONObject(0).getString("appointment_status_name");
            String status = messageFormCustomerTurnMethod("MAINTAIN_CONFIRM_STATUS", confirmStatus);
            String result = null;
            if (respond.getJSONArray("list").size() > 0) {
                if (pram.equals("confirm_status")) {
                    JSONObject respond1 = jc.appointmentRecordManage("", "1", "10", pram, status);
                    int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.appointmentRecordManage("", String.valueOf(page), "10", pram, status).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = jc.appointmentRecordManage("", String.valueOf(page), String.valueOf(list.size()), pram, status).getJSONArray("list").getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(confirmStatus), "预约记录管理按" + confirmStatus + "查询，结果错误" + Flag);
                        }
                    }
                } else if(pram.equals("service_sale_id")){
                    result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                    String saleId=businessUtil.authNameTransformId(result,"MAINTAIN_DISTRIBUTION");
                    String name=businessUtil.getAuthNameExist(result,"MAINTAIN_DISTRIBUTION");
                    JSONObject respond1 = jc.appointmentRecordManage("", "1", "10", pram, saleId);
                    int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.appointmentRecordManage("", String.valueOf(page), "10", pram, saleId).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag=list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(name), "预约记录管理按" + name + "查询，结果错误" + Flag);
                        }
                    }
                }else {
                    result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                    JSONObject respond1 = jc.appointmentRecordManage("", "1", "10", pram, result);
                    int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.appointmentRecordManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag=list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "预约记录管理按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }

            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
            JSONObject respond=jc.appointmentRecordTimeManage("","1","10",startTime,endTime,startTime,endTime,startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            if(respond.getJSONArray("list").size()>0){
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.appointmentRecordTimeManage("", String.valueOf(page),"10",startTime,endTime,startTime,endTime,startTime,endTime).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String createDate = list.getJSONObject(i).containsKey("create_date")?list.getJSONObject(i).getString("create_date").substring(0,10):startTime;
                        String confirmTime =list.getJSONObject(i).containsKey("confirm_time")? list.getJSONObject(i).getString("confirm_time").substring(0,10):startTime;
                        Preconditions.checkArgument(confirmTime.compareTo(startTime)>=0&&confirmTime.compareTo(endTime)<=0, "预约记录确认开始时间："+startTime+" 确认结束时间："+endTime+" 列表中的确认时间为："+confirmTime);
                        Preconditions.checkArgument(createDate.compareTo(startTime)>=0&&createDate.compareTo(endTime)<=0, "预约记录创建开始时间："+startTime+" 注册结束时间："+endTime+" 列表中的创建时间为："+createDate);
                    };
                }
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
    @Test(enabled = true)
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
                String saleId=businessUtil.authNameTransformId(data.getString(flag[6][1].toString()),"MAINTAIN_DISTRIBUTION");
                String name=businessUtil.getAuthNameExist(data.getString(flag[6][1].toString()),"MAINTAIN_DISTRIBUTION");
                System.out.println(data.getString(flag[6][1].toString())+"----------"+name);
                variable.plate_number = data.getString(flag[0][1].toString());
                variable.shop_id = data.getString(flag[1][1].toString());
                variable.customer_name = data.getString(flag[2][1].toString());
                variable.confirm_status = status;
                variable.customer_phone = data.getString(flag[4][1].toString());
                variable.is_overtime = data.getString(flag[5][1].toString());
                variable.service_sale_id = saleId;
                variable.page = "1";
                variable.size = "10";

                //全部筛选之后的结果
                JSONObject result = jc.appointmentRecordManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.plate_number), "参数全部输入的查询的" + variable.plate_number + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.shop_id), "参数全部输入的查询的" + variable.shop_id + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[3][1].toString()).contains(confirmStatus), "参数全部输入的查询的" + result.getString(flag[3][1].toString()) + "与列表信息的第一行的" + confirmStatus + "不一致");
                Preconditions.checkArgument(result.getString(flag[4][1].toString()).contains(variable.customer_phone), "参数全部输入的查询的" + variable.customer_phone + "与列表信息的第一行的" + result.getString(flag[4][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[5][1].toString()).contains(variable.is_overtime), "参数全部输入的查询的" + variable.is_overtime + "与列表信息的第一行的" + result.getString(flag[5][1].toString()) + "不一致");
                Preconditions.checkArgument(name.contains(data.getString(flag[6][1].toString())), "参数全部输入的查询的" + variable.service_sale_id + "与列表信息的第一行的" + result.getString(flag[6][1].toString()) + "不一致");

            } else {
                Preconditions.checkArgument(res==null, "接待列表系统错误,请联系开发人员");
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
    @Test(enabled = true)
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
                variable.shop_id = data.getString(flag[1][1].toString());
                variable.customer_name = data.getString(flag[2][1].toString());
                variable.confirm_status = status;
                variable.page = "1";
                variable.size = "10";

                //全部筛选之后的结果
                JSONObject result = jc.appointmentRecordManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.plate_number), "参数全部输入的查询的" + variable.plate_number + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(variable.shop_id), "参数全部输入的查询的" + variable.shop_id + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(flag[3][1].toString()).contains(confirmStatus), "参数全部输入的查询的" + result.getString(flag[3][1].toString()) + "与列表信息的第一行的" + confirmStatus + "不一致");
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
     * @description :保养配置-筛选栏单项查询   ---配置中关村店
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_maintainFilter", dataProviderClass = Constant.class)
    public void maintainOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId="49195";
            JSONObject respond = jc.maintainFilterManage("49195", "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respond1 = jc.maintainFilterManage("49195", "1", "10", pram, result);
                int pages = respond1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.maintainFilterManage("49195", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "保养配置按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("保养配置单项查询，结果校验");
            commonConfig.shopId=product.getShopId();
        }
    }

    /**
     * @description :保养配置-筛选栏填写全部参数查询
     * @date :2020/11/24e
     **/
    @Test
    public void maintainALLFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.shopId="49195";
            Object[][] flag = Constant.maintainFilter_pram();
            maintainVariable variable = new maintainVariable();
            JSONArray res = jc.maintainFilterManage("49195", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.brand_name = data.getString(flag[0][1].toString());
                variable.manufacturer = data.getString(flag[1][1].toString());
                variable.car_model = data.getString(flag[2][1].toString());
                variable.year = data.getString(flag[3][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.shop_id="49195";
                //全部筛选之后的结果
                JSONObject result = jc.maintainFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.brand_name), "参数全部输入的查询的" + variable.brand_name + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.manufacturer), "参数全部输入的查询的" + variable.manufacturer + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.car_model), "参数全部输入的查询的" + variable.car_model + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.year), "参数全部输入的查询的" + variable.year + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("保养配置填写全部参数查询，结果校验");
            commonConfig.shopId=product.getShopId();
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
            commonConfig.shopId="49195";
            Object[][] flag = Constant.maintainFilter_pram();
            maintainVariable variable = new maintainVariable();
            JSONArray res = jc.maintainFilterManage("49195", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.brand_name = data.getString(flag[0][1].toString());
                variable.manufacturer = data.getString(flag[1][1].toString());
                variable.car_model = data.getString(flag[2][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.shop_id = "49195";

                //全部筛选之后的结果
                JSONArray result = jc.maintainFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.brand_name), "参数全部输入的查询的" + variable.brand_name + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString() + "不一致"));
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(variable.manufacturer), "参数全部输入的查询的" + variable.manufacturer + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString() + "不一致"));
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[2][1])).contains(variable.car_model), "参数全部输入的查询的" + variable.car_model + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[2][1].toString() + "不一致"));

                }
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("保养配置填写多项参数查询，结果校验");
            commonConfig.shopId=product.getShopId();
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
            commonConfig.shopId="49195";
            jc.maintainFilterManage("49195", "1", "10", "", "").getJSONArray("list");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("保养配置列表参数不填写，结果校验");
            commonConfig.shopId=product.getShopId();
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
            JSONObject respond = jc.voucherPageFilterManage( "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respond1 = jc.voucherPageFilterManage("1", "10", pram, result);
                int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.voucherPageFilterManage( String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "卡券管理管理按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list")== null, "接待列表系统错误,请联系开发人员");
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
    @Test(enabled = false)
    public void voucherFormAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.voucherFormFilter_pram();
            voucherFormVariable variable = new voucherFormVariable();
            JSONArray res = jc.voucherFormFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.subject_name = data.getString(flag[0][1].toString());
                variable.voucher_name = data.getString(flag[1][1].toString());
                variable.creator_name = data.getString(flag[2][1].toString());
                variable.creator_account = data.getString(flag[3][1].toString());
                variable.voucher_status = data.getString(flag[4][1].toString());
//                variable.voucher_type = data.getString(flag[5][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.voucherFormFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.subject_name),"参数全部输入的查询的"+variable.subject_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.voucher_name), "参数全部输入的查询的" + variable.voucher_name + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.creator_name), "参数全部输入的查询的" + variable.creator_name + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.creator_account), "参数全部输入的查询的" + variable.creator_account + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[4][1])).contains(variable.voucher_status), "参数全部输入的查询的" + variable.voucher_status + "与列表信息的第一行的" + result.getString(flag[4][1].toString()) + "不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[5][1])).contains(variable.voucher_type), "参数全部输入的查询的" + variable.voucher_type + "与列表信息的第一行的" + result.getString(flag[5][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
    @Test(enabled = false)
    public void voucherFormSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.voucherFormFilter_pram();
            voucherFormVariable variable = new voucherFormVariable();
            JSONArray res = jc.voucherFormFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
//                variable.subject_name = data.getString(flag[0][1].toString());
                variable.voucher_name = data.getString(flag[1][1].toString());
                variable.creator_name = data.getString(flag[2][1].toString());
                variable.creator_account = data.getString(flag[3][1].toString());
//                variable.voucher_status = data.getString(flag[4][1].toString());
//                variable.voucher_type = data.getString(flag[5][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.voucherFormFilterManage(variable).getJSONArray("list").getJSONObject(0);
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.subject_name),"参数全部输入的查询的"+variable.subject_name+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.voucher_name), "参数全部输入的查询的" + variable.voucher_name + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.creator_name), "参数全部输入的查询的" + variable.creator_name + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.creator_account), "参数全部输入的查询的" + variable.creator_account + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[4][1])).contains(variable.voucher_status), "参数全部输入的查询的" + variable.voucher_status + "与列表信息的第一行的" + result.getString(flag[4][1].toString()) + "不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[5][1])).contains(variable.voucher_type), "参数全部输入的查询的" + variable.voucher_type + "与列表信息的第一行的" + result.getString(flag[5][1].toString()) + "不一致"); } else {
                Preconditions.checkArgument(res== null, "接待列表系统错误,请联系开发人员");
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
    @Test(enabled = false)
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
     * @description :V3.0核销记录-筛选栏单项查询
     * @date :2020/3/17
     **/
    @Test(dataProvider = "SELECT_verificationRecordFilter", dataProviderClass = Constant.class)
    public void verificationRecordOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response=jc.voucherFormFilterManage(shopId, "1", "10", "", "");
            String id=response.getJSONArray("list").getJSONObject(0).getString("voucher_id");
            System.err.println(id);
            JSONObject respond = jc.verificationReordFilterManage(shopId,id, "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respond1 = jc.verificationReordFilterManage(shopId,id, "1", "10", pram, result);
                int pages = respond1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.verificationReordFilterManage("", id,String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "核销记录管理按" + result + "查询，结果错误" + Flag);
                    }
                }
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
    @Test(enabled = true)
    public void verificationRecordTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response=jc.voucherFormFilterManage(shopId, "1", "10", "", "");
            String id=response.getJSONArray("list").getJSONObject(0).getString("voucher_id");
            String startTime=  dt.getHistoryDate(-10);
            String endTime=  dt.getHistoryDate(10);
            JSONObject respond=jc.verificationReordTimeFilterManage("",id,"1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            if(respond.getJSONArray("list").size()>0){
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.verificationReordTimeFilterManage("",id, String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String verificationTime =list.getJSONObject(i).containsKey("verification_time")?list.getJSONObject(i).getString("verification_time").substring(0,10):startTime;
                        Preconditions.checkArgument(verificationTime.compareTo(startTime)>=0&&verificationTime.compareTo(endTime)<=0, "核销开始时间："+startTime+" 核销结束时间："+endTime+" 列表中核销时间为："+verificationTime);
                    };
                }
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
    @Test(enabled = false)
    public void verificationRecordAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response=jc.voucherFormFilterManage(shopId, "1", "10", "", "");
            String id=response.getJSONArray("list").getJSONObject(0).getString("id");
            Object[][] flag = Constant.verificationRecordFilter_pram();
            verificationRecordVariable variable = new verificationRecordVariable();
            JSONArray res = jc.verificationReordFilterManage(shopId,id, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.voucher_name = data.getString(flag[0][1].toString());
                variable.page = "1";
                variable.size = "10";


                //全部筛选之后的结果
                JSONObject result = jc.verificationReordFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.voucher_name), "参数全部输入的查询的" + variable.voucher_name + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));
          } else {
                Preconditions.checkArgument(res== null, "接待列表系统错误,请联系开发人员");
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
    @Test(enabled = false)
    public void verificationRecordSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response=jc.voucherFormFilterManage(shopId, "1", "10", "", "");
            String id=response.getJSONArray("list").getJSONObject(0).getString("id");
            Object[][] flag = Constant.verificationRecordFilter_pram();
            verificationRecordVariable variable = new verificationRecordVariable();
            JSONArray res = jc.verificationReordFilterManage(shopId, id,"1", "10", "", "").getJSONArray("list");
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
                Preconditions.checkArgument(res== null, "接待列表系统错误,请联系开发人员");
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
    @Test(enabled = false)
    public void verificationRecordEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response=jc.voucherFormFilterManage(shopId, "1", "10", "", "");
            String id=response.getJSONArray("list").getJSONObject(0).getString("id");
            jc.verificationReordFilterManage(shopId,id, "1", "10", "", "").getJSONArray("list");

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
            JSONObject respond = jc.verificationPeopleFilterManage(shopId, "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(pram);
                JSONObject respond1 = jc.verificationPeopleFilterManage(shopId, "1", "10", pram, result);
                int pages = respond1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.verificationPeopleFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "核销记录管理按" + result + "查询，结果错误" + Flag);
                    }
                }
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
                Preconditions.checkArgument(res== null, "接待列表系统错误,请联系开发人员");
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
                Preconditions.checkArgument(res== null, "接待列表系统错误,请联系开发人员");
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

    @Test
    public void verifi() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String id=businessUtil.shopNameTransformId("宁波轿辰一汽丰田");
            System.out.println("---------"+id);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("test");
        }
    }

    /**
     * @description :套餐表单-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_packageFormFilter", dataProviderClass = Constant.class)
    public void packageFormOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respond = jc.packageFormFilterManage("", "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                if(pram.equals("shop_id")){
                    String shopId=businessUtil.shopNameTransformId(result);
                    String shopName=businessUtil.getShopNameExist(result);;
                   JSONObject respond1 = jc.packageFormFilterManage("", "1", "10", pram, shopId);
                   int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                   for (int page = 1; page <= pages; page++) {
                       JSONArray list = jc.packageFormFilterManage("", String.valueOf(page),"10", pram, shopId).getJSONArray("list");
                       for (int i = 0; i < list.size(); i++) {
                           String Flag = list.getJSONObject(i).getString(output);
                           Preconditions.checkArgument(Flag.contains(shopName), "套餐表单管理按" + shopName + "查询，结果错误" + Flag);
                       }
                   }
               }else{
                    JSONObject respond1 = jc.packageFormFilterManage("", "1", "10", pram, result);
                   int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                   for (int page = 1; page <= pages; page++) {
                       JSONArray list = jc.packageFormFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                       for (int i = 0; i < list.size(); i++) {
                           String Flag = list.getJSONObject(i).getString(output);
                           Preconditions.checkArgument(Flag.contains(result), "套餐表单管理按" + result + "查询，结果错误" + Flag);
                       }
                   }
               }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.packageFormTimeFilterManage("","1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            if(respond.getJSONArray("list").size()>0){
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.packageFormTimeFilterManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String createTime = list.getJSONObject(i).containsKey("create_time")?list.getJSONObject(i).getString("create_time").substring(0,10):startTime;
                        Preconditions.checkArgument(createTime.compareTo(startTime)>=0&&createTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime+" 列表中创建时间为："+createTime);
                    }
                }
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
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
     * @description :套餐购买记录-筛选栏单项查询
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_buyPackageRecordFilter", dataProviderClass = Constant.class)
    public void buyPackageRecordOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respond = jc.buyPackageRecordFilterManage("", "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                if (pram.equals("send_type")) {
                    int sendType = result.equals("售出") ? 1 : 0;
                    JSONObject respond1 = jc.buyPackageRecordFilterManage("", "1", "10", pram, String.valueOf(sendType));
                    int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.buyPackageRecordFilterManage("", String.valueOf(page), "10", pram, String.valueOf(sendType)).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "套餐购买管理按" + result + "查询结果" + Flag);
                        }
                    }
                } else {
                    JSONObject respond1 = jc.buyPackageRecordFilterManage("", "1", "10", pram, result);
                    int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.buyPackageRecordFilterManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "套餐购买管理按" + result + "查询结果" + Flag);
                        }
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respon=jc.buyPackageRecordFilterTimeManage("","1","10",startTime,endTime);
            int pages = respon.getInteger("pages")>10?10:respon.getInteger("pages");
            if(respon.getJSONArray("list").size()>0){
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.buyPackageRecordFilterTimeManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String sendTime = list.getJSONObject(i).containsKey("send_time")?list.getJSONObject(i).getString("send_time").substring(0,10):startTime;
                        Preconditions.checkArgument(sendTime.compareTo(startTime)>=0&&sendTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime+" 列表中时间发出为："+sendTime);
                    };
                }
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
            String payTypeName=res.getJSONObject(0).getString("pay_type_name");
            int sendType=payTypeName.equals("售出")?1:0;
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.package_name = data.getString(flag[0][1].toString());
                variable.send_type = String.valueOf(sendType);
                variable.sender = data.getString(flag[2][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.buyPackageRecordFilterManage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.package_name), "参数全部输入的查询的" + variable.package_name + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(payTypeName), "参数全部输入的查询的" +payTypeName + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.sender), "参数全部输入的查询的" + variable.sender + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
            String payTypeName=res.getJSONObject(0).getString("pay_type_name");
            int sendType=payTypeName.equals("售出")?1:0;
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.package_name = data.getString(flag[0][1].toString());
                variable.send_type = String.valueOf(sendType);
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONArray result = jc.buyPackageRecordFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.package_name), "参数全部输入的查询的" + variable.package_name + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[0][1].toString()) + "不一致");
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[1][1])).contains(payTypeName), "参数全部输入的查询的" + payTypeName + "与列表信息的第" + i + "行的" + result.getJSONObject(i).getString(flag[1][1].toString()) + "不一致");
                }
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
            JSONObject respond = jc.messageFormFilterManage("", "1", "10", "", "");
            String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
            String sendAccountReason = respond.getJSONArray("list").getJSONObject(0).getString("message_type_name");
            String sendAccount = messageFormCustomerTurnMethod("MESSAGE_TYPE_LIST", sendAccountReason);
            if (pram.equals("message_type")) {
                JSONObject respond1 = jc.messageFormFilterManage("", "1", "10", pram, sendAccount);
                if (respond1.getJSONArray("list").size() > 0 && sendAccountReason != null){
                    int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.messageFormFilterManage("", String.valueOf(page), "10", pram, sendAccount).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(sendAccountReason), "消息表单管理按" + sendAccount + "查询结果" + Flag);
                        }
                    }
                } else if (result != null) {
                Preconditions.checkArgument(respond1.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
            }

            }else{
                JSONObject respond1 = jc.messageFormFilterManage("", "1", "10", pram, result);
                int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                if (respond1.getJSONArray("list").size() > 0 && result != null){
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.messageFormFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "消息表单管理按" + result + "查询结果" + Flag);
                        }
                    }
                }else if (result != null) {
                    Preconditions.checkArgument(respond1.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
            int pages = respon.getInteger("pages")>10?10:respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.messageFormTimeFilterManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String pushTime = list.getJSONObject(i).containsKey("push_time")?list.getJSONObject(i).getString("push_time").substring(0,10):startTime;
                    Preconditions.checkArgument((pushTime.compareTo(startTime)>0||pushTime.compareTo(startTime)==0)&&(pushTime.compareTo(endTime)<0||pushTime.compareTo(endTime)==0), "开始时间："+startTime+" 结束时间："+endTime+" 推送时间为："+pushTime);
                }
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
    @Test(enabled = false)
    public void messageFormAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.messageFormFilter_pram();
            messageFormVariable variable = new messageFormVariable();
            JSONArray res = jc.messageFormFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.shop_id = (data.getString(flag[0][1].toString())==null)?"":data.getString(flag[0][1].toString());
                variable.customer_name = (data.getString(flag[1][1].toString())==null)?"":data.getString(flag[1][1].toString());
                variable.send_account = (data.getString(flag[3][1].toString())==null)?"":data.getString(flag[3][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.messageFormFilterManage(variable).getJSONArray("list").getJSONObject(0);
                String customerName=result.getString("customer_name")==null?"":result.getString("customer_name");
                String sendAccount=result.getString("send_account")==null?"":result.getString("send_account");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(variable.shop_id), "参数全部输入的查询的" + variable.shop_id + "与列表信息的第一行的" + result.getString(flag[0][1].toString()) + "不一致");
                Preconditions.checkArgument(customerName.contains(variable.customer_name), "参数全部输入的查询的" + variable.customer_name + "与列表信息的第一行的" + customerName + "不一致");
                Preconditions.checkArgument(sendAccount.contains(variable.send_account), "参数全部输入的查询的" + variable.send_account + "与列表信息的第一行的" + sendAccount + "不一致");

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息表单填写全部参数查询，结果校验");
        }
    }

    /**
     * @description :消息表单-筛选栏填写多项参数查询--校验前五页数据
     * @date :2020/11/28
     **/
    @Test(enabled = false)
    public void messageFormSomeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.messageFormFilter_pram();
            messageFormVariable variable = new messageFormVariable();
            JSONArray res = jc.messageFormFilterManage("", "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.shop_id = (data.getString(flag[0][1].toString())==null)?"":data.getString(flag[0][1].toString());
                variable.customer_name = (data.getString(flag[1][1].toString())==null)?"":data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "50";
                //全部筛选之后的结果
                JSONArray result = jc.messageFormFilterManage(variable).getJSONArray("list");
                for (int i = 0; i < result.size(); i++) {
                    String customerName=result.getJSONObject(i).getString("customer_name")==null?"":result.getJSONObject(i).getString("customer_name");
                    Preconditions.checkArgument(result.getJSONObject(i).getString(String.valueOf(flag[0][1])).contains(variable.shop_id), "参数全部输入的查询的:" + variable.shop_id + "与列表信息的第" + i + "行的:" + result.getJSONObject(i).getString(flag[0][1].toString()) + "不一致");
                    Preconditions.checkArgument(customerName.contains(variable.customer_name),"参数全部输入的查询的:" + variable.customer_name + "与列表信息的第" + i + "行的:" + customerName + "不一致");
                }
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
            JSONObject respond = jc.articleFilterManage(shopId, "1", "10", "", "");
            String result = respond.getJSONArray("list").getJSONObject(0).getString(pram);
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
    @Test(enabled = true)
    public void articleTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-5);
            String endTime=  dt.getHistoryDate(5);
            JSONObject respond=jc.articleTimeFilterManage("","1","10",startTime,endTime,startTime,endTime);
            int pages = respond.getInteger("pages");
            if(respond.getJSONArray("list").size()>0){
                for (int page = 1; page <= pages; page++){
                    JSONArray list = jc.articleTimeFilterManage("", String.valueOf(page),"10",startTime,endTime,startTime,endTime).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String createTime = list.getJSONObject(i).containsKey("create_time")?list.getJSONObject(i).getString("create_time").substring(0,10):startTime;
                        String modifyTime = list.getJSONObject(i).containsKey("modify_time")?list.getJSONObject(i).getString("modify_time").substring(0,10):startTime;
                        Preconditions.checkArgument(createTime.compareTo(startTime)>=0&&createTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的创建时间时间:"+createTime);
                        Preconditions.checkArgument(modifyTime.compareTo(startTime)>=0&&modifyTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的更新时间时间:"+modifyTime);
                    }
                }
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
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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

    @Test
    public void arti() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String id=businessUtil.shopNameTransformId("集团管理");
            System.err.println(id);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("test");
        }
    }


    /**
     * @description :优惠券审批-筛选栏单项查询      申请门店查询，已提bug（7882）
     * @date :2020/11/24
     **/
    @Test(dataProvider = "SELECT_applyListFilter", dataProviderClass = Constant.class)
    public void applyListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respond = jc.applyListFilterManage(shopId, "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                if (pram.equals("status")) {
                    String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                    String statusNameRespond = respond.getJSONArray("list").getJSONObject(0).getString("status_name");
                    String statusName = messageFormCustomerTurnMethod("VOUCHER_AUDIT_STATUS_LIST", statusNameRespond);
                    JSONObject respond2 = jc.applyListFilterManage(shopId, "1", "10", pram, statusName);
                    int pages = respond2.getInteger("pages")>10?10:respond2.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.applyListFilterManage("", String.valueOf(page),"10", pram, statusName).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "卡券申请按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }else if(pram.equals("apply_group")){
                    String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                    String applyGroup=businessUtil.shopNameTransformId(result);
                    String name=businessUtil.getShopNameExist(result);;
                    JSONObject respond1 = jc.applyListFilterManage(shopId, "1", "10", pram, applyGroup);
                    int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.applyListFilterManage("", String.valueOf(page),"10", pram, applyGroup).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(name), "卡券申请按" + name + "查询，结果错误" + Flag);
                        }
                    }
                } else {
                    String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                    JSONObject respond1 = jc.applyListFilterManage(shopId, "1", "10", pram, result);
                    int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.applyListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "卡券申请按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("优惠券审批单项查询，结果校验");
        }
    }

    /**
     * @description 卡券申请-时间的筛选
     * @date :2020/12/16
     **/
    @Test(enabled = false)
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
                    String applyTime = list.getJSONObject(i).getString("apply_time").substring(0,10);
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
    @Test(enabled = false)
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
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
    @Test(enabled = false)
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
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
            JSONObject respond = jc.shopListFilterManage(shopId, "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(pram);
                JSONObject respond1 = jc.shopListFilterManage(shopId, "1", "10", pram, result);
                int pages = respond1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.shopListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "门店列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店列表单项查询，结果校验");
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
            JSONObject respond = jc.brandListFilterManage(shopId, "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respond1 = jc.brandListFilterManage(shopId, "1", "10", pram, result);
                int pages = respond1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.brandListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "品牌列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
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
//            String id = jc.brandListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("id");
            String id="61";//自动化车系
            JSONObject respond = jc.carStyleListFilterManage(shopId, "1", "10", id, "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(pram);
                JSONObject respond1 = jc.carStyleListFilterManage(shopId, "1", "10", id, pram, result);
                int pages = respond1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.carStyleListFilterManage("", String.valueOf(page),"10", id, pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.toLowerCase().contains(result.toLowerCase()), "车系列表按" + result + "查询，结果错误" + Flag);
                    }
                }
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
//            String id = jc.brandListFilterManage(shopId, "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("id");
            String id="61";//自动化车系
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
            String brandId = "61";
            String styleId = jc.carStyleListFilterManage(shopId, "1", "10", "", brandId).getJSONArray("list").getJSONObject(0).getString("id");
            JSONObject respond = jc.carModelListFilterManage1(shopId, "1", "10", "", "", brandId, styleId);
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respond1 = jc.carModelListFilterManage(shopId, "1", "10", brandId, styleId, pram, result);
                int pages = respond1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.carModelListFilterManage("", String.valueOf(page),"10",  brandId, styleId,pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++)
                    {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "车型列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
            String brand_id = "61";//自动化车型
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
                Preconditions.checkArgument(list== null, "接待列表系统错误,请联系开发人员");
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
            String brand_id = "61";
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
                Preconditions.checkArgument(list == null, "接待列表系统错误,请联系开发人员");
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
    @Test(enabled = false)
    public void carModelListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String brand_id =  "61";
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
            JSONObject respond = jc.roleListFilterManage(shopId, "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respond1 = jc.roleListFilterManage(shopId, "1", "10", pram, result);
                int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.roleListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "角色列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
    @Test(dataProvider = "SELECT_staffListFilter", dataProviderClass = Constant.class)
    public void staffListOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        JSONObject respond = jc.staffListFilterManage(null, "1", "10", "", "");
        try {
            if (respond.getJSONArray("list").size() > 0) {
                if(pram.equals("shop_id")){
                    String result = respond.getJSONArray("list").getJSONObject(0).getJSONArray("shop_list").getJSONObject(0).getString(output);
                    JSONObject respond1 = jc.staffListFilterManage(null, "1", "10", pram, result);
                    int pages = respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.staffListFilterManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String string="";
                            JSONArray list1 = list.getJSONObject(i).getJSONArray("shop_list");
                            for(int j=0;j<list1.size();j++){
                                String Flag = list1.getJSONObject(j).getString("shop_id");
                                string=Flag+string;
                            }
                            Preconditions.checkArgument(string.contains(result), "员工列表按" + string + "查询，结果错误" + result);

                        }
                    }
                }else if(pram.equals("role_id")){
                    String result = respond.getJSONArray("list").getJSONObject(0).getJSONArray("role_list").getJSONObject(0).getString(output);
                    JSONObject respond1 = jc.staffListFilterManage(null, "1", "10", pram, result);
                    int pages = respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.staffListFilterManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String string="";
                            JSONArray list1 = list.getJSONObject(i).getJSONArray("role_list");
                            for(int j=0;j<list1.size();j++){
                                String Flag = list1.getJSONObject(j).getString("role_id");
                                string=Flag+string;
                            }
                            System.out.println("员工列表按" + string + "查询，结果错误" + result);
                            Preconditions.checkArgument(string.contains(result), "员工列表按" + string + "查询，结果错误" + result);

                        }
                    }
                }else{
                    String result = respond.getJSONArray("list").getJSONObject(0).getString(pram);
                    JSONObject respond1 = jc.staffListFilterManage(null, "1", "10", pram, result);
                    int pages = respond1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.staffListFilterManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "员工列表按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "员工列表错误,请自行检查");
            }

//            if (respond.getJSONArray("list").size() > 0) {
//                System.out.println("-------"+pram);
//                if(pram.equals("role_id")) {
//                    if (respond.getJSONArray("list").size() > 0) {
//                        String result = respond.getJSONArray("list").getJSONObject(0).getJSONArray("role_list").getJSONObject(0).getString(pram);
//                        JSONObject respond1 = jc.staffListFilterManage(null, "1", "10", pram, result);
//                        int pages = respond1.getInteger("pages");
//                        for (int page = 1; page <= pages; page++) {
//                            JSONArray list = jc.staffListFilterManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
//                            for (int i = 0; i < list.size(); i++) {
//                                String Flag = list.getJSONObject(i).getJSONArray("role_list").getJSONObject(0).getString("role_id");
//                                System.out.println("-------"+Flag);
//                                Preconditions.checkArgument(Flag.contains(result), "员工列表按" + result + "查询，结果错误" + Flag);
//                            }
//                        }
//                    }
//                }if(pram.equals("shop_id")) {
//                    if (respond.getJSONArray("list").size() > 0) {
//                        String result = respond.getJSONArray("list").getJSONObject(0).getJSONArray("shop_list").getJSONObject(0).getString(pram);
//                        JSONObject respond1 = jc.staffListFilterManage(null, "1", "10", pram, result);
//                        int pages = respond1.getInteger("pages");
//                        for (int page = 1; page <= pages; page++) {
//                            JSONArray list = jc.staffListFilterManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
//                            for (int i = 0; i < list.size(); i++) {
//                                String Flag = list.getJSONObject(i).getJSONArray("shop_list").getJSONObject(0).getString("shop_id");
//                                System.out.println("-------"+Flag);
//                                Preconditions.checkArgument(Flag.contains(result), "员工列表按" + result + "查询，结果错误" + Flag);
//                            }
//                        }
//                    }
//                } else{
//                    String result = respond.getJSONArray("list").getJSONObject(0).getString(pram);
//                    JSONObject respond1 = jc.staffListFilterManage(null, "1", "10", pram, result);
//                    int pages = respond1.getInteger("pages");
//                    for (int page = 1; page <= pages; page++) {
//                        JSONArray list = jc.staffListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
//                        for (int i = 0; i < list.size(); i++) {
//                            String Flag = list.getJSONObject(i).getString(output);
//                            Preconditions.checkArgument(Flag.contains(result), "员工列表按" + result + "查询，结果错误" + Flag);
//                        }
//                    }
//                    }
//                }
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
    @Test(enabled = false)
    public void staffListEmptyFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list=jc.staffListFilterManage(null, "1", "10", "", "").getJSONArray("list");
            System.err.println(list);
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
            JSONObject respond = jc.importListFilterManage(shopId, "1", "10", "", "");
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respond1 = jc.importListFilterManage(shopId, "1", "10", pram, result);
                int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.importListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "导入记录按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
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
            JSONObject respond=jc.importListTimeFilterManage("","1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.importListTimeFilterManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String importTime = list.getJSONObject(i).containsKey("import_time")?list.getJSONObject(i).getString("import_time").substring(0,10):startTime;
                    Preconditions.checkArgument(importTime.compareTo(startTime)>=0&&importTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的导入时间:"+importTime);
                }
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
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导入记录列表填写全部参数查询，结果校验");
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
            JSONObject respond = jc.exportListFilterManage(shopId, "1", "10", "", "");
            System.err.println(respond);
            if (respond.getJSONArray("list").size() > 0) {
                String result = respond.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject respond1 = jc.exportListFilterManage(shopId, "1", "10", pram, result);
                int pages = respond1.getInteger("pages")>10?10:respond1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.exportListFilterManage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        System.out.println("导出记录按" + result + "查询，结果错误" + Flag);
                        Preconditions.checkArgument(Flag.contains(result), "导出记录按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(respond.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出记录列表单项查询，结果校验");
        }
    }

    /**
     * @description 导出记录-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void exportListTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-2);
            String endTime=  dt.getHistoryDate(2);
            JSONObject respond=jc.exportListFilterManage1("","1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.exportListFilterManage1("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String importTime = list.getJSONObject(i).containsKey("export_time")?list.getJSONObject(i).getString("import_time").substring(0,10):startTime;
                    Preconditions.checkArgument(importTime.compareTo(startTime)>=0&&importTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的导出时间:"+importTime);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出记录时间的筛选，结果校验");
        }
    }

    /**
     * @description :导出记录列表-筛选栏填写全部参数查询
     * @date :2020/11/27
     **/
    @Test(enabled = false)
    public void exportListAllFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.exportListFilter_pram();
            JSONArray res = jc.exportListFilterManage(shopId, "1", "10", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                String type = data.getString(flag[0][1].toString());
                String user = data.getString(flag[1][1].toString());

                //全部筛选之后的结果
                JSONObject result = jc.exportListFilterManage(shopId, "1", "10", type, user).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(type), "参数全部输入的查询的" + type + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(user), "参数全部输入的查询的" + user + "与列表信息的第一行的" + result.getString(flag[1][1].toString() + "不一致"));
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出记录列表填写全部参数查询，结果校验");
        }
    }


    /**
     * @description :导出记录查询-筛选栏参数不填写
     * @date :2020/11/27
     **/
    @Test()
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
            JSONObject respond=jc.pushMsgListTimeFilterManage("","1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.pushMsgListTimeFilterManage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String sendTime = list.getJSONObject(i).containsKey("send_time")?list.getJSONObject(i).getString("send_time"):startTime;
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
                System.out.println("key:"+key);
                System.out.println("pram:"+pram);
                JSONObject respond1 = jc.pushMsgListFilterManage("-1", "1", "10", pram, key);
                JSONArray list = respond1.getJSONArray("list");
                String value = map.get(key);
                if (list.size() > 0) {
                    for (int j = 0; j < list.size(); j++) {
                        String resultA = list.getJSONObject(j).getString(fieldReturn);
                        Preconditions.checkArgument(resultA.equals(value), "列表中的结果为：" + resultA + "筛选传入的值为" + value);
                    }
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
    @Test(enabled = false)
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
                Preconditions.checkArgument(res == null, "消息记录列表错误,请检查代码");
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
     * @description :V2.0-智能提醒列表
     *  item 提醒类型
     * @date :2021-2-1
     **/
    @Test()
    public void intelligentRemindList(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            commonConfig.shopId="49195";//指定中关村门店
            JSONObject response=jc.remindPage("1","10","","","");
            String item=response.getJSONArray("list").getJSONObject(0).getString("item");
            JSONObject response1=jc.remindPage("1","10","","item",item);
            int pages=response1.getInteger("pages")>10?10:response1.getInteger("pages");
            JSONArray list1=response1.getJSONArray("list");
           if(list1.size()>0){
               for(int page=1;page<=pages;page++){
                   JSONArray list=jc.remindPage(String.valueOf(page),"10","","item",item).getJSONArray("list");
                   for(int i=0;i<list.size();i++){
                           String itemCheck=list.getJSONObject(i).getString("item");
                           Preconditions.checkArgument(itemCheck.contains(item),"智能提醒列表中的第"+(i+1)+"行的提醒类型与搜索的内容不一致，为："+itemCheck);
                   }
               }
           }
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            commonConfig.shopId=product.getShopId();
            saveData("V2.0-智能提醒筛选栏校验");
        }
    }

    /**
     * @description :V2.0-洗车管理列表--单项搜索    ---问题：客户类型搜索存在问题（bug7809）
     * @date :2021-2-1
     **/
    @Test(dataProvider = "SELECT_washCarManagerFilter", dataProviderClass = Constant.class )
    public void washCarManagerPageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject response = jc.washCarManagerPage( "1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject response1 = jc.washCarManagerPage("1", "10", pram, result);
                int pages = response1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.washCarManagerPage( String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        System.out.println("洗车管理列表按" + result + "查询，结果错误" + Flag);
                        Preconditions.checkArgument(Flag.contains(result), "洗车管理列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(response.getJSONArray("list") == null, "洗车管理列表系统错误,请联系开发人员");
            }
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0-智能提醒筛选栏校验");
        }
    }

    /**
     * @description 洗车记录-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void washCarManagerPageTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.washCarManagerPage("","1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.washCarManagerPage("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String sendTime = list.getJSONObject(i).containsKey("wash_car_date")?list.getJSONObject(i).getString("wash_car_date"):startTime;
                    System.out.println("开始时间："+startTime+" 结束时间："+endTime +"列表中的推送时间:"+sendTime);
                    Preconditions.checkArgument(sendTime.compareTo(startTime)>=0&&sendTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的推送时间:"+sendTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("洗车记录时间的筛选，结果校验");
        }
    }

    /**
     * @description :V2.0-洗车管理列表--筛选栏全部填写查询
     * @date :2021-2-1
     **/
    @Test(enabled = false)
    public void washCarManagerPageSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.washCarManagerListFilter_pram();
            WashCarManagerVariable washCarManagerVariable=new WashCarManagerVariable();
            JSONArray res = jc.washCarManagerPage("1","10","","").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                washCarManagerVariable.customerName = data.getString(flag[0][1].toString());
                washCarManagerVariable.customerVipType = data.getString(flag[1][1].toString());
                washCarManagerVariable.shopId = data.getString(flag[2][1].toString());
                washCarManagerVariable.phone = data.getString(flag[3][1].toString());
                washCarManagerVariable.page = "1";
                washCarManagerVariable.size = "10";
                //全部筛选之后的结果
                JSONObject result =  jc.washCarManagerPage(washCarManagerVariable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(flag[0][1].toString()).contains(washCarManagerVariable.customerName), "参数全部输入的查询的" + washCarManagerVariable.customerName + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(washCarManagerVariable.customerVipType), "参数全部输入的查询的" + washCarManagerVariable.customerVipType + "与列表信息的第一行的" + result.getString(flag[1][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(washCarManagerVariable.shopId), "参数全部输入的查询的" + washCarManagerVariable.shopId + "与列表信息的第一行的" + result.getString(flag[2][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(washCarManagerVariable.phone), "参数全部输入的查询的" + washCarManagerVariable.phone + "与列表信息的第一行的" + result.getString(flag[3][1].toString() + "不一致"));
                 } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
            }
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0-洗车管理列表，筛选栏全部填写查询");
        }
    }

    /**
     * @description :V2.0-洗车管理列表--筛选栏多选查询
     * @date :2021-2-1
     **/
    @Test(enabled = false)
    public void washCarManagerPageAllFielter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.washCarManagerListFilter_pram();
            WashCarManagerVariable washCarManagerVariable=new WashCarManagerVariable();
            JSONArray res = jc.washCarManagerPage("1","10","","").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                washCarManagerVariable.customerName = data.getString(flag[0][1].toString());
//                washCarManagerVariable.customerVipType = data.getString(flag[1][1].toString());
//                washCarManagerVariable.shopId = data.getString(flag[2][1].toString());
                washCarManagerVariable.phone = data.getString(flag[3][1].toString());
                washCarManagerVariable.page = "1";
                washCarManagerVariable.size = "10";
                //多选后筛选之后的结果
                JSONObject result =  jc.washCarManagerPage(washCarManagerVariable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(flag[0][1].toString()).contains(washCarManagerVariable.customerName), "参数全部输入的查询的" + washCarManagerVariable.customerName + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));
//                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(washCarManagerVariable.customerVipType), "参数全部输入的查询的" + washCarManagerVariable.customerVipType + "与列表信息的第一行的" + result.getString(flag[1][1].toString() + "不一致"));
//                Preconditions.checkArgument(result.getString(flag[2][1].toString()).contains(washCarManagerVariable.shopId), "参数全部输入的查询的" + washCarManagerVariable.shopId + "与列表信息的第一行的" + result.getString(flag[2][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(washCarManagerVariable.phone), "参数全部输入的查询的" + washCarManagerVariable.phone + "与列表信息的第一行的" + result.getString(flag[3][1].toString() + "不一致"));
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
            }
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0-洗车管理列表，筛选栏多选填写查询");
        }
    }

    /**
     * @description :V2.0-洗车管理列表--筛选栏不填写查询
     * @date :2021-2-2
     **/
    @Test()
    public void washCarManagerPageEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray res = jc.washCarManagerPage("1","10","","").getJSONArray("list");
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0-洗车管理列表，筛选栏不填写校验");
        }
    }

    /**
     * @description :V2.0-调整洗车次数--单项搜索
     * @date :2021-2-2
     **/
    @Test(dataProvider = "SELECT_adjustNumberRecordFilter", dataProviderClass = Constant.class)
    public void AdjustNumberRecordOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject response = jc.adjustNumberRecord( "1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
              if(pram.equals("customer_type")){
                  String result1 = response.getJSONArray("list").getJSONObject(0).getString(output).equals("普通会员")?"1":"10";
                  String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                  JSONObject response1 = jc.adjustNumberRecord("1", "10", pram, result1);
                  int pages = response1.getInteger("pages");
                  for (int page = 1; page <= pages; page++) {
                      JSONArray list = jc.adjustNumberRecord(String.valueOf(page),"10", pram, result1).getJSONArray("list");
                      for (int i = 0; i < list.size(); i++) {
                          String Flag = list.getJSONObject(i).getString(output);
                          System.out.println("调整洗车次数按" + result + "查询，结果错误" + Flag);
                          Preconditions.checkArgument(Flag.contains(result), "调整洗车次数按" + result + "查询，结果错误" + Flag);
                      }
                  }
              }else if(pram.equals("adjust_shop_id")){
                  String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                  String shopId=businessUtil.shopNameTransformId(result);
                  String name=businessUtil.getShopNameExist(result);
                  System.out.println("------"+name);
                  JSONObject response1 = jc.adjustNumberRecord("1", "10", pram, shopId);
                  int pages = response1.getInteger("pages");
                  for (int page = 1; page <= pages; page++) {
                      JSONArray list = jc.adjustNumberRecord(String.valueOf(page),"10", pram, shopId).getJSONArray("list");
                      for (int i = 0; i < list.size(); i++) {
                          String Flag = list.getJSONObject(i).getString(output);
                          System.out.println("调整洗车次数按" + name + "查询，结果错误" + Flag);
                          Preconditions.checkArgument(Flag.contains(name), "调整洗车次数按" + name + "查询，结果错误" + Flag);
                      }
                  }
              }else{
                  String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                  JSONObject response1 = jc.adjustNumberRecord("1", "10", pram, result);
                  int pages = response1.getInteger("pages");
                  for (int page = 1; page <= pages; page++) {
                      JSONArray list = jc.adjustNumberRecord(String.valueOf(page),"10", pram, result).getJSONArray("list");
                      for (int i = 0; i < list.size(); i++) {
                          String Flag = list.getJSONObject(i).getString(output);
                          Preconditions.checkArgument(Flag.contains(result), "调整洗车次数按" + result + "查询，结果错误" + Flag);
                      }
                  }
              }
            } else {
                Preconditions.checkArgument(response.getJSONArray("list") == null, "接待列表系统错误,请联系开发人员");
            }
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0-调整洗车次数--单项搜索");
        }
    }

    /**
     * @description 调整洗车记录-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void AdjustNumberRecordTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.adjustNumberRecord("","1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.adjustNumberRecord("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String adjustDate = list.getJSONObject(i).containsKey("adjust_date")?list.getJSONObject(i).getString("adjust_date"):startTime;
                    System.out.println("开始时间："+startTime+" 结束时间："+endTime +"列表中的推送时间:"+adjustDate);
                    Preconditions.checkArgument(adjustDate.compareTo(startTime)>=0&&adjustDate.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的推送时间:"+adjustDate);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("调整洗车记录时间的筛选，结果校验");
        }
    }

    /**
     * @description :V2.0-调整洗车次数--筛选栏全部填写查询
     * @date :2021-2-2
     **/
    @Test(enabled = false)
    public void AdjustNumberRecordAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.adjustNumberRecordFilter_pram();
            AdjustNumberRecordVariable adjustNumberRecordVariable=new AdjustNumberRecordVariable();
            JSONArray res = jc.adjustNumberRecord("1","10","","").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                adjustNumberRecordVariable.customerName = data.getString(flag[0][1].toString());
                adjustNumberRecordVariable.customerPhone = data.getString(flag[1][1].toString());
                adjustNumberRecordVariable.customerType = data.getString(flag[2][1].toString());
                adjustNumberRecordVariable.adjustShopId = data.getString(flag[3][1].toString());
                adjustNumberRecordVariable.page = "1";
                adjustNumberRecordVariable.size = "10";
                //全部筛选之后的结果
                JSONObject result =  jc.adjustNumberRecord(adjustNumberRecordVariable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(flag[0][1].toString()).contains(adjustNumberRecordVariable.customerName), "参数全部输入的查询的" + adjustNumberRecordVariable.customerName + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(adjustNumberRecordVariable.customerPhone), "参数全部输入的查询的" + adjustNumberRecordVariable.customerPhone + "与列表信息的第一行的" + result.getString(flag[1][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(flag[3][1].toString()).contains(adjustNumberRecordVariable.adjustShopId), "参数全部输入的查询的" + adjustNumberRecordVariable.adjustShopId + "与列表信息的第一行的" + result.getString(flag[3][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(adjustNumberRecordVariable.customerType), "参数全部输入的查询的" + adjustNumberRecordVariable.customerType + "与列表信息的第一行的" + result.getString(flag[2][1].toString() + "不一致"));
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
            }
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0-洗车管理列表，筛选栏全部填写查询");
        }
    }



    /**
     * @description :V2.0-调整洗车次数--筛选栏多项填写查询
     * @date :2021-2-2
     **/
    @Test(enabled = false)
    public void AdjustNumberRecordSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            Object[][] flag = Constant.adjustNumberRecordFilter_pram();
            AdjustNumberRecordVariable adjustNumberRecordVariable=new AdjustNumberRecordVariable();
            JSONArray res = jc.adjustNumberRecord("1","10","","").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                adjustNumberRecordVariable.customerName = data.getString(flag[0][1].toString());
                adjustNumberRecordVariable.customerPhone = data.getString(flag[1][1].toString());
                adjustNumberRecordVariable.page = "1";
                adjustNumberRecordVariable.size = "10";
                //部分筛选之后的结果
                JSONObject result =  jc.adjustNumberRecord(adjustNumberRecordVariable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(flag[0][1].toString()).contains(adjustNumberRecordVariable.customerName), "参数全部输入的查询的" + adjustNumberRecordVariable.customerName + "与列表信息的第一行的" + result.getString(flag[0][1].toString() + "不一致"));
                Preconditions.checkArgument(result.getString(flag[1][1].toString()).contains(adjustNumberRecordVariable.customerPhone), "参数全部输入的查询的" + adjustNumberRecordVariable.customerPhone + "与列表信息的第一行的" + result.getString(flag[1][1].toString() + "不一致"));
            } else {
                Preconditions.checkArgument(res == null, "接待列表系统错误,请联系开发人员");
            }
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0-洗车管理列表，筛选栏多项填写查询");
        }
    }

    /**
     * @description :V2.0-调整次数记录-筛选栏不填写查询
     * @date :2021-2-1
     **/
    @Test()
    public void AdjustNumberRecordEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray res = jc.adjustNumberRecord("1","10","","").getJSONArray("list");
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0-调整次数记录-筛选栏不填写查询");
        }
    }

    /**
     * @deprecated V3.0-优惠券领取记录--筛选栏单项搜索
     * @date :2021-3-17
     */
    @Test(dataProvider = "SELECT_voucherManageSendRecordFilter", dataProviderClass = Constant.class)
    public void voucherManageSendRecordOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject jsonObject=jc.oucherFormVoucherPage("","1","10");
            String id = jsonObject.getJSONArray("list").getJSONObject(0).getString("id");
            JSONObject response = jc.voucherManageSendRecord("1", "10",id, "", "");
            if (response.getJSONArray("list").size() > 0) {
               if(pram.equals("customer_label")){
                   String result1 = "COMMON";
                   String result="普通会员";
                   JSONObject response1 = jc.voucherManageSendRecord( "1", "10",id, pram, result1);
                   int pages = response1.getInteger("pages")>10?10:response1.getInteger("pages");
                   for (int page = 1; page <= pages; page++) {
                       JSONArray list = jc.voucherManageSendRecord( String.valueOf(page),"10",id, pram, result1).getJSONArray("list");
                       for (int i = 0; i < list.size(); i++) {
                           String Flag = list.getJSONObject(i).getString(output);
                           System.out.println("优惠券领取记录按" + result + "查询，结果错误" + Flag);
                           Preconditions.checkArgument(Flag.contains(result), "优惠券领取记录按" + result + "查询，结果错误" + Flag);
                       }
                   }
               }else{
                   String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                   JSONObject response1 = jc.voucherManageSendRecord( "1", "10",id, pram, result);
                   int pages = response1.getInteger("pages");
                   for (int page = 1; page <= pages; page++) {
                       JSONArray list = jc.voucherManageSendRecord( String.valueOf(page),"10",id, pram, result).getJSONArray("list");
                       for (int i = 0; i < list.size(); i++) {
                           String Flag = list.getJSONObject(i).getString(output);
                           System.out.println("优惠券领取记录按" + result + "查询，结果错误" + Flag);
                           Preconditions.checkArgument(Flag.contains(result), "优惠券领取记录按" + result + "查询，结果错误" + Flag);
                       }
                   }
               }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0-优惠券领取记录单项查询，结果校验");
        }
    }

    /**
     * @description 优惠券领取记录-时间的筛选
     * @date :2020/12/16
     **/
    @Test(enabled = false)
    public void voucherManageSendRecordTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject jsonObject=jc.oucherFormVoucherPage("","1","10");
            String id = jsonObject.getJSONArray("list").getJSONObject(0).getString("id");
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.voucherManageSendRecord("1","10",id,startTime,endTime,startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.voucherManageSendRecord(String.valueOf(page),"10",id,startTime,endTime,startTime,endTime).getJSONArray("list");
                System.err.println(list);
                for (int i = 0; i < list.size(); i++) {
                    String sendTime = list.getJSONObject(i).containsKey("send_time")?list.getJSONObject(i).getString("send_time").substring(0,10):startTime;
                    String validityTimeStart = list.getJSONObject(i).containsKey("validity_time")?list.getJSONObject(i).getString("validity_time").substring(0,10):startTime;
                    String validityTimeEnd = list.getJSONObject(i).containsKey("validity_time")?list.getJSONObject(i).getString("validity_time").substring(18,27):startTime;
                    System.out.println("开始时间："+startTime+" 结束时间："+endTime +"列表中的领取时间时间:"+sendTime);
                    System.out.println("validityTime开始时间："+validityTimeStart+" 结束时间："+validityTimeEnd);
                    Preconditions.checkArgument(sendTime.compareTo(startTime)>=0&&sendTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的领取时间时间:"+sendTime);
                    Preconditions.checkArgument(validityTimeStart.compareTo(startTime)>=0&&validityTimeStart.compareTo(endTime)<=0&&validityTimeEnd.compareTo(startTime)>=0&&validityTimeEnd.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的有效的开始时间:"+validityTimeStart+"列表中的有效的结束时间:"+validityTimeEnd);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("优惠券领取记录-时间的筛选");
        }
    }

    /**
     *@deprecated V2.0-优惠券领取记录--筛选栏全部填写搜索
     *  @date :2021-2-2
     */
    @Test(enabled = false)
    public void voucherManageSendRecordAllFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject jsonObject=jc.oucherFormVoucherPage(shopId,"1","10");
            String id = jsonObject.getJSONArray("list").getJSONObject(0).getString("id");
            Object[][] flag = Constant.voucherManageSendRecordFilter_pram();
            VoucherManageSendVariable variable = new VoucherManageSendVariable();
            JSONArray res = jc.voucherManageSendRecord( "1", "10", "","", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.receiver = data.getString(flag[0][1].toString());
                variable.receivePhone = data.getString(flag[1][1].toString());
                variable.useStatus = data.getString(flag[2][1].toString());
                variable.customerLabel = data.getString(flag[3][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.id=id;
                //全部筛选之后的结果
                JSONObject result = jc.voucherManageSendRecord(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains( variable.receiver),"参数全部输入的查询的"+ variable.receiver+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.receivePhone), "参数全部输入的查询的" + variable.receivePhone + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.useStatus), "参数全部输入的查询的" + variable.useStatus + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.customerLabel), "参数全部输入的查询的" + variable.customerLabel + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
               }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0-优惠券领取记录全部查询，结果校验");
        }
    }

    /**
     * @deprecated V2.0-优惠券领取记录--筛选栏多项填写搜索
     *  @date :2021-2-2
     */
    @Test(enabled = false)
    public void voucherManageSendRecordSomeFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject jsonObject=jc.oucherFormVoucherPage(shopId,"1","10");
            String id = jsonObject.getJSONArray("list").getJSONObject(0).getString("id");
            Object[][] flag = Constant.voucherManageSendRecordFilter_pram();
            VoucherManageSendVariable variable = new VoucherManageSendVariable();
            JSONArray res = jc.voucherManageSendRecord( "1", "10","", "", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.receiver = data.getString(flag[0][1].toString());
                variable.receivePhone = data.getString(flag[1][1].toString());
//                variable.useStatus = data.getString(flag[2][1].toString());
//                variable.customerLabel = data.getString(flag[7][1].toString());
                variable.id=id;
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.voucherManageSendRecord(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains( variable.receiver),"参数全部输入的查询的"+ variable.receiver+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.receivePhone), "参数全部输入的查询的" + variable.receivePhone + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.useStatus), "参数全部输入的查询的" + variable.useStatus + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[7][1])).contains(variable.customerLabel), "参数全部输入的查询的" + variable.customerLabel + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0-优惠券领取记录全部查询，结果校验");
        }
    }
    /**
     * @description :V2.0-优惠券领取记录--筛选栏不填写查询
     * @date :2021-2-1
     **/
    @Test()
    public void voucherManageSendRecordEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject jsonObject=jc.oucherFormVoucherPage("","1","10");
            String id = jsonObject.getJSONArray("list").getJSONObject(0).getString("id");
            JSONArray res = jc.voucherManageSendRecord("1","10",id,"","").getJSONArray("list");
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0-优惠券领取记录--筛选栏不填写查询");
        }
    }

    /**
     * @deprecated V2.0-优惠券作废记录--筛选栏单项搜索
     * @date :2021-2-2
     */
    @Test(dataProvider = "SELECT_voucherInvalidPageFilter", dataProviderClass = Constant.class)
    public void voucherInvalidPageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject jsonObject=jc.oucherFormVoucherPage("","1","10");
            String id = jsonObject.getJSONArray("list").getJSONObject(0).getString("id");
            JSONObject response = jc.voucherInvalidPage("", "1", "10",id, "", "");
            if (response.getJSONArray("list").size() > 0) {
                String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject response1 = jc.voucherInvalidPage("", "1", "10",id,pram, result);
                int pages = response1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.voucherInvalidPage("", String.valueOf(page),"10",id, pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        System.out.println("优惠券作废记录按" + result + "查询，结果错误" + Flag);
                        Preconditions.checkArgument(Flag.contains(result), "优惠券作废记录按" + result + "查询，结果错误" + Flag);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0-优惠券作废记录--筛选栏单项搜索");
        }
    }

    /**
     * @description 优惠券作废记录-时间的筛选
     * @date :2020/12/16
     **/
    @Test(enabled = true)
    public void voucherInvalidRecordTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject jsonObject=jc.oucherFormVoucherPage("","1","10");
            String id = jsonObject.getJSONArray("list").getJSONObject(0).getString("id");
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.voucherInvalidPage("1","10",id,startTime,endTime,startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.voucherInvalidPage(String.valueOf(page),"10",id,startTime,endTime,startTime,endTime).getJSONArray("list");
                System.err.println(list);
                for (int i = 0; i < list.size(); i++) {
                    String sendTime = list.getJSONObject(i).containsKey("send_time")?list.getJSONObject(i).getString("send_time").substring(0,10):startTime;
                    String invalidTime = list.getJSONObject(i).containsKey("invalid_time")?list.getJSONObject(i).getString("invalid_time").substring(0,10):startTime;
                    Preconditions.checkArgument(sendTime.compareTo(startTime)>=0&&sendTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的领取时间时间:"+sendTime);
                    Preconditions.checkArgument(invalidTime.compareTo(startTime)>=0&&invalidTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的有效的开始时间:"+invalidTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("优惠券作废记录-时间的筛选");
        }
    }

    /**
     * @deprecated V2.0-优惠券作废记录--筛选栏全部填写搜索
     * @date :2021-2-2
     */
    @Test(enabled = false)
    public void voucherInvalidPageAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject jsonObject=jc.oucherFormVoucherPage(shopId,"1","10");
            String id = jsonObject.getJSONArray("list").getJSONObject(0).getString("id");
            Object[][] flag = Constant.voucherInvalidPageFilter_pram();
            VoucherInvalidPageVariable variable = new VoucherInvalidPageVariable();
            JSONArray res = jc.voucherInvalidPage("", "1", "10", "","", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.receiver = data.getString(flag[0][1].toString());
                variable.receivePhone = data.getString(flag[1][1].toString());
                variable.invalidName = data.getString(flag[2][1].toString());
                variable.invalidPhone = data.getString(flag[3][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.id=id;
                //全部筛选之后的结果
                JSONObject result = jc.voucherInvalidPage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains( variable.receiver),"参数输入的查询的"+ variable.receiver+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.receivePhone), "参数输入的查询的" + variable.receivePhone + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.invalidName), "参数输入的查询的" + variable.invalidName + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.invalidPhone), "参数输入的查询的" + variable.invalidPhone + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res == null, "优惠券作废记录系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0-优惠券作废记录--筛选栏全部填写搜索");
        }
    }

    /**
     * @deprecated V2.0-优惠券作废记录--筛选栏多项填写搜索
     * @date :2021-2-2
     */
    @Test(enabled = false)
    public void voucherInvalidPageSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject jsonObject=jc.oucherFormVoucherPage(shopId,"1","10");
            String id = jsonObject.getJSONArray("list").getJSONObject(0).getString("id");
            Object[][] flag = Constant.voucherInvalidPageFilter_pram();
            VoucherInvalidPageVariable variable = new VoucherInvalidPageVariable();
            JSONArray res = jc.voucherInvalidPage("", "1", "10", "","", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
//                variable.receiver = data.getString(flag[0][1].toString());
//                variable.receivePhone = data.getString(flag[1][1].toString());
                variable.invalidName = data.getString(flag[2][1].toString());
                variable.invalidPhone = data.getString(flag[3][1].toString());
                variable.page = "1";
                variable.size = "10";
                variable.id=id;
                //全部筛选之后的结果
                JSONObject result = jc.voucherInvalidPage(variable).getJSONArray("list").getJSONObject(0);
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains( variable.receiver),"参数输入的查询的"+ variable.receiver+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.receivePhone), "参数输入的查询的" + variable.receivePhone + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.invalidName), "参数输入的查询的" + variable.invalidName + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.invalidPhone), "参数输入的查询的" + variable.invalidPhone + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res == null, "优惠券作废记录系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0-优惠券作废记录--筛选栏多项填写搜索");
        }
    }

    /**
     * @description :V2.0-优惠券作废记录--筛选栏不填写查询
     * @date :2021-2-1
     **/
    @Test()
    public void voucherInvalidPageEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject jsonObject=jc.oucherFormVoucherPage("","1","10");
            String id = jsonObject.getJSONArray("list").getJSONObject(0).getString("id");
            JSONArray res = jc.voucherInvalidPage("","1","10",id,"","").getJSONArray("list");
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0-优惠券作废记录--筛选栏不填写查询");
        }
    }

    /**
     * @deprecated V2.0道路救援--筛选栏单项搜索
     * @date :2021-2-2
     */
    @Test(dataProvider = "SELECT_rescuePageFilter", dataProviderClass = Constant.class)
    public void rescuePageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.rescuePage("", "1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                if(pram.equals("vip_type")){
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    String result1 = response.getJSONArray("list").getJSONObject(0).getString(output).equals("vip会员")?"10":"1";
                    JSONObject response1 = jc.rescuePage("", "1", "10",pram, result1);
                    int pages = response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.rescuePage("", String.valueOf(page),"10", pram, result1).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "道路救援列表按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }else if(pram.equals("shop_id")){
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    String shopId=businessUtil.shopNameTransformId(result);
                    String name=businessUtil.getShopNameExist(result);
                    System.out.println(shopId+"-----"+name);
                    JSONObject response1 = jc.rescuePage("", "1", "10",pram, shopId);
                    int pages = response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.rescuePage("", String.valueOf(page),"10", pram, shopId).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            System.out.println("道路救援列表按" + name + "查询，结果错误" + Flag);
                            Preconditions.checkArgument(Flag.contains(name), "道路救援列表按" + name + "查询，结果错误" + Flag);
                        }
                    }
                }else{
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    JSONObject response1 = jc.rescuePage("", "1", "10",pram, result);
                    int pages = response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.rescuePage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            Preconditions.checkArgument(Flag.contains(result), "道路救援列表按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }
            } else {
                Preconditions.checkArgument(response.getJSONArray("list") == null, "道路救援列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0道路救援--筛选栏单项搜索");
        }
    }

    /**
     * @description 道路救援-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void rescuePageTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.rescuePage1("","1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.rescuePage1("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String dialTime = list.getJSONObject(i).containsKey("dial_time")?list.getJSONObject(i).getString("dial_time").substring(0,10):startTime;
                    System.out.println("开始时间："+startTime+" 结束时间："+endTime +"列表中的推送时间:"+dialTime);
                    Preconditions.checkArgument(dialTime.compareTo(startTime)>=0&&dialTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的时间:"+dialTime);
                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("道路救援-时间的筛选，结果校验");
        }
    }

    /**
     * @deprecated V2.0道路救援列表记录--筛选栏全部填写搜索
     * @date :2021-2-2
     */
    @Test(enabled = false)
    public void rescuePageAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.rescuePageFilter_pram();
            RescuePageVariable variable = new RescuePageVariable();
            JSONArray res = jc.rescuePage("", "1", "10","", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.customerName = data.getString(flag[0][1].toString());
                variable.vipType = data.getString(flag[1][1].toString());
                variable.customerPhone = data.getString(flag[2][1].toString());
                variable.shopId = data.getString(flag[3][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.rescuePage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(  variable.customerName),"参数输入的查询的"+ variable.customerName+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.vipType), "参数输入的查询的" + variable.vipType + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.customerPhone), "参数输入的查询的" + variable.customerPhone + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.shopId), "参数输入的查询的" + variable.shopId + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res == null, "道路救援列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0道路救援列表记录--筛选栏全部填写搜索");
        }
    }

    /**
     * @deprecated V2.0道路救援列表记录--筛选栏多项填写搜索
     * @date :2021-2-2
     */
    @Test(enabled = false)
    public void rescuePageSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.rescuePageFilter_pram();
            RescuePageVariable variable = new RescuePageVariable();
            JSONArray res = jc.rescuePage("", "1", "10","", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.customerName = data.getString(flag[0][1].toString());
                variable.vipType = data.getString(flag[1][1].toString());
//                variable.customerPhone = data.getString(flag[2][1].toString());
//                variable.shopId = data.getString(flag[3][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.rescuePage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(  variable.customerName),"参数输入的查询的"+ variable.customerName+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.vipType), "参数输入的查询的" + variable.vipType + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.customerPhone), "参数输入的查询的" + variable.customerPhone + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.shopId), "参数输入的查询的" + variable.shopId + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res == null, "优惠券作废记录系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0道路救援列表记录--筛选栏多项填写搜索");
        }
    }
    /**
     * @description :V2.0道路救援列表记录--筛选栏不填写查询
     * @date :2021-2-1
     **/
    @Test()
    public void rescuePageEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray res = jc.rescuePage("","1","10","","").getJSONArray("list");
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0道路救援列表记录--筛选栏不填写查询");
        }
    }

    /**
     * @deprecated V2.0评价列表--筛选栏单项搜索      是否留言有问题，已提bug【7884】
     * @date :2021-2-2
     */
    @Test(dataProvider = "SELECT_evaluatePageFilter", dataProviderClass = Constant.class)
    public void evaluatePageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.evaluatePage("", "1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                if(pram.equals("evaluate_type")){
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output).equals("维修评价")?"维修评价":"新车评价";
                    String result1 = response.getJSONArray("list").getJSONObject(0).getString(output).equals("维修评价")?"2":"3";
                    JSONObject response1 = jc.evaluatePage("", "1", "10",pram, result1);
                    int pages = response1.getInteger("pages")>10?10: response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.evaluatePage("", String.valueOf(page),"10", pram, result1).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            System.out.println("评价列表按" + result + "查询，结果错误" + Flag);
                            Preconditions.checkArgument(Flag.contains(result), "评价列表按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }else if(pram.equals("is_follow_up")){
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    String result1="true";//response.getJSONArray("list").getJSONObject(0).getString(output).isEmpty()?"false":"true";
                    JSONObject response1 = jc.evaluatePage("", "1", "10",pram, result1);
                    int pages = response1.getInteger("pages")>10?10: response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.evaluatePage("", String.valueOf(page),"10", pram, result1).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            if(result1.equals("false")){
                                Preconditions.checkArgument(Flag.isEmpty(), "评价列列表按照未跟进筛选,结果不为空");
                            }else{
                                Preconditions.checkArgument(!Flag.isEmpty(), "评价列列表按照已跟进筛选,结果为空");

                            }
                        }
                    }
                }else if(pram.equals("is_have_msg")){
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    String result1="true";//response.getJSONArray("list").getJSONObject(0).getString(output).isEmpty()?"false":"true";
                    JSONObject response1 = jc.evaluatePage("", "1", "10",pram, result1);
                    int pages = response1.getInteger("pages")>10?10: response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.evaluatePage("", String.valueOf(page),"10", pram, result1).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            System.out.println("评价列表按" + result1 + "查询，结果错误" + Flag);
                            if(result1.equals("false")){
                                Preconditions.checkArgument(Flag.isEmpty(), "评价列表按未留言筛选,查询结果不为空" );
                            }else{
                                Preconditions.checkArgument(!Flag.isEmpty(), "评价列表按已留言筛选,查询结果为空");
                            }
                        }
                    }
                }else if(pram.equals("service_sale_id")){
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    String saleId=businessUtil.authNameTransformId(result,"AFTER_SALE_RECEPTION");
                    String name=businessUtil.getAuthNameExist(result,"AFTER_SALE_RECEPTION");
                    JSONObject response1 = jc.evaluatePage("", "1", "10",pram, saleId);
                    System.out.println(saleId+"------"+response1);
                    int pages = response1.getInteger("pages")>10?10:response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.evaluatePage("", String.valueOf(page),"10", pram, saleId).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            System.out.println("评价列表按" + name + "查询，结果错误" + Flag);
                            Preconditions.checkArgument(Flag.contains(name), "评价列表按" + name + "查询，结果错误" + Flag);
                        }
                    }
                }else if(pram.equals("plate_number")){
                    String result = response.getJSONArray("list").getJSONObject(0).containsKey("plate_number")?response.getJSONArray("list").getJSONObject(0).getString(output):"浙A12345";
                    JSONObject response1 = jc.evaluatePage("", "1", "10",pram, result);
                    int pages = response1.getInteger("pages")>10?10:response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.evaluatePage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            System.out.println("评价列表按" + result + "查询，结果错误" + Flag);
                            Preconditions.checkArgument(Flag.contains(result), "评价列表按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }else{
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    JSONObject response1 = jc.evaluatePage("", "1", "10",pram, result);
                    int pages = response1.getInteger("pages")>10?10:response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.evaluatePage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            System.out.println("评价列表按" + result + "查询，结果错误" + Flag);
                            Preconditions.checkArgument(Flag.contains(result), "评价列表按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }
            } else {
                Preconditions.checkArgument(response.getJSONArray("list") == null, "评价列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0评价列表--筛选栏单项搜索");
        }
    }

    /**
     * @description 评价列表-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void evaluatePageTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.evaluatePage("","1","10",startTime,endTime,startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.evaluatePage("", String.valueOf(page),"10",startTime,endTime,startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String evaluateTime = list.getJSONObject(i).containsKey("evaluate_time")?list.getJSONObject(i).getString("evaluate_time").substring(0,10):startTime;
                    String sourceCreateTime = list.getJSONObject(i).containsKey("source_create_time")?list.getJSONObject(i).getString("source_create_time").substring(0,10):startTime;
                    Preconditions.checkArgument(evaluateTime.compareTo(startTime)>=0&&evaluateTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的评价时间:"+evaluateTime);
                    Preconditions.checkArgument(sourceCreateTime.compareTo(startTime)>=0&&sourceCreateTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的任务时间:"+sourceCreateTime);

                };
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("评价列表-时间的筛选，结果校验");
        }
    }

    /**
     * @deprecated V2.0评价列表--筛选栏全部填写搜索
     * @date :2021-2-2
     */
    @Test(enabled = false)
    public void evaluatePageAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.evaluatePageFilter_pram();
            EvaluatePageVariable variable = new EvaluatePageVariable();
            JSONArray res = jc.evaluatePage("", "1", "10","", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.plateNumber = data.getString(flag[0][1].toString());
                variable.serviceSaleId = data.getString(flag[1][1].toString());
                variable.evaluateType = data.getString(flag[2][1].toString());
                variable.shopId = data.getString(flag[3][1].toString());
                variable.customerName = data.getString(flag[4][1].toString());
                variable.score = data.getString(flag[5][1].toString());
                variable.isFollowUp = data.getString(flag[6][1].toString());
                variable.customerPhone = data.getString(flag[7][1].toString());
                variable.isHaveMsg = data.getString(flag[8][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.evaluatePage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(  variable.plateNumber),"参数输入的查询的"+ variable.plateNumber+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.serviceSaleId), "参数输入的查询的" + variable.serviceSaleId + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.evaluateType), "参数输入的查询的" + variable.evaluateType + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.shopId), "参数输入的查询的" + variable.shopId + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[4][1])).contains(  variable.customerName),"参数输入的查询的"+ variable.customerName+"与列表信息的第一行的"+result.getString(flag[4][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[5][1])).contains(variable.score), "参数输入的查询的" + variable.score + "与列表信息的第一行的" + result.getString(flag[5][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[6][1])).contains(variable.isFollowUp), "参数输入的查询的" + variable.isFollowUp + "与列表信息的第一行的" + result.getString(flag[6][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[7][1])).contains(variable.customerPhone), "参数输入的查询的" + variable.customerPhone + "与列表信息的第一行的" + result.getString(flag[7][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[8][1])).contains(variable.isHaveMsg), "参数输入的查询的" + variable.isHaveMsg + "与列表信息的第一行的" + result.getString(flag[8][1].toString()) + "不一致");

            } else {
                Preconditions.checkArgument(res == null, "道路救援列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0评价列表--筛选栏全部填写搜索");
        }
    }

    /**
     * @deprecated V2.0评价列表--筛选栏多项填写搜索
     * @date :2021-2-2
     */
    @Test(enabled = false)
    public void evaluatePageSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.evaluatePageFilter_pram();
            EvaluatePageVariable variable = new EvaluatePageVariable();
            JSONArray res = jc.evaluatePage("", "1", "10","", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.plateNumber = data.getString(flag[0][1].toString());
                variable.serviceSaleId = data.getString(flag[1][1].toString());
                variable.evaluateType = data.getString(flag[2][1].toString());
                variable.shopId = data.getString(flag[3][1].toString());
                variable.customerName = data.getString(flag[4][1].toString());
                variable.score = data.getString(flag[5][1].toString());
//                variable.isFollowUp = data.getString(flag[6][1].toString());
//                variable.customerPhone = data.getString(flag[7][1].toString());
//                variable.isHaveMsg = data.getString(flag[8][1].toString());
                variable.page = "1";
                variable.size = "10";
                //筛选之后的结果
                JSONObject result = jc.evaluatePage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(  variable.plateNumber),"参数输入的查询的"+ variable.plateNumber+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.serviceSaleId), "参数输入的查询的" + variable.serviceSaleId + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.evaluateType), "参数输入的查询的" + variable.evaluateType + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[3][1])).contains(variable.shopId), "参数输入的查询的" + variable.shopId + "与列表信息的第一行的" + result.getString(flag[3][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[4][1])).contains(  variable.customerName),"参数输入的查询的"+ variable.customerName+"与列表信息的第一行的"+result.getString(flag[4][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[5][1])).contains(variable.score), "参数输入的查询的" + variable.score + "与列表信息的第一行的" + result.getString(flag[5][1].toString()) + "不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[6][1])).contains(variable.isFollowUp), "参数输入的查询的" + variable.isFollowUp + "与列表信息的第一行的" + result.getString(flag[6][1].toString()) + "不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[7][1])).contains(variable.customerPhone), "参数输入的查询的" + variable.customerPhone + "与列表信息的第一行的" + result.getString(flag[7][1].toString()) + "不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[8][1])).contains(variable.isHaveMsg), "参数输入的查询的" + variable.isHaveMsg + "与列表信息的第一行的" + result.getString(flag[8][1].toString()) + "不一致");

            } else {
                Preconditions.checkArgument(res == null, "评价列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0评价列表--筛选栏多项填写搜索");
        }
    }

    /**
     * @description :V2.0评价列表记录--筛选栏不填写查询
     * @date :2021-2-1
     **/
    @Test()
    public void evaluatePageEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray res = jc.evaluatePage("","1","10","","").getJSONArray("list");
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0评价列表记录--筛选栏不填写查询");
        }
    }

    /**
     * @deprecated V2.0商城套餐列表--筛选栏单项搜索（只有一个参数）
     * @date :2021-2-2
     */
    @Test(dataProvider = "SELECT_storeCommodityPageFilter", dataProviderClass = Constant.class)
    public void storeCommodityPageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.storeCommodityPage("", "1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject response1 = jc.storeCommodityPage("", "1", "10",pram, result);
                int pages = response1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.storeCommodityPage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "商城套餐列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(response.getJSONArray("list") == null, "商城套餐列表-系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0商城套餐列表--筛选栏单项搜索");
        }
    }

    /**
     * @description 精品套餐-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void storeCommodityPageTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.storeCommodityPage1("","1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.storeCommodityPage1("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String createDate = list.getJSONObject(i).containsKey("create_date")?list.getJSONObject(i).getString("create_date").substring(0,10):startTime;
                    Preconditions.checkArgument(createDate.compareTo(startTime)>=0&&createDate.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的支付时间:"+createDate);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("精品套餐-时间的筛选，结果校验");
        }
    }


    /**
     * @deprecated V2.0精品商城-商城订单--筛选栏单项搜索
     * @date :2021-2-3
     */
    @Test(dataProvider = "SELECT_storeOrderPageFilter", dataProviderClass = Constant.class)
    public void storeOrderPageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.storeOrderPage("", "1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                String result = response.getJSONArray("list").getJSONObject(0).containsKey(output)?response.getJSONArray("list").getJSONObject(0).getString(output):"";
                JSONObject response1 = jc.storeOrderPage("", "1", "10",pram, result);
                int pages = response1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.storeOrderPage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag =list.getJSONObject(i).containsKey(output)? list.getJSONObject(i).getString(output):"";
                        System.out.println("商城订单列表按" + result + "查询，结果错误" + Flag);
                        Preconditions.checkArgument(Flag.contains(result), "商城订单列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(response.getJSONArray("list") == null, "商城订单列表-系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0精品商城-商城订单--筛选栏单项搜索");
        }
    }

    /**
     * @description 精品商城-时间的筛选
     * @date :2020/12/16
     **/
    @Test
    public void storeOrderPageTimeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject respond=jc.storeOrderPage1("","1","10",startTime,endTime);
            int pages = respond.getInteger("pages")>10?10:respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.storeOrderPage1("", String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String payTime = list.getJSONObject(i).containsKey("pay_time")?list.getJSONObject(i).getString("pay_time").substring(0,10):startTime;
                    Preconditions.checkArgument(payTime.compareTo(startTime)>=0&&payTime.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的支付时间:"+payTime);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("精品商城-时间的筛选，结果校验");
        }
    }

    /**
     * @deprecated V2.0商城订单列表--筛选栏全部填写校验
     * @date :2021-2-2
     */
    @Test(enabled = false)
    public void storeOrderPageAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.storeOrderPageFilter_pram();
            StoreOrderPageVariable variable = new StoreOrderPageVariable();
            JSONArray res = jc.storeOrderPage("", "1", "10","", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.bindPhone = data.getString(flag[0][1].toString());
                variable.commodityName = data.getString(flag[1][1].toString());
                variable.orderNumber = data.getString(flag[2][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.storeOrderPage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(  variable.bindPhone),"参数输入的查询的"+ variable.bindPhone+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.commodityName), "参数输入的查询的" + variable.commodityName + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.orderNumber), "参数输入的查询的" + variable.orderNumber + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");

            } else {
                Preconditions.checkArgument(res == null, "商城订单列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0商城订单列表--筛选栏全部填写校验");
        }
    }

    /**
     * @deprecated V2.0商城订单列表--筛选栏多项填写校验
     * @date :2021-2-2
     */
    @Test(enabled = false)
    public void storeOrderPageSomeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.storeOrderPageFilter_pram();
            StoreOrderPageVariable variable = new StoreOrderPageVariable();
            JSONArray res = jc.storeOrderPage("", "1", "10","", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.bindPhone = data.getString(flag[0][1].toString());
                variable.commodityName = data.getString(flag[1][1].toString());
//                variable.orderNumber = data.getString(flag[2][1].toString());
                variable.page = "1";
                variable.size = "10";
                //筛选之后的结果
                JSONObject result = jc.storeOrderPage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(  variable.bindPhone),"参数输入的查询的"+ variable.bindPhone+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.commodityName), "参数输入的查询的" + variable.commodityName + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
//                Preconditions.checkArgument(result.getString(String.valueOf(flag[2][1])).contains(variable.orderNumber), "参数输入的查询的" + variable.orderNumber + "与列表信息的第一行的" + result.getString(flag[2][1].toString()) + "不一致");

            } else {
                Preconditions.checkArgument(res == null, "商城订单列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0商城订单列表--筛选栏多项填写校验");
        }
    }
    /**
     * @description :V2.0商城订单列表--筛选栏不填写查询
     * @date :2021-2-3
     **/
    @Test()
    public void storeOrderPageEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray res = jc.storeOrderPage("","1","10","","").getJSONArray("list");
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0商城订单列表--筛选栏不填写查询");
        }
    }
    /**
     * @deprecated V2.0精品商城-分销员管理--筛选栏单项搜索
     * @date :2021-2-3
     */
    @Test(dataProvider = "SELECT_storeSalesPageFilter", dataProviderClass = Constant.class)
    public void storeSalesPageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.storeSalesPage("", "1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                if(pram.equals("shop_id")){
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    String shopId=businessUtil.shopNameTransformId(result);
                    String name=businessUtil.getShopNameExist(result);
                    JSONObject response1 = jc.storeSalesPage("", "1", "10",pram, shopId);
                    int pages = response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.storeSalesPage("", String.valueOf(page),"10", pram, shopId).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            System.out.println("分销员管理列表按" + name + "查询，结果错误" + Flag);
                            Preconditions.checkArgument(Flag.contains(name), "分销员管理列表按" + name + "查询，结果错误" + Flag);
                        }
                    }
                }else{
                    String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                    JSONObject response1 = jc.storeSalesPage("", "1", "10",pram, result);
                    int pages = response1.getInteger("pages");
                    for (int page = 1; page <= pages; page++) {
                        JSONArray list = jc.storeSalesPage("", String.valueOf(page),"10", pram, result).getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            String Flag = list.getJSONObject(i).getString(output);
                            System.out.println("分销员管理列表按" + result + "查询，结果错误" + Flag);
                            Preconditions.checkArgument(Flag.contains(result), "分销员管理列表按" + result + "查询，结果错误" + Flag);
                        }
                    }
                }
            } else {
                Preconditions.checkArgument(response.getJSONArray("list") == null, "分销员管理列表-系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0精品商城-分销员管理--筛选栏单项搜索");
        }
    }

    /**
     * @deprecated V2.0分销员管理--筛选栏全部填写校验
     * @date :2021-2-2
     */
    @Test(enabled = false)
    public void storeSalesPageAllFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] flag = Constant.storeSalesPageFilter_pram();
            StoreSalesPageVariable variable = new StoreSalesPageVariable();
            JSONArray res = jc.storeSalesPage("", "1", "10","", "").getJSONArray("list");
            if (res.size() > 0) {
                JSONObject data = res.getJSONObject(0);
                variable.salesPhone = data.getString(flag[0][1].toString());
                variable.shopId = data.getString(flag[1][1].toString());
                variable.page = "1";
                variable.size = "10";
                //全部筛选之后的结果
                JSONObject result = jc.storeSalesPage(variable).getJSONArray("list").getJSONObject(0);
                Preconditions.checkArgument(result.getString(String.valueOf(flag[0][1])).contains(  variable.salesPhone),"参数输入的查询的"+ variable.salesPhone+"与列表信息的第一行的"+result.getString(flag[0][1].toString())+"不一致");
                Preconditions.checkArgument(result.getString(String.valueOf(flag[1][1])).contains(variable.shopId), "参数输入的查询的" + variable.shopId + "与列表信息的第一行的" + result.getString(flag[1][1].toString()) + "不一致");
            } else {
                Preconditions.checkArgument(res == null, "分销员管理列表系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0分销员管理列表--筛选栏全部填写校验");
        }
    }
    /**
     * @description :V2.0分销员管理列表--筛选栏不填写查询
     * @date :2021-2-3
     **/
    @Test()
    public void storeSalesPageEmptyFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONArray res = jc.storeSalesPage("","1","10","","").getJSONArray("list");
        }catch(AssertionError|Exception e){
            appendFailReason(e.toString());
        }finally{
            saveData("V2.0商城订单列表--筛选栏不填写查询");
        }
    }

    /**
     * @deprecated V2.0活动管理列表/活动审批--筛选栏单项搜索
     * @date :2021-2-3
     */
    @Test(dataProvider = "SELECT_activityManagePageFilter", dataProviderClass = Constant.class)
    public void activityManagePageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.activityPage( "1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject response1 = jc.activityPage( "1", "10",pram, result);
                int pages = response1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.activityPage( String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "分销员管理列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            } else {
                Preconditions.checkArgument(response.getJSONArray("list") == null, "分销员管理列表-系统错误,请联系开发人员");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData(" V2.0活动管理列表--筛选栏单项搜索 ");
        }
    }

    /**
     * @deprecated V2.0活动管理-报名管理列表--筛选栏单项搜索
     * @date :2021-2-3
     */
    @Test(dataProvider = "SELECT_registerPageFilter", dataProviderClass = Constant.class)
    public void registerPageOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String activityId=jc.activityPage( "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("id");
            JSONObject response = jc.registerPage(activityId,"1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject response1 = jc.registerPage(activityId, "1", "10",pram, result);
                int pages = response1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.registerPage(activityId, String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "报名管理列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V2.0活动管理-报名管理列表--筛选栏单项搜索 ");
        }
    }


    /**
     * @deprecated V3.0积分中心-积分客户管理--筛选栏单项搜索
     * @date :2021-3-16
     */
    @Test(enabled = false)
    public void integralCenterCustomerPageOneFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response=jc.integralCenterCustomerPage(1,10,"");
            JSONArray list=response.getJSONArray("list");
            String phone=list.getJSONObject(0).containsKey("customer_name")?list.getJSONObject(0).getString("customer_name"):list.getJSONObject(1).getString("customer_name");
            JSONObject response1=jc.integralCenterCustomerPage(1,10,phone);
            JSONArray list1=response1.getJSONArray("list");
            String phone1=list1.getJSONObject(0).containsKey("customer_name")?list1.getJSONObject(0).getString("customer_name"):list1.getJSONObject(1).getString("customer_name");
            Preconditions.checkArgument(phone.equals(phone),"积分客户管理按照："+phone+" 查询，结果为："+phone1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V3.0积分中心-积分客户管理--筛选栏单项搜索 ");
        }
    }

    /**
     * @deprecated V3.0积分中心-客户积分变更记录--筛选栏单项搜索
     * @date :2021-3-16
     */
    @Test(enabled = false)
    public void customerIntegralChangeRecordPageOneFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response=jc.customerIntegralChangeRecordPage(1,10,"","","");
            JSONArray list=response.getJSONArray("list");
            String phone=list.getJSONObject(0).containsKey("customer_name")?list.getJSONObject(0).getString("customer_name"):list.getJSONObject(1).getString("customer_name");
            JSONObject response1=jc.customerIntegralChangeRecordPage(1,10,phone,"","");
            JSONArray list1=response1.getJSONArray("list");
            String phone1=list1.getJSONObject(0).containsKey("customer_name")?list1.getJSONObject(0).getString("customer_name"):list1.getJSONObject(1).getString("customer_name");
            Preconditions.checkArgument(phone.equals(phone),"客户积分变更记录按照："+phone+" 查询，结果为："+phone1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V3.0积分中心-客户积分变更记录--筛选栏单项搜索 ");
        }
    }

    /**
     * @deprecated V3.0积分中心-客户积分变更记录--筛选栏时间搜索
     * @date :2021-3-16
     */
    @Test(enabled = false)
    public void customerIntegralChangeRecordPageTimeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject response=jc.customerIntegralChangeRecordPage(1,10,"",startTime,endTime);
            int pages = response.getInteger("pages")>10?10:response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.customerIntegralChangeRecordPage(page,10,"",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String time = list.getJSONObject(i).containsKey("time")?list.getJSONObject(i).getString("time").substring(0,10):startTime;
                    Preconditions.checkArgument(time.compareTo(startTime)>=0&&time.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的支付时间:"+time);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V3.0积分中心-客户积分变更记录--筛选栏时间搜索 ");
        }
    }



    /**
     * @deprecated V3.0卡券管理-增发记录-筛选栏单项搜索
     * @date :2021-3-16
     */
    @Test(dataProvider = "SELECT_voucherManageAdditionalRecordFilter", dataProviderClass = Constant.class)
    public void voucherManageAdditionalRecordOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = jc.additionalRecordPage("1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject response1 = jc.additionalRecordPage( "1", "10",pram, result);
                int pages = response1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = jc.additionalRecordPage(String.valueOf(page),"10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        Preconditions.checkArgument(Flag.contains(result), "卡券管理-增发记录列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V3.0卡券管理-增发记录-筛选栏单项搜索 ");
        }
    }

    /**
     * @deprecated V3.0卡券管理-增发记录-筛选栏时间搜索
     * @date :2021-3-16
     */
    @Test(enabled = false)
    public void voucherManageAdditionalRecordTimeFilter(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime=  dt.getHistoryDate(-30);
            String endTime=  dt.getHistoryDate(30);
            JSONObject response=jc.additionalRecordTimePage("1","10",startTime,endTime);
            int pages = response.getInteger("pages")>10?10:response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.additionalRecordTimePage(String.valueOf(page),"10",startTime,endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String time = list.getJSONObject(i).containsKey("time")?list.getJSONObject(i).getString("time").substring(0,10):startTime;
                    Preconditions.checkArgument(time.compareTo(startTime)>=0&&time.compareTo(endTime)<=0, "开始时间："+startTime+" 结束时间："+endTime +"列表中的增发时间:"+time);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("V3.0卡券管理-增发记录-筛选栏时间搜索 ");
        }
    }









}
