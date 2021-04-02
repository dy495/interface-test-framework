package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 23.13. 商品品类详情 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class BrandDetailBean implements Serializable {
    /**
     * 描述 品牌id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 品牌名称
     * 版本 v2.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 品牌说明
     * 版本 v2.0
     */
    @JSONField(name = "brand_description")
    private String brandDescription;

    /**
     * 描述 品牌logo
     * 版本 v2.0
     */
    @JSONField(name = "brand_pic")
    private String brandPic;

}