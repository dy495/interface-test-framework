package com.haisheng.framework.testng.bigScreen.itemXundian.scene.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.6. 内容运营 : 活动置顶
 *
 * @author wangmin
 * @date 2021-03-30 14:00:02
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/operation/article/top";
    }
}