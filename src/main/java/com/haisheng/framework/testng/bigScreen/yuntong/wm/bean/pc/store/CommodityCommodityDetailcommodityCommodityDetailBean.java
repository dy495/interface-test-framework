package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.store;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 20.4. 特惠商品详情(修改前调用) v2.0(池)
 *
 * @author wangmin
 * @date 2021-06-01 18:39:23
 */
@Data
public class CommodityCommodityDetailcommodityCommodityDetailBean implements Serializable {
    /**
     * 描述 id 商品id
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 商品名称
     * 版本 v2.0
     */
    @JSONField(name = "commodity_name")
    private String commodityName;

    /**
     * 描述 商品规格
     * 版本 v2.0
     */
    @JSONField(name = "commodity_specification")
    private String commoditySpecification;

    /**
     * 描述 归属
     * 版本 v2.0
     */
    @JSONField(name = "subject_type")
    private String subjectType;

    /**
     * 描述 归属
     * 版本 v2.0
     */
    @JSONField(name = "subject_id")
    private Long subjectId;

    /**
     * 描述 商品单价
     * 版本 v2.0
     */
    @JSONField(name = "price")
    private Double price;

    /**
     * 描述 商品佣金
     * 版本 v2.0
     */
    @JSONField(name = "commission")
    private Double commission;

    /**
     * 描述 邀请奖励金
     * 版本 v2.0
     */
    @JSONField(name = "invitation_payment")
    private Double invitationPayment;

    /**
     * 描述 卡卷
     * 版本 v2.0
     */
    @JSONField(name = "voucher_list")
    private JSONArray voucherList;

    /**
     * 描述 卡卷名称
     * 版本 v2.0
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 描述 卡卷id
     * 版本 v2.0
     */
    @JSONField(name = "voucher_id")
    private Long voucherId;

    /**
     * 描述 卡卷生效天数
     * 版本 v2.0
     */
    @JSONField(name = "effective_day")
    private Integer effectiveDay;

    /**
     * 描述 卡卷数量
     * 版本 v2.0
     */
    @JSONField(name = "voucher_count")
    private Integer voucherCount;

}