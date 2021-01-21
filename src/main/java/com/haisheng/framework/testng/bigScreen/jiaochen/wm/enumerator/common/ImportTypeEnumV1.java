package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/12/4 17:13
 */
public enum ImportTypeEnumV1 {

    /**
     * 售后客户工单记录
     */
    AFTER_CUSTOMER("work_order","AFTER_CUSTOMER_TEMP/","售后客户工单记录","workOrderImportServiceImpl","ai.winsense.retail.business.car.platform.core.business.excel.bo.WorkOrderRecordImportInfo"),

    POTENTIAL_CUSTOMER("potential_customer","POTENTIAL_CUSTOMER_TEMP","潜在客户导入记录","potentialCustomerImportServiceImpl","ai.winsense.retail.business.car.platform.core.business.excel.bo.PotentialCustomerImportInfo");

    private String name;

    private String type;

    private String filePath;

    private String beanName;

    private String entityName;


    ImportTypeEnumV1(String type,String path,String name,String beanName,String entityName) {
        this.type = type;
        this.filePath = path;
        this.name = name;
        this.beanName = beanName;
        this.entityName = entityName;
    }
    public String getEntityName() {
        return entityName;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFilePath(){return filePath;}

    public String getBeanName(){return beanName;}


    public static ImportTypeEnumV1 findByType(String type) {
        Optional<ImportTypeEnumV1> any = Arrays.stream(values()).filter(e -> e.getType().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "导入类型不存在");
        return any.get();
    }
    public static ImportTypeEnumV1 findByFilePath(String filePath) {
        Optional<ImportTypeEnumV1> any = Arrays.stream(values()).filter(e -> e.getFilePath().equals(filePath)).findAny();
        Preconditions.checkArgument(any.isPresent(), "导入路径不存在");
        return any.get();
    }

    public static ImportTypeEnumV1 findTemplateByType(String type){
        Optional<ImportTypeEnumV1> any = Arrays.stream(values()).filter(t -> t.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "模板类型不存在");
        return any.get();
    }
}
