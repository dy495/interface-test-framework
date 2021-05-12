package com.haisheng.framework.testng.bigScreen.crm.wm.base.freemarker.enumerator;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * 文件类型枚举
 *
 * @author wangmin
 * @date 2021/3/8 19:40
 */
public enum FileFormatEnum {
    JAVA(".java"),

    HTML(".html"),

    XML(".xml"),
    ;

    FileFormatEnum(String suffix) {
        this.suffix = suffix;
    }

    @Getter
    private final String suffix;

    public static FileFormatEnum findBySuffix(String suffix) {
        Preconditions.checkArgument(!StringUtils.isEmpty(suffix), "后缀不存在");
        Optional<FileFormatEnum> any = Arrays.stream(values()).filter(e -> e.getSuffix().equals(suffix)).findAny();
        Preconditions.checkArgument(any.isPresent(), "后缀不存在");
        return any.get();
    }
}
