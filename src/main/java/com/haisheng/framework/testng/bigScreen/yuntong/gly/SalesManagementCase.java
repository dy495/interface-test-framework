package com.haisheng.framework.testng.bigScreen.yuntong.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.yuntong.gly.Util.Constant;
import com.haisheng.framework.testng.bigScreen.yuntong.gly.Util.YunTongUtil;
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

public class SalesManagementCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.YT_DAILY;
    YunTongUtil yt=YunTongUtil.getInstance(product);


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.product = product.getAbbreviation();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "YT-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.referer =product.getReferer();
        commonConfig.shopId = product.getShopId();
        commonConfig.roleId =product.getRoleId();
        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @deprecated :get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }


    /**
     * @deprecated 销售客户接待列表-筛选栏单项搜索
     * @date :2021-6-4
     */
    @Test(dataProvider = "SELECT_preSalesReceptionPageRecordFilter", dataProviderClass = Constant.class,enabled = true)
    public void preSaleCustomerPageRecordOneFilter(String pram,String output){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = yt.preSalesReceptionPage("1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                System.out.println(pram+"   "+output);
                String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                System.err.println(result);
                JSONObject response1 = yt.preSalesReceptionPage("1", "10", pram, result);
                int pages = response1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = yt.preSalesReceptionPage(String.valueOf(page), "10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        System.out.println("销售客户接待列表按" + result + "查询，结果错误" + Flag);
                        Preconditions.checkArgument(Flag.contains(result), "销售客户接待列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户接待列表-筛选栏单项搜索 ");
        }
    }

    /**
     * 销售客户列表项校验
     */
    @Test
    public void preSaleCustomerPage(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = yt.preSalesReceptionPage("1", "10", "", "");
            int pages=response.getInteger("pages");
            for(int page=1;page<=pages;page++){
               JSONArray list= yt.preSalesReceptionPage("1", "10", "", "").getJSONArray("list");
               for(int i=0;i<list.size();i++){
                    String shopName=list.getJSONObject(i).getString("shop_name");
                   String brandName=list.getJSONObject(i).getString("brand_name");
                   String subjectType=list.getJSONObject(i).getString("subject_type");
                   String customerTypeName=list.getJSONObject(i).getString("customer_type_name");
                   String registrationStatus=list.getJSONObject(i).getString("registration_status");
                   String customerName=list.getJSONObject(i).getString("customer_name");
                   String customerPhone=list.getJSONObject(i).getString("customer_phone");
                   String intentionCarModelName=list.getJSONObject(i).getString("intention_car_model_name");
                   String sex=list.getJSONObject(i).getString("sex");
                   String intentionCarStyleName=list.getJSONObject(i).getString("intention_car_style_name");
                   String createDate=list.getJSONObject(i).getString("create_date");
                   String saleName=list.getJSONObject(i).getString("sale_name");
                   String salePhone=list.getJSONObject(i).getString("sale_phone");
                   Preconditions.checkArgument(shopName!=null&&brandName!=null&&subjectType!=null&&customerTypeName!=null&&registrationStatus!=null&&customerName!=null&&customerPhone!=null&&intentionCarModelName!=null&&sex!=null&&intentionCarStyleName!=null&&createDate!=null&&saleName!=null&&salePhone!=null,"第"+pages+"页，第"+i+"行存列表项为空的数据");
               }
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户列表项校验 ");
        }

    }

    /**
     * 销售客户查看详情
     */
    @Test
    public void preSaleCustomerPageCheck(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = yt.preSalesReceptionPage("1", "10", "", "");
            int pages=response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list= yt.preSalesReceptionPage("1", "10", "", "").getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String sex=list.getJSONObject(i).getString("sex");
                    String saleName=list.getJSONObject(i).getString("sale_name");
                    String customerPhone=list.getJSONObject(i).getString("customer_phone");
                    String intentionCarStyleName=list.getJSONObject(i).getString("intention_car_style_name");
                    String createDate=list.getJSONObject(i).getString("create_date");
                    Long customerId=list.getJSONObject(i).getLong("customer_id");
                    //进入销售客户详情



                    Preconditions.checkArgument(true,"第"+pages+"页，第"+i+"行存列表项为空的数据");
                }
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("销售客户查看详情 ");
        }

    }













}