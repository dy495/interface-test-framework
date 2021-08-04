package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.general.GeneralEnumValueListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.VoiceDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.VoiceEvaluationPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.sensitivewords.SensitiveWordsLabelListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.sensitivewords.SensitiveBehaviorDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.sensitivewords.SensitiveBehaviorPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 内容运营
 *
 * @author lxq
 * @date 2021/1/29 11:17
 */
public class HuiTing_DataCase extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct PRODUCE = EnumTestProduct.YT_DAILY_ZH;
    EnumAccount ALL_AUTHORITY = EnumAccount.YT_ALL_DAILY;
    VisitorProxy visitor = new VisitorProxy(PRODUCE);
    SceneUtil businessUtil = new SceneUtil(visitor);

    YunTongInfo info = new YunTongInfo();

    CommonConfig commonConfig = new CommonConfig();


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");

        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = "吕雪晴";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = PRODUCE.getShopId();
        commonConfig.roleId = ALL_AUTHORITY.getRoleId();
        beforeClassInit(commonConfig);
        businessUtil.loginPc(ALL_AUTHORITY);

        visitor.setProduct(EnumTestProduct.YT_DAILY_GK);  //会听模块
    }



    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {

        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);

    }


    /**
     * --------------------------------- 语音评鉴列表 ---------------------------------
     */

    @Test
    public void voiceEvaluationIn1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {//这个枚举0 要改
            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).build().invoke(visitor).getJSONArray("list");
            if (arr1.size() > 0) {
                JSONObject obj = arr1.getJSONObject(0);
                String list_receptor_name = obj.getString("receptor_name");
                String list_reception_time = obj.getString("reception_time").substring(0,10);
//                String list_reception_duration = obj.getString("reception_duration");
                String list_customer_name = obj.getString("customer_name");
                String list_customer_phone = obj.getString("customer_phone");
                Long id = obj.getLong("id");

                JSONObject detailObj = VoiceDetailScene.builder().id(id).build().invoke(visitor);
                String detail_receptor_name = detailObj.getString("receptor_name");
                String detail_reception_time = detailObj.getString("start_time").substring(0, 10);
//                String detail_reception_duration = detailObj.getString("reception_duration");
                String detail_customer_name = detailObj.getString("name");
                String detail_customer_phone = detailObj.getString("phone");

                Preconditions.checkArgument(list_receptor_name.equals(detail_receptor_name), "列表中接待顾问姓名=" + list_receptor_name + " ,详情中接待顾问姓名=" + detail_receptor_name);
                Preconditions.checkArgument(list_reception_time.equals(detail_reception_time), "列表中接待日期=" + list_reception_time + " ,详情中接待日期=" + detail_reception_time);
//                Preconditions.checkArgument(list_reception_duration.equals(detail_reception_duration), "列表中接待时长=" + list_reception_duration + " ,详情中接待时长=" + detail_reception_duration);
                Preconditions.checkArgument(list_customer_name.equals(detail_customer_name), "列表中客户姓名=" + list_customer_name + " ,详情中客户姓名=" + detail_customer_name);
                Preconditions.checkArgument(list_customer_phone.equals(detail_customer_phone), "列表中客户电话=" + list_customer_phone + " ,详情中客户电话=" + detail_customer_phone);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表与详情页客户信息一致");
        }
    }

    @Test
    public void voiceEvaluationDetailIn1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(20).evaluateStatus(500).build().invoke(visitor).getJSONArray("list");
            if (arr1.size() > 0) {
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj = arr1.getJSONObject(i);
                    Long id = obj.getLong("id");
                    JSONObject detailObj = VoiceDetailScene.builder().id(id).build().invoke(visitor);
                    int average_score = detailObj.getInteger("average_score");
                    int score = 0;
                    JSONArray scores = detailObj.getJSONArray("scores");
                    for (int j = 0; j < scores.size(); j++) {
                        score = score + scores.getJSONObject(j).getInteger("score");
                    }
                    int jisuan = Math.round(score / 5);
                    Preconditions.checkArgument(average_score <= jisuan+1 &&average_score >= jisuan-1 , "接待平均分" + average_score + " != 环节计算结果" + jisuan);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴详情，接待得分=各环节得分之和/5 四舍五入");
        }
    }

    @Test
    public void voiceEvaluationDetailIn2() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(20).evaluateStatus(500).build().invoke(visitor).getJSONArray("list");
            if (arr1.size() > 0) {
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj = arr1.getJSONObject(i);
                    Long id = obj.getLong("id");
                    JSONObject detailObj = VoiceDetailScene.builder().id(id).build().invoke(visitor);
                    List lable = new ArrayList<>();
                    JSONArray link_label_list = detailObj.getJSONArray("link_label_list");
                    for (int j = 0; j < link_label_list.size(); j++) { //取出 link_label_list 中每一个置灰的标签
                        JSONArray link_labels = link_label_list.getJSONObject(j).getJSONArray("labels");
                        for (int k = 0; k < link_labels.size(); k++) {
                            if (!link_labels.getJSONObject(k).getBoolean("is_hit"))
                                lable.add(link_labels.getJSONObject(k).getString("lable"));
                        }
                    }
                    JSONArray advice_list = detailObj.getJSONArray("advice_list");
                    for (int j = 0; j < advice_list.size(); j++) {
                        String advice_lable = advice_list.getJSONObject(j).getString("lable");
                        Preconditions.checkArgument(lable.contains(advice_lable), "记录ID" + id + "的建议标签" + advice_lable + "未在置灰标签内");
                    }
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴详情，建议中的环节内容 是 各环节得分标签中 置灰环节 的子集");
        }
    }

    //@Test
    public void voiceEvaluationDetailIn3() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(20).evaluateStatus(500).build().invoke(visitor).getJSONArray("list");
            if (arr1.size() > 0) {
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj = arr1.getJSONObject(i);
                    Long id = obj.getLong("id");
                    JSONObject detailObj = VoiceDetailScene.builder().id(id).build().invoke(visitor);
                    //展示的各环节分数
                    HashMap<String, String> showscore = new HashMap<>();
                    JSONArray scores = detailObj.getJSONArray("scores");
                    for (int j = 0; j < scores.size(); j++) {
                        JSONObject obj1 = scores.getJSONObject(j);
                        showscore.put(obj1.getString("type"), obj1.getString("score"));
                    }
                    HashMap<String, String> jisuanscore = new HashMap<>();

                    //根据置灰标签计算出来的各环节分数
                    JSONArray link_label_list = detailObj.getJSONArray("link_label_list");
                    for (int j = 0; j < link_label_list.size(); j++) {
                        int count = 0;
                        JSONArray link_labels = link_label_list.getJSONObject(j).getJSONArray("labels");
                        for (int k = 0; k < link_labels.size(); k++) {
                            if (link_labels.getJSONObject(k).getBoolean("is_hit"))
                                count++;
                        }
                        jisuanscore.put(link_label_list.getJSONObject(j).getString("type"), Integer.toString(Math.round(count * 100 / link_labels.size())));
                    }
                    //两个集合比较
                    for (String key : showscore.keySet()) {
                        Preconditions.checkArgument(showscore.get(key).equals(jisuanscore.get(key)), key + "环节中，PC展示分数" + showscore.get(key) + " != 根据标签计算出的分数" + jisuanscore.get(key));
                    }
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴详情，各环节得分 约等于 该环节的 高亮标签数/总标签数 * 100");
        }
    }


    /**
     * --------------------------------- 敏感词风控 ---------------------------------
     */

    //@Test
    //todo 要改的
    public void sensitiveIn1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = GeneralEnumValueListScene.builder().enumType("SENSITIVE_WORDS_TYPES").build().invoke(visitor).getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                int evaluate_status = arr.getJSONObject(i).getInteger("key");
                String evaluate_status_name = arr.getJSONObject(i).getString("value"); //各敏感词类别

                int lablecount = 0; // 标签数量
                JSONArray lablearray = SensitiveWordsLabelListScene.builder().sensitiveWordsType(evaluate_status).build().invoke(visitor).getJSONArray("list");
                for (int j = 0; j < lablearray.size(); j++) {
                    lablecount += lablearray.getJSONObject(j).getInteger("count");
                }

                int listcount = 0; //列表中每个记录涉及到敏感词的数量
                int total = SensitiveBehaviorPageScene.builder().page(1).size(1).sensitiveWordsType(evaluate_status).build().invoke(visitor).getInteger("total");
                if (total <= 100) {
                    JSONArray listarray = SensitiveBehaviorPageScene.builder().page(1).size(100).sensitiveWordsType(evaluate_status).build().invoke(visitor).getJSONArray("list");
                    for (int k = 0; k < listarray.size(); k++) {
                        Long id = listarray.getJSONObject(k).getLong("id");
                        listcount += SensitiveBehaviorDetailScene.builder().id(id).build().invoke(visitor).getJSONArray("sensitive_words").size();
                    }
                } else {
                    for (int j = 1; j < total / 100 + 1; j++) {
                        JSONArray listarray = SensitiveBehaviorPageScene.builder().page(j).size(100).sensitiveWordsType(evaluate_status).build().invoke(visitor).getJSONArray("list");
                        for (int k = 0; k < listarray.size(); k++) {
                            Long id = listarray.getJSONObject(k).getLong("id");
                            listcount += SensitiveBehaviorDetailScene.builder().id(id).build().invoke(visitor).getJSONArray("sensitive_words").size();
                        }
                    }
                }
                Preconditions.checkArgument(lablecount == listcount, "敏感词类别：" + evaluate_status_name + " 在柱状图中数量" + lablecount + " != 列表中计算结果" + listcount);


            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {

            appendFailReason(e.toString());
        } finally {
            saveData("敏感词风控审核柱状图中类别数量与记录详情中数量一致");
        }
    }

    /**
     * --------------------------------- 特殊音频审核 ---------------------------------
     */


}
