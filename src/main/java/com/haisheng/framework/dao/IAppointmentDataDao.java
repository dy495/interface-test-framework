package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.AppointmentData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IAppointmentDataDao {

    void insert(AppointmentData appointmentData);

    List<AppointmentData> select(AppointmentData appointmentData);

    void update(AppointmentData appointmentData);
}
