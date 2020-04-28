package com.haisheng.framework.testng.defence;

import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.StatusCode;
import com.haisheng.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public class DefenceConsistencyDaily {

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
    public String CUSTOMER_REGISTER_ROUTER = "/business/defence/CUSTOMER_REGISTER/v1.0";
    public String CUSTOMER_DELETE_ROUTER = "/business/defence/CUSTOMER_DELETE/v1.0";

    public final long VILLAGE_ID = 8;

//    ------------------------------------------------------非创单验证（其他逻辑）-------------------------------------


    @Test
    public void alarmLogPageOperateTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "告警记录(分页查询)中的告警条数==告警统计中的次数";

        logger.info("\n\n" + caseName + "\n");

        try {

//            String deviceId = defence.device1Caiwu;
//            String deviceId = defence.deviceXieduimen;
            String deviceId = defence.deviceDongbeijiao;

//            告警记录(分页查询)
            int total = defence.alarmLogPage(deviceId, 1, 100).getJSONObject("data").getInteger("total");

//            告警统计
            int alarmCount = defence.deviceAlarmStatistic(deviceId).getJSONObject("data").getInteger("alarm_count");

            if (total!=alarmCount){
                throw new Exception("告警记录（分页查询）中的记录条数=" + total + "，告警统计中的总数=" + alarmCount);
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


    @BeforeClass
    public void initial() {
        defence.initial();
    }

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
                "pull_rtsp_url", "expire_time", "device_status"
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

    @DataProvider(name = "CUSTOMER_REG")
    public Object[][] customerReg() {
        return new Object[][]{
//                        faceUrl,userId,name,phone,type,cardKey,age,sex,address,birthday
                new Object[]{
//                        userId相同，其他均不同
                        "userId", defence.nanhaiFaceUrlNew, "userId", defence.genRandom7(), defence.genPhoneNum(), "RESIDENT",
                        defence.genRandom(), "20", "MALE", "address", "birthday", StatusCode.BAD_REQUEST
                },

                new Object[]{
//                        phone+name相同，其他均不同
                        "phone+name", defence.nanhaiFaceUrlNew, defence.genRandom(), "name", "phone", "RESIDENT",
                        defence.genRandom(), "20", "MALE", "address", "birthday", StatusCode.BAD_REQUEST
                },

                new Object[]{
//                        cardKey相同，其他均不同
                        "cardKey", defence.nanhaiFaceUrlNew, defence.genRandom(), defence.genRandom7(), defence.genPhoneNum(), "RESIDENT",
                        "cardKey", "20", "MALE", "address", "birthday", StatusCode.BAD_REQUEST
                },

                new Object[]{
//                        faceUrl相同，其他的参数不同
                        "faceUrl", defence.nalaFaceUrlNew, defence.genRandom(), defence.genRandom7(), defence.genPhoneNum(), "RESIDENT",
                        defence.genRandom(), "20", "MALE", "address", "birthday", StatusCode.BAD_REQUEST
                }
        };
    }

    @DataProvider(name = "CUSTOMER_DELETE")
    public Object[][] customerDelete() {
        return new Object[][]{
//                      village_id，user_id
                new Object[]{
//                       villageId不存在，userId存在
                        1, "userId"
                },

                new Object[]{
//                        villageId存在，userId不存在
                        VILLAGE_ID, "notExist"
                },
        };
    }

    @DataProvider(name = "CUSTOMER_BLACK_REG_USERID_NEWUSER")
    public Object[][] customerDeleteUserIdNewuser() {
        return new Object[][]{
//                      village_id，user_id
                new Object[]{
//                      与社区人员是同一个face
                        "faceUrl"
                },

                new Object[]{
//                      与社区人员是不同face
                        "faceUrl"
                },
        };
    }

    @DataProvider(name = "CUSTOMER_BLACK_REG_NEWUSER")
    public Object[][] customerDeleteNewuser() {
        return new Object[][]{
//                      village_id，user_id
                new Object[]{
//                      与社区人员是同一个face
                        "faceUrl", StatusCode.BAD_REQUEST
                },

                new Object[]{
//                      与社区人员是不同face
                        "faceUrl", StatusCode.SUCCESS
                },
        };
    }
}

