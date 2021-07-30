package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pic.base64;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.4. 图片上传
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class Base64UploadScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 图片base64
     * 是否必填 false
     * 版本 -
     */
    private final String picData;

    /**
     * 描述 图片类型：0 留痕图片；1 现场巡检图片；
     * 是否必填 false
     * 版本 -
     */
    private final Integer type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("picData", picData);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/pic/base64/upload";
    }
}