package com.haisheng.framework.testng.bigScreen.yuntong.gly.Util;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.customermanage.PreSaleCustomerPageScene;

public class MethodsCollection {
    private static EnumTestProduce product=null;



    private MethodsCollection(EnumTestProduce product){
        this.product=product;
    }

    /**
     * 运通的登录
     */
    public void loginIn(){
        IScene scene= PreSaleCustomerPageScene.builder().build();
    }













}
