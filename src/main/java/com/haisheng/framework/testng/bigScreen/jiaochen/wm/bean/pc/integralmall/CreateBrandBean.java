package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralmall;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 23.11. 创建品牌 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class CreateBrandBean implements Serializable {
    /**
     * 描述 常见成功返回id
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

}