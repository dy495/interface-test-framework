package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.5. app接待预约（谢）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppAppointmentReceptionBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}