package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.brand;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.13. 编辑品牌车系（谢）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class CarStyleEditScene extends BaseScene {
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

    /**
     * 描述 车系id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("brand_id", brandId);
        object.put("manufacturer", manufacturer);
        object.put("name", name);
        object.put("online_time", onlineTime);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/brand/car-style/edit";
    }
}