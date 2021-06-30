package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.5. 特惠商品修改 v2.0(池)
 *
 * @author wangmin
 * @date 2021-03-31 12:47:26
 */
@Builder
public class CommodityEditScene extends BaseScene {
    /**
     * 描述 id 商品id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 商品名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String commodityName;

    /**
     * 描述 主体类型
     * 是否必填 false
     * 版本 v2.0
     */
    private final String subjectType;

    /**
     * 描述 主体id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long subjectId;

    /**
     * 描述 商品单价
     * 是否必填 false
     * 版本 v2.0
     */
    private final Double price;

    /**
     * 描述 商品佣金
     * 是否必填 false
     * 版本 v2.0
     */
    private final Double commission;

    /**
     * 描述 邀请奖励金
     * 是否必填 false
     * 版本 v2.0
     */
    private final Double invitationPayment;

    /**
     * 描述 卡卷
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray voucherList;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("commodity_name", commodityName);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("price", price);
        object.put("commission", commission);
        object.put("invitation_payment", invitationPayment);
        object.put("voucher_list", voucherList);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/store/commodity/edit";
    }
}