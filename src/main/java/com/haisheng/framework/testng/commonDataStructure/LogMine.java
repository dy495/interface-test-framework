package com.haisheng.framework.testng.commonDataStructure;

import org.slf4j.Logger;

public class LogMine {

    private final Logger logger;
    private String ENV = System.getProperty("ENV_INFO");

    public LogMine(Logger logger) {

        this.logger = logger;
        if (null == ENV || 0 == ENV.trim().length()) {
            ENV = "DAILY";
        }
    }

    public String getENV() {
        return this.ENV;
    }

    public void printImportant(String info) {
        logger.info("[ENV-" + ENV + "]" + info);
    }

    public void info(String info) {
        logger.info("[ENV-" + ENV + "]" + info);
    }

    public void error(String info) {
        logger.error("[ENV-" + ENV + "]" + info);
    }

    public void debug(String info) {
        logger.debug("[ENV-" + ENV + "]" + info);
    }

    public void warn(String info) {
        logger.warn("[ENV-" + ENV + "]" + info);
    }

    public void logCase(String info) {
        logger.info("==================================");
        logger.info("[ENV-" + ENV + "]" + "[CASE]" + info);
        logger.info("==================================");
    }

    public void logCaseStart(String caseName) {
        logger.info("==================================");
        logger.info("[ENV-" + ENV + "]" + "[CASE-START] " + caseName);
        logger.info("==================================");
    }

    public void logCaseEnd(boolean isSuccess, String caseName) {
        String result = "FAIL";
        if (isSuccess) {
            result = "PASS";
        }
        logger.info("[ENV-" + ENV + "]" + "[CASE-END]" + "[" + result + "] " + caseName);
    }

    public void logStep(String info) {
        logger.info("");
        logger.info("[ENV-" + ENV + "]" + ">>>>>>step: " + info);
    }

    public void info(String var1, Object... var2) {
        logger.info(var1, var2);
    }
}
