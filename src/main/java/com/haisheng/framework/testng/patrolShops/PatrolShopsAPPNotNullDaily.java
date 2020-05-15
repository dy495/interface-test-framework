package com.haisheng.framework.testng.patrolShops;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class PatrolShopsAPPNotNullDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

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


//    ---------------------------------------------------移动端首页接口-----------------------------------------------

    /**
     * 巡店中心-获取巡检成果
     */
    @Test
    public void mTaskDetailNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "获取工作成果";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取工作成果
            JSONObject data = patrolShops.mTaskDetail();

            Object[] objects = mTaskDetailNotNull();

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
     * 巡店中心-门店列表
     */
    @Test
    public void mShopListNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "门店列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            获取工作成果
            long lastTime = 0;
            JSONObject data = patrolShops.mShopList(10, lastTime);

            Object[] objects = mShopListNotNull();

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
     * 巡店中心-代办/已办列表
     */
    @Test
    public void mTaskListNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "代办/已办列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            代办/已办列表
            int types[] = {0, 1};
            int size = 0;
            long lastId = 0;

            Object[] objects = mTaskListDataNotNull();
            Object[] objects1 = mTaskListListNotNull();

            for (int i = 0; i < types.length; i++) {
                JSONObject data = patrolShops.mTaskList(types[i], size, lastId);

//                data非空
                checkUtil.checkNotNull(caseDesc, data, objects[i].toString());

//                list非空
                JSONArray list = data.getJSONArray("list");
                for (int k = 0; k < list.size(); k++) {
                    JSONObject single = list.getJSONObject(k);
                    for (int j = 0; j < objects.length; j++) {
                        checkUtil.checkNotNull(caseDesc, single, objects[j].toString());
                    }
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
     * 巡店中心-设备定检图片列表
     */
    @Test
    public void schedulePicListNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "设备定检图片列表";

        logger.info("\n\n" + caseName + "\n");

        try {

//            设备定检图片列表
            long deviceId = 0;
            String date = "";

            Object[] objects = mPicListNotNull();

            JSONArray list = patrolShops.mPicList(deviceId,date).getJSONArray("list");

//                list非空
            for (int k = 0; k < list.size(); k++) {
                JSONObject single = list.getJSONObject(k);
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
     * 巡店中心-复检不合格处理步骤提交
     */
    @Test
    public void taskStepSubmitNotNullTest() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "复检不合格处理步骤提交";

        logger.info("\n\n" + caseName + "\n");

        try {

//            设备定检图片列表
            long deviceId = 0;
            String date = "";

            Object[] objects = mPicListNotNull();

            JSONArray list = patrolShops.mPicList(deviceId,date).getJSONArray("list");

//                list非空
            for (int k = 0; k < list.size(); k++) {
                JSONObject single = list.getJSONObject(k);
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


//    该写5.6啦


    @DataProvider(name = "M_TASK_DETAIL_NOT_NULL")
    public Object[] mTaskDetailNotNull() {
        return new Object[]{
                "check_shops", "handle_tasks", "shops", "tasks"
        };
    }

    @DataProvider(name = "M_SHOP_LIST_NOT_NULL")
    public Object[] mShopListNotNull() {
        return new Object[]{
                "total", "last_time", "[list]-id", "[list]-name", "[list]-address", "[list]-last_patrol_time"
        };
    }

    @DataProvider(name = "M_TASK_LIST_DATA_NOT_NULL")
    public Object[] mTaskListDataNotNull() {
        return new Object[]{
                "total", "last_id"
        };
    }

    @DataProvider(name = "M_TASK_LIST_LIST_NOT_NULL")
    public Object[] mTaskListListNotNull() {
        return new Object[]{
                "id", "task_type", "type_name", "name", "shop_id", "shop_name",
                "shop_address", "time", "content", "audit_comment", "[pic_list]-pic_path", "[pic_list]-show_url",
                "[step_list]-time", "[step_list]-type", "[step_list]-pic_list", "[step_list]-recheck_result",
                "[step_list]-comment", "[step_list]-user"
        };
    }

    @DataProvider(name = "M_PIC_LIST_NOT_NULL")
    public Object[] mPicListNotNull() {
        return new Object[]{
                "id", "time", "{pic}-pic_path", "{pic}-show_url", "shop_id", "shop_name",
                "date"
        };
    }

    @DataProvider(name = "SHOP_DEVICE_LIST_NOT_NULL")
    public Object[] shopDeviceListNotNull() {
        return new Object[]{
                "device_id", "name", "device_type", "type_name", "device_status", "status_name"
        };
    }

    @DataProvider(name = "SHOP_DEVICE_LIVE_NOT_NULL")
    public Object[] shopDeviceLiveNotNull() {
        return new Object[]{
                "stream_url", "expire_time"
        };
    }

    @DataProvider(name = "SHOP_CHECKS_START_DATA_NOT_NULL")
    public Object[] shopChecksStartDataNotNull() {
        return new Object[]{
                "id", "shop_id","check_type","check_type_name","is_new",
        };
    }

    @DataProvider(name = "SHOP_CHECKS_START_LIST_NOT_NULL")
    public Object[] shopChecksStartListNotNull() {
        return new Object[]{
                "id", "name","total","checked","[check_items]-id","[check_items]-order",
                "[check_items]-title", "[check_items]-check_result","[check_items]-pic_list",
                "[check_items]-audit_comment","[check_items]-item_comment"
        };
    }

    @DataProvider(name = "SHOP_CHECKS_ITEM_SUBMIT_NOT_NULL")
    public Object[] shopChecksItemSubmitNotNull() {
        return new Object[]{
                "id", "order","title","check_result","[pic_list]-pic_path","[pic_list]-show_url",
                "audit_comment", "item_comment"
        };
    }

    @DataProvider(name = "PROBLEM_ITEMS_NOT_NULL")
    public Object[] problemItemsNotNull() {
        return new Object[]{
                "id", "name","title","[check_items]-id","[check_items]-title"
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
                "id", "name","phone"
        };
    }
}

