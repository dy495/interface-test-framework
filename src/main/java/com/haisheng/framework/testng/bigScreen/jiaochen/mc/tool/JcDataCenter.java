package com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool;

import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.DataProvider;

public class JcDataCenter {

    @DataProvider(name = "createErrorInfo")
    public Object[] createErrorInfo() {
        String phone = "15" + CommonUtil.getRandom(9);
        return new Object[][]{
                //{"校验姓名：必填","customer_name",null,"1001"},  //X   "message":"系统繁忙，请稍后再试！！"
                //{"校验姓名：长度51字","customer_name",FastContent.NAME51,"1001"},//X    "message":"系统繁忙，请稍后再试！！"
                //{"校验姓名：空字符","customer_name","","1001"},  //X   能创建  接口问题，人工无法复现
                //{"校验姓名：一个空格","customer_name"," ","1001"}, //X   success
                //{"校验联系方式：必填","customer_phone",null,"1001"},  //X   能创建  接口问题，人工无法复现
                // {"校验联系方式：长度10","customer_phone","15"+numRandom(8),"1001"}, //X   success
                //{"校验联系方式：长度12","customer_phone","15"+numRandom(10),"1001"}, //X   success
                //{"校验联系方式：11位非手机号","customer_phone","00"+numRandom(10),"1001"}, //X   success
                //{"校验联系方式：11位中文","customer_phone","阿坝县的风格解开了破了","1001"}, //X   success
                //{"校验联系方式：11位英文","customer_phone","AbCdEfGhiJk","1001"}, //X   success
                //{"校验联系方式：11位符号","customer_phone",")(_{[';@$% `","1001"}, //X   success
                {"成功创建,手机号固定,前置条件", "customer_phone", phone, "1000"},
                {"校验联系方式：同一个手机号接待中再创建接待", "customer_phone", phone, "1001"}, //"当前客户正在接待中，请勿重复接待"
                //{"校验性别：必填","sex_id",null,"1001"},  // "message":"系统繁忙，请稍后再试！！"
                {"校验性别：格式", "sex_id", "3", "1001"}, //"性别不正确"
                //{"校验购车车型：必填","intention_car_model_id",null,"1001"},  //X   能创建  接口问题，人工无法复现
                {"校验购车车型：不存在的车型", "intention_car_model_id", "0000", "1001"}, // "车型不存在"
                //{"校验购车车型：权限外","intention_car_model_id","716","1001"}, //X   能创建（车型为-）  接口问题，人工无法复现
                //{"校验购车时间：必填","estimate_buy_car_time",null,"1001"},  //X   能创建  接口问题，人工无法复现
                {"校验购车时间：早于今天", "estimate_buy_car_time", "2021-01-01", "1001"}, //"预计购车时间不能小于今天"
        };
    }


    @DataProvider(name = "editErrorInfo")
    public Object[] editErrorInfo(){
        return new Object[][]{
                {"校验购车时间：必填","estimate_buy_car_date",null,"预计购车时间不能为空"},
                {"校验姓名：必填","customer_name",null,"客户名称不能为空"},
                {"校验联系方式：必填","customer_phone",null,"客户手机号不能为空"},
                {"校验购车车型：必填","intention_car_model_id",null,"意向车型不能为空"},
                {"校验性别：必填","sex_id",null,"客户性别不能为空"},
                //{"校验姓名：长度51字","customer_name",FastContent.NAME51,"客户名称格式不正确"},//X    "message":"系统繁忙，请稍后再试！！"
                //{"校验姓名：一个空格","customer_name"," ","客户名称不能为空"}, //X   success
                {"校验姓名：空字符","customer_name","","客户名称不能为空"},
                {"校验联系方式：长度10","customer_phone","15"+ CommonUtil.getRandom(8),"客户手机号格式不正确"},
                {"校验联系方式：长度12","customer_phone","15"+CommonUtil.getRandom(10),"客户手机号格式不正确"},
                {"校验联系方式：11位非手机号","customer_phone","00"+CommonUtil.getRandom(10),"客户手机号格式不正确"},
                {"校验联系方式：11位中文","customer_phone","阿坝县的风格解开了破了","客户手机号格式不正确"},
                {"校验联系方式：11位英文","customer_phone","AbCdEfGhiJk","客户手机号格式不正确"},
                {"校验联系方式：11位符号","customer_phone",")(_{[';@$% `","客户手机号格式不正确"},
                {"校验联系方式：系统存在的手机号", "customer_phone", "15022399925", "当前手机号已被其他客户绑定"},
                {"校验性别：格式", "sex_id", "3", "性别不正确"},
                {"校验购车车型：系统中不存在的车型", "intention_car_model_id", "0000", "意向车型不存在"},
                //{"校验购车车型：门店中不存在的车型","intention_car_model_id","716","意向车型不存在"},    //X   能创建  接口问题，人工无法复现
                {"校验购车时间：早于今天", "estimate_buy_car_date", "2021-01-01", "预计购车时间不能小于今天"},
                {"修改正常情况", "", "", "success"},
        };
    }

    @DataProvider(name = "errorBuyCar")
    public Object[] errorBuyCar(){
        return new Object[][]{
                {"校验购买车型：必填","car_model",null,"购买车辆车型不能为空"},
                {"校验购车车型：系统中不存在的车型", "car_model", "0000", "车型不存在，请重新选择"},
                //{"校验购车车型：门店中不存在的车型", "car_model","716","车型不存在，请重新选择"},      //X   能创建  接口问题，人工无法复现
                {"校验底盘号：底盘号16位英文+数字", "vin", "aaaabbbc" + CommonUtil.getRandom(8), "底盘号格式不正确"},
                {"校验底盘号：非底盘号格式", "vin", "aaaa你bbc年十大十大啊；。", "底盘号格式不正确"},
                {"校验底盘号：底盘号18位英文+数字", "vin", "aaaabbbc" + CommonUtil.getRandom(10), "底盘号格式不正确"},
                {"校验底盘号：系统存在的底盘号", "vin", "ABC12345678901234", "数据已存在，请勿重复录入"}
        };
    }
    @DataProvider(name = "addPlate")
    public Object[] addPlate(){
        return new Object[][]{
                {"车牌号格式：7位,汉字+纯数字","京"+CommonUtil.getRandom(6),"车牌号格式不正确"},
                {"车牌号格式：9位,汉字+大写字母+纯数字","浙A"+CommonUtil.getRandom(7),"车牌号格式不正确"},
                {"车牌号格式：6位,汉字+大写字母+纯数字","浙B"+CommonUtil.getRandom(4),"车牌号格式不正确"}
        };
    }
    @DataProvider(name = "remark")
    public Object[] remark(){
        return new Object[][]{
                {"内容9个字", FastContent.CONTENT9, "1001"},
                {"内容0个字符", "", "1001"},
                {"内容1001个字", FastContent.CONTENT1000+"字", "1001"},
                {"内容10个字", FastContent.CONTENT10, "1000"},
                {"内容1000个字", FastContent.CONTENT1000, "1000"},
        };
    }

    @DataProvider(name = "evaluateRemark")
    public Object[] evaluateRemark(){
        return new Object[][]{
              //  {"内容9个字", FastContent.CONTENT9, "1001"}, // 前端有校验
                {"内容0个字符", "", "1001"},
                {"内容1001个字", FastContent.CONTENT1000+"字", "1001"},
                {"内容10个字", FastContent.CONTENT10, "1000"},
                {"内容1000个字", FastContent.CONTENT1000, "1000"},
        };
    }

    @DataProvider(name = "preFalse")
    public Object[] preFalse(){
        return new Object[][]{
                // {"正常名字51字，手机不填", FastContent.NAME51,""}, // 系统繁忙
                {"正常名字1字，手机号11数字，非手机号格式", "1","2"+CommonUtil.getRandom(11)},
                {"正常名字1字，手机号12数字", "1",CommonUtil.getRandom(12)},
                {"正常名字0字", "","13"+CommonUtil.getRandom(9)}
        };
    }

    @DataProvider(name = "customerLevel")
    public Object[] customerLevel(){
        DateTimeUtil dt = new DateTimeUtil();
        return new Object[][]{
                {dt.getHistoryDate(7), "H"},
                {dt.getHistoryDate(8), "A"},
                {dt.getHistoryDate(15), "A"},
                {dt.getHistoryDate(16), "B"},
                {dt.getHistoryDate(30), "B"},
                {dt.getHistoryDate(31), "C"},
        };
    }


}
