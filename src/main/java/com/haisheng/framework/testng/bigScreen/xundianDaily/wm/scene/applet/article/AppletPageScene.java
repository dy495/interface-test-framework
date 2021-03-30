package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.article;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.3. 小程序-首页-文章列表更多-分页
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletPageScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 上次请求最后值
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject lastValue;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("size", size);
        object.put("last_value", lastValue);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/article/page";
    }
}