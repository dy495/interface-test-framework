package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.userange;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 38.1. 主体列表
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class SubjectListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "subject_key")
    private String subjectKey;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "subject_value")
    private String subjectValue;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "is_select")
    private Boolean isSelect;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "is_show_detail")
    private Boolean isShowDetail;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "name")
    private String name;

}