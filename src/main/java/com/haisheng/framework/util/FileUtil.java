package com.haisheng.framework.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<File> getFiles(String folderPath) {
        final Path path = Paths.get(folderPath);
        final List<File> files = new ArrayList<>();
        SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                files.add(file.toFile());
                return super.visitFile(file, attrs);
            }
        };
        try{
            java.nio.file.Files.walkFileTree(path, finder);
        }catch(IOException e){
            logger.info(e.toString());
        }

        return files;
    }

    public List<File> getFiles(String folderPath, String keyString) {
        final Path path = Paths.get(folderPath);
        final List<File> files = new ArrayList<>();
        SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().contains(keyString)) {
                    files.add(file.toFile());
                }

                return super.visitFile(file, attrs);
            }
        };
        try{
            java.nio.file.Files.walkFileTree(path, finder);
        }catch(IOException e){
            logger.info(e.toString());
        }

        return files;
    }

    public List<File> getCurrentDirFilesWithoutDeepTraverse(String folderPath, String keyString) {
        List<File> files = new ArrayList<>();

        File dir = new File(folderPath);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                if (children[i].contains(keyString)) {
                    files.add(new File(folderPath + File.separator +children[i]));
                }
            }
        }
        return files;
    }

    public String findLineByKey(String filePath, String key) {
        String line = null;
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                String current = scanner.nextLine();
                if (current.contains(key)) {
                    line = current;
                }
            }
            scanner.close();
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return line;
    }

    public List<String> findLinesByKey(String filePath, String key) {
        List<String> lines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                String current = scanner.nextLine();
                if (current.contains(key)) {
                    lines.add(current);
                }
            }
            scanner.close();
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return lines;
    }

    public List<String> getFileContent(String filePath) {
        List<String> content = null;
        try {
            content = new ArrayList<>();
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                content.add(scanner.nextLine());
            }
            scanner.close();
        } catch (Exception e) {
            logger.info(e.toString());
            content = null;
        }
        return content;
    }

    public boolean writeContentToFile(String filePath, List<String> lines) {

        try {
            FileUtils.writeLines(new File(filePath), "UTF-8", lines);
        } catch (IOException e) {
            logger.error(e.toString());
            return false;
        }

        return true;
    }

}
