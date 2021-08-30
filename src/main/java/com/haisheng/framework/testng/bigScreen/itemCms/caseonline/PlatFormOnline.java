package com.haisheng.framework.testng.bigScreen.itemCms.caseonline;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.container.ExcelContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.container.IContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemCms.common.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;

public class PlatFormOnline extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduct PRODUCT = EnumTestProduct.CMS_ONLINE;
    private static final EnumAccount ACCOUNT = EnumAccount.CMS_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(PRODUCT);
    public SceneUtil util = new SceneUtil(visitor);

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_MANAGE_PORTAL_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MANAGEMENT_PLATFORM_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.WM.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCT.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(PRODUCT.getShopId()).setReferer(PRODUCT.getReferer()).setRoleId(PRODUCT.getRoleId()).setProduct(PRODUCT.getAbbreviation());
        beforeClassInit(commonConfig);
        util.login(ACCOUNT);
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

    @Test
    public void createEntrance() {
        File file = null;
        try {
            file = util.downloadFile();
            if (file != null) {
                IContainer container = new ExcelContainer.Builder().path(file.getPath()).build();
                container.init();
                //输出表名，没有啥实际意义
                Arrays.stream(container.getTables()).forEach(e -> logger.info("table_name is：{}", e.getKey()));
                //这三行是生成设备及出入口的代码，注释掉，用的时候开启然后提交到git
                long subjectId = util.getSubjectId(container);
                ITable[] tables = container.getTables();
                Arrays.stream(tables).forEach(table -> util.createLayoutAndAddDevice(subjectId, table));
            }
        } catch (Exception e) {
            collectMessage(e);
        } finally {
            util.deleteFile(file);
        }
    }
}
