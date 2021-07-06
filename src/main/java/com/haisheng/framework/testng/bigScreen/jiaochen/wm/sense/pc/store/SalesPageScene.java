package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.store;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.14. 分销员列表 v2.0(池)
 *
 * @author wangmin
 * @date 2021-03-31 12:47:26
 */
@Builder
public class SalesPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 分销员手机号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String salesPhone;

    /**
     * 描述 门店
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long shopId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("sales_phone", salesPhone);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/store/sales/page";
    }
}