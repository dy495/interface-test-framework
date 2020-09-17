package com.haisheng.framework.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.experiment.enumerator.*;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.sale.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crmOnline.CrmScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wangmin
 * @date 2020/7/27 14:17
 */
public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    private static final CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    private static final CrmScenarioUtilOnline crmOnline = CrmScenarioUtilOnline.getInstance();

    public static String getStrField(JSONObject response, String field) {
        String value = response.getString(field);
        return value == null ? "" : value;
    }

    public static String getStrField(JSONObject response, int index, String field) {
        String value = response.getJSONArray("list").getJSONObject(index).getString(field);
        return value == null ? "" : value;
    }

    public static Integer getIntField(JSONObject response, String field) {
        return response.getInteger(field);
    }

    public static Integer getIntField(JSONObject response, int index, String field) {
        return response.getJSONArray("list").getJSONObject(index).getInteger(field);
    }

    public static List<String> getMoreParam(JSONObject object, String... param) {
        List<String> list = new ArrayList<>();
        Arrays.stream(param).forEach(e -> {
            if (StringUtils.isEmpty(e)) {
                throw new RuntimeException("param类型应为String类型且不能为空");
            } else {
                if (!object.containsKey(e)) {
                    throw new RuntimeException("object中不包含此key");
                }
                list.add(object.getString(e));
            }
        });
        return list;
    }

    /**
     * 结果展示
     *
     * @param value value
     * @param <T>   T
     */
    @SafeVarargs
    public static <T> void valueView(T... value) {
        Arrays.stream(value).forEach(e -> logger.info("value:{}", e));
    }


    public static void log(String s) {
        logger.info("---------------------------------------{}---------------------------------------", s);
    }

    /**
     * 时间格式判断
     *
     * @param date   实际日期
     * @param format 比较的日期格式
     * @return boolean
     */
    public static boolean isLegalDate(String date, String format) {
        if (StringUtils.isEmpty(date) || date.length() < format.length()) {
            return false;
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            Date date1 = dateFormat.parse(date);
            return date.equals(dateFormat.format(date1));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 页面跳转
     * 当接口每页只能传入pageSize时，获取接口的访问次数来得到list.size()
     *
     * @param listSize list的尺寸
     * @param pageSize 接口要求的size
     * @return a
     */
    public static int pageTurning(double listSize, double pageSize) {
        if (listSize < 0 || pageSize < 0) {
            if (listSize < 0) {
                throw new RuntimeException("listSize不可为负数");
            }
            if (pageSize < 0) {
                throw new RuntimeException("pageSize不可为负数");
            }
        }
        double a;
        a = listSize > pageSize ? listSize % pageSize == 0 ? listSize / pageSize : Math.ceil(listSize / pageSize) + 1 : 1 + 1;
        return (int) a;
    }

    /**
     * 登录账号
     *
     * @param enumAccount 人员
     */
    public static void login(EnumAccount enumAccount) {
        if (enumAccount == null) {
            throw new RuntimeException("enumAccount is null");
        }
        if (enumAccount.getEnvironment().equals("daily")) {
            crm.login(enumAccount.getAccount(), enumAccount.getPassword());
        } else {
            crmOnline.login(enumAccount.getAccount(), enumAccount.getPassword());
        }
    }

    /**
     * 更新小程序
     *
     * @param appletCode 自己的token
     */
    public static void loginApplet(EnumAppletCode appletCode) {
        if (appletCode == null) {
            throw new RuntimeException("appletCode is null");
        }
        crm.appletLoginToken(appletCode.getCode());
    }

    /**
     * 删除垃圾客户
     *
     * @param customerName 客户姓名
     */
    public static void deleteCustomer(String customerName) {
        login(EnumAccount.ZJL_DAILY);
        JSONObject response = crm.customerList(customerName, "", "", "", "", 1, 1000);
        JSONArray list = response.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            int customerId = list.getJSONObject(i).getInteger("customer_id");
            crm.customerDelete(customerId);
        }
    }

    /**
     * 集合去重
     *
     * @param arr 集合
     * @return 一个新集合
     */
    private static List<String> removeDuplicates(List<String> arr) {
        List<String> list = new ArrayList<>();
        Iterator<String> it = arr.iterator();
        while (it.hasNext()) {
            String a = it.next();
            if (list.contains(a)) {
                it.remove();
            } else {
                list.add(a);
            }
        }
        return list;
    }

    /**
     * 车辆进店车牌号上传
     *
     * @param carNum 车牌号
     * @param status 车辆进店状态 0入店/1出店
     */
    public static void uploadShopCarPlate(String carNum, Integer status) throws Exception {
        String router = "/business/porsche/PLATE_UPLOAD/v1.0";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        String deviceId;
        switch (status) {
            case 0:
                deviceId = "7709867521115136";
                break;
            case 1:
                deviceId = "7724082825888768";
                break;
            default:
                throw new RuntimeException("状态值只能为0或1");
        }
        upload(picPath, carNum, router, deviceId);
    }

    /**
     * 上传
     *
     * @param picPath  图片地址
     * @param carNum   车牌号
     * @param router   地址
     * @param deviceId deviceId
     */
    private static void upload(String picPath, String carNum, String router, String deviceId) throws Exception {
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        JSONObject object = new JSONObject();
        object.put("plate_num", carNum);
        object.put("plate_pic", "@0");
        object.put("time", System.currentTimeMillis());
        crm.carUploadToDaily(router, deviceId, resource, JSON.toJSONString(object));
    }

    /**
     * 添加配置
     */
    public static void addConfigDaily() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        TestCaseCommon testCaseCommon = new TestCaseCommon();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_DAILY.getName());
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.QA_TEST_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP.getShopId();
        testCaseCommon.beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
    }

    /**
     * 添加线上配置
     */
    public static void addConfigOnline() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        TestCaseCommon testCaseCommon = new TestCaseCommon();
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.CRM_ONLINE.getName());
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.shopId = EnumShopId.PORSCHE_SHOP_ONLINE.getShopId();
        testCaseCommon.beforeClassInit(commonConfig);
        logger.debug("crm: " + crm);
    }
}
