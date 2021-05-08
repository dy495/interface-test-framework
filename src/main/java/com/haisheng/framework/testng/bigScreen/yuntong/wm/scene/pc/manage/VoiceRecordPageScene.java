package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.2. 语音记录列表分页（谢）
 *
 * @author wangmin
 * @date 2021-05-08 20:23:16
 */
@Builder
public class VoiceRecordPageScene extends BaseScene {
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
        object.put("page", page);
        object.put("size", size);
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/manage/voice/record/page";
    }
}