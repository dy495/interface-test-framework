package com.haisheng.framework.testng.bigScreen.fengkongdaily.util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventHandleScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.AddScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.DeleteScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.SwitchScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.util.DateTimeUtil;
import java.util.*;

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
     * 新增风控规则---黑名单风控
     */
    public String ruleDelete(Long ruleId){
        IScene scene= DeleteScene.builder().id(ruleId).build();
        String message=visitor.invokeApi(scene).getString("message");
        return message;
    }

    /**
     * 风控规则开关---黑名单风控
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
        List<Long> ruleIdList=new ArrayList();
        //风控规则中的对风控规则类型进行筛选，取前三个
        IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).type(type).build();
        JSONArray list=visitor.invokeApi(scene).getJSONArray("list");
        for(int i = 0; i<(Math.min(list.size(), 3)); i++){
            Long id=list.getJSONObject(i).getLong("id");
            ruleIdList.add(id);
        }
        //接收人ID  todo
        List<Long> acceptRoleIdList=new ArrayList();
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
    public JSONObject getRiskEventHandle(Long id,int result,String remarks,List<Long> customerIds){
        IScene scene= RiskEventHandleScene.builder()
                .id(id)
                .result(result)
                .remarks(remarks)
                .customerIds(customerIds)
                .build();
        JSONObject object=visitor.invokeApi(scene,false);
        return object;
    }






}
