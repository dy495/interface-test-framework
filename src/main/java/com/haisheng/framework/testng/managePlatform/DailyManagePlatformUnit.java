package com.haisheng.framework.testng.managePlatform;


/**
 * @author yu
 * @date 2020.08.10
 */


public class DailyManagePlatformUnit {

    //日常环境【接口测试】专用应用
    public String appId = "0d28ec728799";
    //日常环境【接口测试】专用品牌
    public String brandId = "638";

    //QA管理后台日常回归测试scope，case无发获得新建的scopeid时默认绑定的scope
    public String nodeId = "41280";
    //边缘服务器集群ID,固定
    public String clusterNodeId = "538";
    public String clusterAlias = "QA-算法测评专用";

    //线下设备id，用于信息打印
    public String dailyDeviceId = "default";
}
