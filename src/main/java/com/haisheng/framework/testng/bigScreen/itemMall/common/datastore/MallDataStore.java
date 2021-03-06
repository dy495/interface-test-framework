package com.haisheng.framework.testng.bigScreen.itemMall.common.datastore;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.Constants;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.DataCheckRunner;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.data.OTSTableData;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.data.RuleDataSource;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.sql.Sql;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.entity.Factory;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.enumerator.EnumContainer;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.row.IRow;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.datacheck.data.OTSRowData;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.tarot.table.ITable;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.DetailMessage;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.bean.PvUvInfo;
import com.haisheng.framework.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

public class MallDataStore {
    private static final Logger logger = LoggerFactory.getLogger(MallDataStore.class);
    private PvUvInfo pvUvInfo;

    public IRow getRowByField(IRow[] rows, String field) {
        return Arrays.stream(rows).filter(e -> e.getField(Constants.RULE_COLUMN_FIELD).getValue().equals(field)).findFirst().orElse(null);
    }

    public JSONObject putField(JSONObject jsonObject, IRow row, String... field) {
        Arrays.stream(field).forEach(e -> jsonObject.put(row.getField(e).getKey(), row.getField(e).getValue()));
        return jsonObject;
    }

    @Test
    public void realTimeData() {
        List<DetailMessage> detailMessageList = pvUvInfo.getDetailMessages();
        detailMessageList.forEach(e -> {
            if (e.getSourceName().contains("??????")) {
                String date = DateTimeUtil.getFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
                Sql sql = Sql.instance().insert("t_mall_realtime_data")
                        .set("shop_id", "33467").set("source", e.getSourceName()).set("map_value", e.getNoReception())
                        .set("list_value", e.getHasReception()).set("data", date).set("environment", "online").end();
                new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql.getSql());
            }
        });
    }

    @Test
    public void historyData() {
        List<DetailMessage> detailMessageList = pvUvInfo.getDetailMessages();
        detailMessageList.forEach(e -> {
            if (!e.getSourceName().contains("??????")) {
                String date = DateTimeUtil.addDayFormat(new Date(), -1, "yyyy-MM-dd");
                Sql sql = Sql.instance().insert("t_mall_history_data")
                        .set("shop_id", "33467").set("source", e.getSourceName()).set("map_value", e.getNoReception())
                        .set("list_value", e.getHasReception()).set("data", date).set("environment", "online").end();
                new Factory.Builder().container(EnumContainer.DB_ONE_PIECE.getContainer()).build().create(sql.getSql());
            }
        });
    }

    @BeforeClass
    public void initData() {
        pvUvInfo = new PvUvInfo();
        String rulePath = "src/main/resources/excel/?????????.xlsx";
        DataCheckRunner dataCheckRunner = new DataCheckRunner.Builder().rulePath(rulePath).queryPrimaryKeyName("scope_date").shopId("33467").build();
        ITable[] fieldRuleTables = dataCheckRunner.getFieldRuleTables();
        //??????????????????
        List<DetailMessage> detailMessages = new LinkedList<>();
        dataCheckRunner.getOtsTableDataList().stream().map(OTSTableData::initOTSRowData).forEach(otsTableData -> {
            List<OTSRowData> otsRowDataList = otsTableData.getRowDataList();
            Preconditions.checkNotNull(otsRowDataList);
            Arrays.stream(fieldRuleTables).filter(iTable -> iTable.getKey().contains(otsTableData.getSourceName())).forEach(iTable -> {
                iTable.load();
                IRow[] fieldRuleRows = iTable.getRows();
                String[] regionIds = RuleDataSource.parse(getRowByField(fieldRuleRows, "region_id").getField(Constants.RULE_COLUMN_RANGE).getValue());
                String status = getRowByField(fieldRuleRows, "status").getField(Constants.RULE_COLUMN_RANGE).getValue();
                String isNull = getRowByField(fieldRuleRows, "user_id").getField(Constants.RULE_COLUMN_IS_NULL).getValue();
                String isReception = getRowByField(fieldRuleRows, "user_id").getField(Constants.RULE_COLUMN_IS_RECEPTION).getValue();
                logger.info("regionIds:{}", Arrays.toString(regionIds));
                logger.info("status:{}", status);
                logger.info("isNull:{}", isNull);
                logger.info("isReception:{}", isReception);
                Map<String, OTSRowData> map = new LinkedHashMap<>();
                List<OTSRowData> list = otsRowDataList.stream().filter(e -> e.getStatus().equals(status))
                        .filter(e -> Arrays.asList(regionIds).contains(e.getRegionId())).collect(Collectors.toCollection(LinkedList::new));
                list = isNull.equals("false") ? list.stream().filter(e -> !e.getUserId().equals("N")).collect(Collectors.toCollection(LinkedList::new)) : list;
                list.stream().filter(e -> isReception.equals("false")).forEach(e -> map.put(e.getUserId(), e));
                logger.info("???????????????{}", map.values().size());
                logger.info("??????????????????{}", list.size());
                DetailMessage detailMessage = new DetailMessage();
                detailMessage.setSourceName(otsTableData.getSourceName());
                detailMessage.setNoReception(map.values().size());
                detailMessage.setHasReception(list.size());
                detailMessages.add(detailMessage);
                logger.info("--------------------{}??????---------------------------", otsTableData.getSourceName());
            });
        });
        pvUvInfo.setShopId("33467");
        pvUvInfo.setDetailMessages(detailMessages);
    }
}

