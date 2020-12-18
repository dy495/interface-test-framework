package com.haisheng.framework.testng.bigScreen.jiaochen;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.util.DateTimeUtil;

import java.util.Arrays;
import java.util.List;

public class jiaoChenInfo {
    DateTimeUtil dt = new DateTimeUtil();
    ScenarioUtil jc = ScenarioUtil.getInstance();
    public final String logo = "general_temp/16de43e8-e98f-44d2-a005-62cab62b26ef";//120*120 品牌logo
    public final String logo2 = "general_temp/9c6fbc65-0f1f-4341-9892-1f1052b6aa04";
    public final String stringone = "a";//字符串长度1
    public final String stringten = "a2！啊A"+Integer.toString((int)((Math.random()*9+1)*10000));//字符串长度10
    public final String stringsix = "A"+ Integer.toString((int)((Math.random()*9+1)*10000));//随机字符串长度6
    public final String stringfifty = "自动化创建--ZDHZDH"+Integer.toString((int)(Math.random()*10))+"1234567890ABCDeFGHIJ啊啊啊～！@#¥%，：67890";//随机字符串长度50
    public final String stringfifty1 = "ZDHZDH"+Integer.toString((int)((Math.random()*9+1)*100000))+"1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：678901";//随机字符串长度51
    public final String string20 = "ZdH啊！_*"+System.currentTimeMillis(); //20位字符串
    public final String stringlong = "自动化"+System.currentTimeMillis()+"a2～！啊A"+Integer.toString((int)(Math.random()*1000000))+"1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：67891234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：67890101"+System.currentTimeMillis();
    public final String  district_code= "222402";
    public final String phone = "1380110"+Integer.toString((int)((Math.random()*9+1)*1000));//手机号

    //日常
    public final long BrandID = 16L;//自动化用的品牌id
    public final long CarStyleID = 50L;//自动化用的品牌车系id
    //线上
    public final long BrandIDOnline = 33L;//自动化用的品牌id
    public final long CarStyleIDOnline = 830L;//自动化用的品牌车系id

    //创建品牌，返回品牌id
    public final long getBrandID(int n){
        String name = ""+Integer.toString((int)(Math.random()*10000));
        jc.addBrand(name,logo);

        //删除品牌
        Long id = jc.brandPage(1,10,"","").getJSONArray("list").getJSONObject(0).getLong("id");

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

    public final int BUSINESS_JIAOCHEN_PC_17 = 118; public  final String name118 = "BUSINESS_JIAOCHEN_PC_17";//导入记录权限-导入记录页面
    public final int BUSINESS_JIAOCHEN_PC_18 = 119; public  final String name119 = "BUSINESS_JIAOCHEN_PC_18";//导入记录权限-导出记录页面
    public final int BUSINESS_JIAOCHEN_PC_19 =  120 ; public  final String name120 = "BUSINESS_JIAOCHEN_PC_19"; //导入记录权限-消息记录页面
    public final int BUSINESS_JIAOCHEN_PC_00111 =   136 ; public  final String name136 = "BUSINESS_JIAOCHEN_PC_00111";//数据权限-全部
    public final int BUSINESS_JIAOCHEN_PC_00112 =   142; public  final String name142 = "BUSINESS_JIAOCHEN_PC_00112"; //数据权限-个人
    public final int BUSINESS_JIAOCHEN_PC_00211 =   137 ; public  final String name137 = "BUSINESS_JIAOCHEN_PC_00211";//功能权限-售后接待
    public final int BUSINESS_JIAOCHEN_PC_00212 =   143 ; public  final String name143 = "BUSINESS_JIAOCHEN_PC_00212";//功能权限-预约保养分配
    public final int BUSINESS_JIAOCHEN_PC_00213 =   145 ; public  final String name145 = "BUSINESS_JIAOCHEN_PC_00213";//功能权限-预约应答人
    public final int BUSINESS_JIAOCHEN_PC_00214 =   147 ; public  final String name147 = "BUSINESS_JIAOCHEN_PC_00214";//功能权限-提醒接收人
    public final int BUSINESS_JIAOCHEN_PC_00311 =   138 ; public  final String name138 = "BUSINESS_JIAOCHEN_PC_00311";//主体类型权限-集团
    public final int BUSINESS_JIAOCHEN_PC_00312 =   144 ; public  final String name144 = "BUSINESS_JIAOCHEN_PC_00312";//主体类型权限-区域
    public final int BUSINESS_JIAOCHEN_PC_00313 =   146 ; public  final String name146 = "BUSINESS_JIAOCHEN_PC_00313";//主体类型权限-品牌
    public final int BUSINESS_JIAOCHEN_PC_00314 =   148 ; public  final String name148 = "BUSINESS_JIAOCHEN_PC_00314";//主体类型权限-门店
    public final int BUSINESS_JIAOCHEN_PC_2 =   104 ; public  final String name104 = "BUSINESS_JIAOCHEN_PC_2";//接待管理权限-接待管理页面
    public final int BUSINESS_JIAOCHEN_PC_3 =   105; public  final String name105 = "BUSINESS_JIAOCHEN_PC_3"; //客户管理权限-客户管理页面
    public final int BUSINESS_JIAOCHEN_PC_31 =   121; public  final String name121 = "BUSINESS_JIAOCHEN_PC_31"; //客户管理权限-销售客户tab
    public final int BUSINESS_JIAOCHEN_PC_32 =   122 ; public  final String name122 = "BUSINESS_JIAOCHEN_PC_32";//客户管理权限-售后客户tab
    public final int BUSINESS_JIAOCHEN_PC_33 =   123 ; public  final String name123 = "BUSINESS_JIAOCHEN_PC_33";//客户管理权限-小程序客户tab
    public final int BUSINESS_JIAOCHEN_PC_4 =   106; public  final String name106 = "BUSINESS_JIAOCHEN_PC_4"; //预约管理权限-预约管理页面
    public final int BUSINESS_JIAOCHEN_PC_41 =   124 ; public  final String name124 = "BUSINESS_JIAOCHEN_PC_41";//预约管理权限-预约看板tab
    public final int BUSINESS_JIAOCHEN_PC_42 =   125; public  final String name125 = "BUSINESS_JIAOCHEN_PC_42"; //预约管理权限-预约记录tab
    public final int BUSINESS_JIAOCHEN_PC_43 =   126; public  final String name126 = "BUSINESS_JIAOCHEN_PC_43"; //预约管理权限-预约配置tab
    public final int BUSINESS_JIAOCHEN_PC_44 =   127; public  final String name127 = "BUSINESS_JIAOCHEN_PC_44"; //预约管理权限-保养配置tab
    public final int BUSINESS_JIAOCHEN_PC_5 =   107; public  final String name107 = "BUSINESS_JIAOCHEN_PC_5"; //系统配置权限-门店管理页面
    public final int BUSINESS_JIAOCHEN_PC_6 =   108; public  final String name108 = "BUSINESS_JIAOCHEN_PC_6"; //系统配置权限-品牌管理页面
    public final int BUSINESS_JIAOCHEN_PC_7 =  109 ; public  final String name109 = "BUSINESS_JIAOCHEN_PC_7";//系统配置权限-角色管理页面
    public final int BUSINESS_JIAOCHEN_PC_8 =   110 ; public  final String name110 = "BUSINESS_JIAOCHEN_PC_8";//系统配置权限-员工管理页面
    public final int BUSINESS_JIAOCHEN_PC_611 =   140 ; public  final String name140 = "BUSINESS_JIAOCHEN_PC_611";//系统配置权限-品牌删除按钮
    public final int BUSINESS_JIAOCHEN_PC_9 =   111 ; public  final String name111 = "BUSINESS_JIAOCHEN_PC_9";//卡券管理权限-卡券管理页面
    public final int BUSINESS_JIAOCHEN_PC_91 =   128 ; public  final String name128 = "BUSINESS_JIAOCHEN_PC_91";//卡券管理权限-卡券表单tab
    public final int BUSINESS_JIAOCHEN_PC_92 =   129 ; public  final String name129 = "BUSINESS_JIAOCHEN_PC_92";//卡券管理权限-发卡记录tab
    public final int BUSINESS_JIAOCHEN_PC_93 =   130 ; public  final String name130 = "BUSINESS_JIAOCHEN_PC_93";//卡券管理权限-核销记录tab
    public final int BUSINESS_JIAOCHEN_PC_94 =   131 ; public  final String name131 = "BUSINESS_JIAOCHEN_PC_94";//卡券管理权限-核销人员tab
    public final int BUSINESS_JIAOCHEN_PC_10 =   112 ; public  final String name112 = "BUSINESS_JIAOCHEN_PC_10";//套餐管理权限-套餐管理页面
    public final int BUSINESS_JIAOCHEN_PC_101 =   132 ; public  final String name132 = "BUSINESS_JIAOCHEN_PC_101";//套餐管理权限-套餐表单tab
    public final int BUSINESS_JIAOCHEN_PC_102 =  133 ; public  final String name133 = "BUSINESS_JIAOCHEN_PC_102";//套餐管理权限-套餐购买记录tab
    public final int BUSINESS_JIAOCHEN_PC_1021 =  141; public  final String name141 = "BUSINESS_JIAOCHEN_PC_1021"; //套餐管理权限-确认支付按钮
    public final int BUSINESS_JIAOCHEN_PC_11 =  113; public  final String name113 = "BUSINESS_JIAOCHEN_PC_11"; //消息管理权限-消息管理页面
    public final int BUSINESS_JIAOCHEN_PC_12 =  114 ; public  final String name114 = "BUSINESS_JIAOCHEN_PC_12";//内容运营权限-内容管理页面
    public final int BUSINESS_JIAOCHEN_PC_13 =  115 ; public  final String name115 = "BUSINESS_JIAOCHEN_PC_13";//内容运营权限-报名管理页面
    public final int BUSINESS_JIAOCHEN_PC_15 =  116 ; public  final String name116 = "BUSINESS_JIAOCHEN_PC_15";//内容运营权限-banner管理页面
    public final int BUSINESS_JIAOCHEN_PC_16 =   117 ; public  final String name117 = "BUSINESS_JIAOCHEN_PC_16";//卡券申请权限-卡券申请页面

    //全部页面权限
    public final int[] allauth = {118, 119, 120, 136, 142, 137, 143, 145, 147, 138, 144, 146, 148, 104, 105, 121, 122, 123, 106,
            124, 125, 126, 127, 107, 108, 109, 110, 140, 111, 128, 129, 130, 131, 112, 132, 133, 141, 113, 114, 115, 116, 117};
    public final JSONArray allauth_id = (JSONArray) JSONArray.toJSON(allauth);

    public final String [] allauth_name = {name118, name119, name120, name136, name142, name137, name143, name145,
            name147, name138, name144, name146, name148, name104, name105, name121, name122,
            name123, name106, name124, name125, name126, name127, name107, name108, name109,
            name110, name140, name111, name128, name129, name130, name131, name112, name132,
            name133, name141, name113, name114, name115, name116, name117};
    public final List<String> allauth_list = Arrays.asList(allauth_name);



    //导入记录权限所有页面；数据权限=全部；主体类型权限=品牌；无功能权限
    public final int[] daoruauth = {118, 119, 120, 136,146};
    public final JSONArray daoruauth_id = (JSONArray) JSONArray.toJSON(daoruauth);

    public final String [] daoruauth_name = {name118, name119, name120, name136, name146};
    public final List<String> daoruauth_list = Arrays.asList(daoruauth_name);


    //接待管理所有页面；数据权限=个人；主体类型权限=门店；全部功能权限
    public final int[] jiedaiauth = {104,142,148,137,143,145,147};
    public final JSONArray jiedaiauth_id = (JSONArray) JSONArray.toJSON(jiedaiauth);

    public final String [] jiedaiauth_name = {name104, name142, name148, name137, name143,name145,name147};
    public final List<String> jiedaiauth_list = Arrays.asList(jiedaiauth_name);


    //客户管理+销售客户tab+售后客户tab；数据权限=全部；主体类型权限=集团；无功能权限
    public final int[] kehu12auth = {105,121,122,136,138};
    public final JSONArray  kehu12auth_id = (JSONArray) JSONArray.toJSON(kehu12auth);

    public final String []  kehu12auth_name = {name105, name121, name122, name136, name138};
    public final List<String>  kehu12auth_list = Arrays.asList( kehu12auth_name);

    //客户管理所有页面；数据权限=全部；主体类型权限=集团；无功能权限
    public final int[] kehu123auth = {105,121,122,123,136,138};
    public final JSONArray  kehu123auth_id = (JSONArray) JSONArray.toJSON(kehu123auth);

    public final String []  kehu123auth_name = {name105, name121, name122, name123,name136, name138};
    public final List<String>  kehu123auth_list = Arrays.asList( kehu123auth_name);

    //预约管理+预约看板tab+预约记录tab；数据权限=全部；主体类型权限=门店；功能权限=售后接待+预约保养分配+预约应答人
    public final int[] yuyue12auth = {106,124,125,148,137,143,145};
    public final JSONArray  yuyue12auth_id = (JSONArray) JSONArray.toJSON(yuyue12auth);

    public final String []  yuyue12auth_name = {name106, name124, name125, name148,name137, name143,name145};
    public final List<String>  yuyue12auth_list = Arrays.asList( yuyue12auth_name);

    //门店管理+品牌管理+品牌删除；数据权限=全部；主体类型权限=集团；无功能权限
    public final int[] xitong123auth = {107,108,140, 136, 138};
    public final JSONArray  xitong123auth_id = (JSONArray) JSONArray.toJSON(xitong123auth);

    public final String []  xitong123auth_name = {name107, name108, name140, name136,name138};
    public final List<String>  xitong123auth_list = Arrays.asList( xitong123auth_name);

    //角色管理+员工管理；数据权限=全部；主体类型权限=区域；无功能权限
    public final int[] xitong45auth = {109,110,136,144};
    public final JSONArray  xitong45auth_id = (JSONArray) JSONArray.toJSON(xitong45auth);

    public final String []  xitong45auth_name = {name109, name110, name136, name144};
    public final List<String>  xitong45auth_list = Arrays.asList( xitong45auth_name);

    //卡券管理+核销人员tab+核销记录tab；数据权限=全部；主体类型权限=门店；无功能权限
    public final int[] kaquan45auth = {111,130,131,136,148};
    public final JSONArray  kaquan45auth_id = (JSONArray) JSONArray.toJSON(kaquan45auth);

    public final String []  kaquan45auth_name = {name111, name130, name131, name136,name148};
    public final List<String>  kaquan45auth_list = Arrays.asList( kaquan45auth_name);

    //卡券管理+卡券表单tab+发卡记录tab+卡券申请页面；数据权限=全部；主体类型权限=品牌；无功能权限
    public final int[] kaquan123auth = {111, 128,129,117,136,146};
    public final JSONArray  kaquan123auth_id = (JSONArray) JSONArray.toJSON(kaquan123auth);

    public final String []  kaquan123auth_name = {name111, name128, name129, name117,name136,name146};
    public final List<String> kaquan123auth_list = Arrays.asList( kaquan123auth_name);

    //套餐管理+套餐表单tab+套餐购买记录tab+无确认支付按钮；数据权限=全部；主体类型权限=门店；无功能权限
    public final int[] taocannoauth = {112, 132,133,136,148};
    public final JSONArray  taocannoauth_id = (JSONArray) JSONArray.toJSON(taocannoauth);

    public final String [] taocannoauth_name = {name112, name132, name133, name136,name148};
    public final List<String> taocannoauth_list = Arrays.asList(taocannoauth_name);

    //套餐管理+套餐表单tab+套餐购买记录tab+有确认支付按钮；数据权限=全部；主体类型权限=门店；无功能权限
    public final int[] taocanauth = {112, 132,133,136,148,141};
    public final JSONArray  taocanauth_id = (JSONArray) JSONArray.toJSON(taocanauth);

    public final String [] taocanauth_name = {name112, name132, name133, name136,name148,name141};
    public final List<String> taocanauth_list = Arrays.asList(taocanauth_name);


    //内容运营+消息管理；数据权限=全部；主体类型权限=集团；无功能权限
    public final int[] nxauth = {113, 114,115,116};
    public final JSONArray  nxauth_id = (JSONArray) JSONArray.toJSON(nxauth);

    public final String [] nxauth_name = {name113, name114, name115, name116};
    public final List<String> nxauth_list = Arrays.asList(nxauth_name);



    //接待管理所有页面；数据权限=个人；主体类型权限=门店；功能权限无接待
    public final int[] jiedai234auth = {104,142,148,143,145,147};
    public final JSONArray jiedai234auth_id = (JSONArray) JSONArray.toJSON(jiedai234auth);

    public final String [] jiedai234auth_name = {name104, name142, name148, name143,name145,name147};
    public final List<String> jiedai234auth_list = Arrays.asList(jiedai234auth_name);





}
