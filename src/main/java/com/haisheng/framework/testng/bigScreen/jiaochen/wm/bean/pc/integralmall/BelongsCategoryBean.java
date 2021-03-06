package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 23.4. 所属品类列表 (张小龙) (2020-12-28)
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class BelongsCategoryBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 品类类型
     * 版本 v2.0
     */
    @JSONField(name = "category_type")
    private JSONObject categoryType;

    /**
     * 描述 品类名称
     * 版本 v2.0
     */
    @JSONField(name = "category_name")
    private String categoryName;

}