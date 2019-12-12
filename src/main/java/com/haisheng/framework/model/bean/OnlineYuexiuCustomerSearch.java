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
    private String role;
    /**daily or online**/
    private String env;
    private Timestamp updateTime;
    /**不计入1005 error的总数**/
    private int totalNum;
    /**成功搜索到用户的数量**/
    private int successNum;
    /**搜索用户不匹配的数量**/
    private int failNum;
    /**图片质量不合格的数量**/
    private int picQualityErrorNum;
    private float successRate;
    private float picQualityErrorRate;
}