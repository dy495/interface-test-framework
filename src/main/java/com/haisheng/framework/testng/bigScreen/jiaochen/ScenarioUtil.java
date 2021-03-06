package com.haisheng.framework.testng.bigScreen.jiaochen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.Variable.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.lxq.create.pcCreateExchangeGoods;
import com.haisheng.framework.testng.bigScreen.jiaochen.lxq.create.submitOrder;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.*;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.springframework.util.StringUtils;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 轿辰接口类
 */
public class ScenarioUtil extends TestCaseCommon {
    private static volatile ScenarioUtil instance = null;
    private static String IpPort = EnumTestProduct.JC_DAILY_JD.getIp();

    /**
     * 单例
     *
     * @return ScenarioUtil
     */
    public static ScenarioUtil getInstance() {
        if (instance == null) {
            synchronized (ScenarioUtil.class) {
                if (instance == null) {
                    instance = new ScenarioUtil();
                }
            }
        }
        return instance;
    }

    public void changeIpPort(String ipPort) {
        IpPort = ipPort;
    }

    //pc登录
    public void pcLogin(String phone, String verificationCode) {
        String path = "/account-platform/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        object.put("type", 1);
        httpPost(EnumTestProduct.JC_DAILY_ZH.getIp(), path, object);
    }

    //app登录
    public void appLogin(String username, String password) {
        String path = "/account-platform/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        httpPost(EnumTestProduct.JC_DAILY_ZH.getIp(), path, object);
    }

    //app登录
    public JSONObject appLogin2(String username, String password, Boolean checkCode) {
        String path = "/account-platform/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        String rs = JSONObject.toJSONString(object);
        String response = httpPost(EnumTestProduct.JC_DAILY_ZH.getIp(), path, rs, checkCode, false);
        return JSONObject.parseObject(response);
    }

    //pc登录
    public JSONObject pcTryLogin(String phone, String verificationCode, Boolean checkCode) {
        String path = "/account-platform/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        object.put("type", 1);
        String rs = JSONObject.toJSONString(object);
        String response = httpPost(EnumTestProduct.JC_DAILY_ZH.getIp(), path, rs, checkCode, false);
        return JSONObject.parseObject(response);
    }

    //app登录
    public void appLoginout() {
        String path = "/account-platform/m-app/login-user/logout";
        JSONObject object = new JSONObject();
        httpPost(EnumTestProduct.JC_DAILY_ZH.getIp(), path, object);
    }

    public void appletLoginToken(String token) {
        authorization = token;
        logger.info("applet authorization is:{}", authorization);
    }

    //pc门店列表
    public JSONObject pcShopList() {
        String path = "/car-platform/pc/login-user/shop-list";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //pc主体列表
    public JSONObject pcSubjectList() {
        String path = "/car-platform/pc/use-range/subject-list";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //pc修改密码
    public JSONObject pcModifyPassword(String oldPassword, String newPassword) {
        String path = "/account-platform/pc/modifyPassword";
        JSONObject object = new JSONObject();
        object.put("old_password", oldPassword);
        object.put("new_password", newPassword);
        return invokeApi(path, object);
    }

    //pc通过token获取用户信息
    public JSONObject pcAuthLoginUserDetail() {
        String path = "/account-platform/pc/auth/login-user/detail";
        JSONObject object = new JSONObject();
        return invokeApi(path, object, false);
    }

    //pc登出
    public JSONObject pcLogout() {
        String path = "/account-platform/pc/logout";
        JSONObject object = new JSONObject();
        String response = httpPost(EnumTestProduct.JC_DAILY_ZH.getIp(), path, JSONObject.toJSONString(object), false, false);
        return JSONObject.parseObject(response);
    }

    //pc通用枚举接口
    public JSONObject pcEnuMap() {
        String path = "/car-platform/pc/enum-map";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //pc地区树
    public JSONObject pcDistrictTree() {
        String path = "/car-platform/pc/district/tree";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //图片上传
    public JSONObject pcFileUpload(String pic, Boolean isPermanent, Double ratio) {
        String path = "/car-platform/pc/file/upload";
        JSONObject object = new JSONObject();
        object.put("pic", pic);
        object.put("is_permanent", isPermanent);
        object.put("ratio", ratio);
        return invokeApi(path, object);
    }

    //图片上传
    public JSONObject pcFileUploadNew(String pic) {
        String path = "/car-platform/pc/file/upload";
        JSONObject object = new JSONObject();
        object.put("permanent_pic_type", 7);
        object.put("pic", pic);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 列表
    public JSONObject pcReceptionManagePage(String shop_id, String page, String size) {
        String path = "/car-platform/pc/reception-manage/page";
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shop_id);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 开始接待
    public JSONObject pcStartReception(AppStartReception ar) {
        String path = "/car-platform/pc/reception-manage/start-reception";
        JSONObject object = new JSONObject();
        object.put("customer_id", ar.id);
        object.put("plate_number", ar.plate_number);
        object.put("customer_name", ar.customer_name);
        object.put("customer_phone", ar.customer_phone);
        object.put("after_sales_type", ar.after_sales_type);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 开始接待
    public JSONObject pcStartReception(String customerId, List<Long> voucherIdList, String customerName, String customerPhone) {
        String path = "/car-platform/pc/reception-manage/start-reception";
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("voucher_id_list", voucherIdList);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 开始接待
    public JSONObject pcManageReception(String plate_number, boolean checkCode) {
        String path = "/car-platform/pc/reception-manage/reception";
        JSONObject object = new JSONObject();
        object.put("plate_number", plate_number);
        return invokeApi(path, object, checkCode);
    }

    //pc接待管理 -> 完成接待
    public JSONObject pcFinishReception(Long receptionId, String shop_id) {
        String path = "/car-platform/pc/reception-manage/finish-reception";
        JSONObject object = new JSONObject();
        object.put("id", receptionId);
        object.put("shop_id", shop_id);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 取消接待
    public JSONObject pcCancelReception(Long receptionId) {
        String path = "/car-platform/pc/reception-manage/cancel-reception";
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 取消接待
    public JSONObject pcCancelReception(Long receptionId, Long shop_id) {
        String path = "/car-platform/pc/reception-manage/cancel-reception";
        JSONObject object = new JSONObject();
        object.put("id", receptionId);
        object.put("shop_id", shop_id);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 套餐列表
    public JSONObject pcPackageList() {
        String path = "/car-platform/pc/reception-manage/package-list";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //pc接待管理 -> 卡券列表
    public JSONObject pcVoucherList() {
        String path = "/car-platform/pc/reception-manage/voucher-list";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //pc客户管理 -> 客户类型
    public JSONObject pcCustomerType() {
        String path = "/car-platform/pc/customer-manage/pre-sale-customer/customer-type";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    public JSONObject pcUserRangeDetail() {
        String path = "/car-platform/pc/use-range/detail";
        JSONObject object = new JSONObject();
        object.put("subject_key", "BRAND");
        return invokeApi(path, object);
    }


    //客户管理 -> 维修记录
    public JSONObject pcAfterSaleCustomerRepairPage(Integer page, Integer size, Integer carId) {
        String url = "/car-platform/pc/customer-manage/after-sale-customer/repair-page";
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        object.put("car_id", carId);
        return invokeApi(url, object);
    }

    //客户管理 -> 小程序客户
    public JSONObject pcWechatCustomerPage(String createDate, String customerPhone, Integer activeType, Integer page, Integer size) {
        String url = "/car-platform/pc/customer-manage/wechat-customer/page";
        JSONObject object = new JSONObject();
        object.put("create_date", createDate);
        object.put("customer_phone", customerPhone);
        object.put("active_type", activeType);
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //预约管理 -> 预约看板
    public JSONObject pcTimeTableList(String appointmentMonth, String type) {
        String url = "/car-platform/pc/appointment-manage/time-table/list";
        JSONObject object = new JSONObject();
        object.put("appointment_month", appointmentMonth);
        object.put("type", type);
        return invokeApi(url, object);
    }

    //预约管理 -> 确认预约
    public JSONObject pcAppointmentRecordConfirm(Long id) {
        String url = "/car-platform/pc/appointment-manage/appointment-record/confirm";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }

    //预约管理 -> 取消预约
    public JSONObject pcAppointmentRecordCancel(Long id) {
        String url = "/car-platform/pc/appointment-manage/appointment-record/cancel";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }

    //日期可预约时间段
    public JSONObject pcMaintainTimeList(Long shopId, String day) {
        String url = "/car-platform/pc/appointment-manage/maintain/time/list";
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("day", day);
        return invokeApi(url, object);
    }

    //预约管理 -> 调整
    public JSONObject pcAppointmentRecordAdjust(Long id, Long timeId) {
        String url = "/car-platform/pc/appointment-manage/appointment-record/adjust";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("time_id", timeId);
        return invokeApi(url, object);
    }

    //卡券管理 -> 是否自助核销
    public JSONObject pcSwichSelfVerification(Long id, Boolean status) {
        String url = "/car-platform/pc/voucher-manage/swich_self_verification";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return invokeApi(url, object);
    }

    //卡券管理 -> 卡券作废
    public JSONObject pcInvalidVoucher(Long id) {
        String url = "/car-platform/pc/voucher-manage/invalid-voucher";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }

    public JSONObject pcWorkOrder(String filePath) {
        String path = "/car-platform/pc/import/work_order";
        String response = uploadFile(IpPort, path, filePath);
        return JSON.parseObject(response);
    }

    public JSONObject pcPotentialCustomer(String filePath) {
        String path = "/car-platform/pc/import/potential_customer";
        String response = uploadFile(IpPort, path, filePath);
        return JSON.parseObject(response);
    }

    //卡券管理 -> 卡券增发
    public JSONObject pcAddVoucher(Long id, Integer addNumber) {
        String url = "/car-platform/pc/voucher-manage/add-voucher";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("add_number", addNumber);
        return invokeApi(url, object);
    }

    //卡券管理 -> 卡券转移
    public JSONObject pcTransfer(String transferPhone, String receivePhone, List<Long> voucherIds) {
        String url = "/car-platform/pc/voucher-manage/transfer";
        JSONObject object = new JSONObject();
        object.put("transfer_phone", transferPhone);
        object.put("receive_phone", receivePhone);
        object.put("voucher_ids", voucherIds);
        return invokeApi(url, object);
    }

    //卡券管理 -> 手机号查询卡券列表
    public JSONObject pcVoucherList(String transferPhone) {
        String url = "/car-platform/pc/voucher-manage/voucher-list";
        JSONObject object = new JSONObject();
        object.put("transfer_phone", transferPhone);
        return invokeApi(url, object);
    }

    //卡券管理 -> 发卡记录
    public JSONObject pcSendRecord(String voucherName, String sender, Long startTime, Long endTime, Integer page, Integer size) {
        String url = "/car-platform/pc/voucher-manage/send-record";
        JSONObject object = new JSONObject();
        object.put("voucher_name", voucherName);
        object.put("sender", sender);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //卡券管理 -> 核销记录
    public JSONObject pcVerificationRecord(String voucherName, String sender, Long startTime, Long endTime, Integer page, Integer size) {
        String url = "/car-platform/pc/voucher-manage/verification-record";
        JSONObject object = new JSONObject();
        object.put("voucher_name", voucherName);
        object.put("sender", sender);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //卡券管理 -> 核销人员
    public JSONObject pcVerificationPeople(String verificationPerson, String verificationPhone, String verificationCode, Integer page, Integer size) {
        String url = "/car-platform/pc/voucher-manage/verification-people";
        JSONObject object = new JSONObject();
        object.put("verification_person", verificationPerson);
        object.put("verification_phone", verificationPhone);
        object.put("verification_code", verificationCode);
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //卡券管理 -> 创建核销员
    public JSONObject pcCreateVerificationPeople(String verificationPersonName, String verificationPersonPhone, Integer status, Integer type) {
        String url = "/car-platform/pc/voucher-manage/create-verification-people";
        JSONObject object = new JSONObject();
        object.put("verification_person_name", verificationPersonName);
        object.put("verification_person_phone", verificationPersonPhone);
        object.put("status", status);
        object.put("type", type);
        return invokeApi(url, object);
    }

    //套餐管理 -> 套餐开启状态
    public JSONObject pcSwitchPackageStatus(Boolean status, Long id) {
        String url = "/car-platform/pc/package-manage/package-form/switch-package-status";
        JSONObject object = new JSONObject();
        object.put("status", status);
        object.put("id", id);
        return invokeApi(url, object);
    }

    //套餐管理 -> 套餐详情
    public JSONObject pcPackageDetail(Long id) {
        String url = "/car-platform/pc/package-manage/package-detail";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }

    //套餐管理 -> 手机号查询客户信息
    public JSONObject pcSearchCustomer(String customerPhone) {
        String url = "/car-platform/pc/package-manage/search-customer";
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        return invokeApi(url, object);
    }

    //卡券管理 -> 套餐确认购买
    public JSONObject pcMakeSureBuy(Long id) {
        String url = "/car-platform/pc/packsge-manage/make-sure-buy";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }


    //保养配置修改
    public JSONObject pcCarModelPriceEdit(String id, String price, String status, Boolean checkcode, String type) {
        String url = "/car-platform/pc/shop-style-model/manage/model/edit";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("price", price);
        object.put("status", status);
        object.put("type", type);
        return invokeApi(url, object, checkcode);
    }

    //预约时间段
    public JSONObject timeRangeDetail(String type, String dateType) {
        String url = "/car-platform/pc/manage/appointment/time-range/detail";
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("date_type", dateType);
        return invokeApi(url, object);
    }

    //评价跟进
    public JSONObject pcEvaluateFollowUp(Long id, String remark) {
        String url = "/car-platform/pc/manage/evaluate/follow-up";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("remark", remark);
        return invokeApi(url, object);
    }

    //评价配置详情
    public JSONObject pcConfigDetail(String type) {
        String url = "/car-platform/pc/manage/evaluate/config/detail";
        JSONObject object = new JSONObject();
        object.put("type", type);
        return invokeApi(url, object);
    }

    //卡券申请审批
    public JSONObject pcApplyApproval(Long id, String status) {
        String url = "/car-platform/pc/voucher/apply/approval";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return invokeApi(url, object);
    }

    //小程序我的卡券
    public JSONObject appletVoucherList(JSONObject lastValue, String type, Integer size) {
        String url = "/car-platform/applet/granted/voucher/list";
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("type", type);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //小程序我的卡券
    public JSONObject appletVoucherListNew(JSONObject lastValue, Integer size) {
        String url = "/car-platform/applet/granted/voucher/list";
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //小程序我的套餐
    public JSONObject appletpackageList(String lastValue, String type, Integer size) {
        String url = "/car-platform/applet/granted/package/list";
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("type", type);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //小程序我的卡券
    public JSONObject appletpackageDeatil(String id) {
        String url = "/car-platform/applet/granted/package/detail";
        JSONObject object = new JSONObject();

        object.put("id", id);
        return invokeApi(url, object);
    }

    public JSONObject appletMessageList(Long lastValue, Integer size) {
        String url = "/car-platform/applet/granted/message/list";
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //app今日任务
    public JSONObject appTodayTask() {
        String url = "/car-platform/m-app/home-page/today-task";
        JSONObject object = new JSONObject();
        return invokeApi(url, object);
    }

    //app接待列表
    public JSONObject appReceptionPage(Integer lastValue, Integer size) {
        String url = "/car-platform/m-app/task/reception/page";
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("size", size);
        return invokeApi(url, object);
    }

    public JSONObject pcMakeSureBuy(Long id, String auditStatus) {
        String path = "/car-platform/pc/package-manage/make-sure-buy";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("audit_status", auditStatus);
        return invokeApi(path, object);
    }

    public JSONObject invokeApi(IScene scene) {
        return invokeApi(scene, true);
    }

    public JSONObject invokeApi(IScene scene, boolean checkCode) {
        return invokeApi(scene.getPath(), scene.getBody(), checkCode);
    }

    private JSONObject invokeApi(String path, JSONObject requestBody) {
        return invokeApi(path, requestBody, true);
    }

    /**
     * http请求方法调用
     *
     * @param path        路径
     * @param requestBody 请求体
     * @param checkCode   是否校验code
     * @return JSONObject response.data
     */
    public JSONObject invokeApi(String path, JSONObject requestBody, boolean checkCode) {
        Preconditions.checkArgument(!StringUtils.isEmpty(path), "path不可为空");
        String request = JSON.toJSONString(requestBody);
        String result = null;
        if (checkCode) {
            result = httpPostWithCheckCode(path, request, IpPort);
            return JSON.parseObject(result).getJSONObject("data");
        } else {
            try {
                result = httpPost(path, request, IpPort);
            } catch (Exception e) {
                appendFailReason(e.toString());
            }
            return JSON.parseObject(result);
        }
    }

    //---------------------十一、组织架构接口-----------------


    /**
     * @description:11.1.1 账号管理列表
     * @author: qingqing
     * @time:
     */
    public JSONObject pcStaffPage(String name, Integer page, Integer size) {
        String url = "/car-platform/pc/staff/page";
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    /**
     * @description:11.1.2 新增账号
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountAdd(String name, String phone, List role_id_list, List shop_list) {
        String url = "/car-platform/pc/staff/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n";
        json = json +
                "\"role_list\" :" + role_id_list + ",\n" +
                "\"shop_list\" :" + shop_list + ",\n" +
                "\"phone\" :\"" + phone + "\"\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    public JSONObject organizationAccountAdd(String name, String phone, List role_list, List shop_list, Boolean checkcode) {
        String url = "/account-platform/auth/staff/add";
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("phone", phone);
        json.put("role_list", role_list);
//        json.put("shop_list",shop_list);
        return invokeApi(url, json, checkcode);
    }

    public JSONObject organizationAccountAdd2(String name, String phone, List role_list, List shop_list, Boolean checkcode, String empty) {
        String url = "/car-platform/pc/staff/add";
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("phone", phone);
        json.put("role_list", role_list);
        json.put("shop_list", shop_list);
        if (empty != null) {
            json.remove(empty);
        }
        return invokeApi(url, json, checkcode);
    }


    /**
     * @description:11.1.3 账号详情
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountDetail(String account) {
        String url = "/patrol/organization/account/detail";
        String json =
                "{" +
                        "\"account\" :\"" + account + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.1.4 账号编辑
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountEdit(String id, String name, String phone, JSONArray role_id_list, JSONArray shop_list) {
        String url = "/car-platform/pc/staff/edit";
        String json =
                "{" +
                        "\"id\" :\"" + id + "\",\n" +
                        "\"name\" :\"" + name + "\",\n";

        if (phone != "") {
            json = json + "\"phone\" :\"" + phone + "\",\n";
        }
        ;
        json = json +

                "\"role_list\" :" + role_id_list + ",\n" +
                "\"shop_list\" :" + shop_list + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:11.1.5 角色删除organizationAccountDelete
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationidRoleDelete(String account) {
        String url = "/car-platform/pc/role/delete";
        String json =
                "{" +
                        "\"id\" :\"" + account + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    public JSONObject organizationAccountDelete(String account) {
        String url = "/car-platform/pc/staff/delete";
        String json =
                "{" +
                        "\"id\" :\"" + account + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:11.1.6 账号状态更改（不是启用就是禁用，点击就更改对应的状态，前端传入当前账号状态就可以，后台判断更改）
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountButtom(String account, String status) {
        String url = "/car-platform/pc/staff/status/change";
        String json =
                "{" +
                        "\"id\" :\"" + account + "\",\n" +
                        "\"status\" :\"" + status + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject organizationRoleButtom(String account, String status) {
        String url = "/car-platform/pc/role/status/change";
        String json =
                "{" +
                        "\"id\" :\"" + account + "\",\n" +
                        "\"status\" :\"" + status + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.1.7 角色列表
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleList() {
        String url = "/patrol/organization/role/list";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:11.1.8 门店列表
     * @author: qingqing
     * @time:
     */
    public JSONObject patrolShopList(String district_code) {
        String url = "/patrol/shop/list";
        String json =
                "{" +
                        "\"district_code\" :\"" + district_code + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.1.9 修改密码
     * @author: qingqing
     * @time:
     */
    public JSONObject patrolShopList(String new_password, String old_password) {
        String url = "/patrol/organization/account/change-password";
        String json =
                "{" +
                        "\"new_password\" :\"" + new_password + "\",\n" +
                        "\"old_password\" :\"" + old_password + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:11.2.2 角色管理列表
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRolePage(String role_name, Integer page, Integer size) {
        String url = "/car-platform/pc/role/page";
        JSONObject object = new JSONObject();
        object.put("name", role_name);
        object.put("page", page);
        object.put("size", size);
        String json = JSONObject.toJSONString(object);
        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject organizationRolePage(Integer page, Integer size) {
        String url = "/car-platform/pc/role/page";
        String json =
                "{" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.2.3 新增角色
     * @author: ok
     * @time:
     */
    public JSONObject organizationRoleAdd(String name, String description, JSONArray module_id, Boolean checkcode) {
        String url = "/car-platform/pc/role/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"description\" :\"" + description + "\",\n" +
                        "\"auth_list\" :" + module_id + "\n" +

                        "} ";

        return invokeApi(url, JSONObject.parseObject(json), checkcode);


    }

    public JSONObject organizationRoleAdd(String name, String description, JSONArray module_id) {
        String url = "/car-platform/pc/role/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"description\" :\"" + description + "\",\n" +
                        "\"auth_list\" :" + module_id + "\n" +

                        "} ";

        return invokeApi(url, JSONObject.parseObject(json), false);


    }


    /**
     * @description:11.2.4 角色详情
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleDetail(long role_id) {
        String url = "/patrol/organization/role/detail";
        String json =
                "{" +
                        "\"role_id\" :" + role_id + "\n" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.2.5 角色编辑
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleEdit(long role_id, String name, String description, JSONArray module_ids) {
        String url = "/car-platform/pc/role/edit";
        String json =
                "{" +
                        "\"id\" :" + role_id + ",\n" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"description\" :\"" + description + "\",\n" +
                        "\"auth_list\" :" + module_ids + "\n" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:11.2.6 角色删除
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleDelete(long role_id, Boolean checkcode) {
        String url = "/patrol/organization/role/delete";
        String json =
                "{" +
                        "\"role_id\" :" + role_id + "\n" +

                        "} ";


        return invokeApi(url, JSONObject.parseObject(json), checkcode);
    }

    public JSONObject organizationRoleDelete(long role_id) {
        String url = "/car-platform/pc/role/delete";
        String json =
                "{" +
                        "\"id\" :" + role_id + "\n" +

                        "} ";


        return invokeApi(url, JSONObject.parseObject(json));
    }

    /**
     * @description:11.2.7 角色对应的账号列表
     * @author: qingqing
     * @time:
     */
    public JSONObject AccountRolePage(long role_id, Integer page, Integer size) {
        String url = "/patrol/organization/account/role/page";
        String json =
                "{" +
                        "\"role_id\" :" + role_id + "\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @return
     * @description :app上小程序码 xmf
     * @date :2020/11/19 14:24
     */
    public JSONObject apperCOde() {
        String url = "/car-platform/m-app/personal-center/er-code";
        JSONObject json = new JSONObject();
        return invokeApi(url, json);
    }

    /**
     * @description :pc接待管理查询  ---xmf
     * @date :2020/11/24 15:13
     **/
    public JSONObject receptionManage(String shop_id, String page, String size, String parm, String result) {
        String url = "/car-platform/pc/reception-manage/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("page", page);
        json1.put("size", size);
        if (parm != null) {
            json1.put(parm, result);
        }
        return invokeApi(url, json1);
    }

    /**
     * @description :pc接待管理查询  ---xmf
     * @date :2020/11/24 15:13
     **/
    public JSONObject receptionManageC(SelectReception sr) {
        String url = "/car-platform/pc/reception-manage/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", sr.shop_id);
        json1.put("page", sr.page);
        json1.put("size", sr.size);
        json1.put("plate_number", sr.plate_number);
        json1.put("reception_sale_name", sr.reception_sale_name);
        json1.put("reception_end", sr.reception_end);
        json1.put("reception_start", sr.reception_start);
        json1.put("customer_name", sr.customer_name);
        json1.put("reception_status", sr.reception_status);
        json1.put("finish_start", sr.finish_start);
        json1.put("finish_end", sr.finish_end);
        json1.put("customer_phone", sr.customer_phone);
        json1.put("reception_type", sr.reception_type);
        json1.put("shop_id", sr.shop_id);

        return invokeApi(url, json1);
    }

    /**
     * @description :pc接待管理查询  ---xmf
     * @date :2020/11/24 15:13
     **/
    public JSONObject receptionManage(String shop_id, String page, String size, String parm, String result, String parm2, String result2) {
        String url = "/car-platform/pc/reception-manage/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("page", page);
        json1.put("size", size);
        if (parm != null || !parm.equals("")) {
            json1.put(parm, result);
        }
        if (parm2 != null || !parm2.equals("")) {
            json1.put(parm2, result2);
        }

        return invokeApi(url, json1);
    }


    /**
     * @description :今日任务xmf
     * @date :2020/11/24 15:15
     **/
    public JSONObject appTask() {
        String url = "/car-platform/m-app/home-page/today-task";
        JSONObject json = new JSONObject();

        return invokeApi(url, json);
    }

    /**
     * @description :app今日数据Xmf
     * @date :2020/11/24 15:21
     **/

    public JSONObject apptodayDate(String type, Integer last_value, Integer size) {
        String url = "/car-platform/m-app/home-page/today-data";
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("last_value", last_value);
        json.put("size", size);

        return invokeApi(url, json);
    }

    /**
     * @description :   卡券核销Xmf
     * @date :2020/11/24 15:24
     **/

    public JSONObject verification(String card_number, Boolean checkCode) {
        String url = "/car-platform/m-app/voucher/verification";
        JSONObject json = new JSONObject();
        json.put("card_number", card_number);

        return invokeApi(url, json, checkCode);
    }

    /**
     * @description :代办事项数Xmf
     * @date :2020/11/24 15:25
     **/

    public JSONObject waitTask() {
        String url = "/car-platform/m-app/home-page/waiting-task/num";
        JSONObject json = new JSONObject();

        return invokeApi(url, json);
    }

    /**
     * @description :预约任务页Xmf
     * @date :2020/11/24 15:30
     **/

    public JSONObject appointmentPage(Integer last_value, Integer size) {
        String url = "/car-platform/m-app/task/appointment/page";
        JSONObject json = new JSONObject();
        json.put("last_value", last_value);
        json.put("size", size);

        return invokeApi(url, json);
    }

    /**
     * @description :取消/确定预约Xmf
     * @date :2020/11/24 15:30
     **/

    public JSONObject appointmentHandle(Long id, Integer type, Long shop_id) {
        String url = "/car-platform/m-app/task/appointment/handle";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("type", type);
        json.put("shop_id", shop_id);

        return invokeApi(url, json);
    }

    /**
     * @description :app接待任务页Xmf
     * @date :2020/11/24 19:26
     **/

    public JSONObject appreceptionPage(Integer last_value, Integer size) {
        String url = "/car-platform/m-app/task/reception/page";
        JSONObject json = new JSONObject();
        json.put("last_value", last_value);
        json.put("size", size);

        return invokeApi(url, json);
    }

    /**
     * @description :输入车牌，确认接待Xmf
     * @date :2020/11/24 19:27
     **/

    public JSONObject appReceptionAdmit(String plate_number) {
        String url = "/car-platform/m-app/task/reception/admit";
        JSONObject json = new JSONObject();
        json.put("plate_number", plate_number);

        return invokeApi(url, json);
    }

    public JSONObject appReceptionAdmitcode(String plate_number) {
        String url = "/car-platform/m-app/task/reception/admit";
        JSONObject json = new JSONObject();
        json.put("plate_number", plate_number);

        return invokeApi(url, json, false);
    }

    /**
     * @description :app开始接待xmf
     * @date :2020/11/24 19:28
     **/

    public JSONObject StartReception(AppStartReception sr) {
        String url = "/car-platform/m-app/task/reception/start-reception";
        JSONObject json = new JSONObject();
        json.put("customer_id", sr.id);
        json.put("is_new", sr.is_new);
        json.put("customer_name", sr.customer_name);
        json.put("customer_phone", sr.customer_phone);
        json.put("plate_number", sr.plate_number);
        json.put("after_sales_type", sr.after_sales_type);

        return invokeApi(url, json);
    }

    /**
     * @description :完成接待Xmf
     * @date :2020/11/24 19:31
     **/

    public JSONObject finishReception(Long id, Long shop_id) {
        String url = "/car-platform/m-app/task/reception/finish-reception";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shop_id);

        return invokeApi(url, json);
    }

    /**
     * @description :取消接待Xmf
     * @date :2020/11/24 19:32
     **/

    public JSONObject cancleReception(Long id, Long shopId) {
        String url = "/car-platform/m-app/task/reception/cancel-reception";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * @description :小程序预约xmf
     * @date :2020/11/25 17:01
     **/
    public JSONObject appletAppointment(AppletAppointment pm) {
        String url = "/car-platform/applet/granted/appointment/submit";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", pm.shop_id);
        json1.put("staff_id", pm.staff_id);
        json1.put("time_id", pm.time_id);
        json1.put("car_id", pm.car_id);
        json1.put("appointment_name", pm.appointment_name);
        json1.put("appointment_phone", pm.appointment_phone);
        json1.put("type", pm.type);

        return invokeApi(url, json1);
    }

    /**
     * @description :门店信息 xmf
     * @date :2020/11/28 12:11
     **/
    public JSONObject appletShopInfo() {
        String url = "/car-platform/applet/shop-info";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    /**
     * @description :banner xmf
     * @date :2020/11/28 12:14
     **/

    public JSONObject appletbanner() {
        String url = "/car-platform/applet/banner";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    /**
     * @description :文章列表xmf
     * @date :2020/11/28 12:14
     **/
    public JSONObject appletArticleList(String size, String last_value, String label) {
        String url = "/car-platform/applet/article/list";
        JSONObject json1 = new JSONObject();
        json1.put("size", size);
        json1.put("last_value", last_value);
        json1.put("label", label);
        return invokeApi(url, json1);
    }

    /**
     * @description :文章详情xmf
     * @date :2020/11/28 12:14
     **/
    public JSONObject appletArticleDetail(String id) {
        String url = "/car-platform/applet/article/detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :文章发放卡券列表xmf
     * @date :2020/11/28 12:14
     **/
    public JSONObject appletArticlevoucher(String id) {
        String url = "/car-platform/applet/article/voucher/list";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :活动领卡券 xmf
     * @date :2020/11/28 12:14
     **/
    public JSONObject appletvoucherReceive(String article_id, String voucher_id) {
        String url = "/car-platform/applet/granted/article/voucher/receive";
        JSONObject json1 = new JSONObject();
        json1.put("article_id", article_id);
        json1.put("voucher_id", voucher_id);
        return invokeApi(url, json1);
    }

    /**
     * @description :活动报名xmf
     * @date :2020/11/28 12:20
     **/
    public JSONObject appletactivityRegister(AppletActivityRegister pm) {
        String url = "/car-platform/applet/granted/article/activity/register";
        JSONObject json1 = new JSONObject();
        json1.put("id", pm.id);
        json1.put("name", pm.name);
        json1.put("num", pm.num);
        json1.put("phone", pm.phone);
        json1.put("car_style", pm.car_style);
        json1.put("other_car_style", pm.other_car_style);

        return invokeApi(url, json1);
    }

    /**
     * @description :新增车辆xmf
     * @date :2020/11/28 12:29
     **/
    public JSONObject appletAddCar(String plate_number, String model_id) {
        String url = "/car-platform/applet/granted/car/create";
        JSONObject json1 = new JSONObject();
        json1.put("plate_number", plate_number);
        json1.put("model_id", model_id);

        return invokeApi(url, json1);
    }

    /**
     * @description :新增车辆xmf
     * @date :2020/11/28 12:29
     **/
    public JSONObject appletCarCreate(String plate_number, String model_id) {
        String url = "/car-platform/applet/granted/car/create";
        JSONObject json1 = new JSONObject();
        json1.put("plate_number", plate_number);
        json1.put("model_id", model_id);
        return invokeApi(url, json1, false);
    }

    /**
     * @description :我的车辆
     * @date :2020/11/28 12:44
     **/
    public JSONObject appletMyCar(String style_id) {
        String url = "/car-platform/applet/granted/car/list";
        JSONObject json1 = new JSONObject();
//        json1.put("style_id", style_id);
        return invokeApi(url, json1);
    }

    /**
     * @description :我的预约XMF
     * @date :2020/11/28 12:45
     **/

    public JSONObject appletMyAppointment(String last_value, String type, String size) {
        String url = "/car-platform/applet/granted/car/create";
        JSONObject json1 = new JSONObject();
        json1.put("last_value", last_value);
        json1.put("type", type);
        json1.put("size", size);

        return invokeApi(url, json1);
    }

    /**
     * @description :取消预约XMF
     * @date :2020/11/28 12:45
     **/

    public JSONObject appletCancleAppointment(Long id, String type) {
        String url = "/car-platform/applet/granted/appointment/cancel";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("type", type);

        return invokeApi(url, json1);
    }

    /**
     * @description :删除预约记录XMF
     * @date :2020/11/28 12:45
     **/

    public JSONObject appletmaintainDelete(String id) {
        String url = "/car-platform/applet/granted/appointment/maintain/delete";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    //888888

    /**
     * @description :我的活动报名XMF
     * @date :2020/11/28 12:45
     **/

    public JSONObject appletMyActually(String last_value, String size) {
        String url = "/car-platform/applet/granted/appointment/activity/list";
        JSONObject json1 = new JSONObject();
        json1.put("last_value", last_value);
        json1.put("size", size);

        return invokeApi(url, json1);
    }

    /**
     * @description :取消预约XMF
     * @date :2020/11/28 12:45
     **/

    public JSONObject appletactivityCancel(String id) {
        String url = "/car-platform/applet/granted/appointment/activity/cancel";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :删除预约记录XMF
     * @date :2020/11/28 12:45
     **/

    public JSONObject appletactivityDelete(String id) {
        String url = "/car-platform/applet/granted/appointment/activity/delete";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :预约评价项 XMF
     * @date :2020/11/28 12:45
     **/

    public JSONObject appletevaluateItems(String id) {
        String url = "/car-platform/applet/granted/appointment/evaluate/items";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :我的消息XMF
     * @date :2020/11/28 12:45
     **/
    public JSONObject appletmessageList(String last_value, String size) {
        String url = "/car-platform/applet/granted/message/list";
        JSONObject json1 = new JSONObject();
        json1.put("last_value", last_value);
        json1.put("size", size);

        return invokeApi(url, json1);
    }

    /**
     * @description :我的消息详情 XMF
     * @date :2020/11/28 12:45
     **/
    public JSONObject appletMessageDetail(String id) {
        String url = "/car-platform/applet/granted/message/detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :编辑车辆xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletCarEdit(String id, String plate_number, String model_id) {
        String url = "/car-platform/applet/granted/car/edit";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("plate_number", plate_number);
        json1.put("model_id", model_id);

        return invokeApi(url, json1, false);
    }

    /**
     * @description :删除车辆xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletCarDelete(String id) {
        String url = "/car-platform/applet/granted/car/delete";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :品牌列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletBrandList() {
        String url = "/car-platform/applet/brand/list";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    /**
     * @description :车系列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletCarStyleList(Long brand_id) {
        String url = "/car-platform/applet/style/list";
        JSONObject json1 = new JSONObject();
        json1.put("brand_id", brand_id);
        return invokeApi(url, json1);
    }

    /**
     * @description :车型列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletCarModelList(Long brand_id, Long style_id) {
        String url = "/car-platform/applet/model/list";
        JSONObject json1 = new JSONObject();
        json1.put("brand_id", brand_id);
        json1.put("style_id", style_id);
        return invokeApi(url, json1);
    }
    //00

    /**
     * @description :车型列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletName() {
        String url = "/car-platform/applet/name";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    /**
     * @description :车型列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletPlateNumberProvinceList() {
        String url = "/car-platform/applet/plate-number-province-list";
        JSONObject obj = new JSONObject();
        return invokeApi(url, obj);
    }

    /**
     * @description :保养门店列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletmaintainShopList(String car_id, List coordinate, String type) {
        String url = "/car-platform/applet/granted/appointment/shop/list";
        JSONObject json1 = new JSONObject();
        json1.put("car_id", car_id);
        json1.put("coordinate", coordinate);
        json1.put("type", type);

        return invokeApi(url, json1);
    }

    /**
     * @description :服务顾问列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletStaffList(String shop_id) {
        String url = "/car-platform/applet/granted/maintain/staff/list";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);

        return invokeApi(url, json1);
    }

    /**
     * @description :可预约时段列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletmaintainTimeList(Long shop_id, Long car_id, String day, String type) {
        String url = "/car-platform/applet/granted/appointment/time/list";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("car_id", car_id);
        json1.put("day", day);
        json1.put("type", type);

        return invokeApi(url, json1);
    }

    public JSONObject appletmaintainTimeList(Long shop_id, Long car_id, String day, String type, Boolean checkcode) {
        String url = "/car-platform/applet/granted/appointment/time/list";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("car_id", car_id);
        json1.put("day", day);
        json1.put("type", type);

        return invokeApi(url, json1, checkcode);
    }


    /**
     * @description :可预约时段列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject pcRoleList() {
        String url = "/car-platform/pc/role/list";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    //将账户使用次数为0的角色删除
    public void deleteRole() {
        JSONArray role_list = organizationRolePage("", 1, 100).getJSONArray("list");
        for (int i = 0; i < role_list.size(); i++) {
            int account_number = role_list.getJSONObject(i).getInteger("account_number");
            if (account_number == 0) {
                Long role_id = role_list.getJSONObject(i).getLong("role_id");
                organizationRoleDelete(role_id);
            }

        }
    }

    //角色权限列表 xmf
    @DataProvider(name = "LIMITID")
    public static Object[][] limitid() {
        return new Integer[][]{
                {118, 119, 120},
                {136, 118},
        };
    }

    @DataProvider(name = "SELECT_PARAM")  //xmf
    public static Object[] select_param() {
        return new String[][]{
                {"vehicle_chassis_code", "vehicle_chassis_code"},

        };
    }

    @DataProvider(name = "PLATE")  //xmf
    public static Object[] plate() {   //异常车牌号集合
        return new String[]{
                "苏BJ123",   //6位
//                "BJ12345",    //不含汉字
                "京1234567",  //不含英文
//                "京bj12345", //含小写
                "京B@12345", //含字母
                "苏BJ123456",//9位
        };
    }

    /**
     * @description:品牌管理-品牌列表分页
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject brandPage(int page, int size, String name, String first_letter) {
        String url = "/car-platform/pc/brand/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (name != "") {
            json.put("name", name);
        }
        if (first_letter != "") {
            json.put("first_letter", first_letter);
        }
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    /**
     * @description:品牌管理-新建品牌
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject addBrand(String name, String logo) {
        String url = "/car-platform/pc/brand/add";
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("logo_path", logo);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject addBrandNotChk(String name, String logo) throws Exception {
        String url = "/car-platform/pc/brand/add";
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("logo_path", logo);
        String result = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result);
    }


    /**
     * @description:品牌管理-编辑品牌
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject editBrand(Long id, String name, String logo) {
        String url = "/car-platform/pc/brand/edit";
        JSONObject json = new JSONObject();
        json.put("id", id);
        if (name != "") {
            json.put("name", name);
        }
        if (logo != "") {
            json.put("logo_path", logo);
        }
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:品牌管理-删除品牌
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject delBrand(Long id) {
        String url = "/car-platform/pc/brand/delete";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    /**
     * @description:品牌管理-品牌车系列表分页
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject carStylePage(int page, int size, Long brand_id, String name) {
        String url = "/car-platform/pc/brand/car-style/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("brand_id", brand_id);
        if (name != "") {
            json.put("name", name);
        }
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:品牌管理-新建品牌车系
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject addCarStyle(Long brand_id, String manufacturer, String name, String online_time) {
        String url = "/car-platform/pc/brand/car-style/add";
        JSONObject json = new JSONObject();
        json.put("brand_id", brand_id);
        json.put("manufacturer", manufacturer); //生产商
        json.put("name", name); //车系名称
        json.put("online_time", online_time); // 上线时间
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject addCarStyleNotChk(Long brand_id, String manufacturer, String name, String online_time) throws Exception {
        String url = "/car-platform/pc/brand/car-style/add";
        JSONObject json = new JSONObject();
        json.put("brand_id", brand_id);
        json.put("manufacturer", manufacturer); //生产商
        json.put("name", name); //车系名称
        json.put("online_time", online_time); // 上线时间
        String result = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result);
    }


    /**
     * @description:品牌管理-编辑品牌车系
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject editCarStyle(Long id, Long brand_id, String manufacturer, String name, String online_time) {
        String url = "/car-platform/pc/brand/car-style/edit";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("brand_id", brand_id);
        json.put("manufacturer", manufacturer); //生产商
        json.put("name", name); //车系名称
        json.put("online_time", online_time); // 上线时间
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:品牌管理-删除品牌车系
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject delCarStyle(Long id) {
        String url = "/car-platform/pc/brand/car-style/delete";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    /**
     * @description:品牌管理-品牌车系车型列表分页
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject carModelPage(int page, int size, Long brand_id, Long style_id, String name, String year, String status) { //预约状态ENABLE（开启） DISABLE（关闭）
        String url = "/car-platform/pc/brand/car-style/car-model/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("brand_id", brand_id);
        json.put("style_id", style_id);
        if (name != "") {
            json.put("name", name);
        }
        if (year != "") {
            json.put("year", year);
        }
        if (status != "") {
            json.put("status", status);
        }
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:品牌管理-新建品牌车系车型
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject addCarModel(Long brand_id, Long style_id, String name, String year, String status) { //预约状态ENABLE（开启） DISABLE（关闭）
        String url = "/car-platform/pc/brand/car-style/car-model/add";
        JSONObject json = new JSONObject();
        json.put("brand_id", brand_id);
        json.put("style_id", style_id);
        json.put("name", name);
        json.put("year", year);
        json.put("status", status);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject addCarModelNotChk(Long brand_id, Long style_id, String name, String year, String status) throws Exception { //预约状态ENABLE（开启） DISABLE（关闭）
        String url = "/car-platform/pc/brand/car-style/car-model/add";
        JSONObject json = new JSONObject();
        json.put("brand_id", brand_id);
        json.put("style_id", style_id);
        json.put("name", name);
        json.put("year", year);
        json.put("status", status);
        String result = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result);
    }


    /**
     * @description:品牌管理-编辑品牌车系车型
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject editCarModel(Long id, Long brand_id, Long style_id, String name, String year, String status) { //预约状态ENABLE（开启） DISABLE（关闭）
        String url = "/car-platform/pc/brand/car-style/car-model/edit";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("brand_id", brand_id);
        json.put("style_id", style_id);
        json.put("name", name);
        json.put("year", year);
        json.put("status", status);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:品牌管理-删除品牌车系车型
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject delCarModel(Long id) {
        String url = "/car-platform/pc/brand/car-style/car-model/delete";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:接待管理列表
     * @author: gly
     * @time: 2020-12/16
     */
    public JSONObject receptionTimeManage(String shop_id, String page, String size, String finish_start, String finish_end, String reception_start, String reception_end) {
        String url = "/car-platform/pc/reception-manage/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("finish_end", finish_end);
        json1.put("finish_start", finish_start);
        json1.put("reception_start", reception_start);
        json1.put("reception_end", reception_end);
        return invokeApi(url, json1);
    }

    /**
     * @description:销售管理列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject preSleCustomerManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:销售管理列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject preSleCustomerTimeManage(String shopId, String page, String size, String start_time, String end_time) {
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }

    /**
     * @description:销售管理列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject preSleCustomerManage(PreSleCustomerVariable variable) {
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/page";
        JSONObject json = new JSONObject();
        json.put("shopId", variable.shop_id);
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("customer_name", variable.customer_name);
        json.put("customer_phone", variable.customer_phone);
        json.put("create_date", variable.create_date);
        json.put("sale_name", variable.sale_name);

        return invokeApi(url, json);
    }


    /**
     * @description:售后管理列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject afterSleCustomerManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/customer-manage/after-sale-customer/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:售后管理列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject afterSleCustomerManage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/customer-manage/after-sale-customer/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }


    /**
     * @description:售后管理列表-导入时间筛选
     * @author: gly
     * @time: 2020-12/16
     */
    public JSONObject afterSleCustomerCreateTimeManage(String shop_id, String page, String size, String create_start_time, String create_end_time) {
        String url = "/car-platform/pc/customer-manage/after-sale-customer/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("create_start_time", create_start_time);
        json1.put("create_end_time", create_end_time);

        return invokeApi(url, json1);
    }

    /**
     * @description:售后管理列表-开单时间筛选
     * @author: gly
     * @time: 2020-12/16
     */
    public JSONObject afterSleCustomerOrderTimeManage(String shop_id, String page, String size, String order_start_time, String order_end_time) {
        String url = "/car-platform/pc/customer-manage/after-sale-customer/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("order_start_time", order_start_time);
        json1.put("order_end_time", order_end_time);
        return invokeApi(url, json1);
    }

    /**
     * @description:售后管理列表-购车时间筛选
     * @author: gly
     * @time: 2020-12/16
     */
    public JSONObject afterSleCustomerBuyTimeManage(String shop_id, String page, String size, String buyCarTimeStart, String buyCarTimeEnd) {
        String url = "/car-platform/pc/customer-manage/after-sale-customer/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("buy_car_time_end", buyCarTimeEnd);
        json1.put("buy_car_time_start", buyCarTimeStart);
        return invokeApi(url, json1);
    }

    /**
     * @description:售后管理列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject afterSleCustomerManage(String page, String size) {
        String url = "/car-platform/pc/customer-manage/after-sale-customer/page";
        JSONObject json = new JSONObject();
//        json.put("shopId",shopId);
        json.put("page", page);
        json.put("size", size);
        return invokeApi(url, json);
    }


    /**
     * @description:售后管理列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject afterSleCustomerManage(AfterSleCustomerVariable variable) {
        String url = "/car-platform/pc/customer-manage/after-sale-customer/page";
        JSONObject json = new JSONObject();
        json.put("shopId", variable.shop_id);
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("vehicle_chassis_code", variable.vehicle_chassis_code);
        json.put("customer_name", variable.customer_name);
        json.put("customer_phone", variable.customer_phone);
        json.put("create_end_time", variable.create_end_time);
        json.put("create_start_time", variable.create_start_time);
        json.put("order_end_time", variable.order_end_time);
        json.put("order_start_time", variable.order_start_time);

        return invokeApi(url, json);
    }

    /**
     * @description:小程序客户管理列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject weChatSleCustomerManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/customer-manage/wechat-customer/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:小程序客户管理列表
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject weChatSleCustomerTimeManage(String shopId, String page, String size, String start_time, String end_time) {
        String url = "/car-platform/pc/customer-manage/wechat-customer/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }

    /**
     * @description:V2.0小程序客户管理列表
     * @time: 2020-11-25
     */
    public JSONObject weChatSleCustomerManage(weChatSleCustomerVariable variable) {
        String url = "/car-platform/pc/customer-manage/wechat-customer/page";
        JSONObject json = new JSONObject();
        json.put("shopId", variable.shop_id);
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("create_date", variable.end_time);
        json.put("active_type", variable.start_time);
        json.put("customer_phone", variable.customer_phone);
        json.put("vip_type", variable.vip_type);

        return invokeApi(url, json);
    }

    /**
     * @description:预约记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject appointmentRecordManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/appointment-manage/appointment-record/appointment-page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:预约记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject appointmentRecordManage1(String page, String size, String type, String pram, String result) {
        String url = "/car-platform/pc/appointment-manage/appointment-record/appointment-page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("type", type);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:预约记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject appointmentRecordManage(String shopId, String page, String size, String type, String pram, String result) {
        String url = "/car-platform/pc/appointment-manage/appointment-record/appointment-page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("type", type);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:预约记录列表
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject appointmentRecordTimeManage(String shopId, String page, String size, String appointment_start, String appointment_end, String confirm_start, String confirm_end, String create_start, String create_end) {
        String url = "/car-platform/pc/appointment-manage/appointment-record/appointment-page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
//        json.put("appointment_end", appointment_end);
//        json.put("appointment_start", appointment_start);
        json.put("confirm_end", confirm_end);
        json.put("confirm_start", confirm_start);
        json.put("create_end", create_end);
        json.put("create_start", create_start);
        return invokeApi(url, json);
    }

    /**
     * @description:预约记录列表
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject appointmentRecordTimeManage(String shopId, String page, String size, String type, String appointment_start, String appointment_end, String confirm_start, String confirm_end, String create_start, String create_end) {
        String url = "/car-platform/pc/appointment-manage/appointment-record/appointment-page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("type", type);
//        json.put("appointment_end", appointment_end);
//        json.put("appointment_start", appointment_start);
        json.put("confirm_end", confirm_end);
        json.put("confirm_start", confirm_start);
        json.put("create_end", create_end);
        json.put("create_start", create_start);
        return invokeApi(url, json);
    }

    /**
     * @description:预约记录列表
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject appointmentDreiverRecordTimeManage(String shopId, String page, String size, String type, String cancelStart, String cancelEnd, String receptionStart, String receptionEnd, String createStart, String createEnd) {
        String url = "/car-platform/pc/appointment-manage/appointment-record/appointment-page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("type", type);
        json.put("cancel_end", cancelEnd);
        json.put("cancel_start", cancelStart);
        json.put("reception_end", receptionEnd);
        json.put("reception_start", receptionStart);
        json.put("create_end", createEnd);
        json.put("create_start", createStart);
        return invokeApi(url, json);
    }

    /**
     * @description:预约记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject appointmentRecordManage(appointmentRecordVariable variable) {
        String url = "/car-platform/pc/appointment-manage/appointment-record/appointment-page";
        JSONObject json = new JSONObject();
        json.put("shopId", variable.shop_id);
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("plate_number", variable.plate_number);
        json.put("service_sale_id", variable.service_sale_id);
        json.put("shop_id", variable.shop_id);
        json.put("customer_name", variable.customer_name);
        json.put("confirm_status", variable.confirm_status);
        json.put("customer_phone", variable.customer_phone);
        json.put("is_overtime", variable.is_overtime);
        json.put("type", variable.type);
        json.put("appointment_date", variable.appointment_date);

        return invokeApi(url, json);
    }

    /**
     * @description:保养配置
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject maintainFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/shop-style-model/manage/model/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:保养配置
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject maintainFilterManage(String shopId, String page, String size, String type, String pram, String result) {
        String url = "/car-platform/pc/shop-style-model/manage/model/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("type", type);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:保养配置
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject maintainFilterManage(maintainVariable variable) {
        String url = "/car-platform/pc/shop-style-model/manage/model/page";
        JSONObject json = new JSONObject();
        json.put("shopId", variable.shop_id);
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("brand_name", variable.brand_name);
        json.put("manufacturer", variable.manufacturer);
        json.put("car_model", variable.car_model);
        json.put("year", variable.year);
        json.put("type", variable.type);

        return invokeApi(url, json);
    }

    /**
     * @description:卡券管理
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject voucherFormFilterManage(voucherFormVariable variable) {
        String url = "/car-platform/pc/voucher-manage/voucher-form/voucher-page";
        JSONObject json = new JSONObject();
        json.put("shopId", variable.shop_id);
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("subject_name", variable.subject_name);
        json.put("voucher_status", variable.voucher_status);
        json.put("voucher_name", variable.voucher_name);
        json.put("voucher_type", variable.voucher_type);
        json.put("creator_name", variable.creator_name);
        json.put("creator_account", variable.creator_account);

        return invokeApi(url, json);
    }

    /**
     * @description:卡券管理
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject voucherFormFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/voucher-manage/voucher-form/voucher-page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:优惠券管理
     * @author: gly
     * @time: 2021-3-3
     */
    public JSONObject voucherPageFilterManage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/voucher-manage/voucher-form/voucher-page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }


    /**
     * @description:发卡记录
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject sendRecordFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/voucher-manage/send-record";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:发卡记录列表
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject sendRecordFilterTimeManage(String shopId, String page, String size, String start_time, String end_time) {
        String url = "/car-platform/pc/voucher-manage/send-record";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }

    /**
     * @description:核销记录
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject verificationReordFilterManage(String shopId, String id, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/voucher-manage/verification-record";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("voucher_id", id);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:V 3.0核销记录
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject verificationReordFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/voucher-manage/verification-record";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:核销记录列表
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject verificationReordTimeFilterManage(String shopId, String id, String page, String size, String start_time, String end_time) {
        String url = "/car-platform/pc/voucher-manage/verification-record";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("voucher_id", id);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }

    /**
     * @description:核销记录
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject verificationReordFilterManage(verificationRecordVariable variable) {
        String url = "/car-platform/pc/voucher-manage/verification-record";
        JSONObject json = new JSONObject();
        json.put("customer_name", variable.customer_name);
        json.put("customer_phone", variable.customer_phone);
        json.put("verify_channel", variable.verification_channel_name);
        json.put("verify_code", variable.verification_code);
        json.put("verify_sale_phone", variable.verification_account);
        json.put("voucher_name", variable.voucher_name);

        json.put("page", variable.page);
        json.put("size", variable.size);

        return invokeApi(url, json);
    }

    /**
     * @description:核销人员记录
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject verificationPeopleFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/voucher-manage/verification-people";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:核销人员记录
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject verificationPeopleFilterManage(verificationPeopleVariable variable) {
        String url = "/car-platform/pc/voucher-manage/verification-people";
        JSONObject json = new JSONObject();
        json.put("verification_person", variable.verification_person);
        json.put("verification_phone", variable.verification_phone);
        json.put("verification_code", variable.verification_code);
        json.put("page", variable.page);
        json.put("size", variable.size);
        return invokeApi(url, json);
    }

    /**
     * @description:套餐表单
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject packageFormFilterManage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/package-manage/package-form/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * /**
     *
     * @description:套餐表单列表
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject packageFormTimeFilterManage(String shopId, String page, String size, String start_time, String end_time) {
        String url = "/car-platform/pc/package-manage/package-form/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }

    /**
     * @description:套餐表单
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject packageFormFilterManage(packageFormVariable variable) {
        String url = "/car-platform/pc/package-manage/package-form/page";
        JSONObject json = new JSONObject();
        json.put("package_status", variable.package_status);
        json.put("package_name", variable.package_name);
        json.put("creator", variable.creator);
        json.put("start_time", variable.start_time);
        json.put("end_time", variable.end_time);
        json.put("shop_name", variable.shop_name);
        json.put("page", variable.page);
        json.put("size", variable.size);
        return invokeApi(url, json);
    }


    /**
     * @description:套餐购买记录
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject buyPackageRecordFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/package-manage/buy-package-record";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:套餐表单列表
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject buyPackageRecordFilterTimeManage(String shopId, String page, String size, String start_time, String end_time) {
        String url = "/car-platform/pc/package-manage/buy-package-record";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }

    /**
     * @description:套餐购买记录
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject buyPackageRecordFilterManage(buyPackageVariable variable) {
        String url = "/car-platform/pc/package-manage/buy-package-record";
        JSONObject json = new JSONObject();
        json.put("package_name", variable.package_name);
        json.put("sender", variable.sender);
//        json.put("start_time", variable.start_time);
//        json.put("end_time", variable.end_time);
        json.put("send_type", variable.send_type);
        json.put("page", variable.page);
        json.put("size", variable.size);
        return invokeApi(url, json);
    }

    /**
     * @description:消息表单
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject messageFormFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/message-manage/message-form/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:消息表单
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject messageFormTimeFilterManage(String shopId, String page, String size, String start_time, String end_time) {
        String url = "/car-platform/pc/message-manage/message-form/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }

    /**
     * @description:消息表单
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject messageFormFilterManage(messageFormVariable variable) {
        String url = "/car-platform/pc/message-manage/message-form/page";
        JSONObject json = new JSONObject();
        json.put("message_type", variable.message_type);
        json.put("send_account", variable.send_account);
        json.put("start_time", variable.start_time);
        json.put("end_time", variable.end_time);
        json.put("customer_name", variable.customer_name);
        json.put("shop_id", variable.shop_id);
        json.put("page", variable.page);
        json.put("size", variable.size);
        return invokeApi(url, json);
    }

    /**
     * @description:文章表单
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject articleFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/operation/article/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:文章表单
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject articleTimeFilterManage(String shopId, String page, String size, String create_start, String create_end, String modify_start, String modify_end) {
        String url = "/car-platform/pc/operation/article/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("create_start", create_start);
        json.put("create_end", create_end);
        json.put("modify_start", modify_start);
        json.put("modify_end", modify_end);
        return invokeApi(url, json);
    }

    /**
     * @description:文章表单
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject articleFilterManage(articleVariable variable) {
        String url = "/car-platform/pc/operation/article/page";
        JSONObject json = new JSONObject();
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("title", variable.title);
        json.put("start_date", variable.start_date);
        json.put("end_date", variable.end_date);
        json.put("register_start_date", variable.register_start_date);
        json.put("register_end_date", variable.register_end_date);
        return invokeApi(url, json);
    }

    /**
     * @description:报名列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject registerListFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/operation/register/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:文章表单
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject registerListTimeFilterManage(String shopId, String page, String size, String start_time, String end_time, String register_start_date, String register_end_date) {
        String url = "/car-platform/pc/operation/register/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        json.put("register_start_date", register_start_date);
        json.put("register_end_date", register_end_date);
        return invokeApi(url, json);
    }

    /**
     * @description:报名列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject registerListFilterManage(registerListVariable variable) {
        String url = "/car-platform/pc/operation/register/page";
        JSONObject json = new JSONObject();
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("title", variable.title);
        json.put("start_date", variable.start_date);
        json.put("end_date", variable.end_date);
        json.put("register_start_date", variable.register_start_date);
        json.put("register_end_date", variable.register_end_date);
        return invokeApi(url, json);
    }

    /**
     * @description:活动审批
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject approvalListFilterManage(String shopId, String page, String size, int id, String pram, String result) {
        String url = "/car-platform/pc/operation/approval/page";
        JSONObject json = new JSONObject();
//        json.put("shopId", shopId);
        json.put("page", page);
        json.put("article_id", id);
        json.put("size", size);
        if ((pram != null)) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:活动审批
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject approvalListFilterManage(approvalListVariable variable) {
        String url = "/car-platform/pc/operation/approval/page";
        JSONObject json = new JSONObject();
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("article_id", variable.article_id);
        json.put("customer_name", variable.customer_name);
        json.put("phone", variable.phone);
        json.put("status", variable.status);
        json.put("article_id", variable.article_id);
        return invokeApi(url, json);
    }


    /**
     * @description:优惠券审批
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject applyListFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/voucher/apply/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:卡券申请
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject applyListTimeFilterManage(String shopId, String page, String size, String start_time, String end_time) {
        String url = "/car-platform/pc/voucher/apply/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }

    /**
     * @description:卡券申请
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject applyListFilterManage(applyListVariable variable) {
        String url = "/car-platform/pc/voucher/apply/page";
        JSONObject json = new JSONObject();
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("name", variable.name);
        json.put("send_time", variable.send_time);
        json.put("start_time", variable.start_time);
        json.put("status", variable.status);
        json.put("apply_group", variable.apply_group);
        json.put("end_time", variable.end_time);
        json.put("apply_item", variable.send_time);
        return invokeApi(url, json);
    }

    /**
     * @description:门店列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject shopListFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/shop/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:门店列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject shopListFilterManage(String shopId, String page, String size, String name) {
        String url = "/car-platform/pc/shop/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);

        return invokeApi(url, json);
    }

    /**
     * @description:门店列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject shopListFilterManage(String page, String size) {
        String url = "/car-platform/pc/shop/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);

        return invokeApi(url, json);
    }

    /**
     * @description:品牌列表
     * @author: gly
     * @time: 2020-11-24
     */

    public JSONObject brandListFilterManage3(String shopId, String page, String size, String name) {
        String url = "/car-platform/pc/brand/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);

        return invokeApi(url, json);
    }

    /**
     * @description: 品牌列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject brandListFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/brand/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", Integer.valueOf(page));
        json.put("size", Integer.valueOf(size));
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:品牌列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject brandListFilterManage2(String shopId, String page, String size, String name, String firstLetter) {
        String url = "/car-platform/pc/brand/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);
        json.put("first_letter", firstLetter);
        return invokeApi(url, json);
    }

    /**
     * @description:车系列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject carStyleListFilterManage(String shopId, String page, String size, String brand_id, String pram, String result) {
        String url = "/car-platform/pc/brand/car-style/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("brand_id", brand_id);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:车系列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject carStyleListFilterManage(String shopId, String page, String size, String name, String brand_id) {
        String url = "/car-platform/pc/brand/car-style/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);
        json.put("brand_id", brand_id);
        return invokeApi(url, json);
    }

    /**
     * @description:车型列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject carModelListFilterManage(String shopId, String page, String size, String brand_id, String style_id, String pram, String result) {
        String url = "/car-platform/pc/brand/car-style/car-model/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("brand_id", brand_id);
        json.put("style_id", style_id);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:车型列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject carModelListFilterManage1(String shopId, String page, String size, String name, String year, String brand_id, String style_id) {
        String url = "/car-platform/pc/brand/car-style/car-model/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);
        json.put("year", year);
        json.put("brand_id", brand_id);
        json.put("style_id", style_id);
        return invokeApi(url, json);
    }

    /**
     * @description:角色列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject roleListFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/role/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:角色列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject roleListFilterManage(String shopId, String page, String size, String name) {
        String url = "/car-platform/pc/role/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);
        return invokeApi(url, json);
    }

    /**
     * @description:员工列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject staffListFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/staff/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:员工列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject staffListFilterManage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/staff/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:导入记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject importListFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/record/import/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:导入记录
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject importListTimeFilterManage(String shopId, String page, String size, String start_time, String end_time) {
        String url = "/car-platform/pc/record/import/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }

    /**
     * @description:导入记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject importListFilterManage(String shopId, String page, String size, String type, String user, String import_date) {
        String url = "/car-platform/pc/record/import/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("type", type);
        json.put("user", user);
        json.put("import_date", import_date);

        return invokeApi(url, json);
    }

    /**
     * @description:导出记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject exportListFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/record/export/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:导出记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject exportListFilterManage1(String shopId, String page, String size, String endTime, String startTime) {
        String url = "/car-platform/pc/record/export/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("end_time", endTime);
        json.put("start_time", startTime);

        return invokeApi(url, json);
    }

    /**
     * @description:导出记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject exportListFilterManage(String shopId, String page, String size, String type, String user, String export_time) {
        String url = "/car-platform/pc/record/export/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("type", type);
        json.put("user", user);
        json.put("export_time", export_time);

        return invokeApi(url, json);
    }

    /**
     * @description:消息记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject pushMsgListFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/record/push-msg/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:消息记录
     * @author: gly
     * @time: 2020-12-16
     */
    public JSONObject pushMsgListTimeFilterManage(String shopId, String page, String size, String start_time, String end_time) {
        String url = "/car-platform/pc/record/push-msg/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }

    /**
     * @description:消息记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject pushMsgListFilterManage1(String shopId, String page, String size, String customer_type, String message_type, String push_date) {
        String url = "/car-platform/pc/record/push-msg/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("customer_type", customer_type);
        json.put("message_type", message_type);
        json.put("push_date", push_date);

        return invokeApi(url, json);
    }


    /**
     * @description:门店管理-门店列表
     * @author: lxq
     * @time: 2020-11-25
     */

    public JSONObject shopPage(int page, int size, String name) {
        String url = "/car-platform/pc/shop/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:门店管理-新建门店
     * @author: lxq
     * @time: 2020-11-25
     */

    public JSONObject addShop(String avatar_path, String simple_name, String name, JSONArray brand_list, String district_code, String address,
                              String sale_tel, String service_tel, double longitude, double latitude,
                              String appointment_status, String washing_status) { // 预约状态：ENABLE（开启） DISABLE（关闭）
        String url = "/car-platform/pc/shop/add";
        JSONObject json = new JSONObject();
        json.put("avatar_path", avatar_path);
        json.put("simple_name", simple_name);
        json.put("name", name);
        json.put("brand_list", brand_list);
        json.put("district_code", district_code);
        json.put("address", address);
        json.put("sale_tel", sale_tel);
        json.put("service_tel", service_tel);
        json.put("longitude", longitude);
        json.put("latitude", latitude);
        json.put("appointment_status", appointment_status);
        json.put("washing_status", washing_status);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject addShopNotChk(String avatar_path, String simple_name, String name, JSONArray brand_list, String district_code, String address,
                                    String sale_tel, String service_tel, double longitude, double latitude,
                                    String appointment_status, String washing_status) throws Exception { // 预约状态：ENABLE（开启） DISABLE（关闭）
        String url = "/car-platform/pc/shop/add";
        JSONObject json = new JSONObject();
        json.put("avatar_path", avatar_path);
        json.put("simple_name", simple_name);
        json.put("name", name);
        json.put("brand_list", brand_list);
        json.put("district_code", district_code);
        json.put("address", address);
        json.put("sale_tel", sale_tel);
        json.put("service_tel", service_tel);
        json.put("longitude", longitude);
        json.put("latitude", latitude);
        json.put("appointment_status", appointment_status);
        json.put("washing_status", washing_status);
        String result = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result);
    }


    /**
     * @description:门店管理-编辑门店
     * @author: lxq
     * @time: 2020-11-25
     */

    public JSONObject editShop(Long id, String avatar_path, String simple_name, String name, JSONArray brand_list, String district_code, String address,
                               String sale_tel, String service_tel, double longitude, double latitude,
                               String appointment_status, String washing_status, String rescue_tel) { // 预约状态：ENABLE（开启） DISABLE（关闭）
        String url = "/car-platform/pc/shop/edit";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("avatar_path", avatar_path);
        json.put("simple_name", simple_name);
        json.put("name", name);
        json.put("brand_list", brand_list);
        json.put("district_code", district_code);
        json.put("address", address);
        json.put("sale_tel", sale_tel);
        json.put("service_tel", service_tel);
        json.put("longitude", longitude);
        json.put("latitude", latitude);
        json.put("appointment_status", appointment_status);
        json.put("washing_status", washing_status);
        json.put("rescue_tel", rescue_tel);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:门店管理-门店详情
     * @author: lxq
     * @time: 2020-11-25
     */

    public JSONObject shopDetail(Long id) {
        String url = "/car-platform/pc/shop/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:门店管理-修改门店状态
     * @author: lxq
     * @time: 2020-11-25
     */

    public JSONObject shopDetail(Long id, String type, String status) {
        String url = "/car-platform/pc/shop/change";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("type", type);
        json.put("status", status);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:内容运营-文章列表分页
     * @author: lxq
     * @time: 2020-11-28
     */

    public JSONObject articlePage(Integer page, Integer size, String start_date, String end_date, String register_start_date, String register_end_date) {
        String url = "/car-platform/pc/operation/article/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("start_date", start_date);
        json.put("end_date", end_date);
        json.put("register_start_date", register_start_date);
        json.put("register_end_date", register_end_date);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:内容运营-新建活动
     * @author: lxq
     * @time: 2020-11-28
     */

    public JSONObject addArticle(String title, String pic_type, JSONArray pic_list, String content, String label, String content_type,
                                 String start_date, String end_date, String register_start_date, String register_end_date, Integer total_quota,
                                 String address, Boolean is_can_maintain, Boolean is_voucher, JSONArray voucher_list, String voucher_receive_type,
                                 String voucher_start_date, String voucher_end_date, Integer voucher_get_use_days) {
        String url = "/car-platform/pc/operation/article/add";
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("pic_type", pic_type);
        json.put("pic_list", pic_list);
        json.put("content", content);
        json.put("label", label);
        json.put("content_type", content_type);
        json.put("start_date", start_date);
        json.put("end_date", end_date);
        json.put("register_start_date", register_start_date);
        json.put("register_end_date", register_end_date);
        json.put("total_quota", total_quota);
        json.put("address", address);
        json.put("is_can_maintain", is_can_maintain);
        json.put("is_voucher", is_voucher);
        json.put("voucher_list", voucher_list);
        json.put("voucher_receive_type", voucher_receive_type);
        json.put("voucher_start_date", voucher_start_date);
        json.put("voucher_end_date", voucher_end_date);
        json.put("voucher_get_use_days", voucher_get_use_days);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject addArticleNotChk(String title, String pic_type, JSONArray pic_list, String content, String label, String content_type,
                                       String start_date, String end_date, String register_start_date, String register_end_date, Integer total_quota,
                                       String address, Boolean is_can_maintain, Boolean is_voucher, JSONArray voucher_list, String voucher_receive_type,
                                       String voucher_start_date, String voucher_end_date, Integer voucher_get_use_days) throws Exception {
        String url = "/car-platform/pc/operation/article/add";
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("pic_type", pic_type);
        json.put("pic_list", pic_list);
        json.put("content", content);
        json.put("label", label);
        json.put("content_type", content_type);
        json.put("start_date", start_date);
        json.put("end_date", end_date);
        json.put("register_start_date", register_start_date);
        json.put("register_end_date", register_end_date);
        json.put("total_quota", total_quota);
        json.put("address", address);
        json.put("is_can_maintain", is_can_maintain);
        json.put("is_voucher", is_voucher);
        json.put("voucher_list", voucher_list);
        json.put("voucher_receive_type", voucher_receive_type);
        json.put("voucher_start_date", voucher_start_date);
        json.put("voucher_end_date", voucher_end_date);
        json.put("voucher_get_use_days", voucher_get_use_days);
        String result = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result);
    }

    /**
     * @description:内容运营-编辑活动
     * @author: lxq
     * @time: 2020-11-28
     */

    public JSONObject editArticle(Long id, String content, String address) {
        String url = "/car-platform/pc/operation/article/edit";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("content", content);
        json.put("address", address);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:内容运营-活动置顶
     * @author: lxq
     * @time: 2020-11-28
     */

    public JSONObject topArticle(Long id) {
        String url = "/car-platform/pc/operation/article/top";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:内容运营-查看活动详情
     * @author: lxq
     * @time: 2020-11-28
     */

    public JSONObject detailArticle(Long id) {
        String url = "/car-platform/pc/operation/article/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:内容运营-删除文章
     * @author: lxq
     * @time: 2021-02-04
     */

    public JSONObject delArticle(Long id) {
        String url = "/car-platform/pc/operation/article/delete";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    /**
     * @description:内容运营-报名批量审批
     * @author: lxq
     * @time: 2020-11-28
     */

    public JSONObject approvalArticle(JSONArray register_ids, String status) {
        String url = "/car-platform/pc/operation/approval";
        JSONObject json = new JSONObject();
        json.put("register_ids", register_ids);
        json.put("status", status);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:内容运营-活动开启/关闭 * @author: lxq
     * @time: 2020-11-28
     */

    public JSONObject changeArticleStatus(Long id) {
        String url = "/car-platform/pc/operation/status/change";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:小程序-预约保养 * @author: gly
     * @time: 2020-11-30
     */

    public JSONObject appointmentMaintain(Long shop_id, String staff_id, long time_id, long car_id, String appointment_name, String appointment_phone) {
        String url = "/car-platform/applet/granted/maintain/appointment";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("staff_id", staff_id);
        json.put("time_id", time_id);
        json.put("car_id", car_id);
        json.put("appointment_name", appointment_name);
        json.put("appointment_phone", appointment_phone);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }


    /**
     * @description:PC 登陆
     * * @author: lxq
     * @time: 2020-11-28
     */

    public JSONObject loginPC(String phone, String code) {
        String url = "/account-platform/login-pc";
        JSONObject json = new JSONObject();
        json.put("phone", phone);
        json.put("verification_code", code);
        json.put("type", 1);
        String result = httpPostWithCheckCode(url, json.toJSONString(), EnumTestProduct.JC_DAILY_ZH.getIp());
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject enumMap() {
        String url = "/car-platform/pc/enum-map";
        JSONObject json = new JSONObject();
        return invokeApi(url, json);
    }

    @DataProvider(name = "ERR_PHONE")
    public static Object[] errPhone() {
        return new String[]{
                "1231234123",
                "aaaaaaaaaaa",
                "汉字汉字",
                "10：10",
                "!@#$%^&*()_+{}:",
                "123a123好*123",
                "1         1",
                "123123412345"
        };
    }

    /**
     * @description :编辑用户详情
     * @date :2020/12/16 16:09
     **/

    public JSONObject appletUserInfoEdit(AppletInfoEdit er) {
        String url = "/car-platform/applet/granted/user-info/edit";
        JSONObject json1 = new JSONObject();
        json1.put("name", er.name);
        json1.put("contact", er.contact);
        json1.put("gender", er.gender);
        json1.put("birthday", er.birthday);
//        json1.put("shipping_address", er.shipping_address);
        if (er.parmkey != null) {
            json1.put(er.parmkey, er.parmvalue);
        }
        return invokeApi(url, json1, er.checkcode);
    }

    /**
     * @description :用户详情
     * @date :2020/12/16 16:09
     **/

    public JSONObject appletUserInfoDetail() {
        String url = "/car-platform/applet/granted/user-info/detail";
        JSONObject json1 = new JSONObject();
        return invokeApi(url, json1);
    }

    /**
     * @description :创建活动
     * @date :2020/12/16 16:10
     **/

    public JSONObject pccreateActile(PcCreateActile er) {
        String url = "/car-platform/pc/operation/article/add";
        JSONObject json1 = new JSONObject();
        json1.put("title", er.title);
        json1.put("pic_type", er.pic_type);
        json1.put("pic_list", er.pic_list);
        json1.put("content", er.content);
        json1.put("label", er.label);
        json1.put("content_type", er.content_type);
        json1.put("total_quota", er.total_quota);
        json1.put("register_start_date", er.register_start_date);
        json1.put("register_end_date", er.register_end_date);
        json1.put("start_date", er.start_date);
        json1.put("end_date", er.end_date);
        json1.put("address", er.address);
        json1.put("is_can_maintain", er.is_can_maintain);
        json1.put("is_voucher", er.is_voucher);
        json1.put("voucher_list", er.voucher_list);
        json1.put("voucher_receive_type", er.voucher_receive_type);
        json1.put("voucher_get_use_days", er.voucher_get_use_days);
        return invokeApi(url, json1, er.checkcode);
    }

    /**
     * @description :卡券列表
     * @date :2020/12/16 16:12
     **/

    public JSONObject appletVoucherList(String articleId) {
        String url = "/car-platform/applet/granted/article/voucher/list";
        JSONObject json1 = new JSONObject();
        json1.put("id", articleId);
        return invokeApi(url, json1);
    }

    //app-核销记录
    public JSONObject appWriteOffRecordsPage(String type, String size, String last_value) {
        String url = "/car-platform/m-app/personal-center/write-off-records/page";
        JSONObject json1 = new JSONObject();
        json1.put("type", type);
        json1.put("size", size);
        json1.put("last_value", last_value);
        return invokeApi(url, json1);
    }

    /**
     * @description :小程序预约记录
     * @date :2020/12/17 14:35
     **/
    public JSONObject appletAppointmentList(String type, String size, String last_value) {
        String url = "/car-platform/applet/granted/appointment/list";
        JSONObject json1 = new JSONObject();
        json1.put("type", type);
        json1.put("size", size);
        json1.put("last_value", last_value);
        return invokeApi(url, json1);
    }

    /**
     * @description:PC 系统配置-角色管理
     * * @author: lxq
     * @time: 2020-12-17
     */

    //新建角色
    public JSONObject roleAdd(String name, JSONArray auth_list) {
        String url = "/car-platform/pc/role/add";
        String json =
                "{\n" +
                        "\"name\":\"" + name + "\"," +
                        "\"description\":\"" + "自动化创建的角色" + "\"," +
                        "\"auth_list\":" + auth_list +
                        "}";
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //删除角色
    public JSONObject roleDel(long id) {
        String url = "/car-platform/pc/role/delete";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description :消息管理-推送消息--推送个人
     * * * @author: gly
     * @date :2020/12/17 14:35
     **/
    public JSONObject pushMessage(Boolean if_send_immediately, String message_content, String message_name, String push_target, ArrayList tel_list) {
        String url = "/car-platform/pc/message-manage/push-message";
        JSONObject json1 = new JSONObject();
        json1.put("if_send_immediately", if_send_immediately);
        json1.put("message_content", message_content);
        json1.put("message_name", message_name);
        json1.put("push_target", push_target);
        json1.put("tel_list", tel_list);
        return invokeApi(url, json1);
    }

    /**
     * @description :消息管理-推送消息--推送门店
     * * * @author: gly
     * @date :2020/12/17 14:35
     **/
    public JSONObject pushMessageShop(Boolean if_send_immediately, String message_content, String message_name, String push_target, ArrayList shop_list) {
        String url = "/car-platform/pc/message-manage/push-message";
        JSONObject json1 = new JSONObject();
        json1.put("if_send_immediately", if_send_immediately);
        json1.put("message_content", message_content);
        json1.put("message_name", message_name);
        json1.put("push_target", push_target);
        json1.put("shop_list", shop_list);
        return invokeApi(url, json1);
    }

    /**
     * @description :小程序-我的消息-消息详情
     * * * @author: gly
     * @date :2020/12/17 14:35
     **/
    public JSONObject messageDetail(String id) {
        String url = "/car-platform/applet/granted/message/detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :自主核销
     * @date :2020/12/22 17:46
     **/
    public JSONObject appleterification(String id, String verification_code, Boolean checkcode) {
        String url = "/car-platform/applet/granted/voucher/verification";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("verification_code", verification_code);
        return invokeApi(url, json1, checkcode);
    }

    /**
     * @description :PC-创建销售客户
     * * * @author: gly
     * @date :2020/12/17 14:35
     **/
    public JSONObject createCustomer(String shopId, String customerName, String customerPhone, String sex, String customerType) {
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/create-customer";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("customer_name", customerName);
        json.put("customer_phone", customerPhone);
        json.put("sex", sex);
        json.put("customer_type", customerType);
        return invokeApi(url, json);
    }

    public JSONObject appointmentTimeEdit(JSONObject json) {
        String url = "/car-platform/pc/manage/appointment/time-range/edit";
        return invokeApi(url, json);
    }

    public JSONObject shopStatusChange(String id, String type, String status) {
        String url = "/car-platform/pc/shop/status/change";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("type", type);
        json.put("status", status);
        return invokeApi(url, json);
    }

    public JSONObject pcappointmentConfig(PcAppointmentConfig er) {
        String url = "/car-platform/pc/manage/appointment/config";
        JSONObject json1 = new JSONObject();
        json1.put("type", er.type);
        json1.put("remind_time", er.remind_time);
        json1.put("replay_time_limit", er.replay_time_limit);
        json1.put("appointment_interval", er.appointment_interval);
        json1.put("on_time_reward", er.on_time_reward);
        json1.put("is_send_voucher", er.is_send_voucher);
        json1.put("vouchers", er.vouchers);
        json1.put("voucher_start", er.voucher_start);
        json1.put("voucher_end", er.voucher_end);
        return invokeApi(url, json1, er.checkcode);
    }


    /**
     * @description :会员权益列表
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject equityPage(String shopId, String page, String size) {
        String url = "/car-platform/pc/vip-marketing/equity/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("page", page);
        json.put("size", size);
        return invokeApi(url, json);
    }

    /**
     * @description :洗车列表
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject washCarManagerPage(String shopId, String page, String size) {
        String url = "/car-platform/pc/vip-marketing/wash-car-manager/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("page", page);
        json.put("size", size);
        return invokeApi(url, json);
    }

    /**
     * @description :洗车列表
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject washCarManagerPage(String shopId, String page, String size, String washStartTime, String washEndTime) {
        String url = "/car-platform/pc/vip-marketing/wash-car-manager/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("wash_end_time", washEndTime);
        json.put("wash_start_time", washStartTime);
        return invokeApi(url, json);
    }

    /**
     * @description :调整次数记录列表
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject adjustNumberRecord(String shopId, String page, String size) {
        String url = "/car-platform/pc/vip-marketing/wash-car-manager/adjust-number/record";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("page", page);
        json.put("size", size);
        return invokeApi(url, json);
    }

    /**
     * @description :调整次数记录列表
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject adjustNumberRecord(String shopId, String page, String size, String adjustStartTime, String adjustEndTime) {
        String url = "/car-platform/pc/vip-marketing/wash-car-manager/adjust-number/record";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("adjust_end_time", adjustEndTime);
        json.put("adjust_start_time", adjustStartTime);
        return invokeApi(url, json);
    }

    /**
     * @description :签到配置列表
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject signInConfigPage(String shopId, String page, String size) {
        String url = "jiaochen/pc/vip-marketing/sign_in_config/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("page", page);
        json.put("size", size);
        return invokeApi(url, json);
    }

    /**
     * @description :分享管理
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject shareManagerPage(String shopId, String page, String size) {
        String url = "/car-platform/pc/vip-marketing/share-manager/page";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("page", page);
        json.put("size", size);
        return invokeApi(url, json);
    }

    /**
     * @description :卡券表单
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject oucherFormVoucherPage(String shopId, String page, String size) {
        String url = "/car-platform/pc/voucher-manage/voucher-form/voucher-page";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("page", page);
        json.put("size", size);
        return invokeApi(url, json);
    }

    /**
     * @description :增发记录
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject additionalRecordPage(String shopId, String page, String size) {
        String url = "/car-platform/pc/voucher-manage/additional-record";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("page", page);
        json.put("size", size);
        return invokeApi(url, json);
    }


    //轿辰2.0

    /**
     * @description :app跟进列表
     * @date :2021/1/7 18:46
     **/

    public JSONObject appFollowUpList(String size, JSONObject last_value) {
        String url = "/car-platform/m-app/follow-up/page";
        JSONObject json1 = new JSONObject();
        json1.put("size", size);
        json1.put("last_value", last_value);
        json1.put("page", 10);

        return invokeApi(url, json1);
    }

    public JSONObject appFollowUp(String id, String shop_id, String remark, Boolean checkcode) {
        String url = "/car-platform/m-app/follow-up/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("id", id);
        json1.put("remark", remark);

        return invokeApi(url, json1, checkcode);
    }

    /**
     * @description :app我的消息列表
     * @date :2021/1/7 19:06
     **/

    public JSONObject appmessageList(String size, JSONObject last_value) {
        String url = "/car-platform/m-app/message/page";
        JSONObject json1 = new JSONObject();
        json1.put("size", size);
        json1.put("last_value", last_value);

        return invokeApi(url, json1);
    }

    public JSONObject appmessagedetail(String id) {
        String url = "/car-platform/m-app/message/detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :新建分销员
     * @date :2021/1/13 11:47
     **/

    public JSONObject SalesCreate(PcCreateStoreSales er) {
        String url = "/car-platform/pc/store/sales/create";
        JSONObject json1 = new JSONObject();
        json1.put("sales_phone", er.sales_phone);
        json1.put("sales_name", er.sales_name);
        json1.put("shop_id", er.shop_id);
        json1.put("shop_name", er.shop_name);
        json1.put("dept_name", er.dept_name);
        json1.put("job_name", er.job_name);

        return invokeApi(url, json1, er.checkcode);
    }

    public JSONObject SalesList(String page, String size, String sales_phone, String shop_id) {
        String url = "/car-platform/pc/store/sales/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("sales_phone", sales_phone);
        json1.put("shop_id", shop_id);

        return invokeApi(url, json1);
    }

    public JSONObject StoreCommodityList(String page, String size, String commodity_name) {
        String url = "/car-platform/pc/store/commodity/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("commodity_name", commodity_name);

        return invokeApi(url, json1);
    }

    /**
     * @description :新建商城套餐
     * @date :2021/1/13 14:17
     **/
    public JSONObject CreateStoreCommodity(PcCreateStoreCommodity er) {
        String url = "/car-platform/pc/store/commodity/create";
        JSONObject json1 = new JSONObject();
        json1.put("commodity_name", er.commodity_name);
        json1.put("commodity_specification", er.commodity_specification);
        json1.put("price", er.price);
        json1.put("commission", er.commission);
        json1.put("invitation_payment", er.invitation_payment);
        json1.put("subject_type", er.subject_type);
        json1.put("voucher_list", er.voucher_list);


        return invokeApi(url, json1, er.checkcode);
    }


    //商品套餐详情
    public JSONObject StoreCommodityDetail(String id) {
        String url = "/car-platform/pc/store/commodity/detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :修改商城套餐
     * @date :2021/1/13 14:17
     **/
    public JSONObject EditStoreCommodity(PcCreateStoreCommodity er) {
        String url = "/car-platform/pc/store/commodity/edit";
        JSONObject json1 = new JSONObject();
        json1.put("id", er.id);
        json1.put("commodity_name", er.commodity_name);
        json1.put("commodity_specification", er.commodity_specification);
        json1.put("affiliation", er.affiliation);
        json1.put("price", er.price);
        json1.put("commission", er.commission);
        json1.put("invitation_payment", er.invitation_payment);
        json1.put("volume", er.volume);

        return invokeApi(url, json1);
    }


    /**
     * @description :订单商城
     * @date :2021/1/13 15:35
     **/
    public JSONObject StoreorderPage(String page, String size, String bind_phone, String commodity_name, String pay_time, String order_number) {
        String url = "/car-platform/pc/store/order/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("bind_phone", bind_phone);
        json1.put("commodity_name", commodity_name);
        json1.put("pay_time", pay_time);
        json1.put("order_number", order_number);

        return invokeApi(url, json1);
    }

    //商品订单详情
    public JSONObject StoreOrderDetail(String order_number) {
        String url = "/car-platform/pc/store/order/detail";
        JSONObject json1 = new JSONObject();
        json1.put("order_number", order_number);

        return invokeApi(url, json1);
    }

    //编辑订单
    public JSONObject EditOrder(PcEditStoreOrder er) {
        String url = "/car-platform/pc/store/order/edit";
        JSONObject json1 = new JSONObject();
        json1.put("order_number", er.order_number);
        json1.put("express_number", er.express_number);
        json1.put("sales_phone", er.sales_phone);
        json1.put("bind_phone", er.bind_phone);
        json1.put("commission", er.commission);
        json1.put("invitation_payment", er.invitation_payment);
        json1.put("remark", er.remark);

        return invokeApi(url, json1);
    }

    //商品订单，发放
    public JSONObject volumeSend(int id) {
        String url = "/car-platform/pc/store/order/volume-send";
        JSONObject json1 = new JSONObject();

        json1.put("id", id);

        return invokeApi(url, json1);
    }

    //商品订单，作废
    public JSONObject volumeCancel(int id) {
        String url = "/car-platform/pc/store/order/volume-cancel";
        JSONObject json1 = new JSONObject();

        json1.put("id", id);

        return invokeApi(url, json1);
    }

    //商品套餐下架、上架
    public JSONObject communityUpAndDown(String status, int id) {
        String url = "/car-platform/pc/store/commodity/up-or-down";
        JSONObject json1 = new JSONObject();

        json1.put("id", id);
        json1.put("status", status);

        return invokeApi(url, json1);
    }

    /**
     * @description :智能提醒
     * @date :2021/1/13 19:45
     **/
    public JSONObject remindPage(String page, String size, String item) {
        String url = "/car-platform/pc/manage/intelligent-remind/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("item", item);

        return invokeApi(url, json1);
    }

    public JSONObject createRemindMethod(PcCreateRemind er) {
        String url = "/car-platform/pc/manage/intelligent-remind/add";
        JSONObject json1 = new JSONObject();
        json1.put("item", er.item);
        json1.put("content", er.content);
        json1.put("vouchers", er.vouchers);
        json1.put("effective_days", er.effective_days);
        json1.put("days", er.days);
        json1.put("mileage", er.mileage);

        return invokeApi(url, json1, er.checkcode);
    }

    public JSONObject editRemindMethod(PcCreateRemind er) {
        String url = "/car-platform/pc/manage/intelligent-remind/edit";
        JSONObject json1 = new JSONObject();
        json1.put("item", er.item);
        json1.put("id", er.id);
        json1.put("content", er.content);
        json1.put("vouchers", er.vouchers);
        json1.put("effective_days", er.effective_days);
        json1.put("days", er.days);
        json1.put("mileage", er.mileage);

        return invokeApi(url, json1);
    }

    /**
     * @description :app变更接待
     * @date :2021/1/21 14:43
     **/

    public JSONObject receptorChange(Long id, Long shop_id, String receptor_id) {
        String url = "/car-platform/m-app/task/reception/receptor/change";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("shop_id", shop_id);
        json1.put("receptor_id", receptor_id);

        return invokeApi(url, json1);
    }

    /**
     * @description :app接待人员列表
     * @date :2021/1/21 14:48
     **/

    public JSONObject receptorList(Long shop_id) {
        String url = "/car-platform/m-app/task/reception/receptor/list";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);

        return invokeApi(url, json1);
    }

    /**
     * ----------------------------积分商城相关开始-----------------------------
     */


    /**
     * @description :商品品类列表
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryPage(Integer page, Integer size, Boolean category_status, Long first_category, Long second_category, Long third_category) {
        String url = "/car-platform/pc/integral-mall/category-page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("category_status", category_status);
        json1.put("first_category", first_category);
        json1.put("second_category", second_category);
        json1.put("third_category", third_category);

        return invokeApi(url, json1);
    }

    /**
     * @description :创建商品品类
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryCreate(Boolean Checkcode, String category_name, String category_level, String belong_category, String belong_pic, Long id) {
        String url = "/car-platform/pc/integral-mall/create-category";
        JSONObject json1 = new JSONObject();
        json1.put("category_name", category_name);
        json1.put("category_level", category_level);
        json1.put("belong_category", belong_category);
        json1.put("belong_pic", belong_pic);
        json1.put("id", id);
        return invokeApi(url, json1, Checkcode);
    }

    /**
     * @description :品类下拉列表
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryList(String category_level) {
        String url = "/car-platform/pc/integral-mall/category-list";
        JSONObject json1 = new JSONObject();
        json1.put("category_level", category_level);
        return invokeApi(url, json1);
    }

    /**
     * @description :所属品类列表
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryBelong(String category_level) {
        String url = "/car-platform/pc/integral-mall/belongs-category";
        JSONObject json1 = new JSONObject();
        json1.put("category_level", category_level);
        return invokeApi(url, json1);
    }

    /**
     * @description :修改品类状态
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryChgStatus(Long id, Boolean status) {
        String url = "/car-platform/pc/integral-mall/change-status";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("status", status);
        return invokeApi(url, json1);
    }

    public JSONObject categoryChgStatus(Long id, Boolean status, Boolean chk) {
        String url = "/car-platform/pc/integral-mall/change-status";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("status", status);
        return invokeApi(url, json1, chk);
    }

    /**
     * @description :查看品类详情
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryDetail(Integer id, Integer page, Integer size) {
        String url = "/car-platform/pc/integral-mall/category-detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("page", page);
        json1.put("size", size);
        return invokeApi(url, json1);
    }

    /**
     * @description :修改商品品类
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryEdit(Boolean Checkcode, Long id, String category_name, String category_level, String belong_category, String belong_pic) {
        String url = "/car-platform/pc/integral-mall/edit-category";
        JSONObject json1 = new JSONObject();
        json1.put("category_name", category_name);
        json1.put("category_level", category_level);
        json1.put("belong_category", belong_category);
        json1.put("belong_pic", belong_pic);
        json1.put("id", id);
        return invokeApi(url, json1, Checkcode);
    }

    /**
     * @description :删除商品品类
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryDel(Long id, Boolean chkcode) {
        String url = "/car-platform/pc/integral-mall/delete-category";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :商品品牌分页
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandPage(Integer page, Integer size, String brand_name, Boolean brand_status) {
        String url = "/car-platform/pc/integral-mall/brand-page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("brand_name", brand_name);
        json1.put("brand_status", brand_status);
        return invokeApi(url, json1);
    }

    /**
     * @description :品牌下拉列表
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandList() {
        String url = "/car-platform/pc/integral-mall/brand-list";
        JSONObject json1 = new JSONObject();
        return invokeApi(url, json1);
    }

    /**
     * @description :创建品牌
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandCreat(Boolean chkcode, Long id, String brand_name, String brand_description, String brand_pic) {
        String url = "/car-platform/pc/integral-mall/create-brand";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("brand_name", brand_name);
        json1.put("brand_description", brand_description);
        json1.put("brand_pic", brand_pic);
        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :修改品牌状态
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandChgStatus(Long id, Boolean brand_status, Boolean chk) {
        String url = "/car-platform/pc/integral-mall/change-brand-status";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("status", brand_status);
        return invokeApi(url, json1, chk);
    }

    /**
     * @description :商品品牌详情
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandDetail(Integer id, Integer page, Integer size) {
        String url = "/car-platform/pc/integral-mall/brand-detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("page", page);
        json1.put("size", size);
        return invokeApi(url, json1);
    }

    /**
     * @description :修改品牌
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandEdit(Boolean chkcode, Long id, String brand_name, String brand_description, String brand_pic) {
        String url = "/car-platform/pc/integral-mall/edit-brand";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("brand_name", brand_name);
        json1.put("brand_description", brand_description);
        json1.put("brand_pic", brand_pic);
        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :删除商品品牌
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandDel(Long id, Boolean chkcode) {
        String url = "/car-platform/pc/integral-mall/delete-brand";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :创建商品规格
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsCreate(String specifications_name, Long belongs_category, JSONArray category_list, Long id, Boolean chkcode) {
        String url = "/car-platform/pc/integral-mall/create-specifications";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("specifications_name", specifications_name);
        json1.put("belongs_category", belongs_category);
        json1.put("category_list", category_list);
        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :修改规格状态
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsChgStatus(Long id, Boolean... status) {
        String url = "/car-platform/pc/integral-mall/change-specifications-status";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("status", status[0]);
        if (status.length > 1) {
            return invokeApi(url, json1, status[1]);
        } else {
            return invokeApi(url, json1);
        }

    }

    /**
     * @description :删除规格状态
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsDel(Long id, Boolean... chk) {
        String url = "/car-platform/pc/integral-mall/delete-specifications";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        if (chk.length > 0) {
            return invokeApi(url, json1, chk[0]);
        } else {
            return invokeApi(url, json1);
        }
    }


    /**
     * @description :商品规格详情
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsDetail(Long id, Integer page, Integer size) {
        String url = "/car-platform/pc/integral-mall/specifications-detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("page", page);
        json1.put("size", size);
        return invokeApi(url, json1);
    }

    /**
     * @description :修改规格
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsEdit(String specifications_name, Long belongs_category, JSONArray category_list, Long id, Boolean chkcode) {
        String url = "/car-platform/pc/integral-mall/edit-specifications";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("specifications_name", specifications_name);
        json1.put("belongs_category", belongs_category);
        json1.put("specifications_list", category_list);
        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :删除商品规格
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsDel(Integer id, Integer page, Integer size, Boolean chkcode) {
        String url = "/car-platform/pc/integral-mall/delete-brand";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("page", page);
        json1.put("size", size);
        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :商品管理-品类树
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryTree() {
        String url = "/car-platform/pc/integral-mall/category-tree";
        JSONObject json1 = new JSONObject();
        return invokeApi(url, json1);
    }

    /**
     * @description :商品管理列表
     * @date :2021/1/20 14:00
     **/
    public JSONObject goodsManagePage(Integer page, Integer size, String goods_name, Long goods_brand, String goods_status,
                                      Integer first_category, Integer second_category, Integer third_category) {
        String url = "/car-platform/pc/integral-mall/goods-manage-page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("goods_name", goods_name);
        json1.put("goods_brand", goods_brand);
        json1.put("goods_status", goods_status);
        json1.put("first_category", first_category);
        json1.put("second_category", second_category);
        json1.put("third_category", third_category);
        return invokeApi(url, json1);
    }

    /**
     * @description :商品规格列表
     * @date :2021/3/03 19:00
     **/
    public JSONObject specificationsPage(Integer page, Integer size, String specifications_name, Long first_category, Boolean specifications_status) {
        String url = "/car-platform/pc/integral-mall/specifications-page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("specifications_name", specifications_name);
        json1.put("first_category", first_category);
        json1.put("specifications_status", specifications_status);
        return invokeApi(url, json1);
    }

    /**
     * @description :商品导出
     * @date :2021/1/20 14:00
     **/
    public JSONObject goodsManageExport(Integer page, Integer size, String goods_name, Integer goods_brand, String goods_status,
                                        Integer first_category, Integer second_category, Integer third_category,
                                        String export_type, JSONArray ids, Boolean chkcode) {
        String url = "/car-platform/pc/integral-mall/goods-manage/export";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("goods_name", goods_name);
        json1.put("goods_brand", goods_brand);
        json1.put("goods_status", goods_status);
        json1.put("first_category", first_category);
        json1.put("second_category", second_category);
        json1.put("third_category", third_category);
        json1.put("export_type", export_type);
        json1.put("ids", ids);
        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :上架/下架
     * @date :2021/1/20 14:00
     **/
    public JSONObject goodsChgStatus(Long id, String status, Boolean checkCode) {
        String url = "/car-platform/pc/integral-mall/change-goods-status";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("status", status);
        return invokeApi(url, json1, checkCode);
    }

    /**
     * @description :创建商品
     * @date :2021/1/22 17:07
     **/

    public JSONObject createGoodMethod(PcCreateGoods er) {
        String url = "/car-platform/pc/integral-mall/create-goods";
        JSONObject json1 = new JSONObject();
        json1.put("id", er.id);
        json1.put("goods_name", er.goods_name);
        json1.put("goods_description", er.goods_description);
        json1.put("first_category", er.first_category);
        json1.put("second_category", er.second_category);
        json1.put("third_category", er.third_category);
        json1.put("goods_brand", er.goods_brand);
        json1.put("goods_pic_list", er.goods_pic_list);
        json1.put("price", er.price);
        json1.put("select_specifications", er.select_specifications);
        json1.put("goods_specifications_list", er.goods_specifications_list);
        json1.put("goods_detail", er.goods_detail);

        return invokeApi(url, json1, er.checkcode);
    }

    /**
     * @description :删除商品
     * @date :2021/1/22 17:07
     **/

    public JSONObject deleteGoodMethod(Long id) {
        String url = "/car-platform/pc/integral-mall/delete-goods";
        JSONObject json1 = new JSONObject();

        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :商品管理列表
     * @date :2021/1/22 17:07
     **/

    public JSONObject GoodsList(String page, String size) {
        String url = "/car-platform/pc/integral-mall/goods-manage-page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);

        return invokeApi(url, json1);
    }

    /**
     * @description :编辑商品
     * @date :2021/1/22 17:07
     **/

    public JSONObject editGoodMethod(PcCreateGoods er) {
        String url = "/car-platform/pc/integral-mall/edit-goods";
        JSONObject json1 = new JSONObject();
        json1.put("id", er.id);
        json1.put("goods_name", er.goods_name);
        json1.put("goods_description", er.goods_description);
        json1.put("first_category", er.first_category);
        json1.put("second_category", er.second_category);
        json1.put("third_category", er.third_category);
        json1.put("goods_brand", er.goods_brand);
        json1.put("goods_pic_list", er.goods_pic_list);
        json1.put("price", er.price);
        json1.put("select_specifications", er.select_specifications);
        json1.put("goods_specifications_list", er.goods_specifications_list);
        json1.put("goods_detail", er.goods_detail);

        return invokeApi(url, json1, er.checkcode);
    }

    /**
     * @description :积分兑换列表
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangePage(Integer page, Integer size, String exchange_goods, String exchange_type, String status) {
        String url = "/car-platform/pc/integral-center/exchange-page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("exchange_goods", exchange_goods);
        json1.put("exchange_type", exchange_type);
        json1.put("status", status);

        return invokeApi(url, json1);
    }

    /**
     * @description :积分兑换列表--导出
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeExport(Integer page, Integer size, String exchange_goods, String exchange_type, String status, String export_type, JSONArray ids, Boolean chkcode) {
        String url = "/car-platform/pc/integral-center/exchange/export";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("exchange_goods", exchange_goods);
        json1.put("exchange_type", exchange_type);
        json1.put("status", status);
        json1.put("export_type", export_type);
        json1.put("ids", ids);

        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :创建积分兑换商品
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodCreat(pcCreateExchangeGoods ex) {
        String url = "/car-platform/pc/integral-center/create-exchange-goods";
        JSONObject json1 = new JSONObject();
        json1.put("id", ex.id);
        json1.put("exchange_goods_type", ex.exchange_goods_type);
        json1.put("goods_id", ex.goods_id);
        json1.put("exchange_start_time", ex.exchange_start_time);
        json1.put("exchange_end_time", ex.exchange_end_time);
        json1.put("exchange_price", ex.exchange_price);
        json1.put("exchange_num", ex.exchange_num);
        json1.put("is_limit", ex.is_limit);
        json1.put("exchange_people_num", ex.exchange_people_num); // 接口有问题
        json1.put("specification_list", ex.specification_list);

        return invokeApi(url, json1, ex.chkcode);
    }

    /**
     * @description :编辑积分兑换商品
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodEdit(pcCreateExchangeGoods ex) {
        String url = "/car-platform/pc/integral-center/edit-exchange-goods";
        JSONObject json1 = new JSONObject();
        json1.put("id", ex.id);
        json1.put("exchange_goods_type", ex.exchange_goods_type);
        json1.put("goods_id", ex.goods_id);
        json1.put("exchange_start_time", ex.exchange_start_time);
        json1.put("exchange_end_time", ex.exchange_end_time);
        json1.put("exchange_price", ex.exchange_price);
        json1.put("exchange_num", ex.exchange_num);
        json1.put("is_limit", ex.is_limit);
        json1.put("exchange_people_num", ex.exchange_people_num);
        json1.put("specification_list", ex.specification_list);

        return invokeApi(url, json1, ex.chkcode);
    }


    /**
     * @description :积分兑换商品详情
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodDetail(Integer id) {
        String url = "/car-platform/pc/integral-center/exchange-goods-detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :积分兑换开关
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodChgStatus(Integer id, Boolean status, Boolean chkcode) {
        String url = "/car-platform/pc/integral-center/change-switch-status";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("status", status);
        return invokeApi(url, json1, chkcode);
    }


    /**
     * @description :积分兑换置顶
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodTop(Integer id, Boolean chkcode) {
        String url = "/car-platform/pc/integral-center/make-top";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :积分兑换删除
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodDel(Integer id) {
        String url = "/car-platform/pc/integral-center/delete-exchange-goods";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :积分兑换库存
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodStock(Integer id) {
        String url = "/car-platform/pc/integral-center/exchange-goods-stock";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :编辑积分兑换库存
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodStockEdit(Integer id, String goods_name, String change_stock_type, Integer num) {
        String url = "/car-platform/pc/integral-center/edit-exchange-stock";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("goods_name", goods_name);
        json1.put("change_stock_type", change_stock_type);
        json1.put("num", num);
        return invokeApi(url, json1);
    }

    /**
     * @description :兑换商品规格详情列表
     * @date :2021/1/27 16:00
     **/

    public JSONObject commodityList(Integer id) {
        String url = "/car-platform/pc/integral-center/commodity-specifications-list";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :兑换商品库存明细
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeStockPage(Integer page, Integer size, Integer id) {
        String url = "/car-platform/pc/integral-center/exchange-stock-page";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("page", page);
        json1.put("size", size);
        return invokeApi(url, json1);
    }

    /**
     * @description :积分兑换明细
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeDetail(Integer page, Integer size, Integer id, String exchange_customer_name, String exchange_type,
                                     String exchange_start_time, String exchange_end_time) {
        String url = "/car-platform/pc/integral-center/exchange-detailed";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("exchange_customer_name", exchange_customer_name);
        json1.put("exchange_type", exchange_type);
        json1.put("exchange_start_time", exchange_start_time);
        json1.put("exchange_end_time", exchange_end_time);
        return invokeApi(url, json1);
    }

    /**
     * @description :积分兑换明细导出
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeDetailExport(Integer page, Integer size, Integer id, String exchange_customer_name, String exchange_type,
                                           String exchange_start_time, String exchange_end_time, String export_type, JSONArray ids) {
        String url = "/car-platform/pc/integral-center/exchange-detail/export";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("exchange_customer_name", exchange_customer_name);
        json1.put("exchange_type", exchange_type);
        json1.put("exchange_start_time", exchange_start_time);
        json1.put("exchange_end_time", exchange_end_time);
        json1.put("export_type", export_type);
        json1.put("ids", ids);
        return invokeApi(url, json1);
    }

    /**
     * @description :积分兑换订单
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeOrder(Integer page, Integer size, String order_id, String start_time, String end_time, String order_status,
                                    String member, String goods_name) {
        String url = "/car-platform/pc/integral-center/exchange-order";
        JSONObject json1 = new JSONObject();
        json1.put("order_id", order_id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("member", member);
        json1.put("goods_name", goods_name);
        json1.put("start_time", start_time);
        json1.put("end_time", end_time);
        json1.put("order_status", order_status);
        return invokeApi(url, json1);
    }

    /**
     * @description :积分兑换订单导出
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeOrderExport(Integer page, Integer size, String order_id, String start_time, String end_time, String order_status,
                                          String member, String goods_name, String export_type, JSONArray ids, Boolean chkcode) {
        String url = "/car-platform/pc/integral-center/exchange-order/export";
        JSONObject json1 = new JSONObject();
        json1.put("order_id", order_id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("member", member);
        json1.put("goods_name", goods_name);
        json1.put("start_time", start_time);
        json1.put("end_time", end_time);
        json1.put("order_status", order_status);
        json1.put("export_type", export_type);
        json1.put("ids", ids);
        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :取消订单
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeOrderCancel(Integer id) {
        String url = "/car-platform/pc/integral-center/cancel-order";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :确认发货
     * @date :2021/1/27 16:00
     **/

    public JSONObject confirmShipment(Long id, String odd_numbers, Boolean chkcode) {
        String url = "/car-platform/pc/integral-center/confirm_shipment";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("odd_numbers", odd_numbers);
        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :订单明细
     * @date :2021/1/27 16:00
     **/

    public JSONObject orderDetail(Integer id) {
        String url = "/car-platform/pc/integral-center/order-detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :积分基础规则设置
     * @date :2021/02/02 16:00
     **/

    public JSONObject basicRule(Integer page, Integer size) {
        String url = "/car-platform/pc/integral-center/integral-basic-rules";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        return invokeApi(url, json1);
    }

    /**
     * @description :积分规则设置
     * @date :2021/02/02 16:00
     **/

    public JSONObject setRule(Long id, Integer year, String description, String rule_type) {
        String url = "/car-platform/pc/integral-center/integral-rule-set";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("year", year);
        json1.put("description", description);
        json1.put("rule_type", rule_type);
        return invokeApi(url, json1);
    }

    /**
     * @description :积分兑换规则设置
     * @date :2021/02/02 16:00
     **/

    public JSONObject exchangeRule(Integer page, Integer size) {
        String url = "/car-platform/pc/integral-center/integral-exchange-rules";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        return invokeApi(url, json1);
    }


    //-------------小程序部分------------------------


    /**
     * @description :小程序积分商城首页
     * @date :2021/1/27 14:00
     **/
    public JSONObject appletHomePage() {
        String url = "/car-platform/applet/granted/integral-mall/home-page";
        JSONObject json1 = new JSONObject();
        return invokeApi(url, json1);
    }

    /**
     * @description :小程序积分商城首页
     * @date :2021/1/27 14:00
     **/
    public JSONObject appletIntegralRule(Integer id, String status) {
        String url = "/car-platform/applet/granted/integral-mall/integral-rule";
        JSONObject json1 = new JSONObject();
        return invokeApi(url, json1);
    }

    /**
     * @description :小程序积分商城商品列表
     * @date :2021/1/27 14:00
     **/
    public JSONObject appletMallCommidityList(Integer size, JSONObject last_value, String integral_sort, String integral_num, Boolean status) {
        String url = "/car-platform/applet/granted/integral-mall/commodity-list";
        JSONObject json1 = new JSONObject();
        json1.put("size", size);
        json1.put("last_value", last_value);
        json1.put("integral_sort", integral_sort);
        json1.put("integral_num", integral_num);
        json1.put("status", status);
        return invokeApi(url, json1);
    }

    /**
     * @description :小程序积分商城商品详情
     * @date :2021/1/27 14:00
     **/
    public JSONObject appletMallCommidityDetail(Integer id) {
        String url = "/car-platform/applet/granted/integral-mall/commodity-detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :小程序积分商城创建收货地址
     * @date :2021/1/27 14:00
     **/
    public JSONObject appletMallCreateAddr(String name, String phone, String district_code, String address, String postal_code) {
        String url = "/car-platform/applet/granted/integral-mall/create-address";
        JSONObject json1 = new JSONObject();
        json1.put("name", name);
        json1.put("phone", phone);
        json1.put("district_code", district_code);
        json1.put("address", address);
        json1.put("postal_code", postal_code);
        return invokeApi(url, json1);
    }

    /**
     * @description :小程序实体商品提交订单
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletSubmitOrder(submitOrder or) {
        String url = "/car-platform/applet/granted/integral-mall/submit-order";
        JSONObject json1 = new JSONObject();
        json1.put("commodity_id", or.commodity_id);
        json1.put("specification_id", or.specification_id);
        json1.put("buyer_message", or.buyer_message);
        json1.put("sms_notify", or.sms_notify);
        json1.put("commodity_num", or.commodity_num);
        json1.put("district_code", or.district_code);
        json1.put("address", or.address);
        json1.put("receiver", or.receiver);
        json1.put("receive_phone", or.receive_phone);
        return invokeApi(url, json1, or.chkcode);
    }

    /**
     * @description :小程序虚拟商品提交订单
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletSubmitExchange(Long id, Boolean chkcode) {
        String url = "/car-platform/applet/granted/integral-mall/integral-exchange";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1, chkcode);
    }

    /**
     * @description :小程序 个人积分详情记录
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletIntegralRecord(Integer size, Object last_value, String type, String start_time, String end_time) {
        String url = "/car-platform/applet/granted/integral-mall/integral-record";
        JSONObject json1 = new JSONObject();
        json1.put("size", size);
        json1.put("last_value", last_value);
        json1.put("type", type);
        json1.put("start_time", start_time);
        json1.put("end_time", end_time);
        return invokeApi(url, json1);
    }

    /**
     * @description :小程序 兑换记录
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletExchangeRecord(Integer size, Object last_value, String status) {
        String url = "/car-platform/applet/granted/integral-mall/exchange-record";
        JSONObject json1 = new JSONObject();
        json1.put("size", size);
        json1.put("last_value", last_value);
        json1.put("status", status);
        return invokeApi(url, json1);
    }

    /**
     * @description :小程序 订单详情
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletExchangeRecordDetail(Long id) {
        String url = "/car-platform/applet/granted/integral-mall/exchange-record-detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :小程序 确认收货
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletconfirmReceive(Long id) {
        String url = "/car-platform/applet/granted/integral-mall/confirm-receive";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }


    /**
     * ---------------------------------积分商城相关结束--------------------------------
     */

    /**
     * @description :applet道路救援
     * @date :2021/1/21 16:14
     **/

    public JSONObject rescueApply(String shop_id, JSONArray coordinate) {
        String url = "/car-platform/applet/granted/rescue/apply";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("coordinate", coordinate);
        return invokeApi(url, json1);
    }

    /**
     * @description :pc道路救援
     * @date :2021/1/21 16:14
     **/
    public JSONObject pcrescuePage(int page, int size) {
        String url = "/car-platform/pc/manage/rescue/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        return invokeApi(url, json1);
    }

    /**
     * @description :小程序授权手机号
     * @date :2021/1/21 19:20
     **/

    public JSONObject grantPhone(String phone, String verification_code, String recommend_customer_id, String business_type) {
        String url = "/car-platform/applet/grant/phone";
        JSONObject json1 = new JSONObject();
        json1.put("phone", phone);
        json1.put("verification_code", verification_code);
        json1.put("recommend_customer_id", recommend_customer_id);
        json1.put("business_type", business_type);
        return invokeApi(url, json1);
    }

    /**
     * @description :洗车次数
     * @date :2021/1/21 19:20
     **/

    public JSONObject washTimes() {
        String url = "/car-platform/applet/granted/member-center/car-wash/remain-number";
        JSONObject json1 = new JSONObject();
        return invokeApi(url, json1);
    }

    /**
     * @description :开始洗车
     * @date :2021/1/21 19:20
     **/

    public JSONObject carWsah(String car_wash_shop_id) {
        String url = "/car-platform/applet/granted/member-center/car-wash/start";
        JSONObject json1 = new JSONObject();
        json1.put("car_wash_shop_id", car_wash_shop_id);

        return invokeApi(url, json1);
    }

    /**
     * @description :免费洗车列表
     * @date :2021/1/21 19:29
     **/

    public JSONObject carWashShopList(JSONArray coordinate) {
        String url = "/car-platform/applet/granted/member-center/car-wash/shop-list";
        JSONObject json1 = new JSONObject();
        json1.put("coordinate", coordinate);

        return invokeApi(url, json1);
    }

    /**
     * @description :品牌列表
     * @date :2021/1/22 17:20
     **/

    public JSONObject bandList() {
        String url = "/car-platform/pc/integral-mall/brand-list";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    //品类
    public JSONObject categoryList() {
        String url = "/car-platform/pc/integral-mall/category-tree";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    //品类规格下拉
    public JSONObject specifications(Long first_category) {
        String url = "/car-platform/pc/integral-mall/specifications-list";
        JSONObject json1 = new JSONObject();
        json1.put("first_category", first_category);
        return invokeApi(url, json1);
    }

    //道路救援门店列表
    public JSONObject rescueShopList(JSONArray coordinate, String washingStatus) {
        String url = "/car-platform/applet/granted/rescue/shop/list";
        JSONObject json1 = new JSONObject();
        if (!coordinate.equals(null)) {
            json1.put("coordinate", coordinate);

        }
        if (!washingStatus.equals("null")) {
            json1.put("washingStatus", washingStatus);
        }
        return invokeApi(url, json1);
    }

    /**
     * @description :V2.0智能提醒
     * @date :2021/2/1
     **/
    public JSONObject remindPage(String page, String size, String item, String pram, String result) {
        String url = "/car-platform/pc/manage/intelligent-remind/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("item", item);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }


    /**
     * @description :V2.0洗车管理列表
     * @date :2021/2/1
     **/
    public JSONObject washCarManagerPage(WashCarManagerVariable washCarManagerVariable) {
        String url = "/car-platform/pc/vip-marketing/wash-car-manager/page";
        JSONObject json = new JSONObject();
        json.put("page", washCarManagerVariable.page);
        json.put("size", washCarManagerVariable.size);
        json.put("customer_name", washCarManagerVariable.customerName);
        json.put("customer_vip_type", washCarManagerVariable.customerVipType);
        json.put("wash_start_time", washCarManagerVariable.washStartTime);
        json.put("wash_end_time", washCarManagerVariable.washEndTime);
        json.put("shop_id", washCarManagerVariable.shopId);
        json.put("phone", washCarManagerVariable.phone);
        return invokeApi(url, json);
    }

    /**
     * @description :V2.0洗车管理列表
     * @date :2021/2/1
     **/
    public JSONObject washCarManagerPage(String page, String size, String param, String result) {
        String url = "/car-platform/pc/vip-marketing/wash-car-manager/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (param != null) {
            json.put(param, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :V2.0调整记录次数
     * @date :2021/2/2
     **/
    public JSONObject adjustNumberRecord(String page, String size, String param, String result) {
        String url = "/car-platform/pc/vip-marketing/wash-car-manager/adjust-number/record";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (param != null) {
            json.put(param, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :V2.0调整记录次数
     * @date :2021/2/2
     **/
    public JSONObject adjustNumberRecord(AdjustNumberRecordVariable adjustNumberRecordVariable) {
        String url = "/car-platform/pc/vip-marketing/wash-car-manager/adjust-number/record";
        JSONObject json = new JSONObject();
        json.put("page", adjustNumberRecordVariable.page);
        json.put("size", adjustNumberRecordVariable.size);
        json.put("customer_name", adjustNumberRecordVariable.customerName);
        json.put("customer_phone", adjustNumberRecordVariable.customerPhone);
        json.put("adjust_shop_id", adjustNumberRecordVariable.adjustShopId);
        json.put("adjust_start_time", adjustNumberRecordVariable.adjustStartTime);
        json.put("adjust_end_time", adjustNumberRecordVariable.adjustEndTime);
        json.put("customer_type", adjustNumberRecordVariable.customerType);

        return invokeApi(url, json);
    }

    /**
     * @description :V2.0优惠券领取记录
     * @date :2021/2/2
     **/
    public JSONObject voucherManageSendRecord(VoucherManageSendVariable variable) {
        String url = "/car-platform/pc/voucher-manage/send-record";
        JSONObject json = new JSONObject();
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("id", variable.id);
        json.put("receiver", variable.receiver);
        json.put("receive_phone", variable.receivePhone);
        json.put("use_status", variable.useStatus);
        json.put("start_time", variable.startTime);
        json.put("end_time", variable.endTime);
        json.put("use_start_time", variable.useStartTime);
        json.put("use_end_time", variable.useEndTime);
        json.put("customer_label", variable.customerLabel);

        return invokeApi(url, json);
    }

    /**
     * @description :V2.0优惠券领取记录
     * @date :2021/2/2
     **/
    public JSONObject voucherManageSendRecord(String shopId, String page, String size, String id, String param, String result) {
        String url = "/car-platform/pc/voucher-manage/send-record";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_id", shopId);
        json.put("id", id);
        if (param != null) {
            json.put(param, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :V2.0优惠券领取记录
     * @date :2021/2/2
     **/
    public JSONObject voucherManageSendRecord(String page, String size, String param, String result) {
        String url = "/car-platform/pc/voucher-manage/send-record";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (param != null) {
            json.put(param, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :V2.0优惠券领取记录
     * @date :2021/2/2
     **/
    public JSONObject voucherManageSendRecord(String page, String size, String id, String param, String result) {
        String url = "/car-platform/pc/voucher-manage/additional-record";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("id", id);
        if (param != null) {
            json.put(param, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :V3.0优惠券领取记录
     * @date :2021/2/2
     **/
    public JSONObject voucherManageSendRecord1(String page, String size, String startTime, String endTime) {
        String url = "/car-platform/pc/voucher-manage/send-record";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", startTime);
        json.put("end_time", endTime);

        return invokeApi(url, json);
    }

    /**
     * @description :V2.0优惠券作废记录
     * @date :2021/2/2
     **/
    public JSONObject voucherInvalidPage(VoucherInvalidPageVariable variable) {
        String url = "/car-platform/pc/voucher-manage/voucher-invalid-page";
        JSONObject json = new JSONObject();
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("id", variable.id);
        json.put("receiver", variable.receiver);
        json.put("receive_phone", variable.receivePhone);
        json.put("start_time", variable.startTime);
        json.put("end_time", variable.endTime);
        json.put("invalid_name", variable.invalidName);
        json.put("invalid_phone", variable.invalidPhone);
        json.put("invalid_start_time", variable.invalidStartTime);
        json.put("invalid_end_time", variable.invalidEndTime);

        return invokeApi(url, json);
    }

    /**
     * @description :V2.0优惠券作废记录
     * @date :2021/2/2
     **/
    public JSONObject voucherInvalidPage(String shopId, String page, String size, String id, String param, String result) {
        String url = "/car-platform/pc/voucher-manage/voucher-invalid-page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_id", shopId);
        json.put("id", id);
        if (param != null) {
            json.put(param, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :V2.0优惠券作废记录
     * @date :2021/2/2
     **/
    public JSONObject voucherInvalidPage1(String shopId, String page, String size, String invalidStartTime, String invalidEndTime) {
        String url = "/car-platform/pc/voucher-manage/voucher-invalid-page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_id", shopId);
        json.put("invalid_end_time", invalidEndTime);
        json.put("invalid_start_time", invalidStartTime);

        return invokeApi(url, json);
    }


    /**
     * @description :V2.0优惠券作废记录
     * @date :2021/2/2
     **/
    public JSONObject voucherInvalidPage(String shopId, String page, String size, String param, String result) {
        String url = "/car-platform/pc/voucher-manage/voucher-invalid-page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_id", shopId);
        if (param != null) {
            json.put(param, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :V2.0优惠券作废记录
     * @date :2021/2/2
     **/
    public JSONObject voucherInvalidPage(String page, String size, String id, String startTime, String endTime, String invalidStartTime, String invalidEndTime) {
        String url = "/car-platform/pc/voucher-manage/voucher-invalid-page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("id", id);
        json.put("start_time", startTime);
        json.put("end_time", endTime);
        json.put("invalid_start_time", invalidStartTime);
        json.put("invalid_end_time", invalidEndTime);
        return invokeApi(url, json);
    }

    /**
     * @description :pc道路救援
     * @date :2021/2/2
     **/
    public JSONObject rescuePage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/manage/rescue/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_id", shopId);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :pc道路救援
     * @date :2021/2/2
     **/
    public JSONObject rescuePage1(String shopId, String page, String size, String dialtart, String dialEnd) {
        String url = "/car-platform/pc/manage/rescue/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("dial_end", dialEnd);
        json.put("dial_start", dialtart);

        return invokeApi(url, json);
    }

    /**
     * @description :pc道路救援
     * @date :2021/2/2
     **/
    public JSONObject rescuePage(RescuePageVariable variable) {
        String url = "/car-platform/pc/manage/rescue/page";
        JSONObject json = new JSONObject();
        json.put("customer_name", variable.customerName);
        json.put("vip_type", variable.vipType);
        json.put("customer_phone", variable.customerPhone);
        json.put("shop_id", variable.shopId);
        json.put("dial_start", variable.dialStart);
        json.put("dial_end", variable.dialEnd);

        return invokeApi(url, json);
    }

    /**
     * @description :PC评价列表
     * @date :2021/2/2
     **/
    public JSONObject evaluatePage(String shopId, String page, String size, String evaluateType, String pram, String result) {
        String url = "/car-platform/pc/manage/evaluate/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("evaluate_type", evaluateType);
        json.put("shop_id", shopId);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :PC评价列表
     * @date :2021/2/2
     **/
    public JSONObject evaluatePage(String shopId, String page, String size, String evaluateType, String evaluateStart, String evaluateEnd, String sourceCreateStart, String sourceCreateEnd) {
        String url = "/car-platform/pc/manage/evaluate/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("evaluate_end", evaluateEnd);
        json.put("evaluate_start", evaluateStart);
        json.put("source_create_end", sourceCreateEnd);
        json.put("evaluate_type", evaluateType);
        json.put("source_create_start", sourceCreateStart);

        return invokeApi(url, json);
    }

    /**
     * @description :PC评价列表
     * @date :2021/2/2
     **/
    public JSONObject evaluatePage(EvaluatePageVariable variable) {
        String url = "/car-platform/pc/manage/evaluate/page";
        JSONObject json = new JSONObject();
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("plate_number", variable.plateNumber);
        json.put("service_sale_id", variable.serviceSaleId);
        json.put("evaluate_type", variable.evaluateType);
        json.put("shop_id", variable.shopId);
        json.put("customer_name", variable.customerName);
        json.put("score", variable.score);
        json.put("evaluate_start", variable.evaluateStart);
        json.put("evaluate_end", variable.evaluateEnd);
        json.put("is_follow_up", variable.isFollowUp);
        json.put("customer_phone", variable.customerPhone);
        json.put("source_create_start", variable.sourceCreateStart);
        json.put("source_create_end", variable.sourceCreateEnd);
        return invokeApi(url, json);
    }

    /**
     * @description :PC精品商城-商城套餐
     * @date :2021/2/2
     **/
    public JSONObject storeCommodityPage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/store/commodity/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_id", shopId);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :PC精品商城-商城套餐
     * @date :2021/2/2
     **/
    public JSONObject storeCommodityPage1(String shopId, String page, String size, String startCreateDate, String endCreateDate) {
        String url = "/car-platform/pc/store/commodity/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_id", shopId);
        json.put("end_create_date", endCreateDate);
        json.put("start_create_date", startCreateDate);

        return invokeApi(url, json);
    }

    /**
     * @description :PC精品商城-商城套餐
     * @date :2021/2/2
     **/
    public JSONObject storeCommodityPage(StoreCommodityPageVariable variable) {
        String url = "/car-platform/pc/manage/evaluate/page";
        JSONObject json = new JSONObject();
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("commodity_name", variable.commodityName);
        json.put("start_create_date", variable.startCreateDate);
        json.put("end_create_date", variable.endCreateDate);

        return invokeApi(url, json);
    }

    /**
     * @description :PC精品商城-商城订单
     * @date :2021/2/2
     **/
    public JSONObject storeOrderPage(String shopId, String page, String size, String pram, String result) {
        String url = "/car-platform/pc/store/order/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_id", shopId);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :PC精品商城-商城订单
     * @date :2021/2/2
     **/
    public JSONObject storeOrderPage1(String shopId, String page, String size, String startPayTime, String endPayTime) {
        String url = "/car-platform/pc/store/order/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_id", shopId);
        json.put("start_pay_time", startPayTime);
        json.put("end_pay_time", endPayTime);

        return invokeApi(url, json);
    }


    /**
     * @description :PC精品商城-商城订单
     * @date :2021/2/2
     **/
    public JSONObject storeOrderPage(StoreOrderPageVariable variable) {
        String url = "/car-platform/pc/store/order/page";
        JSONObject json = new JSONObject();
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("bind_phone", variable.bindPhone);
        json.put("commodity_name", variable.commodityName);
        json.put("start_pay_time", variable.startPayTime);
        json.put("end_pay_time", variable.endPayTime);
        json.put("order_number", variable.orderNumber);

        return invokeApi(url, json);
    }

    /**
     * @description :PC精品商城-分销员管理列表
     * @date :2021/2/3
     **/
    public JSONObject storeSalesPage(StoreSalesPageVariable variable) {
        String url = "/car-platform/pc/store/sales/page";
        JSONObject json = new JSONObject();
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("sales_phone", variable.salesPhone);
        json.put("shop_id", variable.shopId);

        return invokeApi(url, json);
    }

    /**
     * @description :PC精品商城-分销员管理列表
     * @date :2021/2/3
     **/
    public JSONObject storeSalesPage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/store/sales/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :PC2.0新建账号
     * @date :2021/2/3
     **/
    public JSONObject addAccount(String name, String phone, Long role_id, String role_name) {
        String url = "/car-platform/pc/staff/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"phone\" :" + phone + ",\n" +
                        "\"role_list\":[{" +
                        "\"role_id\" :" + role_id + ",\n" +
                        "\"role_name\" :\"" + role_name + "\",\n" +
                        "\"shop_list\":[{\"shop_id\":49309,\"shop_name\":\"0209门店-1全称\"}]}]" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 导出列表
     */

    /**
     * @description :PC导出 通用
     * @date :2021/3/2
     **/
    public JSONObject recExport(String url) {
        JSONObject json = new JSONObject();
        json.put("page", 1);
        json.put("size", 10);
        json.put("export_type", "CURRENT_PAGE");
//        if (url.equals("/car-platform/pc/manage/maintain/car-model/export")){
//            json.put("shop_id","46439");
//        }
        return invokeApi(url, json, false);
    }

    /**
     * @description :PC导出 维修记录
     * @date :2021/3/2
     **/
    public JSONObject weixiuExport(String car_id, String shop_id) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/customer-manage/after-sale-customer/repair-page/export";
        json.put("page", 1);
        json.put("size", 10);
        json.put("export_type", "CURRENT_PAGE");
        json.put("car_id", car_id);
        json.put("shop_id", shop_id);
        return invokeApi(url, json, false);
    }

    /**
     * @description :PC导出 优惠券记录
     * @date :2021/3/2
     **/
    public JSONObject vourcherExport(String url, String id) {
        JSONObject json = new JSONObject();
        json.put("page", 1);
        json.put("size", 10);
        json.put("export_type", "CURRENT_PAGE");
        json.put("voucher_id", id);
        return invokeApi(url, json, false);
    }

    /**
     * @description :PC导出 活动报名记录
     * @date :2021/3/2
     **/
    public JSONObject activityExport(String activity_id) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/activity/manage/register/export";
        json.put("page", 1);
        json.put("size", 10);
        json.put("export_type", "CURRENT_PAGE");
        json.put("activity_id", activity_id);
        return invokeApi(url, json, false);
    }

    /**
     * @description :PC导出 车系列表
     * @date :2021/3/2
     **/
    public JSONObject carStyleExport(Long brand_id) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/brand/car-style/export";
        json.put("page", 1);
        json.put("size", 10);
        json.put("export_type", "CURRENT_PAGE");
        json.put("brand_id", brand_id);
        return invokeApi(url, json, false);
    }

    /**
     * @description :PC导出 车型列表
     * @date :2021/3/2
     **/
    public JSONObject carModelExport(Long brand_id, Long style_id) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/brand/car-style/car-model/export";
        json.put("page", 1);
        json.put("size", 10);
        json.put("export_type", "CURRENT_PAGE");
        json.put("brand_id", brand_id);
        json.put("style_id", style_id);
        return invokeApi(url, json, false);
    }

    public JSONObject activityPage(Integer page, Integer size) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/activity/manage/page";
        json.put("page", page);
        json.put("size", size);
        return invokeApi(url, json);
    }

    /**
     * 活动管理
     *
     * @param page
     * @param size
     * @param pram
     * @param result
     * @return
     */
    public JSONObject activityPage(String page, String size, String pram, String result) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/activity/manage/page";
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * 活动报名列表
     *
     * @param page
     * @param size
     * @param pram
     * @param result
     * @return
     */
    public JSONObject registerPage(String activityId, String page, String size, String pram, String result) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/activity/manage/register/page";
        json.put("activity_id", activityId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * 门店列表
     *
     * @return
     */
    public JSONObject shopListPage() {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/login-user/shop-list";

        return invokeApi(url, json);
    }

    /**
     * 销售顾问列表下拉
     *
     * @return
     */
    public JSONObject saleList(Long shop_id, String type) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/sales-list";
        json.put("shop_id", shop_id);
        json.put("type", type);
        return invokeApi(url, json);
    }

    /**
     * 车辆style列表下拉
     *
     * @return
     */
    public JSONObject styleList(Long shop_id) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/style-list";
        json.put("shop_id", shop_id);
        return invokeApi(url, json);
    }

    /**
     * 车辆model列表下拉
     *
     * @return
     */
    public JSONObject modelList(Long style_id) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/model-list";
        json.put("style_id", style_id);
        return invokeApi(url, json);
    }

    /**
     * 创建潜客
     *
     * @return
     */
    public JSONObject createPotentialCstm(String customer_name, String customer_phone, String customer_type, String sex, Long car_style_id,
                                          Long car_model_id, Long shop_id, String salesId, Boolean... chk) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/create-potential-customer";
        json.put("customer_name", customer_name);
        json.put("customer_phone", customer_phone);
        json.put("customer_type", customer_type);
        json.put("sex", sex);
        json.put("car_style_id", car_style_id);
        json.put("car_model_id", car_model_id);
        json.put("shop_id", shop_id);
        json.put("salesId", salesId);
        if (chk.length > 0) {
            return invokeApi(url, json, chk[0]);
        } else {
            return invokeApi(url, json);
        }

    }

    /**
     * 创建订单
     *
     * @return
     */
    public JSONObject createCstm(String customer_name, String customer_phone, String customer_type, String sex, Long car_style_id, Long car_model_id,
                                 Long shop_id, String salesId, String purchase_car_date, String vehicle_chassis_code, Boolean... chk) {
        JSONObject json = new JSONObject();
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/create-customer";
        json.put("customer_name", customer_name);
        json.put("customer_phone", customer_phone);
        json.put("customer_type", customer_type);
        json.put("sex", sex);
        json.put("car_style_id", car_style_id);
        json.put("car_model_id", car_model_id);
        json.put("shop_id", shop_id);
        json.put("salesId", salesId);
        json.put("purchase_car_date", purchase_car_date);
        json.put("vehicle_chassis_code", vehicle_chassis_code);
        if (chk.length > 0) {
            return invokeApi(url, json, chk[0]);
        } else {
            return invokeApi(url, json);
        }
    }

    /**
     * 接待人列表
     */
    public JSONObject authListPage(String authType, String shopId) {
        JSONObject json = new JSONObject();
        json.put("auth_type", authType);
        json.put("shop_id", shopId);
        String url = "/car-platform/pc/staff/auth-list";

        return invokeApi(url, json);
    }

    /**
     * V3.0积分客户管理
     * 2021/3/6
     */
    public JSONObject integralCenterCustomerPage(int page, int size, String customerPhone) {
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("customer_phone", customerPhone);
        String url = "/car-platform/pc/integral-center/customer/page";
        return invokeApi(url, json);
    }

    /**
     * V3.0积分客户管理
     * 2021/3/6
     */
    public JSONObject customerIntegralChangeRecordPage(int page, int size, String customerPhone, String changeStart, String changeEnd) {
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("change_start", changeStart);
        json.put("customer_phone", customerPhone);
        json.put("change_end", changeEnd);
        String url = "/car-platform/pc/integral-center/customer-integral/change-record/page";
        return invokeApi(url, json);
    }

    /**
     * @description :增发记录
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject additionalRecordPage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/voucher-manage/additional-record";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :增发记录
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject additionalRecordTimePage(String page, String size, String addStartTime, String addEndTime) {
        String url = "/car-platform/pc/voucher-manage/additional-record";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("add_start_time", addStartTime);
        json.put("add_end_time", addEndTime);
        return invokeApi(url, json);
    }

    /**
     * @description ：主体类型接口
     * * * @author: gly
     * @date :2020/03/25
     **/
    public JSONObject subjectList() {
        String url = "/car-platform/pc/use-range/subject-list";
        JSONObject json = new JSONObject();

        return invokeApi(url, json);
    }

    /**
     * @description :优惠券状态接口
     * * * @author: gly
     * @date :2020/03/25
     **/
    public JSONObject activityStatus() {
        String url = "/car-platform/pc/enum-map";
        JSONObject json = new JSONObject();

        return invokeApi(url, json);
    }

    /**
     * @description :V3.1在线专家
     * * * @author: gly
     * @date :2020/03/25
     **/
    public JSONObject onlineExpertsPageList(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/consult-management/online-experts-page-list";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :V3.1专属服务顾问
     * * * @author: gly
     * @date :2020/03/25
     **/
    public JSONObject dedicatedServicePageList(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/consult-management/dedicated-service-page-list";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :V3.1销售接待记录
     * * * @author: gly
     * @date :2020/03/25
     **/
    public JSONObject salesReceptionPage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/pre-sales-reception/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :V3.1销售接待记录
     * * * @author: gly
     * @date :2020/03/25
     **/
    public JSONObject salesReceptionPageTime(String page, String size, String reception_start, String reception_end) {
        String url = "/car-platform/pc/pre-sales-reception/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("reception_end", reception_end);
        json.put("reception_start", reception_start);

        return invokeApi(url, json);
    }

    /**
     * @description :V3.1销售客户管理
     * * * @author: gly
     * @date :2020/03/25
     **/
    public JSONObject preSalesReceptionPage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/reception-manage/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :V3.1流失客户管理
     * * * @author: gly
     * @date :2020/03/25
     **/
    public JSONObject lossCustomerPage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/customer-manage/loss-customer/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description:流失管理列表-导入时间筛选
     * @author: gly
     * @time: 2020-12/16
     */
    public JSONObject lossCustomerCreateTimeManage(String shop_id, String page, String size, String create_start_time, String create_end_time) {
        String url = "/car-platform/pc/customer-manage/after-sale-customer/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("create_start_time", create_start_time);
        json1.put("create_end_time", create_end_time);

        return invokeApi(url, json1);
    }

    /**
     * @description:流失管理列表-开单时间筛选
     * @author: gly
     * @time: 2020-12/16
     */
    public JSONObject lossCustomerOrderTimeManage(String shop_id, String page, String size, String order_start_time, String order_end_time) {
        String url = "/car-platform/pc/customer-manage/loss-customer/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("order_start_time", order_start_time);
        json1.put("order_end_time", order_end_time);
        return invokeApi(url, json1);
    }

    /**
     * @description:流失管理列表-购车时间筛选
     * @author: gly
     * @time: 2020-12/16
     */
    public JSONObject lossCustomerBuyTimeManage(String shop_id, String page, String size, String buyCarTimeStart, String buyCarTimeEnd) {
        String url = "/car-platform/pc/customer-manage/loss-customer/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("buy_car_time_end", buyCarTimeEnd);
        json1.put("buy_car_time_start", buyCarTimeStart);
        return invokeApi(url, json1);
    }

    /**
     * @description: V3.1售后管理列表-时间筛选
     * @author: gly
     * @time: 2021/3/26
     */
    public JSONObject lossCustomerTimeManage(String page, String size, String create_start_time, String create_end_time, String order_start_time, String order_end_time) {
        String url = "/car-platform/pc/customer-manage/loss-customer/page";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("create_start_time", create_start_time);
        json1.put("create_end_time", create_end_time);
        json1.put("order_start_time", order_start_time);
        json1.put("order_end_time", order_end_time);
        return invokeApi(url, json1);
    }

    /**
     * @description: V3.1专属服务顾问列表-时间筛选
     * @author: gly
     * @time: 2021/3/26
     */
    public JSONObject lossCustomerTimeManage(String page, String size, String consult_date_start, String consult_date_end) {
        String url = "/car-platform/pc/consult-management/dedicated-service-page-list";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("consult_date_start", consult_date_start);
        json1.put("consult_date_end", consult_date_end);
        return invokeApi(url, json1);
    }

    /**
     * @description: V3.1在线专家表-时间筛选
     * @author: gly
     * @time: 2021/3/26
     */
    public JSONObject onlineExpertsPageListTimeManage(String page, String size, String consult_date_start, String consult_date_end) {
        String url = "/car-platform/pc/consult-management/online-experts-page-list";
        JSONObject json1 = new JSONObject();
        json1.put("page", page);
        json1.put("size", size);
        json1.put("consult_date_start", consult_date_start);
        json1.put("consult_date_end", consult_date_end);
        return invokeApi(url, json1);
    }

    /**
     * @description: V3.1登录日志
     * @author: gly
     * @time: 2021/3/26
     */
    public JSONObject loginLogStaffTimeManage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/record/login-record/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description: V3.1登录日志-时间筛选
     * @author: gly
     * @time: 2021/3/26
     */
    public JSONObject loginLogStaffTimeManageTime(String page, String size, String start_date, String end_date) {
        String url = "/car-platform/pc/record/login-record/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("start_date", start_date);
        json.put("end_date", end_date);
        return invokeApi(url, json);
    }

    /**
     * 1.2. app完成评价跟进（谢）（2020-12-15） (不用)的接口
     *
     * @date 2021-04-07 17:50:29
     * Long id : 跟进任务id  是否必填 true  版本 v2.0
     * Long shopId : 跟进任务所属门店id  是否必填 true  版本 v2.0
     * String remark : 跟进备注  是否必填 true  版本 v2.0
     */

    public JSONObject AppCompleteScene(Long id, Long shopId, String remark) {
        String url = "/car-platform/m-app/follow-up/complete";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);
        json.put("remark", remark);

        return invokeApi(url, json);
    }

    /**
     * 1.3. app跟进列表（池）V3（2020-03-09）的接口
     *
     * @date 2021-04-07 17:50:29
     * Integer size : 页大小 范围为[1,100]  是否必填 true  版本 v1.0
     * JSONObject lastValue : 上次请求最后值  是否必填 false  版本 v1.0
     * String type : 跟进类型  是否必填 false  版本 v3.0
     */

    public JSONObject AppPageV3Scene(Integer size, JSONObject lastValue, String type) {
        String url = "/car-platform/m-app/follow-up/page-v3";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("last_value", lastValue);
        json.put("type", type);

        return invokeApi(url, json);
    }

    /**
     * 1.4. app 跟进列表回复 v3 (池)(2020-03-09)的接口
     *
     * @date 2021-04-07 17:50:30
     * Long followId : 跟进id  是否必填 true  版本 v3.0
     * String content : 回复内容  是否必填 true  版本 v3.0
     */

    public JSONObject AppReplyV3Scene(Long followId, String content) {
        String url = "/car-platform/m-app/follow-up/reply-v3";
        JSONObject json = new JSONObject();
        json.put("follow_id", followId);
        json.put("content", content);

        return invokeApi(url, json);
    }

    /**
     * 1.5. app 跟进列表备注 v3 (池)(2020-03-11)的接口
     *
     * @date 2021-04-07 17:50:30
     * Long followId : 跟进id  是否必填 true  版本 v3.0
     * String remark : 备注内容  是否必填 true  版本 v3.0
     */

    public JSONObject AppRemarkV3Scene(Long followId, String remark) {
        String url = "/car-platform/m-app/follow-up/remark-v3";
        JSONObject json = new JSONObject();
        json.put("follow_id", followId);
        json.put("remark", remark);

        return invokeApi(url, json);
    }

    /**
     * 3.4. 读取代办 (谢) （2021-01-22）的接口
     *
     * @date 2021-04-07 17:50:30
     * String type : 代办类型类型APPOINTMENT(预约)、RECEPTION(接待)、FOLLOW_UP(跟进)  是否必填 true  版本 1.0
     * Boolean isAllDataAuth : No comments found.  是否必填 false  版本 -
     */

    public JSONObject AppWaitingTaskReadScene(String type, Boolean isAllDataAuth) {
        String url = "/car-platform/m-app/home-page/waiting-task/read";
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("isAllDataAuth", isAllDataAuth);

        return invokeApi(url, json);
    }

    /**
     * 5.1. 查询手机号客户信息（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:30
     * String phone : 手机号  是否必填 true  版本 v1.0
     */

    public JSONObject AppAdmitScene(String phone) {
        String url = "/car-platform/m-app/pre-sales-reception/admit";
        JSONObject json = new JSONObject();
        json.put("phone", phone);

        return invokeApi(url, json);
    }

    /**
     * 5.3. 完成接待（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:30
     * Long id : 接待id  是否必填 true  版本 v1.0
     * Long shopId : 接待门店id  是否必填 true  版本 v1.0
     */

    public JSONObject AppFinishReceptionScene(Long id, Long shopId) {
        String url = "/car-platform/m-app/pre-sales-reception/finish-reception";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 5.4. 取消接待（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:30
     * Long id : 接待id  是否必填 true  版本 v1.0
     * Long shopId : 接待门店id  是否必填 true  版本 v1.0
     */

    public JSONObject AppCancelReceptionScene(Long id, Long shopId) {
        String url = "/car-platform/m-app/pre-sales-reception/cancel-reception";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shopId", shopId);

        return invokeApi(url, json);
    }

    /**
     * 5.5. app接待分页（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:30
     * Integer size : 页大小 范围为[1,100]  是否必填 true  版本 v1.0
     * JSONObject lastValue : 上次请求最后值  是否必填 false  版本 v1.0
     */

    public JSONObject AppPageScene(Integer size, JSONObject lastValue) {
        String url = "/car-platform/m-app/pre-sales-reception/page";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("last_value", lastValue);

        return invokeApi(url, json);
    }

    /**
     * 5.6. 销售接待员工列表（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:30
     * Long shopId : 门店id  是否必填 true  版本 v2.0
     */

    public JSONObject AppReceptorListScene(Long shopId) {
        String url = "/car-platform/m-app/pre-sales-reception/receptor/list";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 5.7. 变更销售接待（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:31
     * Long id : 接待id  是否必填 true  版本 v2.0
     * Long shopId : 门店id  是否必填 true  版本 v2.0
     * String receptorId : 售后接待员工id  是否必填 true  版本 v2.0
     */

    public JSONObject AppReceptorChangeScene(Long id, Long shopId, String receptorId) {
        String url = "/car-platform/m-app/pre-sales-reception/receptor/change";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);
        json.put("receptor_id", receptorId);

        return invokeApi(url, json);
    }

    /**
     * 5.9. 客户备注（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:31
     * Long id : 接待id  是否必填 true  版本 v3.0
     * Long shopId : 门店id  是否必填 true  版本 v3.0
     * String remark : 客户备注信息  是否必填 true  版本 v3.0
     */

    public JSONObject AppCustomerRemarkScene(Long id, Long shopId, String remark) {
        String url = "/car-platform/m-app/pre-sales-reception/customer/remark";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);
        json.put("remark", remark);

        return invokeApi(url, json);
    }

    /**
     * 5.10. 客户备注（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:31
     * Long id : 接待id  是否必填 true  版本 v3.0
     */

    public JSONObject AppCustomerDetailScene(Long id) {
        String url = "/car-platform/m-app/pre-sales-reception/customer/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 5.12. 客户备注（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:31
     * Long id : 购车记录id  是否必填 true  版本 v3.0
     * Long carModel : 购买车辆车型  是否必填 true  版本 v3.0
     * String vin : 车辆底盘号  是否必填 false  版本 v3.0
     */

    public JSONObject AppVehicleEditScene(Long id, Long carModel, String vin) {
        String url = "/car-platform/m-app/pre-sales-reception/vehicle/edit";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("car_model", carModel);
        json.put("vin", vin);

        return invokeApi(url, json);
    }

    /**
     * 6.3. app变更预约时间段（谢）的接口
     *
     * @date 2021-04-07 17:50:31
     * Long id : 预约记录id  是否必填 true  版本 v1.0
     * Long shopId : 预约门店id  是否必填 true  版本 v1.0
     * Long timeId : 预约时间段id  是否必填 true  版本 v1.0
     */

    public JSONObject AppAppointmentModifyTimeScene(Long id, Long shopId, Long timeId) {
        String url = "/car-platform/m-app/task/appointment/modify-time";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);
        json.put("time_id", timeId);

        return invokeApi(url, json);
    }

    /**
     * 6.4. app获取可变更预约日期时间段列表（谢）v3.0（2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:31
     * String type : 预约类型 取值见字典表《预约类型》v3.0  是否必填 true  版本 v2.0
     * Long shopId : 预约门店id  是否必填 true  版本 v1.0
     */

    public JSONObject AppAppointmentMaintainTimeListScene(String type, Long shopId) {
        String url = "/car-platform/m-app/task/appointment/maintain/time/list";
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 6.5. app接待预约（谢）（2020-12-15）的接口
     *
     * @date 2021-04-07 17:50:31
     * Long id : 预约id  是否必填 true  版本 v2.0
     */

    public JSONObject AppAppointmentReceptionScene(Long id) {
        String url = "/car-platform/m-app/task/appointment/reception";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 7.2. 获取指定枚举值列表（谢）（2021-02-19）的接口
     *
     * @date 2021-04-07 17:50:31
     * String enumType : 枚举类型  是否必填 true  版本 v2.0
     */

    public JSONObject AppEnumValueListScene(String enumType) {
        String url = "/car-platform/m-app/enum-value-list";
        JSONObject json = new JSONObject();
        json.put("enum_type", enumType);

        return invokeApi(url, json);
    }

    /**
     * 2.3. 我的卡券详情 (张小龙) v1.0的接口
     *
     * @date 2021-04-07 17:50:32
     * Long id : 卡券id  是否必填 true  版本 v2.0
     */

    public JSONObject AppletVoucherDetailScene(Long id) {
        String url = "/car-platform/applet/granted/voucher/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 4.3. 小程序-首页-文章列表更多-分页（谢）(2021-03-08)的接口
     *
     * @date 2021-04-07 17:50:32
     * Integer size : 页大小 范围为[1,100]  是否必填 true  版本 v1.0
     * JSONObject lastValue : 上次请求最后值  是否必填 false  版本 v1.0
     */

    public JSONObject AppletPageScene(Integer size, JSONObject lastValue) {
        String url = "/car-platform/applet/article/page";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("last_value", lastValue);

        return invokeApi(url, json);
    }

    /**
     * 4.5. 小程序门店列表（谢）(2021-01-04)的接口
     *
     * @date 2021-04-07 17:50:32
     * JSONArray coordinate : 客户当前位置经纬度 [纬度,经度]  是否必填 false  版本 v2.0
     * String washingStatus : No comments found.  是否必填 false  版本 -
     */

    public JSONObject AppletShopListScene(JSONArray coordinate, String washingStatus) {
        String url = "/car-platform/applet/shop-list";
        JSONObject json = new JSONObject();
        json.put("coordinate", coordinate);
        json.put("washingStatus", washingStatus);

        return invokeApi(url, json);
    }

    /**
     * 5.2. 获取指定通用规则说明的接口
     *
     * @date 2021-04-07 17:50:32
     * String businessType : 业务类型  是否必填 true  版本 v2.0
     */

    public JSONObject AppletCommonRuleExplainDetailScene(String businessType) {
        String url = "/car-platform/applet/granted/common/rule-explain-detail";
        JSONObject json = new JSONObject();
        json.put("business_type", businessType);

        return invokeApi(url, json);
    }

    /**
     * 5.4. 获取指定枚举值列表（谢）（2021-02-19）的接口
     *
     * @date 2021-04-07 17:50:32
     * String enumType : 枚举类型  是否必填 true  版本 v2.0
     */

    public JSONObject AppletCommonEnumValueListScene(String enumType) {
        String url = "/car-platform/applet/granted/common/enum-value-list";
        JSONObject json = new JSONObject();
        json.put("enum_type", enumType);

        return invokeApi(url, json);
    }

    /**
     * 5.5. 获取门店id（池）(下拉)（2021-03-23）的接口
     *
     * @date 2021-04-07 17:50:32
     * Long carModelId : 车型id  是否必填 false  版本 v3.0
     */

    public JSONObject AppletCommonShopListScene(Long carModelId) {
        String url = "/car-platform/applet/granted/common/shop-list";
        JSONObject json = new JSONObject();
        json.put("car_model_id", carModelId);

        return invokeApi(url, json);
    }

    /**
     * 6.1. 获取banner通用（池）（2021-03-15）的接口
     *
     * @date 2021-04-07 17:50:33
     * String type : type banner类型 ONLINE_EXPERTS 在线专家 RENEW_CONSULT 续保咨询 USED_CAR 二手车 USED_CAR_ASSESS 二手车评估  是否必填 false  版本 v3.0
     */

    public JSONObject AppletConsultBannerScene(String type) {
        String url = "/car-platform/applet/granted/consult/banner";
        JSONObject json = new JSONObject();
        json.put("type", type);

        return invokeApi(url, json);
    }

    /**
     * 6.8. 服务顾问分享海报 （池） （2021-03-12）的接口
     *
     * @date 2021-04-07 17:50:33
     * String salesId : 服务顾问id  是否必填 true  版本 v3.0
     * Long shopId : 门店id  是否必填 true  版本 v3.0
     */

    public JSONObject AppletConsultSalesPosterScene(String salesId, Long shopId) {
        String url = "/car-platform/applet/granted/consult/sales-poster";
        JSONObject json = new JSONObject();
        json.put("sales_id", salesId);
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 7.2. 预约员工列表 （谢）v3.0（2021-03-12）的接口
     *
     * @date 2021-04-07 17:50:33
     * String type : 预约类型 详见字典表《预约类型》v3.0（2021-03-12）  是否必填 true  版本 v2.0
     * Long shopId : 预约门店id  是否必填 true  版本 v1.0
     */

    public JSONObject AppletAppointmentStaffListScene(String type, Long shopId) {
        String url = "/car-platform/applet/granted/appointment/staff/list";
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 7.7. 删除预约 （谢）v3.0（2021-03-12）的接口
     *
     * @date 2021-04-07 17:50:33
     * String type : 预约类型 详见字典表《预约类型》v3.0（2021-03-12）  是否必填 true  版本 v2.0
     * Long id : 预约记录id  是否必填 true  版本 v1.0
     */

    public JSONObject AppletAppointmentDeleteScene(String type, Long id) {
        String url = "/car-platform/applet/granted/appointment/delete";
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 10.1. 获取评价配置 （谢）v3.0 （2021-03-12）的接口
     *
     * @date 2021-04-07 17:50:33
     * Integer type : 评价类型 详见字典表《评价类型》 2021-03-12  是否必填 true  版本 v2.0
     * Long shopId : 评价所属门店  是否必填 true  版本 v2.0
     */

    public JSONObject AppletEvaluateConfigScene(Integer type, Long shopId) {
        String url = "/car-platform/applet/granted/evaluate/config";
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 10.3. 我的评价分页 （谢）v3.0 （2021-04-06）的接口
     *
     * @date 2021-04-07 17:50:33
     * Integer size : 页大小 范围为[1,100]  是否必填 true  版本 v1.0
     * JSONObject lastValue : 上次请求最后值  是否必填 false  版本 v1.0
     * Integer type : 评价类型 详见字典表《评价类型》 2021-03-12  是否必填 true  版本 v2.0
     */

    public JSONObject AppletEvaluatePageScene(Integer size, JSONObject lastValue, Integer type) {
        String url = "/car-platform/applet/granted/evaluate/page";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("last_value", lastValue);
        json.put("type", type);

        return invokeApi(url, json);
    }

    /**
     * 13.2. 二手车商品详情（华成裕）的接口
     *
     * @date 2021-04-07 17:50:33
     * Long id : 商品ID  是否必填 false  版本 v5.3
     */

    public JSONObject usedDetailScene(Long id) {
        String url = "/car-platform/used-car/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 14.10. 生成分享二维码 (池) v2.0的接口
     *
     * @date 2021-04-07 17:50:34
     * String businessType : 业务类型 ("0A", "售后维修"), ("04", "签到分享"), ("05", "活动分享"), ("06", "二维码分享"),  是否必填 false  版本 -
     * Long taskId : No comments found.  是否必填 true  版本 -
     */

    public JSONObject AppletMemberCenterShareTaskGeneratePosterScene(String businessType, Long taskId) {
        String url = "/car-platform/applet/granted/member-center/share-task/generate-Poster";
        JSONObject json = new JSONObject();
        json.put("business_type", businessType);
        json.put("taskId", taskId);

        return invokeApi(url, json);
    }

    /**
     * 15.1. 微信小程序登录（谢）的接口
     *
     * @date 2021-04-07 17:50:34
     * String code : 小程序登录授权code  是否必填 true  版本 v1.0
     */

    public JSONObject AppletAppletLoginScene(String code) {
        String url = "/account-platform/applet-login";
        JSONObject json = new JSONObject();
        json.put("code", code);

        return invokeApi(url, json);
    }

    /**
     * 15.2. 获取小程序验证码（谢）的接口
     *
     * @date 2021-04-07 17:50:34
     * String phone : 手机号  是否必填 true  版本 v1.0
     * String type : 验证码类型 默认为LOGIN  是否必填 true  版本 v1.0
     */

    public JSONObject AppletVerificationCodeScene(String phone, String type) {
        String url = "/car-platform/applet/verification-code";
        JSONObject json = new JSONObject();
        json.put("phone", phone);
        json.put("type", type);

        return invokeApi(url, json);
    }

    /**
     * 15.5. 授权用户信息（谢）的接口
     *
     * @date 2021-04-07 17:50:34
     * String encryptedData : 授权的加密数据  是否必填 true  版本 v1.0
     * String iv : 加密向量  是否必填 true  版本 v1.0
     */

    public JSONObject AppletUserDetailScene(String encryptedData, String iv) {
        String url = "/car-platform/applet/grant/user-detail";
        JSONObject json = new JSONObject();
        json.put("encrypted_data", encryptedData);
        json.put("iv", iv);

        return invokeApi(url, json);
    }

    /**
     * 2.2. 权益修改 (池) v2.0的接口
     *
     * @date 2021-04-07 17:50:35
     * Long equityId : 权益id  是否必填 true  版本 v2.0
     * Integer awardCount : 奖励数  是否必填 true  版本 v2.0
     * String description : 权益说明  是否必填 true  版本 v2.0
     */

    public JSONObject EquityEditScene(Long equityId, Integer awardCount, String description) {
        String url = "/car-platform/pc/vip-marketing/equity/edit";
        JSONObject json = new JSONObject();
        json.put("equity_id", equityId);
        json.put("award_count", awardCount);
        json.put("description", description);

        return invokeApi(url, json);
    }

    /**
     * 2.3. 权益开启或关闭 (池) v2.0的接口
     *
     * @date 2021-04-07 17:50:35
     * Long equityId : 权益id  是否必填 true  版本 v2.0
     * String equityStatus : 权益状态 ENABLE "开启" DISABLE "关闭"  是否必填 true  版本 v2.0
     */

    public JSONObject EquityStartOrCloseScene(Long equityId, String equityStatus) {
        String url = "/car-platform/pc/vip-marketing/equity/start-or-close";
        JSONObject json = new JSONObject();
        json.put("equity_id", equityId);
        json.put("equity_status", equityStatus);

        return invokeApi(url, json);
    }

    /**
     * 2.4. 编辑洗车规则说明 (池) v2.0的接口
     *
     * @date 2021-04-07 17:50:35
     * String ruleDetail : 规则  是否必填 true  版本 1.2
     */

    public JSONObject WashCarManagerEditRuleScene(String ruleDetail) {
        String url = "/car-platform/pc/vip-marketing/wash-car-manager/edit-rule";
        JSONObject json = new JSONObject();
        json.put("rule_detail", ruleDetail);

        return invokeApi(url, json);
    }

    /**
     * 2.5. 编辑洗车权益说明 (池) v2.0的接口
     *
     * @date 2021-04-07 17:50:35
     * String equityDetail : 洗车权益说明  是否必填 true  版本 v2.0
     */

    public JSONObject WashCarManagerEditEquityScene(String equityDetail) {
        String url = "/car-platform/pc/vip-marketing/wash-car-manager/edit-equity";
        JSONObject json = new JSONObject();
        json.put("equity_detail", equityDetail);

        return invokeApi(url, json);
    }

    /**
     * 2.13. 签到配置列表 (池) v2.0的接口
     *
     * @date 2021-04-07 17:50:35
     * Integer page : 页码 大于0  是否必填 true  版本 v1.0
     * Integer size : 页大小 范围为[1,100]  是否必填 true  版本 v1.0
     */

    public JSONObject SignInConfigPageScene(Integer page, Integer size) {
        String url = "/car-platform/pc/vip-marketing/sign_in_config/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);

        return invokeApi(url, json);
    }

    /**
     * 2.14. 签到开启或关闭的接口
     *
     * @date 2021-04-07 17:50:35
     * String status : 状态  是否必填 true  版本 v2.0
     * Long id : id  是否必填 true  版本 v2.0
     */

    public JSONObject SignInConfigStartOrCloseScene(String status, Long id) {
        String url = "/car-platform/pc/vip-marketing/sign_in_config/start-or-close";
        JSONObject json = new JSONObject();
        json.put("status", status);
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 2.16. 签到积分变更记录 (池) v2.0的接口
     *
     * @date 2021-04-07 17:50:35
     * Integer page : 页码 大于0  是否必填 true  版本 v1.0
     * Integer size : 页大小 范围为[1,100]  是否必填 true  版本 v1.0
     * Long signInConfigId : 签到id  是否必填 false  版本 v2.0
     */

    public JSONObject SignInConfigChangeRecordScene(Integer page, Integer size, Long signInConfigId) {
        String url = "/car-platform/pc/vip-marketing/sign_in_config/change-record";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("sign_in_config_id", signInConfigId);

        return invokeApi(url, json);
    }

    /**
     * 2.20. 分享管理(修改前获取根据任务id查询) (池) v2.0的接口
     *
     * @date 2021-04-07 17:50:35
     * Long taskId : No comments found.  是否必填 false  版本 -
     */

    public JSONObject ShareManagerEditPreScene(Long taskId) {
        String url = "/car-platform/pc/vip-marketing/share-manager/edit-pre";
        JSONObject json = new JSONObject();
        json.put("task_id", taskId);

        return invokeApi(url, json);
    }

    /**
     * 2.21. 分享管理任务开启或关闭(池) v2.0的接口
     *
     * @date 2021-04-07 17:50:35
     * Long id : 任务id  是否必填 true  版本 v2.0
     * String status : 状态  是否必填 true  版本 v2.0
     */

    public JSONObject ShareManagerStartOrCloseScene(Long id, String status) {
        String url = "/car-platform/pc/vip-marketing/share-manager/start-or-close";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("status", status);

        return invokeApi(url, json);
    }

    /**
     * 2.22. 分享说明(池) v2.0的接口
     *
     * @date 2021-04-07 17:50:36
     * String content : 内容  是否必填 false  版本 v2.0
     */

    public JSONObject ShareManagerExplainEditScene(String content) {
        String url = "/car-platform/pc/vip-marketing/share-manager/explain_edit";
        JSONObject json = new JSONObject();
        json.put("content", content);

        return invokeApi(url, json);
    }

    /**
     * 3.2. 预约配置详情（谢）v3.0（2021-03-22）的接口
     *
     * @date 2021-04-07 17:50:36
     * String type : 预约类型 取值见字典表《预约类型》 v3.0增加试驾  是否必填 true  版本 v2.0
     */

    public JSONObject AppointmentMaintainConfigDetailScene(String type) {
        String url = "/car-platform/pc/manage/appointment/maintain-config/detail";
        JSONObject json = new JSONObject();
        json.put("type", type);

        return invokeApi(url, json);
    }

    /**
     * 4.7. 添加登陆日志（后端接口，前端无需处理）的接口
     *
     * @date 2021-04-07 17:50:36
     * String saleId : No comments found.  是否必填 true  版本 -
     * Long loginTime : No comments found.  是否必填 true  版本 -
     */

    public JSONObject LoginRecordAddScene(String saleId, Long loginTime) {
        String url = "/car-platform/pc/record/login-record/add";
        JSONObject json = new JSONObject();
        json.put("sale_id", saleId);
        json.put("login_time", loginTime);

        return invokeApi(url, json);
    }

    /**
     * 5.2. 卡券推广 （张小龙） v2.0的接口
     *
     * @date 2021-04-07 17:50:36
     * Long id : 卡券id  是否必填 true  版本 v2.0
     */

    public JSONObject ExtensionPageScene(Long id) {
        String url = "/car-platform/pc/voucher-manage/extension-page";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 5.3. 卡券撤回 （张小龙） v2.0的接口
     *
     * @date 2021-04-07 17:50:36
     * Long id : 唯一id  是否必填 true  版本 v2.0
     */

    public JSONObject RecallVoucherScene(Long id) {
        String url = "/car-platform/pc/voucher-manage/recall-voucher";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 5.5. 增发撤回 （张小龙） v2.0的接口
     *
     * @date 2021-04-07 17:50:36
     * Long id : 卡券id  是否必填 true  版本 v2.0
     */

    public JSONObject RecallAdditionalScene(Long id) {
        String url = "/car-platform/pc/voucher-manage/recall-additional";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 5.7. 变更记录 （张小龙） v2.0的接口
     *
     * @date 2021-04-07 17:50:36
     * Integer page : 页码 大于0  是否必填 true  版本 v1.0
     * Integer size : 页大小 范围为[1,100]  是否必填 true  版本 v1.0
     * Long voucherId : 卡券id  是否必填 true  版本 v2.0
     */

    public JSONObject ChangeRecordScene(Integer page, Integer size, Long voucherId) {
        String url = "/car-platform/pc/voucher-manage/change-record";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("voucher_id", voucherId);

        return invokeApi(url, json);
    }

    /**
     * 5.10. 卡券详情 （张小龙） v2.0的接口
     *
     * @date 2021-04-07 17:50:36
     * Long id : 卡券id  是否必填 true  版本 v2.0
     */

    public JSONObject VoucherDetailScene(Long id) {
        String url = "/car-platform/pc/voucher-manage/voucher-detail";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 5.12. 删除卡券 （张小龙） v2.0的接口
     *
     * @date 2021-04-07 17:50:36
     * Long id : 卡券id  是否必填 true  版本 v2.0
     */

    public JSONObject DeleteVoucherScene(Long id) {
        String url = "/car-platform/pc/voucher-manage/delete-voucher";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 5.14. 开始/结束发放 （张小龙） v2.0的接口
     *
     * @date 2021-04-07 17:50:36
     * Long id : 卡券id  是否必填 true  版本 v2.0
     * Boolean isStart : 是否开始发放  是否必填 true  版本 v2.0
     */

    public JSONObject ChangeProvideStatusScene(Long id, Boolean isStart) {
        String url = "/car-platform/pc/voucher-manage/change-provide-status";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("is_start", isStart);

        return invokeApi(url, json);
    }

    /**
     * 5.17. 作废客户卡券 （张小龙） v2.0的接口
     *
     * @date 2021-04-07 17:50:36
     * Long id : 领取记录id  是否必填 false  版本 v2.0
     * String invalidReason : 做给原因  是否必填 false  版本 v2.0
     */

    public JSONObject InvalidCustomerVoucherScene(Long id, String invalidReason) {
        String url = "/car-platform/pc/voucher-manage/invalid-customer-voucher";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("invalid_reason", invalidReason);

        return invokeApi(url, json);
    }

    /**
     * 5.20. 卡券表头展示信息查询 （张小龙） v2.0的接口
     *
     * @date 2021-04-07 17:50:36
     * Long id : 卡券id  是否必填 true  版本 v2.0
     */

    public JSONObject VoucherInfoScene(Long id) {
        String url = "/car-platform/pc/voucher-manage/voucher-info";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 6.4. 卡券批量审批 （张小龙） v3.0的接口
     *
     * @date 2021-04-07 17:50:36
     * JSONArray ids : 批量审批记录id  是否必填 true  版本 v3.0
     * Integer status : 状态 1 通过，2 拒绝  是否必填 true  版本 v1.0
     * String reason : 审批未通过原因  是否必填 false  版本 v3.0
     */

    public JSONObject ApplyBatchApprovalScene(JSONArray ids, Integer status, String reason) {
        String url = "/car-platform/pc/voucher/apply/batch-approval";
        JSONObject json = new JSONObject();
        json.put("ids", ids);
        json.put("status", status);
        json.put("reason", reason);

        return invokeApi(url, json);
    }

    /**
     * 7.2. 导入模板下载的接口
     *
     * @date 2021-04-07 17:50:36
     * String type : 模板类型 AFTER_CUSTOMER 售后工单 v1.0 POTENTIAL 潜客模板 v2.0 BUY_CAR 购车模板v3.0  是否必填 true  版本 v1.0
     */

    public JSONObject TemplateScene(String type) {
        String url = "/car-platform/pc/import/template";
        JSONObject json = new JSONObject();
        json.put("type", type);

        return invokeApi(url, json);
    }

    /**
     * 8.3. 响应规则详情（池）（2021-03-15）通用的接口
     *
     * @date 2021-04-07 17:50:37
     * String businessType : 咨询业务类型  是否必填 true  版本 v3.0
     */

    public JSONObject ResponseRuleDetailScene(String businessType) {
        String url = "/car-platform/pc/consult-management/response-rule-detail";
        JSONObject json = new JSONObject();
        json.put("business_type", businessType);

        return invokeApi(url, json);
    }

    /**
     * 8.9. 回复（通用）（池）(2021-03-12)的接口
     *
     * @date 2021-04-07 17:50:37
     * Long id : id  是否必填 true  版本 -
     * String content : 回复内容  是否必填 true  版本 v3.0
     */

    public JSONObject ReplyScene(Long id, String content) {
        String url = "/car-platform/pc/consult-management/reply";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("content", content);

        return invokeApi(url, json);
    }

    /**
     * 8.10. 备注（通用）（池）(2021-03-12)的接口
     *
     * @date 2021-04-07 17:50:37
     * Long id : 唯一id  是否必填 true  版本 v3.0
     * String remarkContent : 备注内容  是否必填 true  版本 v3.0
     */

    public JSONObject RemarkScene(Long id, String remarkContent) {
        String url = "/car-platform/pc/consult-management/remark";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("remark_content", remarkContent);

        return invokeApi(url, json);
    }

    /**
     * 8.11. 专属服务说明配置（池）（2021-03-08）的接口
     *
     * @date 2021-04-07 17:50:37
     * String content : No comments found.  是否必填 true  版本 -
     */

    public JSONObject DedicatedServiceExplainEditScene(String content) {
        String url = "/car-platform/pc/consult-management/dedicated-service/explain-edit";
        JSONObject json = new JSONObject();
        json.put("content", content);

        return invokeApi(url, json);
    }

    /**
     * 8.14. 专属服务说明配置（池）（2021-03-08）的接口
     *
     * @date 2021-04-07 17:50:37
     * String content : No comments found.  是否必填 true  版本 -
     */

    public JSONObject OnlineExpertsExplainEditScene(String content) {
        String url = "/car-platform/pc/consult-management/online-experts/explain-edit";
        JSONObject json = new JSONObject();
        json.put("content", content);

        return invokeApi(url, json);
    }

    /**
     * 9.6. 试驾车系配置更新（谢）V3.0（2020-12-18）的接口
     *
     * @date 2021-04-07 17:50:37
     * Long id : 车系id  是否必填 true  版本 v3.0
     * String status : 预约状态 ENABLE：开启，DISABLE：关闭  是否必填 true  版本 v3.0
     */

    public JSONObject ManageStyleEditScene(Long id, String status) {
        String url = "/car-platform/pc/shop-style-model/manage/style/edit";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("status", status);

        return invokeApi(url, json);
    }

    /**
     * 11.10. 售前客户详情(杨)v3.0的接口
     *
     * @date 2021-04-07 17:50:37
     * Long shopId : 门店Id  是否必填 true  版本 v3.0
     * Long customerId : 客户Id  是否必填 true  版本 v3.0
     */

    public JSONObject PreSaleCustomerInfoScene(Long shopId, Long customerId) {
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/info";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("customerId", customerId);

        return invokeApi(url, json);
    }

    /**
     * 11.12. 售前变更所属销售 (杨)v3.0的接口
     *
     * @date 2021-04-07 17:50:37
     * Long shopId : 门店Id  是否必填 true  版本 v3.0
     * Long customerId : 客户Id  是否必填 true  版本 v3.0
     * String saleId : 客户Id  是否必填 true  版本 v3.0
     */

    public JSONObject PreSaleCustomerChangeBelongSaleScene(Long shopId, Long customerId, String saleId) {
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/change-belong-sale";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("customer_id", customerId);
        json.put("sale_id", saleId);

        return invokeApi(url, json);
    }

    /**
     * 11.17. 售后客户详情(杨)v3.0的接口
     *
     * @date 2021-04-07 17:50:37
     * Long carId : 车辆Id  是否必填 true  版本 v3.0
     * Long shopId : 门店Id  是否必填 true  版本 v3.0
     * Long customerId : 客户Id  是否必填 true  版本 v3.0
     */

    public JSONObject AfterSaleCustomerInfoScene(Long carId, Long shopId, Long customerId) {
        String url = "/car-platform/pc/customer-manage/after-sale-customer/info";
        JSONObject json = new JSONObject();
        json.put("car_id", carId);
        json.put("shop_id", shopId);
        json.put("customer_id", customerId);

        return invokeApi(url, json);
    }

    /**
     * 12.3. 消息发送客户总数 （谢） （2020-12-18）的接口
     *
     * @date 2021-04-07 17:50:37
     * Integer pushTarget : 推送目标 0：全部客户，1：门店客户，2：个人客户  是否必填 true  版本 v2.0
     * JSONArray shopList : 门店列表 推送目标为门店客户时必填  是否必填 false  版本 v1.0
     * JSONArray telList : 客户手机号列表 推送目标个人客户时时必填  是否必填 false  版本 v1.0
     */

    public JSONObject GroupTotalScene(Integer pushTarget, JSONArray shopList, JSONArray telList) {
        String url = "/car-platform/pc/message-manage/group-total";
        JSONObject json = new JSONObject();
        json.put("push_target", pushTarget);
        json.put("shop_list", shopList);
        json.put("tel_list", telList);

        return invokeApi(url, json);
    }

    /**
     * 14.5. 接待预约（谢）（2020-12-15）的接口
     *
     * @date 2021-04-07 17:50:38
     * Long id : 预约id  是否必填 true  版本 v2.0
     * String type : 预约类型 见字典表《预约类型》  是否必填 true  版本 v3.0
     */

    public JSONObject ReceptionScene(Long id, String type) {
        String url = "/car-platform/pc/appointment-manage/reception";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("type", type);

        return invokeApi(url, json);
    }

    /**
     * 15.4. 套餐审核 v2.0 （张小龙） 2020-01-28的接口
     *
     * @date 2021-04-07 17:50:38
     * Long id : 套餐id  是否必填 false  版本 v2.0
     * String status : 套餐审核状态 AUDITING(0, "审核中"), AGREE(1,"已通过"),REFUSAL(2,"已拒绝"),  是否必填 false  版本 v2.0
     * String auditStatus : 确认购买状态 AGREE(1,"已通过")  是否必填 false  版本 -
     */

    public JSONObject AuditPackageStatusScene(Long id, String status, String auditStatus) {
        String url = "/car-platform/pc/package-manage/audit-package-status";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("status", status);
        json.put("audit_status", auditStatus);

        return invokeApi(url, json);
    }

    /**
     * 15.5. 套餐取消 v2.3（张小龙） 2021-03-17的接口
     *
     * @date 2021-04-07 17:50:38
     * Long id : 唯一id  是否必填 false  版本 -
     */

    public JSONObject CancelPackageScene(Long id) {
        String url = "/car-platform/pc/package-manage/cancel-package";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 15.12. 套餐购买记录取消 （张小龙）（2020-12-25）的接口
     *
     * @date 2021-04-07 17:50:38
     * Long id : 套餐购买记录id  是否必填 true  版本 v2.0
     */

    public JSONObject CancelSoldPackageScene(Long id) {
        String url = "/car-platform/pc/package-manage/cancel-sold-package";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 15.13. 作废客户购买套餐 （张小龙）（2021-03-15）的接口
     *
     * @date 2021-04-07 17:50:38
     * Long id : 唯一id  是否必填 false  版本 v2.2
     * String reason : 作废原因  是否必填 true  版本 v2.2
     */

    public JSONObject InvalidCustomerPackageScene(Long id, String reason) {
        String url = "/car-platform/pc/package-manage/invalid-customer-package";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("reason", reason);

        return invokeApi(url, json);
    }

    /**
     * 17.4. 车辆投保记录 （池）（2021-03-05）的接口
     *
     * @date 2021-04-07 17:50:38
     * Integer page : 页码 大于0  是否必填 true  版本 v1.0
     * Integer size : 页大小 范围为[1,100]  是否必填 true  版本 v1.0
     * Long id : 列表id  是否必填 false  版本 v3.0
     */

    public JSONObject CarInsuranceRecordScene(Integer page, Integer size, Long id) {
        String url = "/car-platform/pc/insurance-management/car-insurance-record";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 17.5. 购买 （池）（2021-03-05）的接口
     *
     * @date 2021-04-07 17:50:38
     * Long id : id  是否必填 false  版本 v3.0
     * Long insuranceCompanyId : 投保公司  是否必填 false  版本 v3.0
     * Double insuranceMoney : 投保金额  是否必填 false  版本 v3.0
     */

    public JSONObject BuyInsuranceScene(Long id, Long insuranceCompanyId, Double insuranceMoney) {
        String url = "/car-platform/pc/insurance-management/buy-insurance";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("insurance_company_id", insuranceCompanyId);
        json.put("insurance_money", insuranceMoney);

        return invokeApi(url, json);
    }

    /**
     * 17.7. 投保公司列表（池）（分页）（2021-03-05）的接口
     *
     * @date 2021-04-07 17:50:38
     * Integer page : 页码 大于0  是否必填 true  版本 v1.0
     * Integer size : 页大小 范围为[1,100]  是否必填 true  版本 v1.0
     */

    public JSONObject InsuranceCompanyPageListScene(Integer page, Integer size) {
        String url = "/car-platform/pc/insurance-management/insurance-company-page-list";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);

        return invokeApi(url, json);
    }

    /**
     * 17.8. 投保公司删除（池）（2021-03-05）的接口
     *
     * @date 2021-04-07 17:50:38
     * Long insuranceCompanyId : 投保公司id  是否必填 false  版本 v3.0
     */

    public JSONObject InsuranceCompanyDeleteScene(Long insuranceCompanyId) {
        String url = "/car-platform/pc/insurance-management/insurance-company-delete";
        JSONObject json = new JSONObject();
        json.put("insurance_company_id", insuranceCompanyId);

        return invokeApi(url, json);
    }

    /**
     * 17.9. 保险公司维护（池）（2021-03-05）的接口
     *
     * @date 2021-04-07 17:50:38
     * Long insuranceCompanyId : 投保公司id  是否必填 false  版本 v3.0
     * String insuranceCompanyName : 投保公司名称  是否必填 false  版本 v3.0
     */

    public JSONObject InsuranceCompanyEditScene(Long insuranceCompanyId, String insuranceCompanyName) {
        String url = "/car-platform/pc/insurance-management/insurance-company-edit";
        JSONObject json = new JSONObject();
        json.put("insurance_company_id", insuranceCompanyId);
        json.put("insurance_company_name", insuranceCompanyName);

        return invokeApi(url, json);
    }

    /**
     * 17.10. 保险公司新增（池）（2021-03-12）的接口
     *
     * @date 2021-04-07 17:50:38
     * String name : No comments found.  是否必填 true  版本 -
     */

    public JSONObject InsuranceCompanyAddScene(String name) {
        String url = "/car-platform/pc/insurance-management/insurance-company-add";
        JSONObject json = new JSONObject();
        json.put("name", name);

        return invokeApi(url, json);
    }

    /**
     * 18.7. 特惠商品删除 v2.0(池)的接口
     *
     * @date 2021-04-07 17:50:38
     * Long id : id  是否必填 false  版本 -
     */

    public JSONObject CommodityDeleteScene(Long id) {
        String url = "/car-platform/pc/store/commodity/delete";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 19.3. 接待详情（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:39
     * Long id : 接待id  是否必填 true  版本 v3.0
     * Long shopId : 门店id  是否必填 true  版本 v3.0
     */

    public JSONObject DetailScene(Long id, Long shopId) {
        String url = "/car-platform/pc/pre-sales-reception/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 19.4. 查询手机号客户信息（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:39
     * String phone : 手机号  是否必填 true  版本 v1.0
     */

    public JSONObject AdmitScene(String phone) {
        String url = "/car-platform/pc/pre-sales-reception/admit";
        JSONObject json = new JSONObject();
        json.put("phone", phone);

        return invokeApi(url, json);
    }

    /**
     * 19.6. 完成接待（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:39
     * Long id : 接待id  是否必填 true  版本 v1.0
     * Long shopId : 接待门店id  是否必填 true  版本 v1.0
     */

    public JSONObject FinishReceptionScene(Long id, Long shopId) {
        String url = "/car-platform/pc/pre-sales-reception/finish-reception";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 19.7. 取消接待（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:39
     * Long id : 接待id  是否必填 true  版本 v1.0
     * Long shopId : 接待门店id  是否必填 true  版本 v1.0
     */

    public JSONObject CancelReceptionScene(Long id, Long shopId) {
        String url = "/car-platform/pc/pre-sales-reception/cancel-reception";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 19.8. 销售接待员工列表（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:39
     * Long shopId : 门店id  是否必填 true  版本 v2.0
     */

    public JSONObject saleReceptorListScene(Long shopId) {
        String url = "/car-platform/pc/pre-sales-reception/receptor/list";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 19.9. 变更销售接待（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:39
     * Long id : 接待id  是否必填 true  版本 v2.0
     * Long shopId : 门店id  是否必填 true  版本 v2.0
     * String receptorId : 售后接待员工id  是否必填 true  版本 v2.0
     */

    public JSONObject saleReceptorChangeScene(Long id, Long shopId, String receptorId) {
        String url = "/car-platform/pc/pre-sales-reception/receptor/change";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);
        json.put("receptor_id", receptorId);

        return invokeApi(url, json);
    }

    /**
     * 19.11. 客户备注（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:39
     * Long id : 接待id  是否必填 true  版本 v3.0
     * Long shopId : 门店id  是否必填 true  版本 v3.0
     * String remark : 客户备注信息  是否必填 true  版本 v3.0
     */

    public JSONObject CustomerRemarkScene(Long id, Long shopId, String remark) {
        String url = "/car-platform/pc/pre-sales-reception/customer/remark";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);
        json.put("remark", remark);

        return invokeApi(url, json);
    }

    /**
     * 19.13. 客户车辆编辑（谢）v3.0 （2021-03-16）的接口
     *
     * @date 2021-04-07 17:50:39
     * Long id : 车辆id  是否必填 true  版本 v3.0
     * Long carModel : 购买车辆车型  是否必填 true  版本 v3.0
     * String vin : 车辆底盘号  是否必填 false  版本 v3.0
     */

    public JSONObject VehicleEditScene(Long id, Long carModel, String vin) {
        String url = "/car-platform/pc/pre-sales-reception/vehicle/edit";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("car_model", carModel);
        json.put("vin", vin);

        return invokeApi(url, json);
    }

    /**
     * 20.3. 售后接待员工列表（谢）（2020-12-15）的接口
     *
     * @date 2021-04-07 17:50:39
     * Long shopId : 门店id  是否必填 true  版本 v2.0
     */

    public JSONObject ReceptorListScene(Long shopId) {
        String url = "/car-platform/pc/reception-manage/receptor/list";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 20.4. 变更售后接待（谢）（2020-12-15）的接口
     *
     * @date 2021-04-07 17:50:39
     * Long id : 接待id  是否必填 true  版本 v2.0
     * Long shopId : 门店id  是否必填 true  版本 v2.0
     * String receptorId : 售后接待员工id  是否必填 true  版本 v2.0
     */

    public JSONObject ReceptorChangeScene(Long id, Long shopId, String receptorId) {
        String url = "/car-platform/pc/reception-manage/receptor/change";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);
        json.put("receptor_id", receptorId);

        return invokeApi(url, json);
    }

    /**
     * 22.8. 员工详情 （杨）（2021-03-23） v3.0的接口
     *
     * @date 2021-04-07 17:50:40
     * String id : 账号id  是否必填 true  版本 v2.0
     */

    public JSONObject DetailScene(String id) {
        String url = "/car-platform/pc/staff/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 23.28. 商品详情 (张小龙) v2.0的接口
     *
     * @date 2021-04-07 17:50:40
     * Long id : 唯一id  是否必填 true  版本 v2.0
     */

    public JSONObject GoodsDetailScene(Long id) {
        String url = "/car-platform/pc/integral-mall/goods-detail";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 25.7. 查看门店洗车二维码(池)的接口
     *
     * @date 2021-04-07 17:50:41
     * Long shopId : 门店id  是否必填 false  版本 v2.0
     */

    public JSONObject ShowShopExtensionScene(Long shopId) {
        String url = "/car-platform/pc/shop/show-shop-extension";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * 26.2. 内容运营 : 活动置顶 （谢）（2020-03-02）的接口
     *
     * @date 2021-04-07 17:50:41
     * Long id : 活动id  是否必填 true  版本 v2.0
     */

    public JSONObject ManageTopScene(Long id) {
        String url = "/car-platform/pc/activity/manage/top";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 26.12. 活动详情 （谢）v3.0（2021-04-02）的接口
     *
     * @date 2021-04-07 17:50:41
     * Long id : 活动id  是否必填 true  版本 v2.0
     */

    public JSONObject ManageDetailScene(Long id) {
        String url = "/car-platform/pc/activity/manage/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 26.13. 删除活动 （谢）（2020-12-23）的接口
     *
     * @date 2021-04-07 17:50:41
     * Long id : 活动id  是否必填 true  版本 v2.0
     */

    public JSONObject ManageDeleteScene(Long id) {
        String url = "/car-platform/pc/activity/manage/delete";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 26.14. 撤销活动申请 （谢）（2020-12-23）的接口
     *
     * @date 2021-04-07 17:50:41
     * Long id : 活动id  是否必填 true  版本 v2.0
     */

    public JSONObject ManageRevokeScene(Long id) {
        String url = "/car-platform/pc/activity/manage/revoke";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 26.15. 取消活动 （谢）（2020-12-23）的接口
     *
     * @date 2021-04-07 17:50:41
     * Long id : 活动id  是否必填 true  版本 v2.0
     */

    public JSONObject ManageCancelScene(Long id) {
        String url = "/car-platform/pc/activity/manage/cancel";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 26.16. 恢复活动 （谢）v3.0（2021-04-02）的接口
     *
     * @date 2021-04-07 17:50:41
     * Long id : 活动id  是否必填 true  版本 v2.0
     */

    public JSONObject ManageRecoveryScene(Long id) {
        String url = "/car-platform/pc/activity/manage/recovery";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 26.17. 下架活动 （谢）v3.0（2021-04-02）的接口
     *
     * @date 2021-04-07 17:50:41
     * Long id : 活动id  是否必填 true  版本 v2.0
     */

    public JSONObject ManageOfflineScene(Long id) {
        String url = "/car-platform/pc/activity/manage/offline";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 26.18. 上架活动 （谢）v3.0（2021-04-02）的接口
     *
     * @date 2021-04-07 17:50:41
     * Long id : 活动id  是否必填 true  版本 v2.0
     */

    public JSONObject ManageOnlineScene(Long id) {
        String url = "/car-platform/pc/activity/manage/online";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 26.19. 活动推广 （谢）（2020-12-23）的接口
     *
     * @date 2021-04-07 17:50:41
     * Long id : 活动id  是否必填 true  版本 v2.0
     */

    public JSONObject ManagePromotionScene(Long id) {
        String url = "/car-platform/pc/activity/manage/promotion";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 26.20. 活动审批（2020-12-23）的接口
     *
     * @date 2021-04-07 17:50:41
     * Integer status : 审批状态 枚举见字典表《活动审批状态》  是否必填 true  版本 v2.0
     * JSONArray ids : 活动id列表  是否必填 true  版本 v2.0
     */

    public JSONObject ManageApprovalScene(Integer status, JSONArray ids) {
        String url = "/car-platform/pc/activity/manage/approval";
        JSONObject json = new JSONObject();
        json.put("status", status);
        json.put("ids", ids);

        return invokeApi(url, json);
    }

    /**
     * 26.23. 活动报名数据 （谢）（2020-12-23）的接口
     *
     * @date 2021-04-07 17:50:42
     * Long activityId : 活动id  是否必填 true  版本 v2.0
     */

    public JSONObject ManageRegisterDataScene(Long activityId) {
        String url = "/car-platform/pc/activity/manage/register/data";
        JSONObject json = new JSONObject();
        json.put("activity_id", activityId);

        return invokeApi(url, json);
    }

    /**
     * 26.25. 活动报名审批（2020-12-23）的接口
     *
     * @date 2021-04-07 17:50:42
     * Long activityId : 活动id  是否必填 true  版本 v2.0
     * Integer status : 审批状态 101：通过，201：拒绝  是否必填 true  版本 v2.0
     * JSONArray ids : 报名id列表  是否必填 true  版本 v2.0
     */

    public JSONObject ManageRegisterApprovalScene(Long activityId, Integer status, JSONArray ids) {
        String url = "/car-platform/pc/activity/manage/register/approval";
        JSONObject json = new JSONObject();
        json.put("activity_id", activityId);
        json.put("status", status);
        json.put("ids", ids);

        return invokeApi(url, json);
    }

    /**
     * 26.26. 活动变更记录分页 （谢）（2020-12-23）的接口
     *
     * @date 2021-04-07 17:50:42
     * Integer page : 页码 大于0  是否必填 true  版本 v1.0
     * Integer size : 页大小 范围为[1,100]  是否必填 true  版本 v1.0
     * Long id : 活动id  是否必填 true  版本 v2.0
     */

    public JSONObject ManageChangeRecordPageScene(Integer page, Integer size, Long id) {
        String url = "/car-platform/pc/activity/manage/change/record/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 28.4. 品牌详情（谢）的接口
     *
     * @date 2021-04-07 17:50:42
     * Long id : 品牌id  是否必填 true  版本 v1.0
     */

    public JSONObject brandDetailScene(Long id) {
        String url = "/car-platform/pc/brand/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 28.11. 品牌车系详情（谢）的接口
     *
     * @date 2021-04-07 17:50:42
     * Integer page : 页码 大于0  是否必填 true  版本 v1.0
     * Integer size : 页大小 范围为[1,100]  是否必填 true  版本 v1.0
     * Long id : 车系id  是否必填 true  版本 v1.0
     */

    public JSONObject CarStyleDetailScene(Integer page, Integer size, Long id) {
        String url = "/car-platform/pc/brand/car-style/detail";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 28.18. 品牌车系车型详情（谢）的接口
     *
     * @date 2021-04-07 17:50:42
     * Long id : 车型id  是否必填 true  版本 v1.0
     */

    public JSONObject CarStyleCarModelDetailScene(Long id) {
        String url = "/car-platform/pc/brand/car-style/car-model/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 29.1. 修改banner详情的接口
     *
     * @date 2021-04-07 17:50:42
     * JSONArray list : No comments found.  是否必填 false  版本 -
     * String bannerType : banner类型 HOME_PAGE 首页 ONLINE_EXPERTS 在线专家 RENEW_CONSULT 续保咨询 USED_CAR 二手车 USED_CAR_ASSESS 二手车评估  是否必填 true  版本 -
     */

    public JSONObject EditScene(JSONArray list, String bannerType) {
        String url = "/car-platform/pc/banner/edit";
        JSONObject json = new JSONObject();
        json.put("list", list);
        json.put("banner_type", bannerType);

        return invokeApi(url, json);
    }

    /**
     * 29.2. banner列表详情的接口
     *
     * @date 2021-04-07 17:50:42
     * JSONArray list : No comments found.  是否必填 false  版本 -
     * String bannerType : banner类型 HOME_PAGE 首页 ONLINE_EXPERTS 在线专家 RENEW_CONSULT 续保咨询 USED_CAR 二手车 USED_CAR_ASSESS 二手车评估  是否必填 true  版本 -
     */

    public JSONObject ListScene(JSONArray list, String bannerType) {
        String url = "/car-platform/pc/banner/list";
        JSONObject json = new JSONObject();
        json.put("list", list);
        json.put("banner_type", bannerType);

        return invokeApi(url, json);
    }

    /**
     * 29.3. 返回banner类型列表 （池）（2021-03-11）的接口
     *
     * @date 2021-04-07 17:50:42
     * JSONArray list : No comments found.  是否必填 false  版本 -
     * String bannerType : banner类型 HOME_PAGE 首页 ONLINE_EXPERTS 在线专家 RENEW_CONSULT 续保咨询 USED_CAR 二手车 USED_CAR_ASSESS 二手车评估  是否必填 true  版本 -
     */

    public JSONObject TypeListScene(JSONArray list, String bannerType) {
        String url = "/car-platform/pc/banner/type-list";
        JSONObject json = new JSONObject();
        json.put("list", list);
        json.put("banner_type", bannerType);

        return invokeApi(url, json);
    }

    /**
     * 31.6. 角色详情 （杨航）的接口
     *
     * @date 2021-04-07 17:50:42
     * Integer id : 角色id  是否必填 true  版本 v1.0
     */

    public JSONObject roleDetailScene(Integer id) {
        String url = "/car-platform/pc/role/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 32.2. 获取指定枚举值列表（谢）（2021-02-19）的接口
     *
     * @date 2021-04-07 17:50:42
     * String enumType : 枚举类型  是否必填 true  版本 v2.0
     */

    public JSONObject EnumValueListScene(String enumType) {
        String url = "/car-platform/pc/enum-value-list";
        JSONObject json = new JSONObject();
        json.put("enum_type", enumType);

        return invokeApi(url, json);
    }

    /**
     * 35.7. 积分兑换排序 (张小龙) v2.0的接口
     *
     * @date 2021-04-07 17:50:43
     * Long id : 唯一id  是否必填 true  版本 v2.0
     */

    public JSONObject ChangeOrderScene(Long id) {
        String url = "/car-platform/pc/integral-center/change-order";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    /**
     * 35.13. 兑换商品规格详情列表 (张小龙) v2.0 （2021-01-11）的接口
     *
     * @date 2021-04-07 17:50:43
     * Long id : 唯一id  是否必填 true  版本 v2.0
     */

    public JSONObject ExchangeCommoditySpecificationsListScene(Long id) {
        String url = "/car-platform/pc/integral-center/exchange-commodity-specifications-list";
        JSONObject json = new JSONObject();
        json.put("id", id);

        return invokeApi(url, json);
    }

    public JSONObject roleTree() {
        String url = "/car-platform/pc/auth/tree";
        JSONObject json = new JSONObject();

        return invokeApi(url, json);
    }


}
