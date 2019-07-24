package com.haisheng.framework.testng.study.testng.dependson;

import org.testng.annotations.Test;

public class DependsOnMethod {

    @Test
    public void method1() {
        System.out.println("this is method 1");
        throw new RuntimeException();
    }

//    如果被依赖的方法报错，那么遗爱该方法的方法会被跳过，不会执行
//    如果method1失败，那么method2会被跳过
    @Test(dependsOnMethods = {"method1"})
    public void method2() {
        System.out.println("this is method 2");
    }
}
