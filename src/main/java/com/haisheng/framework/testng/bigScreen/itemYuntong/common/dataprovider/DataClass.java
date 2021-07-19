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
                {"customer_phone", null},
                {"customer_phone", ""},
                {"customer_phone", "中文电话号"},
                {"customer_phone", "english"},
                {"customer_phone", "数字+中文；英文"},
                {"customer_phone", "1337316680"},
                {"customer_phone", "133731668061"},
                {"customer_name", EnumDesc.DESC_BETWEEN_200_300.getDesc()},
                {"customer_name", null},
                {"customer_name", ""},
                //系统异常
//                {"salesId", "不存在的销售"},
                //系统异常
//                {"salesId", null},
                //系统异常
//                {"salesId", ""},
                {"sex", ""},
                {"sex", "4"},
                {"sex", null},
                {"customer_type", ""},
                {"customer_type", null},
                {"customer_type", "不存在的客户类型"},
                {"car_style_id", ""},
                {"car_style_id", null},
                {"car_model_id", ""},
                {"car_model_id", null},
                {"shop_id", null}
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
                {"customer_phone", null},
                {"customer_phone", ""},
                {"customer_phone", "中文电话号"},
                {"customer_phone", "english"},
                {"customer_phone", "数字+中文；英文"},
                {"customer_phone", "1337316680"},
                {"customer_phone", "133731668061"},
                {"salesId", "不存在的销售"},
                {"salesId", null},
                {"salesId", ""},
                {"vehicle_chassis_code", "WEQRDFTGYRDCVBFRE"},
                //创建成功了
//                {"vehicle_chassis_code", "12345678901234567"},
                {"vehicle_chassis_code", "AAASSDFD3208758189"},
                {"vehicle_chassis_code", "AAASSDFD32087581"},
                {"vehicle_chassis_code", "AAASSDFD32087;819"},
                {"vehicle_chassis_code", null},
                {"vehicle_chassis_code", ""},
                {"customer_name", EnumDesc.DESC_BETWEEN_200_300.getDesc()},
                {"customer_name", null},
                {"customer_name", ""},
                {"sex", ""},
                {"sex", "4"},
                {"sex", null},
                {"customer_type", ""},
                {"customer_type", null},
                {"customer_type", "不存在的客户类型"},
                {"shop_id", null},
                {"car_style_id", ""},
                {"car_style_id", null},
                {"car_model_id", ""},
                {"car_model_id", null},
                {"purchase_car_date", DateTimeUtil.addDayFormat(new Date(), 1)},
                {"purchase_car_date", "2006-01-07"},
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
