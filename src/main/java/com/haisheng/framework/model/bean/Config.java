package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by yuhaisheng
 */
@Data
public class Config implements Serializable {
    int id;
    int applicationId;
    String service;
    String caseTotal;
    int scenarioTotal;
    String passTotal;
    String rdOwner;
    String qaOwner;

}
