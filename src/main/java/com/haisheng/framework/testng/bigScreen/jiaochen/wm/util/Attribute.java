package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;

import com.haisheng.framework.util.CommonUtil;

/**
 * @author wangmin
 * @date 2021/3/8 14:32
 */
public class Attribute {
    private String type;
    private String name;
    private final String buildName;

    public Attribute(String type, String name) {
        this.type = type;
        this.name = name;
        this.buildName = CommonUtil.humpToLine(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuildName() {
        return buildName;
    }
}
