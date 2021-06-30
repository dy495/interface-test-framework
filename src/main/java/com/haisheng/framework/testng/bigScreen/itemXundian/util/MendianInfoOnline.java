package com.haisheng.framework.testng.bigScreen.itemXundian.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.org.apache.commons.codec.binary.Base64;
import com.haisheng.framework.util.DateTimeUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MendianInfoOnline {
    DateTimeUtil dt = new DateTimeUtil();
    XundianScenarioUtilOnline xd = XundianScenarioUtilOnline.getInstance();
    public final String username = "storedemo@winsense.ai";
    public final String usernamechin = "管理员";
    public final String passwd = "b0581aa73b04d9fe6e3057a613e6f363";
    public final String uid = "uid_580f244a";
    public final String usershop = "中关村1号店";
    public final long shop_id = 14630L;
    public final long shop_id_01 = 14630L;
    public final String shop_id_01_chin = "中关村1号店";

    public final String deviceId = "7698197825586176"; //设备id 门店-进店
    public final String deviceId2 = "8070121884156928"; //设备id yushipingtai(全功能)
    public final String deviceId3 = ""; //设备id
    public final String picPath = ""; //图片相对路径



    public final Long xdOperateitem(Long shopid, String type, int reset, int result,boolean is_personalized_check_list) throws Exception {
        JSONObject obj = xd.checkStartapp(shopid, type, reset,is_personalized_check_list);
        Long patrolID = obj.getLong("id");
        JSONArray checklist = obj.getJSONArray("check_lists");

        for (int i = 0; i < checklist.size(); i++) {
            JSONObject eachlist = checklist.getJSONObject(i);
            Long listID = eachlist.getLong("id"); // 获取list id
            JSONArray chkitems = eachlist.getJSONArray("check_items");
            for (int j = 0; j < chkitems.size(); j++) {
                JSONObject eachitem = chkitems.getJSONObject(j);
                Long itemID = eachitem.getLong("id"); //每个清单内循环 获取item id
                JSONArray piclist = getpic(0);
                //巡检项目结果 1合格；2不合格；3不适用
                xd.checks_item_submit(shopid, patrolID, listID, itemID, result, "啊啊啊啊啊啊", piclist);
                //xd.checks_item_submit(shopid, patrolID, listID, itemID, result, "", null);
                Thread.sleep(100);
            }

        }
        return patrolID;
    }

    public final JSONObject xdOperate(Long shopid, String type, int reset, int result,boolean is_personalized_check_list) throws Exception {

        Long patrolID =  xdOperateitem(shopid,type,reset,result,is_personalized_check_list);
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

    public final JSONArray getpicFour(Integer type) throws Exception {
        String base64 = getImgStr("src/main/java/com/haisheng/framework/testng/bigScreen/MenjinImages/猫.png");

        JSONObject obj = xd.upload_pic(base64,shop_id_01,type);
        JSONObject obj2 = xd.upload_pic(base64,shop_id_01,type);
        JSONObject obj3 = xd.upload_pic(base64,shop_id_01,type);
        JSONObject obj4 = xd.upload_pic(base64,shop_id_01,type);

        JSONArray pic_list = new JSONArray();
        JSONObject obj1 = new JSONObject();
        JSONObject obj5 = new JSONObject();
        JSONObject obj6 = new JSONObject();
        JSONObject obj7 = new JSONObject();

        obj1.put("pic_path",obj.getString("pic_path"));
        obj1.put("device_id",deviceId);

        obj5.put("pic_path",obj.getString("pic_path"));
        obj5.put("device_id",deviceId);

        obj6.put("pic_path",obj.getString("pic_path"));
        obj6.put("device_id",deviceId);

        obj7.put("pic_path",obj.getString("pic_path"));
        obj7.put("device_id",deviceId);

        //obj1.put("time",System.currentTimeMillis());
        pic_list.add(obj1);
        pic_list.add(obj2);
        pic_list.add(obj3);
        pic_list.add(obj4);
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
