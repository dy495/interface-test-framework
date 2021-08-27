package com.haisheng.framework.testng.bigScreen.itemMall.caseonline.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.AuthTreeScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.role.RoleAddScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.role.RoleDeleteScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.role.RoleEditScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview.OverviewShopOverviewScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.shop.CustomerTrendScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.shop.ShopDetailScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.shop.ShopPageScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.shop.ShopFloorListScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.util.LoginUntil;
import com.haisheng.framework.testng.bigScreen.itemMall.common.util.MallScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.role.RolePageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.staff.StaffPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
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

public class MallCase extends TestCaseCommon implements TestCaseStd {
    private final EnumTestProduct product = EnumTestProduct.MALL_ONLINE;
    private final EnumTestProduct product1 = EnumTestProduct.MALL_ONLINE_SSO;
    private static final AccountEnum ACCOUNT_ENUM = AccountEnum.MALL_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(product);
    public VisitorProxy visitor1 = new VisitorProxy(product1);
    public LoginUntil user = new LoginUntil(visitor1);
    MallScenarioUtil mall = MallScenarioUtil.getInstance();
    public static final int page = 1;
    public static final int size = 100;
    public static final String starting = dt.getHistoryDate(-7);
    public static final String ending = dt.getHistoryDate(-1);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_SHOPMALL_Online_SERVICE;
        commonConfig.checklistQaOwner = "周涛";
//        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.SHOPMALL_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc());
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation()).setMallId("4283");
        commonConfig.setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        user.loginPc(ACCOUNT_ENUM);
        logger.debug("beforeMethod");
//        mall.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);

    }

    /*
       --------------------------------------------------------------购物中心门店客流总览----------------------------------------------------------------
                                                                                                                                                    */
    @Test(description = "通过楼层筛选")
    public void mallShopSystemCase() {
        logger.logCase(caseResult.getCaseName());
        try {
            //获取楼层list
            JSONArray floorlist = ShopFloorListScene.builder().build().execute(visitor, true).getJSONArray("list");
            //获取楼层id
            for(int i = 0; i<floorlist.size();i++){
                Long floorId = floorlist.getJSONObject(i).getLong("id");
                String floorname = floorlist.getJSONObject(i).getString("name");
                JSONArray shoplist = ShopPageScene.builder().size(size).page(page).floorId(floorId).startTime(starting).endTime(ending).build().execute(visitor,true).getJSONArray("list");
                //门店楼层
                for (int j = 0; j<shoplist.size(); j++){
                    Long shopid = shoplist.getJSONObject(j).getLong("id");
                    String floor_name = ShopDetailScene.builder().shopId(shopid).startTime(starting).endTime(ending).build().execute(visitor,true).getString("floor_name");
                    Preconditions.checkArgument(floorname.equals(floor_name),"输入框输入"+floorname+"列表展示门店不属于此楼层，属于"+floor_name);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        }
        saveData("通过楼层筛选");
    }

    @Test(description = "各楼层门店数量之和==列表门店之和")
    public void mallShopDataCase1(){
        try{
            JSONArray shoplist0 = ShopPageScene.builder().size(size).page(page).startTime(starting).endTime(ending).build().execute(visitor,true).getJSONArray("list");
            //目前日常环境展示了全店区域的，所有需要-1,
            int shopNum = shoplist0.size()-1;
            JSONArray floorlist = ShopFloorListScene.builder().build().execute(visitor,true).getJSONArray("list");
            int count = 0;
            for (int i=0; i<floorlist.size();i++){
                Long floorId = floorlist.getJSONObject(i).getLong("id");
                //获取门店数量
                JSONArray shoplist = ShopPageScene.builder().size(size).page(page).floorId(floorId).startTime(starting).endTime(ending).build().execute(visitor,true).getJSONArray("list");
                count +=shoplist.size();
            }
            Preconditions.checkArgument(shopNum==count,"楼层门店数之和"+count+"！=列表门店之和"+shopNum);
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("各楼层门店数量之和==列表门店之和");
    }

    @Test(description = "角色管理-通过筛选框筛选角色")
    public void mallShopSystemCase1(){
        logger.logCase(caseResult.getCaseName());
        try{
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            JSONArray list = RolePageScene.builder().page(page).size(10).build().execute(visitor,true).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String name = list.getJSONObject(i).getString("name");
                JSONArray list_size = RolePageScene.builder().name(name).page(page).size(10).build().execute(visitor,true).getJSONArray("list");
                for (int j = 0;j<list_size.size();j++){
                    String name2 = list_size.getJSONObject(j).getString("name");
                    Preconditions.checkArgument(name2.contains(name),"输入框输入"+name+"列表展示"+name2);
                }
            }
        }
        catch (AssertionError |Exception e){
            appendFailReason(e.toString());
        }
        saveData("角色管理-通过筛选框筛选角色");
    }

    @Test(description = "角色管理-添加角色--编辑角色--删除角色")
    public void mallShopSystemCase2(){
        logger.logCase(caseResult.getCaseName());
        try{
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            String mess = RoleAddScene.builder().name("自动化添加角色").description(EnumDesc.DESC_BETWEEN_5_10.getDesc()).authList(mallMethod()).parentRoleId(10107).build().execute(visitor,false).getString("message");
            Preconditions.checkArgument(mess.equals("success"),"创建角色失败"+mess);
            JSONArray list_size = RolePageScene.builder().name("自动化添加角色").page(page).size(10).build().execute(visitor,true).getJSONArray("list");
            int id = list_size.getJSONObject(0).getInteger("id");
            String message = RoleEditScene.builder().name("自动化编辑角色").description(EnumDesc.DESC_BETWEEN_20_30.getDesc()).authList(mallMethod()).id(id).parentRoleId(10107).build().execute(visitor,false).getString("message");
            Preconditions.checkArgument(message.equals("success"),"编辑角色失败"+message);
            String mes = RoleDeleteScene.builder().id(id).build().execute(visitor,false).getString("message");
            Preconditions.checkArgument(mes.equals("success"),"删除角色失败"+message);
        }
        catch (AssertionError |Exception e){
            appendFailReason(e.toString());
        }
        saveData("角色管理-添加角色--编辑角色--删除角色");
    }

    @Test(description = "账号管理-筛选框")
    public void mallShopSystemCase3(){
        logger.logCase(caseResult.getCaseName());
        try{
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            JSONArray list = StaffPageScene.builder().name("越秀测试账号").phone("18513118484").shopId(55456L).roleId(10107).page(page).size(size).build().execute(visitor,true).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String name = list.getJSONObject(i).getString("name");
                String phone = list.getJSONObject(i).getString("phone");
                Preconditions.checkArgument(name.equals("越秀测试账号"),"门店名称"+name);
                Preconditions.checkArgument(phone.equals("18513118484"),"手机号"+phone);
                JSONArray rloe_list = list.getJSONObject(i).getJSONArray("role_list");
                for(int j=0;j<rloe_list.size();j++){
                    int role_id = rloe_list.getJSONObject(j).getInteger("role_id");
                    if (role_id==10107){
                        int shop_id = rloe_list.getJSONObject(j).getJSONArray("shop_list").getJSONObject(0).getInteger("shop_id");
                        Preconditions.checkArgument(shop_id==55456,"门店返回失败"+shop_id);
                    }
                }
            }
        }
        catch (AssertionError |Exception e){
            appendFailReason(e.toString());
        }
        saveData("账号管理-筛选框");
    }

    @Test(description = "列表页门店数据==门店详情页门店数据")
    public void mallShopDataCase2(){
        try{
            //获取门店列表页
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().execute(visitor,true).getJSONArray("list");
            for (int i=0;i<shopList.size();i++){
                //判断返回值里有没有参数，没有则跳过，有则取出它的id和数据
                boolean contains = shopList.getJSONObject(i).containsKey("enter_number");
                if(contains){
                    Long shopId = shopList.getJSONObject(i).getLong("id");
                    //进店人数
                    Integer enter_number = shopList.getJSONObject(i).getInteger("enter_number");
                    //进店人数环比
                    String enter_number_qoq = shopList.getJSONObject(i).getString("enter_number_qoq");
                    //进店率
                    String enter_percentage = shopList.getJSONObject(i).getString("enter_percentage");
                    //进店率环比
                    String enter_percentage_qoq = shopList.getJSONObject(i).getString("enter_percentage_qoq");
                    //门店名
                    String name = shopList.getJSONObject(i).getString("name");
                    //过店人数
                    Integer pass_number = shopList.getJSONObject(i).getInteger("pass_number");
//                    过店人数环比
                    String pass_number_qoq = shopList.getJSONObject(i).getString("pass_number_qoq");
                    //人均停留时长
                    Integer stay_time_avg = shopList.getJSONObject(i).getInteger("stay_time_avg");
                    //人均停留时长环比
                    String stay_time_avg_qoq = shopList.getJSONObject(i).getString("stay_time_avg_qoq");
                    //光顾率
                    String visit_percentage = shopList.getJSONObject(i).getString("visit_percentage");
                    //光顾率环比
                    String visit_percentage_qoq = shopList.getJSONObject(i).getString("visit_percentage_qoq");

                    //利用获取到的id进入此门店详情页，对比数据一致性
                    //进店人数相关
                    JSONObject entry_overview = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().execute(visitor,true).getJSONObject("entry_overview");
                    //客户进店率相关
                    JSONObject entry_percentage_overview = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().execute(visitor,true).getJSONObject("entry_percentage_overview");
                    //人均停留时长相关
                    JSONObject stay_time_overview = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().execute(visitor,true).getJSONObject("stay_time_overview");
                    //客户光顾率相关
                    JSONObject visit_percentage_overview = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().execute(visitor,true).getJSONObject("visit_percentage_overview");
                    //门店进店人数、环比
                    Integer shop_num = entry_overview.getInteger("number");
                    String enter_day_qoq = entry_overview.getString("day_qoq");
                    Preconditions.checkArgument(shop_num.equals(enter_number),"门店进店人数"+enter_number+"！=门店详情页进店人数"+shop_num+"门店是"+name);
                    Preconditions.checkArgument(enter_number_qoq.equals(MallScenarioUtil.getPercentFormat(enter_day_qoq)),"门店进店环比"+enter_number_qoq+"！=门店详情页进店人数环比"+MallScenarioUtil.getPercentFormat(enter_day_qoq)+"门店是"+name);
                    //门店进店率、进店率环比
                    String entry_day_qoq = entry_percentage_overview.getString("percentage");
                    String entry_percentage = entry_percentage_overview.getString("day_qoq");
                    Preconditions.checkArgument(enter_percentage.equals(entry_day_qoq),"门店进店率"+enter_percentage+"！=门店详情页进店率"+entry_day_qoq+"门店是"+name);
                    Preconditions.checkArgument(enter_percentage_qoq.equals(MallScenarioUtil.getPercentFormat(entry_percentage)),"门店进店率环比"+enter_percentage_qoq+"！=门店详情页进店率环比"+MallScenarioUtil.getPercentFormat(entry_percentage)+"门店是"+name);
                    //门店光顾率、光顾率环比
                    String visit_day_qoq = visit_percentage_overview.getString("day_qoq");
                    String visit_per = visit_percentage_overview.getString("percentage");
                    Preconditions.checkArgument(visit_per.equals(visit_percentage),"门店光顾率"+enter_percentage+"！=门店详情页进店率"+visit_per+"门店是"+name);
                    Preconditions.checkArgument(visit_percentage_qoq.equals(MallScenarioUtil.getPercentFormat(visit_day_qoq)),"门店光顾率环比"+enter_percentage_qoq+"！=门店详情页光顾率环比"+MallScenarioUtil.getPercentFormat(visit_day_qoq)+"门店是"+name);
                    //门店人均停留时长、停留时长环比
                    Integer time_num = stay_time_overview.getInteger("number");
                    String time_day_qoq = stay_time_overview.getString("day_qoq");
                    Preconditions.checkArgument(stay_time_avg.equals(time_num),"人均停留时长"+stay_time_avg+"！=门店详情页人均停留时长"+time_num+"门店是"+name);
                    Preconditions.checkArgument(stay_time_avg_qoq.equals(MallScenarioUtil.getPercentFormat(time_day_qoq)),"门店人均停留时长环比"+stay_time_avg_qoq+"！=门店详情页人均停留时长环比"+MallScenarioUtil.getPercentFormat(time_day_qoq)+"门店是"+name);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("列表页门店数据==门店详情页门店数据");
    }

    @Test(description = "列表页门店数据==门店详情页趋势图门店数据")
    public void mallShopDataCase3(){
        try{
            //获取门店列表页
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().execute(visitor,true).getJSONArray("list");
            for (int i=0;i<shopList.size();i++){
                //判断返回值里有没有参数，没有则跳过，有则取出它的id和数据
                boolean contains = shopList.getJSONObject(i).containsKey("enter_number");
                if(contains){
                    Long shopId = shopList.getJSONObject(i).getLong("id");
                    //进店人数
                    Integer enter_number = shopList.getJSONObject(i).getInteger("enter_number");
                    //门店名
                    String name = shopList.getJSONObject(i).getString("name");
                    //过店人数
                    Integer pass_number = shopList.getJSONObject(i).getInteger("pass_number");
                    //利用获取到的id进入此门店详情页，对比数据一致性
                    //进店人数相关
                    JSONObject entry_overview = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().execute(visitor,true).getJSONObject("entry_overview");
                    //进店人数
                    int entry_num = entry_overview.getInteger("number");
                    JSONArray list = CustomerTrendScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().execute(visitor,true).getJSONArray("list");
                    int count_enter_uv = 0;
                    int count_pass_uv = 0;
                    for (int j=0;j<list.size();j++){
                        int enter_uv=list.getJSONObject(j).containsKey("enter_uv")?list.getJSONObject(j).getInteger("enter_uv"):0;
                        //key名写错了，目前返回的是enter_pv作为pass_us，数是正确的，后续有变化后续更改
                        int pass_uv=list.getJSONObject(j).containsKey("enter_pv")?list.getJSONObject(j).getInteger("enter_pv"):0;
                        count_enter_uv += enter_uv;
                        count_pass_uv += pass_uv;
                    }
                    Preconditions.checkArgument(entry_num==count_enter_uv,"门店详情页进店人数"+entry_num+"！=门店详情页趋势图进店人数"+count_enter_uv+"门店是"+name);
                    Preconditions.checkArgument(enter_number.equals(count_enter_uv),"门店列表页进店人数"+enter_number+"！=门店详情页趋势图进店人数"+count_enter_uv+"门店是"+name);
                    Preconditions.checkArgument(pass_number.equals(count_pass_uv),"门店列表页进店人数"+pass_number+"！=门店详情页趋势图进店人数"+count_pass_uv+"门店是"+name);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("列表页门店数据==门店详情页趋势图门店数据");
    }

    @Test(description = "门店进店人数环比=上周期-上上周期/上上周期")
    public void mallShopDataCase4(){
        try {
            //获取门店列表页
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().execute(visitor, true).getJSONArray("list");
            for(int i=0; i<shopList.size();i++){
                Long shopId = shopList.getJSONObject(i).getLong("id");
                String name = shopList.getJSONObject(i).getString("name");
                JSONObject data = CustomerTrendScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().execute(visitor,false).getJSONObject("data");
                JSONObject data2 = CustomerTrendScene.builder().shopId(shopId).startTime(dt.getHistoryDate(-14)).endTime(dt.getHistoryDate(-8)).build().execute(visitor,false).getJSONObject("data");
                if (data.size()!=0&&data2.size()!=0){
                    JSONArray list = data.getJSONArray("list");
                    JSONArray list1 = data2.getJSONArray("list");
                    double count = 0;
                    double count2 = 0;
                    for(int j=0;j<list.size();j++){
                        double enter_uv=list.getJSONObject(j).containsKey("enter_uv")?list.getJSONObject(j).getDouble("enter_uv"):0;
                        count += enter_uv;
                        double enter_uv1=list1.getJSONObject(j).containsKey("enter_uv")?list1.getJSONObject(j).getDouble("enter_uv"):0;
                        count2 += enter_uv1;
                    }
                    double x = (count-count2)/count2;
                    String day_qoq = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().execute(visitor,true).getJSONObject("entry_overview").getString("day_qoq");
                    Preconditions.checkArgument(MallScenarioUtil.getPercentFormat(String.valueOf(x)).equals(MallScenarioUtil.getPercentFormat(day_qoq)),"门店进店人数环比是"+MallScenarioUtil.getPercentFormat(String.valueOf(x))+"！=页面展示环比"+MallScenarioUtil.getPercentFormat(day_qoq)+"门店是"+name);
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("门店进店人数环比");
    }

    @Test(description = "进店率环比=上周期-上上周期/上上周期")
    public void mallShopDataCase5(){
        try {
            //获取门店列表页
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().execute(visitor,true).getJSONArray("list");
            JSONArray shopList1 = ShopPageScene.builder().page(page).size(size).startTime(dt.getHistoryDate(-14)).endTime(dt.getHistoryDate(-8)).build().execute(visitor,true).getJSONArray("list");
            for (int i=0;i<shopList.size();i++){
                //判断返回值里有没有参数，没有则跳过，有则取值
                boolean contains = shopList.getJSONObject(i).containsKey("enter_number");
                boolean contains1 = shopList1.getJSONObject(i).containsKey("enter_number");
                if(contains&&contains1) {
                    String enter_percentage = shopList.getJSONObject(i).getString("enter_percentage");
                    String enter_percentage1 = shopList1.getJSONObject(i).getString("enter_percentage");
                    String enter_percentage_qoq = shopList.getJSONObject(i).getString("enter_percentage_qoq");
                    String name = shopList.getJSONObject(i).getString("name");
                    double port=Double.parseDouble(enter_percentage.substring(0,enter_percentage.length()-1));
                    double port1=Double.parseDouble(enter_percentage1.substring(0,enter_percentage1.length()-1));
                    //分母是0，没有环比
                    if(port1!=0.00){
                        double x = (port-port1)/port1;
                        Preconditions.checkArgument(
                                MallScenarioUtil.getPercentFormat(String.valueOf(x)).equals(enter_percentage_qoq),
                                "门店进店率环比是"+MallScenarioUtil.getPercentFormat(String.valueOf(x))+"！=页面展示环比"+enter_percentage_qoq+"门店是"+name);
                    }else {//上上周期进店率0的，本周期没有环比
                        System.err.println(enter_percentage_qoq);
                        Preconditions.checkArgument("0.00%".equals(enter_percentage_qoq), "门店进店率环比是"+"0.00%"+"！=页面展示环比"+enter_percentage_qoq+"门店是"+name);
                        }
                    }
                }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("门店进店率环比=上周期-上上周期/上上周期");
    }

    @Test(description = "光顾率环比=上周期-上上周期/上上周期")
    public void mallShopDataCase6(){
        try {
            //获取门店列表页
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().execute(visitor,true).getJSONArray("list");
            JSONArray shopList1 = ShopPageScene.builder().page(page).size(size).startTime(dt.getHistoryDate(-14)).endTime(dt.getHistoryDate(-8)).build().execute(visitor,true).getJSONArray("list");
            for (int i=0;i<shopList.size();i++){
                //判断返回值里有没有参数，没有则跳过，有则取值
                boolean contains = shopList.getJSONObject(i).containsKey("enter_number");
                boolean contains1 = shopList1.getJSONObject(i).containsKey("enter_number");
                if(contains&&contains1) {
                    String visit_percentage = shopList.getJSONObject(i).getString("visit_percentage");
                    String visit_percentage1 = shopList1.getJSONObject(i).getString("visit_percentage");
                    String visit_percentage_qoq = shopList.getJSONObject(i).getString("visit_percentage_qoq");
                    String name = shopList.getJSONObject(i).getString("name");
                    double port=Double.parseDouble(visit_percentage.substring(0,visit_percentage.length()-1));
                    double port1=Double.parseDouble(visit_percentage1.substring(0,visit_percentage1.length()-1));
                    //分母是0，没有环比
                    if(port1!=0.00){
                        double x = (port-port1)/port1;
                        Preconditions.checkArgument(
                                MallScenarioUtil.getPercentFormat(String.valueOf(x)).equals(visit_percentage_qoq),
                                "门店光顾率环比是"+MallScenarioUtil.getPercentFormat(String.valueOf(x))+"！=页面展示环比"+visit_percentage_qoq+"门店是"+name);
                    }else {//上上周期进店率0的，本周期没有环比
                        Preconditions.checkArgument("0.00%".equals(visit_percentage_qoq), "门店光顾率环比是"+"0.00%"+"！=页面展示环比"+visit_percentage_qoq+"门店是"+name);
                    }
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("门店光顾率环比=上周期-上上周期/上上周期");
    }

    @Test(description = "人均停留时长环比=上周期-上上周期/上上周期")
    public void mallShopDataCase7(){
        try {
            //获取门店列表页
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().execute(visitor,true).getJSONArray("list");
            JSONArray shopList1 = ShopPageScene.builder().page(page).size(size).startTime(dt.getHistoryDate(-14)).endTime(dt.getHistoryDate(-8)).build().execute(visitor,true).getJSONArray("list");
            for (int i=0;i<shopList.size();i++){
                //判断返回值里有没有参数，没有则跳过，有则取值
                boolean contains = shopList.getJSONObject(i).containsKey("enter_number");
                boolean contains1 = shopList1.getJSONObject(i).containsKey("enter_number");
                if(contains&&contains1) {
                    int stay_time_avg = shopList.getJSONObject(i).getInteger("stay_time_avg");
                    int stay_time_avg1 = shopList1.getJSONObject(i).getInteger("stay_time_avg");
                    String stay_time_avg_qoq = shopList.getJSONObject(i).getString("stay_time_avg_qoq");
                    String name = shopList.getJSONObject(i).getString("name");
                    double port=Double.parseDouble(String.valueOf(stay_time_avg));
                    double port1=Double.parseDouble(String.valueOf(stay_time_avg1));
                    //分母是0，没有环比
                    if(port1!=0.00){
                        double x = (port-port1)/port1;
                        System.err.println(x);
                        Preconditions.checkArgument(
                                MallScenarioUtil.getPercentFormat(String.valueOf(x)).equals(stay_time_avg_qoq),
                                "门店光顾率环比是"+MallScenarioUtil.getPercentFormat(String.valueOf(x))+"！=页面展示环比"+stay_time_avg_qoq+"门店是"+name);
                    }else {//上上周期进店率0的，本周期没有环比
                        Preconditions.checkArgument("0.00%".equals(stay_time_avg_qoq), "门店光顾率环比是"+"0.00%"+"！=页面展示环比"+stay_time_avg_qoq+"门店是"+name);
                    }
                }
            }
        }catch(AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("人均停留时长环比=上周期-上上周期/上上周期");
    }

    public JSONArray mallMethod() {
        visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
//            获取所有权限id,因为日常购物中心特殊，所以只能固定parentRoleid
        JSONArray childrenList = AuthTreeScene.builder().parentRole(10107).build().execute(visitor,true).getJSONArray("children");
        JSONArray authList = new JSONArray();
        for (int i=0;i<childrenList.size();i++){
            JSONArray childlist = childrenList.getJSONObject(i).getJSONArray("children");
            for (int j=0;j<childlist.size();j++){
                int value = childlist.getJSONObject(j).getInteger("value");
                authList.add(j,value);
            }
        }
        return authList;
    }
}


