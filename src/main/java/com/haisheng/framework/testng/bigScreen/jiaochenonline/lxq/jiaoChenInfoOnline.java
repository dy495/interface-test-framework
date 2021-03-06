package com.haisheng.framework.testng.bigScreen.jiaochenonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.model.AppletModeListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppAdmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppStartReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.brand.BrandPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.EvaluateConfigSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop.ShopPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyBatchApprovalScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.voucher.ApplyPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.AddVoucherScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherFormVoucherPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class jiaoChenInfoOnline {

    private static final EnumTestProduct PRODUCE = EnumTestProduct.JC_ONLINE_JD;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_LXQ_ONLINE;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.JC_ONLINE_YS;
    public SceneUtil user = new SceneUtil(visitor);

    DateTimeUtil dt = new DateTimeUtil();
    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
    public final String logo = "general_temp/fd19d80a-bbff-45dc-8d02-36548ad2c43e";//120*120 ??????logo
    public String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/??????.jpg";
    public final String stringone = "a";//???????????????1
    public final String stringten = "a2??????A" + Integer.toString((int) (Math.random() * 100000));//???????????????10
    public final String stringsix = "A" + (int) (Math.random() * 100000);//?????????????????????6
    public final String stringfifty = "???????????????--ZDHZDH" + Integer.toString((int) (Math.random() * 10)) + "1234567890ABCDeFGHIJ???????????????@#??%??????67890";//?????????????????????50
    public final String stringfifty1 = "ZDHZDH" + Integer.toString((int) (Math.random() * 1000000)) + "1234567890ABCDeFGHIJ?????????????????????@#??%??????678901";//?????????????????????51
    public final String string20 = "ZdH??????_*" + System.currentTimeMillis(); //20????????????
    public final String stringlong = "?????????" + System.currentTimeMillis() + "a2?????????A" + Integer.toString((int) (Math.random() * 1000000)) + "1234567890ABCDeFGHIJ?????????????????????@#??%??????67891234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????6789011234567890ABCDeFGHIJ?????????????????????@#??%??????67890101" + System.currentTimeMillis();
    public final String district_code = "222402";
    public final String phone = "1380110" + Integer.toString((int) (Math.random() * 10000));//?????????
    public final String donephone = "13436941018";//?????????????????????
    public final Long oneshopid = 20032L;

    //??????
    public final long BrandIDOnline = 65L;//?????????????????????id
    public final long CarStyleIDOnline = 1365L;//???????????????????????????id

    //???????????????????????????id
    public final long getBrandID(int n) {
        String name = "" + Integer.toString((int) (Math.random() * 10000));
        Long id = jc.addBrand(name, getLogo()).getLong("id");
        return id;
    }

    //??????????????????????????????????????????id
    public final long getCarStyleID(long id, int n) {
        //????????????
        String manufacturer = "?????????" + System.currentTimeMillis();
        String name = "?????????" + System.currentTimeMillis();
        String online_time = dt.getHistoryDate(0);
        jc.addCarStyle(id, manufacturer, name, online_time);
        //????????????id
        return jc.carStylePage(1, 1, id, name).getJSONArray("list").getJSONObject(0).getLong("id");
    }

    //????????????????????????????????????????????????id
    public final long getCarStyleID(long brand_id, long carStyle_id) {
        //????????????
        String name1 = "?????????" + System.currentTimeMillis();
        String year1 = dt.getHistoryDate(-100);
        String status1 = "ENABLE";
        jc.addCarModel(brand_id, carStyle_id, name1, year1, status1);
        //????????????id
        return jc.carModelPage(1, 1, brand_id, carStyle_id, name1, "", "").getJSONArray("list").getJSONObject(0).getLong("id");
    }


    //V 2.0
    public final Long first_category = 6251L; //????????????id
    public final String first_category_chin = "0813????????????"; //????????????name

    public final Long second_category = 6252L; //????????????id
    public final String second_category_chin = "0813????????????"; //????????????name

    public final Long third_category = 6253L; //????????????id
    public final String third_category_chin = "0813????????????"; //????????????name

    public final Long goods_brand = 6L; //????????????


    //??????????????????
    public JSONObject newFirstCategory(String name) {
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        int code = jc.categoryCreate(false, name, "FIRST_CATEGORY", "", logo, null).getInteger("code");
        Long id = jc.categoryPage(1, 100, null, null, null, null).getJSONArray("list").getJSONObject(0).getLong("id");
        obj.put("code", code);
        obj.put("id", id);
        return obj;
    }

    //??????????????????
    public JSONObject newSecondCategory(String name, String... firstid) {
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        int code;
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

    //??????????????????
    public JSONObject newThirdCategory(String name, String... secid) {
        JSONObject obj = new JSONObject();
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
        int code;
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

    //??????????????????
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
        JSONObject obj1;
        if (cs.length > 0) {
            obj1 = jc.BrandCreat(false, null, cs[0], cs[1], logo);
        } else {
            obj1 = jc.BrandCreat(false, null, "name" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "??????desc", logo);
        }
        obj.put("code", obj1.getInteger("code"));
        obj.put("id", obj1.getJSONObject("data").getLong("id"));
        return obj;
    }

    public Long newSpecificition() {
        //????????????
        String spename = "??????" + (int) ((Math.random() * 9 + 1) * 10000);
        return jc.specificationsCreate(spename, first_category, null, null, true).getLong("id");
    }

    public String getLogo() {
        String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/??????.jpg";
        return jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");
    }

    public JSONObject newArtical() throws Exception {

        JSONArray pic_list1 = new JSONArray();
        pic_list1.add(getLogo());
        JSONArray pic_list2 = new JSONArray();
        pic_list2.add(getLogo());
        pic_list2.add(getLogo());
        pic_list2.add(getLogo());
        JSONObject obj = jc.addArticleNotChk("" + System.currentTimeMillis(), "ONE_BIG", pic_list1, "content", "CAR_WELFARE", "ARTICEL", null, null, null,
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

    public final String ONLINE_EXPERTS = "ONLINE_EXPERTS"; //????????????
    public final String SALES = "SALES"; //????????????
    public final String AFTER_SALES = "AFTER_SALES"; //????????????
    public final String sitphone1 = "010-8159" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));
    ;//?????????
    public final String string200 = "???????????????????????????????????????????????????????????????AAAAAAA12345AAAAAA???sssssss!@#$%^&*???????????????????????????????????????????????????????????????AAAAAAA12345AAAAAA???sssssss!@#$%^&*???????????????????????????????????????????????????????????????AAAAAAA12345AAAAAA???sssssss!@#$%^&*???????????????????????????????????????????????????????????????AAAAAAA12345AA";


    //????????????????????????
    public JSONObject submitPreService() throws Exception {
        user.loginApplet(APPLET_USER_ONE);
        Long brandId = BrandIDOnline;
        Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(CarStyleIDOnline).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
        Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");
        String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("sales_list").getJSONObject(0).getString("sales_id");
        String customerName = "??????";
        String customerPhone = "13811110000";
        String content = "12345678901234567890";
        AppletConsultPreServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).execute();
        JSONObject obj1 = new JSONObject();
        obj1.put("customerName", customerName);
        obj1.put("customerPhone", customerPhone);
        obj1.put("content", content);
        obj1.put("shopId", shopId);
        obj1.put("shopName", shopIdForName(shopId));
        obj1.put("brandId", brandIdForName(brandId));
        return obj1;
    }

    //????????????????????????
    public JSONObject submitAfterService() throws Exception {
        user.loginApplet(APPLET_USER_ONE);
        Long brandId = BrandIDOnline;
        Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(CarStyleIDOnline).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
        Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");

        String salesId = AppletConsultDedicatedServiceSalesListScene.builder().build().visitor(visitor).execute().getJSONArray("after_sales_list").getJSONObject(0).getString("sales_id");

        String customerName = "??????";
        String customerPhone = "13811110000";
        String content = "12345678901234567890";
        AppletConsultAfterServiceSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                .salesId(salesId).modelId(modelId).shopId(shopId).build().visitor(visitor).execute();
        JSONObject obj1 = new JSONObject();
        obj1.put("customerName", customerName);
        obj1.put("customerPhone", customerPhone);
        obj1.put("content", content);
        obj1.put("shopId", shopId);
        obj1.put("shopName", shopIdForName(shopId));
        obj1.put("brandId", brandIdForName(brandId));
        return obj1;
    }

    //????????????????????????
    public JSONObject submitonlineExpert() throws Exception {
        user.loginApplet(APPLET_USER_ONE);
        Long brandId = BrandIDOnline;
        Long modelId = AppletModeListScene.builder().brandId(brandId).styleId(CarStyleIDOnline).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
//        String str = "[39.981536865234375,116.30351257324219]";
//        Long shopId  = AppletCommonShopListScene.builder().carModelId(modelId).coordinate(JSONArray.parseArray(str)).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("shop_id");

        Long shopId = AppletCommonShopListScene.builder().carModelId(modelId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("shop_id");

        String customerName = "??????";
        String customerPhone = "13811110000";
        String content = "12345678901234567890";
        AppletConsultOnlineExpertsSubmitScene.builder().customerName(customerName).customerPhone(customerPhone).content(content)
                .brandId(brandId).modelId(modelId).shopId(shopId).build().visitor(visitor).execute();
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

    //?????????????????????????????????
    public int getAppletmessNum() {
        user.loginApplet(APPLET_USER_ONE);
        int num = AppletMessageListScene.builder().size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getInteger("id");
        return num;
    }

    public String getLogoUrl() {
        String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/??????.jpg";
        String base64 = new ImageUtil().getImageBinary(filePath);
        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_url");
        return logo;
    }

    //??????id?????????????????????
    public String shopIdForName(Long id) {
        user.loginPc(ALL_AUTHORITY);
        JSONArray array = ShopPageScene.builder().page(1).size(100).build().visitor(visitor).execute().getJSONArray("list");
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

    //??????id?????????????????????
    public String brandIdForName(Long id) {
        user.loginPc(ALL_AUTHORITY);
        JSONArray array = BrandPageScene.builder().page(1).size(100).build().visitor(visitor).execute().getJSONArray("list");
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

        String name = "??????";
        String phone = "13436941018";
        String type = "PERSON";
        String sex = "0";

        jc.createCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(0), "ASDFUGGDSF99" + (int) ((Math.random() * 9 + 1) * 10000), true);

    }

    public String getVoucherId() {
        JSONObject obj = VoucherFormVoucherPageScene.builder().voucherStatus("WORKING").page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
        String id = obj.getString("id");
        int allow = obj.getInteger("allow_use_inventory");
        if (allow > 1) {
            return id;
        } else {
            //??????
            AddVoucherScene.builder().addNumber(100).id(Long.parseLong(id)).build().visitor(visitor).execute();
            //????????????
            Long applyid = ApplyPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            ArrayList list = new ArrayList();
            list.add(applyid);
            ApplyBatchApprovalScene.builder().ids(list).status(1).build().visitor(visitor).execute();
            return id;

        }
    }

    //????????????
    public JSONObject setevaluate(Integer type, String messageName) {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Integer points;
        if (day % 2 == 1) {
            points = 3;
        } else {
            points = 2;
        }
        //?????????????????????????????????????????????
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
        int total = VoucherListScene.builder().transferPhone(phone).build().visitor(visitor).execute().getJSONArray("list").size();
        return total;
    }

    public Long getMessDetailId() {
        Long listid = AppletMessageListScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
        Long detailid = AppletMessageDetailScene.builder().id(listid).build().visitor(visitor).execute().getJSONObject("evaluate_info").getLong("id");
        return detailid;
    }


    public String[] salereception(String phone) {
        //???????????????????????????
        IScene appAdmitScene = AppAdmitScene.builder().phone(phone).build();
        JSONObject data = jc.invokeApi(appAdmitScene);
        Long customerId = data.getLong("customer_id");
        //????????????
        IScene appstartReception = AppStartReceptionScene.builder()
                .customerId(customerId)
                .customerPhone(phone)
                .build();
        String[] receptionId = new String[2];
        receptionId[0] = jc.invokeApi(appstartReception).getString("id");  //??????ID
        return receptionId;
    }


}
