package com.haisheng.framework.testng.bigScreen.jiaochenonline.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumDingTalkWebHook;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.customer.EnumAppletToken;
import com.haisheng.framework.testng.bigScreen.jiaochenonline.ScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.aliyun.openservices.shade.com.alibaba.rocketmq.common.UtilAll.deleteFile;

public class JcPc_SystemLogOnline extends TestCaseCommon implements TestCaseStd {
    ScenarioUtilOnline jc = new ScenarioUtilOnline();
    public String shopId = "-1";
    public String appletTocken = EnumAppletToken.JC_GLY_ONLINE.getToken();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        //替换checklist的相关信息
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "郭丽雅";
        commonConfig.produce = EnumProduce.JC.name();
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "jc-onLine-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JIAOCHEN_ONLINE.getName() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = EnumDingTalkWebHook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP.getWebHook();
        //放入shopId
        commonConfig.shopId = EnumTestProduce.JIAOCHEN_ONLINE.getShopId();
        beforeClassInit(commonConfig);
        logger.debug("jc: " + jc);
        commonConfig.referer = EnumTestProduce.JIAOCHEN_ONLINE.getReferer();

    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        jc.pcLogin("15711200001", "000000");
    }

    /**
     * @description :系统日志-数据一致性1:导入成功条数=导入条数-失败条数
     * @date :2020/12/21
     **/
    @Test
    public void SystemLog_Date1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.importListFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String pages = respon.getString("pages");
                for (int page = 1; page <= Integer.parseInt(pages); page++) {
                    JSONArray list = jc.importListFilterManage(shopId, String.valueOf(page), "10", "", "").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        //导入总数
                        String imporNum = list.getJSONObject(i).containsKey("import_num") ? list.getJSONObject(i).getString("import_num") : "0";
                        //导入成功条数
                        String successNum = list.getJSONObject(i).containsKey("success_num") ? list.getJSONObject(i).getString("success_num") : "0";
                        //导入失败条数
                        String failureNum = list.getJSONObject(i).containsKey("failure_num") ? list.getJSONObject(i).getString("failure_num") : "0";
                        int num = Integer.parseInt(successNum) + Integer.parseInt(failureNum);
                        Preconditions.checkArgument(Integer.valueOf(imporNum).equals(num), "导入条数为:" + imporNum + "  导入失败的条数为:" + failureNum + "  导入成功的条数为:" + successNum);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("系统日志-数据一致性1:导入成功条数=导入条数-失败条数");
        }
    }

    /**
     * @description :系统日志-数据一致性2:导入成功条数<=导入条数
     * @date :2020/12/21
     **/
    @Test
    public void SystemLog_Date2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.importListFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String pages = respon.getString("pages");
                for (int page = 1; page <= Integer.parseInt(pages); page++) {
                    JSONArray list = jc.importListFilterManage(shopId, String.valueOf(page), "10", "", "").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        //导入总数
                        String imporNum = list.getJSONObject(i).containsKey("import_num") ? list.getJSONObject(i).getString("import_num") : "0";
                        //导入成功条数
                        String successNum = list.getJSONObject(i).containsKey("success_num") ? list.getJSONObject(i).getString("success_num") : "0";
                        Preconditions.checkArgument(Integer.parseInt(imporNum) >= Integer.parseInt(successNum), "导入条数为:" + imporNum + "  导入成功的条数为:" + successNum);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("系统日志-数据一致性2:导入成功条数<=导入条数");
        }
    }

    /**
     * @description :系统日志-数据一致性3:导入失败条数<=导入条数
     * @date :2020/12/21
     **/
    @Test
    public void SystemLog_Date3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.importListFilterManage(shopId, "1", "10", "", "");
            if (respon.getJSONArray("list").size() > 0) {
                String pages = respon.getString("pages");
                for (int page = 1; page <= Integer.parseInt(pages); page++) {
                    JSONArray list = jc.importListFilterManage(shopId, String.valueOf(page), "10", "", "").getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        //导入总数
                        String imporNum = list.getJSONObject(i).containsKey("import_num") ? list.getJSONObject(i).getString("import_num") : "0";
                        //导入失败条数
                        String failureNum = list.getJSONObject(i).containsKey("failure_num") ? list.getJSONObject(i).getString("failure_num") : "0";
                        System.out.println("导入条数为:" + imporNum + "  导入失败的条数为:" + failureNum);
                        Preconditions.checkArgument(Integer.parseInt(imporNum) >= Integer.parseInt(failureNum), "导入条数为:" + imporNum + "  导入失败的条数为:" + failureNum);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("系统日志-数据一致性3:导入失败条数<=导入条数");
        }
    }

    /**
     * @description :系统日志-数据一致性4:筛选栏选择【导入工单】==列表展示全部的数据
     * @date :2020/12/21
     **/
    @Test
    public void SystemLog_Date4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //筛选栏直接搜索
            JSONObject respon = jc.importListFilterManage(shopId, "1", "10", "", "");
            int total = respon.getInteger("total");
            //筛选栏选择[导入工单]
            JSONObject respon1 = jc.importListFilterManage(shopId, "1", "10", "type", "AFTER_CUSTOMER");
            int total1 = respon1.getInteger("total");
            System.out.println("---------" + total + "---------" + total1);
            Preconditions.checkArgument(total == total1, "搜索栏直接搜索的条数为:" + total + "  筛选栏选择[导入工单]" + total1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("统日志-数据一致性4:筛选栏选择【导入工单】==列表展示全部的数据");
        }
    }

    /**
     * @description :系统日志-数据一致性5:导入记录里面导入的条数＜＝５０００
     * @date :2020/12/21
     **/
    @Test(enabled = false)
    public void SystemLog_Date5() {
        logger.logCaseStart(caseResult.getCaseName());
        Workbook wb = null;
        String path = "C:\\work\\downloadFile";
        File fileDir = new File(path);
        try {
            JSONObject respond = jc.importListFilterManage(shopId, "1", "10", "", "");
            JSONArray list = respond.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                String url = list.getJSONObject(i).getString("file_upload_url");
                //下载第一页的10个excel文件   下载\批量新建顾客表单.xlsx
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            }
            // 判断是否是文件夹
            if (fileDir.isDirectory()) {
                File[] fileList = fileDir.listFiles();
                for (int i = 0; i < 10; i++) {
                    //读取文件的条数
//                    FileInputStream inp = new FileInputStream(fileList[i].getPath());
//                    wb = new XSSFWorkbook(inp);
//                    Sheet sheet = wb.getSheetAt(0);
//                    CellReference cellReference = new CellReference("A1");
//                    boolean flag = false;
//                    for (int j = cellReference.getRow(); j <= sheet.getLastRowNum();) {
//                        Row r = sheet.getRow(j);
//                        if(r == null){
//                            // 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
//                            sheet.shiftRows(j+1, sheet.getLastRowNum(),-1);
//                            continue;
//                        }
//                        flag = false;
//                        for(Cell c:r){
//                            if(c.getCellType()!= Cell.CELL_TYPE_BLANK){
//                                flag = true;
//                                break;
//                            }
//                        }
//                        if(flag){
//                            j++;
//                            continue;
//                        }
//                        else{
//                            //如果是空白行（即可能没有数据，但是有一定格式）
//                            if(j == sheet.getLastRowNum()){
//                                //如果到了最后一行，直接将那一行remove掉
//                                sheet.removeRow(r);
//                            }
//                            else{
//                                //如果还没到最后一行，则数据往上移一行
//                                sheet.shiftRows(j+1, sheet.getLastRowNum(),-1);
//                            }
//                        }
//                    }
//                    System.out.println("第"+i+"文件的总行数    "+"总行数：" + sheet.getLastRowNum());
//                    Preconditions.checkArgument(sheet.getLastRowNum()<5000,"导入工单的条数大于5000条");
                }
            }
            //清空文件夹中的所有文件
            deleteFile(fileDir);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("统日志-数据一致性5:筛选栏选择【导入工单】==列表展示全部的数据");
        }

    }

    /**
     * @description :系统日志-数据一致性6:小程序客户查看通知消息的内容+1==PC消息记录中客户查看为【是】+1(发送一条消息。)
     * @date :2020/12/21
     **/
    @Test
    public void SystemLog_Date6() {
        logger.logCaseStart(caseResult.getCaseName());
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String messageContent = "这个一个自动化发送的消息呀,早上好呀";
        String messageName = "早上好呀-自动化 " + df.format(date);
        ArrayList<String> phone = new ArrayList();
        phone.add("13373166806");
        try {
            //推送个人消息-13373166806
            jc.pushMessage(true, messageContent, messageName, "PERSONNEL_CUSTOMER", phone);
            //查看消息记录中的第一条消息
            JSONObject respon = jc.pushMsgListFilterManage("", "1", "10", "", "");
            String isReadBefore = respon.getJSONArray("list").getJSONObject(0).getString("is_read");
            //登录小程序
            jc.appletLoginToken(appletTocken);
            //查看消息
            JSONArray list = jc.appletmessageList("", "10").getJSONArray("list");
            String id = list.getJSONObject(0).getString("id");
            jc.messageDetail(id);
            //查看现在的-客户查看
            jc.pcLogin("15711200001", "000000");
            String isReadAfter = jc.pushMsgListFilterManage("", "1", "10", "", "").getJSONArray("list").getJSONObject(0).getString("is_read");
            System.out.println("客户查看之前的状态为:" + isReadBefore + "现在客户查看的状态为:" + isReadAfter);
            Preconditions.checkArgument(isReadAfter.equals("true"), "客户查看之前的状态为:" + isReadBefore + "现在客户查看的状态为:" + isReadAfter);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("系统日志-数据一致性6:小程序客户查看通知消息的内容＋1==PC消息记录中客户查看为【是】＋1");
        }
    }

    /**
     * @description :系统日志-数据一致性7:【营销管理】里面消息推送给个人,消息记录中消息+1
     * @date :2020/12/21
     **/
    @Test
    public void SystemLog_Date7() {
        logger.logCaseStart(caseResult.getCaseName());
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String messageContent = "这个一个自动化发送的消息呀-撒浪嘿哟";
        String messageName = "撒浪嘿哟-自动化 " + df.format(date);
        ArrayList<String> phone = new ArrayList();
        phone.add("13373166806");
        try {
            //查看消息记录的总条数
            JSONObject respon = jc.pushMsgListFilterManage("", "1", "10", "", "");
            int total = respon.getInteger("total");
            //推送个人消息-13373166806
            jc.pushMessage(true, messageContent, messageName, "PERSONNEL_CUSTOMER", phone);
            //推送消息以后再次查看消息记录的总条数
            JSONObject respon1 = jc.pushMsgListFilterManage("", "1", "10", "", "");
            int total1 = respon1.getInteger("total");
            Preconditions.checkArgument(total1 == total + 1, "推送消息之前的消息记录总条数为:" + total + "推送消息之后的的消息记录总条数为:" + total1);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("系统日志-数据一致性7:【营销管理】里面消息推送给个人,消息记录中消息+1");
        }
    }

    /**
     * @description :系统日志-数据一致性8:营销管理中消息推送中【选择客户】的数量=【营销管理】中消息管理里面【发出条数】=消息记录中新增的记录条数
     * @date :2020/12/21
     **/
    @Test
    public void SystemLog_Date8() {
        logger.logCaseStart(caseResult.getCaseName());
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String messageContent = "啦啦啦啦啦啦啦啦啦-自动化";
        String messageName = "嘿嘿哈嘿-自动化" + df.format(date);
        ArrayList<String> phone = new ArrayList();
        phone.add("13373166806");
        try {
            //查看消息记录的总条数
            JSONObject respon = jc.pushMsgListFilterManage("", "1", "10", "", "");
            int total = respon.getInteger("total");
            //推送个人消息-13373166806
            jc.pushMessage(true, messageContent, messageName, "PERSONNEL_CUSTOMER", phone);
            int sendCount = jc.messageFormFilterManage("", "1", "10", "customer_name", "哈哈哈").getJSONArray("list").getJSONObject(0).getInteger("send_count");
            int receiveCount = jc.messageFormFilterManage("", "1", "10", "customer_name", "哈哈哈").getJSONArray("list").getJSONObject(0).getInteger("receive_count");
            //推送消息以后再次查看消息记录的总条数
            JSONObject respon1 = jc.pushMsgListFilterManage("", "1", "10", "", "");
            int total1 = respon1.getInteger("total");
            //消息记录新增的数量
            int num = total1 - total;
            Preconditions.checkArgument(num == 1 && sendCount == 1, "推送消息之后消息新增的数量:" + (total1 - total) + "消息记录中发出的消息为:" + sendCount + " 原本消息推送的人数为1人");
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("系统日志-数据一致性8:【营销管理】里面消息推送给个人,消息记录中消息+1");
        }
    }

    /**
     * @description :系统日志-数据一致性9:【营销管理】中同一批次 消息管理里面收到条数>=消息记录中客户查看为【是】的客户---选择中关村门店
     * @date :2020/12/21
     **/
    @Test
    public void SystemLog_Date9() {
        logger.logCaseStart(caseResult.getCaseName());
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String messageContent = "推送中关村门店-自动化";
        String messageName = "推送中关村门店-自动化" + df.format(date);
        ArrayList<String> shop = new ArrayList();
        shop.add("20032");
        int isReadNum = 0;
        try {
            //查看消息记录的总条数
            JSONObject respon = jc.pushMsgListFilterManage("", "1", "10", "", "");
            int total = respon.getInteger("total");
            //推送中关村门店消息
            jc.pushMessageShop(true, messageContent, messageName, "SHOP_CUSTOMER", shop);
            int receiveCount = jc.messageFormFilterManage("", "1", "10", "shop_id", "20032").getJSONArray("list").getJSONObject(0).getInteger("receive_count");
            //推送消息以后再次查看消息记录的总条数
            JSONObject respon1 = jc.pushMsgListFilterManage("", "1", "100", "", "");
            int total1 = respon1.getInteger("total");
            //消息记录新增的数量
            int num = total1 - total;
            //消息记录查看为是的个数
            for (int i = 0; i < num; i++) {
                String isRead = respon1.getJSONArray("list").getJSONObject(i).getString("is_read");
                if (isRead.equals("true")) {
                    isReadNum++;
                }
            }
            Preconditions.checkArgument(receiveCount > isReadNum || receiveCount == isReadNum, "同一批次消息记录中查看为是的消息为:" + isReadNum + "消息表单中收到的消息为:" + receiveCount);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("系统日志-数据一致性9:【营销管理】里面消息推送给门店,消息记录中消息+门店对应的人数");
        }
    }

    /**
     * @description :系统日志-功能:导入记录列表项不为空校验---校验前2页的数据
     * @date :2020/12/22
     **/
    @Test
    public void SystemLog_System1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.importListFilterManage(shopId, "1", "20", "", "");
            JSONArray list = respon.getJSONArray("list");
            for (int i = 0; i < 10; i++) {
                String affiliation = list.getJSONObject(i).getString("affiliation");
                String typeName = list.getJSONObject(i).getString("type_name");
                String importTime = list.getJSONObject(i).getString("import_time");
                String fileType = list.getJSONObject(i).getString("file_type");
                String importNum = list.getJSONObject(i).getString("import_num");
                String successNum = list.getJSONObject(i).getString("success_num");
                String failureNum = list.getJSONObject(i).getString("failure_num");
                String operateShopName = list.getJSONObject(i).getString("operate_shop_name");
                String userName = list.getJSONObject(i).getString("user_name");
                String userAccount = list.getJSONObject(i).getString("user_account");
                String fileUploadUrl = list.getJSONObject(i).getString("file_upload_url");
                Preconditions.checkArgument(affiliation != null && typeName != null && importTime != null && fileType != null && importNum != null && successNum != null && failureNum != null && operateShopName != null && userName != null && userAccount != null && fileUploadUrl != null, "导入记录前20行列表项中存在列表项为空的行数为:" + i);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("系统日志-功能1:导入记录列表项不为空校验");
        }
    }

    /**
     * @description :系统日志-功能:消息记录列表项不为空校验---校验前5页的数据
     * @date :2020/12/22
     **/
    @Test
    public void SystemLog_System2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject respon = jc.pushMsgListFilterManage(shopId, "1", "50", "", "");
            JSONArray list = respon.getJSONArray("list");
            for (int i = 0; i < 50; i++) {
                String messageTypeName = list.getJSONObject(i).getString("message_type_name");
                String phone = list.getJSONObject(i).getString("phone");
                String sendTime = list.getJSONObject(i).getString("send_time");
                String content = list.getJSONObject(i).getString("content");
                String isRead = list.getJSONObject(i).getString("is_read");
                Preconditions.checkArgument(messageTypeName != null && phone != null && sendTime != null && content != null && isRead != null, "消息记录前50行列表项中存在列表项为空的行数为:" + i);
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("系统日志-功能2:消息记录列表项不为空校验");
        }
    }


}


