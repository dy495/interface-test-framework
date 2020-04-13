package com.haisheng.framework.testng.patrolShops;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.bigScreen.Feidan;
import com.haisheng.framework.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class PatrolShopsDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

    Feidan feidan = new Feidan();
    StringUtil stringUtil = new StringUtil();
    DateTimeUtil dateTimeUtil = new DateTimeUtil();
    CheckUtil checkUtil = new CheckUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    DateTimeUtil dt = new DateTimeUtil();
    PatrolShops patrolShops = new PatrolShops();

//    ------------------------------------------------------非创单验证（其他逻辑）-------------------------------------

    /**
     * 同一业务员报备同一顾客两次（全号）
     */
    @Test
    public void dupReport() {

        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        String caseDesc = "重复报备";

        logger.info("\n\n" + caseName + "\n");

        try {

            

        } catch (AssertionError e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason = e.toString();
            aCase.setFailReason(failReason);
        } finally {
            feidan.saveData(aCase, ciCaseName, caseName, failReason, caseDesc);
        }
    }

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        patrolShops.login();
    }

    @AfterClass
    public void clean() {
        feidan.clean();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    @DataProvider(name = "RULE_ID")
    public Object[] ruleId() {
        return new Object[]{
                "0min", "60min", "1day", "7day", "30day", "max"
        };
    }

    @DataProvider(name = "INVALID_NUM_AHEAD")
    public Object[] invalidNumAhead() {
        return new Object[]{
                "260001"
        };
    }

    @DataProvider(name = "INVALID_NUM_PROTECT")
    public Object[] invalidNumProtect() {
        return new Object[]{
                "10001"
        };
    }

    @DataProvider(name = "VALID_NUM_AHEAD")
    public Object[] validNumAhead() {
        return new Object[]{
                0, 1, 60, 1440, 10080, 43200, 10000
        };
    }

    @DataProvider(name = "VALID_NUM_PROTECT")
    public Object[] validNumProtect() {
        return new Object[]{
                0, 1, 60, 1440, 10080, 43200, 10001, 260000
        };
    }

    @DataProvider(name = "INVALID_NAME")
    public Object[] invalidName() {
        return new Object[]{
                "qwer@tyui&opas.dfgh#？",
                "qwer tyui opas dfg  h",
                "默认规则"
        };
    }

    @DataProvider(name = "VALID_NAME")
    public Object[] validName() {
        return new Object[]{
                "正常一点-飞单V3.0",
        };
    }



    @DataProvider(name = "H5_REPORT")
    public Object[][] report() {
        return new Object[][]{
//channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE"
                new Object[]{
                        "adviser", "166****2222", "16622222222"
                },
                new Object[]{
                        "channelStaff", "176****8107", "17610248107"
                }
        };
    }

    @DataProvider(name = "ORDER_LIST_CHECK")
    public Object[][] orderListCheck1() {
        return new Object[][]{
//String channelId, int status, boolean isAudited, String namePhone, int pageSize
                new Object[]{
                        "5", "1", "true", "廖祥茹", 10
                },
                new Object[]{
                        "5", "2", "false", "", 10
                },
                new Object[]{
                        "1", "3", "true", "", 10
                },
        };
    }

    @DataProvider(name = "BAD_CHANNEL_STAFF")
    public Object[][] badChannelStaff() {
        return new Object[][]{
//String channelId, int status, boolean isAudited, String namePhone, int pageSize
                new Object[]{
                        "新建业务员（与置业顾问手机号相同）", "16622222222", "业务员手机号已被员工占用，请重新填写或更改员工信息"
                },
                new Object[]{
                        "新建业务员（与本渠道已启用业务员手机号相同）", "17610248107", "当前手机号17610248107已被使用"
                },
                new Object[]{
                        "新建业务员（与本渠道已禁用业务员手机号相同）", "17794123828", "当前手机号17794123828在本渠道被禁用，请先启用、修改业务员信息即可"
                },
                new Object[]{
                        "新建业务员（与其他渠道已启用业务员手机号相同）", "17711111024", "当前手机号17711111024已被使用"
                }
        };
    }

    @DataProvider(name = "BAD_ADVISER")
    public Object[][] badAdviser() {
        return new Object[][]{
//String channelId, int status, boolean isAudited, String namePhone, int pageSize
                new Object[]{
                        "新建置业顾问（与已启用业务员手机号相同）", "17610248107", "当前手机号已被使用"
                },
                new Object[]{
                        "新建置业顾问（与置业顾问手机号相同）", "16622222222", "当前手机号已被使用"
                }
        };
    }

    @DataProvider(name = "CATCH_DATE")
    public Object[][] catchDate() {
        return new Object[][]{
//start,end
                new Object[]{
                        LocalDate.now().toString(), LocalDate.now().toString()
                },
                new Object[]{
                        LocalDate.now().minusDays(7).toString(), LocalDate.now().toString()
                },
                new Object[]{
                        LocalDate.now().minusDays(30).toString(), LocalDate.now().toString()
                },
                new Object[]{
                        LocalDate.now().minusDays(365).toString(), LocalDate.now().toString()
                },
        };
    }
}

