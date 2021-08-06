package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.2. 开始接待车主车辆（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppPreSalesReceptionStartReceptionScene extends BaseScene {
    /**
     * 描述 接待顾客id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;

    /**
     * 描述 接待顾客手机号
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerPhone;

    /**
     * 描述 是否是主客
     * 是否必填 false
     * 版本 v5.0
     */
    private final Boolean isDecision;

    /**
     * 描述 批次ID
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long batchId;

    /**
     * 描述 人脸ID
     * 是否必填 false
     * 版本 v5.0
     */
    private final String reid;

    /**
     * 描述 人脸图片
     * 是否必填 false
     * 版本 v5.0
     */
    private final String reidPic;

    /**
     * 描述 接待来源
     * 是否必填 false
     * 版本 v5.0
     */
    private final String receptSource;

    /**
     * 描述 接待销售
     * 是否必填 false
     * 版本 v5.0
     */
    private final String saleId;

    /**
     * 描述 客户进店时间
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long enterTime;

    /**
     * 描述 门店
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("customer_phone", customerPhone);
        object.put("is_decision", isDecision);
        object.put("batchId", batchId);
        object.put("reid", reid);
        object.put("reidPic", reidPic);
        object.put("receptSource", receptSource);
        object.put("saleId", saleId);
        object.put("enterTime", enterTime);
        object.put("shopId", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/start-reception";
    }
}