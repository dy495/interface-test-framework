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

    @Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class)
    public void yuhaisheng_demo2(String id, String u1, String u2, String u3) {

        System.out.println("id: " + id);
        System.out.println("u1: [" + u1 + "]");
        System.out.println("u2: [" + u2 + "]");
        System.out.println("u3: [" + u3 + "]");
    }

}
