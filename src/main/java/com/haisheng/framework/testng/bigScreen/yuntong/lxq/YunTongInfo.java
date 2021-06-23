package com.haisheng.framework.testng.bigScreen.yuntong.lxq;

import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class YunTongInfo {
    DateTimeUtil dt = new DateTimeUtil();
//    public final String donephone = "";//成交客户手机号
    public final String phone = "1380110"+Integer.toString((int)((Math.random()*9+1)*1000));//手机号
    public final Long oneshopid = 56721L; //自动化门店
    public final String stringone = "a";//字符串长度1
    public final String stringten = "a2！啊A"+Integer.toString((int)((Math.random()*9+1)*10000));//字符串长度10
    public final String stringsix = "A"+ Integer.toString((int)((Math.random()*9+1)*10000));//随机字符串长度6
    public final String stringfifty = "自动化创建--ZDHZDH"+Integer.toString((int)(Math.random()*10))+"1234567890ABCDeFGHIJ啊啊啊～！@#¥%，：67890";//随机字符串长度50
    public final String stringfifty1 = "ZDHZDH"+Integer.toString((int)((Math.random()*9+1)*100000))+"1234567890ABCDeFGHIJ啊啊啊啊啊～！@#¥%，：678901";//随机字符串长度51
    public final String string20 = "ZdH啊！_*"+System.currentTimeMillis(); //20位字符串
    public final String string200 = "自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AAAAAA次sssssss!@#$%^&*自动化自动化自动化自动化自动化自动化自动化AAAAAAA12345AA";


    public  long toMinute(String time){
        long mintue = 0L;
        mintue += Integer.parseInt(time.substring(0,2)) * 60;
        mintue += Integer.parseInt(time.substring(3,5));
        return mintue;
    }

    private static String getBase64(String imgFile) { //图片/语音 转base64
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

    public String getLogo() {
        String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
        String bbase64 = new ImageUtil().getImageBinary(filePath);
//        String logo = jc.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path"); //要改
        String logo = ""; //要改
        return logo;
    }





    public static void main(String[] args) {

    }


    /**
     * DataProvider
     */
    @DataProvider(name = "FILTER")
    public Object[] filter(){
        return new String[]{
                "1",
                "A"
        };
    }

    @DataProvider(name = "TIME")
    public Object[][] time(){
        return new String[][]{ // 开始时间 结束时间 提示语 是否正常

                {dt.getHistoryDate(-2),dt.getHistoryDate(-1),"开始时间<结束时间 && 结束时间<今天","true"},
                {dt.getHistoryDate(-34),dt.getHistoryDate(-1),"开始时间<结束时间 && 结束时间<今天","true"},
                {dt.getHistoryDate(-1),dt.getHistoryDate(-1),"开始时间=结束时间 && 结束时间<今天","true"},

                {dt.getHistoryDate(-1),dt.getHistoryDate(0),"开始时间<结束时间 && 结束时间=今天","false"},
                {dt.getHistoryDate(-1),dt.getHistoryDate(-2),"开始时间>结束时间 ","false"},


        };
    }



}
