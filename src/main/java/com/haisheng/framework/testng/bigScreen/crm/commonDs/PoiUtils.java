package com.haisheng.framework.testng.bigScreen.crm.commonDs;

import com.haisheng.framework.util.DateTimeUtil;
import org.apache.poi.hssf.usermodel.*;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @Description:excel导出工具类
 * @Author: dk
 * @Date: 2020/1/14 9:49
 */
public class PoiUtils {
    public static void main(String[] args) throws Exception {
        importCustomer("2000");
    }
    //导入工单，公里数=固定值
    public static void importCustomer(String mile) throws IOException {
        Random random = new Random();
        String serverNumber = "A" + random.nextInt(100000);
        DateTimeUtil dt = new DateTimeUtil();
        String begin = dt.getHistoryDate1(-20);
        String end = dt.getHistoryDate1(-15);
        String importFilepath="src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/xmf/file/importfile.xlsx";
//        importFilepath= importFilepath.replace("/", File.separator);
        String[] roeName = {"*服务单号",
                "*开单时间",
                "*工单类型",
                "*维修类型",
                "*车牌号",
                "*VIN码",
                "*送修人姓名",
                "*性别",
                "*送修人手机",
                "*服务顾问",
                "*里程数/km",
                "*费用/rmb",
                "*交车时间"};
        String[] parm = {serverNumber,
                begin,
                "2",
                "1",
                "沪A88888",
                "ASDAAABBB78765431",
                "隔壁老王",
                "男",
                "15321527989",
                "路虎揽胜",
                mile,
                "2000",
                end};

        XSSFWorkbook workbook = export2(roeName, parm);
        FileOutputStream output = new FileOutputStream(importFilepath);
        workbook.write(output);
        output.flush();

    }

    public static XSSFWorkbook export2(String[] rowName, String[] parm) {
        XSSFWorkbook workbook = new XSSFWorkbook(); // 创建工作簿对象
        XSSFSheet sheet = workbook.createSheet(); // 创建工作表对象

        // 定义所需列数
        int columnNum = rowName.length;
        XSSFRow rowRowName = sheet.createRow(0); // 在索引2的位置创建行(最顶端的行开始的第二行)
        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum; n++) {
            XSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
            cellRowName.setCellType(XSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
            XSSFRichTextString text = new XSSFRichTextString(rowName[n]);
            cellRowName.setCellValue(text); // 设置列头单元格的值
        }

        int columnNum2 = parm.length;
        XSSFRow rowRowName2 = sheet.createRow(1); // 在索引2的位置创建行(最顶端的行开始的第二行)
        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum2; n++) {
            XSSFCell cellRowName = rowRowName2.createCell(n); // 创建列头对应个数的单元格
            cellRowName.setCellType(XSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
            XSSFRichTextString text = new XSSFRichTextString(parm[n]);
            cellRowName.setCellValue(text); // 设置列头单元格的值
        }
        return workbook;
    }
    /**
     * excel导出数据
     *
     * @param title    显示的导出表的标题
     * @param rowName  导出表的列名
     * @param dataList 表的内容
     * @return
     */
    public static HSSFWorkbook export(String title, String[] rowName, List<Object[]> dataList) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
        HSSFSheet sheet = workbook.createSheet(title); // 创建工作表
        // 产生表格标题行
        HSSFRow rowm = sheet.createRow(0);
        HSSFCell cellTiltle = rowm.createCell(0);
        // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
        HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);// 获取列头样式对象
        HSSFCellStyle style = getStyle(workbook); // 单元格样式对象
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length - 1)));
        cellTiltle.setCellStyle(columnTopStyle);
        cellTiltle.setCellValue(title);
        // 定义所需列数
        int columnNum = rowName.length;
        HSSFRow rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)
        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum; n++) {
            HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
            cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
            HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
            cellRowName.setCellValue(text); // 设置列头单元格的值
            cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式

        }
        // 将查询出的数据设置到sheet对应的单元格中
        for (int i = 0; i < dataList.size(); i++) {
            Object[] obj = dataList.get(i);// 遍历每个对象
            HSSFRow row = sheet.createRow(i + 3);// 创建所需的行数
            for (int j = 0; j < obj.length; j++) {
                HSSFCell cell = null; // 设置单元格的数据类型
                if (j == 0) {
                    cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(i + 1);
                } else {
                    cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                    if (!"".equals(obj[j]) && obj[j] != null) {
                        cell.setCellValue(obj[j].toString()); // 设置单元格的值
                    }
                }
                cell.setCellStyle(style); // 设置单元格样式
            }
        }
        // 让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if (colNum == 0) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
            }
        }
        return workbook;
    }

    /**
     * 列头单元格样式
     *
     * @param workbook
     * @return
     */
    public static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    /**
     * 列数据信息单元格样式
     *
     * @param workbook
     * @return
     */
    public static HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体
        style.setFont(font);
        // 设置自动换行
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }
}