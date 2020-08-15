package com.haisheng.framework.testng.bigScreen.crm.commonDs;

import com.haisheng.framework.util.FileUtil;

public class CustomerInfo {

    //app登陆密码
    public final String pwd = "e10adc3949ba59abbe56e057f20f883e";//123456
    public final String lxqgw = "lxqgw";//销售顾问
    public final String lxqby = "lxqby";//保养顾问姓名
    public final String lxqwx = "lxqwx";//维修顾问姓名
    public final String xszj = "xszj";//销售总监
    public final String qt = "qt";//前台
    public final String baoshijie = "baoshijie";//超级管理员


    //交车照片
    public final FileUtil fileUtil = new FileUtil();
    public final String picurl = fileUtil.getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/dailyImages/2019-10-22_1.jpg");
    public final String jpgPath = "src/main/java/com/haisheng/framework/testng/bigScreen/dailyImages/2019-10-22_1.jpg";

}
