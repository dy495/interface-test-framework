package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.reception;

/**
 * @author wangmin
 */
public enum  ManagerRoleType {


    /****/
    GENERAL_MANAGER("总经理"),

    SALES_DIRECTOR("销售总监"),

    SALES_MANAGER("销售经理"),

    MARKETING_DIRECTOR("市场总监"),

    SERVICE_DIRECTOR("服务总监"),
    ;

    private String name;


    public String getName() {
        return name;
    }

    ManagerRoleType(String name) {
        this.name = name;
    }



    public static boolean isManager(String name){
        ManagerRoleType[] values = ManagerRoleType.values();
        for (ManagerRoleType value : values) {
            if(value.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
}
