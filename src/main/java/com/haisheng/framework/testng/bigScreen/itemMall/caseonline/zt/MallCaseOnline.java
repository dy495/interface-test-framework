package com.haisheng.framework.testng.bigScreen.itemMall.caseonline.zt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.AuthTreeScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.role.*;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.staff.StaffAddScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.staff.StaffDeleteScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.staff.StaffEditScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.LoginPcMall;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.file.FileUploadScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview.OverviewShopOverviewScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.shop.CustomerTrendScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.shop.ShopDetailScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.shop.ShopPageScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.shop.ShopFloorListScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.util.LoginUntil;
import com.haisheng.framework.testng.bigScreen.itemMall.common.util.MallScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.auth.staff.StaffPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;

public class MallCaseOnline extends TestCaseCommon implements TestCaseStd {
    private final EnumTestProduct product = EnumTestProduct.MALL_ONLINE;
    private static final AccountEnum ACCOUNT = AccountEnum.MALL_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(product);
    public LoginUntil util = new LoginUntil(visitor);
    public static final int page = 1;
    public static final int size = 100;
    private final static String FILEPATH = "src/main/java/com/haisheng/framework/testng/bigScreen/itemMall/common/pic/人脸.jpg";
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
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        util.loginPc(ACCOUNT);
        logger.debug("case: " + caseResult);
        logger.logCaseStart(caseResult.getCaseName());

    }

    /*
       --------------------------------------------------------------购物中心门店客流总览----------------------------------------------------------------
                                                                                                                                                    */
    @Test(description = "角色管理-通过筛选框筛选角色")
    public void mallShopSystemCase1(){
        logger.logCase(caseResult.getCaseName());
        try{
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            JSONArray list = RolePageScene.builder().page(page).size(10).build().visitor(visitor).execute().getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String name = list.getJSONObject(i).getString("name");
                JSONArray list_size = RolePageScene.builder().name(name).page(page).size(10).build().visitor(visitor).execute().getJSONArray("list");
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

    @Test(description = "角色管理-添加角色--编辑角色--删除角色")//ok
    public void mallShopSystemCase2(){
        logger.logCase(caseResult.getCaseName());
        try{
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            String mess = RoleAddScene.builder().name("自动化添加角色").description(EnumDesc.DESC_BETWEEN_5_10.getDesc()).authList(mallMethod()).parentRoleId(Integer.parseInt(product.getRoleId())).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess.equals("success"),"创建角色失败"+mess);
            JSONArray list_size = RolePageScene.builder().name("自动化添加角色").page(page).size(10).build().visitor(visitor).execute().getJSONArray("list");
            int id = list_size.getJSONObject(0).getInteger("id");
            String message = RoleEditScene.builder().name("自动化编辑角色").description(EnumDesc.DESC_BETWEEN_20_30.getDesc()).authList(mallMethod()).id(id).parentRoleId(Integer.parseInt(product.getRoleId())).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(message.equals("success"),"编辑角色失败"+message);
            String mes = RoleDeleteScene.builder().id(id).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mes.equals("success"),"删除角色失败"+message);
        }
        catch (AssertionError |Exception e){
            appendFailReason(e.toString());
        }
        saveData("角色管理-添加角色--编辑角色--删除角色");
    }

    @Test(description = "账号管理-筛选框")//ok
    public void mallShopSystemCase3(){
        logger.logCase(caseResult.getCaseName());
        try{
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            JSONArray staff_list = StaffPageScene.builder().page(page).size(size).build().visitor(visitor).execute().getJSONArray("list");
            for (int i=0;i<staff_list.size();i++){
                String name = staff_list.getJSONObject(i).getString("name");
                String phone = staff_list.getJSONObject(i).getString("phone");
                Integer role_id = staff_list.getJSONObject(i).getJSONArray("role_list").getJSONObject(0).getInteger("role_id");
                String role_name = staff_list.getJSONObject(i).getJSONArray("role_list").getJSONObject(0).getString("role_name");
                Long shop_id = staff_list.getJSONObject(i).getJSONArray("shop_list").getJSONObject(0).getLong("shop_id");
                String shopname = staff_list.getJSONObject(i).getJSONArray("shop_list").getJSONObject(0).getString("shop_name");
                JSONArray list = StaffPageScene.builder()
                        .name(name)
                        .phone(phone)
                        .shopId(shop_id)
                        .roleId(role_id)
                        .page(page)
                        .size(size)
                        .build().visitor(visitor).execute().getJSONArray("list");
                if(list.size()!=0){
                    for (int j=0;j<list.size();j++) {
                        String name1 = list.getJSONObject(j).getString("name");
                        String phone1 = list.getJSONObject(j).getString("phone");
                        Integer role_id1 = list.getJSONObject(j).getJSONArray("role_list").getJSONObject(0).getInteger("role_id");
                        Long shop_id1 = list.getJSONObject(j).getJSONArray("shop_list").getJSONObject(0).getLong("shop_id");
                        String role_name1 = list.getJSONObject(j).getJSONArray("role_list").getJSONObject(0).getString("role_name");
                        String shopname1 = list.getJSONObject(j).getJSONArray("shop_list").getJSONObject(0).getString("shop_name");
                        Preconditions.checkArgument(name.equals(name1),"筛选框输入"+name+"列表返回"+name1);
                        Preconditions.checkArgument(phone.equals(phone1),"筛选框输入"+phone+"列表返回"+phone1);
                        Preconditions.checkArgument(role_id.equals(role_id1),"筛选框输入"+role_name+"列表返回"+role_name1);
                        Preconditions.checkArgument(shop_id1.equals(shop_id),"筛选框输入"+shopname+"列表返回"+shopname1);
                    }
                }
            }
        }
        catch (AssertionError |Exception e){
            appendFailReason(e.toString());
        }
        saveData("账号管理-筛选框");
    }

    @Test(description = "账号管理-新建-编辑-状态-删除")//ok
    public void mallShopSystemCase4(){
        logger.logCase(caseResult.getCaseName());
        try{
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            String pic_path = getPicPath(FILEPATH,"1:1");
            String phone = "136" + CommonUtil.getRandom(8);
            JSONArray role_list = new JSONArray();
            JSONObject role = new JSONObject();
            role.put("role_id",product.getRoleId());
            role.put("role_name","购物中心管理员");
            role_list.add(role);
            JSONArray shop_list = new JSONArray();
            JSONObject shop = new JSONObject();
            shop.put("shop_id",product.getShopId());
            shop.put("shop_name","购物中心");
            shop_list.add(shop);
            role.put("shop_list",shop_list);
            String mess = StaffAddScene.builder().name("自动化创建账号").phone(phone).roleList(role_list).picturePath(pic_path).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess.equals("success"),"创建账号"+mess);
            String id = StaffPageScene.builder().name("自动化创建账号").page(page).size(size).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getString("id");
//
            String staMess = StatusChangeScene.builder().id(id).status("DISABLE").build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(staMess.equals("success"),"关闭账号"+staMess);
            String startMess = StatusChangeScene.builder().id(id).status("ENABLE").build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(startMess.equals("success"),"开启账号"+staMess);
            String editMess = StaffEditScene.builder().name("自动化创建角色2").phone(phone).roleList(role_list).picturePath(pic_path).id(id).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(editMess.equals("success"),"编辑账号"+editMess);
            String delMess = StaffDeleteScene.builder().id(id).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(delMess.equals("success"),"删除账号"+delMess);
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("账号管理-新建-编辑-状态-删除");
    }

    @Test(description = "新增账号异常情况-手机号账号名称")
    public void mallShopSystemCase5(){
        logger.logCase(caseResult.getCaseName());
        try{
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            String pic_path = getPicPath(FILEPATH,"1:1");
            String phone = "136" + CommonUtil.getRandom(8);
            JSONArray role_list = new JSONArray();
            JSONObject role = new JSONObject();
            role.put("role_id",product.getRoleId());
            role.put("role_name","购物中心管理员");
            role_list.add(role);
            JSONArray shop_list = new JSONArray();
            JSONObject shop = new JSONObject();
            shop.put("shop_id",product.getShopId());
            shop.put("shop_name","购物中心");
            shop_list.add(shop);
            role.put("shop_list",shop_list);
            String mess = StaffAddScene.builder().name("自动化创建账号").phone("18888888488").roleList(role_list).picturePath(pic_path).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess.equals("创建账号失败当前手机号已被占用"),"重复手机号也创建成功"+mess);

            String mess1 = StaffAddScene.builder().name("自动化创建账号").phone("18888888488！！").roleList(role_list).picturePath(pic_path).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess1.equals("手机号格式不正确请重新输入"),"不规则11位手机号也创建成功"+mess1);

            String mess2 = StaffAddScene.builder().name("自动化创建账号").phone("1888888848").roleList(role_list).picturePath(pic_path).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess2.equals("手机号格式不正确请重新输入"),"不规则位手机号也创建成功"+mess2);

            String mess3 = StaffAddScene.builder().name("自动化创建账号").phone("188888884888").roleList(role_list).picturePath(pic_path).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess3.equals("手机号格式不正确请重新输入"),"不规则位手机号也创建成功"+mess3);

            String mess4 = StaffAddScene.builder().name(EnumDesc.DESC_BETWEEN_20_30.getDesc()).phone(phone).roleList(role_list).picturePath(pic_path).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess4.equals("名称需要在1-20个字内"),"账号名称大于20个字也创建成功"+mess4);

            String mess5 = StaffAddScene.builder().phone(phone).roleList(role_list).picturePath(pic_path).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess5.equals("名称不能为空"),"账号名称为空也创建成功"+mess5);

            String mess6 = StaffAddScene.builder().name(EnumDesc.DESC_BETWEEN_10_15.getDesc()).roleList(role_list).picturePath(pic_path).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess6.equals("手机号不能为空"),"手机号为空也创建成功"+mess6);
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("新增账号异常情况-手机号-账号名称");
    }

    @Test(description = "新增账号异常情况-账号角色")
    public void mallShopSystemCase6(){
        logger.logCase(caseResult.getCaseName());
        try{
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            String pic_path = getPicPath(FILEPATH,"1:1");
            String phone = "136" + CommonUtil.getRandom(8);
            JSONArray role_list = new JSONArray();
            JSONObject role = new JSONObject();
            role.put("role_id",product.getRoleId());
            role.put("role_name","购物中心管理员");
            JSONObject role1 = new JSONObject();
            role1.put("role_id",12609);
            role1.put("role_name","实时总览权限");
            JSONObject role2 = new JSONObject();
            role2.put("role_id",12668);
            role2.put("role_name","场馆客流总览");
            JSONObject role3 = new JSONObject();
            role3.put("role_id",12689);
            role3.put("role_name","楼层客流总览");
            JSONObject role4 = new JSONObject();
            role4.put("role_id",12690);
            role4.put("role_name","门店客流总览");
            JSONObject role5 = new JSONObject();
            role5.put("role_id",12691);
            role5.put("role_name","账号管理");
            role_list.add(role);
            role_list.add(role1);
            role_list.add(role2);
            role_list.add(role3);
            role_list.add(role4);
            role_list.add(role5);
            JSONArray shop_list = new JSONArray();
            JSONObject shop = new JSONObject();
            shop.put("shop_id",product.getShopId());
            shop.put("shop_name","购物中心");
            shop_list.add(shop);
            role.put("shop_list",shop_list);
            String mess = StaffAddScene.builder().name("自动化创建账号").phone(phone).roleList(role_list).picturePath(pic_path).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess.equals("必须添加1-5个角色"),"账号角色大于5个也创建成功了"+mess);
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("新增账号异常情况-账号角色");
    }

    @Test(description = "角色管理-添加角色异常情况")//ok
    public void mallShopSystemCase7(){
        logger.logCase(caseResult.getCaseName());
        try {
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            String mess = RoleAddScene.builder().name(EnumDesc.DESC_BETWEEN_20_30.getDesc()).description(EnumDesc.DESC_BETWEEN_5_10.getDesc()).authList(mallMethod()).parentRoleId(Integer.parseInt(product.getRoleId())).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess.equals("角色名称需要在1-20个字内"), "角色名称大于20个字也创建成功了" + mess);

            String mess1 = RoleAddScene.builder().name(EnumDesc.DESC_BETWEEN_5_10.getDesc()).description(EnumDesc.DESC_BETWEEN_500_1000.getDesc()).authList(mallMethod()).parentRoleId(Integer.parseInt(product.getRoleId())).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess1.equals("角色名称需要在1-50个字内"), "角色权限大于50个字也创建成功了" + mess1);

            String mess2 = RoleAddScene.builder().description(EnumDesc.DESC_BETWEEN_5_10.getDesc()).authList(mallMethod()).parentRoleId(Integer.parseInt(product.getRoleId())).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess2.equals("角色名称不能为空"), "角色名称为空也创建成功了" + mess2);

            String mess3 = RoleAddScene.builder().name(EnumDesc.DESC_BETWEEN_5_10.getDesc()).authList(mallMethod()).parentRoleId(Integer.parseInt(product.getRoleId())).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess3.equals("角色描述不能为空"), "角色描述为空也创建成功了" + mess3);

            String mess4 = RoleAddScene.builder().name(EnumDesc.DESC_BETWEEN_5_10.getDesc()).description(EnumDesc.DESC_BETWEEN_10_15.getDesc()).parentRoleId(Integer.parseInt(product.getRoleId())).build().visitor(visitor).getResponse().getMessage();
            Preconditions.checkArgument(mess4.equals("新增角色失败角色权限项不能为空"), "权限为空也创建成功了" + mess4);
        }
        catch (AssertionError |Exception e){
            appendFailReason(e.toString());
        }
        saveData("角色管理-添加角色-异常情况");
    }

    @Test(description = "通过楼层筛选")
    public void mallShopSystemCase8() {
        logger.logCase(caseResult.getCaseName());
        try {
            //获取楼层list
            visitor.setProduct(EnumTestProduct.MALL_ONLINE);
            JSONArray floorlist = ShopFloorListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
            //获取楼层id
            for(int i = 0; i<floorlist.size();i++){
                Long floorId = floorlist.getJSONObject(i).getLong("id");
                String floorname = floorlist.getJSONObject(i).getString("name");
                JSONArray shoplist = ShopPageScene.builder().size(size).page(page).floorId(floorId).startTime(starting).endTime(ending).build().visitor(visitor).execute().getJSONArray("list");
                //门店楼层
                for (int j = 0; j<shoplist.size(); j++){
                    Long shopid = shoplist.getJSONObject(j).getLong("id");
                    String floor_name = ShopDetailScene.builder().shopId(shopid).startTime(starting).endTime(ending).build().visitor(visitor).execute().getString("floor_name");
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
            Integer total = ShopPageScene.builder().size(size).page(page).startTime(starting).endTime(ending).build().visitor(visitor).execute().getInteger("total");
            //目前日常环境展示了全店区域的，所有需要-1,
            JSONArray floorlist = ShopFloorListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
            int count = 0;
            for (int i=0; i<floorlist.size();i++){
                Long floorId = floorlist.getJSONObject(i).getLong("id");
                //获取门店数量
                JSONArray shoplist = ShopPageScene.builder().size(size).page(page).floorId(floorId).startTime(starting).endTime(ending).build().visitor(visitor).execute().getJSONArray("list");
                count +=shoplist.size();
            }
            Preconditions.checkArgument(total==count,"楼层门店数之和"+count+"！=列表门店之和"+total);
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("各楼层门店数量之和==列表门店之和");
    }

    @Test(description = "列表页门店数据==门店详情页门店数据")
    public void mallShopDataCase2(){
        try{
            //获取门店列表页
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().visitor(visitor).execute().getJSONArray("list");
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
                    JSONObject entry_overview = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().visitor(visitor).execute().getJSONObject("entry_overview");
                    //客户进店率相关
                    JSONObject entry_percentage_overview = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().visitor(visitor).execute().getJSONObject("entry_percentage_overview");
                    //人均停留时长相关
                    JSONObject stay_time_overview = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().visitor(visitor).execute().getJSONObject("stay_time_overview");
                    //客户光顾率相关
                    JSONObject visit_percentage_overview = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().visitor(visitor).execute().getJSONObject("visit_percentage_overview");
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
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().visitor(visitor).execute().getJSONArray("list");
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
                    JSONObject entry_overview = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().visitor(visitor).execute().getJSONObject("entry_overview");
                    //进店人数
                    int entry_num = entry_overview.getInteger("number");
                    JSONArray list = CustomerTrendScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().visitor(visitor).execute().getJSONArray("list");
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
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().visitor(visitor).execute().getJSONArray("list");
            for(int i=0; i<shopList.size();i++){
                Long shopId = shopList.getJSONObject(i).getLong("id");
                String name = shopList.getJSONObject(i).getString("name");
                JSONObject data = CustomerTrendScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().visitor(visitor).getResponse().getData();
                JSONObject data2 = CustomerTrendScene.builder().shopId(shopId).startTime(dt.getHistoryDate(-14)).endTime(dt.getHistoryDate(-8)).build().visitor(visitor).getResponse().getData();
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
                    String day_qoq = OverviewShopOverviewScene.builder().shopId(shopId).startTime(starting).endTime(ending).build().visitor(visitor).execute().getJSONObject("entry_overview").getString("day_qoq");
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
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().visitor(visitor).execute().getJSONArray("list");
            JSONArray shopList1 = ShopPageScene.builder().page(page).size(size).startTime(dt.getHistoryDate(-14)).endTime(dt.getHistoryDate(-8)).build().visitor(visitor).execute().getJSONArray("list");
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
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().visitor(visitor).execute().getJSONArray("list");
            JSONArray shopList1 = ShopPageScene.builder().page(page).size(size).startTime(dt.getHistoryDate(-14)).endTime(dt.getHistoryDate(-8)).build().visitor(visitor).execute().getJSONArray("list");
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
            JSONArray shopList = ShopPageScene.builder().page(page).size(size).endTime(ending).startTime(starting).build().visitor(visitor).execute().getJSONArray("list");
            JSONArray shopList1 = ShopPageScene.builder().page(page).size(size).startTime(dt.getHistoryDate(-14)).endTime(dt.getHistoryDate(-8)).build().visitor(visitor).execute().getJSONArray("list");
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

    @Test(description = "角色管理-添加角色--删除角色-列表+-1")//ok
    public void mallShopDataCase8(){
        logger.logCase(caseResult.getCaseName());
        try{
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            //新建前角色页得角色数据
            Integer total =RolePageScene.builder().page(page).size(size).build().visitor(visitor).execute().getInteger("total");
            JSONArray listAdd1 = RoleListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
            int listAddNum1 = listAdd1.size();
            RoleAddScene.builder().name("自动化添加角色").description(EnumDesc.DESC_BETWEEN_5_10.getDesc()).authList(mallMethod()).parentRoleId(Integer.parseInt(product.getRoleId())).build().visitor(visitor).execute();
            Integer total1 =RolePageScene.builder().page(page).size(size).build().visitor(visitor).execute().getInteger("total");
            int a = total1-total;
            Preconditions.checkArgument(a==1, "新增一个角色后-角色页实际添加了"+a);
            //账号管理页角色下拉框数量+1
            JSONArray listAdd2 = RoleListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
            int listAddNum2 = listAdd2.size();
            int addNum = listAddNum2-listAddNum1;
            Preconditions.checkArgument(addNum==1, "新增一个角色后-账号角色下拉框实际添加了"+addNum);

            //删除后角色的角色数量
            int id = RolePageScene.builder().page(page).size(size).name("自动化添加角色").build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getInteger("id");
            RoleDeleteScene.builder().id(id).build().visitor(visitor).execute();
            Integer total2 =RolePageScene.builder().page(page).size(size).build().visitor(visitor).execute().getInteger("total");
            int b = total1-total2;
            Preconditions.checkArgument(b==1, "删除一个角色后-角色页实际减少了"+b);

            JSONArray listDelete = RoleListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
            int delNum = listAddNum2-listDelete.size();
            Preconditions.checkArgument(delNum==1, "删除一个角色后-账号页角色下拉框实际减少了"+delNum);
        }
        catch (AssertionError |Exception e){
            appendFailReason(e.toString());
        }
        saveData("角色管理-添加角色--删除角色-列表+-1");
    }

    @Test(description = "账号管理-新建-编辑-状态-删除--列表+-1")//ok
    public void mallShopDataCase9(){
        logger.logCase(caseResult.getCaseName());
        try{
            visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
            String pic_path = getPicPath(FILEPATH,"1:1");
            String phone = "136" + CommonUtil.getRandom(8);
            JSONArray role_list = new JSONArray();
            JSONObject role = new JSONObject();
            role.put("role_id",product.getRoleId());
            role.put("role_name","购物中心管理员");
            role_list.add(role);
            JSONArray shop_list = new JSONArray();
            JSONObject shop = new JSONObject();
            shop.put("shop_id",product.getShopId());
            shop.put("shop_name","购物中心");
            shop_list.add(shop);
            role.put("shop_list",shop_list);
            //添加前账号管理页列表数量
            int total0 = StaffPageScene.builder().page(page).size(size).build().visitor(visitor).execute().getInteger("total");
            int num0 = RolePageScene.builder().page(page).size(size).name("购物中心管理员").build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getInteger("num");
            StaffAddScene.builder().name("自动化创建账号").phone(phone).roleList(role_list).picturePath(pic_path).build().visitor(visitor).execute();
            int total1 = StaffPageScene.builder().page(page).size(size).build().visitor(visitor).execute().getInteger("total");
            int a = total1-total0;
            Preconditions.checkArgument(a==1,"新建一个账号后列表实际添加了"+a);
            //角色管理此角色增加了1
            int num1 = RolePageScene.builder().page(page).size(size).name("购物中心管理员").build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getInteger("num");
            int n = num1-num0;
            Preconditions.checkArgument(n==1,"新建一个此角色后列表实际添加了"+n);
            //获取要删除账号的id
            String id = StaffPageScene.builder().name("自动化创建账号").page(page).size(size).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getString("id");
            //改变状态-账号列表账号数量不变
            StatusChangeScene.builder().id(id).status("DISABLE").build().visitor(visitor).execute();
            int sDis = StaffPageScene.builder().page(page).size(size).build().visitor(visitor).execute().getInteger("total");
            int b = sDis-total1;
            Preconditions.checkArgument(b==0,"关闭账号状态列表实际改了变数量"+b);

            StatusChangeScene.builder().id(id).status("ENABLE").build().visitor(visitor).execute();
            int sEna = StaffPageScene.builder().page(page).size(size).build().visitor(visitor).execute().getInteger("total");
            int c = sEna-total1;
            Preconditions.checkArgument(c==0,"开启账号状态列表实际改了变数量"+c);

            StaffEditScene.builder().name("自动化创建角色2").phone(phone).roleList(role_list).picturePath(pic_path).id(id).build().visitor(visitor).execute();
            int edNum = StaffPageScene.builder().page(page).size(size).build().visitor(visitor).execute().getInteger("total");
            int d = edNum-total1;
            Preconditions.checkArgument(d==0,"编辑账号列表实际改变了数量"+d);

            StaffDeleteScene.builder().id(id).build().visitor(visitor).execute();
            int delNum = StaffPageScene.builder().page(page).size(size).build().visitor(visitor).execute().getInteger("total");
            int e = total1-delNum;
            Preconditions.checkArgument(e==1,"删除一个账号，账号页实际减少了"+e);
            int num2 = RolePageScene.builder().page(page).size(size).name("购物中心管理员").build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getInteger("num");
            int m = num1-num2;
            Preconditions.checkArgument(m==1,"删除一个账号，此角色账号页实际减少了"+m);
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }
        saveData("账号管理-新建-编辑-状态-删除--列表+-1");
    }

    public JSONArray mallMethod() {
        visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
//            获取所有权限id,因为日常购物中心特殊，所以只能固定parentRoleid
        JSONArray childrenList = AuthTreeScene.builder().parentRole(Integer.parseInt(product.getRoleId())).build().visitor(visitor).execute().getJSONArray("children");
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
    public String getPicPath(String picPath, String ratioStr) {
        visitor.setProduct(EnumTestProduct.MALL_ONLINE_SSO);
        String picture = new ImageUtil().getImageBinary(picPath);
        String[] strings = ratioStr.split(":");
        double ratio = BigDecimal.valueOf(Double.parseDouble(strings[0]) / Double.parseDouble(strings[1])).divide(new BigDecimal(1), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return FileUploadScene.builder().permanentPicType(0).pic(picture).ratioStr(ratioStr).ratio(ratio).build().visitor(visitor).execute().getString("pic_path");
    }
}


