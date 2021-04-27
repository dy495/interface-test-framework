package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppointmentData implements Serializable {
    public Long id;
    public Long shopId;
    public Long appointmentId;
    public String appointmentType;
    public String appointmentDate;
    public Integer appointmentStatus;
    public String product;
}
