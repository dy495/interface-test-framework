package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 11.2. 获取指定枚举值列表（谢）（2021-02-19）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppEnumValueListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

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