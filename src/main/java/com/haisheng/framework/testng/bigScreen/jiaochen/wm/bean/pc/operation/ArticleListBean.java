package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.operation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 24.9. 内容运营 : 活动下拉, 全部信息流（2021-01-26）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ArticleListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

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
     * 描述 关联项id
     * 版本 v2.0
     */
    @JSONField(name = "itemId")
    private Long itemId;

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
     * 描述 活动类型 null表示非活动(enum:)
     * 版本 v2.0
     */
    @JSONField(name = "activityType")
    private String activityType;

    /**
     * 描述 活动类型 null表示非活动
     * 版本 v2.0
     */
    @JSONField(name = "activityStart")
    private String activityStart;

    /**
     * 描述 活动类型 null表示非活动
     * 版本 v2.0
     */
    @JSONField(name = "activityEnd")
    private String activityEnd;

    /**
     * 描述 活动规则
     * 版本 v2.0
     */
    @JSONField(name = "rule")
    private String rule;

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
     * 描述 参与限制类型
     * 版本 v2.0
     */
    @JSONField(name = "participationLimitType")
    private Integer participationLimitType;

    /**
     * 描述 限制标签
     * 版本 v2.0
     */
    @JSONField(name = "chooseLabels")
    private String chooseLabels;

}