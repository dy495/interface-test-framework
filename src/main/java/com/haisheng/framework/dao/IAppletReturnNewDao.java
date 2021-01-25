package com.haisheng.framework.dao;


import com.haisheng.framework.model.bean.AppletCustomer;
import com.haisheng.framework.model.bean.ProtectTime;
import org.springframework.stereotype.Repository;

@Repository
public interface IAppletReturnNewDao {
    void updateAppletCustomer(String id);
    AppletCustomer selectAppletCustomer(String id);
}
