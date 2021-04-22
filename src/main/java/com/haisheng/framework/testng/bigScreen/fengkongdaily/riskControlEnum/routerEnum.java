package com.haisheng.framework.testng.bigScreen.fengkongdaily.riskControlEnum;

public enum routerEnum {
    /**
     *
     */
    SHOPDAILY("43072", "49998b971ea0", "AI-Test(门店订单录像)", "uid_ef6d2de5","3fdce1db0e843ee0","5036807b1c25b9312116fd4b22c351ac","http://dev.api.winsenseos.com/retail/api/data/biz"),
    SHOPONLINE("13260", "672170545f50", "中关村1号店", "uid_0ba743d8","691ff41137d954f3","d76f2d8a7846382f633c1334139767fe","http://api.winsenseos.com/retail/api/data/biz"),
    ;

    public String getShopid() {
        return shopid;
    }

    public String getUid() {
        return uid;
    }

    public String getAk() {
        return ak;
    }

    public String getSk() {
        return sk;
    }

    private String shopid;

    public String getAppid() {
        return appid;
    }

    private String appid;

    public String getShopName() {
        return shopName;
    }

    private String shopName;

    private String uid;
    private String ak;
    private String sk;

    public String getRequestUrl() {
        return requestUrl;
    }

    private String requestUrl;


    routerEnum(String shopid, String appid, String shopName, String uid, String ak, String sk,String requestUrl) {
        this.shopid = shopid;
        this.appid = appid;
        this.shopName = shopName;
        this.uid = uid;
        this.ak = ak;
        this.sk = sk;
        this.requestUrl=requestUrl;
    }






}
