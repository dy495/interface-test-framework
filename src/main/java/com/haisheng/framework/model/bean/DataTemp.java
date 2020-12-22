package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * Created by x
 */

@Data
public class DataTemp implements Serializable {
    int id;
    String dataName="pc_appointmentPage";
    Integer pcAppointmentRecordNum;    //pc预约记录数
    Integer appReceiptage;             //app接待列表数
    Integer pcAppointmentNUmber;             //pc预约看板预约数
    Integer pc_appointment_times;             //pc小程序客户预约次数
    Integer applet_myappointment;             //pc小程序客户预约消息个数

    Integer appSurplusAppointment;      //app未完成确认预约数
    Integer app_all_appointment;

    Integer app_surplus_reception;
    Integer app_all_reception;
    Long appointmentId;       //预约记录id

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public void setPcAppointmentRecordNum(Integer pcAppointmentRecordNum) {
        this.pcAppointmentRecordNum = pcAppointmentRecordNum;
    }


}