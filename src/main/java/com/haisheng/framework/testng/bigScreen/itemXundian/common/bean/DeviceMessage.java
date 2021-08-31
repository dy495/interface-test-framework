package com.haisheng.framework.testng.bigScreen.itemXundian.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 设备信息
 */
@Data
public class DeviceMessage implements Serializable {

    private String deviceName;

    private String deviceId;

    private String deviceStatus;
}
