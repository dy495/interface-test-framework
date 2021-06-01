package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.messagemanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 14.3. 消息发送客户总数 （谢） （2020-12-18）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class GroupTotalBean implements Serializable {
    /**
     * 描述 总数
     * 版本 v2.0
     */
    @JSONField(name = "total")
    private Long total;

}