package com.haisheng.framework.testng.service;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static final String dirPlusPrefix = "src/main/resources/csv";

    private static List<String> csvFiles = new ArrayList<>();

    private static List<String> csvDir = new ArrayList<>();

    public static List<String> getCsvFilesByResources() {
        return csvFiles;
    }

    public static List<String> getDirByResources() {
        return csvDir;
    }

    static {
        File root = new File(dirPlusPrefix);
        if (!root.exists()) {
            System.err.println("root is not exists.");
        }
        showAllFiles(root);
    }

    private static void showAllFiles(File dir) {
        File[] fs = dir.listFiles();
        if (null != fs) {
            for (File file : fs) {
                String filePath = file.getAbsolutePath();
                if (filePath.contains(".csv")) {
                    csvFiles.add(filePath);
                } else if (file.isDirectory()) {
                    csvDir.add(file.getName());
                }
                if (file.isDirectory()) {
                    showAllFiles(file);
                }
            }
        }
    }
}
