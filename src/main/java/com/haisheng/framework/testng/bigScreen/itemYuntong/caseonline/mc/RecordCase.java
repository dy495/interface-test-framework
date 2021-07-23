package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.mc;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.CarFileUploadScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand.BrandAddScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand.BrandDeleteScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand.BrandPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Objects;


/**
 * @description :系统记录
 **/
public class RecordCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.YT_ONLINE_SSO; // 管理页—-首页
    private static final EnumAccount AUTHORITY = EnumAccount.YT_RECEPTION_ONLINE_5; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);   // 产品类放到代理类中（通过代理类发请求）
    public SceneUtil util = new SceneUtil(visitor);    //场景工具类中放入代理类，类中封装接口方法直接调用
    CommonConfig commonConfig = new CommonConfig();    // 配置类初始化

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = PRODUCE.getAbbreviation(); // 产品代号 -- YT
        commonConfig.shopId = PRODUCE.getShopId();  //请求头放入shopId
        commonConfig.roleId = AUTHORITY.getRoleId(); //请求头放入roleId
        beforeClassInit(commonConfig);  // 配置请求头
        util.loginPc(AUTHORITY);   //登录
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
        logger.debug("case: " + caseResult);
    }


    /**
     * @description :导出页-->导出当前页-->导出记录增加一条“导出记录”处的信息
     **/
    @Test(dataProvider = "CarExportPages")
    public void testExportPage(String product, String path, String type) {
        try {
            visitor.setProduct(EnumTestProduce.YT_ONLINE_CAR);
            JSONObject res1 = util.checkExport(); //查询接口
            Integer total1 = res1.getInteger("total");//检查导出操作前的总记录
            if (Objects.equals(product,"control")) {
                visitor.setProduct(EnumTestProduce.YT_ONLINE_CONTROL);
            }
            if (Objects.equals(product,"car")) {
                visitor.setProduct(EnumTestProduce.YT_ONLINE_CAR);
            }
            util.carPageExport(path); //在对应页面中导出
            visitor.setProduct(EnumTestProduce.YT_ONLINE_CAR);
            JSONObject res2 = util.checkExport();//查询接口
            Integer total2 = res2.getInteger("total"); // 检查导出操作后的总记录
            String typeName = res2.getJSONArray("list").getJSONObject(0).getString("type_name"); //获取导出的页面字段
            Preconditions.checkArgument(total2 == total1 + 1, type + "页面导出结果导出记录中没有+1");  //判断记录是否+1
            if (total2 == total1 + 1) {
                Preconditions.checkArgument(Objects.equals(typeName,type), "导出记录中的第一条不是" + type); // 如果结果+1 ，判断第一条是不是相应的位置
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出记录");
        }


    }

    @DataProvider(name = "CarExportPages")
    public Object[] exportPages() {
        return new String[][]{
                {"car", "/car-platform/pc/customer-manage/pre-sale-customer/buy-car/page/export", "成交记录"},
                {"car", "/car-platform/pc/pre-sales-reception/export", "销售接待记录"},
                {"car", "/car-platform/pc/manage/evaluate/v4/export", "销售接待线下评价"},
                {"car", "/car-platform/pc/record/import-record/export", "导入记录"},
                {"car", "/car-platform/pc/record/export-record/export", "导出记录"},
                {"car", "/car-platform/pc/record/login-record/export", "登录记录"},
                {"car", "/car-platform/pc/shop/export", "门店管理"},
                {"car", "/car-platform/pc/brand/export", "品牌管理"},
                {"car", "/car-platform/pc/brand/car-style/export", "车系列表"},
                {"car", "/car-platform/pc/brand/car-style/car-model/export", "车型列表"},
                {"control", "/intelligent-control/pc/manage/voice/evaluation/export", "语音评鉴记录"}
        };
    }

    /**
     * @description :创建品牌-->删除品牌-->删除记录+1，名字为新创建的品牌名
     * @date :2021/7/15 18:30
     **/

    @Test
    public void deleteRecord() {
        visitor.setProduct(EnumTestProduce.YT_ONLINE_CAR);
        try {
            JSONObject res1 = util.checkDelete(); //删除之前校验
            Integer total1 = res1.getInteger("total");  //之前总删除条数
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
            String logo = CarFileUploadScene.builder().pic(new ImageUtil().getImageBinary(filePath)).permanentPicType(0).build().invoke(visitor).getString("pic_path");
            String name = "测试删除记录";
            BrandAddScene.builder().name(name).logoPath(logo).build().invoke(visitor);
            //获取创建的品牌id
            Long id = BrandPageScene.builder().page(1).size(10).name(name).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
            BrandDeleteScene.builder().id(id).build().invoke(visitor);
            JSONObject res2 = util.checkDelete();  // 删除之后校验
            Integer total2 = res2.getInteger("total");  //之后总删除条数
            String type = res2.getJSONArray("list").getJSONObject(0).getString("operation_content");  //删除的品牌名
            Preconditions.checkArgument(total2 == total1 + 1, "删除记录没有增加1");
            if (total2 == total1 + 1) {
                Preconditions.checkArgument(Objects.equals(type,name), "删除的不是新创建的:" + type + "!=" + name);
            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("删除记录");
        }


    }


}
