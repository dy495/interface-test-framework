package com.haisheng.framework.testng.bigScreen.crm.wm.pojo;

import lombok.Data;

/**
 * 每日数据记录表
 */
@Data
public class TPorscheTodayDataDO {

    private long id;
    private String shopId;
    private long todayClueNum;
    private long todayTestDriverNum;
    private long todayOrderNum;
    private long todayDealNum;
    private long todayReceptionNum;
    private long todayNewCustomerReceptionNum;
    private long todayOldCustomerReceptionNum;
    private long todayAppointmentNum;
    private String saleId;
    private String saleName;
    private String todayDate;
    private java.sql.Timestamp gmtCreate;
}
