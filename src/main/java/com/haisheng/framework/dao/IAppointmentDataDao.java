package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.AppointmentData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IAppointmentDataDao {

    void insert(AppointmentData appointmentData);

    List<AppointmentData> select(Map<String, Object> map);
}
