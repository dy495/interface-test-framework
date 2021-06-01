package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 13.6. 车辆model列表 (池)v2.0 （2021-3-15）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
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