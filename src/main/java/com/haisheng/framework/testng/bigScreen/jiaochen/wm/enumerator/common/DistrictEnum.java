package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common;

/**
 * @author wangmin
 * @date  2020/4/2  18:28
 */
public enum DistrictEnum {

    /**
     * 省
     */
    PROVINCE("0000","____"),
    /**
     * 市
     */
    CITY("00","__");

    private String originSuffix;

    private String resultSuffix;

    DistrictEnum(String originSuffix, String resultSuffix) {
        this.originSuffix = originSuffix;
        this.resultSuffix = resultSuffix;
    }

    public String getOriginSuffix() {
        return originSuffix;
    }

    public String getResultSuffix() {
        return resultSuffix;
    }

    public static String code2province(String code) {
        return code.substring(0, 2) + PROVINCE.getOriginSuffix();
    }

    public static String code2city(String code) {
        return code.substring(0, 4) + CITY.getOriginSuffix();
    }

}
