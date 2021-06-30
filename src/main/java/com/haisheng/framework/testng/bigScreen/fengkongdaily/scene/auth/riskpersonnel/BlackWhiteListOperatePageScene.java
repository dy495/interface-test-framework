package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.5. 特殊人员操作日志分页
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class BlackWhiteListOperatePageScene extends BaseScene {
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
     * 描述 人物id
     * 是否必填 true
     * 版本 v1.0
     */
    private final String customerId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("is_export", isExport);
        object.put("customer_id", customerId);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/risk-personnel/black-white-list/operate/page";
    }
}