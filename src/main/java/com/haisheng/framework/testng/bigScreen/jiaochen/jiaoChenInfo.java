package com.haisheng.framework.testng.bigScreen.jiaochen;

import com.haisheng.framework.util.DateTimeUtil;

public class jiaoChenInfo {
    DateTimeUtil dt = new DateTimeUtil();
    ScenarioUtil jc = ScenarioUtil.getInstance();
    public final String logo = "general_temp/16de43e8-e98f-44d2-a005-62cab62b26ef";//120*120 品牌logo
    public final String stringone = "a";//字符串长度1
    public final String stringten = "a2！啊A"+Integer.toString((int)(Math.random()*100000));//字符串长度10
    public final String stringsix = "A"+ Integer.toString((int)(Math.random()*100000));//随机字符串长度6
    public final String stringfifty = "ZDHZDH"+Integer.toString((int)(Math.random()*1000000))+"1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：67890";//随机字符串长度50
    public final String stringfifty1 = "ZDHZDH"+Integer.toString((int)(Math.random()*1000000))+"1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：678901";//随机字符串长度51
    public final String string20 = "ZdH啊！_*"+System.currentTimeMillis(); //20位字符串
    public final String stringlong = "自动化"+System.currentTimeMillis()+"a2～！啊A"+Integer.toString((int)(Math.random()*1000000))+"1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：67891234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：67890101"+System.currentTimeMillis();
    public final long BrandID = 16L;//自动化用的品牌id
    public final long CarStyleID = 50L;//自动化用的品牌车系id
    public final String  district_code= "222402";
    public final String phone = "1380110"+Integer.toString((int)(Math.random()*10000));//手机号



    //创建品牌，返回品牌id
    public final long getBrandID(int n){
        String name = ""+Integer.toString((int)(Math.random()*10000));
        jc.addBrand(name,logo);

        //删除品牌
        int size= jc.brandPage(1,1,"","").getInteger("total") - 1;

        Long id = jc.brandPage(1,size+1,"","").getJSONArray("list").getJSONObject(size).getLong("id");

        return id;
    }

    //创建某品牌下的车系，返回车系id
    public final long getCarStyleID(long id, int n){
        //创建车系
        String manufacturer = "自动化"+System.currentTimeMillis();
        String name= "自动化"+System.currentTimeMillis();
        String online_time= dt.getHistoryDate(0);
        jc.addCarStyle(id, manufacturer,  name,  online_time);
        //获取车系id
        Long carStyleId = jc.carStylePage(1,1,id,name).getJSONArray("list").getJSONObject(0).getLong("id");
        return carStyleId;
    }

    //创建某品牌车系下的车型，返回车型id
    public final long getCarStyleID(long brand_id, long carStyle_id){
        //创建车型
        String name1 = "自动化"+System.currentTimeMillis();
        String year1= dt.getHistoryDate(-100);
        String status1 = "ENABLE";
        jc.addCarModel(brand_id, carStyle_id,  name1,year1,  status1);
        //获取车系id
        Long id = jc.carModelPage(1,1,brand_id, carStyle_id,name1,"","").getJSONArray("list").getJSONObject(0).getLong("id");
        return  id;
    }







}
