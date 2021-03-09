//package com.haisheng.framework.testng.bigScreen.jiaochen.wm.util;
//
//import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
//import com.google.common.base.Preconditions;
//import com.haisheng.framework.util.CommonUtil;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import org.testng.annotations.Test;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author wangmin
// * @date 2021/3/8 11:46
// */
//public class FreeMarkerUtil {
//
//    private void createJavaClass(String templatePath, String classPath, String packageName, String className, String urlPath, List<Attribute> attributeList) {
//        // step1 创建freeMarker配置实例
//        Configuration configuration = new Configuration();
//        Writer out = null;
//        try {
//            // step2 获取模版路径
//            configuration.setDirectoryForTemplateLoading(new File(templatePath));
//            // step3 创建数据模型
//            Map<String, Object> dataMap = new HashMap<>();
//            dataMap.put("packageName", packageName);
//            dataMap.put("className", className);
//            dataMap.put("path", urlPath);
//            List<Attribute> attributes = new ArrayList<>();
//            attributeList.forEach(e -> attributes.add(new Attribute(e.getType(), e.getName())));
//            dataMap.put("attrs", attributes);
//            // step4 加载模版文件
//            Template template = configuration.getTemplate("sceneTemplate.ftl");
//            // step5 生成数据
//            File docFile = new File(classPath + "\\" + className + ".java");
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
//            // step6 输出文件
//            template.process(dataMap, out);
//            System.err.println(className + "文件创建成功 !");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.flush();
//                }
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
//    }
//
//    public void createScene(String urlPath, List<Attribute> attributeList) {
//        Preconditions.checkArgument(!StringUtils.isEmpty(urlPath), "地址路径不能为空");
//        String templatePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/template";
//        StringBuilder sb = new StringBuilder();
//        String[] strings = urlPath.split("/");
//        for (int i = 2; i < strings.length - 1; i++) {
//            sb.append(strings[i]).append("/");
//        }
//        String lastPathName = sb.substring(0, sb.toString().length() - 1);
//        String classPath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/sense/" + lastPathName;
//        String k = "com/haisheng/framework/testng/bigScreen/jiaochen/wm/sense/" + lastPathName;
//        String packageName = k.replace("/", ".");
//        String className = CommonUtil.lineToHump(strings[strings.length - 1], true) + "Scene";
//        createJavaClass(templatePath, classPath, packageName, className, urlPath, attributeList);
//    }
//
//    @Test
//    public void ss() {
//        List<Attribute> attributeList = new ArrayList<>();
//        attributeList.add(new Attribute("String", "name"));
//        attributeList.add(new Attribute("Integer", "age"));
//        attributeList.add(new Attribute("String", "customerPhone"));
//        createScene("/jiaochen/pc/banner/list_page", attributeList);
//    }
//}
