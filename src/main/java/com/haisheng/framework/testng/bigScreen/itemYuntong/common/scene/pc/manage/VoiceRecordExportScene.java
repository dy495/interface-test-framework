package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.3. 语音下载（谢）
 *
 * @author wangmin
 * @date 2021-05-31 16:28:11
 */
@Builder
public class VoiceRecordExportScene extends BaseScene {
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
        return "/intelligent-control/pc/manage/voice/record/export";
    }
}