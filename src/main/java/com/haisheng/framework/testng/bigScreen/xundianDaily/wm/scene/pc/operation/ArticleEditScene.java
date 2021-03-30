package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.operation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.4. 内容运营 : 编辑文章
 *
 * @author wangmin
 * @date 2021-03-30 14:00:02
 */
@Builder
public class ArticleEditScene extends BaseScene {
    /**
     * 描述 文章ID
     * 是否必填 true
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 标题
     * 是否必填 true
     * 版本 v1.0
     */
    private final String title;

    /**
     * 描述 富文本内容
     * 是否必填 true
     * 版本 v1.0
     */
    private final String content;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("title", title);
        object.put("content", content);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/operation/article/edit";
    }
}