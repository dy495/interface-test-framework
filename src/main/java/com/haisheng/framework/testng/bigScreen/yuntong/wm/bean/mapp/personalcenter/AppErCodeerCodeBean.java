package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.personalcenter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 11.6. 个人小程序码
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppErCodeerCodeBean implements Serializable {
    /**
     * 描述 小程序码展示路径
     * 版本 v1.0
     */
    @JSONField(name = "er_code_url")
    private String erCodeUrl;

}