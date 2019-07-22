package com.haisheng.framework.testng.study.testng.ignore;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestIgnore {

    /*
    * 有时，我们编写的代码并没有准备就绪，并且测试用例要测试该方法/代码是否失败(或成功)，就可以用注释@Test(enabled = false)禁用此测试用例。
      如果使用@Test(enabled = false)注释在测试方法上，则会绕过这个未准备好测试的测试用例。
      如果不加enabled参数的话，就会默认为enabled = true,就不会绕过。
    * */

    @Test//default enabled = true
    public void test1() {
        System.out.println("test1,enabled = true");
        Assert.assertEquals(true, true);
    }

    @Test(enabled = true)
    public void test2() {
        System.out.println("test2,enabled = true");

        Assert.assertEquals(true, true);
    }

    //???????????------------------为什么在控制台show ignore后没有显示该方法呢-----------------
    @Test(enabled = false)
//    @Test
    public void test3() {
        System.out.println("test3,enabled = false");
        Assert.assertEquals(true, true);
    }
}
