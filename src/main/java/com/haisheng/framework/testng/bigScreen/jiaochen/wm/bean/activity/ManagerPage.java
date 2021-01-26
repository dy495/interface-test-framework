package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.activity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangmin
 * @date 2021/1/25 11:06
 */
@Data
public class ManagerPage implements Serializable {
    /**
     * 活动id
     */
    @JSONField(name = "id")
    private Long id;
    /**
     * 活动标题
     */
    @JSONField(name = "title")
    private String title;
    /**
     * 创建者名称
     */
    @JSONField(name = "creator_name")
    private String creatorName;
    @JSONField(name = "creator_account")
    private String creatorAccount;
    @JSONField(name = "subject_type")
    private String subjectType;
    @JSONField(name = "subject_type_name")
    private String subjectTypeName;

    /**
     * 活动状态
     */
    @JSONField(name = "status")
    private Integer status;

    /**
     * 活动状态描述
     */
    @JSONField(name = "statusName")
    private String statusName;

}
