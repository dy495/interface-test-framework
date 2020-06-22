package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class XunDian {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();

    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    public QADbUtil qaDbUtil = new QADbUtil();
    public int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    public int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;

    public String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/patrol-shops-daily/buildWithParameters?case_name=";

    public String DEBUG = System.getProperty("DEBUG", "true");

    public String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dp" +
            "blRpbWUiOjE1OTI3OTY0ODI2NjJ9.Dq2dIDYcJPRP9h3Dvgr0MMbfXwivtBdmrQUt8kKyh2k";

    public HttpConfig config;

    private final String IPPort = "http://123.57.148.247";
    private final String SHOP_ID = "28760";

    DateTimeUtil dt = new DateTimeUtil();
    String word20 = "12345678901234567890";
    String word100 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
    String word30 = "123456789012345678901234567890";

//    #########################################################接口调用方法########################################################


//    ********************************************************PC端登陆*************************************************888



//    ***************************************************** 一、权限相关接口************************************************************

    /**
     * @description: 1.1 获取登陆验证码
     * @author: liao
     * @time:
     */
    public JSONObject getSmsCode(String phone) throws Exception {
        String url = "/patrol-verification-code";
        String json =
                "{\n" +
                        "\"phone\":\"" + phone + "\"," +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.2 登陆
     * @author: liao
     * @time:
     */
    public JSONObject login(int type, String userName, String passwd, String phone, String veriCode) throws Exception {
        String url = "/patrol-login";
        String json =
                "{\n" +
                        "    \"type\":" + type + ",\n" +
                        "    \"username\":\"" + userName + "\",\n" +
                        "    \"password\":\"" + passwd + "\",\n" +
                        "    \"phone\":\"" + phone + "\",\n" +
                        "    \"verification_code\":\"" + veriCode + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.3 通过token获取用户信息
     * @author: liao
     * @time:
     */
    public JSONObject userDetail() throws Exception {
        String url = "/patrol/user/detail";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.4 登出
     * @author: liao
     * @time:
     */
    public JSONObject logOut() throws Exception {
        String url = "/patrol-logout";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

//    ********************************************************二、巡店通用接口********************************************************

    /**
     * @description: 2.1 获取区划
     * @author: liao
     * @time:
     */
    public JSONObject patrolDistricts() throws Exception {
        String url = "/patrol/districts";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.2 图片上传
     * @author: liao
     * @time:
     */
    public JSONObject uploadImage(String picData, int type) throws Exception {
        String url = "/patrol/pic/base64/upload";
        String json =
                "{\n" +
                        "    \"pic_data\":\"" + picData + "\",\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "    \"type\":" + type + "0\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

//    ************************************************三. 定检任务相关接口*******************************************************

    /**
     * @description: 3.1 获取巡检员列表
     * @author: liao
     * @time:
     */
    public JSONObject inspectorList() throws Exception {
        String url = "/patrol/schedule-check/inspector/list";
        String json =
                "{}";

        String res = httpPostNoPrintPara(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 3.2 获取可巡查门店
     * @author: liao
     * @time:
     */
    public JSONObject shopList(String inspectorId, String districtCode) throws Exception {
        String url = "/patrol/schedule-check/shop/list";
        String json =
                "{\n" +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"district_code\":\"" + districtCode + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 3.3 新建定检任务
     * @author: liao
     * @time:
     */
    public JSONObject addScheduleCheck(String name, String cycle, String dates, String sendTime, String validStart, String validEnd,
                                       String inspectorId,String shopId) throws Exception {
        String url = "/patrol/schedule-check/add";
        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"cycle\":\"" + cycle + "\",\n" +
                        "    \"dates\":[\n" + dates + "    ],\n" +
                        "    \"send_time\":\"" + sendTime + "\",\n" +
                        "    \"valid_start\":\"" + validStart + "\",\n" +
                        "    \"valid_end\":\"" + validEnd + "\",\n" +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"shop_list\":[\n" + shopId + "    ]\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String  addScheduleCheckNoCode(String name, String cycle, String dates, String sendTime, String validStart, String validEnd,
                                       String inspectorId,String shopId) throws Exception {
        String url = "/patrol/schedule-check/add";
        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"cycle\":\"" + cycle + "\",\n" +
                        "    \"dates\":[\n" + dates + "    ],\n" +
                        "    \"send_time\":\"" + sendTime + "\",\n" +
                        "    \"valid_start\":\"" + validStart + "\",\n" +
                        "    \"valid_end\":\"" + validEnd + "\",\n" +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"shop_list\":[\n" + shopId + "    ]\n" +
                        "}";

        String res = httpPost(url, stringUtil.trimStr(json));

        return res;
    }

    public JSONObject addScheduleCheckEmptyPara(String name, String cycle, String dates, String sendTime, String validStart, String validEnd,
                                       String inspectorId,String shopId,String emptyPara) throws Exception {
        String url = "/patrol/schedule-check/add";
        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"cycle\":\"" + cycle + "\",\n" +
                        "    \"dates\":[\n" + dates + "    ],\n" +
                        "    \"send_time\":\"" + sendTime + "\",\n" +
                        "    \"valid_start\":\"" + validStart + "\",\n" +
                        "    \"valid_end\":\"" + validEnd + "\",\n" +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"shop_list\":[\n" + shopId + "    ]\n" +
                        "}";

        JSONObject temp = JSON.parseObject(json);
        temp.put(emptyPara,null);

        json = temp.toJSONString();

        String res = httpPost(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 3.4 定检任务列表
     * @author: liao
     * @time:
     */
    public JSONObject scheduleCheckList(int page, int size) throws Exception {
        String url = "/patrol/schedule-check/page";
        String json =
                "{\n" +
                        "    \"page\":\"" + page + "\",\n" +
                        "    \"size\":\"" + size + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 3.5 删除定检任务
     * @author: liao
     * @time:
     */
    public JSONObject scheduleCheckDelete(long id) throws Exception {
        String url = "/patrol/schedule-check/delete";
        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 3.6 编辑定检任务
     * @author: liao
     * @time:
     */
    public JSONObject scheduleCheckEdit(long id, String name, String cycle, String dates, String sendTime, String validStart, String validEnd,
                                        String inspectorId,String shopId) throws Exception {
        String url = "/patrol/schedule-check/edit";

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\",\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"cycle\":\"" + cycle + "\",\n" +
                        "    \"dates\":[\n" + dates + "    ],\n" +
                        "    \"send_time\":\"" + sendTime + "\",\n" +
                        "    \"valid_start\":\"" + validStart + "\",\n" +
                        "    \"valid_end\":\"" + validEnd + "\",\n" +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"shop_list\":[\n" + shopId + "    ]\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String scheduleCheckEditNoCode(long id, String name, String cycle, String dates, String sendTime, String validStart, String validEnd,
                                        String inspectorId,String shopId) throws Exception {
        String url = "/patrol/schedule-check/edit";

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\",\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"cycle\":\"" + cycle + "\",\n" +
                        "    \"dates\":[\n" + dates + "    ],\n" +
                        "    \"send_time\":\"" + sendTime + "\",\n" +
                        "    \"valid_start\":\"" + validStart + "\",\n" +
                        "    \"valid_end\":\"" + validEnd + "\",\n" +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"shop_list\":[\n" + shopId + "    ]\n" +
                        "}";

        String res = httpPost(url, stringUtil.trimStr(json));

        return res;
    }

    public JSONObject scheduleCheckEditEmptyPara(long id, String name, String cycle, String dates, String sendTime, String validStart, String validEnd,
                                        String inspectorId,String shopId,String emptyPara) throws Exception {
        String url = "/patrol/schedule-check/edit";

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\",\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"cycle\":\"" + cycle + "\",\n" +
                        "    \"dates\":[\n" + dates + "    ],\n" +
                        "    \"send_time\":\"" + sendTime + "\",\n" +
                        "    \"valid_start\":\"" + validStart + "\",\n" +
                        "    \"valid_end\":\"" + validEnd + "\",\n" +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"shop_list\":[\n" + shopId + "    ]\n" +
                        "}";

        JSONObject temp = JSON.parseObject(json);
        temp.put(emptyPara,null);

        json = temp.toJSONString();

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }


//    *********************************************************四. 巡店中心相关接口*******************************************************

    /**
     * @description: 4.1 获取门店列表
     * @author: liao
     * @time:
     */
    public JSONObject shopPage(String name, int status) throws Exception {
        String url = "/patrol/shop/page";

        String json = "";

        if ("".equals(name) && status !=-1){

            json =
                    "{\n" +
                            "    \"name\":\"" + name + "\",\n" +
                            "    \"status\":\"" + status + "\"\n" +
                            "}";
        }else if (!"".equals(name) ){
            json =
                    "{\n" +
                            "    \"name\":\"" + name + "\"\n" +
                            "}";
        }else if (status !=-1){
            json =
                    "{\n" +
                            "    \"status\":\"" + status + "\"\n" +
                            "}";
        }else {
            json="{}";
        }

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.2 获取门店详情
     * @author: liao
     * @time:
     */
    public JSONObject shopDetail(String id) throws Exception {
        String url = "/patrol/shop/detail";

        String json =
                "{\n" +
                        "    \"name\":\"" + id + "\",\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.3 获取门店巡店记录列表
     * @author: liao
     * @time:
     */
    public JSONObject shopChecksPage(int checkResult, int handleStatus, String inspectorId, String orderRule) throws Exception {
        String url = "/patrol/shop/checks/page";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "    \"check_result\":" + checkResult + ",\n" +
                        "    \"handle_status\":" + handleStatus + "," +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"order_rule\":\"" + orderRule + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.4 获取门店巡店记录详情
     * @author: liao
     * @time:
     */
    public JSONObject shopChecksDetail(int id) throws Exception {
        String url = "/patrol/shop/checks/detail";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "    \"id\":" + id + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.5 获取设备列表
     * @author: liao
     * @time:
     */
    public JSONObject shopDeviceList() throws Exception {
        String url = "/patrol/shop/device/list";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.6 获取设备实时流
     * @author: liao
     * @time:
     */
    public JSONObject shopDeviceLive(long deviceId) throws Exception {
        String url = "/patrol/shop/device/live";

        String json =
                "{\n" +
                        "    \"device_id\":" + deviceId + "," +
                        "    \"shop_id\":" + SHOP_ID + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.7 获取设备重播流
     * @author: liao
     * @time:
     */
    public JSONObject shopDeviceReplay(long deviceId, String date, String time) throws Exception {
        String url = "/patrol/shop/device/replay";

        String json =
                "{\n" +
                        "    \"device_id\":" + deviceId + "," +
                        "    \"date\":" + date + "," +
                        "    \"time\":" + time + "," +
                        "    \"shop_id\":" + SHOP_ID + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.8 开始或继续巡店
     * @author: liao
     * @time:
     */
    public JSONObject shopCheckStart(String checkType,int reset,long taskId) throws Exception {
        String url = "/patrol/shop/checks/start";

        String json =
                "{\n" +
                        "    \"check_type\":\"" + checkType + "\"," +
                        "    \"reset\":" + reset + ",\n" +
                        "    \"task_id\":" + taskId + ",\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.9 提交巡检项目结果
     * @author: liao
     * @time:
     */
    public JSONObject checksItemSubmit(long patrolId, long listId, long itemId, int checkResult, String auditComment, String picList) throws Exception {
        String url = "/patrol/shop/checks/item/submit";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "    \"patrol_id\":" + patrolId + ",\n" +
                        "    \"list_id\":" + listId + ",\n" +
                        "    \"item_id\":" + itemId + ",\n" +
                        "    \"check_result\":" + checkResult + ",\n" +
                        "    \"audit_comment\":" + auditComment + ",\n" +
                        "    \"pic_list\":[\n" + picList + "    ]\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.10 提交巡检结果
     * @author: liao
     * @time:
     */
    public JSONObject checksSubmit(long patrolId, String comment) throws Exception {
        String url = "/patrol/shop/checks/submit";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "    \"patrol_id\":" + patrolId + ",\n" +
                        "    \"comment\":" + comment + ",\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * @description: 4.11 门店当前清单项目列表
     * @author: liao
     * @time:
     */
    public JSONObject problemItems() throws Exception {
        String url = "/patrol/shop/problem/items";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.12 负责人
     * @author: liao
     * @time:
     */
    public JSONObject problemResponsors() throws Exception {
        String url = "/patrol/shop/problem/responsors";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.13 留痕
     * @author: liao
     * @time:
     */
    public JSONObject problemMark(long listId, long itemId, String responsorId, String auditComment, String picData) throws Exception {
        String url = "/patrol/shop/problem/mark";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "    \"list_id\":" + listId + ",\n" +
                        "    \"item_id\":" + itemId + ",\n" +
                        "    \"responsor_id\":\"" + responsorId + "\",\n" +
                        "    \"audit_comment\":\"" + auditComment + "\",\n" +
                        "    \"pic_data\":\"" + picData + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.14 获取门店巡检员列表
     * @author: liao
     * @time:
     */
    public JSONObject shopInspectors() throws Exception {
        String url = "/patrol/shop/inspectors";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.15 获取巡店结果筛选项
     * @author: liao
     * @time:
     */
    public JSONObject resultTypeList() throws Exception {
        String url = "/patrol/shop/result-type/list";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.16 获取巡店处理状态筛选项
     * @author: liao
     * @time:
     */
    public JSONObject statusTypeList() throws Exception {
        String url = "/patrol/shop/status-type/list";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

//    *********************************************************五. 移动端首页接口****************************************************

    /**
     * @description: 5.1 获取工作成果
     * @author: liao
     * @time:
     */
    public JSONObject mTaskDetail() throws Exception {
        String url = "/patrol/m/task/detail";

        String json =
                "{}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.2 门店列表
     * @author: liao
     * @time:
     */
    public JSONObject mShopList(int size, long lastTime) throws Exception {
        String url = "/patrol/m/shop/list";

        String json =
                "{\n" +
                        "    \"page\":" + size + "\n" +
                        "    \"last_time\":" + lastTime + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.3 待办/已办列表
     * @author: liao
     * @time:
     */
    public JSONObject mTaskList(int type, int size, long lastId) throws Exception {
        String url = "/patrol/m/task/list";

        String json =
                "{\n" +
                        "    \"type\":" + type + ",\n" +
                        "    \"page\":" + size + ",\n" +
                        "    \"last_id\":" + lastId + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.4 设备定检图片列表
     * @author: liao
     * @time:
     */
    public JSONObject mPicList(long deviceId, String date) throws Exception {
        String url = "/patrol/m/schedule-task/pic/list";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "    \"date\":" + date + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 5.5 复检、不合格处理步骤提交
     * @author: liao
     * @time:
     */
    public JSONObject mStepSubmit(long id, String picList, String comment, int recheckResult) throws Exception {
        String url = "/patrol/m/task/step/submit";

        String json =
                "{\n" +
                        "    \"shop_id\":" + SHOP_ID + ",\n" +
                        "    \"id\":" + id + "\n" +
                        "    \"pic_list\":[\n" + picList + "    ],\n" +
                        "    \"recheck_result\":" + recheckResult + ",\n" +
                        "    \"comment\":\"" + comment + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }


//    *******************************************************六. 定检任务相关接口*****************************

    /**
     * @description: 6.1 获取主账号下所有门店
     * @author: liao
     * @time:
     */
    public JSONObject checkListShops(String districtCode) throws Exception {
        String url = "/patrol/check-list/shops";

        String json =
                "{\n" +
                        "    \"district_code\":\"" + districtCode + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 6.2 新建执行清单
     * @author: liao
     * @time:
     */
    public JSONObject addCheckList(String name, String desc, String title, String comment) throws Exception {
        String url = "/patrol/check-list/add";

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"desc\":\"" + desc + "\",\n" +
                        "    \"items\":[\n" +
                        "        {\n" +
                        "            \"order\":" + 0 + ",\n" +
                        "            \"title\":\"" + title + "\",\n" +
                        "            \"comment\":\"" + comment + "\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"shop_list\":[\n" + SHOP_ID + "    ]\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String addCheckListNoCode(String name, String desc, String title, String comment) throws Exception {
        String url = "/patrol/check-list/add";

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"desc\":\"" + desc + "\",\n" +
                        "    \"items\":[\n" +
                        "        {\n" +
                        "            \"order\":" + 1 + ",\n" +
                        "            \"title\":\"" + title + "\",\n" +
                        "            \"comment\":\"" + comment + "\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"shop_list\":[\n" + SHOP_ID + "    ]\n" +
                        "}";

        String res = httpPost(url, stringUtil.trimStr(json));

        return res;
    }

    public JSONObject addCheckListEmpty(String name, String desc, String title, String comment,String emptyPara,String message) throws Exception {
        String url = "/patrol/check-list/add";

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"desc\":\"" + desc + "\",\n" +
                        "    \"items\":[\n" +
                        "        {\n" +
                        "            \"order\":" + 0 + ",\n" +
                        "            \"title\":\"" + title + "\",\n" +
                        "            \"comment\":\"" + comment + "\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"shop_list\":[\n" + SHOP_ID + "    ]\n" +
                        "}";

        JSONObject temp = JSON.parseObject(json);

        if ("items-title".equals(emptyPara)){

            JSONArray items = temp.getJSONArray("items");

            JSONObject item = items.getJSONObject(0);
            item.put("title","");
            items.add(0,item);
            temp.put("items",items);

            json = temp.toJSONString();

        }else if ("items".equals(emptyPara) || "shop_list".equals(emptyPara)){
            temp.put(emptyPara,null);

            json = temp.toJSONString();
        }else {
            temp.put(emptyPara,"");

            json = temp.toJSONString();
        }

        String res = httpPost(url, stringUtil.trimStr(json));

        checkCode(res,StatusCode.BAD_REQUEST,"新建执行清单," + emptyPara + "为空！");
        checkMessage("新建执行清单," + emptyPara + "为空！",res,message);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 6.3 执行清单列表
     * @author: liao
     * @time:
     */
    public JSONObject checkListPage(int page, int size) throws Exception {
        String url = "/patrol/check-list/page";

        String json =
                "{\n" +
                        "    \"page\":" + page + ",\n" +
                        "    \"size\":" + size + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 6.4 删除执行清单任务
     * @author: liao
     * @time:
     */
    public JSONObject checkListDelete(long id) throws Exception {
        String url = "/patrol/check-list/delete";

        String json =
                "{\n" +
                        "    \"id\":" + id + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 6.5 执行清单详情
     * @author: liao
     * @time:
     */
    public JSONObject checkListDetail(long id) throws Exception {
        String url = "/patrol/check-list/detail";

        String json =
                "{\n" +
                        "    \"id\":" + id + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 6.6 编辑执行清单
     * @author: liao
     * @time:
     */
    public JSONObject checkListEdit(long id, String name, String desc, String title, String comment) throws Exception {
        String url = "/patrol/check-list/edit";

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\",\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"desc\":\"" + desc + "\",\n" +
                        "    \"items\":[\n" +
                        "        {\n" +
                        "            \"order\":" + 1 + ",\n" +
                        "            \"title\":\"" + title + "\",\n" +
                        "            \"comment\":\"" + comment + "\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"shop_list\":[\n" + SHOP_ID + "    ]\n" +
                        "}";

        String res = httpPostWithCheckCode(url, stringUtil.trimStr(json));

        return JSON.parseObject(res).getJSONObject("data");
    }

    public String checkListEditNoCode(long id, String name, String desc, String title, String comment) throws Exception {
        String url = "/patrol/check-list/edit";

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\",\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"desc\":\"" + desc + "\",\n" +
                        "    \"items\":[\n" +
                        "        {\n" +
                        "            \"order\":" + 1 + ",\n" +
                        "            \"title\":\"" + title + "\",\n" +
                        "            \"comment\":\"" + comment + "\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"shop_list\":[\n" + SHOP_ID + "    ]\n" +
                        "}";

        String res = httpPost(url, stringUtil.trimStr(json));

        return res;
    }

    public String checkListEditEmptyPara(long id, String name, String desc, String title, String comment,String emptyPara) throws Exception {
        String url = "/patrol/check-list/edit";

        String json =
                "{\n" +
                        "    \"id\":\"" + id + "\",\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"desc\":\"" + desc + "\",\n" +
                        "    \"items\":[\n" +
                        "        {\n" +
                        "            \"order\":" + 0 + ",\n" +
                        "            \"title\":\"" + title + "\",\n" +
                        "            \"comment\":\"" + comment + "\"\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"shop_list\":[\n" + SHOP_ID + "    ]\n" +
                        "}";

        JSONObject temp = JSON.parseObject(json);

        if ("items-title".equals(emptyPara)){

            JSONArray items = temp.getJSONArray("items");

            JSONObject item = items.getJSONObject(0);
            item.put("title","");
            items.clear();
            items.add(0,item);

            temp.put("items",items);

            json = temp.toJSONString();

        }else if ("items".equals(emptyPara) || "shop_list".equals(emptyPara)){
            temp.put(emptyPara,null);
            json = temp.toJSONString();
        }else {
            temp.put(emptyPara,"");
            json = temp.toJSONString();
        }

        String res = httpPost(url, stringUtil.trimStr(json));

        checkCodes(res,"1001或1009","编辑执行清单," + emptyPara + "为空！");

        return res;
    }


//    #########################################################接口调用方法########################################################


//    #########################################################数据验证方法########################################################

    public String getCheckListCreateTime() throws Exception {
        JSONArray list = checkListPage(1, 1).getJSONArray("list");
        JSONObject newCheck = list.getJSONObject(0);
        String createTime = newCheck.getString("create_time");

        return createTime;
    }

    public long checkNewCheckList(String name, String desc) throws Exception {
        JSONArray list = checkListPage(1, 1).getJSONArray("list");
        JSONObject newCheck = list.getJSONObject(0);
        long id = newCheck.getLong("id");

        boolean isExist = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String nameRes = single.getString("name");
            if (name.equals(nameRes)){
                isExist = true;

                checkUtil.checkKeyValue("执行清单列表",newCheck,"name",name,true);
                checkUtil.checkKeyValue("执行清单列表",newCheck,"desc",desc,true);
            }
        }

        if (!isExist){
            throw new Exception("新建后，执行清单列表中不存在该清单，清单名称=" + name);
        }

        return id;
    }

    public long checkEditCheckList(long id, String name, String desc, String createTime) throws Exception {
        JSONArray list = checkListPage(1, 1).getJSONArray("list");

        boolean isExist = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);

            long idRes = single.getLongValue("id");

            if (id==idRes){
                isExist = true;

                checkUtil.checkKeyValue("执行清单列表",single,"name",name,true);
                checkUtil.checkKeyValue("执行清单列表",single,"desc",desc,true);
                checkUtil.checkKeyValue("执行清单列表",single,"create_time",createTime,true);
            }
        }

        if (!isExist){
            throw new Exception("编辑后，执行清单列表中不存在该清单，清单id=" + id + "，name =" +name);
        }

        return id;
    }

    public long checkCheckListNotExist(long id,String name) throws Exception {
        JSONArray list = checkListPage(1, 1).getJSONArray("list");

        boolean isExist = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            if (id ==single.getLongValue("id")){
                isExist = true;
            }
        }

        if (isExist){
            throw new Exception("删除执行清单后，列表中仍存在该清单，清单id=" + id + "，清单名称 = " + name);
        }

        return id;
    }

    public void checkCheckListDetail(long id, String name, String desc, String title, String comment) throws Exception {
        JSONObject detail = checkListDetail(id);

        checkUtil.checkKeyValue("执行清单详情",detail,"name",name,true);
        checkUtil.checkKeyValue("执行清单详情",detail,"desc",desc,true);

        JSONObject item = detail.getJSONArray("items").getJSONObject(0);
        checkUtil.checkKeyValue("执行清单详情",item,"title",title,true);
        checkUtil.checkKeyValue("执行清单详情",item,"comment",comment,true);
    }

    public long checkNewScheduleCheck(String name, String cycle, String dates, String validStart, String validEnd,
                                      String inspectorName) throws Exception {
        JSONArray list = checkListPage(1, 1).getJSONArray("list");
        JSONObject newSchedule = list.getJSONObject(0);
        long id = newSchedule.getLong("id");

        checkUtil.checkKeyValue("定检任务列表",newSchedule,"name",name,true);
        checkUtil.checkKeyValue("定检任务列表",newSchedule,"cycle",cycle,true);
        checkUtil.checkKeyValue("定检任务列表",newSchedule,"valid_start",validStart,true);
        checkUtil.checkKeyValue("定检任务列表",newSchedule,"valid_end",validEnd,true);
        checkUtil.checkKeyValue("定检任务列表",newSchedule,"inspector_name",inspectorName,true);

        String today = dateTimeUtil.timestampToDate("yyyy-MM-dd", System.currentTimeMillis());

        String status = "1";
        if (validStart.compareTo(today)>0 && validEnd.compareTo(today)<0){
            status = "0";
        }

        checkUtil.checkKeyValue("定检任务列表",newSchedule,"status",status,true);

        return id;
    }

    public void checkEditScheduleCheck(long id, String name, String cycle, String dates, String validStart, String validEnd,
                                      String inspectorName) throws Exception {
        JSONArray list = scheduleCheckList(1, 10).getJSONArray("list");

        boolean isExist = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            if (single.getLongValue("id")==id){

                isExist =true;

                checkUtil.checkKeyValue("定检任务列表",single,"name",name,true);
                checkUtil.checkKeyValue("定检任务列表",single,"cycle",cycle,true);
                checkUtil.checkKeyValue("定检任务列表",single,"valid_start",validStart,true);
                checkUtil.checkKeyValue("定检任务列表",single,"valid_end",validEnd,true);
                checkUtil.checkKeyValue("定检任务列表",single,"inspector_name",inspectorName,true);

                String today = dateTimeUtil.timestampToDate("yyyy-MM-dd", System.currentTimeMillis());

                String status = "1";
                if (validStart.compareTo(today)>0 && validEnd.compareTo(today)<0){
                    status = "0";
                }

                checkUtil.checkKeyValue("定检任务列表",single,"status",status,true);
            }
        }

        if (!isExist){
            throw new Exception("编辑后定检任务列表中不存在该任务，任务id=" + id + "，name=" + name);
        }
    }

    public void checkScheduleCheckNotExist(long id, String name) throws Exception {
        JSONArray list = checkListPage(1, 10).getJSONArray("list");

        boolean isExist = false;

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            if (single.getLongValue("id")==id){
                isExist =true;
            }
        }

        if (isExist){
            throw new Exception("删除后定检任务列表中仍存在该任务，任务id=" + id + "，name=" + name);
        }
    }


    public String httpPostWithCheckCode(String path, String json) throws Exception {
        response = httpPost(path, json);
        checkCode(response, StatusCode.SUCCESS, path);
        return response;
    }

    public String httpPost(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = IPPort + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    public String httpPostNoPrintPara(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = IPPort + path;
        config.url(queryUrl).json(json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        checkCode(response, StatusCode.SUCCESS, path);

        return response;
    }

    public void checkNotCode(String response, int expectNot, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            if (expectNot == code) {
                Assert.assertNotEquals(code, expectNot, message + resJo.getString("message"));
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    public void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            if (expect != code) {
                if (code != 1000) {
                    message += resJo.getString("message");
                }
                Assert.assertEquals(code, expect, message);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    public void checkCodes(String response, String expects, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            String code = resJo.getString("code");

            if (!expects.contains(code)) {
                if (!"1000".equals(code)) {
                    message += resJo.getString("message");
                }

                throw new Exception("期待statusCode：" + expects + "，实际返回=" + code + "," + message);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }

    public void checkMessage(String function, String response, String message) throws Exception {

        String messageRes = JSON.parseObject(response).getString("message");
        if (!message.equals(messageRes)) {
            throw new Exception(function + "，提示信息与期待不符，期待=" + message + "，实际=" + messageRes);
        }
    }

    public void login() {
        qaDbUtil.openConnection();
        qaDbUtil.openConnectionRdDaily();
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        logger.info("\n\n" + caseName + "\n");

        initHttpConfig();
        String path = "/patrol-login";
        String loginUrl = IPPort + path;
        String json = "{\"username\":\"yuexiu@test.com\",\"type\":\"0\",\"password\":\"f5b3e737510f31b88eb2d4b5d0cd2fb4\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            logger.info(response);
            authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
        } catch (Exception e) {
            aCase.setFailReason("http post 调用异常，url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, ciCaseName, caseName, failReason, "登录获取authentication");
    }

    public void clean() {
        qaDbUtil.closeConnection();
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal();
    }

    public void dingPushFinal() {
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

        FAIL = false;
    }

    public void saveData(Case aCase, String ciCaseName, String caseName, String failReason, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, failReason, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());

            failReason = aCase.getFailReason();

            String message = "巡店日常 \n" +
                    "验证：" + aCase.getCaseDescription() +
                    " \n\n" + failReason;

            dingPush(aCase, message);
        }
    }

    public void dingPush(Case aCase, String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());
    }

    public void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String failReason, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            if (failReason.contains("java.lang.Exception:")) {
                failReason = failReason.replace("java.lang.Exception", "异常");
            } else if (failReason.contains("java.lang.AssertionError")) {
                failReason = failReason.replace("java.lang.AssertionError", "异常");
            }
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    public void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "初始化http配置异常" + "\n" + e;
            return;
        }
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", SHOP_ID)
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

}
