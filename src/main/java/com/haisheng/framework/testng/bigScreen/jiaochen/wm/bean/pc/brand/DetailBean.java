package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.brand;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.4. 品牌详情（谢）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class DetailBean implements Serializable {
    /**
     * 描述 品牌id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 品牌名称
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 品牌logo展示url
     * 版本 v1.0
     */
    @JSONField(name = "logo_url")
    private String logoUrl;

    /**
     * 描述 品牌logo oss路径
     * 版本 v1.0
     */
    @JSONField(name = "logo")
    private String logo;

    /**
     * 描述 状态
     * 版本 v1.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 品牌名称首字母
     * 版本 v1.0
     */
    @JSONField(name = "first_letter")
    private String firstLetter;

}