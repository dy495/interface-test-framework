//package com.haisheng.framework.testng.bigScreen.crm.wm.base.util;
//
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.event.AnalysisEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//
//public class EasyExcelConsumerListener<T> extends AnalysisEventListener<T> {
//    private final int pageSize;
//    private final Consumer<List<T>> consumer;
//    private final List<T> list;
//
//    public EasyExcelConsumerListener(int pageSize, Consumer<List<T>> consumer) {
//        this.pageSize = pageSize;
//        this.consumer = consumer;
//        list = new ArrayList<>(pageSize);
//    }
//
//    @Override
//    public void invoke(T t, AnalysisContext analysisContext) {
//        list.add(t);
//        if (list.size() >= pageSize) {
//            consumer.accept(list);
//            list.clear();
//        }
//    }
//
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//        consumer.accept(list);
//    }
//}
