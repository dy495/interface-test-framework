package com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 预约记录 - 查询
 */
@Builder
public class appointmentRecodeSelect extends BaseScene {
    private final String plate_number;
    private final String customer_manager;
    private final String appointment_date;
    private final String shop_id;
    private final String customer_name;
    private final String appointment_status;
    private final String create_date;
    private final String confirm_time;
    private final String customer_phone;
    private final String is_overtime;
    private final String page;
    private final String size;
    private final String appointment_end;
    private final String appointment_start;
    private final String create_end;
    private final String create_start;
    private final String service_sale_id;

    @Override
    public JSONObject getRequestBody() {

        JSONObject json1=new JSONObject();
        json1.put("plate_number",plate_number);
        json1.put("customer_manager",customer_manager);
        json1.put("appointment_date",appointment_date);
        json1.put("appointment_end",appointment_end);
        json1.put("appointment_start",appointment_start);
        json1.put("shop_id",shop_id);
        json1.put("customer_name",customer_name);
        json1.put("confirm_status",appointment_status);
        json1.put("create_date",create_date);
        json1.put("create_end",create_end);
        json1.put("create_start",create_start);
        json1.put("confirm_time",confirm_time);
        json1.put("customer_phone",customer_phone);
        json1.put("is_overtime",is_overtime);
        json1.put("page",page);
        json1.put("size",size);
        json1.put("service_sale_id",service_sale_id);

        return json1;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/appointment-manage/appointment-record/appointment-page";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
