package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;

public class PublicParmOnline {
    public String roleId="";
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
    public String roleidJdgw="";      //TODO:
    public String nameJdgw="接待顾问x";
    public String jdgw2="15037286013";  //xx ,属于中关村店
    public String jdgwpassword="000000"; //单个店的客户的数据  登录密码
    public String jdgwName="接待顾问x"; //单个店的客户的数据  登录密码

    public String dzphone="15037286014";   //中关村店长
    public String dzroleId="";
    public String dzcode="000000";

    //---------2.0---------
    public String String_20="一二三四五六七八九十一二三四五六七八九十";
    public String String_200="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十";
    public String StoreCommodity="1902";  //TODO:编辑需要的商品套餐的名字
    public String importFilepath="src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/importfile.xlsx";   //导入工单文件路径
    //其他小程序用户tocken和车牌号信息
    public String getAppletTockenOther="R4M7UpQ2+Q3L0QbxGA32iA==";
    public  String CarplateOther="浙ZXCV123";
    public String userid="uid_0216a935"; //xx的userId;
    public String userid2="uid_0216a935"; //xx2的userId;

    public int ordeId=10; // 订单Id
    public String filepath11="src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/1_1.jpg";
    //---------2.0---------
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
