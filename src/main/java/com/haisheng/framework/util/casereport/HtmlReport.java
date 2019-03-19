package com.haisheng.framework.util.casereport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

public class HtmlReport {
    //1,server,caseContent, interfaceName, key_words,time, testResult, errorMsg

    private String path           = HtmlReport.class.getClassLoader().getResource("").getPath();
    private String[] split        = path.split("/");
    private String workSp         = split[split.length-3];

    private String testReportName  = "TEST-REPORT";
    private String testngReportUrl = "http://ci.sankuai.com/job/pingtai-beijing/job/"+workSp+"/ws/report/"+testReportName;

    private String HTML_REPORT_MODEL_PATH = "src/main/resources/report/html-report-model.html";
    private String REPORT_DETAIL_INFO_KEY = "jsonFileName";

    public void createReport(ReportSummary reportSummary, String timeStamp, String priority){

        updateHtmlReportModel(timeStamp);
        createResultShowHtml(reportSummary, timeStamp, priority);
        restoreHtmlReportModel(timeStamp);

    }

    private void updateHtmlReportModel(String timeStamp) {
        try {
            //读取报告模板
            File htmlModel = new File(HTML_REPORT_MODEL_PATH);
            //将html-report-model.html中的内容替换为timeStamp.json
            String newReportModel = FileUtils.readFileToString(htmlModel, "UTF-8").replace(REPORT_DETAIL_INFO_KEY, timeStamp + ".json");
            FileUtils.writeStringToFile(htmlModel, newReportModel, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createResultShowHtml(ReportSummary reportSummary, String timeStamp, String priority) {
        //读取更新json内容后的报告模板，并更新tile和总数据信息
        try {
            File htmlModel = new File(HTML_REPORT_MODEL_PATH);
            Document doc = Jsoup.parse(htmlModel,"UTF-8");
            //设置title
            setTitle(doc,testReportName + "_" + timeStamp);
            //插入总数信息
            writeTotallInfo(reportSummary, doc);
            //生成展示结果的html："report/" + testReportName + "_" + timeStamp + ".html"
            File reportHtml   = new File("report/" + testReportName + "_" + timeStamp + ".html");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportHtml),"utf-8"));
            bw.write(doc.html());
            bw.close();

            //将报告的url地址写入文件: testReportUrl.txt
            BufferedWriter bufferNotify = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream("testReportUrl.txt", true),
                            "utf-8"
                    )
            );
            if (!priority.isEmpty()) {
                bufferNotify.write(priority + "#" + testngReportUrl + "_" + timeStamp + ".html");
            }
            else {
                bufferNotify.write(testngReportUrl + "_" + timeStamp + ".html");
            }
            bufferNotify.close();

            //将报告的url地址写入历史记录文件  为了周报的时候获取
            FileUtils.write(
                    new File("report/testReportUrlHistory.txt"),
                    "\n" + testngReportUrl + "_" + timeStamp + ".html",
                    "UTF-8",
                    true
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void restoreHtmlReportModel(String timeStamp) {
        //恢复ReportMode.html, 方便下次使用
        //读取报告模板
        File htmlReport = new File(HTML_REPORT_MODEL_PATH);

        //将html-report-model.html中的内容替换为timeStamp.json
        try {
            String newReportModel = FileUtils.readFileToString(htmlReport, "UTF-8").replace(timeStamp + ".json",REPORT_DETAIL_INFO_KEY);
            FileUtils.writeStringToFile(htmlReport, newReportModel, "UTF-8", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTotallInfo(ReportSummary reportSummary, Document doc) {
        Elements total = doc.select("#total");
        //获取结果中的分类数据
        int passed      = reportSummary.getPassCaseCount();
        int failed      = reportSummary.getFailCaseCount();
        int warning     = reportSummary.getWarnCaseCount();
        int totalNumber = reportSummary.getTotalCaseCount();
        System.out.println("passed" + passed);
        System.out.println("failed" + failed);
        System.out.println("totalNumber" + totalNumber);
        DecimalFormat df = new DecimalFormat("0.00");
        double pR = 0;

        //计算通过率
        if (totalNumber != 0) {
            pR = ((double) passed / (double) totalNumber) * 100;
        }
        String passRate=String.valueOf(df.format(pR));
        String element="<tr>"
                +"<td>"+String.valueOf(totalNumber)+"</td>"
                +"<td tyle='color:bule;'>"+String.valueOf(passed)+"</td>"
                +"<td tyle='color:red;'>"+String.valueOf(failed)+"</td>"
                +"<td tyle='color:green;'>"+String.valueOf(warning)+"</td>"
                +"<td>"+reportSummary.getRunTotalSecs()+"</td>"
                +"<td>"+passRate+"%"+"</td>"
                +"</tr>";
        total.append(element);

    }

    private void setTitle(Document doc, String reportName) {
        //设置标题
        Element title = doc.select("title").first();
        title.text(reportName);
        Element h1 = doc.select("h1").first();
        h1.text(reportName);
    }

}

