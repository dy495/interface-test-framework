package com.haisheng.framework.testng.bigScreen.itemMall.common.scene.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.3. 创建门店
 *
 * @author wangmin
 * @date 2021-07-28 16:58:57
 */
@Builder
public class ShopCreateScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 门店头像的OSS_PATH
     * 是否必填 true
     * 版本 -
     */
    private final String shopImagePath;

    /**
     * 描述 门店名称
     * 是否必填 true
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 营业开始时间
     * 是否必填 true
     * 版本 -
     */
    private final String openingTime;

    /**
     * 描述 营业结束时间
     * 是否必填 true
     * 版本 -
     */
    private final String closingTime;

    /**
     * 描述 联系人
     * 是否必填 true
     * 版本 -
     */
    private final String managerName;

    /**
     * 描述 联系人手机号
     * 是否必填 true
     * 版本 -
     */
    private final String managerPhone;

    /**
     * 描述 所属城市
     * 是否必填 false
     * 版本 -
     */
    private final String city;

    /**
     * 描述 行政区划码
     * 是否必填 true
     * 版本 -
     */
    private final String districtCode;

    /**
     * 描述 详细地址
     * 是否必填 true
     * 版本 -
     */
    private final String address;

    /**
     * 描述 经度
     * 是否必填 true
     * 版本 -
     */
    private final Double longitude;

    /**
     * 描述 维度
     * 是否必填 true
     * 版本 -
     */
    private final Double latitude;

    /**
     * 描述 三方门店ID
     * 是否必填 true
     * 版本 -
     */
    private final String tripartiteShopId;

    /**
     * 描述 三方品牌ID
     * 是否必填 false
     * 版本 -
     */
    private final String thirdpartBrandId;

    /**
     * 描述 推荐度
     * 是否必填 true
     * 版本 -
     */
    private final Integer recommended;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("shop_image_path", shopImagePath);
        object.put("shop_name", shopName);
        object.put("opening_time", openingTime);
        object.put("closing_time", closingTime);
        object.put("manager_name", managerName);
        object.put("manager_phone", managerPhone);
        object.put("city", city);
        object.put("district_code", districtCode);
        object.put("address", address);
        object.put("longitude", longitude);
        object.put("latitude", latitude);
        object.put("tripartite_shop_id", tripartiteShopId);
        object.put("thirdpart_brand_id", thirdpartBrandId);
        object.put("recommended", recommended);
        return object;
    }

    @Override
    public String getPath() {
        return "/mall/shop/create";
    }
}