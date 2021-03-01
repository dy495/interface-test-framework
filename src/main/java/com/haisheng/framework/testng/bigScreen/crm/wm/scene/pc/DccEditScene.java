package com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 发送站内消息接口
 *
 * @author wangmin
 */
@Builder
public class DccEditScene extends BaseScene {
    private final String customerName;
    private final String customerLevel;
    private final String recordDate;
    private final String belongsSaleId;
    private final String createDate;
    private final Integer carStyle;
    private final Integer carModel;
    private final Integer sourceChannel;
    private final String expectedBuyDay;
    private final String region;
    private final String[] records;
    private final JSONArray visits;
    private final String customerPhone1;
    private final String customerPhone2;
    private final String plateNumber1;
    private final String plateNumber2;
    private final int customerId;

    @Override
    public JSONObject getRequestBody() {
        String[] plateNumber = plateNumber1 == null && plateNumber2 == null ? null : plateNumber1 == null ?
                new String[]{plateNumber2} : plateNumber2 == null ?
                new String[]{plateNumber1} : new String[]{plateNumber1, plateNumber2};
        String[] customerPhone = customerPhone1 == null && customerPhone2 == null ? null : customerPhone1 == null ?
                new String[]{customerPhone2} : customerPhone2 == null ?
                new String[]{customerPhone1} : new String[]{customerPhone1, customerPhone2};
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_level", customerLevel);
        object.put("record_date", recordDate);
        object.put("belongs_sale_id", belongsSaleId);
        object.put("create_date", createDate);
        object.put("car_style", carStyle);
        object.put("car_model", carModel);
        object.put("source_channel", sourceChannel);
        object.put("expected_buy_day", expectedBuyDay);
        object.put("region", region);
        object.put("records", records);
        object.put("visits", visits);
        object.put("customer_phone", customerPhone);
        object.put("plate_number", plateNumber);
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/customer/dcc-edit";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
