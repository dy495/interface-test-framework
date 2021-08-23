package com.haisheng.framework.testng.bigScreen.jiaochen.xmf.pc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemPorsche.common.util.commonDs.JsonPathUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.Constant;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.manage.AppointmentMaintainConfigDetailBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.role.RolePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletEvaluateSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage.EvaluateConfigSubmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.role.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelEditScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shopstylemodel.ManageModelPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffAddScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff.StaffPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.DataAbnormal;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.JcFunction;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.SelectReception;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.PcAppointmentConfig;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

public class JcPc extends TestCaseCommon implements TestCaseStd {

    private static final EnumTestProduct PRODUCT = EnumTestProduct.JC_DAILY_ZH;
    private static final EnumAccount ACCOUNT = EnumAccount.JC_DAILY_LXQ;
    private final VisitorProxy visitor = new VisitorProxy(PRODUCT);
    private final SceneUtil util = new SceneUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();
    ScenarioUtil jc = ScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    PublicParm pp = new PublicParm();
    JcFunction pf = new JcFunction(visitor, pp);

    public int page = 1;
    public int size = 10;
    public String name = "创建角色xia";
    public String email = "";
    public String phone = "";


    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.XMF.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(ACCOUNT.getShopId()).setReferer(PRODUCT.getReferer()).setRoleId(ACCOUNT.getRoleId()).setProduct(PRODUCT.getAbbreviation());
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
        util.loginPc(ACCOUNT);

    }

    //app登录
    public void appLogin(String username, String password, String roleId) {
        String path = "/account-platform/login-m-app";
        JSONObject object = new JSONObject();
        object.put("phone", username);
        object.put("verification_code", password);
        commonConfig.setRoleId(roleId);
        httpPost(EnumTestProduct.JC_DAILY_ZH.getIp(), path, object);
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

    /**
     * ====================新增角色======================
     */
    @Test(description = "角色的CURD,数据校验")
    public void role_manager_1() {
        Integer id = null;
        try {
            String name = "创建角色xia";
            String description = "自动化测试给店长用的角色";
            IScene rolePageScene = RolePageScene.builder().build();
            Long total = rolePageScene.visitor(visitor).execute().getLong("total");
            //新增一个角色
            JSONObject response = RoleListScene.builder().build().visitor(visitor).execute();
            JSONObject obj = response.getJSONArray("list").getJSONObject(0);
            Long parentId = obj.getLong("id");
            JSONArray authList = obj.getJSONArray("auth_list");
            RoleAddScene.builder().name(name).description(description).authList(authList).parentRoleId(parentId).build().visitor(visitor).execute();
            response = rolePageScene.visitor(visitor).execute();
            Long addTotal = response.getLong("total");
            Preconditions.checkArgument(addTotal == total + 1, "添加角色前列表数：" + total + " 添加角色后列表数：" + addTotal);
            //编辑角色
            id = util.toJavaObject(rolePageScene, RolePageBean.class, "name", name).getId();
            Integer code = RoleEditScene.builder().parentRoleId(parentId).authList(authList).description(description).name(name + "-改").id(id).build().visitor(visitor).getResponse().getCode();
            checkArgument(code == 1000, "编辑角色的信息预期code：1000 实际code：" + code);
            String newName = util.toJavaObject(rolePageScene, RolePageBean.class, "id", id).getName();
            checkArgument(newName.equals(name + "-改"), "编辑角色后账号名称预期为：" + name + "-改" + " 实际为：" + newName);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            RoleDeleteScene.builder().id(id).build().visitor(visitor).execute();
            saveData("新增删改查角色");
        }
    }

    /**
     * ====================新增角色(名称校验)======================
     */
    @DataProvider(name = "ROLENAME")
    public static Object[][] rolename() {
        return new String[][]{
                {"这是一个二十字的角色名称是的是的是的", "角色名称为20个字，创建失败"},
                {"这是一个二十字的角色名称AABB1111", "角色名称为中文+字母+数字，创建失败"},
                {"这是一个二十字的角色名称AABB11.。", "角色名称为中文+字母+数字+字符，创建失败"},
        };
    }

    @Test(dataProvider = "ROLENAME")  //ok
    public void role_add_work2(String name, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray moduleId = pp.roleList;
            int totalB = jc.roleListFilterManage("", "1", "10", "", "").getInteger("total");

            String description = "自动化测试给店长自动化用的角色";
            JSONObject res = jc.organizationRoleAdd(name, description, moduleId);
            checkArgument(res.getInteger("code") == 1000, mess);
            int total = jc.roleListFilterManage("", "1", "10", "", "").getInteger("total");
//
            String id = jc.roleListFilterManage("", "" + "1", "10", "name", name).getJSONArray("list").getJSONObject(0).getString("id");
            jc.organizationidRoleDelete(id);
            Preconditions.checkArgument(total - totalB == 1, "新增角色列表没+1");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("轿辰-新增角色(名称校验)-正常");
        }
    }

    @DataProvider(name = "ROLENAMEAB")
    public static Object[][] rolenameab() {
        return new String[][]{
                {"这是一个二十一字的角色名称是的是的是的是的", "角色名称需要在1-20个字内", "角色名称需要在1-20个字内"},
                {"别删-仅卡劵申请tab", "新增角色失败当前角色名称已存在！请勿重复添加", "重复的角色名称，创建成功"},
        };
    }

    @Test(dataProvider = "ROLENAMEAB")  //ok
    public void role_add_workAb(String name, String res, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
             util.loginPc(ACCOUNT);
            JSONArray moduleId = pp.roleList;
            String description = "自动化测试给店长自动化用的角色";

            JSONObject res3 = jc.organizationRoleAdd(name, description, moduleId);
            checkArgument(res3.getString("message").equals(res), mess);


        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("轿辰-新增角色(名称异常校验)");
        }

    }


    /**
     * ====================账户管理中的一致性========================
     */
    @Test(description = "新增1个账号，列表+1；删除1个账号，列表-1；修改账号名称后与列表是否一致")    //ok
    public void accountInfoData() {
        try {
            Integer total = jc.pcStaffPage("", page, size).getInteger("total");

            JSONArray r_dList = pf.getroleLlist();
            JSONArray shop_list = new JSONArray();
            String phone = pf.genPhoneNum();
            //用EMAIL新增一个账号
            jc.organizationAccountAdd(name, phone, r_dList, shop_list);

            JSONArray accountList = jc.pcStaffPage(name, 1, 10).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("id");

            //新增账号以后，再查询列表
            Integer total1 = jc.pcStaffPage("", page, size).getInteger("total");
            int result = total1 - total;
            Preconditions.checkArgument(result == 1, "新增1个账号，账号列表的数量却加了：" + result);


            //编辑账号的名称，是否与列表该账号的一致
            String reName = "自动化测编辑";
            jc.organizationAccountEdit(account, reName, phone, r_dList, shop_list);
            JSONArray accountsList = jc.pcStaffPage(reName, page, size).getJSONArray("list");
            String name_1 = accountsList.getJSONObject(0).getString("name");
            Preconditions.checkArgument(name_1.equals(reName), "修改账号：" + account + "的名称为：" + reName + "修改后，该账号的名称为：" + name_1);


            //删除账号以后，再查询列表
            jc.organizationAccountDelete(account);

            Integer total2 = jc.pcStaffPage("", page, size).getInteger("total");
            Preconditions.checkArgument(total1 - total2 == 1, "删除1个账号，账号列表的数量却减了：" + (total1 - total2));

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {

            saveData("轿辰-新增1个账号，列表+1；删除1个账号，列表-1；修改账号信息以后与列表是否一致");
        }

    }

    @Test(description = "新增姓名异常")
    public void staff_manager_2() {
        try {
            String phone = util.getRandomPhone();
            String name = "123456789012345678901咿呀咿呀";
            IScene scene = StaffPageScene.builder().build();
            int total = scene.visitor(visitor).execute().getInteger("total");
            util.getRandomRoleMap().forEach((roleId, roleName) -> {
                JSONArray shopList = util.getShopIdArray();
                String picPath = util.uploadFile();
                Integer code = StaffAddScene.builder().phone(phone).name(name).shopList(shopList).roleId(roleId).roleName(roleName).picturePath(picPath).build().visitor(visitor).getResponse().getCode();
                Preconditions.checkArgument(code == 1001, "新增账户名称为：" + name + " 预期code为1000 实际为：" + code);
            });
            int newTotal = scene.visitor(visitor).execute().getInteger("total");
            Preconditions.checkArgument(newTotal == total, "新增账户前列表数为：" + total + " 新增账户后列表数为：" + newTotal);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新增姓名异常");
        }
    }

    @Test(description = "新增姓名电话异常", dataProvider = "ERR_PHONE", dataProviderClass = DataAbnormal.class)
    public void staff_manager_3(String phone) {
        try {
            String name = "创建角色xia";
            util.getRandomRoleMap().forEach((roleId, roleName) -> {
                JSONArray shopList = util.getShopIdArray();
                String picPath = util.uploadFile();
                Integer code = StaffAddScene.builder().phone(phone).name(name).shopList(shopList).roleId(roleId).roleName(roleName).picturePath(picPath).build().visitor(visitor).getResponse().getCode();
                Preconditions.checkArgument(code == 1001, "新增账户电话号为：" + phone + " 预期code为1000 实际为：" + code);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("新增姓名电话异常");
        }
    }

    @Test(description = "重复电话异常")    //ok
    public void accountAb3() {
        try {
            JSONArray r_dList = pf.getroleLlist();

            JSONArray shop_list = new JSONArray();
            String phone = pp.gwphone;
            //用EMAIL新增一个账号
            int code = jc.organizationAccountAdd(name, phone, r_dList, shop_list, false).getInteger("code");
            Preconditions.checkArgument(code == 1001, "新增账户，电话异常");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {

            saveData("轿辰-新增1个账号，列表+1；删除1个账号，列表-1；修改账号信息以后与列表是否一致");
        }

    }

    @Test(description = "角色6个异常")    //ok
    public void accountAb6() {
        try {
            JSONArray r_dList = new JSONArray();
            String[][] roleId = {
                    {"2874", "角色1（东东测试）"},
                    {"2875", "角色2（东东测试）"},
                    {"2847", "角色A"},
                    {"2889", "测试角色"},
                    {"2873", "个人-售前接待"},
                    {"2889", "测试角色A"},
            };
            for (String[] strings : roleId) {
                JSONObject roleList1 = pf.getRoleList1(strings[0], strings[1]);
                r_dList.add(roleList1);
            }
            JSONArray shop_list = new JSONArray();

            String phone = pf.genPhoneNum();
            //用EMAIL新增一个账号
            int code = jc.organizationAccountAdd(name, phone, r_dList, shop_list, false).getInteger("code");
            Preconditions.checkArgument(code == 1001, "新增账户，角色超过5个");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {

            saveData("轿辰-新增1个账号电话号码重复异常验证");
        }

    }

    @Test(description = "创建账号角色5个")    //ok
    public void account5() {
        try {
            JSONArray r_dList = new JSONArray();

            String[][] roleId = {
                    {"2874", "角色1（东东测试）"},
                    {"2875", "角色2（东东测试）"},
                    {"2847", "角色A"},
                    {"2889", "测试角色"},
                    {"2873", "个人-售前接待"},
            };
            for (String[] strings : roleId) {
                JSONObject roleList1 = pf.getRoleList1(strings[0], strings[1]);
                r_dList.add(roleList1);
            }
            JSONArray shop_list = new JSONArray();

            String phone = pf.genPhoneNum();
            //新增一个账号
            jc.organizationAccountAdd(name, phone, r_dList, shop_list, true);
            JSONArray accountList = jc.pcStaffPage(name, 1, 10).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("id");
            jc.organizationAccountDelete(account);

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("轿辰-新增1个账号角色5个");
        }
    }

    //    @Test(description = "创建账号必填项不填")    //角色列表和门店列表接口未作限制，前端作了限制
    public void account7() {
        try {
            JSONArray r_dList = new JSONArray();
            r_dList.add("417");
            r_dList.add("418");

            JSONArray shop_list = new JSONArray();
            shop_list.add("46012");
            String phone = pf.genPhoneNum();

            String[] emptyList = {"name",
                    "phone",
                    "role_list",
                    "shop_list"
            };
            for (String s : emptyList) {
                int code = jc.organizationAccountAdd2(name, phone, r_dList, shop_list, false, s).getInteger("code");//用EMAIL新增一个账号
                Preconditions.checkArgument(code == 1001, "新增账户必填项" + s + "不填仍成功");
            }

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {

            saveData("轿辰-创建账号必填项不填");
        }

    }


    /**
     * ====================账户管理中的一致性========================
     */
    @Test  //编辑账号信息以后，创建者和创建时间是否发生改变
    public void accountInfoData_1() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            Integer total = jc.pcStaffPage("", page, size).getInteger("total");

            JSONArray list = jc.pcStaffPage("", page, size).getJSONArray("list");
            String today = dt.getHistoryDate(0);
            String id = "";
            String old_phone = "";
            String name = "";
            String create_time = "";
            JSONArray r_dList = new JSONArray();
            JSONArray shop_list = new JSONArray();

            for (int i = 1; i < list.size(); i++) {
                create_time = list.getJSONObject(i).getString("create_time");
                if (!create_time.equals(today)) {
                    id = list.getJSONObject(i).getString("id");
                    name = list.getJSONObject(i).getString("name");
                    old_phone = list.getJSONObject(i).getString("phone");
                    r_dList = list.getJSONObject(i).getJSONArray("role_list");
                    shop_list = list.getJSONObject(i).getJSONArray("shop_list");
                    break;
                }
            }
//            JSONArray r_dList2=new JSONArray();
//            JSONArray shop_list2 = new JSONArray();
//            for(int i=0;i<r_dList.size();i++){
//               String rid=r_dList.getJSONObject(i).getString("role_id");
//               r_dList2.add(rid);
//            }
//            for(int i=0;i<shop_list.size();i++){
//                String rid=shop_list.getJSONObject(i).getString("shop_id");
//                shop_list2.add(rid);
//            }
            if (!old_phone.equals("")) {
                //编辑账号的名称，权限
                jc.organizationAccountEdit(id, name, old_phone, r_dList, shop_list);
                //获取列表该账号
                JSONArray accountList = jc.pcStaffPage(name, page, size).getJSONArray("list");
                String create_time_1 = "";
                String phone_1 = accountList.getJSONObject(0).getString("phone");//获取通过手机号搜索到的账号的手机号
                if (phone_1.equals(old_phone)) {
                    create_time_1 = accountList.getJSONObject(0).getString("create_time");
                }
                Preconditions.checkArgument(create_time_1.equals(create_time), "编辑昨天" + create_time + "的创建的账号" + old_phone + "列表该账号的创建时间变成了最新编辑的时间" + create_time_1);
                //编辑完以后获取列表的数量，是否有增多或者减少
                Integer total1 = jc.pcStaffPage(null, page, size).getInteger("total");
                Preconditions.checkArgument(total.equals(total1), "编辑一个账号，账号列表的数量由:" + total + "变成了" + total1);
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {

            saveData("轿辰-编辑账号信息以后，创建者和创建时间是否发生改变");
        }

    }

    //禁用账户登录失败，开启登录成功
    @Test
    public void accountStart() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray data = jc.staffListFilterManage(null, "1", "100", "name", "自动化专用账号").getJSONArray("list");
            String id = "";
            for (int i = 0; i < data.size(); i++) {
                String status = data.getJSONObject(i).getString("status");
                if (status.equals("ENABLE")) {
                    id = data.getJSONObject(i).getString("id");
                    break;
                } else {
                    throw new Exception("账户xx 被关闭了");
                }
            }

            //禁用开启按钮
            jc.organizationAccountButtom(id, "DISABLE");
            int codeApp = jc.appLogin2(pp.jdgw2, pp.jdgwpassword, false).getInteger("code");
            int codePc = jc.pcTryLogin(pp.jdgw2, pp.jdgwpassword, false).getInteger("code");

            jc.organizationAccountButtom(id, "ENABLE");
            int codePcAfter = jc.pcTryLogin(pp.jdgw2, pp.jdgwpassword, false).getInteger("code");

            Preconditions.checkArgument(codeApp == 1001, "账户禁用，app仍登录成功");
            Preconditions.checkArgument(codePc == 1001, "账户禁用，pc仍登录成功");
            Preconditions.checkArgument(codePcAfter == 1000, "账户启用，pc登录失败");


        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("轿辰-禁用账户登录失败，开启登录成功");
        }
    }

    //查询案例

    /**
     * @description :接待管理查询   ---for  liya
     * @date :2020/8/3 12:48
     **/
//    @Test(dataProvider = "SELECT_PARM", dataProviderClass = ScenarioUtil.class)
    public void receptionSelect(String parm, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String shopId = "-1";
            JSONObject data = jc.afterSleCustomerManage(shopId, "1", "10", "", "");

//            JSONObject data = jc.brandListFilterManage1("", "1","10","","");

            String Expetresult = data.getJSONArray("list").getJSONObject(0).getString(parm);
            JSONArray list = jc.receptionManage(shopId, "1", "10", parm, Expetresult).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String SelectResult = list.getJSONObject(i).getString(output);
                Preconditions.checkArgument((Expetresult.equals(SelectResult)), "接待管理按" + parm + "查询，结果错误" + SelectResult);
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("轿辰-接待管理查询，结果校验");
        }
    }

    //    @Test()
    public void selectAppointmentRecodeFilter() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Object[][] ss = Constant.receptionManageFilter_pram();
            SelectReception sr = new SelectReception();
            JSONArray res = jc.receptionManage(pp.shopId, "1", "10", "", "").getJSONArray("list");
            JSONObject data = res.getJSONObject(0);

            sr.plate_number = data.getString(ss[0][1].toString());

            sr.reception_sale_id = data.getString(ss[1][1].toString());
            sr.reception_date = data.getString(ss[2][1].toString());

            JSONObject result = jc.receptionManageC(sr);

            Preconditions.checkArgument(result.getString(ss[0][1].toString()).contains(sr.plate_number), "");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("轿辰-接待管理列表查询全填，结果校验");
        }
    }

    //    @Test(dataProvider = "SELECT_PreSleCustomerManageFilter",dataProviderClass = Constant.class)
    public void preSleCustomerManageOneFilter(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.preSleCustomerManage("", "1", "10", "", "");
            int pages = respon.getInteger("pages");
            String result = respon.getJSONArray("list").getJSONObject(0).getString(pram);
            for (int page = 1; page <= pages; page++) {
                JSONArray list = jc.preSleCustomerManage("", String.valueOf(page), "10", pram, result).getJSONArray("list");
                for (int i = 0; i < 10; i++) {
                    String Flag = list.getJSONObject(i).getString(output);
                    Preconditions.checkArgument(Flag.contains(result), "销售管理按" + result + "查询，结果错误" + Flag);
                }
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("销售客户查询单项查询，结果校验");
        }
    }

    /**
     * @description :开始接待接口车牌号异常验证
     * @date :2020/12/15 17:47
     **/
    @Test(description = "pc接待车牌号验证", dataProvider = "PLATE", dataProviderClass = ScenarioUtil.class)
    public void pcReceiptAb(String plate) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = jc.pcManageReception(plate, false).getInteger("code");
            Preconditions.checkArgument(code == 1001, "异常车牌号依然成功");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc接待车牌号验证");
        }
    }

    @Test(description = "pc接待搜索老车牌号展示项验证")
    public void pcReceipt() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            JSONObject data = jc.pcManageReception(pp.carplate, true);
            String jsonpath = "$.arrive_times&&$.customers[*].voucher_list[*]&&$.er_code_url&&$.last_reception_sale_name&&$.last_arrive_time&&$.plate_number";
            JsonPathUtil.spiltString(data.toJSONString(), jsonpath);

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc接待搜索老车牌号展示项验证");
        }
    }

    //pc-取消接待
    @Test()
    public void pcCancleReception() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            int appTask = pf.appReceptionPage();
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            //取消接待
            JSONObject dd = jc.receptionManage("", "1", "10", "", "").getJSONArray("list").getJSONObject(0);
            long receptionID = dd.getLong("id");
            long shopId = dd.getLong("shop_id");
            String reception_status_name = dd.getString("reception_status_name");
            if (!reception_status_name.equals("接待中")) {
                logger.warn("暂无接待中客户");
                return;
            }
            jc.pcCancelReception(receptionID, shopId);
            appLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            int appTaskA = pf.appReceptionPage();
            Preconditions.checkArgument(appTask - appTaskA == 1, "pc取消接待，app接待任务没-1");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
             util.loginPc(ACCOUNT);
            saveData("pc取消接待，app任务-1");
        }
    }

    //***************pc保养配置相关********************

    @DataProvider(name = "TYPE")
    public static Object[] type() {
        return new String[]{
                "MAINTAIN",
                "REPAIR"
        };
    }

    @Test(enabled = false) //pc修改车预约价格，小程序对应变更
    public void pcmaintainPriceEdit() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = "MAINTAIN";
            int num = 0;
            String dataType = "WEEKDAY";
            pcLogin(pp.jdgw, pp.gwpassword, pp.roleidJdgw);
            //判断今天是否是周末，周末就取周末的折扣配置
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, num);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dataType = "WEEKEND";
            }
            Double price = 300.0;
            IScene manageModelEdit = ManageModelEditScene.builder().price(price).id(Long.parseLong(pp.modolIdAppointment)).type(type).status("ENABLE").build();
            //修改预约价格
            jc.invokeApi(manageModelEdit);

            //工位配置里的折扣
            JSONObject timeRangeDetail = jc.timeRangeDetail(type, dataType);

            JSONArray morning = timeRangeDetail.getJSONObject("morning").getJSONArray("list");
            JSONArray afternoon = timeRangeDetail.getJSONObject("afternoon").getJSONArray("list");
            Double[] discount = new Double[morning.size() + afternoon.size()];

            for (int i = 0; i < morning.size(); i++) {
                discount[i] = morning.getJSONObject(i).getDouble("discount");
            }
            for (int j = 0; j < afternoon.size(); j++) {
                discount[morning.size() + j] = afternoon.getJSONObject(j).getDouble("discount");
            }

            //小程序这个车预约的价格
            jc.appletLoginToken(pp.appletTocken);
            JSONArray appletTime = jc.appletmaintainTimeList(Long.parseLong(pp.shopIdZ), pp.car_idA, dt.getHistoryDate(num), type).getJSONArray("list");
            Preconditions.checkArgument(discount.length == appletTime.size(), "pc配置的预约时间段与小程序展示不一致");
            for (int z = 0; z < appletTime.size(); z++) {
                String priceApplet = appletTime.getJSONObject(z).getString("price");
                Double result = price * discount[z];
                String pp = priceApplet.substring(1);
                Double pa = Double.valueOf(pp);
                Preconditions.checkArgument(result.equals(pa), "预约价格异常" + result + ":" + pa);
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置验证");
        }
    }

    @Test(dataProvider = "APPOINTMENTTYPE")  //pc修改车预约价格，价格格式异常判断
    public void pcmaintainPriceEditAb(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] price = {"", "qwe", "我", "!@#", "100000000.011"};   //保养价格为空，保养价格异常格式
            //修改预约价格
            for (String s : price) {
                int code = jc.pcCarModelPriceEdit(pp.modolIdAppointment, s, null, false, type).getInteger("code");
                System.out.println("code:" + code);
            }

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {

            saveData("pc修改预约配置价格格式验证");
        }
    }

    @Test()  //pc修改车预约价格，列表价格变更，其他不变更
    public void pcmaintainPriceEdit2() {
        String type = "MAINTAIN";
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            String price = "300.0";
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 0);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                price = "200.0";
            }
            jc.pcCarModelPriceEdit(pp.modolIdAppointment, price, null, true, type);
            IScene modelpage = ManageModelPageScene.builder().page(1).type(type).size(10).carModel(pp.carModel).build();

            String priceAfter = jc.invokeApi(modelpage).getJSONArray("list").getJSONObject(0).getString("price");
            Preconditions.checkArgument(priceAfter.equals(price), "编辑后价格与入参不一致");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc修改预约配置价格，列表验证");
        }
    }

    @Test(dataProvider = "APPOINTMENTTYPE")  //pc开关【保养配置】-预约按钮，小程序对应变更
    public void pcappointmentButton(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene modelPage = ManageModelPageScene.builder().page(1).size(10).type(type).carModel(pp.carModel).build();
            JSONObject data = jc.invokeApi(modelPage).getJSONArray("list").getJSONObject(0);
            String status = data.getString("status");
            if (!status.equals("ENABLE")) {
                throw new Exception("车型,预约配置被关闭");
            }
            //  public JSONObject pcCarModelPriceEdit(String id, String price, String status, Boolean checkcode,String  type) {
            jc.pcCarModelPriceEdit(pp.modolIdAppointment, null, "DISABLE", true, type);

            jc.appletLoginToken(pp.appletTocken);
            JSONObject isAble = jc.appletmaintainTimeList(Long.parseLong(pp.shopIdZ), pp.car_idA, dt.getHistoryDate(1), type, false);
            int code = isAble.getInteger("code");
            String message = isAble.getString("message");

            jc.pcLogin(pp.gwphone, pp.gwpassword);
            jc.pcCarModelPriceEdit(pp.modolIdAppointment, null, "ENABLE", true, type);


            Preconditions.checkArgument(code != 1000, "预约配置关闭小程序预约保养页返回" + message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置验证");
        }
    }

    @Test(dataProvider = "APPOINTMENTTYPE") //pc开关【保养配置】-预约按钮，保养配置列表数量无变化
    public void pcappointmentButton2(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene modelPage = ManageModelPageScene.builder().page(1).size(10).type(type).carModel(pp.carModel).build();
            JSONObject data = jc.invokeApi(modelPage).getJSONArray("list").getJSONObject(0);
            String status = data.getString("status");
            if (!status.equals("ENABLE")) {
                throw new Exception("车型-赢识-xia,预约配置被关闭");
            }
            IScene modelPage2 = ManageModelPageScene.builder().page(1).size(10).type(type).build();
            int total = jc.invokeApi(modelPage2).getInteger("total");

            jc.pcCarModelPriceEdit(pp.modolIdAppointment, null, "DISABLE", true, type);
            IScene modelPage3 = ManageModelPageScene.builder().page(1).size(10).type(type).build();
            int totalAfter = jc.invokeApi(modelPage3).getInteger("total");

            jc.pcCarModelPriceEdit(pp.modolIdAppointment, null, "ENABLE", true, type);
            IScene modelPage4 = ManageModelPageScene.builder().page(1).size(10).type(type).build();
            int totalAfter2 = jc.invokeApi(modelPage4).getInteger("total");

            Preconditions.checkArgument(total == totalAfter, "关闭后列表数量变化");
            Preconditions.checkArgument(totalAfter == totalAfter2, "开启预约配置后列表数量变化");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc开关预约配置按钮，预约配置列表数量有无变化");
        }
    }
    //***********pc预约配置************************

    @Test(dataProvider = "APPOINTMENTTYPE")  //pc修改工位，工作日和休息日不同步变更
    public void pcmaintainTableEdit(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            pcLogin(pp.jdgw, pp.gwpassword, pp.roleidJdgw);
            //修改配置前，工作日的配置
            JSONObject timeDetailWeekday = jc.timeRangeDetail(type, "WEEKDAY");
            JSONObject afternoonBefore = timeDetailWeekday.getJSONObject("afternoon");
            JSONObject morningBefore = timeDetailWeekday.getJSONObject("morning");

            //修改休息日的配置
            JSONObject timeDetailWeekend = jc.timeRangeDetail(type, "WEEKEND");
            JSONObject morning1 = timeDetailWeekend.getJSONObject("morning");
            JSONObject afternoon1 = timeDetailWeekend.getJSONObject("afternoon");

            JSONObject morning = JSONObject.parseObject(morning1.toJSONString());

            if (day % 2 == 1) {
                morning.put("reply_start", "09:00");  //奇数天 设置9：00
            } else {
                morning.put("reply_start", "08:00");
            }
            afternoon1.put("title", "下午时段");

            timeDetailWeekend.put("morning", morning);
            timeDetailWeekend.put("afternoon", afternoon1);
            timeDetailWeekend.put("date_type", "WEEKEND");
            timeDetailWeekend.put("type", type);

            //修改休息日工位配置
            jc.appointmentTimeEdit(timeDetailWeekend);

            //修改之后的配置,工作日
            JSONObject timeDetailWeekdayAfter = jc.timeRangeDetail(type, "WEEKDAY");
            JSONObject afternoonAfter = timeDetailWeekdayAfter.getJSONObject("afternoon");
            JSONObject morningAfter = timeDetailWeekdayAfter.getJSONObject("morning");
            //休息日
            JSONObject timeDetailWeekendAfter = jc.timeRangeDetail(type, "WEEKEND");
            JSONObject afternoonAfterend = timeDetailWeekendAfter.getJSONObject("afternoon");
            JSONObject morningAfterend = timeDetailWeekendAfter.getJSONObject("morning");

            //工作日
            System.out.println(afternoonAfter.equals(afternoonBefore));  //true
            System.out.println(morningAfter.equals(morningBefore));  //true
            //休息日
            System.out.println(afternoonAfterend.equals(afternoon1));  //ture
            System.out.println(morningAfterend.equals(morning1));   //false

//            //工作日
            Preconditions.checkArgument(afternoonAfter.equals(afternoonBefore), "修改休息日上午工作配置，工作日下午看板变更了");
            Preconditions.checkArgument(morningAfter.equals(morningBefore), "修改休息日上午工作配置，工作日上午看板变更了");
//            //休息日
//            Preconditions.checkArgument(afternoonAfterend.equals(afternoon1),"修改休息日上午工作配置，休息日下午看板变更了");
//            Preconditions.checkArgument(morningAfterend.equals(morning1),"修改休息日上午工作配置，休息日上午看板没有变更了");   //false


        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改工位，工作日和休息日不同步变更");
        }
    }

    @Test(dataProvider = "APPOINTMENTTYPE")  //pc修改预约配置，正常
    public void pcmaintainTableEdit2(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            JSONArray voucher = new JSONArray();
            voucher.add("77");
            PcAppointmentConfig er = new PcAppointmentConfig();
            er.type = type;
            er.remind_time = "720";
            er.replay_time_limit = "720";
            er.appointment_interval = "12";  //TODO:需设置成12 ，目前有bug
//            er.on_time_reward=true;
//            er.is_send_voucher=true;
//            er.vouchers=voucher;
//            er.voucher_start=dt.getHistoryDate(0);
//            er.voucher_end=dt.getHistoryDate(10);
            jc.pcappointmentConfig(er);

            er.remind_time = "20";
            er.replay_time_limit = "30";
            er.appointment_interval = "1";
            jc.pcappointmentConfig(er);
            JSONObject config = jc.AppointmentMaintainConfigDetailScene(type);

            AppointmentMaintainConfigDetailBean as = JSONObject.toJavaObject(config, AppointmentMaintainConfigDetailBean.class);

            Preconditions.checkArgument(as.getAppointmentInterval() == 1, "间隔时间配置失败");
            Preconditions.checkArgument(as.getReplayTimeLimit() == 30, "超时时间配置失败");
            Preconditions.checkArgument(as.getRemindTime() == 20, "提醒时间配置失败");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置，正常");
        }
    }

    //    @Test  //pc修改预约配置，正常  限制使用天数超过2000  TODO:目前未作限制，bug修改后打开,2.0该功能取消
    public void pcmaintainTableEdit3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.pcLogin(pp.jdgw, pp.jdgwpassword);
            JSONArray voucher = new JSONArray();
            voucher.add("77");
            PcAppointmentConfig er = new PcAppointmentConfig();
            er.checkcode = false;
            er.type = "MAINTAIN";
            er.remind_time = "30";
            er.replay_time_limit = "20";
            er.appointment_interval = "1";
            er.on_time_reward = true;
            er.is_send_voucher = true;
            er.vouchers = voucher;
            er.voucher_effective_days = "20000";
            int code = jc.pcappointmentConfig(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "修改预约配置，卡券使用天数限制超过2000仍成功");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置卡券限制使用时间2000天验证");
        }
    }

    //    @Test  //pc修改预约配置,异常    TODO:接口未作限制，前端限制
    public void pcmaintainTableEditAb() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            jc.pcLogin(pp.jdgw, pp.jdgwpassword);
            JSONArray voucher = new JSONArray();
            voucher.add("77");
            PcAppointmentConfig er = new PcAppointmentConfig();
            er.checkcode = false;
            er.type = "MAINTAIN";
            er.remind_time = "721";
            er.replay_time_limit = "20";
            er.appointment_interval = "1";
//            er.on_time_reward=true;
//            er.is_send_voucher=true;
//            er.vouchers=voucher;
//            er.voucher_start=dt.getHistoryDate(0);
//            er.voucher_end=dt.getHistoryDate(10);
            int code = jc.pcappointmentConfig(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "修改预约配置【提醒时间】异常：" + er.remind_time);
            er.remind_time = "20";
            er.replay_time_limit = "721";
            er.appointment_interval = "1";
            int code2 = jc.pcappointmentConfig(er).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "修改预约配置【超时应答时间】异常：" + er.replay_time_limit);

            er.replay_time_limit = "20";
            er.appointment_interval = "13";
            int code3 = jc.pcappointmentConfig(er).getInteger("code");
            Preconditions.checkArgument(code3 == 1001, "修改预约配置【正常应答时间】异常：" + er.appointment_interval);
            er.appointment_interval = "1";
            int code4 = jc.pcappointmentConfig(er).getInteger("code");
            Preconditions.checkArgument(code4 == 1000, "修改预约配置异常");
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置，异常");
        }
    }

    @DataProvider(name = "APPOINTMENTTYPE")
    public static Object[] appointmenttype() {
        return new String[]{
                "MAINTAIN",
                "REPAIR",
        };
    }

    //***********pc门店按钮开关************
    @Test(dataProvider = "APPOINTMENTTYPE") //pc门店预约关闭，小程序对应变更
    public void pcShopAppointmentButton(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject data = jc.shopListFilterManage("", "1", "10", "name", pp.shopname).getJSONArray("list").getJSONObject(0);
            String status = data.getString("appointment_status");
            if (!status.equals("ENABLE")) {
                throw new Exception("中关村店,预约开关被关闭了");
            }
            //配置前预约门店配置列表
            jc.appletLoginToken(pp.appletTocken);
            JSONArray isAble = jc.appletmaintainShopList(pp.car_idA.toString(), pp.coordinate, type).getJSONArray("list");
            int total = isAble.size();
            //关闭门店预约配置
             util.loginPc(ACCOUNT);
            jc.shopStatusChange(pp.shopIdZ, "APPOINTMENT", "DISABLE");
            //小程序预约门店列表
            jc.appletLoginToken(pp.appletTocken);
            JSONArray isAbleAfter = jc.appletmaintainShopList(pp.car_idA.toString(), pp.coordinate, type).getJSONArray("list");
            int totalAfter = isAbleAfter.size();

             util.loginPc(ACCOUNT);
            jc.shopStatusChange(pp.shopIdZ, "APPOINTMENT", "ENABLE");
            jc.appletLoginToken(pp.appletTocken);

            int totalAfter2 = jc.appletmaintainShopList(pp.car_idA.toString(), pp.coordinate, "MAINTAIN").getJSONArray("list").size();
            Preconditions.checkArgument(total - totalAfter == 1, "关闭预约配置，小程序预约门店-1");
            Preconditions.checkArgument(totalAfter2 - totalAfter == 1, "开启预约配置，小程序预约门店+1");

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            jc.pcLogin(pp.gwphone, pp.gwpassword);
            saveData("pc修改预约配置验证");
        }
    }

    @Test()  //pc门店开关关闭，预约和洗车开关自动关闭
    public void pcShopButton() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            String statusAll = "DISABLE";
            String open = "ENABLE";
            JSONObject data = jc.shopListFilterManage("", "1", "10", "name", pp.shopname).getJSONArray("list").getJSONObject(0);
            String status1 = data.getString("status");
            if (!status1.equals("ENABLE")) {
                statusAll = "ENABLE";
                open = "DISABLE";
            }
            //关闭门店预约配置
             util.loginPc(ACCOUNT);
            jc.shopStatusChange(pp.shopIdZ, "SHOP", statusAll);
            JSONObject dataAfter = jc.shopListFilterManage("", "1", "10", "name", "中关村店").getJSONArray("list").getJSONObject(0);
            String status = dataAfter.getString("status");
            String appointment_status = dataAfter.getString("appointment_status");
            String washing_status = dataAfter.getString("washing_status");
            Preconditions.checkArgument(appointment_status.equals(statusAll) && washing_status.equals(statusAll) && status.equals(statusAll), "门店开关未同步");

            jc.shopStatusChange(pp.shopIdZ, "SHOP", open);
            JSONObject dataAfter2 = jc.shopListFilterManage("", "1", "10", "name", "中关村店").getJSONArray("list").getJSONObject(0);
            String status2 = dataAfter2.getString("status");
            String appointment_status2 = dataAfter2.getString("appointment_status");
            String washing_status2 = dataAfter2.getString("washing_status");
            Preconditions.checkArgument(appointment_status2.equals(statusAll) && washing_status2.equals(statusAll) && status2.equals(open), "门店开关未同步2");
            jc.shopStatusChange(pp.shopIdZ, "WASHING", open);
            jc.shopStatusChange(pp.shopIdZ, "APPOINTMENT", open);

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("pc门店按钮修改关联验证");
        }
    }

    @DataProvider(name = "EVALUATETYPE")
    public static Object[][] evaluatetype() {
        return new String[][]{
                {"1", "保养评价消息"},
                {"2", "维修评价消息"}
        };
    }

    @Test(dataProvider = "EVALUATETYPE", enabled = false)
    public void evalute(String type1, String messageName) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
//            System.out.println(type1 + ":" + messageName);
            Integer type = Integer.parseInt(type1);

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            int points;
            if (day % 2 == 1) {
                points = 3;
            } else {
                points = 2;
            }

            //修改配置，评价，积分数，卡券数
            IScene evaluateConfig = EvaluateConfigSubmitScene.builder().evaluateReward(true)
                    .defaultFavourableCycle(3)
                    .isSendPoints(true).isSendVoucher(true).points(points)
                    .type(type).vouchersId(pp.voucherIdevluate).build();
            jc.invokeApi(evaluateConfig);

            int voucherTotal = pf.getVoucherTotal("13436941018");
            jc.appletLoginToken(pp.appletTocken);
////            String id[] = pf.getMessageId(messageName);
//            if (StringUtils.isEmpty(id[0])) {
//                throw new Exception("没有待评价的消息，检查接待case 是否失败");
//            }
//            System.out.println("messageId" + id[0]);
            //评价前的积分和卡券数

            Integer appletScore = jc.appletUserInfoDetail().getInteger("score");

            JSONObject evaluateConfigDescribe = jc.AppletEvaluateConfigScene(type, Long.parseLong(pp.shopIdZ));
            String describe = evaluateConfigDescribe.getJSONArray("list").getJSONObject(2).getString("describe");
            List label = evaluateConfigDescribe.getJSONArray("list").getJSONObject(2).getJSONArray("labels");

            //评价
            IScene evaluatesubmit = AppletEvaluateSubmitScene.builder()
                    .describe(describe).labels(label).id(pf.getMessDetailId())
                    .isAnonymous(true)
                    .score(2)
                    .shopId(Long.parseLong(pp.shopIdZ)).suggestion("自动评价").type(type)
                    .build();
            jc.invokeApi(evaluatesubmit);

            //评价后的积分和卡券数
            Integer appletScoreAfter = jc.appletUserInfoDetail().getInteger("score");
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            int voucherTotalAfter = pf.getVoucherTotal("13436941018");


            Preconditions.checkArgument(voucherTotalAfter - voucherTotal == 1, "评价完成后，卡券没+1");
            Preconditions.checkArgument(appletScoreAfter - appletScore == points, "评价完成后，积分奖励没发");


        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
            saveData(type1 + "评价配置修改验证");
        }
    }

    @Test()
    public void delete() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcLogin(pp.gwphone, pp.jdgwpassword, pp.roleId);
            for (int j = 13; j < 16; j++) {
                JSONArray list = jc.organizationRolePage(j, 10).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String id = list.getJSONObject(i).getString("id");
                    String name = list.getJSONObject(i).getString("name");
                    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                    Matcher m = p.matcher(name);
                    if (m.find()) {
                        continue;
                    }
                    jc.organizationidRoleDelete(id);
                }
            }

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            pcLogin(pp.jdgw, pp.jdgwpassword, pp.roleidJdgw);
//            saveData("");
        }
    }


}
