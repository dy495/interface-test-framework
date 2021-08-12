package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.CustomerInfo;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.PackFunction;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * @author : lxq
 * @date :  2020/05/30
 */

public class CrmSystemCase extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.PORSCHE_DAILY;
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
    CustomerInfo cstm = new CustomerInfo();
    PackFunction pf = new PackFunction();


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "吕雪晴";

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);

        logger.debug("crm: " + crm);
        crm.login(cstm.lxqgw, cstm.pwd);

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
     * 上传日常进场车牌
     * 接口说明：https://winsense.yuque.com/staff-qt5ptf/umvi00/mhinpu
     */
    @Test
    public void uploadEnterShopCarPlate() {
//        String carNum = "黑ABC1357";     //售前新，售后老（维修+保养）
//        String carNum = "鲁ABB1711";    //全新
//        String carNum = "浙ABC1711";    //售前老客，售后新客
//        String carNum = "京D738848";    //售前老客，售后新客
//        String carNum = "京ASD1235";    //售前老客，售后新客
//        String carNum = "京A081800";    //售前新客，售后新客
        //String carNum = "苏ZDH197";    //试驾车未注销

        //String carNum = "京A1ER19";    //试驾车已注销
//        String carNum = "苏G452282";    //试驾车已注销
//        String carNum = "苏G452282";    //试驾车已注销
        String carNum = "冀A2473";    //试驾车已注销
        String router = "/business/porsche/PLATE_UPLOAD/v1.0";
        //设备与日常环境的设置一致，不要修改
        String deviceId = "7709867521115136";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        String json = "{\"plate_num\":\"" + carNum + "\"," +
                "\"plate_pic\":\"@0\"," +
                "\"time\":\"" + System.currentTimeMillis() + "\"" +
                "}";
        try {
            crm.carUploadToDaily(router, deviceId, resource, json);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("日常入场车牌号上传");
        }
    }

    public void uploadEnterShopCarPlatex(String carNum) {

        String router = "/business/porsche/PLATE_UPLOAD/v1.0";
        //设备与日常环境的设置一致，不要修改
        String deviceId = "7709867521115136";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        String json = "{\"plate_num\":\"" + carNum + "\"," +
                "\"plate_pic\":\"@0\"," +
                "\"time\":\"" + System.currentTimeMillis() + "\"" +
                "}";
        try {
            crm.carUploadToDaily(router, deviceId, resource, json);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("日常入场车牌号上传");
        }
    }

    @Test
    public void heiping() {
        for (int i = 0; i < 2; i++) {
            String carNum = "京B1DF9" + i;    //试驾车已注销
            uploadEnterShopCarPlatex(carNum);
        }
    }

    /**
     * 上传日常离场车牌
     * 接口说明：https://winsense.yuque.com/staff-qt5ptf/umvi00/mhinpu
     */
    @Test
    public void uploadLeaveShopCarPlate() {
        String carNum = "苏ZDH396";
        String router = "/business/porsche/PLATE_UPLOAD/v1.0";
        //设备与日常环境的设置一致，不要修改
        String deviceId = "7724082825888768";
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        String json = "{\"plate_num\":\"" + carNum + "\"," +
                "\"plate_pic\":\"@0\"," +
                "\"time\":\"" + System.currentTimeMillis() + "\"" +
                "}";
        try {
            crm.carUploadToDaily(router, deviceId, resource, json);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("日常离场车牌号上传");
        }
    }


    /**
     * 上传赢识线上进场车牌
     * 接口说明：https://winsense.yuque.com/staff-qt5ptf/umvi00/mhinpu
     */
    @Test
    public void uploadOnlineEnterShopCarPlate() {
//        String carNum = "黑ABC1357";     //售前新，售后老（维修+保养）
//        String carNum = "鲁ABB1711";    //全新
//        String carNum = "浙ABC1711";    //售前老客，售后新客
        //String carNum = "京ASD1235";    //售前老客，售后新客
//        String carNum = "京A081800";    //售前新客，售后新客
        String carNum = "吉EJK0012";


        String router = "/business/porsche/PLATE_UPLOAD/v1.0";
        //设备与线上环境的设置一致，不要修改
        String deviceId = "7736789438301184";//线上设备
        String picPath = "src/main/resources/test-res-repo/pic/911_big_pic.jpg";
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        String json = "{\"plate_num\":\"" + carNum + "\"," +
                "\"plate_pic\":\"@0\"," +
                "\"time\":\"" + System.currentTimeMillis() + "\"" +
                "}";
        try {
            crm.carUploadToOnline(router, deviceId, resource, json);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("线上入场车牌号上传");
        }
    }


    /**
     * ====================我的客户======================
     */
    //----------------------查询--------------------
    @Test
    public void customerListSearchName() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(cstm.xszj, cstm.pwd);
            JSONObject obj = crm.customerListPC("", -1, "", "", 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_name = obj.getString("customer_name");

            //姓名查询
            int total = crm.customerListPC("", -1, search_name, "", 0, 0, 1, 50).getInteger("total");

            Preconditions.checkArgument(total >= 1, "无查询结果");
            for (int i = 0; i < total; i++) {
                JSONObject o = crm.customerListPC("", -1, search_name, "", 0, 0, 1, total).getJSONArray("list").getJSONObject(i);
                String name = o.getString("customer_name");
                Preconditions.checkArgument(name.contains(search_name), "查询结果与查询条件不一致");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("我的客户页面根据姓名查询");
        }

    }

    @Test
    public void customerListSearchPhone() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(cstm.xszj, cstm.pwd);
            //获取手机号
            JSONObject obj = crm.customerListPC("", -1, "", "", "2020-06-01", "2020-06-30", 1, 1).getJSONArray("list").getJSONObject(0);
            String search_phone = obj.getJSONArray("phones").getString(0);
            //查询
            JSONObject obj1 = crm.customerListPC("", -1, "", search_phone, 0, 0, 1, 1).getJSONArray("list").getJSONObject(0);
            String search_phone1 = obj1.getJSONArray("phones").getString(0);
            Preconditions.checkArgument(search_phone.equals(search_phone1), "查询结果与查询条件不一致");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的客户页面根据手机号查询");
        }

    }


    //    @Test
    public void customerListSearchDate() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            crm.login(cstm.xszj, cstm.pwd);
            String starttime = dt.getHistoryDate(0);
            String endtime = starttime;
            //查询
            JSONArray list = crm.customerListPC("", 1, "", "", starttime, endtime, 1, 50).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                String date = single.getString("service_date");
                Preconditions.checkArgument(date.equals(starttime) || date == null, "查询结果与查询条件不一致");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的客户页面根据日期查询");
        }

    }

    //----------------------展示--------------------

    @Ignore
    @Test
    public void customerListShowAll() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(cstm.lxqgw, cstm.pwd);
            //查询
            JSONArray list = crm.customerListPC("", -1, "", "", "", "", 1, 50).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);
                Preconditions.checkArgument(single.getString("belongs_sale_name").equals(cstm.lxqgw), "展示信息不正确");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("我的客户页面所属销售的顾客信息");
        }

    }

    //----------------------删除--------------------
//    @Ignore
//    @Test
//    public void customerListDel() {
//        logger.logCaseStart(caseResult.getCaseName());
//        Long customerid = -1L;
//        try {
//
//
//            long level_id = 7L;
//            String phone = "" + System.currentTimeMillis();
//            String name = phone;
//            String phone1 = phone.substring(3);
//            customerid = creatCust(name, phone);
//            //完成接待
//
//            //姓名+手机号查询
//            int total = crm.customerListPC("", -1, name, phone1, 0, 0, 1, 1).getInteger("total");
//            Preconditions.checkArgument(total == 1, "删除前查询，期待有一条记录，实际" + total);
//
//            //总经理登陆
//            crm.login(cstm.xszj, cstm.pwd);
//            //删除顾客
//            crm.customerDeletePC(customerid);
//
//            //销售顾问登陆
//            crm.login(cstm.lxqgw, cstm.pwd);
//            //再次查询应无结果
//            int total2 = crm.customerListPC("", -1, name, phone1, 0, 0, 1, 1).getInteger("total");
//            Preconditions.checkArgument(total2 == 0, "删除后查询，期待无结果，实际" + total);
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            crm.login(cstm.lxqgw, cstm.pwd);
//            saveData("我的客户页面删除后再查询");
//        }
//
//    }

    //@Test
    public void customerListDelInService() {
        logger.logCaseStart(caseResult.getCaseName());
        Long customerid = -1L;
        try {

            long level_id = 7L;
            String phone = "" + System.currentTimeMillis();
            String name = phone;
            customerid = creatCust(name, phone);
            //总经理登陆
            crm.login(cstm.xszj, cstm.pwd);
            //删除顾客
            int code = crm.customerDeletePCNotChk(customerid).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);
            crm.login(cstm.lxqgw, cstm.pwd);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("删除接待中客户");
        }

    }

//    @Ignore
//    @Test
//    public void customerListDelServiced() {
//        logger.logCaseStart(caseResult.getCaseName());
//        Long customerid = -1L;
//        try {
//
//            long level_id = 7L;
//            String phone = "" + System.currentTimeMillis();
//            String name = phone;
//            customerid = creatCust(name, phone);
//
//            //总经理登陆
//            crm.login(cstm.xszj, cstm.pwd);
//            //删除顾客
//            int code = crm.customerDeletePCNotChk(customerid).getInteger("code");
//            Preconditions.checkArgument(code == 1000, "删除失败");
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            crm.login(cstm.lxqgw, cstm.pwd);
//            saveData("删除已完成接待客户");
//        }
//
//    }


    /**
     * ====================创建账号======================
     */
    @Test
    public void addUserREname() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie, cstm.pwd);
            String userName = "" + System.currentTimeMillis();
            int roleId = 13;
            String userLoginName = userName;
            String userid = pf.createUserId(userLoginName, roleId);
            String phone2 = pf.genPhoneNum();
            //重复添加
            int code = crm.addUserNotChk(userName, userLoginName, phone2, cstm.pwd, roleId).getInteger("code");
            //删除账号
            crm.userDel(userid);
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("创建已存在账号");
        }
    }

    @Test
    public void addUserREphone() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie, cstm.pwd);
            String userName = "" + System.currentTimeMillis();
            String userLoginName = userName;
            int roleId = 13;
            String phone = pf.genPhoneNum();
            //创建用户
            JSONObject data = crm.userPage(1, 100);
            int total = data.getInteger("total");
            JSONArray list;
            if (total == 200) {
                throw new Exception("用户数量已达上线，case运行终止");
            } else {
                crm.addUser(userName, userName, phone, cstm.pwd, roleId, "", "");
                list = crm.userPage(1, 100).getJSONArray("list");
            }
            String userid = list.getJSONObject(0).getString("user_id"); //获取用户id
            String passwd = cstm.pwd;
            //重复添加
            int code = crm.addUserNotChk(userName + "1", userLoginName + "1", phone, passwd + "1", roleId).getInteger("code");
            //删除账号
            crm.userDel(userid);
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("使用已存在手机号创建账号");
        }
    }


    @Test(dataProvider = "ERR_PHONE", dataProviderClass = CrmScenarioUtil.class)
    public void addUserPhoneErr1(String errphone) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie, cstm.pwd);
            String userName = "" + System.currentTimeMillis();
            String userLoginName = userName;
            String phone = errphone;

            String passwd = userLoginName;
            int roleId = 13; //销售顾问
            //添加账号

            int code = crm.addUserNotChk(userName, userLoginName, phone, passwd, roleId).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("创建账号时手机号格式不正确");
        }
    }

    @Test()
    public void addUse201() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie, cstm.pwd);
            String userLoginName = "" + System.currentTimeMillis();
            ArrayList<String> ll = new ArrayList();

            int roleId = 13; //销售顾问
            int before_total = crm.userPage(1, 1).getInteger("total");
            while (before_total < 200) {
                String newloginname = userLoginName + before_total;
                //添加账号
                ll.add(pf.createUserId(newloginname, roleId));
                before_total = before_total + 1;
            }
            //创建201个账户
            String phone201 = pf.genPhoneNum();
            int code = crm.addUserNotChk("201个账户", "201个账户", phone201, cstm.pwd, roleId).getInteger("code");
            for (String userid : ll) {
                crm.userDel(userid);
            }
            Preconditions.checkArgument(code == 1001, "创建201个账户成功");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("创建200个账号");
        }
    }

    @Ignore //与下面的case重复
    @Test(dataProvider = "ROLE_ID", dataProviderClass = CrmScenarioUtil.class)
    public void delUserDiffRole(String role) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie, cstm.pwd);
            String userName = "" + System.currentTimeMillis();
            String userLoginName = userName;
            String phone = "1";
            for (int i = 0; i < 10; i++) {
                String a = Integer.toString((int) (Math.random() * 10));
                phone = phone + a;
            }

            String passwd = cstm.pwd;
            int roleId = Integer.parseInt(role);
            //添加账号
            crm.addUser(userName, userLoginName, phone, passwd, roleId, "", "");

            String userid = crm.userPage(1, 50).getJSONArray("list").getJSONObject(0).getString("user_id");


            //删除账号
            int code = crm.userDelNotChk(userid).getInteger("code");
            Preconditions.checkArgument(code == 1000, "删除失败，状态码" + code);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("删除不同身份账号");
        }
    }

    @Test(dataProvider = "ROLE_ID", dataProviderClass = CrmScenarioUtil.class)
    public void delUserDiffRoleANDlogin(String role) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie, cstm.pwd);
            String userName = "" + System.currentTimeMillis();
            String userLoginName = userName;
            String phone = "1";
            for (int i = 0; i < 10; i++) {
                String a = Integer.toString((int) (Math.random() * 10));
                phone = phone + a;
            }

            String passwd = cstm.pwd;
            int roleId = Integer.parseInt(role);
            //添加账号
            crm.addUser(userName, userLoginName, phone, passwd, roleId, "", "");

            String userid = crm.userPage(1, 50).getJSONArray("list").getJSONObject(0).getString("user_id");

            //删除账号
            crm.userDel(userid);

            //登陆
            String message = crm.tryLogin(userLoginName, cstm.pwd).getString("message");
            Preconditions.checkArgument(message.equals("用户名或密码错误"), "提示语为：" + message);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("删除不同身份账号后，再次登陆");
        }
    }


    /**
     * ====================登陆======================
     */
    @Test
    public void loginExist() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie, cstm.pwd);
            String userName = "" + System.currentTimeMillis();
            String userLoginName = "" + System.currentTimeMillis();
            String phone = "1";
            for (int i = 0; i < 10; i++) {
                String a = Integer.toString((int) (Math.random() * 10));
                phone = phone + a;
            }

            String passwd = cstm.pwd;
            int roleId = 13; //销售顾问
            //添加账号
            crm.addUser(userName, userLoginName, phone, passwd, roleId, "", "");
            String userid = crm.userPage(1, 50).getJSONArray("list").getJSONObject(0).getString("user_id");

            int code = crm.tryLogin(userLoginName, passwd).getInteger("code");
            Preconditions.checkArgument(code == 1000, "登陆失败");


            //删除账号
            crm.userDel(userid);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("使用存在的销售账号登陆");
        }
    }

    @Test
    public void loginExistWrongPwd() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie, cstm.pwd);
            String userName = "" + System.currentTimeMillis();
            String userLoginName = "" + System.currentTimeMillis();
            String phone = "1";
            for (int i = 0; i < 10; i++) {
                String a = Integer.toString((int) (Math.random() * 10));
                phone = phone + a;
            }

            String passwd = cstm.pwd;
            int roleId = 13; //销售顾问
            //添加账号
            crm.addUser(userName, userLoginName, phone, passwd, roleId, "", "");
            String userid = crm.userPage(1, 50).getJSONArray("list").getJSONObject(0).getString("user_id");

            int code = crm.tryLogin(userLoginName, passwd + "1").getInteger("code");
            Preconditions.checkArgument(code != 1000, "登陆成功");


            //删除账号
            crm.userDel(userid);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("使用存在的销售账号，错误的密码登陆");
        }
    }

    @Test
    public void loginExistWrongAcc() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int code = crm.tryLogin("1@q啊～", "1").getInteger("code");
            Preconditions.checkArgument(code != 1000, "登陆成功");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("使用不存在的销售账号登陆");
        }
    }


    /**
     * ====================人脸排除======================
     */
    @Test(dataProvider = "NO_FACE", dataProviderClass = CrmScenarioUtil.class)
    public void faceOutNoFace(String path) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            crm.login(cstm.baoshijie, cstm.pwd);
            //上传图片
            int code = crm.faceOutUpload(path).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            crm.login(cstm.lxqgw, cstm.pwd);
            saveData("人脸排除上传识别不出人脸的图片");
        }
    }


    //前台点击创建接待按钮创建顾客
    public Long creatCust(String name, String phone) throws Exception {
        //前台登陆
        crm.login(cstm.qt, cstm.pwd);
        Long customerid = -1L;
        //获取当前空闲第一位销售id

        String sale_id = crm.freeSaleList().getJSONArray("list").getJSONObject(0).getString("sale_id");
        //
        String userLoginName = "";
        JSONArray userlist = crm.userPage(1, 100).getJSONArray("list");
        for (int i = 0; i < userlist.size(); i++) {
            JSONObject obj = userlist.getJSONObject(i);
            if (obj.getString("user_id").equals(sale_id)) {
                userLoginName = obj.getString("user_login_name");
            }
        }
        //创建接待
        crm.creatReception("FIRST_VISIT");
        //销售登陆，获取当前接待id
        crm.login(userLoginName, cstm.pwd);
        customerid = crm.userInfService().getLong("customer_id");
        //创建某级客户
        if (name.equals("")) {
            String name1 = "zdh";
            String phone1 = "zdh" + (int) ((Math.random() * 9 + 1) * 100000);
            JSONObject customer = crm.finishReception(customerid, 7, name1, phone1, "自动化---------创建----------H级客户");

        } else {
            JSONObject customer = crm.finishReception(customerid, 7, name, phone.substring(3), "自动化---------创建----------H级客户");

        }

        return customerid;
    }


    /**
     * ====================2.1case   销售======================
     */

    //app-销售-工作管理-我的预约
    @Test
    public void Search_testDriver_name() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.lxqgw, cstm.pwd);
            JSONArray list = crm.appointmentlist().getJSONArray("list");

            if (list.size() > 0) {
                String name = list.getJSONObject(0).getString("customer_name").substring(1);
                String phone = list.getJSONObject(0).getString("customer_phone_number").substring(1);
                int size1 = crm.appointmentlist(name).getJSONArray("list").size();
                int size2 = crm.appointmentlist(phone).getJSONArray("list").size();
                Preconditions.checkArgument(size1 >= 1, "根据已存在姓名模糊搜索无结果");
                Preconditions.checkArgument(size2 >= 1, "根据已存在手机号模糊搜索无结果");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app-销售-工作管理-我的预约,根据姓名/手机号模糊搜索");
        }
    }

    @Test
    public void Search_testDriver_time() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.lxqgw, cstm.pwd);
            String yesterday = dt.getHistoryDate(-1);
            String tomorrow = dt.getHistoryDate(1);
            String dayaftertom = dt.getHistoryDate(2);
            int code1 = crm.appointmentlist(yesterday, tomorrow).getInteger("code");
            int code2 = crm.appointmentlist(tomorrow, dayaftertom).getInteger("code");
            int code3 = crm.appointmentlist(tomorrow, yesterday).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "开始时间<=当前时间<=结束时间状态码为" + code1);
            Preconditions.checkArgument(code2 == 1000, "当前时间<=开始时间<=结束时间" + code2);
            Preconditions.checkArgument(code3 == 1001, "结束时间<开始时间" + code3);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app-销售-工作管理-我的预约,根据时间搜索");
        }
    }

    /**
     * ====================2.1case   售后======================
     */


    @Test
    public void del() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.baoshijie, cstm.pwd);
            JSONArray array = crm.userPage(1, 100).getJSONArray("list");
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.getString("user_name").contains("161") || obj.getString("user_name").contains("162")) {
                    crm.userDel(obj.getString("user_id"));
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("删除账号");
        }
    }


    /**
     * ====================   4.0  试驾车管理======================
     */

    @Test(dataProvider = "ADD_CAR", dataProviderClass = CrmScenarioUtil.class)
    public void addtestcar(String name, String car, String carid) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj, cstm.pwd);
            int total = crm.driverCarList().getInteger("total");
            Long starttime = dt.getHistoryDateTimestamp(1);
            Long endtime = dt.getHistoryDateTimestamp(31);
            crm.carManagementAdd(name, 1L, 37L, car, carid, starttime, endtime);
            JSONObject obj = crm.driverCarList();
            int total2 = obj.getInteger("total");
            int add = total2 - total;
            String car_name = obj.getJSONArray("list").getJSONObject(0).getString("car_name");
            String plate_number = obj.getJSONArray("list").getJSONObject(0).getString("plate_number");
            String vehicle_chassis_code = obj.getJSONArray("list").getJSONObject(0).getString("vehicle_chassis_code");

            Preconditions.checkArgument(add == 1, "新建试驾车，列表数据增加了" + add);
            Preconditions.checkArgument(car_name.equals(name), "新建试驾车，新建时名称为" + name + ", 列表展示为" + car_name);
            Preconditions.checkArgument(plate_number.equals(car), "新建试驾车，新建时车牌号为" + car + ", 列表展示为" + plate_number);
            Preconditions.checkArgument(vehicle_chassis_code.equals(carid), "新建试驾车，新建时车架号为" + carid + ", 列表展示为" + vehicle_chassis_code);

            Thread.sleep(2000);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建试驾车-名字20位+车牌7位/8位");
        }
    }

    @Test
    public void addtestcarExist() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj, cstm.pwd);

            Long starttime = dt.getHistoryDateTimestamp(1);
            Long endtime = dt.getHistoryDateTimestamp(2);
            String car = "吉ZDH" + (int) ((Math.random() * 9 + 1) * 100);
            String carid = "ZDHZDHZDD" + (long) ((Math.random() * 9 + 1) * 10000000);
            String carname = "" + System.currentTimeMillis();
            //新增
            Long test_car_id = crm.carManagementAdd("1" + carname, 1L, 37L, getPlateNum(), carid, starttime, endtime).getLong("test_car_id");
            //注销
            crm.carLogout(test_car_id);

            int code1 = crm.carManagementAddNotChk("2" + carname, 1L, 37L, car, "ZDHZDHZDH" + (long) ((Math.random() * 9 + 1) * 10000000), starttime, endtime).getInteger("code"); //车牌重复
            int code2 = crm.carManagementAddNotChk("3" + carname, 1L, 37L, getPlateNum(), carid, starttime, endtime).getInteger("code"); //车架重复
            Preconditions.checkArgument(code1 == 1000, "车牌重复时失败");
            Preconditions.checkArgument(code2 != 1000, "车架号重复时成功");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建试驾车-车牌号和已注销车牌重复可成功，车架号和已注销的重复应失败");
        }
    }

    @Test
    public void addtestcarErr() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj, cstm.pwd);

            Long starttime = dt.getHistoryDateTimestamp(1);
            Long yesterday = dt.getHistoryDateTimestamp(-1);
            Long endtime = dt.getHistoryDateTimestamp(2);
            String car = "苏ZDH" + (int) ((Math.random() * 9 + 1) * 100);
            String carid = "ZDHZDHZDH" + (long) ((Math.random() * 9 + 1) * 10000000);

            //新增
            String name21 = "123456789012345678901";
            String car6 = "苏ZDH" + (int) ((Math.random() * 9 + 1) * 10);
            String car9 = "苏ZDH" + (int) ((Math.random() * 9 + 1) * 10000);
            String carno = "11111111";
            String carid16 = "ZDHZDHZDH" + (long) ((Math.random() * 9 + 1) * 1000000);
            String carid18 = "ZDHZDHZDH" + (long) ((Math.random() * 9 + 1) * 100000000);
            String caridnum = "0000000000" + (long) ((Math.random() * 9 + 1) * 10000000);
            String carideng = "ZDHZDHZDHZDHZDHZD";

            int code1 = crm.carManagementAddNotChk(name21, 1L, 37L, car, carid, starttime, endtime).getInteger("code"); //名字21位
            int code2 = crm.carManagementAddNotChk(getCarName(), 1L, 37L, car6, carid, starttime, endtime).getInteger("code"); //车牌号6位
            int code3 = crm.carManagementAddNotChk(getCarName(), 1L, 37L, car9, carid, starttime, endtime).getInteger("code"); //车牌号9位
            int code4 = crm.carManagementAddNotChk(getCarName(), 1L, 37L, car, carid16, starttime, endtime).getInteger("code"); //车架号16位
            int code5 = crm.carManagementAddNotChk(getCarName(), 1L, 37L, car, carid18, starttime, endtime).getInteger("code"); //车架号18位
            int code8 = crm.carManagementAddNotChk(getCarName(), 1L, 37L, carno, carid, starttime, endtime).getInteger("code"); //车牌号8位纯数字
            int code9 = crm.carManagementAddNotChk(getCarName(), 1L, 37L, car, carid, endtime, starttime).getInteger("code"); //服役结束时间<服役开始时间 前端限制
            //int code10 = crm.carManagementAddNotChk(getCarName(),1L,37L,car,carid,yesterday,starttime).getInteger("code"); //服役开始时间<当前时间
            //有bug 先注掉
            int code6 = crm.carManagementAddNotChk(getCarName(), 1L, 37L, car, caridnum, starttime, endtime).getInteger("code"); //车架号纯数字
            int code7 = crm.carManagementAddNotChk(getCarName(), 1L, 37L, car, carideng, starttime, endtime).getInteger("code"); //车架号纯字母


            Preconditions.checkArgument(code1 == 1001, "名字21位成功");
            Preconditions.checkArgument(code2 == 1001, "车牌号6位成功");
            Preconditions.checkArgument(code3 == 1001, "车牌号9位成功");
            Preconditions.checkArgument(code4 == 1001, "车架号16位成功");
            Preconditions.checkArgument(code5 == 1001, "车架号18位成功");
            Preconditions.checkArgument(code8 == 1001, "车牌号8位纯数字成功");
            Preconditions.checkArgument(code9 == 1001, "服役结束时间<服役开始时间成功");
            //Preconditions.checkArgument(code10==1001,"服役开始时间<当前时间成功");
            Preconditions.checkArgument(code6 == 1001, "车架号17位纯数字成功");
            Preconditions.checkArgument(code7 == 1001, "车架号17位纯字母成功");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建试驾车-名字车牌号车架号内容校验");
        }
    }

    @Test
    public void addtestcarLogout() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj, cstm.pwd);

            Long starttime = dt.getHistoryDateTimestamp(1);
            Long endtime = dt.getHistoryDateTimestamp(2);

            String carid = "ZDHZDHZDH" + (long) ((Math.random() * 9 + 1) * 10000000);
            //新增
            Long test_car_id = crm.carManagementAdd(getCarName(), 1L, 37L, getPlateNum(), carid, starttime, endtime).getLong("test_car_id");
            //注销
            crm.carLogout(test_car_id);
            boolean exit = false;
            JSONArray array = crm.driverCarList().getJSONArray("list");
            for (int i = array.size() - 1; i >= 0; i--) {
                System.out.println(i);
                JSONObject obj = array.getJSONObject(i);
                if (obj.getString("vehicle_chassis_code").equals(carid)) {
                    exit = true;
                    break;
                }
            }
            Preconditions.checkArgument(exit == true, "注销后车辆不存在");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("注销试驾车，该车辆仍在列表中");
        }
    }

    @Test
    public void addtestcarEdit() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj, cstm.pwd);

            Long starttime = dt.getHistoryDateTimestamp(1);
            Long starttimenew = dt.getHistoryDateTimestamp(2);
            String startdate = dt.getHistoryDate(2);

            Long endtime = dt.getHistoryDateTimestamp(2);
            Long endtimenew = dt.getHistoryDateTimestamp(3);
            String enddate = dt.getHistoryDate(3);

            String car = getPlateNum();
            String carnew = getPlateNum();
            String carid = "ZDHZDHZDH" + (long) ((Math.random() * 9 + 1) * 10000000);
            String caridnew = "ZDHZDHGGG" + (long) ((Math.random() * 9 + 1) * 10000000);
            String name = getCarName();
            String namenew = name + "a";
            //新增
            int test_car_id = crm.carManagementAdd(name, 1L, 37L, car, carid, starttime, endtime).getInteger("test_car_id");

            //修改
            crm.carManagementEdit(test_car_id, namenew, 1L, 37L, carnew, caridnew, starttimenew, endtimenew);
            String startresult = "";
            String endtimeresult = "";
            String carresult = "";
            String caridresult = "";
            String nameresult = "";

            JSONArray array = crm.driverCarList().getJSONArray("list");
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.getLong("test_car_id") == test_car_id) {
                    startresult = obj.getString("service_time_start_date");
                    endtimeresult = obj.getString("service_time_end_date");
                    nameresult = obj.getString("car_name");
                    carresult = obj.getString("plate_number");
                    caridresult = obj.getString("vehicle_chassis_code");
                }
            }

            Preconditions.checkArgument(startresult.equals(startdate), "开始服役时间未更新");
            Preconditions.checkArgument(endtimeresult.equals(enddate), "结束服役时间未更新");
            Preconditions.checkArgument(nameresult.equals(namenew), "车辆名称未更新");
            Preconditions.checkArgument(carresult.equals(carnew), "车牌号未更新");
            Preconditions.checkArgument(caridresult.equals(caridnew), "车架号未更新");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("修改试驾车，判断信息是否更新");
        }
    }


    /**
     * ====================   4.0  创建DCC线索  ======================
     */

    @Test(dataProvider = "DCCCREAT", dataProviderClass = CrmScenarioUtil.class)
    public void addDccCust(String name, String phone, String car) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.lxqgw, cstm.pwd);

            int total1 = crm.dcclist(1, 1).getInteger("total");
            crm.dccCreate(name, phone, car);
            JSONObject obj = crm.dcclist(1, 1);
            int total2 = obj.getInteger("total");
            int change = total2 - total1;
            String phoneresult = obj.getJSONArray("list").getJSONObject(0).getString("customer_phone");
            String nameresult = obj.getJSONArray("list").getJSONObject(0).getString("customer_name");
            Preconditions.checkArgument(change == 1, "新建dcc线索后，列表数增加了" + change);
            Preconditions.checkArgument(phoneresult.equals(phone), "列表中手机号为" + phoneresult + ", 新建时手机号为" + phone);
            Preconditions.checkArgument(nameresult.equals(name), "列表中客户名称为" + nameresult + ", 新建时客户名称为" + name);
            Thread.sleep(1000);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建dcc线索-校验姓名/手机号/车牌号格式");
        }
    }

    @Test
    public void addDccCustErr() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.dccxs, cstm.pwd);

            String name = "自动化";
            String nameno = "      ";
            String name51 = "姓名51位姓名51位姓名51位姓名51位姓名51位姓名51位姓名51位姓名51位姓名51位123451";
            String phone = "139000" + (int) ((Math.random() * 9 + 1) * 10000);
            String phone12 = "1390001" + (int) ((Math.random() * 9 + 1) * 10000); //手机号12位
            String phone10 = "13900" + (int) ((Math.random() * 9 + 1) * 10000); //手机号10位
            String car7 = "苏ZDH" + (int) ((Math.random() * 9 + 1) * 100);
            String car8 = "苏ZDH" + (int) ((Math.random() * 9 + 1) * 1000);
            String car6 = "苏ZDH" + (int) ((Math.random() * 9 + 1) * 10);
            String car9 = "苏ZDH" + (int) ((Math.random() * 9 + 1) * 10000);
            String carno = "AZDH" + (int) ((Math.random() * 9 + 1) * 1000);

            int code = crm.dccCreateNotChk(name51, phone, car7).getInteger("code"); //姓名51位

            int code1 = crm.dccCreateNotChk(name, phone12, car8).getInteger("code"); //手机号12位
            int code2 = crm.dccCreateNotChk(name, phone10, "").getInteger("code"); //手机号10位
            int code3 = crm.dccCreateNotChk(name, phone, car6).getInteger("code"); //车牌号6位
            int code4 = crm.dccCreateNotChk(name, phone, car9).getInteger("code"); //车牌号9位
            int code5 = crm.dccCreateNotChk(name, phone, carno).getInteger("code"); //车牌号非
            int code6 = crm.dccCreateNotChk(nameno, phone, car7).getInteger("code"); //姓名为空

            Preconditions.checkArgument(code == 1001, "姓名51位期待1001，实际" + code);
            Preconditions.checkArgument(code1 == 1001, "手机号12位" + phone12 + "期待1001，实际" + code1);
            Preconditions.checkArgument(code2 == 1001, "手机号10位" + phone10 + "期待1001，实际" + code2);
            Preconditions.checkArgument(code3 == 1001, "车牌号6位" + car6 + "期待1001，实际" + code3);
            Preconditions.checkArgument(code4 == 1001, "车牌号9位" + car9 + "期待1001，实际" + code4);
            Preconditions.checkArgument(code5 == 1001, "非车牌号格式" + carno + "期待1001，实际" + code5);
            Preconditions.checkArgument(code6 == 1001, "姓名为空格时期待1001，实际" + code6);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建dcc线索-姓名/手机号/车牌号格式不正确");
        }
    }

    @Test
    public void addDccCustRe() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.dccxs, cstm.pwd);

            String name = "自动化";
            String phone = "139000" + (int) ((Math.random() * 9 + 1) * 10000);
            String phone1 = "139001" + (int) ((Math.random() * 9 + 1) * 10000);
            String car7 = "辽ZDH" + (int) ((Math.random() * 9 + 1) * 100);
            String car8 = "辽ZDH" + (int) ((Math.random() * 9 + 1) * 1000);


            crm.dccCreate(name, phone, car7);
            int code = crm.dccCreateNotChk(name, phone, car8).getInteger("code"); //已存在手机号
            Preconditions.checkArgument(code == 1001, "使用已存在手机号期待1001，实际" + code);
            int code1 = crm.dccCreateNotChk(name, phone1, car7).getInteger("code"); //已存在客户的车牌号
            Preconditions.checkArgument(code1 == 1001, "使用已存在客户的车牌号期待1001，实际" + code1);

            Long starttime = dt.getHistoryDateTimestamp(1);
            Long endtime = dt.getHistoryDateTimestamp(2);
            String car = "黑ZDH" + (int) ((Math.random() * 9 + 1) * 100);
            String carid = "ZDHZDHZDH" + (long) ((Math.random() * 9 + 1) * 10000000);
            //新增
            crm.login(cstm.xszj, cstm.pwd);
            Long test_car_id = crm.carManagementAdd("ZDH" + (int) ((Math.random() * 9 + 1) * 100), 1L, 37L, car, carid, starttime, endtime).getLong("test_car_id");

            crm.login(cstm.dccxs, cstm.pwd);
            int code2 = crm.dccCreateNotChk(name, phone1, car).getInteger("code"); //试驾车列表未注销的车牌号
            Preconditions.checkArgument(code2 == 1001, "使用试驾车列表未注销的车牌号" + car + "期待1001，实际" + code2);

            //注销
            crm.login(cstm.xszj, cstm.pwd);
            crm.carLogout(test_car_id);
            crm.login(cstm.dccxs, cstm.pwd);
            int code3 = crm.dccCreateNotChk(name, phone1, car).getInteger("code"); //试驾车列表已注销的车牌号
            Preconditions.checkArgument(code3 == 1000, "使用试驾车列表已注销的车牌号" + car + "期待1000，实际" + code3);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建dcc线索-使用存在手机号/车牌号期待失败");
        }
    }


    /**
     * ====================   4.0  收件箱  ======================
     */

    @Test(dataProvider = "EMAIL", dataProviderClass = CrmScenarioUtil.class)
    public void emailConfig(String email) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj, cstm.pwd);
            crm.mailConfig(email);
            String result = crm.mailDetail().getString("email");
            Preconditions.checkArgument(result.equals(email), "配置后，邮箱未改变");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("配置收件箱，邮箱格式，长度1-100");
        }
    }

    @Test(dataProvider = "EMAILERR", dataProviderClass = CrmScenarioUtil.class)
    public void emailConfigErr(String email) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            crm.login(cstm.xszj, cstm.pwd);
            int code = crm.mailConfigNotChk(email).getInteger("code");
            Preconditions.checkArgument(code == 1001, "邮箱为" + email + "时修改成功");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("配置收件箱，邮箱格式/长度不正确");
        }
    }


    public String getPlateNum() {
        String qu = "CEFGHJKLMNPQY";
        int a = (int) (Math.random() * 10);
        String plateNum = "京";
        plateNum = plateNum + qu.substring(a, a + 1);
        for (int i = 0; i < 5; i++) {
            String b = Integer.toString((int) (Math.random() * 10));
            plateNum = plateNum + b;
        }
        System.out.println(plateNum);
        return plateNum;
    }

    public String getCarName() {

        String name = "Name" + Integer.toString((int) (Math.random() * 100000000));
        return name;
    }


}
