package com.haisheng.framework.testng.bigScreen.jiaochen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.config.EnumAddress;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.springframework.util.StringUtils;
import org.testng.annotations.DataProvider;

import java.util.List;

/**
 * 轿辰接口类
 */
public class ScenarioUtil extends TestCaseCommon {
    private static volatile ScenarioUtil instance = null;
    private static final String IpPort = EnumAddress.JIAOCHEN_DAILY.name();

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
    public void pcLogin(String username, String password) {
        String path = "/jiaochen/pc/login";
        JSONObject object = new JSONObject();
        object.put("username", username);
        object.put("password", password);
        httpPost(path, object, IpPort);
    }

    //app登录
    public void appLogin(String username, String password) {
        String path = "/jiaochen/app/login";
        JSONObject object = new JSONObject();
        object.put("username", username);
        object.put("password", password);
        httpPost(path, object, IpPort);
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

    //pc门店列表
    public JSONObject pcShopList() {
        String path = "/jiaochen/pc/shop-list";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
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
    public JSONObject pcFileUpload(String pic, boolean isPermanent) {
        String path = "/jiaochen/pc/file/upload";
        JSONObject object = new JSONObject();
        object.put("pic", pic);
        object.put("is_permanent", isPermanent);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 列表
    public JSONObject pcReceptionManagePage(int page, int size) {
        String path = "/jiaochen/pc/reception-manage/page";
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        return invokeApi(path, object);
    }

    public JSONObject tryLogin(String userName, String passwd) throws Exception {
        String url = "/jiaochen/pc/login";
        String json = "{\"type\":0, \"username\":\"" + userName + "\",\"password\":\"" + passwd + "\"}";
        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res);
    }

    //pc接待管理 -> 开始接待
    public JSONObject pcStartReception(String customerId, String voucherIdList, String customerName, String customerPhone) {
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
    public JSONObject pcCancelReception(Long receptionId) {
        String path = "/jiaochen/pc/reception-manage/cancel-reception";
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 套餐列表
    public JSONObject pcPackageList(Long customerId) {
        String path = "/jiaochen/pc/reception-manage/package-list";
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        return invokeApi(path, object);
    }

    //pc接待管理 -> 卡券列表
    public JSONObject pcVoucherList(Long customerId) {
        String path = "/jiaochen/pc/reception-manage/voucher-list";
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        return invokeApi(path, object);
    }

    //pc客户管理 -> 客户类型
    public JSONObject pcCustomerType() {
        String path = "/jiaochen/pc/customer-manage/pre-sale-customer/customer-type";
        JSONObject object = new JSONObject();
        return invokeApi(path, object);
    }

    //pc客户管理 -> 售后客户列表
    public JSONObject pcAfterSaleCustomerPage(String customerName, String customerPhone, String createDate, String vehicleChassisCode, Integer page, Integer size) {
        String path = "/jiaochen/pc/customer-manage/after-sale-customer/page";
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("create_date", createDate);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        object.put("page", page);
        object.put("size", size);
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
    public JSONObject organizationAccountPage(String name, String account, String email, String phone, String role_name, String shop_list, Integer page, Integer size) {
        String url = "/patrol/organization/account/page";
        String json =
                "{";
        if (name != "") {
            json = json + "\"name\" :\"" + name + "\",\n";
        }
        if (account != "") {
            json = json + "\"account\" :\"" + account + "\",\n";
        }
        if (email != "") {
            json = json + "\"email\" :\"" + email + "\",\n";
        }
        if (phone != "") {
            json = json + "\"phone\" :\"" + phone + "\",\n";
        }
        if (role_name != "") {
            json = json + "\"role_name\" :\"" + role_name + "\",\n";
        }
        if (shop_list != "") {
            json = json + "\"shop_list\" :\"" + shop_list + "\",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.1.2 新增账号
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountAdd(String name, String email, String phone, List role_id_list, Integer status, List shop_list, String type) {
        String url = "/patrol/organization/account/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n";
        if (email != "") {
            json = json + "\"email\" :\"" + email + "\",\n";
        }
        if (phone != "") {
            json = json + "\"phone\" :\"" + phone + "\",\n";
        }
        json = json +
                "\"role_id_list\" :" + role_id_list + ",\n" +
                "\"status\" :" + status + ",\n" +
                "\"shop_list\" :" + shop_list + ",\n" +
                "\"type\" :\"" + type + "\"\n" +
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
    public JSONObject organizationAccountEdit(String account, String name, String email, String phone, List role_id_list, Integer status, List shop_list, String type) {
        String url = "/patrol/organization/account/edit";
        String json =
                "{" +
                        "\"account\" :\"" + account + "\",\n" +
                        "\"name\" :\"" + name + "\",\n";
        if (email != "") {
            json = json + "\"email\" :\"" + email + "\",\n";
        }
        ;
        if (phone != "") {
            json = json + "\"phone\" :\"" + phone + "\",\n";
        }
        ;
        json = json +

                "\"role_id_list\" :" + role_id_list + ",\n" +
                "\"shop_list\" :" + shop_list + ",\n" +
                "\"type\" :\"" + type + "\"\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:11.1.5 账号删除
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountDelete(String account) {
        String url = "/patrol/organization/account/delete";
        String json =
                "{" +
                        "\"account\" :\"" + account + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:11.1.6 账号状态更改（不是启用就是禁用，点击就更改对应的状态，前端传入当前账号状态就可以，后台判断更改）
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountButtom(String account, Integer status) {
        String url = "/patrol/organization/account/change-status";
        String json =
                "{" +
                        "\"account\" :\"" + account + "\",\n" +
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
        String url = "/patrol/organization/role/page";
        String json =
                "{" +
                        "\"role_name\" :\"" + role_name + "\",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject organizationRolePage(Integer page, Integer size) {
        String url = "/patrol/organization/role/page";
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
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleAdd(String name, String description, JSONArray module_id, Boolean checkcode) {
        String url = "/patrol/organization/role/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"description\" :\"" + description + "\",\n" +
                        "\"module_id\" :" + module_id + "\n" +

                        "} ";

        return invokeApi(url, JSONObject.parseObject(json), checkcode);


    }

    public JSONObject organizationRoleAdd(String name, String description, JSONArray module_id) {
        String url = "/patrol/organization/role/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"description\" :\"" + description + "\",\n" +
                        "\"module_id\" :" + module_id + "\n" +

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
        String url = "/patrol/organization/role/edit";
        String json =
                "{" +
                        "\"role_id\" :" + role_id + ",\n" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"description\" :\"" + description + "\",\n" +
                        "\"module_ids\" :" + module_ids + "\n" +

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
        String url = "/patrol/organization/role/delete";
        String json =
                "{" +
                        "\"role_id\" :" + role_id + "\n" +

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
     * @description :app上小程序码
     * @date :2020/11/19 14:24
     */
    public JSONObject apperCOde() {
        String url = "";
        JSONObject json = new JSONObject();
        return invokeApi(url, json);
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

    //角色权限列表
    @DataProvider(name = "LIMITID")
    public static Object[][] limitid() {
        return new Integer[][]{
                {1, 2, 3, 4},
                {2, 2, 3, 4},
        };
    }



    /**
     * @description:品牌管理-品牌列表分页
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject brandPage(int page, int size,  String name, String first_letter) {
        String url = "/jiaochen/pc/brand/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if ( name != ""){
            json.put("name", name);
        }
        if (first_letter != ""){
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

    /**
     * @description:品牌管理-编辑品牌
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject editBrand(Long id, String name, String logo) {
        String url = "/jiaochen/pc/brand/edit";
        JSONObject json = new JSONObject();
        json.put("id", id);
        if ( name != ""){
            json.put("name", name);
        }
        if (logo != ""){
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

    public JSONObject carStylePage(int page, int size,  Long brand_id, String name) {
        String url = "/jiaochen/pc/brand/car-style/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("brand_id", brand_id);
        if ( name != ""){
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

    public JSONObject delCarStyle(Long id, Long brand_id, String manufacturer, String name, String online_time) {
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

    public JSONObject carModelPage(int page, int size,  Long brand_id, Long style_id, String name, String year, String status) { //预约状态ENABLE（开启） DISABLE（关闭）
        String url = "/jiaochen/pc/brand/car-style/car-model/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("brand_id", brand_id);
        json.put("style_id", style_id);
        if ( name != ""){
            json.put("name", name);
        }
        if ( year != ""){
            json.put("year", year);
        }
        if ( status != ""){
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

    public JSONObject addCarModel( Long brand_id, Long style_id, String name, String year, String status) { //预约状态ENABLE（开启） DISABLE（关闭）
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

    /**
     * @description:品牌管理-编辑品牌车系车型
     * @author: lxq
     * @time: 2020-11-24
     */

    public JSONObject editCarModel( Long id, Long brand_id, Long style_id, String name, String year, String status) { //预约状态ENABLE（开启） DISABLE（关闭）
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

    public JSONObject delCarModel( Long id) {
        String url = "/jiaochen/pc/brand/car-style/car-model/delete";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String result = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(result).getJSONObject("data");
    }







}
