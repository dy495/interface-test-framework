package com.haisheng.framework.testng.study.exception.checked;

import org.springframework.core.annotation.Order;

import java.lang.annotation.Annotation;

public class OrderImpl implements Order {
    int id;
    String createBy;

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int value() {
        return 0;
    }


    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
