package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class Case implements Serializable {
    int id;
    int applicationId;
    int configId;
    String caseName;
    String result;
    Timestamp createTime;
    Timestamp editTime;
    String expect;
    String response;
    String requestData;
    String qaOwner;
    String failReason;
    String authenticationInfo;
    boolean canManualRun = false; //can NOT run by checklist-tool, default value


}