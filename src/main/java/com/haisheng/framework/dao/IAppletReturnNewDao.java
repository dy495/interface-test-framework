package com.haisheng.framework.dao;


import com.haisheng.framework.model.bean.AppletCustomer;
import com.haisheng.framework.model.bean.ProtectTime;
import org.springframework.stereotype.Repository;

@Repository
public interface IAppletReturnNewDao {
    int updateAppletCustomer(AppletCustomer appletCustomer);
    AppletCustomer selectAppletCustomer(String id);
}
