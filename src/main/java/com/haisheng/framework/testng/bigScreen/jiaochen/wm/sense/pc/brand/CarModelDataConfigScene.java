package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.brand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 34.22. 资料配置 v5.0 (张)
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class CarModelDataConfigScene extends BaseScene {
    /**
     * 描述 车型id
     * 是否必填 true
     * 版本 v5.0
     */
    private final Long id;

    /**
     * 描述 资料连接
     * 是否必填 false
     * 版本 v5.0
     */
    private final String dataConfigUrl;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("data_config_url", dataConfigUrl);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/brand/car-style/car-model/data-config";
    }
}