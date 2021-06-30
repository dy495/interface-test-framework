package com.haisheng.framework.testng.bigScreen.fengkongdaily.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.LoginPcScene;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.springframework.util.StringUtils;

import java.util.List;

public class RiskControlUtil extends TestCaseCommon {

    private static volatile RiskControlUtil instance = null;
    private static String IpPort;
    private static EnumTestProduce product;
    private final VisitorProxy visitor;

    /**
     * 单例
     *
     * @return ScenarioUtil
     */
    public static synchronized RiskControlUtil getInstance(EnumTestProduce product) {
        if (instance == null) {
            instance = new RiskControlUtil(product);
            IpPort = product.getAddress();
        } else {
            if (RiskControlUtil.product != product) {
                instance = new RiskControlUtil(product);
                IpPort = product.getAddress();
            }
        }
        return instance;
    }

    private RiskControlUtil(EnumTestProduce product) {
        RiskControlUtil.product = product;
        visitor = new VisitorProxy(product);
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
            throw new RuntimeException("path不可为空");
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

    //更换域名
    public void changeIpPort(String ipPort, EnumTestProduce product1) {
        IpPort = ipPort;
        product = product1;
    }

    /**
     * @description:风控登录--手机号验证码登录（0：账号密码，1：验证码 , 2:用户名密码）
     * @author:gly
     * @time:2021/4/1
     */
    public void pcLogin(String username, String password) {
        IScene scene = LoginPcScene.builder().type(0).username(username).password(password).build();
        visitor.login(scene);
    }


    //--------------------------------收银风控-------------

    /**
     * @description:13.1.1 收银风控列表
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_page(String shop_name, String manager_name, String manager_phone, String sort_event_type, Integer sort_event_type_order, Integer page, Integer size) throws Exception {
        String url = "/risk-control/auth/cashier/page";
        String json =
                "{";
        if (shop_name != "") {
            json = json + "\"shop_name\" :\"" + shop_name + "\",\n";
        }
        if (manager_name != "") {
            json = json + "\"manager_name\" :\"" + manager_name + "\",\n";
        }
        if (sort_event_type != "") {
            json = json + "\"sort_event_type\" :\"" + sort_event_type + "\",\n";
        }
        if (sort_event_type_order != null) {
            json = json + "\"sort_event_type_order\" :" + sort_event_type_order + ",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.2 收银追溯
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_traceBack(long shop_id, String date, String order_id, Integer page, Integer size) throws Exception {
        String url = "/risk-control/auth/cashier/trace-back";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n";
        if (date != "") {
            json = json + "\"date\" :\"" + date + "\",\n";
        }
        ;
        if (order_id != "") {
            json = json + "\"order_id\" :\"" + order_id + "\",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.3 收银追溯查看小票详情
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_orderDetail(String order_id) throws Exception {
        String url = "/risk-control/auth/cashier/order-detail";
        String json =
                "{" +
                        "\"order_id\" :\"" + order_id + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.4 收银风控事件列表
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_riskPage(String shop_id, String event_name, String order_id, String order_date, String member_name, String handle_result, String current_state, Integer page, Integer size) throws Exception {
        String url = "/risk-control/auth/cashier/risk-event/page";
        String json =
                "{" +
                        "\"shop_id\" :" + shop_id + ",\n";
        if (event_name != "") {
            json = json + "\"event_name\" :\"" + event_name + "\",\n";
        }
        if (order_id != "") {
            json = json + "\"order_id\" :\"" + order_id + "\",\n";
        }
        if (order_date != "") {
            json = json + "\"order_date\" :\"" + order_date + "\",\n";
        }
        if (member_name != "") {
            json = json + "\"member_name\" :\"" + member_name + "\",\n";
        }
        if (handle_result != "") {
            json = json + "\"handle_result\" :\"" + handle_result + "\",\n";
        }
        if (current_state != "") {
            json = json + "\"current_state\" :\"" + current_state + "\",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.5 风控事件概览
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_riskPage(long id) throws Exception {
        String url = "/risk-control/auth/cashier/risk-event/overview";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.6 风控事件涉及订单列表
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_ordersInvolvedList(long id) throws Exception {
        String url = "/risk-control/auth/cashier/risk-event/orders-involved/list";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.1.7 风控事件处理
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_riskEventHandle(long id, Integer result, String remarks) throws Exception {
        String url = "/risk-control/auth/cashier/risk-event/handle";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"result\" :" + result + ",\n" +
                        "\"remarks\" :\"" + remarks + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }


    /**
     * @description:13.1.8 风控事件处理查看
     * @author: qingqing
     * @time:
     */
    public JSONObject cashier_riskEventView(long id) throws Exception {
        String url = "/risk-control/auth/cashier/risk-event/handle-result";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:13.2.1 风控规则列表
     * @author: qingqing
     * @time:
     */
    public JSONObject risk_controlPage(String name, String type, String shop_name, Integer status, Integer page, Integer size) throws Exception {
        String url = "/risk-control/auth/rule/page";
        String json =
                "{";
        if (name != "") {
            json = json + "\"name\" :\"" + name + "\",\n";
        }
        if (type != "") {
            json = json + "\"type\" :\"" + type + "\",\n";
        }
        if (shop_name != "") {
            json = json + "\"shop_name\" :\"" + shop_name + "\",\n";
        }
        if (status != null) {
            json = json + "\"status\" :" + status + ",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 20.2 获取新增风控规则树结构
     * @author: qingqing
     * @time:
     */
    public JSONObject riskRuleTree() throws Exception {
        String url = "/risk-control/auth/rule/tree";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:13.2.3 新增风控规则
     * @author: qingqing
     * @time:
     */
    public JSONObject riskRuleAdd(String name, String shop_type, JSONObject rule) throws Exception {
        String url = "/risk-control/auth/rule/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"shop_type\" :\"" + shop_type + "\",\n" +
                        "\"rule\" :" + rule + "\n" +
                        "} ";

        return invokeApi(url, JSONObject.parseObject(json), false);
    }

    /**
     * @description:13.2.4 风控规则删除
     * @author: qingqing
     * @time:
     */
    public JSONObject risk_controlDelete(long id) throws Exception {
        String url = "/risk-control/auth/rule/delete";
        String json =
                "{" +
                        "\"id\" :" + id + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    /**
     * @description:13.2.5 风控规则开关（前端传当前字段值即可，后端自动判断更改）
     * @author: qingqing
     * @time:
     */
    public JSONObject risk_controlPageSwitch(long id, Integer status) throws Exception {
        String url = "/risk-control/auth/rule/switch";
        String json =
                "{" +
                        "\"id\" :" + id + ",\n" +
                        "\"status\" :" + status + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.3.1 风控告警规则列表
     * @author: qingqing
     * @time:
     */
    public JSONObject alarm_rulePage(String name, String type, String accept_role, Integer status, Integer page, Integer size) throws Exception {
        String url = "/risk-control/auth/alarm-rule/page";
        String json =
                "{";
        if (name != "") {
            json = json + "\"name\" :\"" + name + "\",\n";
        }
        if (type != "") {
            json = json + "\"type\" :\"" + type + "\",\n";
        }
        if (accept_role != "") {
            json = json + "\"accept_role\" :\"" + accept_role + "\",\n";
        }
        if (status != null) {
            json = json + "\"status\" :" + status + ",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:13.3.2 新建风控告警规则
     * @author: qingqing
     * @time:
     */
    public JSONObject alarm_ruleAdd(String name, String type, List rule_id_list, List accept_role_id_list, String start_time, String end_time, String silent_time) throws Exception {
        String url = "/risk-control/auth/alarm-rule/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"type\" :\"" + type + "\",\n" +
                        "\"rule_id_list\" :" + rule_id_list + ",\n" +
                        "\"accept_role_id_list\" :" + accept_role_id_list + ",\n" +
                        "\"start_time\" :\"" + start_time + "\",\n" +
                        "\"end_time\" :\"" + end_time + "\",\n" +
                        "\"silent_time\" :\"" + silent_time + "\"\n" +
                        "} ";
        return invokeApi(url, JSONObject.parseObject(json), false);
    }

    /**
     * @description: 特殊人员管理 ---黑白名单列表(黑白名单共用)
     * @author: zt
     * @time:
     */
    public JSONObject white_black_list(Integer page, Integer size, String name, String member_id, String customer_id, String type) throws Exception {
        String url = "/risk-control/auth/rule/white-list/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);
        json.put("member_id", member_id);
        json.put("customer_id", customer_id);
        json.put("type", type);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 特殊人员管理 ---黑白名单-删除黑白名单(黑白名单共用)
     * @author: zt
     * @time:
     */
    public JSONObject white_black_delete(String customer_id, String type) throws Exception {
        String url = "/risk-control/auth/rule/black-white-list/delete";
        JSONObject json = new JSONObject();
        json.put("customer_id", customer_id);
        json.put("type", type);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 特殊人员管理 ---黑白名单名单新增列表(黑白名单共用)
     * @author: zt
     * @time:
     */
    public JSONObject white_black_addList(Integer page, Integer size, String name, String member_id, String customer_id, String type) throws Exception {
        String url = "/risk-control/auth/rule/white-list/add/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("name", name);
        json.put("member_id", member_id);
        json.put("customer_id", customer_id);
        json.put("type", type);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 特殊人员管理 ---新增页面顾客详情(黑白名单共用)
     * @author: zt
     * @time:
     */
    public JSONObject white_black_addDetail(String customer_id, String type) throws Exception {
        String url = "/risk-control/auth/rule/black-white-list/add/detail";
        JSONObject json = new JSONObject();
        json.put("customer_id", customer_id);
        json.put("type", type);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 特殊人员管理 ---新增黑白名单(黑白名单共用)
     * @author: zt
     * @time:
     */
    public JSONObject white_black_add(String customer_id, String shop_id, String name, String add_reason, String type) throws Exception {
        String url = "/risk-control/auth/rule/black-white-list/add";
        JSONObject json = new JSONObject();
        json.put("customer_id", customer_id);
        json.put("shop_id", shop_id);
        json.put("name", name);
        json.put("add_reason", add_reason);
        json.put("type", type);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 特殊人员管理 ---操作日志列表(黑白名单共用)
     * @author: zt
     * @time:
     */
    public JSONObject white_black_operate(Integer page, Integer size, String customer_id) throws Exception {
        String url = "/risk-control/auth/rule/black-white-list/operate/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("customer_id", customer_id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 特殊人员管理 ---添加黑名单-风控事件列表
     * @time:
     */
    public JSONObject black_listPage(Integer page, Integer size, String customer_id) throws Exception {
        String url = "/risk-control/auth/rule/black-list/event/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("customer_id", customer_id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:13.4.1 风控告警事件列表
     * @author: qingqing
     */
    public JSONObject alarm_page(String name, String type, String shop_name, String accept_role, Integer page, Integer size) throws Exception {
        String url = "/risk-control/auth/alarm/page";
        String json =
                "{";
        if (name != "") {
            json = json + "\"name\" :\"" + name + "\",\n";
        }
        if (type != "") {
            json = json + "\"type\" :\"" + type + "\",\n";
        }
        if (shop_name != "") {
            json = json + "\"shop_name\" :\"" + shop_name + "\",\n";
        }
        if (accept_role != "") {
            json = json + "\"accept_role\" :\"" + accept_role + "\",\n";
        }
        json = json +
                "\"page\" :" + page + ",\n" +
                "\"size\" :" + size + "\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    //---------------------十一、组织架构接口-----------------


    /**
     * @description:11.1.1 账号管理列表
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountPage(String name, String account, String email, String phone, String role_name, String shop_list, Integer page, Integer size) throws Exception {
        String url = "/risk-control/auth/account/page";
        String json =
                "{";
        if (name != "") {
            json = json + "\"name\" :\"" + name + "\",\n";
        }
        ;
        if (account != "") {
            json = json + "\"account\" :\"" + account + "\",\n";
        }
        ;
        if (email != "") {
            json = json + "\"email\" :\"" + email + "\",\n";
        }
        ;
        if (phone != "") {
            json = json + "\"phone\" :\"" + phone + "\",\n";
        }
        ;
        if (role_name != "") {
            json = json + "\"role_name\" :\"" + role_name + "\",\n";
        }
        ;
        if (shop_list != "") {
            json = json + "\"shop_list\" :\"" + shop_list + "\",\n";
        }
        ;
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
    public JSONObject organizationAccountAdd(String name, String email, String phone, List role_id_list, Integer status, List shop_list, String type) throws Exception {
        String url = "/risk-control/auth/account/add";
        String json =
                "{" +
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
                "\"status\" :" + status + ",\n" +
                "\"shop_list\" :" + shop_list + ",\n" +
                "\"type\" :\"" + type + "\"\n" +
                "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res);
    }

    public JSONObject organizationAccountAddTwo(String account, String name, String number, String leaderUid, String type, String email, String phone, int status, JSONArray roleIdList, JSONArray shopIdList) throws Exception {
        String url = "/risk-control/auth/account/add";
        JSONObject json = new JSONObject();
        json.put("account", account);
        json.put("name", name);
        json.put("number", number);
        json.put("leaderUid", leaderUid);
        json.put("type", type);
        json.put("email", email);
        json.put("phone", phone);
        json.put("status", status);
        json.put("roleIdList", roleIdList);
        json.put("shopIdList", shopIdList);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:11.1.3 账号详情
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountDetail(String account) throws Exception {
        String url = "/risk-control/auth/account/detail";
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
    public JSONObject organizationAccountEdit(String account, String name, String email, String phone, List role_id_list, Integer status, List shop_list, String type) throws Exception {
        String url = "/risk-control/auth/account/edit";
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

    public JSONObject organizationAccountEditTwo(String account, String name, String number, String leaderUid, String type, String email, String phone, int status, JSONArray roleIdList, JSONArray shopIdList) throws Exception {
        String url = "/risk-control/auth/account/edit";
        JSONObject json = new JSONObject();
        json.put("account", account);
        json.put("name", name);
        json.put("number", number);
        json.put("leaderUid", leaderUid);
        json.put("type", type);
        json.put("email", email);
        json.put("phone", phone);
        json.put("status", status);
        json.put("roleIdList", roleIdList);
        json.put("shopIdList", shopIdList);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:11.1.5 账号删除
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationAccountDelete(String account) throws Exception {
        String url = "/risk-control/auth/account/delete";
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
    public JSONObject organizationAccountButtom(String account, Integer status) throws Exception {
        String url = "/risk-control/auth/account/change-status";
        String json =
                "{" +
                        "\"account\" :\"" + account + "\",\n" +
                        "\"status\" :\"" + status + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:上级领导下拉列表
     * @author:
     * @time:
     */
    public JSONObject leaderList() throws Exception {
        String url = "/risk-control/auth/account/leader/list";
        JSONObject json = new JSONObject();
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:权限包列表
     * @author:
     * @time:
     */
    public JSONObject rolePackage(int superior_role_id) throws Exception {
        String url = "/risk-control/auth/role-management/query-permission-map";
        JSONObject json = new JSONObject();
        json.put("superior_role_id", superior_role_id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:权限包列表
     * @author:
     * @time:
     */
    public JSONObject superiorList() throws Exception {
        String url = "/risk-control/auth/role/superior/list";
        JSONObject json = new JSONObject();
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:11.1.7 角色列表
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleList() throws Exception {
        String url = "/risk-control/auth/role/list";
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
    public JSONObject patrolShopList(String district_code) throws Exception {
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
    public JSONObject patrolShopList(String new_password, String old_password) throws Exception {
        String url = "/risk-control/auth/account/change-password";
        String json =
                "{" +
                        "\"new_password\" :\"" + new_password + "\",\n" +
                        "\"old_password\" :\"" + old_password + "\"\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:11.2.1 向SSO服务查询当前门店可用的配置模块和对应的权限列表（参数依赖sso服务，这个是新增接口，后续可能变化）
     * @author: qingqing
     * @time:
     */
    public JSONObject query_permission_map() throws Exception {
        String url = "/risk-control/auth/role-management/query-permission-map";
        String json =
                "{} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.2.2 角色管理列表
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRolePage(String role_name, Integer page, Integer size) throws Exception {
        String url = "/risk-control/auth/role/page";
        String json =
                "{" +
                        "\"role_name\" :\"" + role_name + "\",\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.2.3 新增角色（参数依赖sso服务，这个是新增接口，后续可能变化）
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleAdd(String name, int superiorRoleId, String description, JSONArray module_ids) throws Exception {
        String url = "/risk-control/auth/role/add";
        String json =
                "{" +
                        "\"name\" :\"" + name + "\",\n" +
                        "\"superiorRoleId\" :\"" + superiorRoleId + "\",\n" +
                        "\"description\" :\"" + description + "\",\n" +
                        "\"module_ids\" :" + module_ids + "\n" +

                        "} ";

        return invokeApi(url, JSONObject.parseObject(json), false);


    }


    public JSONObject organizationRoleAddTwo(String roleName, int superiorRoleId, String description, JSONArray moduleIds) throws Exception {
        String url = "/risk-control/auth/role/add";
        JSONObject json = new JSONObject();
        json.put("roleName", roleName);
        json.put("superiorRoleId", superiorRoleId);
        json.put("description", description);
        json.put("moduleIds", moduleIds);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:11.2.4 角色详情
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleDetail(long role_id) throws Exception {
        String url = "/risk-control/auth/role/detail";
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
    public JSONObject organizationRoleEdit(long role_id, String name, String description, JSONArray module_ids) throws Exception {
        String url = "/risk-control/auth/role/edit";
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


    public JSONObject organizationRoleEditTwo(int roleId, int superiorRoleId, String roleName, String description, JSONArray moduleIds) throws Exception {
        String url = "/risk-control/auth/role/edit";
        JSONObject json = new JSONObject();
        json.put("roleId", roleId);
        json.put("superiorRoleId", superiorRoleId);
        json.put("roleName", roleName);
        json.put("description", description);
        json.put("moduleIds", moduleIds);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description:11.2.6 角色删除
     * @author: qingqing
     * @time:
     */
    public JSONObject organizationRoleDelete(long role_id) throws Exception {
        String url = "/risk-control/auth/role/delete";
        String json =
                "{" +
                        "\"role_id\" :" + role_id + "\n" +

                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:11.2.7 角色对应的账号列表
     * @author: qingqing
     * @time:
     */
    public JSONObject AccountRolePage(long role_id, Integer page, Integer size) throws Exception {
        String url = "/risk-control/auth/account/role/page";
        String json =
                "{" +
                        "\"role_id\" :" + role_id + "\n" +
                        "\"page\" :" + page + ",\n" +
                        "\"size\" :" + size + "\n" +
                        "} ";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }


}
