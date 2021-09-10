package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.7. 评鉴详情语音下载（华成裕）
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class PreRecordDownloadScene extends BaseScene {
    /**
     * 描述 语音评鉴id
     * 是否必填 true
     * 版本 v7.0
     */
    private final String id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/manage/voice/record/download";
    }
}