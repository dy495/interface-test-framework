package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.container;

import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.table.SheetTable;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelContainer extends BaseContainer {
    private static final String SUFFIX = "xlsx";
    private final boolean hsaHeader;

    public ExcelContainer(Builder builder) {
        super(builder);
        this.hsaHeader = builder.hasHeader;
    }

    @Override
    public boolean init() {
        Preconditions.checkArgument(!StringUtils.isBlank(getPath()), "文件路径为空，无法初始化");
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(getPath()));
            Workbook workbook = getPath().endsWith(SUFFIX) ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream);
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                SheetTable sheetTable = new SheetTable.Builder().sheet(sheet).hsaHeader(hsaHeader).name(sheet.getSheetName()).build();
                addTable(sheetTable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder<Builder, ExcelContainer> {
        private boolean hasHeader = true;

        @Override
        public ExcelContainer buildContainer() {
            return new ExcelContainer(this);
        }
    }
}
