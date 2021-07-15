package com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator;

import lombok.Getter;

public enum CameraStatusEnum {

    /**
     * 视频流错误
     */
    STREAM_ERROR("STREAM_ERROR","视频流错误"),
    /**
     * 掉线
     */
    OFFLINE("OFFLINE","掉线"),
    /**
     * 停止中
     */
    STOPPING("STOPPING","停止中"),
    /**
     * 未部署
     */
    UN_DEPLOYMENT("UN_DEPLOYMENT","未部署"),
    /**
     * 部署中
     */
    DEPLOYMENT_ING("DEPLOYMENT_ING","部署中"),
    /**
     * 已停止
     */
    STOPPED("STOPPED","已停止"),
    /**
     * 运行中
     */
    RUNNING("RUNNING","运行中");

    CameraStatusEnum(String deviceStatus,String deviceName){
        this.deviceStatus=deviceStatus;
        this.deviceName=deviceName;
    }

    @Getter
    private String deviceStatus;
    @Getter
    private String deviceName;


}
