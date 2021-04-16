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
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralCategoryTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity.CommodityStatusEnum;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.enumerator.AccountEnum;
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
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
    private static final AccountEnum ALL_AUTHORITY = AccountEnum.YUE_XIU_ONLINE;
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
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
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

    @Test()
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

    @Test()
    public void integralMall_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CreateBrandScene.builder().brandName("奔驰").brandDescription("梅赛德斯奔驰").brandPic(util.getCategoryPicPath()).build().invoke(visitor);
            IScene scene = BrandPageScene.builder().build();
            Long id = util.collectBeanByField(scene, BrandPageBean.class, "brand_name", "奔驰").getId();
            //修改品牌
            ChangeBrandStatusScene.builder().id(id).status(false).build().invoke(visitor);
            EditBrandScene.builder().id(id).brandName("联想").brandDescription("thinkPad").brandPic(util.getCategoryPicPath()).build().invoke(visitor);
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

    @Test()
    public void integralMall_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] names = {EnumDesc.DESC_20.getDesc() + "1", null};
            Arrays.stream(names).forEach(name -> {
                String picPath = util.getPicPath(FILEPATH, "1:1");
                String message = CreateBrandScene.builder().brandName(name).brandDescription("梅赛德斯奔驰").brandPic(picPath).build().invoke(visitor, false).getString("message");
                String err = name == null ? "品牌名称不能为空" : "品牌名称长度应该为1～20个字";
                CommonUtil.checkResult("商品名称为" + name, err, message);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("商品品牌-创建品牌，名称异常");
        }
    }

    /**
     * 商品运营
     */
    //ok
    @Test
    public void goodsCategory_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<String> categoryNameList = new ArrayList<>();
            Long id = util.getCategoryByLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY);
            String categoryName = util.getCategoryName(id);
            IScene scene = CategoryPageScene.builder().firstCategory(id).build();
            List<CategoryPageBean> categoryPageBeanList = util.collectBean(scene, CategoryPageBean.class);
            categoryPageBeanList.forEach(e -> {
                if (e.getCategoryLevel().equals(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc())) {
                    CommonUtil.checkResult(e.getCategoryName() + "的父级品类", categoryName, e.getParentCategory());
                    categoryNameList.add(e.getCategoryName());
                }
            });
            categoryPageBeanList.forEach(e -> {
                if (e.getCategoryLevel().equals(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc())) {
                    Preconditions.checkArgument(categoryNameList.contains(e.getParentCategory()), e.getCategoryName() + "的父级品类在" + categoryNameList + "不存在");
                }
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】根据一级品类单项筛选，结果期待为该品类下的全部二级/三级品类");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List<Long> categoryIdList = new ArrayList<>();
            Long id = util.getCategoryByLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY);
            IScene scene = CategoryPageScene.builder().firstCategory(id).build();
            List<CategoryPageBean> categoryPageBeanList = util.collectBean(scene, CategoryPageBean.class);
            categoryPageBeanList.forEach(e -> {
                if (e.getCategoryLevel().equals(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc())) {
                    categoryIdList.add(e.getId());
                }
            });
            IScene newScene = CategoryPageScene.builder().firstCategory(id).secondCategory(categoryIdList.get(0)).build();
            List<CategoryPageBean> newCategoryPageBeanList = util.collectBean(newScene, CategoryPageBean.class);
            newCategoryPageBeanList.forEach(e -> {
                if (e.getCategoryLevel().equals(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc())) {
                    CommonUtil.checkResult(e.getCategoryName() + "的父级品类", util.getCategoryName(categoryIdList.get(0)), e.getParentCategory());
                }
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】根据一级品类和二级品类筛选,结果期待为该品类下的全部三级品类");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = CategoryPageScene.builder().categoryStatus(true).build().invoke(visitor).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Boolean status = obj.getBoolean("category_status");
                String name = obj.getString("category_name");
                CommonUtil.checkResult("商品" + name + "的状态", true, status);
            }
            JSONArray list2 = CategoryPageScene.builder().categoryStatus(false).build().invoke(visitor).getJSONArray("list");
            for (int i = 0; i < list2.size(); i++) {
                JSONObject obj = list2.getJSONObject(i);
                Boolean status = obj.getBoolean("category_status");
                String name = obj.getString("category_name");
                CommonUtil.checkResult("商品" + name + "的状态", false, status);
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】根据品类状态进行筛选");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = CategoryPageScene.builder().categoryStatus(true).build().invoke(visitor).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                int id = obj.getInteger("id");
                Preconditions.checkArgument(obj.containsKey("category_pic"), "品类" + id + "无品类图");
                Preconditions.checkArgument(obj.containsKey("category_name"), "品类" + id + "无品类名称");
                Preconditions.checkArgument(obj.containsKey("category_level"), "品类" + id + "无品类级别");
                Preconditions.checkArgument(obj.containsKey("num"), "品类" + id + "无商品数量");
                Preconditions.checkArgument(obj.containsKey("category_status"), "品类" + id + "无品类状态");
                Preconditions.checkArgument(obj.containsKey("last_modify_time"), "品类" + id + "无最新修改时间");
                Preconditions.checkArgument(obj.containsKey("modify_sale_name"), "品类" + id + "无修改人");
                if (!obj.getString("category_level").equals("一级品类")) {
                    Preconditions.checkArgument(obj.containsKey("parent_category"), "品类" + id + "无上级品类");
                }
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】列表中展示项校验");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] names = {"啊", "12345", "1Aa啊！@#，嗷嗷",};
            Arrays.stream(names).forEach(name -> {
                String picPath = util.getCategoryPicPath();
                Long id = util.createCategory(name, picPath, IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
                //禁用品类
                ChangeStatusScene.builder().id(id).status(false).build().invoke(visitor);
                //编辑品类-更换图片
                String picPath2 = util.getPicPath(FILEPATH_TWO, "1:1");
                EditCategoryScene.builder().id(id).belongPic(picPath2).categoryName(name).categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build().invoke(visitor);
                //编辑品类-不更换图片
                EditCategoryScene.builder().id(id).categoryName(name).categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build().invoke(visitor);
                //删除启用品类
                DeleteCategoryScene.builder().id(id).build().invoke(visitor);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】新建/编辑一级品类，品类名称1/5/10个字");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "12345";
            String picPath = util.getCategoryPicPath();
            //新建一级品类
            Long firstId = util.createCategory(name, picPath, IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //新建二级品类
            Long secondId = util.createCategory(name, picPath, IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), firstId);
            //删除品类
            Long[] ids = {secondId, firstId};
            Arrays.stream(ids).forEach(id -> DeleteCategoryScene.builder().id(id).build().invoke(visitor));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】新建二级品类，与所属一级品类名称相同");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String picPath = util.getCategoryPicPath();
            String[] names = {null, EnumDesc.DESC_10.getDesc() + "1"};
            Arrays.stream(names).forEach(name -> {
                String message = CreateCategoryScene.builder().categoryName(name).belongPic(picPath).categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build().invoke(visitor, false).getString("message");
                String err = name == null ? "品类名称不能为空" : "品类名称需要在1-10个字内";
                CommonUtil.checkResult("品类名称为" + name, err, message);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】新建一级品类，品类名称异常");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long[] belongCategorys = {null, 99999L};
            Arrays.stream(belongCategorys).forEach(belongCategory -> {
                String picPath = util.getCategoryPicPath();
                String message = CreateCategoryScene.builder().belongPic(picPath).categoryLevel(IntegralCategoryTypeEnum.SECOND_CATEGORY.name())
                        .categoryName("奔驰").belongCategory(belongCategory).build().invoke(visitor, false).getString("message");
                String err = belongCategory == null ? "所属品类不能为空" : "所属品类不存在或已关闭";
                CommonUtil.checkResult("所属一级品类不存在时", err, message);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】新建二级品类，所属一级品类不存在");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "奔驰";
            String picPath = util.getCategoryPicPath();
            Long id = util.createCategory(name, picPath, IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            Long secondId = util.createCategory(name, picPath, IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            String message = CreateCategoryScene.builder().categoryName(name).belongPic(picPath).belongCategory(id)
                    .categoryLevel(IntegralCategoryTypeEnum.SECOND_CATEGORY.name())
                    .build().invoke(visitor, false).getString("message");
            String err = "相同品类下，品类名称不能重复";
            CommonUtil.checkResult("一级品类下创建同名的二级品类", err, message);
            //删除品类
            DeleteCategoryScene.builder().id(secondId).build().invoke(visitor);
            DeleteCategoryScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】一级品类下创建同名的二级品类，失败");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "奔驰";
            Long id = util.getCategoryByLevel(IntegralCategoryTypeEnum.SECOND_CATEGORY);
            Long thirdId = util.createCategory(name, util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), id);
            String message = CreateCategoryScene.builder().categoryName(name).belongPic(util.getCategoryPicPath()).belongCategory(id)
                    .categoryLevel(IntegralCategoryTypeEnum.THIRD_CATEGORY.name())
                    .build().invoke(visitor, false).getString("message");
            String err = "相同品类下，品类名称不能重复";
            CommonUtil.checkResult("二级品类下创建同名的三级品类", err, message);
            //删除品类
            DeleteCategoryScene.builder().id(thirdId).build().invoke(visitor);
            DeleteCategoryScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】二级品类下创建同名的三级品类，失败");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "奔驰";
            String message = CreateCategoryScene.builder().categoryName(name).belongPic(util.getCategoryPicPath())
                    .categoryLevel(IntegralCategoryTypeEnum.SECOND_CATEGORY.name())
                    .build().invoke(visitor, false).getString("message");
            String err = "所属品类不能为空";
            CommonUtil.checkResult("创建二级品类不填写所属品类", err, message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】创建二级品类不填写所属品类，失败");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "奔驰";
            String message = CreateCategoryScene.builder().categoryName(name)
                    .categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name())
                    .build().invoke(visitor, false).getString("message");
            String err = "品类图片不能为空";
            CommonUtil.checkResult("创建一级品类不选择logo", err, message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】创建一级品类不选择logo，失败");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建一级品类
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //创建二级品类
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //创建三级品类
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);
            //停用
            String message = ChangeStatusScene.builder().id(id).status(false).build().invoke(visitor, false).getString("message");
            String err = "品类有下级品类，无法修改";
            CommonUtil.checkResult("停用下级未关闭的一级品类", err, message);
            //停用
            String secondMessage = ChangeStatusScene.builder().id(secondId).status(false).build().invoke(visitor, false).getString("message");
            String secondErr = "品类有下级品类，无法修改";
            CommonUtil.checkResult("停用下级未关闭的二级品类", secondErr, secondMessage);
            //clean
            Long[] ids = {thirdId, secondId, id};
            Arrays.stream(ids).forEach(e -> {
                ChangeStatusScene.builder().id(e).status(false).build().invoke(visitor);
                DeleteCategoryScene.builder().id(e).build().invoke(visitor);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】停用有下级品类的品类，失败");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建一级品类
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //创建二级品类
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //创建三级品类
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);
            //删除
            String message = DeleteCategoryScene.builder().id(id).build().invoke(visitor, false).getString("message");
            String err = "该品类有下级品类，不能删除";
            CommonUtil.checkResult("删除含有二级品类的一级品类", err, message);
            //删除
            String secondMessage = DeleteCategoryScene.builder().id(secondId).build().invoke(visitor, false).getString("message");
            String secondErr = "该品类有下级品类，不能删除";
            CommonUtil.checkResult("删除含有三级品类的二级品类", secondErr, secondMessage);
            //clean
            Long[] ids = {thirdId, secondId, id};
            Arrays.stream(ids).forEach(e -> {
                ChangeStatusScene.builder().id(e).status(false).build().invoke(visitor);
                DeleteCategoryScene.builder().id(e).build().invoke(visitor);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】删除有下级品类的品类，失败");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建一级品类
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //创建二级品类
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //创建三级品类
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);

            ChangeStatusScene.builder().id(thirdId).status(false).build().invoke(visitor);
            DeleteCategoryScene.builder().id(thirdId).build().invoke(visitor);

            ChangeStatusScene.builder().id(secondId).status(false).build().invoke(visitor);
            DeleteCategoryScene.builder().id(secondId).build().invoke(visitor);

            ChangeStatusScene.builder().id(id).status(false).build().invoke(visitor);
            DeleteCategoryScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】停用、删除无下级的品类，成功");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建一级品类
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //创建二级品类
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //创建三级品类
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);

            ChangeStatusScene.builder().id(thirdId).status(false).build().invoke(visitor);
            ChangeStatusScene.builder().id(secondId).status(false).build().invoke(visitor);
            ChangeStatusScene.builder().id(id).status(false).build().invoke(visitor);

            DeleteCategoryScene.builder().id(secondId).build().invoke(visitor);
            DeleteCategoryScene.builder().id(id).build().invoke(visitor);
            DeleteCategoryScene.builder().id(thirdId).build().invoke(visitor);

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】停用、删除下级无占用&已关闭的品类，成功");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            Long speId = CreateSpecificationsScene.builder().id(id).specificationsName("规格1号").belongsCategory(id).build().invoke(visitor).getLong("id");
            //删除
            String message = DeleteCategoryScene.builder().id(id).build().invoke(visitor, false).getString("message");
            String err = "该品类有规格绑定，不能删除";
            CommonUtil.checkResult("删除有规格绑定的品类", err, message);
            ChangeSpecificationsStatusScene.builder().id(speId).status(false).build().invoke(visitor);
            DeleteSpecificationsScene.builder().id(speId).build().invoke(visitor);
            DeleteCategoryScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】删除有规格绑定的品类，失败");
        }
    }

    @Test(enabled = false)
    public void goodsCategory_system_18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            Long speId = CreateSpecificationsScene.builder().id(id).specificationsName("规格1号").belongsCategory(id).build().invoke(visitor).getLong("id");
            //停用
            String message = ChangeStatusScene.builder().id(id).status(false).build().invoke(visitor, false).getString("message");
            String err = "该品类有规格绑定，无法修改";
            CommonUtil.checkResult("停用有规格绑定的品类", err, message);
            ChangeSpecificationsStatusScene.builder().id(speId).status(false).build().invoke(visitor);
            DeleteSpecificationsScene.builder().id(speId).build().invoke(visitor);
            DeleteCategoryScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】停用有规格绑定的品类，失败");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建一级品类
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //创建二级品类
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //创建三级品类
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);
            //启用1
            ChangeStatusScene.builder().id(id).status(true).build().invoke(visitor);

            //关闭23
            ChangeStatusScene.builder().id(thirdId).status(false).build().invoke(visitor);
            ChangeStatusScene.builder().id(secondId).status(false).build().invoke(visitor);

            //启用23
            ChangeStatusScene.builder().id(secondId).status(true).build().invoke(visitor);
            ChangeStatusScene.builder().id(thirdId).status(true).build().invoke(visitor);
            //clean
            Long[] ids = {thirdId, secondId, id};
            Arrays.stream(ids).forEach(e -> DeleteCategoryScene.builder().id(e).build().invoke(visitor));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】启用 有上级品类&上级品类状态为启用的品类，期待成功");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_20() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //创建一级品类
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //创建二级品类
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //创建三级品类
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);
            //全部关闭
            Long[] ids = {thirdId, secondId, id};
            Arrays.stream(ids).forEach(e -> ChangeStatusScene.builder().id(e).status(false).build().invoke(visitor));
            //全部开启
            Arrays.stream(ids).forEach(e -> ChangeStatusScene.builder().id(e).status(true).build().invoke(visitor));
            //clean
            Arrays.stream(ids).forEach(e -> DeleteCategoryScene.builder().id(e).build().invoke(visitor));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品类】启用 有上级品类&上级品类状态为停用的品类，成功");
        }
    }

    /**
     * 商品品牌
     */
    //ok
    @Test()
    public void goodsBrand_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = BrandPageScene.builder().build();
            BrandPageBean brandPageBean = util.collectBean(scene, BrandPageBean.class).get(0);
            String name = brandPageBean.getBrandName().substring(0, 1);
            IScene newScene = BrandPageScene.builder().brandName(name).build();
            List<BrandPageBean> brandPageBeans = util.collectBean(newScene, BrandPageBean.class);
            brandPageBeans.forEach(e -> Preconditions.checkArgument(e.getBrandName().contains(name), "搜索结果" + e.getBrandName() + "不包含" + name));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】根据品牌名称搜索");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = BrandPageScene.builder().brandStatus(true).build();
            List<BrandPageBean> brandPageBeans = util.collectBean(scene, BrandPageBean.class);
            brandPageBeans.forEach(e -> Preconditions.checkArgument(e.getBrandStatus(), "搜索结果中" + e.getBrandName() + "的状态为" + e.getBrandStatus()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】搜索状态为启用的品牌");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = BrandPageScene.builder().brandStatus(false).build();
            List<BrandPageBean> brandPageBeans = util.collectBean(scene, BrandPageBean.class);
            brandPageBeans.forEach(e -> Preconditions.checkArgument(!e.getBrandStatus(), "搜索结果中" + e.getBrandName() + "的状态为" + e.getBrandStatus()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】搜索状态为停用的品牌");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = BrandPageScene.builder().size(SIZE).build().invoke(visitor).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                int id = obj.getInteger("id");
                Preconditions.checkArgument(obj.containsKey("brand_pic"), "品牌" + id + "品牌logo");
                Preconditions.checkArgument(obj.containsKey("brand_name"), "品牌" + id + "无品牌名称");
                Preconditions.checkArgument(obj.containsKey("brand_description"), "品牌" + id + "无品牌简介");
                Preconditions.checkArgument(obj.containsKey("brand_status"), "品牌" + id + "无品牌状态");
                Preconditions.checkArgument(obj.containsKey("num"), "品牌" + id + "无商品数量");
                Preconditions.checkArgument(obj.containsKey("last_modify_time"), "品牌" + id + "无最新修改时间");
                Preconditions.checkArgument(obj.containsKey("modify_sale_name"), "品牌" + id + "无修改人");
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】列表中展示项校验");
        }
    }

    //ok
    @Test(dataProvider = "BRAND_ADD")
    public void goodsBrand_system_5(String name, String desc) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = BrandPageScene.builder().build();
            int brandTotal = scene.invoke(visitor).getInteger("total");
            CreateBrandScene.builder().brandPic(util.getCategoryPicPath()).brandName(name).brandDescription(desc).build().invoke(visitor);
            int newBrandTotal = scene.invoke(visitor).getInteger("total");
            CommonUtil.checkResult("商品品牌列表数量", brandTotal + 1, newBrandTotal);
            //判断品牌状态
            BrandPageBean brandPageBean = util.collectBeanByField(scene, BrandPageBean.class, "brand_name", name);
            Long id = brandPageBean.getId();
            Boolean status = brandPageBean.getBrandStatus();
            CommonUtil.checkResult("品牌 " + name + " 的状态", true, status);
            //判断新建商品时品牌下拉列表
            JSONArray list = BrandListScene.builder().build().invoke(visitor).getJSONArray("list");
            List<Long> idList = list.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
            Preconditions.checkArgument(idList.contains(id), "创建商品时的品牌下拉框不包含品牌id" + id);
            DeleteBrandScene.builder().id(id).build().invoke(visitor);
            sleep(1);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】新建品牌");
        }
    }

    @DataProvider(name = "BRAND_ADD")
    public Object[] brandAdd() {
        return new String[][]{
                {EnumDesc.DESC_BETWEEN_1_5.getDesc(), EnumDesc.DESC_BETWEEN_1_5.getDesc()},
                {EnumDesc.DESC_BETWEEN_5_10.getDesc(), EnumDesc.DESC_BETWEEN_5_10.getDesc()},
                {EnumDesc.DESC_20.getDesc(), EnumDesc.DESC_BETWEEN_40_50.getDesc() + "1234"},
        };
    }

    //ok
    @Test()
    public void goodsBrand_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] names = {EnumDesc.DESC_20.getDesc() + "1", null};
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
            Long goodsId = goodsBean.getGoodsId();
            //上架&下架
            IScene goodsManagePageScene = GoodsManagePageScene.builder().build();
            Arrays.stream(CommodityStatusEnum.values()).forEach(anEnum -> {
                ChangeGoodsStatusScene.builder().id(goodsId).status(anEnum.name()).build().invoke(visitor);
                List<GoodsManagePageBean> goodsManagePageBeanList = util.collectBean(goodsManagePageScene, GoodsManagePageBean.class);
                goodsManagePageBeanList.stream().filter(e -> e.getId().equals(goodsId)).forEach(e -> {
                    CommonUtil.checkResult(e.getGoodsName() + "的商品状态", anEnum.getName(), e.getGoodsStatusName());
                    CommonUtil.checkResult(e.getGoodsName() + "的商品状态", anEnum.name(), e.getGoodsStatus());
                });
                if (anEnum.equals(CommodityStatusEnum.DOWN)) {
                    IScene scene = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build();
                    util.collectBean(scene, GoodsManagePageBean.class).forEach(e -> Preconditions.checkArgument(!e.getId().equals(goodsId), "下架的商品" + e.getGoodsName() + "在创建积分兑换时可见"));
                }
            });
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
