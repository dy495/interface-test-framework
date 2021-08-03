package com.haisheng.framework.testng.bigScreen.itemMall.casedaily.gly;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemMall.casedaily.gly.Util.MallBusinessUtil;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.RegionTypeEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.TimeTypeEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview.OverviewVenueOverviewScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.CustomersPortraitScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.FullCourtTrendHistoryScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.HourPortraitScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.RegionTrendScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime.FullCourtTrendScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime.RealTimeOverviewScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.lang.reflect.Method;

public class MallSystemCase extends TestCaseCommon implements TestCaseStd {

    private final EnumTestProduct product = EnumTestProduct.MALL_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    MallBusinessUtil businessUtil=new MallBusinessUtil();
    StoreScenarioUtil su = StoreScenarioUtil.getInstance();
    public Long shopId = 28758L;
    public String shopName = "巡店测试门店1";
    CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.GLY.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = product.getAbbreviation();
        commonConfig.shopId = product.getShopId();
        commonConfig.referer = product.getReferer();
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
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        su.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
    }

    /**
     * --------------------------------------------------场馆客流分时数据---------------------------------------------------------
     */

    /**
     * 场馆客流总览中的到访人数<=到访人次(按天查询)
     */
    @Test(description = "场馆客流总览中的到访人数<=到访人次(按天查询)")
    public void mallCenterDataCase4(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().timeType(TimeTypeEnum.DAY.getTimeType()).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");
            //当前人数
            int numberUv=response.getJSONObject("uv_overview").getInteger("number");
            Preconditions.checkArgument(numberPv>=numberUv,"当前的人数的UV："+numberUv+"  当前人次的PV："+numberPv);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流总览中的到访人数<=到访人次(按天查询)");
        }
    }

    /**
     * 场馆客流分时总览中的日环比=昨日某个时间段的到访人数-前日某个时间段的到访人数/前日同一个时间段的到访人数
     */
    @Test(description = "场馆客流分时总览中的日环比=昨日某个时间段的到访人数-前日某个时间段的到访人数/前日同一个时间段的到访人数")
    public void mallCenterDataCase5(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流人数UV数据
            IScene scene= OverviewVenueOverviewScene.builder().timeType(TimeTypeEnum.DAY.getTimeType()).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的日环比
            int dayQoqUv=response.getJSONObject("uv_overview").getInteger("day_qoq");
            //获取系统的当前的小时
            int hour = Integer.parseInt(String.valueOf(dt.currentDateToTimestamp()).substring(11, 13));
            System.out.println("------"+hour);

            //获取历史的全场到访趋势图种的前一天当前小时的的UV数据
            IScene scene1= FullCourtTrendHistoryScene.builder().type("UV").date(businessUtil.getDateTime(-1)).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            // todo 全场到访趋势图中的人数的key
            int hourTodayUv=response1.getJSONArray("list").getJSONObject(hour).getInteger("");
            int hourYesterdayUv=response1.getJSONArray("list").getJSONObject(hour).getInteger("");
            long qoqUv=(hourTodayUv-hourYesterdayUv)/hourYesterdayUv;
            Preconditions.checkArgument(dayQoqUv==qoqUv,"页面中展示的日环比为："+dayQoqUv+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+qoqUv);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时总览中的日环比=昨日某个时间段的到访人数-前日某个时间段的到访人数/前日同一个时间段的到访人数");
        }
    }

    /**
     * 场馆客流分时的人次日环比=昨日某个时间段的到访人次-前日某个时间段的到访人次/前日同一个时间段的到访人次
     */
    @Test(description = "场馆客流分时的人次日环比=昨日某个时间段的到访人次-前日某个时间段的到访人次/前日同一个时间段的到访人次")
    public void mallCenterDataCase6(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流人数数据
            IScene scene= OverviewVenueOverviewScene.builder().timeType(TimeTypeEnum.DAY.getTimeType()).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次的日环比
            int dayQoqPv=response.getJSONObject("pv_overview").getInteger("day_qoq");
            //获取系统的当前的小时
            int hour = Integer.parseInt(String.valueOf(dt.currentDateToTimestamp()).substring(11, 13));
            System.out.println("------"+hour);

            //获取历史的全场到访趋势图种的当前小时的的PV数据
            IScene scene2=FullCourtTrendHistoryScene.builder().type("PV").date(businessUtil.getDateTime(-1)).build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            // todo 全场到访趋势图中的人数的key
            int hourTodayPv=response2.getJSONArray("list").getJSONObject(hour).getInteger("");
            int hourYesterdayPv=response2.getJSONArray("list").getJSONObject(hour).getInteger("");
            int qoqPv=(hourTodayPv-hourYesterdayPv)/hourYesterdayPv;
            Preconditions.checkArgument(dayQoqPv==qoqPv,"页面中展示的日环比为："+dayQoqPv+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+qoqPv);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的人次日环比=昨日某个时间段的到访人次-前日某个时间段的到访人次/前日同一个时间段的到访人次");
        }
    }


    /**
     * 场馆客流分时的人次周同比=本周昨天的人次－上周同一日到访人次/上周同一日到访人次
     */
    @Test(description = "场馆客流分时的人次周同比=本周昨天的人次－上周同一日到访人次/上周同一日到访人次")
    public void mallCenterDataCase7(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流人次数据
            IScene scene= OverviewVenueOverviewScene.builder().timeType(TimeTypeEnum.WEEK.getTimeType()).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次的周同比
            int preWeekCompare=response.getJSONObject("pv_overview").getInteger("pre_week_compare");
            //获取系统的当前的小时
            int hour = Integer.parseInt(String.valueOf(dt.currentDateToTimestamp()).substring(11, 13));
            System.out.println("------"+hour);

            //获取历史的全场到访趋势图种的昨天当前小时的的PV数据
            IScene scene2=FullCourtTrendHistoryScene.builder().type("PV").date(businessUtil.getDateTime(-1)).build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            // todo 全场到访趋势图中的人数的key
            int hourPv=response2.getJSONArray("list").getJSONObject(hour).getInteger("");

            //获取历史的全场到访趋势图种的前天当周小时的的PV数据
            IScene scene3=FullCourtTrendHistoryScene.builder().type("PV").date(businessUtil.getDateTime(-8)).build();
            JSONObject response3=visitor.invokeApi(scene3,true);
            // todo 全场到访趋势图中的人数的key
            int hourLastPv=response3.getJSONArray("list").getJSONObject(hour).getInteger("");

            int weekYoy=(hourPv-hourLastPv)/hourLastPv;

            Preconditions.checkArgument(preWeekCompare==weekYoy,"页面中展示的周同比为："+preWeekCompare+"   根据全场到访趋势图中上周和上上周的周同比计算结果为："+weekYoy);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的人次周同比=本周昨天的人次－上周同一日到访人次/上周同一日到访人次");
        }
    }

    /**
     * 场馆客流分时的人数周同比=本周昨天的到访人数-上周同一日的到访人数/上周同一日的到访人数
     */
    @Test(description = "场馆客流分时的人数周同比=本周昨天的到访人数-上周同一日的到访人数/上周同一日的到访人数")
    public void mallCenterDataCase8(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流人数数据
            IScene scene= OverviewVenueOverviewScene.builder().timeType(TimeTypeEnum.WEEK.getTimeType()).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的周同比
            int preWeekCompare=response.getJSONObject("uv_overview").getInteger("pre_week_compare");
            //获取系统的当前的小时
            int hour = Integer.parseInt(String.valueOf(dt.currentDateToTimestamp()).substring(11, 13));
            System.out.println("------"+hour);

            //获取历史的全场到访趋势图种的昨天当前小时的的UV数据
            IScene scene2=FullCourtTrendHistoryScene.builder().type("UV").date(businessUtil.getDateTime(-1)).build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            // todo 全场到访趋势图中的人数的key(可以写一个三元表达式)
            int hourUv=response2.getJSONArray("list").getJSONObject(hour).getInteger("");

            //获取历史的全场到访趋势图种的前天当前小时的的UV数据
            IScene scene3=FullCourtTrendHistoryScene.builder().type("UV").date(businessUtil.getDateTime(-8)).build();
            JSONObject response3=visitor.invokeApi(scene3,true);
            // todo 全场到访趋势图中的人数的key
            int hourLastUv=response3.getJSONArray("list").getJSONObject(hour).getInteger("");

            int weekYoy=(hourUv-hourLastUv)/hourLastUv;

            Preconditions.checkArgument(preWeekCompare==weekYoy,"页面中展示的周同比为："+preWeekCompare+"   根据全场到访趋势图中上周和上上周的周同比计算结果为："+weekYoy);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的人数的周同比=本周昨天的到访人数-上周同一日的到访人数/上周同一日的到访人数");
        }
    }

    /**
     * 场馆客流分时的到访人数<=【全场到访趋势图】种的各个时间段的人数相加
     */
    @Test(description = "场馆客流分时的到访人数<=【全场到访趋势图】种的各个时间段的人数相加")
    public void mallCenterDataCase9(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            ///获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().timeType(TimeTypeEnum.DAY.getTimeType()).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数
            int numberUv=response.getJSONObject("uv_overview").getInteger("number");

            //获取全场到访趋势图在中人数的之和
            IScene scene2=FullCourtTrendHistoryScene.builder().type("UV").date(businessUtil.getDateTime(-1)).build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            int number=0;
            JSONArray list=response2.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                // todo 全场到访趋势图中的人数的key
                int hourNum=list.getJSONObject(i).getInteger("");
                number+=hourNum;
            }

            Preconditions.checkArgument(numberUv<=number," 客流总览中的当前人数为："+numberUv+"    全场到访趋势图在中人数的之和为："+number);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的到访人数<=【全场到访趋势图】种的各个时间段的人数相加");
        }
    }

    /**
     * 场馆客流分时的到访人次=【全场到访趋势图】种的各个时间段的人次相加
     */
    @Test(description = "场馆客流分时的到访人次=【全场到访趋势图】种的各个时间段的人次相加")
    public void mallCenterDataCase10(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            ///获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().timeType(TimeTypeEnum.DAY.getTimeType()).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");

            //获取全场到访趋势图在中人次的之和
            IScene scene2=FullCourtTrendHistoryScene.builder().type("PV").date(businessUtil.getDateTime(-1)).build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            int number=0;
            JSONArray list=response2.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                // todo 全场到访趋势图中的人次的key
                int hourNum=list.getJSONObject(i).getInteger("");
                number+=hourNum;
            }

            Preconditions.checkArgument(numberPv<=number," 客流总览中的当前人次为："+numberPv+"    全场到访趋势图在中人次的之和为："+number);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的到访人次=【全场到访趋势图】种的各个时间段的人次相加");
        }
    }

    /**
     * 场馆客流分时的日环比=昨日某个时间段的人均停留时长－前日某个时间段的人均停留时长/前日相同时间段的人均停留时长
     */
    @Test(description = "场馆客流分时的日环比=昨日某个时间段的人均停留时长－前日某个时间段的人均停留时长/前日相同时间段的人均停留时长")
    public void mallCenterDataCase11(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            ///获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().timeType(TimeTypeEnum.DAY.getTimeType()).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //昨天人均停留时长
            int stayTimeOverview=response.getJSONObject("stay_time_overview").getInteger("number");
            //昨日的日环比
            int dayQoq=response.getJSONObject("stay_time_overview").getInteger("day_qoq");

            //获取前日的人均停留时长
            IScene scene1= OverviewVenueOverviewScene.builder().timeType(TimeTypeEnum.DAY.getTimeType()).time(businessUtil.getDate(0)).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            int stayTimeOverviewLast=response1.getJSONObject("stay_time_overview").getInteger("number");
            //计算日环比
            int qoqNum=(stayTimeOverview-stayTimeOverviewLast)/stayTimeOverviewLast;

            Preconditions.checkArgument(dayQoq==qoqNum,"  人均停留时长的客流总览中的日环比为："+dayQoq+"   通过计算昨天和前天人均停留时长的得来的日环比为："+qoqNum);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的日环比=昨日某个时间段的人均停留时长－前日某个时间段的人均停留时长/前日相同时间段的人均停留时长");
        }
    }

    /**
     * 场馆客流分时的周同比=上周今日的人均停留时长－上上周今日的人均停留时长/上上周同一日的人均停留时长
     */
    @Test(description = "场馆客流分时的周同比=上周今日的人均停留时长－上上周今日的人均停留时长/上上周同一日的人均停留时长")
    public void mallCenterDataCase12(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            ///获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().timeType(TimeTypeEnum.DAY.getTimeType()).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //昨天人均停留时长
            int stayTimeOverview=response.getJSONObject("stay_time_overview").getInteger("number");
            //昨日的日环比
            int preWeekCompare=response.getJSONObject("stay_time_overview").getInteger("pre_week_compare");

            //获取上周昨日的的人均停留时长
            IScene scene1= OverviewVenueOverviewScene.builder().timeType(TimeTypeEnum.DAY.getTimeType()).time(businessUtil.getDate(-8)).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            int stayTimeOverviewLast=response1.getJSONObject("stay_time_overview").getInteger("number");

            //计算日环比
            int preWeekCompareNum=(stayTimeOverview-stayTimeOverviewLast)/stayTimeOverviewLast;

            Preconditions.checkArgument(preWeekCompare==preWeekCompareNum,"  人均停留时长的客流总览中的日环比为："+preWeekCompare+"   通过计算昨天和前天人均停留时长的得来的日环比为："+preWeekCompareNum);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的周同比=上周今日的人均停留时长－上上周今日的人均停留时长/上上周同一日的人均停留时长");
        }
    }

    /**
     * 场馆客流分时的进出口到访趋势图-各入口的人次的占比相加为100%
     */
    @Test(description = "场馆客流分时的进出口到访趋势图-各入口的人次的占比相加为100%")
    public void mallCenterDataCase13(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数占比
            IScene scene= HourPortraitScene.builder().date(businessUtil.getDate(-1)).region(RegionTypeEnum.EXIT.getRegion()).build();
            JSONArray list=visitor.invokeApi(scene,true).getJSONArray("list");
            int portNum=0;
            for(int i=0;i<=list.size();i++){
                int number=list.getJSONObject(i).getInteger("percentage");
                portNum+=number;
            }

            Preconditions.checkArgument(portNum==1,"进出口到访趋势图-各入口的人次的占比相加为："+portNum);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的进出口到访趋势图-各入口的人次的占比相加为100%");
        }
    }

    /**
     * 场馆客流分时的进出口到访趋势图某一时间点各个折线的UV的百分相加为100%
     */
    @Test(description = "场馆客流分时的进出口到访趋势图某一时间点各个折线的UV的百分相加为100%")
    public void mallCenterDataCase14(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数占比
            IScene scene= RegionTrendScene.builder().date(businessUtil.getDate(-1)).type("UV").region(RegionTypeEnum.EXIT.getRegion()).build();
            JSONArray list=visitor.invokeApi(scene,true).getJSONArray("list");
            int portNum=0;
            for(int i=0;i<=list.size();i++){
                // todo 获取各个折线图各个小时的百分比
                int number1=list.getJSONObject(i).getInteger("");
                int number2=list.getJSONObject(i).getInteger("");

                Preconditions.checkArgument( (number1+number2)==1,"在"+i+"进出口到访趋势图-各入口的人数的占比相加："+portNum+"   不是100%");
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的进出口到访趋势图某一时间点各个折线的UV的百分相加为100%");
        }
    }

    /**
     * 场馆客流分时的进出口到访趋势图某一时间点各个折线的PV的百分相加为100%
     */
    @Test(description = "场馆客流分时的进出口到访趋势图某一时间点各个折线的PV的百分相加为100%")
    public void mallCenterDataCase15(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数占比
            IScene scene= RegionTrendScene.builder().date(businessUtil.getDate(-1)).type("PV").region(RegionTypeEnum.EXIT.getRegion()).build();
            JSONArray list=visitor.invokeApi(scene,true).getJSONArray("list");
            int portNum=0;
            for(int i=0;i<=list.size();i++){
                // todo 获取各个折线图各个小时的百分比
                int number1=list.getJSONObject(i).getInteger("");
                int number2=list.getJSONObject(i).getInteger("");

                Preconditions.checkArgument( (number1+number2)==1,"在"+i+"进出口到访趋势图-各入口的人数的占比相加："+portNum+"   不是100%");
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的进出口到访趋势图某一时间点各个折线的PV的百分相加为100%");
        }
    }

    /**
     * 场馆客流分时的各停车场到访人次占比=各停车场到访人次/停车场到访的总人次*100%
     */
    @Test(description = "场馆客流分时的各停车场到访人次占比=各停车场到访人次/停车场到访的总人次*100%")
    public void mallCenterDataCase16(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数占比
            IScene scene= HourPortraitScene.builder().date(businessUtil.getDate(-1)).region(RegionTypeEnum.PARKING.getRegion()).build();
            JSONArray list=visitor.invokeApi(scene,true).getJSONArray("list");
            int portNum=0;
            for(int i=0;i<=list.size();i++){
                int number=list.getJSONObject(i).getInteger("percentage");
                portNum+=number;
            }

            Preconditions.checkArgument(portNum==1,"进出口到访趋势图-各入口的人次的占比相加为："+portNum);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        }finally {
            saveData("场馆客流分时的各停车场到访人次占比=各停车场到访人次/停车场到访的总人次*100%");
        }
    }

    /**
     * 场馆客流分时的停车厂到访趋势图某一时间点各个折线的UV的百分相加为100%
     */
    @Test(description = "场馆客流分时的停车厂到访趋势图某一时间点各个折线的UV的百分相加为100%")
    public void mallCenterDataCase17(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数占比
            IScene scene= RegionTrendScene.builder().date(businessUtil.getDate(-1)).type("UV").region(RegionTypeEnum.PARKING.getRegion()).build();
            JSONArray list=visitor.invokeApi(scene,true).getJSONArray("list");
            int portNum=0;
            for(int i=0;i<=list.size();i++){
                // todo 获取各个折线图各个小时的百分比
                int number1=list.getJSONObject(i).getInteger("");
                int number2=list.getJSONObject(i).getInteger("");

                Preconditions.checkArgument( (number1+number2)==1,"在"+i+"进出口到访趋势图-各入口的人数的占比相加："+portNum+"   不是100%");
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的停车厂到访趋势图某一时间点各个折线的UV的百分相加为100%");
        }
    }

    /**
     * 场馆客流分时的停车厂到访趋势图某一时间点各个折线的PV的百分相加为100%
     */
    @Test(description = "场馆客流分时的停车厂到访趋势图某一时间点各个折线的PV的百分相加为100%")
    public void mallCenterDataCase18(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数占比
            IScene scene= RegionTrendScene.builder().date(businessUtil.getDate(-1)).type("PV").region(RegionTypeEnum.PARKING.getRegion()).build();
            JSONArray list=visitor.invokeApi(scene,true).getJSONArray("list");
            int portNum=0;
            for(int i=0;i<=list.size();i++){
                // todo 获取各个折线图各个小时的百分比
                int number1=list.getJSONObject(i).getInteger("");
                int number2=list.getJSONObject(i).getInteger("");

                Preconditions.checkArgument( (number1+number2)==1,"在"+i+"进出口到访趋势图-各入口的人次的占比相加："+portNum+"   不是100%");
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的停车厂到访趋势图某一时间点各个折线的UV的百分相加为100%");
        }
    }

    /**
     * 场馆客流分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比
     */
    @Test(description = "场馆客流分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比")
    public void mallCenterDataCase19(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
           //获取各个年龄段的占比
            IScene scene= CustomersPortraitScene.builder().scene("VENUE").date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            Long malePercent=response.getLong("male_percent");
            Long femalePercent=response.getLong("female_percent");
            Long sum=malePercent+femalePercent;

            Preconditions.checkArgument(sum==1,"男性占比和女性占比之和为："+sum);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比");
        }
    }

    /**
     * 场馆客流分时的男性占比+女性占比=100%
     */
    @Test(description = "场馆客流分时的男性占比+女性占比=100%")
    public void mallCenterDataCase20(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取各个年龄段的占比
            IScene scene= CustomersPortraitScene.builder().scene("VENUE").date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            Long malePercent=response.getLong("male_ratio_str");
            Long femalePercent=response.getLong("female_ratio_str");
            Long sum=malePercent+femalePercent;

            Preconditions.checkArgument(sum==1,"男性占比和女性占比之和为："+sum);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的男性占比+女性占比=100%");
        }
    }

    /**
     * 场馆客流分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比
     */
    @Test(description = "场馆客流分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比")
    public void mallCenterDataCase21(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取各个年龄段的占比
            IScene scene= CustomersPortraitScene.builder().scene("VENUE").date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            JSONArray list=response.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                Long malePercent=list.getJSONObject(i).getLong("male_percent");
                Long femalePercent=list.getJSONObject(i).getLong("female_percent");
                Long ageGroupPercent=list.getJSONObject(i).getLong("age_group_percent");

                Preconditions.checkArgument(ageGroupPercent==(malePercent+femalePercent),"男性占比为："+malePercent+"  女性占比为："+femalePercent+"  年龄段占比为："+ageGroupPercent);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比场馆客流分时");
        }
    }

    /**
     * 女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加（分时数据）
     */
    @Test(description = "女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加（分时数据）")
    public void mallCenterDataCase22(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取各个年龄段的占比
            IScene scene= CustomersPortraitScene.builder().scene("VENUE").date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            Long malePercentStr=response.getLong("male_ratio_str");
            Long femalePercentStr=response.getLong("female_ratio_str");
            JSONArray list=response.getJSONArray("list");
            Long maleNum=0L;
            Long femaleNum=0L;
            for(int i=0;i<list.size();i++){
                Long malePercent=list.getJSONObject(i).getLong("male_percent");
                Long femalePercent=list.getJSONObject(i).getLong("female_percent");
                maleNum+=malePercent;
                femaleNum+=femalePercent;
            }
            Preconditions.checkArgument(malePercentStr.equals(maleNum) ,"女性占比为："+malePercentStr+"  各年龄段女性占比相加为："+maleNum);
            Preconditions.checkArgument( femalePercentStr.equals(femaleNum),"男性占比为："+femalePercentStr+"  各年龄段男性占比相加为："+femaleNum);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加（分时数据）");
        }
    }


    /**
     * -----------------------------------------------------------实时客流数据分析---------------------------------------------------
     */
    /**
     * 实时客流的到访人数<=到访人次
     */
    @Test(description = "实时客流的到访人数<=到访人次")
    public void mallCenterDataCase1(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流数据
            IScene scene= RealTimeOverviewScene.builder().build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");
            //当前人数
            int numberUv=response.getJSONObject("uv_overview").getInteger("number");
            Preconditions.checkArgument(numberPv>=numberUv,"当前的人数的UV："+numberUv+"  当前人次的PV："+numberPv);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流的到访人数<=到访人次");
        }
    }

    /**
     * 实时客流的日环比=昨日某个时间段的到访人数-前日某个时间段的到访人数/前日同一个时间段的到访人数
     */
    @Test(description = "日环比=昨日某个时间段的到访人数-前日某个时间段的到访人数/前日同一个时间段的到访人数")
    public void mallCenterDataCase2(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流人数UV数据
            IScene scene= RealTimeOverviewScene.builder().build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的日环比
            int dayQoqUv=response.getJSONObject("uv_overview").getInteger("day_qoq");
            //获取系统的当前的小时
            int hour = Integer.parseInt(String.valueOf(dt.currentDateToTimestamp()).substring(11, 13));
            System.out.println("------"+hour);

            //获取实时的全场到访趋势图种的当前小时的的UV数据
            IScene scene1=FullCourtTrendScene.builder().type("UV").build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            // todo 全场到访趋势图中的人数的key
            int hourTodayUv=response1.getJSONArray("list").getJSONObject(hour).getInteger("");
            int hourYesterdayUv=response1.getJSONArray("list").getJSONObject(hour).getInteger("");
            long qoqUv=(hourTodayUv-hourYesterdayUv)/hourYesterdayUv;
            Preconditions.checkArgument(dayQoqUv==qoqUv,"页面中展示的日环比为："+dayQoqUv+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+qoqUv);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("日环比=昨日某个时间段的到访人数-前日某个时间段的到访人数/前日同一个时间段的到访人数");
        }
    }

    /**
     * 实时客流的人次日环比=昨日某个时间段的到访人次-前日某个时间段的到访人次/前日同一个时间段的到访人次
     */
    @Test(description = "实时客流的人次日环比=昨日某个时间段的到访人次-前日某个时间段的到访人次/前日同一个时间段的到访人次")
    public void mallCenterDataCase3(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流人数UV数据
            IScene scene= RealTimeOverviewScene.builder().build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次的日环比
            int dayQoqPv=response.getJSONObject("pv_overview").getInteger("day_qoq");
            //获取系统的当前的小时
            int hour = Integer.parseInt(String.valueOf(dt.currentDateToTimestamp()).substring(11, 13));
            System.out.println("------"+hour);

            //获取实时的全场到访趋势图种的当前小时的的PV数据
            IScene scene2=FullCourtTrendScene.builder().type("PV").build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            // todo 全场到访趋势图中的人数的key
            int hourTodayPv=response2.getJSONArray("list").getJSONObject(hour).getInteger("");
            int hourYesterdayPv=response2.getJSONArray("list").getJSONObject(hour).getInteger("");
            int qoqPv=(hourTodayPv-hourYesterdayPv)/hourYesterdayPv;
            Preconditions.checkArgument(dayQoqPv==qoqPv,"页面中展示的日环比为："+dayQoqPv+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+qoqPv);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流的人次日环比=昨日某个时间段的到访人次-前日某个时间段的到访人次/前日同一个时间段的到访人次");
        }
    }


    /**
     * 实时客流的今日到访人数-上周同时间到访人数/上周同时间到访人数
     */
    @Test(description = "实时客流的今日到访人数-上周同时间到访人数/上周同时间到访人数")
    public void mallCenterDataCase23(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流人数UV数据
            IScene scene= RealTimeOverviewScene.builder().build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的日环比
            int preWeekCompare=response.getJSONObject("uv_overview").getInteger("pre_week_compare");
            //获取系统的当前的小时
            int hour = Integer.parseInt(String.valueOf(dt.currentDateToTimestamp()).substring(11, 13));
            System.out.println("------"+hour);

            //获取实时的全场到访趋势图种的当前小时的的UV数据
            IScene scene1=FullCourtTrendScene.builder().type("UV").build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            // todo 全场到访趋势图中的人数的key
            int hourTodayUv=response1.getJSONArray("list").getJSONObject(hour).getInteger("");

            //获取场馆客流分时数据中的当前小时的UV
            //获取历史的全场到访趋势图种的前天当周小时的的PV数据
            IScene scene3=FullCourtTrendHistoryScene.builder().type("UV").date(businessUtil.getDateTime(-7)).build();
            JSONObject response3=visitor.invokeApi(scene3,true);
            // todo 全场到访趋势图中的人数的key
            int hourLastWeekPv=response3.getJSONArray("list").getJSONObject(hour).getInteger("");

            long weekUv=(hourTodayUv-hourLastWeekPv)/hourLastWeekPv;
            Preconditions.checkArgument(weekUv==preWeekCompare,"页面中展示的日环比为："+preWeekCompare+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+weekUv);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流的今日到访人数-上周同时间到访人数/上周同时间到访人数");
        }
    }

    /**
     * 实时客流的今日到访人次-上周同时间到访人次/上周同时间到访人次
     */
    @Test(description = "实时客流的今日到访人次-上周同时间到访人次/上周同时间到访人次")
    public void mallCenterDataCase24(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流人数PV数据
            IScene scene= RealTimeOverviewScene.builder().build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的日环比
            int preWeekCompare=response.getJSONObject("pv_overview").getInteger("pre_week_compare");
            //获取系统的当前的小时
            int hour = Integer.parseInt(String.valueOf(dt.currentDateToTimestamp()).substring(11, 13));
            System.out.println("------"+hour);

            //获取实时的全场到访趋势图种的当前小时的的PV数据
            IScene scene1=FullCourtTrendScene.builder().type("PV").build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            // todo 全场到访趋势图中的人数的key
            int hourTodayUv=response1.getJSONArray("list").getJSONObject(hour).getInteger("");

            //获取历史的全场到访趋势图种的前天当周小时的的PV数据
            IScene scene3=FullCourtTrendHistoryScene.builder().type("PV").date(businessUtil.getDateTime(-7)).build();
            JSONObject response3=visitor.invokeApi(scene3,true);
            // todo 全场到访趋势图中的人数的key
            int hourLastWeekPv=response3.getJSONArray("list").getJSONObject(hour).getInteger("");

            long weekUv=(hourTodayUv-hourLastWeekPv)/hourLastWeekPv;
            Preconditions.checkArgument(weekUv==preWeekCompare,"页面中展示的日环比为："+preWeekCompare+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+weekUv);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流的今日到访人次-上周同时间到访人次/上周同时间到访人次");
        }
    }

    /**
     * 实时客流的今日到访人次==场馆内到访人次之和
     */
    @Test(description = "实时客流的今日到访人次==场馆内到访人次之和")
    public void mallCenterDataCase25(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流数据
            IScene scene= RealTimeOverviewScene.builder().build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");

            //全场到访趋势图中的PV的总人次
            IScene scene1=FullCourtTrendHistoryScene.builder().build();
            // todo 全场到访趋势图中的人次的key
            JSONArray list=visitor.invokeApi(scene1,true).getJSONArray("list");
            int pvSum=0;
            for(int i=0;i<list.size();i++){
                int hourPv=list.getJSONObject(i).getInteger("");
                pvSum+=hourPv;
            }

            Preconditions.checkArgument(numberPv==pvSum,"今日到访人次为："+numberPv+"  全场到访趋势图中的PV的总人次为："+pvSum);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流的今日到访人次==场馆内到访人次之和");
        }
    }

    /**
     * 实时客流的今日到访人数<=场馆内到访人数之和
     */
    @Test(description = "实时客流的今日到访人数<=场馆内到访人数之和")
    public void mallCenterDataCase26(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流数据
            IScene scene= RealTimeOverviewScene.builder().build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数
            int numberUv=response.getJSONObject("uv_overview").getInteger("number");

            //全场到访趋势图中的UV的总人次
            IScene scene1=FullCourtTrendHistoryScene.builder().build();
            // todo 全场到访趋势图中的人次的key
            JSONArray list=visitor.invokeApi(scene1,true).getJSONArray("list");
            int uvSum=0;
            for(int i=0;i<list.size();i++){
                int hourPv=list.getJSONObject(i).getInteger("");
                uvSum+=hourPv;
            }

            Preconditions.checkArgument(numberUv==uvSum,"今日到访人次为："+numberUv+"  全场到访趋势图中的PV的总人次为："+uvSum);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流的今日到访人数<=场馆内到访人数之和");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase27(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase28(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase29(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase30(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase31(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase32(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase33(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase34(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase35(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase36(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase37(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase38(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase39(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase40(){
        logger.logCaseStart(caseResult.getCaseName());
        try{



        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }













}
