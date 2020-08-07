package com.haisheng.framework.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
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

    public boolean createDir(String dirPath) {
        File file = new File(dirPath);
        if (file.isDirectory()) {
            return true;
        } else {
            return file.mkdir();
        }

    }

    public boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.isFile();

    }
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

    public String getCurrentDirFileWithoutDeepTraverse(String folderPath, String fullFileNameMatch) {

        File dir = new File(folderPath);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                if (children[i].equals(fullFileNameMatch)) {
                    return folderPath + File.separator +children[i];
                }
            }
        }
        return null;
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

    public List<String> findLinesByKey(String filePath, String key, String noKey) {
        List<String> lines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                String current = scanner.nextLine();
                if (current.contains(key) && !current.contains(noKey)) {
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

    public boolean writeContentToFile(String filePath, String content) {

        try {
            FileUtils.writeStringToFile(new File(filePath), content, "UTF-8");
        } catch (IOException e) {
            logger.error(e.toString());
            return false;
        }

        return true;
    }

    public void downloadImageMana(String picA,String terminalPath) {

        URL url = null;

        try {
            url = new URL(picA);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(terminalPath));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public boolean downloadImage(String urlOrPath, String newFilePath){
        boolean result = false;

        InputStream in = null;
        try {
            byte[] b ;
            if(urlOrPath.toLowerCase().startsWith("http")){
                //加载http途径的图片
                java.net.URL url = new URL(urlOrPath);
                in = url.openStream();
            }else{ //加载本地路径的图片
                File file = new File(urlOrPath);
                if(!file.isFile() || !file.exists() || !file.canRead()){
                    logger.info("图片不存在或文件错误");
                    return false;
                }
                in = new FileInputStream(file);
            }
            b = getByte(in); //调用方法，得到输出流的字节数组
            FileUtils.writeByteArrayToFile(new File(newFilePath), b);
            result = true;
        } catch (Exception e) {
            logger.error("读取图片发生异常:"+ e);
            result = false;
        } finally {
            return result;
        }
    }

    private byte[] getByte(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf=new byte[1024]; //缓存数组
            while(in.read(buf)!=-1){ //读取输入流中的数据放入缓存，如果读取完则循环条件为false;
                out.write(buf); //将缓存数组中的数据写入out输出流，如果需要写到文件，使用输出流的其他方法
            }
            out.flush();
            return out.toByteArray();	//将输出流的结果转换为字节数组的形式返回	（先执行finally再执行return	）
        } finally{
            if(in!=null){
                in.close();
            }
            if(out!=null){
                out.close();
            }
        }
    }

    public void clearInfoForFile(String fileName) {
        File file =new File(fileName);
        try {
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getImgStr(String imgFile){
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try
        {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

}
