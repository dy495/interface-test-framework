package com.haisheng.framework.testng.bigScreen.shopMallDaily;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.DataCheckRunner;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.OTSRowData;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.datacheck.data.OTSTableData;
import com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean.DetailMessage;
import com.haisheng.framework.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

public class MallEveryHourDataStore {
    private static final Logger logger = LoggerFactory.getLogger(MallDataStore.class);

    @BeforeClass
    public void everyHourData() {
        String rulePath = "src/main/java/com/haisheng/framework/testng/bigScreen/crm/wm/base/datacheck/rule/规则表.xlsx";
        DataCheckRunner dataCheckRunner = new DataCheckRunner.Builder().rulePath(rulePath).queryPrimaryKeyName("scope_date").shopId("33467").build();
        List<OTSTableData> otsTableDataList = dataCheckRunner.getOtsTableDataList();
        List<OTSRowData> otsRowDataList = new LinkedList<>();
        otsTableDataList.stream().filter(e -> e.getName().contains("UV实时数据")).map(OTSTableData::initOTSRowData).forEach(e -> otsRowDataList.addAll(e.getRowDataList().stream().filter(otsRowData -> otsRowData.getStartTime().compareTo(getNowStamp()) < 0
                && otsRowData.getStartTime().compareTo(String.valueOf(Long.parseLong(getNowStamp()) - 1000 * 60 * 60)) > 0).collect(Collectors.toCollection(LinkedList::new))));
        Map<String, OTSRowData> map = new LinkedHashMap<>();
        List<OTSRowData> list = otsRowDataList.stream().filter(e -> e.getStatus().equals("ENTER") && e.getRegionId().equals("33468") && !e.getUserId().equals("N"))
                .map(e -> map.put(e.getUserId(), e)).collect(Collectors.toCollection(LinkedList::new));
        logger.info("去重结果：{}", map.values().size());
        logger.info("不去重结果：{}", list.size());
        List<DetailMessage> detailMessages = new ArrayList<>();
        DetailMessage detailMessage = new DetailMessage();
        detailMessage.setNoReception(map.values().size());
        detailMessage.setHasReception(list.size());
        detailMessages.add(detailMessage);
        logger.info("--------------------{}跑完---------------------------", DateTimeUtil.stampToDate(getNowStamp()));
    }

    public String getNowStamp() {
        String format = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd ") + "11:00:00";
        System.err.println(format);
        return DateTimeUtil.dateToStamp(format);
    }

    @Test
    public void test(){

    }
}
