package com.haisheng.framework.testng.bigScreen.jiaochen.lxq.create;

import com.haisheng.framework.util.DateTimeUtil;

public class submitOrder {
    public static DateTimeUtil dt = new DateTimeUtil();
    public Long commodity_id;
    public Long specification_id;
    public String buyer_message =dt.getHistoryDate(0)+"buyerMessage";
    public Boolean sms_notify;
    public Integer commodity_num;
    public String district_code ;
    public String address = "lxq自动化地址" ;
    public String receiver="吕" ;
    public String receive_phone ="13400000000";
    public Boolean chkcode = true;

}
