package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;

public class PublicParmOnline {
    public int roleId=1;
    public String shopId="-1";   //门店Id
    public String shopIdZ="20032";   //门店Id 中关村店
    public String reception_sale_id="接待顾问x";
    public String name="";
    public String customerPhone = "15037286013";
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/intefer/jcBase64.txt";

    public String shichang="";
    public String shichangPassword="";

    public String carModelId="111";
    public String carModel="AMG S 63 L 4MATIC+";
    public String car_id="4624";             //小程序车id
    public String carplate="浙QWER123";      //编辑小程序车牌号

    //预约使用车辆
    public Long car_idA=4624L;
    public String shopname="中关村";
    public String modolIdAppointment = "111";
    public String carStyleId="";
    public String carStyleName="";

    public String gwname="15711200001"; //单个店的客户的数据   //登录账号

    public String gwphone="15711200001"; //单个店的客户的数据   //登录账号
    public String gwpassword="000000"; //单个店的客户的数据  登录密码

    public String jdgw="15037286011";  //xx ,属于中关村店
    public String jdgw2="15037286013";  //xx ,属于中关村店
    public String jdgwpassword="000000"; //单个店的客户的数据  登录密码
    public String jdgwName="接待顾问x"; //单个店的客户的数据  登录密码

    public String dzphone="15037286014";   //中关村店长
    public String dzcode="000000";


    public String appletTocken= EnumAppletToken.JC_XMF_ONLINE.getToken();
    public JSONArray coordinate = getCoordinate();
    public Long voucherId = 81L;  //经纬度

    public JSONArray roleList=getRoleList();

    public JSONArray getRoleList(){
        JSONArray moduleId2 = new JSONArray();
        moduleId2.add(136);
        moduleId2.add(137);
        return moduleId2;
    }

    public JSONArray getCoordinate() {
        JSONArray dd=new JSONArray();
        dd.add(39.95933);
        dd.add(116.29845);
        return dd;
    }






}
