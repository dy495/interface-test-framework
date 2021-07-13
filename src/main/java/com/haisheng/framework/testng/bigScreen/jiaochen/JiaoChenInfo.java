package com.haisheng.framework.testng.bigScreen.jiaochen;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.model.AppletModeListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppAdmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppStartReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.brand.BrandPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.EvaluateConfigSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop.PageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyBatchApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.AddVoucherScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormVoucherPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class JiaoChenInfo {
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    ScenarioUtil jc = ScenarioUtil.getInstance();
    public String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
    public final String logo = "general_temp/9a215339-d897-4516-a1e6-3dee1f4021f8";//120*120 品牌logo
    public final String logo2 = "general_temp/9c6fbc65-0f1f-4341-9892-1f1052b6aa04";
    public final String stringone = "a";//字符串长度1
    public final String stringten = "a2！啊A" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));//字符串长度10
    public final String stringsix = "A" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));//随机字符串长度6
    public final String stringfifty = "自动化创建--ZDHZDH" + Integer.toString((int) (Math.random() * 10)) + "1234567890ABCDeFGHIJ啊啊啊～！@#¥%，：67890";//随机字符串长度50
    public final String stringfifty1 = "ZDHZDH" + Integer.toString((int) ((Math.random() * 9 + 1) * 100000)) + "1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：678901";//随机字符串长度51
    public final String string20 = "ZdH啊！_*" + System.currentTimeMillis(); //20位字符串
    public final String string200 = "自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AA";
    public final String stringlong = "自动化" + System.currentTimeMillis() + "a2～！啊A" + Integer.toString((int) (Math.random() * 1000000)) + "1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：67891234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：67890101" + System.currentTimeMillis();
    public final String district_code = "222402";
    public final String phone = "1380110" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));//手机号
    public final String sitphone1 = "010-8159" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));
    ;//座机号
    public final String sitphone2 = "0433-381" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));
    ;//座机号
    public final String donephone = "15843317232";//成交客户手机号
    public final Long oneshopid = 46439L;

    public final String ONLINE_EXPERTS = "ONLINE_EXPERTS"; //在线专家
    public final String SALES = "SALES"; //专属销售
    public final String AFTER_SALES = "AFTER_SALES"; //专属售后

    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_LXQ_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.JC_ALL_AUTHORITY_DAILY_LXQ;
    public UserUtil user = new UserUtil(visitor);
    //日常
    public final long BrandID = 61L;//自动化用的品牌id
    public final long CarStyleID = 48L;//自动化用的品牌车系id

    //创建品牌，返回品牌id
    public final long getBrandID(int n) {
        String name = "" + Integer.toString((int) (Math.random() * 10000));
        Long id = jc.addBrand(name, getLogo()).getLong("id");
        return id;
    }


    //创建某品牌下的车系，返回车系id
    public final long getCarStyleID(long id, int n) {
        //创建车系
        String manufacturer = "自动化" + System.currentTimeMillis();
        String name = "自动化" + System.currentTimeMillis();
        String online_time = dt.getHistoryDate(0);
        jc.addCarStyle(id, manufacturer, name, online_time);
        //获取车系id
        Long carStyleId = jc.carStylePage(1, 1, id, name).getJSONArray("list").getJSONObject(0).getLong("id");
        return carStyleId;
    }

    //创建某品牌车系下的车型，返回车型id
    public final long getCarStyleID(long brand_id, long carStyle_id) {
        //创建车型
        String name1 = "自动化" + System.currentTimeMillis();
        String year1 = dt.getHistoryDate(-100);
        String status1 = "ENABLE";
        jc.addCarModel(brand_id, carStyle_id, name1, year1, status1);
        //获取车系id
        Long id = jc.carModelPage(1, 1, brand_id, carStyle_id, name1, "", "").getJSONArray("list").getJSONObject(0).getLong("id");
        return id;
    }

    public final int BUSINESS_JIAOCHEN_PC_17 = 118;
    public final String name118 = "BUSINESS_JIAOCHEN_PC_17";//导入记录权限-导入记录页面
    public final int BUSINESS_JIAOCHEN_PC_18 = 119;
    public final String name119 = "BUSINESS_JIAOCHEN_PC_18";//导入记录权限-导出记录页面
    public final int BUSINESS_JIAOCHEN_PC_19 = 120;
    public final String name120 = "BUSINESS_JIAOCHEN_PC_19"; //导入记录权限-消息记录页面
    public final int BUSINESS_JIAOCHEN_PC_00111 = 136;
    public final String name136 = "BUSINESS_JIAOCHEN_PC_00111";//数据权限-全部
    public final int BUSINESS_JIAOCHEN_PC_00112 = 142;
    public final String name142 = "BUSINESS_JIAOCHEN_PC_00112"; //数据权限-个人
    public final int BUSINESS_JIAOCHEN_PC_00211 = 137;
    public final String name137 = "BUSINESS_JIAOCHEN_PC_00211";//功能权限-售后接待
    public final int BUSINESS_JIAOCHEN_PC_00212 = 143;
    public final String name143 = "BUSINESS_JIAOCHEN_PC_00212";//功能权限-预约保养分配
    public final int BUSINESS_JIAOCHEN_PC_00213 = 145;
    public final String name145 = "BUSINESS_JIAOCHEN_PC_00213";//功能权限-预约应答人
    public final int BUSINESS_JIAOCHEN_PC_00214 = 147;
    public final String name147 = "BUSINESS_JIAOCHEN_PC_00214";//功能权限-提醒接收人
    public final int BUSINESS_JIAOCHEN_PC_00311 = 138;
    public final String name138 = "BUSINESS_JIAOCHEN_PC_00311";//主体类型权限-集团
    public final int BUSINESS_JIAOCHEN_PC_00312 = 144;
    public final String name144 = "BUSINESS_JIAOCHEN_PC_00312";//主体类型权限-区域
    public final int BUSINESS_JIAOCHEN_PC_00313 = 146;
    public final String name146 = "BUSINESS_JIAOCHEN_PC_00313";//主体类型权限-品牌
    public final int BUSINESS_JIAOCHEN_PC_00314 = 148;
    public final String name148 = "BUSINESS_JIAOCHEN_PC_00314";//主体类型权限-门店
    public final int BUSINESS_JIAOCHEN_PC_2 = 104;
    public final String name104 = "BUSINESS_JIAOCHEN_PC_2";//接待管理权限-接待管理页面
    public final int BUSINESS_JIAOCHEN_PC_3 = 105;
    public final String name105 = "BUSINESS_JIAOCHEN_PC_3"; //客户管理权限-客户管理页面
    public final int BUSINESS_JIAOCHEN_PC_31 = 121;
    public final String name121 = "BUSINESS_JIAOCHEN_PC_31"; //客户管理权限-销售客户tab
    public final int BUSINESS_JIAOCHEN_PC_32 = 122;
    public final String name122 = "BUSINESS_JIAOCHEN_PC_32";//客户管理权限-售后客户tab
    public final int BUSINESS_JIAOCHEN_PC_33 = 123;
    public final String name123 = "BUSINESS_JIAOCHEN_PC_33";//客户管理权限-小程序客户tab
    public final int BUSINESS_JIAOCHEN_PC_4 = 106;
    public final String name106 = "BUSINESS_JIAOCHEN_PC_4"; //预约管理权限-预约管理页面
    public final int BUSINESS_JIAOCHEN_PC_41 = 124;
    public final String name124 = "BUSINESS_JIAOCHEN_PC_41";//预约管理权限-预约看板tab
    public final int BUSINESS_JIAOCHEN_PC_42 = 125;
    public final String name125 = "BUSINESS_JIAOCHEN_PC_42"; //预约管理权限-预约记录tab
    public final int BUSINESS_JIAOCHEN_PC_43 = 126;
    public final String name126 = "BUSINESS_JIAOCHEN_PC_43"; //预约管理权限-预约配置tab
    public final int BUSINESS_JIAOCHEN_PC_44 = 127;
    public final String name127 = "BUSINESS_JIAOCHEN_PC_44"; //预约管理权限-保养配置tab
    public final int BUSINESS_JIAOCHEN_PC_5 = 107;
    public final String name107 = "BUSINESS_JIAOCHEN_PC_5"; //系统配置权限-门店管理页面
    public final int BUSINESS_JIAOCHEN_PC_6 = 108;
    public final String name108 = "BUSINESS_JIAOCHEN_PC_6"; //系统配置权限-品牌管理页面
    public final int BUSINESS_JIAOCHEN_PC_7 = 109;
    public final String name109 = "BUSINESS_JIAOCHEN_PC_7";//系统配置权限-角色管理页面
    public final int BUSINESS_JIAOCHEN_PC_8 = 110;
    public final String name110 = "BUSINESS_JIAOCHEN_PC_8";//系统配置权限-员工管理页面
    public final int BUSINESS_JIAOCHEN_PC_611 = 140;
    public final String name140 = "BUSINESS_JIAOCHEN_PC_611";//系统配置权限-品牌删除按钮
    //    public final int BUSINESS_JIAOCHEN_PC_9 =   111 ; public  final String name111 = "BUSINESS_JIAOCHEN_PC_9";//卡券管理权限-卡券管理页面 V2.0取消
//    public final int BUSINESS_JIAOCHEN_PC_91 =   128 ; public  final String name128 = "BUSINESS_JIAOCHEN_PC_91";//卡券管理权限-卡券表单tab V2.0取消
//    public final int BUSINESS_JIAOCHEN_PC_92 =   129 ; public  final String name129 = "BUSINESS_JIAOCHEN_PC_92";//卡券管理权限-发卡记录tab V2.0取消
//    public final int BUSINESS_JIAOCHEN_PC_93 =   130 ; public  final String name130 = "BUSINESS_JIAOCHEN_PC_93";//卡券管理权限-核销记录tab V2.0取消
    public final int BUSINESS_JIAOCHEN_PC_94 = 131;
    public final String name131 = "BUSINESS_JIAOCHEN_PC_94";//卡券管理权限-核销人员tab
    public final int BUSINESS_JIAOCHEN_PC_10 = 112;
    public final String name112 = "BUSINESS_JIAOCHEN_PC_10";//套餐管理权限-套餐管理页面
    public final int BUSINESS_JIAOCHEN_PC_101 = 132;
    public final String name132 = "BUSINESS_JIAOCHEN_PC_101";//套餐管理权限-套餐表单tab
    public final int BUSINESS_JIAOCHEN_PC_102 = 133;
    public final String name133 = "BUSINESS_JIAOCHEN_PC_102";//套餐管理权限-套餐购买记录tab
    public final int BUSINESS_JIAOCHEN_PC_1021 = 141;
    public final String name141 = "BUSINESS_JIAOCHEN_PC_1021"; //套餐管理权限-确认支付按钮
    public final int BUSINESS_JIAOCHEN_PC_11 = 113;
    public final String name113 = "BUSINESS_JIAOCHEN_PC_11"; //消息管理权限-消息管理页面
    public final int BUSINESS_JIAOCHEN_PC_12 = 114;
    public final String name114 = "BUSINESS_JIAOCHEN_PC_12";//内容运营权限-内容管理页面
    //    public final int BUSINESS_JIAOCHEN_PC_13 =  115 ; public  final String name115 = "BUSINESS_JIAOCHEN_PC_13";//内容运营权限-报名管理页面 V2.0取消
    public final int BUSINESS_JIAOCHEN_PC_15 = 116;
    public final String name116 = "BUSINESS_JIAOCHEN_PC_15";//内容运营权限-banner管理页面
//    public final int BUSINESS_JIAOCHEN_PC_16 =   117 ; public  final String name117 = "BUSINESS_JIAOCHEN_PC_16";//卡券申请权限-卡券申请页面 V2.0取消

    //V2.0新增
//    public final int BUSINESS_JIAOCHEN_PC_000212 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_000212"; // 功能权限-导出
//    public final int BUSINESS_JIAOCHEN_PC_00211 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_00211"; // 功能权限-售后接待
//    public final int BUSINESS_JIAOCHEN_PC_0002112 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_0002112"; // 功能权限-变更售后接待
//    public final int BUSINESS_JIAOCHEN_PC_1011 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_1011"; // 临时套餐套餐表单
//    public final int BUSINESS_JIAOCHEN_PC_45 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_45"; // 预约维修配置
//    public final int BUSINESS_JIAOCHEN_PC_201 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_201"; // 评价列表
//    public final int BUSINESS_JIAOCHEN_PC_202 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_202"; // 页面-售后评价配置
//    public final int BUSINESS_JIAOCHEN_PC_2111 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_2111"; // 功能-售后评价差评接收人
//    public final int BUSINESS_JIAOCHEN_PC_203 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_203"; // 页面-销售评价配置
//    public final int BUSINESS_JIAOCHEN_PC_2211 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_2211"; // 功能-销售评价差评接收人
//    public final int BUSINESS_JIAOCHEN_PC_23 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_23"; // 页面-智能提醒
//    public final int BUSINESS_JIAOCHEN_PC_24 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_24"; // 页面-道路救援
//    public final int BUSINESS_JIAOCHEN_PC_711 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_711"; // 功能权限-全员核销
//    public final int BUSINESS_JIAOCHEN_PC_712 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_712"; // 功能权限-核销权限
//    public final int BUSINESS_JIAOCHEN_PC_25 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_25"; // 页面-优惠券管理
//    public final int BUSINESS_JIAOCHEN_PC_16 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_16"; // 页面-卡券申请
//    public final int BUSINESS_JIAOCHEN_PC_27 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_27"; // 页面-活动管理
//    public final int BUSINESS_JIAOCHEN_PC_28 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_28"; // 页面-会员营销
//    public final int BUSINESS_JIAOCHEN_PC_2511 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_2511"; // 按钮-优惠券作废
//    public final int BUSINESS_JIAOCHEN_PC_2512 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_2512"; // 按钮-优惠券删除
//    public final int BUSINESS_JIAOCHEN_PC_161 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_161"; // 卡券申请？？？
//    public final int BUSINESS_JIAOCHEN_PC_271 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_271"; // 活动审批？？？
//    public final int BUSINESS_JIAOCHEN_PC_40 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_40"; // 页面-删除记录
//    public final int BUSINESS_JIAOCHEN_PC_46 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_46"; // 页面-商品运营-商品管理
//    public final int BUSINESS_JIAOCHEN_PC_47 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_47"; // 页面-商品运营-商品品类
//    public final int BUSINESS_JIAOCHEN_PC_48 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_48"; // 页面-商品运营-商品品牌
//    public final int BUSINESS_JIAOCHEN_PC_49 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_49"; // 页面-商品运营-商品规格
//    public final int BUSINESS_JIAOCHEN_PC_50 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_50"; // 页面-积分中心-积分兑换
//    public final int BUSINESS_JIAOCHEN_PC_51 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_51"; // 页面-积分中心-积分明细
//    public final int BUSINESS_JIAOCHEN_PC_52 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_52"; // 页面-积分中心-积分订单
//    public final int BUSINESS_JIAOCHEN_PC_53 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_53"; // 页面-积分中心-积分规则
//    public final int BUSINESS_JIAOCHEN_PC_54 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_54"; // 页面-特惠商城-商城订单
//    public final int BUSINESS_JIAOCHEN_PC_55 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_55"; // 页面-特惠商城-商城套餐
//    public final int BUSINESS_JIAOCHEN_PC_56 =  ; public  final String name = "BUSINESS_JIAOCHEN_PC_56"; // 页面-特惠商城-分销员管理


    //全部页面权限
//    public final int[] allauth = {118, 119, 120, 136, 142, 137, 143, 145, 147, 138, 144, 146, 148, 104, 105, 121, 122, 123, 106,
//            124, 125, 126, 127, 107, 108, 109, 110, 140, 111, 128, 129, 130, 131, 112, 132, 133, 141, 113, 114, 115, 116, 117};
//    public final JSONArray allauth_id = (JSONArray) JSONArray.toJSON(allauth);
//
//    public final String [] allauth_name = {name118, name119, name120, name136, name142, name137, name143, name145,
//            name147, name138, name144, name146, name148, name104, name105, name121, name122,
//            name123, name106, name124, name125, name126, name127, name107, name108, name109,
//            name110, name140, name111, name128, name129, name130, name131, name112, name132,
//            name133, name141, name113, name114, name115, name116, name117};
//    public final List<String> allauth_list = Arrays.asList(allauth_name);


    //导入记录权限所有页面；数据权限=全部；主体类型权限=品牌；无功能权限
    public final int[] daoruauth = {118, 119, 120, 136, 146};
    public final JSONArray daoruauth_id = (JSONArray) JSONArray.toJSON(daoruauth);

    public final String[] daoruauth_name = {name118, name119, name120, name136, name146};
    public final List<String> daoruauth_list = Arrays.asList(daoruauth_name);


    //接待管理所有页面；数据权限=个人；主体类型权限=门店；全部功能权限
    public final int[] jiedaiauth = {104, 142, 148, 137, 143, 145, 147};
    public final JSONArray jiedaiauth_id = (JSONArray) JSONArray.toJSON(jiedaiauth);

    public final String[] jiedaiauth_name = {name104, name142, name148, name137, name143, name145, name147};
    public final List<String> jiedaiauth_list = Arrays.asList(jiedaiauth_name);


    //客户管理+销售客户tab+售后客户tab；数据权限=全部；主体类型权限=集团；无功能权限
    public final int[] kehu12auth = {105, 121, 122, 136, 138};
    public final JSONArray kehu12auth_id = (JSONArray) JSONArray.toJSON(kehu12auth);

    public final String[] kehu12auth_name = {name105, name121, name122, name136, name138};
    public final List<String> kehu12auth_list = Arrays.asList(kehu12auth_name);

    //客户管理所有页面；数据权限=全部；主体类型权限=集团；无功能权限
    public final int[] kehu123auth = {105, 121, 122, 123, 136, 138};
    public final JSONArray kehu123auth_id = (JSONArray) JSONArray.toJSON(kehu123auth);

    public final String[] kehu123auth_name = {name105, name121, name122, name123, name136, name138};
    public final List<String> kehu123auth_list = Arrays.asList(kehu123auth_name);

    //预约管理+预约看板tab+预约记录tab；数据权限=全部；主体类型权限=门店；功能权限=售后接待+预约保养分配+预约应答人
    public final int[] yuyue12auth = {106, 124, 125, 148, 137, 143, 145};
    public final JSONArray yuyue12auth_id = (JSONArray) JSONArray.toJSON(yuyue12auth);

    public final String[] yuyue12auth_name = {name106, name124, name125, name148, name137, name143, name145};
    public final List<String> yuyue12auth_list = Arrays.asList(yuyue12auth_name);

    //门店管理+品牌管理+品牌删除；数据权限=全部；主体类型权限=集团；无功能权限
    public final int[] xitong123auth = {107, 108, 140, 136, 138};
    public final JSONArray xitong123auth_id = (JSONArray) JSONArray.toJSON(xitong123auth);

    public final String[] xitong123auth_name = {name107, name108, name140, name136, name138};
    public final List<String> xitong123auth_list = Arrays.asList(xitong123auth_name);

    //角色管理+员工管理；数据权限=全部；主体类型权限=区域；无功能权限
    public final int[] xitong45auth = {109, 110, 136, 144};
    public final JSONArray xitong45auth_id = (JSONArray) JSONArray.toJSON(xitong45auth);

    public final String[] xitong45auth_name = {name109, name110, name136, name144};
    public final List<String> xitong45auth_list = Arrays.asList(xitong45auth_name);

//    //卡券管理+核销人员tab+核销记录tab；数据权限=全部；主体类型权限=门店；无功能权限
//    public final int[] kaquan45auth = {111,130,131,136,148};
//    public final JSONArray  kaquan45auth_id = (JSONArray) JSONArray.toJSON(kaquan45auth);
//
//    public final String []  kaquan45auth_name = {name111, name130, name131, name136,name148};
//    public final List<String>  kaquan45auth_list = Arrays.asList( kaquan45auth_name);

//    //卡券管理+卡券表单tab+发卡记录tab+卡券申请页面；数据权限=全部；主体类型权限=品牌；无功能权限
//    public final int[] kaquan123auth = {111, 128,129,117,136,146};
//    public final JSONArray  kaquan123auth_id = (JSONArray) JSONArray.toJSON(kaquan123auth);

//    public final String []  kaquan123auth_name = {name111, name128, name129, name117,name136,name146};
//    public final List<String> kaquan123auth_list = Arrays.asList( kaquan123auth_name);

    //套餐管理+套餐表单tab+套餐购买记录tab+无确认支付按钮；数据权限=全部；主体类型权限=门店；无功能权限
    public final int[] taocannoauth = {112, 132, 133, 136, 148};
    public final JSONArray taocannoauth_id = (JSONArray) JSONArray.toJSON(taocannoauth);

    public final String[] taocannoauth_name = {name112, name132, name133, name136, name148};
    public final List<String> taocannoauth_list = Arrays.asList(taocannoauth_name);

    //套餐管理+套餐表单tab+套餐购买记录tab+有确认支付按钮；数据权限=全部；主体类型权限=门店；无功能权限
    public final int[] taocanauth = {112, 132, 133, 136, 148, 141};
    public final JSONArray taocanauth_id = (JSONArray) JSONArray.toJSON(taocanauth);

    public final String[] taocanauth_name = {name112, name132, name133, name136, name148, name141};
    public final List<String> taocanauth_list = Arrays.asList(taocanauth_name);


    //内容运营+消息管理；数据权限=全部；主体类型权限=集团；无功能权限
    public final int[] nxauth = {113, 114, 115, 116};
    public final JSONArray nxauth_id = (JSONArray) JSONArray.toJSON(nxauth);

//    public final String [] nxauth_name = {name113, name114, name115, name116};
//    public final List<String> nxauth_list = Arrays.asList(nxauth_name);


    //接待管理所有页面；数据权限=个人；主体类型权限=门店；功能权限无接待
    public final int[] jiedai234auth = {104, 142, 148, 143, 145, 147};
    public final JSONArray jiedai234auth_id = (JSONArray) JSONArray.toJSON(jiedai234auth);

    public final String[] jiedai234auth_name = {name104, name142, name148, name143, name145, name147};
    public final List<String> jiedai234auth_list = Arrays.asList(jiedai234auth_name);

    //V2.0新增权限


    public static JSONObject getallauth() {
        JSONObject all = new JSONObject();
        all.put("123", "aaa");
        return all;
    }

    public static final JSONObject obj = getallauth();


    //V2.0
    public final Long first_category = 46L; //一级品类id
    public final String first_category_chin = "自动化一级品类别删"; //一级品类name

    public final Long second_category = 47L; //二级品类id
    public final String second_category_chin = "自动化二级品类别删"; //二级品类name

    public final Long third_category = 48L; //三级品类id
    public final String third_category_chin = "自动化三级品类别删"; //三级品类name

    public final Long goods_brand = 19L; //商品品牌
    //还没改
    public final Long goods_id = 1L; //商品id
    public final String goods_name = "1"; //商品名称

    public final Long specifications_id = 1L;//规格类型id
    public final String specifications_name = "";//规格类型
    public final long specifications_detail_id = 1L;//规格参数详情id
    public final String specifications_detail_name = "";//规格参数详情名称


    //新建一级品类
    public JSONObject newFirstCategory(String name) {
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        int code = jc.categoryCreate(false, name, "FIRST_CATEGORY", "", logo, null).getInteger("code");
        Long id = jc.categoryPage(1, 100, null, null, null, null).getJSONArray("list").getJSONObject(0).getLong("id");
        obj.put("code", code);
        obj.put("id", id);
        return obj;
    }

    //新建二级品类
    public JSONObject newSecondCategory(String name, String... firstid) {
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        int code = 0;
        if (firstid.length == 0) {
            code = jc.categoryCreate(false, name, "SECOND_CATEGORY", Long.toString(first_category), logo, null).getInteger("code");
        } else {
            code = jc.categoryCreate(false, name, "SECOND_CATEGORY", firstid[0], logo, null).getInteger("code");
        }
        Long id = jc.categoryPage(1, 10, null, null, null, null).getJSONArray("list").getJSONObject(0).getLong("id");
        obj.put("code", code);
        obj.put("id", id);
        return obj;
    }

    //新建三级品类
    public JSONObject newThirdCategory(String name, String... secid) {
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        int code = 0;
        if (secid.length == 0) {
            code = jc.categoryCreate(false, name, "THIRD_CATEGORY", Long.toString(second_category), logo, null).getInteger("code");
        } else {
            code = jc.categoryCreate(false, name, "THIRD_CATEGORY", secid[0], logo, null).getInteger("code");
        }
        Long id = jc.categoryPage(1, 10, null, null, null, null).getJSONArray("list").getJSONObject(0).getLong("id");
        obj.put("code", code);
        obj.put("id", id);
        return obj;
    }

    //新建商品品牌
    public JSONObject newGoodBrand(String name, String desc) {
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        JSONObject obj1 = jc.BrandCreat(false, null, name, desc, logo);

        obj.put("code", obj1.getInteger("code"));
        obj.put("id", obj1.getJSONObject("data").getLong("id"));
        return obj;
    }

    public JSONObject newGoodBrand(String... cs) {
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        JSONObject obj1 = new JSONObject();
        if (cs.length > 0) {
            obj1 = jc.BrandCreat(false, null, cs[0], cs[1], logo);
        } else {
            obj1 = jc.BrandCreat(false, null, "name" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "品牌desc", logo);
        }


        obj.put("code", obj1.getInteger("code"));
        obj.put("id", obj1.getJSONObject("data").getLong("id"));
        return obj;
    }

    public Long newSpecificition() {
        //新建规格
        String spename = "规格" + (int) ((Math.random() * 9 + 1) * 10000);
        return jc.specificationsCreate(spename, first_category, null, null, true).getLong("id");
    }

    public String getLogo() {
        String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
        return jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
    }

    public String getLogoUrl() {
        String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
        return jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_url");
    }

    public JSONObject newArtical() throws Exception {

        JSONArray pic_list1 = new JSONArray();
        pic_list1.add(getLogo());
        JSONArray pic_list2 = new JSONArray();
        pic_list2.add(getLogo());
        pic_list2.add(getLogo());
        pic_list2.add(getLogo());
        JSONObject obj = jc.addArticleNotChk("" + System.currentTimeMillis(), "ONE_BIG", pic_list1, "content", "RED_PAPER", "ARTICEL", null, null, null,
                null, null, null, null, null, null,
                null, null, null, null);
        int code = obj.getInteger("code");
        Long id = obj.getJSONObject("data").getLong("id");
        JSONObject obj1 = new JSONObject();
        obj1.put("code", code);
        obj1.put("id", id);
        return obj1;
    }


    //--------V3.0------------

    //专属销售顾问提交
    public JSONObject submitPreService() throws Exception {
        user.loginApplet(APPLET_USER_ONE);
        Long brandId = BrandID;
        Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(CarStyleID).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
        Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("shop_id");
        String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().invoke(visitor).getJSONArray("sales_list").getJSONObject(0).getString("sales_id");
        String customerName = "奶糖";
        String customerPhone = "13811110000";
        String content = "12345678901234567890";
        AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor);
        JSONObject obj1 = new JSONObject();
        obj1.put("customerName", customerName);
        obj1.put("customerPhone", customerPhone);
        obj1.put("content", content);
        obj1.put("shopId", shopId);
        obj1.put("shopName", shopIdForName(shopId));
        obj1.put("brandId", brandIdForName(brandId));
        return obj1;
    }

    //专属售后顾问提交
    public JSONObject submitAfterService() throws Exception {
        user.loginApplet(APPLET_USER_ONE);
        Long brandId = BrandID;
        Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(CarStyleID).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
        Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("shop_id");

        String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().invoke(visitor).getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");

        String customerName = "奶糖";
        String customerPhone = "13811110000";
        String content = "12345678901234567890";
        AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor);

        JSONObject obj1 = new JSONObject();
        obj1.put("customerName", customerName);
        obj1.put("customerPhone", customerPhone);
        obj1.put("content", content);
        obj1.put("shopId", shopId);
        obj1.put("shopName", shopIdForName(shopId));
        obj1.put("brandId", brandIdForName(brandId));
        return obj1;
    }

    //在线专家咨询提交
    public JSONObject submitonlineExpert() throws Exception {
        user.loginApplet(APPLET_USER_ONE);
        Long brandId = BrandID;
        Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(CarStyleID).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
//        String str = "[39.981536865234375,116.30351257324219]";
//        Long shopId  = AppletCommonShopListScene.builder().carModelId(modelId).coordinate(JSONArray.parseArray(str)).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("shop_id");

        Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("shop_id");

        String customerName = "奶糖";
        String customerPhone = "13811110000";
        String content = "12345678901234567890";
        AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                .brandId(brandId).modelId(modelId).shopId(shopId).build().invoke(visitor);
        JSONObject obj1 = new JSONObject();
        obj1.put("customerName", customerName);
        obj1.put("customerPhone", customerPhone);
        obj1.put("content", content);
        obj1.put("brandId", brandId);
        obj1.put("modelId", modelId);
        obj1.put("shopId", shopId);
        return obj1;
    }

    public final String getString(int n) {
        String a = "";
        for (int i = 0; i < n; i++) {
            a = a + "q";
        }
        return a;
    }

    //获取小程序消息列表数量
    public int getAppletmessNum() {
        user.loginApplet(APPLET_USER_ONE);
        int num = AppletMessageListScene.builder().size(10).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getInteger("id");
        return num;
    }

    //门店id对应的门店名字
    public String shopIdForName(Long id) {
        user.loginPc(ALL_AUTHORITY);
        JSONArray array = PageScene.builder().page(1).size(100).build().invoke(visitor).getJSONArray("list");
        String name = "";
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Long searchid = obj.getLong("id");
            if (searchid.longValue() == id.longValue()) {
                name = obj.getString("simple_name");
            }
        }
        return name;
    }

    //品牌id对应的品牌名字
    public String brandIdForName(Long id) {
        user.loginPc(ALL_AUTHORITY);
        JSONArray array = BrandPageScene.builder().page(1).size(100).build().invoke(visitor).getJSONArray("list");
        String name = "";
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Long searchid = obj.getLong("id");
            if (searchid.longValue() == id.longValue()) {
                name = obj.getString("name");
            }
        }
        return name;
    }


    public void newBuyCarRec() {
        Long shop_id = oneshopid;
        Long car_style_id = jc.styleList(shop_id).getJSONArray("list").getJSONObject(0).getLong("style_id");
        Long car_model_id = jc.modelList(car_style_id).getJSONArray("list").getJSONObject(0).getLong("model_id");
        String salesId = jc.saleList(shop_id, "PRE").getJSONArray("list").getJSONObject(0).getString("sales_id");

        String name = "吕吕";
        String phone = "13436941018";
        String type = "PERSON";
        String sex = "0";

        jc.createCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(0), "ASDFUGGDSF99" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), true);

    }

    public String getVoucherId() {
        JSONObject obj = VoucherFormVoucherPageScene.builder().voucherStatus("WORKING").page(1).size(10).build().invoke(visitor).getJSONArray("list").getJSONObject(0);
        String id = obj.getString("id");
        int allow = obj.getInteger("allow_use_inventory");
        if (allow > 1) {
            return id;
        } else {
            //增发
            AddVoucherScene.builder().addNumber(100).id(Long.parseLong(id)).build().invoke(visitor);
            //审核通过
            Long applyid = ApplyPageScene.builder().page(1).size(10).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
            ArrayList list = new ArrayList();
            list.add(applyid);
            ApplyBatchApprovalScene.builder().ids(list).status(1).build().invoke(visitor);
            return id;

        }
    }

    //配置评价
    public JSONObject setevaluate(Integer type, String messageName) {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Integer points;
        if (day % 2 == 1) {
            points = 3;
        } else {
            points = 2;
        }
        //修改配置，评价，积分数，卡券数
        IScene evaluateConfig = EvaluateConfigSubmitScene.builder().evaluateReward(true)
                .defaultFavourableCycle(3)
                .isSendPoints(true).isSendVoucher(true).points(points)
                .type(type).vouchersId(getVoucherId()).build();
        jc.invokeApi(evaluateConfig);

        JSONObject obj = new JSONObject();
        obj.put("points", points);
        return obj;

    }

    public int getVoucherTotal(String phone) {
        int total = VoucherListScene.builder().transferPhone(phone).build().invoke(visitor).getJSONArray("list").size();
        return total;
    }

    public Long getMessDetailId() {
        Long listid = AppletMessageListScene.builder().build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
        Long detailid = AppletMessageDetailScene.builder().id(listid).build().invoke(visitor).getJSONObject("evaluate_info").getLong("id");
        return detailid;
    }


    public String[] salereception(String phone) {
        //注册过的手机号接待
        IScene appAdmitScene = AppAdmitScene.builder().phone(phone).build();
        JSONObject data = jc.invokeApi(appAdmitScene);
        Long customerId = data.getLong("customer_id");
        //开始接待
        IScene appstartReception = AppStartReceptionScene.builder()
                .customerId(customerId)
                .customerPhone(phone)
                .build();
        String[] receptionId = new String[2];
        receptionId[0] = jc.invokeApi(appstartReception).getString("id");  //接待ID
        return receptionId;
    }


}
