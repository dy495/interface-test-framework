package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/12/2 13:14
 */
public enum FileTypeEnum {
    /**
     * EXCEL
     */
    EXCEL(".xlsx");


    private String name;

    FileTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static FileTypeEnum findByType(String type) {
        Optional<FileTypeEnum> any = Arrays.stream(values()).filter(e -> e.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "导入类型不正确");
        return any.get();
    }

    public static FileTypeEnum findTemplateByType(String type){
        Optional<FileTypeEnum> any = Arrays.stream(values()).filter(t -> t.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "模板类型不存在");
        return any.get();
    }

}
