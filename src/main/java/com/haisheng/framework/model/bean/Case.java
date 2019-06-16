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
    String result = "FAIL";
    Timestamp editTime;
    String expect;
    String response;
    String requestData;
    String qaOwner;
    String failReason;
    boolean canManualRun = false; //false: can NOT run by checklist-tool
    boolean runByCi = false; //false: can NOT run by ci
    String ciCmd;

    public void setCiCmd(String ciCmd) {
        this.ciCmd = ciCmd;
        this.runByCi = true;
        this.canManualRun = true;
    }


}