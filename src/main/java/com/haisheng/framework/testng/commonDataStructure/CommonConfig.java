package com.haisheng.framework.testng.commonDataStructure;

public class CommonConfig {
    //宏变量，用于被替换真实参数
    public final String JOB_NAME = "JOB-NAME";
    public final String TEST_PRODUCT = "YOUR-TEST-PRODUCT";
    public final String CASE_DESC = "CASE_DESCRIPTION";
    public final String CASE_FAIL = "CASE_FAIL";
    public final String CASE_NAME = "CASE_NAME";

    //网关信息
    public String gateway = "http://dev.api.winsenseos.cn/retail/api/data/biz";
    public String gatewayDevice = "http://dev.api.winsenseos.cn/retail/api/data/device";
    public String gatewayDeviceOnline = "http://api.winsenseos.com/retail/api/data/device";

    //管理后台的应用信息
    public String uid = "uid_7fc78d24";
    public String appId = "097332a388c2";
    public String ak = "77327ffc83b27f6d";
    public String sk = "7624d1e6e190fbc381d0e9e18f03ab81";

    //checklist入库信息
    public int checklistAppId = 0;
    public int checklistConfId = 0;
    //使用是请替换JOB_NAME为自己的jenkins-job名
    public String checklistCiCmd = "curl -X POST http://qarobot:qarobot@192.168.50.2:8080/job/" + JOB_NAME + "/buildWithParameters?case_name=";
    public String checklistQaOwner = "于海生";
    //钉钉推送信息
    public String dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
    public String[] pushRd = {
            "18513118484", //杨航
            "15011479599", //谢志东
            "15898182672"}; //华成裕
    public String message = TEST_PRODUCT + " \n" +
            "case：" + CASE_NAME + "\n\n" +
            "验证：" + CASE_DESC +
            " \n\n" + CASE_FAIL;
    //配置信息
    public String shopId;
    public String caseName = "login";
    public String referer;
    public String produce;
}
