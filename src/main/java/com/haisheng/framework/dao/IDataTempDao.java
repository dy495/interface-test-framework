package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.DataTemp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by x
 */
@Repository
public interface IDataTempDao {
    int insert(DataTemp dataTemp);
    DataTemp queryDataByName(@Param("dataName")String dataName);
    Integer queryDataOneByName(@Param("column_name")String column_name,@Param("dataName")String dataName);
    int updateDataNum(@Param("dataName")String dataName, @Param("pcAppointmentRecordNum")Integer pcAppointmentRecordNum);
    int updateDataAll(DataTemp dataTemp);
}