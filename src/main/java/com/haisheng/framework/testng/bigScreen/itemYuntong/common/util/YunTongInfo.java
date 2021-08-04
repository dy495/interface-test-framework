package com.haisheng.framework.testng.bigScreen.itemYuntong.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.CarFileUploadScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionCreateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand.BrandAddScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand.CarStyleAddScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand.CarStylePageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.FinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesRecpEvaluateOpt;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class YunTongInfo {
    EnumTestProduct PRODUCE = EnumTestProduct.YT_DAILY_CAR;
    EnumAccount ALL_AUTHORITY = EnumAccount.YT_ALL_DAILY;
    VisitorProxy visitor = new VisitorProxy(PRODUCE);
    DateTimeUtil dt = new DateTimeUtil();
//    public final String donephone = "";//成交客户手机号
    public final String phone = "1380110"+Integer.toString((int)((Math.random()*9+1)*1000));//手机号
    public final Long oneshopid = 56721L; //自动化门店
    public final String stringone = "a";//字符串长度1
    public final String stringten = "a2！啊A"+Integer.toString((int)((Math.random()*9+1)*10000));//字符串长度10
    public final String stringsix = "A"+ Integer.toString((int)((Math.random()*9+1)*10000));//随机字符串长度6
    public final String stringfifty = "自动化创建--ZDHZDH"+Integer.toString((int)(Math.random()*10))+"1234567890ABCDeFGHIJ啊啊啊～！@#¥%，：67890";//随机字符串长度50
    public final String stringfifty1 = "ZDHZDH"+Integer.toString((int)((Math.random()*9+1)*100000))+"1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：678901";//随机字符串长度51
    public final String string20 = "ZdH啊！_*"+System.currentTimeMillis(); //20位字符串
    public final String string200 = "自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AA";


    public final Long BrandID = 1526L; //自动化用的品牌id
    public final long CarStyleID = 1584L;//自动化用的车系id

    public  long toMinute(String time){
        long mintue = 0L;
        mintue += Integer.parseInt(time.substring(0,2)) * 60;
        mintue += Integer.parseInt(time.substring(3,5));
        return mintue;
    }

    private static String getBase64(String imgFile) { //图片/语音 转base64
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

    public String getLogo() {
        String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";

        return CarFileUploadScene.builder().pic(new ImageUtil().getImageBinary(filePath)).permanentPicType(0).build().invoke(visitor).getString("pic_path");
    }

    //创建品牌，返回品牌id
    public final long getBrandID(int n) {
        String name = "" + (int) (Math.random() * Math.pow(10, n));
        Long id = BrandAddScene.builder().name(name).logoPath(getLogo()).build().invoke(visitor).getLong("id");;

        return id;
    }
    //创建某品牌下的车系，返回车系id
    public final long getCarStyleID(long id, int n) {
        //创建车系
        String manufacturer = "自动化" + System.currentTimeMillis();
        String name = "" + Integer.toString((int) (Math.random() * Math.pow(10,n)));
        String online_time = dt.getHistoryDate(0);
        CarStyleAddScene.builder().brandId(id).manufacturer(manufacturer).name(name).onlineTime(online_time).build().invoke(visitor);

        //获取车系id

        Long carStyleId = CarStylePageScene.builder().brandId(id).name(name).page(1).size(10).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");

        return carStyleId;
    }

    //创建展厅客户，返回客户id
    public final long newPotentialCstm(){
        Long shop_id = oneshopid;
        String name = "自动化"+dt.getHistoryDate(0)+Integer.toString((int)((Math.random()*9+1)*100));
        String phone = "138"+Integer.toString((int)((Math.random()*9+1)*1000))+"7777";
        String type = "CORPORATION";
        String sex = "0";
        Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(shop_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
        Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
        String salesId = PreSaleCustomerSalesListScene.builder().shopId(shop_id).type("PRE").build().invoke(visitor).getJSONArray("list").getJSONObject(0).getString("sales_id");
        PreSaleCustomerCreatePotentialCustomerScene.builder().customerName(name).customerPhone(phone).customerType(type).sex(sex).carStyleId(car_style_id).carModelId(car_model_id).shopId(shop_id).salesId(salesId).build().invoke(visitor);

        return PreSaleCustomerPageScene.builder().page(1).size(1).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("customer_id");

    }

    //接待之后评价"： 评价满分high/最低分low/中间分数mid
    public final JSONArray evaluateInfo(Long recId, String type){
        JSONArray evaluate_info_list = new JSONArray();
        JSONArray optarr = PreSalesRecpEvaluateOpt.builder().reception_id(recId).build().invoke(visitor).getJSONArray("list");
        for (int i = 0; i < optarr.size(); i++) {
            JSONObject asubmit = new JSONObject();
            int score = 0;
            Long id = optarr.getJSONObject(i).getLong("id");

            JSONArray aopt = optarr.getJSONObject(i).getJSONArray("answer_list");
            if (type.equals("high")){
                score = aopt.getJSONObject(0).getInteger("score");
            }
            if (type.equals("low")){
                score = aopt.getJSONObject(aopt.size()-1).getInteger("score");
            }
            if (type.equals("mid")){
                if (i % 2 == 0) {
                    score = aopt.getJSONObject(aopt.size()-1).getInteger("score");
                } else {
                    score = aopt.getJSONObject(0).getInteger("score");
                }
            }
            asubmit.put("id", id);
            asubmit.put("score", score);
            evaluate_info_list.add(asubmit);

        }
        return evaluate_info_list;
    }

    public final Long startrecption(Boolean done){
        //销售接待
        String phone = "1380110" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));//手机号
        String name = "小紫"+dt.getHistoryDate(0);
        Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(oneshopid).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
        Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
        AppPreSalesReceptionCreateScene.builder().customerName(name).customerPhone(phone).sexId("1").intentionCarModelId(Long.toString(car_model_id)).estimateBuyCarTime("2100-07-12").build().invoke(visitor);
        //获取接待id
        Long recId = PreSalesReceptionPageScene.builder().phone(phone).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
        if (done == true){
            //完成接待
            FinishReceptionScene.builder().id(recId).shopId(oneshopid).build().invoke(visitor);

        }
        else {}
        return recId;
    }






    /**
     * DataProvider
     */
    @DataProvider(name = "FILTER")
    public Object[] filter(){
        return new String[]{
                "1",
                "啊",
                "A"
        };
    }

    @DataProvider(name = "TIME")
    public Object[][] time(){
        return new String[][]{ // 开始时间 结束时间 提示语 是否正常

                {dt.getHistoryDate(-2),dt.getHistoryDate(-1),"开始时间<结束时间 && 结束时间<今天","true"},
                {dt.getHistoryDate(-34),dt.getHistoryDate(-1),"开始时间<结束时间 && 结束时间<今天","true"},
                {dt.getHistoryDate(-1),dt.getHistoryDate(-1),"开始时间=结束时间 && 结束时间<今天","true"},

                //前端限制 注释掉
//                {dt.getHistoryDate(-1),dt.getHistoryDate(0),"开始时间<结束时间 && 结束时间=今天","false"},
//                {dt.getHistoryDate(-1),dt.getHistoryDate(-2),"开始时间>结束时间 ","false"},


        };
    }

    @DataProvider(name = "TYPE")
    public Object[] type(){
        return new String[]{
                "high",
                "low",
                "mid",
        };
    }

    @DataProvider(name = "WORDNUM")
    public Object[] wordNum(){
        return new String[]{
                "123456789",
        };
    }



}
