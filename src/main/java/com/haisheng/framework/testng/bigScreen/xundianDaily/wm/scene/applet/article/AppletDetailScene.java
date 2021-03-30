package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.article;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.4. 小程序-文章详情
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletDetailScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("id", id);
        object.put("share_id", shareId);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/article/detail";
    }
}