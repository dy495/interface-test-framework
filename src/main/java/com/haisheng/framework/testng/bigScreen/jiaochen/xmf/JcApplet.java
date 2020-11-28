package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.inject.internal.util.$Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

public class JcApplet extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = new ScenarioUtil();

    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    JcFunction pf = new JcFunction();
    FileUtil file = new FileUtil();
    Random random = new Random();
    public int page = 1;
    public int size = 50;
    public String name = "";
    public String email = "";
    public String phone = "";


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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "xmf";


        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "汽车-轿辰 日常X");

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        jc.appletLoginToken(pp.appletTocken);


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
     * @description :添加车辆，车牌8位，数量+1
     * @date :2020/7/10 18:03
     **/
    @Test()
    public void mycarConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String plate_number = "蒙JKIO123";

            int count = pf.carListNumber(pp.carStyleId);
            String car_idBefore = pf.appletAddCar(plate_number);

            JSONArray listB = jc.appletMyCar(pp.carStyleId).getJSONArray("list");
            int aftercount = listB.size();

            jc.appletCarDelst(car_idBefore);
            int countA = pf.carListNumber(pp.carStyleId);

            checkArgument((aftercount - count) == 1, "增加车辆，我的车辆列表没加1");
            checkArgument((aftercount - countA) == 1, "删除车辆，我的车辆列表没-1");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加车辆，applet我的车辆列表加1");
        }
    }

    /**
     * @description :添加车辆，车牌7位
     * @date :2020/7/10 18:03
     **/
    @Test()
    public void mycarConsistencySeven() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String plate_number = "蒙JKIO12";

            int count = pf.carListNumber(pp.carStyleId);
            String car_idBefore = pf.appletAddCar(plate_number);

            JSONArray listB = jc.appletMyCar(pp.carStyleId).getJSONArray("list");
            int aftercount = listB.size();

            jc.appletCarDelst(car_idBefore);
            int countA = pf.carListNumber(pp.carStyleId);

            checkArgument((aftercount - count) == 1, "增加车辆，我的车辆列表没加1");
            checkArgument((aftercount - countA) == 1, "删除车辆，我的车辆列表没-1");


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加车辆7位车牌，applet我的车辆列表加1");
        }
    }

    /**
     * @description :添加重复车牌失败
     * @date :2020/7/10 18:03
     **/
    @Test(priority = 2)
    public void sameCarFail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int num=jc.appletMyCar(pp.carStyleId).getJSONArray("list").size();
            String plate_number = pp.carplate;
            jc.appletAddCar(pp.carModelId ,plate_number).getLong("code");
            int numA=jc.appletMyCar(pp.carStyleId).getJSONArray("list").size();
            Preconditions.checkArgument(numA-num==0,"添加重复车牌，不重复显示");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("添加重复车牌验证");
        }
    }


    /**
     * @description :【我的】添加车辆，10辆边界
     * @date :2020/7/27 19:43
     **/
    @Test(priority = 2)
    public void myCarTen() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appletLoginToken(pp.appletTocken);
            int count=pf.carListNumber(pp.carStyleId);
            int limit = 5 - count;
            JSONArray carId=new JSONArray();
            for (int i = 0; i < limit; i++) {
                String plate_number;
                plate_number = "豫GBBA3" + i;
                String car_id=pf.appletAddCar( plate_number);
                carId.add(car_id);
            }
            String plate_number = "豫GBBA11";
            Long code = jc.appletAddCarcode( plate_number,pp.carModelId).getLong("code");
            checkArgument(code == 1001, "我的车辆上限5辆车");

            for (int j = 0; j < carId.size(); j++) {
                String car_id = carId.getString(j);
                jc.appletCarDelst(car_id);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序我的车辆，增加6辆");
        }
    }

    /**
     * @description :车牌号数量
     * @date :2020/8/24 19:54
     **/
    @Test
    public void provinceList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.appletplateNumberProvinceList();
            JSONArray list = data.getJSONArray("list");
//            String p=list.getJSONObject(0).getString("province_name");
            checkArgument(list.size() == 31, "车牌号省份不是31");
//            Preconditions.checkArgument(p.equals("苏"),"省份默认不是苏");
        } catch (AssertionError | Exception e) {

            appendFailReason(e.toString());
        } finally {
            saveData("车牌号验证");
        }
    }

    /**
     * @description :编辑车辆，异常车牌验证
     * @date :2020/10/10 16:00
     **/
    @Test(dataProvider = "PLATE", dataProviderClass = ScenarioUtil.class)
    public void editplateab(String plate) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long code = jc.appletCarEdit(pp.car_id, plate, pp.carModelId).getLong("code");
            $Preconditions.checkArgument(code == 1001, "编辑输入错误车牌，仍成功");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("车辆，车牌号异常验证");
        }
    }
    /**
     * @description :编辑车辆
     * @date :2020/10/10 16:00
     **/
    @Test(priority = 1)
    public void editplate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long code = jc.appletCarEdit(pp.car_id, pp.carplate, pp.carModelId).getLong("code");
            Preconditions.checkArgument(code == 1000, "编辑车辆接口报错");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("编辑车辆");
        }
    }


    /**
     * @description :新增车辆，异常车牌验证
     * @date :2020/10/10 16:00
     **/
    @Test(dataProvider = "PLATE", dataProviderClass = ScenarioUtil.class)
    public void plateabnormal(String plate) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data=jc.appletAddCarcode( plate, pp.carModelId);
            Long code = data.getLong("code");
            $Preconditions.checkArgument(code == 1001, "编辑输入错误车牌，仍成功");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新建车辆，车牌号异常验证");
        }
    }


}
