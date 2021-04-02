package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 19.5. 开始接待车主车辆（谢）v3.0 （2021-03-29）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class StartReceptionBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}