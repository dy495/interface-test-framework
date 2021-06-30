package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 27.6. 内容运营 : 活动置顶 （谢）（2020-12-22）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/operation/article/top";
    }
}