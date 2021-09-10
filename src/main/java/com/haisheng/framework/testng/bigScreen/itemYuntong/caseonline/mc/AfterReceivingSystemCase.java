package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.mc;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.afterreception.AppAfterReceptionListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.afterreception.AppCustomerEditScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.task.AppReceptionFinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.bean.AfterReceptionBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool.YtDataCenter;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Random;

public class AfterReceivingSystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct product = EnumTestProduct.YT_ONLINE_JD; // 管理页—-首页
    private static final EnumAccount YT_RECEPTION_ACCOUNT = EnumAccount.YT_ONLINE_MC; // 全部权限账号 【运通】
    public VisitorProxy visitor = new VisitorProxy(product);   // 产品类放到代理类中（通过代理类发请求）
    public SceneUtil util = new SceneUtil(visitor);
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
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(YT_RECEPTION_ACCOUNT.getReceptionShopId()).setRoleId(YT_RECEPTION_ACCOUNT.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);  // 配置请求头
        util.loginPc(YT_RECEPTION_ACCOUNT);   //登录
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

    public String getCarPlat(){
        String[] all = {"A","B","C","D","E","F","G","H",
                "I","J","K","L","M","N","O","P","Q",
                "R","S","T","U","V","W","X","Y","Z",
                "0","1","2","3","4","5","6","7","8","9"};
        String[] letter = {"A","B","C","D","E","F","G","H",
                "I","J","K","L","M","N","O","P","Q",
                "R","S","T","U","V","W","X","Y","Z"};
        String[] city = {"浙","京","沪","津","渝","鲁","冀","晋","蒙",
                "辽","吉","黑","苏","皖","闽","赣","豫","湘","鄂","粤",
                "桂","琼","川","贵","云","藏","陕","甘","青","宁","新"};
        Random random = new Random();
        String l = letter[random.nextInt(26)];
        String c = city[random.nextInt(31)];
        StringBuilder sb = new StringBuilder();
        sb.append(c);
        sb.append(l);
        for (int i = 0; i < 6; i++) {
            sb.append(all[random.nextInt(36)]);
        }
        return sb.toString();
    }

    @Test(dataProvider = "AfterErrorInfo",dataProviderClass = YtDataCenter.class)
    public void afterReceptionEditInfo(String description, String point, String content, String expect){
        try {
            AfterReceptionBean afterReception = util.getAfterReception();
            Integer code = AppCustomerEditScene.builder().customerId(afterReception.getCustomerId()).id(afterReception.getId()).shopId(afterReception.getShopId())
                    .afterSalesType("MAINTENANCE").customerName("afterName").customerPhone("13" + CommonUtil.getRandom(9)).plateNumber(getCarPlat())
                    .sexId(1).build().modify(point, content).visitor(visitor).getResponse().getCode();
            sleep(3);
            Preconditions.checkArgument(Objects.equals(code.toString(), expect),description+"预期结果:"+expect+"实际结果:"+code);
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally {
            saveData("app售后接待修改资料");
        }
    }

    @Test
    public void finishReceptionError(){
        try {
            AfterReceptionBean afterReception = util.getAfterReception();
            if (afterReception.getCustomerId() == null){
                String message = AppReceptionFinishReceptionScene.builder().id(afterReception.getId()).shopId(afterReception.getShopId()).build().visitor(visitor).getResponse().getMessage();
                Preconditions.checkArgument(Objects.equals("请先完成客户资料的维护", message),"完成接待message为"+message);
            } else {
                int beforeSize = AppAfterReceptionListScene.builder().customerId(afterReception.getCustomerId()).shopId(afterReception.getShopId()).size(10).build().visitor(visitor).execute().getJSONArray("list").size();
                String message = AppReceptionFinishReceptionScene.builder().id(afterReception.getId()).shopId(afterReception.getShopId()).build().visitor(visitor).getResponse().getMessage();
                Preconditions.checkArgument(Objects.equals("success", message),"完成接待失败message:"+message);
                int afterSize = AppAfterReceptionListScene.builder().customerId(afterReception.getCustomerId()).shopId(afterReception.getShopId()).size(10).build().visitor(visitor).execute().getJSONArray("list").size();
                Preconditions.checkArgument(beforeSize+1 == afterSize,"客户车牌"+afterReception.getPlateNumber()+"接待完成后接待记录+1,接待前该客户的接待记录="+beforeSize+"接待后="+afterSize);
            }
        }catch (AssertionError | Exception e){
            appendFailReason(e.toString());
        }finally {
            saveData("完成接待，（虚拟卡片不能完成功能）");
        }
    }




}
