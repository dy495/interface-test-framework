package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.record;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.3. 导出记录 v1.0 (池)
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ExportPageBean implements Serializable {
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
     * 描述 唯一标识
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 归属
     * 版本 v1.0
     */
    @JSONField(name = "affiliation")
    private String affiliation;

    /**
     * 描述 导出页面
     * 版本 v1.0
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 导出页面名称
     * 版本 v1.0
     */
    @JSONField(name = "type_name")
    private String typeName;

    /**
     * 描述 导出时间
     * 版本 v1.0
     */
    @JSONField(name = "export_time")
    private String exportTime;

    /**
     * 描述 操作人员
     * 版本 v1.0
     */
    @JSONField(name = "user_name")
    private String userName;

    /**
     * 描述 操作员账号
     * 版本 v1.0
     */
    @JSONField(name = "user_account")
    private String userAccount;

    /**
     * 描述 状态枚举
     * 版本 v1.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 状态枚举名称
     * 版本 v1.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 下载文件url
     * 版本 v1.0
     */
    @JSONField(name = "file_upload_url")
    private String fileUploadUrl;

    /**
     * 描述 文件名
     * 版本 v2.0
     */
    @JSONField(name = "file_name")
    private String fileName;

    /**
     * 描述 导出条数
     * 版本 v2.0
     */
    @JSONField(name = "export_number")
    private Integer exportNumber;

    /**
     * 描述 是否可下载
     * 版本 v3.0
     */
    @JSONField(name = "is_can_download")
    private Boolean isCanDownload;

}