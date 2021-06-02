package com.haisheng.framework.testng.bigScreen.fengkongdaily;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.RuleEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum.routerEnum;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.DetailScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.CommonUsedUtil;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.PublicParam;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.util.RiskControlUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RiskControlCaseDataDaily extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce product = EnumTestProduce.FK_DAILY;
    public VisitorProxy visitor =  VisitorProxy.getInstance(product);
    private static final routerEnum router = routerEnum.SHOPDAILY;
    PublicParam pp=new PublicParam();
    CommonUsedUtil cu=new CommonUsedUtil(visitor, router);
    RiskControlUtil ru=new RiskControlUtil();




    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.product = product.getAbbreviation();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "FengKong-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "风控-日常-郭丽雅");        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.referer = product.getReferer();
        commonConfig.shopId = product.getShopId();
        commonConfig.roleId = product.getRoleId();
        beforeClassInit(commonConfig);
        logger.debug("FK: " + cu);
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
        ru.pcLogin(pp.userName,pp.password);
    }

    /**
     * ①新增一个账户，账号管理列表数量+1，新增账号的信息与列表该账号的信息一致 ②删除一个账号，列表数量-1  ③创建者和创建日期==新增账号的当前登录者和当前创建时间----ok
     */
    @Test(description = "①新增一个账户，账号管理列表数量+1，新增账号的信息与列表该账号的信息一致 ②删除一个账号，列表数量-1   ③创建者和创建日期==新增账号的当前登录者和当前创建时间")
    public void authCashierPageData1(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取账号管理页面的账号数量
            int numBefore=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            //新建账号
            String name1=pp.roleName;
            String phone1=pp.ownerPhone;
            String message=cu.createAccountNumber(name1,phone1);
            Preconditions.checkArgument(message.equals("success"), "新建账号失败");
            //获取账号管理页面的账号数量
            JSONObject response=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            int numAfter=response.getInteger("total");
            String phone=response.getJSONArray("list").getJSONObject(0).getString("phone");
            String name=response.getJSONArray("list").getJSONObject(0).getString("name");
            String id=response.getJSONArray("list").getJSONObject(0).getString("id");
            String createTime=response.getJSONArray("list").getJSONObject(0).getString("create_time");
            Preconditions.checkArgument(numAfter==numBefore+1&&name.equals(name1)&&phone.equals(phone1)&&createTime.equals(cu.getDate()),"新建前的账号数量为："+numBefore+"   新建后的数量为："+numAfter+"   新建的姓名和手机号为："+name1+"   "+phone1);

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
     *  ①编辑修改后的账号信息与门店列表该账号的信息一致 ，编辑一个账号提交以后，列表数量不变    --ok
     *  ②禁用一个账号，列表数量不变  --ok
     *  ③将一个昨天增加的账号进行编辑提交，还是首次的创建者和创建日期 --ok
     */
    @Test(description = "①编辑修改后的账号信息与门店列表该账号的信息一致 ，编辑一个账号提交以后，列表数量不变   ②禁用一个账号，列表数量不变   ③将一个昨天增加的账号进行编辑提交，还是首次的创建者和创建日期")
    public void authCashierPageData2(){
        try{
            //获取固定的账号，是以前的创建的数据  (17611474518)
            String id="uid_55e20df0";
            //获取账号管理页面的账号数量
            JSONObject response=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            int numBefore=response.getInteger("total");
            String createTimeBefore=cu.staffIdTransResponse(id).getString("create_time");
            System.out.println("----------"+createTimeBefore);
            //编辑此账号
            String name=pp.roleEditName;
            String phone=pp.ownerPhone;
            String message=cu.getEditAccountNumber(id,name,phone);
            System.out.println("----message------"+message);
            //禁用此账号
            String message2=cu.getStaffStatusChange(id,"DISABLE").getString("message");
            System.out.println("----message2------"+message2);
            //启用此账号
            String message3=cu.getStaffStatusChange(id,"ENABLE").getString("message");
            System.out.println("----message3------"+message3);
            //获取账号管理页面的账号数量
            JSONObject response1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            int numAfter=response1.getInteger("total");
            JSONObject response2=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).name(name).build().invoke(visitor,true);
            String editName=response2.getJSONArray("list").getJSONObject(0).getString("name");
            String createTimeAfter=response2.getJSONArray("list").getJSONObject(0).getString("create_time");
            String editPhone=response2.getJSONArray("list").getJSONObject(0).getString("phone");
            System.out.println("----createTimeAfter------"+createTimeAfter);
            Preconditions.checkArgument(name.equals(editName)&&phone.equals(editPhone),"编辑后的姓名："+editName+"    编辑后的手机号为："+editPhone);
            Preconditions.checkArgument(numAfter==numBefore&&message.equals("success")&&message2.equals("success")&&message3.equals("success")&&createTimeAfter.equals(createTimeBefore),"编辑和禁用前的数量为："+numBefore+"   编辑和禁用后的数量为："+numAfter);

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("①编辑修改后的账号信息与门店列表该账号的信息一致 ，编辑一个账号提交以后，列表数量不变   ②禁用一个账号，列表数量不变   ③将一个昨天增加的账号进行编辑提交，还是首次的创建者和创建日期");
        }
    }

    /**
     * ①新增一个角色，角色列表+1  ②删除一个角色，角色列表-1  --ok
     */
    @Test(description = "①新增一个角色，角色列表+1  ②删除一个角色，角色列表-1")
    public void authCashierPageData3(){
        try{
            //新建角色前的角色数量
            int totalBefore=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            //新建角色
            cu.getAddRole(pp.roleName,pp.descriptionRole);
            //获取此角色的ID
            Long roleId=cu.authRoleNameTransId(pp.roleName);
            //新建角色后的角色数量
            int totalAfter=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            //删除新建的角色
            String message=cu.getDelRole(roleId);
            //删除角色后的角色数量
            int totalDel=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            System.out.println(totalBefore+"    "+totalAfter+"   "+totalDel);
            Preconditions.checkArgument(totalAfter==totalBefore+1&&totalDel==totalAfter-1&&message.equals("success"),"新建前的数量："+totalBefore+"  新建后的数量："+totalAfter+"   删除后的数量："+totalDel);
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("①新增一个角色，角色列表+1  ②删除一个角色，角色列表-1");
        }
    }

    /**
     * ①门店列表的该角色信息==编辑修改后的角色信息 --ok
     */
    @Test(description = "①门店列表的该角色信息==编辑修改后的角色信息")
    public void authCashierPageData4(){
        try{
            //新建角色
            cu.getAddRole(pp.roleName,pp.descriptionRole);
            //获取此角色的ID
            Long roleId=cu.authRoleNameTransId(pp.roleName);
           //编辑角色
            String message=cu.getEditRole(roleId,pp.roleEditName,pp.descriptionEditRole);
            //获取编辑后的角色姓名和角色描述---角色是创建时间的正序
            JSONObject response=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            int pages=response.getInteger("pages");
            JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(pages).size(10).build().invoke(visitor,true).getJSONArray("list");
            String name = list.getJSONObject(list.size()-1).getString("name");
            String description = list.getJSONObject(list.size()-1).getString("description");
            System.out.println(name+"    "+pp.roleEditName+"   "+description+"   "+pp.descriptionEditRole);
            Preconditions.checkArgument(name.equals(pp.roleEditName)&&description.equals(pp.descriptionEditRole)&&message.equals("success"),"编辑后的角色姓名和角色的描述分别是："+name+"   "+description);

        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("①门店列表的该角色信息==编辑修改后的角色信息");
        }
    }

    /**
     * ①使用账号数量==账号列表中的数量  ②某一角色使用账号数量==账号管理中配置了该角色的账号数量  --ok
     */
    @Test(description = "①使用账号数量==账号列表中的数量  ②某一角色使用账号数量==账号管理中配置了该角色的账号数量")
    public void authCashierPageData5(){
        try{
            //角色管理列表
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            int pages=response.getInteger("pages")>3?3:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.role.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    int num=list.getJSONObject(i).getInteger("num");
                    String name=list.getJSONObject(i).getString("name");
                    Long id=list.getJSONObject(i).getLong("id");
                    //账号列表对于角色进行筛选，计算角色对应的账号数量
                    IScene scene1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().roleId(id).page(1).size(10).build();
                    int total=visitor.invokeApi(scene1).getInteger("total");
                    System.out.println(name+"   "+num+"   "+total);
                    Preconditions.checkArgument(num==total,"角色列表中的数量为："+num+"  账号管理里面通过角色筛选的数量为："+total);
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("①使用账号数量==账号列表中的数量  ②某一角色使用账号数量==账号管理中配置了该角色的账号数量");
        }
    }

    /**
     * 累计的风险事件==【收银风控事件】待处理+已处理+已过期--ok
     */
    @Test(description = "累计的风险事件==【收银风控事件】待处理+已处理+已过期")
    public void riskControlData6(){
        try{
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build();
            int pages=visitor.invokeApi(scene).getInteger("pages")>5?5:visitor.invokeApi(scene).getInteger("pages");
            for(int page=1;page<=pages;page++){
                IScene scene1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(page).size(10).build();
                JSONArray list=visitor.invokeApi(scene1).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    Long id=list.getJSONObject(i).getLong("shop_id");
                    //累计风险事件
                    int total=list.getJSONObject(i).getInteger("risk_total");
                    //待处理
                    int pendingTotal=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(id).page(1).size(10).currentState("PENDING").build().invoke(visitor,true).getInteger("total");
                    //已处理
                    int processedTotal=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(id).page(1).size(10).currentState("PROCESSED").build().invoke(visitor,true).getInteger("total");
                    //已过期
                    int ExpiredTotal=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(id).page(1).size(10).currentState("EXPIRED").build().invoke(visitor,true).getInteger("total");
                    Preconditions.checkArgument(total==pendingTotal+processedTotal+ExpiredTotal,"累计风险事件的总数为："+total+"  待处理事件："+pendingTotal+"   已处理的事件为："+processedTotal+"   已处理的事件为:"+ExpiredTotal);
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("累计的风险事件==【收银风控事件】待处理+已处理+已过期");
        }
    }

    /**
     * ①正常事件==【收银风控事件】列表页处理结果为正常的数量 --ok
     * ②异常事件==【收银风控事件】列表页处理结果为异常的数量  --ok
     * ③待处理事件==【收银风控事件】列表页中当前状态为待处理的事件 --ok
     * ④正常事件+异常事件==【收银风控事件】列表页的当前状态为已处理的数量 --ok
     */
    @Test(description = "①正常事件==【收银风控事件】列表页处理结果为正常的数量 ②异常事件==【收银风控事件】列表页处理结果为异常的数量 ③待处理事件==【收银风控事件】列表页中当前状态为待处理的事件  ④正常事件+异常事件==【收银风控事件】列表页的当前状态为已处理的数量  ")
    public void riskControlData8(){
        try{
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build();
            visitor.invokeApi(scene);
            int pages=visitor.invokeApi(scene).getInteger("pages")>5?5:visitor.invokeApi(scene).getInteger("pages");
            for(int page=1;page<=pages;page++){
                IScene scene1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(page).size(10).build();
                JSONArray list=visitor.invokeApi(scene1).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    Long id=list.getJSONObject(i).getLong("shop_id");
                    //正常事件
                    int abnormalTotal=list.getJSONObject(i).getInteger("abnormal_total");
                    //异常事件
                    int normalTotal=list.getJSONObject(i).getInteger("normal_total");
                    //待处理事件
                    int pendingRisksTotal=list.getJSONObject(i).getInteger("pending_risks_total");

                    //已处理
                    int processed=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(id).page(1).size(10).currentState("PROCESSED").build().invoke(visitor,true).getInteger("total");
                    //待处理
                    int pendingTotal=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(id).page(1).size(10).currentState("PENDING").build().invoke(visitor,true).getInteger("total");
                    //收银风控事件里面正常的事件
                    int normal=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(id).page(1).size(10).handleResult("NORMAL").build().invoke(visitor,true).getInteger("total");
                    //收银风控事件里面异常的事件
                    int abnormal=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(id).page(1).size(10).handleResult("ABNORMAL").build().invoke(visitor,true).getInteger("total");
                    Preconditions.checkArgument(abnormalTotal==abnormal,"收银风控中的异常事件："+abnormalTotal+"   收银风控事件里面异常、常的事件为："+abnormal);
                    Preconditions.checkArgument(normalTotal==normal,"收银风控中的正常事件："+normalTotal+"   收银风控事件里面正常的事件为："+normal);
                    Preconditions.checkArgument(pendingTotal==pendingRisksTotal,"收银风控中的待处理事件："+pendingRisksTotal+"   收银风控事件里面待处理的事件为："+pendingTotal);
                    Preconditions.checkArgument(processed==abnormalTotal+normalTotal,"收银风控中的异常事件："+abnormalTotal+"  收银风控中的正常事件："+normalTotal+"   收银风控事件里面已处理事件为："+processed);
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("①正常事件==【收银风控事件】列表页处理结果为正常的数量 ②异常事件==【收银风控事件】列表页处理结果为异常的数量 ③待处理事件==【收银风控事件】列表页中当前状态为待处理的事件  ④正常事件+异常事件==【收银风控事件】列表页的当前状态为已处理的数量  ");
        }
    }

    /**
     * 【收银追溯】页的A小票号的时间==【收银风控时间】中A小票号的时间  --ok
     */
    @Test(description = "【收银追溯】页的A小票号的时间==【收银风控时间】中A小票号的时间")
    public void riskControlData9(){
        try{
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build();
            int pages=visitor.invokeApi(scene).getInteger("pages")>5?5:visitor.invokeApi(scene).getInteger("pages");
            for(int page=1;page<=pages;page++){
                IScene scene1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(page).size(10).build();
                JSONArray list=visitor.invokeApi(scene1).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    Long shopId=list.getJSONObject(i).getLong("shop_id");
                    //收银追溯列表
                    IScene scene2=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.TraceBackScene.builder().shopId(shopId).page(1).size(10).build();
                    JSONObject response=visitor.invokeApi(scene2);
                    int pages1=response.getInteger("pages")>3?3:response.getInteger("pages");
                    for(int page1=1;page1<=pages1;page1++){
                        JSONArray list1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.TraceBackScene.builder().shopId(shopId).page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                       for(int j=0;j<list1.size();j++){
                           //订单号
                           String orderId=list1.getJSONObject(j).getString("order_id");
                           //订单时间
                           String orderTime=list1.getJSONObject(j).getString("order_time");
                           //小票信息详情
                           JSONObject orderResponse=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.OrderDetailScene.builder().shopId(shopId).orderId(orderId).build().invoke(visitor,true);
                           //小票中的订单时间
                           String time=orderResponse.getString("order_time");
                           Preconditions.checkArgument(orderTime.equals(time),"收银追溯中的时间为："+orderTime+"   小票中的时间为："+time);
                       }
                    }
                }
            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("【收银追溯】页的A小票号的时间==【收银风控时间】中A小票号的时间 ");
        }
    }

    /**
     * 待处理进行处理为正常，【收银风控】列表正常事件+1；【收银风控】列表待处理-1，累计风险数量不变  --ok
     */
    @Test(description = "待处理进行处理为正常，【收银风控】列表正常事件+1；【收银风控】列表待处理-1，累计风险数量不变")
    public void riskControlData10(){
        try{
            //获取收银追溯列表第一个门店的门第ID和门店名称，累计正常事件，和待处理事件
            JSONArray dataList = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
            long shopId = dataList.getJSONObject(0).getInteger("shop_id");
            //累计风险数量
            int riskTotal = dataList.getJSONObject(0).getInteger("risk_total");
            //待处理
            int pendingTotal=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(1).size(10).currentState("PENDING").build().invoke(visitor,true).getInteger("total");
            if(pendingTotal>0){
                //收银风控事件里面正常的事件
                int normalTotal=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(1).size(10).handleResult("NORMAL").build().invoke(visitor,true).getInteger("total");

                //获取待处理风险事件ID
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(1).size(10).currentState("PENDING").build().invoke(visitor,true).getJSONArray("list");
                long id = list.getJSONObject(0).getInteger("id");
                String order = list.getJSONObject(0).getString("order_id");

                //将待处理的风控事件处理成正常
                cu.getRiskEventHandle(id,1,"订单正常",null).getString("message");
                //获取处理完以后的累计正常事件和待处理事项
                JSONArray dataList1 = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                //待处理
                int pendingTotal1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(1).size(10).currentState("PENDING").build().invoke(visitor,true).getInteger("total");
                //收银风控事件里面正常的事件
                int normalTotal1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(1).size(10).handleResult("NORMAL").build().invoke(visitor,true).getInteger("total");
                //累计风险数量
                int riskTotal1 = dataList1.getJSONObject(0).getInteger("risk_total");
                Preconditions.checkArgument(normalTotal1==normalTotal+1, "将待处理事件中小票单号为" + order + "处理成正常，【收银风控】列表正常事件-处理前正常事件！=1");
                Preconditions.checkArgument(pendingTotal1==pendingTotal-1, "将待处理事件中小票单号为" + order + "处理成正常，【收银风控】列表待处理没有-1");
                Preconditions.checkArgument(riskTotal == riskTotal1, "将待处理事件中小票单号为" + order + "处理成正常，累计风险数量数量变化了，处理前：" + riskTotal + "处理以后：" + riskTotal1);

            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("待处理进行处理为正常，【收银风控】列表正常事件+1；【收银风控】列表待处理-1，累计风险数量不变");
        }
    }

    /**
     * 待处理进行处理为异常,【收银风控】列表异常事件+1；【收银风控】列表待处理-1，累计风险数量不变  --ok
     */
    @Test(description = "待待处理进行处理为异常,【收银风控】列表异常事件+1；【收银风控】列表待处理-1，累计风险数量不变")
    public void riskControlData11(){
        try{
            //获取收银追溯列表第一个门店的门第ID和门店名称，累计正常事件，和待处理事件
            JSONArray dataList = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
            long shopId = dataList.getJSONObject(0).getInteger("shop_id");
            //累计风险数量
            int riskTotal = dataList.getJSONObject(0).getInteger("risk_total");
            //待处理
            int pendingTotal=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(1).size(10).currentState("PENDING").build().invoke(visitor,true).getInteger("total");
            if(pendingTotal>0){
                //收银风控事件里面异常的事件
                int abnormalTotal=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(1).size(10).handleResult("ABNORMAL").build().invoke(visitor,true).getInteger("total");

                //获取待处理风险事件ID
                JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(1).size(10).currentState("PENDING").build().invoke(visitor,true).getJSONArray("list");
                long id = list.getJSONObject(0).getInteger("id");
                String order = list.getJSONObject(0).getString("order_id");

                //将待处理的风控事件处理成异常
                cu.getRiskEventHandle(id,0,"订单正常",null).getString("message");

                //获取处理完以后的累计正常事件和待处理事项
                JSONArray dataList1 = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                //累计风险数量
                int riskTotal1 = dataList1.getJSONObject(0).getInteger("risk_total");
                //待处理
                int pendingTotal1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(1).size(10).currentState("PENDING").build().invoke(visitor,true).getInteger("total");
                //收银风控事件里面异常的事件
                int abnormalTotal1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(1).size(10).handleResult("ABNORMAL").build().invoke(visitor,true).getInteger("total");
                Preconditions.checkArgument(abnormalTotal1==abnormalTotal+1, "将待处理事件中小票单号为" + order + "处理成异常，【收银风控】列表正常事件-处理前正常事件！=1");
                Preconditions.checkArgument(pendingTotal1==pendingTotal-1, "将待处理事件中小票单号为" + order + "处理成异常，【收银风控】列表待处理没有-1");
                Preconditions.checkArgument(riskTotal == riskTotal1, "将待处理事件中小票单号为" + order + "处理成异常，累计风险数量数量变化了，处理前：" + riskTotal + "处理以后：" + riskTotal1);

            }
        }catch(Exception|AssertionError e){
            collectMessage(e);
        }finally{
            saveData("待处理进行处理为异常,【收银风控】列表异常事件+1；【收银风控】列表待处理-1，累计风险数量不变");
        }
    }

    /**
     *收银风控事件的数据一致性（涉当前状态为【待处理】==处理结果；备注为空）  --ok
     */
    @Test
    public void riskControlData12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取收银追溯列表第一个门店的门第ID
            JSONArray dataList = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
            long shopId = dataList.getJSONObject(0).getInteger("shop_id");

            JSONObject response= RiskEventPageScene.builder().shopId(shopId).page(1).size(10).currentState("PENDING").build().invoke(visitor,true);
            if(response.getJSONArray("list").size()>0){
                int pages=response.getInteger("pages")>5?5:response.getInteger("pages");
                for(int page=1;page<=pages;page++){
                    JSONArray list=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(shopId).page(page).size(10).currentState("PENDING").build().invoke(visitor,true).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String orderId = list.getJSONObject(i).getString("order_id");
                        String responseTime = list.getJSONObject(i).containsKey("response_time")?list.getJSONObject(i).getString("response_time"):"000000";
                        String handler = list.getJSONObject(i).containsKey("handler")?list.getJSONObject(i).getString("handler"):"000000";
                        String remarks = list.getJSONObject(i).containsKey("remarks")?list.getJSONObject(i).getString("remarks"):"000000";
                        String handleResult =list.getJSONObject(i).containsKey("handle_result")?list.getJSONObject(i).getString("handle_result"):"000000";

                        Preconditions.checkArgument(responseTime!=null, "待处理的事件：" + orderId + "的响应时间不为空");
                        Preconditions.checkArgument(handler.equals("000000"), "待处理的事件：" + orderId + "的处理人不为空");
                        Preconditions.checkArgument(remarks.equals("000000"), "待处理的事件：" + orderId + "的备注不为空");
                        Preconditions.checkArgument(handleResult.equals("000000"), "待处理的事件：" + orderId + "的处理结果不为空");
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("当前状态为【待处理】==处理结果；备注为空");
        }

    }

    /**
     * ①新增一个规则,列表风险规则+1，更新时间=新增该规则的时间，更新者==新增该风控规则的人员   ②删除一个风险规则，列表风险规则-1 --ok
     */
    @Test(description = "①新增一个规则,列表风险规则+1，更新时间=新增该规则的时间，更新者==新增该风控规则的人员   ②删除一个风险规则，列表风险规则-1")
    public void riskControlData13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建前风控规则前列表数量
            int totalBefore = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            //新增一个风控规则
            Long id=cu.getRuleAdd(RuleEnum.BLACK_LIST.getType());
            //获取当前时间
            String time= DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm");
            //新建前风控规则后列表数
            JSONObject response= com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            int totalAfter =response.getInteger("total");
            String updateTime=response.getJSONArray("list").getJSONObject(0).getString("update_time").substring(0,16);
            System.err.println(updateTime);
//            String updateUser=response.getJSONArray("list").getJSONObject(0).getString("update_user");
            Preconditions.checkArgument(totalAfter==totalBefore+1&&time.equals(updateTime),"新建前风控规则列表数量:"+totalBefore+"  新建前风控规则后列表数："+totalAfter+"  更新时间和当前时间分别为："+updateTime+"   "+time);
            //关闭风控规则
            cu.ruleSwitch(id,0);
            //删除规则
            String message=cu.ruleDelete(id);
            //新建前风控规则前列表数量
            int totalDel = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            Preconditions.checkArgument(totalAfter==totalDel+1&&message.equals("success"),"删除后风控规则列表数量:"+totalDel+"  删除前风控规则后列表数："+totalAfter );
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("①新增一个规则,列表风险规则+1，更新时间=新增该规则的时间，更新者==新增该风控规则的人员   ②删除一个风险规则，列表风险规则-1");
        }
    }

    /**
     * 开关按钮,列表数量不变,更新者==为开启/关闭规则的账号名,更新时间==更新为开启/关闭规则的时间  --ok
     */
    @Test(description = "开关按钮,列表数量不变,更新者==为开启/关闭规则的账号名,更新时间==更新为开启/关闭规则的时间")
    public void riskControlData14(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            Long id=response.getJSONArray("list").getJSONObject(0).getLong("id");
            System.err.println(id);
            //关闭风控规则前列表数量
            int totalBefore = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            //关闭风控规则
            cu.ruleSwitch(id,0);
            //关闭风控规则后列表数量
            int totalAfter = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            Preconditions.checkArgument(totalBefore==totalAfter,"关闭前风控规则列表数量:"+totalBefore+"  关闭后风控规则后列表数："+totalAfter );
            //开启风控规则
            cu.ruleSwitch(id,1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("开关按钮,列表数量不变,更新者==为开启/关闭规则的账号名,更新时间==更新为开启/关闭规则的时间");
        }
    }

    /**
     * 风控告警列表的数据一致性（最新告警时间>=首次告警时间）  --ok
     */
    @Test(description = "风控告警列表的数据一致性（最新告警时间>=首次告警时间）")
    public void riskControlData15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarm.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            int pages=response.getInteger("pages")>5?5:response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarm.PageScene.builder().page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String firstAlarmTime = list.getJSONObject(i).getString("first_alarm_time");
                    String lastAlarmTime = list.getJSONObject(i).getString("last_alarm_time");
                    Long result = new DateTimeUtil().timeDiff(firstAlarmTime, lastAlarmTime, "yyyy-MM-dd HH:mm:ss");
                    Preconditions.checkArgument(result >= 0, "最新告警时间<首次告警时间，首次告警时间：" + firstAlarmTime + "最新告警时间" + lastAlarmTime);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("风控告警列表的数据一致性（最新告警时间>=首次告警时间）");
        }
    }

    /**
     * ①新增一个风控告警规则,列表风险告警规则+1 ③删除一个风控告警规则，列表风险告警规则-1  --ok
     */
    @Test(description = "①新增一个风控告警规则,列表风险告警规则+1  ③删除一个风控告警规则，列表风险告警规则-1")
    public void riskControlData16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建风控告警规则前列表数量
            int totalBefore = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            //新建风控告警规则
            cu.getAlarmRuleAdd(true,600000L,RuleEnum.CASHIER.getType(),pp.AlarmNameCashier);
            //获取风控告警规则第一条
            IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.PageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene);
            Long id=response.getJSONArray("list").getJSONObject(0).getLong("id");
            //新建风控告警规则列后表数量
            int totalAfter = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            Preconditions.checkArgument(totalBefore==totalAfter-1,"新建风控告警规则列表数量:"+totalBefore+"  新建风控告警规则后列表数："+totalAfter );

            //关闭风控告警规则
            cu.getAlarmRuleSwitch(id,0);
            //删除新建的风控告警规则
            String message=cu.getAlarmRuleDel(id);
            //删除后的风控告警规则的总条数
            int totalDel = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            System.out.println(totalBefore+"-------"+totalAfter+"    "+totalDel);
            Preconditions.checkArgument(totalBefore==totalDel&&message.equals("success"),"删除后风控规则列表数量:"+totalDel+"  删除前风控规则后列表数："+totalAfter );
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("①新增一个风控告警规则,列表风险告警规则+1  ③删除一个风控告警规则，列表风险告警规则-1");
        }
    }

    /**
     * ①编辑后的告警规则信息==列表该告警规则的信息  --ok
     */
    @Test(description = "①编辑后的告警规则信息==列表该告警规则的信息")
    public void riskControlData17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建风控告警规则
            cu.getAlarmRuleAdd(true,600000L,RuleEnum.CASHIER.getType(),pp.AlarmNameCashier);
            //获取风控告警规则第一条
            IScene scene1 =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.PageScene.builder().page(1).size(10).build();
            JSONObject response1=visitor.invokeApi(scene1);
            Long id1=response1.getJSONArray("list").getJSONObject(0).getLong("id");
            //接收人ID
            List<Long> acceptRoleIdList=new ArrayList<>();
            acceptRoleIdList.add(4945L);
            acceptRoleIdList.add(5030L);
            //风控规则的ID
            List<Long> ruleIdList=new ArrayList();
            //风控规则中的对风控规则类型进行筛选，取第一个
            IScene scene =com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.rule.PageScene.builder().page(1).size(10).type(RuleEnum.CASHIER.getType()).build();
            Long id=visitor.invokeApi(scene).getJSONArray("list").getJSONObject(0).getLong("id");
            ruleIdList.add(id);
            //关闭风控告警规则的第一条
            cu.getAlarmRuleSwitch(id1,0);
            //编辑风控告警规则
            String message=cu.getAlarmRuleEdit(id1,true,1800000L,RuleEnum.CASHIER.getType(),ruleIdList,acceptRoleIdList).invoke(visitor,false).getString("message");
            //开启风控告警规则的第一条
            cu.getAlarmRuleSwitch(id1,1);
            //查看风控告警规则的详情
            JSONObject response= DetailScene.builder().id(id1).build().invoke(visitor,true);
            String name=response.getString("name");
            String type=response.getString("type");
            String realTime=response.getString("real_time");
            String silentTime=response.getString("silent_time_name");
            String ruleId=response.getJSONArray("rule_id_list").get(0).toString();
            String roleId=response.getJSONArray("accept_role_id_list").get(0).toString();
            Preconditions.checkArgument(message.equals("success")&&name.contains("已编辑风控告警规则")&&type.equals(RuleEnum.CASHIER.getType())&&realTime.equals("true")&&silentTime.equals("30分钟")&&ruleId.equals(String.valueOf(id))&&roleId.equals(String.valueOf(acceptRoleIdList.get(0))),"编辑风控告警规则，编辑后的各项分别为："+name+"---"+type+"---"+realTime+"---"+silentTime+"---"+ruleId+"----"+roleId);

            //关闭风控告警规则
            cu.getAlarmRuleSwitch(id1,0);
            //删除新建的风控告警规则
            String message2=cu.getAlarmRuleDel(id1);
            Preconditions.checkArgument(message2.equals("success"),"删除失败："+message2);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("①编辑后的告警规则信息==列表该告警规则的信息");
        }
    }


    /**
     * 开关风控告警规则，列表数不变   --ok
     */
    @Test(description = "开关风控告警规则，列表数不变")
    public void riskControlData18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //关闭风控告警规则前列表数量
            JSONObject response=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.PageScene.builder().page(1).size(10).build().invoke(visitor,true);
            int totalBefore = response.getInteger("total");
            Long id=response.getJSONArray("list").getJSONObject(0).getLong("id");

            //关闭风控告警规则
            cu.getAlarmRuleSwitch(id,0);
            //关闭风控告警规则列后表数量
            int totalAfter = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.alarmrule.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            Preconditions.checkArgument(totalBefore==totalAfter,"关闭风控告警规则列表数量:"+totalBefore+"  关闭风控告警规则后列表数："+totalAfter );
            //开启风控告警规则
            cu.getAlarmRuleSwitch(id,1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("开关风控告警规则，列表数不变");
        }
    }

    /**
     * 处理收银风控 事件，加入黑名单，列表+1，再删除，列表-1
     */
    @Test(description = "处理收银风控 事件，加入黑名单，列表+1，再删除，列表-1")
    public void riskControlData19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取黑名单列表的条数
            int totalBefore = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("BLACK").build().invoke(visitor,true).getInteger("total");
            //收银追溯的ID
            Long id=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //待处理事件的ID
            JSONObject response1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(id).page(1).size(10).currentState("PENDING").build().invoke(visitor,true);
            if(response1.getJSONArray("list").size()>0){
                Long pendId=response1.getJSONArray("list").getJSONObject(0).getLong("id");
                //判断此处理事件是否有人脸
                JSONObject res=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventOrderCustomerListScene.builder().id(pendId).build().invoke(visitor,true);
                JSONArray list=res.getJSONArray("list");
                if(list.size()>0){
                    String customerId=list.getJSONObject(0).getString("customer_id");
                    String faceUrl=list.getJSONObject(0).getString("face_url");
                    String transId=list.getJSONObject(0).getString("trans_id");
                    //风控处理事件为：添加白名单
                    JSONArray customerIds=new JSONArray();
                    JSONObject object=new JSONObject();
                    object.put("face_url",faceUrl);
                    object.put("customer_id",customerId);
                    object.put("trans_id",transId);
                    object.put("type","BLACK");
                    customerIds.add(object);
                    cu.getRiskEventHandle(pendId,0,"订单异常，请关注",customerIds);
                    //获取黑名单列表的条数
                    int totalAfter = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("BLACK").build().invoke(visitor,true).getInteger("total");
                    Preconditions.checkArgument(totalAfter==totalBefore+1,"加入黑名单前黑名单的数据为："+totalBefore+"   加入黑名单后黑名单的数据为："+totalAfter);
                    //删除黑名单中
                    cu.getRiskPersonDel(customerId,"BLACK");
                    //删除后黑名单的条数
                    int totalDel = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("BLACK").build().invoke(visitor,true).getInteger("total");
                    Preconditions.checkArgument(totalDel==totalAfter-1,"加入黑名单后黑名单的数据为："+totalAfter+"   删除后黑名单的条数："+totalDel);

                }
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("处理收银风控 事件，加入黑名单，列表+1，再删除，列表-1");
        }
    }

    /**
     * 处理收银风控 事件，加入白名单，列表+1，再删除，列表-1
     */
    @Test(description = "处理收银风控 事件，加入白名单，列表+1，再删除，列表-1")
    public void riskControlData20() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取白名单列表的条数
            int totalBefore = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("WHITE").build().invoke(visitor,true).getInteger("total");
            //收银追溯的ID
            Long id=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //待处理事件的ID
            JSONObject response1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(id).page(1).size(10).currentState("PENDING").build().invoke(visitor,true);
            if(response1.getJSONArray("list").size()>0){
                Long pendId= response1.getJSONArray("list").getJSONObject(0).getLong("id");

                //判断此处理事件是否有人脸
                JSONObject res=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventOrderCustomerListScene.builder().id(pendId).build().invoke(visitor,true);
                JSONArray list=res.getJSONArray("list");
                System.out.println(list.size()+"list------"+list);
                if(list.size()>0){
                    String customerId=list.getJSONObject(0).getString("customer_id");
                    String faceUrl=list.getJSONObject(0).getString("face_url");
                    String transId=list.getJSONObject(0).getString("trans_id");
                    //风控处理事件为：添加白名单
                    JSONArray customerIds=new JSONArray();
                    JSONObject object=new JSONObject();
                    object.put("face_url",faceUrl);
                    object.put("customer_id",customerId);
                    object.put("trans_id",transId);
                    object.put("type","WHITE");
                    customerIds.add(object);
                    cu.getRiskEventHandle(pendId,1,"订单正常，请关注",customerIds);
                    //获取白名单列表的条数
                    int totalAfter = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("WHITE").build().invoke(visitor,true).getInteger("total");
                    Preconditions.checkArgument(totalAfter==totalBefore+1,"加入白名单前黑名单的数据为："+totalBefore+"   加入白名单后黑名单的数据为："+totalAfter);
                    //删除白名单中
                    cu.getRiskPersonDel(customerId,"WHITE");
                    //删除后白名单的条数
                    int totalDel = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("WHITE").build().invoke(visitor,true).getInteger("total");
                    Preconditions.checkArgument(totalDel==totalAfter-1,"加入白名单后的数据为："+totalAfter+"   删除后白名单的条数："+totalDel);

                }
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("处理收银风控 事件，加入白名单，列表+1，再删除，列表-1");
        }
    }

    /**
     * 处理收银风控 事件，加入重点观察人员，列表+1，再删除，列表-1
     */
    @Test(description = "处理收银风控 事件，加入重点观察人员，列表+1，再删除，列表-1")
    public void riskControlData21() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取重点观察人员列表的条数
            int totalBefore = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("FOCUS").build().invoke(visitor,true).getInteger("total");
            //收银追溯的ID
            Long id=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0).getLong("shop_id");
            //待处理事件的ID
            JSONObject response1=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventPageScene.builder().shopId(id).page(1).size(10).currentState("PENDING").build().invoke(visitor,true);
            if(response1.getJSONArray("list").size()>0){
                Long pendId=response1.getJSONArray("list").getJSONObject(0).getLong("id");

                //判断此处理事件是否有人脸
                JSONObject res=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.cashier.RiskEventOrderCustomerListScene.builder().id(pendId).build().invoke(visitor,true);
                JSONArray list=res.getJSONArray("list");
                System.out.println(list.size()+"list------"+list);
                if(list.size()>0){
                    String customerId=list.getJSONObject(0).getString("customer_id");
                    String faceUrl=list.getJSONObject(0).getString("face_url");
                    String transId=list.getJSONObject(0).getString("trans_id");
                    //风控处理事件为：添加白名单
                    JSONArray customerIds=new JSONArray();
                    JSONObject object=new JSONObject();
                    object.put("face_url",faceUrl);
                    object.put("customer_id",customerId);
                    object.put("trans_id",transId);
                    object.put("type","FOCUS");
                    customerIds.add(object);
                    cu.getRiskEventHandle(pendId,0,"订单异常，请关注",customerIds);
                    //获取重点观察人员列表的条数
                    int totalAfter = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("FOCUS").build().invoke(visitor,true).getInteger("total");
                    Preconditions.checkArgument(totalAfter==totalBefore+1,"加入重点观察人员前重点观察人员的数据为："+totalBefore+"   加入重点观察人员后重点观察人员的数据为："+totalAfter);
                    //删除重点观察人员中
                    cu.getRiskPersonDel(customerId,"FOCUS");
                    //删除后重点观察人员的条数
                    int totalDel = com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.riskpersonnel.PageScene.builder().page(1).size(10).type("FOCUS").build().invoke(visitor,true).getInteger("total");
                    Preconditions.checkArgument(totalDel==totalAfter-1,"加入黑名单后重点观察人员的数据为："+totalAfter+"   删除后重点观察人员的条数："+totalDel);

                }

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("处理收银风控 事件，加入重点观察人员，列表+1，再删除，列表-1");
        }
    }

    /**
     * 员工信息查询，总数>=【账号管理】数量   ----不成立
     */
    @Test(description = "员工信息查询，总数>=【账号管理】数量",enabled = false)
    public void riskControlData22() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取账号管理页面的账号数量
            int num=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff.PageScene.builder().page(1).size(10).build().invoke(visitor,true).getInteger("total");
            //获取员工查询信息列表的条数
            IScene scene=com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.outstaff.PageScene.builder().page(1).size(10).build();
            int total=visitor.invokeApi(scene).getInteger("tatal");
            Preconditions.checkArgument(num<=total,"账号管理中的数量为："+num+"   员工信息查询中的数量为："+total);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("员工信息查询，总数>=【账号管理】数量");
        }
    }










}
