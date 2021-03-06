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

    public boolean appendContentToFile(String filePath, String content) { //?????????????????????

        try {
            FileWriter fileWriter =new FileWriter(filePath, true);
            fileWriter.write(content + "\n");
            fileWriter.flush();
            fileWriter.close();

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
                //??????http???????????????
                java.net.URL url = new URL(urlOrPath);
                in = url.openStream();
            }else{ //???????????????????????????
                File file = new File(urlOrPath);
                if(!file.isFile() || !file.exists() || !file.canRead()){
                    logger.info("??????????????????????????????");
                    return false;
                }
                in = new FileInputStream(file);
            }
            b = getByte(in); //?????????????????????????????????????????????
            FileUtils.writeByteArrayToFile(new File(newFilePath), b);
            result = true;
        } catch (Exception e) {
            logger.error("????????????????????????:"+ e);
            result = false;
        } finally {
            return result;
        }
    }

    private byte[] getByte(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf=new byte[1024]; //????????????
            while(in.read(buf)!=-1){ //???????????????????????????????????????????????????????????????????????????false;
                out.write(buf); //?????????????????????????????????out?????????????????????????????????????????????????????????????????????
            }
            out.flush();
            return out.toByteArray();	//?????????????????????????????????????????????????????????	????????????finally?????????return	???
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
        //???????????????????????????????????????????????????????????????Base64????????????

        InputStream in = null;
        byte[] data = null;
        //????????????????????????
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
    public String texFile(String fileName) {
        String str="";
        try{
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        str = in.readLine();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

}
