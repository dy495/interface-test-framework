package com.haisheng.framework.testng.bigScreen.jiaochen.xmf.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.crm.commonDs.PoiUtils;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.DataAbnormal;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.JcFunction;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.pcCreateGoods;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.pcCreateStoreCommodity;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.pccreateRemind;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.pccreateStoreSales;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class JcPc2_0 extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = ScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    JsonPathUtil jpu = new JsonPathUtil();
    PublicParm pp = new PublicParm();
    JcFunction pf = new JcFunction();
    FileUtil file = new FileUtil();
    Random random = new Random();
    private QADbProxy qaDbProxy = QADbProxy.getInstance();
    public QADbUtil qaDbUtil = qaDbProxy.getQaUtil();
    CommonConfig commonConfig = new CommonConfig();
    public String IpPort = EnumTestProduce.JC_DAILY.getAddress();



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
        commonConfig.checklistQaOwner = "xmf";
        commonConfig.referer = EnumTestProduce.JC_DAILY.getReferer();
        commonConfig.product = EnumTestProduce.JC_DAILY.name();

        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JC_DAILY.getDesc() + commonConfig.checklistQaOwner);


        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};
//        commonConfig.referer="http://dev.dealer-jc.winsenseos.cn/authpage/login";
        //set shop id
        commonConfig.shopId = pp.shopIdZ;
        commonConfig.roleId="603";
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        pcLogin(pp.gwname, pp.gwpassword,pp.roleidJdgw);
        qaDbUtil.openConnection();

    }
    //app登录
    public void appLogin(String username, String password,String roleId) {
        String path = "/jiaochen/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        commonConfig.roleId=roleId;
        httpPost(path, object, EnumTestProduce.JC_DAILY.getAddress());
    }

    //pc登录
    public void pcLogin(String phone, String verificationCode,String roleId) {
        String path = "/jiaochen/login-pc";
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        object.put("verification_code", verificationCode);
        commonConfig.roleId=roleId;
        httpPost(path, object, EnumTestProduce.JC_DAILY.getAddress());
    }
    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
        qaDbUtil.closeConnection();
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


    //2.0
    /**
     * @description :新建分销员
     * @date :2021/1/13 11:23
     **/
    @Test  //新建分销员（必填项+选填项）
    public void CreateSale() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pccreateStoreSales er=new pccreateStoreSales();
            er.sales_phone=pf.genPhoneNum();
            er.sales_name=String.valueOf(System.currentTimeMillis());
            er.shop_id=pp.shopIdZ;
            er.dept_name="分销1部";
            er.job_name="分销员";
            jc.SalesCreate(er);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建分销员单接口");
        }
    }
    @Test  //新建分销员（仅必填项）
    public void CreateSale2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pccreateStoreSales er=new pccreateStoreSales();
            er.sales_phone=pf.genPhoneNum();
            er.shop_id=pp.shopIdZ;
            jc.SalesCreate(er);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建分销员单接口");
        }
    }
    @Test  //新建分销员（必填项不填）
    public void CreateSaleAb0() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pccreateStoreSales er=new pccreateStoreSales();
            er.sales_phone=pf.genPhoneNum();
            er.checkcode=false;
            int code=jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code==1001,"新建分销员必填项不填异常");
            er.sales_phone="";
            er.shop_id=pp.shopIdZ;
            int code2=jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code2==1001,"新建分销员必填项不填异常");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建分销员单接口，必填项不填");
        }
    }
    @Test  //新建分销员参数长度异常
    public void CreateSaleAb1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pccreateStoreSales er=new pccreateStoreSales();
            er.sales_phone=pf.genPhoneNum();
            er.sales_name=String.valueOf(System.currentTimeMillis());
            er.shop_id=pp.shopIdZ;
            er.checkcode=false;
            //名字51
            er.dept_name="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十分销1部";
            er.job_name="分销员";
            int code=jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code==1001,"部门名21异常");
            //岗位21
            er.dept_name="分销1部";
            er.job_name="一二三四五六七八九十一二三四五六七分销员1";
            int code2=jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code2==1001,"岗位名21异常");
            er.sales_name="一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十分销1部";
            er.job_name="分销员";
            int code3=jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code3==1001,"分销员名51异常");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建分销员单接口异常参数验证");
        }
    }
    @Test(dataProvider = "ERR_PHONE",dataProviderClass = DataAbnormal.class)  //新建分销员
    public void CreateSaleAb2(String phone) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pccreateStoreSales er=new pccreateStoreSales();
            er.sales_phone=pf.genPhoneNum();
            er.sales_name=String.valueOf(System.currentTimeMillis());
            er.shop_id=pp.shopIdZ;
            er.sales_phone=phone;
            er.dept_name="分销1部";
            er.job_name="分销员";
            er.checkcode=false;
            int code=jc.SalesCreate(er).getInteger("code");
            Preconditions.checkArgument(code==1001,"新建分销员手机号异常");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建分销员手机号异常");
        }
    }

    @Test   //分销员列表
    public void SaleList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
           JSONObject data= jc.SalesList("1","10",null,null);
            jpu.spiltString(data.toJSONString(),"$.list[*].sales_phone&&$.list[*].shop_name");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-分销员列表单接口");
        }
    }

    @Test  //商城套餐列表
    public void CommodityList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data=jc.StoreCommodityList("1","10",null);
            System.out.println(IpPort);
            jpu.spiltString(data.toJSONString(),"$.list[*].id&&$.list[*].commodity_name&&$.list[*].commodity_specification&&$.list[*].distribution_manner&&$.list[*].volume_name&&$.list[*].period_of_validity&&$.list[*].commodity_amount&&$.list[*].affiliation&&$.list[*].price&&$.list[*].commission&&$.list[*].invitation_payment&&$.list[*].status&&$.list[*].status_name&&$.list[*].create_date");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-商城套餐列表单接口");
        }
    }
//    @Test  //新建商城套餐
    public void CreateCommodity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcCreateStoreCommodity er=new pcCreateStoreCommodity();
            er.commodity_name="保温杯"+random.nextInt(10);
            er.commodity_specification="颜色:红色";    //规格
            er.price=0.1;       //单价
            er.commission=0.1;  //佣金
            er.invitation_payment=1.99;   //邀请奖励金
            er.voucher_list=pp.getvouchersList();

            jc.CreateStoreCommodity( er);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建商城套餐单接口");
        }
    }

    @Test  //新建商城套餐
    public void CreateCommodityAB() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcCreateStoreCommodity er=new pcCreateStoreCommodity();
            er.commodity_name="一二三四五六七八九十一二三四五六七八九十保温杯"+random.nextInt(10);
            er.commodity_specification="颜色:红色";    //规格
            er.price=89.99;       //单价
            er.commission=99.99;  //佣金
            er.invitation_payment=1.99;   //邀请奖励金
            er.voucher_list=pp.getvouchersList();
            er.checkcode=false;
            int code=jc.CreateStoreCommodity( er).getInteger("code");
            Preconditions.checkArgument(code==1001,"创建商城套餐异常");
            er.commodity_name="保温杯"+random.nextInt(10);
            er.commodity_specification="一:二三 四:五六 七: 八九 十:一二三四五六七八九十红色";  //规格>20
            int code2=jc.CreateStoreCommodity( er).getInteger("code");
            Preconditions.checkArgument(code2==1001,"创建商城套餐异常");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建商城套餐异常格式验证");
        }
    }

//    @Test  //下单，发送卡券，下架商城套餐，下单，不发送套餐  TODO:无法自动化实现有赞下单
    public void Commodity2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //固定某一套餐


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建商城套餐单接口");
        }
    }

//    @Test  //下单，发送卡券，下架商城套餐，下单，不发送套餐  TODO:
    public void CommodityUpAndDown() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //固定某一套餐
             int id=0;  //套餐id
            String status="up";    //DOWN 下架
            jc.communityUpAndDown(status,id);



        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建商城套餐单接口");
        }
    }

    @Test  //仅编辑商城套餐
    public void EditCommodity() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data=jc.StoreCommodityDetail(pp.StoreCommodityId);
            data.put("id",pp.StoreCommodityId);
            Random random=new Random();
            String name="一二三四"+random.nextInt(10);
            data.put("commodity_name",name);
            httpPostWithCheckCode("/jiaochen/pc/store/commodity/edit",data.toJSONString(),IpPort);
            String nameAfter=jc.StoreCommodityDetail(pp.StoreCommodityId).getString("commodity_name");
//            Preconditions.checkArgument(nameAfter.equals(name),"编辑名称后没有变化");
            data.put("commodity_specification","颜色:黑色");
            String result=httpPost("/jiaochen/pc/store/commodity/edit",data.toJSONString(),IpPort);
            Integer code=JSONObject.parseObject(result).getInteger("code");
            Preconditions.checkArgument(code==1001,"规格编辑不应该成功");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-编辑商城套餐");
        }
    }

//    @Test  //商城订单页  //TODO: JSONpath 没校准
    public void StoreOrderList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data=jc.StoreorderPage("1","10","","","","");
            jpu.spiltString(data.toJSONString(),"$.commodity_name&&$.commission&&$.commodity_specification&&$.create_date&&$.id&&$.invitation_payment&&$.price&&$.consignee&&$.status_name&&$.subject_type&&$.subject_type_name");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建商城套餐单接口");
        }
    }
    @Test  //商城页套餐
    public void StoreList() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data=jc.storeCommodityPage("","1","10","","");
            jpu.spiltString(data.toJSONString(),"$.list[*].commodity_name&&$.list[*].commission&&$.list[*].id&&$.list[*].invitation_payment&&$.list[*].price&&$.list[*].status_name&&$.list[*].subject_type_name");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-商城套餐列表字段不为空校验");
        }
    }

//    @Test  //商城订单页，作废&发放  TODO:订单id 需要给定
    public void StoreOrderVolumeSend() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //提前创建好订单，写入订单号
            int totalBefore=pf.getVoucherTotal();  //小程序 卡券个数
           //发放，套餐个数+1
            jc.volumeSend(pp.ordeId);

            int total=pf.getVoucherTotal();
//            JSONArray list=jc.appletpackageList(null,"GENERAL",20).getJSONArray("list");
//            Integer id=list.getJSONObject(0).getInteger("id");

            //作废，套餐状态变更 失效
            jc.volumeCancel(pp.ordeId);
            int totalAfter=pf.getVoucherTotal();

//            JSONArray packageList=jc.appletpackageDeatil(id.toString()).getJSONArray("list");
//            for(int i=0;i<packageList.size();i++) {
//                String status_name=packageList.getJSONObject(i).getString("status_name");
//                Preconditions.checkArgument(status_name.equals("已过期"));
//            }
            Preconditions.checkArgument(totalBefore-total==-1,"发放卡券，卡券数+1");
            Preconditions.checkArgument(totalAfter-total==1,"作废卡券，卡券数-1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建商城套餐单接口");
        }
    }

    @Test  //新建智能提醒异常情况
    public void CreateRemindAB() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pccreateRemind er=new pccreateRemind();
            er.item=pp.String_20+"提醒标题";
            er.content="提醒内容";
            er.vouchers=pp.vouchers;    //卡券
            er.effective_days="1";     //卡券有效期
//            er.days="1";            //提醒天数
            er.mileage="200";        //提醒公里数
            er.checkcode=false;
            int code=jc.createRemindMethod( er).getInteger("code");
            Preconditions.checkArgument(code==1001,"创建商城套餐异常");
            er.item="智能提醒A"+random.nextInt(10);
            er.content=pp.String_200+"提醒内容";
            int code2=jc.createRemindMethod( er).getInteger("code");
            Preconditions.checkArgument(code2==1001,"创建商城套餐异常");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建智能提醒异常格式验证");
        }
    }

//    @Test  //新建智能提醒   单接口
    public void CreateRemind() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pccreateRemind er=new pccreateRemind();
            er.item="提醒标题";
            er.content="提醒内容";
            er.vouchers=pp.vouchers2;    //卡券
            er.effective_days="180";     //卡券有效期
//            er.days="1";            //提醒天数
            er.mileage="90000";        //提醒公里数
           jc.createRemindMethod(er);
           String RemindId =jc.remindPage( "1","10","","","").getJSONArray("list").getJSONObject(0).getString("id");
           //编辑
            er.id=RemindId;
            er.content="修改提醒内容";
            jc.editRemindMethod(er);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("pc-新建智能提醒");
        }



    }

    @Test(description = "",enabled = false)  //新建智能提醒（公里数）,由于智能提醒隔天生效，故此case一天运行一次  明天调试
    public void CreateRemindCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            System.out.println(dt.getHistoryDate(-181));
            System.out.println(dt.getHHmm(0,"HH:mm:ss"));
            dt.getHHmm(0);
            String maile="2001";
            //前提新建好一个任务
            //查询小程序卡券数量
            jc.appletLoginToken(pp.appletTocken);
            int total=pf.getVoucherTotal();
            //新建一个excel,里程数=智能提醒公里数
            PoiUtils.importCustomer(maile);
            //导入工单
            pcLogin(pp.jdgw,pp.jdgwpassword,pp.roleidJdgw);
            jc.pcWorkOrder(pp.importFilepath);      //导入工单文件的路径=新建excel 路径
            //查询小程序卡券数量
            jc.appletLoginToken(pp.appletTocken);
            int totalAfter=pf.getVoucherTotal();
            PoiUtils.importCustomer(maile);
            pcLogin(pp.jdgw,pp.jdgwpassword,pp.roleidJdgw);
            jc.pcWorkOrder(pp.importFilepath);      //导入工单文件的路径=新建excel 路径
            jc.appletLoginToken(pp.appletTocken);
            int totalAfter2=pf.getVoucherTotal();
            //新建下一个智能提醒
            pcLogin(pp.jdgw,pp.jdgwpassword,pp.roleidJdgw);
            pccreateRemind er=new pccreateRemind();
            er.item="提醒标题";
            er.content="提醒内容";
            er.vouchers=pp.vouchers2;    //卡券
            er.effective_days="1";     //卡券有效期
//            er.days="1";            //提醒天数
            er.mileage=maile;        //提醒公里数
            Integer RemindId =jc.createRemindMethod(er).getInteger("id");
            sleep(30);
//            公里数同一任务只触发一次智能提醒，小程序收不到卡券
            Preconditions.checkArgument(totalAfter-totalAfter2==0,"第一次导入工单后卡券数:"+totalAfter+";第二次导入工单数："+totalAfter2+",导入工单前："+total);
            Preconditions.checkArgument(totalAfter-total==1,"第一次导入工单后卡券数："+totalAfter+"；导入工单前卡券数："+total);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            pcLogin(pp.jdgw,pp.jdgwpassword,pp.roleidJdgw);
            saveData("pc-新建智能提醒（公里）结果验证");
        }



    }
    /**
     * @description :新建一个智能提醒，天数1天，该小程序用户找研发要tocken,隔天接待一次，查询卡券数量
     * @date :2021/1/20 17:57
     **/
//    @Test  //新建智能提醒（天数 1天）  TODO:虚拟用户的tocken 未取到
    public void CreateRemindCheck2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //前提新建好一个任务，在奇数星期 接待这个客户;奇数天，查询小程序卡券数，存下来，作比较；偶数星期 和周日啥也不干
            Calendar calendar=Calendar.getInstance();

            int day=calendar.get(Calendar.DAY_OF_WEEK);
            if(day%2==0&&day!=Calendar.SUNDAY){  //如果是星期数数是基数且不是周日
                //查询小程序卡券数量
                jc.appletLoginToken(pp.getAppletTockenOther);
                int totalAfter=pf.getVoucherTotal();
                int historyData=qaDbUtil.selsetDataTempOne("pcAppointmentRecordNum","Applet");  //取数据库存好的数
                //接待该客户
                pf.pcstartReception(pp.CarplateOther);      //虚拟小程序客户，车牌号和手机号只有我知道
                Preconditions.checkArgument(totalAfter-historyData==1,"触发智能提醒，小程序收到卡券");
                qaDbUtil.updateDataNum("Applet",totalAfter);  //把新的卡券数存到数据库
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            pcLogin(pp.jdgw,pp.jdgwpassword,pp.roleidJdgw);
            saveData("pc-新建智能提醒（天数）结果验证");
        }



    }

    //人员管理2.0
    @Test
    public void accountInfoData_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.staffListFilterManage("", "1", "10","","").getJSONArray("list");

            for (int i = 1; i < list.size(); i++) {
                String role_name = list.getJSONObject(i).getJSONArray("role_list").getJSONObject(0).getString("role_name");
                JSONArray list1 = jc.organizationRolePage(role_name, 1, 100).getJSONArray("list");
                int account_num = list1.getJSONObject(0).getInteger("num");
                String id = list1.getJSONObject(0).getString("id");

                Integer Total = jc.staffListFilterManage("", "1", "10",   "role_id",id ).getInteger("total");
                Preconditions.checkArgument(account_num == Total, "角色名为:" + role_name + "的使用账户数量：" + account_num + "！=【账户列表】中该角色的账户数量：" + Total);
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("角色的使用账号数量==账号列表中该角色的数量");
        }

    }

    /**
     * @description :创建积分商品
     * @date :2021/1/22 18:42
     **/

//    @Test
    public void createGoods() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int total=jc.GoodsList("1","10").getInteger("total");
            pcCreateGoods er=new pcCreateGoods();

            //品类树
            JSONObject category=jc.categoryList().getJSONArray("list").getJSONObject(0);  //品类数
            ReadContext context = JsonPath.parse(category.toJSONString());
            List<Integer> result = context.read("$..category_id");
            System.out.println("result:"+result);
            System.out.println(result.get(0));
            er.first_category=result.get(0).longValue();
            er.second_category=result.get(1).longValue();
            er.third_category=result.get(2).longValue();  //品类

            er.goods_brand=jc.bandList().getJSONArray("list").getJSONObject(0).getLong("id");  //品牌
            er.price="9.99";  //价格
            er.select_specifications=jc.specifications(er.first_category).getJSONArray("list");  //品类下规格
            jc.createGoodMethod(er);

            JSONObject data=jc.GoodsList("1","10");
            int totalAfterCreate=data.getInteger("total");
            Long id=data.getJSONArray("list").getJSONObject(0).getLong("id");
            jc.deleteGoodMethod(id);
            int totalAfterDelete=jc.GoodsList("1","10").getInteger("total");

            Preconditions.checkArgument(totalAfterCreate-total==1,"新建商品，列表+1");
            Preconditions.checkArgument(totalAfterCreate-totalAfterDelete==-1,"删除商品，列表-1");

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新建/删除积分商品,商品列表+-1");
        }

    }

//    @Test(description = "创建积分商品，名称异常")
    public void createGoodsAb() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcCreateGoods er=new pcCreateGoods();

            //品类树
            JSONObject category=jc.categoryList().getJSONArray("list").getJSONObject(0);  //品类数
            ReadContext context = JsonPath.parse(category.toJSONString());
            List<Long> result = context.read("$..category_id");
            er.first_category=result.get(0);
            er.second_category=result.get(1);
            er.third_category=result.get(2);  //品类
            er.goods_brand=jc.bandList().getJSONArray("list").getJSONObject(0).getLong("id");  //品牌
            er.select_specifications=jc.specifications(er.first_category).getJSONArray("list");  //品类下规格

            er.price="10000000000";  //价格
            int code=jc.createGoodMethod(er).getInteger("code");
            Preconditions.checkArgument(code==1001,"创建商品，价格异常");
            er.price="9.99";

            er.goods_name=pp.String_20+"1";
            int code2=jc.createGoodMethod(er).getInteger("code");
            Preconditions.checkArgument(code2==1001,"创建商品，价格异常");

            er.goods_name=System.currentTimeMillis()+"";
            er.goods_description=pp.String_20+pp.String_20+"1";
            int code3=jc.createGoodMethod(er).getInteger("code");
            Preconditions.checkArgument(code3==1001,"创建商品，价格异常");
            er.goods_description="商品描述";



        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新建/删除积分商品,商品列表+-1");
        }

    }


    @Test(enabled = false,description = "生成文件导入 最新接待时间为未来日期的客户")  //导入流失客户
    public void losscustomer() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            pcLogin(pp.jdgw,pp.jdgwpassword,pp.roleidJdgw);

            System.out.println(dt.getHistoryDate(-366));
            System.out.println(dt.getHHmm(0,"HH:mm:ss"));
            dt.getHHmm(0);
            String maile="2001";
            String vin="ASDAAAAAAA12"+ CommonUtil.getRandom(5);
            String plate="京AS"+CommonUtil.getRandom(4);
            String phone=177+CommonUtil.getRandom(8);
            //新建一个excel,里程数=智能提醒公里数
            PoiUtils.importlossCustomer(maile,vin,-365,plate,phone);
            //导入工单
            jc.pcWorkOrder(pp.importFilepath2);      //导入工单文件的路径=新建excel 路径

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导入流失客户");
        }



    }


}
