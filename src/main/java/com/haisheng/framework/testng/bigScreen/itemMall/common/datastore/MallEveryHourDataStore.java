package com.haisheng.framework.testng.bigScreen.itemMall.common.datastore;

import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.DataCheckRunner;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.data.OTSRowData;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.data.OTSTableData;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.sql.Sql;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.enumerator.EnumContainer;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.DetailMessage;
import com.haisheng.framework.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

public class MallEveryHourDataStore {
    private static final Logger logger = LoggerFactory.getLogger(MallDataStore.class);
    private List<OTSTableData> otsTableDataList;

    @BeforeClass
    public void initOTStable() {
        String rulePath = "src/main/resources/excel/规则表.xlsx";
        DataCheckRunner dataCheckRunner = new DataCheckRunner.Builder().rulePath(rulePath).queryPrimaryKeyName("scope_date").shopId("33467").build();
        otsTableDataList = dataCheckRunner.getOtsTableDataList();
    }

    @Test
    public void test() {
        List<DetailMessage> detailMessages = new ArrayList<>();
        detailMessages.add(getDetailMessage("UV实时数据", "33468"));
        detailMessages.add(getDetailMessage("楼层L1实时数据", "33469"));
        detailMessages.add(getDetailMessage("楼层L2实时数据", "33480"));
        detailMessages.add(getDetailMessage("楼层B1实时数据", "33489"));
        String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
        detailMessages.forEach(e -> {
            Sql sql = Sql.instance().insert("t_mall_moment_data")
                    .set("shop_id", "33467").set("source", e.getSourceName()).set("map_value", e.getNoReception())
                    .set("list_value", e.getHasReception()).set("data", date).set("environment", "online").end();
            new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql.getSql());
        });
    }

    private String getNowStamp() {
        String format = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd H") + ":00:00";
        return DateTimeUtil.dateToStamp(format);
    }

    private boolean compareTime(OTSRowData otsRowData) {
        return otsRowData.getStartTime().compareTo(getNowStamp()) < 0 && otsRowData.getStartTime().compareTo(String.valueOf(Long.parseLong(getNowStamp()) - 1000 * 60 * 60)) > 0;
    }

    public DetailMessage getDetailMessage(String name, String regionId) {
        List<OTSRowData> otsRowDataList = new LinkedList<>();
        otsTableDataList.stream().filter(e -> e.getSourceName().contains(name)).map(OTSTableData::initOTSRowData)
                .map(OTSTableData::getRowDataList).forEach(otsRowData -> otsRowData.stream().filter(this::compareTime)
                .forEach(otsRowDataList::add));
        Map<String, OTSRowData> map = new LinkedHashMap<>();
        List<OTSRowData> list = otsRowDataList.stream().filter(e -> e.getStatus().equals("ENTER"))
                .filter(e -> e.getRegionId().equals(regionId)).filter(e -> !e.getUserId().equals("N"))
                .map(e -> map.put(e.getUserId(), e)).collect(Collectors.toCollection(LinkedList::new));
        logger.info("去重结果：{}", map.values().size());
        logger.info("不去重结果：{}", list.size());
        DetailMessage detailMessage = new DetailMessage();
        detailMessage.setSourceName(name);
        detailMessage.setNoReception(map.values().size());
        detailMessage.setHasReception(list.size());
        logger.info("-----------------{}-{}跑完-------------------", name, DateTimeUtil.stampToDate(getNowStamp()));
        return detailMessage;
    }
}
