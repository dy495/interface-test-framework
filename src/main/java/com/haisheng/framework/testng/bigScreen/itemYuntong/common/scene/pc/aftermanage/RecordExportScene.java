package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftermanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.4. 语音下载（华成裕）
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class RecordExportScene extends BaseScene {
    /**
     * 描述 开始日期
     * 是否必填 true
     * 版本 v1.0
     */
    private final String startDate;

    /**
     * 描述 结束日期
     * 是否必填 true
     * 版本 v1.0
     */
    private final String endDate;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/after-manage/voice/record/export";
    }
}