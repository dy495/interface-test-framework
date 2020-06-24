package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;

import java.util.ArrayList;
import java.util.List;

public class xundianScenarioUtilX extends TestCaseCommon {

    /**
     * 单例，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     *
     * */

    private static volatile xundianScenarioUtilX instance = null;

    private xundianScenarioUtilX() {}


    public static xundianScenarioUtilX getInstance() {

        if (null == instance) {
            synchronized (xundianScenarioUtilX.class) {
                if (null == instance) {
                    instance = new xundianScenarioUtilX();
                }
            }
        }

        return instance;
    }
    /**
     * @description :新增清单
     * @date :2020/6/20 16:42
     **/
    public JSONObject CheckListAdd(String name, String desc, String title, String comment) throws Exception {
        String url = "/patrol/check-list/add";
        //TODO：此处优化动态查询店铺list
        List<Long> shop_list=new ArrayList<>();
        long i=Long.valueOf(getXunDianShop());
        shop_list.add(i);

        JSONObject json1=new JSONObject();
        json1.put("order",1);
        json1.put("title",title);
        json1.put("comment",comment);

        JSONArray item=new JSONArray();
        item.add(0,json1);

        JSONObject json=new JSONObject();
        json.put("name",name);
        json.put("desc",desc);
        json.put("items",item);
        json.put("shop_list",shop_list);


        String res = httpPostWithCheckCode(url, json.toJSONString(),IpPort);
        System.out.println(res);

        return JSON.parseObject(res).getJSONObject("data");
    }



    /**
     * 巡检员列表
     * @return
     * @throws Exception
     */
    public JSONObject inspectorList() throws Exception {
        String url = "/patrol/schedule-check/inspector/list";
        String json =
                "{}";

        String res = httpPostWithCheckCode(url, json,IpPort);
        System.out.println(res);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 巡检门店
     */
    public JSONObject shopList(String inspectorId, String districtCode) throws Exception {
        String url = "/patrol/schedule-check/shop/list";
        String json =
                "{\n" +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"district_code\":\"" + districtCode + "\"\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json,IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "http://123.57.148.247/";
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
            appendFailreason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);

        //saveData("登陆");
    }

    /**
     * @description :新建定检任务
     * @date :2020/6/21 9:58
     **/
    public JSONObject addScheduleCheck(String name, String cycle, String dates, String sendTime, String validStart, String validEnd,
                                       String inspectorId) throws Exception {
        String url = "/patrol/schedule-check/add";
        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"cycle\":\"" + cycle + "\",\n" +
                        "    \"dates\":" + dates + ",\n" +
                        "    \"send_time\":\"" + sendTime + "\",\n" +
                        "    \"valid_start\":\"" + validStart + "\",\n" +
                        "    \"valid_end\":\"" + validEnd + "\",\n" +
                        "    \"inspector_id\":\"" + inspectorId + "\",\n" +
                        "    \"shop_list\":[\n" + getXunDianShop() + "    ]\n" +
                        "}";

        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }



    /**
     * @description :获取定检任务列表
     * @date :2020/6/21 9:59
     **/
    public JSONObject scheduleCheckList(int size,int page) throws Exception {
        String url = "/patrol/schedule-check/page";

        JSONObject json=new JSONObject();
        json.put("size",size);
        json.put("page",page);
        String res = httpPostWithCheckCode(url, json.toJSONString(),IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description:  删除定检任务
     */
    public JSONObject scheduleCheckDelete(long id) throws Exception {
        String url = "/patrol/schedule-check/delete";
        JSONObject json=new JSONObject();
        json.put("id",id);
        String res = httpPostWithCheckCode(url, json.toJSONString(),IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * 不合格提交图片
     */
    public JSONObject checksItemSubmitN(long patrolId, long listId, long itemId, List<String> picList) throws Exception {
        String url = "/patrol/shop/checks/item/submit";

        JSONObject json=new JSONObject();
        json.put("shop_id",getXunDianShop());
        json.put("patrol_id",patrolId);
        json.put("list_id",listId);
        json.put("item_id",itemId);
        json.put("pic_list",picList);



        String res = httpPost(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * @description :不合格图片提交返回code
     * @date :2020/6/22 20:54
     **/

    public Integer checksItemSubmitY(long patrolId, long listId, long itemId, List<String> picList) throws Exception {
        String url = "/patrol/shop/checks/item/submit";

        JSONObject json=new JSONObject();
        json.put("shop_id",getXunDianShop());
        json.put("patrol_id",patrolId);
        json.put("list_id",listId);
        json.put("item_id",itemId);
        json.put("pic_list",picList);

        String res = httpPost(url, json.toJSONString(), IpPort);

        return JSON.parseObject(res).getInteger("code");
    }

    /**
     * submit one
     */
    public JSONObject submitOne(Integer check_result,long item_id,long list_id,long patrol_id)throws Exception{
        String url="/patrol/shop/checks/item/submit";
//        String json="{\n" +
//                "    \"shop_id\":" + getXunDianShop() + ",\n" +
//                "    \"check_result\":" +check_result+ ",\n" +
//                "    \"item_id\":" + item_id + ",\n" +
//                "    \"list_id\":" + list_id + ",\n" +
//                "    \"patrol_id\":" + patrol_id + "\n" +
//                "}";
        JSONObject json=new JSONObject();
        String shopid=getXunDianShop();
        json.put("shop_id",shopid);
        json.put("check_result",check_result);
        json.put("item_id",item_id);
        json.put("list_id",list_id);
        json.put("patrol_id",patrol_id);


        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * start 巡店
     */
    public JSONObject checkStart(String check_type,Integer reset) throws Exception{
        String url="/patrol/shop/checks/start";
        String json="{\n" +
                "    \"shop_id\":" + getXunDianShop() + ",\n" +
                "    \"check_type\":" + check_type+ ",\n" +
                "    \"reset\":" + reset + "\n" +
                "}";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description :不合格留痕截屏
     * @date :2020/6/21 10:58
     **/
    public JSONObject picUpload(int type,String pic_data)throws Exception{
         String url="/patrol/pic/base64/upload";
         JSONObject json=new JSONObject();
         json.put("shop_id",getXunDianShop());
         json.put("type",type);
         json.put("pic_data",pic_data);
        String res = httpPostWithCheckCode(url, json.toJSONString(),IpPort);

        return JSON.parseObject(res).getJSONObject("data");


    }

    /**
     * checks submit
     */
    public JSONObject checkSubmit(String commit,Long id)throws Exception{
        String url="/patrol/shop/checks/submit";
        String json="{\n" +
                "    \"shop_id\":" + getXunDianShop() + ",\n" +
                "    \"comment\":" +commit+ ",\n" +
                "    \"id\":" + id + "\n" +
                "}";
        String res = httpPost(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * 寻巡店中心门店设备查询
     */
   public JSONObject shopDevice()throws Exception{
       String url="/patrol/shop/device/list";
       String json="{\n"+
               "\"shop_id\":"+getXunDianShop()+"\n"+
               "}";
       String res=httpPostWithCheckCode(url,json,IpPort);
       return JSON.parseObject(res).getJSONObject("data");
   }


    /**
     * shop detail
     */
    public JSONObject xunDianCenterDetail()throws Exception{
        String url="/patrol/shop/detail";
        JSONObject json=new JSONObject();
        json.put("id",getXunDianShop());

        String res=httpPostWithCheckCode(url,json.toJSONString(),IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
    /**
     * @description :门店详情页 page
     * @date :2020/6/23 18:42
     **/
    public JSONObject xunDianCenterPage(int page,int size)throws Exception{
        String url="/patrol/shop/page";
        JSONObject json=new JSONObject();
        json.put("page",page);
        json.put("size",size);

        String res=httpPostWithCheckCode(url,json.toJSONString(),IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }


}
