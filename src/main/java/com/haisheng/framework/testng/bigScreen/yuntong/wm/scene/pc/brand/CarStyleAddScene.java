package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.brand;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 31.12. 新建品牌车系（谢）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class CarStyleAddScene extends BaseScene {
    /**
     * 描述 品牌id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long brandId;

    /**
     * 描述 生产商
     * 是否必填 true
     * 版本 v1.0
     */
    private final String manufacturer;

    /**
     * 描述 车系名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 上线时间
     * 是否必填 true
     * 版本 v1.0
     */
    private final String onlineTime;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("brand_id", brandId);
        object.put("manufacturer", manufacturer);
        object.put("name", name);
        object.put("online_time", onlineTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/brand/car-style/add";
    }
}