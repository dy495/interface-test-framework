package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.store;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.11. 编辑订单 v2.0(池)
 *
 * @author wangmin
 * @date 2021-03-31 12:47:26
 */
@Builder
public class OrderEditScene extends BaseScene {
    /**
     * 描述 id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 快递单号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String expressNumber;

    /**
     * 描述 分销员手机号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String salesPhone;

    /**
     * 描述 分销佣金
     * 是否必填 true
     * 版本 v2.0
     */
    private final Double commission;

    /**
     * 描述 邀请奖励金
     * 是否必填 true
     * 版本 v2.0
     */
    private final Double invitationPayment;

    /**
     * 描述 备注
     * 是否必填 false
     * 版本 v2.0
     */
    private final String remark;

    /**
     * 描述 更正绑定手机号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String phone;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("express_number", expressNumber);
        object.put("sales_phone", salesPhone);
        object.put("commission", commission);
        object.put("invitation_payment", invitationPayment);
        object.put("remark", remark);
        object.put("phone", phone);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/store/order/edit";
    }
}