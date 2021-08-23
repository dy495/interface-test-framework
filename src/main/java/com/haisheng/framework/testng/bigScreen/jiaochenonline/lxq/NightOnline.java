package com.haisheng.framework.testng.bigScreen.jiaochenonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import org.testng.annotations.*;

import java.lang.reflect.Method;


/**
 * @author : lxq
 * @date :  2021/08/19
 */

public class NightOnline extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.JC_ONLINE_JD;
    EnumAccount account = EnumAccount.JC_ONLINE_YS;
    VisitorProxy visitor = new VisitorProxy(product);
    CommonConfig commonConfig = new CommonConfig();
    ScenarioUtilOnline jc = ScenarioUtilOnline.getInstance();
    jiaoChenInfoOnline info = new jiaoChenInfoOnline();
    String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
    SceneUtil util = new SceneUtil(visitor);

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");

        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "吕雪晴";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.CRM_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId("395").setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);

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
        util.loginPc(account);
    }






    @Test(dataProvider = "export")
    public void ExportAll(String url, String mess) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            commonConfig.setShopId("20032");
            //导出
            int code = jc.recExport(url).getInteger("code");
            Preconditions.checkArgument(code == 1000, mess + "导出状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), mess + " " + status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            commonConfig.setShopId("-1");
            saveData("导出");
        }
    }

    @DataProvider(name = "export")
    public Object[] export() {
        return new String[][]{
                {"/car-platform/pc/customer-manage/pre-sale-customer/page/export", "销售客户"},
                {"/car-platform/pc/customer-manage/after-sale-customer/page/export", "售后客户"},
                {"/car-platform/pc/customer-manage/wechat-customer/page/export", "小程序客户"},
                {"/car-platform/pc/voucher-manage/voucher-form/export", "优惠券管理"},
                {"/car-platform/pc/voucher-manage/verification-people/export", "核销人员"},
                {"/car-platform/pc/package-manage/buy-package-record/export", "套餐购买记录"},
                {"/car-platform/pc/operation/article/export", "文章列表"},
                {"/car-platform/pc/activity/manage/export", "活动列表"},
                {"/car-platform/pc/voucher/apply/export", "优惠券申请"},
                {"/car-platform/pc/shop/export", "门店管理"},
                {"/car-platform/pc/brand/export", "品牌管理"},
                {"/car-platform/pc/role/export", "角色管理"},
                {"/car-platform/pc/staff/export", "员工管理"},
                {"/car-platform/pc/record/import-record/export", "导入记录"},
                {"/car-platform/pc/record/export-record/export", "导出记录"},
                {"/car-platform/pc/record/push-msg/export", "消息记录"},
                {"/car-platform/pc/record/login-record/export", "登陆记录"},
                {"/car-platform/pc/manage/rescue/export", "道路救援"},
                {"/car-platform/pc/vip-marketing/wash-car-manager/export", "洗车管理"},
                {"/car-platform/pc/vip-marketing/wash-car-manager/adjust-number/export", "调整次数"},
                {"/car-platform/pc/vip-marketing/sign_in_config/change-record/export", "签到积分变更记录"},
                {"/car-platform/pc/integral-center/exchange/export", "积分兑换"},
                {"/car-platform/pc/integral-center/exchange-detail/export", "积分明细"},
                {"/car-platform/pc/integral-center/exchange-order/export", "积分订单"},
                {"/car-platform/pc/integral-mall/goods-manage/export", "商品管理"},
                {"/car-platform/pc/manage/maintain/car-model/export", "保养配置"},
                {"/car-platform/pc/customer-manage/pre-sale-customer/buy-car/page/export", "成交记录"},
                {"/car-platform/pc/pre-sales-reception/export", "销售接待记录"},
                {"/car-platform/pc/reception-manage/record/export", "售后接待管理"},
                {"/car-platform/pc/customer-manage/loss-customer/page/export", "流失客户管理"},
                {"/car-platform/pc/voucher/apply/export", "优惠券审批"},
                {"XXXXXXXX", "保养车系配置"},
                {"/car-platform/pc/consult-management/online-experts-page-list-export", "在线专家列表"},
                {"/car-platform/pc/consult-management/dedicated-service-page-list-export", "专属服务列表"},


        };
    }

    @Test
    public void Exportweixiu() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONObject obj = jc.afterSleCustomerManage("1", "10").getJSONArray("list").getJSONObject(0);
            String carid = obj.getString("car_id");
            String shopid = obj.getString("shop_id");

            //导出
            int code = jc.weixiuExport(carid, shopid).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出维修记录");
        }
    }

    @Test(dataProvider = "exportVourcher")
    public void ExportAll1(String url, String mess) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            String id = jc.oucherFormVoucherPage(null, "1", "10").getJSONArray("list").getJSONObject(0).getString("voucher_id");
            //导出
            int code = jc.vourcherExport(url, id).getInteger("code");
            Preconditions.checkArgument(code == 1000, mess + "导出状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), mess + " " + status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出");
        }
    }

    @DataProvider(name = "exportVourcher")
    public Object[] exportVourcher() {
        return new String[][]{ // 单弄 活动报名记录、车系列表、车型列表

                {"/car-platform/pc/voucher-manage/change-record/export", "优惠券变更记录"},
                {"/car-platform/pc/voucher-manage/voucher-invalid-page/export", "作废记录"},
                {"/car-platform/pc/voucher-manage/additional-record/export", "增发记录"},
                {"/car-platform/pc/voucher-manage/send-record/export", "领取记录"},
                {"/car-platform/pc/voucher-manage/verification-record/export", "核销记录"},

        };
    }

    @Test
    public void ExportAll2() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            String id = "";
            JSONArray array = jc.activityPage(1, 50).getJSONArray("list");
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.getInteger("activity_type") == 2) {// 招募活动 类型是2
                    id = obj.getString("id");
                }
            }
            //导出
            int code = jc.activityExport(id).getInteger("code");
            Preconditions.checkArgument(code == 1000, "导出状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出活动报名记录");
        }
    }

    @Test
    public void ExportAll3() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            //导出
            int code = jc.carStyleExport(info.BrandIDOnline).getInteger("code");
            Preconditions.checkArgument(code == 1000, "导出状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出车系列表");
        }
    }

    @Test
    public void ExportAll4() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            //导出
            int code = jc.carModelExport(info.BrandIDOnline, info.CarStyleIDOnline).getInteger("code");
            Preconditions.checkArgument(code == 1000, "导出状态码为" + code);
            Thread.sleep(800);
            String status = jc.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出车型列表");
        }
    }


}
