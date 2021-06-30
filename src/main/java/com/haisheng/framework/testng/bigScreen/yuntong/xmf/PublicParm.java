package com.haisheng.framework.testng.bigScreen.yuntong.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.enumerator.config.EnumAppletToken;


public class PublicParm {
    //3.0
    public Long brandId=1L;  //TODO:
    public String user;
    public String userpassword;
    public String userroleId="4965";
    public String voucherIdevluate="3273";  //评价配置使用的卡券id    评价任务专用

    //3.0

    public int num = 4;
    public String roleId = "603";
    public String gwphone = "13114785236"; //超级管理员
    public String gwpassword = "000000"; //单个店的客户的数据  登录密码

    public String shopId = "-1";   //门店Id
    public String shopIdZ = "49195";   //门店Id

    public String shopname="中关村店(简称)";
    public String reception_sale_id = "2945";
    public String name = "";
    public String customerPhone = "15037286013";
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/intefer/jcBase64.txt";
    public String shichang = "";
    public String shichangPassword = "";
    public String verification_code = "3MR9C7";   //夏夏的核销码
    public String carModelId = "212";
    public String car_id = "24";    //小程序车id
    public String carplate = "津DAASSE";      //编辑小程序车牌号
    public String carplate7 = "津DAASSE";      //编辑小程序车牌号
    public String carplate8 = "吉GBBA10";      //编辑小程序车牌号

    //预约使用车辆
    public Long car_idA =48L;  //plate_number=津DAASSE
    public String modolIdAppointment = "212";
    public String carModel="Model 3";
    public String carStyleId = "";
    public String carStyleName = "";

    public String gwname = "13402050050"; //单个店的客户的数据   //登录账号



    public String jdgw = "13402050050";  //xx ,属于中关村店
    public String roleidJdgw="2945"; //拥有接待权限的校色id
    public String nameJdgw="自动化专用账号"; //拥有接待权限的校色id
    public String jdgw2 = "13402050050";  //xx2 ,属于中关村店
    public String jdgwpassword = "000000"; //单个店的客户的数据  登录密码
    public String jdgwName = "自动化专用账号"; //单个店的客户的数据  登录密码

    public String dzphone = "13402050049";   //中关村店长
    public String dzroleId="2946";
    public String dzcode = "000000";

    //---------2.0---------
    public String String_50="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十";
    public String String_20="一二三四五六七八九十一二三四五六七八九十";
    public String String_200="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十";
    public String StoreCommodityId="85";  //TODO:编辑需要的商品套餐的名字
    public String importFilepath="src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/测试.xlsx";   //导入工单文件路径
    public String importFilepath2="src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/importfile2.xlsx";   //导入工单文件路径
    public String importFilepath3="src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/importPotentialCustomerfile.xlsx";   //导入工单文件路径
    //其他小程序用户tocken和车牌号信息
    public String getAppletTockenOther=EnumAppletToken.JC_XMF_DAILY.getToken();
    public  String CarplateOther="豫GBBA11";
    public String userid="uid_0216a935"; //xx的userId;
    public String userid2="uid_5eabfb9c"; //店长的userId;

    //3.0
    public String useridxs="uid_0216a935"; //销售的userId;


    public int ordeId=10; // 订单Id
    public String filepath11="src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/1_1.jpg";
    //---------2.0---------

    public String appletTocken = EnumAppletToken.JC_XMF_DAILY.getToken();
    public JSONArray coordinate = getCoordinate();
    public Long voucherId = 81L;

    public JSONArray roleList = getRoleList();
    public JSONArray vouchers = getvouchersList();
    public JSONArray vouchers2 =voucherSingle() ;


    public JSONArray getvouchersList() {   //创建精品套餐时创建的卡券数组
        JSONObject vouletList=new JSONObject();
        vouletList.put("effective_day",1);
        vouletList.put("voucher_count",1);
        vouletList.put("voucher_id",69);
        vouletList.put("voucher_name","测试专用优惠券");
        JSONArray moduleId2 = new JSONArray();
        moduleId2.add(vouletList);
        return moduleId2;
    }
    public JSONArray voucherSingle(){
        JSONArray moduleId2 = new JSONArray();
        moduleId2.add(69);
        return moduleId2;
    }

    public JSONArray getRoleList() {
        JSONArray moduleId2 = new JSONArray();
        moduleId2.add(104);
        moduleId2.add(148);
        moduleId2.add(142);
        return moduleId2;
    }

    public JSONArray getCoordinate() {
       JSONArray dd=new JSONArray();
       dd.add(39.95933);
       dd.add(116.29845);
        return dd;
    }


}
