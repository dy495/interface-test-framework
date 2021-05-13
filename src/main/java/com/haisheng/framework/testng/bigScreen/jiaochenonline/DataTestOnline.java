package com.haisheng.framework.testng.bigScreen.jiaochenonline;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.container.ExcelContainer;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.container.IContainer;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.row.IRow;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.table.ITable;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.util.FileUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vouchermanage.SendRecordBean;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.SendRecordScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class DataTestOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_ONLINE;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_JC;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = PRODUCE.getShopId();
        commonConfig.roleId = ALL_AUTHORITY.getRoleId();
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
//        user.loginPc(ALL_AUTHORITY);
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }


    @Test
    public void test1() {
        String path = FileUtil.getResourcePath("excel/卡券数据-新模板-所有去重数据-未使用状态0510.xlsx");
        IContainer container = new ExcelContainer.Builder().path(path).buildContainer();
        container.init();
        ITable[] tables = container.getTables();
        ITable table = tables[0];
        table.load();
        IRow[] rows = table.getRows();
        Arrays.stream(rows).forEach(row -> {
            String code = row.getField("卡券号").getValue();
            String name = row.getField("卡券名称").getValue();
            String time = row.getField("创建时间").getValue();
            String startDate = row.getField("生效时间").getValue();
            String endDate = row.getField("过期时间").getValue();
            CommonUtil.valueView(code, name);
            IScene scene = SendRecordScene.builder().voucherName(name).build();
            SendRecordBean sendRecordBean = util.toJavaObject(scene, SendRecordBean.class, "voucher_code", code);
            Preconditions.checkArgument(sendRecordBean != null, code + "为空");
            String sendTime = sendRecordBean.getSendTime();
            Preconditions.checkArgument(time.equals(sendTime), "表格的创建时间" + time + "列表的发送时间" + sendTime);
            String validityTime = sendRecordBean.getValidityTime();
            String[] strings = validityTime.split("~");
            String startTime = strings[0];
            String endTime = strings[1];
            Preconditions.checkArgument(startDate.equals(startTime), "表格的生效时间" + startDate + "列表的生效时间" + startTime);
            Preconditions.checkArgument(endDate.equals(endTime), "表格的结束时间" + endDate + "列表的结束时间" + endTime);
            CommonUtil.logger(code);
        });
    }
}
