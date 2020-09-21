package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.time.LocalDate;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class XunDianPCSTDaily {

    //    入库相关
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private Case aCase = new Case();

    //    方法调用相关
    XunDian xunDian = new XunDian();
    StringUtil su = new StringUtil();
    DateTimeUtil dt = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();

    //    全局变量
    String inspectorId = "uid_8198e69f";
    String inspectorName = "巡检员-2";
    String shopId = "28760";

    //    其他
    String word20 = "12345678901234567890";
    String word100 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
    String word30 = "123456789012345678901234567890";
    String specialChar = "[]@-+~！#$^&()={}|;:'<>.?/·！￥……（）——【】、；：”‘《》。？、,%*";

    /**
     * 执行清单验证
     */
    @Test
    public void checkListCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建-列表-详情-编辑-列表-详情-删除-列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单
            String name = "checkList" + "-" + su.genRandom(3);
            String desc = "清单验证";
            String title = "title";
            String comment = "comment";
            xunDian.addCheckList(name, desc, title, comment);

//            清单列表
            long id = xunDian.checkNewCheckList(name, desc);

//            获取新建时间
            String createTime = xunDian.getCheckListCreateTime();

//            清单详情
            xunDian.checkCheckListDetail(id, name, desc, title, comment);

//            编辑清单
            name = name + "-new";
            desc = desc + "-new";
            title = title + "-new";
            comment = comment + "-new";
            xunDian.checkListEdit(id, name, desc, title, comment);

//            查询清单
            xunDian.checkEditCheckList(id, name, desc, createTime);

//            清单详情
            xunDian.checkCheckListDetail(id, name, desc, title, comment);

//            删除清单
            xunDian.checkListDelete(id);

//            清单列表
            xunDian.checkCheckListNotExist(id, name);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建执行清单验证-参数为空
     */
    @Test
    public void checkListNewEmpty() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单
            String name = "empty-" + su.genRandom(7);
            String desc = "清单验证";
            String title = "title";
            String comment = "comment";

            Object[][] objects = emptyParaCheckList();

            for (int i = 0; i < objects.length; i++) {
                String emptyPara = objects[i][0].toString();
                caseDesc = "新建清单-" + emptyPara + "为空！";

                xunDian.addCheckListEmpty(name, desc, title, comment, emptyPara, objects[i][1].toString());
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
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

        String caseDesc = "新建清单-清单列表-详情-编辑-列表-详情-删除-列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单
            String name = specialChar.substring(0, 20);
            String desc = specialChar;
            String title = specialChar.substring(0, 30);
            String comment = specialChar;

//            本地时间比服务器时间快1s
            xunDian.addCheckList(name, desc, title, comment);

//            获取新建清单的时间
            String createTime = xunDian.getCheckListCreateTime();

//            查询清单
            long id = xunDian.checkNewCheckList(name, desc);

//            清单详情
            xunDian.checkCheckListDetail(id, name, desc, title, comment);

//            编辑清单
            xunDian.checkListEdit(id, name, desc, title, comment);

//            查询清单
            xunDian.checkEditCheckList(id, name, desc, createTime);

//            清单详情
            xunDian.checkCheckListDetail(id, name, desc, title, comment);

//            删除清单
            xunDian.checkListDelete(id);

//            清单列表
            xunDian.checkCheckListNotExist(id, name);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 编辑执行清单验证-参数为空
     */
    @Test
    public void checkListEditEmpty() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单
            String name = ciCaseName;
            String desc = "清单验证";
            String title = "title";
            String comment = "comment";
            xunDian.addCheckList(name, desc, title, comment);

//            查询清单
            long id = xunDian.checkNewCheckList(name, desc);

//            编辑清单
            Object[][] objects = emptyParaCheckList();

            for (int i = 0; i < objects.length; i++) {
                String emptyPara = objects[i][0].toString();
                caseDesc = "编辑清单-" + emptyPara + "为空！";

                xunDian.addCheckListEmpty(su.genRandom(7), desc, title, comment, emptyPara, objects[i][1].toString());
            }

//            清单删除
            xunDian.checkListDelete(id);

//            清单列表
            xunDian.checkCheckListNotExist(id, name);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建和编辑时两边有空格，要自动trim，备注和清单说明也要trim
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
            String name = " " + "trim-" + su.genRandom7() + " ";
            String nameTrim = su.trimStr(name);
            String desc = "  两边空格desc  ";
            String descTrim = su.trimStr(desc);
            String title = "  两边空格标题   ";
            String titleTrim = su.trimStr(title);
            String comment = "   两边空格备注   ";
            String commentTrim = su.trimStr(comment);
            xunDian.addCheckList(name, desc, title, comment);

//            查询清单
            long id = xunDian.checkNewCheckList(nameTrim, descTrim);

//            清单详情
            xunDian.checkCheckListDetail(id, nameTrim, descTrim, titleTrim, commentTrim);

//            编辑清单
            xunDian.checkListEdit(id, name, desc, title, comment);

//            查询清单
            xunDian.checkEditCheckList(id, nameTrim, descTrim, xunDian.getCheckListCreateTime());

//            清单详情
            xunDian.checkCheckListDetail(id, nameTrim, descTrim, titleTrim, commentTrim);

//            删除清单
            xunDian.checkListDelete(id);

//            清单列表
            xunDian.checkCheckListNotExist(id, nameTrim);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
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

        String caseDesc = "新建1-再次新建1（相同名称）-再次新建2（不同名称，相同标题）-将2编辑为与1同名-删除清单";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单1
            String name1 = "dump-" + su.genRandom7();
            String desc = "清单验证";
            String title = "title";
            String comment = "comment";
            xunDian.addCheckList(name1, desc, title, comment);
            long id1 = xunDian.checkNewCheckList(name1, desc);

//            再次新建1(相同名称，不同标题，失败)
            title = "title" + "diff";
            String res = xunDian.addCheckListNoCode(name1, desc, title, comment);
            xunDian.checkCode(res, StatusCode.BAD_REQUEST, "新建清单-重名");
            xunDian.checkMessage("新建清单-重名", res, "清单名称已存在");

//            再次新建2（不同名称，相同标题）
            String name2 = name1 + "diff";
            title = "title";
            xunDian.addCheckList(name2, desc, title, comment);
            xunDian.checkNewCheckList(name2, desc);
            long id2 = xunDian.checkNewCheckList(name2, desc);

//            编辑清单（不同名称，相同title）
            String name3 = "edit-diff-name3";
            xunDian.checkListEdit(id2, name3, desc, title, comment);

//            将2编辑为与1同名
            res = xunDian.checkListEditNoCode(id2, name1, desc, title, comment);
            xunDian.checkCode(res, StatusCode.BAD_REQUEST, "编辑订单-重名");
            xunDian.checkMessage("编辑订单-重名", res, "清单名称已存在");

//            删除清单
            xunDian.checkListDelete(id1);
            xunDian.checkListDelete(id2);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 执行清单验证-字数在边界上
     */
    @Test
    public void checkListWordLimitOn() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建(字数均=边界值)-编辑(字数均=边界值)-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单1
            String name = su.genRandom().substring(0, 20);
            String desc = word100;
            String title = word30;
            String comment = word100;
            xunDian.addCheckList(name, desc, title, comment);

//            清单列表
            long id1 = xunDian.checkNewCheckList(name, desc);

//            编辑清单
            name = ciCaseName;
            String res = xunDian.checkListEditNoCode(id1, name, desc, title, comment);

//            删除清单
            xunDian.checkListDelete(id1);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建执行清单-字数在边界外
     */
    @Test
    public void checkListNewWordLimitOut() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建-编辑-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

            Object[][] objects = checkListWordLimitOutAdd();
            for (int i = 0; i < objects.length; i++) {

//                新建清单
                String res = xunDian.addCheckListNoCode(objects[i][1].toString(), objects[i][2].toString(),
                        objects[i][3].toString(), objects[i][4].toString());
                xunDian.checkCode(res, StatusCode.BAD_REQUEST, "新建执行清单-" + objects[i][0].toString());
                xunDian.checkMessage("新建执行清单-" + objects[i][0].toString(), res, objects[i][5].toString());
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 编辑执行清单-字数在边界外
     */
    @Test
    public void checkListEditWordLimitOut() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建-编辑-删除";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单1
            String nameNew = su.genRandom(20);
            xunDian.addCheckListNoCode(nameNew, "desc", "title", "comment");
            long id1 = xunDian.checkNewCheckList(nameNew, "desc");

            Object[][] objects = checkListWordLimitOutEdit();
            for (int i = 0; i < objects.length; i++) {

//                编辑清单
                String res = xunDian.checkListEditNoCode(id1, objects[i][1].toString(), objects[i][2].toString(),
                        objects[i][3].toString(), objects[i][4].toString());
                xunDian.checkCode(res, StatusCode.BAD_REQUEST, "编辑执行清单-" + objects[i][0].toString());
                xunDian.checkMessage("编辑执行清单-" + objects[i][0].toString(), res, objects[i][5].toString());
            }

//            删除清单
            xunDian.checkListDelete(id1);
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

//    -------------------------------------------定检任务验证----------------------------------------------------

    /**
     * 定检任务验证
     */
    @Test
    public void scheduleCheckCheck() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务-列表-编辑-列表-删除-列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建定检任务
            String name = ciCaseName;
            String cycle = "WEEK";
            String dates = "\"MON\",\"TUES\",\"WED\",\"THUR\",\"FRI\",\"SAT\",\"SUN\"";
            String sendTime = "23:59";
            String validStart = dt.getDaysMinusPlusStr(-1);
            String validEnd = dt.getDaysMinusPlusStr(8);

            xunDian.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd,
                    inspectorId, shopId);

//            定检任务列表
            long scheduleId = xunDian.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            name = ciCaseName;
            cycle = "MONTH";
            dates = "\"" + dt.getDaysMinusPlusInt(1) + "\"";
            sendTime = "23:59";
            validStart = dt.getDaysMinusPlusStr(-1);
            validEnd = dt.getDaysMinusPlusStr(8);
            xunDian.scheduleCheckEdit(scheduleId, name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            xunDian.checkEditScheduleCheck(scheduleId, name, cycle, dates, validStart, validEnd, inspectorName);

//            删除定检任务
            xunDian.scheduleCheckDelete(scheduleId);

//            定检任务列表
            xunDian.checkScheduleCheckNotExist(scheduleId, name);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建/编辑定检任务-名称为特殊字符
     */
    @Test
    public void scheduleCheckSpecialChar() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务-名称为特殊字符";

        logger.info("\n\n" + caseName + "\n");

        try {
//            新建定检任务
            String name = "!@#$%^&*()_";
            String cycle = "WEEK";
            String dates = "\"MON\",\"TUES\",\"WED\",\"THUR\",\"FRI\",\"SAT\",\"SUN\"";
            String sendTime = "23:59";
            String validStart = LocalDate.now().minusDays(4).toString();
            String validEnd = LocalDate.now().plusDays(3).toString();

            xunDian.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId = xunDian.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            xunDian.scheduleCheckEdit(scheduleId, name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            xunDian.checkEditScheduleCheck(scheduleId, name, cycle, dates, validStart, validEnd, inspectorName);

//            删除定检任务
            xunDian.scheduleCheckDelete(scheduleId);

//            定检任务列表
            xunDian.checkScheduleCheckNotExist(scheduleId, name);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建定检任务-参数为空
     */
    @Test
    public void scheduleCheckNewEmptyPara() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建定检任务
            String name = "emptyPara" + su.genRandom(5);
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "23:59";
            String validStart = dt.getDaysMinusPlusStr(-1);
            String validEnd = dt.getDaysMinusPlusStr(8);

            Object[][] objects = emptyParaScheduleCheck();

            for (int i = 0; i < objects.length; i++) {
                caseDesc = "新建定检任务-" + objects[i][0].toString() + "=null";
                xunDian.addScheduleCheckEmptyPara(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId, objects[i][0].toString(), objects[i][1].toString());
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 编辑定检任务-参数为空
     */
    @Test
    public void scheduleCheckEditEmptyPara() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建定检任务
            String name = "empty" + su.genRandom(7);
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = dt.getDaysMinusPlusStr(-1);
            String validEnd = dt.getDaysMinusPlusStr(8);

            xunDian.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId = xunDian.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            Object[][] objects = emptyParaScheduleCheck();
            for (int i = 0; i < objects.length; i++) {
                caseDesc = "编辑定检任务-" + objects[i][0].toString() + "=null";
                xunDian.scheduleCheckEditEmptyPara(scheduleId, name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId, objects[i][0].toString(), objects[i][1].toString());
            }

//            删除定检任务
            xunDian.scheduleCheckDelete(scheduleId);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建/编辑定检任务-两边有空格
     */
    @Test
    public void scheduleCheckTrim() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务-name两边有空格";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建定检任务
            String name = "  " + "blank" + "   ";
            String nameTrim = name.trim();
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "23:59";
            String validStart = dt.getDaysMinusPlusStr(-1);
            String validEnd = dt.getDaysMinusPlusStr(8);

            xunDian.addScheduleCheck(nameTrim, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId = xunDian.checkNewScheduleCheck(nameTrim, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            xunDian.scheduleCheckEdit(scheduleId, name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            xunDian.checkNewScheduleCheck(nameTrim, cycle, dates, validStart, validEnd, inspectorName);

//            删除定检任务
            xunDian.scheduleCheckDelete(scheduleId);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建/编辑定检任务-重名，且名字长度=20
     */
    @Test
    public void scheduleDumpName20() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务-重名";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建定检任务
            String name = word20;
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = dt.getDaysMinusPlusStr(-1);
            String validEnd = dt.getDaysMinusPlusStr(8);

            xunDian.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId1 = xunDian.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            再次新建(允许重名)
            xunDian.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId2 = xunDian.checkNewScheduleCheck(name, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务（允许重名）
            xunDian.scheduleCheckEdit(scheduleId2, name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            删除定检任务
            xunDian.scheduleCheckDelete(scheduleId1);
            xunDian.scheduleCheckDelete(scheduleId2);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
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

//            新建定检任务
            String name = word20 + "1";
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = dt.getDaysMinusPlusStr(-1);
            String validEnd = dt.getDaysMinusPlusStr(8);

            String res = xunDian.addScheduleCheckNoCode(name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

            xunDian.checkCode(res, StatusCode.BAD_REQUEST, "新建定检任务，name=" + name);
            xunDian.checkMessage("新建定检任务，name=" + name, res, "定检任务名称不能超过20字");

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 编辑定检任务-字数在边界外
     */
    @Test
    public void scheduleCheckEditWordLimitOut() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "编辑定检任务-字数在边界外";

        logger.info("\n\n" + caseName + "\n");

        try {
//            新建定检任务
            String name20 = word20;
            String name21 = word20 + "1";
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = dt.getDaysMinusPlusStr(-1);
            String validEnd = dt.getDaysMinusPlusStr(8);

            xunDian.addScheduleCheck(name20, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId1 = xunDian.checkNewScheduleCheck(name20, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            String res = xunDian.scheduleCheckEditNoCode(scheduleId1, name21, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

            xunDian.checkCode(res, StatusCode.BAD_REQUEST, "编辑定检任务，name=" + name21);
            xunDian.checkMessage("编辑定检任务，name=" + name21, res, "定检任务名称不能超过20字");

//            删除定检任务
            xunDian.scheduleCheckDelete(scheduleId1);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 新建定检任务-日期/有效期验证
     */
    @Test(dataProvider = "SCHEDULE_CHECK_MONTH")
    public void scheduleCheckMonthCheck(String validStart, String validEnd, int dates, String sendTime, int expectCode) {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务-列表-编辑-列表-9删除-列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建定检任务
            String name = "scheduleCheckMonth";
            String cycle = "MONTH";

            long scheduleId = 0;

            if (expectCode == 1000) {
                xunDian.addScheduleCheck(name, cycle, "\"" + dates + "\"", sendTime, validStart, validEnd,
                        inspectorId, shopId);

//                定检任务列表
                scheduleId = xunDian.checkNewScheduleCheck(name, cycle, "\"" + dates + "\"", validStart, validEnd, inspectorName);

//                删除定检任务
                xunDian.scheduleCheckDelete(scheduleId);
            } else {
                String res = xunDian.addScheduleCheckNoCode(name, cycle, "\"" + dates + "\"", sendTime, validStart, validEnd,
                        inspectorId, shopId);

                xunDian.checkCode(res, expectCode, "dates=" + dates + "，validStart=" + validStart +
                        ",validEnd=" + validEnd + ",sendTime=" + sendTime);
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

//    /**
//     * 新建定检任务-日期/有效期验证
//     */
//    @Test(dataProvider = "SCHEDULE_CHECK_MONTH")
//    public void scheduleCheckMonthEdit(String validStart, String validEnd, int dates, String sendTime, int expectCode) {
//
//        String ciCaseName = new Object() {
//        }.getClass().getEnclosingMethod().getName();
//
//        String caseName = ciCaseName;
//
//        String caseDesc = "新建定检任务-列表-编辑-列表-9删除-列表";
//
//        logger.info("\n\n" + caseName + "\n");
//
//        try {
//
////            新建定检任务
//            String name = "别删!!!!!!!";
//            String cycle = "MONTH";
//
//            long scheduleId = 406L;
//
//            if (expectCode == 1000) {
//                xunDian.scheduleCheckEdit(scheduleId, name, cycle, "\"" + dates + "\"", sendTime, validStart, validEnd,
//                        inspectorId, shopId);
//            } else {
//                String res = xunDian.scheduleCheckEditNoCode(scheduleId, name, cycle, "\"" + dates + "\"", sendTime, validStart, validEnd,
//                        inspectorId, shopId);
//
//                xunDian.checkCode(res, expectCode, "dates=" + dates + "，validStart=" + validStart +
//                        ",validEnd=" + validEnd + ",sendTime=" + sendTime);
//            }
//
//        } catch (AssertionError e) {
//            failReason = e.toString();
//            aCase.setFailReason(failReason);
//        } catch (Exception e) {
//            failReason = e.toString();
//            aCase.setFailReason(failReason);
//        } finally {
//            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
//        }
//    }

//    -----------------------------------------------巡店中心----------------------------------------------------------

    /**
     * 编辑定检任务-字数在边界外
     */
    @Test
    public void shopChecksStartPc() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "远程巡店一次后，巡店列表验证";

        logger.info("\n\n" + caseName + "\n");

        try {
//            新建定检任务
            String name20 = word20;
            String name21 = word20 + "1";
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = dt.getDaysMinusPlusStr(-1);
            String validEnd = dt.getDaysMinusPlusStr(8);

            xunDian.addScheduleCheck(name20, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            定检任务列表
            long scheduleId1 = xunDian.checkNewScheduleCheck(name20, cycle, dates, validStart, validEnd, inspectorName);

//            编辑定检任务
            String res = xunDian.scheduleCheckEditNoCode(scheduleId1, name21, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

            xunDian.checkCode(res, StatusCode.BAD_REQUEST, "编辑定检任务，name=" + name21);
            xunDian.checkMessage("编辑定检任务，name=" + name21, res, "定检任务名称不能超过20字");

//            删除定检任务
            xunDian.scheduleCheckDelete(scheduleId1);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            xunDian.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }


    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        xunDian.login();
    }

    @AfterClass
    public void clean() {
        xunDian.clean();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        aCase = new Case();
    }

    @DataProvider(name = "EMPTY_PARA_CHECK_LIST")
    public Object[][] emptyParaCheckList() {
        return new Object[][]{
                new Object[]{
                        "name", "清单名称不能为空"
                },
                new Object[]{
                        "desc", "清单说明不能为空"
                },
                new Object[]{
                        "items-title", "清单执行项标题不能为空"
                },
                new Object[]{
                        "items", "清单执行项不能为空"
                },
                new Object[]{
                        "shop_list", "清单应用门店不能为空"
                }
        };
    }

    @DataProvider(name = "CHECK_LIST_WORD_LIMIT_OUT_ADD")
    public Object[][] checkListWordLimitOutAdd() {
        return new Object[][]{
//name,desc,title,comment,message
                new Object[]{
                        "name=21个字", word20 + "1", "desc", "titile", "comment", "清单名称最⻓不超过20个字"
                },
                new Object[]{
                        "desc=101个字", "descLimit" + su.genRandom7(), word100 + "1", "titile", "comment", "清单说明最⻓不超过100个字"
                },
                new Object[]{
                        "title=31个字", "titleLimit" + su.genRandom7(), "desc", word30 + "1", "comment", "执行项标题不能超过30个字"
                },
                new Object[]{
                        "comment=101个字", "commentLimit" + su.genRandom7(), "desc", "titile", word100 + "1", "执行备注不能超过100个字"
                },
        };
    }

    @DataProvider(name = "CHECK_LIST_WORD_LIMIT_OUT_EDIT")
    public Object[][] checkListWordLimitOutEdit() {
        return new Object[][]{
//name,desc,title,comment,message
                new Object[]{
                        "name=21个字", word20 + "1", "desc", "titile", "comment", "清单名称不能超过20个字"
                },
                new Object[]{
                        "desc=101个字", "descLimit" + su.genRandom7(), word100 + "1", "titile", "comment", "清单说明不能超过100个字"
                },
                new Object[]{
                        "title=31个字", "titleLimit" + su.genRandom7(), "desc", word30 + "1", "comment", "执行项标题不能超过30个字"
                },
                new Object[]{
                        "comment=101个字", "commentLimit" + su.genRandom7(), "desc", "titile", word100 + "1", "执行备注不能超过100个字"
                },
        };
    }

    @DataProvider(name = "EMPTY_PARA_SCHEDULE_CHECK")
    public Object[][] emptyParaScheduleCheck() {
        return new Object[][]{
                new Object[]{
                        "name", "任务名称不能为空"
                },
                new Object[]{
                        "cycle", "任务周期不能为空"
                },
                new Object[]{
                        "dates", "任务推送日期不能为空"
                },
                new Object[]{
                        "send_time", "任务推送时间不能为空"
                },
                new Object[]{
                        "valid_start", "有效开始日期不能为空"
                },
                new Object[]{
                        "valid_end", "有效结束日期不能为空"
                },
                new Object[]{
                        "inspector_id", "巡检员id不能为空"
                },
                new Object[]{
                        "shop_list", "应用门店列表不能为空"
                }
        };
    }

    @DataProvider(name = "SCHEDULE_CHECK_MONTH")
    public Object[][] scheduleCheckMonth() {

        //valid_start,valid_end,dates,send_time,result

        return new Object[][]{
//                今天-日期-有效期
                new Object[]{
                        dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusStr(3), dt.getDaysMinusPlusInt(1), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusStr(3), dt.getDaysMinusPlusInt(1), "23:59", 1001
                },

//                日期-今天-有效期
                new Object[]{
                        dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusInt(-1), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusInt(-1), "23:59", 1001
                },

//                日期-有效期-今天
                new Object[]{
                        dt.getDaysMinusPlusStr(-2), dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusInt(-3), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-2), dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusInt(-3), "23:59", 1001
                },

//                今天-有效期-日期
                new Object[]{
                        dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusInt(3), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusInt(3), "23:59", 1001
                },

//                有效期-今天-日期
                new Object[]{
                        dt.getDaysMinusPlusStr(-2), dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusInt(1), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-2), dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusInt(1), "23:59", 1001
                },

//                有效期-日期-今天
                new Object[]{
                        dt.getDaysMinusPlusStr(-3), dt.getDaysMinusPlusStr(-2), dt.getDaysMinusPlusInt(-1), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-3), dt.getDaysMinusPlusStr(-2), dt.getDaysMinusPlusInt(-1), "23:59", 1001
                },


//                今天-有效期（日期在左边界）
                new Object[]{
                        dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusInt(1), "00:10", 1000
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusInt(1), "23:59", 1000
                },

//                有效期、今天和左边界在是同一天
                new Object[]{
                        LocalDate.now().toString(), dt.getDaysMinusPlusStr(1), dt.getdayOfThisMonth(), "00:10", 1001
                },
                new Object[]{
                        LocalDate.now().toString(), dt.getDaysMinusPlusStr(1), dt.getdayOfThisMonth(), "23:59", 1000
                },

//                日期在左边界，今天在有效期内
                new Object[]{
                        dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusInt(-1), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusInt(-1), "23:59", 1001
                },

//                有效期在左边界，日期在右边界
                new Object[]{
                        dt.getDaysMinusPlusStr(-1), LocalDate.now().toString(), dt.getdayOfThisMonth(), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-1), LocalDate.now().toString(), dt.getdayOfThisMonth(), "23:59", 1000
                },

//                有效期-今天（日期在左边界）
                new Object[]{
                        dt.getDaysMinusPlusStr(-2), dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusInt(-2), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-2), dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusInt(-2), "23:59", 1001
                },

//                今天-有效期（日期在右边界）
                new Object[]{
                        dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusInt(2), "00:10", 1000
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusStr(2), dt.getDaysMinusPlusInt(2), "23:59", 1000
                },

//                今天在左边界，日期在右边界
                new Object[]{
                        LocalDate.now().toString(), dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusInt(1), "00:10", 1000
                },
                new Object[]{
                        LocalDate.now().toString(), dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusInt(1), "23:59", 1000
                },

//                今天在有效期内，日期在右边界
                new Object[]{
                        dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusInt(1), "00:10", 1000
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusInt(1), "23:59", 1000
                },

//                今天、右边界、日期在同一天
                new Object[]{
                        dt.getDaysMinusPlusStr(-1), LocalDate.now().toString(), dt.getdayOfThisMonth(), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-1), LocalDate.now().toString(), dt.getdayOfThisMonth(), "23:59", 1000
                },

//                有效期-今天（日期在右边界）
                new Object[]{
                        dt.getDaysMinusPlusStr(-2), dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusInt(-1), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-2), dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusInt(-1), "23:59", 1001
                },

//                今天-有效期（日期在有效期内）
                new Object[]{
                        dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusStr(3), dt.getDaysMinusPlusInt(2), "00:10", 1000
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusStr(3), dt.getDaysMinusPlusInt(2), "23:59", 1000
                },

//                今天-日期（今天和日期均在有效期内）
                new Object[]{
                        dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusStr(3), dt.getDaysMinusPlusInt(1), "00:10", 1000
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusStr(3), dt.getDaysMinusPlusInt(1), "23:59", 1000
                },

//                有效期-今天（日期在有效期内）
                new Object[]{
                        dt.getDaysMinusPlusStr(-3), dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusInt(-2), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-3), dt.getDaysMinusPlusStr(-1), dt.getDaysMinusPlusInt(-2), "23:59", 1001
                },

//                日期-今天（日期和有有效期均在有效期内）
                new Object[]{
                        dt.getDaysMinusPlusStr(-3), dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusInt(-1), "00:10", 1001
                },
                new Object[]{
                        dt.getDaysMinusPlusStr(-3), dt.getDaysMinusPlusStr(1), dt.getDaysMinusPlusInt(-1), "23:59", 1001
                }
        };
    }
}

