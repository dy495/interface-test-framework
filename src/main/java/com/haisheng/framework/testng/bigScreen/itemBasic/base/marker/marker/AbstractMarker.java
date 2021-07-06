package com.haisheng.framework.testng.bigScreen.itemBasic.base.marker.marker;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.marker.enumerator.FileFormatEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Marker的抽象类
 *
 * @author wangmin
 * @date 2021/3/8 18:42
 */
@Getter
@Setter
public abstract class AbstractMarker implements IMarker {
    protected final static Logger logger = LoggerFactory.getLogger(AbstractMarker.class);
    private final String templatePath;
    private final String templateName;
    private final String templateFile;
    private final String ExitFile;
    protected Structure structure;

    protected AbstractMarker(AbstractBuilder<?> abstractBuilder) {
        this.templatePath = abstractBuilder.templatePath;
        this.templateName = abstractBuilder.templateName;
        this.templateFile = abstractBuilder.templateFile;
        this.ExitFile = abstractBuilder.ExitFile;
    }

    @Override
    public void execute() {
        init();
        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration();
        Writer out = null;
        try {
            //获取数据结构
            Structure structure = getStructure();
            if (structure != null) {
                if (StringUtils.isNotEmpty(structure.getOutputPath()) && StringUtils.isNotEmpty(structure.getClassName())) {
                    String parentPath = structure.getOutputPath();
                    File file = new File(parentPath);
                    if (!file.exists()) {
                        logger.info("开始创建文件所在文件夹 :{}", parentPath);
                        Preconditions.checkArgument(file.mkdirs(), "文件夹创建失败 !");
                        logger.info("文件所在文件夹创建成功 !");
                    }
                    //step2 获取模版路径
                    configuration.setDirectoryForTemplateLoading(new File(templatePath));
                    //step4 加载模版文件
                    Template template = configuration.getTemplate(templateName);
                    //step3 创建数据模型
                    Map<String, Object> dataMap = structure.getDataMap();
                    //step5 生成数据
                    File docFile = new File(parentPath + "\\" + structure.getClassName() + FileFormatEnum.findBySuffix(structure.getFileFormat()).getSuffix());
                    if (!docFile.exists()) {
                        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
                        // step6 输出文件
                        template.process(dataMap, out);
                        logger.info("----------{}----------", "恭喜~ 文件创建成功 !");
                    } else {
                        logger.info("----------{}----------", "文件已存在 !");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override
    public void execute2() {
        init();
        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration();
        Writer out = null;
        try {
            //获取数据结构
            Structure structure = getStructure();
            if (structure != null) {
                if (StringUtils.isNotEmpty(structure.getOutputPath()) && StringUtils.isNotEmpty(structure.getClassName())) {
                    //step2 获取模版路径
                    configuration.setDirectoryForTemplateLoading(new File(templatePath));
                    //step4 加载模版文件
                    Template template = configuration.getTemplate(templateName);
                    //step3 创建数据模型
                    Map<String, Object> dataMap = structure.getDataMap();
                    if (dataMap.containsKey("attrs")) {
                        ArrayList a = (ArrayList) dataMap.get("attrs");
                        if (a.size() < 4) {
                            File docFile = new File(templateFile);
                            File docFile2 = new File(ExitFile);
//                            if (!checkName(docFile, structure.getClassName())) {
                            if (!checkName(docFile2, (String) dataMap.get("path"))) {
                                System.err.println(dataMap.get("path"));
                                out = new BufferedWriter(new FileWriter(docFile, true));
                                template.process(dataMap, out);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static boolean checkName(File file, String name) throws Exception {
        BufferedReader read = new BufferedReader(new FileReader(file));
        String a ;
        while ((a=read.readLine()) != null ) {
            System.out.println(a);
            if (a.contains(name)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 子类继承
     * 赋值初始化
     */
    public abstract void init();

    public abstract static class AbstractBuilder<T extends AbstractBuilder<?>> {
        private String templatePath;
        private String templateName;
        private String templateFile;
        private String ExitFile;

        public T templateFile(String templateFile) {
            this.templateFile = templateFile;
            return (T) this;
        }

        public T ExitFile(String ExitFile) {
            this.ExitFile = ExitFile;
            return (T) this;
        }

        /**
         * 模板路径
         *
         * @param templatePath 模板路径
         * @return T
         */
        public T templatePath(String templatePath) {
            this.templatePath = templatePath;
            return (T) this;
        }

        /**
         * 模板文件名称
         *
         * @param templateName 模板文件名称
         * @return T
         */
        public T templateName(String templateName) {
            this.templateName = templateName;
            return (T) this;
        }

        /**
         * 构建抽象类的Marker
         *
         * @return IMarker
         */
        public IMarker buildMarker() {
            Preconditions.checkArgument(StringUtils.isNotEmpty(templateName), "模板文件名称为空！！");
            Preconditions.checkArgument(StringUtils.isNotEmpty(templatePath), "模板路径为空！！");
            return build();
        }

        /**
         * 构建自己的Marker
         *
         * @return IMarker
         */
        protected abstract IMarker build();
    }
}
