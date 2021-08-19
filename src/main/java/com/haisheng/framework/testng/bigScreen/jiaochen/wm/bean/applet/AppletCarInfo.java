package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangmin
 * @date 2021/1/29 14:26
 */
@Data
public class AppletCarInfo implements Serializable {
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "plate_number")
    private String plateNumber;
    @JSONField(name = "style_id")
    private Long styleId;
    @JSONField(name = "style_name")
    private String styleName;
    @JSONField(name = "brand_id")
    private Long brandId;
    @JSONField(name = "brand_name")
    private String brandName;
    @JSONField(name = "model_id")
    private Long modelId;
    @JSONField(name = "model_name")
    private String modelName;

}
