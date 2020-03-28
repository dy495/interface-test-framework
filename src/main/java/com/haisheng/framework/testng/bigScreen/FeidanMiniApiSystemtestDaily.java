package com.haisheng.framework.testng.bigScreen;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.model.bean.ReportTime;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.DingWebhook;
import com.haisheng.framework.util.*;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @author : lvxueqing
 * @date :  2020/03/27  14:55
 */

public class FeidanMiniApiSystemtestDaily {
    //    -------------------------------------------------数据一致性验证-------------------------------------------------------------
    Feidan feidan = new Feidan();
    StringUtil stringUtil = new StringUtil();

    /**
     * 获取登录信息 如果上述初始化方法（initHttpConfig）使用的authorization 过期，请先调用此方法获取
     *
     * @ 异常
     */
    @BeforeClass
    public void login() {
        qaDbUtil.openConnection();
        qaDbUtil.openConnectionRdDaily();
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        initHttpConfig();
        String path = "/risk-login";
        String loginUrl = getIpPort() + path;
        String json = "{\"username\":\"yuexiu@test.com\",\"passwd\":\"f5b3e737510f31b88eb2d4b5d0cd2fb4\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            this.authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization: {}", this.authorization);
        } catch (Exception e) {
            aCase.setFailReason("http post 调用异常，url = " + loginUrl + "\n" + e);
            logger.error(aCase.getFailReason());
            logger.error(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        saveData(aCase, ciCaseName, caseName, "登录获取authentication");
    }

    @AfterClass
    public void clean() {
        qaDbUtil.closeConnection();
        qaDbUtil.closeConnectionRdDaily();
        dingPushFinal();
    }

    @BeforeMethod
    public void initialVars() {
        failReason = "";
        response = "";
        aCase = new Case();
    }

    @Test
    public void testShopList() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {
            String path = "/risk/shop/list";
            String json = "{}";
            String checkColumnName = "list";
            httpPostWithCheckCode(path, json, checkColumnName);

        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);

        } finally {
            saveData(aCase, ciCaseName, caseName, "校验shop");
        }

    }


//---------------- 人脸搜索页面 start ---------------------

    /**
     * V3.0人脸搜索页面-上传jpg人脸图片
     **/
    @Test
    public void FaceSearch_jpg() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/李婷婷.jpg";
            System.out.println(imageUpload(path).getJSONObject("data"));
            JSONObject response = imageUpload(path).getJSONObject("data");
            String face_url_tmp = response.getString("face_url_tmp");
            String face = faceTraces(face_url_tmp);
            JSONObject trace = JSON.parseObject(face);
            JSONArray list = trace.getJSONObject("data").getJSONArray("list");

            if (list.size() == 0) {
                throw new Exception("搜索结果为空");
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：人脸搜索页面，上传PNG格式人脸图片\n");
        }
    }


    /**
     * V3.0人脸搜索页面-上传PNG猫脸图片
     **/
    @Test
    public void FaceSearch_cat() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        try {
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/猫.png";
            JSONObject response = imageUpload(path).getJSONObject("data");
            String face_url_tmp = response.getString("face_url_tmp");
            String face = faceTraces(face_url_tmp);
            JSONObject trace = JSON.parseObject(face);
            String code = trace.getString("code");
            String message = trace.getString("message");
            Preconditions.checkArgument(code.equals("1001"), "状态码不正确，期待1001，实际" + code);
            Preconditions.checkArgument(message.equals("人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片"), "未提示：人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：人脸搜索页面，上传PNG非人脸图片提示人脸图片不符合要求\n");
        }
    }

    /**
     * V3.0人脸搜索页面-上传txt
     **/
    @Test
    public void FaceSearch_txt() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        try {
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/人脸搜索.txt";
            JSONObject response = imageUpload(path);
            System.out.println(response);
            String message = response.getString("message");
            int code = response.getInteger("code");
            Preconditions.checkArgument(code == 1001, "状态码不正确，期待1001，实际" + code);
            Preconditions.checkArgument(message.equals("请上传png/jpg格式的图片"), "未提示：请上传png/jpg格式的图片");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：人脸搜索页面，上传txt文件\n");
        }
    }

    /**
     * V3.0人脸搜索页面-上传分辨率较低png
     **/
    @Test
    public void FaceSearch_lowQuality() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        try {
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/分辨率较低.png";
            JSONObject response = imageUpload(path).getJSONObject("data");
            String face_url_tmp = response.getString("face_url_tmp");
            String face = faceTraces(face_url_tmp);
            JSONObject trace = JSON.parseObject(face);
            System.out.println(trace);
            String code = trace.getString("code");
            String message = trace.getString("message");
            Preconditions.checkArgument(code.equals("1001"), "状态码不正确，期待1001，实际" + code);
            Preconditions.checkArgument(message.equals("人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片"), "未提示：人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：人脸搜索页面，上传分辨率较低png\n");
        }
    }

    /**
     * V3.0人脸搜索页面-上传风景图png
     **/
    @Test
    public void FaceSearch_view() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        try {
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/风景.png";
            JSONObject response = imageUpload(path).getJSONObject("data");
            System.out.println(response);
            String face_url_tmp = response.getString("face_url_tmp");
            String face = faceTraces(face_url_tmp);
            JSONObject trace = JSON.parseObject(face);
            System.out.println(trace);
            String code = trace.getString("code");
            String message = trace.getString("message");
            Preconditions.checkArgument(code.equals("1001"), "状态码不正确，期待1001，实际" + code);
            Preconditions.checkArgument(message.equals("人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片"), "未提示：人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：人脸搜索页面，上传PNG风景图\n");
        }
    }

    /**
     * V3.0人脸搜索页面-上传单人戴口罩png
     **/
    @Test
    public void FaceSearch_personwithmask() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/单人遮挡.png";
            JSONObject response = imageUpload(path).getJSONObject("data");
            System.out.println(response);
            String face_url_tmp = response.getString("face_url_tmp");
            String face = faceTraces(face_url_tmp);
            JSONObject trace = JSON.parseObject(face);
            System.out.println(trace);
            int code = trace.getInteger("code");
            JSONArray list = trace.getJSONObject("data").getJSONArray("list");
            Preconditions.checkArgument(code == 1000, "状态码不正确，期待1000，实际" + code); //判断状态码是否成功
            if (list.size() == 0) {
                String message = trace.getString("message");
                Preconditions.checkArgument(message.equals(null), "上传成功不应有提示语"); //搜索结果可能为空，为空时有message=""

            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：人脸搜索页面，上传单人戴口罩png\n");
        }
    }

    /**
     * V3.0人脸搜索页面-上传90度旋转
     **/
    @Test
    public void FaceSearch_Rotate90() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;
        try {
            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/90度旋转.jpg";
            JSONObject response = imageUpload(path).getJSONObject("data");
            String face_url_tmp = response.getString("face_url_tmp");
            String face = faceTraces(face_url_tmp);
            JSONObject trace = JSON.parseObject(face);
            System.out.println(trace);
            String code = trace.getString("code");
            String message = trace.getString("message");
            Preconditions.checkArgument(code.equals("1001"), "状态码不正确，期待1001，实际" + code);
            Preconditions.checkArgument(message.equals("人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片"), "未提示：人脸图片不符合要求(1.正脸 2.光照均匀 3.人脸大小128x128 4.格式为JPG/PNG),请更换图片");

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：人脸搜索页面，上传90度旋转\n");
        }
    }

    /**
     * V3.0人脸搜索页面-上传多人不遮挡
     **/
    @Test
    public void FaceSearch_peoplenotwithmask() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/多张人脸不遮挡.png";
            JSONObject response = imageUpload(path).getJSONObject("data");
            System.out.println(response);
            String face_url_tmp = response.getString("face_url_tmp");
            String face = faceTraces(face_url_tmp);
            JSONObject trace = JSON.parseObject(face);
            System.out.println(trace);
            int code = trace.getInteger("code");
            JSONArray list = trace.getJSONObject("data").getJSONArray("list");
            Preconditions.checkArgument(code == 1000, "状态码不正确，期待1000，实际" + code); //判断状态码是否成功
            if (list.size() == 0) {
                String message = trace.getString("message");
                Preconditions.checkArgument(message.equals(null), "上传成功不应有提示语"); //搜索结果可能为空，为空时有message=""

            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：人脸搜索页面，上传多人无遮挡png\n");
        }
    }

    /**
     * V3.0人脸搜索页面-上传多人仅一人不遮挡
     **/
    @Test
    public void FaceSearch_onlyonenomask() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseName = ciCaseName;

        try {

            String path = "src/main/java/com/haisheng/framework/testng/bigScreen/feidanImages/多人脸仅一位不遮挡.png";
            JSONObject response = imageUpload(path).getJSONObject("data");
            System.out.println(response);
            String face_url_tmp = response.getString("face_url_tmp");
            String face = faceTraces(face_url_tmp);
            JSONObject trace = JSON.parseObject(face);
            System.out.println(trace);
            int code = trace.getInteger("code");
            JSONArray list = trace.getJSONObject("data").getJSONArray("list");
            Preconditions.checkArgument(code == 1000, "状态码不正确，期待1000，实际" + code); //判断状态码是否成功
            if (list.size() == 0) {
                String message = trace.getString("message");
                Preconditions.checkArgument(message.equals(null), "上传成功不应有提示语"); //搜索结果可能为空，为空时有message=""

            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：人脸搜索页面，上传多人无遮挡png\n");
        }
    }


    //---------------- 人脸搜索页面 end ---------------------



//---------------- 活动页面 start -------------------------

    /**
     * 活动页面-根据活动类型-品牌活动进行搜索
     */
    @Test
    public void activity_BRAND() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        try {
            int total = activityList("","BRAND","",1,1).getInteger("total");//总活动数
            int a = 0;
            if (total > 0) {
                if (total > 50) {
                    if (total % 50 == 0) {
                        a = total / 50;
                    } else {
                        a = (int) Math.ceil(total / 50) + 1;
                    }
                    for (int i = 1; i <= a; i++) {
                        JSONArray list = activityList("", "BRAND", "", i, pageSize).getJSONArray("list");
                        for (int j = 0; j < list.size(); j++) {
                            JSONObject single = list.getJSONObject(j);
                            Preconditions.checkArgument(single.getString("activity_type_name").equals("品牌活动"), "活动名称: " + single.getString("activity_name") + "   类型:" + single.getString("activity_type_name"));
                        }
                    }
                } else {
                    JSONArray list = activityList("", "BRAND", "", 1, pageSize).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        Preconditions.checkArgument(single.getString("activity_type_name").equals("品牌活动"), "活动名称: " + single.getString("activity_name") + "   类型:" + single.getString("activity_type_name"));
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：活动分析页面，验证根据[活动类型-品牌活动]进行筛选的结果准确\n");
        }
    }

    /**
     * 活动页面-根据活动类型-庆典活动进行搜索
     */
    @Test
    public void activity_CELEBRATION() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        try {
            int total = activityList("","CELEBRATION","",1,1).getInteger("total");//总活动数
            int a = 0;
            if (total > 0) {
                if (total > 50) {
                    if (total % 50 == 0) {
                        a = total / 50;
                    } else {
                        a = (int) Math.ceil(total / 50) + 1;
                    }
                    for (int i = 1; i <= a; i++) {
                        JSONArray list = activityList("", "CELEBRATION", "", i, pageSize).getJSONArray("list");
                        for (int j = 0; j < list.size(); j++) {
                            JSONObject single = list.getJSONObject(j);
                            Preconditions.checkArgument(single.getString("activity_type_name").equals("庆典活动"), "活动名称: " + single.getString("activity_name") + "   类型:" + single.getString("activity_type_name"));
                        }
                    }
                } else {
                    JSONArray list = activityList("", "CELEBRATION", "", 1, pageSize).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        Preconditions.checkArgument(single.getString("activity_type_name").equals("庆典活动"), "活动名称: " + single.getString("activity_name") + "   类型:" + single.getString("activity_type_name"));
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：活动分析页面，验证根据[活动类型-庆典活动]进行筛选的结果准确\n");
        }
    }

    /**
     * 活动页面-根据活动类型-其他进行搜索
     */
    @Test
    public void activity_OTHER() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        try {
            int total = activityList("","OTHER","",1,1).getInteger("total");//总活动数
            int a = 0;
            if (total > 0) {
                if (total > 50) {
                    if (total % 50 == 0) {
                        a = total / 50;
                    } else {
                        a = (int) Math.ceil(total / 50) + 1;
                    }
                    for (int i = 1; i <= a; i++) {
                        JSONArray list = activityList("", "OTHER", "", i, pageSize).getJSONArray("list");
                        for (int j = 0; j < list.size(); j++) {
                            JSONObject single = list.getJSONObject(j);
                            Preconditions.checkArgument(single.getString("activity_type_name").equals("其他"), "活动名称: " + single.getString("activity_name") + "   类型:" + single.getString("activity_type_name"));
                        }
                    }
                } else {
                    JSONArray list = activityList("", "OTHER", "", 1, pageSize).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        Preconditions.checkArgument(single.getString("activity_type_name").equals("其他"), "活动名称: " + single.getString("activity_name") + "   类型:" + single.getString("activity_type_name"));
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：活动分析页面，验证根据[活动类型-其他]进行筛选的结果准确\n");
        }
    }

    /**
     * 活动页面-根据活动类型-促销活动进行搜索
     */
    @Test
    public void activity_PROMOTION() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        try {
            int total = activityList("","PROMOTION","",1,1).getInteger("total");//总活动数
            int a = 0;
            if (total > 0) {
                if (total > 50) {
                    if (total % 50 == 0) {
                        a = total / 50;
                    } else {
                        a = (int) Math.ceil(total / 50) + 1;
                    }
                    for (int i = 1; i <= a; i++) {
                        JSONArray list = activityList("", "PROMOTION", "", i, pageSize).getJSONArray("list");
                        for (int j = 0; j < list.size(); j++) {
                            JSONObject single = list.getJSONObject(j);
                            Preconditions.checkArgument(single.getString("activity_type_name").equals("促销活动"), "活动名称: " + single.getString("activity_name") + "   类型:" + single.getString("activity_type_name"));
                        }
                    }
                } else {
                    JSONArray list = activityList("", "PROMOTION", "", 1, pageSize).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        Preconditions.checkArgument(single.getString("activity_type_name").equals("促销活动"), "活动名称: " + single.getString("activity_name") + "   类型:" + single.getString("activity_type_name"));
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：活动分析页面，验证根据[活动类型-促销活动]进行筛选的结果准确\n");
        }
    }

    /**
     * 活动页面-根据活动类型-会员活动进行搜索
     */
    @Test
    public void activity_VIP() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        try {
            int total = activityList("","VIP","",1,1).getInteger("total");//总活动数
            int a = 0;
            if (total > 0) {
                if (total > 50) {
                    if (total % 50 == 0) {
                        a = total / 50;
                    } else {
                        a = (int) Math.ceil(total / 50) + 1;
                    }
                    for (int i = 1; i <= a; i++) {
                        JSONArray list = activityList("", "VIP", "", i, pageSize).getJSONArray("list");
                        for (int j = 0; j < list.size(); j++) {
                            JSONObject single = list.getJSONObject(j);
                            Preconditions.checkArgument(single.getString("activity_type_name").equals("会员活动"), "活动名称: " + single.getString("activity_name") + "   类型:" + single.getString("activity_type_name"));
                        }
                    }
                } else {
                    JSONArray list = activityList("", "VIP", "", 1, pageSize).getJSONArray("list");
                    for (int j = 0; j < list.size(); j++) {
                        JSONObject single = list.getJSONObject(j);
                        Preconditions.checkArgument(single.getString("activity_type_name").equals("会员活动"), "活动名称: " + single.getString("activity_name") + "   类型:" + single.getString("activity_type_name"));
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：活动分析页面，验证根据[活动类型-会员活动]进行筛选的结果准确\n");
        }
    }

    /**
     * 活动页面-根据活动名称前进行模糊搜索
     */
    @Test
    public void activity_before() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        try {
            int total = activityList("","","",1,1).getInteger("total");//总活动数
            int a = 0;
            if (total > 0) {
                JSONArray list = activityList("", "", "", 1, 10).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    String before_name = single.getString("activity_name").substring(0,1);
                    int total2 = activityList(before_name, "", "", 1, 1).getInteger("total");//包含字符串的总活动数
                    if (total2 > 0){
                        JSONArray list2 = activityList(before_name, "", "", 1, total2).getJSONArray("list");
                        for (int k = 0; k < list2.size(); k ++){
                            JSONObject single2 = list2.getJSONObject(k);
                            Preconditions.checkArgument(single2.getString("activity_name").contains(before_name), "活动名称根据[" + before_name + "]进行筛选时，结果中包含活动: " + single2.getString("activity_name"));
                        }
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：活动分析页面，验证根据[活动名称-前几个字符]进行筛选的结果准确\n");
        }
    }

    /**
     * 活动页面-根据活动名称后进行模糊搜索
     */
    @Test
    public void activity_after() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        try {
            int total = activityList("","","",1,1).getInteger("total");//总活动数
            int a = 0;
            if (total > 0) {
                JSONArray list = activityList("", "", "", 1, 10).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    String name = single.getString("activity_name");
                    String after_name = name.substring(name.length()-1,name.length());
                    int total2 = activityList(after_name, "", "", 1, 1).getInteger("total");//包含字符串的总活动数
                    if (total2 > 0){
                        JSONArray list2 = activityList(after_name, "", "", 1, total2).getJSONArray("list");
                        for (int k = 0; k < list2.size(); k ++){
                            JSONObject single2 = list2.getJSONObject(k);
                            Preconditions.checkArgument(single2.getString("activity_name").contains(after_name), "活动名称根据[" + after_name + "]进行筛选时，结果中包含活动: " + single2.getString("activity_name"));
                        }
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：活动分析页面，验证根据[活动名称-后几个字符]进行筛选的结果准确\n");
        }
    }

    /**
     * 活动页面-根据活动名称中间进行模糊搜索
     */
    @Test
    public void activity_mid() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        try {
            int total = activityList("","","",1,1).getInteger("total");//总活动数
            int a = 0;
            if (total > 0) {
                JSONArray list = activityList("", "", "", 1, 10).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    String name = single.getString("activity_name");
                    if (name.length() > 3) {
                        String mid_name = name.substring(1, 2);
                        int total2 = activityList(mid_name, "", "", 1, 1).getInteger("total");//包含字符串的总活动数
                        if (total2 > 0) {
                            JSONArray list2 = activityList(mid_name, "", "", 1, total2).getJSONArray("list");
                            for (int k = 0; k < list2.size(); k++) {
                                JSONObject single2 = list2.getJSONObject(k);
                                Preconditions.checkArgument(single2.getString("activity_name").contains(mid_name), "活动名称根据[" + mid_name + "]进行筛选时，结果中包含活动: " + single2.getString("activity_name"));
                            }
                        }
                    }
                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：活动分析页面，验证根据[活动名称-中间几个字符]进行筛选的结果准确\n");
        }
    }

    /**
     * 活动页面-根据活动日期进行搜索
     */
    @Test
    public void activity_searchDate() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        try {
            int total = activityList("","","",1,1).getInteger("total");//总活动数
            int a = 0;
            if (total > 0) {
                JSONArray list = activityList("", "", "", 1, 10).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    String startdate = single.getString("start_date");
                    int total2 = activityList("", "", startdate, 1, 1).getInteger("total");//包含字符串的总活动数
                    if (total2 > 0) {
                        JSONArray list2 = activityList("", "", startdate, 1, total2).getJSONArray("list");
                        for (int k = 0; k < list2.size(); k++) {
                            JSONObject single2 = list2.getJSONObject(k);
                            String start = single2.getString("start_date");
                            String end = single2.getString("end_date");
                            Preconditions.checkArgument(belongCalendar(strToDate(startdate),strToDate(start),strToDate(end)), "活动时间根据[" + startdate + "]进行筛选时，结果中包含活动: " + single.getString("activity_name"));
                        }
                    }

                }
            }
        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, "校验：活动分析页面，验证根据[活动日期]进行筛选的结果准确\n");
        }
    }



//---------------- 活动页面 end -------------------------

    /**
     * 案场OCR不上传人脸
     */
    @Test
    public void ocr_noface() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        String desc = "校验：OCR不上传人脸图片\n";
        desc = desc + "\n期待状态码1001；期待提示人脸照片不可为空\n";
        try {
            String code = ocrcode().getString("code");
            System.out.println(code);
            String confirmCode = feidan.confirmQrcodeNoCheckCode(code);

            feidan.checkCode(confirmCode, StatusCode.SUCCESS, "确认验证码");

            String token = JSON.parseObject(confirmCode).getJSONObject("data").getString("token");


//        上传身份信息
            String idCardPath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/idCard.jpg";
            idCardPath = idCardPath.replace("/", File.separator);
            //String facePath = "src/main/java/com/haisheng/framework/testng/bigScreen/checkOrderFile/share.jpg";
            //facePath = facePath.replace("/", File.separator);

            ImageUtil imageUtil = new ImageUtil();
            String imageBinary = imageUtil.getImageBinary(idCardPath);
            imageBinary = stringUtil.trimStr(imageBinary);
           // String faceBinary = imageUtil.getImageBinary(facePath);
            //faceBinary = stringUtil.trimStr(faceBinary);

            //String ocrPicUpload = feidan.ocrPicUpload(token, imageBinary, faceBinary);
            JSONObject ocrPicUpload = ocrPicUpload(token, imageBinary, "");
            int returncode = ocrPicUpload.getInteger("code");
            String message = ocrPicUpload.getString("message");
            System.out.println(ocrPicUpload);
            Preconditions.checkArgument(returncode==1001,"实际状态码：" + returncode);
            Preconditions.checkArgument(message.equals("人脸照片不可为空"),"实际提示语：" + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }
    }

    /**
     * 渠道列表信息各列与新建渠道时信息一致
     */
    @Test
    public void channel_list() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        String desc = "校验：渠道列表各列与新建渠道时信息一致\n";

        try {
            //新建渠道
            String channelname = Long.toString(System.currentTimeMillis());
            Random random = new Random();
            String phone = "147";
            for (int i = 0; i < 8; i++) {
                phone = phone + random.nextInt(10);

            }
            addChannel(channelname, channelname, phone, "837");
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String today = formatter.format(date);

            //获取渠道列表
            JSONObject single = channelList(1,1).getJSONArray("list").getJSONObject(0);
            String single_channel_name = single.getString("channel_name");
            String single_rule_name = single.getString("rule_name");
            String owner_principal = single.getString("owner_principal");
            String single_phone = single.getString("phone");
            String total_customers = single.getString("total_customers");
            Long register_time = single.getLong("register_time"); //注册时间
            String showdate = getDateToString(register_time);
            Preconditions.checkArgument(single_channel_name.equals(channelname),"新建时渠道名称为["+ channelname + "], 展示为[" + single_channel_name + "]");
            Preconditions.checkArgument(single_rule_name.equals("默认规则"),"新建时规则名称为[默认规则], 展示为[" + single_rule_name + "]");
            Preconditions.checkArgument(owner_principal.equals(channelname),"新建时主理人为["+ channelname + "], 展示为[" + owner_principal + "]");
            Preconditions.checkArgument(single_phone.equals(phone),"新建时主理人手机号为["+ phone + "], 展示为[" + single_phone + "]");
            Preconditions.checkArgument(showdate.equals(today),"新建时日期为["+ today + "], 展示为[" + showdate + "]");
            Preconditions.checkArgument(total_customers.equals("0"),"累计报备顾客期待为0，实际为"+ total_customers);




        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }
    }


    /**
     * 登记信息页搜索带特殊字符的姓名
     */
    @Test
    public void customer_search() {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String caseName = ciCaseName;
        String desc = "校验：登记信息页搜索带特殊字符的姓名\n";
        desc = desc + "\n期待状态码1001；期待提示顾客姓名不允许输入特殊字符\n";

        try {
            JSONObject res = customerListnotcheck(1,10,"!@#$%^&*()_+<>:{}");
            String code = res.getString("code");
            String message = res.getString("message");
            Preconditions.checkArgument(code.equals("1001"),"实际状态码：" + code);
            Preconditions.checkArgument(message.equals("顾客姓名不允许输入特殊字符"),"实际提示语：" + message);

        } catch (AssertionError e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } catch (Exception e) {
            failReason += e.toString();
            aCase.setFailReason(failReason);
        } finally {
            saveData(aCase, ciCaseName, caseName, desc);
        }
    }

//    ----------------------------------------------变量定义--------------------------------------------------------------------

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String failReason = "";

    private String response = "";

    private boolean FAIL = false;

    private Case aCase = new Case();

    DateTimeUtil dateTimeUtil = new DateTimeUtil();

    private QADbUtil qaDbUtil = new QADbUtil();

    private int APP_ID = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;

    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_FEIDAN_DAILY_SERVICE;

    private String CI_CMD = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/feidan-daily-test/buildWithParameters?case_name=";

    private String DEBUG = System.getProperty("DEBUG", "true");

    private String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLotornp4DmtYvor5XotKblj7ciLCJ1aWQiOiJ1aWRfZWY2ZDJkZTUiLCJsb2dpblRpbWUiOjE1NzQyNDE5NDIxNjV9.lR3Emp8iFv5xMZYryi0Dzp94kmNT47hzk2uQP9DbqUU";

    private HttpConfig config;

    String mineChannelStr = "5";

    int pageSize = 50;

    private String getIpPort() {
        return "http://dev.store.winsenseos.cn";
    }

    private void initHttpConfig() {
        HttpClient client;
        try {
            client = HCB.custom()
                    .pool(50, 10)
                    .retry(3).build();
        } catch (HttpProcessException e) {
            failReason = "初始化http配置异常" + "\n" + e;
            return;
            //throw new RuntimeException("初始化http配置异常", e);
        }
        //String authorization = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJ5dWV4aXUiLCJleHAiOjE1NzE0NzM1OTh9.QYK9oGRG48kdwzYlYgZIeF7H2svr3xgYDV8ghBtC-YUnLzfFpP_sDI39D2_00wiVONSelVd5qQrjtsXNxRUQ_A";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
        Header[] headers = HttpHeader.custom().contentType("application/json; charset=utf-8")
                .other("shop_id", getShopId().toString())
                .userAgent(userAgent)
                .authorization(authorization)
                .build();

        config = HttpConfig.custom()
                .headers(headers)
                .client(client);
    }

    private String httpPostWithCheckCode(String path, String json, String... checkColumnNames) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        checkCode(response, StatusCode.SUCCESS, path);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private String httpPostUrl(String path, String json) throws Exception {
        initHttpConfig();
        config.url(path).json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();

        response = HttpClientUtil.post(config);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

    private void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);

        if (resJo.containsKey("code")) {
            int code = resJo.getInteger("code");

            message += resJo.getString("message");

            if (expect != code) {
                throw new Exception(message + " expect code: " + expect + ",actual: " + code);
            }
        } else {
            int status = resJo.getInteger("status");
            String path = resJo.getString("path");
            throw new Exception("接口调用失败，status：" + status + ",path:" + path);
        }
    }


    private Object getShopId() {
        return "4116";
    }


    private static final String STAFF_LIST = "/risk/staff/page";
    private static final String STAFF_TYPE_LIST = "/risk/staff/type/list";


    private static String STAFF_TYPE_LIST_JSON = "{\"shop_id\":${shopId}}";

    private static String STAFF_LIST_JSON = "{\"shop_id\":${shopId},\"page\":\"${page}\",\"size\":\"${pageSize}\"}";

    private static String STAFF_LIST_WITH_TYPE_JSON = "{\"shop_idaddStaffTestPage\":${shopId},\"staff_type\":\"${staffType}\",\"page\":\"${page}\",\"size\":\"${pageSize}\"}";


//    ----------------------------------------------接口方法--------------------------------------------------------------------

    /**
     * 订单详情
     */
    public JSONObject orderDetail(String orderId) throws Exception {
        String json =
                "{" +
                        "   \"shop_id\" : " + getShopId() + ",\n" +
                        "\"order_id\":" + orderId +
                        "}";
        String url = "/risk/order/detail";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 订单关键步骤接口
     */
    public JSONObject orderLinkList(String orderId) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"order_id\":\"" + orderId + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 订单关键步骤接口分页
     */
    public JSONObject orderLinkListPage(String orderId, int page, int pageSize) throws Exception {
        String url = "/risk/order/link/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"order_id\":\"" + orderId + "\",\n" +
                        "    \"page\":" + page + ",\n" +
                        "    \"size\":" + pageSize + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);//订单详情与订单跟进详情入参json一样

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 订单列表
     */
    public JSONObject orderList(int status, String namePhone, int page, int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":" + page + ",\n";
        if (status != -1) {
            json += "    \"status\":\"" + status + "\",\n";
        }

        if (!"".equals(namePhone)) {
            json += "    \"customer_name\":\"" + namePhone + "\",\n";
        }

        json += "    \"page_size\":" + pageSize + "\n" +
                "}";
        String[] checkColumnNames = {};
        String res = httpPostWithCheckCode(url, json, checkColumnNames);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /*订单列表根据审核过进行筛选*/
    public JSONObject orderList_audited(int page, int pageSize, String is_audited) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":" + page + ",\n" +
                        "   \"is_audited\":\"" + is_audited + "\",\n" +
                        "    \"size\":" + pageSize + "\n" +
                        "}";
        String[] checkColumnNames = {};
        String res = httpPostWithCheckCode(url, json, checkColumnNames);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 无状态的订单列表
     */
    public JSONObject orderListt(int pageSize) throws Exception {

        String url = "/risk/order/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":1" + ",\n" +
                        "    \"size\":" + pageSize + "\n" +
                        "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 顾客列表 校验code
     */
    public JSONObject customerList(int page, int pageSize, String phone_or_name) throws Exception {
        String url = "/risk/customer/list";
        String json =
                "{\n" +
                        "    \"page\":" + page + ",\n" +
                        "    \"size\":" + pageSize + ",\n";

        if (phone_or_name.equals("")) {
        } else {
            json = json + "    \"phone_or_name\":\"" + phone_or_name + "\",\n";

        }
        json = json + "    \"shop_id\":" + getShopId() + "\n" +
                "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 顾客列表 不校验code
     */
    public JSONObject customerListnotcheck(int page, int pageSize, String phone_or_name) throws Exception {

        String path = "/risk/customer/list";
        String queryUrl = getIpPort() + path;
        String json =
                "{\n" +
                        "    \"page\":" + page + ",\n" +
                        "    \"size\":" + pageSize + ",\n";

        if (phone_or_name.equals("")) {
        } else {
            json = json + "    \"phone_or_name\":\"" + phone_or_name + "\",\n";

        }
        json = json + "    \"shop_id\":" + getShopId() + "\n" +
                "}";

        String res = httpPostUrl(queryUrl, json);

        return JSON.parseObject(res);
    }


    public JSONObject customerList2(String phone, String channel, String adviser, int page, int pageSize) throws
            Exception {

        String json =
                "{\n";

        if (!"".equals(phone)) {
            json += "    \"phone_or_name\":\"" + phone + "\",";
        }

        if (!"".equals(channel)) {
            json += "    \"channel_id\":" + channel + ",";
        }

        if (!"".equals(adviser)) {
            json += "    \"adviser_id\":" + adviser + ",";
        }

        json += "    \"shop_id\":" + getShopId() + "," +
                "    \"page\":" + page + "," +
                "    \"page_size\":" + pageSize +
                "}";


        String res = httpPostWithCheckCode("/risk/customer/list", json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public JSONObject customerEditPC(String cid, String customerName, String phone, String adviserName, String
            adviserPhone) throws Exception {
        String url = "/risk/customer/edit/" + cid;
        String json =
                "{\n" +
                        "\"customer_name\":\"" + customerName + "\"," +
                        "\"phone\":\"" + phone + "\"," +
                        "\"adviser_name\":\"" + adviserName + "\"," +
                        "\"adviser_phone\":\"" + adviserPhone + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        Thread.sleep(1000);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //新建渠道
    public void addChannel(String channelName, String owner, String phone, String ruleId) throws Exception {
        String url = "/risk/channel/add";
        String json =
                "{\n" +
                        "    \"channel_name\":\"" + channelName + "\"," +
                        "    \"owner_principal\":\"" + owner + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"rule_id\":\"" + ruleId + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);
    }

    /**
     * 渠道列表
     */
    public JSONObject channelList(int page, int pageSize) throws Exception {
        String url = "/risk/channel/page";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"page\":" + page + ",\n" +
                        "    \"size\":" + pageSize + "\n" +
                        "}";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 渠道业务员列表
     */
    public JSONObject channelStaffList(String channelId, String namePhone, int page, int pageSize) throws Exception {
        String url = "/risk/channel/staff/page";

        String json =
                "{\n";
        if (!"".equals(namePhone)) {
            json += "    \"name_phone\":\"" + namePhone + "\",";

        }

        json +=
                "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"channel_id\":\"" + channelId + "\",\n" +
                        "    \"page\":" + page + ",\n" +
                        "    \"size\":" + pageSize +
                        "}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 历史信息数据(2020.02.12)
     */
    public JSONObject historyRuleDetail() throws Exception {
        String url = "/risk/history/rule/detail";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 人物抓拍列表(2020-02-12)
     */
    public JSONObject personCatch(int page, int pageSize, String person_type, String start_time, String end_time) throws Exception {
        String url = "/risk/evidence/person-catch/page";
        String json = "{\n";
        if (!"".equals(person_type)) {
            json = json + "   \"person_type\":\"" + person_type + "\",\n";
        }
        if (!"".equals(start_time)) {
            json = json + "   \"start_time\":\"" + start_time + "\",\n";
        }
        if (!"".equals(end_time)) {
            json = json + "   \"end_time\":\"" + end_time + "\",\n";
        }
        json = json +
                "   \"page\":" + page + ",\n" +
                "   \"size\":" + pageSize + ",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 订单数据趋势(2020.02.12) 框架
     */
    public JSONObject historyOrderTrend(String start, String end) throws Exception {
        String url = "/risk/history/rule/order/trend";
        String json = "{\n" +
                //"   \"trend_type\":\"" + trendtype + "\",\n" +
                "   \"start_day\":\"" + start + "\",\n" +
                "   \"end_day\":\"" + end + "\",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 访客数据趋势(2020.02.12)
     */
    public JSONObject historycustomerTrend(String start, String end) throws Exception {
        String url = "/risk/history/rule/customer/trend";
        String json = "{\n" +
                "   \"start_day\":\"" + start + "\",\n" +
                "   \"end_day\":\"" + end + "\",\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 门店实时客流统计
     */
    public JSONObject realTime() throws Exception {
        String url = "/risk/real-time/shop";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 门店实时客流身份统计
     */
    public JSONObject realCustomerType() throws Exception {
        String url = "/risk/real-time/customer-type/distribution";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }



    /**
     * 今日全场累计客流
     */
    public JSONObject realPersonAccumulate() throws Exception {
        String url = "/risk/real-time/persons/accumulated";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 门店历史客流统计
     */
    public JSONObject historyShop(String startTime) throws Exception {
        String url = "/risk/history/shop";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"cycle\":\"" + "WEEK" + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 历史客流身份分布
     */
    public JSONObject historyCustomerType(String startTime) throws Exception {
        String url = "/risk/history/customer-type/distribution";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"cycle\":\"" + "WEEK" + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 历史互动人数趋势
     */
    public JSONObject historypersonAccumulate(String startTime) throws Exception {
        String url = "/risk/history/persons/accumulated";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"start_time\":\"" + startTime + "\",\n" +
                        "    \"cycle\":\"" + "WEEK" + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 活动详情
     */
    public JSONObject activityDetail(String id) throws Exception {
        String url = "/risk/manage/activity/detail";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}\n";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 活动客流对比
     */
    public JSONObject activityContrast(String id) throws Exception {
        String url = "/risk/manage/activity/passenger-flow/contrast";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"id\":\"" + id + "\"" +
                        "}\n";
        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 活动列表
     */
    public JSONObject activityList(String activity_name,String activity_type, String activity_date,int page, int pageSize) throws Exception {
        String url = "/risk/manage/activity/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" ;
        if (!activity_name.equals("")){
            json = json + "\"activity_name\":\"" + activity_name + "\",\n";
        }
        if (!activity_type.equals("")){
            json = json + "\"activity_type\":\"" + activity_type + "\",\n";
        }
        if (!activity_date.equals("")){
            json = json + "\"activity_date\":\"" + activity_date + "\",\n";
        }
        json = json+ "   \"page\":" + page + ",\n" +
                "   \"size\":" + pageSize + "\n" +"}\n";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 人脸搜索上传图片
     */
    public JSONObject imageUpload(String path) throws Exception {
        String url = "http://dev.store.winsenseos.cn/risk/imageUpload";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("authorization", authorization);
        httppost.addHeader("shop_id", String.valueOf(getShopId()));
        File file = new File(path);
        MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
        if (file.toString().contains("png")) {
            mpEntity.addBinaryBody("img_file", file, ContentType.IMAGE_PNG, file.getName());
        }
        if (file.toString().contains("txt")) {
            mpEntity.addBinaryBody("img_file", file, ContentType.TEXT_PLAIN, file.getName());
        }
        if (file.toString().contains("jpg")) {
            mpEntity.addBinaryBody("img_file", file, ContentType.IMAGE_JPEG, file.getName());
        }

        mpEntity.addTextBody("path", "undefined", ContentType.TEXT_PLAIN);
        HttpEntity httpEntity = mpEntity.build();
        httppost.setEntity(httpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpClient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        this.response = EntityUtils.toString(resEntity, "UTF-8");
        System.out.println(response.getStatusLine());
        //checkCode(this.response, StatusCode.SUCCESS, file.getName() + "\n");
        return JSON.parseObject(this.response);
    }


    /**
     * 人脸轨迹搜索
     */
    public String faceTraces(String showUrl) throws Exception {
        String url = "/risk/evidence/face/traces";
        url = getIpPort() + url;
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + ",\n" +
                        "    \"show_url\":\"" + showUrl + "\"" +
                        "}";

        String res = httpPostUrl(url, json);

        return res;
    }


    /**
     * 渠道报备统计 (2020-03-02) 框架要改
     */
    public JSONObject channelReptstatistics() throws Exception {
        String url = "/risk/channel/report/statistics";
        String json = "{\n" +
                "    \"shop_id\":" + getShopId() + "\n}";
        String res = httpPostWithCheckCode(url, json);
        return JSON.parseObject(res).getJSONObject("data");
    }


    /**
     * 渠道业务员详情H5
     */
    public String staffDetailH5(String token) throws Exception {
        String url = "http://dev.store.winsenseos.cn/external/channel/staff/detail";
        String json =
                "{\n" +
                        "    \"token\":\"" + token + "\"," +
                        "    \"shop_id\":\"" + getShopId() + "\"" +
                        "}\n";

        String response = httpPostUrl(url, json);

        return response;
    }

    /**
     * 渠道客户报备H5
     */
    public String customerReportH5(String staffId, String customerName, String phone, String gender, String token) throws Exception {
        String url = "/external/channel/customer/report";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"id\":\"" + staffId + "\"," +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"customer_phone\":\"" + phone + "\"," +
                        "    \"gender\":\"" + gender + "\"," +
                        "    \"token\":\"" + token + "\"" +
                        "}\n";

        String response = httpPostWithCheckCode(url, json);

        return response;
    }

    /**
     * 获取客户报备列表H5
     */
    public String channelCustomerListH5(String token, int page, int pageSize) throws Exception {
        String url = "http://dev.store.winsenseos.cn/external/channel/customer/list";
        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"token\":\"" + token + "\"," +
                        "    \"page\":\"" + page + "\"," +
                        "    \"size\":\"" + pageSize + "\"" +
                        "}\n";

        String response = httpPostUrl(url, json);

        return response;
    }

    /**
     * 员工身份列表
     */
    public JSONArray staffTypeList() throws Exception {
        String json = StrSubstitutor.replace(STAFF_TYPE_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .build()
        );

        String res = httpPostWithCheckCode(STAFF_TYPE_LIST, json);

        return JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
    }

    /**
     * 员工列表
     */
    public JSONArray staffList(int page, int pageSize) throws Exception {
        return staffListReturnData(page, pageSize).getJSONArray("list");
    }

    public JSONObject staffListReturnData(int page, int pageSize) throws Exception {
        String json = StrSubstitutor.replace(STAFF_LIST_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("page", page)
                .put("pageSize", pageSize)
                .build()
        );

        String res = httpPostWithCheckCode(STAFF_LIST, json);

        return JSON.parseObject(res).getJSONObject("data");
    }


    public void newCustomer(int channelId, String channelStaffName, String channelStaffPhone, String
            adviserName, String adviserPhone, String phone, String customerName, String gender) throws Exception {
        String url = "/risk/customer/insert";
        String json =
                "{\n" +
                        "    \"customer_name\":\"" + customerName + "\"," +
                        "    \"phone\":\"" + phone + "\",";
        if (!"".equals(adviserName)) {
            json += "    \"adviser_name\":\"" + adviserName + "\",";
            json += "    \"adviser_phone\":\"" + adviserPhone + "\",";
        }

        if (channelId != -1) {
            json += "    \"channel_id\":" + channelId + "," +
                    "    \"channel_staff_name\":\"" + channelStaffName + "\"," +
                    "    \"channel_staff_phone\":\"" + channelStaffPhone + "\",";
        }

        json +=
                "    \"gender\":\"" + gender + "\"," +
                        "    \"shop_id\":" + getShopId() + "" +
                        "}";

        httpPostWithCheckCode(url, json);
    }

    public JSONObject staffList(String namePhone, int page, int size) throws Exception {
        String url = "/risk/staff/page";
        String json =
                "{\n" +
                        "\"name_phone\":\"" + namePhone + "\"," +
                        "\"page\":\"" + page + "\"," +
                        "\"size\":\"" + size + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //案场OCR验证码
    public JSONObject ocrcode() throws Exception {
        String url = "/risk/shop/ocr/qrcode/refresh";
        String json =
                "{\n" +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }
    //OCR 上传图片
    public JSONObject ocrPicUpload2(String token, String idCard, String face) throws Exception {

        String url = "/external/ocr/pic/upload";

        String json =
                "{\n" +
                        "\"token\":\"" + token + "\"," +
                        "\"idCard\":\"" + idCard + "\"," +
                        "\"face\":\"" + face + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostUrl(url, json);

        return JSON.parseObject(res).getJSONObject("data");


    }

    private static String OCR_PIC_UPLOAD_JSON = "{\"shop_id\":${shopId},\"token\":\"${token}\"," +
            "\"identity_card\":\"${idCard}\",\"face\":\"${face}\"}";

    public JSONObject ocrPicUpload(String token, String idCard, String face) throws Exception {

        String url = "/external/ocr/pic/upload";
        String json = StrSubstitutor.replace(OCR_PIC_UPLOAD_JSON, ImmutableMap.builder()
                .put("shopId", getShopId())
                .put("token", token)
                .put("idCard", idCard)
                .put("face", face)
                .build()
        );

        String res = httpPostNoPrintPara(url, json);

        return JSON.parseObject(res);
    }

    private String httpPostNoPrintPara(String path, String json) throws Exception {
        initHttpConfig();
        String queryUrl = getIpPort() + path;
        config.url(queryUrl).json(json);
        long start = System.currentTimeMillis();


        response = HttpClientUtil.post(config);

        logger.info("response: {}", response);

        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        return response;
    }

//-------------------------------------------------------------用例用到的方法--------------------------------------------------------------------

    private long getTimebeforetoday() throws ParseException {//今天的00：00：00
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");//设置日期格式,今天的0点之前
        String datenow = df.format(new Date());// new Date()为获取当前系统时间，2020-02-18 00:00:00
        Date date = df.parse(datenow);
        long ts = date.getTime(); //转换为时间戳1581955200000
        System.out.println(ts);
        return ts;
    }

    private long getTimebeforeyseterday() throws ParseException { //昨天的00：00：00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -1);
        Date d = c.getTime();
        String day = format.format(d);
        long yesterdray = Long.parseLong(day);
        return yesterdray;
    }

    private String getStartTime(int n) throws ParseException { //前第n天的开始时间（当天的0点）
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -n);
        Date d = c.getTime();
        String day = format.format(d);
        //long starttime = Long.parseLong(day);
        return day;
    }

    private long getEndTime(int n) throws ParseException { //前第n天的结束时间(第二天的0点)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -n + 1);
        Date d = c.getTime();
        String day = format.format(d);
        long endtime = Long.parseLong(day);
        return endtime;
    }

    private String datetoday(String date) { //活动页面返回的3.1 转换为 历史页面 2020-03-07 格式
        String[] spl = date.split("\\.");
        String MM = spl[0];
        String DD = spl[1];
        String day = "2020-" + MM + "-" + DD;
        return day;
    }


    public String getValue(JSONObject data, String key) {
        String value = data.getString(key);

        if (value == null || "".equals(value)) {
            value = "";
        }

        return value;
    }

    public String getOneStaffType() throws Exception {
        JSONArray staffTypeList = staffTypeList();
        Random random = new Random();
        return staffTypeList.getJSONObject(random.nextInt(3)).getString("staff_type");
    }



    private ArrayList unique(ArrayList obj) { //arraylist 去重
        for (int i = 0; i < obj.size() - 1; i++) {
            for (int j = obj.size() - 1; j > i; j--) {
                if (obj.get(j).equals(obj.get(i))) {
                    obj.remove(j);
                }
            }
        }
        return obj;
    }

    private int getTimeNum(int status, String date) throws Exception { //某一天的数量。status为订单状态，day为某一天0点的时间戳
        int total = Integer.parseInt(orderList(status, "", 1, 10).getString("total"));//1正常 2未知 3风险
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long day = sdf.parse(date).getTime();
        System.out.println("day" + day);
        int Timenum = 0; //未知订单总数-今天订单=截止昨天24点前订单页的未知订单数量
        int a = 0;
        if (total > 50) {
            if (total % 50 == 0) {
                a = total / 50;
            } else {
                a = (int) Math.ceil(total / 50) + 1;
            }
            for (int i = 1; i <= a; i++) {
                JSONArray list = orderList(status, "", i, pageSize).getJSONArray("list");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject single = list.getJSONObject(j);
                    long ordertime = single.getLong("deal_time");
                    if (ordertime >= day && ordertime < (day + 86400000)) {
                        Timenum = Timenum + 1;
                    }
                }
            }
        } else {
            JSONArray list = orderList(status, "", 1, pageSize).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                JSONObject single = list.getJSONObject(j);
                long ordertime = single.getLong("deal_time");
                if (ordertime >= day && ordertime < (day + 86400000)) {
                    Timenum = Timenum + 1;
                }
            }
        }
        return Timenum;
    }

    private int getValidDays(JSONObject data) {
        int num = 0;

        JSONArray list = data.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String presentCycle = single.getString("present_cycle");
            if (presentCycle != null && !"".equals(presentCycle)) {
                num++;
            }
        }

        return num;
    }


    String zhangName = "张钧甯";
    String zhangPhone = "19111311116";
    long lianjiaReportTime = 1547014265000L;//2019-01-09 14:11:05
    long noChannelReportTime = 1547034265000L;//2019-01-09 19:44:25
    String lianjiaToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLmtYvor5XjgJDli7_liqjjgJEiLCJ1aWQiOjIxMzYsImxvZ2luVGltZSI6MT" +
            "U3ODk5OTY2NjU3NH0.kQsEw_wGVmPQ4My1p-FNZ556FJC7W177g7jfjFarTu4";
    String lianjiaFreezeStaffIdStr = "2136";
    int lianjiaFreezeStaffIdInt = 2136;
    String wudongToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLjgJDli7_li" +
            "qjjgJExIiwidWlkIjoyMDk4LCJsb2dpblRpbWUiOjE1Nzg1NzQ2MjM4NDB9.exDJ6avJKJd3ezQkYc4fmUkHvXaukqfgjThkpoYgnAw";
    String wudongStaffIdStr = "2098";

    String wudongChannelIdStr = "5";
    String lianjiaChannelStr = "1";


    public void PCF(String customerName, String customerPhone) throws Exception { //PC无渠道

        String smsCode = "384435";
        String adviserName = zhangName;
        String adviserPhone = zhangPhone;
        int channelId = -1;
        String channelStaffName = "";
        String channelStaffPhone = "";
        newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = noChannelReportTime;
        updateReportTime_PCF(customerPhone, customerName, afterReportTime);

    }

    public void PCT(String customerName, int channelId, String channelStaffName, String channelStaffPhone) throws Exception { //PC有渠道
        String customerPhone = "14422110002";
        String smsCode = "384435";
        String adviserName = zhangName;
        String adviserPhone = zhangPhone;
        newCustomer(channelId, channelStaffName, channelStaffPhone, adviserName, adviserPhone, customerPhone, customerName, "MALE");
        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = noChannelReportTime;
        updateReportTime_PCF(customerPhone, customerName, afterReportTime);

    }


    public void H5WuDong(String customerName, String customerPhone) throws Exception {
        String smsCode = "384435";
        customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
    }

    public void H5Lianjia(String customerName, String customerPhone) throws Exception {
        String smsCode = "384435";
        customerReportH5(lianjiaFreezeStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);
        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = lianjiaReportTime;
        updateReportTimeChannel(customerPhone, customerName, 1, lianjiaFreezeStaffIdInt, afterReportTime);
    }


    public void updateReportTime_PCF(String phone, String customerName, long repTime) throws Exception {
        ReportTime reportTime = new ReportTime();
        reportTime.setShopId(4116);
        reportTime.setChannelId(-1);
        reportTime.setChannelStaffId(0);
        reportTime.setPhone(phone);
        reportTime.setCustomerName(customerName);
        long timestamp = repTime;
        reportTime.setReportTime(String.valueOf(timestamp));
        reportTime.setGmtCreate(dateTimeUtil.changeDateToSqlTimestamp(timestamp));
        qaDbUtil.updateReportTime(reportTime);
    }



    //@Test//报备
    public void H5WuDong1() throws Exception {
        String customerName = "昨报备今补全3";
        String customerPhone = "144****0199";
        String smsCode = "384435";
        customerReportH5(wudongStaffIdStr, customerName, customerPhone, "MALE", wudongToken);
    }

    //@Test//报备
    public void H5Lianjia1() throws Exception {
        String customerName = "昨报备今补全3";
        String customerPhone = "14422110199";
        String smsCode = "384435";
        customerReportH5(lianjiaFreezeStaffIdStr, customerName, customerPhone, "MALE", lianjiaToken);
        long afterReportTime = System.currentTimeMillis();
        long beforeReportTime = lianjiaReportTime;
        updateReportTimeChannel(customerPhone, customerName, 1, lianjiaFreezeStaffIdInt, afterReportTime);
    }

    public void updateReportTimeChannel(String phone, String customerName, int channelId, int staffId, long repTime) throws
            Exception {
        ReportTime reportTime = new ReportTime();
        reportTime.setShopId(4116);
        reportTime.setChannelId(channelId);
        reportTime.setChannelStaffId(staffId);
        reportTime.setPhone(phone);
        reportTime.setCustomerName(customerName);
        long timestamp = repTime;
        reportTime.setReportTime(String.valueOf(timestamp));
        reportTime.setGmtCreate(dateTimeUtil.changeDateToSqlTimestamp(timestamp));

        qaDbUtil.updateReportTime(reportTime);
    }

    public JSONObject addStaff(String staffName, String phone, String faceUrl) throws Exception { //新建置业顾问

        String url = "/risk/staff/add";

        String json =
                "{\n" +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"phone\":\"" + phone + "\"," +
                        "    \"face_url\":\"" + faceUrl + "\"," +
                        "\"shop_id\":" + getShopId() +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject staffDelete(String id) throws Exception { //删除置业顾问
        String url = "/risk/staff/delete/" + id;
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public JSONObject addChannelStaff(String channelId, String staffName, String phone) throws Exception { //新建业务员

        String url = "/risk/channel/staff/register";

        String json =
                "{\n" +
                        "    \"shop_id\":" + getShopId() + "," +
                        "    \"staff_name\":\"" + staffName + "\"," +
                        "    \"channel_id\":\"" + channelId + "\"," +
                        "    \"phone\":\"" + phone + "\"" +
                        "}";

        String res = httpPostWithCheckCode(url, json);

        return JSON.parseObject(res).getJSONObject("data");
    }

    public void changeChannelStaffState(String staffId) throws Exception { //禁用业务员
        String json = "{}";

        httpPostWithCheckCode("/risk/channel/staff/state/change/" + staffId, json);
    }




    private void setBasicParaToDB(Case aCase, String ciCaseName, String caseName, String caseDesc) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("于海生");
        aCase.setExpect("code==1000");
        aCase.setResponse(response);

        if (!StringUtils.isEmpty(failReason) || !StringUtils.isEmpty(aCase.getFailReason())) {
            aCase.setFailReason(failReason);
        } else {
            aCase.setResult("PASS");
        }
    }

    private void saveData(Case aCase, String ciCaseName, String caseName, String caseDescription) {
        setBasicParaToDB(aCase, ciCaseName, caseName, caseDescription);
        qaDbUtil.saveToCaseTable(aCase);
        if (!StringUtils.isEmpty(aCase.getFailReason())) {
            logger.error(aCase.getFailReason());
            dingPush("飞单日常-吕雪晴 \n" + aCase.getCaseDescription() + " \n" + aCase.getFailReason());
        }
    }

    private void dingPush(String msg) {
        AlarmPush alarmPush = new AlarmPush();
        if (DEBUG.trim().toLowerCase().equals("false")) {
//            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);
        } else {
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
        }
        msg = msg.replace("java.lang.Exception: ", "异常：");
        msg = msg.replace("java.lang.IllegalArgumentException:", "异常：");
        alarmPush.dailyRgn(msg);
        this.FAIL = true;
        Assert.assertNull(aCase.getFailReason());


    }

    private void dingPushFinal() {
        if (DEBUG.trim().toLowerCase().equals("false") && FAIL) {
            AlarmPush alarmPush = new AlarmPush();
            alarmPush.setDingWebhook(DingWebhook.QA_TEST_GRP);
//            alarmPush.setDingWebhook(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP);

            //15898182672 华成裕
            //18513118484 杨航
            //15011479599 谢志东
            //18600872221 蔡思明
            String[] rd = {"18513118484", //杨航
                    "15011479599", //谢志东
                    "15898182672"}; //华成裕
            alarmPush.alarmToRd(rd);
        }
    }

    private static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else if (nowTime.compareTo(beginTime) == 0 || nowTime.compareTo(endTime) == 0) {
            return true;
        } else {
            return false;
        }
    }

    // 字符串 转 日期
    private static Date strToDate(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
        }
        return date;
    }

    public static String getDateToString(long time) { //时间戳转日期
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
        }

   // public static void main(String[] args) throws ParseException {// ---不用理我！

    //}


}



