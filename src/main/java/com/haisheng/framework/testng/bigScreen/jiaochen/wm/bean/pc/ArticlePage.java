package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.intefer.SelectReception;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 内容列表
 *
 * @author wangmin
 * @date 2021/1/27 20:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticlePage extends SelectReception {

    /**
     * 状态
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 内容标题
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 开启/关闭
     */
    @JSONField(name = "status")
    private String status;
}
