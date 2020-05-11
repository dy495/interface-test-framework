
package com.haisheng.framework.testng.bigScreen;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.QADbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;
import java.util.List;


/**
 * @author : lvxueqing
 * @date :  2020/04/26  11:10
 */

public class MenjinTestHardwareDaily {


    Menjin menjin = new Menjin();
    //    ----------------------------------------------变量定义--------------------------------------------------------------------
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String failReason = "";

    private String response = "";

    private boolean FAIL = false;

    private Case aCase = new Case();

    CheckUtil checkUtil = new CheckUtil();

    private QADbUtil qaDbUtil = new QADbUtil();

    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;

    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_MENJIN_BE_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/menjin-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");


    private void checkResult(String result, String... checkColumnNames) {
        logger.info("result = {}", result);
        JSONObject res = JSONObject.parseObject(result);
        if (!res.getInteger("code").equals(1000)) {
            throw new RuntimeException("result code is " + res.getInteger("code") + " not success code");
        }
        for (String checkColumn : checkColumnNames) {
            Object column = res.getJSONObject("data").get(checkColumn);
            logger.info("{} : {}", checkColumn, column);
            if (column == null) {
                throw new RuntimeException("result does not contains column " + checkColumn);
            }
        }
    }




    private void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }


    @AfterClass //还没改
    public void clean() {
        //qaDbUtil.closeConnection();
        dingPushFinal();
    }

    @BeforeMethod //还没改
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }


    @Test
    public void delUserBatch() throws Exception {
        List<Integer> inputList=null;
        try (FileReader reader = new FileReader("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/c.txt");
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;

            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                String[] inputdatas=line.split("    ");
                for(String s:inputdatas){
                    String[] a = s.split("\\s+");
                    //System.out.println(a[0]);
                    //System.out.println(a[1]);
                    menjin.userDelete(a[0],a[1]);
                    System.out.println("del" + a[1]);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void deleteuser() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "删除人物\n";

        String key = "";

        try {
            //menjin.userDelete(menjin.scopeUser,"lvxueqing");
            menjin.userDelete(menjin.scopeUser,"user1588923482384");
            //menjin.userDelete(menjin.existUserscope,"existpeopletest");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }
    @Test
    public void adddeviceauth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "配置设备权限\n";

        String key = "";

        try {
            //配置权限

            int pass_num = 3;
            //Long start_time = menjin.todayStartLong();
            //Long end_time = start_time + 86400000;
            //Long start_time = -1L;
            //Long end_time = -1L;
            //Long start_time = menjin.todayStartLong() - 86400000 -86400000;
            //Long end_time = start_time + 86400000;

            String start_time = "03:00:00";
            String end_time = "23:59:00";

            //JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");

            menjin.authAdd("7404475132150784","","","DEVICE",config);
            //menjin.authAdd("152","","","DEVICE",config);

            // menjin.deviceauthDelete("7404475132150784");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void deldeviceauth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "删除设备权限\n";

        String key = "";

        try {
            //menjin.deviceauthDelete("7404475132150784","");
            menjin.deviceauthDelete("7404475132150784");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }


    @Test
    public void adduser() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "\n";

        String key = "";

        try {
            //人物注册

            String scope = menjin.scopeUser;
            String user_id = "lvxueqing";
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userAdd(scope,user_id,image_type,face_image,"177BDC49","");

            //menjin.userAdd(menjin.EnDevice,"existpeopletest","BASE64",getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/51.png"),"","别删");
            //menjin.userAdd(menjin.EnDevice,"testtuisong","BASE64",getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/52.png"),"","别删");

            //String scope = menjin.fifty_people;

            //for (int i = 1; i < 51; i++){
               // String user_id = "fifty0" + i;
                //String image_type = "BASE64";
                //String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/" + i + ".png");
                //menjin.userAdd(scope,user_id,image_type,face_image,"","测试勿删");
                //Thread.sleep(100);
                //System.out.println(user_id);

            //}

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void adduserauth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "配置用户权限\n";

        String key = "";

        try {
            //配置权限

            int pass_num = -1;
            //Long start_time = menjin.todayStartLong()-86400000-86400000;
            //Long end_time = start_time + 86400000;
            //Long start_time = menjin.todayStartLong();
            //Long end_time = start_time + 86400000;
            //Long start_time = -1L;
            //Long end_time = -1L;
            //JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");



            String start_time = "00:00:00";
            String end_time = "23:22:00";

            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"DAY");

            menjin.authAdd("7404475132150784",menjin.scopeUser,"lvxueqing","USER",config);
            //menjin.authAdd("7404475132150784",menjin.existUserscope,"existpeopletest","USER",config);


            // menjin.deviceauthDelete("7404475132150784");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void deluserauth() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "删除用户权限\n";

        String key = "";

        try {
            menjin.authDelete("19999");

//            List listuser = new ArrayList();
//            listuser.add("\""+"lvxueqing"+"\"");
//            System.out.println(listuser);
//            menjin.authDelete(menjin.scopeUser,"7404475132150784",listuser,"USER");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    //@Test
    public void adddevice() throws Exception {
        menjin.deviceAdd(menjin.EnDevice,"Testdevice2");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice3");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice4");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice5");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice6");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice7");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice8");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice9");
        menjin.deviceAdd(menjin.EnDevice,"Testdevice10");
    }

    @Test
    public void operatordevice() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "禁用设备\n";

        String key = "";

        try {
            //menjin.operateDevice("7404475132150784","DISABLE");
            menjin.operateDevice("7404475132150784","ENABLE");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void tb() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "禁用设备\n";

        String key = "";

        try {

            //menjin.operateDevice("7404475132150784","ENABLE");
            menjin.fullSync("7404475132150784","USER_INFO");
            //menjin.incSync("7404475132150784","AUTH_USER",1586144086000L);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    //@Test
    public void test() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "配置用户权限设备id空\n";

        String key = "";

        try {
            //注册人物，单一人脸
            String scope = menjin.scopeUser;
            String user_id = "user" + System.currentTimeMillis();
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/w1.png");
            menjin.userAdd(scope,user_id,image_type,face_image,user_id,"testcrf");

            //配置人物通行权限
            int pass_num = 2;
            Long start_time = menjin.todayStartLong();
            Long end_time = start_time + 86400000;
            JSONObject config = menjin.authconfig(pass_num,start_time,end_time,"FOREVER");
            menjin.authAdd("null",scope,user_id,"USER",config);

            //删除人物
            menjin.userDelete(scope,user_id);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void delscope() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "删除层级\n";

        String key = "";

        try {
            menjin.scopeDelete("14876","1");
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void updateuser() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "\n";

        String key = "";

        try {
            //人物更新

            String scope = menjin.scopeUser;
            String user_id = "lvxueqing";
            String image_type = "BASE64";
            String face_image = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/吕雪晴.JPG");
            menjin.userUpdate(scope,user_id,image_type,face_image,"177BDC49","小吕今天也是美女");
            //menjin.userAdd(menjin.EnDevice,"existpeopletest","BASE64",getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/existtest.png"),"","别删");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }

    @Test
    public void qrcode() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String function = "\n";

        String key = "";

        try {
            //人物更新

            String scope = menjin.scopeUser;
            String user_id = "lvxueqing";
            menjin.userQRCode(scope,user_id);
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, function);
        }
    }




//    ---------------------------------------------------通用方法--------------------------------------------------------------

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        //qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("门禁日常-数据一致性 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        msg = msg.replace("java.lang.Exception: ", "异常：");
        msg = msg.replace("java.lang.IllegalArgumentException:", "异常：");
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());
    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();

            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {"18513118484", //杨航
                    "15011479599", //谢志东
                    "15898182672"}; //华成裕
            alarmPush.alarmToRd(rd);
        }
    }


    private static String getImgStr(String imgFile) { //图片转base64
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

       //public static void main(String[] args) throws Exception {// ---不用理我！
        //String path = "src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/分辨率较低.png";
        //String imgbese = getImgStr(path);




    //}


}