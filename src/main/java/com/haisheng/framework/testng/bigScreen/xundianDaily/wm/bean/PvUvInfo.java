package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PvUvInfo implements Serializable {

    private String shopId;

    List<DetailMessage> detailMessages;
}
