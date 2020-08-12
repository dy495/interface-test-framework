package com.haisheng.framework.testng.bigScreen.xundianOnline;

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


/**
 * @author : qingqing
 * @date :  2020/07/06 10:00
 */

public class StoreCaseV3 extends TestCaseCommon implements TestCaseStd {
    StoreScenarioUtilOnline Md = StoreScenarioUtilOnline.getInstance();
    long shop_id = 13260;
    int startM=2;

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "青青";

//
//        //replace backend gateway url
//        //commonConfig.gateway = "";
//
//        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "mendian-online-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 线上");

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("store " + Md);

        Md.login("salesdemo@winsense.ai","c216d5045fbeb18bcca830c235e7f3c8");


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     *
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }


//    /**
//     *
//     * ====================添加事件(结束时间为开始时间&结束时间大于开始时间)======================
//     * */
//    @Test(dataProvider = "END_TIME_TYPE", dataProviderClass = StoreScenarioUtil.class)
//    public void thingAddT1(String endTimeType) {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        try {
//            //新增一个正常进行的添加事项
//            String activity_description = "店庆店庆店庆店庆店庆";
//            String activity_type = "NEW_COMMODITY";
//            String start_date=dt.getHistoryDate(0); //今天日期;
//            String end_date = endTimeType;
//
//            int code = Md.StoreActivityAdd(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");
//
//
//            Preconditions.checkArgument(code == 1000,"添加事项不成功");
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
//    /**
//     *
//     * ====================添加事件（说明小于20个子&等于20个字）======================
//     * */
//    @Test(dataProvider = "DESCRIPTION", dataProviderClass = StoreScenarioUtil.class)
//    public void thingAddT2(String description) {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        try {
//            //新增一个正常进行的添加事项
//            String activity_description = description;
//            String activity_type = "NEW_COMMODITY";
//            String start_date=dt.getHistoryDate(0); //今天日期;
//            String end_date=dt.getHistoryDate(startM); //今天日期+2;;
//
//            int code = Md.StoreActivityAdd(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");
//
//
//            Preconditions.checkArgument(code == 1000,"添加事项不成功");
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("添加事件（说明小于20个子&等于20个字）");
//        }
//
//    }
//
//    /**
//     *
//     * ====================添加事件（单选一个事件类型）======================
//     * */
//    @Test(dataProvider = "THING_TYPE", dataProviderClass = StoreScenarioUtil.class)
//    public void thingAddT03(String thing_type) {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        try {
//            //新增一个正常进行的添加事项
//            String activity_description = "店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆";
//            String activity_type = thing_type;
//            String start_date=dt.getHistoryDate(0); //今天日期;
//            String end_date=dt.getHistoryDate(startM); //今天日期+2;;
//
//            int code = Md.StoreActivityAdd(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");
//
//
//            Preconditions.checkArgument(code == 1000,"添加事项不成功");
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("添加事件(单选一个事项类型)");
//        }
//
//    }

//    /**
//     *
//     * ====================添加事件(事件类型不选)======================
//     * */
//    @Test(dataProvider = "THING_TYPE_FALSE", dataProviderClass = StoreScenarioUtil.class)
//    public void thingAddF(String thing_type_false) {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        try {
//            //新增一个正常进行的添加事项
//            String activity_description = "店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆";//24字的说明
//            String activity_type = thing_type_false;
//            String start_date=dt.getHistoryDate(0); //今天日期;
//            String end_date=dt.getHistoryDate(startM); //今天日期+2;;
//
//            int code = Md.StoreActivityAdd(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");
//
//
//            Preconditions.checkArgument(code == 1001,"事件类型不选择却添加事项成功");
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("添加事件(事件类型不选)");
//        }
//
//    }
//
//    /**
//     *
//     * ====================添加事件(时间格式不正确)======================
//     * */
//    @Test(dataProvider = "TIME_TYPE_FALSE", dataProviderClass = StoreScenarioUtil.class)
//    public void thingAddF02(String time_type_false) {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        try {
//            //新增一个正常进行的添加事项
//            String activity_description = "店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆店庆";
//            String activity_type = "NEW_COMMODITY";
//            String start_date=time_type_false;
//            String end_date="2020-07-06"; ;;
//
//            int code = Md.StoreActivityAdd(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");
//
//
//            Preconditions.checkArgument(code == 1001,"时间格式不正确却添加事项成功");
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("添加事件(时间类型不选)");
//        }
//
//    }
//    /**
//     *
//     * ====================添加事件(事件说明超过20个字&不填)======================
//     * */
//    @Test(dataProvider = "DESCRIPTION_FALSE", dataProviderClass = StoreScenarioUtil.class)
//    public void thingAddF03(String description_false) {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        try {
//            //新增一个正常进行的添加事项
//            String activity_description =description_false;
//            String activity_type = "NEW_COMMODITY";
//            String start_date=dt.getHistoryDate(0); //今天日期;
//            String end_date=dt.getHistoryDate(startM); //今天日期+2;;
//
//            int code = Md.StoreActivityAdd(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");
//
//
//            Preconditions.checkArgument(code == 1001,"事件说明超过20个字或不填却添加事项成功");
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("添加事件(事件说明超过20个字&不填)");
//        }
//
//    }
//
//
//    /**
//     *
//     * ====================添加事件（同一事件均已添加过同一天再次添加）======================
//     * */
//    @Test(dataProvider = "THING_TYPE", dataProviderClass = StoreScenarioUtil.class)
//    public void thingAddT04(String thing_type) {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        try {
//            //新增一个正常进行的添加事项
//            String activity_description = "店庆店庆店庆店庆店庆店庆店庆店庆";
//            String activity_type = thing_type;
//            String start_date=dt.getHistoryDate(0); //今天日期;
//            String end_date=dt.getHistoryDate(startM); //今天日期+2;;
//
//            int code = Md.StoreActivityAdd(activity_description, activity_type, start_date, end_date, shop_id).getInteger("code");
//
//
//            Preconditions.checkArgument(code == 1001,"同一类型的事项已添加过同一天再次添加事项成功了");
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("添加事件(事件类型不选)");
//        }
//
//    }
//
    /**
     * @author : guoliya
     * @date :  2020/08/04
     */

    /**
     * ====================列表页排序按照上个整点计算的今日到访人次排序(人次从大到小排序)======================
     */
    @Test
    public void storeListRank() {
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
            //System.out.println("数据为  "+storeList);
            if (storeList.size()>1) {
            for (int i = 0; i < storeList.size() - 1; i++) {
                Integer realtimePv = storeList.getJSONObject(i).getInteger("realtime_pv");
                Integer realtimePv1 = storeList.getJSONObject(i + 1).getInteger("realtime_pv");
                if(realtimePv!=null&&realtimePv1!=null){
                    //System.out.println("i=="+i+"   "+realtimePv+"    "+realtimePv1);
                    Preconditions.checkArgument(realtimePv >= realtimePv1, "人次多的数据为" + realtimePv + "人次少的数据为" + realtimePv1);
                }
            }
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
        }finally {
            saveData("门店筛选栏--模糊搜索");
        }


    }
}
