package com.haisheng.framework.testng.bigScreen.itemMall.caseonline.gly;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemMall.casedaily.gly.Util.MallBusinessUtil;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.FloorTypeEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.RegionTypeEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator.SortTypeEnum;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview.OverviewFloorOverviewScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview.OverviewRegionOverviewScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.overview.OverviewVenueOverviewScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.region.*;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.pc.shop.ShopPageScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.CustomersPortraitScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.RegionTrendScene;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.history.*;
import com.haisheng.framework.testng.bigScreen.itemMall.common.scene.visittrend.realtime.*;
import com.haisheng.framework.testng.bigScreen.itemMall.common.util.SceneUntil;
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

public class MallSystemOnlineCase extends TestCaseCommon implements TestCaseStd {
    private final EnumTestProduct product = EnumTestProduct.MALL_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(product);
    MallBusinessUtil businessUtil = new MallBusinessUtil(visitor);
    CommonConfig commonConfig = new CommonConfig();
    public SceneUntil user = new SceneUntil(visitor);
    private static final AccountEnum ACCOUNT = AccountEnum.MALL_ONLINE_ZD;
    Long floorId=1256L;//日常1楼的楼层id
    String mallId="3115";
    String roleId="12278";//正大的roleId

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_SHOPMALL_Online_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.GLY.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.MALL_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId(product.getShopId()).setReferer(product.getReferer()).setRoleId(roleId).setProduct(product.getAbbreviation()).setMallId(mallId);
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
        user.loginPc(ACCOUNT);

    }



     //--------------------------------------------------场馆客流分时数据---------------------------------------------------------


    /**
     * 场馆客流总览中的到访人数<=到访人次---ok
     */
    @Test(description = "场馆客流总览中的到访人数<=到访人次(按天查询)")
    public void mallCenterDataCase4(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-1)).build();
            JSONObject response=scene.visitor(visitor).execute();
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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人数的日环比
            String dayQoqUv=response.getJSONObject("uv_overview").getString("day_qoq");
            double dayPercent=businessUtil.getNumHalfUp(dayQoqUv,4);
            //当前人数
            double numberUv=response.getJSONObject("uv_overview").getDouble("number");

            //获取历史客流前一天的人数UV数据
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-2)).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            double preUv=response1.getJSONObject("uv_overview").getDouble("number");
            double percent=preUv==0?0:(numberUv-preUv)/preUv;
            double qaq=businessUtil.getNumHalfUp(percent,4);
            logger.info("页面中展示的日环比为："+dayPercent+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+qaq);

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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人次
            double numberPv=response.getJSONObject("pv_overview").getInteger("number");
            //当前人数的日环比
            double dayQoq=businessUtil.getNumHalfUp(response.getJSONObject("pv_overview").getDouble("day_qoq"),4);

            //获取历史客流前一天的人数pv数据
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-2)).build();
            JSONObject response2=scene1.visitor(visitor).execute();
            double YesterdayPv=response2.getJSONObject("pv_overview").getInteger("number");
            double num=YesterdayPv==0?0:(numberPv-YesterdayPv)/YesterdayPv;
            double qoqPv=businessUtil.getNumHalfUp(num,4);

            logger.info("页面中展示的日环比为："+dayQoq+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+qoqPv);
            Preconditions.checkArgument(dayQoq==qoqPv,"页面中展示的日环比为："+dayQoq+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+qoqPv);
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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人次
            double numberPv=response.getJSONObject("pv_overview").getDouble("number");
            //当前人次的周同比
            double weekPercent=businessUtil.getNumHalfUp(response.getJSONObject("pv_overview").getDouble("pre_week_compare"),4);

            //获取历史客流上一周同一天的人数pv数据
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-8)).build();
            JSONObject response2=scene1.visitor(visitor).execute();
            double YesterdayPv=response2.getJSONObject("pv_overview").getDouble("number");
            double percent=YesterdayPv==0?0:(numberPv-YesterdayPv)/YesterdayPv;
            double qoqPv=businessUtil.getNumHalfUp(percent,4);

            logger.info("页面中展示的周同比为："+weekPercent+"   根据全场到访趋势图中上周和上上周的周同比计算结果为："+qoqPv);
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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人数
            double numberPv=response.getJSONObject("uv_overview").getDouble("number");
            //当前人数的周同比
            double weekPercent=businessUtil.getNumHalfUp(response.getJSONObject("uv_overview").getDouble("pre_week_compare"),4);

            //获取历史客流上一周同一天的人数pv数据
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-8)).build();
            JSONObject response2=scene1.visitor(visitor).execute();
            double YesterdayPv=response2.getJSONObject("uv_overview").getDouble("number");
            double percent=YesterdayPv==0?0:(numberPv-YesterdayPv)/YesterdayPv;
            double qoqPv=businessUtil.getNumHalfUp(percent,4);

            logger.info("页面中展示的周同比为："+weekPercent+"   根据全场到访趋势图中上周和上上周的周同比计算结果为："+qoqPv);
            Preconditions.checkArgument(weekPercent==qoqPv,"页面中展示的周同比为："+weekPercent+"   根据全场到访趋势图中上周和上上周的周同比计算结果为："+qoqPv);
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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人数
            int numberUv=response.getJSONObject("uv_overview").getInteger("number");

            //获取全场到访趋势图在中人数的之和
            IScene scene2=FullCourtTrendHistoryScene.builder().type("UV").scene("VENUE").date(businessUtil.getDate(-1)).build();
            JSONObject response2=scene2.visitor(visitor).execute();
            int number=0;
            JSONArray list=response2.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                int hourNum=list.getJSONObject(i).getInteger("current_day");
                number+=hourNum;
            }
            logger.info(" 客流总览中的当前人数为："+numberUv+"    全场到访趋势图在中人数的之和为："+number);
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
            IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getDate(-1)).endTime(businessUtil.getDate(-1)).build();
            JSONObject response=scene.visitor(visitor).execute();
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");

            //获取全场到访趋势图在中人次的之和
            IScene scene2=FullCourtTrendHistoryScene.builder().type("PV").scene("VENUE").date(businessUtil.getDate(-1)).build();
            JSONObject response2=scene2.visitor(visitor).execute();
            int number=0;
            JSONArray list=response2.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                int hourNum=list.getJSONObject(i).getInteger("current_day");
                number+=hourNum;
            }
            logger.info(" 客流总览中的当前人次为："+numberPv+"    全场到访趋势图在中人次的之和为："+number);
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
            //获取历史客流数据
            IScene scene= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-1)).build();
            JSONObject response=scene.visitor(visitor).execute();
            //当前人均停留时长
            double numberPv=response.getJSONObject("stay_time_overview").getInteger("number");
            //当前人均停留时长的日环比
            double dayQoq=businessUtil.getNumHalfUp(response.getJSONObject("stay_time_overview").getDouble("day_qoq"),4);

            //获取历史客流前一天的人均停留时长数据
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-2)).build();
            JSONObject response2=scene1.visitor(visitor).execute();
            double YesterdayPv=response2.getJSONObject("stay_time_overview").getInteger("number");
            double num=YesterdayPv==0?0:(numberPv-YesterdayPv)/YesterdayPv;
            double qoqTime=businessUtil.getNumHalfUp(num,4);
            double count =businessUtil.getDecimalResult(dayQoq,qoqTime,"subtract",4);


            logger.info("页面中展示的日环比为："+dayQoq+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+qoqTime+"-------"+count);
            Preconditions.checkArgument(count<=0.01,"页面中展示的日环比为："+dayQoq+"   根据全场到访趋势图中今日和昨日的数据的日环比计算结果为："+qoqTime);
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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人均停留时长
            double numberPv=response.getJSONObject("stay_time_overview").getDouble("number");
            //当前人均停留时长的周同比
            double weekPercent=businessUtil.getNumHalfUp(response.getJSONObject("stay_time_overview").getDouble("pre_week_compare"),4);

            //获取历史客流上一周同一天的人均停留时长数据
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-8)).build();
            JSONObject response2=scene1.visitor(visitor).execute();
            double YesterdayPv=response2.getJSONObject("stay_time_overview").getDouble("number");
            double percent=YesterdayPv==0?0:(numberPv-YesterdayPv)/YesterdayPv;
            double qoqPv=businessUtil.getNumHalfUp(percent,4);
            double count =businessUtil.getDecimalResult(weekPercent,qoqPv,"subtract",4);

            logger.info("页面中展示的周同比为："+weekPercent+"   根据全场到访趋势图中上周和上上周的周同比计算结果为："+qoqPv+"     "+count);
            Preconditions.checkArgument(count<=0.01,"页面中展示的周同比为："+weekPercent+"   根据全场到访趋势图中上周和上上周的周同比计算结果为："+qoqPv);
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
                JSONArray list=scene.visitor(visitor).execute().getJSONArray("list");
                double portNum=0;
                    //获取楼层出入口的各个人数占比
                    for(int i=0;i<list.size();i++){
                        String number=list.getJSONObject(i).getString("percentage");
                        double port=businessUtil.getNumHalfUp(Double.parseDouble(number.substring(0,number.length()-1)),4);
                        portNum=businessUtil.getDecimalResult(portNum,port,"add",4);
                    }
                    Preconditions.checkArgument(portNum<=100.01&&portNum>=99.99,e+"进出口到访趋势图-各进出口的人次的占比相加为："+portNum);
            });

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时/楼层客流分时的进出口到访趋势图-各入口的人次的占比相加为100%");
        }
    }

    /**
     * 场馆客流-某一进出口的人次占比=某一进出口的人次/各个进出口的人次的总和--ok
     */
    @Test(description = "场馆客流-某一进出口的人次占比=某一进出口的人次/各个进出口的人次的总和")
    public void mallCenterDataCase14(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数
            IScene scene= RegionTrendScene.builder().date(businessUtil.getDate(-1)).type("PV").region(RegionTypeEnum.EXIT.getRegion()).build();
            JSONObject response=scene.visitor(visitor).execute();
            JSONArray seriesList=response.getJSONArray("series_List");
            JSONArray list=response.getJSONArray("list");
            List<String> exitArray=new ArrayList<>();
            //存放各个出入口的名称和对应的人次的map
            Map<String,Integer> exitPvMap=new HashMap<>();
            //存放各个出入口的名称和id的map
            Map<String,String> exitName=new HashMap<>();
            //获取各个出入口的id和名称
            for(int i=0;i<seriesList.size();i++){
                String key=seriesList.getJSONObject(i).getString("key");
                String name=seriesList.getJSONObject(i).getString("name");
                exitName.put(key,name);
                exitArray.add(key);
            }
            //获取各个出口的人次和名称
            int count=0;
            for(int j=0;j<list.size();j++){
                for (String s : exitArray) {
                    Integer pv = list.getJSONObject(j).containsKey(s)?list.getJSONObject(j).getInteger(s):new Integer(0);
                    exitPvMap.merge(s, pv, Integer::sum);
                    count+=pv;
                }
            }
            //两个Map的根据key(进出口id)进行重新组合成一个新的map
            Map<String,Double> map=new HashMap<>();
            Set<String> keySet=exitPvMap.keySet();
            Set<String> key=exitName.keySet();
            for(String s:keySet){
                for (String k:key){
                    if(s.equals(k)){
                        map.put(exitName.get(k),businessUtil.getNumHalfUp((double)exitPvMap.get(s)*100/count,2));
                    }
                }
            }
            //判断饼状图中的中的百分比与计算的map中的结果是否一致
            IScene scene1=HourPortraitScene.builder().date(businessUtil.getDate(-1)).region(RegionTypeEnum.EXIT.getRegion()).build();
            JSONArray list1=scene1.visitor(visitor).execute().getJSONArray("list");
            for(int a=0;a<list1.size();a++){
                String keyName=list1.getJSONObject(a).getString("key");
                String percentPage=list1.getJSONObject(a).getString("percentage");
                double percentage=Double.parseDouble(percentPage.substring(0,percentPage.length()-1));
                double percentCount=map.getOrDefault(keyName,0.0);
                logger.info("饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
                Preconditions.checkArgument(percentage==percentCount,"饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流-某一进出口的人次占比=某一进出口的人次/各个进出口的人次的总和");
        }
    }


    /**
     * 场馆客流分时的停车厂到访趋势图某一进出口的人次占比=某一进出口的人次/各个进出口的人次的总和--ok
     */
    @Test(description = "场馆客流分时的停车厂到访趋势图某一进出口的人次占比=某一进出口的人次/各个进出口的人次的总和")
    public void mallCenterDataCase17(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数
            IScene scene= RegionTrendScene.builder().date(businessUtil.getDate(-1)).type("PV").region(RegionTypeEnum.PARKING.getRegion()).build();
            JSONObject response=scene.visitor(visitor).execute();
            JSONArray seriesList=response.getJSONArray("series_List");
            JSONArray list=response.getJSONArray("list");
            List<String> exitArray=new ArrayList<>();
            //存放各个出入口的名称和对应的人次的map
            Map<String,Integer> exitPvMap=new HashMap<>();
            //存放各个出入口的名称和id的map
            Map<String,String> exitName=new HashMap<>();
            //获取各个出入口的id和名称
            for(int i=0;i<seriesList.size();i++){
                String key=seriesList.getJSONObject(i).getString("key");
                String name=seriesList.getJSONObject(i).getString("name");
                exitName.put(key,name);
                exitArray.add(key);
            }
            //获取各个出口的人次和名称
            int count=0;
            for(int j=0;j<list.size();j++){
                for (String s : exitArray) {
                    Integer pv = list.getJSONObject(j).containsKey(s)?list.getJSONObject(j).getInteger(s):new Integer(0);
                    exitPvMap.merge(s, pv, Integer::sum);
                    count+=pv;
                }
            }
            //两个Map的根据key(进出口id)进行重新组合成一个新的map
            Map<String,Double> map=new HashMap<>();
            Set<String> keySet=exitPvMap.keySet();
            Set<String> key=exitName.keySet();
            for(String s:keySet){
                for (String k:key){
                    if(s.equals(k)){
                        map.put(exitName.get(k),businessUtil.getNumHalfUp((double)exitPvMap.get(s)*100/count,2));
                    }
                }
            }
            //判断饼状图中的中的百分比与计算的map中的结果是否一致
            IScene scene1=HourPortraitScene.builder().date(businessUtil.getDate(-1)).region(RegionTypeEnum.PARKING.getRegion()).build();
            JSONArray list1=scene1.visitor(visitor).execute().getJSONArray("list");
            for(int a=0;a<list1.size();a++){
                String keyName=list1.getJSONObject(a).getString("key");
                String percentPage=list1.getJSONObject(a).getString("percentage");
                double percentage=Double.parseDouble(percentPage.substring(0,percentPage.length()-1));
                double percentCount=map.getOrDefault(keyName,0.0);
                logger.info("饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
                Preconditions.checkArgument(percentage==percentCount,"饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
            }
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
                //获取各个年龄段的占比
                IScene scene1=scene.equals("VENUE")? CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-1)).build():CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-1)).floorId(floorId).build();
                JSONObject response=scene1.visitor(visitor).execute();
                String malePercent=response.getString("male_ratio_str");
                double male=Double.parseDouble(malePercent.substring(0,malePercent.length()-1));
                String femalePercent=response.getString("female_ratio_str");
                double female=Double.parseDouble(femalePercent.substring(0,malePercent.length()-1));
                double sum=businessUtil.getDecimalResult(male,female,"add",4);
                logger.info("---------"+sum);
                Preconditions.checkArgument((sum>=99.98&&sum<=100.02)||sum==0.00,"男性占比和女性占比之和为："+sum);
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
                //获取各个年龄段的占比
                IScene scene1=scene.equals("VENUE")? CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-1)).build():CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-1)).floorId(floorId).build();
                JSONObject response=scene1.visitor(visitor).execute();
                JSONArray list=response.getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String ageGroupPercent=list.getJSONObject(i).getString("age_group_percent");
                    double ageGroup=Double.parseDouble(ageGroupPercent.substring(0,ageGroupPercent.length()-1));
                    String malePercent=list.getJSONObject(i).getString("male_percent");
                    double male=Double.parseDouble(malePercent.substring(0,malePercent.length()-1));
                    String femalePercent=list.getJSONObject(i).getString("female_percent");
                    double female=Double.parseDouble(femalePercent.substring(0,femalePercent.length()-1));
                    System.out.println(scene+"男性占比为："+malePercent+"  女性占比为："+femalePercent+"  年龄段占比为："+ageGroupPercent);
                    Preconditions.checkArgument(ageGroup<=(male+female+0.02)&&ageGroup>=(male+female-0.02),scene+"男性占比为："+malePercent+"  女性占比为："+femalePercent+"  年龄段占比为："+ageGroupPercent);
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
                //获取各个年龄段的占比
                IScene scene1=scene.equals("VENUE")? CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-1)).build():CustomersPortraitScene.builder().scene(scene).date(businessUtil.getDate(-1)).floorId(floorId).build();
                JSONObject response=scene1.visitor(visitor).execute();
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
                    maleNum=businessUtil.getDecimalResult(maleNum,male,"add",2);
                    femaleNum=businessUtil.getDecimalResult(femaleNum,female,"add",2);
                }
                logger.info(maleNum+"      "+femaleNum+"     "+maleStr+"      "+femaleStr+"    "+scene);
                Preconditions.checkArgument(maleStr<=maleNum+0.01||maleNum-0.01<=maleStr,scene+"女性占比为："+malePercentStr+"  各年龄段女性占比相加为："+maleNum);
                Preconditions.checkArgument( femaleStr<=femaleNum+0.01&&femaleStr-0.01<=femaleStr,scene+" 男性占比为："+femalePercentStr+"  各年龄段男性占比相加为："+femaleNum);

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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");
            //当前人数
            int numberUv=response.getJSONObject("uv_overview").getInteger("number");
            logger.info(numberPv+"-----"+numberUv);
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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");

            //全场到访趋势图中的PV的总人次
            IScene scene1=FullCourtTrendScene.builder().type("PV").build();
            JSONArray list=scene1.visitor(visitor).execute().getJSONArray("list");
            int pvSum=0;
            for(int i=0;i<list.size();i++){
                int hourPv=list.getJSONObject(i).getInteger("today");
                pvSum+=hourPv;
            }
            logger.info(numberPv+"----pvSum-----"+pvSum);
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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人数
            int numberUv=response.getJSONObject("uv_overview").getInteger("number");

            //全场到访趋势图中的UV的总人次
            IScene scene1=FullCourtTrendScene.builder().type("UV").build();
            JSONArray list=scene1.visitor(visitor).execute().getJSONArray("list");
            int uvSum=0;
            for(int i=0;i<list.size();i++){
                int hourPv=list.getJSONObject(i).getInteger("today");
                uvSum+=hourPv;
            }
            logger.info(numberUv+"-------"+uvSum);
            Preconditions.checkArgument(numberUv<=uvSum,"今日到访人次为："+numberUv+"  全场到访趋势图中的PV的总人次为："+uvSum);

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
                JSONObject response=scene.visitor(visitor).execute();
                JSONArray list=response.getJSONArray("list");
                double percentSum=0L;
                for(int i=0;i<list.size();i++){
                    String percent=list.getJSONObject(i).getString("percentage");
                    double percentage=Double.parseDouble(percent.substring(0,percent.length()-1));
                    percentSum=businessUtil.getDecimalResult(percentSum,percentage,"add",2);
                }
                logger.info("percentSum:"+percentSum);
                Preconditions.checkArgument(percentSum<=100.02||percentSum>=99.98,"实时客流的楼层各个出入口的百分比相加的总和为："+percentSum);
            });

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流-楼层出入口和停车场环形图百分比之和=100%");
        }
    }

    /**
     *实时客流-楼层到访趋势图累计到访人数和>=今日到访人数--ok
     */
    @Test(description = "实时客流-楼层到访趋势图累计到访人数和>=今日到访人数")
    public void mallCenterDataCase28(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流数据
            IScene scene= RealTimeOverviewScene.builder().build();
            JSONObject response=scene.visitor(visitor).execute();
            //当前人次
            int numberPv=response.getJSONObject("pv_overview").getInteger("number");

            //获取楼层到访趋势的数据
            IScene scene1= RealTimeFloorVisitTrendScene.builder().type(FloorTypeEnum.VISIT.getFloorType()).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            JSONArray list=response1.getJSONArray("visit_trend_list");
            int sum=0;
            for(int i=0;i<list.size();i++){
                int number=list.getJSONObject(i).getInteger("number");
                sum+=number;
                System.out.println("-----"+number);
            }
            logger.info("当前各个楼层的人数之和为："+sum+"  超过了累计人数之和的人数数量为："+numberPv);
            Preconditions.checkArgument(numberPv<=sum,"当前各个楼层的人数之和为："+sum+"  超过了累计人数之和的人数数量为："+numberPv);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流-楼层到访趋势图累计到访人数和>=今日到访人数");
        }
    }

    /**
     * 实时客流-进出口占比=进出口的人次/各个进出口的人次的总和--ok
     */
    @Test(description = "实时客流-进出口占比=进出口的人次/各个进出口的人次的总和")
    public void mallCenterDataCase23(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数
            IScene scene= RegionRealTimeTrendScene.builder().type("PV").region(RegionTypeEnum.EXIT.getRegion()).build();
            JSONObject response=scene.visitor(visitor).execute();
            JSONArray seriesList=response.getJSONArray("series_List");
            JSONArray list=response.getJSONArray("list");
            List<String> exitArray=new ArrayList<>();
            //存放各个出入口的名称和对应的人次的map
            Map<String,Integer> exitPvMap=new HashMap<>();
            //存放各个出入口的名称和id的map
            Map<String,String> exitName=new HashMap<>();
            //获取各个出入口的id和名称
            for(int i=0;i<seriesList.size();i++){
                String key=seriesList.getJSONObject(i).getString("key");
                String name=seriesList.getJSONObject(i).getString("name");
                exitName.put(key,name);
                exitArray.add(key);
            }
            //获取各个出口的人次和名称
            int count=0;
            for(int j=0;j<list.size();j++){
                for (String s : exitArray) {
                    Integer pv = list.getJSONObject(j).containsKey(s)?list.getJSONObject(j).getInteger(s):new Integer(0);
                    exitPvMap.merge(s, pv, Integer::sum);
                    count+=pv;
                }
            }
            //两个Map的根据key(进出口id)进行重新组合成一个新的map
            Map<String, Double> map=new HashMap<>();
            Set<String> keySet=exitPvMap.keySet();
            Set<String> key=exitName.keySet();
            for(String s:keySet){
                for (String k:key){
                    if(s.equals(k)){
                        map.put(exitName.get(k),businessUtil.getNumHalfUp((double)exitPvMap.get(s)*100/count,2));
                    }
                }
            }
            //判断饼状图中的中的百分比与计算的map中的结果是否一致
            IScene scene1=RealTimePortraitScene.builder().region(RegionTypeEnum.EXIT.getRegion()).build();
            JSONArray list1=scene1.visitor(visitor).execute().getJSONArray("list");
            for(int a=0;a<list1.size();a++){
                String keyName=list1.getJSONObject(a).getString("key");
                String percentPage=list1.getJSONObject(a).getString("percentage");
                double percentage=Double.parseDouble(percentPage.substring(0,percentPage.length()-1));
                double percentCount=map.getOrDefault(keyName,0.0);
                logger.info("饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
                Preconditions.checkArgument(percentage==percentCount,"饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流-进出口占比=进出口的人次/各个进出口的人次的总和");
        }
    }

    /**
     * 实时客流-停车场进出口占比=进出口的人次/各个进出口的人次的总和--ok
     */
    @Test(description = "实时客流-停车场进出口占比=进出口的人次/各个进出口的人次的总和")
    public void mallCenterDataCase24(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取楼层出入口的各个人数
            IScene scene= RegionRealTimeTrendScene.builder().type("PV").region(RegionTypeEnum.PARKING.getRegion()).build();
            JSONObject response=scene.visitor(visitor).execute();
            JSONArray seriesList=response.getJSONArray("series_List");
            JSONArray list=response.getJSONArray("list");
            List<String> exitArray=new ArrayList<>();
            //存放各个出入口的名称和对应的人次的map
            Map<String,Integer> exitPvMap=new HashMap<>();
            //存放各个出入口的名称和id的map
            Map<String,String> exitName=new HashMap<>();
            //获取各个出入口的id和名称
            for(int i=0;i<seriesList.size();i++){
                String key=seriesList.getJSONObject(i).getString("key");
                String name=seriesList.getJSONObject(i).getString("name");
                exitName.put(key,name);
                exitArray.add(key);
            }
            //获取各个出口的人次和名称
            int count=0;
            for(int j=0;j<list.size();j++){
                for (String s : exitArray) {
                    Integer pv = list.getJSONObject(j).containsKey(s)?list.getJSONObject(j).getInteger(s):new Integer(0);
                    exitPvMap.merge(s, pv, Integer::sum);
                    count+=pv;
                }
            }
            //两个Map的根据key(进出口id)进行重新组合成一个新的map
            Map<String, Double> map=new HashMap<>();
            Set<String> keySet=exitPvMap.keySet();
            Set<String> key=exitName.keySet();
            for(String s:keySet){
                for (String k:key){
                    if(s.equals(k)){
                        map.put(exitName.get(k),businessUtil.getNumHalfUp((double)exitPvMap.get(s)*100/count,2));
                    }
                }
            }
            //判断饼状图中的中的百分比与计算的map中的结果是否一致
            IScene scene1=RealTimePortraitScene.builder().region(RegionTypeEnum.PARKING.getRegion()).build();
            JSONArray list1=scene1.visitor(visitor).execute().getJSONArray("list");
            for(int a=0;a<list1.size();a++){
                String keyName=list1.getJSONObject(a).getString("key");
                String percentPage=list1.getJSONObject(a).getString("percentage");
                double percentage=Double.parseDouble(percentPage.substring(0,percentPage.length()-1));
                double percentCount=map.getOrDefault(keyName,0.0);
                logger.info("饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
                Preconditions.checkArgument(percentage==percentCount,"饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("实时客流-停车场进出口占比=进出口的人次/各个进出口的人次的总和");
        }
    }

    /**
     *过店人数/进店人数/进店率/光顾率的柱状图的排名前五和后五的柱状图从小到大的比较--ok
     */
    @Test(description = "过店人数/进店人数/进店率/光顾率的柱状图的排名前五和后五的柱状图从大到小和从小到大的比较")
    public void mallCenterDataCase29(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sortTypeArray={SortTypeEnum.ENTRY.getSortType(),SortTypeEnum.ENTRY_PERCENTAGE.getSortType(),SortTypeEnum.PASS.getSortType(),SortTypeEnum.VISIT_PERCENTAGE.getSortType()};
            Arrays.stream(sortTypeArray).forEach(type->{

                   //获取柱状图的数据
                   IScene scene=RealTimeColumnarRankingScene.builder().type(type).build();
                   JSONObject response=scene.visitor(visitor).execute();
                   //排名前五的list
                   JSONArray topList=response.getJSONArray("top_five_list");
                if(type.equals(SortTypeEnum.ENTRY.getSortType())||type.equals(SortTypeEnum.PASS.getSortType())){
                   for(int i=0;i<topList.size()-1;i++){
                       int number=topList.getJSONObject(i).getInteger("number");
                       int numberNext=topList.getJSONObject(i+1).getInteger("number");
                       Preconditions.checkArgument(number>=numberNext,type+"按照排名前五的门店进行并排序，前一位的为："+number+"   后一位的为："+numberNext);
                   }

               }else{
                    for(int i=0;i<topList.size()-1;i++){
                        String percent=topList.getJSONObject(i).getString("percentage");
                        double number=businessUtil.getNumHalfUp(percent.substring(0,percent.length()-1),4);
                        String percentNext=topList.getJSONObject(i+1).getString("percentage");
                        double numberNext=businessUtil.getNumHalfUp(percentNext.substring(0,percentNext.length()-1),4);
                        Preconditions.checkArgument(number>=numberNext,type+"按照排名前五的门店进行并排序，前一位的为："+number+"   后一位的为："+numberNext);
                    }
               }
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("过店人数/进店人数/进店率/光顾率的柱状图的排名前五和后五的柱状图从大到小和从小到大的比较");
        }
    }

    /**
     *过店人数/进店人数/进店率/光顾率的柱状图的排名前五和后五的柱状图从大到小的比较--ok
     */
    @Test(description = "过店人数/进店人数/进店率/光顾率的柱状图的排名前五和后五的柱状图从大到小和从小到大的比较")
    public void mallCenterDataCase20(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sortTypeArray={SortTypeEnum.ENTRY.getSortType(),SortTypeEnum.ENTRY_PERCENTAGE.getSortType(),SortTypeEnum.PASS.getSortType(),SortTypeEnum.VISIT_PERCENTAGE.getSortType()};
            Arrays.stream(sortTypeArray).forEach(type->{

                //获取柱状图的数据
                IScene scene=RealTimeColumnarRankingScene.builder().type(type).build();
                JSONObject response=scene.visitor(visitor).execute();
                //排名前五的list
                JSONArray lastList=response.getJSONArray("last_five_list");
                if(type.equals(SortTypeEnum.ENTRY.getSortType())||type.equals(SortTypeEnum.PASS.getSortType())){
                    for(int i=0;i<lastList.size()-1;i++){
                        int number=lastList.getJSONObject(i).getInteger("number");
                        int numberNext=lastList.getJSONObject(i+1).getInteger("number");
                        logger.info(type+"按照排名后五的门店进行并排序，前一位的为："+number+"   后一位的为："+numberNext);
                        Preconditions.checkArgument(number>=numberNext,type+"按照排名后五的门店进行并排序，前一位的为："+number+"   后一位的为："+numberNext);
                    }
                }else{
                    for(int i=0;i<lastList.size()-1;i++){
                        String percent=lastList.getJSONObject(i).getString("percentage");
                        double number=businessUtil.getNumHalfUp(percent.substring(0,percent.length()-1),4);
                        String percentNext=lastList.getJSONObject(i+1).getString("percentage");
                        double numberNext=businessUtil.getNumHalfUp(percentNext.substring(0,percentNext.length()-1),4);
                        logger.info(type+"按照排名后五的门店进行并排序，前一位的为："+number+"   后一位的为："+numberNext);
                        Preconditions.checkArgument(number>=numberNext,type+"按照排名后五的门店进行并排序，前一位的为："+number+"   后一位的为："+numberNext);
                    }
                }
            });

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("过店人数/进店人数/进店率/光顾率的柱状图的排名前五和后五的柱状图从大到小和从小到大的比较");
        }
    }

    /**
     *实时客流-男性女性各个年龄段的占比相加==100%--ok
     */
    @Test(description = "实时客流-男性女性各个年龄段的占比相加==100%")
    public void mallCenterDataCase30(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取实时客流的客群画像的占比
            IScene scene =CustomersPortraitRealTimeScene.builder().mallId(mallId).build();
            JSONObject response=scene.visitor(visitor).execute();
            JSONArray list=response.getJSONArray("list");
            double sum=0L;
            for(int i=0;i<list.size();i++){
                String percent=list.getJSONObject(i).getString("age_group_percent");
                double percentNum=businessUtil.getNumHalfUp(percent.substring(0,percent.length()-1),2);
                sum+=percentNum;
            }
            logger.info("--------"+sum);
            Preconditions.checkArgument(sum<=100.02&&sum>=100.02,"所有年龄段的人数占比的相加之和为："+sum);
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
            IScene scene= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-1)).floorId(floorId).build();
            JSONObject response=scene.visitor(visitor).execute();
            //当前人数的日环比
            Double dayQoq=response.getJSONObject("uv_overview").getDouble("day_qoq");
            double dayQ= businessUtil.getNumHalfUp(dayQoq,4);
            //当日的的人数
            double number=response.getJSONObject("uv_overview").getDouble("number");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-2)).floorId(floorId).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            //获取人数
            double numberLast=response1.getJSONObject("uv_overview").getDouble("number");
            double num=numberLast==0?0:(number-numberLast)/numberLast;
            double percent= businessUtil.getNumHalfUp(num,4);
            logger.info(numberLast+"---"+number+"----"+percent+"-----"+dayQ);

            Preconditions.checkArgument(dayQ==percent,"通过昨天和前天的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人数的周同比
            Double dayQoq=response.getJSONObject("uv_overview").getDouble("pre_week_compare");
            double preWeekCompare=businessUtil.getNumHalfUp(dayQoq,4);
            //当日的的人数
            double number=response.getJSONObject("uv_overview").getDouble("number");

            //楼层客流概览-上周同一天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-8)).floorId(floorId).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            //获取人数
            double numberLast=response1.getJSONObject("uv_overview").getDouble("number");
            double num=numberLast==0?0:(number-numberLast)/numberLast;
            double percent= businessUtil.getNumHalfUp(num,4);
            logger.info(preWeekCompare+"      "+percent);

            Preconditions.checkArgument(preWeekCompare==percent,"通过昨天和上周同一天的数据计算出的周同比为："+percent+ "  页面中展示的周同比为："+preWeekCompare);

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
            IScene scene= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-1)).floorId(floorId).build();
            JSONObject response=scene.visitor(visitor).execute();
            //当前人次的日环比
            Double dayQoq=response.getJSONObject("pv_overview").getDouble("day_qoq");
            double dayQ= businessUtil.getNumHalfUp(dayQoq,4);
            //当日的的人次
            double number=response.getJSONObject("pv_overview").getDouble("number");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-2)).floorId(floorId).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            //获取人次
            double numberLast=response1.getJSONObject("pv_overview").getDouble("number");
            double num=numberLast==0?0:(number-numberLast)/numberLast;
            double percent= businessUtil.getNumHalfUp(num,4);
            logger.info(numberLast+"---"+number+"----"+percent+"-----"+dayQ);

            Preconditions.checkArgument(dayQ==percent,"通过昨天和前天的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人次的周同比
            Double dayQoq=response.getJSONObject("pv_overview").getDouble("pre_week_compare");
            double preWeekCompare=businessUtil.getNumHalfUp(dayQoq,4);
            //当日的的人次
            double number=response.getJSONObject("pv_overview").getDouble("number");

            //楼层客流概览-上周同一天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-8)).floorId(floorId).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            //获取人次
            double numberLast=response1.getJSONObject("pv_overview").getDouble("number");
            double num=numberLast==0?0:(number-numberLast)/numberLast;
            double percent= businessUtil.getNumHalfUp(num,4);
            logger.info(preWeekCompare+"      "+percent);

            Preconditions.checkArgument(preWeekCompare==percent,"通过昨天和上周同一天的数据计算出的周同比为："+percent+ "  页面中展示的周同比为："+preWeekCompare);
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
            IScene scene= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-1)).floorId(floorId).build();
            JSONObject response=scene.visitor(visitor).execute();
            //当前人均停留时长的日环比
            Double dayQoq=response.getJSONObject("stay_time_overview").getDouble("day_qoq");
            double dayQ= businessUtil.getNumHalfUp(dayQoq,4);
            //当日的的人均停留时长
            double number=response.getJSONObject("stay_time_overview").getDouble("number");

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-2)).floorId(floorId).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            //获取人均停留时长
            double numberLast=response1.getJSONObject("stay_time_overview").getDouble("number");
            double num=numberLast==0?0:(number-numberLast)/numberLast;
            double percent= businessUtil.getNumHalfUp(num,4);
            double count=businessUtil.getDecimalResult(dayQ,percent,"subtract",4);
            logger.info(count+"----"+percent+"-----"+dayQ);

            Preconditions.checkArgument(count<=0.01,"通过昨天和前天的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQ);
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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人均停留时长 的周同比
            Double dayQoq=response.getJSONObject("stay_time_overview").getDouble("pre_week_compare");
            double preWeekCompare=businessUtil.getNumHalfUp(dayQoq,4);
            //当日的的人均停留时长
            double number=response.getJSONObject("stay_time_overview").getDouble("number");

            //楼层客流概览-上周同一天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-8)).floorId(floorId).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            //获取人均停留时长
            double numberLast=response1.getJSONObject("stay_time_overview").getDouble("number");
            double num=numberLast==0?0:(number-numberLast)/numberLast;
            double percent= businessUtil.getNumHalfUp(num,4);
            double count=businessUtil.getDecimalResult(preWeekCompare,percent,"subtract",4);

            logger.info(preWeekCompare+"---------"+percent+"----"+numberLast);

            Preconditions.checkArgument(count<=0.01,"通过昨天和上周同一天的数据计算出的周同比为："+percent+ "  页面中展示的周同比为："+preWeekCompare);
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层分时客流：人均停留时长 周同比=昨天的人均停留时长-上周同天人均停留时长/上周同天人均停留时长");
        }
    }

    /**
     *楼层分时客流：爬楼率=楼层到访人数/各个楼层总人数--ok（现在逻辑有问题）
     */
    @Test(description = "楼层分时客流：爬楼率=楼层到访人数/各个楼层总人数")
    public void mallCenterDataCase36(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //楼层客流概览-昨天数据
            IScene scene= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-1)).floorId(floorId).build();
            JSONObject response=scene.visitor(visitor).execute();
            //获取爬楼率
            String percentage=response.getJSONObject("floor_enter_percentage_overview").getString("percentage");
            double floorPercent=businessUtil.getNumHalfUp(percentage.substring(0,percentage.length()-1),2);
            //获取当前楼层的人数
            double number=response.getJSONObject("uv_overview").getDouble("number");

           //获取场馆累计人数
            IScene scene1= OverviewVenueOverviewScene.builder().date(businessUtil.getDate(-1)).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            //当前人数
            int numberSum=response1.getJSONObject("uv_overview").getInteger("number");

            System.err.println(numberSum);
            double num=numberSum==0?0:(number*100)/numberSum;
            double  percent= businessUtil.getNumHalfUp(num,2);
            logger.info(floorPercent+"------"+percent);

            Preconditions.checkArgument(floorPercent==percent,"计算后的爬楼率为:"+percent+"  页面展示的爬楼率为："+floorPercent);
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
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-1)).build();
            JSONObject response=scene.visitor(visitor).execute();
            //获取爬楼率的日环比
            String dayQoq=response.getJSONObject("floor_enter_percentage_overview").getString("day_qoq");
            double  floorPercent=businessUtil.getNumHalfUp(dayQoq,4);
            //获取昨天的爬楼率
            String percentage=response.getJSONObject("floor_enter_percentage_overview").getString("percentage");
            double percentBefore=businessUtil.getNumHalfUp(percentage.substring(0,percentage.length()-1),4);

            //楼层客流概览-前天的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-2)).floorId(floorId).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            //获取前天的爬楼率
            String percentageLast=response1.getJSONObject("floor_enter_percentage_overview").getString("percentage");
            double percentLast=businessUtil.getNumHalfUp(percentageLast.substring(0,percentageLast.length()-1),4);

            double num=percentLast==0?0:(percentBefore-percentLast)/percentLast;
            double percent= businessUtil.getNumHalfUp(num,4);
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
            JSONObject response=scene.visitor(visitor).execute();
            //当前人数的周同比
            String dayQoq=response.getJSONObject("floor_enter_percentage_overview").getString("pre_week_compare");
            double preWeekCompare=businessUtil.getNumHalfUp(dayQoq.substring(0,dayQoq.length()-1),4);
            //当日的的爬楼率
            String number=response.getJSONObject("floor_enter_percentage_overview").getString("percentage");
            double floorPercent=Double.parseDouble(number.substring(0,number.length()-1));

            //楼层客流概览-上周同一日的数据
            IScene scene1= OverviewFloorOverviewScene.builder().date(businessUtil.getDate(-8)).floorId(floorId).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            //获取上周同一时间的周同比
            String numberLast=response1.getJSONObject("floor_enter_percentage_overview").getString("percentage");
            double floorLastPercent=Double.parseDouble(numberLast.substring(0,numberLast.length()-1));
            double num=floorLastPercent==0?0:(floorPercent-floorLastPercent)/floorLastPercent;
            double percent= businessUtil.getNumHalfUp(num,4);
            double count=businessUtil.getDecimalResult(preWeekCompare,percent,"subtract",4);
            logger.info(preWeekCompare+"      "+percent+"     "+count);

            Preconditions.checkArgument(count<=0.01,"通过昨天和上周同一天的数据计算出的爬楼率的周环比为："+percent+ "  页面中展示的日环比为："+preWeekCompare);

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
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-1)).build();
            JSONObject response=scene.visitor(visitor).execute();
            //当日的的人数
            int number=response.getJSONObject("uv_overview").getInteger("number");
            int sum=0;
            //获取历史的全场到访趋势图种的前一天当前小时的的UV数据
            IScene scene1= FullCourtTrendHistoryScene.builder().type("UV").date(businessUtil.getDate(-1)).scene(FloorTypeEnum.FLOOR.getFloorType()).floorId(floorId).build();
            JSONObject response1=scene1.visitor(visitor).execute();
            JSONArray list=response1.getJSONArray("list");
            for(int i=0;i<list.size();i++){
                int num=list.getJSONObject(i).getInteger("current_day");
                sum+=num;
                logger.info(sum+"-------"+num);
            }
            Preconditions.checkArgument(number<=sum,"当日的总人数为："+number+"   全场到访趋势图中的小时间uv和："+sum);

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
            IScene scene= OverviewFloorOverviewScene.builder().floorId(floorId).date(businessUtil.getDate(-1)).build();
            JSONObject response=scene.visitor(visitor).execute();
            //当日的的人次
            int number=response.getJSONObject("pv_overview").getInteger("number");
            int sum=0;

            //获取历史的全场到访趋势图种的前一天当前小时的的PV数据
            IScene scene1= FullCourtTrendHistoryScene.builder().type("PV").scene(FloorTypeEnum.FLOOR.getFloorType()).date(businessUtil.getDate(-1)).floorId(floorId).build();
            JSONObject response1=scene1.visitor(visitor).execute();
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
    @Test(description = "过店人数环比=前7天过店人数之和-上上个7天过店人数之和/上上个7天过店人数之和",enabled = false)
    public void mallCenterDataCase42(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=scene.visitor(visitor).execute();
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().visitor(visitor).execute().getJSONArray("list");
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
    @Test(description = "进店人数环比=前7天进店人数之和-上上个7天过进店人数之和/上上个7天进店人数之和",enabled = false)
    public void mallCenterDataCase43(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=scene.visitor(visitor).execute();
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().visitor(visitor).execute().getJSONArray("list");
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
    @Test(description = "进店率环比=前7天进店率-上上个7天进店率/上上个7天进店率",enabled = false)
    public void mallCenterDataCase44(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=scene.visitor(visitor).execute();
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().visitor(visitor).execute().getJSONArray("list");
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
    @Test(description = "人均停留时长环比=前7天人均停留时长-上上个7天人均停留时长/上上个7天人均停留时长",enabled = false)
    public void mallCenterDataCase45(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=scene.visitor(visitor).execute();
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().visitor(visitor).execute().getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    //人均停留时长
                    double stayTimeAvg=list.getJSONObject(i).getDouble("stay_time_avg");
                    //人均停留时长环比
                    String stayTimeAvgQoq=list.getJSONObject(i).getString("stay_time_avg_qoq");

                    //上一个七天的人均停留时长人数
                    double stayTimeAvgLast=list1.getJSONObject(i).getDouble("stay_time_avg");
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
    @Test(description = "进店率=前7天进店人数/前7天过店总人数（含进店）",enabled = false)
    public void mallCenterDataCase46(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=scene.visitor(visitor).execute();
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().visitor(visitor).execute().getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    //进店人数
                    double enterNumber=list.getJSONObject(i).getDouble("enter_number");
                    //进店率
                    String enterPercentage=list.getJSONObject(i).getString("enter_percentage");

                    //上一个七天的过店人数
                    double enterNumberLast=list1.getJSONObject(i).getDouble("enter_number");
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
    @Test(description = "光顾率环比=前7天光顾率之和-上上个7天光顾率之和/上上个7天光顾率之和",enabled = false)
    public void mallCenterDataCase47(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店数据分析列表  默认七天的数据
            IScene scene= ShopPageScene.builder().page(1).size(10).build();
            JSONObject response=scene.visitor(visitor).execute();
            int pages=response.getInteger("pages");
            for (int page=1;page<=pages;page++){
                JSONArray list=ShopPageScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list");
                //获取门店上一个七天的数据
                JSONArray list1=ShopPageScene.builder().page(1).size(10).startTime(businessUtil.getDateTime(-9)).endTime(businessUtil.getDateTime(-16)).build().visitor(visitor).execute().getJSONArray("list");
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



    //------------------------------------------------------------------------场馆客流周期数据---------------------------------------------------------------------

    /**
     *场馆周数据 人数/人次/人均停留时长环比=上周到访人数-上上周到访人数/上上周到访人数--ok
     */
    @Test(description = "场馆周期数据 人数/人次/人均停留时长环比=上周到访人数-上上周到访人数/上上周到访人数")
    public void mallCenterDataCase48(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] arrays={"pv_overview","stay_time_overview","uv_overview"};
            Arrays.stream(arrays).forEach(e->{
                //获取历史客流数据
                IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getStartDate(-1)).endTime(businessUtil.getEndDate(0)).build();
                JSONObject response=scene.visitor(visitor).execute();
                //当前人次
                double numberPv=response.getJSONObject(e).getDouble("number");
                //当前人次的周同比
                String preWeekCompare=response.getJSONObject(e).getString("day_qoq");
                BigDecimal weekNum=new BigDecimal(preWeekCompare);
                double weekPercent=weekNum.setScale(4,RoundingMode.HALF_UP).doubleValue();

                //获取历史客流上一周同一天的人数pv数据
                IScene scene1= OverviewVenueOverviewScene.builder().startTime(businessUtil.getStartDate(-2)).endTime(businessUtil.getEndDate(-1)).build();
                JSONObject response2=scene1.visitor(visitor).execute();
                double Yesterday=response2.getJSONObject(e).getDouble("number");
                double percent=Yesterday==0?0:(numberPv-Yesterday)/Yesterday;
                BigDecimal b=new  BigDecimal(percent);
                double qaqCount=b.setScale(4, RoundingMode.HALF_UP).doubleValue();
                //计算两个两个环比之间相减的绝对值
                double numAbs=businessUtil.getDecimalResult(weekPercent,qaqCount,"subtract",4);
                System.err.println(e+" 选择的是页面中展示的环比的数据为："+weekPercent+"计算出来的环比数据为："+qaqCount);
                Preconditions.checkArgument(numAbs<=0.01,e+" 选择的是页面中展示的环比的数据为："+weekPercent+"计算出来的环比数据为："+qaqCount);
            });

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆周期数据 人数/人次/人均停留时长环比=上周到访人数-上上周到访人数/上上周到访人数");
        }
    }

    /**
     *场馆月数据 人数/人次/人均停留时长环比=上月到访人数-上上月到访人数/上上月到访人数--ok
     */
    @Test(description = "场馆周期数据 人数/人次/人均停留时长环比=上月到访人数-上上月到访人数/上上月到访人数")
    public void mallCenterDataCase49(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] arrays={"pv_overview","stay_time_overview","uv_overview"};
            Arrays.stream(arrays).forEach(e->{
                //获取历史客流数据
                IScene scene= OverviewVenueOverviewScene.builder().startTime(businessUtil.getMonthStartDate(-1)).endTime(businessUtil.getMonthEndDate(-1)).build();
                JSONObject response=scene.visitor(visitor).execute();
                //当前人次
                double numberPv=response.getJSONObject(e).getDouble("number");
                //当前人次的环比
                String preWeekCompare=response.getJSONObject(e).getString("day_qoq");
                BigDecimal weekNum=new BigDecimal(preWeekCompare);
                double weekPercent=weekNum.setScale(4,RoundingMode.HALF_UP).doubleValue();

                //获取历史客流上月同一天的人数pv数据
                IScene scene1= OverviewVenueOverviewScene.builder().startTime(businessUtil.getMonthStartDate(-2)).endTime(businessUtil.getMonthEndDate(-2)).build();
                JSONObject response2=scene1.visitor(visitor).execute();
                double Yesterday=response2.getJSONObject(e).getDouble("number");
                double percent=Yesterday==0?0:(numberPv-Yesterday)/Yesterday;
                BigDecimal b=new  BigDecimal(percent);
                double qaqCount=b.setScale(4, RoundingMode.HALF_UP).doubleValue();
                //计算两个两个环比之间相减的绝对值
                double numAbs=businessUtil.getDecimalResult(weekPercent,qaqCount,"subtract",4);
                System.err.println(e+" 选择的是页面中展示的环比的数据为："+weekPercent+"计算出来的环比数据为："+qaqCount);
                Preconditions.checkArgument(numAbs<=0.01,e+" 选择的是页面中展示的环比的数据为："+weekPercent+"计算出来的环比数据为："+qaqCount);
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆周期数据 人数/人次/人均停留时长环比=上月到访人数-上上月到访人数/上上月到访人数");
        }
    }

    /**
     *场馆周数据-到访人数/人次<=【全场到访趋势图】种的各个时间段的人数相加--ok
     */
    @Test(description = "场馆周数据-到访人数/人次<=【全场到访趋势图】种的各个时间段的人数相加")
    public void mallCenterDataCase50(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[][] type={{"pv_overview","enter_pv","PV"},{"uv_overview","enter_uv","UV"}};
            for (String[] strings : type) {
                //获取历史客流数据
                IScene scene = OverviewVenueOverviewScene.builder().startTime(businessUtil.getStartDate(-1)).endTime(businessUtil.getEndDate(0)).build();
                JSONObject response = scene.visitor(visitor).execute();
                //当前人数
                int numberUv = response.getJSONObject(strings[0]).getInteger("number");

                //获取全场到访趋势图在中人数的之和
                IScene scene2 = CycleFullCourtTrendHistoryScene.builder().type(strings[2]).startTime(businessUtil.getStartDate(-1)).endTime(businessUtil.getEndDate(0)).build();
                JSONObject response2 = scene2.visitor(visitor).execute();
                int number = 0;
                JSONArray list = response2.getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    int hourNum = list.getJSONObject(j).containsKey(strings[1]) ? list.getJSONObject(j).getInteger(strings[1]) : 0;
                    number += hourNum;
                }
                logger.info(strings[2] + " 客流总览中的当前人数为：" + numberUv + "    全场到访趋势图在中人数的之和为：" + number);
                if (strings[2].equals("UV")) {
                    Preconditions.checkArgument(numberUv <= number, strings[2] + " 客流总览中的当前人数为：" + numberUv + "    全场到访趋势图在中人数的之和为：" + number);
                } else {
                    Preconditions.checkArgument(numberUv == number, strings[2] + " 客流总览中的当前人数为：" + numberUv + "    全场到访趋势图在中人数的之和为：" + number);
                }
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆周数据-到访人数/人次<=【全场到访趋势图】种的各个时间段的人数相加");
        }
    }

    /**
     *场馆月数据-到访人数/人次<=【全场到访趋势图】种的各个时间段的人数相加--ok(暂时月数据有问题)
     */
    @Test(description = "场馆月数据-到访人数/人次==【全场到访趋势图】种的各个时间段的人数相加")
    public void mallCenterDataCase51(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[][] type={{"pv_overview","enter_pv","PV"},{"uv_overview","enter_uv","UV"}};
            for (String[] strings : type) {
                //获取历史客流数据
                IScene scene = OverviewVenueOverviewScene.builder().startTime(businessUtil.getMonthStartDate(-1)).endTime(businessUtil.getMonthEndDate(-1)).build();
                JSONObject response = scene.visitor(visitor).execute();
                //当前人数
                int numberUv = response.getJSONObject(strings[0]).getInteger("number");

                //获取全场到访趋势图在中人数的之和
                IScene scene2 = CycleFullCourtTrendHistoryScene.builder().type(strings[2]).startTime(businessUtil.getMonthStartDate(-1)).endTime(businessUtil.getMonthEndDate(-1)).build();
                JSONObject response2 = scene2.visitor(visitor).execute();
                int number = 0;
                JSONArray list = response2.getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    int hourNum = list.getJSONObject(j).containsKey(strings[1]) ? list.getJSONObject(j).getInteger(strings[1]) : 0;
                    number += hourNum;
                }
                if (strings[2].equals("UV")) {
                    Preconditions.checkArgument(numberUv <= number, strings[2] + " 客流总览中的当前人数为：" + numberUv + "    全场到访趋势图在中人数的之和为：" + number);

                } else {
                    Preconditions.checkArgument(numberUv == number, strings[2] + " 客流总览中的当前人数为：" + numberUv + "    全场到访趋势图在中人数的之和为：" + number);
                }
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆月数据-到访人数/人次==【全场到访趋势图】种的各个时间段的人数相加");
        }
    }

    /**
     *场馆周数据-各出入口的人次的占比相加为100%&&各停车场到访人次占比=各停车场到访人次/停车场到访的总人次--ok
     */
    @Test(description = "场馆周数据-各出入口的人次的占比相加为100%&&各停车场到访人次占比=各停车场到访人次/停车场到访的总人次")
    public void mallCenterDataCase52(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] array={RegionTypeEnum.EXIT.getRegion(),RegionTypeEnum.PARKING.getRegion()};
            Arrays.stream(array).forEach(e->{
                IScene scene= CyclePortraitScene.builder().startTime(businessUtil.getStartDate(-1)).endTime(businessUtil.getEndDate(0)).region(e).build();
                JSONArray list=scene.visitor(visitor).execute().getJSONArray("list");
                double portNum=0;
                //获取楼层出入口的各个人数占比
                for(int i=0;i<list.size();i++){
                    String number=list.getJSONObject(i).getString("percentage");
                    double port=Double.parseDouble(number.substring(0,number.length()-1));
                    portNum=businessUtil.getDecimalResult(portNum,port,"add",2);
                }
                logger.info("----------"+portNum);

                Preconditions.checkArgument(portNum<=100.02&&portNum>=99.98,e+" 进出口到访趋势图-各进出口的人次的占比相加为："+portNum);
            });

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆周数据-各出入口的人次的占比相加为100%&&各停车场到访人次占比=各停车场到访人次/停车场到访的总人次");
        }
    }

    /**
     *场馆月数据-各出入口的人次的占比相加为100%&&各停车场到访人次占比=各停车场到访人次/停车场到访的总人次--ok(月数据暂时没有)
     */
    @Test(description = "场馆月数据-各出入口的人次的占比相加为100%&&各停车场到访人次占比=各停车场到访人次/停车场到访的总人次")
    public void mallCenterDataCase53(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] array={RegionTypeEnum.EXIT.getRegion(),RegionTypeEnum.PARKING.getRegion()};
            Arrays.stream(array).forEach(e->{
                IScene scene= CyclePortraitScene.builder().startTime(businessUtil.getMonthStartDate(-1)).endTime(businessUtil.getMonthEndDate(-1)).region(e).build();
                JSONArray list=scene.visitor(visitor).execute().getJSONArray("list");
                double portNum=0;
                //获取楼层出入口的各个人数占比
                for(int i=0;i<list.size();i++){
                    String number=list.getJSONObject(i).getString("percentage");
                    double port=Double.parseDouble(number.substring(0,number.length()-1));
                    portNum=businessUtil.getDecimalResult(portNum,port,"add",2);
                }
                System.err.println(e+" 进出口到访趋势图-各进出口的人次的占比相加为："+portNum);
                Preconditions.checkArgument(portNum<=100.02&&portNum>=99.98,e+" 进出口到访趋势图-各进出口的人次的占比相加为："+portNum);
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆月数据-各出入口的人次的占比相加为100%&&各停车场到访人次占比=各停车场到访人次/停车场到访的总人次");
        }
    }

    /**
     * 场馆月数据-进出口/停车场进出口占比=进出口的人次/各个进出口的人次的总和--ok
     */
    @Test(description = "场馆月数据-进出口/停车场进出口占比=进出口的人次/各个进出口的人次的总和")
    public void mallCenterDataCase60(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] arrays={RegionTypeEnum.PARKING.getRegion(),RegionTypeEnum.EXIT.getRegion()};
            Arrays.stream(arrays).forEach(e->{
                //获取楼层出入口的各个人数
                IScene scene= CycleRegionTrendScene.builder().startTime(businessUtil.getMonthStartDate(-1)).endTime(businessUtil.getMonthEndDate(-1)).type("PV").region(e).build();
                JSONObject response=scene.visitor(visitor).execute();
                JSONArray seriesList=response.getJSONArray("series_List");
                JSONArray list=response.getJSONArray("list");
                List<String> exitArray=new ArrayList<>();
                //存放各个出入口的名称和对应的人次的map
                Map<String,Integer> exitPvMap=new HashMap<>();
                //存放各个出入口的名称和id的map
                Map<String,String> exitName=new HashMap<>();
                //获取各个出入口的id和名称
                for(int i=0;i<seriesList.size();i++){
                    String key=seriesList.getJSONObject(i).getString("key");
                    String name=seriesList.getJSONObject(i).getString("name");
                    exitName.put(key,name);
                    exitArray.add(key);
                }
                //获取各个出口的人次和名称
                int count=0;
                for(int j=0;j<list.size();j++){
                    for (String s : exitArray) {
                        Integer pv = list.getJSONObject(j).containsKey(s)?list.getJSONObject(j).getInteger(s):new Integer(0);
                        if(pv>0){
                            exitPvMap.merge(s, pv, Integer::sum);
                            count+=pv;
                        }
                    }
                }
                //两个Map的根据key(进出口id)进行重新组合成一个新的map
                Map<String, Double> map=new HashMap<>();
                Set<String> keySet=exitPvMap.keySet();
                Set<String> key=exitName.keySet();
                for(String s:keySet){
                    for (String k:key){
                        if(s.equals(k)){
                            map.put(exitName.get(k),businessUtil.getNumHalfUp((double)exitPvMap.get(s)*100/count,2));
                        }
                    }
                }
                System.out.println(map);
                //判断饼状图中的中的百分比与计算的map中的结果是否一致
                IScene scene1=CyclePortraitScene.builder().startTime(businessUtil.getMonthStartDate(-1)).endTime(businessUtil.getMonthEndDate(-1)).region(e).build();
                JSONArray list1=scene1.visitor(visitor).execute().getJSONArray("list");
                for(int a=0;a<list1.size();a++){
                    String keyName=list1.getJSONObject(a).getString("key");
                    String percentPage=list1.getJSONObject(a).getString("percentage");
                    double percentage=Double.parseDouble(percentPage.substring(0,percentPage.length()-1));
                    double percentCount= map.getOrDefault(keyName, 0.0);
                    logger.info("饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
                    Preconditions.checkArgument(percentage==percentCount,"饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
                }
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆月数据-进出口/停车场进出口占比=进出口的人次/各个进出口的人次的总和");
        }
    }

    /**
     * 场馆周数据-进出口/停车场进出口占比=进出口的人次/各个进出口的人次的总和--ok
     */
    @Test(description = "场馆周数据-进出口/停车场进出口占比=进出口的人次/各个进出口的人次的总和")
    public void mallCenterDataCase61(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] arrays={RegionTypeEnum.PARKING.getRegion(),RegionTypeEnum.EXIT.getRegion()};
            Arrays.stream(arrays).forEach(e->{
                //获取楼层出入口的各个人数
                IScene scene= CycleRegionTrendScene.builder().startTime(businessUtil.getStartDate(-1)).endTime(businessUtil.getEndDate(0)).type("PV").region(e).build();
                JSONObject response=scene.visitor(visitor).execute();
                JSONArray seriesList=response.getJSONArray("series_List");
                JSONArray list=response.getJSONArray("list");
                List<String> exitArray=new ArrayList<>();
                //存放各个出入口的名称和对应的人次的map
                Map<String,Integer> exitPvMap=new HashMap<>();
                //存放各个出入口的名称和id的map
                Map<String,String> exitName=new HashMap<>();
                //获取各个出入口的id和名称
                for(int i=0;i<seriesList.size();i++){
                    String key=seriesList.getJSONObject(i).getString("key");
                    String name=seriesList.getJSONObject(i).getString("name");
                    exitName.put(key,name);
                    exitArray.add(key);
                }
                System.err.println(exitName);
                //获取各个出口的人次和名称
                int count=0;
                for(int j=0;j<list.size();j++){
                    for (String s : exitArray) {
                        Integer pv = list.getJSONObject(j).containsKey(s)?list.getJSONObject(j).getInteger(s):new Integer(0);
                        if(pv>0){
                            exitPvMap.merge(s, pv, Integer::sum);
                            count+=pv;
                        }
                    }
                }
                System.err.println(exitPvMap);
                //两个Map的根据key(进出口id)进行重新组合成一个新的map
                Map<String, Double> map=new HashMap<>();
                Set<String> keySet=exitPvMap.keySet();
                Set<String> key=exitName.keySet();
                for(String s:keySet){
                    for (String k:key){
                        if(s.equals(k)){
                            map.put(exitName.get(k),businessUtil.getNumHalfUp((double)exitPvMap.get(s)*100/count,2));
                        }
                    }
                }
                //判断饼状图中的中的百分比与计算的map中的结果是否一致
                IScene scene1=CyclePortraitScene.builder().startTime(businessUtil.getStartDate(-1)).endTime(businessUtil.getEndDate(0)).region(e).build();
                JSONArray list1=scene1.visitor(visitor).execute().getJSONArray("list");
                for(int a=0;a<list1.size();a++){
                    String keyName=list1.getJSONObject(a).getString("key");
                    String percentPage=list1.getJSONObject(a).getString("percentage");
                    double percentage=Double.parseDouble(percentPage.substring(0,percentPage.length()-1));
                    double percentCount= map.getOrDefault(keyName, 0.0);
                    logger.info("饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
                    Preconditions.checkArgument(percentage==percentCount,"饼状图中"+keyName+"  展示的百分比为："+percentage+",通过计算得出的结果为："+percentCount);
                }
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆周数据-进出口/停车场进出口占比=进出口的人次/各个进出口的人次的总和");
        }
    }


    /**
     * 场馆周期/楼层周期 男性占比+女性占比=100%--ok
     */
    @Test(description = "场馆周期/楼层周期 男性占比+女性占比=100%")
    public void mallCenterDataCase54(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sceneArray={"VENUE","FLOOR"};
            String[][] time={{businessUtil.getStartDate(-1),businessUtil.getEndDate(0),"周数据"},{businessUtil.getMonthStartDate(-1),businessUtil.getMonthEndDate(-1),"月数据"}};
            Arrays.stream(sceneArray).forEach(scene->{
                for (String[] strings : time) {
                    //获取各个年龄段的占比
                    IScene scene1 = scene.equals("VENUE") ? CycleCustomersPortraitScene.builder().scene(scene).startTime(strings[0]).endTime(strings[1]).floorId(floorId).build() : CycleCustomersPortraitScene.builder().scene(scene).startTime(strings[0]).endTime(strings[1]).floorId(floorId).build();
                    JSONObject response = scene1.visitor(visitor).execute();
                    String malePercent = response.getString("male_ratio_str");
                    double male = Double.parseDouble(malePercent.substring(0, malePercent.length() - 1));
                    String femalePercent = response.getString("female_ratio_str");
                    double female = Double.parseDouble(femalePercent.substring(0, malePercent.length() - 1));
                    double sum = male + female;
                   logger.info(strings[2] + " "+scene + "-男性占比为："+male+"  女性占比为："+female);
                    Preconditions.checkArgument((sum >= 99.98 && sum <= 100.02) || sum == 0.0, strings[2] + " " + scene + "-男性占比为："+male+"  女性占比为："+female);
                }
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆周期/楼层周期 男性占比+女性占比=100%");
        }
    }

    /**
     * 场馆周期/楼层周期 各年龄段占比=各年龄段女性占比+各年龄段男性占比--ok
     */
    @Test(description = "场馆周期/楼层周期 各年龄段占比=各年龄段女性占比+各年龄段男性占比")
    public void mallCenterDataCase55(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sceneArray={"VENUE","FLOOR"};
            String[][] time={{businessUtil.getStartDate(-1),businessUtil.getEndDate(0),"周数据"},{businessUtil.getMonthStartDate(-1),businessUtil.getMonthEndDate(-1),"月数据"}};
            Arrays.stream(sceneArray).forEach(scene->{
                for (String[] strings : time) {
                    //获取各个年龄段的占比
                    IScene scene1 = scene.equals("VENUE") ? CycleCustomersPortraitScene.builder().scene(scene).startTime(strings[0]).endTime(strings[1]).floorId(floorId).build() : CycleCustomersPortraitScene.builder().scene(scene).startTime(strings[0]).endTime(strings[1]).floorId(floorId).build();
                    JSONObject response =scene1.visitor(visitor).execute();
                    JSONArray list = response.getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String ageGroupPercent = list.getJSONObject(i).getString("age_group_percent");
                        double ageGroup = Double.parseDouble(ageGroupPercent.substring(0, ageGroupPercent.length() - 1));
                        String malePercent = list.getJSONObject(i).getString("male_percent");
                        double male = Double.parseDouble(malePercent.substring(0, malePercent.length() - 1));
                        String femalePercent = list.getJSONObject(i).getString("female_percent");
                        double female = Double.parseDouble(femalePercent.substring(0, femalePercent.length() - 1));
                        logger.info(scene + " " + strings[2] + " 男性占比为：" + malePercent + "  女性占比为：" + femalePercent + "  年龄段占比为：" + ageGroupPercent);
                        Preconditions.checkArgument(ageGroup <= (male + female + 0.02) && ageGroup >= (male + female - 0.02), scene + " " + strings[2] + " 男性占比为：" + malePercent + "  女性占比为：" + femalePercent + "  年龄段占比为：" + ageGroupPercent);
                    }
                }
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆周期/楼层周期 各年龄段占比=各年龄段女性占比+各年龄段男性占比");
        }
    }

    /**
     * 场馆周期/楼层周期 女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加--ok
     */
    @Test(description = "场馆周期/楼层周期 女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加（分时数据）")
    public void mallCenterDataCase56(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] sceneArray={"VENUE","FLOOR"};
            String[][] time={{businessUtil.getStartDate(-1),businessUtil.getEndDate(0),"周数据"},{businessUtil.getMonthStartDate(-1),businessUtil.getMonthEndDate(-1),"月数据"}};
            Arrays.stream(sceneArray).forEach(scene->{
                for (String[] strings : time) {
                    //获取各个年龄段的占比
                    IScene scene1 = scene.equals("VENUE") ? CycleCustomersPortraitScene.builder().scene(scene).startTime(strings[0]).endTime(strings[1]).floorId(floorId).build() : CycleCustomersPortraitScene.builder().scene(scene).startTime(strings[0]).endTime(strings[1]).floorId(floorId).build();
                    JSONObject response = scene1.visitor(visitor).execute();
                    String malePercentStr = response.getString("male_ratio_str");
                    double maleStr = Double.parseDouble(malePercentStr.substring(0, malePercentStr.length() - 1));
                    String femalePercentStr = response.getString("female_ratio_str");
                    double femaleStr = Double.parseDouble(femalePercentStr.substring(0, femalePercentStr.length() - 1));

                    JSONArray list = response.getJSONArray("list");
                    double maleNum = 0L;
                    double femaleNum = 0L;
                    for (int i = 0; i < list.size(); i++) {
                        String malePercent = list.getJSONObject(i).getString("male_percent");
                        double male = Double.parseDouble(malePercent.substring(0, malePercent.length() - 1));
                        String femalePercent = list.getJSONObject(i).getString("female_percent");
                        double female = Double.parseDouble(femalePercent.substring(0, femalePercent.length() - 1));
                        maleNum += male;
                        femaleNum += female;
                    }
                    logger.info(scene + " " + strings[2] + " " + maleNum + "      " + femaleNum + "     " + maleStr + "      " + femaleStr);
                    Preconditions.checkArgument(maleStr <= (maleNum + 0.05) && (maleNum - 0.05) <= maleStr, scene + " " + strings[2] + "女性占比为：" + malePercentStr + "  各年龄段女性占比相加为：" + maleNum);
                    Preconditions.checkArgument(femaleStr <= (femaleNum + 0.05) && (femaleStr - 0.05) <= femaleStr, scene + " " + strings[2] + "男性占比为：" + femalePercentStr + "  各年龄段男性占比相加为：" + femaleNum);

                }
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆周期/楼层周期 女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加（分时数据）");
        }
    }




//-------------------------------------------------------------楼层周期数据-------------------------------------------------------


    /**
     *楼层周期数据-到访人数/到访人次/人均停留时长 环比=本月-上月/上月--ok
     */
    @Test(description = "楼层周期数据-到访人数/到访人次/人均停留时长 环比=本月-上月/上月")
    public void mallCenterDataCase57(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] arrays={"floor_enter_percentage_overview","pv_overview","stay_time_overview","uv_overview"};
            Arrays.stream(arrays).forEach(e->{
                //楼层客流概览-昨天数据
                IScene scene= OverviewFloorOverviewScene.builder().startTime(businessUtil.getMonthStartDate(-1)).endTime(businessUtil.getMonthEndDate(-1)).floorId(floorId).build();
                JSONObject response=scene.visitor(visitor).execute();
                //当前环比
                Double dayQoq=response.getJSONObject(e).getDouble("day_qoq");
                BigDecimal qaq=new BigDecimal(dayQoq);
                double dayQ= qaq.setScale(4,RoundingMode.HALF_UP).doubleValue();
                //当日的的人数
                String percentage=e.equals("floor_enter_percentage_overview") ?response.getJSONObject(e).getString("percentage"):response.getJSONObject(e).getString("number");
                double number=e.equals("floor_enter_percentage_overview") ?Double.parseDouble(percentage.substring(0,percentage.length()-1)):Double.parseDouble(percentage);
                //楼层客流概览-前天的数据
                IScene scene1= OverviewFloorOverviewScene.builder().startTime(businessUtil.getMonthStartDate(-2)).endTime(businessUtil.getMonthEndDate(-2)).floorId(floorId).build();
                JSONObject response1=scene1.visitor(visitor).execute();
                //获取人数
                String percentageLast= e.equals("floor_enter_percentage_overview") ?response1.getJSONObject(e).getString("percentage"):response1.getJSONObject(e).getString("number");
                double numberLast=e.equals("floor_enter_percentage_overview") ?Double.parseDouble(percentageLast.substring(0,percentageLast.length()-1)):Double.parseDouble(percentageLast);
                double num=numberLast==0?0:(number-numberLast)/numberLast;
                BigDecimal b=new BigDecimal(num);
                double percent= b.setScale(4,RoundingMode.HALF_UP).doubleValue();
                double count=businessUtil.getDecimalResult(dayQ,percent,"subtract",4);
                logger.info(e+" 通过本月和上月的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
                if(e.equals("stay_time_overview")){
                    Preconditions.checkArgument(count<=0.01,e+" 通过本月和上月的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
                }else{
                    Preconditions.checkArgument(dayQ==percent,e+" 通过本月和上月的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
                }
            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层周期数据-到访人数/到访人次/人均停留时长 环比=本月-上月/上月");
        }
    }

    /**
     *楼层周期数据-到访人数/到访人次/人均停留时长 环比=本周-上周/上周--ok
     */
    @Test(description = "楼层周期数据-到访人数/到访人次/人均停留时长 环比=本周-上周/上周")
    public void mallCenterDataCase58(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            String[] arrays={"pv_overview","stay_time_overview","uv_overview","floor_enter_percentage_overview"};
            Arrays.stream(arrays).forEach(e->{
                //楼层客流概览-昨天数据
                IScene scene= OverviewFloorOverviewScene.builder().startTime(businessUtil.getStartDate(-1)).endTime(businessUtil.getEndDate(0)).floorId(floorId).build();
                JSONObject response=scene.visitor(visitor).execute();
                //当前环比
                Double dayQoq=response.getJSONObject(e).getDouble("day_qoq");
                BigDecimal qaq=new BigDecimal(dayQoq);
                double dayQ= qaq.setScale(4,RoundingMode.HALF_UP).doubleValue();
                //当日的的人数
                String percentage=e.equals("floor_enter_percentage_overview") ?response.getJSONObject(e).getString("percentage"):response.getJSONObject(e).getString("number");
                double number=e.equals("floor_enter_percentage_overview") ?Double.parseDouble(percentage.substring(0,percentage.length()-1)):Double.parseDouble(percentage);
                //楼层客流概览-前天的数据
                IScene scene1= OverviewFloorOverviewScene.builder().startTime(businessUtil.getStartDate(-2)).endTime(businessUtil.getEndDate(-1)).floorId(floorId).build();
                JSONObject response1=scene1.visitor(visitor).execute();
                //获取人数
                String percentageLast= e.equals("floor_enter_percentage_overview") ?response1.getJSONObject(e).getString("percentage"):response1.getJSONObject(e).getString("number");
                double numberLast=e.equals("floor_enter_percentage_overview") ?Double.parseDouble(percentageLast.substring(0,percentageLast.length()-1)):Double.parseDouble(percentageLast);
                double num=numberLast==0?0:(number-numberLast)/numberLast;
                BigDecimal b=new BigDecimal(num);
                double percent= b.setScale(4,RoundingMode.HALF_UP).doubleValue();
                double count=businessUtil.getDecimalResult(dayQ,percent,"subtract",4);
                System.out.println(e+" 通过本周和上周的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
                if(e.equals("stay_time_overview")||e.equals("floor_enter_percentage_overview")){
                    Preconditions.checkArgument(count<=0.01,e+" 通过本周和上周的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
                }else{
                    Preconditions.checkArgument(dayQ==percent,e+" 通过本周和上周的数据计算出的日环比为："+percent+ "  页面中展示的日环比为："+dayQoq);
                }

            });
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("楼层周期数据-到访人数/到访人次/人均停留时长 环比=本周-上周/上周");
        }
    }


    //-------------------------------------------------场景区域总览---------------------------------------------------

    /**
     *区域管理-列表中累计到访人数<=累计到访人次
     */
    @Test(description = "区域管理-列表中累计到访人数<=累计到访人次")
    public void mallCenterDataCase62(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //区域管理的列表
            IScene scene= RegionPageScene.builder().page(1).size(10).build();
            JSONArray list=scene.visitor(visitor).execute().getJSONArray("list");
            for(int i=0;i<list.size();i++){
                //累计人数
                int visitNumber=list.getJSONObject(i).getInteger("visit_number");
                //累计人次
                int visitTimes=list.getJSONObject(i).getInteger("visit_times");

                System.out.println("累计人次为："+visitTimes+"  累计人数为："+visitNumber);
                Preconditions.checkArgument(visitTimes>=visitNumber,"累计人次为："+visitTimes+"  累计人数为："+visitNumber);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("区域管理-列表中累计到访人数<=累计到访人次");
        }
    }

    /**
     *累计到访人次==客流趋势到访人次之和&&累计到访人数==客流趋势到访人数之和&&累计到访人数<=累计到访人次
     */
    @Test(description = "累计到访人次==客流趋势到访人次之和&&累计到访人数==客流趋势到访人数之和&&累计到访人数<=累计到访人次")
    public void mallCenterDataCase63(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //区域管理的列表
            IScene scene= RegionPageScene.builder().page(1).size(10).build();
            JSONArray list=scene.visitor(visitor).execute().getJSONArray("list");
            for(int i=0;i<list.size();i++) {
                //获取区域id
                String regionId = list.getJSONObject(i).getString("region_id");
                //场馆列表中查看详情页面
                IScene scene1 = OverviewRegionOverviewScene.builder().startTime(businessUtil.getDate(-8)).endTime(businessUtil.getDate(-1)).regionId(regionId).build();
                JSONObject response=scene1.visitor(visitor).execute();
                //获取人数
                int numberUv=response.getJSONObject("uv_overview").getInteger("number");
                //获取人次
                int numberPv=response.getJSONObject("pv_overview").getInteger("number");

                //获取到访趋势的人数的总和
                int sumUV=0;
                int sumPV=0;
                IScene scene2= RegionCustomerTrendScene.builder().regionId(regionId).startTime(businessUtil.getDate(-8)).endTime(businessUtil.getDate(-1)).build();
                JSONArray list1=scene2.visitor(visitor).execute().getJSONArray("list");
                for(int j=0;j<list1.size();j++){
                    int numberUV=list1.getJSONObject(j).containsKey("enter_uv")?list1.getJSONObject(j).getInteger("enter_uv"):0;
                    int numberPV=list1.getJSONObject(j).containsKey("enter_pv")?list1.getJSONObject(j).getInteger("enter_pv"):0;
                    sumUV+=numberUV;
                    sumPV+=numberPV;
                }
                Preconditions.checkArgument(numberUv<=numberPv," 客流总览中的当前人数为："+numberUv+"    客流总览中的当前人次为："+numberPv);
                Preconditions.checkArgument(numberUv<=sumUV," 客流总览中的当前人数为："+numberUv+"    全场到访趋势图在中人数的之和为："+sumUV);
                Preconditions.checkArgument(numberPv<=sumPV," 客流总览中的当前人次为："+numberPv+"    全场到访趋势图在中的之re人次和为："+sumPV);

            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("累计到访人次==客流趋势到访人次之和&&累计到访人数==客流趋势到访人数之和&&累计到访人数<=累计到访人次");
        }
    }

    /**
     *累计到访人次环比==前7天人次-前前7天人次/前前7天人次x100%
     */
    @Test(description = " 累计到访人次环比==前7天人次-前前7天人次/前前7天人次x100%")
    public void mallCenterDataCase64(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //区域管理的列表
            IScene scene= RegionPageScene.builder().page(1).size(10).build();
            JSONArray list=scene.visitor(visitor).execute().getJSONArray("list");
            for(int i=0;i<list.size();i++) {
                //获取区域id
                String regionId = list.getJSONObject(i).getString("region_id");
                //场馆列表中查看详情页面上个七天
                IScene scene1 = OverviewRegionOverviewScene.builder().startTime(businessUtil.getDate(-8)).endTime(businessUtil.getDate(-1)).regionId(regionId).build();
                JSONObject response=scene1.visitor(visitor).execute();
                //场馆列表中查看详情页面上上个七天
                IScene scene2 = OverviewRegionOverviewScene.builder().startTime(businessUtil.getDate(-16)).endTime(businessUtil.getDate(-9)).regionId(regionId).build();
                JSONObject response2=scene2.visitor(visitor).execute();
                //获取人次环比
                String number=response.getJSONObject("pv_overview").getString("pre_week_compare");
                double numberQoq=businessUtil.getNumHalfUp(number,4);
                //获取上个七天人次
                int numberUv=response.getJSONObject("pv_overview").getInteger("number");
                //上上个七天获取的人次
                int numberUvLast=response2.getJSONObject("pv_overview").getInteger("number");

                //计算过店人次环比
                double value=numberUv-numberUvLast;
                double qoq=numberUvLast==0?0:value/numberUvLast;
                double percent=businessUtil.getNumHalfUp(qoq,4);
                logger.info("页面展示的环比数据为："+numberQoq+"  计算结果的结果为："+percent);
                Preconditions.checkArgument(numberQoq==percent,"页面展示的环比数据为："+numberQoq+"  计算结果的结果为："+percent);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("累计到访人次环比==前7天人次-前前7天人次/前前7天人次x100%");
        }
    }

    /**
     *累计到访人数环比==前7天人数-前前7天人数/前前7天人数x100%
     */
    @Test(description = "累计到访人数环比==前7天人数-前前7天人数/前前7天人数x100% ")
    public void mallCenterDataCase65(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //区域管理的列表
            IScene scene= RegionPageScene.builder().page(1).size(10).build();
            JSONArray list=scene.visitor(visitor).execute().getJSONArray("list");
            for(int i=0;i<list.size();i++) {
                //获取区域id
                String regionId = list.getJSONObject(i).getString("region_id");
                //场馆列表中查看详情页面上个七天
                IScene scene1 = OverviewRegionOverviewScene.builder().startTime(businessUtil.getDate(-8)).endTime(businessUtil.getDate(-1)).regionId(regionId).build();
                JSONObject response=scene1.visitor(visitor).execute();
                //场馆列表中查看详情页面上上个七天
                IScene scene2 = OverviewRegionOverviewScene.builder().startTime(businessUtil.getDate(-16)).endTime(businessUtil.getDate(-9)).regionId(regionId).build();
                JSONObject response2=scene2.visitor(visitor).execute();
                //获取人数环比
                String number=response.getJSONObject("uv_overview").getString("pre_week_compare");
                double numberQoq=businessUtil.getNumHalfUp(number,4);
                //获取上个七天人数
                int numberUv=response.getJSONObject("uv_overview").getInteger("number");
                //上上个七天获取的人数
                int numberUvLast=response2.getJSONObject("uv_overview").getInteger("number");

                //计算过店人数环比
                double value=numberUv-numberUvLast;
                double qoq=numberUvLast==0?0:value/numberUvLast;
                double percent=businessUtil.getNumHalfUp(qoq,4);
                logger.info("页面展示的环比数据为："+numberQoq+"  计算结果的结果为："+percent);
                Preconditions.checkArgument(numberQoq==percent,"页面展示的环比数据为："+numberQoq+"  计算结果的结果为："+percent);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("累计到访人数环比==前7天人数-前前7天人数/前前7天人数x100%");
        }
    }

    /**
     *人均停留时长==前7天人均停留时长-前前7天人均停留时长/前前7天人均停留时长x100%
     */
    @Test(description = "人均停留时长==前7天人均停留时长-前前7天人均停留时长/前前7天人均停留时长x100% ")
    public void mallCenterDataCase66(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //区域管理的列表
            IScene scene= RegionPageScene.builder().page(1).size(10).build();
            JSONArray list=scene.visitor(visitor).execute().getJSONArray("list");
            for(int i=0;i<list.size();i++) {
                //获取区域id
                String regionId = list.getJSONObject(i).getString("region_id");
                //场馆列表中查看详情页面上个七天
                IScene scene1 = OverviewRegionOverviewScene.builder().startTime(businessUtil.getDate(-8)).endTime(businessUtil.getDate(-1)).regionId(regionId).build();
                JSONObject response=scene1.visitor(visitor).execute();
                //场馆列表中查看详情页面上上个七天
                IScene scene2 = OverviewRegionOverviewScene.builder().startTime(businessUtil.getDate(-16)).endTime(businessUtil.getDate(-9)).regionId(regionId).build();
                JSONObject response2=scene2.visitor(visitor).execute();
                //获取人次环比
                String number=response.getJSONObject("stay_time_overview").getString("pre_week_compare");
                double numberQoq=businessUtil.getNumHalfUp(number,4);
                //获取上个七天人次
                int numberUv=response.getJSONObject("stay_time_overview").getInteger("number");
                //上上个七天获取的人次
                int numberUvLast=response2.getJSONObject("stay_time_overview").getInteger("number");

                //计算过店人次环比
                double value=numberUv-numberUvLast;
                double qoq=numberUvLast==0?0:value/numberUvLast;
                double percent=businessUtil.getNumHalfUp(qoq,4);
                double count=businessUtil.getDecimalResult(numberQoq,percent,"subtract",4);
                logger.info("页面展示的环比数据为："+numberQoq+"  计算结果的结果为："+percent);
                Preconditions.checkArgument(count<=0.01,"页面展示的环比数据为："+numberQoq+"  计算结果的结果为："+percent);
            }

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("人均停留时长==前7天人均停留时长-前前7天人均停留时长/前前7天人均停留时长x100%");
        }
    }

    /**
     *区域进出口趋势-所有区域占比之和==100%
     */
    @Test(description = " 区域进出口趋势-所有区域占比之和==100%")
    public void mallCenterDataCase67(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //区域管理的列表
            IScene scene= RegionPageScene.builder().page(1).size(10).build();
            JSONArray list=scene.visitor(visitor).execute().getJSONArray("list");
            for(int i=0;i<list.size();i++) {
                //获取区域id
                String regionId = list.getJSONObject(i).getString("region_id");
                //获取区域进出口的
                IScene scene1= RegionPortraitScene.builder().regionId(regionId).startTime(businessUtil.getDate(-8)).endTime(businessUtil.getDate(-1)).build();
                JSONArray list1=scene1.visitor(visitor).execute().getJSONArray("list");
                double portNum=0;
                //获取楼层出入口的各个人数占比
                for(int j=0;j<list1.size();j++){
                    String number=list1.getJSONObject(j).getString("percentage");
                    double port=businessUtil.getNumHalfUp(Double.parseDouble(number.substring(0,number.length()-1)),4);
                    portNum=businessUtil.getDecimalResult(portNum,port,"add",4);
                }
                Preconditions.checkArgument(portNum<=100.01&&portNum>=99.99,"进出口到访趋势图-各进出口的人次的占比相加为："+portNum);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("区域进出口趋势-所有区域占比之和==100%");
        }
    }


    /**
     *客流趋势每日的人次==趋势图中每个出入口的人次之和--不等
     */
    @Test(description = "客流趋势每日的人次==趋势图中每个出入口的人次之和 ",enabled = false)
    public void mallCenterDataCase68(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //区域管理的列表
            IScene scene= RegionPageScene.builder().page(1).size(10).build();
            JSONArray list=scene.visitor(visitor).execute().getJSONArray("list");
            for(int i=0;i<list.size();i++) {
                //获取区域id
                String regionId = list.getJSONObject(i).getString("region_id");
                //获取客流到访的的总和
//                int sumCustomerUV=0;
                int sumCustomerPV=0;
                IScene scene2= RegionCustomerTrendScene.builder().regionId(regionId).startTime(businessUtil.getDate(-8)).endTime(businessUtil.getDate(-1)).build();
                JSONArray list1=scene2.visitor(visitor).execute().getJSONArray("list");
                for(int j=0;j<list1.size();j++){
//                    int numberUV=list1.getJSONObject(j).containsKey("enter_uv")?list1.getJSONObject(j).getInteger("enter_uv"):0;
                    int numberPV=list1.getJSONObject(j).containsKey("enter_pv")?list1.getJSONObject(j).getInteger("enter_pv"):0;
//                    sumCustomerUV+=numberUV;
                    sumCustomerPV+=numberPV;
                }
                //获取趋势进出口的人数和人次的总和
                IScene scene3= RegionRegionTrendScene.builder().startTime(businessUtil.getDate(-8)).endTime(businessUtil.getDate(-1)).type("PV").regionId(regionId).build();
                JSONObject response=scene3.visitor(visitor).execute();
                JSONArray list3=response.getJSONArray("list");
                JSONArray seriesList=response.getJSONArray("series_List");
                List<String> exitArray=new ArrayList<>();
                //存放各个出入口的名称和id的map
                Map<String,String> exitName=new HashMap<>();
                //获取各个出入口的id和名称
                for(int a=0;a<seriesList.size();a++){
                    String key=seriesList.getJSONObject(a).getString("key");
                    String name=seriesList.getJSONObject(a).getString("name");
                    exitName.put(key,name);
                    exitArray.add(key);
                }
                System.err.println(exitName);
                System.err.println(list3);
                //获取各个出口的人次和名称
                int count=0;
                for(int k=0;k<list3.size();k++){
                    for (String s : exitArray) {
                        int pv = list3.getJSONObject(k).containsKey(s)?list3.getJSONObject(k).getInteger(s):0;
                        System.out.println(s+"---------"+pv+"-------"+list3.getJSONObject(k).containsKey(s));

                        count+=pv;
                    }
                }

                System.out.println("各个出入口的人次总和为："+count+"  客流总览中的人次的总和为："+sumCustomerPV);
                Preconditions.checkArgument(count==sumCustomerPV,"各个出入口的人次总和为："+count+"  客流总览中的人次的总和为："+sumCustomerPV);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("客流趋势每日的人次==趋势图中每个出入口的人次之和");
        }
    }

    /**
     * 场馆客流分时/楼层客流分时的男性占比+女性占比=100%--ok
     */
    @Test(description = "场馆客流分时/楼层客流分时的男性占比+女性占比=100%")
    public void mallCenterDataCase69(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //区域管理的列表
            IScene scene= RegionPageScene.builder().page(1).size(10).build();
            JSONArray list=scene.visitor(visitor).execute().getJSONArray("list");
            for(int i=0;i<list.size();i++) {
                //获取区域id
                String regionId = list.getJSONObject(i).getString("region_id");
                //获取各个年龄段的占比
                IScene scene1 = RegionCustomersPortraitScene.builder().regionId(regionId).startTime(businessUtil.getDate(-8)).endTime(businessUtil.getDate(-1)).build();
                JSONObject response = scene1.visitor(visitor).execute();
                String malePercent = response.getString("male_ratio_str");
                double male = Double.parseDouble(malePercent.substring(0, malePercent.length() - 1));
                String femalePercent = response.getString("female_ratio_str");
                double female = Double.parseDouble(femalePercent.substring(0, malePercent.length() - 1));
                double sum = businessUtil.getDecimalResult(male, female, "add", 4);
                logger.info(femalePercent+"---------" + malePercent);
                Preconditions.checkArgument((sum >= 99.98 &&sum <= 100.02) || sum == 0.00, "男性占比和女性占比之和为：" + sum);
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("场馆客流分时/楼层客流分时的男性占比+女性占比=100%");
        }
    }

    /**
     * 各年龄段占比=各年龄段女性占比+各年龄段男性占比--ok
     */
    @Test(description = "各年龄段占比=各年龄段女性占比+各年龄段男性占比")
    public void mallCenterDataCase70(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //区域管理的列表
            IScene scene= RegionPageScene.builder().page(1).size(10).build();
            JSONArray list1=scene.visitor(visitor).execute().getJSONArray("list");
            for(int j=0;j<list1.size();j++) {
                //获取区域id
                String regionId = list1.getJSONObject(j).getString("region_id");
                //获取各个年龄段的占比
                IScene scene1 = RegionCustomersPortraitScene.builder().regionId(regionId).startTime(businessUtil.getDate(-8)).endTime(businessUtil.getDate(-1)).build();
                JSONObject response = scene1.visitor(visitor).execute();
                JSONArray list=response.getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String ageGroupPercent=list.getJSONObject(i).getString("age_group_percent");
                    double ageGroup=Double.parseDouble(ageGroupPercent.substring(0,ageGroupPercent.length()-1));
                    String malePercent=list.getJSONObject(i).getString("male_percent");
                    double male=Double.parseDouble(malePercent.substring(0,malePercent.length()-1));
                    String femalePercent=list.getJSONObject(i).getString("female_percent");
                    double female=Double.parseDouble(femalePercent.substring(0,femalePercent.length()-1));
                    Preconditions.checkArgument(ageGroup<=(male+female+0.02)||ageGroup>=(male+female-0.02),"男性占比为："+malePercent+"  女性占比为："+femalePercent+"  年龄段占比为："+ageGroupPercent);
                }
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("各年龄段占比=各年龄段女性占比+各年龄段男性占比");
        }
    }

    /**
     * 女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加
     */
    @Test(description = "女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加")
    public void mallCenterDataCase71(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //区域管理的列表
            IScene scene= RegionPageScene.builder().page(1).size(10).build();
            JSONArray list1=scene.visitor(visitor).execute().getJSONArray("list");
            for(int j=0;j<list1.size();j++) {
                //获取区域id
                String regionId = list1.getJSONObject(j).getString("region_id");
                //获取各个年龄段的占比
                IScene scene1 = RegionCustomersPortraitScene.builder().regionId(regionId).startTime(businessUtil.getDate(-8)).endTime(businessUtil.getDate(-1)).build();
                JSONObject response = scene1.visitor(visitor).execute();
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
                    maleNum=businessUtil.getDecimalResult(maleNum,male,"add",2);
                    femaleNum=businessUtil.getDecimalResult(femaleNum,female,"add",2);
                    logger.info(male+"--------"+female);
                }
                logger.info(maleNum+"      "+femaleNum+"     "+maleStr+"      "+femaleStr+"    ");
                Preconditions.checkArgument(maleStr<=maleNum+0.01||maleNum-0.01<=maleStr,"女性占比为："+malePercentStr+"  各年龄段女性占比相加为："+maleNum);
                Preconditions.checkArgument( femaleStr<=femaleNum+0.01&&femaleStr-0.01<=femaleStr," 男性占比为："+femalePercentStr+"  各年龄段男性占比相加为："+femaleNum);



            }

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("女性占比=各年龄段女性占比相加&&男性占比=各年龄段男性占比相加");
        }
    }
















}
