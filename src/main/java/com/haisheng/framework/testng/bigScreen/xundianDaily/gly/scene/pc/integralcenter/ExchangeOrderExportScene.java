package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 56.19. 积分兑换订单导出
 *
 * @author wangmin
 * @date 2021-06-29 14:11:45
 */
@Builder
public class ExchangeOrderExportScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

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

    /**
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页，SPECIFIED_DATA：导出特定数据
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exportType;

    /**
     * 描述 导出数据id列表，特定数据时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray ids;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("page", page);
        object.put("size", size);
        object.put("order_id", orderId);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("order_status", orderStatus);
        object.put("member", member);
        object.put("goods_name", goodsName);
        object.put("phone", phone);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/exchange-order/export";
    }
}