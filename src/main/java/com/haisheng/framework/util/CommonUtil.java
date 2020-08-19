package com.haisheng.framework.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.haisheng.framework.model.experiment.enumerator.EnumAccount;
import com.haisheng.framework.model.experiment.enumerator.EnumAppletCode;
import com.haisheng.framework.model.experiment.excep.DataExcept;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import org.jooq.util.derby.sys.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author wangmin
 * @date 2020/7/27 14:17
 */
public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    private static final CrmScenarioUtil crm = CrmScenarioUtil.getInstance();

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
                throw new DataExcept("listSize不可为负数");
            }
            if (pageSize < 0) {
                throw new DataExcept("pageSize不可为负数");
            }
        }
        double a;
        a = listSize > pageSize ? listSize % pageSize == 0 ? listSize / pageSize : Math.floor(listSize / pageSize) + 1 : 1;
        return (int) a;
    }

    /**
     * 登录账号
     *
     * @param enumAccount 人员
     */
    public static void login(EnumAccount enumAccount) {
        if (enumAccount == null) {
            throw new DataExcept("enumAccount is null");
        }
        crm.login(enumAccount.getUsername(), enumAccount.getPassword());
    }

    /**
     * 更新小程序
     *
     * @param appletCode 自己的token
     */
    public static void loginApplet(EnumAppletCode appletCode) {
        if (appletCode == null) {
            throw new DataExcept("appletCode is null");
        }
        crm.appletLoginToken(appletCode.getCode());
    }

    /**
     * 删除垃圾客户
     *
     * @param customerName 客户姓名
     */
    public static void deleteCustomer(String customerName) {
        login(EnumAccount.XSZJ);
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
    public static void uploadShopCarPlate(String carNum, Integer status) {
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
                throw new DataExcept("状态值只能为0或1");
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
    private static void upload(String picPath, String carNum, String router, String deviceId) {
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        JSONObject object = new JSONObject();
        object.put("plate_num", carNum);
        object.put("plate_pic", "@0");
        object.put("time", System.currentTimeMillis());
        try {
            crm.carUploadToDaily(router, deviceId, resource, JSON.toJSONString(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
