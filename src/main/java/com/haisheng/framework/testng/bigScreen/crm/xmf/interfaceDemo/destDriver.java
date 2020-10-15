package com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo;

import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicParm;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;

import java.text.ParseException;

public class destDriver {
    public static DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp=new PublicParm();
    FileUtil file = new FileUtil();
    public String receptionId;
    public String customer_id;
    public String customerName;
    public Integer customer_gender=0;
    public String phone;
    public Integer car_model;

    public String country="中国";
    public String city="苏州";
    public String address="西湖";
    public String email;
    public Integer activity=1;
    public String driverLicensePhoto1Url=file.texFile(pp.filePath);;
    public String sign_date=dt.getHistoryDate(0);
    public String sign_time=dt.getHHmm(0);
    public String call="MEN";
    public boolean is_fill=false;
    public String apply_time;
    public Long test_drive_car;
    public boolean checkCode=true;
    public destDriver() throws ParseException {
    }
}
