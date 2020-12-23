package com.haisheng.framework.dao;


import com.haisheng.framework.model.bean.PorscheDeliverInfo;
import com.haisheng.framework.testng.bigScreen.crm.wm.bean.TPorscheDeliverInfo;

import java.util.List;

public interface IPorscheDao {

    List<PorscheDeliverInfo> selectPorscheDeliver();
}
