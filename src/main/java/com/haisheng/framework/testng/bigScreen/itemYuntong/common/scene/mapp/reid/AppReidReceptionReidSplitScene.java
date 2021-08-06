package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.reid;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.6. 接待中：拆分批次（华）v5.0（2021-07-09）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppReidReceptionReidSplitScene extends BaseScene {
    /**
     * 描述 接待ID
     * 是否必填 true
     * 版本 v5.0
     */
    private final Long receptionId;

    /**
     * 描述 人脸ID
     * 是否必填 true
     * 版本 v5.0
     */
    private final JSONArray reidList;

    /**
     * 描述 客户ID列表
     * 是否必填 true
     * 版本 v5.0
     */
    private final JSONArray customerIdList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        object.put("reid_list", reidList);
        object.put("customer_id_list", customerIdList);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/reid/reception-reid-split";
    }
}