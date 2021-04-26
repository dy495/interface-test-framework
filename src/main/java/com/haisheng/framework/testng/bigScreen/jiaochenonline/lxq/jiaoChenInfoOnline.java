package com.haisheng.framework.testng.bigScreen.jiaochenonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.model.AppletModeListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;

public class jiaoChenInfoOnline {

    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_LXQ_ONLINE;
    public UserUtil user = new UserUtil(visitor);

    DateTimeUtil dt = new DateTimeUtil();
    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
    public final String logo = "general_temp/fd19d80a-bbff-45dc-8d02-36548ad2c43e";//120*120 品牌logo
    public  String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
    public final String stringone = "a";//字符串长度1
    public final String stringten = "a2！啊A"+Integer.toString((int)(Math.random()*100000));//字符串长度10
    public final String stringsix = "A"+ Integer.toString((int)(Math.random()*100000));//随机字符串长度6
    public final String stringfifty = "自动化创建--ZDHZDH"+Integer.toString((int)(Math.random()*10))+"1234567890ABCDeFGHIJ啊啊啊～！@#¥%，：67890";//随机字符串长度50
    public final String stringfifty1 = "ZDHZDH"+Integer.toString((int)(Math.random()*1000000))+"1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：678901";//随机字符串长度51
    public final String string20 = "ZdH啊！_*"+System.currentTimeMillis(); //20位字符串
    public final String stringlong = "自动化"+System.currentTimeMillis()+"a2～！啊A"+Integer.toString((int)(Math.random()*1000000))+"1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：67891234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：6789011234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：67890101"+System.currentTimeMillis();
    public final String  district_code= "222402";
    public final String phone = "1380110"+Integer.toString((int)(Math.random()*10000));//手机号
    public final String donephone = "17611474518";//成交客户手机号
    public final Long oneshopid = 20032L;

    //线上
    public final long BrandIDOnline = 65L;//自动化用的品牌id
    public final long CarStyleIDOnline = 1365L;//自动化用的品牌车系id

    //创建品牌，返回品牌id
    public final long getBrandID(int n){
        String name = ""+Integer.toString((int)(Math.random()*10000));
        Long id = jc.addBrand(name,getLogo()).getLong("id");
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



    //V 2.0
    public final Long  first_category= 14L; //一级品类id
    public final String  first_category_chin= "自动化一级品类别删"; //一级品类name

    public final Long  second_category= 15L; //二级品类id
    public final String  second_category_chin= "自动化二级品类别删"; //二级品类name

    public final Long  third_category= 16L; //三级品类id
    public final String  third_category_chin= "自动化三级品类别删"; //三级品类name

    public final Long  goods_brand= 6L; //商品品牌



    //新建一级品类
    public JSONObject newFirstCategory(String name){
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        int code = jc.categoryCreate(false,name,"FIRST_CATEGORY","",logo,null).getInteger("code");
        Long id = jc.categoryPage(1,100,null,null,null,null).getJSONArray("list").getJSONObject(0).getLong("id");
        obj.put("code",code);
        obj.put("id",id);
        return obj;
    }

    //新建二级品类
    public JSONObject newSecondCategory(String name,String ... firstid){
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        int code = 0 ;
        if (firstid.length==0){
            code = jc.categoryCreate(false,name,"SECOND_CATEGORY",Long.toString(first_category),logo,null).getInteger("code");
        }
        else {
            code = jc.categoryCreate(false,name,"SECOND_CATEGORY",firstid[0],logo,null).getInteger("code");
        }
        Long id =jc.categoryPage(1,10,null,null,null,null).getJSONArray("list").getJSONObject(0).getLong("id");
        obj.put("code",code);
        obj.put("id",id);
        return obj;
    }

    //新建三级品类
    public JSONObject newThirdCategory(String name,String ... secid){
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        int code = 0 ;
        if (secid.length==0){
            code = jc.categoryCreate(false,name,"THIRD_CATEGORY",Long.toString(second_category),logo,null).getInteger("code");
        }
        else {
            code = jc.categoryCreate(false,name,"THIRD_CATEGORY",secid[0],logo,null).getInteger("code");
        }
        Long id =jc.categoryPage(1,10,null,null,null,null).getJSONArray("list").getJSONObject(0).getLong("id");
        obj.put("code",code);
        obj.put("id",id);
        return obj;
    }

    //新建商品品牌
    public JSONObject newGoodBrand(String name,String desc){
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        JSONObject obj1 = jc.BrandCreat(false,null,name,desc,logo);

        obj.put("code",obj1.getInteger("code"));
        obj.put("id",obj1.getJSONObject("data").getLong("id"));
        return obj;
    }

    public JSONObject newGoodBrand(String ... cs){
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        JSONObject obj1 = new JSONObject();
        if (cs.length>0){
            obj1 = jc.BrandCreat(false,null,cs[0],cs[1],logo);
        }
        else {
            obj1 = jc.BrandCreat(false,null,"name"+Integer.toString((int)((Math.random()*9+1)*1000)),"品牌desc",logo);
        }


        obj.put("code",obj1.getInteger("code"));
        obj.put("id",obj1.getJSONObject("data").getLong("id"));
        return obj;
    }

    public Long newSpecificition(){
        //新建规格
        String spename = "规格"+Integer.toString((int)((Math.random()*9+1)*10000));
        Long speId = jc.specificationsCreate(spename,first_category,null,null,true).getLong("id");
        return speId;
    }

    public String getLogo(){
        String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
        String base64 = new ImageUtil().getImageBinary(filePath);
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        return logo;
    }

    public JSONObject newArtical() throws Exception {

        JSONArray pic_list1 =new JSONArray();
        pic_list1.add(getLogo());
        JSONArray pic_list2 =new JSONArray();
        pic_list2.add(getLogo());
        pic_list2.add(getLogo());
        pic_list2.add(getLogo());
        JSONObject obj = jc.addArticleNotChk(""+System.currentTimeMillis(),"ONE_BIG",pic_list1,"content","RED_PAPER","ARTICEL",null,null,null,
                null,null,null,null,null,null,
                null,null,null,null);
        int code = obj.getInteger("code");
        Long id = obj.getJSONObject("data").getLong("id");
        JSONObject obj1 = new JSONObject();
        obj1.put("code",code);
        obj1.put("id",id);
        return obj1;
    }


    //--------V3.0------------

    public final String ONLINE_EXPERTS = "ONLINE_EXPERTS"; //在线专家
    public final String SALES = "SALES"; //专属销售
    public final String AFTER_SALES = "AFTER_SALES"; //专属售后
    public final String sitphone1 = "010-8159"+Integer.toString((int)((Math.random()*9+1)*1000)); ;//座机号
    public final String string200 = "自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AA";


    //专属销售顾问提交
    public JSONObject submitPreService() throws Exception {
        user.loginApplet(APPLET_USER_ONE);
        Long brandId = BrandIDOnline;
        Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(CarStyleIDOnline).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
        Long shopId  = AppletCommonShopListScene.builder().carModelId(modelId).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("shop_id");
        String salesId  = AppletConsultDedicatedServiceSalesListScene.builder().build().invoke(visitor).getJSONArray("sales_list").getJSONObject(0).getString("sales_id");
        String customerName="奶糖";
        String customerPhone="13811110000";
        String content="12345678901234567890";
        AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor);
        JSONObject obj1 = new JSONObject();
        obj1.put("customerName",customerName);
        obj1.put("customerPhone",customerPhone);
        obj1.put("content",content);
        return obj1;
    }

    //专属售后顾问提交
    public JSONObject submitAfterService() throws Exception {
        user.loginApplet(APPLET_USER_ONE);
        Long brandId = BrandIDOnline;
        Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(CarStyleIDOnline).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
        Long shopId  = AppletCommonShopListScene.builder().carModelId(modelId).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("shop_id");

        String salesId  = AppletConsultDedicatedServiceSalesListScene.builder().build().invoke(visitor).getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");

        String customerName="奶糖";
        String customerPhone="13811110000";
        String content="12345678901234567890";
        AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                .salesId(salesId).modelId(modelId).shopId(shopId).build().invoke(visitor);
        JSONObject obj1 = new JSONObject();
        obj1.put("customerName",customerName);
        obj1.put("customerPhone",customerPhone);
        obj1.put("content",content);
        return obj1;
    }

    //在线专家咨询提交
    public JSONObject submitonlineExpert() throws Exception {
        user.loginApplet(APPLET_USER_ONE);
        Long brandId = BrandIDOnline;
        Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(CarStyleIDOnline).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
//        String str = "[39.981536865234375,116.30351257324219]";
//        Long shopId  = AppletCommonShopListScene.builder().carModelId(modelId).coordinate(JSONArray.parseArray(str)).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("shop_id");

        Long shopId  = AppletCommonShopListScene.builder().carModelId(modelId).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("shop_id");

        String customerName="奶糖";
        String customerPhone="13811110000";
        String content="12345678901234567890";
        AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                .brandId(brandId).modelId(modelId).shopId(shopId).build().invoke(visitor);
        JSONObject obj1 = new JSONObject();
        obj1.put("customerName",customerName);
        obj1.put("customerPhone",customerPhone);
        obj1.put("content",content);
        obj1.put("brandId",brandId);
        obj1.put("modelId",modelId);
        obj1.put("shopId",shopId);
        return obj1;
    }

    public final String getString(int n){
        String a = "";
        for (int i = 0 ; i < n; i++){
            a = a + "q" ;
        }
        return a;
    }

    //获取小程序消息列表数量
    public int getAppletmessNum(){
        user.loginApplet(APPLET_USER_ONE);
        int num = AppletMessageListScene.builder().size(10).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getInteger("id");
        return num;
    }

    public String getLogoUrl(){
        String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
        String base64 = new ImageUtil().getImageBinary(filePath);
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_url");
        return logo;
    }




}
