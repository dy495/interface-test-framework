package com.haisheng.framework.testng.bigScreen.fengkongdaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.CommonUtil;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.PublicParam;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.RiskControlUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
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
import java.util.ArrayList;
import java.util.List;

public class RiskControlCaseDataDaily extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.FK_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
    StoreScenarioUtil md = StoreScenarioUtil.getInstance();
    //    StoreFuncPackage mds = StoreFuncPackage.getInstance();
    PublicParam pp=new PublicParam();
    CommonUtil cu=new CommonUtil();
    RiskControlUtil ru=new RiskControlUtil();




    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "FengKong-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "风控 日常");
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672","18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);
        logger.debug("store " + md);
        ru.pcLogin(pp.userName,pp.password);
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
     * ①新增一个账户，账号管理列表数量+1，新增账号的信息与列表该账号的信息一致 ②删除一个账号，列表数量-1  ③创建者和创建日期==新增账号的当前登录者和当前创建时间
     */
    @Test(description = "①新增一个账户，账号管理列表数量+1，新增账号的信息与列表该账号的信息一致 ②删除一个账号，列表数量-1   ③创建者和创建日期==新增账号的当前登录者和当前创建时间")
    public void authCashierPageData1(){
        try{
            //获取账号管理页面的账号数量
            int numBefore=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            //新建账号
            String id=cu.createAccountNumber(pp.roleName,pp.ownerPhone);
            Preconditions.checkArgument(id!=null, "新建账号失败");
            //获取账号管理页面的账号数量
            JSONObject response=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            int numAfter=response.getInteger("total");
            String phone=response.getJSONArray("list").getJSONObject(0).getString("phone");
            String name=response.getJSONArray("list").getJSONObject(0).getString("name");
//            String createTime=response.getJSONArray("list").getJSONObject(0).getString("create_time");
            Preconditions.checkArgument(numAfter==numBefore+1&&name.equals(pp.staffName)&&phone.equals(pp.ownerPhone),"新建前的账号数量为："+numBefore+"   新建后的数量为："+numAfter+"   新建的姓名和手机号为："+name+"   "+phone);

            //删除新建的账号
            IScene scene4=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.DeleteScene.builder().id(id).build();
            String message4=visitor.invokeApi(scene4,false).getString("message");
            JSONObject response1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            int numAfter1=response1.getInteger("total");
            Preconditions.checkArgument(message4.equals("success")&&numAfter==numAfter1+1, "删除账号:" + id + "失败了");

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("①新增一个账户，账号管理列表数量+1，新增账号的信息与列表该账号的信息一致 ②删除一个账号，列表数量-1   ③创建者和创建日期==新增账号的当前登录者和当前创建时间");
        }
    }


    /**
     *  ①编辑修改后的账号信息与门店列表该账号的信息一致 ，编辑一个账号提交以后，列表数量不变   ②禁用一个账号，列表数量不变
     *   ③将一个昨天增加的账号进行编辑提交，还是首次的创建者和创建日期---todo
     */
    @Test(description = "     *  ①编辑修改后的账号信息与门店列表该账号的信息一致 ，编辑一个账号提交以后，列表数量不变   ②禁用一个账号，列表数量不变   ③将一个昨天增加的账号进行编辑提交，还是首次的创建者和创建日期\n")
    public void authCashierPageData2(){
        try{
            //新建账号
            String id=cu.createAccountNumber(pp.roleName,pp.ownerPhone);
            //获取账号管理页面的账号数量
            JSONObject response=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            int numBefore=response.getInteger("total");
            //编辑此账号
            String message=cu.getEditAccountNumber(id,pp.roleEditName,pp.ownerPhone);
            //禁用此账号
            String message2=cu.getStaffStatusChange(id,"").getString("message");
            //获取账号管理页面的账号数量
            JSONObject response1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            int numAfter=response1.getInteger("total");
            Preconditions.checkArgument(numAfter==numBefore&&message.equals("success")&&message2.equals("success"),"编辑和禁用前的数量为："+numBefore+"   编辑和禁用后的数量为："+numAfter);


        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("①编辑修改后的账号信息与门店列表该账号的信息一致 ，编辑一个账号提交以后，列表数量不变   ②禁用一个账号，列表数量不变   ③将一个昨天增加的账号进行编辑提交，还是首次的创建者和创建日期");
        }
    }


}
