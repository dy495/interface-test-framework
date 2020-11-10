package com.haisheng.framework.testng.bigScreen.crm.xmf.interfaceDemo;

import com.haisheng.framework.testng.bigScreen.crm.commonDs.PublicParm;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;


public class createArticle {
    public static DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp=new PublicParm();
    FileUtil file = new FileUtil();
    public  String positions=pp.positions;
    public  String valid_start=dt.getHistoryDate(0);
    public  String valid_end=dt.getHistoryDate(4);
    public  String[] customer_types={"PRE_SALES", "AFTER_SALES"};
    public  int[] car_styles={};
    public  int[] customer_level={};
    public  String[] customer_property={};
    public  String article_title ="app任务报名品牌上新，优惠多多，限时4天---" + dt.getHistoryDate(0);
    public  Boolean is_pic_content=false;
    public  String article_bg_pic=file.texFile(pp.filePath);;
    public  String article_content= "品牌上新，优惠多多，限时4天,活动内容";
    public  String article_remarks="品牌上新，优惠多多，限时4天,备注";
    public  Boolean is_online_activity=true;
    public  String reception_name=pp.reception_name;;
    public  String reception_phone=pp.reception_phone;;
    public  String customer_max= "50";
    public  String simulation_count="8";
    public  String activity_start=dt.getHistoryDate(1);
    public  String activity_end=dt.getHistoryDate(4);
    public  String role_id="13";
    public  String task_customer_num="5";
    public  Boolean is_create_poster=true;
    public  String Empty;
    public  Boolean checkCode=true;

}
