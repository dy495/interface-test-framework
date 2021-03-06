package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class ArticleList extends BaseScene {

    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/shop/pc/operation/article-list";
    }
}
