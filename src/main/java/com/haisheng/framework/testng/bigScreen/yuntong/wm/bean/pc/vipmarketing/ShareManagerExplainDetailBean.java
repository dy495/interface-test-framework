package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.23. 分享说明(池) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ShareManagerExplainDetailBean implements Serializable {
    /**
     * 描述 内容
     * 版本 v2.0
     */
    @JSONField(name = "content")
    private String content;

}