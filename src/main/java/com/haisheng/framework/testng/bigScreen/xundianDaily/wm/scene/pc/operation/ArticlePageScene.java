package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.operation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.1. 内容运营 : 文章列表
 *
 * @author wangmin
 * @date 2021-03-30 14:00:02
 */
@Builder
public class ArticlePageScene extends BaseScene {
    /**
     * 描述 当前页
     * 是否必填 true
     * 版本 -
     */
    private final Integer page;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    private final Integer size;

    /**
     * 描述 文章标题
     * 是否必填 false
     * 版本 v1.0
     */
    private final String title;

    /**
     * 描述 文章创建时间范围查询开始日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String createStart;

    /**
     * 描述 文章创建时间范围查询结束日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String createEnd;

    /**
     * 描述 文章更新时间范围查询开始日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String modifyStart;

    /**
     * 描述 文章更新时间范围查询结束日期
     * 是否必填 false
     * 版本 v2.0
     */
    private final String modifyEnd;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("title", title);
        object.put("create_start", createStart);
        object.put("create_end", createEnd);
        object.put("modify_start", modifyStart);
        object.put("modify_end", modifyEnd);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/operation/article/page";
    }
}