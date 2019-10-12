package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class OnlinePvuvCheck implements Serializable {
    private int pvEnter;
    private int pvLeave;
    private int uvEnter;
    private int uvLeave;
    private String date;
}