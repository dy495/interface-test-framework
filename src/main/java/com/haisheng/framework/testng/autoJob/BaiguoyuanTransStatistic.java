package com.haisheng.framework.testng.autoJob;

import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BaiguoyuanTransStatistic {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    FileUtil fileUtil = new FileUtil();
    DateTimeUtil dt   = new DateTimeUtil();

    String TRANS_FILE = System.getProperty("TRANS_REPORT_FILE");

    String keyXianxia = "线下:";
    String keySanfang = "三方";
    String keyApp = "APP";
    String keyNophone = "无电话";
    String keyRmb = "人民币";
    String keyCreditcard = "信用卡";
    String keyWeizhifu = "微支付";
    String keyZhifubao = "支付宝";
    String keyChongzhi = "会员储值";
    String keyXianxiaOthter = "线下其他";

    boolean IS_DEBUG = true;



    @Test
    private void statistic() {

        if (IS_DEBUG) {
            List<File> fileList = fileUtil.getCurrentDirFilesWithoutDeepTraverse("/Users/yuhaisheng/jason/document/work/项目/百果园/trans/statistic", ".csv");
            for (File file : fileList) {
                String filefullname = file.getAbsolutePath();

                statisticAndSaveData(filefullname);
            }

            fileList = fileUtil.getCurrentDirFilesWithoutDeepTraverse("/Users/yuhaisheng/jason/document/work/项目/百果园/trans/statistic/result", "statistic");
            statisticAllAndSaveData(fileList);
        } else {
            statisticAndSaveData(TRANS_FILE);
        }


    }

    private void statisticAllAndSaveData(List<File> fileList) {

        ConcurrentHashMap<String, Integer> result = new ConcurrentHashMap<String, Integer>();
        for (File file : fileList) {
            statisticAllData(file, result);
        }

        List<String> content = changeAllResultToList(result);

        fileUtil.writeContentToFile(fileList.get(0).getParent() + File.separator + "all.result", content);
    }

    private List<String> changeAllResultToList(ConcurrentHashMap<String, Integer> result) {
        List<String> content = new ArrayList<>();

        int numxianxia = result.get(keyXianxia);
        int numsanfang = result.get(keySanfang);
        int numapp     = result.get(keyApp);
        int total      = numxianxia + numsanfang + numapp;

        int numNoPhone = result.get(keyNophone);
        int numRmb     = result.get(keyRmb);
        int numCreditcard = result.get(keyCreditcard);
        int numWeizhifu   = result.get(keyWeizhifu);
        int numZhifubao   = result.get(keyZhifubao);
        int numChongzhi   = result.get(keyChongzhi);
        int numOther      = result.get(keyXianxiaOthter);

        DecimalFormat df = new DecimalFormat("#.00");
        String all     = "TOTAL: " + total;
        String xianxia = "线下:   " + numxianxia + ",     占比: " + String.valueOf(df.format((float)numxianxia*100/(float)total)) + "%";
        String sanfang = "三方:   " + numsanfang + ",      占比: " + String.valueOf(df.format((float)numsanfang*100/(float)total)) + "%";
        String app     = "APP:   " + numapp     + ",      占比: " + String.valueOf(df.format((float)numapp*100/(float)total)) + "%";

        String noPhone    = "无电话:  " + numNoPhone    + ",      占比: " + String.valueOf(df.format((float)numNoPhone*100/(float)numxianxia)) + "%";
        String rmb        = "人民币:  " + numRmb        + ",      占比: " + String.valueOf(df.format((float)numRmb*100/(float)numxianxia)) + "%";
        String creditcard = "信用卡:  " + numCreditcard + ",       占比: " + String.valueOf(df.format((float)numCreditcard*100/(float)numxianxia)) + "%";
        String weizhifu   = "微支付:  " + numWeizhifu   + ",     占比: " + String.valueOf(df.format((float)numWeizhifu*100/(float)numxianxia)) + "%";
        String zhifubao   = "支付宝:  " + numZhifubao   + ",      占比: " + String.valueOf(df.format((float)numZhifubao*100/(float)numxianxia)) + "%";
        String chongzhi   = "会员储值: " + numChongzhi   + ",      占比: " + String.valueOf(df.format((float)numChongzhi*100/(float)numxianxia)) + "%";
        String other      = "线下其他: " + numOther      + ",      占比: " + String.valueOf(df.format((float)numOther*100/(float)numxianxia)) + "%";


        content.add("交易类型统计：");
        content.add(all);
        content.add("");
        content.add(xianxia);
        content.add(sanfang);
        content.add(app);
        content.add("");
        content.add("");

        content.add("线下支付类型统计：");
        content.add(noPhone);
        content.add(rmb);
        content.add(creditcard);
        content.add(weizhifu);
        content.add(zhifubao);
        content.add(chongzhi);
        content.add(other);

        return content;
    }

    private void putHm(ConcurrentHashMap<String, Integer> result, String key, Integer value) {
        if (result.containsKey(key)) {
            result.put(key, result.get(key) + value);
        } else {
            result.put(key, value);
        }

    }
    private void statisticAllData(File file, ConcurrentHashMap<String, Integer> result) {

        List<String> content = fileUtil.getFileContent(file.getAbsolutePath());
        for (String line : content) {
            int begin = line.indexOf(":")+1;
            int end = line.indexOf(",");
            if (line.contains(keyXianxia)) {
                String value = line.substring(begin, end).trim();
                putHm(result, keyXianxia, Integer.parseInt(value));

            } else if (line.contains(keySanfang)) {
                String value = line.substring(begin, end).trim();
                putHm(result, keySanfang, Integer.parseInt(value));

            } else if (line.contains(keyApp)) {
                String value = line.substring(begin, end).trim();
                putHm(result, keyApp, Integer.parseInt(value));

            } else if (line.contains(keyNophone)) {
                String value = line.substring(begin, end).trim();
                putHm(result, keyNophone, Integer.parseInt(value));

            } else if (line.contains(keyRmb)) {
                String value = line.substring(begin, end).trim();
                putHm(result, keyRmb, Integer.parseInt(value));

            } else if (line.contains(keyCreditcard)) {
                String value = line.substring(begin, end).trim();
                putHm(result, keyCreditcard, Integer.parseInt(value));

            } else if (line.contains(keyWeizhifu)) {
                String value = line.substring(begin, end).trim();
                putHm(result, keyWeizhifu, Integer.parseInt(value));

            } else if (line.contains(keyZhifubao)) {
                String value = line.substring(begin, end).trim();
                putHm(result, keyZhifubao, Integer.parseInt(value));

            } else if (line.contains(keyChongzhi)) {
                String value = line.substring(begin, end).trim();
                putHm(result, keyChongzhi, Integer.parseInt(value));

            } else if (line.contains(keyXianxiaOthter)) {
                String value = line.substring(begin, end).trim();
                putHm(result, keyXianxiaOthter, Integer.parseInt(value));

            } else {
                continue;
            }
        }

    }
    private void statisticAndSaveData(String filefullname) {

        //statistic data
        List<String> content = statisticData(filefullname);

        //save result to file
        String pDir = filefullname.substring(0, filefullname.lastIndexOf("/"));
        String filename = filefullname.substring(filefullname.lastIndexOf("/") + 1);
        fileUtil.writeContentToFile(pDir + File.separator + "result" + File.separator + filename + ".statistic", content);
    }

    private List<String> statisticData(String filefullname) {

        List<String> content = fileUtil.getFileContent(filefullname);
        List<String> resultList = new ArrayList<>();
        int numsanfang = 0;
        int numapp   = 0;
        int numother = 0;

        for (String line : content) {

            if (line.contains("小票号") || line.contains("小票查询") || line.contains(",,,")) {
                continue;

            } else if (line.contains("线下")) {
                resultList.add(line);

            } else if (line.contains("三方")) {
                numsanfang++;

            } else if (line.contains("APP")) {
                numapp++;

            } else {
                numother++;
            }
        }

        int total = resultList.size() + numapp + numsanfang + numother;
        List<String> statisticList = calResult(total, resultList.size(), numsanfang, numapp, numother);

        List<String> xianxiaList = analysisXianxia(resultList);
        for (String item : xianxiaList) {
            statisticList.add(item);
        }

        return statisticList;

    }

    private List<String> calResult(int total, int numxianxia, int numsanfang, int numapp, int numother) {

        List <String> statisticList = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#.00");
        String all     = "TOTAL: " + total;
        String xianxia = "线下:   " + numxianxia + ",     占比: " + String.valueOf(df.format((float)numxianxia*100/(float)total)) + "%";
        String sanfang = "三方:   " + numsanfang + ",      占比: " + String.valueOf(df.format((float)numsanfang*100/(float)total)) + "%";
        String app     = "APP:   " + numapp     + ",      占比: " + String.valueOf(df.format((float)numapp*100/(float)total)) + "%";
        String other   = "其他:   " + numother + ",       占比: " + String.valueOf(df.format((float)numother*100/(float)total)) + "%";

        statisticList.add(all);
        statisticList.add(xianxia);
        statisticList.add(sanfang);
        statisticList.add(app);
        statisticList.add(other);
        statisticList.add("");
        statisticList.add("");

        return statisticList;
    }

    private List<String> analysisXianxia(List<String> resultContent) {

        int numNoPhone = 0;
        int chongzhi   = 0;
        int weizhifu   = 0;
        int zhifubao   = 0;
        int rmb        = 0;
        int creditcard = 0;
        int other      = 0;

        for (String line : resultContent) {
            String[] itemArray = line.split(",");
            if (StringUtils.isEmpty(itemArray[4].trim()) || itemArray[4].trim().length() < 11) {
                numNoPhone++;
            }
            if (itemArray[3].contains("会员储值")) {
                chongzhi++;
            } else if (itemArray[3].contains("微支付")) {
                weizhifu++;
            } else if (itemArray[3].contains("支付宝")) {
                zhifubao++;
            } else if (itemArray[3].contains("人民币")) {
                rmb++;
            } else if (itemArray[3].contains("信用卡")) {
                creditcard++;
            } else {
                other++;
            }
        }

        int total = resultContent.size();
        resultContent = null;

        List<String> xianxiaContent = calXianxia(total, numNoPhone, chongzhi, weizhifu, zhifubao, rmb, creditcard, other);
        return xianxiaContent;
    }

    private List<String> calXianxia(int total, int numNoPhone,
                                    int numChongzhi, int numWeizhifu,
                                    int numZhifubao, int numRmb,
                                    int numCreditcard, int numOther) {

        List <String> statisticList = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#.00");
        String noPhone    = "无电话:  " + numNoPhone    + ",      占比: " + String.valueOf(df.format((float)numNoPhone*100/(float)total)) + "%";
        String rmb        = "人民币:  " + numRmb        + ",      占比: " + String.valueOf(df.format((float)numRmb*100/(float)total)) + "%";
        String creditcard = "信用卡:  " + numCreditcard + ",       占比: " + String.valueOf(df.format((float)numCreditcard*100/(float)total)) + "%";
        String weizhifu   = "微支付:  " + numWeizhifu   + ",     占比: " + String.valueOf(df.format((float)numWeizhifu*100/(float)total)) + "%";
        String zhifubao   = "支付宝:  " + numZhifubao   + ",      占比: " + String.valueOf(df.format((float)numZhifubao*100/(float)total)) + "%";
        String chongzhi   = "会员储值: " + numChongzhi   + ",      占比: " + String.valueOf(df.format((float)numChongzhi*100/(float)total)) + "%";
        String other      = "线下其他: " + numOther      + ",      占比: " + String.valueOf(df.format((float)numOther*100/(float)total)) + "%";

        statisticList.add(noPhone);
        statisticList.add(rmb);
        statisticList.add(creditcard);
        statisticList.add(weizhifu);
        statisticList.add(zhifubao);
        statisticList.add(chongzhi);
        statisticList.add(other);
        statisticList.add("");
        statisticList.add("");

        return statisticList;
    }



}
