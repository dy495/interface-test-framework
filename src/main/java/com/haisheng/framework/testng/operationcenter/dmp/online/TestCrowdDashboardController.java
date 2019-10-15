package com.haisheng.framework.testng.operationcenter.dmp.online;

import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.operationcenter.dmp.StatusCode;
import com.haisheng.framework.testng.operationcenter.dmp.TimeDimensionEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.util.HttpHelper;
import com.haisheng.framework.util.QADbUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 人群仪表盘接口测试
 */
@Slf4j
public class TestCrowdDashboardController {


    /**
     * 环境   线上为 ONLINE
     */
    private String failReason = "";
    private String response   = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID    = ChecklistDbInfo.DB_APP_ID_OPEN_PLATFORM_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_OPERATION_CENTER;
    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/operation-center/buildWithParameters?case_name=";

    private final String DMP_HOST = "http://10.0.16.44"; //online

    private final String NODE_ID = "668";

    private final String SUBJECT_ID = "669";

    private final String API_SOURCE = "DMP";

    private final String UID = "uid_7fc78d24";

    private final String U_NAME = "实验室Demo";

    private HttpClient client;
    private final int TIME_OUT = 3000;

    public Map<String, String> getHeader() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("request_id", UUID.randomUUID().toString());
        headerMap.put("api_source", API_SOURCE);
        headerMap.put("uid", UID);
        headerMap.put("node_id", NODE_ID);
        headerMap.put("Content-Type", "application/json");

        JSONObject creatorObj = new JSONObject();
        creatorObj.put("uid", UID);
        creatorObj.put("name", U_NAME);
        creatorObj.put("source", API_SOURCE);
        String decode = "";
        try {
            decode = URLEncoder.encode(JSON.toJSONString(creatorObj), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("test error", e);
        }
        headerMap.put("creator", decode);
        return headerMap;
    }

    @BeforeSuite
    public void initial() {
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
    }

    /********************************** 人群仪表盘接口 **********************************/

    @Test
    public  void crowRealAnalysis() {
        String requestUrl = DMP_HOST + "/dashboard/crowd/real/analysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result);

            JSONArray analysisList = data.getJSONArray("analysis_list");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(analysisList),
                    "趋势图返回为空");

            Preconditions.checkArgument(analysisList.size() >= 4,
                    "趋势图返回长度小于4");
            for (int i = 0; i < analysisList.size(); i++) {
                JSONObject obj = analysisList.getJSONObject(i);
                Preconditions.checkArgument(null != obj.getLong("id"),
                        "返回人群id为空");
                Preconditions.checkArgument(!StringUtils.isEmpty(obj.getString("name")),
                        "返回人群名称为空");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(obj.getJSONArray("uv_list")),
                        "uv列表为空");
            }
        } catch (Exception e) {
            failReason = e.toString();
        }


        Case aCase      = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/crowd/real/analysis 人群实时分析");
    }


    @Test
    public  void crowRealProgressRatio() {
        String requestUrl = DMP_HOST + "/dashboard/crowd/real/progressRatio";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            /*区分时间维度**/
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                /*校验返回**/
                JSONObject data = checkRspCode(result);

                /*校验人群信息**/
                JSONArray crowdList = data.getJSONArray("crowd_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(crowdList),
                        "人群信息列表为空");

                Preconditions.checkArgument(crowdList.size() >= 4,
                        "人群信息列表长度小于4");

                for (int i = 0; i < crowdList.size(); i++) {
                    JSONObject crowObj = crowdList.getJSONObject(i);
                    Preconditions.checkArgument(null != crowObj.getLong("crowd_id"),
                            "返回人群id为空");
                    Preconditions.checkArgument(!StringUtils.isEmpty(crowObj.getString("crowd_name")),
                            "返回人群名称为空");
                    Preconditions.checkArgument(null != crowObj.getString("total_num"),
                            "返回人群总人数为空");
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(crowObj.getJSONArray("progress_ratio")),
                            "返回人群消费历程为空");
                }
                /*年龄消费历程**/
                JSONArray ageDistribution = data.getJSONArray("age_distribution");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(ageDistribution),
                        "年龄消费历程为空或长度小于5");
                Preconditions.checkArgument(ageDistribution.size() >= 5 ,
                        "年龄消费历程长度小于5");

            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/crowd/real/progressRatio 人群实时分析");
    }


    @Test
    public  void crowdAgeSex() {
        String requestUrl = DMP_HOST + "/dashboard/crowd/real/ageSex";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                /*校验返回**/
                JSONObject data = checkRspCode(result);

                JSONObject male = data.getJSONObject("male");
                Preconditions.checkArgument(null != male,
                        "返回男性性别占比为空");
                Preconditions.checkArgument(null != male.getLong("total_num"),
                        "返回男性总人数为空");
                JSONArray maleAgeList = male.getJSONArray("list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(maleAgeList),
                        "返回男性年龄占比为空");

                Preconditions.checkArgument(maleAgeList.size() >= 8,
                        "返回男性年龄占比长度小于8");

                JSONObject female = data.getJSONObject("female");
                Preconditions.checkArgument(null != female,
                        "返回女性性别占比为空");
                Preconditions.checkArgument(null != female.getLong("total_num"),
                        "返回女性总人数为空");
                JSONArray femaleAgeList = male.getJSONArray("list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(femaleAgeList),
                        "返回女性年龄占比为空");
                Preconditions.checkArgument(femaleAgeList.size() >= 8 ,
                        "返回女性年龄占比长度小于8");
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/crowd/real/ageSex 人群实时分析");
    }


    @Test
    public  void crowRealLabel() {
        String requestUrl = DMP_HOST + "/dashboard/crowd/real/label";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));

                JSONObject data = checkRspCode(result);
                /*校验人群信息**/
                JSONArray crowdList = data.getJSONArray("crowd_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(crowdList),
                        "人群信息为空");
                Preconditions.checkArgument(crowdList.size() >= 4,
                        "人群信息长度小于4");

                for (int i = 0; i < crowdList.size(); i++) {
                    JSONObject crowObj = crowdList.getJSONObject(i);
                    Preconditions.checkArgument(null != crowObj.getLong("crowd_id"),
                            "返回人群id为空");
                    Preconditions.checkArgument(!StringUtils.isEmpty(crowObj.getString("crowd_name")),
                            "返回人群名称为空");
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(crowObj.getJSONArray("label_list")),
                            "返回人群标签为空");
                }
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/crowd/real/label 人群实时分析");
    }

    @Test
    public  void crowdRealMemberAnalysis() {
        String requestUrl = DMP_HOST + "/dashboard/crowd/real/memberAnalysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));

                /*校验返回**/
                JSONObject data = checkRspCode(result);
                JSONArray crowdList = data.getJSONArray("crowd_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(crowdList),
                        "人群信息列表为空");
                Preconditions.checkArgument(crowdList.size() >= 4 ,
                        "人群信息列表长度小于4");
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/crowd/real/memberAnalysis 人群实时分析");
    }


    /********************************店铺仪表盘******************************************/
    @Test
    public  void shopRealAnalysis() {
        String requestUrl = DMP_HOST + "/dashboard/shop/real/analysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result);

            /*校验返回**/
            JSONArray analysisList = data.getJSONArray("analysis_list");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(analysisList),
                    "趋势图返回为空");

            Preconditions.checkArgument(analysisList.size() >= 4 ,
                    "趋势图返回长度小于4");
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/shop/real/analysis 店铺实时分析");
    }


    @Test
    public  void shopRealStayAnalysis() {
        String requestUrl = DMP_HOST + "/dashboard/shop/real/stayAnalysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result);
                JSONArray analysisList = data.getJSONArray("analysis_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(analysisList),
                        "返回店铺信息为空");
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/shop/real/stayAnalysis 人群实时分析");
    }

    @Test
    public  void shopRealMemberAnalysis() {
        String requestUrl = DMP_HOST + "/dashboard/shop/real/memberAnalysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result);
                JSONArray list = data.getJSONArray("list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(list),
                        "返回平面信息为空");
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/shop/real/memberAnalysis 人群实时分析");
    }


    /***********************************************业态仪表盘***********************/
    @Test
    public  void formatRealAnalysis() {
        String requestUrl = DMP_HOST + "/dashboard/format/real/analysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result);

            /*校验返回**/
            JSONArray analysisList = data.getJSONArray("analysis_list");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(analysisList),
                    "业态趋势返回为空");

            Preconditions.checkArgument(analysisList.size() >= 4,
                    "业态趋势返回长度小于4");
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/format/real/analysis 业态实时分析");
    }


    @Test
    public  void formatRealCoincidence() {
        String requestUrl = DMP_HOST +  "/dashboard/format/real/coincidence";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result);

                JSONArray coincidenceList = data.getJSONArray("coincidence_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(coincidenceList),
                        "主业态重合度返回为空");

                Preconditions.checkArgument(coincidenceList.size() >= 4,
                        "主业态重合度返回长度小于4");

                JSONArray layoutList = data.getJSONArray("layout_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(layoutList),
                        "平面业态信息返回为空");
            }
        } catch (Exception e) {
            failReason = e.toString();
        }



        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/format/real/coincidence 业态实时分析");
    }

    @Test
    public  void formatRealCoincidenceDetail() {
        String requestUrl = DMP_HOST + "/dashboard/format/real/coincidenceDetail";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                requestJson.put("layout_id", 680L);
                requestJson.put("format_id", 1060L);
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result);
                JSONArray list = data.getJSONArray("list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(list),
                        "子业态重合度返回为空");
            }
        } catch (Exception e) {
            failReason = e.toString();
        }



        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/format/real/coincidenceDetail 业态实时分析");
    }

    @Test
    public  void formatRealHistogram() {
        String requestUrl = DMP_HOST+ "/dashboard/format/real/histogram";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            //只有七天和30天
            for (TimeDimensionEnum value : values) {
                if(value.equals(TimeDimensionEnum.ONE_D)){
                    continue;
                }
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result);
                JSONArray histogramList = data.getJSONArray("histogram_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(histogramList),
                        "主业态客流柱状图返回为空");
                Preconditions.checkArgument( histogramList.size() >= 4,
                        "主业态客流柱状图返回长度小于4");
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/format/real/histogram 业态实时分析");
    }

    @Test
    public  void formatRealCrowdRelation() {
        String requestUrl = DMP_HOST + "/dashboard/format/real/crowdRelation";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            //之后七天和30天
            for (TimeDimensionEnum value : values) {
                if(value.equals(TimeDimensionEnum.ONE_D)){
                    continue;
                }
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result);
                JSONArray crowdList = data.getJSONArray("crowd_list");
                Preconditions.checkArgument( !CollectionUtils.isEmpty(crowdList) ,
                        "人群列表返回为空");
                Preconditions.checkArgument( crowdList.size() >= 4,
                        "人群列表返回长度小于4");
                JSONArray relations = data.getJSONArray("relations");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(relations) ,
                        "主业态人群关系列表返回为空");

                Preconditions.checkArgument( relations.size() >= 4,
                        "主业态人群关系列表返回长度小于4");

            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/format/real/crowdRelation 业态实时分析");
    }

    @Test
    public  void formatRealMemberAnalysis() {
        String requestUrl = DMP_HOST + "/dashboard/format/real/memberAnalysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result);
                JSONArray list = data.getJSONArray("list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(list),
                        "主业态泛会员转化率列表返回为空");

                Preconditions.checkArgument(list.size() >= 4,
                        "主业态泛会员转化率列表返回长度小于4");
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/format/real/memberAnalysis 业态实时分析");
    }

    /************************************************客流仪表盘**************************************************/

    @Test
    public  void customerRealStatistic() {
        String requestUrl = DMP_HOST + "/dashboard/customer/real/statistics";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result);

            JSONArray uv = data.getJSONArray("uv");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(uv),
                    "客流统计uv列表为空");

            JSONArray aveTime = data.getJSONArray("ave_time");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(aveTime),
                    "客流统计平均停留时间列表为空");

            JSONArray stayNum = data.getJSONArray("stay_num");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(stayNum),
                    "客流统计在店人数列表列表为空");

            JSONArray aveEnterShopCount = data.getJSONArray("ave_enter_shop_count");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(aveEnterShopCount),
                    "客流统计平均进店次数表列表为空");
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/customer/real/statistics 客流实时分析");
    }


    @Test
    public  void customerRealEntrance(){
        String requestUrl = DMP_HOST + "/dashboard/customer/real/entrance";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result);

            JSONObject up = data.getJSONObject("up");
            Preconditions.checkArgument(null != up,
                    "客流地面客流为空");

            JSONObject down = data.getJSONObject("down");
            Preconditions.checkArgument(null != down,
                    "客流地下客流为空");
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/customer/real/entrance 客流实时分析");
    }


    @Test
    public  void customerRealFloor(){
        String requestUrl = DMP_HOST + "/dashboard/customer/real/floor";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result);

            JSONArray layoutTrendList = data.getJSONArray("layout_trend_list");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(layoutTrendList),
                    "客流统计平面客流趋势列表为空");

            JSONArray layoutList = data.getJSONArray("layout_list");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(layoutList),
                    "客流统计平面信息列表为空");
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/customer/real/floor 客流实时分析");

    }

    @Test
    public  void customerRealHistogram(){
        String requestUrl =  DMP_HOST + "/dashboard/customer/real/histogram";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                if(value.equals(TimeDimensionEnum.ONE_D)){
                    continue;
                }
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result);
                JSONArray histogramList = data.getJSONArray("histogram_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(histogramList),
                        "客流柱状图返回为空");
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/customer/real/histogram 客流实时分析");
    }


    @Test
    public  void customerRealRegionTrend(){
        String requestUrl = DMP_HOST + "/dashboard/customer/real/regionTrend";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                if(value.equals(TimeDimensionEnum.ONE_D)){
                    continue;
                }
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result);
                JSONArray topRegionList = data.getJSONArray("top_region_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(topRegionList),
                        "客流区域排行列表返回为空");
                JSONArray trendList = data.getJSONArray("trend_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(trendList),
                        "客流区域趋势列表返回为空");

            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/customer/real/regionTrend 客流实时分析");
    }


    @Test
    public  void customerRealMemberAnalysis(){
        String requestUrl =  DMP_HOST + "/dashboard/customer/real/memberAnalysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result);

                JSONArray funnel = data.getJSONArray("funnel");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(funnel),
                        "客流信息列表返回为空");

                JSONObject memberData = data.getJSONObject("member_data");
                Preconditions.checkArgument(null != memberData,
                        "客流信息泛会员转化率返回为空");
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "/dashboard/customer/real/memberAnalysis 客流实时分析");
    }
    /*********** 公共方法 *********/
    public JSONObject checkRspCode(HttpHelper.Result result) {
        response = result.getContent();
        if (!result.isSuccess()) {
            throw new RuntimeException("result code is not 200");
        }
        JSONObject rspJson = JSON.parseObject(result.getContent());
        if (!StatusCode.SUCCESS.getCode().equals(rspJson.getInteger("code"))) {
            log.error("",JSON.toJSONString(rspJson));
            throw new RuntimeException("result json code is not 1000");
        }
        JSONObject data = rspJson.getJSONObject("data");
        Preconditions.checkArgument(null != data,
                "返回data为空");
        return data;
    }


    private void setBasicParaToDB(Case aCase, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + caseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000 && key col not null");
        aCase.setResponse(response);

        if (! StringUtils.isEmpty(failReason) && StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private void saveData(Case aCase, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        Assert.assertNull(aCase.getFailReason());
    }

}
