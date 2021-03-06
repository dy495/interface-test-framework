package com.haisheng.framework.testng.bigScreen.jiaochenonline.xmf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAppletToken;

public class PublicParamOnline {

    //3.0
    public Long brandId = 1L;  //TODO:
    public String user;
    public String userPassword;
    public String userRoleId = "4580";
    public String voucherIdevluate = "7110";  //评价配置使用的卡券id

    public int num = 5;
    public String roleId = "395";
    public String shopId = "-1";   //门店Id
    public String shopIdZ = "20032";   //门店Id 中关村店

    public String reception_sale_id = "石矶娘娘";
    public String name = "创建角色xia";
    public String customerPhone = "15037286012";
    public String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/jcBase64.txt";

    public String shichang = "";
    public String shichangPassword = "";

    public String roleName = "创建角色xia";
    public String carModelId = "19655";
    public String carModel = "AB";
    public String car_id = "78";             //小程序车id
    public String carPlate = "浙MNB789";      //编辑小程序车牌号
    public String carPlate7 = "浙ASD124";      //编辑小程序车牌号
    //预约使用车辆
    public Long car_idA = 206834L;
    public String shopName = "中关村店简";
    public String modelIdAppointment = "19655";
    public String carStyleId = "78";
    public String carStyleName = "";

    public String gwname = "15711200001"; //单个店的客户的数据   //登录账号

    public String gwphone = "15711200001"; //单个店的客户的数据   //登录账号
    public String gwpassword = "000000"; //单个店的客户的数据  登录密码

    public String jdgw = "15037286012";  //xx ,属于中关村店
    public String roleidJdgw = "2227";
    public String nameJdgw = "";
    public String jdgw2 = "15037286011";  //xx ,属于中关村店
    public String jdgwpassword = "000000"; //单个店的客户的数据  登录密码
    public String jdgwName = "接待顾问x"; //单个店的客户的数据  登录密码

    public String dzphone = "15037286014";   //中关村店长
    public String dzroleId = "439";
    public String dzcode = "000000";

    //---------2.0---------
    public String String_50 = "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十";
    public String String_20 = "一二三四五六七八九十一二三四五六七八九十";
    public String String_200 = "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十";
    public String StoreCommodityId = "1902";  //TODO:编辑需要的商品套餐的名字
    public String importFilepath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/测试.xlsx";   //导入工单文件路径
    public String importFilepath2 = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/importfile2.xlsx";   //导入工单文件路径
    public String importFilepath3 = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/importPotentialCustomerfile.xlsx";   //导入工单文件路径
    //其他小程序用户token和车牌号信息
    public String getAppletTokenOther = "R4M7UpQ2+Q3L0QbxGA32iA==";
    public String carPlateOther = "浙ZXCV123";
    public String userid = "uid_2960c1cf"; //接待顾问x 15037286011 的userId;
    public String userid2 = "uid_bf9d24ef"; //接待顾问2 6013的userId;

    //3.0
    public String useridxs = "uid_2960c1cf"; //销售的userId;

    public int orderId = 10; // 订单Id
    public String filepath11 = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/1_1.jpg";

    //---------2.0---------
    public String appletToken = EnumAppletToken.JC_LXQ_ONLINE.getToken();
    public JSONArray coordinate = getCoordinate();
    public Long voucherId = 81L;  //经纬度

    public Long parentRoleId = 395L;
    public JSONArray authList = getAuthList();
    public JSONArray vouchers = getvouchersList();
    public JSONArray vouchers2 = voucherSingle();

    public JSONArray getvouchersList() {   //创建精品套餐时创建的卡券数组
        JSONObject vouletList = new JSONObject();
        vouletList.put("effective_day", 1);
        vouletList.put("voucher_count", 1);
        vouletList.put("voucher_id", 69);
        vouletList.put("voucher_name", "测试专用优惠券");
        JSONArray moduleId2 = new JSONArray();
        moduleId2.add(vouletList);
        return moduleId2;
    }

    public JSONArray voucherSingle() {
        JSONArray moduleId2 = new JSONArray();
        moduleId2.add(69);
        return moduleId2;
    }

    public JSONArray getAuthList() {
        JSONArray moduleId2 = new JSONArray();
        moduleId2.add(136);
        moduleId2.add(138);
        moduleId2.add(138);
        moduleId2.add(245);
        return moduleId2;
    }

    public JSONArray getCoordinate() {
        JSONArray dd = new JSONArray();
        dd.add(39.95933);
        dd.add(116.29845);
        return dd;
    }
}
