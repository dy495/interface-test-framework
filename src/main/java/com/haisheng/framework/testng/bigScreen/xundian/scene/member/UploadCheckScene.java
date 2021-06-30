package com.haisheng.framework.testng.bigScreen.xundian.scene.member;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.6. 上传图片并校验人物信息
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class UploadCheckScene extends BaseScene {
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
    private final String image;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("image", image);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/member/upload_check";
    }
}