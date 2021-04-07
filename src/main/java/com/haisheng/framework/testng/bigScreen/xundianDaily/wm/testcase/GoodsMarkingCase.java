package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.testcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.BrandPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.CategoryPageBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralCategoryTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VoucherStatusEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.voucher.VoucherGenerator;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.pcCreateGoods;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralcenter.ChangeSwitchStatusScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralmall.*;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.InsInfo;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.ImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lxq
 * @date :  2020/11/24
 */

public class GoodsMarkingCase extends TestCaseCommon implements TestCaseStd {
    private final static EnumTestProduce PRODUCE = EnumTestProduce.INS_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    ScenarioUtil ins = ScenarioUtil.getInstance();
    InsInfo info = new InsInfo(visitor);
    private final static String FILEPATH = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
    private final static String FILEPATH_TWO = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/goods/联想.jpg";
    private final static Integer SIZE = 100;

    @BeforeClass
    @Override
    public void initial() {
        CommonConfig commonConfig = new CommonConfig();
        logger.debug("before classs initial");
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
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
        user.loginPc(EnumAccount.YUE_XIU_DAILY);
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
            String[] names = {"", null, EnumDesc.DESC_10.getDesc() + "1"};
            Arrays.stream(names).forEach(name -> {
                String message = CreateCategoryScene.builder().categoryName(name).belongPic(picPath).categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build().invoke(visitor, false).getString("message");
                String err = StringUtils.isEmpty(name) ? "品类名称不能为空" : "品类名称需要在1-10个字内";
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
            saveData("PC【商品品类】启用 有上级品类&上级品类状态为停用的品类，期待成功");
        }
    }

    /**
     * 商品品牌
     */
    @Test(dataProvider = "BRAND_NAME")
    public void goodsBrand_system_1(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = BrandPageScene.builder().brandName(name).build();
            List<BrandPageBean> brandPageBeans = util.collectBean(scene, BrandPageBean.class);
            brandPageBeans.forEach(e -> Preconditions.checkArgument(e.getBrandName().contains(name), "搜索结果" + e.getBrandName() + "不包含" + name));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】根据品牌名称搜索");
        }
    }

    @DataProvider(name = "BRAND_NAME")
    public Object[] brandName() {
        return new String[]{
                "1",
//                "aA",
//                "!@#$%^&*(-",
//                "自动化",
//                "之家",
//                "测试",
//                "2021",
        };
    }

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
            sleep(100);
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

    @Test()
    public void goodsBrand_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] names = {EnumDesc.DESC_20.getDesc() + "1", "", null};
            Arrays.stream(names).forEach(name -> {
                String message = CreateBrandScene.builder().brandPic(util.getCategoryPicPath()).brandName(name).brandDescription(EnumDesc.DESC_BETWEEN_5_10.getDesc()).build().invoke(visitor, false).getString("message");
                String err = StringUtils.isEmpty(name) ? "" : "";
                CommonUtil.checkResult("品牌名称为" + name, err, message);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】新建品牌");
        }
    }

    @Test
    public void goodsBrand_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = EnumDesc.DESC_BETWEEN_5_10.getDesc() + "重复";
            Long id = CreateBrandScene.builder().brandName(name).brandDescription(name).brandPic(util.getCategoryPicPath()).build().invoke(visitor).getLong("id");
            String message = CreateBrandScene.builder().brandName(name).brandDescription(name).brandPic(util.getCategoryPicPath()).build().invoke(visitor, false).getString("message");
            String err = "";
            CommonUtil.checkResult("创建重复名称的品牌", err, message);
            DeleteBrandScene.builder().id(id).build().invoke(visitor);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品品牌】新建品牌,品牌名称与已存在的重复");
        }
    }

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

    @Test
    public void specificationsDel1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id = info.newSpecificition();


            //删除启用的规格，应失败（ 前端限制，这里注释掉）
//            jc.specificationsChgStatus(id,true,true);
//            int code1 = jc.specificationsDel(id,false).getInteger("code");
//            Preconditions.checkArgument(code1==1001,"删除启用的规格，期待失败实际"+code1);
            //删除停用的规格，应成功
            ins.specificationsChgStatus(id, false);
            int code2 = ins.specificationsDel(id, false).getInteger("code");
            Preconditions.checkArgument(code2 == 1000, "删除停用的规格，期待成功实际" + code2);

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品规格】删除各种状态的&无商品使用的规格");
        }
    }


    //商品管理
    //2021-01-27
    @Test(dataProvider = "BRANDNAME")
    public void goodFilter1(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.goodsManagePage(1, 50, name, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("goods_name");
                Preconditions.checkArgument(searchname.contains(name), "搜索" + name + ", 结果中包含" + searchname);
            }

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品名称搜索");
        }
    }


    @Test
    public void goodFilter2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //todo
            JSONArray list = ins.BrandList().getJSONArray("list");
            if (list.size() > 0) {
                Long id = list.getJSONObject(0).getLong("id");
                String brandname = list.getJSONObject(0).getString("brand_name");
                JSONArray goodlist = ins.goodsManagePage(1, 10, null, id, null, null, null, null).getJSONArray("list");
                for (int i = 0; i < goodlist.size(); i++) {
                    JSONObject obj = goodlist.getJSONObject(i);
                    String search = obj.getString("belongs_brand");
                    Preconditions.checkArgument(search.equals(brandname), "搜索" + brandname + ", 结果包含" + search);
                }
            }
            //要去下拉框的接口
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品品牌搜索");
        }
    }

    @Test(dataProvider = "Status")
    public void goodFilter3(String status, String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray goodlist = ins.goodsManagePage(1, 10, null, null, status, null, null, null).getJSONArray("list");
            for (int i = 0; i < goodlist.size(); i++) {
                JSONObject obj = goodlist.getJSONObject(i);
                String search = obj.getString("goods_status_name");
                Preconditions.checkArgument(search.equals(name), "搜索" + name + ", 结果包含" + search);
            }

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】根据筛选栏-商品状态搜索");
        }
    }

    @DataProvider(name = "Status")
    public Object[] searchStatus() {
        return new String[][]{
                {"UP", "上架"},
                {"DOWN", "下架"},
        };
    }

    //@Test
    public void goodFilter4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = ins.categoryTree().getJSONArray("list");
            if (list.size() > 0) {
                int id = list.getJSONObject(0).getInteger("category_id");

                //todo 没补充完
            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】根据一级品类搜索");
        }
    }

    //@Test
    public void goodShow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.goodsManagePage(1, 50, null, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.containsKey("goods_pic"), "未展示商品图片");
                Preconditions.checkArgument(obj.containsKey("goods_name"), "未展示商品名称");
                Preconditions.checkArgument(obj.containsKey("belongs_brand"), "未展示所属品牌");
                Preconditions.checkArgument(obj.containsKey("goods_category"), "未展示商品分类");
                Preconditions.checkArgument(obj.containsKey("price"), "未展示市场价");
                Preconditions.checkArgument(obj.containsKey("goods_status"), "未展示商品状态");
            }

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】列表展示项校验");
        }
    }

    // @Test
    public void goodUp() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            ins.goodsChgStatus(info.goods_id, "true", true); //启用商品
            //查看商品列表该商品状态
            JSONArray list = ins.goodsManagePage(1, 10, info.goods_name, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                if (obj.getLong("id") == info.goods_id) {
                    Preconditions.checkArgument(obj.getString("goods_status_name").equals("启用"), "启用后列表商品状态不是启用");
                }
            }
            //查看小程序是否有该商品

            JSONArray appletlist = ins.appletMallCommidityList(100, null, null, null, true).getJSONArray("list");
            Boolean isexist = false;
//            for (int j = 0 ; j < appletlist.size();j++){
//                JSONObject obj = list.getJSONObject(j);
//                if (obj.getInteger("id")==info.goods_id){
//                    isexist = true;
//                    break;
//                }
//            }
            Preconditions.checkArgument(isexist == true, "小程序未展示该商品");

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】上架存在的商品");
        }
    }

    //@Test
    public void goodUp2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = ins.goodsChgStatus(info.goods_id, "true", false).getInteger("code"); //启用商品

            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】A删除商品，B不刷新页面启用商品");
        }
    }

    //@Test
    public void goodDown() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            ins.goodsChgStatus(info.goods_id, "false", true); //启用商品
            //查看商品列表该商品状态
            JSONArray list = ins.goodsManagePage(1, 10, info.goods_name, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                if (obj.getLong("id") == info.goods_id) {
                    Preconditions.checkArgument(obj.getString("goods_status_name").equals("下架"), "下架后列表商品状态不是停用");
                }
            }
//            //查看小程序是否有该商品
//
//            JSONArray appletlist = jc.appletMallCommidityList(100,null,null,null,true).getJSONArray("list");
//            Boolean isexist = false;
//            for (int j = 0 ; j < appletlist.size();j++){
//                JSONObject obj = list.getJSONObject(j);
//                if (obj.getInteger("id")==info.goods_id){
//                    isexist = true;
//                    break;
//                }
//            }
//            Preconditions.checkArgument(isexist==true,"小程序展示了该商品");

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】下架商品");
        }
    }

    @Test
    public void goodEdit1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            pcCreateGoods er = new pcCreateGoods();
            er.checkcode = false;
            er.id = 9999L;

            int code = ins.editGoodMethod(er).getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码期待1001，实际" + code);

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】编辑中的商品被删除");
        }
    }


    @Test(dataProvider = "GOOD")
    public void goodAdd(String goodname, String goodDesc, String gooddetail, String price, String spename, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一个一二三级品类
            String name = "品类";
            JSONObject obj = info.newFirstCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)));
            String idone = obj.getString("id");
            JSONObject objtwo = info.newSecondCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idone);
            String idtwo = objtwo.getString("id");
            JSONObject objthree = info.newThirdCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idtwo);
            String idthree = objthree.getString("id");

            //新建规格

            String speItemName = "规格参数" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));

            //新建品牌
            String brandid = info.newGoodBrand("pp" + System.currentTimeMillis(), "pp说明" + System.currentTimeMillis()).getString("id");

            Long speId = ins.specificationsCreate(spename, Long.parseLong(idone), null, null, true).getLong("id");
            //新建规格参数
            JSONObject objItem = new JSONObject();
            objItem.put("specifications_item", speItemName);
            JSONArray spearray = new JSONArray();
            spearray.add(objItem);
            ins.specificationsEdit(spename, Long.parseLong(idone), spearray, speId, true);
            Long speItemId = ins.specificationsDetail(speId, 1, 10).getJSONArray("specifications_list").getJSONObject(0).getLong("specifications_id");

            //新建商品


            pcCreateGoods good = new pcCreateGoods();
            good.first_category = Long.parseLong(idone);
            good.second_category = Long.parseLong(idtwo);
            good.third_category = Long.parseLong(idthree);
            good.goods_brand = Long.parseLong(brandid);

            //新建商品
            String select_specifications_str =
                    "[" +
                            "{" +
                            "\"specifications_id\":" + speId + "," +
                            "\"specifications_name\":\"" + spename + "\"," +
                            "\"specifications_list\":[" +
                            "{\"specifications_detail_id\":" + speItemId + "," +
                            "\"specifications_detail_name\":\"" + speItemName + "\"" +
                            "}]}]";
            JSONArray select_specifications = JSONArray.parseArray(select_specifications_str); //所选规格
            String goods_specifications_list_str = "[" +
                    "{" +
                    "\"first_specifications\":" + speItemId + "," +
                    "\"first_specifications_name\":\"" + speItemName + "\"," +

                    "\"head_pic\":\"" + info.getLogo() + "\"," +
                    "\"price\":69.98" +
                    "}]";
            JSONArray goods_specifications_list = JSONArray.parseArray(goods_specifications_list_str);
            good.select_specifications = select_specifications;
            good.goods_specifications_list = goods_specifications_list;
            good.checkcode = false;
            good.goods_name = goodname;
            good.goods_description = goodDesc;
            good.price = price;
            good.goods_detail = gooddetail;
            good.goods_pic_list = good.getPicone();
            JSONObject objnew = ins.createGoodMethod(good);
            int code = objnew.getInteger("code");
            Preconditions.checkArgument(code == 1000, mess + "状态码" + code);
            Long goodid = objnew.getJSONObject("data").getLong("id");


            //删除商品
            ins.deleteGoodMethod(goodid);
            //关闭->删除规格
            ins.specificationsChgStatus(speId, false);
            ins.specificationsDel(speId);
            //删除品类
            ins.categoryDel(Long.parseLong(idthree), true);
            ins.categoryDel(Long.parseLong(idtwo), true);
            ins.categoryDel(Long.parseLong(idone), true);


        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】创建商品,一个规格一个参数");
        }
    }

    @DataProvider(name = "GOOD")
    public Object[] good() { //商品名称 商品描述  商品详情 市场价 规格名称 描述
        return new String[][]{
                {"a", "a", "123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂", "0.00", "a", "商品名称1个字 商品描述1个字 市场价0.00 规格名称1个字"},
                {"12345Q~!a啊不嘈杂啊67890", "12345Q~!a啊不嘈杂啊6789012345Q~!a啊不嘈杂啊67890", "123456789sdxf123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂123456789sdxfcghvjbknlm,@#$%^&*JHGFDs事事顺遂cghvjbknlm,@#$%^&*JHGFDs事事顺遂", "100000000.00", "a啊123～！@90", "商品名称20个字 商品描述40个字 市场价100000000.00 规格名称10个字"},
                {"12Q~!a啊不嘈", "12Q~!a啊不嘈", "1", "99.99", "a啊23～", "商品名称10个字 商品描述10个字 市场价99.99 规格名称5个字"},

        };
    }

    @Test
    public void goodAddMore() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //新建一个一二三级品类
            String name = "品类";
            JSONObject obj = info.newFirstCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)));
            String idone = obj.getString("id");
            JSONObject objtwo = info.newSecondCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idone);
            String idtwo = objtwo.getString("id");
            JSONObject objthree = info.newThirdCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idtwo);
            String idthree = objthree.getString("id");


            //新建品牌
            String brandid = info.newGoodBrand("pp" + System.currentTimeMillis(), "pp说明" + System.currentTimeMillis()).getString("id");

            //新建规格
            String spename1 = "1规格" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));
            String spename2 = "2规格" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));

            Long speId = ins.specificationsCreate(spename1, Long.parseLong(idone), null, null, true).getLong("id"); //第一个规格 有10个参数
            Long speId2 = ins.specificationsCreate(spename2, Long.parseLong(idone), null, null, true).getLong("id"); //第二个规格 有1个参数
            //新建规格1的10个参数
            JSONArray spearray = new JSONArray();
            for (int i = 1; i < 11; i++) {
                JSONObject objItem = new JSONObject();
                objItem.put("specifications_item", "speItemName" + i);
                spearray.add(objItem);
            }

            ins.specificationsEdit(spename1, Long.parseLong(idone), spearray, speId, true);
            Long speItemId = ins.specificationsDetail(speId, 1, 10).getJSONArray("specifications_list").getJSONObject(0).getLong("specifications_id");

            //新建规格2的一个参数
            JSONObject objItem1 = new JSONObject();
            objItem1.put("specifications_item", "speItemName11");
            JSONArray spearray2 = new JSONArray();
            spearray2.add(objItem1);
            ins.specificationsEdit(spename2, Long.parseLong(idone), spearray2, speId2, true);
            Long speItemId2 = ins.specificationsDetail(speId2, 1, 10).getJSONArray("specifications_list").getJSONObject(0).getLong("specifications_id");

            //新建商品

            pcCreateGoods good = new pcCreateGoods();
            good.first_category = Long.parseLong(idone);
            good.second_category = Long.parseLong(idtwo);
            good.third_category = Long.parseLong(idthree);
            good.goods_brand = Long.parseLong(brandid);

            //新建商品

            JSONArray select_specifications = new JSONArray();

            JSONObject spe1obj = new JSONObject();
            spe1obj.put("specifications_id", speId);
            spe1obj.put("specifications_name", spename1);
            //规格1的信息
            JSONArray spe1list = new JSONArray();
            for (int i = 0; i < 10; i++) {
                JSONObject speclistobj = ins.specificationsDetail(speId, 1, 10).getJSONArray("specifications_list").getJSONObject(i);
                Long specifications_id = speclistobj.getLong("specifications_id");
                String specifications_item = speclistobj.getString("specifications_item");
                JSONObject everyspeclist = new JSONObject();
                everyspeclist.put("specifications_detail_id", specifications_id);
                everyspeclist.put("specifications_detail_name", specifications_item);
                spe1list.add(everyspeclist);
            }
            spe1obj.put("specifications_list", spe1list);
            //规格2的信息
            JSONObject spe2obj = new JSONObject();
            spe2obj.put("specifications_id", speId2);
            spe2obj.put("specifications_name", spename2);

            JSONArray spe2list = new JSONArray();
            JSONObject everyspeclist = new JSONObject();
            everyspeclist.put("specifications_detail_id", speItemId2);
            everyspeclist.put("specifications_detail_name", "speItemName11");
            spe2list.add(everyspeclist);
            spe2obj.put("specifications_list", spe2list);

            select_specifications.add(spe1obj);
            select_specifications.add(spe2obj);


            //参数组合
            JSONArray goods_specifications_list = new JSONArray();
            for (int j = 0; j < 10; j++) {
                JSONObject speclistobj = ins.specificationsDetail(speId, 1, 10).getJSONArray("specifications_list").getJSONObject(j);
                Long specifications_id = speclistobj.getLong("specifications_id");
                String specifications_item = speclistobj.getString("specifications_item");
                JSONObject everygoodspeclist = new JSONObject();
                everygoodspeclist.put("first_specifications", specifications_id);
                everygoodspeclist.put("first_specifications_name", specifications_item);
                everygoodspeclist.put("second_specifications", speItemId2);
                everygoodspeclist.put("second_specifications_name", "speItemName11");
                goods_specifications_list.add(everygoodspeclist);
            }


            good.select_specifications = select_specifications;
            good.goods_specifications_list = goods_specifications_list;
            good.checkcode = false;

            JSONObject objnew = ins.createGoodMethod(good);
            int code = objnew.getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码" + code);
            Long goodid = objnew.getJSONObject("data").getLong("id");


            //删除商品
            ins.deleteGoodMethod(goodid);
            //关闭->删除规格
            ins.specificationsChgStatus(speId, false);
            ins.specificationsDel(speId);
            ins.specificationsChgStatus(speId2, false);
            ins.specificationsDel(speId2);
            //删除品类
            ins.categoryDel(Long.parseLong(idthree), true);
            ins.categoryDel(Long.parseLong(idtwo), true);
            ins.categoryDel(Long.parseLong(idone), true);


        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【商品管理】创建商品,多个规格多参数");
        }
    }


    /**
     * 积分中心
     */

    @Test(dataProvider = "exchangeType")
    public void exchangeFilter1(String type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.exchangePage(1, 50, null, type, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.getString("exchange_type").equals(type), "结果包含" + obj.getString("exchange_type_name"));
            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分兑换】根据积分兑换类型筛选");
        }
    }

    @DataProvider(name = "exchangeType")
    public Object[] exchangeType() {
        return new String[]{
                "FICTITIOUS",// 虚拟
                "REAL", // 实物
        };
    }

    @Test(dataProvider = "exchangeStatus")
    public void exchangeFilter2(String status) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.exchangePage(1, 50, null, null, status).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.getString("status").equals(status), "结果包含" + obj.getString("status_name"));
            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分兑换】根据状态筛选");
        }
    }

    @DataProvider(name = "exchangeStatus")
    public Object[] exchangeStatus() {
        return new String[]{
                "NOT_START",//未开始
                "WORKING", // 进行中
                "CLOSE", // 已关闭
                "EXPIRED", // 已过期

        };
    }

    @Test(dataProvider = "BRANDNAME")
    public void exchangeFilter3(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.exchangePage(1, 50, name, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.getString("goods_name").contains(name), "结果包含" + obj.getString("goods_name"));
            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分兑换】根据商品名称筛选");
        }
    }


    @Test
    public void exchangeDetailFilter1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = ins.exchangeDetail(1, 1, null, null, null, null, null).getJSONArray("list").getJSONObject(0).getString("exchange_customer_name");
            JSONArray list = ins.exchangeDetail(1, 50, null, name, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("exchange_customer_name");
                Preconditions.checkArgument(searchname.contains(name), "搜索结果包含" + searchname);
            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分明细】根据存在的兑换客户全称筛选");
        }
    }

    @Test(dataProvider = "exchange_type")
    public void exchangeDetailFilter2(String exchange_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.exchangeDetail(1, 50, null, null, exchange_type, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchname = obj.getString("exchange_type");
                Preconditions.checkArgument(searchname.equals(exchange_type), "搜索结果包含" + searchname);
            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分明细】根据存在的兑换类型筛选");
        }
    }

    @DataProvider(name = "exchange_type")
    public Object[] exchange_type() {
        return new String[]{
                "ADD",
                "MINUS",

        };
    }

    @Test(dataProvider = "exchange_time")
    public void exchangeDetailFilter3(String start, String end) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.exchangeDetail(1, 50, null, null, null, start, end).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchtime = obj.getString("operate_time");
                Preconditions.checkArgument(searchtime.contains(start), "搜索" + start + "，结果包含" + searchtime);

            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分明细】根据兑换时间筛选");
        }
    }

    @DataProvider(name = "exchange_time")
    public Object[] exchange_time() {
        return new String[][]{
                {dt.getHistoryDate(0), dt.getHistoryDate(0)},
                {dt.getHistoryDate(-2), dt.getHistoryDate(-2)},

        };
    }

    @Test
    public void exchangeDetailShow() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.exchangeDetail(1, 50, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.containsKey("exchange_customer_name"), obj.getString("phone") + "未展示客户名称");
                Preconditions.checkArgument(obj.containsKey("phone"), obj.getString("exchange_customer_name") + "未展示客户手机号");
                Preconditions.checkArgument(obj.containsKey("exchange_type"), obj.getString("exchange_customer_name") + "未展示兑换类型");
                Preconditions.checkArgument(obj.containsKey("stock_detail"), obj.getString("exchange_customer_name") + "未展示库存明细");
                Preconditions.checkArgument(obj.containsKey("operate_time"), obj.getString("exchange_customer_name") + "未展示操作时间");
            }

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分明细】列表展示项校验");
        }
    }

    @Test
    public void exchangeDetailOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List time = new ArrayList();


            JSONArray list = ins.exchangeDetail(1, 50, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String date = obj.getString("operate_time") + ":000";
                time.add(dt.dateToTimestamp1(date));
            }
            List timecopy = time;
            Collections.sort(time);
            Preconditions.checkArgument(Iterators.elementsEqual(time.iterator(), timecopy.iterator()), "未倒序排列");


        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分明细】列表根据兑换时间倒序排列");
        }
    }

    //2021-02-02
    @Test
    public void exchangeOrder() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List time = new ArrayList();


            JSONArray list = ins.exchangeOrder(1, 50, null, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String date = obj.getString("exchange_time") + ":000";
                time.add(dt.dateToTimestamp1(date));
            }
            List timecopy = time;
            Collections.sort(time);
            Preconditions.checkArgument(Iterators.elementsEqual(time.iterator(), timecopy.iterator()), "未倒序排列");


        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】列表根据兑换时间倒序排列");
        }
    }

    @Test(dataProvider = "BRANDNAME")
    public void exchangeOrderFilter1(String orderid) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            List time = new ArrayList();


            JSONArray list = ins.exchangeOrder(1, 50, orderid, null, null, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String search = obj.getString("order_id");
                Preconditions.checkArgument(search.contains(orderid), "搜索" + orderid + ",结果包含" + search);
            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】根据订单ID筛选");
        }
    }

    @Test(dataProvider = "BRANDNAME")
    public void exchangeOrderFilter2(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.exchangeOrder(1, 50, null, null, null, null, name, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String search = obj.getString("member_name");
                Preconditions.checkArgument(search.contains(name), "搜索" + name + ",结果包含" + search);
            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】根据会员名称筛选");
        }
    }

    @Test(dataProvider = "BRANDNAME")
    public void exchangeOrderFilter3(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.exchangeOrder(1, 50, null, null, null, null, null, name).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String search = obj.getString("goods_name");
                Preconditions.checkArgument(search.contains(name), "搜索" + name + ",结果包含" + search);
            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】根据商品名称筛选");
        }
    }

    @Test(dataProvider = "exchange_time")
    public void exchangeOrderFilter4(String start, String end) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.exchangeOrder(1, 50, null, start, end, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchtime = obj.getString("exchange_time");
                Preconditions.checkArgument(searchtime.contains(start), "搜索" + start + "，结果包含" + searchtime);

            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】根据兑换时间筛选");
        }
    }

    @Test(dataProvider = "exchangeOrderStatus")
    public void exchangeOrderFilter5(String status, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = ins.exchangeOrder(1, 50, null, null, null, status, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                String searchtime = obj.getString("order_status_name");
                Preconditions.checkArgument(searchtime.equals(mess), "搜索" + mess + "，结果包含" + searchtime);

            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC【积分订单】根据状态筛选");
        }
    }

    @DataProvider(name = "exchangeOrderStatus")
    public Object[] exchangeOrderStatus() {
        return new String[][]{
                {"WAITING", "待发货"},
                {"CANCELED", "已取消"},
                {"SEND", "待收货"},
                {"FINISHED", "已完成"},


        };
    }


//    @Test
//    public void newFictitiousAndBuy() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            /**
//             * 步骤一 新建虚拟积分商品 兑换次数限制=1
//             */
//            //新建虚拟商品前记录数据
//            int PCtotalPre = jc.exchangePage(1,1,null,null,null).getInteger("total");
//            jc.appletLoginToken(pp.appletTocken);
//            int applettotalPre = jc.appletMallCommidityList(1,null,null,null,null).getInteger("total");
//
//            //新建虚拟积分商品 兑换次数限制=1
//            jc.pcLogin("15711300001","000000");
//            Long fictitiousId = info.newFictitious();
//
//            //PC积分兑换列表+1
//            //小程序积分商城 积分兑换商品+1
//            int PCtotalAft = jc.exchangePage(1,1,null,null,null).getInteger("total");
//            jc.appletLoginToken(pp.appletTocken);
//            int applettotalAft = jc.appletMallCommidityList(1,null,null,null,null).getInteger("total");
//
//            Preconditions.checkArgument(PCtotalAft-PCtotalPre == 1, "新建虚拟积分商品后，PC积分兑换未增加1" );
//            Preconditions.checkArgument(applettotalAft-applettotalPre == 1, "新建虚拟积分商品后，小程序积分商城的积分兑换商品未增加1" );
//
//
//            /**
//             * 步骤二 小程序兑换
//             */
//
//            info.appletBuyFictitious(fictitiousId);
//
//            /**
//             * 步骤三 小程序再次兑换
//             */
//            //小程序再次兑换 应失败
//            int code = jc.appletSubmitExchange(fictitiousId,false).getInteger("code");
//            Preconditions.checkArgument(code==1001,"期待兑换失败1001，实际"+code);
//
//
//            jc.pcLogin("15711300001","000000");
//        } catch (AssertionError e) {
//            collectMessage(e);
//        } catch (Exception e) {
//            collectMessage(e);
//        } finally {
//            saveData("PC新建虚拟商品->小程序兑换");
//        }
//    }
//
//    @Test
//    public void newRealAndBuy() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            /**
//             * 步骤一 新建品牌、一二三级品类、规格、商品
//             */
//
//            //新建品牌
//            Long brandID = System.currentTimeMillis();
//            jc.BrandCreat(true,brandID,"name"+brandID,"desc"+brandID,info.getLogo());
//
//            //新建一级品类
//            Long firstid = System.currentTimeMillis();
//            String firstname = "F"+Integer.toString((int)((Math.random()*9+1)*1000));
//            jc.categoryCreate(true,firstname,"一级品类",null,info.getLogo(),firstid);
//            jc.categoryChgStatus(firstid,true);
//
//            //新建二级品类
//            Long secondid = System.currentTimeMillis();
//            String secondname = "S"+Integer.toString((int)((Math.random()*9+1)*1000));
//            jc.categoryCreate(true,secondname,"二级品类",Long.toString(firstid),info.getLogo(),secondid);
//            jc.categoryChgStatus(secondid,true);
//
//            //新建三级品类
//            Long thirdid = System.currentTimeMillis();
//            String thirdname = "T"+Integer.toString((int)((Math.random()*9+1)*1000));
//            jc.categoryCreate(true,thirdname,"三级品类",Long.toString(secondid),info.getLogo(),thirdid);
//            jc.categoryChgStatus(thirdid,true);
//
//            //新建规格
//            Long speID = System.currentTimeMillis();
//            String speName = "规格" + Integer.toString((int)((Math.random()*9+1)*1000));
//            String specifications_list_str = "[" +
//                    "{" +
//                    "\"specifications_item\":\""+dt.getHistoryDate(0) +"\"," +
//                    "\"num\":100" +
//                    "}" +
//                    "]";
//            JSONArray specifications_list = JSONArray.parseArray(specifications_list_str);
//            Long specifications_detail_id = 1L;// ??
//            jc.specificationsCreate(speName,firstid,specifications_list,speID,true);
//
//            //新建商品
//            String select_specifications_str =
//                    "[" +
//                            "{" +
//                            "\"specifications_id\":"+ speID+","+
//                            "\"specifications_name\":\""+speName +"\","+
//                            "\"specifications_list\":[" +
//                            "\"specifications_detail_id\":"+specifications_detail_id +"\","+
//                            "\"specifications_detail_name\":\""+dt.getHistoryDate(0)+"\""+
//                            "}]}]";
//            JSONArray select_specifications = JSONArray.parseArray(select_specifications_str); //所选规格
//            String goods_specifications_list_str = "[" +
//                    "{" +
//                    "\"first_specifications\":"+specifications_detail_id+"," +
//                    //"\"second_specifications\":"+null+",\n" +
//                    "\"head_pic\":\""+info.getLogo()+"\"," +
//                    "\"price\":69.98" +
//                    "}]";
//            JSONArray goods_specifications_list = JSONArray.parseArray(goods_specifications_list_str);
//            pcCreateGoods goods = new pcCreateGoods();
//            goods.id = System.currentTimeMillis();
//            goods.price = "99.99";
//            goods.select_specifications = select_specifications;
//            goods.goods_specifications_list = goods_specifications_list;
//            jc.createGoodMethod(goods);
//
//
//            /**
//             * 步骤二 新建实体积分商品 兑换次数不限
//             */
//
//            JSONArray specification_list = new JSONArray(); // 内容要补充
//            pcCreateExchangeGoods ex = new pcCreateExchangeGoods();
//            ex.chkcode=true;
//            ex.id  = System.currentTimeMillis();
//            ex.exchange_goods_type = "REAL";
//            ex.goods_id = goods.id;
//            ex.is_limit=false;
//            ex.specification_list = specification_list;
//            jc.exchangeGoodCreat(ex);
//
//
//            /**
//             * 步骤三 小程序【积分商城】兑换
//             */
//            submitOrder or = new submitOrder();
//            or.commodity_id = ex.id;
//            or.specification_id = specifications_detail_id;
//            or.buyer_message = "自动化"+System.currentTimeMillis();
//            or.commodity_num = 1;
//            or.district_code = info.district_code;
//            or.address= "zdh北京市海淀区";
//            or.receiver = "zdh";
//            or.receive_phone= "13400000001";
//            jc.appletSubmitOrder(or);
//
//            //查询订单id
//            Long order_id = jc.exchangeOrder(1,1,null,null,null,null,null,null).getJSONArray("list").getJSONObject(0).getLong("id");
//
//            /**
//             * 步骤四 PC发货
//             */
//            jc.confirmShipment(order_id,String.valueOf(System.currentTimeMillis()),true);
//
//            /**
//             * 步骤五 小程序【积分兑换订单】再次兑换
//             */
//            jc.appletSubmitOrder(or);
//
//
//        } catch (AssertionError e) {
//            collectMessage(e);
//        } catch (Exception e) {
//            collectMessage(e);
//        } finally {
//            saveData("PC新建实体商品积分兑换-> 小程序兑换-> 发货");
//        }
//    }


    @Test(dataProvider = "export")
    public void ExportAll(String url, String mess) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            //导出
            int code = ins.recExport(url).getInteger("code");
            Preconditions.checkArgument(code == 1000, mess + "导出状态码为" + code);
            Thread.sleep(800);
            String status = ins.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), mess + " " + status);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("导出");
        }
    }

    @DataProvider(name = "export")
    public Object[] export() {
        return new String[][]{ // 单弄 、优惠券变更记录、作废记录、增发记录、领取记录、核销记录、活动报名记录、车系列表、车型列表
                {"/jiaochen/pc/reception-manage/record/export", "接待管理"},
                {"/jiaochen/pc/customer-manage/pre-sale-customer/page/export", "销售客户"},
                {"/jiaochen/pc/customer-manage/after-sale-customer/page/export", "售后客户"},
                {"/jiaochen/pc/customer-manage/wechat-customer/page/export", "小程序客户"},
                {"/jiaochen/pc/appointment-manage/record/export", "预约记录"},
                {"/jiaochen/pc/manage/evaluate/export", "评价列表"},
                {"/jiaochen/pc/voucher-manage/voucher-form/export", "优惠券管理"},
                {"/jiaochen/pc/voucher-manage/verification-people/export", "核销人员"},
                {"/jiaochen/pc/package-manage/buy-package-record/export", "套餐购买记录"},
                {"/jiaochen/pc/operation/article/export", "文章列表"},
                {"/jiaochen/pc/activity/manage/export", "活动列表"},
                {"/jiaochen/pc/voucher/apply/export", "优惠券申请"},
                {"/jiaochen/pc/shop/export", "门店管理"},
                {"/jiaochen/pc/brand/export", "品牌管理"},
                {"/jiaochen/pc/role/export", "角色管理"},
                {"/jiaochen/pc/staff/export", "员工管理"},
                {"/jiaochen/pc/record/import-record/export", "导入记录"},
                {"/jiaochen/pc/record/export-record/export", "导出记录"},
                {"/jiaochen/pc/record/push-msg/export", "消息记录"},
                {"/jiaochen/pc/manage/rescue/export", "在线救援"},
                {"/jiaochen/pc/vip-marketing/wash-car-manager/export", "洗车管理"},
                {"/jiaochen/pc/vip-marketing/wash-car-manager/adjust-number/export", "调整次数"},
                {"/jiaochen/pc/vip-marketing/sign_in_config/change-record/export", "签到积分变更记录"},
                {"/jiaochen/pc/integral-center/exchange/export", "积分兑换"},
                {"/jiaochen/pc/integral-center/exchange-detail/export", "积分明细"},
                {"/jiaochen/pc/integral-center/exchange-order/export", "积分订单"},
                {"/jiaochen/pc/integral-mall/goods-manage/export", "商品管理"},
                {"/jiaochen/pc/manage/maintain/car-model/export", "保养配置"},

        };
    }

    @Test
    public void Exportweixiu() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONObject obj = ins.afterSleCustomerManage("1", "10").getJSONArray("list").getJSONObject(0);
            String carid = obj.getString("car_id");
            String shopid = obj.getString("shop_id");

            //导出
            int code = ins.weixiuExport(carid, shopid).getInteger("code");
            Preconditions.checkArgument(code == 1000, "状态码为" + code);
            Thread.sleep(800);
            String status = ins.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("导出维修记录");
        }
    }

    @Test(dataProvider = "exportVourcher")
    public void ExportAll1(String url, String mess) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            String id = ins.oucherFormVoucherPage(null, "1", "10").getJSONArray("list").getJSONObject(0).getString("voucher_id");
            //导出
            int code = ins.vourcherExport(url, id).getInteger("code");
            Preconditions.checkArgument(code == 1000, mess + "导出状态码为" + code);
            Thread.sleep(800);
            String status = ins.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), mess + " " + status);
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("导出");
        }
    }

    @DataProvider(name = "exportVourcher")
    public Object[] exportVourcher() {
        return new String[][]{ // 单弄 活动报名记录、车系列表、车型列表

                {"/jiaochen/pc/voucher-manage/change-record/export", "优惠券变更记录"},
                {"/jiaochen/pc/voucher-manage/voucher-invalid-page/export", "作废记录"},
                {"/jiaochen/pc/voucher-manage/additional-record/export", "增发记录"},
                {"/jiaochen/pc/voucher-manage/send-record/export", "领取记录"},
                {"/jiaochen/pc/voucher-manage/verification-record/export", "核销记录"},

        };
    }

    @Test
    public void ExportAll2() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            String id = "";
            JSONArray array = ins.activityPage(1, 50).getJSONArray("list");
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.getInteger("activity_type") == 2) {// 招募活动 类型是2
                    id = obj.getString("id");
                }
            }
            //导出
            int code = ins.activityExport(id).getInteger("code");
            Preconditions.checkArgument(code == 1000, "导出状态码为" + code);
            Thread.sleep(800);
            String status = ins.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("导出活动报名记录");
        }
    }

    @Test
    public void ExportAll3() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            //导出
            int code = ins.carStyleExport(info.BrandID).getInteger("code");
            Preconditions.checkArgument(code == 1000, "导出状态码为" + code);
            Thread.sleep(800);
            String status = ins.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("导出车系列表");
        }
    }

    @Test
    public void ExportAll4() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            //导出
            int code = ins.carModelExport(info.BrandID, info.CarStyleID).getInteger("code");
            Preconditions.checkArgument(code == 1000, "导出状态码为" + code);
            Thread.sleep(800);
            String status = ins.exportListFilterManage("-1", "1", "1", null, null).getJSONArray("list").getJSONObject(0).getString("status_name");

            Preconditions.checkArgument(status.equals("导出完成"), status);
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("导出车型列表");
        }
    }


    @Test
    public void show() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            VisitorProxy visitor = new VisitorProxy(EnumTestProduce.JC_DAILY);

            Long voucherId = new VoucherGenerator.Builder().visitor(visitor).status(VoucherStatusEnum.INVALIDED).buildVoucher().getVoucherId();

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("卡券用法示例");
        }
    }

    //@Test
    public void pic() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";
            String base64 = new ImageUtil().getImageBinary(filePath);
            String logo = ins.pcFileUploadNew(new ImageUtil().getImageBinary(filePath)).getString("pic_path");

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("卡券用法示例");
        }
    }

    //@Test
    public void enuma() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            ins.pcEnuMap();

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("通用枚举");
        }
    }

    @Test(priority = 7)
    public void zzzzzdel() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            //删除规格
            JSONArray list = ins.specificationsPage(1, 50, null, null, null).getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                if (obj.getString("first_category").contains("品类") && obj.getString("first_category").length() == 7 && obj.getInteger("num") == 0) {
                    Long id = obj.getLong("id");
                    ins.specificationsDel(id, false);
                }

            }

            //删除品牌
            JSONArray list2 = ins.BrandPage(1, 50, "pp", null).getJSONArray("list");
            for (int j = 0; j < list2.size(); j++) {
                JSONObject obj = list2.getJSONObject(j);
                if (obj.getString("brand_name").length() == 15) {
                    Long id = obj.getLong("id");
                    ins.BrandDel(id, false);
                }
            }

            //删除品类
            JSONArray list3 = ins.categoryPage(1, 50, null, null, null, null).getJSONArray("list");
            for (int j = 0; j < list3.size(); j++) {
                JSONObject obj = list3.getJSONObject(j);
                if (obj.getString("category_name").length() == 7 && obj.getString("category_name").contains("品类")) {
                    Long id = obj.getLong("id");
                    ins.categoryChgStatus(id, false, false);
                    ins.categoryDel(id, false);
                }
            }


        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("删除规格品牌品类");
        }
    }

    @Test(dataProvider = "CSTMINFO")
    public void newPotentialCustomer(String name, String phone, String type, String sex, String mess, String chk) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            int bef = ins.preSleCustomerManage(null, "1", "1", null, null).getInteger("total");
            Long shop_id = info.oneshopid;
            Long car_style_id = ins.styleList(shop_id).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = ins.modelList(car_style_id).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = ins.saleList(shop_id).getJSONArray("list").getJSONObject(0).getString("sales_id");
            int code = ins.createPotentialCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
            int after = ins.preSleCustomerManage(null, "1", "1", null, null).getInteger("total");
            if (chk.equals("false")) {
                Preconditions.checkArgument(code == 1001, mess + "期待失败，实际" + code);
            } else {
                int sum = after - bef;
                Preconditions.checkArgument(code == 1000, mess + "期待创建成功，实际" + code);
                Preconditions.checkArgument(sum == 1, mess + "期待创建成功列表+1，实际增加" + sum);
                int code2 = ins.createPotentialCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
                Preconditions.checkArgument(code2 == 1001, "使用列表中存在的手机号期待创建失败，实际" + code);
            }
        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("创建潜客");
        }
    }

    @DataProvider(name = "CSTMINFO")
    public Object[] customerInfo() {
        return new String[][]{ // 姓名 手机号 类型 性别  提示语 正常/异常

                {"我", "1382172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "PERSON", "0", "姓名一个字", "true"},
                {info.stringfifty, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "CORPORATION", "1", "姓名50个字", "true"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 100)), "CORPORATION", "1", "手机号10位", "false"},
                {info.stringsix, "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), "CORPORATION", "1", "手机号12位", "false"},
                {info.stringfifty + "1", "1381172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000)), "CORPORATION", "1", "姓名51位", "false"},

        };
    }

    @Test
    public void newPotentialCustomerErr() {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            Long shop_id = info.oneshopid;
            Long car_style_id = ins.styleList(shop_id).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = ins.modelList(car_style_id).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = ins.saleList(shop_id).getJSONArray("list").getJSONObject(0).getString("sales_id");
            String name = "name" + System.currentTimeMillis();
            String phone = "1391172" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));
            String type = "PERSON";
            String sex = "0";
            //不填写姓名
            int code = ins.createPotentialCstm(null, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
            Preconditions.checkArgument(code == 1001, "不填写姓名期待失败，实际" + code);

            //不填写手机号
            int code1 = ins.createPotentialCstm(name, null, type, sex, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "不填写手机号期待失败，实际" + code);

            //不填写类型
            int code2 = ins.createPotentialCstm(name, phone, null, sex, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "不填写车主类型期待失败，实际" + code);

            //不填写性别
            int code3 = ins.createPotentialCstm(name, phone, type, null, car_style_id, car_model_id, shop_id, salesId, false).getInteger("code");
            Preconditions.checkArgument(code3 == 1001, "不填写性别期待失败，实际" + code);

            //不填写意向车型 bug 7866
//            int code4 = jc.createPotentialCstm(name,phone,type,sex,null,shop_id,salesId,false).getInteger("code");
//            Preconditions.checkArgument(code4==1001,"不填写意向车型期待失败，实际"+code);

            //不填写所属门店
            int code5 = ins.createPotentialCstm(name, phone, type, sex, car_style_id, car_model_id, null, salesId, false).getInteger("code");
            Preconditions.checkArgument(code5 == 1001, "不填写所属门店期待失败，实际" + code);

//            //不填写所属销售 bug 7866
//            int code6 = jc.createPotentialCstm(name,phone,type,sex,car_model_id,shop_id,null,false).getInteger("code");
//            Preconditions.checkArgument(code6==1001,"不填写所属销售期待失败，实际"+code);


        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("创建潜客");
        }
    }

    @Test(dataProvider = "CSTMINFO")
    public void newCstmRecord(String name, String phone, String type, String sex, String mess, String chk) {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            Long shop_id = info.oneshopid;
            Long car_style_id = ins.styleList(shop_id).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = ins.modelList(car_style_id).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = ins.saleList(shop_id).getJSONArray("list").getJSONObject(0).getString("sales_id");


            if (chk.equals("false")) {
                int code = ins.createCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(0), "ASDFUGGDSF12" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), false).getInteger("code");
                Preconditions.checkArgument(code == 1001, mess + "期待失败，实际" + code);
            } else {
                int code1 = ins.createCstm(name, info.donephone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(0), "ASDFUGGDSF02" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), false).getInteger("code");
                Preconditions.checkArgument(code1 == 1000, mess + "期待创建成功，实际" + code1);

            }

        } catch (AssertionError e) {
            collectMessage(e);
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            saveData("创建成交记录");
        }
    }

    @Test
    public void newCstmRecordErr() {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            Long shop_id = info.oneshopid;
            Long car_style_id = ins.styleList(shop_id).getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = ins.modelList(car_style_id).getJSONArray("list").getJSONObject(0).getLong("model_id");
            String salesId = ins.saleList(shop_id).getJSONArray("list").getJSONObject(0).getString("sales_id");
            String name = "name" + System.currentTimeMillis();
            String phone = info.donephone;
            String type = "PERSON";
            String sex = "0";


//            int code = jc.createCstm(name,phone,type,sex,car_model_id,shop_id,salesId,dt.getHistoryDate(1),"ASDFUGGDSF12"+Integer.toString((int)((Math.random()*9+1)*10000)),false).getInteger("code");
//            Preconditions.checkArgument(code==1001,"购车日期大于当前时间期待失败，实际"+code);

            int code1 = ins.createCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(-1), "ASDFUGGDSF1" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), false).getInteger("code");
            Preconditions.checkArgument(code1 == 1001, "底盘号16位期待失败，实际" + code1);

            int code2 = ins.createCstm(name, phone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(-1), "ASDFUGGDSF111" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), false).getInteger("code");
            Preconditions.checkArgument(code2 == 1001, "底盘号18位期待失败，实际" + code2);

            int code3 = ins.createCstm(name, info.phone, type, sex, car_style_id, car_model_id, shop_id, salesId, dt.getHistoryDate(-1), "ASDFUGGDSF11" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), false).getInteger("code");
            Preconditions.checkArgument(code3 == 1001, "手机号未注册小程序期待失败，实际" + code3);


        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("创建成交记录异常条件");
        }
    }


}
