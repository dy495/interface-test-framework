package com.haisheng.framework.testng.study.groups;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

public class TestGroup {

    @Test(groups = "selenium-test")
    public void runSelenium(){
        System.out.println("runSelenium");
    }

    @Test(groups = "selenium-test")
    public void runSelenium1(){
        System.out.println("runSelenium1");
    }

    @BeforeGroups("database")
    public void setupDB(){
        System.out.println("setupDB");
    }

    @AfterGroups("database")
    public void cleanupDB(){
        System.out.println("cleanupDB");
    }

    @Test(groups = "database")
    public void testConnectOracle(){
        System.out.println("testConnectOracle");
    }

    @Test(groups = "database")
    public void testConnectMysql(){
        System.out.println("testConnectMysql");
    }

    @Test(dependsOnGroups = {"selenium-test","database"})
    public void runfinal(){
        System.out.println("runfinal");
    }

}
