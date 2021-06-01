package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 26.7. 内容运营 : 文章详情 （谢）（2020-12-22）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ArticleDetailBean implements Serializable {
    /**
     * 描述 信息流id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 是否自定义分享信息
     * 版本 v3.0
     */
    @JSONField(name = "is_custom_share_info")
    private Boolean isCustomShareInfo;

    /**
     * 描述 自定义分享图片
     * 版本 v3.0
     */
    @JSONField(name = " share_pic_path")
    private String  sharePicPath;

    /**
     * 描述 自定义分享标题
     * 版本 v3.0
     */
    @JSONField(name = "share_title")
    private String shareTitle;

    /**
     * 描述 文章标题
     * 版本 v1.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 类型 1：文章，2：活动
     * 版本 v2.0
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 描述 子类型 type为活动时 见字典表《活动类型》
     * 版本 v2.0
     */
    @JSONField(name = "sub_type")
    private Integer subType;

    /**
     * 描述 图片样式类型(大图 三途 左侧图)
     * 版本 v1.0
     */
    @JSONField(name = "pic_type")
    private String picType;

    /**
     * 描述 标签枚举
     * 版本 v1.0
     */
    @JSONField(name = "label")
    private String label;

    /**
     * 描述 标签枚举名
     * 版本 v1.0
     */
    @JSONField(name = "label_name")
    private String labelName;

    /**
     * 描述 活动图片OSS地址
     * 版本 v1.0
     */
    @JSONField(name = "pic_list")
    private JSONArray picList;

    /**
     * 描述 创建时间, 转换中文表示
     * 版本 v1.0
     */
    @JSONField(name = "time_str")
    private String timeStr;

    /**
     * 描述 创建时间 时间戳
     * 版本 v1.0
     */
    @JSONField(name = "timestamp")
    private Long timestamp;

    /**
     * 描述 发表账号
     * 版本 v1.0
     */
    @JSONField(name = "creator")
    private String creator;

    /**
     * 描述 富文本内容
     * 版本 v1.0
     */
    @JSONField(name = "content")
    private String content;

}