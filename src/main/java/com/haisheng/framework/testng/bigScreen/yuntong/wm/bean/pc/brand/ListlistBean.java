package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.brand;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.1. 获取品牌列表（谢）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:20
 */
@Data
public class ListlistBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 记录id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

    /**
     * 描述 品牌名
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

}