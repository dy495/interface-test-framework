package com.haisheng.framework.testng.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.collections.Lists;
import org.testng.xml.XmlSuite;

import java.util.List;

public class TestNGSimpleReport implements ITestListener, IReporter {
    private List<String> testPassed = Lists.newArrayList();
    private List<String> testFailed = Lists.newArrayList();
    private List<String> testSkipped = Lists.newArrayList();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String outputDirectory) {
        logger.info("Passed Case: " + testPassed.size());
        logger.info("Failed Case: " + testFailed.size());
        logger.info("Skipped Case: " + testSkipped.size());

        for (String pass : testPassed) {
            logger.info("passed case:" + pass);
        }
        for (String fail : testFailed) {
            logger.info("failed case:" + fail);
        }

        for (String skip : testSkipped) {
            logger.info("skipped case:" + skip);
        }

    }

    @Override
    public void onTestStart(ITestResult result) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testPassed.add(result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testFailed.add(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testSkipped.add(result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {

    }
}