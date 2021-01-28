package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 核销记录
 *
 * @author wangmin
 * @date 2021/1/27 20:57
 */
@Data
public class VerificationRecord implements Serializable {
    /**
     * 核销渠道
     */
    @JSONField(name = "verification_channel")
    private String verificationChannel;

    /**
     * 核销渠道名称
     */
    @JSONField(name = "verification_channel_name")
    private String verificationChannelName;
}
