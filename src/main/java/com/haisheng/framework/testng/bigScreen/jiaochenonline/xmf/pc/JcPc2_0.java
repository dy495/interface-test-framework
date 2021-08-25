package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.PoiUtils;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.store.CommodityPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage.LossCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage.PreSaleCustomerPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.store.CommodityDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.store.CommodityEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.store.CommodityPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.DataAbnormal;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.PcCreateStoreCommodity;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.PcCreateRemind;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.PcCreateStoreSales;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.JcFunctionOnline;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf.PublicParamOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Random;


public class JcPc2_0 extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.JC_ONLINE_JD;
    private final VisitorProxy visitor = new VisitorProxy(product);
    private final SceneUtil util = new SceneUtil(visitor);
    ScenarioUtil jc = ScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParamOnline pp = new PublicParamOnline();
    JcFunctionOnline pf = new JcFunctionOnline();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.XMF.getName();
        jc.changeIpPort(product.getIp());
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(pp.shopIdZ).setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
        qaDbUtil.openConnection();
    }

    //pc登录
    public void pcLogin(String phone, String verificationCode) {
        String path = "/account-platform/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        object.put("type", 1);
        httpPost(EnumTestProduct.JC_ONLINE_ZH.getIp(), path, object);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
        qaDbUtil.closeConnection();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        logger.logCaseStart(caseResult.getCaseName());
        pcLogin(pp.jdgw, pp.jdgwpassword);
    }

    /**
     * @description :新建分销员
     * @date :2021/1/13 11:23
     **/
    @Test(description = "新建分销员（必填项+选填项）")
    public void CreateSale() {
        try {
            PcCreateStoreSales er = new PcCreateStoreSales();
            er.sales_phone = pf.genPhoneNum();
            er.sales_name = String.valueOf(System.currentTimeMillis());
            er.shop_id = pp.shopIdZ;
            er.dept_name = "分销1部";
            er.job_name = "分销员";
            jc.SalesCreate(er);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建分销员单接口");
        }
    }

    @Test(description = "新建分销员（仅必填项）")
    public void CreateSale2() {
        try {
            PcCreateStoreSales er = new PcCreateStoreSales();
            er.sales_phone = pf.genPhoneNum();
            er.shop_id = pp.shopIdZ;
            jc.SalesCreate(er);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建分销员单接口");
        }
    }

    @Test(description = "新建分销员（必填项不填）")
    public void CreateSaleAb0() {
        try {
            PcCreateStoreSales er = new PcCreateStoreSales();
            er.sales_phone = pf.genPhoneNum();
            er.checkcode = false;
            int code = jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "新建分销员必填项不填异常");
            er.sales_phone = "";
            er.shop_id = pp.shopIdZ;
            int code2 = jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "新建分销员必填项不填异常");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建分销员单接口，必填项不填");
        }
    }

    @Test(description = "新建分销员参数长度异常")
    public void CreateSaleAb1() {
        try {
            PcCreateStoreSales er = new PcCreateStoreSales();
            er.sales_phone = pf.genPhoneNum();
            er.sales_name = String.valueOf(System.currentTimeMillis());
            er.shop_id = pp.shopIdZ;
            er.checkcode = false;
            //名字51
            er.dept_name = "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十分销1部";
            er.job_name = "分销员";
            int code = jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "部门名21异常");
            //岗位21
            er.dept_name = "分销1部";
            er.job_name = "一二三四五六七八九十一二三四五六七分销员1";
            int code2 = jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "岗位名21异常");
            er.sales_name = "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十分销1部";
            er.job_name = "分销员";
            int code3 = jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code3 == 1001, "分销员名51异常");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建分销员单接口异常参数验证");
        }
    }

    @Test(dataProvider = "ERR_PHONE", dataProviderClass = DataAbnormal.class)  //新建分销员
    public void CreateSaleAb2(String phone) {
        try {
            PcCreateStoreSales er = new PcCreateStoreSales();
            er.sales_phone = pf.genPhoneNum();
            er.sales_name = String.valueOf(System.currentTimeMillis());
            er.shop_id = pp.shopIdZ;
            er.sales_phone = phone;
            er.dept_name = "分销1部";
            er.job_name = "分销员";
            er.checkcode = false;
            int code = jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "新建分销员手机号异常");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建分销员手机号异常");
        }
    }

    @Test   //分销员列表
    public void SaleList() {
        try {
            JSONObject data = jc.SalesList("1", "10", null, null);
            JsonPathUtil.spiltString(data.toJSONString(), "$.list[*].sales_phone&&$.list[*].shop_name");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-分销员列表单接口");
        }
    }

    @Test  //商城套餐列表
    public void CommodityList() {
        try {
            JSONObject data = jc.StoreCommodityList("1", "10", null);
            JsonPathUtil.spiltString(data.toJSONString(), "$.list[*].id&&$.list[*].commodity_name&&$.list[*].commodity_specification&&$.list[*].distribution_manner&&$.list[*].volume_name&&$.list[*].period_of_validity&&$.list[*].commodity_amount&&$.list[*].affiliation&&$.list[*].price&&$.list[*].commission&&$.list[*].invitation_payment&&$.list[*].status&&$.list[*].status_name&&$.list[*].create_date");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-商城套餐列表单接口");
        }
    }

    @Test(description = "新建商城套餐", enabled = false)
    public void CreateCommodity() {
        try {
            PcCreateStoreCommodity er = new PcCreateStoreCommodity();
            er.commodity_name = "保温杯" + new Random().nextInt(10);
            er.commodity_specification = "颜色:红色";    //规格
            er.price = 0.1;       //单价
            er.commission = 0.1;  //佣金
            er.invitation_payment = 1.99;   //邀请奖励金
            er.voucher_list = pp.getvouchersList();
            jc.CreateStoreCommodity(er);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建商城套餐单接口");
        }
    }

    @Test  //新建商城套餐
    public void CreateCommodityAB() {
        try {
            PcCreateStoreCommodity er = new PcCreateStoreCommodity();
            er.commodity_name = "一二三四五六七八九十一二三四五六七八九十保温杯" + new Random().nextInt(10);
            er.commodity_specification = "颜色:红色";    //规格
            er.price = 89.99;       //单价
            er.commission = 99.99;  //佣金
            er.invitation_payment = 1.99;   //邀请奖励金
            er.voucher_list = pp.getvouchersList();
            er.checkcode = false;
            int code = jc.CreateStoreCommodity(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "创建商城套餐异常");
            er.commodity_name = "保温杯" + new Random().nextInt(10);
            er.commodity_specification = "一:二三 四:五六 七: 八九 十:一二三四五六七八九十红色";  //规格>20
            int code2 = jc.CreateStoreCommodity(er).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "创建商城套餐异常");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建商城套餐异常格式验证");
        }
    }

    @Test(enabled = false, description = "仅编辑商城套餐")
    public void EditCommodity() {
        try {
            String name = "一二三四" + new Random().nextInt(10);
            IScene scene = CommodityPageScene.builder().build();
            CommodityPageBean commodityPageBean = util.toFirstJavaObject(scene, CommodityPageBean.class);
            String commodityName = commodityPageBean.getCommodityName();
            Long id = commodityPageBean.getId();
            JSONObject data = CommodityDetailScene.builder().id(id).build().visitor(visitor).execute();
            data.put("id", id);
            data.put("commodity_name", name);
            CommodityEditScene.builder().build().setRequestBodyBody(data).visitor(visitor).execute();
            String nameAfter = CommodityDetailScene.builder().id(id).build().visitor(visitor).execute().getString("commodity_name");
            Preconditions.checkArgument(nameAfter.equals(name), "编辑名称前名称为：" + commodityName + " 编辑后名称为：" + nameAfter);
            data.put("commodity_specification", "颜色:黑色");
            int code = CommodityEditScene.builder().build().setRequestBodyBody(data).visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code == 1001, "规格编辑不应该成功");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-编辑商城套餐");
        }
    }

    @Test(enabled = false, description = "商城订单页")
    public void StoreOrderList() {
        try {
            JSONObject data = jc.StoreorderPage("1", "10", "", "", "", "");
            //TODO: JsonPath 没校准
            JsonPathUtil.spiltString(data.toJSONString(), "$.commodity_name&&$.commission&&$.commodity_specification&&$.create_date&&$.id&&$.invitation_payment&&$.price&&$.consignee&&$.status_name&&$.subject_type&&$.subject_type_name");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建商城套餐单接口");
        }
    }

    @Test(description = "商城页套餐")
    public void StoreList() {
        try {
            JSONObject data = jc.storeCommodityPage("", "1", "10", "", "");
            JsonPathUtil.spiltString(data.toJSONString(), "$.list[*].commodity_name&&$.list[*].commission&&$.list[*].id&&$.list[*].invitation_payment&&$.list[*].price&&$.list[*].status_name&&$.list[*].subject_type_name");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-商城套餐列表字段不为空校验");
        }
    }

    @Test(enabled = false, description = "商城订单页，作废&发放")
    public void StoreOrderVolumeSend() {
        try {
            //TODO:订单id 需要给定
            //提前创建好订单，写入订单号
            int totalBefore = pf.getVoucherTotal();  //小程序 卡券个数
            //发放，套餐个数+1
            jc.volumeSend(pp.orderId);
            int total = pf.getVoucherTotal();
//            JSONArray list=jc.appletpackageList(null,"GENERAL",20).getJSONArray("list");
//            Integer id=list.getJSONObject(0).getInteger("id");
            //作废，套餐状态变更 失效
            jc.volumeCancel(pp.orderId);
            int totalAfter = pf.getVoucherTotal();
//            JSONArray packageList=jc.appletpackageDeatil(id.toString()).getJSONArray("list");
//            for(int i=0;i<packageList.size();i++) {
//                String status_name=packageList.getJSONObject(i).getString("status_name");
//                Preconditions.checkArgument(status_name.equals("已过期"));
//            }
            Preconditions.checkArgument(totalBefore - total == -1, "发放卡券，卡券数+1");
            Preconditions.checkArgument(totalAfter - total == 1, "作废卡券，卡券数-1");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建商城套餐单接口");
        }
    }

    @Test(description = "新建智能提醒异常情况")
    public void CreateRemindAB() {
        try {
            PcCreateRemind er = new PcCreateRemind();
            er.item = pp.String_20 + "提醒标题";
            er.content = "提醒内容";
            er.vouchers = pp.vouchers;    //卡券
            er.effective_days = "1";     //卡券有效期
//            er.days="1";            //提醒天数
            er.mileage = "200";        //提醒公里数
            er.checkcode = false;
            int code = jc.createRemindMethod(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "创建商城套餐异常");
            er.item = "智能提醒A" + new Random().nextInt(10);
            er.content = pp.String_200 + "提醒内容";
            int code2 = jc.createRemindMethod(er).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "创建商城套餐异常");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建智能提醒异常格式验证");
        }
    }

    @Test(description = "新建智能提醒   单接口", enabled = false)
    public void CreateRemind() {
        try {
            PcCreateRemind er = new PcCreateRemind();
            er.item = "提醒标题";
            er.content = "提醒内容";
            er.vouchers = pp.vouchers2;    //卡券
            er.effective_days = "180";     //卡券有效期
//            er.days="1";            //提醒天数
            er.mileage = "90000";        //提醒公里数
            jc.createRemindMethod(er);
            //编辑
            er.id = jc.remindPage("1", "10", "", "", "").getJSONArray("list").getJSONObject(0).getString("id");
            er.content = "修改提醒内容";
            jc.editRemindMethod(er);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建智能提醒");
        }
    }

    @Test(description = "新建智能提醒（公里数）,由于智能提醒隔天生效，故此case一天运行一次  明天调试", enabled = false)
    public void CreateRemindCheck() {
        try {
            System.out.println(dt.getHistoryDate(-181));
            System.out.println(dt.getHHmm(0, "HH:mm:ss"));
            dt.getHHmm(0);
            String mail = "2001";
            //前提新建好一个任务
            //查询小程序卡券数量
            jc.appletLoginToken(pp.appletToken);
            int total = pf.getVoucherTotal();
            //新建一个excel,里程数=智能提醒公里数
            PoiUtils.importCustomer(mail);
            //导入工单
            pcLogin(pp.jdgw, pp.jdgwpassword);
            jc.pcWorkOrder(pp.importFilepath);      //导入工单文件的路径=新建excel 路径
            sleep(20);
            //查询小程序卡券数量
            jc.appletLoginToken(pp.appletToken);
            int totalAfter = pf.getVoucherTotal();
            PoiUtils.importCustomer(mail);
            pcLogin(pp.jdgw, pp.jdgwpassword);
            jc.pcWorkOrder(pp.importFilepath);      //导入工单文件的路径=新建excel 路径
            sleep(30);
            jc.appletLoginToken(pp.appletToken);
            int totalAfter2 = pf.getVoucherTotal();
            //新建下一个智能提醒
            pcLogin(pp.jdgw, pp.jdgwpassword);
            PcCreateRemind er = new PcCreateRemind();
            er.item = "提醒标题";
            er.content = "提醒内容";
            er.vouchers = pp.vouchers2;    //卡券
            er.effective_days = "1";     //卡券有效期
//            er.days="1";            //提醒天数
            er.mileage = mail;        //提醒公里数
            Integer RemindId = jc.createRemindMethod(er).getInteger("id");
            //公里数同一任务只触发一次智能提醒，小程序收不到卡券
            Preconditions.checkArgument(totalAfter - totalAfter2 == 0, "第一次导入工单后卡券数:" + totalAfter + ";第二次导入工单数：" + totalAfter2 + ",导入工单前：" + total);
            Preconditions.checkArgument(totalAfter - total == 1, "第一次导入工单后卡券数：" + totalAfter + "；导入工单前卡券数：" + total);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建智能提醒（公里）结果验证");
        }
    }

    /**
     * @description :新建一个智能提醒，天数1天，该小程序用户找研发要tocken,隔天接待一次，查询卡券数量
     * @date :2021/1/20 17:57
     **/
    @Test(description = "新建智能提醒（天数 1天）", enabled = false)
    public void CreateRemindCheck2() {
        //TODO:虚拟用户的token 未取到
        try {
            //前提新建好一个任务，在奇数星期 接待这个客户;奇数天，查询小程序卡券数，存下来，作比较；偶数星期 和周日啥也不干
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            if (day % 2 == 0) {  //如果是星期数数是基数且不是周日
                //查询小程序卡券数量
                jc.appletLoginToken(pp.getAppletTokenOther);
                int totalAfter = pf.getVoucherTotal();
                int historyData = qaDbUtil.selectDataTempOne("pcAppointmentRecordNum", "Applet");  //取数据库存好的数
                //接待该客户
                pf.pcStartReception(pp.carPlateOther);      //虚拟小程序客户，车牌号和手机号只有我知道
                Preconditions.checkArgument(totalAfter - historyData == 1, "触发智能提醒，小程序收到卡券");
                qaDbUtil.updateDataNum("Applet", totalAfter);  //把新的卡券数存到数据库
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc-新建智能提醒（天数）结果验证");
        }
    }

    //人员管理2.0
    @Test
    public void accountInfoData_2() {
        try {
            JSONArray list = jc.staffListFilterManage("", "1", "10", "", "").getJSONArray("list");
            for (int i = 1; i < list.size(); i++) {
                String role_name = list.getJSONObject(i).getJSONArray("role_list").getJSONObject(0).getString("role_name");
                JSONArray list1 = jc.organizationRolePage(role_name, 1, 100).getJSONArray("list");
                int account_num = list1.getJSONObject(0).getInteger("num");
                String id = list1.getJSONObject(0).getString("id");
                Integer Total = jc.staffListFilterManage("", "1", "10", "role_id", id).getInteger("total");
                Preconditions.checkArgument(account_num == Total, "角色名为:" + role_name + "的使用账户数量：" + account_num + "！=【账户列表】中该角色的账户数量：" + Total);
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("角色的使用账号数量==账号列表中该角色的数量");
        }

    }

    @Test(description = "导入180天未接待车牌号", enabled = false)
    public void lossCustomer2() {
        try {
            pcLogin(pp.jdgw, pp.jdgwpassword);
            System.out.println(dt.getHistoryDate(-181));
            System.out.println(dt.getHHmm(0, "HH:mm:ss"));
            dt.getHHmm(0);
            String mail = "2001";
            String vin = "ASDAAAAAAA12" + CommonUtil.getRandom(5);
            String plate = "京AS" + CommonUtil.getRandom(4);
            String phone = "15037286013";
            System.out.println("vin" + vin);
            System.out.println("plate" + plate);
            System.out.println("phone" + phone);
            //新建一个excel,里程数=智能提醒公里数
            PoiUtils.importLossCustomer(mail, vin, -181, plate, phone, pp.jdgwName);
            //导入工单
            jc.pcWorkOrder(pp.importFilepath2);      //导入工单文件的路径=新建excel 路径
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("导入流失客户");
        }


    }

    @Test(description = "导入流失客户")
    public void lossCustomer() {
        try {
            System.out.println(dt.getHistoryDate(-366));
            System.out.println(dt.getHHmm(0, "HH:mm:ss"));
            dt.getHHmm(0);
            String mail = "2001";
            String vin = "ASDAAAAAAA12" + CommonUtil.getRandom(5);
            String plate = "京AS" + CommonUtil.getRandom(4);
            String phone = "177" + CommonUtil.getRandom(8);
            System.out.println("vin" + vin);
            System.out.println("plate" + plate);
            System.out.println("phone" + phone);
            //新建一个excel,里程数=智能提醒公里数
            PoiUtils.importLossCustomer(mail, vin, -181, plate, phone, pp.jdgwName);
            //导入工单
            jc.pcWorkOrder(pp.importFilepath2);      //导入工单文件的路径=新建excel 路径
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("导入流失客户");
        }


    }

    @Test(description = "流失客户数量")
    public void customer1() {
        try {
            pcLogin(pp.jdgw, pp.jdgwpassword);
            IScene PreSaleCustomerList = PreSaleCustomerPageScene.builder().page(1).size(10).build();
            Integer total = jc.invokeApi(PreSaleCustomerList).getInteger("total");
            IScene LossCustomerList = LossCustomerPageScene.builder().page(1).size(10).build();
            Integer totalLoss = jc.invokeApi(LossCustomerList).getInteger("total");
            logger.info("销售客户数：" + total);
            logger.info("流失客户数：" + totalLoss);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("导入流失客户");
        }


    }

    @Test(description = "导入潜客,参数全填正常")
    public void importPotentialCustomer() {
        try {
            String[] param = {
                    pp.shopName,
                    "个人",
                    "潜客" + CommonUtil.getRandom(2),
                    "157" + CommonUtil.getRandom(8),
                    "女",
                    "Model",
                    "Model 3",
                    dt.getHistoryDate(0) + " " + dt.getHHmm(0),
                    pp.nameJdgw,
                    pp.jdgw};

            PoiUtils.importPotentialCustomer(param);
            //导入工单
            jc.pcPotentialCustomer(pp.importFilepath3);      //导入工单文件的路径=新建excel 路径
            sleep(10);
            int successNum = pf.importCheck(pp.jdgwName);
            Preconditions.checkArgument(successNum == 1, "导入潜客失败");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("参数全填 导入潜客成功");
        }


    }

    @Test(description = "导入潜客，车系填，车型不填，成功")
    public void importPotentialCustomer2() {
        try {
            String[] param = {
                    pp.shopName,
                    "个人",
                    "潜客" + CommonUtil.getRandom(2),
                    "157" + CommonUtil.getRandom(8),
                    "女",
                    "Model",
                    "",
                    dt.getHistoryDate(0) + " " + dt.getHHmm(0),
                    pp.nameJdgw,
                    pp.jdgw};

            PoiUtils.importPotentialCustomer(param);
            //导入工单
            jc.pcPotentialCustomer(pp.importFilepath3);      //导入工单文件的路径=新建excel 路径
            sleep(10);
            int successNum = pf.importCheck(pp.jdgwName);
            Preconditions.checkArgument(successNum == 1, "导入潜客失败");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("车系正确，车型不填，导入潜客期待成功");
        }


    }

    @Test(description = "导入潜客，车系不填，车型不填，失败")
    public void importPotentialCustomer3() {
        try {
            String[] param = {
                    pp.shopName,
                    "个人",
                    "潜客" + CommonUtil.getRandom(2),
                    "157" + CommonUtil.getRandom(8),
                    "女",
                    "",
                    "",
                    dt.getHistoryDate(0) + " " + dt.getHHmm(0),
                    pp.nameJdgw,
                    pp.jdgw};
            PoiUtils.importPotentialCustomer(param);
            //导入工单
            jc.pcPotentialCustomer(pp.importFilepath3);      //导入工单文件的路径=新建excel 路径
            sleep(10);
            int successNum = pf.importCheck(pp.jdgwName);
            Preconditions.checkArgument(successNum == 0, "车系不填导入成功");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("导入潜客");
        }


    }

    @Test(description = "导入潜客,参数全填正常")
    public void importPotentialCustomer5000() {
        try {
            String[] param = {
                    pp.shopName,
                    "个人",
                    "潜客" + CommonUtil.getRandom(2),
                    "157" + CommonUtil.getRandom(8),
                    "女",
                    "Model",
                    "特斯拉 Model 3",
                    dt.getHistoryDate(0) + " " + dt.getHHmm(0),
                    pp.nameJdgw,
                    pp.jdgw};
            System.out.println(param.length);

            PoiUtils.importPotentialCustomer(param);
            //导入工单
            jc.pcPotentialCustomer(pp.importFilepath3);      //导入工单文件的路径=新建excel 路径
            sleep(10);
            int successNum = pf.importCheck(pp.jdgwName);
            Preconditions.checkArgument(successNum == 1, "导入潜客失败");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("参数全填 导入潜客成功");
        }
    }
}
