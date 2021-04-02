package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.customermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 11.5. 销售顾问列表下拉 (池)v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class PreSaleCustomerSalesListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 服务顾问id
     * 版本 v2.0
     */
    @JSONField(name = "sales_id")
    private String salesId;

    /**
     * 描述 服务顾问名称
     * 版本 v2.0
     */
    @JSONField(name = "sales_name")
    private String salesName;

}