package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class OnlinePvuvCheck implements Serializable {
    private int pvEnter = 0;
    private int pvLeave = 0;
    private int uvEnter = 0;
    private int uvLeave = 0;
    private String date;
}