package com.haisheng.framework.testng.patrolShops;

import com.arronlong.httpclientutil.common.HttpConfig;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.bigScreen.feidanDaily.Feidan;
import com.haisheng.framework.util.CheckUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.time.LocalDate;

/**
 * @author : huachengyu
 * @date :  2019/11/21  14:55
 */

public class PatrolShopsAPPDaily {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String failReason = "";
    private String response = "";
    private boolean FAIL = false;
    private Case aCase = new Case();

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


}

