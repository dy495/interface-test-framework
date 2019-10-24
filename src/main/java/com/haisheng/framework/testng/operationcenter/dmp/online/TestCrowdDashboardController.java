package com.haisheng.framework.testng.operationcenter.dmp.online;

import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.operationcenter.dmp.StatusCode;
import com.haisheng.framework.testng.operationcenter.dmp.TimeDimensionEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
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
public class TestCrowdDashboardController {


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

    private final String DMP_HOST   = "http://10.0.16.44"; //online
    private final String NODE_ID    = "668";
    private final String SUBJECT_ID = "669";
    private final String API_SOURCE = "DMP";
    private final String UID        = "uid_7fc78d24";
    private final String U_NAME     = "实验室Demo";

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
        //2543
        String requestUrl = DMP_HOST + "/dashboard/crowd/real/analysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());

            JSONArray analysisList = data.getJSONArray("analysis_list");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(analysisList),
                    "人群-趋势图返回为空");

            Preconditions.checkArgument(analysisList.size() == 4,
                    "人群-趋势图数组长度不为4, size: " + analysisList.size());
            for (int i = 0; i < analysisList.size(); i++) {
                JSONObject obj = analysisList.getJSONObject(i);
                Preconditions.checkArgument(null != obj.getLong("id"),
                        "人群-趋势图数组[" + i + "].id为空");
                String name = obj.getString("name");
                Preconditions.checkArgument(!StringUtils.isEmpty(name) && !name.trim().equals("null"),
                        "人群-趋势图数组[" + i + "].name为空");
                if (0 == i) {
                    Preconditions.checkArgument(name.trim().equals("新青年"),
                            "人群-趋势图数组[" + i + "].name不为 新青年, name: " + name);
                } else if (1 == i) {
                    Preconditions.checkArgument(name.trim().equals("新中产"),
                            "人群-趋势图数组[" + i + "].name不为 新中产, name: " + name);
                } else if (2 == i) {
                    Preconditions.checkArgument(name.trim().equals("新家庭"),
                            "人群-趋势图数组[" + i + "].name不为 新家庭, name: " + name);
                } else if (3 == i) {
                    Preconditions.checkArgument(name.trim().equals("其他"),
                            "人群-趋势图数组[" + i + "].name不为 其他, name: " + name);
                }

                JSONArray uvList = obj.getJSONArray("uv_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(uvList),
                        "人群-趋势图数组[" + i + "].uv_list为空");
                Preconditions.checkArgument(uvList.size() == 24,
                        "人群-趋势图数组[" + i + "]" + ".uv_list 长度不为24, size == " + uvList.size());

                List<String> uvArray = uvList.toJavaList(String.class);
                //0到当前时间的整点有数据, 且数据递增
                int lastValue = 0;
                int hour = Integer.parseInt(dt.getCurrentHour());
                for (int index=0; index<hour; index++) {
                    String vs = uvArray.get(index);
                    Preconditions.checkArgument(!StringUtils.isEmpty(vs) && !vs.contains("null"),
                            "人群-趋势图数组[" + i + "]" + ".uv_list.[" + index + "] 为空");
                    int value = Integer.parseInt(vs);
                    if (0==index) {
                        lastValue = value;
                    }
                    Preconditions.checkArgument(value >= 0,
                            "人群-趋势图数组[" + i + "]" + ".uv_list.[" + index + "] < 0 , value: " + value);
                    Preconditions.checkArgument(value>=lastValue,
                            "人群-趋势图数组[" + i + "]" + ".uv_list.[" + index + "] 值小于上个小时的值, current value: " + value + ", last hour value: " + lastValue);

                    lastValue = value;
                }
            }
        } catch (Exception e) {
            failReason = e.toString();
        }


        Case aCase      = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2543 /dashboard/crowd/real/analysis 人群实时分析");
    }


    @Test
    public  void crowRealProgressRatio() {
        //2544
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
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());

                /*校验人群信息**/
                JSONArray crowdList = data.getJSONArray("crowd_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(crowdList),
                        "人群-信息列表数组为空");

                Preconditions.checkArgument(crowdList.size() == 4,
                        "人群-信息列表数组长度不为4, size: " + crowdList.size());

                //crowd_list[i].progress_ratio.percent 相加>=1
                float percent = 0;
                for (int i = 0; i < crowdList.size(); i++) {
                    JSONObject crowObj = crowdList.getJSONObject(i);
                    Preconditions.checkArgument(null != crowObj.getLong("crowd_id"),
                            "人群-信息列表数组[" + i + "].id 为空");
                    Preconditions.checkArgument(!StringUtils.isEmpty(crowObj.getString("crowd_name")),
                            "人群-信息列表数组[" + i + "].crowd_name 为空");
                    Preconditions.checkArgument(null != crowObj.getString("total_num"),
                            "人群-信息列表数组[" + i + "].total_num 为空");
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(crowObj.getJSONArray("progress_ratio")),
                            "人群-信息列表数组[" + i + "].progress_ratio 为空");



                    //active_num >=0
                    int active_num = crowObj.getInteger("active_num");
                    Preconditions.checkArgument(active_num >= 0,
                            "人群-信息列表数组[" + i + "].active_num < 0, active_num: " + active_num);

                    //member_num >=0
                    int member_num = crowObj.getInteger("member_num");
                    Preconditions.checkArgument(member_num >= 0,
                            "人群-信息列表数组[" + i + "].member_num < 0, member_num: " + member_num);

                    //crowd_name order 新青年、新中产、新家庭、其他
                    String crowd_name = crowObj.getString("crowd_name");
                    if (0 == i) {
                        Preconditions.checkArgument(crowd_name.trim().equals("新青年"),
                                "人群-信息列表数组[" + i + "].crowd_name不是 新青年, crowd_name: " + crowd_name);
                    } else if (1 == i) {
                        Preconditions.checkArgument(crowd_name.trim().equals("新中产"),
                                "人群-信息列表数组[" + i + "].crowd_name不是 新中产, crowd_name: " + crowd_name);
                    } else if (2 == i) {
                        Preconditions.checkArgument(crowd_name.trim().equals("新家庭"),
                                "人群-信息列表数组[" + i + "].crowd_name不是 新家庭, crowd_name: " + crowd_name);
                    } else if (3 == i) {
                        Preconditions.checkArgument(crowd_name.trim().equals("其他"),
                                "人群-信息列表数组[" + i + "].crowd_name不是 其他, crowd_name: " + crowd_name);
                    }

                    //total_num >=0
                    int total_num = crowObj.getInteger("total_num");
                    Preconditions.checkArgument(total_num >= 0,
                            "人群-信息列表数组[" + i + "].total_num < 0, total_num: " + total_num);

                    //progress_ratio not null
                    JSONArray progress_ratio = crowObj.getJSONArray("progress_ratio");
                    int size = progress_ratio.size();
                    Preconditions.checkArgument(size == 4,
                            "人群-信息列表数组[" + i + "].progress_ratio数组大小不为4, size: " + size);
                    for (int j=0; j<size; j++) {
                        JSONObject item = progress_ratio.getJSONObject(j);
                        String progress_key = item.getString("progress_key");
                        Preconditions.checkArgument(!StringUtils.isEmpty(progress_key) && !progress_key.trim().equals("null"),
                                "人群-信息列表数组[" + i + "].progress_ratio[" + j + "].progress_key 为空");
                        if (0 == j) {
                            Preconditions.checkArgument(progress_key.trim().equals("AWARE"),
                                    "人群-信息列表数组[" + i + "].progress_ratio[" + j + "].progress_key 不为 AWARE, progress_key: " + progress_key);
                        } else if (1 == j) {
                            Preconditions.checkArgument(progress_key.trim().equals("INTEREST"),
                                    "人群-信息列表数组[" + i + "].progress_ratio[" + j + "].progress_key 不为 AWARE, progress_key: " + progress_key);
                        } else if (2 == j) {
                            Preconditions.checkArgument(progress_key.trim().equals("LOYALTY"),
                                    "人群-信息列表数组[" + i + "].progress_ratio[" + j + "].progress_key 不为 AWARE, progress_key: " + progress_key);
                        } else if (3 == j) {
                            Preconditions.checkArgument(progress_key.trim().equals("PURCHASE"),
                                    "人群-信息列表数组[" + i + "].progress_ratio[" + j + "].progress_key 不为 AWARE, progress_key: " + progress_key);
                        }
                        percent += item.getFloat("percent");
                    }
                }
                Preconditions.checkArgument(percent >=1, "" +
                        "crowd_list[*].progress_ratio[*].percent 相加<1, percent: " + percent);

                /*年龄消费历程**/
                JSONArray ageDistribution = data.getJSONArray("age_distribution");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(ageDistribution),
                        "人群-年龄消费列表为空");
                Preconditions.checkArgument(ageDistribution.size() == 4 ,
                        "人群-年龄消费列表数组长度不为4, size: " + ageDistribution.size());
                for (int i=0; i<ageDistribution.size(); i++) {
                    JSONObject item = ageDistribution.getJSONObject(i);
                    //progress_key age_list total_num 非空
                    String progress_key = item.getString("progress_key");
                    Preconditions.checkArgument(!StringUtils.isEmpty(progress_key) && !progress_key.trim().equals("null"),
                            "人群-年龄消费列表数组[" + i + "].progress_key 为空");
                    Integer total_num = item.getInteger("total_num");
                    Preconditions.checkArgument(null != total_num,
                            "人群-年龄消费列表数组[" + i + "].total_num 为空");
                    JSONArray ageList = item.getJSONArray("age_list");
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(ageList),
                            "人群-年龄消费列表数组[" + i + "].age_list数组 为空");

                    if (0 == i) {
                        Preconditions.checkArgument(progress_key.trim().equals("AWARE"),
                                "人群-年龄消费列表数组[" + i + "].progress_key不是 AWARE, progress_key: " + progress_key);
                    } else if (1 == i) {
                        Preconditions.checkArgument(progress_key.trim().equals("INTEREST"),
                                "人群-年龄消费列表数组[" + i + "].progress_key不是 INTEREST, progress_key: " + progress_key);
                    } else if (2 == i) {
                        Preconditions.checkArgument(progress_key.trim().equals("LOYALTY"),
                                "人群-年龄消费列表数组[" + i + "].progress_key不是 LOYALTY, progress_key: " + progress_key);
                    } else if (3 == i) {
                        Preconditions.checkArgument(progress_key.trim().equals("PURCHASE"),
                                "人群-年龄消费列表数组[" + i + "].progress_key不是 PURCHASE, progress_key: " + progress_key);
                    }
                    //total_num >=0
                    Preconditions.checkArgument(total_num >= 0,
                            "人群-年龄消费列表数组[" + i + "].total_num < 0, total_num: " + total_num);

                }

            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2544 /dashboard/crowd/real/progressRatio 人群实时分析");
    }

    private float checkCrowdAgeSexData(JSONObject data, String datakey, String msgKey) {
        float percent = 0;
        JSONObject dataValue = data.getJSONObject(datakey);
        Preconditions.checkArgument(null != dataValue,
                "人群-年龄趋势" + msgKey + "性别占比为空");
        Preconditions.checkArgument(null != dataValue.getLong("total_num"),
                "人群-年龄趋势" + msgKey + "总人数为空");
        JSONArray ageList = dataValue.getJSONArray("list");
        Preconditions.checkArgument(!CollectionUtils.isEmpty(ageList),
                "人群-年龄趋势" + msgKey + "年龄段占比数组为空");
        Preconditions.checkArgument(ageList.size() == 8 ,
                "人群-年龄趋势" + msgKey + "年龄段占比数组长度不为8");
        int size = ageList.size();
        for (int i=0; i<size; i++) {
            percent += ageList.getJSONObject(i).getFloat("percent");
            Preconditions.checkArgument(percent >= 0,
                    "人群-年龄趋势" + msgKey + "年龄段数组[" + i + "].percent < 0, percent: " + percent);
        }
        return percent;
    }

    @Test
    public  void crowdAgeSex() {
        //2545
        String requestUrl = DMP_HOST + "/dashboard/crowd/real/ageSex";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                /*校验返回**/
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());
                float percent = 0;
                percent += checkCrowdAgeSexData(data, "male", "男性");
                percent += checkCrowdAgeSexData(data, "female", "女性");
                //percent total check TODO
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2545 /dashboard/crowd/real/ageSex 人群实时分析");
    }


    @Test
    public  void crowRealLabel() {
        //2546
        String requestUrl = DMP_HOST + "/dashboard/crowd/real/label";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));

                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());
                /*校验人群信息**/
                JSONArray crowdList = data.getJSONArray("crowd_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(crowdList),
                        "人群-人群标签信息为空");
                Preconditions.checkArgument(crowdList.size() == 4,
                        "人群-人群标签信息长度不为4, size: " + crowdList.size());

                for (int i = 0; i < crowdList.size(); i++) {
                    JSONObject crowObj = crowdList.getJSONObject(i);
                    Preconditions.checkArgument(null != crowObj.getLong("crowd_id"),
                            "人群-人群标签数组[" + i + "].id 为空");
                    Preconditions.checkArgument(!StringUtils.isEmpty(crowObj.getString("crowd_name")),
                            "人群-人群标签数组[" + i + "].crowd_name 为空");
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(crowObj.getJSONArray("label_list")),
                            "人群-人群标签数组[" + i + "].label_list 为空");

                    String crowd_name = crowObj.getString("crowd_name").trim();
                    if (0 == i) {
                        Preconditions.checkArgument(crowd_name.equals("新青年"),
                                "人群-人群标签数组[" + i + "].crowd_name 不为新青年, crowd_name: " + crowd_name);
                    } else if (1 == i) {
                        Preconditions.checkArgument(crowd_name.equals("新中产"),
                                "人群-人群标签数组[" + i + "].crowd_name 不为新中产, crowd_name: " + crowd_name);
                    } else if (2 == i) {
                        Preconditions.checkArgument(crowd_name.equals("新家庭"),
                                "人群-人群标签数组[" + i + "].crowd_name 不新家庭, crowd_name: " + crowd_name);
                    } else if (3 == i) {
                        Preconditions.checkArgument(crowd_name.equals("其他"),
                                "人群-人群标签数组[" + i + "].crowd_name 不为其他, crowd_name: " + crowd_name);
                    }
                }
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2546 /dashboard/crowd/real/label 人群实时分析");
    }

    @Test
    public  void crowdRealMemberAnalysis() {
        //2547
        String requestUrl = DMP_HOST + "/dashboard/crowd/real/memberAnalysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));

                /*校验返回**/
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());
                JSONArray crowdList = data.getJSONArray("crowd_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(crowdList),
                        "人群-泛会员转化趋势-列表 为空");
                Preconditions.checkArgument(crowdList.size() >= 4 ,
                        "人群-泛会员转化趋势-列表数组长度不为4, size: " + crowdList.size());
                int size = crowdList.size();
                for (int i=0; i<size; i++) {
                    JSONObject item = crowdList.getJSONObject(i);
                    //member_new_num >= 0
                    int member_new_num = item.getInteger("member_new_num");
                    Preconditions.checkArgument(member_new_num >= 0 ,
                            "人群-泛会员转化趋势-列表数组[" + i + "].member_new_num < 0, member_new_num: " + member_new_num);
                    //total_num >= 0
                    int total_num = item.getInteger("total_num");
                    Preconditions.checkArgument(total_num >= 0 ,
                            "人群-泛会员转化趋势-列表数组[" + i + "].total_num < 0, total_num: " + total_num);
                    //0  <=inversion_rate <= 1.0
                    float inversion_rate = item.getFloat("inversion_rate");
                    Preconditions.checkArgument(inversion_rate >= 0  && inversion_rate <= 1,
                            "人群-泛会员转化趋势-列表数组[" + i + "].inversion_rate < 0, inversion_rate: " + total_num);
                    //crowd_id > 0
                    int crowd_id = item.getInteger("crowd_id");
                    Preconditions.checkArgument(total_num > 0 ,
                            "人群-泛会员转化趋势-列表数组[" + i + "].crowd_id <= 0, crowd_id: " + crowd_id);
                    //crowd_name 新青年、新中产、新家庭、其他
                    String crowd_name = item.getString("crowd_name");
                    Preconditions.checkArgument(!StringUtils.isEmpty(crowd_name) && !crowd_name.trim().equals("null"),
                            "人群-泛会员转化趋势-列表数组[" + i + "].crowd_name 为空");
                    if (0 == i) {
                        Preconditions.checkArgument(crowd_name.equals("新青年"),
                                "人群-泛会员转化趋势-列表数组[" + i + "].crowd_name 不为新青年, crowd_name: " + crowd_name);
                    } else if (1 == i) {
                        Preconditions.checkArgument(crowd_name.equals("新中产"),
                                "人群-泛会员转化趋势-列表数组[" + i + "].crowd_name 不为新中产, crowd_name: " + crowd_name);
                    } else if (2 == i) {
                        Preconditions.checkArgument(crowd_name.equals("新家庭"),
                                "人群-泛会员转化趋势-列表数组[" + i + "].crowd_name 不新家庭, crowd_name: " + crowd_name);
                    } else if (3 == i) {
                        Preconditions.checkArgument(crowd_name.equals("其他"),
                                "人群-泛会员转化趋势-列表数组[" + i + "].crowd_name 不为其他, crowd_name: " + crowd_name);
                    }
                }
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2547 /dashboard/crowd/real/memberAnalysis 人群实时分析");
    }


    /********************************店铺仪表盘******************************************/
    @Test
    public  void shopRealAnalysis() {
        //2539
        String requestUrl = DMP_HOST + "/dashboard/shop/real/analysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());

            /*校验返回**/
            JSONArray analysisList = data.getJSONArray("analysis_list");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(analysisList),
                    "店铺-趋势图返回为空");
            Preconditions.checkArgument(analysisList.size() == 4 ,
                    "店铺-趋势图返回长度不为4, size: " + analysisList.size());

            for (int i=0; i<analysisList.size(); i++) {
                JSONObject item = analysisList.getJSONObject(i);
                //name不为空, 且属于 零售、餐饮、亲子、娱乐 4大板块之一
                String name = item.getString("name");
                Preconditions.checkArgument(!StringUtils.isEmpty(name) && !name.contains("null"),
                        "店铺-趋势图数组[" + i + "]" + ".name 为空");
                Preconditions.checkArgument(name.trim().contains("零售")
                                || name.trim().contains("餐饮")
                                || name.trim().contains("亲子")
                                || name.trim().contains("娱乐"),
                        "店铺-趋势图数组[" + i + "]" + ".name 不是 零售、餐饮、亲子、娱乐, name: " + name);

                //shop_count >= 0
                int shop_count = item.getInteger("shop_count");
                Preconditions.checkArgument(shop_count >= 0,
                        "店铺-趋势图数组[" + i + "]" + ".shop_count < 0, shop_count: " + shop_count);

                //uv list size == 24
                JSONArray uvList = item.getJSONArray("uv_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(uvList),
                        "店铺-趋势图数组[" + i + "]" + ".uv_list 为空");

                Preconditions.checkArgument(uvList.size() == 24,
                        "店铺-趋势图数组[" + i + "]" + ".uv_list 长度不为24, size == " + uvList.size());

                List<String> uvArray = uvList.toJavaList(String.class);
                //0到当前时间的整点有数据, 且数据递增
                int lastValue = 0;
                int hour = Integer.parseInt(dt.getCurrentHour());
                for (int index=0; index<hour; index++) {
                    String vs = uvArray.get(index);
                    Preconditions.checkArgument(!StringUtils.isEmpty(vs) && !vs.contains("null"),
                            "店铺-趋势图数组[" + i + "]" + ".uv_list.[" + index + "] 为空");
                    int value = Integer.parseInt(vs);
                    if (0==index) {
                        lastValue = value;
                    }
                    Preconditions.checkArgument(value >= 0,
                            "店铺-趋势图数组[" + i + "]" + ".uv_list.[" + index + "] < 0 , value: " + value);
                    Preconditions.checkArgument(value>=lastValue,
                            "店铺-趋势图数组[" + i + "]" + ".uv_list.[" + index + "] 值小于上个小时的值, current value: " + value + ", last hour value: " + lastValue);

                    lastValue = value;
                }
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2539 /dashboard/shop/real/analysis 店铺实时分析");
    }


    @Test
    public  void shopRealStayAnalysis() {
        //2540
        String requestUrl = DMP_HOST + "/dashboard/shop/real/stayAnalysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());
                JSONArray analysisList = data.getJSONArray("analysis_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(analysisList),
                        "返回店铺信息为空");
                int size = analysisList.size();
                //size > 0
                Preconditions.checkArgument(size > 0,
                        "店铺-店铺信息数组size <= 0, size: " + size);
                for (int i=0; i<size; i++) {
                    JSONObject item = analysisList.getJSONObject(i);

                    //dp_name 不为空
                    String dp_name = item.getString("dp_name");
                    Preconditions.checkArgument(!StringUtils.isEmpty(dp_name) && !dp_name.contains("null"),
                            "店铺-店铺信息数组[" + i + "]" + ".dp_name 为空");

                    //format_name 不为空，且属于 零售、餐饮、亲子、娱乐 4大板块之一
                    String format_name = item.getString("format_name");
                    Preconditions.checkArgument(!StringUtils.isEmpty(format_name) && !format_name.contains("null"),
                            "店铺-店铺信息数组[" + i + "]" + ".format_name 为空");
                    Preconditions.checkArgument(format_name.trim().contains("零售")
                                    || format_name.trim().contains("餐饮")
                                    || format_name.trim().contains("亲子")
                                    || format_name.trim().contains("娱乐"),
                            "店铺-店铺信息数组[" + i + "]" + ".format_name 不是 零售、餐饮、亲子、娱乐, format_name: " + format_name);

                    //ave_time >= 0
                    float ave_time = item.getFloat("ave_time");
                    Preconditions.checkArgument(ave_time >= 0,
                            "店铺-店铺信息数组[" + i + "]" + ".ave_time < 0, ave_time: " + ave_time);

                    //total_uv >= 0
                    int total_uv = item.getInteger("total_uv");
                    Preconditions.checkArgument(total_uv >= 0,
                            "店铺-店铺信息数组[" + i + "]" + ".total_uv < 0, total_uv: " + total_uv);

                    //shop_count >= 0
                    float shop_count = item.getFloat("shop_count");
                    Preconditions.checkArgument(shop_count >= 0,
                            "店铺-店铺信息数组[" + i + "]" + ".shop_count < 0, shop_count: " + shop_count);

                }
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2540 /dashboard/shop/real/stayAnalysis 人群实时分析");
    }

    @Test
    public  void shopRealMemberAnalysis() {
        //2542
        String requestUrl = DMP_HOST + "/dashboard/shop/real/memberAnalysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());
                JSONArray list = data.getJSONArray("list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(list),
                        "店铺-平面信息为空");
                int size = list.size();
                Preconditions.checkArgument(size == 7,
                        "店铺-平面信息数组长度不为7, size: " + size);
                for (int i=0; i<size; i++) {
                    String floor = list.getJSONObject(i).getString("floor_no");
                    Preconditions.checkArgument(!StringUtils.isEmpty(floor) && !floor.trim().equals("null"),
                            "店铺-平面信息数组[" + i + "]" + ".floor_no 为空");
                    if (0 == i) {
                        Preconditions.checkArgument(floor.trim().equals("L3"),
                                "店铺-平面信息数组[" + i + "]" + ".floor_no 不为最高层L3, floor_no: " + floor);
                    } else if (1 == i) {
                        Preconditions.checkArgument(floor.trim().equals("L2"),
                                "店铺-平面信息数组[" + i + "]" + ".floor_no 不为L2, floor_no: " + floor);

                    } else if (2 == i) {
                        Preconditions.checkArgument(floor.trim().equals("L1"),
                                "店铺-平面信息数组[" + i + "]" + ".floor_no 不为L1, floor_no: " + floor);
                        JSONArray subList = list.getJSONObject(i).getJSONArray("dp_list");
                        Preconditions.checkArgument(!CollectionUtils.isEmpty(subList),
                                "店铺-平面信息数组[" + i + "]" + ".dp_list 为空，即L1层店铺解析为空");
                        for (int j=0; j<subList.size(); j++) {
                            JSONObject subItem = subList.getJSONObject(j);
                            //check dp_name not null
                            String subName = subItem.getString("dp_name");
                            Preconditions.checkArgument(!StringUtils.isEmpty(subName) && !subName.trim().equals("null"),
                                    "店铺-平面信息数组[" + i + "]" + ".dp_list[" + j + "].dp_name 为空");

                            //member_new_num >=0
                            int member_new_num = subItem.getInteger("member_new_num");
                            Preconditions.checkArgument(member_new_num >=0,
                                    "店铺-平面信息数组[" + i + "]" + ".dp_list[" + j + "].member_new_num < 0, member_new_num: " + member_new_num);

                            //last_member_new_num >=0
                            int last_member_new_num = subItem.getInteger("last_member_new_num");
                            Preconditions.checkArgument(last_member_new_num >=0,
                                    "店铺-平面信息数组[" + i + "]" + ".dp_list[" + j + "].last_member_new_num < 0, last_member_new_num: " + last_member_new_num);

                            //member_count >= 0
                            int member_count = subItem.getInteger("member_count");
                            Preconditions.checkArgument(member_count >=0,
                                    "店铺-平面信息数组[" + i + "]" + ".dp_list[" + j + "].member_count < 0, member_count: " + member_count);

                            //mom_rise not null
                            String mom_rise = subItem.getString("mom_rise");
                            Preconditions.checkArgument(!StringUtils.isEmpty(mom_rise) && !mom_rise.trim().equals("null"),
                                    "店铺-平面信息数组[" + i + "]" + ".dp_list[" + j + "].mom_rise 为空");

                            //member_inversion_rate >= 0
                            float member_inversion_rate = subItem.getFloat("member_inversion_rate");
                            Preconditions.checkArgument(member_inversion_rate >=0,
                                    "店铺-平面信息数组[" + i + "]" + ".dp_list[" + j + "].member_inversion_rate < 0, member_inversion_rate: " + member_inversion_rate);

                        }
                    } else if (3 == i) {
                        Preconditions.checkArgument(floor.trim().equals("B1"),
                                "店铺-平面信息数组[" + i + "]" + ".floor_no 不为B1, floor_no: " + floor);

                    } else if (4 == i) {
                        Preconditions.checkArgument(floor.trim().equals("B2"),
                                "店铺-平面信息数组[" + i + "]" + ".floor_no 不为B2, floor_no: " + floor);

                    } else if (5 == i) {
                        Preconditions.checkArgument(floor.trim().equals("B3"),
                                "店铺-平面信息数组[" + i + "]" + ".floor_no 不为B3, floor_no: " + floor);

                    } else if (6 == i) {
                        Preconditions.checkArgument(floor.trim().equals("B4"),
                                "店铺-平面信息数组[" + i + "]" + ".floor_no 不为B4, floor_no: " + floor);

                    }
                }
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2542 /dashboard/shop/real/memberAnalysis 人群实时分析");
    }


    /***********************************************业态仪表盘***********************/
    @Test
    public  void formatRealAnalysis() {
        //2533
        String requestUrl = DMP_HOST + "/dashboard/format/real/analysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());

            /*校验返回**/
            JSONArray analysisList = data.getJSONArray("analysis_list");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(analysisList),
                    "业态-趋势数组为空");

            Preconditions.checkArgument(analysisList.size() == 4,
                    "业态-趋势数组长度不等于4, size: " + analysisList.size());

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

        saveData(aCase, caseName, "2533 /dashboard/format/real/analysis 业态实时分析");
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
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());

                JSONArray coincidenceList = data.getJSONArray("coincidence_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(coincidenceList),
                        "业态解析数组为空");

                Preconditions.checkArgument(coincidenceList.size() == 4,
                        "业态解析数组长度不为4, size: " + coincidenceList.size());
                for (int i=0;i<coincidenceList.size(); i++) {
                    String name = coincidenceList.getJSONObject(i).getString("name");
                    Preconditions.checkArgument(!StringUtils.isEmpty(name) && !name.trim().equals("null"),
                            "业态解析数组[" + i + "]" + ".name 为空");
                    JSONArray subList = coincidenceList.getJSONObject(i).getJSONArray("list");
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(subList),
                            "业态解析数组[" + i + "]" + ".list 为空");
                    Preconditions.checkArgument(subList.size() == coincidenceList.size()-1,
                            "业态解析数组[" + i + "]" + ".list 大小不为3, size: " + subList.size());
                    //校验顺序
                    //餐饮
                    //零售
                    //亲子
                    //娱乐
                    if (0 == i) {
                        Preconditions.checkArgument(name.trim().equals("餐饮"),
                                "业态解析数组[" + i + "]" + ".name 不为餐饮");
                        for (int j=0; j<subList.size(); j++) {
                            String subName = subList.getJSONObject(j).getString("name");
                            Preconditions.checkArgument(!StringUtils.isEmpty(subName) && !subName.trim().equals("null"),
                                    "业态解析数组[" + i + "]" + ".list[" + j + "].name 为空");
                            if (0 == j) {
                                Preconditions.checkArgument(subName.trim().equals("零售"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为零售");
                            } else if (1 == j) {
                                Preconditions.checkArgument(subName.trim().equals("亲子"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为亲子");
                            } else if (2 == j) {
                                Preconditions.checkArgument(subName.trim().equals("娱乐"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为娱乐");
                            }
                            float coincidence = subList.getJSONObject(j).getFloat("coincidence");
                            Preconditions.checkArgument(coincidence >= 0,
                                    "业态解析数组[" + i + "]" + ".list[" + j + "].coincidence<0, coincidence: " + coincidence);
                        }

                    } else if (1 == i) {
                        Preconditions.checkArgument(name.trim().equals("零售"),
                                "业态解析数组[" + i + "]" + ".name 不为零售");
                        for (int j=0; j<subList.size(); j++) {
                            String subName = subList.getJSONObject(j).getString("name");
                            Preconditions.checkArgument(!StringUtils.isEmpty(subName) && !subName.trim().equals("null"),
                                    "业态解析数组[" + i + "]" + ".list[" + j + "].name 为空");
                            if (0 == j) {
                                Preconditions.checkArgument(subName.trim().equals("餐饮"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为餐饮");
                            } else if (1 == j) {
                                Preconditions.checkArgument(subName.trim().equals("亲子"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为亲子");
                            } else if (2 == j) {
                                Preconditions.checkArgument(subName.trim().equals("娱乐"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为娱乐");
                            }
                            float coincidence = subList.getJSONObject(j).getFloat("coincidence");
                            Preconditions.checkArgument(coincidence >= 0,
                                    "业态解析数组[" + i + "]" + ".list[" + j + "].coincidence<0, coincidence: " + coincidence);
                        }

                    } else if (2 == i) {
                        Preconditions.checkArgument(name.trim().equals("亲子"),
                                "业态解析数组[" + i + "]" + ".name 不为亲子");
                        for (int j=0; j<subList.size(); j++) {
                            String subName = subList.getJSONObject(j).getString("name");
                            Preconditions.checkArgument(!StringUtils.isEmpty(subName) && !subName.trim().equals("null"),
                                    "业态解析数组[" + i + "]" + ".list[" + j + "].name 为空");
                            if (0 == j) {
                                Preconditions.checkArgument(subName.trim().equals("餐饮"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为餐饮");
                            } else if (1 == j) {
                                Preconditions.checkArgument(subName.trim().equals("零售"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为零售");
                            } else if (2 == j) {
                                Preconditions.checkArgument(subName.trim().equals("娱乐"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为娱乐");
                            }
                            float coincidence = subList.getJSONObject(j).getFloat("coincidence");
                            Preconditions.checkArgument(coincidence >= 0,
                                    "业态解析数组[" + i + "]" + ".list[" + j + "].coincidence<0, coincidence: " + coincidence);
                        }

                    } else if (3 == i) {
                        Preconditions.checkArgument(name.trim().equals("娱乐"),
                                "业态解析数组[" + i + "]" + ".name 不为娱乐");
                        for (int j=0; j<subList.size(); j++) {
                            String subName = subList.getJSONObject(j).getString("name");
                            Preconditions.checkArgument(!StringUtils.isEmpty(subName) && !subName.trim().equals("null"),
                                    "业态解析数组[" + i + "]" + ".list[" + j + "].name 为空");
                            if (0 == j) {
                                Preconditions.checkArgument(subName.trim().equals("餐饮"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为餐饮");
                            } else if (1 == j) {
                                Preconditions.checkArgument(subName.trim().equals("零售"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为零售");
                            } else if (2 == j) {
                                Preconditions.checkArgument(subName.trim().equals("亲子"),
                                        "业态解析数组[" + i + "]" + ".list[" + j + "].name 不为亲子");
                            }
                            float coincidence = subList.getJSONObject(j).getFloat("coincidence");
                            Preconditions.checkArgument(coincidence >= 0,
                                    "业态解析数组[" + i + "]" + ".list[" + j + "].coincidence<0, coincidence: " + coincidence);
                        }
                    }



                }

                JSONArray layoutList = data.getJSONArray("layout_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(layoutList),
                        "平面业态信息为空");
                int layoutSize = layoutList.size();
                Preconditions.checkArgument(layoutSize == 7,
                        "平面业态信息数组长度不为7, size: " + layoutSize);
                for (int i=0; i<layoutSize; i++) {
                    String floor = layoutList.getJSONObject(i).getString("floor_no");
                    Preconditions.checkArgument(!StringUtils.isEmpty(floor) && !floor.trim().equals("null"),
                            "平面业态信息数组[" + i + "]" + ".floor_no 为空");
                    if (0 == i) {
                        Preconditions.checkArgument(floor.trim().equals("L3"),
                                "平面业态信息数组[" + i + "]" + ".floor_no 不为最高层L3, floor_no: " + floor);
                    } else if (1 == i) {
                        Preconditions.checkArgument(floor.trim().equals("L2"),
                                "平面业态信息数组[" + i + "]" + ".floor_no 不为L2, floor_no: " + floor);

                    } else if (2 == i) {
                        Preconditions.checkArgument(floor.trim().equals("L1"),
                                "平面业态信息数组[" + i + "]" + ".floor_no 不为L1, floor_no: " + floor);
                        JSONArray subList = layoutList.getJSONObject(i).getJSONArray("format_list");
                        Preconditions.checkArgument(!CollectionUtils.isEmpty(subList),
                                "平面业态信息数组[" + i + "]" + ".format_list 为空，即L1层业态解析为空");
                        for (int j=0; j<subList.size(); j++) {
                            //check name, format_size, format_name
                            String subName = subList.getJSONObject(j).getString("name");
                            Preconditions.checkArgument(!StringUtils.isEmpty(subName) && !subName.trim().equals("null"),
                                    "平面业态信息数组[" + i + "]" + ".format_list[" + j + "].name 为空");
                            String formatName = subList.getJSONObject(j).getString("format_name");
                            Preconditions.checkArgument(!StringUtils.isEmpty(formatName) && !formatName.trim().equals("null"),
                                    "平面业态信息数组[" + i + "]" + ".format_list[" + j + "].format_name 为空");
                            int formatSize = subList.getJSONObject(j).getInteger("format_size");
                            Preconditions.checkArgument(formatSize > 0,
                                    "平面业态信息数组[" + i + "]" + ".format_list[" + j + "].format_size<=0, format_size: " + formatSize);
                        }
                    } else if (3 == i) {
                        Preconditions.checkArgument(floor.trim().equals("B1"),
                                "平面业态信息数组[" + i + "]" + ".floor_no 不为B1, floor_no: " + floor);

                    } else if (4 == i) {
                        Preconditions.checkArgument(floor.trim().equals("B2"),
                                "平面业态信息数组[" + i + "]" + ".floor_no 不为B2, floor_no: " + floor);

                    } else if (5 == i) {
                        Preconditions.checkArgument(floor.trim().equals("B3"),
                                "平面业态信息数组[" + i + "]" + ".floor_no 不为B3, floor_no: " + floor);

                    } else if (6 == i) {
                        Preconditions.checkArgument(floor.trim().equals("B4"),
                                "平面业态信息数组[" + i + "]" + ".floor_no 不为B4, floor_no: " + floor);

                    }
                }

            }
        } catch (Exception e) {
            failReason = e.toString();
        }



        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2534 /dashboard/format/real/coincidence 业态实时分析");
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
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());
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
        //2536
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
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());
                JSONArray histogramList = data.getJSONArray("histogram_list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(histogramList),
                        "主业态客流柱状图返回为空");
                Preconditions.checkArgument( histogramList.size() == 4,
                        "主业态客流柱状图返回长度小于4");
                int hlistSize = histogramList.size();
                for (int i=0; i<hlistSize; i++) {
                    JSONObject item = histogramList.getJSONObject(i);
                    //check num >0 and name not null
                    String formatName = item.getString("format_name");
                    Preconditions.checkArgument(!StringUtils.isEmpty(formatName) && !formatName.trim().equals("null"),
                            "主业态客流柱状图数组[" + i + "]" + ".format_name 为空");
                    int male_visitor_num = item.getInteger("male_visitor_num");
                    Preconditions.checkArgument(male_visitor_num >= 0,
                            "主业态客流柱状图数组[" + i + "]" + ".male_visitor_num < 0");
                    int female_member_num = item.getInteger("female_member_num");
                    Preconditions.checkArgument(female_member_num >= 0,
                            "主业态客流柱状图数组[" + i + "]" + ".female_member_num < 0");
                    int female_total_num = item.getInteger("female_total_num");
                    Preconditions.checkArgument(female_total_num >= 0,
                            "主业态客流柱状图数组[" + i + "]" + ".female_total_num < 0");
                    int male_member_num = item.getInteger("male_member_num");
                    Preconditions.checkArgument(male_member_num >= 0,
                            "主业态客流柱状图数组[" + i + "]" + ".male_member_num < 0");
                    int female_visitor_num = item.getInteger("female_visitor_num");
                    Preconditions.checkArgument(female_visitor_num >= 0,
                            "主业态客流柱状图数组[" + i + "]" + ".female_visitor_num < 0");
                    int male_total_num = item.getInteger("male_total_num");
                    Preconditions.checkArgument(male_total_num >= 0,
                            "主业态客流柱状图数组[" + i + "]" + ".male_total_num < 0");

                }
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2536 /dashboard/format/real/histogram 业态实时分析");
    }

    @Test
    public  void formatRealCrowdRelation() {
        //2537
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
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());
                JSONArray crowdList = data.getJSONArray("crowd_list");
                Preconditions.checkArgument( !CollectionUtils.isEmpty(crowdList) ,
                        "主业态人群列表返回为空");
                Preconditions.checkArgument( crowdList.size() == 4,
                        "主业态人群列表返回长度不等于4, size: " + crowdList.size());
                int size = crowdList.size();
                for (int i=0; i<size; i++) {
                    JSONObject item = crowdList.getJSONObject(i);
                    //crowd_name not null, and belong to 新青年、新中产、新家庭、其他
                    String crowd_name = item.getString("crowd_name");
                    Preconditions.checkArgument(!StringUtils.isEmpty(crowd_name) && !crowd_name.trim().equals("null"),
                            "主业态人群数组[" + i + "]" + ".crowd_name 为空");
                    Preconditions.checkArgument(crowd_name.trim().contains("新青年")
                            || crowd_name.trim().contains("新中产")
                            || crowd_name.trim().contains("新家庭")
                            || crowd_name.trim().contains("其他"), "" +
                            "主业态人群数组[" + i + "]" + ".crowd_name 不属于 新青年、新中产、新家庭、其他, crowd_name: " + crowd_name);

                    //crowd_id > 0
                    int crowd_id = item.getInteger("crowd_id");
                    Preconditions.checkArgument(crowd_id > 0, "" +
                            "主业态人群数组[" + i + "]" + ".crowd_id <= 0, crowd_id: " + crowd_id);
                }


                JSONArray relations = data.getJSONArray("relations");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(relations) ,
                        "主业态人群关系列表返回为空");
                Preconditions.checkArgument( relations.size() == 4,
                        "主业态人群关系列表返回长度不等于4, size: " + relations.size());
                size = relations.size();
                for (int i=0; i<size; i++) {
                    JSONObject item = relations.getJSONObject(i);
                    //format_name 非空， crowd_num_list非空
                    String format_name = item.getString("format_name");
                    Preconditions.checkArgument(!StringUtils.isEmpty(format_name) && !format_name.trim().equals("null"),
                            "主业态人群关系数组[" + i + "]" + ".format_name 为空");
                    Preconditions.checkArgument(format_name.trim().contains("零售")
                                    || format_name.trim().contains("餐饮")
                                    || format_name.trim().contains("亲子")
                                    || format_name.trim().contains("娱乐"),
                            "主业态人群关系数组[" + i + "]" + ".format_name 不是 零售、餐饮、亲子、娱乐, format_name: " + format_name);
                    JSONArray crowd_num_list = item.getJSONArray("crowd_num_list");
                    Preconditions.checkArgument(!CollectionUtils.isEmpty(crowd_num_list), "" +
                            "主业态人群关系数组[" + i + "]" + ".crowd_num_list 为空");
                    Preconditions.checkArgument(crowd_num_list.size() == 4, "" +
                            "主业态人群关系数组[" + i + "]" + ".crowd_num_list.size() 不等于 4, size: " + crowd_num_list.size());
                    int subSize = crowd_num_list.size();
                    for (int j=0; j<subSize; j++) {
                        //num >= 0, crowd_id >0
                        JSONObject subItem = crowd_num_list.getJSONObject(i);
                        int crowd_id = subItem.getInteger("crowd_id");
                        Preconditions.checkArgument(crowd_id > 0, "" +
                                "主业态人群关系数组[" + i + "]" + ".crowd_num_list.[" + j + "].crowd_id <= 0, crowd_id: " + crowd_id);
                        int num = subItem.getInteger("num");
                        Preconditions.checkArgument(num >= 0, "" +
                                "主业态人群关系数组[" + i + "]" + ".crowd_num_list.[" + j + "].num < 0, num: " + num);

                    }
                }

            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2537 /dashboard/format/real/crowdRelation 业态实时分析");
    }

    @Test
    public  void formatRealMemberAnalysis() {
        //2538
        String requestUrl = DMP_HOST + "/dashboard/format/real/memberAnalysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());
                JSONArray list = data.getJSONArray("list");
                Preconditions.checkArgument(!CollectionUtils.isEmpty(list),
                        "主业态泛会员转化率列表返回为空");

                Preconditions.checkArgument(list.size() == 4,
                        "主业态泛会员转化率列表返回长度不为4, size: " + list.size());
                int size = list.size();
                for (int i=0; i<size; i++) {
                    JSONObject item = list.getJSONObject(i);

                    //inversion_rate>=0
                    float inversion_rate = item.getFloat("inversion_rate");
                    Preconditions.checkArgument(inversion_rate >= 0, "" +
                            "主业态泛会员转化率数组[" + i + "]" + ".inversion_rate < 0, inversion_rate: " + inversion_rate);

                    //today_new_member_num>=0
                    int today_new_member_num = item.getInteger("today_new_member_num");
                    Preconditions.checkArgument(today_new_member_num >= 0, "" +
                            "主业态泛会员转化率数组[" + i + "]" + ".today_new_member_num < 0, today_new_member_num: " + today_new_member_num);

                    //time_total_member_num>=0
                    int time_total_member_num = item.getInteger("time_total_member_num");
                    Preconditions.checkArgument(time_total_member_num >= 0, "" +
                            "主业态泛会员转化率数组[" + i + "]" + ".time_total_member_num < 0, time_total_member_num: " + time_total_member_num);

                    //format_name belongs to 餐饮、零售、亲子、娱乐
                    String format_name = item.getString("format_name");
                    Preconditions.checkArgument(!StringUtils.isEmpty(format_name) && !format_name.trim().equals("null"),
                            "主业态泛会员转化率[" + i + "]" + ".format_name 为空");
                    Preconditions.checkArgument(format_name.trim().contains("餐饮")
                            || format_name.trim().contains("零售")
                            || format_name.trim().contains("亲子")
                            || format_name.trim().contains("娱乐"), "" +
                            "主业态泛会员转化率数组[" + i + "]" + ".format_name 不属于 餐饮、零售、亲子、娱乐, format_name: " + format_name);


                }
            }
        } catch (Exception e) {
            failReason = e.toString();
        }

        Case aCase = new Case();
        String caseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        saveData(aCase, caseName, "2538 /dashboard/format/real/memberAnalysis 业态实时分析");
    }

    /************************************************客流仪表盘**************************************************/

    @Test
    public  void customerRealStatistic() {
        //2527
        String requestUrl = DMP_HOST + "/dashboard/customer/real/statistics";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());

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

        saveData(aCase, caseName, "2527 /dashboard/customer/real/statistics 客流实时分析");
    }


    @Test
    public  void customerRealEntrance(){
        //2528
        String requestUrl = DMP_HOST + "/dashboard/customer/real/entrance";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());

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
            Preconditions.checkArgument(uvPercent > 0.5 && uvPercent < 4,
                    "客流-地面客流uv_percent_num all sum < 0.5 or >2, sum == " + uvPercent);
            float pvPercent = entranceList.getJSONObject(0).getFloat("pv_percent_num");
            Preconditions.checkArgument(pvPercent > 0,
                    "客流-地面客流pv_percent_num=" + pvPercent);
            pvPercent = 0;
            int pvLast = 0;
            nameLast = "";
            for (int i=0; i<entranceList.size(); i++) {
                pvPercent += entranceList.getJSONObject(i).getFloat("pv_percent_num");

                //pv降序检查
                pv = entranceList.getJSONObject(i).getInteger("pv");
                String name = entranceList.getJSONObject(i).getString("name");
                Preconditions.checkArgument(!StringUtils.isEmpty(name),
                        "客流-地面客流entrance_list数组中name为空");
                if (0 == i) {
                    pvLast = pv;
                    nameLast = name;
                }
//                uv 降序，pv未必降序
//                Preconditions.checkArgument(pv <= pvLast,
//                        "客流-地面客流entrance_list数组中pv数据顺序异常, " + nameLast + "pv: " + pvLast + ", " + name + "pv: " + pv);
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

                //pv降序检查
                pv = entranceList.getJSONObject(i).getInteger("pv");
                String name = entranceList.getJSONObject(i).getString("name");
                Preconditions.checkArgument(!StringUtils.isEmpty(name),
                        "客流-地下客流entrance_list数组中name为空");
                if (0 == i) {
                    pvLast = pv;
                    nameLast = name;
                }
//                uv 降序，pv未必降序
//                Preconditions.checkArgument(pv <= pvLast,
//                        "客流-地下客流entrance_list数组中pv数据顺序异常, " + nameLast + "pv: " + pvLast + ", " + name + "pv: " + pv);
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

        saveData(aCase, caseName, "2528 /dashboard/customer/real/entrance 客流实时分析");
    }


    @Test
    public  void customerRealFloor(){
        //2529
        String requestUrl = DMP_HOST + "/dashboard/customer/real/floor";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
            JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());

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

        saveData(aCase, caseName, "2529 /dashboard/customer/real/floor 客流实时分析");

    }

    @Test
    public  void customerRealHistogram(){
        //2530
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
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());
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

        saveData(aCase, caseName, "2530 /dashboard/customer/real/histogram 客流实时分析");
    }


    @Test
    public  void customerRealRegionTrend(){
        //2531
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
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());
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

        saveData(aCase, caseName, "2531 /dashboard/customer/real/regionTrend 客流实时分析");
    }


    @Test
    public  void customerRealMemberAnalysis(){
        //2532
        String requestUrl =  DMP_HOST + "/dashboard/customer/real/memberAnalysis";

        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("subject_id", SUBJECT_ID);
            TimeDimensionEnum[] values = TimeDimensionEnum.values();
            for (TimeDimensionEnum value : values) {
                requestJson.put("time_dimension", value.name());
                HttpHelper.Result result = HttpHelper.post(getHeader(), requestUrl, JSON.toJSONString(requestJson));
                JSONObject data = checkRspCode(result, requestUrl, requestJson.toJSONString());

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

        saveData(aCase, caseName, "2532 /dashboard/customer/real/memberAnalysis 客流实时分析");
    }

    /*********** 公共方法 *********/
    public JSONObject checkRspCode(HttpHelper.Result result, String url, String requestPara) {
        response = result.getContent();
        if (!result.isSuccess()) {
            throw new RuntimeException("result code is not 200, code: " + result.getStatusCode() + " \nurl: " + url
                    + " \nrequest para: " + requestPara
                    + " \nresponse: " + response);
        }
        JSONObject rspJson = JSON.parseObject(result.getContent());
        if (!StatusCode.SUCCESS.getCode().equals(rspJson.getInteger("code"))) {
            log.error("",JSON.toJSONString(rspJson));
            throw new RuntimeException("result json code is not 1000 "+ " \nurl: " + url
                    + " \nrequest para: " + requestPara
                    + " \nresponse: " + response);
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

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
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
