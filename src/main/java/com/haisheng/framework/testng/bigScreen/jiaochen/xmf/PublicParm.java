package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;

public class PublicParm {
    public int roleId=1;
    public String shopId="-1";   //门店Id
    public String shopIdZ="46012";   //门店Id
    public String reception_sale_id="xx";
    public String name="";
    public String customerPhone="15037286013";
    public String filepath="src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/intefer/jcBase64.txt";

    public String shichang="";
    public String shichangPassword="";
    public String verification_code="OGWTJK";   //夏夏的核销码
    public String carModelId="198";
    public String car_id="63217";    //小程序车id
    public String carplate="浙QWER123";      //编辑小程序车牌号
    public String carplate7="津DAASSE";      //编辑小程序车牌号

    //预约使用车辆
    public Long car_idA=63213L;

    public String carStyleId="";
    public String carStyleName="";

    public String gwname="15711300001"; //单个店的客户的数据   //登录账号

    public String gwphone="15711300001"; //单个店的客户的数据   //登录账号
    public String gwpassword="000000"; //单个店的客户的数据  登录密码

    public String jdgw="13412010080";  //xx ,属于中关村店
    public String jdgwpassword="000000"; //单个店的客户的数据  登录密码
    public String jdgwName="xx"; //单个店的客户的数据  登录密码

    public String dzphone="13412010085";   //中关村店长
    public String dzcode="000000";


    public String appletTocken= EnumAppletToken.JC_XMF_DAILY.getToken();
    public String coordinate="[116.29845,39.95933]}";  //经纬度
    public Long voucherId=81L;  //经纬度

    public JSONArray roleList=getRoleList();

    public JSONArray getRoleList(){
        JSONArray moduleId2 = new JSONArray();
        moduleId2.add(136);
        moduleId2.add(137);
        return moduleId2;
    }







}
