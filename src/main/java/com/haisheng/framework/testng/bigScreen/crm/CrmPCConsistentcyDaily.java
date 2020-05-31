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
                    "销售排班中的总人数=" + salerNum + "不等于销售账号数量=" + salerNum1 );

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
            crm.checkSalerStatus(freeSaleUserList,saleId,"接待中");

//            2、完成接待
            crm.finishReception();

//            获取销售排班中，该销售的状态(接待完成后该销售状态为空闲)
            freeSaleUserList = crm.freeSaleUserList();
            crm.checkSalerStatus(freeSaleUserList,saleId,"空闲");

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


//    ***********************************************我的回访***************************************************

    @Test
    public void appVisitEqualsPCVisit() {
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
            crm.checkSalerStatus(freeSaleUserList,saleId,"接待中");

//            2、完成接待
            crm.finishReception();

//            获取销售排班中，该销售的状态(接待完成后该销售状态为空闲)
            freeSaleUserList = crm.freeSaleUserList();
            crm.checkSalerStatus(freeSaleUserList,saleId,"空闲");

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
