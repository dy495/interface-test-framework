package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/integral-center/exchange/export的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class ExchangeExportScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 积分兑换品
     * 是否必填 false
     * 版本 v2.0
     */
    private final String exchangeGoods;

    /**
     * 描述 积分兑换类型
     * 是否必填 false
     * 版本 v2.0
     */
    private final String exchangeType;

    /**
     * 描述 状态
     * 是否必填 false
     * 版本 v2.0
     */
    private final String status;

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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("id", id);
        object.put("exchange_goods", exchangeGoods);
        object.put("exchange_type", exchangeType);
        object.put("status", status);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/integral-center/exchange/export";
    }
}