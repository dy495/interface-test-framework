package com.haisheng.framework.testng.bigScreen.jiaochen.lxq.create;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.util.DateTimeUtil;

public class pcCreateExchangeGoods {
    public static DateTimeUtil dt = new DateTimeUtil();
    public Long id;
    public String exchange_goods_type;
    public Long goods_id;
    public String exchange_start_time=dt.getHistoryDate(0);
    public String exchange_end_time = dt.getHistoryDate(-1000);
    public Long exchange_price  = 1L;
    public Long exchange_num = 100L;
    public Boolean is_limit = false;
    public Integer exchange_people_num ;
    public JSONArray specification_list;
    public Boolean chkcode  = false;
}
