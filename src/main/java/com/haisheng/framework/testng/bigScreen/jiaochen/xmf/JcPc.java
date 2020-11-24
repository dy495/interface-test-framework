package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.IScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.appointmentRecodeSelect;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;


public class JcPc extends TestCaseCommon implements TestCaseStd {

    ScenarioUtil jc = ScenarioUtil.getInstance();
    DateTimeUtil dt = new DateTimeUtil();
    FileUtil file = new FileUtil();
    Random random = new Random();
    public int page = 1;
    public int size = 50;
    public String name = "";
    public String email = "";
    public String phone = "";

    Integer status = 1;
    String type = "PHONE";

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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "CRM 日常X");

        //replace ding push conf
//        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getProscheShop();
        beforeClassInit(commonConfig);

        logger.debug("jc: " + jc);
        jc.pcLogin("", "");


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

    //创建复合权限角色
    @Test(dataProvider = "NUMA", dataProviderClass = ScenarioUtil.class)
    public void createRole(int a[]) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "" + a[0];
            JSONArray moduleId = new JSONArray();
            for (int i = 1; i < a.length; i++) {
                moduleId.add(i);
            }
            String description = "自动创建" + a[0];
            //新增一个角色
            jc.organizationRoleAdd(name, description, moduleId, true);

            int size = jc.organizationRolePage(1, 10).getInteger("total");
            if (size < 100) {
                JSONArray list = jc.organizationRolePage(name, 1, 100).getJSONArray("list");
                Long role_id = list.getJSONObject(list.size() - 1).getLong("role_id");
                jc.organizationRoleDelete(role_id, true);
            } else {
                logger.warn("警告：角色数量超过100个，不在删除新增角色，将造成数据冗余");
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("新增角色");
        }


    }

    /**
     * ====================新增角色======================
     */
    @Test(description = "角色的CURD,数据校验")
    public void role_add() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

            String description = "自动化测试给店长用的角色";
            JSONArray moduleId = new JSONArray();
            moduleId.add(7);
            moduleId.add(9);

            //新增一个角色
            JSONObject res = jc.organizationRoleAdd(name, description, moduleId);
            Integer code = res.getInteger("code");

            Long role_id = jc.organizationRolePage(name, page, size).getJSONArray("list").getJSONObject(0).getLong("role_id");
            checkArgument(code == 1000, "新增角色失败了");

            //编辑角色
            String name1 = "AUTOtest在编辑";
            Integer code1 = jc.organizationRoleEdit(role_id, name1, description, moduleId).getInteger("code");

            checkArgument(code1 == 1000, "编辑角色的信息失败了");

            //列表中编辑过的角色是否已更新
            JSONArray list1 = jc.organizationRolePage(name1, page, size).getJSONArray("list");
            String role_name = list1.getJSONObject(0).getString("role_name");

            checkArgument(name1.equals(role_name), "编辑过的角色没有更新在列表");


            //新建成功以后删除新建的账号
            if (name.equals(role_name)) {
                Integer code2 = jc.organizationRoleDelete(role_id).getInteger("code");
                checkArgument(code2 == 1000, "删除角色:" + role_id + "失败了");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新增删改查角色");
        }

    }

    /**
     * ====================新增角色(名称校验)======================
     */
    @Test
    public void role_add_work() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray moduleId = new JSONArray();
            moduleId.add(7);
            moduleId.add(9);

            //新增角色名称20个字的角色
            String description = "自动化测试给店长自动化用的角色";
            JSONObject res = jc.organizationRoleAdd("这是一个二十字的角色名称是的是的是的", description, moduleId);
            checkArgument(res.getInteger("code") == 1000, "角色名称为20个字，创建失败");

            //新增角色名称20个字英文+中文+数字的角色
            JSONObject res1 = jc.organizationRoleAdd("这是一个二十字的角色名称AABB1111", description, moduleId);
            checkArgument(res1.getInteger("code") == 1000, "角色名称为中文+字母+数字，创建失败");

            //新增角色名称20个字英文+中文+数字+字符的角色
            JSONObject res2 = jc.organizationRoleAdd("这是一个二十字的角色名称AABB11.。", description, moduleId);
            checkArgument(res2.getInteger("code") == 1000, "角色名称为中文+字母+数字+字符，创建失败");

            //新增角色名称21个字角色
            JSONObject res3 = jc.organizationRoleAdd("这是一个二十一字的角色名称是的是的是的是的", description, moduleId);
            checkArgument(res3.getString("message").equals("角色名称需要在1-20个字内"), "角色名称为21个字，创建成功");

            //新增重复角色名称的角色
            JSONObject res4 = jc.organizationRoleAdd("这是一个二十字的角色名称AABB11.。", description, moduleId);
            checkArgument(res4.getString("message").equals("新增角色异常:当前角色名称已存在！请勿重复添加"), "重复的角色名称，创建成功");
            jc.deleteRole();

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新增角色(名称校验)");
        }

    }

    /**
     * ====================新增角色(权限说明校验)======================
     */
    @Test
    public void role_add_work1() {
        logger.logCaseStart(caseResult.getCaseName());

        try {

            JSONArray moduleId = new JSONArray();
            moduleId.add(7);
            moduleId.add(9);
            moduleId.add(10);

            //新增角色权限说明50个字的角色
            JSONObject res = jc.organizationRoleAdd("auto名字3", "不是这是一个二十字的角色名称是的是的是的不是的的不是的好的好还需要二十个字现在是三十七了吧刚好五个字", moduleId);
            checkArgument(res.getInteger("code") == 1000, "角色权限说明为50个字，创建失败");

            //新增角色权限说明角色字英文+中文+数字的角色
            JSONObject res1 = jc.organizationRoleAdd("auto名字1", "22一个二十字的角色名称AABB", moduleId);
            checkArgument(res1.getInteger("code") == 1000, "角色权限说明中文+字母+数字，创建失败");

            //新增角色权限说明角色英文+中文+数字+字符的角色
            JSONObject res2 = jc.organizationRoleAdd("auto名字2", "这是一个二十字色名称BB11.。", moduleId);
            checkArgument(res2.getInteger("code") == 1000, "角色权限说明为中文+字母+数字+字符，创建失败");

            //新增角色权限说明51个字的角色
            JSONObject res3 = jc.organizationRoleAdd("auto名字4", "不是这是一个二十字的角色名称是的是的是的不是的的不是的好的好还需要二十个字现在是三十七了吧刚好五个字多", moduleId);
            checkArgument(res3.getString("message").equals("角色名称需要在1-50个字内"), "角色权限说明为51个字，创建成功");
            jc.deleteRole();

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新增角色(权限说明校验)");
        }

    }


    /**
     * ====================账户管理中的一致性========================
     */
    @Test(description = "新增1个账号，列表+1；删除1个账号，列表-1；修改账号名称后与列表是否一致")
    public void accountInfoData() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            Integer total = jc.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");

            List<String> r_dList = new ArrayList<String>();
            r_dList.add("4");
            r_dList.add("99");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("4116");

            Integer status = 1;
            //用EMAIL新增一个账号
            JSONObject res = jc.organizationAccountAdd(name, email, phone, r_dList, status, shop_list, type);

            //从列表获取刚刚新增的账户的account
            JSONArray accountList = jc.organizationAccountPage(name, "", email, phone, "", "", page, size).getJSONArray("list");
            String account = accountList.getJSONObject(0).getString("account");

            //新增账号以后，再查询列表
            Integer total1 = jc.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");
            int result = total1 - total;
            Preconditions.checkArgument(result == 1, "新增1个账号，账号列表的数量却加了：" + result);


            //编辑账号的名称，是否与列表该账号的一致
            String reName = "自动化测编辑";
            jc.organizationAccountEdit(account, reName, email, "", r_dList, status, shop_list, type);
            JSONArray accountsList = jc.organizationAccountPage("", "", "", "", "", "", page, size).getJSONArray("list");
            String name_1 = accountsList.getJSONObject(accountsList.size() - 1).getString("name");
            Preconditions.checkArgument(name_1.equals(reName), "修改账号：" + account + "的名称为：" + reName + "修改后，该账号的名称为：" + name_1);


            //删除账号以后，再查询列表
            Integer code1 = jc.organizationAccountDelete(account).getInteger("code");
            Preconditions.checkArgument(code1 == 1000, "删除emial的账号:" + email + "失败了");
            Integer total2 = jc.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");
            int result1 = total1 - total2;
            Preconditions.checkArgument(result1 == 1, "删除1个账号，账号列表的数量却减了：" + result);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("新增1个账号，列表+1；删除1个账号，列表-1；修改账号信息以后与列表是否一致");
        }

    }

    /**
     * ====================账户管理中的一致性========================
     */
    @Test
    public void accountInfoData_1() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack = false;
        try {
            Integer total = jc.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");

            List<String> r_dList = new ArrayList<String>();
            r_dList.add("4");
            r_dList.add("99");

            List<String> shop_list = new ArrayList<String>();
            shop_list.add("4116");


            JSONArray list = jc.organizationAccountPage("", "", "", "", "", "", page, size).getJSONArray("list");
            String today = dt.getHHmm(0);
            String account = "";
            String old_phone = "";
            String create_time = "";
            for (int i = 1; i < list.size(); i++) {
                create_time = list.getJSONObject(list.size() - 1).getString("create_time");
                if (!create_time.equals(today)) {
                    account = list.getJSONObject(list.size() - 1).getString("account");
                    old_phone = list.getJSONObject(list.size() - 1).getString("phone");
                    break;
                }
            }

            if (old_phone != "" && old_phone != null) {
                //编辑账号的名称，权限
                String reName = "自动化在测编辑";
                jc.organizationAccountEdit(account, reName, "", old_phone, r_dList, status, shop_list, type);
                //获取列表该账号
                JSONArray accountList = jc.organizationAccountPage("", "", "", old_phone, "", "", page, size).getJSONArray("list");
                String create_time_1 = "";
                String phone_1 = accountList.getJSONObject(0).getString("phone");//获取通过手机号搜索到的账号的手机号
                if (phone_1.equals(old_phone)) {
                    create_time_1 = accountList.getJSONObject(0).getString("create_time");

                }
                Preconditions.checkArgument(create_time_1.equals(create_time), "编辑昨天" + create_time + "的创建的账号" + old_phone + "列表该账号的创建时间变成了最新编辑的时间" + create_time_1);
                //编辑完以后获取列表的数量，是否有增多或者减少
                Integer total1 = jc.organizationAccountPage("", "", "", "", "", "", page, size).getInteger("total");
                Preconditions.checkArgument(total == total1, "编辑一个账号，账号列表的数量由:" + total + "变成了" + total1);

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("编辑账号信息以后，创建者和创建时间是否发生改变");
        }

    }

    /**
     * ====================账户管理中的一致性（使用账号数量==账号列表中的数量）========================
     */
    @Test
    public void accountInfoData_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = jc.organizationRolePage("", page, size).getJSONArray("list");

            for (int i = 1; i < list.size(); i++) {
                String role_name = list.getJSONObject(i).getString("role_name");
                JSONArray list1 = jc.organizationRolePage(role_name, page, size).getJSONArray("list");
                int account_num = list1.getJSONObject(0).getInteger("account_number");

                Integer Total = jc.organizationAccountPage("", "", "", "", role_name, "", page, size).getInteger("total");

                Preconditions.checkArgument(account_num == Total, "角色名为:" + role_name + "的使用账户数量：" + account_num + "！=【账户列表】中该角色的账户数量：" + Total);
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("角色的使用账号数量==账号列表中该角色的数量");
        }

    }

    //禁用账户登录失败，开启登录成功
    @Test
    public void accountStart() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //禁用开启按钮
            jc.organizationAccountButtom("", 1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("禁用账户登录失败，开启登录成功");
        }
    }

    //查询案例
    /**
     * @description :接待管理查询   ---for  liya
     * @date :2020/8/3 12:48
     **/
    @Test(dataProvider = "SELECT_PARM", dataProviderClass = ScenarioUtil.class)
    public void receptionSelect(String parm,String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String shopId="";
            JSONObject data = jc.receptionManage(shopId,"1", "10","","");

            String Expetresult = data.getJSONArray("list").getJSONObject(0).getString(parm);
            JSONArray list = jc.receptionManage(shopId, "1","10", parm,Expetresult).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String SelectResult = list.getJSONObject(i).getString(output);
                Preconditions.checkArgument((Expetresult.equals(SelectResult)), "接待管理按"+parm+"查询，结果错误"+SelectResult);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("接待管理查询，结果校验");
        }
    }


}
