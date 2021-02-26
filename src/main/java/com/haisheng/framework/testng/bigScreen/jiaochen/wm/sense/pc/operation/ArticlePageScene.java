package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 内容管理页
 */
@Builder
public class ArticlePageScene extends BaseScene {
    private final String startDate;
    private final String endDate;
    private final String registerStartDate;
    private final String registerEndDate;
    private final String title;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("start_date", startDate);
        object.put("end_date", endDate);
        object.put("register_start_date", registerStartDate);
        object.put("register_end_date", registerEndDate);
        object.put("title", title);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/operation/article/page";
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
