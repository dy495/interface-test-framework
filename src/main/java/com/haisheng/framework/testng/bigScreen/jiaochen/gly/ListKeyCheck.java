package com.haisheng.framework.testng.bigScreen.jiaochen.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
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

public class ListKeyCheck extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.JC_DAILY_JD;
    ScenarioUtil jc = new ScenarioUtil();
    PublicParm pp = new PublicParm();
    public String appletToken = EnumAppletToken.JC_MC_DAILY.getToken();

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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "jc-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
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
     * @description:保养配置列表校验
     * @author: gly
     * @time: 2021-01-07
     */
    @Test()
    public void carModelList() {
        try {
            JSONObject respon = jc.maintainFilterManage("", "1", "10", "", "");
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.maintainFilterManage("", String.valueOf(page), "10", "", "").getJSONArray("list");
                for (int num = 0; num < list.size(); num++) {
                    if (list.getJSONObject(num).getString("status") != null && list.getJSONObject(num).getString("price") != null) {
                        JSONObject obj = list.getJSONObject(num);
                        Preconditions.checkArgument(obj.containsKey("id"), "保养配置中第 " + num + " 行的信息中的key值：" + "id" + "为空");
                        Preconditions.checkArgument(obj.containsKey("brand_name"), "保养配置中第 " + num + " 行的信息中的key值：" + "brand_name" + "为空");
                        Preconditions.checkArgument(obj.containsKey("manufacturer"), "保养配置中第 " + num + " 行的信息中的key值：manufacturer 为空");
                        Preconditions.checkArgument(obj.containsKey("style_name"), "保养配置中第 " + num + " 行的信息中的key值：style_name 为空");
                        Preconditions.checkArgument(obj.containsKey("model"), "保养配置中第 " + num + " 行的信息中的key值：model 为空");
                        Preconditions.checkArgument(obj.containsKey("year"), "保养配置中第 " + num + " 行的信息中的key值：year 为空");
                        Preconditions.checkArgument(obj.containsKey("price"), "保养配置中第 " + num + " 行的信息中的key值：price 为空");
                        Preconditions.checkArgument(obj.containsKey("status"), "保养配置中第 " + num + " 行的信息中的key值：status 为空");
                    }
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("保养配置列表key值不为空校验");
        }
    }

    /**
     * @description：会员权益列表
     * @author: gly
     * @time: 2021-01-07
     */
    @Test()
    public void carModelExportList() {
        try {
            JSONObject respon = jc.equityPage("", "1", "10");
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.equityPage("", String.valueOf(page), "10").getJSONArray("list");
                for (int num = 0; num < list.size(); num++) {
                    JSONObject obj = list.getJSONObject(num);
                    Preconditions.checkArgument(obj.containsKey("service_type_name"), "会员权益中第 " + num + " 行的信息中的key值：" + "service_type_name" + "为空");
                    Preconditions.checkArgument(obj.containsKey("service_type"), "会员权益中第 " + num + " 行的信息中的key值：" + "service_type" + "为空");
                    Preconditions.checkArgument(obj.containsKey("equity_id"), "会员权益中第 " + num + " 行的信息中的key值：equity_id 为空");
                    Preconditions.checkArgument(obj.containsKey("equity_name"), "会员权益中第 " + num + " 行的信息中的key值：equity_name 为空");
                    Preconditions.checkArgument(obj.containsKey("award_count"), "会员权益中第 " + num + " 行的信息中的key值：award_count 为空");
                    Preconditions.checkArgument(obj.containsKey("award_count_unit"), "会员权益中第 " + num + " 行的信息中的key值：award_count_unit 为空");
                    Preconditions.checkArgument(obj.containsKey("description"), "会员权益中第 " + num + " 行的信息中的key值：description 为空");
                    Preconditions.checkArgument(obj.containsKey("status_name"), "会员权益中第 " + num + " 行的信息中的key值：status_name 为空");
                    Preconditions.checkArgument(obj.containsKey("status"), "会员权益中第 " + num + " 行的信息中的key值：status 为空");
                    Preconditions.checkArgument(obj.containsKey("business_type"), "会员权益中第 " + num + " 行的信息中的key值：business_type 为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("会员权益列表key值不为空校验");
        }
    }

    /**
     * @description：洗车管理列表
     * @author: gly
     * @time: 2021-01-07
     */
    @Test()
    public void washCarManagerList() {
        try {
            JSONObject respon = jc.washCarManagerPage("", "1", "10");
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.washCarManagerPage("", String.valueOf(page), "10").getJSONArray("list");
                for (int num = 0; num < list.size(); num++) {
                    JSONObject obj = list.getJSONObject(num);
                    Preconditions.checkArgument(obj.containsKey("id"), "洗车管理中第 " + num + " 行的信息中的key值：" + "id" + "为空");
                    Preconditions.checkArgument(obj.containsKey("wash_car_date"), "洗车管理中第 " + num + " 行的信息中的key值：" + "wash_car_date" + "为空");
                    Preconditions.checkArgument(obj.containsKey("shop_name"), "洗车管理中第 " + num + " 行的信息中的key值：shop_name 为空");
                    Preconditions.checkArgument(obj.containsKey("customer_type"), "洗车管理中第 " + num + " 行的信息中的key值：customer_type 为空");
                    Preconditions.checkArgument(obj.containsKey("customer_name"), "洗车管理中第 " + num + " 行的信息中的key值：customer_name 为空");
                    Preconditions.checkArgument(obj.containsKey("phone"), "洗车管理中第 " + num + " 行的信息中的key值：phone 为空");
                    Preconditions.checkArgument(obj.containsKey("plate_number"), "洗车管理中第 " + num + " 行的信息中的key值：plate_number 为空");
                    Preconditions.checkArgument(obj.containsKey("wash_number"), "洗车管理中第 " + num + " 行的信息中的key值：wash_number 为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("洗车管理列表key值不为空校验");
        }
    }

    /**
     * @description：调整次数记录列表
     * @author: gly
     * @time: 2021-01-07
     */
    @Test()
    public void adjustNumberRecordList() {
        try {
            JSONObject respon = jc.adjustNumberRecord("", "1", "10");
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.adjustNumberRecord("", String.valueOf(page), "10").getJSONArray("list");
                for (int num = 0; num < list.size(); num++) {
                    JSONObject obj = list.getJSONObject(num);
                    Preconditions.checkArgument(obj.containsKey("id"), "调整次数记录中第 " + num + " 行的信息中的key值：" + "id" + "为空");
                    Preconditions.checkArgument(obj.containsKey("adjust_date"), "调整次数记录中第 " + num + " 行的信息中的key值：" + "adjust_date" + "为空");
                    Preconditions.checkArgument(obj.containsKey("adjust_shop_name"), "调整次数记录中第 " + num + " 行的信息中的key值：adjust_shop_name 为空");
                    Preconditions.checkArgument(obj.containsKey("customer_type_name"), "调整次数记录中第 " + num + " 行的信息中的key值：customer_type_name 为空");
                    Preconditions.checkArgument(obj.containsKey("customer_name"), "调整次数记录中第 " + num + " 行的信息中的key值：customer_name 为空");
                    Preconditions.checkArgument(obj.containsKey("customer_phone"), "调整次数记录中第 " + num + " 行的信息中的key值：customer_phone 为空");
                    Preconditions.checkArgument(obj.containsKey("adjust_number"), "调整次数记录中第 " + num + " 行的信息中的key值：adjust_number 为空");
                    Preconditions.checkArgument(obj.containsKey("after_number"), "调整次数记录中第 " + num + " 行的信息中的key值：after_number 为空");
                    Preconditions.checkArgument(obj.containsKey("remark"), "调整次数记录中第 " + num + " 行的信息中的key值：remark 为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("调整次数记录列表key值不为空校验");
        }
    }

    /**
     * @description：签到配置列表
     * @author: gly
     * @time: 2021-01-07
     */
    @Test()
    public void signInConfigList() {
        try {
            JSONObject respon = jc.signInConfigPage("", "1", "10");
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.signInConfigPage("", String.valueOf(page), "10").getJSONArray("list");
                for (int num = 0; num < list.size(); num++) {
                    JSONObject obj = list.getJSONObject(num);
                    Preconditions.checkArgument(obj.containsKey("id"), "调签到配置中第 " + num + " 行的信息中的key值：" + "id" + "为空");
                    Preconditions.checkArgument(obj.containsKey("type"), "签到配置中第 " + num + " 行的信息中的key值：" + "type" + "为空");
                    Preconditions.checkArgument(obj.containsKey("typeName"), "签到配置中第 " + num + " 行的信息中的key值：typeName 为空");
                    Preconditions.checkArgument(obj.containsKey("award_score"), "签到配置中第 " + num + " 行的信息中的key值：award_score 为空");
                    Preconditions.checkArgument(obj.containsKey("status"), "签到配置中第 " + num + " 行的信息中的key值：status 为空");
                    Preconditions.checkArgument(obj.containsKey("status_name"), "签到配置中第 " + num + " 行的信息中的key值：status_name 为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("签到配置列表key值不为空校验");
        }
    }

    /**
     * @description：分享管理列表
     * @author: gly
     * @time: 2021-01-07
     */
    @Test()
    public void shareManagerList() {
        try {
            JSONObject respon = jc.shareManagerPage("", "1", "10");
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.shareManagerPage("", String.valueOf(page), "10").getJSONArray("list");
                for (int num = 0; num < list.size(); num++) {
                    JSONObject obj = list.getJSONObject(num);
                    Preconditions.checkArgument(obj.containsKey("id"), "分享管理中第 " + num + " 行的信息中的key值：" + "id" + "为空");
                    Preconditions.checkArgument(obj.containsKey("task"), "分享管理中第 " + num + " 行的信息中的key值：" + "task" + "为空");
                    Preconditions.checkArgument(obj.containsKey("taskName"), "分享管理中第 " + num + " 行的信息中的key值：taskName 为空");
                    Preconditions.checkArgument(obj.containsKey("award_score"), "分享管理中第 " + num + " 行的信息中的key值：award_score 为空");
                    Preconditions.checkArgument(obj.containsKey("award_card_volume"), "分享管理中第 " + num + " 行的信息中的key值：award_card_volume 为空");
                    Preconditions.checkArgument(obj.containsKey("description"), "分享管理中第 " + num + " 行的信息中的key值：description 为空");
                    Preconditions.checkArgument(obj.containsKey("status_name"), "分享管理中第 " + num + " 行的信息中的key值：status_name 为空");
                    Preconditions.checkArgument(obj.containsKey("status"), "分享管理中第 " + num + " 行的信息中的key值：status 为空");
                    Preconditions.checkArgument(obj.containsKey("business_type"), "分享管理中第 " + num + " 行的信息中的key值：business_type 为空");
                    Preconditions.checkArgument(obj.containsKey("business_type_name"), "分享管理中第 " + num + " 行的信息中的key值：business_type_name 为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("分享管理列表key值不为空校验");
        }
    }

    /**
     * @description：导入记录列表
     * @author: gly
     * @time: 2021-01-07
     */
    @Test()
    public void recordImportList() {
        try {
            JSONObject respon = jc.importListFilterManage("", "1", "10", "", "");
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.importListFilterManage("", String.valueOf(page), "10", "", "").getJSONArray("list");
                for (int num = 0; num < list.size(); num++) {
                    JSONObject obj = list.getJSONObject(num);
                    Preconditions.checkArgument(obj.containsKey("id"), "导入记录中第 " + num + " 行的信息中的key值：" + "id" + "为空");
                    Preconditions.checkArgument(obj.containsKey("affiliation"), "导入记录中第 " + num + " 行的信息中的key值：" + "affiliation" + "为空");
                    Preconditions.checkArgument(obj.containsKey("type"), "导入记录中第 " + num + " 行的信息中的key值：type 为空");
                    Preconditions.checkArgument(obj.containsKey("type_name"), "导入记录中第 " + num + " 行的信息中的key值：type_name 为空");
                    Preconditions.checkArgument(obj.containsKey("import_time"), "导入记录中第 " + num + " 行的信息中的key值：import_time 为空");
                    Preconditions.checkArgument(obj.containsKey("file_type"), "导入记录中第 " + num + " 行的信息中的key值：file_type 为空");
                    Preconditions.checkArgument(obj.containsKey("import_num"), "导入记录中第 " + num + " 行的信息中的key值：import_num 为空");
                    Preconditions.checkArgument(obj.containsKey("success_num"), "导入记录中第 " + num + " 行的信息中的key值：success_num 为空");
                    Preconditions.checkArgument(obj.containsKey("failure_num"), "导入记录中第 " + num + " 行的信息中的key值：failure_num 为空");
                    Preconditions.checkArgument(obj.containsKey("operate_shop_name"), "导入记录中第 " + num + " 行的信息中的key值：operate_shop_name 为空");
                    Preconditions.checkArgument(obj.containsKey("user_name"), "导入记录中第 " + num + " 行的信息中的key值：user_name 为空");
                    Preconditions.checkArgument(obj.containsKey("user_account"), "导入记录中第 " + num + " 行的信息中的key值：user_account 为空");
                    Preconditions.checkArgument(obj.containsKey("is_can_download"), "导入记录中第 " + num + " 行的信息中的key值：is_can_download 为空");
                    Preconditions.checkArgument(obj.containsKey("file_upload_url"), "导入记录中第 " + num + " 行的信息中的key值：file_upload_url 为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导入记录列表key值不为空校验");
        }
    }

    /**
     * @description：导出记录列表
     * @author: gly
     * @time: 2021-01-07
     */
    @Test()
    public void recordExportList() {
        try {
            JSONObject respon = jc.exportListFilterManage("", "1", "10", "", "");
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.exportListFilterManage("", String.valueOf(page), "10", "", "").getJSONArray("list");
                for (int num = 0; num < list.size(); num++) {
                    JSONObject obj = list.getJSONObject(num);
                    Preconditions.checkArgument(obj.containsKey("id"), "导出记录中第 " + num + " 行的信息中的key值：" + "id" + "为空");
                    Preconditions.checkArgument(obj.containsKey("affiliation"), "导出记录中第 " + num + " 行的信息中的key值：" + "affiliation" + "为空");
                    Preconditions.checkArgument(obj.containsKey("type"), "导出记录中第 " + num + " 行的信息中的key值：type 为空");
                    Preconditions.checkArgument(obj.containsKey("type_name"), "导出记录中第 " + num + " 行的信息中的key值：type_name 为空");
                    Preconditions.checkArgument(obj.containsKey("export_time"), "导出记录中第 " + num + " 行的信息中的key值：export_time 为空");
                    Preconditions.checkArgument(obj.containsKey("total"), "导出记录中第 " + num + " 行的信息中的key值：total 为空");
                    Preconditions.checkArgument(obj.containsKey("user_name"), "导出记录中第 " + num + " 行的信息中的key值：user_name 为空");
                    Preconditions.checkArgument(obj.containsKey("success_num"), "导出记录中第 " + num + " 行的信息中的key值：success_num 为空");
                    Preconditions.checkArgument(obj.containsKey("failure_num"), "导出记录中第 " + num + " 行的信息中的key值：failure_num 为空");
                    Preconditions.checkArgument(obj.containsKey("operate_shop_name"), "导出记录中第 " + num + " 行的信息中的key值：operate_shop_name 为空");
                    Preconditions.checkArgument(obj.containsKey("user_name"), "导出记录中第 " + num + " 行的信息中的key值：user_name 为空");
                    Preconditions.checkArgument(obj.containsKey("user_account"), "导出记录中第 " + num + " 行的信息中的key值：user_account 为空");
                    Preconditions.checkArgument(obj.containsKey("status"), "导出记录中第 " + num + " 行的信息中的key值：status 为空");
                    Preconditions.checkArgument(obj.containsKey("file_upload_url"), "导出记录中第 " + num + " 行的信息中的key值：file_upload_url 为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出记录列表key值不为空校验");
        }
    }

    /**
     * @description：消息记录列表
     * @author: gly
     * @time: 2021-01-08
     */
    @Test()
    public void pushMsgList() {
        try {
            JSONObject respon = jc.pushMsgListFilterManage("", "1", "10", "", "");
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.pushMsgListFilterManage("", String.valueOf(page), "10", "", "").getJSONArray("list");
                for (int num = 0; num < list.size(); num++) {
                    JSONObject obj = list.getJSONObject(num);
                    Preconditions.checkArgument(obj.containsKey("id"), "消息记录中第 " + num + " 行的信息中的key值：" + "id" + "为空");
                    Preconditions.checkArgument(obj.containsKey("message_type"), "消息记录中第 " + num + " 行的信息中的key值：" + "message_type" + "为空");
                    Preconditions.checkArgument(obj.containsKey("message_type_name"), "消息记录中第 " + num + " 行的信息中的key值：message_type_name 为空");
                    Preconditions.checkArgument(obj.containsKey("send_time"), "消息记录中第 " + num + " 行的信息中的key值：send_time 为空");
                    Preconditions.checkArgument(obj.containsKey("content"), "消息记录中第 " + num + " 行的信息中的key值：content 为空");
                    Preconditions.checkArgument(obj.containsKey("phone"), "消息记录中第 " + num + " 行的信息中的key值：phone 为空");
                    Preconditions.checkArgument(obj.containsKey("customer_name"), "消息记录中第 " + num + " 行的信息中的key值：customer_name 为空");
                    Preconditions.checkArgument(obj.containsKey("is_read"), "消息记录中第 " + num + " 行的信息中的key值：is_read 为空");
                    Preconditions.checkArgument(obj.containsKey("customer_type_name"), "消息记录中第 " + num + " 行的信息中的key值：customer_type_name 为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("消息记录列表key值不为空校验");
        }
    }

    /**
     * @description：卡券表单列表
     * @author: gly
     * @time: 2021-01-08
     */
    @Test()
    public void voucherList() {
        try {
            JSONObject respon = jc.oucherFormVoucherPage("", "1", "10");
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.oucherFormVoucherPage("", String.valueOf(page), "10").getJSONArray("list");
                for (int num = 0; num < list.size(); num++) {
                    JSONObject obj = list.getJSONObject(num);
                    Preconditions.checkArgument(obj.containsKey("voucher_id"), "卡券表单中第 " + num + " 行的信息中的key值：" + "voucher_id" + "为空");
                    Preconditions.checkArgument(obj.containsKey("voucher_name"), "卡券表单中第 " + num + " 行的信息中的key值：" + "voucher_name" + "为空");
                    Preconditions.checkArgument(obj.containsKey("creator_name"), "卡券表单中第 " + num + " 行的信息中的key值：creator_name 为空");
                    Preconditions.checkArgument(obj.containsKey("creator_account"), "卡券表单中第 " + num + " 行的信息中的key值：creator_account 为空");
                    Preconditions.checkArgument(obj.containsKey("voucher_status"), "卡券表单中第 " + num + " 行的信息中的key值：voucher_status 为空");
                    Preconditions.checkArgument(obj.containsKey("voucher_status_name"), "卡券表单中第 " + num + " 行的信息中的key值：voucher_status_name 为空");
                    Preconditions.checkArgument(obj.containsKey("subject_name"), "卡券表单中第 " + num + " 行的信息中的key值：subject_name 为空");
                    Preconditions.checkArgument(obj.containsKey("surplus_inventory"), "卡券表单中第 " + num + " 行的信息中的key值：surplus_inventory 为空");
                    Preconditions.checkArgument(obj.containsKey("cumulative_delivery"), "卡券表单中第 " + num + " 行的信息中的key值：cumulative_delivery 为空");
                    Preconditions.checkArgument(obj.containsKey("is_check"), "卡券表单中第 " + num + " 行的信息中的key值：is_check 为空");
                    Preconditions.checkArgument(obj.containsKey("is_extension"), "卡券表单中第 " + num + " 行的信息中的key值：is_extension 为空");
                    Preconditions.checkArgument(obj.containsKey("is_edit"), "卡券表单中第 " + num + " 行的信息中的key值：is_edit 为空");
                    Preconditions.checkArgument(obj.containsKey("is_additional"), "卡券表单中第 " + num + " 行的信息中的key值：is_additional 为空");
                    Preconditions.checkArgument(obj.containsKey("is_copy"), "卡券表单中第 " + num + " 行的信息中的key值：is_copy 为空");
                    Preconditions.checkArgument(obj.containsKey("is_invalid"), "卡券表单中第 " + num + " 行的信息中的key值：is_invalid 为空");
                    Preconditions.checkArgument(obj.containsKey("is_stop"), "卡券表单中第 " + num + " 行的信息中的key值：is_stop 为空");
                    Preconditions.checkArgument(obj.containsKey("is_recall"), "卡券表单中第 " + num + " 行的信息中的key值：is_recall 为空");
                    Preconditions.checkArgument(obj.containsKey("is_delete"), "卡券表单中第 " + num + " 行的信息中的key值：is_delete 为空");
                    Preconditions.checkArgument(obj.containsKey("begin_send"), "卡券表单中第 " + num + " 行的信息中的key值：begin_send 为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("卡券表单列表key值不为空校验");
        }
    }

    /**
     * @description：增发记录列表
     * @author: gly
     * @time: 2021-01-08
     */
    @Test()
    public void additionalRecordList() {
        try {
            JSONObject respon = jc.additionalRecordPage("", "1", "10");
            int pages = respon.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.additionalRecordPage("", String.valueOf(page), "10").getJSONArray("list");
                for (int num = 0; num < list.size(); num++) {
                    JSONObject obj = list.getJSONObject(num);
                    Preconditions.checkArgument(obj.containsKey("id"), "增发记录中第 " + num + " 行的信息中的key值：" + "id" + "为空");
                    Preconditions.checkArgument(obj.containsKey("voucher_name"), "增发记录中第 " + num + " 行的信息中的key值：" + "voucher_name" + "为空");
                    Preconditions.checkArgument(obj.containsKey("time"), "增发记录中第 " + num + " 行的信息中的key值：time 为空");
                    Preconditions.checkArgument(obj.containsKey("operate_sale_name"), "增发记录中第 " + num + " 行的信息中的key值：operate_sale_name 为空");
                    Preconditions.checkArgument(obj.containsKey("operate_sale_account"), "增发记录中第 " + num + " 行的信息中的key值：operate_sale_account 为空");
                    Preconditions.checkArgument(obj.containsKey("additional_num"), "增发记录中第 " + num + " 行的信息中的key值：additional_num 为空");
                    Preconditions.checkArgument(obj.containsKey("status"), "增发记录中第 " + num + " 行的信息中的key值：status 为空");
                    Preconditions.checkArgument(obj.containsKey("status_name"), "增发记录中第 " + num + " 行的信息中的key值：status_name 为空");
                    Preconditions.checkArgument(obj.containsKey("is_recall"), "增发记录中第 " + num + " 行的信息中的key值：is_recall 为空");
                }
            }
        } catch (Exception | AssertionError e) {
            appendFailReason(e.toString());
        } finally {
            saveData("增发记录列表key值不为空校验");
        }
    }


}