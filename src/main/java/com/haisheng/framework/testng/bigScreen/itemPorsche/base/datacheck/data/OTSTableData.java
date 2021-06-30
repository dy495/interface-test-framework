package com.haisheng.framework.testng.bigScreen.itemPorsche.base.datacheck.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.field.IField;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.tarot.row.IRow;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * ots表结果
 * 每个表都需要包含实例名、表名、所有行数据、数据源名称
 *
 * @author wangmin
 * @date 2021-06-18
 */
@Data
public class OTSTableData implements Serializable {
    /**
     * 实例名称
     */
    String instanceName;

    /**
     * 表名
     */
    String tableName;

    /**
     * 表中行数据
     */
    IRow[] rows;

    /**
     * 数据源
     */
    String sourceName;

    /**
     * 行数据
     */
    List<OTSRowData> rowDataList;

    /**
     * 如果想要行数据需执行此方法
     * 将所有行数据插入OTSRowData
     */
    public OTSTableData initOTSRowData() {
        List<OTSRowData> list = new LinkedList<>();
        Arrays.stream(rows).forEach(iRow -> {
            Preconditions.checkArgument(iRow != null, "行数据为空");
            IField regionField = iRow.getField("region");
            Preconditions.checkArgument(regionField != null, "字段region为空");
            IField startTimeField = iRow.getField("start_time");
            Preconditions.checkArgument(startTimeField != null, "字段start_time为空");
            String region = regionField.getValue();
            String startTime = startTimeField.getValue();
            String userId = iRow.getField("user_id") == null ? "N" : iRow.getField("user_id").getValue();
            JSONArray regions = JSONArray.parseArray(region);
            for (int i = 0; i < regions.size(); i++) {
                JSONObject jsonObject = regions.getJSONObject(i);
                jsonObject.put("user_id", userId);
                jsonObject.put("start_time", startTime);
                OTSRowData otsRowData = JSONObject.toJavaObject(jsonObject, OTSRowData.class);
                list.add(otsRowData);
            }
        });
        rowDataList = list;
        return this;
    }
}
