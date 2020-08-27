package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CrmPCConsistentcyDaily {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();

    Ignore_Crm crm = new Ignore_Crm();

    String majordomoSaleId = "uid_9c2b914d";

    //    ---------------------------------------------------展厅接待-------------------------------------------------------------------
    @Test
    public void OnServiceOnly1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "展厅接待，一个分配销售有多条记录，客户状态为“接待中”的数量=1";
        String caseName = ciCaseName;

        try {

            JSONObject data = crm.customerTodayList();

            crm.checkOnservice1(data);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void TodayListEqualsOrderListOnService() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "展厅接待，接待中数量=销售排班中接待中数量";
        String caseName = ciCaseName;

        try {

            JSONObject todayList = crm.customerTodayList();
            int num1 = crm.getTodayListOnService(todayList, "接待中");

            JSONObject userList = crm.freeSaleUserList();
            int num2 = crm.getOrderListOnService(userList, "接待中");

            Preconditions.checkArgument(num1 == num2, "展厅接待中，接待中销售员数=" + num1 +
                    "，不等于销售排班中接待中的销售员数=" + num2);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void TodayListWaitNew0() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "展厅接待，新客等待中状态条数=0";
        String caseName = ciCaseName;

        try {

            JSONObject data = crm.customerTodayList();

            crm.getTodayListWaitNew0(data);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

//    -------------------------------------------------销售排班-----------------------------------------------------------------

    //    @Test
    public void receptionTodayCustomerAdd1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "销售排班，每接待一次，今日接待次数+1";
        String caseName = ciCaseName;

        try {

            JSONObject data = crm.freeSaleUserList();

            int salerCustomerNumB = crm.getSalerCustomerNum(data, majordomoSaleId);

            //先确认下customerId填什么吧-----------------------------------------------------
            crm.addCustomerApp("qwerty");

            data = crm.freeSaleUserList();

            int salerCustomerNumA = crm.getSalerCustomerNum(data, majordomoSaleId);

            Preconditions.checkArgument(salerCustomerNumA - salerCustomerNumB == 1,
                    "saleId=" + majordomoSaleId + "，接待一个新客前，今日接待人数=" + salerCustomerNumB +
                            "，接待一个新客后，今日接待人数=" + salerCustomerNumA);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    @Test
    public void freeSaleUserListEquals() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "销售排班，总条数=顾问账号数量";
        String caseName = ciCaseName;

        try {

            int salerNum = crm.freeSaleUserList().getJSONArray("list").size();

            crm.adminLogin();

            int salerNum1 = crm.getUserPageSalerNUm();

            Preconditions.checkArgument(salerNum == salerNum1,
                    "销售排班中的总人数=" + salerNum + "不等于销售账号数量=" + salerNum1);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    //    @Test  手动创建顾客时点击按钮调了修改状态的接口，此case作废
    public void compReceptionFreeAdd1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "销售排班，完成接待后，空闲顾问+1";
        String caseName = ciCaseName;

        try {

//            状态置为空闲
            crm.updateStatusAPP("FREE");

//            1、新建顾客
            crm.addCustomerApp("");

//            获取销售排班中，该销售的状态
            JSONObject freeSaleUserList = crm.freeSaleUserList();
            crm.checkSalerStatus(freeSaleUserList, majordomoSaleId, "接待中");

//            2、完成接待
            crm.finishReception();

//            获取销售排班中，该销售的状态(接待完成后该销售状态为空闲)
            freeSaleUserList = crm.freeSaleUserList();
            crm.checkSalerStatus(freeSaleUserList, majordomoSaleId, "空闲");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    //    @Test 退出登录状态不改变，此case作废
    public void logoutVacationAdd1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "退出登录，休假中的销售+1";
        String caseName = ciCaseName;

        try {

//            登出
            crm.logout();

//            销售排班中的该销售员的状态为休假
            JSONObject data = crm.freeSaleUserList();

            crm.checkSaleStatus(data, "", "休假");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

//    ***********************************************客户详情***************************************************



//    ************************************************我的试驾********************************************************************

    //    @Test
    public void addDriveEQDetail() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "新建试驾的信息与我的试驾的信息相同";
        String caseName = ciCaseName;

        try {

//           新建试驾
            String customerName = "";
            String idCard = "";
            String gender = "";
            String phone = "";
            long signTime = 0;
            long appointmentTime = 0;
            String model = "";
            String country = "";
            String city = "";
            String email = "";
            String address = "";
            String driverLicensePhoto1Url = "";
            String driverLicensePhoto2Url = "";
            String electronicContractUrl = "";
//            crm.addDriveWithCustomerInfo(customerName, idCard, gender, phone,
//                    signTime, appointmentTime, model, country, city, email, address, driverLicensePhoto1Url,
//                    driverLicensePhoto2Url, electronicContractUrl);

//            试驾列表
            long id = crm.driveList(1, 1).getJSONArray("list").getJSONObject(0).getLongValue("id");

//            试驾详情
            JSONObject driveDetail = crm.driveDetail(id);

            crm.checkAddDrive(driveDetail, customerName, idCard, gender, phone,
                    signTime, appointmentTime, model, country, city, email, address, driverLicensePhoto1Url,
                    driverLicensePhoto2Url, electronicContractUrl);

//            删除试驾
            crm.driveDelete(id);

//            试驾列表
            JSONObject driveList = crm.driveList(1, 1);


        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }


//    ***************************************************创建账号*********************************************************************

    @Test
    public void addRoleSaleChk() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "创建账号（销售员）-新建时与列表一致";
        String caseName = ciCaseName;

        try {

//            新建销售账号
            String userName = crm.genRandom7();
            String userLoginName = crm.genRandom7();
            String phone = crm.genPhoneNum();
            String password = crm.genRandom7();
            int[] roleIds = {10, 11, 12, 13, 14};

//            管理员登陆
            crm.adminLogin();

            for (int i = 0; i < roleIds.length; i++) {

                crm.addUser(userName, userLoginName, phone, password, roleIds[i]);

//            账号列表
                int pages = crm.userPage(1, 10).getInteger("pages");
                JSONArray list = crm.userPage(pages, 10).getJSONArray("list");
                JSONObject roleMess = list.getJSONObject(list.size() - 1);

                String userId = roleMess.getString("user_id");

                Preconditions.checkArgument(roleMess.getInteger("role_id") == roleIds[i], "新建时的role_id="
                        + roleIds[i] + "，不等于列表中的role_id=" + roleMess.getInteger("role_id"));

                Preconditions.checkArgument(userName.equals(roleMess.getString("user_name")), "新建时的user_name="
                        + userName + "，不等于列表中的user_name=" + roleMess.getString("user_name"));

                Preconditions.checkArgument(userLoginName.equals(roleMess.getString("user_login_name")), "新建时的user_login_name="
                        + userLoginName + "，不等于列表中的user_login_name=" + roleMess.getString("user_login_name"));

                Preconditions.checkArgument(phone.equals(roleMess.getString("user_phone")), "新建时的user_phone="
                        + phone + "，不等于列表中的user_phone=" + roleMess.getString("user_phone"));

//            删除销售的账号
                crm.deleteUser(userId);

//            账号列表
                pages = crm.userPage(1, 10).getInteger("pages");
                JSONObject data = crm.userPage(pages, 10);

                crm.checkDeleteUser(data, userId);
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            crm.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        crm.majordomoLogin();
    }

    @AfterClass
    public void clean() {
        crm.clean();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }
}
