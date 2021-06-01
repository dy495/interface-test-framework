package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 9.3. 开始接待车主车辆v3版本 （谢）v3.0 替换"开始接待车主车辆"接口
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppReceptionStartReceptionV3Bean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}