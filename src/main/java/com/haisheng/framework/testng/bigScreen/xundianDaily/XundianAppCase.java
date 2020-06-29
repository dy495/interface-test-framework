package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XundianAppCase extends TestCaseCommon implements TestCaseStd {

    xundianScenarioUtilX xd = xundianScenarioUtilX.getInstance();
    public Long shop_idX=28762L;  //巡检员现场巡店的店铺id
    //app 巡检员账号
    public String adminNamex = "xunjianyuan3@winsense.ai";
    public String adminPasswdx = "e10adc3949ba59abbe56e057f20f883e";
    //app对应店长账号
    public String dzName="dianzhang3@winsense.ai";
    public String dzPassword="e10adc3949ba59abbe56e057f20f883e";

    //主账号
     public String adminName = "yuexiu@test.com";
     public String adminPasswd = "f5b3e737510f31b88eb2d4b5d0cd2fb4";
    public String filepath="src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/64.txt";  //巡店不合格图片base64
    //读取文件内容
    public String texFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str;
        str = in.readLine();
        return str;
    }
    public String getPicList (String filename) throws Exception {
        String pic_data0=texFile(filepath);
        JSONObject pic=xd.picUpload(1,pic_data0);
        String pic_list0=pic.getString("pic_path");
        return pic_list0;
    }


    //app定检任务 巡店全合格

    public Integer xundianapp(Long shop_id,String comment,String check_type,Long task_id){
        int qualifiedCount = 0;
        try {
            //重新巡店
            JSONObject list = xd.checkStartapp(shop_id, check_type,1,task_id);
            long patrol_id = list.getLong("id");//巡检记录id
            JSONArray check_lists = list.getJSONArray("check_lists");  //执行清单列表
            if (check_lists.size() <= 0) {
                logger.info("该门店未配置执行清单");
                return 0;
            }

            for (int i = 0; i < check_lists.size(); i++) {
                JSONArray check_items = check_lists.getJSONObject(i).getJSONArray("check_items");  //执行清单项
                long listId2 = check_lists.getJSONObject(i).getLong("id");
                for (int j = 0; j < check_items.size(); j++) {
                    long itemId2 = check_items.getJSONObject(j).getLongValue("id");
                    xd.appsubmit(shop_id,1,itemId2,listId2,patrol_id);  //提交单个清单项，默认合格
                    qualifiedCount = qualifiedCount + 1;
                }
            }
            xd.appcheckSubmit(shop_id,comment, patrol_id);  //巡店完成提交
//            xd.checkSubmit("自动化提交全部合格xiaxia",patrol_id);  //巡店完成提交

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc远程巡店");
        }
        return qualifiedCount;
    }

    //app定检任务巡店全不合格

    public Integer xundianappNO(Long shop_id,String comment,String check_type,Long task_id,String date){
        int qualifiedCount = 0;
        int unqualifiedCount = 0;

        try {
            //重新巡店
            JSONObject list = xd.checkStartapp(shop_id, check_type,1,task_id);
            long patrol_id = list.getLong("id");//巡检记录id
            JSONArray check_lists = list.getJSONArray("check_lists");  //执行清单列表
            if (check_lists.size() <= 0) {
                logger.info("该门店未配置执行清单");
                return 0;
            }

            for (int i = 0; i < check_lists.size(); i++) {
                JSONArray check_items = check_lists.getJSONObject(i).getJSONArray("check_items");  //执行清单项
                long listId2 = check_lists.getJSONObject(i).getLong("id");
                for (int j = 0; j < check_items.size(); j++) {
                    long itemId2 = check_items.getJSONObject(j).getLongValue("id");
                    int check_result=2;
                        if(check_result==1){
                            xd.appsubmit(shop_id,1,itemId2,listId2,patrol_id);  //提交单个清单项，默认合格
                            qualifiedCount = qualifiedCount + 1;
                        }
                        else if(check_result==2){
                            ArrayList picList=new ArrayList();
                            picList=picList(shop_id,date);
                            if(picList.size()==0){
                                logger.info("该巡检任务上没有拍照");
                                throw new Exception("\"该巡检任务上没有拍照片\"");
                            }
                            xd.appSubmitN(shop_id,patrol_id,listId2,itemId2,picList);   //提交不合格图片
                            xd.appsubmit(shop_id,check_result, itemId2, listId2, patrol_id,comment);
                            unqualifiedCount = unqualifiedCount + 1;
                        }
                }
            }
            xd.appcheckSubmit(shop_id,comment, patrol_id);  //巡店完成提交

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("app定检任务");
        }
        return unqualifiedCount;
    }

    //获取定检不合格图片路径

    public ArrayList<?> picList(Long shop_id,String date)throws Exception{
        JSONObject data=xd. shopDevice(shop_id);
        JSONArray list=data.getJSONArray("list");
        //获取设备id
        String device_id=list.getJSONObject(0).getString("device_id");
        //获取定检任务定时拍摄图片
        JSONObject data2=xd.picList(shop_id,device_id,date);
        JSONArray plist=data2.getJSONArray("list");
        ArrayList<String> pp=  new ArrayList<String>();
        int count=0;
        for (int i=0;i<plist.size();i++){
            String pic_path=plist.getJSONObject(i).getJSONObject("pic").getString("pic_path");
            pp.add(pic_path);
            count++;
            //取前五张图
            if(count>=5){
                break;
            }
        }
        return pp;
    }

    //巡店  check_result=2 不合格 1 合格;
    public Integer appXundianP(Long shop_id,String check_type,String comment,int check_result) {
        int qualifiedCount = 0;
        int unqualifiedCount = 0;
        String comment2="app远程、现场巡店";//提交说明
        try {
            //重新巡店
            JSONObject list = xd.checkStartapp(shop_id,check_type, 1);
            long patrol_id = list.getLong("id");//巡检记录id
            JSONArray check_lists = list.getJSONArray("check_lists");  //执行清单列表
            if (check_lists.size() <= 0) {
                logger.info("该门店未配置执行清单");
                return 0;
            }
            for (int i = 0; i < check_lists.size(); i++) {
                JSONArray check_items = check_lists.getJSONObject(i).getJSONArray("check_items");  //执行清单项
                long listId2 = check_lists.getJSONObject(i).getLong("id");
                for (int j = 0; j < check_items.size(); j++) {
                    long itemId2 = check_items.getJSONObject(j).getLongValue("id");
                    if(check_result==1){
                        xd.appsubmit(shop_id,1,itemId2,listId2,patrol_id);  //提交单个清单项，默认合格
                        qualifiedCount = qualifiedCount + 1;
                    }
                    else if(check_result==2){
                        ArrayList picList=new ArrayList();
                        String pic_data=getPicList(filepath);
                        picList.add(pic_data);
                        xd.appSubmitN(shop_id,patrol_id,listId2,itemId2,picList);   //提交不合格图片
                        xd.appsubmit(shop_id,check_result, itemId2, listId2, patrol_id,comment);
                        unqualifiedCount = unqualifiedCount + 1;
                    }
                }
            }
            xd.appcheckSubmit(shop_id,comment2, patrol_id);  //巡店完成提交

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("pc远程巡店");
        }
        return unqualifiedCount;
    }

    /**
     * @description :1.处理未完成代办事项,定检巡店处理结果全部合格，不产生待办事项
     * @date :2020/6/26 20:46
     **/
    @Test
    public void daibanThingDingjian() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //1.店长登录，记录原始待办事项数
            xd.applogin(dzName,dzPassword);
            int tasksNum=xd.taskDetail().getInteger("tasks");
            logger.info("处理定检任务前店长待办事项数：{}",tasksNum);

            xd.applogin(adminNamex,adminPasswdx);
            //2.巡检员登录，进行一次定检巡检，记录不合格项数
            //获取待办实现未完成列表 0 待办 1已完成
            JSONObject data = xd.Task_list(0, 10, null);
            JSONArray list = data.getJSONArray("list");
            String comment="app定检巡店合格";
            for (int i = 0; i < list.size(); i++) {
                JSONObject list1 = list.getJSONObject(i);
                String task_type = list1.getString("task_type");
                long shop_id = list1.getLong("shop_id");
                Integer reset = 0;//重新开始巡店
                Long task_id = list1.getLong("id");
                if (task_type.equals("SCHEDULE_TASK")) {
                    String task_type1="SCHEDULED";
                    //第一个待办事项若是定检任务，则执行定检任务，开始巡店
                    xundianapp(shop_id, comment,task_type, task_id);
                } else {
                    continue;
                }
                xd.applogin(dzName,dzPassword);
                int tasksNumAfter=xd.taskDetail().getInteger("tasks");
                logger.info("处理定检任务后店长待办事项数：{}",tasksNumAfter);
                Preconditions.checkArgument((tasksNumAfter-tasksNum)==0,"定检巡店合格不产生待办事项");
                break;       //为调试代码只循环一次，处理一个待办事项
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("定检巡店合格不产生待办事项");
        }
    }

    /**
     * @description :2.用于账号处理待办事项，清除数据  最后执行
     * @date :2020/6/27 16:04
     **/
    @Test(priority = 9)
    public void daibanThingNumCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            xd.login(adminNamex,adminPasswdx);
            //获取待办实现未完成列表 0 待办 1已完成
            JSONObject data = xd.Task_list(0, 50, null);
            JSONArray list = data.getJSONArray("list");
            String comment="app定检巡店合格x";
            if(list.size()==0){
                logger.info("该用户没有待处理事项");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                JSONObject detail=xd.taskDetail();  //查看门店详情
                int check_shops=detail.getInteger("check_shops");    //已巡店次数
                int shops=detail.getInteger("shops");   //可巡检门店数
                int handle_tasks=detail.getInteger("handle_tasks");  //完成代办数
                int tasks=detail.getInteger("tasks");  //代办事项数

                JSONObject list1 = list.getJSONObject(i);
                String task_type = list1.getString("task_type");
                long shop_id = list1.getLong("shop_id");
                Integer reset = 0;//重新开始巡店
                Long task_id = list1.getLong("id");
                if (task_type.equals("SCHEDULE_TASK")) {
                    //第一个待办事项若是定检任务，则执行定检任务，开始巡店
                    String task_type1="SCHEDULED";
                    xundianapp(shop_id, comment,task_type1, task_id);
                } else if(task_type.equals("HANDLE_RESULT")){
                    //查看处理结果
                    String comment1="处理结果合格x";
                    xd.stepSubmit2(shop_id,task_id,comment1,0);
                }else if(task_type.equals("SCHEDULE_UNQUALIFIED")){
                    String comment3="定检不合格处理";
                    xd.stepSubmit(shop_id,task_id,comment3);
                }else if(task_type.equals("REMOTE_UNQUALIFIED")){
//                    continue;
                    String comment4="远程巡店不合格处理";
                    xd.stepSubmit(shop_id,task_id,comment4);
                }
                else {continue;}
                JSONObject detailAfter=xd.taskDetail();
                int handle_tasksAfter=detailAfter.getInteger("handle_tasks");
                //代办事项数
                int tasksAfter=detailAfter.getInteger("tasks");
                Preconditions.checkArgument((handle_tasksAfter-handle_tasks)==1,"定检任务完成，已完成待办事项没+1");
//                Preconditions.checkArgument((tasks-tasksAfter)==1,"定检任务完成，待办事项没-1");
//                break; //为调试代码只循环一次，处理一个待办事项
            }

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("处理待办事项，校验详情页待办事项数-1，已完成数+1");
        }
    }

    /**
     * @description :3.处理定检、远程、现场巡店、复检不合格，主账号增加1个【查看结果】，减少一个待处理事项， 待办事项数不变
     *               店长减少一个待办事项，巡检员增加一个【查看结果】
     *               优先级靠后等待其他用例执行完，用户待办事项中有数据
     * @date :2020/6/28 14:51
     **/
    @Test(priority = 8,dataProvider = "TASK_TYPE", dataProviderClass = xundianScenarioUtilX.class)
    public void dealdaiban(String task_type){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //1.巡检员登录，记录原始待办事项数
            xd.applogin(adminNamex,adminPasswdx);
            int tasksNum=xd.taskDetail().getInteger("tasks");
            logger.info("处理定检任务前店长待办事项数：{}",tasksNum);

            xd.applogin(dzName,dzPassword);
            JSONObject data = xd.Task_list(0, 50, null);
            JSONArray list = data.getJSONArray("list");
            if(list.size()==0){
                logger.info("该用户没有待处理事项");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                JSONObject list1 = list.getJSONObject(i);

                JSONObject detail=xd.taskDetail();  //查看门店详情
                int handle_tasks=detail.getInteger("handle_tasks");  //完成代办数
                int tasks=detail.getInteger("tasks");  //代办事项数
                logger.info("店长处理 待办事项前的 已完成待办数：{}，代办事项数{}",handle_tasks,tasks);

                String task_type1= list1.getString("task_type");
                String time = list1.getString("time").substring(0,10);
                long shop_id = list1.getLong("shop_id");
                String responsors=xd.responsors(shop_id).getJSONArray("list").getJSONObject(0).getString("name");  //门店负责人
                Integer reset = 0;//重新开始巡店
                Long task_id = list1.getLong("id");
                if (task_type1.equals(task_type)) {
                    String comment3="定检、现场、远程、复检不合格处理@#￥%……&*（——+~·";
                    String pic_data=getPicList(filepath);
                    ArrayList<String> pic_list=new ArrayList<String>();
                    pic_list.add(pic_data);
                    xd.stepSubmit(shop_id,task_id,comment3,pic_list);
                } else {
                    continue;
                }
                JSONObject detailAfter=xd.taskDetail();
                int handle_tasksAfter=detailAfter.getInteger("handle_tasks");
                int tasksAfter=detailAfter.getInteger("tasks"); //代办事项数
                logger.info("店长处理待办事项后 已完成代办数：{}，代办事项数：{}",handle_tasksAfter,tasksAfter);
                Preconditions.checkArgument((handle_tasksAfter-handle_tasks)==1,"店长处理待办事项，已完成待办事项没+1");
                Preconditions.checkArgument((tasks-tasksAfter)==1,"店长处理待办事项后，待办事项总数没-1");

                //巡检员 代办事项数 +1
                xd.applogin(adminNamex,adminPasswdx);
                JSONObject handleAfter=xd.taskDetail();
                int tasksAfterXJY=handleAfter.getInteger("tasks");
                logger.info("处理查看结果后，店长待办事项数：{}",tasksAfterXJY);
                Preconditions.checkArgument((tasksAfterXJY-tasksNum)==1,"店长处理待办事项后，巡检员待办事项总数没+1");
                break;       //为调试代码只循环一次，处理一个待办事项

            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("处理定检、远程、现场巡店、复检不合格，主账号增加1个【查看结果】，减少一个待处理事项");
        }
    }

    /**
     * @description :4.执行定检任务，不合格，店长待办事项增加 ok  适用于主账号
     * @date :2020/6/27 17:41
     **/
    @Test
    public void daibanNOThingAdd() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            xd.applogin(adminName,adminPasswd);
            //获取待办实现未完成列表 0 待办 1已完成
            JSONObject data = xd.Task_list(0, 10, null);
            JSONArray list = data.getJSONArray("list");
            String comment="app定检巡店不合格";
            int num=0;       //定检巡店不合格项数
            if(list.size()==0){
                logger.info("该用户没有待处理事项");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                JSONObject detail=xd.taskDetail();  //查看门店详情
                int handle_tasks=detail.getInteger("handle_tasks");  //完成代办数
                int tasks=detail.getInteger("tasks");  //代办事项数

                JSONObject list1 = list.getJSONObject(i);
                String task_type = list1.getString("task_type");
                String time = list1.getString("time").substring(0,10);

                long shop_id = list1.getLong("shop_id");
                Integer reset = 0;//重新开始巡店
                Long task_id = list1.getLong("id");
                if (task_type.equals("SCHEDULE_TASK")) {
                    //第一个待办事项若是定检任务，则执行定检任务，开始巡店
                    String task_type1="SCHEDULED";
                    num=xundianappNO(shop_id, comment,task_type1, task_id,time);
                } else {
                    continue;
                }
                JSONObject detail2=xd.taskDetail();  //查看门店详情
                int tasksA=detail2.getInteger("tasks");  //代办事项数

                Preconditions.checkArgument((tasksA-tasks)==(num-1),"定检巡店不合格项数与店长待办事项增加项数不符");
                break;       //为调试代码只循环一次，处理一个待办事项
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("执行定检任务，不合格，店长待办事项增加");
        }
    }
    /**
     * @description :5.执行定检任务，不合格，店长待办事项增加  适用于已知巡检员和店长 ok
     * @date :2020/6/27 17:41
     **/
    @Test
    public void daibanNOThingAdd1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //1.店长登录，记录原始待办事项数

            xd.applogin(dzName,dzPassword);
           int tasksNum=xd.taskDetail().getInteger("tasks");
           logger.info("处理定检任务前店长待办事项数：{}",tasksNum);

           xd.applogin(adminNamex,adminPasswdx);
            //2.巡检员登录，进行一次定检巡检，记录不合格项数
            //获取待办实现未完成列表 0 待办 1已完成
            JSONObject data = xd.Task_list(0, 10, null);
            JSONArray list = data.getJSONArray("list");
            String comment="app定检巡店不合格";
            int num=0;       //定检巡店不合格项数
            if(list.size()==0){
                logger.info("该用户没有待处理事项");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                JSONObject list1 = list.getJSONObject(i);
                String task_type = list1.getString("task_type");
                String time = list1.getString("time").substring(0,10);

                long shop_id = list1.getLong("shop_id");
                Integer reset = 0;//重新开始巡店
                Long task_id = list1.getLong("id");
                if (task_type.equals("SCHEDULE_TASK")) {
                    //第一个待办事项若是定检任务，则执行定检任务，开始巡店
                    String task_type1="SCHEDULED";
                    num=xundianappNO(shop_id, comment,task_type1, task_id,time);
                } else {
                    continue;
                }
                xd.applogin(dzName,dzPassword);
                int tasksNumAfter=xd.taskDetail().getInteger("tasks");
                logger.info("处理定检任务后店长待办事项数：{}",tasksNumAfter);
                Preconditions.checkArgument((tasksNumAfter-tasksNum)==num,"定检巡店不合格项数与店长待办事项增加项数不符");
                break;       //为调试代码只循环一次，处理一个待办事项
            }
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("执行定检任务，不合格，店长待办事项增加");
        }
    }

    /**
     * @description :6.查看处理结果，合格，不产生待办事项 --巡检员代办-1，店长代办不变 ok
     * @date :2020/6/28 14:51  TODO:
     **/
    @Test
    public void checkDealResultY(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //1.店长登录，记录原始待办事项数
            xd.applogin(dzName,dzPassword);
            int tasksNum=xd.taskDetail().getInteger("tasks");
            logger.info("处理定检任务前店长待办事项数：{}",tasksNum);

            //2.巡检员登录
            xd.applogin(adminNamex,adminPasswdx);
            JSONObject data = xd.Task_list(0, 10, null);
            JSONArray list = data.getJSONArray("list");
            if(list.size()==0){
                logger.info("该用户没有待处理事项");
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                JSONObject list1 = list.getJSONObject(i);

                JSONObject detail=xd.taskDetail();  //查看门店详情
                int handle_tasks=detail.getInteger("handle_tasks");  //完成代办数
                int tasks=detail.getInteger("tasks");  //代办事项数
                logger.info("巡检员处理 【查看处理结果前的 已完成待办数：{}，代办事项数{}",handle_tasks,tasks);

                String task_type = list1.getString("task_type");
                String time = list1.getString("time").substring(0,10);
                long shop_id = list1.getLong("shop_id");
                //门店负责人
                String responsors=xd.responsors(shop_id).getJSONArray("list").getJSONObject(0).getString("name");
                Integer reset = 0;//重新开始巡店
                Long task_id = list1.getLong("id");
                if (task_type.equals("HANDLE_RESULT")) {
                    String comment1="处理结果合格";
                    xd.stepSubmit2(shop_id,task_id,comment1,0);
                } else {
                    continue;
                }
                JSONObject detailAfter=xd.taskDetail();
                int handle_tasksAfter=detailAfter.getInteger("handle_tasks");//已完成待办事项
                int taskAfters=detailAfter.getInteger("tasks");  //代办事项数
                logger.info("巡检员处理 查看结果后 已完成代办数：{}，代办事项数：{}",handle_tasksAfter,taskAfters);
                Preconditions.checkArgument((handle_tasksAfter-handle_tasks)==1,"巡检员代办事项（查看处理结果）完成，已完成待办事项没+1");
                Preconditions.checkArgument((tasks-taskAfters)==1,"巡检员代办事项（查看处理结果）完成，待办事项没-1");

                //店长 代办事项数 不变
                xd.applogin(dzName,dzPassword);
                JSONObject handleAfter=xd.taskDetail();
                int tasksAfter=handleAfter.getInteger("tasks");
                Preconditions.checkArgument((tasksNum-tasksAfter)==0,"巡检员代办事项（查看处理结果）完成，合格不产生代办事项  --此项不符预期");
                break;       //为调试代码只循环一次，处理一个待办事项

            }
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("查看处理结果，合格，不产生待办事项");
        }
    }

   /**
    * @description :7.查看处理结果，不合格，产生1个待办事项
    * @date :2020/6/28 14:51
    **/
   @Test
   public void checkDealResult(){
       logger.logCaseStart(caseResult.getCaseName());
       try{
           //1.店长登录，记录原始待办事项数
           xd.applogin(dzName,dzPassword);
           int tasksNum=xd.taskDetail().getInteger("tasks");
           logger.info("处理定检任务前店长待办事项数：{}",tasksNum);

           //2.巡检员登录
           xd.applogin(adminNamex,adminPasswdx);
           JSONObject data = xd.Task_list(0, 100, null);
           JSONArray list = data.getJSONArray("list");
           if(list.size()==0){
               logger.info("该用户没有待处理事项");
               return;
           }
           for (int i = 0; i < list.size(); i++) {
               JSONObject list1 = list.getJSONObject(i);

               JSONObject detail=xd.taskDetail();  //查看门店详情
               int handle_tasks=detail.getInteger("handle_tasks");  //完成代办数
               int tasks=detail.getInteger("tasks");  //代办事项数
               logger.info("巡检员处理 【查看处理结果前的 已完成待办数：{}，代办事项数{}",handle_tasks,tasks);

               String task_type = list1.getString("task_type");
               String time = list1.getString("time").substring(0,10);
               long shop_id = list1.getLong("shop_id");
               //门店负责人
               String responsors=xd.responsors(shop_id).getJSONArray("list").getJSONObject(0).getString("name");
               Integer reset = 0;//重新开始巡店
               Long task_id = list1.getLong("id");
               if (task_type.equals("HANDLE_RESULT")) {
                   String comment1="处理结果不合格";
                   xd.stepSubmit2(shop_id,task_id,comment1,1);
               } else {
                  continue;
               }
               JSONObject detailAfter=xd.taskDetail();
               int handle_tasksAfter=detailAfter.getInteger("handle_tasks");  //完成代办数
               int tasksAfters=detailAfter.getInteger("tasks");   //代办事项数
               logger.info("巡检员处理 查看结果后 已完成代办数：{}，代办事项数：{}",handle_tasksAfter,tasksAfters);
               Preconditions.checkArgument((handle_tasksAfter-handle_tasks)==1,"巡检员代办事项（查看处理结果）完成，已完成待办事项没+1");
               Preconditions.checkArgument((tasks-tasksAfters)==1,"巡检员代办事项（查看处理结果）完成，待办事项没-1");

               //店长 代办事项数 +1
               xd.applogin(dzName,dzPassword);
               JSONObject handleAfter=xd.taskDetail();
               int tasksAfter=handleAfter.getInteger("tasks");
               logger.info("处理查看结果后，店长待办事项数：{}",tasksAfter);
               Preconditions.checkArgument((tasksAfter-tasksNum)==1,"巡检员代办事项（查看处理结果）完成，不合格产生1待办事项  --此项不符预期");
               break;       //为调试代码只循环一次，处理一个待办事项

           }
       }catch (AssertionError e){
           appendFailreason(e.toString());
       }catch (Exception e){
           appendFailreason(e.toString());
       }finally {
           saveData("巡检员代办事项（查看处理结果）完成，不合格 产生1待办事项");
       }
   }

    /**
     * @description :8.巡检员3 现场巡检，不合格，店长3处理，数据信息校验：1.店长待办事项中信息校验  2.店长已完成待办事项数据校验 ok
     * @date :2020/6/28 14:51
     **/
    @Test
    public void checkDealResultNo(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //巡检员3现场巡店
            String checkType="SPOT";
            String comment="现场巡店不合格";
            int unPassNum=appXundianP(shop_idX,checkType,comment,2);
            //记录巡店时间，即现在的时间
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date=new Date();
            String time=dateFormat.format(date).substring(0,14);

            //店长登录
            xd.login(dzName,dzPassword);
            JSONObject data = xd.Task_list(0, 10, null);
            JSONArray list = data.getJSONArray("list");
            String audit_comment=list.getJSONObject(0).getString("audit_comment");
            String time1=list.getJSONObject(0).getString("time").substring(0,14);
            Preconditions.checkArgument(audit_comment.equals(comment),"审核意见与巡店时填写不一致");
            Preconditions.checkArgument(time1.equals(time),"待办事项时间与巡店时间不一致");
            //处理其中一个待办事项
            Long task_id = list.getJSONObject(0).getLong("id");
            String comment3="处理待办事项，现场巡店不合格";
            xd.stepSubmit(shop_idX,task_id,comment3);// 处理时间是系统时间

            //查看店长已完成待办并校验信息
            JSONObject data2 = xd.Task_list(1, 10, null);
            String audit_commentAfter=data2.getJSONArray("list").getJSONObject(0).getString("audit_comment");
            String stepComment=data2.getJSONArray("list").getJSONObject(0).getJSONArray("step_list").getJSONObject(0).getString("comment");
            Preconditions.checkArgument(audit_commentAfter.equals(comment),"已完成事项中审核意见显示不一致");
            Preconditions.checkArgument(stepComment.equals(comment3),"已完成事项中处理说明显示不一致");

            //巡检员登录，查看处理事项不合格
//            xd.applogin(adminNamex,adminPasswdx);
//            JSONObject data2 = xd.Task_list(0, 10, null);

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("查看处理结果，不合格，产生1个待办事项");
        }
    }



    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();



        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "xx";
//
//
//        //replace backend gateway url
//        //commonConfig.gateway = "";
//
//        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "巡店 日常");

        //replace ding push conf
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getXunDianShop(); //这里要改！！！！！
        beforeClassInit(commonConfig);

        logger.debug("xundian " + xd);


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     *
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    @BeforeClass
    public void login() {
        xd.applogin(adminNamex,adminPasswdx);
    }

}
