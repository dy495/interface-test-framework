package com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.gly.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.gly.StoreInspectionCase;
import com.haisheng.framework.testng.bigScreen.itemXundian.enumerator.EventStateEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.scene.checkrisk.tasks.ListScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.scene.checkriskalarm.HandleScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.util.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class BusinessUtil {
    private final VisitorProxy visitor;
    private final UserUtil user;
    public Long shopId=28758L;
    public String shopName="巡店测试门店1";
    public BusinessUtil(VisitorProxy visitor) {
        this.visitor = visitor;
        this.user = new UserUtil(visitor);
    }
    StoreScenarioUtil su=StoreScenarioUtil.getInstance();

    /**
     * 门店事件中-根据门店的ID，获取门店的留痕的图片数量
     */
    public int getLeaveMarkBeforeNum(Long shopId,int eventId){
        int leaveMarkBeforeNum=0;
        IScene scene= ListScene.builder().page(1).size(10).shopId(shopId).build();
        JSONObject response=visitor.invokeApi(scene,true);
        int pages=response.getInteger("pages");
        for(int page=1;page<=pages;page++){
            JSONArray list=ListScene.builder().page(page).size(10).shopId(shopId).build().invoke(visitor,true).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                int id=list.getJSONObject(i).getInteger("id");
                if(eventId==id){
                    leaveMarkBeforeNum=list.getJSONObject(i).getInteger("leave_mark_num");
                    System.out.println();
                }else if(eventId==0){
                    leaveMarkBeforeNum=list.getJSONObject(0).getInteger("leave_mark_num");
                }
            }
        }
        return leaveMarkBeforeNum;
    }

    /**
     * 确认告警/取消告警
     */
    public int checkRiskAlarmHandle(Long eventId,String eventStatus){
        IScene scene= HandleScene.builder().eventId(eventId).eventStatus(eventStatus).build();
        JSONObject response=visitor.invokeApi(scene,false);
        int code=response.getInteger("code");
        return code;
    }

    /**
     * 获取待处理的规则--日常
     */
    public List<Long> waitingAlarmConfirm(Long shopId){
        List<Long> ids = new ArrayList<>();
        IScene scene= ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
        JSONObject response=visitor.invokeApi(scene,true);
        int total=response.getInteger("total");
        if(total>0){
            System.out.println("--当前列表有待处理的事件："+total);
            int pages=response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                IScene scene1=ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
                JSONArray list=visitor.invokeApi(scene1).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    Long id=list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }else{
            System.out.println("--当前列表没有待处理的事件，正在新建中");
            //触发口罩事件
            su.maskEvent(shopId,false,"customerFalse",true);
            //获取当前列表的第一个的规则的id
            IScene scene2= ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            Long id=response2.getJSONArray("list").getJSONObject(0).getLong("id");
            ids.add(id);
        }
        return ids;

    }

    /**
     * 获取待处理的规则--日常
     */
    public List<Long> waitingAlarmConfirmOnline(Long shopId){
        List<Long> ids = new ArrayList<>();
        IScene scene= ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
        JSONObject response=visitor.invokeApi(scene,true);
        int total=response.getInteger("total");
        if(total>0){
            System.out.println("--当前列表有待处理的事件："+total);
            int pages=response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                IScene scene1=ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
                JSONArray list=visitor.invokeApi(scene1).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    Long id=list.getJSONObject(i).getLong("id");
                    ids.add(id);
                }
            }
        }else{
            System.out.println("--当前列表没有待处理的事件，正在新建中");
            //触发口罩事件
            su.maskEventOnline(shopId,false,"customerFalse",true);
            //获取当前列表的第一个的规则的id
            IScene scene2= ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            Long id=response2.getJSONArray("list").getJSONObject(0).getLong("id");
            ids.add(id);
        }
        return ids;

    }

    /**
     * 获取紧急待处理的规则
     */
    public List<Long> waitingUrgentAlarmConfirm(Long shopId){
        List<Long> ids = new ArrayList<>();
        IScene scene= ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
        JSONObject response=visitor.invokeApi(scene,true);
        int total=response.getInteger("total");
        if(total>0){
            System.out.println("--当前列表有待处理的事件："+total);
            int pages=response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                IScene scene1=ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
                JSONArray list=visitor.invokeApi(scene1).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    Boolean isUrgent=list.getJSONObject(i).getBoolean("is_urgent");
                    if(isUrgent){
                        Long id=list.getJSONObject(i).getLong("id");
                        ids.add(id);
                    }
                }
            }
        }

        if(ids.size()==0){
            System.out.println("--当前列表没有待处理的事件，正在新建中");
            //触发口罩事件
            su.maskEventOnline(shopId,false,"customerFalse",true);
            //获取当前列表的第一个的规则的id
            IScene scene2= ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            Long id=response2.getJSONArray("list").getJSONObject(0).getLong("id");
            ids.add(id);
        }
        return ids;

    }

    /**
     * 获取紧急待处理的规则
     */
    public List<Long> waitingUrgentAlarmConfirmOnline(Long shopId){
        List<Long> ids = new ArrayList<>();
        IScene scene= ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
        JSONObject response=visitor.invokeApi(scene,true);
        int total=response.getInteger("total");
        if(total>0){
            System.out.println("--当前列表有待处理的事件："+total);
            int pages=response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                IScene scene1=ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
                JSONArray list=visitor.invokeApi(scene1).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    Boolean isUrgent=list.getJSONObject(i).getBoolean("is_urgent");
                    if(isUrgent){
                        Long id=list.getJSONObject(i).getLong("id");
                        ids.add(id);
                    }
                }
            }
        }

        if(ids.size()==0){
            System.out.println("--当前列表没有待处理的事件，正在新建中");
            //触发口罩事件
            su.maskEventOnline(shopId,false,"customerFalse",true);
            //获取当前列表的第一个的规则的id
            IScene scene2= ListScene.builder().page(1).size(10).shopId(shopId).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            Long id=response2.getJSONArray("list").getJSONObject(0).getLong("id");
            ids.add(id);
        }
        return ids;

    }


}
