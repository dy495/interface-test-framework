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
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.RiskBusinessTypeEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.RuleEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.RuleTypeEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventHandleScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.AddScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.DeleteScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.SwitchScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CommonUsedUtil {
    PublicParam pp=new PublicParam();
    DateTimeUtil dt =new DateTimeUtil();
    private VisitorProxy visitor;
    private UserUtil user;
    public CommonUsedUtil(VisitorProxy visitor) {
        this.visitor = visitor;
        this.user=new UserUtil(visitor);
    }

    public CommonUsedUtil() {
    }

    /**
     * 获取当前时间
     */
    public String getStartDate() {
        // 格式化时间
        return DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
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
        //应用的门店     todo
        List<String> shopIds=new ArrayList<>();
        shopIds.add("");
        shopIds.add("");
        //规则中的type
        JSONObject rule=new JSONObject();
        rule.put("type",type);
        //新建风控规则   todo
        IScene scene= AddScene.builder()
                .name(pp.blackName)
                .rule(rule)
                .shopIds(shopIds)
                .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                .build();
        Long id=visitor.invokeApi(scene).getLong("id");
        return id;
    }

    /**
     * 新增风控规则---收银风控---一人多单
     * @param dayRange 天数限制
     * @param upperLimit 数量限制
     */
    public JSONObject getCashierOrderRuleAdd(String dayRange,String upperLimit){
        //应用的门店     todo
        List<String> shopIds=new ArrayList<>();
        shopIds.add("");
        shopIds.add("");
        //规则中的详情
        JSONObject object=new JSONObject();
        Map<String,String> parameters=new HashMap<>();
        parameters.put("DAY_RANGE",dayRange);
        parameters.put("ORDER_QUANTITY_UPPER_LIMIT",upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.RISK_SINGLE_MEMBER_ORDER_QUANTITY.getType());
        object.put("parameters",parameters);
        //新建风控规则
        IScene scene= AddScene.builder()
                .name(pp.blackName)
                .rule(object)
                .shopIds(shopIds)
                .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                .build();
        JSONObject response=visitor.invokeApi(scene,false);
        return response;
    }

    /**
     * 新增风控规则---收银风控---无人风控
     */
    public JSONObject getCashierUnmannedRuleAdd(){
        //应用的门店     todo
        List<String> shopIds=new ArrayList<>();
        shopIds.add("");
        shopIds.add("");
        //规则中的type
        //规则中的type
        JSONObject rule=new JSONObject();
        rule.put("type", RuleEnum.CASHIER.getType());
        rule.put("item",RuleTypeEnum.UNMANNED_ORDER.getType());
        //新建风控规则
        IScene scene= AddScene.builder()
                .name(pp.blackName)
                .rule(rule)
                .shopIds(shopIds)
                .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                .build();
        JSONObject response=visitor.invokeApi(scene,false);
        return response;
    }

    /**
     * 新增风控规则---收银风控---员工支付订单监控
     */
    public JSONObject getCashierEmployeeRuleAdd(String timeRange,String upperLimit){
        //应用的门店     todo
        List<String> shopIds=new ArrayList<>();
        shopIds.add("");
        shopIds.add("");
        //规则中的详情
        JSONObject object=new JSONObject();
        Map<String,String> parameters=new HashMap<>();
        parameters.put("TIME_RANGE",timeRange);
        parameters.put("EMPLOYEE_ORDER_UPPER_LIMIT",upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.EMPLOYEE_ORDER.getType());
        object.put("parameters",parameters);
        //新建风控规则
        IScene scene= AddScene.builder()
                .name(pp.blackName)
                .rule(object)
                .shopIds(shopIds)
                .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                .build();
        JSONObject response=visitor.invokeApi(scene,false);
        return response;
    }

    /**
     * 新增风控规则---收银风控---同一客户为多台车支付的上限,既一人多车
     */
    public JSONObject getCashierCarRuleAdd(String upperLimit){
        //应用的门店     todo
        List<String> shopIds=new ArrayList<>();
        shopIds.add("");
        shopIds.add("");
        //规则中的详情
        JSONObject object=new JSONObject();
        Map<String,String> parameters=new HashMap<>();
        parameters.put("TIME_RANGE",upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.RISK_SINGLE_MEMBER_CAR_QUANTITY.getType());
        object.put("parameters",parameters);
        //新建风控规则
        IScene scene= AddScene.builder()
                .name(pp.blackName)
                .rule(object)
                .shopIds(shopIds)
                .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                .build();
        JSONObject response=visitor.invokeApi(scene,false);
        return response;
    }

    /**
     * 新增风控规则---收银风控---同一车辆被多人支付的上限,既一车多人
     */
    public JSONObject getCashierMemberRuleAdd(String upperLimit){
        //应用的门店     todo
        List<String> shopIds=new ArrayList<>();
        shopIds.add("");
        shopIds.add("");
        //规则中的详情
        JSONObject object=new JSONObject();
        Map<String,String> parameters=new HashMap<>();
        parameters.put("TIME_RANGE",upperLimit);
        object.put("type", RuleEnum.CASHIER.getType());
        object.put("item", RuleTypeEnum.RISK_SINGLE_CAR_TRANSACTION_QUANTITY.getType());
        object.put("parameters",parameters);
        //新建风控规则
        IScene scene= AddScene.builder()
                .name(pp.blackName)
                .rule(object)
                .shopIds(shopIds)
                .businessType(RiskBusinessTypeEnum.FIRST_INSPECTION.getName())  //首次检查类型
                .build();
        JSONObject response=visitor.invokeApi(scene,false);
        return response;
    }



    /**
     * 删除风控规则
     */
    public String ruleDelete(Long ruleId){
        IScene scene= DeleteScene.builder().id(ruleId).build();
        String message=visitor.invokeApi(scene).getString("message");
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
    public IScene getAlarmRuleAdd(Boolean realTime,Long silentTime,String type,List<Long> ruleIdList,List<Long> acceptRoleIdList){
        IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.AddScene.builder()
                .name(pp.AlarmName)
                .type(type)
                .ruleIdList(ruleIdList)
                .acceptRoleIdList(acceptRoleIdList)
                .startTime(getDateTime(0))
                .endTime(getDateTime(2))
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
    public Long getAlarmRuleAdd(Boolean realTime,Long silentTime,String type){
        //风控规则的ID
        List<Long> ruleIdList=new ArrayList<>();
        //风控规则中的对风控规则类型进行筛选，取前三个
        IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).type(type).build();
        JSONArray list=visitor.invokeApi(scene).getJSONArray("list");
        for(int i = 0; i<(Math.min(list.size(), 3)); i++){
            Long id=list.getJSONObject(i).getLong("id");
            ruleIdList.add(id);
        }
        //接收人ID  todo
        List<Long> acceptRoleIdList=new ArrayList<>();
        //新建风控告警规则
        Long alarmId=getAlarmRuleAdd( realTime, silentTime,type,ruleIdList,acceptRoleIdList).invoke(visitor,true).getLong("id");
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
        IScene scene= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.DetailScene.builder().id(ruleId).build();
        String message=visitor.invokeApi(scene,false).getString("message");
        return message;
    }

    /**
     * 风控事件处理
     * @param id
     * @param result    1：订单正常  2：订单异常
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
        //新建账号       todo
        JSONArray roleList=new JSONArray();
        JSONObject object=new JSONObject();
        List<JSONObject> shopList=new ArrayList<>();
        JSONObject shopObject=new JSONObject();
        shopObject.put("shop_id","");
        shopObject.put("shop_name","");
        shopList.add(shopObject);
        object.put("role_id","");
        object.put("shop_list",shopList);
        roleList.add(object);
        IScene scene2=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.AddScene.builder().name(name).phone(phone).gender("女").roleList(roleList).build();
        String id=visitor.invokeApi(scene2).getString("id");
        return id;
    }

    /**
     * 编辑账号的姓名和手机号
     */
    public String getEditAccountNumber(String id,String name,String phone){
        // 角色及门店      todo
        JSONArray roleList=new JSONArray();
        JSONObject object=new JSONObject();
        List<JSONObject> shopList=new ArrayList<>();
        JSONObject shopObject=new JSONObject();
        shopObject.put("shop_id","");
        shopObject.put("shop_name","");
        shopList.add(shopObject);
        object.put("role_id","");
        object.put("shop_list",shopList);
        roleList.add(object);
        //编辑账号
        IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.EditScene.builder().id(id).name(name).phone(phone).gender("女").roleList(roleList).build();
        String message=visitor.invokeApi(scene,false).getString("message");
        return message;
    }

    /**
     * 新增角色
     */
    public Long getAddRole(String name,String descriptionRole){
        //权限合集
        JSONArray authIds = new JSONArray();
        authIds.add(7);
        authIds.add(9);
        //新增一个角色---上级角色为【总管理员】
        IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.AddScene.builder()
                .name(name)
                .parentRoleId(2)
                .authList(authIds)
                .description(descriptionRole)
                .build();
        JSONObject response=visitor.invokeApi(scene);
        String message=visitor.invokeApi(scene,false).getString("message");
        Long roleId = response.getLong("role_id");
        return  roleId;
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
        authIds.add(7);
        authIds.add(9);
        //上级角色为【总管理员】
        IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.EditScene.builder()
                .id(roleId).name(name )
                .parentRoleId(2).authList(authIds)
                .description(descriptionRole)
                .build();
        String message=visitor.invokeApi(scene,false).getString("message");
        return message;
    }

    /**
     * 删除特殊人员
     */
    public String getRiskPersonDel(String customerId) {
        IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.DeleteScene.builder().customerId(customerId).build();
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
     * @param realPrice   实际金额
     * @param userId      客户ID
     * @param openId      支付ID
     * @param posId       pos机ID
     * @param commodityId 商品ID
     */
     public String getCreateOrder(String shopId,String transId,String realPrice,String userId,String openId,String posId,String commodityId) throws HttpProcessException, NoSuchAlgorithmException, InvalidKeyException {
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
             String str = "{\n" +
                     "  \"uid\": \"uid_ef6d2de5\",\n" +
                     "  \"app_id\": \"49998b971ea0\",\n" +
                     "  \"request_id\": \"5d45a085-8774-4jd0-943e-ded373ca6a74uuyy0\",\n" +
                     "  \"version\": \"v1.0\",\n" +
                     "  \"router\": \"/business/precipitation/TRANS_INFO_RECEIVE/v1.0\",\n" +
                     "  \"data\": {\n" +
                     "    \"biz_data\":  {\n" +
                     "        \"shop_id\": \"" + shopId + "\",\n" +
                     "        \"trans_id\": " + "\"" + transId + "\"" + " ,\n" +
                     "        \"trans_time\": " + "\"" + transTime + "\"" + " ,\n" +
                     "        \"trans_type\": [\n" +
                     "            \"W\"\n" +
                     "        ],\n" +
                     "        \"user_id\":  " + "\"" + userId + "\"" + " ,\n" +
                     "        \"openid\": "+openId+",\n" +
                     "        \"total_price\": 1800,\n" +
                     "        \"real_price\":"+realPrice+",\n" +
                     "        \"shopType\": \"SHOP_TYPE\",\n" +
                     "        \"orderNumber\": \"13444894484\",\n" +
                     "        \"memberName\":\"无人风控\",\n" +
                     "        \"receipt_type\":\"小票类型\",\n" +
                     "        \"posId\": "+posId+",\n" +
                     "        \"commodityList\": [\n" +
                     "            {\n" +
                     "                \"commodityId\": "+commodityId+",\n" +
                     "                \"commodity_name\":\"法拉第未来\",\n" +
                     "                \"unit_price\": 500000,\n" +
                     "                \"num\": 1\n" +
                     "            },\n" +
                     "        ]\n" +
                     "        \"trans_business_params\":{\n" +
                     "               \"car_plate\":\"京A11111\",\n" +
                     "               \"car_vehicle_number\":\"AAAAAAAAAAQQQQQQ1\",\n" +
                     "                \"business_type\":\"REGULAR_MAINTENANCE\",\n" +
                     "                \"business_order_id\":\"xxxxxxx\"\n" +
                     "    }"+
                     "    }\n" +
                     "  }\n" +
                     "}";

             JSONObject jsonObject = JSON.parseObject(str);
             HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(jsonObject)).client(client);
             post = HttpClientUtil.post(config);
         }
         return  post;
     }



}