package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.file;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 11.5. 图片上传
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppUploadBean implements Serializable {
    /**
     * 描述 图片展示路径
     * 版本 v1.0
     */
    @JSONField(name = "pic_url")
    private String picUrl;

    /**
     * 描述 图片oss路径
     * 版本 v1.0
     */
    @JSONField(name = "pic_path")
    private String picPath;

}