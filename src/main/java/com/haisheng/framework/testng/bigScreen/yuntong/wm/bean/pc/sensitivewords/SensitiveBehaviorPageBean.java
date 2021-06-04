package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.sensitivewords;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.2. 敏感行为记录分页（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class SensitiveBehaviorPageBean implements Serializable {
    /**
     * 描述 当前页
     * 版本 v1.0
     */
    @JSONField(name = "page")
    private Integer page;

    /**
     * 描述 当前页的数量
     * 版本 v1.0
     */
    @JSONField(name = "size")
    private Integer size;

    /**
     * 描述 每页的数量
     * 版本 v1.0
     */
    @JSONField(name = " page_size")
    private Integer  pageSize;

    /**
     * 描述 总数
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 总页数
     * 版本 v1.0
     */
    @JSONField(name = "pages")
    private Integer pages;

    /**
     * 描述 详细数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 记录id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

    /**
     * 描述 接待顾问姓名
     * 版本 v1.0
     */
    @JSONField(name = "receptor_name")
    private String receptorName;

    /**
     * 描述 敏感词类别描述
     * 版本 v1.0
     */
    @JSONField(name = "sensitive_words_type_name")
    private String sensitiveWordsTypeName;

    /**
     * 描述 敏感词
     * 版本 v1.0
     */
    @JSONField(name = "words")
    private String words;

    /**
     * 描述 审核状态 详见《审核状态》
     * 版本 v1.0
     */
    @JSONField(name = "approval_status")
    private Integer approvalStatus;

    /**
     * 描述 审核状态描述
     * 版本 v1.0
     */
    @JSONField(name = "approval_status_name")
    private String approvalStatusName;

    /**
     * 描述 开始接待时间
     * 版本 v1.0
     */
    @JSONField(name = "reception_start_time")
    private String receptionStartTime;

}