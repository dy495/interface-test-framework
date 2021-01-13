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
import com.haisheng.framework.util.CommonUtil;
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
    String request_id = "8b21f20d-6af6-43ff-8fd3-4251e9";
    String shop_id = "43072";
    String scope = "22728";
    String scope1 = "43072";
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
    String face_url = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=381876729,1649964117&fm=26&gp=0.jpg";
    String app_id = "88590052b177";
    String app_id1 = "49998b971ea0";
    




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
       // commonConfig.dingHook = DingWebhook.APP_DATA_LAYER_ALARM_GRP;
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
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
            JSONObject  res = data.customer_dealData("","","",trans_type,"",total_price,real_price,openid,orderNumber,memberName,receipt_type,posId,commodity_list);
            Integer code = res.getInteger("code");
            checkArgument(code == 1001, "接收交易数据接口，缺少必填项shop_id等参数，接口仍然调成功,code:"+code);
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
    //@Test
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
    //@Test
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
     * ====================会员注册(正确入参&格式),根据注册接口的参数，在查询接口进行查询是否新建成功======================
     */
    @Test
    public void member_register() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
             
             JSONObject res = data.memberRegisted(request_id+CommonUtil.getRandom(3),app_id,scope,face_url,user_id,"tester",false,false,false,false,false,null);
             Integer code =res.getInteger("code");
             String  message = res.getString("message");
             String requestId = res.getString("request_id");
             checkArgument(code == 1000, "【会员管理】会员注册，正确格式传参报错,报错报文："+message+" 。【requestId】："+requestId);
             //查询会员
             JSONObject res1 = data.qyery_userSearch(requestId,app_id,scope,"tester",false);
             JSONArray member = res1.getJSONArray("member");
             for(int i=0;i<member.size();i++){
                 String user_id1 =member.getJSONObject(i).getString("user_id");
                 checkArgument(user_id1.equals(user_id), "【根据身份tester进行查询，没有查询到user_id为："+user_id+"的会员在列表中");
             }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员注册(正确入参&格式)");
        }
    }
    /**
     * ====================会员注册(人脸图片使用非人脸)======================
     */
    @Test
    public void member_register1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            String face_url = "https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1042556063,3979976810&fm=26&gp=0.jpg";
            JSONObject res = data.memberRegisted(request_id+ CommonUtil.getRandom(3),app_id,scope,face_url,user_id,"tester",false,false,false,false,false,null);
            Integer code =res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code==3001, "【会员管理】会员注册，人脸地址为动物，接口调用成功："+message+" 。【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员注册(人脸图片使用非人脸)");
        }
    }

    /**
     * ====================会员注册(重复注册)======================
     */
    @Test
    public void member_register2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
                
                String face_url = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1340544313,4145608903&fm=26&gp=0.jpg";
                data.memberRegisted(request_id+ CommonUtil.getRandom(3),app_id,scope,face_url,user_id,"tester",false,false,false,false,false,null);
                JSONObject res = data.memberRegisted(request_id+ CommonUtil.getRandom(3),app_id,scope,face_url,user_id,"tester",false,false,false,false,false,null);
                Integer code =res.getInteger("code");
                String  message = res.getString("message");
                JSONObject same_member = res.getJSONObject("same_member");
                String requestId = res.getString("request_id");
                checkArgument(code == 1000 , "【会员管理】会员注册(重复注册)，接口调用失败："+message+" 。【requestId】："+requestId);
                checkArgument(same_member != null, "【会员管理】会员注册(重复注册)，接口没有返回注册重复时的响应值参数【same_member】"+same_member+" 。【requestId】："+requestId);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员注册(重复注册)");
        }
    }

    /**
     * ====================会员删除-删除人脸（正确参数）======================
     */
    @Test
    public void member_delete() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            String time = dt.getHHmm(0);
            JSONObject res = data.memberRegisted(request_id+ CommonUtil.getRandom(3),app_id,scope,face_url,user_id,"tester1",false,false,false,false,false,null);
            String face_id =res.getString("face_id");
            JSONObject res1 = data.deleteFace(request_id,app_id,scope,"tester1",user_id,face_id,false);
            Integer code = res1.getInteger("code");
            String  message = res1.getString("message");
            String requestId = res1.getString("request_id");
            checkArgument(code == 1000 , "【会员管理】删除人脸（正确参数）-接口调用失败："+message+" 。【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员删除-删除人脸（正确参数）");
        }
    }
    /**
     * ====================会员删除-删除人脸（错误参数之face_id错误）======================
     */
   // @Test
    public void member_delete1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            JSONObject res1 = data.deleteFace(request_id+ CommonUtil.getRandom(3),app_id,scope,"tester",user_id,"2423434234",false);
            Integer code = res1.getInteger("code");
            String  message = res1.getString("message");
            String requestId = res1.getString("request_id");
            checkArgument(code == 1001 , "【会员管理】删除人脸（错误参数之face_id错误）-接口调用成功："+message+" 。【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员删除-删除人脸（错误参数之face_id错误）");
        }
    }

    /**
     * ====================会员删除-删除人脸（必填项不传参）======================
     */
    @Test
    public void z_member_delete2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res1 = data.deleteFace(request_id+ CommonUtil.getRandom(3),"","","","","",false);
            Integer code = res1.getInteger("code");
            String  message = res1.getString("message");
            checkArgument(code == 1001 , "【会员管理】删除人脸（必填项空参）-报文："+message);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员删除-删除人脸（必填项不传参）");
        }
    }

    /**
     * ====================会员删除-删除人脸（必填项超出规定字符范围）======================
     */
    @Test
    public void z_member_delete3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            JSONObject res1 = data.deleteFace(request_id+ CommonUtil.getRandom(3),app_id,scope,"tester####￥￥￥%%","**&^%$#!$%^*^","2423434234",false);
            Integer code = res1.getInteger("code");
            String  message = res1.getString("message");
            String requestId = res1.getString("request_id");
            checkArgument(code == 1001 , "【会员管理】删除人脸（人物身份&user_id输入%￥#@%……&格式）-接口调用成功："+message+" 。【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员删除-删除人脸（人物身份&user_id输入%￥#@%……&格式）");
        }
    }
    /**
     * ====================会员删除-删除人脸（删除跨店下的人脸，但门店非跨店门店）======================
     */
    @Test
    public void z_member_delete4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            JSONObject res = data.memberRegisted(request_id+ CommonUtil.getRandom(3),app_id,scope,face_url,user_id,"tester",false,false,false,false,false,null);
            String face_id =res.getString("face_id");
            JSONObject res1 = data.deleteFace(request_id,app_id,scope,"tester",user_id,face_id,true);
            Integer code = res1.getInteger("code");
            String  message = res1.getString("message");
            String requestId = res1.getString("request_id");
            checkArgument(code == 1001 , "【会员管理】删除人脸（删除跨店下的人脸，但门店非跨店门店）-接口调用成功："+message+" 。【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员删除-删除人脸（删除跨店下的人脸，但门店非跨店门店）");
        }
    }

    /**
     * ====================会员删除-删除会员（正确参数）======================
     */
    @Test
    public void z_member_delete5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            data.memberRegisted(request_id+ CommonUtil.getRandom(3),app_id,scope,face_url,user_id+"1010","tester3",false,false,false,false,false,null);
            JSONObject res1 = data.deleteUser(request_id+ CommonUtil.getRandom(3),app_id,scope,"tester3",user_id+"1010",false);
            Integer code = res1.getInteger("code");
            String  message = res1.getString("message");
            String requestId = res1.getString("request_id");
            checkArgument(code == 1000 , "【会员管理】删除会员（正确参数）-接口调用失败："+message+" 。【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员删除-删除会员（正确参数）");
        }
    }


    /**
     * ====================会员删除-删除会员（错误参数）======================
     */
    @Test
    public void z_member_delete6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            data.memberRegisted(request_id+ CommonUtil.getRandom(3),app_id,scope,face_url,user_id,"tester08",false,false,false,false,false,null);
            JSONObject res1 = data.deleteUser(request_id,app_id,scope,"tester%%^^&*((",user_id,false);
            Integer code = res1.getInteger("code");
            checkArgument(code == 1001 , "【会员管理】删除会员（错误参数，自定义身份参数超过字符范围）-接口调用成功");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员删除-删除会员（错误参数）");
        }
    }

    /**
     * ====================会员删除-删除身份组（正确参数）======================
     */
    @Test
    public void z_member_delete7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            data.memberRegisted(request_id+ CommonUtil.getRandom(3),app_id,scope,face_url,user_id,"tester09",false,false,false,false,false,null);
            JSONObject res = data.deleteClear_identify(request_id,app_id,scope,"tester09",false);
            Integer code = res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code == 1000 , "【会员管理】删除身份组（正确传参）-接口调用失败，报错报文："+message+"【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员删除-删除身份组（正确参数）");
        }
    }
    /**
     * ====================会员删除-删除身份组（必填项不填写）======================
     */
    @Test
    public void z_member_delete8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = data.deleteClear_identify(request_id+ CommonUtil.getRandom(3),"","","",false);
            Integer code = res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code == 1001 , "【会员管理】删除身份组(必填项不填写)-接口调用成功,报错报文："+message+"【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员删除-删除身份组（正确参数）");
        }
    }

    /**
     * ====================会员查询-查询身份下的会员（正确传参）======================
     */
    @Test
    public void member_search() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = data.qyery_userSearch(request_id+ CommonUtil.getRandom(3),app_id,scope,"tester3",false);
            Integer code = res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code == 1000 , "【会员管理】查询身份下的会员(正确传参)-接口调用失败,报错报文："+message+"【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员查询-查询身份下的会员（正确传参）");
        }
    }

    /**
     * ====================会员查询-查询身份下的会员（必填项不填写）======================
     */
    @Test
    public void member_search1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = data.qyery_userSearch(request_id+ CommonUtil.getRandom(3),"","","",false);
            Integer code = res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code == 1001 , "【会员管理】删除身份组(必填项不填写)-接口调用成功,报文："+message+"【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员查询-查询身份下的会员（必填项不填写）");
        }
    }
    /**
     * ====================会员查询-查询身份列表（正常入参）======================
     */
    @Test
    public void identify_search() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            JSONObject res = data.qyery_identifySearch(request_id+ CommonUtil.getRandom(3),app_id,scope,false);
            Integer code = res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code == 1000 , "【会员管理】删除身份组(正常入参)-接口调用失败,报错报文："+message+"【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员查询-查询身份下的会员（正常入参）");
        }
    }

    /**
     * ====================会员查询-查询身份列表（必填项不填写）======================
     */
    @Test
    public void identify_search1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = data.qyery_identifySearch(request_id+ CommonUtil.getRandom(3),"","",false);
            Integer code = res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code == 1001 , "【会员管理】删除身份组(必填项不填写)-接口调用成功,报文："+message+"【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员查询-查询身份下的会员（必填项不填写）");
        }
    }

    /**
     * ====================会员查询-会员检索（正常入参）======================
     */
    @Test
    public void face_search() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            JSONObject res = data.identify_search(request_id+ CommonUtil.getRandom(3),app_id,scope,face_url,false,false,false);
            Integer code = res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            JSONArray search_result= res.getJSONArray("search_result");
            checkArgument(code == 1000 , "【会员管理】会员检索(正常入参)-接口调用失败,报错报文："+message+"【requestId】："+requestId);
            checkArgument(!search_result.isEmpty() , "存在的会员人脸通过会员检索无结果,报错报文："+message+"【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员查询-会员检索（正常入参）");
        }
    }
    /**
     * ====================会员查询-会员检索（必填项不填写）======================
     */
    @Test
    public void face_search1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = data.identify_search(request_id+ CommonUtil.getRandom(3),"","","",false,false,false);
            Integer code = res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code == 1001 , "【会员管理】会员检索(必填项不填写)-接口调用成功,报文："+message+"【requestId】："+requestId);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【会员管理】会员查询-会员检索（必填项不填写）");
        }
    }

    /**
     * ====================添加身份配置（正确入参）======================
     */
    @Test
    public void add_identify() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            JSONObject res = data.add_identify(request_id+ CommonUtil.getRandom(3),app_id,scope,"QQtest",false,false);
            Integer code = res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code == 1000 , "添加身份配置(正确入参)-接口调用失败,报错报文："+message+"【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加身份配置（正确入参）");
        }
    }
    /**
     * ====================添加身份配置（必填项不填写）======================
     */
    @Test
    public void add_identify1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            JSONObject res = data.add_identify(request_id+ CommonUtil.getRandom(3),"","","*******",false,false);
            Integer code = res.getInteger("code");
            String  message = res.getString("message");
            String requestId = res.getString("request_id");
            checkArgument(code == 1001 , "添加身份配置(必填项不填写)-接口调用成功,报文："+message+"【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加身份配置（必填项不填写）");
        }
    }

    /**
     * ====================实时热力图（正确传参）======================
     */
    @Test
    public void map_real() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            
            Date time = DateTimeUtil.addSecond(new Date(), -3 * 60 * 60);
            String s = DateTimeUtil.getFormat(time, "yyyy-MM-dd HH:mm:ss");
            Long start_time =Long.parseLong(DateTimeUtil.dateToStamp(s)) ;
            System.err.println(start_time);
            Long end_time = System.currentTimeMillis();
            JSONObject datas = new JSONObject();
            datas.put("shop_id",shop_id);
            datas.put("region_id","46997");
            datas.put("start_time",start_time);
            datas.put("end_time",end_time);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");

            JSONObject res = data.thermal_map(request_id+ CommonUtil.getRandom(4),"QUERY_THERMAL_MAP",datas,system);
            Integer code = res.getInteger("code");
            JSONArray thermal_map = res.getJSONArray("thermal_map");
            //checkArgument(!thermal_map.isEmpty() && code ==1000, "实时热力图接口（正确传参【当前时间】往后推3小时的热力图数据为空）,code值为："+code+"【requestId】："+requestId);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时热力图（正确传参）");
        }
    }

    /**
     * ====================实时热力图（必填项不填写）======================
     */
    @Test
    public void map_real1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("shop_id","");
            datas.put("region_id","");
            datas.put("start_time",null);
            datas.put("end_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.thermal_map(request_id+ CommonUtil.getRandom(4),"",datas,system);
            Integer code = res.getInteger("code");
            checkArgument(code==1001 , "实时热力图接口（必填项不填写）+接口报错code不正确,code:"+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时热力图（必填项不填写）");
        }
    }
    /**
     * ====================当日统计查询（正确传参）======================
     */
    @Test
    public void day_data() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("shop_id",shop_id);
            datas.put("region_id","46997");

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.customer_currData(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS",datas,system);
            Integer code = res.getInteger("code");
            JSONArray statistics = res.getJSONArray("statistics");
            checkArgument(!statistics.isEmpty()&& code==1000 , "当日统计查询接口（正确传参）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日统计查询（正确传参）");
        }
    }
    /**
     * ====================当日统计查询（必填项不填写）======================
     */
    @Test
    public void day_data1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("shop_id","");
            datas.put("region_id","");

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.customer_currData(request_id+ CommonUtil.getRandom(4),"",datas,system);
            Integer code = res.getInteger("code");
            checkArgument(code==1001 , "当日统计查询接口（必填项不填写）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日统计查询（必填项不填写）");
        }
    }
    /**
     * ====================当日商铺PV/UV统计（正确传参）======================
     */
    @Test
    public void day_Puv() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope",scope1);
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.curr_shopPv_Uv(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_PUV_DAY",datas,system);
            JSONArray statistics = res.getJSONArray("statistics");
            Integer code = res.getInteger("code");
            checkArgument(code==1000 && !statistics.isEmpty() , "当日商铺PV/UV统计（正确传参）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺PV/UV统计（正确传参）");
        }
    }
    /**
     * ====================当日商铺PV/UV统计（必填项不填写）======================
     */
    @Test
    public void day_Puv1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope","");
            datas.put("region_id","");

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.curr_shopPv_Uv(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_PUV_DAY",datas,system);
            Integer code = res.getInteger("code");
            checkArgument(code==1001 , "当日商铺PV/UV统计（必填项不填写）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺PV/UV统计（必填项不填写）");
        }
    }

    /**
     * ====================当当日商铺PV/UV小时统计（正确传参）======================
     */
    @Test
    public void day_PuvHour() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope",scope1);
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.curr_shopHourPv_Uv(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_PUV_HOUR",datas,system);
            JSONArray statistics = res.getJSONArray("statistics");
            Integer code = res.getInteger("code");
            checkArgument(code==1000 && !statistics.isEmpty() , "当日商铺PV/UV小时统计（正确传参）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺PV/UV小时统计（正确传参）");
        }
    }
    /**
     * ====================当日商铺PV/UV小时统计（必填项不填写）======================
     */
    @Test
    public void day_PuvHour1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope","");
            datas.put("region_id","");

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.curr_shopHourPv_Uv(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_PUV_HOUR",datas,system);
            Integer code = res.getInteger("code");
            checkArgument(code==1001 , "当当日商铺PV/UV小时统计（必填项不填写）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当当日商铺PV/UV小时统计（必填项不填写）");
        }
    }

    /**
     * ====================当当日商铺男女统计（正确传参）======================
     */
    @Test
    public void day_Puv_sex() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope",scope1);
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.curr_shopSex(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_SEX_DAY",datas,system);
            JSONArray statistics = res.getJSONArray("statistics");
            Integer code = res.getInteger("code");
            checkArgument(code==1000 && !statistics.isEmpty() , "当日商铺男女统计（正确传参）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺男女统计（正确传参）");
        }
    }
    /**
     * ====================当日商铺男女统计（必填项不填写）======================
     */
    @Test
    public void day_Puv_sex1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope","");
            datas.put("region_id","");

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.curr_shopSex(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_SEX_DAY",datas,system);
            Integer code = res.getInteger("code");
            checkArgument(code==1001 , "当日商铺男女统计（必填项不填写）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺男女统计（必填项不填写）");
        }
    }

    /**
     * ====================当日商铺年龄统计（正确传参）======================
     */
    @Test
    public void day_Puv_age() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope",scope1);
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.curr_shopSex(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_AGE_DAY",datas,system);
            JSONArray statistics = res.getJSONArray("statistics");
            Integer code = res.getInteger("code");
            checkArgument(code==1000 && !statistics.isEmpty() , "当日商铺年龄统计（正确传参）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺年龄统计（正确传参）");
        }
    }
    /**
     * ====================当日商铺年龄统计（必填项不填写）======================
     */
    @Test
    public void day_Puv_age1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope","");
            datas.put("region_id","");

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");

            JSONObject res = data.curr_shopSex(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_AGE_DAY",datas,system);
            Integer code = res.getInteger("code");
            checkArgument(code==1001 , "当日商铺年龄统计（必填项不填写）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺年龄统计（必填项不填写）");
        }
    }

    /**
     * ====================当日商铺内区域PV/UV统计（正确传参）======================
     */
    @Test
    public void day_Puv_region() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope",scope1);
            datas.put("region_id","46997");
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.curr_shopRegin_PvUv(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_REGION_PUV_DAY",datas,system);
            JSONArray statistics = res.getJSONArray("statistics");
            Integer code = res.getInteger("code");
            checkArgument(code==1000  , "当日商铺内区域PV/UV统计（正确传参）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺内区域PV/UV统计（正确传参）");
        }
    }
    /**
     * ====================当日商铺内区域PV/UV统计（必填项不填写）======================
     */
    @Test
    public void day_Puv_region1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope","");
            datas.put("region_id","");
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");

            JSONObject res = data.curr_shopRegin_PvUv(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_REGION_PUV_DAY",datas,system);
            Integer code = res.getInteger("code");
            checkArgument(code==1001 , "当日商铺内区域PV/UV统计（必填项不填写）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺内区域PV/UV统计（必填项不填写）");
        }
    }

    /**
     * ====================当当日商铺内区域男女统计（正确传参）======================
     */
    @Test
    public void day_Puv_regionSex() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope",scope1);
            datas.put("region_id","46997");
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.curr_shopRegin_sex(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_REGION_SEX_DAY",datas,system);
            JSONArray statistics = res.getJSONArray("statistics");
            Integer code = res.getInteger("code");
            checkArgument(code==1000  , "当日商铺内区域男女统计（正确传参）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺内区域男女统计（正确传参）");
        }
    }
    /**
     * ====================当日商铺内区域男女统计（必填项不填写）======================
     */
    @Test
    public void day_Puv_regionSex1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope","");
            datas.put("region_id","");
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");

            JSONObject res = data.curr_shopRegin_sex(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_REGION_SEX_DAY",datas,system);
            Integer code = res.getInteger("code");
            checkArgument(code==1001 , "当日商铺内区域男女统计（必填项不填写）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺内区域男女统计（必填项不填写）");
        }
    }
    /**
     * ====================当日商铺内区域年龄统计（正确传参）======================
     */
    @Test
    public void day_Puv_regionAge() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope",scope1);
            datas.put("region_id","46997");
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.curr_shopRegin_age(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_REGION_AGE_DAY",datas,system);
            //JSONArray statistics = res.getJSONArray("statistics");
            Integer code = res.getInteger("code");
            checkArgument(code==1000  , "当日商铺内区域年龄统计（正确传参）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺内区域年龄统计（正确传参）");
        }
    }
    /**
     * ====================当日商铺内区域年龄统计（必填项不填写）======================
     */
    @Test
    public void day_Puv_regionAge1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope","");
            datas.put("entrance_id","");
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");

            JSONObject res = data.curr_shopRegin_age(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_REGION_AGE_DAY",datas,system);
            Integer code = res.getInteger("code");
            checkArgument(code==1001 , "当日商铺内区域年龄统计（必填项不填写）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺内区域年龄统计（必填项不填写）");
        }
    }
    /**
     * ====================当日商铺内进出口PV/UV统计（正确传参）======================
     */
    @Test
    public void day_Puv_regionEnter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope",scope1);
            datas.put("entrance_id","43110");
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");
            JSONObject res = data.curr_shopRegin_age(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_ENTRANCE_PUV_DAY",datas,system);
            JSONArray statistics = res.getJSONArray("statistics");
            Integer code = res.getInteger("code");
            checkArgument(code==1000  , "当日商铺内进出口PV/UV统计（正确传参）,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺内进出口PV/UV统计（正确传参）");
        }
    }
    /**
     * ====================当日商铺内进出口PV/UV统计（必填项不填写）======================
     */
    @Test
    public void day_Puv_regionEnter1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject datas = new JSONObject();
            datas.put("scope","");
            datas.put("entrance_id","");
            datas.put("statistics_time",null);

            JSONObject system = new JSONObject();
            system.put("app_id",app_id1);
            system.put("source","postMan");

            JSONObject res = data.curr_shopRegin_age(request_id+ CommonUtil.getRandom(4),"QUERY_CURRENT_CUSTOMER_STATISTICS_ENTRANCE_PUV_DAY",datas,system);
            Integer code = res.getInteger("code");
            checkArgument(code==1001 , "当日商铺内进出口PV/UV统计,code为："+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当日商铺内进出口PV/UV统计（必填项不填写）");
        }
    }


    /**
     * ====================历史设备出现人物======================
     */
    @Test
    public void find() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long trans_time =  System.currentTimeMillis();
            JSONObject datas = new JSONObject();
            datas.put("date","2021-01-11");
            datas.put("query_device_id","8145390754759680");
            datas.put("start_time_min",trans_time-15*60*1000);
            datas.put("start_time_max",trans_time);
            datas.put("query_has_user",null);
            datas.put("query_has_face",null);
            datas.put("query_has_body",null);
            datas.put("scope","43072");
            JSONObject system = new JSONObject();
            system.put("app_id","49998b971ea0");
            system.put("source","postMan");
            JSONObject res = data.date_device_customer("test-local-QUERY_SCOPE_DATE_DEVICE_CUSTOMER-20201229-0002","QUERY_SCOPE_DATE_DEVICE_CUSTOMER",datas,system);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("历史设备出现人物");
        }
    }
}

