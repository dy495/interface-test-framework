package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.8. 取消接待 （谢）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class CancelReceptionScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 接待门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shopId", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/reception-manage/cancel-reception";
    }
}