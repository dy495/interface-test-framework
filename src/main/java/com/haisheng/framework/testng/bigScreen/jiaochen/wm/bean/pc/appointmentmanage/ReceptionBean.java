package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 14.5. 接待预约（谢）（2020-12-15）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ReceptionBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}