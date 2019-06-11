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
    String caseDescription;
    String result;
    Timestamp createTime;
    Timestamp editTime;
    String expect;
    String response;
    String requestData;
    String qaOwner;
    String failReason;
    String authenticationInfo;
    boolean canManualRun = true; //false: can NOT run by checklist-tool
    boolean runByCi = true; //false: can NOT run by ci


}