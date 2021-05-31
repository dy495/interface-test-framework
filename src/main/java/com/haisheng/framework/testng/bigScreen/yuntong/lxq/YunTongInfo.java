package com.haisheng.framework.testng.bigScreen.yuntong.lxq;

import com.haisheng.framework.util.DateTimeUtil;

public class YunTongInfo {
    DateTimeUtil dt = new DateTimeUtil();
    public final String donephone = "";//成交客户手机号
    public final String phone = "1380110"+Integer.toString((int)((Math.random()*9+1)*1000));//手机号
    public final Long oneshopid = 1L;
    public final String stringten = "a2！啊A"+Integer.toString((int)((Math.random()*9+1)*10000));//字符串长度10
    public final String stringsix = "A"+ Integer.toString((int)((Math.random()*9+1)*10000));//随机字符串长度6
    public final String stringfifty = "自动化创建--ZDHZDH"+Integer.toString((int)(Math.random()*10))+"1234567890ABCDeFGHIJ啊啊啊～！@#¥%，：67890";//随机字符串长度50
    public final String stringfifty1 = "ZDHZDH"+Integer.toString((int)((Math.random()*9+1)*100000))+"1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：678901";//随机字符串长度51
    public final String string20 = "ZdH啊！_*"+System.currentTimeMillis(); //20位字符串
    public final String string200 = "自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AA";


}
