package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 12.1. 操作记录分页（谢）（2020-12-22）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:23
 */
@Data
public class OperateLogOperateLogRecordPageoperateLogOperateLogRecordPageBean implements Serializable {
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
     * 描述 日志id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 操作账号
     * 版本 v2.0
     */
    @JSONField(name = "subject_name")
    private String subjectName;

    /**
     * 描述 操作账号
     * 版本 v2.0
     */
    @JSONField(name = "account")
    private String account;

    /**
     * 描述 数据类型
     * 版本 v2.0
     */
    @JSONField(name = "data_type")
    private Integer dataType;

    /**
     * 描述 数据类型描述
     * 版本 v2.0
     */
    @JSONField(name = "data_type_name")
    private String dataTypeName;

    /**
     * 描述 操作类型
     * 版本 v2.0
     */
    @JSONField(name = "operation_type")
    private Integer operationType;

    /**
     * 描述 操作类型
     * 版本 v2.0
     */
    @JSONField(name = "operation_type_name")
    private String operationTypeName;

    /**
     * 描述 操作时间
     * 版本 v2.0
     */
    @JSONField(name = "operation_time")
    private String operationTime;

    /**
     * 描述 操作内容
     * 版本 v2.0
     */
    @JSONField(name = "operation_content")
    private String operationContent;

}