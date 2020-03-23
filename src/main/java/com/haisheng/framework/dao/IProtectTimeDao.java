package com.haisheng.framework.dao;


import com.haisheng.framework.model.bean.ProtectTime;
import com.haisheng.framework.model.bean.ReportTime;
import org.springframework.stereotype.Repository;

@Repository
public interface IProtectTimeDao {
    int updateProtectTime(ProtectTime protectTime);
}
