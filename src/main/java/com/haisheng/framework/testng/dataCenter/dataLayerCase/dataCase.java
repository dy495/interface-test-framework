package com.haisheng.framework.testng.dataCenter.dataLayerCase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.dataCenter.interfaceUtil.dataLayerUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
;import static com.google.common.base.Preconditions.checkArgument;
/**
 * @author : qingqing
 * @date :  2020/07/06
 */
public class dataCase extends TestCaseCommon implements TestCaseStd {
    dataLayerUtil data = dataLayerUtil.getInstance();
    String request_id = "8b21f20d-6af6-43ff-8fd3-4251e959d28c";
    String shop_id = "43072";
    String scope = "22728";
    String trans_id = "20210107";
    String trans_time = "" + System.currentTimeMillis();
    String user_id = "11111";
    String memberName = "消费话";
    String openid = "";
    double total_price = 12;
    double real_price = 12;
    String receipt_type = "";
    String posId = "pos-1234586789";
    String orderNumber = "8888888";
    String face_path = "src/main/java/com/haisheng/framework/testng/dataCenter/img/china.png";
    String face_path1 = "src/main/java/com/haisheng/framework/testng/dataCenter/img/woman.jpg";



    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE;
        commonConfig.checklistQaOwner = "青青";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "data_center");
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "数据层接口 日常");
        commonConfig.dingHook = DingWebhook.APP_DATA_LAYER_ALARM_GRP;
        commonConfig.pushRd = new String[]{"18810332354", "15084928847"};
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);
        logger.debug("dataCenter " + data);
    }
    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }
    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }
    /**
     * ====================接收交易数据(正确入参&格式)======================
     */
    @Test
    public void recive() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            ArrayList<String> trans_type = new ArrayList<>();
            trans_type.add("W");
            JSONObject obj = new JSONObject();
            obj.put("commodity_id","iPhone12");
            obj.put("commodity_name","苹果手机12代");
            obj.put("num",5);
            obj.put("unit_price",123.12);
            JSONArray commodity_list = new JSONArray();
            commodity_list.add(obj);
            JSONObject  res = data.customer_dealData(shop_id,trans_id,trans_time,trans_type,user_id,total_price,real_price,openid,orderNumber,memberName,receipt_type,posId,commodity_list);
            Integer code = res.getInteger("code");
            String message = res.getString("message");
            System.out.println(code);
             checkArgument(code == 1000, "接收交易数据接口，正常格式入参报错,报错报文："+message);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("接收交易数据(正确入参&格式)");
        }

    }
    /**
     * ====================接收交易数据(必填项不填写)======================
     */
    @Test
    public void recive1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            ArrayList<String> trans_type = new ArrayList<>();
            trans_type.add("W");
            JSONObject obj = new JSONObject();
            obj.put("commodity_id","iPhone12");
            obj.put("commodity_name","苹果手机12代");
            obj.put("num",5);
            obj.put("unit_price",123.12);
            JSONArray commodity_list = new JSONArray();
            commodity_list.add(obj);
            JSONObject  res = data.customer_dealData("",trans_id,trans_time,trans_type,user_id,total_price,real_price,openid,orderNumber,memberName,receipt_type,posId,commodity_list);
            checkArgument(res == null, "接收交易数据接口，缺少必填项shop_id，接口仍然调成功");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接收交易数据(必填项不填写)");
        }

    }
    /**
     * ====================接收交易数据(必填项填写错误)======================
     */
    @Test
    public void recive2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            ArrayList<String> trans_type = new ArrayList<>();
            trans_type.add("W");
            JSONObject obj = new JSONObject();
            obj.put("commodity_id","iPhone12");
            obj.put("commodity_name","苹果手机12代");
            obj.put("num",5);
            obj.put("unit_price",123.12);
            JSONArray commodity_list = new JSONArray();
            commodity_list.add(obj);
            JSONObject  res = data.customer_dealData("",trans_id,"090909090",trans_type,user_id,total_price,real_price,openid,orderNumber,memberName,receipt_type,posId,commodity_list);
            checkArgument(res == null, "接收交易数据接口，必填项交易时间格式不正确，接口仍然调成功");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接收交易数据(必填项填写错误)");
        }
    }

    /**
     * ====================接收交易数据(非必填项填写错误)======================
     */
    @Test
    public void recive3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            ArrayList<String> trans_type = new ArrayList<>();
            trans_type.add("W");
            JSONObject obj = new JSONObject();
            obj.put("commodity_id","iPhone12");
            obj.put("commodity_name","苹果手机12代");
            obj.put("num",5);
            obj.put("unit_price",123.122);
            JSONArray commodity_list = new JSONArray();
            commodity_list.add(obj);
            JSONObject  res = data.customer_dealData("",trans_id,trans_time,trans_type,user_id,total_price,real_price,openid,orderNumber,memberName,receipt_type,posId,commodity_list);
            checkArgument(res == null, "接收交易数据接口，非必填项格式不正确，接口仍然调成功");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接收交易数据(必填项填写错误)");
        }
    }

//    /**
//     * ====================识别付款人物候选，上传图像(正确入参&格式)======================
//     */
//    @Test
//    public void recivePic() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//           // checkArgument(res == null, "接收交易数据接口，必填项交易时间格式不正确，接口仍然调成功");
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("识别付款人物候选，上传图像(正确入参&格式)");
//        }
//    }
    /**
     * ====================会员注册(正确入参&格式)======================
     */
    @Test
    public void member_register() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String app_id = "49998b971ea0";
            String face_url = new ImageUtil().getImageBinary(face_path1);
            JSONObject res = data.memberRegisted(request_id,app_id,scope,face_url,user_id,"tester",false,false,false,false,false,null);
             Integer code =res.getInteger("code");
             String  message = res.getString("message");
             String requestId = res.getString("request_id");
             checkArgument(code == 1000, "【会员管理】会员注册，正确格式传参报错,报错报文："+message+" 。【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员注册(正确入参&格式)");
        }
    }
    /**
     * ====================会员注册(人脸图片使用PNG格式)======================
     */
    @Test
    public void member_register1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String app_id = "49998b971ea0";
            String face_url = new ImageUtil().getImageBinary(face_path);
            JSONObject res = data.memberRegisted(request_id,app_id,scope,face_url,user_id,"tester",false,false,false,false,false,null);
            Integer code =res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code == 1000, "【会员管理】会员注册，人脸图片格式PNG，报错,报错报文："+message+" 。【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员注册(正确入参&格式)");
        }
    }
    /**
     * ====================会员注册(错误参数)======================
     */
    @Test
    public void member_register2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String app_id = "49998b971ea0";
            String face_url = new ImageUtil().getImageBinary(face_path);
            JSONObject res = data.memberRegisted(request_id,app_id,scope,face_url,user_id,"tester",false,false,false,false,false,null);
            Integer code =res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code == 1000, "【会员管理】会员注册，人脸图片格式PNG，报错,报错报文："+message+" 。【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员注册(正确入参&格式)");
        }
    }
}

