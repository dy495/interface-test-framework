package com.haisheng.framework.testng.bigScreen.yuntong.gly.Util;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.customermanage.PreSaleCustomerPageScene;
import com.haisheng.framework.util.DateTimeUtil;

import java.util.Date;

public class BusinessUtil {
    private static EnumTestProduce product=null;
    public VisitorProxy visitor = new VisitorProxy(product);
    YunTongUtil yt=new YunTongUtil(product);


    public BusinessUtil(EnumTestProduce product){
        this.product=product;
    }

    /**
     * 运通的登录
     */
    public void loginIn(){
        IScene scene= PreSaleCustomerPageScene.builder().build();
    }

    /**
     * 门店管理--通过门店的名字查找门店的ID
     */
    public Long getStoreId(String shopName){
        //获取门店的list地址
        JSONArray list=yt.userShopList().getJSONArray("list");
        Long shopId=0L;
        //遍历shopId与传入一致的门店名称
        for(int i=0;i<list.size();i++){
            String shopName1=list.getJSONObject(i).getString("shop_name");
            if(shopName1.equals(shopName)){
                shopId=list.getJSONObject(i).getLong("shop_id");
            }
        }
        return shopId;
    }

    /**
     * 门店管理--通过门店的名字查找门店的ID
     */
    public Long getStyleId(String styleName) {
        //获取门店的list地址
        JSONArray list = yt.preSaleStyleList().getJSONArray("list");
        Long styleId = 0L;
        //遍历styleId与传入一致的门店名称
        for (int i = 0; i < list.size(); i++) {
            String styleName1 = list.getJSONObject(i).getString("shop_name");
            if (styleName.equals(styleName1)) {
                styleId = list.getJSONObject(i).getLong("style_id");
            }
        }
        return styleId;

    }

    /**
     * 随机四位数
     */
    public int randomNumber(){
        return (int)Math.random()*1000;
    }

    /**
     * 获取当天的时间（年月日）
     */
    public String getDateNow(){
        return  DateTimeUtil.getFormat(new Date(),"YYYY-MM-dd HH:mm ");

    }






}
