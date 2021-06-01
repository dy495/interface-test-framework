package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 22.6. 开始接待车主车辆 （谢）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
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