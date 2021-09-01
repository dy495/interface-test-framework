package com.haisheng.framework.testng.bigScreen.itemMall.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegionTrendBean implements Serializable {
    /**
     * 区域id
     */
    @JSONField(name = "key")
    private String key;

    /**
     * 区域名称
     */
    @JSONField(name = "name")
    private String name;

    /**
     * pv数据
     */
    private Integer pv;

    /**
     * uv数据
     */
    private Integer uv;
}
