package com.haisheng.framework.testng.study.testng.groups;

import org.testng.annotations.Test;

/*
* 1、“分组”可以在类上应用。 在下面的示例中，“TestSelenium”类的每个公共方法都属于分组：selenium-test 。
* 2、测试方法也可以同时属于多个分组
 */

//“TestSelenium”类的每个公共方法都属于分组：selenium-test 。
@Test(groups = "selenium-test")
public class TestSelenium {

    public void runSelenium(){
        System.out.println("runSelenium()");
    }

    public void runSelenium1(){
        System.out.println("runSelenium1()");
    }


//    测试方法也可以同时属于多个分组
    @Test(groups = {"mysql","database"})
    public void testMultiGrp(){
        System.out.println("testMultiGrp()");
    }
}
