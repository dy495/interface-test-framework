package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.6. 内容运营 : 活动置顶 （谢）（2020-12-22）
 *
 * @author wangmin
 * @date 2021-03-31 12:04:39
 */
@Builder
public class ArticleTopScene extends BaseScene {
    /**
     * 描述 文章ID
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/operation/article/top";
    }
}