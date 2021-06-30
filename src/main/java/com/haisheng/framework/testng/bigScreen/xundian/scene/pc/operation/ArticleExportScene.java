package com.haisheng.framework.testng.bigScreen.xundian.scene.pc.operation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.2. 内容运营 : 文章导出
 *
 * @author wangmin
 * @date 2021-03-30 14:00:02
 */
@Builder
public class ArticleExportScene extends BaseScene {
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

    /**
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页，SPECIFIED_DATA：导出特定数据
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exportType;

    /**
     * 描述 导出数据id列表，特定数据时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray ids;


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
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/operation/article/export";
    }
}