package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.record;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.1. 导入记录 v1.0 (池)
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ImportPageBean implements Serializable {
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
     * 描述 导入位置码值
     * 版本 v1.0
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 导入位置名称
     * 版本 1.0
     */
    @JSONField(name = "type_name")
    private String typeName;

    /**
     * 描述 导入时间
     * 版本 v1.0
     */
    @JSONField(name = "import_time")
    private String importTime;

    /**
     * 描述 导入格式
     * 版本 v1.0
     */
    @JSONField(name = "file_type")
    private String fileType;

    /**
     * 描述 导入条数
     * 版本 v1.0
     */
    @JSONField(name = "import_num")
    private Integer importNum;

    /**
     * 描述 成功条数
     * 版本 v1.0
     */
    @JSONField(name = "success_num")
    private Integer successNum;

    /**
     * 描述 失败条数
     * 版本 v1.0
     */
    @JSONField(name = "failure_num")
    private Integer failureNum;

    /**
     * 描述 操作门店名称
     * 版本 v1.0
     */
    @JSONField(name = "operate_shop_name")
    private String operateShopName;

    /**
     * 描述 操作人员
     * 版本 v1.0
     */
    @JSONField(name = "user_name")
    private String userName;

    /**
     * 描述 操作人员账号
     * 版本 v1.0
     */
    @JSONField(name = "user_account")
    private String userAccount;

    /**
     * 描述 是否可以下载
     * 版本 v1.0
     */
    @JSONField(name = "is_can_download")
    private Boolean isCanDownload;

    /**
     * 描述 下载地址
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

}