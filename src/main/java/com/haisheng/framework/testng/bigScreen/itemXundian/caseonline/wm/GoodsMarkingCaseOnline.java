package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.wm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.Response;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.GoodsBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.GoodsParamBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral.IntegralCategoryTypeEnum;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity.CommodityStatusEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralmall.*;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.UserUtil;
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
 * ??????????????????
 *
 * @author wangmin
 * @date 2020/11/24
 */
public class GoodsMarkingCaseOnline extends TestCaseCommon implements TestCaseStd {
    private final static EnumTestProduct PRODUCE = EnumTestProduct.INS_ONLINE;
    private static final AccountEnum ALL_AUTHORITY = AccountEnum.YUE_XIU_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SceneUtil util = new SceneUtil(visitor);
    private final static String FILEPATH = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/??????.jpg";
    private final static String FILEPATH_TWO = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/goods/??????.jpg";
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
        commonConfig.setProduct(PRODUCE.getAbbreviation());
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
            String[] names = {"???", EnumDesc.DESC_10.getDesc()};
            Arrays.stream(names).forEach(name -> {
                String picPath = util.getPicPath(FILEPATH, "1:1");
                Response response = CreateBrandScene.builder().brandName(name).brandDescription("??????????????????").brandPic(picPath).build().visitor(visitor).getResponse();
                String message = response.getMessage();
                String err = "success";
                CommonUtil.checkResult("?????????message", err, message);
                //????????????
                IScene scene = BrandPageScene.builder().build();
                BrandPageBean brandPageBean = util.toJavaObject(scene, BrandPageBean.class, "brand_name", name);
                DeleteBrandScene.builder().id(brandPageBean.getId()).build().visitor(visitor).execute();
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("????????????-?????????????????????1?????????10??????");
        }

    }

    @Test()
    public void integralMall_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            CreateBrandScene.builder().brandName("??????").brandDescription("??????????????????").brandPic(util.getCategoryPicPath()).build().visitor(visitor).execute();
            IScene scene = BrandPageScene.builder().build();
            Long id = util.toJavaObject(scene, BrandPageBean.class, "brand_name", "??????").getId();
            //????????????
            ChangeBrandStatusScene.builder().id(id).status(false).build().visitor(visitor).execute();
            EditBrandScene.builder().id(id).brandName("??????").brandDescription("thinkPad").brandPic(util.getCategoryPicPath()).build().visitor(visitor).execute();
            BrandPageBean newBrandPageBean = util.toJavaObject(scene, BrandPageBean.class, "id", id);
            CommonUtil.checkResult("?????????????????????", "??????", newBrandPageBean.getBrandName());
            CommonUtil.checkResult("?????????????????????", "thinkPad", newBrandPageBean.getBrandDescription());
            //????????????
            DeleteBrandScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("????????????-???????????????????????????");
        }
    }

    @Test()
    public void integralMall_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] names = {EnumDesc.DESC_20.getDesc() + "1", null};
            Arrays.stream(names).forEach(name -> {
                String picPath = util.getPicPath(FILEPATH, "1:1");
                String message = CreateBrandScene.builder().brandName(name).brandDescription("??????????????????").brandPic(picPath).build().visitor(visitor).getResponse().getMessage();
                String err = name == null ? "????????????????????????" : "???????????????????????????1???20??????";
                CommonUtil.checkResult("???????????????" + name, err, message);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("????????????-???????????????????????????");
        }
    }

    /**
     * ????????????
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
            List<CategoryPageBean> categoryPageBeanList = util.toJavaObjectList(scene, CategoryPageBean.class);
            categoryPageBeanList.forEach(e -> {
                if (e.getCategoryLevel().equals(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc())) {
                    CommonUtil.checkResult(e.getCategoryName() + "???????????????", categoryName, e.getParentCategory());
                    categoryNameList.add(e.getCategoryName());
                }
            });
            categoryPageBeanList.forEach(e -> {
                if (e.getCategoryLevel().equals(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc())) {
                    Preconditions.checkArgument(categoryNameList.contains(e.getParentCategory()), e.getCategoryName() + "??????????????????" + categoryNameList + "?????????");
                }
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????????????????????????????????????????????????????????????????/????????????");
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
            List<CategoryPageBean> categoryPageBeanList = util.toJavaObjectList(scene, CategoryPageBean.class);
            categoryPageBeanList.forEach(e -> {
                if (e.getCategoryLevel().equals(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc())) {
                    categoryIdList.add(e.getId());
                }
            });
            IScene newScene = CategoryPageScene.builder().firstCategory(id).secondCategory(categoryIdList.get(0)).build();
            List<CategoryPageBean> newCategoryPageBeanList = util.toJavaObjectList(newScene, CategoryPageBean.class);
            newCategoryPageBeanList.forEach(e -> {
                if (e.getCategoryLevel().equals(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc())) {
                    CommonUtil.checkResult(e.getCategoryName() + "???????????????", util.getCategoryName(categoryIdList.get(0)), e.getParentCategory());
                }
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????????????????????????????,????????????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = CategoryPageScene.builder().categoryStatus(true).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                Boolean status = obj.getBoolean("category_status");
                String name = obj.getString("category_name");
                CommonUtil.checkResult("??????" + name + "?????????", true, status);
            }
            JSONArray list2 = CategoryPageScene.builder().categoryStatus(false).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list2.size(); i++) {
                JSONObject obj = list2.getJSONObject(i);
                Boolean status = obj.getBoolean("category_status");
                String name = obj.getString("category_name");
                CommonUtil.checkResult("??????" + name + "?????????", false, status);
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC????????????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = CategoryPageScene.builder().categoryStatus(true).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                int id = obj.getInteger("id");
                Preconditions.checkArgument(obj.containsKey("category_pic"), "??????" + id + "????????????");
                Preconditions.checkArgument(obj.containsKey("category_name"), "??????" + id + "???????????????");
                Preconditions.checkArgument(obj.containsKey("category_level"), "??????" + id + "???????????????");
                Preconditions.checkArgument(obj.containsKey("num"), "??????" + id + "???????????????");
                Preconditions.checkArgument(obj.containsKey("category_status"), "??????" + id + "???????????????");
                Preconditions.checkArgument(obj.containsKey("last_modify_time"), "??????" + id + "?????????????????????");
                Preconditions.checkArgument(obj.containsKey("modify_sale_name"), "??????" + id + "????????????");
                if (!obj.getString("category_level").equals("????????????")) {
                    Preconditions.checkArgument(obj.containsKey("parent_category"), "??????" + id + "???????????????");
                }
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC??????????????????????????????????????????");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String[] names = {"???", "12345", "1Aa??????@#?????????",};
            Arrays.stream(names).forEach(name -> {
                String picPath = util.getCategoryPicPath();
                Long id = util.createCategory(name, picPath, IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
                //????????????
                ChangeStatusScene.builder().id(id).status(false).build().visitor(visitor).execute();
                //????????????-????????????
                String picPath2 = util.getPicPath(FILEPATH_TWO, "1:1");
                EditCategoryScene.builder().id(id).belongPic(picPath2).categoryName(name).categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build().visitor(visitor).execute();
                //????????????-???????????????
                EditCategoryScene.builder().id(id).categoryName(name).categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build().visitor(visitor).execute();
                //??????????????????
                DeleteCategoryScene.builder().id(id).build().visitor(visitor).execute();
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC????????????????????????/?????????????????????????????????1/5/10??????");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "12345";
            String picPath = util.getCategoryPicPath();
            //??????????????????
            Long firstId = util.createCategory(name, picPath, IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //??????????????????
            Long secondId = util.createCategory(name, picPath, IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), firstId);
            //????????????
            Long[] ids = {secondId, firstId};
            Arrays.stream(ids).forEach(id -> DeleteCategoryScene.builder().id(id).build().visitor(visitor).execute());
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC????????????????????????????????????????????????????????????????????????");
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
                String message = CreateCategoryScene.builder().categoryName(name).belongPic(picPath).categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build().visitor(visitor).getResponse().getMessage();
                String err = name == null ? "????????????????????????" : "?????????????????????1-10?????????";
                CommonUtil.checkResult("???????????????" + name, err, message);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????????????????????????????");
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
                        .categoryName("??????").belongCategory(belongCategory).build().visitor(visitor).getResponse().getMessage();
                String err = belongCategory == null ? "????????????????????????" : "?????????????????????????????????";
                CommonUtil.checkResult("??????????????????????????????", err, message);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC??????????????????????????????????????????????????????????????????");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "??????";
            String picPath = util.getCategoryPicPath();
            Long id = util.createCategory(name, picPath, IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            Long secondId = util.createCategory(name, picPath, IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            String message = CreateCategoryScene.builder().categoryName(name).belongPic(picPath).belongCategory(id)
                    .categoryLevel(IntegralCategoryTypeEnum.SECOND_CATEGORY.name())
                    .build().visitor(visitor).getResponse().getMessage();
            String err = "??????????????????????????????????????????";
            CommonUtil.checkResult("??????????????????????????????????????????", err, message);
            //????????????
            DeleteCategoryScene.builder().id(secondId).build().visitor(visitor).execute();
            DeleteCategoryScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????????????????????????????????????????");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "??????";
            Long id = util.getCategoryByLevel(IntegralCategoryTypeEnum.SECOND_CATEGORY);
            Long thirdId = util.createCategory(name, util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), id);
            String message = CreateCategoryScene.builder().categoryName(name).belongPic(util.getCategoryPicPath()).belongCategory(id)
                    .categoryLevel(IntegralCategoryTypeEnum.THIRD_CATEGORY.name())
                    .build().visitor(visitor).getResponse().getMessage();
            String err = "??????????????????????????????????????????";
            CommonUtil.checkResult("??????????????????????????????????????????", err, message);
            //????????????
            DeleteCategoryScene.builder().id(thirdId).build().visitor(visitor).execute();
            DeleteCategoryScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????????????????????????????????????????");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "??????";
            String message = CreateCategoryScene.builder().categoryName(name).belongPic(util.getCategoryPicPath())
                    .categoryLevel(IntegralCategoryTypeEnum.SECOND_CATEGORY.name())
                    .build().visitor(visitor).getResponse().getMessage();
            String err = "????????????????????????";
            CommonUtil.checkResult("???????????????????????????????????????", err, message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC??????????????????????????????????????????????????????????????????");
        }
    }

    //ok
    @Test()
    public void goodsCategory_system_12() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = "??????";
            String message = CreateCategoryScene.builder().categoryName(name)
                    .categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name())
                    .build().visitor(visitor).getResponse().getMessage();
            String err = "????????????????????????";
            CommonUtil.checkResult("???????????????????????????logo", err, message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????????????????logo?????????");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_13() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //??????????????????
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //??????????????????
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //??????????????????
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);
            //??????
            String message = ChangeStatusScene.builder().id(id).status(false).build().visitor(visitor).getResponse().getMessage();
            String err = "????????????????????????????????????";
            CommonUtil.checkResult("????????????????????????????????????", err, message);
            //??????
            String secondMessage = ChangeStatusScene.builder().id(secondId).status(false).build().visitor(visitor).getResponse().getMessage();
            String secondErr = "????????????????????????????????????";
            CommonUtil.checkResult("????????????????????????????????????", secondErr, secondMessage);
            //clean
            Long[] ids = {thirdId, secondId, id};
            Arrays.stream(ids).forEach(e -> {
                ChangeStatusScene.builder().id(e).status(false).build().visitor(visitor).execute();
                DeleteCategoryScene.builder().id(e).build().visitor(visitor).execute();
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_14() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //??????????????????
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //??????????????????
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //??????????????????
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);
            //??????
            String message = DeleteCategoryScene.builder().id(id).build().visitor(visitor).getResponse().getMessage();
            String err = "???????????????????????????????????????";
            CommonUtil.checkResult("???????????????????????????????????????", err, message);
            //??????
            String secondMessage = DeleteCategoryScene.builder().id(secondId).build().visitor(visitor).getResponse().getMessage();
            String secondErr = "???????????????????????????????????????";
            CommonUtil.checkResult("???????????????????????????????????????", secondErr, secondMessage);
            //clean
            Long[] ids = {thirdId, secondId, id};
            Arrays.stream(ids).forEach(e -> {
                ChangeStatusScene.builder().id(e).status(false).build().visitor(visitor).execute();
                DeleteCategoryScene.builder().id(e).build().visitor(visitor).execute();
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_15() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //??????????????????
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //??????????????????
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //??????????????????
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);

            ChangeStatusScene.builder().id(thirdId).status(false).build().visitor(visitor).execute();
            DeleteCategoryScene.builder().id(thirdId).build().visitor(visitor).execute();

            ChangeStatusScene.builder().id(secondId).status(false).build().visitor(visitor).execute();
            DeleteCategoryScene.builder().id(secondId).build().visitor(visitor).execute();

            ChangeStatusScene.builder().id(id).status(false).build().visitor(visitor).execute();
            DeleteCategoryScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC????????????????????????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_16() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //??????????????????
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //??????????????????
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //??????????????????
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);

            ChangeStatusScene.builder().id(thirdId).status(false).build().visitor(visitor).execute();
            ChangeStatusScene.builder().id(secondId).status(false).build().visitor(visitor).execute();
            ChangeStatusScene.builder().id(id).status(false).build().visitor(visitor).execute();

            DeleteCategoryScene.builder().id(secondId).build().visitor(visitor).execute();
            DeleteCategoryScene.builder().id(id).build().visitor(visitor).execute();
            DeleteCategoryScene.builder().id(thirdId).build().visitor(visitor).execute();

        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC????????????????????????????????????????????????&???????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_17() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            Long speId = CreateSpecificationsScene.builder().id(id).specificationsName("??????1???").belongsCategory(id).build().visitor(visitor).execute().getLong("id");
            //??????
            String message = DeleteCategoryScene.builder().id(id).build().visitor(visitor).getResponse().getMessage();
            String err = "???????????????????????????????????????";
            CommonUtil.checkResult("??????????????????????????????", err, message);
            ChangeSpecificationsStatusScene.builder().id(speId).status(false).build().visitor(visitor).execute();
            DeleteSpecificationsScene.builder().id(speId).build().visitor(visitor).execute();
            DeleteCategoryScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????????????????????????????");
        }
    }

    @Test(enabled = false)
    public void goodsCategory_system_18() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            Long speId = CreateSpecificationsScene.builder().id(id).specificationsName("??????1???").belongsCategory(id).build().visitor(visitor).execute().getLong("id");
            //??????
            String message = ChangeStatusScene.builder().id(id).status(false).build().visitor(visitor).getResponse().getMessage();
            String err = "???????????????????????????????????????";
            CommonUtil.checkResult("??????????????????????????????", err, message);
            ChangeSpecificationsStatusScene.builder().id(speId).status(false).build().visitor(visitor).execute();
            DeleteSpecificationsScene.builder().id(speId).build().visitor(visitor).execute();
            DeleteCategoryScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_19() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //??????????????????
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //??????????????????
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //??????????????????
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);
            //??????1
            ChangeStatusScene.builder().id(id).status(true).build().visitor(visitor).execute();

            //??????23
            ChangeStatusScene.builder().id(thirdId).status(false).build().visitor(visitor).execute();
            ChangeStatusScene.builder().id(secondId).status(false).build().visitor(visitor).execute();

            //??????23
            ChangeStatusScene.builder().id(secondId).status(true).build().visitor(visitor).execute();
            ChangeStatusScene.builder().id(thirdId).status(true).build().visitor(visitor).execute();
            //clean
            Long[] ids = {thirdId, secondId, id};
            Arrays.stream(ids).forEach(e -> DeleteCategoryScene.builder().id(e).build().visitor(visitor).execute());
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC???????????????????????? ???????????????&???????????????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsCategory_system_20() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //??????????????????
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            //??????????????????
            Long secondId = util.createCategory(IntegralCategoryTypeEnum.SECOND_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.SECOND_CATEGORY.name(), id);
            //??????????????????
            Long thirdId = util.createCategory(IntegralCategoryTypeEnum.THIRD_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.THIRD_CATEGORY.name(), secondId);
            //????????????
            Long[] ids = {thirdId, secondId, id};
            Arrays.stream(ids).forEach(e -> ChangeStatusScene.builder().id(e).status(false).build().visitor(visitor).execute());
            //????????????
            Arrays.stream(ids).forEach(e -> ChangeStatusScene.builder().id(e).status(true).build().visitor(visitor).execute());
            //clean
            Arrays.stream(ids).forEach(e -> DeleteCategoryScene.builder().id(e).build().visitor(visitor).execute());
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC???????????????????????? ???????????????&?????????????????????????????????????????????");
        }
    }

    /**
     * ????????????
     */
    //ok
    @Test()
    public void goodsBrand_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = BrandPageScene.builder().build();
            BrandPageBean brandPageBean = util.toJavaObjectList(scene, BrandPageBean.class).get(0);
            String name = brandPageBean.getBrandName().substring(0, 1);
            IScene newScene = BrandPageScene.builder().brandName(name).build();
            List<BrandPageBean> brandPageBeans = util.toJavaObjectList(newScene, BrandPageBean.class);
            brandPageBeans.forEach(e -> Preconditions.checkArgument(e.getBrandName().contains(name), "????????????" + e.getBrandName() + "?????????" + name));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC??????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = BrandPageScene.builder().brandStatus(true).build();
            List<BrandPageBean> brandPageBeans = util.toJavaObjectList(scene, BrandPageBean.class);
            brandPageBeans.forEach(e -> Preconditions.checkArgument(e.getBrandStatus(), "???????????????" + e.getBrandName() + "????????????" + e.getBrandStatus()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC????????????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = BrandPageScene.builder().brandStatus(false).build();
            List<BrandPageBean> brandPageBeans = util.toJavaObjectList(scene, BrandPageBean.class);
            brandPageBeans.forEach(e -> Preconditions.checkArgument(!e.getBrandStatus(), "???????????????" + e.getBrandName() + "????????????" + e.getBrandStatus()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC????????????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = BrandPageScene.builder().size(SIZE).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                int id = obj.getInteger("id");
                Preconditions.checkArgument(obj.containsKey("brand_pic"), "??????" + id + "??????logo");
                Preconditions.checkArgument(obj.containsKey("brand_name"), "??????" + id + "???????????????");
                Preconditions.checkArgument(obj.containsKey("brand_description"), "??????" + id + "???????????????");
                Preconditions.checkArgument(obj.containsKey("brand_status"), "??????" + id + "???????????????");
                Preconditions.checkArgument(obj.containsKey("num"), "??????" + id + "???????????????");
                Preconditions.checkArgument(obj.containsKey("last_modify_time"), "??????" + id + "?????????????????????");
                Preconditions.checkArgument(obj.containsKey("modify_sale_name"), "??????" + id + "????????????");
            }
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC??????????????????????????????????????????");
        }
    }

    //ok
    @Test(dataProvider = "BRAND_ADD")
    public void goodsBrand_system_5(String name, String desc) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = BrandPageScene.builder().build();
            int brandTotal = scene.visitor(visitor).execute().getInteger("total");
            CreateBrandScene.builder().brandPic(util.getCategoryPicPath()).brandName(name).brandDescription(desc).build().visitor(visitor).execute();
            int newBrandTotal = scene.visitor(visitor).execute().getInteger("total");
            CommonUtil.checkResult("????????????????????????", brandTotal + 1, newBrandTotal);
            //??????????????????
            BrandPageBean brandPageBean = util.toJavaObject(scene, BrandPageBean.class, "brand_name", name);
            Long id = brandPageBean.getId();
            Boolean status = brandPageBean.getBrandStatus();
            CommonUtil.checkResult("?????? " + name + " ?????????", true, status);
            //???????????????????????????????????????
            JSONArray list = BrandListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
            List<Long> idList = list.stream().map(e -> (JSONObject) e).map(e -> e.getLong("id")).collect(Collectors.toList());
            Preconditions.checkArgument(idList.contains(id), "????????????????????????????????????????????????id" + id);
            DeleteBrandScene.builder().id(id).build().visitor(visitor).execute();
            sleep(1);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC??????????????????????????????");
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
                String message = CreateBrandScene.builder().brandPic(util.getCategoryPicPath()).brandName(name).brandDescription(EnumDesc.DESC_BETWEEN_5_10.getDesc()).build().visitor(visitor).getResponse().getMessage();
                String err = StringUtils.isEmpty(name) ? "????????????????????????" : "???????????????????????????1???20??????";
                CommonUtil.checkResult("???????????????" + name, err, message);
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC??????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = EnumDesc.DESC_BETWEEN_5_10.getDesc() + "??????";
            Long id = CreateBrandScene.builder().brandName(name).brandDescription(name).brandPic(util.getCategoryPicPath()).build().visitor(visitor).execute().getLong("id");
            String message = CreateBrandScene.builder().brandName(name).brandDescription(name).brandPic(util.getCategoryPicPath()).build().visitor(visitor).getResponse().getMessage();
            String err = "?????????????????????";
            CommonUtil.checkResult("???????????????????????????", err, message);
            DeleteBrandScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC??????????????????????????????,?????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = EnumDesc.DESC_BETWEEN_5_10.getDesc();
            Long id = CreateBrandScene.builder().brandName(name).brandDescription(name).brandPic(util.getCategoryPicPath()).build().visitor(visitor).execute().getLong("id");
            ChangeBrandStatusScene.builder().id(id).status(false).build().visitor(visitor).execute();
            DeleteBrandScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC???????????????????????? ???????????????&??????=????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsBrand_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String name = EnumDesc.DESC_BETWEEN_5_10.getDesc();
            Long id = CreateBrandScene.builder().brandName(name).brandDescription(name).brandPic(util.getCategoryPicPath()).build().visitor(visitor).execute().getLong("id");
            ChangeBrandStatusScene.builder().id(id).status(false).build().visitor(visitor).execute();
            ChangeBrandStatusScene.builder().id(id).status(true).build().visitor(visitor).execute();
            DeleteBrandScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC???????????????????????? ???????????????&??????=????????????????????????");
        }
    }

    /**
     * ????????????
     */
    //ok
    @Test
    public void goodsSpecifications_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //??????????????????
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            String name = EnumDesc.DESC_BETWEEN_5_10.getDesc();
            Long specificationsId = CreateSpecificationsScene.builder().specificationsName(name).belongsCategory(id).build().visitor(visitor).execute().getLong("id");
            ChangeSpecificationsStatusScene.builder().id(specificationsId).status(false).build().visitor(visitor).execute();
            DeleteSpecificationsScene.builder().id(specificationsId).build().visitor(visitor).execute();
            DeleteCategoryScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsSpecifications_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //??????????????????
            Long id = util.createCategory(IntegralCategoryTypeEnum.FIRST_CATEGORY.getDesc(), util.getCategoryPicPath(), IntegralCategoryTypeEnum.FIRST_CATEGORY.name(), null);
            String name = EnumDesc.DESC_BETWEEN_5_10.getDesc();
            //????????????
            Long specificationsId = CreateSpecificationsScene.builder().specificationsName(name).belongsCategory(id).build().visitor(visitor).execute().getLong("id");
            //?????????????????????
            ChangeSpecificationsStatusScene.builder().id(specificationsId).status(false).build().visitor(visitor).execute();
            DeleteSpecificationsScene.builder().id(specificationsId).build().visitor(visitor).execute();
            Long newSpecificationsId = CreateSpecificationsScene.builder().specificationsName(name).belongsCategory(id).build().visitor(visitor).execute().getLong("id");
            //????????????????????????
            DeleteSpecificationsScene.builder().id(newSpecificationsId).build().visitor(visitor).execute();
            DeleteCategoryScene.builder().id(id).build().visitor(visitor).execute();
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC???????????????????????????????????????&????????????????????????");
        }
    }

    /**
     * ????????????
     */
    //ok
    @Test()
    public void goodsManager_system_1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene goodsManagePageScene = GoodsManagePageScene.builder().build();
            GoodsManagePageBean goodsManagePageBean = util.toJavaObjectList(goodsManagePageScene, GoodsManagePageBean.class).get(0);
            String goodsName = goodsManagePageBean.getGoodsName();
            IScene newScene = GoodsManagePageScene.builder().goodsName(goodsName).build();
            List<GoodsManagePageBean> newGoodsManagePageBean = util.toJavaObjectList(newScene, GoodsManagePageBean.class);
            newGoodsManagePageBean.forEach(e -> Preconditions.checkArgument(e.getGoodsName().contains(goodsName), "??????" + goodsName + ", ???????????????" + e.getGoodsName()));
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????-??????????????????");
        }
    }

    //ok
    @Test
    public void goodsManager_system_2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = BrandListScene.builder().build().visitor(visitor).execute().getJSONArray("list");
            List<BrandListBean> brandPageBeanList = list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, BrandListBean.class)).collect(Collectors.toList());
            brandPageBeanList.forEach(brandListBean -> {
                IScene goodsManagePageScene = GoodsManagePageScene.builder().goodsBrand(brandListBean.getId()).build();
                List<GoodsManagePageBean> goodsManagePageBeanList = util.toJavaObjectList(goodsManagePageScene, GoodsManagePageBean.class);
                goodsManagePageBeanList.forEach(goodsManagePageBean -> CommonUtil.checkResult(goodsManagePageBean.getGoodsName() + "???????????????", brandListBean.getBrandName(), goodsManagePageBean.getBelongsBrand()));
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????-??????????????????");
        }
    }

    //ok
    @Test()
    public void goodsManager_system_3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Arrays.stream(CommodityStatusEnum.values()).forEach(e -> {
                IScene scene = GoodsManagePageScene.builder().goodsStatus(e.name()).build();
                List<GoodsManagePageBean> goodsManagePageBeanList = util.toJavaObjectList(scene, GoodsManagePageBean.class);
                goodsManagePageBeanList.forEach(goodsManagePageBean -> {
                    CommonUtil.checkResult(goodsManagePageBean.getGoodsName() + "???????????????", e.name(), goodsManagePageBean.getGoodsStatus());
                    CommonUtil.checkResult(goodsManagePageBean.getGoodsName() + "???????????????", e.getName(), goodsManagePageBean.getGoodsStatusName());
                });
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC?????????????????????????????????-??????????????????");
        }
    }

    //ok
    @Test
    public void goodsManager_system_4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene categoryListScene = CategoryListScene.builder().categoryLevel(IntegralCategoryTypeEnum.FIRST_CATEGORY.name()).build();
            JSONArray list = categoryListScene.visitor(visitor).execute().getJSONArray("list");
            List<CategoryListBean> categoryListBeanList = list.stream().map(e -> (JSONObject) e).map(e -> JSONObject.toJavaObject(e, CategoryListBean.class)).collect(Collectors.toList());
            categoryListBeanList.forEach(categoryListBean -> {
                IScene goodsManagePageScene = GoodsManagePageScene.builder().firstCategory(categoryListBean.getCategoryType()).build();
                List<GoodsManagePageBean> goodsManagePageBeanList = util.toJavaObjectList(goodsManagePageScene, GoodsManagePageBean.class);
                goodsManagePageBeanList.forEach(goodsManagePageBean -> CommonUtil.checkResult("?????????????????????????????????????????????", categoryListBean.getCategoryName(), goodsManagePageBean.getGoodsCategory()));
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC??????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsManager_system_5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = GoodsManagePageScene.builder().size(SIZE).build().visitor(visitor).execute().getJSONArray("list");
            list.stream().map(obj -> (JSONObject) obj).forEach(obj -> {
                Preconditions.checkArgument(obj.containsKey("goods_pic"), "?????????????????????");
                Preconditions.checkArgument(obj.containsKey("goods_name"), "?????????????????????");
                Preconditions.checkArgument(obj.containsKey("belongs_brand"), "?????????????????????");
                Preconditions.checkArgument(obj.containsKey("goods_category"), "?????????????????????");
                Preconditions.checkArgument(obj.containsKey("price"), "??????????????????");
                Preconditions.checkArgument(obj.containsKey("goods_status"), "?????????????????????");
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC???????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsManager_system_8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene goodsManagePageScene = GoodsManagePageScene.builder().build();
            List<GoodsManagePageBean> goodsManagePageBeanListA = util.toJavaObjectList(goodsManagePageScene, GoodsManagePageBean.class);
            GoodsManagePageBean goodsManagePageBean = goodsManagePageBeanListA.stream().filter(e -> util.goodsIsOccupation(e.getGoodsName())).findFirst().orElse(goodsManagePageBeanListA.get(0));
            //????????????
            String message = ChangeGoodsStatusScene.builder().id(goodsManagePageBean.getId()).status(CommodityStatusEnum.DOWN.name()).build().visitor(visitor).getResponse().getMessage();
            String err = "????????????????????????????????????";
            CommonUtil.checkResult("?????????????????????" + goodsManagePageBean.getGoodsName(), err, message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC???????????????????????????????????????????????????????????????");
        }
    }

    //ok
    @Test
    public void goodsManager_system_9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene goodsManagePageScene = GoodsManagePageScene.builder().build();
            List<GoodsManagePageBean> goodsManagePageBeanListA = util.toJavaObjectList(goodsManagePageScene, GoodsManagePageBean.class);
            GoodsManagePageBean goodsManagePageBean = goodsManagePageBeanListA.stream().filter(e -> util.goodsIsOccupation(e.getGoodsName())).findFirst().orElse(goodsManagePageBeanListA.get(0));
            //????????????
            String message = DeleteGoodsScene.builder().id(goodsManagePageBean.getId()).build().visitor(visitor).getResponse().getMessage();
            String err = "????????????????????????????????????";
            CommonUtil.checkResult("?????????????????????" + goodsManagePageBean.getGoodsName(), err, message);
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC???????????????????????????????????????????????????????????????");
        }
    }

    //ok
    @Test()
    public void goodsManager_system_10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            GoodsBean goodsBean = util.createGoods();
            Long goodsId = goodsBean.getGoodsId();
            //??????&??????
            IScene goodsManagePageScene = GoodsManagePageScene.builder().build();
            Arrays.stream(CommodityStatusEnum.values()).forEach(anEnum -> {
                ChangeGoodsStatusScene.builder().id(goodsId).status(anEnum.name()).build().visitor(visitor).execute();
                List<GoodsManagePageBean> goodsManagePageBeanList = util.toJavaObjectList(goodsManagePageScene, GoodsManagePageBean.class);
                goodsManagePageBeanList.stream().filter(e -> e.getId().equals(goodsId)).forEach(e -> {
                    CommonUtil.checkResult(e.getGoodsName() + "???????????????", anEnum.getName(), e.getGoodsStatusName());
                    CommonUtil.checkResult(e.getGoodsName() + "???????????????", anEnum.name(), e.getGoodsStatus());
                });
                if (anEnum.equals(CommodityStatusEnum.DOWN)) {
                    IScene scene = GoodsManagePageScene.builder().goodsStatus(CommodityStatusEnum.UP.name()).build();
                    util.toJavaObjectList(scene, GoodsManagePageBean.class).forEach(e -> Preconditions.checkArgument(!e.getId().equals(goodsId), "???????????????" + e.getGoodsName() + "??????????????????????????????"));
                }
            });
            GoodsParamBean goodsParamBean = goodsBean.getGoodsParamBean();
            //????????????
            DeleteGoodsScene.builder().id(goodsBean.getGoodsId()).build().visitor(visitor).execute();
            //????????????
            DeleteBrandScene.builder().id(goodsParamBean.getBrandId()).build().visitor(visitor).execute();
            //????????????
            ChangeSpecificationsStatusScene.builder().id(goodsParamBean.getSpecificationsId()).status(false).build().visitor(visitor).execute();
            DeleteSpecificationsScene.builder().id(goodsParamBean.getSpecificationsId()).build().visitor(visitor).execute();
            //????????????
            Long[] ids = {goodsParamBean.getThirdCategory(), goodsParamBean.getSecondCategory(), goodsParamBean.getFirstCategory()};
            Arrays.stream(ids).forEach(id -> {
                ChangeStatusScene.builder().id(id).status(false).build().visitor(visitor).execute();
                DeleteCategoryScene.builder().id(id).build().visitor(visitor).execute();
            });
        } catch (AssertionError | Exception e) {
            collectMessage(e);
        } finally {
            saveData("PC??????????????????????????????,????????????????????????");
        }
    }

//    @Test
//    public void goodAddMore() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            //??????????????????????????????
//            String name = "??????";
//            JSONObject obj = info.newFirstCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)));
//            String idone = obj.getString("id");
//            JSONObject objtwo = info.newSecondCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idone);
//            String idtwo = objtwo.getString("id");
//            JSONObject objthree = info.newThirdCategory(name + Integer.toString((int) ((Math.random() * 9 + 1) * 10000)), idtwo);
//            String idthree = objthree.getString("id");
//
//
//            //????????????
//            String brandid = info.newGoodBrand("pp" + System.currentTimeMillis(), "pp??????" + System.currentTimeMillis()).getString("id");
//
//            //????????????
//            String spename1 = "1??????" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));
//            String spename2 = "2??????" + Integer.toString((int) ((Math.random() * 9 + 1) * 10000));
//
//            Long speId = ins.specificationsCreate(spename1, Long.parseLong(idone), null, null, true).getLong("id"); //??????????????? ???10?????????
//            Long speId2 = ins.specificationsCreate(spename2, Long.parseLong(idone), null, null, true).getLong("id"); //??????????????? ???1?????????
//            //????????????1???10?????????
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
//            //????????????2???????????????
//            JSONObject objItem1 = new JSONObject();
//            objItem1.put("specifications_item", "speItemName11");
//            JSONArray spearray2 = new JSONArray();
//            spearray2.add(objItem1);
//            ins.specificationsEdit(spename2, Long.parseLong(idone), spearray2, speId2, true);
//            Long speItemId2 = ins.specificationsDetail(speId2, 1, 10).getJSONArray("specifications_list").getJSONObject(0).getLong("specifications_id");
//
//            //????????????
//
//            pcCreateGoods good = new pcCreateGoods();
//            good.first_category = Long.parseLong(idone);
//            good.second_category = Long.parseLong(idtwo);
//            good.third_category = Long.parseLong(idthree);
//            good.goods_brand = Long.parseLong(brandid);
//
//            //????????????
//
//            JSONArray select_specifications = new JSONArray();
//
//            JSONObject spe1obj = new JSONObject();
//            spe1obj.put("specifications_id", speId);
//            spe1obj.put("specifications_name", spename1);
//            //??????1?????????
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
//            //??????2?????????
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
//            //????????????
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
//            Preconditions.checkArgument(code == 1000, "?????????" + code);
//            Long goodid = objnew.getJSONObject("data").getLong("id");
//
//
//            //????????????
//            ins.deleteGoodMethod(goodid);
//            //??????->????????????
//            ins.specificationsChgStatus(speId, false);
//            ins.specificationsDel(speId);
//            ins.specificationsChgStatus(speId2, false);
//            ins.specificationsDel(speId2);
//            //????????????
//            ins.categoryDel(Long.parseLong(idthree), true);
//            ins.categoryDel(Long.parseLong(idtwo), true);
//            ins.categoryDel(Long.parseLong(idone), true);
//
//
//        } catch (AssertionError | Exception e) {
//            collectMessage(e);
//        } finally {
//            saveData("PC??????????????????????????????,?????????????????????");
//        }
//    }
}
