package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 18.8. 确认购车（杨）v3.0 （2021-03-16）的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:32:27
 */
@Builder
public class BuyCarScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long id;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 购买车辆车型
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long carModel;

    /**
     * 描述 车辆底盘号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String vin;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("car_model", carModel);
        object.put("vin", vin);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/pre-sales-reception/buy-car";
    }
}