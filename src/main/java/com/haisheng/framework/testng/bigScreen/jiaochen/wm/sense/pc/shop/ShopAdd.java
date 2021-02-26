package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

import java.util.List;

/**
 * 新建门店
 */
@Builder
public class ShopAdd extends BaseScene {
    private final String avatarPath;
    private final String simpleName;
    private final String name;
    private final List<Long> brandList;
    private final String districtCode;
    private final String address;
    private final String saleTel;
    private final String serviceTel;
    private final Double longitude;
    private final Double latitude;
    private final String appointmentStatus;
    private final String washingStatus;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("avatar_path", avatarPath);
        object.put("simple_name", simpleName);
        object.put("name", name);
        object.put("brand_list", brandList);
        object.put("district_code", districtCode);
        object.put("address", address);
        object.put("sale_tel", saleTel);
        object.put("service_tel", serviceTel);
        object.put("longitude", longitude);
        object.put("latitude", latitude);
        object.put("appointment_status", appointmentStatus);
        object.put("washing_status", washingStatus);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/shop/add";
    }
}
