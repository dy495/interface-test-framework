package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class OnlineYuexiuCustomerSearch implements Serializable {
    private String date;
    /**顾客角色: new, high_active, low_active, deal, lost, all**/
    private String role;
    /**daily or online, default is daily**/
    private String env = "daily";
    private Timestamp updateTime;
    /**不计入1005 error的总数**/
    private int totalNum;
    /**成功搜索到用户的数量**/
    private int successNum;
    /**搜索用户不匹配的数量**/
    private int failNum;
    /**搜索无结果的数量**/
    private int failNoResultNum = 0;
    /**搜索结果人物ID与图片人物ID不一致的数量**/
    private int failIdDiffNum = 0;
    /**图片质量不合格的数量**/
    private int picQualityErrorNum;
    private float successRate;
    private float picQualityErrorRate;

    /**day or tillnow, default is day**/
    private String sample = "day";
}