package com.haisheng.framework.util.casereport;

/**
 * @author yuhaisheng
 * create this class to save report summary info
 * 2018-11-22
 */
public class ReportSummary {

    public int totalCaseCount = 0;
    public int passCaseCount = 0;
    public int failCaseCount = 0;
    public int warnCaseCount = 0;
    public int runTotalSecs = 0;

    public int getTotalCaseCount() {
        return totalCaseCount;
    }

    public void setTotalCaseCount(int totalCaseCount) {
        this.totalCaseCount = totalCaseCount;
    }

    public int getPassCaseCount() {
        return passCaseCount;
    }

    public void setPassCaseCount(int passCaseCount) {
        this.passCaseCount = passCaseCount;
    }

    public int getFailCaseCount() {
        return failCaseCount;
    }

    public void setFailCaseCount(int failCaseCount) {
        this.failCaseCount = failCaseCount;
    }

    public int getWarnCaseCount() {
        return warnCaseCount;
    }

    public void setWarnCaseCount(int warnCaseCount) {
        this.warnCaseCount = warnCaseCount;
    }

    public int getRunTotalSecs() {
        return runTotalSecs;
    }

    public void setRunTotalSecs(int runTotalSecs) {
        this.runTotalSecs = runTotalSecs;
    }
}
