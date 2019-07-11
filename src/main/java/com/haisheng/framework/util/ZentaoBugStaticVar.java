package com.haisheng.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ZentaoBugStaticVar {

    //bug severity
    public final static int SEV_BLOCK = 1;
    public final static int SEV_CRITICAL = 2;
    public final static int SEV_NORMAL = 3;

    //status, close or unclose
    public final static String STATUS_CLOSE = "closed";

    //env
//    public final static int ENV_ONLINE = ; //NO online issue currently

    //product
    public final static int PROD_EDGE_END = 1;
    public final static int PROD_CLOUD_SERVICE = 2;
    public final static int PROD_OPEN_PLATFORM = 4;
    public final static int PROD_SCREEN_BIG = 7;
    public final static int PROD_COMMODITY_SHLF = 8;

    //checklist app ID
    public final static int APP_EDGE_END = 1;
    public final static int APP_CLOUD_SERVICE = 2;
    public final static int APP_MANAGE_PLATFORM = 3;
    public final static int APP_OPEN_PLATFORM = 4;
    public final static int APP_SCREEN_BIG = 5;
    public final static int APP_COMMODITY_SHLF = 6;


    public ConcurrentHashMap<Integer, Integer> getProdAppMap() {
        ConcurrentHashMap<Integer, Integer> prodAppMap = new ConcurrentHashMap<>();

        prodAppMap.put(PROD_EDGE_END, APP_EDGE_END);
        prodAppMap.put(PROD_CLOUD_SERVICE, APP_CLOUD_SERVICE);
        prodAppMap.put(PROD_OPEN_PLATFORM, APP_OPEN_PLATFORM);
        prodAppMap.put(PROD_SCREEN_BIG, APP_SCREEN_BIG);
        prodAppMap.put(PROD_COMMODITY_SHLF, APP_COMMODITY_SHLF);

        return prodAppMap;
    }


}
