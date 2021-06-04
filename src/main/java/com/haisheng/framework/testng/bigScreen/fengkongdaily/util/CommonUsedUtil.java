package com.haisheng.framework.testng.bigScreen.fengkongdaily.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
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
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
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
    PublicParam pp=new PublicParam();
    DateTimeUtil dt =new DateTimeUtil();
    private VisitorProxy visitor;
    private UserUtil user;
    private final routerEnum router;

    public CommonUsedUtil(VisitorProxy visitor, routerEnum router) {
        this.visitor = visitor;
        this.user=new UserUtil(visitor);
        this.router = router;
    }

    /**
     * 获取当前时间
     */
    public String getStartDate() {
        // 格式化时间
        return DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
    }
    /**
     * 获取当前时间
     */
    public String getDate() {
        // 格式化时间
        return DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd");
    }

    /**
     * 获取当前时间+10天
     */
    public String getEndDate() {
        Date endDate = DateTimeUtil.addDay(new Date(), 10);
        return DateTimeUtil.getFormat(endDate, "yyyy-MM-dd HH:mm");
    }
    /**
     * 获取任意一天的日期
     */
    public String getDateTime(int day) {
        Date endDate = DateTimeUtil.addDay(new Date(), day);
        return DateTimeUtil.getFormat(endDate, "yyyy-MM-dd HH:mm");
    }

    /**
     * 风控规则中的rule
     */
    public JSONObject getRuleObject(String type,String item,String dayRange,String orderQuantityUpperLimit){
        JSONObject object=new JSONObject();
        Map<String,String> parameters=new HashMap<>();
        parameters.put("DAY_RANGE",dayRange);
        parameters.put("ORDER_QUANTITY_UPPER_LIMIT",orderQuantityUpperLimit);
        object.put("type",type);
        object.put("item",item);
        object.put("parameters",parameters);
        return object;
    }


    /**
     * 新增风控规则----黑名单和重点观察人员风控规则
     * @param type 风控规则类型
     */
    public Long getRuleAdd(String type){
        //应用的门店
        List<String> shopIds=new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //规则中的type
        JSONObject rule=new JSONObject();
        rule.put("type",type);
        //适用业务类型
        List<String> businessTypeArray=new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //新建风控规则
        String name=type.equals("BLACK_LIST")?pp.blackName:pp.observeName;
        IScene scene= AddScene.builder()
                .name(name)
                .rule(rule)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //首次检查类型
                .build();
        Long id=visitor.invokeApi(scene).getLong("id");
        System.err.println("id:"+id);
        return id;
    }

    /**
     * 新增风控规则---收银风控---一人多单
     * @param dayRange 天数限制
     * @param upperLimit 数量限制
     */
    public JSONObject getCashierOrderRuleAdd(String dayRange,String upperLimit){
        //应用的门店
        List<String> shopIds=new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //规则中的详情
        JSONObject object=new JSONObject();
        Map<String,String> parameters=new HashMap<>();
        parameters.put("DAY_RANGE",dayRange);
        parameters.put("ORDER_QUANTITY_UPPER_LIMIT",upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.RISK_SINGLE_MEMBER_ORDER_QUANTITY.getType());
        object.put("parameters",parameters);
        //适用业务类型
        List<String> businessTypeArray=new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //新建风控规则
        IScene scene= AddScene.builder()
                .name("一人多单"+(int) (Math.random() * 10000))
                .rule(object)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //首次检查类型
                .build();
        JSONObject response=visitor.invokeApi(scene,false);
        return response;
    }

    /**
     * 新增风控规则---收银风控---无人风控
     */
    public JSONObject getCashierUnmannedRuleAdd(){
        //应用的门店
        List<String> shopIds=new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //规则中的type
        //规则中的type
        JSONObject rule=new JSONObject();
        rule.put("type", RuleEnum.CASHIER.getType());
        rule.put("item",RuleTypeEnum.UNMANNED_ORDER.getType());
        //适用业务类型
        List<String> businessTypeArray=new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //新建风控规则
        IScene scene= AddScene.builder()
                .name("无人风控"+(int) (Math.random() * 10000))
                .rule(rule)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //首次检查类型
                .build();
        JSONObject response=visitor.invokeApi(scene,false);
        return response;
    }

    /**
     * 新增风控规则---收银风控---员工支付订单监控
     */
    public JSONObject getCashierEmployeeRuleAdd(String timeRange,String upperLimit){
        //应用的门店
        List<String> shopIds=new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //规则中的详情
        JSONObject object=new JSONObject();
        Map<String,String> parameters=new HashMap<>();
        parameters.put("TIME_RANGE",timeRange);
        parameters.put("EMPLOYEE_ORDER_UPPER_LIMIT",upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.EMPLOYEE_ORDER.getType());
        object.put("parameters",parameters);
        //适用业务类型
        List<String> businessTypeArray=new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //新建风控规则
        IScene scene= AddScene.builder()
                .name("员工支付"+(int) (Math.random() * 10000))
                .rule(object)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //首次检查类型
                .build();
        JSONObject response=visitor.invokeApi(scene,false);
        return response;
    }

    /**
     * 新增风控规则---收银风控---同一客户为多台车支付的上限,既一人多车
     */
    public JSONObject getCashierCarRuleAdd(String upperLimit){
        //应用的门店
        List<String> shopIds=new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //规则中的详情
        JSONObject object=new JSONObject();
        Map<String,String> parameters=new HashMap<>();
        parameters.put("CAR_LIMIT",upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.RISK_SINGLE_MEMBER_CAR_QUANTITY.getType());
        object.put("parameters",parameters);
        //适用业务类型
        List<String> businessTypeArray=new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //新建风控规则
        IScene scene= AddScene.builder()
                .name("一人多车"+(int) (Math.random() * 10000))
                .rule(object)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //首次检查类型
                .build();
        JSONObject response=visitor.invokeApi(scene,false);
        return response;
    }

    /**
     * 新增风控规则---收银风控---同一车辆被多人支付的上限,既一车多人
     */
    public JSONObject getCashierMemberRuleAdd(String upperLimit){
        //应用的门店
        List<String> shopIds=new ArrayList<>();
        shopIds.add("43072");
        shopIds.add("28764");
        shopIds.add("28762");
        //规则中的详情
        JSONObject object=new JSONObject();
        Map<String,String> parameters=new HashMap<>();
        parameters.put("MEMBER_LIMIT",upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.RISK_SINGLE_CAR_TRANSACTION_QUANTITY.getType());
        object.put("parameters",parameters);
        //适用业务类型
        List<String> businessTypeArray=new ArrayList<>();
        businessTypeArray.add(RiskBusinessTypeEnum.FIRST_INSPECTION.getType());
        //新建风控规则
        IScene scene= AddScene.builder()
                .name("一车多人"+(int) (Math.random() * 10000))
                .rule(object)
                .shopIds(shopIds)
                .businessType(businessTypeArray)  //首次检查类型
                .build();
        JSONObject response=visitor.invokeApi(scene,false);
        return response;
    }



    /**
     * 删除风控规则
     */
    public String ruleDelete(Long ruleId){
        IScene scene= DeleteScene.builder().id(ruleId).build();
        String message=visitor.invokeApi(scene,false).getString("message");
        return message;
    }

    /**
     * 风控规则开关
     * @param status 1:开启  0：关闭
     */
    public void ruleSwitch(Long ruleId,int status){
        IScene scene= SwitchScene.builder().id(ruleId).status(status).build();
        visitor.invokeApi(scene);
    }

    /**
     * 构造风控告警规则
     * @param  realTime：是否接收实时告警
     * @param silentTime：沉默时间
     * @param type：风控告警类型
     * @param ruleIdList：风控规则ID集合
     * @param acceptRoleIdList：接收者Id结合
     */
    public IScene getAlarmRuleAdd(Boolean realTime,Long silentTime,String type,List<Long> ruleIdList,List<Long> acceptRoleIdList,String name){
        IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.AddScene.builder()
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
     * 新建风控告警规则
     * @param  realTime：是否接收实时告警
     * @param silentTime：沉默时间
     * @param type：风控告警类型
     */
    public String getAlarmRuleAdd(Boolean realTime,Long silentTime,String type,String name){
        //风控规则的ID
        List<Long> ruleIdList=new ArrayList<>();
        //风控规则中的对风控规则类型进行筛选，取前三个
        IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).type(type).build();
        JSONArray list=visitor.invokeApi(scene).getJSONArray("list");
        for(int i = 0; i<(Math.min(list.size(), 3)); i++){
            Long id=list.getJSONObject(i).getLong("id");
            ruleIdList.add(id);
        }
        //接收人ID
        List<Long> acceptRoleIdList=new ArrayList<>();
        acceptRoleIdList.add(4945L);
        acceptRoleIdList.add(5030L);
        //新建风控告警规则
        String alarmId=getAlarmRuleAdd( realTime, silentTime,type,ruleIdList,acceptRoleIdList,name).invoke(visitor,false).getString("message");
        return alarmId;
    }

    /**
     * 编辑风控告警规则
     * @param  realTime：是否接收实时告警
     * @param silentTime：沉默时间
     * @param type：风控告警类型
     * @param ruleIdList：风控规则ID集合
     * @param acceptRoleIdList：接收者Id结合
     */
    public IScene getAlarmRuleEdit(Long id,Boolean realTime,Long silentTime,String type,List<Long> ruleIdList,List<Long> acceptRoleIdList){
        IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.EditScene.builder()
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
        return scene;
    }

    /**
     * 风控告警规则开关
     * @param status 1:开启  0：关闭
     */
    public void getAlarmRuleSwitch(Long ruleId,int status){
        IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.SwitchScene.builder().id(ruleId).status(status).build();
        visitor.invokeApi(scene);
    }

    /**
     * 风控告警规则删除
     */
    public String  getAlarmRuleDel(Long ruleId){
        IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.DeleteScene.builder().id(ruleId).build();
        String message=visitor.invokeApi(scene,false).getString("message");
        return message;
    }

    /**
     * 风控事件处理
     * @param id
     * @param result    1：订单正常  0：订单异常
     * @param remarks   备注
     * @param customerIds  注册人物列表
     */
    public JSONObject getRiskEventHandle(Long id,int result,String remarks,JSONArray customerIds){
        IScene scene= RiskEventHandleScene.builder()
                .id(id)
                .result(result)
                .remarks(remarks)
                .customerIds(customerIds)
                .build();
        JSONObject object=visitor.invokeApi(scene,false);
        return object;
    }


    /**
     * 员工状态的
     * @param status 1:开启  0：关闭
     */
    public JSONObject getStaffStatusChange(String id,String status){
        IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.StatusChangeScene.builder().id(id).status(status).build();
        JSONObject object=visitor.invokeApi(scene,false);
        return object;
    }

    /**
     * 创建一个账号
     */
    public String createAccountNumber(String name,String phone){
        //新建账号
        JSONArray roleList=new JSONArray();
        JSONObject object=new JSONObject();
        List<JSONObject> shopList=new ArrayList<>();
        JSONObject shopObject=new JSONObject();
        shopObject.put("shop_id","43072");
        shopObject.put("shop_name","AI-Test(门店订单录像)");
        shopList.add(shopObject);
        object.put("role_id","4944");
        object.put("role_name","超级管理员");
        object.put("shop_list",shopList);
        roleList.add(object);
        IScene scene2=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.AddScene.builder()
                .name(name)
                .phone(phone)
                .roleList(roleList)
                .build();
        String message=visitor.invokeApi(scene2,false).getString("message");
        return message;
    }

    /**
     * 编辑账号的姓名和手机号
     */
    public String getEditAccountNumber(String id,String name,String phone){
        //新建账号
        JSONArray roleList=new JSONArray();
        JSONObject object=new JSONObject();
        List<JSONObject> shopList=new ArrayList<>();
        JSONObject shopObject=new JSONObject();
        shopObject.put("shop_id","43072");
        shopObject.put("shop_name","AI-Test(门店订单录像)");
        shopList.add(shopObject);
        object.put("role_id","4944");
        object.put("role_name","超级管理员");
        object.put("shop_list",shopList);
        roleList.add(object);
        //编辑账号
        IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.EditScene.builder()
                .id(id)
                .name(name)
                .phone(phone)
                .roleList(roleList)
                .build();
        String message=visitor.invokeApi(scene,false).getString("message");
        return message;
    }

    /**
     * 编辑账号的姓名和手机号,返回roleID
     */
    public Long getEditAccountNumberReturn(String id,String name,String phone){
        //新增一个角色---上级角色为【总管理员】
        getAddRole(pp.roleName,pp.descriptionRole);
        //获取此角色的ID
        Long roleId=authRoleNameTransId(pp.roleName);
        //新建账号
        JSONArray roleList=new JSONArray();
        JSONObject object=new JSONObject();
        List<JSONObject> shopList=new ArrayList<>();
        JSONObject shopObject=new JSONObject();
        shopObject.put("shop_id","43072");
        shopObject.put("shop_name","AI-Test(门店订单录像)");
        shopList.add(shopObject);
        object.put("role_id",roleId);
        object.put("role_name",pp.roleName);
        object.put("shop_list",shopList);
        roleList.add(object);
        //编辑账号
        IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.EditScene.builder()
                .id(id)
                .name(name)
                .phone(phone)
                .roleList(roleList)
                .build();
        visitor.invokeApi(scene,false).getString("message");
        return roleId;
    }


    /**
     * 新增角色
     */
    public void getAddRole(String name,String descriptionRole){
        //权限合集
        JSONArray authIds = new JSONArray();
        authIds.add(232);
        authIds.add(233);
        authIds.add(234);
        authIds.add(235);
        authIds.add(236);
        authIds.add(237);
        authIds.add(238);
        authIds.add(239);
        //新增一个角色---上级角色为【总管理员】
        IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.AddScene.builder()
                .name(name)
                .parentRoleId(4944)
                .authList(authIds)
                .description(descriptionRole)
                .build();
        JSONObject response=visitor.invokeApi(scene);
    }

    /**
     * 删除角色
     */
    public String getDelRole(Long roleId) {
        IScene scene = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.DeleteScene.builder().id(roleId).build();
        String message = visitor.invokeApi(scene, false).getString("message");
        return message;
    }

    /**
     * 编辑角色
     */
    public String getEditRole(Long roleId,String name,String descriptionRole) {
        //权限合集
        JSONArray authIds = new JSONArray();
        authIds.add(232);
        authIds.add(233);
        authIds.add(234);
        //上级角色为【总管理员】
        IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.EditScene.builder()
                .id(roleId)
                .name(name )
                .parentRoleId(4944)
                .authList(authIds)
                .description(descriptionRole)
                .build();
        String message=visitor.invokeApi(scene,false).getString("message");
        return message;
    }

    /**
     * 删除特殊人员
     */
    public String getRiskPersonDel(String customerId,String type) {
        IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.DeleteScene.builder().customerId(customerId).type(type).build();
        String message=visitor.invokeApi(scene,false).getString("message");
        return message;
    }

    /**
     * 新增特殊人员
     */

    public String getRiskPersonAdd(String customerId, List<String> customerIds,String type) {
        //新增特殊人员--重点观察人员
        IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.AddScene.builder().customerId(customerId).customerIds(customerIds).type(type).build();
        String message=visitor.invokeApi(scene,false).getString("message");
        return message;
    }

    /**
     * 生成订单
     * @param shopId      门店ID
     * @param transId     交易ID
     * @param carVehicleNumber   车架号 haode
     * @param userId      客户ID
     * @param openId      支付ID
     */
     public String getCreateOrder(String shopId,String transId,String userId,String openId,String carVehicleNumber) throws Exception {
         String post="";
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
             // java代码示例
             String requestUrl = "http://dev.api.winsenseos.com/retail/api/data/biz";
             // 1. 将以下参数(uid、app_id、ak、router、timestamp、nonce)的值之间使用顿号(.)拼接成一个整体字符串
             String signStr = uid + NUMBER + appId + NUMBER + ak + NUMBER + router + NUMBER + timestamp + NUMBER + nonce;
             // 2. 使用HmacSHA256加密算法, 使用平台分配的sk作为算法的密钥. 对上面拼接后的字符串进行加密操作,得到byte数组
             Mac sha256Hmac = Mac.getInstance(ALGORITHM);
             SecretKeySpec encodeSecretKey = new SecretKeySpec(sk.getBytes(StandardCharsets.UTF_8), ALGORITHM);
             sha256Hmac.init(encodeSecretKey);
             byte[] hash = sha256Hmac.doFinal(signStr.getBytes(StandardCharsets.UTF_8));
             // 3. 对2.中的加密结果,再进行一次base64操作, 得到一个字符串
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
                     "        \"shop_id\": \""+shopId+"\",\n" +
                     "        \"trans_id\": \""+transId+"\" ,\n" +
                     "        \"trans_time\": \""+transTime+"\" ,\n" +
                     "        \"trans_type\": [\n" +
                     "            \"SCAN\"\n" +
                     "        ],\n" +
                     "        \"user_id\": \""+userId+"\" ,\n" +
                     "        \"total_price\": 1800,\n" +
                     "        \"real_price\": 1500,\n" +
                     "        \"shopType\": \"SHOP_TYPE\",\n" +
                     "        \"orderNumber\": \"13444894484\",\n" +
                     "        \"memberName\":\"自动化员工\",\n" +
                     "        \"memberPhone\":\"15789033456\",\n" +
                     "        \"receipt_type\":\"小票类型\",\n" +
                     "        \"open_id\":\""+openId+"\","+
                     "        \"posId\": \"pos-1234586789\",\n" +
                     "        \"commodityList\": [\n" +
                     "            {\n" +
                     "                \"commodityId\": \"ff1234567890\",\n" +
                     "                \"commodity_name\":\"法拉第未来\",\n" +
                     "                \"unit_price\": 500000,\n" +
                     "                \"num\": 4\n" +
                     "            },\n" +
                     "            {\n" +
                     "                \"commodityId\": \"model991221313\",\n" +
                     "                \"commodity_name\":\"特斯拉model3\",\n" +
                     "                \"unit_price\": 300000,\n" +
                     "                \"num\": 4\n" +
                     "            }\n" +
                     "        ],\n" +
                     "        \"trans_business_params\":{\n" +
                     "               \"car_plate\":\"京A11111\",\n" +
                     "               \"car_vehicle_number\":\""+carVehicleNumber+"\",\n" +
//                     "                \"business_type\":\"FIRST_INSPECTION\",\n" +
                     "                \"business_order_id\":\"27389182\"\n" +
                     "    }    }\n" +
                     "  }\n" +
                     "}";

             JSONObject jsonObject = JSON.parseObject(str);
             System.out.println("transId:"+transId);
             System.out.println(str);
             HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);
             post = HttpClientUtil.post(config);
         }
         return  post;
     }


    public String getCreateOrderOnline(String shopId,String transId,String userId,String openId,String carVehicleNumber) throws Exception {
        String post="";
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
            // java代码示例
            // 1. 将以下参数(uid、app_id、ak、router、timestamp、nonce)的值之间使用顿号(.)拼接成一个整体字符串
            String signStr = uid + NUMBER + appId + NUMBER + ak + NUMBER + router + NUMBER + timestamp + NUMBER + nonce;
            // 2. 使用HmacSHA256加密算法, 使用平台分配的sk作为算法的密钥. 对上面拼接后的字符串进行加密操作,得到byte数组
            Mac sha256Hmac = Mac.getInstance(ALGORITHM);
            SecretKeySpec encodeSecretKey = new SecretKeySpec(sk.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            sha256Hmac.init(encodeSecretKey);
            byte[] hash = sha256Hmac.doFinal(signStr.getBytes(StandardCharsets.UTF_8));
            // 3. 对2.中的加密结果,再进行一次base64操作, 得到一个字符串
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
                    "   \"uid\": \""+uid+"\",\n" +
                    "   \"app_id\": \""+appId+"\",\n" +
                    "  \"request_id\": \"5d45a085-8774-4jd0-943e-ded373ca6a919987\",\n" +
                    "  \"version\": \"v1.0\",\n" +
                    "  \"router\": \"/business/precipitation/TRANS_INFO_RECEIVE/v1.0\",\n" +
                    "  \"data\": {\n" +
                    "    \"biz_data\":  {\n" +
                    "        \"shop_id\": \""+shopId+"\",\n" +
                    "        \"trans_id\": \""+transId+"\" ,\n" +
                    "        \"trans_time\": \""+transTime+"\" ,\n" +
                    "        \"trans_type\": [\n" +
                    "            \"SCAN\"\n" +
                    "        ],\n" +
                    "        \"user_id\": \""+userId+"\" ,\n" +
                    "        \"total_price\": 1800,\n" +
                    "        \"real_price\": 1500,\n" +
                    "        \"shopType\": \"SHOP_TYPE\",\n" +
                    "        \"orderNumber\": \"13444894484\",\n" +
                    "        \"memberName\":\"自动化员工\",\n" +
                    "        \"memberPhone\":\"15789033456\",\n" +
                    "        \"receipt_type\":\"小票类型\",\n" +
                    "        \"open_id\":\""+openId+"\","+
                    "        \"posId\": \"pos-1234586789\",\n" +
                    "        \"commodityList\": [\n" +
                    "            {\n" +
                    "                \"commodityId\": \"ff1234567890\",\n" +
                    "                \"commodity_name\":\"法拉第未来\",\n" +
                    "                \"unit_price\": 500000,\n" +
                    "                \"num\": 4\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"commodityId\": \"model991221313\",\n" +
                    "                \"commodity_name\":\"特斯拉model3\",\n" +
                    "                \"unit_price\": 300000,\n" +
                    "                \"num\": 4\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"trans_business_params\":{\n" +
                    "               \"car_plate\":\"京A11111\",\n" +
                    "               \"car_vehicle_number\":\""+carVehicleNumber+"\",\n" +
//                    "               \"business_type\":"+business_type+",\n" +
//                    "                \"business_type\":\"GOODS_PAY\",\n" +
                    "                \"business_order_id\":\"27389182\"\n" +
                    "    }    }\n" +
                    "  }\n" +
                    "}";
            JSONObject jsonObject = JSON.parseObject(str);
            System.out.println("str:"+str);
            HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);
            post = HttpClientUtil.post(config);
        }
        return  post;
    }

    public String getCreateOrder3(orderParm orderParm) throws Exception {
        String post="";
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
            // java代码示例
            // 1. 将以下参数(uid、app_id、ak、router、timestamp、nonce)的值之间使用顿号(.)拼接成一个整体字符串
            String signStr = uid + NUMBER + appId + NUMBER + ak + NUMBER + router + NUMBER + timestamp + NUMBER + nonce;
            // 2. 使用HmacSHA256加密算法, 使用平台分配的sk作为算法的密钥. 对上面拼接后的字符串进行加密操作,得到byte数组
            Mac sha256Hmac = Mac.getInstance(ALGORITHM);
            SecretKeySpec encodeSecretKey = new SecretKeySpec(sk.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            sha256Hmac.init(encodeSecretKey);
            byte[] hash = sha256Hmac.doFinal(signStr.getBytes(StandardCharsets.UTF_8));
            // 3. 对2.中的加密结果,再进行一次base64操作, 得到一个字符串
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
            String transTime = "" +System.currentTimeMillis();
            //ONLINE   SCAN
            String str = "{\n" +
                    "   \"uid\": \""+uid+"\",\n" +
                    "   \"app_id\": \""+appId+"\",\n" +
                    "  \"request_id\": \"5d45a085-8774-4jd0-943e-ded373ca6a919987\",\n" +
                    "  \"version\": \"v1.0\",\n" +
                    "  \"router\": \"/business/precipitation/TRANS_INFO_RECEIVE/v1.0\",\n" +
                    "  \"data\": {\n" +
                    "    \"biz_data\":  {\n" +
                    "        \"shop_id\": \""+orderParm.shopId+"\",\n" +
                    "        \"trans_id\": \""+orderParm.transId+"\" ,\n" +
                    "        \"trans_time\": \""+transTime+"\" ,\n" +
                    "        \"trans_type\": [\n" +
                    "            \""+orderParm.type+"\"\n" +
                    "        ],\n" +
                    "        \"user_id\": \""+orderParm.userId+"\" ,\n" +
                    "        \"total_price\": 1800,\n" +
                    "        \"real_price\": 1500,\n" +
                    "        \"shopType\": \"SHOP_TYPE\",\n" +
                    "        \"orderNumber\": \"13444894484\",\n" +
                    "        \"memberName\":\"自动化员工\",\n" +
                    "        \"memberPhone\":\"15789033456\",\n" +
                    "        \"receipt_type\":\"小票类型\",\n" +
                    "        \"open_id\":\""+orderParm.openId+"\","+
                    "        \"posId\": \"pos-1234586789\",\n" +
                    "        \"commodityList\": [\n" +
                    "            {\n" +
                    "                \"commodityId\": \"ff1234567890\",\n" +
                    "                \"commodity_name\":\"法拉第未来\",\n" +
                    "                \"unit_price\": 500000,\n" +
                    "                \"num\": 4\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"commodityId\": \"model991221313\",\n" +
                    "                \"commodity_name\":\"特斯拉model3\",\n" +
                    "                \"unit_price\": 300000,\n" +
                    "                \"num\": 4\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"trans_business_params\":{\n" +
                    "               \"car_plate\":\"京A11111\",\n" +
                    "               \"car_vehicle_number\":\""+orderParm.carVehicleNumber+"\",\n" +
                    "               \"business_type\":"+orderParm.business_type+",\n" +
                    "                \"business_order_id\":\"27389182\"\n" +
                    "    }    }\n" +
                    "  }\n" +
                    "}";

            JSONObject jsonObject = JSON.parseObject(str);
            System.out.println("transId:"+orderParm.transId);
            System.out.println("str:"+str);
            HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);
            post = HttpClientUtil.post(config);
        }
        return  post;
    }

    public Integer riskTotal(){
        IScene pageScene= PageScene.builder().page(1).size(10).shopName(router.getShopName()).build();
        JSONArray data=visitor.invokeApi(pageScene).getJSONArray("list");
        Integer cashierTolta=data.getJSONObject(0).getInteger("risk_total");  //事件数
        return  cashierTolta;
    }


    public static void main(String[] args) {
        String transTime = "" + LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("+8")).toEpochMilli()
                ;
        System.out.println(transTime);
    }
    /**
     * 判断车架号是否17位
     */
    public String carVehicleNumberCheck(){
        //随机生成17位车架号
        String carVehicleNumber="AAAAAAAAAA"+CommonUtil.getRandom(7);
        System.out.println("carVehicleNumber2222:"+carVehicleNumber);
        if(carVehicleNumber.length()!=17){
            carVehicleNumber="AAAAAAAAAA"+CommonUtil.getRandom(7);
            System.out.println("carVehicleNumber111:"+carVehicleNumber);
        }
        String carNumber= carVehicleNumber.length()!=17?carVehicleNumberCheck():carVehicleNumber;
        System.out.println("----carNumber-----"+carNumber);
        return carNumber;
    }

    /**
     * 通过角色名称获取角色ID
     */
    public Long authRoleNameTransId(String name){
        Long id=0L;
        IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(1).size(10).build();
        JSONObject response=visitor.invokeApi(scene);
        int pages=response.getInteger("pages");
        for(int page=1;page<=pages;page++){
            JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String name1=list.getJSONObject(i).getString("name");
                if(name1.equals(name)){
                    id=list.getJSONObject(i).getLong("id");
                    System.err.println("-------id:---"+id);
                }
            }
        }
        return id;
    }

    /**
     * 通过账号手机号获取账号的ID
     */
    public String authStaffTransId(String phone){
        String id="";
        IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build();
        JSONObject response=visitor.invokeApi(scene);
        int pages=response.getInteger("pages")>10?10:response.getInteger("pages");
        for(int page=1;page<=pages;page++){
            JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
            if(list.size()>0){
                for(int i=0;i<list.size();i++){
                    String phone1=list.getJSONObject(i).getString("phone");
                    System.err.println(phone1);
                    if(phone.equals(phone1)){
                        id=list.getJSONObject(i).getString("id");
                    }
                }
            }
        }
        return id;
    }

    /**
     * 通过员工id获取员工的创建时间
     */
    public JSONObject staffIdTransResponse(String id){
        JSONObject res=null;
        JSONObject response=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
        int numBefore=response.getInteger("total");
        int pages=response.getInteger("pages");
        for(int page=1;page<=pages;page++){
            JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String id1=list.getJSONObject(i).getString("id");
                if(id1.equals(id)){
//                    createTime=list.getJSONObject(i).getString("create_time");
                    res=list.getJSONObject(i);
                }
            }
        }
        return res;
    }





}
