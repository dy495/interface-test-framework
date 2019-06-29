package com.haisheng.framework.util;

public class StatusCode {

    public static final int SUCCESS = 1000;
    public static final int BAD_REQUEST = 1001;
    public static final int INTERNAL_TIME_OUT = 1002;
    public static final int SYSTEM_BUSY = 1003;
    public static final int REQUEST_ENTITY_TOO_LARGE = 1004;
    public static final int INTERNAL_SERVER_ERROR = 1005;
    public static final int DOWNLOAD_FAIL = 1006;
    public static final int IMAGE_SIZE_ERROR = 1007;
    public static final int IMAGE_DECODE_ERROR = 1008;
    public static final int UNKNOWN_ERROR = 1009;
    public static final int DB_ERROR = 1010;
    public static final int TASK_DISPATCH_ERROR = 1011;
    public static final int NET_ERROR = 1012;
    public static final int DB_EXIST = 1013;

    //系统状态码
    public static final int UN_AUTHORIZED = 2001;
    public static final int FORBIDDEN = 2002;
    public static final int API_NOT_FOUND = 2003;
    public static final int DEVICE_MANAGE_ERROR = 2004;


    //业务状态码
    public static final int NO_FACE = 3001;
    public static final int FACE_UNQUALIFIED = 3002;
    public static final int RELATED_FAIL = 3003;
    public static final int REGISTER_LIMIT = 3004;
    public static final int GROUP_LOCK = 3005;
    public static final int GROUP_RUNNING = 3006;
    public static final int NO_GROUP = 3007;
    public static final int ALG_ERROR = 3008;
    public static final int ALG_FEATURE_ERROR = 3009;
    public static final int ALG_UNKOWN_ERROR = 3010;
    public static final int BODY_UNQUALIFIED = 3011;

    public static final int stocktakingUnfinished = 4001;



}
