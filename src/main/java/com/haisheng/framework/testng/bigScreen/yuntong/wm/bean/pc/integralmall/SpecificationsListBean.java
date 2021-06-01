package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 25.22. 规格下拉 (张小龙 2020-01-20) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class SpecificationsListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 规格id
     * 版本 v2.0
     */
    @JSONField(name = "specifications_id")
    private Long specificationsId;

    /**
     * 描述 规格名称
     * 版本 v2.0
     */
    @JSONField(name = "specifications_name")
    private String specificationsName;

}