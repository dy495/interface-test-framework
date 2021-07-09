package com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.departmentdata;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.6. 给您的建议（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class AppSpeechTechniqueAdviceBean implements Serializable {
    /**
     * 描述 建议话术
     * 版本 v1.0
     */
    @JSONField(name = "terms")
    private String terms;

    /**
     * 描述 话术标签
     * 版本 v1.0
     */
    @JSONField(name = "title")
    private String title;
}