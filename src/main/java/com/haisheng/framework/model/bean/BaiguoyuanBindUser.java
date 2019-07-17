package com.haisheng.framework.model.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class BaiguoyuanBindUser {

    private String date;
    private String custUserId;
    private String custShopId;
    private JSONObject personList;


}
