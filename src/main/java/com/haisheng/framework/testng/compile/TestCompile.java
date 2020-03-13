package com.haisheng.framework.testng.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class TestCompile {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testCompile() {

        logger.info("\n===================\n"
                + "===Compile PASS====\n"
                + "===================");
    }
}
