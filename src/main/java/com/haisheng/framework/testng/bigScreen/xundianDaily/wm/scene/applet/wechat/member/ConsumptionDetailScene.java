package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.wechat.member;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.2. 订单详情 lj
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class ConsumptionDetailScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 订单编号
     * 是否必填 false
     * 版本 -
     */
    private final String orderNumber;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("order_number", orderNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/wechat/member/consumption/detail";
    }
}