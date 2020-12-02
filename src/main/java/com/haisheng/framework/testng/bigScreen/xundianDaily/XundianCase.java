package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author : yu
 * @date :  2020/05/30
 */

public class XundianCase extends TestCaseCommon implements TestCaseStd {
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    String xjy4 = "uid_663ad653";
    String test = "uid_ef6d2de5";
    int page = 1;
    int size = 50;
    public String adminName = "yuexiu@test.com";
    public String adminPasswd = "f5b3e737510f31b88eb2d4b5d0cd2fb4";
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/64.txt";  //巡店不合格图片base64


    //读取文件内容
    public String texFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str = in.readLine();
        return str;
    }

    public String getPicList(String filename) throws Exception {
        String pic_data0 = texFile(filepath);
        JSONObject pic = xd.picUpload(1, pic_data0);
        String pic_list0 = pic.getString("pic_path");
        return pic_list0;
    }


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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";

        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "巡店 日常");
        commonConfig.pushRd = new String[]{"13581630214", "15084928847"};


        //set shop id
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("xundian " + xd);

        xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");


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
     * ====================执行清单======================
     */
    @Test
    public void checkListAdd() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            //新增一个执行清单
            int startM = 2;
            String name = dt.getHHmm(startM) + "qingqing";
            String desc = "是青青创建的哦，为了测试用的";
            JSONArray items = new JSONArray();//new一个数组
            JSONObject jsonObject = new JSONObject();//数组里面是JSONObject
            jsonObject.put("order", 0);
            jsonObject.put("title", "我是青青第一线");
            jsonObject.put("comment", "要怎么检查啊啊");
            items.add(0, jsonObject);
            JSONArray shoplist = new JSONArray();
            shoplist.add(0, 28764);
            JSONObject res = xd.checkListAdd(name, desc, items, shoplist);
            int code_add = res.getInteger("code");


            //获取执行清单列表，取第一个执行清单的id值
            JSONArray list = xd.checklistPage(page, size).getJSONArray("list");
            long id = list.getJSONObject(0).getInteger("id");


            //编辑一个执行清单
            String name_one = dt.getHHmm(startM) + "qingqingb";
            JSONObject res_one = xd.checkListEdit((long) id, name_one, desc, items, shoplist);
            int code_edit = res_one.getInteger("code");


            //删除一个执行清单
            JSONObject res_delete = xd.checkListDelete(id);
            int code_delete = res_delete.getInteger("code");


            Preconditions.checkArgument(code_add == 1000, "新建执行清单失败，code=" + code_add);
            Preconditions.checkArgument(!StringUtils.isEmpty(list), "执行清单列表获取异常");
            Preconditions.checkArgument(code_edit == 1000, "编辑执行清单失败，code=" + code_edit);
            Preconditions.checkArgument(code_delete == 1000, "删除执行清单失败，code=" + code_delete);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("PC端巡检配置新建执行清单");
        }

    }

    /**
     * ====================定检任务======================
     */
    @Test
    public void scheduleRuleAdd() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            //新建定检任务
            int startM = 1;
            String names = "qingqing" + dt.getHHmm(startM);
            String cycle = "WEEK";
            JSONArray jal = new JSONArray();
            jal.add(0, "MON");
            jal.add(0, "TUES");
            jal.add(0, "WED");
            jal.add(0, "THUR");
            jal.add(0, "FRI");
            jal.add(0, "SAT");
            jal.add(0, "SUN");
            String send_time = dt.getHHmm(0);
            String valid_start = dt.getHistoryDate(0);
            ;
            String valid_end = dt.getHistoryDate(startM);
            ;
            JSONArray shoplist = new JSONArray();
            shoplist.add(0, 28760);
            JSONObject res = xd.scheduleCheckAdd(names, cycle, jal, send_time, valid_start, valid_end, test, shoplist);
            int code_add = res.getInteger("code");

            //获取定检任务列表，取第一个执行清单的id值
            JSONArray list = xd.scheduleCheckPage(page, size).getJSONArray("list");
            long id = list.getJSONObject(0).getInteger("id");

            //编辑一个定检任务
            String name_one = dt.getHHmm(startM) + "qingqinga";
            JSONObject res_one = xd.scheduleCheckEdit(id, name_one, cycle, jal, send_time, valid_start, valid_end, test, shoplist);
            int code_edit = res_one.getInteger("code");


            //删除一个定检任务
            JSONObject res_delete = xd.scheduleCheckDelete(id);
            int code_delete = res_delete.getInteger("code");


            Preconditions.checkArgument(code_add == 1000, "新建定检任务失败，code=" + code_add);
            Preconditions.checkArgument(!StringUtils.isEmpty(list), "定检任务列表获取异常");
            Preconditions.checkArgument(code_edit == 1000, "编辑定检任务失败，code=" + code_edit);
            Preconditions.checkArgument(code_delete == 1000, "删除定检任务失败，code=" + code_delete);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("PC端巡检配置新建定检任务");
        }

    }

    /**
     * ====================定检规则======================
     */
    @Test
    public void createdScheduleCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            String end_time = "03:00";//获取当前时间
            int interval_hour = 1; //间隔时间;
            String start_time = "09:00"; //今天日期;;
            String name = "青青测试用";
            JSONArray shoplist = new JSONArray();
            shoplist.add(0, 4116);

            //新建定检规则
            JSONObject res = xd.scheduleRuleAdd(name, start_time, end_time, interval_hour, shoplist);
            int code = res.getInteger("code");


            //----------------------获取列表刚刚我新建的定检规则的id
            JSONObject response = xd.scheduleRuleList(page, size);
            JSONArray list = response.getJSONArray("list");
            int id = 0;
            int status = 0;
            for (int i = 0; i < list.size(); i++) {
                String the_name = list.getJSONObject(i).getString("name");
                if (the_name.equals(name)) {
                    id = list.getJSONObject(i).getInteger("id");
                    status = list.getJSONObject(i).getInteger("status");
                }
            }

            //将新建的定检规则设置为开或者关(0为关，1为开)
            int code_swi = 0;
            if (status == 0) {
                code_swi = xd.scheduleRuleSwith(id, status).getInteger("code");
            } else {
                code_swi = xd.scheduleRuleSwith(id, status).getInteger("code");
            }


            //编辑定检规则
            int interval_hours = 2;
            JSONObject response_two = xd.scheduleRuleEdit(name, start_time, end_time, interval_hours, shoplist, id);
            int code_two = response_two.getInteger("code");


            //删除定检规则
            JSONArray rule_ids = new JSONArray();
            rule_ids.add(0, id);
            JSONObject res_thr = xd.scheduleRuleDelete(rule_ids);
            int code_thr = res_thr.getInteger("code");


            Preconditions.checkArgument(code == 1000, "新建定检规则失败，code=" + code);
            Preconditions.checkArgument(code_swi == 1000, "定检规则的开关按钮启动失败，code=" + code_swi);
            Preconditions.checkArgument(code_two == 1000, "编辑定检规则失败，code=" + code_two);
            Preconditions.checkArgument(code_thr == 1000, "删除定检规则失败，code=" + code_thr);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("PC端巡检配置新建定检规则");
        }

    }

    /**
     * ====================巡店中心列表======================
     */
    @Test
    public void patrolShopPage() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            int page = 1;
            int size = 10;

            xd.ShopPage(page, size);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("PC端巡店中心列表");
        }

    }


    //=======================================================pc部分的巡店留痕===============================================================

    /**
     * @description :1. 新增门店执行清单，门店详情清单+1 ok
     * @date :2020/6/21 15:19
     **/
    @Test
    public void checklist() {
        logger.logCaseStart(caseResult.getCaseName());
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        String name = "新建清单1" + df.format(new Date());
        String title = "新建清单";
        String comment = "新建清单";
        try {
            JSONObject list = xd.xunDianCenterDetail();
            JSONArray chengeB = list.getJSONArray("check_lists");
            int chengeBefore = chengeB.size();
            xd.CheckListAdd(name, "1", title, comment);

            JSONObject list2 = xd.xunDianCenterDetail();
            JSONArray chengeA = list2.getJSONArray("check_lists");

            int chengeAfter = chengeA.size();
            int i = chengeAfter - chengeBefore;

            Preconditions.checkArgument(i == 1, "新增门店执行清单，门店详情清单没+1");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增门店执行清单，门店详情清单+1");
        }
    }


    /**
     * 2.pc远程巡店清单全部合格 ok
     */
    @Test
    public void remoteXunDianMore() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject list = xd.checkStart("\"REMOTE\"", 1);
            long patrol_id = list.getLong("id");//巡检记录id
            JSONArray check_lists = list.getJSONArray("check_lists");
            if (check_lists.size() == 0) {
                logger.info("该门店未配置执行清单");
                return;
            }

            for (int i = 0; i < check_lists.size(); i++) {
                JSONArray check_items = check_lists.getJSONObject(i).getJSONArray("check_items");
                long listId2 = check_lists.getJSONObject(i).getLong("id");

                for (int j = 0; j < check_items.size(); j++) {
                    long itemId2 = check_items.getJSONObject(j).getLongValue("id");
                    xd.submitOne(1, itemId2, listId2, patrol_id);
                }
            }

            xd.checkSubmit("\"自动化提交全部合格xiaxia\"", patrol_id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc远程巡店全部合格");
        }
    }

    /**
     * @description :3.远程巡店全部不合格 ok
     * @date :2020/6/21 11:32
     **/
    @Test
    public void remotePatrolNoPass() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject list = xd.checkStart("\"REMOTE\"", 1);
            long patrol_id = list.getLong("id");//巡检记录id
            JSONArray check_lists = list.getJSONArray("check_lists");
            String pic_data1 = texFile(filepath);
            String audit_comment = "不合格提交审核意见";

            if (check_lists.size() == 0) {
                logger.info("该门店未配置执行清单");
                return;
            }

            for (int i = 0; i < check_lists.size(); i++) {
                JSONArray check_items = check_lists.getJSONObject(i).getJSONArray("check_items");
                long listId2 = check_lists.getJSONObject(i).getLong("id");
                for (int j = 0; j < check_items.size(); j++) {
                    long itemId2 = check_items.getJSONObject(j).getLongValue("id");
                    //上传现场巡店截图
                    JSONObject pic = xd.picUpload(1, pic_data1);
                    String pic_list0 = pic.getString("pic_path");
                    List<String> pic_listT = new ArrayList<String>();
                    pic_listT.add(pic_list0);
                    //提交不合格图片
                    xd.checksItemSubmitN(patrol_id, listId2, itemId2, pic_listT);
                    //提交单项审核结果
                    xd.submitOne(2, itemId2, listId2, patrol_id, audit_comment);
                }
            }
            xd.checkSubmit("\"自动化提交全不合格xiaxia\"", patrol_id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc远程巡店不合格提交");
        }
    }

    /**
     * pc远程巡店simple ok
     */
    //    @Test
    public void remoteXunDian() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject list = xd.checkStart("\"REMOTE\"", 0);

            long patrol_id = list.getLong("id");//巡检记录id
            JSONArray check_lists = list.getJSONArray("check_lists");


            // long list_id=3029;//巡检清单id
            long list_id = check_lists.getJSONObject(0).getLong("id");


            //获取json array 下标0 的
            JSONObject check_items = check_lists.getJSONObject(0);
//            JSONArray item=check_lists.getJSONArray(check_items);


            //  long item_id=6527;//巡检项目id
            long item_id = check_items.getJSONArray("check_items").getJSONObject(0).getLong("id");
            xd.submitOne(1, item_id, list_id, patrol_id);
            xd.checkSubmit("\"提交\"", patrol_id);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc远程巡店单执行清单");
        }
    }


    /**
     * 4.新建定检任务-执行周期按月 ok
     */
    //@Test
    public void addScheduleCheckMonth() {

        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray list = xd.inspectorList().getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("定检员列表为空");
            }
            String inspectorId = list.getJSONObject(0).getString("id");
            System.out.println(inspectorId);
            String districtCode = "130000";
            list = xd.shopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }
            String shopId = list.getJSONObject(0).getString("id");
            //新建定检任务
            String name = "定检任务·特殊规则月";
            String cycle = "MONTH";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(20).toString();

            List<Integer> data = new ArrayList<Integer>();
            data.add(1);
            data.add(18);
            data.add(19);

            xd.addScheduleCheck(name, cycle, JSON.toJSONString(data), sendTime, validStart, validEnd,
                    inspectorId);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建定检任务-月");
        }
    }

    /**
     * 5.新建定检任务-周期特殊规则 ok
     */
    //@Test
    public void addScheduleCheckSpecially() {

        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray list = xd.inspectorList().getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("定检员列表为空");
            }
            String inspectorId = list.getJSONObject(0).getString("id");
            logger.info("inspectorID:[{}]", inspectorId);

            String districtCode = "130000";
            list = xd.shopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }
            String shopId = list.getJSONObject(0).getString("id");


//            新建定检任务
            String name = "定检任务·特殊规则";
            String cycle = "WEEK";

            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();

            List<String> data = new ArrayList<String>();
            data.add("MON");
            data.add("TUES");
            data.add("WED");

            xd.addScheduleCheck(name, cycle, JSON.toJSONString(data), sendTime, validStart, validEnd,
                    inspectorId);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建定检任务特殊规则");
        }
    }


    /**
     * 7.case1:最多5次留痕 留痕6次  ok
     */
    //@Test
    public void PictureMoreFiveA() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject list = xd.checkStart("\"REMOTE\"", 1);

            long patrol_id = list.getLong("id");//巡检记录id
            JSONArray check_lists = list.getJSONArray("check_lists");

            // long list_id=3029;//巡检清单id
            long list_id = check_lists.getJSONObject(0).getLong("id");

            //获取json array 下标0 的
            JSONObject check_items = check_lists.getJSONObject(0);
//            JSONArray item=check_lists.getJSONArray(check_items);

            //  long item_id=6527;//巡检项目id
            long item_id = check_items.getJSONArray("check_items").getJSONObject(0).getLong("id");


            String pic_list0 = getPicList(filepath);
            String pic_list1 = getPicList(filepath);
            String pic_list2 = getPicList(filepath);
            String pic_list3 = getPicList(filepath);
            String pic_list4 = getPicList(filepath);
            String pic_list5 = getPicList(filepath);


            // 上传5个list
            List<String> pic_listT = new ArrayList<String>();
            pic_listT.add(pic_list0);
            pic_listT.add(pic_list1);
            pic_listT.add(pic_list2);
            pic_listT.add(pic_list3);
            pic_listT.add(pic_list4);
            pic_listT.add(pic_list5);


//            提交留痕照片 ToDO:次数  for循环6次可验证超过五次流痕----返回预期如何写
            int code = xd.checksItemSubmitY(patrol_id, list_id, item_id, pic_listT);

            logger.info("{}", code);
            Preconditions.checkArgument(code == 1001, "六张不合格图片上传成功");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("6次留痕异常验证");
        }
    }

    /**
     * @description :8.留痕5次
     * @date :2020/6/22 16:45
     **/
    @Test
    public void PictureMoreFiveXix() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject list = xd.checkStart("\"REMOTE\"", 1);

            long patrol_id = list.getLong("id");//巡检记录id
            JSONArray check_lists = list.getJSONArray("check_lists");


            // long list_id=3029;//巡检清单id
            long list_id = check_lists.getJSONObject(0).getLong("id");


            //获取json array 下标0 的
            JSONObject check_items = check_lists.getJSONObject(0);
            //  long item_id=6527;//巡检项目id
            long item_id = check_items.getJSONArray("check_items").getJSONObject(0).getLong("id");


            String pic_list0 = getPicList(filepath);
            String pic_list1 = getPicList(filepath);
            String pic_list2 = getPicList(filepath);
            String pic_list3 = getPicList(filepath);
            String pic_list4 = getPicList(filepath);

            List<String> pic_listT = new ArrayList<String>();
            pic_listT.add(pic_list0);
            pic_listT.add(pic_list1);
            pic_listT.add(pic_list2);
            pic_listT.add(pic_list3);
            pic_listT.add(pic_list4);

            xd.checksItemSubmitN(patrol_id, list_id, item_id, pic_listT);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("五次留痕");
        }
    }


    //获取门店的巡店次数
    public Integer patrol_num(JSONObject data) throws Exception {
        Integer patrol_num = 0;
        long shop_id = Long.parseLong(getXunDianShop());
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            long id = list.getJSONObject(i).getInteger("id");
            if (id == shop_id) {
                patrol_num = list.getJSONObject(i).getInteger("patrol_num");
                break;
            }
        }
        return patrol_num;
    }

    //获取门店的最新巡店时间
    public String patrol_time(JSONObject data) throws Exception {
        String patrol_time = "";
        long shop_id = Long.parseLong(getXunDianShop());
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            long id = list.getJSONObject(i).getInteger("id");
            if (id == shop_id) {
                patrol_time = list.getJSONObject(i).getString("last_patrol_time");
                break;
            }
        }
        return patrol_time;
    }

    //巡店
    public Integer xundianP(String commit) {
        int count = 0;
        try {
            //重新巡店
            JSONObject list = xd.checkStart("\"REMOTE\"", 1);
            long patrol_id = list.getLong("id");//巡检记录id
            JSONArray check_lists = list.getJSONArray("check_lists");  //执行清单列表
            if (check_lists.size() <= 0) {
                logger.info("该门店未配置执行清单");
                return 0;
            }

            for (int i = 0; i < check_lists.size(); i++) {
                JSONArray check_items = check_lists.getJSONObject(i).getJSONArray("check_items");  //执行清单项
                long listId2 = check_lists.getJSONObject(i).getLong("id");
                for (int j = 0; j < check_items.size(); j++) {
                    long itemId2 = check_items.getJSONObject(j).getLongValue("id");
                    xd.submitOne(1, itemId2, listId2, patrol_id);  //提交单个清单项，默认合格
                    count = count + 1;
                }
            }
            xd.checkSubmit(commit, patrol_id);  //巡店完成提交
//            xd.checkSubmit("自动化提交全部合格xiaxia",patrol_id);  //巡店完成提交

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc远程巡店");
        }
        return count;
    }

//    /**
//     * @description :9.巡店后，巡店次数加1，且巡店时间更新
//     * @date :2020/6/24 12:20
//     **/
//    @Test
//    public void xundianTimesAndTime() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            String submit_commit = "巡店测试巡店次数、时间更新";
//            //1.获取该店铺原始巡店次数和时间
//            int before_num = 0;
//            String before_time = "";
//            JSONObject data = xd.xunDianCenterPage(1, 10);
//            int pages = data.getInteger("pages");
//
//            for (int i = 1; i < pages; i++) {
//                JSONObject data2 = xd.xunDianCenterPage(i, 10);
//                before_num = patrol_num(data2);
//
//
//                before_time = patrol_time(data2);
//                logger.info("巡店前巡店时间：{}", before_time);
//                if (before_num != 0) {
//                    logger.info("巡店前巡店次数:{}", before_num);
//                    break;
//                }
//            }
//            //2.巡店
//            xundianP(submit_commit);
//            //3.获取新的巡店时间和次数,巡店完成的数据必然是首页第一个数据故直接取0下标
//            int dataAfter = xd.xunDianCenterPage(1, 10).getJSONArray("list").getJSONObject(0).getInteger("patrol_num");
//            String AfterTime = xd.xunDianCenterPage(1, 10).getJSONArray("list").getJSONObject(0).getString("last_patrol_time");
//
//            logger.info("巡店后巡店次数：{}", dataAfter);
//            logger.info("巡店后巡店时间：{}", AfterTime);
//
//            Preconditions.checkArgument((dataAfter - before_num) == 1, "巡店后店铺巡店次数没加1");
//            Preconditions.checkArgument(!before_time.equals(AfterTime), "巡店后店铺巡店时间没更新");
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//            saveData("巡店后次数加1巡店时间更新");
//        }
//    }

    /**
     * @description :10.门店列表页查询
     * @date :2020/6/24 13:56
     **/
    @Test
    public void selectShop() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = xd.xunDianCenterPage(1, 10);
            JSONArray list = data.getJSONArray("list");
            if (list.size() == 0) {
                logger.info("门店中心列表空");
                throw new Exception("门店中心列表空");
            }
            String name = list.getJSONObject(0).getString("name");
            JSONObject AfterSelectData = xd.xunDianCenterselect(1, 10, name);
            String aftername = AfterSelectData.getJSONArray("list").getJSONObject(0).getString("name");
            Preconditions.checkArgument(aftername.equals(name), "查询门店名结果与条件不符");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店名查询");
        }

    }


    /**
     * @description :11.巡店详情查询
     * @date :2020/6/24 15:05
     **/
    @Test
    public void xundianDetail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> xunjianyuan = new ArrayList<String>();
            //获取巡店者名单
            JSONArray list = xd.mendianinSpectorList().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String inspector = list.getJSONObject(i).getString("id");
                xunjianyuan.add(inspector);
            }
            //o 合格 ；1 不合格；0无需处理；1已处理；2待处理

            //1.合格 无需处理，巡检员名任意，状态码一定1000
            xd.xundianDetil(0, 1, 10, 0, xunjianyuan.get(0));
            xd.xundianDetil(0, 1, 10, 0);

            //合格 待处理，巡检员名空  结果返回一定空
            JSONObject list2 = xd.xundianDetil(0, 1, 10, 2);
            JSONArray listY = list2.getJSONArray("list");
            Preconditions.checkArgument(listY.size() == 0, "巡店详情显示异常,合格不应有待处理事项");


            //合格 已处理，巡检员名空  结果返回一定空
            JSONObject list3 = xd.xundianDetil(0, 1, 10, 1);
            JSONArray listZ = list3.getJSONArray("list");
            Preconditions.checkArgument(listZ.size() == 0, "巡店详情显示异常,合格不应有已处理事项");

            //不合格 待处理 巡检员任意,，状态码一定1000
            xd.xundianDetil(1, 1, 10, 2);
            xd.xundianDetil(1, 1, 10, 2, xunjianyuan.get(0));

            //不合格，无需处理 巡检员名空  结果返回一定空
            JSONObject list4 = xd.xundianDetil(1, 1, 10, 0);
            JSONArray listN = list4.getJSONArray("list");
            Preconditions.checkArgument(listN.size() == 0, "巡店详情显示异常,不合格不应有无需处理事项");

            //不合格 已处理 巡检员固定 结果符合且不为空
            JSONArray ll = xd.xundianDetil(1, 1, 10, 2, xunjianyuan.get(0)).getJSONArray("list");
            for (int i = 0; i < ll.size(); i++) {
                String check_result = ll.getJSONObject(i).getString("check_result_name");
                if (!check_result.equals("不合格")) {
                    throw new Exception("查询不合格巡店详情结果出错");
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

        }
    }

    /**
     * @description :12.巡店后巡店门店巡店详情页数据校验
     * @date :2020/6/24 18:11
     **/
    @Test
    public void xundianDetailitem() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String submit_commit1 = "asd";
            //pc 远程巡店 所有巡检清单合格,返回合格项数和
            int quaNum = xundianP(submit_commit1);
            logger.info("巡店合格项数{}", quaNum);
            //巡店详情页列表信息，获取最新一项巡店记录的id
            JSONObject list = xd.xundianDetilpage(1, 10);
            long detailId = list.getJSONArray("list").getJSONObject(0).getLong("id");
            //巡店中心门店列表中最新巡店时间
            String check_time1 = list.getJSONArray("list").getJSONObject(0).getString("check_time");
            String inspector_name1 = list.getJSONArray("list").getJSONObject(0).getString("inspector_name");


            //最新巡店记录详情-详细信息
            JSONObject data = xd.xundianCheckpage(detailId);
            String check_type_name = data.getString("check_type_name");  //巡店方式
            String check_time = data.getString("check_time");            //单条巡店详情页巡店时间
            String commit = data.getString("submit_comment");           //提交说明
            Integer inappropriate_num = data.getInteger("inappropriate_num");         //不适用项数
            Integer unqualified_num = data.getInteger("unqualified_num");         //不合格项数
            Integer qualified_num = data.getInteger("qualified_num");         //合格项数


            Preconditions.checkArgument(check_type_name.equals("远程巡店"), "巡店方式显示错误");
            Preconditions.checkArgument(inappropriate_num == 0, "不适用项数显示错误");
            Preconditions.checkArgument(unqualified_num == 0, "不合格项数显示错误");
            Preconditions.checkArgument(qualified_num == quaNum, "不合格项数显示错误");

            String inspector_name = data.getString("inspector_name");   //巡检员名（pc远程巡店巡检员名未主账号名 可写固定值 ）

            //门店列表页巡店时间
            JSONObject data1 = xd.xunDianCenterPage(1, 10);
            String AfterTime = data1.getJSONArray("list").getJSONObject(0).getString("last_patrol_time");
            String inspector_name2 = data1.getJSONArray("list").getJSONObject(0).getString("inspector_name");

            logger.info("巡店详情列表中巡店时间{}", AfterTime);
            logger.info("门店详情列表中巡店时间{}", check_time1);
            logger.info("单条巡店详情页巡店时间{}", check_time);
            logger.info("提交说明:{}", commit);
            Preconditions.checkArgument(check_time.equals(AfterTime), "巡店时间显示不同步");
            Preconditions.checkArgument(check_time.equals(check_time1), "巡店时间显示不同步");
            Preconditions.checkArgument(inspector_name2.equals(inspector_name), "巡检员显示不同步");
            Preconditions.checkArgument(inspector_name2.equals(inspector_name1), "巡检员显示不同步");
            Preconditions.checkArgument(commit.equals(submit_commit1), "提交说明显示不同步");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("巡店后信息校验");
        }
    }

    /**
     * @description :13 pc巡店提交说明字数限制和非法字符
     * @date :2020/6/25 13:09
     **/
    @Test
    public void wordLimit1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //提交说明超过100 字包含特殊字符，result 接口未对字数作限制
            String comment = ("@#￥%……&*+——90花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆花好月圆100花");
            xundianP(comment);
            JSONObject list = xd.xundianDetilpage(1, 10);
            long detailId = list.getJSONArray("list").getJSONObject(0).getLong("id");
            JSONObject data = xd.xundianCheckpage(detailId);
            String submit_comment = data.getString("submit_comment");           //提交说明
            //取参数comment前100字
//            String submit_commit1=comment.substring(0,100);

//            logger.info("comment100:",submit_commit1);
            logger.info("submit_comment:{}", submit_comment);
            Preconditions.checkArgument(submit_comment.equals(comment), "提交说明显示不同步");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc巡店提交说明字数限制和非法字符");
        }
    }

    /**
     * @description :14.pc特有截屏留痕
     * @date :2020/6/25 16:20
     **/
    //@Test
    public void problemMark() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //JSONObject list = xd.checkStart("\"REMOTE\"", 1); //进入远程巡店
            JSONObject data = xd.problemeItems();

            JSONArray check_lists = data.getJSONArray("list");  //执行清单列表
            long listId = check_lists.getJSONObject(0).getInteger("id"); //取一个执行清单
            //取执行清单的一个执行项
            long itemId = check_lists.getJSONObject(0).getJSONArray("check_items").getJSONObject(0).getLong("id");
            //截屏图片
            String pic_list1 = getPicList(filepath);
            List<String> pic_list = new ArrayList<String>();
            pic_list.add(pic_list1);
            //获取整改处理人
            String responsorId = xd.problemesponsors().getJSONArray("list").getJSONObject(0).getString("id");
            String audit_comment = "pc 截屏留痕推送给门店负责人";
            xd.problemMark(responsorId, listId, itemId, pic_list, audit_comment);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc特有截屏留痕");
        }
    }
}
