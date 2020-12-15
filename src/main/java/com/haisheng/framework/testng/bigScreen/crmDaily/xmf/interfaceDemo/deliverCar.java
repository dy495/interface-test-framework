package com.haisheng.framework.testng.bigScreen.crmDaily.xmf.interfaceDemo;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.crmDaily.commonDs.PublicParm;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;

import java.util.Random;

public class deliverCar {
    public static DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp=new PublicParm();
    FileUtil file = new FileUtil();
    Random random=new Random();
    public String car_id;
    public String reception_id;
    public String customer_id;
    public String customer_name;
    public String deliver_car_time=dt.getHistoryDate(0);
    public String img_file=file.texFile(pp.filePath);
    public Boolean accept_show;
    public String sign_name_url=file.texFile(pp.filePath);
    public String vehicle_chassis_code="ASD123456" + (random.nextInt(89999999) + 10000000);;

    public JSONArray works=getWorks();
    public JSONArray likes=getLikes();
    public String call="先生";
    public String greeting="自动化-恭喜QA同学喜提车车一辆";

    public boolean checkCode = true;
    public String Empty;
    private JSONArray getLikes(){
        JSONArray aa=new JSONArray();
        aa.add("摄影");
        return aa;
    }

    private JSONArray getWorks(){
        JSONArray aa=new JSONArray();
        aa.add("自由");
        return aa;
    }
}
