package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.haisheng.framework.util.DateTimeUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MendianInfo {
    DateTimeUtil dt = new DateTimeUtil();
    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    public final String username = "yuexiu@test.com";
    public final String passwd = "f5b3e737510f31b88eb2d4b5d0cd2fb4";
    public final long shop_id = 4116L;
    public final long shop_id_01 = 43072L;
    public final String shop_id_01_chin = "AI-Test(门店订单录像)";

    public final String deviceId = "8075861419983872"; //设备id 152-k8s-1
    public final String deviceId2 = "7975589134468096"; //设备id AI摄像头算法测试 （152）
    public final String deviceId3 = ""; //设备id
    public final String picPath = ""; //图片相对路径



    public final Long xdOperateitem(Long shopid, String type, int reset, int result) throws Exception {
        JSONObject obj = xd.checkStartapp(shopid, type, reset);
        Long patrolID = obj.getLong("id");
        JSONArray checklist = obj.getJSONArray("check_lists");
        JSONArray piclist = getpic(0);
        for (int i = 0; i < checklist.size(); i++) {
            JSONObject eachlist = checklist.getJSONObject(i);
            Long listID = eachlist.getLong("id"); // 获取list id
            JSONArray chkitems = eachlist.getJSONArray("check_items");
            for (int j = 0; j < chkitems.size(); j++) {
                JSONObject eachitem = chkitems.getJSONObject(j);
                Long itemID = eachitem.getLong("id"); //每个清单内循环 获取item id
                //巡检项目结果 1合格；2不合格；3不适用
                xd.checks_item_submit(shopid, patrolID, listID, itemID, result, "啊啊啊啊啊啊", piclist);
                //xd.checks_item_submit(shopid, patrolID, listID, itemID, result, "", null);
                Thread.sleep(100);
            }

        }
        return patrolID;
    }

    public final JSONObject xdOperate(Long shopid, String type, int reset, int result) throws Exception {

        Long patrolID =  xdOperateitem(shopid,type,reset,result);
        xd.checks_submit(shopid, patrolID, "一次巡店完成");
        JSONObject retobj = new JSONObject();
        retobj.put("patrolID",patrolID);
        return retobj;
    }




    public final JSONObject djXdOperate(Long shopid, String type, int reset, int result,Long task_id) throws Exception {
        JSONObject obj = xd.checkStartapp(shopid, type, reset,task_id);
        Long patroldjID = obj.getLong("id");
        JSONArray checklist = obj.getJSONArray("check_lists");
        for (int i = 0; i < checklist.size(); i++) {
            JSONObject eachlist = checklist.getJSONObject(i);
            Long listID = eachlist.getLong("id"); // 获取list id
            JSONArray chkitems = eachlist.getJSONArray("check_items");
            for (int j = 0; j < chkitems.size(); j++) {
                JSONObject eachitem = chkitems.getJSONObject(j);
                Long itemID = eachitem.getLong("id"); //每个清单内循环 获取item id
                //巡检项目结果 1合格；2不合格；3不适用
                xd.checks_item_submit(shopid, patroldjID, listID, itemID, result, "zdh", null);
            }

        }
        xd.checks_submit(shopid, patroldjID, "一次定检巡店完成");
        JSONObject retobj = new JSONObject();
        retobj.put("patrolID",patroldjID);
        return retobj;
    }

    public final JSONArray getpic(Integer type) throws Exception {
        String base64 = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/猫.png");
        JSONObject obj = xd.upload_pic(base64,shop_id_01,type);
        JSONArray pic_list = new JSONArray();
        JSONObject obj1 = new JSONObject();
        obj1.put("pic_path",obj.getString("pic_path"));
        obj1.put("device_id",deviceId);
        //obj1.put("time",System.currentTimeMillis());
        pic_list.add(obj1);

        return pic_list;

    }

    public static String getImgStr(String imgFile) { //图片转base64
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }


}
