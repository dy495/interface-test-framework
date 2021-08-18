package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.inject.internal.util.$Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.Variable.registerListVariable;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.VoucherFormVoucherPageBean;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.SendRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.AppletActivityRegister;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.AppletInfoEdit;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import static com.google.common.base.Preconditions.checkArgument;

public class JcApplet extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.JC_DAILY_JD;
    VisitorProxy visitor = new VisitorProxy(product);
    private static final EnumAccount administrator = EnumAccount.JC_ALL_DAILY;
    ScenarioUtil jc = new ScenarioUtil();
    SceneUtil util = new SceneUtil(visitor);
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    JcFunction pf = new JcFunction(visitor, pp);
    CommonConfig commonConfig = new CommonConfig();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "夏明凤";
        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;

        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.setShopId(pp.shopIdZ).setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        jc.appletLoginToken(pp.appletTocken);
        logger.debug("jc: " + jc);
    }

    //pc登录
    public void pcLogin(String phone, String verificationCode, String roleId) {
        String path = "/account-platform/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        object.put("type", 1);
        commonConfig.setRoleId(roleId);
        httpPost(EnumTestProduct.JC_DAILY_ZH.getIp(), path, object);
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

    @Test(description = "卡券 套餐数量")
    public void number() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appletLoginToken(pp.appletTocken);
            System.out.println("卡券数量" + pf.getVoucherTotal());
//            System.out.println("套餐数量"+pf.getpackgeTotal());


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("number");
        }
    }

    /**
     * @description :添加车辆，车牌8位，数量+1 ok
     * @date :2020/7/10 18:03
     **/
//    @Test()
    public void mycarConsistency() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int count = pf.carListNumber(pp.carStyleId);
            String plate_number = "蒙JKIO123";
            String car_idBefore = pf.appletAddCar(plate_number);

            JSONArray listB = jc.appletMyCar(pp.carStyleId).getJSONArray("list");
            int aftercount = listB.size();

            jc.appletCarDelete(car_idBefore);
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
     * @description :添加车辆，车牌7位 ok
     * @date :2020/7/10 18:03
     **/
//    @Test()
    public void mycarConsistencySeven() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String plate_number = "蒙JKIO12";

            int count = pf.carListNumber(pp.carStyleId);
            String car_idBefore = pf.appletAddCar(plate_number);

            JSONArray listB = jc.appletMyCar(pp.carStyleId).getJSONArray("list");
            int aftercount = listB.size();

            jc.appletCarDelete(car_idBefore);
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
     * @description :添加重复车牌失败ok
     * @date :2020/7/10 18:03
     **/
//    @Test()
    public void sameCarFail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
//            int num=jc.appletMyCar(pp.carStyleId).getJSONArray("list").size();
            String plate_number = pp.carplate;
            Long code = jc.appletAddCarcode(plate_number, pp.carModelId).getLong("code");
            Preconditions.checkArgument(code == 1001, "重复车牌仍成功");
//            int numA=jc.appletMyCar(pp.carStyleId).getJSONArray("list").size();
//            Preconditions.checkArgument(numA-num==0,"添加重复车牌，不重复显示");

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
//    @Test()
    public void myCarTen() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appletLoginToken(pp.appletTocken);
            int count = pf.carListNumber(pp.carStyleId);
            int limit = 5 - count;
            JSONArray carId = new JSONArray();
            for (int i = 1; i < limit; i++) {
                String plate_number;
                plate_number = "吉GBBA3" + i;
                String car_id = pf.appletAddCar(plate_number);
                carId.add(car_id);
            }
            String plate_number = "吉GBBA10";
            Long code = jc.appletAddCarcode(plate_number, pp.carModelId).getLong("code");
            checkArgument(code == 1001, "我的车辆上限5辆车");

            for (int j = 0; j < carId.size(); j++) {
                String car_id = carId.getString(j);
                jc.appletCarDelete(car_id);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
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
            System.out.println(authorization);
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
    @Test()
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
            JSONObject data = jc.appletAddCarcode(plate, pp.carModelId);
            Long code = data.getLong("code");
            $Preconditions.checkArgument(code == 1001, "编辑输入错误车牌，仍成功");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
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
        logger.logCaseStart(caseResult.getCaseName());
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
            appendFailReason(e.toString());
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
        logger.logCaseStart(caseResult.getCaseName());
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
            appendFailReason(e.toString());
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
        logger.logCaseStart(caseResult.getCaseName());
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
            registerListVariable sv = new registerListVariable();
            JSONObject ll = jc.registerListFilterManage(sv);
            String appointment_id = ll.getJSONArray("list").getJSONObject(0).getString("id");
            JSONArray passItem = new JSONArray();
            passItem.add(appointment_id);    //审批取得id与预约id是否一致？？ TODO:
            //审批
            jc.approvalArticle(passItem, "APPROVAL_CONFIRM(");
            //活动报名审批后
            jc.appletLoginToken(pp.appletTocken);
            int numA[] = pf.appletActivityDetail(activity_id.toString());

            checkArgument(numA[0] - num[0] == 1, "小程序活动报名，pc报名客户未+1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            jc.appletLoginToken(pp.appletTocken);
            saveData("活动报名，applet已报名人数++，剩余人数--，pc 总数--，已报名人数++");
        }
    }

    /**
     * @description :修改个人信息
     * @date :2020/12/15 17:58
     **/
    @Test
    public void appletCustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //修改用户信息
            AppletInfoEdit er = new AppletInfoEdit();
            er.birthday = "1996-02-19";
            er.gender = "FEMALE";
            er.name = "自动";
            er.contact = "15037286013";
//            er.shipping_address = "中关村soho" + dt.getHHmm(0);
            jc.appletUserInfoEdit(er);
            JSONObject data = jc.appletUserInfoDetail();      //查看用户详情
            Preconditions.checkArgument(data.getString("birthday").equals(er.birthday));
            Preconditions.checkArgument(data.getString("gender").equals(er.gender));
            Preconditions.checkArgument(data.getString("name").equals(er.name));
//            Preconditions.checkArgument(data.getString("shipping_address").equals(er.shipping_address));

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("修改个人信息正常常");
        }
    }

    //    @Test(description = "异常",dataProvider = "ERR_PHONE",dataProviderClass = DataAbnormal.class)
    public void appletCustomer2(String contact) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //修改用户信息
            AppletInfoEdit er = new AppletInfoEdit();
            er.birthday = "1996-02-19";
            er.gender = "FEMALE";
            er.name = "@@@";
            er.contact = contact;
            er.checkcode = false;
            er.shipping_address = "中关村soho" + dt.getHHmm(0);
            int code = jc.appletUserInfoEdit(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "applet-修改个人信息手机号异常");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("修改个人信息正常常");
        }
    }

    //    @Test(description = "异常")
    public void appletCustomer3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //修改用户信息
            AppletInfoEdit er = new AppletInfoEdit();
            er.birthday = "1996-02-19";
            er.gender = "FEMALE";
            er.name = "@@@";
            er.contact = "15037286013";
            er.checkcode = false;
            er.shipping_address = "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十中关";
            int code = jc.appletUserInfoEdit(er).getInteger("code");
            er.shipping_address = "中关村soho";
            er.name = "一二三四五六七八九十一二";
            int code2 = jc.appletUserInfoEdit(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "applet-修改个人信息地址长度异常验证");
            Preconditions.checkArgument(code2 == 1001, "applet-修改个人信息名称长度异常验证");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("修改个人信息异常验证");
        }
    }

    //报名领卡券 卡券选择通用不限量的固定id ok
    //卡券累计发出+1，发卡记录+1
//    @Test()
    public void activity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer voucherTotal = pf.getVoucherTotal();  //卡券数量
//           Long total=jc.appletVoucherList(null,"GENERAL",20).getLong("total");
            jc.pcLogin(pp.gwphone, pp.jdgwpassword);
            //登录门店查询数据
            commonConfig.setShopId("-1");
            String voucherName = util.getVoucherName(pp.voucherId);
            VoucherFormVoucherPageBean voucher = util.getVoucherPage(voucherName);
            //累计发出数
            int cumulativeDelivery = voucher.getCumulativeDelivery();
            //发卡记录数
            int sendRecordTotal = jc.invokeApi(SendRecordScene.builder().build()).getInteger("total");
            //登回
            commonConfig.setShopId("45973");
            JSONArray voucherList = new JSONArray();
//            Long voucherId=jc.pcVoucherList().getJSONArray("list").getJSONObject(0).getLong("id");
            voucherList.add(pp.voucherId);   //
            Long id = pf.creteArticle(voucherList, "ARTICLE_BUTTON");
            jc.appletLoginToken(pp.appletTocken);
            jc.appletvoucherReceive(id.toString(), pp.voucherId.toString());   //领卡券
            Integer voucherTotalB = pf.getVoucherTotal();      //查卡券数
            Preconditions.checkArgument(voucherTotalB - voucherTotal == 1, "活动领取卡券后，卡券数量未加1");
            util.loginPc(administrator);
            //查询新数据
            commonConfig.setShopId("-1");
            //累计发出数
            VoucherFormVoucherPageBean newVoucher = util.getVoucherPage(voucherName);
            int newCumulativeDelivery = newVoucher.getCumulativeDelivery();
            //发卡记录数
            int newSendRecordTotal = jc.invokeApi(SendRecordScene.builder().build()).getInteger("total");
            CommonUtil.valueView(cumulativeDelivery, newCumulativeDelivery, sendRecordTotal, newSendRecordTotal);
            Preconditions.checkArgument(newCumulativeDelivery == cumulativeDelivery + 1, "领券之前累计发出数：" + cumulativeDelivery + "领券之后累计发出数：" + newCumulativeDelivery);
            Preconditions.checkArgument(newSendRecordTotal == sendRecordTotal + 1, "领券之前发卡记录数：" + sendRecordTotal + "领券之后发卡记录数：" + newSendRecordTotal);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            //还原
            commonConfig.setShopId(pp.shopIdZ);
            jc.appletLoginToken(pp.appletTocken);
            saveData("创建活动，领卡券");
        }
    }

    //报名领卡券报名通过即发券 卡券选择通用不限量的固定id  ok
//    @Test()
    public void activity2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Integer voucherTotal = pf.getVoucherTotal();  //卡券数量
//           Long total=jc.appletVoucherList(null,"GENERAL",20).getLong("total");
            jc.pcLogin(pp.gwphone, pp.jdgwpassword);
            JSONArray voucherList = new JSONArray();
//            Long voucherId=jc.pcVoucherList().getJSONArray("list").getJSONObject(0).getLong("id");
            voucherList.add(pp.voucherId);   //
            Long id = pf.creteArticle(voucherList, "SIGN_UP");       //创建活动

            jc.appletLoginToken(pp.appletTocken);
            //报名
//            Long id=3921L;

            AppletActivityRegister ar = new AppletActivityRegister();
            ar.id = id.toString();
            ar.name = "@@@";
            ar.num = 1;
            ar.phone = "15037286013";
            jc.appletactivityRegister(ar);
            //pc--审批通过
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            String passId = jc.approvalListFilterManage(null, "1", "10", id.intValue(), null, null).getJSONArray("list").getJSONObject(0).getString("id");
            JSONArray json = new JSONArray();
            json.add(passId);
            jc.approvalArticle(json, "APPROVAL_CONFIRM");    //审批通过

            jc.appletLoginToken(pp.appletTocken);
            Integer voucherTotalB = pf.getVoucherTotal();      //查卡券数
            Preconditions.checkArgument(voucherTotalB - voucherTotal == 1, "活动领取卡券后，卡券数量未加1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("创建活动，领卡券");
        }
    }

    /**
     * @description :首页文章20个
     * @date :2020/12/16 19:47
     **/
    @Test(dataProvider = "TYPE")
    public void appletArticleList(String lable, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.appletLoginToken(pp.appletTocken);
            JSONArray list = jc.appletArticleList("20", null, lable).getJSONArray("list");
            Preconditions.checkArgument(list.size() <= 10, "首页" + mess + "文章超过了10");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("小程序活动首页最多20个");
        }
    }

    @DataProvider(name = "TYPE")
    public Object[] type() {
        return new Object[][]{
                {"CAR_WELFARE", "车福利"},
                {"CAR_INFORMATION", "车资讯"},
                {"CAR_LIFE", "车生活"},
                {"CAR_ACVITITY", "车活动"},
                {"CAR_KNOWLEDGE", "车知识"}

        };
    }

    @Test(description = "自主核销异常验证")
    public void appletVerification() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String verification_code[] = {"OGW", "OGW123", "OGWTJKW", "OGWT真"};
            String voucher_code[] = pf.voucherName();
            for (int i = 0; i < verification_code.length; i++) {
                int code = jc.appleterification(voucher_code[2], verification_code[i], false).getInteger("code");
                Preconditions.checkArgument(code == 1001, "异常核销码");
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("自主核销异常验证");
        }
    }

    @Test(description = "自主核销")
    public void appletVerification2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String voucher_code[] = pf.voucherName();
            jc.appleterification(voucher_code[2], pp.verification_code, true).getInteger("code");

            //小程序消息最新一条信息校验
            jc.appletLoginToken(pp.appletTocken);
            JSONObject message = jc.appletMessageList(null, 20).getJSONArray("list").getJSONObject(0);
            Long id = message.getLong("id");

            String messageName = jc.appletMessageDetail(id.toString()).getString("content");
            Preconditions.checkArgument(messageName.equals("您的卡券【" + voucher_code[1] + "】已被核销，请立即查看"), "");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("自主核销");
        }
    }

    @Test(description = "申请道路救援，pc救援列表+1")  //ok
    public void recuse() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            int total = jc.pcrescuePage(1, 10).getInteger("total");

            jc.appletLoginToken(pp.appletTocken);
            jc.rescueApply(pp.shopIdZ, pp.coordinate);

            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            int totalAfter = jc.pcrescuePage(1, 10).getInteger("total");
            Preconditions.checkArgument(totalAfter - total == 1, "申请道路救援，pc救援列表+1");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("申请道路救援，pc救援列表+1");
        }
    }

    @Test(description = "文章详情")  //ok
    public void actiaclDetail() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject last_value = null;
            int size = 10;
            while (size == 10) {
                JSONObject data = jc.AppletPageScene(10, last_value);
                last_value = data.getJSONObject("last_value");
                JSONArray list = data.getJSONArray("list");
                size = list.size();
                for (int i = 0; i < list.size(); i++) {
                    String id = list.getJSONObject(i).getString("id");
                    jc.appletArticleDetail(id);
                }
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("文章详情");
        }
    }

    //获取某种状态的门店数量
    public Integer getshopNumber(String status) {
        JSONObject data = jc.shopPage(1, 10, null);
        int count = 0;
        int pages = data.getInteger("pages");
        for (int i = 0; i < pages; i++) {
            JSONArray list = jc.shopPage(i + 1, 10, null).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                String status1 = list.getJSONObject(j).getString(status);
                if (status1.equals("ENABLE")) {
                    count++;
                }
            }

        }
        return count;
    }

    // @Test(description = "道路救援门店数=门店管理开启的门店")   这个case 待确定，救援门店 可能时预约开启门店
    public void recuseShopList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            commonConfig.setShopId(pp.shopId);
            int openShoptotal = getshopNumber("status");
            jc.appletLoginToken(pp.appletTocken);
            int total = jc.rescueShopList(pp.coordinate, "null").getJSONArray("list").size();  //TODO：接口没有返回门店列表总数，暂取list的size
            Preconditions.checkArgument(openShoptotal == total, "道路救援门店数" + total + "!=门店管理开启的门店" + openShoptotal);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            commonConfig.setShopId(pp.shopIdZ);
            saveData("道路救援");
        }
    }


    //    @Test(description = "免费洗车门店数=门店管理开启的门店")
    public void washShopList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcLogin(pp.gwphone, pp.gwpassword, pp.roleId);
            commonConfig.setShopId(pp.shopId);
            int openShoptotal = getshopNumber("washing_status");
            jc.appletLoginToken(pp.appletTocken);
            int total = jc.carWashShopList(pp.coordinate).getJSONArray("list").size();    //TODO：接口没有返回门店列表总数，暂取list的size
            Preconditions.checkArgument(openShoptotal == total, "免费洗车门店数!=门店管理洗车开启的门店");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            commonConfig.setShopId(pp.shopIdZ);
            saveData("免费洗车门店数=门店管理开启的门店");
        }
    }

    //    @Test(description = "洗车，剩余次数-1，pc洗车管理+1")   //改功能暂被取消，无需监控
    public void washCar() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcLogin(pp.jdgw, pp.gwpassword, pp.roleidJdgw);
            int total = jc.washCarManagerPage(pp.shopIdZ, "1", "10").getInteger("total");  //洗车管理数

            jc.appletLoginToken(pp.appletTocken);
            int washCarTimes = jc.washTimes().getInteger("remainNumber");  //洗车次数

            jc.carWsah(pp.shopIdZ);
            int washCarTimesAfter = jc.washTimes().getInteger("remainNumber");

            pcLogin(pp.jdgw, pp.gwpassword, pp.roleidJdgw);

            int totalAfter = jc.washCarManagerPage(pp.shopIdZ, "1", "10").getInteger("total");

            Preconditions.checkArgument(totalAfter - total == 1, "洗车，洗车管理+1");
            Preconditions.checkArgument(washCarTimes - washCarTimesAfter == 1, "洗车，洗车次数-1");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.appletLoginToken(pp.appletTocken);
            saveData("洗车，洗车管理+1");
        }
    }


}
