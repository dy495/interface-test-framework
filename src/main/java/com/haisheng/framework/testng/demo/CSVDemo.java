package com.haisheng.framework.testng.demo;

import com.haisheng.framework.testng.service.CsvDataProvider;
import org.testng.annotations.Test;

public class CSVDemo {

    @Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class)
    public void swimlane_wptDbMonitor(String id, String service, String user, String passwd, String jdbcDriver, String url) {

        System.out.println("id: " +id);
        System.out.println("service: " +service);
        System.out.println("user: " +user);
        System.out.println("url: " +url);
    }

    @Test
    public void test() {
        System.out.println("hhhhhhhhh");
    }

}
