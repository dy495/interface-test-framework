package com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.table;

import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.field.SimpleField;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.row.IRow;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.base.row.SimpleRow;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class SheetTable extends BaseTable {
    private final Sheet sheet;
    private final boolean hasHeader;

    public SheetTable(Builder builder) {
        super(builder);
        this.hasHeader = builder.hsaHeader;
        this.sheet = builder.sheet;
    }

    public boolean load() {
        String[] titles = getTitles();
        for (int rowIndex = hasHeader ? 1 : 0, index = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row currentRow = sheet.getRow(rowIndex);
            IRow row = new SimpleRow.Builder().index(index).build();
            for (int columnIndex = 0; columnIndex < titles.length; columnIndex++) {
                String title = titles[columnIndex];
                String value = getCellValue(currentRow.getCell(columnIndex));
                SimpleField simpleField = new SimpleField.Builder().name(title).value(value).build();
                row.addField(simpleField);
            }
            if (row.getFields().length > 0) {
                addRow(row);
                index++;
            }
        }
        return true;
    }

    @NotNull
    private String[] getTitles() {
        Row firstRow = sheet.getRow(0);
        String[] titles = new String[firstRow.getLastCellNum()];
        for (int columnIndex = 0; columnIndex < firstRow.getLastCellNum(); columnIndex++) {
            titles[columnIndex] = hasHeader ? getCellValue(firstRow.getCell(columnIndex)) : String.valueOf(columnIndex);
        }
        return titles;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case 0:
                DecimalFormat df = new DecimalFormat("#");
                return df.format(cell.getNumericCellValue());
            case 1:
                return cell.getRichStringCellValue().getString();
            case 2:
                return cell.getCellFormula();
            case 4:
                return String.valueOf(cell.getBooleanCellValue()).trim();
            default:
                return "";
        }
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder extends BaseBuilder<Builder, SheetTable> {
        private boolean hsaHeader;
        private Sheet sheet;

        @Override
        public SheetTable buildTable() {
            return new SheetTable(this);
        }
    }
}
