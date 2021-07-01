package com.haisheng.framework.testng.bigScreen.itemXundian.enumerator;


import lombok.Getter;

public enum EventStateEnum {
    WAITING_ALARM_CONFIRM("WAITING_ALARM_CONFIRM","待告警处理"),
    ALARM_CONFIRMED("ALARM_CONFIRMED","告警已处理"),
    NO_NEED_HANDLE("NO_NEED_HANDLE","无需处理");

    EventStateEnum(String eventState,String eventStateName){
        this.eventState=eventState;
        this.eventStateName=eventStateName;
    }

    private  String eventState;

    private  String eventStateName;

    public String getEventState(){
        return eventState;
    }
    public String getEventStateName(){
        return eventStateName;
    }
}
