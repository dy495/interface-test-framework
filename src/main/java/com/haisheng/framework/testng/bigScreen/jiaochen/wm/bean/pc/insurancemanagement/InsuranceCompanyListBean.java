package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.insurancemanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 17.6. 投保公司列表（池）（下拉）（2021-03-05）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class InsuranceCompanyListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 唯一表示id
     * 版本 v3.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 投保公司名称
     * 版本 v3.0
     */
    @JSONField(name = "insurance_company_name")
    private String insuranceCompanyName;

}