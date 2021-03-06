package com.haisheng.framework.testng.bigScreen.fengkongdaily.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.orderParm;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.RiskBusinessTypeEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.RuleEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.RuleTypeEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.routerEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventHandleScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.AddScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.DeleteScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.SwitchScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.Response;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


public class CommonUsedUtil {
    PublicParam pp = new PublicParam();
    DateTimeUtil dt = new DateTimeUtil();
    private VisitorProxy visitor;
    private final routerEnum router;

    public CommonUsedUtil(VisitorProxy visitor, routerEnum router) {
        this.visitor = visitor;
        this.router = router;
    }

    /**
     * ??????????????????
     */
    public String getStartDate() {
        // ???????????????
        return DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
    }

    /**
     * ??????????????????
     */
    public String getDate() {
        // ???????????????
        return DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
    }

    /**
     * ??????????????????+10???
     */
    public String getEndDate() {
        Date endDate = DateTimeUtil.addDay(new Date(), 10);
        return DateTimeUtil.getFormat(endDate, "yyyy-MM-dd HH:mm");
    }

    /**
     * ???????????????????????????
     */
    public String getDateTime(int day) {
        Date endDate = DateTimeUtil.addDay(new Date(), day);
        return DateTimeUtil.getFormat(endDate, "yyyy-MM-dd HH:mm");
    }

    /**
     * ??????????????????rule
     */
    public JSONObject getRuleObject(String type, String item, String dayRange, String orderQuantityUpperLimit) {
        JSONObject object = new JSONObject();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("DAY_RANGE", dayRange);
        parameters.put("ORDER_QUANTITY_UPPER_LIMIT", orderQuantityUpperLimit);
        object.put("type", type);
        object.put("item", item);
        object.put("parameters", parameters);
        return object;
    }


    /**
     * ??????????????????----??????????????????????????????????????????
     *
     * @param type ??????????????????
     */
    public Long getRuleAdd(String type) {
        //???????????????
        List<String> shopIds = new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //????????????type
        JSONObject rule = new JSONObject();
        rule.put("type", type);
        //??????????????????
        List<String> businessTypeArray = new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //??????????????????
        String name = type.equals("BLACK_LIST") ? pp.blackName : pp.observeName;
        IScene scene = AddScene.builder()
                .name(name)
                .rule(rule)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //??????????????????
                .build();
        Long id = scene.visitor(visitor).execute().getLong("id");
        System.err.println("id:" + id);
        return id;
    }

    /**
     * ??????????????????---????????????---????????????
     *
     * @param dayRange   ????????????
     * @param upperLimit ????????????
     */
    public Response getCashierOrderRuleAdd(String dayRange, String upperLimit) {
        //???????????????
        List<String> shopIds = new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //??????????????????
        JSONObject object = new JSONObject();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("DAY_RANGE", dayRange);
        parameters.put("ORDER_QUANTITY_UPPER_LIMIT", upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.RISK_SINGLE_MEMBER_ORDER_QUANTITY.getType());
        object.put("parameters", parameters);
        //??????????????????
        List<String> businessTypeArray = new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //??????????????????
        IScene scene = AddScene.builder()
                .name("????????????" + (int) (Math.random() * 10000))
                .rule(object)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //??????????????????
                .build();
        return scene.visitor(visitor).getResponse();
    }

    /**
     * ??????????????????---????????????---????????????
     */
    public Response getCashierUnmannedRuleAdd() {
        //???????????????
        List<String> shopIds = new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //????????????type
        //????????????type
        JSONObject rule = new JSONObject();
        rule.put("type", RuleEnum.CASHIER.getType());
        rule.put("item", RuleTypeEnum.UNMANNED_ORDER.getType());
        //??????????????????
        List<String> businessTypeArray = new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //??????????????????
        IScene scene = AddScene.builder()
                .name("????????????" + (int) (Math.random() * 10000))
                .rule(rule)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //??????????????????
                .build();
        return scene.visitor(visitor).getResponse();
    }

    /**
     * ??????????????????---????????????---????????????????????????
     */
    public Response getCashierEmployeeRuleAdd(String timeRange, String upperLimit) {
        //???????????????
        List<String> shopIds = new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //??????????????????
        JSONObject object = new JSONObject();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("TIME_RANGE", timeRange);
        parameters.put("EMPLOYEE_ORDER_UPPER_LIMIT", upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.EMPLOYEE_ORDER.getType());
        object.put("parameters", parameters);
        //??????????????????
        List<String> businessTypeArray = new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //??????????????????
        IScene scene = AddScene.builder()
                .name("????????????" + (int) (Math.random() * 10000))
                .rule(object)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //??????????????????
                .build();
        return scene.visitor(visitor).getResponse();
    }

    /**
     * ??????????????????---????????????---???????????????????????????????????????,???????????????
     */
    public Response getCashierCarRuleAdd(String upperLimit) {
        //???????????????
        List<String> shopIds = new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //??????????????????
        JSONObject object = new JSONObject();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("CAR_LIMIT", upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.RISK_SINGLE_MEMBER_CAR_QUANTITY.getType());
        object.put("parameters", parameters);
        //??????????????????
        List<String> businessTypeArray = new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //??????????????????
        IScene scene = AddScene.builder()
                .name("????????????" + (int) (Math.random() * 10000))
                .rule(object)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //??????????????????
                .build();
        return scene.visitor(visitor).getResponse();
    }

    /**
     * ??????????????????---????????????---????????????????????????????????????,???????????????
     */
    public Response getCashierMemberRuleAdd(String upperLimit) {
        //???????????????
        List<String> shopIds = new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //??????????????????
        JSONObject object = new JSONObject();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("MEMBER_LIMIT", upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.RISK_SINGLE_CAR_TRANSACTION_QUANTITY.getType());
        object.put("parameters", parameters);
        //??????????????????
        List<String> businessTypeArray = new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //??????????????????
        IScene scene = AddScene.builder()
                .name("????????????" + (int) (Math.random() * 10000))
                .rule(object)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //??????????????????
                .build();
        return scene.visitor(visitor).getResponse();
    }


    /**
     * ??????????????????
     */
    public String ruleDelete(Long ruleId) {
        IScene scene = DeleteScene.builder().id(ruleId).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * ??????????????????
     *
     * @param status 1:??????  0?????????
     */
    public void ruleSwitch(Long ruleId, int status) {
        IScene scene = SwitchScene.builder().id(ruleId).status(status).build();
        scene.visitor(visitor).execute();
    }

    /**
     * ????????????????????????
     *
     * @param realTime???????????????????????????
     * @param silentTime???????????????
     * @param type?????????????????????
     * @param ruleIdList???????????????ID??????
     * @param acceptRoleIdList????????????Id??????
     */
    public IScene getAlarmRuleAdd(Boolean realTime, Long silentTime, String type, List<Long> ruleIdList, List<Long> acceptRoleIdList, String name) {
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.AddScene.builder()
                .name(name)
                .type(type)
                .ruleIdList(ruleIdList)
                .acceptRoleIdList(acceptRoleIdList)
                .startTime("09:00")
                .endTime("18:00")
                .realTime(realTime)
                .silentTime(silentTime)
                .build();
        return scene;
    }

    /**
     * ????????????????????????
     *
     * @param realTime???????????????????????????
     * @param silentTime???????????????
     * @param type?????????????????????
     */
    public String getAlarmRuleAdd(Boolean realTime, Long silentTime, String type, String name) {
        //???????????????ID
        List<Long> ruleIdList = new ArrayList<>();
        //??????????????????????????????????????????????????????????????????
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).type(type).build();
        JSONArray list = scene.visitor(visitor).execute().getJSONArray("list");
        for (int i = 0; i < (Math.min(list.size(), 3)); i++) {
            Long id = list.getJSONObject(i).getLong("id");
            ruleIdList.add(id);
        }
        //?????????ID
        List<Long> acceptRoleIdList = new ArrayList<>();
        acceptRoleIdList.add(4945L);
        acceptRoleIdList.add(5030L);
        //????????????????????????
        return getAlarmRuleAdd(realTime, silentTime, type, ruleIdList, acceptRoleIdList, name).visitor(visitor).getResponse().getMessage();
    }

    /**
     * ????????????????????????
     *
     * @param realTime???????????????????????????
     * @param silentTime???????????????
     * @param type?????????????????????
     * @param ruleIdList???????????????ID??????
     * @param acceptRoleIdList????????????Id??????
     */
    public IScene getAlarmRuleEdit(Long id, Boolean realTime, Long silentTime, String type, List<Long> ruleIdList, List<Long> acceptRoleIdList) {
        return com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.EditScene.builder()
                .id(id)
                .name(pp.AlarmEditName)
                .type(type)
                .ruleIdList(ruleIdList)
                .acceptRoleIdList(acceptRoleIdList)
                .startTime(getDateTime(0))
                .endTime(getDateTime(1))
                .realTime(realTime)
                .silentTime(silentTime)
                .build();
    }

    /**
     * ????????????????????????
     *
     * @param status 1:??????  0?????????
     */
    public void getAlarmRuleSwitch(Long ruleId, int status) {
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.SwitchScene.builder().id(ruleId).status(status).build();
        scene.visitor(visitor).execute();
    }

    /**
     * ????????????????????????
     */
    public String getAlarmRuleDel(Long ruleId) {
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.DeleteScene.builder().id(ruleId).build();
        return scene.visitor(visitor).getResponse().getMessage();
    }

    /**
     * ??????????????????
     *
     * @param id
     * @param result      1???????????????  0???????????????
     * @param remarks     ??????
     * @param customerIds ??????????????????
     */
    public Response getRiskEventHandle(Long id, int result, String remarks, JSONArray customerIds) {
        IScene scene = RiskEventHandleScene.builder()
                .id(id)
                .result(result)
                .remarks(remarks)
                .customerIds(customerIds)
                .build();
        return scene.visitor(visitor).getResponse();
    }


    /**
     * ???????????????
     *
     * @param status 1:??????  0?????????
     */
    public Response getStaffStatusChange(String id, String status) {
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.StatusChangeScene.builder().id(id).status(status).build();
        return scene.visitor(visitor).getResponse();
    }

    /**
     * ??????????????????
     */
    public String createAccountNumber(String name, String phone) {
        //????????????
        JSONArray roleList = new JSONArray();
        JSONObject object = new JSONObject();
        List<JSONObject> shopList = new ArrayList<>();
        JSONObject shopObject = new JSONObject();
        shopObject.put("shop_id", "43072");
        shopObject.put("shop_name", "AI-Test(??????????????????)");
        shopList.add(shopObject);
        object.put("role_id", "4944");
        object.put("role_name", "???????????????");
        object.put("shop_list", shopList);
        roleList.add(object);
        IScene scene2 = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.AddScene.builder()
                .name(name)
                .phone(phone)
                .roleList(roleList)
                .build();
        String message = scene2.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * ?????????????????????????????????
     */
    public String getEditAccountNumber(String id, String name, String phone) {
        //????????????
        JSONArray roleList = new JSONArray();
        JSONObject object = new JSONObject();
        List<JSONObject> shopList = new ArrayList<>();
        JSONObject shopObject = new JSONObject();
        shopObject.put("shop_id", "43072");
        shopObject.put("shop_name", "AI-Test(??????????????????)");
        shopList.add(shopObject);
        object.put("role_id", "4944");
        object.put("role_name", "???????????????");
        object.put("shop_list", shopList);
        roleList.add(object);
        //????????????
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.EditScene.builder()
                .id(id)
                .name(name)
                .phone(phone)
                .roleList(roleList)
                .build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * ?????????????????????????????????,??????roleID
     */
    public Long getEditAccountNumberReturn(String id, String name, String phone) {
        //??????????????????---?????????????????????????????????
        getAddRole(pp.roleName, pp.descriptionRole);
        //??????????????????ID
        Long roleId = authRoleNameTransId(pp.roleName);
        //????????????
        JSONArray roleList = new JSONArray();
        JSONObject object = new JSONObject();
        List<JSONObject> shopList = new ArrayList<>();
        JSONObject shopObject = new JSONObject();
        shopObject.put("shop_id", "43072");
        shopObject.put("shop_name", "AI-Test(??????????????????)");
        shopList.add(shopObject);
        object.put("role_id", roleId);
        object.put("role_name", pp.roleName);
        object.put("shop_list", shopList);
        roleList.add(object);
        //????????????
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.EditScene.builder()
                .id(id)
                .name(name)
                .phone(phone)
                .roleList(roleList)
                .build();
        scene.visitor(visitor).getResponse().getMessage();
        return roleId;
    }


    /**
     * ????????????
     */
    public void getAddRole(String name, String descriptionRole) {
        //????????????
        JSONArray authIds = new JSONArray();
        authIds.add(232);
        authIds.add(233);
        authIds.add(234);
        authIds.add(235);
        authIds.add(236);
        authIds.add(237);
        authIds.add(238);
        authIds.add(239);
        //??????????????????---?????????????????????????????????
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.AddScene.builder()
                .name(name)
                .parentRoleId(4944)
                .authList(authIds)
                .description(descriptionRole)
                .build();
        JSONObject response = scene.visitor(visitor).execute();
    }

    /**
     * ????????????
     */
    public String getDelRole(Long roleId) {
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.DeleteScene.builder().id(roleId).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * ????????????
     */
    public String getEditRole(Long roleId, String name, String descriptionRole) {
        //????????????
        JSONArray authIds = new JSONArray();
        authIds.add(232);
        authIds.add(233);
        authIds.add(234);
        //?????????????????????????????????
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.EditScene.builder()
                .id(roleId)
                .name(name)
                .parentRoleId(4944)
                .authList(authIds)
                .description(descriptionRole)
                .build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * ??????????????????
     */
    public String getRiskPersonDel(String customerId, String type) {
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.DeleteScene.builder().customerId(customerId).type(type).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * ??????????????????
     */

    public String getRiskPersonAdd(String customerId, List<String> customerIds, String type) {
        //??????????????????--??????????????????
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.AddScene.builder().customerId(customerId).customerIds(customerIds).type(type).build();
        String message = scene.visitor(visitor).getResponse().getMessage();
        return message;
    }

    /**
     * ????????????
     *
     * @param shopId           ??????ID
     * @param transId          ??????ID
     * @param carVehicleNumber ????????? haode
     * @param userId           ??????ID
     * @param openId           ??????ID
     */
    public String getCreateOrder(String shopId, String transId, String userId, String openId, String carVehicleNumber) throws Exception {
        String post = "";
        for (int i = 0; i < 1; i++) {
            final String NUMBER = ".";
            final String ALGORITHM = "HmacSHA256";
            HttpClient client = null;
            try {
                client = HCB.custom()
                        .pool(50, 10)
                        .retry(3).build();
            } catch (HttpProcessException e) {
                e.printStackTrace();
            }
            String timestamp = "" + System.currentTimeMillis();
            String uid = "uid_ef6d2de5";
            String appId = "49998b971ea0";
            String ak = "3fdce1db0e843ee0";
            String router = "/business/precipitation/TRANS_INFO_RECEIVE/v1.0";
            String nonce = UUID.randomUUID().toString();
            String sk = "5036807b1c25b9312116fd4b22c351ac";
            // java????????????
            String requestUrl = "http://dev.api.winsenseos.com/retail/api/data/biz";
            // 1. ???????????????(uid???app_id???ak???router???timestamp???nonce)????????????????????????(.)??????????????????????????????
            String signStr = uid + NUMBER + appId + NUMBER + ak + NUMBER + router + NUMBER + timestamp + NUMBER + nonce;
            // 2. ??????HmacSHA256????????????, ?????????????????????sk?????????????????????. ????????????????????????????????????????????????,??????byte??????
            Mac sha256Hmac = Mac.getInstance(ALGORITHM);
            SecretKeySpec encodeSecretKey = new SecretKeySpec(sk.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            sha256Hmac.init(encodeSecretKey);
            byte[] hash = sha256Hmac.doFinal(signStr.getBytes(StandardCharsets.UTF_8));
            // 3. ???2.??????????????????,???????????????base64??????, ?????????????????????
            String auth = Base64.getEncoder().encodeToString(hash);
            Header[] headers = HttpHeader.custom()
                    .other("Accept", "application/json")
                    .other("Content-Type", "application/json;charset=utf-8")
                    .other("timestamp", timestamp)
                    .other("nonce", nonce)
                    .other("ExpiredTime", "50 * 1000")
                    .other("Authorization", auth)
                    .build();
            String transTime = "" + System.currentTimeMillis();
            //ONLINE   SCAN
            String str = "{\n" +
                    "  \"uid\": \"uid_ef6d2de5\",\n" +
                    "  \"app_id\": \"49998b971ea0\",\n" +
                    "  \"request_id\": \"5d45a085-8774-4jd0-943e-ded373ca6a919987\",\n" +
                    "  \"version\": \"v1.0\",\n" +
                    "  \"router\": \"/business/precipitation/TRANS_INFO_RECEIVE/v1.0\",\n" +
                    "  \"data\": {\n" +
                    "    \"biz_data\":  {\n" +
                    "        \"shop_id\": \"" + shopId + "\",\n" +
                    "        \"trans_id\": \"" + transId + "\" ,\n" +
                    "        \"trans_time\": \"" + transTime + "\" ,\n" +
                    "        \"trans_type\": [\n" +
                    "            \"SCAN\"\n" +
                    "        ],\n" +
                    "        \"user_id\": \"" + userId + "\" ,\n" +
                    "        \"total_price\": 1800,\n" +
                    "        \"real_price\": 1500,\n" +
                    "        \"shopType\": \"SHOP_TYPE\",\n" +
                    "        \"orderNumber\": \"13444894484\",\n" +
                    "        \"memberName\":\"???????????????\",\n" +
                    "        \"memberPhone\":\"15789033456\",\n" +
                    "        \"receipt_type\":\"????????????\",\n" +
                    "        \"open_id\":\"" + openId + "\"," +
                    "        \"posId\": \"pos-1234586789\",\n" +
                    "        \"commodityList\": [\n" +
                    "            {\n" +
                    "                \"commodityId\": \"ff1234567890\",\n" +
                    "                \"commodity_name\":\"???????????????\",\n" +
                    "                \"unit_price\": 500000,\n" +
                    "                \"num\": 4\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"commodityId\": \"model991221313\",\n" +
                    "                \"commodity_name\":\"?????????model3\",\n" +
                    "                \"unit_price\": 300000,\n" +
                    "                \"num\": 4\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"trans_business_params\":{\n" +
                    "               \"car_plate\":\"???A11111\",\n" +
                    "               \"car_vehicle_number\":\"" + carVehicleNumber + "\",\n" +
//                     "                \"business_type\":\"FIRST_INSPECTION\",\n" +
                    "                \"business_order_id\":\"27389182\"\n" +
                    "    }    }\n" +
                    "  }\n" +
                    "}";

            JSONObject jsonObject = JSON.parseObject(str);
            System.out.println("transId:" + transId);
            System.out.println(str);
            HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);
            post = HttpClientUtil.post(config);
        }
        return post;
    }


    public String getCreateOrderOnline(String shopId, String transId, String userId, String openId, String carVehicleNumber) throws Exception {
        String post = "";
        for (int i = 0; i < 1; i++) {
            final String NUMBER = ".";
            final String ALGORITHM = "HmacSHA256";
            HttpClient client = null;
            try {
                client = HCB.custom()
                        .pool(50, 10)
                        .retry(3).build();
            } catch (HttpProcessException e) {
                e.printStackTrace();
            }
            String timestamp = "" + System.currentTimeMillis();

            String uid = router.getUid();
            String appId = router.getAppid();
            String ak = router.getAk();
            String sk = router.getSk();
            String requestUrl = router.getRequestUrl();

            String router = "/business/precipitation/TRANS_INFO_RECEIVE/v1.0";
            String nonce = UUID.randomUUID().toString();
            // java????????????
            // 1. ???????????????(uid???app_id???ak???router???timestamp???nonce)????????????????????????(.)??????????????????????????????
            String signStr = uid + NUMBER + appId + NUMBER + ak + NUMBER + router + NUMBER + timestamp + NUMBER + nonce;
            // 2. ??????HmacSHA256????????????, ?????????????????????sk?????????????????????. ????????????????????????????????????????????????,??????byte??????
            Mac sha256Hmac = Mac.getInstance(ALGORITHM);
            SecretKeySpec encodeSecretKey = new SecretKeySpec(sk.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            sha256Hmac.init(encodeSecretKey);
            byte[] hash = sha256Hmac.doFinal(signStr.getBytes(StandardCharsets.UTF_8));
            // 3. ???2.??????????????????,???????????????base64??????, ?????????????????????
            String auth = Base64.getEncoder().encodeToString(hash);
            Header[] headers = HttpHeader.custom()
                    .other("Accept", "application/json")
                    .other("Content-Type", "application/json;charset=utf-8")
                    .other("timestamp", timestamp)
                    .other("nonce", nonce)
                    .other("ExpiredTime", "50 * 1000")
                    .other("Authorization", auth)
                    .build();
            String transTime = "" + System.currentTimeMillis();
            //ONLINE   SCAN
            String str = "{\n" +
                    "   \"uid\": \"" + uid + "\",\n" +
                    "   \"app_id\": \"" + appId + "\",\n" +
                    "  \"request_id\": \"5d45a085-8774-4jd0-943e-ded373ca6a919987\",\n" +
                    "  \"version\": \"v1.0\",\n" +
                    "  \"router\": \"/business/precipitation/TRANS_INFO_RECEIVE/v1.0\",\n" +
                    "  \"data\": {\n" +
                    "    \"biz_data\":  {\n" +
                    "        \"shop_id\": \"" + shopId + "\",\n" +
                    "        \"trans_id\": \"" + transId + "\" ,\n" +
                    "        \"trans_time\": \"" + transTime + "\" ,\n" +
                    "        \"trans_type\": [\n" +
                    "            \"SCAN\"\n" +
                    "        ],\n" +
                    "        \"user_id\": \"" + userId + "\" ,\n" +
                    "        \"total_price\": 1800,\n" +
                    "        \"real_price\": 1500,\n" +
                    "        \"shopType\": \"SHOP_TYPE\",\n" +
                    "        \"orderNumber\": \"13444894484\",\n" +
                    "        \"memberName\":\"???????????????\",\n" +
                    "        \"memberPhone\":\"15789033456\",\n" +
                    "        \"receipt_type\":\"????????????\",\n" +
                    "        \"open_id\":\"" + openId + "\"," +
                    "        \"posId\": \"pos-1234586789\",\n" +
                    "        \"commodityList\": [\n" +
                    "            {\n" +
                    "                \"commodityId\": \"ff1234567890\",\n" +
                    "                \"commodity_name\":\"???????????????\",\n" +
                    "                \"unit_price\": 500000,\n" +
                    "                \"num\": 4\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"commodityId\": \"model991221313\",\n" +
                    "                \"commodity_name\":\"?????????model3\",\n" +
                    "                \"unit_price\": 300000,\n" +
                    "                \"num\": 4\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"trans_business_params\":{\n" +
                    "               \"car_plate\":\"???A11111\",\n" +
                    "               \"car_vehicle_number\":\"" + carVehicleNumber + "\",\n" +
//                    "               \"business_type\":"+business_type+",\n" +
//                    "                \"business_type\":\"GOODS_PAY\",\n" +
                    "                \"business_order_id\":\"27389182\"\n" +
                    "    }    }\n" +
                    "  }\n" +
                    "}";
            JSONObject jsonObject = JSON.parseObject(str);
            System.out.println("str:" + str);
            HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);
            post = HttpClientUtil.post(config);
        }
        return post;
    }

    public String getCreateOrder3(orderParm orderParm) throws Exception {
        String post = "";
        for (int i = 0; i < 1; i++) {
            final String NUMBER = ".";
            final String ALGORITHM = "HmacSHA256";
            HttpClient client = null;
            try {
                client = HCB.custom()
                        .pool(50, 10)
                        .retry(3).build();
            } catch (HttpProcessException e) {
                e.printStackTrace();
            }
            String timestamp = "" + System.currentTimeMillis();
//            String timestamp = "1620454938752";

            String uid = router.getUid();
            String appId = router.getAppid();
            String ak = router.getAk();
            String sk = router.getSk();
            String requestUrl = router.getRequestUrl();

            String router = "/business/precipitation/TRANS_INFO_RECEIVE/v1.0";
            String nonce = UUID.randomUUID().toString();
            // java????????????
            // 1. ???????????????(uid???app_id???ak???router???timestamp???nonce)????????????????????????(.)??????????????????????????????
            String signStr = uid + NUMBER + appId + NUMBER + ak + NUMBER + router + NUMBER + timestamp + NUMBER + nonce;
            // 2. ??????HmacSHA256????????????, ?????????????????????sk?????????????????????. ????????????????????????????????????????????????,??????byte??????
            Mac sha256Hmac = Mac.getInstance(ALGORITHM);
            SecretKeySpec encodeSecretKey = new SecretKeySpec(sk.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            sha256Hmac.init(encodeSecretKey);
            byte[] hash = sha256Hmac.doFinal(signStr.getBytes(StandardCharsets.UTF_8));
            // 3. ???2.??????????????????,???????????????base64??????, ?????????????????????
            String auth = Base64.getEncoder().encodeToString(hash);
            Header[] headers = HttpHeader.custom()
                    .other("Accept", "application/json")
                    .other("Content-Type", "application/json;charset=utf-8")
                    .other("timestamp", timestamp)
                    .other("nonce", nonce)
                    .other("ExpiredTime", "50 * 1000")
                    .other("Authorization", auth)
                    .build();
//            String transTime = "" + LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            String transTime = "" + System.currentTimeMillis();
            //ONLINE   SCAN
            String str = "{\n" +
                    "   \"uid\": \"" + uid + "\",\n" +
                    "   \"app_id\": \"" + appId + "\",\n" +
                    "  \"request_id\": \"5d45a085-8774-4jd0-943e-ded373ca6a919987\",\n" +
                    "  \"version\": \"v1.0\",\n" +
                    "  \"router\": \"/business/precipitation/TRANS_INFO_RECEIVE/v1.0\",\n" +
                    "  \"data\": {\n" +
                    "    \"biz_data\":  {\n" +
                    "        \"shop_id\": \"" + orderParm.shopId + "\",\n" +
                    "        \"trans_id\": \"" + orderParm.transId + "\" ,\n" +
                    "        \"trans_time\": \"" + transTime + "\" ,\n" +
                    "        \"trans_type\": [\n" +
                    "            \"" + orderParm.type + "\"\n" +
                    "        ],\n" +
                    "        \"user_id\": \"" + orderParm.userId + "\" ,\n" +
                    "        \"total_price\": 1800,\n" +
                    "        \"real_price\": 1500,\n" +
                    "        \"shopType\": \"SHOP_TYPE\",\n" +
                    "        \"orderNumber\": \"13444894484\",\n" +
                    "        \"memberName\":\"???????????????\",\n" +
                    "        \"memberPhone\":\"15789033456\",\n" +
                    "        \"receipt_type\":\"????????????\",\n" +
                    "        \"open_id\":\"" + orderParm.openId + "\"," +
                    "        \"posId\": \"pos-1234586789\",\n" +
                    "        \"commodityList\": [\n" +
                    "            {\n" +
                    "                \"commodityId\": \"ff1234567890\",\n" +
                    "                \"commodity_name\":\"???????????????\",\n" +
                    "                \"unit_price\": 500000,\n" +
                    "                \"num\": 4\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"commodityId\": \"model991221313\",\n" +
                    "                \"commodity_name\":\"?????????model3\",\n" +
                    "                \"unit_price\": 300000,\n" +
                    "                \"num\": 4\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"trans_business_params\":{\n" +
                    "               \"car_plate\":\"???A11111\",\n" +
                    "               \"car_vehicle_number\":\"" + orderParm.carVehicleNumber + "\",\n" +
                    "               \"business_type\":" + orderParm.business_type + ",\n" +
                    "                \"business_order_id\":\"27389182\"\n" +
                    "    }    }\n" +
                    "  }\n" +
                    "}";

            JSONObject jsonObject = JSON.parseObject(str);
            System.out.println("transId:" + orderParm.transId);
            System.out.println("str:" + str);
            HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);
            post = HttpClientUtil.post(config);
        }
        return post;
    }

    public Integer riskTotal() {
        IScene pageScene = PageScene.builder().page(1).size(10).shopName(router.getShopName()).build();
        JSONArray data = pageScene.visitor(visitor).execute().getJSONArray("list");
        Integer cashierTolta = data.getJSONObject(0).getInteger("risk_total");  //?????????
        return cashierTolta;
    }


    public static void main(String[] args) {
        String transTime = "" + LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(transTime);
    }

    /**
     * ?????????????????????17???
     */
    public String carVehicleNumberCheck() {
        //????????????17????????????
        String carVehicleNumber = "AAAAAAAAAA" + CommonUtil.getRandom(7);
        System.out.println("carVehicleNumber2222:" + carVehicleNumber);
        if (carVehicleNumber.length() != 17) {
            carVehicleNumber = "AAAAAAAAAA" + CommonUtil.getRandom(7);
            System.out.println("carVehicleNumber111:" + carVehicleNumber);
        }
        String carNumber = carVehicleNumber.length() != 17 ? carVehicleNumberCheck() : carVehicleNumber;
        System.out.println("----carNumber-----" + carNumber);
        return carNumber;
    }

    /**
     * ??????????????????????????????ID
     */
    public Long authRoleNameTransId(String name) {
        Long id = 0L;
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(1).size(10).build();
        JSONObject response = scene.visitor(visitor).execute();
        int pages = response.getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            JSONArray list = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(page).size(10).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String name1 = list.getJSONObject(i).getString("name");
                if (name1.equals(name)) {
                    id = list.getJSONObject(i).getLong("id");
                    System.err.println("-------id:---" + id);
                }
            }
        }
        return id;
    }

    /**
     * ????????????????????????????????????ID
     */
    public String authStaffTransId(String phone) {
        String id = "";
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build();
        JSONObject response = scene.visitor(visitor).execute();
        int pages = response.getInteger("pages") > 10 ? 10 : response.getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            JSONArray list = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(page).size(10).build().visitor(visitor).execute().getJSONArray("list");
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String phone1 = list.getJSONObject(i).getString("phone");
                    System.err.println(phone1);
                    if (phone.equals(phone1)) {
                        id = list.getJSONObject(i).getString("id");
                    }
                }
            }
        }
        return id;
    }

    /**
     * ????????????id???????????????????????????
     */
    public JSONObject staffIdTransResponse(String id) {
        JSONObject res = null;
        JSONObject response = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().visitor(visitor).execute();
        int numBefore = response.getInteger("total");
        int pages = response.getInteger("pages");
        for (int page = 1; page <= pages; page++) {
            JSONArray list = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(page).size(10).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String id1 = list.getJSONObject(i).getString("id");
                if (id1.equals(id)) {
//                    createTime=list.getJSONObject(i).getString("create_time");
                    res = list.getJSONObject(i);
                }
            }
        }
        return res;
    }


}
