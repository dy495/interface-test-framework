package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 42.17. 积分兑换订单
 *
 * @author wangmin
 * @date 2021-03-30 14:00:03
 */
@Builder
public class ExchangeOrderScene extends BaseScene {
    /**
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 订单id
     * 是否必填 false
     * 版本 v2.0
     */
    private final String orderId;

    /**
     * 描述 兑换开始时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String startTime;

    /**
     * 描述 兑换结束时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String endTime;

    /**
     * 描述 订单状态 UNPAID("未支付"),WAITING("待发货"),CANCELED("已取消"),FINISHED("已完成"),LOSE("交易失败")
     * 是否必填 false
     * 版本 v2.0
     */
    private final String orderStatus;

    /**
     * 描述 会员
     * 是否必填 false
     * 版本 v2.0
     */
    private final String member;

    /**
     * 描述 商品名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String goodsName;

    /**
     * 描述 手机号
     * 是否必填 false
     * 版本 v2.3
     */
    private final String phone;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("order_id", orderId);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("order_status", orderStatus);
        object.put("member", member);
        object.put("goods_name", goodsName);
        object.put("phone", phone);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/exchange-order";
    }
}