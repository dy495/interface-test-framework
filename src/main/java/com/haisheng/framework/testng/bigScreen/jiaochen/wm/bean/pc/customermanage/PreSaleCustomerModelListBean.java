package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 11.6. 车辆model列表 (池)v2.0 （2021-3-15）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class PreSaleCustomerModelListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 车型id
     * 版本 v2.0
     */
    @JSONField(name = "model_id")
    private Long modelId;

    /**
     * 描述 车型名称
     * 版本 v2.0
     */
    @JSONField(name = "model_name")
    private String modelName;

}