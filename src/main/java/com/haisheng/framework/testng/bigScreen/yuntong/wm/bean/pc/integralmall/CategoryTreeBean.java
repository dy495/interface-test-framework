package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 25.23. 商品管理 - 品类树 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class CategoryTreeBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 品类id
     * 版本 v2.0
     */
    @JSONField(name = "category_id")
    private Long categoryId;

    /**
     * 描述 品类名称
     * 版本 v2.0
     */
    @JSONField(name = "category_name")
    private String categoryName;

    /**
     * 描述 商品数量
     * 版本 v2.0
     */
    @JSONField(name = "num")
    private Integer num;

    /**
     * 描述 子节点类表
     * 版本 v2.0
     */
    @JSONField(name = "child_category_list")
    private JSONArray childCategoryList;

}