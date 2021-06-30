package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.5. 接待预约（谢）（2020-12-15）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class AppReceptionScene extends BaseScene {
    /**
     * 描述 预约id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 预约类型 见字典表《预约类型》
     * 是否必填 true
     * 版本 v3.0
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/appointment-manage/reception";
    }
}