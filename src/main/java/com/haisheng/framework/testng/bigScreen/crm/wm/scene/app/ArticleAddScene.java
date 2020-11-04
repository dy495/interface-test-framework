package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 添加活动接口
 *
 * @author wangmin
 */
@Builder
public class ArticleAddScene extends BaseScene {
    private final String position;
    @Builder.Default
    private final boolean isPicContent = false;
    private final JSONArray customerTypes;
    @Builder.Default
    private final String carStyles = "";
    @Builder.Default
    private final String customerLevel = "";
    @Builder.Default
    private final String customerProperty = "";
    private final String validStart;
    private final String validEnd;
    private final String articleTitle;
    private final String articleBgPic;
    private final String articleContent;
    @Builder.Default
    private final String articleRemarks = "";
    @Builder.Default
    private final boolean isCreatePoster = true;
    @Builder.Default
    private final boolean isOnlineActivity = false;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("position", position);
        object.put("valid_start", validStart);
        object.put("valid_end", validEnd);
        object.put("customer_types", customerTypes);
        object.put("customer_level", customerLevel);
        object.put("customer_property", customerProperty);
        object.put("article_title", articleTitle);
        object.put("is_pic_content", isPicContent);
        object.put("article_bg_pic", articleBgPic);
        object.put("article_content", articleContent);
        object.put("article_remarks", articleRemarks);
        object.put("is_online_activity", isOnlineActivity);
        object.put("is_create_poster", isCreatePoster);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/article/add";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
