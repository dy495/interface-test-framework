package com.haisheng.framework.testng.bigScreen.itemYuntong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class ThreadTest {
    private static final Logger logger = LoggerFactory.getLogger(ThreadTest.class);

    @Test()
    public void test1() throws InterruptedException {
        Thread.sleep(2);
        long id = Thread.currentThread().getId();
        logger.info("test1-1 thread id:{}", id);
    }

    @Test
    public void test2() throws InterruptedException {
        Thread.sleep(3);
        long id = Thread.currentThread().getId();
        logger.info("test1-2 thread id:{}", id);
    }
}

