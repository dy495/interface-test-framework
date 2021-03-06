package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 11.7. 车系列表 (池)v3.0 （2021-3-15）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class PreSaleCustomerStyleListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 车系id
     * 版本 v2.0
     */
    @JSONField(name = "style_id")
    private Long styleId;

    /**
     * 描述 车系名称
     * 版本 v2.0
     */
    @JSONField(name = "style_name")
    private String styleName;

}