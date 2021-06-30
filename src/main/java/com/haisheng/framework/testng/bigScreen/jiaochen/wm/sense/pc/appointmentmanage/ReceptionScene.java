package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.5. 接待预约（谢）（2020-12-15）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:26
 */
@Builder
public class ReceptionScene extends BaseScene {
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/appointment-manage/reception";
    }
}