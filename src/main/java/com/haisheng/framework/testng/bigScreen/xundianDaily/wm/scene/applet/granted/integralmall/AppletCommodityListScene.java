package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.3. 小程序 - 积分商城商品列表 (张小龙) v2.0car_platform_path: /jiaochen/applet/granted/integral-mall/commodity-list
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletCommodityListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("size", size);
        object.put("last_value", lastValue);
        object.put("integral_sort", integralSort);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/integral-mall/commodity-list";
    }
}