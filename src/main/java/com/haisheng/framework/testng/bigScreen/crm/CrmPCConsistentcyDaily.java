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

import java.text.DecimalFormat;
import java.util.HashMap;

public class CrmPCConsistentcyDaily {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    public String failReason = "";
    public String response = "";
    public boolean FAIL = false;
    public Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();

    Crm crm = new Crm();

    String saleId = "";


//    -------------------------------------------------销售排班-----------------------------------------------------------------

    @Test
    public void regionUvLTUv() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "销售排班，每接待一次，今日接待次数+1";
        String caseName = ciCaseName;

        try {

            JSONObject data = crm.freeSaleUserList();

            int salerCustomerNumB = crm.getSalerCustomerNum(data, saleId);

            crm.addCustomerApp("");

            data = crm.freeSaleUserList();

            int salerCustomerNumA = crm.getSalerCustomerNum(data, saleId);


            Preconditions.checkArgument(salerCustomerNumA - salerCustomerNumB == 1,
                    "新接待一人后，今日接待人数没有+1");

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

            int salerNum1 = crm.userPage(crm.genRandom7(), 0).getJSONArray("list").size();


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

    @Test
    public void compReceptionFreeAdd1() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "销售排班，完成接待后，空闲顾问+1";
        String caseName = ciCaseName;

        try {

//            获取销售排班中，该销售的状态(接待前该销售状态为空闲)
            JSONObject freeSaleUserList = crm.freeSaleUserList();
//            状态置为空闲
            crm.updateStatusAPP("FREE");

//            1、新建顾客
            crm.addCustomerApp("");

//            获取销售排班中，该销售的状态
            freeSaleUserList = crm.freeSaleUserList();
            crm.checkSalerStatus(freeSaleUserList, saleId, "接待中");

//            2、完成接待
            crm.finishReception();

//            获取销售排班中，该销售的状态(接待完成后该销售状态为空闲)
            freeSaleUserList = crm.freeSaleUserList();
            crm.checkSalerStatus(freeSaleUserList, saleId, "空闲");

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

    @Test
    public void appCustomerDetailEqualsPC() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "客户详情页信息与列表页信息一致";
        String caseName = ciCaseName;

        try {

//            新建顾客
            String customerId = "";
            String analysisCustomerId = "";
            int customerLevel = 0;
            int customerSelectType = 0;
            String customerName = "";
            String customerPhone = "";
            int visitCount = 0;
            int belongsArea = 0;
            String alreadyCar = "";
            int testDriveCar = 0;
            int sehandAssess = 0;
            int carAssess = 0;
            String preBuyTime = "";
            int likeCar = 0;
            String compareCar = "";
            int showPrice = 0;
            int payType = 0;
            int buyCar = 0;
            int buyCarType = 0;
            int buyCarAttribute = 0;
            String reamrks = "";
            String comment = "";
            String nextReturnVisitDate = "";
            crm.addCustomerApp(customerId, analysisCustomerId, customerLevel, customerSelectType, customerName,
                    customerPhone, visitCount, belongsArea, alreadyCar, testDriveCar, sehandAssess,
                    carAssess, preBuyTime, likeCar, compareCar, showPrice,
                    payType, buyCar, buyCarType, buyCarAttribute, reamrks, comment,
                    nextReturnVisitDate);

//            顾客详情(app)
            JSONObject customerDetailApp = crm.customerDetailApp(customerId);

            crm.checkAddCustomer(customerDetailApp, customerId, analysisCustomerId, customerLevel, customerSelectType, customerName,
                    customerPhone, visitCount, belongsArea, alreadyCar, testDriveCar, sehandAssess,
                    carAssess, preBuyTime, likeCar, compareCar, showPrice,
                    payType, buyCar, buyCarType, buyCarAttribute, reamrks, comment,
                    nextReturnVisitDate);

//            顾客详情（PC）
            JSONObject customerDetailPC = crm.customerDetailPC(customerId);
            crm.checkAddCustomer(customerDetailApp, customerId, analysisCustomerId, customerLevel, customerSelectType, customerName,
                    customerPhone, visitCount, belongsArea, alreadyCar, testDriveCar, sehandAssess,
                    carAssess, preBuyTime, likeCar, compareCar, showPrice,
                    payType, buyCar, buyCarType, buyCarAttribute, reamrks, comment,
                    nextReturnVisitDate);

//            编辑顾客
            int customerLevelE = 0;
            int customerSelectTypeE = 0;
            String customerNameE = "";
            String customerPhoneE = "";
            int visitCountE = 0;
            int belongsAreaE = 0;
            String alreadyCarE = "";
            int testDriveCarE = 0;
            int sehandAssessE = 0;
            int carAssessE = 0;
            String preBuyTimeE = "";
            int likeCarE = 0;
            String compareCarE = "";
            int showPriceE = 0;
            int payTypeE = 0;
            int buyCarE = 0;
            int buyCarTypeE = 0;
            int buyCarAttributeE = 0;
            String reamrksE = "";
            String commentE = "";
            String nextReturnVisitDateE = "";
            crm.customerEditApp(customerId, analysisCustomerId, customerLevelE, customerSelectTypeE, customerNameE,
                    customerPhone, visitCount, belongsArea, alreadyCar, testDriveCar, sehandAssess,
                    carAssess, preBuyTime, likeCar, compareCar, showPrice,
                    payType, buyCar, buyCarType, buyCarAttribute, reamrks, comment,
                    nextReturnVisitDate);

//            顾客详情(app)
            customerDetailApp = crm.customerDetailApp(customerId);

            crm.checkAddCustomer(customerDetailApp, customerId, analysisCustomerId, customerLevel, customerSelectType, customerName,
                    customerPhone, visitCount, belongsArea, alreadyCar, testDriveCar, sehandAssess,
                    carAssess, preBuyTime, likeCar, compareCar, showPrice,
                    payType, buyCar, buyCarType, buyCarAttribute, reamrks, comment,
                    nextReturnVisitDate);

//            顾客详情（PC）
            customerDetailPC = crm.customerDetailPC(customerId);
            crm.checkAddCustomer(customerDetailApp, customerId, analysisCustomerId, customerLevel, customerSelectType, customerName,
                    customerPhone, visitCount, belongsArea, alreadyCar, testDriveCar, sehandAssess,
                    carAssess, preBuyTime, likeCar, compareCar, showPrice,
                    payType, buyCar, buyCarType, buyCarAttribute, reamrks, comment,
                    nextReturnVisitDate);

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
            crm.addDriveWithCustomerInfo(customerName, idCard, gender, phone,
                    signTime, appointmentTime, model, country, city, email, address, driverLicensePhoto1Url,
                    driverLicensePhoto2Url, electronicContractUrl);

//            试驾列表
            long id = crm.driveList(1, 1).getJSONArray("list").getJSONObject(0).getLongValue("id");

//            试驾详情
            JSONObject driveDetail = crm.driveDetail(id);

            crm.checkAddDrive(driveDetail,customerName, idCard,gender,phone,
            signTime,appointmentTime, model, country, city,email,address,driverLicensePhoto1Url,
                    driverLicensePhoto2Url,electronicContractUrl);

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


    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        crm.salesPersonLogin();
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
