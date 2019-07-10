package com.haisheng.framework.model.bean;


import lombok.Data;

/**
 * Created by yuhaisheng
 */

@Data
public class Bug {
    private int id;
    private int allTotalNum             = 0;
    private int allCriticalNum          = 0;
    private int allBlockerNum           = 0;
    private int allTestTotalNum         = 0;
    private int allTestCriticalNum      = 0;
    private int allTestBlockerNum       = 0;
    private int allOnlineTotalNum       = 0;
    private int allOnlineCriticalNum    = 0;
    private int allOnlineBlockerNum     = 0;
    private int openTotalNum            = 0;
    private int openCriticalNum         = 0;
    private int openBlockerNum          = 0;
    private int appId;
    private int day;
    private int week;
    private int month;
    private int year;


}
