package com.haisheng.framework.testng.study.testng.exception.checked;

public class OrderNotFoundException extends Exception{
    public OrderNotFoundException(String message){
        super(message);
    }
}
