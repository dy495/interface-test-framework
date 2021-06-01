package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.store;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 20.10. 查询订单信息(编辑前调用) v2.0(池)
 *
 * @author wangmin
 * @date 2021-06-01 18:39:23
 */
@Data
public class OrderOrderDetailorderOrderDetailBean implements Serializable {
    /**
     * 描述 订单编号
     * 版本 v2.0
     */
    @JSONField(name = "order_number")
    private String orderNumber;

    /**
     * 描述 快递单号
     * 版本 v2.0
     */
    @JSONField(name = "express_number")
    private String expressNumber;

    /**
     * 描述 分销员手机号
     * 版本 v2.0
     */
    @JSONField(name = "sales_phone")
    private String salesPhone;

    /**
     * 描述 分销佣金
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
     * 描述 备注
     * 版本 v2.0
     */
    @JSONField(name = "remark")
    private String remark;

    /**
     * 描述 更正绑定手机号
     * 版本 -
     */
    @JSONField(name = "phone")
    private String phone;

}