package com.haisheng.framework.testng.bigScreen.crmOnline.commonDsOnline;

import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;

public class CustomerInfoOnline {
    DateTimeUtil dt = new DateTimeUtil();
    //app登陆密码
    public final String pwd123456 = "e10adc3949ba59abbe56e057f20f883e";//123456
    public final String pwdqt = "";//前台密码 //qt
    public final String pwdzj = "b1d8fcdf6d0db7011c71fc30e7aef4a4";//总监密码 //zj
    public final String pwdzjl = "";//总经理密码 //zjl
    public final String pwd11 = "6512bd43d9caa6e02c990b0a82652dca";//11密码 //11
    public final String pwd = "a806f5026dabadc5cff19211d9f4afa2";//ys123456
    //账号
    public final String gly = "";//超级管理员
    public final String xsgw = ""; //销售顾问
    public final String bygw = ""; //保养顾问
    public final String wxgw = ""; //维修顾问
    public final String zj = "zj"; //总监
    public final String zjl = "zjl"; //总经理
    public final String qt = "qt"; //前台
    public final Long lxqid = 838L; //lxq自动化要用的客户 别删
    public final String lxqname = "刘（自动化-别动）";
    public final String lxqphone = "15567898766";
    public final Long lxqlevel = 15L;
    public final String lxqsale = "11";
    public final String phone11 = "13436941018";
    public final String saleid11 = "uid_c01f9419"; //11的id
    public final String saleid22 = "uid_da55f38d"; //22的id



    //交车照片
    public final FileUtil fileUtil = new FileUtil();
    public final String picurl = fileUtil.getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/dailyImages/2019-10-22_1.jpg");
    public final String jpgPath = "src/main/java/com/haisheng/framework/testng/bigScreen/dailyImages/2019-10-22_1.jpg";


}
