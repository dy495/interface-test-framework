package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author wangmin
 * @date 2020/12/6 6:03 PM
 * @desc
 */
@Data
public class ShopDetailRes {
    private Long id;

    @JSONField(name = "shop_id")
    private Long shopId;

    private String name;

    @JSONField(name = "shop_name")
    private String shopName;

    public void setShopIdAndId(Long id) {
        this.id = id;
        this.shopId = id;
    }

    public void setShopNameAndName(String shopName) {
        this.name = shopName;
        this.shopName = shopName;
    }
}
