package com.haisheng.framework.testng.bigScreen.jiaochenonline;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.Variable.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.lxq.create.pcCreateExchangeGoods;
import com.haisheng.framework.testng.bigScreen.jiaochen.lxq.create.submitOrder;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.*;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.lxq.pcCreateGoodsOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.springframework.util.StringUtils;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 轿辰接口类
 */
public class ScenarioUtilOnline extends TestCaseCommon {
    private static volatile ScenarioUtilOnline instance = null;
    private static final String IpPort = EnumTestProduce.JC_ONLINE.getAddress();
    private static final String shopId = EnumTestProduce.JC_ONLINE.getShopId();

    /**
     * 单例
     *
     * @return ScenarioUtil
     */
    public static ScenarioUtilOnline getInstance() {
        if (instance == null) {
            synchronized (ScenarioUtilOnline.class) {
                if (instance == null) {
                    instance = new ScenarioUtilOnline();
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
    //pc登录
    public JSONObject pcTryLogin(String phone, String verificationCode,Boolean checkcode) {
        String path = "/jiaochen/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        return invokeApi(path, object, checkcode);
    }
    //app登录
    public JSONObject appLogin2(String username, String password, Boolean checkCode) {
        String path = "/jiaochen/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        return invokeApi(path, object, checkCode);
    }
    //app登录
    public void appLogin(String username, String password) {
        String path = "/jiaochen/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        httpPost(path, object, IpPort);
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
    public JSONObject pcReceptionManagePage(Integer page, Integer size) {
        String path = "/jiaochen/pc/reception-manage/page";
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
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
    public JSONObject pcStartReception(String customerId, List<Long> voucherIdList, String customerName, String customerPhone) {
        String path = "/jiaochen/pc/reception-manage/start-reception";
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("voucher_id_list", voucherIdList);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 完成接待
    public JSONObject pcFinishReception(Long receptionId) {
        String path = "/jiaochen/pc/reception-manage/finish-reception";
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

    //卡券管理 -> 卡券增发
    public JSONObject pcAddVoucher(Long id, Integer addNumber) {
        String url = "/jiaochen/pc/voucher-manage/add-voucher";
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("add_number", addNumber);
        return invokeApi(url, object);
    }

    //卡券管理 -> 卡券转移
    public JSONObject pcTransfer(String transferPhone, String receivePhone, String voucherIds) {
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
    public JSONObject appletVoucherList(Long lastValue, String type, Integer size) {
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
        return invokeApi(scene.getPath(), scene.getRequestBody(), checkCode);
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
    public JSONObject organizationAccountEdit(String account, String name, String phone, JSONArray role_id_list,  JSONArray shop_list) {
        String url = "/jiaochen/pc/staff/edit";
        String json =
                "{" +
                        "\"id\" :\"" + account + "\",\n" +
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
        if (parm != null || !parm.equals("")) {
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
        json1.put("reception_sale_id", sr.reception_sale_id);
        json1.put("reception_date", sr.reception_date);
        json1.put("customer_name", sr.customer_name);
        json1.put("reception_status", sr.reception_status);
        json1.put("finish_date", sr.finish_date);
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

    public JSONObject appointmentHandle(Long id, String type) {
        String url = "/jiaochen/m-app/task/appointment/handle";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("type", type);

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
        json.put("id", sr.id);
        json.put("is_new", sr.is_new);
        json.put("customer_name", sr.customer_name);
        json.put("customer_phone", sr.customer_phone);

        return invokeApi(url, json);
    }

    /**
     * @description :完成接待Xmf
     * @date :2020/11/24 19:31
     **/

    public JSONObject finishReception(Long id, String shop_id) {
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

    public JSONObject cancleReception(Long id, String shopId) {
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
        String url = "/jiaochen/m-app/task/reception/cancel-reception";
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
    public JSONObject appletArticleDetile(String id) {
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
     * @description :文章发放卡券列表 xmf
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

    public JSONObject appletCancleAppointment(String id) {
        String url = "/jiaochen/applet/granted/appointment/maintain/cancel";
        JSONObject json1 = new JSONObject();
        json1.put("id", id);

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
    public JSONObject appletmessageDetail(String id) {
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
    public JSONObject appletCarModelList(String brand_id, String style_id) {
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
        json.put("service_sale_id", variable.service_sale_id);
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
        json.put("creator_name", variable.creator_name);
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
                               String appointment_status, String washing_status, String rescue_tel) { // 预约状态：ENABLE（开启） DISABLE（关闭）
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

    //pc接待管理 -> 开始接待
    public JSONObject pcManageReception(String plate_number, boolean checkCode) {
        String path = "/jiaochen/pc/reception-manage/reception";
        JSONObject object = new JSONObject();
        object.put("plate_number", plate_number);
        return invokeApi(path, object, checkCode);
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

    public JSONObject appointmentTimeEdit(JSONObject json) {
        String url = "/jiaochen/pc/manage/appointment/time-range/edit";
        return invokeApi(url, json);
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

    public JSONObject shopStatusChange(String id,String type,String status) {
        String url = "/jiaochen/pc/shop/status/change";
        JSONObject json=new JSONObject();
        json.put("id",id);
        json.put("type",type);
        json.put("status",status);
        return invokeApi(url, json);
    }

    //图片上传
    public JSONObject pcFileUploadNew(String pic) {
        String path = "/jiaochen/pc/file/upload";
        JSONObject object = new JSONObject();
        object.put("permanent_pic_type", 0);
        object.put("pic", pic);


        return invokeApi(path, object);
    }
    /**
     * ----------------------------积分商城相关开始-----------------------------
     */


    /**
     * @description :商品品类列表
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryPage(Integer page,Integer size,Boolean category_status,Long first_category,Long second_category,Long third_category) {
        String url = "/jiaochen/pc/integral-mall/category-page";
        JSONObject json1=new JSONObject();
        json1.put("page",page);
        json1.put("size",size);
        json1.put("category_status",category_status);
        json1.put("first_category",first_category);
        json1.put("second_category",second_category);
        json1.put("third_category",third_category);

        return invokeApi(url,json1);
    }

    /**
     * @description :创建商品品类
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryCreate(Boolean Checkcode, String category_name,String category_level,String belong_category,String belong_pic,Long id) {
        String url = "/jiaochen/pc/integral-mall/create-category";
        JSONObject json1=new JSONObject();
        json1.put("category_name",category_name);
        json1.put("category_level",category_level);
        json1.put("belong_category",belong_category);
        json1.put("belong_pic",belong_pic);
        json1.put("id",id);
        return invokeApi(url,json1,Checkcode);
    }

    /**
     * @description :品类下拉列表
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryList(String category_level) {
        String url = "/jiaochen/pc/integral-mall/category-list";
        JSONObject json1=new JSONObject();
        json1.put("category_level",category_level);
        return invokeApi(url,json1);
    }

    /**
     * @description :所属品类列表
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryBelong(String category_level) {
        String url = "/jiaochen/pc/integral-mall/belongs-category";
        JSONObject json1=new JSONObject();
        json1.put("category_level",category_level);
        return invokeApi(url,json1);
    }

    /**
     * @description :修改品类状态
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryChgStatus(Long id, Boolean status) {
        String url = "/jiaochen/pc/integral-mall/change-status";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("status",status);
        return invokeApi(url,json1);
    }

    public JSONObject categoryChgStatus(Long id, Boolean status,Boolean chk) {
        String url = "/jiaochen/pc/integral-mall/change-status";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("status",status);
        return invokeApi(url,json1,chk);
    }

    /**
     * @description :查看品类详情
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryDetail(Integer id, Integer page, Integer size) {
        String url = "/jiaochen/pc/integral-mall/category-detail";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("page",page);
        json1.put("size",size);
        return invokeApi(url,json1);
    }

    /**
     * @description :修改商品品类
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryEdit(Boolean Checkcode, Long id, String category_name,String category_level,String belong_category,String belong_pic) {
        String url = "/jiaochen/pc/integral-mall/edit-category";
        JSONObject json1=new JSONObject();
        json1.put("category_name",category_name);
        json1.put("category_level",category_level);
        json1.put("belong_category",belong_category);
        json1.put("belong_pic",belong_pic);
        json1.put("id",id);
        return invokeApi(url,json1,Checkcode);
    }

    /**
     * @description :删除商品品类
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryDel(Long id, Boolean chkcode) {
        String url = "/jiaochen/pc/integral-mall/delete-category";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :商品品牌分页
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandPage(Integer page, Integer size, String brand_name, Boolean brand_status) {
        String url = "/jiaochen/pc/integral-mall/brand-page";
        JSONObject json1=new JSONObject();
        json1.put("page",page);
        json1.put("size",size);
        json1.put("brand_name",brand_name);
        json1.put("brand_status",brand_status);
        return invokeApi(url,json1);
    }

    /**
     * @description :品牌下拉列表
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandList() {
        String url = "/jiaochen/pc/integral-mall/brand-list";
        JSONObject json1=new JSONObject();
        return invokeApi(url,json1);
    }

    /**
     * @description :创建品牌
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandCreat(Boolean chkcode, Long id, String brand_name, String brand_description,String brand_pic) {
        String url = "/jiaochen/pc/integral-mall/create-brand";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("brand_name",brand_name);
        json1.put("brand_description",brand_description);
        json1.put("brand_pic",brand_pic);
        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :修改品牌状态
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandChgStatus(Long id,  Boolean brand_status,Boolean chk) {
        String url = "/jiaochen/pc/integral-mall/change-brand-status";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("status",brand_status);
        return invokeApi(url,json1,chk);
    }

    /**
     * @description :商品品牌详情
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandDetail(Integer id,  Integer page, Integer size) {
        String url = "/jiaochen/pc/integral-mall/brand-detail";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("page",page);
        json1.put("size",size);
        return invokeApi(url,json1);
    }

    /**
     * @description :修改品牌
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandEdit(Boolean chkcode, Long id, String brand_name, String brand_description,String brand_pic) {
        String url = "/jiaochen/pc/integral-mall/edit-brand";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("brand_name",brand_name);
        json1.put("brand_description",brand_description);
        json1.put("brand_pic",brand_pic);
        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :删除商品品牌
     * @date :2021/1/20 14:00
     **/
    public JSONObject BrandDel(Long id, Boolean chkcode) {
        String url = "/jiaochen/pc/integral-mall/delete-brand";
        JSONObject json1=new JSONObject();
        json1.put("id",id);

        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :创建商品规格
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsCreate(String specifications_name, Long belongs_category, JSONArray category_list,Long id,Boolean chkcode) {
        String url = "/jiaochen/pc/integral-mall/create-specifications";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("specifications_name",specifications_name);
        json1.put("belongs_category",belongs_category);
        json1.put("category_list",category_list);
        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :修改规格状态
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsChgStatus(Long id,  Boolean ... status) {
        String url = "/jiaochen/pc/integral-mall/change-specifications-status";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("status",status[0]);
        if (status.length > 1){
            return invokeApi(url,json1,status[1]);
        }
        else {
            return invokeApi(url,json1);
        }

    }

    /**
     * @description :删除规格状态
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsDel(Long id,Boolean ... chk) {
        String url = "/jiaochen/pc/integral-mall/delete-specifications";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        if (chk.length>0){
            return invokeApi(url,json1,chk[0]);
        }
        else {
            return invokeApi(url,json1);
        }
    }


    /**
     * @description :商品规格详情
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsDetail(Long id,  Integer page, Integer size) {
        String url = "/jiaochen/pc/integral-mall/specifications-detail";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("page",page);
        json1.put("size",size);
        return invokeApi(url,json1);
    }

    /**
     * @description :修改规格
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsEdit(String specifications_name, Long belongs_category, JSONArray category_list,Long id,Boolean chkcode) {
        String url = "/jiaochen/pc/integral-mall/edit-specifications";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("specifications_name",specifications_name);
        json1.put("belongs_category",belongs_category);
        json1.put("specifications_list",category_list);
        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :删除商品规格
     * @date :2021/1/20 14:00
     **/
    public JSONObject specificationsDel(Integer id, Integer page, Integer size, Boolean chkcode) {
        String url = "/jiaochen/pc/integral-mall/delete-brand";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("page",page);
        json1.put("size",size);
        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :商品管理-品类树
     * @date :2021/1/20 14:00
     **/
    public JSONObject categoryTree() {
        String url = "/jiaochen/pc/integral-mall/category-tree";
        JSONObject json1=new JSONObject();
        return invokeApi(url,json1);
    }

    /**
     * @description :商品管理列表
     * @date :2021/1/20 14:00
     **/
    public JSONObject goodsManagePage(Integer page, Integer size,String goods_name,Long goods_brand,String goods_status,
                                      Integer first_category,Integer second_category,Integer third_category) {
        String url = "/jiaochen/pc/integral-mall/goods-manage-page";
        JSONObject json1=new JSONObject();
        json1.put("page",page);
        json1.put("size",size);
        json1.put("goods_name",goods_name);
        json1.put("goods_brand",goods_brand);
        json1.put("goods_status",goods_status);
        json1.put("first_category",first_category);
        json1.put("second_category",second_category);
        json1.put("third_category",third_category);
        return invokeApi(url,json1);
    }

    /**
     * @description :商品规格列表
     * @date :2021/3/03 19:00
     **/
    public JSONObject specificationsPage(Integer page, Integer size,String specifications_name,Long first_category,Boolean specifications_status) {
        String url = "/jiaochen/pc/integral-mall/specifications-page";
        JSONObject json1=new JSONObject();
        json1.put("page",page);
        json1.put("size",size);
        json1.put("specifications_name",specifications_name);
        json1.put("first_category",first_category);
        json1.put("specifications_status",specifications_status);
        return invokeApi(url,json1);
    }

    /**
     * @description :商品导出
     * @date :2021/1/20 14:00
     **/
    public JSONObject goodsManageExport(Integer page, Integer size,String goods_name,Integer goods_brand,String goods_status,
                                        Integer first_category,Integer second_category,Integer third_category,
                                        String export_type, JSONArray ids,Boolean chkcode) {
        String url = "/jiaochen/pc/integral-mall/goods-manage/export";
        JSONObject json1=new JSONObject();
        json1.put("page",page);
        json1.put("size",size);
        json1.put("goods_name",goods_name);
        json1.put("goods_brand",goods_brand);
        json1.put("goods_status",goods_status);
        json1.put("first_category",first_category);
        json1.put("second_category",second_category);
        json1.put("third_category",third_category);
        json1.put("export_type",export_type);
        json1.put("ids",ids);
        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :上架/下架
     * @date :2021/1/20 14:00
     **/
    public JSONObject goodsChgStatus(Long id, String status,Boolean checkCode) {
        String url = "/jiaochen/pc/integral-mall/change-goods-status";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("status",status);
        return invokeApi(url,json1,checkCode);
    }

    /**
     * @description :创建商品
     * @date :2021/1/22 17:07
     **/

    public JSONObject createGoodMethod(pcCreateGoodsOnline er) {
        String url = "/jiaochen/pc/integral-mall/create-goods";
        JSONObject json1=new JSONObject();
        json1.put("id",er.id);
        json1.put("goods_name",er.goods_name);
        json1.put("goods_description",er.goods_description);
        json1.put("first_category",er.first_category);
        json1.put("second_category",er.second_category);
        json1.put("third_category",er.third_category);
        json1.put("goods_brand",er.goods_brand);
        json1.put("goods_pic_list",er.goods_pic_list);
        json1.put("price",er.price);
        json1.put("select_specifications",er.select_specifications);
        json1.put("goods_specifications_list",er.goods_specifications_list);
        json1.put("goods_detail",er.goods_detail);

        return invokeApi(url,json1,er.checkcode);
    }
    /**
     * @description :删除商品
     * @date :2021/1/22 17:07
     **/

    public JSONObject deleteGoodMethod(Long id) {
        String url = "/jiaochen/pc/integral-mall/delete-goods";
        JSONObject json1=new JSONObject();

        json1.put("id",id);

        return invokeApi(url,json1);
    }
    /**
     * @description :商品管理列表
     * @date :2021/1/22 17:07
     **/

    public JSONObject GoodsList(String page,String size) {
        String url = "/jiaochen/pc/integral-mall/goods-manage-page";
        JSONObject json1=new JSONObject();
        json1.put("page",page);
        json1.put("size",size);

        return invokeApi(url,json1);
    }

    /**
     * @description :编辑商品
     * @date :2021/1/22 17:07
     **/

    public JSONObject editGoodMethod(pcCreateGoods  er) {
        String url = "/jiaochen/pc/integral-mall/edit-goods";
        JSONObject json1=new JSONObject();
        json1.put("id",er.id);
        json1.put("goods_name",er.goods_name);
        json1.put("goods_description",er.goods_description);
        json1.put("first_category",er.first_category);
        json1.put("second_category",er.second_category);
        json1.put("third_category",er.third_category);
        json1.put("goods_brand",er.goods_brand);
        json1.put("goods_pic_list",er.goods_pic_list);
        json1.put("price",er.price);
        json1.put("select_specifications",er.select_specifications);
        json1.put("goods_specifications_list",er.goods_specifications_list);
        json1.put("goods_detail",er.goods_detail);

        return invokeApi(url,json1,er.checkcode);
    }

    /**
     * @description :积分兑换列表
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangePage(Integer page,Integer size, String exchange_goods,String exchange_type,String status) {
        String url = "/jiaochen/pc/integral-center/exchange-page";
        JSONObject json1=new JSONObject();
        json1.put("page",page);
        json1.put("size",size);
        json1.put("exchange_goods",exchange_goods);
        json1.put("exchange_type",exchange_type);
        json1.put("status",status);

        return invokeApi(url,json1);
    }

    /**
     * @description :积分兑换列表--导出
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeExport(Integer page,Integer size, String exchange_goods,String exchange_type,String status,String export_type,JSONArray ids,Boolean chkcode) {
        String url = "/jiaochen/pc/integral-center/exchange/export";
        JSONObject json1=new JSONObject();
        json1.put("page",page);
        json1.put("size",size);
        json1.put("exchange_goods",exchange_goods);
        json1.put("exchange_type",exchange_type);
        json1.put("status",status);
        json1.put("export_type",export_type);
        json1.put("ids",ids);

        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :创建积分兑换商品
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodCreat(pcCreateExchangeGoods ex) {
        String url = "/jiaochen/pc/integral-center/create-exchange-goods";
        JSONObject json1=new JSONObject();
        json1.put("id",ex.id);
        json1.put("exchange_goods_type",ex.exchange_goods_type);
        json1.put("goods_id",ex.goods_id);
        json1.put("exchange_start_time",ex.exchange_start_time);
        json1.put("exchange_end_time",ex.exchange_end_time);
        json1.put("exchange_price",ex.exchange_price);
        json1.put("exchange_num",ex.exchange_num);
        json1.put("is_limit",ex.is_limit);
        json1.put("exchange_people_num",ex.exchange_people_num); // 接口有问题
        json1.put("specification_list",ex.specification_list);

        return invokeApi(url,json1,ex.chkcode);
    }

    /**
     * @description :编辑积分兑换商品
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodEdit(pcCreateExchangeGoods ex) {
        String url = "/jiaochen/pc/integral-center/edit-exchange-goods";
        JSONObject json1=new JSONObject();
        json1.put("id",ex.id);
        json1.put("exchange_goods_type",ex.exchange_goods_type);
        json1.put("goods_id",ex.goods_id);
        json1.put("exchange_start_time",ex.exchange_start_time);
        json1.put("exchange_end_time",ex.exchange_end_time);
        json1.put("exchange_price",ex.exchange_price);
        json1.put("exchange_num",ex.exchange_num);
        json1.put("is_limit",ex.is_limit);
        json1.put("exchange_people_num",ex.exchange_people_num);
        json1.put("specification_list",ex.specification_list);

        return invokeApi(url,json1,ex.chkcode);
    }


    /**
     * @description :积分兑换商品详情
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodDetail(Integer id) {
        String url = "/jiaochen/pc/integral-center/exchange-goods-detail";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1);
    }

    /**
     * @description :积分兑换开关
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodChgStatus(Integer id,Boolean status,Boolean chkcode) {
        String url = "/jiaochen/pc/integral-center/change-switch-status";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("status",status);
        return invokeApi(url,json1,chkcode);
    }


    /**
     * @description :积分兑换置顶
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodTop(Integer id,Boolean chkcode) {
        String url = "/jiaochen/pc/integral-center/make-top";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :积分兑换删除
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodDel(Integer id) {
        String url = "/jiaochen/pc/integral-center/delete-exchange-goods";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1);
    }

    /**
     * @description :积分兑换库存
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodStock(Integer id) {
        String url = "/jiaochen/pc/integral-center/exchange-goods-stock";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1);
    }

    /**
     * @description :编辑积分兑换库存
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeGoodStockEdit(Integer id,String goods_name,String change_stock_type,Integer num) {
        String url = "/jiaochen/pc/integral-center/edit-exchange-stock";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("goods_name",goods_name);
        json1.put("change_stock_type",change_stock_type);
        json1.put("num",num);
        return invokeApi(url,json1);
    }

    /**
     * @description :兑换商品规格详情列表
     * @date :2021/1/27 16:00
     **/

    public JSONObject commodityList(Integer id) {
        String url = "/jiaochen/pc/integral-center/commodity-specifications-list";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1);
    }

    /**
     * @description :兑换商品库存明细
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeStockPage(Integer page, Integer size,Integer id) {
        String url = "/jiaochen/pc/integral-center/exchange-stock-page";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("page",page);
        json1.put("size",size);
        return invokeApi(url,json1);
    }

    /**
     * @description :积分兑换明细
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeDetail(Integer page, Integer size,Integer id,String exchange_customer_name,String exchange_type,
                                     String exchange_start_time,String exchange_end_time) {
        String url = "/jiaochen/pc/integral-center/exchange-detailed";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("page",page);
        json1.put("size",size);
        json1.put("exchange_customer_name",exchange_customer_name);
        json1.put("exchange_type",exchange_type);
        json1.put("exchange_start_time",exchange_start_time);
        json1.put("exchange_end_time",exchange_end_time);
        return invokeApi(url,json1);
    }

    /**
     * @description :积分兑换明细导出
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeDetailExport(Integer page, Integer size,Integer id,String exchange_customer_name,String exchange_type,
                                           String exchange_start_time,String exchange_end_time,String export_type,JSONArray ids) {
        String url = "/jiaochen/pc/integral-center/exchange-detail/export";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("page",page);
        json1.put("size",size);
        json1.put("exchange_customer_name",exchange_customer_name);
        json1.put("exchange_type",exchange_type);
        json1.put("exchange_start_time",exchange_start_time);
        json1.put("exchange_end_time",exchange_end_time);
        json1.put("export_type",export_type);
        json1.put("ids",ids);
        return invokeApi(url,json1);
    }

    /**
     * @description :积分兑换订单
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeOrder(Integer page, Integer size,String order_id, String start_time,String end_time,String order_status,
                                    String member,String goods_name) {
        String url = "/jiaochen/pc/integral-center/exchange-order";
        JSONObject json1=new JSONObject();
        json1.put("order_id",order_id);
        json1.put("page",page);
        json1.put("size",size);
        json1.put("member",member);
        json1.put("goods_name",goods_name);
        json1.put("start_time",start_time);
        json1.put("end_time",end_time);
        json1.put("order_status",order_status);
        return invokeApi(url,json1);
    }

    /**
     * @description :积分兑换订单导出
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeOrderExport(Integer page, Integer size,String order_id, String start_time,String end_time,String order_status,
                                          String member,String goods_name,String export_type,JSONArray ids,Boolean chkcode) {
        String url = "/jiaochen/pc/integral-center/exchange-order/export";
        JSONObject json1=new JSONObject();
        json1.put("order_id",order_id);
        json1.put("page",page);
        json1.put("size",size);
        json1.put("member",member);
        json1.put("goods_name",goods_name);
        json1.put("start_time",start_time);
        json1.put("end_time",end_time);
        json1.put("order_status",order_status);
        json1.put("export_type",export_type);
        json1.put("ids",ids);
        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :取消订单
     * @date :2021/1/27 16:00
     **/

    public JSONObject exchangeOrderCancel(Integer id) {
        String url = "/jiaochen/pc/integral-center/cancel-order";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1);
    }

    /**
     * @description :确认发货
     * @date :2021/1/27 16:00
     **/

    public JSONObject confirmShipment(Long id,String odd_numbers,Boolean chkcode) {
        String url = "/jiaochen/pc/integral-center/confirm_shipment";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("odd_numbers",odd_numbers);
        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :订单明细
     * @date :2021/1/27 16:00
     **/

    public JSONObject orderDetail(Integer id) {
        String url = "/jiaochen/pc/integral-center/order-detail";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1);
    }

    /**
     * @description :积分基础规则设置
     * @date :2021/02/02 16:00
     **/

    public JSONObject basicRule(Integer page,Integer size) {
        String url = "/jiaochen/pc/integral-center/integral-basic-rules";
        JSONObject json1=new JSONObject();
        json1.put("page",page);
        json1.put("size",size);
        return invokeApi(url,json1);
    }

    /**
     * @description :积分规则设置
     * @date :2021/02/02 16:00
     **/

    public JSONObject setRule(Long id,Integer year,String description,String rule_type) {
        String url = "/jiaochen/pc/integral-center/integral-rule-set";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        json1.put("year",year);
        json1.put("description",description);
        json1.put("rule_type",rule_type);
        return invokeApi(url,json1);
    }

    /**
     * @description :积分兑换规则设置
     * @date :2021/02/02 16:00
     **/

    public JSONObject exchangeRule(Integer page,Integer size) {
        String url = "/jiaochen/pc/integral-center/integral-exchange-rules";
        JSONObject json1=new JSONObject();
        json1.put("page",page);
        json1.put("size",size);
        return invokeApi(url,json1);
    }


    //-------------小程序部分------------------------


    /**
     * @description :小程序积分商城首页
     * @date :2021/1/27 14:00
     **/
    public JSONObject appletHomePage() {
        String url = "/jiaochen/applet/granted/integral-mall/home-page";
        JSONObject json1=new JSONObject();
        return invokeApi(url,json1);
    }

    /**
     * @description :小程序积分商城首页
     * @date :2021/1/27 14:00
     **/
    public JSONObject appletIntegralRule(Integer id, String status) {
        String url = "/jiaochen/applet/granted/integral-mall/integral-rule";
        JSONObject json1=new JSONObject();
        return invokeApi(url,json1);
    }

    /**
     * @description :小程序积分商城商品列表
     * @date :2021/1/27 14:00
     **/
    public JSONObject appletMallCommidityList(Integer size, JSONObject last_value, String integral_sort, String integral_num, Boolean status) {
        String url = "/jiaochen/applet/granted/integral-mall/commodity-list";
        JSONObject json1=new JSONObject();
        json1.put("size",size);
        json1.put("last_value",last_value);
        json1.put("integral_sort",integral_sort);
        json1.put("integral_num",integral_num);
        json1.put("status",status);
        return invokeApi(url,json1);
    }

    /**
     * @description :小程序积分商城商品详情
     * @date :2021/1/27 14:00
     **/
    public JSONObject appletMallCommidityDetail(Integer id) {
        String url = "/jiaochen/applet/granted/integral-mall/commodity-detail";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1);
    }

    /**
     * @description :小程序积分商城创建收货地址
     * @date :2021/1/27 14:00
     **/
    public JSONObject appletMallCreateAddr(String name,String phone,String district_code,String address,String postal_code) {
        String url = "/jiaochen/applet/granted/integral-mall/create-address";
        JSONObject json1=new JSONObject();
        json1.put("name",name);
        json1.put("phone",phone);
        json1.put("district_code",district_code);
        json1.put("address",address);
        json1.put("postal_code",postal_code);
        return invokeApi(url,json1);
    }

    /**
     * @description :小程序实体商品提交订单
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletSubmitOrder(submitOrder or) {
        String url = "/jiaochen/applet/granted/integral-mall/submit-order";
        JSONObject json1=new JSONObject();
        json1.put("commodity_id",or.commodity_id);
        json1.put("specification_id",or.specification_id);
        json1.put("buyer_message",or.buyer_message);
        json1.put("sms_notify",or.sms_notify);
        json1.put("commodity_num",or.commodity_num);
        json1.put("district_code",or.district_code);
        json1.put("address",or.address);
        json1.put("receiver",or.receiver);
        json1.put("receive_phone",or.receive_phone);
        return invokeApi(url,json1,or.chkcode);
    }

    /**
     * @description :小程序虚拟商品提交订单
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletSubmitExchange(Long id,Boolean chkcode) {
        String url = "/jiaochen/applet/granted/integral-mall/integral-exchange";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1,chkcode);
    }

    /**
     * @description :小程序 个人积分详情记录
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletIntegralRecord(Integer size,Object last_value,String type,String start_time,String end_time) {
        String url = "/jiaochen/applet/granted/integral-mall/integral-record";
        JSONObject json1=new JSONObject();
        json1.put("size",size);
        json1.put("last_value",last_value);
        json1.put("type",type);
        json1.put("start_time",start_time);
        json1.put("end_time",end_time);
        return invokeApi(url,json1);
    }

    /**
     * @description :小程序 兑换记录
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletExchangeRecord(Integer size,Object last_value,String status) {
        String url = "/jiaochen/applet/granted/integral-mall/exchange-record";
        JSONObject json1=new JSONObject();
        json1.put("size",size);
        json1.put("last_value",last_value);
        json1.put("status",status);
        return invokeApi(url,json1);
    }

    /**
     * @description :小程序 订单详情
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletExchangeRecordDetail(Long id) {
        String url = "/jiaochen/applet/granted/integral-mall/exchange-record-detail";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1);
    }

    /**
     * @description :小程序 确认收货
     * @date :2021/1/29 14:00
     **/
    public JSONObject appletconfirmReceive(Long id) {
        String url = "/jiaochen/applet/granted/integral-mall/confirm-receive";
        JSONObject json1=new JSONObject();
        json1.put("id",id);
        return invokeApi(url,json1);
    }



    /**
     * ---------------------------------积分商城相关结束--------------------------------
     */

    /**
     * @description :applet道路救援
     * @date :2021/1/21 16:14
     **/

    public JSONObject rescueApply(String shop_id, JSONArray coordinate) {
        String url = "/jiaochen/applet/granted/rescue/apply";
        JSONObject json1=new JSONObject();
        json1.put("shop_id",shop_id);
        json1.put("coordinate",coordinate);
        return invokeApi(url,json1);
    }
    /**
     * @description :pc道路救援
     * @date :2021/1/21 16:14
     **/
    public JSONObject pcrescuePage(int page,int size) {
        String url = "/jiaochen/pc/manage/rescue/page";
        JSONObject json1=new JSONObject();
        json1.put("page",page);
        json1.put("size",size);
        return invokeApi(url,json1);
    }
    /**
     * @description :小程序授权手机号
     * @date :2021/1/21 19:20
     **/

    public JSONObject grantPhone(String phone,String verification_code,String recommend_customer_id,String business_type) {
        String url = "/jiaochen/applet/grant/phone";
        JSONObject json1=new JSONObject();
        json1.put("phone",phone);
        json1.put("verification_code",verification_code);
        json1.put("recommend_customer_id",recommend_customer_id);
        json1.put("business_type",business_type);
        return invokeApi(url,json1);
    }
    /**
     * @description :洗车次数
     * @date :2021/1/21 19:20
     **/

    public JSONObject washTimes() {
        String url = "/jiaochen/applet/granted/member-center/car-wash/remain-number";
        JSONObject json1=new JSONObject();
        return invokeApi(url,json1);
    }
    /**
     * @description :开始洗车
     * @date :2021/1/21 19:20
     **/

    public JSONObject carWsah(String car_wash_shop_id) {
        String url = "/jiaochen/applet/granted/member-center/car-wash/start";
        JSONObject json1=new JSONObject();
        json1.put("car_wash_shop_id",car_wash_shop_id);

        return invokeApi(url,json1);
    }
    /**
     * @description :免费洗车列表
     * @date :2021/1/21 19:29
     **/

    public JSONObject carWashShopList(JSONArray coordinate) {
        String url = "/jiaochen/applet/granted/member-center/car-wash/shop-list";
        JSONObject json1=new JSONObject();
        json1.put("coordinate",coordinate);

        return invokeApi(url,json1);
    }

    /**
     * @description :品牌列表
     * @date :2021/1/22 17:20
     **/

    public JSONObject bandList() {
        String url = "/jiaochen/pc/integral-mall/brand-list";
        JSONObject json1=new JSONObject();

        return invokeApi(url,json1);
    }
    //品类
    public JSONObject categoryList() {
        String url = "/jiaochen/pc/integral-mall/category-tree";
        JSONObject json1=new JSONObject();

        return invokeApi(url,json1);
    }

    //品类规格下拉
    public JSONObject specifications(Long first_category) {
        String url = "/jiaochen/pc/integral-mall/specifications-list";
        JSONObject json1=new JSONObject();
        json1.put("first_category",first_category);
        return invokeApi(url,json1);
    }

    //道路救援门店列表
    public JSONObject rescueShopList(JSONArray coordinate,String washingStatus ) {
        String url = "/jiaochen/applet/granted/rescue/shop/list";
        JSONObject json1=new JSONObject();
        if(!coordinate.equals(null)){
            json1.put("coordinate",coordinate);

        }
        if(!washingStatus.equals("null")) {
            json1.put("washingStatus", washingStatus);
        }
        return invokeApi(url,json1);
    }

    /**
     * @description :V2.0智能提醒
     * @date :2021/2/1
     **/
    public JSONObject remindPage(String page,String size,String item,String pram, String result) {
        String url = "/jiaochen/pc/manage/intelligent-remind/page";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);
        json.put("item",item);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url,json);
    }

    /**
     * @description :V2.0洗车管理列表
     * @date :2021/2/1
     **/
    public JSONObject washCarManagerPage(WashCarManagerVariable washCarManagerVariable) {
        String url = "/jiaochen/pc/vip-marketing/wash-car-manager/page";
        JSONObject json=new JSONObject();
        json.put("page",washCarManagerVariable.page);
        json.put("size",washCarManagerVariable.size);
        json.put("customer_name",washCarManagerVariable.customerName);
        json.put("customer_vip_type",washCarManagerVariable.customerVipType);
        json.put("wash_start_time",washCarManagerVariable.washStartTime);
        json.put("wash_end_time",washCarManagerVariable.washEndTime);
        json.put("shop_id",washCarManagerVariable.shopId);
        json.put("phone",washCarManagerVariable.phone);
        return invokeApi(url,json);
    }

    /**
     * @description :V2.0洗车管理列表
     * @date :2021/2/1
     **/
    public JSONObject washCarManagerPage(String page,String size,String param,String result) {
        String url = "/jiaochen/pc/vip-marketing/wash-car-manager/page";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);
        if (param != null){
            json.put(param, result);
        }
        return invokeApi(url,json);
    }

    /**
     * @description :V2.0调整记录次数
     * @date :2021/2/2
     **/
    public JSONObject adjustNumberRecord(String page,String size,String param,String result) {
        String url = "/jiaochen/pc/vip-marketing/wash-car-manager/adjust-number/record";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);
        if (param != null){
            json.put(param, result);
        }
        return invokeApi(url,json);
    }
    /**
     * @description :V2.0调整记录次数
     * @date :2021/2/2
     **/
    public JSONObject adjustNumberRecord(AdjustNumberRecordVariable adjustNumberRecordVariable) {
        String url = "/jiaochen/pc/vip-marketing/wash-car-manager/adjust-number/record";
        JSONObject json=new JSONObject();
        json.put("page",adjustNumberRecordVariable.page);
        json.put("size",adjustNumberRecordVariable.size);
        json.put("customer_name",adjustNumberRecordVariable.customerName);
        json.put("customer_phone",adjustNumberRecordVariable.customerPhone);
        json.put("adjust_shop_id",adjustNumberRecordVariable.adjustShopId);
        json.put("adjust_start_time",adjustNumberRecordVariable.adjustStartTime);
        json.put("adjust_end_time",adjustNumberRecordVariable.adjustEndTime);
        json.put("customer_type",adjustNumberRecordVariable.customerType);

        return invokeApi(url,json);
    }

    /**
     * @description :V2.0优惠券领取记录
     * @date :2021/2/2
     **/
    public JSONObject voucherManageSendRecord(VoucherManageSendVariable variable) {
        String url = "/jiaochen/pc/voucher-manage/send-record";
        JSONObject json=new JSONObject();
        json.put("page",variable.page);
        json.put("size",variable.size);
        json.put("id" ,variable.id);
        json.put("receiver",variable.receiver);
        json.put("receive_phone",variable.receivePhone);
        json.put("use_status",variable.useStatus);
        json.put("start_time",variable.startTime);
        json.put("end_time",variable.endTime);
        json.put("use_start_time",variable.useStartTime);
        json.put("use_end_time",variable.useEndTime);
        json.put("customer_label",variable.customerLabel);

        return invokeApi(url,json);
    }
    /**
     * @description :V2.0优惠券领取记录
     * @date :2021/2/2
     **/
    public JSONObject voucherManageSendRecord(String shopId,String page,String size,String id ,String param,String result) {
        String url = "/jiaochen/pc/voucher-manage/send-record";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);
        json.put("shop_id",shopId);
        json.put("id",id);
        if (param != null){
            json.put(param, result);
        }
        return invokeApi(url,json);
    }
    /**
     * @description :V2.0优惠券领取记录
     * @date :2021/2/2
     **/
    public JSONObject voucherManageSendRecord(String page,String size,String id ,String param,String result) {
        String url = "/jiaochen/pc/voucher-manage/send-record";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);
        json.put("id",id);
        if (param != null){
            json.put(param, result);
        }
        return invokeApi(url,json);
    }

    /**
     * @description :V2.0优惠券作废记录
     * @date :2021/2/2
     **/
    public JSONObject voucherInvalidPage(VoucherInvalidPageVariable variable) {
        String url = "/jiaochen/pc/voucher-manage/voucher-invalid-page";
        JSONObject json=new JSONObject();
        json.put("page",variable.page);
        json.put("size",variable.size);
        json.put("id",variable.id);
        json.put("receiver",variable.receiver);
        json.put("receive_phone",variable.receivePhone);
        json.put("start_time",variable.startTime);
        json.put("end_time",variable.endTime);
        json.put("invalid_name",variable.invalidName);
        json.put("invalid_phone",variable.invalidPhone);
        json.put("invalid_start_time",variable.invalidStartTime);
        json.put("invalid_end_time",variable.invalidEndTime);

        return invokeApi(url,json);
    }

    /**
     * @description :V2.0优惠券作废记录
     * @date :2021/2/2
     **/
    public JSONObject voucherInvalidPage(String shopId,String page,String size,String id ,String param,String result) {
        String url = "/jiaochen/pc/voucher-manage/voucher-invalid-page";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);
        json.put("shop_id",shopId);
        json.put("id",id);
        if (param != null){
            json.put(param, result);
        }
        return invokeApi(url,json);
    }

    /**
     * @description :pc道路救援
     * @date :2021/2/2
     **/
    public JSONObject rescuePage(String shopId,String page,String size,String pram,String result) {
        String url = "/jiaochen/pc/manage/rescue/page";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);
        json.put("shop_id",shopId);
        if (pram != null){
            json.put(pram, result);
        }
        return invokeApi(url,json);
    }

    /**
     * @description :pc道路救援
     * @date :2021/2/2
     **/
    public JSONObject rescuePage(RescuePageVariable variable) {
        String url = "/jiaochen/pc/manage/rescue/page";
        JSONObject json=new JSONObject();
        json.put("customer_name",variable.customerName);
        json.put("vip_type",variable.vipType);
        json.put("customer_phone",variable.customerPhone);
        json.put("shop_id",variable.shopId);
        json.put("dial_start",variable.dialStart);
        json.put("dial_end",variable.dialEnd);

        return invokeApi(url,json);
    }

    /**
     * @description :PC评价列表
     * @date :2021/2/2
     **/
    public JSONObject evaluatePage(String shopId,String page,String size,String pram,String result) {
        String url = "/jiaochen/pc/manage/evaluate/page";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);
        json.put("shop_id",shopId);
        if (pram != null){
            json.put(pram, result);
        }
        return invokeApi(url,json);
    }

    /**
     * @description :PC评价列表
     * @date :2021/2/2
     **/
    public JSONObject evaluatePage(EvaluatePageVariable variable) {
        String url = "/jiaochen/pc/manage/evaluate/page";
        JSONObject json=new JSONObject();
        json.put("page",variable.customerName);
        json.put("size",variable.customerName);
        json.put("plate_number",variable.customerName);
        json.put("service_sale_id",variable.customerName);
        json.put("evaluate_type",variable.customerName);
        json.put("shop_id",variable.customerName);
        json.put("customer_name",variable.customerName);
        json.put("score",variable.customerName);
        json.put("evaluate_start",variable.customerName);
        json.put("evaluate_end",variable.customerName);
        json.put("is_follow_up",variable.customerName);
        json.put("customer_phone",variable.customerName);
        json.put("source_create_start",variable.customerName);
        json.put("source_create_end",variable.customerName);
        return invokeApi(url,json);
    }

    /**
     * @description :PC精品商城-商城套餐
     * @date :2021/2/2
     **/
    public JSONObject storeCommodityPage(String shopId,String page,String size,String pram,String result) {
        String url = "/jiaochen/pc/store/commodity/page";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);
        json.put("shop_id",shopId);
        if (pram != null){
            json.put(pram, result);
        }
        return invokeApi(url,json);
    }

    /**
     * @description :PC精品商城-商城套餐
     * @date :2021/2/2
     **/
    public JSONObject storeCommodityPage(StoreCommodityPageVariable variable) {
        String url = "/jiaochen/pc/manage/evaluate/page";
        JSONObject json=new JSONObject();
        json.put("page",variable.page);
        json.put("size",variable.size);
        json.put("commodity_name",variable.commodityName);
        json.put("start_create_date",variable.startCreateDate);
        json.put("end_create_date",variable.endCreateDate);

        return invokeApi(url,json);
    }

    /**
     * @description :PC精品商城-商城订单
     * @date :2021/2/2
     **/
    public JSONObject storeOrderPage(String shopId,String page,String size,String pram,String result) {
        String url = "/jiaochen/pc/store/order/page";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);
        json.put("shop_id",shopId);
        if (pram != null){
            json.put(pram, result);
        }
        return invokeApi(url,json);
    }


    /**
     * @description :PC精品商城-商城订单
     * @date :2021/2/2
     **/
    public JSONObject storeOrderPage(StoreOrderPageVariable variable) {
        String url = "/jiaochen/pc/store/order/page";
        JSONObject json=new JSONObject();
        json.put("page",variable.page);
        json.put("size",variable.size);
        json.put("bind_phone",variable.bindPhone);
        json.put("commodity_name",variable.commodityName);
        json.put("start_pay_time",variable.startPayTime);
        json.put("end_pay_time",variable.endPayTime);
        json.put("order_number",variable.orderNumber);

        return invokeApi(url,json);
    }

    /**
     * @description :PC精品商城-分销员管理列表
     * @date :2021/2/3
     **/
    public JSONObject storeSalesPage(StoreSalesPageVariable variable) {
        String url = "/jiaochen/pc/store/sales/page";
        JSONObject json=new JSONObject();
        json.put("page",variable.page);
        json.put("size",variable.size);
        json.put("sales_phone",variable.salesPhone);
        json.put("shop_id",variable.shopId);

        return invokeApi(url,json);
    }

    /**
     * @description :PC精品商城-分销员管理列表
     * @date :2021/2/3
     **/
    public JSONObject storeSalesPage(String shopId,String page,String size,String pram,String result) {
        String url = "/jiaochen/pc/store/sales/page";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);
        json.put("shop_id",shopId);
        if (pram != null){
            json.put(pram, result);
        }
        return invokeApi(url,json);
    }




    /**
     * 导出列表
     */

    /**
     * @description :PC导出 通用
     * @date :2021/3/2
     **/
    public JSONObject recExport(String url) {
        JSONObject json=new JSONObject();
        json.put("page",1);
        json.put("size",10);
        json.put("export_type","CURRENT_PAGE");
//        if (url.equals("/jiaochen/pc/manage/maintain/car-model/export")){
//            json.put("shop_id","46439");
//        }
        return invokeApi(url,json,false);
    }

    /**
     * @description :PC导出 维修记录
     * @date :2021/3/2
     **/
    public JSONObject weixiuExport(String car_id,String shop_id) {
        JSONObject json=new JSONObject();
        String url = "/jiaochen/pc/customer-manage/after-sale-customer/repair-page/export";
        json.put("page",1);
        json.put("size",10);
        json.put("export_type","CURRENT_PAGE");
        json.put("car_id",car_id);
        json.put("shop_id",shop_id);
        return invokeApi(url,json,false);
    }

    /**
     * @description :PC导出 优惠券记录
     * @date :2021/3/2
     **/
    public JSONObject vourcherExport(String url, String id) {
        JSONObject json=new JSONObject();
        json.put("page",1);
        json.put("size",10);
        json.put("export_type","CURRENT_PAGE");
        json.put("voucher_id",id);
        return invokeApi(url,json,false);
    }

    /**
     * @description :PC导出 活动报名记录
     * @date :2021/3/2
     **/
    public JSONObject activityExport(String activity_id) {
        JSONObject json=new JSONObject();
        String url = "/jiaochen/pc/activity/manage/register/export";
        json.put("page",1);
        json.put("size",10);
        json.put("export_type","CURRENT_PAGE");
        json.put("activity_id",activity_id);
        return invokeApi(url,json,false);
    }
    /**
     * @description :PC导出 车系列表
     * @date :2021/3/2
     **/
    public JSONObject carStyleExport(Long brand_id) {
        JSONObject json=new JSONObject();
        String url = "/jiaochen/pc/brand/car-style/export";
        json.put("page",1);
        json.put("size",10);
        json.put("export_type","CURRENT_PAGE");
        json.put("brand_id",brand_id);
        return invokeApi(url,json,false);
    }

    /**
     * @description :PC导出 车型列表
     * @date :2021/3/2
     **/
    public JSONObject carModelExport(Long brand_id,Long style_id) {
        JSONObject json=new JSONObject();
        String url = "/jiaochen/pc/brand/car-style/car-model/export";
        json.put("page",1);
        json.put("size",10);
        json.put("export_type","CURRENT_PAGE");
        json.put("brand_id",brand_id);
        json.put("style_id",style_id);
        return invokeApi(url,json,false);
    }

    public JSONObject activityPage(Integer page,Integer size) {
        JSONObject json=new JSONObject();
        String url = "/jiaochen/pc/activity/manage/page";
        json.put("page",page);
        json.put("size",size);
        return invokeApi(url,json);
    }

    /**
     * 活动管理
     * @param page
     * @param size
     * @param pram
     * @param result
     * @return
     */
    public JSONObject activityPage(String page,String size,String pram,String result) {
        JSONObject json=new JSONObject();
        String url = "/jiaochen/pc/activity/manage/page";
        json.put("page",page);
        json.put("size",size);
        if (pram != null){
            json.put(pram, result);
        }
        return invokeApi(url,json);
    }

    /**
     * 活动报名列表
     * @param page
     * @param size
     * @param pram
     * @param result
     * @return
     */
    public JSONObject registerPage(String activityId,String page,String size,String pram,String result) {
        JSONObject json = new JSONObject();
        String url = "/jiaochen/pc/activity/manage/register/page";
        json.put("activity_id", activityId);
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    public JSONObject delArticle(Long id) {
        String url = "/jiaochen/pc/operation/article/delete";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }

    /**
     * @description :卡券表单
     * * * @author: gly
     * @date :2020/01/07
     **/
    public JSONObject oucherFormVoucherPage(String shopId, String page, String size) {
        String url = "/jiaochen/pc/voucher-manage/voucher-form/voucher-page";
        JSONObject json = new JSONObject();
        json.put("shop_id", shopId);
        json.put("page", page);
        json.put("size", size);
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
        String url = "/jiaochen/pc/customer-manage/pre-sale-customer/create-potential-customer";
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
        String url = "/jiaochen/pc/customer-manage/pre-sale-customer/create-customer";
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
     *
     */
    public JSONObject authListPage(String authType, String shopId) {
        JSONObject json = new JSONObject();
        json.put("auth_type", authType);
        json.put("shop_id", shopId);
        String url = "/jiaochen/pc/staff/auth-list";

        return invokeApi(url, json);
    }

    /**
     * 门店列表
     *
     * @return
     */
    public JSONObject shopListPage() {
        JSONObject json = new JSONObject();
        String url = "/jiaochen/pc/login-user/shop-list";

        return invokeApi(url, json);
    }

    /**
     * 销售顾问列表下拉
     *
     * @return
     */
    public JSONObject saleList(Long shop_id) {
        JSONObject json = new JSONObject();
        String url = "/jiaochen/pc/customer-manage/pre-sale-customer/sales-list";
        json.put("shop_id", shop_id);
        return invokeApi(url, json);
    }

    /**
     * 车辆style列表下拉
     *
     * @return
     */
    public JSONObject styleList(Long shop_id) {
        JSONObject json = new JSONObject();
        String url = "/jiaochen/pc/customer-manage/pre-sale-customer/style-list";
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
        String url = "/jiaochen/pc/customer-manage/pre-sale-customer/model-list";
        json.put("style_id", style_id);
        return invokeApi(url, json);
    }

}
