package com.haisheng.framework.testng.bigScreen.jiaochen.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.jiaoChenInfo;
import com.haisheng.framework.testng.bigScreen.jiaochen.xmf.PublicParm;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;


/**
 * @author : lxq
 * @date :  2020/11/24
 */

public class IgnoreCase extends TestCaseCommon implements TestCaseStd {


    ScenarioUtil jc = ScenarioUtil.getInstance();
    jiaoChenInfo info = new  jiaoChenInfo();
    PublicParm pp = new PublicParm();
    String filePath = "src/main/java/com/haisheng/framework/testng/bigScreen/jiaochen/wm/multimedia/picture/奔驰.jpg";

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
        commonConfig.checklistQaOwner = "吕雪晴";

        commonConfig.product = EnumTestProduce.JC_DAILY.getAbbreviation();
        //replace backend gateway url
        //commonConfig.gateway = "";

        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, EnumTestProduce.JC_DAILY.getDesc() + commonConfig.checklistQaOwner);

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        //commonConfig.shopId = "-1";
        commonConfig.shopId = "45973";
        beforeClassInit(commonConfig);

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

        jc.pcLogin("15711300001","000000");
    }

    //新建活动 V2.0 取消
    @Test(dataProvider = "ACTIVITY")
    public void addActivity(String title, String pic_type,  String content, String label,String start,String end,String quota,String address,String maintain,String voucher,String type, String day) {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            JSONArray pic_list2 =new JSONArray();
            pic_list2.add("general_temp/367611e8-96a4-4fed-9e58-e869f459fbe2");
            pic_list2.add("general_temp/367611e8-96a4-4fed-9e58-e869f459fbe2");
            pic_list2.add("general_temp/367611e8-96a4-4fed-9e58-e869f459fbe2");
            JSONArray vou_list =new JSONArray();
            vou_list.add(jc.pcVoucherList().getJSONArray("list").getJSONObject(0).getInteger("id"));

            if (day.equals(0)){
                JSONObject obj = jc.addArticleNotChk(title,pic_type,pic_list2,content,label,"ACTIVITY",start,end,start,
                        end,Integer.valueOf(quota),address,Boolean.valueOf(maintain),Boolean.valueOf(voucher),vou_list,
                        type,start,end,null);
                int code = obj.getInteger("code");
                Long id = obj.getJSONObject("data").getLong("id");
                //关闭活动
                jc.changeArticleStatus(id);
                Preconditions.checkArgument(code==1000,"期待1000，实际"+ code);
            }
            else {
                JSONObject obj = jc.addArticleNotChk(title,pic_type,pic_list2,content,label,"ACTIVITY",start,end,start,
                        end,Integer.valueOf(quota),address,Boolean.valueOf(maintain),Boolean.valueOf(voucher),vou_list,
                        type,null,null,Integer.valueOf(day));
                int code = obj.getInteger("code");
                Long id = obj.getJSONObject("data").getLong("id");
                //关闭活动
                jc.changeArticleStatus(id);
                Preconditions.checkArgument(code==1000,"期待1000，实际"+ code);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC【内容运营】，新建活动3张图");
        }
    }
    @DataProvider(name = "ACTIVITY")
    public  Object[] activity() {
        return new String[][]{
                {"1234", "THREE",info.stringone, "RED_PAPER",dt.getHistoryDate(0),dt.getHistoryDate(100),"1","啊","false","true","SIGN_UP","0"},
                {info.stringten, "THREE",info.stringfifty, "PREFERENTIAL",dt.getHistoryDate(0),dt.getHistoryDate(110),"100",info.stringfifty,"true","true","SIGN_UP","1"},
                {info.string20, "THREE",info.stringten, "BARGAIN",dt.getHistoryDate(0),dt.getHistoryDate(365),"100000000",info.stringsix,"true","true","SIGN_UP","2000"},
                {info.stringten, "THREE",info.stringlong, "WELFARE",dt.getHistoryDate(0),dt.getHistoryDate(364),"999",info.stringten,"false","true","ARTICLE_BUTTON","1000"},
                {info.stringsix, "THREE",info.stringlong, "GIFT",dt.getHistoryDate(0),dt.getHistoryDate(62),"10000",info.stringfifty,"false","true","ARTICLE_BUTTON","50"}
        };
    }



}
