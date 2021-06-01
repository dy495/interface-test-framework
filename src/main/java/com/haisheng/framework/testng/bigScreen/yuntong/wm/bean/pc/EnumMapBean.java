package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 34.1. 通用枚举接口
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class EnumMapBean implements Serializable {
    /**
     * 描述 枚举key
     * 版本 -
     */
    @JSONField(name = "key")
    private JSONObject key;

    /**
     * 描述 枚举value 一般作为展示
     * 版本 -
     */
    @JSONField(name = "value")
    private JSONObject value;

}