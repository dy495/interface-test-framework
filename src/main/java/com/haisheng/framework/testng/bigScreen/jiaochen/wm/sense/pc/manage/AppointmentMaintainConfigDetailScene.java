package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.2. 预约配置详情（谢）v3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
 */
@Builder
public class AppointmentMaintainConfigDetailScene extends BaseScene {
    /**
     * 描述 预约类型 取值见字典表《预约类型》 v3.0增加试驾
     * 是否必填 true
     * 版本 v2.0
     */
    private final String type;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/appointment/maintain-config/detail";
    }
}