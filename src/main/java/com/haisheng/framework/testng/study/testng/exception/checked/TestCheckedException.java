package com.haisheng.framework.testng.study.exception.checked;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/*用自定义的类设置expectedExceptions*/

public class TestCheckedException {

    OrderBo orderBo;
    OrderImpl data;

    @BeforeTest
    void setUp(){
        orderBo = new OrderBo();
        data = new OrderImpl();

        data.setId(100);
        data.setCreateBy("sophie");
    }

    @Test(expectedExceptions = OrderSaveException.class)
    public void throwIforderIsNull() throws OrderSaveException{
        orderBo.save(null);
    }

    @Test(expectedExceptions = {OrderUpdateException.class,OrderNotFoundException.class})
    public void throwIfOrderIsNotExists() throws OrderNotFoundException, OrderUpdateException {
        orderBo.update(data);
    }


}
