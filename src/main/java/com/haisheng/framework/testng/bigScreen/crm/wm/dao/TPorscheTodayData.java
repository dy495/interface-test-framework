package com.haisheng.framework.testng.bigScreen.crm.wm.dao;

import lombok.Data;

/**
 * 每日数据记录表
 */
@Data
public class TPorscheTodayData {

    private Integer id;
    private String shopId;
    private Integer todayClueNum;
    private Integer todayTestDriverNum;
    private Integer todayOrderNum;
    private Integer todayDealNum;
    private Integer todayReceptionNum;
    private Integer todayNewCustomerReceptionNum;
    private Integer todayOldCustomerReceptionNum;
    private Integer todayAppointmentNum;
    private String saleId;
    private String saleName;
    private String todayDate;
    private java.sql.Timestamp gmtCreate;
}
