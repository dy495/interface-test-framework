package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 28.5. 编辑门店 （杨）（2021-03-23）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class EditScene extends BaseScene {
    /**
     * 描述 门店名称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String name;

    /**
     * 描述 门店简称
     * 是否必填 true
     * 版本 v1.0
     */
    private final String simpleName;

    /**
     * 描述 门店头像
     * 是否必填 true
     * 版本 v1.0
     */
    private final String avatarPath;

    /**
     * 描述 门店所属区划
     * 是否必填 true
     * 版本 v1.0
     */
    private final String districtCode;

    /**
     * 描述 门店品牌列表
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONArray brandList;

    /**
     * 描述 门店详细地址
     * 是否必填 true
     * 版本 v1.0
     */
    private final String address;

    /**
     * 描述 门店销售电话
     * 是否必填 true
     * 版本 v1.0
     */
    private final String saleTel;

    /**
     * 描述 门店售后电话
     * 是否必填 true
     * 版本 v1.0
     */
    private final String serviceTel;

    /**
     * 描述 门店客服电话
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerServiceTel;

    /**
     * 描述 门店救援电话
     * 是否必填 true
     * 版本 v2.0
     */
    private final String rescueTel;

    /**
     * 描述 门店经度
     * 是否必填 true
     * 版本 v1.0
     */
    private final Double longitude;

    /**
     * 描述 门店纬度
     * 是否必填 true
     * 版本 v1.0
     */
    private final Double latitude;

    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("simple_name", simpleName);
        object.put("avatar_path", avatarPath);
        object.put("district_code", districtCode);
        object.put("brand_list", brandList);
        object.put("address", address);
        object.put("sale_tel", saleTel);
        object.put("service_tel", serviceTel);
        object.put("customer_service_tel", customerServiceTel);
        object.put("rescue_tel", rescueTel);
        object.put("longitude", longitude);
        object.put("latitude", latitude);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/shop/edit";
    }
}