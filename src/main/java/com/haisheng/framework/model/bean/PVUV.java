package com.haisheng.framework.model.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by yuhaisheng
 */

@Data
public class PVUV implements Serializable {
    int id;
    int mapId;
    int mapPv;
    int mapUv;
    int regionId;
    int regionPv;
    int regionUv;
    Timestamp updateTime;

}