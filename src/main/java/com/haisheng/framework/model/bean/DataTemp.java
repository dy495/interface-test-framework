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

    Integer appSurplusAppointment;
    Integer app_all_appointment;

    Integer app_surplus_reception;
    Integer app_all_reception;
    Long appointmentId;

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public void setPcAppointmentRecordNum(Integer pcAppointmentRecordNum) {
        this.pcAppointmentRecordNum = pcAppointmentRecordNum;
    }


}