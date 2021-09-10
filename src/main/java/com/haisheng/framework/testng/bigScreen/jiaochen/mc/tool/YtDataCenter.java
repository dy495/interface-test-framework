package com.haisheng.framework.testng.bigScreen.jiaochen.mc.tool;

import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.DataProvider;

public class YtDataCenter {


    @DataProvider(name = "editErrorInfo")
    public Object[] editErrorInfo(){
        return new Object[][]{
                {"校验购车时间：必填","estimate_buy_car_date",null,"1001"},
                {"校验姓名：必填","customer_name",null,"1001"},
                {"校验联系方式：必填","customer_phone",null,"1001"},
                {"校验购车车型：必填","intention_car_model_id",null,"1001"},
                {"校验性别：必填","sex_id",null,"1001"},
                //{"校验意向类型：必填","customer_source",null,"1001"},
                //{"校验姓名：长度51字","customer_name",FastContent.NAME51,"1001"},//X    "message":"系统繁忙，请稍后再试！！"
                //{"校验姓名：一个空格","customer_name"," ","1001"}, //X   success
                {"校验姓名：空字符","customer_name","","1001"},
                {"校验联系方式：长度10","customer_phone","15"+ CommonUtil.getRandom(8),"1001"},
                {"校验联系方式：长度12","customer_phone","15"+CommonUtil.getRandom(10),"1001"},
                {"校验联系方式：11位非手机号","customer_phone","00"+CommonUtil.getRandom(10),"1001"},
                {"校验联系方式：11位中文","customer_phone","阿坝县的风格解开了破了","1001"},
                {"校验联系方式：11位英文","customer_phone","AbCdEfGhiJk","1001"},
                {"校验联系方式：11位符号","customer_phone",")(_{[';@$% `","1001"},
                //{"校验联系方式：系统存在的手机号", "customer_phone", "15022399925", "1001"},
                {"校验性别：格式", "sex_id", "3", "1001"},
                {"校验购车车型：系统中不存在的车型", "intention_car_model_id", "0000", "1001"},
                //{"校验购车车型：门店中不存在的车型","intention_car_model_id","716","1001"},    //X   能创建  接口问题，人工无法复现
                {"校验购车时间：早于今天", "estimate_buy_car_date", "2021-01-01", "1001"},
                {"修改正常情况", "", "", "1000"},
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
//                {"内容10个字", FastContent.CONTENT10, "1000"},
                {"内容1000个字", FastContent.CONTENT1000, "1000"},
        };
    }

    @DataProvider(name = "exportPages")
    public Object[] exportPages() {
        return new String[][]{
                {"car", "/car-platform/pc/customer-manage/pre-sale-customer/buy-car/page/export", "成交记录"},
                {"car", "/car-platform/pc/pre-sales-reception/export", "销售接待记录"},
                {"car", "/car-platform/pc/manage/evaluate/v4/export", "销售接待线下评价"},
                {"car", "/car-platform/pc/record/import-record/export", "导入记录"},
                {"car", "/car-platform/pc/record/export-record/export", "导出记录"},
                {"car", "/car-platform/pc/record/login-record/export", "登录记录"},
                {"car", "/car-platform/pc/shop/export", "门店管理"},
                {"car", "/car-platform/pc/brand/export", "品牌管理"},
                {"car", "/car-platform/pc/brand/car-style/export", "车系列表"},
                {"car", "/car-platform/pc/brand/car-style/car-model/export", "车型列表"},
                {"control", "/intelligent-control/pc/manage/voice/evaluation/export", "销售语音评鉴记录"}
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

    @DataProvider(name = "qrCodeInfo")
    public Object[] qrCodeInfo(){
        return new Object[][]{
                //{"校验姓名：必填","customer_name",null,"1001"},
                //{"校验姓名：空字符","customer_name","","1001"},
                //{"校验姓名：长度51字","customer_name",FastContent.NAME51,"1001"},
                {"校验姓名：长度50字","customer_name",FastContent.NAME50,"1000"},
                //{"校验性别：必填","sex_id",null,"1001"},
        };
    }


    @DataProvider(name = "AfterErrorInfo")
    public Object[] afterEditErrorInfo(){
        return new Object[][]{
                {"校验姓名：必填","customer_name",null,"1001"},
                {"校验联系方式：必填","customer_phone",null,"1001"},
                //{"校验性别：必填","sex_id",null,"1001"}, //前端有默认值
                //{"校验姓名：长度51字","customer_name",FastContent.NAME51,"1001"},//X    "message":"系统繁忙，请稍后再试！！"
                //{"校验姓名：一个空格","customer_name"," ","1001"}, //X   success
                {"校验姓名：空字符","customer_name","","1001"},
                {"校验联系方式：长度10","customer_phone","15"+ CommonUtil.getRandom(8),"1001"},
                {"校验联系方式：长度12","customer_phone","15"+CommonUtil.getRandom(10),"1001"},
                {"校验联系方式：11位非手机号","customer_phone","00"+CommonUtil.getRandom(10),"1001"},
                {"校验联系方式：11位中文","customer_phone","阿坝县的风格解开了破了","1001"},
                {"校验联系方式：11位英文","customer_phone","AbCdEfGhiJk","1001"},
                {"校验联系方式：11位符号","customer_phone",")(_{[';@$% `","1001"},
                {"校验性别：格式", "sex_id", "3", "1001"},
                {"车牌号格式：7位,汉字+纯数字","plate_number","京"+CommonUtil.getRandom(6),"1001"},
                {"车牌号格式：9位,汉字+大写字母+纯数字","plate_number","浙A"+CommonUtil.getRandom(7),"1001"},
                {"车牌号格式：6位,汉字+大写字母+纯数字","plate_number","浙B"+CommonUtil.getRandom(4),"1001"},
                {"修改正常情况", "", "", "1000"}
        };
    }

}
