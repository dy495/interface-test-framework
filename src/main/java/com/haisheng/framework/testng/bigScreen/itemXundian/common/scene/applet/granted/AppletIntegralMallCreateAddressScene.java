package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.6. 小程序 - 积分商城创建收货地址 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletIntegralMallCreateAddressScene extends BaseScene {
    /**
     * 描述 收货人
     * 是否必填 true
     * 版本 v2.0
     */
    private final String name;

    /**
     * 描述 联系电话
     * 是否必填 true
     * 版本 v2.0
     */
    private final String phone;

    /**
     * 描述 收货地区编码
     * 是否必填 true
     * 版本 v2.0
     */
    private final String districtCode;

    /**
     * 描述 收货详细地址
     * 是否必填 true
     * 版本 v2.0
     */
    private final String address;

    /**
     * 描述 邮政编码
     * 是否必填 false
     * 版本 v2.0
     */
    private final String postalCode;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("phone", phone);
        object.put("district_code", districtCode);
        object.put("address", address);
        object.put("postal_code", postalCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/integral-mall/create-address";
    }
}