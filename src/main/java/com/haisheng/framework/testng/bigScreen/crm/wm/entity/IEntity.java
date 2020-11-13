package com.haisheng.framework.testng.bigScreen.crm.wm.entity;

import java.util.Map;

public interface IEntity {

    IEntity getEntity();

    void setEntity(Map<String, Object> map);

    IEntity[] create();

}
