package com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDsOnline;

import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;

public class CustomerInfoOnline {
    DateTimeUtil dt = new DateTimeUtil();
    //app登陆密码

    public final String pwd11 = "a806f5026dabadc5cff19211d9f4afa2";//11密码 //ys123456
    public final String pwd = "a806f5026dabadc5cff19211d9f4afa2";//统一密码ys123456
    public final String demoPassword = "f2064e9d2477a6bc75c132615fe3294c";//密码全部一致
    //账号
    public final String gly = "demo";//超级管理员
    public final String xsgw = ""; //销售顾问
    public final String bygw = "baoyang"; //保养顾问
    public final String wxgw = "66"; //维修顾问
    public final String zj = "zj"; //总监
    public final String fwzj = "fwzj";//服务总监
    public final String zjl = "zjl"; //总经理
    public final String qt = "qt"; //前台
    public final String dcc = "dcc"; //dcc销售


    public final String lxqsale = "11";
    public final String phone11 = "13436941018";
    public final String saleid11 = "uid_c01f9419"; //11的id
    public final String saleid22 = "uid_da55f38d"; //22的id
    public final String xszj = "zj";//销售总监


    //交车照片
    public final FileUtil fileUtil = new FileUtil();
    public final String picurl = fileUtil.getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/dailyImages/2019-10-22_1.jpg");
    public final String jpgPath = "src/main/java/com/haisheng/framework/testng/bigScreen/dailyImages/2019-10-22_1.jpg";


}
