package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;

public class PublicParm {
    public int roleId=1;
    public String shopId="";   //门店Id
    public String reception_sale_id="";
    public String name="";

    public String shichang="";
    public String shichangPassword="";


    public String car_id="";    //小程序车id
    public String carplate="";      //编辑小程序车牌号

    public String carStyleId="";
    public String carStyleName="";

    public String carModelId="151";
    public String gwname="15711300001"; //单个店的客户的数据   //登录账号

    public String gwphone="15711300001"; //单个店的客户的数据   //登录账号
    public String gwpassword="000000"; //单个店的客户的数据  登录密码

    public String appletTocken="wYznhF2kOIMCL7otZeH8iA==";
    public String coordinate="";  //经纬度

    public JSONArray roleList=getRoleList();

    public JSONArray getRoleList(){
        JSONArray moduleId2 = new JSONArray();
        moduleId2.add(136);
        moduleId2.add(137);
        return moduleId2;
    }






}
