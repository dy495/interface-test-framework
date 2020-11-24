package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.appStartReception;

public class JcFunction {
    ScenarioUtil jc=new ScenarioUtil();
    PublicParm pp=new PublicParm();

    //app开始接待，并返回接待id
    public Long startReception(String carPlate) throws Exception{
        appStartReception sr=new appStartReception();
        JSONObject data=jc.appReceptionAdmit(carPlate);

        sr.id=data.getString("id");
        sr.plate_number=data.getString("plate_number");
        sr.customer_name=data.getString("customer_name");
        sr.customer_phone=data.getString("customer_phone");
        //开始接待
        jc.StartReception(sr);
        //取接待列表id
        JSONObject dd=jc.appreceptionPage(null,10).getJSONArray("list").getJSONObject(0);
        long receptionID=dd.getLong("id");
        long plate_number=dd.getLong("plate_number");
        if(!carPlate.equals(plate_number)){
            throw new Exception("获取接待id失败");
        }
        return receptionID;
    }

}
