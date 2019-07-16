package com.haisheng.framework.testng.study.exception.expected;

import org.testng.annotations.Test;

public class TestRuntime {

    /*-------------------------运行时异常----------------------------------*/

    //如果divisionWithException()方法抛出一个运行时异常 — ArithmeticException，它会获得通过。
    @Test(expectedExceptions = ArithmeticException.class)
    public void divisionWithException(){
        int i = 1/0;
        System.out.println("After division the value of i is:" + i);
    }


}
