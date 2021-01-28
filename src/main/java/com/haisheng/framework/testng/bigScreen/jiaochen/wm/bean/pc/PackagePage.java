package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class PackagePage implements Serializable {
    @JSONField(name = "package_name")
    private String packageName;
    @JSONField(name = "package_id")
    private Long packageId;
    @JSONField(name = "price")
    private String price;
    /**
     * 售出数量
     */
    @JSONField(name = "sold_number")
    private Integer soldNumber;

    /**
     * 赠送数量
     */
    @JSONField(name = "give_number")
    private Integer giveNumber;
}
