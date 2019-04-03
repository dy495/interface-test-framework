package com.haisheng.framework.testng.custemorGateTest;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.exception.SdkClientException;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.testng.CommonDataStructure.PvInfo;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.StatusCode;
import org.apache.ibatis.annotations.Delete;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.UUID;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class ApiScenarioTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    private String UID            = "uid_e0d1ebec";
    private String APP_ID         = "a4d4d18741a8";
    private String SHOP_ID        = "134";
    private String RE_ID          = "144";
    private String DEVICE_ID      = "6254834559910912";
    private BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    private BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    private String vipGroup = "vipGroup";
    private String vipUser = "00000";

    //特殊人物注册
    @Test (dataProvider = "CASE_USER_ID")
    public void registerFaceMultiUser(String userId) throws Exception{
        String picPath = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{\"group_name\":\"" + vipGroup +"\"," +
                "\"user_id\":\""+userId+"\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"pic_url\":\"@0\"" +
                "}";
        apiCustomerRequest(router,  resource, json);
    }

    @Test (dataProvider = "BAD_GRP_NAME_REG")
    public void registerFaceTestGroupName(String grpName) throws Exception{
        String picPath = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{\"group_name\":\"" + grpName +"\"," +
                "\"user_id\":\""+vipUser+"\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"pic_url\":\"@0\"" +
                "}";
        apiCustomerRequest(router,  resource, json);
    }

//特殊人脸注册，给一个人注册多张图片
    @Test(dataProvider = "CASE_PIC")
    public void registerFaceMultiPic(String picPath) throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{\"group_name\":\"" + vipGroup +"\"," +
                "\"user_id\":\""+vipUser+"\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"pic_url\":\"@0\"" +
                "}";
        apiCustomerRequest(router,  resource, json);
    }

/*
给一个人注册注册一张图片
*/
   @Test(dataProvider = "CASE_USER_ID")
   public void registerFaceSingle(String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
       String picPath = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{\"group_name\":\"" + vipGroup +"\"," +
                "\"user_id\":\""+userId+"\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"pic_url\":\"@0\"" +
                "}";
        apiCustomerRequest(router,  resource, json);
    }

    @Test(dataProvider = "CASE_USER_ID")
    public void registerFaceSingleCompare(String userId,String picPath) throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{\"group_name\":\"" + vipGroup +"\"," +
                "\"user_id\":\""+userId+"\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"pic_url\":\"@0\"" +
                "}";
        apiCustomerRequest(router,  resource, json);
    }

    public void registerFaceSingleNewGroup(String userId,String grpName, String picPath) throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{\"group_name\":\"" + grpName +"\"," +
                "\"user_id\":\""+userId+"\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"pic_url\":\"@0\"" +
                "}";
        apiCustomerRequest(router,  resource, json);
    }

    //特殊人物注册，测试非必填参数
    @Test
    public void registerFaceTestNotRequiredPara() throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String picPath = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+vipUser+"\"," +
                "\"pic_url\":\"@0\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"is_after_detect\":\"true\"," +
                "\"shop_user\":{" +
                "\"134\":\"79d528090d67a7944d352bcc913ea580\"" +
                "}" +
                "}";
        apiCustomerRequest(router,  resource, json);
    }

//测试用新group注册后首次特殊人物查询是否报3006
@Test
public void TestFirstQueryUserWithNewGroup () throws Exception{
        String newGroup = String.valueOf(System.currentTimeMillis());
        String faceId1 = "6331ec2742d22680ba5161643d149dbe";

    String msg;
    //1、先注册一张人脸图片
    registerFaceTestGroupName(newGroup);
    //2、首次用“特殊人物查询”查询该人信息
    queryUserWithFaceIdAndGRpName(vipUser,newGroup);
}

    //测试用新group注册后首次特殊人物组查询是否报3006
    @Test
    public void TestFirstQuerygroupWithNewGroup () throws Exception{
        String newGroup = String.valueOf(System.currentTimeMillis());

        String msg;
        //1、先注册一张人脸图片
        registerFaceTestGroupName(newGroup);
        //2、首次用“特殊人物查询”查询该人信息
        queryGroupTestNewGroupName(newGroup);
    }

    //测试用新group注册后首次特殊人脸查询是否报3006
    //--------------------------------------待完成---------------------
    @Test
    public void SearchFaceWithNewGroup () throws Exception{
        String newGroup = String.valueOf(System.currentTimeMillis());
        String picPath = "src/main/resources/test-res-repo/customer-gateway/NewGroup.jpg";
        String userId = "2012";

        String msg;
        //1、先注册一张人脸图片
        registerFaceSingleNewGroup(userId,newGroup,picPath);

        //2、首次用“特殊人脸查询”查询该人信息
        searchFaceWithNewGroup(newGroup,picPath);
    }

//特殊人物查询，没有返回值
    @Test(dataProvider = "CASE_USER_ID")
    public void queryUserNormal(String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        apiCustomerRequest(router, resource, json);
    }

//特殊人物查询，返回该人的faceid的合集
    public StringBuffer queryUserWithFaceId(String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        return apiCustomerRequestWithFaceId(router, resource, json);
    }

    //特殊人物查询，返回该人的faceid的合集
    public String queryUserWithFaceURL(String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        return apiCustomerRequestWithFaceURL(router, resource, json);
    }

    //特殊人物查询，返回该人的faceid的合集

    public void queryUserWithFaceIdAndGRpName(String userId, String grpName) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        apiCustomerRequestWithNewGroup(router, resource, json);
    }

    //特殊人物查询，返回该人上传的图片的数量
    public int queryUserWithResult(String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        return apiCustomerRequestWithResult(router, resource, json);
    }
//特殊人物删除，根据userId删除特定人物
    public void deleteUserWithUserId(String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        apiCustomerRequest(router, resource, json);
    }
//特殊人物组查询，测试UID,包括UID为空的，throw不同的exception
    @Test(dataProvider = "CASE_UID")
    public void TestUID(String UIDCase) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"" +
                "}";
        apiCustomerRequestTestUID(router, resource, json, UIDCase);
    }

    //特殊人物组查询，测试异常的appid
    @Test(dataProvider = "CASE_APPID")
    public void TestAppid(String AppidCase) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"" +
                "}";
        apiCustomerRequestTestAppid(router, resource, json, AppidCase);
    }

    //特殊人物组查询，测试异常的appid
    @Test(dataProvider = "CASE_VERSION")
    public void TestVersion(String versionCase) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"" +
                "}";
        apiCustomerRequestTestVersion(router, resource, json, versionCase);
    }

    //特殊人物组查询，测试组名
    @Test(dataProvider = "BAD_GRP_NAME_QUERY")
    public void queryGroupTestGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"" +
                "}";
        apiCustomerRequestTestGrpName(router, resource, json);
    }

    //特殊人物组查询，测试组名
    public void queryGroupTestNewGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"" +
                "}";
        apiCustomerRequestTestNewGrpName(router, resource, json);
    }

    //特殊人物查询，测试userid
    @Test (dataProvider = "CASE_BAD_USER_ID")
    public void queryUserTestBadUserId(String badUserId) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+badUserId+"\"" +
                "}";
        apiCustomerRequestTestUserId(router, resource, json);
    }

    //特殊人物查询，测试组名
    @Test(dataProvider = "BAD_GRP_NAME_QUERY")
    public void queryUserTestGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+vipUser+"\"" +
                "}";
        apiCustomerRequestTestGrpName(router, resource, json);
    }

    //特殊人物删除，测试无效userid
    @Test (dataProvider = "CASE_BAD_USER_ID")
    public void deleteUserTestBadUserId(String badUserId) throws Exception{
        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+badUserId+"\"" +
                "}";
        apiCustomerRequestTestUserId(router, resource, json);
    }

    //特殊人物删除，测试组名
    @Test(dataProvider = "BAD_GRP_NAME_QUERY")
    public void deleteUserTestGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+vipUser+"\"" +
                "}";
        apiCustomerRequestTestGrpName(router, resource, json);
    }

//特护人物删除，测试正常删除是否成功
    @Test
    public void deleteUserTestIsSuccess() throws Exception{
        String msg = "";
        String userId = "04021400";
        registerFaceSingle(userId);
        int beforeDelete = queryUserWithResult(userId);
        deleteUserWithUserId(userId);
        int afterDelete = queryUserWithResult(userId);
        if(beforeDelete>0&&afterDelete==0){
            msg = "“特定用户删除”操作成功！";
            logger.info(msg);
        }else{
            msg = "“特定用户删除”操作失败！";
            throw new Exception(msg);
        }

    }

    //特殊用户删除，测试删除以后是否能重新注册该人
    @Test
    public void deleteUserTestReAdd() throws Exception{
        String msg = "";
        String userId = "04021400";
        deleteUserWithUserId(userId);
        int beforeDelete = queryUserWithResult(userId);
        registerFaceSingle(userId);
        int afterDelete = queryUserWithResult(userId);
        if(beforeDelete==0&&afterDelete>0){
            msg = "用“特定人物删除”功能删除该用户后，重新注册该用户成功！";
            logger.info(msg);
        }else{
            msg = "用“特定人物删除”功能删除用户后，不能重新注册该用户！"
                  + "group: " + vipGroup
                  + "userid: " + userId;
            throw new Exception(msg);
        }
    }

    //特殊人脸删除，测试组名
    @Test(dataProvider = "BAD_GRP_NAME_QUERY")
    public void deleteFaceTestGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_DELETE_FACE/v1.0";
        String[] resource = new String[]{};
        String faceId = "ee4a2829872770e8e1ee8e1f82e3324a";
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+vipUser+"\"," +
                "\"face_id\":\""+faceId+"\"" +
                "}";
        apiCustomerRequestTestGrpName(router, resource, json);
    }

//特殊人脸删除，目前没有使用
    public void deleteFace() throws Exception{
        String router = "/scenario/gate/SYSTEM_DELETE_FACE/v1.0";
        String[] resource = new String[]{};
        String faceId = "ee4a2829872770e8e1ee8e1f82e3324a";
        String json = "{" +
                "\"group_name\":\"TestGroup\"," +
                "\"user_id\":\""+vipUser+"\"," +
                "\"face_id\":\""+faceId+"\"" +
                "}";
        apiCustomerRequest(router, resource, json);
    }

    //特殊人脸删除，正常的删除测试
@Test(dataProvider = "CASE_FACE_ID")
    public void deleteFace(String faceId) throws Exception{
        String router = "/scenario/gate/SYSTEM_DELETE_FACE/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+vipUser+"\"," +
                "\"face_id\":\""+faceId+"\"" +
                "}";
        apiCustomerRequest(router, resource, json);
    }

    //特殊人脸删除，测试删除以后是否可以重新注册该人。（注册-查询-删除-查询-注册-查询）
    @Test
    public void deleteFaceTestReAdd () throws Exception{

        String [] picPath = {
                "src/main/resources/test-res-repo/customer-gateway/1.jpg",
                "src/main/resources/test-res-repo/customer-gateway/2.jpg",
                "src/main/resources/test-res-repo/customer-gateway/3.jpg",
                "src/main/resources/test-res-repo/customer-gateway/4.jpg",
                "src/main/resources/test-res-repo/customer-gateway/5.jpg"
        };

        String [] faceIdArray = {
                "6331ec2742d22680ba5161643d149dbe",
                "6fdb50aa3d88f30fea6cdc90145a2e47",
                "707fbcdffba8669f9f7b9aa34a51af79",
                "79d528090d67a7944d352bcc913ea581",
                "ed70b52582299c5d9d7eda65efce9a2a"
        };

        int faceArrLen = faceIdArray.length;
        StringBuffer beforeDelete = null;
        String msg;
        //1、先增加，一次增加五个
        for(int i = 0;i<picPath.length;i++){
            registerFaceMultiPic(picPath[i]);
        }
        //2、查询删除后的faceID数据
        beforeDelete = queryUserWithFaceId(vipUser);
        int index = 0;
        for(index = 0;index<faceArrLen;index++){
            String compareFaceId = beforeDelete.toString();
            if(compareFaceId.contains(faceIdArray[index])){
                continue;
            }
        }

        if(index==faceArrLen){
            msg = "successful";
        }else{
            msg = "特定人脸查询失败！";
            throw new Exception(msg);
        }

        //3、特定人脸删除
        for(int i = 0;i<faceArrLen;i++){
            deleteFace(faceIdArray[i]);
        }
        //4、查询删除后的数据
        StringBuffer afterDelete =  queryUserWithFaceId(vipUser);
        if(!("".equals(afterDelete)||afterDelete!=null)){
            msg = "特定人脸删除后，仍能查询到该人的人脸信息，故特定人脸删除失败！";
            throw new Exception(msg);
        }

        //5、再次注册
        for(int i = 0;i<picPath.length;i++){
            registerFaceMultiPic(picPath[i]);
        }

        //6、注册后查询
        StringBuffer reReg =  queryUserWithFaceId(vipUser);
        for(index = 0;index<faceArrLen;index++){
            String compareFaceId = reReg.toString();
            if(compareFaceId.contains(faceIdArray[index])){
                continue;
            }
        }
        if(index==faceArrLen){
            msg = "successful";
            logger.info(msg);
        }else{
            msg = "特定人脸删除并再次注册该人后，特定人脸查询失败！";
            throw new Exception(msg);
        }
    }

//特殊人脸删除，测试是否可以删除两次(注册-查询-删除-查询-删除-查询)
    @Test
    public void TwiceDelete() throws Exception{
        String [] picPath = {
                "src/main/resources/test-res-repo/customer-gateway/1.jpg",
                "src/main/resources/test-res-repo/customer-gateway/2.jpg",
                "src/main/resources/test-res-repo/customer-gateway/3.jpg",
                "src/main/resources/test-res-repo/customer-gateway/4.jpg",
                "src/main/resources/test-res-repo/customer-gateway/5.jpg"
        };

        String [] faceIdArray = {
                "6331ec2742d22680ba5161643d149dbe",
                "6fdb50aa3d88f30fea6cdc90145a2e47",
                "707fbcdffba8669f9f7b9aa34a51af79",
                "79d528090d67a7944d352bcc913ea581",
                "ed70b52582299c5d9d7eda65efce9a2a"
        };

        int faceArrLen = faceIdArray.length;
        StringBuffer beforeDelete = null;
        String msg;
        //1、先增加，一次增加五个
        for(int i = 0;i<picPath.length;i++){
            registerFaceMultiPic(picPath[i]);
        }
        //2、查询删除后的faceID数据
        beforeDelete = queryUserWithFaceId(vipUser);
        int index = 0;
        for(index = 0;index<faceArrLen;index++){
            String compareFaceId = beforeDelete.toString();
            if(compareFaceId.contains(faceIdArray[index])){
                continue;
            }
        }

        if(index==faceArrLen){
            msg = "successful";
        }else{
            msg = "特定人脸查询失败！";
            throw new Exception(msg);
        }

        //3、特定人脸删除
        for(int i = 0;i<faceArrLen;i++){
            deleteFace(faceIdArray[i]);
        }
        //4、查询删除后的数据
        StringBuffer afterDelete =  queryUserWithFaceId(vipUser);
        if(!("".equals(afterDelete)||afterDelete!=null)){
            msg = "特定人脸删除后，仍能查询到该人的人脸信息，故特定人脸删除失败！";
            throw new Exception(msg);
        }

        //5、特定人脸删除
        for(int i = 0;i<faceArrLen;i++){
            deleteFace(faceIdArray[i]);
        }

        //6、查询删除后的数据
        StringBuffer afterReDelete =  queryUserWithFaceId(vipUser);
        if(!("".equals(afterReDelete)||afterReDelete!=null)){
            msg = "第二次特定人脸删除后，仍能查询到该人的人脸信息，故再次特定人脸删除失败！";
            throw new Exception(msg);
        }
    }

    //删除某一张图片再查询(注册-查询-删除一张-查询)
    @Test
    public void deleteFaceTestdeleteOne () throws Exception{

        String [] picPath = {
                "src/main/resources/test-res-repo/customer-gateway/1.jpg",
                "src/main/resources/test-res-repo/customer-gateway/2.jpg",
                "src/main/resources/test-res-repo/customer-gateway/3.jpg",
                "src/main/resources/test-res-repo/customer-gateway/4.jpg",
                "src/main/resources/test-res-repo/customer-gateway/5.jpg"
        };

        String [] faceIdArray = {
                "6331ec2742d22680ba5161643d149dbe",
                "6fdb50aa3d88f30fea6cdc90145a2e47",
                "707fbcdffba8669f9f7b9aa34a51af79",
                "79d528090d67a7944d352bcc913ea581",
                "ed70b52582299c5d9d7eda65efce9a2a"
        };

        int faceArrLen = faceIdArray.length;
        StringBuffer beforeDelete = null;
        String msg;
        //1、先增加，一次增加五个
        for(int i = 0;i<picPath.length;i++){
            registerFaceMultiPic(picPath[i]);
        }
        //2、查询删除前的faceID数据
        beforeDelete = queryUserWithFaceId(vipUser);
        int index = 0;
        for(index = 0;index<faceArrLen;index++){
            String compareFaceId = beforeDelete.toString();
            if(compareFaceId.contains(faceIdArray[index])){
                continue;
            }
        }

        if(index==faceArrLen){
            msg = "successful";
        }else{
            msg = "特定人物查询失败！";
            throw new Exception(msg);
        }

        //3、特定人脸删除，删除某一张人脸图片
        deleteFace(faceIdArray[0]);
        //4、查询删除后的数据
        StringBuffer afterDelete =  queryUserWithFaceId(vipUser);
        String afterDel = afterDelete.toString();
        if(afterDel.contains(faceIdArray[0])){
            msg = "用特定人脸删除功能删除某张图片后，再次查询仍能查询到该图片";
            throw new Exception(msg);
        }
    }

    //校验“特殊人物查询”和“特殊人脸查询”返回的faceURL是否一致。
    @Test
    public void checkFaceURL () throws Exception{
       String userId = String.valueOf(System.currentTimeMillis());
        String picPath = "src/main/resources/test-res-repo/customer-gateway/compareUrl.jpg";
       registerFaceSingleCompare(userId,picPath);
       //2、获取特殊人物查询的URL
        String userUrl = queryUserWithFaceURL(userId);
        logger.info(userUrl);
        String faceurl = searchFaceWithFaceUrlCompare();
        logger.info(faceurl);
        if(userUrl!=null&&!"".equals(userUrl)){
            if(!userUrl.equals(faceurl)){
                String msg = "同一个人的同一张人脸图片，在“特殊人物查询”与“特殊人脸查询”中返回的face_url不同！";
                throw new Exception(msg);
            }
        }
    }


//特定人脸查询（先注册，再查询）
    @Test
    public void searchFaceNormal() throws Exception{
        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
        String facePath = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
        String[] resource = new String[]{getImageBinary(facePath)};
        registerFaceSingle(vipUser);
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"pic_url\":\"@0\"," +
                "\"result_num\":1" +
                "}";
        apiCustomerRequest(router, resource, json);
    }

    public void searchFaceWithNewGroup(String grpName,String picPath) throws Exception{
        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"pic_url\":\"@0\"," +
                "\"result_num\":1" +
                "}";
        apiCustomerRequestSearchFaceWithNewGroup(router, resource, json);
    }

    public String searchFaceWithFaceUrlCompare() throws Exception{
        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
        String facePath = "src/main/resources/test-res-repo/customer-gateway/compareUrl.jpg";
        String[] resource = new String[]{getImageBinary(facePath)};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"pic_url\":\"@0\"," +
                "\"result_num\":1" +
                "}";
        return  apiCustomerRequestSearchFaceWithFaceUrl(router, resource, json);
    }

    //特殊人脸查询，测试resultNum
    @Test(dataProvider = "CASE_RESULT_NUM")
    public void searchFaceTestResultNumwithout0(int resultNum) throws Exception{
        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
        String facePath = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
        String[] resource = new String[]{getImageBinary(facePath)};
        String json = "{" +
                "\"group_name\":\"TestGroup\"," +
                "\"pic_url\":\"@0\"," +
                "\"result_num\":" + resultNum +
                "}";
        apiCustomerRequest(router, resource, json);
    }
@Test
    public void searchFaceTestResultNum0() throws Exception{
        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
        String facePath = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
        String[] resource = new String[]{getImageBinary(facePath)};
        String json = "{" +
                "\"group_name\":\"TestGroup\"," +
                "\"pic_url\":\"@0\"," +
                "\"result_num\":" + 0 +
                "}";
    apiCustomerRequestwithResultNum0(router, resource, json);
    }

    private void apiCustomerRequest(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test normal");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private String apiCustomerRequestSearchFaceWithFaceUrl(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Search face with faceUrl!");
        String faceUrl = "";
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            com.alibaba.fastjson.JSONObject jstr = (com.alibaba.fastjson.JSONObject) apiResponse.getData();
            com.alibaba.fastjson.JSONArray jsonArray = jstr.getJSONArray("faces");
            int len = jsonArray.size();
            for(int i = 0; i<len;i++){
                com.alibaba.fastjson.JSONArray similar_faces = jsonArray.getJSONObject(i).getJSONArray("similar_faces");
                for(int j=0;j<similar_faces.size();j++){
                    com.alibaba.fastjson.JSONObject face = similar_faces.getJSONObject(j);
                    faceUrl = face.getString("face_url");
                }
            }
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
        return faceUrl;

    }

    private void apiCustomerRequestSearchFaceWithNewGroup(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test normal");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse)+
                        "用一个新group“特殊人脸注册”后的首次“特殊人脸查询”失败";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    //测试查询新增组是否报3006
    private void apiCustomerRequestWithNewGroup(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test query new group！");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if (apiResponse.getCode() != StatusCode.SUCCESS) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.SUCCESS +" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }


    private void apiCustomerRequestwithResultNum0(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test resultNum = 0");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if (apiResponse.getCode() != StatusCode.BAD_REQUEST) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.BAD_REQUEST +" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }

//也能将空的UID分类抛异常
    private void apiCustomerRequestTestUID(String router, String[] resource, String json,String UIDCase) throws Exception {
        logMine.logStep("Test invalid UID!");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UIDCase)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));

            if (apiResponse.getCode() != StatusCode.UN_AUTHORIZED) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.UN_AUTHORIZED +" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e){
            throw e;
        }
    }

    //也能将空的Appid分类抛异常
    private void apiCustomerRequestTestAppid(String router, String[] resource, String json, String appidCase) throws Exception {
        logMine.logStep("Test invalid appid");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(appidCase)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if (apiResponse.getCode() != StatusCode.UN_AUTHORIZED) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.UN_AUTHORIZED +" 不符！";
                throw new Exception(msg);
            }
        } catch (SdkClientException e) {
            String msg = e.getMessage();
            throw new Exception(msg);

        } catch (Exception e){
            throw e;
        }

    }

    //测试公共请求体中的version参数
    private void apiCustomerRequestTestVersion(String router, String[] resource, String json, String versionCase) throws Exception {
        logMine.logStep("Test invalid version");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(versionCase)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if (apiResponse.getCode() == StatusCode.SUCCESS) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统未对version进行校验！";
                throw new Exception(msg);
            }
        } catch (SdkClientException e) {
            String msg = e.getMessage();
            throw new Exception(msg);

        } catch (Exception e){
            throw e;
        }

    }

//测试“特殊人物组查询”的人物组名
    private void apiCustomerRequestTestGrpName(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test invalid groupName！");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if (apiResponse.getCode() != StatusCode.BAD_REQUEST) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.BAD_REQUEST +" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    //测试“特殊人物组查询”的新注册的人物组名
    private void apiCustomerRequestTestNewGrpName(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test invalid groupName！");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if (apiResponse.getCode() != StatusCode.SUCCESS) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.SUCCESS +" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private void apiCustomerRequestTestUserId(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test invalid userId！");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if (apiResponse.getCode() != StatusCode.SUCCESS) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.SUCCESS +" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private int apiCustomerRequestWithResult(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test query user result!");
//        String userId = null;
        int faceNum = 0;
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            com.alibaba.fastjson.JSONObject jstr = (com.alibaba.fastjson.JSONObject) apiResponse.getData();
//            String userId = jstr.getString("user_id");
            com.alibaba.fastjson.JSONArray jsonArray = jstr.getJSONArray("faces");
            faceNum = jsonArray.size();
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
        return faceNum;
    }

    private StringBuffer apiCustomerRequestWithFaceId(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test query user URL!");
//        String userId = null;
        int faceNum = 0;
        StringBuffer FaceIdBuffer = new StringBuffer();
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            com.alibaba.fastjson.JSONObject jstr = (com.alibaba.fastjson.JSONObject) apiResponse.getData();
            String userId = jstr.getString("user_id");
            com.alibaba.fastjson.JSONArray jsonArray = jstr.getJSONArray("faces");
            int len = jsonArray.size();
            for(int i = 0; i<len;i++){
                String faceIdTmp = jsonArray.getString(i);
                FaceIdBuffer.append(faceIdTmp+" ");
            }
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
        return FaceIdBuffer;
    }

    private String apiCustomerRequestWithFaceURL(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test query user with face URL!");
        String faceUrl = "";
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            com.alibaba.fastjson.JSONObject jstr = (com.alibaba.fastjson.JSONObject) apiResponse.getData();
//            String userId = jstr.getString("user_id");
            com.alibaba.fastjson.JSONArray jsonArray = jstr.getJSONArray("faces");
            int len = jsonArray.size();
            for(int i = 0; i<len;i++){
                String faceIdTmp = jsonArray.getString(i);
                faceUrl = jsonArray.getJSONObject(i).getString("face_url");
            }
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
        return faceUrl;
    }


    public String getImageBinary(String picPath){
//        File f = new File("src/main/resources/test-res-repo/customer-gateway/1.jpg");
        File f = new File(picPath);
        BufferedImage bi;
        try {
            bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();

            return encoder.encodeBuffer(bytes).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //用于特殊人物注册
    @DataProvider(name = "BAD_GRP_NAME_REG")
    public Object[] createRegBadGrpName() {
        return new String[] {
                "",
                " ",
                "`~!@#$^&()-=_+[]{}|\\;\':\",./ <>?\"￥。、‘；《》",
                "%*",
                " TestGroup1621",
                "TestGroup1621 ",
                "嗨"
        };
    }

    //用于各种查询
    @DataProvider(name = "BAD_GRP_NAME_QUERY")
    public Object[] createQueryBadGrpName() {
        return new String[] {
                String.valueOf(System.currentTimeMillis()),
                "",
                " ",
                "`~!@#$^&()-=_+[]{}|\\;\':\",./ <>?\"￥。、‘；《》",
                "%*",
                "TestGroup1621",
                "嗨"
        };
    }


    @DataProvider(name = "CASE_UID")
    public Object[] createInvalidUID() {

        return new String[] {
                "",
                "0.0",
                "0",
                "你好",
                "abcd",
                "^&*$￥||\\*",
                " uid_e0d1ebec",
                "uid_e0d1ebec "
        };
    }

    @DataProvider(name = "CASE_APPID")
    public Object[] createInvalidAppid() {

        return new String[] {
                "",
                "-1",
                "0",
                "0.0",
                "hello",
                "2A!34'\\*a",
                " a4d4d18741a8",
                "a4d4d18741a8 "
        };
    }

    @DataProvider(name = "CASE_VERSION")
    public Object[] createInvalidVersion() {

        return new String[] {
                "",
                " ",
                "!@#$%^&*()",
                "1.0",
                "hello",
                "2A!34'\\*a",
                " v1.0",
                "v1.1 "
        };
    }

    @DataProvider(name = "CASE_USER_ID")
    public Object[] createUserid() {

        return new String[] {
                "00000",
                "00001",
                "00002",
                "00003",
                "00004",
                "00005",
        };
    }

    @DataProvider(name = "CASE_BAD_USER_ID")
    public Object[] createBadUserid() {

        return new String[] {
                "00004",
                "你好",
                "hello！",
                "-100",
                "#^%$#￥",
                "00004 ",
                " 00005"
        };
    }

    @DataProvider(name = "CASE_PIC")
    public Object[] createMultiUserid() {
        return new String[] {
                "src/main/resources/test-res-repo/customer-gateway/1.jpg",
                "src/main/resources/test-res-repo/customer-gateway/2.jpg",
                "src/main/resources/test-res-repo/customer-gateway/3.jpg",
                "src/main/resources/test-res-repo/customer-gateway/4.jpg",
                "src/main/resources/test-res-repo/customer-gateway/5.jpg",
//                "src/main/resources/test-res-repo/customer-gateway/6.jpg"报错
        };
    }

    @DataProvider(name = "CASE_FACE_ID")
    public Object[] createMultiFaceid() {

        return new String[] {
        "6331ec2742d22680ba5161643d149dbe",
        "6fdb50aa3d88f30fea6cdc90145a2e47",
        "707fbcdffba8669f9f7b9aa34a51af79",
        "79d528090d67a7944d352bcc913ea581",
        "ed70b52582299c5d9d7eda65efce9a2a"
        };
    }

    @DataProvider(name = "CASE_RESULT_NUM")
    public Object[] createResultNum1() {
        return new Integer[] {0,1,5,10};
    };
}
