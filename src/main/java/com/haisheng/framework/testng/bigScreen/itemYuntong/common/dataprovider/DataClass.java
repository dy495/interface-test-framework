package com.haisheng.framework.testng.bigScreen.itemYuntong.common.dataprovider;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumDesc;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.DataProvider;

import java.util.Date;

public class DataClass {
    /**
     * 创建潜客
     *
     * @return object
     */
    @DataProvider(name = "createCustomerAbnormalParam")
    public Object[] createCustomerAbnormalParam() {
        return new Object[][]{
                {"customer_phone", null, "客户手机不允许为空"},
                {"customer_phone", "", "客户手机不允许为空"},
                {"customer_phone", "中文电话号", "手机号格式不正确"},
                {"customer_phone", "english", "手机号格式不正确"},
                {"customer_phone", "数字+中文；英文", "手机号格式不正确"},
                {"customer_phone", "1337316680", "手机号格式不正确"},
                {"customer_phone", "133731668061", "手机号格式不正确"},
                {"customer_name", EnumDesc.DESC_BETWEEN_200_300.getDesc(), "客户名称不能多于50个字"},
                {"customer_name", null, "客户名称不允许为空"},
                {"customer_name", "", "客户名称不允许为空"},
                //系统异常
//                {"salesId", "不存在的销售"},
                //系统异常
//                {"salesId", null},
                //系统异常
//                {"salesId", ""},
                {"sex", "", "性别不可为空"},
                {"sex", "4", "性别不正确"},
                {"sex", null, "性别不可为空"},
                {"customer_type", "", "车主类型不可为空"},
                {"customer_type", null, "车主类型不可为空"},
                {"customer_type", "不存在的客户类型", "客户类型不存在"},
                {"car_style_id", "", "车系不能为空"},
                {"car_style_id", null, "车系不能为空"},
                {"car_model_id", "", "车型不能为空"},
                {"car_model_id", null, "车型不能为空"},
//                {"shop_id", null, "门店id不能为空"}
        };
    }

    /**
     * 创建成交记录
     *
     * @return object
     */
    @DataProvider(name = "createCustomerOrderAbnormalParam")
    public Object[] createCustomerOrder() {
        return new Object[][]{
                {"customer_phone", null, "客户手机不允许为空"},
                {"customer_phone", "", "客户手机不允许为空"},
                {"customer_phone", "中文电话号", "手机号格式不正确"},
                {"customer_phone", "english", "手机号格式不正确"},
                {"customer_phone", "数字+中文；英文", "手机号格式不正确"},
                {"customer_phone", "1337316680", "手机号格式不正确"},
                {"customer_phone", "133731668061", "手机号格式不正确"},
                {"salesId", "不存在的销售", "销售不存在"},
                {"salesId", null, "销售不存在"},
                {"salesId", "", "销售不存在"},
                //创建成功了
//                {"vehicle_chassis_code", "12345678901234567"},
                {"vehicle_chassis_code", "WEQRDFTGYRDCVBFRE", "底盘号格式不正确"},
                {"vehicle_chassis_code", "AAASSDFD3208758189", "底盘号格式不正确"},
                {"vehicle_chassis_code", "AAASSDFD32087581", "底盘号格式不正确"},
                {"vehicle_chassis_code", "AAASSDFD32087;819", "底盘号格式不正确"},
                {"vehicle_chassis_code", null, "底盘号不能为空"},
                {"vehicle_chassis_code", "", "底盘号不能为空"},
                {"customer_name", EnumDesc.DESC_BETWEEN_200_300.getDesc(), "客户名称不能多于50个字"},
                {"customer_name", null, "客户名称不允许为空"},
                {"customer_name", "", "客户名称不允许为空"},
                {"sex", "", "性别不可为空"},
                {"sex", "4", "性别不正确"},
                {"sex", null, "性别不可为空"},
                {"customer_type", "", "车主类型不可为空"},
                {"customer_type", null, "车主类型不可为空"},
                {"customer_type", "不存在的客户类型", "客户类型不存在"},
                {"shop_id", null, "门店id不能为空"},
                {"car_style_id", "", "车系不能为空"},
                {"car_style_id", null, "车系不能为空"},
                {"car_model_id", "", "车型不能为空"},
                {"car_model_id", null, "车型不能为空"},
                {"purchase_car_date", DateTimeUtil.addDayFormat(new Date(), 1), "购车日期必须小于等于当前日期并且大于等于2006-01-08"},
                {"purchase_car_date", "2006-01-07", "购车日期必须小于等于当前日期并且大于等于2006-01-08"},
                //系统异常
//                {"purchase_car_date", ""},
//                {"purchase_car_date", null}
        };
    }

    /**
     * 星级等级
     *
     * @return object
     */
    @DataProvider(name = "starType")
    public Object[] getStarData() {
        return new Object[][]{
                {"one", 1, "一星"},
                {"two", 2, "二星"},
                {"three", 3, "三星"},
                {"four", 4, "四星"},
                {"five", 5, "五星"}
        };
    }

    @DataProvider(name = "receptionType")
    public Object[] getReceptionTypeName() {
        return new Object[][]{
                {"欢迎接待", 100},
                {"个性需求", 200},
                {"新车推荐", 300},
                {"试乘试驾", 400},
                {"车辆提案", 500},
        };
    }

    @DataProvider(name = "receptionWord")
    public Object[] getReceptionWord() {
        return new String[]{
                "不卖裸车/不允许跨区域",
                "服务费/验车上牌费/手续费/现金/微信转",
                "必须上保险/一定上保险/强制上保险/必须",
        };
    }
}
