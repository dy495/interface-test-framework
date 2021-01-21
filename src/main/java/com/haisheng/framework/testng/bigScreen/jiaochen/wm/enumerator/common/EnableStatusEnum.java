package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common;

/**
 * @author wangmin
 * @date  2020/11/21  17:11
 */
public enum EnableStatusEnum {
    /**
     * 开启
     */
    ENABLE("开启","ACTIVE"),
    DISABLE("关闭","UN_ACTIVE");


    private String statusName;

    private String ssoStatus;

    EnableStatusEnum(String statusName, String ssoStatus) {
        this.statusName = statusName;
        this.ssoStatus = ssoStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getSsoStatus() {
        return ssoStatus;
    }

    public static EnableStatusEnum findByType(String type) {
        if (ENABLE.name().equals(type)) {
            return ENABLE;
        }
        if (DISABLE.name().equals(type)) {
            return DISABLE;
        }
        throw new IllegalArgumentException("状态类型不存在");
    }

    public static EnableStatusEnum findBySsoStatus(String type){
        if (ENABLE.getSsoStatus().equals(type)) {
            return ENABLE;
        }
        if (DISABLE.getSsoStatus().equals(type)) {
            return DISABLE;
        }
        return null;
    }
}
