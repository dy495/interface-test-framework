package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 11.4. 获取省份车牌首字母列表
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppPlateNumberProvinceListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

}