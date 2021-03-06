package com.haisheng.framework.testng.bigScreen.jiaochen;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParam;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @description :校验单接口列表返回值各项不为空 xmf
 * @date :2020/11/27 15:30
 **/


public class EnumListCheckNotNull extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.JC_DAILY_JD;
    ScenarioUtil jc = new ScenarioUtil();
    JsonPathUtil jpu = new JsonPathUtil();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParam pp = new PublicParam();


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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";


        //replace backend gaturl
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.setShopId(pp.shopIdZ).setReferer(product.getReferer()).setRoleId(pp.roleidJdgw).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);

        jc.appletLoginToken(pp.appletTocken);

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

    @Test
    public void AJc_bannerList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletbanner();
            String jsonpath = "$.list[*].id&&$.list[*].name&&$.list[*].address&&$.list[*].tel&&$.list[*].coordinate&&$.list[*].distance";
            JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-applet首页-banner返回不为空");
        }
    }

    @Test
    public void AJc_ArticleList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletArticleList("20", null, "CAR_WELFARE");
            String jsonpath = "$.list[*].id&&$.list[*].label&&$.list[*].label_name&&$.list[*].title&&$.list[*].pic_type&&$.list[*].pic_list&&$.list[*].timestamp&&$.list[*].time_str";
            JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-applet首页-文章列表返回为空警告");
        }
    }

    //    @Test  接口不存在
    public void AJc_shopList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletShopInfo();
            String jsonpath = "$.id&&$.name&&$.address&&$.tel";
            JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-applet首页-门店信息返回不为空");
        }
    }

    @Test
    public void AJc_bandList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletBrandList();
            String jsonpath = "$.list[*].id&&$.list[*].name&&$.list[*].logo";
            JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-applet品牌列表返回不为空");
        }
    }

    @Test
    public void AJc_appletName() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletName();
            String jsonpath = "$.name";
            JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-applet名不为空");
        }
    }

    @Test
    public void AJc_appletProvinceList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletPlateNumberProvinceList();
            String jsonpath = "$.list[*].province&&$.list[*].province_name";
            JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-apple车牌号列表不为空");
        }
    }

    //    @Test  接口变更
    public void AJc_appletMaintainShop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List coor = new ArrayList();
            coor.add(116.29845);
            coor.add(39.95933);

            JSONObject data = jc.appletmaintainShopList(pp.car_id, coor, "MAINTAIN");
            String jsonpath = "$.list[*].id&&$.list[*].name&&$.list[*].address&&$.list[*].distance&&$.list[*].pic_url&&$.list[*].label";
            JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-apple保养门店列表不为空");
        }
    }

    //    @Test  接口不存在
    public void AJc_appletStaffList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletStaffList(pp.shopIdZ);
            String jsonpath = "$.list[*].uid&&$.list[*].name&&$.list[*].greetings&&$.list[*].pic_url&&$.list[*].label&&$.list[*].is_selected";
            JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("轿辰-apple保养门店服务列表为空提醒");
        }
    }

    //app
    @Test
    public void BAerCode() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            System.out.println();
            JSONObject data = jc.apperCOde();
            String jsonpath = "$.er_code_url";
            JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
//            saveData("轿辰-app个人中心，小程序码返回结果不为空");
        }
    }

    @Test(description = "核销记录")
    public void BappWrite() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            JSONObject data = jc.appWriteOffRecordsPage("ALL", "10", null);
            String jsonpath = "$.list[*].card_name&&$.list[*].card_number&&$.list[*].id&&$.list[*].user_name&&$.list[*].write_off_time&&$.total";
            JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("核销记录返回值非空校验");
        }
    }

    //    2.0
    @Test(description = "跟进列表不为空校验")
    public void BJc_appfollowUpList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            JSONObject data = jc.appFollowUpList("10", null);
            JSONArray list = data.getJSONArray("list");
            if (list.size() != 0) {
                String jsonpath = "$.list[*].id&&$.list[*].shop_id&&$.list[*].plate_number&&$.list[*].car_style&&$.list[*].customer_name&&$.list[*].customer_phone&&$.list[*].evaluate_time&&$.list[*].score&&$.list[*].suggestion&&$.list[*].labels";
                JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("跟进列表不为空校验");
        }
    }

    @Test(description = "消息列表不为空校验")
    public void BJc_appmessageList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin(pp.jdgw, pp.jdgwpassword);

            JSONObject data = jc.appmessageList("10", null);
            JSONArray list = data.getJSONArray("list");
            if (list.size() != 0) {
                String jsonpath = "$.list[*].id&&$.list[*].title&&$.list[*].time&&$.list[*].is_read&&$.list[*].type";
                JsonPathUtil.spiltString(data.toJSONString(), jsonpath);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息列表不为空校验");
        }
    }

    //消息  jsonpath 未校验
    @Test(description = "消息详情不为空校验", enabled = false)
    public void ABappmessagedetail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appLogin(pp.jdgw, pp.jdgwpassword);
            JSONObject data = jc.appmessageList("10", null);
            JSONArray list = data.getJSONArray("list");
            if (list.size() == 0) {
                return;
            }
            String id = list.getJSONObject(0).getString("id");
            JSONObject data2 = jc.appmessagedetail(id);
            String jsonpath = "$.list[*].id&&$.list[*].title&&$.list[*].time&&$.list[*].is_read&&$.list[*].type&&$.list[*].shop_id&&$.list[*].brand_name&&$.list[*].plate_number&&$.list[*].car_style_name&&$.list[*].customer_name&&$.list[*].customer_phone&&$.list[*].is_overtime&&$.list[*].car_logo_url&&$.list[*].appointment_arrival_time&&$.list[*].type_name&&$.list[*].fault_description&&$.list[*].service_sale_name";
            JsonPathUtil.spiltString(data2.toJSONString(), jsonpath);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息详情");
        }
    }


}
