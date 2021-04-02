package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 23.6. 商品品类详情 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class CategoryDetailBean implements Serializable {
    /**
     * 描述 品类名称
     * 版本 v2.0
     */
    @JSONField(name = "category_name")
    private String categoryName;

    /**
     * 描述 品类级别
     * 版本 v2.0
     */
    @JSONField(name = "category_level")
    private String categoryLevel;

    /**
     * 描述 所属品类
     * 版本 v2.0
     */
    @JSONField(name = "belong_category")
    private Long belongCategory;

    /**
     * 描述 所属logo
     * 版本 v2.0
     */
    @JSONField(name = "belong_pic")
    private String belongPic;

}