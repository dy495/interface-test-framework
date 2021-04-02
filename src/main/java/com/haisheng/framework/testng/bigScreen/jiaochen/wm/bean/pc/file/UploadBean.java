package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.file;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 32.5. 通用文件上传接口
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class UploadBean implements Serializable {
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