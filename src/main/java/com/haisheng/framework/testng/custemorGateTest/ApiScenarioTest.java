package com.haisheng.framework.testng.custemorGateTest;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.exception.SdkClientException;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.UUID;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 线下消费者接口测试
 * @author Shine
 */
public class ApiScenarioTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    private String UID            = "uid_e0d1ebec";
    private String APP_ID         = "a4d4d18741a8";
    private String SHOP_ID        = "134";
    private String RE_ID          = "144";
    private String DEVICE_ID      = "6254834559910912";
    private BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    private String vipGroup = "vipGroup";
    private String vipUser = "00000";
    private String[] picPathArr =
            { "src/main/resources/test-res-repo/customer-gateway/1.jpg",
                    "src/main/resources/test-res-repo/customer-gateway/2.jpg",
                    "src/main/resources/test-res-repo/customer-gateway/3.jpg",
                    "src/main/resources/test-res-repo/customer-gateway/4.jpg",
                    "src/main/resources/test-res-repo/customer-gateway/5.jpg"};
    private String vipPic = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
    private String[] userIdArr =
            {
               "user1", "user2", "user3", "user4", "user5"
            };
    //1.jpg是通用的默认图片


    /**
     * 特殊人物注册通用方法，将必填参数作为参入传入，根据不同的方法调用进行传入不同的参数，
     * 且参数没有默认值
    */
    public void registerFaceTestBadParaAgent(String grpName, String userId, String picPath, int function) throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{\"group_name\":\"" + grpName +"\"," +
                "\"user_id\":\""+userId+"\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"pic_url\":\"@0\"" +
                "}";
        switch (function){
            case 1:doTestBadGrpName(router,  resource, json);
            break;
            case 2:doTestBadUserId(router,  resource, json);
            break;
            default:
        }
    }

    /** 
    * @Description:  
    * @Param: [grpName, userId, picPath] 
    * @return: void 
    * @Author: Shine 
    * @Date: 2019/4/6 
    */ 
    public void registerFaceNormal(String grpName, String userId, String picPath) throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{\"group_name\":\"" + grpName +"\"," +
                "\"user_id\":\""+userId+"\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"pic_url\":\"@0\"" +
                "}";
        apiCustomerRequest(router,  resource, json);
    }

    @Test (dataProvider = "BAD_GRP_NAME_REG")
    public void registerFaceTestBadGrpName(String grpName) throws Exception{
        registerFaceTestBadParaAgent(grpName,vipUser,vipPic,1);
    }

    //特殊人物注册，测试非必填参数
    @Test
    public void registerFaceTestNotRequiredPara() throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String picPath = vipPic;
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

    @Test (dataProvider = "CASE_BAD_USER_ID")
    public void registerFaceTestBadUserId(String userId) throws Exception{
        registerFaceTestBadParaAgent(vipGroup,userId,vipPic,2);
    }

//特殊人脸注册，给一个人注册多张图片
    @Test
    public void registerFaceMultiPic(String picPath) throws Exception{
        deleteUserNormal(vipGroup,vipUser);
        for(int i = 0;i<picPathArr.length;i++){
            registerFaceNormal(vipGroup,vipUser,picPathArr[i]);
        }
        HashMap hm =  queryUserWithResult(vipGroup,vipUser);
        int faceNum = (int)hm.get("faceNum");
        if(faceNum!=5){
            String msg = "用“特殊人物注册”给一个人注册多张人脸不同的图片后，查询到的图片数量与注册的数量不相符";
            throw new Exception(msg);
        }
    }
//------------------------------以上是特殊人物注册的case----------------------------------
// -----------------------------以下是特殊人物组查询的case-------------------------------------

    //2.1 特殊人物组查询，测试UID,包括UID为空的，throw不同的exception
    @Test(dataProvider = "CASE_UID")
    public void TestUID(String UIDCase) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"" +
                "}";
        doTestUID(router, resource, json, UIDCase);
    }

    //2.2 特殊人物组查询，测试异常的appid
    @Test(dataProvider = "CASE_APPID")
    public void TestAppid(String AppidCase) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"" +
                "}";
        doTestAppid(router, resource, json, AppidCase);
    }

    //2.3 特殊人物组查询，测试异常的version
    @Test(dataProvider = "CASE_VERSION")
    public void TestVersion(String versionCase) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"" +
                "}";
        doTestVersion(router, resource, json, versionCase);
    }

    //2.4 特殊人物组查询，测试组名
    @Test(dataProvider = "BAD_GRP_NAME_QUERY")
    public void queryGroupTestGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"" +
                "}";
        doTestBadGrpName(router, resource, json);
    }

    //2.5 测试用新group注册后首次特殊人物组查询是否报3006
    @Test
    public void QueryGroupWithNewGroup () throws Exception{
        String newGroup = String.valueOf(System.currentTimeMillis());

        String msg;
        //1、先注册一张人脸图片
        registerFaceNormal(newGroup,vipUser,vipPic);
        //2、首次用“特殊人物组查询”查询该人信息
        queryGroupWithNewGroup(newGroup);
    }

    //2.5特殊人物组查询，测试新组3006
    public void queryGroupWithNewGroup(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"" +
                "}";
        doTestNewGroup(router, resource, json);
    }

    //2.6.1 特殊人物组查询，测试查询结果是否正确
    @Test
    public void queryGroupTestIsSuccess() throws Exception {
        String newGroup = String.valueOf(System.currentTimeMillis());
        for (int i= 0;i<userIdArr.length;i++){
            registerFaceNormal(newGroup,userIdArr[i],vipPic);
        }
        int userNum = queryGroupWithResult(newGroup);
        if(userNum!=5){
            String msg = "特殊人物组查询失败";
            throw new Exception(msg);
        }
    }

    //2.6.2 特殊人物组查询，测试查询结果是否正确
    public int queryGroupWithResult(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"" +
                "}";
        return doQueryGroupTestIsSuccess(router, resource, json);
    }

    //----------------------以上是特殊人物组查询的case---------------------------------

    //3.1 特殊人物查询，测试组名
    @Test(dataProvider = "BAD_GRP_NAME_QUERY")
    public void queryUserTestGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+vipUser+"\"" +
                "}";
        doTestBadGrpName(router, resource, json);
    }

    //3.2 特殊人物查询，测试userid
    @Test (dataProvider = "CASE_BAD_USER_ID")
    public void queryUserTestBadUserId(String badUserId) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+badUserId+"\"" +
                "}";
        doTestBadUserId(router, resource, json);
    }

// 3.3 测试用新group注册后首次特殊人物查询是否报3006
@Test
public void queryUserWithNewGroup () throws Exception{
        String newGroup = String.valueOf(System.currentTimeMillis());
    //1、先注册一张人脸图片
    registerFaceNormal(newGroup,vipUser,vipPic);
    //2、首次用“特殊人物查询”查询该人信息
    queryUserWithNewGroup(newGroup,vipUser);
}

//3.3 特殊人物查询，测试新组3006
    public void queryUserWithNewGroup(String grpName,String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        doTestNewGroup(router, resource, json);
    }

    /*特殊人物查询，返回查询人物的人脸数量，第一张图片的faceId和faceUrl
              以及所有图片faceId的拼接和所有图片faceUrl的拼接*/
    public HashMap queryUserWithResult(String grpName, String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        return doQueryUserWithResult(router, resource, json);
    }
    //--------------------以上是特殊人物查询的case-----------------------------
    //--------------------以下是特殊人脸查询的case-----------------------------

    //4.1 特殊人脸查询，测试组名
    @Test(dataProvider = "BAD_GRP_NAME_QUERY")
    public void searchFaceTestGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+vipUser+"\"" +
                "}";
        doTestBadGrpName(router, resource, json);
    }

    //4.2 测试用新group注册后首次特殊人脸查询是否报3006
    @Test
    public void SearchFaceWithNewGroup () throws Exception{
        String newGroup = String.valueOf(System.currentTimeMillis());
        String picPath = "src/main/resources/test-res-repo/customer-gateway/NewGroup.jpg";
        String userId = newGroup;

        String msg;
        //1、先注册一张人脸图片
        registerFaceNormal(userId,newGroup,picPath);

        //2、首次用“特殊人脸查询”查询该人信息
        searchFaceWithNewGroup(newGroup,picPath);
    }

    //4.2 测试用新group注册后首次特殊人脸查询是否报3006
    public void searchFaceWithNewGroup(String grpName,String picPath) throws Exception{
        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"pic_url\":\"@0\"," +
                "\"result_num\":1" +
                "}";
        doTestNewGroup(router, resource, json);
    }

    //4.3 校验“特殊人物查询”和“特殊人脸查询”返回的faceURL是否一致。
    @Test
    public void checkFaceURL () throws Exception{
        String grpName = String.valueOf(System.currentTimeMillis());
        String userId = grpName;
        String picPath = "src/main/resources/test-res-repo/customer-gateway/compareUrl.jpg";
        registerFaceNormal(grpName,userId,picPath);
        //2、获取特殊人物查询的URL
        HashMap<String,String> queryResult = queryUserWithResult(grpName,userId);
        String userUrl = queryResult.get("firstUrl");
        logger.info("用特殊人物查询查到的url是："+userUrl);
        //3、获取特殊人脸查询的url
        HashMap<String,String> searchFaceResult = searchFaceWithResult(grpName,picPath);
        String faceurl = searchFaceResult.get("firstFaceUrl");
        logger.info("用特殊人脸查询查到的url是："+faceurl);
        //4、比较两个url
        if(userUrl!=null&&!"".equals(userUrl)){
            if(!userUrl.equals(faceurl)){
                String msg = "同一个人的同一张人脸图片，在“特殊人物查询”与“特殊人脸查询”中返回的face_url不同！";
                throw new Exception(msg);
            }
        }
    }

    //4.4 获取特殊人脸查询的结果（faceNum、firstFaceUrl、faceUrlConcat）
    public HashMap<String, String> searchFaceWithResult(String grpName, String picPath) throws Exception{
        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"pic_url\":\"@0\"," +
                "\"result_num\":1" +
                "}";
        return  doSearchFaceWithResult(router, resource, json);
    }

    //4.5 特殊人脸查询，测试resultNum（正常的输入，1-10）
    @Test(dataProvider = "CASE_RESULT_NUM")
    public void searchFaceTestResultNormal(int resultNum) throws Exception{
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
    //特殊人脸查询，测试resultNum（异常的输入，1-10之外的数字，以及特殊字符和字符串）
    @Test(dataProvider = "")
    public void searchFaceTestResultNum0(String badResultNum) throws Exception{
        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(vipPic)};
        String json = "{" +
                "\"group_name\":\"TestGroup\"," +
                "\"pic_url\":\"@0\"," +
                "\"result_num\":" + badResultNum +
                "}";
        doSearchFaceWithResultNum0(router, resource, json);
    }

    //----------------------以上是特殊人脸查询的case-------------------
//------------------一下是特殊人物删除的case--------------------
    //1 特殊人物删除，测试组名
    @Test(dataProvider = "BAD_GRP_NAME_QUERY")
    public void deleteUserTestGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+vipUser+"\"" +
                "}";
        doTestBadGrpName(router, resource, json);
    }

    //2 特殊人物删除，测试无效userid
    @Test (dataProvider = "CASE_BAD_USER_ID")
    public void deleteUserTestBadUserId(String badUserId) throws Exception{
        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+badUserId+"\"" +
                "}";
        doTestBadUserId(router, resource, json);
    }

    //3、特殊人物删除，根据userId删除特定人物
    public void deleteUserNormal(String grpName,String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        apiCustomerRequest(router, resource, json);
    }

    //5、特殊用户删除，测试删除以后是否能重新注册该人(增-查-删-查-增-查)
    //此功能也可测试正常的删除是否成功。
    @Test
    public void deleteUserTestReAdd() throws Exception{
        String msg = "";
        String userId = String.valueOf(System.currentTimeMillis());
        registerFaceNormal(vipGroup,userId,vipPic);
        HashMap beforeDeleteResult = queryUserWithResult(vipGroup,userId);
        int beforeDelete = (int)beforeDeleteResult.get("faceNum");
        deleteUserNormal(vipGroup,userId);
        HashMap afterDeleteResult = queryUserWithResult(vipGroup,userId);
        int afterDelete = (int)afterDeleteResult.get("faceNum");
        if(beforeDelete==1&&afterDelete==0){
            msg = "“特定用户删除”操作成功！";
            logger.info(msg);
        }else{
            msg = "“特定用户删除”操作失败！"
                    + "group: " + vipGroup
                    + "userid: " + userId;
            throw new Exception(msg);
        }
        registerFaceNormal(vipGroup,userId,vipPic);
        HashMap reAddResult = queryUserWithResult(vipGroup,userId);
        int reAdd = (int)reAddResult.get("faceNum");
        if(reAdd==1){
            msg = "用“特定人物删除”功能删除该用户后，重新注册该用户成功！";
            logger.info(msg);
        }else{
            msg = "用“特定人物删除”功能删除用户后，不能重新注册该用户！"
                    + "group: " + vipGroup
                    + "userid: " + userId;
            throw new Exception(msg);
        }
    }

    //--------------------------------以上是特殊用户删除-----------------------
    //--------------------------------以下是特殊人脸删除-----------------------
    //1、特殊人脸删除，测试组名
    @Test(dataProvider = "BAD_GRP_NAME_QUERY")
    public void deleteFaceTestBadGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_DELETE_FACE/v1.0";
        String[] resource = new String[]{};
        //这个faceId应该可以随便写，只是写了个错的查不到就是了。
        String faceId = "ee4a2829872770e8e1ee8e1f82e3324a";
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+vipUser+"\"," +
                "\"face_id\":\""+faceId+"\"" +
                "}";
        doTestBadGrpName(router, resource, json);
    }

    //3.2 特殊人物查询，测试userid
    @Test (dataProvider = "CASE_BAD_USER_ID")
    public void deleteFaceTestBadUserId(String badUserId) throws Exception{
        String router = "/scenario/gate/SYSTEM_DELETE_FACE/v1.0";
        String[] resource = new String[]{};
        String faceId = "ee4a2829872770e8e1ee8e1f82e3324a";
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+badUserId+"\"," +
                "\"face_id\":\""+faceId+"\"" +
                "}";
        doTestBadUserId(router, resource, json);
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

    //特殊人脸删除，测试删除以后是否可以重新注册该人。（注册-查询-删除一张-查询-全部删除-查询-删除-注册-查询）
    //此case可以测试（1）删除一张是否成功，（2）以及删除多张是否成功（3）删除后是否可以重新注册（4）是否可以删除两次
    @Test
    public void deleteFaceTestReAdd () throws Exception{
        String [] faceIdArray = {
                "6331ec2742d22680ba5161643d149dbe",
                "6fdb50aa3d88f30fea6cdc90145a2e47",
                "707fbcdffba8669f9f7b9aa34a51af79",
                "79d528090d67a7944d352bcc913ea581",
                "ed70b52582299c5d9d7eda65efce9a2a"
        };
        int faceIdArrLen = faceIdArray.length;
        String userId = String.valueOf(System.currentTimeMillis());
        String msg;
        //1、先增加，一次增加五个
        for(int i = 0;i<picPathArr.length;i++){
            registerFaceNormal(vipGroup,userId,picPathArr[i]);
        }
        //2、查询删除前的faceID数据
        HashMap beforeDeleteResult = queryUserWithResult(vipGroup,userId);
        String faceIdConcat = (String) beforeDeleteResult.get("faceIdConcat");
        int index;
        for(index = 0;index<faceIdArrLen;index++){
            if(!faceIdConcat.contains(faceIdArray[index])){
                msg = "给一个人注册多张图片后，用“特定人脸查询”未能查询到全部注册图片的faceId"
                        + "group: " + vipGroup
                        + "userid: " + userId;
                throw new Exception(msg);
            }
        }
        //3、删除一张图片
        deleteFace(faceIdArray[0]);
//        4、查询删除一张图片后的数据
        HashMap afterDeleteOneResult = queryUserWithResult(vipGroup,userId);
        String AfterDeleteOnefaceIdConcat = (String) afterDeleteOneResult.get("faceIdConcat");
        if(AfterDeleteOnefaceIdConcat.contains(faceIdArray[0])){
            msg = "用特定人脸删除功能删除某张图片后，再次查询仍能查询到该图片,"
                    +"故用“特殊人脸删除”删除某张特定图片失败！"
                    + "group: " + vipGroup
                    + "userid: " + userId
                    + "faceId: " + faceIdArray[0];
            throw new Exception(msg);
        }

        //5、删除全部图片
        for(int i = 0;i<faceIdArrLen;i++){
            deleteFace(faceIdArray[i]);
        }
        //6、查询删除全部图片后的数据
        HashMap afterDeleteAllResult = queryUserWithResult(vipGroup,userId);
        String AfterDeleteAllfaceIdConcat = (String) afterDeleteAllResult.get("faceIdConcat");
        if(!("".equals(AfterDeleteAllfaceIdConcat)||AfterDeleteAllfaceIdConcat!=null)){
            msg = "用“特定人脸删除”功能删除全部图片后，仍能查询到该人的人脸信息，故“特定人脸删除”删除全部图片失败！"
                    + "group: " + vipGroup
                    + "userid: " + userId;
            throw new Exception(msg);
        }
        //7、删除全部图片
        for(int i = 0;i<faceIdArrLen;i++){
            deleteFace(faceIdArray[i]);
        }
        //查询删除全部图片后的数据（应该不用查了，应该不会出现能够查询到数据的情况，
        // 如果有错的话，一定是别的错误，在删除方法中就会catch到）
        /*HashMap reDeleteAllResult = queryUserWithResult(vipGroup,userId);
        String reDeleteAllfaceIdConcat = (String) reDeleteAllResult.get("faceIdConcat");
        if(!("".equals(reDeleteAllfaceIdConcat)||reDeleteAllfaceIdConcat!=null)){
            msg = "再次用“特定人脸删除”功能删除全部图片出错！"
                    + "group: " + vipGroup
                    + "userid: " + userId;
            throw new Exception(msg);
        }*/
        //8、再次注册（其实可以只注册一张，这里就注册多张吧）
        for(int i = 0;i<picPathArr.length;i++){
            registerFaceMultiPic(picPathArr[i]);
        }

        //9、注册后查询
        HashMap reAddResult = queryUserWithResult(vipGroup,userId);
        String reAddfaceIdConcat = (String) reAddResult.get("faceIdConcat");
        for(index = 0;index<faceIdArrLen;index++){
            if(!reAddfaceIdConcat.contains(faceIdArray[index])){
                msg = "用“特定人脸删除”删除某人的全部图片后，再次注册失败！"
                        + "group: " + vipGroup
                        + "userid: " + userId;
                throw new Exception(msg);
            }
        }
    }
    //-----------------------------以上是特殊人脸删除的case-------------------------------------
    //-----------------------------以下是具体的执行方法-----------------------------------------
//1、通用的方法
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

    //2、测试“特殊人物组查询”的人物组名
    private void doTestBadGrpName(String router, String[] resource, String json) throws Exception {
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

    //3、测试新注册的组名查询时是否报3306（这个是不是可以直接用通用的方法，因为只要报错就行，即使是组名没问题，也不一定就是1000）
    //所以用状态码是不是1000来验证有点不大合适！
    private void doTestNewGroup(String router, String[] resource, String json) throws Exception {
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
            if (apiResponse.getCode() == StatusCode.GROUP_RUNNING) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "采用新组注册后，首次查询时"+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.SUCCESS +" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    //4、测试UID，除了字母数字，下划线以外的所有字符（包括空，空格，特殊字符，中文）都为非法
    private void doTestUID(String router, String[] resource, String json,String UIDCase) throws Exception {
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

    //5、测试Appid，除了字母数字，下划线以外的所有字符（包括空，空格，特殊字符，中文）都为非法
    private void doTestAppid(String router, String[] resource, String json, String appidCase) throws Exception {
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

    //6、测试公共请求体中的version参数
    private void doTestVersion(String router, String[] resource, String json, String versionCase) throws Exception {
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
//7、测试userId
    private void doTestBadUserId(String router, String[] resource, String json) throws Exception {
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
            if (apiResponse.getCode() != StatusCode.BAD_REQUEST) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.BAD_REQUEST+" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    //8、获取图片的base64编码
    public String getImageBinary(String picPath){
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

    //---------------------------以下特殊人物组查询用到的方法---------------------------

    //1、特殊人物组查询，查询验证该功能的正确性（返回该组中userid的数量）
    //是不是要将具体的userid返回呢？这样才能验证结果的准确性啊！
    private int doQueryGroupTestIsSuccess(String router, String[] resource, String json) throws Exception {
        logMine.logStep("测试特殊人物组查询是否成功！");
        int len = 0;
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

            com.alibaba.fastjson.JSONObject jsonObjectData = (com.alibaba.fastjson.JSONObject) apiResponse.getData();
            com.alibaba.fastjson.JSONArray jsonArrayPerson = jsonObjectData.getJSONArray("person");
            len = jsonArrayPerson.size();

            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
        return len;
    }

    //------------------以下是特殊人物查询用到的方法----------------------------------
//1、特殊人物查询，返回查询结果。
    private HashMap<String, String> doQueryUserWithResult(String router, String[] resource, String json) throws Exception {
        logMine.logStep("特殊人物查询，返回查询结果。");
        String faceUrlFirst = "", faceIdFirst = "", faceUrlConcat = "", faceIdConcat = "";
        HashMap<String, String> hm = null;
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
            com.alibaba.fastjson.JSONObject jsonObjectData = (com.alibaba.fastjson.JSONObject) apiResponse.getData();
            com.alibaba.fastjson.JSONArray jsonArrayFaces = jsonObjectData.getJSONArray("faces");
            int len = jsonArrayFaces.size();
            for(int i = 0; i<len;i++){
                faceUrlFirst = jsonArrayFaces.getJSONObject(0).getString("face_url");
                faceIdFirst = jsonArrayFaces.getJSONObject(0).getString("face_id");

               String faceUrl = jsonArrayFaces.getJSONObject(i).getString("face_url");
               String faceId = jsonArrayFaces.getJSONObject(i).getString("face_id");

                faceUrlConcat = faceUrlConcat.concat(faceUrl);
                faceIdConcat = faceIdConcat.concat(faceId);
            }
            hm.put("faceNum",String.valueOf(len));
            hm.put("faceIdFirst",faceIdFirst);
            hm.put("faceUrlFirst",faceUrlFirst);
            hm.put("faceIdConcat",faceIdConcat);
            hm.put("faceUrlConcat",faceUrlConcat);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
        return hm;
    }

//------------------以上是特殊人脸查询用到的方法-----------------------------

    //1、特殊人脸查询，返回查询结果（url和num）
    private HashMap<String,String> doSearchFaceWithResult(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Search face with result!");
        HashMap<String,String> hm= new HashMap();
        String firstFaceUrl = "";
        String faceUrlConcat = "";

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
            com.alibaba.fastjson.JSONArray jsonArrayFaces = jstr.getJSONArray("faces");
            int len = jsonArrayFaces.size();
            for(int i = 0; i<len;i++){
                com.alibaba.fastjson.JSONArray jsonArraySimilar_faces = jsonArrayFaces.getJSONObject(i).getJSONArray("similar_faces");
                for(int j=0;j<jsonArraySimilar_faces.size();j++){
                    com.alibaba.fastjson.JSONObject jsonObjectfirstFace = jsonArraySimilar_faces.getJSONObject(0);
                    com.alibaba.fastjson.JSONObject jsonObjectFace = jsonArraySimilar_faces.getJSONObject(j);
                    firstFaceUrl = jsonObjectfirstFace.getString("face_url");
                    String FaceUrl = jsonObjectFace.getString("face_url");
                    faceUrlConcat = faceUrlConcat.concat(FaceUrl);
                }
            }
            hm.put("faceNum",String.valueOf(len));
            hm.put("firstFaceUrl",firstFaceUrl);
            hm.put("faceUrlConcat",faceUrlConcat);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
        return hm;

    }

    //2、特殊人脸查询，参数resultNum非法（目前只测试了为 0）
    private void doSearchFaceWithResultNum0(String router, String[] resource, String json) throws Exception {
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

        //用于特殊人物注册
    @DataProvider(name = "BAD_GRP_NAME_REG")
    public Object[] createRegBadGrpName() {
        return new String[] {
                "",  " ",  "嗨",
                //英文字符
                "~",  "！", "@",  "#",  "$",
                "%",  "^",  "&",  "*",  "(",
                ")",  "-",  "+",  "=",  "{",
                "}",  "[",  "]", "|", "\\",
                ";",  ":",  "'",  "\"",  ",",
                "<",  ">",  ".",  "?",  "/",
                //中文字符
                "·",  "！",  "￥",  "……",  "（",
                "）",  "——",  "【",  "】",  "、",
                "；",  "：",  "”",  "‘",  "《",
                "，",  "》",  "。" ,  "？",  "、",
                //前后带空格的，看查询中不带空格能查出来吗?其实最好能查出来，前后的空格希望可以trim掉
                " TestGroup201904061848",
                "TestGroup201904061848 "
        };
    }

    @DataProvider(name = "BAD_GRP_NAME_REQ")
    public Object[] createReqBadGrpName() {
        return new String[] {
                "",  " ",  "嗨",
                //英文字符
                "~",  "！", "@",  "#",  "$",
                "%",  "^",  "&",  "*",  "(",
                ")",  "-",  "+",  "=",  "{",
                "}",  "[",  "]", "|", "\\",
                ";",  ":",  "'",  "\"",  ",",
                "<",  ">",  ".",  "?",  "/",
                //中文字符
                "·",  "！",  "￥",  "……",  "（",
                "）",  "——",  "【",  "】",  "、",
                "；",  "：",  "”",  "‘",  "《",
                "，",  "》",  "。" ,  "？",  "、",
                //前后带空格的，看查询中不带空格能查出来吗?其实最好能查出来，前后的空格希望可以trim掉
                "TestGroup201904061848"//与查询的区别就在于此
        };
    }

    @DataProvider(name = "BAD_UID")
    public Object[] createBadUID() {

        return new String[] {
                "",  " ",  "嗨",
                //英文字符
                "~",  "！", "@",  "#",  "$",
                "%",  "^",  "&",  "*",  "(",
                ")",  "-",  "+",  "=",  "{",
                "}",  "[",  "]", "|", "\\",
                ";",  ":",  "'",  "\"",  ",",
                "<",  ">",  ".",  "?",  "/",
                //中文字符
                "·",  "！",  "￥",  "……",  "（",
                "）",  "——",  "【",  "】",  "、",
                "；",  "：",  "”",  "‘",  "《",
                "，",  "》",  "。" ,  "？",  "、",
                //前后带空格的，看查询中不带空格能查出来吗?其实最好能查出来，前后的空格希望可以trim掉
                " uid_e0d1ebec",
                "uid_e0d1ebec "
        };
    }

    @DataProvider(name = "BAD_APPID")
    public Object[] createBadAppid() {

        return new String[] {
                "",  " ",  "嗨",
                //英文字符
                "~",  "！", "@",  "#",  "$",
                "%",  "^",  "&",  "*",  "(",
                ")",  "-",  "+",  "=",  "{",
                "}",  "[",  "]", "|", "\\",
                ";",  ":",  "'",  "\"",  ",",
                "<",  ">",  ".",  "?",  "/",
                //中文字符
                "·",  "！",  "￥",  "……",  "（",
                "）",  "——",  "【",  "】",  "、",
                "；",  "：",  "”",  "‘",  "《",
                "，",  "》",  "。" ,  "？",  "、",
                //前后带空格的，看查询中不带空格能查出来吗?其实最好能查出来，前后的空格希望可以trim掉
                " a4d4d18741a8",
                "a4d4d18741a8 "
        };
    }

    //version建议做成下拉框，减少出错
    @DataProvider(name = "CASE_VERSION")
    public Object[] createInvalidVersion() {

        return new String[] {
                "1.1.1.11."
        };
    }

    @DataProvider(name = "BAD_USER_ID_REG")
    public Object[] createBadUseridReg() {

        return new String[] {
                "",  " ",  "嗨",
                //英文字符
                "~",  "！", "@",  "#",  "$",
                "%",  "^",  "&",  "*",  "(",
                ")",  "-",  "+",  "=",  "{",
                "}",  "[",  "]", "|", "\\",
                ";",  ":",  "'",  "\"",  ",",
                "<",  ">",  ".",  "?",  "/",
                //中文字符
                "·",  "！",  "￥",  "……",  "（",
                "）",  "——",  "【",  "】",  "、",
                "；",  "：",  "”",  "‘",  "《",
                "，",  "》",  "。" ,  "？",  "、",
                //前后带空格的，看查询中不带空格能查出来吗?其实最好能查出来，前后的空格希望可以trim掉
                " badUserId",
                "badUserId "
        };
    }

    @DataProvider(name = "BAD_USER_ID_REQ")
    public Object[] createBadUseridReq() {

        return new String[] {
                "",  " ",  "嗨",
                //英文字符
                "~",  "！", "@",  "#",  "$",
                "%",  "^",  "&",  "*",  "(",
                ")",  "-",  "+",  "=",  "{",
                "}",  "[",  "]", "|", "\\",
                ";",  ":",  "'",  "\"",  ",",
                "<",  ">",  ".",  "?",  "/",
                //中文字符
                "·",  "！",  "￥",  "……",  "（",
                "）",  "——",  "【",  "】",  "、",
                "；",  "：",  "”",  "‘",  "《",
                "，",  "》",  "。" ,  "？",  "、",
                //前后带空格的，看查询中不带空格能查出来吗?其实最好能查出来，前后的空格希望可以trim掉
                "badUserId"
        };
    }

    @DataProvider(name = "GOOD_RESULT_NUM")
    public Object[] createGoodResultNum1() {
        return new Integer[] {0,1,5,10};
    };

    @DataProvider(name = "BAD_RESULT_NUM")
    public Object[] createBadResultNum1() {
        return new String[] {
                "",  " ",  "嗨", "badResultNum",
                //英文字符
                "~",  "！", "@",  "#",  "$",
                "%",  "^",  "&",  "*",  "(",
                ")",  "-",  "+",  "=",  "{",
                "}",  "[",  "]", "|", "\\",
                ";",  ":",  "'",  "\"",  ",",
                "<",  ">",  ".",  "?",  "/",
                //中文字符
                "·",  "！",  "￥",  "……",  "（",
                "）",  "——",  "【",  "】",  "、",
                "；",  "：",  "”",  "‘",  "《",
                "，",  "》",  "。" ,  "？",  "、"
        };
    };
}
