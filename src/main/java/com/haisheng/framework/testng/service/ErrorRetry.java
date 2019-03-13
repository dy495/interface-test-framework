package com.haisheng.framework.testng.service;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;


public class ErrorRetry implements IRetryAnalyzer {
    public int initReTryNum = 1;
    public int maxReTryNum  = Integer.parseInt(System.getProperty("MAX_RETRY_TIMES"));

    @Override
    public boolean retry(ITestResult iTestResult) {
        if(initReTryNum <= maxReTryNum){
            String message = "方法<"+iTestResult.getName()+">执行失败，重试第"+initReTryNum+"次";
            Reporter.setCurrentTestResult(iTestResult);
            Reporter.log(message);
            initReTryNum++;
            return true;
        }
        return false;
    }
}