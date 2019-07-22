package com.haisheng.framework.testng.study.testng.timeout;

import org.testng.annotations.Test;


/*
“超时”表示如果单元测试花费的时间超过指定的毫秒数，那么TestNG将会中止它并将其标记为失败。
“超时”也可用于性能测试，以确保方法在合理的时间内返回。
*/
public class TestTimeout {

    @Test(timeOut = 3000)
    public void testPass() throws InterruptedException {
        Thread.sleep(2000);
    }

    @Test(timeOut = 2000)
    public void testFail() throws InterruptedException {
        Thread.sleep(3000);
    }

}
