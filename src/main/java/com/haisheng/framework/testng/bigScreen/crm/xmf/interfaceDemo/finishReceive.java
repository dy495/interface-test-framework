package com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.DateTimeUtil;
/**
 * @description :app完成接待,参数类；
 * @date :2020/9/29 18:22
 **/


public class finishReceive {
    public static DateTimeUtil dt = new DateTimeUtil();
    public boolean checkCode = true;
    public String Empty;
    public String customer_id;
    public String reception_id;
    public String belongs_sale_id;
    public String reception_type;
    public String name;
    public String subjectType = "PERSON";
    public String call = "WOMEN";
    public String districtCode;
    public String address = "东城";
    public String birthday;
    public String id_number;
    public String plate_number_one_id;
    public String plate_number_one;
    public String plate_number_two;
    public String intention_car_model="37";
    public String expected_buy_day = dt.getHistoryDate(1);
    public String test_drive_car_model = "37";
    public String pay_type = "1";
    public String assess_car_model = "37";
    public String source_channel;
    public String compare_car_model;
    public String own_car_model;
    public String visit_count_type = "0";
    public String is_offer = "1";
    public String buy_car_type = "2";
    public String power_type;
    public String is_assessed = "1";
    public JSONArray phoneList;
    public JSONArray remark = getRemark();
    public JSONArray face_list;

    private JSONArray getRemark() {
        JSONArray remark = new JSONArray();
        JSONObject re = new JSONObject();
        re.put("remark", "备注A!@#$%^&*()_+~?/.><;；auto_remark_12345678901234567890");
        remark.add(re);
        return remark;
    }

}
