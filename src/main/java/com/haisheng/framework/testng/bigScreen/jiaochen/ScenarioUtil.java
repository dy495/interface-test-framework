package com.haisheng.framework.testng.bigScreen.jiaochen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.Variable.*;
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
    private static final String IpPort = EnumTestProduce.JIAOCHEN_DAILY.getAddress();
    private static final String shopId = EnumTestProduce.JIAOCHEN_DAILY.getShopId();

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

    //pc登录
    public void pcLogin(String phone, String verificationCode) {
        String path = "/jiaochen/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        httpPost(path, object, IpPort);
    }

    //app登录
    public void appLogin(String username, String password) {
        String path = "/jiaochen/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        httpPost(path, object, IpPort);
    }

    //app登录
    public JSONObject appLogin2(String username, String password, Boolean checkCode) {
        String path = "/jiaochen/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        return invokeApi(path, object, checkCode);
    }
    //pc登录
    public JSONObject pcTryLogin(String phone, String verificationCode,Boolean checkcode) {
        String path = "/jiaochen/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        return invokeApi(path, object, checkcode);
    }

    //app登录
    public void appLoginout() {
        String path = "/jiaochen/m-app/login-user/logout";
        JSONObject object = new JSONObject();
        httpPostWithCheckCode(path, object.toJSONString(), IpPort);
    }

    public void appletLoginToken(String token) {
        authorization = token;
        logger.info("applet authorization is:{}", authorization);
    }

    //pc门店列表
    public JSONObject pcShopList() {
        String path = "/jiaochen/pc/login-user/shop-list";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //pc主体列表
    public JSONObject pcSubjectList() {
        String path = "/jiaochen/pc/use-range/subject-list";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //pc修改密码
    public JSONObject pcModifyPassword(String oldPassword, String newPassword) {
        String path = "/jiaochen/pc/modifyPassword";
        JSONObject object = new JSONObject();
        object.put("old_password", oldPassword);
        object.put("new_password", newPassword);
        return invokeApi(path, object);
    }

    //pc通过token获取用户信息
    public JSONObject pcAuthLoginUserDetail() {
        String path = "/jiaochen/pc/auth/login-user/detail";
        JSONObject object = new JSONObject();
        return invokeApi(path, object, false);
    }

    //pc登出
    public JSONObject pcLogout() {
        String path = "/jiaochen/pc/logout";
        JSONObject object = new JSONObject();
        return invokeApi(path, object, false);
    }

    //pc通用枚举接口
    public JSONObject pcEnuMap() {
        String path = "/jiaochen/pc/enum-map";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //pc地区树
    public JSONObject pcDistrictTree() {
        String path = "/jiaochen/pc/district/tree";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //图片上传
    public JSONObject pcFileUpload(String pic, Boolean isPermanent, Double ratio) {
        String path = "/jiaochen/pc/file/upload";
        JSONObject object = new JSONObject();
        object.put("pic", pic);
        object.put("is_permanent", isPermanent);
        object.put("ratio", ratio);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 列表
    public JSONObject pcReceptionManagePage(String shop_id, String page, String size) {
        String path = "/jiaochen/pc/reception-manage/page";
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shop_id);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 开始接待
    public JSONObject pcStartReception(appStartReception ar) {
        String path = "/jiaochen/pc/reception-manage/start-reception";
        JSONObject object = new JSONObject();
        object.put("customer_id", ar.id);
        object.put("plate_number", ar.plate_number);
        object.put("customer_name", ar.customer_name);
        object.put("customer_phone", ar.customer_phone);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 开始接待
    public JSONObject pcStartReception(String customerId, List<Long> voucherIdList, String customerName, String customerPhone) {
        String path = "/jiaochen/pc/reception-manage/start-reception";
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("voucher_id_list", voucherIdList);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 开始接待
    public JSONObject pcManageReception(String plate_number, boolean checkCode) {
        String path = "/jiaochen/pc/reception-manage/reception";
        JSONObject object = new JSONObject();
        object.put("plate_number", plate_number);
        return invokeApi(path, object, checkCode);
    }

    //pc接待管理 -> 完成接待
    public JSONObject pcFinishReception(Long receptionId) {
        String path = "/jiaochen/pc/reception-manage/finish-reception";
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 取消接待
    public JSONObject pcCancelReception(Long receptionId) {
        String path = "/jiaochen/pc/reception-manage/cancel-reception";
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 取消接待
    public JSONObject pcCancelReception(Long receptionId, Long shop_id) {
        String path = "/jiaochen/pc/reception-manage/cancel-reception";
        JSONObject object = new JSONObject();
        object.put("id", receptionId);
        object.put("shop_id", shop_id);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 套餐列表
    public JSONObject pcPackageList() {
        String path = "/jiaochen/pc/reception-manage/package-list";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //pc接待管理 -> 卡券列表
    public JSONObject pcVoucherList() {
        String path = "/jiaochen/pc/reception-manage/voucher-list";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //pc客户管理 -> 客户类型
    public JSONObject pcCustomerType() {
        String path = "/jiaochen/pc/customer-manage/pre-sale-customer/customer-type";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    public JSONObject pcUserRangeDetail() {
        String path = "/jiaochen/pc/use-range/detail";
        JSONObject object = new JSONObject();
        object.put("subject_key", "BRAND");
        return invokeApi(path, object);
    }


    //客户管理 -> 维修记录
    public JSONObject pcAfterSaleCustomerRepairPage(Integer page, Integer size, Integer carId) {
        String url = "/jiaochen/pc/customer-manage/after-sale-customer/repair-page";
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        object.put("car_id", carId);
        return invokeApi(url, object);
    }

    //客户管理 -> 小程序客户
    public JSONObject pcWechatCustomerPage(String createDate, String customerPhone, Integer activeType, Integer page, Integer size) {
        String url = "/jiaochen/pc/customer-manage/wechat-customer/page";
        JSONObject object = new JSONObject();
        object.put("create_date", createDate);
        object.put("customer_phone", customerPhone);
        object.put("active_type", activeType);
        object.put("page", page);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //预约管理 -> 预约看板
    public JSONObject pcTimeTableList(String appointmentMonth) {
        String url = "/jiaochen/pc/appointment-manage/time-table/list";
        JSONObject object = new JSONObject();
        object.put("appointment_month", appointmentMonth);
        return invokeApi(url, object);
    }

    //预约管理 -> 确认预约
    public JSONObject pcAppointmentRecordConfirm(Long id) {
        String url = "/jiaochen/pc/appointment-manage/appointment-record/confirm";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }

    //预约管理 -> 取消预约
    public JSONObject pcAppointmentRecordCancel(Long id) {
        String url = "/jiaochen/pc/appointment-manage/appointment-record/cancel";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }

    //日期可预约时间段
    public JSONObject pcMaintainTimeList(Long shopId, String day) {
        String url = "/jiaochen/pc/appointment-manage/maintain/time/list";
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("day", day);
        return invokeApi(url, object);
    }

    //预约管理 -> 调整
    public JSONObject pcAppointmentRecordAdjust(Long id, Long timeId) {
        String url = "/jiaochen/pc/appointment-manage/appointment-record/adjust";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("time_id", timeId);
        return invokeApi(url, object);
    }

    //卡券管理 -> 是否自助核销
    public JSONObject pcSwichSelfVerification(Long id, Boolean status) {
        String url = "/jiaochen/pc/voucher-manage/swich_self_verification";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return invokeApi(url, object);
    }

    //卡券管理 -> 卡券作废
    public JSONObject pcInvalidVoucher(Long id) {
        String url = "/jiaochen/pc/voucher-manage/invalid-voucher";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }

    public JSONObject pcWorkOrder(String filePath) {
        String path = "/jiaochen/pc/import/work_order";
        String response = uploadFile(filePath, path, IpPort);
        return JSON.parseObject(response);
    }

    //卡券管理 -> 卡券增发
    public JSONObject pcAddVoucher(Long id, Integer addNumber) {
        String url = "/jiaochen/pc/voucher-manage/add-voucher";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("add_number", addNumber);
        return invokeApi(url, object);
    }

    //卡券管理 -> 卡券转移
    public JSONObject pcTransfer(String transferPhone, String receivePhone, List<Long> voucherIds) {
        String url = "/jiaochen/pc/voucher-manage/transfer";
        JSONObject object = new JSONObject();
        object.put("transfer_phone", transferPhone);
        object.put("receive_phone", receivePhone);
        object.put("voucher_ids", voucherIds);
        return invokeApi(url, object);
    }

    //卡券管理 -> 手机号查询卡券列表
    public JSONObject pcVoucherList(String transferPhone) {
        String url = "/jiaochen/pc/voucher-manage/voucher-list";
        JSONObject object = new JSONObject();
        object.put("transfer_phone", transferPhone);
        return invokeApi(url, object);
    }

    //卡券管理 -> 发卡记录
    public JSONObject pcSendRecord(String voucherName, String sender, Long startTime, Long endTime, Integer page, Integer size) {
        String url = "/jiaochen/pc/voucher-manage/send-record";
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
        String url = "/jiaochen/pc/voucher-manage/verification-record";
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
        String url = "/jiaochen/pc/voucher-manage/verification-people";
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
        String url = "/jiaochen/pc/voucher-manage/create-verification-people";
        JSONObject object = new JSONObject();
        object.put("verification_person_name", verificationPersonName);
        object.put("verification_person_phone", verificationPersonPhone);
        object.put("status", status);
        object.put("type", type);
        return invokeApi(url, object);
    }

    //套餐管理 -> 套餐开启状态
    public JSONObject pcSwitchPackageStatus(Boolean status, Long id) {
        String url = "/jiaochen/pc/package-manage/package-form/switch-package-status";
        JSONObject object = new JSONObject();
        object.put("status", status);
        object.put("id", id);
        return invokeApi(url, object);
    }

    //套餐管理 -> 套餐详情
    public JSONObject pcPackageDetail(Long id) {
        String url = "/jiaochen/pc/package-manage/package-detail";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }

    //套餐管理 -> 手机号查询客户信息
    public JSONObject pcSearchCustomer(String customerPhone) {
        String url = "/jiaochen/pc/package-manage/search-customer";
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        return invokeApi(url, object);
    }

    //卡券管理 -> 套餐确认购买
    public JSONObject pcMakeSureBuy(Long id) {
        String url = "/jiaochen/pc/packsge-manage/make-sure-buy";
        JSONObject object = new JSONObject();
        object.put("id", id);
        return invokeApi(url, object);
    }

    //保养配置修改
    public JSONObject pcCarModelPriceEdit(String id, Double price, String status) {
        String url = "/jiaochen/pc/manage/maintain/car-model/price/edit";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("price", price);
        object.put("status", status);
        return invokeApi(url, object);
    }

    //保养配置修改
    public JSONObject pcCarModelPriceEdit(String id, String price, String status,Boolean checkcode) {
        String url = "/jiaochen/pc/manage/maintain/car-model/price/edit";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("price", price);
        object.put("status", status);
        return invokeApi(url, object,checkcode);
    }

    //预约时间段
    public JSONObject timeRangeDetail(String type, String dateType) {
        String url = "/jiaochen/pc/manage/appointment/time-range/detail";
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("date_type", dateType);
        return invokeApi(url, object);
    }

    //保养配置修改
    public JSONObject carModelPriceEdit(Long id, Double price, String status) {
        String url = "/jiaochen/pc/manage/maintain/car-model/price/edit";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("price", price);
        object.put("status", status);
        return invokeApi(url, object);
    }

    //评价跟进
    public JSONObject pcEvaluateFollowUp(Long id, String remark) {
        String url = "/jiaochen/pc/manage/evaluate/follow-up";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("remark", remark);
        return invokeApi(url, object);
    }

    //评价配置详情
    public JSONObject pcConfigDetail(String type) {
        String url = "/jiaochen/pc/manage/evaluate/config/detail";
        JSONObject object = new JSONObject();
        object.put("type", type);
        return invokeApi(url, object);
    }

    //卡券申请审批
    public JSONObject pcApplyApproval(Long id, String status) {
        String url = "/jiaochen/pc/voucher/apply/approval";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return invokeApi(url, object);
    }

    //小程序我的卡券
    public JSONObject appletVoucherList(JSONObject lastValue, String type, Integer size) {
        String url = "/jiaochen/applet/granted/voucher/list";
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("type", type);
        object.put("size", size);
        return invokeApi(url, object);
    }

    public JSONObject appletMessageList(Long lastValue, Integer size) {
        String url = "/jiaochen/applet/granted/message/list";
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("size", size);
        return invokeApi(url, object);
    }

    //app今日任务
    public JSONObject appTodayTask() {
        String url = "/jiaochen/m-app/home-page/today-task";
        JSONObject object = new JSONObject();
        return invokeApi(url, object);
    }

    //app接待列表
    public JSONObject appReceptionPage(Integer lastValue, Integer size) {
        String url = "/jiaochen/m-app/task/reception/page";
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("size", size);
        return invokeApi(url, object);
    }

    public JSONObject pcMakeSureBuy(Long id, String auditStatus) {
        String path = "/jiaochen/pc/package-manage/make-sure-buy";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("audit_status", auditStatus);
        return invokeApi(path, object);
    }

    public JSONObject invokeApi(IScene scene) {
        return invokeApi(scene, true);
    }

    public JSONObject invokeApi(IScene scene, boolean checkCode) {
        return invokeApi(scene.getPath(), scene.getJSONObject(), checkCode);
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
        if (StringUtils.isEmpty(path)) {
            throw new DataException("path不可为空");
        }
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
        String url = "/jiaochen/pc/staff/page";
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
        String url = "/jiaochen/pc/staff/add";
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
    public JSONObject organizationAccountAdd(String name, String phone, List role_list, List shop_list,Boolean checkcode) {
        String url = "/jiaochen/pc/staff/add";
        JSONObject json=new JSONObject();
        json.put("name",name);
        json.put("phone",phone);
        json.put("role_list",role_list);
        json.put("shop_list",shop_list);
        return invokeApi(url,json,checkcode);
    }

    public JSONObject organizationAccountAdd2(String name, String phone, List role_list, List shop_list,Boolean checkcode,String empty) {
        String url = "/jiaochen/pc/staff/add";
        JSONObject json=new JSONObject();
        json.put("name",name);
        json.put("phone",phone);
        json.put("role_list",role_list);
        json.put("shop_list",shop_list);
        if(empty!=null){
            json.remove(empty);
        }
        return invokeApi(url,json,checkcode);
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
    public JSONObject organizationAccountEdit(String id, String name,String phone, JSONArray role_id_list, JSONArray shop_list) {
        String url = "/jiaochen/pc/staff/edit";
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
        String url = "/jiaochen/pc/role/delete";
        String json =
                "{" +
                        "\"id\" :\"" + account + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    public JSONObject organizationAccountDelete(String account) {
        String url = "/jiaochen/pc/staff/delete";
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
        String url = "/jiaochen/pc/staff/status/change";
        String json =
                "{" +
                        "\"id\" :\"" + account + "\",\n" +
                        "\"status\" :\"" + status + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject organizationRoleButtom(String account, String status) {
        String url = "/jiaochen/pc/role/status/change";
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
        String url = "/jiaochen/pc/role/page";
        String json =
                "{" +
                        "\"name\" :\"" + role_name + "\",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject organizationRolePage(Integer page, Integer size) {
        String url = "/jiaochen/pc/role/page";
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
        String url = "/jiaochen/pc/role/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"description\" :\"" + description + "\",\n" +
                        "\"module_id\" :" + module_id + "\n" +

                        "} ";

        return invokeApi(url, JSONObject.parseObject(json), checkcode);


    }

    public JSONObject organizationRoleAdd(String name, String description, JSONArray module_id) {
        String url = "/jiaochen/pc/role/add";
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
        String url = "/jiaochen/pc/role/edit";
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
        String url = "/jiaochen/pc/role/delete";
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
        String url = "/jiaochen/m-app/personal-center/er-code";
        JSONObject json = new JSONObject();
        return invokeApi(url, json);
    }

    /**
     * @description :pc接待管理查询  ---xmf
     * @date :2020/11/24 15:13
     **/
    public JSONObject receptionManage(String shop_id, String page, String size, String parm, String result) {
        String url = "/jiaochen/pc/reception-manage/page";
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
        String url = "/jiaochen/pc/reception-manage/page";
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
        String url = "/jiaochen/pc/reception-manage/page";
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
        String url = "/jiaochen/m-app/home-page/today-task";
        JSONObject json = new JSONObject();

        return invokeApi(url, json);
    }

    /**
     * @description :app今日数据Xmf
     * @date :2020/11/24 15:21
     **/

    public JSONObject apptodayDate(String type, Integer last_value, Integer size) {
        String url = "/jiaochen/m-app/home-page/today-data";
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
        String url = "/jiaochen/m-app/voucher/verification";
        JSONObject json = new JSONObject();
        json.put("card_number", card_number);

        return invokeApi(url, json, checkCode);
    }

    /**
     * @description :代办事项数Xmf
     * @date :2020/11/24 15:25
     **/

    public JSONObject waitTask() {
        String url = "/jiaochen/m-app/home-page/waiting-task/num";
        JSONObject json = new JSONObject();

        return invokeApi(url, json);
    }

    /**
     * @description :预约任务页Xmf
     * @date :2020/11/24 15:30
     **/

    public JSONObject appointmentPage(Integer last_value, Integer size) {
        String url = "/jiaochen/m-app/task/appointment/page";
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
        String url = "/jiaochen/m-app/task/appointment/handle";
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
        String url = "/jiaochen/m-app/task/reception/page";
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
        String url = "/jiaochen/m-app/task/reception/admit";
        JSONObject json = new JSONObject();
        json.put("plate_number", plate_number);

        return invokeApi(url, json);
    }

    public JSONObject appReceptionAdmitcode(String plate_number) {
        String url = "/jiaochen/m-app/task/reception/admit";
        JSONObject json = new JSONObject();
        json.put("plate_number", plate_number);

        return invokeApi(url, json, false);
    }

    /**
     * @description :app开始接待xmf
     * @date :2020/11/24 19:28
     **/

    public JSONObject StartReception(appStartReception sr) {
        String url = "/jiaochen/m-app/task/reception/start-reception";
        JSONObject json = new JSONObject();
        json.put("customer_id", sr.id);
        json.put("is_new", sr.is_new);
        json.put("customer_name", sr.customer_name);
        json.put("customer_phone", sr.customer_phone);
        json.put("plate_number", sr.plate_number);

        return invokeApi(url, json);
    }

    /**
     * @description :完成接待Xmf
     * @date :2020/11/24 19:31
     **/

    public JSONObject finishReception(Long id, Long shop_id) {
        String url = "/jiaochen/m-app/task/reception/finish-reception";
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
        String url = "/jiaochen/m-app/task/reception/cancel-reception";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("shop_id", shopId);

        return invokeApi(url, json);
    }

    /**
     * @description :小程序预约xmf
     * @date :2020/11/25 17:01
     **/
    public JSONObject appletAppointment(appletAppointment pm) {
        String url = "/jiaochen/applet/granted/maintain/appointment";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", pm.shop_id);
        json1.put("staff_id", pm.staff_id);
        json1.put("time_id", pm.time_id);
        json1.put("car_id", pm.car_id);
        json1.put("appointment_name", pm.appointment_name);
        json1.put("appointment_phone", pm.appointment_phone);

        return invokeApi(url, json1);
    }

    /**
     * @description :门店信息 xmf
     * @date :2020/11/28 12:11
     **/
    public JSONObject appletShopInfo() {
        String url = "/jiaochen/applet/shop-info";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    /**
     * @description :banner xmf
     * @date :2020/11/28 12:14
     **/

    public JSONObject appletbanner() {
        String url = "/jiaochen/applet/banner";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    /**
     * @description :文章列表xmf
     * @date :2020/11/28 12:14
     **/
    public JSONObject appletArticleList(String size, String last_value) {
        String url = "/jiaochen/applet/article/list";
        JSONObject json1 = new JSONObject();
        json1.put("size", size);
        json1.put("last_value", last_value);
        return invokeApi(url, json1);
    }

    /**
     * @description :文章详情xmf
     * @date :2020/11/28 12:14
     **/
    public JSONObject appletArticleDetail(String id) {
        String url = "/jiaochen/applet/article/detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :文章发放卡券列表xmf
     * @date :2020/11/28 12:14
     **/
    public JSONObject appletArticlevoucher(String id) {
        String url = "/jiaochen/applet/article/voucher/list";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :活动领卡券 xmf
     * @date :2020/11/28 12:14
     **/
    public JSONObject appletvoucherReceive(String article_id, String voucher_id) {
        String url = "/jiaochen/applet/granted/article/voucher/receive";
        JSONObject json1 = new JSONObject();
        json1.put("article_id", article_id);
        json1.put("voucher_id", voucher_id);
        return invokeApi(url, json1);
    }

    /**
     * @description :活动报名xmf
     * @date :2020/11/28 12:20
     **/
    public JSONObject appletactivityRegister(appletActivityRegister pm) {
        String url = "/jiaochen/applet/granted/article/activity/register";
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
        String url = "/jiaochen/applet/granted/car/create";
        JSONObject json1 = new JSONObject();
        json1.put("plate_number", plate_number);
        json1.put("model_id", model_id);

        return invokeApi(url, json1);
    }

    /**
     * @description :新增车辆xmf
     * @date :2020/11/28 12:29
     **/
    public JSONObject appletAddCarcode(String plate_number, String model_id) {
        String url = "/jiaochen/applet/granted/car/create";
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
        String url = "/jiaochen/applet/granted/car/list";
        JSONObject json1 = new JSONObject();
//        json1.put("style_id", style_id);
        return invokeApi(url, json1);
    }

    /**
     * @description :我的预约XMF
     * @date :2020/11/28 12:45
     **/

    public JSONObject appletMyAppointment(String last_value, String type, String size) {
        String url = "/jiaochen/applet/granted/car/create";
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

    public JSONObject appletCancleAppointment(Long id, String shopId) {
        String url = "/jiaochen/applet/granted/appointment/maintain/cancel";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        json1.put("shop_id", shopId);

        return invokeApi(url, json1);
    }

    /**
     * @description :删除预约记录XMF
     * @date :2020/11/28 12:45
     **/

    public JSONObject appletmaintainDelete(String id) {
        String url = "/jiaochen/applet/granted/appointment/maintain/delete";
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
        String url = "/jiaochen/applet/granted/appointment/activity/list";
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
        String url = "/jiaochen/applet/granted/appointment/activity/cancel";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :删除预约记录XMF
     * @date :2020/11/28 12:45
     **/

    public JSONObject appletactivityDelete(String id) {
        String url = "/jiaochen/applet/granted/appointment/activity/delete";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :预约评价项 XMF
     * @date :2020/11/28 12:45
     **/

    public JSONObject appletevaluateItems(String id) {
        String url = "/jiaochen/applet/granted/appointment/evaluate/items";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :我的消息XMF
     * @date :2020/11/28 12:45
     **/
    public JSONObject appletmessageList(String last_value, String size) {
        String url = "/jiaochen/applet/granted/message/list";
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
        String url = "/jiaochen/applet/granted/message/detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :编辑车辆xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletCarEdit(String id, String plate_number, String model_id) {
        String url = "/jiaochen/applet/granted/car/edit";
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
    public JSONObject appletCarDelst(String id) {
        String url = "/jiaochen/applet/granted/car/delete";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

        return invokeApi(url, json1);
    }

    /**
     * @description :品牌列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletBrandList() {
        String url = "/jiaochen/applet/brand/list";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    /**
     * @description :车系列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletCarStyleList(Long brand_id) {
        String url = "/jiaochen/applet/style/list";
        JSONObject json1 = new JSONObject();
        json1.put("brand_id", brand_id);
        return invokeApi(url, json1);
    }

    /**
     * @description :车型列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletCarModelList(Long brand_id, Long style_id) {
        String url = "/jiaochen/applet/model/list";
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
        String url = "/jiaochen/applet/name";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    /**
     * @description :车型列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletplateNumberProvinceList() {
        String url = "/jiaochen/applet/plate-number-province-list";
        JSONObject json1 = new JSONObject();

        return invokeApi(url, json1);
    }

    /**
     * @description :保养门店列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletmaintainShopList(String car_id, List coordinate) {
        String url = "/jiaochen/applet/granted/maintain/shop/list";
        JSONObject json1 = new JSONObject();
        json1.put("car_id", car_id);
        json1.put("coordinate", coordinate);

        return invokeApi(url, json1);
    }

    /**
     * @description :服务顾问列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletStaffList(String shop_id) {
        String url = "/jiaochen/applet/granted/maintain/staff/list";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);

        return invokeApi(url, json1);
    }

    /**
     * @description :可预约时段列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject appletmaintainTimeList(Long shop_id, Long car_id, String day) {
        String url = "/jiaochen/applet/granted/maintain/time/list";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("car_id", car_id);
        json1.put("day", day);

        return invokeApi(url, json1);
    }

    public JSONObject appletmaintainTimeList(Long shop_id, Long car_id, String day,Boolean checkcode) {
        String url = "/jiaochen/applet/granted/maintain/time/list";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("car_id", car_id);
        json1.put("day", day);

        return invokeApi(url, json1,checkcode);
    }


    /**
     * @description :可预约时段列表 xmf
     * @date :2020/11/28 12:58
     **/
    public JSONObject pcRoleList() {
        String url = "/jiaochen/pc/role/list";
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

    @DataProvider(name = "SELECT_PARM")  //xmf
    public static Object[] select_parm() {
        return new String[][]{
                {"vehicle_chassis_code", "vehicle_chassis_code"},

        };
    }

    @DataProvider(name = "PLATE")  //xmf
    public static Object[] plate() {   //异常车牌号集合
        return new String[]{
                "苏BJ123",   //6位
                "BJ12345",    //不含汉字
                "京1234567",  //不含英文
                "京bj12345", //含小写
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
        String url = "/jiaochen/pc/brand/page";
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
        String url = "/jiaochen/pc/brand/add";
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("logo_path", logo);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject addBrandNotChk(String name, String logo) throws Exception {
        String url = "/jiaochen/pc/brand/add";
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
        String url = "/jiaochen/pc/brand/edit";
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
        String url = "/jiaochen/pc/brand/delete";
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
        String url = "/jiaochen/pc/brand/car-style/page";
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
        String url = "/jiaochen/pc/brand/car-style/add";
        JSONObject json = new JSONObject();
        json.put("brand_id", brand_id);
        json.put("manufacturer", manufacturer); //生产商
        json.put("name", name); //车系名称
        json.put("online_time", online_time); // 上线时间
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject addCarStyleNotChk(Long brand_id, String manufacturer, String name, String online_time) throws Exception {
        String url = "/jiaochen/pc/brand/car-style/add";
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
        String url = "/jiaochen/pc/brand/car-style/edit";
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
        String url = "/jiaochen/pc/brand/car-style/delete";
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
        String url = "/jiaochen/pc/brand/car-style/car-model/page";
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
        String url = "/jiaochen/pc/brand/car-style/car-model/add";
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
        String url = "/jiaochen/pc/brand/car-style/car-model/add";
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
        String url = "/jiaochen/pc/brand/car-style/car-model/edit";
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
        String url = "/jiaochen/pc/brand/car-style/car-model/delete";
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
        String url = "/jiaochen/pc/reception-manage/page";
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
        String url = "/jiaochen/pc/customer-manage/pre-sale-customer/page";
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
        String url = "/jiaochen/pc/customer-manage/pre-sale-customer/page";
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
        String url = "/jiaochen/pc/customer-manage/pre-sale-customer/page";
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
        String url = "/jiaochen/pc/customer-manage/after-sale-customer/page";
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
     * @description:售后管理列表-时间筛选
     * @author: gly
     * @time: 2020-12/16
     */
    public JSONObject afterSleCustomerTimeManage(String shop_id, String page, String size, String create_start_time, String create_end_time, String order_start_time, String order_end_time) {
        String url = "/jiaochen/pc/customer-manage/after-sale-customer/page";
        JSONObject json1 = new JSONObject();
        json1.put("shop_id", shop_id);
        json1.put("page", page);
        json1.put("size", size);
        json1.put("create_start_time", create_start_time);
        json1.put("create_end_time", create_end_time);
        json1.put("order_start_time", order_start_time);
        json1.put("order_end_time", order_end_time);
        return invokeApi(url, json1);
    }

    /**
     * @description:售后管理列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject afterSleCustomerManage(String page, String size) {
        String url = "/jiaochen/pc/customer-manage/after-sale-customer/page";
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
        String url = "/jiaochen/pc/customer-manage/after-sale-customer/page";
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
        String url = "/jiaochen/pc/customer-manage/wechat-customer/page";
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
        String url = "/jiaochen/pc/customer-manage/wechat-customer/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }

    /**
     * @description:小程序客户管理列表
     * @time: 2020-11-25
     */
    public JSONObject weChatSleCustomerManage(weChatSleCustomerVariable variable) {
        String url = "/jiaochen/pc/customer-manage/wechat-customer/page";
        JSONObject json = new JSONObject();
        json.put("shopId", variable.shop_id);
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("create_date", variable.end_time);
        json.put("active_type", variable.start_time);
        json.put("customer_phone", variable.customer_phone);

        return invokeApi(url, json);
    }

    /**
     * @description:预约记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject appointmentRecordManage(String shopId, String page, String size, String pram, String result) {
        String url = "/jiaochen/pc/appointment-manage/appointment-record/appointment-page";
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
     * @time: 2020-12-16
     */
    public JSONObject appointmentRecordTimeManage(String shopId, String page, String size, String appointment_start, String appointment_end, String confirm_start, String confirm_end, String create_start, String create_end) {
        String url = "/jiaochen/pc/appointment-manage/appointment-record/appointment-page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("appointment_end", appointment_end);
        json.put("appointment_start", appointment_start);
        json.put("confirm_end", confirm_end);
        json.put("confirm_start", confirm_start);
        json.put("create_end", create_end);
        json.put("create_start", create_start);
        return invokeApi(url, json);
    }

    /**
     * @description:预约记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject appointmentRecordManage(appointmentRecordVariable variable) {
        String url = "/jiaochen/pc/appointment-manage/appointment-record/appointment-page";
        JSONObject json = new JSONObject();
        json.put("shopId", variable.shop_id);
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("plate_number", variable.plate_number);
        json.put("customer_manager", variable.customer_manager);
        json.put("shop_id", variable.shop_id);
        json.put("customer_name", variable.customer_name);
        json.put("confirm_status", variable.confirm_status);
        json.put("customer_phone", variable.customer_phone);
        json.put("is_overtime", variable.is_overtime);
//        json.put("confirm_time", variable.confirm_time);
        json.put("appointment_date", variable.appointment_date);
//        json.put("create_date", variable.create_date);

        return invokeApi(url, json);
    }

    /**
     * @description:保养配置
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject maintainFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/jiaochen/pc/manage/maintain/car-model/page";
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
     * @description:保养配置
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject maintainFilterManage(maintainVariable variable) {
        String url = "/jiaochen/pc/manage/maintain/car-model/page";
        JSONObject json = new JSONObject();
        json.put("shopId", variable.shop_id);
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("brand_name", variable.brand_name);
        json.put("manufacturer", variable.manufacturer);
        json.put("car_model", variable.car_model);
        json.put("year", variable.year);

        return invokeApi(url, json);
    }

    /**
     * @description:卡券管理
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject voucherFormFilterManage(voucherFormVariable variable) {
        String url = "/jiaochen/pc/voucher-manage/voucher-form/page";
        JSONObject json = new JSONObject();
        json.put("shopId", variable.shop_id);
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("subject_name", variable.subject_name);
        json.put("voucher_status", variable.voucher_status);
        json.put("creator", variable.creator);
        json.put("is_diff", variable.is_diff);
        json.put("is_self_verification", variable.is_self_verification);
        json.put("voucher_status", variable.voucher_status);

        return invokeApi(url, json);
    }

    /**
     * @description:卡券管理
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject voucherFormFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/jiaochen/pc/voucher-manage/voucher-form/page";
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
     * @description:发卡记录
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject sendRecordFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/jiaochen/pc/voucher-manage/send-record";
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
        String url = "/jiaochen/pc/voucher-manage/send-record";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("start_time", start_time);
        json.put("end_time", end_time);
        return invokeApi(url, json);
    }


    /**
     * @description:发卡记录
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject sendRecordFilterManage(sendRecordVariable variable) {
        String url = "/jiaochen/pc/voucher-manage/send-record";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", variable.page);
        json.put("size", variable.size);
        json.put("end_time", variable.end_time);
        json.put("voucher_name", variable.voucher_name);
        json.put("sender", variable.sender);
        json.put("start_time", variable.start_time);


        return invokeApi(url, json);
    }

    /**
     * @description:核销记录
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject verificationReordFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/jiaochen/pc/voucher-manage/verification-record";
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
    public JSONObject verificationReordTimeFilterManage(String shopId, String page, String size, String start_time, String end_time) {
        String url = "/jiaochen/pc/voucher-manage/verification-record";
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
    public JSONObject verificationReordFilterManage(verificationRecordVariable variable) {
        String url = "/jiaochen/pc/voucher-manage/verification-record";
        JSONObject json = new JSONObject();
        json.put("voucher_name", variable.voucher_name);
        json.put("sender", variable.sender);
        json.put("start_time", variable.start_time);
        json.put("end_time", variable.start_time);
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
        String url = "/jiaochen/pc/voucher-manage/verification-people";
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
        String url = "/jiaochen/pc/voucher-manage/verification-people";
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
    public JSONObject packageFormFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/jiaochen/pc/package-manage/package-form/page";
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
    public JSONObject packageFormTimeFilterManage(String shopId, String page, String size, String start_time, String end_time) {
        String url = "/jiaochen/pc/package-manage/package-form/page";
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
        String url = "/jiaochen/pc/package-manage/package-form/page";
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
        String url = "/jiaochen/pc/package-manage/buy-package-record";
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
        String url = "/jiaochen/pc/package-manage/buy-package-record";
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
        String url = "/jiaochen/pc/package-manage/buy-package-record";
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
        String url = "/jiaochen/pc/message-manage/message-form/page";
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
        String url = "/jiaochen/pc/message-manage/message-form/page";
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
        String url = "/jiaochen/pc/message-manage/message-form/page";
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
        String url = "/jiaochen/pc/operation/article/page";
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
    public JSONObject articleTimeFilterManage(String shopId, String page, String size, String start_time, String end_time, String register_start_date, String register_end_date) {
        String url = "/jiaochen/pc/operation/article/page";
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
     * @description:文章表单
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject articleFilterManage(articleVariable variable) {
        String url = "/jiaochen/pc/operation/article/page";
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
        String url = "/jiaochen/pc/operation/register/page";
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
        String url = "/jiaochen/pc/operation/register/page";
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
        String url = "/jiaochen/pc/operation/register/page";
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
        String url = "/jiaochen/pc/operation/approval/page";
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
        String url = "/jiaochen/pc/operation/approval/page";
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
     * @description:卡券申请
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject applyListFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/jiaochen/pc/voucher/apply/page";
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
        String url = "/jiaochen/pc/voucher/apply/page";
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
        String url = "/jiaochen/pc/voucher/apply/page";
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
        String url = "/jiaochen/pc/shop/page";
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
        String url = "/jiaochen/pc/shop/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);

        return invokeApi(url, json);
    }

    /**
     * @description:品牌列表
     * @author: gly
     * @time: 2020-11-24
     */

    public JSONObject brandListFilterManage3(String shopId, String page, String size, String name) {
        String url = "/jiaochen/pc/brand/page";
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
        String url = "/jiaochen/pc/brand/page";
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
        String url = "/jiaochen/pc/brand/page";
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
        String url = "/jiaochen/pc/brand/car-style/page";
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
        String url = "/jiaochen/pc/brand/car-style/page";
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
        String url = "/jiaochen/pc/brand/car-style/car-model/page";
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
        String url = "/jiaochen/pc/brand/car-style/car-model/page";
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
        String url = "/jiaochen/pc/role/page";
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
        String url = "/jiaochen/pc/role/page";
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
        String url = "/jiaochen/pc/staff/page";
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
    public JSONObject staffListFilterManage(String shopId, String page, String size, String role_name) {
        String url = "/jiaochen/pc/staff/page";
        JSONObject json = new JSONObject();
        json.put("shopId", shopId);
        json.put("page", page);
        json.put("size", size);
        json.put("role_name", role_name);
        return invokeApi(url, json);
    }

    /**
     * @description:导入记录列表
     * @author: gly
     * @time: 2020-11-24
     */
    public JSONObject importListFilterManage(String shopId, String page, String size, String pram, String result) {
        String url = "/jiaochen/pc/record/import/page";
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
        String url = "/jiaochen/pc/record/import/page";
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
        String url = "/jiaochen/pc/record/import/page";
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
        String url = "/jiaochen/pc/record/export/page";
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
    public JSONObject exportListFilterManage(String shopId, String page, String size, String type, String user, String export_time) {
        String url = "/jiaochen/pc/record/export/page";
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
        String url = "/jiaochen/pc/record/push-msg/page";
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
        String url = "/jiaochen/pc/record/push-msg/page";
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
        String url = "/jiaochen/pc/record/push-msg/page";
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
        String url = "/jiaochen/pc/shop/page";
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
        String url = "/jiaochen/pc/shop/add";
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
        String url = "/jiaochen/pc/shop/add";
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
                               String appointment_status, String washing_status) { // 预约状态：ENABLE（开启） DISABLE（关闭）
        String url = "/jiaochen/pc/shop/edit";
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
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description:门店管理-门店详情
     * @author: lxq
     * @time: 2020-11-25
     */

    public JSONObject shopDetail(Long id) {
        String url = "/jiaochen/pc/shop/detail";
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
        String url = "/jiaochen/pc/shop/change";
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
        String url = "/jiaochen/pc/operation/article/page";
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
        String url = "/jiaochen/pc/operation/article/add";
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
        String url = "/jiaochen/pc/operation/article/add";
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
        String url = "/jiaochen/pc/operation/article/edit";
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
        String url = "/jiaochen/pc/operation/article/top";
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
        String url = "/jiaochen/pc/operation/article/detail";
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
        String url = "/jiaochen/pc/operation/approval";
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
        String url = "/jiaochen/pc/operation/status/change";
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
        String url = "/jiaochen/applet/granted/maintain/appointment";
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
        String url = "/jiaochen/login-pc";
        JSONObject json = new JSONObject();
        json.put("phone", phone);
        json.put("verification_code", code);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    public JSONObject enummap() {
        String url = "/jiaochen/pc/enum-map";
        JSONObject json = new JSONObject();
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
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

    public JSONObject appletUserInfoEdit(appletInfoEdit er) {
        String url = "/jiaochen/applet/granted/user-info/edit";
        JSONObject json1 = new JSONObject();
        json1.put("name", er.name);
        json1.put("contact", er.contact);
        json1.put("gender", er.gender);
        json1.put("birthday", er.birthday);
        json1.put("shipping_address", er.shipping_address);
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
        String url = "/jiaochen/applet/granted/user-info/detail";
        JSONObject json1 = new JSONObject();
        return invokeApi(url, json1);
    }

    /**
     * @description :创建活动
     * @date :2020/12/16 16:10
     **/

    public JSONObject pccreateActile(pccreateActile er) {
        String url = "/jiaochen/pc/operation/article/add";
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

    public JSONObject appletvoucherList() {
        String url = "/jiaochen/applet/granted/article/voucher/list";
        JSONObject json1 = new JSONObject();
        return invokeApi(url, json1);
    }

    //app-核销记录
    public JSONObject appWriteOffRecordsPage(String type, String size, String last_value) {
        String url = "/jiaochen/m-app/personal-center/write-off-records/page";
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
        String url = "/jiaochen/applet/granted/appointment/list";
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
        String url = "/jiaochen/pc/role/add";
        String json =
                "{\n" +
                        "\"name\":\"" + name + "\"," +
                        "\"auth_list\":" + auth_list +
                        "}";
        String result = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    //删除角色
    public JSONObject roleDel(long id) {
        String url = "/jiaochen/pc/role/delete";
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
        String url = "/jiaochen/pc/message-manage/push-message";
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
        String url = "/jiaochen/pc/message-manage/push-message";
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
        String url = "/jiaochen/applet/granted/message/detail";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);
        return invokeApi(url, json1);
    }

    /**
     * @description :自主核销
     * @date :2020/12/22 17:46
     **/
    public JSONObject appleterification(String id, String verification_code, Boolean checkcode) {
        String url = "/jiaochen/applet/granted/voucher/verification";
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
        String url = "/jiaochen/pc/customer-manage/pre-sale-customer/create-customer";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("customer_name", customerName);
        json.put("customer_phone", customerPhone);
        json.put("sex", sex);
        json.put("customer_type", customerType);
        return invokeApi(url, json);
    }

    public JSONObject appointmentTimeEdit(JSONObject json) {
        String url = "/jiaochen/pc/manage/appointment/time-range/edit";
        return invokeApi(url, json);
    }

    public JSONObject shopStatusChange(String id,String type,String status) {
        String url = "/jiaochen/pc/shop/status/change";
        JSONObject json=new JSONObject();
        json.put("id",id);
        json.put("type",type);
        json.put("status",status);
        return invokeApi(url, json);
    }

    public JSONObject pcappointmentConfig(pcAppointmentConfig er) {
        String url = "/jiaochen/pc/manage/appointment/config";
        JSONObject json1=new JSONObject();
        json1.put("type",er.type);
        json1.put("remind_time",er.remind_time);
        json1.put("replay_time_limit",er.replay_time_limit);
        json1.put("appointment_interval",er.appointment_interval);
        json1.put("on_time_reward",er.on_time_reward);
        json1.put("is_send_voucher",er.is_send_voucher);
        json1.put("vouchers",er.vouchers);
        json1.put("voucher_start",er.voucher_start);
        json1.put("voucher_end",er.voucher_end);
        return invokeApi(url, json1,er.checkcode);
    }


}
