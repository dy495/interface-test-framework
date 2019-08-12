package com.haisheng.framework.testng.CommonDataStructure;

import ai.winsense.constant.SdkConstant;
import lombok.Data;


@Data
public class GateRequstPara {
    private String[] resource;
    private String json;
    private String router;
    private String UID = "uid_87803c0c";
    private String APP_CODE = "7485a90349a2";
    private String AK = "8da9aeabd74198b1";
    private String SK = "ec44b94f9b3cf4333c5d000781cb0289";

    private String version = SdkConstant.API_VERSION;
}
