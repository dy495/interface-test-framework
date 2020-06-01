package com.haisheng.framework.dao;


import com.haisheng.framework.model.bean.ReportTime;
import com.haisheng.framework.model.bean.ReturnVisitTime;
import org.springframework.stereotype.Repository;

@Repository
public interface IReturnVisitDao {
    int updateReturnVisitTime(ReturnVisitTime returnVisitTime);
}
