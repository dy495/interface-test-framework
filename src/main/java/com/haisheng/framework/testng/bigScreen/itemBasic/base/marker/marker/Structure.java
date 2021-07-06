package com.haisheng.framework.testng.bigScreen.itemBasic.base.marker.marker;

import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 结构数据
 * 给AbstractMaker提供必要参数
 *
 * @author wangmin
 * @date 2021/3/8 19:30
 */
@Getter
public class Structure {
    private final Map<String, Object> dataMap = new HashMap<>(64);
    private final String outputPath;
    private final String fileFormat;
    private final String className;

    public Structure(Builder builder) {
        this.outputPath = builder.outputPath;
        this.fileFormat = builder.fileFormat;
        this.className = builder.className;
        this.dataMap.putAll(builder.dataMap);
    }

    public static class Builder {
        private String outputPath;
        private String fileFormat;
        private String className;
        private Map<String, Object> dataMap = new LinkedHashMap<>(64);

        /**
         * 构建文件输出地址
         *
         * @param outputPath 输出地址
         * @return Builder.outputPath
         */
        public Builder outputPath(String outputPath) {
            this.outputPath = outputPath;
            return this;
        }

        /**
         * 创建数据模型
         *
         * @param dataMap 数据模型
         * @return Builder.dateMap
         */
        public Builder dateMap(Map<String, Object> dataMap) {
            this.dataMap = dataMap;
            return this;
        }

        /**
         * 指定文件类型
         *
         * @param fileFormat 文件类型
         * @return Builder.fileFormat
         */
        public Builder fileFormat(String fileFormat) {
            this.fileFormat = fileFormat;
            return this;
        }

        /**
         * 类名
         *
         * @param className 类名
         * @return Builder.className
         */
        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Structure build() {
            return new Structure(this);
        }
    }
}
