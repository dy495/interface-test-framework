package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 37.5. 编辑积分兑换商品
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class IntegralCenterEditExchangeGoodsScene extends BaseScene {
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
     * 描述 兑换商品id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 兑换商品类型（FICTITIOUS("虚拟商品"),REAL("实物")）
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exchangeGoodsType;

    /**
     * 描述 商品id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long goodsId;

    /**
     * 描述 兑换有效期开始时间
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exchangeStartTime;

    /**
     * 描述 兑换有效期结束时间
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exchangeEndTime;

    /**
     * 描述 兑换价格
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long exchangePrice;

    /**
     * 描述 兑换数量
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long exchangeNum;

    /**
     * 描述 是否限制
     * 是否必填 true
     * 版本 v2.0
     */
    private final Boolean isLimit;

    /**
     * 描述 兑换限制人数
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer exchangePeopleNum;

    /**
     * 描述 规格列表
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray specificationList;

    /**
     * 描述 卡券有效期类型 选择发送卡券时必填，1：时间段，2：有效天数
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer expireType;

    /**
     * 描述 优惠券使用有效期开始时间
     * 是否必填 true
     * 版本 v3.0
     */
    private final String voucherUseStartTime;

    /**
     * 描述 优惠券使用有效期结束时间
     * 是否必填 true
     * 版本 v3.0
     */
    private final String voucherUseEndTime;

    /**
     * 描述 领取后使用时间
     * 是否必填 true
     * 版本 v3.0
     */
    private final Integer useDays;

    /**
     * 描述 兑换要求的最低会员等级id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long limitCustomerLevelId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("id", id);
        object.put("exchange_goods_type", exchangeGoodsType);
        object.put("goods_id", goodsId);
        object.put("exchange_start_time", exchangeStartTime);
        object.put("exchange_end_time", exchangeEndTime);
        object.put("exchange_price", exchangePrice);
        object.put("exchange_num", exchangeNum);
        object.put("is_limit", isLimit);
        object.put("exchange_people_num", exchangePeopleNum);
        object.put("specification_list", specificationList);
        object.put("expire_type", expireType);
        object.put("voucher_use_start_time", voucherUseStartTime);
        object.put("voucher_use_end_time", voucherUseEndTime);
        object.put("use_days", useDays);
        object.put("limit_customer_level_id", limitCustomerLevelId);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/edit-exchange-goods";
    }
}