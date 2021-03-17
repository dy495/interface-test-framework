package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.article;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/article/detail的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
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
    public JSONObject getRequestBody(){
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