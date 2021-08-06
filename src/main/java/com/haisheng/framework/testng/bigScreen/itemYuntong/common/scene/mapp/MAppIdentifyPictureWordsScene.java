package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 17.10. 识别图片文字（刘）v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class MAppIdentifyPictureWordsScene extends BaseScene {
    /**
     * 描述 证件正面地址路
     * 是否必填 true
     * 版本 5.0
     */
    private final String frontCardPath;

    /**
     * 描述 证件反面地址路
     * 是否必填 true
     * 版本 5.0
     */
    private final String backCardPath;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("frontCardPath", frontCardPath);
        object.put("back_card_path", backCardPath);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/identify_picture_words";
    }
}