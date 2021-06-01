package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.message;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 12.4. 获取未读消息数（谢）（2021-01-26）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppUnreadunreadBean implements Serializable {
    /**
     * 描述 总数
     * 版本 v2.0
     */
    @JSONField(name = "total")
    private Long total;

}