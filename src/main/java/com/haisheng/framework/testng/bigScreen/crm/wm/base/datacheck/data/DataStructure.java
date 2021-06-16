package com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * tableStore内部结构
 *
 * @author wangmin
 * @date 2021-06-11
 */
@Data
public class DataStructure implements Serializable {

    @JSONField(name = "end_time")
    private String endTime;

    @JSONField(name = "entrance_id")
    private String entranceId;

    @JSONField(name = "entrance_type")
    private String entranceType;

    @JSONField(name = "region_id")
    private String regionId;

    @JSONField(name = "region_type")
    private String regionType;

    @JSONField(name = "start_time")
    private String startTime;

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "user_id")
    private String userId;
}