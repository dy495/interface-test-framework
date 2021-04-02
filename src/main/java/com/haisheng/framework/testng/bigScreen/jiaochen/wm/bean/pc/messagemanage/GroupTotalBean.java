package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.messagemanage;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 12.3. 消息发送客户总数 （谢） （2020-12-18）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
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