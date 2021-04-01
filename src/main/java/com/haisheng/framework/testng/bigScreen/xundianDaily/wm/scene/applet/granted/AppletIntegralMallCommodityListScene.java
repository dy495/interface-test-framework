package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.3. 小程序 - 积分商城商品列表 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 13:03:21
 */
@Builder
public class AppletIntegralMallCommodityListScene extends BaseScene {
    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 上次请求最后值
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject lastValue;

    /**
     * 描述 积分排序方式（UP("升序")，DOWN("降序")）
     * 是否必填 false
     * 版本 v2.0
     */
    private final String integralSort;

    /**
     * 描述 是否我可兑换
     * 是否必填 false
     * 版本 v2.0
     */
    private final Boolean status;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        object.put("integral_sort", integralSort);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/applet/granted/integral-mall/commodity-list";
    }
}