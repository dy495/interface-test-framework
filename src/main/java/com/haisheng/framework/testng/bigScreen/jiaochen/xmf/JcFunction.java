package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.util.BasicUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.gly.Variable.registerListVariable;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletMessageDetailScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted.AppletMessageListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppAdmitScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception.AppStartReceptionScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.record.ImportPageScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage.VoucherListScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.AppStartReception;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.AppletAppointment;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.PcCreateActile;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class JcFunction extends BasicUtil {
    private final VisitorProxy visitor;
    public final PublicParm pp;
    ScenarioUtil jc = new ScenarioUtil();

    Random random = new Random();
    DateTimeUtil dt = new DateTimeUtil();
    FileUtil file = new FileUtil();

    public JcFunction(VisitorProxy visitor, PublicParm pp) {
        super(visitor);
        this.visitor = visitor;
        this.pp = pp;
    }

    public String genPhoneNum() {
        String num = "177" + (random.nextInt(89999999) + 10000000);

        return num;
    }

    public String[] salereception(String phone) {
        //注册过的手机号接待
        IScene appAdmitScene = AppAdmitScene.builder().phone(phone).build();
        JSONObject data = jc.invokeApi(appAdmitScene);
        Long customerId = data.getLong("customer_id");
        //开始接待
        IScene appstartReception = AppStartReceptionScene.builder()
                .customerId(customerId)
                .customerPhone(phone)
                .build();
        String[] receptionId = new String[2];
        receptionId[0] = jc.invokeApi(appstartReception).getString("id");  //接待ID
        return receptionId;
    }

    public Integer importCheck(String name) {
        IScene importPageScene = ImportPageScene.builder().page(1).size(10).user(name).build();
        JSONObject importReault = jc.invokeApi(importPageScene).getJSONArray("list").getJSONObject(0);
        int sueecssNum = importReault.getInteger("success_num");
        return sueecssNum;
    }

    //通过权限描述，返回权限id
    public Long getAccessId(String label) {
        JSONArray child = jc.roleTree().getJSONArray("children");
        Long id = 0L;
        for (Object aa : child) {
            JSONObject tmp = (JSONObject) JSONObject.toJSON(aa);
            JSONArray temp = tmp.getJSONArray("children");
            for (Object bb : temp) {
                JSONObject temp2 = (JSONObject) JSONObject.toJSON(bb);
                String labelTemp = temp2.getString("label");
                id = temp2.getLong("value");
                if (label.equals(label)) {
                    System.err.println("all:" + id);
                }
            }
        }
        return id;
    }

    public Long getAccessId2(String labelParm) {

        JSONObject child = jc.roleTree();
        ReadContext readContext = JsonPath.parse(child);

        ArrayList label = readContext.read("$.children[*].children[*].label");
        ArrayList value = readContext.read("$.children[*].children[*].value");

        HashMap<String, String> access = new HashMap<>();
        for (int i = 0; i < label.size(); i++) {
            label.get(i);
//            System.out.println("DateAccess"+i+"(\""+label.get(i)+"\","+value.get(i)+"),");
            access.put(String.valueOf(label.get(i)), String.valueOf(value.get(i)));
        }
//        access.forEach((k,v)-> System.err.println(k+"-"+v));
        Long id = Long.valueOf(access.get(labelParm));
        return id;
    }

    //new 获取小程序待评价的消息id
    public String[] getMessageId(String messageType) {
        String messageId[] = new String[2];
        //小程序获取评价id
        Long lastValue = null;
        int size = 20;
        while (size == 20) {
            JSONObject data = jc.appletMessageList(lastValue, 20);
            lastValue = data.getLong("last_value");
            JSONArray list = data.getJSONArray("list");
            size = list.size();
            for (int i = 0; i < list.size(); i++) {
                messageId[0] = list.getJSONObject(i).getString("id");

                JSONObject messageDetail = jc.appletMessageDetail(messageId[0]);
                String messageTypeName = messageDetail.getString("message_type_name");
                String isCanEvaluate = messageDetail.getString("is_can_evaluate");
                if (messageTypeName.equals(messageType) && isCanEvaluate.equals("true")) {
                    messageId[1] = messageDetail.getJSONObject("evaluate_info").getString("id");
                    return messageId;
                }
            }
        }
        return messageId;

    }

    public int getVoucherTotal(String phone) {
        int total = VoucherListScene.builder().transferPhone(phone).build().visitor(visitor).execute().getJSONArray("list").size();
        return total;
    }

    public Long getMessDetailId() {
        Long listid = AppletMessageListScene.builder().build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
        Long detailid = AppletMessageDetailScene.builder().id(listid).build().visitor(visitor).execute().getJSONObject("evaluate_info").getLong("id");
        return detailid;
    }

    public JSONArray getroleLlist() {
        //shopList
        JSONObject shopdate = new JSONObject();
        shopdate.put("shop_id", pp.shopIdZ);
        shopdate.put("shop_name", pp.shopname);
        JSONArray shop_list = new JSONArray();
        shop_list.add(shopdate);
        //shopList
        JSONObject roleList = new JSONObject();
        roleList.put("role_id", pp.roleidJdgw);
        roleList.put("role_name", pp.nameJdgw);
        roleList.put("shop_list", shop_list);

        JSONArray r_dList = new JSONArray();
        r_dList.add(roleList);

        return r_dList;
    }

    public JSONObject getRoleList1(String roleId, String roleName) {
        //shopList
        JSONObject shopdate = new JSONObject();
        shopdate.put("shop_id", pp.shopIdZ);
        shopdate.put("shop_name", pp.shopname);
        JSONArray shop_list = new JSONArray();
        shop_list.add(shopdate);
        //shopList
        JSONObject roleList = new JSONObject();
        roleList.put("role_id", roleId);
        roleList.put("role_name", roleName);
        roleList.put("shop_list", shop_list);

        return roleList;
    }


    //pc预约记录总数
    public int pcAppointmentRecodePage() {
        jc.pcLogin(pp.jdgw, pp.jdgwpassword);
        int num = jc.appointmentRecordManage("", "1", "10", "type", "MAINTAIN").getInteger("total");
        System.out.println("预约记录数：" + num);
        return num;
    }

    public Long getAppointmentId() {
        jc.appletLoginToken(pp.appletTocken);
        Long id = jc.appletAppointmentList("", "10", null).getJSONArray("list").getJSONObject(0).getLong("id");
        return id;
    }

    //小程序客户预约保养次数
    public int pcAppointmentTimes() {
        jc.pcLogin(pp.jdgw, pp.jdgwpassword);
        int num = jc.weChatSleCustomerManage("", "1", "10", "customer_phone", "15037286013").getJSONArray("list").getJSONObject(0).getInteger("appointment_maintain");
        return num;
    }

    //预约看板的预约数
    public Integer appointmentNUmber(int num) {
        jc.appLogin(pp.jdgw, pp.jdgwpassword);
        String month = dt.getMounth(num);
        int day = dt.getDay(num);
        Integer total = 0;
        JSONArray list = jc.pcTimeTableList(month, "MAINTAIN").getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            int list_day = list.getJSONObject(i).getInteger("day");
            if (list_day == day) {
                total = list.getJSONObject(i).getInteger("appointment_number");
                break;
            }
        }
        return total;
    }

    //小程序预约消息数
    public Integer appletmyAppointment() {
        jc.appletLoginToken(pp.appletTocken);
        return jc.appletAppointmentList("MAINTAIN", "20", null).getInteger("total");
    }

    //pc接待管理总数
    public int pcReceptionPage() {
        jc.appLogin(pp.jdgw, pp.jdgwpassword);
        int num = jc.receptionManage("", "1", "10", null, null).getInteger("total");
        return num;
    }

    //app[任务-预约数]
    public int appReceiptPage() {
        jc.appLogin(pp.jdgw, pp.jdgwpassword);
        JSONObject data = jc.appointmentPage(null, 10);
        int total = data.getInteger("total");
        return total;
    }

    //app[任务-接待数]  AppPageScene
    public int appReceptionPage() {
        jc.appLogin(pp.jdgw, pp.jdgwpassword);
        JSONObject data = jc.appreceptionPage(null, 10);
        int total = data.getInteger("total");
        return total;
    }

    //app[任务-接待数]  3.0
    public int appSaleReceptionPage() {
        JSONObject data = jc.AppPageScene(10, null);
        int total = data.getInteger("total");
        return total;
    }


    //app开始接待，并返回接待id
    public Long[] startReception(String carPlate) throws Exception {

        AppStartReception sr = new AppStartReception();
        JSONObject data = jc.appReceptionAdmit(carPlate).getJSONArray("customers").getJSONObject(0);
        Long result[] = new Long[2];
        sr.id = data.getString("customer_id");
        sr.plate_number = carPlate;
        sr.customer_name = data.getString("customer_name");
        sr.customer_phone = data.getString("customer_phone");
        sr.after_sales_type = "MAINTAIN";
        //开始接待
        jc.StartReception(sr);
        //取接待列表id
        JSONObject dd = jc.appreceptionPage(null, 10).getJSONArray("list").getJSONObject(0);
        result[0] = dd.getLong("id");
        String plate_number = dd.getString("plate_number");
        result[1] = dd.getLong("shop_id");
        if (!carPlate.equals(plate_number)) {
            throw new Exception("获取接待id失败");
        }
        return result;
    }

    //pc开始接待，并返回接待id
    public Long pcstartReception(String carPlate) throws Exception {
        jc.pcLogin(pp.jdgw, pp.jdgwpassword);
        AppStartReception sr = new AppStartReception();
        JSONObject data = jc.pcManageReception(carPlate, true).getJSONArray("customers").getJSONObject(0);

        sr.id = data.getString("customer_id");
        sr.plate_number = carPlate;
        sr.customer_name = data.getString("customer_name");
        sr.customer_phone = data.getString("customer_phone");
        sr.after_sales_type = "REPAIR";
//        sr.after_sales_type="MAINTAIN";

        //开始接待
        jc.pcStartReception(sr);
        //取接待列表id
        JSONObject dd = jc.receptionManage("", "1", "10", "", "").getJSONArray("list").getJSONObject(0);
        long receptionID = dd.getLong("id");
        String plate_number = dd.getString("plate_number");
        if (!carPlate.equals(plate_number)) {
            throw new Exception("获取接待id失败");
        }
        System.out.println("接待ID:" + receptionID);
        return receptionID;
    }

    //app今日任务数据
    public int[] appTask() {
        JSONObject data = jc.appTask();
        int sum[] = new int[6];
        //预约
        sum[0] = data.getInteger("surplus_appointment");   //分子
        sum[1] = data.getInteger("all_appointment");     //分母
        //接待
        sum[2] = data.getInteger("after_surplus_reception");  //分子
        sum[3] = data.getInteger("after_all_reception");      //分母

        sum[4] = data.getInteger("surplus_follow");  //分子
        sum[5] = data.getInteger("all_follow");      //分母
        return sum;
    }

    //根据门店名称或接待顾问名称，获取今日数据中待处理数据
    public int[] apptodayDate(String type, String name) {
        //今日数据
        JSONArray todaydate = jc.apptodayDate(type, null, 10).getJSONArray("list");
        String[] both = new String[4];

        for (int i = 0; i < todaydate.size(); i++) {
            JSONObject list_data = todaydate.getJSONObject(i);
            //待处理预约数和
            String name1 = list_data.getString("name");
            if (name1.equals(name)) {
                String pending_appointment = list_data.getString("pending_appointment");
                String[] appointment = pending_appointment.split("/");   //0待处理预约分子，1预约分母，2接待分子，3接待分母
                //接待
                String pending_reception = list_data.getString("pending_reception");
                String[] reception = pending_reception.split("/");
                both = (String[]) ArrayUtils.addAll(appointment, reception);
            }
        }
        int result[] = new int[both.length];
        for (int j = 0; j < both.length; j++) {
            result[j] = Integer.valueOf(both[j]);
        }
        return result;
    }

    //增加车辆，返回车辆id
    public String appletAddCar(String plateNumber) {
        jc.appletAddCar(plateNumber, pp.carModelId);
        String car_id = "";
        JSONArray carData = jc.appletMyCar(pp.carStyleId).getJSONArray("list");
        for (int i = 0; i < carData.size(); i++) {
            String plate_numberAfter = carData.getJSONObject(i).getString("plate_number");
            if (plate_numberAfter.equals(plateNumber)) {
                car_id = carData.getJSONObject(i).getString("id");
                break;
            }
        }
        return car_id;
    }

    public int carListNumber(String carStyleId) {
        JSONObject carData = jc.appletMyCar(carStyleId);
        JSONArray list = carData.getJSONArray("list");
        int count;
        if (list == null || list.size() == 0) {
            count = 0;
        } else {
            count = list.size();
        }
        return count;
    }

    //获取pc活动报名人数
    public int[] jsonActivityNUm(String id) {   //活动id
        registerListVariable sv = new registerListVariable();
        int num[] = new int[4];

        JSONObject ll = jc.registerListFilterManage(sv);
        num[0] = ll.getInteger("total");
        JSONArray list = ll.getJSONArray("list");

        for (int i = 0; i < list.size(); i++) {
            JSONObject temp = list.getJSONObject(i);
            String idt = temp.getString("id");
            if (idt.equals(id)) {
                num[1] = temp.getInteger("total_quota");   //总数
                num[2] = temp.getInteger("register_num");   //已报名
                num[3] = temp.getInteger("passed_num");     //入选
                break;
            }
        }
        return num;
    }

    //applet文章详情
    public int[] appletActivityDetail(String id) {   //活动id

        int num[] = new int[3];   //0

        JSONObject ll = jc.appletArticleDetail(id);
        num[0] = ll.getInteger("total_quota");    //全部名额
        num[1] = ll.getInteger("register_num");   //已报名名额
        JSONArray list = ll.getJSONArray("list");  //报名成功名单输
        num[2] = list.size();
        return num;
    }

    public int[] getPage(int total) {
        int page[] = new int[2];
        if (total == 0) {
            page[0] = 1;
            page[1] = 0;
        } else if (total % 10 == 0) {
            page[0] = total / 10;
            page[1] = 10;
        } else {
            page[0] = total / 10 + 1;
            page[1] = total % 10 - 1;
        }
        System.out.println(page[0] + "index:" + page[1]);
        return page;
    }

    //获取预约时段id
    public Long getTimeId(Long shop_id, Long car_id, String data) {
        JSONArray list = jc.appletmaintainTimeList(shop_id, car_id, data, "MAINTAIN").getJSONArray("list");
        Long id = 0L;
        for (int i = 0; i < list.size(); i++) {
            String is_full = list.getJSONObject(i).getString("is_full");
            if (is_full.equals("false")) {
                id = list.getJSONObject(i).getLong("id");
                break;
            }
        }
        return id;
    }

    //小程序预约，返回预约id
    public Long appletAppointment(int num) {
        //小程序预约
        jc.appletLoginToken(pp.appletTocken);
        AppletAppointment pm = new AppletAppointment();
        pm.car_id = pp.car_idA;
        pm.appointment_name = "自动吕";
        pm.appointment_phone = "13436941018";
        pm.shop_id = Long.parseLong(pp.shopIdZ);
        pm.staff_id = pp.userid;
        pm.time_id = getTimeId(pm.shop_id, pm.car_id, dt.getHistoryDate(num));
        pm.type = "MAINTAIN";

        Long appointmentId = jc.appletAppointment(pm).getLong("id");
        return appointmentId;
    }

    //创建活动
    public Long creteArticle(JSONArray voucher_list, String voucher_receive_type) {
        String article_bg_pic = file.texFile(pp.filepath);
        String path = jc.pcFileUpload(article_bg_pic, true, 1.5).getString("pic_path");
        JSONArray picList = new JSONArray();
        picList.add(path);

        PcCreateActile er = new PcCreateActile();
        er.title = "1234" + dt.getHHmm(0);
        er.pic_type = "ONE_BIG";
//        er.content="\"<p>890089008900890089008900</p><div class=\"media-wrap image-wrap\"><img src=\"http://retail-huabei2.oss-cn-beijing.aliyuncs.com/business-porsche/dev/general_temp/fac4bd9d-f898-4f97-8926-73812bd20667?Expires=4730163216&OSSAccessKeyId=LTAI4G4xNBGMWuAV9dBwkZya&Signature=hInQW6TDZijOmenWksfDC%2BPUOR8%3D\"/></div><p></p>";
        er.content = "\"<p>890089008900890089008900</p>";
        er.label = "SELL_WELL";
        er.content_type = "ACTIVITY";
        er.total_quota = "90";
        er.register_start_date = dt.getHistoryDate(0);
        er.register_end_date = dt.getHistoryDate(0);
        er.start_date = dt.getHistoryDate(0);
        er.end_date = dt.getHistoryDate(0);
        er.address = "海淀区中关村soho";
        er.is_can_maintain = true;
        er.is_voucher = true;
        er.voucher_list = voucher_list;
        er.voucher_receive_type = voucher_receive_type;  //"ARTICLE_BUTTON"  页面领取  //SIGN_UP   报名成功后领取
        er.voucher_get_use_days = "365";
        er.pic_list = picList;
        Long id = jc.pccreateActile(er).getLong("id");
        return id;
    }

    /**
     * 创建活动
     *
     * @param totalQuota 报名名额
     * @return 活动id
     */
    public Long creteArticle(String totalQuota) {
        String article_bg_pic = file.texFile(pp.filepath);
        String path = jc.pcFileUpload(article_bg_pic, true, 1.5).getString("pic_path");
        JSONArray picList = new JSONArray();
        picList.add(path);
        PcCreateActile er = new PcCreateActile();
        er.title = "1234" + dt.getHHmm(0);
        er.pic_type = "ONE_BIG";
        er.content = "\"<p>890089008900890089008900</p>";
        er.label = "SELL_WELL";
        er.content_type = "ACTIVITY";
        er.total_quota = totalQuota;
        er.register_start_date = dt.getHistoryDate(0);
        er.register_end_date = dt.getHistoryDate(0);
        er.start_date = dt.getHistoryDate(0);
        er.end_date = dt.getHistoryDate(0);
        er.address = "海淀区中关村soho";
        er.is_can_maintain = true;
        er.pic_list = picList;
        return jc.pccreateActile(er).getLong("id");
    }

    /**
     * @description :获取卡券列表中总数
     * @date :2020/12/16 17:18
     **/

    public Integer getVoucherTotal() {
        JSONObject data = jc.appletVoucherList(null, "GENERAL", 20);
        JSONObject lastValue = data.getJSONObject("last_value");
        JSONArray list = data.getJSONArray("list");
        int size = list.size();
        Integer count = size;   //计数器
        int i = 0;
        while (size != 0) {
            JSONObject temp = jc.appletVoucherList(lastValue, "GENERAL", 20);
            lastValue = temp.getJSONObject("last_value");
            list = temp.getJSONArray("list");
            size = list.size();
            count = count + size;
            i = i + 1;
        }
        return count;
    }

    //获取套餐个数
    public Integer getpackgeTotal() {
        JSONObject data = jc.appletpackageList(null, "GENERAL", 20);
        String lastValue = data.getString("last_value");
        JSONArray list = data.getJSONArray("list");
        int size = list.size();
        Integer count = size;   //计数器
        int i = 0;
        while (size != 0) {
            JSONObject temp = jc.appletpackageList(lastValue, "GENERAL", 20);
            lastValue = temp.getString("last_value");
            list = temp.getJSONArray("list");
            size = list.size();
            count = count + size;
            i = i + 1;
        }
        return count;
    }

    //获取小程序可用核销码
    public String[] voucherName() throws Exception {
        jc.appletLoginToken(pp.appletTocken);
        JSONArray list = jc.appletVoucherList(null, "GENERAL", 20).getJSONArray("list");
        String voucher_code[] = {"123", "123", ""};
        for (int i = 0; i < list.size(); i++) {
            JSONObject data = list.getJSONObject(i);
            String isLimitCar = data.getString("is_limit_car");
            String status_name = data.getString("status_name");
            if (isLimitCar.equals("false") && (status_name.equals("快过期") || status_name.equals("未使用"))) {
                voucher_code[0] = data.getString("voucher_code");
                voucher_code[1] = data.getString("title");
                voucher_code[2] = data.getString("id");
                break;
            }
        }
        if (voucher_code.equals("123")) {
            throw new Exception("小程序卡券不足，需领取卡券");
        }
        return voucher_code;
    }

    //app 跟进列表数量
    public Integer followPageNumber() {
        JSONObject lastValue = null;
        JSONArray list;
        int count = 0;
        do {
            JSONObject data = jc.AppPageV3Scene(10, lastValue, null);
            lastValue = data.getJSONObject("last_value");
            list = data.getJSONArray("list");
            count = count + list.size();
            System.out.println("listsize:" + list.size());
        } while (list.size() == 10);
        System.out.println("count:" + count);
        return count;
    }

}
