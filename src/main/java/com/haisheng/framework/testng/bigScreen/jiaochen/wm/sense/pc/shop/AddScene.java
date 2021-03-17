package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/shop/add的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class AddScene extends BaseScene {
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


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("simple_name", simpleName);
        object.put("avatar_path", avatarPath);
        object.put("district_code", districtCode);
        object.put("brand_list", brandList);
        object.put("address", address);
        object.put("sale_tel", saleTel);
        object.put("service_tel", serviceTel);
        object.put("longitude", longitude);
        object.put("latitude", latitude);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/shop/add";
    }
}