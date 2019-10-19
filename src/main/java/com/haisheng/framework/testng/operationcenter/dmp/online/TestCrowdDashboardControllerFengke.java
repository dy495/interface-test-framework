package com.haisheng.framework.testng.operationcenter.dmp.online;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.testng.operationcenter.dmp.StatusCode;
import com.haisheng.framework.testng.operationcenter.dmp.TimeDimensionEnum;
import com.haisheng.framework.util.AlarmPush;
import com.haisheng.framework.util.DateTimeUtil;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 人群仪表盘接口测试
 */
@Slf4j
public class TestCrowdDashboardControllerFengke {


    /**
     * 环境   线上为 ONLINE
     */
    private String failReason = "";
    private String response   = "";
    private DateTimeUtil dt   = new DateTimeUtil();
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID    = ChecklistDbInfo.DB_APP_ID_OPEN_PLATFORM_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_OPERATION_CENTER;
    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/operation-center/buildWithParameters?case_name=";

    //{"uid_09845c0e","241","242","万达广场丰科店线上"},
    private final String DMP_HOST   = "http://10.0.16.44"; //online
    private final String NODE_ID    = "241";
    private final String SUBJECT_ID = "242";
    private final String API_SOURCE = "DMP";
    private final String UID        = "uid_09845c0e";
    private final String U_NAME     = "万达广场丰科店线上";

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
                        "年龄消费历程为空");
                Preconditions.checkArgument(ageDistribution.size() >= 4 ,
                        "年龄消费历程长度小于4");

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
                    "业态-趋势数组为空");

            Preconditions.checkArgument(analysisList.size() >= 4,
                    "业态-趋势数组长度小于4");

            float percent = 0;
            for (int i=0; i<analysisList.size(); i++) {
                JSONObject item = analysisList.getJSONObject(i);
                //name不为空
                String name = item.getString("name");
                Preconditions.checkArgument(!StringUtils.isEmpty(name) && !name.contains("null"),
                        "业态-趋势数组[" + i + "]" + ".name 为空");

                //percent相加为1
                percent += item.getFloat("percent");

                //uv list size == 24
                JSONArray uvList = item.getJSONArray("uv_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(uvList),
                        "业态-趋势数组[" + i + "]" + ".uv_list 为空");

                Preconditions.checkArgument(uvList.size() == 24,
                        "业态-趋势数组[" + i + "]" + ".uv_list 长度不为24, size == " + uvList.size());

                List<String> uvArray = uvList.toJavaList(String.class);
                //0到当前时间的整点有数据, 且数据递增
                int lastValue = 0;
                int hour = Integer.parseInt(dt.getCurrentHour());
                for (int index=0; index<hour; index++) {
                    String vs = uvArray.get(index);
                    Preconditions.checkArgument(!StringUtils.isEmpty(vs) && !vs.contains("null"),
                            "业态-趋势数组[" + i + "]" + ".uv_list.[" + index + "] 为空");
                    int value = Integer.parseInt(vs);
                    if (0==index) {
                        lastValue = value;
                    }
                    Preconditions.checkArgument(value>=0,
                            "业态-趋势数组[" + i + "]" + ".uv_list.[" + index + "] < 0 , value: " + value);

                    Preconditions.checkArgument(value>=lastValue,
                            "业态-趋势数组[" + i + "]" + ".uv_list.[" + index + "] 值小于上个小时的值, current value: " + value + ", last hour value: " + lastValue);

                    lastValue = value;
                }


            }
            //percent相加为1
            Preconditions.checkArgument(1 == percent,
                    "业态-趋势数组中percent相加不等于1, percent sum == " + percent);
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
        //2534
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

    //接口没有调用，故注销
    //@Test
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
                    "客流-地面客流为空");
            int uv = up.getInteger("uv");
            Preconditions.checkArgument(uv > 0,
                    "客流-地面客流uv=" + uv);
            int pv = up.getInteger("pv");
            Preconditions.checkArgument(pv > 0,
                    "客流-地面客流pv=" + pv);
            Preconditions.checkArgument(pv >= uv,
                    "客流-地面客流pv < uv, pv: " + pv + ", uv: " + uv);
            JSONArray entranceList = up.getJSONArray("entrance_list");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(entranceList),
                    "客流-地面客流entrance_list为空");
            float uvPercent = entranceList.getJSONObject(0).getFloat("uv_percent_num");
            Preconditions.checkArgument(uvPercent > 0,
                    "客流-地面客流uv_percent_num=" + uvPercent);
            uvPercent = 0;
            int uvLast = 0;
            String nameLast = "";
            for (int i=0; i<entranceList.size(); i++) {
                uvPercent += entranceList.getJSONObject(i).getFloat("uv_percent_num");

                //uv降序检查
                uv = entranceList.getJSONObject(i).getInteger("uv");
                String name = entranceList.getJSONObject(i).getString("name");
                Preconditions.checkArgument(!StringUtils.isEmpty(name),
                        "客流-地面客流entrance_list数组中name为空");
                if (0 == i) {
                    uvLast = uv;
                    nameLast = name;
                }
                Preconditions.checkArgument(uv <= uvLast,
                        "客流-地面客流entrance_list数组中uv数据顺序异常, " + nameLast + "uv: " + uvLast + ", " + name + "uv: " + uv);
                uvLast = uv;
                nameLast = name;

            }
            log.info("up total uv_percent_num: " + uvPercent);
            Preconditions.checkArgument(uvPercent > 0.5 && uvPercent < 2,
                    "客流-地面客流uv_percent_num all sum < 0.5 or >2, sum == " + uvPercent);
            float pvPercent = entranceList.getJSONObject(0).getFloat("pv_percent_num");
            Preconditions.checkArgument(pvPercent > 0,
                    "客流-地面客流pv_percent_num=" + pvPercent);
            pvPercent = 0;
            int pvLast = 0;
            nameLast = "";
            for (int i=0; i<entranceList.size(); i++) {
                pvPercent += entranceList.getJSONObject(i).getFloat("pv_percent_num");

                //uv降序检查
                pv = entranceList.getJSONObject(i).getInteger("pv");
                String name = entranceList.getJSONObject(i).getString("name");
                Preconditions.checkArgument(!StringUtils.isEmpty(name),
                        "客流-地面客流entrance_list数组中name为空");
                if (0 == i) {
                    pvLast = pv;
                    nameLast = name;
                }
                Preconditions.checkArgument(pv <= pvLast,
                        "客流-地面客流entrance_list数组中uv数据顺序异常, " + nameLast + "pv: " + pvLast + ", " + name + "pv: " + pv);
                pvLast = pv;
                nameLast = name;

            }
            log.info("up total pv_percent_num: " + pvPercent);
            Preconditions.checkArgument(pvPercent > 0.5 && pvPercent < 2,
                    "客流-地面客流pv_percent_num all sum < 0.5 or >2, sum == " + pvPercent);


            JSONObject down = data.getJSONObject("down");
            Preconditions.checkArgument(null != down,
                    "客流-地下客流为空");
            uv = down.getInteger("uv");
            Preconditions.checkArgument(uv > 0,
                    "客流-地下客流uv=" + uv);
            pv = down.getInteger("pv");
            Preconditions.checkArgument(pv > 0,
                    "客流-地下客流pv=" + pv);
            Preconditions.checkArgument(pv >= uv,
                    "客流-地下客流pv < uv, pv: " + pv + ", uv: " + uv);
            entranceList = down.getJSONArray("entrance_list");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(entranceList),
                    "客流-地下客流entrance_list为空");
            uvPercent = entranceList.getJSONObject(0).getFloat("uv_percent_num");
            Preconditions.checkArgument(uvPercent > 0,
                    "客流-地下客流uv_percent_num=" + uvPercent);
            uvPercent = 0;
            uvLast = 0;
            nameLast = "";
            for (int i=0; i<entranceList.size(); i++) {
                uvPercent += entranceList.getJSONObject(i).getFloat("uv_percent_num");

                //uv降序检查
                uv = entranceList.getJSONObject(i).getInteger("uv");
                String name = entranceList.getJSONObject(i).getString("name");
                Preconditions.checkArgument(!StringUtils.isEmpty(name),
                        "客流-地下客流entrance_list数组中name为空");
                if (0 == i) {
                    uvLast = uv;
                    nameLast = name;
                }
                Preconditions.checkArgument(uv <= uvLast,
                        "客流-地下客流entrance_list数组中uv数据顺序异常, " + nameLast + "uv: " + uvLast + ", " + name + "uv: " + uv);
                uvLast = uv;
                nameLast = name;

            }
            log.info("down total uv_percent_num: " + uvPercent);
            Preconditions.checkArgument(uvPercent > 0.5 && uvPercent < 2,
                    "客流-地下客流uv_percent_num all sum < 0.5 or >2, sum == " + uvPercent);
            pvPercent = entranceList.getJSONObject(0).getFloat("pv_percent_num");
            Preconditions.checkArgument(pvPercent > 0,
                    "客流-地下客流pv_percent_num=" + pvPercent);
            pvPercent = 0;
            pvLast = 0;
            nameLast = "";
            for (int i=0; i<entranceList.size(); i++) {
                pvPercent += entranceList.getJSONObject(i).getFloat("pv_percent_num");

                //uv降序检查
                pv = entranceList.getJSONObject(i).getInteger("pv");
                String name = entranceList.getJSONObject(i).getString("name");
                Preconditions.checkArgument(!StringUtils.isEmpty(name),
                        "客流-地下客流entrance_list数组中name为空");
                if (0 == i) {
                    pvLast = pv;
                    nameLast = name;
                }
                Preconditions.checkArgument(pv <= pvLast,
                        "客流-地下客流entrance_list数组中uv数据顺序异常, " + nameLast + "pv: " + pvLast + ", " + name + "pv: " + pv);
                pvLast = pv;
                nameLast = name;

            }
            log.info("down total pv_percent_num: " + pvPercent);
            Preconditions.checkArgument(pvPercent > 0.5 && pvPercent < 2,
                    "客流-地下客流pv_percent_num all sum < 0.5 or >2, sum == " + pvPercent);
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
                    "客流-统计平面客流趋势列表为空");

            JSONArray layoutList = data.getJSONArray("layout_list");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(layoutList),
                    "客流-统计平面信息列表为空");
            float totalUpstairsRateSum = 0;
            float currentUpstairsRateSum = 0;
            for (int i=0; i<layoutList.size(); i++) {
                JSONObject item = layoutList.getJSONObject(i);
                float totalUpstairsRate = item.getFloat("total_upstairs_rate");
                float currentUpstairsRate = item.getFloat("current_upstairs_rate");
                int totalUvNum = item.getInteger("total_uv_num");
                int currentStayUvNum = item.getInteger("current_stay_uv_num");
                String floor = item.getString("layout_name");

                Preconditions.checkArgument(!StringUtils.isEmpty(floor),
                        "客流-统计平面信息列表-layout_name为空");
                Preconditions.checkArgument(totalUpstairsRate > 0,
                        "客流-统计平面信息列表-" + floor + "累计爬楼率<=0, value: " + totalUpstairsRate);
                Preconditions.checkArgument(currentUpstairsRate > 0,
                        "客流-统计平面信息列表-" + floor + "当前爬楼率<=0, value: " + currentUpstairsRate);
                Preconditions.checkArgument(totalUvNum > 0,
                        "客流-统计平面信息列表-" + floor + "累计人数<=0, value: " + totalUvNum);
                Preconditions.checkArgument(currentStayUvNum > 0,
                        "客流-统计平面信息列表-" + floor + "当前人数<=0, value: " + currentStayUvNum);

                totalUpstairsRateSum += totalUpstairsRate;
                currentUpstairsRateSum += currentUpstairsRate;

            }
            Preconditions.checkArgument(totalUpstairsRateSum >= 1,
                    "客流-统计平面信息列表-" + "所有楼层累计爬楼率总和<1, value: " + totalUpstairsRateSum);
            Preconditions.checkArgument(currentUpstairsRateSum >= 1,
                    "客流-统计平面信息列表-" + "所有楼层当前爬楼率总和<1, value: " + currentUpstairsRateSum);
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
                        "客流-柱状图histogram_list为空");
                Preconditions.checkArgument(histogramList.size() >=3,
                        "客流-柱状图-histogram_list size < 3，size: " + histogramList.size());

                for (int i=0; i<histogramList.size(); i++) {
                    JSONObject item = histogramList.getJSONObject(i);
                    String name = item.getString("name");
                    int femaleMemNum = item.getInteger("female_member_num");
                    int totalFemale = item.getInteger("female_total_num");
                    int maleMemNum = item.getInteger("male_member_num");
                    int totalMale = item.getInteger("male_total_num");

                    //check array order
                    if (value.equals(TimeDimensionEnum.SEVEN_D)) {
                        if (0 == i) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(name) && name.equals("休息日"),
                                    "客流-柱状图-数组[0].name 不是休息日，name: " + name);
                        } else if (1 == i) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(name) && name.equals("工作日"),
                                    "客流-柱状图-数组[1].name 不是工作日，name: " + name);
                        } else if (2 == i) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(name) && name.equals("今天"),
                                    "客流-柱状图-数组[2].name 不是今天，name: " + name);
                        }
                    } else if (value.equals(TimeDimensionEnum.THIRTY_D)) {
                        if (i < 2) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(name) && name.trim().length()==11,
                                    "客流-柱状图-数组[" + i + "].name 长度小于11, trendName: " + name);
                        } else if (2 == i) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(name) && name.equals("上周"),
                                    "客流-柱状图-数组[2].name 不是上周, trendName: " + name);
                        } else if (3 == i) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(name) && name.equals("本周"),
                                    "客流-柱状图-数组[3].name 不是本周, trendName: " + name);
                        }
                    }

                    Preconditions.checkArgument(femaleMemNum <= totalFemale,
                            "客流-柱状图-" + name + "女性泛会员大于女性到访");
                    Preconditions.checkArgument(maleMemNum <= totalMale,
                            "客流-柱状图-" + name + "男性泛会员大于男性到访");

                }
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

                int uvLast = 0;
                for (int i=0; i<topRegionList.size(); i++) {
                    //uv 顺序递减 且 region_name 非空
                    int uv = topRegionList.getJSONObject(i).getInteger("uv");
                    if (0 == i) {
                        uvLast = uv;
                    }
                    Preconditions.checkArgument(uv <= uvLast,
                            "客流-区域排行列表-uv 排序错误，数组前一条记录uv: " + uvLast + ", 数组当前记录uv: " + uv);
                    String regionName = topRegionList.getJSONObject(i).getString("region_name");
                    Preconditions.checkArgument(!StringUtils.isEmpty(regionName),
                            "客流-区域排行列表-region_name为空");

                    uvLast = uv;

                }

                JSONArray trendList = data.getJSONArray("trend_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(trendList),
                        "客流-区域趋势列表返回为空");
                Preconditions.checkArgument(trendList.size() >= 4,
                        "客流-区域趋势列表数组长度小于4, size: " + trendList.size());
                for (int i=0; i<trendList.size(); i++) {
                    String trendName = trendList.getJSONObject(i).getString("trend_name");
                    if (value.equals(TimeDimensionEnum.SEVEN_D)) {
                        if (0 == i) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(trendName) && trendName.equals("休息日"),
                                    "客流-区域趋势列表数组[0].trendName不是休息日, trendName: " + trendName);
                        } else if (1 == i) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(trendName) && trendName.equals("工作日"),
                                    "客流-区域趋势列表数组[1].trendName不是工作日, trendName: " + trendName);
                        } else if (2 == i) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(trendName) && trendName.equals("昨天"),
                                    "客流-区域趋势列表数组[2].trendName不是昨天, trendName: " + trendName);
                        } else if (3 == i) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(trendName) && trendName.equals("今天"),
                                    "客流-区域趋势列表数组[3].trendName不是今天, trendName: " + trendName);
                        }
                    } else if (value.equals(TimeDimensionEnum.THIRTY_D)) {
                        if (i < 2) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(trendName) && trendName.trim().length()==11,
                                    "客流-区域趋势列表数组[2].trendName长度小于11, trendName: " + trendName);
                        } else if (2 == i) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(trendName) && trendName.equals("上周"),
                                    "客流-区域趋势列表数组[2].trendName不是上周, trendName: " + trendName);
                        } else if (3 == i) {
                            Preconditions.checkArgument(!StringUtils.isEmpty(trendName) && trendName.equals("本周"),
                                    "客流-区域趋势列表数组[3].trendName不是本周, trendName: " + trendName);
                        }
                    }

                    JSONArray topList = trendList.getJSONObject(i).getJSONArray("top_list");
                    Preconditions.checkArgument(topList.size() <= 7,
                            "客流-区域趋势列表中top_list数组长度大于7, size: " + topList.size());
                    uvLast = 0;
                    for (int j=0; j<topList.size(); j++) {
                        int uv = topList.getJSONObject(j).getInteger("uv");
                        if (0 == j) {
                            uvLast = uv;
                        }
                        Preconditions.checkArgument(uv <= uvLast,
                                "客流-区域趋势列表-top_list-uv 排序错误，数组前一条记录uv: " + uvLast + ", 数组当前记录uv: " + uv);
                        String regionName = topList.getJSONObject(j).getString("region_name");
                        Preconditions.checkArgument(!StringUtils.isEmpty(regionName),
                                "客流-区域趋势列表-top_list-region_name为空");

                        uvLast = uv;
                    }
                }

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
        log.info("customerRealMemberAnalysis, debug");

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
                        "客流-信息列表返回为空");
                Preconditions.checkArgument(funnel.size() == 4,
                        "客流-信息列表数组长度不为4, size: " + funnel.size());
                int uvLast = 0;
                String nameLast = "";
                for (int i=0; i<funnel.size(); i++) {
                    //数组顺序检查
                    String name = funnel.getJSONObject(i).getString("name");
                    if (0 == i) {
                        Preconditions.checkArgument(!StringUtils.isEmpty(name) && name.equals("全部客流"),
                                "客流-信息列表数组[0].name不是全部客流, name: " + name);
                        nameLast = name;
                    } else if (1 == i) {
                        Preconditions.checkArgument(!StringUtils.isEmpty(name) && name.equals("泛会员客流"),
                                "客流-信息列表数组[1].name不是泛会员客流, name: " + name);
                    } else if (2 == i) {
                        Preconditions.checkArgument(!StringUtils.isEmpty(name) && name.equals("引导客流"),
                                "客流-信息列表数组[2].name不是引导客流, name: " + name);
                    }else if (3 == i) {
                        Preconditions.checkArgument(!StringUtils.isEmpty(name) && name.equals("转化客流"),
                                "客流-信息列表数组[3].name不是转化客流, name: " + name);
                    }

                    //uv降序检查
                    int uv = funnel.getJSONObject(i).getInteger("uv");
                    if (0 == i) {
                        uvLast = uv;
                    }
                    Preconditions.checkArgument(uv <= uvLast,
                            "客流-信息列表-漏斗uv数据异常, " + nameLast + "uv: " + uvLast + ", " + name + "uv: " + uv);
                    uvLast = uv;
                    nameLast = name;

                }

                JSONObject memberData = data.getJSONObject("member_data");
                Preconditions.checkArgument(!StringUtils.isEmpty(memberData),
                        "客流-信息泛会员为空");
                String riseRate = memberData.getString("rise_rate");
                Preconditions.checkArgument(!StringUtils.isEmpty(riseRate),
                        "客流-信息泛会员增长率为空");
                float inversionRate = memberData.getFloat("inversion_rate");
                Preconditions.checkArgument(inversionRate >= 0,
                        "客流-信息泛会员转化率<0, inversion_rate: " + inversionRate);

                int todayNewMemberNum = memberData.getInteger("today_new_member_num");
                int todayMemberActive = memberData.getInteger("today_member_active");
                int timeTotalNewMemberNum = memberData.getInteger("time_total_new_member_num");
                int totalMemberNum = memberData.getInteger("total_member_num");

                Preconditions.checkArgument(todayNewMemberNum>=todayMemberActive,
                        "客流-信息泛会员今日活跃数>今日新增数, 今日活跃数: " + todayMemberActive + "，今日新增数: " + todayNewMemberNum);
                Preconditions.checkArgument(timeTotalNewMemberNum>=todayNewMemberNum,
                        "客流-信息泛会员最近7天<今日新增数, 最近7天: " + timeTotalNewMemberNum + "，今日新增数: " + todayNewMemberNum);
                Preconditions.checkArgument(totalMemberNum>=timeTotalNewMemberNum,
                        "客流-信息泛会员累计<最近7天, 最近7天: " + timeTotalNewMemberNum + "，累计: " + totalMemberNum);



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
//        qaDbUtil.saveToCaseTable(aCase);
        if (! StringUtils.isEmpty(aCase.getFailReason())) {
            log.error(aCase.getFailReason());
            dingPush("丙昇线上 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();

        alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

        alarmPush.onlineMonitorPvuvAlarm(msg);
        Assert.assertTrue(false);

    }

}
