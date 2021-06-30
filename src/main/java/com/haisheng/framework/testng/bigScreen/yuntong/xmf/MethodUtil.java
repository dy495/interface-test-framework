package com.haisheng.framework.testng.bigScreen.yuntong.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppAdmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppCustomerEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppStartReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.appStartReception;
import com.haisheng.framework.util.DateTimeUtil;
import org.apache.commons.lang.ArrayUtils;


public class MethodUtil extends BasicUtil {
    private final VisitorProxy visitor;
    ScenarioUtil jc=new ScenarioUtil();

    DateTimeUtil dt =new DateTimeUtil();
    public final PublicParm pp;


    public MethodUtil(VisitorProxy visitor,PublicParm pp) {
        super(visitor);
        this.visitor = visitor;
        this.pp=pp;
    }
    public String[] salereception(String phone) {
        //注册过的手机号接待
        IScene appAdmitScene = AppAdmitScene.builder().phone(phone).build();
        JSONObject data = visitor.invokeApi(appAdmitScene);
        Long customerId = data.getLong("customer_id");
        //开始接待
        IScene appstartReception = AppStartReceptionScene.builder()
                .customerId(customerId)
                .customerPhone(phone)
                .build();
        String[] receptionId = new String[2];
        receptionId[0] = visitor.invokeApi(appstartReception).getString("id");  //接待ID
        return receptionId;
    }

    public void finishReception(Long receptionId){
        IScene appcustomerEdit = AppCustomerEditScene.builder()
                .id(String.valueOf(receptionId))
                .shopId(pp.shopIdZ)
                .customerName("夏明凤")
                .estimatedBuyTime(dt.getHistoryDate(0))
                .build();
        visitor.invokeApi(appcustomerEdit, true);


        IScene appfinishReception = AppFinishReceptionScene.builder()
                .shopId(Long.valueOf(pp.shopIdZ))
                .id(receptionId).build();

        visitor.invokeApi(appfinishReception);
    }

    //pc接待管理总数
    public int pcReceptionPage() {

        int num = jc.receptionManage("", "1", "10", null, null).getInteger("total");
        return num;
    }

    //app[任务-预约数]
    public int appReceiptPage() {

        JSONObject data = jc.appointmentPage(null, 10);
        int total = data.getInteger("total");
        return total;
    }

    //app[任务-接待数]  AppPageScene
    public int appReceptionPage() {
        jc.appLogin(pp.jdgw, pp.jdgwpassword);
        JSONObject data = jc.appreceptionPage(null, 10);
        int total = data.getInteger("total");
        return total;
    }

    //app[任务-接待数]  3.0
    public int appSaleReceptionPage() {
        JSONObject data = jc.AppPageScene( 10,null);
        int total = data.getInteger("total");
        return total;
    }



    //app开始接待，并返回接待id
    public Long[] startReception(String carPlate) throws Exception {

        appStartReception sr = new appStartReception();
        JSONObject data = jc.appReceptionAdmit(carPlate).getJSONArray("customers").getJSONObject(0);
        Long result[] = new Long[2];
        sr.id = data.getString("customer_id");
        sr.plate_number = carPlate;
        sr.customer_name = data.getString("customer_name");
        sr.customer_phone = data.getString("customer_phone");
        sr.after_sales_type="MAINTAIN";
        //开始接待
        jc.StartReception(sr);
        //取接待列表id
        JSONObject dd = jc.appreceptionPage(null, 10).getJSONArray("list").getJSONObject(0);
        result[0] = dd.getLong("id");
        String plate_number = dd.getString("plate_number");
        result[1] = dd.getLong("shop_id");
        if (!carPlate.equals(plate_number)) {
            throw new Exception("获取接待id失败");
        }
        return result;
    }

    //pc开始接待，并返回接待id
    public Long pcstartReception(String carPlate) throws Exception {
        jc.pcLogin(pp.jdgw, pp.jdgwpassword);
        appStartReception sr = new appStartReception();
        JSONObject data = jc.pcManageReception(carPlate, true).getJSONArray("customers").getJSONObject(0);

        sr.id = data.getString("customer_id");
        sr.plate_number = carPlate;
        sr.customer_name = data.getString("customer_name");
        sr.customer_phone = data.getString("customer_phone");
        sr.after_sales_type="REPAIR";
//        sr.after_sales_type="MAINTAIN";

        //开始接待
        jc.pcStartReception(sr);
        //取接待列表id
        JSONObject dd = jc.receptionManage("", "1", "10", "", "").getJSONArray("list").getJSONObject(0);
        long receptionID = dd.getLong("id");
        String plate_number = dd.getString("plate_number");
        if (!carPlate.equals(plate_number)) {
            throw new Exception("获取接待id失败");
        }
        System.out.println("接待ID:"+receptionID);
        return receptionID;
    }

    //app今日任务数据
    public int[] appTask() {
        JSONObject data = jc.appTask();
        int sum[] = new int[6];
        //预约
        sum[0] = data.getInteger("surplus_appointment");   //分子
        sum[1] = data.getInteger("all_appointment");     //分母
        //接待
        sum[2] = data.getInteger("after_surplus_reception");  //分子
        sum[3] = data.getInteger("after_all_reception");      //分母

        sum[4] = data.getInteger("surplus_follow");  //分子
        sum[5] = data.getInteger("all_follow");      //分母
        return sum;
    }

    //根据门店名称或接待顾问名称，获取今日数据中待处理数据
    public int[] apptodayDate(String type, String name) {
        //今日数据
        JSONArray todaydate = jc.apptodayDate(type, null, 10).getJSONArray("list");
        String[] both = new String[4];

        for (int i = 0; i < todaydate.size(); i++) {
            JSONObject list_data = todaydate.getJSONObject(i);
            //待处理预约数和
            String name1 = list_data.getString("name");
            if (name1.equals(name)) {
                String pending_appointment = list_data.getString("pending_appointment");
                String[] appointment = pending_appointment.split("/");   //0待处理预约分子，1预约分母，2接待分子，3接待分母
                //接待
                String pending_reception = list_data.getString("pending_reception");
                String[] reception = pending_reception.split("/");
                both = (String[]) ArrayUtils.addAll(appointment, reception);
            }
        }
        int result[] = new int[both.length];
        for (int j = 0; j < both.length; j++) {
            result[j] = Integer.valueOf(both[j]);
        }
        return result;
    }













}
