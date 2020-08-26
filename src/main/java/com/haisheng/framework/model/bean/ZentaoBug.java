package com.haisheng.framework.model.bean;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class ZentaoBug {

//    private int id;
    private int severity; //等级
    private int product; //产品
    private int module; //模块
    private String status; //状态
//    private String openedBuild;
//    private Timestamp lastEditedDate;
    private String openedDate; //创建时间
    private String openedBy;//创建者
    private String title; //bug title
    private String type; //bug type
    private String resolvedBy; //修复者
    private String resolution; //解决方案：postponed/willnotfix/fixed/duplicate/external/bydesign/空/notrepro

}
