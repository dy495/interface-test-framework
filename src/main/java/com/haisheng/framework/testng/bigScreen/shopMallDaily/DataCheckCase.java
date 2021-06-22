package com.haisheng.framework.testng.bigScreen.shopMallDaily;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.DataCheckRunner;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.OTSRowData;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.OTSTableData;
import com.haisheng.framework.util.DateTimeUtil;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DataCheckCase {

    @Test
    public void everyHourData() {
        String rulePath = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/base/datacheck/rule/规则表.xlsx";
        DataCheckRunner dataCheckRunner = new DataCheckRunner.Builder().rulePath(rulePath).queryPrimaryKeyName("scope_date").shopId("33467").build();
        List<OTSTableData> otsTableDataList = dataCheckRunner.getOtsTableDataList();
        List<OTSRowData> otsRowDataList = new LinkedList<>();
        otsTableDataList.stream().filter(e -> e.getName().contains("实时")).forEach(e -> {
            e.initOTSRowData();
            otsRowDataList.addAll(e.getRowDataList().stream().filter(otsRowData -> otsRowData.getStartTime().compareTo(getNowStamp()) < 0
                    && otsRowData.getStartTime().compareTo(String.valueOf(Long.parseLong(getNowStamp()) - 1000 * 60 * 60)) > 0)
                    .collect(Collectors.toCollection(LinkedList::new)));
        });
        otsRowDataList.forEach(System.err::println);
    }

    public String getNowStamp() {
        String format = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH") + ":00:00";
        String stamp = DateTimeUtil.dateToStamp(format);
        System.err.println("stamp:" + stamp);
        return stamp;
    }

    @Test
    public void ss() {
        System.err.println("1624161649857".compareTo(getNowStamp()) < 0);
    }
}
