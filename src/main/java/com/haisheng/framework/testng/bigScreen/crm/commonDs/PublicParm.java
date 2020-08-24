package com.haisheng.framework.testng.bigScreen.crm.commonDs;

import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;

public class PublicParm {
    DateTimeUtil dt = new DateTimeUtil();
    FileUtil file = new FileUtil();
    public String superManger = "baoshijie";    //超级管理员
    public String qiantai = "qt";
    public String zongjingli = "xx";    //总经理
    public String adminpassword = "e10adc3949ba59abbe56e057f20f883e";
    //售后 预约保养 维修 服务总监fuwuZongjian
    public String fuwuZongjian = "baoyang";    //服务总监
    public String baoyangGuwen = "baoyangr"; //保养顾问
    public String weixiuGuwen = "weixiu";     //维修顾问

    public Long mycarId = 357L;   //TODO:车id待定

    public String xiaoshouGuwen = "销售顾问xia";      //销售账号
    public String xiaoshouZongjian = "xszj";      //销售账号
    //预约使用参数
    public String customer_name = "@@@2";
    public String customer_phone_number = "15037286013";
    public Integer car_type = 1;
    public String car_type_name = "Panamera";
    public String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/article_bg_pic";


    public String positions = "CAR_ACTIVITY";
    public String remark = "自动化---------创建----------H级客户";

}
