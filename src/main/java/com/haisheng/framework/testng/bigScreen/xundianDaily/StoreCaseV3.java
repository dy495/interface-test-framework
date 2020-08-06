package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;


/**
 * @author : qingqing
 * @date :  2020/07/06
 */

public class StoreCaseV3 extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtil Md = StoreScenarioUtil.getInstance();
    long shop_id = 4116;
    int startM = 2;


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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";

//
//        //replace backend gateway url
//        //commonConfig.gateway = "";
//
//        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 日常");

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("store " + Md);

        Md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");


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


//    /**
//     * ====================添加事件(结束时间为开始时间&结束时间大于开始时间)======================
//     */
//    @Test(dataProvider = "END_TIME_TYPE", dataProviderClass = StoreScenarioUtil.class)
//    public void thingAddT1(String endTimeType) {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack = false;
//        try {
//            //新增一个正常进行的添加事项
//            String activity_description = "店庆店庆店庆店庆店庆";
//            String activity_type = "NEW_COMMODITY";
//            String start_date = dt.getHistoryDate(0); //今天日期;
//            String end_date = endTimeType;
//
//            int code = Md.activityAddV3(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");
//
//
//            Preconditions.checkArgument(code == 1000, "添加事项不成功");
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("添加事件(结束时间为开始时间&结束时间大于开始时间)");
//        }
//
//    }




    /**
     * @author : guoliya
     * @date :  2020/08/04
     */

    /**
     * ====================列表页排序按照上个整点计算的今日到访人次排序(人次从大到小排序)======================
     */
    @Test
    public void storeLiistRank() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String districtCode = "";
            String[] shopType = {};
            String shopName = "";
            String shopManager = "";
            int page = 1;
            int size = 10;
            JSONObject response = Md.patrolShopRealV3A(districtCode, shopType, shopName, shopManager, page, size);
            JSONArray storeList = response.getJSONArray("list");
            for (int i = 0; i < storeList.size() - 1; i++) {
                System.out.println(i);
                int realtimePv = storeList.getJSONObject(i).getInteger("realtime_pv");
                int realtimePv1 = storeList.getJSONObject(i + 1).getInteger("realtime_pv");
                Preconditions.checkArgument(realtimePv >= realtimePv1, "人次多的数据为" + realtimePv + "人次少的数据为" + realtimePv1);
            }
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("列表页排序按照的今日到访人次从大到小排序");
        }
    }

    /**
     * ====================门店类型（单选、多选、全选、不选）======================
     */
    @Test
    public void storeType() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //多选
            String district_code = "";
            String[] shopType = new String[]{"NORMAL", "COMMUNITY"};
            String shopName = "";
            String shopManager = "";
            int page = 1;
            int size = 10;
            int num = 0;
            int num1 = 0;
            JSONArray storeList = Md.patrolShopRealV3A(district_code, shopType, shopName, shopManager, page, size).getJSONArray("list");
            for (int i = 0; i < storeList.size(); i++) {
                Preconditions.checkArgument(storeList.getJSONObject(i).getString("type").equals("NORMAL") || storeList.getJSONObject(i).getString("type").equals("COMMUNITY"), "筛选栏多选数据有问题");
            }

            //单选
            String[] shopType1 = new String[]{"NORMAL"};
            JSONArray storeList1 = Md.patrolShopRealV3A(district_code, shopType1, shopName, shopManager, page, size).getJSONArray("list");
            for (int j = 0; j < storeList1.size(); j++) {
                Preconditions.checkArgument(storeList1.getJSONObject(j).getString("type").equals("NORMAL"), "筛选栏单选数据有问题");
            }

            //全选
            String[] shopType2 = new String[]{"NORMAL", "COMMUNITY", "PLAZA", "FLAGSHIP"};
            JSONArray storeList2 = Md.patrolShopRealV3A(district_code, shopType2, shopName, shopManager, page, size).getJSONArray("list");
            for (int m = 0; m < storeList2.size(); m++) {
                num++;
                System.out.println("num为  " + num);
                Preconditions.checkArgument(storeList2.getJSONObject(m).getString("type").equals("PLAZA") || storeList2.getJSONObject(m).getString("type").equals("FLAGSHIP") || storeList2.getJSONObject(m).getString("type").equals("COMMUNITY") || storeList2.getJSONObject(m).getString("type").equals("NORMAL"), "筛选栏全选数据有问题");
            }

            //不选
            String[] shopType3 = new String[]{};
            JSONArray storeList3 = Md.patrolShopRealV3A(district_code, shopType3, shopName, shopManager, page, size).getJSONArray("list");
            for (int n = 0; n < storeList3.size(); n++) {
                num1++;
                //System.out.println("num1为  "+num1);
                Preconditions.checkArgument(storeList2.getJSONObject(n).getString("type").equals("PLAZA") || storeList2.getJSONObject(n).getString("type").equals("FLAGSHIP") || storeList2.getJSONObject(n).getString("type").equals("COMMUNITY") || storeList2.getJSONObject(n).getString("type").equals("NORMAL"), "筛选栏不选数据有问题");
            }
            Preconditions.checkArgument(num == num1, "全选：" + num + "不选：" + num1);
        } catch (AssertionError | Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("门店类型（单选、多选、全选、不选）");
        }
    }

    /**
     * ====================模糊搜索======================
     */
    @Test
    public void fuzzySearch() {
        String district_code = "";
        String[] shopType = {};
        String shopName = "t";
        String shopManager = "";
        int page = 1;
        int size = 10;
        try {
            JSONArray storeList = Md.patrolShopRealV3A(district_code, shopType, shopName, shopManager, page, size).getJSONArray("list");
            for (int i = 0; i < storeList.size(); i++) {
                String string = storeList.getJSONObject(i).getString("name");
                if(string != null&&string.contains(shopName)) {
                    Preconditions.checkArgument(true, "\"t\"的模糊搜索的结果为：" + string);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
//    private void shopTypeMd(String[] type,String name){
//        String district_code = "";
//        String[] shopType = type;
//        String shopName = name;
//        String shopManager = "";
//        int page = 1;
//        int size = 10;
//        try {
//            JSONArray storeList = Md.patrolShopRealV3A(district_code, shopType, shopName, shopManager, page, size).getJSONArray("list");
//            for(int i = 0; i < storeList.size(); i++){
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
}
