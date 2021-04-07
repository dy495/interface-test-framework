package com.haisheng.framework.testng.bigScreen.fengkongdaily.util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventHandleScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.AddScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.DeleteScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.SwitchScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.util.DateTimeUtil;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

public class CommonUtil {
    PublicParam pp=new PublicParam();

    private VisitorProxy visitor;
    private UserUtil user;
    public CommonUtil(VisitorProxy visitor) {
        this.visitor = visitor;
        this.user=new UserUtil(visitor);
    }

    public CommonUtil() {
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
        Map<String,String> parameters=new HashMap<String,String>();
        parameters.put("DAY_RANGE",dayRange);
        parameters.put("ORDER_QUANTITY_UPPER_LIMIT",orderQuantityUpperLimit);
        object.put("type",type);
        object.put("item",item);
        object.put("parameters",parameters);
        return object;
    }

    /**
     * 新增风控规则---黑名单风控
     */
    public IScene blackRuleAdd(List<String> shopIds){
        //规则详细
        JSONObject rule=getRuleObject("BLACK_LIST","","","");
        IScene scene= AddScene.builder()
                .name(pp.blackName)
                .rule(rule)
                .shopIds(shopIds)
                .build();
        return scene;
    }

    /**
     * 新增风控规则---黑名单风控
     */
    public Long blackRuleAdd(){
        //应用的门店     todo
        List<String> shopIds=new ArrayList<>();
        shopIds.add("");
        shopIds.add("");
        //新建风控规则   todo
        IScene scene=blackRuleAdd(shopIds);
        Long id=visitor.invokeApi(scene).getLong("id");
        return id;
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
    public String getRiskPerson(String customerId) {
        IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.DeleteScene.builder().customerId(customerId).build();
        String message=visitor.invokeApi(scene,false).getString("message");
        return message;
    }







}
