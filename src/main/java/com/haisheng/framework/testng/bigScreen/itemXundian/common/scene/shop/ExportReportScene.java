package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 导出历史数据-手写的接口-有更改手动改
 *
 * @author zt
 * @date 2021-09-01 16:35:44
 */
@Builder
public class ExportReportScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final JSONArray day;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final String winSense_shop_id;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final String time_type;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("day", day);
        object.put("winsense_shop_id", winSense_shop_id);
        object.put("time_type",time_type);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/history/shop/export-report";
    }
}