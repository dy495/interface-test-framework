package com.haisheng.framework.testng.patrolShops;

import com.alibaba.fastjson.JSONArray;
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

            patrolShops.checkListEdit(name,desc,title,comment);

            patrolShops.checkListEdit(name + "-new", desc + "-new", title + "-new", comment + "-new");

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
     * 执行清单验证-参数为空
     */
    @Test(dataProvider = "EMPTY_PARA_CHECK_LIST")
    public void checkListCheckEmpty(String emptyPara) {

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
            patrolShops.addCheckListEmpty(name, desc, title, comment,emptyPara);

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

        String caseDesc = "新建清单";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建清单
            String name = ciCaseName;
            String desc = "#￥%……&";
            String title = "&*（）";
            String comment = "：“《？》";
            patrolShops.addCheckList(name, desc, title, comment);

//            查询清单
            String createTime = dateTimeUtil.timestampToDate("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
            long id = patrolShops.checkNewCheckList(name, desc, createTime);

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

    @DataProvider(name = "BAD_ADVISER")
    public Object[][] badAdviser() {
        return new Object[][]{
//String channelId, int status, boolean isAudited, String namePhone, int pageSize
                new Object[]{
                        "新建置业顾问（与已启用业务员手机号相同）", "17610248107", "当前手机号已被使用"
                },
                new Object[]{
                        "新建置业顾问（与置业顾问手机号相同）", "16622222222", "当前手机号已被使用"
                }
        };
    }
}

