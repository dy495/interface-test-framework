package com.haisheng.framework.testng.patrolShops;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.time.LocalDate;

public class PatrolShopsPCNotNulDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private Case aCase = new Case();

    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    DateTimeUtil dt = new DateTimeUtil();
    PatrolShops patrolShops = new PatrolShops();

    String word20 = "12345678901234567890";
    String word100 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
    String word30 = "123456789012345678901234567890";



//    ------------------------------------------------定检任务相关接口---------------------------------------------------------

    /**
     * 巡店中心-获取巡检员列表
     */
    @Test
    public void inspectorListNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "获取巡检员列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取巡检员列表
            JSONArray list = patrolShops.inspectorList().getJSONArray("list");

            Object[] objects = inspectorsListNotNull();

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                }
            }
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-获取可巡检门店
     */
    @Test
    public void shopListNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "获取可巡检门店";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取可巡检门店
            String inspectorId = "";
            String districtCode = "";
            JSONArray list = patrolShops.shopList(inspectorId,districtCode).getJSONArray("list");

            Object[] objects = shopListNotNull();

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                }
            }
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-新建定检任务
     */
    @Test
    public void addScheduleCheckNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "新建定检任务";

        logger.info("\n\n" + caseName + "\n");

        try {

//            新建定检任务
            String name = ciCaseName;
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();
            String inspectorId = "";
            String shopId = "";

            patrolShops.addScheduleCheck(name, cycle, dates, sendTime, validStart, validEnd,
                    inspectorId, shopId);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-定检任务列表
     */
    @Test
    public void scheduleCheckListNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "定检任务列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            定检任务列表
            JSONObject data = patrolShops.checkListPage(1, 10);

//            data的校验
            Object[] objects = shceduleCheckPageNotNull();

            for (int j = 0; j < objects.length; j++) {
                checkUtil.checkNotNull(caseDesc, data, objects[j].toString());
            }

//            list的校验
            Object[] objects1 = shceduleCheckPageListNotNull();

            JSONArray list = data.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects1.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects1[j].toString());
                }
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-删除定检任务
     */
    @Test
    public void scheduleCheckDeleteNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "删除定检任务";

        logger.info("\n\n" + caseName + "\n");

        try {

//            定检任务列表
            JSONArray checkList = patrolShops.checkListPage(1, 10).getJSONArray("list");
            long scheduleId = checkList.getJSONObject(0).getLongValue("id");

//            编辑定检任务
            String name = ciCaseName;
            String cycle = "WEEK";
            String dates = "\"MON\"";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();
            String inspectorId = "";
            String shopId = "";
            patrolShops.scheduleCheckEdit(scheduleId, name, cycle, dates, sendTime, validStart, validEnd, inspectorId, shopId);

//            删除定检任务
            patrolShops.scheduleCheckDelete(scheduleId);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

//    -----------------------------------------------巡店中心----------------------------------------------------------

    /**
     * 巡店中心-获取门店列表
     */
    @Test
    public void shopPageNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "获取门店列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取门店列表

            String shipId = "";
            int status = 1;//1是合格
//            int status = 0;//0是不合格

            JSONArray list = patrolShops.shopPage(shipId, status).getJSONArray("list");

            Object[] objects = shopPageNotNull();

            for (int i = 0; i < list.size(); i++) {
                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                }
            }
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-获取门店详情
     */
    @Test
    public void shopDetailNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "获取门店详情";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取门店列表
            String shipId = "";
            int status = 1;//1是合格
//            int status = 0;//0是不合格

            JSONArray list = patrolShops.shopPage(shipId, status).getJSONArray("list");

            Object[] objects = shopDetailNotNull();

            for (int i = 0; i < list.size(); i++) {
                String id = list.getJSONObject(i).getString("id");

                JSONObject data = patrolShops.shopDetail(id);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, data, objects[j].toString());
                }
            }
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-获取门店巡店记录列表
     */
    @Test
    public void shopChecksPageNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "获取门店巡店记录列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取门店列表
            String shipId = "";
            int checkResult = 1;//1是不合格
//            int checkResult = 0;//0是合格

            int handleStatus = 0;//无需处理
//            int handleStatus = 1;//已处理
//            int handleStatus = 2;//待处理

            String inspectorId = "";
            String orderRule = "ASC";
//            String orderRule = "DESC";//默认
            JSONArray list = patrolShops.shopChecksPage(checkResult, handleStatus, inspectorId, orderRule).getJSONArray("list");

            Object[] objects = shopChecksPageNotNull();

            for (int i = 0; i < list.size(); i++) {

                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                }
            }
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-获取门店巡店记录详情
     */
    @Test
    public void shopChecksDetailNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "获取门店巡店记录详情";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取门店列表
            String shipId = "";
            int checkResult = 1;//1是不合格
//            int checkResult = 0;//0是合格

            int handleStatus = 0;//无需处理
//            int handleStatus = 1;//已处理
//            int handleStatus = 2;//待处理

            String inspectorId = "";
            String orderRule = "ASC";
//            String orderRule = "DESC";//默认
            JSONArray list = patrolShops.shopChecksPage(checkResult, handleStatus, inspectorId, orderRule).getJSONArray("list");

            Object[] objects = shopChecksDetailNotNull();

            for (int i = 0; i < list.size(); i++) {

                int id = list.getJSONObject(i).getInteger("id");

                JSONObject data = patrolShops.shopChecksDetail(id);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, data, objects[j].toString());
                }
            }
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-获取设备实时流/重播流
     */
    @Test
    public void shopDeviceLiveNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "获取设备实时流/重播流";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取设备实时流
            long deviceId = 0l;
            JSONArray list = patrolShops.shopDeviceLive(deviceId).getJSONArray("list");

            Object[] objects = shopDeviceLiveNotNull();

            for (int i = 0; i < list.size(); i++) {

                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                }
            }

//            获取重播流
            String date = dateTimeUtil.timestampToDate("yyyy-MM-dd",
                    System.currentTimeMillis() - 24 * 60 * 60 * 1000);

            String time = "09:01:00";

            list = patrolShops.shopDeviceReplay(deviceId, date, time).getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {

                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                }
            }
        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-开始或继续巡店
     */
    @Test
    public void shopChecksStartNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "开始或继续巡店-校验返回值非空";

        logger.info("\n\n" + caseName + "\n");

        try {

//            开始或继续巡店
            String checkType = "";
            int reset = 0;//是否检查清单 0否1是
            long taskId = 0;
            JSONObject data = patrolShops.shopCheckStart(checkType, reset, taskId);

            Object[] objects = shopChecksStartNotNull();

            for (int j = 0; j < objects.length; j++) {
                checkUtil.checkNotNull(caseDesc, data, objects[j].toString());
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-提交巡检项目结果
     */
    @Test
    public void shopChecksItemSubmitNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "开始或继续巡店-提交巡检项目结果";

        logger.info("\n\n" + caseName + "\n");

        try {

//            开始或继续巡店
            String checkType = "";
            int reset = 0;//是否检查清单 0否1是
            long taskId = 0;
            JSONObject data = patrolShops.shopCheckStart(checkType, reset, taskId);
            long patrolId = data.getLong("id");
            long listId = data.getJSONArray("check_lists").getJSONObject(0).getLongValue("id");
            long itemId = data.getJSONArray("check_lists").getJSONObject(0).getJSONArray("check_items").getJSONObject(0).getLongValue("id");

//            提交巡检项目结果
            int checkResult = 0;//0合格，1不合格，2不适用
            String auditComment = "";
            String picList = "";
            JSONObject dataSubmit = patrolShops.checksItemSubmit(patrolId, listId, itemId, checkResult, auditComment, picList);

            Object[] objects = shopChecksItemSubmitNotNull();

            for (int j = 0; j < objects.length; j++) {
                checkUtil.checkNotNull(caseDesc, dataSubmit, objects[j].toString());
            }

//            提交巡店结果
            String comment = "ddsds";
            patrolShops.checksSubmit(patrolId, comment);//返回值中data为空

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-门店当前清单项目列表
     */
    @Test
    public void problemItemsNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "门店当前清单项目列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            门店当前清单项目列表
            JSONArray list = patrolShops.problemItems().getJSONArray("list");

            Object[] objects = problemItemsNotNull();

            for (int i = 0; i < list.size(); i++) {

                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                }
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-负责人
     */
    @Test
    public void problemResponseorsNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "门店负责人";

        logger.info("\n\n" + caseName + "\n");

        try {

//            门店负责人列表
            JSONArray list = patrolShops.problemResponsors().getJSONArray("list");

            Object[] objects = problemResponsorsNotNull();

            for (int i = 0; i < list.size(); i++) {

                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                }
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-留痕
     */
    @Test
    public void problemMarkNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "留痕";

        logger.info("\n\n" + caseName + "\n");

        try {

//            开始或继续巡店
            String checkType = "";
            int reset = 0;//是否检查清单 0否1是
            long taskId = 0;
            JSONObject data = patrolShops.shopCheckStart(checkType, reset, taskId);
            long listId = data.getJSONArray("check_lists").getJSONObject(0).getLongValue("id");
            long itemId = data.getJSONArray("check_lists").getJSONObject(0).getJSONArray("check_items").getJSONObject(0).getLongValue("id");

            String responsorId = "";
            String auditComment = "";
            String picList = "";

//            留痕
            patrolShops.problemMark(listId, itemId, responsorId, auditComment,picList);

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-门店巡检员列表
     */
    @Test
    public void shopInspectorNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "门店巡检员列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            门店巡检员列表
            JSONArray list = patrolShops.shopInspectors().getJSONArray("list");

            Object[] objects = shopInspectorsNotNull();

            for (int i = 0; i < list.size(); i++) {

                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                }
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-获取巡店结果筛选项
     */
    @Test
    public void resultTypeListNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "巡店中心-获取巡店结果筛选项";

        logger.info("\n\n" + caseName + "\n");

        try {

//            门店巡检员列表
            JSONArray list = patrolShops.resultTypeList().getJSONArray("list");

            Object[] objects = resultTypeListNotNull();

            for (int i = 0; i < list.size(); i++) {

                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                }
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 巡店中心-获取巡店结果筛选项
     */
    @Test
    public void statusTypeListNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "巡店中心-获取巡店处理状态筛选项";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取巡店处理状态筛选项
            JSONArray list = patrolShops.statusTypeList().getJSONArray("list");

            Object[] objects = statusTypeListNotNull();

            for (int i = 0; i < list.size(); i++) {

                JSONObject single = list.getJSONObject(i);

                for (int j = 0; j < objects.length; j++) {
                    checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                }
            }

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            patrolShops.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    //4.16写完啦，该写其他的了~

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
        patrolShops.clean();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        aCase = new Case();
    }
//    -----------------------------------------巡店中心相关接口------------------------------------------------------

    @DataProvider(name = "SHOP_PAGE_NOT_NULL")
    public Object[] shopPageNotNull() {
        return new Object[]{
                "id", "name", "patrol_num", "last_patrol_time", "inspector_name", "inspector_name", "status_name"
        };
    }

    @DataProvider(name = "SHOP_DETAIL_NOT_NULL")
    public Object[] shopDetailNotNull() {
        return new Object[]{
                "id", "name", "address", "manager", "manager_phone", "schedule_check_rule", "check_lists"
        };
    }

    @DataProvider(name = "SHOP_CHECKS_PAGE_NOT_NULL")
    public Object[] shopChecksPageNotNull() {
        return new Object[]{
                "id", "shop_id", "check_time", "inspector_name", "check_result",
                "check_result_name", "handle_status", "handle_status_name"
        };
    }

    @DataProvider(name = "SHOP_CHECKS_DETAIL_NOT_NULL")
    public Object[] shopChecksDetailNotNull() {
        return new Object[]{
                "id", "shop_id", "check_time", "inspector_name", "check_type",
                "check_type_name", "inappropriate_num", "qualified_num", "unqualified_num", "submit_comment",
                "check_lists", "timestamp", "request_id"
        };
    }

    @DataProvider(name = "SHOP_DEVICE_LIVE_NOT_NULL")
    public Object[] shopDeviceLiveNotNull() {
        return new Object[]{
                "stream_url", "expire_time"
        };
    }

    @DataProvider(name = "SHOP_CHECKS_START_NOT_NULL")
    public Object[] shopChecksStartNotNull() {
        return new Object[]{
                "[check_lists]-id", "[check_lists]-name", "[check_lists]-total", "[check_lists]-checked",
                "[check_lists]-check_items",
        };
    }

    @DataProvider(name = "CHECKS_ITEM_SUBMIT_NOT_NULL")
    public Object[] shopChecksItemSubmitNotNull() {
        return new Object[]{
                "id", "order", "title", "check_result", "audit_comment", "item_comment",
                "[pic_list]-pic_path", "[pic_list]-show_url",
        };
    }

    @DataProvider(name = "PROBLEM_ITEMS_NOT_NULL")
    public Object[] problemItemsNotNull() {
        return new Object[]{
                "id", "name", "[check_items]-id", "[check_items]-title",
        };
    }

    @DataProvider(name = "PROBLEM_RESPONSORS_NOT_NULL")
    public Object[] problemResponsorsNotNull() {
        return new Object[]{
                "id", "name"
        };
    }

    @DataProvider(name = "SHOP_INSPECTORS_NOT_NULL")
    public Object[] shopInspectorsNotNull() {
        return new Object[]{
                "id", "name", "phone"
        };
    }

    @DataProvider(name = "RESULT_TYPE_LIST_NOT_NULL")
    public Object[] resultTypeListNotNull() {
        return new Object[]{
                "type", "type_name"
        };
    }

    @DataProvider(name = "STATUS_TYPE_LIST_NOT_NULL")
    public Object[] statusTypeListNotNull() {
        return new Object[]{
                "type", "type_name"
        };
    }


//    ---------------------------------------------------------定检任务相关接口----------------------------------------------

    @DataProvider(name = "INSPECTORS_LIST_NOT_NULL")
    public Object[] inspectorsListNotNull() {
        return new Object[]{
                "id", "name", "phone"
        };
    }

    @DataProvider(name = "SHOP_LIST_NOT_NULL")
    public Object[] shopListNotNull() {
        return new Object[]{
                "id", "name", "district_code", "district_name"
        };
    }

    @DataProvider(name = "SCHEDULE_CHECK_PAGE_NOT_NULL")
    public Object[] shceduleCheckPageNotNull() {
        return new Object[]{
                "total", "pages", "size", "page","page_size",
        };
    }

    @DataProvider(name = "SCHEDULE_CHECK_PAGE_LIST_NOT_NULL")
    public Object[] shceduleCheckPageListNotNull() {
        return new Object[]{
                "id", "name", "cycle", "dates","send_time",
                "valid_start","valid_end","inspector_id","inspector_name","status",
                "status_name","[shop_list]-id","[shop_list]-name","[shop_list]-district_code"
        };
    }
}

