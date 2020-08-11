package com.haisheng.framework.testng.bigScreen.crm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CrmStatistics {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    Crm crm = new Crm();

    HashMap<String, Integer> level = new HashMap<>();
    HashMap<String, Integer> source = new HashMap<>();
    HashMap<String, Integer> channel = new HashMap<>();
    HashMap<String, Integer> visit = new HashMap<>();
    HashMap<String, Integer> delieve = new HashMap();

    String message = "";

    @Test
    public void statisticsCustomer() throws Exception {
        String ciCaseName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        String caseDesc = "";
        String caseName = ciCaseName;

        //到店人数=visit_count；
        //所属区域=belongs_area（来源）
        //客户级别=customer_level（0-H,1-A,2-B,3-C,4-F）
        //customer_select_type（渠道）
        /**
         * 客户等级：H：7，
         *
         *
         *
         * */

//        String startTime = "2020-06-10";
//        String endTime = "2020-06-10";

        String startTime = LocalDate.now().toString();
        String endTime = startTime;

        LocalDate now = LocalDate.now();

        int pages = crm.customerListPC(startTime, endTime, 1, 10).getInteger("pages");

        for (int i = 1; i <= pages; i++) {

            JSONObject data = crm.customerListPC(startTime, endTime, i, 10);

            statis(data);
        }

        logger.info("=============================顾客等级===================================");
        for (Map.Entry<String, Integer> entry : level.entrySet()) {
            logger.info(entry.getKey() + ":" + entry.getValue());
        }

        logger.info("=============================到店人数===================================");
        for (Map.Entry<String, Integer> entry : visit.entrySet()) {
            logger.info(entry.getKey() + ":" + entry.getValue());
        }

        logger.info("=============================来源===================================");
        for (Map.Entry<String, Integer> entry : source.entrySet()) {
            logger.info(entry.getKey() + ":" + entry.getValue());
        }

        logger.info("=============================渠道===================================");
        for (Map.Entry<String, Integer> entry : channel.entrySet()) {
            logger.info(entry.getKey() + ":" + entry.getValue());
        }
    }

    private void statis(JSONObject data) {

        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String customerLevel = single.getString("customer_level");//顾客等级（身份）
            addData(level, customerLevel);

            String visitCount = single.getString("visit_count");//伴随人数
            addData(visit, visitCount);

            String belongsArea = single.getString("belongs_area");//所属区域（来源）
            addData(source, belongsArea);

            String customerSelectType = single.getString("customer_select_type");//渠道
            addData(channel, customerSelectType);
        }
    }

    public void addData(HashMap<String, Integer> hm, String key) {

        if (key != null && !"".equals(key)) {

            if (hm.containsKey(key)) {
                hm.put(key, hm.get(key) + 1);
            } else {
                hm.put(key, 1);
            }
        } else {
            key = "unKnown";
            if (hm.containsKey(key)) {
                hm.put(key, hm.get(key) + 1);
            } else {
                hm.put(key, 1);
            }
        }
    }


    HashMap<String, Integer> drive = new HashMap();

    @Test
    public void statisticDrive() throws Exception {

        int pages = crm.driveList(1, 10).getInteger("pages");

        for (int i = 1; i <= pages; i++) {

            JSONObject data = crm.driveList(i, 10);
            statis1(data);
        }

        logger.info("=============================试驾统计===================================");
        for (Map.Entry<String, Integer> entry : drive.entrySet()) {
            logger.info(entry.getKey() + ":" + entry.getValue());
        }
    }

    public void statis1(JSONObject data) {

        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String model = single.getString("model");
            addData(drive, model);
        }
    }


    @Test
    public void statisticDeleive() throws Exception {

        int pages = crm.delieveList(1, 10).getInteger("pages");

        for (int i = 1; i <= pages; i++) {

            JSONObject data = crm.delieveList(i, 10);
            statis2(data);
        }

        logger.info("=============================交车统计===================================");
        for (Map.Entry<String, Integer> entry : delieve.entrySet()) {
            logger.info(entry.getKey() + ":" + entry.getValue());
        }
    }

    public void statis2(JSONObject data) {

        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            String model = single.getString("model");
            addData(delieve, model);
        }
    }

    //    @Test
    public void test() throws Exception {
        String[] models = {"Panamera", "718", "911", "Macan", "Taycan", "Cayenne"};

        String model = "718";

        for (int i = 0; i < models.length; i++) {
            addDrive(models[i]);
        }
    }


    public void addDrive(String model) throws Exception {

        DateTimeUtil dt = new DateTimeUtil();
        FileUtil fileUtil = new FileUtil();
        String picurl = fileUtil.getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/dailyImages/2019-10-22_1.jpg");

        String name = crm.genRandom7();
        String idCard = "110226198210260078";
        String gender = "男";
        String signTime = dt.getHistoryDate(0);
        String country = "中国";
        String city = "city";
        String email = dt.getHistoryDate(0) + "@qq.com";
        String address = "飞单将返回空收到货副科级";
        String ward_name = "防富家大室";
        String driverLicensePhoto1Url = picurl;
        String driverLicensePhoto2Url = picurl;
        String electronicContractUrl = picurl;
        String phone = crm.genPhoneNum();
        crm.addDrive(name, idCard, gender, phone, signTime, "试乘试驾", model, country, city, email, address,
                ward_name, driverLicensePhoto1Url, driverLicensePhoto2Url, electronicContractUrl).getInteger("id");

    }


    @BeforeClass
    public void login() {
        crm.majordomoLogin();
    }

}
