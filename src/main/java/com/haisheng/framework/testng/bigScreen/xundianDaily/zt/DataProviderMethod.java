package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import org.testng.annotations.DataProvider;

public class DataProviderMethod {

    @DataProvider(name = "device_id")
    public Object[] cameraId() {

        return new String[][]{
                {"8061349193417728"},
                {"8097818264503296"},
                {"8058611994690560"}

        };
    }

    @DataProvider(name = "type")
    public Object[] type_1() {

        return new String[][]{
                {"NORMAL"},
                {"COMMUNITY"},
                {"PLAZA"},
                {"FLAGSHIP"},

        };
    }
}
