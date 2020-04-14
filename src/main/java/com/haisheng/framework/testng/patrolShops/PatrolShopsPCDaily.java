package com.haisheng.framework.testng.patrolShops;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.bigScreen.Feidan;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.time.LocalDate;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class PatrolShopsPCDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    Feidan feidan = new Feidan();
    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    DateTimeUtil dt = new DateTimeUtil();
    PatrolShops patrolShops = new PatrolShops();

    String word20 = "12345678901234567890";
    String word100 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
    String word30 = "123456789012345678901234567890";

    /**
     * 执行清单验证
     */
    @Test
    public void checkListCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建-清单列表-清单详情-编辑清单-清单列表-清单详情-删除清单-清单列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单
            String name = ciCaseName;
            String desc = "清单验证";
            String title = "title";
            String comment = "";
            String createTime = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
            patrolShops.addCheckList(name, desc, title, comment);

//            查询清单
            long id = patrolShops.checkNewCheckList(name, desc, createTime);

//            清单详情
            patrolShops.checkCheckListDetail(id, name, desc, title, comment);

//            编辑清单
            name = name + "-new";
            desc = desc + "-new";
            title = title + "-new";
            comment = comment + "-new";

            patrolShops.checkListEdit(id, name, desc, title, comment);

            patrolShops.checkListEdit(id, name, desc, title, comment);

//            查询清单
            patrolShops.checkEditCheckList(id, name, desc, createTime);

//            清单详情
            patrolShops.checkCheckListDetail(id, name, desc, title, comment);

//            删除清单
            patrolShops.checkListDelete(id);

//            清单列表
            patrolShops.checkCheckListNotExist(id, name);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建执行清单验证-参数为空
     */
    @Test(dataProvider = "EMPTY_PARA_CHECK_LIST")
    public void checkListNewEmpty(String emptyPara) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + emptyPara + "isNull";

        String caseDesc = "新建清单-" + emptyPara + "为空！";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单
            String name = ciCaseName;
            String desc = "清单验证";
            String title = "title";
            String comment = "comment";
            patrolShops.addCheckListEmpty(name, desc, title, comment, emptyPara);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 执行清单验证-参数特殊字符
     */
    @Test
    public void checkListCheckSpecialChar() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建清单-编辑清单";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单
            String name = ciCaseName;
            String desc = "#￥%……&";
            String title = "&*（）";
            String comment = "：“《？》";
            String createTime = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
            patrolShops.addCheckList(name, desc, title, comment);

//            查询清单
            long id = patrolShops.checkNewCheckList(name, desc, createTime);

//            清单详情
            patrolShops.checkCheckListDetail(id, name, desc, title, comment);

//            编辑清单
            patrolShops.checkListEdit(id, name, desc, title, comment);

//            查询清单
            patrolShops.checkEditCheckList(id, name, desc, createTime);

//            清单详情
            patrolShops.checkCheckListDetail(id, name, desc, title, comment);

//            删除清单
            patrolShops.checkListDelete(id);

//            清单列表
            patrolShops.checkCheckListNotExist(id, name);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }


    /**
     * 编辑执行清单验证-参数为空
     */
    @Test(dataProvider = "EMPTY_PARA_CHECK_LIST")
    public void checkListEditEmpty(String emptyPara) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + "-" + emptyPara + "isNull";

        String caseDesc = "新建清单-" + emptyPara + "为空！";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单
            String name = ciCaseName;
            String desc = "清单验证";
            String title = "title";
            String comment = "comment";
            String createTime = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
            patrolShops.addCheckList(name, desc, title, comment);

//            查询清单
            long id = patrolShops.checkNewCheckList(name, desc, createTime);

//            编辑清单
            patrolShops.checkListEditEmptyPara(id, name, desc, title, comment, emptyPara);

            patrolShops.checkListDelete(id);

            patrolShops.checkCheckListNotExist(id, name);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }


    /**
     * 新建和编辑时两边有空格，要自动trim，备注和清单说明不用trim
     */
    @Test
    public void checkListTrim() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建(两边有空格)-清单列表-清单详情-编辑清单(两边有空格)-清单列表-清单详情-删除清单-清单列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单
            String name = "  " + ciCaseName + "   ";
            String nameTrim = stringUtil.trimStr(name);
            String desc = "  两边空格名称  ";
            String title = "  两边空格标题   ";
            String titleTrim = stringUtil.trimStr(title);
            String comment = "   两边空格备注   ";
            String createTime = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
            patrolShops.addCheckList(name, desc, title, comment);

//            查询清单
            long id = patrolShops.checkNewCheckList(nameTrim, desc, createTime);

//            清单详情
            patrolShops.checkCheckListDetail(id, nameTrim, desc, titleTrim, comment);

//            编辑清单
            patrolShops.checkListEdit(id, name, desc, title, comment);

//            查询清单
            patrolShops.checkEditCheckList(id, nameTrim, desc, createTime);

//            清单详情
            patrolShops.checkCheckListDetail(id, nameTrim, desc, titleTrim, comment);

//            删除清单
            patrolShops.checkListDelete(id);

//            清单列表
            patrolShops.checkCheckListNotExist(id, nameTrim);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }


    /**
     * 执行清单验证-重名
     */
    @Test
    public void checkListDumpName() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建1-再次新建1（相同名称）-再次新建2（不同名称，相同标题）-将2编辑为与1同名";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单1
            String name = ciCaseName;
            String desc = "清单验证";
            String title = "title";
            String comment = "comment";
            String createTime = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
            patrolShops.addCheckList(name, desc, title, comment);
            long id1 = patrolShops.checkNewCheckList(name, desc, createTime);

//            再次新建1(相同名称，不同标题，失败)
            title = "title" + "diff";
            String res = patrolShops.addCheckListNoCode(name, desc, title, comment);
            patrolShops.checkCode(res, StatusCode.BAD_REQUEST, "新建清单-重名");
            patrolShops.checkMessage("新建清单-重名", res, "");

//            再次新建2（不同名称，相同标题）
            name = ciCaseName + "diff";
            title = "title";
            createTime = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
            patrolShops.addCheckList(name, desc, title, comment);
            patrolShops.checkNewCheckList(name, desc, createTime);
            long id2 = patrolShops.checkNewCheckList(name, desc, createTime);

//            编辑清单（不同名称，相同title）
            patrolShops.checkListEdit(id2, name, desc, title, comment);

//            将2编辑为与1同名
            name = ciCaseName;
            res = patrolShops.checkListEditNoCode(id2, name, desc, title, comment);
            patrolShops.checkCode(res, StatusCode.BAD_REQUEST, "");
            patrolShops.checkMessage("", res, "");

//            删除清单
            patrolShops.checkListDelete(id1);
            patrolShops.checkListDelete(id2);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 执行清单验证-字数在边界上
     */
    @Test
    public void checkListWordLimit() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建-编辑-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单1
            String name = "12345678901234567890";
            String desc = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
            String title = "123456789012345678901234567890";
            String comment = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
            String createTime = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
            patrolShops.addCheckList(name, desc, title, comment);

//            清单列表
            long id1 = patrolShops.checkNewCheckList(name, desc, createTime);

//            编辑清单
            name = ciCaseName;
            String res = patrolShops.checkListEditNoCode(id1, name, desc, title, comment);
            patrolShops.checkCode(res, StatusCode.BAD_REQUEST, "");
            patrolShops.checkMessage("", res, "");

//            删除清单
            patrolShops.checkListDelete(id1);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建执行清单-字数在边界外
     */
    @Test(dataProvider = "CHECK_LIST_WORD_LIMIT_OUT")
    public void checkListNewWordLimitOut(String name, String desc, String title, String comment) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建-编辑-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单1
            String createTime = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
            patrolShops.addCheckListNoCode(name, desc, title, comment);
            long id1 = patrolShops.checkNewCheckList(name, desc, createTime);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 编辑执行清单-字数在边界外
     */
    @Test(dataProvider = "CHECK_LIST_WORD_LIMIT_OUT")
    public void checkListEditWordLimitOut(String name, String desc, String title, String comment) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建-编辑-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单1
            String createTime = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
            patrolShops.addCheckListNoCode("name", "desc", "title", "comment");
            long id1 = patrolShops.checkNewCheckList(name, desc, createTime);

//            编辑清单
            name = ciCaseName;
            String res = patrolShops.checkListEditNoCode(id1, name, desc, title, comment);
            patrolShops.checkCode(res, StatusCode.BAD_REQUEST, "");
            patrolShops.checkMessage("", res, "");

//            删除清单
            patrolShops.checkListDelete(id1);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 定检任务验证
     */
    @Test
    public void scheduleCheckCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "获取定检员列表-获取可查询门店-新建定检任务-列表-编辑-列表-删除-列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取定检员列表
            JSONArray list = patrolShops.inspectorList().getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，定检员列表为空！");
            }

            String inspectorId = list.getJSONObject(0).getString("id");
            String inspectorName = list.getJSONObject(0).getString("name");

//            获取可巡检门店列表
            String districtCode = "";
            list = patrolShops.scheduleCheckShopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }

            String shopId = list.getJSONObject(0).getString("id");

//            新建定检任务
            String name = ciCaseName;
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();

            patrolShops.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd,
                    inspectorId, shopId);

//            定检任务列表
            long scheduleId = patrolShops.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            name = ciCaseName;
            cycle = "MONTH";
            dates = "\"1\"";
            sendTime = "09:00";
            validStart = LocalDate.now().minusDays(5).toString();
            validEnd = LocalDate.now().plusDays(1).toString();
            patrolShops.scheduleCheckEdit(scheduleId, name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            patrolShops.checkEditScheduleCheck(scheduleId, name, cycle, dates, validStart, validEnd, inspectorName);

//            删除定检任务
            patrolShops.scheduleCheckDelete(scheduleId);

//            定检任务列表
            patrolShops.checkScheduleCheckNotExist(scheduleId, name);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建定检任务-名称为特殊字符
     */
    @Test
    public void scheduleCheckSpecialChar() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务-名称为特殊字符";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取定检员列表
            JSONArray list = patrolShops.inspectorList().getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，定检员列表为空！");
            }

            String inspectorId = list.getJSONObject(0).getString("id");
            String inspectorName = list.getJSONObject(0).getString("name");

//            获取可巡检门店列表
            String districtCode = "";
            list = patrolShops.scheduleCheckShopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }

            String shopId = list.getJSONObject(0).getString("id");

//            新建定检任务
            String name = "!@#$%^&*()_";
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();

            patrolShops.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId = patrolShops.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            patrolShops.scheduleCheckEdit(scheduleId, name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            patrolShops.checkEditScheduleCheck(scheduleId, name, cycle, dates, validStart, validEnd, inspectorName);

//            删除定检任务
            patrolShops.scheduleCheckDelete(scheduleId);

//            定检任务列表
            patrolShops.checkScheduleCheckNotExist(scheduleId, name);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建定检任务-参数为空
     */
    @Test(dataProvider = "EMPTY_PARA_SCHEDULE_CHECK")
    public void scheduleCheckNewEmptyPara(String para) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + para + "=null";

        String caseDesc = "新建定检任务-" + para + "=null";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取定检员列表
            JSONArray list = patrolShops.inspectorList().getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，定检员列表为空！");
            }

            String inspectorId = list.getJSONObject(0).getString("id");
            String inspectorName = list.getJSONObject(0).getString("name");

//            获取可巡检门店列表
            String districtCode = "";
            list = patrolShops.scheduleCheckShopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }

            String shopId = list.getJSONObject(0).getString("id");

//            新建定检任务
            String name = ciCaseName;
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();

            patrolShops.addScheduleCheckEmptyPara(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId, para);

//            定检任务列表
            long scheduleId = patrolShops.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            删除定检任务
            patrolShops.scheduleCheckDelete(scheduleId);

//            定检任务列表
            patrolShops.checkScheduleCheckNotExist(scheduleId, name);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 编辑定检任务-参数为空
     */
    @Test(dataProvider = "EMPTY_PARA_SCHEDULE_CHECK")
    public void scheduleCheckEditEmptyPara(String para) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName + para + "=null";

        String caseDesc = "新建定检任务-" + para + "=null";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取定检员列表
            JSONArray list = patrolShops.inspectorList().getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，定检员列表为空！");
            }

            String inspectorId = list.getJSONObject(0).getString("id");
            String inspectorName = list.getJSONObject(0).getString("name");

//            获取可巡检门店列表
            String districtCode = "";
            list = patrolShops.scheduleCheckShopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }

            String shopId = list.getJSONObject(0).getString("id");

//            新建定检任务
            String name = ciCaseName;
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();

            patrolShops.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId = patrolShops.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            patrolShops.scheduleCheckEditEmptyPara(scheduleId, name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId, para);

//            删除定检任务
            patrolShops.scheduleCheckDelete(scheduleId);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建/编辑定检任务-参数为空
     */
    @Test
    public void scheduleCheckTrim() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务-name两边有空格";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取定检员列表
            JSONArray list = patrolShops.inspectorList().getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，定检员列表为空！");
            }

            String inspectorId = list.getJSONObject(0).getString("id");
            String inspectorName = list.getJSONObject(0).getString("name");

//            获取可巡检门店列表
            String districtCode = "";
            list = patrolShops.scheduleCheckShopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }

            String shopId = list.getJSONObject(0).getString("id");

//            新建定检任务
            String name = ciCaseName;
            String nameTrim = "  " + ciCaseName + "   ";
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();

            patrolShops.addScheduleCheck(nameTrim, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId = patrolShops.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            patrolShops.scheduleCheckEdit(scheduleId, nameTrim, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            patrolShops.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            删除定检任务
            patrolShops.scheduleCheckDelete(scheduleId);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建/编辑定检任务-重名
     */
    @Test
    public void scheduleCheckDumpName() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务-name两边有空格";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取定检员列表
            JSONArray list = patrolShops.inspectorList().getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，定检员列表为空！");
            }

            String inspectorId = list.getJSONObject(0).getString("id");
            String inspectorName = list.getJSONObject(0).getString("name");

//            获取可巡检门店列表
            String districtCode = "";
            list = patrolShops.scheduleCheckShopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }

            String shopId = list.getJSONObject(0).getString("id");

//            新建定检任务
            String name = ciCaseName;
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();

            patrolShops.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId1 = patrolShops.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            再次新建
            patrolShops.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId2 = patrolShops.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            patrolShops.scheduleCheckEdit(scheduleId2, name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            删除定检任务
            patrolShops.scheduleCheckDelete(scheduleId1);
            patrolShops.scheduleCheckDelete(scheduleId2);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建/编辑定检任务-字数在边界上
     */
    @Test
    public void scheduleCheckWordLimit() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务-字数在边界上";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取定检员列表
            JSONArray list = patrolShops.inspectorList().getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，定检员列表为空！");
            }

            String inspectorId = list.getJSONObject(0).getString("id");
            String inspectorName = list.getJSONObject(0).getString("name");

//            获取可巡检门店列表
            String districtCode = "";
            list = patrolShops.scheduleCheckShopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }

            String shopId = list.getJSONObject(0).getString("id");

//            新建定检任务
            String name = word20;
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();

            patrolShops.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId1 = patrolShops.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            patrolShops.scheduleCheckEdit(scheduleId1, name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            删除定检任务
            patrolShops.scheduleCheckDelete(scheduleId1);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建定检任务-字数在边界外
     */
    @Test
    public void scheduleCheckNewWordLimitOut() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务-字数在边界外";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取定检员列表
            JSONArray list = patrolShops.inspectorList().getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，定检员列表为空！");
            }

            String inspectorId = list.getJSONObject(0).getString("id");

//            获取可巡检门店列表
            String districtCode = "";
            list = patrolShops.scheduleCheckShopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }

            String shopId = list.getJSONObject(0).getString("id");

//            新建定检任务
            String name = word20 + "1";
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();

            String res = patrolShops.addScheduleCheckNoCode(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

            patrolShops.checkCode(res, StatusCode.BAD_REQUEST, "");
            patrolShops.checkMessage("", res, "");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建/编辑定检任务-字数在边界外
     */
    @Test
    public void scheduleCheckEditWordLimitOut() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务-字数在边界外";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取定检员列表
            JSONArray list = patrolShops.inspectorList().getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，定检员列表为空！");
            }

            String inspectorId = list.getJSONObject(0).getString("id");
            String inspectorName = list.getJSONObject(0).getString("name");

//            获取可巡检门店列表
            String districtCode = "";
            list = patrolShops.scheduleCheckShopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }

            String shopId = list.getJSONObject(0).getString("id");

//            新建定检任务
            String name = word20 + "1";
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();

            patrolShops.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId1 = patrolShops.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            String res = patrolShops.scheduleCheckEditNoCode(scheduleId1, name + "1", cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

            patrolShops.checkCode(res, StatusCode.BAD_REQUEST, "");
            patrolShops.checkMessage("", res, "");

//            删除定检任务
            patrolShops.scheduleCheckDelete(scheduleId1);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        patrolShops.login();
    }

    @AfterClass
    public void clean() {
        feidan.clean();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    @DataProvider(name = "EMPTY_PARA_CHECK_LIST")
    public Object[] emptyParaCheckList() {
        return new Object[]{
                "name", "desc", "items-title", "items", "shop_list"
        };
    }

    @DataProvider(name = "CHECK_LIST_WORD_LIMIT_OUT")
    public Object[][] checkListWordLimitOut() {
        return new Object[][]{
//name,desc,title,comment
                new Object[]{
                        word20 + "1", "desc", "titile", "comment", "message"
                },
                new Object[]{
                        "name", word100 + "1", "titile", "comment", "message"
                },
                new Object[]{
                        "name", "desc", word30 + "1", "comment", "message"
                },
                new Object[]{
                        "name", "desc", "titile", word100 + "1", "message"
                },
        };
    }

    @DataProvider(name = "EMPTY_PARA_SCHEDULE_CHECK")
    public Object[] emptyParaScheduleCheck() {
        return new Object[]{
                "name", "cycle", "dates", "send_time", "valid_start", "valid_end", "inspector_id", "shop_list"
        };
    }
}

