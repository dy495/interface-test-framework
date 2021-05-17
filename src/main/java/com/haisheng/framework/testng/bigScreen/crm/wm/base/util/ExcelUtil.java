//package com.haisheng.framework.testng.bigScreen.crm.wm.base.util;
//
//import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.read.builder.ExcelReaderBuilder;
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//public class ExcelUtil extends EasyExcel {
//
//    @NotNull
//    @Contract("_, _, _, _ -> new")
//    public static <T> ExcelReaderBuilder read(String pathName, Class<T> head, Integer pageSize, Consumer<List<T>> consumer) {
//        return read(pathName, head, new EasyExcelConsumerListener<>(pageSize, consumer));
//    }
//
//    @NotNull
//    @Contract("_, _, _ -> new")
//    public static <T> ExcelReaderBuilder read(String pathName, Integer pageSize, Consumer<List<T>> consumer) {
//        return read(pathName, new EasyExcelConsumerListener<>(pageSize, consumer));
//    }
//}
