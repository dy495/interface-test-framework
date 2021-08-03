package com.haisheng.framework.testng.bigScreen.itemCms.common.scene;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 区域/映射管理列表
 *
 * @author wangmin
 * @date 2021/08/03
 */
@Builder
public class SubjectListScene extends BaseScene {
    private final String subjectId;
    private final String subjectName;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("subject_id", subjectId);
        object.put("subject_name", subjectName);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/admin/data/subject/list";
    }


    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }
}
