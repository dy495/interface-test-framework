package com.haisheng.framework.dao;


import com.haisheng.framework.testng.bigScreen.crm.wm.bean.TPorscheDeliverInfo;

import java.util.List;

public interface IPorscheDao {

    List<TPorscheDeliverInfo> selectPorscheDeliver();
}
