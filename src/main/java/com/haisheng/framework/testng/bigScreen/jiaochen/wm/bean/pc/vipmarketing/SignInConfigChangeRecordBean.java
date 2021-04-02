package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vipmarketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.16. 签到积分变更记录 (池) v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class SignInConfigChangeRecordBean implements Serializable {
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
     * 描述 唯一表示
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 操作主体
     * 版本 v2.0
     */
    @JSONField(name = "operate_main")
    private String operateMain;

    /**
     * 描述 操作员手机号
     * 版本 v2.0
     */
    @JSONField(name = "operate_phone")
    private String operatePhone;

    /**
     * 描述 操作时间
     * 版本 v2.0
     */
    @JSONField(name = "operate_date")
    private String operateDate;

    /**
     * 描述 变更积分
     * 版本 v2.0
     */
    @JSONField(name = "change_score")
    private Integer changeScore;

    /**
     * 描述 变更备注
     * 版本 v2.0
     */
    @JSONField(name = "change_remark")
    private String changeRemark;

}