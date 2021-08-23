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
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.shop.ShopFloorListScene;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MallSystemCase extends TestCaseCommon implements TestCaseStd {

    private final EnumTestProduct product = EnumTestProduct.MALL_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    MallBusinessUtil businessUtil = new MallBusinessUtil();
    StoreScenarioUtil su = StoreScenarioUtil.getInstance();
    CommonConfig commonConfig = new CommonConfig();
    Long floorId=7344L;//日常1楼的楼层id

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
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation()).setMallId("55456");
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
     * 场馆客流分时总览中的日环比=昨日某个时间段的到访人数-前日某个时间段的到访人数/前日同一个时间段的到访人数--ok
     */
    @Test(description = "场馆客流分时总览中的日环比=昨日某个时间段的到访人数-前日某个时间段的到访人数/前日同一个时间段的到访人数")
    public void mallCenterDataCase5(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流人数UV数据
            IScene scene= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的日环比
            String dayQoqUv=response.getJSONObject("uv_overview").getString("day_qoq");
            BigDecimal dayNum=new BigDecimal(dayQoqUv);
            double dayPercent=dayNum.setScale(2,RoundingMode.HALF_UP).doubleValue();

            //当前人数
            double numberUv=response.getJSONObject("uv_overview").getDouble("number");

            //获取历史客流前一天的人数UV数据
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-2)).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            double preUv=response1.getJSONObject("uv_overview").getDouble("number");
            double percent=preUv==0?0:(numberUv-preUv)/preUv;
            BigDecimal b=new  BigDecimal(percent);
            double qaq=b.setScale(2, RoundingMode.HALF_UP).doubleValue();

            Preconditions.checkArgument(qaq==dayPercent,"页面中展示的日环比为："+dayPercent+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+qaq);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时总览中的日环比=昨日某个时间段的到访人数-前日某个时间段的到访人数/前日同一个时间段的到访人数");
        }
    }

    /**
     * 场馆客流分时的人次日环比=昨日某个时间段的到访人次-前日某个时间段的到访人次/前日同一个时间段的到访人次--ok
     */
    @Test(description = "场馆客流分时的人次日环比=昨日某个时间段的到访人次-前日某个时间段的到访人次/前日同一个时间段的到访人次")
    public void mallCenterDataCase6(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");
            //当前人数的日环比
            String dayQoq=response.getJSONObject("pv_overview").getString("day_qoq");

            //获取历史客流前一天的人数pv数据
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-2)).build();
            JSONObject response2=visitor.invokeApi(scene1,true);
            int YesterdayPv=response2.getJSONObject("pv_overview").getInteger("number");
            BigDecimal b=new  BigDecimal((float)(numberPv-YesterdayPv)/YesterdayPv);
            String qoqPv=String.valueOf((b.setScale(2, RoundingMode.HALF_UP).doubleValue()));
            Preconditions.checkArgument(dayQoq.equals(qoqPv),"页面中展示的日环比为："+dayQoq+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+qoqPv);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的人次日环比=昨日某个时间段的到访人次-前日某个时间段的到访人次/前日同一个时间段的到访人次");
        }
    }

    /**
     * 场馆客流分时的人次周同比=本周昨天的人次－上周同一日到访人次/上周同一日到访人次--ok
     */
    @Test(description = "场馆客流分时的人次周同比=本周昨天的人次－上周同一日到访人次/上周同一日到访人次")
    public void mallCenterDataCase7(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次
            double numberPv=response.getJSONObject("pv_overview").getDouble("number");
            //当前人次的周同比
            String preWeekCompare=response.getJSONObject("pv_overview").getString("pre_week_compare");
            BigDecimal weekNum=new BigDecimal(preWeekCompare);
            double weekPercent=weekNum.setScale(4,RoundingMode.HALF_UP).doubleValue();

            //获取历史客流上一周同一天的人数pv数据
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-8)).build();
            JSONObject response2=visitor.invokeApi(scene1,true);
            double YesterdayPv=response2.getJSONObject("pv_overview").getDouble("number");
            System.err.println(YesterdayPv);
            double percent=YesterdayPv==0?0:(numberPv-YesterdayPv)/YesterdayPv;
            BigDecimal b=new  BigDecimal(percent);
            double qoqPv=b.setScale(2, RoundingMode.HALF_UP).doubleValue();


            Preconditions.checkArgument(weekPercent==qoqPv,"页面中展示的周同比为："+weekPercent+"   根据全场到访趋势图中上周和上上周的周同比计算结果为："+qoqPv);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的人次周同比=本周昨天的人次－上周同一日到访人次/上周同一日到访人次");
        }
    }

    /**
     * 场馆客流分时的人数周同比=本周昨天的到访人数-上周同一日的到访人数/上周同一日的到访人数--ok
     */
    @Test(description = "场馆客流分时的人数周同比=本周昨天的到访人数-上周同一日的到访人数/上周同一日的到访人数")
    public void mallCenterDataCase8(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次
            double numberPv=response.getJSONObject("uv_overview").getDouble("number");
            //当前人次的周同比
            String preWeekCompare=response.getJSONObject("uv_overview").getString("pre_week_compare");
            BigDecimal weekNum=new BigDecimal(preWeekCompare);
            double weekPercent=weekNum.setScale(4,RoundingMode.HALF_UP).doubleValue();

            //获取历史客流上一周同一天的人数pv数据
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-8)).build();
            JSONObject response2=visitor.invokeApi(scene1,true);
            double YesterdayPv=response2.getJSONObject("uv_overview").getDouble("number");
            System.err.println(YesterdayPv);
            double percent=YesterdayPv==0?0:(numberPv-YesterdayPv)/YesterdayPv;
            BigDecimal b=new  BigDecimal(percent);
            double qoqUv=b.setScale(2, RoundingMode.HALF_UP).doubleValue();

            Preconditions.checkArgument(weekPercent==qoqUv,"页面中展示的周同比为："+weekPercent+"   根据全场到访趋势图中上周和上上周的周同比计算结果为："+qoqUv);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的人数的周同比=本周昨天的到访人数-上周同一日的到访人数/上周同一日的到访人数");
        }
    }


    /**
     * 场馆客流分时-楼层客流分时的到访人数<=【全场到访趋势图】种的各个时间段的人数相加--ok
     */
    @Test(description = "场馆客流分时的到访人数<=【全场到访趋势图】种的各个时间段的人数相加")
    public void mallCenterDataCase9(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDate(-1)).endTime(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数
            int numberUv=response.getJSONObject("uv_overview").getInteger("number");

            //获取全场到访趋势图在中人数的之和
            IScene scene2=FullCourtTrendHistoryScene.builder().type("UV").scene("VENUE").date(businessUtil.getDate(-1)).build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            int number=0;
            JSONArray list=response2.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                int hourNum=list.getJSONObject(i).getInteger("current_day");
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
     * 场馆客流分时的到访人次=【全场到访趋势图】中的各个时间段的人次相加--ok
     */
    @Test(description = "场馆客流分时的到访人次=【全场到访趋势图】种的各个时间段的人次相加")
    public void mallCenterDataCase10(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            ///获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDate(-2)).endTime(businessUtil.getDate(-2)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");

            //获取全场到访趋势图在中人次的之和
            IScene scene2=FullCourtTrendHistoryScene.builder().type("PV").scene("VENUE").date(businessUtil.getDate(-2)).build();
            JSONObject response2=visitor.invokeApi(scene2,true);
            int number=0;
            JSONArray list=response2.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                int hourNum=list.getJSONObject(i).getInteger("current_day");
                number+=hourNum;
                logger.info(numberPv+"---------"+number);
            }

            Preconditions.checkArgument(numberPv<=number," 客流总览中的当前人次为："+numberPv+"    全场到访趋势图在中人次的之和为："+number);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的到访人次=【全场到访趋势图】种的各个时间段的人次相加");
        }
    }

    /**
     * 场馆客流分时的日环比=昨日某个时间段的人均停留时长－前日某个时间段的人均停留时长/前日相同时间段的人均停留时长--ok
     */
    @Test(description = "场馆客流分时的日环比=昨日某个时间段的人均停留时长－前日某个时间段的人均停留时长/前日相同时间段的人均停留时长")
    public void mallCenterDataCase11(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            ///获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDate(-1)).endTime(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //昨天人均停留时长
            double stayTimeOverview=response.getJSONObject("stay_time_overview").getDouble("number");
            //昨日的日环比
            double dayQoq=response.getJSONObject("stay_time_overview").getDouble("day_qoq");

            //获取前日的人均停留时长
            IScene scene1= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDate(-2)).endTime(businessUtil.getDate(-2)).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            double stayTimeOverviewLast=response1.getJSONObject("stay_time_overview").getDouble("number");
            //计算日环比
            BigDecimal b=new  BigDecimal((stayTimeOverview-stayTimeOverviewLast)/stayTimeOverviewLast);
            String qoqNum=String.valueOf((b.setScale(2, RoundingMode.HALF_UP).doubleValue()));
            logger.info(dayQoq+"---------"+qoqNum);

            Preconditions.checkArgument(qoqNum.equals(String.valueOf(dayQoq)),"  人均停留时长的客流总览中的日环比为："+dayQoq+"   通过计算昨天和前天人均停留时长的得来的日环比为："+qoqNum);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的日环比=昨日某个时间段的人均停留时长－前日某个时间段的人均停留时长/前日相同时间段的人均停留时长");
        }
    }

    /**
     * 场馆客流分时的周同比=上周今日的人均停留时长－上上周今日的人均停留时长/上上周同一日的人均停留时长--ok
     */
    @Test(description = "场馆客流分时的周同比=上周今日的人均停留时长－上上周今日的人均停留时长/上上周同一日的人均停留时长")
    public void mallCenterDataCase12(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次
            double numberPv=response.getJSONObject("stay_time_overview").getDouble("number");
            //当前人次的周同比
            String preWeekCompare=response.getJSONObject("stay_time_overview").getString("pre_week_compare");
            BigDecimal weekNum=new BigDecimal(preWeekCompare);
            double weekPercent=weekNum.setScale(4,RoundingMode.HALF_UP).doubleValue();

            //获取历史客流上一周同一天的人数pv数据
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-8)).build();
            JSONObject response2=visitor.invokeApi(scene1,true);
            double YesterdayPv=response2.getJSONObject("stay_time_overview").getDouble("number");
            System.err.println(YesterdayPv);
            double percent=YesterdayPv==0?0:(numberPv-YesterdayPv)/YesterdayPv;
            BigDecimal b=new  BigDecimal(percent);
            double preWeekCompareNum=b.setScale(2, RoundingMode.HALF_UP).doubleValue();

            Preconditions.checkArgument(weekPercent==preWeekCompareNum,"  人均停留时长的客流总览中的日环比为："+preWeekCompare+"   通过计算昨天和前天人均停留时长的得来的日环比为："+preWeekCompareNum);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的周同比=上周今日的人均停留时长－上上周今日的人均停留时长/上上周同一日的人均停留时长");
        }
    }

    /**
     * 场馆客流分时-出口到访趋势图和停车场到访趋势图-各出入口的人次的占比相加为100%--ok
     */
    @Test(description = "场馆客流分时/楼层客流分时的进出口到访趋势图-各入口的人次的占比相加为100%")
    public void mallCenterDataCase13(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] array={RegionTypeEnum.EXIT.getRegion(),RegionTypeEnum.PARKING.getRegion()};
            Arrays.stream(array).forEach(e->{
                IScene scene= HourPortraitScene.builder().date(businessUtil.getDate(-3)).region(e).build();
                JSONArray list=visitor.invokeApi(scene,true).getJSONArray("list");
                if(e.equals(RegionTypeEnum.EXIT.getRegion())){
                    //获取楼层出入口的各个人数占比
                    double portNum=0;
                    for(int i=0;i<list.size();i++){
                        String number=list.getJSONObject(i).getString("percentage");
                        double port=Double.parseDouble(number.substring(0,number.length()-1));
                        portNum+=port;
                        logger.info("----------"+portNum);
                    }
                    Preconditions.checkArgument(portNum==100.0,"进出口到访趋势图-各进出口的人次的占比相加为："+portNum);
                }else{
                    //获取楼层出入口的各个人数占比
                    int portNum=0;
                    for(int i=0;i<list.size();i++){
                        String number=list.getJSONObject(i).getString("percentage");
                        double port=Double.parseDouble(number.substring(0,number.length()-1));
                        portNum+=port;
                        logger.info("----------"+portNum);
                    }
                    Preconditions.checkArgument(portNum==100.0,"进出口到访趋势图-各停车场的人次的占比相加为："+portNum);
                }
            });

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时/楼层客流分时的进出口到访趋势图-各入口的人次的占比相加为100%");
        }
    }

    /**
     * 场馆客流-某一进出口的人次占比=某一进出口的人次/各个进出口的人次的总和--待写
     */
    @Test(description = "场馆客流-某一进出口的人次占比=某一进出口的人次/各个进出口的人次的总和")
    public void mallCenterDataCase14(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数
            IScene scene= RegionTrendScene.builder().date(businessUtil.getDate(-4)).type("PV").region(RegionTypeEnum.EXIT.getRegion()).build();
            JSONArray seriesList=visitor.invokeApi(scene,true).getJSONArray("series_List");
            JSONArray list=visitor.invokeApi(scene,true).getJSONArray("list");
            List<String> exitArray=new ArrayList<>();
            Map<String,Integer> exitPvMap=new HashMap<>();
            Map<String,String> exitName=new HashMap<>();
            for(int i=0;i<seriesList.size();i++){
                String key=seriesList.getJSONObject(i).getString("key");
                String name=seriesList.getJSONObject(i).getString("name");
                exitName.put(key,name);
                exitArray.add(key);
                System.out.println();
            }
            //获取各个出口的人次
            for(int j=0;j<list.size();j++){
                for(int exit=0;exit<exitArray.size();exit++){
                    Integer pv=list.getJSONObject(j).getInteger(exitArray.get(exit));
                    if(exitPvMap.get(exitArray.get(exit))==null){
                        exitPvMap.put(exitArray.get(exit), pv);
                    }else{
                        exitPvMap.put(exitArray.get(exit), pv+exitPvMap.get(exitArray.get(exit)));

                    }
                    System.err.println("---------------"+exitPvMap);
                }
            }
            //两个Map的根据key(进出口id)进行重新组合  todo
            Map<String,Integer> map=new HashMap<>();



            System.out.println("---------"+exitPvMap);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流-某一进出口的人次占比=某一进出口的人次/各个进出口的人次的总和");
        }
    }


    /**
     * 场馆客流分时的停车厂到访趋势图某一进出口的人次占比=某一进出口的人次/各个进出口的人次的总和--待写
     */
    @Test(description = "场馆客流分时的停车厂到访趋势图某一进出口的人次占比=某一进出口的人次/各个进出口的人次的总和")
    public void mallCenterDataCase17(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数占比
            IScene scene= RegionTrendScene.builder().date(businessUtil.getDate(-1)).type("PV").region(RegionTypeEnum.PARKING.getRegion()).build();
            JSONArray list=visitor.invokeApi(scene,true).getJSONArray("list");

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时的停车厂到访趋势图某一进出口的人次占比=某一进出口的人次/各个进出口的人次的总和");
        }
    }


    /**
     * 场馆客流分时/楼层客流分时的男性占比+女性占比=100%--ok
     */
    @Test(description = "场馆客流分时/楼层客流分时的男性占比+女性占比=100%")
    public void mallCenterDataCase19(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sceneArray={"VENUE","FLOOR"};
            Arrays.stream(sceneArray).forEach(scene->{
                if(scene.equals("VENUE")){
                    //获取各个年龄段的占比
                    IScene scene1= CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-1)).build();
                    JSONObject response=visitor.invokeApi(scene1,true);
                    String malePercent=response.getString("male_ratio_str");
                    double male=Double.parseDouble(malePercent.substring(0,malePercent.length()-1));
                    String femalePercent=response.getString("female_ratio_str");
                    double female=Double.parseDouble(femalePercent.substring(0,malePercent.length()-1));
                    double sum=male+female;
                    Preconditions.checkArgument(sum==100.0||sum==0.0,"男性占比和女性占比之和为："+sum);
                }else{
                    //获取各个年龄段的占比
                    IScene scene1= CustomersPortraitScene.builder().scene(scene).floorId(1L).date(businessUtil.getDate(-1)).build();
                    JSONObject response=visitor.invokeApi(scene1,true);
                    String malePercent=response.getString("male_ratio_str");
                    double male=Double.parseDouble(malePercent.substring(0,malePercent.length()-1));
                    String femalePercent=response.getString("female_ratio_str");
                    double female=Double.parseDouble(femalePercent.substring(0,femalePercent.length()-1));
                    double sum=male+female;
                    Preconditions.checkArgument(sum>=99.98||sum<=100.02||sum==0.0,"男性占比和女性占比之和为："+sum);
                }
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时/楼层客流分时的男性占比+女性占比=100%");
        }
    }

    /**
     * 场馆客流分时/楼层客流分时的各年龄段占比=各年龄段女性占比+各年龄段男性占比--ok
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
                        String ageGroupPercent=list.getJSONObject(i).getString("age_group_percent");
                        double ageGroup=Double.parseDouble(ageGroupPercent.substring(0,ageGroupPercent.length()-1));
                        String malePercent=list.getJSONObject(i).getString("male_percent");
                        double male=Double.parseDouble(malePercent.substring(0,malePercent.length()-1));
                        String femalePercent=list.getJSONObject(i).getString("female_percent");
                        double female=Double.parseDouble(femalePercent.substring(0,femalePercent.length()-1));
                        logger.info(ageGroup+"    "+male+"    "+female);
                        Preconditions.checkArgument(ageGroup<=(male+female+0.02)||ageGroup>=(male+female-0.02),"男性占比为："+malePercent+"  女性占比为："+femalePercent+"  年龄段占比为："+ageGroupPercent);
                    }
                }else {
                    //获取各个年龄段的占比
                    IScene scene1 = CustomersPortraitScene.builder().scene(scene).floorId(1L).date(businessUtil.getDate(-1)).build();
                    JSONObject response = visitor.invokeApi(scene1, true);
                    JSONArray list = response.getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String ageGroupPercent=list.getJSONObject(i).getString("age_group_percent");
                        double ageGroup=Double.parseDouble(ageGroupPercent.substring(0,ageGroupPercent.length()-1));
                        String malePercent=list.getJSONObject(i).getString("male_percent");
                        double male=Double.parseDouble(malePercent.substring(0,malePercent.length()-1));
                        String femalePercent=list.getJSONObject(i).getString("female_percent");
                        double female=Double.parseDouble(femalePercent.substring(0,femalePercent.length()-1));
                        logger.info(ageGroup+"    "+male+"    "+female);
                        Preconditions.checkArgument(ageGroup<=(male+female+0.02)||ageGroup>=(male+female-0.02),"男性占比为："+malePercent+"  女性占比为："+femalePercent+"  年龄段占比为："+ageGroupPercent);
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
     * 女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加（分时数据）--ok
     */
    @Test(description = "女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加（分时数据）")
    public void mallCenterDataCase22(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sceneArray={"VENUE","FLOOR"};
            Arrays.stream(sceneArray).forEach(scene->{
                if(scene.equals("VENUE")){
                    //获取各个年龄段的占比
                    IScene scene1= CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-3)).build();
                    JSONObject response=visitor.invokeApi(scene1,true);
                    String malePercentStr=response.getString("male_ratio_str");
                    double maleStr=Double.parseDouble(malePercentStr.substring(0,malePercentStr.length()-1));
                    String femalePercentStr=response.getString("female_ratio_str");
                    double femaleStr=Double.parseDouble(femalePercentStr.substring(0,femalePercentStr.length()-1));

                    JSONArray list=response.getJSONArray("list");
                    double maleNum=0L;
                    double femaleNum=0L;
                    for(int i=0;i<list.size();i++){
                        String malePercent=list.getJSONObject(i).getString("male_percent");
                        double male=Double.parseDouble(malePercent.substring(0,malePercent.length()-1));
                        String femalePercent=list.getJSONObject(i).getString("female_percent");
                        double female=Double.parseDouble(femalePercent.substring(0,femalePercent.length()-1));
                        maleNum+=male;
                        femaleNum+=female;
                    }
                    logger.info(maleNum+"      "+femaleNum+"     "+maleStr+"      "+femaleStr);
                    Preconditions.checkArgument(maleStr<=maleNum+0.5||maleNum-0.5<=maleStr,"女性占比为："+malePercentStr+"  各年龄段女性占比相加为："+maleNum);
                    Preconditions.checkArgument( femaleStr<=femaleNum+0.5||femaleStr-0.5<=femaleStr,"男性占比为："+femalePercentStr+"  各年龄段男性占比相加为："+femaleNum);
                }else {
                    //获取各个年龄段的占比
                    IScene scene1= CustomersPortraitScene.builder().scene(scene).floorId(floorId).date(businessUtil.getDate(-3)).build();
                    JSONObject response=visitor.invokeApi(scene1,true);
                    String malePercentStr=response.getString("male_ratio_str");
                    double maleStr=Double.parseDouble(malePercentStr.substring(0,malePercentStr.length()-1));
                    String femalePercentStr=response.getString("female_ratio_str");
                    double femaleStr=Double.parseDouble(femalePercentStr.substring(0,femalePercentStr.length()-1));

                    JSONArray list=response.getJSONArray("list");
                    double maleNum=0L;
                    double femaleNum=0L;
                    for(int i=0;i<list.size();i++){
                        String malePercent=list.getJSONObject(i).getString("male_percent");
                        double male=Double.parseDouble(malePercent.substring(0,malePercent.length()-1));
                        String femalePercent=list.getJSONObject(i).getString("female_percent");
                        double female=Double.parseDouble(femalePercent.substring(0,femalePercent.length()-1));
                        maleNum+=male;
                        femaleNum+=female;
                    }
                    logger.info(maleNum+"      "+femaleNum+"     "+maleStr+"      "+femaleStr);
                    Preconditions.checkArgument(maleStr<=maleNum+0.5||maleNum-0.5<=maleStr,"女性占比为："+malePercentStr+"  各年龄段女性占比相加为："+maleNum);
                    Preconditions.checkArgument( femaleStr<=femaleNum+0.5||femaleStr-0.5<=femaleStr,"男性占比为："+femalePercentStr+"  各年龄段男性占比相加为："+femaleNum);
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
     * 实时客流的到访人数<=到访人次--ok
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
     * 实时客流的今日到访人次==全场到访趋势今日数据人次之和--ok
     */
    @Test(description = "实时客流的今日到访人次==全场到访趋势今日数据人次之和")
    public void mallCenterDataCase25(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流数据
            IScene scene= RealTimeOverviewScene.builder().type("UV").build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");

            //全场到访趋势图中的PV的总人次
            IScene scene1=FullCourtTrendScene.builder().type("UV").build();
            JSONArray list=visitor.invokeApi(scene1,true).getJSONArray("list");
            int pvSum=0;
            for(int i=0;i<list.size();i++){
                int hourPv=list.getJSONObject(i).getInteger("today");
                System.out.println("-------"+hourPv);
                pvSum+=hourPv;
            }
            Preconditions.checkArgument(numberPv==pvSum,"今日到访人次为："+numberPv+"  全场到访趋势图中的PV的总人次为："+pvSum);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流的今日到访人次==全场到访趋势今日数据人次之和");
        }
    }

    /**
     * 实时客流的今日到访人数<=全场到访趋势今日数据之和--ok
     */
    @Test(description = "实时客流的今日到访人数<=全场到访趋势今日数据之和")
    public void mallCenterDataCase26(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流数据
            IScene scene= RealTimeOverviewScene.builder().build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数
            int numberUv=response.getJSONObject("uv_overview").getInteger("number");

            //全场到访趋势图中的UV的总人次
            IScene scene1=FullCourtTrendScene.builder().type("UV").build();
            JSONArray list=visitor.invokeApi(scene1,true).getJSONArray("list");
            int uvSum=0;
            for(int i=0;i<list.size();i++){
                int hourPv=list.getJSONObject(i).getInteger("today");
                uvSum+=hourPv;
            }
            logger.info(numberUv+"-------"+uvSum);
            Preconditions.checkArgument(numberUv==uvSum,"今日到访人次为："+numberUv+"  全场到访趋势图中的PV的总人次为："+uvSum);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流的今日到访人数<=全场到访趋势今日数据之和");
        }
    }

    /**
     *实时客流-楼层出入口和停车场环形图百分比之和=100%--ok
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
                double percentSum=0L;
                for(int i=0;i<list.size();i++){
                    String percent=list.getJSONObject(i).getString("percentage");
                    double percentage=Double.parseDouble(percent.substring(0,percent.length()-1));
                    percentSum+=percentage;
                    System.err.println(percentage);
                }
                Preconditions.checkArgument(percentSum==100.0,"实时客流的楼层各个出入口的百分比相加的总和为："+percentSum);
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
            //获取实时客流数据
            IScene scene= RealTimeOverviewScene.builder().build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");


            //获取楼层到访趋势的数据
            IScene scene1= RealTimeFloorVisitTrendScene.builder().type(FloorTypeEnum.VISIT.getFloorType()).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            JSONArray list=response1.getJSONArray("list");
            int sum=0;
            for(int i=0;i<list.size();i++){
                int number=list.getJSONObject(i).getInteger("number");
                sum+=number;
            }

            Preconditions.checkArgument(numberPv<=sum,"当前各个楼层的人数之和为："+sum+"  超过了累计人数之和的人数数量为："+numberPv);

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
            double sum=0L;
            for(int i=0;i<list.size();i++){
                String percent=list.getJSONObject(i).getString("age_group_percent");
                BigDecimal b=new BigDecimal(percent.substring(0,percent.length()-1));
                double percentNum=b.setScale(2,RoundingMode.HALF_UP).doubleValue();
                sum+=percentNum;
            }
            Preconditions.checkArgument(sum<=100.02||sum>=100.02,"所有年龄段的人数占比的相加之和为："+sum);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流-男性女性各个年龄段的占比相加==100%");
        }
    }


     // ----------------------------------------------------------楼层客流总览-----------------------------------------------------------


    /**
     *楼层分时客流：当日到访人数日环比=昨天的uv-前天的uv/前天的uv--ok
     */
    @Test(description = "楼层客流：日环比=昨天的uv-前天的uv/前天的uv")
    public void mallCenterDataCase31(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-2)).floorId(floorId).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的日环比
            Double dayQoq=response.getJSONObject("uv_overview").getDouble("day_qoq");
            BigDecimal qaq=new BigDecimal(dayQoq);
            String dayQ= String.valueOf(qaq.setScale(2,RoundingMode.HALF_UP).doubleValue());
            //当日的的人数
            double number=response.getJSONObject("uv_overview").getDouble("number");
            logger.info(number+"      "+dayQoq);
            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-3)).floorId(floorId).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取人数
            double numberLast=response1.getJSONObject("uv_overview").getDouble("number");
            double num=numberLast==0?0:(number-numberLast)/numberLast;
            BigDecimal b=new BigDecimal(num);
            String percent= String.valueOf(b.setScale(2,RoundingMode.HALF_UP).doubleValue());
            logger.info(numberLast+"      "+percent);

            Preconditions.checkArgument(dayQ.equals(percent),"通过昨天和前天的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层客流：日环比=昨天的uv-前天的uv/前天的uv");
        }
    }

    /**
     *楼层分时客流：当日到访人数周同比=昨天的uv-上周同天uv/上周同天的uv--ok
     */
    @Test(description = "楼层分时客流：当日到访人数周同比=昨天的uv-上周同天uv/上周同天的uv")
    public void mallCenterDataCase32(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的周同比
            Double dayQoq=response.getJSONObject("uv_overview").getDouble("pre_week_compare");
            BigDecimal qaq=new BigDecimal(dayQoq);
            String preWeekCompare= String.valueOf(qaq.setScale(4,RoundingMode.HALF_UP).doubleValue());
            //当日的的人数
            double number=response.getJSONObject("uv_overview").getDouble("number");
            logger.info("------"+number);

            //楼层客流概览-上周同一天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-8)).floorId(floorId).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取人数
            double numberLast=response1.getJSONObject("uv_overview").getDouble("number");
            logger.info("------"+numberLast);
            double num=numberLast==0?0:(number-numberLast)/numberLast;
            BigDecimal b=new BigDecimal(num);
            String percent= String.valueOf(b.setScale(4,RoundingMode.HALF_UP).doubleValue());
            logger.info(numberLast+"      "+percent);

            Preconditions.checkArgument(preWeekCompare.equals(percent),"通过昨天和上周同一天的数据计算出的周同比为："+percent+ "  页面中展示的周同比为："+preWeekCompare);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：当日到访人数周同比=昨天的uv-上周同天uv/上周同天的uv");
        }
    }

    /**
     *楼层分时客流：当日到访人次日环比=昨天的pv-前天的pv/前天的pv--ok
     */
    @Test(description = "楼层分时客流：当日到访人次日环比=昨天的pv-前天的pv/前天的pv")
    public void mallCenterDataCase33(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-2)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人次的日环比
            double dayQoq=response.getJSONObject("pv_overview").getDouble("day_qoq");
            BigDecimal qaq=new BigDecimal(dayQoq);
            String dayQ= String.valueOf(qaq.setScale(2,RoundingMode.HALF_UP).doubleValue());
            //当日的的人次
            double number=response.getJSONObject("pv_overview").getDouble("number");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-3)).floorId(floorId).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取人次
            double numberLast=response1.getJSONObject("pv_overview").getDouble("number");
            double num=numberLast==0?0:(number-numberLast)/numberLast;
            BigDecimal b=new BigDecimal(num);
            String percent =String.valueOf(b.setScale(2,RoundingMode.HALF_UP).doubleValue());

            logger.info(dayQ+"-------"+percent);
            Preconditions.checkArgument(percent.equals(dayQ),"通过昨天和前天的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：当日到访人次日环比=昨天的pv-前天的pv/前天的pv");
        }
    }

    /**
     *楼层分时客流：当日到访人次周同比=昨天的pv-上周同天pv/上周同天的pv--ok
     */
    @Test(description = "楼层分时客流：当日到访人次周同比=昨天的pv-上周同天pv/上周同天的pv")
    public void mallCenterDataCase34(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的周同比
            Double dayQoq=response.getJSONObject("pv_overview").getDouble("pre_week_compare");
            BigDecimal qaq=new BigDecimal(dayQoq);
            String preWeekCompare= String.valueOf(qaq.setScale(4,RoundingMode.HALF_UP).doubleValue());
            //当日的的人数
            double number=response.getJSONObject("pv_overview").getDouble("number");

            //楼层客流概览-上周同一日的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-8)).floorId(floorId).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取人数
            double numberLast=response1.getJSONObject("pv_overview").getDouble("number");
            double num=numberLast==0?0:(number-numberLast)/numberLast;
            BigDecimal b=new BigDecimal(num);
            String percent= String.valueOf(b.setScale(4,RoundingMode.HALF_UP).doubleValue());
            logger.info(numberLast+"      "+percent);

            Preconditions.checkArgument(preWeekCompare.equals(percent),"通过昨天和上周同一天的数据计算出的周同比为："+percent+ "  页面中展示的周同比为："+preWeekCompare);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：当日到访人次周同比=昨天的pv-上周同天pv/上周同天的pv");
        }
    }

    /**
     *楼层分时客流：人均停留时长 日环比=昨天的人均停留时长-前天的人均停留时长/前天的人均停留时长--ok
     */
    @Test(description = "楼层分时客流：人均停留时长 日环比=昨天的人均停留时长-前天的人均停留时长/前天的人均停留时长")
    public void mallCenterDataCase35(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-3)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //获取人均停留时长的日环比
            double dayQoq=response.getJSONObject("stay_time_overview").getDouble("day_qoq");
            BigDecimal b =new BigDecimal(dayQoq);
            String dayQ=String.valueOf(b.setScale(2,RoundingMode.HALF_UP).doubleValue());
            //获取昨天的人均停留时长
            double number=response.getJSONObject("stay_time_overview").getDouble("number");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-4)).floorId(floorId).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取前天的人均停留时长
            double numberLast=response1.getJSONObject("stay_time_overview").getDouble("number");
            BigDecimal numPercent=numberLast==0?BigDecimal.valueOf(0):new BigDecimal((number-numberLast)/numberLast);
            String percent= String.valueOf(numPercent.setScale(2,RoundingMode.HALF_UP).doubleValue());
            logger.info(dayQ+"       "+percent);

            Preconditions.checkArgument(percent.equals(dayQ),"通过昨天和昨天的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQ);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：人均停留时长 日环比=昨天的人均停留时长-前天的人均停留时长/前天的人均停留时长");
        }
    }

    /**
     *楼层分时客流：人均停留时长 周同比=昨天的人均停留时长-上周同天人均停留时长/上周同天人均停留时长--ok
     */
    @Test(description = "楼层分时客流：人均停留时长 周同比=昨天的人均停留时长-上周同天人均停留时长/上周同天人均停留时长")
    public void mallCenterDataCase39(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的周同比
            Double dayQoq=response.getJSONObject("stay_time_overview").getDouble("pre_week_compare");
            BigDecimal qaq=new BigDecimal(dayQoq);
            String preWeekCompare= String.valueOf(qaq.setScale(4,RoundingMode.HALF_UP).doubleValue());
            //当日的的人数
            double number=response.getJSONObject("stay_time_overview").getDouble("number");

            //楼层客流概览-上周同一日的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-8)).floorId(floorId).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取人数
            double numberLast=response1.getJSONObject("stay_time_overview").getDouble("number");
            double num=numberLast==0?0:(number-numberLast)/numberLast;
            BigDecimal b=new BigDecimal(num);
            String percent= String.valueOf(b.setScale(4,RoundingMode.HALF_UP).doubleValue());
            logger.info(numberLast+"      "+percent);

            Preconditions.checkArgument(preWeekCompare.equals(percent),"通过昨天和前天数据计算出的人均停留时长周同比为："+percent+ "  页面中展示的人均停留时长周同比为："+preWeekCompare);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：人均停留时长 周同比=昨天的人均停留时长-上周同天人均停留时长/上周同天人均停留时长");
        }
    }

    /**
     *楼层分时客流：爬楼率=楼层到访人数/各个楼层总人数--ok
     */
    @Test(description = "楼层分时客流：爬楼率=楼层到访人数/各个楼层总人数")
    public void mallCenterDataCase36(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-4)).floorId(floorId).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //获取爬楼率
            String percentage=response.getJSONObject("floor_enter_percentage_overview").getString("percentage");
            BigDecimal b=new BigDecimal(percentage.substring(0,percentage.length()-1));
            String floorPercent=String.valueOf(b.setScale(2,RoundingMode.HALF_UP).doubleValue());
            //获取当前楼层的人数
            double number=response.getJSONObject("uv_overview").getDouble("number");

            //获取楼层的list
            IScene scene3=ShopFloorListScene.builder().build();
            JSONArray list= visitor.invokeApi(scene3,true).getJSONArray("list");

            //获取各个楼层总人数
            double numberSum=0;
            for(int i=0;i<list.size();i++){
                Long floorId=list.getJSONObject(i).getLong("id");
                IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-4)).floorId(floorId).build();
                JSONObject response1=visitor.invokeApi(scene1,true);
                double num=response1.getJSONObject("uv_overview").getDouble("number");
                numberSum+=num;
            }
            BigDecimal numPercent=new BigDecimal(number*100/numberSum);
            String  percent= String.valueOf(numPercent.setScale(2,RoundingMode.HALF_UP).doubleValue());
            logger.info(floorPercent+"       "+percent);

            Preconditions.checkArgument(floorPercent.equals(percent),"计算后的爬楼率为:"+percent+"  页面展示的爬楼率为："+floorPercent);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：爬楼率=楼层到访人数/各个楼层总人数");
        }
    }

    /**
     *楼层分时客流：爬楼率的 日环比=昨天的爬楼率-前天的爬楼率/前天的爬楼率--ok
     */
    @Test(description = "楼层分时客流：爬楼率的 日环比=昨天的爬楼率-前天的爬楼率/前天的爬楼率")
    public void mallCenterDataCase37(){
        logger.logCaseStart(caseResult.getCaseName());
        try{

            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-7)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //获取爬楼率的日环比
            String dayQoq=response.getJSONObject("floor_enter_percentage_overview").getString("day_qoq");
            BigDecimal b=new BigDecimal(dayQoq);
            double  floorPercent=b.setScale(4,RoundingMode.HALF_UP).doubleValue();
            //获取昨天的爬楼率
            String percentage=response.getJSONObject("floor_enter_percentage_overview").getString("percentage");
            BigDecimal percentageCount=new BigDecimal(percentage.substring(0,percentage.length()-1));
            double percentBefore=percentageCount.setScale(4,RoundingMode.HALF_UP).doubleValue();

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-8)).floorId(floorId).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取前天的爬楼率
            String percentageLast=response1.getJSONObject("floor_enter_percentage_overview").getString("percentage");
            BigDecimal percentageLastCount=new BigDecimal(percentageLast.substring(0,percentageLast.length()-1));
            double percentLast=percentageLastCount.setScale(4,RoundingMode.HALF_UP).doubleValue();

            BigDecimal numPercent=new BigDecimal((percentBefore-percentLast)/percentLast);
            double percent= numPercent.setScale(4,RoundingMode.HALF_UP).doubleValue();
            logger.info(floorPercent+"       "+percent);

            Preconditions.checkArgument(floorPercent<=(percent+0.01)||floorPercent>=(percent-0.01),"通过昨天和昨天的数据计算出的爬楼率的日环比为："+percent+ "  页面中展示的日环比为："+floorPercent);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：爬楼率的 日环比=昨天的爬楼率-前天的爬楼率/前天的爬楼率");
        }
    }

    /**
     *楼层分时客流：爬楼率的 周同比=昨天的爬楼率-上周同天爬楼率/上周同天爬楼率--ok
     */
    @Test(description = "楼层分时客流：爬楼率的 周同比=昨天的爬楼率-上周同天爬楼率/上周同天爬楼率")
    public void mallCenterDataCase38(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-1)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当前人数的周同比
            String dayQoq=response.getJSONObject("floor_enter_percentage_overview").getString("pre_week_compare");
            BigDecimal qaq=new BigDecimal(dayQoq.substring(0,dayQoq.length()-1));
            String preWeekCompare= String.valueOf(qaq.setScale(2,RoundingMode.HALF_UP).doubleValue());
            //当日的的爬楼率
            String number=response.getJSONObject("floor_enter_percentage_overview").getString("percentage");
            double floorPercent=Double.parseDouble(number.substring(0,number.length()-1));

            //楼层客流概览-上周同一日的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-8)).floorId(floorId).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            //获取上周同一时间的周同比
            String numberLast=response1.getJSONObject("floor_enter_percentage_overview").getString("percentage");
            double floorLastPercent=Double.parseDouble(numberLast.substring(0,numberLast.length()-1));
            double num=floorLastPercent==0?0:(floorPercent-floorLastPercent)*100/floorLastPercent;
            BigDecimal b=new BigDecimal(num);
            String percent= String.valueOf(b.setScale(2,RoundingMode.HALF_UP).doubleValue());
            logger.info(preWeekCompare+"      "+percent);

            Preconditions.checkArgument(preWeekCompare.equals(percent),"通过昨天和上周同一天的数据计算出的爬楼率的周环比为："+percent+ "  页面中展示的日环比为："+preWeekCompare);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：爬楼率的 周同比=昨天的爬楼率-上周同天爬楼率/上周同天爬楼率");
        }
    }


    /**
     *全场到访趋势图中的小时间uv和>=当日到访人数--ok
     */
    @Test(description = "全场到访趋势图中的小时间uv和>=当日到访人数")
    public void mallCenterDataCase40(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-4)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当日的的人数
            int number=response.getJSONObject("uv_overview").getInteger("number");
            int sum=0;
            //获取历史的全场到访趋势图种的前一天当前小时的的UV数据
            IScene scene1= FullCourtTrendHistoryScene.builder().type("UV").date(businessUtil.getDate(-4)).scene(FloorTypeEnum.FLOOR.getFloorType()).floorId(floorId).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            JSONArray list=response1.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                int num=list.getJSONObject(i).getInteger("current_day");
                sum+=num;
                logger.info(sum+"-------"+num);
            }
            Preconditions.checkArgument(number>=sum,"当日的总人数为："+number+"   当日某一时刻的人数为："+sum);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("全场到访趋势图中的小时间uv和>=当日到访人数");
        }
    }

    /**
     *全场到访趋势图中的小时间pv和==当日到访人次--ok
     */
    @Test(description = "全场到访趋势图中的小时间pv和==当日到访人次")
    public void mallCenterDataCase41(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-4)).build();
            JSONObject response=visitor.invokeApi(scene,true);
            //当日的的人次
            int number=response.getJSONObject("pv_overview").getInteger("number");
            int sum=0;

            //获取历史的全场到访趋势图种的前一天当前小时的的PV数据
            IScene scene1= FullCourtTrendHistoryScene.builder().type("PV").scene(FloorTypeEnum.FLOOR.getFloorType()).date(businessUtil.getDate(-4)).floorId(floorId).build();
            JSONObject response1=visitor.invokeApi(scene1,true);
            JSONArray list=response1.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                int num=list.getJSONObject(i).getInteger("current_day");
                sum+=num;
                logger.info(sum+"-------"+num);
            }
            Preconditions.checkArgument(number==sum,"当日的总人次为："+number+"   当日某一时刻的人次为："+sum);
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
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().execute(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().execute(visitor,true).getJSONArray("list");
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
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().execute(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().execute(visitor,true).getJSONArray("list");
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
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().execute(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().execute(visitor,true).getJSONArray("list");
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
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().execute(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().execute(visitor,true).getJSONArray("list");
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
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().execute(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().execute(visitor,true).getJSONArray("list");
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
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().execute(visitor,true).getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().execute(visitor,true).getJSONArray("list");
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
