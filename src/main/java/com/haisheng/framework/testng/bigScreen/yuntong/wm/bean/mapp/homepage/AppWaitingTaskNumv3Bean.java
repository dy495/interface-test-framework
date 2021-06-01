package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.homepage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.6. 代办数量 (谢) v3.0 （2021-03-27）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppWaitingTaskNumv3Bean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 代办类型 见字典表《代办类型》
     * 版本 v1.0
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 未读数
     * 版本 v1.0
     */
    @JSONField(name = "num")
    private Long num;

}