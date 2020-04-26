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

public class DefenceNotNullDaily {

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

    private String boundaryDeviceId = "153";
    private String blackDeviced = "150";
    private String NumDeviced = "155";
//    ------------------------------------------------------非创单验证（其他逻辑）-------------------------------------

    @Test
    public void villageList() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "社区管理";

        logger.info("\n\n" + caseName + "\n");

        try {

            JSONObject data = defence.villageList().getJSONObject("data");

//            校验非空
            Object[] objects = villageListNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("社区管理--", data, key);
            }

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

    @Test
    public void deviceList() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "同步社区下设备列表";

        logger.info("\n\n" + caseName + "\n");

        try {

            JSONObject data = defence.deviceList().getJSONObject("data");

            Object[] objects = deviceListNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("同步社区下设备列表--", data, key);
            }

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

    @Test
    public void customer() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "社区人员注册-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = liaoFaceUrl;
            String userId = defence.genRandom();

//            注册
            defence.customerReg(faceUrl, userId);

//            删除
            defence.customerDelete(userId);

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

    @Test
    public void customerBlack() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "社区人员注册-注册黑名单-黑名单列表-删除黑名单";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = liaoFaceUrl;
            String userId = defence.genRandom();

//            注册社区人员
            defence.customerReg(faceUrl, userId);

            String level = defence.genRandom7();
            String label = defence.genRandom7();

//            注册黑名单
            JSONObject data = defence.customerRegBlackUserId(userId, level, label).getJSONObject("data");
            String alarmCustomerId = data.getString("alarm_customer_id");
            Object[] objects = customerRegBlackNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("注册黑名单--", data, key);
            }

//            黑名单列表
            data = defence.customerBlackPage(1, 1).getJSONObject("data");

            objects = customerDeleteBlackNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("黑名单列表--", data, key);
            }

//            删除黑名单
            data = defence.customerDeleteBlack(alarmCustomerId).getJSONObject("data");

            objects = customerBlackPageNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("删除黑名单--", data, key);
            }

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

    @Test
    public void boundaryAlarm() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设置周界报警-获取-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = boundaryDeviceId;

//            注册周界
            defence.boundaryAlarmAdd(deviceId);

//            周界列表
            JSONArray axis = defence.boundaryAlarmInfo(deviceId).getJSONObject("data").getJSONArray("boundary_axis");
            Object[] objects = boundaryAlarmInfoNotNull();

            for (int i = 0; i < axis.size(); i++) {
                JSONObject single = axis.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    String key = objects.toString();
                    checkUtil.checkNotNull("周界列表--", single, key);
                }
            }

//            删除周界
            defence.boundaryAlarmDelete(deviceId);

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

    @Test
    public void alarmLogPageOperate() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录(分页查询)-告警记录处理，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = boundaryDeviceId;
            String operator = "sophie";
            String optResult = "有不明人员进入与周界，目前没有确定是具体的那个人，继续观察";

//            告警记录(分页查询)
            JSONObject data = defence.alarmLogPage(deviceId, 1, 1).getJSONObject("data");
            String alarmId = data.getString("id");

            Object[] objects = alarmLogPageNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
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
            defence.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void deivceCustomerNumAlarm() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设备画面人数告警设置，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = NumDeviced;
            int threshold = 10;

//            设备画面人数告警设置
            defence.deviceCustomerNumAlarmAdd(deviceId, threshold);

//            删除
            defence.deviceCustomerNumAlarmDelete(deviceId);
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

    @Test
    public void customerHistoryCapturePage() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人脸识别记录分页查询";

        logger.info("\n\n" + caseName + "\n");

        try {

            String namePhone = "";
            String device_id = blackDeviced;
            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            人脸识别记录分页查询
            JSONObject data = defence.customerHistoryCapturePage(namePhone, device_id, startTime, endTime, 1, 10).getJSONObject("data");

            Object[] objects = alarmLogPageNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("人脸识别记录分页查询--", data, key);
            }

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

    @Test
    public void customerFaceTraceList() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "轨迹查询(人脸搜索)，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String picUrl = liaoMaskFaceUrl;
            String similarity = "LOW";
            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            轨迹查询(人脸搜索)
            JSONObject data = defence.customerFaceTraceList(picUrl, startTime, endTime, similarity).getJSONObject("data");

            Object[] objects = customerFaceTraceListNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("轨迹查询(人脸搜索)--", data, key);
            }

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

    @Test
    public void customerSearchList() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "结构化检索(分页查询)，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = boundaryDeviceId;
            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            结构化检索(分页查询)
            JSONObject data = defence.customerSearchList(deviceId, startTime, endTime).getJSONObject("data");

            Object[] objects = customerSearchListNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("结构化检索(分页查询)--", data, key);
            }

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

    @Test
    public void customerInfo() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "人物详情信息，验证code==1000";

        logger.info("\n\n" + caseName + "\n");

        try {

            String faceUrl = liaoFaceUrl;
            String userId = defence.genRandom();

//            社区人员注册
            String customerId = defence.customerReg(faceUrl, userId).getJSONObject("data").getString("customer_id");

//            人物详情信息
            JSONObject data = defence.customerInfo(userId, customerId).getJSONObject("data");

            Object[] objects = customerInfoNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("人物详情信息--", data, key);
            }

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

    @Test
    public void messageSwitch() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "实时通知开关";

        logger.info("\n\n" + caseName + "\n");

        try {

            String messageSwitch = "CLOSE";
            String messageType = "ALARM";

//            实时通知开关
             defence.messageSwitch(messageSwitch,messageType);

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

    @Test
    public void deviceStream() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设备画面播放(实时/历史)";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = boundaryDeviceId;
            long startTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
            long endTime = System.currentTimeMillis();

//            实时视频流地址
            JSONObject data = defence.deviceStream(deviceId).getJSONObject("data");

            Object[] objects = deviceStreamNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("设备画面播放（实时）--", data, key);
            }

//            历史视频流地址
            data = defence.deviceStream(deviceId, startTime, endTime).getJSONObject("data");

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("设备画面播放（实时）--", data, key);
            }

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

    @Test
    public void deviceCustomerFlowStatistic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设备实时-客流统计";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = boundaryDeviceId;

//            设备实时-报警统计
            JSONObject data = defence.deviceCustomerFlowStatistic(deviceId).getJSONObject("data");

            Object[] objects = deviceCustomerFlowStatisticNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("设备实时-客流统计--", data, key);
            }

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

    @Test
    public void deviceAlarmStatistic() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设备实时-报警统计";

        logger.info("\n\n" + caseName + "\n");

        try {

            String deviceId = blackDeviced;

//            设备实时-报警统计
            JSONObject data = defence.deviceAlarmStatistic(deviceId).getJSONObject("data");

            Object[] objects = deviceCustomerFlowStatisticNotNull();

            for (int i = 0; i < objects.length; i++) {
                String key = objects.toString();
                checkUtil.checkNotNull("设备实时-报警统计--", data, key);
            }

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

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */

    @AfterClass
    public void clean() {
        defence.clean();
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
                "[boundary_axis]-x", "[boundary_axis]-y"
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

