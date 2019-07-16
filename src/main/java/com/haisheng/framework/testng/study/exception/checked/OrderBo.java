package com.haisheng.framework.testng.study.exception.checked;

import org.springframework.core.annotation.Order;

public class OrderBo {

    public void save(Order order) throws OrderSaveException {

        if(order == null){
            throw new OrderSaveException("order is empty!");
        }
    }

    public void update(Order order) throws OrderUpdateException, OrderNotFoundException {
        if(order ==null){
            throw new OrderUpdateException("order is empty！");
        }

        //if order is not available in database
        throw new OrderNotFoundException("order is not exists");

    }
}
