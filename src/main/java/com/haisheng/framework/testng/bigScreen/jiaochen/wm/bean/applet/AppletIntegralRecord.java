package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangmin
 * @date 2021/2/23 19:56
 */
@Data
public class AppletIntegralRecord implements Serializable {
    /**
     * 积分数
     */
    @JSONField(name = "integral")
    private String integral;

    /**
     * 类型
     */
    @JSONField(name = "change_type")
    private String changeType;
}
