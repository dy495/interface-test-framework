package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class FeidanPicSearch implements Serializable {
    private String date;
    /**飞单版本**/
    private String version;
    /**daily or online, default is daily**/
    private String env = "daily";
    private Timestamp updateTime;

    /**样本总数**/
    private int totalNum;

    /**订单所有图片样本成功搜索到用户的数量**/
    private int sampleSuccessNumAll;
    /**订单随机一张图片样本成功搜索到用户的数量**/
    private int sampleSuccessNumOne;

    /**订单所有图片样本搜索无结果的数量**/
    private int sampleFailNoResultNumAll = 0;
    /**订单随机一张图片样本搜索无结果的数量**/
    private int sampleFailNoResultNumOne = 0;

    /**订单所有图片样本图片质量不合格的数量**/
    private int samplePicQualityErrorNumAll = 0;
    /**订单随机一张图片样本图片质量不合格的数量**/
    private int samplePicQualityErrorNumOne = 0;


    /**
    订单所有图片样本的召回率 = (totalNum - sampleFailNoResultNumAll) / totalNum
     */
    private float sampleRecallRateAll;

    /**
    订单随机一张图片样本的召回率 = (totalNum - sampleFailNoResultNumOne) / totalNum
    */
    private float sampleRecallRateOne;

    /**
    订单所有图片样本，图片质量不合格的占比 = samplePicQualityErrorNumAll / totalNum
    */
    private float samplePicQualityErrorRateAll;

    /**
    订单随机一张图片样本，图片质量不合格的占比 = samplePicQualityErrorNumOne / totalNum
    */
    private float samplePicQualityErrorRateOne;


    /**
    订单所有图片样本的准确率 = sampleSuccessNumAll / (totalNum - sampleFailNoResultNumAll)
    */
    private float samplePrecisionRateAll;

    /**
    订单随机一张图片样本的准确率 = sampleSuccessNumOne / (totalNum - sampleFailNoResultNumOne)
    */
    private float samplePrecisionRateOne;

    /**
    订单所有图片样本的正确率 = 订单所有图片样本的召回率 * 订单所有图片样本的准确率
    */
    private float sampleAccuracyRateAll;

    /**
    订单随机一张图片样本的正确率 = 订单随机一张图片样本的召回率 * 订单随机一张图片样本的准确率
    */
    private float sampleAccuracyRateOne;

}