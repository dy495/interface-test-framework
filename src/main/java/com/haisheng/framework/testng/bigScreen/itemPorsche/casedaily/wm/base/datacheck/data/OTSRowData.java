package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.datacheck.data;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 每一行的数据内容
 *
 * @author wangmin
 * @date 2021-06-16
 */
@Data
public class OTSRowData implements Serializable {

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