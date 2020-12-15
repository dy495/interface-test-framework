package com.haisheng.framework.testng.bigScreen.jiaochenDaily.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.gly.Variable.registerListVariable;
import com.haisheng.framework.testng.bigScreen.jiaochenDaily.xmf.intefer.appStartReception;
import org.apache.commons.lang.ArrayUtils;

import java.util.Random;

public class JcFunction {
    ScenarioUtil jc=new ScenarioUtil();
    PublicParm pp=new PublicParm();
    Random random=new Random();
    public String genPhoneNum() {
        String num = "177" + (random.nextInt(89999999) + 10000000);

        return num;
    }
    //app开始接待，并返回接待id
    public Long startReception(String carPlate) throws Exception{
        appStartReception sr=new appStartReception();
        JSONObject data=jc.appReceptionAdmit(carPlate).getJSONArray("customers").getJSONObject(0);

        sr.id=data.getString("customer_id");
        sr.plate_number=carPlate;
        sr.customer_name=data.getString("customer_name");
        sr.customer_phone=data.getString("customer_phone");
        //开始接待
        jc.StartReception(sr);
        //取接待列表id
        JSONObject dd=jc.appreceptionPage(null,10).getJSONArray("list").getJSONObject(0);
        long receptionID=dd.getLong("id");
        String plate_number=dd.getString("plate_number");
        if(!carPlate.equals(plate_number)){
            throw new Exception("获取接待id失败");
        }
        return receptionID;
    }

    //app今日任务数据
    public int [] appTask(){
        JSONObject data = jc.appTask();
        int sum []=new int[4];
        //预约
       sum[0] = data.getInteger("surplus_appointment");   //分子
        sum[1] = data.getInteger("all_appointment");     //分母
        //接待
        sum[2] = data.getInteger("surplus_reception");  //分子
        sum[3] = data.getInteger("all_reception");      //分母
        return sum;
    }
   //根据门店名称或接待顾问名称，获取今日数据中待处理数据
    public int [] apptodayDate(String type,String name){
        //今日数据
        JSONArray todaydate = jc.apptodayDate(type, null, 10).getJSONArray("list");
        String[] both =new String[4];

        for (int i = 0; i < todaydate.size(); i++) {
            JSONObject list_data = todaydate.getJSONObject(i);
            //待处理预约数和
            String name1 = list_data.getString("name");
            if(name1.equals(name)){
                String pending_appointment = list_data.getString("pending_appointment");
                String[] appointment = pending_appointment.split("/");   //0待处理预约分子，1预约分母，2接待分子，3接待分母
                //接待
                String pending_reception = list_data.getString("pending_reception");
                String[] reception = pending_reception.split("/");
                both = (String[]) ArrayUtils.addAll(appointment, reception);
            }
        }
        int result[]=new int[both.length];
        for(int j=0;j<both.length;j++){
            result[j]=Integer.valueOf(both[j]);
        }
        return result;
    }
    //增加车辆，返回车辆id
    public String appletAddCar(String plateNumber){
        jc.appletAddCar( plateNumber, pp.carModelId);
        String car_id="";
        JSONArray carData = jc.appletMyCar(pp.carStyleId).getJSONArray("list");
        for(int i=0;i<carData.size();i++){
            String plate_numberAfter = carData.getJSONObject(i).getString("plate_number");
            if(plate_numberAfter.equals(plateNumber)){
                car_id= carData.getJSONObject(i).getString("id");
                break;
            }
        }
        return car_id;
    }
    public int carListNumber(String carStyleId){
        JSONObject carData = jc.appletMyCar(carStyleId);
        JSONArray list = carData.getJSONArray("list");
        int count;
        if (list == null || list.size() == 0) {
            count = 0;
        } else {
            count = list.size();
        }
        return count;
    }

    //获取pc活动报名人数
    public int [] jsonActivityNUm(String id){   //活动id
        registerListVariable sv=new registerListVariable();
        int num[]=new int[4];

        JSONObject ll=jc.registerListFilterManage(sv);
        num[0]=ll.getInteger("total");
        JSONArray list=ll.getJSONArray("list");

        for(int i=0;i<list.size();i++){
            JSONObject temp=list.getJSONObject(i);
            String idt=temp.getString("id");
            if(idt.equals(id)){
                num[1]=temp.getInteger("total_quota");   //总数
                num[2]=temp.getInteger("register_num");   //已报名
                num[3]=temp.getInteger("passed_num");     //入选
                break;
            }
        }
        return num;
    }

    //applet文章详情
    public int [] appletActivityDetail(String id){   //活动id

        int num[]=new int[3];   //0

        JSONObject ll=jc.appletArticleDetile(id);
        num[0]=ll.getInteger("total_quota");    //全部名额
        num[1]=ll.getInteger("register_num");   //已报名名额
        JSONArray list=ll.getJSONArray("list");  //报名成功名单输
        num[2]=list.size();
        return num;
    }

    public int[] getPage(int total){
        int page[]=new int[2];
        if(total==0){
            page[0]=1;
            page[1]=0;
        }else if(total%10==0){
            page[0]=total/10;
            page[1]=10;
        }else{
            page[0]=total/10+1;
            page[1]=total%10-1;
        }
        System.out.println(page[0]+"index:"+page[1]);
        return page;
    }

    //获取预约时段id
    public Long getTimeId(Long shop_id,Long car_id,String data){
        JSONArray list=jc.appletmaintainTimeList(shop_id,car_id,data).getJSONArray("list");
        Long id=0L;
        for(int i=0;i<list.size();i++){
            String is_full=list.getJSONObject(i).getString("is_full");
            if(is_full.equals("false")){
                id=list.getJSONObject(i).getLong("id");
                break;
            }
        }
        return id;
    }

}
