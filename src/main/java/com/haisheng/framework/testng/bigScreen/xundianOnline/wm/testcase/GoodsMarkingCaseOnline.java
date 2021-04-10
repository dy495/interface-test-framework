package com.haisheng.framework.testng.bigScreen.xundianOnline.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.GoodsBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.GoodsParamBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.BrandListBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.BrandPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.CategoryListBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.GoodsManagePageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralCategoryTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity.CommodityStatusEnum;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralcenter.ChangeSwitchStatusScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralmall.*;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品运营用例
 *
 * @author wangmin
 * @date 2020/11/24
 */

public class GoodsMarkingCaseOnline extends TestCaseCommon implements TestCaseStd {
    private final static EnumTestProduce PRODUCE = EnumTestProduce.INS_ONLINE;
    private final static AccountEnum ALL_AUTHORITY = AccountEnum.YUE_XIU_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    private final static String FILEPATH = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
    private final static String FILEPATH_TWO = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/goods/联想.jpg";
    private final static Integer SIZE = 100;

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = PRODUCE.getAbbreviation();
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
        user.loginPc(ALL_AUTHORITY);
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @Test(description = "商品品牌-创建品牌，名称1个字与10个字")
    public void integralMall_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] names = {"大", EnumDesc.DESC_10.getDesc()};
            Arrays.stream(names).forEach(name -> {
                String picPath = util.getPicPath(FILEPATH, "1:1");
                JSONObject response = CreateBrandScene.builder().brandName(name).brandDescription("梅赛德斯奔驰").brandPic(picPath).build().invoke(visitor, false);
                String message = response.getString("message");
                String err = "success";
                CommonUtil.checkResult("返回值message", err, message);
                //删除品牌
                IScene scene = BrandPageScene.builder().build();
                BrandPageBean brandPageBean = util.collectBeanByField(scene, BrandPageBean.class, "brand_name", name);
                DeleteBrandScene.builder().id(brandPageBean.getId()).build().invoke(visitor);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("商品品牌-创建品牌，名称1个字与10个字");
        }

    }

    @Test(description = "商品品牌-创建品牌，修改品牌")
    public void integralMall_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String picPath;
            picPath = util.getPicPath(FILEPATH, "1:1");
            CreateBrandScene.builder().brandName("奔驰").brandDescription("梅赛德斯奔驰").brandPic(picPath).build().invoke(visitor);
            IScene scene = BrandPageScene.builder().build();
            BrandPageBean brandPageBean = util.collectBeanByField(scene, BrandPageBean.class, "brand_name", "奔驰");
            Long id = brandPageBean.getId();
            //修改品牌
            ChangeSwitchStatusScene.builder().id(id).status(false).build().invoke(visitor);
            picPath = util.getPicPath(FILEPATH_TWO, "1:1");
            EditBrandScene.builder().id(id).brandName("联想").brandDescription("thinkPad").brandPic(picPath).build().invoke(visitor);
            BrandPageBean newBrandPageBean = util.collectBeanByField(scene, BrandPageBean.class, "id", id);
            CommonUtil.checkResult("修改后品牌名称", "联想", newBrandPageBean.getBrandName());
            CommonUtil.checkResult("修改后品牌描述", "thinkPad", newBrandPageBean.getBrandDescription());
            //删除品牌
            DeleteBrandScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("商品品牌-创建品牌，修改品牌");
        }
    }

    @Test(description = "商品品牌-创建品牌，名称异常")
    public void integralMall_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] names = {EnumDesc.DESC_10.getDesc() + "1", null, ""};
            Arrays.stream(names).forEach(name -> {
                String picPath = util.getPicPath(FILEPATH, "1:1");
                String message = CreateBrandScene.builder().brandName(name).brandDescription("梅赛德斯奔驰").brandPic(picPath).build().invoke(visitor, false).getString("message");
                String err = StringUtils.isEmpty(message) ? "" : "";
                CommonUtil.checkResult("商品名称为" + name, err, message);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("商品品牌-创建品牌，名称异常");
        }
    }

    //ok
    @Test()
    public void goodsBrand_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] names = {EnumDesc.DESC_20.getDesc() + "1", "", null};
            Arrays.stream(names).forEach(name -> {
                String message = CreateBrandScene.builder().brandPic(util.getCategoryPicPath()).brandName(name).brandDescription(EnumDesc.DESC_BETWEEN_5_10.getDesc()).build().invoke(visitor, false).getString("message");
                String err = StringUtils.isEmpty(name) ? "品牌名称不能为空" : "品牌名称长度应该为1～20个字";
                CommonUtil.checkResult("品牌名称为" + name, err, message);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】新建品牌");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = EnumDesc.DESC_BETWEEN_5_10.getDesc() + "重复";
            Long id = CreateBrandScene.builder().brandName(name).brandDescription(name).brandPic(util.getCategoryPicPath()).build().invoke(visitor).getLong("id");
            String message = CreateBrandScene.builder().brandName(name).brandDescription(name).brandPic(util.getCategoryPicPath()).build().invoke(visitor, false).getString("message");
            String err = "品牌名称已存在";
            CommonUtil.checkResult("创建重复名称的品牌", err, message);
            DeleteBrandScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】新建品牌,品牌名称与已存在的重复");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = EnumDesc.DESC_BETWEEN_5_10.getDesc();
            Long id = CreateBrandScene.builder().brandName(name).brandDescription(name).brandPic(util.getCategoryPicPath()).build().invoke(visitor).getLong("id");
            ChangeBrandStatusScene.builder().id(id).status(false).build().invoke(visitor);
            DeleteBrandScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】停用 无商品使用&状态=启用的品牌，成功");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = EnumDesc.DESC_BETWEEN_5_10.getDesc();
            Long id = CreateBrandScene.builder().brandName(name).brandDescription(name).brandPic(util.getCategoryPicPath()).build().invoke(visitor).getLong("id");
            ChangeBrandStatusScene.builder().id(id).status(false).build().invoke(visitor);
            ChangeBrandStatusScene.builder().id(id).status(true).build().invoke(visitor);
            DeleteBrandScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】启用 无商品使用&状态=停用的品牌，成功");
        }
    }

    /**
     * 商品规格
     */
    //ok
    @Test
    public void goodsSpecifications_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建一级品类
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            String name = EnumDesc.DESC_BETWEEN_5_10.getDesc();
            Long specificationsId = CreateSpecificationsScene.builder().specificationsName(name).belongsCategory(id).build().invoke(visitor).getLong("id");
            ChangeSpecificationsStatusScene.builder().id(specificationsId).status(false).build().invoke(visitor);
            DeleteSpecificationsScene.builder().id(specificationsId).build().invoke(visitor);
            DeleteCategoryScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品规格】停用无商品使用的规格，成功");
        }
    }

    //ok
    @Test
    public void goodsSpecifications_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建一级品类
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            String name = EnumDesc.DESC_BETWEEN_5_10.getDesc();
            //创建规格
            Long specificationsId = CreateSpecificationsScene.builder().specificationsName(name).belongsCategory(id).build().invoke(visitor).getLong("id");
            //删除停用的规格
            ChangeSpecificationsStatusScene.builder().id(specificationsId).status(false).build().invoke(visitor);
            DeleteSpecificationsScene.builder().id(specificationsId).build().invoke(visitor);
            Long newSpecificationsId = CreateSpecificationsScene.builder().specificationsName(name).belongsCategory(id).build().invoke(visitor).getLong("id");
            //删除未停用的规格
            DeleteSpecificationsScene.builder().id(newSpecificationsId).build().invoke(visitor);
            DeleteCategoryScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品规格】删除各种状态的&无商品使用的规格");
        }
    }

    /**
     * 商品管理
     */
    //ok
    @Test()
    public void goodsManager_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene goodsManagePageScene = GoodsManagePageScene.builder().build();
            GoodsManagePageBean goodsManagePageBean = util.collectBean(goodsManagePageScene, GoodsManagePageBean.class).get(0);
            String goodsName = goodsManagePageBean.getGoodsName();
            IScene newScene = GoodsManagePageScene.builder().goodsName(goodsName).build();
            List<GoodsManagePageBean> newGoodsManagePageBean = util.collectBean(newScene, GoodsManagePageBean.class);
            newGoodsManagePageBean.forEach(e -> Preconditions.checkArgument(e.getGoodsName().contains(goodsName), "搜索" + goodsName + ", 结果中包含" + e.getGoodsName()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品名称搜索");
        }
    }

    //ok
    @Test
    public void goodsManager_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = BrandListScene.builder().build().invoke(visitor).getJSONArray("list");
            List<BrandListBean> brandPageBeanList = list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, BrandListBean.class)).collect(Collectors.toList());
            brandPageBeanList.forEach(brandListBean -> {
                IScene goodsManagePageScene = GoodsManagePageScene.builder().goodsBrand(brandListBean.getId()).build();
                List<GoodsManagePageBean> goodsManagePageBeanList = util.collectBean(goodsManagePageScene, GoodsManagePageBean.class);
                goodsManagePageBeanList.forEach(goodsManagePageBean -> CommonUtil.checkResult(goodsManagePageBean.getGoodsName() + "的所属品牌", brandListBean.getBrandName(), goodsManagePageBean.getBelongsBrand()));
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品品牌搜索");
        }
    }

    //ok
    @Test()
    public void goodsManager_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(CommodityStatusEnum.values()).forEach(e -> {
                IScene scene = GoodsManagePageScene.builder().goodsStatus(e.name()).build();
                List<GoodsManagePageBean> goodsManagePageBeanList = util.collectBean(scene, GoodsManagePageBean.class);
                goodsManagePageBeanList.forEach(goodsManagePageBean -> {
                    CommonUtil.checkResult(goodsManagePageBean.getGoodsName() + "的商品状态", e.name(), goodsManagePageBean.getGoodsStatus());
                    CommonUtil.checkResult(goodsManagePageBean.getGoodsName() + "的商品状态", e.getName(), goodsManagePageBean.getGoodsStatusName());
                });
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品状态搜索");
        }
    }

    //ok
    @Test
    public void goodsManager_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene categoryListScene = CategoryListScene.builder().categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build();
            JSONArray list = categoryListScene.invoke(visitor).getJSONArray("list");
            List<CategoryListBean> categoryListBeanList = list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, CategoryListBean.class)).collect(Collectors.toList());
            categoryListBeanList.forEach(categoryListBean -> {
                IScene goodsManagePageScene = GoodsManagePageScene.builder().firstCategory(categoryListBean.getCategoryType()).build();
                List<GoodsManagePageBean> goodsManagePageBeanList = util.collectBean(goodsManagePageScene, GoodsManagePageBean.class);
                goodsManagePageBeanList.forEach(goodsManagePageBean -> CommonUtil.checkResult("按照一级品类搜索结果的商品分类", categoryListBean.getCategoryName(), goodsManagePageBean.getGoodsCategory()));
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】根据一级品类搜索");
        }
    }

    //ok
    @Test
    public void goodsManager_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = GoodsManagePageScene.builder().size(SIZE).build().invoke(visitor).getJSONArray("list");
            list.stream().map(obj -> (JSONObject) obj).forEach(obj -> {
                Preconditions.checkArgument(obj.containsKey("goods_pic"), "未展示商品图片");
                Preconditions.checkArgument(obj.containsKey("goods_name"), "未展示商品名称");
                Preconditions.checkArgument(obj.containsKey("belongs_brand"), "未展示所属品牌");
                Preconditions.checkArgument(obj.containsKey("goods_category"), "未展示商品分类");
                Preconditions.checkArgument(obj.containsKey("price"), "未展示市场价");
                Preconditions.checkArgument(obj.containsKey("goods_status"), "未展示商品状态");
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】列表展示项校验");
        }
    }

    //ok
    @Test
    public void goodsManager_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene goodsManagePageScene = GoodsManagePageScene.builder().build();
            List<GoodsManagePageBean> goodsManagePageBeanListA = util.collectBean(goodsManagePageScene, GoodsManagePageBean.class);
            GoodsManagePageBean goodsManagePageBean = goodsManagePageBeanListA.stream().filter(e -> !util.goodsIsOccupation(e.getGoodsName())).findFirst().orElse(goodsManagePageBeanListA.get(0));
            Long id = goodsManagePageBean.getId();
            //上架
            Arrays.stream(CommodityStatusEnum.values()).forEach(anEnum -> {
                ChangeGoodsStatusScene.builder().id(id).status(anEnum.name()).build().invoke(visitor);
                List<GoodsManagePageBean> goodsManagePageBeanList = util.collectBean(goodsManagePageScene, GoodsManagePageBean.class);
                goodsManagePageBeanList.stream().filter(e -> e.getId().equals(id)).forEach(e -> {
                    CommonUtil.checkResult(e.getGoodsName() + "的商品状态", anEnum.getName(), e.getGoodsStatusName());
                    CommonUtil.checkResult(e.getGoodsName() + "的商品状态", anEnum.name(), e.getGoodsStatus());
                });
            });
            //clean
            ChangeGoodsStatusScene.builder().id(id).status(CommodityStatusEnum.UP.name()).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】上架&下架存不被积分兑换占用商品");
        }
    }

    //ok
    @Test
    public void goodsManager_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene goodsManagePageScene = GoodsManagePageScene.builder().build();
            List<GoodsManagePageBean> goodsManagePageBeanListA = util.collectBean(goodsManagePageScene, GoodsManagePageBean.class);
            GoodsManagePageBean goodsManagePageBean = goodsManagePageBeanListA.stream().filter(e -> !util.goodsIsOccupation(e.getGoodsName())).findFirst().orElse(goodsManagePageBeanListA.get(0));
            Long id = goodsManagePageBean.getId();
            //下架商品
            ChangeGoodsStatusScene.builder().id(id).status(CommodityStatusEnum.DOWN.name()).build().invoke(visitor);
            //积分兑换看不见
            IScene scene = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build();
            util.collectBean(scene, GoodsManagePageBean.class).forEach(e -> Preconditions.checkArgument(!e.getId().equals(id), "下架的商品" + e.getGoodsName() + "在创建积分兑换时可见"));
            //clean
            ChangeGoodsStatusScene.builder().id(id).status(CommodityStatusEnum.UP.name()).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】下架的商品在创建积分兑换时不可见");
        }
    }

    //ok
    @Test
    public void goodsManager_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene goodsManagePageScene = GoodsManagePageScene.builder().build();
            List<GoodsManagePageBean> goodsManagePageBeanListA = util.collectBean(goodsManagePageScene, GoodsManagePageBean.class);
            GoodsManagePageBean goodsManagePageBean = goodsManagePageBeanListA.stream().filter(e -> util.goodsIsOccupation(e.getGoodsName())).findFirst().orElse(goodsManagePageBeanListA.get(0));
            //下架商品
            String message = ChangeGoodsStatusScene.builder().id(goodsManagePageBean.getId()).status(CommodityStatusEnum.DOWN.name()).build().invoke(visitor, false).getString("message");
            String err = "商品正在使用中，不能下架";
            CommonUtil.checkResult("下架被占用商品" + goodsManagePageBean.getGoodsName(), err, message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】下架被积分兑换占用的商品，失败");
        }
    }

    //ok
    @Test
    public void goodsManager_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene goodsManagePageScene = GoodsManagePageScene.builder().build();
            List<GoodsManagePageBean> goodsManagePageBeanListA = util.collectBean(goodsManagePageScene, GoodsManagePageBean.class);
            GoodsManagePageBean goodsManagePageBean = goodsManagePageBeanListA.stream().filter(e -> util.goodsIsOccupation(e.getGoodsName())).findFirst().orElse(goodsManagePageBeanListA.get(0));
            //下架商品
            String message = DeleteGoodsScene.builder().id(goodsManagePageBean.getId()).build().invoke(visitor, false).getString("message");
            String err = "商品正在使用中，不能删除";
            CommonUtil.checkResult("删除被占用商品" + goodsManagePageBean.getGoodsName(), err, message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】删除被积分兑换占用的商品，失败");
        }
    }

    //ok
    @Test()
    public void goodsManager_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            GoodsBean goodsBean = util.createGoods();
            GoodsParamBean goodsParamBean = goodsBean.getGoodsParamBean();
            //删除商品
            DeleteGoodsScene.builder().id(goodsBean.getGoodsId()).build().invoke(visitor);
            //删除品牌
            DeleteBrandScene.builder().id(goodsParamBean.getBrandId()).build().invoke(visitor);
            //删除规格
            ChangeSpecificationsStatusScene.builder().id(goodsParamBean.getSpecificationsId()).status(false).build().invoke(visitor);
            DeleteSpecificationsScene.builder().id(goodsParamBean.getSpecificationsId()).build().invoke(visitor);
            //删除品类
            Long[] ids = {goodsParamBean.getThirdCategory(), goodsParamBean.getSecondCategory(), goodsParamBean.getFirstCategory()};
            Arrays.stream(ids).forEach(id -> {
                ChangeStatusScene.builder().id(id).status(false).build().invoke(visitor);
                DeleteCategoryScene.builder().id(id).build().invoke(visitor);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】创建商品,一个规格一个参数");
        }
    }

//    @Test
//    public void goodAddMore() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //新建一个一二三级品类
//            String name = "品类";
//            JSONObject obj = info.newFirstCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)));
//            String idone = obj.getString("id");
//            JSONObject objtwo = info.newSecondCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idone);
//            String idtwo = objtwo.getString("id");
//            JSONObject objthree = info.newThirdCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idtwo);
//            String idthree = objthree.getString("id");
//
//
//            //新建品牌
//            String brandid = info.newGoodBrand("pp" + System.currentTimeMillis(), "pp说明" + System.currentTimeMillis()).getString("id");
//
//            //新建规格
//            String spename1 = "1规格" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));
//            String spename2 = "2规格" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));
//
//            Long speId = ins.specificationsCreate(spename1, Long.parseLong(idone), null, null, true).getLong("id"); //第一个规格 有10个参数
//            Long speId2 = ins.specificationsCreate(spename2, Long.parseLong(idone), null, null, true).getLong("id"); //第二个规格 有1个参数
//            //新建规格1的10个参数
//            JSONArray spearray = new JSONArray();
//            for (int i = 1; i < 11; i++) {
//                JSONObject objItem = new JSONObject();
//                objItem.put("specifications_item", "speItemName" + i);
//                spearray.add(objItem);
//            }
//
//            ins.specificationsEdit(spename1, Long.parseLong(idone), spearray, speId, true);
//            Long speItemId = ins.specificationsDetail(speId, 1, 10).getJSONArray("specifications_list").getJSONObject(0).getLong("specifications_id");
//
//            //新建规格2的一个参数
//            JSONObject objItem1 = new JSONObject();
//            objItem1.put("specifications_item", "speItemName11");
//            JSONArray spearray2 = new JSONArray();
//            spearray2.add(objItem1);
//            ins.specificationsEdit(spename2, Long.parseLong(idone), spearray2, speId2, true);
//            Long speItemId2 = ins.specificationsDetail(speId2, 1, 10).getJSONArray("specifications_list").getJSONObject(0).getLong("specifications_id");
//
//            //新建商品
//
//            pcCreateGoods good = new pcCreateGoods();
//            good.first_category = Long.parseLong(idone);
//            good.second_category = Long.parseLong(idtwo);
//            good.third_category = Long.parseLong(idthree);
//            good.goods_brand = Long.parseLong(brandid);
//
//            //新建商品
//
//            JSONArray select_specifications = new JSONArray();
//
//            JSONObject spe1obj = new JSONObject();
//            spe1obj.put("specifications_id", speId);
//            spe1obj.put("specifications_name", spename1);
//            //规格1的信息
//            JSONArray spe1list = new JSONArray();
//            for (int i = 0; i < 10; i++) {
//                JSONObject speclistobj = ins.specificationsDetail(speId, 1, 10).getJSONArray("specifications_list").getJSONObject(i);
//                Long specifications_id = speclistobj.getLong("specifications_id");
//                String specifications_item = speclistobj.getString("specifications_item");
//                JSONObject everyspeclist = new JSONObject();
//                everyspeclist.put("specifications_detail_id", specifications_id);
//                everyspeclist.put("specifications_detail_name", specifications_item);
//                spe1list.add(everyspeclist);
//            }
//            spe1obj.put("specifications_list", spe1list);
//            //规格2的信息
//            JSONObject spe2obj = new JSONObject();
//            spe2obj.put("specifications_id", speId2);
//            spe2obj.put("specifications_name", spename2);
//
//            JSONArray spe2list = new JSONArray();
//            JSONObject everyspeclist = new JSONObject();
//            everyspeclist.put("specifications_detail_id", speItemId2);
//            everyspeclist.put("specifications_detail_name", "speItemName11");
//            spe2list.add(everyspeclist);
//            spe2obj.put("specifications_list", spe2list);
//
//            select_specifications.add(spe1obj);
//            select_specifications.add(spe2obj);
//
//
//            //参数组合
//            JSONArray goods_specifications_list = new JSONArray();
//            for (int j = 0; j < 10; j++) {
//                JSONObject speclistobj = ins.specificationsDetail(speId, 1, 10).getJSONArray("specifications_list").getJSONObject(j);
//                Long specifications_id = speclistobj.getLong("specifications_id");
//                String specifications_item = speclistobj.getString("specifications_item");
//                JSONObject everygoodspeclist = new JSONObject();
//                everygoodspeclist.put("first_specifications", specifications_id);
//                everygoodspeclist.put("first_specifications_name", specifications_item);
//                everygoodspeclist.put("second_specifications", speItemId2);
//                everygoodspeclist.put("second_specifications_name", "speItemName11");
//                goods_specifications_list.add(everygoodspeclist);
//            }
//
//
//            good.select_specifications = select_specifications;
//            good.goods_specifications_list = goods_specifications_list;
//            good.checkcode = false;
//
//            JSONObject objnew = ins.createGoodMethod(good);
//            int code = objnew.getInteger("code");
//            Preconditions.checkArgument(code == 1000, "状态码" + code);
//            Long goodid = objnew.getJSONObject("data").getLong("id");
//
//
//            //删除商品
//            ins.deleteGoodMethod(goodid);
//            //关闭->删除规格
//            ins.specificationsChgStatus(speId, false);
//            ins.specificationsDel(speId);
//            ins.specificationsChgStatus(speId2, false);
//            ins.specificationsDel(speId2);
//            //删除品类
//            ins.categoryDel(Long.parseLong(idthree), true);
//            ins.categoryDel(Long.parseLong(idtwo), true);
//            ins.categoryDel(Long.parseLong(idone), true);
//
//
//        } catch (AssertionError | Exception e) {
//            collectMessage(e);
//        } finally {
//            saveData("PC【商品管理】创建商品,多个规格多参数");
//        }
//    }
}
