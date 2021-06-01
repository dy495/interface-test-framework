package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 7.2. 开始接待车主车辆（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppStartReceptionBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}