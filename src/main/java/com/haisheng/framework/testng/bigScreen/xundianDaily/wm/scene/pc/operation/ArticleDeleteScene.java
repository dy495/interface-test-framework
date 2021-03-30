package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.operation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.5. 内容运营 : 删除文章
 *
 * @author wangmin
 * @date 2021-03-30 14:00:02
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/operation/article/delete";
    }
}