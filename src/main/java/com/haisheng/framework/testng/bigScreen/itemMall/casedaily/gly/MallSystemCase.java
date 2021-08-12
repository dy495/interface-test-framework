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
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.FloorTypeEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.RegionTypeEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.SortTypeEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview.OverviewFloorOverviewScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview.OverviewVenueOverviewScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.shop.ShopPageScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.*;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.CustomersPortraitScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.RegionTrendScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime.*;
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
import java.util.Arrays;
public class MallSystemCase extends TestCaseCommon implements TestCaseStd {

    private final EnumTestProduct product = EnumTestProduct.MALL_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    MallBusinessUtil businessUtil=new MallBusinessUtil();
    StoreScenarioUtil su = StoreScenarioUtil.getInstance();
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
        commonConfig.mallId="55456";
        commonConfig.roleId=product.getRoleId();
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
        //登录的日常的账号
        su.loginInMall("yuexiu@test.com","f5b3e737510f31b88eb2d4b5d0cd2fb4");
        //切换角色

    }



     //--------------------------------------------------场馆客流分时数据---------------------------------------------------------


    /**
     * 场馆客流总览中的到访人数<=到访人次(按天查询)---ok
     */
    @Test(description = "场馆客流总览中的到访人数<=到访人次(按天查询)")
    public void mallCenterDataCase4(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-1)).build();
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
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDateTime(-1)).endTime(businessUtil.getDateTime(-1)).build();
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
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDateTime(-1)).endTime(businessUtil.getDateTime(-1)).build();
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
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDateTime(-1)).endTime(businessUtil.getDateTime(-1)).build();
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
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDateTime(-1)).endTime(businessUtil.getDateTime(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的周同比
            int preWeekCompare=response.getJSONObject("uv_overview").getInteger("pre_week_compare");
            //获取系统的当前的小时
            int hour = Integer.parseInt(String.valueOf(dt.currentDateToTimestamp()).substring(11, 13));
            System.out.println("------"+hour);

            //获取历史的全场到访趋势图中的昨天当前小时的的UV数据
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
     * 场馆客流分时/楼层客流分时的到访人数<=【全场到访趋势图】种的各个时间段的人数相加
     */
    @Test(description = "场馆客流分时的到访人数<=【全场到访趋势图】种的各个时间段的人数相加")
    public void mallCenterDataCase9(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDateTime(-1)).endTime(businessUtil.getDateTime(-1)).build();
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
     * 场馆客流分时的到访人次=【全场到访趋势图】中的各个时间段的人次相加
     */
    @Test(description = "场馆客流分时的到访人次=【全场到访趋势图】种的各个时间段的人次相加")
    public void mallCenterDataCase10(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            ///获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDateTime(-1)).endTime(businessUtil.getDateTime(-1)).build();
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
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDateTime(-1)).endTime(businessUtil.getDateTime(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //昨天人均停留时长
            int stayTimeOverview=response.getJSONObject("stay_time_overview").getInteger("number");
            //昨日的日环比
            int dayQoq=response.getJSONObject("stay_time_overview").getInteger("day_qoq");

            //获取前日的人均停留时长
            IScene scene1= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDateTime(-1)).endTime(businessUtil.getDateTime(-1)).build();
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
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDateTime(-1)).endTime(businessUtil.getDateTime(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //昨天人均停留时长
            int stayTimeOverview=response.getJSONObject("stay_time_overview").getInteger("number");
            //昨日的日环比
            int preWeekCompare=response.getJSONObject("stay_time_overview").getInteger("pre_week_compare");

            //获取上周昨日的的人均停留时长
            IScene scene1= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDateTime(-1)).endTime(businessUtil.getDateTime(-1)).build();
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
     * 场馆客流分时/楼层客流分时的进出口到访趋势图-各入口的人次的占比相加为100%
     */
    @Test(description = "场馆客流分时/楼层客流分时的进出口到访趋势图-各入口的人次的占比相加为100%")
    public void mallCenterDataCase13(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] array={RegionTypeEnum.EXIT.getRegion(),RegionTypeEnum.FLOOR.getRegion()};
            Arrays.stream(array).forEach(e->{
                if(e.equals(RegionTypeEnum.EXIT.getRegion())){
                    //获取楼层出入口的各个人数占比
                    IScene scene= HourPortraitScene.builder().date(businessUtil.getDate(-1)).region(e).build();
                    JSONArray list=visitor.invokeApi(scene,true).getJSONArray("list");
                    int portNum=0;
                    for(int i=0;i<=list.size();i++){
                        int number=list.getJSONObject(i).getInteger("percentage");
                        portNum+=number;
                    }
                    Preconditions.checkArgument(portNum==1,"进出口到访趋势图-各入口的人次的占比相加为："+portNum);
                }else{
                    //获取楼层出入口的各个人数占比
                    IScene scene= HourPortraitScene.builder().date(businessUtil.getDate(-1)).region(e).floorId(1L).build();
                    JSONArray list=visitor.invokeApi(scene,true).getJSONArray("list");
                    int portNum=0;
                    for(int i=0;i<=list.size();i++){
                        int number=list.getJSONObject(i).getInteger("percentage");
                        portNum+=number;
                    }
                    Preconditions.checkArgument(portNum==1,"进出口到访趋势图-各入口的人次的占比相加为："+portNum);
                }
            });

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时/楼层客流分时的进出口到访趋势图-各入口的人次的占比相加为100%");
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
     * 场馆客流分时的进出口到访趋势图-各入口的人次的占比相加为100%
     */
    @Test(description = "场馆客流分时的进出口到访趋势图-各入口的人次的占比相加为100%")
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
            saveData("场馆客流分时的进出口到访趋势图-各入口的人次的占比相加为100%");
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
            saveData("场馆客流分时的停车厂到访趋势图某一时间点各个折线的PV的百分相加为100%");
        }
    }

    /**
     * 场馆客流分时/楼层客群分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比
     */
    @Test(description = "场馆客流分时/楼层客群分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比")
    public void mallCenterDataCase19(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sceneArray={"VENUE","FLOOR"};
            Arrays.stream(sceneArray).forEach(scene->{
                if(scene.equals("VENUE")){
                    //获取各个年龄段的占比
                    IScene scene1= CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-1)).build();
                    JSONObject response=visitor.invokeApi(scene1,true);
                    long malePercent=response.getLong("male_percent");
                    long femalePercent=response.getLong("female_percent");
                    long sum=malePercent+femalePercent;

                    Preconditions.checkArgument(sum==1,"男性占比和女性占比之和为："+sum);
                }else{
                    //获取各个年龄段的占比
                    IScene scene1= CustomersPortraitScene.builder().scene(scene).floorId(1L).date(businessUtil.getDate(-1)).build();
                    JSONObject response=visitor.invokeApi(scene1,true);
                    long malePercent=response.getLong("male_percent");
                    long femalePercent=response.getLong("female_percent");
                    long sum=malePercent+femalePercent;

                    Preconditions.checkArgument(sum==1,"男性占比和女性占比之和为："+sum);
                }
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时/楼层客群分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比");
        }
    }

    /**
     * 场馆客流分时/楼层客流分时的男性占比+女性占比=100%
     */
    @Test(description = "场馆客流分时/楼层客流分时的男性占比+女性占比=100%")
    public void mallCenterDataCase20(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sceneArray={"VENUE","FLOOR"};
            Arrays.stream(sceneArray).forEach(scene->{
                if(scene.equals("VENUE")){
                    //获取各个年龄段的占比
                    IScene scene1= CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-1)).build();
                    JSONObject response=visitor.invokeApi(scene1,true);
                    Long malePercent=response.getLong("male_ratio_str");
                    Long femalePercent=response.getLong("female_ratio_str");

                    Preconditions.checkArgument((malePercent+femalePercent)==1,"男性占比和女性占比之和为："+(malePercent+femalePercent));

                }else{
                    //获取各个年龄段的占比
                    IScene scene2= CustomersPortraitScene.builder().scene(scene).floorId(1L).date(businessUtil.getDate(-1)).build();
                    JSONObject response=visitor.invokeApi(scene2,true);
                    Long malePercent=response.getLong("male_ratio_str");
                    Long femalePercent=response.getLong("female_ratio_str");
                    Preconditions.checkArgument((malePercent+femalePercent)==1,"男性占比和女性占比之和为："+(malePercent+femalePercent));
                }
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时/楼层客流分时的男性占比+女性占比=100%");
        }
    }

    /**
     * 场馆客流分时/楼层客流分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比
     */
    @Test(description = "场馆客流分时/楼层客流分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比")
    public void mallCenterDataCase21(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sceneArray={"VENUE","FLOOR"};
            Arrays.stream(sceneArray).forEach(scene->{
                if(scene.equals("VENUE")){
                    //获取各个年龄段的占比
                    IScene scene1= CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-1)).build();
                    JSONObject response=visitor.invokeApi(scene1,true);
                    JSONArray list=response.getJSONArray("list");
                    for(int i=0;i<list.size();i++){
                        long malePercent=list.getJSONObject(i).getLong("male_percent");
                        long femalePercent=list.getJSONObject(i).getLong("female_percent");
                        long ageGroupPercent=list.getJSONObject(i).getLong("age_group_percent");
                        Preconditions.checkArgument(ageGroupPercent==(malePercent+femalePercent),"男性占比为："+malePercent+"  女性占比为："+femalePercent+"  年龄段占比为："+ageGroupPercent);
                    }
                }else {
                    //获取各个年龄段的占比
                    IScene scene1 = CustomersPortraitScene.builder().scene(scene).floorId(1L).date(businessUtil.getDate(-1)).build();
                    JSONObject response = visitor.invokeApi(scene1, true);
                    JSONArray list = response.getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        long malePercent = list.getJSONObject(i).getLong("male_percent");
                        long femalePercent = list.getJSONObject(i).getLong("female_percent");
                        long ageGroupPercent = list.getJSONObject(i).getLong("age_group_percent");
                        Preconditions.checkArgument(ageGroupPercent==(malePercent+femalePercent),"男性占比为："+malePercent+"  女性占比为："+femalePercent+"  年龄段占比为："+ageGroupPercent);
                    }
                }
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时/楼层客流分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比");
        }
    }

    /**
     * 女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加（分时数据）
     */
    @Test(description = "女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加（分时数据）")
    public void mallCenterDataCase22(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sceneArray={"VENUE","FLOOR"};
            Arrays.stream(sceneArray).forEach(scene->{
                if(scene.equals("VENUE")){
                    //获取各个年龄段的占比
                    IScene scene1= CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-1)).build();
                    JSONObject response=visitor.invokeApi(scene1,true);
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
                }else {
                    //获取各个年龄段的占比
                    IScene scene1= CustomersPortraitScene.builder().scene(scene).floorId(1L).date(businessUtil.getDate(-1)).build();
                    JSONObject response=visitor.invokeApi(scene1,true);
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
                }
            });


        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加（分时数据）");
        }
    }



     // -----------------------------------------------------------实时客流数据分析---------------------------------------------------


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
     *实时客流-楼层出入口和停车场环形图百分比之和=100%
     */
    @Test(description = "实时客流-楼层出入口和停车场环形图百分比之和=100%")
    public void mallCenterDataCase27(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] regionArray={RegionTypeEnum.EXIT.getRegion(),RegionTypeEnum.PARKING.getRegion()};
            Arrays.stream(regionArray).forEach(Region->{
                //获取实时客流的饼状图数据
                IScene scene= RealTimePortraitScene.builder().region(Region).build();
                JSONObject response=visitor.invokeApi(scene,true);
                JSONArray list=response.getJSONArray("list");
                Long percentSum=0L;
                for(int i=0;i<=list.size();i++){
                    Long percent=list.getJSONObject(i).getLong("value");
                    percentSum+=percent;
                }
                Preconditions.checkArgument(percentSum==1,"实时客流的楼层各个出入口的百分比相加的总和为："+percentSum);
            });

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流-楼层出入口和停车场环形图百分比之和=100%");
        }
    }

    /**
     *实时客流-楼层到访趋势图累计到访人数和>=今日到访人数
     */
    @Test(description = "实时客流-楼层到访趋势图累计到访人数和>=今日到访人数")
    public void mallCenterDataCase28(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层到访趋势的数据
            IScene scene= RealTimeFloorVisitTrendScene.builder().type(FloorTypeEnum.VISIT.getFloorType()).build();
            JSONObject response=visitor.invokeApi(scene,true);
            JSONArray list=response.getJSONArray("list");
            int sum=0;
            for(int i=0;i<list.size();i++){
                int number=list.getJSONObject(i).getInteger("number");
                sum+=number;
            }

            //获取楼层到访趋势的折线图的各个时间的人数
            IScene scene1= FloorTrendScene.builder().build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            JSONArray list1=response1.getJSONArray("list");
            for(int i=0;i<list1.size();i++){
                // todo 楼层到访趋势的折线图的人数的key
                int num=list.getJSONObject(i).getInteger("");

                Preconditions.checkArgument(num<=sum,"当前各个楼层的人数之和为："+sum+"  超过了累计人数之和的人数数量为："+num);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流-楼层到访趋势图累计到访人数和>=今日到访人数");
        }
    }

    /**
     *过店人数/进店人数/进店率/光顾率的柱状图的排名前五和后五的柱状图从大到小和从小到大的比较
     */
    @Test(description = "过店人数/进店人数/进店率/光顾率的柱状图的排名前五和后五的柱状图从大到小和从小到大的比较")
    public void mallCenterDataCase29(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sortTypeArray={SortTypeEnum.ENTRY.getSortType(),SortTypeEnum.ENTRY_PERCENTAGE.getSortType(),SortTypeEnum.PASS.getSortType(),SortTypeEnum.VISIT_PERCENTAGE.getSortType()};
            Arrays.stream(sortTypeArray).forEach(type->{
                //获取柱状图的数据
                IScene scene=RealTimeColumnarRankingScene.builder().type(type).build();
                JSONObject response=visitor.invokeApi(scene,true);
                //排名前五的list  todo list不是这个名字呀
                JSONArray topList=response.getJSONArray("list");
                JSONArray lastList=response.getJSONArray("list");
                for(int i=0;i<topList.size()-1;i++){
                    int number=topList.getJSONObject(i).getInteger("number");
                    int numberNext=topList.getJSONObject(i+1).getInteger("number");
                    Preconditions.checkArgument(number>=numberNext,"按照排名前五的门店进行并排序，前一位的为："+number+"   后一位的为："+numberNext);
                }

                for(int i=0;i<lastList.size()-1;i++){
                    int number=lastList.getJSONObject(i).getInteger("number");
                    int numberNext=lastList.getJSONObject(i+1).getInteger("number");
                    Preconditions.checkArgument(number<=numberNext,"按照排名后五的门店进行并排序，前一位的为："+number+"   后一位的为："+numberNext);
                }


            });

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("过店人数/进店人数/进店率/光顾率的柱状图的排名前五和后五的柱状图从大到小和从小到大的比较");
        }
    }

    /**
     *实时客流-男性女性各个年龄段的占比相加==100%
     */
    @Test(description = "实时客流-男性女性各个年龄段的占比相加==100%")
    public void mallCenterDataCase30(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流的客群画像的占比
            IScene scene =CustomersPortraitScene.builder().mallId(999L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            JSONArray list=response.getJSONArray("list");
            Long sum=0L;
            for(int i=0;i<list.size();i++){
                Long percent=list.getJSONObject(i).getLong("age_group_percent");
                sum+=percent;
            }
            Preconditions.checkArgument(sum==1,"所有年龄段的人数占比的相加之和为："+sum);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流-男性女性各个年龄段的占比相加==100%");
        }
    }


     // ----------------------------------------------------------楼层客流总览-----------------------------------------------------------


    /**
     *楼层分时客流：当日到访人数日环比=昨天的uv-前天的uv/前天的uv
     */
    @Test(description = "楼层客流：日环比=昨天的uv-前天的uv/前天的uv")
    public void mallCenterDataCase31(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(1L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的日环比
            String dayQoq=response.getJSONObject("uv_overview").getString("day_qoq");
            //当日的的人数
            int number=response.getJSONObject("uv_overview").getInteger("number");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDateTime(-1)).floorId(1L).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取人数
            int numberLast=response1.getJSONObject("uv_overview").getInteger("number");
            String percent= String.valueOf((number-numberLast)/numberLast);

            Preconditions.checkArgument(dayQoq.equals(percent),"通过昨天和前天的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层客流：日环比=昨天的uv-前天的uv/前天的uv");
        }
    }

    /**
     *楼层分时客流：当日到访人数周同比=昨天的uv-上周同天uv/上周同天的uv
     */
    @Test(description = "楼层分时客流：当日到访人数周同比=昨天的uv-上周同天uv/上周同天的uv")
    public void mallCenterDataCase32(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(1L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的周同比
            String preWeekCompare=response.getJSONObject("uv_overview").getString("pre_week_compare");
            //当日的的人数
            int number=response.getJSONObject("uv_overview").getInteger("number");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDateTime(-8)).floorId(1L).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取人数
            int numberLast=response1.getJSONObject("uv_overview").getInteger("number");
            String percent= String.valueOf((number-numberLast)/numberLast);

            Preconditions.checkArgument(preWeekCompare.equals(percent),"通过昨天和上周同一天的数据计算出的周同比为："+percent+ "  页面中展示的周同比为："+preWeekCompare);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：当日到访人数周同比=昨天的uv-上周同天uv/上周同天的uv");
        }
    }

    /**
     *楼层分时客流：当日到访人次日环比=昨天的pv-前天的pv/前天的pv
     */
    @Test(description = "楼层分时客流：当日到访人次日环比=昨天的pv-前天的pv/前天的pv")
    public void mallCenterDataCase33(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(1L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次的日环比
            String dayQoq=response.getJSONObject("pv_overview").getString("day_qoq");
            //当日的的人次
            int number=response.getJSONObject("pv_overview").getInteger("number");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDateTime(-1)).floorId(1L).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取人次
            int numberLast=response1.getJSONObject("pv_overview").getInteger("number");
            String percent= String.valueOf((number-numberLast)/numberLast);

            Preconditions.checkArgument(dayQoq.equals(percent),"通过昨天和前天的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：当日到访人次日环比=昨天的pv-前天的pv/前天的pv");
        }
    }

    /**
     *楼层分时客流：当日到访人次周同比=昨天的pv-上周同天pv/上周同天的pv
     */
    @Test(description = "楼层分时客流：当日到访人次周同比=昨天的pv-上周同天pv/上周同天的pv")
    public void mallCenterDataCase34(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(1L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次的周同比
            String preWeekCompare=response.getJSONObject("pv_overview").getString("pre_week_compare");
            //当日的的人次
            int number=response.getJSONObject("pv_overview").getInteger("number");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDateTime(-8)).floorId(1L).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取人次
            int numberLast=response1.getJSONObject("pv_overview").getInteger("number");
            String percent= String.valueOf((number-numberLast)/numberLast);

            Preconditions.checkArgument(preWeekCompare.equals(percent),"通过昨天和上周同一天的数据计算出的周同比为："+percent+ "  页面中展示的周同比为："+preWeekCompare);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：当日到访人次周同比=昨天的pv-上周同天pv/上周同天的pv");
        }
    }

    /**
     *楼层分时客流：人均停留时长 日环比=昨天的人均停留时长-前天的人均停留时长/前天的人均停留时长
     */
    @Test(description = "楼层分时客流：人均停留时长 日环比=昨天的人均停留时长-前天的人均停留时长/前天的人均停留时长")
    public void mallCenterDataCase35(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(1L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //获取人均停留时长的日环比
            String dayQoq=response.getJSONObject("stay_time_overview").getString("day_qoq");
            //获取昨天的人均停留时长
            int number=response.getJSONObject("stay_time_overview").getInteger("number");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDateTime(-1)).floorId(1L).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取前天的人均停留时长
            int numberLast=response1.getJSONObject("stay_time_overview").getInteger("number");
            String percent= String.valueOf((number-numberLast)/numberLast);

            Preconditions.checkArgument(dayQoq.equals(percent),"通过昨天和昨天的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：人均停留时长 日环比=昨天的人均停留时长-前天的人均停留时长/前天的人均停留时长");
        }
    }

    /**
     *楼层分时客流：人均停留时长 周同比=昨天的人均停留时长-上周同天人均停留时长/上周同天人均停留时长
     */
    @Test(description = "楼层分时客流：人均停留时长 周同比=昨天的人均停留时长-上周同天人均停留时长/上周同天人均停留时长")
    public void mallCenterDataCase39(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(1L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //人均停留时长的周同比
            String preWeekCompare=response.getJSONObject("stay_time_overview").getString("pre_week_compare");
            //当日的的人均停留时长
            int number=response.getJSONObject("stay_time_overview").getInteger("number");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDateTime(-8)).floorId(1L).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //当日的的人均停留时
            int numberLast=response1.getJSONObject("stay_time_overview").getInteger("number");
            String percent= String.valueOf((number-numberLast)/numberLast);

            Preconditions.checkArgument(preWeekCompare.equals(percent),"通过昨天和前天数据计算出的人均停留时长周同比为："+percent+ "  页面中展示的人均停留时长周同比为："+preWeekCompare);


        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：人均停留时长 周同比=昨天的人均停留时长-上周同天人均停留时长/上周同天人均停留时长");
        }
    }

    /**
     *楼层分时客流：爬楼率=楼层到访人数/场馆总人数
     */
    @Test(description = "楼层分时客流：爬楼率=楼层到访人数/场馆总人数")
    public void mallCenterDataCase36(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(1L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //获取爬楼率
            String percentage=response.getJSONObject("floor_enter_percentage_overview").getString("percentage");
            //获取当前楼层的人数
            int number=response.getJSONObject("uv_overview").getInteger("number");

            //获取场馆总人数
            IScene scene1= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDateTime(-1)).endTime(businessUtil.getDateTime(-1)).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            int numberSum=response1.getJSONObject("uv_overview").getInteger("number");
            String  percent= String.valueOf((number/numberSum)*100).substring(0,5)+"%";

            Preconditions.checkArgument(percentage.equals(percent),"计算后的爬楼率为:"+percent+"  页面展示的爬楼率为："+percentage);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：爬楼率=楼层到访人数/场馆总人数");
        }
    }

    /**
     *楼层分时客流：爬楼率的 日环比=昨天的爬楼率-前天的爬楼率/前天的爬楼率
     */
    @Test(description = "楼层分时客流：爬楼率的 日环比=昨天的爬楼率-前天的爬楼率/前天的爬楼率")
    public void mallCenterDataCase37(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(1L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //获取爬楼率的日环比
            String dayQoq=response.getJSONObject("floor_enter_percentage_overview").getString("day_qoq");
            //获取昨天的爬楼率
            int percentage=response.getJSONObject("floor_enter_percentage_overview").getInteger("percentage");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDateTime(-1)).floorId(1L).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取前天的爬楼率
            int percentageLast=response1.getJSONObject("floor_enter_percentage_overview").getInteger("percentage");
            String percent= String.valueOf((percentage-percentageLast)/percentageLast);

            Preconditions.checkArgument(dayQoq.equals(percent),"通过昨天和昨天的数据计算出的爬楼率的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：爬楼率的 日环比=昨天的爬楼率-前天的爬楼率/前天的爬楼率");
        }
    }

    /**
     *楼层分时客流：爬楼率的 周同比=昨天的爬楼率-上周同天爬楼率/上周同天爬楼率
     */
    @Test(description = "楼层分时客流：爬楼率的 周同比=昨天的爬楼率-上周同天爬楼率/上周同天爬楼率")
    public void mallCenterDataCase38(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(1L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //获取爬楼率的日环比
            String preWeekCompare=response.getJSONObject("floor_enter_percentage_overview").getString("pre_week_compare");
            //获取昨天的爬楼率
            int percentage=response.getJSONObject("floor_enter_percentage_overview").getInteger("percentage");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDateTime(-8)).floorId(1L).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取上周同一天的爬楼率
            int percentageLast=response1.getJSONObject("floor_enter_percentage_overview").getInteger("percentage");
            String percent= String.valueOf((percentage-percentageLast)/percentageLast);

            Preconditions.checkArgument(preWeekCompare.equals(percent),"通过昨天和上周同一天的数据计算出的爬楼率的周环比为："+percent+ "  页面中展示的日环比为："+preWeekCompare);


        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：爬楼率的 周同比=昨天的爬楼率-上周同天爬楼率/上周同天爬楼率");
        }
    }


    /**
     *全场到访趋势图中的小时间uv和>=当日到访人数
     */
    @Test(description = "全场到访趋势图中的小时间uv和>=当日到访人数")
    public void mallCenterDataCase40(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(1L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当日的的人数
            int number=response.getJSONObject("uv_overview").getInteger("number");

            //获取历史的全场到访趋势图种的前一天当前小时的的UV数据
            IScene scene1= FullCourtTrendHistoryScene.builder().type("UV").scene(FloorTypeEnum.FLOOR_ENTER.getFloorType()).floorId(1L).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            JSONArray list=response1.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                // todo 全场到访趋势图中的人数的key
                int num=list.getJSONObject(i).getInteger("");
                Preconditions.checkArgument(number>=num,"当日的总人数为："+number+"   当日某一时刻的人数为："+num);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("全场到访趋势图中的小时间uv和>=当日到访人数");
        }
    }

    /**
     *全场到访趋势图中的小时间pv和==当日到访人次
     */
    @Test(description = "全场到访趋势图中的小时间pv和==当日到访人次")
    public void mallCenterDataCase41(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            IScene scene= OverviewFloorOverviewScene.builder().floorId(1L).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当日的的人次
            int number=response.getJSONObject("pv_overview").getInteger("number");

            //获取历史的全场到访趋势图种的前一天当前小时的的PV数据
            IScene scene1= FullCourtTrendHistoryScene.builder().type("PV").scene(FloorTypeEnum.FLOOR_ENTER.getFloorType()).floorId(1L).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            JSONArray list=response1.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                // todo 全场到访趋势图中的人次的key
                int num=list.getJSONObject(i).getInteger("");
                Preconditions.checkArgument(number>=num,"当日的总人次为："+number+"   当日某一时刻的人次为："+num);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("全场到访趋势图中的小时间pv和==当日到访人次");
        }
    }

    //-----------------------------------------------------------门店数据分析---------------------------------------------------

    /**
     *过店人数环比=前7天过店人数之和-上上个7天过店人数之和/上上个7天过店人数之和
     */
    @Test(description = "过店人数环比=前7天过店人数之和-上上个7天过店人数之和/上上个7天过店人数之和")
    public void mallCenterDataCase42(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene,true);
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().invoke(visitor,true).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    //过店人数
                    int passNumber=list.getJSONObject(i).getInteger("pass_number");
                    //过店人数环比
                    String passNumberQoq=list.getJSONObject(i).getString("pass_number_qoq");

                    //上一个七天的过店人数
                    int passNumberLast=list1.getJSONObject(i).getInteger("pass_number");
                    //计算过店人数环比
                    double value=passNumber-passNumberLast;
                    double qoq=value/passNumberLast;
                    Preconditions.checkArgument(passNumberQoq.equals(String.valueOf(qoq)));
                }
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("过店人数环比=前7天过店人数之和-上上个7天过店人数之和/上上个7天过店人数之和");
        }
    }

    /**
     *进店人数环比=前7天进店人数之和-上上个7天过进店人数之和/上上个7天进店人数之和
     */
    @Test(description = "进店人数环比=前7天进店人数之和-上上个7天过进店人数之和/上上个7天进店人数之和")
    public void mallCenterDataCase43(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene,true);
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().invoke(visitor,true).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    //进店人数
                    int enterNumber=list.getJSONObject(i).getInteger("enter_number");
                    //进店人数环比
                    String enterNumberQoq=list.getJSONObject(i).getString("enter_number_qoq");

                    //上一个七天的进店人数
                    int enterNumberLast=list1.getJSONObject(i).getInteger("enter_number");
                    //计算进店人数环比
                    double value=enterNumber-enterNumberLast;
                    double qoq=value/enterNumberLast;
                    Preconditions.checkArgument(enterNumberQoq.equals(String.valueOf(qoq)));
                }
            }

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("进店人数环比=前7天进店人数之和-上上个7天过进店人数之和/上上个7天进店人数之和");
        }
    }

    /**
     *进店率环比=前7天进店率-上上个7天进店率/上上个7天进店率
     */
    @Test(description = "进店率环比=前7天进店率-上上个7天进店率/上上个7天进店率")
    public void mallCenterDataCase44(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene,true);
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().invoke(visitor,true).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    //进店率
                    double enterPercentage=list.getJSONObject(i).getDouble("enter_percentage");
                    //进店率环比
                    String enterPercentageQoq=list.getJSONObject(i).getString("enter_percentage_qoq");

                    //上一个七天的进店率人数
                    double enterPercentageLast=list1.getJSONObject(i).getDouble("enter_percentage");
                    //计算进店率的环比
                    double value=enterPercentage-enterPercentageLast;
                    double qoq=value/enterPercentageLast;
                    Preconditions.checkArgument(enterPercentageQoq.equals(String.valueOf(qoq)));
                }
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("进店率环比=前7天进店率-上上个7天进店率/上上个7天进店率");
        }
    }

    /**
     *人均停留时长环比=前7天人均停留时长-上上个7天人均停留时长/上上个7天人均停留时长
     */
    @Test(description = "人均停留时长环比=前7天人均停留时长-上上个7天人均停留时长/上上个7天人均停留时长")
    public void mallCenterDataCase45(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene,true);
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().invoke(visitor,true).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    //人均停留时长
                    double stayTimeAvg=list.getJSONObject(i).getDouble("stay_time_avg");
                    //人均停留时长环比
                    String stayTimeAvgQoq=list.getJSONObject(i).getString("stay_time_avg_qoq");

                    //上一个七天的人均停留时长人数
                    double stayTimeAvgLast=list.getJSONObject(i).getDouble("stay_time_avg");
                    //计算人均停留时长的环比
                    double value=stayTimeAvg-stayTimeAvgLast;
                    double qoq=value/stayTimeAvgLast;
                    Preconditions.checkArgument(stayTimeAvgQoq.equals(String.valueOf(qoq)));
                }
            }

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("人均停留时长环比=前7天人均停留时长-上上个7天人均停留时长/上上个7天人均停留时长");
        }
    }

    /**
     *进店率=前7天进店人数/前7天过店总人数（含进店）
     */
    @Test(description = "进店率=前7天进店人数/前7天过店总人数（含进店）")
    public void mallCenterDataCase46(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene,true);
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().invoke(visitor,true).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    //进店人数
                    int enterNumber=list.getJSONObject(i).getInteger("enter_number");
                    //进店率
                    String enterPercentage=list.getJSONObject(i).getString("enter_percentage");

                    //上一个七天的过店人数
                    int enterNumberLast=list1.getJSONObject(i).getInteger("enter_number");
                    //计算过店人数环比
                    double qoq=enterNumber/enterNumberLast;
                    Preconditions.checkArgument(enterPercentage.equals(String.valueOf(qoq)));
                }
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("进店率=前7天进店人数/前7天过店总人数（含进店）");
        }
    }


    /**
     *光顾率环比=前7天光顾率之和-上上个7天光顾率之和/上上个7天光顾率之和
     */
    @Test(description = "光顾率环比=前7天光顾率之和-上上个7天光顾率之和/上上个7天光顾率之和")
    public void mallCenterDataCase47(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene,true);
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().invoke(visitor,true).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    //光顾率
                    double visitPercentage=list.getJSONObject(i).getDouble("visit_percentage");
                    //光顾率环比
                    String visitPercentageQoq=list.getJSONObject(i).getString("visit_percentage_qoq");

                    //上一个七天的光顾率
                    double visitPercentageLast=list1.getJSONObject(i).getDouble("visit_percentage");
                    //计算过店人数环比
                    double value=visitPercentage-visitPercentageLast;
                    double qoq=value/visitPercentageLast;
                    Preconditions.checkArgument(visitPercentageQoq.equals(String.valueOf(qoq)));
                }
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("光顾率环比=前7天光顾率之和-上上个7天光顾率之和/上上个7天光顾率之和");
        }
    }

    /**
     *
     */
    @Test(description = "")
    public void mallCenterDataCase48(){
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
    public void mallCenterDataCase49(){
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
    public void mallCenterDataCase50(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }



    //------------------------------------------------------------------------场馆客流周期数据---------------------------------------------------------------------














}
