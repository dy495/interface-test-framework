package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 小程序卡券信息
 *
 * @author wangmin
 * @date 2020-12-20
 */
@Data
public class AppletVoucher implements Serializable {
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "title")
    private String title;
}
