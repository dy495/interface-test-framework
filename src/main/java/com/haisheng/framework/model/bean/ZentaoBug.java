package com.haisheng.framework.model.bean;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class ZentaoBug {

//    private int id;
    private int severity;
    private int product;
    private String status;
    private String openedBuild;
//    private Timestamp lastEditedDate;

}
