package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONArray;

public class PublicParmOnline {
    public int roleId=1;
    public String shopId="-1";   //门店Id
    public String shopIdZ="20032";   //门店Id
    public String reception_sale_id="接待顾问x";
    public String name="";

    public String shichang="";
    public String shichangPassword="";

    public String carModelId="111";
    public String car_id="4624";    //小程序车id
    public String carplate="浙QWER123";      //编辑小程序车牌号

    //预约使用车辆
    public Long car_idA=63213L;

    public String carStyleId="";
    public String carStyleName="";

    public String gwname="15711200001"; //单个店的客户的数据   //登录账号

    public String gwphone="15711200001"; //单个店的客户的数据   //登录账号
    public String gwpassword="000000"; //单个店的客户的数据  登录密码

    public String jdgw="15037286011";  //xx ,属于中关村店
    public String jdgwpassword="000000"; //单个店的客户的数据  登录密码
    public String jdgwName="接待顾问x"; //单个店的客户的数据  登录密码

    public String dzphone="15037286014";   //中关村店长
    public String dzcode="000000";


    public String appletTocken="7kTQENEfxhu28uOOZZoJiQ==";
    public String coordinate="[116.29845,39.95933]}";  //经纬度

    public JSONArray roleList=getRoleList();

    public JSONArray getRoleList(){
        JSONArray moduleId2 = new JSONArray();
        moduleId2.add(136);
        moduleId2.add(137);
        return moduleId2;
    }






}
