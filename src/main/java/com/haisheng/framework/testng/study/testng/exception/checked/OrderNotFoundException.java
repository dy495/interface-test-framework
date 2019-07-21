package com.haisheng.framework.testng.study.exception.checked;

public class OrderNotFoundException extends Exception{
    public OrderNotFoundException(String message){
        super(message);
    }
}
