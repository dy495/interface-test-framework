package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.general;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 7.1. 获取指定枚举值列表（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class EnumValueListBean implements Serializable {
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
    private Integer key;

    /**
     * 描述 枚举value 一般作为展示
     * 版本 -
     */
    @JSONField(name = "value")
    private JSONObject value;

}