package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.3. 特惠商品创建 v2.0(池)
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class CommodityCreateScene extends BaseScene {
    /**
     * 描述 商品名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String commodityName;

    /**
     * 描述 商品规格
     * 是否必填 false
     * 版本 v2.0
     */
    private final String commoditySpecification;

    /**
     * 描述 归属 主体类型
     * 是否必填 false
     * 版本 v2.0
     */
    private final String subjectType;

    /**
     * 描述 归属 主体id
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

    /**
     * 描述 sku no
     * 是否必填 false
     * 版本 -
     */
    private final String skuNo;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("commodity_name", commodityName);
        object.put("commodity_specification", commoditySpecification);
        object.put("subject_type", subjectType);
        object.put("subject_id", subjectId);
        object.put("price", price);
        object.put("commission", commission);
        object.put("invitation_payment", invitationPayment);
        object.put("voucher_list", voucherList);
        object.put("skuNo", skuNo);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/store/commodity/create";
    }
}