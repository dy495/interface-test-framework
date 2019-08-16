package com.haisheng.framework.testng.custemorGateTest;

/**
 * 线下消费者接口测试
 * @author Shine
 */
public class ClassifiedCharacterManage {
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    private LogMine logMine = new LogMine(logger);
//    private ICaseDao caseDao      = null;
//    private SqlSession sqlSession = null;
//    private String UID            = "uid_e0d1ebec";
//    private String APP_ID         = "a4d4d18741a8";
//    private String SHOP_ID        = "8";
//    private BASE64Encoder encoder = new sun.misc.BASE64Encoder();
//    private String vipGroup = "vipGroup";
//    private String vipUser = "00000";
//    private String queryGrpGrp = "queryGrpGrp";//测试queryGroupTestIsSuccess专用组
//    private ApiResponse apiResponse = null;
//    private static String fromGrpName = "fromGrp";
//    private static String fromUserId = "fromUser";
//    private static String toGrpName = "toGrp";
//    private static String toUserId = "toUser";
//    private static String isCheckSame = "true";
//
//    String ROUTER_REGISTER = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
//    String ROUTER_QUERY_GROUP = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
//    String ROUTER_QUERY_USER = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
//    String ROUTER_SEARCH_FACE = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
//    String ROUTER_DELETE_USER = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
//    String ROUTER_DELETE_FACE = "/scenario/gate/SYSTEM_DELETE_FACE/v1.0";
//    String ROUTER_CHANGE_USER = "/scenario/gate/SYSTEM_CHANGE_USER/v1.0";
//
//
//    String failReason = "";
//    private String [] faceIdArray = {
//            "0c77e3b9a2522e90dbf415baa0e26a69",
//            "6fdb50aa3d88f30fea6cdc90145a2e47",
//            "707fbcdffba8669f9f7b9aa34a51af79",
//            "79d528090d67a7944d352bcc913ea581",
//            "79d528090d67a7944d352bcc913ea581"
//    };
//    private String[] picPathArr =
//            { "src/main/resources/test-res-repo/customer-gateway/1.jpg",
//                    "src/main/resources/test-res-repo/customer-gateway/2.jpg",
//                    "src/main/resources/test-res-repo/customer-gateway/3.jpg",
//                    "src/main/resources/test-res-repo/customer-gateway/4.jpg",
//                    "src/main/resources/test-res-repo/customer-gateway/5.jpg"};
//    private String vipPic = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
//    private String[] userIdArr =
//            {
//                    "user1", "user2", "user3", "user4", "user5"
//            };
//    //1.jpg是通用的默认图片
//
//    public ApiResponse registerFaceNormal(String grpName, String userId, String picPath,int expectCode) throws Exception{
//        logger.info("------------------------registerFace---------------------------------------");
//        ImageUtil imageUtil = new ImageUtil();
//        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
//        String json =
//                "{\"group_name\":\"" + grpName +"\"," +
//                "\"user_id\":\""+userId+"\"," +
//                "\"is_quality_limit\":\"false\"," +
//                "\"pic_url\":\"@0\"" +
//                "}";
//        apiResponse = sendRequest(ROUTER_REGISTER, resource,json);
//        checkCode(apiResponse,ROUTER_REGISTER,expectCode);
//
//        return apiResponse;
//    }
//
//    @Test
//    public void registerFaceTestDS() throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "验证注册人脸的数据结构";
//        failReason = "";
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.SUCCESS);
//        String response = expect;
//
//        try {
//            int expectCode = StatusCode.SUCCESS;
//            apiResponse = registerFaceNormal(vipGroup,vipUser,vipPic,expectCode);
//            checkRegisterDS(apiResponse,ROUTER_REGISTER);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, "invalid grp name: ", response, expect, result);
//        }
//    }
//
//    @Test (dataProvider = "BAD_GRP_NAME",priority = 1)
//    public void registerFaceTestBadGrpName(String grpName) throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "注册人脸--验证无效组名是否注册成功。groupName：" + grpName;
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = registerFaceNormal(grpName, vipUser,vipPic,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, "invalid grp name: " + grpName, response, expect, result);
//        }
//    }
//
//    @Test (dataProvider = "BAD_USER_ID", priority = 1)
//    public void registerFaceTestBadUserId(String userId) throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "注册人脸--验证无效的userId。userId：" + userId;
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = registerFaceNormal(vipGroup,userId,vipPic,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, "invalid user id: " + userId, response, expect, result);
//        }
//    }
//
//    @Test (priority = 1)
//    public void registerFaceTestShopUser() throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "注册人脸--验证shop_user参数。";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.SUCCESS);
//        String response = expect;
//
//        try {
//            int expectCode = StatusCode.SUCCESS;
//            apiResponse = registerFaceNormal(vipGroup,vipUser,vipPic,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, "json", response, expect, result);
//        }
//    }
//
////------------------------------the above are the cases for register face-------------------------------------------------
//// -----------------------------here are the cases for query group-----------------------------------------------
//
//    @Test(dataProvider = "BAD_UID",priority = 2)
//    public void TestUIDWithoutEmpty(String uid) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "测试无效的UID。UID: " + uid;
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.UN_AUTHORIZED);
//        String response = expect;
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+vipGroup+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.UN_AUTHORIZED;
//            apiResponse = doTestHeadPara(ROUTER_QUERY_GROUP, resource, json, uid,APP_ID,SdkConstant.API_VERSION);
//            checkCode(apiResponse,ROUTER_QUERY_GROUP,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test(dataProvider = "EMPTY_PARA",priority = 2)
//    public void TestUIDEmpty(String uid) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+vipGroup+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = doTestHeadPara(ROUTER_QUERY_GROUP, resource, json, uid, APP_ID,SdkConstant.API_VERSION);;
//            checkCode(apiResponse,ROUTER_QUERY_GROUP,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test(dataProvider = "BAD_APPID",priority = 2)
//    public void TestAppidWithoutEmpty(String appid) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.UN_AUTHORIZED);
//        String response = expect;
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+vipGroup+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.UN_AUTHORIZED;
//            apiResponse = doTestHeadPara(ROUTER_QUERY_GROUP, resource, json, UID, appid,SdkConstant.API_VERSION);;
//            checkCode(apiResponse,ROUTER_QUERY_GROUP,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test(dataProvider = "EMPTY_PARA",priority = 2)
//    public void TestAppidEmpty(String appid) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+vipGroup+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = doTestHeadPara(ROUTER_QUERY_GROUP, resource, json, UID, appid,SdkConstant.API_VERSION);;
//            checkCode(apiResponse,ROUTER_QUERY_GROUP,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test(dataProvider = "BAD_VERSION")
//    public void queryGroupTestBadVersion(String version) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+vipGroup+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = doTestHeadPara(ROUTER_QUERY_GROUP, resource, json, UID, APP_ID, version );
//            checkCode(apiResponse,ROUTER_QUERY_GROUP,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test(dataProvider = "GOOD_VERSION")
//    public void queryGroupTestGoodVersion(String version) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.SUCCESS);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+vipGroup+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.SUCCESS;
//            apiResponse = doTestHeadPara(ROUTER_QUERY_GROUP, resource, json, UID, APP_ID, version );
//            checkCode(apiResponse,ROUTER_QUERY_GROUP,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//    /**
//     * @Description:  2.4 测试组名（用的是查询和删除专用的dataProvider）
//     * @Param: [grpName]
//     * @return: void
//     * @Author: Shine
//     * @Date: 2019/4/9
//     */
//    @Test(dataProvider = "BAD_GRP_NAME",priority = 2)
//    public void queryGroupTestGroupName(String grpName) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+grpName+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test
//    public void QueryGroupWithNewGroup() throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "QueryGroupWithNewGroup";
//        String expect = String.valueOf(StatusCode.SUCCESS);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
//        String[] resource = new String[]{};
//        String newGroup = String.valueOf(System.currentTimeMillis());
//        String json =
//                "{" +
//                "\"group_name\":\""+newGroup+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.SUCCESS;
//            //1、register face
//            registerFaceNormal(newGroup,vipUser,vipPic);
//            //2、query group
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test(priority = 2)
//    public void queryGroupTestIsSuccess() throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "queryGroupTestIsSuccess";
//        String expect = String.valueOf(StatusCode.SUCCESS);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
//        try {
//            for (int i= 0;i<userIdArr.length;i++){
//                registerFaceNormal(queryGrpGrp,userIdArr[i],vipPic);
//            }
//            int expectCode = StatusCode.SUCCESS;
//            apiResponse = queryGroupNormal(queryGrpGrp);
//            checkCode(apiResponse,router,expectCode);
//            int userNum = checkQueryGrpResult(apiResponse,router);
//            if(userNum!=5){
//                String msg = "Query user failed! The number is wrong!" +
//                        "groupName: " + queryGrpGrp;
//                throw new Exception(msg);
//            }
//
//            for (int i= 0;i<userIdArr.length;i++){
//                deleteUserNormal(queryGrpGrp,userIdArr[i]);
//            }
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, "", response, expect, result);
//        }
//
//        for (int i= 0;i<userIdArr.length;i++){
//            registerFaceNormal(queryGrpGrp,userIdArr[i],vipPic,);
//        }
//    }
//
//    public ApiResponse queryGroupNormal(String grpName) throws Exception {
//
//        logger.info("queryGroupNormal ");
//        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+grpName+"\"" +
//                "}";
//        return sendRequest(router, resource, json);
//    }
//
//    @Test
//    public void queryGroupTestDS() throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "queryGroupTestDS";
//        String expect = String.valueOf(StatusCode.SUCCESS);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+vipGroup+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.SUCCESS;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    //----------------------the above are the cases of query group----------------------------------------------------------
//    //----------------------here are the cases of query user----------------------------------------------------------
//
//    @Test(dataProvider = "BAD_GRP_NAME",priority = 3)
//    public void queryUserTestGroupName(String grpName) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "queryUserTestGroupName-" + grpName;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+grpName+"\"," +
//                "\"user_id\":\""+vipUser+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test (dataProvider = "BAD_USER_ID",priority = 3)
//    public void queryUserTestBadUserId(String badUserId) throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "queryUserTestBadUserId-" + badUserId;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+vipGroup+"\"," +
//                "\"user_id\":\""+badUserId+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test(priority = 3)
//    public void queryUserWithNewGroup () throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "queryUserWithNewGroup ";
//        String expect = String.valueOf(StatusCode.SUCCESS);
//        String response = expect;
//        String newGroup = String.valueOf(System.currentTimeMillis());
//        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+newGroup+"\"," +
//                "\"user_id\":\""+vipUser+"\"" +
//                "}";
//        try {
//            //1、register face
//            registerFaceNormal(newGroup,vipUser,vipPic);
//            //2、query user
//            int expectCode = StatusCode.SUCCESS;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    public HashMap queryUserWithResult(String grpName, String userId) throws Exception{
//        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+grpName+"\"," +
//                "\"user_id\":\""+userId+"\"" +
//                "}";
//        return doQueryUserWithResult(router, resource, json);
//    }
//
//    //--------------------the above are the cases of query user--------------------------------------------------------
//    //--------------------here are the cases of search face--------------------------------------------------------
//
//    @Test(dataProvider = "BAD_GRP_NAME",priority = 4)
//    public void searchFaceTestBadGroup(String grpName) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "searchFaceTestGroupName-" + grpName;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
//        ImageUtil imageUtil = new ImageUtil();
//        String[] resource = new String[]{imageUtil.getImageBinary(vipPic)};
//        String json =
//                "{" +
//                "\"group_name\":\""+grpName+"\"," +
//                "\"pic_url\":\"@0\"," +
//                "\"result_num\":1" +
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test(priority = 4)
//    public void SearchFaceWithNewGroup () throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "SearchFaceWithNewGroup";
//        String expect = String.valueOf(StatusCode.SUCCESS);
//        String response = expect;
//        String newGroup = String.valueOf(System.currentTimeMillis());
//        String picPath = "src/main/resources/test-res-repo/customer-gateway/NewGroup.jpg";
//        String userId = newGroup;
//        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
//        ImageUtil imageUtil = new ImageUtil();
//        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
//        String json =
//                "{" +
//                "\"group_name\":\""+newGroup+"\"," +
//                "\"pic_url\":\"@0\"," +
//                "\"result_num\":1" +
//                "}";
//
//        try {
//            //1、register face
//            registerFaceNormal(userId,newGroup,picPath);
//            //2、search face
//            int expectCode = StatusCode.SUCCESS;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    public void searchFaceNormal(String grpName,String picPath) throws Exception{
//        logger.info("searchFaceNormal");
//        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
//        ImageUtil imageUtil = new ImageUtil();
//        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
//        String json =
//                "{" +
//                "\"group_name\":\""+grpName+"\"," +
//                "\"pic_url\":\"@0\"," +
//                "\"result_num\":1" +
//                "}";
//
//        int expectCode = StatusCode.SUCCESS;
//        apiResponse = sendRequest(router, resource, json);
//        checkCode(apiResponse,router,expectCode);
//    }
//
//    @Test(priority = 4)
//    public void checkFaceURL () throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "checkFaceURL";
//        String expect = String.valueOf(StatusCode.SUCCESS);
//        String response = expect;
//        String grpName = "faceUrlGrp";
//        String userId = "faceUrlUser";
//        String picPath = "src/main/resources/test-res-repo/customer-gateway/compareUrl.jpg";
//        try {
//            //1、register face
//            registerFaceNormal(grpName,userId,picPath);
//            //2、extract the faceUrl of query user
//            HashMap<String,String> queryResult = queryUserWithResult(grpName,userId);
//            String userUrl = queryResult.get("faceUrlFirst");
//            logger.info("the faceUrl of query user is："+userUrl);
//            //3、extract the faceUrl of search face
//            HashMap<String,String> searchFaceResult = searchFaceWithResult(grpName,picPath);
//            String faceUrl = searchFaceResult.get("firstFaceUrl");
//            logger.info("the faceUrl of search face is："+faceUrl);
//            //4、compare two urls
//            if(userUrl==null&&"".equals(userUrl)){
//                String message = "the faceUrl of query user is empty.";
//                throw new Exception(message);
//            }
//            if(faceUrl==null&&"".equals(faceUrl)){
//                String message = "the faceUrl of search face is empty.";
//                throw new Exception(message);
//            }
//
//            if(!userUrl.equals(faceUrl)){
//                String msg = "two faceUrls are different ！"+
//                        "groupName: " + grpName+
//                        "userId: " + userId+
//                        ". the faceUrl of query user is:"+userUrl+
//                        ", the faceUrl of search face:"+faceUrl;
//                throw new Exception(msg);
//            }
//            //5、delete face ,clean up the environment
//            deleteUserNormal(grpName,userId);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, "three jsons", response, expect, result);
//        }
//    }
//
//    public HashMap<String, String> searchFaceWithResult(String grpName, String picPath) throws Exception{
//        logger.info("searchFaceWithResult ");
//        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
//        ImageUtil imageUtil = new ImageUtil();
//        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
//        String json =
//                "{" +
//                "\"group_name\":\""+grpName+"\"," +
//                "\"pic_url\":\"@0\"," +
//                "\"result_num\":1" +
//                "}";
//        return  doSearchFaceWithResult(router, resource, json);
//    }
//
//    @Test(dataProvider = "GOOD_RESULT_NUM",priority = 4)
//    public void searchFaceTestGoodResultNum(int resultNum) throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "searchFaceTestGoodResultNum-" + resultNum;
//        String expect = String.valueOf(StatusCode.SUCCESS);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
//        String facePath = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
//        ImageUtil imageUtil = new ImageUtil();
//        String[] resource = new String[]{imageUtil.getImageBinary(facePath)};
//        String json =
//                "{" +
//                "\"group_name\":\"TestGroup\"," +
//                "\"pic_url\":\"@0\"," +
//                "\"result_num\":" + resultNum +
//                "}";
//        try {
//            int expectCode = StatusCode.SUCCESS;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test(dataProvider = "BAD_RESULT_NUM",priority = 4)
//    public void searchFaceTestBadResultNum(String badResultNum) throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "searchFaceTestBadResultNum-" + badResultNum;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
//        ImageUtil imageUtil = new ImageUtil();
//        String[] resource = new String[]{imageUtil.getImageBinary(vipPic)};
//        String json =
//                "{" +
//                "\"group_name\":\"TestGroup\"," +
//                "\"pic_url\":\"@0\"," +
//                "\"result_num\":" +"\"" + badResultNum + "\""+
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//    //----------------------the above are the cases of search face-------------------------------------------------
//    //----------------------here are the cases of delete user-----------------------------------------------------
//
//    @Test(dataProvider = "BAD_GRP_NAME",priority = 5)
//    public void deleteUserTestBadGrp(String grpName) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+grpName+"\"," +
//                "\"user_id\":\""+vipUser+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test (dataProvider = "BAD_USER_ID",priority = 5)
//    public void deleteUserTestBadUserId(String badUserId) throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "deleteUserTestBadUserId-" + badUserId;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+vipGroup+"\"," +
//                "\"user_id\":\""+badUserId+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    public void deleteUserNormal(String grpName,String userId) throws Exception{
//        logger.info("deleteUserNormal");
//        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+grpName+"\"," +
//                "\"user_id\":\""+userId+"\"" +
//                "}";
//        int expectCode = StatusCode.SUCCESS;
//        apiResponse = sendRequest(router, resource, json);
//        checkCode(apiResponse,router,expectCode);
//    }
//
//    @Test(priority = 5)
//    public void deleteUserTestReAdd() throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "deleteUserTestReAdd";
//        String expect = String.valueOf(StatusCode.SUCCESS);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
//        String msg;
//        String userId = String.valueOf(System.currentTimeMillis());
//
//        try {
//            //1、register
//            registerFaceNormal(vipGroup,userId,vipPic);
//            //2、query user
//            HashMap beforeDeleteResult = queryUserWithResult(vipGroup,userId);
//            int beforeDelete = Integer.parseInt((String)beforeDeleteResult.get("faceNum"));
//            //3、delete user
//            deleteUserNormal(vipGroup,userId);
//            //4、query user
//            HashMap afterDeleteResult = queryUserWithResult(vipGroup,userId);
//
//            int afterDelete = Integer.parseInt((String) afterDeleteResult.get("faceNum"));
//            if(beforeDelete==1&&afterDelete==0){
//                msg = "delete user succeeed!";
//                logger.info(msg);
//            }else{
//                msg = "delete user failed!"
//                        + "group: " + vipGroup
//                        + "userid: " + userId;
//                throw new Exception(msg);
//            }
//            //5、register
//            registerFaceNormal(vipGroup,userId,vipPic);
//            //6、query user
//            HashMap reAddResult = queryUserWithResult(vipGroup,userId);
//            int reAdd = Integer.parseInt((String)reAddResult.get("faceNum"));
//            if(reAdd==1){
//                msg = "register successfully after delete user!";
//                logger.info(msg);
//            }else{
//                msg = "register failed after delete user!"
//                        + "group: " + vipGroup
//                        + "userid: " + userId;
//                throw new Exception(msg);
//            }
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            String requestStep = "register-query-deleteUser-query-reg-query";
//            saveCaseToDb(caseName, requestStep, response, expect, result);
//        }
//    }
//
//    //--------------------------------above are the cases of delete user-----------------------------------
//    //--------------------------------here are the cases of delete face-----------------------------------
//
//    @Test(dataProvider = "BAD_GRP_NAME",priority = 6)
//    public void deleteFaceTestBadGrp(String grpName) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "deleteFaceTestBadGrp-" + grpName;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_DELETE_FACE/v1.0";
//        String[] resource = new String[]{};
//        String faceId = "ee4a2829872770e8e1ee8e1f82e3324a";
//        String json =
//                "{" +
//                "\"group_name\":\""+grpName+"\"," +
//                "\"user_id\":\""+vipUser+"\"," +
//                "\"face_id\":\""+faceId+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    @Test (dataProvider = "BAD_USER_ID",priority = 6)
//    public void deleteFaceTestBadUserId(String badUserId) throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "deleteFaceTestBadUserId-" + badUserId;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_DELETE_FACE/v1.0";
//        String[] resource = new String[]{};
//        String faceId = "ee4a2829872770e8e1ee8e1f82e3324a";
//        String json =
//                "{" +
//                "\"group_name\":\""+vipGroup+"\"," +
//                "\"user_id\":\""+badUserId+"\"," +
//                "\"face_id\":\""+faceId+"\"" +
//                "}";
//        try {
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, json, response, expect, result);
//        }
//    }
//
//    public void deleteFaceNormal(String grpName, String userId, String faceId) throws Exception{
//        logger.info("deleteFaceNormal ");
//        String router = "/scenario/gate/SYSTEM_DELETE_FACE/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                "\"group_name\":\""+grpName+"\"," +
//                "\"user_id\":\""+userId+"\"," +
//                "\"face_id\":\""+faceId+"\"" +
//                "}";
//        int expectCode = StatusCode.SUCCESS;
//        apiResponse = sendRequest(router, resource, json);
//        checkCode(apiResponse,router,expectCode);
//    }
//
//    /**
//     * @Description: 6.4 特殊人脸删除，测试多个功能。
//     *   （注册-查询-删除一张-查询-全部删除-再次删除-注册-查询）
//     *     此case可以测试（1）删除一张是否成功，
//     *                  （2）以及删除多张是否成功
//     *                  （3）删除后是否可以重新注册
//     *                  （4）是否可以删除两次
//     * @Param: []
//     * @return: void
//     * @Author: Shine
//     * @Date: 2019/4/9
//     */
//    @Test(priority = 6)
//    public void deleteFaceTestReAdd () throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "deleteFaceTestReAdd ";
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        int faceIdArrLen = faceIdArray.length;
//        String userId = String.valueOf(System.currentTimeMillis());
//        String msg;
//        try {
//            //1、register five pictures
//            for(int i = 0;i<picPathArr.length;i++){
//                registerFaceNormal(vipGroup,userId,picPathArr[i]);
//            }
//            //2、query user before delete face.
//            HashMap beforeDeleteResult = queryUserWithResult(vipGroup,userId);
//            String faceIdConcat = (String) beforeDeleteResult.get("faceIdConcat");
//            int index;
//            for(index = 0;index<faceIdArrLen;index++){
//                if(!faceIdConcat.contains(faceIdArray[index])){
//                    msg = "search face failed!"
//                            + "group: " + vipGroup
//                            + "userid: " + userId;
//                    throw new Exception(msg);
//                }
//            }
//            //3、delete one picture
//            deleteFaceNormal(vipGroup,userId,faceIdArray[0]);
//            //4、query user
//            HashMap afterDeleteOneResult = queryUserWithResult(vipGroup,userId);
//            String AfterDeleteOnefaceIdConcat = (String) afterDeleteOneResult.get("faceIdConcat");
//            if(AfterDeleteOnefaceIdConcat.contains(faceIdArray[0])){
//                msg = "delete one picture failed!"
//                        + "group: " + vipGroup
//                        + "userid: " + userId
//                        + "faceId: " + faceIdArray[0];
//                throw new Exception(msg);
//            }
//
//            //5、delete all pictures
//            for(int i = 0;i<faceIdArrLen;i++){
//                deleteFaceNormal(vipGroup,userId,faceIdArray[i]);
//            }
//            //6、query user
//            HashMap afterDeleteAllResult = queryUserWithResult(vipGroup,userId);
//            String AfterDeleteAllfaceIdConcat = (String) afterDeleteAllResult.get("faceIdConcat");
//            if(!("".equals(AfterDeleteAllfaceIdConcat)||AfterDeleteAllfaceIdConcat!=null)){
//                msg = "delete all pictures failed!"
//                        + "group: " + vipGroup
//                        + "userid: " + userId;
//                throw new Exception(msg);
//            }
//            //7、delete face again
//            for(int i = 0;i<faceIdArrLen;i++){
//                deleteFaceNormal(vipGroup,userId,faceIdArray[i]);
//            }
//            //8、register again
//            for(int i = 0;i<picPathArr.length;i++){
//                registerFaceNormal(vipGroup,userId,picPathArr[i]);
//            }
//
//            //9、query user
//            HashMap reAddResult = queryUserWithResult(vipGroup,userId);
//            String reAddfaceIdConcat = (String) reAddResult.get("faceIdConcat");
//            for(index = 0;index<faceIdArrLen;index++){
//                if(!reAddfaceIdConcat.contains(faceIdArray[index])){
//                    msg = "can't register again after delete face!"
//                            + "group: " + vipGroup
//                            + "userid: " + userId;
//                    throw new Exception(msg);
//                }
//            }
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, "", response, expect, result);
//        }
//    }
//
//    public void changeUserNormal(String shopId,String fGrp,String fUser,String toGrp,String toUser,String isCheckSame) throws Exception{
//        logger.info("changeUserNormal");
//        String router = "/scenario/gate/SYSTEM_CHANGE_USER/v1.0";
//        String[] resource = new String[]{};
//        String json =
//                "{"+
//                        "\"shop_id\":\"" + shopId +"\"," +
//                        "\"from_group_name\":\""+fGrp+"\"," +
//                        "\"from_user_id\":\""+fUser+"\"," +
//                        "\"to_group_name\":\""+toGrp+"\"," +
//                        "\"to_user_id\":\""+toUser+"\"," +
//                        "\"is_check_same\":\""+isCheckSame+"\"" +
//                        "}";
//        int expectCode = StatusCode.SUCCESS;
//        apiResponse = sendRequest(router, resource, json);
//        checkCode(apiResponse,router,expectCode);
//    }
//
//    @Test
//    public void changeUserTestResult() throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "TestUIDEmpty";
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        try {
//            registerFaceNormal(fromGrpName,fromUserId,vipPic);
////       registerFaceNormal(toGrpName,toUserId,vipPic);
//            deleteUserNormal(toGrpName,toUserId);
//            changeUserNormal(SHOP_ID,fromGrpName,fromUserId,toGrpName,toUserId,isCheckSame);
//            queryUserWithResult(fromGrpName,fromUserId);
//            queryUserWithResult(toGrpName,toUserId);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, "", response, expect, result);
//        }
//    }
//
//    @Test(dataProvider = "CHNG_USER_MISS_PARA")
//    public void changeUserTestMissPara(String json) throws Exception{
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "计算实时人物列表的uv识别率";
//        failReason = "";
//        int step = 0;
//
//        boolean result = true;
//        String caseName = "changeUserTestMissPara-" + json;
//        String expect = String.valueOf(StatusCode.BAD_REQUEST);
//        String response = expect;
//        String router = "/scenario/gate/SYSTEM_CHANGE_USER/v1.0";
//        String[] resource = new String[]{};
//        try {
//            registerFaceNormal(fromGrpName,fromUserId,vipPic);
//            registerFaceNormal(toGrpName,toUserId,vipPic);
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = sendRequest(router, resource, json);
//            checkCode(apiResponse,router,expectCode);
//        } catch (Exception e) {
//            result = false;
//            response = e.toString();
//            //throw exception to case running job, then user can get details of failure
//            throw e;
//        } finally {
//            saveCaseToDb(caseName, "", response, expect, result);
//        }
//    }
//    //-----------------------------the above are the cases of change user-------------------------------------
//
//    private ApiResponse sendRequest(String router, String[] resource, String json) throws Exception {
//        logMine.logStep("Send request only！");
//        try {
//            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
//            String requestId = UUID.randomUUID().toString();
//            ApiRequest apiRequest = new ApiRequest.Builder()
//                    .uid(UID)
//                    .appId(APP_ID)
//                    .requestId(requestId)
//                    .version(SdkConstant.API_VERSION)
//                    .router(router)
//                    .dataResource(resource)
//                    .dataBizData(JSON.parseObject(json))
//                    .build();
//
//            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
//            apiResponse = apiClient.doRequest(apiRequest);
//            logMine.printImportant(JSON.toJSONString(apiRequest));
//            logMine.printImportant(JSON.toJSONString(apiResponse));
//        } catch (Exception e) {
//            throw e;
//        }
//        return apiResponse;
//    }
//
//    public void checkCode(ApiResponse apiResponse,String router,int expectCode) throws Exception{
//        try {
//            String requestId = apiResponse.getRequestId();
//            if (apiResponse.getCode() != expectCode) {
//                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
//                        "actual code: " + apiResponse.getCode() + " expect code: " + expectCode+ ".";
//                throw new Exception(msg);
//            }
//        }catch(Exception e){
//            throw e;
//        }
//    }
//
//    public void checkRegisterDS(ApiResponse apiResponse,String router) throws Exception{
//        try {
//            String requestId = apiResponse.getRequestId();
//            String responseStr = JSON.toJSONString(apiResponse);
//            JSONObject resJson = JSON.parseObject(responseStr);
//
//            JSONObject dataJsonObject = resJson.getJSONObject("data");
//            if(dataJsonObject.size()!=3){
//                String message = "The number of columns that returned in the system is not 3.";
//                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
//                throw new Exception(message);
//            }
//            String age = dataJsonObject.getString("age");
//            String isMale = dataJsonObject.getString("is_male");
//            String axis = dataJsonObject.getString("axis");
//
//            if(age==null||age==null||isMale==null||axis==null){
//                String message = "The columns that are expected to be returned do not match the columns actually returned in the system.";
//                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
//                throw new Exception(message);
//            }
//        }catch(Exception e){
//            throw e;
//        }
//    }
//
//    public int checkQueryGrpResult(ApiResponse apiResponse,String router) throws Exception{
//        int len = 0;
//        try {
//            String requestId = apiResponse.getRequestId();
//            JSONObject jsonObjectData = (com.alibaba.fastjson.JSONObject) apiResponse.getData();
//            com.alibaba.fastjson.JSONArray jsonArrayPerson = jsonObjectData.getJSONArray("person");
//            if(jsonArrayPerson!=null){
//                len = jsonArrayPerson.size();
//            }
//            String aa = JSON.toJSONString(apiResponse);
//            logMine.printImportant(aa);
//            if(! apiResponse.isSuccess()) {
//                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
//                throw new Exception(msg);
//            }
//        }catch(Exception e){
//            throw e;
//        }
//        return len;
//    }
//
//    private ApiResponse doTestHeadPara(String router, String[] resource, String json,String uid,String appid,String version) throws Exception {
//        logMine.logStep("Test invalid head para!");
//        try {
//            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
//            String requestId = UUID.randomUUID().toString();
//            ApiRequest apiRequest = new ApiRequest.Builder()
//                    .uid(uid)
//                    .appId(appid)
//                    .requestId(requestId)
//                    .version(version)
//                    .router(router)
//                    .dataResource(resource)
//                    .dataBizData(JSON.parseObject(json))
//                    .build();
//
//            // client 请求
//            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
//            apiResponse = apiClient.doRequest(apiRequest);
//            logMine.printImportant(JSON.toJSONString(apiRequest));
//            logMine.printImportant(JSON.toJSONString(apiResponse));
//        } catch (Exception e){
//            throw e;
//        }
//        return apiResponse;
//    }
//
//    /**
//     * @Description:  1、特殊人物查询，返回查询结果(faceIdFirst,faceUrlFirst,faceIdConcat,faceUrlConcat，faceNum)
//     * @Param: [router, resource, json]
//     * @return: java.util.HashMap<java.lang.String,java.lang.String>
//     * @Author: Shine
//     * @Date: 2019/4/9
//     */
//    private HashMap<String, String> doQueryUserWithResult(String router, String[] resource, String json) throws Exception {
//        logMine.logStep("Search user with result!");
//        String faceUrlFirst = "", faceIdFirst = "", faceUrlConcat = "", faceIdConcat = "";
//        //如果不初始化的话，直接put会报错，可怕！
//        HashMap<String, String> hm = new HashMap<>();
//        try {
//            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
//            String requestId = UUID.randomUUID().toString();
//            ApiRequest apiRequest = new ApiRequest.Builder()
//                    .uid(UID)
//                    .appId(APP_ID)
//                    .requestId(requestId)
//                    .version(SdkConstant.API_VERSION)
//                    .router(router)
//                    .dataResource(resource)
//                    .dataBizData(JSON.parseObject(json))
//                    .build();
//
//            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
//            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
//            com.alibaba.fastjson.JSONObject jsonObjectData = (com.alibaba.fastjson.JSONObject) apiResponse.getData();
//            com.alibaba.fastjson.JSONArray jsonArrayFaces = jsonObjectData.getJSONArray("faces");
//            int len = 0;
//            //len = jsonArrayFaces.size();(如果jsonArrayFaces是null的话，会报空指针错)
//            if(jsonArrayFaces!=null){
//                len = jsonArrayFaces.size();
//            }
//            for(int i = 0; i<len;i++){
//                faceUrlFirst = jsonArrayFaces.getJSONObject(0).getString("face_url");
//                faceIdFirst = jsonArrayFaces.getJSONObject(0).getString("face_id");
//
//                String faceUrl = jsonArrayFaces.getJSONObject(i).getString("face_url");
//                String faceId = jsonArrayFaces.getJSONObject(i).getString("face_id");
//
//                faceUrlConcat = faceUrlConcat.concat(faceUrl);
//                faceIdConcat = faceIdConcat.concat(faceId);
//            }
//            hm.put("faceNum",String.valueOf(len));
//            hm.put("faceIdFirst",faceIdFirst);
//            hm.put("faceUrlFirst",faceUrlFirst);
//            hm.put("faceIdConcat",faceIdConcat);
//            hm.put("faceUrlConcat",faceUrlConcat);
//            logMine.printImportant(JSON.toJSONString(apiResponse));
//            if(! apiResponse.isSuccess()) {
//                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
//                throw new Exception(msg);
//            }
//        } catch (Exception e) {
//            throw e;
//        }
//        return hm;
//    }
//
//    /**
//     * @Description:  1、特殊人脸查询，返回查询结果（faceNum,firstFaceUrl,faceUrlConcat）
//     * @Param: [router, resource, json]
//     * @return: java.util.HashMap<java.lang.String,java.lang.String>
//     * @Author: Shine
//     * @Date: 2019/4/9
//     */
//    private HashMap<String,String> doSearchFaceWithResult(String router, String[] resource, String json) throws Exception {
//        logMine.logStep("Search face with result!");
//        HashMap<String,String> hm= new HashMap<>();
//        String firstFaceUrl = "";
//        String faceUrlConcat = "";
//
//        try {
//            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
//            String requestId = UUID.randomUUID().toString();
//            ApiRequest apiRequest = new ApiRequest.Builder()
//                    .uid(UID)
//                    .appId(APP_ID)
//                    .requestId(requestId)
//                    .version(SdkConstant.API_VERSION)
//                    .router(router)
//                    .dataResource(resource)
//                    .dataBizData(JSON.parseObject(json))
//                    .build();
//
//            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
//            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
//            com.alibaba.fastjson.JSONObject jstr = (com.alibaba.fastjson.JSONObject) apiResponse.getData();
//            com.alibaba.fastjson.JSONArray jsonArrayFaces = jstr.getJSONArray("faces");
//            int len = jsonArrayFaces.size();
//            for(int i = 0; i<len;i++){
//                com.alibaba.fastjson.JSONArray jsonArraySimilar_faces = jsonArrayFaces.getJSONObject(i).getJSONArray("similar_faces");
//                for(int j=0;j<jsonArraySimilar_faces.size();j++){
//                    com.alibaba.fastjson.JSONObject jsonObjectfirstFace = jsonArraySimilar_faces.getJSONObject(0);
//                    com.alibaba.fastjson.JSONObject jsonObjectFace = jsonArraySimilar_faces.getJSONObject(j);
//                    firstFaceUrl = jsonObjectfirstFace.getString("face_url");
//                    String FaceUrl = jsonObjectFace.getString("face_url");
//                    faceUrlConcat = faceUrlConcat.concat(FaceUrl);
//                }
//            }
//            hm.put("faceNum",String.valueOf(len));
//            hm.put("firstFaceUrl",firstFaceUrl);
//            hm.put("faceUrlConcat",faceUrlConcat);
//            logMine.printImportant(JSON.toJSONString(apiResponse));
//            if(! apiResponse.isSuccess()) {
//                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
//                throw new Exception(msg);
//            }
//        } catch (Exception e) {
//            throw e;
//        }
//        return hm;
//
//    }
//
//    //仅支持a-z,A-Z,0-9,_
//    @DataProvider(name = "BAD_GRP_NAME")
//    public Object[] createBadGrpName() {
//        return new String[] {
//                "", " ",
//                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*"
//        };
//    }
//
//    //仅支持a-z,A-Z,0-9,_-
//    @DataProvider(name = "BAD_USER_ID")
//    public Object[] createBadUserid() {
//
//        return new String[] {
//                "", " ",
//                //英文字符
//                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*",
//        };
//    }
//
//    @DataProvider(name = "BAD_UID")
//    public Object[] createBadUID() {
//
//        return new String[] {
//                //英文字符
//                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*",
//                " uid_e0d1ebec",
//                "uid_e0d1ebec "
//        };
//    }
//
//    @DataProvider(name = "BAD_APPID")
//    public Object[] createBadAppid() {
//
//        return new String[] {
//                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*",
//                " a4d4d18741a8",
//                "a4d4d18741a8 "
//        };
//    }
//
//    /**
//     * @Description:  version建议做成下拉框，减少出错
//     * @Param: []
//     * @return: java.lang.Object[]
//     * @Author: Shine
//     * @Date: 2019/4/9
//     */
//    @DataProvider(name = "BAD_VERSION")
//    public Object[] createBadVersion() {
//
//        return new String[] {
//                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*",
//                //特殊数字
//                "11",  "0",   "-1",  "0.1",
//                "2.2", "-0.1","-2.2",
//                "v1.1.1"
//        };
//    }
//
//    @DataProvider(name = "GOOD_VERSION")
//    public Object[] createGoodVersion() {
//
//        return new String[] {
//                "V1",  "1.0",  "v1.0", ""
//        };
//    }
//
//    @DataProvider(name = "GOOD_RESULT_NUM")
//    public Object[] createGoodResultNum() {
//        return new Integer[] {1,2,3,4,5,6,7,8,9,10};
//    }
//
//    @DataProvider(name = "BAD_RESULT_NUM")
//    public Object[] createBadResultNum() {
//        return new String[] {
//                "",  " ",  "badResultNum",
//                //英文字符
//                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*",
//                //特殊数字
//                "11",  "0",   "-1",  "0.1",
//                "2.2", "-0.1","-2.2", "1.0"
//        };
//    };
//
//    @DataProvider(name = "CHNG_USER_MISS_PARA")
//    public Object[] createChangeUserMissPara() {
//        return new String[] {
//                "{"+
//                        "\"from_group_name\":\""+fromGrpName+"\"," +
//                        "\"from_user_id\":\""+fromUserId+"\"," +
//                        "\"to_group_name\":\""+toGrpName+"\"," +
//                        "\"to_user_id\":\""+toUserId+"\"," +
//                        "\"is_check_same\":\""+isCheckSame+"\"" +
//                        "}",
//
//                "{"+
//                        "\"shop_id\":\"" + SHOP_ID +"\"," +
//                        "\"from_group_name\":\""+fromGrpName+"\"," +
//                        "\"to_group_name\":\""+toGrpName+"\"," +
//                        "\"to_user_id\":\""+toUserId+"\"," +
//                        "\"is_check_same\":\""+isCheckSame+"\"" +
//                        "}",
//
//                "{"+
//                        "\"shop_id\":\"" + SHOP_ID +"\"," +
//                        "\"from_group_name\":\""+fromGrpName+"\"," +
//                        "\"from_user_id\":\""+fromUserId+"\"," +
//                        "\"to_group_name\":\""+toGrpName+"\"," +
//                        "\"is_check_same\":\""+isCheckSame+"\"" +
//                        "}"
//        };
//    };
//
//
//
//    @DataProvider(name = "EMPTY_PARA")
//    public Object[] createEmptyPara() {
//        return new String[] {
//                "  "
//        };
//    };
//
//
//    private void saveCaseToDb(String caseName, String request, String response, String expect, boolean result) {
//
////        Case checklist = new Case();
////        List<Integer> listId = caseDao.queryCaseByName(ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE,
////                ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE,
////                caseName);
////        int id = -1;
////        if (listId.size() > 0) {
////            checklist.setId(listId.get(0));
////        }
////        checklist.setApplicationId(ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE);
////        checklist.setConfigId(ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE);
////        checklist.setCaseName(caseName);
////        checklist.setCanManualRun(true);
////        checklist.setRunByCi(true);
////        checklist.setEditTime(new Timestamp(System.currentTimeMillis()));
////        checklist.setQaOwner("廖祥茹");
////        checklist.setRequestData(request);
////        checklist.setResponse(response);
////        checklist.setExpect(expect);
////        if (result) {
////            checklist.setResult("PASS");
////        } else {
////            checklist.setResult("FAIL");
////        }
////        caseDao.insert(checklist);
////        sqlSession.commit();
//
//    }
//
//    @BeforeSuite
//    public void initial() {
//        logger.debug("initial");
//        SqlSessionFactory sessionFactory = null;
//        String resource = "configuration.xml";
//        try {
//            sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(
//                    resource));
//            sqlSession = sessionFactory.openSession();
//            caseDao = sqlSession.getMapper(ICaseDao.class);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    @AfterSuite
//    public void clean() {
//        logger.info("clean");
//        sqlSession.close();
//    }
}
