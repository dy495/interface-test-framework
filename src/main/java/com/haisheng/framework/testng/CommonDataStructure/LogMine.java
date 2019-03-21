package com.haisheng.framework.testng.CommonDataStructure;

import org.slf4j.Logger;

public class LogMine {

    private Logger logger;

    public LogMine(Logger logger) {
        this.logger = logger;
    }

    public void printImportant(String info){
        logger.info("");
        logger.info("");
        logger.info(info);
        logger.info("");
        logger.info("");
    }
    public void logCase(String info){
        logger.info("==================================");
        logger.info("case: " + info);
        logger.info("==================================");
    }
    public void logStep(String info){
        logger.info("");
        logger.info(">>>>>>step: " + info);
    }
}
