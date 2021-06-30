package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.article;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.4. 小程序-文章详情（谢）v3.0 (2021-03-16)
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletDetailScene extends BaseScene {
    /**
     * 描述 文章ID
     * 是否必填 true
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 分享者小程序id 裂变优惠券需要传递
     * 是否必填 false
     * 版本 v2.0
     */
    private final String shareId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("share_id", shareId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/article/detail";
    }
}