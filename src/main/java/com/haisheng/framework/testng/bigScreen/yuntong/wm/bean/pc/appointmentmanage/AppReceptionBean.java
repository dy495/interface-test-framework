package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 16.5. 接待预约（谢）（2020-12-15）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class AppReceptionBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}