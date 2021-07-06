package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
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
    @Builder.Default
    private Integer page = 1;

    /**
     * 描述 当前页的数量
     * 是否必填 true
     * 版本 -
     */
    @Builder.Default
    private Integer size = 10;

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
    public JSONObject getRequestBody() {
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

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}