package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 27.5. 内容运营 : 删除文章 （谢）（2021-01-08）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class ArticleDeleteScene extends BaseScene {
    /**
     * 描述 文章ID
     * 是否必填 true
     * 版本 -
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
        return "/account-platform/auth/operation/article/delete";
    }
}