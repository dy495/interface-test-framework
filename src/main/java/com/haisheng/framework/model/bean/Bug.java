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
    private int onlineOpenTotalNum      = 0;
    private int onlineOpenCriticalNum   = 0;
    private int onlineOpenBlockerNum    = 0;
    private int typePdBug      = 0;
    private int typePdSug      = 0;
    private int typeUiBug      = 0;
    private int typeUiSug      = 0;
    private int typeUxBug      = 0;
    private int typeUxSug      = 0;
    private int typeAppBug     = 0;
    private int typePcBug      = 0;
    private int typeAppletBug  = 0;
    private int typeBeBug      = 0;
    private int typeEdgeBug    = 0;
    private int typeDcBug      = 0;
    private int typeAlgorithmBug      = 0;
    private int typeAlgorithmPerf     = 0;
    private int typeBePerf     = 0;

    private int appId;
    private int day;
    private int week;
    private int month;
    private int year;


}
