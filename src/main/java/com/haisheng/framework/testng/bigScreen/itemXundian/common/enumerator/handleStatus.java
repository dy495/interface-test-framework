package com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator;

public enum handleStatus {
        ALARM_CONFIRMED("ALARM_CONFIRMED","告警确认"),
        NO_NEED_HANDLE("NO_NEED_HANDLE","取消告警");

        handleStatus(String handleState,String handleStateName){
            this.handleState=handleState;
            this.handleStateName=handleStateName;
        }

        private  String handleState;
        private  String handleStateName;

        public String getHandleState(){
            return handleState;
        }
        public String getHandleStateName(){
            return handleStateName;
        }

}
