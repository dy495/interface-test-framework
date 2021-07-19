package com.haisheng.framework.testng.bigScreen.itemYuntong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class ThreadTest2 {
    private static final Logger logger = LoggerFactory.getLogger(ThreadTest2.class);

    @Test()
    public void test1() throws InterruptedException {
        Thread.sleep(4);
        long id = Thread.currentThread().getId();
        logger.info("test2-1 thread id:{}", id);
    }

    @Test
    public void test2() throws InterruptedException {
        Thread.sleep(5);
        long id = Thread.currentThread().getId();
        logger.info("test2-2 thread id:{}", id);
    }
}

