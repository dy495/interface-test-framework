package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.DateTimeUtil;

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
        for (int i = 0; i < checklist.size(); i++) {
            JSONObject eachlist = checklist.getJSONObject(i);
            Long listID = eachlist.getLong("id"); // 获取list id
            JSONArray chkitems = eachlist.getJSONArray("check_items");
            for (int j = 0; j < chkitems.size(); j++) {
                JSONObject eachitem = chkitems.getJSONObject(j);
                Long itemID = eachitem.getLong("id"); //每个清单内循环 获取item id
                //巡检项目结果 1合格；2不合格；3不适用
                xd.checks_item_submit(shopid, patrolID, listID, itemID, result, "zdh", null);
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
}
