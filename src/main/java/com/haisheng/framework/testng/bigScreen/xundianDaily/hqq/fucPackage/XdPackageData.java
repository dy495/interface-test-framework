package com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.ImageUtil;
import org.jooq.util.derby.sys.Sys;
import org.testng.annotations.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class XdPackageData extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */

    private static volatile XdPackageData instance = null;

    private XdPackageData() {
    }

    public static XdPackageData getInstance() {

        if (null == instance) {
            synchronized (XdPackageData.class) {
                if (null == instance) {
                    //这里
                    instance = new XdPackageData();
                }
            }
        }

        return instance;
    }

    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "http://123.57.148.247";
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/64.txt";  //巡店不合格图片base64

    /**
     * @description:登录
     * @author: qingqing
     * @time:
     */
    public void login(String userName, String passwd) {
        initHttpConfig();
        String path = "/patrol-login";
        String loginUrl = IpPort + path;
        String json = "{\"type\":0, \"username\":\"" + userName + "\",\"password\":\"" + passwd + "\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization:" + authorization);
        } catch (Exception e) {
            appendFailReason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        //saveData("登陆");

    }

    /**
     * 获取待办事项列表中三个tab页的数量
     */
    public <T> Map<String, T> getTab_total(Integer page, Integer size, Integer type, Long last_value) throws Exception {
        JSONArray list = xd.task_list(page, size, type, last_value).getJSONArray("list");
        Integer total = list.size();
        Map<String, T> result = new HashMap<>();
        result.put("list", (T) list);
        result.put("total", (T) total);
        return result;
    }

    /**
     * 获取待办事项列表中的shop_id和id
     */
    public Map<String, Long> getId_ShopId(JSONArray list, String taskType) throws Exception {
        Long id = null;
        Long shop_id = null;
        for (int i = 0; i < list.size(); i++) {
            String task_type = list.getJSONObject(i).getString("task_type");
            if (task_type.equals(taskType)) {
                id = list.getJSONObject(i).getLong("id");
                shop_id = list.getJSONObject(i).getLong("shop_id");
            }
        }
        Map<String, Long> result = new HashMap<>();
        result.put("id", id);
        result.put("shop_id", shop_id);
        return result;
    }

    /**
     * 获取base64接口上传留痕图片的数组（里面是5张）
     */
    public JSONArray getPicPath() throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 5; i++) {
            String picture = new ImageUtil().getImageBinary(filepath);
            String picPath = xd.picUpload(0, picture).getString("pic_path");
            jsonArray.add(picPath);
        }
        return jsonArray;
    }

    /**
     * 开始定检巡店
     */
    public Map<String, Object> Scheduled(Long shop_id, Integer reset, Long task_id) throws Exception {
        JSONObject res = xd.shopChecks_start(shop_id, "SCHEDULED", reset, task_id);
        Long patrol_id = res.getLong("id");
        Long list_id = null;
        Long item_id = null;
        JSONArray check_lists = res.getJSONArray("check_lists");
        for (int i = 0; i < check_lists.size(); i++) {
            list_id = check_lists.getJSONObject(i).getLong("id");
            JSONArray items_list = check_lists.getJSONObject(i).getJSONArray("check_items");
            for (int j = 0; j < items_list.size(); j++) {
                item_id = items_list.getJSONObject(j).getLong("id");
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("patrol_id", patrol_id);
        result.put("list_id", list_id);
        result.put("item_id", item_id);
        return result;
    }
    /**
     * 获取门店的巡店次数
     */
    public Integer patrol_num(JSONObject data ,Long shop_id) throws Exception {
        Integer patrol_num = 0;
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            long id = list.getJSONObject(i).getInteger("id");
            if (id == shop_id) {
                patrol_num = list.getJSONObject(i).getInteger("patrol_num");
                break;
            }
        }
        return patrol_num;
    }
}


