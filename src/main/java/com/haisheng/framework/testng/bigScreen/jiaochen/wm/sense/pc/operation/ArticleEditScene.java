package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.4. 内容运营 : 编辑文章 （谢）（2020-12-22）
 *
 * @author wangmin
 * @date 2021-03-31 12:04:39
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("title", title);
        object.put("content", content);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/operation/article/edit";
    }
}