package com.haisheng.framework.testng.bigScreen.jiaochen;

import com.haisheng.framework.util.DateTimeUtil;

public class jiaoChenInfo {
    DateTimeUtil dt = new DateTimeUtil();

    public final String logo = "";//120*120 品牌logo
    public final String stringone = "a";//字符串长度1
    public final String stringten = "a2～！啊67aaa";//字符串长度10
    public final String stringsix = "A"+ Integer.toString((int)(Math.random()*100000));//随机字符串长度6
    public final String stringfifty = "ZDHZDHZDHZDH1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：67890";//随机字符串长度50
    public final long BrandID = 1L;//自动化用的品牌id
    public final long CarStyleID = 1L;//自动化用的品牌车型系id



}
