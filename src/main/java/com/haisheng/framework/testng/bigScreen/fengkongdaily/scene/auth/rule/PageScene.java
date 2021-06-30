package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.4. 风控规则列表
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class PageScene extends BaseScene {
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
     * 描述 是否导出
     * 是否必填 false
     * 版本 v1.0
     */
    private final Boolean isExport;

    /**
     * 描述 风控规则名称
     * 是否必填 false
     * 版本 -
     */
    private final String name;

    /**
     * 描述 规则类型
     * 是否必填 false
     * 版本 -
     */
    private final String type;

    /**
     * 描述 门店名称
     * 是否必填 false
     * 版本 -
     */
    private final String shopName;

    /**
     * 描述 状态 1开启 0关闭
     * 是否必填 false
     * 版本 -
     */
    private final Integer status;

    /**
     * 描述 业务类型
     * 是否必填 false
     * 版本 -
     */
    private final String businessType;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("is_export", isExport);
        object.put("name", name);
        object.put("type", type);
        object.put("shop_name", shopName);
        object.put("status", status);
        object.put("business_type", businessType);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/rule/page";
    }
}