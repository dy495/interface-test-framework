package com.haisheng.framework.testng.defence;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public class DefenceSingleDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //    工具类变量
    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dt = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    Defence defence = new Defence();

    //    入库相关变量
    private String failReason = "";
    private Case aCase = new Case();

    //    case相关变量
    private String liaoFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/liao.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903004987&Signature=TYljFO4ipdEJvj1QDKSnjcVjbpA%3D";
    private String liaoMaskFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/liaoMask.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005006&Signature=x%2B2GjT%2BedL82HhL6n6%2FOUMxfpvU%3D";
    private String xueqingFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/xueqing.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005023&Signature=Hv9x9LsKtFJCGjV6e%2F1RXfuB02s%3D";
    private String xueqingMaskFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/xueqingMask.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005047&Signature=oBUSxN8rLPxtcj3JDIHnHoOfmgM%3D";
    private String yuFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/yu_7.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005104&Signature=ASaweFXsYZsmrVRXC2MLUAwqArA%3D";
    private String yuMaskFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/yuMask.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005085&Signature=GMfI5sVHwhBs2QXNX1whHoMJFp0%3D";
    private String hangFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/yang_4.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903005065&Signature=cv0C8aHoOmWimkWYPRGjua2jwhQ%3D";
    private String hangMaskFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/AI/hangMask.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1903004952&Signature=oUof5bUV%2BHBJk%2BAYyW5XW%2BkJCgo%3D";

    private String hangGoodFaceUrl = "http://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E6%9D%A8%E8%88%AA.jpg?OSSAccessKeyId=LTAIlYpjA39n18Yr&Expires=1587977038&Signature=2ajWe69Wl%2FSUi2PuRnKKzuWv0mU%3D";
    private String liaoGoodFaceUrl = "https://retail-huabei2.oss-cn-beijing.aliyuncs.com/BUSINESS_RISK_DAILY/qa_test/soho_staff/%E5%BB%96%E7%A5%A5%E8%8C%B9.jpg?OSSAccessKeyId=LTAILRdMUAwTZdPh&Expires=1948075013&Signature=lAN0ho1qYA2X0AJbp5VUA%2BLq49o%3D";


    private String boundaryDeviceId = "153";
    private String blackDeviced = "150";
    private String NumDeviced = "155";
//    ------------------------------------------------------非创单验证（其他逻辑）-------------------------------------

    @Test
    public void customerReg() throws Exception {
//        defence.customerDelete("6ef2cae9-4f97-4ec6-85ff-eff1c722d4b3");

//        String faceUrl = yuFaceUrl;
        String faceUrl = xueqingFaceUrl;
        String userId = defence.genRandom();

        defence.customerReg(faceUrl,userId);
    }

    @Test
    public void cusotmerDelete() throws Exception {
//        String userId = "0000";
        String userId = "464ed4ce-fdb3-4375-9e46-aeceb345d4fe";
//        String userId = "5318b438-3a08-4444ba3-9833-442bdc8daad9";
        defence.customerDelete(userId);
    }

    @Test
    public void blackDelete() throws Exception {

//        String blackId = "9832dbf4-350d-4ac7-b8bb-650acffdb67b";
        String blackId = "cde60190-3cf4-45da-8810-a3e8e933b0f5";

        defence.customerDeleteBlack(blackId);
    }

    @Test
    public void blackRegUserId() throws Exception {
//        defence.customerDelete("6ef2cae9-4f97-4ec6-85ff-eff1c722d4b3");

        String blackId = "";
        String level = "level";
        String label = "label";

        defence.customerRegBlackUserId(blackId,label, level);
    }

    @Test
    public void blackRegNewUser() throws Exception {
//        defence.customerDelete("6ef2cae9-4f97-4ec6-85ff-eff1c722d4b3");

        String faceUrl = xueqingFaceUrl;
//        String faceUrl = liaoGoodFaceUrl;
//        String faceUrl = hangGoodFaceUrl;
//        String faceUrl = hangFaceUrl;
//        String faceUrl = liaoFaceUrl;
//        String faceUrl = liaoFaceUrl;
        String level = "level";
        String label = "label";

        JSONObject data = defence.customerRegBlackNewUser(faceUrl,label, level).getJSONObject("data");
        String alarmCustomerId = data.getString("alarm_customer_id");

    }

    @Test
    public void boundaryAlarmList() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设置周界报警-获取";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = boundaryDeviceId;

//            周界列表
            JSONArray axis = defence.boundaryAlarmInfo(deviceId).getJSONObject("data").getJSONArray("boundary_axis");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void boundaryAlarmAdd() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;


        logger.info("\n\n" + caseName + "\n");

        try {

//            public String device1Caiwu = "157";
//            public String device1Huiyi = "151";
//            public String deviceYilaoshi = "150";
//            public String deviceXieduimen = "152";
//            public String deviceChukou = "153";
//            public String deviceDongbeijiao = "155";

//            String deviceId = defence.device1Caiwu;
//            String deviceId = defence.device1Huiyi;
//            String deviceId = defence.deviceYilaoshi;
//            String deviceId = defence.deviceXieduimen;
//            String deviceId = defence.deviceChukou;
//            String deviceId = defence.deviceDongbeijiao;
            String deviceId = boundaryDeviceId;

//            注册周界
            defence.boundaryAlarmAdd(deviceId);

//            周界列表
//            JSONArray axis = defence.boundaryAlarmInfo(deviceId).getJSONObject("data").getJSONArray("boundary_axis");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void boundaryAlarmDelete() throws Exception {
//        defence.boundaryAlarmDelete(boundaryDeviceId);
        String deviceId = "157";
        defence.boundaryAlarmDelete(deviceId);
    }

    @Test
    public void alarmLogPageOperate() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录(分页查询)-告警记录处理，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = defence.deviceDongbeijiao;
            String operator = "sophie";
            String optResult = "有不明人员进入与周界，目前没有确定是具体的那个人，继续观察";

//            告警记录(分页查询)
            JSONObject data = defence.alarmLogPage(deviceId, 1, 1).getJSONObject("data");
            String alarmId = data.getString("id");

            Object[] objects = alarmLogPageNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects[i].toString();
                checkUtil.checkNotNull("告警记录(分页查询)--", data, key);
            }

//            告警记录处理
            defence.alarmLogOperate(alarmId, operator, optResult);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void deivceCustomerNumAlarmAdd() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设备画面人数告警设置，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

//            String deviceId = defence.device1Caiwu;
//            String deviceId = defence.device1Huiyi;
//            String deviceId = defence.deviceYilaoshi;
//            String deviceId = defence.deviceXieduimen;
//            String deviceId = defence.deviceChukou;
//            String deviceId = defence.deviceDongbeijiao;

            int threshold = 1;

//            设备画面人数告警设置
            defence.deviceCustomerNumAlarmAdd(defence.device1Caiwu, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.device1Huiyi, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceYilaoshi, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceXieduimen, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceChukou, threshold);
            defence.deviceCustomerNumAlarmAdd(defence.deviceDongbeijiao, threshold);



//            String[] devices = {defence.device1Caiwu,defence.device1Huiyi,defence.deviceYilaoshi,
//                    defence.deviceXieduimen,defence.deviceChukou,defence.deviceDongbeijiao};

//            for (int i = 0; i < devices.length; i++) {
//                defence.deviceCustomerNumAlarmAdd(deviceId, threshold);
//            }


        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void deivceCustomerNumAlarmDelete() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设备画面人数告警设置，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = NumDeviced;
            int threshold = 10;

////            设备画面人数告警设置
//            defence.deviceCustomerNumAlarmAdd(deviceId, threshold);

//            删除
            defence.deviceCustomerNumAlarmDelete(deviceId);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void customerInfo() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人物详情信息，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = yuFaceUrl;
            String userId = defence.genRandom();

//            社区人员注册
            String customerId = defence.customerReg(faceUrl, userId).getJSONObject("data").getString("customer_id");

//            人物详情信息
            JSONObject data = defence.customerInfo(userId, customerId).getJSONObject("data").getJSONObject("info");

            Object[] objects = customerInfoNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects[i].toString();
                checkUtil.checkNotNull("人物详情信息--", data, key);
            }

//            删除社区人员
            defence.customerDelete(userId);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
//            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void customerSearchListTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)";

        logger.info("\n\n" + caseName + "\n");

        try {

            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

            String deviceId = defence.device1Caiwu;
//            String deviceId = defence.device1Huiyi;
//            String deviceId = defence.deviceYilaoshi;
//            String deviceId = defence.deviceXieduimen;
//            String deviceId = defence.deviceChukou;
//            String deviceId = defence.deviceDongbeijiao;

            String sex = "FEMALE";
            String age = "";
            String hair = "LONG";
            String clothes = "LONG_SLEEVES";
            String clothesColour = "YELLOW";
            String trousers = "";
            String trousersColour = "";
            String hat = "";
            String knapsack = "";
            String similarity = "LOW";


//            结构化检索(分页查询)
            defence.customerSearchList(deviceId, startTime, endTime,
            sex, age, hair, clothes, clothesColour, trousers, trousersColour, hat, knapsack,similarity);

            defence.customerSearchList(deviceId, startTime, endTime);


        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }





    @AfterClass
    public void clean() {
        defence.clean();
    }

    @BeforeClass
    public void initial(){
        defence.initial();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        aCase = new Case();
    }

    @DataProvider(name = "VILLAGE_LIST_NOT_NULL")
    public Object[] villageListNotNull() {
        return new Object[]{
                "[list]-village_id", "[list]-village_name"
        };
    }

    @DataProvider(name = "DEVICE_LIST_NOT_NULL")
    public Object[] deviceListNotNull() {
        return new Object[]{
                "[list]-device_id", "[list]-device_name", "[list]-device_url", "[list]-device_type",
        };
    }

//    社区人员注册
    @DataProvider(name = "CUSTOMER_REGISTER_NOT_NULL")
    public Object[] customerRegNotNull() {
        return new Object[]{
                "user_id", "customer_id"
        };
    }

//    注册人员黑名单
    @DataProvider(name = "CUSTOMER_REGISTER_BLACK_NOT_NULL")
    public Object[] customerRegBlackNotNull() {
        return new Object[]{
                "alarm_customer_id"
        };
    }

//    删除人员黑名单
    @DataProvider(name = "CUSTOMER_DELETE_BLACK_NOT_NULL")
    public Object[] customerDeleteBlackNotNull() {
        return new Object[]{
                "alarm_customer_id"
        };
    }

//    获取人员黑名单
    @DataProvider(name = "CUSTOMER_BLACK_PAGE_NOT_NULL")
    public Object[] customerBlackPageNotNull() {
        return new Object[]{
                "[list]-user_id", "[list]-face_url", "[list]-level", "[list]-label"
        };
    }

//    获取设备周界报警配置
    @DataProvider(name = "BOUNDARY_ALARM_INFO_NOT_NULL")
    public Object[] boundaryAlarmInfoNotNull() {
        return new Object[]{
                "x", "y"
        };
    }

//    告警记录(分页查询)
    @DataProvider(name = "ALARM_LOG_PAGE_NOT_NULL")
    public Object[] alarmLogPageNotNull() {
        return new Object[]{
                "[list]-id", "[list]-alarm_type", "[list]-alarm_desc", "[list]-device_id", "[list]-device_name",
                "[list]-pic_url", "[list]-opt_status", "[list]-opt_result", "[list]-operator", "[list]-opt_timestamp",
                "[list]-level"
        };
    }

//    人脸识别记录分页查询
    @DataProvider(name = "CUSTOMER_HISTORY_CAPTURE_PAGE_NOT_NULL")
    public Object[] customerHistoryCapturePageNotNull() {
        return new Object[]{
                "[list]-id", "[list]-customer_id", "[list]-timestamp", "[list]-pic_url", "[list]-village_id",
                "[list]-village_name", "[list]-device_id", "[list]-device_name", "[list]-page", "[list]-total"
        };
    }

//    轨迹查询(人脸搜索)
    @DataProvider(name = "CUSTOMER_FACE_TRACE_LIST_NOT_NULL")
    public Object[] customerFaceTraceListNotNull() {
        return new Object[]{
                "[list]-id", "[list]-customer_id", "[list]-timestamp", "[list]-pic_url", "[list]-village_id",
                "[list]-village_name", "[list]-device_id", "[list]-device_name", "[list]-similarity"
        };
    }

//    结构化检索(分页查询)
    @DataProvider(name = "CUSTOMER_SEARCH_LIST_NOT_NULL")
    public Object[] customerSearchListNotNull() {
        return new Object[]{
                "[list]-id", "[list]-customer_id", "[list]-pic_url", "[list]-timestamp",
                "[list]-village_id", "[list]-village_name", "[list]-device_id", "[list]-device_name"
        };
    }

//    人物详情信息
    @DataProvider(name = "CUSTOMER_INFO_NOT_NULL")
    public Object[] customerInfoNotNull() {
        return new Object[]{
                "customer_id"
        };
    }

//    设备画面播放(实时/历史)
    @DataProvider(name = "DEVICE_STREAM_NOT_NULL")
    public Object[] deviceStreamNotNull() {
        return new Object[]{
                "pull_rtsp_url","expire_time","device_status"
        };
    }

//    客流统计
    @DataProvider(name = "DEVICE_CUSTOMER_FLOW_STATISTIC_NOT_NULL")
    public Object[] deviceCustomerFlowStatisticNotNull() {
        return new Object[]{
                "pv", "device_status", "status_name"
        };
    }

//    报警统计
    @DataProvider(name = "DEVICE_ALARM_STATISTIC_NOT_NULL")
    public Object[] deviceAlarmStatisticNotNull() {
        return new Object[]{
                "alarm_count", "device_status", "status_name"
        };
    }
}

