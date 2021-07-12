package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 21.1. 接待分页（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class PreSalesReceptionPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer page = 1;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    @Builder.Default
    private Integer size = 10;

    /**
     * 描述 门店Id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long shopId;

    private final String customerId;

    /**
     * 描述 客户名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String phone;

    /**
     * 描述 车系Id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long carStyleId;

    /**
     * 描述 销售名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String preSaleName;

    /**
     * 描述 销售账号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String preSaleAccount;

    /**
     * 描述 接待日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String receptionStart;

    /**
     * 描述 接待日期
     * 是否必填 false
     * 版本 v3.0
     */
    private final String receptionEnd;

    /**
     * 描述 接待时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long receptionEndLong;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("shop_id", shopId);
        object.put("customer_name", customerName);
        object.put("phone", phone);
        object.put("car_style_id", carStyleId);
        object.put("pre_sale_name", preSaleName);
        object.put("pre_sale_account", preSaleAccount);
        object.put("reception_start", receptionStart);
        object.put("reception_end", receptionEnd);
        object.put("receptionEndLong", receptionEndLong);
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/pre-sales-reception/page";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}