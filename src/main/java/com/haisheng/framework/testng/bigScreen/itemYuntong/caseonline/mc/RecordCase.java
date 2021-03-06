package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.mc;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.CarFileUploadScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand.BrandAddScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand.BrandDeleteScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.brand.BrandPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool.JcDataCenter;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool.YtDataCenter;
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
    private static final EnumTestProduct product = EnumTestProduct.YT_ONLINE_JD; // 管理页—-首页
    private static final EnumAccount AUTHORITY = EnumAccount.YT_ONLINE_MC; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(product);   // 产品类放到代理类中（通过代理类发请求）
    public SceneUtil util = new SceneUtil(visitor);    //场景工具类中放入代理类，类中封装接口方法直接调用

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        CommonConfig commonConfig = new CommonConfig();    // 配置类初始化
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "孟辰";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(product.getShopId()).setRoleId(AUTHORITY.getRoleId()).setProduct(product.getAbbreviation());
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
        logger.logCaseStart(caseResult.getCaseName());
    }


    /**
     * @description :导出页-->导出当前页-->导出记录增加一条“导出记录”处的信息
     **/
    @Test(dataProvider = "exportPages", dataProviderClass = YtDataCenter.class)
    public void testExportPage(String product, String path, String type) {
        try {
            visitor.setProduct(EnumTestProduct.YT_ONLINE_JD);
            JSONObject res1 = util.checkExport(); //查询接口
            Integer total1 = res1.getInteger("total");//检查导出操作前的总记录
            if (Objects.equals(product, "control")) {
                visitor.setProduct(EnumTestProduct.YT_ONLINE_GK);
            }
            if (Objects.equals(product, "car")) {
                visitor.setProduct(EnumTestProduct.YT_ONLINE_JD);
            }
            util.carPageExport(path); //在对应页面中导出
            visitor.setProduct(EnumTestProduct.YT_ONLINE_JD);
            JSONObject res2 = util.checkExport();//查询接口
            Integer total2 = res2.getInteger("total"); // 检查导出操作后的总记录
            String typeName = res2.getJSONArray("list").getJSONObject(0).getString("type_name"); //获取导出的页面字段
            Preconditions.checkArgument(total2 == total1 + 1, type + "页面导出结果导出记录中没有+1");  //判断记录是否+1
            if (total2 == total1 + 1) {
                Preconditions.checkArgument(Objects.equals(typeName, type), "导出记录中的第一条期待：" + type+"，实际："+typeName); // 如果结果+1 ，判断第一条是不是相应的位置
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("导出记录");
        }


    }



    /**
     * @description :创建品牌-->删除品牌-->删除记录+1，名字为新创建的品牌名
     * @date :2021/7/15 18:30
     **/

    @Test
    public void deleteRecord() {
        visitor.setProduct(EnumTestProduct.YT_ONLINE_JD);
        try {
            JSONObject res1 = util.checkDelete(); //删除之前校验
            Integer total1 = res1.getInteger("total");  //之前总删除条数
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
            String logo = CarFileUploadScene.builder().pic(new ImageUtil().getImageBinary(filePath)).permanentPicType(0).build().visitor(visitor).execute().getString("pic_path");
            String name = "测试删除记录";
            BrandAddScene.builder().name(name).logoPath(logo).build().visitor(visitor).execute();
            //获取创建的品牌id
            Long id = BrandPageScene.builder().page(1).size(10).name(name).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            BrandDeleteScene.builder().id(id).build().visitor(visitor).execute();
            JSONObject res2 = util.checkDelete();  // 删除之后校验
            Integer total2 = res2.getInteger("total");  //之后总删除条数
            String type = res2.getJSONArray("list").getJSONObject(0).getString("operation_content");  //删除的品牌名
            Preconditions.checkArgument(total2 == total1 + 1, "删除记录没有增加1");
            if (total2 == total1 + 1) {
                Preconditions.checkArgument(Objects.equals(type, name), "删除的不是新创建的:" + type + "!=" + name);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("删除记录");
        }


    }


}
