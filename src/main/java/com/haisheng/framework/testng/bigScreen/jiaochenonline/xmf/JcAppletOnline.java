package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.inject.internal.util.$Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletCarListScene;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.AppletActivityRegister;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

public class JcAppletOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.JC_ONLINE_JD;
    private static final EnumAccount account = EnumAccount.JC_ALL_ONLINE_LXQ;
    private static final EnumAppletToken APPLET_USER = EnumAppletToken.JC_LXQ_ONLINE;
    private VisitorProxy visitor = new VisitorProxy(product);
    ScenarioUtil jc = new ScenarioUtil();

    PublicParmOnline pp = new PublicParmOnline();
    JcFunctionOnline pf = new JcFunctionOnline();
    CommonConfig commonConfig = new CommonConfig();


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        jc.changeIpPort(product.getIp());
        logger.debug("before class initial");
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId("45973").setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
        jc.appletLoginToken(APPLET_USER.getToken());
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        logger.logCaseStart(caseResult.getCaseName());
    }

    /**
     * @description :添加车辆，车牌8位，数量+1 ok
     * @date :2020/7/10 18:03
     **/
    @Test()
    public void myCarConsistency() {
        try {
            jc.appletLoginToken(APPLET_USER.getToken());
            int count = pf.carListNumber(pp.carStyleId);
            String plate_number = "蒙JKIO123";
            String car_idBefore = pf.appletAddCar(plate_number);
            JSONArray listB = jc.appletMyCar(pp.carStyleId).getJSONArray("list");
            int afterCount = listB.size();
            jc.appletCarDelete(car_idBefore);
            int countA = pf.carListNumber(pp.carStyleId);
            checkArgument((afterCount - count) == 1, "增加车辆，我的车辆列表没加1");
            checkArgument((afterCount - countA) == 1, "删除车辆，我的车辆列表没-1");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("添加车辆，applet我的车辆列表加1");
        }
    }

    /**
     * @description :添加车辆，车牌7位 ok
     * @date :2020/7/10 18:03
     **/
    @Test()
    public void myCarConsistencySeven() {
        try {
            String plate_number = "蒙JKIO12";
            int count = pf.carListNumber(pp.carStyleId);
            String car_idBefore = pf.appletAddCar(plate_number);
            JSONArray list = jc.appletMyCar(pp.carStyleId).getJSONArray("list");
            int afterCount = list.size();
            jc.appletCarDelete(car_idBefore);
            int countA = pf.carListNumber(pp.carStyleId);
            checkArgument((afterCount - count) == 1, "增加车辆，我的车辆列表没加1");
            checkArgument((afterCount - countA) == 1, "删除车辆，我的车辆列表没-1");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("添加车辆7位车牌，applet我的车辆列表加1");
        }
    }

    /**
     * @description :添加重复车牌失败ok
     * @date :2020/7/10 18:03
     **/
    @Test()
    public void sameCarFail() {
        try {
            jc.appletLoginToken(pp.appletTocken);
//            int num=jc.appletMyCar(pp.carStyleId).getJSONArray("list").size();
            String plate_number = pp.carplate;
            Long code = jc.appletAddCarcode(plate_number, pp.carModelId).getLong("code");
            Preconditions.checkArgument(code == 1001, "重复车牌仍成功");
//            int numA=jc.appletMyCar(pp.carStyleId).getJSONArray("list").size();
//            Preconditions.checkArgument(numA-num==0,"添加重复车牌，不重复显示");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("添加重复车牌验证");
        }
    }


    /**
     * @description :【我的】添加车辆，10辆边界
     * @date :2020/7/27 19:43
     **/
    @Test()
    public void myCarTen() {
        try {
            jc.appletLoginToken(pp.appletTocken);
            int count = pf.carListNumber(pp.carStyleId);
            int limit = 5 - count;
            JSONArray carId = new JSONArray();
            for (int i = 0; i < limit; i++) {
                String plate_number;
                plate_number = "豫GBBA3" + i;
                String car_id = pf.appletAddCar(plate_number);
                carId.add(car_id);
            }
            String plate_number = "豫GBBA11";
            Long code = jc.appletAddCarcode(plate_number, pp.carModelId).getLong("code");
            checkArgument(code == 1001, "我的车辆上限5辆车");

            for (int j = 0; j < carId.size(); j++) {
                String car_id = carId.getString(j);
                jc.appletCarDelete(car_id);
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("小程序我的车辆，增加6辆");
        }
    }

    /**
     * @description :车牌号数量 ok
     * @date :2020/8/24 19:54
     **/
    @Test
    public void provinceList() {
        try {
            JSONObject data = jc.appletplateNumberProvinceList();
            JSONArray list = data.getJSONArray("list");
//            String Util=list.getJSONObject(0).getString("province_name");
            checkArgument(list.size() == 31, "车牌号省份不是31");
//            Preconditions.checkArgument(Util.equals("苏"),"省份默认不是苏");
        } catch (AssertionError | Exception e) {

            collectMessage(e);
        } finally {
            saveData("车牌号验证");
        }
    }

    /**
     * @description :编辑车辆，异常车牌验证
     * @date :2020/10/10 16:00
     **/
    @Test(dataProvider = "PLATE", dataProviderClass = ScenarioUtilOnline.class)
    public void editplateab(String plate) {
        try {
            Long code = jc.appletCarEdit(pp.car_id, plate, pp.carModelId).getLong("code");
            $Preconditions.checkArgument(code == 1001, "编辑输入错误车牌，仍成功");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
//            saveData("车辆，车牌号异常验证");
        }
    }

    /**
     * @description :编辑车辆
     * @date :2020/10/10 16:00
     **/
    @Test()
    public void editplate() {
        try {
            Long code = jc.appletCarEdit(pp.car_id, pp.carplate, pp.carModelId).getLong("code");
            Preconditions.checkArgument(code == 1000, "编辑车辆接口报错");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("编辑车辆");
        }
    }


    /**
     * @description :新增车辆，异常车牌验证
     * @date :2020/10/10 16:00
     **/
    @Test(dataProvider = "PLATE", dataProviderClass = ScenarioUtilOnline.class)
    public void plateabnormal(String plate) {
        try {
            JSONObject data = jc.appletAddCarcode(plate, pp.carModelId);
            Long code = data.getLong("code");
            $Preconditions.checkArgument(code == 1001, "编辑输入错误车牌，仍成功");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新建车辆，车牌号异常验证");
        }
    }

    /**
     * @description :活动报名
     * @date :2020/11/30 15:52
     **/
    /**
     * @description :活动报名 pc报名人数变化
     * @date :2020/7/12 11:48
     **/
//    @Test()
    public void activityConsistency() {
        try {
            //活动报名前
            Long[] aid = {};
            Long activity_id = aid[1];
            jc.pcLogin(pp.shichang, pp.shichangPassword);
            int num[] = pf.jsonActivityNUm(activity_id.toString());
            AppletActivityRegister ar = new AppletActivityRegister();

            jc.appletLoginToken(pp.appletTocken);
            JSONObject data = jc.appletactivityRegister(ar);
            String appointment_id = data.getString("appointment_id");

            //活动报名后
            jc.pcLogin(pp.shichang, pp.shichangPassword);
            int numA[] = pf.jsonActivityNUm(activity_id.toString());

            jc.appletLoginToken(pp.appletTocken);
            jc.appletactivityCancel(appointment_id);  //取消活动报名
            //TODO:
            checkArgument(numA[1] - num[1] == 0, "小程序活动报名，pc报名总数变了");
            checkArgument(numA[2] - num[2] == 1, "小程序活动报名，pc已报名客户未+1");
            checkArgument(numA[3] - num[3] == 0, "小程序活动报名，未审批 pc已入选变了");


        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.appletLoginToken(pp.appletTocken);
            saveData("applet活动报名,pc报名客户+1");

        }
    }

    /**
     * @description :活动报名；未审核 applet文章详情，报名人数+1
     * @date :2020/7/21 15:29
     **/
//    @Test()
    public void pcappointmentSum() {
        try {
            //活动报名前
            Long[] aid = {};
            Long activity_id = aid[1];

            int num[] = pf.appletActivityDetail(activity_id.toString());
            AppletActivityRegister ar = new AppletActivityRegister();
            //活动报名
            JSONObject data = jc.appletactivityRegister(ar);
            String appointment_id = data.getString("appointment_id");

            //活动报名后
            int numA[] = pf.appletActivityDetail(activity_id.toString());
            jc.appletactivityCancel(appointment_id);      //取消活动报名
            checkArgument(numA[0] - num[0] == 1, "小程序活动报名，小程序文章全部名额未+0");
            checkArgument(numA[1] - num[1] == 1, "小程序活动报名，小程序文章已报名名额未+1");
            checkArgument(numA[2] - num[2] == 1, "小程序活动报名，小程序文章报名名单未+1");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.appletLoginToken(pp.appletTocken);
            saveData("活动报名，applet已报名人数++，剩余人数--，pc 总数--，已报名人数++");
        }
    }

    /**
     * @description :活动审批通过
     * @date :2020/11/30 17:23
     **/

//    @Test()
    public void pcappointmentSumPass() {
        try {
//            String date=dt.getHistoryDate(0);
            String date = "2020-12-04";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//24小时制
            long time = simpleDateFormat.parse(date).getTime();
            System.out.println(time);
//            System.out.println(dt.getHistoryDateTimestamp(0));
            //活动报名前
            Long[] aid = {};
            Long activity_id = aid[1];

            int num[] = pf.appletActivityDetail(activity_id.toString());
            AppletActivityRegister ar = new AppletActivityRegister();
            //活动报名
            jc.appletactivityRegister(ar);

            jc.pcLogin(pp.shichang, pp.shichangPassword);
//            registerListVariable sv = new registerListVariable();
//            JSONObject ll = jc.registerListFilterManage(sv);
//            String appointment_id = ll.getJSONArray("list").getJSONObject(0).getString("id");
            JSONArray passItem = new JSONArray();
//            passItem.add(appointment_id);    //审批取得id与预约id是否一致？？ TODO:
            //审批
            jc.approvalArticle(passItem, "APPROVAL_CONFIRM(");
            //活动报名审批后
            jc.appletLoginToken(pp.appletTocken);
            int numA[] = pf.appletActivityDetail(activity_id.toString());

            checkArgument(numA[0] - num[0] == 1, "小程序活动报名，pc报名客户未+1");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.appletLoginToken(pp.appletTocken);
            saveData("活动报名，applet已报名人数++，剩余人数--，pc 总数--，已报名人数++");
        }
    }

    @Test(description = "卡券 套餐数量")
    public void number() {
        try {
            jc.appletLoginToken(pp.appletTocken);
            System.out.println(product.getIp());
            System.out.println("卡券数量" + pf.getVoucherTotal());
//            System.out.println("套餐数量"+pf.getpackgeTotal());


        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
//            saveData("number");
        }
    }


}
