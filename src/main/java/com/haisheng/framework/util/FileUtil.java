package com.haisheng.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

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
}
