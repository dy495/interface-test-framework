package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.usedcar;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 22.1. 
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class ConsultPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
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
    protected JSONObject getRequestBody() {
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
        return "/account-platform/auth/used-car/consult/page";
    }
}