package com.haisheng.framework.testng.bigScreen.jiaochen.gly;
import org.testng.annotations.DataProvider;

public class ListConstant {


    /**
     * @description:保养配置列表
     * @author: gly
     * @time: 2020-01-07
     */
    @DataProvider(name="carModel-provider")
    public static Object[][] carModelConstant(){
        return new String[][]{
                {"id"},
                {"brand_name"},
                {"manufacturer"},
                {"style_name"},
                {"model"},
                {"year"},
                {"price"},
                {"status"},
        };
    }




}
